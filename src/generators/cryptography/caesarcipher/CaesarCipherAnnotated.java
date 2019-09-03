/**
 * 
 */
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
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Jan H. Post, Tim Grube
 * 
 */
public class CaesarCipherAnnotated extends AnnotatedAlgorithm implements
    Generator {

  private static final String algname = "Caesarverschlüsselung";
  // private static final String author = "Jan H. Post, Tim Grube";

  private String              msg;
  private int                 rot;
  private int                 pointerCounter;

  public CaesarCipherAnnotated(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  public CaesarCipherAnnotated() {
    this(new AnimalScript(algname, "Jan H. Post, Tim Grube", 640, 480));
  }

  /*
   * (non-Javadoc)
   * 
   * @see generators.AnnotatedAlgorithm#getAnnotatedSrc()
   */
  @Override
  public String getAnnotatedSrc() {

    return ""
        + "String caesar(String message, int verschiebung){ @label(\"header\")\n"
        + "String[] encryptedAlph = rotateAlphabet(verschiebung); @label(\"rotate\")\n"
        + "String encryptedMessage; @label(\"msg\") @declare(\"int\", \"i\", \"0\") @declare(\"String\", \"enc\")\n"
        + "for(int i = 0; i < message.length(); i++ ){  @label(\"loop\") \n"
        + "char current = message.charAt(i); @label(\"char\")\n"
        + "char encryptedChar = encryptedAlph[current]; @label(\"encrypt\")\n"
        + "encryptedMessage += enchryptedChar; @inc(\"i\") @label(\"addchar\")\n"
        + "} @label(\"endLOOP\")\n"
        + "return encryptedMessage; @label(\"return\") \n"
        + "} @label(\"end\")";
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * generator.Generator#generate(generator.properties.AnimationPropertiesContainer
   * , java.util.Hashtable)
   */
  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    init();

    if (primitives != null) {
      msg = (String) primitives.get("Klartextnachricht");
      rot = (Integer) primitives.get("Anzahl der verschobenen Stellen");
    }

    msg = msg.toLowerCase(); // es werden nur Nachrichten mit
    // Kleinbuchstaben unterstuetzt

    setup(msg, rot);

    return lang.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getAlgorithmName()
   */
  @Override
  public String getAlgorithmName() {
    return "Caesar-Verschl\u00fcsselung";
  }

  /**
   * gibt den/die autor(en) der anim. zurueck
   */
  @Override
  public String getAnimationAuthor() {
    return "Jan H. Post, Tim Grube";
  }

  /**
   * gibt die lokalisierung der anim. zurueck
   */
  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getDescription()
   */
  @Override
  public String getDescription() {
    return "Die Caesarverschlüsselung ist eine einfache"
        + "Verschiebechiffre welche Nachrichten durch Verschiebungen des Alphabets"
        + "verschlüsselt.\n"
        + "Bei einer Verschiebung um 3 wird das A durch ein D substituiert, "
        + "das B durch ein E, ...\n";
  }

  /**
   * gibt die kategorie der animation zurueck
   */
  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  /**
   * gibt den namen der animation zurueck
   * 
   * @return the name of this animation
   */
  public String getName() {
    return "Verschlüsselung: Caesar";
  }

  /**
   * welche programmiersprache wurde verwendet?
   */
  @Override
  public String getOutputLanguage() {
    return generators.framework.Generator.JAVA_OUTPUT;
  }

  /**
   * Bereite die Animation vor und starte diese...
   * 
   * @param msg
   *          zu verschluesselnder Text
   * @param rot
   *          wie weit soll rotiert werden?
   */
  private void setup(String msg, int rot) {

    // Definiere die Ueberschrift
    TextProperties txtProps = new TextProperties();
    txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));

    @SuppressWarnings("unused")
    Text txt = lang.newText(new Coordinates(40, 30), "Caesarverschlüsselung",
        "header", null, txtProps);

    // Definiere den Rahmen um die Ueberschrift
    RectProperties rectProps = new RectProperties();
    @SuppressWarnings("unused")
    Rect rect = lang.newRect(new Offset(-5, -5, "header",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header",
        AnimalScript.DIRECTION_SE), "hRect", null, rectProps);

    // Definiere das Aussehen des Source-Codes
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    // Zeichne das Kommentar-Code Objekt
    SourceCode com = lang.newSourceCode(new Coordinates(450, 350),
        "sourceCode", null, scProps);

    // Fuege Zeile fuer Zeile des Kommentar-Codes ein.
    // Line, name, indentation, display dealy
    com.addCodeLine(
        "// Caesarverschlüsselung auf message bei Alphabetverschiebung "
            + "um verschiebung anwenden", null, 0, null); // 0
    com.addCodeLine("// verschiebe/rotiere Alphabet", null, 0, null); // 2
    com.addCodeLine(
        "// Variable zur Speicherung der verschlüsselten Nachricht", null, 0,
        null); // 2
    com.addCodeLine("// Verschlüssele Zeichen für Zeichen", null, 0, null); // 5
    com.addCodeLine("// aktuelles Zeichen holen", null, 0, null); // 7
    com.addCodeLine("// verschlüsselte Repräsentation des Zeichens holen",
        null, 0, null); // 8
    com.addCodeLine("// füge verschlüsseltes Zeichen zur verschlüsselten "
        + "Nachricht hinzu", null, 0, null); // 9
    com.addCodeLine("", null, 0, null); // 11
    com.addCodeLine("// verschlüsselte Nachricht zurückgeben", null, 0, null); // 12

    // Definiere die warnung
    SourceCodeProperties warnProps = new SourceCodeProperties();
    warnProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    warnProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);

    SourceCode warning = lang.newSourceCode(new Coordinates(450, 250),
        "warning", null, warnProps);

    warning.addCodeLine(
        "Diese Animation kann lediglich Nachrichten verschlüsseln", null, 0,
        null);
    warning.addCodeLine("welche nur aus Buchstaben bestehen.", null, 0, null);
    warning.addCodeLine("Leerzeichen werden unverschlüsselt übernommen", null,
        0, null);

    // Definiere den Rahmen um die warnung
    @SuppressWarnings("unused")
    Rect warnRect = lang.newRect(new Offset(-5, -5, "warning",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "warning",
        AnimalScript.DIRECTION_SE), "warnRect", null, rectProps);

    // im ersten Schritt ist nur die Ueberschrift zu sehen - der Inhalt
    // kommt einen Schritt spaeter
    lang.nextStep();

    // Definiere das Aussehen der Arrays
    ArrayProperties arrayProps = new ArrayProperties();

    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    // Alphabet
    String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
        "y", "z" };

    // zielalphabet
    String[] rotatedalph = new String[alphabet.length];
    rotatedalph = rotateArray(alphabet, rot);

    // Stringarrays zum Anzeigen der Informationen in der Animation
    StringArray ca = lang.newStringArray(new Coordinates(40, 115), alphabet,
        "base", null, arrayProps);

    StringArray ea = lang.newStringArray(new Coordinates(40, 180), rotatedalph,
        "destination", null, arrayProps);
    ea.hide();

    // ---------------------------------------------------------------------------------------------------------------------

    // Definiere den Rotationszahlanzeige-Text
    SourceCodeProperties rotTextProps = new SourceCodeProperties();
    rotTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Arial",
        Font.PLAIN, 14));
    rotTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode rotText = lang.newSourceCode(new Coordinates(40, 250),
        "rotText", null, rotTextProps);

    rotText.addCodeLine("Anzahl der Stellen, um die verschoben wird: ", null,
        0, null);

    // Definiere die Rotationszahlanzeige
    SourceCodeProperties rotDisplayProps = new SourceCodeProperties();
    rotDisplayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Arial", Font.BOLD, 20));
    rotDisplayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode rotDisplay = lang.newSourceCode(new Coordinates(350, 240),
        "rotDisplay", null, rotDisplayProps);

    rotDisplay.addCodeLine(Integer.toString(rot), null, 0, null);

    // Definiere den Rahmen um die Rotationszahlanzeige
    @SuppressWarnings("unused")
    Rect rotDisplayRect = lang.newRect(new Offset(-5, -5, "rotDisplay",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "rotDisplay",
        AnimalScript.DIRECTION_SE), "warnRect", null, rectProps);

    // ---------------------------------------------------------------------------------------------------------------------

    // Pfeile zum animieren der Rotation
    PolylineProperties lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    lineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
    Coordinates[] line1Coordinates = { new Coordinates(70, 250),
        new Coordinates(130, 250) };
    Polyline arrow1 = lang.newPolyline(line1Coordinates, "arrow1", null,
        lineProps);
    Coordinates[] line2Coordinates = { new Coordinates(140, 250),
        new Coordinates(200, 250) };
    Polyline arrow2 = lang.newPolyline(line2Coordinates, "arrow2", null,
        lineProps);
    Coordinates[] line3Coordinates = { new Coordinates(210, 250),
        new Coordinates(270, 250) };
    Polyline arrow3 = lang.newPolyline(line3Coordinates, "arrow3", null,
        lineProps);

    Polyline arrows[] = { arrow1, arrow2, arrow3 };

    // zum animieren der rotation...
    StringArray[] steps = null;

    // ...berechne zwischenalphabete
    if (rot > 0) {
      steps = new StringArray[rot];

      for (int i = 0; i < rot; i++) {
        String[] ra = rotateArray(alphabet, i);
        steps[i] = lang.newStringArray(new Coordinates(40, 180), ra,
            "step" + i, null, arrayProps);
        steps[i].hide();
      }
    }

    encrypt(msg, ca, ea, steps, arrows);
  }

  /**
   * Beginne die Animation des Verschluesselungsvorganges
   * 
   * @param message
   *          Nachricht
   * @param ca
   *          Klaralphabet
   * @param ea
   *          verschobenes Alphabet
   * @param rotsteps
   *          Zwischenschritte beim verschieben
   * @param arrows
   *          Pfeile die die Verschiebung animieren
   */
  private void encrypt(String message, StringArray ca, StringArray ea,
      StringArray[] rotsteps, Polyline[] arrows) {

    exec("header");

    // aussehen der texte mit klar- und chiffre-text
    TextProperties txtProps = new TextProperties();
    txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    // klartext anzeigen
    @SuppressWarnings("unused")
    Text klartitel = lang.newText(new Coordinates(450, 115),
        "Klartextnachricht:", "klartitel", null, txtProps);
    @SuppressWarnings("unused")
    Text klartext = lang.newText(new Coordinates(610, 120), message,
        "klartext", null, txtProps);

    // verschluesselter text anzeigen
    @SuppressWarnings("unused")
    Text encryptedtitel = lang.newText(new Coordinates(450, 180),
        "verschlüsselter Text:", "enctitel", null, txtProps);
    Text encrypted = lang.newText(new Coordinates(610, 180), "", "chiffretext",
        null, txtProps);

    // Erzeuge Array-Pointer ...
    pointerCounter++;
    // ... und setzte seine Properties
    ArrayMarkerProperties arrayMIProps = new ArrayMarkerProperties();
    arrayMIProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
    arrayMIProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.YELLOW);
    ArrayMarker iMarker = lang.newArrayMarker(ea, 0, "i" + pointerCounter,
        new Hidden(), arrayMIProps);
    iMarker.hide();

    iMarker.hide();

    lang.nextStep();
    iMarker.hide();
    exec("rotate");
    showRotation(ea, rotsteps, arrows);
    lang.nextStep();
    iMarker.hide();
    exec("msg");
    lang.nextStep();
    iMarker.hide();
    encryptionloop(message, ca, ea, encrypted, iMarker);
    lang.nextStep();
    iMarker.hide();
    exec("return");
    lang.nextStep();

  }

  /**
   * Schleife die Buchstaben fuer Buchstaben durch den verschobenen ersetzt
   * 
   * @param message
   *          Nachricht
   * @param ca
   *          Klaralphabet (zum highlighten)
   * @param ea
   *          verschobenes Alphabet (zum highlighten)
   * @param encrypted
   *          Anzeigeelement des verschluesselten Texts
   * @param marker
   *          Markerpfeil zum zeigen der aktuellen Position im Alphabet
   */
  private void encryptionloop(String message, StringArray ca, StringArray ea,
      Text encrypted, ArrayMarker marker) {

    exec("loop");
    int hlc = 0;
    marker.hide();

    while (Integer.parseInt(vars.get("i")) < message.length()) {
      lang.nextStep();
      marker.hide();

      exec("char");
      char current = message.charAt(Integer.parseInt(vars.get("i")));
      hlc = current - 97;

      if (hlc >= 0) {
        ca.highlightCell(hlc, null, new TicksTiming(75));
        marker.show();
        marker.move(hlc, new TicksTiming(75), new TicksTiming(150));
      }
      lang.nextStep();

      exec("encrypt");
      String encryptedChar;
      if (current != ' ') {
        encryptedChar = ea.getData(current - 97); // a ist 97
        if (hlc >= 0) {
          ea.highlightCell(hlc, null, new TicksTiming(75));

        }
      } else {
        encryptedChar = " "; // Leerzeichen werdn uebernommen
      }
      lang.nextStep();

      exec("addchar");
      String newenc = vars.get("enc") + encryptedChar;
      vars.set("enc", newenc);
      encrypted.setText(newenc, new TicksTiming(75), null);
      if (hlc >= 0) {
        ca.unhighlightCell(hlc, null, null);
        ea.unhighlightCell(hlc, null, null);
      }
      lang.nextStep();
    }
    marker.hide();
  }

  /**
   * Zeigt die Rotation an
   * 
   * @param ea
   *          endgueltiges, rotiertes Alphabet
   * @param rotsteps
   *          Zwischenschritte
   * @param arrows
   *          Pfeile zur Animation
   */
  private void showRotation(StringArray ea, StringArray[] rotsteps,
      Polyline[] arrows) {
    // zeige rotation an...
    TicksTiming timing = new TicksTiming(0);
    TicksTiming timing1 = new TicksTiming(33);
    TicksTiming timing2 = new TicksTiming(66);
    for (Polyline arrow : arrows) {
      arrow.show();
    }
    if (rotsteps != null) {
      for (int i = 0; i < rotsteps.length; i++) {
        arrows[0].changeColor(null, Color.GRAY, timing, null);
        arrows[1].changeColor(null, Color.LIGHT_GRAY, timing, null);
        arrows[2].changeColor(null, Color.LIGHT_GRAY, timing, null);
        arrows[0].changeColor(null, Color.LIGHT_GRAY, timing1, null);
        arrows[1].changeColor(null, Color.GRAY, timing1, null);
        arrows[2].changeColor(null, Color.LIGHT_GRAY, timing1, null);
        arrows[0].changeColor(null, Color.LIGHT_GRAY, timing2, null);
        arrows[1].changeColor(null, Color.LIGHT_GRAY, timing2, null);
        arrows[2].changeColor(null, Color.GRAY, timing2, null);
        if (i > 0) {
          rotsteps[i - 1].hide(timing);
        }
        rotsteps[i].show(timing);
        timing = new TicksTiming(100 * (i + 1));
        timing1 = new TicksTiming(100 * (i + 1) + 33);
        timing2 = new TicksTiming(100 * (i + 1) + 66);
      }
      if (rotsteps.length > 0) {
        rotsteps[rotsteps.length - 1].hide(timing);
      }
    }
    // endgueltiges alphabet einblenden...
    ea.show(timing);
  }

  /**
   * rotiere ein array um rot-positionen
   * 
   * @param array
   *          zu rotierendes array
   * @param rot
   *          anzahl positionen
   * @return rotiertes array
   */
  private String[] rotateArray(String[] array, int rot) {
    String[] ar = new String[array.length];

    /*
     * rotiert das array:
     * 
     * a, b, c, d, e (2) -> d, e, a, b, c
     */

    int rotation = rot % array.length;
    // mod array.length damit ich auch mehrfach im kreis rotieren kann...
    for (int i = 0; i < array.length; i++) {
      if (i < rot) {
        ar[i] = array[array.length - rotation + i];
      } else {
        ar[i] = array[i - rotation];
      }
    }

    return ar;
  }

  @Override
  public void init() {
    super.init();

    // default werte...
    msg = "testnachricht";
    rot = 5;

    // Sourcecodedesign
    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(40, 350), "Code", null,
        props);

    // parsing anwerfen
    parse();
  }
}
