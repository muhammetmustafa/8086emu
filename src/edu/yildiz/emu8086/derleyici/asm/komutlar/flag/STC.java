
package edu.yildiz.emu8086.derleyici.asm.komutlar.flag;

import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class STC extends OperandsizKomut
{
    public STC() 
    {
        super.bayrakIslemiEkle(FlagRegister.Tur.CARRY, true);
    }
}
