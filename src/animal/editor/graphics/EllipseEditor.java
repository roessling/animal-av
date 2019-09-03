package animal.editor.graphics;

import java.awt.Point;
import java.awt.Rectangle;

import animal.editor.Editor;
import animal.editor.graphics.meta.FillablePrimitiveEditor;
import animal.graphics.PTEllipse;
import animal.graphics.PTGraphicObject;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a Ellipse
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class EllipseEditor extends FillablePrimitiveEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public EllipseEditor() {
    super();
  }

  public int pointsNeeded() {
    return 2;
  }

  public boolean nextPoint(int num, Point p) {
    PTEllipse shape = (PTEllipse) getCurrentObject();
    if (num == 1)
      shape.setCenter(p.x, p.y);
    if (num == 2)
      shape.setRadius(Math.abs(p.x - shape.getCenter().x), Math.abs(p.y
          - shape.getCenter().y));
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTEllipse pg = (PTEllipse) go;
    Point a = new Point(pg.getCenter().x, pg.getCenter().y);
    Rectangle boundingBox = pg.getBoundingBox();
    if (boundingBox.contains(p.x, p.y))
      return 0;

    // (ULC, URC)
    Point b = new Point(a.x + pg.getXRadius(), a.y);
    int minDist = Integer.MAX_VALUE;
    int newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    // (URC, LRC)
    b.translate(0, pg.getXRadius());
    newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    // (LRC, LLC)
    a.translate(pg.getXRadius(), pg.getYRadius());
    newDist = MSMath.dist(p, a, b);
    if (newDist < minDist)
      minDist = newDist;

    newDist = MSMath.dist(p, a, pg.getCenter());
    if (newDist < minDist)
      minDist = newDist;
    return minDist;
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTEllipse pg = (PTEllipse) go;
    Point radius = pg.getRadius();
    // add change points(nodes)
    EditPoint[] result = new EditPoint[5];
    Point helper = pg.getCenter();
 
    int x = helper.x;
    int y = helper.y;
    int nr = 0;
    result[nr++] = new EditPoint(2, new Point(x + radius.x, y + radius.y));
    result[nr++] = new EditPoint(-1, new Point(x - radius.x, y));
    result[nr++] = new EditPoint(-2, new Point(x, y + radius.y));
    result[nr++] = new EditPoint(-3, new Point(x + radius.x, y));
    result[nr++] = new EditPoint(-4, new Point(x, y - radius.y));

    return result;
  } // getEditPoints


  public EditableObject createObject() {
    PTEllipse pg = new PTEllipse();
    storeAttributesInto(pg);
    return pg;
  }

  public Editor getSecondaryEditor(EditableObject go) {
    EllipseEditor result = new EllipseEditor();
    // important! result must be of type EllipseEditor (or cast)
    // and the parameter passed must be of type PTEllipse.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTEllipse.ELLIPSE_TYPE;
  }
} // EllipseEditor
