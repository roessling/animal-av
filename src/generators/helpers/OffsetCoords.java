package generators.helpers;
import algoanim.util.Coordinates;
import algoanim.util.Node;
/**
 * 
 * @author Dirk Kröhan, Kamil Erhard
 *
 */
public class OffsetCoords extends Coordinates {

	public OffsetCoords(Coordinates ref, int x, int y) {
		super(ref.getX() + x, ref.getY() + y);
	}

	public OffsetCoords(Node ref, int x, int y) {
		this((Coordinates) ref, x, y);
	}
}