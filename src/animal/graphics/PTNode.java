/*
 * PTStringArray.java
 * The class for a PTStringArray.
 *
 * Created on 27. November 2005, 0:12
 *
 * @author Michael Schmitt
 * @version 0.0.1
 * @date 2005-12-05
 */

package animal.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.meta.ImmediateTextContainer;
import animal.main.Animal;
import animal.misc.XProperties;

public class PTNode extends PTGraphicObject implements ImmediateTextContainer {

  // =========
  // CONSTANTS
  // =========
  public static final String TYPE_LABEL = "Node";

  // public static final String FONT = TYPE_LABEL + ".Font";
  //
  // public static final String FONT_NAME = TYPE_LABEL + ".fontName";
  //
  // public static final String FONT_SIZE = TYPE_LABEL + ".fontSize";
  //
  // public static final String FONT_COLOR = TYPE_LABEL + ".fontColor";
  //
  // public static final String OUTLINE_COLOR = TYPE_LABEL + ".outlineColor";
  //
  // public static final String BG_COLOR = TYPE_LABEL + ".bgColor";

  // =================
  // STATIC ATTRIBUTES
  // =================

  // public static XProperties DefaultProperties;

  private Font myFont; // = new Font ("Monospaced", Font.PLAIN, 14);

  // private Color bgColor = Color.white;
  //
  // private Color fontColor = Color.black;
  //
  // private Color outlineColor = Color.black;

  private FontMetrics fm;

  /**
   * The actual StringArray to store the values.
   */
  private PTText entry;

  /**
   * The node itself, without the entered value
   */
  private PTArc node;

  /**
   * List of connected nodes and objects
   */
  private int[] connections;

  // ============
  // CONSTRUCTORS
  // ============

  /**
   * Default constructor
   */
  public PTNode() {
    // initialize with default attributes
    initializeWithDefaults(getType());
    // if (DefaultProperties == null)
    // initializeDefaultProperties(AnimalConfiguration.getDefaultConfiguration()
    // .getProperties());
    // setProperties((XProperties) DefaultProperties.clone());
    // myFont = getProperties().getFontProperty(
    // mapKey(FONT),
    // new Font(getProperties().getProperty(mapKey(FONT_NAME), "Monospaced"),
    // Font.PLAIN, getProperties().getIntProperty(mapKey(FONT_SIZE), 14)));
    // fm = Animal.getConcreteFontMetrics(myFont);
  }

  /**
   * Constructs a PTStringArray corresponding to the parsed String.
   * 
   * @param s
   *          the value to be set in this node
   */
  public PTNode(String s) {
    this();
    enterValue(s);
  }

  //
  // /**
  // * Constructs a PTStringArray depending on the settings in the properties
  // file
  // *
  // * @param props
  // * the XProperties to be used
  // */
  // public PTNode(XProperties props) {
  // setProperties(props);
  // myFont = getProperties().getFontProperty(
  // mapKey(FONT),
  // new Font(getProperties().getProperty(mapKey(FONT_NAME), "Monospaced"),
  // Font.PLAIN, getProperties().getIntProperty(mapKey(FONT_SIZE), 14)));
  // fm = Animal.getConcreteFontMetrics(myFont);
  // }

  // ===============
  // PROPERTY ACCESS
  // ===============

  public void setDepth(int newDepth) {
    int theDepth = (newDepth < 2) ? 2 : newDepth;
    super.setDepth(theDepth);
    if (entry != null) {
      entry.setDepth(theDepth - 2);
      node.setDepth(theDepth);
    }
  }

  /**
   * Defines objects of this class as StringArray.
   */
  public String[] handledKeywords() {
    return new String[] { "Node" };
  }

  // /**
  // * Set the default properties of the newly created PTStringArray
  // *
  // * @param prop
  // * the properties file being used
  // */
  // public static void initializeDefaultProperties(XProperties prop) {
  // DefaultProperties = extractDefaultProperties(prop, "Node");
  // }

