/*
    A man, a plan, a canal - Panama!
    A man, a plan, a cat, a canal - Panama!
    A man, a plan, a cat, a ham, a yak, a yam, a hat, a canal - Panama!
    A man, a plan, a canoe, pasta, heros' rajahs, a coloratura, maps, snipe, percale, macaroni, a gag, a banana bag, a tan, a tag, a banana bag again (or a camel), a crepe, pins, Spam, a rut, a Rolo, cash, a jar, sore hats, a peon, a canal - Panama!
    A man, a plan, a caret, a ban, a myriad, a sum, a lac, a liar, a hoop, a pint, a catalpa, a gas, an oil, a bird, a yell, a vat, a caw, a pax, a wag, a tax, a nay, a ram, a cap, a yam, a gay, a tsar, a wall, a car, a luger, a ward, a bin, a woman, a vassal, a wolf, a tuna, a nit, a pall, a fret, a watt, a bay, a daub, a tan, a cab, a datum, a gall, a hat, a fag, a zap, a say, a jaw, a lay, a wet, a gallop, a tug, a trot, a trap, a tram, a torr, a caper, a top, a tonk, a toll, a ball, a fair, a sax, a minim, a tenor, a bass, a passer, a capital, a rut, an amen, a ted, a cabal, a tang, a sun, an ass, a maw, a sag, a jam, a dam, a sub, a salt, an axon, a sail, an ad, a wadi, a radian, a room, a rood, a rip, a tad, a pariah, a revel, a reel, a reed, a pool, a plug, a pin, a peek, a parabola, a dog, a pat, a cud, a nu, a fan, a pal, a rum, a nod, an eta, a lag, an eel, a batik, a mug, a mot, a nap, a maxim, a mood, a leek, a grub, a gob, a gel, a drab, a citadel, a total, a cedar, a tap, a gag, a rat, a manor, a bar, a gal, a cola, a pap, a yaw, a tab, a raj, a gab, a nag, a pagan, a bag, a jar, a bat, a way, a papa, a local, a gar, a baron, a mat, a rag, a gap, a tar, a decal, a tot, a led, a tic, a bard, a leg, a bog, a burg, a keel, a doom, a mix, a map, an atom, a gum, a kit, a baleen, a gala, a ten, a don, a mural, a pan, a faun, a ducat, a pagoda, a lob, a rap, a keep, a nip, a gulp, a loop, a deer, a leer, a lever, a hair, a pad, a tapir, a door, a moor, an aid, a raid, a wad, an alias, an ox, an atlas, a bus, a madam, a jag, a saw, a mass, an anus, a gnat, a lab, a cadet, an em, a natural, a tip, a caress, a pass, a baronet, a minimax, a sari, a fall, a ballot, a knot, a pot, a rep, a carrot, a mart, a part, a tort, a gut, a poll, a gateway, a law, a jay, a sap, a zag, a fat, a hall, a gamut, a dab, a can, a tabu, a day, a batt, a waterfall, a patina, a nut, a flow, a lass, a van, a mow, a nib, a draw, a regular, a call, a war, a stay, a gam, a yap, a cam, a ray, an ax, a tag, a wax, a paw, a cat, a valley, a drib, a lion, a saga, a plat, a catnip, a pooh, a rail, a calamus, a dairyman, a bater, a canal - Panama! 
 */

package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
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
import algoanim.util.Offset;

public class PalindromeGenerator implements Generator {
  private Language                  lang;
  private String                    text;
  private TextProperties            helperProps;
  private SourceCodeProperties      codeProps;
  private TextProperties            textProps;

  private StringArray               strArray;
  private SourceCode                sc;
  private Text                      counter;
  private int                       count, num;
  private Text                      helper;
  // private Text header;
  // private Rect hRect;
  private ArrayProperties           arrayProps;
  private ArrayMarker               lMarker;
  private ArrayMarker               rMarker;
  private ArrayMarkerProperties     markerProps;
  private FillInBlanksQuestionModel algoQuest1;
  private boolean                   complexity;

