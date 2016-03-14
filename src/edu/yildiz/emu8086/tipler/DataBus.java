
package edu.yildiz.emu8086.tipler;

import edu.yildiz.emu8086.board.eu.EU;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MMC
 */
public class DataBus
{
    private static final Logger kutukcu = Logger.getLogger(EU.class.getName());
    private static DataBus dataBus;
    
    //Word veya byte
    private Object data;
    
    protected DataBus()
    {
        this.data = null;
    }
    
    public static DataBus get()
    {
        if (dataBus == null)
            dataBus = new DataBus();
        
        return dataBus;
    }
    
    public synchronized void yaz(Object data)
    {
        while (this.data != null)
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
        
        this.data = data;
    }
    
    public synchronized Object oku()
    {
        while (this.data == null)
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
        
        Object d = this.data;
        this.data = null;
        return d;
    }
}
