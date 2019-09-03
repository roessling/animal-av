package animal.animator;

import interactionsupport.UnknownInteractionException;
import interactionsupport.controllers.InteractionController;
import translator.AnimalTranslator;
import animal.main.Animal;

/**
 * Animator that supports external action events
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class InteractionElement extends Animator { 
	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The mode for defining a new interaction element
	 */
	public static final int INTERACTION_DEFINITION = 1;

	/**
	 * The mode for invoking a concrete interaction element
	 */
	public static final int INTERACTION_INVOCATION = 2;

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "InteractionElement";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 4835863619518931027L;

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	/**
	 * The parameter that is to be changed
	 */
	private String actionKey = null;

	private InteractionController interactionHandler = null;

	private int interactionType = InteractionElement.INTERACTION_INVOCATION;

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
	public InteractionElement() {
		// do nothing; only used for serialization
	} // for serialization

	/**
	 * Construct a new ExternalAction from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param key
	 *            the key for the action, used for later referencing
	 */
	public InteractionElement(int step, String key, InteractionController module) {
		this(step, key, INTERACTION_INVOCATION, module);
	}

	/**
	 * Construct a new ExternalAction from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param key
	 *            the key for the action, used for later referencing
	 * @param mode
	 *            the interaction mode, either
	 *            InteractionElement.INTERACTION_DEFINITION or
	 *            InteractionElement.INTERACTION_INVOCATION
	 */
	public InteractionElement(int step, String key, int mode,
			InteractionController module) {
		super(step, new int[] { -1 });
		actionKey = key;
		interactionType = mode;
		interactionHandler = module;
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
		super.execute();
		if (interactionType != InteractionElement.INTERACTION_DEFINITION) {
			try {
			  if (Animal.animationLoadFinished()) {
			    interactionHandler.interaction(actionKey);
        }
			} catch (UnknownInteractionException uie) {
				AnimalTranslator.translateMessage("unknownInteractionElement",
						new String[] {actionKey});
			}
		}
	}

	public void execute() {
		// do nothing; only used for serialization
	}

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public String getAnimatorName() {
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

	public String getType() {
		return TYPE_LABEL;
	}
	
	public String getActionKey() {
	  return actionKey;
	}
	
	public int getInteractionType() {
	  return interactionType;
	}


	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "Interaction", TYPE_LABEL };
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
		sb.append("interaction");
		if (interactionType == InteractionElement.INTERACTION_DEFINITION)
			sb.append("Definition");
		sb.append(" \"").append(actionKey).append("\"");
		return sb.toString();
	}
}