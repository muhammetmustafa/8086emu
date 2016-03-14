
package edu.yildiz.emu8086.derleyici.asm.operand;

import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.Memory;
import edu.yildiz.emu8086.tipler.Word;
import edu.yildiz.emu8086.tipler.Yazmac;
import java.util.StringJoiner;

/**
 *  Bellek üzerinde işlem yapabilmek için 
 * hangi segment üzerinde işlem yapılacağı belirtilmeli ve offset'i 
 * ya sadece displacement'dan oluşmalı veya izin verilen (bp, bx, di, si)
 * yazmaç kombinasyonlarından biri ile oluşturulmalıdır. 
 * 
 * Eğer segmentOverride ve offset yazmaç kombinasyonlarından biri kullanılmazsa (Örneğin: [34h])
 * segmentOverride olarak Data Segment atanır, offset yazmaç kombinasyonlarından biri kullanılmışsa
 * (Örneğin: [bp + di + 43]) bu durumda segmentOverride bu offset yazmaçlarından tahmin edilir.
 * Bu tahmin aşağıdaki tabloya göre gerçekleşir:
 * 
 *     Yazmac1     Yazmac2     Segment
 *      BP          DI          Stack Segment (SS)
 *      BP          SI          Stack Segment (SS)
 *      BX          DI          Data Segment (DS)
 *      BX          SI          Data Segment (DS)
 *      SI          -           Data Segment (DS)
 *      DI          -           Data Segment (DS)
 *      BP          -           Stack Segment (SS)
 *      BX          -           Data Segment (DS)
 * 
 * 
 * @author MMC
 */
public class MemoryOperand extends Operand
{
    private Yazmac segment = null;
    private Yazmac[] offset = null;
    private ImmediateOperand displacement = null;
    
    /**
     * Bu değer EU içerisinde yukarıdaki offset dizisiyle
     * displacement değerini kullanılarak hesaplanır.
     */
    private Word hesaplanmisOffset = null;
    
    /**
     * MemoryOperand'dan okunacak veya yazılacak veri miktarı.
     */
    private VeriUzunlugu uzunluk = VeriUzunlugu.Word_16;
    
    /**
     * MemoryOperand'ının gösteriminin tutulduğu alan.
     */
    private String gosterim = null;
    
    /**
     * MemoryOperand üzerinde yapılacak işlem türü. OKU veya YAZ
     */
    private Memory.IslemTuru islemTuru;
    
    public MemoryOperand(Yazmac segmentOverride, 
        Yazmac[] offset, 
        ImmediateOperand displacement) throws ParserException
    {
        this.offset = offset;
        
        if (segmentOverride == null)
        {
            if (offset == null)
            {
                if (displacement == null)
                {
                    throw new ParserException("Memory operand hatası!");
                }
                else
                {
                    this.segment = Yazmac.DS; //Segment tahmini displacement'tan yapıldı
                    this.displacement = displacement;
                }
            }
            else
            {
                this.segment = this.segmentTahmini(offset);
                this.offset = offset;
                this.displacement = displacement;
            }
        }
        else
        {
            this.segment = segmentOverride;
            
            if (offset == null && displacement == null)
            {
                throw new ParserException("Memory operand hatası!");
            }
            else
            {
                this.offset = offset;
                this.displacement = displacement;
            }
        }
        
        this.gosterimOlustur(segmentOverride, offset, displacement);
    }
    
    private Yazmac segmentTahmini(Yazmac[] offset) throws ParserException
    {
        if (offset[0] == null)
        {
            if (offset[1] == null)
            {
                throw new ParserException("Memory operand hatası!");
            }
            else
            {
                if (offset[1] == Yazmac.BP)
                    return Yazmac.SS;
                else
                    return Yazmac.DS;
            }
        }
        else
        {
            if (offset[1] == null)
            {
                if (offset[0] == Yazmac.BP)
                    return Yazmac.SS;
                else
                    return Yazmac.DS;
            }
            else
            {
                if (offset[0] == Yazmac.BP || offset[1] == Yazmac.BP)
                    return Yazmac.SS;
                else
                    return Yazmac.DS;
            }
        }
    }
    
    private void gosterimOlustur(Yazmac segment, Yazmac[] offset, ImmediateOperand displacement)
    {
        //Boş veya ES:, SS:, 
        StringBuilder operand = new StringBuilder(segment == null ? "" : segment.name() + ":");
        
        //"[", "ES:[", "SS:["
        operand.append("[");
        
        if (offset != null)
        {
            StringJoiner sj = new StringJoiner("+");
            
            for (Yazmac y : offset)
                sj.add(y.name());
            
            //"[BX", "[BX+SI", "ES:[BX+SI", "ES:[BX"
            operand.append(sj.toString());
            
            //"[BX+38H", "ES:[BX+SI+38H]"
            if (displacement != null)
            {
                operand.append("+");
                operand.append(displacement);
            }
        }
        else
        {
            //"[38H", "ES:[4933H"
            operand.append(displacement);
        }
        
        operand.append("]");
        
        this.gosterim = operand.toString();
    }

    @Override
    public String toString()
    {
        return this.gosterim;
    }
    
    public ImmediateOperand getDisplacement()
    {
        return displacement;
    }
    
    public Yazmac getSegment()
    {
        return segment;
    }

    public Yazmac[] getOffset()
    {
        return offset;
    }

    public VeriUzunlugu getUzunluk()
    {
        return uzunluk;
    }

    public void setUzunluk(VeriUzunlugu uzunluk)
    {
        this.uzunluk = uzunluk;
    }

    public Memory.IslemTuru getIslemTuru()
    {
        return islemTuru;
    }

    public void setIslemTuru(Memory.IslemTuru islemTuru)
    {
        this.islemTuru = islemTuru;
    }

    public Word getHesaplanmisOffset()
    {
        return hesaplanmisOffset;
    }

    public void setHesaplanmisOffset(Word hesaplanmisOffset)
    {
        this.hesaplanmisOffset = hesaplanmisOffset;
    }
}
