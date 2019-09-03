package algoanim.properties.items;

import java.util.Vector;

import algoanim.properties.Visitable;
import algoanim.properties.Visitor;

/**
 * Represents an <code>AnimationPropertiesItem</code> that stores a
 * <code>Color</code> value.
 * 
 * @author Guido R&ouml;&szlig;ling <roessling@acm.org>
 * @version 0.7 20080509
 */
public class EnumerationPropertyItem extends AnimationPropertyItem implements
Cloneable, Visitable {

  /** stores the Color */
  private String choice = "";

  /**
   * stores the possible choice values
   */
  private String[] choices;

  /**
   * Sets the default value to defValue.
   * 
   * @param defaultChoice
   *          the default color.
   * @param availableChoices the vector of possible choices
   */
  public EnumerationPropertyItem(String defaultChoice,
      Vector<String> availableChoices) {
    choice = defaultChoice;
    choices = new String[availableChoices.size()];
    int choiceNr = 0;
    for (String aChoice: availableChoices)
      choices[choiceNr++] = aChoice;
//    for (int choiceNr = 0; choiceNr < availableChoices.length; choiceNr++)
//      choices[choiceNr] = new String(availableChoices[choiceNr]);
  }
  public EnumerationPropertyItem() {
    // does nothing
  }
  /**
   * Sets the color to green (default).
   */
  public EnumerationPropertyItem(Vector<String> choices) {
    this(choices.elementAt(0), choices);
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#get()
   */
  public Vector<String> get() { //TODO Check if this change was OK!
    Vector<String> outputVector = new Vector<String>(choices.length + 1);
    outputVector.add(choice);
    for (String value : choices) {
      outputVector.add(value);
    }
    return outputVector;
  }

  /**
   * @see algoanim.properties.items.AnimationPropertyItem#set(java.awt.Color)
   */
  public boolean set(String value) throws IllegalArgumentException {
    if (value == null) {
      throw new IllegalArgumentException("Null not accepted!");
    }
    String comparator = null;
    if (value.indexOf(",") > -1)
      comparator = value.substring(0, value.indexOf(","));
    else
      comparator = value;
    for (String anOption : choices)
      if (anOption.equals(comparator)) {
        choice = comparator;
        return true;
      }
    throw new IllegalArgumentException("Choice '" +value +"' not in list.");
  }

  /**
   * Clones the element
   * 
   * @return a clone of this element
   */
  public Object clone() {
    Vector<String> newVector = new Vector<String>(choices.length);
    for (String aChoice: choices)
      newVector.add(aChoice);
    EnumerationPropertyItem ret = new EnumerationPropertyItem(choice, newVector);
//    EnumerationPropertyItem ret = new EnumerationPropertyItem(choice, choices);
    return ret;
  }

  /**
   * @see algoanim.properties.Visitable
   */
  public void accept(Visitor v) {
    if (v != null)
      v.visit(this);
  }
  
  public String getChoice() {
    return choice;
  }
}
