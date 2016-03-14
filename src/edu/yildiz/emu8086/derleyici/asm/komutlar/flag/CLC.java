
package edu.yildiz.emu8086.derleyici.asm.komutlar.flag;

import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class CLC extends OperandsizKomut
{
    public CLC()
    {
        super.bayrakIslemiEkle(FlagRegister.Tur.CARRY, false);
    }
}
