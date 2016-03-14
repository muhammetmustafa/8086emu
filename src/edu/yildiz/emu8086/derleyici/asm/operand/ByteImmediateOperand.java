
package edu.yildiz.emu8086.derleyici.asm.operand;

import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.util.Donusum;

/**
 *
 * @author MMC
 */
public class ByteImmediateOperand extends ImmediateOperand
{
    private byte immediate;

    public ByteImmediateOperand(String deger)
    {
        try
        {
            switch(gosterimTuru(deger))
            {
                case Hexacedimal: 
                    this.immediate = Integer.decode(javaHexadecimal(deger)).byteValue();
                    break;
                case Decimal: 
                    this.immediate = (byte)Integer.parseInt(deger);
                    break;
            }
        } 
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException();
        }
    }
    
    public ByteImmediateOperand(int deger)
    {
        this.immediate = (byte) deger;
    }
    
    public ByteImmediateOperand(byte deger)
    {
        this.immediate = deger;
    }
    
    @Override
    public String toString()
    {
        return Donusum.decimalToHexadecimal(immediate, VeriUzunlugu.Byte_8);
    }
    
    /**
     * deger = -deger işlemini yapar.
     * 
     */
    @Override
    public void ikininKomplementi()
    {
        this.immediate = (byte)(-this.immediate);
    }
    
    public byte getImmediate()
    {
        return immediate;
    }
}
