package algoanim.properties;

/**
 * This interface is implemented by all AnimationPropertyItems and 
 * AnimationProperties, so the user is able to perform further
 * actions on these items without having to touch the code of this
 * API.
 * 
 * @author Jens Pfau
 */
public interface Visitable {
	/**
	 * Defines the interface for a Visitor to access a Visitable.
	 * @param v			the visitor
	 */
	public void accept(Visitor v);
}
