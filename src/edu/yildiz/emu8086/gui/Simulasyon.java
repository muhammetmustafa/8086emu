package edu.yildiz.emu8086.gui;

import edu.yildiz.emu8086.board.BIU;
import edu.yildiz.emu8086.board.eu.EU;
import edu.yildiz.emu8086.derleyici.Parser;
import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.asm.Assembly;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.gui.logginghandlers.ListModelHandler;
import edu.yildiz.emu8086.gui.tablemodels.CodeSegmentTableModel;
import edu.yildiz.emu8086.gui.tablemodels.MemorySegmentTableModel;
import edu.yildiz.emu8086.tipler.ControlBus;
import edu.yildiz.emu8086.tipler.MemoryAddress;
import edu.yildiz.emu8086.tipler.Word;
import edu.yildiz.emu8086.tipler.Yazmac;
import edu.yildiz.emu8086.util.Donusum;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import org.jdesktop.application.Action;

/**
 *
 * @author MMC
 */
public class Simulasyon extends javax.swing.JFrame
{
    private TableModel tmCodeSegment;
    private TableModel tmDataSegment;
    private TableModel tmStackSegment;
    private TableModel tmExtraSegment;
    
    private Assembly kod;
    
    private boolean animasyonCalisiyormu = false;
    
    private List<Component> euKontrolGrubu;
    private List<Component> biuKontrolGrubu;
    
    private Thread tbiu;
    private Thread teu;
    

    public Simulasyon()
    {
        initComponents();    
        initEU();    
        initBIU();
        initLoggers();
    }
    
    private void initEU()
    {
        this.euKontrolGrubu = new ArrayList<>();
        this.euKontrolGrubu.add(this.jtxt_AH);
        this.euKontrolGrubu.add(this.jtxt_AL);
        this.euKontrolGrubu.add(this.jtxt_BH);
        this.euKontrolGrubu.add(this.jtxt_BL);
        this.euKontrolGrubu.add(this.jtxt_CH);
        this.euKontrolGrubu.add(this.jtxt_CL);
        this.euKontrolGrubu.add(this.jtxt_DH);
        this.euKontrolGrubu.add(this.jtxt_DL);
        this.euKontrolGrubu.add(this.jtxt_SP);
        this.euKontrolGrubu.add(this.jtxt_BP);
        this.euKontrolGrubu.add(this.jtxt_SI);
        this.euKontrolGrubu.add(this.jtxt_DI);
        this.euKontrolGrubu.add(this.jtxt_OF);
        this.euKontrolGrubu.add(this.jtxt_DF);
        this.euKontrolGrubu.add(this.jtxt_SF);
        this.euKontrolGrubu.add(this.jtxt_ZF);
        this.euKontrolGrubu.add(this.jtxt_AF);
        this.euKontrolGrubu.add(this.jtxt_PF);
        this.euKontrolGrubu.add(this.jtxt_CF);
       
        EU.get().olayDegisiklikDinleyiciEkle((PropertyChangeEvent evt) ->
        {
            switch (evt.getPropertyName())
            {
                case "komutsonu":
                    this.jbtnTekKomut.setEnabled(true);
                    break;
            }
        });
        
        EU.get().yazmacDegisiklikDinleyiciEkle((PropertyChangeEvent evt) ->
        {
            String low, high;
            
            if (evt.getNewValue() instanceof Word)
            {
                Word w = (Word) evt.getNewValue();
                high = w.getStringH();
                low  = w.getStringL();
            }
            else
            {
                high = "00";
                low = Donusum.decimalToHexadecimal((byte) evt.getNewValue());
            }
            
            SwingUtilities.invokeLater(() -> 
            {
                switch (evt.getPropertyName())
                {
                    case "AH":  
                        this.jtxt_AH.setText(low); 
                        break;
                    case "AL":  
                        this.jtxt_AL.setText(low); 
                        break;
                    case "AX":
                        this.jtxt_AH.setText(high); 
                        this.jtxt_AL.setText(low);
                        break;
                    case "BH":  
                        this.jtxt_BH.setText(low); 
                        break;
                    case "BL":  
                        this.jtxt_BL.setText(low); 
                        break;
                    case "BX":  
                        this.jtxt_BH.setText(high); 
                        this.jtxt_BL.setText(low);
                        break;
                    case "CH":  
                        this.jtxt_CH.setText(low); 
                        break;
                    case "CL":  
                        this.jtxt_CL.setText(low); 
                        break;
                    case "CX":  
                        this.jtxt_CH.setText(high); 
                        this.jtxt_CL.setText(low);
                        break;
                    case "DH":  
                        this.jtxt_DH.setText(low); 
                        break;
                    case "DL":  
                        this.jtxt_DL.setText(low); 
                        break;
                    case "DX":  
                        this.jtxt_DH.setText(high); 
                        this.jtxt_DL.setText(low);
                        break;
                    case "SP":
                        this.jtxt_SP.setText(high+low); 
                        break;
                    case "BP":  
                        this.jtxt_BP.setText(high+low); 
                        break;
                    case "SI":  
                        this.jtxt_SI.setText(high+low); 
                        break;
                    case "DI":  
                        this.jtxt_DI.setText(high+low); 
                        break;
                }
            });
        });
        EU.get().getAlu().getFlags().addPropertyChangeListener((PropertyChangeEvent evt) ->
        {
            String deger = ((boolean)evt.getNewValue()) ? "1" : "0";
            System.out.println(evt.getPropertyName() + "=" + deger);
            SwingUtilities.invokeLater(() ->
            {
                switch (evt.getPropertyName())
                {
                    case "OF":
                        jtxt_OF.setText(deger);
                        break;
                    case "DF":
                        jtxt_DF.setText(deger);
                        break;
                    case "SF":
                        jtxt_SF.setText(deger);
                        break;
                    case "ZF":
                        jtxt_ZF.setText(deger);
                        break;
                    case "AF":
                        jtxt_AF.setText(deger);
                        break;
                    case "PF":
                        jtxt_PF.setText(deger);
                        break;
                    case "CF":
                        jtxt_CF.setText(deger);
                }
            });
        });
    }
    
    private void initBIU()
    {
        this.biuKontrolGrubu = new ArrayList<>();
        this.biuKontrolGrubu.add(this.jtxt_CS);
        this.biuKontrolGrubu.add(this.jtxt_DS);
        this.biuKontrolGrubu.add(this.jtxt_SS);
        this.biuKontrolGrubu.add(this.jtxt_ES);
        this.biuKontrolGrubu.add(this.jtxt_IP);
        
        BIU.get().setLabelCycle(this.jlbl_BIU_Cycle);
        
        ControlBus.get().setLabel(this.jlbl_ControlBus);
        
        BIU.get().yazmacDegisiklikDinleyiciEkle((PropertyChangeEvent evt) ->
        {
            String low, high;
            
            if (evt.getNewValue() instanceof Word)
            {
                Word w = (Word) evt.getNewValue();
                high = w.getStringH();
                low  = w.getStringL();
            }
            else
            {
                high = "00";
                low = Donusum.decimalToHexadecimal((byte) evt.getNewValue());
            }
            
            SwingUtilities.invokeLater(() ->
            {
                switch (evt.getPropertyName())
                {
                    case "IP":
                        jtxt_IP.setText(high+low);
                        break;
                    case "CS":
                        jtxt_CS.setText(high+low);
                        break;
                    case "DS":
                        jtxt_DS.setText(high+low);
                        break;
                    case "SS":
                        jtxt_SS.setText(high+low);
                        break;
                    case "ES":
                        jtxt_ES.setText(high+low);
                        break;
                }
            });
        });
        BIU.get().olayDegisiklikDinleyiciEkle((PropertyChangeEvent evt) ->
        {
            SwingUtilities.invokeLater(() ->
            {
                switch (evt.getPropertyName())
                {
                    case "sonlandi":
                        //end of program
                        jbtnBaslat.setEnabled(true);
                        jbtnDurdur.setEnabled(false);
                        jbtnDevam.setEnabled(false);
                        animasyonCalisiyormu = false;
                        enable_disable(euKontrolGrubu, true);
                        enable_disable(biuKontrolGrubu, true);
                        break;
                    case "durakladi":
                        jbtnBaslat.setEnabled(false);
                        jbtnDurdur.setEnabled(false);
                        jbtnDevam.setEnabled(true);
                        animasyonCalisiyormu = false;
                        enable_disable(euKontrolGrubu, true);
                        enable_disable(biuKontrolGrubu, true);
                        break;
                }
            });
        });
        BIU.get().getInstructionQueue().addPropertyChangeListener((PropertyChangeEvent evt) ->
        {
            SwingUtilities.invokeLater(() ->
            {
                jlbl_Kuyruk_1.setText("1. " + BIU.get().getInstructionQueue().getKuyruk(0));
                jlbl_Kuyruk_2.setText("2. " + BIU.get().getInstructionQueue().getKuyruk(1));
                jlbl_Kuyruk_3.setText("3. " + BIU.get().getInstructionQueue().getKuyruk(2));
                jlbl_Kuyruk_4.setText("4. " + BIU.get().getInstructionQueue().getKuyruk(3));
                jlbl_Kuyruk_5.setText("5. " + BIU.get().getInstructionQueue().getKuyruk(4));
                jlbl_Kuyruk_6.setText("6. " + BIU.get().getInstructionQueue().getKuyruk(5));
            });
        });
    }
    
