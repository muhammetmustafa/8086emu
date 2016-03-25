
package edu.yildiz.emu8086.derleyici.asm.komutlar.logical;

import edu.yildiz.emu8086.board.eu.AluIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;

/**
 *
 * @author MMC
 */
public class NOT extends TekOperandliKomut
{
    public NOT(ByteRegisterOperand kaynak)
    {
        super(kaynak);
        
        super.aluIslemiEkle(AluIslemi.Tur.NOT, VeriUzunlugu.Byte_8, kaynak);
        super.veriTransferIslemiEkleKaynakAlu(kaynak);
    }

    public NOT(WordRegisterOperand kaynak)
    {
        super(kaynak);
        
        super.aluIslemiEkle(AluIslemi.Tur.NOT, VeriUzunlugu.Word_16, kaynak);
        super.veriTransferIslemiEkleKaynakAlu(kaynak);
    }
    
    public NOT(MemoryOperand kaynak)
    {
        super(kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Byte_8);
        
        super.aluIslemiEkle(AluIslemi.Tur.NOT, VeriUzunlugu.Byte_8, kaynak);
        super.veriTransferIslemiEkleKaynakAlu(kaynak);
    }
}
