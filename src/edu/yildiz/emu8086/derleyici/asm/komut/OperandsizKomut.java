
package edu.yildiz.emu8086.derleyici.asm.komut;

/**
 * Bu sınıf STC, STD gibi parametre almayan assembly komutlarını temsil etmek için kullanılır.
 * Parametre almayan komutlar bu sınıfı kalıtımla almalıdırlar.
 * 
 * @author MMC
 */
public abstract class OperandsizKomut extends Komut
{
    @Override
    public String toString()
    {
        return getClass().getSimpleName();
    }
}
