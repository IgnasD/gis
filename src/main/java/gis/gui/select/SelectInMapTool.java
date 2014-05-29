package gis.gui.select;

import gis.gui.MainFrame;
import gis.Utils;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.swing.dialog.JExceptionReporter;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * @author Ignas Daukšas
 */
public class SelectInMapTool extends CursorTool {
    private final MainFrame mainFrame;
    private final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private boolean dragged = false;
    private Point startPoint;
    private final Cursor cursor;

    public SelectInMapTool(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        Toolkit tk = Toolkit.getDefaultToolkit();
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("/select-cursor-icon.png"));
        cursor = tk.createCustomCursor(imgIcon.getImage(), new Point(), null);
    }
    
    @Override
    public boolean drawDragBox() {
        return true;
    }
    
    @Override
    public void onMouseClicked(MapMouseEvent ev) {
        try {
            selectFeatures(ev.getPoint(), ev.isControlDown());
        } catch (IOException ex) {
            JExceptionReporter.showDialog(ex);
        }
    }
    
    @Override
    public void onMousePressed(MapMouseEvent ev) {
        startPoint = ev.getPoint();
    }

    @Override
    public void onMouseDragged(MapMouseEvent ev) {
        dragged = true;
    }

    @Override
    public void onMouseReleased(MapMouseEvent ev) {
        if (dragged) {
            dragged = false;
            try {
                selectFeatures(startPoint, ev.getPoint(), ev.isControlDown());
            } catch (IOException ex) {
                JExceptionReporter.showDialog(ex);
            }
        }
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }
    
    
    private void selectFeatures(Point startScreenPos, Point endScreenPos, boolean append)
            throws IOException {
        Rectangle rectangle = new Rectangle();
        rectangle.setFrameFromDiagonal(startScreenPos, endScreenPos);
        selectFeatures(rectangle, append);
    }
    
    private void selectFeatures(Point screenPos, boolean append) throws IOException {
        selectFeatures(new Rectangle(screenPos.x-2, screenPos.y-2, 5, 5), append);
    }
    
    private void selectFeatures(Rectangle screenRect, boolean append) throws IOException {
        Layer layer = mainFrame.getFirstSelectedLayer();
        if (layer == null) {
            Utils.showMsg("No layer selected", "Please select active layer.");
            return;
        }
        SimpleFeatureSource source = (SimpleFeatureSource) layer.getFeatureSource();
        
        AffineTransform screenToWorld = getMapPane().getScreenToWorldTransform();
        Rectangle2D worldRect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
        ReferencedEnvelope bbox = new ReferencedEnvelope(worldRect,
                getMapPane().getMapContent().getCoordinateReferenceSystem());
        
        Filter filter = ff.bbox(
                ff.property(source.getSchema().getGeometryDescriptor().getLocalName()),
                bbox);
        
        SimpleFeatureCollection selectedFeatures = source.getFeatures(filter);
        
        if (!append) {
            mainFrame.resetFeatureSelection();
        }
        
        SimpleFeatureIterator iter = selectedFeatures.features();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                mainFrame.selectFeature(feature.getIdentifier());
            }
        } finally {
            iter.close();
        }
        
        mainFrame.highlightSelected();
    }
}
