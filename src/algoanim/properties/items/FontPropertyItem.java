package algoanim.properties.items;

import java.awt.Font;

import algoanim.properties.Visitable;
import algoanim.properties.Visitor;

/**
 * Represents an <code>AnimationPropertiesItem</code> that stores a
 * <code>Font</code> value.
 * 
 * @author T. Ackermann
 */
public class FontPropertyItem extends AnimationPropertyItem implements
    Cloneable, Visitable {

  /** stores the Font */
  private Font fon = new Font("SansSerif", Font.PLAIN, 12);

  /**
   * Sets the default value to defValue.
   * 
   * @param defValue
   *          the default font.
   */
  public FontPropertyItem(Font defValue) {
    fon = defValue;
  }

  /**
   * Sets the default font to sansserif.
   * 
   */
  public FontPropertyItem() {
    // do nothing
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#get()
   */
  public Object get() {
    return fon;
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#set(java.awt.Font)
   */
  public boolean set(Font value) throws IllegalArgumentException {
    if (value == null) {
      throw new IllegalArgumentException("Null not accepted!");
    }
    fon = value;
    return true;
  }

  /**
   * Clones the element
   * 
   * @return a clone of this element
   */
  public Object clone() {
    FontPropertyItem ret = new FontPropertyItem();
    ret.set(fon);
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
