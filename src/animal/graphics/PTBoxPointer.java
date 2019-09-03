package animal.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import animal.graphics.meta.ImmediateTextContainer;
import animal.main.Animal;
import animal.misc.XProperties;

/**
 * PTBoxPointer is a BoxPointer used for example in linked lists. It consists of
 * the Box containing a text and the pointer, pointing to another object(or to
 * null). The pointer can start either at the right or at the left side of the
 * BoxPointer.
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.2 2007-10-19
 */
public class PTBoxPointer extends PTGraphicObject implements
    ImmediateTextContainer {

  /**
   * the type label for this object, used e.g. for access properties
   */
  public static final String TYPE_LABEL = "BoxPointer";

  /**
   * the serialization UID for this object
   */
//  private static final long serialVersionUID = 6082233834117817710L;

  /**
   * The standard minimal size of a BoxPointer
   */
  private static final Dimension MINIMUM_SIZE = new Dimension(40, 20);

  /**
   * The amount of space to be left between the text and the box
   */
  private static final Point PADDING = new Point(5, 2);

  /**
   * The padding between the pointers' origins, pointers and pointerArea
   */
  private static final int POINTER_PADDING = 10;

  /**
   * Constant for pointerPosition <em>bottom</em>
   */
  public static final int POINTER_POSITION_BOTTOM = 4;

  /**
   * Constant for pointerPosition <em>left</em>
   */
  public static final int POINTER_POSITION_LEFT = 2;

  /**
   * Constant for pointerPosition <em>none</em>
   */
  public static final int POINTER_POSITION_NONE = 0;

  /**
   * Constant for pointerPosition <em>right</em>
   */
  public static final int POINTER_POSITION_RIGHT = 1;

  /**
   * Constant for pointerPosition <em>top</em>
   */
  public static final int POINTER_POSITION_TOP = 3;

  // =================================================================
  // ATTRIBUTES
  // =================================================================

  /**
   * The box containing the pointers
   */
  protected PTRectangle pointerArea;

  /**
   * The position of the pointers. Must be one of the constants
   * POINTER_POSITION_TOP, POINTER_POSITION_LEFT, POINTER_POSITION_RIGHT,
   * POINTER_POSITION_BOTTOM, POINTER_POSITION_NONE. Default:
   * POINTER_POSITION_BOTTOM
   */
  protected int pointerPosition = POINTER_POSITION_BOTTOM;

  /**
   * The array of pointers
   */
  protected Vector<PTLine> pointers = null;

  /**
   * the position for this component
   */
  protected Point position = new Point(0, 0);

  /**
   * The text box containing the text
   */
  protected PTRectangle textBox;

  /**
   * The actual text component
   */
  protected PTText textComponent;

  // =================================================================
  // CONSTRUCTORS
  // =================================================================

  /**
   * Constructs an invisible BoxPointer. It remains invisible until
   * <code>setLocation</code> is called.
   */
  public PTBoxPointer() {
    // initialize attributes to their default values
    initializeWithDefaults(getType());
  }

  /**
   * constructs a BoxPointer at location(x,y)
   * 
   * @param x
   *          the x position of the pointer's top left corner
   * @param y
   *          the y position of the pointer's top left corner
   */
  public PTBoxPointer(int x, int y) {
    this(x, y, "");
  }

  /**
   * constructs a BoxPointer with the given text at location(x,y)
   * 
   * @param x
   *          the x position of the pointer's top left corner
   * @param y
   *          the y position of the pointer's top left corner
   * @param text
   *          the text to be placed inside the pointer
   */
  public PTBoxPointer(int x, int y, String text) {
    initializeWithDefaults(getType());
    setText(text);
    init(x, y);
  }

  public void initializeBoxPointer() {
     if (getPointerCount() == 0)
      makeNullPointer();
    if (getPosition() != null)
      initTextBox(position.x, position.y, 20, 10);
    initTextComponent();
    init(position);
    setPointerCount(getPointerCount());
  }

  // =================================================================
  // INITIALIZATION
  // =================================================================

  /**
   * reinitializes the BoxPointer.
   */
  public void init() {
    init(getOrigin());
  }

  /**
   * (re-)initializes the BoxPointer by calculating the size required for the
   * text, setting the box and the pointer.
   * 
   * @param p
   *          the position of the pointer's top left corner
   */
  void init(Point p) {
    if (p == null)
      init(10, 10);
    else
      init(p.x, p.y);
  }

  /**
   * (re-)initializes the BoxPointer by calculating the size required for the
   * text, setting the box and the pointer.
   * 
   * @param x
   *          the x position of the pointer's top left corner
   * @param y
   *          the y position of the pointer's top left corner
   */
  void init(int x, int y) {
    // who knocks on this door?
    if (position == null)
      position = new Point(x, y);
    else
      position.setLocation(x, y);
    /*
     * order of the Polygon's points: 0----3 | | 1----2
     */
    FontMetrics f = Animal.getConcreteFontMetrics(getTextComponent().getFont());
    // Toolkit.getDefaultToolkit().getFontMetrics(getFont());
    // calculate the width of the box. This is the space the text
    // needs(including padding) plus the space for the pointer
    // origin
    int width = PADDING.x * 2;
    if (getTextComponent() != null) {
      String text = textComponent.getText();
      width += Animal.getStringWidth(text, textComponent.getFont());
    }
    if (width < MINIMUM_SIZE.width - 20)// but at least it's the minimal size
      width = MINIMUM_SIZE.width - 20;
    int right = x + width - 1; // TODO test
    int bottom = y + MINIMUM_SIZE.height + f.getHeight() - 15;
    int pointerSize = (getPointerCount() + 1) * POINTER_PADDING;

    // put pointerArea at correct position
    switch (getPointerPosition()) {
    case POINTER_POSITION_TOP:
      // pointerArea = makeRectangle(x, y - pointerSize, right, y);
      initPointerArea(x, y - pointerSize, right, y);
      break;
    case POINTER_POSITION_BOTTOM:
      // pointerArea = makeRectangle(x, bottom, right, bottom + pointerSize);
      initPointerArea(x, bottom, right, bottom + pointerSize);
      break;
    case POINTER_POSITION_LEFT:
      // pointerArea = makeRectangle(x - pointerSize, y, x, bottom);
      initPointerArea(x - pointerSize, y, x, bottom);
      break;
    case POINTER_POSITION_RIGHT:
      // pointerArea = makeRectangle(right, y, right + pointerSize, bottom);
      initPointerArea(right, y, right + pointerSize, bottom);
      break;
    default:
      pointerArea = null;
    }
    initTextBox(x, y, width, MINIMUM_SIZE.height + f.getHeight() - 15);
    textComponent.setLocation(new Point(x, y));
    // finally(re-)construct the pointers
    if (pointerArea != null) {
      recalcPointerOrigins();
    }
  }

  /**
   * This method initializes the primitive with the primitive type's default
   * values (looked up at the default properties)
   * 
   * @param primitiveName
   *          the name of the primitive to support inheritance, e.g. "Circle".
   */
  public void initializeWithDefaults(String primitiveName) {
    super.initializeWithDefaults(primitiveName);
    // pointerArea = new PTRectangle(10, 10, 10, 10);
    // pointerArea.initializeWithDefaults(getType() + "PointerArea");
    // fillColor = config.getDefaultColor(primitiveName, "fillColor",
    // Color.BLACK);
  }

  // =================================================================
  // GRAPHIC OPERATIONS
  // =================================================================

  /**
   * calculates the origin of the pointer given. Pointers are numbered from
   * inside to outside, i.e. the pointer next to the textbox is #0.
   * 
   * @param pointerNum
   *          the number of the pointer for calculation purposes
   */
  Point calcPointerOrigin(int pointerNum) {
    Rectangle textBoxBounds = textBox.getBoundingBox();
    int left = textBoxBounds.x;
    int right = textBoxBounds.x + textBoxBounds.width;
    int top = textBoxBounds.y;
    int bottom = textBoxBounds.y + textBoxBounds.height;
    int space = (pointerNum + 1) * POINTER_PADDING;
    switch (getPointerPosition()) {
    case POINTER_POSITION_TOP:
      return new Point((left + right) / 2, top - space);
    case POINTER_POSITION_BOTTOM:
      return new Point((left + right) / 2, bottom + space);
    case POINTER_POSITION_LEFT:
      return new Point(left - space, (top + bottom) / 2);
    case POINTER_POSITION_RIGHT:
      return new Point(right + space, (top + bottom) / 2);
    default:
      return new Point(0, 0);
    }
  }

  /**
   * sets the pointer to null, usually to a place somewhat below its origin.
   */
  public void makeNullPointer() {
    int count = getPointerCount();
    pointers = null;
    setPointerCount(count);
  }

  /**
   * returns a PTPolyline with the given specification
   * 
   * Used for generating the pointer area.
   * 
   * @param left
   *          the x coordinate of the left rectangle boundary
   * @param top
   *          the y coordinate of the top rectangle boundary
   * @param right
   *          the x coordinate of the right rectangle boundary
   * @param bottom
   *          the y coordinate of the bottom rectangle boundary
   */
  PTPolyline makeRectangle(int left, int top, int right, int bottom) {
    return new PTPolyline(new int[] { left, left, right, right, left },
        new int[] { top, bottom, bottom, top, top });
  }

  /**
   * recalculates the pointer origins after updates
   */
  public void recalcPointerOrigins() {
    if (pointers != null) {
    	int nrPointers = pointers.size();
      for (int a = 0; a < nrPointers; a++)
        pointers.get(a).setNode(0, new PTPoint(calcPointerOrigin(a)));
    }
  }

  // =================================================================
  // ATTRIBUTE GET/SET
  // =================================================================

  /**
   * returns the text box
   * 
   * @return the rectangle containing the text entry
   */
  public PTRectangle getTextBox() { // was: getBox()
    if (textBox == null)
      initTextBox(10, 10, 10, 10);
    return textBox;
  }

  /**
   * returns the BoxPointer's bounding box, i.e. the smallest rectangle that
   * contains the box and the pointer.
   * 
   * @return the bounding box of the entire object
   */
  public Rectangle getBoundingBox() {
    Rectangle r = textBox.getBoundingBox();
    if (pointerArea != null) {
      Rectangle r2 = pointerArea.getBoundingBox();
      r = SwingUtilities.computeUnion(r2.x, r2.y, r2.width, r2.height, r);

      if (pointers != null) {
      	int nrPointers = pointers.size();
        for (int i = 0; i < nrPointers; i++) {
          Point tip = getTip(i);
          r = SwingUtilities.computeUnion(tip.x, tip.y, 0, 0, r);
        }
      }
    }
    return r;
  }

  /**
   * returns the BoxPointer's bounding box, i.e. the smallest rectangle that
   * contains the box and the pointer.
   * 
   * @return the bounding box rectangle when ignoring the pointers.
   */
  public Rectangle getBoundingBoxWithoutPointers() {
    Rectangle r = textBox.getBoundingBox();
    if (pointerArea != null) {
      Rectangle r2 = pointerArea.getBoundingBox();
      r = SwingUtilities.computeUnion(r2.x, r2.y, r2.width, r2.height, r);
    }
    return r;
  }

  /**
   * This constant is used for the file version. It must be incremented whenever
   * the save format is changed.
   * 
   * Versions include:
   * 
   * <ol>
   * <li>basic version(box, pointer, text and color)</li>
   * <li>added "pointerOnLeft"-attribute</li>
   * <li>n pointers, on arbitrary side, fillColors</li>
   * <li>text color set separately</li>
   * <li>object depth, text, font, style and size</li>
   * <li>new save format, needs less node information</li>
   * </ol>
   * 
   * @return the file version
   */
  public int getFileVersion() {
    return 6;
  }

  public Color getFillColor() {
    if (textBox != null)
      return textBox.getFillColor();
    return Color.BLACK;
   }

  /**
   * return's the text box's point of origin
   * 
   * @return the top left corner of the text box
   */
  public Point getOrigin() {
    if (textBox == null)
      return new Point(0, 0);
    // Nodes are PTPoints, so cast them to Point
    return textBox.getStartNode();
  }

  /**
   * returns the rectangle containing the pointers
   * 
   * @return the pointer area as a rectangle
   */
  public PTRectangle getPointerArea() {
    if (pointerArea == null)
      // TODO: is the change from init() to initPointerArea() OK?
      // initPointerArea(left, top, right, bottom);
      init();
    return pointerArea;
  }

  /**
   * returns the number of pointers, 0 if none.
   * 
   * @return the number of pointers. If the pointer array is empty, 0 will be
   *         returned instead.
   */
  public int getPointerCount() {
    return (pointers == null) ? 0 : pointers.size();
  }

  /**
   * returns the pointer's origin.
   * 
   * @param pointerNum
   *          the number of the pointer used in the calculation
   */
  public Point getPointerOrigin(int pointerNum) {
    // if pointer already exists, return its origin without calculating
  	int nrPointers = (pointers != null) ? pointers.size() : -1;
    if (pointerNum < nrPointers && pointerNum >= 0) {
    	PTLine line = pointers.get(pointerNum);
    	if (line != null)
    		return line.getNodeAsPoint(0);
    }
    return calcPointerOrigin(pointerNum);
  }

  /**
   * returns the position of the pointers
   * 
   * @return the pointer position, will be POINTER_POSITION_LEFT,
   *         POINTER_POSITION_RIGHT, POINTER_POSITION_TOP,
   *         POINTER_POSITION_BOTTOM, or POINTER_POSITION_NONE
   */
  public int getPointerPosition() {
    if (!validPosition(pointerPosition))
      pointerPosition = POINTER_POSITION_BOTTOM;
    return pointerPosition;
   }

  protected boolean validPosition(int targetPosition) {
    switch (targetPosition) {
    case POINTER_POSITION_BOTTOM:
    case POINTER_POSITION_LEFT:
    case POINTER_POSITION_NONE:
    case POINTER_POSITION_RIGHT:
    case POINTER_POSITION_TOP:
      return true;
    default:
      return false;
    }
  }

  /**
   * returns the rectangle containing the pointers
   * 
   * @return the pointer area as a rectangle
   */
  public PTLine getPointer(int pos) {
    if (pos >= 0 && pointers != null && pos < pointers.size()) {
    	PTLine line = pointers.get(pos);
    	if (line != null)
    		return line;
    }
    return new PTLine();
  }

  /**
   * returns the rectangle containing the pointers
   * 
   * @return the pointer area as a Vector of PTLine instances
   */
  public Vector<PTLine> getPointers() {
    return pointers;
  }

  public Point getPosition() {
    if (position == null) {
      if (textBox != null)
        position = textBox.getBoundingBox().getLocation();
      else
        position = new Point(10, 10);
    }
    return position;
  }

  /**
   * returns the minimal size of a BoxPointer.
   * 
   * @return the minimal size of a BoxPointer
   */
  public Dimension getSize() {
    return MINIMUM_SIZE;
  }

  /**
   * returns the encapsulated text object
   * 
   * @return the encapsulated text component
   */
  public PTText getTextComponent() {
    if (textComponent == null)
      initTextComponent();
    return textComponent;
  }

  /**
   * returns the pointer's tip, if it exists
   * 
   * @param pointerNum
   *          the number of the pointer
   */
  public Point getTip(int pointerNum) {
    if (pointers == null || pointerNum < 0 || pointerNum >= pointers.size())
      return null;

    return pointers.get(pointerNum).getNodeAsPoint(1);
  }

  /**
   * returns the pointer's tip, if it exists.
   * 
   * @param pointerNum
   *          the number of the pointer
   */
  public Point getTipOrigin(int pointerNum) {
    if (pointers == null || pointerNum < 0 || pointerNum >= pointers.size())
      return null;

    return pointers.get(pointerNum).getNodeAsPoint(0);
  }

  /**
   * returns the type of this object
   * 
   * @return the type of this object
   */
  public String getType() {
    return TYPE_LABEL;
  }

  /**
   * Returns the names of the structures this object can parse.
   * 
   * @return an array of Strings containing all handled keywords in the stream
   */
  public String[] handledKeywords() {
    return new String[] { "BoxPointer", "ListElement" };
  }

  protected void initPointerArea(int left, int top, int right, int bottom) {
    // if (getPointerCount() > 0) {
    if (pointerArea == null) {
      pointerArea = new PTRectangle(left, top, right - left + 1, bottom - top
          + 1);
      pointerArea.setFilled(true);
      pointerArea.initializeWithDefaults(getType() + ".PointerArea");
    } else {
      pointerArea.setStartNode(left, top);
      pointerArea.setWidth(right - left + 1);
      pointerArea.setFilled(true);
      pointerArea.setHeight(bottom - top + 1);
    }
  }

  public void initTextBox(int x, int y, int width, int height) {
    if (textBox == null) {
      textBox = new PTRectangle(x, y, width, height);
      textBox.initializeWithDefaults(getType() + ".TextBox");
    } else {
      textBox.setStartNode(x, y);
      textBox.setWidth(width);
      textBox.setHeight(height);
    }
    textBox.setFilled(true);
  }

  protected void initTextComponent() {
    textComponent = new PTText(); // was "(getProperties())"
    // TODO correct?
    textComponent.setLocation(getPosition());
    textComponent.initializeWithDefaults(getType() + ".Text");
    textComponent.setText("");
  }

  /**
   * returns whether the pointer with the given index is a null pointer.
   * 
   * @param i
   *          the number of the pointer
   * @return true if the given pointer points to null, else false.
   */
  private boolean isNullPointer(int i) {
    Point basePoint = getPointerOrigin(i);
    if (getPointerPosition() == POINTER_POSITION_TOP
        || getPointerPosition() == POINTER_POSITION_BOTTOM)
      basePoint.translate((i % 2 == 0) ? 30 : -30, 0);
    else
      basePoint.translate(0, (i % 2 == 0) ? 30 : -30);
    return basePoint.equals(getTip(i));
  }

  /**
   * sets the color shared by text box and pointers
   * 
   * @param newColor
   *          the color to be used
   */
  public void setColor(Color newColor) {
    super.setColor(newColor);
    // also serves as the color for pointers!
    if (pointers != null)
      for (PTLine line : pointers)
      	if (line != null)
      		line.setColor(newColor);
  }

  /**
   * sets the BoxPointer's location(its upper left corner). The pointer is set
   * to null.
   */
  public void setPosition(Point p) {
    init(p);
    makeNullPointer();
  }
  /**
   * sets the number of pointers to <i>pointerCount</i>. Old pointers with a
   * higher index are deleted, if new pointers have to be appended, they are
   * created as "null-pointers" (which is not currently determined correct in
   * "isNullPointer"!
   */
  public void setPointerCountWithoutReinitialization(int pointerCount) {
    if (pointerCount == 0) {
      pointers = null;
      pointerArea = null;
    } else {
      pointers = new Vector<PTLine>(pointerCount);
    }
  }

  /**
   * sets the number of pointers to <i>pointerCount</i>. Old pointers with a
   * higher index are deleted, if new pointers have to be appended, they are
   * created as "null-pointers" (which is not currently determined correct in
   * "isNullPointer"!
   */
  public void setPointerCount(int pointerCount) {
    if (pointerCount == 0) {
      pointers = null;
      pointerArea = null;
    } else {
    	Vector<PTLine> oldPointers = pointers;
      pointers = new Vector<PTLine>(pointerCount);
      // copy old pointers, if possible
      if (oldPointers != null)
        for (int a = 0; a < Math.min(oldPointers.size(), pointerCount); a++)
          pointers.add(oldPointers.get(a));
      // now create the new pointers, if necessary
      for (int a = (oldPointers != null ? oldPointers.size() : 0); a < pointerCount; a++) {
        Point p = calcPointerOrigin(a);
        int x0 = p.x, y0 = p.y, x1 = p.x, y1 = p.y;
        int dx = p.x - getBoundingBoxWithoutPointers().x;
        int dy = p.y - getBoundingBoxWithoutPointers().y;
        // on top and bottom, the pointers change from left to right...
        if (getPointerPosition() == POINTER_POSITION_TOP
            || getPointerPosition() == POINTER_POSITION_BOTTOM) {
        	x1 += ((a % 2 == 0) ? dx: -dx);
        	//          px = new int[] { p.x, p.x + (a % 2 == 0 ? dx : -dx) };
        	//      	py = new int[] { p.y, p.y };
        } else {
          // left and right they change from up to down.
        	y1 += ((a % 2 == 0) ? dy : -dy);
        	//          px = new int[] { p.x, p.x };
//          py = new int[] { p.y, p.y + (a % 2 == 0 ? dy : -dy) };
        }
        PTLine currentPointer = new PTLine(x0, y0, x1, y1);
        currentPointer.setObjectName(getObjectName() + ".pointer_" + a);
        currentPointer.setFWArrow(true);
        currentPointer.setBWArrow(false);
        currentPointer.setColor(getColor());
        pointers.add(currentPointer);
      }
    }
    init(); // recalculate the pointerArea's size
  }

  /**
   * sets the target placement for the pointer area. Must be one of the valid
   * value POINTER_POSITION_TOP, POINTER_POSITION_LEFT, POINTER_POSITION_RIGHT,
   * POINTER_POSITION_BOTTOM, POINTER_POSITION_NONE. Also re-initializes the
   * display.
   * 
   * @param targetPosition
   */
  public void setPointerPosition(int targetPosition) {
    if (validPosition(targetPosition)) {
      pointerPosition = targetPosition;
      init();
    }
  }

  /**
   * sets the target placement for the pointer area. Must be one of the valid
   * value POINTER_POSITION_TOP, POINTER_POSITION_LEFT, POINTER_POSITION_RIGHT,
   * POINTER_POSITION_BOTTOM, POINTER_POSITION_NONE.
   * 
   * This method will <em>not</em> re-initialize the display.
   * 
   * @param targetPosition
   */
  public void setPointerPositionWithoutReinitizalization(int targetPosition) {
    if (validPosition(targetPosition))
      pointerPosition = targetPosition;
  }

  /**
   * sets the tip of the Pointer. If both <i>tipX</i> and <i>tipY</i> are 0, a
   * null pointer is generated.
   */
  public void setTip(int pointerNum, int tipX, int tipY) {
    int theTipX = tipX, theTipY = tipY;
    Point p = getPointerOrigin(pointerNum);
    if (theTipX == 0 && theTipY == 0) {
      // null pointer points to a place a little vertically below
      // the pointer's origin
      theTipX = p.x;
      theTipY = p.y + MINIMUM_SIZE.height + MINIMUM_SIZE.height / 2;
    }
    // if a pointer already existed, just reset its tip
    if (pointers != null && pointerNum < pointers.size()) {
    	PTLine selectedPointer = pointers.get(pointerNum);
      if (selectedPointer != null) {
      	selectedPointer.setNode(1, new PTPoint(theTipX, theTipY, 1));
      } else {
      	selectedPointer = new PTLine(p.x, p.y, theTipX, theTipY);
//            new Point(tipX, tipY) });
      	selectedPointer.setFWArrow(true);
      	pointers.set(pointerNum, selectedPointer);
      }
      return;
    }
    // else make a new Polyline
    // TODO Check if this is OK, seems to be ineffective!
    // int[] px = new int[]{p.x, p.x + tipX};
    // int[] py = new int[]{p.y, p.y + tipY};
    // END GR
  }

  /**
   * resets the pointer's tip.
   */
  public void setTip(int pointerNum, Point p) {
    setTip(pointerNum, p.x, p.y);
  }

  public void setTipRelative(int pointerNum, int tipX, int tipY) {
    int theTipX = tipX, theTipY = tipY;
    Point p = getPointerOrigin(pointerNum);
    if (theTipX == 0 && theTipY == 0) {
      /*
       * null pointer points to a place a little vertically below the pointer's
       * origin
       */
      theTipX = p.x;
      theTipY = p.y + MINIMUM_SIZE.height + MINIMUM_SIZE.height / 2;
    }
    // if a pointer already existed, just reset its tip
    if (pointers != null && pointerNum < pointers.size()) {
    	PTLine currentPointer = pointers.get(pointerNum);
    	if (currentPointer != null)
    		currentPointer.setNode(1, new PTPoint(p.x + theTipX, p.y + theTipY, 1));
      return;
    }
    // else make a new Polyline
    // TODO Check if this is OK, seems to be ineffective!
    // int[] px = new int[]{p.x, p.x + tipX};
    // int[] py = new int[]{p.y, p.y + tipY};
    // END GR
  }

  public void setTipRelative(int pointerNum, Point p) {
    Point orig = getPointerOrigin(pointerNum);
    setTip(pointerNum, orig.x + p.x, orig.y + p.y);
  }

  public void setTips(boolean[] nodeMap, int tipX, int tipY) {
    // do nothing
  }

  /*****************************************************************************
   * the paint method
   ****************************************************************************/

  /**
   * paints the BoxPointer into the given Graphic context.
   */
  public void paint(Graphics g) {
    // don't paint if both x and y are set to -1. This is done, as
    // otherwise even before the first click a BoxPointer would be
    // visible as it has a minimal size(which does not hold for a
    // Polyline, e.g.)
    if (getOrigin().x == -1 && getOrigin().y == -1)
      return;

    // painting means:
    // 1. painting the text box (without the included text)
    getTextBox().paint(g);

    // 2. drawing the text component
    getTextComponent().paint(g);

    // 3. drawing the pointer area
    if (pointerArea != null)
      pointerArea.paint(g);

    // 4. drawing the pointers
    g.setColor(getColor());
    if (pointerArea != null && pointers != null)
      for (PTLine currentPointer : pointers)
        if (currentPointer!= null)
        	currentPointer.paint(g);
  }

  /*****************************************************************************
   * standard graphics routines (of which PTBoxPointer does not implement many)
   ****************************************************************************/

  /**
   * translates the box and the pointer.
   */
  public void translate(int x, int y) {
    // use Polyline's translate method.
    textBox.translate(x, y);
    textComponent.translate(x, y);
    if (pointerArea != null)
      pointerArea.translate(x, y);
    if (pointers != null)
      for (PTLine currentPointer : pointers)
      	if (currentPointer != null)
      		currentPointer.translate(x, y);
  }

  /**
   * translates the box, but leaves the pointer's tip fixed. only null pointers
   * are moved together with the BP.
   */
  public void translateWithFixedTips(boolean[] staticTips, int x, int y) {
    if (staticTips != null && pointers != null) {
      Point[] points = new Point[getPointerCount()];
      for (int i = 0; i < pointers.size(); i++)
        if (staticTips[i])
          points[i] = getTip(i);
        else
          points[i] = null;
      translateWithFixedTips(points, x, y);
    } else
      translate(x, y);
  }

  /**
   * translates the box, but leaves the pointer's tip fixed. only null pointers
   * are moved together with the BP.
   */
  public void translateWithFixedTips(Point[] tipsToKeep, int x, int y) {
    translate(x, y);
    for (int i = 0; i < pointers.size(); i++)
      if (tipsToKeep[i] != null)
        setTip(i, tipsToKeep[i]);
    // others left "untouched", i.e. translated
  }

  /**
   * translates the box, but leaves the pointer's tip fixed. only null pointers
   * are moved together with the BP.
   */
  public void translateWithFixedTips(int x, int y) {
    if (pointers != null) {
      Point[] points = new Point[getPointerCount()];
      for (int i = 0; i < pointers.size(); i++)
        if (!isNullPointer(i))
          points[i] = getTip(i);
        else
          points[i] = null;
      translateWithFixedTips(points, x, y);
    } else
      translate(x, y);
  }

  /*****************************************************************************
   * other GraphicObject's methods that have to be implemented
   ****************************************************************************/

  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    defaultProperties.put(getType() + ".fillColor", getFillColor());
    Font f = getTextComponent().getFont();
    if (f != null) {
      defaultProperties.put(getType() + ".font", f);
      defaultProperties.put(getType() + ".fontName", f.getFamily());
      defaultProperties.put(getType() + ".fontSize", f.getSize());
      defaultProperties.put(getType() + ".fontStyle", f.getStyle());
    }
    defaultProperties.put(getType() + ".nrPointers", getPointerCount());
    PTRectangle r = getPointerArea();
    if (r != null) {
      defaultProperties.put(getType() + ".pointerAreaColor", r.getColor());
      defaultProperties.put(getType() + ".pointerAreaFillColor", r
          .getFillColor());
    }
    defaultProperties.put(getType() + ".pointerPosition", getPointerPosition());
    PTText t = getTextComponent();
    if (t != null) {
      defaultProperties.put(getType() + ".text", t.getText());
      defaultProperties.put(getType() + ".textColor", t.getColor());
    }
  }

  /**
   * clones the BoxPointer.
   */
  public Object clone() {
    // PTBoxPointer b = (PTBoxPointer) super.clone();
    PTBoxPointer b = new PTBoxPointer();
    b.setNum(getNum(false));
    b.setDepth(getDepth());
    b.setColor(createColor(color));
    b.textBox = (PTRectangle) textBox.clone();
    if (pointerArea != null) {
      b.pointerArea = (PTRectangle) pointerArea.clone();
      b.pointerArea.setFilled(true);
    }
    if (pointers != null) {
    	int nrPointers = pointers.size();
      b.pointers = new Vector<PTLine>(nrPointers); //new PTPolyline[pointers.length];
      for (int pos = 0; pos < nrPointers; pos++)
      	if (pointers.get(pos) != null)
      		b.pointers.add((PTLine)(pointers.get(pos).clone()));
      	else
      		b.pointers.add(null);
//      for (int a = 0; a < pointers.length; a++)
//        b.pointers[a] = (PTPolyline) pointers[a].clone();
    }
    if (textComponent != null)
      b.textComponent = (PTText) textComponent.clone();
    b.setPointerPositionWithoutReinitizalization(pointerPosition);
    return b;
  }

  /**
   * converts the BoxPointer to a string representation.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder(60);
    sb.append("ListElement ");
    if (getObjectName() != null)
      sb.append("\"").append(getObjectName()).append("\" ");
    sb.append(textBox.toString()).append(",");
    if (pointers != null)
      sb.append(pointers.toString());
    sb.append(")");
    return sb.toString();
  }

  /*****************************************************************************
   * communication with the Animators
   ****************************************************************************/

  /**
   * This method will test the operation passed in for needing a node selection.
   * Most operations will not need this, but some require a selector -
   * especially the 'move selected / all but selected nodes' animators.
   * 
   * @param operation
   *          the name of the requested operation
   * @return true if the use of a NodeSelector is required, else false. Note
   *         that PTGraphicObject always returns <code>false</code>.
   */
  public boolean operationRequiresSpecialSelector(String operation) {
    return (operation != null && operation.indexOf("Tips") != -1);
  }

  public String baseOperationName(String methodName) {
    if (methodName.endsWith("..."))
      return methodName.substring(0, methodName.indexOf('.'));
    else if (methodName.indexOf("Tips") != 0)
      return methodName.substring(0, methodName.indexOf(' '));
    else
      return methodName;
  }

  /**
   * This will test the operation passed for allowing multiple node selection.
   * Most operations will not need this, but some require a selector -
   * especially the 'move selected / all but selected nodes' animators.
   * 
   * @param operation
   *          the name of the requested operation
   * @return true if the use of a NodeSelector is required, else false. Note
   *         that PTGraphicObject always returns <code>false</code>.
   */
  public boolean enableMultiSelectionFor(String operation) {
    return (operation != null && operation.indexOf("Tips") != -1);
  }

  public boolean compatibleMethod(String method) {
    return (method != null && method.indexOf("Tips") != -1);
  }

  /*****************************************************************************
   * File I/O
   ****************************************************************************/

  public void discard() {
    super.discard();
    pointerArea.discard();
    for (PTLine localPointer : pointers)
    	if (localPointer != null)
    		localPointer.discard();
    pointers.clear();
//    for (int i = 0; i < pointers.size; i++) {
//      
//    	pointers[i].discard();
//      pointers[i] = null;
//    }
    pointers = null;
    textBox.discard();
    textComponent.discard();
  }

  public Font getFont() {
    return getTextComponent().getFont();
  }

  public String getText() {
    return getTextComponent().getText();
  }

  public void setFont(Font targetFont) {
    getTextComponent().setFont(targetFont);
    init(); // recalc the size
  }

  /**
   * sets the BoxPointer's text.
   * 
   * @param newText
   *          the new text for this object
   */
  public void setText(String newText) {
    getTextComponent().setText(newText);
    init(); // recalc the size
  }
} // PTBoxPointer
