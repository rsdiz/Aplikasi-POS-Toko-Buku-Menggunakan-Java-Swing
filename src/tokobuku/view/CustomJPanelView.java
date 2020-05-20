package tokobuku.view;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author Rosyid Iz
 */
public class CustomJPanelView {

    private UIManager ui = new UIManager();

    public void setPanel(String type) {
        switch (type) {
            case "black":
                ui.put("OptionPane.background", new ColorUIResource(0x333333));
                ui.put("Panel.background", new ColorUIResource(0x333333));
                ui.put("OptionPane.warningDialog.titlePane.background", new ColorUIResource(0x232323));
                ui.put("OptionPane.warningDialog.titlePane.foreground", new ColorUIResource(0xFFFFFF));
                ui.put("OptionPane.messageForeground", new ColorUIResource(0xFFFFFF));
                ui.put("OptionPane.foreground", new ColorUIResource(0xFFFFFF));
                ui.put("OptionPane.messageFont", new java.awt.Font("Metropolis Medium", 0, 14));
                ui.put("OptionPane.font", new java.awt.Font("Metropolis Medium", 0, 14));
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
        int value = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
        resetUI();
        return value;
    }

    private void resetUI() {
        ui.put("OptionPane.background", new ColorUIResource(238, 238, 238));
        ui.put("Panel.background", new ColorUIResource(238, 238, 238));
        ui.put("OptionPane.warningDialog.titlePane.background", new ColorUIResource(238, 238, 238));
        ui.put("OptionPane.warningDialog.titlePane.foreground", new ColorUIResource(0, 0, 0));
        ui.put("OptionPane.messageForeground", new ColorUIResource(0, 0, 0));
        ui.put("OptionPane.foreground", new ColorUIResource(0, 0, 0));
    }
}
