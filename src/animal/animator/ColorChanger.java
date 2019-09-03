package animal.animator;

import java.awt.Color;
import java.beans.PropertyChangeEvent;

import animal.main.AnimationState;
import animal.misc.ColorChoice;
import animal.misc.XProperties;

/**
 * Animator that changes a GraphicObject's color.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */ 
public class ColorChanger extends TimedAnimator {
	// =================================================================
	//                                CONSTANTS
	// =================================================================

//	private static final int FILE_VERSION = 1;

	/**
	 * The label of the property - always use this constant!
	 */
	public static final String COLOR_LABEL = "color";

	/**
	 * The type label of the animator
	 */
	public static final String TYPE_LABEL = "ColorChanger";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 5575863619518931027L;

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The original color(s), used for a gradual color change
	 */
	private Color[] originalColor = null;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public ColorChanger() {
		// do nothing
	} // for serialization

	/**
	 * Construct a new ColorChanger from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param color
	 *            the target color to change to for this animator
	 */
	public ColorChanger(int step, int objectNum, int totalTimeOrTicks,
			String method, Color color) {
		this(step, new int[] { objectNum }, totalTimeOrTicks, method, color);
	}

	/**
	 * Construct a new ColorChanger from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param color
	 *            the target color to change to for this animator
	 */
	public ColorChanger(int step, int objectNum, int totalTimeOrTicks,
			String method, Color color, Color initialColor) {
		this(step, new int[] { objectNum }, totalTimeOrTicks, method, color,
				initialColor);
	}

	/**
	 * Construct a new ColorChanger for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param color
	 *            the target color to change to for this animator
	 */
	public ColorChanger(int step, int[] objectNums, int totalTimeOrTicks,
			String method, Color color) {
		super(step, objectNums, totalTimeOrTicks, method);
		setColor(color);
	}

	/**
	 * Construct a new ColorChanger for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param color
	 *            the target color to change to for this animator
	 */
	public ColorChanger(int step, int[] objectNums, int totalTimeOrTicks,
			String method, Color color,  Color initialColor) {
		super(step, objectNums, totalTimeOrTicks, method);
		setColor(color);
	}

	public ColorChanger(XProperties props) {
		setProperties(props);
	}

	// =================================================================
	//                         INITIALIZATION
	// =================================================================

	/**
	 * Prepares the Animator for use by the AnimationState and initializes it.
	 * If any actions have to be done <em>before</em>
	 * <code>action</code> is
	 * called, it has to be done here.
	 * 
	 * @param animationState
	 *            the animation state in which the animator is placed
	 * @param time
	 *            the duration of this animator in ms
	 * @param ticks
	 *            the duration of this animator in ticks
	 */
	public void init(AnimationState animationState, long time, double ticks) {
		super.init(animationState, time, ticks);
		if (objects != null) {
			originalColor = new Color[objects.length];
 			for (int i = 0; i < originalColor.length; i++) {
				if (objects != null && objects.length >i && objects[i] != null) {
				  XProperties props22 = objects[i].getProperties();
          String key = objects[i].getType() +"." +getMethod();
          Color c = props22.getColorProperty(key, Color.BLACK);
          originalColor[i] = c;
        }
      }
		}
	}

	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================

	/**
	 * Sets the object to the state it has after a certain time has passed. Can
	 * rely on <code>init</code> begin called before. Should call <code>
	 * execute</code> when finished(e.g. because time is over). If <code>
	 * hasFinished()</code> is true, it should return immediately
	 * 
	 * @param time
	 *            the current time [ms], used for determining what action is to
	 *            be taken
	 * @param ticks
	 *            the current time in ticks, used for determining what action is
	 *            to be taken
	 * @see animal.animator.Animator#hasFinished
	 */
	public void action(long time, double ticks) {
		int totalTimeOrTicks = getDuration();
		double factor;
		if (hasFinished())
			return;
		double elapsed = (isUnitIsTicks() ? ticks : time)
				- getStartTimeOrTicks() - getOffset();
		factor = elapsed / totalTimeOrTicks;
		if ((totalTimeOrTicks == 0 && elapsed >= 0) || (factor >= 1))
			execute();
		else if (factor < 0)// not yet time to do anything! Offset not yet over
			return;
		else {
			Object newProperty = null;
      String methodName = getMethod();
      Color targetColor = getColor();
      if (objects == null)
        return;
			for (int a = 0; a < objects.length; a++) {
				Color origColor = null;
				if (originalColor != null && a < originalColor.length
						&& originalColor[a] != null)
					origColor = originalColor[a];
				else 
          if (objects[a] != null)
            origColor = objects[a].getProperties().getColorProperty(
                methodName, Color.black);
          else
            origColor = Color.BLACK;
        newProperty = interpolateColor(origColor, targetColor, factor);
				objects[a].propertyChange(new PropertyChangeEvent(this,
						getMethod(), oldProperty, newProperty));
				oldProperty = newProperty;
			}
		}
	}

  private Color interpolateColor(Color originalColor, 
      Color targetColor, double factor) {
    double redValue = (factor * targetColor.getRed()) 
        + (1 - factor) * originalColor.getRed();
    double greenValue = (factor * targetColor.getGreen()) 
        + (1 - factor) * originalColor.getGreen();
    double blueValue = (factor * targetColor.getBlue()) 
        + (1 - factor) * originalColor.getBlue();
    
    return new Color((int)redValue, (int)greenValue, (int)blueValue);
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
		return "ColorChange";
	}

	/**
	 * Returns the color to change to
	 * 
	 * @return the color to change to
	 */
	public Color getColor() {
		return getProperties().getColorProperty(COLOR_LABEL, Color.black);
	}

	/***************************************************************************
	 * retrieve the file version for this animator This constant is used for the
	 * file version. It must be incremented whenever the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>original release</li>
	 * <li>now allows for smooth color transformations</li>
	 * </ol>
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public int getFileVersion() {
		return 2;
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
		int[] objectNums = getObjectNums();
		if (objectNums == null || objectNums.length == 0)
			return getColor();
    // TODO
//		if (originalColor == null)
//      storeOriginalColors();
//    return interpolateColor(originalColor[0], getColor(), factor);
		return getColor();
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
		return new String[] { "ColorChange" };
	}

	/**
	 * Set the color to change to
	 * 
	 * @param color
	 *            the target color to change to
	 */
	public void setColor(Color color) {
		getProperties().put(COLOR_LABEL, color);
	}

	/**
	 * Set the original color before the change
	 * 
	 * @param color
	 *            the target color to change to
	 */
	public void setOriginalColor(Color color) {
		int[] objectNums = getObjectNums();
		if (objectNums != null && objectNums.length != 0) {
			originalColor = new Color[objectNums.length];
			for (int i = 0; i < originalColor.length; i++)
				originalColor[i] = color;
		}
	}
  
  /**
   * Store the original color before the change
   */
  public void storeOriginalColors() {
    int[] objectNums = getObjectNums();
    if (objectNums != null && objectNums.length > 0) {
      originalColor = new Color[objectNums.length];
      String methodName = getMethod();
      for (int i = 0; i < originalColor.length; i++) {
        if (objects != null && objects.length > i && objects[i] != null) {
          XProperties xp = objects[i].getProperties();
          originalColor[i] = xp.getColorProperty(methodName, Color.black);
        }
        else
          originalColor[i] = Color.black;
      }
          
    }
  }


	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		setColor(null);
		super.discard();
	}

	/**
	 * Return a String representation of this object.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		return "set color(" + getMethod() + ") to "
				+ ColorChoice.getColorName(getColor()) + " on "
				+ super.toString();
	}
}
