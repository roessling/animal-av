package animal.animator;

import java.beans.PropertyChangeEvent;

import animal.main.AnimationState;
import animal.misc.MessageDisplay;

/**
 * An Animator that is not executed at once but in a certain time. The time may
 * begiven in milliseconds [ms] or in a certain amount of ticks. We earnestly
 * <em>recommend</em> using <code>ticks</code>, not "hard" timing on a
 * <code>ms</code> base. This is due to the different speeds of Java VMs. If
 * the current VM lags behind the timing, the animation will "jump".
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.2 2000-09-30
 */
public abstract class TimedAnimator extends Animator {
	// implements Externalizable {

	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The label used for accessing the duration property
	 */
	public static final String DURATION_LABEL = "TimedAnimator.duration";

	/**
	 * The label used for accessing the offset property
	 */
	public static final String OFFSET_LABEL = "TimedAnimator.offset";

	/**
	 * The label used for accessing the time unit property
	 */
	public static final String TIME_UNIT_LABEL = "TimedAnimator.useTicks";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = -6987994586122663241l;

	// =================================================================
	//                               TRANSIENTS
	// =================================================================

	/**
	 * Determines the last property sent to the GraphicObject. As most Animators
	 * work with relative properties, the old properties must be passed, too.
	 * This method is accessed from Scale for initialization
	 * 
	 * @see animal.animator.Scale#init(AnimationState, long, double)
	 */
	protected transient Object oldProperty = null;

	/**
	 * Determines when the object was initialized
	 */
	protected transient long startTimeOrTicks = 0;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public TimedAnimator() {
		// do nothing; only used for serialization
	}

	/**
	 * Construct a new animator for a given step and the given object ID.
	 * 
	 * By default, it works on ticks. If it is to work on time [ms], <code>
	 * setUnitIsTicks</code> is to be called.
	 * 
	 * @see #setUnitIsTicks(boolean)
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param method
	 *            the method to be used; depends on the animator.
	 */
	public TimedAnimator(int step, int objectNum, int totalTimeOrTicks,
			int timeOrTicksOffset, String method) {
		this(step, new int[] { objectNum }, totalTimeOrTicks,
				timeOrTicksOffset, method);
	}

	/**
	 * Construct a new animator for a given step and the given object IDs.
	 * 
	 * By default, it works on ticks. If it is to work on time [ms], <code>
	 * setUnitIsTicks</code> is to be called.
	 * 
	 * @see #setUnitIsTicks(boolean)
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param method
	 *            the method to be used; depends on the animator.
	 */
	public TimedAnimator(int step, int[] objectNums, int totalTimeOrTicks,
			int timeOrTicksOffset, String method) {
		super(step, objectNums);
		setDuration(totalTimeOrTicks);
		setOffset(timeOrTicksOffset);
		setMethod(method);
	}

	/**
	 * Construct a new animator at start of a given step and the given object
	 * IDs. Note that the <em>offset</em> is set to 0 for this animator.
	 * 
	 * By default, it works on ticks. If it is to work on time [ms], <code>
	 * setUnitIsTicks</code> is to be called.
	 * 
	 * @see #setUnitIsTicks(boolean)
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param method
	 *            the method to be used; depends on the animator.
	 */
	public TimedAnimator(int step, int[] objectNums, int totalTimeOrTicks,
			String method) {
		this(step, objectNums, totalTimeOrTicks, 0, method);
	}

	// =================================================================
	//                               INITIALIZATION
	// =================================================================

	/**
	 * Copy the timing for this operation from the animator pased in.
	 * 
	 * @param animator
	 *            the base animator from which the time is to be copied.
	 */
	public void copyTimingFrom(TimedAnimator animator) {
		setOffset(animator.getOffset());
		setDuration(animator.getDuration());
		setUnitIsTicks(animator.isUnitIsTicks());
	}

	/**
	 * Prepares the Animator for use by the AnimationState and initializes it.
	 * If any actions have to be done <em>before</em>
	 * <code>action</code> is
	 * called, it has to be done here.
	 * 
	 * This method should be called by subclasses. But as this method calls
	 * <code>getProperty</code>, it must be made sure that a valid property
	 * is returned <code>super.init</code> is called.
	 * 
	 * @param animationState
	 *            the animation state in which the animator is placed
	 * @param time
	 *            the duration of this animator in ms
	 * @param ticks
	 *            the duration of this animator in ticks
	 * 
	 * @see Scale#init(AnimationState, long, double)
	 * @see animal.animator.Animator#hasFinished()
	 */
	public void init(AnimationState animationState, long time, double ticks) {
		super.init(animationState, time, ticks);
		startTimeOrTicks = (isUnitIsTicks() ? Math.round(ticks) : time);
		oldProperty = getProperty(0);
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
		double factor;
		if (hasFinished())
			return;
		double elapsed = (isUnitIsTicks() ? ticks : time) - startTimeOrTicks
				- getOffset();
		factor = elapsed / getDuration();
		if ((getDuration() == 0 && elapsed >= 0) || (factor >= 1))
			execute();
		else if (factor < 0)
			return;
		else {
			Object newProperty = getProperty(factor);
			for (int a = 0; a < objects.length; a++)
				if (objects[a] != null)
					objects[a].propertyChange(new PropertyChangeEvent(this,
							getMethod(), oldProperty, newProperty));
			oldProperty = newProperty;
		}
	}

