package interactionsupport.models.backend;


/**
 * Interface of backends usable with AVInteraction
 */
public interface BackendInterface {

  /**
   * Tells the interaction module whether to re-enable the submit button or not.
   * 
   * @return A boolean value which determines whether the submit button should
   *         be re-enabled or not.
   */
  public boolean enableSubmit();

  /**
   * Submits the answer to interaction "questionID" to the backend and awaits a
   * boolean value which signifies whether or not to display the solution in the
   * interaction window. A backend connecting to a server for grading reasons
   * for example normally would not display the answer, a practice-only backend
   * on the other hand would normally want to give the student a feedback
   * whether he was right or wrong.
   * 
   * @param questionID
   *          Contains the ID of the answered question.
   * @param pointsAchieved
   *          Number of points user achieved.
   * @param pointsPossible
   *          Number of points one gets for answering the question right.
   * 
   * @return A boolean value which determines whether the question window should
   *         display the answer of the question.
   */
  public boolean submitAnswer(String questionID, int pointsAchieved,
      int pointsPossible);

  /**
   * Returns a string representation of the backend object, most commonly this
   * would be the name of the backend.
   * 
   * @return A String identifying the backend object.
   */
  public String toString();

}
