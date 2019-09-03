package generators.helpers.binarySpacePartitioning;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.Circle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

/**
 * A graphical representation of an ordinary binary tree node used for graphical
 * (animal-script) tree traversal and creation.
 */
public class Node {

  private static int _radius     = 10;

  private static int _posX       = 10;
  private static int _posY       = 100;
  private static int _sizeX      = 200;

  private int        _x, _y;
  private String     _titel;

  private Node       _parent;
  private Circle     _circle;
  private Polygon    _connection = null;

  public Node(int posX, int posY, int sizeX) {
    _posX = posX;
    _posY = posY;
    _sizeX = sizeX;

    _x = _sizeX / 2;
    _y = _radius * 2;
    _titel = "";
  }

  public Node(Node parent, int x, int y, String titel) {
    _x = x;
    _y = y;
    _titel = titel;
    _parent = parent;
  }

  /**
   * Since a BSP tree divides space in two regions only (binary tree), only two
   * children can be created.
   * 
   * @param index
   *          Index of child node. 0 = back, 1 = front.
   */
  public Node createChild(int index) {
    if (_parent != null) {
      int dx = Math.abs(_x - _parent.getX());

      if (index == 0)
        return new Node(this, _x - dx / 2, _y + 30, "-");
      else
        return new Node(this, _x + dx / 2, _y + 30, "+");

    } else {
      if (index == 0)
        return new Node(this, _x - _x / 2, _y + 30, "-");
      else
        return new Node(this, _x + _x / 2, _y + 30, "+");
    }
  }

  public int getX() {
    return _x;
  }

  public int getY() {
    return _y;
  }

  public Circle getCircle() {
    return _circle;
  }

  /**
   * Displays this node and its connections to the parent node if existing.
   */
  public void draw(Language lang, Color color) {

    // Draw circle:
    CircleProperties circleProps = new CircleProperties();
    circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
    circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

    Coordinates center = new Coordinates(_posX + _x, _posY + _y);
    _circle = lang.newCircle(center, _radius, "Polygon", null, circleProps);

    // Draw connection to parent:
    if (_parent != null) {
      Vector pos = new Vector(_posX + _x, _posY + _y);
      Vector parentPos = new Vector(_posX + _parent.getX(), _posY
          + _parent.getY());
      Vector d = parentPos.subtract(pos).calcDirection().multiply(_radius);
      parentPos = parentPos.subtract(d);
      _connection = new Polygon(new Vector[] { pos, parentPos });
      _connection.draw(lang, color);
    }

    // Draw text:
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.BOLD, 22));
    Coordinates textPos = new Coordinates(_posX + _x - 6, _posY + _y - 17);
    lang.newText(textPos, _titel, "Text", null, textProps);

  }

  public void highlight(Language lang, Color color) {

    CircleProperties circleProps = new CircleProperties();
    circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
    circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

    Coordinates center = new Coordinates(_posX + _x, _posY + _y);
    _circle = lang.newCircle(center, _radius, "Polygon", null, circleProps);
  }
}
