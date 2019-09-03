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
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class AnnotatedCaesarChiffre extends AnnotatedAlgorithm implements
    Generator {

  private static final String DESCRIPTION = "Hier folgt eine Beschreibung,"
                                              + "die über zwei Zeilen geht...!";

  private static final String SOURCE_CODE = "// Java-Code für CeasarChiffre\n"
                                              + "public void GnomeSort(int[] array)\n"
                                              + "und so weiter...";

  // public Language animalScript; // siehe "AnnotatedAlgorithm"
  // private SourceCode sc; // siehe "AnnotatedAlgorithm"

  private StringMatrix        codeMatrix, textMatrix;

  private String              zeichenkette;
  private int                 verschiebewert;
  private MatrixProperties    mProps;

  // private Timing defaultTiming = new TicksTiming(50);

  public AnnotatedCaesarChiffre() {
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    // Parameter
    zeichenkette = arg1.get("zeichenkette").toString();
    verschiebewert = Integer.parseInt((arg1.get("verschiebewert")).toString());
    mProps = (MatrixProperties) arg0.getPropertiesByName("matrixProps");

    // Init
    super.init();
    localInit();

    // Parsen
    parse();

    // Sortieren
    execute();

    // AnimalScript
    System.out.println(lang.toString());
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Caesar-Verschlüsselung";
  }

  public String getAnimationAuthor() {
    return "Markus Vogel";
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(
        generators.framework.GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getName() {
    return "CaesarChiffre mit Annotations";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void init() {
    // nothing to be done here
  }

  public void localInit() {

    lang = new AnimalScript("Annotated Caesar-Cipher", "Markus Vogel", 640, 480);
    lang.setStepMode(true);

    createTitle("Annotated Caesar-Cipher");
    sourceCode = createSourceCode();
    createOffsetField(verschiebewert);

    while (verschiebewert < 0) {
      verschiebewert += 26;
    }

    String[][] codeMatrixString, textMatrixString;

    // Code-Matrix erstellen
    codeMatrixString = new String[2][27];
    for (int i = 0; i < 26; i++) {
      codeMatrixString[0][i] = String.valueOf((char) (65 + i));
      codeMatrixString[1][i] = "";
      // encode[1][i] = String.valueOf(encode((char) (65 + i), offset));
    }

    // Sonstige Zeichen einfügen
    codeMatrixString[0][26] = "~";
    codeMatrixString[1][26] = "~";

    // Text-Matrix erstellen
    textMatrixString = new String[2][zeichenkette.length()];
    for (int i = 0; i < zeichenkette.length(); i++) {
      textMatrixString[0][i] = String.valueOf(zeichenkette.charAt(i));
      textMatrixString[1][i] = "";
    }

    codeMatrix = createStringMatrix(codeMatrixString, 450, 0, mProps);
    textMatrix = createStringMatrix(textMatrixString, 550, 2, mProps);
  }

  public void execute() {

    // begin encrypting
    exec("begin");
    lang.nextStep();

    // 1. Schritt - Alphabet mit Verschiebewert verschlüsseln
    encryptAlphabet();

    // 2. Schritt - Nachricht mit o.a. Alphabet verschlüsseln
    encryptMessage();

    // end sorting
    exec("end");
    completed();
    System.out.println("--- encrypting completed ---");
    lang.nextStep();
  }

  private void encryptAlphabet() {

    // Markiere Zeile
    // sourceCode.toggleHighlight(0, 1);

    exec("for_i");
    // Ersten Verschiebewert schreiben
    codeMatrix.highlightCell(0, 0, null, null);
    codeMatrix.put(1, 0, new String("0"), null, null);
    lang.nextStep();

    for (int i = 1; i <= verschiebewert; i++) {

      exec("offset");
      // Nächsten Verschiebewert schreiben
      codeMatrix.highlightCell(0, i % 26, null, null);
      codeMatrix.put(1, i % 26, new String("" + i), null, null);
      // Vorherige Markierung entfernen
      codeMatrix.unhighlightCell(0, (i - 1) % 26, null, null);
      codeMatrix.put(1, (i - 1) % 26, "", null, null);
      lang.nextStep();
      exec("for_i");
      lang.nextStep();
    }

    // Letzten Verschiebewert entfernen
    codeMatrix.put(1, verschiebewert % 26, "", null, null);

    exec("for_j");
    lang.nextStep();

    for (int j = 0; j < 26; j++) {

      exec("alphabet");
      // Aktuelles Zeichen verschlüsseln
      codeMatrix.highlightCell(0, (j + verschiebewert) % 26, null, null);
      codeMatrix.highlightCell(1, j % 26, null, null);
      codeMatrix
          .put(1, j % 26,
              encrypt(codeMatrix.getElement(0, j % 26), verschiebewert), null,
              null);
      lang.nextStep();
      exec("for_j");
      lang.nextStep();
    }

    codeMatrix.unhighlightCellColumnRange(0, 0, 25, null, null);
    codeMatrix.unhighlightCellColumnRange(1, 0, 25, null, null);
  }

  private void encryptMessage() {

    exec("for_k");
    lang.nextStep();

    for (int k = 0; k < textMatrix.getNrCols(); k++) {

      exec("message");
      encryptElementAt(k);
      // lang.nextStep();
      exec("for_k");
      lang.nextStep();
    }
  }

  private int getIndex(char c) {
    int i = c - 65;
    if (i > 25) {
      i -= 32;
    }
    if (i > 25 || i < 0) {
      i = 26; // sonstige Zeichen
    }
    return i;
  }

  // private char getChar(int i) {
  // return (char) (i + 65);
  // }

  private void encryptElementAt(int pos) {

    // Markiere Zeile
    // sourceCode.toggleHighlight(3, 4);

    // Zeichen verschlüsseln
    textMatrix.put(1, pos,
        encrypt(textMatrix.getElement(0, pos), verschiebewert), null, null);
    textMatrix.highlightCell(0, pos, null, null);
    textMatrix.highlightCell(1, pos, null, null);

    // Alphabet-Position markieren
    codeMatrix.highlightCellRowRange(0, 1,
        getIndex(textMatrix.getElement(0, pos).charAt(0)), null, null);
    lang.nextStep();
    codeMatrix.unhighlightCellRowRange(0, 1,
        getIndex(textMatrix.getElement(0, pos).charAt(0)), null, null);
  }

  private String encrypt(String s, int i) {
    return String.valueOf(encrypt(s.charAt(0), i));
  }

  private char encrypt(char c, int i) {
    int i2 = i;
    while (i2 < 0) {
      i2 += 26;
    }
    char[] alphabetUC = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z' };
    for (int k = 0; k < 26; k++) {
      if (alphabetUC[k] == c) {
        i2 = (i2 + k) % 26;
        return alphabetUC[i2];
      }
    }
    char[] alphabetLC = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z' };
    for (int k = 0; k < 26; k++) {
      if (alphabetLC[k] == c) {
        i2 = (i2 + k) % 26;
        return alphabetLC[i2];
      }
    }
    return c;
  }

  // private char decrypt(char c, int i) {
  // return encrypt(c, -1 * i);
  // }

  private Text createOffsetField(int offset) {
    TextProperties props = new TextProperties();
    props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    return lang.newText(new Coordinates(20, 400), "Verschiebewert = " + offset,
        "offsetField", null, props);
  }

  private void completed() {

    // Markiere Zeile
    // sourceCode.toggleHighlight(3, 5);
    // sourceCode.toggleHighlight(4, 5);

    // sourceCode.unhighlight(1);
    TextProperties props = new TextProperties();
    props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("sans Serif",
        Font.BOLD, 20));
    lang.newText(new Coordinates(20, 700),
        "Verschlüsselung mit Caesar-Chiffre erfolgreich abgeschlossen.",
        "complete", null, props);
    // lang.nextStep();
  }

  private void createTitle(String title) {
    TextProperties tProps = new TextProperties();
    tProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("sans Serif",
        Font.BOLD, 20));
    lang.newText(new Coordinates(20, 30), title, "title", null, tProps);
    lang.newRect(new Coordinates(15, 25), new Coordinates(300, 65), "titlebox",
        null);
    lang.nextStep();
  }

  private SourceCode createSourceCode() {

    // Source-Code erstellen
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    SourceCode sc = lang.newSourceCode(new Coordinates(20, 100),
        "codeSelectionSort", null, scProps);
    // sc.addCodeLine("0 - Alphabet erstellen", null, 0, null);
    // sc.addCodeLine("1 - Alphabet übersetzen", null, 0, null);
    // sc.addCodeLine("2 - Nachricht erstellen", null, 0, null);
    // sc.addCodeLine(
    // "3 - Wiederholen, solange weiterer Buchstabe vorhanden ist",
    // null, 0, null);
    // sc.addCodeLine("4    - Aktuellen Buchstabe verschlüsseln", null, 0,
    // null);
    // sc.addCodeLine("5 - ENDE -", null, 0, null);
    //
    // lang.nextStep();

    return sc;
  }

  private StringMatrix createStringMatrix(String[][] string, int y,
      int codeline, MatrixProperties smProps) {

    // Markiere Zeile
    // sourceCode.toggleHighlight(Math.max(0, codeline - 1), codeline);

    // Array erstellen
    // MatrixProperties smProps = new MatrixProperties();
    // smProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 16));
    // smProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // smProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // smProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    // smProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
    // Color.BLACK);
    // smProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
    // Color.RED);
    // smProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
    // Color.YELLOW);
    textMatrix = lang.newStringMatrix(new Coordinates(20, y), string,
        "StringMatrix" + y, null, smProps);
    // lang.nextStep();
    return textMatrix;
  }

  @Override
  public String getAnnotatedSrc() {
    return "ceasarChiffre(String text, int offset) {			@label(\"begin\") @declare(\"int\", \"i\", \"0\") @declare(\"int\", \"j\", \"-1\") @declare(\"int\", \"k\", \"-1\")\n"
        + "   for (int i = 0; i <= move; i++) {				@label(\"for_i\") @inc(\"i\")\n"
        + "      goToElement(offset);						@label(\"offset\")\n"
        + "   }												@label(\"end_i\")\n"
        + "   for (int j = 0; j < 26; j++) {				@label(\"for_j\") @inc(\"j\")\n"
        + "      encryptAlphabetAt(j);						@label(\"alphabet\")\n"
        + "   } 											@label(\"end_j\")\n"
        + "   for (int k = 0; text.length; k++) {			@label(\"for_k\") @inc(\"k\")\n"
        + "      encryptMessageAt(k);						@label(\"message\")\n"
        + "   } 											@label(\"end_k\")\n"
        + "}												@label(\"end\")\n";
  }
}