package animal.animator;

import animal.graphics.PTPoint;
import animal.main.AnimationState;
import animal.misc.XProperties;

/**
 * Animator that rotates GraphicObjects around a Point about a certain angle.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class Rotate extends TimedAnimator { // implements Externalizable {

	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The label used for accessing the rotation center point
	 */
	public static final String CENTER_LABEL = "center";

	/**
	 * The label used for accessing the rotation degree
	 */
	public static final String DEGREES_LABEL = "degrees";

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "Rotate";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 6013542892925447746L;

	// =================================================================
	//                               TRANSIENTS
	// =================================================================

	/**
	 * The center point for the rotation
	 */
	private transient PTPoint center;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public Rotate() { // required for serialization
	}

	/**
	 * Construct a new Rotate animator for a given step and the given object ID.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param centerNum
	 *            the numeric ID of the rotation center
	 * @param degrees
	 *            the degree for the rotation, usually -360...360
	 */
	public Rotate(int step, int objectNum, int totalTimeOrTicks, int centerNum,
			int degrees) {
		this(step, new int[] { objectNum }, "rotate", totalTimeOrTicks,
				centerNum, degrees);
	}

	/**
	 * Construct a new Rotate animator for a given step and the given object
	 * IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param centerNum
	 *            the numeric ID of the rotation center
	 * @param degrees
	 *            the degree for the rotation, usually -360...360
	 */
	public Rotate(int step, int[] objectNums, int totalTimeOrTicks,
			int centerNum, int degrees) {
		this(step, objectNums, "rotate", totalTimeOrTicks, centerNum, degrees);
	}

	/**
	 * Construct a new Rotate animator for a given step and the given object ID.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param method
	 *            the name of the method used
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param centerNum
	 *            the numeric ID of the rotation center
	 * @param degrees
	 *            the degree for the rotation, usually -360...360
	 */
	public Rotate(int step, int objectNum, String method, int totalTimeOrTicks,
			int centerNum, int degrees) {
		this(step, new int[] { objectNum }, method, totalTimeOrTicks,
				centerNum, degrees);
	}

	/**
	 * Construct a new Rotate animator for a given step and the given object
	 * IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param method
	 *            the name of the method used
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param centerNum
	 *            the numeric ID of the rotation center
	 * @param degrees
	 *            the degree for the rotation, usually -360...360
	 */
	public Rotate(int step, int[] objectNums, String method,
			int totalTimeOrTicks, int centerNum, int degrees) {
		super(step, objectNums, totalTimeOrTicks, method);
		setCenterNum(centerNum);
		setDegrees(degrees);
	}

	public Rotate(XProperties props) {
		setProperties(props);
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
		return "Rotate";
	}

	/**
	 * Returns the numeric ID of the center object for this rotation.
	 * 
	 * @return the numeric ID of the rotation center.
	 */
	public int getCenterNum() {
		return getProperties().getIntProperty(CENTER_LABEL, 1);
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

	/*
	 * public int[] getSpecialObjectNums() { return new int[]{ getCenterNum() }; }
	 */

	/**
	 * Returns the degrees for this rotation.
	 * 
	 * @return the degrees for the rotation, usually -360...360
	 */
	public int getDegrees() {
		return getProperties().getIntProperty(DEGREES_LABEL, 360);
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
		if (center != null)
			//       angle is inverted as coordinates are mirrored!
			//       y-axis points down instead of up.
			return new Rotation(center, -factor * getDegrees() * Math.PI / 180);
		return new Rotation(new PTPoint(0, 0), 0);
	}

	/**
	 * Returns the temporary objects of this Animator. Temporary object are
	 * required for animation, but are not animated themselves: for example,
	 * move paths. These are passed as an <code>int[]</code> and not as
	 * PTGraphicObject[], as resetting the numbers of the temporary objects in
	 * the Animators doesn't change the objects but only the numbers. The
	 * objects are not changed until the animator is reinitialized.
	 * 
	 * @return an array containing the numerics IDs of the temporary objects;
	 *         <code>null</code> if no temporary objects are used.
	 */
	public int[] getTemporaryObjects() {
		return new int[] { getCenterNum() };
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
		return new String[] { "Rotate" };
	}

	/**
	 * Sets the numeric ID of the center object for this rotation.
	 * 
	 * @param centerNum
	 *            the numeric ID of the rotation center.
	 */
	public void setCenterNum(int centerNum) {
		getProperties().put(CENTER_LABEL, centerNum);
	}

	/**
	 * Sets the degrees for this rotation.
	 * 
	 * @param degrees
	 *            the degrees for the rotation, usually -360...360
	 */
	public void setDegrees(int degrees) {
		getProperties().put(DEGREES_LABEL, degrees);
	}

	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		center = null;
		super.discard();
	}

	/**
	 * Determine which objects are used(and thus prevented from deletion).
	 * 
	 * @param usedValues
	 *            updated with the info that the used objects must not be
	 *            deleted.
	 */
	public void scavenge(boolean[] usedValues) {
		super.scavenge(usedValues);
		usedValues[getCenterNum() - 1] = true;
	}

	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		return "rotate " + super.toString() + " for " + getDegrees()
				+ " degrees, centered " + getCenterNum();
	}
}
