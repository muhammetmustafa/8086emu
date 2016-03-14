
package edu.yildiz.emu8086.board.eu;

import static edu.yildiz.emu8086.board.eu.EU.kutukcu;
import edu.yildiz.emu8086.derleyici.asm.komut.CiftOperandliKomut;
import edu.yildiz.emu8086.derleyici.asm.komut.Komut;
import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import edu.yildiz.emu8086.derleyici.asm.komut.TekOperandliKomut;
import java.util.logging.Logger;

/**
 *
 * @author MMC
 */
public class InstructionDecoder 
{
    private static final Logger kutukcu = Logger.getLogger(InstructionDecoder.class.getName());
    
    public static void decode(Komut komut)
    {
        kutukcu.info("Komut decoderde çözülüyor...");
        
        String komutAdi = komut.getClass().getSimpleName();
        
        if (komut instanceof OperandsizKomut)
        {
            //Burdaki işlemin amacı sadece burdaki kısmın anlamsı iş yapmasıdır.
            OperandsizKomut pk = (OperandsizKomut) komut;
        }
        else if (komut instanceof TekOperandliKomut)
        {
            TekOperandliKomut tpk = (TekOperandliKomut) komut;
            
            komutAdi = komutAdi + " " + tpk.getHedef();
        }
        else
        {
            CiftOperandliKomut ipk = (CiftOperandliKomut) komut;
            
            komutAdi = komutAdi + " " + ipk.getHedef() + ", " + ipk.getKaynak();
        }
        
        kutukcu.info("Komut çözüldü: " + komutAdi);
    }
}
