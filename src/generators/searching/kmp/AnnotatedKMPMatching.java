package generators.searching.kmp;

import generators.AnnotatedAlgorithm;
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
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
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
public class AnnotatedKMPMatching extends AnnotatedAlgorithm implements
    Generator {

  /**
   * The concrete language object used for creating output
   */

  private ArrayProperties       arrayProps;
  private TextProperties        titleProps;
  private RectProperties        rectProps;

  private SourceCodeProperties  scPropsMain;

  private TextProperties        hintProps;
  private Text                  subTitle;
  private MsTiming              durationTiming;
  private ArrayMarkerProperties arrayIMProps;
  private ArrayMarkerProperties arrayRMProps;

  private IntArray              patternFailFunc;
  private ArrayProperties       intArrayProps;
  private ArrayMarkerProperties arrayJMProps;
  private StringArray           textStringArray;
  private StringArray           patternStringArray;
  private ArrayMarker           iMarker;
  private ArrayMarker           jMarker;
  private ArrayMarker           IndexMarker;
  private ArrayMarkerProperties arrayIndexMProps;
  private ArrayMarker           j2Marker;

  private String[]              text;
  private String[]              pattern;

  // declare annotation variable
  private ArrayMarkerUpdater    amuI;
  private ArrayMarkerUpdater    amuJ;

  @Override
  public String getAnnotatedSrc() {
    return "The KMPMatching consists of two phases: Prepossessing and Searching. @label(\"intro\") \n"
        + "KMPMatching(String text, String pattern) { @label(\"entryMain\") \n"
        + "    Start with prepossessing ... @label(\"startPrepossessing\") \n"
        + "    int[] Fail = Prepossessing(String pattern) { @label(\"entryPrep\") \n"
        + "         Initialize array Fail[] for the input Pattern; @label(\"initialFail\") \n"
        + "         Let Fail[0] = 0; @label(\"setFail\") \n"
        + "         For all other Index { @label(\"loopInFail\") \n"
        + "             Find the longest prefix of Pattern[0, Index-1] which is also a suffix of Pattern[1..Index]; @label(\"findPrefix\") \n"
        + "             Update the length of prefix to Fail[index]; @label(\"updateFail\") \n"
        + "         } @label(\"loopInFailEnd\")\n"
        + "         return Fail[]; @label(\"returnFail\") \n"
        + "    }; 	@label(\"prepossessingEnd\") \n"
        + "    Now Searching ... @label(\"startSearching\") \n"
        + "    Initialize variables n = TextLength, m = PatternLength, i, j; @label(\"initVars\") @declare(\"int\", \"n\") @declare(\"int\", \"m\") @declare(\"int\", \"i\") @declare(\"int\", \"j\")  \n"
        + "    while (i < n) { @label(\"iwhile\") \n"
        + "         if (pattern.charAt(j) == text.charAt(i)) { @label(\"charCompare\")\n"
        + "             if (j == m-1) @label(\"jcompm\") \n"
        + "                   return i - m + 1; //return match position @label(\"returnMatch\") \n"
        + "             i++; j++; @label(\"ijInc\") @inc(\"i\")@inc(\"j\") \n"
        + "         } @label(\"charCompEnd\") \n"
        + "         else if (j > 0) @label(\"jcomp0\") \n"
        + "             j = Fail[j-1]; @label(\"checkFail\") \n"
        + "         else i++; @label(\"iInc\")@inc(\"i\") \n"
        + "    } @label(\"iwhileEnd\") \n"
        + "    return -1; // no match found @label(\"returnNoMatch\") \n"
        + "} @label(\"KMPMatchingEnd\") \n";
  }

  public void init() {
    super.init();

    // first, set the visual properties (text, etc.)
    titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);

    hintProps = new TextProperties();
    hintProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
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

    // set up the properties of arrayMarkers
    arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);

    arrayRMProps = new ArrayMarkerProperties();
    arrayRMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "R");
    arrayRMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    arrayJMProps = new ArrayMarkerProperties();
    arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
    arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);

    arrayIndexMProps = new ArrayMarkerProperties();
    arrayIndexMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Index");
    arrayIndexMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);

    // at last, timing effect
    durationTiming = new MsTiming(500);

    // layout of mainframe GUI
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

    sourceCode = lang.newSourceCode(new Offset(0, 50, "patternString",
        AnimalScript.DIRECTION_SW), "MainSC", null, scPropsMain);
    vars.declare("int", "Index");

    iMarker = lang.newArrayMarker(textStringArray, 0, "i", null, arrayIMProps);
    jMarker = lang.newArrayMarker(patternStringArray, 0, "j", null,
        arrayJMProps);

    parse();

  }

  public void KMPmatching(String[] text, String[] pattern) { // here string
                                                             // array as
                                                             // parameter, not
                                                             // string.

    // convert stringArray to string, which are used in java implementation;
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

    amuI = new ArrayMarkerUpdater(iMarker, null, null, textString.length() - 1);
    amuJ = new ArrayMarkerUpdater(jMarker, null, null,
        patternString.length() - 1);

    exec("intro");
    lang.nextStep();
    exec("entryMain");
    lang.nextStep();

    int result = KMPmain(textString, patternString);
    lang.nextStep();
    if (result == -1) {
      exec("returnNoMatch");
      subTitle.setText("No pattern found in given text !", null, null);
    } else {
      subTitle.setText("Pattern Found at Index R = " + Integer.toString(result)
          + ".", null, null);
      lang.newArrayMarker(textStringArray, result, "r", null, arrayRMProps);
    }

  }

  public int KMPmain(String textString, String patternString) {
    exec("startPrepossessing");
    lang.nextStep();
    exec("entryPrep");
    lang.nextStep();
    subTitle.setText(
        "The prepossessing of pattern string starts at right hand", null, null);
    int[] fail = computeFailFunction(patternString);
    exec("prepossessingEnd");
    lang.nextStep();
    exec("startSearching");
    lang.nextStep();
    exec("initVars");
    int n = textString.length();
    vars.set("n", String.valueOf(n));
    int m = patternString.length();
    vars.set("m", String.valueOf(m));
    int i = 0;
    vars.set("i", String.valueOf(i));
    int j = 0;
    vars.set("j", String.valueOf(j));
    // lang.nextStep();

    amuI.setVariable(vars.getVariable("i"));
    amuJ.setVariable(vars.getVariable("j"));
    lang.nextStep();
    subTitle.setText("The search is running now ... ", null, null);

    exec("iwhile");
    while (i < n) { // 5
      lang.nextStep();
      exec("charCompare");
      patternStringArray.highlightElem(j, null, null);
      textStringArray.highlightElem(i, null, null);

      if (patternString.charAt(j) == textString.charAt(i)) { // 6

        lang.nextStep();
        exec("jcompm");
        if (j == m - 1) {
          lang.nextStep();
          exec("returnMatch");
          return i - m + 1; // match 8
        }
        lang.nextStep();
        exec("ijInc");
        i++; // 9
        j++; // 10
        lang.nextStep();

        exec("charCompare");
      } else {
        lang.nextStep();
        exec("jcomp0");
        if (j > 0) { // 12
          lang.nextStep();
          exec("checkFail");
          int oldj = j;
          arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, new String(
              "fail[j-1]"));
          j2Marker = lang.newArrayMarker(patternFailFunc, j - 1, "j-1", null,
              arrayJMProps);
          j = fail[j - 1];
          vars.set("j", String.valueOf(j));
          lang.nextStep();
          j2Marker.hide();
          int skip = oldj - j;

          patternStringArray.moveBy("translate", 16 * skip, 0, null,
              durationTiming);
          patternStringArray.unhighlightElem(j, j + skip, null, null);
          patternStringArray.unhighlightElem(j, null, null);
          textStringArray.highlightCell(i - oldj, i - j - 1, null, null);
          textStringArray.unhighlightElem(i - oldj, i - j - 1, null, null);
          textStringArray.unhighlightElem(i, null, null);

        } else {
          lang.nextStep();
          exec("iInc");
          i++; // 14
          lang.nextStep();

          patternStringArray.moveBy("translate", 16, 0, null, durationTiming);
          textStringArray.unhighlightElem(i - 1, i, null, null);
          textStringArray.highlightCell(i - 1, null, null);
          patternStringArray.unhighlightElem(0, null, null);
        }
      }
      exec("iwhile");
    }
    exec("iwhileEnd");
    iMarker.moveOutside(null, durationTiming);
    lang.nextStep();
    return -1; // no match 16
  }

  public int[] computeFailFunction(String patternString) {
    exec("initialFail");
    int[] fail = new int[patternString.length()];
    lang.newText(new Offset(130, 25, "title", AnimalScript.DIRECTION_SE),
        "FailFunction", "failfunc", null, hintProps);
    patternFailFunc = lang.newIntArray(new Offset(230, 25, "title",
        AnimalScript.DIRECTION_SE), fail, "patternFailFunc", null,
        intArrayProps);
    lang.nextStep();
    exec("setFail");
    fail[0] = 0;
    int m = patternString.length();
    int j = 0;
    int i = 1;

    lang.nextStep();
    IndexMarker = lang.newArrayMarker(patternFailFunc, i, "Index", null,
        arrayIndexMProps);

    exec("loopInFail");
    lang.nextStep();
    String prefix, suffix;
    int oldi = 0;
    boolean updateI = true; // help variables in implementation
    while (i < m) { // 3
      vars.set("Index", String.valueOf(i));
      suffix = patternString.substring(1, i + 1);
      IndexMarker.hide();
      lang.nextStep();
      arrayIndexMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, new String(
          "current Pattern[1..Index] is " + suffix));
      IndexMarker = lang.newArrayMarker(patternFailFunc, i, "Index", null,
          arrayIndexMProps);
      exec("findPrefix");
      lang.nextStep();

      if (patternString.charAt(j) == patternString.charAt(i)) { // j + 1
                                                                // characters
                                                                // match
        fail[i] = j + 1;
        oldi = i;
        i++;
        j++;
      } else if (j > 0) {// j follows a matching prefix
        j = fail[j - 1];
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
          prefix = patternString.substring(0, fail[oldi]);
        }

        IndexMarker.hide();
        arrayIndexMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
            new String("The longest prefix is : " + prefix));
        IndexMarker = lang.newArrayMarker(patternFailFunc, oldi, "Index", null,
            arrayIndexMProps);
        exec("updateFail");
        lang.nextStep();
        patternFailFunc.put(oldi, fail[oldi], null, null);

        lang.nextStep();
        // IndexMarker.hide();
        exec("loopInFail");
      }
      updateI = true;
    } // 6

    lang.nextStep();
    IndexMarker.hide();
    exec("returnFail");
    lang.nextStep();
    return fail; // 8
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    text = (String[]) primitives.get("Text");
    pattern = (String[]) primitives.get("Pattern");
    // now, set the visual properties for the source code
    scPropsMain = new SourceCodeProperties();
    scPropsMain.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    scPropsMain.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    // user input through GUI
    scPropsMain.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    scPropsMain.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
    scPropsMain.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, props.get(
        "sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));

    init();

    KMPmatching(this.text, this.pattern);
    return lang.toString();
  }

  private static final String DESCRIPTION = "In typical string matching problem, a Text (T with length of n) and a Pattern (P with length of m) are given. \n"
                                              + "We need to find whether P is a substring of T. If yes, return the first position index in T.\n"
                                              + "Two algorithms like Brute Force(BF), Boyer-Moore(BM) methods are already in Animal-x.jar visualized.\n"
                                              + "The Knuth-Morris-Pratt(KMP) algorithm will be introduced, which steadily achieves running time of O(n+m).\n"
                                              + "Especially, in comparision under the worst case, either BF or BM has a worse running time O(nm).";

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  public String getName() {
    return "KMP Matching[Annotation Based]";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public String getAlgorithmName() {
    return "KMP String Matching Algorithm";
  }

  @Override
  public String getAnimationAuthor() {
    return "Leqiao Peng"; // <ne95ocyg[at]rbg.informatik.tu-darmstadt.de>";
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
