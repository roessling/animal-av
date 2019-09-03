/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.searching.straightselection;

import generators.framework.Generator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.util.Offset;

/**
 * Implements a generic iterative straight selection algorithm on a String array
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 1.1 2008-06-23
 */
public class GenericIterativeStraightStringSelection extends
    AbstractStraightStringSelection implements Generator {

  public GenericIterativeStraightStringSelection(String aResourceName,
      Locale aLocale) {
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
    String valueToSearchFor = (String) prims.get("value");
    // int resultIndex =
    search(valueToSearchFor);
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  protected int search(String valueToSearchFor) {
    ArrayMarker iMarker = null;

    valueText = installText("value", "value: " + valueToSearchFor, new Offset(
        30, 0, array, AnimalScript.DIRECTION_SE));

    // highlight method header
    code.highlight("header");
    lang.nextStep();
    code.toggleHighlight("header", "ifNull");
    incrementNrComparisons();
    lang.nextStep();

    // switch to variable declaration
    code.toggleHighlight("ifNull", "getArrayLength");
    int nrElems = array.getLength();
    incrementNrAssignments();
    lang.nextStep();

    // initialize loop counter to 0
    code.toggleHighlight("getArrayLength", "installMarker");
    iMarker = installArrayMarker("iMarker", array, 0);
    array.highlightElem(iMarker.getPosition(), null, null);
    incrementNrAssignments(); // i = 0
    lang.nextStep();

    // switch to start of while loop
    code.toggleHighlight("installMarker", "whileLoop");
    while (iMarker.getPosition() < nrElems
        && !(array.getData(iMarker.getPosition()).equals(valueToSearchFor))) {
      incrementNrComparisons(3); // <, !=, &&
      lang.nextStep();

      // move marker
      code.toggleHighlight("whileLoop", "moveMarker");
      array.unhighlightElem(iMarker.getPosition(), null, null);
      iMarker.move(iMarker.getPosition() + 1, null, DEFAULT_TIMING);
      array.highlightElem(iMarker.getPosition(), DEFAULT_TIMING, null);
      incrementNrAssignments(); // i++
      lang.nextStep();

      code.toggleHighlight("moveMarker", "whileLoop");
    }

    // end of loop
    if (iMarker.getPosition() < nrElems)
      incrementNrComparisons(3); // all 3 comparisons performed
    else
      incrementNrComparisons(); // end found
    code.toggleHighlight("whileLoop", "outerIf");
    incrementNrComparisons(); // i < array.length
    if (iMarker.getPosition() <= nrElems) {
      array.highlightCell(iMarker.getPosition(), null, null);
      lang.nextStep();
      code.toggleHighlight("outerIf", "return value");
      result = installText("value", translator.translateMessage("result",
          new Integer[] { new Integer(iMarker.getPosition()) }), new Offset(20,
          0, valueText, AnimalScript.DIRECTION_BASELINE_END));
      return iMarker.getPosition();
    }

    // not found!
    lang.nextStep();
    code.toggleHighlight("outerIf", "notFound");
    array.unhighlightElem(iMarker.getPosition() - 1, null, null);
    result = installText("value", translator.translateMessage("result",
        new Integer[] { new Integer(iMarker.getPosition()) }), new Offset(20,
        0, valueText, AnimalScript.DIRECTION_BASELINE_END));

    return -1;
  }
}