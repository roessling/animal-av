package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.TicksTiming;

public class AnimalShakerSort implements Generator {

  /**
   * The concrete language used for creating output
   */
  private Language      language;

  private final String  ALGORITHMNAME = "Shaker Sort";
  private final String  NAME          = "Shaker Sort";
  private final String  AUTHOR        = "Roger Ponka";

  ArrayMarkerProperties currentArrayMarker;
  ArrayMarkerProperties leftArrayMarker;
  SourceCodeProperties  sourceCodeProperties;
  ArrayMarkerProperties rightArrayMarker;
  ArrayProperties       arrayProperties;
  TextProperties        textProperties;

  public AnimalShakerSort(Language language) {
    this.language = language;
    language.setStepMode(true);
  }

  public AnimalShakerSort() {
  }

  private static final String DESCRIPTION = "Shakersort is a variation of bubble sort that is both a stable sorting algorithm and a comparison sort.\n"
                                              + "The algorithm differs from bubble sort in that sorts in both directions each pass through the list.\n"
                                              + "This sorting algorithm is only marginally more difficult than bubble sort to implement, and solves\n"
                                              + "the problem with so-called turtles in bubble sort."
                                              + " ";

  private static final String SOURCE_CODE = "public void sort(int[] values)\n"
                                              + "{ \n"
                                              + " boolean sortiert = false; \n"
                                              + " boolean vorwaerts = true; \n"
                                              + " int left = 0; \n"
                                              + " int right = values.length-1; \n"
                                              + " int x ; \n"
                                              + " while (left &lt right && !sortiert ) \n"
                                              + " { \n"
                                              + "  sortiert = true; \n"
                                              + "  if ( vorwaerts ) \n"
                                              + "  { \n"
                                              + "   for (int i = left; i &lt right; i++)\n"
                                              + "   { \n"
                                              + "    if (values[i] &gt values[i + 1])\n"
                                              + "    {\n"
                                              + "     x = values[i];\n"
                                              + "     values[i] = values[i + 1]; \n"
                                              + "     values[i + 1] = x; \n"
                                              + "     sortiert = false; \n"
                                              + "    }\n"
                                              + "   }\n"
                                              + "   right--; \n"
                                              + "   else \n"
                                              + "   { \n"
                                              + "    for (int i = right; i &gt left; i--)\n"
                                              + "    { \n"
                                              + "     if (values[i] &lt values[i-1]) \n"
                                              + "     { \n"
                                              + "      x = values[i]; \n"
                                              + "      values[i] = values[i-1]; \n"
                                              + "      values[i-1] = x; \n"
                                              + "      sortiert = false; \n"
                                              + "     } \n" + "    } \n"
                                              + "   left++; \n" + "  } \n"
                                              + "  vorwaerts = !vorwaerts; \n"
                                              + " } \n" + "}";

  public void sort(int[] values) {

    TextProperties title = new TextProperties();
    title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("VERDANA",
        Font.BOLD, 20));

    language
        .newText(new Coordinates(20, 20), "ShakerSort", "info", null, title);
    // create the IntArray object, linked to the properties
    IntArray intArray = language.newIntArray(new Coordinates(400, 70), values,
        "array", null, arrayProperties);

    // start a new step after the array was created
    language.nextStep();

    // now, create the source code entity
    SourceCode sourceCode = language.newSourceCode(new Coordinates(20, 105),
        "code", null, sourceCodeProperties);

    // Add the lines to the SourceCode object.
    sourceCode.addCodeLine("public void shakerSort( int[] values){", null, 0,
        null); // 0
    sourceCode.addCodeLine(
        "boolean sortiert = false; boolean vorwaerts = true; "
            + "int left = 0; int right = values.length-1; int x;", null, 1,
        null); // 1
    sourceCode
        .addCodeLine("while (left < right && !sortiert ){", null, 1, null); // 2
    sourceCode.addCodeLine("sortiert = true;", null, 2, null); // 3
    sourceCode.addCodeLine("if ( vorwaerts ){", null, 2, null); // 4
    sourceCode.addCodeLine("for(int i = left; i < right; i++){", null, 3, null); // 5
    sourceCode.addCodeLine("if(values[i] > values[i+1]){", null, 4, null); // 6
    sourceCode.addCodeLine("x = values[i];", null, 5, null); // 7
    sourceCode.addCodeLine("values[i] = values[i + 1];", null, 5, null); // 8
    sourceCode.addCodeLine("values[i + 1] = x;", null, 5, null); // 9
    sourceCode.addCodeLine("sortiert = false;", null, 5, null); // 10
    sourceCode.addCodeLine("}", null, 4, null); // 11
    sourceCode.addCodeLine("}", null, 3, null); // 12
    sourceCode.addCodeLine("right--;", null, 3, null); // 13
    sourceCode.addCodeLine("}", null, 2, null); // 14
    sourceCode.addCodeLine("else{", null, 2, null); // 15
    sourceCode.addCodeLine("for(int i = right; i > left; i--){", null, 3, null); // 16
    sourceCode.addCodeLine("if(values[i] < values[i-1]){", null, 4, null); // 17
    sourceCode.addCodeLine("x = values[i];", null, 5, null); // 18
    sourceCode.addCodeLine("values[i] = values[i-1];", null, 5, null); // 19
    sourceCode.addCodeLine("values[i-1] = x;", null, 5, null); // 20
    sourceCode.addCodeLine("sortiert = false;", null, 5, null); // 21
    sourceCode.addCodeLine("}", null, 4, null); // 22
    sourceCode.addCodeLine("}", null, 3, null); // 23
    sourceCode.addCodeLine("left++;", null, 3, null); // 24
    sourceCode.addCodeLine("}", null, 2, null); // 25
    sourceCode.addCodeLine("vorwaerts = !vorwaerts;", null, 2, null); // 26
    sourceCode.addCodeLine("}", null, 1, null);
    sourceCode.addCodeLine("}", null, 0, null);

