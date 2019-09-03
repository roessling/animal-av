/*
 * Created on 27.08.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.graphics.meta;

/**
 * This interface models primitives that may have forward or 
 * backward arrows
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0 27.08.2007
 */
public interface ArrowablePrimitive {
	/**
	 * Return whether the primitive has a backward arrow
	 * 
	 * @return <code>true</code> if the primitive has a backward 
	 * arrow, else <code>false</code>
	 */
	public boolean hasBWArrow();

	/**
	 * Return whether the primitive has a forward arrow
	 * 
	 * @return <code>true</code> if the primitive has a forward arrow, 
	 * else <code>false</code>
	 */
	public boolean hasFWArrow();
	
	/**
	 * Set whether the primitive has a backward arrow or not
	 * 
	 * @param shallHaveBackwardArrow if <code>true</code>, the 
	 * primitive will have a backward arrow
	 */
	public void setBWArrow(boolean shallHaveBackwardArrow);
	
	/**
	 * Set whether the primitive has a forward arrow or not
	 * 
	 * @param shallHaveForwardArrow if <code>true</code>, the 
	 * primitive will have a forward arrow
	 */
	public void setFWArrow(boolean shallHaveForwardArrow);
}
