
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.board.eu.Islem;
import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import java.util.List;

/**
 *
 * @author MMC
 */
public class MOVSW extends OperandsizKomut
{
    public MOVSW() throws ParserException
    {
        throw new ParserException("AAA işlemi henüz desteklenmiyor");
    }
    
    @Override
    public List<Islem> getIslemler()
    {
        return null;
    }
}
