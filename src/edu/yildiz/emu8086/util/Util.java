
package edu.yildiz.emu8086.util;

import edu.yildiz.emu8086.tipler.Word;

/**
 *
 * @author MMC
 */
public class Util 
{   
    /**
     * Verilen değerin kaçıncı bitinin ne olduğunu döndürür.
     * Değer byte veya Word olabilir.
     * 
     * @param deger
     * @param kacinciBit
     * @return 
     */
    public static boolean bitGet(Object deger, int kacinciBit)
    {
        if (deger instanceof Byte)
        {
            byte degerB = (byte)deger;
            byte maske = (byte)(1 << kacinciBit);
            
            return (degerB & maske) != 0;
        }
        else
        {
            return ((Word)deger).bitGet(kacinciBit);
        }
    }
    
    /**
     * Verilen değerin kaçıncı bitinin ne olduğunu döndürür.
     * Değer byte veya Word olabilir.
     * 
     * @param hedef
     * @param deger
     * @param kacinciBit
     * @return 
     */
    public static byte bitSet(byte hedef, int kacinciBit, boolean deger)
    {
        byte maske = (byte)(1 << kacinciBit);
        
        if (deger)
            return (byte)(hedef | maske);
        else
            return (byte)(hedef & maske);
    }
    
    /**
     * Verilen değerin en değerli bitini ( :) ) döndürür.
     * Değer byte veya Word olabilir.
     * 
     * @param deger
     * @return 
     */
    public static boolean enDegerliBit(Object deger)
    {
        return bitGet(deger, (deger instanceof Byte) ? 8 : 16);
    }
    
    /**
     * Verilen değerin en değerli bitini ( :) ) döndürür.
     * Değer byte veya Word olabilir.
     * 
     * @param deger
     * @return 
     */
    public static boolean ikinciEnDegerliBit(Object deger)
    {
        return bitGet(deger, (deger instanceof Byte) ? 7 : 15);
    }
}
