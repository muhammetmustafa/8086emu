
package edu.yildiz.emu8086.tipler;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Singleton
 * @author MMC
 */
public class ControlBus 
{
    public enum State {IDLE, READ, WRITE, EOP};
    
    private static ControlBus controlBus;
    private ControlBus.State state;
    private JLabel label;
    
    protected ControlBus()
    {
        this.state = ControlBus.State.IDLE;
        
        this.anime();
    }
    
    public static ControlBus get()
    {
        if (controlBus == null)
            controlBus = new ControlBus();
        
        return controlBus;
    }
    
    public synchronized void yaz(ControlBus.State state)
    {
        while (this.state != State.IDLE)
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
        
        this.state = state;
        
        this.anime();
    }
    
    public synchronized ControlBus.State oku()
    {
        while (this.state == State.IDLE)
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
        
        ControlBus.State durum = this.state;
        this.state = State.IDLE;
        
        this.anime();
        
        return durum;
    }

    private void anime()
    {
        if (label != null)
        {
            label.setIcon(new ImageIcon(getClass().getResource("/edu/yildiz/emu8086/gui/resources/Control_Bus_" + this.state.toString() + ".png"))); 
        }
    }
    
    public JLabel getLabel()
    {
        return label;
    }

    public void setLabel(JLabel label)
    {
        this.label = label;
    }
    
    
}
