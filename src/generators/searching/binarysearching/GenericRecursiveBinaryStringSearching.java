/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.searching.binarysearching;

import generators.framework.Generator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Text;
import algoanim.util.Offset;

public class GenericRecursiveBinaryStringSearching extends
    AbstractBinaryStringSearching implements Generator {

  protected Text lPos = null, rPos = null;

  ArrayMarker    lMarker = null, rMarker = null, midMarker = null;

  public GenericRecursiveBinaryStringSearching(String aResourceName,
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
    search(valueToSearchFor, 0, array.getLength() - 1);
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  protected int search(String valueToSearchFor, int lowerBound, int upperBound) {

    if (valueText == null) { // set up!
      valueText = installText("value", "value: " + valueToSearchFor,
          new Offset(30, 0, array, AnimalScript.DIRECTION_SE));
      lPos = installText("value", "l: " + lowerBound, new Offset(20, 0,
          valueText, AnimalScript.DIRECTION_BASELINE_END));
      rPos = installText("value", "r: " + upperBound, new Offset(20, 0, lPos,
          AnimalScript.DIRECTION_BASELINE_END));
      lMarker = installArrayMarker("lMarker", array, lowerBound);
      rMarker = installArrayMarker("rMarker", array, upperBound);
    } else {
      lPos.setText("l: " + lowerBound, null, DEFAULT_TIMING);
      rPos.setText("r: " + upperBound, null, DEFAULT_TIMING);
      lMarker.move(lowerBound, null, DEFAULT_TIMING);
      rMarker.move(upperBound, null, DEFAULT_TIMING);
    }
    // unhighlight "uninteresting" elements
    if (lowerBound > 0)
      array.unhighlightCell(0, lowerBound - 1, null, null);
    if (upperBound < array.getLength() - 1)
      array.unhighlightCell(upperBound + 1, array.getLength() - 1, null, null);
    // highlight "current" elements
    array.highlightCell(lowerBound, upperBound, null, null);

    // highlight method header
    code.highlight("header");
    lang.nextStep();

    code.toggleHighlight("header", "ifNull");
    incrementNrComparisons();
    lang.nextStep();
    if (valueToSearchFor == null) {
      code.unhighlight("ifNull");
      lang.nextStep();
      return -1;
    }

    code.toggleHighlight("ifNull", "invalidIndex");
    incrementNrComparisons(5);
    if (lowerBound > upperBound || lowerBound < 0
        || upperBound >= array.getLength()) {
      lang.nextStep();
      code.toggleHighlight("invalidIndex", "invalidIndexReturn");
      result = installText("value", translator.translateMessage("result",
          new Integer[] { new Integer(-1) }), new Offset(20, 0, valueText,
          AnimalScript.DIRECTION_BASELINE_END));
      lang.nextStep();
      code.unhighlight("invalidIndexReturn");
      return -1;
    }
    lang.nextStep();

    code.toggleHighlight("invalidIndex", "installMidMarker");
    if (midMarker == null) {
      midMarker = installArrayMarker("midMarker", array,
          (lowerBound + upperBound) / 2);
    } else {
      midMarker.move((lowerBound + upperBound) / 2, null, DEFAULT_TIMING);
    }
    incrementNrAssignments(); // set mid
    array.highlightElem(midMarker.getPosition(), null, null);
    lang.nextStep();

    code.toggleHighlight("installMidMarker", "checkFound");
    incrementNrComparisons(); // mid ==?
    if (valueToSearchFor.equals(array.getData(midMarker.getPosition()))) {
      lang.nextStep();
      code.toggleHighlight("checkFound", "found");
      result = installText("value", translator.translateMessage("result",
          new Integer[] { new Integer(midMarker.getPosition()) }), new Offset(
          20, 0, rPos, AnimalScript.DIRECTION_BASELINE_END));
      // array.highlightCell(midMarker.getPosition(), null, DEFAULT_TIMING);
      lang.nextStep();
      code.unhighlight("found");
      return midMarker.getPosition();
    } else if (valueToSearchFor
        .compareTo(array.getData(midMarker.getPosition())) < 0) {
      code.toggleHighlight("checkFound", "ifLess");
      incrementNrComparisons(); // else if <
      lang.nextStep();
      code.toggleHighlight("ifLess", "continueLeft");
      lang.nextStep();

      code.unhighlight("continueLeft");
      int resultingPosition = search(valueToSearchFor, lowerBound,
          midMarker.getPosition() - 1);
      code.highlight("continueLeft");
      // re-highlight
      if (lowerBound > 0)
        array.unhighlightCell(0, lowerBound - 1, null, null);
      if (upperBound < array.getLength() - 1)
        array.unhighlightCell(upperBound + 1, null, null);
      array.highlightCell(lowerBound, upperBound, null, null);
      lPos.setText("l: " + lMarker.getPosition(), null, null);
      rPos.setText("r: " + rMarker.getPosition(), null, null);
      lang.nextStep();
      return resultingPosition;
    }
    code.toggleHighlight("checkFound", "ifLess");
    incrementNrComparisons(); // else if <
    lang.nextStep();
    code.toggleHighlight("ifLess", "continueRight");
    lang.nextStep();

    code.unhighlight("continueRight");
    int resultingPosition = search(valueToSearchFor,
        midMarker.getPosition() + 1, upperBound);
    code.highlight("continueRight");
    // re-highlight
    if (lowerBound > 0)
      array.unhighlightCell(0, lowerBound - 1, null, null);
    if (upperBound < array.getLength() - 1)
      array.unhighlightCell(upperBound + 1, null, null);
    array.highlightCell(lowerBound, upperBound, null, null);
    lPos.setText("l: " + lMarker.getPosition(), null, null);
    rPos.setText("r: " + rMarker.getPosition(), null, null);

    lang.nextStep();
    return resultingPosition;

  }

  /**
   * hides the array, code, and number of steps taken from the display
   */
  protected void hideNrStepsArrayCode() {
    super.hideNrStepsArrayCode();
    if (lPos != null)
      lPos.hide();
    if (rPos != null)
      rPos.hide();
  }
}