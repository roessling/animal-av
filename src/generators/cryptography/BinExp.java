package generators.cryptography;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.OffsetCoords;

import java.awt.Font;
//import java.awt.Label;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
//import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
//import algoanim.variables.Variable;
//import algoanim.variables.VariableTypes;
//import animal.misc.LabeledAnimator;

public class BinExp implements Generator {
  private static Language      lang;
  private static int           basis;
  private static int           exponent;
  private static int           mod;
  private int                  multiplikationen;
  private int                  z;               // Zähler Berechnungsschritte
  private BigInteger           ze;              // Zwischenergebnis
  private TextProperties       textProps_titel;
  private ArrayProperties      arrayProps;
  private RectProperties       rectProps;
  private SourceCodeProperties scProps;
  private TextProperties       textProps;
  private Timing               defaultTiming;

  public void init() {
    lang = new AnimalScript("Binäre Exponentiation",
        "Julian Metzler, Tino Fuhrmann", 800, 600);
    lang.setStepMode(true);
    this.setMultiplikationen(0);
    this.setZe(BigInteger.ONE);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    if (primitives.get("Modul") instanceof String) {
      mod = Integer.parseInt((String) primitives.get("Modul"));
    } else
      mod = (Integer) primitives.get("Modul");

    if (primitives.get("Basis") instanceof String) {
      basis = Integer.parseInt((String) primitives.get("Basis"));
    } else
      basis = (Integer) primitives.get("Basis");

    if (primitives.get("Exponent") instanceof String) {
      exponent = Integer.parseInt((String) primitives.get("Exponent"));
    } else
      exponent = (Integer) primitives.get("Exponent");

    textProps_titel = (TextProperties) props
        .getPropertiesByName("Text Properties (Titel)");
    arrayProps = (ArrayProperties) props
        .getPropertiesByName("Array Properties");
    rectProps = (RectProperties) props
        .getPropertiesByName("Background Properties");
    scProps = (SourceCodeProperties) props
        .getPropertiesByName("Source Code Properties");
    textProps = (TextProperties) props.getPropertiesByName("Text Properties");
    defaultTiming = new TicksTiming(15);

    // Titel etwas prominenter gestalten
    textProps_titel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 24));

    potentieren(basis, exponent);

    return lang.toString();
  }

  public String getName() {
    return "Binäre Exponentiation";
  }

  public String getAlgorithmName() {
    return "Exponentiation";
  }

  public String getAnimationAuthor() {
    return "Julian Metzler, Tino Fuhrmann";
  }

  public String getDescription() {
    return "Die Binäre Exponentiation ist ein Algorithmus zum effizienteren Potenzieren."
        + "\n"
        + "Um zum Beispiel z = x^4 zu berechnen, kann man entweder z = x * x * x * x ausrechnen "
        + "\n"
        + "(drei Multiplikationen) oder y = x * x, z = y * y (zwei Multiplikationen), also z = (x^2)^2"
        + "\n"
        + "Ebenso können auch andere ganzzahlige Potenzen durch fortgesetztes Quadrieren"
        + "\n"
        + "und gelegentliches Multiplizieren effizient berechnet werden.";
  }

  public String getCodeExample() {
    return "1. Umwandlung des Exponenten in Binärdarstellung"
        + "\n"
        + "2. Zwischenergebnis mit 1 initialisieren"
        + "\n"
        + "3. Den Exponenten schrittweise durchgehen"
        + "\n"
        + " - Bei einer 0 wird das aktuelle Ergebnis quadriert"
        + "\n"
        + " - Bei einer 1 wird das aktuelle Ergebnis quadriert und mit der Basis multipliziert"
        + "\n";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void setMultiplikationen(int multiplikationen) {
    this.multiplikationen = multiplikationen;
  }

  public void setZe(BigInteger ze) {
    this.ze = ze;
  }

  public void potentieren(int a, int b) {

    /*
     * Step 1 - Einleitung
     */

    // Titel
    Text titel = lang.newText(new Coordinates(10, 10), "Binäre Exponentiation",
        "titel", null, textProps_titel);

    // Einleitung
    SourceCode einleitung = lang.newSourceCode(
        new OffsetCoords(titel.getUpperLeft(), 0, 20), "sourceCode", null,
        scProps);
    einleitung
        .addCodeLine(
            "Die Binäre Exponentiation ist ein Algorithmus zum effizienteren Potenzieren.",
            "Beschreibung", 0, null);
    einleitung
        .addCodeLine(
            "Um zum Beispiel z = x^4 zu berechnen, kann man entweder z = x * x * x * x ausrechnen (drei Multiplikationen)",
            "Beschreibung", 0, null);
    einleitung.addCodeLine(
        "oder y = x * x, z = y * y (zwei Multiplikationen), also z = (x^2)^2.",
        "Beschreibung", 0, null);
    einleitung
        .addCodeLine(
            "Ebenso können auch andere ganzzahlige Potenzen durch fortgesetztes Quadrieren",
            "Beschreibung", 0, null);
    einleitung.addCodeLine(
        "und gelegentliches Multiplizieren effizient berechnet werden.",
        "Beschreibung", 0, null);

    Rect bg_einleitung = lang.newRect(new Offset(-5, -5, einleitung,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, einleitung,
        AnimalScript.DIRECTION_SE), "bg_einleitung", null, rectProps);

    lang.nextStep("Einleitung");
    /*
     * Step 2 - Algorithmus
     */

    einleitung.hide();
    bg_einleitung.hide();

    SourceCode code = lang.newSourceCode(new OffsetCoords(titel.getUpperLeft(),
        0, 20), "code", null, scProps);
    code.addCodeLine("Algorithmus:", null, 0, null);
    code.addCodeLine("1. Umwandlung des Exponenten in Binärdarstellung", null,
        0, null);
    code.addCodeLine("2. Zwischenergebnis mit 1 initialisieren", null, 0, null);
    code.addCodeLine("3. Den Exponenten schrittweise durchgehen", null, 0, null);
    code.addCodeLine(" - Bei einer 0 wird das aktuelle Ergebnis quadriert",
        null, 0, null);
    code.addCodeLine(" - Bei einer 1:", null, 0, null);
    code.addCodeLine("   - wird das aktuelle Ergebnis quadriert", null, 0, null);
    code.addCodeLine("   - und mit der Basis multipliziert", null, 0, null);

    Rect bg_code = lang.newRect(new Offset(-5, -5, code,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, code,
        AnimalScript.DIRECTION_SE), "bg_code", null, rectProps);

    lang.nextStep("Algorithmus");
    /*
     * Step 3 - Aufgabe
     */

    // Platzhalter

    int ph1 = ((int) Math.log10((int) a)) * 10 + 65;
    int ph2 = (int) Math.log10((int) b) * 15 + ph1 + 10;
    int ph3 = ((int) Math.log10((int) mod)) * 10 + 50;

    // Aufgabe
    Text aufgabe = lang.newText(new OffsetCoords(code.getUpperLeft(), 0, 160),
        "Aufgabe:  " + a, "aufgabe", null, textProps);
    // Exponent
    Text aufgabe2 = lang.newText(new OffsetCoords(aufgabe.getUpperLeft(), ph1,
        -5), "" + b, "aufgabe2", null, textProps);
    // Modul
    Text aufgabe3 = lang.newText(new OffsetCoords(aufgabe.getUpperLeft(), ph2,
        0), "mod " + mod, "aufgabe3", null, textProps);

    Rect bg_aufgabe = lang.newRect(new OffsetCoords(aufgabe.getUpperLeft(), -5,
        -5), new OffsetCoords(aufgabe3.getUpperLeft(), ph3, 30), "bg_aufgabe",
        null, rectProps);

    lang.nextStep("Aufgabe");
    /*
     * Step 4 - Binärdarstellung erzeugen
     */

    code.highlight(1);

    Text aufgabe02 = lang.newText(new OffsetCoords(aufgabe.getUpperLeft(), 0,
        80), b + " => ", "aufgabe2", null, textProps);
    int[] arr = bin(b, new int[0]);
    IntArray array = lang.newIntArray(new OffsetCoords(
        aufgabe02.getUpperLeft(), 60, 0), arr, "array", null, arrayProps);

    String binaryString = "";
    for (int i = 0; i < arr.length; i++) {
      binaryString += String.valueOf(arr[i]);
    }
    CheckpointUtils.checkpointEvent(this, "exponentToBinary", new Variable(
        "binaryExponent", binaryString));

    lang.nextStep("Binärdarstellung");
    /*
     * Step 5 - Initialisierung
     */

    code.unhighlight(1);
    code.highlight(2);

    aufgabe02.hide();

    Text ergebnis = lang.newText(new OffsetCoords(array.getUpperLeft(), 0, 30),
        "Zwischenergebnis: ", "ergebnis", null, textProps);
    Text mult = lang.newText(new OffsetCoords(ergebnis.getUpperLeft(), 0, 20),
        "Multiplikationen: ", "mult", null, textProps);
    Text ergebnis01 = lang.newText(new OffsetCoords(ergebnis.getUpperLeft(),
        110, 0), "" + ze, "ergebnis01", null, textProps);
    Text mult01 = lang.newText(new OffsetCoords(mult.getUpperLeft(), 110, 0),
        "" + multiplikationen, "mult01", null, textProps);
    z = 1;
    lang.nextStep("Initialisierung");
    /*
     * Step's 6 bis 6 + n - Berechnung
     */

    code.unhighlight(2);
    code.highlight(3);
    BigInteger x = BigInteger.ZERO;
    ArrayMarker am = lang.newArrayMarker(array, 0, "am", null);

    int quadrierenCount = 0;
    int multiplizierenCount = 0;
    // int zwischenErgebnisProSchritt = 0;
    for (int n = 0; n < arr.length; n++) {

      if (n != 0) {

        am.increment(null, defaultTiming);

      }

      array.highlightCell(n, null, null);
      if (arr[n] == 1) {
        code.highlight(5);
        code.highlight(6);
        code.unhighlight(4);
        code.unhighlight(7);
        x = ze;
        ze = x.multiply(x).mod(BigInteger.valueOf(mod)); // Quadrieren

        // zwischenErgebnisProSchritt = ze.intValue();
        CheckpointUtils.checkpointEvent(this, "zwischenErgebnis", new Variable(
            "zwErg", ze));

        quadrierenCount++;
        multiplikationen++;
        ergebnis01.setText(x + " * " + x + " mod " + mod + " = " + ze
            + "  (Quadrieren)", null, null);
        mult01.setText("" + multiplikationen, null, null);

        lang.nextStep("Berechnung (" + z + ")");
        z++;

        code.highlight(7);
        code.unhighlight(6);
        x = ze;
        ze = (x.multiply(BigInteger.valueOf(a))).mod(BigInteger.valueOf(mod)); // Multiplizieren

        // zwischenErgebnisProSchritt = ze.intValue();
        CheckpointUtils.checkpointEvent(this, "zwischenErgebnis", new Variable(
            "zwErg", ze));

        multiplizierenCount++;
        multiplikationen++;
        ergebnis01.setText(x + " * " + a + " mod " + mod + " = " + ze
            + "  (Multiplizieren)", null, null);
        mult01.setText("" + multiplikationen, null, null);
      } else {
        code.highlight(4);
        code.unhighlight(5);
        code.unhighlight(6);
        code.unhighlight(7);
        x = ze;
        ze = x.multiply(x).mod(BigInteger.valueOf(mod)); // Quadrieren

        // zwischenErgebnisProSchritt = ze.intValue();
        CheckpointUtils.checkpointEvent(this, "zwischenErgebnis", new Variable(
            "zwErg", ze));

        quadrierenCount++;
        multiplikationen++;
        ergebnis01.setText(x + " * " + x + " mod " + mod + " = " + ze
            + "  (Quadrieren)", null, null);
        mult01.setText("" + multiplikationen, null, null);

      }

      lang.nextStep("Berechnung (" + z + ")");
      z++;
    }
    CheckpointUtils.checkpointEvent(this, "countMultQuadEvent", new Variable(
        "quad", quadrierenCount), new Variable("mult", multiplizierenCount));
    CheckpointUtils.checkpointEvent(this, "ergEvent", new Variable("erg", ze));

    /*
     * Step n + 7 - Berechnung fertig!
     */

    Text fertig = lang.newText(new OffsetCoords(mult.getUpperLeft(), 0, 20),
        "Der Exponent wurde komplett eingelesen. Das Ergebnis ist " + ze + ".",
        "fertig", null, textProps);

    lang.nextStep("Ergebnis");
    /*
     * Step n + 8 - Ende
     */
    code.hide();
    bg_code.hide();
    array.hide();
    aufgabe.hide();
    aufgabe2.hide();
    aufgabe3.hide();
    bg_aufgabe.hide();
    ergebnis.hide();
    ergebnis01.hide();
    mult.hide();
    mult01.hide();
    fertig.hide();

    SourceCode ende = lang.newSourceCode(new OffsetCoords(titel.getUpperLeft(),
        0, 20), "ende", null);
    ende.addCodeLine(
        "Anwendung findet dieser Algorithmus beispielsweise in der Kryptographie.",
        null, 0, null);
    ende.addCodeLine(
        "Beim Ver- und Entschlüsseln mit RSA wird eine Nachicht potenziert.",
        null, 0, null);
    ende.addCodeLine(
        "Dabei kann der Exponent sehr groß sein. Dadurch ist ein effizienterer ",
        null, 0, null);
    ende.addCodeLine("Algorithmus zum potenzieren sehr hilfreich.", null, 0,
        null);
    ende.addCodeLine(
        "Die Komplexität verringert sich durch diesen Algorithmus von (e := Exponent)",
        null, 0, null);
    ende.addCodeLine("O(e) auf O(log(e)).", null, 0, null);
    ende.addCodeLine("In unserem Beispiel haben wir also nur "
        + multiplikationen + " statt " + b + " Multiplikationen benötigt.",
        null, 0, null);

    lang.newRect(new Offset(-5, -5, ende, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, ende, AnimalScript.DIRECTION_SE), "bg_ende", null,
        rectProps);

    lang.nextStep("Fazit");
    lang.hideAllPrimitives();

  }

  public static int[] bin(int x, int[] akk) {
    int[] akk2 = akk;
    int[] akkA = akk2;
    if (x == 0) {
      return akkA;
    } else {
      if (x % 2 == 1) {
        akk2 = new int[akkA.length + 1];
        akk2[0] = 1;
        for (int i = 0; i < akkA.length; i++) {
          akk2[i + 1] = akkA[i];
        }
        akkA = bin((x - 1) / 2, akk2); // 1
      } else {
        akk2 = new int[akkA.length + 1];
        akk2[0] = 0;
        for (int i = 0; i < akkA.length; i++) {
          akk2[i + 1] = akkA[i];
        }
        akkA = bin(x / 2, akk2); // 0

      }

    }
    return akkA;

  }

}