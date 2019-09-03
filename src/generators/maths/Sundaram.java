package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JFileChooser;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Sundaram implements generators.framework.Generator {
  /*
   * m =: Floor of N/2 L =: List of numbers from 1 to m For every solution (i,j)
   * to i + j + 2ij < m: Remove i + j + 2ij from L
   * 
   * For each k remaining in L: 2k + 1 is prime.
   */
  // debugging
  private boolean              printFile                = false;

  // Farben
  private Color                arrayMarkerColor         = Color.blue;
  private Color                codeHighlihgtColor       = Color.blue;
  private Color                counterViewColor         = Color.yellow;
  private Color                headlineBgColor          = Color.WHITE;
  private Color                sourceCodeHighlightColor = Color.red;
  private Color                matrixFillColor          = Color.white;
  private Color                arrayFillColor           = Color.white;
  private Color                counterFillColor         = Color.white;
  private Color                matrixIsPrime            = Color.green;
  private Color                matrixIsNotPrime         = Color.red;
  private Color                matrixIsBlacked          = Color.black;

  // Fonts
  Font                         sourceCodeFont;
  Font                         headlineFont;
  Font                         defaultTextFont;

  // Variablen fuer Animal
  private Language             language;
  // private Text header;
  private Text                 topic;
  private Text                 siebCounters;
  private Text                 resultCounter;
  // private Rect hRect;
  private SourceCode           src;
  private SourceCodeProperties sourceCodeProps;
  // private IntArray results;
  private IntArray             countArray;
  private IntArray             matrixAsArray;
  // private MatrixPrimitive matrix;
  private ArrayMarker          marker;
  private TwoValueCounter      counter;

  // Variablen des Algorithmus
  private int                  maxPrim;
  private int                  m;

  // Variablen fuer Hilfsfunktionen
  private List<Integer>        res;

  // Ausgabe in Datei
  private FileWriter           writer;
  private File                 file;

  public Sundaram(Language aLang) {
    language = aLang;
    language.setStepMode(true);
  }

  public Sundaram() {
    language = new AnimalScript("Sieb des Sundaram [DE]",
        "Marcel Andreas Gazsi, Mark Schneemann", 800, 600);
    language.setStepMode(true);
  }

  public void runSundaram(int maxPrim) {

    // Beschreibungsfolie
    showHeadline(maxPrim);
    showTopic();
    showDescription();

    // Quellcode anzeigen
    showSourceCode();

    // matrix aufbauen
    m = (maxPrim + 1) / 2;
    showMatrix(m);
    showMatrixAsArray();
    showCounterView();

    // Gitter anlegen
    language
        .addLine("relRectangle \"trenner1\" (430, 100) (0, 290)  color black");

    // initialisierung
    topic.setText("Initialisierung", null, null); // Ueberschrift setzen
    language.nextStep("Sieb initialisieren");
    initialStep(maxPrim);

    // sieben
    language.nextStep("Sieben durchführen");
    sieveIt();

    // Ergebnisse sammeln
    language.nextStep("Ergebnissieb auslesen");
    getResult();

    // Ausgabe der Ergebnisfolie
    hideSourceCode();
    topic.setText("Auswertung", null, null);
    showLastPage();

    // Print AnimalScript
    if (printFile)
      generateASU(language.toString());
  }

  // ------------ Schritte der Codeausfuerung ------------//
  private void initialStep(int maxPrim) {
    m = (maxPrim + 1) / 2; // Siebgroesse waehlen

    src.highlight(0);
    src.highlight(1);
    src.highlight(2);
    src.highlight(3);
    src.highlight(4);
    language.nextStep();
    // Sieb initialisieren
    for (int i = 1; i < m; i++) {
      countArray.put(i, 0, null, null);
    }
    countArray.hide();

    src.unhighlight(1);
    src.unhighlight(2);
    src.unhighlight(3);
    src.unhighlight(4);
    src.highlight(5);
    src.highlight(6);
    src.highlight(7);
    src.highlight(8);
    src.highlight(9);
    src.highlight(10);
    language.nextStep();
    src.unhighlight(5);
    src.unhighlight(6);
    src.unhighlight(7);
    src.unhighlight(8);
    src.unhighlight(9);
    src.unhighlight(10);
  }

  private void sieveIt() {
    topic.setText("Sieben", null, null); // Ueberschrift setzen
    // Zaehler fuer i,j und i+j+2ij visualisieren
    TextProperties siebCountersProps = new TextProperties();
    siebCountersProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.ITALIC, 12));
    siebCounters = language.newText(new Coordinates(550, 160),
        "i = 0; j = 0; i+j+2ij = 0", "siebCounters", null, siebCountersProps);

    // Siebvorgang durchlaufen
    for (int i = 1; (i < m) && ((i + i + (2 * i * i) < m)); i++) {
      src.unhighlight(14);
      src.highlight(12);
      language.nextStep();
      siebCounters.setText("i = " + i + "     j = 0     i+j+2ij = " + i + ";",
          null, null);
      for (int j = i; i + j + (2 * i * j) < m; j++) {
        src.unhighlight(12);
        src.unhighlight(14);
        src.highlight(13);
        language.nextStep();
        siebCounters.setText("i = " + i + "     j = " + j + "     i+j+2ij = "
            + (i + j + (2 * i * j)) + ";", null, null);
        countArray.put(i + j + (2 * i * j), 1, null, null);
        countArray.hide();
        // !!!!!!!!! NACHFRAGEN MATRIX HIGHLIGHT !!!!!!!!!!!
        language.addLine("#highlight " + (i + j + (2 * i * j)));
        // language.addLine("highlightGridCell \"matrix["+((i+j+(2*i*j)) /
        // 10)+"]["+ ((i+j+(2*i*j)) % 10)+"]\"");
        language.addLine("setGridColor \"matrix["
            + ((i + j + (2 * i * j)) / 10) + "]["
            + ((i + j + (2 * i * j)) % 10) + "]\" fillColor ("
            + matrixIsNotPrime.getRed() + ", " + matrixIsNotPrime.getGreen()
            + ", " + matrixIsNotPrime.getBlue() + ")");
        // !!!!!!!!! NACHFRAGEN MATRIX HIGHLIGHT !!!!!!!!!!!
        matrixAsArray.highlightCell((i + j + (2 * i * j) - 1), null, null);
        src.unhighlight(13);
        src.highlight(14);
        language.nextStep();
      }
    }
    siebCounters.hide();
  }

  private void getResult() {
    topic.setText("Ergebnisse sammeln", null, null); // Ueberschrift setzen
    showArrayMarker(); // ArrayMarker setzen

    // Zaehler fuer i und 2i+1 visualisieren
    TextProperties resultCounterProps = new TextProperties();
    resultCounterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.ITALIC, 12));
    resultCounter = language.newText(new Coordinates(550, 260),
        "i = 1; 2i + 1 = 3", "siebCounters", null, resultCounterProps);

    // Ergebnisvektor initialisieren
    res = new Vector<Integer>();
    res.add(2);

    // ErgebnisArray visualisieren
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set("fillColor", new Color(255, 255, 255));
    language.newIntArray(new Coordinates(20, 520), resultsAsInts(),
        "Ergebnisliste", null, arrayProps);

    // ErgebnisArray mit Ueberschrift versehen
    TextProperties arrayHdProps = new TextProperties();
    arrayHdProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 12));
    language.newText(new Coordinates(20, 500), "gefundene Primzahlen:",
        "gefundenePrimzahlen", null, arrayHdProps);

    // Ergebnisse auslesen
    for (int i = 1; i < m; i++) {
      marker.move(i - 1, null, null);
      language.addLine("setGridColor \"matrix[" + (i / 10) + "][" + (i % 10)
          + "]\" fillColor (0, 0, 255)");
      resultCounter.setText("i = " + i + "     2i + 1 = " + (2 * i + 1), null,
          null);
      src.unhighlight(14);
      src.unhighlight(20);
      src.unhighlight(21);
      src.highlight(19);
      language.nextStep();
      src.unhighlight(19);
      src.highlight(20);
      language.nextStep();
      countArray.hide();
      if (countArray.getData(i) == 0) {
        countArray.hide();
        res.add(2 * i + 1);
        language.addLine("setGridColor \"matrix[" + (i / 10) + "][" + (i % 10)
            + "]\" fillColor (" + matrixIsPrime.getRed() + ", "
            + matrixIsPrime.getGreen() + ", " + matrixIsPrime.getBlue() + ")");
        language.newIntArray(new Coordinates(20, 520), resultsAsInts(),
            "Ergebnisliste", null, arrayProps);
        src.unhighlight(20);
        src.highlight(21);
        language.nextStep();
      } else {
        language.addLine("setGridColor \"matrix[" + (i / 10) + "][" + (i % 10)
            + "]\" fillColor (" + matrixIsBlacked.getRed() + ", "
            + matrixIsBlacked.getGreen() + ", " + matrixIsBlacked.getBlue()
            + ")");
      }
    }
    src.unhighlight(21);
    src.unhighlight(19);
    resultCounter.hide();
  }

  // ------------ Wichtig fuer den Generator ------------ //

  public void init() {
    language = new AnimalScript("Sieb des Sundaram [DE]",
        "Marcel Andreas Gazsi, Mark Schneemann", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    language.setStepMode(true);

    counterViewColor = (Color) primitives.get("counterViewColor");
    arrayMarkerColor = (Color) primitives.get("arrayMarkerColor");
    counterFillColor = (Color) primitives.get("counterFillColor");
    maxPrim = (Integer) primitives.get("maxPrim");
    matrixFillColor = (Color) primitives.get("matrixFillColor");
    headlineBgColor = (Color) primitives.get("headlineBgColor");
    matrixIsPrime = (Color) primitives.get("matrixIsPrime");
    codeHighlihgtColor = (Color) primitives.get("codeHighlihgtColor");
    matrixIsNotPrime = (Color) primitives.get("matrixIsNotPrime");
    matrixIsBlacked = (Color) primitives.get("matrixIsBlacked");
    sourceCodeHighlightColor = (Color) primitives
        .get("sourceCodeHighlightColor");
    arrayFillColor = (Color) primitives.get("arrayFillColor");

    runSundaram(maxPrim);

    return language.toString();
  }

  public String getName() {
    return "Sieb des Sundaram [DE]";
  }

  public String getAlgorithmName() {
    return "Sieb des Sundaram";
  }

  public String getAnimationAuthor() {
    return "Marcel Andreas Gazsi, Mark Schneemann";
  }

  public String getDescription() {
    return "Beschreibung:"
        + "\n"
        + "Das Sieb des Eratosthenes ist die bekannteste M&ouml;glichkeit zum finden von Primzahlen."
        + "\n"
        + "Doch eine mindestens genauso interessante aber kaum bekannte M&ouml;glichkeit ist das Sieb des Sundaram. "
        + "\n"
        + "Dieser Algorithmus wurde 1934 vom indischen Mathematiker S. P. Sundaram entwickelt und erm&ouml;glicht es Primzahlen in einer Laufzeit von O(nlog(n)) zu finden."
        + "\n"
        + "\n"
        + "\n"
        + "Ablauf des Algorithmus:"
        + "\n"
        + "Der Algorithmus selbst legt bei der Suche nach allen Primzahlen kleiner oder gleich n zuerst ein Sieb der Gr&ouml;&szlig;e n/2 an. "
        + "\n"
        + "Anschlie&szlig;end werden aus diesem Sieb alle Zahlen gestrichen, die in der Form i + j +2ij geschrieben werden k&ouml;nnen. Alle &uuml;brigen Zahlen multipliziert mit 2 und um 1 erh&ouml;ht sind Primzahlen. "
        + "\n"
        + "Da die 2 in der gefundenen Liste fehlt (Begr&uuml;ndung in der Mathematischen Erkl&auml;rung) muss diese noch manuell zur Ergebnisliste hinzugef&uuml;gt werden."
        + "\n"
        + "\n"
        + "\n"
        + "Mathematische Erkl&auml;rung:"
        + "\n"
        + "Eine Zahl die sich als 2k + 1 schreiben l&auml;sst wobei k = i + j + 2ij ist keine Primzahl, da"
        + "\n"
        + "\n"
        + "	   2k + 1 "
        + "\n"
        + "	= 2(i + j + 2ij) "
        + "\n"
        + "	= 2i + 2j + 4ij + 1 "
        + "\n"
        + "	= (2i + 1)(2j + 1)"
        + "\n"
        + "\n"
        + "gilt, wobei 2i+1 und 2j+1 ungerade Zahlen sind."
        + "\n"
        + "\n"
        + "Da alle ungeraden Zahlen, die sich nicht in dieser Form darstellen lassen nicht als Multiplikation von ungeraden Zahlen zu schreiben sind m&uuml;ssen sie Primzahlen sein. "
        + "\n"
        + "Die einzige Primzahl, die auf diesem Weg nicht gefunden wird ist die einzige gerade Primzahl - die 2. Daher muss diese manuell an die Liste der Primzahlen angef&uuml;gt werden."
        + "\n"
        + "\n"
        + "\n"
        + "&Auml;hnliche Algorithmen:"
        + "\n"
        + "Sieb des Eratothenes, Kiebart-Kamm,  Sieb des Atkin, Sieb des Fermat, Sieb des Ulam";
  }

  public String getCodeExample() {
    return "public Vector<Integer> runSundaram(int maxPrim){" + "\n"
        + "	Vector<Integer> result = new Vector<Integer>();" + "\n"
        + "	result.add(2);" + "\n" + "	int m = maxPrim / 2;" + "\n"
        + "	boolean[] L = new boolean[m];" + "\n" + "\n" + "	// initialisieren"
        + "\n" + "	for(int i = 0; i < m; i++){" + "\n" + "		L[i] = false;"
        + "\n" + "		}" + "\n" + "\n" + "	// sieben" + "\n"
        + "	for(int i=1; (i<m) && ((i + i + (2 * i * i) < m)); i++){" + "\n"
        + "		for(int j=i; i + j + (2 * i * j) < m ; j++){" + "\n"
        + "			L[i+j+(2*i*j)] = true;" + "\n" + "			}" + "\n" + "		}" + "\n"
        + "\n" + "	// Ergebnisse sammeln" + "\n"
        + "	for(int i = 1; i < m; i++){" + "\n" + "		if(!L[i]){" + "\n"
        + "			result.add(2 * i + 1);" + "\n" + "			}" + "\n" + "		}" + "\n"
        + "	return result;" + "\n" + "	}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  // ------------- Ausgelagerte optische Elemente ------ //
  private void showHeadline(int maxPrim) {
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    language.newText(new Coordinates(20, 30), "Sieb des Sundaram mit n = "
        + maxPrim, "header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, headlineBgColor);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    language.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
  }

  // private void hideHeadline() {
  // header.hide();
  // hRect.hide();
  // }

  private void showSourceCode() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 11));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        codeHighlihgtColor);

    src = language.newSourceCode(new Coordinates(450, 0), "sourceCode", null,
        sourceCodeProps);
    src.addCodeLine("public Vector<Integer> runSundaram(int maxPrim){", null,
        0, null); // 0
    src.addCodeLine("Vector<Integer> result = new Vector<Integer>();", null, 1,
        null); // erg
    src.addCodeLine("result.add(2);", null, 1, null); // erg
    src.addCodeLine("int m = maxPrim / 2;", null, 1, null); // 1
    src.addCodeLine("boolean[] Sieb = new boolean[m];", null, 1, null); // 2
    src.addCodeLine("", null, 1, null); // 3
    // initialisierung
    src.addCodeLine("// initialisierung", null, 1, null); // 4
    src.addCodeLine("for(int i = 0; i < m; i++){", null, 1, null); // 5
    src.addCodeLine("Sieb[i] = false;", null, 2, null); // 6
    src.addCodeLine("}", null, 2, null); // 7
    src.addCodeLine("", null, 1, null); // 8
    // sieben
    src.addCodeLine("// sieben", null, 1, null); // 9
    src.addCodeLine("for(int i=1; (i<m) && ((i + i + (2 * i * i) < m)); i++){",
        null, 1, null); // 10
    src.addCodeLine("for(int j=i; i + j + (2 * i * j) < m ; j++){", null, 2,
        null); // 11
    src.addCodeLine("Sieb[i+j+(2*i*j)] = true;", null, 3, null); // 12
    src.addCodeLine("}", null, 3, null); // 13
    src.addCodeLine("}", null, 2, null); // 14
    src.addCodeLine("", null, 1, null); // 15
    // Ergebnisse Sammeln
    src.addCodeLine("// Ergebnisse Sammeln", null, 1, null); // 16
    src.addCodeLine("for(int i = 1; i < m; i++){", null, 1, null); // 17
    src.addCodeLine("if(!Sieb[i]){", null, 2, null); // 18
    src.addCodeLine("result.add(2 * i + 1))", null, 3, null); // 19
    src.addCodeLine("}", null, 3, null); // 20
    src.addCodeLine("return result;", null, 1, null); // 20
    src.addCodeLine("}", null, 2, null); // 21
  }

  private void hideSourceCode() {
    src.hide();
  }

  private void showDescription() {
    showIntro();
    showMath();
  }

  private void showIntro() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        codeHighlihgtColor);

    SourceCode intro = language.newSourceCode(new Coordinates(20, 100),
        "introCode", null, sourceCodeProps);
    intro
        .addCodeLine(
            "Das Sieb des Eratosthenes ist die bekannteste Möglichkeit zum finden von Primzahlen.",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "Doch eine mindestens genauso interessante aber kaum bekannte Möglichkeit ist das Sieb des",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "Sundaram. Dieser Algorithmus wurde 1934 vom indischen Mathematiker S. P. Sundaram entwickelt",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "und ermöglicht es Primzahlen in einer Laufzeit von O(nlog(n)) zu finden.",
            null, 0, null); // 0
    intro.addCodeLine("", null, 0, null); // 0
    intro.addCodeLine("Ablauf des Algorithmus:", null, 0, null); // 0
    intro
        .addCodeLine(
            "Der Algorithmus selbst legt bei der Suche nach allen Primzahlen kleiner oder gleich n zuerst ein",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "Sieb der Größe n/2 an. Anschließend werden aus diesem Sieb alle Zahlen gestrichen, die in der",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "Form i + j +2ij geschrieben werden können. Alle übrigen Zahlen multipliziert mit 2 und um 1",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "erhöht sind Primzahlen. Da die 2 in der gefundenen Liste fehlt (Begründung in der Mathematischen",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "Erklärung) muss diese noch manuell zur Ergebnisliste hinzugefügt werden.",
            null, 0, null); // 0

    language.nextStep();
    intro.hide();
  }

  private void showMath() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        codeHighlihgtColor);

    SourceCode intro = language.newSourceCode(new Coordinates(20, 100),
        "introCode", null, sourceCodeProps);
    intro.addCodeLine("Mathematische Erklärung:", null, 0, null); // 0
    intro
        .addCodeLine(
            "Eine Zahl die sich als 2k + 1 schreiben lässt wobei k = i + j + 2ij ist keine Primzahl, da",
            null, 0, null); // 0
    intro.addCodeLine("", null, 0, null); // 0
    intro.addCodeLine("   2k + 1 ", null, 2, null); // 0
    intro.addCodeLine("= 2(i + j + 2ij) ", null, 2, null); // 0
    intro.addCodeLine("= 2i + 2j + 4ij + 1 ", null, 2, null); // 0
    intro.addCodeLine("= (2i + 1)(2j + 1)", null, 2, null); // 0
    intro.addCodeLine("", null, 0, null); // 0
    intro.addCodeLine("gilt, wobei 2i+1 und 2j+1 ungerade Zahlen sind.", null,
        0, null); // 0
    intro
        .addCodeLine(
            "Da alle ungeraden Zahlen, die sich nicht in dieser Form darstellen lassen nicht als Multiplikation",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "von ungeraden Zahlen zu schreiben sind müssen sie Primzahlen sein. Die einzige Primzahl, die auf",
            null, 0, null); // 0
    intro
        .addCodeLine(
            "diesem Weg nicht gefunden wird ist die einzige gerade Primzahl - die 2. Daher muss diese manuell",
            null, 0, null); // 0
    intro.addCodeLine("an die Liste der Primzahlen angefügt werden.", null, 0,
        null); // 0
    intro.addCodeLine("", null, 0, null); // 0
    intro.addCodeLine("", null, 0, null); // 0
    intro.addCodeLine("Ähnliche Algorithmen:", null, 0, null); // 0
    intro
        .addCodeLine(
            "Sieb des Eratothenes, Kiebart-Kamm,  Sieb des Atkin, Sieb des Fermat, Sieb des Ulam",
            null, 0, null); // 0

    language.nextStep();
    intro.hide();
  }

  private void showLastPage() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        sourceCodeHighlightColor);

    SourceCode finText = language.newSourceCode(new Coordinates(450, 50),
        "lastText", null, sourceCodeProps);
    finText.addCodeLine("- bei der Initialisierung wurde das Sieb...", null, 0,
        null);
    finText.addCodeLine("... " + (m - 1) + " mal geschrieben", null, 1, null);
    finText.addCodeLine("", null, 1, null);
    finText.addCodeLine("- beim Sieben wurde das Sieb...", null, 0, null);
    finText.addCodeLine("... " + (counter.getAssigments() - (m - 1))
        + " mal geschrieben", null, 1, null);
    finText.addCodeLine("", null, 1, null);
    finText.addCodeLine("- beim Ergenisse sammeln...", null, 0, null);
    finText.addCodeLine("... " + counter.getAccess() + " mal gelesen", null, 1,
        null);
    finText.addCodeLine("", null, 1, null);
    finText.addCodeLine("- insgesamt wurden " + res.size()
        + " Primzahlen gefunden.", null, 0, null);
    finText.addCodeLine("", null, 1, null);
    finText.addCodeLine("- es wurden lediglich " + (m - 1)
        + " booleans als Sieb benutzt", null, 0, null);

    marker.hide();
  }

  // private void hideLastPage() {
  //
  // }

  private void showMatrix(int elementNumber) {
    String[][] matrixString;
    int n;
    if (elementNumber % 10 == 0) {
      n = elementNumber / 10;
    } else {
      n = elementNumber / 10 + 1;
    }
    matrixString = new String[n][10];

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < n; j++) {
        if (i == 0 && j == 0)
          matrixString[0][0] = "";
        else
          matrixString[j][i] = "" + (j * 10 + i);
        if ((j * 10 + i) >= elementNumber)
          matrixString[j][i] = "";
      }
    }

    TextProperties matrixHdProps = new TextProperties();
    matrixHdProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 12));
    language.newText(new Coordinates(20, 105), "Sieb als Matrix:", "arraySieb",
        null, matrixHdProps);

    MatrixProperties matProp = new MatrixProperties();
    matProp.set("fillColor", matrixFillColor);
    language.newStringMatrix(new Coordinates(20, 135), matrixString, "matrix",
        null, matProp);
    language
        .addLine("grid \"matrix\" (20, 135) lines "
            + n
            + " columns 10  color (0, 0, 0) textColor (0, 0, 0) fillColor (255, 255, 255) highlightTextColor (255, 0, 0) highlightBackColor (255, 255, 255) depth 0");
  }

  private void showMatrixAsArray() {
    TextProperties arrayHdProps = new TextProperties();
    arrayHdProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 12));
    language.newText(new Coordinates(20, 375), "Sieb als Array:", "arraySieb",
        null, arrayHdProps);

    int[] matrixArray = new int[m - 1];
    for (int i = 0; i < m - 1; i++) {
      matrixArray[i] = i + 1;
    }
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set("fillColor", arrayFillColor);
    matrixAsArray = language.newIntArray(new Coordinates(20, 400), matrixArray,
        "Ergebnisliste", null, arrayProps);
  }

  private void showArrayMarker() {
    ArrayMarkerProperties arrayMarkerProps = new ArrayMarkerProperties();
    arrayMarkerProps.set("color", arrayMarkerColor);
    marker = language.newArrayMarker(matrixAsArray, 0, "imarker", null,
        arrayMarkerProps);
  }

  private void showCounterView() {
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set("fillColor", counterFillColor);
    countArray = language.newIntArray(new Coordinates(0, 0),
        booleanArrayAsInts(), "boolSieb", null, arrayProps);
    counter = language.newCounter(countArray);
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, counterViewColor);
    String[] values = new String[2];
    values[0] = "Schreibzugriffe:";
    values[1] = "Lesezugriffe:";
    language.newCounterView(counter, new Coordinates(20, 445), cp, true, true,
        values);
    countArray.hide();
  }

  private void showTopic() {
    TextProperties descrHdProps = new TextProperties();
    descrHdProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 18));
    topic = language.newText(new Coordinates(20, 70), "Beschreibung",
        "descrHD", null, descrHdProps);
  }

  // ----------- Hilfsfunktionen ----------//
  private int[] resultsAsInts() {
    int[] result = new int[res.size()];

    for (int i = 0; i < res.size(); i++) {
      result[i] = res.get(i);
    }

    return result;
  }

  private int[] booleanArrayAsInts() {
    int[] result = new int[m];
    for (int i = 0; i < m; i++)
      result[i] = 0;
    return result;
  }

  private void generateASU(String code) {
    try {
      JFileChooser chooser = new JFileChooser();
      chooser.showSaveDialog(null);
      file = new File(chooser.getSelectedFile().getAbsoluteFile() + ".asu");
      writer = new FileWriter(file, false);
      writer.write(code);
      writer.flush();
      writer.close();
      System.out.println("Datei .asu wurde erfolgreich erstellt");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Fehler beim Erzeugen der .asu Datei.");
    }
  }

}
