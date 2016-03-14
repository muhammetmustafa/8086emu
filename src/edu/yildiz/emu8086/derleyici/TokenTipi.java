package edu.yildiz.emu8086.derleyici;

/**
 *
 * @author MMC
 */
public enum TokenTipi
{
    VIRGUL(","),
    ARTI("+"), 
    EKSI("-"),
    KOSELI_PARANTEZ_AC("["),
    KOSELI_PARANTEZ_KAPA("]"),
    IKI_NOKTA_UST_USTE(":"),
    SATIR_SONU,
    
    YAZMAC,                     
    LITERAL,
    
    OLU,
    EOF,
    
    ETIKET, 
    IDENTIFIER, 
    YORUM,
    KOMUT;
    
    private String karakter;

    private TokenTipi()
    {
    }
    
    private TokenTipi(String karakter)
    {
        this.karakter = karakter;
    }

    public String getKarakter()
    {
        return karakter;
    }
}
