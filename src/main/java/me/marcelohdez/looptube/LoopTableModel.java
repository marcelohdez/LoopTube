package me.marcelohdez.looptube;

import me.marcelohdez.looptube.library.SongData;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Comparator;

public class LoopTableModel implements TableModel {
    public static final String COL_NUM = "#";
    public static final String COL_NAME = "Name";
    private static final String[] COLUMNS = {COL_NUM, COL_NAME};

    private final ArrayList<TableModelListener> listeners = new ArrayList<>();
    private final ArrayList<SongData> loopsList = new ArrayList<>();

    public void add(SongData item) {
        if (loopsList.add(item))
            propagateTableModelEvent(new TableModelEvent(this, loopsList.size() - 1));
    }

    public void clear() {
        loopsList.clear();
        propagateTableModelEvent(new TableModelEvent(this));
    }

    public SongData get(int index) {
        return loopsList.get(index);
    }

    public void sortAlphabetically() {
        loopsList.sort(Comparator.comparing(SongData::toString));
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
        if (aValue instanceof String s && loopsList.get(rowIndex).setName(s))
            propagateTableModelEvent(new TableModelEvent(this, rowIndex));
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    private void propagateTableModelEvent(TableModelEvent tme) {
        for (var l : listeners) l.tableChanged(tme);
    }
}
