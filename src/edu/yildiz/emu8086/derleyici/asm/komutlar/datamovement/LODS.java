
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.Yazmac;

/**
 *
 * @author MMC
 */
public class LODS extends TekOperandliKomut
{
    public LODS(MemoryOperand kaynak)
    {
        super(kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Byte_8);
        
        super.veriTransferIslemiEkle(new ByteRegisterOperand(Yazmac.AL), kaynak);
    }
}
