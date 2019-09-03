package generators.cryptography.caesarcipher;

import generators.AnnotatedAlgorithm;
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
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Alexander Heinz
 * @version 1.0 2010-25-07
 * 
 */
public class CaesarChiffreAH extends AnnotatedAlgorithm implements
    generators.framework.Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  /**
   * Default constructor
   */
  public CaesarChiffreAH() {
  }

  private static final String DESCRIPTION = "Der Caesar-Chiffre geht das Eingabewort durch und verschiebt dabei jeden "
                                              + "Buchstaben um einen konstanten Verschiebewert. Beim Decodieren wird dieser "
                                              + "Vorgang rückgängig gemacht, indem die Buchstaben 'zurückgeschoben' werden.";

  private static final String SOURCE_CODE = "Codieren:"
                                              + "\nFuer jedes Zeichen:"
                                              + "\n1. Zeiger auf aktuelles Zeichen setzen"
                                              + "\n2. Zeichen um Schiebewert nach hinten verschieben"
                                              + "\nDecodieren"
                                              + "\nFuer jedes Zeichen:"
                                              + "\n1. Zeiger auf aktuelles Zeichen setzen"
                                              + "\n2. Zeichen um Schiebewert nach vorne verschieben";

  /**
   * initialize primitives
   * 
   * @param a
   *          the array to be sorted
   * @param schieb
   *          moving value
   * @param arrayProps
   *          properties of the array
   * @param scProps
   *          properties of the sourcecode
   */
  public void init(String[] a, int schieb, ArrayProperties arrayProps,
      SourceCodeProperties scProps) {
    super.init();

    // header text
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    lang.newText(new Coordinates(70, 30), "CaesarChiffre", "header", null,
        textProps);
    // rectangle around header
    lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header",
        "SE"), "hRect", null);

    // create array to encrypt
    StringArray ia = lang.newStringArray(new Coordinates(100, 100), a, "arr",
        null, arrayProps);

    // create the sourcecode
    sourceCode = lang.newSourceCode(new Coordinates(10, 200), "listSource",
        null, scProps);

    lang.nextStep("Uebersicht");

    // parse the annotated sourcecode
    parse();

    // Start caesar-chiffre
    crypt(ia, sourceCode, schieb);

  }

  /**
   * crypt: encrypts and then decrypts the given word
   * 
   * @param array
   *          the StringArray to be encrypted/decrypted
   * @param codeSupport
   *          the underlying code instance
   */
  private void crypt(StringArray array, SourceCode codeSupport, int schieb)
      throws LineNotExistsException {

    // create ArrayMarker
    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ArrayMarker iMarker = lang.newArrayMarker(array, 0, "pointer", null,
        arrayIMProps);
    lang.newText(new Offset(0, 5, "arr", "SW"), "Verschiebung: " + schieb,
        "schiebText", null);

    exec("codetitle");
    lang.nextStep("Codieren");

    // loop for encrypting word
    for (int i = 0; i < array.getLength(); i++) {
      exec("codemark");
      // move marker to letter to encrypt
      iMarker.move(i, new TicksTiming(100), new TicksTiming(100));
      lang.nextStep();

      exec("code");

      // encrypt letter
      array.put(i, move(array.getData(i), schieb), null, null);
      // highlight encrypted cell
      array.highlightCell(i, null, null);
      lang.nextStep();
    }

    // loop for decrypting word
    exec("decodetitle");
    lang.nextStep("Decodieren");

    for (int i = 0; i < array.getLength(); i++) {
      exec("decodemark");
      iMarker.move(i, new TicksTiming(100), new TicksTiming(100));
      lang.nextStep();

      exec("decode");

      // decrypt letter
      array.put(i, move(array.getData(i), -schieb), null, null);
      // unhighlight decrypted cell
      array.unhighlightCell(i, null, null);
      lang.nextStep();
    }
  }

  // decrypts/encrypts given letter
  protected String move(String in, int distance) {
    // letter > 'z'
    int distance2 = distance;
    if (in.toCharArray()[0] + distance2 > 122)
      distance2 = distance2 - 26;
    // letter < 'a'
    if (in.toCharArray()[0] + distance2 < 97)
      distance2 = distance2 + 26;
    // return decrypted/encrypted letter
    return Character.toString((char) (in.toCharArray()[0] + distance2));
  }

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  public String getName() {
    return "Caesar Chiffre";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    lang = new AnimalScript("Caesar-Chiffre", "Alexander Heinz", 640, 480);
    lang.setStepMode(true);
    init((String[]) arg1.get("array"),
        ((Number) arg1.get("schiebewert")).intValue(),
        (ArrayProperties) arg0.get(0), (SourceCodeProperties) arg0.get(1));
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Caesar-Verschl\u00fcsselung";
  }

  @Override
  public String getAnimationAuthor() {
    return "Alexander Heinz";
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
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {

  }

  @Override
  public String getAnnotatedSrc() {
    return "Codieren                                         @label(\"codetitle\")\n"
        + "Fuer jedes Zeichen:                                  @label(\"unused1\")\n"
        + "1. Zeiger auf aktuelles Zeichen setzen               @label(\"codemark\")\n"
        + "2. Zeichen um Schiebewert nach hinten verschieben    @label(\"code\")\n"
        + "Decodieren                                           @label(\"decodetitle\")\n"
        + "Fuer jedes Zeichen:                                  @label(\"unused2\")\n"
        + "1. Zeiger auf aktuelles Zeichen setzen               @label(\"decodemark\")\n"
        + "2. Zeichen um Schiebewert nach vorne verschieben     @label(\"decode\")\n";
  }
}
