
package edu.yildiz.emu8086.board.eu.islem;

import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;

/**
 *
 * @author MMC
 */
public class AluIslemi extends Islem
{
    public enum Tur
    {
        ADD,
        ADC,
        INC,
        DEC,
        
        MUL,
        
        RCL,
        RCR,
        ROL,
        ROR,
        
        AND,
        OR,
        NOT,
        NEG,
        XOR
    }
    
    private final Tur aluIslemTuru;
    private final Operand sol; 
    private final Operand sag;
    private final VeriUzunlugu sonucBoyutu;
    
    public AluIslemi(Tur aluIslemTuru, VeriUzunlugu sonucBoyutu, Operand sol, Operand sag)
    {
        this.aluIslemTuru = aluIslemTuru;
        this.sol = sol;
        this.sag = sag;
        this.sonucBoyutu = sonucBoyutu;
    }

    public Tur getAluIslemTuru()
    {
        return aluIslemTuru;
    }

    public Operand getSag()
    {
        return sag;
    }

    public Operand getSol()
    {
        return sol;
    }

    public VeriUzunlugu getSonucBoyutu()
    {
        return sonucBoyutu;
    }
}
