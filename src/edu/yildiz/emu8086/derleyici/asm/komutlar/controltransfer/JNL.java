
package edu.yildiz.emu8086.derleyici.asm.komutlar.controltransfer;

import edu.yildiz.emu8086.board.eu.AtlamaIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.FlagOperand;
import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class JNL extends TekOperandliKomut
{
    public JNL(EtiketOperand hedef)
    {
        super(hedef);
        
        AtlamaIslemi islem = new AtlamaIslemi(hedef);
        islem.kosulEkle(new FlagOperand(FlagRegister.Tur.SIGN), AtlamaIslemi.Operator.ESIT, 
            new FlagOperand(FlagRegister.Tur.OVERFLOW));
        
        super.atlamaIslemiEkle(islem);
    }
}
