package generators.sorting.quicksort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class DelphiAnnotatedQuickSort2 extends AnimatedIntArrayAlgorithm
    implements Generator {
  protected Text   iLoLabel, iHiLabel, midLabel, tLabel;
  protected Text   iLoValue, iHiValue, midValue, tValue;
  protected Locale contentLocale = null;

  String           resourceName;
  Locale           locale;

  public DelphiAnnotatedQuickSort2() {
    this("resources/DelphiQuickSort", Locale.GERMANY);
  }

  public DelphiAnnotatedQuickSort2(String aResourceName, Locale aLocale) {
    resourceName = aResourceName;
    locale = aLocale;
    init();
  }

  public void init() {
    translator = new Translator(resourceName, locale);
    primitiveProps = new Hashtable<String, AnimationProperties>(59);
    localType = new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    contentLocale = locale;
  }

  /**
   * hides the array, code, and number of steps taken from the display
   */
  protected void hideNrStepsArrayCode() {
    super.hideNrStepsArrayCode();
    if (array != null)
      array.hide();
  }

  public void sort() {
    iLoLabel = lang.newText(new Offset(0, 10, nrCompLabel,
        AnimalScript.DIRECTION_SW), "iLo =", "iLoLabel", null,
        (TextProperties) primitiveProps.get("title"));
    iLoValue = lang.newText(new Offset(10, 0, iLoLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "iLoValue", null,
        (TextProperties) primitiveProps.get("title"));
    iHiLabel = lang.newText(new Offset(0, 10, iLoLabel,
        AnimalScript.DIRECTION_SW), "iHi =", "iHiLabel", null,
        (TextProperties) primitiveProps.get("title"));
    iHiValue = lang.newText(new Offset(10, 0, iHiLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "iHiValue", null,
        (TextProperties) primitiveProps.get("title"));
    tLabel = lang.newText(new Offset(100, 0, iLoLabel,
        AnimalScript.DIRECTION_BASELINE_END), "T =", "tLabel", null,
        (TextProperties) primitiveProps.get("title"));
    tValue = lang.newText(new Offset(10, 0, tLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "tValue", null,
        (TextProperties) primitiveProps.get("title"));
    midLabel = lang.newText(new Offset(100, 0, iHiLabel,
        AnimalScript.DIRECTION_BASELINE_END), "Mid =", "midLabel", null,
        (TextProperties) primitiveProps.get("title"));
    midValue = lang.newText(new Offset(10, 0, midLabel,
        AnimalScript.DIRECTION_BASELINE_END), "", "midValue", null,
        (TextProperties) primitiveProps.get("title"));

    quickSort(0, (array.getLength() - 1), 0);
  }

  private String makeLabel(int l, int r, int depth) {
    StringBuilder sb = new StringBuilder(32);
    for (int i = 0; i < depth; i++)
      sb.append(' ');
    sb.append("quicksort(").append(l).append(", ").append(r);
    sb.append(")");
    return sb.toString();
  }

  public void quickSort(int iLo, int iHi, int depth) {
    ArrayMarker loMarker = null, hiMarker = null; // , midMarker = null;
    int lo, hi, mid, t = 0;
    // boolean done = false;

    code.highlight("header");
    array.unhighlightCell(0, array.getLength() - 1, null, null);
    array.highlightCell(iLo, iHi, null, null);
    iLoValue.setText(String.valueOf(iLo), null, null);
    iHiValue.setText(String.valueOf(iHi), null, null);
    lang.nextStep(makeLabel(iLo, iHi, depth));

    code.toggleHighlight("header", "variables");
    lang.nextStep();

    // Lo := iLo;
    code.toggleHighlight("variables", "setLo");
    lo = iLo;
    // if (loMarker != null)
    // System.err.println(loMarker.getPosition());
    if (loMarker == null)
      loMarker = installArrayMarker("Lo", array, lo);
    // else
    // loMarker.move(lo, null, null);
    incrementNrAssignments();
    lang.nextStep();

    // Hi := iHi;
    code.toggleHighlight("setLo", "setHi");
    hi = iHi;
    if (hiMarker == null)
      hiMarker = installArrayMarker("Hi", array, hi);
    // else
    // hiMarker.move(hi, null, null);
    incrementNrAssignments();
    lang.nextStep();

    // Mid := A[(Lo + Hi) div 2];
    code.toggleHighlight("setHi", "setMid");
    mid = array.getData((lo + hi) / 2);
    midValue.setText(String.valueOf(mid), null, null);
    // if (midMarker == null)
    // midMarker = installArrayMarker("Mid", array, (lo + hi) / 2);
    // else
    // midMarker.move((lo + hi) / 2, null, null);
    array.highlightCell(mid, null, null);
    incrementNrAssignments();
    lang.nextStep();

    // repeat
    code.unhighlight("setMid");
    do {
      code.highlight("repeat");
      lang.nextStep();

      // while A[Lo] < Mid do Inc(Lo);
      code.toggleHighlight("repeat", "incLo");
      // array.highlightCell(mid, null, null);
      array.highlightElem(lo, null, null);
      incrementNrComparisons();
      lang.nextStep();
      // System.err.println("lo= " +lo);
      while (array.getData(lo) < mid) {
        // Inc(Lo)
        lo++;
        incrementNrAssignments();
        updateMarker(loMarker, lo, DEFAULT_TIMING);
        // System.err.println("+lo: " +lo +", loM:" +loMarker.getPosition());
        array.unhighlightElem(lo - 1, null, null);
        array.highlightElem(lo, null, null);
        lang.nextStep();
        incrementNrComparisons();
      }
      // System.err.println("+=lo: " +lo +", loM:" +loMarker.getPosition());

      // while A[Hi] > Mid do Dec(Hi);
      code.toggleHighlight("incLo", "decHi");
      // array.unhighlightCell(lo, null, null);
      array.highlightElem(hi, null, null);
      while (array.getData(hi) > mid) {
        incrementNrComparisons();
        lang.nextStep();

        // Dec(Hi)
        hi--;
        updateMarker(hiMarker, hi, DEFAULT_TIMING);
        incrementNrAssignments();
        array.unhighlightElem(hi + 1, null, null);
        array.highlightElem(hi, null, null);
      }
      incrementNrComparisons();

      // if Lo <= Hi then
      lang.nextStep();
      code.toggleHighlight("decHi", "testLoHi");
      incrementNrComparisons();
      lang.nextStep();

      code.unhighlight("testLoHi");
      if (lo <= hi) {
        code.highlight("copyLo");
        // T := A[Lo];
        t = array.getData(lo);
        incrementNrAssignments();
        tValue.setText(String.valueOf(t), null, null);
        lang.nextStep();

        // A[Lo] := A[Hi];
        code.toggleHighlight("copyLo", "replicateHi");
        array.put(lo, array.getData(hi), null, DEFAULT_TIMING);
        incrementNrAssignments();
        lang.nextStep();

        // A[Hi] := T;
        code.toggleHighlight("replicateHi", "insertLo");
        array.put(hi, t, null, DEFAULT_TIMING);
        incrementNrAssignments();
        lang.nextStep();

        // Inc(Lo);
        code.toggleHighlight("insertLo", "incLo2");
        lo++;
        updateMarker(loMarker, lo, DEFAULT_TIMING);
        incrementNrAssignments();
        lang.nextStep();

        // Dec(Hi);
        code.toggleHighlight("incLo2", "decHi2");
        hi--;
        updateMarker(hiMarker, hi, DEFAULT_TIMING);
        incrementNrAssignments();
        lang.nextStep();

        code.unhighlight("decHi2");
      }
      code.highlight("until");
      lang.nextStep();
      code.unhighlight("until");
    } while (lo <= hi);

    // if Hi > iLo then
    code.highlight("checkHiiLo");
    incrementNrComparisons();
    lang.nextStep();

    if (hi > iLo) {
      code.toggleHighlight("checkHiiLo", "rightRec");
      lang.nextStep();

      code.unhighlight("rightRec");
      // QuickSort(A, iLo, Hi);
      loMarker.hide();
      hiMarker.hide();
      midValue.setText("", null, null);
      quickSort(iLo, hi, depth + 1);

      // reset values after recursion
      code.highlight("rightRec");
      loMarker.show();
      hiMarker.show();
      // updateMarker(loMarker, lo, null);
      // updateMarker(hiMarker, hi, null);
      midValue.setText(String.valueOf(mid), null, null);
      // updateMarker(midMarker, mid, null);
      tValue.setText(String.valueOf(t), null, null);
      array.highlightCell(iLo, iHi, null, null);

      lang.nextStep();
      code.unhighlight("rightRec");
    } else
      code.unhighlight("checkHiiLo");

    // if Lo < iHi then
    code.highlight("checkLoiHi");
    incrementNrComparisons();
    lang.nextStep();

    if (lo < iHi) {
      code.toggleHighlight("checkLoiHi", "leftRec");
      lang.nextStep();

      code.unhighlight("leftRec");
      // QuickSort(A, Lo, iHi);
      loMarker.hide();
      hiMarker.hide();
      midValue.setText("", null, null);
      quickSort(lo, iHi, depth + 1);
      // reset values after recursion
      code.highlight("leftRec");
      loMarker.show();
      hiMarker.show();
      // updateMarker(loMarker, lo, null);
      // updateMarker(hiMarker, hi, null);
      midValue.setText(String.valueOf(mid), null, null);
      // updateMarker(midMarker, mid, null);
      tValue.setText(String.valueOf(t), null, null);
      array.highlightCell(iLo, iHi, null, null);

      lang.nextStep();
      code.unhighlight("leftRec");
    } else
      code.unhighlight("checkLoiHi");

    code.highlight("checkDone");
    // done =
    terminated(array);
    lang.nextStep();
    code.unhighlight("checkDone");
    array.unhighlightCell(iLo, iHi, null, null);
    loMarker.hide();
    hiMarker.hide();
  }

  private boolean terminated(IntArray theArray) {
    for (int i = 0; i < array.getLength() - 1; i++)
      if (array.getData(i) > array.getData(i + 1))
        return false;
    return true;
  }

  private void updateMarker(ArrayMarker marker, int pos, Timing duration) {
    if (marker != null && pos >= 0 && pos < array.getLength())
      marker.move(pos, null, duration);
    else if (pos < 0)
      marker.moveBeforeStart(null, duration);
    else
      marker.moveOutside(null, duration);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    setUpDefaultElements(props, prims, "array", "code", "code", 0, 20);
    // new Offset(0, 20, array, AnimalScript.DIRECTION_SW));
    sort();
    hideText(iLoLabel);
    hideText(iLoValue);
    hideText(iHiLabel);
    hideText(iHiValue);
    hideText(tLabel);
    hideText(tValue);
    hideText(midLabel);
    hideText(midValue);
    wrapUpAnimation();

    lang.finalizeGeneration();
    // System.err.println(lang.toString());
    return lang.toString();
  }

  private void hideText(Text textObject) {
    if (textObject != null)
      textObject.hide();
  }

  public String getAlgorithmName() {
    return "Quicksort";
  }

  public String getAnimationAuthor() {
    return "Krasimir Markov";
  }

  /**
   * getContentLocale returns the target Locale of the generated output Use e.g.
   * Locale.US for English content, Locale.GERMANY for German, etc.
   * 
   * @return a Locale instance that describes the content type of the output
   */
  public Locale getContentLocale() {
    return contentLocale;
  }

}
