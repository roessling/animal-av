package animal.animator;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.misc.InteractionInterface;
import animal.misc.MessageDisplay;
import animal.misc.TrueFalseQuestionInterface;

/**
 * Action that supports popping up "true/false" questions
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-09-30
 */
public class TrueFalseQuestionAction extends QuestionAction implements
		PerformableAction { // , Externalizable
	// =================================================================
	//                             CONSTANTS
	// =================================================================

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
//	private static final long serialVersionUID = 6655967163100757977L;

	/**
	 * The name of the animator
	 */
	public static final String TYPE_LABEL = "TrueFalseQuestion";

	// =================================================================
	//                             ATTRIBUTES
	// =================================================================

	/**
	 * The number of answers / int answerCounter = 0;
	 */

	/**
	 * The Hashtable containing the possible answers to the question
	 */
//	private Hashtable answers = new Hashtable(17);

	/**
	 * Determine if the answer is correct / private boolean answerIsCorrect =
	 * false;
	 */

	private boolean constructed = false;

	private TrueFalseQuestionInterface handler = null;

	// =================================================================
	//                           CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public TrueFalseQuestionAction() {
		this("none");
	}

	/**
	 * Construct a new entry with the given title
	 * 
	 * @param theTitle
	 *            the title of the question
	 */
	public TrueFalseQuestionAction(String theTitle) {
		setType(TYPE_TRUE_FALSE);
		setTitle(theTitle);
		handler = (TrueFalseQuestionInterface) Animal.getInteractionHandler()
				.getHandlerFor(InteractionInterface.TRUE_FALSE, theTitle);
	}

	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================

	/**
	 * Perform the action by showing the target URL in an HTML frame
	 */
	public void perform() {
		if (Animal.animationLoadFinished()) {
			MessageDisplay.errorMsg(AnimalTranslator.translateMessage("questionText",
					new String[] {getQuestionText()}), MessageDisplay.INFO);
			Animal.getInteractionHandler().initialize(handler, constructed,
					getQuestionText());
			constructed = true;
			Animal.getInteractionHandler().performQuestionOperation(handler);
		}
	}

	// =================================================================
	//                        ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Add a new answer possibility
	 * 
	 * @param answerText
	 *            the text of the answer to be added
	 */
	public void addAnswer( String answerText) {
		MessageDisplay.errorMsg("doNotInvokeMethodException", new Object[] {
				"addAnswer", getClass().getName() });
	}

	/*
	 * public String getType() { return TYPE_LABEL; }
	 */

	/**
	 * Set the truth value for the question
	 * 
	 * @param isCorrect
	 *            the truth value of the question
	 */
	public void setCorrectAnswer(boolean isCorrect) {
		getProperties().put(ANSWER_CORRECT_TAG, isCorrect);
		handler.SetAnswer(isCorrect);
	}

	// =================================================================
	//                               I/O
	// =================================================================

	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(400);
		sb.append("True / False Question: '").append(getQuestionText());
		sb.append("' [");
		if (!getProperties().getBoolProperty(ANSWER_CORRECT_TAG, false))
			sb.append("in");
		sb.append("correct]");
		return sb.toString();
	}
}
