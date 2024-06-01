package org.example;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
public class MainFrame extends JFrame {
    private static final int FRAME_WIDTH = 1300;
    private static final int FRAME_HEIGHT = 800;
    private DataSheetTableFrame dataSheetTableFrame;
    private DataSheetGraph dataSheetGraph;
    private JPanel buttonPanel;
    private JButton open;
    private JButton save;
    private JButton clear;
    private JButton exit;
    private JFileChooser fileChooser;
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        mainFrame.setVisible(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        mainFrame.setBounds(dimension.width/2-650,dimension.height/2-400,FRAME_WIDTH,FRAME_HEIGHT);
    }
    public MainFrame() {
        initUI();
        initListeners();
    }
    private void initUI() {
        setTitle("GraphConstructor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        dataSheetTableFrame = new DataSheetTableFrame();
        add(dataSheetTableFrame, BorderLayout.WEST);

        dataSheetGraph = new DataSheetGraph();
        add(dataSheetGraph, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 4));
        open = new JButton("Open");
        buttonPanel.add(open);
        save = new JButton("Save");
        buttonPanel.add(save);
        clear = new JButton("Clear");
        buttonPanel.add(clear);
        exit = new JButton("Exit");
        buttonPanel.add(exit);
        add(buttonPanel, BorderLayout.SOUTH);

        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".xml", "xml"));
        fileChooser.setCurrentDirectory(new java.io.File("."));
    }
    private void initListeners() {
        open.addActionListener(e -> openFile());
        save.addActionListener(e -> saveFile());
        clear.addActionListener(e -> clearData());
        exit.addActionListener(e -> dispose());

        dataSheetTableFrame.getDataSheetTable().getTableModel()
                .addDataSheetChangeListener(e -> updateGraph());
    }
    private void openFile() {
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
            File file = fileChooser.getSelectedFile();
            DataSheet dataSheet;
            try {
                dataSheet = new DataSheet(DataSheet.SAXRead(file));
            } catch (SAXException | IOException ex) {
                throw new RuntimeException(ex);
            }
            dataSheetTableFrame.getDataSheetTable().getTableModel().setDataSheet(dataSheet);
            dataSheetTableFrame.getDataSheetTable().revalidate();
            dataSheetGraph.setDataSheet(dataSheet);
        }
    }
    private void saveFile() {
        fileChooser.setDialogTitle("Look in");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showDialog(null, "Look in") == JFileChooser.APPROVE_OPTION) {
            try {
                DataSheet dataSheet = dataSheetTableFrame.getDataSheetTable().getTableModel().getDataSheet();
                dataSheet.saveDocument(new File(fileChooser.getSelectedFile(), "out.xml"));
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }
    private void clearData() {
        DataSheet dataSheet = null;
        try {
            dataSheet = new DataSheet();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        dataSheet.addElement(dataSheet.newElement());
        dataSheetTableFrame.getDataSheetTable().getTableModel().setDataSheet(dataSheet);
        dataSheetTableFrame.getDataSheetTable().revalidate();
        dataSheetGraph.setDataSheet(dataSheet);
    }
    private void updateGraph() {
        dataSheetGraph.setDataSheet(dataSheetTableFrame.getDataSheetTable().getTableModel().getDataSheet());
        dataSheetGraph.repaint();
    }
}

