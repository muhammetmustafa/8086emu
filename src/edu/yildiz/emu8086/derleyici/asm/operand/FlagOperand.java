
package edu.yildiz.emu8086.derleyici.asm.operand;

import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class FlagOperand extends Operand
{
    private final FlagRegister.Tur hangiFlag;

    public FlagOperand(FlagRegister.Tur hangiFlag)
    {
        this.hangiFlag = hangiFlag;
    }

    public FlagRegister.Tur getHangiFlag()
    {
        return hangiFlag;
    }
    
}