  /**
   * Set color of the cell background.
   * 
   * @param c
   *          a color value
   */
  public void setBGColor(Color c) {
    // getProperties().put(mapKey(BG_COLOR), c);
    node.setFillColor(c);
  }

  /**
   * Set the font of the cell text.
   * 
   * @param f
   *          the font to set
   */
  public void setFont(Font f) {
    // getProperties().put(reverseMapKey(FONT), f);
    myFont = f;
    int textOffset = -fm.getAscent();
    int heightOffset = -fm.getAscent() - fm.getDescent();
    fm = Animal.getConcreteFontMetrics(f);
    textOffset = textOffset + fm.getAscent();
    heightOffset = heightOffset + fm.getAscent() + fm.getDescent();

    // int widthOffset = 0;
    // int prevOffset;
    entry.setFont(f);
  }

  /**
   * Retrieve the currently used font
   */
  public Font getFont() {
    if (myFont == null)
      myFont = getEntry().getFont();
    return myFont;
    // return getProperties().getFontProperty(mapKey(FONT), myFont);
  }

  /**
   * Retrieve the background color of the array cells.
   */
  public Color getBGColor() {
    return getNode().getFillColor();
    // return bgColor;
    // return getProperties().getColorProperty(mapKey(BG_COLOR), Color.white);
  }

  public PTText getEntry() {
    if (entry == null)
      entry = new PTText(".", new Point(10, 10), getFont());
    return entry;
  }

  /**
   * Set the text to the given color.
   * 
   * @param c
   *          the requested color
   */
  public void setFontColor(Color c) {
    // // fontColor = c;
    // getProperties().put(mapKey(FONT_COLOR), c);
    entry.setColor(c);
  }

  /**
   * Retrieve the color of the text.
   */
  public Color getFontColor() {
    // return fontColor;
    return getEntry().getColor();
    // return getProperties().getColorProperty(mapKey(FONT_COLOR), Color.black);
  }

  public PTArc getNode() {
    if (node == null)
      node = new PTArc();
    return node;
  }

  /**
   * Set the outline color to the given one.
   * 
   * @param c
   *          the wanted color
   */
  public void setOutlineColor(Color c) {
    // highlightColor = c;
    // getProperties().put(mapKey(OUTLINE_COLOR), c);
    node.setColor(c);
  }

  /**
   * Return the color of the cell outlines.
   */
  public Color getOutlineColor() {
    return getNode().getColor();
    // return highlightColor;
    // return getProperties()
    // .getColorProperty(mapKey(OUTLINE_COLOR), Color.yellow);
  }

  /**
   * Store the given String in the specified cell of the array. The cell size is
   * automatically adapted to the new content.
   * 
   * @param val
   *          the value that's to be stored
   */
  public void enterValue(String val) {
    entry.setText(val);
  }

  /**
   * Retrieve the content of an array cell.
   * 
   */
  public String getValue() {
    return entry.getText();
  }

  /**
   * Translate the node by the differences provided
   * 
   * @param dx
   *          the translation in x-direction
   * @param dy
   *          the translation in y-direction
   */
  public void translate(int dx, int dy) {
    // do nothing
  }

  /**
   * The bounding box for the whole PTStringArray, specified by the the top left
   * point, width and height.
   */
  public java.awt.Rectangle getBoundingBox() {
    return new Rectangle(0, 0, 10, 10);
  }

  /**
   * Get the middle under the bottom of the i-th array cell as the point where
   * an arrow has to point for marking that cell.
   * 
   * @param i
   *          the i-th cell of the array
   */
  public Point getArrowPoint(int i) {
    // if (i == 0) {
    // return new Point (origin.x + entryPos[0] / 2, origin.y + fm.getAscent ()
    // + fm.getDescent () + 15);
    // } else if ((i > 0) && (i < entry.length)) {
    // return new Point (origin.x + entryPos[i-1] + (entryPos[i] -
    // entryPos[i-1])/2, origin.y + fm.getAscent () + fm.getDescent () + 15);
    // } else {
    // return new Point (-1, -1);
    // }
    return new Point(-1, -1);
  }