    // The print out the source code
    language.nextStep();

    try {

      shakerSort(intArray, sourceCode);
    } catch (LineNotExistsException ex) {
      ex.printStackTrace();
    }
  }

  private void shakerSort(IntArray intArray, SourceCode sourceCode)
      throws LineNotExistsException {

    final MsTiming msTiming = new MsTiming(100);
    final TicksTiming ticksTiming = new TicksTiming(20);

    boolean sortiert = false;
    boolean vorwaerts = true;
    int left = 0;
    int right = intArray.getLength() - 1;
    int x = 0; // Hilfsvariabel zum tauschen

    /*
     * Highlight the first line
     */
    sourceCode.highlight(0, 0, false);
    language.nextStep();

    // Highlight next line : Variables declaration
    sourceCode.toggleHighlight(0, 0, false, 1, 0);

    // Create a marker to point on start
    ArrayMarker leftMarker = language.newArrayMarker(intArray, 0, "left", null,
        leftArrayMarker);

    // Create a marker to point on end
    ArrayMarker rightMarker = language.newArrayMarker(intArray, 0, "right",
        null, rightArrayMarker);

    Text sortiertText = language.newText(new Coordinates(20, 55), "sortiert = "
        + sortiert, "sortiert", null, textProperties);
    Text vorwaertsText = language.newText(new Coordinates(20, 75),
        "vorwaerts = " + vorwaerts, "vorwaerts", null, textProperties);
    Text xText = language.newText(new Coordinates(20, 90), "x = " + x, "x",
        null, textProperties);

    // make the markers and local variables visible
    language.nextStep();

    // Highlight the while loop and show the marker on end
    rightMarker.move(right, null, null);
    sourceCode.toggleHighlight(1, 0, false, 2, 0);
    language.nextStep();

    while (left < right && !sortiert) {

      sortiert = true;
      sortiertText.setText("sortiert = " + sortiert, msTiming, ticksTiming);
      // Highlight the next line
      sourceCode.toggleHighlight(2, 0, false, 3, 0);

      language.nextStep();

      if (!vorwaerts) {
        sourceCode.toggleHighlight(3, 0, false, 4, 0);
        language.nextStep();
        sourceCode.toggleHighlight(4, 0, false, 15, 0);
        language.nextStep();
      } else {

        sourceCode.toggleHighlight(3, 0, false, 4, 0);
        language.nextStep();
      }

      if (vorwaerts) {

        sourceCode.toggleHighlight(4, 0, false, 5, 0);
        ArrayMarker iMarker = language.newArrayMarker(intArray, left, "i",
            null, currentArrayMarker);
        language.nextStep();

        for (int i = left; i < right; ++i) {

          boolean tausch = intArray.getData(i) > intArray.getData(i + 1);

          sourceCode.toggleHighlight(5, 0, false, 6, 0);
          intArray.highlightCell(i, null, null);
          intArray.highlightCell(i + 1, null, null);
          language.nextStep();

          if (!tausch) {

            iMarker.move(i + 1, null, null);
            intArray.unhighlightCell(i, null, null);
            intArray.unhighlightCell(i + 1, null, null);
            sourceCode.toggleHighlight(6, 0, false, 5, 0);
            language.nextStep();
          }

          else {
            sourceCode.toggleHighlight(6, 0, false, 7, 0);
            x = intArray.getData(i);
            xText.setText("x = " + x, msTiming, ticksTiming);
            language.nextStep();
            sourceCode.toggleHighlight(7, 0, false, 8, 0);
            intArray.put(i, intArray.getData(i + 1), msTiming, ticksTiming);
            language.nextStep();
            sourceCode.toggleHighlight(8, 0, false, 9, 0);
            intArray.put(i + 1, x, msTiming, ticksTiming);
            language.nextStep();
            sourceCode.toggleHighlight(9, 0, false, 10, 0);
            sortiert = false;
            sortiertText.setText("sortiert = " + sortiert, msTiming,
                ticksTiming);
            language.nextStep();
            iMarker.move(i + 1, null, null);
            sourceCode.toggleHighlight(10, 0, false, 5, 0);
            intArray.unhighlightCell(i, null, null);
            intArray.unhighlightCell(i + 1, null, null);
            language.nextStep();
          }
        }
        sourceCode.toggleHighlight(5, 0, false, 13, 0);
        right--;
        intArray.highlightElem(right + 1, null, null);
        rightMarker.move(right, null, null);
        language.nextStep();
        sourceCode.toggleHighlight(13, 0, false, 26, 0);
        vorwaertsText.setText("vorwaerts = " + !vorwaerts, msTiming,
            ticksTiming);
        iMarker.hide();
        language.nextStep();
      } else {
        sourceCode.toggleHighlight(15, 0, false, 16, 0);
        ArrayMarker iMarker = language.newArrayMarker(intArray, right, "i",
            null, currentArrayMarker);

        language.nextStep();
        for (int i = right; i > left; --i) {

          sourceCode.toggleHighlight(16, 0, false, 17, 0);
          intArray.highlightCell(i, null, null);
          intArray.highlightCell(i - 1, null, null);
          language.nextStep();

          boolean tausch = intArray.getData(i) < intArray.getData(i - 1);

          if (!tausch) {

            sourceCode.toggleHighlight(17, 0, false, 16, 0);
            intArray.unhighlightCell(i, null, null);
            intArray.unhighlightCell(i - 1, null, null);
            iMarker.move(i - 1, null, null);
            language.nextStep();
          }

          else {

            sourceCode.toggleHighlight(17, 0, false, 18, 0);
            x = intArray.getData(i);
            xText.setText("x = " + x, msTiming, ticksTiming);
            language.nextStep();
            sourceCode.toggleHighlight(18, 0, false, 19, 0);
            intArray.put(i, intArray.getData(i - 1), msTiming, ticksTiming);
            language.nextStep();
            sourceCode.toggleHighlight(19, 0, false, 20, 0);
            intArray.put(i - 1, x, msTiming, ticksTiming);
            language.nextStep();
            sourceCode.toggleHighlight(20, 0, false, 21, 0);
            sortiert = false;
            sortiertText.setText("sortiert = " + sortiert, msTiming,
                ticksTiming);
            language.nextStep();
            iMarker.move(i - 1, null, null);
            sourceCode.toggleHighlight(21, 0, false, 16, 0);
            intArray.unhighlightCell(i, null, null);
            intArray.unhighlightCell(i - 1, null, null);
            language.nextStep();
          }
        }

        sourceCode.toggleHighlight(16, 0, false, 24, 0);
        left++;
        intArray.highlightElem(left - 1, null, null);
        leftMarker.move(left, null, null);
        language.nextStep();
        sourceCode.toggleHighlight(24, 0, false, 26, 0);
        vorwaertsText.setText("vorwaerts = " + !vorwaerts, msTiming,
            ticksTiming);
        iMarker.hide();
        language.nextStep();
      }
      vorwaerts = !vorwaerts;
      sourceCode.toggleHighlight(26, 0, false, 2, 0);
      language.nextStep();

      // clean all variables
      if (left == right || sortiert) {
        sourceCode.unhighlight(2, 0, false);
        xText.hide();
        leftMarker.hide();
        rightMarker.hide();
        vorwaertsText.hide();
        sortiertText.hide();
        for (int k = left; k <= right; ++k) {
          intArray.highlightElem(k, null, null);
        }
        language.nextStep();
        break;
      }
    }

  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();

    textProperties = (TextProperties) props
        .getPropertiesByName("textProperties");
    leftArrayMarker = (ArrayMarkerProperties) props
        .getPropertiesByName("leftArrayMarker");
    rightArrayMarker = (ArrayMarkerProperties) props
        .getPropertiesByName("rightArrayMarker");
    currentArrayMarker = (ArrayMarkerProperties) props
        .getPropertiesByName("currentArrayMarker");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    arrayProperties = (ArrayProperties) props
        .getPropertiesByName("arrayProperties");

    sort((int[]) primitives.get("dataArray"));

    System.out.println(language);
    return language.toString();
  }

  public String getAlgorithmName() {
    return ALGORITHMNAME;
  }

  public String getAnimationAuthor() {
    return AUTHOR;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return NAME;
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void init() {
    language = new AnimalScript(getAlgorithmName(), getAnimationAuthor(), 640,
        540);
    language.setStepMode(true);
  }

}
