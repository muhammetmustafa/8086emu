package edu.yildiz.emu8086.tipler;

/**
 *
 * @author MMC
 */
public enum VeriUzunlugu
{
    Byte_8 (8, 1, "0"),
    Word_16 (16, 2, "1")
    ;
    
    private int bitSayisi;
    private int baytSayisi;
    private String sablonDegeri;
    
    private VeriUzunlugu(int bitSayisi, int baytSayisi)
    {
        this.bitSayisi = bitSayisi;
        this.baytSayisi = baytSayisi;
    }

    private VeriUzunlugu(int bitSayisi, int baytSayisi, String sablonDegeri)
    {
        this.bitSayisi = bitSayisi;
        this.baytSayisi = baytSayisi;
        this.sablonDegeri = sablonDegeri;
    }

    /**
     * Bayt miktarı olarak verilen argümanın bu enumdaki karşılığı döner.
     * Örneğin 1 byte için Byte, 2 byte için Word gibi.
     * @param kacBayt
     * @return 
     */
    public static VeriUzunlugu bytedan(int kacBayt)
    {
        switch (kacBayt)
        {
            case 1: return Byte_8;
            case 2: return Word_16;
            default : return null;
        }
    }
    
    /**
     * Bir integer sayinin kaç byte yer kapladığını bulur. Örneğin 140 sayısı 1 byte
     * , 500 sayısı 2 byte gibi.
     * @param sayi
     * @return 
     */
    public static VeriUzunlugu decimal(int sayi) throws IllegalArgumentException
    {
        if (sayi >= Byte.MIN_VALUE && sayi <= Byte.MAX_VALUE)
            return Byte_8; 
        else if (sayi >= Short.MIN_VALUE && sayi <= Short.MAX_VALUE)
            return Word_16;
        else
            throw new IllegalArgumentException("2 byte'dan büyük sayılar desteklenmiyor");
    }
    
    /**
     * Bir integer sayinin kaç byte yer kapladığını bulur. Örneğin 140 sayısı 1 byte
     * , 500 sayısı 2 byte gibi.
     * @param sayi
     * @return 
     */
    public static VeriUzunlugu decimal(String sayi) throws IllegalArgumentException
    {
        return decimal(Integer.parseInt(sayi));
    }
    
    public static VeriUzunlugu hexadecimal(String sayi) throws IllegalArgumentException
    {
        //sayi 0aa34h, 0bcH şeklindeyse baştaki sıfırı at.
        if (sayi.startsWith("0"))
            sayi = sayi.substring(1);
        
        if (sayi.length() == 3) //12h, 12H
            return Byte_8;
        else if (sayi.length() == 5) //3424h, 1123h
            return Word_16;
        else
            throw new IllegalArgumentException(String.format("'%s' hexadecimal sayısı doğru uzunlukta değil!", sayi));
    }
    
    public int getBitSayisi()
    {
        return bitSayisi;
    }

    public int getBaytSayisi()
    {
        return baytSayisi;
    }
    
    public String getSablonDegeri()
    {
        return sablonDegeri;
    }
    
    
}
