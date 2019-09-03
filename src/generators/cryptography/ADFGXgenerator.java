package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class ADFGXgenerator implements Generator {
  private Language lang;
  private String   Codewort;
  private String   Nachricht;
  private String[] Geheimalphabet;

  public void init() {
    lang = new AnimalScript("ADFGX-Verschlüsselung",
        "Taylan Özden, Jonas Dopf", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Codewort = (String) primitives.get("Codewort");
    Nachricht = (String) primitives.get("Nachricht");
    Geheimalphabet = (String[]) primitives.get("Geheimalphabet");

    // Hat Geheimalphabet richtige laenge?
    if (Geheimalphabet.length != 25) {
      lang.newText(
          new Coordinates(20, 20),
          "FEHLER: Die Länge des Geheimalphabets muss 25 betragen! Siehe auch: https://en.wikipedia.org/wiki/ADFGVX_cipher#Operation_of_ADFGX",
          "fehlerWegenLaenge", null);
      System.out
          .println("FEHLER: Die Länge des Geheimalphabets muss 25 betragen! Siehe: https://en.wikipedia.org/wiki/ADFGVX_cipher#Operation_of_ADFGX");
      return lang.toString();
    }

    // Ist jeder Buchstabe der Nachricht in Geheimalphabet vertreten?

    boolean error = false;
    String fehlendeBuchstaben = "";

    for (int i = 0; i < Nachricht.length(); i++) {
      if (!contains(Nachricht.charAt(i), Geheimalphabet)) {
        error = true;
        fehlendeBuchstaben = fehlendeBuchstaben + " " + Nachricht.charAt(i);
      }
    }

    if (error) {
      String fehler = "FEHLER: Nicht jeder Buchstabe der Nachricht ist im Geheimalphabet vertreten: "
          + fehlendeBuchstaben
          + ". Siehe: https://en.wikipedia.org/wiki/ADFGVX_cipher#Operation_of_ADFGX";
      lang.newText(new Coordinates(20, 20), fehler, "fehlerWegenNachricht",
          null);
      System.out.println(fehler);
      return lang.toString();
    }

    run(lang, Geheimalphabet, Codewort, Nachricht);

    lang.finalizeGeneration();
    return lang.toString();
  }

  /**
   * &Uuml;berpfr&uuml;ft, ob der Buchstabe i im Array enthalten ist. return
   * true, Wenn i in array return false, wenn nicht.
   */
  private static boolean contains(char i, String[] array) {

    for (int j = 0; j < array.length; j++) {
      if (array[j].equals("" + i))
        return true;
    }
    return false;
  }

  public String getName() {
    return "ADFGX-Verschlüsselung";
  }

  public String getAlgorithmName() {
    return "ADFGX-Verschlüsselung";
  }

  public String getAnimationAuthor() {
    return "Taylan Özden, Jonas Dopf";
  }

  public String getDescription() {
    return "Das ADFGX-Verschl&uuml;sselungsverfahren wurde vom deutschen Milit&auml;r im Ersten Weltkrieg eingesetzt. Die Verschl&uuml;sselung geschieht zweistufig: "
        + "\n"
        + "1) Zunächst werden alle Buchstaben des Klartextes mithilfe eines Polybios-Quadrats substituiert."
        + "\n"
        + "Der daraus resultierende 'Zwischentext' besteht nur aus den Buchstaben A, D, F, G, X. "
        + "\n"
        + "\n"
        + "Da das Polybios-Quadrat nur 5x5 Buchstaben fasst, wurde wenig sp&auml;ter das ADFGVX-Verfahren benutzt, das analog zu diesem hier funktioniert -- das Polybios-Quadrat wurde lediglich auf 6x6 Buchstaben erweitert."
        + "\n"
        + "\n"
        + "2) Der Zwischentext wird im zweiten Schritt transponiert, indem er zeilenweise in eine Matrix eingetragen und spaltenweise wieder ausgelesen wird. Die Breite der Matrix resultiert aus der L&auml;nge des Codewortes (Transpositions-Schl&uuml;ssel)."
        + "\n"
        + "Auch wenn früher Transpositions-Schl&uuml;ssel der L&auml;nge 15 bis 22 eingesetzt wurden, sollten für diese Visualisierung deutlich k&uuml;rzere Schlüssel benutzt werden, um den Generierungsvorgang zu beschleunigen -- für das"
        + "\n"
        + "Verst&auml;ndnis reicht das allemal aus."
        + "\n"
        + "Wann welche Spalte der Matrix ausgelesen wird, ist über die alphabetische Reihenfolge der Buchstaben des Codewortes definiert."
        + "\n"
        + "\n"
        + "<br><br><font size ='4'><b>Hinweis: Orientieren Sie bez&uuml;glich der L&auml;nge des Codewortes und der Nachricht an der bereits gegebenen Beispielkonfiguration, um unn&ouml;tige Generierungszeit zu sparen.</b></font size>";
  }

  public String getCodeExample() {
    return "Pseudocode:"
        + "\n"
        + "\n"
        + "Hierbei handelt es sich lediglich um einen groben Pseudocode, um einen &Uuml;berblick über die zu absolvierenden Schritte zu geben."
        + "\n"
        + "\n"
        + "\n"
        + "// Schritt 1: Polybios F&uuml;llen"
        + "\n"
        + "\n"
        + "String[][] polybios = new String[5][5];"
        + "\n"
        + "Lege Integer i,j=0 an;"
        + "\n"
        + "\n"
        + "\n"
        + "F&uuml;r jede Zeile i mit i &lt; 5 {"
        + "\n"
        + "	F&uuml;r jede Spalte j mit j &lt; 5 {"
        + "\n"
        + "	"
        + "\n"
        + "		Wenn (i*5+j &lt; 'Anzahl der Bustaben in Geheimalphabet') "
        + "\n"
        + "			polybios[i][j]  &larr; 'i*5+j'-ten Buchstabe des Geheimalphabet;"
        + "\n"
        + "\n"
        + "		}"
        + "\n"
        + "	}"
        + "\n"
        + "		"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "<u>// Schritt 2: Substitution</u>"
        + "\n"
        + "Lege String 'zwischentext' an;"
        + "\n"
        + "\n"
        + "\n"
        + "F&uuml;r jeden Buchstaben i der Nachricht"
        + "\n"
        + "	durchlaufe Polybiosmatrix"
        + "\n"
        + "		wenn i == Eintrag in Polybiosmatrix"
        + "\n"
        + "			füge Koordinaten von i in zwischentext an"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "<u>// Schitt 3: Transposition: Füllen der Transpositionsmatrix</u>"
        + "\n"
        + "\n"
        + "Lege Matrix 'transpostion' an;"
        + "\n"
        + "\n"
        + "F&uuml;r jede Zeile i von 'transposition';"
        + "\n"
        + "	F&uuml;r jede Spalte j von 'transposition';"
        + "\n"
        + "	{"
        + "\n"
        + "		Wenn ( i*'L&auml;nge Codewort'+j &gt;= 'L&auml;nge zwischentext' ) dann RETURN;	// 'Zwischentext wurde vollst&auml;ndig eingefügt'"
        + "\n"
        + "		transposition[i][j]  &larr; 'i*5+j'-te Buchstabe von zwischentext;"
        + "\n"
        + "	}"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "<u>// Schritt 4: Spaltenweises Auslesen (Nach alphatetischer Reihnfolge der Buchstaben des Codewortes)</u>"
        + "\n"
        + "\n"
        + "Lege String 'chiffrat' an;"
        + "\n"
        + "\n"
        + "F&uuml;r jeden Buchstaben m des Alphabets		// m = 'A', 'B', ... , 'Z';"
        + "\n" + "	F&uuml;r jeden Buchstaben i des Codewortes" + "\n"
        + "		Wenn (i = m) " + "\n"
        + "			H&auml;nge jeden Buchstaben der 'm'-ten Spalte an 'chiffrat' an;";
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

  /* Hier beginnt nun unser eigentlicher code */

  /*
   * Begin Programmnotwendige Variablen
   */
  private String[]             alphabet;
  private String               pw2;
  private String               message;
  private String[][]           polybos;
  // private Map<Integer, String> polyMap;
  private String[]             adfgx;
  private String[]             encryptedOnce;
  private char[][]             transposition1;
  public int[]                 reinfolge;
  private char[][]             result;
  // private String encryptedMassage;

  /*
   * Animalnotwendige Variablen
   */

  private Language             algoAnimlang;
  private StringMatrix         algoAnimPolybosMatrix;
  private SourceCodeProperties scProps;
  private Text                 subUeberschift;

  private Polyline             algoAnimLinieUeberSC;

  private StringMatrix         algoAnimADFGXHor;
  private StringMatrix         algoAnimADFGXVert;
  private Polyline             waagrechteLinieFuerPolybiosMatrix;
  private Polyline             senkrechteLinieFuerPolibiosMatrix;
  private StringArray          algoAnimEncryptedOnceArray;
  private StringMatrix         algoAnimCodeWordMatrix;
  private Text                 algoAnimLabelZwischentext;
  private StringMatrix         algoAnimInhaltTranspositionArray;
  private Text                 algoAnimLableNachricht;
  private StringArray          messageArray;

  /**
   * Initialisiert die SourceCodePropertiers() Es werden verschiedene
   * SourceCode-Teile eingeblendet, alle diese Teile haben die hier
   * spezifizierte Propertiy
   */
  private void initDefaultSourceCodeProperties() {

    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  /**
   * Erstelle den Anfangstext, der den Algorithmus vorstellt
   */
  private void setTextForTitlepage() {

    Coordinates[] cc = new Coordinates[4];
    Coordinates nw = new Coordinates(15, 10);
    Coordinates ne = new Coordinates(300, 10);
    Coordinates se = new Coordinates(300, 40);
    Coordinates sw = new Coordinates(15, 40);
    cc[0] = nw;
    cc[1] = ne;
    cc[2] = se;
    cc[3] = sw;

    PolygonProperties pp = new PolygonProperties();
    pp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    pp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    try {
      algoAnimlang.newPolygon(cc, "hintergrundueberschrift", null, pp);
    } catch (NotEnoughNodesException e) {
      e.printStackTrace();
    }

    TextProperties ueberschiftProperties = new TextProperties();

    ueberschiftProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 22));

    TextProperties einleitungsText = new TextProperties();

    einleitungsText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));

    algoAnimlang.newText(new Coordinates(20, 20), "ADFGX-Verschlüsselung",
        "ueberschrift1", null, ueberschiftProperties);

    // Polygon hintergrundUeberschrift = new Polygon(pg, theNodes, name,
    // display, pp)

    String t, s, d, g, l;

    t = "Das ADFGX Verschlüsselungsverfahren wurde vom deutschen Militär im Ersten Weltkrieg eingesetzt. Die Verschlüsselung ";
    s = "geschieht zweistufig: Zunächst werden alle Buchstaben des Klartextes mithilfe eines Polybios-Quadrats";
    d = "substituiert. Der daraus resultierende 'Zwischentext' besteht nur aus den Buchstaben A, D, F, G, X. Er wird im zweiten Schritt transponiert,";
    g = "indem er zeilenweise in eine Matrix eingetragen und spaltenweise wieder ausgelesen wird. Da dieses Polybios-Quadrat nur 5x5 Buchstaben";
    l = "fasst, wurde es wenig später durch das ADFGVX-Verfahren ersetzt, das analog zu diesem hier funktioniert.";

    Text info = algoAnimlang.newText(new Coordinates(40, 60), t,
        "einleitungstext", null, einleitungsText);
    Text info2 = algoAnimlang.newText(new Coordinates(40, 90), s,
        "einleitungstext2", null, einleitungsText);
    Text info3 = algoAnimlang.newText(new Coordinates(40, 115), d,
        "einleitungstext3", null, einleitungsText);
    Text info4 = algoAnimlang.newText(new Coordinates(40, 140), g,
        "einleitungstext4", null, einleitungsText);
    Text info5 = algoAnimlang.newText(new Coordinates(40, 165), l,
        "einleitungstext5", null, einleitungsText);

    algoAnimlang.nextStep();
    info.hide();
    info2.hide();
    info3.hide();
    info4.hide();
    info5.hide();

  }

  /**
   * Das substitutionsalphabet wird in die polybos-matrix eingetragen
   * 
   * @return
   */
  private String[][] calculatePolybos() {
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    // now, create the IntArray object, linked to the properties
    StringArray alphabetArray = algoAnimlang.newStringArray(new Coordinates(20,
        100), alphabet, "alphabet", null, arrayProps);

    Text alphabetArrayBeschriftung = algoAnimlang.newText(new Coordinates(20,
        80), "Alphabet:", "label1212", null);

    String[][] temp = new String[1][5];
    temp[0][0] = "A";
    temp[0][1] = "D";
    temp[0][2] = "F";
    temp[0][3] = "G";
    temp[0][4] = "X";

    String[][] temp2 = new String[5][1];
    temp2[0][0] = "A";
    temp2[1][0] = "D";
    temp2[2][0] = "F";
    temp2[3][0] = "G";
    temp2[4][0] = "X";

    MatrixProperties mp1 = new MatrixProperties();
    mp1.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    mp1.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);

    SourceCode sc = this.generateSourceCodePolybosGenerierung();
    algoAnimADFGXHor = algoAnimlang.newStringMatrix(new Coordinates(40, 170),
        temp, "adfgxMatrix22", null, mp1);
    algoAnimADFGXVert = algoAnimlang.newStringMatrix(new Coordinates(15, 200),
        temp2, "adfgxMatrix22", null, mp1);
    Coordinates c1 = new Coordinates(38, 188);
    Coordinates c2 = new Coordinates(180, 188);
    Coordinates[] cc = new Coordinates[2];
    cc[0] = c1;
    cc[1] = c2;
    waagrechteLinieFuerPolybiosMatrix = algoAnimlang.newPolyline(cc, "test221",
        null);
    cc[1] = new Coordinates(38, 400);
    senkrechteLinieFuerPolibiosMatrix = algoAnimlang.newPolyline(cc,
        "test2221", null);
    cc[0] = new Coordinates(10, 520);
    cc[1] = new Coordinates(300, 520);
    algoAnimLinieUeberSC = algoAnimlang.newPolyline(cc, "lieneUeberSC1", null);

    subUeberschift = algoAnimlang.newText(new Coordinates(20, 40),
        "- Schritt 1: Polybiosmatrix füllen -", "subUeberschrift", null);

    algoAnimlang.nextStep("Schritt 1: Polybiosmatrix füllen");

    this.polybos = new String[5][5];

    // mit Leerzeichen initialisieren, sieht bei Animation besser aus....
    for (int i = 0; i < polybos.length; i++)
      for (int j = 0; j < polybos[0].length; j++)
        this.polybos[i][j] = " ";

    sc.highlight(0);
    MatrixProperties mp = new MatrixProperties();
    mp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    algoAnimPolybosMatrix = algoAnimlang.newStringMatrix(new Coordinates(40,
        200), polybos, "polybos", null, mp);// , null, null);
    algoAnimlang.nextStep();
    sc.unhighlight(0);

    for (int i = 0; i < 5; i++) {
      sc.highlight(1);
      algoAnimlang.nextStep();
      for (int j = 0; j < 5; j++) {
        sc.unhighlight(1);
        sc.highlight(2);
        algoAnimlang.nextStep();
        if (i * 5 + j < alphabet.length) {
          polybos[i][j] = alphabet[i * 5 + j];
          sc.unhighlight(2);
          sc.highlight(3);
          alphabetArray.highlightCell(i * 5 + j, null, null);
          algoAnimPolybosMatrix.highlightCell(i, j, null, null);
          algoAnimPolybosMatrix.put(i, j, alphabet[i * 5 + j], null, null);
          algoAnimlang.nextStep();
          alphabetArray.unhighlightCell(i * 5 + j, null, null);
          algoAnimPolybosMatrix.unhighlightCell(i, j, null, null);
          alphabetArray.highlightElem(i * 5 + j, null, null);
        }
        sc.unhighlight(2);
        sc.unhighlight(3);

      }
    }

    algoAnimlang.nextStep();
    alphabetArray.hide();
    alphabetArrayBeschriftung.hide();

    sc.hide();

    algoAnimlang.nextStep();

    return polybos;
  }

  /*
   * Erstellt den SourceCode f&uuml;r die Poybos-Matrix-Generierung Der
   * SourceCode benutzt die in initDefaultSourceCodeProperties() definierten
   * Properties
   */
  private SourceCode generateSourceCodePolybosGenerierung() {

    SourceCode sc = algoAnimlang.newSourceCode(new Coordinates(10, 520),
        "sourceCode", null, scProps);

    sc.addCodeLine("1. String[][] polybos = new String[5][5];", null, 0, null);
    sc.addCodeLine("2. for (int i = 0; i < 5; i++)", null, 0, null);
    sc.addCodeLine("3.     for (int j = 0; j < 5; j++) {", null, 0, null);
    sc.addCodeLine(
        "4.         if (i*5+j < alphabet.length) polybos[i][j] = alphabet[i*5+j];",
        null, 0, null);

    return sc;
  }

  /*
   * Erstellt den SourceCode f&uuml;r die Substitution Der SourceCode benutzt
   * die in initDefaultSourceCodeProperties() definierten Properties
   */
  private SourceCode generateSourceCodeSubstituate() {

    // now, create the source code entity
    SourceCode sc = algoAnimlang.newSourceCode(new Coordinates(10, 520),
        "sourceCode", null, scProps);

    sc.addCodeLine("Für jeden Buchstaben i der Nachricht", null, 0, null);
    sc.addCodeLine("durchlaufe Polybiosmatrix", null, 1, null);
    sc.addCodeLine("wenn i == Eintrag in Polybiosmatrix", null, 2, null);
    sc.addCodeLine("füge Koordinaten von i in Zwischentext ein", null, 3, null);

    return sc;

  }

  /**
   * Erzeugt ArrayMarkerProperties
   * 
   * @param name
   *          Der Text, der &uuml;ber dem Pfeil steht
   * @return ArrayMarkerProperties
   */
  private ArrayMarkerProperties getArrayMarkerProbs(String name) {
    ArrayMarkerProperties ap = new ArrayMarkerProperties();
    ap.set(AnimationPropertiesKeys.LABEL_PROPERTY, name);

    return ap;

  }

  /**
   * Die Nachricht wird mithilfe der Polybos Matrix substituiert PROBLEM:
   * GRO&szlig;- UND KLEINBUCHSTABEN WERDEN UNTERSCHIEDEN; WENN EXAKTE
   * BUCHSTABEN NICHT IM POLYBIOS-MATRIX KOMMT ES ZU PROBLEMEN!
   * 
   * @return
   */
  private String[] substituate() {

    /**
     * Algo-Anim Setting:
     */

    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));

    String[] messageArrayTemp = new String[this.message.length()];
    // Message in StringArray umformen
    for (int i = 0; i < this.message.length(); i++)
      messageArrayTemp[i] = "" + this.message.charAt(i);

    // now, create the StingArray object, linked to the properties
    messageArray = algoAnimlang.newStringArray(new Coordinates(300, 200),
        messageArrayTemp, "nachrichtenArray1231", null, arrayProps);

    SourceCode sc = generateSourceCodeSubstituate();
    subUeberschift.setText("- Schritt 2: Substitution -", null, null);
    /**
     * Algo-Anim Setting ende
     */

    encryptedOnce = new String[this.message.length() * 2];

    boolean found = false;
    int zaehler = 0;
    // Für die Animation, sieht schöner aus wenn das Array leer ist.
    for (int i = 0; i < encryptedOnce.length; i++)
      encryptedOnce[i] = " ";

    // now, create the IntArray object, linked to the properties
    algoAnimEncryptedOnceArray = algoAnimlang.newStringArray(new Coordinates(
        300, 300), encryptedOnce, "encryptedOnce", null, arrayProps);

    algoAnimLabelZwischentext = algoAnimlang.newText(new Coordinates(300, 280),
        "Zwischentext:", "labelzwischentext", null);

    ArrayMarker algoAnimArrayMarkerNachricht = algoAnimlang.newArrayMarker(
        messageArray, 0, "am1", null, getArrayMarkerProbs("Buchstabe i"));

    algoAnimlang.nextStep("Schritt 2: Substitution");

    // sc.highlight(0);
    // messageArray.highlightCell(0,null,null);
    // algoAnimlang.nextStep();

    // Für jeden Buchstaben der Nachricht ....
    for (int i = 0; i < this.message.length(); i++) {
      // messageArray.highlightCell(i,null,null);
      sc.highlight(0);
      algoAnimlang.nextStep();

      // sc.toggleHighlight(0, 1);
      found = false;

      // Durchsuche die gesamte Polybosnachricht ...
      for (int j = 0; j < 5; j++) {

        for (int m = 0; m < 5; m++) {
          sc.unhighlight(0);
          sc.highlight(1);
          algoAnimPolybosMatrix.highlightElem(j, m, null, null);

          algoAnimlang.nextStep();

          // algoAnimPolybosMatrix.highlightElem(j, m, new
          // MsTiming(((m+1)*1000)+(j*5000)), null);
          // sc.unhighlight(1, 0, false, new
          // MsTiming(((m+1)*1000)+500+(j*5000)), null);
          // sc.highlight(2, 0, false, new MsTiming(((m+1)*1000)+500+(j*5000)),
          // null);
          sc.unhighlight(1);
          sc.highlight(2);
          messageArray.highlightCell(i, null, null);

          // ... wenn entsprechender Eintrag gefunden ...
          if ((message.substring(i, i + 1)).equals(polybos[j][m])) {

            algoAnimlang.nextStep();

            sc.unhighlight(2);
            sc.highlight(3);

            algoAnimPolybosMatrix.highlightElem(j, m, null, null);
            // sc.toggleHighlight(2, 3);

            // algoAnimADFGXVert.highlightElem(j, 0, new MsTiming((1)*1000),
            // null);
            algoAnimADFGXVert.highlightElem(j, 0, null, null);
            encryptedOnce[zaehler] = adfgx[j];

            // algoAnimEncryptedOnceArray.put(zaehler, adfgx[j], new
            // MsTiming(2*1000), null);
            algoAnimEncryptedOnceArray.put(zaehler, adfgx[j], null, null);
            zaehler++;

            algoAnimADFGXHor.highlightCell(0, m, null, null);
            // algoAnimADFGXHor.highlightCell(0, m, new MsTiming(3*1000), null);
            encryptedOnce[zaehler] = adfgx[m];

            // algoAnimEncryptedOnceArray.put(zaehler, adfgx[m], new
            // MsTiming(4*1000), null);
            algoAnimEncryptedOnceArray.put(zaehler, adfgx[m], null, null);

            zaehler++;

            found = true;

            algoAnimlang.nextStep();
            algoAnimADFGXHor.unhighlightCell(0, m, null, null);
            algoAnimADFGXVert.unhighlightElem(j, 0, null, null);
            algoAnimPolybosMatrix.unhighlightCell(j, m, null, null);
            sc.unhighlight(3);
            break;

          }

          algoAnimlang.nextStep();

          if (!found) {
            algoAnimPolybosMatrix.unhighlightElem(j, m, null, null);
            sc.unhighlight(2);

            // algoAnimPolybosMatrix.unhighlightElem(j, m, new
            // MsTiming((m+2)*1000+(j*5000)), null);
            // sc.unhighlight(2, 0, false, new MsTiming((m+2)*1000+(j*5000)),
            // null);
            // sc.highlight(1, 0, false, new MsTiming((m+2)*1000+(j*5000)),
            // null);
          } else {

          }
          // algoAnimlang.nextStep();
        }

        if (found == true) {
          // algoAnimlang.nextStep();
          break;
        }

      }

      algoAnimArrayMarkerNachricht.increment(null, null);
      sc.highlight(0);
      messageArray.unhighlightCell(i, null, null);
      if (i + 1 < this.message.length()) {
        // sc.highlight(0);
        // algoAnimlang.nextStep();
      }

    }

    FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
        "fillInBlanksQuestion");
    fibq.setPrompt("Was ist das Problem bei der Benutzung einer 5x5 Polybios-Matrix");
    fibq.addAnswer("Nur 25 Buchstaben sind Benutzbar.", 10,
        "This is so easy...");
    fibq.addAnswer("Nicht genügend Buchstaben für das deutsche Alphabet.", 10,
        "This is so easy...");
    fibq.addAnswer("2 Buchstaben müssen gleich kodiert werden.", 10,
        "This is so easy...");
    fibq.setGroupID("First question group");
    algoAnimlang.addFIBQuestion(fibq);

    algoAnimPolybosMatrix.hide();
    algoAnimADFGXHor.hide();
    algoAnimADFGXVert.hide();
    waagrechteLinieFuerPolybiosMatrix.hide();
    senkrechteLinieFuerPolibiosMatrix.hide();
    sc.hide();
    algoAnimArrayMarkerNachricht.hide();
    algoAnimLableNachricht = algoAnimlang.newText(new Coordinates(300, 180),
        "Nachricht:", "algoAnimLableNachricht", null);

    algoAnimlang.nextStep();

    /*
     * Begin vorbereitung für Transposition: Verschiebung von
     * algoAnimLableNachricht, algoAnimEncryptedOnceArray,
     * algoAnimLabelZwischentext, messageArray
     */

    try {

      algoAnimLableNachricht.moveTo("NW", null, new Coordinates(400, 10), null,
          new MsTiming(250));
      messageArray.moveTo("NW", null, new Coordinates(400, 0), null,
          new MsTiming(250));
      algoAnimLabelZwischentext.moveTo("NW", null, new Coordinates(20, 75),
          null, new MsTiming(250));
      algoAnimEncryptedOnceArray.moveTo("NW", null, new Coordinates(20, 95),
          null, new MsTiming(250));
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    return encryptedOnce;

  }

  /*
   * Erstellt den SourceCode f&uuml;r den ersten Teil der Transposition Der
   * SourceCode benutzt die in initDefaultSourceCodeProperties() definierten
   * Properties
   */
  private SourceCode generateSourcecodeTransposeFirst() {
    SourceCode sc = algoAnimlang.newSourceCode(new Coordinates(10, 520),
        "sourceCode", null, scProps);

    // sc.addCodeLine("0 float t = (float) zwischentext.length / codewort.length();",
    // null, 0,null);
    // sc.addCodeLine("1 int breiteTranspositionsArray;",null,0,null);
    // sc.addCodeLine("2 ",null,0,null);
    // sc.addCodeLine("3 if (t % 1 == 0) breiteTranspositionsArray = zwischentext.length / codewort.length();",null,0,null);
    // sc.addCodeLine("4 else breiteTranspositionsArray = (zwischentext.length / codewort.length()) +1;",null,0,null);
    // sc.addCodeLine("5 ",null,0,null);
    // sc.addCodeLine("6 char[][] transposition = new char[breiteTranspositionsArray][codewort.length()]",
    // null, 0, null);
    // sc.addCodeLine("7 ",null,0,null);
    sc.addCodeLine("for (int i = 0; i < transposition.length; i++) {", null, 0,
        null);
    sc.addCodeLine("for (int j = 0; j < transposition[0].length; j++) {", null,
        1, null);
    sc.addCodeLine("if (i*codewort.length()+j >= zwischentext.length)", null,
        2, null);
    sc.addCodeLine("return;", null, 3, null);
    sc.addCodeLine(
        "transposition[i][j] = zwischentext[i*codewort.length()+j];", null, 2,
        null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    return sc;
  }

  private Text     codeWort;
  private Polyline vertLinieUnterCodeWortMatrix;

  /**
   * Das substituierte Zwischenergebnis wird hier in eine 2-d-matrix eingetragen
   * (Zeilenweise) PROBLEM: WENN 2. CODEWORT L&Auml;NGER ALS ZU
   * VERSCHL&Uuml;SSELNDER TEXT
   * 
   * @return the transposed char matrix
   */
  public char[][] transposeFirst() {

    subUeberschift.setText("- Schritt 3: Transpositionsmatrix füllen -", null,
        null);
    SourceCode sc = generateSourcecodeTransposeFirst();

    codeWort = algoAnimlang.newText(new Coordinates(20, 130), "Codewort: "
        + pw2, "passwort2", null);
    String[][] codewordArray = new String[1][pw2.length()];

    for (int i = 0; i < pw2.length(); i++) {
      codewordArray[0][i] = "" + pw2.charAt(i);
    }

    algoAnimCodeWordMatrix = algoAnimlang.newStringMatrix(new Coordinates(30,
        190), codewordArray, "algoAnimCodeWordMatrix", null);
    // StringMatrix transpose = algoAnimlang.newStringMatrix(new Coor, data,
    // name, display)

    Coordinates[] cc = new Coordinates[2];
    cc[0] = new Coordinates(25, 210);
    cc[1] = new Coordinates(25 + pw2.length() * 30, 210);
    vertLinieUnterCodeWortMatrix = algoAnimlang.newPolyline(cc, "lkjlkjlkjlk",
        null);// (cc, "linie12312451", null);

    // Begin Algorithmus:

    int laengeSubstitotion = encryptedOnce.length;
    int laengeCodewort = pw2.length();

    float t = (float) laengeSubstitotion / laengeCodewort;

    int hoeheTranspositionsArray;

    if (t % 1 == 0)
      hoeheTranspositionsArray = laengeSubstitotion / laengeCodewort;
    else
      hoeheTranspositionsArray = (laengeSubstitotion / laengeCodewort) + 1;

    transposition1 = new char[hoeheTranspositionsArray][pw2.length()];

    String[][] temp = new String[hoeheTranspositionsArray][pw2.length()];

    // für animal, sieht schöner aus:
    for (int i = 0; i < hoeheTranspositionsArray; i++)
      for (int j = 0; j < pw2.length(); j++)
        temp[i][j] = "~";

    algoAnimInhaltTranspositionArray = algoAnimlang.newStringMatrix(
        new Coordinates(30, 220), temp, "algoAnimInhaltTranspositionArray",
        null);

    // algoAnimlang.nextStep();
    // algoAnimInhaltTranspositionArray.highlightElemRowRange(0, 3, 1, null,
    // null);

    algoAnimlang.nextStep("Schritt 3: Transpositionsmatrix füllen");

    for (int i = 0; i < transposition1.length; i++) {
      sc.highlight(8 - 8);
      algoAnimlang.nextStep();

      for (int j = 0; j < transposition1[0].length; j++) {
        sc.unhighlight(8 - 8);
        sc.highlight(9 - 8);
        algoAnimlang.nextStep();

        sc.unhighlight(9 - 8);
        sc.highlight(10 - 8);

        algoAnimlang.nextStep();
        if (i * pw2.length() + j >= encryptedOnce.length) {

          sc.unhighlight(10 - 8);
          sc.highlight(11 - 8);
          algoAnimlang.nextStep();
          sc.unhighlight(11 - 8);
          algoAnimlang.nextStep();
          sc.hide();
          try {
            codeWort.moveTo("NW", null, new Coordinates(20, 80), null, null);
          } catch (IllegalDirectionException e) {
            e.printStackTrace();
          }
          algoAnimLabelZwischentext.hide();
          algoAnimEncryptedOnceArray.hide();
          algoAnimlang.nextStep();
          return transposition1; // sind wir schon außerhalb des substituierten
                                 // Arrays (dem zwischenergebnis aus
                                 // substituate()?
        }

        sc.unhighlight(10 - 8);
        sc.highlight(12 - 8);
        // algoAnimlang.nextStep();

        transposition1[i][j] = (encryptedOnce[i * pw2.length() + j]).charAt(0);
        algoAnimEncryptedOnceArray.highlightCell(i * pw2.length() + j, null,
            null);
        algoAnimInhaltTranspositionArray.highlightCell(i, j, null, null);
        algoAnimInhaltTranspositionArray.put(i, j,
            "" + (encryptedOnce[i * pw2.length() + j]).charAt(0), null, null);
        algoAnimEncryptedOnceArray.highlightElem(i * pw2.length() + j, null,
            null);

        algoAnimlang.nextStep();
        algoAnimInhaltTranspositionArray.unhighlightCell(i, j, null, null);
        algoAnimEncryptedOnceArray.unhighlightCell(i * pw2.length() + j, null,
            null);

        sc.unhighlight(12 - 8);
      }

    }

    sc.unhighlight(11 - 8);
    algoAnimlang.nextStep();
    sc.hide();
    try {
      codeWort.moveTo("NW", null, new Coordinates(20, 80), null, null);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
    algoAnimLabelZwischentext.hide();
    algoAnimEncryptedOnceArray.hide();
    algoAnimlang.nextStep();

    return transposition1;
  }

  private SourceCode getSourceCodeTranspose2nd() {

    SourceCode sc = algoAnimlang.newSourceCode(new Coordinates(10, 520),
        "sourceCode", null, scProps);

    sc.addCodeLine("for (char m = 'A'; m <= 'Z'; m++)", null, 0, null);
    sc.addCodeLine("for (int i = 0; i < codewort.length();i++)", null, 1, null);
    sc.addCodeLine("if (codewort.toUpperCase().charAt(i) == m) {", null, 2,
        null);
    sc.addCodeLine("for (int l = 0; l < hoeheTranspositionsArray; l++) ", null,
        3, null);
    sc.addCodeLine(
        "if (transposition[l][i] != '~') chiffrat += transposition[l][i];",
        null, 4, null);

    /*
     * for (char m = 'A'; m < 'z'; m++) { // ... gehe das Codewort durch for
     * (int i = 0; i < pw2.length();i++) { //Wenn der Buchstabe m gleich dem
     * angeguckten des Codewortes ... if (pw2.charAt(i) == m) { // ... Kopiere
     * die ganze Spalte an die Position und ... for (int l = 0; l <
     * hoeheTranspositionsArray; l++) { result[l][pos] = transposition1[l][i];
     * //wohl nicht mehr nötig, da so finish() entfällt if (transposition1[l][i]
     * != '\0') theResult += transposition1[l][i]; } // ...rechne die Position
     * eins hoch ("für den nächsten Buchstaben") pos++; } } }
     */
    return sc;
  }

  /**
   * Das zeilenweise transponierte Zwischenergebnis wird nun umsortiert (nach
   * dem Alphabet) PROBLEM: WENN 2. CODEWORT L&Auml;NGER ALS ZU
   * VERSCHL&Uuml;SSELNDER TEXT
   * 
   * @param temp
   *          Sortierte Codewort
   * @return
   */
  private char[][] sortAndtransposeSecond() {
    int laengeSubstitotion = encryptedOnce.length;
    int laengeCodewort = pw2.length();

    float t = (float) laengeSubstitotion / laengeCodewort;

    int hoeheTranspositionsArray;

    if (t % 1 == 0)
      hoeheTranspositionsArray = laengeSubstitotion / laengeCodewort;
    else
      hoeheTranspositionsArray = (laengeSubstitotion / laengeCodewort) + 1;

    result = new char[hoeheTranspositionsArray][pw2.length()];

    subUeberschift.setText("- Schritt 4: Transpositionsmatrix auslesen -",
        null, null);

    SourceCode sc = getSourceCodeTranspose2nd();
    String theResult = "";
    Text algoAnimChiffrat = algoAnimlang.newText(new Coordinates(20, 400),
        "Chiffrat:", "adlvkyycyxde", null);
    Text algoAnimResult = algoAnimlang.newText(new Offset(10, 0,
        algoAnimChiffrat, "NE"), theResult, "lajdnvnas", null);

    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));

    // now, create the IntArray object, linked to the properties

    String[] komplettesAlphabet = { "A", "B", "C", "D", "E", "F", "G", "H",
        "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z" };
    // StringArray algoAnimKomplettesAlphabet = algoAnimlang.newStringArray(new
    // Coordinates(20,480), komplettesAlphabet, "adfycva22ydfskdlj", null);

    StringArray alphabetArray = algoAnimlang.newStringArray(new Coordinates(20,
        480), komplettesAlphabet, "alphabet", null, arrayProps);

    ArrayMarker algoAnimArrayM = algoAnimlang.newArrayMarker(alphabetArray, 0,
        "am1", null, getArrayMarkerProbs("m"));

    algoAnimlang.nextStep("Schritt 4: Transpositionsmatrix auslesen");
    int pos = 0;
    sc.highlight(0);
    algoAnimlang.nextStep();

    // String pwSort[][] = new String[pw2.length()][2];
    sc.unhighlight(0);

    int totalKopierteSpalten = 0;

    // algoAnimArrayMarkerAlphabet.

    // Für jeden Buchstaben m....
    for (char m = 'A'; m <= 'Z'; m++) {
      sc.highlight(0);
      algoAnimlang.nextStep();

      // ... gehe das Codewort durch
      for (int i = 0; i < pw2.length(); i++) {

        algoAnimCodeWordMatrix.highlightCell(0, i, null, null);
        alphabetArray.highlightCell(m, null, null);

        sc.unhighlight(0);
        sc.highlight(1);
        algoAnimlang.nextStep();

        sc.unhighlight(1);
        sc.highlight(2);

        algoAnimlang.nextStep();
        sc.unhighlight(2);
        // Wenn der Buchstabe m gleich dem angeguckten des Codewortes ...
        if (pw2.toUpperCase().charAt(i) == m) {

          if (totalKopierteSpalten == 1) {
            MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
                "multipleChoiceQuestion");
            mcq.setPrompt("Wie viele Buchstaben wird das Chiffrat enthalten?");
            String zweiteFrage = "" + (message.length() * 2) + " Buchstaben.";
            mcq.addAnswer(zweiteFrage, 5,
                "Genau: Doppeltsoviele Buchstaben wir der Klartext");
            mcq.addAnswer(
                "Jedes Chiffrat, das aus diesem Algorithmus resultiert, hat immer 50 Buchstaben.",
                0,
                "Nein: Doppeltsoviele Buchstaben wir der Klartext, schließlich wird jeder Buchstabe durch ein Buchstabenpaar (Bigramm) ersetzt (substituiert) und anschließend 'verwürfelt' (transponiert).");
            mcq.addAnswer(
                "Das ist zu diesem Zeitpunkt noch ungewiss.",
                0,
                "Nein: Doppeltsoviele Buchstaben wir der Klartext, schließlich wird jeder Buchstabe durch ein Buchstabenpaar (Bigramm) ersetzt (substituiert) und anschließend 'verwürfelt' (transponiert).");
            mcq.setGroupID("First question group");
            algoAnimlang.addMCQuestion(mcq);
          }

          totalKopierteSpalten++;
          // ... Kopiere die ganze Spalte an die Position und ...
          for (int l = 0; l < hoeheTranspositionsArray; l++) {

            sc.highlight(3);

            result[l][pos] = transposition1[l][i]; // wohl nicht mehr nötig, da
                                                   // so finish() entfällt

            algoAnimlang.nextStep();

            if (transposition1[l][i] != '\0') {
              sc.unhighlight(3);
              sc.highlight(4);
              theResult += transposition1[l][i];
              algoAnimInhaltTranspositionArray.highlightCell(l, i, null, null);
              algoAnimResult.setText(theResult, null, null);
            } else
              sc.unhighlight(3);

            algoAnimlang.nextStep();
            sc.unhighlight(4);
            algoAnimInhaltTranspositionArray.unhighlightCell(l, i, null, null);

          }

          // ...rechne die Position eins hoch ("für den nächsten Buchstaben")
          pos++;
        }
        sc.unhighlight(1);

        algoAnimCodeWordMatrix.unhighlightCell(0, i, null, null);
        alphabetArray.unhighlightCell(m, null, null);
      }
      sc.unhighlight(0);
      algoAnimArrayM.increment(null, null);
    }

    algoAnimlang.nextStep();
    algoAnimCodeWordMatrix.hide();
    alphabetArray.hide();
    sc.hide();
    algoAnimInhaltTranspositionArray.hide();
    algoAnimArrayM.hide();
    vertLinieUnterCodeWortMatrix.hide();
    algoAnimLinieUeberSC.hide();

    try {
      algoAnimChiffrat.moveTo("NW", null, new Coordinates(20, 125), null,
          new MsTiming(250));
      algoAnimResult.moveTo("NW", null, new Coordinates(70, 125), null,
          new MsTiming(250));
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    algoAnimlang.nextStep();

    // Alles mögliche verstecken, um Abschlusstext anzuzeigen:
    subUeberschift.hide();

    algoAnimChiffrat.hide();
    algoAnimResult.hide();

    codeWort.hide();

    algoAnimLableNachricht.hide();
    messageArray.hide();

    setTextForEndpage();
    algoAnimlang.nextStep("Fazit");
    // System.out.println();
    // System.out.println("Länge: "+theResult.length()+" Verschlüsselter Text: "+theResult);

    return result;

  }

  private void setTextForEndpage() {

    TextProperties ueberschiftProperties = new TextProperties();

    ueberschiftProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 22));

    TextProperties einleitungsText = new TextProperties();

    einleitungsText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));

    String t, s, d, g, l, j;

    t = "Da der Empfänger das Substitutionsalphabet sowie das Codewort kennt, kann er zum Entschlässeln die eben beschriebenen Schritte ";
    s = "in umgekehrter Reihnfolge abarbeiten und somit den Geheimtext entschlüsseln.";
    d = "Die ADFGX-Verschlüsselung wurde im April 1918 duch den französischen Artillerie-Offizier Capitaine Georges Painvin gebrochen.";
    g = "Da die mithilfe des Polybios-Quadrats durchgeführte monoalphabetische Substitution praktisch keinen Schutz bietet (siehe bspw. 'Häufigkeits-";
    l = "analyse'), beruht die Sicherheit des Verfahrens auf der Transposition, ist also im Grunde einstufig. Wer die Spaltentransposition knackt, ";
    j = "kann den Klartext leicht rekonstruieren.";

    algoAnimlang.newText(new Coordinates(40, 65), t, "einleitungstext", null,
        einleitungsText);
    algoAnimlang.newText(new Coordinates(40, 90), s, "einleitungstext2", null,
        einleitungsText);
    algoAnimlang.newText(new Coordinates(40, 115), d, "einleitungstext3", null,
        einleitungsText);
    algoAnimlang.newText(new Coordinates(40, 140), g, "einleitungstext4", null,
        einleitungsText);
    algoAnimlang.newText(new Coordinates(40, 165), l, "einleitungstext5", null,
        einleitungsText);
    algoAnimlang.newText(new Coordinates(40, 190), j, "einleitungstext6", null,
        einleitungsText);

  }

  /**
   * Ein kompletter Lauf der ADFGX-Verschl&auml;sselung. Folgende Schritte
   * werden durchgef&uuml;hrt: - Polybios Matrix erstellen - Substituion -
   * Transposition - Anzeige des Chiffrates auf der Konsole
   */
  public void run(Language l, String[] pw1, String pw2, String message) {

    // Umlaute umwandeln
    String pw22 = pw2;
    pw22 = pw22.replaceAll("Ä", "A");
    pw22 = pw22.replace("Ö", "O");
    pw22 = pw22.replace("Ü", "U");
    pw22 = pw22.replace("ß", "S");

    this.alphabet = pw1; // secret mixed alphabet
    this.pw2 = pw22; // 2nd Codeword to transpose
    this.message = message; // the message to encode

    l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    this.algoAnimlang = l;

    this.algoAnimlang
        .setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    algoAnimlang.setStepMode(true);

    this.adfgx = new String[5];
    adfgx[0] = "A";
    adfgx[1] = "D";
    adfgx[2] = "F";
    adfgx[3] = "G";
    adfgx[4] = "X";

    initDefaultSourceCodeProperties();
    setTextForTitlepage();

    calculatePolybos();
    substituate();
    transposeFirst();
    sortAndtransposeSecond();
    // finish();

  }

}
