/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import javax.swing.JTable;
/**
 *
 * @author Sergiu.Grigoras
 */
public class ColorColumnRenderer extends DefaultTableCellRenderer {

    Color bkgndColor, fgndColor;

    public ColorColumnRenderer(Color bkgnd, Color foregnd) {
        super();
        bkgndColor = bkgnd;
        fgndColor = foregnd;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        cell.setBackground(bkgndColor);
        cell.setForeground(fgndColor);
        setBorder(noFocusBorder);
        if (table.isRowSelected(row)){ 
        cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        cell.setBackground(bkgndColor.darker());
        }
        return cell;
    }
}
