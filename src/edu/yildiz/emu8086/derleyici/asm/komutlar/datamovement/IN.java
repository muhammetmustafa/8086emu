
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.derleyici.asm.komut.CiftOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;

/**
 *
 * @author MMC
 */
public class IN extends CiftOperandliKomut
{
    public IN(Operand hedef, Operand kaynak)
    {
        super(hedef, kaynak);
    }
    
}
