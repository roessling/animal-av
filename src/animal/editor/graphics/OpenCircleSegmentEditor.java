package animal.editor.graphics;

import java.awt.Point;
import java.awt.Rectangle;

import animal.editor.Editor;
import animal.editor.graphics.meta.OrientedArrowablePrimitiveEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTOpenCircleSegment;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * Editor for a closed circle segment (also known as a "pie" for pie charts)
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class OpenCircleSegmentEditor 
extends OrientedArrowablePrimitiveEditor {
  
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2413776784253970663L;

  public OpenCircleSegmentEditor() {
    super();
  }

  /**
   * returns the number of points or clicks needed to draw this primitive Here,
   * the number is four:
   * 
   * <ol>
   * <li>the primitive's center</li>
   * <li>the primitive's radius</li>
   * <li>the primitive's start angle</li>
   * <li>the primitive's end angle</li>
   * </ol>
   */
  public int pointsNeeded() {
    return 4;
  }

  public boolean nextPoint(int num, Point p) {
    PTOpenCircleSegment shape = (PTOpenCircleSegment) getCurrentObject();
    switch (num) {
    case 1:
      shape.setCenter(p.x, p.y);
      break;
    case 2:
      shape.setRadius(p.x - shape.getCenter().x);
      break;
    case 3:
      shape.setStartAngle(shape.getAngle(p));
      break;
    case 4:
      int angle = shape.getAngle(p) - shape.getStartAngle();
      if (angle <= 0)
        angle += 360;
      shape.setTotalAngle(angle);
      // now use the real attributes again
      // shape.setFilled(filledCB.isSelected());
      shape.setFWArrow(fwArrow.isSelected());
      shape.setBWArrow(bwArrow.isSelected());
      break;
    }
    return true;
  } // nextPoint;

  public int getMinDist(PTGraphicObject go, Point p) {
    PTOpenCircleSegment pg = (PTOpenCircleSegment) go;
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
    PTOpenCircleSegment pg = (PTOpenCircleSegment) go;
    // add change points(nodes)
    EditPoint[] result = new EditPoint[7];
    int x = pg.getCenter().x, rx = pg.getRadius();
    int y = pg.getCenter().y, ry = pg.getRadius();
    int nr = 0;
    result[nr++] = new EditPoint(2, new Point(x + rx, y + ry));
    result[nr++] = new EditPoint(3, pg.getPointAtLength(pg.getStartAngle()));
    result[nr++] = new EditPoint(4, pg.getPointAtLength(pg.getStartAngle()
        + pg.getTotalAngle()));

    result[nr++] = new EditPoint(-1, new Point(x - rx, y));
    result[nr++] = new EditPoint(-2, new Point(x, y + ry));
    result[nr++] = new EditPoint(-3, new Point(x + rx, y));
    result[nr++] = new EditPoint(-4, new Point(x, y - ry));

    return result;
  } // getEditPoints

  public EditableObject createObject() {
    PTOpenCircleSegment pg = new PTOpenCircleSegment();
    storeAttributesInto(pg);
    return pg;
  }

  public Editor getSecondaryEditor(EditableObject go) {
    OpenCircleSegmentEditor result = new OpenCircleSegmentEditor();
    // important! result must be of type CircleEditor (or cast)
    // and the parameter passed must be of type PTOpenCircleSegment.
    // Otherwise, not all attributes are copied!
    result.extractAttributesFrom(go);
    return result;
  }

  public String getBasicType() {
    return PTOpenCircleSegment.OPEN_CIRCLE_SEGMENT_TYPE;
  }
} // OpenCircleSegmentEditor
