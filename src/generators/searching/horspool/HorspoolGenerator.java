package generators.searching.horspool;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
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
import algoanim.util.Offset;

public class HorspoolGenerator implements Generator {

  private Language              lang;
  private ArrayProperties       arrayProperties;
  private ArrayMarkerProperties arrayMarkerProperties;
  private RectProperties        rectProperties;
  private String[]              patternArray;
  private String[]              textArray;
  private int                   highPosPattern   = -1;
  private int                   indexText        = -1;
  private int                   indexPattern     = -1;
  private int                   shiftCounter     = -1;
  private int                   indexStringFound = -1;
  private int                   countComp        = -1;
  private Variables             v;

  public HorspoolGenerator() {
  }

  public void init() {
    lang = new AnimalScript("Horspool [DE]", "Thu Huong Luu, Benedikt Hiemenz",
        800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    // create language
    init();

    // data created by user
    arrayProperties = (ArrayProperties) props
        .getPropertiesByName("arrayProperties");
    arrayMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarkerProperties");
    rectProperties = (RectProperties) props
        .getPropertiesByName("rectProperties");
    patternArray = (String[]) primitives.get("patternArray");
    textArray = (String[]) primitives.get("textArray");

    // start algorithm
    v = lang.newVariables();
    buildPages(textArray, patternArray);
    lang.finalizeGeneration();
    return lang.toString();
  }

  /*
   * ********************************
   * ********** ALGORITHM *******************************************
   */

  /**
   * start Algorithm - build basic pages
   * 
   * @param textData
   * @param patternData
   */

  private void buildPages(String[] textData, String[] patternData) {

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    /*
     * create properties for components
     */
    TextProperties propsHeader = new TextProperties();
    propsHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 25));

