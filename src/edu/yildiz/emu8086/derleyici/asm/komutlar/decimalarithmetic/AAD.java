
package edu.yildiz.emu8086.derleyici.asm.komutlar.decimalarithmetic;

import edu.yildiz.emu8086.board.eu.islem.Islem;
import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import java.util.List;

/**
 *
 * @author MMC
 */
public class AAD extends OperandsizKomut
{
    public AAD() throws ParserException
    {
        throw new ParserException("AAD komutu henüz desteklenmiyor");
    }
    
    @Override
    public List<Islem> getIslemler()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
