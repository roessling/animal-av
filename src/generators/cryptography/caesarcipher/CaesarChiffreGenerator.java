package generators.cryptography.caesarcipher;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.annotations.Annotation;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class CaesarChiffreGenerator extends AnnotatedAlgorithm implements
    Generator {

  public CaesarChiffreGenerator() {
  }

  public static void printToFile(String string, File file) {
    FileOutputStream out;
    PrintStream p;
    try {
      out = new FileOutputStream(file);
      p = new PrintStream(out);
      p.println(string);
      p.close();
    } catch (Exception e) {
      System.out.println("Error writing to file");
    }
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    lang = new AnimalScript("Caesar Chiffre Animation", "Carsten Haubold", 640,
        480);

    // initiate variables support
    vars = lang.newVariables();

    annotations = new HashMap<String, Vector<Annotation>>();

    if (arg0 != null && arg1 != null) {
      String plaintext = (String) arg1.get("message");
      int shift = (Integer) arg1.get("shift");
      initCaesar(plaintext, shift);
    } else {
      initCaesar("this is a test", 7);
    }

    prepareAnimal();
    parse();

    encrypt();
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Caesar Cipher";
  }

  @Override
  public String getAnimationAuthor() {
    return "Carsten Haubold";
  }

  @Override
  public String getCodeExample() {
    return "CaesarChiffre(char[] A):\n"
        + "\tfor each i in 0 to length(Message)-1 do:\n"
        + "\t\tif A[i] >= 0 and A[i] < 26\n"
        + "\t\t\tA[i] = (A[i] + shift) % 26";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "The Caesar Chiffre replaces each character in a message "
        + "with a character from a shifted alphabet, as shown in the substitution matrix "
        + "Decryption works the same way, but shift has to be: "
        + "dec_shift = 26 - enc_shift";
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
  public String getName() {
    return "Caesar Chiffre Encryption";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    // everything changes if the parameters are changed, so nothing to do here
  }

  @Override
  public String getAnnotatedSrc() {
    return "int i;										@label(\"prepare\") @declare(\"int\", \"i\")\n"
        + "for(i = 0; 									@label(\"forInit\")\n"
        + " i < length(Message);						@label(\"forCompare\") @continue\n"
        + " i++)										@label(\"forIncreaseI\") @inc(\"i\") @continue\n"
        + "  if A[i] >= 0 and A[i] < 26					@label(\"if\")\n"
        + "    A[i] = (A[i] + shift) % 26				@label(\"shift\")\n";
  }

  private String[]           plaintext;
  private String[][]         substitutions;
  private int                shift;

  // Animal related variables
  // private IntArray intArray;
  private ArrayMarker        iMarker;

  // private Text shiftLabel;
  private StringArray        messageArray;
  private StringMatrix       strArray;
  private ArrayMarkerUpdater amuI;

  public void initCaesar(String plaintext, int shift) {
    // Create a String Array from the incoming String
    String plaintext2 = plaintext;
    this.plaintext = new String[plaintext2.length()];
    plaintext2 = plaintext2.toLowerCase();
    for (int i = 0; i < plaintext2.length(); i++) {
      this.plaintext[i] = String.valueOf(plaintext2.charAt(i));
    }

    this.lang.setStepMode(true);

    this.shift = shift;

    prepareCaesar();
  }

  private void prepareCaesar() {
    substitutions = new String[2][26];

    for (int i = 0; i < 26; i++) {
      substitutions[0][i] = String.valueOf((char) (97 + i));
      substitutions[1][i] = String.valueOf((char) (97 + (shift + i) % 26));
    }
  }

  private void prepareAnimal() {

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 20),
        "Caesar Chiffre - Encryption", "header", null, textProps);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header,
        "SE"), "headerBackground", null, rectProps);

    MatrixProperties matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.FALSE);
    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    matrixProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.WHITE);

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 20));
    Text substitutionLabel = lang.newText(new Coordinates(10, 130),
        "Substitutions:", "substLabel", null, textProps);

    strArray = lang.newStringMatrix(
        new Offset(10, -10, substitutionLabel, "NE"), substitutions,
        "substitutions", null, matrixProps);

    // first, set the visual properties (somewhat similar to CSS)
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.LIGHT_GRAY);

    messageArray = lang.newStringArray(new Offset(0, 120, strArray, "NW"),
        plaintext, "array", null, arrayProps);

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 20));
    Text arrayHeader = lang.newText(new Coordinates(10, 250), "Message:",
        "arrayHeader", null, textProps);

    // first, set the visual properties for the source code
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // now, create the source code entity
    sourceCode = lang.newSourceCode(new Offset(0, 70, arrayHeader, "SW"),
        "sourceCode", null, scProps);
    // // Line, name, indentation, display delay
    // sourceCode.addCodeLine("for each i in 0 to length(Message)-1 do:", null,
    // 0, null);
    // sourceCode.addCodeLine("if A[i] >= 0 and A[i] < 26", null, 1, null);
    // sourceCode.addCodeLine("A[i] = (A[i] + shift) % 26", null, 2, null);
    //
    // sourceCode.highlight(0);

    // Array, current index, name, display options, properties
    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    iMarker = lang.newArrayMarker(messageArray, 0, "i", null, arrayIMProps);
    iMarker.move(0, null, null);

    // NEW:
    amuI = new ArrayMarkerUpdater(iMarker, null, new TicksTiming(50),
        messageArray.getLength() - 1);

    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    lang.newText(new Offset(0, 20, substitutionLabel, "SW"),
        "shift = " + shift, "shift-label", null, textProps);

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode descripion = lang.newSourceCode(
        new Offset(20, 150, header, "SE"), "description", null, scProps);

    descripion.addCodeLine(
        "The Caesar Chiffre replaces each character in the message", null, 0,
        null);
    descripion.addCodeLine(
        "with a character from a shifted alphabet, as shown in the", null, 0,
        null);
    descripion.addCodeLine("substitution matrix.", null, 0, null);
    descripion.addCodeLine("", null, 0, null);
    descripion.addCodeLine(
        "Decryption works the same way, but shift has to be:", null, 0, null);
    descripion.addCodeLine("dec_shift = 26 - enc_shift", null, 1, null);
  }

  public void encrypt() {

    exec("prepare");
    amuI.setVariable(vars.getVariable("i"));
    lang.nextStep();

    exec("forInit");
    lang.nextStep();

    for (int i = 0; i < plaintext.length; i++) {
      exec("forCompare");
      lang.nextStep();

      // iMarker.move(i, new TicksTiming(15), null);
      if (i > 0) {
        messageArray.unhighlightCell(i - 1, null, null);
      }
      messageArray.highlightCell(i, null, null);

      int c = (int) plaintext[i].charAt(0) - 97;
      exec("if");
      if (c >= 0 && c < 26) // is a letter
      {
        strArray.highlightElem(0, c, null, null);
        strArray.highlightElem(1, c, null, null);
        lang.nextStep();
        exec("shift");
        messageArray.put(i, strArray.getElement(1, c), new TicksTiming(20),
            null);
        strArray.unhighlightElem(0, c, null, null);
        strArray.unhighlightElem(1, c, null, null);
      }

      lang.nextStep();
      exec("forIncreaseI");
      lang.nextStep();
    }
  }

  public void printCyphertext() {
    for (int i = 0; i < plaintext.length; i++) {
      System.out.print(plaintext[i]);
    }
    System.out.println("");
  }

}
