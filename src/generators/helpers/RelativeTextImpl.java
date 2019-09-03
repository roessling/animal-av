package generators.helpers;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;
/**
 * 
 * @author Dirk Kröhan, Kamil Erhard
 *
 */
public class RelativeTextImpl extends Text implements RelativeText {

	private final Language lang;

	public RelativeTextImpl(TextGenerator tg, Node upperLeftCorner, String theText, String name, DisplayOptions display,
			TextProperties tp) {

		super(tg, upperLeftCorner, theText, name, display, tp);

		this.lang = tg.getLanguage();

		Coordinates c = (Coordinates) upperLeftCorner;

		this.x = c.getX();
		this.y = c.getY();
	}

	private Integer x, y;

	@Override
	public TextProperties getProperties() {
		return super.getProperties();
	}

	@Override
	public void moveBy(String moveType, int dx, int dy, Timing delay, Timing duration) {
		Polyline line = TextUtil.getVia(this.lang, this.getX(), this.getY(), this.getX() + dx, this.getY() + dy);

		this.moveVia(AnimalScript.DIRECTION_E, "translate", line, delay, duration);
	}

	@Override
	public void moveTo(String direction, String moveType, Node target, Timing delay, Timing duration) throws IllegalDirectionException {
		Coordinates cTo = (Coordinates) target;

		int dx = cTo.getX() - this.x;
		int dy = cTo.getY() - this.y;

		this.moveBy(moveType, dx, dy, delay, duration);
	}

	public void moveTo(Node target, Timing delay, Timing duration) {
		try {
			this.moveTo(AnimalScript.DIRECTION_N, "translate", target, delay, duration);
		} catch (IllegalDirectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void moveVia(String direction, String moveType, Primitive via, Timing delay, Timing duration) {

		try {
			super.moveVia(direction, moveType, via, delay, duration);
		} catch (IllegalDirectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Polyline pl = (Polyline) via;
		Coordinates c = (Coordinates) pl.getNodes()[1];

		this.x = c.getX();
		this.y = c.getY();
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public Node getUpperLeft() {
		if ((this.x == null) || (this.y == null)) {
			return super.getUpperLeft();
		} else {
			return new Coordinates(this.x, this.y);
		}
	}

	public int getWidth() {

		int x;
		if (!TextUtil.isAnimalLoaded) {
			x = 100;
		} else {
			x = Animal.getStringWidth(this.getText(), TextUtil.getFont());
		}

		return x;
	}
}