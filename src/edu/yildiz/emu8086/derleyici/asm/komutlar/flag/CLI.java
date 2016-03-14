
package edu.yildiz.emu8086.derleyici.asm.komutlar.flag;

import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class CLI extends OperandsizKomut
{
    public CLI()
    {
        super.bayrakIslemiEkle(FlagRegister.Tur.INTERRUPT_ENABLE, false);
    }
}
