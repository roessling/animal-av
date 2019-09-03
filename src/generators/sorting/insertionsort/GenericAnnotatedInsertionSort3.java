package generators.sorting.insertionsort;

import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class GenericAnnotatedInsertionSort3 extends
    GenericAnnotatedInsertionSort {
  public GenericAnnotatedInsertionSort3(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  public void sort() {
    int i, j = 0; // Schleifenzaehler
    int temp; // Speicher fuer aktuelles Element

    ArrayMarker iMarker = null, jMarker = null;
    TicksTiming defaultTiming = new TicksTiming(20);

    // step: function header
    code.highlight("header");
    lang.nextStep();

    // step: variable declaration
    code.toggleHighlight("header", "variables");
    vLabel = lang.newText(new Offset(0, 35, nrCompLabel,
        AnimalScript.DIRECTION_NW), "temp =", "temp=", null,
        (TextProperties) primitiveProps.get("title"));
    vContent = lang.newText(new Offset(10, 0, vLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "vValue", null,
        (TextProperties) primitiveProps.get("title"));
    lang.nextStep();

    // step: curentPos = 1
    code.toggleHighlight("variables", "setCurrentPos");
    if (iMarker == null) {
      iMarker = installArrayMarker("iMarker", array, 1);
    }
    // else {
    // iMarker.move(1, defaultTiming, null);
    // }
    i = 1;
    incrementNrAssignments(); // i = 1
    lang.nextStep();

    // step: enter outer while loop
    code.toggleHighlight("setCurrentPos", "outerLoop");

    while (i < array.getLength()) {
      incrementNrComparisons(); // i < array.getLength()
      lang.nextStep();

      // step: set insert pos
      code.toggleHighlight("outerLoop", "setInsertPos");
      j = i;
      incrementNrAssignments(); // j = i
      if (jMarker == null)
        jMarker = installArrayMarker("jMarker", array, i);
      else
        jMarker.move(i, defaultTiming, null);
      lang.nextStep();

      // step: temp = array[i]
      code.toggleHighlight("setInsertPos", "takeOut");
      temp = array.getData(i);
      vContent.setText(String.valueOf(temp), null, null);
      incrementNrAssignments();
      array.highlightElem(i, null, null);
      lang.nextStep();

      // step: enter (peraps) inner loop
      code.toggleHighlight("takeOut", "innerLoop");

      while (j > 0 && array.getData(j - 1) > temp) {
        incrementNrComparisons(2); // j>0, array[j-1] > temp
        array.highlightCell(i - 1, null, null);
        array.unhighlightElem(i, null, null);
        lang.nextStep();

        // step: array[j] = array[j - 1]
        code.toggleHighlight("innerLoop", "moveForward");
        array.put(j, array.getData(j - 1), defaultTiming, null);
        incrementNrAssignments();
        array.highlightCell(j, null, null);
        array.unhighlightCell(j - 1, null, null);
        lang.nextStep();

        // step: j--
        code.toggleHighlight("moveForward", "decrementInsertPos");
        j = j - 1;
        jMarker.move(j, defaultTiming, null);
        incrementNrAssignments(); // j--
        lang.nextStep();

        // step: reenter inner loop (or leave it)
        code.toggleHighlight("decrementInsertPos", "innerLoop");
      }
      if (j > 0)
        incrementNrComparisons(2); // j > 0, but array[j-1] == false
      else
        incrementNrComparisons(); // j <= 0
      lang.nextStep();

      // step: array[j] = temp
      code.toggleHighlight("innerLoop", "insert");
      array.highlightElem(j, null, null);
      array.put(j, temp, null, null);
      incrementNrAssignments(); // array[j] = temp
      lang.nextStep();

      // step: i++
      code.toggleHighlight("insert", "incrementCurrentPos");
      i = i + 1;
      array.unhighlightElem(j, null, null);
      array.highlightCell(j, null, null);
      iMarker.move(i, defaultTiming, null);
      lang.nextStep();

      // step: re-enter outer loop
      code.toggleHighlight("incrementCurrentPos", "outerLoop");
    }
    incrementNrComparisons(); // outer loop comparison failed

    array.highlightCell(i - 1, null, null);
    code.unhighlight("outerLoop");
  }
}
