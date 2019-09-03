package interactionsupport.models;

/**
 * Model representing a multiple choice question. In a multiple choice question,
 * the user has to choose exactly one answers from a list of possible answers.
 * Only one answer is completely correct, but there may be partial points for
 * answers that are partly right.
 * 
 * @author Guido Roessling <roessling@acm.org> / Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class MultipleChoiceQuestionModel extends QuestionModel {

  public MultipleChoiceQuestionModel(String id) {
    super(id);
  }

}
