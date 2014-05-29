package gis.gui;

import gis.Utils;
import gis.gui.select.SelectAllTableModelListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.map.Layer;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.table.FeatureCollectionTableModel;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * @author Ignas Daukšas
 */
public class FeaturesFrame extends JFrame {
    private final MainFrame mainFrame;
    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    
    private final JTable table;
    private final JTextField text;
    
    public FeaturesFrame(MainFrame mainFrame) {
        setTitle("Features table");
        getContentPane().setLayout(new BorderLayout());
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        getContentPane().add(toolbar, BorderLayout.PAGE_START);
        
        text = new JTextField();
        text.setText("include"); // include selects everything!
        toolbar.add(text);
        
        JButton button = new JButton(new SafeAction("Execute query") {
            public void action(ActionEvent e) throws Throwable {
                filterFeatures();
            }
        });
        toolbar.add(button);
        
        toolbar.add(new JToolBar.Separator());
        
        button = new JButton(new SafeAction("Selections from map") {
            public void action(ActionEvent e) throws Throwable {
                fromMap();
            }
        });
        toolbar.add(button);
        
        button = new JButton(new SafeAction("Select in map") {
            public void action(ActionEvent e) throws Throwable {
                toMap();
            }
        });
        toolbar.add(button);
        
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        setSize(800, 400);
        
        this.mainFrame = mainFrame;
    }

    @Override
    public void setVisible(boolean b) {
        setLocationRelativeTo(mainFrame);
        super.setVisible(b);
    }
    
    private void fillTable(SimpleFeatureCollection features) {
        if (table.getModel() instanceof FeatureCollectionTableModel) {
            ((FeatureCollectionTableModel) table.getModel()).dispose();
        }
        FeatureCollectionTableModel model = new FeatureCollectionTableModel(features);
        table.setModel(model);
    }
    
    private void filterFeatures() throws IOException, CQLException {
        Layer layer = mainFrame.getFirstSelectedLayer();
        if (layer == null) {
            Utils.showMsg("No layer selected", "Please select layer from which to get features.");
            return;
        }
        
        SimpleFeatureSource source = (SimpleFeatureSource) layer.getFeatureSource();
        Filter filter = ECQL.toFilter(text.getText());
        
        fillTable(source.getFeatures(filter));
    }
    
    private void fromMap() throws IOException {
        Layer layer = mainFrame.getFirstSelectedLayer();
        if (layer == null) {
            Utils.showMsg("No layer selected", "Please select layer from which to get features.");
            return;
        }
        
        SimpleFeatureSource source = (SimpleFeatureSource) layer.getFeatureSource();
        Filter filter = ff.id(mainFrame.getSelectedFeatures());
        
        fillTable(source.getFeatures(filter));
        
        table.getModel().addTableModelListener(new SelectAllTableModelListener(table));
    }
    
    private void toMap() {
        if (table.getSelectedRowCount() == 0) {
            table.selectAll();
        }
        
        mainFrame.resetFeatureSelection();
        int rows = table.getRowCount();
        for (int i : table.getSelectedRows()) {
            if (i < rows) { // workaround select's lack of sync
                mainFrame.selectFeature(ff.featureId((String) table.getValueAt(i, 0)));
            }
        }
        mainFrame.highlightSelected();
    }
}
