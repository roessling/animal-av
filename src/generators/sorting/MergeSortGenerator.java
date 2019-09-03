package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class MergeSortGenerator implements Generator {
  private Language             lang;
  private TextProperties       title;
  private SourceCodeProperties sourceCode;
  private RectProperties       titleBox;
  private int[]                intArray;
  private TextProperties       caption;
  private ArrayProperties      array;
  private Text                 status;
  private Variables            vars;
  private Text                 statisticsArrayAccess;
  private Text                 statisticsNumberOfArrays;

  private int                  maxDepth       = 0;
  private int                  arrayAccess    = 0;
  private int                  numberOfArrays = 0;

  // private Coordinates initialArrayCoords = new Coordinates(600, 45);

  // -------------Algorithmus

  public void sort(int[] a) {

    createHeader();
    createHeaderBox();
    SourceCode descr = createDescription();

    lang.nextStep();
    Text descriptionCaption = createCaptionTextAt("Funktionsweise",
        new Coordinates(20, 180));
    createDescriptionSteps();
    descriptionCaption.hide();
    descr.hide();

    Text descriptionCaptionMerge = createCaptionTextAt("Merge",
        new Coordinates(20, 180));
    createDescriptionMerge();
    descriptionCaptionMerge.hide();

    createCaptionTextAt("Pseudocode", new Coordinates(20, 80));
    SourceCode pseudoCodeMergeSort = createMergeSortPseudoCode();
    SourceCode pseudoCodeMerge = createMergePseudoCode();

    lang.nextStep();
    arrayAccess = 0;
    if (a == null || a.length == 0) {
      numberOfArrays = 0;
    } else {
      numberOfArrays = 1;
    }
    statisticsArrayAccess = createStatusTextAt("Arrayzugriffe: " + arrayAccess,
        new Coordinates(170, 20));
    statisticsNumberOfArrays = createStatusTextAt("Anzahl an Arrays: "
        + numberOfArrays, new Coordinates(170, 40));
    status = createStatusTextAt("Start!", new Coordinates(170, 80));
    IntArray ia = initArrayAt(a, initialArrayCoords(a));
    maxDepth = maxDepth(a);

    lang.nextStep();
    vars = lang.newVariables();
    vars.openContext();
    mergeSort(ia, pseudoCodeMergeSort, pseudoCodeMerge, initialArrayCoords(a));

    lang.nextStep();
    vars.closeContext();
    lang.hideAllPrimitives();
    createHeader();
    createHeaderBox();
    createCaptionTextAt("Laufzeit", new Coordinates(20, 180));
    createTimeComplexityDescription();
    createStatusTextAt("Arrayzugriffe: " + arrayAccess,
        new Coordinates(20, 280));
    createStatusTextAt("Anzahl an Arrays: " + numberOfArrays, new Coordinates(
        20, 300));
    createStatusTextAt("Ursprungliche Arraylaenge: " + a.length,
        new Coordinates(20, 240));
  }

  private SourceCodeProperties getStandardSourceCodeProperties() {
    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 12));
    //
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // return scProps;
    return sourceCode;
  }

  private ArrayMarkerProperties getStandardArrayMarkerProperties(String label) {
    ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
    arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, label);
    arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    return arrayJMProps;
  }

  private SourceCode createMergePseudoCode() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();
    SourceCode descr = lang.newSourceCode(new Coordinates(20, 270),
        "description", null, scProps);
    descr.addCodeLine("Pseudocode Merge", null, 0, null);
    descr.addCodeLine("", null, 0, null);
    descr.addCodeLine("Funktion merge(linkes_Array, rechtes_Array)", null, 0,
        null);
    descr.addCodeLine("neues Array erzeugen", null, 1, null);
    descr.addCodeLine("solange (linkes_Array und rechtes_Array nicht leer)",
        null, 1, null);
    descr
        .addCodeLine(
            "vergleiche die ersten Elemente beider Arrays und fuege das kleinere hinten in das Hilfsarray ein",
            null, 2, null);
    descr.addCodeLine("und entferne es aus dem urspruenglichen Array", null, 2,
        null);
    descr.addCodeLine("solange (linkes_Array nicht leer)", null, 1, null);
    descr.addCodeLine(
        "fuege erstes Element linkes_Array in neues Array hinten ein", null, 2,
        null);
    descr.addCodeLine("und entferne es aus linkes_Array", null, 2, null);
    descr.addCodeLine("solange (rechtes_Array nicht leer)", null, 1, null);
    descr.addCodeLine(
        "fuege erstes Element rechtes_Array in neues Array hinten ein", null,
        2, null);
    descr.addCodeLine("und entferne es aus rechtes_Array", null, 2, null);
    descr.addCodeLine("retourniere neues Array", null, 0, null);
    return descr;
  }

  private SourceCode createMergeSortPseudoCode() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();

    // now, create the source code entity
    SourceCode descr = lang.newSourceCode(new Coordinates(20, 100),
        "description", null, scProps);
    descr.addCodeLine("Pseudocode Mergesort", null, 0, null);
    descr.addCodeLine("", null, 0, null);
    descr.addCodeLine("Funktion mergesort(array)", null, 0, null);
    descr.addCodeLine("falls Laenge des Arrays <= 1 retourniere das Array",
        null, 1, null);
    descr.addCodeLine("ansonsten:", null, 1, null);
    descr.addCodeLine("halbiere das Array in linkes_Array und rechtes_Array",
        null, 2, null);
    descr.addCodeLine("sortiere linkes_Array rekursiv mit mergesort", null, 2,
        null);
    descr.addCodeLine("sortiere rechtes_Array rekursiv mit mergesort", null, 2,
        null);
    descr.addCodeLine("retourniere merge(linkes_Array, rechtes_Array)", null,
        2, null);
    return descr;
  }

  private void createHeader() {
    // Font newFont = new Font("SansSerif", Font.BOLD, 20);
    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, newFont);
    // textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    // lang.newText(new Coordinates(20, 31), "Mergesort", "header", null,
    // textProps);
    lang.newText(new Coordinates(20, 31), "Mergesort", "header", null, title);
  }

  private void createHeaderBox() {
    // RectProperties rectProps = new RectProperties();
    // rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);
    // Rect rect = lang.newRect(new Coordinates(15, 26), new Coordinates(158,
    // 62), "headerRect", null, rectProps);
    // Rect rect =
    lang.newRect(new Coordinates(15, 26), new Coordinates(158, 62),
        "headerRect", null, titleBox);
  }

  private SourceCode createDescription() {
    // first, set the visual properties for the source code
    SourceCodeProperties scProps = getStandardSourceCodeProperties();

    // now, create the source code entity
    SourceCode descr = lang.newSourceCode(new Coordinates(20, 100),
        "description", null, scProps);
    descr
        .addCodeLine(
            "Mergesort ist ein stablier Sortieralgorithmus welcher nach dem Teile-und-Herrsche-Konzept arbeitet.",
            null, 0, null);
    descr
        .addCodeLine(
            "Bei Teile-und-Herrsche wird ein Problem solange in Teilprobleme zerlegt,",
            null, 0, null);
    descr.addCodeLine("bis ein solches trivial loesbar ist.", null, 0, null);
    return descr;
  }

  private Text createCaptionTextAt(String text, Coordinates coords) {
    // Font newFont = new Font("SansSerif", Font.BOLD, 20);
    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, newFont);
    // textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    // return lang.newText(coords, text, text, null, textProps);
    return lang.newText(coords, text, text, null, caption);
  }

  private Text createStatusTextAt(String text, Coordinates coords) {
    // Font newFont = new Font("SansSerif", Font.BOLD, 16);
    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, newFont);
    // textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    // return lang.newText(coords, text, text, null, textProps);
    return lang.newText(coords, text, text, null, caption);
  }

  private void createDescriptionSteps() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();

    SourceCode descr = lang.newSourceCode(new Coordinates(20, 200), "steps",
        null, scProps);
    descr
        .addCodeLine(
            "Ein unsortierter Datensatz, z.B. ein Array mit ganzen Zahlen, soll aufsteigend sortiert werden.",
            null, 0, null);
    descr.addCodeLine("", null, 0, null);

    lang.nextStep();

    descr.addCodeLine("1. Das Array wird in zwei Haelften geteilt.", null, 0,
        null);

    lang.nextStep();

    descr
        .addCodeLine(
            "2. Auf jede Haelfte wird der Mergesort-Algorithmus rekursiv angewendet.",
            null, 0, null);

    lang.nextStep();

    descr
        .addCodeLine(
            "D.h. dass die Haelften wiederum geteilt werden. Und zwar solange, bis ein geteiltes Array",
            null, 0, null);
    descr.addCodeLine("nur noch eine Zahl enthaelt.", null, 0, null);

    lang.nextStep();

    descr
        .addCodeLine(
            "3. Nun werden die zuvor geteilten Haelften wieder so zusammengefuegt ('Merge'), dass eine Sortierung",
            null, 0, null);
    descr.addCodeLine(
        "der Werte erzielt wird (mehr dazu im Verlauf der Animation)", null, 0,
        null);

    lang.nextStep();
    descr.hide();
  }

  private void createDescriptionMerge() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();

    SourceCode descr = lang.newSourceCode(new Coordinates(20, 200), "steps",
        null, scProps);

    descr
        .addCodeLine(
            "Beim Zusammenfuegen von zwei Arrays gilt die Annahme, dass diese bereits (aufsteigend) sortiert sind.",
            null, 0, null);
    descr.addCodeLine("", null, 0, null);

    lang.nextStep();

    descr.addCodeLine("1. Vergleiche die 'vordersten' Werte der Arrays", null,
        0, null);

    lang.nextStep();

    descr
        .addCodeLine(
            "2. Kopiere den kleineren Wert in ein Hilfs-Array, loesche den Wert im urspruenglichen Array",
            null, 0, null);

    lang.nextStep();

    descr
        .addCodeLine(
            "3. Wiederhole Schritte 1. und 2. bis alle Elemente der Arrays in das Hilfs-Array einsortiert wurden",
            null, 0, null);

    lang.nextStep();

    descr.addCodeLine("", null, 0, null);
    descr
        .addCodeLine(
            "Anschaulich: Man stelle sich die Arrays als zwei Kartenstapel mit Zahlen auf jeder Karte vor.",
            null, 0, null);

    lang.nextStep();

    descr
        .addCodeLine(
            "Man vergleicht die beiden obersten Karten der Stapel, legt die Karte mit der kleinere Zahl",
            null, 0, null);

    lang.nextStep();

    descr
        .addCodeLine(
            "(mit dem Gesicht nach unten) auf einen dritten Stapel ab. Dies wird solange wiederholt bis beide Kartenstapel",
            null, 0, null);

    lang.nextStep();

    descr.addCodeLine("vollstaendig auf den dritten Stapel abgelegt wurden.",
        null, 0, null);

    lang.nextStep();
    descr.hide();
  }

  private void createTimeComplexityDescription() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();

    SourceCode descr = lang.newSourceCode(new Coordinates(20, 200),
        "time complexity", null, scProps);
    descr
        .addCodeLine(
            "Mergesort hat fuer Best-, Average- und Worst-Case eine Zeitkomplexitaet von O(n*log(n))",
            null, 0, null);
  }

  private IntArray initArrayAt(int[] a, Coordinates coords) {
    // Create Array: coordinates, data, name, display options,
    // default properties

    // first, set the visual properties (somewhat similar to CSS)
    ArrayProperties arrayProps = getStandardIntArrayProps();
    // now, create the IntArray object, linked to the properties
    IntArray ia = lang.newIntArray(coords, a, "intArray", null, arrayProps);
    return ia;
  }

  private ArrayProperties getStandardIntArrayProps() {
    // ArrayProperties arrayProps = new ArrayProperties();
    // arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    // arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
    // Color.BLACK);
    // arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
    // Color.RED);
    // arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
    // Color.YELLOW);
    // return arrayProps;
    return array;
  }

  private String arrayDataToString(IntArray sa) {
    int nrElems = sa.getLength();
    int[] helper = new int[nrElems];
    for (int pos = 0; pos < nrElems; pos++)
      helper[pos] = sa.getData(pos);
    return Arrays.toString(helper);
  }

  private IntArray mergeSort(IntArray array, SourceCode mergeSort,
      SourceCode merge, Coordinates coords) {
    status.setText(" ", null, null);
    vars.discard("arrayLeft");
    vars.discard("arrayRight");
    int nrElems = array.getLength();
    int[] helper = new int[nrElems];
    for (int pos = 0; pos < nrElems; pos++)
      helper[pos] = array.getData(pos);
    vars.declare("String", "arrayMergeSort", arrayDataToString(array));
    // vars.declare("String", "arrayMergeSort",
    // Arrays.toString(array.getData()));
    array.highlightCell(0, array.getLength() - 1, null, null);
    mergeSort.highlight(2);
    mergeSort.unhighlight(8);
    merge.unhighlight(13);
    lang.nextStep();
    mergeSort.highlight(3);
    mergeSort.unhighlight(2);
    lang.nextStep();
    arrayAccess++;
    statisticsArrayAccess.setText("Arrayzugriffe: " + arrayAccess, null, null);
    if (array.getLength() <= 1) {
      status.setText("Laenge <= 1", null, null);
      array.unhighlightCell(0, null, null);
      mergeSort.unhighlight(3);
      return array;
    } else {
      // halbiere(array);
      status.setText("Laenge > 1", null, null);
      mergeSort.highlight(4);
      mergeSort.unhighlight(3);
      lang.nextStep();
      mergeSort.highlight(5);
      mergeSort.unhighlight(4);
      int myNrElems = array.getLength(), pos = 0;
      int[] arrayLeft = new int[myNrElems >> 1];
      for (pos = 0; pos < arrayLeft.length; pos++)
        arrayLeft[pos] = array.getData(pos);
      // int[] arrayLeft = getLeftHalf(array.getData());

      int[] arrayRight = new int[myNrElems >> 1 + (myNrElems % 2)];
      for (pos = (myNrElems >> 1) + 1; pos < array.getLength(); pos++)
        arrayRight[pos] = array.getData(pos);
      // int[] arrayRight = getRightHalf(array.getData());
      arrayAccess = arrayAccess + 2;
      statisticsArrayAccess
          .setText("Arrayzugriffe: " + arrayAccess, null, null);
      numberOfArrays = numberOfArrays + 2;
      statisticsNumberOfArrays.setText("Anzahl an Arrays: " + numberOfArrays,
          null, null);
      // Coordinates coordsLeft = new Coordinates(coords.getX() - 40,
      // coords.getY() + 35);
      // Coordinates coordsRight = new Coordinates(coords.getX()
      // + array.getData().length * 20, coords.getY() + 35);
      Coordinates coordsLeft = new Coordinates(coords.getX() - 20
          * arrayLeft.length, coords.getY() + 35);
      Coordinates coordsRight = new Coordinates(coords.getX() + 30
          * arrayRight.length, coords.getY() + 35);
      IntArray left = initArrayAt(arrayLeft, coordsLeft);
      lang.nextStep();
      IntArray right = initArrayAt(arrayRight, coordsRight);
      lang.nextStep();
      mergeSort.highlight(6);
      mergeSort.unhighlight(5);
      status
          .setText("rekursiver Aufruf von mergeSort(linkesArray)", null, null);
      lang.nextStep();
      // sortiere rekursiv
      mergeSort.unhighlight(6);
      array.unhighlightElem(0, array.getLength() - 1, null, null);
      unhighlightCells(array);
      left = mergeSort(left, mergeSort, merge, coordsLeft);
      lang.nextStep();
      mergeSort.highlight(7);
      status.setText("rekursiver Aufruf von mergeSort(rechtesArray)", null,
          null);
      lang.nextStep();
      mergeSort.unhighlight(7);
      right = mergeSort(right, mergeSort, merge, coordsRight);
      // antworte merge(linkesArray, rechtesArray);
      lang.nextStep();
      mergeSort.highlight(8);
      mergeSort.unhighlight(7);
      status.setText("Aufruf Merge", null, null);
      lang.nextStep();
      return merge(left, right, merge, coordsLeft, coordsRight);
    }
  }

  private IntArray merge(IntArray left, IntArray right, SourceCode merge,
      Coordinates coordsLeft, Coordinates coordsRight) {
    status.setText(" ", null, null);
    vars.declare("String", "arrayLeft", arrayDataToString(left)); // Arrays.toString(left.getData()));
    vars.declare("String", "arrayRight", arrayDataToString(right)); // Arrays.toString(right.getData()));
    // Coordinates coords = new Coordinates(
    // (coordsRight.getX() - coordsLeft.getX()) / 2
    // + coordsLeft.getX(), (150 - coordsLeft.getY() + 45) * 2);
    Coordinates coords = new Coordinates(
        (coordsRight.getX() - coordsLeft.getX()) / 2 + coordsLeft.getX(),
        maxDepth * 35 + (maxDepth * 35 - coordsLeft.getY()) + 55);
    ArrayList<Integer> dataLeft = asArrayList(left);
    ArrayList<Integer> dataRight = asArrayList(right);
    int[] mergedData = new int[left.getLength() + right.getLength()];
    numberOfArrays = numberOfArrays + 1;
    statisticsNumberOfArrays.setText("Anzahl an Arrays: " + numberOfArrays,
        null, null);
    merge.highlight(2);
    merge.unhighlight(13);
    left.highlightCell(0, left.getLength() - 1, null, null);
    right.highlightCell(0, right.getLength() - 1, null, null);
    lang.nextStep();
    merge.unhighlight(2);
    merge.highlight(3);
    unhighlightCells(left);
    unhighlightCells(right);
    IntArray mergedArray = initArrayAt(mergedData, coords);
    mergedArray.highlightCell(0, mergedArray.getLength() - 1, null, null);
    lang.nextStep();
    merge.highlight(4);
    merge.unhighlight(3);
    int index = 0;
    int indexLeft = 0;
    int indexRight = 0;
    ArrayMarker iMarker = createArrayMarkerPointingAt(mergedArray, index);
    while (dataLeft.size() > 0 && dataRight.size() > 0) {
      lang.nextStep();
      merge.highlight(5);
      merge.highlight(6);
      merge.unhighlight(4);
      if (dataLeft.get(0) <= dataRight.get(0)) {
        mergedData[index] = dataLeft.get(0);
        dataLeft.remove(0);
        // repaintArray(left, coordsLeft, toArray(dataLeft));
        // repaintArray(mergedArray, coords, mergedData);
        // left.hide();
        // left = initArrayAt(toArray(dataLeft), coordsLeft);
        left.highlightElem(indexLeft, null, null);
        indexLeft++;
        mergedArray.hide();
        mergedArray = initArrayAt(mergedData, coords);
      } else {
        mergedData[index] = dataRight.get(0);
        dataRight.remove(0);
        // repaintArray(right, coordsRight, toArray(dataRight));
        // repaintArray(mergedArray, coords, mergedData);
        // right.hide();
        // right = initArrayAt(toArray(dataRight), coordsRight);
        right.highlightElem(indexRight, null, null);
        indexRight++;
        mergedArray.hide();
        mergedArray = initArrayAt(mergedData, coords);
      }
      iMarker = createArrayMarkerPointingAt(mergedArray, index);
      index++;
      arrayAccess = arrayAccess + 2;
      statisticsArrayAccess
          .setText("Arrayzugriffe: " + arrayAccess, null, null);
    }
    lang.nextStep();
    merge.highlight(7);
    merge.unhighlight(6);
    merge.unhighlight(5);
    while (dataLeft.size() > 0) {
      lang.nextStep();
      merge.highlight(8);
      merge.highlight(9);
      merge.unhighlight(7);
      mergedData[index] = dataLeft.get(0);
      dataLeft.remove(0);
      // repaintArray(left, coordsLeft, toArray(dataLeft));
      // repaintArray(mergedArray, coords, mergedData);
      // left.hide();
      // left = initArrayAt(toArray(dataLeft), coordsLeft);
      left.highlightElem(indexLeft, null, null);
      indexLeft++;
      mergedArray.hide();
      mergedArray = initArrayAt(mergedData, coords);
      iMarker = createArrayMarkerPointingAt(mergedArray, index);
      index++;
      arrayAccess++;
      statisticsArrayAccess
          .setText("Arrayzugriffe: " + arrayAccess, null, null);
    }
    lang.nextStep();
    merge.highlight(10);
    merge.unhighlight(8);
    merge.unhighlight(9);
    merge.unhighlight(7);
    while (dataRight.size() > 0) {
      lang.nextStep();
      merge.highlight(11);
      merge.highlight(12);
      merge.unhighlight(10);
      mergedData[index] = dataRight.get(0);
      dataRight.remove(0);
      // repaintArray(right, coordsRight, toArray(dataRight));
      // repaintArray(mergedArray, coords, mergedData);
      // right.hide();
      // right = initArrayAt(toArray(dataRight), coordsRight);
      right.highlightElem(indexRight, null, null);
      indexRight++;
      mergedArray.hide();
      mergedArray = initArrayAt(mergedData, coords);
      iMarker = createArrayMarkerPointingAt(mergedArray, index);
      index++;
      arrayAccess++;
      statisticsArrayAccess
          .setText("Arrayzugriffe: " + arrayAccess, null, null);
    }
    status.setText("Rueckgabe an aufrufende Funktion", null, null);
    lang.nextStep();
    merge.highlight(13);
    merge.unhighlight(12);
    merge.unhighlight(11);
    merge.unhighlight(10);
    iMarker.hide();
    return initArrayAt(mergedData, coords);
  }

  /**
   * unhighlightCell(from, to, ...) hat nicht gewuenschten effekt erbracht
   * 
   * @param array
   */
  private void unhighlightCells(IntArray array) {
    for (int i = 0; i < array.getLength(); i++) {
      array.unhighlightCell(i, null, null);
    }
  }

  private ArrayMarker createArrayMarkerPointingAt(IntArray array, int index) {
    ArrayMarkerProperties arrayIMProps = getStandardArrayMarkerProperties("");
    ArrayMarker iMarker = lang.newArrayMarker(array, index, "index: " + index,
        null, arrayIMProps);
    return iMarker;
  }

  // private void repaintArray(IntArray array, Coordinates coords, int[]
  // data){
  // array.hide();
  // array = initArrayAt(data,coords);
  // }
  //
  // private int[] toArray(ArrayList<Integer> list) {
  // int[] result = new int[list.size()];
  // int index = 0;
  // for (Integer i : list) {
  // result[index] = i;
  // index++;
  // }
  // return result;
  // }

  private ArrayList<Integer> asArrayList(IntArray array) {
    ArrayList<Integer> result = new ArrayList<Integer>();
    for (int i = 0; i < array.getLength(); i++) {
      result.add(array.getData(i));
    }
    return result;
  }

  //
  // private int[] getLeftHalf(int[] array) {
  // return Arrays.copyOfRange(array, 0,
  // (int) Math.floor((array.length) / 2));
  // }
  //
  // private int[] getRightHalf(int[] array) {
  // return Arrays.copyOfRange(array, (int) Math.ceil((array.length) / 2),
  // array.length);
  // }

  private int maxDepth(int[] a) {
    if (a == null || a.length == 0) {
      return 0;
    } else {
      return (int) (Math.ceil(Math.log(a.length) / Math.log(2)) + 1);
    }
  }

  private Coordinates initialArrayCoords(int[] a) {
    int x = 520 + 20 * a.length;
    int y = 45;
    return new Coordinates(x, y);
  }

  // -------------Generatorzeugs

  public MergeSortGenerator() {
    init();
  }

  public void init() {
    lang = new AnimalScript("Mergesort Generator", "Theo Kischka", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    title = (TextProperties) props.getPropertiesByName("title");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    titleBox = (RectProperties) props.getPropertiesByName("titleBox");
    intArray = (int[]) primitives.get("intArray");
    caption = (TextProperties) props.getPropertiesByName("caption");
    array = (ArrayProperties) props.getPropertiesByName("array");

    sort(intArray);

    return lang.toString();
  }

  public String getName() {
    return "Mergesort Generator";
  }

  public String getAlgorithmName() {
    return "Merge Sort";
  }

  public String getAnimationAuthor() {
    return "Theo Kischka";
  }

  public String getDescription() {
    return "Mergesort ist ein stabiler Sortieralgorithmus welcher nach dem Teile-Und-Herrsche-Konzept arbeitet."
        + "\n"
        + "Bei Teile-und-Herrsche wird ein Problem solange in kleinere Teilprobleme zerlegt, bis ein solches"
        + "\n" + "trivial loesbar ist.";
  }

  public String getCodeExample() {
    return "MergeSort(array)"
        + "\n"
        + "falls(array.length <= 1)"
        + "\n"
        + "   retourniere array"
        + "\n"
        + "ansonsten"
        + "\n"
        + "   halbiere array in arrayLinks und arrayRechts"
        + "\n"
        + "   arrayLinks = MergeSort(arrayLinks)arrayRechts = MergeSort(arrayRechts)"
        + "\n" + "   retourniere merge(arrayLinks, arrayRechts)";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}