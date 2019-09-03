package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Simplex2 implements Generator {

  // Algorithm internal variables
  private static final double EPSILON = 1.0E-10;
  private double[][]          a;                // tableaux
  private int                 M;                // number of constraints
  private int                 N;                // number of original variables

  private int[]               basis;            // basis[i] = basic variable
                                                 // corresponding to row i
                                                 // only needed to print out
                                                 // solution, not book
  private int                 loops;

  // Animal Variables
  public Language             lang;
  private Text[][]            simplexTableaux, helpCalcTableux;
  private Text                header;
  private Text                ti1;
  private Text                ti2;
  private Text                pivotSpalte;
  private Text                pivotZeile;
  private Text                pivotElement;
  private RectProperties      backgroundHeaderRectProp;
  // private SourceCodeProperties sourceCodeProp;
  private SourceCode          sourceCode;
  private double[][]          helpCalc;

  private Color               HervorhebungZeile;
  // private Color HervorhebungSpalte;
  private Color               PivotElement;
  // private Color Infoline;

  private double[][]          Koeffizienten;
  private double[]            Beschraenkungen;
  private double[]            Zielfunktion;

  public static void main(String[] args) {
    Simplex2 s = new Simplex2();
    s.init();
    s.lang.setStepMode(true);

    Hashtable<String, Object> p = new Hashtable<String, Object>();
    int[][] A = { { 1, 1 }, { 6, 9 }, { 0, 1 }, };
    int[] c = { 10, 20 };
    int[] b = { 100, 720, 60 };

    p.put("Koeffizienten", A);
    p.put("Beschraenkungen", b);
    p.put("Zielfunktion", c);
    p.put("Informationszeile", Color.BLACK);
    p.put("PivotElement", Color.GREEN);
    p.put("HervorhebungSpalte", Color.RED);
    p.put("HervorhebungZeile", Color.RED);

    s.generate(null, p);
    s.saveToFile("drop.asu");
  }

  public void init() {
    lang = new AnimalScript("Simplex",
        "Benjamin Boerngen-Schmidt, Julian Bonrath", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    backgroundHeaderRectProp = new RectProperties();
    backgroundHeaderRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.GRAY);
    backgroundHeaderRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);
    backgroundHeaderRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    backgroundHeaderRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // Infoline = (Color)primitives.get("Informationszeile");
    HervorhebungZeile = (Color) primitives.get("HervorhebungZeile");
    // HervorhebungSpalte = (Color)primitives.get("HervorhebungSpalte");
    PivotElement = (Color) primitives.get("PivotElement");

    int[][] A = (int[][]) primitives.get("Koeffizienten"); // A
    int[] b = (int[]) primitives.get("Beschraenkungen"); // b
    int[] c = (int[]) primitives.get("Zielfunktion"); // c

    Koeffizienten = toDouble(A);
    Beschraenkungen = toDouble(b);
    Zielfunktion = toDouble(c);

    M = Koeffizienten.length;
    N = Zielfunktion.length;

    // Erstellen eines Tableaux ( Nichtbasis Variablen + F, nicht BV +
    // Schlupfvar. + Bi)
    a = new double[N + M + 1][M + 1];
    for (int i = 0; i < M; i++)
      for (int j = 0; j < N; j++)
        a[j][i] = Koeffizienten[i][j]; // Kopieren der Werte der Koeffizienten
    for (int j = 0; j < M; j++)
      a[N + j][j] = 1.0; // Einheitsmatix bei Schlupfvariablen
    for (int j = 0; j < M; j++)
      a[N + M][j] = Beschraenkungen[j]; // Beschränkungen
    for (int i = 0; i < N; i++)
      a[i][M] = Zielfunktion[i]; // Zielfunktion

    basis = new int[M];
    for (int i = 0; i < M; i++) { // Verweis, welche Variablen in der basis
                                  // sind.Zu Anfang nur die Schlupfvar.
      basis[i] = N + i;
    }

    // Start Algorithm and visualizing
    init();
    this.Introduction();
    lang.hideAllPrimitives();
    this.initialize();
    lang.nextStep("Update Tableu");
    this.updateTableaux();
    lang.nextStep("Löse");
    solve();
    lang.hideAllPrimitives();
    end();
    lang.finalizeGeneration();

    // check optimality conditions
    assert check(Koeffizienten, Beschraenkungen, Zielfunktion);

    return lang.toString();
  }

  private void end() {
    String s = "";
    for (int i : basis) {
      s += ", x_" + (i + 1);
    }
    s = s.substring(2);

    // lang.hideAllPrimitives();
    // lang.nextStep();
    header.setText("Simplex Analyse", null, null);
    header.show();

    lang.newText(new Offset(10, 40, header, AnimalScript.DIRECTION_SW),
        "In der optimalen Lösung sind die Variablen " + s + " ", "end1", null);
    lang.newText(new Offset(10, 10, "end1", AnimalScript.DIRECTION_SW),
        "mit den Werten " + this.BasisLoesung() + " enthalten.", "end2", null);

    SourceCode ending = lang.newSourceCode(new Offset(10, 25, "end2",
        AnimalScript.DIRECTION_SW), "ending", null);
    ending.addCodeLine("Unsere optimale Lösung haben wir nach" + loops
        + " Iterationen gefunden", null, 0, null);
    ending
        .addCodeLine(
            "Wir hoffen dir mit dieser Animation zu einem besserem Verständniss für die",
            null, 0, null);
    ending
        .addCodeLine(
            "Berechnung einer linearen Optimierung mit dem Simplex Algorithmus geholfen zu haben",
            null, 0, null);
    ending.addCodeLine(" ", null, 0, null);
    ending
        .addCodeLine(
            "Wenn ihr Fragen, Anmerkungen oder Verbesserungen zu dieser Animation habt",
            null, 0, null);
    ending.addCodeLine("dann schreib uns eine E-Mail an", null, 0, null);
    ending.addCodeLine("benjamin@boerngen-schmidt.de oder", null, 0, null);
    ending.addCodeLine("julian.bonrath@gmail.com", null, 0, null);
    ending.addCodeLine("", null, 0, null);
    ending.addCodeLine("Wir bedanken uns für deine Aufmerksamkeit ", null, 0,
        null);
  }

  public String getName() {
    return "Simplex Algorithmus [DE]";
  }

  public String getAlgorithmName() {
    return "Simplex-Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Benjamin Börngen-Schmidt, Julian Bonrath";
  }

  public String getDescription() {
    return "<h1>Simplex-Algorithmus </h1><br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Der Simplex-Algorithmus oder auch Simplex-Verfahren genannt ist ein<br>"
        + "\n"
        + "Optimierungsverfahren aus dem Bereich des Operation Research.<br>"
        + "\n"
        + "Er dient dazu eine Lösung für lineare Optimierungsprobleme zu finden,<br>"
        + "\n"
        + "wie sie z.B. in der Produktionsplanung vorkommen.<br>"
        + "\n"
        + "Vorteil des Verfahren ist, dass es ein Problem in endlich vielen Schrittten<br>"
        + "\n"
        + "exakt löst oder die Unlösbarkeit bzw Unbeschränktheit feststellt.<br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Die Geometrische Idee hinter dem Algorithmus bestaht darin, von einer<br>"
        + "\n"
        + "frei wählbaren Ecke eines Polyeders zu starten. Dieses Polyeder ist durch<br>"
        + "\n"
        + "die linearen Gleichungen des Optimierungsproblemes bestimmt.<br>"
        + "\n"
        + "Entlang der Kanten des Polyeders läuft der Algorithmus nun zur optimalen Ecke.<br>"
        + "\n"
        + "<br>"
        + "\n"
        + "Dabei besteht das Problem einen gültigen Startpunkt zu finden, welches aber<br>"
        + "\n"
        + "durch starten am  Nullpunkt gelöst werden kann. Dies entspricht dann<br>"
        + "\n" + "einer Lösung in der nichts Produziert wird.";
  }

  public String getCodeExample() {
    return "Schritt 1: Wahl der Pivotspalte t \n"
        + "  - keine negativen Eintragungen in F-Zeile: aktuelle Basisloesung optimal -> Ende des Verfahrens \n"
        + "  - negative Eintragung in der F-Zeile: Verbesserung bei Aufnahme der Variable in die Basis \n"
        + "    Auswahl der Variable mit kleinster ('negativster') Eintragung \n"
        + "Schritt 2: Wahl der Pivotzeile s \n"
        + "  - alle Koeffizienten a'_it  in der Pivotspalte t negativ -> unbeschränktes Modell; Abbruch \n"
        + "  - ansonsten waehle Pivotzeile s, fuer die   gilt \n"
        + "    Pivotelement: a'_st \n"
        + "Schritt 3: Berechnung der neuen Basisloesung, Transformation des Tableaus \n"
        + "  - Tausche bisherige BV in Pivotzeile s gegen bisherige NBV  x_t. \n"
        + "  - Schaffe unter neuer BV x_t einen Einheitsvektor mit a'_st = 1 durch Anwenden linearer \n"
        + "    Transformationen: \n"
        + "      a) Dividiere die Pivotzeile durch das Pivotelement a'_st \n"
        + "    b) Multipliziere die neue Pivotzeile mit -a'_it und addiere sie zur Zeile i hinzu (fuer alle i ohne s)";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  // Simplex Functions
  // run simplex algorithm starting from initial BFS
  private void solve() {
    loops = 0;
    sourceCode = createSourceCode(500, 100);
    while (true) {
      lang.nextStep("Iteration " + (loops + 1) + ": Finde Pivotspalte");
      loops++;
      // find entering column q
      int q = bland();
      this.sourceCode.highlight(0);
      if (q == -1) {
        questionIsOptimal(true);
        sourceCode.highlight(1);
        this.ti1.setText("Aktuelle Basislösung ist Optimal", null, null);
        this.ti2.setText("In der F-Zeile sind keine negativen Eintragungen ",
            null, null);
        break; // optimal
      } else {
        questionIsOptimal(false);
      }

      lang.nextStep();

      this.questionPivotSpalte(q, M + N);
      lang.nextStep();
      this.sourceCode.highlight(2);
      this.sourceCode.highlight(3);

      this.highlightCol(q + 1, Color.RED, new TicksTiming(300), null);
      this.pivotSpalte.setText("" + (q + 1), new TicksTiming(250),
          new TicksTiming(200));
      this.ti1.setText(Zielfunktion[q]
          + " ist der kleinste Werte in der F-Zeile", null, null);
      this.ti2.setText("", null, null);

      lang.nextStep("Iteration " + (loops + 1) + ": Finde Pivotzeile");

      // find leaving row p
      int p = minRatioRule(q);
      this.sourceCode.unhighlight(0);
      this.sourceCode.unhighlight(2);
      this.sourceCode.unhighlight(3);
      this.sourceCode.highlight(4);
      this.questionPivotZeile(p, basis);
      lang.nextStep();
      if (p == -1) {
        this.sourceCode.highlight(5);
        throw new RuntimeException("Linear program is unbounded");
      }
      this.sourceCode.highlight(6);

      this.pivotZeile.setText("" + (p + 1), new TicksTiming(250),
          new TicksTiming(200));
      this.highlightRow(p + 1, this.HervorhebungZeile, new TicksTiming(300),
          null);
      this.pivotZeile.setText("" + (p + 1), new TicksTiming(250),
          new TicksTiming(200));
      this.ti1.setText(a[N + M][p]
          + " ist der kleinste Werte in der b'_i / a'_it Spalte",
          new TicksTiming(250), new TicksTiming(250));
      this.ti2.setText("", new TicksTiming(250), new TicksTiming(250));
      lang.nextStep("Iteration " + (loops + 1) + ": Pivotelement");

      // pivot
      this.highlightPivot(p + 1, q + 1, this.PivotElement);
      this.pivotElement.setText("" + a[q][p], new TicksTiming(250),
          new TicksTiming(200));
      this.ti1.setText("Das Pivotelement a'_st hat den Wert " + a[q][p]
          + " und befindet sich", new TicksTiming(250), new TicksTiming(250));
      this.ti2.setText("am Schnittpunkt der Pivotspalte x_" + (q + 1)
          + " und der Pivotzeile x_" + (basis[p] + 1), new TicksTiming(250),
          new TicksTiming(250));

      this.sourceCode.unhighlight(6);
      this.sourceCode.highlight(7);

      lang.nextStep("Iteration " + (loops + 1) + ": Update Basis");

      // do transformation
      this.sourceCode.unhighlight(4);
      this.sourceCode.unhighlight(7);
      this.sourceCode.highlight(8);
      this.sourceCode.highlight(9);
      // update basis
      basis[p] = q;
      this.ti1
          .setText("Die bisherige Basisvariable x_" + (basis[p] + 1)
              + " wird gegen die neue ", new TicksTiming(250), new TicksTiming(
              250));

      this.updateTableaux();

      this.ti2.setText("Basisvariable x_" + (basis[p] + 1)
          + " ausgetauscht (beachte Position des Pivotelemts)",
          new TicksTiming(250), new TicksTiming(250));

      lang.nextStep("Iteration " + (loops + 1) + ": Transformiere");

      this.sourceCode.unhighlight(9);

      this.sourceCode.highlight(10);
      this.sourceCode.highlight(11);
      this.ti1
          .setText(
              "In den nächsten Schritten wird die die Pivotspalte so transformiert",
              new TicksTiming(250), new TicksTiming(250));
      this.ti2.setText(
          "dass bis auf das Pivotelement alle Zellen den Wert 0 haben",
          new TicksTiming(250), new TicksTiming(250));

      lang.nextStep();

      pivot(p, q);

      this.updateTableaux();
      this.ti1.setText("Basisloesung: " + this.BasisLoesung(), null, null);
      this.ti2.setText("Wert: " + this.value(), null, null);
      this.unHighlightTableaux(Color.BLACK);
      this.sourceCode.highlight(13);
      this.sourceCode.highlight(10);
      this.sourceCode.highlight(11);
      lang.nextStep();
      clearHelpCalc();

    }
  }

  private void clearHelpCalc() {
    for (int i = 1; i < simplexTableaux.length; i++) {
      for (int j = 1; j < simplexTableaux[0].length; j++) {
        simplexTableaux[i][j].setText("", null, null);
      }
    }

  }

  private SourceCode createSourceCode(int i, int j) {
    SourceCodeProperties scp = new SourceCodeProperties();
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    SourceCode simplexCode = lang.newSourceCode(new Offset(50, 0,
        this.simplexTableaux[0][this.simplexTableaux[0].length - 1],
        AnimalScript.DIRECTION_NE), "simplex_code", null, scp);
    simplexCode.addCodeLine("Schritt 1: Wahl der Pivotspalte t", null, 0, null);
    simplexCode
        .addCodeLine(
            "- keine negativen Eintragungen in F-Zeile: aktuelle Basisloesung optimal -> Ende des Verfahrens",
            null, 1, null);
    simplexCode
        .addCodeLine(
            "- negative Eintragung in der F-Zeile: Verbesserung bei Aufnahme der Variable in die Basis",
            null, 1, null);
    simplexCode.addCodeLine(
        "    Auswahl der Variable mit kleinster ('negativster') Eintragung",
        null, 1, null);
    simplexCode.addCodeLine("Schritt 2: Wahl der Pivotzeile s", null, 0, null);
    simplexCode
        .addCodeLine(
            "- alle Koeffizienten a'_it in der Pivotspalte t negativ -> unbeschränktes Modell; Abbruch",
            null, 1, null);
    simplexCode
        .addCodeLine(
            "- ansonsten waehle Pivotzeile s, deren Verhältnis von b'_i und a'_it minimal ist",
            null, 1, null);
    simplexCode.addCodeLine("  Pivotelement: a'_st", null, 1, null);
    simplexCode
        .addCodeLine(
            "Schritt 3: Berechnung der neuen Basisloesung, Transformation des Tableaus",
            null, 0, null);
    simplexCode.addCodeLine(
        "- Tausche bisherige BV in Pivotzeile s gegen bisherige NBV  x_t.",
        null, 1, null);
    simplexCode
        .addCodeLine(
            "- Schaffe unter neuer BV x_t einen Einheitsvektor mit a'_st = 1 durch Anwenden linearer",
            null, 1, null);
    simplexCode.addCodeLine("  Transformationen:", null, 1, null);
    simplexCode.addCodeLine(
        "a) Dividiere die Pivotzeile durch das Pivotelement a'_st", null, 2,
        null);
    simplexCode
        .addCodeLine(
            "b) Multipliziere die neue Pivotzeile mit -a'_it und addiere sie zur Zeile i hinzu (fuer alle i ohne s)",
            null, 2, null);

    return simplexCode;
  }

  private String BasisLoesung() {
    String loesung = "";
    double[] dLoesung = new double[M + N];
    for (int i = 0; i < dLoesung.length; i++) {
      dLoesung[i] = 0.0;

    }
    for (int j = 0; j < basis.length; j++) {
      dLoesung[basis[j]] = a[N + M][j];
    }

    for (double x : dLoesung) {
      loesung += ", " + x;
    }
    return loesung.substring(2);
  }

  /**
   * Suche größte positive Zahl in der F Zeile
   * 
   * @return Spaltenindex oder -1 wenn nicht gefunden
   */
  private int bland() {
    int foo = -1;
    double max = 0;
    for (int j = 0; j < M + N; j++)
      if (a[j][M] > max) {
        max = a[j][M];
        foo = j;
      }
    return foo; // optimal
  }

  // find row p using min ratio rule (-1 if no such row)
  /**
   * 
   * @param q
   * @return
   */
  private int minRatioRule(int q) {
    int p = -1;
    for (int i = 0; i < M; i++) {
      if (a[q][i] <= 0)
        continue; // weil nicht durch Null teilen
      else if (p == -1)
        p = i;
      else if ((a[M + N][i] / a[q][i]) < (a[M + N][p] / a[q][p])) {
        p = i;
      }
    }
    return p;
  }

  // pivot on entry (p, q) using Gauss-Jordan elimination
  /**
   * 
   * @param p
   * @param q
   */
  private void pivot(int p, int q) {
    int i, j;
    double foo;

    // scale row p
    // Normalisiere Pivotzeile
    this.ti1.setText("Wir teilen Zeile (*)x_" + (basis[p] + 1)
        + " durch den Wert unseres Pivotelemtes", null, null);
    this.ti2.setText("In diesem Fall teilen wir durch " + a[q][p], null, null);
    this.sourceCode.highlight(12);
    // Teilen Pivotzeile durch Pivotelement
    for (j = 0; j <= M + N; j++)
      if (j != q)
        a[j][p] /= a[q][p];

    a[q][p] = 1.0;
    this.updateTableaux(null, new TicksTiming(200));
    lang.nextStep();
    this.sourceCode.unhighlight(12);
    this.sourceCode.highlight(13);
    // Normalize Table
    // everything but row p and column q
    for (i = 0; i < a[0].length; i++) {
      if (i == p) {
        continue;
      }
      // Set First Colum in HelpCAlc

      helpCalcTableux[0][0].setText("" + (-a[q][i]) + " x_" + (basis[p] + 1),
          null, null);
      if (i == M) {
        helpCalcTableux[1][0].setText("F", null, null);
      } else {
        helpCalcTableux[1][0].setText("(*)x_" + (basis[i] + 1), null, null);
      }
      helpCalcTableux[2][0].setText("Summe", null, null);
      ;

      // Create HelpCalc and calc Simplex Table
      foo = a[q][i];
      for (j = 0; j < a.length; j++) {

        this.ti1.setText(
            "Trage Werte der Pivotzeile in 1. Hilfzeile ein und multipliziere sie mit -"
                + foo, null, null);
        this.ti2.setText("Trage Werte der " + (i + 1)
            + ". oberen Zeile in 2. Hilfzeile ein", null, null);

        helpCalc[j][0] = (-foo * a[j][p]); // Pivot
        helpCalc[j][1] = a[j][i]; // Zeile

        // Calculate new Value for SimplexTableu
        if (i != p)
          a[j][i] = a[j][i] + helpCalc[j][0];

        // Summe
        helpCalc[j][2] = helpCalc[j][0] + helpCalc[j][1];

      }
      this.updateHelpCalcTableaux();
      lang.nextStep();
      // zero out column q
      if (i != p)
        a[q][i] = 0.0;
      this.updateTableaux(null, new TicksTiming(200));

    }
    lang.nextStep();

    this.updateTableaux(new TicksTiming(200), new TicksTiming(200));
    lang.nextStep();
  }

  private void questionPivotSpalte(int col, int possibleCols) {
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestionCols" + loops);
    mcq.setPrompt("Welche Spalte ist unsere Pivotspalte?");
    for (int i = 1; i <= possibleCols; i++) {
      if (i == (col + 1)) {
        mcq.addAnswer("Spalte " + i + " x_" + i, 1,
            "Ja da hier der größte Wert in der F-Zeile steht.");
      } else {
        mcq.addAnswer("Spalte " + i + " x_" + i, 0,
            "Nein, es gibt einen größeren Wert in der F-Zeile.");
      }
    }
    mcq.setGroupID("PivotColumn");
    lang.addMCQuestion(mcq);
  }

  private void questionPivotZeile(int row, int[] possibleRows) {
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestionRows" + loops);
    mcq.setPrompt("Welche Zeile ist unsere Pivotzeile?");
    for (int i = 0; i < possibleRows.length; i++) {
      if (i == row) {
        mcq.addAnswer("Zeile " + (i + 1) + " x_" + (possibleRows[i] + 1), 1,
            "Ja da hier der kleinste Wert in der b_i Spalte steht");
      } else {
        mcq.addAnswer("Zeile " + (i + 1) + " x_" + (possibleRows[i] + 1), 0,
            "Nein, es gibt einen kleineren Wert in der b_i Spalte");
      }
    }
    mcq.setGroupID("PivotRow");
    lang.addMCQuestion(mcq);
  }

  private void questionIsOptimal(boolean answer) {
    TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
        "IsOptimal" + loops, answer, 1);
    tfq.setPrompt("Ist die Basislösung x = (" + BasisLoesung() + ") optimal?");
    tfq.setGroupID("Optimal");
    lang.addTFQuestion(tfq);
  }

  private boolean check(double[][] A, double[] b, double[] c) {
    return isPrimalFeasible(A, b) && isOptimal(b, c);
  }

  // check that optimal value = cx = yb
  private boolean isOptimal(double[] b, double[] c) {
    double[] x = primal();
    double value = value();

    // check that value = cx = yb
    double value1 = 0.0;
    for (int j = 0; j < x.length; j++) {
      value1 += c[j] * x[j];
    }
    if (Math.abs(value - value1) > EPSILON) {
      this.ti1.setText("value = " + value + ", cx = " + value1, null, null);
      this.ti2.setText("", null, null);
      return false;
    }

    return true;
  }

  // return optimal objective value
  public double value() {
    return -a[M + N][M];
  }

  // is the solution primal feasible?
  private boolean isPrimalFeasible(double[][] A, double[] b) {
    double[] x = primal();

    // check that x >= 0
    for (int j = 0; j < x.length; j++) {
      if (x[j] < 0.0) {
        this.ti1.setText("x[" + j + "] = " + x[j] + " is negative", null, null);
        this.ti2.setText("", null, null);
        return false;
      }
    }

    // check that Ax <= b
    for (int i = 0; i < M; i++) {
      double sum = 0.0;
      for (int j = 0; j < N; j++) {
        sum += A[i][j] * x[j];
      }
      if (sum > b[i] + EPSILON) {
        this.ti1.setText("not primal feasible", null, null);
        this.ti2.setText("Beschraenkung[" + i + "] = " + b[i] + ", sum = "
            + sum, null, null);
        return false;
      }
    }
    return true;
  }

  // return primal solution vector
  public double[] primal() {
    double[] x = new double[N];
    for (int i = 0; i < M; i++)
      if (basis[i] < N)
        x[basis[i]] = a[M + N][i];
    return x;
  }

  // End Simplex functions

  // Helper Functions
  /**
   * Converts input integer Matrix to double Matrix
   * 
   * @param a
   *          Input integer Matrix
   * @return Values casted to double
   */
  private double[][] toDouble(int[][] a) {
    double[][] b = new double[a.length][a[0].length];
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[0].length; j++) {
        b[i][j] = (double) a[i][j];

      }

    }
    return b;
  }

  /**
   * Converts Input int Array to double Array
   * 
   * @param a
   *          Input int Array
   * @return Array with double values
   */
  private double[] toDouble(int[] a) {
    double[] b = new double[a.length];
    for (int j = 0; j < a.length; j++) {
      b[j] = (double) a[j];
    }
    return b;
  }

  /**
   * Unhighlights the Tableaux
   * 
   * @param simplexTableaux
   * @param row
   * @param color
   * @param t
   * @param d
   */
  private void unhighlightTableaux(Color color, Timing t, Timing d) {
    for (int i = 0; i < simplexTableaux[0].length; i++) {
      for (int j = 0; j < simplexTableaux.length; j++) {
        simplexTableaux[j][i].changeColor("", color, t, d);
      }
    }
  }

  private void unHighlightTableaux(Color color) {
    this.unhighlightTableaux(color, null, null);
  }

  // /**
  // * Highlights a Row
  // * @param simplexTableaux
  // * @param row
  // * @param color
  // */
  // private void highlightRow(int row, Color color) {
  // this.highlightRow(row, color, null, null);
  // }
  /**
   * Highlights a Row
   * 
   * @param simplexTableaux
   * @param row
   * @param color
   * @param t
   * @param d
   */
  private void highlightRow(int row, Color color, Timing t, Timing d) {
    if (simplexTableaux.length < row) {
      return;
    }
    for (int i = 0; i < simplexTableaux[row].length; i++) {
      simplexTableaux[row][i].changeColor("", color, t, d);
    }
  }

  private void highlightPivot(int col, int row, Color color) {
    this.simplexTableaux[col][row].changeColor("", color, null,
        new TicksTiming(200));
  }

  // /**
  // * Highlights a Column
  // * @param simplexTableaux
  // * @param col
  // * @param color
  // */
  // private void highlightCol(int col, Color color) {
  // this.highlightCol(col, color, null, null);
  // }
  /**
   * Highlights a Column
   * 
   * @param simplexTableaux
   * @param col
   * @param color
   * @param t
   * @param d
   */
  private void highlightCol(int col, Color color, Timing t, Timing d) {
    if (simplexTableaux[0].length < col) {
      return;
    }
    for (int i = 0; i < simplexTableaux.length; i++) {
      simplexTableaux[i][col].changeColor("", color, t, d);
    }
  }

  private void drawSimplexTableaux(int width, int height, Primitive reference) {
    simplexTableaux[0][0] = lang.newText(new Offset(10, 30, reference,
        AnimalScript.DIRECTION_SW), "", "st00", null);
    for (int i = 0; i < simplexTableaux.length; i++) {
      for (int j = 0; j < simplexTableaux[0].length; j++) {
        if (j == 0 && i == 0) {
          // skip element since we initialized it before
          continue;
        } else {
          if (i == 0 && j != 0) {
            if (j <= M + N) {
              simplexTableaux[i][j] = lang.newText(new Offset(j * width, i
                  * height, simplexTableaux[0][0], AnimalScript.DIRECTION_NW),
                  "x_" + j, "st" + i + j, null);
            } else {
              simplexTableaux[i][j] = lang.newText(new Offset(j * width, i
                  * height, simplexTableaux[0][0], AnimalScript.DIRECTION_NW),
                  "b_i", "st" + i + j, null);
            }
          } else if (j == 0 && i != 0) {
            if (i <= basis.length) {
              simplexTableaux[i][j] = lang.newText(new Offset(j * width, i
                  * height, simplexTableaux[0][0], AnimalScript.DIRECTION_NW),
                  "x_" + (basis[i - 1] + 1), "st" + i + j, null);
            } else {
              simplexTableaux[i][j] = lang.newText(new Offset(j * width, i
                  * height, simplexTableaux[0][0], AnimalScript.DIRECTION_NW),
                  "F", "st" + i + j, null);
            }
          } else {
            simplexTableaux[i][j] = lang.newText(new Offset(j * width, i
                * height, simplexTableaux[0][0], AnimalScript.DIRECTION_NW),
                "", "st" + i + j, null);

          }
        }
      }
    }
  }

  // helpCalcTableux = new Text[M+N+2][3];
  private void drawHelpCalcTableaux(int width, int height, Primitive reference) {
    helpCalcTableux[0][0] = lang.newText(new Offset(0, 50, reference,
        AnimalScript.DIRECTION_SW), "x_i", "ht00", null);
    for (int i = 0; i < helpCalcTableux.length; i++) {
      for (int j = 0; j < helpCalcTableux[0].length; j++) {
        if (j == 0 && i == 0) {
          continue;
        } else

        if (j == 0 && i != 0) {
          if (i < helpCalcTableux.length - 1) {
            helpCalcTableux[i][j] = lang.newText(new Offset(j * width, i
                * height, helpCalcTableux[0][0], AnimalScript.DIRECTION_NW),
                "x_i", "ht" + i + j, null);
          } else {
            helpCalcTableux[i][j] = lang.newText(new Offset(j * width, i
                * height, helpCalcTableux[0][0], AnimalScript.DIRECTION_NW),
                "Summe", "ht" + i + j, null);
          }
        } else {
          helpCalcTableux[i][j] = lang.newText(new Offset(j * width,
              i * height, helpCalcTableux[0][0], AnimalScript.DIRECTION_NW),
              "", "ht" + i + j, null);

        }
      }
    }

  }

  private void updateHelpCalcTableaux() {
    updateHelpCalcTableaux(null, null);
  }

  private void updateHelpCalcTableaux(Timing delay, Timing duration) {

    for (int i = 0; i < helpCalc.length; i++) {
      for (int j = 0; j < helpCalc[0].length; j++) {
        if (i == helpCalcTableux.length - 1) {
          helpCalcTableux[j][i + 1].setText(
              "" + String.format("%.2f", helpCalc[i][j]),
              new TicksTiming(1000), new TicksTiming(250));
        }
        helpCalcTableux[j][i + 1].setText(
            "" + String.format("%.2f", helpCalc[i][j]), delay, duration);
      }
    }
  }

  private void updateTableaux() {
    updateTableaux(null, null);
  }

  private void updateTableaux(Timing delay, Timing duration) {

    for (int i = 1; i < simplexTableaux.length; i++) {
      for (int j = 1; j < simplexTableaux[0].length; j++) {
        simplexTableaux[i][j].setText(String.format("%.2f", a[j - 1][i - 1]),
            delay, duration);
      }
    }

    for (int k = 0; k < basis.length; k++) {
      simplexTableaux[k + 1][0].setText("x_" + (basis[k] + 1), delay, duration);
    }
  }

  private void initialize() {
    TextProperties hp = new TextProperties();
    hp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 28));
    this.header = lang.newText(new Coordinates(10, 50), "Simplex Verfahren",
        "header", null, hp);
    Node leftTopOffset, rightBottomOffset;
    leftTopOffset = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
    rightBottomOffset = new Offset(7, 5, header, AnimalScript.DIRECTION_SE);
    lang.newRect(leftTopOffset, rightBottomOffset, "backgroundRectAlgo", null,
        backgroundHeaderRectProp);

    // Create a new Simplex Tableaux using Text
    this.simplexTableaux = new Text[M + 2][N + M + 2];
    this.drawSimplexTableaux(70, 30, this.header);
    this.helpCalc = new double[M + N + 1][3];

    this.helpCalcTableux = new Text[3][M + N + 2];
    this.drawHelpCalcTableaux(70, 30, this.simplexTableaux[M + 1][0]);

    TextProperties tip = new TextProperties();
    tip.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tip.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 12));
    this.ti1 = lang.newText(new Offset(10, 20, this.helpCalcTableux[2][0],
        AnimalScript.DIRECTION_SW), "Dies ist eine Informationszeile",
        "tableau_info1", null, tip);
    this.ti2 = lang
        .newText(
            new Offset(0, 3, ti1, AnimalScript.DIRECTION_SW),
            "Im nächsten Schritt ergänzen wir das Tableu und zeigen einige Variablen an",
            "tableau_info2", null, tip);

    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 12));
    // HashMap<String, Text> simplexOperationInfo = new HashMap<String,
    // Text>(6);
    Text ps = lang.newText(new Offset(25, 15, ti2, AnimalScript.DIRECTION_SW),
        "Pivotspalte:", "simplex_pivotspalte_tex", null, tp);
    Text pz = lang.newText(new Offset(25, 35, ti2, AnimalScript.DIRECTION_SW),
        "Pivotzeile:", "simplex_pivotspalte_tex", null, tp);
    Text pe = lang.newText(new Offset(25, 55, ti2, AnimalScript.DIRECTION_SW),
        "Pivotelement", "simplex_pivotspalte_tex", null, tp);
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    this.pivotSpalte = lang.newText(new Offset(15, 0, ps,
        AnimalScript.DIRECTION_NE), "-", "simplex_pivotspalte_tex", null, tp);
    this.pivotZeile = lang.newText(new Offset(15, 0, pz,
        AnimalScript.DIRECTION_NE), "-", "simplex_pivotspalte_tex", null, tp);
    this.pivotElement = lang.newText(new Offset(15, 0, pe,
        AnimalScript.DIRECTION_NE), "-", "simplex_pivotspalte_tex", null, tp);
  }

  public void Introduction() {
    TextProperties hp = new TextProperties();
    hp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 28));
    header = lang.newText(new Coordinates(10, 30), "Einleitung", "header",
        null, hp);

    lang.newText(new Offset(0, 10, header, "SW"),
        "Eine Einleitung, die die Grundidee des Algorithmus darstellt",
        "einleitung", null);

    SourceCodeProperties sdp = new SourceCodeProperties();
    sdp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sdp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sdp.set(AnimationPropertiesKeys.SIZE_PROPERTY, 14);
    SourceCode simplexDescription = lang.newSourceCode(new Offset(10, 70,
        header, AnimalScript.DIRECTION_SW), "simplex_description", null, sdp);
    simplexDescription
        .addCodeLine(
            "Der Simplex-Algorithmus oder auch Simplex-Verfahren genannt ist ein Optimierungsverfahren aus dem Beriech des Operation Research. ",
            null, 0, null);
    simplexDescription
        .addCodeLine(
            "Er dient dazu eine Lösung für lineare Optimierungsprobleme zu finden, wie sie z.B. in der Produktionsplanung vorkommen.",
            null, 0, null);
    simplexDescription
        .addCodeLine(
            "Vorteil des Verfahren ist, dass es ein Problem in endlich vielen Schrittten exakt löst oder die Unlösbarkeit bzw Unbeschränktheit feststellt.",
            null, 0, null);
    simplexDescription.addCodeLine(" ", null, 0, null);
    simplexDescription
        .addCodeLine(
            "Die Geometrische Idee hinter dem Algorithmus bestaht darin, von einer frei wählbaren Ecke eines Polyeders zu starten. Dieses Polyeder ist durch",
            null, 0, null);
    simplexDescription
        .addCodeLine(
            "die linearen Gleichungen des Optimierungsproblemes bestimmt. Entlang der Kanten des Polyeders läuft der Algorithmus nun zur optimalen Ecke.",
            null, 0, null);
    simplexDescription.addCodeLine(" ", null, 0, null);
    simplexDescription
        .addCodeLine(
            "Dabei besteht das Problem einen gültigen Startpunkt zu finden, welches aber durch starten am  Nullpunkt gelöst werden kann. Dies entspricht",
            null, 0, null);
    simplexDescription.addCodeLine(
        "dann einer Lösung in der nichts Produziert wird.", null, 0, null);

    Node leftTopOffset, rightBottomOffset;
    leftTopOffset = new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW);
    rightBottomOffset = new Offset(7, 5, "header", AnimalScript.DIRECTION_SE);
    lang.newRect(leftTopOffset, rightBottomOffset, "test", null,
        backgroundHeaderRectProp);
    lang.nextStep();
  }

  private void saveToFile(String filename) {

    BufferedWriter out;
    try {
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
          filename), "UTF8"));

      try {
        out.write(lang.toString());
        out.close();
        System.out.println(filename + " abgespeichert im Ordner");
        System.out.println(System.getProperty("user.dir"));
      } catch (IOException e) {

        e.printStackTrace();
      }
    } catch (UnsupportedEncodingException e) {

      e.printStackTrace();
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    }

  }
}