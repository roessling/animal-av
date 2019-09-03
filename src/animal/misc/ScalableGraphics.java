package animal.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Hashtable;

import translator.AnimalTranslator;

/**
 * ScalableGraphics is an extension of <code>Graphics</code> that provides
 * scaling of all graphic methods. It simply overrides all of
 * <code>Graphics</code>' methods and scales the input.
 * <p>
 * A typical use of <code>ScalableGraphics</code> would be in a paint method
 * and look like this:
 * 
 * <pre>
 * 
 *  ScalableGraphics sg = new ScalableGraphics();
 * 
 *  public void paint(Graphics g) {
 *      sg.setGraphics(g); // assign the Graphics object
 *      sg.drawOval(...) // paint
 *      // and so on
 *  }
 *  
 * </pre>
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.0 24.08.1998
 */
public class ScalableGraphics extends Graphics {
	/** the Graphics object associated with this object */
	private Graphics g = null;

	/** the magnification to be used. */
	private double mag = 1;

	/**
	 * as creating scaled Fonts is the most expensive operation in
	 * ScalableGraphics(slows down by factor 6), introduce these two Hashtables
	 * of already used Fonts. So if a font needs to be scaled, look it up here
	 * and create a new one only if none was found. Thus, a font must be scaled
	 * only once.
	 */
	private Hashtable<Font, Font> fontList = new Hashtable<Font, Font>(20);

	/**
	 * "inverse" list of <i>fontList </i>. If <i>fontList </i> contains(a,b),
	 * <i>inverseFontList </i> contains(b,a). This is required for getting
	 * fonts.
	 */
	private Hashtable<Font, Font> inverseFontList = new Hashtable<Font, Font>(20);

	/**
	 * associates a Graphics objects with this object. This must be done before
	 * performing any operation.
	 */
	public void setGraphics(Graphics aGraphicsContext) {
		g = aGraphicsContext;
	}

	public void setMagnification(double magnification) {
		mag = magnification;
		fontList.clear();
		inverseFontList.clear();
	}

	public double getMagnification() {
		return mag;
	}

	/**
	 * transforms the parameter according to the current magnification.
	 * 
	 * @returns coord*mag
	 */
	private int get(int coord) {
		if (mag == 1)
			return coord;
		if (mag == 0)
			MessageDisplay.errorMsg("Magnification is 0",
					MessageDisplay.PROGRAM_ERROR);
		return (int) Math.round(coord * mag);
	}

	/**
	 * the inverse operation of <code>get</code>, such that get(unget(x)) =
	 * x, unget(get(x)) = x, except for mathematical errors(what is
	 * "Rundungsfehler" in english? :-))
	 */
	private int unget(int coord) {
		if (mag == 1)
			return coord;
		return (int) Math.round(coord / mag);
	}

	/**
	 * transforms all points in the given array. Required for transformation of
	 * polylines.
	 */
	int[] getPoints(int[] points) {
		if (mag == 1)
			return points;
		int[] result = new int[points.length];
		for (int a = 0; a < points.length; a++)
			result[a] = get(points[a]);
		return result;
	}

	/***************************************************************************
	 * overwrite all methods from Graphics
	 **************************************************************************/

	public void translate(int x, int y) {
		g.translate(x, y);
	}

	public void setXORMode(Color c1) {
		g.setXORMode(c1);
	}

	public void setPaintMode() {
		g.setPaintMode();
	}

	public void setFont(Font font) {
		// just change the size.
		if (mag == 1)
			g.setFont(font);
		else {
			Font f = fontList.get(font);
			if (f == null) {
				f = new Font(font.getName(), font.getStyle(), get(font
						.getSize()));
				fontList.put(font, f);
				inverseFontList.put(f, font);
			}
			g.setFont(f);
		}
	}

	public void setColor(Color c) {
		g.setColor(c);
	}

	public void setClip(int x, int y, int width, int height) {
		g.setClip(get(x), get(y), get(width), get(height));
	}

	public void setClip(Shape clip) {
		g.setClip(clip);
	}

	public FontMetrics getFontMetrics(Font f) {
		return g.getFontMetrics(f);
	}

