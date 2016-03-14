
package edu.yildiz.emu8086.derleyici.asm.komutlar.arithmetic;

import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;

/**
 * Convert byte to word (signed)
 * 
 * @author MMC
 */
public class CBW extends OperandsizKomut
{
    public CBW()
    {
        super.ozelIslemEkle("CBW");
    }
    
}
