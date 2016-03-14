
package edu.yildiz.emu8086.derleyici.asm.operand;

import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.Token;
import edu.yildiz.emu8086.tipler.Yazmac;
import java.util.Locale;

/**
 *
 * @author MMC
 */
public abstract class RegisterOperand extends Operand
{
    private final Yazmac yazmac;

    public RegisterOperand(Yazmac yazmac)
    {
        this.yazmac = yazmac;
    }
    
    public static RegisterOperand tesbit(Token yazmac) throws ParserException
    {
        Yazmac yazmacTipi = Yazmac.valueOf(yazmac.text.toUpperCase(Locale.ROOT));
        switch (yazmacTipi)
        {
            case BL: case CL: case DL: case AL: 
            case AH: case BH: case CH: case DH: 
                return new ByteRegisterOperand(yazmacTipi);
            case CS: case DS: case ES: case SS: 
                return new SegmentRegisterOperand(yazmacTipi);
            default:  
                return new WordRegisterOperand(yazmacTipi);
        }
    }

    @Override
    public String toString()
    {
        return this.yazmac.name();
    }
    
    public Yazmac getYazmac()
    {
        return yazmac;
    }
}
