/*
 * Created on 13.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.sorting.bubblesort;

import generators.framework.Generator;

import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

public abstract class ImprovedGdI2BubbleSort extends AbstractBubbleSort
    implements Generator {

  public ImprovedGdI2BubbleSort(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  /**
   * Bubble Sort swaps neighbours if they are not sorted. It iterates up to n
   * times over the array, regarding only the elements at indices [0, n-i] in
   * iteration i. Run-time complexity in worst case: O(n*n)
   */
  public void sort() {
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

    code.toggleHighlight("initializeI", "initSwap");
    boolean isSorted = false; // wurde getauscht?
    incrementNrAssignments();
    swapLabel = lang.newText(
        new Offset(30, 0, array, AnimalScript.DIRECTION_SE), "isSorted=",
        "swapLabel", null, (TextProperties) primitiveProps.get("title"));
    swapPerf = lang.newText(new Offset(10, 0, swapLabel,
        AnimalScript.DIRECTION_BASELINE_END), "false", "swapVal", null,
        (TextProperties) primitiveProps.get("title"));
    lang.nextStep();

    // switch to outer loop
    code.unhighlight("initSwap");

    while (iMarker.getPosition() >= 0 && !isSorted) {
      code.highlight("outerLoop");
      lang.nextStep();

      // reset swapPerformed
      code.toggleHighlight("outerLoop", "resetSwap");
      swapPerf.setText("true", null, DEFAULT_TIMING);
      isSorted = true;
      lang.nextStep();

      code.toggleHighlight("resetSwap", "innerLoop");
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
          array.swap(jMarker.getPosition() + 1, jMarker.getPosition(), null,
              DEFAULT_TIMING);
          incrementNrAssignments(3); // swap

          // set swapPerformed to true
          lang.nextStep();
          code.toggleHighlight("swap", "swap=false");
          swapPerf.setText("false", null, DEFAULT_TIMING);
          isSorted = false;

          lang.nextStep();
          code.unhighlight("swap=false");
        } else {
          code.unhighlight("if");
        }
        // clean up...
        // lang.nextStep();
        code.highlight("innerLoop");
        array.unhighlightElem(jMarker.getPosition() + 1, null, null);
        array.unhighlightElem(jMarker.getPosition(), null, null);
        if (jMarker.getPosition() <= iMarker.getPosition())
          jMarker.move(jMarker.getPosition() + 1, null, DEFAULT_TIMING);
        incrementNrAssignments(); // j++ will always happen...
      }
      lang.nextStep();
      code.toggleHighlight("innerLoop", "decrementI");
      array.highlightCell(iMarker.getPosition(), null, DEFAULT_TIMING);
      iMarker.move(iMarker.getPosition() - 1, null, DEFAULT_TIMING);
      incrementNrAssignments();
      lang.nextStep();
      code.unhighlight("decrementI");
    }
  }
}
