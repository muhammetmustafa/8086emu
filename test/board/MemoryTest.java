
package board;

import edu.yildiz.emu8086.tipler.Memory;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class MemoryTest 
{
    @Test
    public void memoryOlusturma()
    {
        Memory mem = new Memory();
        
        System.out.println(mem);
    }
}
