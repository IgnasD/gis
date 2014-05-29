package gis.gui.select;

import gis.gui.MainFrame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 * @author Ignas Daukšas
 */
public class SelectInMapAction extends AbstractAction {
    private final MainFrame mainFrame;
    
    public SelectInMapAction(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/select-icon.png")));
        putValue(SHORT_DESCRIPTION, "Select features in map");
    }

    public void actionPerformed(ActionEvent e) {
        mainFrame.getMapPane().setCursorTool(new SelectInMapTool(mainFrame));
    }
}
