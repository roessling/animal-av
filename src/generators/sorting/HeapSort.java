package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.Graph;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

/**
 * @author Daniel Thies, Dominik Ulrich, Jörg Schmalfuß & Atilla Yalzin
 * @version 1.0 2007-05-30
 * 
 */
public class HeapSort implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;
  private Graph    g;

  /**
   * Default constructor
   */
  public HeapSort() {
  }

  /**
   * Builds a new HeapSort using the given language
   * 
   * @param l
   *          the concrete language object used for creating output
   */
  public HeapSort(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  private static final String DESCRIPTION = "Heapsort ist ein Sortierverfahren das einen binären Baum als Datenstruktur"
                                              + "nutzt. Der eigentliche Sortieralgorithmus nutzt die Tatsache aus, dass die"
                                              + "Wurzel eines Heaps stets der größte Knoten ist. Da im fertig sortierten Array"
                                              + "der größte Wert ganz rechts stehen soll, vertauscht man das erste mit dem letzten"
                                              + "Arrayelement. Das Element am Ende des Arrays ist nun an der gewünschten Position"
                                              + "und bleibt dort. Den Rest des Arrays muss man wieder in einen neuen Heap überführen,"
                                              + "Anschließend vertauscht man das erste mit dem vorletzten Element, d. h. die beiden"
                                              + "größten Werte sind wie gewünscht am Ende des Arrays, usw.";

  private static final String SOURCE_CODE = " public void heapSort(int[] heap) {"
                                              + "\n  generateMaxHeap(heap);"
                                              + "\n  for(int i = heap.length -1 ; i > 0 ; i--) {"
                                              + "\n    swap(i, 0);"
                                              + "\n    versenke(heap, 0, i);"
                                              + "\n  }"
                                              + "\n }"
                                              + "\n"
                                              + "\n private void generateMaxHeap(int[] heap) {"
                                              + "\n  for (int i = heap.length - 1; i >= 0; i--) {"
                                              + "\n   versenke(heap, 0, i);"
                                              + "\n  }"
                                              + "\n }"
                                              + "\n"
                                              + "\n private void versenke(int[] heap, int iNode, int heapSize) {"
                                              + "\n  int i,j;"
                                              + "\n  j := iNode;"
                                              + "\n  do {"
                                              + "\n   i := j;"
                                              + "\n   if((2*i+1) < heapSize) && (heap[2*i+1]>heap[j]))"
                                              + "\n    j := 2*i+1;"
                                              + "\n   if((2*i+2) < heapSize) && (heap[2*i+2]>heap[j]))"
                                              + "\n    j := 2*i+2;"
                                              + "\n   swap(i,j);"
                                              + "\n  }while (i!=j);" + "\n }";

  /**
   * Sort the int array passed in
   * 
   * @param a
   *          the array to be sorted
   */
  public void sort(int[] a, AnimationPropertiesContainer aprops) {
    // Create Array: coordinates, data, name, display options,
    // default properties

    // title...
    TextProperties txprops = new TextProperties();
    txprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    txprops.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        aprops.get("title", AnimationPropertiesKeys.COLOR_PROPERTY));
    lang.newText(new Coordinates(100, 500), "HeapSort", "title", null, txprops);

    // first, set the visual properties (somewhat similar to CSS)

    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.FILL_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.FILLED_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));

    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));

    // now, create the IntArray object, linked to the properties
    IntArray ia = lang.newIntArray(new Coordinates(440, 500), a, "intArray",
        null, arrayProps);

    // start a new step after the array was created
    lang.nextStep();

    // Create SourceCode: coordinates, name, display options,
    // default properties

    // first, set the visual properties for the source code

    /*
     * Coordinates[] cline={new Coordinates(0,0),new Coordinates(100,100)};
     * Polyline line=lang.newPolyline(cline, "line1", null); CircleProperties
     * cprop=new CircleProperties();
     * cprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     * cprop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
     * cprop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
     * Circle c=lang.newCircle(new Coordinates(100,100), 20, "Hallo", null,
     * cprop); c.moveBy("translate", 100, 100, new MsTiming(10), new
     * MsTiming(500)); // c.moveVia(AnimalScript.DIRECTION_BASELINE_START,
     * "translate", line, new MsTiming(0), new MsTiming(500)); TextProperties
     * tprop=new TextProperties(); Text t=lang.newText(new
     * Coordinates(100,100),"Text","title",null,tprop); t.moveBy("translate",
     * 10, 10, new MsTiming(0), new MsTiming(10)); t.hide();
     */

    g = new Graph(a, lang);

    SourceCodeProperties scProps = new SourceCodeProperties();

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, aprops.get(
        "sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        aprops.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        aprops.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
    scProps.set(AnimationPropertiesKeys.SIZE_PROPERTY,
        aprops.get("sourceCode", AnimationPropertiesKeys.SIZE_PROPERTY));

    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(50, 20), "sourceCode",
        null, scProps);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    sc.addCodeLine("public void heapSort(int[] heap)", null, 0, null); // 0
    sc.addCodeLine("{", null, 0, null);
    sc.addCodeLine("generateMaxHeap(heap);", null, 1, null);
    sc.addCodeLine("for(int i = heap.length -1 ; i > 0 ; i--) {", null, 1, null); // 3
    sc.addCodeLine("{", null, 1, null); // 4
    sc.addCodeLine("swap(i, 0);", null, 2, null); // 5
    sc.addCodeLine("versenke(heap, 0,i);", null, 2, null); // 6
    sc.addCodeLine("}", null, 1, null); // 7
    sc.addCodeLine("}", null, 0, null); // 8
    sc.addCodeLine("", null, 0, null); // 9
    sc.addCodeLine("private void generateMaxHeap(int[] heap) {", null, 0, null); // 10
    sc.addCodeLine("for(int i = heap.length  - 1; i >= 0 ; i--) {", null, 1,
        null); // 11
    sc.addCodeLine("versenke(heap,0,i);", null, 2, null); // 12
    sc.addCodeLine(" }", null, 1, null); // 13
    sc.addCodeLine("}", null, 0, null); // 14
    sc.addCodeLine("", null, 0, null); // 15
    sc.addCodeLine("private void versenke(int[] Heap, int iNode,HeapSize) {",
        null, 0, null); // 16
    sc.addCodeLine("int i,j;", null, 1, null); // 17
    sc.addCodeLine("j=iNode;  ", null, 1, null); // 18
    sc.addCodeLine("do{", null, 1, null); // 19
    sc.addCodeLine("i=j;", null, 2, null); // 20
    sc.addCodeLine("if((2*i+1)<HeapSize && Heap[2*i+1]>Heap[j])", null, 2, null); // 21
    sc.addCodeLine("j=2*i+1", null, 3, null); // 22
    sc.addCodeLine("if((2*i+2)<HeapSize && Heap[2*i+2]>Heap[j])", null, 2, null); // 23
    sc.addCodeLine("j=2*i+2;", null, 3, null); // 24
    sc.addCodeLine("swap(i,j);", null, 2, null); // 25
    sc.addCodeLine("}while(i!=j);", null, 1, null); // 26
    sc.addCodeLine("}", null, 0, null); // 27

    lang.nextStep();
    /*
     * for(int i=g.n.length-1;i>=0;i--){ g.moveStart(i); lang.nextStep(); }
     */
    // Highlight all cells
    ia.highlightCell(0, ia.getLength() - 1, null, null);
    try {
      // Start quicksort
      heapSort(ia, sc, aprops);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  /**
   * counter for the number of pointers
   * 
   */
  // private int pointerCounter = 0;

  /**
   * Quicksort: Sort elements using a pivot element between [l, r]
   * 
   * @param array
   *          the IntArray to be sorted
   * @param codeSupport
   *          the underlying code instance
   * @param l
   *          the lower border of the subarray to be sorted
   * @param l
   *          the upper border of the subarray to be sorted
   */
  private void heapSort(IntArray heap, SourceCode codeSupport,
      AnimationPropertiesContainer aprops) throws LineNotExistsException {
    // Highlight first line
    // Line, Column, use context colour?, display options, duration
    codeSupport.highlight(0, 0, false);
    lang.nextStep();
    codeSupport.toggleHighlight(0, 0, false, 2, 0);
    lang.nextStep();
    codeSupport.unhighlight(2, 0, false);
    generateMaxHeap(heap, codeSupport);

    /*
     * we need a pointR? ArrayMarkerProperties arrayJMProps = new
     * ArrayMarkerProperties();
     * arrayJMProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, "true");
     * arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "  i");
     * arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, aprops.get("m",
     * AnimationPropertiesKeys.COLOR_PROPERTY)); ArrayMarker m =
     * lang.newArrayMarker(heap, 0, "m", null, arrayJMProps);
     */
    ArrayMarker m = lang.newArrayMarker(heap, heap.getLength() - 1, "m", null);
    for (int i = heap.getLength() - 1; i >= 0; i--) {

      codeSupport.highlight(3, 0, false);
      lang.nextStep();
      codeSupport.unhighlight(3, 0, false);
      swap(heap, 0, i, codeSupport);
      g.markout(i);
      codeSupport.highlight(5, 0, false);
      lang.nextStep();
      codeSupport.unhighlight(5, 0, false);
      versenke(heap, 0, i, codeSupport);
      codeSupport.highlight(6, 0, false);
      lang.nextStep();
      codeSupport.unhighlight(6, 0, false);
      m.decrement(null, null);
    }

  }

  private void generateMaxHeap(IntArray heap, SourceCode codeSupport)
      throws LineNotExistsException {
    codeSupport.highlight(11, 0, false);
    lang.nextStep();
    codeSupport.unhighlight(11, 0, false);
    for (int i = heap.getLength() - 1; i >= 0; i--) {

      codeSupport.highlight(11, 0, false);
      lang.nextStep();
      g.moveStart(i);
      codeSupport.unhighlight(11, 0, false);
      codeSupport.highlight(12, 0, false);
      lang.nextStep();
      codeSupport.unhighlight(12, 0, false);
      if (i < (heap.getLength() / 2))
        versenke(heap, i, heap.getLength(), codeSupport);

    }

  }

  private void versenke(IntArray Heap, int iNode, int HeapSize,
      SourceCode codeSupport) throws LineNotExistsException {
    codeSupport.highlight(16, 0, false);
    lang.nextStep();
    codeSupport.unhighlight(16, 0, false);
    int i, j;
    j = iNode;
    do {
      codeSupport.highlight(19, 0, false);
      lang.nextStep();
      codeSupport.unhighlight(19, 0, false);
      i = j;
      g.hightlight(i);
      Heap.highlightCell(i, null, null);
      if ((2 * i + 1) < HeapSize && Heap.getData(2 * i + 1) > Heap.getData(j)) {
        j = (2 * i) + 1;
        codeSupport.highlight(21, 0, false);
        lang.nextStep();
        codeSupport.unhighlight(21, 0, false);
      }

      if (((2 * i) + 2) < HeapSize
          && Heap.getData((2 * i) + 2) > Heap.getData(j)) {
        j = (2 * i) + 2;
        codeSupport.highlight(23, 0, false);
        lang.nextStep();
        codeSupport.unhighlight(23, 0, false);
      }
      g.hightlight(j);
      Heap.highlightCell(j, null, null);
      swap(Heap, i, j, codeSupport);
      if (i != j) {
        codeSupport.highlight(25, 0, false);
        lang.nextStep();
        codeSupport.unhighlight(25, 0, false);
        lang.nextStep();
      }
      g.unhightlight(i);
      g.unhightlight(j);
      Heap.unhighlightCell(i, null, null);
      Heap.unhighlightCell(j, null, null);
    } while (i != j);

  }

  private void swap(IntArray heap, int i, int j, SourceCode codeSupport)
      throws LineNotExistsException {
    heap.swap(i, j, null, new TicksTiming(15));
    g.swap(i, j);
  }

  public String getAlgorithmName() {
    return "Heap Sort";
  }

  public String getCodeExample() {

    return SOURCE_CODE;
  }

  public Locale getContentLocale() {

    return Locale.GERMANY;
  }

  public String getDescription() {

    return DESCRIPTION;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {

    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {

    return "HeapSort";
  }

  public String getOutputLanguage() {

    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    // extract array parameter
    int[] myArray = (int[]) arg1.get("array");

    // create language instance
    lang = new AnimalScript("HeapSort Animation", "doowap", 800, 600);
    HeapSort s = new HeapSort(lang);
    // active step mode
    lang.setStepMode(true);

    // sort the array
    s.sort(myArray, arg0);

    // return the output
    return lang.toString();
  }

  @Override
  public String getAnimationAuthor() {
    return "Daniel Thies, Dominik Ulrich, Jörg Schmalfuß, Atilla Yalzin";
  }

  @Override
  public void init() {
    // nothing to be done here...
  }
}
