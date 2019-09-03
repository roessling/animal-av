package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
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
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * Lösung für die 5. Aufgabe
 * 
 * @author Clemens Bergmann
 * 
 */
public class CaesarWithKey implements Generator {

  /**
   * Dieses Interface beschreibt eine Klasse mit einer Funktion die von einem
   * Zeichen auf einen Verschiebewert abbildet.
   * 
   * @author Clemens Bergmann
   * 
   */
  private interface keyfunction {
    /**
     * Diese Funktion bekommt ein Zeichen und liefert den zugehörigen
     * Schiebewert.
     * 
     * @param c
     *          Das Schlüsselzeichen
     * @return der Verschiebewert
     */
    int getshiftnum(char c);
  }

  /*
   * Alle Generatoren dieser Klasse haben das selbe Timing
   */
  /**
   * Wie lange soll vor einer Aktion gewartet werden
   */
  private Timing                wait     = null;
  /**
   * Wie lange soll eine Aktion dauern
   */
  private Timing                duration = new TicksTiming(20);

  /**
   * Die generellen eigenschaften von allen Arrays eines generators diesen Types
   */
  private ArrayProperties       ap;

  /**
   * Die generellen Eigenschaften aller ArrayMarker eines Generators diesen
   * Types
   */
  private ArrayMarkerProperties amp;

  /**
   * Das Alphabet auf dem alle generatoren diesen typs arbeiten.
   */
  char[]                alphabet;

  /**
   * Dieses SourceCode Object beinhaltet den Sourcecode eines Genrators
   */
  private SourceCode            code;

  /**
   * Der Schlüßelbuchstabe eines Generators
   */
  private char                  k;
  /**
   * Das Sprachobjekt eines Generators
   */
  private Language              lang;
  /**
   * Der Geheimtext eines Generators
   */
  private String                secret;

  /**
   * Dies ist der Standard Konstruktor Er erstellt einen Generator mit leerem
   * Geheimtext und dem Schlüsselbuchstaben 'a'
   */
  public CaesarWithKey() {
    this('a', "");
  }

  /**
   * Der normalerweise verwendeten Konstruktor
   * 
   * @param k
   *          Der Schlüsselbuchstabe
   * @param secret
   *          Das Geheimniss das verschlüsselt werden soll
   */
  public CaesarWithKey(char k, String secret) {
    this.k = k;
    this.secret = secret;
    this.lang = new AnimalScript(this.getAlgorithmName(), this
        .getAnimationAuthor(), 640, 480);
  }

  /**
   * Diese Funktion liefert ein String array mit dem Sourcecode.
   * 
   * @return Der Sourcecode. Jede Zeile ist ein Eintrag im array.
   */
  private String[] getcode() {
    ArrayList<String> ar = new ArrayList<String>();
    ar.add("A: Verschlüsselung");
    ar
        .add("1. Gegeben sei der Klartext t. Dieser enthält schützenswerte Informationen.");
    ar
        .add("2. Gegeben sei eine Funktion f die einem Schlüsselbuchstaben eine Zahl zuweist (z.B. seine Position im Alphabet)");
    ar.add("3. Gegeben sei ein Schlüsselbuchstabe k.");
    ar.add("4. Wende f auf k an. Das Ergebniss sei n.");
    ar
        .add("5. Verschiebe jeden Buchstaben im Schlüsseltext zyklisch um k. Das Ergebniss ist der Cyphertext c.");
    ar
        .add("6. Versende eine Nachricht bestehend aus k und c an den Empfänger. Dieser muss f kennen.");
    ar.add("");
    ar.add("B: Entschlüsselung");
    ar.add("1. Empfangen wird der Cyphertext c.");
    ar.add("2. Gegeben sei die Funktion f.");
    ar.add("3. Empfangen wird der Schlüsselbuchstabe K.");
    ar.add("4. Wende f auf an. das Ergeniss sei n.");
    ar
        .add("5. Verschiebe die Buchstaben in c zyklisch um k. Das Ergeniss ist der Klartext t.");
    ar.add("6. Das Ergebniss ist der Klartext t.");

    return ar.toArray(new String[0]);
  }