    private void initMemory()
    {
        this.tmDataSegment = new MemorySegmentTableModel(BIU.get().getDataSegment());
        this.tmDataSegment.addTableModelListener((TableModelEvent e) ->
        {
            Word segment = BIU.get().getDataSegment();
            Word offset = new Word(e.getFirstRow()*2 + e.getColumn() - 1);
            String deger = (String)tmDataSegment.getValueAt(e.getFirstRow(), e.getColumn());
            byte b = (byte)Donusum.hexadecimalToDecimal(deger, VeriUzunlugu.Byte_8);
            
            BIU.get().getMemoryInterface().getMemory().olaysizYaz(new MemoryAddress(segment, offset), b);
        });
       
        this.tmStackSegment = new MemorySegmentTableModel(BIU.get().getStackSegment());
        this.tmStackSegment.addTableModelListener((TableModelEvent e) ->
        {
            Word segment = BIU.get().getDataSegment();
            Word offset = new Word(e.getFirstRow()*2 + e.getColumn() - 1);
            String deger = (String)tmStackSegment.getValueAt(e.getFirstRow(), e.getColumn());
            byte b = (byte)Donusum.hexadecimalToDecimal(deger, VeriUzunlugu.Byte_8);
            
            BIU.get().getMemoryInterface().getMemory().olaysizYaz(new MemoryAddress(segment, offset), b);
        });
        
        this.tmExtraSegment = new MemorySegmentTableModel(BIU.get().getExtraSegment());
        this.tmExtraSegment.addTableModelListener((TableModelEvent e) ->
        {
            Word segment = BIU.get().getDataSegment();
            Word offset = new Word(e.getFirstRow()*2 + e.getColumn() - 1);
            String deger = (String)tmExtraSegment.getValueAt(e.getFirstRow(), e.getColumn());
            byte b = (byte)Donusum.hexadecimalToDecimal(deger, VeriUzunlugu.Byte_8);
            System.out.println(deger + " , " + segment + ":" + offset);
            BIU.get().getMemoryInterface().getMemory().olaysizYaz(new MemoryAddress(segment, offset), b);
        });
        
//        BIU.get().getMemoryInterface().getMemory().yazmacDegisiklikDinleyiciEkle((PropertyChangeEvent evt) ->
//        {
//            if (evt instanceof MemoryChangeEvent)
//            {
//                MemoryChangeEvent mevt = (MemoryChangeEvent) evt;
//                
//                if (mevt.getPropertyName().equals("reset"))
//                {
//                    
//                }
//                else
//                {
//                    TableModel tm;
//
//                    if (mevt.getAdres().getSegment() == BIU.get().getDataSegment())
//                        tm = this.tmDataSegment;
//                    else if (mevt.getAdres().getSegment() == BIU.get().getStackSegment())
//                        tm = this.tmStackSegment;
//                    else
//                        tm = this.tmExtraSegment;
//                   
//                }
//            }
//        });
    }
    
    private void initLoggers()
    {
        ListModelHandler h = new ListModelHandler();
        EU.kutukcu.addHandler(h);
        BIU.kutukcu.addHandler(h);
        this.jlist_Olaylar.setModel(h.getModel());
        h.getModel().addListDataListener(new ListDataListener()
        {
            @Override
            public void intervalAdded(ListDataEvent e)
            {   
                SwingUtilities.invokeLater(() ->
                {
                    jlist_Olaylar.ensureIndexIsVisible(jlist_Olaylar.getModel().getSize()-1);
                });
            }

            @Override
            public void intervalRemoved(ListDataEvent e)
            {}

            @Override
            public void contentsChanged(ListDataEvent e)
            {}
        });
    }
    
    private void enable_disable(List<Component> liste, boolean deger)
    {
        liste.stream().forEach((c) ->
        {
            c.setEnabled(deger);
        });
    }
    
    @Action
    public void action_animasyonBaslat()
    { 
        EU.get().setUyumaSuresi((11-this.jsld_EU.getValue())*1000);
        BIU.get().setUyumaSuresi((11-this.jsld_BIU.getValue())*1000);
        
        this.simulasyonuCalistir();
    }
    
    @Action
    public void action_animasyonDurdur()
    {
        this.animasyonCalisiyormu = false;
        BIU.get().setAnimasyonCalisiyorMu(false);
        EU.get().setAnimasyonCalisiyorMu(false);
    }
    
    @Action
    public void action_animasyonDevam()
    {
        this.simulasyonuCalistir();
    }
    
    private void simulasyonuCalistir()
    {
        if (!this.animasyonCalisiyormu)
        {
            this.animasyonCalisiyormu = true;
            BIU.get().setAnimasyonCalisiyorMu(true);
            EU.get().setAnimasyonCalisiyorMu(true);
            this.jbtnBaslat.setEnabled(false);
            this.jbtnDurdur.setEnabled(true);
            this.jbtnDevam.setEnabled(false);
            this.enable_disable(euKontrolGrubu, false);
            this.enable_disable(biuKontrolGrubu, false);

            tbiu = new Thread(BIU.get(), "BIU");
            teu = new Thread(EU.get(), "EU");
            tbiu.start();
            teu.start();
        }
    }
        
    @Action
    public void action_tekKomut()
    {
        EU.get().setCalismaModu(EU.CalismaModu.TekKomut);
        
        //Simülasyon başlamıysa çalıştır.
        this.simulasyonuCalistir();
        
        EU.get().setAdim(true);
        this.jbtnTekKomut.setEnabled(false);
    }
    
    @Action
    public void action_dosyaAc()
    {
        JFileChooser dosyaAc = new JFileChooser();
        
        int cevap = dosyaAc.showOpenDialog(this);
        if (cevap != JFileChooser.APPROVE_OPTION)
            return;
        
        String dosyaAdi = dosyaAc.getSelectedFile().getAbsolutePath();
        
        try
        {
            try (BufferedReader okuyucu = new BufferedReader(new FileReader(dosyaAdi)))
            {
                this.jtxt_KodDuzenleyici.setText("");
                
                while (okuyucu.ready())
                    this.jtxt_KodDuzenleyici.append(okuyucu.readLine() + "\n");
            }
        }
        catch (Exception e)
        {
        }
    }
    
