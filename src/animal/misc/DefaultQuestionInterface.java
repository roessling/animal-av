package animal.misc;

import javax.swing.JPanel;

public class DefaultQuestionInterface implements QuestionInterface,
		FillInBlanksInterface, MultipleChoiceInterface, TrueFalseQuestionInterface {
	public DefaultQuestionInterface(String text) {
		SetText(text);
	}

	public void AddAnswer(String text) {
		// do nothing
	}

	public void evaluate(boolean isCorrect) {
		// do nothing
	}

	public JPanel GetPanel() {
		return null;
	}

	public void initialize(QuestionInterface question, boolean isInitialized,
			String initTxt) {
		// do nothing
	}

	public void MakePanel() {
		// do nothing
	}

	public void RandomizeAnswers() {
		// do nothing
	}

	public void reset() {
		// do nothing
	}

	public void SetAnswer(boolean isCorrect) {
		// do nothing
	}

	public void SetCorrectAnswer(int answerIndex) {
		// do nothing
	}

	public void SetPossibleAnswer(String answerText) {
		// do nothing
	}

	public void SetQuestion(String questionText) {
		// do nothing
	}

	public void SetText(String text) {
		// do nothing
	}
}
