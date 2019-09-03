package interactionsupport.models;

/**
 * Model representing a fill in blanks question. The user can enter any string
 * as an answer to the question.
 * 
 * @author Guido Roessling <roessling@acm.org> / Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class FillInBlanksQuestionModel extends QuestionModel {

  public FillInBlanksQuestionModel(String id) {
    super(id);
  }

  /**
   * Retrieves the answer ID for the answer with the given text or null if none
   * exists. Mind: The answers are compared after they have been trimmed and
   * ignoring case.
   * 
   * @param answerText
   *          the answer text
   * @return the ID of the answer with the given text or null if none exists
   */
  public String getAnswerID(String answerText) {
    for (AnswerModel answer : answers.values()) {
      if (answerText.trim().equalsIgnoreCase(answer.getText().trim())) {
        return answer.getID();
      }
    }
    return null;
  }

}
