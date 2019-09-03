package animal.animator;

import java.awt.Font;

import animal.misc.XProperties;

/**
 * The Animator SetFont is used on PTText objects to change their Font.
 * At the moment its used by GridProducer to change cellcontent. But of
 * course it works on any PTText. For use in Animal Script an appropriate
 * parser is needed (which should be easy to implement).  
 * 
 * @author <a href="mailto:here@christoph-preisser.de">Christoph Prei&szlig;er</a>
 * @version 0.98 2006-12-21
 */
public class SetFont extends TimedAnimator {

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;

	/**
	 * The label of the property -- always use this constant for access!
	 */
	public static final String FONT_PROPERTY_KEY = "SetFontObject";
	
	/**
	 * The type label of this animator
	 */
	public static final String TYPE_LABEL = "Set Font";

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	
	/**
	 * constructor
	 */
	public SetFont() {
		super();
	}

	/**
	 * Constructor of the Animator.
	 * @param step The step in which the animator is placed
	 * @param objectNum The number of the object used in the animator
	 * @param totalTimeOrTicks The duration (has no effect)
	 * @param timeOrTicksOffset Delay before the animation starts
	 * @param unit "ticks" or "ms"
	 * @param font font to set to
	 */
	public SetFont(int step, int objectNum, int totalTimeOrTicks, 
			int timeOrTicksOffset,String unit, Font font) {
			this(step, new int[] {objectNum}, totalTimeOrTicks, timeOrTicksOffset, unit, font);	
	}

	/**
	 * Constructor of the Animator.
	 * @param step The step in which the animator is placed
	 * @param objectNums Array of objectnumbers to use in the animator
	 * @param totalTimeOrTicks The duration (has no effect)
	 * @param timeOrTicksOffset Delay before the animation starts
	 * @param unit "ticks" or "ms"
	 * @param font font to set to
	 */
	public SetFont(int step, int[] objectNums, int totalTimeOrTicks,
			int timeOrTicksOffset,String unit, Font font) {
		super(step, objectNums, totalTimeOrTicks, timeOrTicksOffset, "setFont");

		setUnitIsTicks(true);
		if (unit.equalsIgnoreCase("ms")) setUnitIsTicks(false);

		setValue(font);
	}


	/**
	 * creates a new Animator instance based on the properties passed in
	 * 
	 * @param props
	 *            the properties from which to generate the new object
	 */
	public SetFont(XProperties props) {
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
		return "SetFont";
	}

	/**
	 * Returns the value to change to
	 * 
	 * @return the value to change to
	 */
	public Font getValue() {
		return getProperties().getFontProperty(FONT_PROPERTY_KEY);
	}


  public void setValue(Font font) {  
		getProperties().put(FONT_PROPERTY_KEY, font);
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
		return getValue();
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
		return new String[] { "setfont" };
	}
	
	public String toString() {
		return "Set Font of " + super.toString() + " to " + getValue().getFontName();
	}
}
