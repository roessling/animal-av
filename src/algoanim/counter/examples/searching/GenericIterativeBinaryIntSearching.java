/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package algoanim.counter.examples.searching;

import generators.framework.Generator;
import generators.framework.properties.AnimationPropertiesContainer;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.parser.InteractionFactory;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.generators.Language;
import algoanim.util.Offset;

public class GenericIterativeBinaryIntSearching extends
    AbstractBinaryIntSearching implements Generator {
  
  private InteractionFactory f;
  
  public GenericIterativeBinaryIntSearching(String aResourceName, Locale aLocale) {
    super(aResourceName, aLocale);
  }

  /**
   * generates the animation
   * 
   * @param props
   *          the properties given by the animation viewer
   * @param prims
   *          the primitive objects as given by the animation viewer
   * @return the String output for the animation
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    setUpDefaultElements(props, prims, "array", "code", "code", 0, 20);
    int valueToSearchFor = ((Integer) prims.get("value")).intValue();
    
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    lang.addQuestionGroup(new QuestionGroupModel("mid", 2));
    f = new InteractionFactory(lang, "InteractionPatterns.xml");
    // int resultIndex =
    search(valueToSearchFor);
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  protected int search(int valueToSearchFor) {
    ArrayMarker lMarker = null, rMarker = null, midMarker = null;
    
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel("Eigenschaft");
    mcq.setPrompt("Welche Eigenschaft muss erf&uuml;llt sein, damit eine Bin&auml;re Suche erfolgreich ist?");
    mcq.addAnswer("Das Suchfeld muss sortiert sein", 2, "Ohne vorherige Sortierung findet die Bin&auml;re Suche nur zuf&auml;llig das richtige Element");
    mcq.addAnswer("Das Suchfeld darf nicht mehr als 1024 Elemente beinhalten", -1, "Die Gr&ouml;&szlig;e des Suchraums ist nicht begrenzt");
    mcq.addAnswer("Das Suchfeld muss eine gemischte Folge von Zahlen und Buchstaben enthalten", 1, 
        "Zwar k&ouml;nnen Buchstaben anhand ihrer Zahlenrepr&auml;sentation sortiert werden, jedoch ist die Verwendung von Zahlen und Buchstaben im Suchfeld optional");
    lang.addMCQuestion(mcq);
    lang.nextStep();
    
    valueText = installText("value", "value: " + valueToSearchFor, new Offset(
        30, 0, array, AnimalScript.DIRECTION_SE));

    // highlight method header
    code.highlight("header");
    lang.nextStep();
    code.toggleHighlight("header", "ifNull");
    incrementNrComparisons(2); // null, length == 0
    lang.nextStep();

    // switch to variable declaration
    code.toggleHighlight("ifNull", "getArrayLength");
    int nrElems = array.getLength();
    incrementNrAssignments();
    lang.nextStep();

    // initialize l counter to 0
    code.toggleHighlight("getArrayLength", "installLMarker");
    lMarker = installArrayMarker("lMarker", array, 0);
    incrementNrAssignments(); // l = 0
    lang.nextStep();

    // initialize r counter to arrayLength - 1
    code.toggleHighlight("installLMarker", "installRMarker");
    rMarker = installArrayMarker("rMarker", array, nrElems - 1);
    incrementNrAssignments(); // r = nrElems - 1
    array.highlightCell(0, nrElems - 1, DEFAULT_TIMING, null);
    lang.nextStep();

    // initialize r counter to arrayLength - 1
    code.toggleHighlight("installRMarker", "installMidMarker");
    midMarker = installArrayMarker("midMarker", array,
        (lMarker.getPosition() + rMarker.getPosition()) / 2);
    array.highlightElem(midMarker.getPosition(), null, null);
    incrementNrAssignments(); // r = nrElems - 1
    lang.nextStep();
    
    FillInBlanksQuestionModel fib = new FillInBlanksQuestionModel("Iterationen");
    fib.setPrompt("Wieviele Iterationen ben&ouml;tigt die Bin&auml;re Suche um das gesuchte Element zu finden?");
    lang.addFIBQuestion(fib);
    lang.nextStep();
    
    FillInBlanksQuestionModel fib2 = f.generateFIBQuestion("nrComparisons", "AnzahlVergleiche");
    
    // switch to start of while loop
    code.toggleHighlight("installMidMarker", "whileLoop");
    
    int iteration = 0;
    // while (r > l && array[mid] != value) {
    while (rMarker.getPosition() > lMarker.getPosition()
        && array.getData(midMarker.getPosition()) != valueToSearchFor) {
      iteration++;
      incrementNrComparisons(3); // >, !=, &&
      lang.nextStep();

      // check if less than
      code.toggleHighlight("whileLoop", "ifLess");
      incrementNrComparisons(); // if (<)
      lang.nextStep();

      MultipleChoiceQuestionModel mcq2 = new MultipleChoiceQuestionModel("midElement" + iteration);
      mcq2.setPrompt("Welches ist das n&auml;chste, mittlere Element?");
      mcq2.setGroupID("mid");
      lang.addMCQuestion(mcq2);
      lang.nextStep();
      
      if (valueToSearchFor < array.getData(midMarker.getPosition())) {

        code.toggleHighlight("ifLess", "continueLeft");
        array.unhighlightCell(midMarker.getPosition(), rMarker.getPosition(),
            DEFAULT_TIMING, DEFAULT_TIMING);
        rMarker.move(midMarker.getPosition() - 1, null, DEFAULT_TIMING); // r =
                                                                          // mid
                                                                          // -1
        incrementNrAssignments();
        lang.nextStep();

        code.unhighlight("continueLeft");
      } else {
        code.toggleHighlight("ifLess", "continueRight");
        array.unhighlightCell(lMarker.getPosition(), midMarker.getPosition(),
            DEFAULT_TIMING, DEFAULT_TIMING);
        lMarker.move(midMarker.getPosition() + 1, null, DEFAULT_TIMING); // l =
                                                                          // mid
                                                                          // + 1
        incrementNrAssignments();
        lang.nextStep();

        code.unhighlight("continueRight");
      }
      code.highlight("updateMidElem");
      array.unhighlightElem(midMarker.getPosition(), null, null);
      midMarker.move((lMarker.getPosition() + rMarker.getPosition()) / 2, null,
          DEFAULT_TIMING); // mid = (l + r) / 2
      array.highlightElem(midMarker.getPosition(), null, null);
      
      mcq2.addAnswer("" + array.getData(midMarker.getPosition()), 1, "Das ist korrekt");
      if(midMarker.getPosition() == array.getLength() - 1) {
        mcq2.addAnswer("" + array.getData(midMarker.getPosition() - 2), -1, 
            "Das n&auml;chste mittlere Element lautet: " + array.getData(midMarker.getPosition()));
      } else {
        mcq2.addAnswer("" + array.getData(midMarker.getPosition() + 1), -1, 
            "Das n&auml;chste mittlere Element lautet: " + array.getData(midMarker.getPosition()));
      }
      if(midMarker.getPosition() == 0) {
        mcq2.addAnswer("" + array.getData(midMarker.getPosition() + 2), -1, 
            "Das n&auml;chste mittlere Element lautet: " + array.getData(midMarker.getPosition()));
      } else {
        mcq2.addAnswer("" + array.getData(midMarker.getPosition() - 1), -1, 
            "Das n&auml;chste mittlere Element lautet: " + array.getData(midMarker.getPosition()));
      }
      
      incrementNrAssignments();
      lang.nextStep();

      code.toggleHighlight("updateMidElem", "whileLoop");
    }
    
    incrementNrComparisons(3); // last iteration
    lang.nextStep();

    code.toggleHighlight("whileLoop", "checkFound");
    incrementNrComparisons();
    fib.addAnswer("" + iteration, 5, "Die korrekte Antwort lautet: " + iteration);
    fib2.addAnswer("" + nrComparisons, 5, "Die korrekte Antwort lautet: " + nrComparisons);
    
    lang.nextStep();
    if (array.getData(midMarker.getPosition()) == valueToSearchFor) {
      code.toggleHighlight("checkFound", "found");
      result = installText("value", translator.translateMessage("result",
          new Integer[] { new Integer(midMarker.getPosition()) }), new Offset(
          20, 0, valueText, AnimalScript.DIRECTION_BASELINE_END));
      lang.nextStep();
      code.unhighlight("found");
      return midMarker.getPosition();
    }
    code.toggleHighlight("checkFound", "notFound");
    result = installText("value", translator.translateMessage("result",
         new Integer[] { new Integer(-1) }), new Offset(20, 0, valueText,
        AnimalScript.DIRECTION_BASELINE_END));
    lang.nextStep();
    code.unhighlight("notFound");
    return -1;
  }
}