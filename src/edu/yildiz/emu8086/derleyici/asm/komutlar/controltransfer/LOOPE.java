
package edu.yildiz.emu8086.derleyici.asm.komutlar.controltransfer;

import edu.yildiz.emu8086.board.eu.islem.AluIslemi;
import edu.yildiz.emu8086.board.eu.islem.AtlamaIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.FlagOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.FlagRegister;
import edu.yildiz.emu8086.tipler.Yazmac;

/**
 *
 * @author MMC
 */
public class LOOPE extends TekOperandliKomut
{
    public LOOPE(EtiketOperand hedef)
    {
        super(hedef);
        
        super.aluIslemiEkle(AluIslemi.Tur.DEC, VeriUzunlugu.Word_16, new WordRegisterOperand(Yazmac.CX));
        
        AtlamaIslemi islem = new AtlamaIslemi(hedef);
        islem.kosulEkle(new WordRegisterOperand(Yazmac.CX), AtlamaIslemi.Operator.ESIT_DEGIL, (byte)0);
        islem.kosulEkle(new FlagOperand(FlagRegister.Tur.ZERO), AtlamaIslemi.Operator.ESIT, (byte)1);
        
        super.atlamaIslemiEkle(islem);
    }
}
