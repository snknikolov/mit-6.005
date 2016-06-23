package jotto.jotto.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel {
    private static final String[] columnNames = { 
            "Guess", "Correct characters", "In correct position" 
            };
    
    private List<Object[]> data;
    
    public TableModel() {
        data = new ArrayList<Object[]>();
    }
    
    
    @Override
    public int getRowCount() {
        return data.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }    
    
    public TableModel getModel() {
        return this;
    }
    
    public void resetTableModel() {
        data = new ArrayList<Object[]>();
        fireTableDataChanged();
    }
    
    public void add(String guess, String fromServer) {
        String[] toAdd = new String[3];
        toAdd[0] = guess;
        if (JottoModel.isValidResponse(fromServer)) {
            toAdd[1] = String.valueOf(fromServer.charAt(0));
            toAdd[2] = String.valueOf(fromServer.charAt(1));
        } else {
            toAdd[1] = fromServer;
            toAdd[2] = "";
        }
        data.add(toAdd);
        fireTableDataChanged();
    }
}