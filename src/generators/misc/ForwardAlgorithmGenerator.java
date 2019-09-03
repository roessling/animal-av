package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.MatrixPrimitive;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class ForwardAlgorithmGenerator implements Generator {
  // Shall we attempt to fix some of the AnimalScript Bugs?
  public static boolean         BUGFIX                  = false;

  private Language              lang;

  // some special elements/primitives in the animation
  private Text                  header;
  private Primitive             lastPrimitive;
  private List<MatrixPrimitive> hideAllMatrices_BUG;

  // some often used properties in the animation
  private TextProperties        headerProperties        = new TextProperties();
  private TextProperties        variableLabelProperties = new TextProperties();
  private SourceCodeProperties  paragraphProperties     = new SourceCodeProperties();
  private MatrixProperties      matrixProperties        = new MatrixProperties();
  private DecimalFormat         matrixDecimalFormat     = new DecimalFormat(
                                                            "0.###");
  private SourceCodeProperties  codeProps               = new SourceCodeProperties();

  /*
   * ============================================================================
   * ================= Constants for an Animal Generator
   * ========================
   * =====================================================================
   */
  // private static final String NAME =
  // "Forward-Algorithmus (Hidden Markov Models) [DE]";

  // private static final String ALGORITHM_NAME = "Forward-Algorithmus";

  // private static final String AUTHORS =
  // "Ji-Ung Lee, Daniel Lehmann <yd43awuw@rbg.informatik.tu-darmstadt.de>";

  private static final String   DESCRIPTION             = "Mit dem Forward-Algorithmus können einige Fragestellungen zu "
                                                            + "einem gegebenen Hidden Markov Model (HMM) beantwortet werden. "
                                                            + "Im Gegensatz zu anderen Fragestellungen bei HMMs sind hier die "
                                                            + "Parameter A (Transitionsmatrix), B (Emmissionsmatrix) und Pi "
                                                            + "(Anfangsverteilung) der HMM bekannt.\n"
                                                            + "Nun ist eine Folge von Beobachtungen O gegeben und es soll "
                                                            + "berechnet werden wie wahrscheinlich es ist, dass diese HMM die "
                                                            + "Folge O erzeugt hat, also P(O|HMM).\n"
                                                            + "Der Forward-Algorithmus macht sich dabei die Markov-Eigenschaft "
                                                            + "von HMMs zu Nutze und löst diese Frage mit Dynamischer "
                                                            + "Programmierung in O(T*N^2), T = Länge der Observationenfolge, N "
                                                            + "= Anzahl der Zustände der HMM.\n"
                                                            + "Angewendet wird der Algorithmus z.B. bei mehreren "
                                                            + "konkurrierenden Modellen. Er kann vergleichbar machen, welches "
                                                            + "davon am besten zu den beobachteten Daten passt.";

  private static final String   SOURCE_CODE             = "public static double forward(double[][] A, double[][] B, double[] Pi,\n"
                                                            + "		int[] O) {\n"
                                                            + "	// Variable creation\n"
                                                            + "	int N = A.length;\n"
                                                            + "	int T = O.length;\n"
                                                            + "	double[][] alpha = new double[T][N];\n"
                                                            + "\n"
                                                            + "	// Initialization of alpha for first t\n"
                                                            + "	int bIndex = O[0]-1;\n"
                                                            + "	for (int i = 0; i < N; i++)\n"
                                                            + "		alpha[0][i] = Pi[i] * B[i][bIndex];\n"
                                                            + "\n"
                                                            + "	// Iterate for all other t's\n"
                                                            + "	for (int t = 1; t < T; t++) {\n"
                                                            + "		for (int k = 0; k < N; k++) {\n"
                                                            + "			for (int i = 0; i < N; i++) {\n"
                                                            + "				alpha[t][k] += alpha[t - 1][i] * A[i][k];\n"
                                                            + "			}\n"
                                                            + "			alpha[t][k] *= B[k][O[t]-1];\n"
                                                            + "		}\n"
                                                            + "	}\n"
                                                            + "\n"
                                                            + "	// Sum all alphas of last step\n"
                                                            + "	double result = 0;\n"
                                                            + "	for (int i = 0; i < N; i++)\n"
                                                            + "		result += alpha[T - 1][i];\n"
                                                            + "	return result;\n"
                                                            + "}";

  /*
   * ============================================================================
   * ================= Constructor
   * ==============================================
   * ===============================================
   */
  public ForwardAlgorithmGenerator() {
    init();

    // set some default properties
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    paragraphProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 13));
    variableLabelProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(Font.MONOSPACED, Font.PLAIN, 20));
    matrixProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
  }

  /*
   * ============================================================================
   * ================= Helper mehtods for the animation
   * ==========================
   * ===================================================================
   */
  private void setHeader(String text) {
    if (header == null) {
      header = lang.newText(new Coordinates(20, 20), text, "header", null,
          headerProperties);
      // new slide, so lastPrimitive is the header
      lastPrimitive = header;
    } else {
      // create new step
      nextSlide();
      header.setText(text, null, null);
    }
    // add label
    lang.addLine("label \"" + text + "\"");
  }

  private Primitive newParagraph(String text, Node placement) {
    SourceCode paragraph = lang.newSourceCode(placement, "", null,
        paragraphProperties);

    // Break into lines
    StringTokenizer words = new StringTokenizer(text);
    StringBuilder line = new StringBuilder();
    while (words.hasMoreTokens()) {
      line.append(words.nextToken()).append(" ");
      // check if words is now empty -> have to output the last line
      if (line.length() > 90 || !words.hasMoreTokens()) {
        paragraph.addCodeLine(line.toString().trim(), "", 0, null);
        line = new StringBuilder();
      }
    }
    // remember last added paragraph for easy adding of elements below
    return lastPrimitive = paragraph;
  }

  /**
   * Convenience method that creates a paragraph below the last one.
   */
  private Primitive newParagraph(String text) {
    return newParagraph(text, new Offset(0,
        (lastPrimitive == header) ? 0 : -10, lastPrimitive,
        AnimalScript.DIRECTION_SW));
  }

  /**
   * Convenience method for clearing all from the animation except for the
   * header.
   */
  private void nextSlide() {
    lang.nextStep();
    lang.hideAllPrimitives();
    // Bug in HideAll, does not hide matrices! Has to be done explicitly!
    if (BUGFIX)
      for (Primitive p : hideAllMatrices_BUG)
        p.hide();
    header.show();
    // the last added primitive is now the header
    lastPrimitive = header;
  }

  /**
   * Convenience methods for showing a small matrix with its variable name to
   * the left.
   */
  private StringMatrix newMatrixWithLabel(String label, Number[][] data,
      Node placement) {
    // string matrix with whitespace entries so stretch initial matrix because
    // of refresh bug
    String[][] stretchMatrix = new String[data.length][];
    for (int row = 0; row < data.length; row++) {
      stretchMatrix[row] = new String[data[row].length];
      for (int col = 0; col < data[row].length; col++) {
        stretchMatrix[row][col] = "     ";
      }
    }
    // label and matrix
    Text matrixLabel = lang.newText(placement, label, "", null,
        variableLabelProperties);
    StringMatrix matrix = lang.newStringMatrix(new Offset(10, 0, matrixLabel,
        AnimalScript.DIRECTION_NE), stretchMatrix, "", null, matrixProperties);

    // convert number matrix to string matrix
    String[][] dataAsString = new String[data.length][];
    for (int row = 0; row < data.length; row++) {
      dataAsString[row] = new String[data[row].length];
      for (int col = 0; col < data[row].length; col++) {
        dataAsString[row][col] = matrixDecimalFormat.format(data[row][col]
            .doubleValue());
        matrix.put(row, col, dataAsString[row][col], null, null);
      }
    }
    hideAllMatrices_BUG.add(matrix);
    return matrix;
  }

  private StringMatrix newMatrixWithLabelAndArrow(String label, Number[][] data) {
    PolylineProperties lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    Polyline arrow = lang.newPolyline(new Node[] {
        new Offset(280, 200, header, AnimalScript.DIRECTION_SW),
        new Offset(340, 200, header, AnimalScript.DIRECTION_SW) }, "", null,
        lineProps);

    return newMatrixWithLabel(label, data, new Offset(10, -50, arrow,
        AnimalScript.DIRECTION_NE));
  }

  /*
   * ============================================================================
   * ================= The actual parts of the animation: 1. HMM Introduction 2.
   * Forward Introduction 3. Forward Calculation 4. Forward Conclusion
   * ==========
   * ==================================================================
   * =================
   */

  /**
   * Shows a generic introduction to Hidden Markov Models, because their
   * understanding is crucial to the understanding of the forward algorithm.
   * Feel free to use this on other algorithms concering HMMs, such as the
   * backward algorithm oder the Viterbi algorithm.
   */
  protected void hmmIntroduction() {
    setHeader("Hintergrund: Hidden Markov Models");
    newParagraph("Falls dir Hidden Markov Model bereits ein Begriff ist, kannst du gerne zum "
        + "nächsten Schritt gehen...");
    newParagraph("Ein Hidden Markov Model basiert auf einer sogenannten Markov Kette erster "
        + "Ordnung. Diese ist ganz ähnlich zu einem Zustandsautomaten: Man hat eine Menge "
        + "von Zuständen, von denen immer einer aktiv ist und mittels Transitionen in "
        + "einen anderen Zustand gewechselt wird.");

    lang.addLine("graph \"markovChain\" size 3 directed weighted nodes {\"s1\" (0,0), \"s2\" "
        + "(100,100), \"s3\" (200,0)} edges {(0,1,\"\"),(2,0,\"\"),(1,2,\"\")} origin "
        + "offset (50,150) from \"header\" SW");
    lang.nextStep();

    // have to use this, as Java API does not support weighted graphs with
    // strings as edges
    lang.addLine("graph \"markovChain1\" size 3 directed weighted nodes {\"s1\" (0,0), \"s2\" "
        + "(100,100), \"s3\" (200,0)} edges {(0,1,\"a_1,2 = 0.3\"),(2,0,\"a_3,1 = 0.2\"),"
        + "(1,2,\"a_2,3 = 0.5\")} origin offset (50,150) from \"header\" SW");
    newParagraph(
        "Im Gegensatz zum Zustandsautomaten passieren diese Übergänge aber "
            + "stochastisch, d.h. nur mit einer bestimmten Wahrscheinlichkeit. Diese "
            + "Übergangswahrscheinlichkeiten a_i,k von Zustand i nach k kann man in eine "
            + "Matrix A eintragen.", new Offset(0, 300, header,
            AnimalScript.DIRECTION_SW));
    newParagraph("Da es sich um Wahrscheinlichkeiten handelt, muss die Summe "
        + "pro Zustand =1 sein. Falls sie <1 ist, dann ist a_i,i (Wahrscheinlichkeit in i "
        + "zu bleiben) gerade der Rest.");

    MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("Asymm");
    q1.setPrompt("Muss die Transitionsmatrix A symmetrisch sein?");
    q1.addAnswer(
        "Ja",
        0,
        "Eine symmetrische Matrix heißt, dass von Zustand i nach j "
            + "genauso wahrscheinlich gewechselt wird wie zurück. Das ist aber nicht notwendig. "
            + "Deswegen leider falsch =(");
    q1.addAnswer("Nein", 1,
        "Wunderbar! A darf zwar symmetrisch sein, muss aber natürlich nicht.");
    lang.addMCQuestion(q1);

    lang.nextStep();
    newMatrixWithLabelAndArrow("A=", new Double[][] { { 0.7, 0.3, 0.0 },
        { 0.0, 0.5, 0.5 }, { 0.2, 0.0, 0.8 } });

    MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("Asum");
    q2.setPrompt("Die Einträge von A müssen...");
    q2.addAnswer(
        "in jeder Zeile in der Summe = 1 sein.",
        1,
        "Korrekt. In einem Schritt muss "
            + "insgesamt mit Sicherheit ein Übergang stattfinden. Das schließt aber nicht aus, "
            + "dass man im Zustand i bleibt (a_i,i > 0).");
    q2.addAnswer(
        "in jeder Spalte in der Summe = 1 sein.",
        0,
        "Falsch. Es kann durchaus sein, "
            + "dass die Zustände unterschiedlich wahrscheinlich erreicht werden (unabhängig "
            + "vom Vorgänger).");
    lang.addMCQuestion(q2);

    nextSlide();

    newParagraph("Bisher ist das _Hidden_ Markov Model aber noch 'sichtbar'. 'Hidden' wird "
        + "es, weil wir eigentlich die Zustandsübergänge und insbesondere den aktuellen "
        + "Zustand gar nicht sehen können. Stattdessen 'strahlen' die Zustände etwas ab. "
        + "Erst dieses 'Abstrahlen' kann man dann beobachten.");
    newParagraph("Diese Beobachtungen v_i treten aber nicht immer auf, sondern nur mit einer "
        + "gewissen Wahrscheinlichkeit. "
        + "Die sog. Emmissionswahrscheinlichkeit b_i(v_k) gibt dann an mit welcher "
        + "Wahrscheinlichkeit im Zustand s_i die Beobachtung v_k gemacht wird.");
    lang.addLine("graph \"hmm\" size 5 directed weighted nodes {\"s1\" (0,0), \"s2\" "
        + "(100,100), \"s3\" (200,0), \"v1\" (0, 250), \"v2\" (200,250)} edges {(0,1,\"\"),"
        + "(2,0,\"\"),(1,2,\"\"),(0,3,\"b_1(v_1)=1\"),(1,3,\"b_2(v_1)=0.6\"),"
        + "(1,4,\"b_2(v_2)=0.4\"),(2,4,\"b_3(v_2)=1\")} origin offset (50,150) from "
        + "\"header\" SW");
    lang.nextStep();

    newParagraph("Auch dies lässt sich in einer Matrix B darstellen.",
        new Offset(0, 450, header, AnimalScript.DIRECTION_NW));
    newMatrixWithLabelAndArrow("B=", new Double[][] { { 1.0, 0.0 },
        { 0.6, 0.4 }, { 0.0, 1.0 } });

    nextSlide();

    lang.addLine("show \"markovChain\"");
    newParagraph("Bisher sind wir immer davon ausgegangen, dass in s1 gestartet wird. "
        + "Möglicherweise wird aber auch in s2 etc. gestartet. π_i gibt deswegen die "
        + "Wahrscheinlichkeit an, dass in Zustand s_i gestartet wird.");
    newParagraph("Die Summe der Einträge in der Tabelle π muss (wie bei A) gleich 1 sein.");
    newParagraph(
        "Mit den Matrizen A, B und π ist das Hidden Markov Model (HMM) eindeutig nun "
            + "festgelegt. Jetzt kann man verschiedene Fragestellungen für ein HMM beantworten. "
            + "Der gezeigte Forward Algorithmus beantwortet die folgende Frage:",
        new Offset(0, 320, header, AnimalScript.DIRECTION_NW));
    newParagraph("Angenommen man hat die Folge von Beobachtungen O = {o1, o2, ..., oT | oi ∈ V} gemacht.");
    newParagraph("Wie wahrscheinlich ist es, dass meine HMM diese Folge O wirklich erzeugt hat?");
    newParagraph("Also quasi: Passt mein Modell zu den Daten? Oder als Wahrscheinlichkeit: Berechne P(O | HMM).");
    newMatrixWithLabelAndArrow("π=",
        new Double[][] { { 0.7 }, { 0.1 }, { 0.2 } });

    MultipleChoiceQuestionModel q3 = new MultipleChoiceQuestionModel("HMMdesc");
    q3.setPrompt("Eine HMM ist durch A, B und Pi eindeutig. Wieso muss man die Menge der "
        + "Zustände S nicht angeben?");
    q3.addAnswer(
        "Diese steckt schon implizit in A.",
        10,
        "Auch Richtig. A gibt ja die "
            + "Übergänge zwischen den Zuständen an und damit deren Anzahl (Höhe/Breite von A).");
    q3.addAnswer(
        "Diese steckt schon implizit in B.",
        10,
        "Auch Richtig. B ordnet jedem "
            + "Zustand ja Emmissionen zu und gibt damit diese auch an (Anzahl = Höhe von B).");
    lang.addMCQuestion(q3);
  }

  /**
   * Shows a brief theoretical (math formulas/probabilites) of why some of the
   * assumptions of the forward algorithm are safe to be made with HMMs and how
   * you can reduce the complexity of a naive approach (O(N^T)) down to
   * O(T*N^2).
   */
  protected void forwardIntroduction() {
    setHeader("Forward-Algorithmus: Theorie, Erklärung");
    newParagraph("Um P(O | HMM) zu berechnen, kann man sich erst einmal fragen wie es zu den "
        + "Beobachtungen O kam. Diese entstehen ja beim Durchlaufen durch die Zustände der "
        + "HMM. Solch einen 'Weg' durch die HMM, d.h. eine Zustandsfolge nennen wir "
        + "Q={q1, q2, ..., qT | qi ∈ S}. Wie wir sehen hat diese genau die Länge der "
        + "Beobachtungsfolge O, nämlich T. Macht auch Sinn, schließlich kann pro Zustand "
        + "nur eine Beobachtung gemacht werden.");
    newParagraph("Angenommen wir haben uns für eine Folge Q entschieden, dann ist die "
        + "Wahrscheinlichkeit in ihr die Beobachtungsfolge O zu sehen genau P(O | Q, HMM) "
        + "= b_q1(o1) * b_q2(o2) * ... * b_qT(oT). Jetzt wissen wir aber nicht ob wir "
        + "wirklich dieses Q gegangen sind und müssen deshalb alle möglichen Wege "
        + "ausprobieren und für jede Zustandsfolge berechnen wie wahrscheinlich die "
        + "Beobachtung war.");
    newParagraph("Es gilt also: P(O | HMM) = Summe für alle Q: P(O | Q, HMM) "
        + "* P(Q | HMM)");
    newParagraph("Den Faktor P(Q | HMM) brauchen wir, weil ja nicht jeder Weg gleich "
        + "wahrscheinlich ist. Das kann man ausrechnen zu: P(Q | HMM) = p_q1 * a_q1,q2 * "
        + "a_q2,q3 * ... * q_q(T-1),q(T)");
    newParagraph("Naiv ist dies eine extrem große Summe, da es ja N^T (N Anzahl der Zustände) "
        + "viele Zustandsfolgen gibt. Da es aber eine Markov Kette erster Ordnung ist, "
        + "hängt die nächste Beobachtung o(t+1) nicht von allen vorherigen Zuständen ab, "
        + "sondern nur vom aktuellen Zustand qt. Welchen Weg man bis qt genommen hat ist "
        + "unerheblich.");
    newParagraph("Im Schritt t brauche ich also nur die Wahrscheinlichkeiten dafür, dass ich "
        + "im letzten Schritt in s1, s2, ... sN gelandet bin.");

    lang.nextStep();

    FillInBlanksQuestionModel q = new FillInBlanksQuestionModel("PO");
    q.setPrompt("Gegeben eine nicht-zyklische (d.h. es gibt keine Schleifen im Zustandsgraph) "
        + "HMM mit N Zuständen. Sei O eine Beobachtungsfolge der Länge T = N + 1. Was ist P(O|HMM)?");
    q.addAnswer("0", 2,
        "Genau! Schließlich ist die längste mögliche Folge nur N lang.");
    lang.addFIBQuestion(q);

    nextSlide();
    newParagraph("Diese Wahrscheinlichkeiten nennt man Forward Variable und lautet formal:");
    newParagraph("a_t(i) = P(o1, o2, ..., ot, qt = si | HMM)");
    newParagraph("Und informal: Die Wahrscheinlichkeit nach t Schritten (d.h. ich habe o1 "
        + "bis ot gesehen) im Zustand qi zu landen.");
    newParagraph("Da es T Schritte gibt und N Zustände, sind dies nur noch N*T Möglichkeiten "
        + "für die a! Für t=T wird a_t(i) dann zu aT(i) = P(O, qT = si | HMM) und wenn ich "
        + "jetzt a_T(i) für alle i ausrechne, dann ist (wegen der totalen Wahrscheinlichkeit):");
    newParagraph("Summe für i=1...N = a_T(1) + a_T(2) + ... + a_T(N) = P(O, qT = s1 | HMM) "
        + "+ P(O, qT = s2 | HMM) + ... + P(O, qT = sN | HMM) = P(O | HMM)");
    newParagraph("Also genau die Wahrscheinlichkeit, die wir berechnen wollten!");
    newParagraph("Bleibt nur noch die Frage wie die a berechnet werden. Dazu geht man "
        + "induktiv für t=1...T vor: a_1(i) = p_i * b_i(o1) a_(t+1)(k) = (Summe von "
        + "i=1...N: a_(t)(i) * a_i,k ) * b_k(o(t+1))");
  }

  /**
   * Shows the last slide of the animation which gives a conclusion and some
   * other facts (runtime, applications) of the forward algorithm.
   */
  protected void forwardConclusion() {
    setHeader("Forward-Algorithmus: Ende");
    newParagraph("Geschafft! Zum Abschluss noch einmal ein paar Daten zum Forward-Algorithmus:");
    newParagraph("Der Speicherbedarf liegt in O(N*T). Dies ist leicht ersichtlich, weil ja die "
        + "berechnete Tabelle der Alphas die Abmessungen N*T hat. Sofern man nicht mit "
        + "dieser Tabelle noch weiter rechnen möchte (Für manche weitere Anwendungen wie "
        + "den Viterbi-Algorithmus macht das Sinn), braucht man eigentlich nur jeweils "
        + "die letzte Spalte der Tabelle speichern und benötigt dann sogar nur N Werte.");
    newParagraph("Die Anzahl der Additionen liegt in O(N^2*T). Gleiches gilt für die "
        + "Multiplikationen. Auch das ist gut an der Anweisung in der innersten Schleife zu "
        + "erkennen. Diese Anweisung enthält eine Addition und eine Multiplikation. "
        + "Diese Anweisung ist eingeschlossen von zwei Schleifen, die je N mal ausgeführt "
        + "werden und der äußersten Schleife, die T-1 mal ausgeführt wird.");
    newParagraph("Neben der Beantwortung der hier vorgestellten Frage, lassen die Alphas auch "
        + "auf andere Antworten schließen, zum Beispiel die Fragen in welchem Zustand sich "
        + "die HMM in der Zukunft befinden wird.");
  }

  /**
   * Calculates with the forward algorithm the probability, that the HMM defined
   * by A, B and Pi has generated the observation sequence O.
   * 
   * @param A
   *          The transition matrix. Gives the probabilities a_i,j that state
   *          s_i moves on to state s_j.
   * @param B
   *          The observation matrix. Gives the probabilities b_i(v_j) that
   *          state s_i emits observation v_j.
   * @param Pi
   *          The initial distribution. Gives the probabilities pi_i that state
   *          s_i is the first state to begin with.
   * @param O
   *          The sequence of observations.
   */
  protected double forward(Double[][] A, Double[][] B, Double[] Pi, Integer[] O) {
    setHeader("Forward-Algorithmus: Auswertung");

    /*
     * Step 1: Show beginning conditions: source code, input matrices
     */
    Primitive desc1 = newParagraph("Angenommen A und B seien als 2-dimensionale double "
        + "Arrays, Pi als double "
        + "Array und O als Vektor von Integers, wobei ein Wert in O die Nummer der "
        + "Beobachtung angibt (beginnend bei 1).");
    Primitive desc2 = newParagraph("Seien A, B, Pi und O wie untenstehend gewählt und die "
        + "Methode forward aufgerufen.");

    // add the source code
    SourceCode code = lang.newSourceCode(new Offset(0, 250, header,
        AnimalScript.DIRECTION_SW), "", null, codeProps);
    for (String line : SOURCE_CODE.split("\n")) {
      int indentation = line.length() - line.replace("\t", "").length();
      code.addCodeLine(line.trim(), "", indentation, null);
    }

    // add the input matrices
    StringMatrix aMatrix = newMatrixWithLabel("A=", A, new Offset(0, 100,
        header, AnimalScript.DIRECTION_SW));
    StringMatrix bMatrix = newMatrixWithLabel("B=", B, new Offset(30, 0,
        aMatrix, AnimalScript.DIRECTION_NE));
    Double[][] PiAsMatrix = new Double[Pi.length][1];
    for (int i = 0; i < Pi.length; i++)
      PiAsMatrix[i][0] = Pi[i];
    StringMatrix piMatrix = newMatrixWithLabel("Pi=", PiAsMatrix, new Offset(
        30, 0, bMatrix, AnimalScript.DIRECTION_NE));
    StringMatrix oMatrix = newMatrixWithLabel("O=", new Integer[][] { O },
        new Offset(30, 0, piMatrix, AnimalScript.DIRECTION_NE));

    lang.nextStep();

    MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("Ovalues");
    q1.setPrompt("Ist " + A.length + " ein gültiger Wert in O?");
    q1.addAnswer(
        "Ja",
        1,
        "Richtig. Die Beobachtungen werden (wie in der Mathematik, nicht in "
            + "der Informatik) von 1 an nummeriert. Damit gibt es maximal T mögliche Beobachtung "
            + "und insbesondere die T-te ist auch eine Möglichkeit.");
    q1.addAnswer(
        "Nein",
        0,
        "Falsch. Ungewohnt, aber wegen der Mathematik üblich: Die "
            + "Beobachtungen werden von 1 an durchnummeriert. Damit bekommt die T-te "
            + "Beobachtung den Index T, nicht T-1!");
    lang.addMCQuestion(q1);

    /*
     * Step 2: Initialize algorithm: N, T, Alpha
     */
    lang.nextStep();
    desc1.hide();
    desc2.hide();
    lastPrimitive = header;
    Primitive desc3 = newParagraph("Zunächst werden die lokalen Variablen initialisiert. Dazu gehören N und T "
        + "(als Abkürzung) und die Tabelle mit den Alphas. Diese ist gerade N*T groß.");

    int N = A.length;
    lang.newText(new Offset(-150, 30, code, AnimalScript.DIRECTION_NE), "N="
        + N, "", null, variableLabelProperties);
    int T = O.length;
    lang.newText(new Offset(-150, 60, code, AnimalScript.DIRECTION_NE), "T="
        + T, "", null, variableLabelProperties);
    Double[][] alpha = new Double[T][N];
    for (int row = 0; row < alpha.length; row++) {
      for (int col = 0; col < alpha[row].length; col++) {
        alpha[row][col] = new Double(0.0);
      }
    }
    StringMatrix alphaMatrix = newMatrixWithLabel("alpha=", alpha, new Offset(
        -150, 100, code, AnimalScript.DIRECTION_NE));
    code.highlight(2);
    code.highlight(3);
    code.highlight(4);
    code.highlight(5);

    // Task 3.3: add counter
    TwoValueCounter c = lang.newCounter(alphaMatrix);
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    lang.newCounterView(c, new Offset(0, 30, alphaMatrix,
        AnimalScript.DIRECTION_SW), cp, true, true);

    /*
     * Step 3: Fill Alpha for t=0
     */
    lang.nextStep();
    desc3.hide();
    lastPrimitive = header;
    Primitive desc4 = newParagraph("Jetzt wird für jeden State das Alpha für das erste t "
        + "ausgerechnet. Achtung: Bei Java fängt die Indizierung bei 0 an!");
    Primitive desc5 = null;
    code.unhighlight(2);
    code.unhighlight(3);
    code.unhighlight(4);
    code.unhighlight(5);
    code.highlight(7);

    lang.nextStep();
    desc5 = newParagraph("Dabei wird zuerst auf O zugegriffen, um den Index für B zu "
        + "ermitteln und dann der Wert aus B mit dem aus Pi multipliziert.");
    code.toggleHighlight(7, 8);
    oMatrix.highlightCell(0, 0, null, null);

    lang.nextStep();
    code.toggleHighlight(8, 9);
    for (int i = 0; i < N; i++) {
      lang.nextStep();
      code.toggleHighlight(9, 10);
      bMatrix.highlightCell(i, O[0] - 1, null, null);
      piMatrix.highlightCell(i, 0, null, null);

      lang.nextStep();
      alpha[0][i] = Pi[i] * B[i][O[0] - 1];
      alphaMatrix
          .put(0, i, matrixDecimalFormat.format(alpha[0][i]), null, null);
      alphaMatrix.highlightCell(0, i, null, null);

      lang.nextStep();
      code.toggleHighlight(10, 9);
      bMatrix.unhighlightCell(i, O[0] - 1, null, null);
      piMatrix.unhighlightCell(i, 0, null, null);
      alphaMatrix.unhighlightCell(0, i, null, null);
    }

    /*
     * Step 4: Calculate Alpha for all other ts
     */
    lang.nextStep();
    oMatrix.unhighlightCell(0, 0, null, null);
    code.toggleHighlight(9, 12);
    desc4.hide();
    desc5.hide();
    lastPrimitive = header;
    Primitive desc6 = newParagraph("Jetzt werden die Alphas für alle Zustände in den restlichen ts berechnet.");

    lang.nextStep();
    code.toggleHighlight(12, 13);
    for (int t = 1; t < T; t++) {
      lang.nextStep();
      code.toggleHighlight(13, 14);
      for (int k = 0; k < N; k++) {
        lang.nextStep();
        code.toggleHighlight(14, 15);
        for (int i = 0; i < N; i++) {
          lang.nextStep();
          code.toggleHighlight(15, 16);
          alphaMatrix.highlightCell(t - 1, i, null, null);
          alphaMatrix.getElement(t - 1, i); // add those gets for the counter
          aMatrix.highlightCell(i, k, null, null);

          lang.nextStep();
          alpha[t][k] += alpha[t - 1][i] * A[i][k];
          alphaMatrix.getElement(t, k); // add those gets for the counter
          alphaMatrix.put(t, k, matrixDecimalFormat.format(alpha[t][k]), null,
              null);
          alphaMatrix.highlightCell(t, k, null, null);

          lang.nextStep();
          alphaMatrix.unhighlightCell(t - 1, i, null, null);
          aMatrix.unhighlightCell(i, k, null, null);
          alphaMatrix.unhighlightCell(t, k, null, null);
          code.toggleHighlight(16, 15);
        }
        lang.nextStep();
        code.toggleHighlight(15, 18);
        oMatrix.highlightCell(0, t, null, null);
        bMatrix.highlightCell(k, O[t] - 1, null, null);

        lang.nextStep();
        alpha[t][k] *= B[k][O[t] - 1];
        alphaMatrix.getElement(t, k); // add those gets for the counter
        alphaMatrix.put(t, k, matrixDecimalFormat.format(alpha[t][k]), null,
            null);
        alphaMatrix.highlightCell(t, k, null, null);

        lang.nextStep();
        oMatrix.unhighlightCell(0, t, null, null);
        bMatrix.unhighlightCell(k, O[t] - 1, null, null);
        alphaMatrix.unhighlightCell(t, k, null, null);
        code.toggleHighlight(18, 14);
      }
      lang.nextStep();
      code.toggleHighlight(14, 13);
    }

    /*
     * Step 5: Sum last alphas for result
     */
    lang.nextStep();
    code.toggleHighlight(13, 22);
    desc6.hide();
    lastPrimitive = header;
    newParagraph("Zum Schluss müssen noch die Werte von Alpha für das letzte t aufsummiert werden.");

    MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel(
        "ResultNec");
    q2.setPrompt("Welche Daten brauchen wir nun zur Berechnung von result?");
    q2.addAnswer("Die gesamte Alpha-Matrix.", 0,
        "Falsch. Man braucht nur die letzte Zeile.");
    q2.addAnswer("Die letzte Spalte der Alpha-Matrix.", 0,
        "Falsch. Man braucht nur die letzte Zeile.");
    q2.addAnswer("B und O.", 0,
        "Falsch. Man braucht nur die letzte Zeile von Alpha.");
    q2.addAnswer("Die letzte Zeile der Alpha-Matrix.", 1, "Richtig.");
    lang.addMCQuestion(q2);

    lang.nextStep();
    code.toggleHighlight(22, 23);
    double result = 0;
    Text resultVar = lang.newText(new Offset(-80, 100, alphaMatrix,
        AnimalScript.DIRECTION_SW),
        "result=" + matrixDecimalFormat.format(result), "", null,
        variableLabelProperties);

    lang.nextStep();
    code.toggleHighlight(23, 24);
    for (int i = 0; i < N; i++) {
      lang.nextStep();
      code.toggleHighlight(24, 25);
      alphaMatrix.highlightCell(T - 1, i, null, null);
      alphaMatrix.getElement(T - 1, i); // add those gets for the counter

      lang.nextStep();
      result += alpha[T - 1][i];
      resultVar.setText("result=" + matrixDecimalFormat.format(result), null,
          null);
      resultVar.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null,
          null);

      lang.nextStep();
      code.toggleHighlight(25, 24);
      alphaMatrix.unhighlightCell(T - 1, i, null, null);
      resultVar.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null,
          null);
    }

    lang.nextStep();
    code.toggleHighlight(24, 26);
    return result;
  }

  /*
   * ============================================================================
   * ================= The methods for implementing a Generator
   * ==================
   * ===========================================================================
   */
  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // get user input as String[] (double[] not supported...)
    String[][] AAsString = (String[][]) primitives.get("A");
    String[][] BAsString = (String[][]) primitives.get("B");
    String[] PiAsString = (String[]) primitives.get("Pi");
    int[] OAsInt = (int[]) primitives.get("O");

    // for testing the props aren't set (because there's no documentation for
    // the container...)
    if (props == null || props.isEmpty()) {
      codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.MONOSPACED, Font.PLAIN, 11));
      codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    } else {
      codeProps = (SourceCodeProperties) props.getPropertiesByName("codeProps");
    }

    // convert string matrix to number matrix
    Double[][] A = new Double[AAsString.length][];
    for (int row = 0; row < AAsString.length; row++) {
      A[row] = new Double[AAsString[row].length];
      for (int col = 0; col < AAsString[row].length; col++) {
        A[row][col] = Double.valueOf(AAsString[row][col]);
      }
    }
    Double[][] B = new Double[BAsString.length][];
    for (int row = 0; row < BAsString.length; row++) {
      B[row] = new Double[BAsString[row].length];
      for (int col = 0; col < BAsString[row].length; col++) {
        B[row][col] = Double.valueOf(BAsString[row][col]);
      }
    }
    Double[] Pi = new Double[PiAsString.length];
    for (int row = 0; row < PiAsString.length; row++) {
      Pi[row] = Double.valueOf(PiAsString[row]);
    }
    Integer[] O = new Integer[OAsInt.length];
    for (int row = 0; row < OAsInt.length; row++) {
      O[row] = OAsInt[row];
    }

    hmmIntroduction();
    forwardIntroduction();
    forward(A, B, Pi, O);
    forwardConclusion();

    lang.finalizeGeneration();

    String code = lang.toString();
    // Remove all refreshes on grids, they destroy the layout
    if (BUGFIX)
      code = code.replaceAll("refresh", "");
    return code;
  }

  @Override
  public String getName() {
    return "Forward-Algorithmus (Hidden Markov Models)";
  }

  @Override
  public String getAlgorithmName() {
    return "Forward-Algorithmus";
  }

  @Override
  public String getAnimationAuthor() {
    return "Ji-Ung Lee, Daniel Lehmann";
  }

  @Override
  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION.replace("ä", "&auml;").replace("ö", "&ouml;")
        .replace("ü", "&uuml;");
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Forward-Algorithmus (Hidden Markov Models)",
        "Ji-Ung Lee, Daniel Lehmann", 800, 1000);
    // Set: Each pair of subsequent steps has to be divdided by a call of
    // lang.nextStep();
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // reset fields
    header = null;
    lastPrimitive = null;
    hideAllMatrices_BUG = new LinkedList<MatrixPrimitive>();
  }

  /*
   * ============================================================================
   * ================= Main method for simple testing
   * ============================
   * =================================================================
   */
  public static void main(String[] args) {
    String[][] A = new String[][] { { "0.3", "0.7" }, { "0.5", "0.5" } };
    String[][] B = new String[][] { { "0.4", "0.6" }, { "0.1", "0.9" } };
    String[] Pi = new String[] { "0.8", "0.2" };
    int[] O = new int[] { 1, 2, 2, 1 };

    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    primitives.put("A", A);
    primitives.put("B", B);
    primitives.put("Pi", Pi);
    primitives.put("O", O);

    ForwardAlgorithmGenerator s = new ForwardAlgorithmGenerator();
    String code = s.generate(null, primitives);

    // // File output
    // String asuPath =
    // "D:\\Dateien\\Studium\\Praktikum Algorithmen Visualisierung\\Übung 5\\Forward.asu";
    // try {
    // FileWriter fw = new FileWriter(asuPath);
    // fw.write(code);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }

    // Console output
    System.out.println(code);
  }
}
