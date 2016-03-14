
package edu.yildiz.emu8086.derleyici.asm.komutlar.arithmetic;

import edu.yildiz.emu8086.board.eu.islem.AluIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;

/**
 * Increment by 1
 * 
 *  Operand Kombinasyonları:
 * reg8
 * mem
 * reg16
 * 
 * @author MMC
 */
public class INC extends TekOperandliKomut
{
    public INC(ByteRegisterOperand hedef)
    {
        super(hedef);
        
        super.aluIslemiEkle(AluIslemi.Tur.INC, VeriUzunlugu.Byte_8, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }

    public INC(WordRegisterOperand hedef)
    {
        super(hedef);
        
        super.aluIslemiEkle(AluIslemi.Tur.INC, VeriUzunlugu.Word_16, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
    
    public INC(MemoryOperand hedef)
    {
        super(hedef);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        
        super.aluIslemiEkle(AluIslemi.Tur.INC, VeriUzunlugu.Byte_8, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " " + this.getHedef();
    }
}
