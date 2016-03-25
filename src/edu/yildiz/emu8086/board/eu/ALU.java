
package edu.yildiz.emu8086.board.eu;

import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.FlagRegister;
import edu.yildiz.emu8086.tipler.Word;
import edu.yildiz.emu8086.util.Donusum;
import edu.yildiz.emu8086.util.Util;
import java.util.logging.Logger;

/**
 * Arithmetic Logic Unit
 * 
 * @author MMC
 */
public class ALU 
{
    private static final Logger kutukcu = Logger.getLogger(ALU.class.getSimpleName());
    
    private Object solOperand;
    private Object sagOperand;
    private Object aluYazmac;
    private final FlagRegister flags;
    private final FlagRegister tmpFlags;
    
    public ALU(FlagRegister flags)
    {
        this.flags = flags;
        this.tmpFlags = new FlagRegister();
    }
    
    public final void reset()
    {
        this.solOperand = null;
        this.sagOperand = null;
        this.aluYazmac = null;
        this.tmpFlags.reset();
    }
    
    public void islem(AluIslemi.Tur tur, VeriUzunlugu sonucBoyutu)
    {
        this.tmpFlags.reset();
        
        switch (tur)
        {
            case ADD: add(sonucBoyutu); break;
            case ADC: adc(sonucBoyutu); break;
            case INC: inc(sonucBoyutu); break;
            case DEC: dec(sonucBoyutu); break;
            case MUL: break;
            case AND: and(sonucBoyutu); break;
            case OR:  or(sonucBoyutu);  break;
            case NOT: not(sonucBoyutu); break;
            case NEG: neg(sonucBoyutu); break;
            case XOR: xor(sonucBoyutu); break;
            case RCL: rcl(); break;
            case RCR: rcr(); break;
            case ROL: rol(); break;
            case ROR: ror(); break;
            default: 
        }
    }
    
    private void genericAdd(VeriUzunlugu sonucBoyutu)
    {
        if (sonucBoyutu == VeriUzunlugu.Byte_8)
        {
            this.aluYazmac = bitToplama((byte)solOperand, (byte)sagOperand);
        }
        else
        {
            Word sonuc;
            
            if (solOperand instanceof Word)
                sonuc = (Word) solOperand;
            else
                sonuc = new Word((byte)solOperand);
            
            Word eklenecek;

            if (sagOperand instanceof Byte)
                eklenecek = new Word((byte)sagOperand);
            else
                eklenecek = (Word) sagOperand;
                
            this.aluYazmac = bitToplama(sonuc, eklenecek);
        }
    }
    
    private void add(VeriUzunlugu sonucBoyutu)
    {
        this.genericAdd(sonucBoyutu);
        
        this.flags.setSignFlag(this.tmpFlags.getSignFlag());
        this.flags.setZeroFlag(this.tmpFlags.getZeroFlag());
        this.flags.setAuxiliaryCarryFlag(this.tmpFlags.getAuxiliaryCarryFlag());
        this.flags.setParityFlag(this.tmpFlags.getParityFlag());
        this.flags.setCarryFlag(this.tmpFlags.getCarryFlag());
    }
    
    private void adc(VeriUzunlugu sonucBoyutu)
    {
        this.genericAdd(sonucBoyutu);
        
        this.solOperand = this.aluYazmac;
        this.sagOperand = this.flags.getCarryFlag() ? (byte)1 : (byte)0;
        
        this.genericAdd(sonucBoyutu);
        
        this.flags.setSignFlag(this.tmpFlags.getSignFlag());
        this.flags.setZeroFlag(this.tmpFlags.getZeroFlag());
        this.flags.setAuxiliaryCarryFlag(this.tmpFlags.getAuxiliaryCarryFlag());
        this.flags.setParityFlag(this.tmpFlags.getParityFlag());
        this.flags.setCarryFlag(this.tmpFlags.getCarryFlag());
    }
    
    private void inc(VeriUzunlugu sonucBoyutu)
    {
        this.sagOperand = (byte)1;
        
        this.genericAdd(sonucBoyutu);
        
        this.flags.setSignFlag(this.tmpFlags.getSignFlag());
        this.flags.setZeroFlag(this.tmpFlags.getZeroFlag());
        this.flags.setAuxiliaryCarryFlag(this.tmpFlags.getAuxiliaryCarryFlag());
        this.flags.setParityFlag(this.tmpFlags.getParityFlag());
    }
    
