
package edu.yildiz.emu8086.board.eu.islem;

import edu.yildiz.emu8086.derleyici.asm.operand.Operand;

/**
 *
 * @author MMC
 */
public class VeriTransferIslemi extends Islem
{
    private Operand hedef;
    private Operand kaynak;
    
    public VeriTransferIslemi(Operand hedef, Operand kaynak)
    {
        this.hedef = hedef;
        this.kaynak = kaynak;
    }

    public Operand getHedef()
    {
        return hedef;
    }

    public Operand getKaynak()
    {
        return kaynak;
    }
}
