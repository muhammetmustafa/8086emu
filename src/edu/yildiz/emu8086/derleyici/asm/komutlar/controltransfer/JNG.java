
package edu.yildiz.emu8086.derleyici.asm.komutlar.controltransfer;

import edu.yildiz.emu8086.board.eu.islem.AtlamaIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.FlagOperand;
import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class JNG extends TekOperandliKomut
{
    public JNG(EtiketOperand hedef)
    {
        super(hedef);
        
        AtlamaIslemi islem = new AtlamaIslemi(hedef);
        islem.kosulEkle(new FlagOperand(FlagRegister.Tur.ZERO), AtlamaIslemi.Operator.ESIT, (byte)1);
        islem.kosulEkle(new FlagOperand(FlagRegister.Tur.SIGN), AtlamaIslemi.Operator.ESIT_DEGIL, 
            new FlagOperand(FlagRegister.Tur.OVERFLOW));
        
        super.atlamaIslemiEkle(islem);
    }
}
