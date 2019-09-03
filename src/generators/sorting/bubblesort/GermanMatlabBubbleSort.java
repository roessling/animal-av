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

public class GermanMatlabBubbleSort extends AbstractBubbleSort implements
    Generator {
  public GermanMatlabBubbleSort() {
    super("resources/MatlabBubbleSort", Locale.GERMANY);
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

    // switch to variable declaration
    code.toggleHighlight("header", "variables");
    int i, j; // Schleifenzaehler

    lang.nextStep();
    incrementNrAssignments(2);
    // switch to init for swapPerformed
    code.toggleHighlight("variables", "initSwap");
    boolean swapPerformed = true; // wurde getauscht?
    incrementNrAssignments();
    swapLabel = lang.newText(
        new Offset(30, 0, array, AnimalScript.DIRECTION_SE), "swapPerformed=",
        "swapLabel", null, (TextProperties) primitiveProps.get("title"));
    swapPerf = lang.newText(new Offset(10, 0, swapLabel,
        AnimalScript.DIRECTION_BASELINE_END), "1", "swapVal", null,
        (TextProperties) primitiveProps.get("title"));

    // create i marker
    lang.nextStep();
    code.unhighlight("initSwap");
    incrementNrAssignments();

    for (i = nrElems - 1; swapPerformed && i >= 0; i--) {
      code.highlight("outerLoop");
      if (i == nrElems - 1) {
        iMarker = installArrayMarker("iMarker", array, nrElems - 1);
        iMarker.moveOutside(null, DEFAULT_TIMING);
      } else {
        iMarker.move(i, null, DEFAULT_TIMING);
        array.highlightCell(i + 1, null, DEFAULT_TIMING);
      }
      nrComparisons++;
      incrementNrAssignments(); // i-- [i = nrElems in first iteration]

      // reset swapPerformed
      lang.nextStep();
      code.toggleHighlight("outerLoop", "resetSwap");
      swapPerf.setText("0", null, DEFAULT_TIMING);
      swapPerformed = false;

      // create j marker on entering the loop
      lang.nextStep();
      for (j = 1; j <= i; j++) {
        code.toggleHighlight("resetSwap", "innerLoop");
        if (jMarker == null) {
          jMarker = installArrayMarker("jMarker", array, j);
        } else
          jMarker.move(j, null, DEFAULT_TIMING);
        incrementNrAssignments(); // j = 1 // j++
        incrementNrComparisons(); // j < i

        // compare a[j-1], a[j]
        lang.nextStep();
        code.toggleHighlight("innerLoop", "if");
        array.highlightElem(j - 1, null, null);
        array.highlightElem(j, null, null);

        incrementNrComparisons();
        lang.nextStep();
        if (array.getData(j - 1) > array.getData(j)) {
          // swap elements
          code.toggleHighlight("if", "swap");
          array.swap(j - 1, j, null, DEFAULT_TIMING);
          incrementNrAssignments(3); // swap

          // set swapPerformed to true
          lang.nextStep();
          code.toggleHighlight("swap", "swap=true");
          swapPerf.setText("1", null, DEFAULT_TIMING);
          swapPerformed = true;

          // clean up...
          lang.nextStep();
          code.unhighlight("swap=true");
        } else {
          code.unhighlight("if");
        }
        array.unhighlightElem(j - 1, null, null);
        array.unhighlightElem(j, null, null);
      } // for j...
      incrementNrComparisons(); // last iteration of inner loop
      incrementNrAssignments(); // last iteration of inner loop
      code.highlight("endOuterLoop");
      lang.nextStep();
      code.unhighlight("endOuterLoop");
    }
    incrementNrComparisons(); // last iteration of outer loop
    incrementNrAssignments(); // last increment of outer loop
  }

}
