package animal.animator;

import java.util.Hashtable;

import translator.AnimalTranslator;
import animal.main.PropertiedObject;
import animal.misc.MessageDisplay;

/**
 * Encapsulator for all kinds of questions prompted as an animation content
 * 
 * @see animal.animator.FillInBlanksQuestionAction
 * @see animal.animator.MultipleChoiceQuestionAction
 * @see animal.animator.TrueFalseQuestionAction
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-09-30
 */
public class QuestionAction extends PropertiedObject implements 
		PerformableAction { // , Externalizable

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
//	private static final long serialVersionUID = 8418489397704058957L;

	// =================================================================
	//                           CONSTANTS
	// =================================================================
	public static final String ANSWER_CORRECT_TAG = "QuestionAction.answerCorrect";

	public static final String QUESTION_TEXT = "QuestionAction.questionText";

	public static final String TITLE_TAG = "QuestionAction.title";

	/**
	 * The constant for the "fill in the blanks" question type
	 */
	public static final int TYPE_FILL_IN_BLANKS = 1;

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "QuestionAction";

	/**
	 * The constant for the "multiple choice" question type
	 */
	public static final int TYPE_MULTIPLE_CHOICE = 2;

	public static final String TYPE_TAG_LABEL = "QuestionAction";

	/**
	 * The constant for the "true/false" question type
	 */
	public static final int TYPE_TRUE_FALSE = 4;

	// =================================================================
	//                                 STATICS
	// =================================================================

	/**
	 * The Hashtable containing all questions in this animation
	 */
	private static Hashtable<String, QuestionAction> questionArray;

	/**
	 * Retrieve the action for the given key from the internal storage
	 * 
	 * @param key
	 *            the key of the requested QuestionAction
	 * @return the QuestionAction associated with the key, or <code>null
	 *         </code> the key in unknown
	 */
	public static QuestionAction getActionFor(String key) {
		if (questionArray.containsKey(key))
			return questionArray.get(key);
		return null;
	}

	// =================================================================
	//                             ATTRIBUTES
	// =================================================================

	/**
	 * The question group this question belongs to / public String
	 * questionGroupID = null;
	 * 
	 *  / ** The text for this question / public String questionText;
	 * 
	 *  / ** The number of points possible for this question / public int
	 * pointsPossible = 1;
	 */

	/**
	 * The type of the current question
	 */
	public int type = TYPE_MULTIPLE_CHOICE;

	/**
	 * Determine if the question is in use
	 */
	boolean used = false;

	// =================================================================
	//                           CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public QuestionAction() {	
		// do nothing; only used for serialization
	}

	/**
	 * Construct a new QuestionAction use the given type and title
	 * 
	 * @param typeOfQuestion
	 *            one of the constants provided: TYPE_FILL_IN_BLANKS,
	 *            TYPE_MULTIPLE_CHOICE, TYPE_TRUE_FALSE
	 * @param title
	 *            the title of the question
	 */
	public QuestionAction(int typeOfQuestion, String title) {
		setType(typeOfQuestion);
		if (questionArray == null)
			questionArray = new Hashtable<String, QuestionAction>(91);
		switch (type) {
		case TYPE_FILL_IN_BLANKS:
			questionArray.put(title, new FillInBlanksQuestionAction(title));
			break;
		case TYPE_MULTIPLE_CHOICE:
			questionArray.put(title, new MultipleChoiceQuestionAction(title));
			break;
		case TYPE_TRUE_FALSE:
			questionArray.put(title, new TrueFalseQuestionAction(title));
			break;
		}
	}

	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================

	/**
	 * Perform the action by showing the target URL in an HTML frame
	 * <strong>This method should only be invoked on a subclass! </strong>
	 */
	public void perform() {
		MessageDisplay.errorMsg("doNotInvokeMethod", new Object[] { "perform",
				getClass().getName() });
	}

	// =================================================================
	//                        ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Add an empty answer(no functionality in this class, but may be
	 * overridden)
	 * 
	 * @param text
	 *            the answer text
	 */
	public void addAnswer( String text) {
		// do nothing; only used for serialization
	}

	/**
	 * Add the given answer to the title
	 * 
	 * @param title
	 *            the title of the question needed for adding the answer to the
	 *            corrent entry
	 * @param answerText
	 *            the text of the answer
	 */
	public void addAnswer(String title, String answerText) {
		if (questionArray.containsKey(title)) {
			Object o = questionArray.get(title);
			if (o instanceof QuestionAction)
				((QuestionAction) o).addAnswer(answerText);
		}
	}

	/**
	 * Add a correct "fill in the blanks" answer
	 * 
	 * @param title
	 *            the title of the question needed for adding the answer to the
	 *            corrent entry
	 * @param correctAnswerText
	 *            the text of the correct answer
	 */
	public void addCorrectFIBAnswer(String title, String correctAnswerText) {
		if (questionArray.containsKey(title)) {
			QuestionAction o = questionArray.get(title);
			if (o instanceof FillInBlanksQuestionAction)
				((FillInBlanksQuestionAction) o)
						.addCorrectFIBAnswer(correctAnswerText);
		}
	}

	/**
	 * Add an entry to the question text, used for multiline input prompts
	 * 
	 * @param text
	 *            the text to be appended -- after a newline character -- to the
	 *            current question text
	 */
	public void addToQuestionText(String text) {
		StringBuilder questionText = new StringBuilder();
		String oldText = getProperties().getProperty(QUESTION_TEXT, null);
		if (oldText != null)
			questionText.append(oldText).append(MessageDisplay.LINE_FEED);
		questionText.append(text);
		getProperties().put(QUESTION_TEXT, questionText.toString());
	}

	/**
	 * Add an entry to the question text for the given question, used for
	 * multiline input prompts
	 * 
	 * @param title
	 *            the title of the question needed for adding the answer to the
	 *            corrent entry
	 * @param text
	 *            the text to be appended -- after a newline character -- to the
	 *            current question text
	 */
	public void addToQuestionText(String title, String text) {
		if (questionArray.containsKey(title)) {
			QuestionAction question = questionArray.get(title);
			question.addToQuestionText(text);
		}
	}

	/**
	 * retrieve the question text
	 * 
	 * @return the text to be used for the question
	 */
	public String getQuestionText() {
		return getProperties().getProperty(QUESTION_TEXT, "");
	}

	public String getTitle() {
		return getProperties().getProperty(TITLE_TAG, "");
	}

	public int getType() {
		return getProperties().getIntProperty(TYPE_TAG_LABEL,
				TYPE_MULTIPLE_CHOICE);
	}

	/**
	 * Set one of the multiple choice questions as correct for the given entry
	 * 
	 * @param title
	 *            the title of the question needed for adding the answer to the
	 *            corrent entry
	 * @param nrOfCorrectAnswer
	 *            the index of the correct answer
	 */
	public void setCorrectMCAnswer(String title, int nrOfCorrectAnswer) {
		if (questionArray.containsKey(title)) {
			QuestionAction questionAction = questionArray.get(title);
			if (questionAction instanceof MultipleChoiceQuestionAction)
				((MultipleChoiceQuestionAction) questionAction)
						.setCorrectAnswer(nrOfCorrectAnswer);
		}
	}

	/**
	 * Set the question text
	 * 
	 * @param text
	 *            the text to be used for the question
	 */
	public void setQuestionText(String text) {
		getProperties().put(QUESTION_TEXT, text);
	}

	/**
	 * Set the question text for the given question
	 * 
	 * @param title
	 *            the title of the question needed for adding the answer to the
	 *            corrent entry
	 * @param text
	 *            the text to be used for the question
	 */
	public void setQuestionTextFor(String title, String text) {
		if (questionArray.containsKey(title)) {
			QuestionAction question = questionArray.get(title);
			question.setQuestionText(text);
		}
	}

	/**
	 * Set the value for an answer for a "true/false" question type
	 * 
	 * @param title
	 *            the title of the question needed for adding the answer to the
	 *            corrent entry
	 * @param isCorrect
	 *            the truth value of the answer
	 */
	public void setCorrectTFAnswer(String title, boolean isCorrect) {
		if (questionArray.containsKey(title)) {
			QuestionAction question = questionArray.get(title);
			if (question instanceof TrueFalseQuestionAction)
				((TrueFalseQuestionAction) question).setCorrectAnswer(isCorrect);
		}
	}

	/**
	 * assign the question title
	 */
	public void setTitle(String title) {
		getProperties().put(TITLE_TAG, title);
	}

	/**
	 * assign the question type
	 */
	public void setType(int typeCode) {
		getProperties().put(TYPE_TAG_LABEL, typeCode);
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
		return AnimalTranslator.translateMessage("doNotInvokeMethodException",
				new Object[] { "toString", getClass().getName() });
	}
}
