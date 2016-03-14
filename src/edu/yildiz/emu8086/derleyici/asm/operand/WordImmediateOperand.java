
package edu.yildiz.emu8086.derleyici.asm.operand;

import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.Word;
import edu.yildiz.emu8086.util.Donusum;

/**
 *
 * @author MMC
 */
public class WordImmediateOperand extends ImmediateOperand
{
    private Word immediate;
    
    public WordImmediateOperand(String deger) throws IllegalArgumentException
    {
         try
        {
            switch(gosterimTuru(deger))
            {
                case Hexacedimal: 
                    this.immediate = new Word(Integer.decode(javaHexadecimal(deger)));
                    break;
                case Decimal: 
                    this.immediate = new Word(Integer.parseInt(deger));
                    break;
            }
        } 
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException();
        }
    }

    public WordImmediateOperand(byte bayt)
    {
        this.immediate = new Word(bayt);
    }
    
    public WordImmediateOperand(int bayt)
    {
        this((byte)bayt);//bayt işaretiyle word'e genişletilir.
    }
    
    public static WordImmediateOperand yeni(byte bayt)
    {
        return new WordImmediateOperand(bayt);
    }

    @Override
    public String toString()
    {
        return Donusum.decimalToHexadecimal(this.immediate.getDegerInt(), VeriUzunlugu.Word_16);
    }
    
    @Override
    public void ikininKomplementi()
    {
        this.immediate = this.immediate.ikininKomplementi();
    }
    
    public Word getImmediate()
    {
        return immediate;
    }
}
