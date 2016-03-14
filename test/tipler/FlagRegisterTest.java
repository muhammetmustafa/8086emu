
package tipler;

import edu.yildiz.emu8086.tipler.FlagRegister;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class FlagRegisterTest 
{
    @Test
    public void flagRegisterTest()
    {
        FlagRegister flags = new FlagRegister();
        
        System.out.println(flags);
        
        flags.setAuxiliaryCarryFlag();
        flags.setOverflowFlag();
        flags.setSignFlag();
        
        System.out.println(flags);
        
        flags.clearSignFlag();
        flags.setDirectionFlag();
        
        System.out.println(flags);
    }
}
