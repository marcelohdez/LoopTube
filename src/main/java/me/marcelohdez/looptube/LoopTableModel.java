package me.marcelohdez.looptube;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class LoopTableModel implements TableModel {
    private static final String COL_NUM = "#";
    private static final String COL_NAME = "Name";
    private static final String[] COLUMNS = {COL_NUM, COL_NAME};

    private final ArrayList<String> loopsList = new ArrayList<>();

    public void add(String item) {
        loopsList.add(item);
    }

    @Override
    public int getRowCount() {
        return loopsList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMNS[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMNS[columnIndex].equals(COL_NUM) ? Integer.class : String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1; // name column
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return columnIndex == 0 ? rowIndex + 1 : loopsList.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue instanceof String s) loopsList.set(rowIndex, s);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
