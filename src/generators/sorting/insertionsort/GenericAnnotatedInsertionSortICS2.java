package generators.sorting.insertionsort;

import java.awt.Font;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

public class GenericAnnotatedInsertionSortICS2 extends
    GenericAnnotatedInsertionSort {
  public GenericAnnotatedInsertionSortICS2(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  public void sort() {
    int i, j = 0; // Schleifenzaehler
    int temp; // Speicher fuer aktuelles Element

    // Create marker objects
    ArrayMarker iMarker = null, jMarker = null;

    // Highlight header
    code.highlight("header");

    // Text properties for the "temp=..."
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 13));
    // Create the labels for "temp=.."
    vLabel = lang.newText(new Offset(80, -8, array, AnimalScript.DIRECTION_SE),
        "temp=", "swapLabel", null, textProps);
    vContent = lang.newText(new Offset(0, 0, vLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "vValue", null, textProps);

    lang.nextStep();

    // step: enter outer loop (or not)
    code.toggleHighlight("header", "outerLoop");

    for (i = 1; i < array.getLength(); i++) {
      vContent.setText("", null, null);

      // Move iMarker
      if (iMarker == null)
        iMarker = installArrayMarker("iMarker", array, i);
      else
        iMarker.move(i, null, null);

      incrementNrComparisons(); // i < array.getLength()
      incrementNrAssignments(); // i = 1 (first call), else i++
      lang.nextStep();

      // Highlight j = i
      code.toggleHighlight("outerLoop", "setJ");

      // Move jMarker
      if (jMarker == null)
        jMarker = installArrayMarker("jMarker", array, i);
      else
        jMarker.move(i, null, null);

      j = i;
      incrementNrAssignments(); // j = i
      lang.nextStep();

      // step: temp = array[i]
      code.toggleHighlight("setJ", "takeOut");
      temp = array.getData(i);
      incrementNrAssignments();
      vContent.setText(String.valueOf(temp), null, null);
      array.highlightElem(i, null, null);
      lang.nextStep();

      // step: enter inner loop
      code.toggleHighlight("takeOut", "innerLoop");

      while (j > 0 && array.getData(j - 1) > temp) {
        incrementNrComparisons(2); // j>0 && array[j-1]>temp

        // Fix element highlight in the array
        array.unhighlightElem(j - 1, null, null);
        array.highlightCell(j - 1, null, null);
        lang.nextStep();

        // step: copy value
        code.toggleHighlight("innerLoop", "moveForward");
        array.unhighlightCell(j - 1, null, null);
        array.highlightCell(j, null, null);
        array.put(j, array.getData(j - 1), null, null);
        incrementNrAssignments(); // a[j] = a[j-1]
        lang.nextStep();

        // step: decrement j
        code.toggleHighlight("moveForward", "decrementJ");
        j = j - 1;
        jMarker.move(j, null, null);
        incrementNrAssignments(); // j = j -1
        lang.nextStep();

        // step: re-enter inner loop
        code.toggleHighlight("decrementJ", "innerLoop");
      }

      // update counters for loop
      if (j > 0)
        incrementNrComparisons(2); // j >0, but a[j-1] <=temp
      else
        incrementNrComparisons(); // j <= 0
      lang.nextStep();

      // step: array[j] = temp
      code.toggleHighlight("innerLoop", "insert");
      array.highlightElem(j, null, null);
      array.put(j, temp, null, null);
      incrementNrAssignments(); // a[j] = temp
      lang.nextStep();

      // step: re-enter outer loop
      code.toggleHighlight("insert", "outerLoop");
      array.unhighlightElem(j, null, null);
      array.highlightCell(j, null, null);
    }
    // leaving outer loop
    incrementNrAssignments(); // last assignment in outer loop
    incrementNrComparisons(); // compare with array length

    array.highlightCell(i - 1, null, null);
    code.unhighlight("outerLoop");
  }
}
