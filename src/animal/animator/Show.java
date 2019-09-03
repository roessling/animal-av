package animal.animator;

import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.main.AnimationState;

/**
 * Animator for showing or hiding GraphicObjects. This is achieved by inserting
 * them into or deleting them from a GraphicVector.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class Show extends Animator { // implements Externalizable {

	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * This constant is used for the file version. It must be incremented
	 * whenever the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>original release</li>
	 * <li>supports working on several objects instead of only one</li>
	 * </ol>
	 */
	private static final int FILE_VERSION = 1;

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "Show";

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
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The type of the effect - show or hide.
	 */
	private boolean show;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public Show() {
		// do nothing; only used for serialization
	}

	/**
	 * Construct a new Show animator for a given step and the given object ID.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param isShow
	 *            if true, show the object; else hide them
	 */
	public Show(int step, int objectNum, boolean isShow) {
		this(step, new int[] { objectNum }, isShow);
	}

	/**
	 * Construct a new Show animator for a given step and the given object IDs.
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param isShow
	 *            if true, show the objects; else hide them
	 */
	public Show(int step, int[] objectNums, boolean isShow) {
		super(step, objectNums);
		show = isShow;
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
	 * @see animal.animator.Animator#hasFinished()
	 */
	public void init(AnimationState animationState, long time, double ticks) {
		super.init(animationState, time, ticks);
		this.graphicVector = animationState.getCurrentObjects();
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
	 * Note that as this operation is <em>untimed</em>, it always takes
	 * effect immediately!
	 * 
	 * @param time
	 *            the current time [ms], used for determining what action is to
	 *            be taken
	 * @param ticks
	 *            the current time in ticks, used for determining what action is
	 *            to be taken
	 * @see animal.animator.Animator#hasFinished()
	 */
	public void action( long time, 
			 double ticks) {
		execute();
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
		for (int a = 0; a < objects.length; a++)
			if (show)
				graphicVector.addElement(objects[a],
						GraphicVectorEntry.UNTOUCHED);
			else
				graphicVector.removeElement(objects[a]);
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
		return ((show) ? "Show" : "Hide");
	}

	/***************************************************************************
	 * retrieve the file version for this animator Note: this method must be
	 * overridden by all subclasses!
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public int getFileVersion() {
		return FILE_VERSION;
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
		return new String[] { "xxx" };
	}

	/**
	 * Determines whether this Animator changes its object.
	 * 
	 * @return <code>true</code> if the Animator(potentially) changes any of
	 *         the object attributes, <code>false</code> otherwise.
	 *         <p>
	 *         E.g. Show never changes attributes, with Move and Rotate it
	 *         depends on the start and end state of the object, therefore they
	 *         should be declared as changing Animators. If the object really is
	 *         the same as before the Animator's execution, this could be false,
	 *         but implementing this could be quite tricky to implement and
	 *         difficult to debug. This should be a static method, as being a
	 *         changing animator depends on the class and not on the instance!
	 *         But this is not possible as with a static method, always this
	 *         method is called, not that of the subclasses, if a variable is
	 *         declared of type Animator! So this method only has to be
	 *         overwritten if an Animator does not change its object's
	 *         attributes, as is the case here.
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
		return show;
	}

	/**
	 * Set the mode of the animator to "show" or "hide"
	 * 
	 * @param isShow
	 *            <code>true</code> if the animator is in <em>show</em>
	 *            mode; <code>false</code> if the animator is in <em>hide
	 *            </em> mode.
	 */
	public void setShow(boolean isShow) {
		show = isShow;
	}

	// =================================================================
	//                                   I/O
	// =================================================================

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		graphicVector.removeAllElements();
		graphicVector = null;
		super.discard();
	}


	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		if (show)
			return "show " + super.toString();
		return "hide " + super.toString();
	}
}