	public Font getFont() {
		Font originalFont = g.getFont();
		// must return a smaller font, such that
		// setFont(getFont()) doesn't alter the font.
		if (mag == 1)
			return originalFont;
		Font f = inverseFontList.get(originalFont);
		if (f == null) {
			f = new Font(originalFont.getName(), originalFont.getStyle(),
					unget(originalFont.getSize()));
			fontList.put(f, originalFont);
			inverseFontList.put(originalFont, f);
		}
		return f;
	}

	public Color getColor() {
		return g.getColor();
	}

	public Rectangle getClipBounds() {
		Rectangle r = g.getClipBounds();
		return new Rectangle(unget(r.x), unget(r.y), unget(r.width),
				unget(r.height));
	}

	public Shape getClip() {
		return g.getClip();
	}

	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		g.fillRoundRect(get(x), get(y), get(width), get(height), get(arcWidth),
				get(arcHeight));
	}

	public void fillRect(int x, int y, int width, int height) {
		g.fillRect(get(x), get(y), get(width), get(height));
	}

	public void fillPolygon(int[] xPoints, int []yPoints, int nPoints) {
		g.fillPolygon(getPoints(xPoints), getPoints(yPoints), nPoints);
	}

	public void fillOval(int x, int y, int width, int height) {
		g.fillOval(get(x), get(y), get(width), get(height));
	}

	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		g
				.fillArc(get(x), get(y), get(width), get(height), startAngle,
						arcAngle);
	}

	// needed by JDK 1.2+!
	public void drawString(AttributedCharacterIterator str, int x, int y) {
		g.drawString(str, get(x), get(y));
	}

	public void drawString(String str, int x, int y) {
		g.drawString(str, get(x), get(y));
	}

	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		g.drawRoundRect(get(x), get(y), get(width), get(height), get(arcWidth),
				get(arcHeight));
	}

	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		g.drawPolyline(getPoints(xPoints), getPoints(yPoints), nPoints);
	}

	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		g.drawPolygon(getPoints(xPoints), getPoints(yPoints), nPoints);
	}

	public void drawOval(int x, int y, int width, int height) {
		g.drawOval(get(x), get(y), get(width), get(height));
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(get(x1), get(y1), get(x2), get(y2));
	}

	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, Color bgcolor,
			ImageObserver observer) {
		return g.drawImage(img, get(dx1), get(dy1), get(dx2), get(dy2), sx1,
				sy1, sx2, sy2, bgcolor, observer);
	}

	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return g.drawImage(img, get(dx1), get(dy1), get(dx2), get(dy2), sx1,
				sy1, sx2, sy2, observer);
	}

	public boolean drawImage(Image img, int x, int y, int width, int height,
			Color bgcolor, ImageObserver observer) {
		return g.drawImage(img, get(x), get(y), get(width), get(height),
				bgcolor, observer);
	}

	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return g.drawImage(img, get(x), get(y), observer);
	}

	public boolean drawImage(Image img, int x, int y, int width, int height,
			ImageObserver observer) {
		return g.drawImage(img, get(x), get(y), get(width), get(height),
				observer);
	}

	public boolean drawImage(Image img, int x, int y, Color bgcolor,
			ImageObserver observer) {
		return g.drawImage(img, get(x), get(y), bgcolor, observer);
	}

	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		g
				.drawArc(get(x), get(y), get(width), get(height), startAngle,
						arcAngle);
	}

	public void dispose() {
		g.dispose();
	}

	public Graphics create() {
		return g.create();
	}

	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		g.copyArea(get(x), get(y), get(width), get(height), get(dx), get(dy));
	}

	public void clipRect(int x, int y, int width, int height) {
		g.clipRect(get(x), get(y), get(width), get(height));
	}

	public void clearRect(int x, int y, int width, int height) {
		g.clearRect(get(x), get(y), get(width), get(height));
	}

	public String toString() {
		return AnimalTranslator.translateMessage("sgToString",
				new Object[] { getClass().getName(),
				Double.valueOf(getMagnification()) });
	}
}
