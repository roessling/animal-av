package generators.framework;

import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;

/**
 * checks if a given invocation of "generate" would be correct,
 * and will indicate possible problems by throwing an
 * IllegalArgumentException
 * 
 * @author Guido R&ouml;&szlig;ling (roessling@acm.org)
 * @version 1.1
 */
public interface ValidatingGenerator extends Generator {
  
  /**
   * checks if the current set of parameters would be valid
   * for an invocation of generate(AnimationPropertiesContainer,
   * Hashtable&lt;String, Object&gt;).
   * 
   * @param props the animation properties determining the visual
   * layout of the components
   * @param primitives the primitive values as entered by the user
   * @throws IllegalArgumentException if the arguments will lead
   * to a problem when passed on to "generate"
   * @see #generate(AnimationPropertiesContainer,Hashtable)
   * @return true if the invocation would be OK; else false
   */
  boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException;
}
