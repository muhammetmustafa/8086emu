
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.board.eu.islem.Islem;
import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import java.util.List;

/**
 *
 * @author MMC
 */
public class PUSHF extends OperandsizKomut
{
    public PUSHF() throws ParserException
    {
        throw new ParserException("AAA işlemi henüz desteklenmiyor");
    }
    
    @Override
    public List<Islem> getIslemler()
    {
        return null;
    }
}
