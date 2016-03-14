
package edu.yildiz.emu8086.derleyici;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import edu.yildiz.emu8086.derleyici.asm.Assembly;
import edu.yildiz.emu8086.derleyici.asm.komut.Komut;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import edu.yildiz.emu8086.derleyici.asm.operand.OperandFabrikasi;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author MMC
 */
public class Parser 
{
    private Tokenizer tokenizer;
    private Map<Token, Object> parseListesi;
    private ImmutableSet<ClassPath.ClassInfo> komutSiniflari;
    
    private Parser(String kod) throws ParserException
    {
        try
        {
            this.tokenizer = new Tokenizer(kod);
            this.parseListesi = new TreeMap<>();
            this.komutSiniflari = ClassPath.from(ClassLoader.getSystemClassLoader())
                                       .getTopLevelClassesRecursive("edu.yildiz.emu8086.derleyici.asm.komutlar");
        } 
        catch (ParserException e)
        {
            throw e;
        }
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    public static Assembly parse(String kod) throws ParserException
    {
        Parser parser = new Parser(kod);
        
        return parser.parcala();
    }
    
    private Assembly parcala() throws ParserException
    {
        Assembly asm = new Assembly();
        
        ilkGecis();
        ikinciGecis();
        ucuncuGecis(asm.getKomutlar(), asm.getEtiketler());
        
        return asm;
    }
    /**
     * İlk geçişte aşağıdaki işlemler yapılıyor.
     * 
     *  1. Etiketlerin tesbiti. Identifier + ":" ikilileri tesbit edilip bunun
     * yerine ETIKET turunde token oluşturulup yerleştirilmesi.
     * 
     */
    private void ilkGecis()
    {
        ArrayList<Token> tokenlar = this.tokenizer.getTokens();
        for (int i = 0; i < tokenlar.size(); i++)
        {
            Token t = tokenlar.get(i);
            
            switch (t.type)
            {
                case IDENTIFIER:
                {
                    //Sonraki tokena göz atalım.
                    //Eğer iki nokta üst üste karakteri ise bu kesinlikle etikettir.
                    if (tokenlar.get(i+1).type == TokenTipi.IKI_NOKTA_UST_USTE)
                    {
                        //Şimdik tokenı etiket olarak değiştir.
                        t.type = TokenTipi.ETIKET;
                        
                        //İki nokta üst üste tokenını kaldır.
                        tokenlar.get(i+1).type = TokenTipi.OLU;
                    }
                }
                default: 
                    break;
            } //switch sonu
        } //döngü sonu
    }
    
    private void ikinciGecis() throws ParserException
    {
        ArrayList<Operand> parametreler = new ArrayList<>();
        ArrayList<Token> parametreTokenlari = new ArrayList<>();
        ArrayList<Token> tokenlar = this.tokenizer.getTokens();
        Token oncekiKomutTokeni = null;
        
        for (int i = 0; i < tokenlar.size(); i++)
        {
            Token t = tokenlar.get(i);
            
            switch (t.type)
            {
                case KOMUT: case EOF:
                {
                    if (oncekiKomutTokeni != null)
                    {
                        if (parametreTokenlari.size() > 0)
                        {
                            try
                            {
                                parametreler.add(OperandFabrikasi.yeniOperand(parametreTokenlari));
                            } 
                            catch (ParserException e)
                            {
                                throw e;
                            }
                            
                            parametreTokenlari = new ArrayList<>();
                        }
                        
                        this.parseListesi.put(oncekiKomutTokeni, 
                            this.yeniKomut(oncekiKomutTokeni, parametreler));
                        
                    }
                    
                    oncekiKomutTokeni = t;
                    parametreler = new ArrayList<>();
                    
                    break;
                }
                case ETIKET: 
                    this.parseListesi.put(t, t.text);
                    break;
                case VIRGUL:
                {
                    parametreler.add(OperandFabrikasi.yeniOperand(parametreTokenlari));
                    parametreTokenlari = new ArrayList<>();
                    
                    break;
                }
                case IDENTIFIER: case IKI_NOKTA_UST_USTE: case ARTI: case EKSI: 
                case KOSELI_PARANTEZ_AC: case KOSELI_PARANTEZ_KAPA: case YAZMAC:
                case LITERAL:
                {
                    parametreTokenlari.add(t);
                    break;
                }
                case YORUM: case OLU: break;
                default: System.err.println("Geçersiz token!"); break;
            } //switch sonu
        } //döngü sonu
    }
    
    private void ucuncuGecis(List<Komut> komutlar, Map<String, Komut> etiketler)
    {
         //Parse listesinden bir kere daha geçelim.
        String etiket = null;
        Komut komut = null;
        for (Token t : this.parseListesi.keySet())
        {
            if (parseListesi.get(t) instanceof String)
            {
                etiket = (String) parseListesi.get(t);
            }
            
            if (parseListesi.get(t) instanceof Komut)
            {
                komut = (Komut) parseListesi.get(t);
                komutlar.add(komut);
                
                if (etiket != null)
                {
                    etiketler.put(etiket, komut);
                }
                
                etiket = null;
            }
        }
    }
    
    private Komut yeniKomut(Token token, List<Operand> parametreler) throws ParserException
    {
        try
        {
            //Token dan gelen text'i kullanarak komutun sınıfını bulalım.
            //bu text mov, MOV, Add olabilir. Aramayı Parser sınıfını başlatırken
            //yüklediğimiz instruction paketindeki sınıflar listesinde gerçekleştiriyoruz.
            String komutSinifi = null;
            for (ClassPath.ClassInfo ci : this.komutSiniflari)
            {
                if (ci.getSimpleName().equals(token.text.toUpperCase(Locale.ROOT)))
                {
                    komutSinifi = ci.getName();
                    break;
                }
            }
            
            if (komutSinifi == null)
                throw new ParserException("'" + token.text + "' komutu henüz kodlanmadı.", token.line, token.col);
            
            Class classKomut = ClassLoader.getSystemClassLoader().loadClass(komutSinifi);
  
            switch (parametreler.size())
            {
                case 0:
                    return (Komut) classKomut.newInstance();
                case 1:
                {
                    Operand o1 = parametreler.get(0);
                    
                    Constructor cs = classKomut.getConstructor(o1.getClass());
                    return (Komut) cs.newInstance(o1);
                }
                case 2:
                {
                    Operand o1 = parametreler.get(0);
                    Operand o2 = parametreler.get(1);
                    
                    Constructor cs = classKomut.getConstructor(o1.getClass(), o2.getClass());
                    return (Komut) cs.newInstance(o1, o2);
                }
                default:
                {
                    throw new ParserException("2 den fazla parametreli komut yok!");
                }
            }
        }
        catch (NoSuchMethodException e)
        {
            throw new ParserException(String.format("'%s' komutu belirlediğiniz operand kombinasyonu ile uyumlu değildir.", token.text), token.line, token.col);
        }
        catch (ParserException e)
        {
            throw new ParserException(String.format("Hata: %s. [Satır: %d, Sütun: %d]", e.getMessage(), token.line, token.col), token.line, token.col);
        }
        catch (InstantiationException e)
        {
            throw new ParserException(String.format("Komut ile operandı/operandları uyumlu değil: %d. satır, %d. sütun", token.line, token.col), token.line, token.col);
        }
        catch (Exception e)
        {
            throw new ParserException("Beklenmedik hata: " + e.getMessage());
        }
    }
}
