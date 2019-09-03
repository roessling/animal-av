/*
 * PTStringArray.java
 * The class for a PTStringArray.
 *
 * Created on 1. Juni 2005, 14:28
 *
 * @author Michael Schmitt
 * @version 2.0.4
 * @date 2006-01-01
 */

package animal.graphics;

import java.awt.Font;

import animal.graphics.meta.PTArray;

public class PTStringArray extends PTArray {

  // =========
  // CONSTANTS
  // =========

  public static final String STRING_ARRAY_TYPE = "StringArray";

  // ============
  // CONSTRUCTORS
  // ============

  /**
   * Default constructor
   */
  public PTStringArray() {
    // initialize with default attributes
    initializeWithDefaults(getType());
    int targetSize = getSize();
    if (targetSize == 0)
      targetSize = 10;
    init(targetSize);
  }

  /**
   * Constructs a PTStringArray of the specified length. This is the one used by
   * the <code>StringArrayEditor</code>.
   * 
   * @param nrEntries
   *          the number of entries
   */
  public PTStringArray(int nrEntries) {
    this();
    init(nrEntries);
  }

  /**
   * Constructs a PTStringArray corresponding to the parsed StringArray.
   * 
   * @param s
   *          the original StringArray
   */
  public PTStringArray(String[] s) {
    this(s.length);
    for (int i = 0; i < s.length; i++) {
      enterValue(i, s[i]);
    }
  }

  /**
   * Clones the current graphical object. Note that the method will per
   * definition return Object, so the result has to be cast to the appropriate
   * type.
   * 
   * @return a clone of the current object, statically typed as Object.
   */
  public Object clone() {
    // create new object
    PTStringArray targetShape = new PTStringArray();

    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    return targetShape;
  }

  // ===============
  // PROPERTY ACCESS
  // ===============

  /**
   * Defines objects of this class as StringArray.
   */
  public String[] handledKeywords() {
    return new String[] { "StringArray" };
  }

  /**
   * Store the given String in the specified cell of the array. The cell size is
   * automatically adapted to the new content.
   * 
   * @param index
   *          the number of the array cell
   * @param val
   *          the value that's to be stored
   */
  public int enterValue(int index, String val) {
    if ((index >= 0) && (index < entry.length)) {
        entry[index].setText(val);
        updateAllCellsWithCurrentStates();
        return 0;
    }
    return -1;
  }

  /**
   * Retrieve the content of an array cell.
   * 
   * @param index
   *          the cell which content is requested.
   */
  public String getValue(int index) {
    if ((index >= 0) && (index < entry.length))
      return entry[index].getText();

    return null;
  }

  /**
   * Return the object type as "PTStringArray".
   */
  public String getType() {
    return STRING_ARRAY_TYPE;
  }

  // ================
  // INTERNAL METHODS
  // ================

  /**
   * file version history 1: basic format 2: display of cell indices can be
   * turned on or off 3: element highlight color added
   */
  public int getFileVersion() {
    return 3;
  }

  @Override
  public void enterStringValueAt(int index, String text) {
    enterValue(index, text);
  }

  @Override
  protected PTText createInternalValue(int cellPosition, Font targetFont) {
    return new PTText(String.valueOf((char) ('a' + cellPosition)), targetFont);
  }
}