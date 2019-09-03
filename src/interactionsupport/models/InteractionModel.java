package interactionsupport.models;

/**
 * Model representing a general element with which the user can interact. Such
 * an element must have a unique identifier.
 * 
 * @author Guido Roessling <roessling@acm.org> / Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public abstract class InteractionModel {

  /** the unique identifier of this interactive element */
  protected String id = null;

  public InteractionModel(String elementID) {
    setID(elementID);
  }

  /**
   * Retrieves the unique identifier of this interactive element.
   * 
   * @return the unique ID of this element
   */
  public String getID() {
    return id;
  }

  /**
   * Sets the unique identifier of this interactive element.
   * 
   * @param newID
   *          the new unique ID of this element
   */
  public void setID(String newID) {
    id = newID;
  }

}
