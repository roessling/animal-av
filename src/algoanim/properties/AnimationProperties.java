package algoanim.properties;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import algoanim.properties.items.AnimationPropertyItem;
import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.items.FontPropertyItem;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.items.StringPropertyItem;
import algoanim.properties.meta.ArrowableAnimationProperties;
import algoanim.properties.meta.ClosableAnimationProperties;
import algoanim.properties.meta.FillableAnimationProperties;
import algoanim.properties.meta.FontContainingAnimationProperties;
import algoanim.properties.meta.HighlightableAnimationProperties;
import algoanim.properties.meta.OrientedAnimationProperties;

/**
 * Description of the Properties system: Every type of all languages has its
 * associated class in the primitives package and an own properties class which
 * holds the relevant informations to display the object. These properties
 * classes are based on a <code>HashMap</code> consisting of pairs of a
 * <code>String</code> key and a <code>PropertyItem</code>. Each
 * <code>PropertyItem</code> is represented by an object of a definite class
 * which is able to work properly on its internal data. There are types for
 * color, font and integer PropertyItems for example. But actually this
 * underlying concept is not visible to the user.
 * 
 * @author Jens Pfau, Stephan Mehlhase, T. Ackermann
 */

public abstract class AnimationProperties {

  /**
   * Contains a mapping from String keys to PropertyItems.
   */
  protected HashMap<String, AnimationPropertyItem> data;
  private HashMap<String, Object>                  defaults;
  private HashMap<String, Boolean>                 isEditable;
  private HashMap<String, String>                  labels;

  /**
   * Default Constructor
   * 
   * The constructor in the derivated classes *MUST* fill the data
   * <code>HashMap</code> with keys and appropriate
   * <code>AnimationPropertyItem</code>s.
   */
  public AnimationProperties() {
    this("Some unknown Property");
  }

  /**
   * Constructor which receives the name of the property.
   * 
   * The constructor in the derivated classes *MUST* fill the data
   * <code>HashMap</code> with keys and appropriate
   * <code>AnimationPropertyItem</code>s.
   * 
   * @param name
   *          the name of the Properties object.
   */
  public AnimationProperties(String name) {
    data = new HashMap<String, AnimationPropertyItem>();
    defaults = new HashMap<String, Object>();
    isEditable = new HashMap<String, Boolean>();
    labels = new HashMap<String, String>();
    setName(name);
  }

  /**
   * Inserts the "name" items into the <code>HashMap</code>s.
   * 
   * @param newName
   *          The initial name for this Propertie.
   */
  public void setName(String newName) {
    StringPropertyItem nameItem = new StringPropertyItem();
    nameItem.set(newName);
    data.put(AnimationPropertiesKeys.NAME, nameItem);
    defaults.put("name", "Some unknown Property");
    isEditable.put("name", Boolean.valueOf(false));
    labels.put("name", "Name");
  }

//  /**
//   * Fills the internal data <code>HashMap</code> with values and copies them to
//   * all other <code>Hashmap</code>s.
//   */
//  protected abstract void fillHashMap();

  /**
   * Fills the internal data <code>HashMap</code> with values and copies them to
   * all other <code>Hashmap</code>s.
   */
 protected void fillHashMap() {
//    System.err.println("fillByInterface for " +getClass().getSimpleName());
    // add values for ArrowableAnimationProperties
    if (this instanceof ArrowableAnimationProperties) {
      data.put(AnimationPropertiesKeys.BWARROW_PROPERTY,
          new BooleanPropertyItem());
      data.put(AnimationPropertiesKeys.FWARROW_PROPERTY,
          new BooleanPropertyItem());
    }
    // add values for ClosableAnimationProperties
    if (this instanceof ClosableAnimationProperties) {
      data.put(AnimationPropertiesKeys.CLOSED_PROPERTY,
          new BooleanPropertyItem());
    }

    // add values for FillableAnimationProperties
    if (this instanceof FillableAnimationProperties) {
      data.put(AnimationPropertiesKeys.FILL_PROPERTY, new ColorPropertyItem());
      data.put(AnimationPropertiesKeys.FILLED_PROPERTY,
          new BooleanPropertyItem());
    }

    // add values for FontContainingAnimationProperties
    if (this instanceof FontContainingAnimationProperties) {
      data.put(AnimationPropertiesKeys.FONT_PROPERTY, new FontPropertyItem());
    }

    // add values for HighlightableAnimationProperties
    if (this instanceof HighlightableAnimationProperties) {
      data.put(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
          new ColorPropertyItem());
      data.put(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
          new ColorPropertyItem());
    }
    
    // add values for OrientedAnimationProperties
    if (this instanceof OrientedAnimationProperties) {
      data.put(AnimationPropertiesKeys.CLOCKWISE_PROPERTY,
          new BooleanPropertyItem());
      data.put(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY,
          new BooleanPropertyItem());
    }

    // add the default entries
    data.put(AnimationPropertiesKeys.COLOR_PROPERTY, new ColorPropertyItem());
    data.put(AnimationPropertiesKeys.DEPTH_PROPERTY, new IntegerPropertyItem());
    data.put(AnimationPropertiesKeys.HIDDEN_PROPERTY, new BooleanPropertyItem());
    
    // add any type-specific values...
    addTypeSpecificValues();
    
    // put in "all else"
    fillAdditional();
  }

 protected void addTypeSpecificValues() {
   // nothing to be done here...
 }
 
