
package edu.yildiz.emu8086.util;

import edu.yildiz.emu8086.board.eu.ALU;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.FlagRegister;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class UtilTest
{
    @Test
    public void test()
    {
        byte b = -34;
        
        System.out.println(Donusum.decimalToBinary(b, VeriUzunlugu.Byte_8));
        
        b = new ALU(new FlagRegister()).shiftLeft(b);
        
         System.out.println(Donusum.decimalToBinary(b, VeriUzunlugu.Byte_8));
    }
}
