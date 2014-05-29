package gis.gui.select;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author Ignas Daukšas
 */
public class SelectAllTableModelListener implements TableModelListener {
    private final JTable table;
    
    public SelectAllTableModelListener(JTable table) {
        this.table = table;
    }

    public void tableChanged(TableModelEvent e) {
        table.selectAll();
    }
    
}
