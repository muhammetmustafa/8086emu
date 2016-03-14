
package edu.yildiz.emu8086.derleyici.asm.komut;

import edu.yildiz.emu8086.derleyici.asm.operand.Operand;

/**
 *
 *  Bu şablon sınıf ADD, MOV gibi iki parametre alan assembly komutlarını temsil için kullanılır. 
 * 
 * @author MMC
 */
public abstract class CiftOperandliKomut extends Komut
{ 
    private final Operand hedef;
    private final Operand kaynak;

    public CiftOperandliKomut(Operand hedef, Operand kaynak)
    {
        this.hedef = hedef;
        this.kaynak = kaynak;
    }
    
    public Operand getHedef()
    {
        return hedef;
    }

    public Operand getKaynak()
    {
        return kaynak;
    }
}
