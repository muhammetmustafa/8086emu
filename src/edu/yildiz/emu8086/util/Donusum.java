package edu.yildiz.emu8086.util;

import edu.yildiz.emu8086.tipler.VeriUzunlugu;

/**
 *
 * @author MMC
 */
public class Donusum 
{
    public static int binaryToDecimal(String binSayi, VeriUzunlugu sonucUzunlugu)
    {
        try
        {
            binSayi = binSayi.toUpperCase();
            if (binSayi.charAt(binSayi.length() - 1) == 'B')
                binSayi = binSayi.substring(0, binSayi.length() - 1);
            int decSayi = Integer.parseInt(binSayi, 2);
            if (sonucUzunlugu == VeriUzunlugu.Byte_8)
                decSayi = decSayi % 256;
            return decSayi;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return 0;
        }
    }
    
    public static String decimalToBinary(byte decSayi, VeriUzunlugu sonucUzunlugu)
    {
        return decimalToBinary(Byte.toUnsignedInt(decSayi), sonucUzunlugu);
    }
    
    public static String decimalToBinary(int decSayi, VeriUzunlugu sonucUzunlugu)
    {
        try
        {
            if (sonucUzunlugu == VeriUzunlugu.Byte_8)
            {
                decSayi = (decSayi + 256) % 256;
                String binSayi = Integer.toString(decSayi, 2);
                binSayi = "00000000".substring(binSayi.length()) + binSayi.toUpperCase();
                return binSayi;
            }
            else if (sonucUzunlugu == VeriUzunlugu.Word_16)
            {
                decSayi = (decSayi + 65536) % 65536;
                String binSayi = Integer.toString(decSayi, 2);
                binSayi = "0000000000000000".substring(binSayi.length()) + binSayi.toUpperCase();
                return binSayi;
            }
            return "";
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return "";
        }
    }
    
    public static String decimalToHexadecimal(byte decSayi)
    {
        return decimalToHexadecimal(decSayi, VeriUzunlugu.Byte_8);
    }
    
    public static String decimalToHexadecimal(int decSayi, VeriUzunlugu sonucUzunlugu)
    {
        try
        {
            if (sonucUzunlugu == VeriUzunlugu.Byte_8)
            {
                decSayi = (decSayi + 256) % 256;
                String hexSayi = Integer.toString(decSayi, 16);
                hexSayi = "00".substring(hexSayi.length()) + hexSayi.toUpperCase();
                return hexSayi;
            }
            else if (sonucUzunlugu == VeriUzunlugu.Word_16)
            {
                decSayi = (decSayi + 65536) % 65536;
                String hexSayi = Integer.toString(decSayi, 16);
                hexSayi = "0000".substring(hexSayi.length()) + hexSayi.toUpperCase();
                return hexSayi;
            }
            return "";
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return "";
        }
    }
    
    public static int hexadecimalToDecimal(String hexSayi, VeriUzunlugu sonucUzunlugu)
    {
        try
        {
            hexSayi = hexSayi.toUpperCase();
            if (hexSayi.charAt(hexSayi.length() - 1) == 'H')
                hexSayi = hexSayi.substring(0, hexSayi.length() - 1);
            int decSayi = Integer.parseInt(hexSayi, 16);
            if (sonucUzunlugu == VeriUzunlugu.Byte_8)
                decSayi = decSayi % 256;
            return decSayi;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return 0;
        }
    }
}
