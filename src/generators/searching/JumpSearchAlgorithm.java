package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class JumpSearchAlgorithm implements Generator {
  private Language              lang;
  // private ArrayProperties user_array;
  // private SourceCodeProperties user_src;
  // private ArrayMarkerProperties user_marker_last;
  // private int[] intArray;
  // private ArrayMarkerProperties user_marker_jump;
  private ArrayProperties       arrayProps;
  private TextProperties        textProps, headlineProps, subHeadProps; // ,
                                                                        // varProps;
  private ArrayMarkerProperties jumpProps, jumpProps_1, lastProps;
  private RectProperties        rectProps;
  private SourceCodeProperties  srcProps;
  private SourceCode            src;
  private Timing                defaultTiming;
  private Text                  headline;                              // ,
                                                                        // srcHeadline;
  private Text                  title_vars;
  private Rect                  title_underline;                       // ,
                                                                        // src_underline;
                                                                        // private
                                                                        // Rect
                                                                        // var_underline;
  private int                   count_first  = 0;
  private int                   count_second = 0;
  private IntArray              arr_input;
  // private Text var_key, var_length;
  private Text                  var_last, var_jump;

  public void init() {
    lang = new AnimalScript("Jump Search [DE]",
        "The An Binh Nguyen, Nam Truong Le", 800, 600);
    // Activate step control
    lang.setStepMode(true);

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    defaultTiming = new TicksTiming(15);

    // create properties with default values
    arrayProps = new ArrayProperties();
    textProps = new TextProperties();
    headlineProps = new TextProperties();
    subHeadProps = new TextProperties();
    // varProps = new TextProperties();
    srcProps = new SourceCodeProperties();
    rectProps = new RectProperties();

    // Redefine source code properties
    srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    srcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(56,
        122, 255));
    srcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // Redefine properties: border red, filled with gray
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(233, 240,
        255)); // fill color gray
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(
        149, 184, 255));
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(
        255, 255, 255));
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(149, 184,
        255));
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 15));

    // Redefine text properties
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    // Redefine headline properties
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    headlineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(56,
        122, 255));

    // Redefine sub headline properties
    subHeadProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 14));
    subHeadProps
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 0));

    // Redefine sub headline properties
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(149, 184,
        255));
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(233, 240,
        255));

    jumpProps = new ArrayMarkerProperties();
    jumpProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    jumpProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "jump");

    jumpProps_1 = new ArrayMarkerProperties();
    jumpProps_1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    jumpProps_1.set(AnimationPropertiesKeys.LABEL_PROPERTY, "jump - 1");

    lastProps = new ArrayMarkerProperties();
    lastProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    lastProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "last");

  }

  public void jump_search(int[] inputArray, int searchKey) {

    // Show headline
    headline = lang.newText(new Coordinates(20, 35),
        "Der Jump Search Algorithmus", "headline", null, headlineProps);

    // Underline rect
    title_underline = lang
        .newRect(new Offset(-5, 2, headline, AnimalScript.DIRECTION_SW),
            new Offset(400, 5, headline, AnimalScript.DIRECTION_SE),
            "title_underline", defaultTiming, rectProps);

    // Show description

    Text desc_1 = lang
        .newText(
            new Coordinates(20, 120),
            "Der 'Jump Search'-Algorithmus ist ein Suchalgorithmus für geordnete Integer-Arrays.",
            "desc_1", null, textProps);

    Text desc_2 = lang
        .newText(
            new Coordinates(20, 155),
            "Der Algorithmus ist in zwei aufeinander folgende Suchabläufe unterteilt:",
            "desc_2", null, textProps);

    Text desc_3 = lang.newText(new Coordinates(20, 190),
        "Suchablauf 1 (Der 'Jump'-Teil):", "desc_3", null, subHeadProps);

    Text desc_4 = lang
        .newText(
            new Coordinates(20, 225),
            "Die Liste wird zunächst an Vielfachen des 'Jumps' überprüft, bis eine Zahl gefunden wurde,",
            "desc_4", null, textProps);

    Text desc_5 = lang
        .newText(
            new Coordinates(20, 260),
            "die größer ist als die gesuchte Zahl. Der 'Jump' beträgt einen Bruchteil der Listenlänge,",
            "desc_5", null, textProps);

    Text desc_6 = lang
        .newText(
            new Coordinates(20, 295),
            "zum Beispiel die Wurzel eben dieser. Die Position der letzten kleineren Zahl dient als",
            "desc_6", null, textProps);

    Text desc_7 = lang.newText(new Coordinates(20, 330),
        "Ausgangspunkt für den zweiten Suchablauf.", "desc_7", null, textProps);

    Text desc_8 = lang.newText(new Coordinates(20, 365),
        "Suchablauf 2 (Die lineare Suche):", "desc_8", null, subHeadProps);

    Text desc_9 = lang
        .newText(
            new Coordinates(20, 400),
            "Nun folgt eine lineare Suche ausgehend vom in Ablauf 1 gefundenen letzten kleineren Zahl",
            "desc_9", null, textProps);

    Text desc_10 = lang
        .newText(
            new Coordinates(20, 435),
            "bis die gesuchte Zahl gefunden wurde. Die Position dieser Zahl ist der Rückgabewert.",
            "desc_10", null, textProps);

    lang.nextStep();

    lang.newText(new Coordinates(335, 120), "Pseudocode:", "srcHeadline", null,
        subHeadProps);

    showSourceCode();

    desc_1.hide();
    desc_2.hide();
    desc_3.hide();
    desc_4.hide();
    desc_5.hide();
    desc_6.hide();
    desc_7.hide();
    desc_8.hide();
    desc_9.hide();
    desc_10.hide();

    src.highlight(0);

    // create array
    arr_input = lang.newIntArray(new Coordinates(30, 240), inputArray,
        "arr_input", null, arrayProps);

    title_vars = lang.newText(new Coordinates(35, 350), "Variablen:",
        "title_vars", null, subHeadProps);

    // Underline vars
    lang.newRect(new Offset(0, 15, title_vars, AnimalScript.DIRECTION_SW),
        new Offset(150, 140, title_vars, AnimalScript.DIRECTION_SE),
        "title_underline", defaultTiming, rectProps);

    lang.newText(new Coordinates(45, 390), "key = " + searchKey, "var_key",
        null, textProps);

    // search pos
    int the_pos = Arrays.binarySearch(inputArray, searchKey);

    if (the_pos >= 0) {
      arr_input.highlightElem(the_pos, null, defaultTiming);
      arr_input.highlightCell(the_pos, null, defaultTiming);
    }

    lang.nextStep("Initialisierung");

    src.unhighlight(0);
    src.highlight(1);

    int arrayLength = inputArray.length;

    lang.newText(new Coordinates(45, 410), "length = " + arrayLength,
        "var_length", null, textProps);

    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);

    lang.addQuestionGroup(groupInfo);

    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion");
    mcq.setPrompt("Welche Vorraussetzung muss für die korrekte Ausführung des Jump-Search-Algorithmus gegeben sein?");
    mcq.addAnswer("Die Eingabeliste muss geordnet sein", 5, "Richtige Antwort!");
    mcq.addAnswer("Die Liste darf nicht länger als der initiale Jump sein", 0,
        "Falsche Antwort!");
    mcq.setGroupID("First question group");
    lang.addMCQuestion(mcq);

    lang.nextStep();

    src.unhighlight(1);
    src.highlight(2);

    int last = 0;
    var_last = lang.newText(new Coordinates(45, 430), "last = " + last,
        "var_last", null, textProps);

    lang.nextStep();

    src.unhighlight(2);
    src.highlight(3);

    int jump = (int) Math.sqrt(arrayLength);
    var_jump = lang.newText(new Coordinates(45, 450), "jump = " + jump,
        "var_jump", null, textProps);

    lang.nextStep("Beginn des Jump-Teils");

    src.unhighlight(3);
    src.highlight(4);

    ArrayMarker marker_jump = lang.newArrayMarker(arr_input, jump - 1,
        "jump - 1", null, jumpProps_1);

    while (arr_input.getData(min(jump, arrayLength) - 1) < searchKey) {

      lang.nextStep();

      count_first++;

      src.unhighlight(4);
      src.highlight(5);

      last = jump;

      var_last.setText("last = " + jump, null, defaultTiming);

      lang.nextStep();

      src.unhighlight(5);
      src.highlight(6);

      jump = jump + (int) Math.sqrt(arrayLength);

      if (count_first == 1) {

        QuestionGroupModel groupInfo2 = new QuestionGroupModel(
            "Second question group", 1);

        lang.addQuestionGroup(groupInfo2);

        FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
            "fillInBlanksQuestion");

        fibq.setPrompt("Wie lautet die Zielposition des nächsten Jumps im Eingabearray?");
        fibq.addAnswer(jump + "", 5, "Richtige Antwort!");
        fibq.setGroupID("Second question group");

        lang.addFIBQuestion(fibq);

        lang.nextStep();
      }

      var_jump.setText("jump = " + jump, null, defaultTiming);

      marker_jump.move(jump - 1, null, defaultTiming);

      lang.nextStep();

      src.unhighlight(6);
      src.highlight(7);

      if (last >= arrayLength) {
        not_in_list();
        return;
      }

      lang.nextStep();

      src.unhighlight(7);
      src.highlight(4);

    }

    src.unhighlight(4);

    src.highlight(9);

    if (inputArray[jump - 1] == searchKey) {

      lang.nextStep();

      in_list(jump - 1);
      return;

    }

    lang.nextStep("Beginn der linearen Suche");

    src.unhighlight(9);
    src.highlight(10);

    marker_jump.hide();

    ArrayMarker marker_last = lang.newArrayMarker(arr_input, last, "last",
        null, lastProps);

    lang.nextStep();

    TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
        "trueFalseQuestion", true, 5);
    tfq.setPrompt("Im linearen Teil muss man die Einschränkung (last < jump) im Schleifenkopf gemacht werden, da im Falle der Überschreitung der Jump im Jumpteil schon einen Schritt weitergesprungen wäre.");
    tfq.setGroupID("Second question group");
    lang.addTFQuestion(tfq);

    lang.nextStep();

    while ((arr_input.getData(last) != searchKey) && (last < jump)) {

      lang.nextStep();

      count_second++;

      src.unhighlight(10);
      src.highlight(11);
      last = last + 1;

      marker_last.move(last, null, defaultTiming);
      var_last.setText("last = " + last, null, defaultTiming);

      lang.nextStep();

      src.unhighlight(11);
      src.highlight(12);

      if (last > arrayLength - 1) {
        lang.nextStep();

        src.unhighlight(12);
        src.highlight(13);

        not_in_list();
        return;
      }

      lang.nextStep();

      src.unhighlight(12);
      src.highlight(14);

      if (inputArray[last] == searchKey) {
        lang.nextStep();

        src.unhighlight(14);
        src.highlight(15);

        in_list(last);
        return;
      }

      lang.nextStep();

      src.unhighlight(14);
      src.highlight(10);

    }

    not_in_list();
    return;

  }

  public void not_in_list() {

    lang.hideAllPrimitives();
    headline.show();
    title_underline.show();

    lang.newText(new Coordinates(20, 120),
        "Die gesuchte Zahl befindet sich nicht in der Liste.", "not_in_list_1",
        null, textProps);

    lang.newText(new Coordinates(20, 155),
        "Um dies festzustellen, benötigte der Algorithmus;", "not_in_list_2",
        null, textProps);

    lang.newText(new Coordinates(20, 190), "<" + count_first
        + "> Jumpschleifendurchläufe", "not_in_list_4", null, subHeadProps);

    lang.newText(new Coordinates(20, 225), "<" + count_second
        + "> Durchgänge der linearen Suche", "not_in_list_5", null,
        subHeadProps);

    lang.newText(new Coordinates(20, 385), "Laufzeit des Algorithmus:",
        "laufzeit_1", null, subHeadProps);

    lang.newText(
        new Coordinates(20, 420),
        "Die Laufzeit des Jump-Search-Algorithmus beträgt in dieser Implementation O(√n).",
        "laufzeit_2", null, textProps);

    lang.newText(
        new Coordinates(20, 455),
        "Die Laufzeit kann allerdings durch eine bessere Wahl des Jumps (zum Beispiel Jumplänge = √|Restliste|)",
        "laufzeit_4", null, textProps);

    lang.newText(new Coordinates(20, 490),
        "auf logarithmische Laufzeit (O[log n]) verkürzt werden.",
        "laufzeit_6", null, textProps);
  }

  public void in_list(int pos) {

    lang.hideAllPrimitives();
    headline.show();
    title_underline.show();
    /*
     * arr_input.hide(); title_vars.hide(); var_underline.hide();
     * 
     * var_key.hide(); var_jump.hide(); var_length.hide(); var_last.hide();
     * 
     * src_underline.hide(); src.hide(); srcHeadline.hide();
     */

    lang.newText(new Coordinates(20, 120), "Die gesuchte Zahl wurde an Stelle "
        + pos + " gefunden!", "in_list_1", null, textProps);

    lang.newText(new Coordinates(20, 155),
        "Um die Position zu finden, benötigte der Algorithmus", "in_list_2",
        null, textProps);

    lang.newText(new Coordinates(20, 190), "<" + count_first
        + "> Jumpschleifendurchläufe", "not_in_list_4", null, subHeadProps);

    lang.newText(new Coordinates(20, 225), "<" + count_second
        + "> Durchgänge der linearen Suche", "not_in_list_5", null,
        subHeadProps);

    lang.newText(new Coordinates(20, 385), "Laufzeit des Algorithmus:",
        "laufzeit_1", null, subHeadProps);

    lang.newText(
        new Coordinates(20, 420),
        "Die Laufzeit des Jump-Search-Algorithmus beträgt in dieser Implementation O(√n).",
        "laufzeit_2", null, textProps);

    lang.newText(
        new Coordinates(20, 455),
        "Die Laufzeit kann allerdings durch eine bessere Wahl des Jumps (zum Beispiel Jumplänge = √|Restliste|)",
        "laufzeit_4", null, textProps);

    lang.newText(new Coordinates(20, 490),
        "auf logarithmische Laufzeit (O[log n]) verkürzt werden.",
        "laufzeit_6", null, textProps);

  }

  public int min(int a, int b) {

    if (b < a)
      return b;
    else
      return a;
  }

  public void showSourceCode() {

    // Create the source code entity
    src = lang.newSourceCode(new Coordinates(350, 160), "sourceCode", null,
        srcProps);

    // Underline rect
    lang.newRect(new Coordinates(335, 158), new Coordinates(705, 508),
        "src_underline", defaultTiming, rectProps);

    // Add code lines
    src.addCodeLine("int jumpSearch(int input[], int key) {", null, 0, null);

    src.addCodeLine("int length = input.length;", null, 1, null);
    src.addCodeLine("int last = 0;", null, 1, null);
    src.addCodeLine("int jump = (int)Math.sqrt(length);", null, 1, null);
    src.addCodeLine("while (input[min(jump, length) - 1] < key) {", null, 1,
        null);

    src.addCodeLine("last = jump;", null, 2, null);
    src.addCodeLine("jump = jump + sqrt(length);", null, 2, null);
    src.addCodeLine("if (last >= length)  return  -1;", null, 2, null);

    src.addCodeLine("}", null, 1, null);
    src.addCodeLine("if (input[jump] == key) return last;", null, 1, null);
    src.addCodeLine("while ((input[last] != key) && (last < jump)) {", null, 1,
        null);

    src.addCodeLine("last++;", null, 2, null);
    src.addCodeLine("if (last > length - 1)", null, 2, null);

    src.addCodeLine("return -1;", null, 3, null);

    src.addCodeLine("if (input[last] == key) {", null, 2, null);

    src.addCodeLine("return last;", null, 3, null);

    src.addCodeLine("}", null, 2, null);

    src.addCodeLine("}", null, 1, null);

    src.addCodeLine("return -1;", null, 1, null);

    src.addCodeLine("}", null, 0, null);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    arrayProps = (ArrayProperties) props.getPropertiesByName("user_array");
    srcProps = (SourceCodeProperties) props.getPropertiesByName("user_src");
    lastProps = (ArrayMarkerProperties) props
        .getPropertiesByName("user_marker_last");
    int[] inputArray = (int[]) primitives.get("intArray");
    int searchKey = (Integer) primitives.get("searchKey");
    jumpProps_1 = (ArrayMarkerProperties) props
        .getPropertiesByName("user_marker_jump");

    jump_search(inputArray, searchKey);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Jump Search [DE]";
  }

  public String getAlgorithmName() {
    return "Jump Search";
  }

  public String getAnimationAuthor() {
    return "The An Binh Nguyen, Nam Truong Le";
  }

  public String getDescription() {
    return "Der  <b>\"Jump Search\"-Algorithmus</b> ist ein Suchalgorithmus f&uuml;r geordnete Integer-Arrays.	"
        + "\n"
        + "\n"
        + "Der Algorithmus ist in zwei aufeinander folgende Suchabl&auml;ufe unterteilt:"
        + "\n"
        + "	"
        + "\n"
        + "<h2>Suchablauf 1 (Der \"Jump\"-Teil):</h2>"
        + "\n"
        + "	"
        + "\n"
        + "Die Liste wird zun&auml;chst an Vielfachen des \"Jumps\" &uuml;berpr&uuml;ft, bis eine Zahl gefunden"
        + "\n"
        + "wurde, die gr&ouml;&szlig;er ist als die gesuchte Zahl. Der \"Jump\" betr&auml;gt einen Bruchteil der"
        + "\n"
        + "Listenl&auml;nge zum Beispiel die Wurzel eben dieser. Die Position der letzten kleineren Zahl dient als"
        + "\n"
        + "Ausgangspunkt f&uuml;r den zweiten Suchablauf."
        + "\n"
        + "\n"
        + "<h2>Suchablauf 2 (Die lineare Suche):</h2>"
        + "\n"
        + "\n"
        + "Nun folgt eine lineare Suche ausgehend vom in Ablauf 1 gefundenen letzten kleineren Zahl "
        + "\n"
        + "bis die gesuchte Zahl gefunden wurde. Die Position dieser Zahl ist der R&uuml;ckgabewert.";
  }

  public String getCodeExample() {
    return "    public static int jumpsearch(int inputArray[], int searchKey ) {"
        + "\n"
        + "    	int arrayLength = inputArray.length;"
        + "\n"
        + "        int t=0;"
        + "\n"
        + "        int b=(int)Math.sqrt(arrayLength);"
        + "\n"
        + "        while (inputArray[min(b,arrayLength)-1] < searchKey){"
        + "\n"
        + "        	steps_a++;"
        + "\n"
        + "            t=b;"
        + "\n"
        + "            b = b + (int)Math.sqrt(arrayLength);"
        + "\n"
        + "            if ( t>=arrayLength)  return  -1;"
        + "\n"
        + "        }"
        + "\n"
        + "        "
        + "\n"
        + "        if (inputArray[t] == searchKey) return t;"
        + "\n"
        + "        "
        + "\n"
        + "        while (inputArray[t] != searchKey){"
        + "\n"
        + "        	steps_b++;"
        + "\n"
        + "            t=t+1;"
        + "\n"
        + "            if (t > arrayLength - 1)    "
        + "\n"
        + "                return -1;"
        + "\n"
        + "            if ( inputArray[t] == searchKey)  {"
        + "\n"
        + "                return t;"
        + "\n"
        + "            }"
        + "\n"
        + "        }" + "\n" + "        return -1;" + "\n" + "    }";
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