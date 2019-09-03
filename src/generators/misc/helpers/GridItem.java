package generators.misc.helpers;
import java.awt.Font;
import java.util.Set;

import algoanim.primitives.Circle;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

/**
 * Encapsulates all the functionality of a grid item.
 * @author chollubetz
 *
 */
public abstract class GridItem {
	
	public static final int SOURCE = -3;
	public static final int TRAIN = -2;
	public static final int WALL = -1;
	public static final int INITIALIZED = 0;
	
	Grid parent;
	
	Circle circle;
	Text text;
	
	/**
	 * Creates a new grid item.
	 * @param parent the grid containing the grid item
	 */
	public GridItem(Grid parent) {
		this.parent = parent;
	}
	
	/**
	 * Return the grid in which the grid item is placed.
	 * @return the parent grid
	 */
	public Grid getParent() {
		return parent;
	}
	
	/**
	 * Returns the circle which visualized the grid item.
	 * @return the visualizing circle
	 */
	public Circle getCircle() {
		return circle;
	}
	
	/**
	 * Sets the circle which visualizes the grid item.
	 * @param circle the visualizing circle
	 */
	public void setCircle(Circle circle) {
		this.circle = circle;
	}

	/**
	 * Returns the value of the grid item.
	 * @return the value of the grid item
	 */
	public abstract int getValue();
	
	/**
	 * Sets the value of the grid item.
	 * @param value the value to set
	 * @throws Exception An exception is thrown, when the value should be set for a special non regular grid item.
	 */
	public abstract void setValue(int value) throws Exception;
	
	/**
	 * Returns the position of the grid item.
	 * @return the position of the grid item
	 */
	public GridItemPosition getPosition() {
		return parent.getPositionOf(this);
	}
	
	/**
	 * Returns all the visitable surrounding grid items.
	 * @return the visitable surrounding grid items
	 */
	public Set<GridItem> getVisitableNeighbors() {
		return parent.findVisitableNeighborsOf(this);
	}
	
	/**
	 * Returns all the surrounding grid items with a specific value.
	 * @param value the value to filter for
	 * @return the surrounding grid items with the given value
	 */
	public Set<GridItem> getNeighborsWithValue(int value) {
		return parent.findNeighborsOfWithValue(this, value);
	}
	
	/**
	 * Labels the grid item by a specific text.
	 * @param label the text
	 * @param lang the language to draw to
	 */
	public void setText(String label, Language lang) {
		Coordinates c = (Coordinates) circle.getCenter();
		TextProperties tp = new TextProperties();
		Font f = new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD , 16);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, f);
		lang.newText(new Coordinates(c.getX() - 2 - label.length() * 3, c.getY() - 10), "", "", null, tp);
		this.text = lang.newText(new Coordinates(c.getX() - 2 - label.length() * 3, c.getY() - 10), label, "", null, tp);
	}
	
	/**
	 * Returns the text.
	 * @return the text of the label
	 */
	public Text getText() {
		return this.text;
	}
}
