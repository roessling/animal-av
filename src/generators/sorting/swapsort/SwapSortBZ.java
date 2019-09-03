package generators.sorting.swapsort;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
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
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class SwapSortBZ implements generators.framework.Generator {

  private Language                     lg;
  private AnimationPropertiesContainer container;
  private Hashtable<String, Object>    primitives;
  private SourceCodeProperties         sourceProperties;
  private ArrayProperties              arrayProperties;
  private ArrayMarkerProperties        iMarkerProperties;
  private ArrayMarkerProperties        jMarkerProperties;
  private SourceCodeProperties         titleProperties;
  private TextProperties               textProperties;
  private SourceCode                   title;
  // private Rect rect_source_code;
  // private SourceCode end;

  /*
   * 
   * Global variables
   */
  // private int factor; // Factor of Caesar-Chiffre
  private Timing                       ticks_0   = new TicksTiming(0);
  private Timing                       ticks_40  = new TicksTiming(40);
  private Timing                       ticks_100 = new TicksTiming(100);
  private SourceCode                   source_code_ss;                  // source
                                                                         // code
                                                                         // swapsort
  private SourceCode                   source_code_cse;                 // source
                                                                         // code
                                                                         // countSmallerElements
  private SourceCode                   source_code_s;                   // source
                                                                         // code
                                                                         // swap
  private IntArray                     arrayPrim;
  private ArrayMarker                  iMarker;
  private ArrayMarker                  jMarker;
  private Text                         count_sum;

  /**
   * Default constructor
   */
  public SwapSortBZ() {
  }

  private static final String[] DESCRIPTION = {
      "Die Idee von Swap-Sort ist, von jedem Element eines Arrays A(0..n) ", // 0
      "die Anzahl \"count\" der kleineren Werte (die in A sind) zu zählen ", // 1
      "und das Element dann mit dem Element in A(count+1) zu vertauschen.", // 2
      "Somit ist sichergestellt, dass das ausgetauschte Element bereits", // 3
      "an der richtigen, also endgültigen Stelle steht.", // 4
      "", // 5
      "Nachteil dieses Algorithmus ist, dass jedes Element nur einmal vorkommen darf,", // 6
      "da sonst keine Terminierung erfolgt." };

  private StringBuffer          SOURCE_CODE = new StringBuffer();

  void initSourceCode() {
    SOURCE_CODE.append("public int[] swapsort(int[] array) {\n");
    SOURCE_CODE.append("   int i = 0;\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE.append("   while(i < array.length - 1) {\n");
    SOURCE_CODE.append("       // zähle die kleinere Elemente\n");
    SOURCE_CODE.append("       int count = countSmallerElements(array , i);\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE.append("       if (count > 0) {\n");
    SOURCE_CODE.append("           // vertausche Elemente\n");
    SOURCE_CODE.append("           array = swap(array , i , count);\n");
    SOURCE_CODE.append("       }\n");
    SOURCE_CODE.append("       else {\n");
    SOURCE_CODE.append("           i++;\n");
    SOURCE_CODE.append("       }\n");
    SOURCE_CODE.append("   }\n");
    SOURCE_CODE.append("   return array;\n");
    SOURCE_CODE.append("}\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE
        .append("private int countSmallerElements(int[] array , int index) {\n");
    SOURCE_CODE.append("   int count = 0;\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE.append("   for (int i = index + 1; i < array.length; i++) {\n");
    SOURCE_CODE.append("       if(array[index] > array[i])\n");
    SOURCE_CODE.append("           count++;\n");
    SOURCE_CODE.append("   }\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE.append("   return count;\n");
    SOURCE_CODE.append("}\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE
        .append("private int[] swap(int[] array , int index1 , int index2) {\n");
    SOURCE_CODE.append("   int tmp = array[index1];\n");
    SOURCE_CODE.append("   array[index1] = array[index1 + index2];\n");
    SOURCE_CODE.append("   array[index1 + index2] = tmp;\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE.append("   return array;\n");
    SOURCE_CODE.append("}\n");
  }

  private static final String[] SOURCE_CODE_SS  = {
      "public int[] swapsort(int[] array) {", // 0
      "int i = 0;", // 1
      "", // 2
      "while(i < array.length - 1) {", // 3
      "// zähle die kleinere Elemente", // 4
      "int count = countSmallerElements(array , i);", // 5
      "", // 6
      "if (count > 0) {", // 7
      "// vertausche Elemente", // 8
      "array = swap(array , i , count);", // 9
      "}", // 10
      "else {", // 11
      "i++;", // 12
      "}", // 13
      "}", // 14
      "return array;", // 15
      "}", // 16
                                                };

  private static final String[] SOURCE_CODE_CSE = {
      "private int countSmallerElements(int[] array , int index) {", // 0
      "int count = 0;", // 1
      "", // 2
      "for (int i = index + 1; i < array.length; i++) {", // 3
      "if(array[index] > array[i])", // 4
      "count++;", // 5
      "}", // 6
      "", // 7
      "return count;", // 8
      "}", // 9
                                                };

  private static final String[] SOURCE_CODE_S   = {
      "private int[] swap(int[] array , int index1 , int index2) {", // 0
      "   int tmp = array[index1];", // 1
      "   array[index1] = array[index1 + index2];", // 2
      "   array[index1 + index2] = tmp;", // 3
      "", // 4
      "   return array;", // 5
      "}" // 6
                                                };

  @Override
  public void init() {
    // nothing to be done here
  }

  public void localInit() {
    lg = new AnimalScript("Swap Sort Animation",
        "Galin Bobev , Mir-Misagh Zayyeni", 640, 380);
    lg.setStepMode(true);

    setProperties();
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    this.container = new AnimationPropertiesContainer();
    this.primitives = new Hashtable<String, Object>();

    this.container = arg0;
    this.primitives = arg1;

    // properties handle
    localInit();

    // data
    int[] array = (int[]) primitives.get("array");

    // secondary structure of the animation
    title();
    source();

    // algorithm
    swapsort(array);

    return lg.toString();
  }

  private void swapsort(int[] array) {
    // show
    title.show();
    source_code_ss.show();
    source_code_cse.show();
    source_code_s.show();

    lg.nextStep();
    /*
     * Start algorithm
     * 
     * public int[] swapsort(int[] array) { // Line 0
     */
    source_code_ss.highlight(0);
    arrayPrim = lg.newIntArray(new Coordinates(40, 250), array, "intArray",
        null, arrayProperties);
    jMarker = lg.newArrayMarker(arrayPrim, 0, "jMarker", null,
        jMarkerProperties);
    jMarker.hide();
    count_sum = lg.newText(new Coordinates(40, 160), "Count = ", "count", null,
        textProperties);
    count_sum.hide();

    lg.nextStep();
    /*
     * int i = 0; // Line 1
     */
    source_code_ss.unhighlight(0);
    source_code_ss.highlight(1);

    int i = 0;
    iMarker = lg.newArrayMarker(arrayPrim, i, "iMarker", null,
        iMarkerProperties);

    lg.nextStep();
    /*
     * INTO THE - WHILE LOOP
     * 
     * while(i < array.length - 1) { // Line 3
     */
    source_code_ss.unhighlight(1);
    int countSwap = 0;// ///////////////// for exercise
    while (i < array.length - 1) {
      source_code_ss.highlight(3);
      iMarker.move(i, ticks_0, ticks_40);

      lg.nextStep();
      /*
       * THE countSmallerElements FUNCTION
       * 
       * int count = countSmallerElements(array , i); // Line 5
       */
      source_code_ss.unhighlight(3);
      source_code_ss.highlight(5);

      int count = countSmallerElements(array, i);

      lg.nextStep();
      /*
       * IF
       * 
       * if (count > 0) { // Line 7
       */
      source_code_ss.unhighlight(5);
      source_code_ss.highlight(7);

      lg.nextStep();

      if (count > 0) {
        /*
         * IF == TRUE
         * 
         * array = swap(array , i , count); // Line 9
         */
        source_code_ss.unhighlight(7);
        source_code_ss.highlight(9);

        /*
         * THE swap FUNCTION
         */
        swap(array, i, count);
        countSwap++;
        // CheckpointUtils.checkpointEvent(this, "swapAnzahl", new
        // Variable("countswap",countswap));/////////////////////////
        lg.nextStep();
        source_code_ss.unhighlight(9);
      } else {
        /*
         * IF == FALSE
         * 
         * i++; // Line 12
         */
        source_code_ss.unhighlight(7);
        source_code_ss.highlight(12);

        i++;

        lg.nextStep();
        arrayPrim.highlightCell(i - 1, ticks_0, ticks_0);
        source_code_ss.unhighlight(12);
      }
      count_sum.hide();

    }
    CheckpointUtils.checkpointEvent(this, "swapAnzahl", new Variable(
        "countSwap", countSwap));// ///////////////////////
    /*
     * AFTER THE END OF THE WHILE - LOOP
     * 
     * } // Line 14
     */
    arrayPrim.highlightCell(array.length - 1, ticks_0, ticks_0);
    source_code_ss.highlight(14);

    lg.nextStep();

    /*
     * RETURN STATEMENT
     * 
     * return array; // Line 15
     */
    source_code_ss.unhighlight(14);
    source_code_ss.highlight(15);

    lg.nextStep();

    /*
     * END OF THE FUNCTION
     * 
     * } // Line 16
     */
    source_code_ss.unhighlight(15);
    source_code_ss.highlight(16);

    lg.nextStep();
    source_code_ss.unhighlight(16);

    lg.nextStep();
  }

  private int countSmallerElements(int[] array, int index) {
    /*
     * Start of the function
     * 
     * private int countSmallerElements(int[] array , int index) { // Line 0
     */
    source_code_cse.highlight(0);

    lg.nextStep();
    /*
     * 
     * int count = 0; Line 1
     */
    source_code_cse.unhighlight(0);
    source_code_cse.highlight(1);

    int count = 0;

    count_sum.setText("Count = " + count, ticks_0, ticks_0);
    count_sum.show();

    lg.nextStep();
    source_code_cse.unhighlight(1);
    /*
     * FOR - LOOP
     * 
     * for (int i = index + 1; i < array.length; i++) { // Line 3
     */
    jMarker.show();
    for (int i = index + 1; i < array.length; i++) {
      source_code_cse.highlight(3);
      jMarker.move(i, ticks_0, ticks_0);

      lg.nextStep();
      /*
       * IF
       * 
       * if(array[index] > array[i]) // Line 4
       */
      source_code_cse.unhighlight(3);
      source_code_cse.highlight(4);

      lg.nextStep();

      if (array[index] > array[i]) {
        /*
         * IF == TRUE
         */
        source_code_cse.unhighlight(4);
        source_code_cse.highlight(5);

        count++;
        count_sum.setText("Count = " + count, ticks_0, ticks_0);

        lg.nextStep();
        source_code_cse.unhighlight(5);
      } else {
        source_code_cse.unhighlight(4);
      }
    }
    jMarker.hide();
    /*
     * AFTER THE FOR - LOOP
     * 
     * return count; // Line 8
     */
    source_code_cse.unhighlight(3);
    source_code_cse.highlight(8);

    lg.nextStep();

    /*
     * END
     * 
     * } // Line 9
     */
    source_code_cse.unhighlight(8);
    source_code_cse.highlight(9);

    lg.nextStep();

    source_code_cse.unhighlight(9);

    return count;
  }

  private void swap(int[] array, int index1, int index2) {
    /*
     * Start of the function
     * 
     * private int[] swap(int[] array , int index1 , int index2) { // Line 0
     */
    source_code_s.highlight(0);
    jMarker.move((index1 + index2), ticks_0, ticks_0);
    jMarker.show();

    lg.nextStep();
    /*
     * SWAP ELEMENTS
     */
    source_code_s.unhighlight(0);
    source_code_s.highlight(1);
    source_code_s.highlight(2);
    source_code_s.highlight(3);

    arrayPrim.swap(index1, (index1 + index2), ticks_40, ticks_100);

    lg.nextStep();

    jMarker.hide();
    source_code_s.unhighlight(1);
    source_code_s.unhighlight(2);
    source_code_s.unhighlight(3);
    source_code_s.highlight(5);

    lg.nextStep();

    source_code_s.unhighlight(5);
    source_code_s.highlight(6);

    lg.nextStep();

    source_code_s.unhighlight(6);

    jMarker.hide();
    CheckpointUtils
        .checkpointEvent(this, "swap", new Variable("austausch_i",
            array[index1]), new Variable("austausch_count", array[index1
            + index2]));// //////////////////////////////////////////////

  }

  /*
   * This functions set the user-defined properties
   */
  private void setSourceProp() {
    sourceProperties = (SourceCodeProperties) container
        .getPropertiesByName("sourceCode");
  }

  private void setArrayProp() {
    arrayProperties = (ArrayProperties) container.getPropertiesByName("array");
  }

  private void setiMarkerProp() {
    iMarkerProperties = (ArrayMarkerProperties) container
        .getPropertiesByName("i");
  }

  private void setjMarkerProp() {
    jMarkerProperties = (ArrayMarkerProperties) container
        .getPropertiesByName("j");
  }

  /*
   * This function set the default properties
   */
  private void setDefaultTitleProp() {
    titleProperties = new SourceCodeProperties();
    titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 40));
    titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  private void setDefaultTextProp() {
    textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
  }

  private void setDefaultSourceProp() {
    sourceProperties = new SourceCodeProperties();
    sourceProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    sourceProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    sourceProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.red);
  }

  private void setDefaultArrayProp() {
    arrayProperties = new ArrayProperties();
    arrayProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.BLACK);
    arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
    arrayProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.ORANGE);
    arrayProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.RED);
    arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
  }

  private void setDefaultiMarkerProp() {
    iMarkerProperties = new ArrayMarkerProperties();
    iMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    iMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, 'i');
  }

  private void setDefaultjMarkerProp() {
    jMarkerProperties = new ArrayMarkerProperties();
    jMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    jMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, 'j');
  }

  private void setProperties() {
    setDefaultTitleProp();
    setDefaultTextProp();

    // set source code user-defined/default properties
    if (container.getPropertiesByName("sourceCode") != null)
      setSourceProp();
    else
      setDefaultSourceProp();

    // set array user-defined/default properties
    if (container.getPropertiesByName("sourceCode") != null)
      setArrayProp();
    else
      setDefaultArrayProp();

    // set iArrayMarker user-defined/default properties
    if (container.getPropertiesByName("sourceCode") != null)
      setiMarkerProp();
    else
      setDefaultiMarkerProp();

    // set jArrayMarker user-defined/default properties
    if (container.getPropertiesByName("sourceCode") != null)
      setjMarkerProp();
    else
      setDefaultjMarkerProp();
  }

  private void title() {
    title = lg.newSourceCode(new Coordinates(35, 15), "title", null,
        titleProperties);
    title.addCodeLine("SwapSort", "title", 0, null);
    title.show();

    RectProperties title_rect_prop = new RectProperties();
    title_rect_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    title_rect_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    title_rect_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    // rect_source_code =
    lg.newRect(new Offset(-20, -20, "title", "NW"), new Offset(20, 20, "title",
        "SE"), "rect2", null, title_rect_prop);
  }

  private void source() {
    source_ss();
    source_cse();
    source_s();
  }

  private void source_ss() {
    source_code_ss = lg.newSourceCode(new Coordinates(400, 150),
        "sourceCodeSS", null, sourceProperties);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    source_code_ss.addCodeLine(SOURCE_CODE_SS[0], null, 0, null); // 0
    source_code_ss.addCodeLine(SOURCE_CODE_SS[1], null, 1, null); // 1
    source_code_ss.addCodeLine(SOURCE_CODE_SS[2], null, 1, null); // 2--
    source_code_ss.addCodeLine(SOURCE_CODE_SS[3], null, 1, null); // 3
    source_code_ss.addCodeLine(SOURCE_CODE_SS[4], null, 2, null); // 4--
    source_code_ss.addCodeLine(SOURCE_CODE_SS[5], null, 2, null); // 5
    source_code_ss.addCodeLine(SOURCE_CODE_SS[6], null, 2, null); // 6--
    source_code_ss.addCodeLine(SOURCE_CODE_SS[7], null, 2, null); // 7
    source_code_ss.addCodeLine(SOURCE_CODE_SS[8], null, 3, null); // 8--
    source_code_ss.addCodeLine(SOURCE_CODE_SS[9], null, 3, null); // 9
    source_code_ss.addCodeLine(SOURCE_CODE_SS[10], null, 2, null); // 10
    source_code_ss.addCodeLine(SOURCE_CODE_SS[11], null, 2, null); // 11
    source_code_ss.addCodeLine(SOURCE_CODE_SS[12], null, 3, null); // 12
    source_code_ss.addCodeLine(SOURCE_CODE_SS[13], null, 2, null); // 13
    source_code_ss.addCodeLine(SOURCE_CODE_SS[14], null, 1, null); // 14
    source_code_ss.addCodeLine(SOURCE_CODE_SS[15], null, 1, null); // 15
    source_code_ss.addCodeLine(SOURCE_CODE_SS[16], null, 0, null); // 16

    RectProperties source_rect_prop = new RectProperties();
    source_rect_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    source_rect_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    source_rect_prop
        .set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
    // rect_source_code =
    lg.newRect(new Offset(-10, -10, "sourceCodeSS", "NW"), new Offset(10, 10,
        "sourceCodeSS", "SE"), "rect2", null, source_rect_prop);
  }

  private void source_cse() {
    source_code_cse = lg.newSourceCode(new Coordinates(790, 150),
        "sourceCodeCSE", null, sourceProperties);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[0], null, 0, null); // 0
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[1], null, 1, null); // 1
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[2], null, 1, null); // 2--
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[3], null, 1, null); // 3
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[4], null, 2, null); // 4
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[5], null, 3, null); // 5
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[6], null, 1, null); // 6
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[7], null, 1, null); // 7--
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[8], null, 1, null); // 8
    source_code_cse.addCodeLine(SOURCE_CODE_CSE[9], null, 0, null); // 9

    RectProperties source_rect_prop = new RectProperties();
    source_rect_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    source_rect_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    source_rect_prop
        .set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
    // rect_source_code =
    lg.newRect(new Offset(-10, -10, "sourceCodeCSE", "NW"), new Offset(10, 10,
        "sourceCodeCSE", "SE"), "rect2", null, source_rect_prop);
  }

  private void source_s() {
    source_code_s = lg.newSourceCode(new Coordinates(790, 320), "sourceCodeS",
        null, sourceProperties);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    source_code_s.addCodeLine(SOURCE_CODE_S[0], null, 0, null); // 0
    source_code_s.addCodeLine(SOURCE_CODE_S[1], null, 1, null); // 1
    source_code_s.addCodeLine(SOURCE_CODE_S[2], null, 1, null); // 2
    source_code_s.addCodeLine(SOURCE_CODE_S[3], null, 1, null); // 3
    source_code_s.addCodeLine(SOURCE_CODE_S[4], null, 1, null); // 4--
    source_code_s.addCodeLine(SOURCE_CODE_S[5], null, 1, null); // 5
    source_code_s.addCodeLine(SOURCE_CODE_S[6], null, 0, null); // 6

    RectProperties source_rect_prop = new RectProperties();
    source_rect_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    source_rect_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    source_rect_prop
        .set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
    // rect_source_code =
    lg.newRect(new Offset(-10, -10, "sourceCodeS", "NW"), new Offset(10, 1,
        "sourceCodeS", "SE"), "rect2", null, source_rect_prop);
  }

  @Override
  public String getAlgorithmName() {
    return "Swap Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Galin Bobev, Misagh Zayyeni";
  }

  @Override
  public String getCodeExample() {
    initSourceCode();
    return SOURCE_CODE.toString();
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    String description = "";

    for (int i = 0; i < DESCRIPTION.length; i++) {
      description += DESCRIPTION[i];
    }

    return description;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return "Swap Sort Animation";
  }

  @Override
  public String getOutputLanguage() {
    return generators.framework.Generator.JAVA_OUTPUT;
  }

}
