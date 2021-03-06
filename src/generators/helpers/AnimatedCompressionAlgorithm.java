package generators.helpers;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import translator.ResourceLocator;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.misc.MessageDisplay;

public abstract class AnimatedCompressionAlgorithm implements Generator {
  public static final String                       CODE_LABEL        = "@CodeLabel";

  protected static final Timing                    DEFAULT_TIMING    = new TicksTiming(
                                                                         10);

  protected AnimationPropertiesContainer           animationProperties;
  protected SourceCode                             code              = null;
  protected SourceCode                             complexity        = null;
  protected Locale                                 contentLocale     = null;
  protected SourceCode                             description       = null;
  protected Text                                   descriptionHeader = null;
  protected Text                                   header            = null;
  protected HashMap<String, Integer>               labelsToLines;
  protected Language                               lang;
  protected GeneratorType                          localType         = new GeneratorType(
                                                                         GeneratorType.GENERATOR_TYPE_COMPRESSION);
  protected Rect                                   nrAssBox          = null;
  protected Text                                   nrAssLabel        = null;
  protected Rect                                   nrCompBox         = null;
  protected Text                                   nrCompLabel       = null;
  protected int                                    nrAssignments     = 0,
      nrComparisons = 0;
  protected Text                                   nrStepsText       = null;
  protected Hashtable<String, AnimationProperties> primitiveProps;
  protected Hashtable<String, Object>              primitives;
  protected String                                 resourceName;
  protected Translator                             translator;
  protected ArrayPrimitive                         array;
  protected static MatrixProperties                mp;

  protected static ArrayProperties                 ap;

  protected static ArrayMarkerProperties           amp;

  protected static TextProperties                  tpsteps;

  protected static TextProperties                  tptopic;

  protected static TextProperties                  tpwords;

  protected static SourceCodeProperties            scp;

  protected static GraphProperties                 gp;

  protected static RectProperties                  rctp;

  public AnimatedCompressionAlgorithm() {
    // nothing to be done here
  }

  public AnimatedCompressionAlgorithm(String resourceName, Locale locale) {
    translator = new Translator(resourceName, locale);
    contentLocale = locale;
    primitiveProps = new Hashtable<String, AnimationProperties>(59);
  }

  public void init() {
    // matrix
    mp = new MatrixProperties();
    mp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    mp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    mp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);

