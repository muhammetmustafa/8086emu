package derleyici;

import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.Token;
import edu.yildiz.emu8086.derleyici.Tokenizer;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class TokenizerTest
{
    
    public TokenizerTest()
    {
    }
    
    @Test
    public void tokenizerTest() throws ParserException
    {
        Tokenizer tokenizer = new Tokenizer(TestUtil.testiOku());
        
        for (Token token : tokenizer.getTokens())
            System.out.println(token);
    }
}