    @Action
    public void action_derleYukle()
    {
        try
        {   
            this.kod = Parser.parse(this.jtxt_KodDuzenleyici.getText());
            this.tmCodeSegment = new CodeSegmentTableModel(this.kod);
            this.jtbl_CodeSegment.setModel(this.tmCodeSegment);
            this.jbtnBaslat.setEnabled(true);
            this.jbtnTekKomut.setEnabled(true);
            BIU.get().setKod(this.kod);
        } 
        catch (ParserException e)
        {
            try
            {
                //Hata olan satırın seçilmesi
                this.jtxt_KodDuzenleyici.select(this.jtxt_KodDuzenleyici.getLineStartOffset(e.getSatir()-1),
                                            this.jtxt_KodDuzenleyici.getLineEndOffset(e.getSatir()-1));
            } 
            catch (BadLocationException ex)
            {
                Logger.getLogger(Simulasyon.class.getName()).log(Level.SEVERE, null, ex);
            }    
            
            ParserExceptionDialog bilgilendirme = new ParserExceptionDialog(this, false, e.getMessage());
            bilgilendirme.setVisible(true);
        }
    }
    
    @Action
    public void action_resetle()
    {
        if (!animasyonCalisiyormu)
        {
            EU.get().reset();
            BIU.get().reset();
            this.jtbl_CodeSegment.setModel(new CodeSegmentTableModel());
            this.jbtnBaslat.setEnabled(false);
            initLoggers();
        }
    }
    
    private Point yazmacKonumuBul(Yazmac segment)
    {
        switch (segment)
        {
            case CS:    return konumHesapla(this.jtxt_CS);
            case DS:    return konumHesapla(this.jtxt_DS);
            case SS:    return konumHesapla(this.jtxt_SS);
            case ES:    return konumHesapla(this.jtxt_ES);
            default:    return null;
        }
    }
    
    private Point konumHesapla(Container c)
    {
        Point p = new Point(c.getLocation());
        
        Point parent;
        while (c.getParent() != null)
        {
            parent = c.getParent().getLocation();
            
            p.setLocation(p.x + parent.x, p.y + parent.y);
            
            c = c.getParent();
        }
        
        return p;
    }
    
    public TableModel getTmCodeSegment()
    {
        if (this.tmCodeSegment == null)
            initMemory();
        
        return this.tmCodeSegment;
    }

    public TableModel getTmDataSegment()
    {
        if (this.tmDataSegment == null)
            initMemory();
        
        return this.tmDataSegment;
    }

    public TableModel getTmExtraSegment()
    {
        if (this.tmExtraSegment == null)
            initMemory();
        
        return this.tmExtraSegment;
    }

