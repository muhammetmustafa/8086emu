
package edu.yildiz.emu8086.gui.events;

import edu.yildiz.emu8086.tipler.MemoryAddress;
import java.beans.PropertyChangeEvent;

/**
 *
 * @author MMC
 */
public class MemoryChangeEvent extends PropertyChangeEvent
{
    private final MemoryAddress adres;
    
    public MemoryChangeEvent(Object source, String propertyName, Object oldValue, Object newValue, MemoryAddress adres)
    {
        super(source, propertyName, oldValue, newValue);
        this.adres = adres;
    }

    public MemoryAddress getAdres()
    {
        return adres;
    }

}
