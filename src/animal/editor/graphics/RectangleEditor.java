package animal.editor.graphics;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import animal.editor.Editor;
import animal.editor.graphics.meta.FillablePrimitiveEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTRectangle;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a Polyline
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class RectangleEditor extends FillablePrimitiveEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public RectangleEditor() {
    super();
  }

  public int pointsNeeded() {
    return 2;
  }

  public boolean nextPoint(int num, Point p) {
    PTRectangle rectangle = (PTRectangle) getCurrentObject();
    if (num == 1)
      rectangle.setStartNode(p.x, p.y);
    if (num == 2) {
      Point firstNode = rectangle.getStartNode();
      rectangle.setSize(new Dimension(p.x - firstNode.x, p.y - firstNode.y));
    }
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTRectangle pg = (PTRectangle) go;
    Point a = new Point(pg.getStartNode().x, pg.getStartNode().y);
    Rectangle boundingBox = pg.getBoundingBox();
    // if point is inside, there is not much of distance ;-)
    if (boundingBox.contains(p.x, p.y))
      return 0;

    // (ULC, URC)
    Point b = new Point(a.x + pg.getWidth(), a.y);
    int minDist = Integer.MAX_VALUE;
    int newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    // (URC, LRC)
    b.translate(0, pg.getHeight());
    newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    // (LRC, LLC)
    a.translate(pg.getWidth(), pg.getHeight());
    newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    newDist = MSMath.dist(p, a, pg.getStartNode());
    if (newDist < minDist)
      minDist = newDist;
    return minDist;
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTRectangle pg = (PTRectangle) go;
    // int pSize = 2;
    int width = pg.getWidth();
    int height = pg.getHeight();
    // int i;
    // add change points(nodes)
    EditPoint[] result = new EditPoint[5];
    Point helper = pg.getStartNode();
    // result[5] = new EditPoint(1, helper);
    helper = new Point(helper.x + width, helper.y + height);
    result[0] = new EditPoint(2, helper);

    int x = pg.getStartNode().x;
    int y = pg.getStartNode().y;
    result[1] = new EditPoint(-2, new Point(x + (width / 2), y));
    result[2] = new EditPoint(-3, new Point(x + width, y + (height / 2)));
    result[3] = new EditPoint(-4, new Point(x + (width / 2), y + height));
    result[4] = new EditPoint(-5, new Point(x, y + (height / 2)));

    return result;
  } // getEditPoints


  public EditableObject createObject() {
    PTRectangle pg = new PTRectangle();
    storeAttributesInto(pg);
    return pg;
  }

  public Editor getSecondaryEditor(EditableObject go) {
    RectangleEditor result = new RectangleEditor();
    // important! result must be of type RectangleEditor (or cast)
    // and the parameter passed must be of type PTRectangle.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTRectangle.RECTANGLE_TYPE;
  }
} // RectangleEditor