    public TableModel getTmStackSegment()
    {
        if (this.tmStackSegment == null)
            initMemory();
        
        return this.tmStackSegment;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jpnl_EU = new javax.swing.JPanel();
        jlbl_EU = new javax.swing.JLabel();
        jpnl_Yazmaclar = new javax.swing.JPanel();
        jlbl_High = new javax.swing.JLabel();
        jlbl_Low = new javax.swing.JLabel();
        jpnl_GenelYazmaclar = new javax.swing.JPanel();
        jpnl_AX = new javax.swing.JPanel();
        jlbl_AX = new javax.swing.JLabel();
        jtxt_AH = new javax.swing.JTextField();
        jtxt_AL = new javax.swing.JTextField();
        jpnl_BX = new javax.swing.JPanel();
        jlbl_BX = new javax.swing.JLabel();
        jtxt_BH = new javax.swing.JTextField();
        jtxt_BL = new javax.swing.JTextField();
        jpnl_CX = new javax.swing.JPanel();
        jlbl_CX = new javax.swing.JLabel();
        jtxt_CH = new javax.swing.JTextField();
        jtxt_CL = new javax.swing.JTextField();
        jpnl_DX = new javax.swing.JPanel();
        jlbl_DX = new javax.swing.JLabel();
        jtxt_DH = new javax.swing.JTextField();
        jtxt_DL = new javax.swing.JTextField();
        jpnl_OffsetYazmaclari = new javax.swing.JPanel();
        jpnl_SP = new javax.swing.JPanel();
        jlbl_SP = new javax.swing.JLabel();
        jtxt_SP = new javax.swing.JTextField();
        jpnl_BP = new javax.swing.JPanel();
        jlbl_BP = new javax.swing.JLabel();
        jtxt_BP = new javax.swing.JTextField();
        jpnl_SI = new javax.swing.JPanel();
        jlbl_SI = new javax.swing.JLabel();
        jtxt_SI = new javax.swing.JTextField();
        jpnl_DI = new javax.swing.JPanel();
        jlbl_DI = new javax.swing.JLabel();
        jtxt_DI = new javax.swing.JTextField();
        jpnl_Flags = new javax.swing.JPanel();
        jpnl_OF = new javax.swing.JPanel();
        jlbl_OF = new javax.swing.JLabel();
        jtxt_OF = new javax.swing.JTextField();
        jpnl_DF = new javax.swing.JPanel();
        jlbl_DF = new javax.swing.JLabel();
        jtxt_DF = new javax.swing.JTextField();
        jpnl_SF = new javax.swing.JPanel();
        jlbl_SF = new javax.swing.JLabel();
        jtxt_SF = new javax.swing.JTextField();
        jpnl_ZF = new javax.swing.JPanel();
        jlbl_ZF = new javax.swing.JLabel();
        jtxt_ZF = new javax.swing.JTextField();
        jpnl_AF = new javax.swing.JPanel();
        jlbl_AF = new javax.swing.JLabel();
        jtxt_AF = new javax.swing.JTextField();
        jpnl_PF = new javax.swing.JPanel();
        jlbl_PF = new javax.swing.JLabel();
        jtxt_PF = new javax.swing.JTextField();
        jpnl_CF = new javax.swing.JPanel();
        jlbl_CF = new javax.swing.JLabel();
        jtxt_CF = new javax.swing.JTextField();
        jpnl_ALU = new javax.swing.JPanel();
        jlbl_ALU = new javax.swing.JLabel();
        jsp_KodDuzenleyici = new javax.swing.JScrollPane();
        jtxt_KodDuzenleyici = new javax.swing.JTextArea();
        jbtnDerleYukle = new javax.swing.JButton();
        jbtnDosyaAc = new javax.swing.JButton();
        jbtnBaslat = new javax.swing.JButton();
        jbtnTekKomut = new javax.swing.JButton();
        jbtnAdimAdim = new javax.swing.JButton();
        jbtnResetle = new javax.swing.JButton();
        jbtnDurdur = new javax.swing.JButton();
        jbtnDevam = new javax.swing.JButton();
        jpnl_BIU = new javax.swing.JPanel();
        jlbl_BIU_Baslik = new javax.swing.JLabel();
        jlbl_BIU_Cycle = new javax.swing.JLabel();
        jpnl_BIU_Yazmaclar = new javax.swing.JPanel();
        jpnl_CS = new javax.swing.JPanel();
        jlbl_CS = new javax.swing.JLabel();
        jtxt_CS = new javax.swing.JTextField();
        jpnl_DS = new javax.swing.JPanel();
        jlbl_DS = new javax.swing.JLabel();
        jtxt_DS = new javax.swing.JTextField();
        jpnl_SS = new javax.swing.JPanel();
        jlbl_SS = new javax.swing.JLabel();
        jtxt_SS = new javax.swing.JTextField();
        jpnl_ES = new javax.swing.JPanel();
        jlbl_ES = new javax.swing.JLabel();
        jtxt_ES = new javax.swing.JTextField();
        jpnl_IP = new javax.swing.JPanel();
        jlbl_IP = new javax.swing.JLabel();
        jtxt_IP = new javax.swing.JTextField();
        jpnl_Kuyruk = new javax.swing.JPanel();
        jlbl_Kuyruk_1 = new javax.swing.JLabel();
        jlbl_Kuyruk_2 = new javax.swing.JLabel();
        jlbl_Kuyruk_3 = new javax.swing.JLabel();
        jlbl_Kuyruk_4 = new javax.swing.JLabel();
        jlbl_Kuyruk_5 = new javax.swing.JLabel();
        jlbl_Kuyruk_6 = new javax.swing.JLabel();
        jpnl_Bus = new javax.swing.JPanel();
        jlbl_DataBus = new javax.swing.JLabel();
        jlbl_ControlBus = new javax.swing.JLabel();
        jlbl_AddressBus = new javax.swing.JLabel();
        jpnl_Memory = new javax.swing.JPanel();
        jpnl_Mem_CS = new javax.swing.JPanel();
        jlbl_Mem_CS = new javax.swing.JLabel();
        jsp_Mem_CS = new javax.swing.JScrollPane();
        jtbl_CodeSegment = new javax.swing.JTable();
        jpnl_Mem_DS = new javax.swing.JPanel();
        jlbl_Mem_DS = new javax.swing.JLabel();
        jsp_Mem_DS = new javax.swing.JScrollPane();
        jtbl_DataSegment = new javax.swing.JTable();
        jpnl_Mem_SS = new javax.swing.JPanel();
        jlbl_Mem_SS = new javax.swing.JLabel();
        jsp_Mem_SS = new javax.swing.JScrollPane();
        jtbl_StackSegment = new javax.swing.JTable();
        jpnl_Mem_ES = new javax.swing.JPanel();
        jlbl_Mem_ES = new javax.swing.JLabel();
        jsp_Mem_ES = new javax.swing.JScrollPane();
        jtbl_ExtraSegment = new javax.swing.JTable();
        jsp_Olaylar = new javax.swing.JScrollPane();
        jlist_Olaylar = new javax.swing.JList();
        jsld_EU = new javax.swing.JSlider();
        jsld_BIU = new javax.swing.JSlider();
        jlbl_EU_Yavas = new javax.swing.JLabel();
        jlbl_EU_Hizli = new javax.swing.JLabel();
        jlbl_BIU_Yavas = new javax.swing.JLabel();
        jlbl_BIU_Hizli = new javax.swing.JLabel();
        jlbl_anm_segment = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("8086 Emülator/Simülator");
        setBackground(new java.awt.Color(204, 204, 204));
        setMaximumSize(new java.awt.Dimension(1317, 490));
        setName("anaCerceve"); // NOI18N

        jlbl_EU.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jlbl_EU.setText("Execution Unit");

        jlbl_High.setBackground(new java.awt.Color(70, 130, 180));
        jlbl_High.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_High.setForeground(java.awt.Color.white);
        jlbl_High.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_High.setText("H");
        jlbl_High.setAlignmentX(1.0F);
        jlbl_High.setAlignmentY(1.0F);
        jlbl_High.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jlbl_High.setOpaque(true);

        jlbl_Low.setBackground(new java.awt.Color(70, 130, 180));
        jlbl_Low.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_Low.setForeground(java.awt.Color.white);
        jlbl_Low.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_Low.setText("L");
        jlbl_Low.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jlbl_Low.setOpaque(true);

        jpnl_GenelYazmaclar.setLayout(new java.awt.GridLayout(4, 1));

        jpnl_AX.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_AX.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_AX.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_AX.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_AX.setText("AX");
        jlbl_AX.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_AX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_AX.setOpaque(true);
        jpnl_AX.add(jlbl_AX);

        jtxt_AH.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_AH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_AH.setText("00");
        jtxt_AH.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_AX.add(jtxt_AH);

        jtxt_AL.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_AL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_AL.setText("00");
        jtxt_AL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_AX.add(jtxt_AL);

        jpnl_GenelYazmaclar.add(jpnl_AX);

        jpnl_BX.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_BX.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_BX.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_BX.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_BX.setText("BX");
        jlbl_BX.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_BX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_BX.setOpaque(true);
        jpnl_BX.add(jlbl_BX);

        jtxt_BH.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_BH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_BH.setText("00");
        jtxt_BH.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_BX.add(jtxt_BH);

        jtxt_BL.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_BL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_BL.setText("00");
        jtxt_BL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_BX.add(jtxt_BL);

        jpnl_GenelYazmaclar.add(jpnl_BX);

        jpnl_CX.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_CX.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_CX.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_CX.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_CX.setText("CX");
        jlbl_CX.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_CX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_CX.setOpaque(true);
        jpnl_CX.add(jlbl_CX);

        jtxt_CH.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_CH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_CH.setText("00");
        jtxt_CH.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_CX.add(jtxt_CH);

        jtxt_CL.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_CL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_CL.setText("00");
        jtxt_CL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_CX.add(jtxt_CL);

        jpnl_GenelYazmaclar.add(jpnl_CX);

        jpnl_DX.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_DX.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_DX.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_DX.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_DX.setText("DX");
        jlbl_DX.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_DX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_DX.setOpaque(true);
        jpnl_DX.add(jlbl_DX);

        jtxt_DH.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_DH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_DH.setText("00");
        jtxt_DH.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_DX.add(jtxt_DH);

        jtxt_DL.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_DL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_DL.setText("00");
        jtxt_DL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_DX.add(jtxt_DL);

        jpnl_GenelYazmaclar.add(jpnl_DX);

        jpnl_OffsetYazmaclari.setLayout(new java.awt.GridLayout(4, 1));

        jpnl_SP.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_SP.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_SP.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_SP.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_SP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_SP.setText("SP");
        jlbl_SP.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_SP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_SP.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_SP.setOpaque(true);
        jpnl_SP.add(jlbl_SP);

        jtxt_SP.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_SP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_SP.setText("0000");
        jtxt_SP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_SP.add(jtxt_SP);

        jpnl_OffsetYazmaclari.add(jpnl_SP);

        jpnl_BP.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_BP.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_BP.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_BP.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_BP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_BP.setText("BP");
        jlbl_BP.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_BP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_BP.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_BP.setOpaque(true);
        jpnl_BP.add(jlbl_BP);

        jtxt_BP.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_BP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_BP.setText("0000");
        jtxt_BP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_BP.add(jtxt_BP);

        jpnl_OffsetYazmaclari.add(jpnl_BP);

        jpnl_SI.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_SI.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_SI.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_SI.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_SI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_SI.setText("SI");
        jlbl_SI.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_SI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_SI.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_SI.setOpaque(true);
        jpnl_SI.add(jlbl_SI);

        jtxt_SI.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_SI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_SI.setText("0000");
        jtxt_SI.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_SI.add(jtxt_SI);

        jpnl_OffsetYazmaclari.add(jpnl_SI);

        jpnl_DI.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_DI.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_DI.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_DI.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_DI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_DI.setText("DI");
        jlbl_DI.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_DI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_DI.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_DI.setOpaque(true);
        jpnl_DI.add(jlbl_DI);

        jtxt_DI.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_DI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_DI.setText("0000");
        jtxt_DI.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_DI.add(jtxt_DI);

        jpnl_OffsetYazmaclari.add(jpnl_DI);

        javax.swing.GroupLayout jpnl_YazmaclarLayout = new javax.swing.GroupLayout(jpnl_Yazmaclar);
        jpnl_Yazmaclar.setLayout(jpnl_YazmaclarLayout);
        jpnl_YazmaclarLayout.setHorizontalGroup(
            jpnl_YazmaclarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_YazmaclarLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jlbl_High, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jlbl_Low, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnl_YazmaclarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jpnl_GenelYazmaclar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jpnl_OffsetYazmaclari, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnl_YazmaclarLayout.setVerticalGroup(
            jpnl_YazmaclarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_YazmaclarLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jpnl_YazmaclarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlbl_High, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlbl_Low, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jpnl_YazmaclarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpnl_GenelYazmaclar, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(jpnl_OffsetYazmaclari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jpnl_Flags.setLayout(new java.awt.GridLayout(7, 1));

        jpnl_OF.setLayout(new java.awt.GridLayout(1, 2));

        jlbl_OF.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_OF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_OF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_OF.setText("OF");
        jlbl_OF.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_OF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_OF.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_OF.setOpaque(true);
        jpnl_OF.add(jlbl_OF);

        jtxt_OF.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_OF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_OF.setText("0");
        jtxt_OF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jtxt_OF.setMaximumSize(new java.awt.Dimension(8, 21));
        jtxt_OF.setMinimumSize(new java.awt.Dimension(8, 21));
        jtxt_OF.setPreferredSize(new java.awt.Dimension(8, 21));
        jpnl_OF.add(jtxt_OF);

        jpnl_Flags.add(jpnl_OF);

        jpnl_DF.setLayout(new java.awt.GridLayout(1, 2));

        jlbl_DF.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_DF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_DF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_DF.setText("DF");
        jlbl_DF.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_DF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_DF.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_DF.setOpaque(true);
        jpnl_DF.add(jlbl_DF);

        jtxt_DF.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_DF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_DF.setText("0");
        jtxt_DF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_DF.add(jtxt_DF);

        jpnl_Flags.add(jpnl_DF);

        jpnl_SF.setLayout(new java.awt.GridLayout(1, 2));

        jlbl_SF.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_SF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_SF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_SF.setText("SF");
        jlbl_SF.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_SF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_SF.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_SF.setOpaque(true);
        jpnl_SF.add(jlbl_SF);

        jtxt_SF.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_SF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_SF.setText("0");
        jtxt_SF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_SF.add(jtxt_SF);

        jpnl_Flags.add(jpnl_SF);

        jpnl_ZF.setLayout(new java.awt.GridLayout(1, 2));

        jlbl_ZF.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_ZF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_ZF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_ZF.setText("ZF");
        jlbl_ZF.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_ZF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_ZF.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_ZF.setOpaque(true);
        jpnl_ZF.add(jlbl_ZF);

        jtxt_ZF.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_ZF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_ZF.setText("0");
        jtxt_ZF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_ZF.add(jtxt_ZF);

        jpnl_Flags.add(jpnl_ZF);

        jpnl_AF.setLayout(new java.awt.GridLayout(1, 2));

        jlbl_AF.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_AF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_AF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_AF.setText("AF");
        jlbl_AF.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_AF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_AF.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_AF.setOpaque(true);
        jpnl_AF.add(jlbl_AF);

        jtxt_AF.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_AF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_AF.setText("0");
        jtxt_AF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_AF.add(jtxt_AF);

        jpnl_Flags.add(jpnl_AF);

        jpnl_PF.setLayout(new java.awt.GridLayout(1, 2));

        jlbl_PF.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_PF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_PF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_PF.setText("PF");
        jlbl_PF.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_PF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_PF.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_PF.setOpaque(true);
        jpnl_PF.add(jlbl_PF);

        jtxt_PF.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_PF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_PF.setText("0");
        jtxt_PF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_PF.add(jtxt_PF);

        jpnl_Flags.add(jpnl_PF);

        jpnl_CF.setLayout(new java.awt.GridLayout(1, 2));

        jlbl_CF.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_CF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_CF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_CF.setText("CF");
        jlbl_CF.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_CF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_CF.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_CF.setOpaque(true);
        jpnl_CF.add(jlbl_CF);

        jtxt_CF.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_CF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_CF.setText("0");
        jtxt_CF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_CF.add(jtxt_CF);

        jpnl_Flags.add(jpnl_CF);

        jpnl_ALU.setBackground(new java.awt.Color(255, 255, 255));
        jpnl_ALU.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ALU", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14))); // NOI18N

        jlbl_ALU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/yildiz/emu8086/gui/resources/ALU.png"))); // NOI18N
        jlbl_ALU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jpnl_ALU.add(jlbl_ALU);

        jtxt_KodDuzenleyici.setColumns(20);
        jtxt_KodDuzenleyici.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jtxt_KodDuzenleyici.setRows(5);
        jtxt_KodDuzenleyici.setText("mov ah, 43\nadc [34], ah");
        jsp_KodDuzenleyici.setViewportView(jtxt_KodDuzenleyici);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(Simulasyon.class, this);
        jbtnDerleYukle.setAction(actionMap.get("action_derleYukle")); // NOI18N
        jbtnDerleYukle.setText("Derle&Yükle");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jtxt_KodDuzenleyici, org.jdesktop.beansbinding.ELProperty.create("${!text.isEmpty}"), jbtnDerleYukle, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jbtnDosyaAc.setAction(actionMap.get("action_dosyaAc")); // NOI18N
        jbtnDosyaAc.setText("Dosya Aç...");

        jbtnBaslat.setAction(actionMap.get("action_animasyonBaslat")); // NOI18N
        jbtnBaslat.setText("Başlat");
        jbtnBaslat.setEnabled(false);

        jbtnTekKomut.setAction(actionMap.get("action_tekKomut")); // NOI18N
        jbtnTekKomut.setText("Tek Komut");
        jbtnTekKomut.setEnabled(false);

        jbtnAdimAdim.setText("Adım Adım");
        jbtnAdimAdim.setEnabled(false);

        jbtnResetle.setAction(actionMap.get("action_resetle")); // NOI18N
        jbtnResetle.setText("Resetle");

        jbtnDurdur.setAction(actionMap.get("action_animasyonDurdur")); // NOI18N
        jbtnDurdur.setText("Durdur");
        jbtnDurdur.setEnabled(false);

        jbtnDevam.setAction(actionMap.get("action_animasyonDevam")); // NOI18N
        jbtnDevam.setText("Devam");
        jbtnDevam.setEnabled(false);

        javax.swing.GroupLayout jpnl_EULayout = new javax.swing.GroupLayout(jpnl_EU);
        jpnl_EU.setLayout(jpnl_EULayout);
        jpnl_EULayout.setHorizontalGroup(
            jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_EULayout.createSequentialGroup()
                .addGroup(jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsp_KodDuzenleyici)
                    .addGroup(jpnl_EULayout.createSequentialGroup()
                        .addGroup(jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnl_EULayout.createSequentialGroup()
                                .addComponent(jbtnBaslat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnDurdur)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnDevam))
                            .addGroup(jpnl_EULayout.createSequentialGroup()
                                .addComponent(jbtnDosyaAc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnDerleYukle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnResetle))
                            .addComponent(jpnl_ALU, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpnl_EULayout.createSequentialGroup()
                                .addComponent(jbtnTekKomut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnAdimAdim))
                            .addGroup(jpnl_EULayout.createSequentialGroup()
                                .addGroup(jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jpnl_Yazmaclar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlbl_EU))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpnl_Flags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpnl_EULayout.setVerticalGroup(
            jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_EULayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnl_EULayout.createSequentialGroup()
                        .addComponent(jlbl_EU)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jpnl_Yazmaclar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpnl_Flags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jpnl_ALU, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jsp_KodDuzenleyici, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnDerleYukle)
                    .addComponent(jbtnDosyaAc)
                    .addComponent(jbtnResetle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnBaslat)
                    .addComponent(jbtnDurdur)
                    .addComponent(jbtnDevam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnl_EULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnTekKomut)
                    .addComponent(jbtnAdimAdim))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(Simulasyon.class);
        jlbl_BIU_Baslik.setFont(resourceMap.getFont("UnitHeading")); // NOI18N
        jlbl_BIU_Baslik.setText("Bus Interface Unit");

        jlbl_BIU_Cycle.setFont(new java.awt.Font("Lucida Sans", 3, 14)); // NOI18N
        jlbl_BIU_Cycle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlbl_BIU_Cycle.setText("  ");
        jlbl_BIU_Cycle.setFocusable(false);
        jlbl_BIU_Cycle.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jlbl_BIU_Cycle.setOpaque(true);

        jpnl_BIU_Yazmaclar.setPreferredSize(new java.awt.Dimension(80, 105));
        jpnl_BIU_Yazmaclar.setLayout(new java.awt.GridLayout(5, 1));

        jpnl_CS.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_CS.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_CS.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_CS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_CS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_CS.setText("CS");
        jlbl_CS.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_CS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_CS.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_CS.setOpaque(true);
        jpnl_CS.add(jlbl_CS);

        jtxt_CS.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_CS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_CS.setText("0100");
        jtxt_CS.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_CS.add(jtxt_CS);

        jpnl_BIU_Yazmaclar.add(jpnl_CS);

        jpnl_DS.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_DS.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_DS.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_DS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_DS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_DS.setText("DS");
        jlbl_DS.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_DS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_DS.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_DS.setOpaque(true);
        jpnl_DS.add(jlbl_DS);

        jtxt_DS.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_DS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_DS.setText("0100");
        jtxt_DS.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_DS.add(jtxt_DS);

        jpnl_BIU_Yazmaclar.add(jpnl_DS);

        jpnl_SS.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_SS.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_SS.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_SS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_SS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_SS.setText("SS");
        jlbl_SS.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_SS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_SS.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_SS.setOpaque(true);
        jpnl_SS.add(jlbl_SS);

        jtxt_SS.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_SS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_SS.setText("0100");
        jtxt_SS.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_SS.add(jtxt_SS);

        jpnl_BIU_Yazmaclar.add(jpnl_SS);

        jpnl_ES.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_ES.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_ES.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_ES.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_ES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_ES.setText("ES");
        jlbl_ES.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_ES.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_ES.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_ES.setOpaque(true);
        jpnl_ES.add(jlbl_ES);

        jtxt_ES.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_ES.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_ES.setText("0100");
        jtxt_ES.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_ES.add(jtxt_ES);

        jpnl_BIU_Yazmaclar.add(jpnl_ES);

        jpnl_IP.setMaximumSize(new java.awt.Dimension(44, 21));
        jpnl_IP.setLayout(new java.awt.GridLayout(1, 0));

        jlbl_IP.setBackground(new java.awt.Color(176, 196, 222));
        jlbl_IP.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlbl_IP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_IP.setText("IP");
        jlbl_IP.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null));
        jlbl_IP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlbl_IP.setMaximumSize(new java.awt.Dimension(10, 21));
        jlbl_IP.setOpaque(true);
        jpnl_IP.add(jlbl_IP);

        jtxt_IP.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtxt_IP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtxt_IP.setText("0000");
        jtxt_IP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnl_IP.add(jtxt_IP);

        jpnl_BIU_Yazmaclar.add(jpnl_IP);

        jpnl_Kuyruk.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Instruction Queue", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N
        jpnl_Kuyruk.setLayout(new java.awt.GridLayout(6, 1));

        jlbl_Kuyruk_1.setFont(new java.awt.Font("Consolas", 3, 12)); // NOI18N
        jlbl_Kuyruk_1.setText("1. BOŞ");
        jpnl_Kuyruk.add(jlbl_Kuyruk_1);

        jlbl_Kuyruk_2.setFont(new java.awt.Font("Consolas", 3, 12)); // NOI18N
        jlbl_Kuyruk_2.setText("2. BOŞ");
        jpnl_Kuyruk.add(jlbl_Kuyruk_2);

        jlbl_Kuyruk_3.setFont(new java.awt.Font("Consolas", 3, 12)); // NOI18N
        jlbl_Kuyruk_3.setText("3. BOŞ");
        jpnl_Kuyruk.add(jlbl_Kuyruk_3);

        jlbl_Kuyruk_4.setFont(new java.awt.Font("Consolas", 3, 12)); // NOI18N
        jlbl_Kuyruk_4.setText("4. BOŞ");
        jpnl_Kuyruk.add(jlbl_Kuyruk_4);

        jlbl_Kuyruk_5.setFont(new java.awt.Font("Consolas", 3, 12)); // NOI18N
        jlbl_Kuyruk_5.setText("5. BOŞ");
        jpnl_Kuyruk.add(jlbl_Kuyruk_5);

        jlbl_Kuyruk_6.setFont(new java.awt.Font("Consolas", 3, 12)); // NOI18N
        jlbl_Kuyruk_6.setText("6. BOŞ");
        jpnl_Kuyruk.add(jlbl_Kuyruk_6);

        javax.swing.GroupLayout jpnl_BIULayout = new javax.swing.GroupLayout(jpnl_BIU);
        jpnl_BIU.setLayout(jpnl_BIULayout);
        jpnl_BIULayout.setHorizontalGroup(
            jpnl_BIULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_BIULayout.createSequentialGroup()
                .addGroup(jpnl_BIULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnl_Kuyruk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnl_BIULayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jlbl_BIU_Cycle, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnl_BIULayout.createSequentialGroup()
                        .addGroup(jpnl_BIULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbl_BIU_Baslik)
                            .addComponent(jpnl_BIU_Yazmaclar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpnl_BIULayout.setVerticalGroup(
            jpnl_BIULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_BIULayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlbl_BIU_Baslik)
                .addGap(3, 3, 3)
                .addComponent(jlbl_BIU_Cycle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnl_BIU_Yazmaclar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnl_Kuyruk, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jlbl_BIU_Cycle.getAccessibleContext().setAccessibleName("");

        jlbl_DataBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/yildiz/emu8086/gui/resources/Data_Bus.png"))); // NOI18N

        jlbl_ControlBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/yildiz/emu8086/gui/resources/Control_Bus.png"))); // NOI18N

        jlbl_AddressBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/yildiz/emu8086/gui/resources/Address_Bus.png"))); // NOI18N

        javax.swing.GroupLayout jpnl_BusLayout = new javax.swing.GroupLayout(jpnl_Bus);
        jpnl_Bus.setLayout(jpnl_BusLayout);
        jpnl_BusLayout.setHorizontalGroup(
            jpnl_BusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlbl_ControlBus, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
            .addComponent(jlbl_AddressBus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jlbl_DataBus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpnl_BusLayout.setVerticalGroup(
            jpnl_BusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_BusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlbl_AddressBus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jlbl_ControlBus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jlbl_DataBus, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );

        jpnl_Mem_CS.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));

        jlbl_Mem_CS.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbl_Mem_CS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_Mem_CS.setText("CS");
        jlbl_Mem_CS.setMaximumSize(new java.awt.Dimension(175, 100));
        jlbl_Mem_CS.setMinimumSize(new java.awt.Dimension(175, 100));
        jpnl_Mem_CS.add(jlbl_Mem_CS);

        jsp_Mem_CS.setBorder(null);
        jsp_Mem_CS.setMaximumSize(new java.awt.Dimension(500, 500));
        jsp_Mem_CS.setMinimumSize(new java.awt.Dimension(240, 100));
        jsp_Mem_CS.setPreferredSize(new java.awt.Dimension(150, 300));

        jtbl_CodeSegment.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jtbl_CodeSegment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jtbl_CodeSegment.setFocusable(false);
        jtbl_CodeSegment.setIntercellSpacing(new java.awt.Dimension(2, 2));
        jtbl_CodeSegment.setMaximumSize(new java.awt.Dimension(100, 72));
        jtbl_CodeSegment.setMinimumSize(new java.awt.Dimension(100, 72));
        jtbl_CodeSegment.setSize(100, 72);
        jtbl_CodeSegment.setRowHeight(18);
        jtbl_CodeSegment.setRowSelectionAllowed(false);
        jtbl_CodeSegment.setShowHorizontalLines(false);
        jtbl_CodeSegment.setShowVerticalLines(false);
        jtbl_CodeSegment.getTableHeader().setReorderingAllowed(false);
        jsp_Mem_CS.setViewportView(jtbl_CodeSegment);

        jpnl_Mem_CS.add(jsp_Mem_CS);

        jpnl_Mem_DS.setAutoscrolls(true);
        jpnl_Mem_DS.setMaximumSize(new java.awt.Dimension(290, 136));
        jpnl_Mem_DS.setPreferredSize(new java.awt.Dimension(290, 136));

        jlbl_Mem_DS.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbl_Mem_DS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_Mem_DS.setText("DS");
        jlbl_Mem_DS.setMaximumSize(new java.awt.Dimension(175, 100));
        jlbl_Mem_DS.setMinimumSize(new java.awt.Dimension(175, 100));
        jpnl_Mem_DS.add(jlbl_Mem_DS);

        jsp_Mem_DS.setBorder(null);
        jsp_Mem_DS.setMaximumSize(new java.awt.Dimension(500, 500));
        jsp_Mem_DS.setMinimumSize(new java.awt.Dimension(240, 100));
        jsp_Mem_DS.setPreferredSize(new java.awt.Dimension(100, 300));

        jtbl_DataSegment.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jtbl_DataSegment.setModel(this.getTmDataSegment());
        jtbl_DataSegment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtbl_DataSegment.setFocusable(false);
        jtbl_DataSegment.setIntercellSpacing(new java.awt.Dimension(2, 2));
        jtbl_DataSegment.setRowHeight(18);
        jtbl_DataSegment.setShowHorizontalLines(false);
        jtbl_DataSegment.setShowVerticalLines(false);
        jtbl_DataSegment.getTableHeader().setResizingAllowed(false);
        jtbl_DataSegment.getTableHeader().setReorderingAllowed(false);
        jsp_Mem_DS.setViewportView(jtbl_DataSegment);
        if (jtbl_DataSegment.getColumnModel().getColumnCount() > 0)
        {
            jtbl_DataSegment.getColumnModel().getColumn(0).setResizable(false);
            jtbl_DataSegment.getColumnModel().getColumn(0).setPreferredWidth(50);
            jtbl_DataSegment.getColumnModel().getColumn(1).setResizable(false);
            jtbl_DataSegment.getColumnModel().getColumn(1).setPreferredWidth(25);
            jtbl_DataSegment.getColumnModel().getColumn(2).setResizable(false);
            jtbl_DataSegment.getColumnModel().getColumn(2).setPreferredWidth(25);
        }

        jpnl_Mem_DS.add(jsp_Mem_DS);

        jpnl_Mem_SS.setAutoscrolls(true);
        jpnl_Mem_SS.setMaximumSize(new java.awt.Dimension(290, 136));
        jpnl_Mem_SS.setPreferredSize(new java.awt.Dimension(290, 136));

        jlbl_Mem_SS.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbl_Mem_SS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_Mem_SS.setText("SS");
        jlbl_Mem_SS.setMaximumSize(new java.awt.Dimension(175, 100));
        jlbl_Mem_SS.setMinimumSize(new java.awt.Dimension(175, 100));
        jpnl_Mem_SS.add(jlbl_Mem_SS);

        jsp_Mem_SS.setBorder(null);
        jsp_Mem_SS.setMaximumSize(new java.awt.Dimension(500, 500));
        jsp_Mem_SS.setMinimumSize(new java.awt.Dimension(240, 100));
        jsp_Mem_SS.setPreferredSize(new java.awt.Dimension(100, 300));

        jtbl_StackSegment.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jtbl_StackSegment.setModel(this.getTmStackSegment());
        jtbl_StackSegment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtbl_StackSegment.setFocusable(false);
        jtbl_StackSegment.setIntercellSpacing(new java.awt.Dimension(2, 2));
        jtbl_StackSegment.setRowHeight(18);
        jtbl_StackSegment.setShowHorizontalLines(false);
        jtbl_StackSegment.setShowVerticalLines(false);
        jtbl_StackSegment.getTableHeader().setResizingAllowed(false);
        jtbl_StackSegment.getTableHeader().setReorderingAllowed(false);
        jsp_Mem_SS.setViewportView(jtbl_StackSegment);
        if (jtbl_StackSegment.getColumnModel().getColumnCount() > 0)
        {
            jtbl_StackSegment.getColumnModel().getColumn(0).setResizable(false);
            jtbl_StackSegment.getColumnModel().getColumn(0).setPreferredWidth(50);
            jtbl_StackSegment.getColumnModel().getColumn(1).setResizable(false);
            jtbl_StackSegment.getColumnModel().getColumn(1).setPreferredWidth(25);
            jtbl_StackSegment.getColumnModel().getColumn(2).setResizable(false);
            jtbl_StackSegment.getColumnModel().getColumn(2).setPreferredWidth(25);
        }

        jpnl_Mem_SS.add(jsp_Mem_SS);

        jpnl_Mem_ES.setAutoscrolls(true);
        jpnl_Mem_ES.setMaximumSize(new java.awt.Dimension(290, 136));
        jpnl_Mem_ES.setPreferredSize(new java.awt.Dimension(290, 136));

        jlbl_Mem_ES.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jlbl_Mem_ES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbl_Mem_ES.setText("ES");
        jlbl_Mem_ES.setMaximumSize(new java.awt.Dimension(175, 100));
        jlbl_Mem_ES.setMinimumSize(new java.awt.Dimension(175, 100));
        jpnl_Mem_ES.add(jlbl_Mem_ES);

        jsp_Mem_ES.setBorder(null);
        jsp_Mem_ES.setMaximumSize(new java.awt.Dimension(500, 500));
        jsp_Mem_ES.setMinimumSize(new java.awt.Dimension(240, 100));
        jsp_Mem_ES.setPreferredSize(new java.awt.Dimension(100, 300));

        jtbl_ExtraSegment.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jtbl_ExtraSegment.setModel(this.getTmExtraSegment());
        jtbl_ExtraSegment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtbl_ExtraSegment.setFocusable(false);
        jtbl_ExtraSegment.setIntercellSpacing(new java.awt.Dimension(2, 2));
        jtbl_ExtraSegment.setRowHeight(18);
        jtbl_ExtraSegment.setShowHorizontalLines(false);
        jtbl_ExtraSegment.setShowVerticalLines(false);
        jtbl_ExtraSegment.getTableHeader().setResizingAllowed(false);
        jtbl_ExtraSegment.getTableHeader().setReorderingAllowed(false);
        jsp_Mem_ES.setViewportView(jtbl_ExtraSegment);
        if (jtbl_ExtraSegment.getColumnModel().getColumnCount() > 0)
        {
            jtbl_ExtraSegment.getColumnModel().getColumn(0).setResizable(false);
            jtbl_ExtraSegment.getColumnModel().getColumn(0).setPreferredWidth(50);
            jtbl_ExtraSegment.getColumnModel().getColumn(1).setResizable(false);
            jtbl_ExtraSegment.getColumnModel().getColumn(1).setPreferredWidth(25);
            jtbl_ExtraSegment.getColumnModel().getColumn(2).setResizable(false);
            jtbl_ExtraSegment.getColumnModel().getColumn(2).setPreferredWidth(25);
        }

        jpnl_Mem_ES.add(jsp_Mem_ES);

        javax.swing.GroupLayout jpnl_MemoryLayout = new javax.swing.GroupLayout(jpnl_Memory);
        jpnl_Memory.setLayout(jpnl_MemoryLayout);
        jpnl_MemoryLayout.setHorizontalGroup(
            jpnl_MemoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnl_MemoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnl_Mem_CS, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jpnl_Mem_DS, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnl_Mem_SS, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnl_Mem_ES, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnl_MemoryLayout.setVerticalGroup(
            jpnl_MemoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnl_MemoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnl_MemoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpnl_Mem_ES, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpnl_MemoryLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jpnl_MemoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jpnl_Mem_CS, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                            .addComponent(jpnl_Mem_DS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpnl_Mem_SS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );

        jsp_Olaylar.setAutoscrolls(true);

        jlist_Olaylar.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jsp_Olaylar.setViewportView(jlist_Olaylar);

        jsld_EU.setMaximum(10);
        jsld_EU.setMinimum(1);

        jsld_BIU.setMaximum(10);
        jsld_BIU.setMinimum(1);
        jsld_BIU.setToolTipText("");

        jlbl_EU_Yavas.setText("Yavaş");

        jlbl_EU_Hizli.setText("Hızlı");

        jlbl_BIU_Yavas.setText("Yavaş");

        jlbl_BIU_Hizli.setText("Hızlı");

        jlbl_anm_segment.setBackground(new java.awt.Color(255, 255, 0));
        jlbl_anm_segment.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jlbl_anm_segment.setForeground(new java.awt.Color(255, 51, 51));
        jlbl_anm_segment.setText("jLabel5");
        jlbl_anm_segment.setOpaque(true);
        jlbl_anm_segment.setVisible(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnl_EU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jlbl_EU_Yavas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsld_EU, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbl_EU_Hizli)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jpnl_BIU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpnl_Bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jsp_Olaylar))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpnl_Memory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jlbl_anm_segment, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jlbl_BIU_Yavas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsld_BIU, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbl_BIU_Hizli)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsld_EU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbl_EU_Yavas, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlbl_EU_Hizli, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnl_EU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsld_BIU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbl_BIU_Yavas, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlbl_BIU_Hizli, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnl_Bus, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpnl_BIU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jsp_Olaylar)
                .addGap(11, 11, 11))
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jpnl_Memory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jlbl_anm_segment)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Windows".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(Simulasyon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Simulasyon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Simulasyon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Simulasyon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() ->
        {
            new Simulasyon().setVisible(true);
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtnAdimAdim;
    private javax.swing.JButton jbtnBaslat;
    private javax.swing.JButton jbtnDerleYukle;
    private javax.swing.JButton jbtnDevam;
    private javax.swing.JButton jbtnDosyaAc;
    private javax.swing.JButton jbtnDurdur;
    private javax.swing.JButton jbtnResetle;
    private javax.swing.JButton jbtnTekKomut;
    private javax.swing.JLabel jlbl_AF;
    private javax.swing.JLabel jlbl_ALU;
    private javax.swing.JLabel jlbl_AX;
    private javax.swing.JLabel jlbl_AddressBus;
    private javax.swing.JLabel jlbl_BIU_Baslik;
    private javax.swing.JLabel jlbl_BIU_Cycle;
    private javax.swing.JLabel jlbl_BIU_Hizli;
    private javax.swing.JLabel jlbl_BIU_Yavas;
    private javax.swing.JLabel jlbl_BP;
    private javax.swing.JLabel jlbl_BX;
    private javax.swing.JLabel jlbl_CF;
    private javax.swing.JLabel jlbl_CS;
    private javax.swing.JLabel jlbl_CX;
    private javax.swing.JLabel jlbl_ControlBus;
    private javax.swing.JLabel jlbl_DF;
    private javax.swing.JLabel jlbl_DI;
    private javax.swing.JLabel jlbl_DS;
    private javax.swing.JLabel jlbl_DX;
    private javax.swing.JLabel jlbl_DataBus;
    private javax.swing.JLabel jlbl_ES;
    private javax.swing.JLabel jlbl_EU;
    private javax.swing.JLabel jlbl_EU_Hizli;
    private javax.swing.JLabel jlbl_EU_Yavas;
    private javax.swing.JLabel jlbl_High;
    private javax.swing.JLabel jlbl_IP;
    private javax.swing.JLabel jlbl_Kuyruk_1;
    private javax.swing.JLabel jlbl_Kuyruk_2;
    private javax.swing.JLabel jlbl_Kuyruk_3;
    private javax.swing.JLabel jlbl_Kuyruk_4;
    private javax.swing.JLabel jlbl_Kuyruk_5;
    private javax.swing.JLabel jlbl_Kuyruk_6;
    private javax.swing.JLabel jlbl_Low;
    private javax.swing.JLabel jlbl_Mem_CS;
    private javax.swing.JLabel jlbl_Mem_DS;
    private javax.swing.JLabel jlbl_Mem_ES;
    private javax.swing.JLabel jlbl_Mem_SS;
    private javax.swing.JLabel jlbl_OF;
    private javax.swing.JLabel jlbl_PF;
    private javax.swing.JLabel jlbl_SF;
    private javax.swing.JLabel jlbl_SI;
    private javax.swing.JLabel jlbl_SP;
    private javax.swing.JLabel jlbl_SS;
    private javax.swing.JLabel jlbl_ZF;
    private javax.swing.JLabel jlbl_anm_segment;
    private javax.swing.JList jlist_Olaylar;
    private javax.swing.JPanel jpnl_AF;
    private javax.swing.JPanel jpnl_ALU;
    private javax.swing.JPanel jpnl_AX;
    private javax.swing.JPanel jpnl_BIU;
    private javax.swing.JPanel jpnl_BIU_Yazmaclar;
    private javax.swing.JPanel jpnl_BP;
    private javax.swing.JPanel jpnl_BX;
    private javax.swing.JPanel jpnl_Bus;
    private javax.swing.JPanel jpnl_CF;
    private javax.swing.JPanel jpnl_CS;
    private javax.swing.JPanel jpnl_CX;
    private javax.swing.JPanel jpnl_DF;
    private javax.swing.JPanel jpnl_DI;
    private javax.swing.JPanel jpnl_DS;
    private javax.swing.JPanel jpnl_DX;
    private javax.swing.JPanel jpnl_ES;
    private javax.swing.JPanel jpnl_EU;
    private javax.swing.JPanel jpnl_Flags;
    private javax.swing.JPanel jpnl_GenelYazmaclar;
    private javax.swing.JPanel jpnl_IP;
    private javax.swing.JPanel jpnl_Kuyruk;
    private javax.swing.JPanel jpnl_Mem_CS;
    private javax.swing.JPanel jpnl_Mem_DS;
    private javax.swing.JPanel jpnl_Mem_ES;
    private javax.swing.JPanel jpnl_Mem_SS;
    private javax.swing.JPanel jpnl_Memory;
    private javax.swing.JPanel jpnl_OF;
    private javax.swing.JPanel jpnl_OffsetYazmaclari;
    private javax.swing.JPanel jpnl_PF;
    private javax.swing.JPanel jpnl_SF;
    private javax.swing.JPanel jpnl_SI;
    private javax.swing.JPanel jpnl_SP;
    private javax.swing.JPanel jpnl_SS;
    private javax.swing.JPanel jpnl_Yazmaclar;
    private javax.swing.JPanel jpnl_ZF;
    private javax.swing.JSlider jsld_BIU;
    private javax.swing.JSlider jsld_EU;
    private javax.swing.JScrollPane jsp_KodDuzenleyici;
    private javax.swing.JScrollPane jsp_Mem_CS;
    private javax.swing.JScrollPane jsp_Mem_DS;
    private javax.swing.JScrollPane jsp_Mem_ES;
    private javax.swing.JScrollPane jsp_Mem_SS;
    private javax.swing.JScrollPane jsp_Olaylar;
    private javax.swing.JTable jtbl_CodeSegment;
    private javax.swing.JTable jtbl_DataSegment;
    private javax.swing.JTable jtbl_ExtraSegment;
    private javax.swing.JTable jtbl_StackSegment;
    private javax.swing.JTextField jtxt_AF;
    private javax.swing.JTextField jtxt_AH;
    private javax.swing.JTextField jtxt_AL;
    private javax.swing.JTextField jtxt_BH;
    private javax.swing.JTextField jtxt_BL;
    private javax.swing.JTextField jtxt_BP;
    private javax.swing.JTextField jtxt_CF;
    private javax.swing.JTextField jtxt_CH;
    private javax.swing.JTextField jtxt_CL;
    private javax.swing.JTextField jtxt_CS;
    private javax.swing.JTextField jtxt_DF;
    private javax.swing.JTextField jtxt_DH;
    private javax.swing.JTextField jtxt_DI;
    private javax.swing.JTextField jtxt_DL;
    private javax.swing.JTextField jtxt_DS;
    private javax.swing.JTextField jtxt_ES;
    private javax.swing.JTextField jtxt_IP;
    private javax.swing.JTextArea jtxt_KodDuzenleyici;
    private javax.swing.JTextField jtxt_OF;
    private javax.swing.JTextField jtxt_PF;
    private javax.swing.JTextField jtxt_SF;
    private javax.swing.JTextField jtxt_SI;
    private javax.swing.JTextField jtxt_SP;
    private javax.swing.JTextField jtxt_SS;
    private javax.swing.JTextField jtxt_ZF;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
