
package edu.yildiz.emu8086.derleyici.asm.komutlar.arithmetic;

import edu.yildiz.emu8086.board.eu.islem.AluIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;

/**
 *
 * @author MMC
 */
public class DEC extends TekOperandliKomut
{
    public DEC(ByteRegisterOperand hedef)
    {
        super(hedef);
        
        super.aluIslemiEkle(AluIslemi.Tur.DEC, VeriUzunlugu.Byte_8, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }

    public DEC(WordRegisterOperand hedef)
    {
        super(hedef);
        
        super.aluIslemiEkle(AluIslemi.Tur.DEC, VeriUzunlugu.Word_16, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
    
    public DEC(MemoryOperand hedef)
    {
        super(hedef);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        
        super.aluIslemiEkle(AluIslemi.Tur.DEC, VeriUzunlugu.Byte_8, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }

}
