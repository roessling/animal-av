package animal.editor.graphics;

import java.awt.Point;

import animal.editor.Editor;
import animal.editor.graphics.meta.ArrowablePrimitiveEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a Polyline
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public class PolylineEditor extends ArrowablePrimitiveEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public PolylineEditor() {
    super(false);
  }

  public int pointsNeeded() {
    return -1;
  }

  public boolean nextPoint(int num, Point p) {
    PTPolyline pl = (PTPolyline) getCurrentObject();
    pl.setNode(num - 1, new PTPoint(p));
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTPolyline pg = (PTPolyline) go;
    // if the polyline is a filled polygon and the point is inside
    // there is not much of distance
    Point a;
    Point b = pg.getNodeAsPoint(0);
    int minDist = Integer.MAX_VALUE;
    int newDist;
    // iterate all edges of the polyline
    for (int i = 1; i < pg.getNodeCount(); i++) {
      a = b;
      b = pg.getNodeAsPoint(i);
      if (!a.equals(b) && (newDist = MSMath.dist(p, a, b)) < minDist)
        minDist = newDist;
    }
    return minDist;
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTPolyline pg = (PTPolyline) go;
    int pSize = pg.getNodeCount();
    int size = pSize * 2 - 1;
    int i;
    // add change points(nodes)
    EditPoint[] result = new EditPoint[size];
    for (i = 0; i < pSize; i++)
      result[i] = new EditPoint(i + 1, pg.getNodeAsPoint(i));
    // add move points(points halfway the edges
    Point a;
    Point b;
    b = pg.getNodeAsPoint(0);
    for (i = 1; i < pSize; i++) {
      a = b;
      b = pg.getNodeAsPoint(i);
      result[i + pSize - 1] = new EditPoint(-i, new Point((a.x + b.x) / 2,
          (a.y + b.y) / 2));
    }
    return result;
  } // getEditPoints

  public EditableObject createObject() {
    PTPolyline pg = new PTPolyline();
    storeAttributesInto(pg);
    return pg;
  }

  public Editor getSecondaryEditor(EditableObject go) {
    PolylineEditor result = new PolylineEditor();
    // important! result must be of type PolylineEditor(or casted)
    // and the parameter passed must be of type PTPolyline.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTPolyline.POLYLINE_TYPE;
  }
} // PolylineEditor
