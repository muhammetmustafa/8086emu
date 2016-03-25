
package edu.yildiz.emu8086.derleyici.asm.komut;

import edu.yildiz.emu8086.board.eu.AluIslemi;
import edu.yildiz.emu8086.board.eu.AtlamaIslemi;
import edu.yildiz.emu8086.board.eu.BayrakIslemi;
import edu.yildiz.emu8086.board.eu.Islem;
import edu.yildiz.emu8086.board.eu.OzelIslem;
import edu.yildiz.emu8086.board.eu.VeriTransferIslemi;
import edu.yildiz.emu8086.derleyici.asm.operand.AluOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.EtiketOperand;
import edu.yildiz.emu8086.derleyici.asm.operand.Operand;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.FlagRegister;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bu şablon sınıf alt paketlerde tanımlayacağımız MOV, ADD gibi komutların sınıflarının(nesnelerinin)
 * 'Komut' olarak işaretlenmesinde kullanılacaktır. Yani bir komut eklemek istediğimizde
 * komutu bu sınıftan kalıtılma alıyoruz.
 * 
 * @author MMC
 */
public abstract class Komut 
{
    private final List<Islem> islemler;

    public Komut()
    {
        this.islemler = new ArrayList<>();
    }
    
    public void veriTransferIslemiEkle(Operand sol, Operand sag)
    {
        this.islemler.add(new VeriTransferIslemi(sol, sag));
    }
    
    public void veriTransferIslemiEkleKaynakAlu(Operand sol)
    {
        this.veriTransferIslemiEkle(sol, new AluOperand());
    }
    
    public void aluIslemiEkle(AluIslemi.Tur aluIslemTuru, VeriUzunlugu sonucBoyutu, Operand sol, Operand sag)
    {
        this.islemler.add(new AluIslemi(aluIslemTuru, sonucBoyutu, sol, sag));
    }
    
    public void aluIslemiEkle(AluIslemi.Tur aluIslemTuru, VeriUzunlugu sonucBoyutu, Operand sol)
    {
        this.islemler.add(new AluIslemi(aluIslemTuru, sonucBoyutu, sol, null));
    }
    
    public void bayrakIslemiEkle(FlagRegister.Tur bayrak, boolean deger)
    {
        this.islemler.add(new BayrakIslemi(bayrak, deger));
    }
    
    public void bayrakIslemiEkle(FlagRegister.Tur bayrak)
    {
        this.islemler.add(new BayrakIslemi(bayrak));
    }
    
    public void ozelIslemEkle(String komutAdi)
    {
        this.islemler.add(new OzelIslem(komutAdi));
    }
    
    public void atlamaIslemiEkle(AtlamaIslemi islem)
    {
        this.islemler.add(islem);
    }
    
    public List<Islem> getIslemler()
    {
        return this.islemler;
    }
}
