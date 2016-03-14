
package gui;

import edu.yildiz.emu8086.gui.Animasyon;
import java.awt.Point;
import javax.swing.JLabel;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class AnimasyonTest 
{
    @Test
    public void animasyon_test()
    {
        JLabel label = new JLabel();
        label.setLocation(34, 88);
        
        Point baslangic = new Point(43,88);
        Point hedef = new Point(23, 529);
        
        Animasyon animasyon = new Animasyon(label);
        animasyon.setBaslangicNoktasi(baslangic);
        animasyon.setBitisNoktasi(hedef);
        
        animasyon.basla();
    }
}
