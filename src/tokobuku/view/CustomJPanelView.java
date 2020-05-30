package tokobuku.view;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import tokobuku.util.CustomFont;

/**
 *
 * @author Rosyid Iz
 */
public class CustomJPanelView {

    public void setPanel(String type) {
        switch (type) {
            case "black":
                UIManager.put("OptionPane.background", new ColorUIResource(0x333333));
                UIManager.put("Panel.background", new ColorUIResource(0x333333));
                UIManager.put("OptionPane.warningDialog.titlePane.background", new ColorUIResource(0x232323));
                UIManager.put("OptionPane.warningDialog.titlePane.foreground", new ColorUIResource(0xFFFFFF));
                UIManager.put("OptionPane.messageForeground", new ColorUIResource(0xFFFFFF));
                UIManager.put("OptionPane.foreground", new ColorUIResource(0xFFFFFF));
                UIManager.put("OptionPane.messageFont", CustomFont.getFont("Bahnschrift", 14));
                UIManager.put("OptionPane.font", CustomFont.getFont("Bahnschrift", 14));
                break;
        }
    }

    public void displayWarning(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
        resetUI();
    }

    public void displayInfo(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
        resetUI();
    }

    public void displayError(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
        resetUI();
    }

    public void displayPlain(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
        resetUI();
    }

    public int displayConfirmDialog(Component parent, String message, String title) {
        int value = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);
        resetUI();
        return value;
    }

    private void resetUI() {
        UIManager.put("OptionPane.background", new ColorUIResource(238, 238, 238));
        UIManager.put("Panel.background", new ColorUIResource(238, 238, 238));
        UIManager.put("OptionPane.warningDialog.titlePane.background", new ColorUIResource(238, 238, 238));
        UIManager.put("OptionPane.warningDialog.titlePane.foreground", new ColorUIResource(0, 0, 0));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(0, 0, 0));
        UIManager.put("OptionPane.foreground", new ColorUIResource(0, 0, 0));
    }
}
