package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataSheetTableFrame extends JPanel {
    private final JButton addButton;
    private final JButton delButton;
    private final JPanel buttonPanel;
    private final JScrollPane scrollPane;
    private final DataSheetTable dataSheetTable;

    public DataSheetTableFrame() {
        setLayout(new BorderLayout());

        addButton = new JButton("Add (+)");
        delButton = new JButton("Del (-)");

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 4));
        buttonPanel.add(addButton);
        buttonPanel.add(delButton);
        add(buttonPanel, BorderLayout.SOUTH);

        dataSheetTable = new DataSheetTable(new DataSheetTableModel());
        scrollPane = new JScrollPane(dataSheetTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRow();
            }
        });
    }

    private void addRow() {
        int rowCount = dataSheetTable.getTableModel().getRowCount();
        dataSheetTable.getTableModel().setRowCount(rowCount + 1);
        dataSheetTable.getTableModel().getDataSheet().addElement(dataSheetTable.getTableModel().getDataSheet().newElement());
        dataSheetTable.revalidate();
        dataSheetTable.getTableModel().fireDataSheetChange();
    }

    private void deleteRow() {
        DataSheetTableModel tableModel = dataSheetTable.getTableModel();
        DataSheet dataSheet = tableModel.getDataSheet();

        int rowCount = tableModel.getRowCount();

        if (rowCount > 1) {
            tableModel.setRowCount(rowCount - 1);
            dataSheet.removeElement(dataSheet.numData() - 1);
            dataSheetTable.revalidate();
            dataSheetTable.getTableModel().fireDataSheetChange();
        } else {
            dataSheet.setDate("", 0);
            String dataItem = dataSheet.getDataItem(0);
            double value = dataItem.isEmpty() ? 0 : Double.parseDouble(dataItem);
            dataSheet.setX(0, value);
            dataSheet.setY(0, value);
            dataSheetTable.revalidate();
            dataSheetTable.repaint();
            dataSheetTable.getTableModel().fireDataSheetChange();
        }
    }

    public DataSheetTable getDataSheetTable() {
        return dataSheetTable;
    }
}
