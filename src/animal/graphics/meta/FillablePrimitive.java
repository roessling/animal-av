/*
 * Created on 27.08.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.graphics.meta;

import java.awt.Color;

/**
 * This interface models primitives that may be filled
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0 27.08.2007
 */
public interface FillablePrimitive {
	/**
	 * Queries if the primitive is actually filled or not
	 * 
	 * @return the information if the primitive is filled (=true)
	 * or not (=false)
	 */
  public boolean isFilled();
  
	/**
	 * Returns the fill color of the primitive.
	 * 
	 * @return the fill color of the primitive
	 */
	public Color getFillColor();
	
	/**
	 * Set the primitives fill color
	 * 
	 * @param targetFillColor the fill color of the primitive
	 */
	public void setFillColor(Color targetFillColor);
	
	
	/**
	 * Set whether the primitive is filled or not
	 * 
	 * @param filled if <code>true</code>, the arc is filled
	 */
	public void setFilled(boolean filled);
}
