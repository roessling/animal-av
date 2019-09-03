package animal.editor.graphics;

import java.awt.Point;
import java.awt.Rectangle;

import animal.editor.Editor;
import animal.editor.graphics.meta.FillablePrimitiveEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTSquare;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a Square
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class SquareEditor extends FillablePrimitiveEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public SquareEditor() {
    super();
  }

  public int pointsNeeded() {
    return 2;
  }

  public boolean nextPoint(int num, Point p) {
    PTSquare square = (PTSquare) getCurrentObject();
    if (num == 1)
      square.setSquareNode(p.x, p.y);
    if (num == 2)
      square.setSize(p.x - square.getSquareNodeAsPoint().x);
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTSquare pg = (PTSquare) go;
    Point a = new Point(pg.getSquareNodeAsPoint().x, pg.getSquareNodeAsPoint().y);
    Rectangle boundingBox = pg.getBoundingBox();
    if (boundingBox.contains(p.x, p.y))
      return 0;

    // (ULC, URC)
    Point b = new Point(a.x + pg.getSize(), a.y);
    int minDist = Integer.MAX_VALUE;
    int newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    // (URC, LRC)
    b.translate(0, pg.getSize());
    newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    // (LRC, LLC)
    a.translate(pg.getSize(), pg.getSize());
    newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    newDist = MSMath.dist(p, a, pg.getSquareNodeAsPoint());
    if (newDist < minDist)
      minDist = newDist;
    return minDist;
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTSquare pg = (PTSquare) go;
    int size = pg.getSize();
    // add change points(nodes)
    EditPoint[] result = new EditPoint[5];
    Point helper = pg.getSquareNodeAsPoint();
    helper = new Point(helper.x + size, helper.y + size);
    result[0] = new EditPoint(2, helper);

    int x = pg.getSquareNodeAsPoint().x;
    int y = pg.getSquareNodeAsPoint().y;
    result[1] = new EditPoint(-2, new Point(x + (size / 2), y));
    result[2] = new EditPoint(-3, new Point(x + size, y + (size / 2)));
    result[3] = new EditPoint(-4, new Point(x + (size / 2), y + size));
    result[4] = new EditPoint(-5, new Point(x, y + (size / 2)));

    return result;
  } // getEditPoints

  public EditableObject createObject() {
    PTSquare pg = new PTSquare();
    storeAttributesInto(pg);
    return pg;
  }

  public Editor getSecondaryEditor(EditableObject go) {
    SquareEditor result = new SquareEditor();
    // important! result must be of type SquareEditor (or cast)
    // and the parameter passed must be of type PTSquare.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTSquare.SQUARE_TYPE;
  }

} // SquareEditor
