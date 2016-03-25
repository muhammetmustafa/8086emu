
package edu.yildiz.emu8086.derleyici.asm.komutlar.arithmetic;

import edu.yildiz.emu8086.board.eu.Islem;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import java.util.List;

/**
 *
 * @author MMC
 */
public class DIV extends TekOperandliKomut
{

    public DIV(Operand hedef)
    {
        super(hedef);
    }

    @Override
    public List<Islem> getIslemler()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
