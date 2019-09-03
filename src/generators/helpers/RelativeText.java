package generators.helpers;
import java.awt.Color;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
/**
 * 
 * @author Dirk Kröhan, Kamil Erhard
 *
 */
public interface RelativeText {

	public void moveBy(String moveType, int dx, int dy, Timing delay, Timing duration);

	public void moveTo(String direction, String moveType, Node target, Timing delay, Timing duration)
	throws IllegalDirectionException;

	public void moveTo(Node target, Timing delay, Timing duration);

	public void moveVia(String direction, String moveType, Primitive via, Timing delay, Timing duration);

	public int getX();

	public int getY();

	public void setX(int x);

	public void setY(int y);

	public Node getUpperLeft();

	public int getWidth();

	public void changeColor(String colorType, Color newColor, Timing t, Timing d);

	public void setText(String newText, Timing delay, Timing duration);

	public void hide();

	public void hide(Timing t);

	public void show();

	public void show(Timing t);

	public String getText();

	public TextProperties getProperties();
}