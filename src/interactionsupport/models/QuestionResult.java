package interactionsupport.models;

/**
 * Class representing a question result. It includes the achieved and the
 * possible points.
 * 
 * @author Simon Sprankel <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class QuestionResult {

  private int achievedPoints;

  private int possiblePoints;

  public QuestionResult() {
    this.achievedPoints = 0;
    this.possiblePoints = 0;
  }

  public QuestionResult(int achievedPoints, int possiblePoints) {
    this.achievedPoints = achievedPoints;
    this.possiblePoints = possiblePoints;
  }

  public String toString() {
    // TODO: translate this message
    return achievedPoints + " of " + possiblePoints + " points";
  }

  public void setAchievedPoints(int achievedPoints) {
    this.achievedPoints = achievedPoints;
  }

  public int getAchievedPoints() {
    return achievedPoints;
  }

  public void setPossiblePoints(int possiblePoints) {
    this.possiblePoints = possiblePoints;
  }

  public int getPossiblePoints() {
    return possiblePoints;
  }

}
