package generators.hashing;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author Christian Glaser c_glaser@rbg.informatik.tu-darmstadt.de, Dmitrij
 *         Burlak Dima85@web.de
 * @version 1.0 2010-04-28
 * 
 */

public class HashingLinearProbing extends AnnotatedAlgorithm implements Generator {

  /**
   * The concrete language object used for creating output
   */
  // private Language lang;

  private ArrayMarkerProperties arrayMProps;

  private ArrayMarkerProperties array2MProps;

  private ArrayProperties       arrayProps;

  private SourceCodeProperties  scProps;

  private TextProperties        tProps;

  private ArrayMarkerUpdater    amuI, amuJ;

  private String                comp          = "Compares";
  private String                assi          = "Assignments";

  private Timing                defaultTiming = new TicksTiming(60);

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */
  HashingLinearProbing() {
    arrayProps = new ArrayProperties();
    scProps = new SourceCodeProperties();
    arrayMProps = new ArrayMarkerProperties();
    array2MProps = new ArrayMarkerProperties();
    tProps = new TextProperties();
  }

  private static final String DESCRIPTION = "Hashing: Divisionrest Methode mit linearer Sondierung";

  private static final String SOURCE_CODE = "input(int[] hashArray, int[] inputArray){	"
                                              + "  int hash, i, x;										"
                                              + "  for (i=0; i< inputArray.getLength();	i++){			"
                                              + "    x = inputArray[i]								"
                                              + "    hash = x % hashArray.getLength();				"
                                              + "    while(hashArray[hash] != 0)						"
                                              + "      hash = (hash + 1) % hashArray.getLength();	"
                                              + "    hashArray[hash] = x;							"
                                              + "	 }													"
                                              + "}														";

