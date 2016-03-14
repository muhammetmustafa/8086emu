
package edu.yildiz.emu8086.derleyici.asm.komutlar.flag;

import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class CLD extends OperandsizKomut
{
    public CLD()
    {
        super.bayrakIslemiEkle(FlagRegister.Tur.DIRECTION, false);
    }
}
