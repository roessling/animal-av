/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.searching.interpolatedsearching;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Text;
import algoanim.util.Offset;

public class GenericRecursiveInterpolatedIntSearching extends
    AbstractInterpolatedIntSearching implements Generator {

  protected Text lPos = null, rPos = null;

  ArrayMarker lMarker = null, rMarker = null, midMarker = null;

  public GenericRecursiveInterpolatedIntSearching(String aResourceName,
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
    int valueToSearchFor = ((Integer) prims.get("value")).intValue();
    search(valueToSearchFor, 0, array.getLength() - 1);
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  protected int search(int valueToSearchFor, int lowerBound, int upperBound) {

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
    int targetPos = lMarker.getPosition()
        + ((valueToSearchFor - array.getData(lMarker.getPosition())) * (rMarker
            .getPosition() - lMarker.getPosition()))
        / (array.getData(rMarker.getPosition()
            - array.getData(lMarker.getPosition())));
    if (midMarker == null) {
      midMarker = installArrayMarker("midMarker", array, targetPos);
    } else {
      midMarker.move(targetPos, null, DEFAULT_TIMING);
    }
    incrementNrAssignments(); // set mid
    array.highlightElem(midMarker.getPosition(), null, null);
    lang.nextStep();

    code.toggleHighlight("installMidMarker", "checkFound");
    
    
    incrementNrComparisons(); // mid ==?
    
    
    CheckpointUtils.checkpointEvent(this, "Recursion", 
        new Variable("SearchVal", valueToSearchFor),
        new Variable("midIndex", targetPos),
        new Variable("midVal", array.getData(targetPos)),
        new Variable("lowIndex", lMarker.getPosition()),
        new Variable("highIndex", rMarker.getPosition())) 
        ;
    
  /*  CheckpointUtils.checkpointEvent(this, "Recursion", 
        new Variable("SearchVal", valueToSearchFor),
        new Variable("midIndex", targetPos),
        new Variable("midVal", array.getData(targetPos)),
        new Variable("lowIndex", lMarker.getPosition()),
        new Variable("lowVal", array.getData(lMarker.getPosition())),
        new Variable("highIndex", rMarker.getPosition()), 
        new Variable("highVal", array.getData(rMarker.getPosition()))
        );
        */
    
    
    if (array.getData(midMarker.getPosition()) == valueToSearchFor) {
      lang.nextStep();
      code.toggleHighlight("checkFound", "found");
      result = installText("value", translator.translateMessage("result",
          new Integer[] { new Integer(midMarker.getPosition()) }), new Offset(
          20, 0, rPos, AnimalScript.DIRECTION_BASELINE_END));
      // array.highlightCell(midMarker.getPosition(), null, DEFAULT_TIMING);
      lang.nextStep();
      code.unhighlight("found");
      return midMarker.getPosition();
    } else if (valueToSearchFor < array.getData(midMarker.getPosition())) {
      code.toggleHighlight("checkFound", "ifLess");
      incrementNrComparisons(); // else if <
      lang.nextStep();
      code.toggleHighlight("ifLess", "continueLeft");
      lang.nextStep();

      code.unhighlight("continueLeft");
      int resultingPosition = search(valueToSearchFor, lowerBound, midMarker
          .getPosition() - 1);
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