  public void standartProperties() {
    // set the visual properties for the array
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps
        .set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.orange);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.lightGray);

    // set the visual properties for the source code
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 15));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        "Einzufuegendes Element");
    arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    array2MProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "hash");
    array2MProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 25));
  }

  /**
   * initialize with the int array passed in
   * 
   * @param inputArray
   *          the array to be sorted
   * @param hashSize
   *          the size of the hash table
   */
  public void init(int[] inputArray, int hashSize) {
    super.init();

    lang.newText(new Coordinates(200, 20),
        "Hashing: Divisionsrestmethode mit linearer Sondierung", "header",
        null, tProps);
    // now, create the IntArray object, linked to the properties
    IntArray intArray = lang.newIntArray(new Coordinates(200, 150), inputArray,
        "Array1", null, arrayProps);

    int[] hashArray = new int[hashSize];
    for (int i = 0; i < hashSize; i++)
      hashArray[i] = 0;

    IntArray intHashArray = lang.newIntArray(new Coordinates(200, 300),
        hashArray, "Array2", null, arrayProps);

    ArrayMarker iMarker = lang.newArrayMarker(intArray, 0, "zeiger", null,
        arrayMProps);
    amuI = new ArrayMarkerUpdater(iMarker, null, defaultTiming,
        intArray.getLength() - 1);

    ArrayMarker iMarker2 = lang.newArrayMarker(intHashArray, 0, "zeiger2",
        null, array2MProps);
    amuJ = new ArrayMarkerUpdater(iMarker2, null, defaultTiming,
        intHashArray.getLength() - 1);

    // now, create the source code entity
    sourceCode = lang.newSourceCode(new Coordinates(50, 350), "sumupCode",
        null, scProps);

    // setup complexity
    vars.declare("int", comp);
    vars.setGlobal(comp);
    vars.declare("int", assi);
    vars.setGlobal(assi);

    Text text = lang.newText(new Coordinates(500, 300), "...", "complexity",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Compares: ");
    tu.addToken(vars.getVariable(comp));
    tu.addToken(" - Assignments: ");
    tu.addToken(vars.getVariable(assi));
    tu.update();

    parse();

    lang.nextStep();

    try {
      // Start quicksort
      Hash(intArray, sourceCode, intHashArray, iMarker, iMarker2);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  /**
   * Hashing mit linearer Sondierung
   * 
   * @param array
   *          the IntArray to be sorted
   * @param codeSupport
   *          the underlying code instance
   */
  private void Hash(IntArray intArray, SourceCode codeSupport,
      IntArray hashArray, ArrayMarker iMarker, ArrayMarker iMarker2)
      throws LineNotExistsException {

    exec("header");
    lang.nextStep();

    exec("declareVars");
    amuI.setVariable(vars.getVariable("i"));
    amuJ.setVariable(vars.getVariable("hash"));
    lang.nextStep();

    int hashSize = hashArray.getLength();

    exec("forInit");
    lang.nextStep();

    exec("forComp");
    lang.nextStep();

    while (Integer.parseInt(vars.get("i")) < intArray.getLength()) {

      exec("actX");
      vars.set("x",
          String.valueOf(intArray.getData(Integer.parseInt(vars.get("i")))));
      lang.nextStep();

      hashArray.unhighlightCell(Integer.parseInt(vars.get("hash")), null, null);
      exec("newHash");
      vars.set("hash",
          String.valueOf(Integer.parseInt(vars.get("x")) % hashSize));
      hashArray.highlightCell(Integer.parseInt(vars.get("hash")), null,
          defaultTiming);
      lang.nextStep();

      exec("while");
      while (hashArray.getData(Integer.parseInt(vars.get("hash"))) != 0) {
        lang.nextStep();

        hashArray.unhighlightCell(Integer.parseInt(vars.get("hash")), null,
            null);
        exec("sond");
        vars.set("hash",
            String.valueOf((Integer.parseInt(vars.get("hash")) + 1) % hashSize));
        hashArray.highlightCell(Integer.parseInt(vars.get("hash")), null,
            defaultTiming);
        lang.nextStep();
      }
      lang.nextStep();

      exec("slotFound");
      hashArray.put(Integer.parseInt(vars.get("hash")),
          Integer.parseInt(vars.get("x")), null, defaultTiming);
      lang.nextStep();

      exec("forInc");
      lang.nextStep();
      exec("forComp");
      lang.nextStep();
    }
  }

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  public String getName() {
    return "Hashing: Divrest und Linearsondierung";
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

    int[] arrayData = (int[]) primitives.get("inputArray");

    int hashSize = (Integer) primitives.get("hashSize");

    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        props.get("array", AnimationPropertiesKeys.FILL_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        props.get("array", AnimationPropertiesKeys.FILLED_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        props.get("array", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        props.get("array", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        props.get("array", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, props.get(
        "sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    scProps.set(AnimationPropertiesKeys.SIZE_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.SIZE_PROPERTY));
    scProps.set(AnimationPropertiesKeys.BOLD_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.BOLD_PROPERTY));

    tProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY,
        props.get("header", AnimationPropertiesKeys.CENTERED_PROPERTY));
    tProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("header", AnimationPropertiesKeys.COLOR_PROPERTY));
    tProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        props.get("header", AnimationPropertiesKeys.FONT_PROPERTY));

    arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        props.get("arrayMarker1", AnimationPropertiesKeys.LABEL_PROPERTY));
    arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("arrayMarker1", AnimationPropertiesKeys.COLOR_PROPERTY));

    arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
        props.get("arrayMarker2", AnimationPropertiesKeys.LABEL_PROPERTY));
    arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("arrayMarker2", AnimationPropertiesKeys.COLOR_PROPERTY));

    init(arrayData, hashSize);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Hashing mit linearer Sondierung";
  }

  @Override
  public String getAnimationAuthor() {
    return "Christian Glaser, Dmitrij Burlak";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
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
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public String getAnnotatedSrc() {
    return "input(int[] hashArray, int[] inputArray){				@label(\"header\")\n"
        + "  int hash, i, x;										@label(\"declareVars\") @declare(\"int\", \"hash\") @declare(\"int\", \"i\") @declare(\"int\", \"x\")\n"
        + "	 for (i = 0; 											@label(\"forInit\") @set(\"i\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "	i < inputArray.getLength();						@label(\"forComp\") @continue @inc(\""
        + comp
        + "\")\n"
        + "	i++) {											@label(\"forInc\") @continue @inc(\"i\")\n"
        + "    x = inputArray[i]								@label(\"actX\") @inc(\""
        + assi
        + "\") \n"
        + "	   hash = x % hashArray.getLength();				@label(\"newHash\") @inc(\""
        + assi
        + "\") \n"
        + "    while (hashArray[hash] != 0)						@label(\"while\") @inc(\""
        + comp
        + "\")\n"
        + "      hash = (hash + 1) % hashArray.getLength();	@label(\"sond\") @inc(\""
        + assi
        + "\") \n"
        + "    hashArray[hash] = x;							@label(\"slotFound\") @inc(\""
        + assi
        + "\") \n"
        + "	 }													@label(\"endFor\") \n"
        + "}														@label(\"end\") \n";
  }

}
