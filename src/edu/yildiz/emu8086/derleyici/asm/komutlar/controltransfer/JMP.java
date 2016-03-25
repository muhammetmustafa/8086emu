
package edu.yildiz.emu8086.derleyici.asm.komutlar.controltransfer;

import edu.yildiz.emu8086.board.eu.AtlamaIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;

/**
 *
 * @author MMC
 */
public class JMP extends TekOperandliKomut
{
    public JMP(EtiketOperand hedef)
    {
        super(hedef);
        
        AtlamaIslemi islem = new AtlamaIslemi(hedef);
        
        super.atlamaIslemiEkle(islem);
    }
}