    private void dec(VeriUzunlugu sonucBoyutu)
    {
        this.sagOperand = (byte)-1;
        
        this.genericAdd(sonucBoyutu);
        
        this.flags.setSignFlag(this.tmpFlags.getSignFlag());
        this.flags.setZeroFlag(this.tmpFlags.getZeroFlag());
        this.flags.setAuxiliaryCarryFlag(this.tmpFlags.getAuxiliaryCarryFlag());
        this.flags.setParityFlag(this.tmpFlags.getParityFlag());
    }
    
    private void not(VeriUzunlugu sonucBoyutu)
    {
        if (sonucBoyutu == VeriUzunlugu.Byte_8)
        {
            this.aluYazmac = (byte)~((byte)this.solOperand);
        }
        else
        {
            Word tmp = (Word)this.aluYazmac;
            this.aluYazmac = new Word((byte)~tmp.getH(), (byte)~tmp.getL());
        }
    }
    
    private void neg(VeriUzunlugu sonucBoyutu)
    {
        if (sonucBoyutu == VeriUzunlugu.Byte_8)
        {
            this.sagOperand = (byte)0;
            this.solOperand = (byte)-((byte)this.solOperand);
        }
        else
        {
            this.sagOperand = new Word((byte)0, (byte)0);
            this.solOperand = ((Word)this.solOperand).ikininKomplementi();
        }
        
        this.add(sonucBoyutu);
    }
    
    private void and(VeriUzunlugu sonucBoyutu)
    {
        and_or_xor(sonucBoyutu, AluIslemi.Tur.AND);
    }
    
    private void or(VeriUzunlugu sonucBoyutu)
    {
        and_or_xor(sonucBoyutu, AluIslemi.Tur.OR);
    }
    
    private void xor(VeriUzunlugu sonucBoyutu)
    {
        and_or_xor(sonucBoyutu, AluIslemi.Tur.XOR);
    }
    
    private void and_or_xor(VeriUzunlugu sonucBoyutu, AluIslemi.Tur tur)
    {
        boolean sign;
        boolean zero;
        boolean parity;
        
        if (sonucBoyutu == VeriUzunlugu.Byte_8)
        {
            byte alu = 0;
            
            if (tur == AluIslemi.Tur.AND)
                alu = (byte)(((byte)this.sagOperand) & ((byte)this.solOperand));
            else if (tur == AluIslemi.Tur.OR)
                alu = (byte)(((byte)this.sagOperand) | ((byte)this.solOperand));
            else if (tur == AluIslemi.Tur.XOR)
                alu = (byte)(((byte)this.sagOperand) ^ ((byte)this.solOperand));
            
            this.aluYazmac = alu;
            
            sign = alu > 0;
            zero = alu == 0;
            parity = Integer.bitCount(Byte.toUnsignedInt(alu)) % 2 == 0;
        }
        else
        {
            Word sag = (Word)this.sagOperand;
            Word sol = (Word)this.solOperand;
            
            Word alu = new Word();
            
            if (tur == AluIslemi.Tur.AND)
                alu = new Word(sag.getH() & sol.getH(), sag.getL() & sag.getL());
            else if (tur == AluIslemi.Tur.OR)
                alu = new Word(sag.getH() | sol.getH(), sag.getL() | sag.getL());
            else if (tur == AluIslemi.Tur.XOR)
                alu = new Word(sag.getH() ^ sol.getH(), sag.getL() ^ sag.getL());
            
            this.aluYazmac = alu;
            
            sign = !alu.negatifMi();
            zero = alu.sifirMi();
            parity = alu.birBitSayisi() % 2 == 0;
        }
        
        this.flags.clearOverflowFlag();
        this.flags.clearCarryFlag();
        this.flags.setSignFlag(sign);
        this.flags.setZeroFlag(zero);
        this.flags.setParityFlag(parity);
    }
    
    private void rcl()
    {
        int kere = ((int) this.sagOperand);
        
        for (int i = 0; i < kere; i++)
        {
            boolean msb = Util.enDegerliBit(this.solOperand);
            
            this.solOperand = shiftLeft(this.solOperand);
            
            if (this.solOperand instanceof Byte)
                this.solOperand = Util.bitSet((byte)this.solOperand, 1, this.flags.getCarryFlag());
            else
                ((Word)this.solOperand).bitSet(1, this.flags.getCarryFlag());
            
            this.flags.setCarryFlag(msb);
        }
        
        if (kere == 1)
            if (Util.enDegerliBit(this.solOperand) != this.flags.getCarryFlag())
                this.flags.setOverflowFlag();
            else
                this.flags.clearOverflowFlag();
    }
    
