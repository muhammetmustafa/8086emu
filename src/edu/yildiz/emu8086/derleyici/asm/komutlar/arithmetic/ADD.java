
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
 *
 * @author MMC
 */
public class ADD extends CiftOperandliKomut
{
    public ADD(ByteRegisterOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.ADD(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADD(WordRegisterOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.ADD(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADD(MemoryOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.ADD(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADD(MemoryOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.ADD(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADD(MemoryOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.ADD(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADD(MemoryOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.ADD(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADD(ByteRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Byte_8);
        this.ADD(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADD(WordRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Word_16);
        this.ADD(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADD(ByteRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.ADD(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public ADD(WordRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.ADD(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public ADD(WordRegisterOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.ADD(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    private void ADD(Operand hedef, Operand kaynak, VeriUzunlugu uzunluk)
    {
        super.aluIslemiEkle(AluIslemi.Tur.ADD, uzunluk, kaynak, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
    
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " " + this.getHedef() + ", " + this.getKaynak();
    }
}
