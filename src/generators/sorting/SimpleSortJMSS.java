package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class SimpleSortJMSS implements Generator {

  protected Language              lang;

  private int[]                   testdata            = new int[] { 1, 9, 2,
      17, 22, 4                                      };

  // private Text headerText;
  // private Rect headerRect;
  private SourceCode              sourceCode;
  // private Rect sourceCodeRect;
  private IntArray                intArray;
  private ArrayMarker             iMark;
  private ArrayMarker             jMark;
  private Text                    infoText;
  // private Rect infoRect;
  private Timing                  tickTime            = new TicksTiming(15);
  private Timing                  longTickTime        = new TicksTiming(30);
  private int                     ctr;

  protected TextProperties        headerTextProps     = new TextProperties();
  protected RectProperties        headerRectProps     = new RectProperties();
  protected SourceCodeProperties  sourceCodeProps     = new SourceCodeProperties();
  protected RectProperties        sourceCodeRectProps = new RectProperties();
  protected ArrayProperties       arrayProps          = new ArrayProperties();
  protected ArrayMarkerProperties iMarkProps          = new ArrayMarkerProperties();
  protected ArrayMarkerProperties jMarkProps          = new ArrayMarkerProperties();
  protected TextProperties        infoBoxProps        = new TextProperties();
  protected RectProperties        infoBoxRect         = new RectProperties();

  public SimpleSortJMSS() {
    this.init();
  }

  @Override
  public void init() {
    lang = new AnimalScript("SimpleSort", "Jerome Moeckel, Stefan Schmitz",
        800, 600);
    lang.setStepMode(true);
    ctr = 0;
  }

  // Generator
  @Override
  public String generate(AnimationPropertiesContainer apc,
      Hashtable<String, Object> primitives) {
    // init lang + ctr
    this.init();

    // fetch properties from XML
    this.sourceCodeProps = (SourceCodeProperties) apc.get(0);
    this.sourceCodeRectProps = (RectProperties) apc.get(1);
    this.arrayProps = (ArrayProperties) apc.get(2);
    this.headerTextProps = (TextProperties) apc.get(3);
    this.headerRectProps = (RectProperties) apc.get(4);
    this.iMarkProps = (ArrayMarkerProperties) apc.get(5);
    this.jMarkProps = (ArrayMarkerProperties) apc.get(6);
    this.infoBoxProps = (TextProperties) apc.get(7);
    this.infoBoxRect = (RectProperties) apc.get(8);
    testdata = (int[]) primitives.get("intArray");

    // header
    // headerText =
    lang.newText(new Coordinates(50, 25), "SimpleSort", "header", null,
        headerTextProps);
    // headerRect =
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null,
        headerRectProps);
    // SourceCode
    sourceCode = lang.newSourceCode(new Coordinates(50, 100), "sourceCode",
        null, sourceCodeProps);
    sourceCode.addCodeLine("public void simpleSort(int[] data) {", null, 0,
        null);
    sourceCode.addCodeLine("for (int i = 0; i < data.length; i++) {", null, 1,
        null);
    sourceCode.addCodeLine("for (int j = i + 1; j < data.length; j++) {", null,
        2, null);
    sourceCode.addCodeLine("if (data[i] > data[j]) {", null, 3, null);
    sourceCode.addCodeLine("int temp = data[i];", null, 4, null);
    sourceCode.addCodeLine("data[i] = data[j];", null, 4, null);
    sourceCode.addCodeLine("data[j] = temp;", null, 4, null);
    sourceCode.addCodeLine("}", null, 3, null);
    sourceCode.addCodeLine("}", null, 2, null);
    sourceCode.addCodeLine("}", null, 1, null);
    sourceCode.addCodeLine("}", null, 0, null);

    // sourceCodeRect =
    lang.newRect(new Offset(-5, -5, "sourceCode", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "sourceCode", AnimalScript.DIRECTION_SE), "scRect",
        null, sourceCodeRectProps);

    // array
    intArray = lang.newIntArray(new Offset(250, 0, "sourceCode",
        AnimalScript.DIRECTION_NE), testdata, "intArray", null, arrayProps);

    // marker for i + j
    iMark = lang.newArrayMarker(intArray, 0, "iMarker", null, iMarkProps);
    jMark = lang.newArrayMarker(intArray, iMark.getPosition() + 1, "jMarker",
        null, jMarkProps);

    // evaluation box
    infoText = lang.newText(new Coordinates(630, 200), "! !    init    ! !",
        "info", null, infoBoxProps);
    // infoRect =
    lang.newRect(new Offset(-5, -5, "info", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "info", AnimalScript.DIRECTION_SE), "infoRect", null,
        infoBoxRect);
    sourceCode.highlight(0);
    lang.nextStep();

    this.simpleSort(testdata);
    return lang.toString();
  }

  /*
   * SimpleSort
   * 
   * Sorts an intArray and creates Animal Script
   */
  private void simpleSort(int[] data) {

    for (iMark.move(iMark.getPosition(), null, tickTime); iMark.getPosition() < intArray
        .getLength(); iMark.increment(null, tickTime)) { // Zeile 1
      sourceCode.toggleHighlight(0, 0, false, 1, 0, tickTime, tickTime);
      infoText.setText(
          "for i = " + iMark.getPosition() + " < " + intArray.getLength(),
          tickTime, tickTime);
      lang.nextStep();

      for (jMark.move(iMark.getPosition() + 1, null, tickTime); jMark
          .getPosition() < intArray.getLength(); jMark
          .increment(null, tickTime)) { // Zeile 2
        sourceCode.toggleHighlight(1, 0, false, 2, 0, null, tickTime);
        infoText.setText(" if " + intArray.getData(iMark.getPosition()) + " > "
            + intArray.getData(jMark.getPosition()), null, tickTime);
        lang.nextStep();

        if (intArray.getData(iMark.getPosition()) > intArray.getData(jMark
            .getPosition())) {
          ctr++;
          sourceCode.toggleHighlight(2, 0, false, 3, 0, null, tickTime);
          infoText.setText("-> true ! ", null, null);
          lang.nextStep();
          // highlight
          for (int c = 4; c <= 6; c++)
            sourceCode.highlight(c, 0, false, null, longTickTime);
          intArray.highlightCell(iMark.getPosition(), tickTime, longTickTime);
          intArray.highlightCell(jMark.getPosition(), tickTime, longTickTime);
          lang.nextStep();
          // swap :: int temp = data[i]; data[i] = data[j]; data[j] = temp;
          intArray.swap(iMark.getPosition(), jMark.getPosition(), tickTime,
              longTickTime);
          lang.nextStep();
          // unhighlight
          intArray.unhighlightCell(iMark.getPosition(), null, tickTime);
          intArray.unhighlightCell(jMark.getPosition(), null, tickTime);
          for (int c = 4; c <= 6; c++)
            sourceCode.unhighlight(c, 0, false, tickTime, tickTime);
          lang.nextStep();
        } else {
          infoText.setText("-> false ! ", null, null);
          lang.nextStep();
        }
        sourceCode.unhighlight(3);
      }
      sourceCode.unhighlight(2);
      lang.nextStep();
    }
    sourceCode.unhighlight(1);
    jMark.hide();
    iMark.hide();
    infoText.setText("# swapped: " + ctr, null, null);
    lang.nextStep();
  }

  @Override
  public String getAlgorithmName() {
    return "Simple Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Jerome Möckel, Stefan Schmitz";
  }

  public String getannotatedCodeExample() {
    String annoSource = "private void simpleSort(int [] data) {         @label(\"methodenkopf\")                   \n"
        + "  for (  int i = 0;              @label(\"for_i\") @declare(\"int\", \"i\", \"0\"   "
        + "      i < data.length;             @label(\"i_lower_data)                 "
        + "    i++) {                  @label(\"i_lower_data                          \n"
        + "    for (  int j = i + 1;          @label(\"i_lower_data    "
        + "        j < data.length;        @label(\"i_lower_data      "
        + "        j++) {   \n            @label(\"i_lower_data    "
        + "      if (  data[i]           @label(\"i_lower_data"
        + "          > data[j]) {  \n        @label(\"i_lower_data"
        + "        int temp = data[i];   \n  @label(\"i_lower_data  "
        + "          data[i] = data[j];       @label(\"i_lower_data  \n"
        + "          data[j] = temp;        @label(\"i_lower_data \n"
        + "      }   \n" + "    }  \n" + "  }  \n" + "}  \n";
    return annoSource;
  }

  @Override
  public String getCodeExample() {
    String source = "private void simpleSort(int [] data) {  \n"
        + "  for (int i = 0; i < data.length; i++) {  \n"
        + "    for (int j = i + 1; j < data.length; j++) {   \n"
        + "      if (data[i] > data[j]) {  \n"
        + "        int temp = data[i];   \n" + "        data[i] = data[j]; \n"
        + "        data[j] = temp;  \n" + "      }   \n" + "    }  \n"
        + "  }  \n" + "}  \n";
    return source;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Einfache Sortierung eines Arrays mittels SimpleSort";
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
    return "SimpleSort";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}
