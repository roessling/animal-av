package generators.maths;

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
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class SchriftlicheMultiplikation implements Generator {
  private Language             lang;
  private SourceCodeProperties sourceCode;
  private int[]                ersteZahl;
  private int[]                zweiteZahl;
  private MatrixProperties     MatrixProps;

  // private Rect hRect;
  // private Text header;
  private TextProperties       textProps;
  private Text                 t1;
  private Text                 t2;
  private Text                 t3;
  private Text                 t4;
  private Text                 t5;
  private Text                 t6;
  private Text                 t7;
  private Text                 t8;
  private Text                 t9;

  // private Text t10;
  // private Text t11;
  // private Text t12;

  public Language getLang() {
    return lang;
  }

  public void setLang(Language lang) {
    this.lang = lang;
  }

  int[]      n;
  int[]      m;
  String[][] ergebnismatrix;

  public int[] getN() {
    return n;
  }

  public void setN(int[] n) {
    this.n = n;
  }

  public int[] getM() {
    return m;
  }

  public void setM(int[] m) {
    this.m = m;
  }

  public void showSourceCode() {
    // first, set the visual properties for the source code
    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 12));
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Offset(100, 0, "ergebnisMatrix",
        AnimalScript.DIRECTION_NE), "sc", null, sourceCode);
    // add a code line
    // parameters: code itself; name (can be null); indentation level;
    // display options
    sc.addCodeLine("multiplikation (int[] n, int[] m){", null, 0, null);
    sc.addCodeLine("int [][] matrix mit Größe (|m|+1) x (|n|+|m|+1);", null, 1,
        null);
    sc.addCodeLine("for i=0,...|m|-1{", null, 1, null);
    sc.addCodeLine("int übertrag1 = 0;", null, 1, null);
    sc.addCodeLine("matrix[i][|n|+2+i)] bis matrix[i][|n|+|m|] = 0;", null, 2,
        null);
    sc.addCodeLine("for j=0,...|n|-1 {", null, 2, null);
    sc.addCodeLine("eintrag=(m[i]*n[|n|-j-1]+übertrag1) mod 10;", null, 3, null);
    sc.addCodeLine("übertrag1=abrunden((m[i]*n[|n|-j-1]+übertrag1):10);", null,
        3, null);
    sc.addCodeLine("matrix[i][|n|+1+i-j] = eintrag;", null, 3, null);
    sc.addCodeLine("j++", null, 2, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("if übertrag1!=0{", null, 2, null);
    sc.addCodeLine("matrix[i][i+1] = übertrag1;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("i++", null, 1, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("int übertrag2=0;", null, 1, null);
    sc.addCodeLine("for j=|m|+|n|,...0{", null, 1, null);
    sc.addCodeLine("int spaltensumme=0;", null, 1, null);
    sc.addCodeLine("for i=0,...|m|-1 {", null, 2, null);
    sc.addCodeLine("spaltensumme= spaltensumme+matrix[i][j]", null, 3, null);
    sc.addCodeLine("i++", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("ergebnis= (spaltensumme + übertrag2) mod 10;", null, 2,
        null);
    sc.addCodeLine("übertrag2= abrunden((spaltensumme + übertrag2):10);", null,
        2, null);
    sc.addCodeLine("matrix[|m|][j]=ergebnis;", null, 2, null);
    sc.addCodeLine("j--", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("return", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    setSc(sc);
  }

  private SourceCode sc; // to ensure it is visible outside the method...

  public SourceCode getSc() {
    return sc;
  }

  public void setSc(SourceCode sc) {
    this.sc = sc;
  }

  public void init() {
    lang = new AnimalScript("Schriftliche Multiplikation",
        "Fatima Isufaj, Jasmin Diehl", 800, 600);
    lang.setStepMode(true);
  }

  public int[][] multiplizieren() {

    // überschrift in rechteck wird erzeugt:
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "schriftliche Multiplikation",
        "header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

    // der Text auf der ersten folie wir nach und nach aufgebaut
    lang.nextStep();
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    t1 = lang.newText(new Coordinates(10, 100), "Der Algorithmus in Worten:",
        "description1", null, textProps);
    lang.nextStep();

    t2 = lang.newText(new Offset(0, 50, "description1",
        AnimalScript.DIRECTION_NW),
        "1. Starte mit der ersten Ziffer der zweiten Zahl", "description2",
        null, textProps);

    lang.nextStep();
    t3 = lang.newText(new Offset(0, 25, "description2",
        AnimalScript.DIRECTION_NW),
        "2. Multipliziere sie zuerst mit der Einer-Ziffer der ersten Zahl",
        "description3", null, textProps);
    t4 = lang.newText(new Offset(20, 25, "description3",
        AnimalScript.DIRECTION_NW),
        "und mache dies aufsteigend bis zur ersten Ziffer der ersten Zahl",
        "description4", null, textProps);
    lang.nextStep();

    t5 = lang.newText(new Offset(-20, 25, "description4",
        AnimalScript.DIRECTION_NW), "3. Schreibe das Ergebnis direkt darunter",
        "description5", null, textProps);
    lang.nextStep();
    t6 = lang
        .newText(
            new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
            "4. Starte die schriftliche Multiplikation nun mit der zweiten Ziffer der zweiten Zahl",
            "description6", null, textProps);
    lang.nextStep();
    t7 = lang
        .newText(
            new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
            "5. Fuehre die schriftliche Multiplikation so lange nacheinander aus, ",
            "description7", null, textProps);

    t8 = lang.newText(new Offset(20, 25, "description7",
        AnimalScript.DIRECTION_NW),
        "bis die Einer-Ziffer der zweiten Zahl erreicht wurde", "description8",
        null, textProps);
    lang.nextStep();
    t9 = lang.newText(new Offset(-20, 25, "description8",
        AnimalScript.DIRECTION_NW), "6. Addiere die erhaltenen Werte",
        "description9", null, textProps);

    lang.nextStep();

    // der text auf der ersten folie verschwindet, nur die Ueberschrift bleibt
    t1.hide();
    t2.hide();
    t3.hide();
    t4.hide();
    t5.hide();
    t6.hide();
    t7.hide();
    t8.hide();
    t9.hide();

    // die matrix wird angelegt (zunaechst leer)

    int[][] multmatrix = new int[m.length + 4][n.length + m.length + 1];

    String[][] ergebnismatrix = new String[m.length + 4][n.length + m.length
        + 1];
    String[][] zwischenschritte = new String[][] { { "Uebertrag1:" },
        { "Eintrag:" }, { " " } };

    // die matrix wird mit leerzeichen gefuellt
    for (int k = 0; k < m.length + 4; k++) {
      for (int l = 0; l < n.length + m.length + 1; l++) {
        ergebnismatrix[k][l] = " ";
      }

    }

    StringMatrix ergebnisMatrix = lang.newStringMatrix(
        new Coordinates(50, 100), ergebnismatrix, "ergebnisMatrix", null,
        MatrixProps);

    // Timing defaultTiming = new TicksTiming(15);

    // in die matrix kommt die aufgabe und die erste zeile -----

    for (int i = 0; i < ergebnismatrix.length; i++) {
      if (i == 0) {
        for (int k = 0; k <= n.length - 1; k++) {
          ergebnismatrix[i][k] = String.valueOf(n[k]);
          ergebnisMatrix.put(i, k, ergebnismatrix[i][k], null, null);

        }
        ergebnismatrix[i][n.length] = "*";
        ergebnisMatrix
            .put(i, n.length, ergebnismatrix[i][n.length], null, null);
        for (int k = 0; k <= m.length - 1; k++) {
          ergebnismatrix[i][k + n.length + 1] = String.valueOf(m[k]);
          ergebnisMatrix.put(i, k + n.length + 1, ergebnismatrix[i][k
              + n.length + 1], null, null);
        }

      } else if (i == 1) {
        for (int k = 0; k <= n.length + m.length; k++) {
          ergebnismatrix[i][k] = "---";
          ergebnisMatrix.put(i, k, ergebnismatrix[i][k], null, null);
        }
      }

    }

    lang.nextStep("Aufgabe");
    showSourceCode(); // der code wird sichtbar

    StringMatrix zwischenSchritte = lang.newStringMatrix(new Offset(0, 100,
        "ergebnisMatrix", AnimalScript.DIRECTION_SW), zwischenschritte,
        "zwischenSchritte", null, MatrixProps);
    lang.nextStep("Code");
    sc.highlight(0);
    lang.nextStep();
    sc.toggleHighlight(0, 1);
    lang.nextStep();

    // hier wird zeilenweise die multiplikation ausgefuehrt
    for (int i = 0; i <= (m.length - 1); i++) {

      sc.toggleHighlight(1, 2);
      // ergebnismatrix zweite zahl wird gehighlighted
      ergebnisMatrix.highlightCell(0, i + n.length + 1, null, null);
      lang.nextStep(i + 1 + ". Zeile");
      int carry1 = 0;
      zwischenSchritte.put(0, 0, "Uebertrag1: 0", null, null);
      sc.toggleHighlight(2, 3);
      lang.nextStep();
      sc.toggleHighlight(3, 4);
      // 0-er setzen
      for (int k = n.length + 2 + i; k <= n.length + m.length; k++) {
        multmatrix[i + 2][k] = 0;

        ergebnismatrix[i + 2][k] = String.valueOf(multmatrix[i + 2][k]);
        ergebnisMatrix.put(i + 2, k, ergebnismatrix[i + 2][k], null, null);
        lang.nextStep();
      }
      // for (int k=n.length+2+i; k<= n.length + m.length; k++){
      // matrix[i+3][k]=0;
      // }

      for (int j = 0; j <= (n.length - 1); j++) {
        int eintrag = (m[i] * n[n.length - j - 1] + carry1) % 10;
        carry1 = (m[i] * n[n.length - j - 1] + carry1) / 10;
        multmatrix[i + 2][n.length + 1 + i - j] = eintrag;
        ergebnismatrix[i + 2][n.length + 1 + i - j] = String
            .valueOf(multmatrix[i + 2][n.length + 1 + i - j]);

        sc.toggleHighlight(4, 5);
        // ergebnismatrix erste zahl wird gehighlighted
        ergebnisMatrix.highlightCell(0, n.length - j - 1, null, null);
        lang.nextStep();
        sc.toggleHighlight(5, 6);
        // eintrag in matrix zwischenschritte setzen
        zwischenSchritte.put(1, 0, "Eintrag: " + String.valueOf(eintrag), null,
            null);

        lang.nextStep();
        sc.toggleHighlight(6, 7);
        // Uebertrag in matrix zwischenschritte setzen
        zwischenSchritte.put(0, 0, "Uebertrag1: " + String.valueOf(carry1),
            null, null);
        lang.nextStep();
        sc.toggleHighlight(7, 8);
        // eintrag in matrix setzen und eintrag in matrix highlighten
        ergebnisMatrix.highlightCell(i + 2, n.length + 1 + i - j, null, null);
        ergebnisMatrix.put(i + 2, n.length + 1 + i - j,
            ergebnismatrix[i + 2][n.length + 1 + i - j], null, null);
        zwischenSchritte.put(1, 0, "Eintrag: ", null, null);
        lang.nextStep();
        ergebnisMatrix.unhighlightCell(i + 2, n.length + 1 + i - j, null, null);
        sc.toggleHighlight(8, 9);
        lang.nextStep();
        ergebnisMatrix.unhighlightCell(0, n.length - j - 1, null, null);
        sc.unhighlight(9);
        lang.nextStep();

      }

      sc.highlight(11);
      lang.nextStep();
      sc.toggleHighlight(11, 12);
      // Uebertrag in matrix setzen
      if (carry1 != 0) {
        multmatrix[i + 2][i + 1] = carry1;
        ergebnismatrix[i + 2][i + 1] = String.valueOf(multmatrix[i + 2][i + 1]);
        ergebnisMatrix.put(i + 2, i + 1, ergebnismatrix[i + 2][i + 1], null,
            null);
        zwischenSchritte.put(0, 0, "Uebertrag1: ", null, null);
        lang.nextStep();
      }
      sc.toggleHighlight(12, 13);
      lang.nextStep();
      sc.toggleHighlight(13, 14);
      lang.nextStep();
      sc.toggleHighlight(14, 15);

      sc.unhighlight(15);
      ergebnisMatrix.unhighlightCell(0, i + n.length + 1, null, null);
      lang.nextStep();

    }

    lang.nextStep();

    // die zeile vor dem ergebnis wird mit ---- gefuellt.

    for (int k = 0; k <= n.length + m.length; k++) {
      ergebnismatrix[m.length + 2][k] = "---";
      ergebnismatrix[m.length + 1][0] = "+";
      ergebnisMatrix.put(m.length + 1, 0, ergebnismatrix[m.length + 1][0],
          null, null);
      ergebnisMatrix.put(m.length + 2, k, ergebnismatrix[m.length + 2][k],
          null, null);
      zwischenSchritte.put(0, 0, " ", null, null);
      zwischenSchritte.put(1, 0, " ", null, null);
    }

    lang.nextStep();

    zwischenSchritte.put(0, 0, "Uebertrag2: ", null, null);
    zwischenSchritte.put(1, 0, "Ergebnis: ", null, null);
    zwischenSchritte.put(2, 0, "Spaltensumme: ", null, null);
    // Uebertrag 2 = 0 anlegen
    lang.nextStep();

    sc.highlight(16);
    zwischenSchritte.put(0, 0, "Uebertrag2: 0", null, null);
    lang.nextStep("Addition");

    // hier wird addiert
    int carry2 = 0;
    for (int a = m.length + n.length; a >= 0; a--) {
      sc.toggleHighlight(16, 17);
      lang.nextStep();
      sc.toggleHighlight(17, 18);
      zwischenSchritte.put(2, 0, "Spaltensumme: 0", null, null);
      lang.nextStep();

      int spaltensumme = 0;
      // boolean leer = true;

      for (int b = 0; b <= m.length - 1; b++) {
        sc.toggleHighlight(18, 19);
        lang.nextStep();
        // if matrix != empty
        sc.toggleHighlight(19, 20);

        spaltensumme = spaltensumme + multmatrix[b + 2][a];
        zwischenSchritte.put(2, 0,
            "Spaltensumme: " + String.valueOf(spaltensumme), null, null);
        ergebnisMatrix.highlightCell(b + 2, a, null, null);
        lang.nextStep();
        ergebnisMatrix.unhighlightCell(b + 2, a, null, null);

        sc.toggleHighlight(20, 21); // spaltensumme in matrix eintragen
        lang.nextStep();
        sc.toggleHighlight(21, 22);
        lang.nextStep();
        sc.unhighlight(22);
      }

      spaltensumme = spaltensumme + carry2;
      zwischenSchritte.put(2, 0,
          "Spaltensumme: " + String.valueOf(spaltensumme), null, null);
      zwischenSchritte.put(0, 0, "Uebertrag2: ", null, null);

      sc.highlight(23); // ergebnis in matrix(rechts) eintragen
      int ergebnis = (spaltensumme) % 10;
      carry2 = (spaltensumme) / 10;
      multmatrix[m.length + 3][a] = ergebnis;
      ergebnismatrix[m.length + 3][a] = String
          .valueOf(multmatrix[m.length + 3][a]);
      zwischenSchritte.put(1, 0, "Ergebnis: " + String.valueOf(ergebnis), null,
          null);
      lang.nextStep();
      sc.toggleHighlight(23, 24); // übertrag2 in matrix(rechts) eintragen
      zwischenSchritte.put(0, 0, "Uebertrag2: " + String.valueOf(carry2),
          null, null);
      lang.nextStep();
      sc.toggleHighlight(24, 25); // in ergebnismatrix das ergebnis eintragen
      multmatrix[m.length + 3][a] = ergebnis;
      ergebnismatrix[m.length + 3][a] = String
          .valueOf(multmatrix[m.length + 3][a]);
      ergebnisMatrix.put(m.length + 3, a, ergebnismatrix[m.length + 3][a],
          null, null);
      zwischenSchritte.put(1, 0, "Ergebnis: " + String.valueOf(ergebnis), null,
          null);
      zwischenSchritte.put(1, 0, "Ergebnis: ", null, null);
      lang.nextStep();
      sc.toggleHighlight(25, 26);
      ergebnisMatrix.put(m.length + 3, a, ergebnismatrix[m.length + 3][a],
          null, null);

      zwischenSchritte.put(2, 0, "Spaltensumme: ", null, null);
      lang.nextStep();
      sc.unhighlight(26);

    }
    sc.highlight(28);
    lang.nextStep();
    sc.unhighlight(28);

    // der Text auf der abschlussfolie wird erzeugt
    lang.nextStep("Ergebnis");
    sc.hide();
    ergebnisMatrix.hide();
    zwischenSchritte.hide();

    lang.newText(new Coordinates(10, 100),
        "Anmerkungen zur schriftlichen Multiplikation:", "text", null,
        textProps);
    lang.nextStep();
    lang.newText(
        new Offset(0, 50, "text", AnimalScript.DIRECTION_NW),
        "Durch dieses Verfahren wird die Multiplikation von Zahlen der Laenge n und m",
        "text2", null, textProps);
    lang.newText(new Offset(0, 25, "text2", AnimalScript.DIRECTION_NW),
        "in insgesamt n*m einzelene Multiplikationen aufgespalten.", "text3",
        null, textProps);
    lang.newText(new Offset(0, 25, "text3", AnimalScript.DIRECTION_NW),
        "Insgesamt gibt es im schlimmsten Fall (n+m)*(m-1)+n*m Additionen,",
        "text4", null, textProps);
    lang.newText(new Offset(0, 25, "text4", AnimalScript.DIRECTION_NW),
        "wobei (n+m)*(m-1) Additionen fuer die Summation der", "text5", null,
        textProps);
    lang.newText(new Offset(0, 25, "text5", AnimalScript.DIRECTION_NW),
        "einzelnen Ziffern (spaltenweise) zaehlen,", "text6", null, textProps);
    lang.newText(new Offset(0, 25, "text6", AnimalScript.DIRECTION_NW),
        "und n*m Additionen fuer den Fall, dass bei jeder Multiplikation",
        "text7", null, textProps);
    lang.newText(new Offset(0, 25, "text7", AnimalScript.DIRECTION_NW),
        "ein Uebertrag entsteht und hinzuaddiert werden muss.", "text8", null,
        textProps);
    lang.nextStep();

    return multmatrix;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    ersteZahl = (int[]) primitives.get("ersteZahl");
    zweiteZahl = (int[]) primitives.get("zweiteZahl");
    MatrixProps = (MatrixProperties) props.getPropertiesByName("MatrixProps");
    setN(ersteZahl);
    setM(zweiteZahl);
    multiplizieren();

    return workAround(lang.toString());
  }

  // work around some Animal bugs
  private static String workAround(String input) {

    // input = input.replaceAll(" refresh", "");
    // input = input.replaceAll(" row 0", "");

    String[] temp = input.split("}");
    String newOut = "";

    for (int i = 0; i < temp.length - 1; i++) {
      if (temp[i].contains("6. Addiere die erhaltenen Werte")) {
        temp[i] = temp[i] + "}" + System.getProperty("line.separator")
            + "Label \"Einleitung\"";
      } else {
        temp[i] = temp[i] + "}";
      }
      newOut = newOut + temp[i];
    }
    return newOut + System.getProperty("line.separator")
        + "label \"Anmerkungen zur schriftlichen Multiplikation\"";
  }

  public String getName() {
    return "Schriftliche Multiplikation";
  }

  public String getAlgorithmName() {
    return "Schriftliche Multiplikation";
  }

  public String getAnimationAuthor() {
    return "Fatima Isufaj, Jasmin Diehl";
  }

  public String getDescription() {
    return "Der Algorithmus \"schriftliche Multiplikation\" berechnet das Produkt aus zwei natuerlichen Zahlen."
        + "\n"
        + "Dabei werden Arrays verwendet, wobei die einzelnen Arrayeintraege den  Ziffern der Zahlen entsprechen.";
  }

  public String getCodeExample() {
    return "multiplikation (int[] n, int[] m){" + "\n"
        + "	int [][] matrix mit Größe (|m|+1) x (|n|+|m|+1);" + "\n"
        + "	for i=0,...|m|-1{;" + "\n" + "	int übertrag1 = 0;" + "\n"
        + "		matrix[i][|n|+2+i)] bis matrix[i][|n|+|m|] = 0;" + "\n"
        + "		for j=0,...|n|-1 {" + "\n"
        + "			eintrag=(m[i]*n[|n|-j-1]+übertrag1) mod 10;" + "\n"
        + "			übertrag1=abrunden((m[i]*n[|n|-j-1]+übertrag1):10);" + "\n"
        + "			matrix[i][|n|+1+i-j] = eintrag;" + "\n" + "		j++" + "\n" + "		}"
        + "\n" + "		if übertrag1!=0{" + "\n" + "			matrix[i][i+1] = übertrag1;"
        + "\n" + "		}" + "\n" + "	i++" + "\n" + "		}" + "\n"
        + "	int übertrag2=0;" + "\n" + "	for j=|m|+|n|,...0{" + "\n"
        + "	int spaltensumme=0;" + "\n" + "		for i=0,...|m|-1 {" + "\n"
        + "			spaltensumme= spaltensumme+matrix[i][j]" + "\n" + "		i++" + "\n"
        + "		}" + "\n" + "		ergebnis= (spaltensumme + übertrag2) mod 10;"
        + "\n" + "		übertrag2= abrunden((spaltensumme + übertrag2):10);" + "\n"
        + "		matrix[|m|][j]=ergebnis;" + "\n" + "	j--" + "\n" + "	}" + "\n"
        + "	return" + "\n" + "}";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}