
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
public class JA extends TekOperandliKomut
{
    public JA(EtiketOperand hedef)
    {
        super(hedef);
        
        AtlamaIslemi islem = new AtlamaIslemi(hedef);
        islem.kosulEkle(new FlagOperand(FlagRegister.Tur.CARRY), AtlamaIslemi.Operator.ESIT, (byte)0);
        islem.kosulEkle(new FlagOperand(FlagRegister.Tur.ZERO), AtlamaIslemi.Operator.ESIT, (byte)0);
        
        super.atlamaIslemiEkle(islem);
    }
}
