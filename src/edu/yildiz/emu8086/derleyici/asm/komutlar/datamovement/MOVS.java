
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.board.eu.islem.Islem;
import edu.yildiz.emu8086.derleyici.asm.komut.CiftOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import java.util.List;

/**
 *
 * @author MMC
 */
public class MOVS extends CiftOperandliKomut
{
    public MOVS(Operand hedef, Operand kaynak)
    {
        super(hedef, kaynak);
    }
    
    @Override
    public List<Islem> getIslemler()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
