
package edu.yildiz.emu8086.derleyici.asm.komutlar.logical;

import edu.yildiz.emu8086.board.eu.islem.AluIslemi;
import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.asm.komut.CiftOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.Yazmac;

/**
 *
 * @author MMC
 */
public class RCR extends CiftOperandliKomut
{
    public RCR(ByteRegisterOperand sol, ByteRegisterOperand sag) throws ParserException
    {
        super(sol, sag);
        
        if (sag.getYazmac() != Yazmac.CL)
            throw new ParserException("Rotate komutlarının sağ operandında sadece CL yazmacına izin verilmektedir.");
        
        this.ortakConst(sol, sag);
    }
    
    public RCR(ByteRegisterOperand sol, ByteImmediateOperand sag)
    {
        super(sol, sag);
        
        this.ortakConst(sol, sag);
    }
    
    public RCR(WordRegisterOperand sol, ByteRegisterOperand sag) throws ParserException
    {
        super(sol, sag);
        
        if (sag.getYazmac() != Yazmac.CL)
            throw new ParserException("Rotate komutlarının sağ operandında sadece CL yazmacına izin verilmektedir.");
        
        this.ortakConst(sol, sag);
    }
    
    public RCR(WordRegisterOperand sol, ByteImmediateOperand sag)
    {
        super(sol, sag);
        
        this.ortakConst(sol, sag);
    }
    
    public RCR(MemoryOperand sol, ByteRegisterOperand sag) throws ParserException
    {
        super(sol, sag);
        
        if (sag.getYazmac() != Yazmac.CL)
            throw new ParserException("Rotate komutlarının sağ operandında sadece CL yazmacına izin verilmektedir.");
        
        this.ortakConst(sol, sag);
    }
    
    public RCR(MemoryOperand sol, ByteImmediateOperand sag)
    {
        super(sol, sag);
        
        this.ortakConst(sol, sag);
    }
    
    private void ortakConst(Operand sol, Operand sag)
    {
        super.aluIslemiEkle(AluIslemi.Tur.RCR, VeriUzunlugu.Byte_8, sol, sag);
        super.veriTransferIslemiEkleKaynakAlu(sol);
    }
}