    private void rcr()
    {
        int kere = ((int) this.sagOperand);
        
        for (int i = 0; i < kere; i++)
        {
            boolean lsb = Util.bitGet(this.solOperand, 0);
            
            this.solOperand = shiftRight(this.solOperand);
            
            if (this.solOperand instanceof Byte)
                this.solOperand = Util.bitSet((byte)this.solOperand, 8, this.flags.getCarryFlag());
            else
                ((Word)this.solOperand).bitSet(16, this.flags.getCarryFlag());
            
            this.flags.setCarryFlag(lsb);
        }
        
        if (kere == 1)
            if (Util.enDegerliBit(this.solOperand) != Util.ikinciEnDegerliBit(this.solOperand))
                this.flags.setOverflowFlag();
            else
                this.flags.clearOverflowFlag();
    }
    
    private void rol()
    {
        int kere = ((int) this.sagOperand);
        
        for (int i = 0; i < kere; i++)
        {
            this.flags.setCarryFlag(Util.enDegerliBit(this.solOperand));
            
            this.solOperand = shiftLeft(this.solOperand);
            
            if (this.solOperand instanceof Byte)
                this.solOperand = Util.bitSet((byte)this.solOperand, 1, this.flags.getCarryFlag());
            else
                ((Word)this.solOperand).bitSet(1, this.flags.getCarryFlag());
        }
        
        if (kere == 1)
            if (Util.enDegerliBit(this.solOperand) != this.flags.getCarryFlag())
                this.flags.setOverflowFlag();
            else
                this.flags.clearOverflowFlag(); 
    }
    
    private void ror()
    {
        int kere = ((int) this.sagOperand);
        
        for (int i = 0; i < kere; i++)
        {
            this.flags.setCarryFlag(Util.bitGet(this.solOperand, 0));
            
            this.solOperand = shiftRight(this.solOperand);
            
            if (this.solOperand instanceof Byte)
                this.solOperand = Util.bitSet((byte)this.solOperand, 8, this.flags.getCarryFlag());
            else
                ((Word)this.solOperand).bitSet(16, this.flags.getCarryFlag());
        }
        
        if (kere == 1)
            if (Util.enDegerliBit(this.solOperand) != Util.ikinciEnDegerliBit(this.solOperand))
                this.flags.setOverflowFlag();
            else
                this.flags.clearOverflowFlag();
    }
    
    /**
     * Kolaylık metodu. deger parametresi sadece
     * byte veya Word türünde olmalıdır.
     * 
     * @param deger
     * @return 
     */
    public Object shiftLeft(Object deger)
    {
        if (deger instanceof Byte)
            return shiftLeft((byte)deger);
        else 
            return shiftLeft((Word)deger);
    }
    
    /**
     * Kolaylık metodu. deger parametresi sadece
     * byte veya Word türünde olmalıdır.
     * 
     * @param deger
     * @return 
     */
    public Object shiftRight(Object deger)
    {
        if (deger instanceof Byte)
            return shiftRight((byte)deger);
        else 
            return shiftRight((Word)deger);
    }
    
    public byte shiftLeft(byte deger)
    {
        return (byte)shift(deger, false);
    }
    
    public byte shiftRight(byte deger)
    {
        return (byte)shift(deger, true);
    }
    
    public Word shiftLeft(Word deger)
    {
        return (Word)shift(deger, false);
    }
    
    public Word shiftRight(Word deger)
    {
        return (Word)shift(deger, true);
    }
    
