package me.marcelohdez.looptube;

import me.marcelohdez.looptube.library.SongData;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class LoopTableModel implements TableModel {
    public static final String COL_NUM = "#";
    public static final String COL_NAME = "Name";
    private static final String[] COLUMNS = {COL_NUM, COL_NAME};

    private final ArrayList<SongData> loopsList = new ArrayList<>();

    public void add(SongData item) {
        loopsList.add(item);
    }

    public void remove(int index) {
        loopsList.remove(index);
    }

    public void clear() {
        loopsList.clear();
    }

    public SongData get(int index) {
        return loopsList.get(index);
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
        return columnIndex == 0 ?
                rowIndex + 1 : // row index
                loopsList.get(rowIndex); // song data
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue instanceof String s) loopsList.get(rowIndex).setName(s);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
