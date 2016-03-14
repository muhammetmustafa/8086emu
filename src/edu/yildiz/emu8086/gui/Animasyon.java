
package edu.yildiz.emu8086.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author MMC
 */
public class Animasyon implements ActionListener
{
    public static final int ADIM_SAYISI = 10;
    private static final Logger kutukcu = Logger.getLogger(Animasyon.class.getSimpleName());
    
    private Timer t;
    private final Component hareketEden;
    private int hareketArasiBeklemeSuresi = 1000;
    private Point baslangicNoktasi;
    private Point bitisNoktasi;
    private Point gecerliKonum;
    
    //Hesaplanmış değerler
    private int xAdimUzunlugu;
    private int yAdimUzunlugu;
    
    public Animasyon(Component hareketEden)
    {
        this.hareketEden = hareketEden;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (yakinMi(gecerliKonum, bitisNoktasi))
        {
            hareketEden.setLocation(bitisNoktasi);
            //hareketEden.setVisible(false);
        
            //Animasyon bitmiştir.
            this.t.stop();
        }
        else
        {
            gecerliKonum.x += xAdimUzunlugu;
            gecerliKonum.y += yAdimUzunlugu;

            hareketEden.setLocation(gecerliKonum);
        }
    }
    
    public void basla()
    {
        xAdimUzunlugunuHesapla();
        yAdimUzunlugunuHesapla();
        
        hareketEden.setLocation(baslangicNoktasi);
        hareketEden.setVisible(true);
        
        t = new Timer(50, this);
        t.setRepeats(true);
        t.start();
    }
    
    private boolean yakinMi(Point p1, Point p2)
    {
        boolean x = xAdimUzunlugu > 0 ? (p1.x + xAdimUzunlugu > p2.x)
                                      :
                                        (p1.x + xAdimUzunlugu < p2.x);
        
        boolean y = yAdimUzunlugu > 0 ? (p1.y + yAdimUzunlugu > p2.y)
                                      :
                                        (p1.y + yAdimUzunlugu < p2.y);
        
        return x || y;
    }
    
    private void xAdimUzunlugunuHesapla()
    {
        int xMesafe = Math.max(baslangicNoktasi.x, bitisNoktasi.x) - 
                Math.min(baslangicNoktasi.x, bitisNoktasi.x);
        
        xAdimUzunlugu = xMesafe / ADIM_SAYISI;
        xAdimUzunlugu = xAdimUzunlugu * (bitisNoktasi.x > baslangicNoktasi.x ? 1 : -1);
    }
    
    private void yAdimUzunlugunuHesapla()
    {
        int yMesafe = Math.max(baslangicNoktasi.y, bitisNoktasi.y) - 
                        Math.min(baslangicNoktasi.y, bitisNoktasi.y);
        
        yAdimUzunlugu = yMesafe / ADIM_SAYISI;
        yAdimUzunlugu = yAdimUzunlugu * (bitisNoktasi.y > baslangicNoktasi.y ? 1 : -1);
    }
    
    public Component getHareketEden()
    {
        return hareketEden;
    }
    
    public Point getBaslangicNoktasi()
    {
        return baslangicNoktasi;
    }

    public void setBaslangicNoktasi(Point baslangicNoktasi)
    {
        this.baslangicNoktasi = baslangicNoktasi;
        this.gecerliKonum = (Point)baslangicNoktasi.clone();
    }

    public Point getBitisNoktasi()
    {
        return bitisNoktasi;
    }

    public void setBitisNoktasi(Point bitisNoktasi)
    {
        this.bitisNoktasi = bitisNoktasi;
    }

    public int getHareketArasiBeklemeSuresi()
    {
        return hareketArasiBeklemeSuresi;
    }

    public void setHareketArasiBeklemeSuresi(int hareketArasiBeklemeSuresi)
    {
        this.hareketArasiBeklemeSuresi = hareketArasiBeklemeSuresi;
    }
    
    public boolean isRunning()
    {
        return this.t.isRunning();
    }
    
}
