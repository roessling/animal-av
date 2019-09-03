package interactionsupport.models;

/**
 * Model representing a question group. The idea behind question groups is that
 * questions that are in the same group should only be asked X times. After X
 * times, it is likely that the user understood this question type.
 * 
 * @author Guido Roessling <roessling@acm.org> / Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class QuestionGroupModel extends InteractionModel {

  /** how often a question of this group should be asked */
  private int numberOfRepeats;

  /** how many questions already have been answered correctly */
  private int correctlyAnswered = 0;

  public QuestionGroupModel(String elementID) {
    super(elementID);
    numberOfRepeats = 0;
  }

  public QuestionGroupModel(String elementID, int numberOfRepeats) {
    super(elementID);
    this.numberOfRepeats = numberOfRepeats;
  }

  /**
   * Retrieves how often a question of this group should be asked.
   * 
   * @return how often a question of this group should be asked
   */
  public int getNumberOfRepeats() {
    return numberOfRepeats;
  }

  /**
   * Sets how often a question of this group should be asked.
   * 
   * @param repeats
   *          the number of repeats
   */
  public void setNumberOfRepeats(int repeats) {
    numberOfRepeats = repeats;
  }

  /**
   * Retrieves how often a question of this group has already been answered
   * correctly.
   * 
   * @return how often a question of this group has already been answered
   *         correctly
   */
  public int getCorrectlyAnswered() {
    return correctlyAnswered;
  }

  /**
   * Sets how often a question of this group has been answered correctly.
   * 
   * @param correctlyAnswered
   *          the number of correctly answered questions of this group
   */
  public void setCorrectlyAnswered(int correctlyAnswered) {
    this.correctlyAnswered = correctlyAnswered;
  }

  /**
   * Increments the number of correctly answered questions.
   */
  public void incrementCorrectlyAnswered() {
    correctlyAnswered++;
  }

}