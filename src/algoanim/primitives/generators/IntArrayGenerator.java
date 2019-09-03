package algoanim.primitives.generators;

import java.awt.Color;

import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.IntArray;
import algoanim.util.Timing;

/**
 * <code>IntArrayGenerator</code> offers methods to request the included 
 * Language object to
 * append integer array related script code lines to the output.
 * It is designed to be included by an <code>IntArray</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering an <code>IntArray</code> primitive has to 
 * implement its own
 * <code>IntArrayGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface IntArrayGenerator extends GenericArrayGenerator {  
  /**
   * Creates the originating script code for a given <code>IntArray</code>,
   * due to the fact that before a primitive can be worked with it has to be 
   * defined and made known to the script language.
   * 
   * @param ia the <code>IntArray</code> for which the initiate script 
   * code shall be created. 
   */
  public void create(IntArray ia);

  /**
   * Inserts an <code>int</code> at certain position in the given 
   * <code>IntArray</code>.
   * 
   * @param iap the <code>IntArray</code> in which to insert the value.
   * @param where the position where the value shall be inserted.
   * @param what the <code>int</code> value to insert.
   * @param delay the time to wait until the operation shall be performed.
   * @param duration the duration of the operation.
   */
  public void put(IntArray iap, int where, int what, Timing delay, 
      Timing duration);
  

  /**
   * Set the visibility of the Indices of the <code>IntArray</code>.
   * 
   * @param iap
   *          the <code>IntArray</code> in which to insert the value.
   * @param show
   *          visibility
   * @param delay
   *          the time to wait until the operation shall be performed.
   * @param duration
   *          the duration of the operation.
   */
  public void showIndices(IntArray iap, boolean show, Timing delay,
			Timing duration);
  
  public void setColorTyp(ArrayPrimitive ia, String typ, int from, int to, Color c, Timing offset,
	      Timing duration);
  
  public void setColorTyp(ArrayPrimitive ia, String typ, int position, Color c, Timing offset,
	      Timing duration);
}