  /**
   * This function takes all keys from the data <code>HashMap</code> and fills
   * the <code>isEditable</code> and <code>labels</code> <code>HashMap</code>s
   * with appropriate values. (All Elements of <code>isEditable</code> are false
   * by default and all elements of <code>labels</code> are empty
   * <code>String</code>s.)
   */
  public void fillAdditional() {
    Iterator<String> it = data.keySet().iterator();
    while (it.hasNext()) {
      String temp = it.next();
      defaults.put(temp, data.get(temp).clone());
      isEditable.put(temp, Boolean.valueOf(true));
      labels.put(temp, "");
    }
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return get("name").toString();
  }

  /**
   * Returns a <code>Set</code> view on all possible keys provided by a concrete
   * <code>AnimationProperties</code>.
   * 
   * @return a <code>Set</code> view on all possible keys provided by a concrete
   *         <code>AnimationProperties</code>.
   */
  public Set<String> getAllPropertyNames() {
    return data.keySet();
  }

  /**
   * Returns a <code>Set</code> view on all possible keys provided by a concrete
   * <code>AnimationProperties</code>.
   * 
   * @return a <code>Set</code> view on all possible keys provided by a concrete
   *         <code>AnimationProperties</code>.
   */
  public Vector<String> getAllPropertyNamesVector() {
    Set<String> keySet = data.keySet();
    Iterator<String> iterator = keySet.iterator();
    Vector<String> vec = new Vector<String>(keySet.size());
    while (iterator.hasNext()) {
      vec.add(iterator.next());
    }
    return vec;
  }

  /**
   * Returns a <code>Vector</code> of <code>String</code>s with all classnames
   * which are in this package and can be used.
   * 
   * @return a <code>Vector</code> of <code>String</code>s with all classnames
   *         which are in this package and can be used.
   */
  public final static Vector<String> getAllPropertyTypes() {
    Vector<String> classes = new Vector<String>();
    classes.add("ArcProperties");
    classes.add("ArrayProperties");
    classes.add("ArrayMarkerProperties");
    classes.add("CircleProperties");
    classes.add("CircleSegProperties");
    classes.add("EllipseProperties");
    classes.add("ListElementProperties");
    classes.add("MatrixProperties");
    classes.add("PolygonProperties");
    classes.add("PolylineProperties");
    classes.add("RectProperties");
    classes.add("SourceCodeProperties");
    classes.add("SquareProperties");
    classes.add("StackProperties");
    classes.add("TextProperties");
    classes.add("TriangleProperties");
    classes.add("CallMethodProperties");
    return classes;
  }

  /**
   * Searches the map for the item according to the given key.
   * 
   * @param key
   *          The key of the item.
   * @return The AnimationPropertyItem with the given key.
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public AnimationPropertyItem getItem(String key)
      throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    return data.get(key);
  }

  /**
   * Sets the <code>PropertyItem</code> associated with the key to the new
   * value.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          the new value (as an <code>int</code>).
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public void set(String key, int value) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    data.get(key).set(value);
  }

  /**
   * Sets the <code>PropertyItem</code> associated with the key to the new
   * value.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          the new value (as a <code>String</code>).
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public void set(String key, String value) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    data.get(key).set(value);
  }

  /**
   * Sets the <code>PropertyItem</code> associated with the key to the new
   * value.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          the new value (as a <code>boolean</code>).
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public void set(String key, boolean value) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    data.get(key).set(value);
  }

  /**
   * Sets the <code>PropertyItem</code> associated with the key to the new
   * value.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          the new value (as a <code>Color</code>).
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public void set(String key, Color value) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    data.get(key).set(value);
  }

  /**
   * Sets the <code>PropertyItem</code> associated with the key to the new
   * value.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          the new value (as a <code>Font</code>).
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public void set(String key, Font value) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    data.get(key).set(value);
  }

  /**
   * Sets the <code>PropertyItem</code> associated with the key to the new
   * value.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          the new value (as an <code>Object</code>).
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public void set(String key, Object value) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found in the " +getClass().getSimpleName() + " properties.");
    }
    data.get(key).set(value);
  }

  /**
   * Searches the map for the value according to the given key.
   * 
   * @param key
   *          the key of the item.
   * @return the value of the item.
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public Object get(String key) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    return data.get(key).get();
  }

  /**
   * Returns the default value for the given key (as an Object).
   * 
   * @param key
   *          the key of the item.
   * @return the default value for the given key (as an Object).
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public AnimationPropertyItem getDefault(String key)
      throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    return (AnimationPropertyItem) defaults.get(key);
  }

  /**
   * Sets the default value for the given key.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          the new default value (as an Object).
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public void setDefault(String key, AnimationPropertyItem value)
      throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    defaults.put(key, value);
  }

  /**
   * Returns whether an item is editable by the end-user of the Generator-GUI.
   * 
   * @param key
   *          the key of the item.
   * @return whether the item is editable?
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public boolean getIsEditable(String key) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    return isEditable.get(key).booleanValue();
  }

  /**
   * Sets whether an item is editable by the end-user of the Generator GUI.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          whether the item should be editable.
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public void setIsEditable(String key, boolean value)
      throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    isEditable.put(key, Boolean.valueOf(value));
  }

  /**
   * Returns the label of the item.
   * 
   * @param key
   *          the key of the item.
   * @return the label of the item.
   * @throws IllegalArgumentException
   *           if the item doesn't exist.
   */
  public String getLabel(String key) throws IllegalArgumentException {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    return labels.get(key);
  }

  /**
   * Sets the label of the given item.
   * 
   * @param key
   *          the key of the item.
   * @param value
   *          the new label for the item.
   */
  public void setLabel(String key, String value) {
    if (!data.containsKey(key)) {
      throw new IllegalArgumentException("The given key '" + key
          + "' was not found!");
    }
    labels.put(key, value);
  }
}
