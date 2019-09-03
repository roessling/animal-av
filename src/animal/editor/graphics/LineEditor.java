package animal.editor.graphics;

import java.awt.Point;
import java.awt.Rectangle;

import animal.editor.Editor;
import animal.editor.graphics.meta.ArrowablePrimitiveEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTLine;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a Polyline
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class LineEditor extends ArrowablePrimitiveEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public LineEditor() {
    super(false);
  }

  public int pointsNeeded() {
    return 2;
  }

  public boolean nextPoint(int num, Point p) {
    PTLine line = (PTLine) getCurrentObject();
    if (num == 1)
      line.setFirstNode(p.x, p.y);
    else if (num == 2)
      line.setLastNode(p.x, p.y);
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTLine pg = (PTLine) go;
    Rectangle boundingBox = pg.getBoundingBox();
    if (boundingBox.contains(p.x, p.y))
      return 0;

    // (ULC, URC)
    int minDist = Integer.MAX_VALUE;
    int newDist = MSMath.dist(p, pg.getFirstNode().toPoint(), pg.getLastNode().toPoint());
    if (newDist < minDist)
      minDist = newDist;

    return minDist;
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTLine pg = (PTLine) go;
    // add change points(nodes)
    EditPoint[] result = new EditPoint[3];
    Point startNode = pg.getFirstNode().toPoint();
    Point endNode = pg.getLastNode().toPoint();
    result[0] = new EditPoint(0, startNode);
    result[1] = new EditPoint(1, endNode);
    result[2] = new EditPoint(-1, new Point((startNode.x + endNode.x) / 2,
    		(startNode.y + endNode.y) / 2));
    return result;
  } // getEditPoints

  public EditableObject createObject() {
    PTLine pg = new PTLine();
    storeAttributesInto(pg);
    return pg;
  }

  public Editor getSecondaryEditor(EditableObject go) {
    LineEditor result = new LineEditor();
    // important! result must be of type SquareEditor (or cast)
    // and the parameter passed must be of type PTLine.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTLine.LINE_TYPE;
  }
} // LineEditor
