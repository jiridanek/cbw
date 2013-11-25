/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.deterministictools;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author miha
 */
public class AllDecimalTableCellRenderer extends DefaultTableCellRenderer {

    private static final DecimalFormat formatter = new DecimalFormat("#.#");

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // First format the cell value as required
        value = formatter.format((Number) value);
        // And pass it on to parent class
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
