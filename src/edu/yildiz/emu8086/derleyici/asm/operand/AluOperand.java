
package edu.yildiz.emu8086.derleyici.asm.operand;

import edu.yildiz.emu8086.tipler.Yazmac;

/**
 *
 * @author MMC
 */
public class AluOperand extends RegisterOperand
{
    public AluOperand()
    {
        super(Yazmac.ALU);
    }
}
