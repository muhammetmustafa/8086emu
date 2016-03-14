
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
public class AND extends CiftOperandliKomut
{
    public AND(ByteRegisterOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.AND(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public AND(WordRegisterOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.AND(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public AND(MemoryOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.AND(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public AND(MemoryOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.AND(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public AND(MemoryOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.AND(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public AND(MemoryOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.AND(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public AND(ByteRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Byte_8);
        this.AND(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public AND(WordRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Word_16);
        this.AND(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public AND(ByteRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.AND(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public AND(WordRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.AND(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public AND(WordRegisterOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.AND(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    private void AND(Operand hedef, Operand kaynak, VeriUzunlugu uzunluk)
    {
        super.aluIslemiEkle(AluIslemi.Tur.AND, uzunluk, kaynak, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
}