  public void init() {
    lang = new AnimalScript("Finding Palindromes",
        "Florian Spitz, Toni Plöchl", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    text = (String) primitives.get("text");
    helperProps = (TextProperties) props.getPropertiesByName("helperProps");
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    codeProps = (SourceCodeProperties) props.getPropertiesByName("codeProps");
    textProps = (TextProperties) props.getPropertiesByName("textProps");

    lang.setStepMode(true);

    // First page... Header + description
    header();
    description();
    lang.nextStep("Introduction");

    // Second page... Header + subHeadline
    lang.hideAllPrimitives();
    header();

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 13));
    lang.newText(new Coordinates(20, 60),
        "Check whether the following text is a palindrome: ", "headline", null,
        textProps);
    lang.newText(new Coordinates(20, 110), text, "headline", null, textProps);

    // create the helper
    helper = lang.newText(new Coordinates(40, 210), "", "helper", null,
        helperProps);

    // now, create the source code entity
    sc = lang.newSourceCode(new Coordinates(40, 270), "sourceCode", null,
        codeProps);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display delay
    sc.addCodeLine("public boolean isPalindromeOrig(String s) {", null, 0, null); // 0
    sc.addCodeLine("// use regex to remove the punctuation and spaces", null,
        2, null); // 1
    sc.addCodeLine("s = s.replaceAll(\\\"\\\\\\\\W\\\",\\\"\\\");", null, 2,
        null); // 2
    sc.addCodeLine(" ", null, 2, null); // 3
    sc.addCodeLine("// check whether the string is a palindrome", null, 2, null); // 4
    sc.addCodeLine("for (int i = 0; i < s.length() / 2; i++) {", null, 2, null); // 5
    sc.addCodeLine("if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {", null,
        4, null); // 6
    sc.addCodeLine(" ", null, 4, null); // 7
    sc.addCodeLine("// if the chars aren't identical compare the lowercases",
        null, 6, null); // 8
    sc.addCodeLine("if (Character.toLowerCase(s.charAt(i)) != Character", null,
        6, null); // 9
    sc.addCodeLine(".toLowerCase(s.charAt(s.length() - 1 - i))) {", null, 10,
        null); // 10
    sc.addCodeLine("return false;", null, 8, null); // 11
    sc.addCodeLine("}", null, 6, null); // 12
    sc.addCodeLine("}", null, 4, null); // 13
    sc.addCodeLine("}", null, 2, null); // 14
    sc.addCodeLine("return true;", null, 2, null); // 15
    sc.addCodeLine("}", null, 0, null); // 16

    highlightSC(0, false);

