package org.example;

import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Iterator;

public class DataSheetTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private int columnCount = 3;
    private int rowCount = 0;
    private DataSheet dataSheet = null;
    String[] columnNames = {"Date", "X Value", "Y Value"};
    public DataSheetTableModel() {
        try {
            dataSheet = new DataSheet();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    public DataSheetTableModel(DataSheet dataSheet) {
        this();
        this.dataSheet = dataSheet;
    }
    public DataSheet getDataSheet() {
        return dataSheet;
    }
    @Override
    public int getColumnCount() {
        return columnCount;
    }
    @Override
    public int getRowCount() {
        return rowCount;
    }
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 0;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        if (dataSheet != null) {
            if (columnIndex == 0)
                return dataSheet.getDate(rowIndex);
            else if (columnIndex == 1)
                return dataSheet.getX(rowIndex);
            else if (columnIndex == 2)
                return dataSheet.getY(rowIndex);
        }
        return null;
    }
    public void setRowCount(int rowCount) {
        if (rowCount > 0)
            this.rowCount = rowCount;
    }
    private ArrayList<DataSheetChangeListener> listenerList = new
            ArrayList<DataSheetChangeListener>();
    private DataSheetChangeEvent event = new DataSheetChangeEvent(this);
    public void addDataSheetChangeListener(DataSheetChangeListener l) {
        listenerList.add(l);
    }
    public void removeDataSheetChangeListener(DataSheetChangeListener l) {
        listenerList.remove(l);
    }
    protected void fireDataSheetChange() {
        Iterator<DataSheetChangeListener> i = listenerList.iterator();
        while ( i.hasNext() )
            (i.next()).dataChanged(event);
    }
    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
        rowCount = this.dataSheet.numData();
        fireDataSheetChange();
    }
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        //super.setValueAt(value, rowIndex, columnIndex);
        try {
            double d;
            if (dataSheet != null) {
                if (columnIndex == 0) {
                    dataSheet.setDate((String) value, rowIndex);
                } else if (columnIndex == 1) {
                    d = Double.parseDouble((String) value);
                    dataSheet.setX(rowIndex, d);
                } else if (columnIndex == 2) {
                    d = Double.parseDouble((String) value);
                    dataSheet.setY(rowIndex, d);
                }
            }
            fireDataSheetChange();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}