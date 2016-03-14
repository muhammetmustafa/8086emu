
package edu.yildiz.emu8086.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author MMC
 */
public class TestUtil 
{
    public static String testiOku()
    {
        InputStream is = ClassLoader.getSystemResourceAsStream("edu/yildiz/emu8086/util/asmkod");
        InputStreamReader r = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        String satir;
        
        try
        {
            while((satir = br.readLine()) != null)
                sb.append(satir+ "\n");
        } 
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        
        return sb.toString();
    }
}
