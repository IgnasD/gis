package gis.gui.select;

import gis.gui.MainFrame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 * @author Ignas Daukšas
 */
public class SelectRectangleTerritoryAction extends AbstractAction {
    private final MainFrame mainFrame;
    
    public SelectRectangleTerritoryAction(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/select-rectangle-icon.png")));
        putValue(SHORT_DESCRIPTION, "Select territory as rectangle");
    }

    public void actionPerformed(ActionEvent e) {
        mainFrame.getMapPane().setCursorTool(new SelectRectangleTerritoryTool(mainFrame));
    }
}
