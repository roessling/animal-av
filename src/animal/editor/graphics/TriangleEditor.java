package animal.editor.graphics;

import java.awt.Point;
import java.awt.Rectangle;

import animal.editor.Editor;
import animal.editor.graphics.meta.FillablePrimitiveEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTTriangle;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a Triangle
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class TriangleEditor extends FillablePrimitiveEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public TriangleEditor() {
    super();
  }

  public int pointsNeeded() {
    return 3;
  }

  public boolean nextPoint(int num, Point p) {
    PTTriangle triangle = (PTTriangle) getCurrentObject();
    switch (num) {
    case 1:
      triangle.setFirstNode(p.x, p.y);
      if (triangle.getSecondNode().x == triangle.getThirdNode().x
          && triangle.getSecondNode().y == triangle.getThirdNode().y) {
        triangle.setSecondNode(p.x + 5, p.y);
        triangle.setThirdNode(p.x + 5, p.y + 5);
      }
      break;
    case 2:
      triangle.setSecondNode(p.x, p.y);
      break;
    case 3:
      triangle.setThirdNode(p.x, p.y);
      break;
    }
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTTriangle pg = (PTTriangle) go;
    Rectangle boundingBox = pg.getBoundingBox();
    // if point is inside, there is not much of distance ;-)
    if (boundingBox.contains(p.x, p.y))
      return 0;

    int minDist = Integer.MAX_VALUE;
    int newDist = MSMath.dist(p, pg.getFirstNode(), pg.getSecondNode());
    if (newDist < minDist)
      minDist = newDist;
    newDist = MSMath.dist(p, pg.getSecondNode(), pg.getThirdNode());
    if (newDist < minDist)
      minDist = newDist;
    newDist = MSMath.dist(p, pg.getThirdNode(), pg.getFirstNode());
    if (newDist < minDist)
      minDist = newDist;
    return minDist;
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTTriangle pg = (PTTriangle) go;
    // add change points(nodes)
    EditPoint[] result = new EditPoint[6];
    result[0] = new EditPoint(1, pg.getFirstNode());
    result[1] = new EditPoint(2, pg.getSecondNode());
    result[2] = new EditPoint(3, pg.getThirdNode());
    Point start = pg.getFirstNode();
    Point end = pg.getSecondNode();
    result[3] = new EditPoint(-1, new Point((start.x + end.x) / 2,
        (start.y + end.y) / 2));
    start = end;
    end = pg.getThirdNode();
    result[4] = new EditPoint(-1, new Point((start.x + end.x) / 2,
        (start.y + end.y) / 2));
    start = end;
    end = pg.getFirstNode();
    result[5] = new EditPoint(-1, new Point((start.x + end.x) / 2,
        (start.y + end.y) / 2));

    return result;
  } // getEditPoints

  public EditableObject createObject() {
    PTTriangle pg = new PTTriangle();
    storeAttributesInto(pg);
    return pg;
  }

  public Editor getSecondaryEditor(EditableObject go) {
    TriangleEditor result = new TriangleEditor();
    // important! result must be of type RectangleEditor (or cast)
    // and the parameter passed must be of type PTTriangle.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTTriangle.TRIANGLE_TYPE;
  }
} // TriangleEditor
