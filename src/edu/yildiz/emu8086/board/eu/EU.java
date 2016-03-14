
package edu.yildiz.emu8086.board.eu;

import edu.yildiz.emu8086.board.BIU;
import edu.yildiz.emu8086.board.eu.islem.AluIslemi;
import edu.yildiz.emu8086.board.eu.islem.AtlamaIslemi;
import edu.yildiz.emu8086.board.eu.islem.AtlamaIslemi.Kosul;
import edu.yildiz.emu8086.board.eu.islem.BayrakIslemi;
import edu.yildiz.emu8086.board.eu.islem.Islem;
import edu.yildiz.emu8086.board.eu.islem.OzelIslem;
import edu.yildiz.emu8086.board.eu.islem.VeriTransferIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.Komut;
import edu.yildiz.emu8086.derleyici.asm.komutlar.HLT;
import edu.yildiz.emu8086.derleyici.asm.operand.AluOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.FlagOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.ImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import edu.yildiz.emu8086.derleyici.asm.operand.RegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordImmediateOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.FlagRegister;
import edu.yildiz.emu8086.tipler.Memory;
import edu.yildiz.emu8086.tipler.Word;
import edu.yildiz.emu8086.tipler.Yazmac;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Execution Unit
 * 
 * boş kaldığında instruction kuyruğundaki bytedan itibaren
 * almaya başla.
 * 
 * jump veya call komutlarında kuyruk yeniden oluşturulmalı.
 * 
 * @author MMC
 */
public class EU implements Runnable
{
    public enum CalismaModu {Sirali, TekKomut};
    
    public static final Logger kutukcu = Logger.getLogger(EU.class.getSimpleName());
    private PropertyChangeSupport yazmacBildirici  = new PropertyChangeSupport(this);
    private PropertyChangeSupport olayBildirici  = new PropertyChangeSupport(this);
    
    /**
     * Singleton alan
     */
    private static EU eu;
    
    //Yazmaçlar
    private Word ax;
    private Word bx;
    private Word cx;
    private Word dx;
    private FlagRegister flags;
    private Word stackPointer;
    private Word basePointer;
    private Word destinationIndex;
    private Word sourceIndex;
    private ALU alu;
    
    private int uyumaSuresi = 1000;
    
    private boolean animasyonCalisiyorMu = true;
    private CalismaModu calismaModu = CalismaModu.Sirali;
    private boolean adim = true;
    
    protected EU()
    {
        this.ax = new Word();
        this.bx = new Word();
        this.cx = new Word();
        this.dx = new Word();
        
        this.flags = new FlagRegister();
        
        this.stackPointer = new Word();
        this.basePointer = new Word();
        this.destinationIndex = new Word();
        this.sourceIndex = new Word();
        
        this.alu = new ALU(this.flags);
    }
    
    public static EU get()
    {
        if (eu == null)
            eu = new EU();
        
        return eu;
    }
    
    @Override
    public void run()
    {
        kutukcu.info("Başladı");
        
        while (animasyonCalisiyorMu)
        {   
            Komut komut = BIU.get().getInstructionQueue().al();
            
            if (komut instanceof HLT)
                break;
            
            ninniSoyledeUyuyalim();
            InstructionDecoder.decode(komut);
                    
            kutukcu.info("Komut çalıştırılıyor...");
            ninniSoyledeUyuyalim();
            
            komut.getIslemler().stream().forEach((islem) ->
            {
                this.islemTurunuTesbitEtVeCalistir(islem);
            });
            
            adim = false;
            this.olayBildirici.firePropertyChange("komutsonu", null, null);
            
            if (this.calismaModu == CalismaModu.TekKomut)
            {
                while (adim == false)
                {
                    this.ninniSoyledeUyuyalim(500);
                }
            }
        }
        
        BIU.get().setAnimasyonCalisiyorMu(false);
        
        kutukcu.info("Sonlandı");
    }
    
    private void ninniSoyledeUyuyalim()
    {
        this.ninniSoyledeUyuyalim(this.uyumaSuresi);
    }
    
