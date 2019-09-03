package generators.maths.faddejewleverrier;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class FaddejewLeverrier extends MatrixOperations implements
    ValidatingGenerator {

  private Language             lang;
  // private boolean showExplanation;
  private SourceCodeProperties sourceCodeProperties;
  private ArrayProperties      arrayProperties;
  // private boolean showTraceComputation;
  private int[][]              quadratische_Matrix_A;
  private TextProperties       textProperties;
  private TextProperties       titelProperties;
  // private boolean showSkalarMatrixMultiplikation;
  private MatrixProperties     matrixProperties;
  // private ArrayMarkerProperties arrayMarkerProperites;
  private SourceCode           sc;
  private DoubleMatrix         A;
  // private DoubleMatrix I;
  private int                  n;
  private DoubleMatrix         Bnext;
  private DoubleMatrix         B;
  private DoubleMatrix         AB;
  private DoubleMatrix         Ainv;
  private DoubleMatrix         cI;
  private DoubleArray          c;

  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    int[][] a = (int[][]) arg1.get("quadratische_Matrix_A");
    for (int[] r : a)
      if (r.length != a.length)
        throw new IllegalArgumentException(
            "Error: Der Algorithmus braucht eine quadratische Matrix, die Anzahl der Zeilen und der Spalten der Eingabematrix müssen gleich sein!");
    return true;
  }

  public void init() {
    lang = new AnimalScript("Faddejew-Leverrier Algorithmus[DE]",
        "Moritz Zysk", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    arrayProperties = (ArrayProperties) props
        .getPropertiesByName("arrayProperties");
    quadratische_Matrix_A = (int[][]) primitives.get("quadratische_Matrix_A");
    textProperties = (TextProperties) props
        .getPropertiesByName("textProperties");
    titelProperties = (TextProperties) props
        .getPropertiesByName("titelProperties");
    matrixProperties = (MatrixProperties) props
        .getPropertiesByName("matrixProperties");
    // System.out.println(Arrays.toString(matrixProperties.toString()));
//    arrayMarkerProperites = (ArrayMarkerProperties) props
//        .getPropertiesByName("arrayMarkerProperites");
    introduction();
    s0();

    close();
    System.out.println(lang.toString());
    lang.finalizeGeneration();
    return lang.toString();
  }

  private Text title;
  private int  width  = 100;
  private int  height = 100;

  private void s0() {
    n = quadratische_Matrix_A.length;
    setN(n);
    TextProperties titleProperties = titelProperties;
    title = lang.newText(new Coordinates(10, 10),
        "Faddejew-Leverrier-Algorithmus", "title", null, titleProperties);
    sc = lang.newSourceCode(new Offset(0, 10, title, null), "sc", null,
        sourceCodeProperties);
    addCodeLines();
    lang.nextStep("Erste Zeile");
    s1();
  }

  private void s1() {

    sc.highlight(0);
    A = lang.newDoubleMatrix(new Offset(width, height, sc, "NE"),
        convert(quadratische_Matrix_A), "A", null, matrixProperties);
    mkCounter(A, "SW");
    // lang.newRect(new Offset(0, 0, A, "NW"), new Offset(0, 0, A, "SE"),
    // "rect", null);

    System.out.println(A.getProperties().toString());
    lang.newText(new Offset(0, -20, A, "NW"), "A:", "tA", null, textProperties);
    lang.nextStep();
    sc.unhighlight(0);
    s2();
  }

  private void s2() {
    sc.highlight(1);
    double dB[][] = new double[n][n];
    B = lang.newDoubleMatrix(new Offset(width, 0, A, "NE"), dB, "B", null,
        matrixProperties);
    mkCounter(B, "NE");
    lang.newText(new Offset(0, -20, B, "NW"), "B:", "tB", null, textProperties);
    lang.nextStep();
    sc.unhighlight(1);
    s3();
  }

  private void s3() {
    sc.highlight(2);
    double[] dc = new double[n + 1];
    dc[n] = 1.0;
    c = lang.newDoubleArray(new Offset(width, -10, sc, "NE"), dc, "c", null,
        arrayProperties);
    mkCounter(c, "SW");
    lang.newText(new Offset(-20, 0, c, "NW"), "c:", "tc", null, textProperties);
    c.highlightCell(n, null, null);
    lang.nextStep();
    sc.unhighlight(2);
    s4();
  }

  private void s4() {
    sc.highlight(3);
    double[][] dAB = mult(A, B);
    AB = lang.newDoubleMatrix(new Offset(0, height, A, "SW"), dAB, "AB", null,
        matrixProperties);
    mkCounter(AB, "SW");
    lang.newText(new Offset(0, -20, AB, "NW"), "AB:", "tAB", null,
        textProperties);
    lang.nextStep();
    sc.unhighlight(3);
    s5();
  }

  private int  k = 0;
  private Text tk;

  private void s5() {
    sc.highlight(4);
    k++;
    if (k == 1) {
      tk = lang.newText(new Offset(width / 2, 0, title, "NE"), "k = " + k,
          "tk", null, textProperties);
      lang.nextStep("for <- k = " + k);
      sc.unhighlight(4);
      s6();
    } else if (k <= n) {
      tk.hide();
      tk = lang.newText(new Offset(width / 2, 0, title, "NE"), "k = " + k,
          "tk", null, textProperties);
      lang.nextStep("for <- k = " + k);
      sc.unhighlight(4);
      s6();
    } else {
      tk.hide();
      tk = lang.newText(new Offset(width / 2, 0, title, "NE"), "k = " + k
          + "> n = " + n, "tk", null, textProperties);
      lang.nextStep("for <- k = " + k);
      sc.unhighlight(4);
      s12();
    }

  }

  // private Text tci;

  private void s6() {
    if (k != 1) {
      // cI.hide();
      // tci.hide();
    }
    sc.highlight(5);
    double[][] dcI = mult(c.getData(n - k + 1), mkI());
    // folgender Code fuehrt leder zum verschwinden der Matrix
    // cI = lang.newDoubleMatrix(new Offset(width,0,AB,"NE"), dcI, "cI",
    // null,matrixProperties);
    if (cI == null)
      cI = lang.newDoubleMatrix(new Offset(0, height, B, "SW"), dcI, "cI",
          null, matrixProperties);
    else
      set(cI, dcI);
    mkCounter(cI, "NE");
    c.highlightElem(n - k + 1, null, null);
    c.unhighlightElem(n - k + 1, new MsTiming(200), null);
    lang.newText(new Offset(0, -20, cI, "NW"), "cI:", "tcI", null,
        textProperties);
    lang.nextStep();
    //
    sc.unhighlight(5);
    s7();
  }

  private void s7() {

    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel("multi");
    mcq.setPrompt("Als Trace oder Spur bezeichnet man bei einer Matrix:");
    mcq.addAnswer(
        "die Summe der Zeilen- und Spaltenzahl. Eine Matrix mit m Zeilen und n Spalten ist eine m X n (sprich: m mal n - oder m trace n-Matrix",
        1,
        "Als Trace bezeichnet man die Summe aller Elemente a11, a22, _, ann der Diagonalen");
    mcq.addAnswer(
        "die Summe aller Elemente a11, a22, ___, ann der Diagonalen",
        2,
        "Als Trace bezeichnet man die Summe aller Elemente a11, a22, _, ann der Diagonalen");
    lang.addMCQuestion(mcq);
    sc.highlight(6);
    set(B, add(AB, cI));
    lang.nextStep();
    sc.unhighlight(6);
    s8();
  }

  private void s8() {

    FillInBlanksQuestionModel trace = new FillInBlanksQuestionModel(
        "compute trace");
    trace.setPrompt("Was ist die Spur von der Matrix A?");
    // String[] answer = new String[A.getNrCols()];
    trace.addAnswer(trace(A) + "", 1,
        "Die Spur der Matrix wird durch die Summe von a11,a22,___,ann und lautet:"
            + trace(A));
    lang.addFIBQuestion(trace);

    sc.highlight(7);
    set(AB, mult(A, B));
    lang.nextStep();
    sc.unhighlight(7);

    s9();
  }

  private Text trace;
  double       t;

  private void s9() {

    if (k != 1)
      trace.hide();
    sc.highlight(8);
    t = trace(AB);
    trace = lang.newText(new Offset(width, 0, c, "NE"), "trace = " + t, "tcI",
        null, textProperties);
    lang.nextStep();
    sc.unhighlight(8);
    s10();
  }

  private void s10() {
    sc.highlight(9);
    double d = -1.0 / k * t;
    c.put(n - k, d, null, null);
    c.highlightCell(n - k, null, null);
    lang.nextStep();
    sc.unhighlight(9);
    s11();
  }

  private void s11() {
    sc.highlight(10);
    lang.nextStep();
    sc.unhighlight(10);
    s5();
  }

  private void s12() {
    sc.highlight(11);
    set(AB, mult(A, B));
    lang.nextStep();
    sc.unhighlight(11);
    s13();
  }

  private void s13() {
    sc.highlight(12);
    set(cI, mult(c.getData(0), mkI()));
    lang.nextStep();
    sc.unhighlight(12);
    s14();
  }

  private void s14() {
    sc.highlight(13);
    Bnext = lang.newDoubleMatrix(new Offset(0, height, cI, "SW"), add(AB, cI),
        "Bnext", null, matrixProperties);
    mkCounter(Bnext, "NE");
    lang.newText(new Offset(0, -20, Bnext, "NW"), "B[n+1]:", "tBnext", null,
        textProperties);
    lang.nextStep();
    sc.unhighlight(13);
    s15();
  }

  private void s15() {
    sc.highlight(14);
    lang.nextStep("Test ob der Algorithmus ordnungsgemäß terminiert");
    sc.unhighlight(14);
    if (notZero(Bnext))
      s16();
    else
      s17();
  }

  private void s16() {
    sc.highlight(15);
    TextProperties errProperties = new TextProperties();
    errProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 36));
    errProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    errProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newText(new Coordinates(200, 200),
        "FEHLER: Algorithmus terminiert nicht korrekt!", "err", null,
        errProperties);
    lang.nextStep();
    sc.unhighlight(15);
    // s17();
  }

  private void s17() {
    sc.highlight(16);
    lang.nextStep("Inverse Matrix");
    sc.unhighlight(16);
    if (c.getData(0) != 0)
      s18();
    else
      s19();
  }

  private void s18() {
    sc.highlight(17);
    double[][] d = mult(-1.0 / c.getData(0), getData(B));
    Ainv = lang.newDoubleMatrix(new Offset(0, height, AB, "SW"),
        new double[n][n], "Ainv", null, matrixProperties);
    mkCounter(Ainv, "SW");
    lang.newText(new Offset(0, -20, Ainv, "NW"), "Ainv:", "tAinv", null,
        textProperties);
    set(Ainv, d);
    lang.nextStep();
    sc.unhighlight(17);
    s21();
  }

  private void s19() {
    sc.highlight(18);
    lang.nextStep();
    sc.unhighlight(18);
    s20();
  }

  private Text tAinv = null;

  private void s20() {
    sc.highlight(19);
    tAinv = lang.newText(new Offset(0, height, sc, "SW"),
        "Die Eingabematrix ist singulaer!", "tAinv", null, textProperties);
    lang.nextStep();
    sc.unhighlight(19);
    s21();
  }

  private void s21() {
    sc.highlight(20);

    FillInBlanksQuestionModel determinante = new FillInBlanksQuestionModel(
        "compute det");
    determinante
        .setPrompt("Was ist die Detrminante der Matrix \n 30 20 \n 10 40");
    // String[] answer = new String[A.getNrCols()];
    determinante.addAnswer(1000 + "", 1, "a11*a22-a12*a21");
    lang.addFIBQuestion(determinante);

    String s1 = "det(A) = " + det(c.getData(0));
    Offset o;
    if (tAinv == null)
      o = new Offset(0, height, sc, "SW");
    else
      o = new Offset(0, height, tAinv, "SW");
    // Text ts1 =
    lang.newText(o, s1, "ts1", null, textProperties);
    lang.nextStep("Determinante");
    sc.unhighlight(20);
  }

  private void addCodeLines() {
    sc.addCodeLine(" 0: A = quadratische_Matrix_A;", null, 0, null);
    sc.addCodeLine(" 1: B = 0;", null, 0, null);
    sc.addCodeLine(" 2: C[n] = 1;", null, 0, null);
    sc.addCodeLine(" 3: AB = A * B;", null, 0, null);
    sc.addCodeLine(" 4: for(k = 1; k<= n; k++){", null, 0, null);
    sc.addCodeLine(" 5:     cI = c[n-k+1] * I", null, 0, null);
    sc.addCodeLine(" 6:     B[k] = AB + cI", null, 0, null);
    sc.addCodeLine(" 7:     AB = A * B", null, 0, null);
    sc.addCodeLine(" 8:     trace = computeTrace(AB);", null, 0, null);
    sc.addCodeLine(" 9:     C[n-k] = -1/k * t;", null, 0, null);
    sc.addCodeLine("10: }", null, 0, null);
    sc.addCodeLine("11: AB = A * B;", null, 0, null);
    sc.addCodeLine("12: cI = c[0] * I;", null, 0, null);
    sc.addCodeLine("13: B[n+1] = AB + cI;", null, 0, null);
    sc.addCodeLine("14: if( B[n+1] <> 0)", null, 0, null);
    sc.addCodeLine(
        "15:     printf('Fehler: Algorithmus terminiert nicht korrekt!');",
        null, 0, null);
    sc.addCodeLine("16: if( c[0] <> 0)", null, 0, null);
    sc.addCodeLine("17:     Ainv = -1/c[0] * B[n];", null, 0, null);
    sc.addCodeLine("18: else", null, 0, null);
    sc.addCodeLine("19:     printf('Die Eingabematrix ist singulaer!');", null,
        0, null);
    sc.addCodeLine("20: det(A) = -1/c[0] * B;", null, 0, null);
  }

  private void mkCounter(DoubleArray A, String richtung) {
    TwoValueCounter counterT = lang.newCounter(A);
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    // TwoValueView viewT;
    if (richtung == "NE")
      lang.newCounterView(counterT, new Offset(0, -30, A, richtung), cp, true,
          true);
    else
      lang.newCounterView(counterT, new Offset(0, 0, A, richtung), cp, true,
          true);
  }

  private void mkCounter(DoubleMatrix A, String richtung) {
    TwoValueCounter counterT = lang.newCounter(A);
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    // TwoValueView viewT;
    if (richtung == "NE")
      lang.newCounterView(counterT, new Offset(0, -30, A, richtung), cp, true,
          true);
    else
      lang.newCounterView(counterT, new Offset(0, 0, A, richtung), cp, true,
          true);
  }

  // public static void main(String[] args){
  // FaddejewLeverrier fl = new FaddejewLeverrier();
  // fl.init();
  // fl.showExplanation = true;
  // fl.showTraceComputation = true;
  // fl.showSkalarMatrixMultiplikation = true;
  //
  // fl.quadratische_Matrix_A = new int[][]{{3 , 1 , 5},{3, 3, 1},{4, 6, 4}};
  //
  // fl.sourceCodeProperties = new SourceCodeProperties();
  // fl.sourceCodeProperties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
  // Color.BLUE);
  // fl.sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new
  // Font("SansSerif",
  // Font.PLAIN, 16));
  // fl.sourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
  // Color.RED);
  // fl.sourceCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
  // Color.BLACK);
  // fl.matrixProperties = new MatrixProperties();
  // fl.matrixProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
  // Color.WHITE);
  // fl.matrixProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
  // fl.explanationTextProperties=new TextProperties();
  // fl.explanationTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new
  // Font("SansSerif",
  // Font.BOLD, 16));
  // fl.arrayProperties = new ArrayProperties();
  // fl.arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
  // fl.arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
  // fl.arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
  // Color.YELLOW);
  // fl.arrayProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
  // Color.GREEN);
  //
  // fl.s0();
  // System.out.println(fl.lang.toString());
  // }

  public String getName() {
    return "Faddejew-Leverrier Algorithmus[DE]";
  }

  public String getAlgorithmName() {
    return "Faddejew-Leverrier Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Moritz Zysk";
  }

  public String getDescription() {
    return "Der Algorithmus berechnet f&uuml;r beliebige quadratische Matrizen die Koeffizienten des charakteristischen Polynoms."
        + "\n"
        + "Weiterhin erh&auml;lt man die Determinante und f&uuml;r regul&auml;re Eingabematrizen die Inverse der Matrix. ";
  }

  public String getCodeExample() {
    return "/* Eingabe: quadratische Matrix A der Dimension n */" + "\n" + "\n"
        + "B[0] = 0;" + "\n" + "C[n] = 1;" + "\n" + "\n"
        + "for (k = 1; k <= n; k++)" + "\n" + "{" + "\n"
        + "  B[k]      =   A * B[k-1] + c[n-k+1] * I;" + "\n"
        + "  C[n-k]  = - 1/k * trace( A * B[k]); " + "\n" + "}" + "\n" + "\n"
        + "B[n+1] = A * B[n] + c[0] * I;" + "\n" + "\n" + "if (B[n+1] <> 0)"
        + "\n" + "{" + "\n"
        + "  printf(\"Fehler: Algorithmus terminiert nicht korrekt!\");" + "\n"
        + "}" + "\n" + "\n" + "if(c[0] <> 0)" + "\n" + "{" + "\n"
        + "  Ainv = -1/c[0] * B[n];" + "\n" + "} else {" + "\n"
        + "  printf(\"Die Ausgabematrix ist singul&auml;r!\");" + "\n" + "}"
        + "\n" + "\n" + "\n" + "\n" + "/*" + "\n"
        + "  Quelle: de.wikipedia.org/wiki/Algorithmus_von_Faddejew-Leverrier"
        + "\n" + "*/";
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

  private void introduction() {
    String richtung = "SW";
    // titelProperties.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    Text t0 = lang.newText(new Coordinates(10, 10),
        "Faddejew-Leverrier-Algorithmus", "title", null, titelProperties);
    // titelProperties.set(AnimationPropertiesKeys.BOLD_PROPERTY, false);
    Text t1 = lang.newText(new Offset(5, 5, t0, richtung), "Einleitung:",
        "title", null, titelProperties);
    Text t2 = lang.newText(new Offset(5, 5, t1, richtung), "Zweck:", "title",
        null, titelProperties);
    Text t3 = lang
        .newText(
            new Offset(5, 5, t2, richtung),
            "Der Faddejew-Leverrier Algorithmus berechnet das  charakteristische Polynom, die Determinante und fuer regulaere Matrizzen das Inverse der Eingabematrix.",
            "title", null, titelProperties);
    Text t4 = lang.newText(new Offset(0, 5, t3, richtung),
        "Der Algorithmus lässt sich effizient parallelisieren.", "title", null,
        titelProperties);
    Text t5 = lang.newText(new Offset(-5, 5, t4, richtung), "Alternativen:",
        "title", null, titelProperties);
    Text t6 = lang
        .newText(
            new Offset(5, 5, t5, richtung),
            "Fuer die reine Determinantenberechnung ist das Gauss-Elimination Verfahren zu empfehlen. Eine entsprechende Animation befindet sich bereits in Animal.",
            "title", null, titelProperties);
    Text t7 = lang
        .newText(
            new Offset(0, 5, t6, richtung),
            "Das Gauss-Eleminationsverfahren hat eine bessere Komplexitaetsklasse, doch ergibt es einen erhoehten technischen Aufwand ",
            "title", null, titelProperties);
    Text t8 = lang
        .newText(
            new Offset(0, 5, t7, richtung),
            "bei der Berechnung des der Koeffizienten des characteristischen Polynoms im Vergleich zu dem FaddejewLeverrier Algorithmus.",
            "title", null, titelProperties);
    Text t9 = lang
        .newText(
            new Offset(0, 5, t8, richtung),
            "Eine andere interessante Alternative ist der  Algorithmus von Samuelson-Berkowitz, da dieser ohne Division auskommt",
            "title", null, titelProperties);
    Text t10 = lang
        .newText(
            new Offset(0, 5, t9, richtung),
            "Quelle: http://de.wikipedia.org/wiki/Algorithmus_von_Faddejew-Leverrier",
            "title", null, titelProperties);
    lang.nextStep();
    t0.hide();
    t1.hide();
    t2.hide();
    t3.hide();
    t4.hide();
    t5.hide();
    t6.hide();
    t7.hide();
    t8.hide();
    t9.hide();
    t10.hide();
  }

  private void close() {
    A.hide();
    B.hide();
    AB.hide();
    cI.hide();
    if (Bnext != null)
      Bnext.hide();
    if (Ainv != null)
      Ainv.hide();
    lang.nextStep();

    lang.hideAllPrimitives();
    String richtung = "SW";
    // titelProperties.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    Text t0 = lang.newText(new Coordinates(10, 10),
        "Faddejew-Leverrier-Algorithmus", "title", null, titelProperties);
    // titelProperties.set(AnimationPropertiesKeys.BOLD_PROPERTY, false);
    Text t1 = lang.newText(new Offset(5, 5, t0, richtung),
        "Abschlussbemerkung:", "title", null, titelProperties);
    Text t2 = lang.newText(new Offset(5, 5, t1, richtung),
        "Der Algorithmus hat eine Komplexitaet von O(n^4).", "title", null,
        titelProperties);
    Text t3 = lang
        .newText(
            new Offset(5, 5, t2, richtung),
            "Im c Array befinden sich die Koeffizienten des characteristischen Polynoms. ",
            "title", null, titelProperties);
    Text t4 = lang.newText(new Offset(-5, 5, t3, richtung),
        "Der Algorithmus lässt sich effizient parallelisieren.", "title", null,
        titelProperties);
    lang.newText(
        new Offset(5, 5, t4, richtung),
        "Zudem wurde die Determinante berechnet und versucht das Inverse von A zu kriegen.",
        "title", null, titelProperties);
  }
}
