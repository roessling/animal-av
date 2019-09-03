/*
 * Created on 13.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.sorting.bubblesort;

import generators.framework.Generator;

import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.generators.Language;

public abstract class GdI2BubbleSort extends AbstractBubbleSort implements
    Generator {

  public GdI2BubbleSort(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  /**
   * Bubble Sort swaps neighbours if they are not sorted. It iterates up to n
   * times over the array, regarding only the elements at indices [0, n-i] in
   * iteration i. Run-time complexity in worst case: O(n*n)
   */
  public void sort() {
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    int nrElems = array.getLength();
    ArrayMarker iMarker = null, jMarker = null;
    // highlight method header
    code.highlight("header");
    lang.nextStep();

    // check if array is null
    code.toggleHighlight("header", "ifNull");
    incrementNrComparisons(); // if null
    lang.nextStep();

    // switch to variable declaration

    code.toggleHighlight("ifNull", "variables");
    lang.nextStep();

    // initialize i
    code.toggleHighlight("variables", "initializeI");
    iMarker = installArrayMarker("iMarker", array, nrElems - 1);
    incrementNrAssignments(); // i =...
    lang.nextStep();

    // switch to outer loop
    code.unhighlight("initializeI");

    while (iMarker.getPosition() >= 0) {
      code.highlight("outerLoop");
      lang.nextStep();

      code.toggleHighlight("outerLoop", "innerLoop");
      if (jMarker == null) {
        jMarker = installArrayMarker("jMarker", array, 0);
      } else
        jMarker.move(0, null, DEFAULT_TIMING);
      incrementNrAssignments();
      while (jMarker.getPosition() <= iMarker.getPosition() - 1) {
        incrementNrComparisons();
        lang.nextStep();
        code.toggleHighlight("innerLoop", "if");
        array.highlightElem(jMarker.getPosition() + 1, null, null);
        array.highlightElem(jMarker.getPosition(), null, null);
        incrementNrComparisons();
        lang.nextStep();

        if (array.getData(jMarker.getPosition()) > array.getData(jMarker
            .getPosition() + 1)) {
          // swap elements
          code.toggleHighlight("if", "swap");
          array.swap(jMarker.getPosition(), jMarker.getPosition() + 1, null,
              DEFAULT_TIMING);
          incrementNrAssignments(3); // swap
          lang.nextStep();
          code.unhighlight("swap");
        } else {
          code.unhighlight("if");
        }
        // clean up...
        // lang.nextStep();
        code.highlight("innerLoop");
        array.unhighlightElem(jMarker.getPosition(), null, null);
        array.unhighlightElem(jMarker.getPosition() + 1, null, null);
        if (jMarker.getPosition() <= iMarker.getPosition())
          jMarker.move(jMarker.getPosition() + 1, null, DEFAULT_TIMING);
        incrementNrAssignments(); // j++ will always happen...
      }
      lang.nextStep();
      code.toggleHighlight("innerLoop", "decrementI");
      array.highlightCell(iMarker.getPosition(), null, DEFAULT_TIMING);
      if (iMarker.getPosition() > 0)
        iMarker.move(iMarker.getPosition() - 1, null, DEFAULT_TIMING);
      else
        iMarker.moveBeforeStart(null, DEFAULT_TIMING);
      incrementNrAssignments();
      lang.nextStep();
      code.unhighlight("decrementI");
    }

    // some interaction...
    TrueFalseQuestionModel tfQuestion = new TrueFalseQuestionModel("tfQ");
    tfQuestion.setPrompt("Did this work?");
    tfQuestion.setPointsPossible(1);
    tfQuestion.setGroupID("demo");
    tfQuestion.setCorrectAnswer(true);
    tfQuestion.setFeedbackForAnswer(true, "Great!");
    tfQuestion.setFeedbackForAnswer(false, "argh!");
    lang.addTFQuestion(tfQuestion);

    lang.nextStep();
    HtmlDocumentationModel link = new HtmlDocumentationModel("link");
    link.setLinkAddress("http://www.heise.de");
    lang.addDocumentationLink(link);

    lang.nextStep();
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel("mcq");
    mcq.setPrompt("Which AV system can handle dynamic rewind?");
    mcq.setGroupID("demo");
    mcq.addAnswer("GAIGS", -1, "Only static images, not dynamic");
    mcq.addAnswer("Animal", 1, "Good!");
    mcq.addAnswer("JAWAA", -2, "Not at all!");
    lang.addMCQuestion(mcq);

    lang.nextStep();
    MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel(
        "msq");
    msq.setPrompt("Which of the following AV systems can handle interaction?");
    msq.setGroupID("demo");
    msq.addAnswer("Animal", 2, "Right - I hope!");
    msq.addAnswer("JHAVE", 2, "Yep.");
    msq.addAnswer("JAWAA", 1, "Only in Guido's prototype...");
    msq.addAnswer("Leonardo", -1, "Nope.");
    lang.addMSQuestion(msq);
    lang.nextStep();
    // Text text = lang.newText(new Offset(10, 20, "array", "S"), "Hi", "hi",
    // null);
    // lang.nextStep();
  }
}
