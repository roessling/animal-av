package animal.animator;


/**
 * Animator that can change some of a GraphicObject's properties.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class PropertyChanger extends TimedAnimator { 

	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "PropertyChanger";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 5575863619518931027L;

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The parameter that is to be changed
	 */
	private Object parameter = null;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public PropertyChanger() {
		// do nothing; only used for serialization
	} // for serialization

	/**
	 * Construct a new PropertyChanger from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param param
	 *            the parameter to change
	 */
	public PropertyChanger(int step, int objectNum, int totalTimeOrTicks,
			String method, Object param) {
		this(step, new int[] { objectNum }, totalTimeOrTicks, method, param);
	}

	/**
	 * Construct a new PropertyChanger from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param param
	 *            the parameter to change
	 */
	public PropertyChanger(int step, int[] objectNums, int totalTimeOrTicks,
			String method, Object param) {
		super(step, objectNums, totalTimeOrTicks, method);
		parameter = param;
	}

	// =================================================================
	//                                 ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public String getAnimatorName() {
		return "PropertyChange";
	}

	/***************************************************************************
	 * retrieve the file version for this animator It must be incremented
	 * whenever the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>original release</li>
	 * </ol>
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public int getFileVersion() {
		return 1;
	}

	/**
	 * Returns the property at a certain time. getProperty <em>must</em>
	 * return a property of the "normal" type (i.e. Move must always return a
	 * Point), even if the object is not completely initialized(then return a
	 * dummy!), as TimedAnimatorEditor relies on receiving a property for
	 * querying the possible methods.
	 * 
	 * @param factor
	 *            a value between 0 and 1, indicating how far this animator has
	 *            got(0: start, 1: end)
	 * 
	 * @return the property at this time
	 */
	public Object getProperty( double factor) {
		return new Integer(42); // MUST change this!
	}

	public String getType() {
		return TYPE_LABEL;
	}

	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "PropertyChange" };
	}

	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Return a String representation of this object.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		return "change property(" + getMethod() + ") to " + parameter + " on "
				+ super.toString();
	}
}
