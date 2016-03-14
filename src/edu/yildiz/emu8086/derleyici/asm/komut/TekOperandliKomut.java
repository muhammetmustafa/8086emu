
package edu.yildiz.emu8086.derleyici.asm.komut;

import edu.yildiz.emu8086.derleyici.asm.operand.Operand;

/**
 *  Bu şablon sınıf INC, DEC gibi assembly komutlarını temsil için kullanılır.
 * 
 * @author MMC
 */
public abstract class TekOperandliKomut extends Komut
{
    private final Operand hedef;

    public TekOperandliKomut(Operand hedef)
    {
        this.hedef = hedef;
    }
    
    public Operand getHedef()
    {
        return hedef;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " " + this.hedef.toString();
    }
    
    
}
