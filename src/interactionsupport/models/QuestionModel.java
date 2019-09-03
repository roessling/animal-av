package interactionsupport.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class QuestionModel extends InteractionModel {

  /** the actual question prompt the user will see */
  protected String                   prompt;

  /** map mapping a unique answer identifier to the actual answer object */
  protected Map<String, AnswerModel> answers       = new HashMap<String, AnswerModel>();

  /** the ID of the answer the user gave */
  protected String                   userAnswerID;

  /** the ID of the group this question belongs to */
  protected String                   groupID;

  /** how often the question may be answered - default is one */
  protected int                      numberOfTries = 1;

  public QuestionModel(String id) {
    super(id);
  }

  /**
   * Retrieves the question prompt, i.e., the introductory text that describes
   * the question statement.
   * 
   * @return the question prompt
   */
  public String getPrompt() {
    return prompt;
  }

  /**
   * Assigns the question prompt which describes the question statement.
   * 
   * @param questionText
   *          the text to be used as the question prompt.
   */
  public void setPrompt(String questionText) {
    prompt = questionText;
  }

  /**
   * Adds the given {@link AnswerModel} object to this question object. If an
   * answer with the same ID already exists, it will be overwritten.
   * Auto-generates an answer ID if none is set.
   * 
   * @param answer
   *          the answer to add
   * @return the added {@link AnswerModel}
   */
  public AnswerModel addAnswer(AnswerModel answer) {
    String id = answer.getID();
    if (id == null) {
      id = generateAnswerID();
      answer.setID(id);
    }
    answers.put(id, answer);
    return answer;
  }

  /**
   * Generates a unique answer ID.
   * 
   * @return a unique answer ID
   */
  private String generateAnswerID() {
    String answerID = UUID.randomUUID().toString();
    // as long as there is another answer with this ID, generate a new ID
    while (answers.keySet().contains(answerID)) {
      answerID = UUID.randomUUID().toString();
    }
    return answerID;
  }

  /**
   * Adds an answer with the given data to this instance. Auto-generates a
   * unique answer ID.
   * 
   * @param text
   *          the answer string
   * @param points
   *          the number of points the user gets when he enters this answer
   * @param feedback
   *          the feedback that should be shown when the user enters this answer
   * @return the generated {@link AnswerModel}
   */
  public AnswerModel addAnswer(String text, int points, String feedback) {
    String id = generateAnswerID();
    return addAnswer(id, text, points, feedback);
  }

  /**
   * Adds an answer with the given data to this instance.
   * 
   * @param id
   *          the unique ID
   * @param text
   *          the answer string
   * @param feedback
   *          the feedback that should be shown when the user enters this answer
   * @param points
   *          the number of points the user gets when he enters this answer
   * @return the generated {@link AnswerModel}
   */
  public AnswerModel addAnswer(String id, String text, int points,
      String feedback) {
    AnswerModel answer = new AnswerModel(id, text, points, feedback);
    addAnswer(answer);
    return answer;
  }

  /**
   * Retrieves all answer objects assigned to this question.
   * 
   * @return all answer objects
   */
  public Collection<AnswerModel> getAnswers() {
    return answers.values();
  }

  /**
   * Retrieves the answer object with the given ID. Returns null if no answer
   * with the given ID exists.
   * 
   * @param answerID
   *          the unique ID of the answer
   * @return the answer object with the given ID or null if it does not exist
   */
  public AnswerModel getAnswer(String answerID) {
    return answers.get(answerID);
  }

  /**
   * Retrieves the ID of the answer the user gave.
   */
  public String getUserAnwerID() {
    return userAnswerID;
  }

  /**
   * Assigns the ID of the user's answer.
   * 
   * @param userAnswerID
   *          the ID of the answer the user gave
   */
  public void setUserAnswerID(String userAnswerID) {
    this.userAnswerID = userAnswerID;
  }

  /**
   * Retrieves the ID of the question group. This can be used to prevent
   * multiple questions of the "same" type to be asked once the user has
   * answered a "sufficient" number of them correctly.
   * 
   * @return the ID of the group of the question, if any. Returns <em>null</em>
   *         if there is no associated ID.
   */
  public String getGroupID() {
    return groupID;
  }

  /**
   * Assigns the question group ID for this question. This can be used to
   * prevent multiple questions of the "same" type to be asked once the user has
   * answered a "sufficient" number of them correctly.
   * 
   * @param groupID
   *          the question group ID for this question.
   */
  public void setGroupID(String groupID) {
    this.groupID = groupID;
  }

  /**
   * How often the student may answer this question.
   * 
   * @return how often the student may answer this question
   */
  public int getNumberOfTries() {
    return numberOfTries;
  }

  /**
   * Defines how often the question may be answered by the student.
   * 
   * @param numberOfTries
   *          the number of tries the student has
   */
  public void setNumberOfTries(int numberOfTries) {
    this.numberOfTries = numberOfTries;
  }

  /**
   * Retrieve the number of points achieved in this question.
   * 
   * @return the number of points achieved with the users answer
   */
  public int getPointsAchieved() {
    if (userAnswerID == null) {
      return 0;
    }
    return answers.get(userAnswerID).getPoints();
  }

  /**
   * Calculates the number of points that are possible to achieve with the
   * "perfect" answer.
   * 
   * @return the number of points that a user can achieve
   */
  public int getPointsPossible() {
    int pointsPossible = 0;
    for (AnswerModel answer : answers.values()) {
      if (answer.getPoints() > pointsPossible) {
        pointsPossible = answer.getPoints();
      }
    }
    return pointsPossible;
  }

  /**
   * Retrieves the feedback the user should see.
   * 
   * @return the feedback that should be shown to the user
   */
  public String getFeedback() {
    if (userAnswerID == null || userAnswerID.equals("")) {
      return "";
    }
    return answers.get(userAnswerID).getFeedback();
  }

}
