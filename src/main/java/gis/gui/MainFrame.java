package gis.gui;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import gis.Utils;
import gis.gui.select.SelectInMapAction;
import gis.gui.select.SelectRectangleTerritoryAction;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.action.SafeAction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

/**
 * @author Ignas Daukšas
 */
public class MainFrame extends JMapFrame {
    private final FeaturesFrame featuresFrame;
    private final Task2Frame task2Frame;
    private final Task3Frame task3Frame;
    private Set<FeatureId> selectedFeatures;
    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private final StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private List<SimpleFeature> selectedTerritoryFeatures;
    private SimpleFeatureBuilder selectedTerritorySFB;
    private FeatureLayer selectedTerritoryLayer;
    
    public MainFrame(Layer firstLayer) {
        super();
        
        setTitle("GIS");
        
        MapContent map = new MapContent();
        map.addLayer(firstLayer);
        setMapContent(map);
        
        enableLayerTable(true);
        enableStatusBar(true);
        enableToolBar(true);
        
        JToolBar toolbar = getToolBar();
        
        JButton button = new JButton(new SafeAction(null) {
            public void action(ActionEvent e) throws Throwable {
                openShapefile();
            }
        });
        button.setIcon(new ImageIcon(getClass().getResource("/layer-add-icon.png")));
        button.setToolTipText("Add layer from shapefile");
        toolbar.add(button, 0);
        
        toolbar.add(new JToolBar.Separator(), 1);
        toolbar.add(new JToolBar.Separator(), 3);
        toolbar.add(new JToolBar.Separator(), 8);
        
        button = new JButton(new SelectInMapAction(this));
        toolbar.add(button, 9);
        
        button = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                featuresFrame.setVisible(true);
            }
        });
        button.setIcon(new ImageIcon(getClass().getResource("/table-icon.png")));
        button.setToolTipText("Open features table");
        toolbar.add(button, 11);
        
        button = new JButton(new SafeAction(null) {
            public void action(ActionEvent e) throws Throwable {
                zoomToSelection();
            }
        });
        button.setIcon(new ImageIcon(getClass().getResource("/zoom-selection-icon.png")));
        button.setToolTipText("Zoom to selection");
        toolbar.add(button);
        
        toolbar.addSeparator();
        
        button = new JButton(new SafeAction("GIS 2 užduotis") {
            public void action(ActionEvent e) throws Throwable {
                task2Frame.setVisible(true);
            }
        });
        toolbar.add(button);
        
        toolbar.addSeparator();
        
        button = new JButton(new SelectRectangleTerritoryAction(this));
        toolbar.add(button);
        button = new JButton(new SafeAction("GIS 3 užduotis") {
            public void action(ActionEvent e) throws Throwable {
                task3Frame.setVisible(true);
            }
        });
        toolbar.add(button);
        
        setSize(1000, 600);
        
        featuresFrame = new FeaturesFrame(this);
        selectedFeatures = new HashSet<FeatureId>();
        task2Frame = new Task2Frame(this);
        task3Frame = new Task3Frame(this);
    }
    
    private void openShapefile() throws IOException {
        File file = Utils.fileChooser();
        if (file != null) {
            getMapContent().addLayer(Utils.createLayer(file));
        }
    }
    
    private void zoomToSelection() throws IOException {
        if (selectedFeatures.size() > 0) {
            Layer layer = getFirstSelectedLayer();
            if (layer != null) {
                Filter filter = ff.id(selectedFeatures);
                ReferencedEnvelope area = layer.getFeatureSource().getFeatures(filter).getBounds();
                getMapPane().setDisplayArea(area);
            }
        }
    }
    
    public Layer getFirstSelectedLayer() {
        for (Layer layer : getMapContent().layers()) {
            if (layer.isSelected()) {
                return layer;
            }
        }
        return null;
    }
    
    public void resetFeatureSelection() {
        selectedFeatures = new HashSet<FeatureId>();
    }
    
    public void selectFeature(FeatureId id) {
        selectedFeatures.add(id);
    }
    
    public Set<FeatureId> getSelectedFeatures() {
        return selectedFeatures;
    }
    
    public void highlightSelected() {
        FeatureLayer layer = (FeatureLayer) getFirstSelectedLayer();
        Style style = layer.getStyle();
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        
        if (fts.rules().size() == 2) {
            fts.rules().get(0).setElseFilter(false);
            fts.rules().remove(1);
        }
        
        if (!selectedFeatures.isEmpty()) {
            Rule rule = sf.createRule();
            rule.symbolizers().add(createHighlighSymbolizer(fts.rules().get(0).symbolizers().get(0)));
            rule.setFilter(ff.id(selectedFeatures));
            fts.rules().add(rule);
            fts.rules().get(0).setElseFilter(true);
        }
        
        layer.setStyle(style);
    }
    
    private Symbolizer createHighlighSymbolizer(Symbolizer referenceSymb) {
        final Color HIGHLIGHT_COLOR = Color.MAGENTA;
        final int HIGHLIGHT_WIDTH = 2;
        final int HIGHLIGHT_SIZE = 8;
        
        Symbolizer symb = null;
        
        if (referenceSymb instanceof LineSymbolizer) {
            LineSymbolizer lineSymb = sf.createLineSymbolizer();
            lineSymb.setStroke(sf.createStroke(ff.literal(HIGHLIGHT_COLOR), ff.literal(HIGHLIGHT_WIDTH)));
            symb = lineSymb;
        }
        else if (referenceSymb instanceof PolygonSymbolizer) {
            PolygonSymbolizer polySymb = sf.createPolygonSymbolizer();
            polySymb.setStroke(sf.createStroke(ff.literal(HIGHLIGHT_COLOR), ff.literal(HIGHLIGHT_WIDTH)));
            polySymb.setFill(sf.createFill(ff.literal(HIGHLIGHT_COLOR)));
            symb = polySymb;
        }
        else if (referenceSymb instanceof PointSymbolizer) {
            PointSymbolizer pointSymb = sf.createPointSymbolizer();
            
            Mark mark = sf.getCircleMark();
            mark.setFill(sf.createFill(ff.literal(HIGHLIGHT_COLOR)));
            mark.setStroke(sf.createStroke(ff.literal(HIGHLIGHT_COLOR), ff.literal(HIGHLIGHT_WIDTH)));
            
            Graphic graphic = sf.createDefaultGraphic();
            graphic.graphicalSymbols().clear();
            graphic.graphicalSymbols().add(mark);
            graphic.setSize(ff.literal(HIGHLIGHT_SIZE));
            
            pointSymb.setGraphic(graphic);
            symb = pointSymb;
        }
        
        return symb;
    }
    
    public Geometry getSelectionPoly() {
        if (selectedTerritoryFeatures == null) {
            return null;
        }
        else {
            return (Geometry) selectedTerritoryFeatures.get(0).getDefaultGeometry();
        }
    }
    
    public SimpleFeatureCollection getSelectionCollection() {
        if (selectedTerritoryFeatures == null) {
            return null;
        }
        else {
            try {
                SimpleFeatureSource source = (SimpleFeatureSource) selectedTerritoryLayer.getFeatureSource();
                SimpleFeatureCollection collection = source.getFeatures();
                return collection;
            } catch (IOException ex) {
                System.out.println("Unexpected exception: "+ex);
            }
            return null;
        }
    }
    
    public void setSelectionPoly(Geometry poly) {
        if (selectedTerritoryFeatures == null) {
            selectedTerritoryFeatures = new ArrayList<SimpleFeature>();

            AttributeTypeBuilder atb = new AttributeTypeBuilder();
            atb.setBinding(Polygon.class);
            AttributeDescriptor geomDesc = atb.buildDescriptor("the_geom");

            SimpleFeatureTypeBuilder sftb = new SimpleFeatureTypeBuilder();
            sftb.add(geomDesc);
            sftb.setName("SELECTED_TERRITORY");
            sftb.setCRS(getMapContent().getCoordinateReferenceSystem());
            SimpleFeatureType sft = sftb.buildFeatureType();

            selectedTerritorySFB = new SimpleFeatureBuilder(sft);
            
            selectedTerritorySFB.add(poly);
            selectedTerritoryFeatures.add(selectedTerritorySFB.buildFeature(null));

            PolygonSymbolizer polySymb = sf.createPolygonSymbolizer();
            polySymb.setStroke(sf.createStroke(ff.literal(Color.ORANGE), ff.literal(1)));
            polySymb.setFill(sf.createFill(ff.literal(Color.ORANGE), ff.literal(0.5)));

            Rule rule = sf.createRule();
            rule.symbolizers().add(polySymb);

            FeatureTypeStyle fts = sf.createFeatureTypeStyle(new Rule[] {rule});

            Style style = sf.createStyle();
            style.featureTypeStyles().add(fts);

            selectedTerritoryLayer = new FeatureLayer(
                    new ListFeatureCollection(sft, selectedTerritoryFeatures),
                    style);

            getMapContent().addLayer(selectedTerritoryLayer);
        }
        else {
            selectedTerritoryFeatures.remove(0);
            
            selectedTerritorySFB.add(poly);
            selectedTerritoryFeatures.add(selectedTerritorySFB.buildFeature(null));

            MapLayerEvent mle = new MapLayerEvent(selectedTerritoryLayer, MapLayerEvent.DATA_CHANGED);
            MapLayerListEvent mlle = new MapLayerListEvent(getMapContent(), selectedTerritoryLayer,
                    getMapContent().layers().indexOf(selectedTerritoryLayer), mle);

            getMapPane().layerChanged(mlle);
        }
    }
    
}
