package animal.animator;

import java.util.Hashtable;

import animal.main.Animal;
import animal.misc.FillInBlanksInterface;
import animal.misc.InteractionInterface;

/**
 * Action that supports popping up "fill in the blanks" questions
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-09-30
 */
public class FillInBlanksQuestionAction extends QuestionAction 
  implements PerformableAction { 
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
//	private static final long serialVersionUID = -3577126547590602694L;

	public static final String ANSWER_COUNTER_TAG = "answerCounter";

	public static final String CORRECT_ANSWER_COUNTER_TAG = "correctAnswerCounter";

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "FillInBlanksQuestion";

	// =================================================================
	//                             ATTRIBUTES
	// =================================================================

	/**
	 * The number of answers / int answerCounter = 0;
	 */

	/**
	 * The Hashtable containing the possible answers to the question
	 */
	private Hashtable<String, String> answers = 
		new Hashtable<String, String>(17);

	private FillInBlanksInterface handler = null;

	/**
	 * Determine if the element has been constructed
	 */
	boolean constructed = false;

	/**
	 * The number of correct answers / int correctAnswerCounter = 0;
	 */

	/**
	 * The Hashtable containing the correct answer(s) to the question
	 */
	private Hashtable<String, String> correctAnswers = 
		new Hashtable<String, String>(17);

	/**
	 * The prompt for the question / private String title;
	 */

	// =================================================================
	//                           CONSTRUCTORS
	// =================================================================
	/**
	 * Public(empty) constructor required for serialization.
	 */
	public FillInBlanksQuestionAction() {
		this("none");
	}

	/**
	 * Construct a new entry with the given title
	 * 
	 * @param theTitle
	 *            the title of the question
	 */
	public FillInBlanksQuestionAction(String theTitle) {
		setType(TYPE_FILL_IN_BLANKS);
		setTitle(theTitle);
		handler = (FillInBlanksInterface) Animal.getInteractionHandler()
				.getHandlerFor(InteractionInterface.FILL_IN_BLANKS, theTitle);
	}

	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================

	/**
	 * Perform the action by showing the target URL in an HTML frame
	 */
	public void perform() {
		Animal.getInteractionHandler().initialize(handler, constructed,
				getQuestionText());
		constructed = true;
		Animal.getInteractionHandler().performQuestionOperation(handler);
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
	public void addAnswer(String answerText) {
		int answerCounter = getProperties().getIntProperty(ANSWER_COUNTER_TAG,
				0) + 1;
		getProperties().put(ANSWER_COUNTER_TAG, answerCounter);
		answers.put(String.valueOf(answerCounter), answerText);
		handler.AddAnswer(answerText);
	}

	/**
	 * Add a new correct answer
	 * 
	 * @param correctAnswerText
	 *            the text of one of the answers
	 */
	public void addCorrectFIBAnswer(String correctAnswerText) {
		int correctAnswerCounter = getProperties().getIntProperty(
				CORRECT_ANSWER_COUNTER_TAG, 0) + 1;
		getProperties().put(ANSWER_COUNTER_TAG, correctAnswerCounter);
		correctAnswers.put(String.valueOf(correctAnswerCounter),
				correctAnswerText);
		handler.AddAnswer(correctAnswerText);
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
		sb.append("Fill In the Blanks: question: '").append(getQuestionText())
				.append("'");
		int nrAnswers = answers.size();
		sb.append(" Possible answers: [").append(nrAnswers).append("]: {");
		for (int i = 1; i <= nrAnswers; i++)
			sb.append("{").append(i).append(". '").append(
					answers.get(String.valueOf(i))).append("'} ");
		sb.append("}");
		return sb.toString();
	}
}
