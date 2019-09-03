package generators.hashing;

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
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * 
 * @author Christian Rosskopf
 * 
 */
public class QuadraticProbingWithAnnotations extends AnnotatedAlgorithm
    implements Generator {

  // Fixed Variables
  // private final String algoName = "Hashing - Quadratic Probing";
  // private final String author = "Christian Rosskopf";

  // String for pseudocode display

  // the used modulo
  private int             size;
  // items to store
  private int[]           numbers;
  // some text for initialization of the animal gui
  private String[]        bucketText;
  // stores if a bucket is empty: true = empty
  private boolean         bucket[];

  // private Language lang;

  // displayed objects and their properties
  private ArrayProperties numberArrayProps;
  private ArrayProperties bucketArrayProps;
  private IntArray        items;
  private StringArray     buckets;
  // private SourceCode sc;
  // private Text textNumbers;
  // private Text textHash;
  private Text            status;
  private Text            varI;
  private Text            varHash;
  private Text            varHash1;
  private Text            roundUp;
  private Text            alternating;
  private ArrayMarker     m1;

  private Timing          shortAfter = new TicksTiming(50);

  // private String calc = "NumberOfCalculatedHashes";

  public QuadraticProbingWithAnnotations() {

  }

  /**
   * Stores the first n items from the given array to the hashmap n equals the
   * selected modulo
   */
  private void hashing() {

    // System.out.println(getAnnotatedSrc());

    exec("title");
    vars.setGlobal("NumberOfCalculatedHashes");

    // find a bucket for every number
    for (int i = 0; i < size && i < numbers.length; i++) {

      System.out.println(String.valueOf(numbers[i]));

      int n = numbers[i];
      status.setText("Find Bucket for " + n, null, null);
      lang.nextStep();
      // calculate the hash
      calculateHash(n);

      items.highlightCell(i, null, null);
      m1.increment(null, shortAfter);
    }
    if (numbers.length > size)
      status.setText("Can't Store all Numbers. Buckets are full!", null, null);
    else
      status.setText("All Numbers Stored", null, null);
  }

  /**
   * Calculate the hash for n use "quadratisches sondieren"
   * 
   * @param n
   *          the number for which a hash is calculated
   */
  private void calculateHash(int n) {

    vars.setGlobal("x");
    vars.setGlobal("hashX");
    exec("computeHash");
    sourceCode.highlight(0, 0, true);

    vars.set("x", String.valueOf(n));
    vars.set("hashX", String.valueOf(n % size));
    status.setText("h(x=" + n + ") = " + n + " mod " + size + " = " + n % size,
        null, null);
    lang.nextStep();

    exec("if1");
    lang.nextStep();
    int position = Integer.valueOf(vars.get("hashX"));
    if (bucket[position]) {
      exec("put");
      sourceCode.highlight(2, 0, true);
      lang.nextStep();

      buckets.highlightCell(n % size, null, shortAfter);
      buckets
          .put(position, String.valueOf(n), new TicksTiming(150), shortAfter);
      lang.nextStep();
      buckets.unhighlightCell(position, null, null);
      bucket[position] = false;
      sourceCode.unhighlight(2);
      sourceCode.unhighlight(3);

    } else {

      exec("else1");
      sourceCode.highlight(2, 0, true);
      buckets.highlightCell(Integer.valueOf(vars.get("hashX")), null,
          shortAfter);

      boolean notfound = true;
      int i = 1;
      int original = Integer.valueOf(vars.get("hashX"));
      int par;

      lang.nextStep();

      exec("computeHash2");
      sourceCode.highlight(4, 0, true);
      sourceCode.highlight(14, 0, true);
      vars.setGlobal("i");
      lang.nextStep();

      while (notfound) {

        buckets.unhighlightCell(original, null, null);

        exec("whileStart");
        vars.setGlobal("i");
        vars.setGlobal("h1");
        vars.setGlobal("h2");
        vars.setGlobal("h3");
        lang.nextStep();

        exec("hash2");
        sourceCode.highlight(6, 0, true);
        sourceCode.highlight(14, 0, true);

        par = (int) Math.pow(-1, i + 1);
        int nextpar = (int) (Math.ceil(i / 2.0) * Math.ceil(i / 2.0));
        int target = ((original + (par * nextpar)));
        while (target < 0)
          target = target + size;
        vars.set("h1", String.valueOf(n % size));
        vars.set("h2", String.valueOf(par));
        vars.set("h3", String.valueOf(nextpar));

        target = target % size;
        // System.out.println("   "+ original + " + " +par +" * " + nextpar
        // +" = "+ target);
        status.setText("Calculating alternative storage position", null, null);
        showBigCalculation(target, i, par, nextpar, size, n % size, n);
        status.setText("h`(x) = " + target, null, null);
        lang.nextStep();

        exec("if2");
        lang.nextStep();
        if (bucket[target]) {

          exec("put2");
          sourceCode.highlight(8, 0, true);
          status.setText("Bucket empty, adding number", null, null);
          buckets.highlightCell(target, null, shortAfter);
          buckets.put(target, String.valueOf(n), new TicksTiming(100),
              shortAfter);
          bucket[target] = false;
          lang.nextStep();

          buckets.unhighlightCell(target, null, null);
          exec("break");
          status.setText("Break: Adding next number", null, null);
          notfound = false;
          lang.nextStep();

          sourceCode.unhighlight(2);
          sourceCode.unhighlight(4);
          sourceCode.unhighlight(6);
          sourceCode.unhighlight(8);
          sourceCode.unhighlight(13);
          sourceCode.unhighlight(14);

        } else {

          status.setText("Bucket not empty, calculate next hash", null, null);
          buckets.highlightCell(target, null, shortAfter);
          // System.out.println("  Try "+ n+ " in " + target);
          i++;

          buckets.unhighlightCell(target, null, null);

          exec("else2");
          sourceCode.highlight(8, 0, true);
          lang.nextStep();

          exec("elseCommand");

          sourceCode.highlight(11, 0, true);
          sourceCode.highlight(13, 0, true);
          lang.nextStep();

          sourceCode.unhighlight(2);
          sourceCode.unhighlight(4);
          sourceCode.unhighlight(6);
          sourceCode.unhighlight(8);
          sourceCode.unhighlight(11);
          sourceCode.unhighlight(13);
          sourceCode.unhighlight(14);
        }
        resetInfos();

      }

    }

  }

  /**
   * Shows the calculation of a rehash in textfields
   * 
   * @param target
   * @param i
   * @param par
   * @param nextpar
   * @param size
   * @param firstPos
   * @param x
   */
  private void showBigCalculation(int target, int i, int par, int nextpar,
      int size, int firstPos, int x) {
    int time = 10;
    varI.setText("i = " + i, null, new TicksTiming(time));
    varHash.setText("h1 = x mod y = " + x + " mod " + size + " = " + firstPos,
        null, new TicksTiming(time));
    alternating.setText("h2 = (-1)^(i+1) = (-1)^(" + (i + 1) + ") = "
        + (int) Math.pow(-1, (i + 1)), null, new TicksTiming(time));
    roundUp.setText("h3 = ( ceiling(i/2) )^2 = " + (int) Math.ceil(i / 2.0)
        + "^2 = " + (int) Math.pow((Math.ceil(i / 2.0)), 2), null,
        new TicksTiming(time));
    lang.nextStep();
    varHash1.setText("h`(x) = h1 + h2*h3 mod y = (" + firstPos + " + " + par
        + "*" + nextpar + ") mod " + size + " = " + target, null,
        new TicksTiming(time));
    vars.declare("int", "hashX" + i, String.valueOf(Integer.valueOf(vars
        .get("h1"))
        + Integer.valueOf(vars.get("h2"))
        * Integer.valueOf(vars.get("h3"))));
  }

  /**
   * Deletes the displayed infos in the textfields
   */
  private void resetInfos() {
    varI.setText("", null, null);
    varHash1.setText("", null, null);
    varHash.setText("", null, null);
    alternating.setText("", null, null);
    roundUp.setText("", null, null);
  }

  @Override
  public String generate(AnimationPropertiesContainer container,
      Hashtable<String, Object> hash) {
    System.out.println("Vars: " + vars != null);
    init();
    System.out.println("Vars: " + vars != null);
    // // Create new Animation
    // lang = new AnimalScript(algoName, author, 640, 480);
    // lang.setStepMode(true);

    // Store Bucket-Array Variables
    AnimationProperties bucketsProps = container.getPropertiesByName("Buckets");
    Color bucketElementColor = (Color) bucketsProps.get("elementColor");
    Color bucketColor = (Color) bucketsProps.get("color");
    Color bucketCellHighlightColor = (Color) bucketsProps.get("cellHighlight");
    System.out.println(bucketCellHighlightColor);

    // Store Number-Array Variables
    AnimationProperties numberProps = container.getPropertiesByName("Numbers");
    Color numberColor = (Color) numberProps.get("color");
    Color numberCellHighlightColor = (Color) numberProps.get("cellHighlight");
    Color numberElementColor = (Color) numberProps.get("elementColor");

    // Store ActivePointer variables
    AnimationProperties acPointerProps = container
        .getPropertiesByName("Active");
    boolean acPointerShort = (Boolean) acPointerProps.get("short");
    boolean acPointerLong = (Boolean) acPointerProps.get("long");
    Color acPointerColor = (Color) acPointerProps.get("color");
    String acPointerLabel = (String) acPointerProps.get("label");

    // Store BucketPointer variables
    AnimationProperties bucketPointerProps = container
        .getPropertiesByName("BucketPointer");
    boolean bucketPointerShort = (Boolean) bucketPointerProps.get("short");
    boolean bucketPointerLong = (Boolean) bucketPointerProps.get("long");
    Color bucketPointerColor = (Color) bucketPointerProps.get("color");

    // Store SourceCodeProperties
    AnimationProperties sourceCodeProperties = container
        .getPropertiesByName("sourceCode");
    boolean sourceCodeBold = (Boolean) sourceCodeProperties.get("bold");
    boolean sourceCodeItalic = (Boolean) sourceCodeProperties.get("italic");
    Color sourceCodeColor = (Color) sourceCodeProperties.get("color");
    Color sourceCodeContext = (Color) sourceCodeProperties.get("contextColor");
    Color sourceCodeHighlight = (Color) sourceCodeProperties
        .get("highlightColor");
    Font sourceCodeFont = (Font) sourceCodeProperties.get("font");

    // Get given values from hash
    // for(String s : hash.keySet()) System.out.println(s);
    numbers = (int[]) hash.get("Integers");
    size = (Integer) hash.get("Modulo");

    // Init variables
    bucket = new boolean[size];
    bucketText = new String[size];
    for (int i = 0; i < size; i++) {
      bucketText[i] = "empty";
      bucket[i] = true;
    }

    // Textblock -> hardcoded properties
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    Text title = lang.newText(new Coordinates(10, 10),
        "Hashing (Quadratisches Sondieren): x mod " + size, "Hashing", null,
        textProps);
    title.changeColor(null, Color.BLACK, null, null);
    title.setFont(new Font("Monospaced", Font.BOLD, 18), null, null);

    // Number-Array-Properties
    numberArrayProps = new ArrayProperties();
    // User defined properties
    numberArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        numberElementColor);
    numberArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        numberCellHighlightColor);
    numberArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, numberColor);
    // Hard-Coded Porperties
    numberArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    numberArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    items = lang.newIntArray(new Offset(20, 70, title,
        AnimalScript.DIRECTION_SW), numbers, "items", null, numberArrayProps);

    // Properties for the Bucket-Array
    bucketArrayProps = new ArrayProperties();
    // User defined properties
    bucketArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        bucketElementColor);
    bucketArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        bucketCellHighlightColor);
    bucketArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, bucketColor);
    // Hard-Coded Porperties
    bucketArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    bucketArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    buckets = lang.newStringArray(new Offset(00, 70, items,
        AnimalScript.DIRECTION_SW), bucketText, "hash", null, bucketArrayProps);

    // Create Pointer for Buckets
    ArrayMarkerProperties amp1 = new ArrayMarkerProperties();
    amp1.set(AnimationPropertiesKeys.COLOR_PROPERTY, bucketPointerColor);
    amp1.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, bucketPointerShort);
    amp1.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, bucketPointerLong);
    for (int i = 0; i < size; i++) {
      amp1.set(AnimationPropertiesKeys.LABEL_PROPERTY, String.valueOf(i));
      ArrayMarker m = lang.newArrayMarker(buckets, i, "m" + i, null, amp1);
      m.setName("m" + i);
    }

    // Create ActivePointer
    ArrayMarkerProperties amp2 = new ArrayMarkerProperties();
    System.out.println("Label: " + acPointerLabel);
    System.out.println("Bool:  " + acPointerShort);
    System.out.println("Color: " + acPointerColor);
    amp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, acPointerColor);
    amp2.set(AnimationPropertiesKeys.LABEL_PROPERTY, acPointerLabel);
    amp2.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, acPointerShort);
    amp2.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, acPointerLong);
    m1 = lang.newArrayMarker(items, 0, "active", null, amp2);

    // DEBUG-OUTPUT: Error with short and long markers
    // for(String debug : m1.getProperties().getAllPropertyNames())
    // System.out.println(debug+": "+ m1.getProperties().get(debug));

    // more textblocks
    lang.newText(new Offset(30, 0, items, AnimalScript.DIRECTION_E), "Numbers",
        "numbers", null, textProps);
    lang.newText(new Offset(30, 0, buckets, AnimalScript.DIRECTION_E),
        "Hashbuckets", "buckets", null, textProps);
    status = lang.newText(
        new Offset(20, 50, buckets, AnimalScript.DIRECTION_S), "", "status",
        null, textProps);

    // Codeblock --> Properties are hardcoded
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        sourceCodeContext);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sourceCodeFont);
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        sourceCodeHighlight);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourceCodeColor);
    scProps.set(AnimationPropertiesKeys.BOLD_PROPERTY, sourceCodeBold);
    scProps.set(AnimationPropertiesKeys.ITALIC_PROPERTY, sourceCodeItalic);

    sourceCode = lang.newSourceCode(new Offset(0, 75, buckets,
        AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
    parse();
    // for(int i = 0; i<source.length; i++ ) sc.addCodeLine(source[i], null, 0,
    // null);

    // and even more textblocks
    varI = lang.newText(
        new Offset(20, 0, sourceCode, AnimalScript.DIRECTION_NE), "", "i",
        null, textProps);
    varHash1 = lang.newText(new Offset(0, 0, varI, AnimalScript.DIRECTION_SW),
        "", "hash1", null, textProps);
    varHash = lang.newText(
        new Offset(0, 0, varHash1, AnimalScript.DIRECTION_SW), "", "hash",
        null, textProps);
    alternating = lang.newText(new Offset(0, 0, varHash,
        AnimalScript.DIRECTION_SW), "", "alternating", null, textProps);
    roundUp = lang.newText(new Offset(0, 0, alternating,
        AnimalScript.DIRECTION_SW), "", "roundup", null, textProps);

    // Start producing the animation
    hashing();
    // printToFile(lang);
    return lang.getAnimationCode();
  }

  @Override
  public String getAlgorithmName() {
    return "Hashing with quadratic probing";
  }

  @Override
  public String getAnimationAuthor() {
    return "Christian Rosskopf";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "The Animation shows how numbers are sorted into hashbuckets using quadratic probing.";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
  }

  @Override
  public String getName() {
    return "Hashing - Quadratic Probing with Annotations ";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    // System.out.println("Vars: "+vars != null);
    super.init();
    // System.out.println("Vars: "+vars != null);
  }

  @Override
  public String getAnnotatedSrc() {
    return "Hashing 														@label(\"title\") @declare(\"int\", \"NumberOfCalculatedHashes\", \"0\") \n"
        + " Compute Hash: h(x) = x mod y;								@label(\"computeHash\") @openContext @declare(\"int\", \"x\") @declare(\"int\", \"hashX\") @inc(\"NumberOfCalculatedHashes\") \n"
        + " if ( Bucket[h(x)] == empty ) { 								@label(\"if1\") \n "
        + "    Add x to Bucket;											@label(\"put\") @closeContext @discard(\"x\") @discard(\"hashX\")  \n"
        + " } else {														@label(\"else1\") @highlight(\"else1End\")  \n"
        + "    Compute Alternative Positions: 							@label(\"computeHash2\") @declare(\"int\", \"i\", \"1\")  \n"
        + "    while (true) { 											@label(\"whileStart\")  @openContext  @highlight(\"whileEnd\") @declare(\"int\", \"h1\") @declare(\"int\", \"h2\") @declare(\"int\", \"h3\") \n"
        + "	     h'(x) = (h(x) + -1^(i+1) * ( ceiling(i/2) )^2 ) mod y 	@label(\"hash2\") @inc(\"NumberOfCalculatedHashes\") \n"
        + "      if (Bucket[h`(x)] == empty) { 							@label(\"if2\")\n"
        + "         Add x to Bucket; 									@label(\"put2\")\n"
        + "         break; 												@label(\"break\") @discard(\"x\") @discard(\"hashX\") @closeContext \n"
        + "      } else { 												@label(\"else2\") @highlight(\"else2End\")\n"
        + "         i=i+1; 												@label(\"elseCommand\") @inc(\"i\") \n"
        + "      } 														@label(\"else2End\") \n"
        + "    } 														@label(\"whileEnd\") @closeContext @discard(\"i\") \n"
        + " }															@label(\"else1End\") \n";

  }

}
