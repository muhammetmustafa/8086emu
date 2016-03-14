
package edu.yildiz.emu8086.tipler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe First-In-First-Out 
 * 
 * @author MMC
 * @param <T>
 */
public class FIFO <T>
{
    private static final Logger kutukcu = Logger.getLogger(FIFO.class.getSimpleName());
    
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    private final Object[] kuyruk;

    public FIFO()
    {
        kuyruk = new Object[6];
    }
    
    public synchronized void ekle(T deger)
    {
        while (this.bosSayisi() <= 1)
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
        
        int i = 0;
        for (; i < this.kuyruk.length; i++)
        {
            if (this.kuyruk[i] == null)
            {
                this.kuyruk[i] = deger;
                break;
            }
        }
        
        this.pcs.fireIndexedPropertyChange("kuyruk", i, null, deger);
        notifyAll();
    }
    
    public synchronized T al()
    {
        while (this.kuyruk[0] == null)
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
        
        T t = (T) this.kuyruk[0];
        this.kaydir();
        
        this.pcs.fireIndexedPropertyChange("kuyruk", 0, null, t);
        notifyAll();
        
        return t;
    }
    
    public synchronized void bosalt()
    {
        for (int i = 0; i < 6; i++)
            this.kuyruk[i] = null;
        
        this.pcs.fireIndexedPropertyChange("kuyruk", 0, null, null);
        this.pcs.fireIndexedPropertyChange("kuyruk", 1, null, null);
        this.pcs.fireIndexedPropertyChange("kuyruk", 2, null, null);
        this.pcs.fireIndexedPropertyChange("kuyruk", 3, null, null);
        this.pcs.fireIndexedPropertyChange("kuyruk", 4, null, null);
        this.pcs.fireIndexedPropertyChange("kuyruk", 5, null, null);
    }
    
    public String getKuyruk(int indis)
    {
        if (indis < 0 || indis > this.kuyruk.length)
            return null;
        
        if (this.kuyruk[indis] == null)
            return "BOŞ";
        else
            return this.kuyruk[indis].toString();
    }
    
    private void kaydir()
    {
        int i;
        Object temp;
        for (i = 1; i <= this.kuyruk.length - 1; i++)
        {
            temp = this.kuyruk[i-1];
            this.kuyruk[i-1] = this.kuyruk[i];
            this.pcs.fireIndexedPropertyChange("kuyruk", i, temp, this.kuyruk[i]);
        }
        
        this.kuyruk[i-1] = null;
    }
    
    public int bosSayisi()
    {
        int miktar = 0;
        for(Object o : this.kuyruk)
            if (o == null)
                miktar++;
        return miktar;
    }
    
    public boolean bosMu()
    {
        return bosSayisi() <= 0;
    }
    
    public boolean instructionFetchIcinUygunMu()
    {
        return bosSayisi() >= 2;
    }
    
    public String varDump()
    {
        StringJoiner sj = new StringJoiner(",", "[", "]");
        
        for(Object o : this.kuyruk)
            sj.add(o == null ? "null" : o.toString());
        
        return sj.toString();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        this.pcs.removePropertyChangeListener(listener);
    }
}
