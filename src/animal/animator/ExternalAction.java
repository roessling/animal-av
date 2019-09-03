package animal.animator;

import java.util.Hashtable;

import translator.AnimalTranslator;


/**
 * Animator that supports external action events
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class ExternalAction extends Animator { 
	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * constant for documentation actions
	 */
	public static final int DOCUMENTATION_ACTION = 1;

	/**
	 * The label for the "documentation" action
	 */
	private static final String DOCUMENTATION_LABEL = "Documentation";

	/**
	 * constant for documentation actions
	 */
	public static final int QUESTION_ACTION = 2;

	/**
	 * The label for the "question" action
	 */
	private static final String QUESTION_LABEL = "Question";

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "ExternalAction";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 4275863619518931027L;

	/**
	 * The Hashtable containing the actions active in this animation
	 */
	private static Hashtable<String, PerformableAction> CurrentActions;

	/**
	 * The Hashtable containing the actions active in this animation
	 */
//	private static Hashtable PerformedActions;

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The parameter that is to be changed
	 */
	private String actionKey = null;

	/**
	 * The type of this action; one of the "*_ACTION" constants above
	 */
	private int type = DOCUMENTATION_ACTION;

	/**
	 * Determine if the action is in use
	 */
	public boolean used = false;

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public ExternalAction() {
		// do nothing; only used for serialization
	} // for serialization

	/**
	 * Construct a new ExternalAction from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param theType
	 *            the type of the action to be inserted
	 * @param key
	 *            the key for the action, used for later referencing
	 * @param action
	 *            the action itself
	 */
	public ExternalAction(int step, int theType, String key,
			PerformableAction action) {
		super(step, new int[] { -1 });

		if (CurrentActions == null) {
			CurrentActions = new Hashtable<String, PerformableAction>(53);
		}

//		if (PerformedActions == null) {
//			PerformedActions = new Hashtable(53);
//		}

		actionKey = key;
		CurrentActions.put(key, action);
		type = theType;
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
		PerformableAction action = CurrentActions.get(actionKey);
		action.perform();
		finished = true;
//		execute();
	}


   // =================================================================
	 //  ATTRIBUTE GET/SET 
	 // =================================================================

  /** Returns the action with the given name
	 * 
	 * @param name
	 *            the name of the action
	 * @return the action associated with the name
	 * @throws IllegalArgumentException
	 *             if there is no action called "name"
	 */
	public static PerformableAction getActionNamed(String name)
			throws IllegalArgumentException {
		if (CurrentActions.containsKey(name)) {
			return CurrentActions.get(name);
		}

		throw new IllegalArgumentException(AnimalTranslator.translateMessage(
				"noSuchKeyException", new Object[] { name }));
	}

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public String getAnimatorName() {
		if (type == DOCUMENTATION_ACTION) {
			return DOCUMENTATION_LABEL;
		}

		if (type == QUESTION_ACTION) {
			return QUESTION_LABEL;
		}

		if ((actionKey != null) && CurrentActions.containsKey(actionKey)) {
			return CurrentActions.get(actionKey).toString();
		}

		return TYPE_LABEL;
	}

	/***************************************************************************
	 * retrieve the file version for this animator
	 * 
	 * It must be incremented whenever the save format is changed.
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
	 * returns the action key for this animator
	 * 
	 * @return the action key for this interaction
	 */
	public String getActionKey() {
		return actionKey;
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
	 * returns the type ID for this action type
	 * 
	 * @return the type ID, which must be one of the constant values defined in
	 *         this class: DOCUMENTATION_ACTION or QUESTION_ACTION
	 */
	public int getTypeID() {
		return type;
	}

	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "Question", "Documentation", TYPE_LABEL };
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
		StringBuilder sb = new StringBuilder(200);
		sb.append("external action");

		if ((actionKey != null) && CurrentActions.containsKey(actionKey)) {
				sb.append(": ").append(CurrentActions.get(actionKey).toString());
		} else {
			sb.append(" to ").append(actionKey).append(" on ").append(
					super.toString());
		}

		return sb.toString();
	}

	/**
	 * clears the set of current actions
	 * 
	 *  
	 */
	public static void clearActions() {
		if (CurrentActions == null)
			CurrentActions = new Hashtable<String, PerformableAction>();
		CurrentActions.clear();
	}

	/**
	 * resets all performed actions
	 */
	public static void resetActions() {
//		PerformedActions.clear();
	}

	/**
	 * returns the current table of supported actions
	 * 
	 * @return the Hashtable containing the current actions
	 */
	public static Hashtable<String, PerformableAction> getCurrentActions() {
		return CurrentActions;
	}
}
