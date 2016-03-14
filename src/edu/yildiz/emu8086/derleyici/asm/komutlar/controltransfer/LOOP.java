
package edu.yildiz.emu8086.derleyici.asm.komutlar.controltransfer;

import edu.yildiz.emu8086.board.eu.islem.AluIslemi;
import edu.yildiz.emu8086.board.eu.islem.AtlamaIslemi;
import edu.yildiz.emu8086.board.eu.islem.Islem;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.FlagOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.FlagRegister;
import edu.yildiz.emu8086.tipler.Yazmac;
import java.util.List;

/**
 * Loop until CX = 0
 * 
 * Operand Kombinasyonları:
 * etiket
 * 
 * @author MMC
 */
public class LOOP extends TekOperandliKomut
{
    public LOOP(EtiketOperand hedef)
    {
        super(hedef);
        
        super.aluIslemiEkle(AluIslemi.Tur.DEC, VeriUzunlugu.Word_16, new WordRegisterOperand(Yazmac.CX));
        super.veriTransferIslemiEkleKaynakAlu(new WordRegisterOperand(Yazmac.CX));
        
        AtlamaIslemi islem = new AtlamaIslemi(hedef);
        islem.kosulEkle(new WordRegisterOperand(Yazmac.CX), AtlamaIslemi.Operator.ESIT_DEGIL, (byte)0);
        
        super.atlamaIslemiEkle(islem);
    }
    
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " " + this.getHedef();
    }
}
