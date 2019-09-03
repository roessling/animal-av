package algoanim.properties.items;

import java.awt.Color;

import algoanim.properties.Visitable;
import algoanim.properties.Visitor;

/**
 * Represents an <code>AnimationPropertiesItem</code> that stores a
 * <code>Color</code> value.
 * 
 * @author T. Ackermann
 */
public class ColorPropertyItem extends AnimationPropertyItem implements
    Cloneable, Visitable {

  /** stores the Color */
  private Color col = Color.BLACK;

  /**
   * Sets the default value to defValue.
   * 
   * @param defValue
   *          the default color.
   */
  public ColorPropertyItem(Color defValue) {
    col = defValue;
  }

  /**
   * Sets the color to green (default).
   */
  public ColorPropertyItem() {
    // do nothing
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#get()
   */
  public Object get() {
    return col;
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#set(java.awt.Color)
   */
  public boolean set(Color value) throws IllegalArgumentException {
    if (value == null) {
      throw new IllegalArgumentException("Null not accepted!");
    }
    col = value;
    return true;
  }

  /**
   * Clones the element
   * 
   * @return a clone of this element
   */
  public Object clone() {
    ColorPropertyItem ret = new ColorPropertyItem();
    ret.set(col);
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