    // create the counter
    TextProperties counterProps = new TextProperties();
    counterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 13));
    counter = lang.newText(new Coordinates(40, 235), "Comparisons: " + count,
        "counter", null, counterProps);
    counter.hide();

    // reset the counter for multiple generations in Animal
    count = 0;
    lang.nextStep("Algorithm");

    // Question
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    algoQuest1 = new FillInBlanksQuestionModel("algoQuest1");

    algoQuest1.setPrompt("Is the given text a palindrome?");
    algoQuest1.addAnswer((isPalindromeOrig(text) ? "yes" : "no"), 1, "");
    lang.addFIBQuestion(algoQuest1);

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 13));

    boolean found = isPalindrome(text);
    lang.newText(new Coordinates(20, 160), "The given text is"
        + (found ? "" : "n't") + " a palindrome!", "result", null, textProps);
    lang.newText(new Offset(0, 25, "result", AnimalScript.DIRECTION_NW),
        "The algorithm needed " + count + " comparisons to find that answer.",
        "result2", null, textProps);
    if (complexity) {
      lang.newText(
          new Offset(0, 25, "result2", AnimalScript.DIRECTION_NW),
          "The complexity is at most O(3n) because we had to convert some chars to lowercase.",
          "result3", null, textProps);
    } else {
      lang.newText(new Offset(0, 25, "result2", AnimalScript.DIRECTION_NW),
          "The complexity is O(2n). No lowercase comparisons were needed.",
          "result3", null, textProps);
    }

    helper.hide();
    counter.hide();
    sc.hide();
    strArray.hide();

    lang.nextStep("Result");

    lang.finalizeGeneration();

    return lang.toString();
  }

  public String getName() {
    return "Finding Palindromes";
  }

  public String getAlgorithmName() {
    return "Finding Palindromes";
  }

  public String getAnimationAuthor() {
    return "Florian Spitz, Toni Plöchl";
  }

  public String getDescription() {
    return "A Palindrome is a sequence of characters, which has the same meaning when read in either direction (left to right, right to left). When deciding whether the given word (phrase, number) is a palindrome, we usually omit white characters, punctuation marks and diacritics."
        + "\n"
        + "Among the basic types of palindromes belong palindromic words (radar, civic, level, rotor...), sentences (Madam, I'm Adam, Never odd or even...), numbers (99, 191, 112211...) and dates (01/02/2010, 10/12/2101...).";
  }

  public String getCodeExample() {
    return "public static boolean isPalindrome(String s) {" + "\n"
        + "	// use regex to remove the punctuation and spaces" + "\n"
        + "	s = s.replaceAll(\"\\W\", \"\");" + "\n"
        + "		// check whether the string is a palindrome" + "\n"
        + "	for (int i = 0; i < s.length() / 2; i++) {" + "\n"
        + "		if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {" + "\n"
        + "				// if the chars aren't identical compare the lowercases" + "\n"
        + "			if (Character.toLowerCase(s.charAt(i)) != Character" + "\n"
        + "					.toLowerCase(s.charAt(s.length() - 1 - i))) {" + "\n"
        + "				return false;" + "\n" + "			}" + "\n" + "		}" + "\n" + "	}"
        + "\n" + "	return true;" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void header() {
    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "Finding Palindromes", "header",
        null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
  }

  public void description() {
    // setup the description
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    lang.newText(
        new Coordinates(20, 100),
        "A Palindrome is a sequence of characters, which has the same meaning when read in either",
        "description1", null, textProps);
    lang.newText(
        new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
        "direction (left to right, right to left). When deciding whether the given word (phrase, number)",
        "description2", null, textProps);
    lang.newText(
        new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
        "is a palindrome, we usually omit white characters, punctuation marks and diacritics.",
        "description3", null, textProps);
    lang.newText(
        new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
        "Among the basic types of palindromes belong palindromic words (radar, civic, level, rotor...),",
        "description4", null, textProps);
    lang.newText(
        new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
        "sentences (Madam, I'm Adam, Never odd or even...), numbers (99, 191, 112211...) and",
        "description5", null, textProps);
    lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
        "dates (01/02/2010, 10/12/2101...).", "description6", null, textProps);

  }

  public boolean isPalindromeOrig(String theString) {
    String s = theString;
    // use regex to remove the punctuation and spaces
    s = s.replaceAll("\\W", "");

    // check whether the string is a palindrome
    for (int i = 0; i < s.length() / 2; i++) {
      if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {

        // if the chars aren't identical compare the lowercases
        if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s
            .charAt(s.length() - 1 - i))) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isPalindrome(String checkString) {
    String check = checkString;
    // use regex to remove the punctuation and spaces
    check = check.replaceAll("\\W", "");
    highlightSC(2, true);
    helper
        .setText("use regex to remove the punctuation and spaces", null, null);

    // convert to StringArray for Animal
    String[] sarray = check.split("(?!^)");

    // create the stringArray
    strArray = lang.newStringArray(new Coordinates(40, 180), sarray,
        "intArray", null, arrayProps);
    lang.nextStep();

    // initialize the markers
    // we need to redraw the markers because ArrayMarker.move() is really
    // broken...
    markerProps = new ArrayMarkerProperties();
    markerProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
    lMarker = lang.newArrayMarker(strArray, 0, "l0", null, markerProps);
    rMarker = lang.newArrayMarker(strArray, 0, "r0", null, markerProps);
    lMarker.hide();
    rMarker.hide();
    highlightSC(5, true);
    int j = strArray.getLength();
    String left, right;
    helper.setText("iterate over the array", null, null);
    lang.nextStep();
    counter.show();

    // check whether the string is a palindrome
    for (int i = 0; i < strArray.getLength() / 2; i++) {
      countTheCounter();
      highlightSC(6, true);
      j--;
      left = strArray.getData(i);
      right = strArray.getData(strArray.getLength() - 1 - i);
      helper.setText("compare \\\"" + left + "\\\" with \\\"" + right
          + "\\\" : ", null, null);
      toggleMarkers(i, j, left, right, Color.black);

      if (left.equals(right)) {
        helper.setText("compare \\\"" + left + "\\\" with \\\"" + right
            + "\\\" : identical", null, null);
        toggleMarkers(i, j, left, right, Color.green);

      }

      if (!left.equals(right)) { // else
        helper.setText("compare \\\"" + left + "\\\" with \\\"" + right
            + "\\\" : not identical", null, null);

        toggleMarkers(i, j, left, right, Color.red);
        helper.setText("compare lowercase chars \\\"" + left.toLowerCase()
            + "\\\" with \\\"" + right.toLowerCase() + "\\\" : ", null, null);
        highlightSC(8, 10, true);
        toggleMarkers(i, j, left.toLowerCase(), right.toLowerCase(),
            Color.black);
        countTheCounter();
        complexity = true;
        if (left.equalsIgnoreCase(right)) {
          helper.setText("compare lowercase chars \\\"" + left.toLowerCase()
              + "\\\" with \\\"" + right.toLowerCase() + "\\\" : identical",
              null, null);
          toggleMarkers(i, j, left.toLowerCase(), right.toLowerCase(),
              Color.green);
        }

        if (left.equalsIgnoreCase(right) == false) {
          helper.setText(
              "compare lowercase chars \\\"" + left.toLowerCase()
                  + "\\\" with \\\"" + right.toLowerCase()
                  + "\\\" : not identical", null, null);
          toggleMarkers(i, j, left, right, Color.red);
          highlightSC(11, true);
          helper.setText("No palindrome found!", null, null);
          lMarker.hide();
          rMarker.hide();
          lang.nextStep();
          return false;
        }
      }
    }
    highlightSC(15, true);
    helper.setText("Palindrome found!", null, null);
    lMarker.hide();
    rMarker.hide();
    lang.nextStep();
    return true;
  }

  /*
   * Helper
   */

  public void toggleMarkers(int i, int j, String lname, String rname,
      Color color) {
    num++;
    markerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

    lMarker.hide();

    markerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, lname);
    lMarker = lang.newArrayMarker(strArray, i, "l" + num, null, markerProps);

    rMarker.hide();
    markerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, rname);

    rMarker = lang.newArrayMarker(strArray, j, "r" + num, null, markerProps);
    lang.nextStep();
  }

  public void highlightSC(int i, boolean unhighlight) {
    highlightSC(i, i, unhighlight);
  }

  public void highlightSC(int from, int to, boolean unhighlight) {
    if (unhighlight) {
      unhighlightSC();
    }
    for (int i = from; i <= to; i++) {
      sc.highlight(i);
    }
  }

  public void unhighlightSC() {
    for (int i = 0; i <= 16; i++) {
      sc.unhighlight(i);
    }
  }

  public void countTheCounter() {
    count++;
    counter.setText("Comparisons: " + count, null, null);
  }

}