    // array
    ap = new ArrayProperties();
    ap.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    // array marker
    amp = new ArrayMarkerProperties();
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // text topic
    tptopic = new TextProperties();
    tptopic.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    tptopic.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 32));

    // text steps
    tpsteps = new TextProperties();
    tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tpsteps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));

    // text "algo in words"
    tpwords = new TextProperties();
    tpwords.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.ITALIC, 22));
    tpwords.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // source code
    scp = new SourceCodeProperties();
    scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.RED);
    scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // graphs
    gp = new GraphProperties();
    gp.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
    gp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    gp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.YELLOW);
    gp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    gp.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
    gp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);

    // rectangle
    rctp = new RectProperties();
    rctp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  protected void setContentLocale(Locale locale) {
    contentLocale = locale;
  }

  //
  // /**
  // * getContentLocale returns the target Locale of the generated output
  // * Use e.g. Locale.US for English content, Locale.GERMANY for German, etc.
  // *
  // * @return a Locale instance that describes the content type of the output
  // */
  // public Locale getContentLocale() {
  // return contentLocale;
  // }

  public String getAlgorithmName() {
    return translator.translateMessage("algorithm");
  }

  /**
   * returns the output language of texts in the file
   * 
   * @return the IEEE code for the language used for texts in the animation
   */
  public String getOutputLanguage() {
    String outputLanguage = translator.translateMessage("outputLanguage", null,
        false);
    if (outputLanguage != null && !outputLanguage.startsWith("Invalid"))
      return outputLanguage;
    return Generator.JAVA_OUTPUT;
  }

  /**
   * getGeneratorType returns the Generator Type for the Generator (Search,
   * Sort, Graph, ...)
   * 
   * @return The Generator Type for the Generator.
   */
  public GeneratorType getGeneratorType() {
    return localType;
  }

  /**
   * getName returns the Name of this Generator (like "Quicksort with right
   * Pivot").
   * 
   * @return The Name of this Generator.
   */
  public String getName() {
    return translator.translateMessage("algoName");
  }

  /**
   * getDescription returns the Description of this Generator.
   * 
   * @return The Description of this Generator.
   */
  public String getDescription() {
    return translator.translateMessage("algoDesc");
  }

  public String getAnimationAuthor() {
    return translator.translateMessage("algoAuthor");
  }

  /**
   * getCodeExample returns a Code Example for this Generator.
   * 
   * @return A Code Example for this Generator.
   */
  public String getCodeExample() {
    return translator.translateMessageWithoutParameterExpansion("codeExample");
  }

  /**
   * getFileExtension returns the Extension for the file that is generated by
   * this Generator. This should be "asu" (animal-script-uncompressed), "asc"
   * (animal-script-compressed), "ama" (animal-ascii-uncompressed), "aml"
   * (animal-ascii-compressed), "tex", "txt", "pdf", ...
   * 
   * @return The extension for the file that is generated by this Generator.
   */
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /**
   * Stores the current parameter set passed in
   * 
   * @param props
   *          The AnimationPropertiesContainer with all the values set.
   * @param prims
   *          The Hashtable with all the Primitives set.
   */
  protected void setParameters(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    animationProperties = props;
    primitives = prims;
  }

  /**
   * Default set-up process for generating the animation
   * 
   * @param width
   *          the width of the target animation
   * @param height
   *          the height of the target animation
   */
  private void setUpAnimation(int width, int height) {
    // 1. generate Language object
    lang = new AnimalScript(translator.translateMessage("animTitle"),
        translator.translateMessage("algoAuthor"), width, height);
    lang.setStepMode(true);
  }

  /**
   * override this method to set other width or height measures, then invoke
   * setUpAnimation(w, h)!
   * 
   */
  protected void setUpAnimation() {
    setUpAnimation(640, 480);
  }

  /**
   * generates the algorithm title boxed by a rectangle
   */
  protected void createTitle() {
    String message = translator.translateMessage("algoTitle");
    boolean titleVisible = (message != null);
    // && animationProperties.get("title", "hidden") == Boolean.FALSE);
    if (titleVisible) {

      // create header text
      header = installText("title", message, new Coordinates(20, 35));

      // create header box
      RectProperties rectProps = new RectProperties();
      rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, animationProperties
          .get("headerRect", AnimationPropertiesKeys.DEPTH_PROPERTY));
      rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
          animationProperties.get("headerRect",
              AnimationPropertiesKeys.FILLED_PROPERTY));
      rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
          (Color) animationProperties.get("headerRect",
              AnimationPropertiesKeys.FILL_PROPERTY));
      lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header,
          "SE"), "headerRect", null, rectProps);
      primitiveProps.put("headerRect", rectProps);
    }
  }

  /**
   * generates the algorithm description
   */
  protected void createAlgorithmDescription() {
    // show algorithm description
    String message = translator.translateMessage("animDesc");
    if (message != null) {
      // Object pp = primitiveProps.get("title");
      descriptionHeader = lang.newText(new Coordinates(20, 80), message,
          "descrHd", null, (TextProperties) primitiveProps.get("title"));
      description = installCodeBlock("description", "descr", new Offset(0, 30,
          descriptionHeader, AnimalScript.DIRECTION_SW));
      lang.nextStep();
    }
  }

  /**
   * generate the animation start: title and author info, header, intro, pseudo
   * code etc.
   */
  protected void createAnimationHeader() {
    // create the title
    createTitle();

    // create the algorithm's description (if any)
    createAlgorithmDescription();

    // hide description and header
    if (descriptionHeader != null)
      descriptionHeader.hide();
    if (description != null)
      description.hide();
  }

  /**
   * Display the number ofassignments and comparisons (in total)
   */
  protected void createNrStepsDisplay() {
    // // show the appropriate message for the algorithm
    // String message = translator.translateMessage("nrSteps",
    // new Integer[] {new Integer(nrAssignments),
    // new Integer(nrComparisons) });
    // nrStepsText = lang.newText(new Offset(0, 30, code,
    // AnimalScript.DIRECTION_SW),
    // message, "nrSteps", null,
    // (TextProperties)primitiveProps.get("title"));
    // lang.nextStep();
  }

  protected void createComplexityDisplay() {
    complexity = installCodeBlock("complexity", "complexity", new Offset(0, 30,
        header, AnimalScript.DIRECTION_SW));

    lang.nextStep();
    if (complexity != null)
      complexity.hide();
  }

  /**
   * may include the number of steps taken and a complexity analysis of the
   * algorithm.
   * 
   */
  protected void wrapUpAnimation() {
    // show the number of assignments and comparisons performed
    createNrStepsDisplay();

    // hide the number of steps, the array, and the source/pseudo code
    hideNrStepsArrayCode();

    // show the comments about complexity (if any)
    createComplexityDisplay();

    // show ad for web page
    lang.newText(new Offset(0, 50, header, AnimalScript.DIRECTION_SW),
        translator.translateMessage("adForURL"), "adForURL", null,
        (TextProperties) primitiveProps.get("title"));
  }

  /**
   * hides the array, code, and number of steps taken from the display
   */
  protected void hideNrStepsArrayCode() {
    // now hide the nr of steps, the underlying code and
    // show the complexity analysis
    if (nrStepsText != null)
      nrStepsText.hide();
    if (code != null)
      code.hide();
    if (array != null)
      array.hide();

    if (nrAssBox != null)
      nrAssBox.hide();
    if (nrAssLabel != null)
      nrAssLabel.hide();
    if (nrCompBox != null)
      nrCompBox.hide();
    if (nrCompLabel != null)
      nrCompLabel.hide();
  }

  /**
   * increment the number of assignments by 1
   */
  protected void incrementNrAssignments() {
    incrementNrAssignments(1);
  }

  /**
   * increment the number of assignments by <em>howMany</em>
   * 
   * @param howMany
   *          determines how many comparisons have taken place
   */
  protected void incrementNrAssignments(int howMany) {
    nrAssignments += howMany;
    if (nrAssBox != null)
      nrAssBox.moveBy("translate #2", howMany * 2, 0, null, null);
  }

  /**
   * increment the number of comparisons by 1
   */
  protected void incrementNrComparisons() {
    incrementNrComparisons(1);
  }

  /**
   * increment the number of comparisons by <em>howMany</em>
   * 
   * @param howMany
   *          determines how many comparisons have taken place
   */
  protected void incrementNrComparisons(int howMany) {
    nrComparisons += howMany;
    if (nrCompBox != null)
      nrCompBox.moveBy("translate #2", howMany * 2, 0, null, null);
  }

  /**
   * Install the display of the array
   * 
   * @param key
   *          the key for retrieving the array's properties
   * @return the IntArray instance
   */
  protected StringArray installStringArray(String key) {
    // create and install array
    String[] arrayData = (String[]) primitives.get(key);
    ArrayProperties iap = new ArrayProperties();
    iap.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.COLOR_PROPERTY));
    iap.set(AnimationPropertiesKeys.FILL_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.FILL_PROPERTY));
    iap.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    iap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, animationProperties
        .get(key, AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
    iap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, animationProperties
        .get(key, AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    iap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, animationProperties
        .get(key, AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
    StringArray stringArray = lang.newStringArray(new Coordinates(30, 150),
        arrayData, "array", null, iap);
    return stringArray;
  }

  /**
   * Installs a marker on an array at a given index
   * 
   * @param key
   *          the key for retrieving the marker's properties
   * @param baseArray
   *          the array the marker points to
   * @param initialPosition
   *          the initial position of the marker
   * @return
   */
  protected ArrayMarker installArrayMarker(String key,
      ArrayPrimitive baseArray, int initialPosition) {
    ArrayMarkerProperties amp = new ArrayMarkerProperties();
    amp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.COLOR_PROPERTY));
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.LABEL_PROPERTY));
    amp.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.DEPTH_PROPERTY));
    amp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.HIDDEN_PROPERTY));
    if (key.equals("iMarker"))
      amp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
    if (key.equals("jMarker"))
      amp.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true);
    // amp.set(AnimationPropertiesKeys.FWARROW_PROPERTY,
    // true);
    // amp.set(AnimationPropertiesKeys.BWARROW_PROPERTY,
    // false);
    ArrayMarker aMarker = lang.newArrayMarker(baseArray, initialPosition, key,
        null, amp);
    return aMarker;
  }

  /**
   * formats the given text as a source code
   * 
   * @param sourceCode
   *          the source code instance to which text should be added
   * @param key
   *          the key for retrieving the data
   */
  private void formatText(SourceCode sourceCode, String key) {
    // int lineNo = -1,
    int indentationLevel = 0;
    String message = null;
    String filename = translator.translateMessage(key + ".filename");
    StringBuilder sb = new StringBuilder(32337);
    String indenter = translator.translateMessage(key + ".level");
    int spacesPerLevel = new Integer(indenter).intValue();
    String labelChar = translator.translateMessage(key + ".labelSymbol");
    int startPos, endPos;
    String lineLabel = null;
    if (labelsToLines == null)
      labelsToLines = new HashMap<String, Integer>(47);
    if (filename != null) {
      try {
        InputStream f = ResourceLocator.getResourceLocator().getResourceStream(
            filename);

        // File f = new File(filename);
        // FileInputStream fis = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(f));
        while ((message = br.readLine()) != null) {
          sb.append(message).append(MessageDisplay.LINE_FEED);
          // lineNo++;
          indentationLevel = 0;
          lineLabel = null;
          while (indentationLevel < message.length()
              && message.charAt(indentationLevel) == ' ')
            indentationLevel++;
          startPos = indentationLevel;
          endPos = message.indexOf(labelChar);
          if (endPos != -1)
            // extract label
            lineLabel = message.substring(endPos + 1, message.length()).trim();
          else {
            endPos = message.indexOf(CODE_LABEL);
            if (endPos != -1) {
              lineLabel = message.substring(endPos + CODE_LABEL.length() + 2,
                  message.indexOf("\")", endPos));
            } else
              endPos = message.length();
            // labelsToLines.put(label, new Integer(lineNo));
          }
          // endPos = message.indexOf(labelChar);
          // if (endPos == -1)
          // endPos = message.length();
          // else {
          // // extract label
          // lineLabel = message.substring(endPos+1,
          // message.length()).trim();
          // // labelsToLines.put(label, new Integer(lineNo));
          // }
          sourceCode.addCodeLine(message.substring(startPos, endPos).trim(), // substring(indentationLevel),
              lineLabel, // sourceCode.getName() +lineNo,
              indentationLevel / spacesPerLevel, null);
        }
        f.close();
        br.close();
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  protected Text installText(String key, String message, Node location,
      TextProperties textProps) {
    boolean textHidden = ((Boolean) animationProperties.get(key,
        AnimationPropertiesKeys.HIDDEN_PROPERTY)).booleanValue();
    if (textHidden || message == null)
      return null;
    primitiveProps.put(key, textProps);
    Text aText = lang.newText(location, message, key, null, textProps);
    return aText;
  }

  protected Text installText(String key, String message, Node location) {

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        (Color) animationProperties.get(key,
            AnimationPropertiesKeys.COLOR_PROPERTY));
    textProps
        .set(AnimationPropertiesKeys.CENTERED_PROPERTY, animationProperties
            .get(key, AnimationPropertiesKeys.CENTERED_PROPERTY));
    Font userFont = (Font) animationProperties.get(key,
        AnimationPropertiesKeys.FONT_PROPERTY);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(userFont.getFamily(), Font.BOLD, 20));
    textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
        animationProperties.get(key, AnimationPropertiesKeys.DEPTH_PROPERTY));
    return installText(key, message, location, textProps);
  }

  protected SourceCode installCodeBlock(String key, String codeName,
      Node location) {
    // install the source code (if any)
    boolean codeHidden = ((Boolean) animationProperties.get(key,
        AnimationPropertiesKeys.HIDDEN_PROPERTY)).booleanValue();
    // if (codeHidden)
    // return null;
    SourceCodeProperties codeProps = new SourceCodeProperties();
    codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        (Color) animationProperties.get(key,
            AnimationPropertiesKeys.COLOR_PROPERTY));
    codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        (Color) animationProperties.get(key,
            AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    Font userFont = (Font) animationProperties.get(key,
        AnimationPropertiesKeys.FONT_PROPERTY);
    int fontProps = Font.PLAIN;
    if (animationProperties.get(key, AnimationPropertiesKeys.BOLD_PROPERTY) == Boolean.TRUE)
      fontProps += Font.BOLD;
    if (animationProperties.get(key, AnimationPropertiesKeys.ITALIC_PROPERTY) == Boolean.TRUE)
      fontProps += Font.ITALIC;
    codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(userFont.getFamily(), fontProps, 16));
    codeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    DisplayOptions display = null;
    if (codeHidden)
      display = new Hidden();
    codeProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, new Boolean(
        codeHidden));
    SourceCode aCode = lang.newSourceCode(location, codeName, display,
        codeProps);
    formatText(aCode, key);
    return aCode;
  }

  public abstract Primitive installAdditionalComponents(String arrayKey,
      String codeKey, String codeName, int dx, int dy);

  // install the array
  // array = installIntArray(arrayKey);
  // // install the source code (if any)
  // code = installCodeBlock(codeKey, codeName,
  // new Offset(dx, dy, array, AnimalScript.DIRECTION_SW));

  protected void setUpDefaultElements(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims, String arrayKey, String codeKey,
      String codeName, int dx, int dy) {
    setParameters(props, prims);
    setUpAnimation();

    // create animation header (title etc.)
    // also includes description
    createAnimationHeader();

    // Primitive alignmentPrimitive =
    installAdditionalComponents(arrayKey, codeKey, codeName, dx, dy);
    //
    // // install the source code (if any)
    // code = installCodeBlock(codeKey, codeName,
    // new Offset(dx, dy, array, AnimalScript.DIRECTION_SW));

    // clear counters in this instance!
    nrComparisons = 0;
    nrAssignments = 0;
    //
    // // create counter boxes
    // createCounterBlocks(new Offset(80, -80, alignmentPrimitive,
    // AnimalScript.DIRECTION_SE));// ,
  }

  /**
   * generate returns the generated String. This method is called by the
   * Generator GUI which passes the AnimationPropertiesContainer and the
   * Hashtable with Primitives with the values that the user changed.
   * 
   * @param props
   *          The AnimationPropertiesContainer with all the values set.
   * @param prims
   *          The Hashtable with all the Primitives set.
   * @return The generated String.
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    setUpDefaultElements(props, prims, "intArray", "code", "code", 0, 20);
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

}
