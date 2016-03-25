
package edu.yildiz.emu8086.derleyici.asm.komutlar.arithmetic;

import edu.yildiz.emu8086.board.eu.AluIslemi;
import edu.yildiz.emu8086.derleyici.asm.komut.CiftOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.MemoryOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordImmediateOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.WordRegisterOperand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;

/**
 *   Add with carry flag
 * 
 *   Operand Kombinasyonları:
 *      reg, reg
 *      mem, reg
 *      reg, mem
 *      reg, imm
 *      mem, imm
 *      acc, imm
 *      
 * @author MMC
 */
public class ADC extends CiftOperandliKomut
{
    public ADC(ByteRegisterOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.ADC(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADC(WordRegisterOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.ADC(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADC(MemoryOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.ADC(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADC(MemoryOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.ADC(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADC(MemoryOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.ADC(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADC(MemoryOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.ADC(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADC(ByteRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Byte_8);
        this.ADC(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADC(WordRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Word_16);
        this.ADC(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADC(ByteRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.ADC(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADC(WordRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.ADC(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADC(WordRegisterOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.ADC(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    private void ADC(Operand hedef, Operand kaynak, VeriUzunlugu uzunluk)
    {
        super.aluIslemiEkle(AluIslemi.Tur.ADC, uzunluk, kaynak, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
    
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " " + this.getHedef() + ", " + this.getKaynak();
    }
}
