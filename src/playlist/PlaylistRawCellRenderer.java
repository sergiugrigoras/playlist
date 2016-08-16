/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Supplier;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Sergiu.Grigoras
 */
class PlaylistRawCellRenderer extends DefaultTableCellRenderer {

    private final Supplier<String> filterSupplier;

    public PlaylistRawCellRenderer(Supplier<String> filterProducer) {
        this.filterSupplier = filterProducer;
    }

//    private static final long serialVersionUID = 1L;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {

        Component c = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, col);
        setBorder(noFocusBorder);
        if (!table.isRowSelected(row)) {
            int modelRow = table.convertRowIndexToModel(row);
            String type = (String) table.getModel().getValueAt(modelRow, 3);
            String name = (String) table.getModel().getValueAt(modelRow, 4);
            Boolean filterMatchName = name.toLowerCase().contains(filterSupplier.get().toLowerCase());
            Boolean filterIsNotEmpty = !(filterSupplier.get().isEmpty());
            Boolean typeIsVideo = type.equalsIgnoreCase("VIDEO");

            if (type.contains("Event")) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.RED);
            } else if (type.contains("Note")) {
                c.setBackground(Color.CYAN);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }

            if (filterMatchName && filterIsNotEmpty && typeIsVideo) {
                c.setBackground(Color.GREEN);
            }

        } else {
            int modelRow = table.convertRowIndexToModel(row);
            String type = (String) table.getModel().getValueAt(modelRow, 3);
//            String name = (String) table.getModel().getValueAt(modelRow, 4);
//            Boolean filterMatchName = name.toLowerCase().contains(Playlist.getFilter().toLowerCase());
//            Boolean filterIsNotEmpty = !(Playlist.getFilter().equalsIgnoreCase(""));
//            Boolean typeIsVideo = type.equalsIgnoreCase("VIDEO");

            if (type.contains("Event")) {
                c.setBackground(Color.RED.darker());
            } else if (type.contains("Note")) {
                c.setBackground(Color.CYAN.darker());
            } else {
                c.setForeground(Color.BLACK);
//                c.setFont(c.getFont().deriveFont(Font.BOLD));
            }

        }
//        Object valueAt = table.getModel().getValueAt(row, col);
//        String s = "";
//        if (valueAt != null) {
//            s = valueAt.toString();
//            System.out.println(s);
//        }
//
//        if (s.equalsIgnoreCase("ID PUB Mz_TEST")) {
//            c.setForeground(Color.RED);
//        } else {
//            c.setForeground(Color.CYAN);
//        }

        return c;
    }
}
