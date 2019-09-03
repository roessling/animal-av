package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class CaesarCipherFB implements Generator {
  private String[]             alphabet     = { "A", "B", "C", "D", "E", "F",
      "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
      "U", "V", "W", "X", "Y", "Z"         };
  private Language             lang;
  private int                  shift        = 3;
  private SourceCode           src;
  private String[]             plainContent = { "A", "N", "I", "M", "A", "L" };
  private String[]             cipherContent;
  private StringArray          plain;
  private StringArray          cipher;
  private SourceCode           titel;
  private SourceCodeProperties titelProps;
  private SourceCodeProperties sourceProps;
  private ArrayMarker          markerPlai;
  private ArrayMarker          markerCiph;
  private ArrayMarkerProperties markerPlainProps, markerCipherProps;
  private ArrayProperties       plainProps;
  private ArrayProperties       cipherProps;

  public CaesarCipherFB() {
    lang = new AnimalScript("CaesarCipher", "Fehmi Belhadj", 0, 0);
    lang.setStepMode(true);
  }

  /**
   * initialize the properties of elements which should be shown in animation
   */
  public void init() {
    initCipher();
    plainProps = new ArrayProperties();
    plainProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    plainProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    plainProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    cipherProps = new ArrayProperties();
    cipherProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cipherProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    titelProps = new SourceCodeProperties();
    titelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 18));
    titel = lang.newSourceCode(new Coordinates(5, 10), "title", null,
        titelProps);
    titel.addCodeLine("CAESAR CIPHER", "first", 0, null);
    sourceProps = new SourceCodeProperties();
    sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 14));
    src = lang.newSourceCode(new Offset(5, 5, "title", "south"), "sourceCode",
        null, sourceProps);
    setSource();
    plain = lang.newStringArray(new Offset(70, 20, "sourceCode", "southeast"),
        plainContent, "plain", null, plainProps);
    cipher = lang.newStringArray(
        new Offset(70, 120, "sourceCode", "southeast"), cipherContent,
        "output", null, cipherProps);
    markerPlainProps = new ArrayMarkerProperties();
    markerPlainProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "plain text");
    markerCipherProps = new ArrayMarkerProperties();
    markerCipherProps
        .set(AnimationPropertiesKeys.LABEL_PROPERTY, "cipher text");
  }

  /**
	 * 
	 */
  private void setSource() {
    src.addCodeLine("FOR EACH ELEMENT OF TEXT ", "first", 0, null);
    src.addCodeLine("FOR I = 0 TO SHIFT", "second", 1, null);
    src.addCodeLine("TAKE THE NEXT LETTER", "third", 2, null);
    src.addCodeLine("SET NEW ELEMENT ", "fourth", 1, null);
    src.addCodeLine("RETURN ENCRYPTED TEXT", "fifth", 0, null);
    src.addCodeLine("", "sixth", 0, null);
    src.addCodeLine("", "7.", 0, null);
    src.addCodeLine("", "7.1", 0, null);
    src.addCodeLine("shift = ", "8.", 0, null);
    src.addCodeElement(Integer.toString(shift), "9.", 0, null);

  }

  /**
	 * 
	 */
  private void initCipher() {
    cipherContent = new String[plainContent.length];
    for (int i = 0; i < plainContent.length; i++)
      cipherContent[i] = plainContent[i];

  }

  /**
   * encrypts the parameter
   * 
   * @param text
   * @return the encrpyted text as a String[]
   */
  public String[] encrypt(String[] text) {
    boolean doesNotExist = true;
    plainContent = text;
    init();
    lang.nextStep();
    src.highlight(0);
    markerPlai = lang.newArrayMarker(plain, 0, "markerplain", null,
        markerPlainProps);
    markerCiph = lang.newArrayMarker(cipher, 0, "markercipher", null,
        markerCipherProps);
    lang.nextStep();
    src.unhighlight(0);
    int indexOfChar = 0;
    for (int i = 0; i < text.length; i++) {
      markerPlai.move(i, null, null);
      markerCiph.move(i, null, null);

      for (int j = 0; j < alphabet.length; j++) {
        if (alphabet[j].toUpperCase().compareTo(text[i].toUpperCase()) == 0) {
          indexOfChar = j;
          doesNotExist = false;
          break;
        }
      }
      src.unhighlight(0);
      if (doesNotExist) {
        lang.nextStep();
        src.highlight(1);
        src.unhighlight(2);
        lang.nextStep();
        src.unhighlight(1);
        src.highlight(2);
        cipher.put(i, " !", null, null);
        cipherContent[i] = " !";

      } else {
        for (int k = 0; k < shift; k++) {
          lang.nextStep();
          src.highlight(1);
          src.unhighlight(2);
          lang.nextStep();
          src.unhighlight(1);
          src.highlight(2);

          if (indexOfChar >= alphabet.length) {
            indexOfChar = 0;
            cipher.put(i, alphabet[indexOfChar], null, null);
          } else {
            indexOfChar++;
            if (indexOfChar >= alphabet.length)
              indexOfChar = 0;
            cipher.put(i, alphabet[indexOfChar], null, null);

          }
          cipherContent[i] = alphabet[indexOfChar];
        }

      }

      src.unhighlight(2);
      src.highlight(3);
      lang.nextStep();
      plain.highlightCell(i, null, null);
      src.unhighlight(3);
      lang.nextStep();
    }

    src.highlight(4);
    lang.nextStep();
    src.unhighlight(4);
    return cipherContent;
  }

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    CaesarCipherFB caesar = new CaesarCipherFB();
    caesar.generate(null, null);
    System.out.println(caesar.lang.toString());
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    init();
    String[] plainText = { "A", "N", "I", "M", "A", "L" };
    encrypt(plainText);
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Caesar Chiffre";
  }

  public String getAnimationAuthor() {
    return "Fehmi Belhadj";
  }

  public String getCodeExample() {
    return "FOR EACH ELEMENT OF TEXT \n" + " FOR I = 0 TO SHIFT \n"
        + " TAKE THE NEXT LETTER\n" + "  SET NEW ELEMENT  \n"
        + "RETURN ENCRYPTED TEXT";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Beim Cäsar-Chiffre handelt es sich um eine monoalphabetische Vertauschung \n "
        + "Diese Zuordnung  basiert auf der zyklischen Drehung des Alphabets um k Zeichen.";
  }

  public String getFileExtension() {
    return ".asu";
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getName() {
    return "Caesar Chiffre";
  }

  public String getOutputLanguage() {
    return null;
  }

}