    TextProperties propsHeaderSection = new TextProperties();
    propsHeaderSection.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 15));

    SourceCodeProperties propsSourceCode = new SourceCodeProperties();
    propsSourceCode.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    propsSourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    propsSourceCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    propsSourceCode.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    /*
     * ******************************
     * ** page one - introduction *********************************
     */

    // create the header with a heading surrounded by a rectangle
    Text header = lang.newText(new Coordinates(20, 30),
        "Algorithmus von Horspool", "header", null, propsHeader);
    Rect hRect = lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5,
        5, "header", "SE"), "hRect", null, rectProperties);

    Text headerSection = lang.newText(new Offset(0, 10, "header",
        AnimalScript.DIRECTION_SW), "Einleitung (der Algorithmus in Worten):",
        "headerSection", null, propsHeaderSection);

    // create introduction text
    SourceCode introduction = lang.newSourceCode(new Offset(0, 10,
        "headerSection", AnimalScript.DIRECTION_SW), "introduction", null,
        new SourceCodeProperties());

    // set code lines for introText: line, name, indentation, display, delay
    introduction.addCodeLine(
        "Der Algorithmus von Horspool dient zur Suche eines bestimmten", null,
        0, null);
    introduction
        .addCodeLine(
            "Musters (Patterns) in einem Text. In dieser Animation gibt er an, an welcher",
            null, 0, null);
    introduction.addCodeLine(
        "Position im Text das gesuchte Muster als erstes auftaucht.", null, 0,
        null);
    introduction.addCodeLine("Der Algorithmus durchsucht den Text wie folgt:",
        null, 0, null);
    introduction.addCodeLine("", null, 0, null);
    introduction
        .addCodeLine(
            "Das Muster wird zeichenweise von rechts nach links mit dem Text verglichen.",
            null, 0, null);
    introduction
        .addCodeLine(
            "Ist der Vergleich erfolgreich, wird zum naechsten Zeichen links gegangen, bis ",
            null, 0, null);
    introduction.addCodeLine("das Muster vollstaendig gefunden wurde.", null,
        0, null);
    introduction
        .addCodeLine(
            "Ist der Vergleich nicht erfolgreich, wird das Muster nach rechts verschoben.",
            null, 0, null);
    introduction
        .addCodeLine(
            "Die verschiebe Distanz ist dabei abhaengig von dem Zeichen im Text, welches",
            null, 0, null);
    introduction.addCodeLine(
        "aktuell an der hoechsten Position im Muster steht.", null, 0, null);
    introduction
        .addCodeLine(
            "Kommt dieses Zeichen gar nicht mehr im Muster vor, verschiebt sich das Muster",
            null, 0, null);
    introduction.addCodeLine("um die eigene Laenge.", null, 0, null);
    introduction
        .addCodeLine(
            "Die verschiebe Distanz wird mit Hilfe eines CharShiftTable berechnet, welche das Muster einliest",
            null, 0, null);
    introduction.addCodeLine("und fuer jedes Zeichen die Distanz bestimmt.",
        null, 0, null);
    introduction.addCodeLine("", null, 0, null);
    introduction.addCodeLine(
        "Die CharShiftTable besteht aus einem 256 Felder großen int Array,",
        null, 0, null);
    introduction.addCodeLine(
        "der Index steht fuer das entsprechende Zeichen im ASCII Code.", null,
        0, null);
    introduction.addCodeLine(
        "Bei jedem Zeichen, welches nicht im Pattern vorkommt, ist der", null,
        0, null);
    introduction.addCodeLine(
        "Eintrag am entsprechenden Index die Laenge des Pattern (komplette",
        null, 0, null);
    introduction.addCodeLine(
        "Verschiebung), sonst die jeweils notwendige Verschiebung.", null, 0,
        null);

    // next step
    lang.nextStep();
    lang.hideAllPrimitives();

    /*
     * ********************************************
     * ** page two - algorithm & charshift code ***
     * ********************************************
     */

    header.show();
    hRect.show();

    // set new text to headerSection
    headerSection.setText("Horspool-Algorithmus", null, null);
    headerSection.show();

    // next step description
    Text description = lang
        .newText(
            new Offset(10, 20, "headerSection", null),
            "Zuerst wird die Verschiebung bestimmt (es werden nur die wichtigen Felder des Charshift angezeigt)",
            "description", null);

    // create StringArrays & labels for text and pattern
    Text textLabel = lang.newText(new Coordinates(90, 150), "Text",
        "textLabel", null);
    Text patternLabel = lang.newText(new Coordinates(90, 185), "Pattern",
        "patternLabel", null);
    StringArray text = lang.newStringArray(new Coordinates(170, 150), textData,
        "text", null, arrayProperties);
    StringArray pattern = lang.newStringArray(new Coordinates(170, 185),
        patternData, "pattern", null, arrayProperties);

    /*
     * section for variables
     */

    // create label
    lang.newText(new Coordinates(5, 220), "Variable", "labelVariable", null,
        propsHeaderSection);

    // create fields
    SourceCode variable = lang.newSourceCode(new Offset(0, 90, "labelVariable",
        null), "variable", null, new SourceCodeProperties());
    variable.addCodeLine("highPosPattern =", null, 1, null);
    variable.addCodeLine("indexText      =", null, 1, null);

    SourceCode variable2 = lang.newSourceCode(new Offset(200, 90,
        "labelVariable", null), "variable2", null, new SourceCodeProperties());
    variable2.addCodeLine("indexPattern  =", null, 1, null);
    variable2.addCodeLine("shiftCounter  =", null, 1, null);

    SourceCode variable3 = lang.newSourceCode(new Offset(400, 90,
        "labelVariable", null), "variable3", null, new SourceCodeProperties());
    variable3.addCodeLine("indexStringFound = ", null, 1, null);
    variable3.addCodeLine("countComparison = ", null, 1, null);

    Text hPP = lang.newText(new Offset(120, 00, "variable", null), ""
        + highPosPattern, "hPP", null);
    Text inT = lang.newText(new Offset(120, 19, "variable", null), ""
        + indexText, "inT", null);
    Text inP = lang.newText(new Offset(110, 00, "variable2", null), ""
        + indexPattern, "inP", null);
    Text shC = lang.newText(new Offset(110, 19, "variable2", null), ""
        + shiftCounter, "shC", null);
    Text iSF = lang.newText(new Offset(130, 00, "variable3", null), ""
        + indexStringFound, "iSF", null);
    Text coC = lang.newText(new Offset(130, 19, "variable3", null), ""
        + countComp, "coC", null);

    /*
     * section for charshift array
     */

    // convert String[] of pattern to String
    String patternString = "";
    for (int i = 0; i < patternData.length; i++)
      patternString += patternData[i];

    // create charShiftTable for bad character heuristics
    int[] charshift = createCharShiftTable(patternString);

    // create smaller arrays with important values only (better for readability)
    ArrayList<Integer> asciiPatternArray = new ArrayList<Integer>();
    ArrayList<Integer> shiftPatternArray = new ArrayList<Integer>();

    for (int i = 0; i < charshift.length; i++) {

      for (int j = 0; j < patternString.length(); j++) {
        if (i != patternString.charAt(j))
          continue;
        if (!asciiPatternArray.contains(i)) {
          asciiPatternArray.add(i);
          shiftPatternArray.add(charshift[i]);
        }
      }
    }

    // convert to int[]
    int[] asciiPattern = new int[asciiPatternArray.size()];
    int[] shiftPattern = new int[shiftPatternArray.size()];

    for (int i = 0; i < asciiPatternArray.size(); i++)
      asciiPattern[i] = (int) asciiPatternArray.get(i);

    // show variables "letter <-> ASCII"
    Variables v = lang.newVariables();

    for (int i = 0; i < asciiPatternArray.size(); i++)
      v.declare("int", "" + (char) asciiPattern[i], "" + asciiPattern[i]);

    // create IntArrays & labels for shift and ascii
    Text shiftText = lang.newText(new Offset(15, 35, "labelVariable", null),
        "Charshift:", "shiftText", null);
    Text asciiText = lang.newText(new Offset(15, 60, "labelVariable", null),
        "Index:", "asciiText", null);
    lang.newText(new Offset(25, 20, "asciiText", null),
        "<---256 Felder groß (ASCII Code)--->", "helpLabel", null);

    IntArray shiftArray = lang.newIntArray(new Coordinates(110, 250),
        shiftPattern, "shiftArray", null, arrayProperties);
    lang.newIntArray(new Coordinates(110, 275), asciiPattern, "asciiArray",
        null, arrayProperties);

    /*
     * section for charshift-code
     */

    Text codeHeader = lang.newText(new Coordinates(5, 370),
        "Java-Code Charshift", "codeHeader", null, propsHeaderSection);

    // code section for charshift
    SourceCode charshiftCode = lang.newSourceCode(new Offset(10, 10,
        "codeHeader", null), "charshiftCode", null, propsSourceCode);
    charshiftCode.addCodeLine(
        "private int[] createCharShiftTable (String pattern) {", null, 1, null);
    charshiftCode.addCodeLine("int[] charshift = new int[256];", null, 2, null);
    charshiftCode.addCodeLine("int len = pattern.length();", null, 2, null);
    charshiftCode.addCodeLine("", null, 1, null);
    charshiftCode.addCodeLine(
        "// belege alle Felder von charshift mit der Länge des Wortes", null,
        2, null);
    charshiftCode.addCodeLine("for (int i = 0; i < charshift.length; i++) {",
        null, 2, null);
    charshiftCode.addCodeLine("charshift[i] = len;", null, 3, null);
    charshiftCode.addCodeLine("}", null, 2, null);
    charshiftCode.addCodeLine("", null, 1, null);
    charshiftCode
        .addCodeLine("// bestimme das Verschiebemuster", null, 2, null);
    charshiftCode.addCodeLine("for (int i = 0; i < len - 1; i++) {", null, 2,
        null);
    charshiftCode.addCodeLine("charshift[pattern.charAt(i)] = len - i - 1;",
        null, 3, null);
    charshiftCode.addCodeLine("}", null, 2, null);
    charshiftCode.addCodeLine("return charshift;", null, 2, null);
    charshiftCode.addCodeLine("}", null, 1, null);

    lang.nextStep("Seite 2 - Code Charshift");

    shiftText.changeColor("color", Color.red, null, null);
    charshiftCode.highlight(1);
    lang.nextStep();
    charshiftCode.unhighlight(1);
    lang.nextStep();

    // start algorithm for charshift
    try {
      runCharshiftAlgo(charshiftCode, patternString, shiftArray,
          shiftPatternArray);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    lang.nextStep();

    /*
     * ***********************************
     * *** page three - run algorithm ***************************************
     */

    shiftText.changeColor("color", Color.black, null, null);
    asciiText.changeColor("color", Color.black, null, null);
    charshiftCode.hide();
    description.setText("Nun folgt der Horspool-Algorithmus", null, null);

    // source code for algorithm
    codeHeader.setText("Pseudo-Code Horspool Algorithmus:", null, null);

    // finally create source code: coordinates, name, display options, default
    // properties
    SourceCode init = lang.newSourceCode(
        new Offset(10, 10, "codeHeader", null), "init", null, propsSourceCode);
    SourceCode code = lang.newSourceCode(
        new Offset(320, -5, "codeHeader", null), "code", null, propsSourceCode);

    // set lines: line, name, indentation, display, delay
    init.addCodeLine("// 1) INITIALIZE", null, 1, null);
    init.addCodeLine("String[ ] text;", null, 1, null);
    init.addCodeLine("String[ ] pattern;", null, 1, null);
    init.addCodeLine("", null, 1, null);
    init.addCodeLine("int highPosPattern", null, 1, null);
    init.addCodeLine("= pattern.getLength() - 1;", null, 3, null);
    init.addCodeLine("int indexText", null, 1, null);
    init.addCodeLine("= highPosPattern;", null, 3, null);
    init.addCodeLine("int indexPattern", null, 1, null);
    init.addCodeLine("	= highPosPattern;", null, 3, null);
    init.addCodeLine("int indexStringFound = -1;", null, 1, null);
    init.addCodeLine("int shiftCounter = 0;", null, 1, null);

    code.addCodeLine("// 2) START ALGORITHM", null, 1, null);
    code.addCodeLine("while (indexText < text.getLength()) {", null, 1, null);
    code.addCodeLine("if (text[indexText] == pattern[indexPattern]) {", null,
        2, null);
    code.addCodeLine("if (indexPattern == 0)	 {", null, 3, null);
    code.addCodeLine("indexStringFound = indexText; break;", null, 4, null);
    code.addCodeLine("} else {", null, 3, null);
    code.addCodeLine("indexText--; indexPattern --;", null, 4, null);
    code.addCodeLine("}", null, 3, null);
    code.addCodeLine("} else {", null, 2, null);
    code.addCodeLine("shiftCounter++;", null, 3, null);
    code.addCodeLine("String lastChar = text[indexText", null, 3, null);
    code.addCodeLine("+ highPosPattern - indexPattern]; ", null, 5, null);
    code.addCodeLine("indexText = indexText + highPosPattern ", null, 3, null);
    code.addCodeLine("- indexPattern + charshift[lastChar.charAt(0)];", null,
        5, null);
    code.addCodeLine("indexPattern = highPosPattern;", null, 3, null);
    code.addCodeLine("}", null, 2, null);
    code.addCodeLine("}", null, 1, null);

    textLabel.changeColor("color", Color.red, null, null);
    patternLabel.changeColor("color", Color.red, null, null);
    lang.nextStep("Seite 3 - Code Horspool");

    // start algorithm
    int[] result = { -1, 0 };
    try {
      // result = {indexStringFound, shiftCounter}
      result = runAlgo(text, pattern, code, charshift, hPP, inT, inP, shC, iSF,
          coC);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }

    String resultString = "Animation beendet, Muster gefunden an Position: "
        + indexStringFound;

    description.setText(resultString, null, null);

    lang.nextStep();
    lang.hideAllPrimitives();

    // *********** end page ***********

    header.show();
    hRect.show();

    if (result[0] == -1) {
      headerSection.setText("Muster wurde nicht gefunden!", null, null);
    } else {
      headerSection.setText("Muster wurde gefunden!", null, null);
    }
    headerSection.show();

    String finished = "Das Muster wurde insgesamt " + result[1]
        + " mal verschoben. Es wurden " + result[2] + " Vergleiche gemacht.";
    description.setText(finished, null, null);
    description.show();

    SourceCode complexity = lang.newSourceCode(new Offset(0, 20, "description",
        null), "complexity", null, propsSourceCode);
    complexity.addCodeLine(
        "Die Komplexitaet des Algorithmus ist unterschiedlich, je nachdem,",
        null, 1, null);
    complexity.addCodeLine(
        "wie viele positive bzw. negative Vergleiche es gab. Im besten Fall",
        null, 1, null);
    complexity.addCodeLine(
        "(nur negative Vergleiche im Sinne von der Buchstabe kommt im ", null,
        1, null);
    complexity.addCodeLine(
        "Pattern ueberhaupt nicht vor) liegt die Laufzeit bei O(n/m).   ",
        null, 1, null);
    complexity.addCodeLine(
        "Im theoreitsch schlimmsten Fall liegt sie bei O(n*m), was einer",
        null, 1, null);
    complexity.addCodeLine("Brute-Force-Suche gleichkommt.", null, 1, null);
  }

  /**
   * run Horspool-Algorithm
   * 
   * @param text
   * @param pattern
   * @param code
   * @return indexStringFound, shiftCounter
   */

  private int[] runAlgo(StringArray text, StringArray pattern, SourceCode code,
      int[] charshift, Text hPP, Text inT, Text inP, Text shC, Text iSF,
      Text coC) {

    // create array marker: array, current index, name, display options,
    // properties
    ArrayMarker markerText = lang.newArrayMarker(text, 0, "arrayMarker1", null,
        arrayMarkerProperties);
    ArrayMarker markerPattern = lang.newArrayMarker(pattern, 0, "arrayMarker2",
        null, arrayMarkerProperties);

    // show variables
    v.declare("int", "highPosPattern", "" + highPosPattern);
    v.declare("int", "indexText", "" + indexText);
    v.declare("int", "indexPattern", "" + indexPattern);
    v.declare("int", "indexStringFound", "" + indexStringFound);
    v.declare("int", "shiftCounter", "" + shiftCounter);
    v.declare("int", "countComp", "" + countComp);

    // initialize fields
    highPosPattern = pattern.getLength() - 1;
    indexText = highPosPattern;
    indexPattern = highPosPattern;
    indexStringFound = -1;
    shiftCounter = 0;
    countComp = 0;

    // set variables
    v.set("highPosPattern", "" + highPosPattern);
    v.set("indexText", "" + indexText);
    v.set("indexPattern", "" + indexPattern);
    v.set("indexStringFound", "" + indexStringFound);
    v.set("shiftCounter", "" + shiftCounter);
    v.set("countComp", "" + countComp);

    int cache = 0;

    hPP.setText("" + highPosPattern, null, null);
    inT.setText("" + indexText, null, null);
    inP.setText("" + indexPattern, null, null);
    iSF.setText("" + indexStringFound, null, null);
    shC.setText("" + shiftCounter, null, null);
    coC.setText("" + countComp, null, null);

    markerText.move(indexText, null, null);
    markerPattern.move(indexPattern, null, null);

    // text and pattern must be set and pattern.length < text.length
    if (text.getLength() == 0 || pattern.getLength() == 0
        || pattern.getLength() > text.getLength())
      return new int[] { -1, 0 };

    code.highlight(0);
    code.highlight(1);

    lang.nextStep("Starte Horspool Algorithmus");
    code.highlight(2);

    // run algorithm
    while (indexText < text.getLength()) {

      countComp++;
      v.set("countComp", "" + countComp);
      coC.setText("" + countComp, null, null);

      // if comparison positive...
      if (pattern.getData(indexPattern).equals(text.getData(indexText))) {
        text.highlightElem(indexText, null, null);
        pattern.highlightElem(indexPattern, null, null);
        code.unhighlight(2);
        code.highlight(3);

        lang.nextStep();

        if (indexPattern == 0) {

          // highlighting code for next step
          code.unhighlight(3);
          code.highlight(4);

          lang.nextStep();

          indexStringFound = indexText;
          // set variables
          v.set("indexStringFound", "" + indexStringFound);
          iSF.setText("" + indexStringFound, null, null);

          lang.nextStep();

          // pattern found, algorithm finished
          code.unhighlight(4);
          code.unhighlight(0);
          code.unhighlight(1);
          markerPattern.hide();
          markerText.hide();
          break;

        } else {

          // highlighting code for next step
          code.unhighlight(3);
          code.highlight(5);
          code.highlight(6);

          lang.nextStep();

          // go to next symbol
          indexText--;
          indexPattern--;
          // set variables
          v.set("indexText", "" + indexText);
          v.set("indexPattern", "" + indexPattern);
          inT.setText("" + indexText, null, null);
          inP.setText("" + indexPattern, null, null);

          lang.nextStep();

          markerText.move(indexText, null, null);
          markerPattern.move(indexPattern, null, null);

          // highlighting code for next step
          code.unhighlight(5);
          code.unhighlight(6);
          code.highlight(2);

          lang.nextStep();
        }

      } else {
        text.highlightCell(indexText, null, null);
        pattern.highlightCell(indexPattern, null, null);
        code.unhighlight(2);
        code.highlight(8);

        lang.nextStep();

        // highlighting code for next step
        code.unhighlight(8);
        code.highlight(9);

        lang.nextStep();

        shiftCounter++;
        // set variables
        v.set("shiftCounter", "" + shiftCounter);
        shC.setText("" + shiftCounter, null, null);

        lang.nextStep();

        // highlighting code for next step
        code.unhighlight(9);
        code.highlight(10);
        code.highlight(11);
        code.highlight(12);
        code.highlight(13);
        code.highlight(14);

        lang.nextStep();

        String lastChar = text.getData(indexText + highPosPattern
            - indexPattern);
        indexText = indexText + highPosPattern - indexPattern
            + charshift[lastChar.charAt(0)];
        cache = indexPattern;
        indexPattern = highPosPattern;

        /*
         * question start
         */

        FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
            "fillInBlanksQuestion" + UUID.randomUUID());
        fibq.setPrompt("Wie lautet der neue Text-Index?");
        fibq.addAnswer("" + indexText, 10, "Ja, absolut richtig ;-)");
        fibq.setGroupID("First question group");
        lang.addFIBQuestion(fibq);

        /*
         * question end
         */

        lang.nextStep();

        // set variables
        v.set("indexText", "" + indexText);
        v.set("indexPattern", "" + indexPattern);
        inT.setText("" + indexText, null, null);
        inP.setText("" + indexPattern, null, null);

        lang.nextStep();

        text.unhighlightElem(0, text.getLength() - 1, null, null);
        pattern.unhighlightElem(0, pattern.getLength() - 1, null, null);

        text.highlightCell(0, indexText - highPosPattern - 1, null, null);
        pattern.unhighlightCell(cache, null, null);

        if (indexText < text.getLength())
          markerText.move(indexText, null, null);
        markerPattern.move(indexPattern, null, null);

        // highlighting code for next step
        code.unhighlight(10);
        code.unhighlight(11);
        code.unhighlight(12);
        code.unhighlight(13);
        code.unhighlight(14);
        code.highlight(2);

        lang.nextStep();
      }
    }
    return new int[] { indexStringFound, shiftCounter, countComp };
  }

  /**
   * create charShiftTable
   */
  private int[] createCharShiftTable(String pattern) {

    int[] charShift = new int[256];
    int length = pattern.length();

    // fill all fields of charShift with "length"
    for (int i = 0; i < charShift.length; i++)
      charShift[i] = length;

    // calculate distance for all symbols of "patternString"
    for (int i = 0; i < length - 1; i++)
      charShift[pattern.charAt(i)] = length - i - 1;

    return charShift;
  }

  private void runCharshiftAlgo(SourceCode charshiftCode, String patternString,
      IntArray shiftArray, ArrayList<Integer> shiftPatternArray) {

    ArrayMarker markerArray = lang.newArrayMarker(shiftArray, 0, "markerArray",
        null, arrayMarkerProperties);
    charshiftCode.highlight(0);
    lang.nextStep("Starte Charshift Berechnung");

    // first loop -> fill with length
    charshiftCode.highlight(5);
    charshiftCode.highlight(6);
    charshiftCode.highlight(7);
    lang.nextStep();

    for (int i = 0; i < shiftArray.getLength(); i++) {
      shiftArray.put(i, patternString.length(), null, null);
      markerArray.move(i, null, null);
      lang.nextStep();
    }

    charshiftCode.unhighlight(5);
    charshiftCode.unhighlight(6);
    charshiftCode.unhighlight(7);
    charshiftCode.highlight(10);
    charshiftCode.highlight(11);
    charshiftCode.highlight(12);
    lang.nextStep();

    // second loop -> caculate shift
    for (int i = 0; i < shiftPatternArray.size(); i++) {
      shiftArray.put(i, (int) shiftPatternArray.get(i), null, null);
      markerArray.move(i, null, null);
      lang.nextStep();
    }
    markerArray.hide();

  }

  // ////////////////////////////////////
  //
  // ////////////////////////////////////

  public String getName() {
    return "Horspool [DE]";
  }

  public String getAlgorithmName() {
    return "Horspool";
  }

  public String getAnimationAuthor() {
    return "Thu Huong Luu, Benedikt Hiemenz";
  }

  public String getDescription() {
    return "Der Algorithmus von Horspool dient zur Suche eines bestimmten Musters (Patterns)  <br>"
        + "\n"
        + "in einem Text. In dieser Animation gibt er an, an welcher Position im Text das gesuchte <br>"
        + "\n"
        + "Muster als erstes auftaucht. Der Algorithmus durchsucht den Text wie folgt: <br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Das Muster wird zeichenweise von rechts nach links mit dem Text verglichen.<br>"
        + "\n"
        + "Ist der Vergleich erfolgreich, wird zum naechsten Zeichen links gegangen, <br>"
        + "\n"
        + "bis das Muster vollstaendig gefunden wurde.<br>"
        + "\n"
        + "Ist der Vergleich nicht erfolgreich, wird das Muster nach rechts verschoben. <br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Die verschiebe Distanz ist dabei abhaengig von dem Zeichen im Text, welches <br>"
        + "\n"
        + "aktuell an der hoechsten Position im Muster steht. Kommt dieses Zeichen gar <br>"
        + "\n"
        + "nicht mehr im Muster vor, verschiebt sich das Muster um die eigene Laenge. <br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Die verschiebe Distanz wird mit Hilfe eines CharShiftTable berechnet, <br>"
        + "\n"
        + "welche das Muster einliest und fuer jedes Zeichen die Distanz bestimmt. <br>"
        + "\n"
        + "Die CharShiftTable besteht aus einem 256 Felder großen int Array,<br>"
        + "\n"
        + "der Index steht fuer das entsprechende Zeichen im ASCII Code. <br>"
        + "\n"
        + "Bei jedem Zeichen, welches nicht im Pattern vorkommt, ist der <br>"
        + "\n"
        + "Eintrag am entsprechenden Index die Laenge des Pattern<br>"
        + "\n"
        + "(komplette Verschiebung), sonst die jeweils notwendige Verschiebung.<br>";
  }

  public String getCodeExample() {
    return "	private int runAlgo(String[] text, String[] pattern) {"
        + "\n"
        + "	"
        + "\n"
        + " 	    // initialize fields		"
        + "\n"
        + "		int highPosPattern = pattern.length - 1;"
        + "\n"
        + "		int indexText = highPosPattern;"
        + "\n"
        + "		int indexPattern = highPosPattern;"
        + "\n"
        + "		"
        + "\n"
        + "	    int indexStringFound = -1;"
        + "\n"
        + "	    int shiftCounter = 0;"
        + "\n"
        + "		"
        + "\n"
        + "		// text and pattern must be set"
        + "\n"
        + "    	if (text.length == 0 || pattern.length == 0 || pattern.length > text.length)"
        + "\n"
        + "    		 return -1; "
        + "\n"
        + "    	 "
        + "\n"
        + "    	// create searchString from pattern array"
        + "\n"
        + "	    String searchString = \"\";"
        + "\n"
        + "	    for(int i = 0; i < pattern.length; i++) "
        + "\n"
        + "	    		searchString += pattern[i];"
        + "\n"
        + "	    		"
        + "\n"
        + "	    // create charShiftTable for bad character heuristics"
        + "\n"
        + "	    int[] charshift = createCharShiftTable(searchString);"
        + "\n"
        + "\n"
        + " 	    // run algorithm"
        + "\n"
        + "		while (indexText < text.length) { "
        + "\n"
        + "			  "
        + "\n"
        + "			// if comparison positive..."
        + "\n"
        + "			if (pattern[indexPattern] == text[indexText]) {   "
        + "\n"
        + "				"
        + "\n"
        + "				// if pattern completely found..."
        + "\n"
        + "				if (indexPattern == 0) { 				"
        + "\n"
        + "					"
        + "\n"
        + "					indexStringFound = indexText;"
        + "\n"
        + "					break;  "
        + "\n"
        + "				// ...else go to next symbol"
        + "\n"
        + "				} else {				"
        + "\n"
        + "					indexText--;"
        + "\n"
        + "					indexPattern--;"
        + "\n"
        + "				}"
        + "\n"
        + "			// ...else comparison is negative"
        + "\n"
        + "			} else {	"
        + "\n"
        + "				shiftCounter++;"
        + "\n"
        + "				// set new index to shift pattern to next valid position"
        + "\n"
        + "				String lastChar = text[indexText + highPosPattern - indexPattern];"
        + "\n"
        + "				indexText = indexText + highPosPattern - indexPattern + charshift[lastChar.charAt(0)];         "
        + "\n" + "				indexPattern = highPosPattern;" + "\n" + "			}" + "\n"
        + "		}" + "\n" + "		return indexStringFound;" + "\n" + "	}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}