package generators.framework;

import java.util.Vector;

/**
 * This interface allows providing a full set of generators at once.
 * Typically, the returned generators will be placed outside the
 * "generators" package (or in a subpackage), to prevent
 * multiple accesses.
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 1.1 2008-06-24
 */
public interface GeneratorBundle {
  /**
   * returns a Vector of generator instances, usually for a given topic.
   * 
   * @return Vector of generator instances, usually for a given topic.
   */
  public Vector<Generator> getGenerators();
}
