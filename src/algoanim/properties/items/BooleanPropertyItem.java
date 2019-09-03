package algoanim.properties.items;

import algoanim.properties.Visitable;
import algoanim.properties.Visitor;

/**
 * Represents an <code>AnimationPropertiesItem</code> that stores a
 * <code>boolean</code> value.
 * 
 * @author T. Ackermann
 */
public class BooleanPropertyItem extends AnimationPropertyItem implements
    Cloneable, Visitable {

  /** stores the boolean value. */
  private boolean bool = false;

  /**
   * Sets the default value to defValue.
   * 
   * @param defValue
   *          the default value.
   */
  public BooleanPropertyItem(boolean defValue) {
    bool = defValue;
  }

  /**
   * Without parameter, the default value of the BooleanPropertyItem is false
   */
  public BooleanPropertyItem() {
    // do nothing
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#get()
   */
  public Object get() {
    return Boolean.valueOf(bool);
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#set(boolean)
   */
  public boolean set(boolean value) throws IllegalArgumentException {
    bool = value;
    return true;
  }

  /**
   * Clone the item
   */
  public Object clone() {
    BooleanPropertyItem ret = (BooleanPropertyItem)super.clone();
    ret.set(bool);
    return ret;
  }

  /**
   * @see algoanim.properties.Visitable
   */
  public void accept(Visitor v) {
    if (v != null)
      v.visit(this);
  }

  public boolean equals(Object o) {
    return (o == null || !(o instanceof BooleanPropertyItem)
        || ((BooleanPropertyItem)o).get().equals(get()));
  }
}
