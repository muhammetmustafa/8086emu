
package edu.yildiz.emu8086.derleyici.asm.operand;

import org.junit.Test;

/**
 *
 * @author MMC
 */
public class ByteImmediateOperandTest
{
    @Test
    public void test_negate()
    {
        ByteImmediateOperand bio = new ByteImmediateOperand("127");
        bio.ikininKomplementi();
        
        System.out.println(bio.getImmediate());
    }
}
