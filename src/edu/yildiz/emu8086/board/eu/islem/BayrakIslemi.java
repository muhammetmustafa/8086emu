
package edu.yildiz.emu8086.board.eu.islem;

import edu.yildiz.emu8086.tipler.FlagRegister;

/**
 *
 * @author MMC
 */
public class BayrakIslemi extends Islem
{
    FlagRegister.Tur bayrak;
    Boolean deger;
    
    public BayrakIslemi(FlagRegister.Tur bayrak, boolean deger)
    {
        this.bayrak = bayrak;
        this.deger = deger;
    }
    
    //Bayrağın değeri ters çevrilecek (Complement)
    public BayrakIslemi(FlagRegister.Tur bayrak)
    {
        this.bayrak = bayrak;
        this.deger = null;
    }
    
    public FlagRegister.Tur getBayrak()
    {
        return bayrak;
    }
    
    public Boolean getDeger()
    {
        return this.deger;
    }
}
