/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Vector;
import java.util.function.Supplier;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Sergiu.Grigoras
 */
public class Playlist {

    static File workingDirectory = new File(System.getProperty("user.dir"));
    JTable table;
    DefaultTableModel tModel;
    JTextField filter;
    Duration startTime;

    public Playlist(JTable table, DefaultTableModel tModel, JTextField filter) {
        this.table = table;
        this.tModel = tModel;
        this.filter = filter;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        }
        Playlist pls = new Playlist(new JTable(), new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }, new JTextField("Filtru"));
        pls.createTable();
        pls.createAndShowGUI();
    }

    public void createTable() {
        tModel.addColumn("No.");
        tModel.addColumn("Start Time");
        tModel.addColumn("Duration");
        tModel.addColumn("Type");
        tModel.addColumn("Title");
        tModel.addColumn("File Location");

        table.setModel(tModel);
        table.getTableHeader().setReorderingAllowed(false);
        final Supplier<String> filterSupplier = () -> {
            return filter.getText();
        };
        table.setDefaultRenderer(Object.class, new PlaylistRawCellRenderer(filterSupplier));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        table.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

        TableColumn col;
        col = table.getColumnModel().getColumn(0); //Nr.
        col.setCellRenderer(new ColorColumnRenderer(Color.lightGray, Color.black));
        col.setResizable(false);
        col.setPreferredWidth(30);

        col = table.getColumnModel().getColumn(1);//Start Time
        col.setPreferredWidth(80);
        col.setResizable(false);

        col = table.getColumnModel().getColumn(2);//Duration
        col.setResizable(false);
        col.setPreferredWidth(60);

        col = table.getColumnModel().getColumn(3);//Type
        col.setResizable(false);
        col.setPreferredWidth(50);

        col = table.getColumnModel().getColumn(4);//Title
        col.setPreferredWidth(300);

        col = table.getColumnModel().getColumn(5);//File Location
        col.setPreferredWidth(490);
    }

    public void fillTable(Collection<RawMediaItem> lista) {
        int i = table.getRowCount();
        if (i == 0) {
            int h = LocalDateTime.now().getHour();
            int m = LocalDateTime.now().getMinute();
            int s = LocalDateTime.now().getSecond();
            startTime = new Duration((h * 3600) + (m * 60) + s);
        }

        for (RawMediaItem item : lista) {
            tModel.addRow(new Object[]{
                String.valueOf(++i),
                startTime,
                item.getDuration(),
                item.getItemType(),
                item.getTitle(),
                item.getFileLocation()
            });
            startTime = new Duration(startTime.getSecondsCount() + item.getDurationInSeconds());
        }
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Playlist");
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        JButton appendButton = new JButton("Append");
        JButton saveButton = new JButton("Save");
        appendButton.addActionListener(new OpenButtonListener());
        appendButton.addActionListener(new FilterListener());
        saveButton.addActionListener(new SaveButtonListener());
        panel.add(BorderLayout.WEST, appendButton);
        panel.add(BorderLayout.CENTER, filter);
        panel.add(BorderLayout.EAST, saveButton);
        filter.addActionListener(new FilterListener());
        filter.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
//                table.updateUI();
//                System.exit(0);
            }
        });
        frame.getContentPane().add(BorderLayout.NORTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        frame.setSize(1150, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public class OpenButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(workingDirectory);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Playlist *.ply", new String[]{"ply"}));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    fillTable(RawMediaItem.getList(selectedFile));
                } catch (Exception ex) {
                }
            }

        }
    }

    public class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Vector data = tModel.getDataVector();
            Duration dur;
            for (int i = 0; i < table.getRowCount(); i++) {
                Vector row = (Vector) data.elementAt(i);
                for (int j = 0; j < row.size(); j++) {
                    if (j == 2) {
                        dur = (Duration) row.elementAt(j);
                        System.out.print(dur.getSecondsCount() + "| ");
                    } else {
                        System.out.print(row.elementAt(j) +"["+row.elementAt(j).getClass().getSimpleName() + "]| ");
                    }
                }
                System.out.println("");
            }
        }

    }

    public class FilterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            table.updateUI();
        }

    }

    public static void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) table.getParent();
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
        Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
        viewport.scrollRectToVisible(rect);
    }
}
