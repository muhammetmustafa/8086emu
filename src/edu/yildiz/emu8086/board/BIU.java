
package edu.yildiz.emu8086.board;

import java.util.logging.Logger;
import edu.yildiz.emu8086.derleyici.asm.Assembly;
import edu.yildiz.emu8086.derleyici.asm.komut.Komut;
import edu.yildiz.emu8086.derleyici.asm.komutlar.HLT;
import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.gui.Animasyon;
import edu.yildiz.emu8086.tipler.AddressBus;
import edu.yildiz.emu8086.tipler.ControlBus;
import edu.yildiz.emu8086.tipler.DataBus;
import edu.yildiz.emu8086.tipler.FIFO;
import edu.yildiz.emu8086.tipler.Word;
import edu.yildiz.emu8086.tipler.Yazmac;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import javax.swing.JLabel;

/**
 *  Bus Interface Unit
 * 
 * Kuyrukta 2 byte boş kaldığı sürece instruction fetch et.
 * 
 * 
 * @author MMC
 */
public class BIU implements Runnable
{
    public static final Logger kutukcu = Logger.getLogger(BIU.class.getSimpleName());
    private static BIU biu;
    
    private final PropertyChangeSupport yazmacBildirici  = new PropertyChangeSupport(this);
    private final PropertyChangeSupport olayBildirici  = new PropertyChangeSupport(this);
    
    //Segment yazmaçları
    private Word codeSegment;  //Code Segment
    private Word dataSegment;  //Data Segment
    private Word stackSegment;  //Stack Segment
    private Word extraSegment;  //Extra Segment
    private Word instructionPointer;  //Instruction Pointer
    
    //EU, BIU'dan bir memory read veya memory write 
    //isteğinde bulunacağı zaman kullanır
    private MemoryOperand memoryOperand;
    private Object memoryObject;
    
    private final FIFO<Komut> instructionQueue;
    private final MemoryInterface memoryInterface;
    
    private Assembly kod;
    
    private boolean animasyonCalisiyorMu = false;
    private boolean bellekIslemiGercekle = false;
    
    private int uyumaSuresi = 1000;
 
    private JLabel labelCycle;
    private Animasyon segmentAnimasyon;
    
    protected BIU()
    {
        this.codeSegment = new Word(0x01, 0x00);
        this.dataSegment = new Word(0x01, 0x00);
        this.stackSegment = new Word(0x01, 0x00);
        this.extraSegment = new Word(0x01, 0x00);
        
        this.instructionPointer = new Word();
        
        this.memoryOperand = null;
        this.memoryObject = null;
        
        this.instructionQueue = new FIFO<>();
        this.kod = null;
        this.memoryInterface = new MemoryInterface();
    }
    
    public final void reset()
    {
        setCodeSegment(new Word(0x01, 0x00));
        setDataSegment(new Word(0x01, 0x00));
        setStackSegment(new Word(0x01, 0x00));
        setExtraSegment(new Word(0x01, 0x00));
        setInstructionPointer(new Word());
        
        this.memoryOperand = null;
        this.memoryObject = null;
        
        this.instructionQueue.bosalt();
        this.kod = null;
        
        this.animasyonCalisiyorMu = false;
        this.bellekIslemiGercekle = false;
        
        this.memoryInterface.getMemory().reset();
    }
    
    public static BIU get()
    {
        if (biu == null)
            biu = new BIU();
        
        return biu;
    }
    
    @Override
    public void run()
    {   
        kutukcu.info("Başladı");
        
        while (animasyonCalisiyorMu)
        {
            //Eğer bekleyen bellek işlemi varsa
            if (bellekIslemiGercekle)
            {
                if (this.memoryOperand != null && this.memoryObject == null)
                {
                    //Memory Read
                    this.memoryReadCycle();
                    this.memoryOperand = null;
                    bellekIslemiGercekle = false;
                }
                else if (this.memoryOperand != null && this.memoryObject != null)
                {
                    //Memory Write
                    this.memoryWriteCycle();
                    this.memoryOperand = null;
                    this.memoryObject = null;
                    bellekIslemiGercekle = false;
                }
            }
            
            //Instruction Fetch Cycle işlemini devam ettirmenin şartı 
            //EU'dan bellek okuma/yazma isteğinin olmamasıdır.
            //Bu isteği de hangiAdres ve hangiIslem alanlarından anlayabiliriz.
            if (this.kod.alinacakKomutKaldiMi())
            {
                if (this.instructionQueue.instructionFetchIcinUygunMu() && 
                        this.bellekIslemiGercekle == false)
                {
                    this.instructionFetchCycle();
                }
            }
        }
        
        if (this.kod.alinacakKomutKaldiMi())
        {
            //Animasyon duraklatma
            this.olayBildirici.firePropertyChange("durakladi", false, true);
            kutukcu.info("Durakladı");
        }
        else
        {
            //Animasyon duraklatma
            this.olayBildirici.firePropertyChange("sonlandi", false, true);
            kutukcu.info("Sonlandı");
        }
        
        if (this.labelCycle != null)
        {
            this.labelCycle.setText("  ");
            this.labelCycle.setBackground(new java.awt.Color(240, 240, 240));
            this.labelCycle.setForeground(new java.awt.Color(0, 0, 0));
        }
    }
    