  /**
   * Return the object type as "Node".
   */
  public String getType() {
    return TYPE_LABEL;
  }

//  /**
//   * Clone method to create a clone from a PTStringArray object
//   */
//  public Object clone() {
//    PTNode newNode = (PTNode) super.clone();
//    // array.entry = new PTText [entry.length];
//    // array.cells = new PTPolyline [cells.length];
//    // array.entryPos = new int [entryPos.length];
//    // array.origin = (Point) origin.clone ();
//    // for (int i = 0; i < entry.length; i++) {
//    // array.entryPos [i] = entryPos [i];
//    // array.entry[i] = (PTText) entry [i].clone ();
//    // array.cells[i] = (PTPolyline) cells [i].clone ();
//    // }
//    return newNode;
//  }

  /**
   * Clones the current graphical object. Note that the method will per
   * definition return Object, so the result has to be cast to the appropriate
   * type.
   * 
   * @return a clone of the current object, statically typed as Object.
   */
  public Object clone() {
    // create new object
    PTNode targetShape = new PTNode();

    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    return targetShape;
  }

  /**
   * Offers cloning support by cloning or duplicating the shared attributes
   * 
   * @param targetShape the shape into which the values are to be copied. Note
   * the direction -- it is "currentObject.cloneCommonFeaturesInto(newObject)", 
   * not vice versa!
   */
  protected void cloneCommonFeaturesInto(PTNode targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    super.cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    targetShape.setText(new String(getText()));
    targetShape.setFont(new Font(getFont().getFamily(),
        getFont().getStyle(), getFont().getSize()));
    targetShape.node = (PTArc)node.clone();
  }
  
  /**
   * Converts the PTStringArray into a String
   */
  public String toString() {
    String toString = new String();
    toString = getType();
    if (getObjectName() != null)
      toString.concat(" '" + getObjectName() + "'");
    // if (isConnectedToOtherNode?)
    toString.concat(" connected to");
    for (int c = 0; c < connections.length; c++) {
      toString.concat(" " + connections[c]);
      if (c < connections.length - 2) {
        toString.concat(",");
      } else if (c == connections.length - 2) {
        toString.concat(" and");
      }
    }
    return toString;
  }

  /**
   * file version history 1: basic format
   */
  public int getFileVersion() {
    return 1;
  }

  // ================
  // INTERNAL METHODS
  // ================

  // /**
  // * Initialize the default settings
  // */
  // private void init() {
  // }

  /**
   * Update the edges to other nodes and elements
   */
  private void updateEdges() {
    // do nothing
  }

  // ================
  // GRAPHICAL OUTPUT
  // ================

  /**
   * The paint method in which the cells an the contents are drawn on the
   * screen.
   */
  public void paint(Graphics g) {
    updateEdges();
    // int height = fm.getAscent();
    node.paint(g);
    entry.paint(g);
  }

  public String getText() {
    return getValue();
  }

  public void setText(String targetText) {
    enterValue(targetText);
  }

  /**
   * Update the default properties for this object
   * 
   * @param defaultProperties
   *          the properties to be updated
   */
  public void updateDefaults(XProperties defaultProperties) {
    super.updateDefaults(defaultProperties);
    Font f = getFont();
    defaultProperties.put(getType() + ".bold", f.isBold());
    defaultProperties.put(getType() + ".font", f);
    defaultProperties.put(getType() + ".fontName", f.getFamily());
    defaultProperties.put(getType() + ".fontSize", f.getSize());
    defaultProperties.put(getType() + ".italic", f.isItalic());
    defaultProperties.put(getType() + ".text", getText());
  }

}