package gis;

import gis.gui.MainFrame;
import java.io.File;
import java.io.IOException;

/**
 * @author Ignas Daukšas
 */
public class App {
    
    private static void setLookAndFeel() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } catch (InstantiationException ex) {
            System.out.println(ex);
        } catch (IllegalAccessException ex) {
            System.out.println(ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) throws IOException {
        setLookAndFeel();
        
        File file = Utils.fileChooser();
        if (file == null) {
            return;
        }
        MainFrame mainFrame = new MainFrame(Utils.createLayer(file));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
