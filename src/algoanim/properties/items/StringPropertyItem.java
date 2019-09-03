package algoanim.properties.items;

import algoanim.properties.Visitable;
import algoanim.properties.Visitor;

/**
 * Represents an <code>AnimationPropertiesItem</code> that stores a String
 * value.
 * 
 * @author T. Ackermann
 */
public class StringPropertyItem extends AnimationPropertyItem implements
    Cloneable, Visitable {

  /** stores the String */
  private String str = "";

  /**
   * Sets the default value to defValue.
   * 
   * @param defValue
   *          the default String.
   */
  public StringPropertyItem(String defValue) {
    str = defValue;
  }

  /**
   * The default String is empty
   */
  public StringPropertyItem() {
    // do nothing
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#get()
   */
  public Object get() {
    return str;
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#set(java.lang.String)
   */
  public boolean set(String value) throws IllegalArgumentException {
    if (value == null)
      throw new IllegalArgumentException("String value cannot be null");

    str = value;
    return true;
  }

  /**
   * Clones the element
   * 
   * @return a clone of this element
   */
  public Object clone() {
    StringPropertyItem ret = new StringPropertyItem();
    ret.set(str);
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
