package animal.animator;

import java.beans.PropertyChangeEvent;

import animal.main.AnimationState;
import animal.misc.XProperties;

/**
 * Animator that changes a GraphicObject's color.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class DepthChanger extends TimedAnimator {

	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The label of the property -- always use this constant for access!
	 */
	public static final String DEPTH_LABEL = "DepthChanger.depth";

	/**
	 * The type label of this animator
	 */
	public static final String TYPE_LABEL = "DepthChanger";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 5575863252518931027L;

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The original color(s), used for a gradual color change
	 */
	private int[] originalDepths = null;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public DepthChanger() {
		// do nothing; only used for serialization
	}
	
	/**
	 * Construct a new DepthChanger from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param depth
	 *            the target depth to change to for this animator
	 */
	public DepthChanger(int step, int objectNum, int totalTimeOrTicks,
			String method, int depth) {
		this(step, new int[] { objectNum }, totalTimeOrTicks, method, depth);
	}

	/**
	 * Construct a new DepthChanger for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param depth
	 *            the target depth to change to for this animator
	 */
	public DepthChanger(int step, int[] objectNums, int totalTimeOrTicks,
			String method, int depth) {
		super(step, objectNums, totalTimeOrTicks, method);
		setDepth(depth);
	}

	/**
	 * creates a new DepthChanger based on the properties passed in
	 * 
	 * @param props
	 *            the properties on which to base this animator
	 */
	public DepthChanger(XProperties props) {
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
			originalDepths = new int[objects.length];

			for (int i = 0; i < originalDepths.length; i++) {
				originalDepths[i] = objects[i].getDepth();
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
	 * @see animal.animator.Animator#hasFinished()
	 */
	public void action(long time, double ticks) {
		double elapsed;
		int totalTimeOrTicks = getDuration();
		float factor;

		if (hasFinished()) {
			return;
		}

		elapsed = (isUnitIsTicks() ? ticks : time) - getStartTimeOrTicks()
				- getOffset();
		factor = (float) elapsed / totalTimeOrTicks;

		if (((totalTimeOrTicks == 0) && (elapsed >= 0)) || (factor >= 1)) {
			execute();
		} else if (factor < 0) { // not yet time to do anything! Offset not yet
								 // over

			return;
		} else {
			Object newProperty = null;

			for (int a = 0; a < objects.length; a++) {
				int origDepth = objects[a].getDepth();
				int newDepth = getDepth();
				int interpolatedDepth = origDepth
						+ (int) (factor * (origDepth - newDepth));
				objects[a].propertyChange(new PropertyChangeEvent(this,
						getMethod(), new Integer(origDepth), 
						new Integer(interpolatedDepth)));
				oldProperty = newProperty;
			}
		}
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
		return "DepthChange";
	}

	/**
	 * Returns the color to change to
	 * 
	 * @return the color to change to
	 */
	public int getDepth() {
		return getProperties().getIntProperty(DEPTH_LABEL, Integer.MAX_VALUE);
	}

	/***************************************************************************
	 * retrieve the file version for this animator Note: this method must be
	 * overridden by all subclasses!
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
		int[] objectNums = getObjectNums();

		if ((objectNums == null) || (objectNums.length == 0)) {
			return new Integer(getDepth());
		}

		if (originalDepths == null) {
			originalDepths = new int[objectNums.length];

			for (int i = 0; i < originalDepths.length; i++)
				originalDepths[i] = objects[i].getDepth();
		}

		return new Integer(getDepth());
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
		return new String[] { "DepthChange" };
	}

	/**
	 * Set the color to change to
	 * 
	 * @param depth
	 *            the target depth to change to
	 */
	public void setDepth(int depth) {
		getProperties().put(DEPTH_LABEL, depth);
	}

	/**
	 * Set the original color before the change
	 * 
	 * @param depth
	 *            the target depth to change to
	 */
	public void setOriginalDepths(int depth) {
		int[] objectNums = getObjectNums();

		if ((objectNums != null) && (objectNums.length != 0)) {
			originalDepths = new int[objectNums.length];

			for (int i = 0; i < originalDepths.length; i++)
				originalDepths[i] = depth;
		}
	}

	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		super.discard();
	}

	/**
	 * Return a String representation of this object.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		return "set depth to " + getDepth() + " on " + super.toString();
	}
}
