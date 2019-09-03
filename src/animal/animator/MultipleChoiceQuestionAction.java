package animal.animator;

import java.util.Hashtable;

import animal.main.Animal;
import animal.misc.InteractionInterface;
import animal.misc.MessageDisplay;
import animal.misc.MultipleChoiceInterface;

/**
 * Action that supports popping up multiple choice questions
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-09-30
 */
public class MultipleChoiceQuestionAction extends QuestionAction implements
		PerformableAction {
	// =================================================================
	//                           Constants
	// =================================================================

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
//	private static final long serialVersionUID = -2375728076191469987L;

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "MultipleChoiceQuestion";

	// =================================================================
	//                             ATTRIBUTES
	// =================================================================

	/**
	 * The number of answers
	 */
	int answerCounter = 0;

	/**
	 * The Hashtable containing the possible answers to the question
	 */
	private Hashtable<String, String> answers = 
		new Hashtable<String, String>(17);

	private boolean constructed = false;

	/**
	 * The number of correct answers
	 */
	int correctAnswerIndex = 0;

	private MultipleChoiceInterface handler = null;

	/**
	 * The prompt for the question / private String title;
	 */

	// =================================================================
	//                           CONSTRUCTORS
	// =================================================================
	/**
	 * Public(empty) constructor required for serialization.
	 */
	public MultipleChoiceQuestionAction() {
		this("none");
	}

	/**
	 * Construct a new entry with the given title
	 * 
	 * @param theTitle
	 *            the title of the question
	 */
	public MultipleChoiceQuestionAction(String theTitle) {
		setType(TYPE_MULTIPLE_CHOICE);
		setTitle(theTitle);
		handler = (MultipleChoiceInterface) Animal.getInteractionHandler()
				.getHandlerFor(InteractionInterface.MULTIPLE_CHOICE, theTitle);
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
		answerCounter++;
		answers.put(String.valueOf(answerCounter), answerText);
		handler.SetPossibleAnswer(answerText.trim());
	}

	/**
	 * Mark the answer at the given index as correct(there may be more than
	 * one!)
	 * 
	 * @param nrOfCorrectAnswer
	 *            the index of the correct answer
	 */
	public void setCorrectAnswer(int nrOfCorrectAnswer) {
		if (correctAnswerIndex <= answers.size())
			correctAnswerIndex = nrOfCorrectAnswer;
		else
			MessageDisplay.errorMsg("invalidCorrectAnswerException",
					new Object[] { String.valueOf(correctAnswerIndex),
							String.valueOf(answers.size()) });
		handler.SetCorrectAnswer(nrOfCorrectAnswer - 1);
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
		sb.append("Multiple Choice question: '").append(getQuestionText());
		int nrAnswers = answers.size();
		sb.append("' answers: [").append(nrAnswers).append("]: {");
		for (int i = 1; i <= nrAnswers; i++)
			sb.append("{ ").append(i).append(". '").append(
					answers.get(String.valueOf(i))).append("'}");
		sb.append("} correct: ").append(correctAnswerIndex);
		return sb.toString();
	}
}
