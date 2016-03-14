
package edu.yildiz.emu8086.derleyici.asm.komutlar.flag;

import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class CMC extends OperandsizKomut
{
    public CMC()
    {
        super.bayrakIslemiEkle(FlagRegister.Tur.CARRY);
    }
}