    private void memoryReadCycle()
    {
        kutukcu.info("Memory Read Cycle Başlıyor...");
        
        if (this.labelCycle != null)
        {
            this.labelCycle.setText("MEMORY READ CYCLE");
            this.labelCycle.setBackground(new java.awt.Color(255, 255, 0));
            this.labelCycle.setForeground(new java.awt.Color(0, 0, 204));
        }
        
        this.setControlBus(ControlBus.State.READ);
        this.setAddresBus(this.yazmacOku(this.memoryOperand.getSegment()), 
                            this.memoryOperand.getHesaplanmisOffset());
        kutukcu.info("Değerin alınacağı bellek adresi AddressBus'a yazıldı.");
        
        this.runMemory();
        
        this.memoryObject = DataBus.get().oku();
    }
    
    private void memoryWriteCycle()
    {
        kutukcu.info("Memory Write Cycle Başlıyor...");
        
        if (this.labelCycle != null)
        {
            this.labelCycle.setText("MEMORY WRITE CYCLE");
            this.labelCycle.setBackground(new java.awt.Color(255, 255, 0));
            this.labelCycle.setForeground(new java.awt.Color(0, 0, 204));
        }
        
        this.setControlBus(ControlBus.State.WRITE);
        this.setAddresBus(this.yazmacOku(this.memoryOperand.getSegment()), 
                            this.memoryOperand.getHesaplanmisOffset());
        kutukcu.info("Değerin yazılacağı bellek adresi AddressBus'a yazıldı.");
        
        DataBus.get().yaz(this.memoryObject);
        kutukcu.info("AddressBus'taki bellek adresine yazılacak değer DataBus'a yazıldı.");
        
        this.runMemory();
    }
    
    private void instructionFetchCycle()
    {
        kutukcu.info("Instruction Fetch Cycle Başlıyor...");
        
        if (this.labelCycle != null)
        {
            this.labelCycle.setText("INSTRUCTION FETCH CYCLE");
            this.labelCycle.setBackground(new java.awt.Color(255, 255, 0));
            this.labelCycle.setForeground(new java.awt.Color(0, 0, 204));
        }
        
        this.setControlBus(ControlBus.State.READ);
        
        //AdresBus = CS:IP
        this.setAddresBus(this.codeSegment, this.instructionPointer);
        kutukcu.info(String.format("Komutun adresi [%s, %s] AdressBus'a gönderildi.", this.codeSegment, this.instructionPointer));
        
        this.runMemory();
        
        //DataBus'ı bekle. Ancak bu çöpe gidecek.
        DataBus.get().oku();

        //Sıradaki komutu bellekte okumuşcasına assembly kod listesinden oku
        //komut kuyruğuna ekle.
        //zıplama olmuşmu kontrol et. Olmuşsa kuyruğu boşalt.
        if (this.kod.getKuyrukBosalt())
            this.instructionQueue.bosalt();
        
        Komut komut = this.kod.sonrakiKomut();
        this.instructionQueue.ekle(komut);
        kutukcu.info("Sıradaki komut bellekten okundu ve kuyruğa eklendi.");

        this.setInstructionPointer(new Word(this.kod.getIp()));
    }
    
    public void zipla(EtiketOperand etiket)
    {
        //Kuyruğu temizle
        this.instructionQueue.bosalt();
        
        //IP'yi ayarla
        this.kod.zipla(etiket.getEtiket());
        this.setInstructionPointer(new Word(this.kod.getIp()));
        
    }
    
    private void setControlBus(ControlBus.State durum)
    {
        ninniSoyledeUyuyalim();
        ControlBus.get().yaz(durum);
        kutukcu.info(String.format("ControlBus '%s' modunda ayarlandı.", durum.name()));
    }
    
