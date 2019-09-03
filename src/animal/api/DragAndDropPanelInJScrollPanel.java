package animal.api;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

/**
 * reacts to mouse pressed and released events.
 */
public class DragAndDropPanelInJScrollPanel extends MouseAdapter {
  
  private JPanel panel;

	public DragAndDropPanelInJScrollPanel(JPanel panel) {
		this.panel = panel;
	}
	
	private Point origin;
	public void mousePressed(MouseEvent evt) {
	  origin = evt.getPoint();
	}

	public void mouseReleased( MouseEvent evt) {
	  panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

  @Override
  public void mouseDragged(MouseEvent e) {
    JScrollPane sp = null;
    try {
      sp = (JScrollPane) panel.getParent().getParent();
    } catch (Exception e2) {
    }
    if(sp!=null) {
      if(sp.getVerticalScrollBar().isVisible() || sp.getHorizontalScrollBar().isVisible()) {
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
      }
      if (origin != null) {
        JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, panel);
        if (viewPort != null) {
            int deltaX = origin.x - e.getX();
            int deltaY = origin.y - e.getY();
    
            Rectangle view = viewPort.getViewRect();
            view.x += deltaX;
            view.y += deltaY;
    
            panel.scrollRectToVisible(view);
        }
      }
    }
  }

}
