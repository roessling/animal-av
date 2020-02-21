package generators.misc.birch;

import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class OffsetHelper {
    public static Node offsetOf(Node node, int x, int y) {
        if (node instanceof Offset) {
            return new Offset(x + ((Offset) node).getX(), y + ((Offset) node).getY(), ((Offset) node).getRef(), ((Offset) node).getDirection());
        } else if (node instanceof Coordinates) {
            return new Coordinates(x + ((Coordinates) node).getX(), y + ((Coordinates) node).getY());
        } else {
            return node;
        }
    }
}
