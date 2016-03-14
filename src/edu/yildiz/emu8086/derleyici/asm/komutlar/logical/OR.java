
package edu.yildiz.emu8086.derleyici.asm.komutlar.logical;

import edu.yildiz.emu8086.board.eu.islem.AluIslemi;
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
public class OR extends CiftOperandliKomut
{
    public OR(ByteRegisterOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.OR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public OR(WordRegisterOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.OR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public OR(MemoryOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.OR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public OR(MemoryOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.OR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public OR(MemoryOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.OR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public OR(MemoryOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.OR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public OR(ByteRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Byte_8);
        this.OR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public OR(WordRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Word_16);
        this.OR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public OR(ByteRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.OR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public OR(WordRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.OR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public OR(WordRegisterOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.OR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    private void OR(Operand hedef, Operand kaynak, VeriUzunlugu uzunluk)
    {
        super.aluIslemiEkle(AluIslemi.Tur.OR, uzunluk, kaynak, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
}
