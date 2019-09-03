package interactionsupport.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Model representing a multiple selection question. In a multiple selection
 * question, the user has to choose one or more answer from a list of possible
 * answers.
 * 
 * @author Guido Roessling <roessling@acm.org> / Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class MultipleSelectionQuestionModel extends QuestionModel {

  public MultipleSelectionQuestionModel(String id) {
    super(id);
  }

  /**
   * Do not use this method on a multiple selection question - it will return
   * null. There can be more than one user answer, so use
   * {@link #getUserAnswerIDs()} instead.
   */
  @Override
  public String getUserAnwerID() {
    return null;
  }

  /**
   * Do not use this method on a multiple selection question - it will do
   * nothing. There can be more than one user answer, so use
   * {@link #addUserAnswerID(String)} instead.
   */
  public void setUserAnswerID(String userAnswerID) {
    // do nothing
  }

  /**
   * Adds the given user answer ID to the set of user answers.
   * 
   * @param userAnswerID
   */
  public void addUserAnswerID(String userAnswerID) {
    // if there is no user answer ID yet, just set it
    if (this.userAnswerID == null) {
      this.userAnswerID = userAnswerID;
    } else {
      // otherwise add it separated by a comma to the string
      this.userAnswerID = this.userAnswerID + "," + userAnswerID;
    }
  }

  /**
   * Retrieves the answer IDs of the answers the user selected.
   * 
   * @return the answer IDs of the selected answers
   */
  public Set<String> getUserAnswerIDs() {
    if (userAnswerID == null) {
      return new HashSet<String>();
    }
    String[] userAnswerIDs = userAnswerID.split(",");
    Set<String> result = new HashSet<String>(Arrays.asList(userAnswerIDs));
    return result;
  }

  @Override
  public int getPointsAchieved() {
    // go through all user answer IDs and determine the corresponding points
    int result = 0;
    for (String userAnswerID : this.getUserAnswerIDs()) {
      result += this.getAnswer(userAnswerID).getPoints();
    }
    return result;
  }

  @Override
  public int getPointsPossible() {
    // go through all answers and add each positive value
    int pointsPossible = 0;
    for (AnswerModel answer : answers.values()) {
      if (answer.getPoints() > 0) {
        pointsPossible += answer.getPoints();
      }
    }
    return pointsPossible;
  }

  @Override
  public String getFeedback() {
    // concatenate the feedback string for each given answer
    String feedback = "";
    for (String userAnswerID : this.getUserAnswerIDs()) {
      feedback += this.getAnswer(userAnswerID).getFeedback() + "\n";
    }
    return feedback;
  }

}
