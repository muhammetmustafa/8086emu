
package edu.yildiz.emu8086.gui.tablemodels;

import edu.yildiz.emu8086.board.BIU;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.MemoryAddress;
import edu.yildiz.emu8086.tipler.Word;
import edu.yildiz.emu8086.util.Donusum;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MMC
 */
public class MemorySegmentTableModel extends AbstractTableModel
{
    private final Word segment;
    
    public MemorySegmentTableModel(Word segment)
    {
        this.segment = segment;
    }
    
    @Override
    public int getRowCount()
    {
        return 32768;
    }

    @Override
    public int getColumnCount()
    {
        return 3;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
            return new Word(rowIndex*2).toString();
        else
        {
            MemoryAddress adres = new MemoryAddress(segment, new Word(rowIndex*2 + 2 - columnIndex));
            byte b = BIU.get().getMemoryInterface().getMemory().oku(adres);
            
            return Donusum.decimalToHexadecimal(b);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (columnIndex != 0) 
        {
            byte b = (byte)Donusum.hexadecimalToDecimal(String.valueOf(aValue), VeriUzunlugu.Byte_8);
            MemoryAddress adres = new MemoryAddress(segment, new Word(rowIndex*2 + 2 - columnIndex));
            BIU.get().getMemoryInterface().getMemory().yaz(adres, b);
        }
    }
    
    @Override
    public String getColumnName(int column)
    {
        if (column == 0)
            return "Adres";
        else if (column == 1)
            return "H";
        else if (column == 2)
            return "L";
        else
            return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
            return false;
        else
            return true;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }
}
