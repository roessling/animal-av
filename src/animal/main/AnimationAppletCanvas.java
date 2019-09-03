package animal.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import animal.gui.DrawCanvas;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.misc.ScalableGraphics;

/**
 * The Canvas that shows the graphic objects. Used only by AnimationApplet.
 */
public class AnimationAppletCanvas extends Canvas {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2288981608142011801L;

	/** image for double buffering */
	private Image image = null;

	/** The GraphicObjects to be displayed */
	private GraphicVector objects = null;

	/**
	 * the size the Canvas had when painted last. Needed for getting a new double
	 * buffering image when the size has changed.
	 */
	private Dimension oldSize = new Dimension(0, 0);

	/** the ScalableGraphics Object that scales the output */
	private ScalableGraphics sg = new ScalableGraphics();

	// private AnimationApplet theApplet;

	/**
	 * sets the magnification.
	 */
	void setMagnification(double mag) {
		sg.setMagnification(mag);
		repaintNow();
	}

	public AnimationAppletCanvas( AnimationApplet model) {
		super();
		// theApplet = model;
	}

	/** paint the current non-temporary GraphicObjects */
	public void paint(Graphics g) {
		// a window must not have a size < 1
		Dimension size = DrawCanvas.ensureLegalSize(getSize());

		Graphics ig = null;
		// new double buffering image needed?
		if (!size.equals(oldSize)) {
			image = createImage(size.width, size.height);
			oldSize = size;
		}
		if (image != null) { // maybe window was not yet shown
			ig = image.getGraphics();
			ig.clearRect(0, 0, size.width, size.height);
			// if magnification is 1, paint directly into the image,
			// otherwise use ScalableGraphics.
			Graphics where;
			if (sg.getMagnification() == 1)
				where = ig;
			else {
				sg.setGraphics(ig);
				where = sg;
			}
			if (objects != null) {
				GraphicVectorEntry gve;
				// paint all GraphicObjects that are not temporary.
				// Temporary Objects can only be shown in DrawWindow
				// but never in AnimationApplet
				for (int a = 0; a < objects.getSize(); a++) {
					gve = objects.elementAt(a);
					if (!gve.isTemporary())
						gve.go.paint(where);
				}
			}
			// finally copy the image to the screen.
			g.drawImage(image, 0, 0, this);
			ig.dispose();
		}
	}

	/**
	 * repaints the GraphicObjects. A convenience method as no GraphicsContext has
	 * to be passed as a parameter.
	 */
	void repaintNow() {
		paint(getGraphics());
	}

	/**
	 * sets the GraphicVector to be drawn to <i>objects</i>.
	 */
	void setObjects(GraphicVector graphicalObjects) {
		objects = graphicalObjects;
	}
}
