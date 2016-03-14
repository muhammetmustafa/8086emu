
package edu.yildiz.emu8086.gui.tablemodels;

import edu.yildiz.emu8086.derleyici.asm.Assembly;
import edu.yildiz.emu8086.tipler.Word;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MMC
 */
public class CodeSegmentTableModel extends AbstractTableModel
{
    private Assembly kod;

    public CodeSegmentTableModel()
    {
    }
    
    public CodeSegmentTableModel(Assembly kod)
    {
        this.kod = kod;
    }
    
    @Override
    public int getRowCount()
    {
        if (this.kod == null)
            return 0;
        
        return this.kod.getKomutlar().size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
    
    @Override
    public int getColumnCount()
    {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (this.kod == null)
            return null;
        
        if (columnIndex == 0)
            return new Word(rowIndex);
        else
            return this.kod.getKomutlar().get(rowIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }

    @Override
    public String getColumnName(int column)
    {
        if (column == 0)
            return "Adres";
        else
            return "Komut";
    }

    public void setKod(Assembly kod)
    {
        this.kod = kod;
    }
}
