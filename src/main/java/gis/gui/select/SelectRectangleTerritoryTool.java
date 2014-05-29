package gis.gui.select;

import com.vividsolutions.jts.geom.Geometry;
import gis.gui.MainFrame;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;

/**
 * @author Ignas Daukšas
 */
public class SelectRectangleTerritoryTool extends CursorTool {
    private final Cursor cursor;
    private final MainFrame mainFrame;
    private Point startPoint;
    private boolean dragged;

    public SelectRectangleTerritoryTool(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        Toolkit tk = Toolkit.getDefaultToolkit();
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("/select-rectangle-cursor-icon.png"));
        cursor = tk.createCustomCursor(imgIcon.getImage(), new Point(), null);
        dragged = false;
    }
    
    @Override
    public Cursor getCursor() {
        return cursor;
    }
    
    @Override
    public boolean drawDragBox() {
        return true;
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
            mainFrame.setSelectionPoly(createSelectionPoly(startPoint, ev.getPoint()));
        }
    }
    
    private Geometry createSelectionPoly(Point startScreenPos, Point endScreenPos) {
        Rectangle screenRect = new Rectangle();
        screenRect.setFrameFromDiagonal(startScreenPos, endScreenPos);
        AffineTransform screenToWorld = getMapPane().getScreenToWorldTransform();
        Rectangle2D worldRect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
        ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(worldRect,
                getMapPane().getMapContent().getCoordinateReferenceSystem());
        return JTS.toGeometry(referencedEnvelope);
    }
}
