
package edu.yildiz.emu8086.board;


import edu.yildiz.emu8086.tipler.AddressBus;
import edu.yildiz.emu8086.tipler.ControlBus;
import edu.yildiz.emu8086.tipler.DataBus;
import edu.yildiz.emu8086.tipler.Memory;
import edu.yildiz.emu8086.tipler.MemoryAddress;
import edu.yildiz.emu8086.tipler.Word;
import java.util.logging.Logger;

/**
 *
 * @author MMC
 */
public class MemoryInterface
{
    private static final Logger kutukcu = Logger.getLogger(MemoryInterface.class.getSimpleName());
    
    private final Memory memory;
    
    public MemoryInterface()
    {
        this.memory = new Memory();
    }
    
    public void run()
    {
        ControlBus.State state = ControlBus.get().oku();
        MemoryAddress adres = AddressBus.get().oku();

        if (adres != null)
        {
            if (state == ControlBus.State.READ)
            {
                Word w = this.memory.okuWord(adres);
                kutukcu.info(String.format("Memory[%s]=%s", adres, w));

                DataBus.get().yaz(w);
            }
            else if (state == ControlBus.State.WRITE)
            {
                Object o = DataBus.get().oku();
                
                this.memory.yaz(adres, o);
            }
        }
    }

    public Memory getMemory()
    {
        return memory;
    }
}
