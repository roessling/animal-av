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
import algoanim.primitives.Text;
import algoanim.util.Offset;

public class GenericRecursiveStraightIntSelection extends
    AbstractStraightIntSelection implements Generator {

  protected Text cPos = null;

  ArrayMarker iMarker = null;

  public GenericRecursiveStraightIntSelection(String aResourceName,
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
    search(valueToSearchFor, 0);
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  protected int search(int valueToSearchFor, int pos) {
    if (pos == 0) { // set up!
      valueText = installText("value", "value: " + valueToSearchFor,
          new Offset(30, 0, array, AnimalScript.DIRECTION_SE));
      cPos = installText("value", "pos: " + pos, new Offset(20, 0, valueText,
          AnimalScript.DIRECTION_BASELINE_END));
      iMarker = installArrayMarker("iMarker", array, 0);
    } else {
      cPos.setText("pos: " + pos, null, DEFAULT_TIMING);
      iMarker.move(pos, null, DEFAULT_TIMING);
    }

    // highlight method header
    code.highlight("header");
    lang.nextStep();

    code.toggleHighlight("header", "ifNull");
    incrementNrComparisons();
    lang.nextStep();

    code.toggleHighlight("ifNull", "ifAtEnd");
    incrementNrComparisons();
    if (pos >= array.getLength()) {
      lang.nextStep();
      code.toggleHighlight("ifAtEnd", "notFound");
      result = installText("value", translator.translateMessage("result",
          new Integer[] { new Integer(-1) }), new Offset(20, 0, cPos,
          AnimalScript.DIRECTION_BASELINE_END));
      lang.nextStep();
      code.unhighlight("notFound");
      return -1;
    }
    lang.nextStep();

    code.toggleHighlight("ifAtEnd", "ifMatch");
    incrementNrComparisons();
    array.highlightElem(iMarker.getPosition(), null, null);

    lang.nextStep();
    if (array.getData(pos) == valueToSearchFor) {
      code.toggleHighlight("ifMatch", "return value");
      result = installText("value", translator.translateMessage("result",
          new Integer[] { new Integer(pos) }), new Offset(20, 0, cPos,
          AnimalScript.DIRECTION_BASELINE_END));
      array.highlightCell(pos, null, DEFAULT_TIMING);
      lang.nextStep();
      code.unhighlight("return value");
      return pos;
    }

    code.unhighlight("ifMatch");
    if (iMarker.getPosition() != pos)
      iMarker.move(pos, null, DEFAULT_TIMING);
    code.highlight("recursion");
    lang.nextStep();
    code.unhighlight("recursion");
    int resultingPos = search(valueToSearchFor, pos + 1);
    iMarker.move(pos, null, DEFAULT_TIMING);
    array.unhighlightElem(pos, null, null);
    lang.nextStep();
    return resultingPos;
  }

  /**
   * hides the array, code, and number of steps taken from the display
   */
  protected void hideNrStepsArrayCode() {
    super.hideNrStepsArrayCode();
    if (cPos != null)
      cPos.hide();
  }
}