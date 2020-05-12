package tokobuku.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

/**
 * Enable feature to drag window
 * @author Rosyid Iz
 */
public class DragWindow {
    public DragWindow(Component component) {
        DragWindowListener dragWindow = new DragWindowListener();
        component.addMouseListener(dragWindow);
        component.addMouseMotionListener(dragWindow);
    }
}

class DragWindowListener extends MouseInputAdapter {
    private Point pointLocation;
    private MouseEvent mousesPressed;

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mousesPressed = mouseEvent;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Component component = mouseEvent.getComponent();
        pointLocation = component.getLocation(pointLocation);
        int x = pointLocation.x - mousesPressed.getX() + mouseEvent.getX();
        int y = pointLocation.y - mousesPressed.getY() + mouseEvent.getY();
        component.setLocation(x, y);
    }    

}