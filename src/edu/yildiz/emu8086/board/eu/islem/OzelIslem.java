
package edu.yildiz.emu8086.board.eu.islem;

/**
 *
 * @author MMC
 */
public class OzelIslem extends Islem
{
    private final String komutAdi;

    public OzelIslem(String komutAdi)
    {
        this.komutAdi = komutAdi;
    }

    public String getKomutAdi()
    {
        return komutAdi;
    }
}
