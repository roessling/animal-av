package algoanim.properties.items;

import algoanim.properties.Visitable;
import algoanim.properties.Visitor;

/**
 * Represents an <code>AnimationPropertiesItem</code> that stores a
 * <code>double</code> value.
 * 
 * @author T. Ackermann
 */
public class DoublePropertyItem extends AnimationPropertyItem implements
    Cloneable, Visitable {

  /** stores the double value */
  private double dValue = 1;

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#get()
   */
  public Object get() {
    return Double.valueOf(dValue);
  }

  /**
   * Sets the internal value to the given <code>double</code> value.
   * 
   * @param value
   *          the new value.
   * @return true, if no error occurred.
   */
  public boolean set(double value) {
    dValue = value;
    return true;
  }

  /**
   * Sets the internal value to the given <code>double</code> value.
   * 
   * @param value
   *          the new value (as a Double).
   * @return true, if no error occurred.
   */
  public boolean set(Double value) {
    dValue = value.doubleValue();
    return true;
  }

  /**
   * Clones the element
   * 
   * @return a clone of this element
   */
  public Object clone() {
    DoublePropertyItem ret = new DoublePropertyItem();
    ret.set(dValue);
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
