
package edu.yildiz.emu8086.board.eu.islem;

import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MMC
 */
public class AtlamaIslemi extends Islem
{
    public enum Operator {ESIT, ESIT_DEGIL, BUYUK, KUCUK};
    
    private final List<Kosul> kosullar;
    private final EtiketOperand etiket;

    public AtlamaIslemi(EtiketOperand etiket)
    {
        this.kosullar = new ArrayList<>();
        this.etiket = etiket;
    }
    
    public AtlamaIslemi(EtiketOperand etiket, List<Kosul> kosullar)
    {
        this.kosullar = kosullar;
        this.etiket = etiket;
    }
    
    public void kosulEkle(Object solTaraf, Operator kosul, Object sagTaraf)
    {
        this.kosullar.add(new Kosul(solTaraf, kosul, sagTaraf));
    }

    public List<Kosul> getKosullar()
    {
        return kosullar;
    }

    public EtiketOperand getEtiket()
    {
        return etiket;
    }
    
    public class Kosul
    {    
        //Koşul sınıfında sol taraf ve sağ taraf 
        //Word, byte, boolean, Yazmac, Flag olabilir.
        //Karşılaştırma ikisi de aynı ve yukarıdaki türdense yapılır.
        private final Object solTaraf;
        private final Object sagTaraf;
        private final Operator kosul;

        public Kosul(Object solTaraf, Operator kosul, Object sagTaraf)
        {
            this.solTaraf = solTaraf;
            this.sagTaraf = sagTaraf;
            this.kosul = kosul;
        }

        public Operator getKosul()
        {
            return kosul;
        }

        public Object getSagTaraf()
        {
            return sagTaraf;
        }

        public Object getSolTaraf()
        {
            return solTaraf;
        }
    }
}
