package generators.sorting.selectionsort;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Timing;

/**
 * @author Prof. Dr. O. William McClung <mcclung@nebrwesleyan.edu>
 * @version 1.0 2011-01-31
 *
 */
public class SelectionSortDemo implements Generator {
  private Language lang; // The concrete language object used to creating output
  private ArrayProperties arrayProps;
  private ArrayMarkerProperties ami, amj, amMin;
  private SourceCode sc;

  public void init() { // initialize central elts.
    // Generate new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript("Selection Sort Animation",
                            "Prof. Dr. O. William McClung",640,480);
    // Activate step control
    lang.setStepMode(true);
    // create array properties with default values
    arrayProps = new ArrayProperties();
    // Redefine properties: border red, filled with gray
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,Color.RED);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.WHITE);
    // marker for i: black with label "i"
    ami = new ArrayMarkerProperties();
    ami.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.BLACK);
    ami.set(AnimationPropertiesKeys.LABEL_PROPERTY,"i");
    // marker for j: blue with label "j"
    amj = new ArrayMarkerProperties();
    amj.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.BLUE);
    amj.set(AnimationPropertiesKeys.LABEL_PROPERTY,"j");
    // marker for min: green with label "min"
    amMin = new ArrayMarkerProperties();
    amMin.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.GREEN);
    amMin.set(AnimationPropertiesKeys.LABEL_PROPERTY,"min");
  } // init

  // Generators need a default, parameter-less constructor
  public SelectionSortDemo() {
  }

  public static void main(String[] args) {
    SelectionSortDemo ssd = new SelectionSortDemo();
    ssd.init();
    int[] original = new int[]{1,13,7,2,11};
    ssd.selectionSort(original);
    System.out.println(ssd.lang.toString());
  }

  public void selectionSort(int[] inputArray) {
    // wrap int[] into IntArray
    IntArray array = lang.newIntArray(new Coordinates(10,100),inputArray,"array",
                                      null,arrayProps);
    // duration of swap effect
    Timing defaultTiming = new MsTiming(15);
    showSourceCode(); // show source code
    sc.highlight(0);  // method header
    lang.nextStep();  // show array without markers
    sc.toggleHighlight(0,1); // jump from header to int decls
    ArrayMarker i = lang.newArrayMarker(array,0,"i",null,ami);
    lang.nextStep(); // show i
    ArrayMarker j = lang.newArrayMarker(array,0,"j",null,amj);
    lang.nextStep(); // show j
    ArrayMarker min = lang.newArrayMarker(array,0,"min",null,amMin);
    sc.unhighlight(1); // unhighlight declarations
    for (; i.getPosition() < array.getLength() - 1;
         i.increment(null,defaultTiming)) {
      sc.highlight(2); // for loop
      lang.nextStep(); // for increment
      sc.toggleHighlight(2,3); // for -> min = i
      min.move(i.getPosition(),null,defaultTiming);
      lang.nextStep(); // move min
      sc.unhighlight(3); // min = i is done
      for (j.move(i.getPosition() + 1,null,defaultTiming);
           j.getPosition() < array.getLength();
           j.increment(null,defaultTiming)) {
        sc.highlight(4); // for j...
        lang.nextStep(); // wait for update of j
        sc.toggleHighlight(4,5); // for loop entered, change to if
        if (array.getData(j.getPosition()) < 
            array.getData(min.getPosition())) {
          lang.nextStep(); // wait for update of min
          sc.toggleHighlight(5,6); // if entered
          min.move(j.getPosition(),null,defaultTiming);
          lang.nextStep(); // wait for update of min
          sc.unhighlight(6); // remove highlight on min =
        } else { // added to ensure if no longer highlighted
          lang.nextStep();
          sc.unhighlight(5); // unhighlight if
        }
      } // for j
      sc.highlight(7); // highlight swap
      array.swap(i.getPosition(),min.getPosition(),null,defaultTiming);
      lang.nextStep(); // see swap
      sc.unhighlight(7); // unhighlight swap
    } // for i
  } // selectionSort

  public void showSourceCode() {
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced",Font.PLAIN,12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.BLACK);
    // create source code object
    sc = lang.newSourceCode(new Coordinates(40,140),"sourceCode",
                                       null,scProps);
    sc.addCodeLine("public void selectionSort(int[] data) {",null,0,null);
    sc.addCodeLine("int i,j,min;",null,1,null);
    sc.addCodeLine("for (i = 0; i < data.length - 1; i++) {",null,1,null);
    sc.addCodeLine("min = i;",null,2,null);
    sc.addCodeLine("for (j = i + 1; j < data.length; j++)",null,2,null);
    sc.addCodeLine("if (data[j] < data[min])",null,3,null);
    sc.addCodeLine("min = j;",null,4,null);
    sc.addCodeLine("swap(data,i,min); // swap",null,2,null);
    sc.addCodeLine("}",null,1,null);
    sc.addCodeLine("}",null,0,null);
  } // showSourceCode

  /*
   * The following are the methods necessary to create a Generator
   */

  public String getAlgorithmName() {
    return "Selection Sort";
  }

  public String getCodeExample() {
    return "Straightforward Selection Sort Algorithm";
  }

  public Locale getContentLocale() {
    return Locale.US; // US English
  }

  public String getDescription() {
    return "Animates Selection Sort with Source Code and Highlighting";
  }

  public String getFileExtension() {
    return ".asu";
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT); // sorter
  }

  public String getName() {
    return "SelectionSortDemo"; // title to be displayed
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public String getAnimationAuthor() {
    return "Prof. Dr. O. William McClung";
  }

  public String generate(AnimationPropertiesContainer props,
                         Hashtable<String,Object> primitives) {
    init(); // ensure all properties are setup
    int[] arrayData = (int[]) primitives.get("array");
    // adapt the color to whatever the user chose
    // could do this for all properties, if you'd like
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
                   props.get("array",AnimationPropertiesKeys.COLOR_PROPERTY));
    // call selection sort method
    selectionSort(arrayData);
    return lang.toString();
  }

} // SelectionSortDemo
