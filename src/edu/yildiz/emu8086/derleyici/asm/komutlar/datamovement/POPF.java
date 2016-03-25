
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.board.eu.Islem;
import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import java.util.List;

/**
 *
 * @author MMC
 */
public class POPF extends OperandsizKomut
{
    public POPF() throws ParserException
    {
        throw new ParserException("AAD komutu henüz desteklenmiyor");
    }
    
    @Override
    public List<Islem> getIslemler()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
