package generators.misc.helperSimpleElevator;

import algoanim.primitives.Circle;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;

public class Passanger{

	private final Circle c;
	private final Text t;
	
	public Passanger (Language lang, int radius, int x_pos, int y_pos, String text, int id, CircleProperties passanger_props) {
		c = lang.newCircle(new Coordinates(x_pos, y_pos), radius, "circ"+id, null, passanger_props);
		t = lang.newText(new Coordinates(x_pos-radius/2+1, y_pos-radius/2-1), text, "text"+id, null);
	}

	public void moveBy(String object, int i, int j, Timing object2, Timing defaultTiming) {
		c.moveBy(object, i, j, object2, defaultTiming);
		t.moveBy(object, i, j, object2, defaultTiming);
	}

	public void hide(Timing defaultTiming) {
		c.hide(defaultTiming);
		t.hide(defaultTiming);
	}

	public Coordinates getCenter() {
		return (Coordinates) c.getCenter();
	}
	

}
