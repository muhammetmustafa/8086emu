
package edu.yildiz.emu8086.derleyici.asm.operand;

import edu.yildiz.emu8086.tipler.VeriUzunlugu;

/**
 *
 * @author MMC
 */
public abstract class ImmediateOperand extends Operand
{
    public enum ImmediateGosterimTuru{Binary, Decimal, Hexacedimal};
    
    public static ImmediateOperand tesbit(String deger) throws IllegalArgumentException
    {
        switch (uzunluk(deger))
        {
            case Byte_8: 
                return new ByteImmediateOperand(deger);
            case Word_16:
                return new WordImmediateOperand(deger);
            default: 
                throw new IllegalArgumentException(String.format("'%s' operandı desteklenmeyen boyutta", deger));
        }
    }
    
    public static String javaHexadecimal(String asmHexadecimal)
    {
        StringBuilder sb = new StringBuilder(asmHexadecimal);
        sb.deleteCharAt(sb.length() - 1); //sondaki H'yi veya h'yi sil.
        sb.insert(0, "0x"); //başa 0x ekle.
        return sb.toString();
    }
    
    public static VeriUzunlugu uzunluk(String deger) throws IllegalArgumentException
    {
        switch (gosterimTuru(deger))
        {
            case Decimal:
                return VeriUzunlugu.decimal(Integer.parseInt(deger));
            case Hexacedimal: 
                return VeriUzunlugu.hexadecimal(deger);
            default:
                throw new IllegalArgumentException("Binary gösterimler desteklenmiyor!");
        }
    }
    
    public static ImmediateGosterimTuru gosterimTuru(String deger)
    {
        if (deger.endsWith("H") || deger.endsWith("h"))
            return ImmediateGosterimTuru.Hexacedimal;
        else if (deger.endsWith("B") || deger.endsWith("b"))
            return ImmediateGosterimTuru.Binary;
        else
            return ImmediateGosterimTuru.Decimal;
    }
    
    public abstract void ikininKomplementi();
}