    private void setAddresBus(Word segment, Word offset)
    {
        //ninniSoyledeUyuyalim();
        
        if (this.segmentAnimasyon != null)
        {
            this.segmentAnimasyon.basla();
            
            while (this.segmentAnimasyon.isRunning())
            {
                try
                {
                    Thread.sleep(100);
                } 
                catch (InterruptedException ex)
                {
                    Logger.getLogger(BIU.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        AddressBus.get().yaz(segment, offset);
    }
    
    private void runMemory()
    {
        ninniSoyledeUyuyalim();
        this.memoryInterface.run();
    }
    
    private void ninniSoyledeUyuyalim()
    {
        try
        {
            Thread.sleep(this.uyumaSuresi);
        } 
        catch (InterruptedException ex)
        {
            Logger.getLogger(BIU.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Word yazmacOku(Yazmac yazmac)
    {
        switch (yazmac)
        {
            case CS: return this.codeSegment;
            case DS: return this.dataSegment;
            case SS: return this.stackSegment;
            case ES: return this.extraSegment;
            default: return null;
        }
    }
    
    public Word getCodeSegment()
    {
        return codeSegment;
    }

    public void setCodeSegment(Word codeSegment)
    {
        this.codeSegment = codeSegment;
        
        this.yazmacBildirici.firePropertyChange("CS", this.codeSegment, codeSegment);
    }
    
    public Word getDataSegment()
    {
        return dataSegment;
    }

    public void setDataSegment(Word dataSegment)
    {
        this.dataSegment = dataSegment;
        
        this.yazmacBildirici.firePropertyChange("DS", this.dataSegment, dataSegment); 
    }
    
    public Word getStackSegment()
    {
        return stackSegment;
    }

    public void setStackSegment(Word stackSegment)
    {
        this.stackSegment = stackSegment;
        
        this.yazmacBildirici.firePropertyChange("SS", this.stackSegment, stackSegment);
    }
    
    public Word getExtraSegment()
    {
        return extraSegment;
    }

    public void setExtraSegment(Word extraSegment)
    {
        this.extraSegment = extraSegment;
        
        this.yazmacBildirici.firePropertyChange("ES", this.extraSegment, extraSegment);
    }

    public Word getInstructionPointer()
    {
        return instructionPointer;
    }
    
    private void setInstructionPointer(Word instructionPointer)
    {
        this.instructionPointer = instructionPointer;
        
        this.yazmacBildirici.firePropertyChange("IP", null, instructionPointer);
    }
    
    public Assembly getKod()
    {
        return kod;
    }

    public FIFO<Komut> getInstructionQueue()
    {
        return instructionQueue;
    }

    public void setUyumaSuresi(int uyumaSuresi)
    {
        this.uyumaSuresi = uyumaSuresi;
    }

    public void setKod(Assembly kod)
    {
        this.kod = kod;
        this.kod.getKomutlar().add(new HLT());
    }

    public MemoryInterface getMemoryInterface()
    {
        return memoryInterface;
    }
    
    public MemoryOperand getMemoryOperand()
    {
        return memoryOperand;
    }

    public void setMemoryOperand(MemoryOperand memoryOperand)
    {
        this.memoryOperand = memoryOperand;
    }

    public Object getMemoryObject()
    {
        return memoryObject;
    }

    public void setMemoryObject(Object memoryObject)
    {
        this.memoryObject = memoryObject;
    }
    
    public void yazmacDegisiklikDinleyiciEkle(PropertyChangeListener listener)
    {
        this.yazmacBildirici.addPropertyChangeListener(listener);
    }
    
    public void olayDegisiklikDinleyiciEkle(PropertyChangeListener listener)
    {
        this.olayBildirici.addPropertyChangeListener(listener);
    }
    
    public JLabel getLabelCycle()
    {
        return labelCycle;
    }

    public void setLabelCycle(JLabel labelCycle)
    {
        this.labelCycle = labelCycle;
    }

    public Animasyon getSegmentAnimasyon()
    {
        return segmentAnimasyon;
    }

    public void setSegmentAnimasyon(Animasyon segmentAnimasyon)
    {
        this.segmentAnimasyon = segmentAnimasyon;
    }
    
    public boolean getBellekIslemiGercekle()
    {
        return this.bellekIslemiGercekle;
    }
    
    public void setBellekIslemiGercekle(boolean bellekIslemiGercekle)
    {
        this.bellekIslemiGercekle = bellekIslemiGercekle;
    }

    public void setAnimasyonCalisiyorMu(boolean animasyonCalisiyorMu)
    {
        this.animasyonCalisiyorMu = animasyonCalisiyorMu;
    }
}
