
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.derleyici.asm.komut.CiftOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.SegmentRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.Memory;

/**
 *
 * @author MMC
 */
public class MOV extends CiftOperandliKomut
{   
    public MOV(ByteRegisterOperand sol, ByteRegisterOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(WordRegisterOperand sol, WordRegisterOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(MemoryOperand sol, ByteRegisterOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(MemoryOperand sol, WordRegisterOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(ByteRegisterOperand sol, MemoryOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(WordRegisterOperand sol, MemoryOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
     
    public MOV(MemoryOperand sol, ByteImmediateOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(MemoryOperand sol, WordImmediateOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(ByteRegisterOperand sol, ByteImmediateOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(WordRegisterOperand sol, ByteImmediateOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, new WordImmediateOperand(sag.getImmediate()));
    }
    
    public MOV(WordRegisterOperand sol, WordImmediateOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }

    public MOV(SegmentRegisterOperand sol, WordRegisterOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(WordRegisterOperand sol, SegmentRegisterOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(SegmentRegisterOperand sol, MemoryOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    public MOV(MemoryOperand sol, SegmentRegisterOperand sag)
    {
        super(sol, sag);
        super.veriTransferIslemiEkle(sol, sag);
    }
    
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " " + this.getHedef() + ", " + this.getKaynak();
    }
}
