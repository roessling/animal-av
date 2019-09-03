package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
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
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class MillerRabinTest implements Generator {
  private Language             lang;
  private MatrixProperties     matrixProps;
  private TextProperties       headerProps;
  private int                  n;
  private SourceCodeProperties sourceCodeProps;
  private RectProperties       rectProps;
  private int                  k;
  private int                  randomTemp;
  private int[]                numbers;
  private String[][]           matrix;
  private boolean              bestanden;
  private List<Text>           tests;
  private Polyline             eins, zwei;
  StringMatrix                 strMatrix;
  private Text                 eingabe, anzahlTests, zweiHochS, nMinusEins;
  private TextProperties       textProp, testProp, testHeaderProp;
  private SourceCode           src;

  public void init() {
    lang = new AnimalScript("Miller-Rabin-Test",
        "Julian Wulfheide, Tim Wimmer, Denis Caruso", 1150, 950);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    validateInput(props, primitives);

    matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
    headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    n = (Integer) primitives.get("n");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");
    rectProps = (RectProperties) props.getPropertiesByName("rectProps");
    k = (Integer) primitives.get("k");

    testHeaderProp = new TextProperties();
    testHeaderProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 15));

    textProp = new TextProperties();

    lang.newText(new Coordinates(20, 30), "Miller Rabin Test", "header", null,
        headerProps);

    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

    lang.nextStep("Einführung");

    // Intro Text
    Text introText1 = lang
        .newText(
            new Offset(0, 100, "hRect", AnimalScript.DIRECTION_SW),
            "Der Miller-Rabin-Test oder Miller-Selfridge-Rabin-Test ist ein probabilistischer Primzahltest.",
            "introText1", null, textProp);

    Text introText2 = lang
        .newText(
            new Offset(0, 10, "introText1", AnimalScript.DIRECTION_SW),
            "Erhält der probabilistische Test eine natürliche Zahl n als Eingabe, so gibt er entweder",
            "introText2", null, textProp);

    Text introText3 = lang
        .newText(
            new Offset(0, 10, "introText2", AnimalScript.DIRECTION_SW),
            "''n ist keine Primzahl'' oder ''n ist wahrscheinlich eine Primzahl'' aus.",
            "introText3", null, textProp);

    Text introText4 = lang.newText(new Offset(0, 10, "introText3",
        AnimalScript.DIRECTION_SW),
        "Das Ergebnis ist hierbei von n und dem Zufall abhängig.",
        "introText4", null, textProp);

    Text introText5 = lang
        .newText(
            new Offset(0, 10, "introText4", AnimalScript.DIRECTION_SW),
            "Der Miller-Rabin-Test prüft hierbei, ob eine gegebene Zahl n zusammengesetzt ist.",
            "introText5", null, textProp);

    Text introText6 = lang
        .newText(
            new Offset(0, 10, "introText5", AnimalScript.DIRECTION_SW),
            "Zunächst wird n-1 in eine ungerade Zahl d und eine Zweierpotenz 2^s zerlegt (n-1 = 2^s * d).",
            "introText6", null, textProp);

    Text introText7 = lang
        .newText(
            new Offset(0, 10, "introText6", AnimalScript.DIRECTION_SW),
            "Daraufhin werden für zufällige Zahlen a aus dem Bereich 1 < a < n-1 die folgenden Tests durchgeführt:",
            "introText7", null, textProp);

    Text introText8 = lang.newText(new Offset(0, 20, "introText7",
        AnimalScript.DIRECTION_SW), "      Test 1:  	Ist a^d \u2263 1 mod n",
        "introText8", null, textProp);

    Text introText9 = lang
        .newText(
            new Offset(0, 15, "introText8", AnimalScript.DIRECTION_SW),
            "      Test 2:  	für alle i mit 0 \u2264 i < s: Gibt es ein i mit a^(2^i * d) \u2263 -1 mod n",
            "introText9", null, textProp);

    Text introText10 = lang
        .newText(
            new Offset(0, 20, "introText9", AnimalScript.DIRECTION_SW),
            "Der Primzahlen-Test besteht nun darin, zu der zu prüfenden Zahl n eine teilerfremde Zahl a zu finden,",
            "introText10", null, textProp);

    Text introText11 = lang
        .newText(
            new Offset(0, 10, "introText10", AnimalScript.DIRECTION_SW),
            "die keine der beiden Aussagen erfüllt. Dies würde die Nicht-Primalitüt von n definitiv bestätigen.",
            "introText11", null, textProp);

    Text introText12 = lang
        .newText(
            new Offset(0, 10, "introText11", AnimalScript.DIRECTION_SW),
            "Da mehr als 3/4 der Zahlen aus [2, n-1] dies tun, ist die Wahrscheinlichkeit,",
            "introText12", null, textProp);

    Text introText13 = lang
        .newText(
            new Offset(0, 10, "introText12", AnimalScript.DIRECTION_SW),
            "bei einem einmaligen Versuch, keinen dieser ''Zeugen'' gegen die Primaliät von n zu finden, kleiner als 1/4.",
            "introText13", null, textProp);

    lang.nextStep("Anfang");

    // Hiden alles
    introText1.hide();
    introText2.hide();
    introText3.hide();
    introText4.hide();
    introText5.hide();
    introText6.hide();
    introText7.hide();
    introText8.hide();
    introText9.hide();
    introText10.hide();
    introText11.hide();
    introText12.hide();
    introText13.hide();

    // SourceCode und PolyLines
    eins = lang.newPolyline(new Node[] { new Coordinates(0, 320),
        new Coordinates(2000, 320) }, "polyLine", null);

    src = lang.newSourceCode(new Offset(0, 40, "hRect",
        AnimalScript.DIRECTION_W), "sourceCode", null, sourceCodeProps);
    src.addCodeLine(
        "Eingabe: n > 3, eine ungerade Zahl, welche auf Primalität getestet werden soll",
        null, 1, null);
    src.addCodeLine(
        "Eingabe: k, ein Parameter, welcher die Genauigkeit des Tests bestimmt",
        null, 1, null);
    src.addCodeLine(
        "schreibe n - 1 als 2^s * d durch 2er Potenzen von n - 1, wobei d ungerade",
        null, 1, null);
    src.addCodeLine("Schleife: Wiederhole k mal:", null, 1, null);
    src.addCodeLine("wähle eine zufällige Zahl a zwischen 2 und n - 2", null,
        2, null);
    src.addCodeLine("x \u2190 a^d mod n", null, 2, null);
    src.addCodeLine(
        "falls x = 1 oder x = n - 1, springe in die nächsten Schleife", null,
        2, null);
    src.addCodeLine("für r = 1 . . s", null, 2, null);
    src.addCodeLine("x \u2190 x^2 mod n", null, 3, null);
    src.addCodeLine("falls x = 1, gebe ''zusammengesetzt'' zurück", null, 3,
        null);
    src.addCodeLine("falls x = n - 1, springe in die nächsten Schleife", null,
        3, null);
    src.addCodeLine("gebe ''zusammengesetzt'' zurück", null, 2, null);
    src.addCodeLine("gebe ''n ist wahrscheinlich eine Primzahl'' zurück", null,
        1, null);

    lang.nextStep();

    // zum eigentlichen Algrithmus
    millerRabinTest();
    finish();

    return lang.toString();
  }

  // berechnet x^c mod m
  private long modpow(long x, long c, long m) {
    long result = 1;
    long aktpot = x;
    long c2 = c;
    while (c2 > 0) {
      if (c2 % 2 == 1) {
        result = (result * aktpot) % m;
      }
      aktpot = (aktpot * aktpot) % m;
      c2 /= 2;
    }
    return result;
  }

  // Algorithmus
  public void millerRabinTest() {
    src.highlight(0, 0, false);
    eingabe = lang.newText(new Offset(15, 25, "polyLine",
        AnimalScript.DIRECTION_W), "Zu testende Zahl (n):                 "
        + this.n, "eingabe", null);

    lang.nextStep();

    src.toggleHighlight(0, 0, false, 1, 0);
    anzahlTests = lang.newText(new Offset(0, 10, "eingabe",
        AnimalScript.DIRECTION_W), "Anzahl maximaler Tests (k):    " + this.k,
        "anzahlTests", null);

    lang.nextStep();

    // schreibe n - 1 als 2^s * d durch 2er Potenzen von n - 1, wobei d
    // ungerade
    int s = 0;
    int d = n - 1;
    while (d % 2 == 0) {
      s++;
      d /= 2;
    }

    src.toggleHighlight(1, 0, false, 2, 0);
    zweiHochS = lang.newText(new Offset(0, 25, "anzahlTests",
        AnimalScript.DIRECTION_W),
        "2^s | " + this.n + " - 1  \u21D2  s = " + s, "zweiHochS", null);

    lang.nextStep();

    nMinusEins = lang.newText(new Offset(0, 10, "zweiHochS",
        AnimalScript.DIRECTION_W), "d = (n - 1) / (2^s) = " + ((this.n) - 1)
        + " / " + (int) Math.pow(2, s) + " = " + d, "nMinusEins", null);

    lang.nextStep();

    src.toggleHighlight(2, 0, false, 3, 0);

    testProp = new TextProperties();
    tests = new ArrayList<Text>();

    zwei = lang.newPolyline(new Node[] { new Coordinates(285, 320),
        new Coordinates(285, 2500) }, "polyLine2", null);

    lang.nextStep();

    // Zähler für Offset
    int y = 1;
    src.unhighlight(3, 0, false);

    matrix = new String[getMatrixLength() + 1][7];
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        matrix[i][j] = "";
      }
    }
    matrix[0][0] = "Durchlauf              ";
    matrix[0][1] = "  a            ";
    matrix[0][2] = "  x            ";
    matrix[0][3] = "x = 1 oder x = n-1              ";
    matrix[0][4] = "r              ";
    matrix[0][5] = "x == 1              ";
    matrix[0][6] = "x == n-1              ";

    strMatrix = lang.newStringMatrix(new Offset(325, 20, "polyLine",
        AnimalScript.DIRECTION_W), matrix, "matrix", null, matrixProps);

    int rowcount = 0;

    outer:
    // Schleife: Wiederhole k mal:
    for (int i = 0; i < k; i++) {
      y = 1;
      rowcount++;

      for (int j = 0; j < tests.size(); j++) {
        tests.get(j).hide();
      }

      tests.clear();

      lang.nextStep("Durchlauf " + (i + 1));

      tests.add(lang.newText(new Offset(0, 35, "nMinusEins",
          AnimalScript.DIRECTION_W), "Durchlauf " + (i + 1) + ":", "tests" + y,
          null, testHeaderProp));
      y++;

      strMatrix.put(rowcount, 0, String.valueOf("      " + (i + 1)),
          new TicksTiming(0), new TicksTiming(40));
      strMatrix.highlightCell(rowcount, 0, new TicksTiming(0), new TicksTiming(
          40));

      lang.nextStep();

      // wähle eine zufällige Zahl a zwischen 2 und n - 2
      int a = numbers[i];

      src.highlight(4, 0, false);
      tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
          AnimalScript.DIRECTION_W), "Zufällig gewähltes a = " + a,
          "tests" + y, null, testProp));
      y++;

      strMatrix.unhighlightCell(rowcount, 0, new TicksTiming(0),
          new TicksTiming(40));
      strMatrix.put(rowcount, 1, String.valueOf(a), new TicksTiming(0),
          new TicksTiming(40));
      strMatrix.highlightCell(rowcount, 1, new TicksTiming(0), new TicksTiming(
          40));

      randomTemp = a;

      lang.nextStep();

      // x <- a^d mod n
      long x = modpow(a, d, n);

      src.toggleHighlight(4, 0, false, 5, 0);
      tests.add(lang.newText(new Offset(0, 10, "tests" + (y - 1),
          AnimalScript.DIRECTION_W), "x = " + a + "^" + d + " mod " + n + " = "
          + x, "tests" + y, null, testProp));
      y++;

      strMatrix.unhighlightCell(rowcount, 1, new TicksTiming(0),
          new TicksTiming(40));
      strMatrix.put(rowcount, 2, String.valueOf(x), new TicksTiming(0),
          new TicksTiming(40));
      strMatrix.highlightCell(rowcount, 2, new TicksTiming(0), new TicksTiming(
          40));

      lang.nextStep();

      src.toggleHighlight(5, 0, false, 6, 0);

      if (x != 1 && x != n - 1) {
        tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
            AnimalScript.DIRECTION_W), "x ist weder 1 noch " + (n - 1)
            + " \u21D2 Nicht bestanden.", "tests" + y, null, testProp));
        strMatrix.unhighlightCell(rowcount, 2, new TicksTiming(0),
            new TicksTiming(40));
        strMatrix.put(rowcount, 3, "          false", new TicksTiming(0),
            new TicksTiming(40));
        strMatrix.highlightCell(rowcount, 3, new TicksTiming(0),
            new TicksTiming(40));
      } else {
        tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
            AnimalScript.DIRECTION_W), "x ist " + x + " \u21D2 Bestanden.",
            "tests" + y, null, testProp));
        strMatrix.unhighlightCell(rowcount, 2, new TicksTiming(0),
            new TicksTiming(40));
        strMatrix.put(rowcount, 3, "          true", new TicksTiming(0),
            new TicksTiming(40));
        strMatrix.highlightCell(rowcount, 3, new TicksTiming(0),
            new TicksTiming(40));
      }
      y++;

      lang.nextStep();
      src.unhighlight(6, 0, false);

      strMatrix.unhighlightCell(rowcount, 3, new TicksTiming(0),
          new TicksTiming(40));

      // falls x = 1 oder x = n - 1, springe zur nächsten Schleife
      if (x != 1 && x != n - 1) {

        src.highlight(7, 0, false);

        lang.nextStep();

        src.unhighlight(7, 0, false);

        long temp;

        // für r = 1 . . s
        for (long r = 1; r < s; r++) {
          src.highlight(8, 0, false);
          tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
              AnimalScript.DIRECTION_W), "Durchlauf innere Schleife: " + r,
              "tests" + y, null, testProp));
          y++;

          strMatrix.put(rowcount, 4, String.valueOf(r), new TicksTiming(0),
              new TicksTiming(40));
          strMatrix.highlightCell(rowcount, 4, new TicksTiming(0),
              new TicksTiming(40));

          lang.nextStep();

          temp = x;

          // x <- x^2 mod n
          x = (x * x) % n;

          tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
              AnimalScript.DIRECTION_W), "x = " + temp + "^2 mod " + n + " = "
              + x, "tests" + y, null, testProp));
          y++;

          strMatrix.unhighlightCell(rowcount, 4, new TicksTiming(0),
              new TicksTiming(40));
          strMatrix.put(rowcount, 2, String.valueOf(x), new TicksTiming(0),
              new TicksTiming(40));
          strMatrix.highlightCell(rowcount, 2, new TicksTiming(0),
              new TicksTiming(40));

          if (r != 1) {
            strMatrix.put(rowcount, 0, "      -", new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.put(rowcount, 1, "   -", new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.put(rowcount, 3, "             -", new TicksTiming(0),
                new TicksTiming(40));
          }

          lang.nextStep();

          src.toggleHighlight(8, 0, false, 9, 0);

          // falls x = 1, gebe ''zusammengesetzt'' zurück
          if (x == 1) {
            tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
                AnimalScript.DIRECTION_W),
                "x = 1 \u21D2 n ist zusammengesetzt.", "tests" + y, null,
                testProp));
            y++;

            strMatrix.unhighlightCell(rowcount, 2, new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.put(rowcount, 5, "true", new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.highlightCell(rowcount, 5, new TicksTiming(0),
                new TicksTiming(40));

            bestanden = false;
            lang.nextStep();
            strMatrix.unhighlightCell(rowcount, 5, new TicksTiming(0),
                new TicksTiming(40));
            src.unhighlight(9, 0, false);
            return;
          } else {
            tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
                AnimalScript.DIRECTION_W),
                "x ungleich 1 \u21D2 Nicht bestanden.", "tests" + y, null,
                testProp));
            y++;

            strMatrix.unhighlightCell(rowcount, 2, new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.put(rowcount, 5, "false", new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.highlightCell(rowcount, 5, new TicksTiming(0),
                new TicksTiming(40));

          }

          lang.nextStep();

          src.toggleHighlight(9, 0, false, 10, 0);

          // falls x = n - 1, springe zur nächsten Schleife
          if (x == n - 1) {
            tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
                AnimalScript.DIRECTION_W), "x = " + (n - 1)
                + " \u21D2 Bestanden.", "tests" + y, null, testProp));
            y++;

            strMatrix.unhighlightCell(rowcount, 5, new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.put(rowcount, 6, "true", new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.highlightCell(rowcount, 6, new TicksTiming(0),
                new TicksTiming(40));

            lang.nextStep();
            src.unhighlight(10, 0, false);
            strMatrix.unhighlightCell(rowcount, 6, new TicksTiming(0),
                new TicksTiming(40));
            continue outer;
          } else {
            tests.add(lang.newText(new Offset(0, 17, "tests" + (y - 1),
                AnimalScript.DIRECTION_W),
                "x ungleich n - 1 \u21D2 Nicht bestanden.", "tests" + y, null,
                testProp));
            y++;

            strMatrix.unhighlightCell(rowcount, 5, new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.put(rowcount, 6, "false", new TicksTiming(0),
                new TicksTiming(40));
            strMatrix.highlightCell(rowcount, 6, new TicksTiming(0),
                new TicksTiming(40));

            lang.nextStep();
            src.unhighlight(10, 0, false);
            strMatrix.unhighlightCell(rowcount, 6, new TicksTiming(0),
                new TicksTiming(40));
          }
          rowcount++;
        }
        // gebe ''zusammengesetzt'' zurück
        src.highlight(11, 0, false);
        tests.add(lang.newText(new Offset(0, 25, "tests" + (y - 1),
            AnimalScript.DIRECTION_W), "n ist zusammengesetzt.", "tests" + y,
            null, testProp));
        y++;
        bestanden = false;
        lang.nextStep();
        src.unhighlight(11, 0, false);
        return;
      }
    }
    // gebe ''n ist wahrscheinlich eine Primzahl'' zurück
    src.highlight(12, 0, false);
    tests.add(lang.newText(new Offset(0, 25, "tests" + (y - 1),
        AnimalScript.DIRECTION_W), "n ist wahrscheinlich eine Primzahl.",
        "tests" + y, null, testProp));
    y++;
    bestanden = true;
    lang.nextStep("Zusammenfassung");
    src.unhighlight(12, 0, false);
    finish();
  }

  // Abschlussfolie
  public void finish() {
    // Alles hiden von vorher
    for (int i = 0; i < tests.size(); i++) {
      tests.get(i).hide();
    }
    src.hide();
    eins.hide();
    zwei.hide();
    eingabe.hide();
    anzahlTests.hide();
    zweiHochS.hide();
    nMinusEins.hide();
    strMatrix.hide();

    lang.newText(new Offset(15, 50, "hRect", AnimalScript.DIRECTION_SW),
        "Ergebnis", "endText1", null, testHeaderProp);

    lang.newText(new Offset(15, 35, "endText1", AnimalScript.DIRECTION_W),
        "Zu testende Zahl (n):                 " + this.n, "endText2", null);

    lang.newText(new Offset(0, 10, "endText2", AnimalScript.DIRECTION_W),
        "Anzahl maximaler Tests (k):    " + this.k, "endText3", null);

    // Verschiedene Ausgaben, je nachdem ob n prim ist, oder nicht.
    if (bestanden) {
      double d = (1 - (Math.pow(0.25, k))) * 100;
      double e = Math.pow(0.25, k) * 1000;
      DecimalFormat df = new DecimalFormat("0.000000");
      String wahrschein = df.format(d);
      String rest = df.format(e);

      lang.newText(new Offset(0, 25, "endText3", AnimalScript.DIRECTION_W),
          "Die Zahl " + n + " hat alle Primzahltests bestanden.", "endText4",
          null);

      lang.newText(new Offset(0, 25, "endText4", AnimalScript.DIRECTION_W),
          "Sie ist mit " + wahrschein
              + "%iger Wahrscheinlichkeit eine Primzahl.", "endText5", null);

      lang.newText(
          new Offset(0, 10, "endText5", AnimalScript.DIRECTION_W),
          "Die Wahrscheinlichkeit beträgt weniger als 100%, auch wenn dies eventuell durch Rundungsfehler suggeriert wird.",
          "endText6", null);

      lang.newText(new Offset(0, 10, "endText6", AnimalScript.DIRECTION_W),
          "Die Restfehlerwahrscheinlichkeit beträgt (1/4)^" + k
              + ", also etwa " + rest + " Promille.", "endText7", null);
    } else {
      lang.newText(new Offset(0, 25, "endText3", AnimalScript.DIRECTION_W),
          "Alle Primzahltests wurden nicht bestanden, " + n
              + " ist keine Primzahl.", "endText4", null);

      lang.newText(new Offset(0, 10, "endText4", AnimalScript.DIRECTION_W),
          "Die Zahl " + randomTemp + " ist Zeuge.", "endText5", null);
    }
  }

  // Wir ermitteln im Vorfeld die Länge der Matrix.
  // Anders leider nicht möglich.
  public int getMatrixLength() {
    int rowcount = 1;
    int s = 0;
    int d = n - 1;
    while (d % 2 == 0) {
      s++;
      d /= 2;
    }

    numbers = new int[k];

    outer:
    // Schleife: Wiederhole k mal:
    for (int i = 0; i < k; i++) {
      rowcount++;
      Random rnd = new Random();
      int a = rnd.nextInt((n - 2) - 2) + 2;
      numbers[i] = a;
      long x = modpow(a, d, n);
      if (x != 1 && x != n - 1) {
        for (long r = 1; r < s; r++) {
          x = (x * x) % n;
          if (x == 1) {
            return rowcount;
          }
          if (x == n - 1) {
            continue outer;
          }
          rowcount++;
        }
        return rowcount;
      }
    }
    return rowcount;
  }

  public String getName() {
    return "Miller-Rabin-Test";
  }

  public String getAlgorithmName() {
    return "Miller-Rabin-Test";
  }

  public String getAnimationAuthor() {
    return "Julian Wulfheide, Tim Wimmer, Denis Caruso";
  }

  public String getDescription() {
    return "Der Miller-Rabin-Test oder Miller-Selfridge-Rabin-Test ist ein probabilistischer Primzahltest.<br>"
        + "\n"
        + "Erh&auml;lt der probabilistische Test eine nat&uuml;rliche Zahl <i>n</i> als Eingabe, so gibt er entweder<br>"
        + "\n"
        + "&quot;n ist keine Primzahl&quot; oder &quot;n ist wahrscheinlich eine Primzahl&quot; aus.<br>"
        + "\n"
        + "Das Ergebnis ist hierbei von <i>n</i> und dem Zufall abh&auml;ngig.<br>"
        + "\n"
        + "Der Miller-Rabin-Test pr&uuml;ft hierbei, ob eine gegebene Zahl <i>n</i> zusammengesetzt ist.<br>"
        + "\n"
        + "Zun&auml;chst wird <i>n-1</i> in eine ungerade Zahl <i>d</i> und eine Zweierpotenz <i>2<sup>s</sup></i> zerlegt (n-1 = 2<sup>d</sup> * d).<br>"
        + "\n"
        + "Daraufhin werden f&uuml;r zuf&auml;llige Zahlen <i>a</i> aus dem Bereich <i>1 &lt; a &lt; n-1</i> die folgenden Tests durchgef&uuml;hrt:<br>&nbsp;<br>"
        + "\n"
        + "\n"
        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Test 1:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Ist <i>a<sup>d</sup> &equiv; 1 mod n</i><br>"
        + "\n"
        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Test 2:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; f&uuml;r alle <i>i</i> mit <i>0 &le; i &lt; s</i>: Gibt es ein <i>i</i> mit <i>a^((2<sup>i</sup>)*d) &equiv; -1 mod n</i><br>&nbsp;<br>"
        + "\n"
        + "\n"
        + "Der Primzahlen-Test besteht nun darin, zu der zu pr&uuml;fenden Zahl <i>n</i> eine teilerfremde Zahl <i>a</i> zu finden,<br>"
        + "\n"
        + "die keine der beiden Aussagen erf&uuml;llt. Dies w&uuml;rde die Nicht-Primalit&auml;t von <i>n</i> definitiv best&auml;tigen.<br>"
        + "\n"
        + "Da mehr als &frac34; der Zahlen aus [2, n-1] dies tun, ist die Wahrscheinlichkeit,<br>"
        + "\n"
        + "bei einem einmaligen Versuch, keinen dieser ''Zeugen'' gegen die Primalit&auml;t von <i>n</i> zu finden,<br>"
        + "\n" + "kleiner als &frac14;.";
  }

  public String getCodeExample() {
    return "Eingabe: <i>n &gt; 3</i>, eine ungerade Zahl, welche auf Primalit&auml;t getestet werden soll;"
        + "\n"
        + "Eingabe: <i>k</i>, ein Parameter, welcher die Genauigkeit des Tests bestimmt"
        + "\n"
        + "Ausgabe: &quot;zusammengesetzt&quot;, wenn <i>n</i> zusammengesetzt ist, andernfalls &quot;n ist wahrscheinlich eine Primzahl&quot;"
        + "\n"
        + "Schreibe <i>n - 1</i> als <i>2<sup>s</sup> * d</i> durch 2er Potenzen von <i>n - 1</i>, wobei <i>d</i> ungerade"
        + "\n"
        + "Schleife: Wiederhole <i>k</i> mal:"
        + "\n"
        + "     w&auml;hle eine zuf&auml;llige Zahl <i>a</i> zwischen <i>2</i> und<i> n - 2</i>"
        + "\n"
        + "     <i>x &larr; a<sup>d</sup> mod n</i>"
        + "\n"
        + "     falls <i>x = 1</i> oder <i>x = n - 1</i>, springe zur n&auml;chsten Schleife"
        + "\n"
        + "     f&uuml;r <i>r = 1 . . s</i>"
        + "\n"
        + "          <i>x &larr; x<sup>2</sup> mod n</i>"
        + "\n"
        + "          falls <i>x = 1</i>, gebe &quot;zusammengesetzt&quot; zur&uuml;ck"
        + "\n"
        + "          falls <i>x = n - 1</i>, springe zur n&auml;chsten Schleife"
        + "\n"
        + "     gebe &quot;zusammengesetzt&quot; zur&uuml;ck"
        + "\n"
        + "gebe &quot;n ist wahrscheinlich eine Primzahl&quot; zur&uuml;ck";
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

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    if ((Integer) primitives.get("n") < 3) {
      throw new IllegalArgumentException("n must be greater 2!");
    } else if ((Integer) primitives.get("k") < 1) {
      throw new IllegalArgumentException("k must be greater 0!");
    }
    return true;
  }

}