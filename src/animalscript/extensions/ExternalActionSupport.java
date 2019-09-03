package animalscript.extensions;

import interactionsupport.UnknownParserException;
import interactionsupport.controllers.InteractionController;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Hashtable;

import animal.animator.DocumentationAction;
import animal.animator.ExternalAction;
import animal.animator.FillInBlanksQuestionAction;
import animal.animator.InteractionElement;
import animal.animator.MultipleChoiceQuestionAction;
import animal.animator.PerformableAction;
import animal.animator.TrueFalseQuestionAction;
import animal.main.Animal;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

/**
 * This class provides an import filter for AnimalScript XXX commands
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2000-06-17
 */
public class ExternalActionSupport extends BasicParser implements
		AnimalScriptInterface {

  private InteractionController interactionController;

	// ========================= attributes =========================

	// private boolean interactionDefinitionFound = false;
	private boolean startQuestionsEncountered = false;

	// private LanguageParserInterface parser = null;

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public ExternalActionSupport() {
		// Use the following commands in here to instantiate the method
		// callup mechanism:
		// 1. instantiate the local XProperties object
		handledKeywords = new Hashtable<String, Object>();

		// 2. For all handled keywords, associate("put") keyword with method name
		// NOTE: you *must* use lowercase for the whole keyword!!!
		handledKeywords.put("documentation", "parseDocumentation");
		handledKeywords.put("fibquestion", "parseFIBQuestion");
		handledKeywords.put("mcquestion", "parseMCQuestion");
		handledKeywords.put("tfquestion", "parseTFQuestion");
		handledKeywords.put("startquestions", "parseStartQuestions");
		handledKeywords.put("interactiondefinition", "parseInteractionDefinition");
		handledKeywords.put("interaction", "parseInteraction");
		ExternalAction.clearActions();

		this.interactionController = Animal.getAnimalConfiguration().getInteractionController();
	}

	// ===================================================================
	// interface methods
	// ===================================================================

	/**
	 * Determine depending on the command passed if a new step is needed Also keep
	 * in mind that we might be in a grouped step using the {...} form. Usually,
	 * every command not inside such a grouped step is contained in a new step.
	 * However, this is not the case for operations without visible effect -
	 * mostly maintenance or declaration entries.
	 * 
	 * @param command
	 *          the command used for the decision.
	 * @return true if a new step must be generated
	 */
	public boolean generateNewStep(String command) {
		// prevent a possible bug: left-over "startQuestionsEncountered" set to true
		// while loading in a new animation...
		if (startQuestionsEncountered && !employsQuestions) {
			startQuestionsEncountered = false;
		}

		// keep in same step if (sameStep || startQuestions || StartQuestions found)
		return !(sameStep || employsQuestions || "StartQuestions".equalsIgnoreCase(command)
				|| "interactionDefinition".equalsIgnoreCase(command) || "interaction".equalsIgnoreCase(command));
	}

	public void reset() {
		employsQuestions = false;
	}

	// ===================================================================
	// Animator parsing routines
	// ===================================================================

	/**
	 * Retrieves the number of points possible for this question
	 * 
	 * QuestionGroups define questions that belong together and are graded on a
	 * group basis, that is, if "enough" questions of the group are answered the
	 * full points are awarded and the rest of the group questions are dropped.
	 * 
	 * @param strTok
	 *          the StreamTokenizer used for parsing the content
	 * @param message
	 *          the standard message containing the question type and ID
	 * 
	 * @return the actual number of points possible for a "perfect" answer, must
	 *         be at least 0
	 */
	protected int getPointsPossibleForQuestion(StreamTokenizer strTok,
			String message) {
		return getPointsPossibleForQuestion(strTok, message, 0, Integer.MAX_VALUE);
	}

	/**
	 * Retrieves the minimum number of iterations the question has to be answered
	 * 
	 * QuestionGroups define questions that belong together and are graded on a
	 * group basis, that is, if "enough" questions of the group are answered the
	 * full points are awarded and the rest of the group questions are dropped.
	 * "Enough" is defined by the value returned by this method.
	 * 
	 * @param strTok
	 *          the StreamTokenizer used for parsing the content
	 * @param message
	 *          the standard message containing the question type and ID
	 * @param minPossible
	 *          the minimum number of valid points (usually 1)
	 * @param maxPossible
	 *          the maximum number of points (usually Integer.MAX_VALUE)
	 * 
	 * @return the actual number of points possible for a "perfect" answer
	 */
	protected int getMinimumIterationCount(StreamTokenizer strTok, String message) {
		int minIterationCount = 1;

		try {
			if (ParseSupport.parseOptionalWord(strTok, message
					+ " keyword 'nrRepeats'", "nrRepeats")) {
				minIterationCount = ParseSupport.parseInt(strTok, message
						+ " minimum number of iterations", 1);
				MessageDisplay.message(message + minIterationCount);
				ParseSupport.parseMandatoryEOL(stok, message + " answer EOL");
			}
		} catch (IOException e) {
			// do nothing
		}

		return minIterationCount;
	}

	/**
	 * Retrieves the number of points possible for this question
	 * 
	 * QuestionGroups define questions that belong together and are graded on a
	 * group basis, that is, if "enough" questions of the group are answered the
	 * full points are awarded and the rest of the group questions are dropped.
	 * 
	 * @param strTok
	 *          the StreamTokenizer used for parsing the content
	 * @param message
	 *          the standard message containing the question type and ID
	 * @param minPossible
	 *          the minimum number of valid points (usually 1)
	 * @param maxPossible
	 *          the maximum number of points (usually Integer.MAX_VALUE)
	 * 
	 * @return the actual number of points possible for a "perfect" answer
	 */
	protected int getPointsPossibleForQuestion(StreamTokenizer strTok,
			String message, int minPossible, int maxPossible) {
		int pointsPossible = 1;

		try {
			if (ParseSupport.parseOptionalWord(strTok, message + " keyword 'points'",
					"Points")) {
				pointsPossible = ParseSupport.parseInt(strTok, message
						+ " possible points", 1);
				MessageDisplay.message(message + pointsPossible);
				ParseSupport.parseMandatoryEOL(stok, message + " answer EOL");
			}
		} catch (IOException e) {
			// do nothing
		}

		return pointsPossible;
	}

	/**
	 * Retrieves the ID of the question group to which the question belongs.
	 * 
	 * QuestionGroups define questions that belong together and are graded on a
	 * group basis, that is, if "enough" questions of the group are answered the
	 * full points are awarded and the rest of the group questions are dropped.
	 * 
	 * @param strTok
	 *          the StreamTokenizer used for parsing the content
	 * @param message
	 *          the standard message containing the question type and ID
	 * 
	 * @return the ID of the question group the question belongs to; null if none
	 */
	protected String getQuestionGroup(StreamTokenizer strTok, String message) {
		String questionGroup = null;

		try {
			if (ParseSupport.parseOptionalWord(strTok, message
					+ " keyword 'questionGroup'", "QuestionGroup")) {
				questionGroup = AnimalParseSupport.parseText(strTok, message
						+ " question group");
				MessageDisplay.message(message + '\"' + questionGroup + '\"');
				ParseSupport.parseMandatoryEOL(strTok, message + " answer EOL");
			}
		} catch (IOException e) {
			// do nothing
		}
		// do nothing

		return questionGroup;
	}

	/**
	 * Create a array from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseDocumentation() throws IOException {
		ParseSupport.parseMandatoryWord(stok, "'Documentation' keyword",
				"Documentation");

		String targetURL = AnimalParseSupport.parseText(stok, "Documentation URL");
		ExternalAction externalAction = new ExternalAction(currentStep,
				ExternalAction.DOCUMENTATION_ACTION, "docu", new DocumentationAction(
						targetURL));

		// System.err.println("inserted new Animator: " +externalAction.toString());
		BasicParser.addAnimatorToAnimation(externalAction, anim);
	}

	// The following commented-out code was an attempt to have Animal
	// use the shared QuestionParser class that does not require
	// quotes around strings in questions.
	// /**
	// * Create a fill in blanks question from the description read from the
	// StreamTokenizer.
	// * The description is usually generated by other programs and dumped in
	// * a file or on System.out.
	// */
	// public void parseFIBQuestion() throws IOException
	// {
	// ParseSupport.parseMandatoryWord(stok,
	// "'FIBQuestion' keyword",
	// "FIBQuestion");
	// String questionID = ParseSupport.parseWord(stok, "'FIBQuestion' keyword");
	// //DEBUG
	// System.out.println("Encountered Question with ID " + questionID);
	// if (!startQuestionsEncountered)
	// {
	// FillInBlanksQuestionAction targetAction = new
	// FillInBlanksQuestionAction(questionID);
	// ExternalAction externalAction = new ExternalAction(currentStep,
	// ExternalAction.QUESTION_ACTION,
	// questionID, targetAction);
	// anim.insertAnimator(externalAction);
	// }
	// else
	// {
	// String qScript = "";
	// String tmpStr = ParseSupport.parseWord(stok, "'FIBQuestion' keyword");
	// while (!tmpStr.equalsIgnoreCase("endanswer"))
	// {
	// qScript += tmpStr + "\n";
	// tmpStr = ParseSupport.parseWord(stok, "'FIBQuestion' keyword");
	// }
	// qScript += "\nENDANSWER";
	// // DEBUG
	// System.out.println("Question Script being given to question parser is " +
	// qScript);
	// StringTokenizer st = new StringTokenizer(tmpStr, " \t\n");
	// FIBQuestionParser qp = new FIBQuestionParser(st);
	// PerformableAction action = ExternalAction.getActionNamed(questionID);
	// if (action == null || !(action instanceof FillInBlanksQuestionAction))
	// return;
	// FillInBlanksQuestionAction targetAction =
	// (FillInBlanksQuestionAction)action;
	// targetAction.addToQuestionText((String)qp.getQuestion());
	// // DEBUG
	// System.out.println("Adding to question text for targetAction" +
	// (String)qp.getQuestion());
	// int k;
	// Vector answers = qp.getCorrectAnswer();
	// for (k = 0; k < answers.size(); ++k) {
	// targetAction.addAnswer((String)answers.elementAt(k));
	// // DEBUG
	// System.out.println("Adding to answer for targetAction" +
	// (String)answers.elementAt(k));
	// }
	// }
	// }

	/**
	 * Create a fill in blanks question from the description read from the
	 * StreamTokenizer. The description is usually generated by other programs and
	 * dumped in a file or on System.out.
	 */
	public void parseFIBQuestion() throws IOException {
		ParseSupport.parseMandatoryWord(stok, "'FIBQuestion' keyword",
				"FIBQuestion");

		// This is a hack to make sure that the question ID in Animal incldes the
		// quotes. Why?
		// Because that's the way that the JHAVE server is parsing it and the two
		// ID's must match
		// when we are in quiz-for-real mode
		String questionID = "\""
				+ AnimalParseSupport.parseText(stok, "question ID") + "\"";

		if (!startQuestionsEncountered) {
			FillInBlanksQuestionAction targetAction = new FillInBlanksQuestionAction(
					questionID);
			ExternalAction externalAction = new ExternalAction(currentStep,
					ExternalAction.QUESTION_ACTION, questionID, targetAction);
			BasicParser.addAnimatorToAnimation(externalAction, anim);
		} else {
			boolean usableAction = true;
			PerformableAction action = null;

			try {
				action = ExternalAction.getActionNamed(questionID);
			} catch (Exception e) {
				// System.err.println("FIBQ Action with ID " +questionID
				// +" not found.");
				usableAction = false;
			}

			usableAction = ((action != null) && (action instanceof FillInBlanksQuestionAction));

			FillInBlanksQuestionAction targetAction = null;

			if (usableAction) {
				targetAction = (FillInBlanksQuestionAction) action;
			} else {
				targetAction = new FillInBlanksQuestionAction(String
						.valueOf(questionID));
			}

			ParseSupport.parseMandatoryEOL(stok,
					"FillInBlanksQuestion keyword 'answer' EOL");

			// retrieve the question group
			
//			String questionGroup = 
			getQuestionGroup(stok,
					"Fill in the Blanks Question \"" + questionID + "\" Group ");

			
//			int minIterations = 
			getMinimumIterationCount(stok,
					"Fill in the Blanks Question \"" + questionID
							+ "\" minimum number of iterations: ");

			// retrieve the maximum number of points possible for this question
			
//			int pointsPossible = 
			getPointsPossibleForQuestion(stok,
					"Fill in the Blanks Question \"" + questionID + "\" points possible ");

			while (!ParseSupport.parseOptionalWord(stok,
					"FillInBlanksQuestion keyword 'ENDTEXT'", "endtext")) {
				String entry = AnimalParseSupport.parseText(stok,
						"FillInBlanksQuestion prompt");
				ParseSupport.parseMandatoryEOL(stok,
						"FillInBlanksQuestion question EOL");
				targetAction.addToQuestionText(entry);
			}

			ParseSupport.parseMandatoryEOL(stok, "FillInBlanksQuestion question EOL");
			ParseSupport.parseMandatoryWord(stok,
					"FillInBlanksQuestion keyword 'answer'", "answer");
			ParseSupport.parseMandatoryEOL(stok,
					"FillInBlanksQuestion keyword 'answer' EOL");

			while (!ParseSupport.parseOptionalWord(stok,
					"FillInBlanksQuestion keyword 'ENDTEXT'", "endanswer")) {
				String entry = AnimalParseSupport.parseText(stok,
						"FillInBlanksQuestion answer");
				ParseSupport.parseMandatoryEOL(stok, "FillInBlanksQuestion answer EOL");
				targetAction.addAnswer(entry);
			}

			if (!usableAction) {
				System.err
						.println("Throwing away the following interaction object, as it was not declared:\n"
								+ targetAction.toString());
			}
		}
	}

	/**
	 * Create a multiple choice question from the description read from the
	 * StreamTokenizer. The description is usually generated by other programs and
	 * dumped in a file or on System.out.
	 */
	public void parseMCQuestion() throws IOException {
		ParseSupport.parseMandatoryWord(stok, "'MCQuestion' keyword", "MCQuestion");

		// String questionID = AnimalParseSupport.parseText(stok, "question ID");
		// This is a hack to make sure that the question ID in Animal incldes the
		// quotes. Why?
		// Because that's the way that the JHAVE server is parsing it and the two
		// ID's must match
		// when we are in quiz-for-real mode
		String questionID = "\""
				+ AnimalParseSupport.parseText(stok, "question ID") + "\"";

		if (!startQuestionsEncountered) {
			MultipleChoiceQuestionAction targetAction = new MultipleChoiceQuestionAction(
					questionID);
			ExternalAction externalAction = new ExternalAction(currentStep,
					ExternalAction.QUESTION_ACTION, questionID, targetAction);
			BasicParser.addAnimatorToAnimation(externalAction, anim);
		} else {
			boolean usableAction = true;
			PerformableAction action = null;

			try {
				action = ExternalAction.getActionNamed(questionID);
			} catch (Exception e) {
				// System.err.println("MCQ Action with ID '" +questionID
				// +"' was not declared.");
				usableAction = false;
			}

			usableAction = ((action != null) && (action instanceof MultipleChoiceQuestionAction));

			MultipleChoiceQuestionAction targetAction = null;

			if (usableAction) {
				targetAction = (MultipleChoiceQuestionAction) action;
			} else {
				targetAction = new MultipleChoiceQuestionAction(String
						.valueOf(questionID));
			}

			ParseSupport.parseMandatoryEOL(stok,
					"MultipleChoiceQuestion keyword 'answer' EOL");

			int nrAnswers = 0;
			
//			int pointsPossible = 1;
			
//			int minIterations = 1;
			String questionText = null;
			String answerText = null;
			String commentText = null;
			
//			String questionGroup = null;

			// retrieve the question group
//			questionGroup = 
			getQuestionGroup(stok, "Multiple Choice Question \""
					+ questionID + "\" Group ");

//			minIterations = 
			getMinimumIterationCount(stok,
					"Multiple Choice Question \"" + questionID
							+ "\" minimum number of iterations: ");

			// retrieve the maximum number of points possible for this question
//			pointsPossible = 
			getPointsPossibleForQuestion(stok,
					"Multiple Choice Question \"" + questionID + "\" points possible ");

			// parse the actual content
			while (!ParseSupport.parseOptionalWord(stok,
					"MultipleChoiceQuestion keyword 'ENDTEXT'", "endtext")) {
				questionText = AnimalParseSupport.parseText(stok,
						"MultipleChoiceQuestion prompt");
				ParseSupport.parseMandatoryEOL(stok,
						"MultipleChoiceQuestion question EOL");
				targetAction.addToQuestionText(questionText);
			}

			ParseSupport.parseMandatoryEOL(stok,
					"MultipleChoiceQuestion question EOL");

			// parse combination of single line of text, EOL, "endchoice", EOL until
			// "answer" is found
			while (!ParseSupport.parseOptionalWord(stok,
					"MultipleChoiceQuestion keyword 'ANSWER'", "answer")) {
				answerText = AnimalParseSupport.parseText(stok,
						"MultipleChoiceQuestion choice");
				targetAction.addAnswer(answerText);
				ParseSupport.parseMandatoryEOL(stok,
						"MultipleChoiceQuestion choice EOL");

				ParseSupport.parseMandatoryWord(stok,
						"MultipleChoiceQuestion keyword 'endchoice'", "EndChoice");
				ParseSupport.parseMandatoryEOL(stok,
						"MultipleChoiceQuestion choice EOL");

				if (ParseSupport.parseOptionalWord(stok,
						"MultipleChoiceQuestion parameter 'comment'", "comment")) {
					ParseSupport.parseMandatoryEOL(stok,
							"MultipleChoiceQuestion choice EOL");
					commentText = AnimalParseSupport.parseText(stok,
							"MultipleChoiceQuestion comment");
					MessageDisplay.message("Didactical comment for question\n\t\""
							+ questionText + "\"\n\tanswer " + answerText + ":\n\t"
							+ commentText);
					ParseSupport.parseMandatoryEOL(stok,
							"MultipleChoiceQuestion choice EOL");
					ParseSupport.parseMandatoryWord(stok,
							"MultipleChoiceQuestion choice ENDCOMMENT", "EndComment");
					ParseSupport.parseMandatoryEOL(stok,
							"MultipleChoiceQuestion choice EOL");
				}

				nrAnswers++;
			}

			ParseSupport.parseMandatoryEOL(stok, "MultipleChoiceQuestion answer EOL");

			int correctAnswer = ParseSupport.parseInt(stok,
					"MultipleChoiceQuestion correct answer ID", 1, nrAnswers);
			targetAction.setCorrectAnswer(correctAnswer);
			ParseSupport.parseMandatoryEOL(stok, "MultipleChoiceQuestion answer EOL");

			// new addition!
			ParseSupport.parseMandatoryWord(stok,
					"MultipleChoiceQuestion keyword 'endAnswer'", "endAnswer");

			if (!usableAction) {
				System.err
						.println("Throwing away the following interaction object, as it was not declared:\n"
								+ targetAction.toString());
			}
		}
	}

	/**
	 * Create a true/false in blanks question from the description read from the
	 * StreamTokenizer. The description is usually generated by other programs and
	 * dumped in a file or on System.out.
	 */
	public void parseTFQuestion() throws IOException {
		ParseSupport.parseMandatoryWord(stok, "'TFQuestion' keyword", "TFQuestion");

		// String questionID = AnimalParseSupport.parseText(stok, "question ID");
		// This is a hack to make sure that the question ID in Animal incldes the
		// quotes. Why?
		// Because that's the way that the JHAVE server is parsing it and the two
		// ID's must match
		// when we are in quiz-for-real mode
		String questionID = "\""
				+ AnimalParseSupport.parseText(stok, "question ID") + "\"";

		if (!startQuestionsEncountered) {
			TrueFalseQuestionAction targetAction = new TrueFalseQuestionAction(
					questionID);
			ExternalAction externalAction = new ExternalAction(currentStep,
					ExternalAction.QUESTION_ACTION, questionID, targetAction);
			BasicParser.addAnimatorToAnimation(externalAction, anim);
		} else {
			// int nrAnswers = 0;
			PerformableAction action = null;
			boolean usableAction = true;

			try {
				action = ExternalAction.getActionNamed(questionID);
			} catch (Exception e) {
				usableAction = false;

				// System.err.println("TFQ Action with key '" +questionID
				// +"' was not declared.");
			}

			// System.err.println("Retrieved action for TF element with questionID '"
			// +questionID +"' -- "
			// +action);
			usableAction = ((action != null) && (action instanceof TrueFalseQuestionAction));

			TrueFalseQuestionAction targetAction = null;

			if (usableAction) {
				targetAction = (TrueFalseQuestionAction) action;
			} else {
				targetAction = new TrueFalseQuestionAction(String.valueOf(questionID));
			}

			ParseSupport.parseMandatoryEOL(stok,
					"TrueFalseQuestion keyword 'answer' EOL");

			// retrieve the question group
			
//			String questionGroup = 
			getQuestionGroup(stok, "True/False Question \""
					+ questionID + "\" Group ");

			
//			int minIterations = 
			getMinimumIterationCount(stok,
					"True/False Question \"" + questionID
							+ "\" minimum number of iterations: ");

			// retrieve the maximum number of points possible for this question
			
//			int pointsPossible = 
			getPointsPossibleForQuestion(stok,
					"True/False Question \"" + questionID + "\" points possible ");

			while (!ParseSupport.parseOptionalWord(stok,
					"TrueFalseQuestion keyword 'ENDTEXT'", "endtext")) {
				String entry = AnimalParseSupport.parseText(stok,
						"TrueFalseQuestion prompt");
				ParseSupport.parseMandatoryEOL(stok, "TrueFalseQuestion question EOL");
				targetAction.addToQuestionText(entry);
			}

			ParseSupport.parseMandatoryEOL(stok, "FillInBlanksQuestion question EOL");
			ParseSupport.parseMandatoryWord(stok,
					"TrueFalseQuestion keyword 'ANSWER'", "answer");
			ParseSupport.parseMandatoryEOL(stok, "FillInBlanksQuestion answer EOL");

			String result = ParseSupport.parseWord(stok,
					"TrueFalseQuestion classifier");
			boolean isCorrect = result.toLowerCase().equals("t");

			ParseSupport.parseMandatoryEOL(stok, "TrueFalseQuestion classifier EOL");
			ParseSupport.parseMandatoryWord(stok,
					"TrueFalseQuestion keyword 'ENDANSWER'", "endAnswer");

			if (usableAction)
				targetAction.setCorrectAnswer(isCorrect);
			else
				System.err
						.println("Throwing away the following interaction object, as it was not declared:\n"
								+ targetAction.toString());
		}
	}

	/**
	 * Create a array from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseStartQuestions() throws IOException {
		ParseSupport.parseMandatoryWord(stok, "'StartQuestions' keyword",
				"StartQuestions");
		startQuestionsEncountered = true;
		employsQuestions = true;
		ExternalAction.resetActions();
	}

	/**
	 * Create an appropriate entry for the interaction definition reference.
	 */
	public void parseInteractionDefinition() throws IOException {
		ParseSupport.parseMandatoryWord(stok, "'InteractionDefinition' keyword",
				"interactionDefinition");
		// interactionDefinitionFound = true;

		String interactionURL = AnimalParseSupport.parseText(stok,
				"interactionDefinition URL");
		try {
			interactionController.interactionDefinition(interactionURL,
					"text/animalscript");
		} catch (UnknownParserException upe) {
			System.err.println("Oops, parser unknown!");
		} catch (Exception ee) {
			System.err.println("Some other exception: " + ee.getMessage());
		}
		InteractionElement interactionElement = new InteractionElement(currentStep,
				interactionURL, InteractionElement.INTERACTION_DEFINITION,
				interactionController);
		BasicParser.addAnimatorToAnimation(interactionElement, anim);
		interactionElement.action(0, 0);
	}

	/**
	 * Parse a planned interaction
	 */
	public void parseInteraction() throws IOException {
		ParseSupport.parseMandatoryWord(stok, "'Interaction' keyword",
				"interaction");

		String interactionID = AnimalParseSupport.parseText(stok, "interaction ID");
		InteractionElement interactionElement = new InteractionElement(currentStep,
				interactionID, InteractionElement.INTERACTION_INVOCATION,
				interactionController);
		BasicParser.addAnimatorToAnimation(interactionElement, anim);
	}
} // class ExternalActionSupport
