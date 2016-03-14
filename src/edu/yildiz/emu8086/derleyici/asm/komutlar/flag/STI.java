
package edu.yildiz.emu8086.derleyici.asm.komutlar.flag;

import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class STI extends OperandsizKomut
{
    public STI()
    {
        super.bayrakIslemiEkle(FlagRegister.Tur.INTERRUPT_ENABLE, true);
    }
}
