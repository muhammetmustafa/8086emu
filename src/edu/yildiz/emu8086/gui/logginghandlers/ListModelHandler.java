
package edu.yildiz.emu8086.gui.logginghandlers;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.DefaultListModel;

/**
 *
 * @author MMC
 */
public class ListModelHandler extends Handler
{
    private final DefaultListModel<String> model;
    
    public ListModelHandler()
    {
        this.model = new DefaultListModel<>();
    }
    
    @Override
    public void publish(LogRecord record)
    {
        this.model.addElement(record.getLoggerName() + " : " + record.getMessage());
    }

    @Override
    public void flush()
    {
        
    }

    @Override
    public void close()
    {
       
    }
    
    public DefaultListModel<String> getModel()
    {
        return model;
    }
}
