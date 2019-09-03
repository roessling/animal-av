import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.primitives.IntArray;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author Dr. Guido R&ouml;&szlig;ling <roessling@acm.org>
 * @version 1.1 20140203
 */
public class APIExample2 {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  /**
   * Default constructor
   * 
   * @param l
   *          the concrete language object used for creating output
   */
  public APIExample2(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
    init();
  }

  // @TODO DESCRIPTION; SOURCE_CODE schlechter Stil!
  private List<String> description = new LinkedList<String>();
  private List<String> code        = new LinkedList<String>();

  public void init() {
    initializeDescription();
    initializeCode();
  }
  private void initializeDescription() {
    description.clear();
    description
        .add("QuickSort wählt ein Element aus der zu sortierenden Liste aus ");
    description
        .add("(Pivotelement) und zerlegt die Liste in zwei Teillisten, eine untere, ");
    description
        .add("die alle Elemente kleiner und eine obere, die alle Elemente gleich oder ");
    description
        .add("größer dem Pivotelement enthält.Dazu wird zunächst ein Element von unten ");
    description
        .add("gesucht, das größer als (oder gleichgroß wie) das Pivotelement und damit ");
    description
        .add("für die untere Liste zu groß ist. Entsprechend wird von oben ein kleineres ");
    description
        .add("Element als das Pivotelement gesucht. Die beiden Elemente werden dann ");
    description
        .add("vertauscht und landen damit in der jeweils richtigen Liste.Der Vorgang ");
    description
        .add("wird fortgesetzt, bis sich die untere und obere Suche treffen. Damit sind ");
    description
        .add("die oben erwähnten Teillisten in einem einzigen Durchlauf entstanden. ");
    description
        .add("Suche und Vertauschung können in-place durchgeführt werden.");
    description
        .add("Die noch unsortierten Teillisten werden über denselben Algorithmus ");
    description
        .add("in noch kleinere Teillisten zerlegt (z. B. mittels Rekursion) und, sobald ");
    description
        .add("nur noch Listen mit je einem Element vorhanden sind, wieder zusammengesetzt. ");
    description.add("Die Sortierung ist damit abgeschlossen.");
  }

  private void initializeCode() {
    code.clear();
    code.add("public void quickSort(int[] array, int l, int r)"); // 0
    code.add("{"); // 1
    code.add("  int i, j, pivot;"); // 2
    code.add("  if (r>l)"); // 3
    code.add("  {"); // 4
    code.add("    pivot = array[r];"); // 5
    code.add("    for (i = l; j = r - 1; i < j; )"); // 6
    code.add("    {"); // 7
    code.add("      while (array[i] <= pivot && j > i)"); // 8
    code.add("        i++;"); // 9
    code.add("      while (pivot < array[j] && j > i)"); // 10
    code.add("        j--;"); // 11
    code.add("      if (i < j)"); // 12
    code.add("        swap(array, i, j);"); // 13
    code.add("    }"); // 14
    code.add("    if (pivot < array[i])"); // 15
    code.add("      swap(array, i, r);"); // 16
    code.add("    else"); // 17
    code.add("      i=r;"); // 18
    code.add("    quickSort(array, l, i - 1);"); // 19
    code.add("    quickSort(array, i + 1, r);"); // 20
    code.add("  }"); // 21
    code.add("}"); // 22
  }

  /**
   * default duration for swap processes
   */
  public final static Timing defaultDuration = new TicksTiming(30);

