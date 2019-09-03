package animal.misc;

import java.awt.Point;

/**
 * <code>EditPoint</code> is a point by which a <code>GraphicObject</code>
 * can be edited.
 * It consists of the point and its number.
 * There are two types of <code>EditPoint</code>s: MovePoints and ChangePoints.
 * MovePoints just translate the object, ChangePoints change its corners,
 * radius etc.
 *
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 24.08.1998
 */
public class EditPoint
{
  // being an auxiliary class, the fields are public.

  /** the number of the EditPoint. Negative numbers indicate MovePoints,
   * positive indicate ChangePoints.
   * Several EditPoints in an EditPoint array may have the same number,
   * for example, it doesn't matter where to change a Circle's radius, so
   * all "corners" have the same number, dragging them resulting in the
   * same action. */
  public int num;

  /** the actual point. */
  public Point p;

  /**
   * guess what it does.
   */
  public EditPoint(int aNum, Point aPoint)
  {
    num = aNum;
    p = aPoint;
  }
}
