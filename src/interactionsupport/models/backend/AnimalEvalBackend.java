package interactionsupport.models.backend;

import interactionsupport.controllers.InteractionController;

/**
 * The "animalEvalBackend" is an example implementation of a customized backend.
 * It displays final results, showing the earned against the total points and
 * the percentage of correct answers.
 * 
 * @author Gina Haeussge, Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class AnimalEvalBackend implements BackendInterface {

  /** the name of this backend */
  private final String            NAME            = "animalEvalBackend";

  /** the number of answers the user submitted */
  private int                     numberOfAnswers = 0;

  /** points available */
  private int                     allPoints       = 0;

  /** points the student earned */
  private int                     earnedPoints    = 0;

  public AnimalEvalBackend() {
    // do nothing
  }

  /**
   * Shows the earned points in relation to the total count of points,
   * calculates the percentage of correct answers and shows it as well.
   * 
   * @return DOCUMENT ME!
   */
  public String getResultString() {
    int percentage = 0;

    if (allPoints > 0) {
      percentage = (earnedPoints * 100) / allPoints;
    }

    return InteractionController.translateMessage(
        "finalRes",
        new Object[] { Integer.valueOf(earnedPoints),
            Integer.valueOf(allPoints), Integer.valueOf(percentage),
            numberOfAnswers });
  }

  /**
   * Tells the interaction module whether to re-enable the submit button or not.
   * 
   * @return A boolean value which determines whether the submit button should
   *         be re-enabled or not.
   */
  public boolean enableSubmit() {
    return false;
  }

  /**
   * Prints the result of the question with the given ID on the console.
   * 
   * @param questionID
   *          the ID of the question
   * @param pointsPossible
   *          the points the user gets for a perfect answer
   * @param pointsAchieved
   *          the points the user actually achieved with his answer
   * 
   * @return Whether to show the answer or not.
   */
  public boolean submitAnswer(String questionID, int pointsAchieved,
      int pointsPossible) {
    String status = "incorrect";
    if (pointsAchieved == pointsPossible) {
      status = "correct";
    } else if (pointsAchieved > 0 && pointsAchieved < pointsPossible) {
      status = "partiallyCorrect";
    }
    System.out.println(InteractionController.translateMessage(
        "receivedMsgAs",
        new String[] { questionID,
            InteractionController.translateMessage(status),
            String.valueOf(pointsAchieved), String.valueOf(pointsPossible) }));

    numberOfAnswers++;
    allPoints += pointsPossible;
    earnedPoints += pointsAchieved;

    // we want the results to be displayed
    return true;
  }

  /**
   * Returns the name of the backend.
   * 
   * @return a String containing the name of the backend
   */
  public String toString() {
    return NAME;
  }

}
