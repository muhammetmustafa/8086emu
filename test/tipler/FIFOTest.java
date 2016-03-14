
package tipler;

import edu.yildiz.emu8086.tipler.FIFO;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class FIFOTest
{
    @Test
    public void test_fifo()
    {
        FIFO<String> kuyruk = new FIFO();
        
        kuyruk.ekle("1");
        kuyruk.ekle("2");
        kuyruk.ekle("3");
        kuyruk.ekle("4");
        kuyruk.ekle("5");
        kuyruk.ekle("6");
        kuyruk.ekle("7");
        
        kuyruk.varDump();
        
        kuyruk.al();
        
        kuyruk.varDump();
        
        kuyruk.al();
        
        kuyruk.varDump();
        
        kuyruk.ekle("7");
        
        kuyruk.varDump();
        
        kuyruk.ekle("8");
        
        kuyruk.varDump();
    }
}
