
package edu.yildiz.emu8086.tipler;

import edu.yildiz.emu8086.board.eu.EU;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 *  Thread-safe, Singleton AddressBus implementation
 * 
 * @author MMC
 */
public class AddressBus 
{
    private static final Logger kutukcu = Logger.getLogger(EU.class.getName());
    
    /**
     * Singleton alan
     */
    private static AddressBus addressBus;
    
    private MemoryAddress adres;

    protected AddressBus()
    {
        this.adres = null;
    }
    
    public static AddressBus get()
    {
        if (addressBus == null)
            addressBus = new AddressBus();
        
        return addressBus;
    }
    
    public synchronized void yaz(Word segment, Word offset)
    {
        this.yaz(new MemoryAddress(segment, offset));
    }
    
    public synchronized void yaz(MemoryAddress adres)
    {
        while (this.adres != null)
        {
            try
            {
                wait();
            } 
            catch (InterruptedException ex)
            {
                Logger.getLogger(FIFO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.notifyAll();
        
        this.adres = adres;
    }
    
    public synchronized MemoryAddress oku()
    {
        while (this.adres == null)
        {
            try
            {
                wait();
            } 
            catch (InterruptedException ex)
            {
                Logger.getLogger(FIFO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.notifyAll();
        
        MemoryAddress adress = this.adres;
        this.adres = null;
        return adress;
    }
}
