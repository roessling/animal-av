/*
 * GraphicObjectSpecificAnimation.java
 *
 * Created on 10. Januar 2006, 19:25
 *
 * @author Michael Schmitt
 * @version 0.1a
 * @date 2006-02-23
 */

package animal.animator;

/**
 * This interface is for animation types that only work on a specific type of
 * graphic object (or data structure).
 * 
 * Think for example of swap in an array structure or push and pop in a stack
 * implementation.
 */
public interface GraphicObjectSpecificAnimation {
	/**
	 * Get the graphic object types on which the animator works.
	 * 
	 * @return an array of the supported types
	 */
	public String[] getSupportedTypes();
}
