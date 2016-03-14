
package edu.yildiz.emu8086.derleyici.asm;

import edu.yildiz.emu8086.derleyici.asm.komut.Komut;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MMC
 */
public class Assembly 
{
    //Instruction Pointer
    private int ip = 0;
    private final List<Komut> komutlar;
    private final Map<String, Komut> etiketler;
    private boolean kuyrukBosalt = false;
    
    public Assembly()
    {
        this.komutlar = new ArrayList<>();
        this.etiketler = new HashMap<>();
    }
    
    public Komut sonrakiKomut()
    {
        Komut komut = this.komutlar.get(this.ip);
        this.ip++;
        kuyrukBosalt = false;
        return komut;
    }
    
    public void zipla(String etiket)
    {
        if (this.etiketler.containsKey(etiket))
        {
            this.ip = this.komutlar.indexOf(this.etiketler.get(etiket));
            kuyrukBosalt = true;
        }
    }
    
    public boolean alinacakKomutKaldiMi()
    {
        return this.ip < this.komutlar.size();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        for (Komut komut : this.komutlar)
        {
            if (this.etiketler.containsValue(komut))
            {
                sb.append(this.etiketBul(komut));
                sb.append(":\n");
            }
            
            sb.append(komut);
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    private String etiketBul(Komut komut)
    {
        for (String s : this.etiketler.keySet())
        {
            if (this.etiketler.get(s) == komut)
            {
                return s;
            }
        }
        
        return null;
    }
    
    public Map<String, Komut> getEtiketler()
    {
        return etiketler;
    }

    public List<Komut> getKomutlar()
    {
        return komutlar;
    }

    public boolean getKuyrukBosalt()
    {
        return kuyrukBosalt;
    }

    public int getIp()
    {
        return ip;
    }
}
