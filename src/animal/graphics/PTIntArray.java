/*
 * PTIntArray.java
 * The class for a PTIntArray.
 *
 * Created on 1. Juni 2005, 14:25
 *
 * @author Michael Schmitt
 * @version 2.0.3b
 * @date 2006-03-27
 */

package animal.graphics;

import java.awt.Font;

import animal.graphics.meta.PTArray;

public class PTIntArray extends PTArray {

  // =========
  // CONSTANTS
  // =========

  public static final String INT_ARRAY_TYPE = "IntArray";

  // ============
  // CONSTRUCTORS
  // ============

  /**
   * Default constructor
   */
  public PTIntArray() {
    // initialize with default attributes
    initializeWithDefaults(getType());
    int targetSize = getSize();
    if (targetSize == 0)
      targetSize = 10;
    init(targetSize);
  }

  /**
   * Constructs a PTIntArray of the specified length. This is the one used by
   * the <code>IntArrayEditor</code>.
   * 
   * @param nrEntries
   *          the number of entries
   */
  public PTIntArray(int nrEntries) {
    this();
    init(nrEntries);
  }

  /**
   * Constructs a PTIntArray corresponding to the parsed IntArray.
   * 
   * @param x
   *          the original IntArray
   */
  public PTIntArray(int[] x) {
    this(x.length);
    for (int i = 0; i < x.length; i++) {
      enterValue(i, x[i]);
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
    PTIntArray targetShape = new PTIntArray();

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
   * Defines objects of this class as IntArray.
   */
  public String[] handledKeywords() {
    return new String[] { "IntArray" };
  }

  /**
   * Store the given int in the specified cell of the array.
   * 
   * @param index
   *          the number of the array cell
   * @param val
   *          the value that's to be stored
   */
  public int enterValue(int index, int val) {
    if ((index >= 0) && (index < entry.length)) {
        entry[index].setText(String.valueOf(val));
        updateAllCellsWithCurrentStates();
        return 0;
    }
    return -1;
  }
  
  /**
   * returns the values of the array as an int array.
   * Note that the data is stored differently internally,
   * so that modifying the returned arrqy will <em>not</em>
   * affect the internal values!
   * 
   * @return a int[] representation of this array
   */
  public int[] getValues() {
    int[] result = new int[getSize()];
    for (int i = 0; i < result.length; i++)
      result[i] = getValue(i);
    return result;
  }

  /**
   * Retrieve the content of an array cell.
   * 
   * @param index
   *          the cell which content is requested.
   */
  public int getValue(int index) {
    if ((index >= 0) && (index < entry.length))
      return Integer.parseInt(entry[index].getText());

    return 0;
  }

  /**
   * Return the object type as "PTIntArray".
   */
  public String getType() {
    return INT_ARRAY_TYPE;
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
    int value = 0;
    try {
      value =  Integer.parseInt(text);
      enterValue(index, value);
    } catch (NumberFormatException exc) {
      // Nothing done here
    }
  }

  @Override
  protected PTText createInternalValue(int cellPosition, Font targetFont) {
    return new PTText(String.valueOf(cellPosition), targetFont);
  }
}