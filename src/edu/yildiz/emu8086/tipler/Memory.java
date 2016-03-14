
package edu.yildiz.emu8086.tipler;

import edu.yildiz.emu8086.gui.events.MemoryChangeEvent;
import edu.yildiz.emu8086.gui.events.MemoryChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * Bellek Ayrılmış alanları
 * ffff0h - fffffh = reset bootstrap program jump
 * 0h - 3ffh = interrupt pointers (4 bytes per pointer)(256 possible interrupt types)
 * @author MMC
 */
public class Memory
{   
    public enum IslemTuru { OKU, YAZ };
    
    private static final int BANK_UZUNLUGU_8086 = 524288; //2^19
    
    private final PropertyChangeSupport olayYonetici  = new PropertyChangeSupport(this);
    
    
    /**
     * Tek adresler = High Bank (0. eleman)
     * Çift adresler = Low Bank (1. eleman)
     */
    private final byte[][] banks; 

    public Memory()
    {
        this.banks = new byte[2][BANK_UZUNLUGU_8086];
    }
    
    public final void reset()
    {
        this.banks[0] = new byte[BANK_UZUNLUGU_8086];
        this.banks[1] = new byte[BANK_UZUNLUGU_8086];
        
        this.olayYonetici.firePropertyChange(new MemoryChangeEvent(this, "reset", null, null, null));
    }
    
    public byte oku(MemoryAddress adres)
    {
        return this.banks[adres.getBank().getIndis()][adres.getIndis()];
    }
    
    public Word okuWord(MemoryAddress adres)
    {
        byte h;
        byte l;
        
        if (adres.getBank() == Bank.LOW)
        {
            l = this.banks[Bank.LOW.getIndis()][adres.getIndis()];
            h = this.banks[Bank.HIGH.getIndis()][adres.getIndis()];
        }
        else
        {
            h = this.banks[Bank.LOW.getIndis()][adres.getIndis()+1];
            l = this.banks[Bank.HIGH.getIndis()][adres.getIndis()];
        }
        
        return new Word(h, l);
    }
    
    public void yaz(MemoryAddress adres, Object deger)
    {
        this.genericYaz(adres, deger);
        this.olayYonetici.firePropertyChange(new MemoryChangeEvent(this, "memory", null, deger, adres));
    }
    
    public void olaysizYaz(MemoryAddress adres, Object deger)
    {
        this.genericYaz(adres, deger);
    }
    
    private void genericYaz(MemoryAddress adres, Object deger)
    {
        if (deger instanceof Byte)
        {
            this.banks[adres.getBank().getIndis()][adres.getIndis()] = (byte)deger;
        }
        else
        {
            Word degerW = (Word) deger;
            
            if (adres.getBank() == Bank.LOW)
            {
                this.banks[Bank.LOW.getIndis()][adres.getIndis()] = degerW.getL();
                this.banks[Bank.HIGH.getIndis()][adres.getIndis()] = degerW.getH();
            }
            else
            {
                this.banks[Bank.LOW.getIndis()][adres.getIndis()+1] = degerW.getH();
                this.banks[Bank.HIGH.getIndis()][adres.getIndis()] = degerW.getL();
            }
        }
    }
    
    public void addPropertyChangeListener(MemoryChangeListener pcs)
    {
        this.olayYonetici.addPropertyChangeListener(pcs);
    }
    
    public enum Bank 
    {
        HIGH(0), 
        LOW(1); 
        
        private final int indis;

        private Bank(int indis)
        {
            this.indis = indis;
        }

        public int getIndis()
        {
            return indis;
        }
    };
}