  /**
   * Diese Funktion erstellt überschift und Sourcecode in der Animation
   */
  private void caesar_prepare() {
    DisplayOptions header_ops = null;
    Text header = lang.newText(new Coordinates(20, 30),
        this.getAlgorithmName(), "header", header_ops);

    lang.nextStep();

    SourceCodeProperties scp = new SourceCodeProperties();

    scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);

    code = lang.newSourceCode(new Offset(0, 100, header,
        AnimalScript.DIRECTION_SW), "source", null, scp);

    String[] sourcecodesource = this.getcode();
    for (String a : sourcecodesource) {
      code.addCodeLine(a, null, 0, null);
    }

    lang.nextStep();

  }

  /**
   * Diese Funktion erstellt die Animation für die Verschlüsselung eines
   * Textes.
   * 
   * @param klartext
   *          Der zu verschlüsselnde Klartext
   * @param schluessel
   *          Der Schlüsselbuchstabe
   * @param f
   *          die Mappingfunktion von schluessel auf einen Verschiebewert
   * @return Der verschlüsselte Text
   */
  private String caesar_crypt(String klartext, char schluessel, keyfunction f) {

    code.highlight(0);

    lang.nextStep();

    code.toggleHighlight(0, 1);
    ArrayList<String> al = new ArrayList<String>();
    for (char c : klartext.toCharArray()) {
      al.add("" + c);
    }

    StringArray arr = lang.newStringArray(new Offset(0, 40, code,
        AnimalScript.DIRECTION_SW), al.toArray(new String[0]), "klartext",
        null, ap);

    lang.nextStep();

    code.toggleHighlight(1, 2);

    lang.nextStep();

    code.toggleHighlight(2, 3);

    Text k_text = lang.newText(
        new Offset(0, 20, arr, AnimalScript.DIRECTION_SW), "k = " + schluessel,
        "k", null);

    lang.nextStep();

    code.toggleHighlight(3, 4);

    int shiftnum = f.getshiftnum(schluessel);

    k_text.setText("n = f(k) = " + shiftnum, null, null);

    lang.nextStep();

    k_text.hide();

    code.toggleHighlight(4, 5);

    ArrayMarker marker = lang.newArrayMarker(arr, 0, "pointer", null, amp);

    for (int i = 0; i < arr.getLength(); i++) {
      marker.move(i, null, null);
      for (int j = 0; j < shiftnum; j++) {
        arr.put(i, "" + shift(arr.getData(i).charAt(0), 1), wait, duration);
      }

      lang.nextStep();
    }

    code.toggleHighlight(5, 6);

    arr.hide(duration);

    lang.nextStep();

    code.unhighlight(6);

    String result = "";
    for (int i = 0; i < arr.getLength(); i++) {
      result += arr.getData(i);
    }

    return result;
  }

  /**
   * Diese Funktion entschlüsselt einen Text der vorher mit
   * {@link #caesar_crypt(String, char, keyfunction)} verschlüsselt wurde
   * 
   * @param cypher
   *          Der verschlüsselte Text
   * @param schluessel
   *          Der Schlüsselbuchstabe
   * @param f
   *          Die gleiche mappingfunktion wie bei
   *          {@link #caesar_crypt(String, char, keyfunction)}.
   * @return Der entschlüsselte Text
   */
  private String caesar_uncrypt(String cypher, char schluessel, keyfunction f) {
    code.highlight(8);

    lang.nextStep();

    code.toggleHighlight(8, 9);
    ArrayList<String> al = new ArrayList<String>();
    for (char c : cypher.toCharArray()) {
      al.add("" + c);
    }
    StringArray arr = lang.newStringArray(new Offset(0, 40, code,
        AnimalScript.DIRECTION_SW), al.toArray(new String[0]), "klartext",
        null, ap);

    lang.nextStep();

    code.toggleHighlight(9, 10);

    lang.nextStep();

    code.toggleHighlight(10, 11);

    Text k_text = lang.newText(
        new Offset(0, 20, arr, AnimalScript.DIRECTION_SW), "k = " + schluessel,
        "k", null);

    lang.nextStep();

    code.toggleHighlight(11, 12);

    int shiftnum = f.getshiftnum(schluessel);

    k_text.setText("n = f(k) = " + shiftnum, null, null);

    lang.nextStep();

    k_text.hide();

    code.toggleHighlight(12, 13);

    ArrayMarker marker = lang.newArrayMarker(arr, 0, "pointer", null, amp);

    for (int i = 0; i < arr.getLength(); i++) {
      marker.move(i, null, null);
      for (int j = 0; j < shiftnum; j++) {
        arr.put(i, "" + shift(arr.getData(i).charAt(0), -1), wait, duration);
      }

      lang.nextStep();
    }

    code.toggleHighlight(13, 14);

    lang.nextStep();

    code.unhighlight(14);

    String result = "";
    for (int i = 0; i < arr.getLength(); i++) {
      result += arr.getData(i);
    }

    return result;
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    this.secret = (String) primitives.get("secret");
    this.k = ((String) primitives.get("key")).charAt(0);
    alphabet = ((String) primitives.get("alphabet")).toCharArray();
    Arrays.sort(alphabet);
    wait = new TicksTiming((Integer) primitives.get("wait"));
    duration = new TicksTiming((Integer) primitives.get("duration"));
    amp = (ArrayMarkerProperties) props.getPropertiesByName("amp");
    ap = (ArrayProperties) props.getPropertiesByName("ap");

    keyfunction f = new keyfunction() {
      @Override
      public int getshiftnum(char c) {
        int result = 0;
        char newC = tolower(c);
        result = Arrays.binarySearch(alphabet, newC) + 1;
        result = Math.max(result, 0);
        return result;
      }

    };
    this.caesar_prepare();
    String c = this.caesar_crypt(this.secret, this.k, f);
    this.caesar_uncrypt(c, this.k, f);

    return this.lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Caesar-Verschl\u00fcsselung";
  }

  @Override
  public String getAnimationAuthor() {
    return "Clemens Bergmann";
  }

  @Override
  public String getCodeExample() {
    StringBuffer sb = new StringBuffer();
    for (String s : this.getcode()) {
      sb.append(s + "\n");
    }
    return sb.toString();
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "This Generator generates a Animation for the Caesar-Chiffre.";
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
    return "Caesar Chiffre mit übergebbarem Verschiebewert.";
  }

  @Override
  public String getOutputLanguage() {
    return PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    this.lang = new AnimalScript(this.getAlgorithmName(), this
        .getAnimationAuthor(), 640, 480);
    lang.setStepMode(true);
  }

  /**
   * Diese Funktion verschiebt einen Buchstaben um den gegebenen Wert.
   * 
   * @param in
   *          Der Buchstabe
   * @param shift
   *          Die Verschiebezahl
   * @return Der verschobene Buchstabe
   */
  private char shift(char in, int shift) {
    char newIn = tolower(in);
    int index = Arrays.binarySearch(alphabet, newIn);

    char result;
    if (index >= 0) {
      int new_index = ((index + shift) % alphabet.length);
      if (new_index < 0) {
        new_index += alphabet.length;
      }
      result = alphabet[new_index];
    } else {
      result = newIn;
    }

    return result;
  }

  /**
   * Diese Funktion wandelt einen Buchstaben in seine kleinschreibweise
   * 
   * @param in
   *          Der Buchstabe
   * @return die kleine Variante des Buchstabens
   */
  static char tolower(char in) {
    return ("" + in).toLowerCase().charAt(0);
  }
}
