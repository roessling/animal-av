package animal.editor.graphics;

import java.awt.Point;
import java.awt.Rectangle;

import animal.editor.Editor;
import animal.editor.graphics.meta.FillablePrimitiveEditor;
import animal.graphics.PTCircle;
import animal.graphics.PTGraphicObject;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a Circle
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class CircleEditor extends FillablePrimitiveEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public CircleEditor() {
    super();
  }

  public int pointsNeeded() {
    return 2;
  }

  public boolean nextPoint(int num, Point p) {
    PTCircle shape = (PTCircle) getCurrentObject();
    if (num == 1)
      shape.setCenter(p.x, p.y);
    if (num == 2)
      shape.setRadius(Math.abs(p.x - shape.getCenter().x));
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTCircle pg = (PTCircle) go;
    Point a = new Point(pg.getCenter().x, pg.getCenter().y);
    Rectangle boundingBox = pg.getBoundingBox();
    if (boundingBox.contains(p.x, p.y))
      return 0;

    // (ULC, URC)
    Point b = new Point(a.x + pg.getRadius(), a.y);
    int minDist = Integer.MAX_VALUE;
    int newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    // (URC, LRC)
    b.translate(0, pg.getRadius());
    newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    // (LRC, LLC)
    a.translate(pg.getRadius(), pg.getRadius());
    newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    newDist = MSMath.dist(p, a, pg.getCenter());
    if (newDist < minDist)
      minDist = newDist;
    return minDist;
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTCircle pg = (PTCircle) go;
    int radius = pg.getRadius();
    // add change points(nodes)
    EditPoint[] result = new EditPoint[5];
    Point helper = pg.getCenter();
    // helper = new Point(helper.x + radius, helper.y + radius);
    // result[0] = new EditPoint(1, helper);

    int x = helper.x;
    int y = helper.y;
    int nr = 0;
    result[nr++] = new EditPoint(2, new Point(x + radius, y + radius));
    result[nr++] = new EditPoint(-1, new Point(x - radius, y));
    result[nr++] = new EditPoint(-2, new Point(x, y + radius));
    result[nr++] = new EditPoint(-3, new Point(x + radius, y));
    result[nr++] = new EditPoint(-4, new Point(x, y - radius));

    return result;
  } // getEditPoints


  public EditableObject createObject() {
    PTCircle pg = new PTCircle();
    storeAttributesInto(pg);
    return pg;
  }


  public Editor getSecondaryEditor(EditableObject go) {
    CircleEditor result = new CircleEditor();
    // important! result must be of type CircleEditor (or cast)
    // and the parameter passed must be of type PTCircle.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTCircle.CIRCLE_TYPE;
  }
} // CircleEditor
