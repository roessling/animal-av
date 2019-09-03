package animal.misc;

/**
 * This interface helps authors define required classes or packages
 * for running an im- or exporter.
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 1.0
 */
public interface LibraryRequirementDefinition {
  /**
   * Returns a set of classes which should be "known" in order to
   * run the current class. Typically used via reflection (Class.forName)
   * to check for availability.
   *  
   * @return the array of String names containing class names
   */
  public String[] getRequiredClassNames();
}
