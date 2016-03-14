
package edu.yildiz.emu8086.derleyici.asm.komutlar.decimalarithmetic;

import edu.yildiz.emu8086.board.eu.islem.Islem;
import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import java.util.List;

/**
 *
 * @author MMC
 */
public class AAA extends OperandsizKomut
{
    public AAA() throws ParserException
    {
        throw new ParserException("AAA işlemi henüz desteklenmiyor");
    }
    
    @Override
    public List<Islem> getIslemler()
    {
        return null;
    }
}
