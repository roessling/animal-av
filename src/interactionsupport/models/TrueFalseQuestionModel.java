package interactionsupport.models;

/**
 * Model representing a true false question. The user can only choose, whether
 * the shown statement is true or false.
 * 
 * @author Guido Roessling <roessling@acm.org> / Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class TrueFalseQuestionModel extends QuestionModel {

  /** the answer ID for the answer "true" */
  public static String answerIDTrue   = "true";

  /** the answer ID for the answer "false" */
  public static String answerIDFalse  = "false";

  /** the number of points the user gets when giving the right answer */
  private int          pointsPossible = -1;

  /** whether the question statement is correct or not */
  private boolean      correctAnswer;

  public TrueFalseQuestionModel(String id) {
    super(id);
    init();
  }

  public TrueFalseQuestionModel(String id, boolean correctAnswer,
      int pointsPossible) {
    super(id);
    init();
    setCorrectAnswer(correctAnswer);
    setPointsPossible(pointsPossible);
  }

  private void init() {
    // add the two default answers with "safe" values (both give no points)
    AnswerModel answer = new AnswerModel(answerIDTrue, "right", 0, "");
    answers.put(answerIDTrue, answer);
    answer = new AnswerModel(answerIDFalse, "wrong", 0, "");
    answers.put(answerIDFalse, answer);
  }

  /**
   * IMPORTANT: Do not use this function on true false questions. It will have
   * no effect. The possible answers are "true" and "false" and the correct
   * answer can be defined in the constructor.
   */
  @Override
  public AnswerModel addAnswer(AnswerModel answer) {
    return null;
  }

  /**
   * IMPORTANT: Do not use this function on true false questions. It will have
   * no effect. The possible answers are "true" and "false" and the correct
   * answer can be defined in the constructor.
   */
  @Override
  public AnswerModel addAnswer(String text, int points, String feedback) {
    return null;
  }

  /**
   * IMPORTANT: Do not use this function on true false questions. It will have
   * no effect. The possible answers are "true" and "false" and the correct
   * answer can be defined in the constructor.
   */
  @Override
  public AnswerModel addAnswer(String id, String text, int points,
      String feedback) {
    return null;
  }

  /**
   * If true is returned, the statement of the question is correct. If false is
   * returned, the statement of the question is wrong.
   * 
   * @return whether the correct answer is true or false
   */
  public boolean getCorrectAnswer() {
    return correctAnswer;
  }

  /**
   * Sets the correct answer. This method should be called before
   * {@link #setPointsPossible(int)} is called.
   * 
   * @param correctAnswer
   *          the correct answer
   */
  public void setCorrectAnswer(boolean correctAnswer) {
    this.correctAnswer = correctAnswer;
    AnswerModel answer = answers.get(correctAnswer + "");
    // if the possible points have been defined before the correct answer has
    // been defined, assign them now to the correct answer
    if (pointsPossible > 0) {
      answer.setPoints(pointsPossible);
      answers.get(!correctAnswer + "").setPoints(0);
    }
  }

  /**
   * @return how much points the user gets when he answers this question
   *         correctly
   */
  public int getPointsPossible() {
    return pointsPossible;
  }

  /**
   * Sets the correct answer. This method should be called after
   * {@link #setCorrectAnswer(boolean)} has been called.
   * 
   * @param points
   *          the points the user gets when he answers the question correctly
   */
  public void setPointsPossible(int points) {
    // do not allow negative points
    if (points < 1) {
      return;
    }
    // save the points in order to make sure that they are also assigned to the
    // correct answer if it has not been defined yet
    this.pointsPossible = points;
    for (AnswerModel answer : answers.values()) {
      if (answer.getID().equals(correctAnswer + "")) {
        answer.setPoints(points);
      } else {
        answer.setPoints(0);
      }
    }
  }

  /**
   * Sets the feedback for the given answer ("true" or "false").
   * 
   * @param type
   *          the answer for which the feedback should be set
   * @param feedback
   *          the feedback text
   */
  public void setFeedbackForAnswer(boolean type, String feedback) {
    AnswerModel answer;
    if (type) {
      answer = getAnswer(answerIDTrue);
    } else {
      answer = getAnswer(answerIDFalse);
    }
    answer.setFeedback(feedback);
  }

}
