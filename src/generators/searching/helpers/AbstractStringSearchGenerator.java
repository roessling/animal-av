package generators.searching.helpers;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import translator.Translator;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public abstract class AbstractStringSearchGenerator implements
    ValidatingGenerator {

  protected Translator           translator;
  protected Language             lang;
  protected String               primitiveText;
  protected String               primitivePattern;
  protected ArrayProperties      textArrayProperties;
  protected SourceCodeProperties sourceCodeProperties;
  protected StringArray          animationText;
  protected StringArray          animationPattern;
  protected SourceCode           phaseCode;
  protected SourceCode           mainCode;
  protected Text                 explanation;
  protected Text                 comparisonCounter;
  protected ArrayMarker          textMarker;
  protected ArrayMarker          patternMarker;
  protected Node                 phaseCodeCoordinates;
  protected int                  comparisons;

  protected char[]               pattern, text;
  protected int                  patternLength, textLength;

  public AbstractStringSearchGenerator(String resourceName, Locale locale) {
    translator = new Translator(resourceName, locale);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    readPrimitives(primitives);
    readProperties(props);
    introduction();
    prepareWindow();
    search(primitiveText, primitivePattern);
    compareToLinearSearch();
    return lang.toString();
  }

  public String getAlgorithmName() {
    return translator.translateMessage("algorithmName");
  }

  public String getName() {
    return translator.translateMessage("generatorName");
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return translator.getCurrentLocale();
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
    lang.setStepMode(true);
    comparisons = 0;
  }

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    String myText = (String) primitives.get("Text");
    String myPattern = (String) primitives.get("Pattern");
    if (myText == null || myText.length() == 0)
      throw new IllegalArgumentException(
          translator.translateMessage("noTextInput"));
    if (myPattern == null || myPattern.length() == 0)
      throw new IllegalArgumentException(
          translator.translateMessage("noPatternInput"));
    return true;
  }

  protected void readPrimitives(Hashtable<String, Object> primitives) {
    primitiveText = (String) primitives.get("Text");
    primitivePattern = (String) primitives.get("Pattern");
  }

  protected void readProperties(AnimationPropertiesContainer props) {
    textArrayProperties = (ArrayProperties) props
        .getPropertiesByName("TextArrayProperties");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("SourceCodeProperties");
  }

  protected void introduction() {
    TextProperties titleProperties = new TextProperties();
    titleProperties.set("font", new Font("Monospaced", 1, 20));
    lang.newText(new Coordinates(50, 20), getName(), "introductionTitle", null,
        titleProperties);
    String[] lines = getDescription().split("\\. ");
    lang.newText(new Offset(0, 10, "introductionTitle", "SW"), lines[0] + ".",
        "line0", null);
    for (int i = 1; i < lines.length; i++) {
      lang.newText(new Offset(0, 5, "line" + (i - 1), "SW"), lines[i] + ".",
          "line" + i, null);
    }
    lang.nextStep(); // step

    lang.hideAllPrimitives();
  }

  protected void prepareWindow() {
    // show title
    TextProperties titleProperties = new TextProperties();
    titleProperties.set("font", new Font("Monospaced", 1, 20));
    lang.newText(new Coordinates(5, 5), getAlgorithmName(), "title", null,
        titleProperties);

    // dummy code line to determine the north east corner of the source code
    // part
    mainCode = lang.newSourceCode(new Offset(5, 20, "title", "SW"),
        "dummyCode", null, sourceCodeProperties);
    StringBuilder dummyLineBuilder = new StringBuilder();
    for (int i = 0; i < getCodeWidth(); i++) {
      dummyLineBuilder.append('e');
    }
    mainCode.addCodeLine(dummyLineBuilder.toString(), "longestLine1", 0, null);
    mainCode.hide();
    Node middleLineTop = new Offset(0, -14, "dummyCode", "NE");

    // show the main source code
    mainCode = lang.newSourceCode(new Offset(5, 10, "title", "SW"),
        "mainSourceCode", null, sourceCodeProperties);
    fillMainCode();

    // initialize phaseCodeCoordinates
    phaseCodeCoordinates = new Offset(0, 10, "mainSourceCode", "SW");

    // dummy "phase code" to determine maximum size
    phaseCode = lang.newSourceCode(phaseCodeCoordinates, "dummyPhaseCode",
        null, sourceCodeProperties);
    for (int i = 1; i < getCodeHeigth(); i++) {
      phaseCode.addCodeLine("", "", 0, null);
    }
    phaseCode.addCodeLine(dummyLineBuilder.toString(), "longestLine2", 0, null);
    phaseCode.hide();

    // horizontal line between the "main code" and the "phase code"
    lang.newPolyline(new Node[] { new Offset(-5, -5, "dummyPhaseCode", "NW"),
        new Offset(0, -5, "dummyPhaseCode", "NE") }, "codeDivider", null);
    // vertical line between source code and the right part of the animation
    lang.newPolyline(new Node[] { middleLineTop,
        new Offset(0, 0, "dummyPhaseCode", "SE") }, "middleLine", null);

    // show the text
    String[] textStringArray = new String[primitiveText.length()];
    for (int i = 0; i < primitiveText.length(); i++) {
      textStringArray[i] = ((Character) primitiveText.charAt(i)).toString();
    }
    lang.newText(new Offset(10, 50, "dummyCode", "NE"),
        translator.translateMessage("label_Text"), "label_Text", null);
    animationText = lang.newStringArray(new Offset(100, 0, "label_Text", "NW"),
        textStringArray, "text", null, textArrayProperties);

    // show the pattern
    lang.newText(new Offset(0, 50, "label_Text", "SW"),
        translator.translateMessage("label_Pattern"), "label_Pattern", null);
    String[] patternStringArray = new String[primitivePattern.length()];
    for (int i = 0; i < primitivePattern.length(); i++) {
      patternStringArray[i] = ((Character) primitivePattern.charAt(i))
          .toString();
    }
    animationPattern = lang.newStringArray(new Offset(100, 0, "label_Pattern",
        "NW"), patternStringArray, "pattern", null, textArrayProperties);

    // horizontal line under the title
    lang.newPolyline(new Node[] { new Offset(-5, -5, "mainSourceCode", "NW"),
        new Offset(0, -65, "text", "NE") }, "titleLine", null);

    // initialize explanation of the current step
    explanation = lang.newText(new Offset(0, 0, "title", "SW"), "",
        "explanation", null);

    // initialize comparison counter
    comparisonCounter = lang.newText(new Offset(20, 5, "title", "NE"),
        translator.translateMessage("comparisonCounter", "0"),
        "comparisonCounter", null);

    lang.nextStep(); // step
  }

  /**
   * Adds the given code lines to the animation main code.
   */
  private void fillMainCode() {
    List<String> codeLines = getMainCode();
    for (int i = 0; i < codeLines.size(); i++) {
      mainCode.addCodeLine(codeLines.get(i), "mainCode_" + i, 0, null);
    }
  }

  protected void setExplanation(String text) {
    explanation.setText(text, null, null);
  }

  protected int increaseComparisonCount() {
    comparisons++;
    comparisonCounter.setText(
        translator.translateMessage("comparisonCounter",
            String.valueOf(comparisons)), null, null);
    return comparisons;
  }

  /**
   * Returns the number of lines in the longest displayed code block. <br/>
   * No code block should be longer than this.
   */
  abstract protected int getCodeHeigth();

  /**
   * Returns the number of characters in the longest displayed code line,
   * including spaces for indention.
   */
  abstract protected int getCodeWidth();

  /**
   * Returns the main code, including indention, for the animation as an ordered
   * list of strings with one String for each line of code.
   * 
   * @return the code lines
   */
  protected abstract List<String> getMainCode();

  protected abstract List<Integer> search(String inputText, String inputPattern);

  protected boolean inputIsBad(String inputText, String inputPattern) { // code
                                                                        // line
                                                                        // 0
    // show code
    phaseCode = lang.newSourceCode(phaseCodeCoordinates, "inputIsBad", null,
        sourceCodeProperties);
    phaseCode.addCodeLine(
        "private boolean inputIsBad(String inputText, String inputPattern) {",
        "inputIsBad_0", 0, null);
    phaseCode.addCodeLine("return (inputText == null || inputText.isEmpty()",
        "inputIsBad_1", 1, null);
    phaseCode.addCodeLine(
        "|| inputPattern == null || inputPattern.isEmpty() || inputPattern",
        "inputIsBad_2", 2, null);
    phaseCode.addCodeLine(".length() > inputText.length());", "inputIsBad_3",
        2, null);
    phaseCode.addCodeLine("}", "inputIsBad_4", 0, null);
    phaseCode.highlight(1);
    phaseCode.highlight(2);
    phaseCode.highlight(3);

    // show description
    setExplanation(translator.translateMessage("checkInput"));
    lang.nextStep(); // step
    phaseCode.hide();
    // code lines 1 to 3
    return (inputText == null || inputText.isEmpty() || inputPattern == null
        || inputPattern.isEmpty() || inputPattern.length() > inputText.length());
  } // code line 4

  protected void setText(String inputText) { // code line 0
    // show code
    phaseCode = lang.newSourceCode(phaseCodeCoordinates, "setText", null,
        sourceCodeProperties);
    phaseCode.addCodeLine("private void setText(String inputText) {",
        "setText_0", 0, null);
    phaseCode.addCodeLine("textLength = inputText.length();", "setText_1", 1,
        null);
    phaseCode.addCodeLine("text = inputText.toCharArray();", "setText_2", 1,
        null);
    phaseCode.addCodeLine("}", "setText_3", 0, null);

    textLength = inputText.length(); // code line 1
    phaseCode.highlight(1);
    setExplanation(translator.translateMessage("saveTextLength"));
    lang.nextStep(); // step
    phaseCode.unhighlight(1);

    text = inputText.toCharArray(); // code line 2
    setExplanation(translator.translateMessage("saveText"));
    phaseCode.highlight(2);
    lang.nextStep(); // step
    phaseCode.unhighlight(2);
    phaseCode.hide();
  } // code line 3

  protected void setPattern(String inputPattern) { // code line 0
    // show code
    phaseCode = lang.newSourceCode(phaseCodeCoordinates, "setPattern", null,
        sourceCodeProperties);
    phaseCode.addCodeLine("private void setPattern(String inputPattern) {",
        "setPattern_0", 0, null);
    phaseCode.addCodeLine("patternLength = inputPattern.length();",
        "setPattern_1", 1, null);
    phaseCode.addCodeLine("pattern = inputPattern.toCharArray();",
        "setPattern_2", 1, null);
    phaseCode.addCodeLine("}", "setPattern_3", 0, null);

    patternLength = inputPattern.length(); // code line 1
    phaseCode.highlight(1);
    setExplanation(translator.translateMessage("savePatternLength"));
    lang.nextStep(); // step
    phaseCode.unhighlight(1);

    pattern = inputPattern.toCharArray(); // code line 2
    setExplanation(translator.translateMessage("savePattern"));
    phaseCode.highlight(2);
    lang.nextStep(); // step
    phaseCode.unhighlight(2);
    phaseCode.hide();
  } // code line 3

  /**
   * Does a linear search with the same text and pattern and displays how many
   * character comparisons were done for that.
   */
  protected void compareToLinearSearch() {
    int naiveComparisons = 0;
    int i = 0, j = 0;
    while (i <= textLength - patternLength) {
      naiveComparisons++;
      while (j < patternLength && text[i + j] == pattern[j]) {
        j++;
        if (j < patternLength) {
          naiveComparisons++;
        }
      }
      i++;
      j = 0;
    }
    lang.newText(
        new Offset(15, 0, "comparisonCounter", "NE"),
        translator.translateMessage("compareNaive",
            String.valueOf(naiveComparisons)), "naiveComparisons", null);
  }
}
