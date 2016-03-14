
package edu.yildiz.emu8086.derleyici;

/**
 *
 * @author MMC
 */
public class ParserException extends Exception
{
    int satir;
    int sutun;
    
    public ParserException(String message)
    {
        this(message, 0, 0);
    }
    
    public ParserException(String message, int satir, int sutun)
    {
        super(message);
        this.satir = satir;
        this.sutun = sutun;
    }

    public int getSatir()
    {
        return satir;
    }

    public int getSutun()
    {
        return sutun;
    }
}