    /**
     * Verilen değeri      yon = TRUE icin sağa,
     *                      yon = FALSE için sola kaydırır.
     * 
     * @param deger
     * @param yon
     * @return 
     */
    private Object shift(Object deger, boolean yon)
    {
        char[] ikili;
        int ustSinir;
        
        if (deger instanceof Byte)
        {
            ikili = Donusum.decimalToBinary((byte)deger, VeriUzunlugu.Byte_8).toCharArray();
            ustSinir = 6;
        }
        else //deger = Word
        {
            Word degerW = (Word) deger;
            ikili = (Donusum.decimalToBinary(degerW.getH(), VeriUzunlugu.Byte_8) + 
                            Donusum.decimalToBinary(degerW.getL(), VeriUzunlugu.Byte_8)).toCharArray();
            ustSinir = 14;
        }
        
        if (yon)//Sağa kaydır
            for(int i = 0; i <= ustSinir; i++)
                ikili[i+1] = ikili[i];
        else//Sola kaydır
            for(int i = 0; i <= ustSinir; i++)
                ikili[i] = ikili[i+1];
        
        ikili[ustSinir + 1] = '0';
        
        if (deger instanceof Byte)
        {
            return (byte)Donusum.binaryToDecimal(String.valueOf(ikili), VeriUzunlugu.Byte_8);
        }
        else
        {
            byte H = (byte)Donusum.binaryToDecimal(String.valueOf(ikili, 0, 8), VeriUzunlugu.Byte_8);
            byte L = (byte)Donusum.binaryToDecimal(String.valueOf(ikili, 8, 8), VeriUzunlugu.Byte_8);

            return new Word(H, L);
        }
    }
    
    public Word bitToplama(Word ust, Word alt)
    {
        String ustBinary = Donusum.decimalToBinary(ust.getDegerInt(), VeriUzunlugu.Word_16);
        String altBinary = Donusum.decimalToBinary(alt.getDegerInt(), VeriUzunlugu.Word_16);
        
        char[] sonucBitleri = new char[16];
        byte elde = 0, ustbit, altbit, toplam, birBitSayisi = 0;
        Word sonuc;
        
        for (int i = 15; i >= 0; i--)
        {
            ustbit = ustBinary.charAt(i) == '1' ? (byte)1 : 0;
            altbit = altBinary.charAt(i) == '1' ? (byte)1 : 0;
            toplam = (byte)(ustbit + altbit + elde);
            sonucBitleri[i] = toplam % 2 == 1 ? '1' : '0';
            birBitSayisi = sonucBitleri[i] == '1' ? (byte)(birBitSayisi+1) : birBitSayisi;
            elde = toplam == 2 || toplam == 3 ? (byte)1 : 0;
            
            if (i == 12 && elde == 1)
                this.tmpFlags.setAuxiliaryCarryFlag();
        }
        
        sonuc = new Word(Integer.parseInt(String.valueOf(sonucBitleri), 2));
        
        if (elde == 1)
            this.tmpFlags.setCarryFlag();
        
        this.tmpFlags.setSignFlag(sonucBitleri[0] == '1');
        this.tmpFlags.setZeroFlag(sonuc.getDegerInt() == 0);
        this.tmpFlags.setParityFlag(birBitSayisi % 2 == 0);
        
        return sonuc;
    }
    
    public byte bitToplama(byte ust, byte alt)
    {
        String ustBinary = Donusum.decimalToBinary(ust, VeriUzunlugu.Byte_8);
        String altBinary = Donusum.decimalToBinary(alt, VeriUzunlugu.Byte_8);
        
        char[] sonucBitleri = new char[8];
        byte elde = 0, ustbit, altbit, toplam, sonuc, birBitSayisi = 0;
        
        for (int i = 7; i >= 0; i--)
        {
            ustbit = ustBinary.charAt(i) == '1' ? (byte)1 : 0;
            altbit = altBinary.charAt(i) == '1' ? (byte)1 : 0;
            toplam = (byte)(ustbit + altbit + elde);
            sonucBitleri[i] = toplam % 2 == 1 ? '1' : '0';
            birBitSayisi = sonucBitleri[i] == '1' ? (byte)(birBitSayisi+1) : birBitSayisi;
            elde = toplam == 2 || toplam == 3 ? (byte)1 : 0;
            
            if (i == 4 && elde == 1)
                this.tmpFlags.setAuxiliaryCarryFlag();
        }
        
        sonuc = (byte)Integer.parseInt(String.valueOf(sonucBitleri), 2);
        
        this.tmpFlags.setCarryFlag(elde == 1);
        this.tmpFlags.setSignFlag(sonucBitleri[0] == '1');
        this.tmpFlags.setZeroFlag(sonuc == 0);
        this.tmpFlags.setParityFlag(birBitSayisi % 2 == 0);
        
        return sonuc;
    }
    
    public Object getAluYazmac()
    {
        return aluYazmac;
    }

    public FlagRegister getFlags()
    {
        return flags;
    }

    public void setSagOperand(Object sagOperand)
    {
        this.sagOperand = sagOperand;
    }

    public void setSolOperand(Object solOperand)
    {
        this.solOperand = solOperand;
    }
}
