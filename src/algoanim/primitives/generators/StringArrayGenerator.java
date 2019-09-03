package algoanim.primitives.generators;

import java.awt.Color;

import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.StringArray;
import algoanim.util.Timing;

/**
 * <code>StringArrayGenerator</code> offers methods to request the included
 * Language object to append String array related script code lines to the
 * output. It is designed to be included by a <code>StringArray</code>
 * primitive, which just redirects action calls to the generator. Each script
 * language offering a <code>StringArray</code> primitive has to implement its
 * own <code>StringArrayGenerator</code>, which is then responsible to create
 * proper script code.
 * 
 * @author Stephan Mehlhase
 */
public interface StringArrayGenerator extends GenericArrayGenerator {
  /**
   * Creates the originating script code for a given <code>StringArray</code>,
   * due to the fact that before a primitive can be worked with it has to be
   * defined and made known to the script language.
   * 
   * @param sa
   *          the <code>StringArray</code> for which the initiate script code
   *          shall be created.
   */
  public void create(StringArray sa);

  /**
   * Inserts a <code>String</code> at certain position in the given
   * <code>StringArray</code>.
   * 
   * @param iap
   *          the <code>StringArray</code> in which to insert the value.
   * @param where
   *          the position where the value shall be inserted.
   * @param what
   *          the <code>String</code> value to insert.
   * @param delay
   *          the time to wait until the operation shall be performed.
   * @param duration
   *          the duration of the operation.
   */
  public void put(StringArray iap, int where, String what, Timing delay,
	      Timing duration);
  

  /**
   * Set the Visibility of the Indices of the <code>StringArray</code>.
   * 
   * @param sap
   *          the <code>StringArray</code> in which to insert the value.
   * @param show
   *          visibility
   * @param delay
   *          the time to wait until the operation shall be performed.
   * @param duration
   *          the duration of the operation.
   */
  public void showIndices(StringArray sap, boolean show, Timing delay,
			Timing duration);
  
  public void setColorTyp(ArrayPrimitive ia, String typ, int from, int to, Color c, Timing offset,
	      Timing duration);
  
  public void setColorTyp(ArrayPrimitive ia, String typ, int position, Color c, Timing offset,
	      Timing duration);
}
