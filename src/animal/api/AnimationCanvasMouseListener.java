package animal.api;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import animal.editor.graphics.meta.GraphicEditor;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.main.Animation;
import animal.main.AnimationCanvas;
import animal.main.AnimationState;
import animal.main.AnimationWindow;
import animal.main.Link;

/**
 * reacts to mouse pressed and released events.
 */
public class AnimationCanvasMouseListener extends MouseAdapter {

	AnimationCanvas canvas = null;

	public AnimationCanvasMouseListener(AnimationCanvas theCanvas) {
		canvas = theCanvas;
	}

	/**
	 * Select the object next to <i>p</i>, but not more distant than <i>tolerance</i>
	 * pixels. If several objects have distance 0, the topmost one is picked. As
	 * selected objects are placed atop of nonselected in the DrawCanvas, they are
	 * considered first.
	 * 
	 * @return the number of the GraphicObject if actually an object was
	 *         (de-)selected, <br>
	 *         -1 otherwise, i.e. no object was found within the neighborhood of
	 *         p.
	 */
	PTGraphicObject select(GraphicVector objects, Point p, int tolerance) {
		int min = Integer.MAX_VALUE; // the smallest distance;
		int dist; // the distance of the current object
		GraphicVectorEntry gve; // for iteration
		GraphicVectorEntry next = null; // the object closest to p.
		// iterate from top to bottom.
		for (int a = objects.getSize() - 1; a >= 0; a--) {
			gve = objects.elementAt(a);
			if (gve != null) {
				// the Editor is needed for getting the distance.
				GraphicEditor e = ((GraphicEditor) gve.go.getEditor());
				if (e != null) {
					dist = e.getMinDist(gve.go, p);
					if (dist < min) {
						min = dist;
						next = gve;
					}
				}
			}
		}

		// no object found at all or object too far away.
		if (min > tolerance || next == null) {
			return null;
		}

		// and its number returned.
		return next.go;
	} // select

	public void mousePressed(MouseEvent evt) {
		double magFactor = canvas.getMagnification();
		Point clickedPoint = new Point((int) Math.round(evt.getX() / magFactor),
				(int) Math.round(evt.getY() / magFactor));
		AnimationWindow animWindow = AnimalMainWindow.getWindowCoordinator()
				.getAnimationWindow(false);
		AnimationState currentState = animWindow.getAnimationState();
		if (currentState == null)
			return;
		Animation anim = currentState.getAnimation();
		Link currentLink = anim.getLink(currentState.getStep());
		PTGraphicObject ptgo = select(canvas.getObjects(), clickedPoint, 20);
		if (ptgo != null) {
			int ptgoNum = ptgo.getNum(false);
			if (currentLink.getMode() == Link.WAIT_CLICK) {
				int targetLinkGONum = currentLink.getTargetObjectID();
				if (targetLinkGONum == ptgoNum) {
					//animWindow.enableControls(true);
				}
			}
		}

	} // mousePressed
	
	public void mouseClicked(MouseEvent evt) {
	  if(SwingUtilities.isRightMouseButton(evt)) {
	    PTGraphicObject ptgo = select(canvas.getObjects(), evt.getPoint(), 20);
	    if(ptgo!=null) {
	      //System.out.println(ptgo);
	    }
	  }
	}

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    AnimationWindow aw = AnimalMainWindow.getWindowCoordinator()
        .getAnimationWindow(false);
    int notches = e.getWheelRotation();
    Point p = e.getPoint();
    double oldScale = currentScale;
    double newScale = 1.0;
    if (notches < 0) {
      newScale = getScale(false);
      aw.setMagnification(newScale);
    } else {
      newScale = getScale(true);
      aw.setMagnification(newScale);
    }
    p.move((int)(p.x*(newScale/oldScale)), (int)(p.y*(newScale/oldScale)));
    final double paramNewScale = newScale;
    new Thread(new Runnable() {
      public void run() {
          long startWaitTime = System.currentTimeMillis(); 
          int waitSeconde = 10;
          long lastWaitTime = startWaitTime+waitSeconde*1000; 
           while(System.currentTimeMillis()<=lastWaitTime) {
             if(canvas.getMagnification()==paramNewScale && canvas.getLastPaintedTime()>startWaitTime) {
               canvas.scrollRectToVisible(getRectWithPointInMiddle(p));
               break;
             }
             try {
              Thread.sleep(1);
            } catch (InterruptedException e) {
            }
           }
      }
    }).start();
  }

  private double currentScale = 1.0;
  private double getScale(boolean scrolldown) {
    AnimationWindow aw = AnimalMainWindow.getWindowCoordinator()
        .getAnimationWindow(false);
    currentScale = aw.getMagnification();
    double magStepValue = aw.getMagnificationStepValue();
    double magMaxValue = aw.getMagnificationMaxValue();
    double magMinValue = aw.getMagnificationMinValue();
    
    double newScale = currentScale;
    if(scrolldown) {//ZoomOut
      newScale = Math.max(magMinValue, currentScale-magStepValue);
    } else {//ZoomIn
      newScale = Math.min(magMaxValue, currentScale+magStepValue);
    }
    currentScale = newScale;
    return newScale;
  }
  
  private Rectangle getRectWithPointInMiddle(Point p) {
    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, canvas);
    Rectangle view = viewPort.getViewRect();
    int width = viewPort.getWidth();
    int height = viewPort.getHeight();
    view.x = p.x - width/2;
    view.y = p.y - height/2;
    return view;
  }

} // AnimationCanvasMouseListener
