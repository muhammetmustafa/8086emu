package edu.yildiz.emu8086.util;

import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.util.Donusum;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class DonusumTest
{
    
    public DonusumTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void donusumTest()
    {
        System.out.println(Donusum.decimalToBinary(132, VeriUzunlugu.Byte_8));
    }
}
