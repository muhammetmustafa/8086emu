
package edu.yildiz.emu8086.derleyici.asm.komutlar.logical;

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
public class XOR extends CiftOperandliKomut
{
    public XOR(ByteRegisterOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.XOR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public XOR(WordRegisterOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        this.XOR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public XOR(MemoryOperand hedef, ByteRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.XOR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public XOR(MemoryOperand hedef, WordRegisterOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.XOR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public XOR(MemoryOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Byte_8);
        this.XOR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public XOR(MemoryOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        
        hedef.setUzunluk(VeriUzunlugu.Word_16);
        this.XOR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public XOR(ByteRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Byte_8);
        this.XOR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public XOR(WordRegisterOperand hedef, MemoryOperand kaynak)
    {
        super(hedef, kaynak);
        
        kaynak.setUzunluk(VeriUzunlugu.Word_16);
        this.XOR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public XOR(ByteRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.XOR(hedef, kaynak, VeriUzunlugu.Byte_8);
    }
    
    public XOR(WordRegisterOperand hedef, ByteImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.XOR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    public XOR(WordRegisterOperand hedef, WordImmediateOperand kaynak)
    {
        super(hedef, kaynak);
        this.XOR(hedef, kaynak, VeriUzunlugu.Word_16);
    }
    
    private void XOR(Operand hedef, Operand kaynak, VeriUzunlugu uzunluk)
    {
        super.aluIslemiEkle(AluIslemi.Tur.XOR, uzunluk, kaynak, hedef);
        super.veriTransferIslemiEkleKaynakAlu(hedef);
    }
}
