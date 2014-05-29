package gis;

import gis.gui.MainFrame;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * @author Ignas Daukšas
 */
public class Utils {
    
    public static File fileChooser() {
        JFileDataStoreChooser dialog = new JFileDataStoreChooser("shp");
        dialog.setFileFilter(dialog.getChoosableFileFilters()[1]);
        dialog.setCurrentDirectory(new File("D:\\Dropbox\\_PS\\6sem\\gis"));
        if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return dialog.getSelectedFile();
        }
        return null;
    }
    
    public static FeatureLayer createLayer(File file) throws IOException {
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        if (store instanceof ShapefileDataStore) {
            ((ShapefileDataStore) store).setCharset(Charset.forName("Windows-1257"));
        }
        SimpleFeatureSource featureSource = store.getFeatureSource();
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        return new FeatureLayer(featureSource, style);
    }
    
    public static void showMsg(String title, String msg) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void fillLayersCombo(MainFrame mainFrame, JComboBox combo, String selected) {
        DefaultComboBoxModel<String> cbm = new DefaultComboBoxModel<String>();
        for (Layer layer : mainFrame.getMapContent().layers()) {
            String layerName = layer.getFeatureSource().getName().getLocalPart();
            cbm.addElement(layerName);
            if (selected != null && layerName.contains(selected)) {
                cbm.setSelectedItem(layerName);
            }
        }
        combo.setModel(cbm);
    }
    
    public static Layer getLayer(MainFrame mainFrame, JComboBox combo) {
        String layerName = (String) combo.getSelectedItem();
        for (Layer layer : mainFrame.getMapContent().layers()) {
            if (layer.getFeatureSource().getName().getLocalPart().equals(layerName)) {
                return layer;
            }
        }
        return null;
    }
    
    public static void createSilentLayer(MainFrame mainFrame, SimpleFeatureCollection collection) {
        createSilentLayer(mainFrame, collection, null);
    }
    
    public static void createSilentLayer(MainFrame mainFrame, SimpleFeatureCollection collection,
            String title) {
        
        FeatureLayer layer = new FeatureLayer(collection,
                SLD.createSimpleStyle(collection.getSchema()));
        layer.setVisible(false);
        if (title != null) {
            layer.setTitle(title);
        }
        mainFrame.getMapPane().setIgnoreRepaint(true);
        mainFrame.getMapContent().addLayer(layer);
        mainFrame.getMapPane().setIgnoreRepaint(false);
    }
    
}