    private void ninniSoyledeUyuyalim(int milisaniye)
    {
        try
        {
            Thread.sleep(milisaniye);
        } 
        catch (InterruptedException ex)
        {
            Logger.getLogger(BIU.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reset()
    {
        yazmacYazWord(Yazmac.AX, new Word());
        yazmacYazWord(Yazmac.BX, new Word());
        yazmacYazWord(Yazmac.CX, new Word());
        yazmacYazWord(Yazmac.DX, new Word());
        yazmacYazWord(Yazmac.SP, new Word());
        yazmacYazWord(Yazmac.BP, new Word());
        yazmacYazWord(Yazmac.DI, new Word());
        yazmacYazWord(Yazmac.SI, new Word());
        
        this.animasyonCalisiyorMu = true;
        this.calismaModu = CalismaModu.Sirali;
        this.adim = true;
        
        this.flags.reset();
    }
   
    public void basla()
    {
        
    }

    private void islemTurunuTesbitEtVeCalistir(Islem islem)
    {
        if (islem instanceof VeriTransferIslemi)
        {
            VeriTransferIslemi transfer = (VeriTransferIslemi)islem;
            Object deger = this.kaynagiTesbitEtVeOku(transfer.getKaynak());
            this.hedefiTesbitEtVeYaz(transfer.getHedef(), deger);
        }
        else if (islem instanceof AluIslemi)
        {
            AluIslemi aluKomutu = (AluIslemi) islem;

            alu.setSagOperand(kaynagiTesbitEtVeOku(aluKomutu.getSag()));
            alu.setSolOperand(kaynagiTesbitEtVeOku(aluKomutu.getSol()));

            alu.islem(aluKomutu.getAluIslemTuru(), aluKomutu.getSonucBoyutu());
        }
        else if (islem instanceof BayrakIslemi)
        {
            BayrakIslemi bayrakKomutu = (BayrakIslemi) islem;

            if (bayrakKomutu.getDeger() != null)
                flags.setFlag(bayrakKomutu.getBayrak(), bayrakKomutu.getDeger());
            else
                flags.setFlag(bayrakKomutu.getBayrak(), !flags.getFlag(bayrakKomutu.getBayrak()));
        }
        else if (islem instanceof OzelIslem)
        {
            OzelIslem ozelKomut = (OzelIslem) islem;

            switch (ozelKomut.getKomutAdi())
            {
                case "CBW":
                    yazmacYazWord(Yazmac.AX, new Word(yazmacOkuBayt(Yazmac.AL)));
                    break;
                case "CWD":
                    Word dx = yazmacOkuWord(Yazmac.AX).negatifMi() ? new Word(0xff, 0xff) : new Word();
                    yazmacYazWord(Yazmac.DX, dx);
                default:    
            }
        }
        else if (islem instanceof AtlamaIslemi)
        {
            AtlamaIslemi atlamaIslemi = (AtlamaIslemi) islem;

            boolean sonuc = false;   

            for (Kosul kosul : atlamaIslemi.getKosullar())
            {   
                Object sol = (byte)0;
                Object sag = (byte)0;
                int solTaraf;
                int sagTaraf;

                if (kosul.getSolTaraf() instanceof Operand)
                    sol = kaynagiTesbitEtVeOku((Operand)kosul.getSolTaraf());
                else if (kosul.getSolTaraf() instanceof Byte)
                    sol = kosul.getSolTaraf();

                if (kosul.getSagTaraf() instanceof Operand)
                    sag = kaynagiTesbitEtVeOku((Operand)kosul.getSagTaraf());
                else if (kosul.getSagTaraf() instanceof Byte)
                    sag = kosul.getSagTaraf();

                if (sol instanceof Word)
                    solTaraf = ((Word)sol).getDegerInt();
                else
                    solTaraf = (int)((byte)sol);

                if (sag instanceof Word)
                    sagTaraf = ((Word)sag).getDegerInt();
                else
                    sagTaraf = (int)((byte)sag);

                if (kosul.getKosul() == AtlamaIslemi.Operator.ESIT)
                    sonuc = sonuc || (sagTaraf == solTaraf);
                else if (kosul.getKosul() == AtlamaIslemi.Operator.ESIT_DEGIL)
                    sonuc = sonuc || (sagTaraf != solTaraf);
            }

            //Eğer sonuç sağlanıyorsa
            //veya atlama koşulsuz ise (JMP)
            if (sonuc || atlamaIslemi.getKosullar().isEmpty())
                BIU.get().zipla(atlamaIslemi.getEtiket());
        }
    }

    private Object kaynagiTesbitEtVeOku(Operand operand)
    {
        if (operand instanceof RegisterOperand)
        {
            if (operand instanceof AluOperand)
                return alu.getAluYazmac();

            RegisterOperand registerOperand = (RegisterOperand) operand;

            if (registerOperand.getYazmac().getBoyut() == VeriUzunlugu.Byte_8)
                return yazmacOkuBayt(registerOperand.getYazmac());
            else
                return yazmacOkuWord(registerOperand.getYazmac());
        }
        else if (operand instanceof FlagOperand)
        {
            FlagOperand flagOperand = (FlagOperand) operand;
            return (byte) (flags.getFlag(flagOperand.getHangiFlag()) ? 1 : 0);
        }
        else if (operand instanceof ImmediateOperand)
        {
            ImmediateOperand immediateOperand = (ImmediateOperand) operand;

            if (immediateOperand instanceof ByteImmediateOperand)
                return ((ByteImmediateOperand) immediateOperand).getImmediate();
            else
                return ((WordImmediateOperand) immediateOperand).getImmediate();
        }
        else if (operand instanceof MemoryOperand)
        {
            MemoryOperand memoryOperand = (MemoryOperand) operand;

            return bellekOku(memoryOperand);//word
        }

        return null;
    }

    private void hedefiTesbitEtVeYaz(Operand operand, Object deger)
    {
        if (operand instanceof RegisterOperand)
        {
            RegisterOperand registerOperand = (RegisterOperand) operand;

            if (deger instanceof Byte)
                yazmacYazBayt(registerOperand.getYazmac(), (byte) deger);
            else if (deger instanceof Word)
                yazmacYazWord(registerOperand.getYazmac(), (Word) deger);
        }
        else if (operand instanceof MemoryOperand)
        {
            MemoryOperand memoryOperand = (MemoryOperand) operand;

            bellekYaz(memoryOperand, deger);
        }
    }

    private Object bellekOku(MemoryOperand memoryOperand)
    {
        //Bellek okuma    
        //BIU memory read cycyle
        kutukcu.info("Bellek oku: ");

        //MemoryOperandın offsetini hesapla
        memoryOperand.setHesaplanmisOffset(offsetHesapla(memoryOperand.getOffset(), 
            memoryOperand.getDisplacement()));
        memoryOperand.setIslemTuru(Memory.IslemTuru.OKU);
        BIU.get().setMemoryOperand(memoryOperand);
        BIU.get().setBellekIslemiGercekle(true);

        //Şimdi BIU.get()'nun az önce gönderdiğimiz adres ve kontrol parametreleriyle
        //belleğe ulaşıp veriyi almasını bekleyelim. Bunu da BIU.get() içerisindeki
        //hangiDeger alanının null olup olmadığını kontrol ederek yapalım.
        while (BIU.get().getBellekIslemiGercekle())
        {
            this.ninniSoyledeUyuyalim(500);
        }

        Object bellek;
        Word w = (Word) BIU.get().getMemoryObject();

        if (memoryOperand.getUzunluk() == VeriUzunlugu.Byte_8)
            bellek = w.getL();
        else
            bellek = w;

        BIU.get().setMemoryObject(null);

        return bellek;
    }

    private void bellekYaz(MemoryOperand memoryOperand, Object deger)
    {
        //Bellek yazma
        //BIU memory write cycyle
        kutukcu.info("Belleğe yaz: ");

        memoryOperand.setHesaplanmisOffset(offsetHesapla(memoryOperand.getOffset(), 
            memoryOperand.getDisplacement()));
        memoryOperand.setIslemTuru(Memory.IslemTuru.YAZ);
        BIU.get().setMemoryOperand(memoryOperand);
        BIU.get().setMemoryObject(deger);
        BIU.get().setBellekIslemiGercekle(true);

        //Şimdi BIU.get()'nun az önce gönderdiğimiz adres, kontrol ve data parametreleriyle
        //belleğe ulaşıp veriyi yazmasını bekleyelim. Bunu da BIU.get() içerisindeki
        //hangiDeger alanının null olup olmadığını kontrol ederek yapalım.

        while (BIU.get().getBellekIslemiGercekle())
        {
            this.ninniSoyledeUyuyalim(500);
        }

        //Belleğe yazma işlemi tamamlanmıştır.
    }

    private Word offsetHesapla(Yazmac[] yazmaclar, ImmediateOperand displacement)
    {
        Word offset = new Word();

        if (yazmaclar != null)
        {
            if (yazmaclar[0] != null)
                offset.topla(yazmacOkuWord(yazmaclar[0]));

            if (yazmaclar[1] != null)
                offset.topla(yazmacOkuWord(yazmaclar[1]));
        }

        if (displacement != null)
        {
            if (displacement instanceof ByteImmediateOperand)
                offset = offset.topla(((ByteImmediateOperand) displacement).getImmediate());
            else
                offset = offset.topla(((WordImmediateOperand) displacement).getImmediate());
        }

        return offset;
    }
    
    public byte yazmacOkuBayt(Yazmac yazmac)
    {
        switch(yazmac)
        {
            case AH:    return ax.getH();
            case AL:    return ax.getL();
            case BH:    return bx.getH();
            case BL:    return bx.getL();
            case CH:    return cx.getH();
            case CL:    return cx.getL();
            case DH:    return dx.getH();
            case DL:    return dx.getL();
            case FLAGH: return flags.getFlags().getH();
            case FLAGL: return flags.getFlags().getL();
            default:    return (byte) 0;
        }
    }
    
    public Word yazmacOkuWord(Yazmac yazmac)
    {
        switch(yazmac)
        {
            case AX:    return ax;
            case BX:    return bx;
            case CX:    return cx;
            case DX:    return dx;
            case SP:    return stackPointer;
            case BP:    return basePointer;
            case SI:    return sourceIndex;
            case DI:    return destinationIndex;
            case CS:    return BIU.get().getCodeSegment();
            case DS:    return BIU.get().getDataSegment();
            case SS:    return BIU.get().getStackSegment();
            case ES:    return BIU.get().getExtraSegment();
            case FLAGX: return flags.getFlags();
            default:    return new Word();
        }
    }
    
    public void yazmacYazBayt(Yazmac yazmac, byte deger)
    {
        switch(yazmac)
        {
            case AH:    ax.setH(deger);  break;
            case AL:    ax.setL(deger);  break;
            case BH:    bx.setH(deger);  break;
            case BL:    bx.setL(deger);  break;
            case CH:    cx.setH(deger);  break;
            case CL:    cx.setL(deger);  break;
            case DH:    dx.setH(deger);  break;
            case DL:    dx.setL(deger);  break;
        }
        
        this.yazmacBildirici.firePropertyChange(yazmac.name(), null, deger);
    }

    public void yazmacYazWord(Yazmac yazmac, Word deger)
    {
        switch(yazmac)
        {
            case AX:    ax.setDeger(deger.getDeger()); break;
            case BX:    bx.setDeger(deger.getDeger()); break;
            case CX:    cx.setDeger(deger.getDeger()); break;
            case DX:    dx.setDeger(deger.getDeger()); break;
            case SP:    stackPointer.setDeger(deger.getDeger()); break;
            case BP:    basePointer.setDeger(deger.getDeger()); break;
            case SI:    sourceIndex.setDeger(deger.getDeger()); break;
            case DI:    destinationIndex.setDeger(deger.getDeger()); break;
            case CS:    BIU.get().getCodeSegment().setDeger(deger.getDeger()); break;
            case DS:    BIU.get().getDataSegment().setDeger(deger.getDeger()); break;
            case SS:    BIU.get().getStackSegment().setDeger(deger.getDeger()); break;
            case ES:    BIU.get().getExtraSegment().setDeger(deger.getDeger()); break;
        }
        
        this.yazmacBildirici.firePropertyChange(yazmac.name(), null, deger);
    }
    
    public void setUyumaSuresi(int uyumaSuresi)
    {
        this.uyumaSuresi = uyumaSuresi;
    }
    
    public void yazmacDegisiklikDinleyiciEkle(PropertyChangeListener listener)
    {
        this.yazmacBildirici.addPropertyChangeListener(listener);
    }
    
    public void olayDegisiklikDinleyiciEkle(PropertyChangeListener listener)
    {
        this.olayBildirici.addPropertyChangeListener(listener);
    }

    public ALU getAlu()
    {
        return alu;
    }

    public void setAnimasyonCalisiyorMu(boolean animasyonCalisiyorMu)
    {
        this.animasyonCalisiyorMu = animasyonCalisiyorMu;
    }

    public void setAdim(boolean adim)
    {
        this.adim = adim;
    }

    public void setCalismaModu(CalismaModu calismaModu)
    {
        this.calismaModu = calismaModu;
    }

    public CalismaModu getCalismaModu()
    {
        return calismaModu;
    }
}