  /**
   * Sort the int array passed in
   * 
   * @param a
   *          the array to be sorted
   */
  public void execute(int[] a) {
    // Create Array: coordinates, data, name, display options,
    // default properties

    // first, set the visual properties (somewhat similar to CSS)
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    // now, create the IntArray object, linked to the properties
    IntArray ia = lang.newIntArray(new Coordinates(20, 100), a, "intArray",
        null, arrayProps);

    // start a new step after the array was created
    lang.nextStep();

    // Create SourceCode: coordinates, name, display options,
    // default properties


    InfoBox codeBox = new InfoBox(lang, new Coordinates(40, 140), 14, "INFOBOX");
    codeBox.setHeadline("Source Code");
    codeBox.setText(code);
    codeBox.show();

/*
    // first, set the visual properties for the source code
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity

//    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
//        null, scProps);

//    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    sc.addCodeLine("public void quickSort(int[] array, int l, int r)", null, 0,
        null); // 0
    sc.addCodeLine("{", null, 0, null);
    sc.addCodeLine("int i, j, pivot;", null, 1, null);
    sc.addCodeLine("if (r>l)", null, 1, null); // 3
    sc.addCodeLine("{", null, 1, null); // 4
    sc.addCodeLine("pivot = array[r];", null, 2, null); // 5
    sc.addCodeLine("for (i = l; j = r - 1; i < j; )", null, 2, null); // 6
    sc.addCodeLine("{", null, 2, null); // 7
    sc.addCodeLine("while (array[i] <= pivot && j > i)", null, 3, null); // 8
    sc.addCodeLine("i++;", null, 4, null); // 9
    sc.addCodeLine("while (pivot < array[j] && j > i)", null, 3, null); // 10
    sc.addCodeLine("j--;", null, 4, null); // 11
    sc.addCodeLine("if (i < j)", null, 3, null); // 12
    sc.addCodeLine("swap(array, i, j);", null, 4, null); // 13
    sc.addCodeLine("}", null, 2, null); // 14
    sc.addCodeLine("if (pivot < array[i])", null, 2, null); // 15
    sc.addCodeLine("swap(array, i, r);", null, 3, null); // 16
    sc.addCodeLine("else", null, 2, null); // 17
    sc.addCodeLine("i=r;", null, 3, null); // 18
    sc.addCodeLine(" quickSort(array, l, i - 1);", null, 2, null); // 19
    sc.addCodeLine(" quickSort(array, i + 1, r);", null, 2, null); // 20
    sc.addCodeLine(" }", null, 1, null); // 21
    sc.addCodeLine("}", null, 0, null); // 22
*/
    
    lang.nextStep();
    codeBox.hide();
    Slide slide = new Slide(lang, "code.txt", "DEMO", new NetworkStyle());
    slide.show();
    // Highlight all cells
    ia.highlightCell(0, ia.getLength() - 1, null, null);
//    sc.hide();
    ia.hide();
    lang.nextStep();

    InfoBox box = new InfoBox(lang, new Coordinates(20, 20), 16, "INFOBOX");
    box.setHeadline("HEADER");
    List<String> contents = new LinkedList<String>();
    contents.add("BODY");
    contents.add("Ain't this nice?");
    contents.add("<b>bold</b><strong>strong</strong>");
    box.setText(contents);
    box.show();
  }

  protected String getAlgorithmDescription() {
    StringBuilder sb = new StringBuilder(2048);
    for (String descriptionLine: description)
      sb.append(descriptionLine);
    return sb.toString();
  }

  protected String getAlgorithmCode() {
    StringBuilder sb = new StringBuilder(2048);
    for (String codeLine: code)
      sb.append(codeLine);
    return sb.toString();
  }

  public String getName() {
    return "Quicksort (pivot=last)";
  }

  public String getDescription() {
    return getAlgorithmDescription();
  }

  public String getCodeExample() {
    return getAlgorithmCode();
  }

  public static void main(String[] args) {
    // Create a new animation
    // name, author, screen width, screen height
    Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
        "Quicksort Example", "Guido Rößling", 640, 480);
        //new AnimalScript("Quicksort Animation", "Guido Rößling",
        //640, 480);
    APIExample2 s = new APIExample2(l);
    int[] a = { 7, 3, 2, 4, 1, 13, 52, 13, 5, 1 };
    s.execute(a);
    System.out.println(l);
  }
}
