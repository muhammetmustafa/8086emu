package edu.yildiz.emu8086.tipler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author MMC
 */
public enum Yazmac
{
    AL(1), AH(1), AX(2),
    BL(1), BH(1), BX(2),
    CL(1), CH(1), CX(2),
    DL(1), DH(1), DX(2),
    BP(2), SP(2),
    IP(2),
    DI(2), SI(2),
    CS(2), DS(2), ES(2), SS(2),
    
    //Bu yazmaçlara erişim yasaktır.
    //Memory Address Register
    MAR(2),
    //Memory Buffer (Data) Register
    MBR(2),
    //Arithmetic-Logic dahili işlemler için kullanılacak
    ALU(2),
    //Flag yazmacını okumak için
    FLAGH(1), FLAGL(1), FLAGX(2)
    ;
    
    private final VeriUzunlugu boyut;
    
    private Yazmac(int kacByte)
    {
        this.boyut = VeriUzunlugu.bytedan(kacByte);
    }
    
    private Yazmac(VeriUzunlugu boyut)
    {
        this.boyut = boyut;
    }
    
    /**
     * Parametrede belirtilen yazmacın CS, DS, ES, SS yazmaçlarından
     * biri olup olmadığını kontrol eder. Bulursa true, aksi takdirde false dönderir.
     * 
     * @param yazmac
     * @return 
     */
    public static boolean segmentYazmaciMi(String yazmac)
    {
       Yazmac _yazmac = Yazmac.valueOf(yazmac.toUpperCase(Locale.ROOT));
       
       return _yazmac == CS || _yazmac == DS || _yazmac == ES || _yazmac == SS; 
    }
    
    /**
     *  Komutların memory operandlarının offset kısmını oluştururken 
     * yazdıkları yazmaçların kullanılabilirliğini kontrol eder. 
     * 
     * Kullanılabilecek yazmaçlar: BX, BP, SI, DI
     * 
     * @param yazmac
     * @return 
     */
    public static boolean offsetYazmaciMi(String yazmac)
    {
        Yazmac _yazmac = Yazmac.valueOf(yazmac.toUpperCase(Locale.ROOT));
       
        return _yazmac == BX || _yazmac == BP || _yazmac == SI || _yazmac == DI; 
    }
    
    /**
     *  Byte yazmaçların kontrolü
     * 
     * Kullanılabilecek yazmaçlar: AH, AL; BH, BL; CH, CL; DH, DL
     * 
     * @param yazmac
     * @return 
     */
    public static boolean baytYazmaciMi(Yazmac yazmac)
    {
        return yazmac == AH || yazmac == AL || yazmac == BH || yazmac == BL ||
                yazmac == CH || yazmac == CL || yazmac == DH || yazmac == DL; 
    }
    
    public VeriUzunlugu getBoyut()
    {
        return boyut;
    }
    
    public static List<Yazmac> parserGuvenliYazmaclar()
    {
        List<Yazmac> parserGuvenliYazmaclar = new ArrayList<>();
        List<Yazmac> yasakliYazmaclar = yasakliYazmaclar();
        
        for (Yazmac yazmac : Yazmac.values())
            if (!yasakliYazmaclar.contains(yazmac))
                parserGuvenliYazmaclar.add(yazmac);
        
        return parserGuvenliYazmaclar;
    }
    
    public static List<Yazmac> yasakliYazmaclar()
    {
        ArrayList<Yazmac> yasakliYazmaclar = new ArrayList<>();
        
        yasakliYazmaclar.add(MAR);
        yasakliYazmaclar.add(MBR);
        yasakliYazmaclar.add(ALU);
        yasakliYazmaclar.add(IP);
        yasakliYazmaclar.add(FLAGH);
        yasakliYazmaclar.add(FLAGL);
        yasakliYazmaclar.add(FLAGX);
        
        return yasakliYazmaclar;
    }
    
}
