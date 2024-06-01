package org.example;

import javax.swing.*;
import javax.swing.table.TableModel;

public class DataSheetTable extends JTable{

    public DataSheetTable(TableModel tableModel) {
        super(tableModel);
    }

    public void setTableModel(TableModel dataModel) {
        super.setModel(dataModel);
    }

    public DataSheetTableModel getTableModel() {
        return (DataSheetTableModel)super.getModel();
    }

}
