
package edu.yildiz.emu8086.derleyici.asm.komutlar.arithmetic;

import edu.yildiz.emu8086.board.eu.islem.AluIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;

/**
 *
 * @author MMC
 */
public class NEG extends TekOperandliKomut
{
    public NEG(ByteRegisterOperand hedef)
    {
        super(hedef);
        
        super.aluIslemiEkle(AluIslemi.Tur.NEG, VeriUzunlugu.Byte_8, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }

    public NEG(WordRegisterOperand hedef)
    {
        super(hedef);
        
        super.aluIslemiEkle(AluIslemi.Tur.NEG, VeriUzunlugu.Word_16, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
    
    public NEG(MemoryOperand hedef)
    {
        super(hedef);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        
        super.aluIslemiEkle(AluIslemi.Tur.INC, VeriUzunlugu.Byte_8, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
}
