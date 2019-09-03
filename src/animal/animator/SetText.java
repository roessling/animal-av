/**
 * 
 */
package animal.animator;

import animal.misc.XProperties;

/**
 * The Animator SetText is used on PTText objects to change their Caption.
 * At the moment its used by GridProducer to change cellcontent. But of
 * course it works on any PTText. For use in Animal Script an appropriate
 * parser is needed (which should be easy to implement).
 * The animation consists of a sequetial appeareance of the new value within
 * the given duration.
 *  
 * @author <a href="mailto:here@christoph-preisser.de">Christoph Prei&szlig;er</a>
 * @version 0.98 2006-12-21
 */
public class SetText extends TimedAnimator {

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;

	/**
	 * The label of the property -- always use this constant for access!
	 */
	public static final String TEXT_PROPERTY_KEY = "SetTextValue";
	
	/**
	 * The type label of this animator
	 */
	public static final String TYPE_LABEL = "Set Text";

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The original Values, used for a gradual Text Change (its more fun than it makes sense)
	 */
	//private String[] originalValues = null;
	
	/**
	 * Public(empty) constructor required for serialization.
	 */
	public SetText() {
		super();
	}

	/**
	 * Constructor of the Animator.
	 * @param step The step in which the animator is placed
	 * @param objectNum The number of the object used in the animator
	 * @param totalTimeOrTicks The duration (has no effect)
	 * @param timeOrTicksOffset Delay before the animation starts
	 * @param unit "ticks" or "ms"
	 * @param value the value to set
	 */
	public SetText(int step, int objectNum, int totalTimeOrTicks, 
			int timeOrTicksOffset, String unit, String value) {
			this(step, new int[] {objectNum}, totalTimeOrTicks, timeOrTicksOffset, 
					unit, "setText", value);	
	}
/**
	 * Constructor of the Animator.
	 * @param step The step in which the animator is placed
	 * @param objectNum The number of the object used in the animator
	 * @param totalTimeOrTicks The duration (has no effect)
	 * @param timeOrTicksOffset Delay before the animation starts
	 * @param unit "ticks" or "ms"
	 * @param method the method name to be used
	 * @param value the value to set
	 */
	public SetText(int step, int objectNum, int totalTimeOrTicks, 
			int timeOrTicksOffset, String unit, String method, String value) {
			this(step, new int[] {objectNum}, totalTimeOrTicks, timeOrTicksOffset, 
					unit, method, value);	
	}

	/**
	 * Constructor of the Animator.
	 * @param step The step in which the animator is placed
	 * @param objectNums Array of objectnumbers to use in the animator
	 * @param totalTimeOrTicks The duration (has no effect)
	 * @param timeOrTicksOffset Delay before the animation starts
	 * @param unit "ticks" or "ms"
	 * @param value the value to set
	 */
	public SetText(int step, int[] objectNums, int totalTimeOrTicks,
			int timeOrTicksOffset, String unit, String value) {
		this(step, objectNums, totalTimeOrTicks, timeOrTicksOffset, 
				unit, "setText", value);
	}
	
	/**
	 * Constructor of the Animator.
	 * @param step The step in which the animator is placed
	 * @param objectNums Array of objectnumbers to use in the animator
	 * @param totalTimeOrTicks The duration (has no effect)
	 * @param timeOrTicksOffset Delay before the animation starts
	 * @param unit "ticks" or "ms"
	 * @param value the value to set
	 */
	public SetText(int step, int[] objectNums, int totalTimeOrTicks,
			int timeOrTicksOffset, String unit, String method, String value) {
		super(step, objectNums, totalTimeOrTicks, timeOrTicksOffset, 
				method); //TOASK hier also wird festgelegt
		//																																							 welcher Handler verwendet wird?
		setUnitIsTicks(true);
		if (unit.equalsIgnoreCase("ms")) 
			setUnitIsTicks(false);

		setValue(value);
	}

	public SetText(XProperties props) {
		setProperties(props);
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
		return "SetText";
	}

	/**
	 * Returns the value to change to
	 * 
	 * @return the value to change to
	 */
	public String getValue() {
		return getProperties().getProperty(TEXT_PROPERTY_KEY);
	}


	public void setValue(String value) {
	  getProperties().put(TEXT_PROPERTY_KEY, value);
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
	public Object getProperty(double factor) {
			String newValue = getValue();
		return newValue.substring(0,(int)(newValue.length()*factor));
	}

	/**
	 * returns the type name for this animator
	 * 
	 * @return the type name for this animator
	 */
	public String getType() {
		return TYPE_LABEL;
	}



	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "SetText", "settext" };
	}
	
	public String toString() {
		return "Set Text(" + getMethod() + ") of " + super.toString() + " to \"" + getValue() + "\"";
	}
}
