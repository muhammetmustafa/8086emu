
package edu.yildiz.emu8086.derleyici.asm.komutlar.controltransfer;

import edu.yildiz.emu8086.board.eu.islem.AtlamaIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.Yazmac;

/**
 *
 * @author MMC
 */
public class JCXZ extends TekOperandliKomut
{
    public JCXZ(EtiketOperand hedef)
    {
        super(hedef);
        
        AtlamaIslemi islem = new AtlamaIslemi(hedef);
        islem.kosulEkle(new WordRegisterOperand(Yazmac.CX), AtlamaIslemi.Operator.ESIT, (byte)0);
        
        super.atlamaIslemiEkle(islem);
    }

}
