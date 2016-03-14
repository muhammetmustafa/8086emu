package derleyici;

import edu.yildiz.emu8086.derleyici.Parser;
import edu.yildiz.emu8086.derleyici.asm.Assembly;
import edu.yildiz.emu8086.derleyici.ParserException;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class ParserTest
{
    
    public ParserTest()
    {
    }
    
    @Test
    public void parserTest()
    {
       
        try
        {
             Assembly asm = Parser.parse(TestUtil.testiOku());
             System.out.println(asm);
        } 
        catch (ParserException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
