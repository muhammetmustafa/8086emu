    
package edu.yildiz.emu8086.derleyici.asm.operand;

/**
 *
 * @author MMC
 */
public class EtiketOperand extends Operand
{
    private final String etiket;

    public EtiketOperand(String etiket)
    {
        this.etiket = etiket;
    }

    public String getEtiket()
    {
        return etiket;
    }

    @Override
    public String toString()
    {
        return "[" + this.etiket + "]";
    }
    
    
}
