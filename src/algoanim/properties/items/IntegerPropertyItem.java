package algoanim.properties.items;

import algoanim.properties.Visitable;
import algoanim.properties.Visitor;

/**
 * Represents an <code>AnimationPropertiesItem</code> that stores an
 * <code>int</code> value.
 * 
 * @author T. Ackermann
 */
public class IntegerPropertyItem extends AnimationPropertyItem implements
    Cloneable, Visitable {

  /** stores the int value */
  private int iValue = 1;

  /** stores the minimal possible value of the int */
  private int min    = 0;
  
  /** stores the maximal possible value of the int */
  private int max    = Integer.MAX_VALUE;

  /**
   * Sets the default value to 1.
   */
  public IntegerPropertyItem() {
    // do nothing
  }

  /**
   * Sets the default value to defValue.
   * 
   * @param defValue
   *          the default value.
   */
  public IntegerPropertyItem(int defValue) {
    iValue = defValue;
  }

  /**
   * Gives the item bounds for the value saved in it, the value given by set
   * must in between [min, max].
   * 
   * @param defValue
   *          default value
   * @param minValue
   *          lower bound
   * @param maxValue
   *          upper bound
   */
  public IntegerPropertyItem(int defValue, int minValue, int maxValue) {
    iValue = defValue;
    min = minValue;
    max = maxValue;
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#get()
   */
  public Object get() {
    return Integer.valueOf(iValue);
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#set(int)
   */
  public boolean set(int value) throws IllegalArgumentException {
    if (iValue >= min && iValue <= max) {
      iValue = value;
      return true;
    }
    throw new IllegalArgumentException("Integer value out of bounds");
  }

  /**
   * Clones the element
   * 
   * @return a clone of this element
   */
  public Object clone() {
    IntegerPropertyItem ret = new IntegerPropertyItem(iValue, min, max);
    return ret;
  }

  /**
   * @see algoanim.properties.Visitable
   */
  public void accept(Visitor v) {
    if (v != null)
      v.visit(this);
  }
}
