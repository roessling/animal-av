package animal.animator;

import animal.graphics.PTPoint;
import animal.main.AnimationState;

/**
 * Animator that scales GraphicObjects.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class Scale extends TimedAnimator { // implements Externalizable {

	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "Scale";

	/**
	 * The label used for accessing the rotation center point
	 */
	public static final String CENTER_LABEL = "center";

	/**
	 * The label used for accessing the x scale factor
	 */
	public static final String X_FACTOR_LABEL = "xFactor";

	/**
	 * The label used for accessing the y scale factor
	 */
	public static final String Y_FACTOR_LABEL = "yFactor";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 6013542892842131526L;

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The center point for the rotation
	 */
	private PTPoint center;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization
	 */
	public Scale() {
		// do nothing; only used for serialization
	}

	/**
	 * Construct a new Scale animator for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param centerNum
	 *            the numeric ID of the scale center
	 * @param xFactor
	 *            the x scale factor
	 * @param yFactor
	 *            the y scale factor
	 */
	public Scale(int step, int[] objectNums, int totalTimeOrTicks,
			int centerNum, double xFactor, double yFactor) {
		super(step, objectNums, totalTimeOrTicks, "scale");
		getProperties().put(CENTER_LABEL, centerNum);
		getProperties().put(X_FACTOR_LABEL, xFactor);
		getProperties().put(Y_FACTOR_LABEL, yFactor);
	}

	// =================================================================
	//                               INITIALIZATION
	// =================================================================

	/**
	 * prepares the Animator for use by the AnimationState and initializes it.
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
	 * @see Scale#init(AnimationState, long, double)
	 */
	public void init(AnimationState animationState, long time, double ticks) {
		super.init(animationState, time, ticks);
		center = (PTPoint) animationState.getCloneByNum(getCenterNum());
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
		return "Scale";
	}

	public int getCenterNum() {
		return getProperties().getIntProperty(CENTER_LABEL, -1);
	}

	public void setCenterNum(int objectNum) {
		getProperties().put(CENTER_LABEL, objectNum);
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
	 * return a property of the "normal" type(i.e. Move must always return a
	 * Point), even if the object is not completely initialized(then return a
	 * dummy!), as TimedAnimatorEditor relies on receiving a property for
	 * querying the possible methods.
	 * 
	 * @param factor
	 *            a value between 0 and 1, indicating how far this animator has
	 *            got(0: start, 1: end)
	 * @return the object at the given time.
	 * @see animal.editor.animators.TimedAnimatorEditor
	 */
	public Object getProperty(double factor) {
	  double theF = factor;
		if (Math.abs(factor) < 1f / 10000)
		  theF = 1f / 10000;
		return new ScaleParams(center, theF * getXScaleFactor(), theF
				* getYScaleFactor());
	}

	public String getType() {
		return TYPE_LABEL;
	}

	public double getXScaleFactor() {
		return getProperties().getDoubleProperty(X_FACTOR_LABEL, 1.0f);
	}

	public double getYScaleFactor() {
		return getProperties().getDoubleProperty(Y_FACTOR_LABEL, 1.0f);
	}

	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "Scale" };
	}

	public void setXScaleFactor(double factor) {
		getProperties().put(X_FACTOR_LABEL, factor);
	}

	public void setYScaleFactor(double factor) {
		getProperties().put(Y_FACTOR_LABEL, factor);
	}

	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		return "scale " + super.toString() + " by (" + getXScaleFactor() + ", "
				+ getYScaleFactor() + ") centered on " + getCenterNum();
	}
}