	/**
	 * Changes the state of the GraphicObject to its final state after
	 * completely executing the Animator. Must be overwritten and called by
	 * subclasses. The object must have been initialized before but some actions
	 * may have been done meanwhile. Should be called at the end of <code>
	 * action</code>
	 * 
	 * @see #action(long, double)
	 */
	public void execute() {
		super.execute();
		if (oldProperty == null)
			oldProperty = getProperty(0);
		Object newProperty = getProperty(1);
		if (objects != null && objects.length > 0) {
			for (int a = 0; a < objects.length; a++)
				if (objects[a] != null) {
					objects[a].propertyChange(new PropertyChangeEvent(this,
							getMethod(), oldProperty, newProperty));
				}
		} else
			MessageDisplay.errorMsg("noObjectsSetException", new Object[] {
					String.valueOf(getStep()),
					getAnimatorName() + " -- " + toString() });
	}

	// =================================================================
	//                       ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Returns the duration of the current animation effect
	 * 
	 * @return the duration; use <code>isUnitIsTicks </COD> to determine
	 *         whether it is on a ticks or ms basis.
	 */
	public int getDuration() {
		return getProperties().getIntProperty(DURATION_LABEL, 0);
	}

	/***************************************************************************
	 * retrieve the file version for this animator It must be incremented
	 * whenever the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>original release</li>
	 * <li>supports working on several objects instead of only one</li>
	 * </ol>
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public int getFileVersion() {
		return 2;
	}

	/**
	 * Returns the method to be used for the animator.
	 * 
	 * @return the name of the animator method
	 */
	public String getMethod() {
		return getProperties().getProperty(METHOD_LABEL);
	}

	/**
	 * Returns the offset of the current animation effect
	 * 
	 * @return the offset; use <code>isUnitIsTicks </COD> to determine whether
	 *         it is on a ticks or ms basis.
	 */
	public int getOffset() {
		return getProperties().getIntProperty(OFFSET_LABEL, 0);
	}

	/**
	 * Returns the property at a certain time.
	 * 
	 * @param factor
	 *            a value between 0 and 1, indicating how far this animator has
	 *            got(0: start, 1: end)
	 * @return getProperty <em>must</em> return a property of the "normal"
	 *         type(i.e. Move must always return a Point), even if the object is
	 *         not completely initialized(then return a dummy!), as
	 *         TimedAnimatorEditor relies on receiving a property for querying
	 *         the possible methods.
	 */
	public abstract Object getProperty(double factor);

	/**
	 * Returns the duration of the current animation effect
	 * 
	 * @return the duration; use <code>isUnitIsTicks </COD> to determine
	 *         whether it is on a ticks or ms basis. / public int getTotal() {
	 *         return getDuration(); }
	 */

	/**
	 * Returns the start time of the current animation effect
	 * 
	 * @return the start; use <code>isUnitIsTicks </COD> to determine whether
	 *         it is on a ticks or ms basis.
	 */
	public long getStartTimeOrTicks() {
		return startTimeOrTicks;
	}

	/**
	 * Returns <code>true</code> if ticks are used, <code>false</code> for
	 * ms.
	 * 
	 * @return <code>true</code> if ticks are used, <code>false</code> for
	 *         ms.
	 */
	public boolean isUnitIsTicks() {
		return getProperties().getBoolProperty(TIME_UNIT_LABEL, true);
	}

	/**
	 * Sets the duration of the current animation effect
	 * 
	 * @param total
	 *            the duration; use <code>isUnitIsTicks </COD> to determine
	 *            whether it is on a ticks or ms basis.
	 */
	public void setDuration(int total) {
		getProperties().put(DURATION_LABEL, total);
	}

	/**
	 * Sets the method to be used for the animator.
	 * 
	 * @param method
	 *            the name of the animator method
	 */
	public void setMethod(String method) {
		getProperties().put(METHOD_LABEL, method);
	}

	/**
	 * Sets the offset of the current animation effect
	 * 
	 * @param offset
	 *            the offset; use <code>isUnitIsTicks </COD> to determine
	 *            whether it is on a ticks or ms basis.
	 */
	public void setOffset(int offset) {
		getProperties().put(OFFSET_LABEL, offset);
	}

	/**
	 * Sets the duration of the current animation effect
	 * 
	 * @param total
	 *            the duration; use <code>isUnitIsTicks </COD> to determine
	 *            whether it is on a ticks or ms basis. / public void
	 *            setTotal(int total) { setDuration(total); }
	 */

	/**
	 * Set time unit to ticks( <code>true</code>) or ms( <code>false</code>)
	 * 
	 * @param useTicks
	 *            if <code>true</code>, use ticks for the animator, else use
	 *            milliseconds.
	 */
	public void setUnitIsTicks(boolean useTicks) {
		getProperties().put(TIME_UNIT_LABEL, useTicks);
	}

	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		setMethod(null);
		oldProperty = null;
		super.discard();
	}


	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		StringBuilder showMe = new StringBuilder(super.toString());
		showMe.append(" within ").append(getDuration()).append(" ");
		showMe.append((isUnitIsTicks() ? "ticks" : "ms"));
		if (getOffset() != 0) {
			showMe.append(" starting after ").append(getOffset());
			showMe.append(" ").append((isUnitIsTicks() ? "ticks" : "ms"));
		}
		return showMe.toString();
	}
} // TimedAnimator
