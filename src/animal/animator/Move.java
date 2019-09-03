package animal.animator;

import java.awt.Point;

import animal.main.AnimationState;
import animal.misc.XProperties;

/**
 * Animator that moves an object or a point of an object etc. The exact
 * operation depends on the methods the objects return as result of <code>
 * getMethod</code>.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.2 2000-06-30
 */
public class Move extends TimedAnimator { //implements Externalizable {
	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The attribute name of the move base
	 */
	public static final String MOVE_BASE_LABEL = "moveBaseNum";

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "Move";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 5363620504730673229L;

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * the length of moveBase
	 */
	private int length;

	/**
	 * The path along which the Move has to be done. Must be calculated from
	 * <em>moveBaseNum</em> in <code>init</code> and is not valid until
	 * then.
	 */
	private MoveBase moveBase = null;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public Move() {
		// do nothing; only used for serialization
	}

	/**
	 * Construct a new Move from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param moveBaseNum
	 *            the number of the path for this move
	 */
	public Move(int step, int objectNum, int totalTimeOrTicks, String method,
			int moveBaseNum) {
		this(step, new int[] { objectNum }, totalTimeOrTicks, method,
				moveBaseNum);
	}

	/**
	 * Construct a new Move for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param moveBaseNum
	 *            the number of the path for this move
	 */
	public Move(int step, int[] objectNums, int totalTimeOrTicks,
			String method, int moveBaseNum) {
		super(step, objectNums, totalTimeOrTicks, method);
		setMoveBaseNum(moveBaseNum);
	}

	public Move(XProperties props) {
		setProperties(props);
	}

	// =================================================================
	//                               INITIALIZATION
	// =================================================================

	/**
	 * Initializes the Animator by constructing the objects from their object
	 * numbers. This can only be done here, as the AnimationState object in
	 * which this Animator is to execute now is only available here.
	 * 
	 * @param animationState
	 *            the animation state in which the animator is placed
	 * @param time
	 *            the duration of this animator in ms
	 * @param ticks
	 *            the duration of this animator in ticks
	 */
	public void init(AnimationState animationState, long time, double ticks) {
		//     moveBase must be initialized before calling super.init, as
		//     that procedure calls getProperty, which results in a
		//     dummy element being created. This dummy would then be
		//     used as start value for the animation resulting in a
		//     wrong animation!
		// construct the temporary object from its number
		moveBase = (MoveBase) animationState.getCloneByNum(getMoveBaseNum());
		// and initialize the other values.
		if (moveBase != null)
			length = moveBase.getLength();
		// always call super
		super.init(animationState, time, ticks);
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
		return "Move";
	}

	/**
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
	 * Returns the numerical ID of the move base for this Move.
	 * 
	 * @return the ID of the move base
	 */
	public int getMoveBaseNum() {
		return getProperties().getIntProperty(MOVE_BASE_LABEL);
	}

	/*
	 * public int[] getSpecialObjectNums() { return new int[]{getMoveBaseNum()}; }
	 */

	/**
	 * Returns the property at a certain time. <code>getProperty</code> <em>
	 * must</em> return a property of the "normal" type(i.e. Move must always
	 * return a Point), even if the object is not completely initialized(then
	 * return a dummy!), as <code>TimedAnimatorEditor</code> relies on
	 * receiving a property for querying the possible methods.
	 * 
	 * @param factor
	 *            a value between 0 and 1, indicating how far this animator has
	 *            got(0: start, 1: end)
	 * @return the property at this time
	 */
	public Object getProperty(double factor) {
		if (moveBase != null)
			return moveBase.getPointAtLength((int) (factor * length));

		// return a property even if the object is not initialized!
		// TimedAnimatorEditor depends on getting a reasonable property
		// for querying the object's possible methods.
		return new Point(0, 0);
	}

	/**
	 * Returns the temporary objects of this Animator. Temporary object are
	 * required for this Animation, but are not animated themselves: for
	 * example, move paths. These are passed as an <code>int[]</code> and not
	 * as PTGraphicObject[], as resetting the numbers of the temporary objects
	 * in the Animators doesn't change the objects but only the numbers. The
	 * objects are not changed until the animator is reinitialized.
	 * 
	 * @return an array containing the numerics IDs of the temporary objects;
	 *         <code>null</code> if no temporary objects are used. Here, the
	 *         array only contains the ID of the <code>moveBase</code>.
	 */
	public int[] getTemporaryObjects() {
		return new int[] { getMoveBaseNum() };
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
		return new String[] { "Move" };
	}

	/**
	 * Set the move base ID to the number passed.
	 * 
	 * @param targetMoveBaseNum
	 *            the numerical ID of the move base
	 */
	public void setMoveBaseNum(int targetMoveBaseNum) {
		getProperties().put(MOVE_BASE_LABEL, targetMoveBaseNum);
	}

	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		moveBase = null;
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
		if (getMoveBaseNum() <= usedValues.length)
			usedValues[getMoveBaseNum() - 1] = true;
	}

	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 */
	public String toString() {
		return "Move(" + getMethod() + ") " + super.toString() + " via "
				+ getMoveBaseNum();
	}
} // Move
