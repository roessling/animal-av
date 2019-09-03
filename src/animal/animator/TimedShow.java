package animal.animator;

import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.main.AnimationState;
import animal.misc.XProperties;
import animalscript.core.AnimalParseSupport;

/**
 * Animator for timed showing or hiding GraphicObjects. This is achieved by
 * inserting them into or deleting them from a GraphicVector.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class TimedShow extends TimedAnimator {

	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The name of the animator
	 */
	public static final String TYPE_LABEL = "TimedShow";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 5575863619518931021L;

	// =================================================================
	//                               TRANSIENTS
	// =================================================================

	/**
	 * The <code>GraphicVector</code> on which the animator works.
	 */
	private transient GraphicVector graphicVector;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public TimedShow() {
		// do nothing; only used for serialization
	}

	/**
	 * Construct a new TimedShow animator for a given step and the given object
	 * ID.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param method
	 *            the name of the method to use, either "show" or "hide"
	 * @param show
	 *            if true, show the object; else hide them
	 */
	public TimedShow(int step, int objectNum, int totalTimeOrTicks,
			String method, boolean show) {
		this(step, new int[] { objectNum }, totalTimeOrTicks, method, show);
	}

	/**
	 * Construct a new TimedShow animator for a given step and the given object
	 * IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this effect
	 * @param method
	 *            the name of the method to use, either "show" or "hide"
	 * @param show
	 *            if true, show the object; else hide them
	 */
	public TimedShow(int step, int[] objectNums, int totalTimeOrTicks,
			String method, boolean show) {
		this(step, objectNums, totalTimeOrTicks, 0, method, show);
	}

	/**
	 * Construct a new TimedShow for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param offset
	 *            the offset for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param show
	 *           determines if the objects should be shown or hidden
	 */           
	public TimedShow(int step, int[] objectNums, int totalTimeOrTicks,
			int offset, String method, boolean show) {
		super(step, objectNums, totalTimeOrTicks, method);
		setShow(show);
		setOffset(offset);
		if (method == null)
			setMethod((show) ? "show" : "hide");
		else
			setMethod(method);
	}

	public TimedShow(XProperties props) {
		setProperties(props);
	}

	// =================================================================
	//                               INITIALIZATION
	// =================================================================

	/**
	 * Copy the timing for this operation from the animator pased in.
	 * 
	 * @param animationState
	 *            the current animation state
	 * @param time
	 *            the current point in time
	 * @param ticks
	 *            the current point in time measures by frames ("ticks")
	 */
	public void init(AnimationState animationState, long time, double ticks) {
		super.init(animationState, time, ticks);
		this.graphicVector = animationState.getCurrentObjects();
	}

	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================


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
		if (getMethod() != null && !getMethod().equalsIgnoreCase(getAnimatorName())) {
			return;
		}
		int a;
		if (objects != null && objects.length > 0) {
			if (isShow())
				for (a = 0; a < objects.length; a++)
					graphicVector.addElement(objects[a],
							GraphicVectorEntry.UNTOUCHED);
			else
				for (a = 0; a < objects.length; a++)
					graphicVector.removeElement(objects[a]);
			for (a = 0; a < objects.length; a++)
				AnimalParseSupport.getCurrentlyVisible().put(String.valueOf(objects[a].getNum(true)), String.valueOf(isShow()));
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
		if (isShow())
			return "show";
		return "hide";
//		return getProperties().getProperty(METHOD_LABEL, "Show");
	}

	/**
	 * This constant is used for the file version. It must be incremented
	 * whenever the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>original release</li>
	 * <li>supports units for the operations
	 * </ol>
	 * Note: this method must be overridden by all subclasses!
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public int getFileVersion() {
		return 2;
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
	public Object getProperty( double factor) {
		return new Boolean(false);
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
		return new String[] { "Show", "Hide" };
	}

	/**
	 * TimedShow does not change the object!
	 */
	public boolean isChangingAnimator() {
		return false;
	}

	/**
	 * Query whether the animator type is "show" or "hide"
	 * 
	 * @return <code>true</code> if the animator is in <em>show</em> mode;
	 *         <code>false</code> if the animator is in <em>hide</em> mode.
	 */
	public boolean isShow() {
		return getProperties().getProperty(METHOD_LABEL, "Show")
				.equalsIgnoreCase("Show");
	}

	/**
	 * Set the mode of the animator to "show" or "hide"
	 * 
	 * @param show
	 *            <code>true</code> if the animator is in <em>show</em>
	 *            mode; <code>false</code> if the animator is in <em>hide
	 *            </em> mode.
	 */
	public void setShow(boolean show) {
		getProperties().put(METHOD_LABEL, (show) ? "Show" : "Hide");
	}

	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		if (graphicVector != null) {
			graphicVector.removeAllElements();
			graphicVector = null;
		}
		super.discard();
	}


	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append(getAnimatorName());
		String showHideMethodName = getMethod();
		if (showHideMethodName != null 
				&& !showHideMethodName.equals("show")
				&& !showHideMethodName.equals("hide"))
			sb.append("(").append(showHideMethodName).append(")");
		sb.append(" ").append(super.toString());
		return sb.toString();
//		return getAnimatorName() +"(" + getMethod() + ") " + super.toString();
	}
}
