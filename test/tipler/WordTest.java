package tipler;

import edu.yildiz.emu8086.tipler.Word;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class WordTest
{
    
    public WordTest()
    {
    }
    
    @Test
    public void wordTest1()
    {
        Word seg = new Word(0x5200);
        Word off = new Word(0x8230);
        
        Assert.assertEquals(20992, seg.getDegerInt());
        Assert.assertEquals(33328, off.getDegerInt());
    }
    
    @Test
    public void wordTest2()
    {
        Word seg = new Word(0x2359);
        Word off = new Word(0x490b);
        
        Assert.assertEquals(9049, seg.getDegerInt());
        Assert.assertEquals(18699, off.getDegerInt());
    }
    
    @Test
    public void test_ikininKomplementi()
    {
        Word w = new Word(7589);
        w.ikininKomplementi();
        
        Assert.assertEquals(new Word(0xe25b), w);
    }
    
    @Test
    public void test_byteGenisletme()
    {
        Word w = new Word(0xf8, 0x35);
        
        System.out.println(w.toStringBinary());
    }
}
