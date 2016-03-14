
package edu.yildiz.emu8086.tipler;

import edu.yildiz.emu8086.util.Donusum;
import java.util.Arrays;

/**
 *
 * @author MMC
 */
public class Word 
{
    private static final byte YUKSEK = 0;
    private static final byte DUSUK = 1;
    
    /**
     * Yazmacin iceriginin tutuldugu deger. Dizi 2 elemanli. 
     * Yuksek anlamli byte = dizinin ilk elemani
     * Dusuk anlami byte = dizinin ikinci elemani
     */
    private byte[] deger = new byte[2];

    public Word()
    {
        this.deger[YUKSEK] = 0;
        this.deger[DUSUK] = 0;
    }
    
    /**
     * Integer değerin en düşük anlamlı 2 baytı ile 
     * Word oluşturur.
     * 
     * @param deger 
     */
    public Word(int deger)
    {
        this((byte)(deger>>8), (byte)(deger&0xFF));
    }
    
    /**
     * byte değeri işaretiyle Word'e genişletir.
     * 
     * @param deger 
     */
    public Word(byte deger)
    {
        this((int)deger);
    }
    
    public Word(int yuksek, int dusuk)
    {
        this((byte)yuksek, (byte)dusuk);
    }
    
    public Word(byte yuksek, byte dusuk)
    {
        this.deger[YUKSEK] = yuksek;
        this.deger[DUSUK] = dusuk;
    }
    
    public Word topla(Word sagTaraf)
    {
        return new Word(this.getDegerInt() + sagTaraf.getDegerInt());
    }
    
    public Word topla(byte sagTaraf)
    {
        return new Word(this.getDegerInt() + sagTaraf);
    }
    
    @Override
    public String toString()
    {
        return Donusum.decimalToHexadecimal(this.deger[YUKSEK], VeriUzunlugu.Byte_8) + 
               Donusum.decimalToHexadecimal(this.deger[DUSUK], VeriUzunlugu.Byte_8);
    }

    public String toStringBinary()
    {
        StringBuilder sb = new StringBuilder();
        
        for (byte i = 15; i >= 8 ; i--)
            sb.append(bitGet(i) ? 1 : 0);
        
        sb.append(":");
        
        for (byte i = 7; i >= 0 ; i--)
            sb.append(bitGet(i) ? 1 : 0);
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        Word w;
        
        if (obj instanceof Word)
            w = (Word) obj;
        else
            return false;
        
        return this.getH() == w.getH() &&
                this.getL() == w.getL();
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 53 * hash + Arrays.hashCode(this.deger);
        return hash;
    }
    
    public Word ikininKomplementi()
    {
        return new Word(-this.getDegerInt());
    }
    
    public void sonrakiByte()
    {
        this.setL((byte)(this.getL()+1));
    }
    
    public byte[] getDeger()
    {
        return this.deger;
    }
    
    public int getDegerInt()
    {
        int degerInt = 0;
        
        degerInt = Byte.toUnsignedInt(this.getH()) | degerInt;
        degerInt = degerInt << 8;
        degerInt = Byte.toUnsignedInt(this.getL()) | degerInt;
        
        return degerInt;
    }
    
    public void bitSet(int kacinciBit, boolean deger)
    {
        byte maskeDusuk = (byte)(1 << kacinciBit);
        byte maskeYuksek = (byte)(1 << (kacinciBit - 8));
        
        if (deger)
        {
            if (kacinciBit <= 7)
                this.setL(this.getL() | maskeDusuk);
            else
                this.setH(this.getH() | maskeYuksek);
        }
        else
        {
            maskeDusuk = (byte) ~maskeDusuk;
            maskeYuksek = (byte) ~maskeYuksek;
            
            if (kacinciBit <= 7)
                this.setL(this.getL() & maskeDusuk);
            else
                this.setH(this.getH() & maskeYuksek);
        }
    }
    
    /**
     * 
     * @param kacinciBit 0 ile 15 arasında değişen değer
     * @return 
     *      bit = 1 için true
     *      bit = 0 için false
     */
    public boolean bitGet(int kacinciBit)
    {
        byte maskeDusuk = (byte)(1 << kacinciBit);
        byte maskeYuksek = (byte)(1 << (kacinciBit - 8));
        
        if (kacinciBit <= 7)
            return (this.getL() & maskeDusuk) != 0;
        else
            return (this.getH() & maskeYuksek) != 0;
    }
    
    /**
     * Bu word 0'dan küçükse daha doğrusu
     * 15. biti 1 ise TRUE, aksi takdirde FALSE döndürür.
     * 
     * @return 
     */
    public boolean negatifMi()
    {
        return this.bitGet(15);
    }
    
    /**
     * Bu word sıfırsa TRUE,
     * aksi takdirde FALSE döndürür.
     * 
     * @return 
     */
    public boolean sifirMi()
    {
        return getH() == 0 && getL() == 0;
    }
    
    /**
     * Bu word'ün ikili karşılığındaki 1'leri sayısını
     * döndürür.
     * 
     * Örnek: 10101001 10100011  = 8
     * 
     * @return 
     */
    public int birBitSayisi()
    {
        return Integer.bitCount(Byte.toUnsignedInt(getH())) + 
                Integer.bitCount(Byte.toUnsignedInt(getL()));
    }
    
    public byte getH()
    {
        return this.deger[YUKSEK];
    }
    
    public String getStringH()
    {
        return Donusum.decimalToHexadecimal(this.getH());
    }
    
    public void setH(byte deger)
    {
        this.deger[YUKSEK] = deger;
    }
    
    public void setH(int deger)
    {
        this.setH((byte)deger);
    }
    
    public byte getL()
    {
        return this.deger[DUSUK];
    }
    
    public String getStringL()
    {
        return Donusum.decimalToHexadecimal(this.getL());
    }
    
    public void setL(byte deger)
    {
        this.deger[DUSUK] = deger;
    }
    
    public void setL(int deger)
    {
        this.setL((byte)deger);
    }
    
    public void setDeger(byte yuksek, byte dusuk)
    {
        this.deger[YUKSEK] = yuksek;
        this.deger[DUSUK] = dusuk;
    }
    
    public void setDeger(String yuksek, String dusuk)
    {
        this.setDeger(Byte.parseByte(yuksek), Byte.parseByte(dusuk));
    }
    
    public void setDeger(byte [] deger)
    {
        this.deger = deger;
    }
}
