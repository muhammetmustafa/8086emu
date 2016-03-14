
package board;

import edu.yildiz.emu8086.tipler.MemoryAddress;
import edu.yildiz.emu8086.tipler.Word;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class MemoryAddressTest 
{
    @Test
    public void memoryAddressTest()
    {
        MemoryAddress ma = new MemoryAddress(new Word(0x5200),new Word(0x8230));
        MemoryAddress ma1 = new MemoryAddress(new Word(0x490b),new Word(0x2359));
        
        Assert.assertEquals(369200, ma.getIndis());
        Assert.assertEquals(308233, ma1.getIndis());
    }
}
