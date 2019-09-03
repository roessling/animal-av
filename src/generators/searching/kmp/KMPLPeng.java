package generators.searching.kmp;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

/**
 * @author Leqiao Peng <peng.leqiao@googlemail.com>
 * @version 1.0 2010-04-24
 * 
 */
public class KMPLPeng implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private static Language              lang;
  private static ArrayProperties       arrayProps;
  private static TextProperties        titleProps;
  private RectProperties               rectProps;
  private SourceCodeProperties         scPropsMain;
  private SourceCodeProperties         scPropsFunc;
  private static SourceCode            scMain;
  private static SourceCode            scFunc;
  private static TextProperties        hintProps;
  private static Text                  subTitle;
  private static MsTiming              durationTiming;
  private static ArrayMarkerProperties arrayIMProps;
  private ArrayMarkerProperties        arrayRMProps;
  // private static StringArray patternStringArray2;
  private static IntArray              patternFailFunc;
  private static ArrayProperties       intArrayProps;
  private static ArrayMarkerProperties arrayJMProps;
  private static StringArray           textStringArray;
  private static StringArray           patternStringArray;
  private static ArrayMarker           iMarker;
  private static ArrayMarker           jMarker;
  private static ArrayMarker           IndexMarker;
  private static ArrayMarkerProperties arrayIndexMProps;
  private static ArrayMarker           j2Marker;

  public KMPLPeng() {
  }

  public void init() {

    lang = new AnimalScript(
        "The Knuth-Morris-Pratt Algorithm in Pattern Matching", "Leqiao Peng",
        800, 600);
    lang.setStepMode(true);

    // first, set the visual properties (text, etc.)
    titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);

    hintProps = new TextProperties();
    hintProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Sans",
        Font.BOLD, 16));
    hintProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    // then, set the visual properties for array
    arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GRAY);

    intArrayProps = new ArrayProperties();
    intArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    intArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    intArrayProps
        .set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);

    // now, set the visual properties for the source code
    scPropsMain = new SourceCodeProperties();
    scPropsMain.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scPropsMain.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));

    scPropsMain.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scPropsMain.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    scPropsMain.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    scPropsFunc = new SourceCodeProperties();
    scPropsFunc.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scPropsFunc.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    scPropsFunc
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    scPropsFunc.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    scPropsFunc.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    // set up the properties of arrayMarkers
    arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i=0");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);

    arrayRMProps = new ArrayMarkerProperties();
    arrayRMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "R");
    arrayRMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    arrayJMProps = new ArrayMarkerProperties();
    arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j=0");
    arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);

    arrayIndexMProps = new ArrayMarkerProperties();
    arrayIndexMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Index");
    arrayIndexMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);

    // at last, timing effect
    durationTiming = new MsTiming(500);

  }

  private static final String DESCRIPTION = "In typical string matching problem, a Text (T with length of n) and a Pattern (P with length of m) are given. \n"
                                              + "We need to find whether P is a substring of T. If yes, return the first position index in T.\n"
                                              + "Two algorithms like Brute Force(BF), Boyer-Moore(BM) methods are already in Animal-x.jar visualized.\n"
                                              + "The Knuth-Morris-Pratt(KMP) algorithm will be introduced, which steadily achieves running time of O(n+m).\n"
                                              + "Especially, in comparision under the worst case, either BF or BM has a worse running time O(nm).";

  private static final String SOURCE_CODE = "Only Introduction is here. Java Code comes later." // 0
                                              + "\n The KMP consists of two phases. "
                                              + "\n 1). Preposessing of the Pattern String. "
                                              + "\n The output is a failure function which encodes repeated substrings inside the pattern itself"
                                              + "\n 2). Searching moves the P along T from left to right."
                                              + "\n If any mismatch occurs, the P gets shifted properly according to the failure function in order to reuse previously performed comparisons.";

  public void KMPmatching(String[] text, String[] pattern) { // here string
                                                             // array as
                                                             // parameter, not
                                                             // string.

    lang.newText(new Coordinates(20, 20), "The Knuth-Morris-Pratt Algorithm",
        "title", null, titleProps);
    lang.newRect(new Offset(-3, -3, "title", AnimalScript.DIRECTION_NW),
        new Offset(3, 3, "title", AnimalScript.DIRECTION_SE), "titleRect",
        null, rectProps);

    subTitle = lang.newText(new Offset(0, 10, "titleRect",
        AnimalScript.DIRECTION_BASELINE_START),
        " Exact String Pattern Matching in given Text Body", "subTitle", null,
        hintProps);
    // now, create the IntArray object, linked to the properties
    lang.newText(new Offset(0, 100, "subTitle",
        AnimalScript.DIRECTION_BASELINE_START), "Text", "", null, hintProps);
    textStringArray = lang.newStringArray(new Offset(80, 100, "subTitle",
        AnimalScript.DIRECTION_BASELINE_START), text, "textString", null,
        arrayProps);
    lang.newText(new Offset(0, 200, "subTitle",
        AnimalScript.DIRECTION_BASELINE_START), "Pattern", "", null, hintProps);
    patternStringArray = lang.newStringArray(new Offset(80, 200, "subTitle",
        AnimalScript.DIRECTION_BASELINE_START), pattern, "patternString", null,
        arrayProps);

    scMain = lang.newSourceCode(new Offset(0, 50, "patternString",
        AnimalScript.DIRECTION_SW), "MainSC", null, scPropsMain);
    scFunc = lang.newSourceCode(new Offset(520, 50, "patternString",
        AnimalScript.DIRECTION_SW), "FuncSC", null, scPropsFunc);

    // patternStringArray2 = lang.newStringArray(new Offset(280, 200,
    // "patternString", AnimalScript.DIRECTION_BASELINE_START), pattern,
    // "patternString2", null, arrayProps);

    lang.nextStep();

    // now, create the source code entities for both main() and function()
    scMain.addCodeLine(
        "public static int KMPmatch(String text, String pattern) {", null, 0,
        null);
    scMain.addCodeLine("int n = text.length();", null, 1, null);
    scMain.addCodeLine("int m = pattern.length();", null, 1, null);
    scMain.addCodeLine("int[] fail = computeFailFunction(pattern);", null, 1,
        null);
    scMain.addCodeLine("int i = 0; int j = 0", null, 1, null);
    scMain.addCodeLine("while (i < n) {", null, 1, null);
    scMain.addCodeLine("if (pattern.charAt(j) == text.charAt(i)) {", null, 2,
        null);
    scMain.addCodeLine("if (j == m - 1)", null, 3, null);
    scMain.addCodeLine("return i - m + 1; // match", null, 4, null);
    scMain.addCodeLine("i++;", null, 3, null);
    scMain.addCodeLine("j++;", null, 3, null);
    scMain.addCodeLine("}", null, 2, null);
    scMain.addCodeLine("else if (j > 0)", null, 2, null);
    scMain.addCodeLine(
        "j = fail[j - 1]; // Check value --------------------------> ", null,
        3, null);
    scMain.addCodeLine("else i++;", null, 2, null);
    scMain.addCodeLine("}", null, 1, null);
    scMain.addCodeLine("return -1; // no match", null, 1, null);
    scMain.addCodeLine("}", null, 0, null);

    /*
     * scFunc.addCodeLine(
     * "public static int[] computeFailFunction(String pattern) {", null, 0,
     * null); scFunc.addCodeLine("int[] fail = new int[pattern.length()]; ",
     * null, 1, null); scFunc.addCodeLine("fail[0] = 0;", null, 1, null);
     * scFunc.addCodeLine("int m = pattern.length();", null, 1, null);
     * scFunc.addCodeLine("int i = 1; int j = 0", null, 1, null);
     * scFunc.addCodeLine("while (i < m) {", null, 1, null); scFunc.addCodeLine(
     * "if (pattern.charAt(j) == pattern.charAt(i)) { // j + 1 characters match"
     * , null, 2, null); scFunc.addCodeLine("fail[i] = j + 1;", null, 3, null);
     * scFunc.addCodeLine("i++;", null, 3, null); scFunc.addCodeLine("j++;",
     * null, 3, null); scFunc.addCodeLine("}", null, 2, null);
     * scFunc.addCodeLine("else if (j > 0)", null, 2, null);
     * scFunc.addCodeLine("j = fail[j - 1];  // j follows a matching prefix ",
     * null, 3, null); scFunc.addCodeLine("else {", null, 3, null);
     * scFunc.addCodeLine("fail[i] = 0; ", null, 4, null);
     * scFunc.addCodeLine("i++;", null, 4, null); scFunc.addCodeLine("}", null,
     * 3, null); scFunc.addCodeLine("}", null, 2, null);
     * scFunc.addCodeLine("return fail;", null, 1, null);
     * scFunc.addCodeLine("}", null, 0, null);
     */

    scFunc.addCodeLine("Pesuedo Code KMPFailFunction(String pattern) {", null,
        0, null);
    scFunc.addCodeLine("Initialize an array fail[] for the input Pattern;",
        null, 1, null);
    scFunc.addCodeLine("Let fail[0]= 0;", null, 1, null);
    scFunc.addCodeLine("For all other Index {", null, 1, null);
    scFunc.addCodeLine(" find the longest prefix of Pattern[0, Index-1]", null,
        2, null);
    scFunc.addCodeLine(" which is also a suffix of Pattern[1..Index];", null,
        2, null);
    scFunc.addCodeLine(" Update the length of prefix to fail[Index];", null, 2,
        null);
    scFunc.addCodeLine("}", null, 1, null);
    scFunc.addCodeLine("return fail;", null, 1, null);
    scFunc.addCodeLine("}", null, 0, null);

    // convert stringArray to string for the algorithms implementation below
    String textString;
    textString = text[0]; // start with the first element
    for (int i = 1; i < text.length; i++) {
      textString = textString + text[i];
    }
    String patternString;
    patternString = pattern[0]; // start with the first element
    for (int i = 1; i < pattern.length; i++) {
      patternString = patternString + pattern[i];
    }
    lang.nextStep();
    // Highlight all cells
    // ia.highlightCell(0, ia.getLength() - 1, null, null);
    try {
      // Start selection sort
      int result = KMPmatch(textString, patternString, scMain);
      lang.nextStep();
      if (result == -1) {
        scMain.unhighlight(16);
        subTitle.setText("No Pattern Found!", null, null);
      } else {
        scMain.unhighlight(7);
        scMain.unhighlight(8);
        subTitle.setText(
            "Pattern Found at Index R = " + Integer.toString(result) + ".",
            null, null);
        lang.newArrayMarker(textStringArray, result, "r", null, arrayRMProps);

      }
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }

  }

  public static int KMPmatch(String text, String pattern, SourceCode codeSupport) {
    codeSupport.highlight(0, 0, false);
    lang.nextStep();
    codeSupport.toggleHighlight(0, 0, false, 1, 0);
    codeSupport.highlight(2, 0, false);
    int n = text.length();
    int m = pattern.length();
    lang.nextStep();
    codeSupport.unhighlight(1);
    codeSupport.toggleHighlight(2, 0, true, 3, 0);
    subTitle.setText(
        "The prepossessing of Pattern string starts at right downside!", null,
        null);
    int[] fail = computeFailFunction(pattern, scFunc);
    lang.nextStep();
    codeSupport.highlight(4);
    int i = 0;
    int j = 0;
    iMarker = lang.newArrayMarker(textStringArray, 0, "i", null, arrayIMProps);
    jMarker = lang.newArrayMarker(patternStringArray, 0, "j", null,
        arrayJMProps);
    lang.nextStep();
    subTitle.setText("The search is running now ... ", null, null);
    codeSupport.toggleHighlight(4, 0, false, 5, 0);

    while (i < n) { // 5
      lang.nextStep();
      codeSupport.unhighlight(5);
      codeSupport.highlight(6, 0, false);
      codeSupport.unhighlight(9);
      codeSupport.unhighlight(10);
      patternStringArray.highlightElem(j, null, null);
      textStringArray.highlightElem(i, null, null);

      if (pattern.charAt(j) == text.charAt(i)) { // 6

        lang.nextStep();
        codeSupport.unhighlight(6);
        codeSupport.highlight(7, 0, false);
        codeSupport.highlight(8, 0, false);
        if (j == m - 1) {
          return i - m + 1; // match 8
        }
        lang.nextStep();
        codeSupport.toggleHighlight(7, 0, false, 9, 0);
        codeSupport.toggleHighlight(8, 0, false, 10, 0);
        i++; // 9
        j++; // 10
        lang.nextStep();
        iMarker.hide();
        jMarker.hide();
        arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, new String(
            "i=" + i));
        arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, new String(
            "j=" + j));
        iMarker = lang.newArrayMarker(textStringArray, i, "i", null,
            arrayIMProps);
        jMarker = lang.newArrayMarker(patternStringArray, j, "j", null,
            arrayJMProps);
        codeSupport.toggleHighlight(9, 0, false, 5, 0);
        codeSupport.unhighlight(10);
      } else {
        lang.nextStep();
        codeSupport.unhighlight(6);
        codeSupport.highlight(12);
        if (j > 0) { // 12
          lang.nextStep();
          codeSupport.toggleHighlight(12, 0, false, 13, 0);
          int oldj = j;
          arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, new String(
              "fail[j-1]"));
          j2Marker = lang.newArrayMarker(patternFailFunc, j - 1, "j-1", null,
              arrayJMProps);
          j = fail[j - 1]; // 13
          lang.nextStep();
          j2Marker.hide();
          int skip = oldj - j;
          jMarker.hide();
          arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, new String(
              "j=" + j));
          jMarker = lang.newArrayMarker(patternStringArray, j, "j", null,
              arrayJMProps);
          patternStringArray.moveBy("translate", 16 * skip, 0, null,
              durationTiming);
          patternStringArray.unhighlightElem(j, j + skip, null, null);
          patternStringArray.unhighlightElem(j, null, null);
          textStringArray.highlightCell(i - oldj, i - j - 1, null, null);
          textStringArray.unhighlightElem(i - oldj, i - j - 1, null, null);
          textStringArray.unhighlightElem(i, null, null);
          codeSupport.toggleHighlight(13, 0, false, 5, 0);
        } else {
          lang.nextStep();
          codeSupport.toggleHighlight(12, 0, false, 14, 0);
          i++; // 14
          lang.nextStep();
          iMarker.hide();
          arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, new String(
              "i=" + i));
          iMarker = lang.newArrayMarker(textStringArray, i, "i", null,
              arrayIMProps);
          patternStringArray.moveBy("translate", 16, 0, null, durationTiming);
          textStringArray.unhighlightElem(i - 1, i, null, null);
          textStringArray.highlightCell(i - 1, null, null);
          patternStringArray.unhighlightElem(0, null, null);
        }
      }
      codeSupport.toggleHighlight(14, 0, false, 5, 0);
    }
    codeSupport.toggleHighlight(5, 0, false, 16, 0);
    iMarker.moveOutside(null, durationTiming);
    lang.nextStep();
    return -1; // no match 16
  }

  public static int[] computeFailFunction(String pattern, SourceCode codeSupport) {
    codeSupport.highlight(0, 0, false);
    lang.nextStep();
    codeSupport.toggleHighlight(0, 0, false, 1, 0);
    codeSupport.highlight(2);
    int[] fail = new int[pattern.length()];
    lang.newText(new Offset(10, 55, "FuncSC",
        AnimalScript.DIRECTION_BASELINE_START), "FailFunction", "failfunc",
        null, hintProps);
    patternFailFunc = lang.newIntArray(new Offset(120, 55, "FuncSC",
        AnimalScript.DIRECTION_BASELINE_START), fail, "patternFailFunc", null,
        intArrayProps);
    // lang.nextStep();
    fail[0] = 0;
    int m = pattern.length();
    int j = 0;
    int i = 1;
    lang.nextStep();
    IndexMarker = lang.newArrayMarker(patternFailFunc, i, "Index", null,
        arrayIndexMProps);

    codeSupport.unhighlight(1);
    codeSupport.toggleHighlight(2, 0, false, 3, 0);

    String prefix, suffix;
    int oldi = 0;
    boolean updateI = true; // these only for animation
    while (i < m) { // 3
      suffix = pattern.substring(1, i + 1);
      IndexMarker.hide();
      arrayIndexMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, new String(
          "The current P[1..Index] is " + suffix));
      IndexMarker = lang.newArrayMarker(patternFailFunc, i, "Index", null,
          arrayIndexMProps);
      lang.nextStep();
      codeSupport.toggleHighlight(3, 0, false, 4, 0);
      codeSupport.highlight(5);
      if (pattern.charAt(j) == pattern.charAt(i)) { // j + 1 characters match
        fail[i] = j + 1;
        oldi = i;
        i++;
        j++;
      } else if (j > 0) {// j follows a matching prefix
        j = fail[j - 1];
        System.out.println("j = " + j);
        updateI = false;
      } else { // no match
        fail[i] = 0;
        oldi = i;
        i++;
      }

      if (updateI) {
        lang.nextStep();
        if (fail[i - 1] == 0)
          prefix = "Null";
        else {
          prefix = pattern.substring(0, fail[oldi]);
        }

        IndexMarker.hide();
        arrayIndexMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
            new String("The longest prefix is : " + prefix));
        IndexMarker = lang.newArrayMarker(patternFailFunc, oldi, "Index", null,
            arrayIndexMProps);

        lang.nextStep();
        codeSupport.unhighlight(4);
        codeSupport.unhighlight(5);
        codeSupport.highlight(6);
        lang.nextStep();
        patternFailFunc.put(oldi, fail[oldi], null, null);

        lang.nextStep();
        // IndexMarker.hide();
        codeSupport.unhighlight(4);
        codeSupport.toggleHighlight(6, 0, false, 3, 0);
      }
      updateI = true;
    } // 6

    lang.nextStep();
    IndexMarker.hide();
    codeSupport.toggleHighlight(3, 0, false, 8, 0);
    return fail; // 8
  }

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  public String getName() {
    return "KMP Matching";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    init();

    String[] text = (String[]) primitives.get("Text");
    String[] pattern = (String[]) primitives.get("Pattern");

    scPropsMain.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    scPropsMain.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
    scPropsMain.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, props.get(
        "sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));

    scPropsFunc.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    scPropsFunc.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));

    /*
     * String message= new String("ACTACATATACGACTACGCACCAGCATTACTACGTA");
     * String words[]=new String [message.length()]; for (int
     * idx=0;idx<message.length();idx++) { char c = message.charAt(idx);
     * words[idx] = Character.toString(c); }
     * 
     * String[] b = {"A","C","T","A", "C", "G", "T"};
     */

    KMPmatching(text, pattern);
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "KMP String Matching Algorithm";
  }

  @Override
  public String getAnimationAuthor() {
    return "Leqiao Peng";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }
}
