
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.board.eu.Islem;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import java.util.List;

/**
 *
 * @author MMC
 */
public class PUSH extends TekOperandliKomut
{

    public PUSH(Operand hedef)
    {
        super(hedef);
    }

    @Override
    public List<Islem> getIslemler()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
