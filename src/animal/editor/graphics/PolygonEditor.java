package animal.editor.graphics;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;

import animal.editor.Editor;
import animal.editor.graphics.meta.FillablePrimitiveEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolygon;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a Polygon
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class PolygonEditor extends FillablePrimitiveEditor implements
    ItemListener, ActionListener, PropertyChangeListener {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public PolygonEditor() {
    super();
  }

  public int pointsNeeded() {
    return -1;
  }

  public boolean nextPoint(int num, Point p) {
    PTPolygon pl = (PTPolygon) getCurrentObject();
    pl.setNode(num - 1, new PTPoint(p));
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTPolygon pg = (PTPolygon) go;
    // if the polyline is a filled polygon and the point is inside
    // there is not much of distance
    if (pg.isFilled()) {
      Polygon poly = pg.toPolygon();
      if (poly.contains(p))
        return 0;
    }
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
    PTPolygon pg = (PTPolygon) go;
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
    PTPolygon pg = new PTPolygon();
    storeAttributesInto(pg);
    return pg;
  }

  public Editor getSecondaryEditor(EditableObject go) {
    PolygonEditor result = new PolygonEditor();
    // important! result must be of type PolygonEditor(or casted)
    // and the parameter passed must be of type PTPolygon.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTPolygon.POLYGON_TYPE;
  }
} // PolygonEditor
