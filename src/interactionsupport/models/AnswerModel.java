package interactionsupport.models;

public class AnswerModel {

  /** a unique identifier */
  private String id;

  /** the answer text */
  private String text;

  /** the number of points the user gets when submitting this answer */
  private int    points   = 0;

  /** the feedback the user gets when submitting this answer */
  private String feedback = "";

  public AnswerModel() {

  }

  /**
   * 
   * @param id
   *          a unique identifier
   * @param text
   *          the answer text
   * @param points
   *          the number of points the user gets when submitting this answer
   * @param feedback
   *          the feedback the user gets when submitting this answer
   */
  public AnswerModel(String id, String text, int points, String feedback) {
    this.id = id;
    this.text = text;
    this.points = points;
    this.feedback = feedback;
  }

  /**
   * @return the unique identifier of this answer
   */
  public String getID() {
    return id;
  }

  /**
   * @return the answer text
   */
  public String getText() {
    return text;
  }

  /**
   * @return the points the user gets for giving this answer
   */
  public int getPoints() {
    return points;
  }

  /**
   * @return the feedback that is shown when the user gives this answer
   */
  public String getFeedback() {
    return feedback;
  }

  /**
   * @param id
   *          the unique identifier of this answer
   */
  public void setID(String id) {
    this.id = id;
  }

  /**
   * @param text
   *          the answer text
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @param points
   *          the points the user gets for giving this answer
   */
  public void setPoints(int points) {
    this.points = points;
  }

  /**
   * @param feedback
   *          the feedback that is shown when the user gives this answer
   */
  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

}
