package algoanim.primitives.generators;

import algoanim.primitives.ArrayMarker;
import algoanim.util.Timing;

/**
 * <code>ArrayMarkerGenerator</code> offers methods to request the 
 * included 
 * Language object to append arraymarker related script code lines to the 
 * output.
 * It is designed to be included by an <code>ArrayMarker</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering an <code>ArrayMarker</code> primitive
 *  has to 
 * implement its own <code>ArrayMarkerGenerator</code>, which is then 
 * responsible to 
 * create proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface ArrayMarkerGenerator extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given 
	 * <code>ArrayMarker</code>, due to the fact that before a primitive 
	 * can be worked with it has to be defined and made known to the script 
	 * language.
	 * 
	 * @param am the <code>ArrayMarker</code> for which the initiate 
	 * script code shall be created. 
	 */
    public void create(ArrayMarker am);
    
    /**
     * Decrements the given <code>ArrayMarker</code> by one position of the 
     * associated <code>ArrayPrimitive</code>.
     * 
     * @param am the <code>ArrayMarker</code> to move.
     * @param delay the time to wait until the operation shall be performed.
     * @param duration the duration of the operation.
     */
    public void decrement(ArrayMarker am, Timing delay, Timing duration);

    /**
     * Increments the given <code>ArrayMarker</code> by one position of the 
     * associated <code>ArrayPrimitive</code>.
     * 
     * @param am the <code>ArrayMarker</code> to move.
      * @param delay the time to wait until the operation shall be performed.
     * @param duration the duration of the operation.
     */
    public void increment(ArrayMarker am, Timing delay, Timing duration);
    
    /**
     * Moves the given <code>ArrayMarker</code> to the given index of the 
     * associated <code>ArrayPrimitive</code>.
     * 
     * @param am the <code>ArrayMarker</code> to move.
     * @param to the position to where the <code>ArrayMarker</code> 
     * shall be moved.
     * @param delay the time to wait until the operation shall be performed.
     * @param duration the duration of the operation.
     */
    public void move(ArrayMarker am, int to, Timing delay, Timing duration);
   
    /**
     * Moves the <code>ArrayMarker</code> out of of the referenced 
     * <code>ArrayPrimitive</code> after the offset <code>t</code>. 
     * The operation will last as long as specified by the 
     * duration <code>d</code>.
     * 
     * @param t [optional] the offset until this operation starts.
     * @param d [optional] the duration of this operation.
     */ 
    public void moveBeforeStart(ArrayMarker am, Timing t, Timing d);
      
    /**
     * Moves the given <code>ArrayMarker</code> to the last element of the 
     * associated <code>ArrayPrimitive</code>.
     * 
     * @param am the <code>ArrayMarker</code> to move.
     * @param delay the time to wait until the operation shall be performed.
     * @param duration the duration of the operation.
     */
    public void moveToEnd(ArrayMarker am, Timing delay, Timing duration);
    
    /**
     * Moves the given <code>ArrayMarker</code> outside of the associated
     * <code>ArrayPrimitive</code>.
     * 
     * @param am the <code>ArrayMarker</code> to move.
     * @param delay the time to wait until the operation shall be performed.
     * @param duration the duration of the operation.
     */    
    public void moveOutside(ArrayMarker am, Timing delay, Timing duration);
}
