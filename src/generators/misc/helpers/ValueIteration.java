package generators.misc.helpers;

import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class ValueIteration {
  // Some constants
  private static List<CleanActions> clean                         = new LinkedList<CleanActions>();
  private static final int          NORTH                         = 0,
      EAST = 1, SOUTH = 2, WEST = 3;
  private static final int          NUMBER_OF_ACTIONS             = 4;
  private static final double       P_TRANSITION_TO_DESIRED_STATE = 0.7;
  private static final double       P_TRANSITION_OTHER_N_STATE    = (1 - P_TRANSITION_TO_DESIRED_STATE) / 3;
  private static final String       pCodeName                     = "listSource";
  private static final String       graphName                     = "graphMDP";
  private int                       sizeX                         = 2;
  private int                       size                          = sizeX * 2;
  private int                       graphDisBetweenNodes          = 100;
  private double                    discountFactor                = 0.9;

  // Important animal objects
  private Language                  lang;
  Graph                             mdpGraph;
  SourceCode                        sc;
  DoubleMatrix                      mdpMatrix;
  Variables                         animalVars;
  Graph[]                           graphToNodes;
  // All animal prop objects
  private GraphProperties           graphProp                     = new GraphProperties();
  private GraphProperties           nGraphProp                    = new GraphProperties();
  private MatrixProperties          valueFunctionGridProp         = new MatrixProperties();
  private SourceCodeProperties      pCodeProp                     = new SourceCodeProperties();
  private TextProperties            infoTextsProp                 = new TextProperties();
  private TextProperties            headlineTextProp              = new TextProperties();
  private TextProperties            headlineVarsTextProp          = new TextProperties();
  private boolean                   askQuestions                  = true;

  // All printed text
  private String[]                  pCode                         = {
      "PARAMTER states, actions, valueFunction",
      "RETURN valueFuntion",
      "CONSTANTS DiscountFactor",
      "DO",
      "   SET change TO 0",
      "   FORALL states BEGIN",
      "      SET neighborStates TO getNeighborStates(state)",
      "      FORALL possible actions BEGIN",
      "         SET alternativeValue TO 0",
      "         FORALL neighborStates BEGIN",
      "            INCREMENT alternativeValue BY",
      "                           transitionProbability(state, action, neighborState) * valueFunction[neighborState]",
      "         END",
      "         SET alternativeValue TO alternativeValue * DiscountFactor",
      "         IF alternativeValue> valueFunction[neighborState] BEGIN",
      "            SET change TO max(change, alternativeValue - valueFunction[state])",
      "            SET valueFunction[state] TO alternativeValue",
      "         END", "      END", "   END", "WHILE change > delta" };
  private String[]                  beginDescription              = {
      "Ist ein Verfahren, welches beim Bestärkenden Lernen (engl. Reinforcement learning) verwendet wird. ",
      "Value Iteration arbeitet dabei auf dem Markow-Entscheidungsproblem (engl. Markov decision process). ",
      "Die Value Iteration findet für jeden Zustand die maximale Belohnung, die im Markow-Entscheidungsproblem für diesen Zustand erreichbar ist.",
      "",
      "Das Markow-Entscheidungsproblem ist hier definiert als ein Tupel(S,A,P,R). ",
      "Dabei ist: ",
      "   S die Menge der Zustände,",
      "   A die Menge der Aktionen, ",
      "   P die Wahrscheinlichkeit, dass bei der Ausführung der Aktion a im Zustand s in den Zustand s_neu gewechselt wird, und",
      "   R die Belohnungsfunktion, welche einem Zustand s eine Belohnung r zuordnet.",
      "Für mehr Informationen zum Markow-Entscheidungsproblem siehe:",
      "en.wikipedia.org/wiki/Markov_decision_process. "          };

  // pcodeBeX explains some pCode lines
  private String[]                  pCodeBe1                      = {
      "Value Iteration Funktionsdefinition  ",
      "stats ist eine Datenstruktur mit allen Zustanden",
      "actions ist eine Datenstruktur mit allen Aktionen",
      "valueFunction ist eine Datenstruktur mit den Initialwerten der Value Funktion" };
  private String[]                  pCodeBe2                      = { "Der Diskontierungsfaktor gibt an, wie stark zukünftige Belohnungen gewichtet werden. " };
  private String[]                  pCodeBe3                      = {
      "Die Do-While-Schleife wiederholt eine Iteration der Value-Iteration (Aktualisierung der Value Funktion für alle Zustände)  ",
      "Die Do-While-Schleife iteriert dabei, bis das Konvergenzkriterium erfüllt ist.",
      "Das Konvergenzkriterium besteht darin, dass die maximale Value Funktionswertsaktualisierung kleiner als ein Delta ist." };
  private String[]                  pCodeBe4                      = {
      "Da der Zustandsübergang nicht deterministisch ist, müssen für jede Aktion   ",
      "alle Nachbarn des aktuellen Zustandes abhängig von der Übergangswahrscheinlichkeit berücksichtig werden.",
      "Hier wird die Value Funktionswertsaktualisierung für die aktuelle Aktion berechnet." };
  private String[]                  pCodeBe5                      = { "Wurde ein besserer Value Funktionswert gefunden? Ja -> update." };
  private String[]                  txtBeforExample               = {
      "Value Iteration anhand eines Beispiels: ",
      "Die Value Iteration wird hier für ein Markow-Entscheidungsproblem, das eine nicht deterministische Rasterwelt darstellt, animiert.  ",
      "Es gibt die Aktionen Wechsel nach Norden, Süden, Osten und Westen in den nächsten Zustand.",
      "Wenn dies nicht möglich ist, wird der Zustand nicht gewechselt.",
      "Beim Ausfuehren der Aktion: wechsle den Zustand nach Norden, wird mit der Wahrscheinlichkeit 0.7 nach Norden gewechselt",
      "und mit der Wahrscheinlichkeit 0.1 nach Osten, Westen oder Sueden. (Der Zustandsübergang ist nicht deterministisch)",
      "Dasselbe gilt für die anderen Aktionen. ",
      "Einem Zustand wird hier die Belohnung 1 zugeordnet, allen anderen Zuständen die Belohnung 0.  " };

  // Text that is printed after the algo
  private String[]                  endText                       = {
      "Nachdem jetzt für alle Zustande die Value Funktion berechnet wurde, kann die beste Aktion ausgewählt werden.",
      "Die beste Aktion ist die Aktion, die in den Nachbarzustand mit dem grössten Value Funktionswert wechselt.  ",
      "  ",
      "Mehr Informationen zur Value Iteration wie auch zum Bestärkendem-Lernen sind in den Vorlesungsunterlagen des Machine Learning Kurses vom Stanford zu finden.  ",
      "Die URL ist http://cs229.stanford.edu/notes/cs229-notes12.pdf. Die Unterlagen sind in English." };

  // All aniaml text fields
  Text                              txtChange;
  Text                              txtAction;
  Text                              txtState;
  Text                              txtNS;
  Text                              txtAV;
  Text                              txtTransProb;
  Text                              txtIfCalc;
  @SuppressWarnings("unused")
  private Text                      txtDoW;
  Text                              txtTmpUpCalc;
  private Text                      txtHeadline;
  private Text                      txtVars;

  private int getSizeX() {
    return sizeX;
  }

  private int getSizeY() {
    return size / sizeX;
  }

  private double[] valueFunction;

  // works only if all Strings betweens "\n" have at least one ' '
  // not tested!!!
  @SuppressWarnings("unused")
  private static List<String> breakLongString(String str, int maxSize) {
    List<String> splitStrAfterLineBreak = new LinkedList<String>();
    int subStrStart = 0;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == ' ') {
        splitStrAfterLineBreak.add(str.substring(subStrStart, i));
        subStrStart = i + 1;
      }
    }
    if (splitStrAfterLineBreak.size() == 0) {
      splitStrAfterLineBreak.add(str);
    }

    int pos = maxSize;
    subStrStart = 0;
    List<String> res2 = new LinkedList<String>();
    for (int listIdx = 0; listIdx < splitStrAfterLineBreak.size(); listIdx++) {
      String curString = splitStrAfterLineBreak.get(listIdx);
      while (pos + maxSize < curString.length()) {
        for (int i = pos;; i--) {
          if (curString.charAt(i) == ' ') {
            res2.add(curString.substring(subStrStart, i));
            subStrStart = i + 1;
            pos = i + maxSize;
            break;
          }

        }

      }
      res2.add(curString.substring(subStrStart));
    }
    return res2;

  }

  public ValueIteration(Language l) {
    lang = l;
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    valueFunction = new double[size];
    valueFunction[size - 1] = 1;

    // set properties
    setDefaultProps();
  }

  private void setDefaultProps() {
    graphProp.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    graphProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    graphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.green);
    graphProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.yellow);
    graphProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    nGraphProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    nGraphProp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.green);
    nGraphProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    nGraphProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    valueFunctionGridProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.white);
    valueFunctionGridProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.red);
    valueFunctionGridProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.red);
    valueFunctionGridProp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY,
        MatrixProperties.styleOptions.get(1));
    headlineTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 14));
    infoTextsProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 12));
    headlineVarsTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 13));

    pCodeProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    pCodeProp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.blue);
  }

  /**
   * method to set properties of the animation
   * 
   * @param vF
   * @param sizeX
   * @param disFactor
   */
  public void setProperties(double[] vF, int sizeX, double disFactor) {
    valueFunction = vF;
    this.sizeX = sizeX;
    discountFactor = disFactor;
    valueFunction = new double[size];
  }

  public void setProperties(int[][] vF, double disFactor) {
    valueFunction = new double[vF.length * vF[0].length];
    int idx = 0;
    for (int[] row : vF)
      for (int el : row)
        valueFunction[idx++] = el;
    this.sizeX = vF[0].length;
    this.size = vF.length * vF[0].length;
    discountFactor = disFactor;
  }

  public String animalScriptOutputHack() {
    // some hacks to fix some issues after generation the animal script
    // from the animal api
    // there is may be a way to do that with the api as well but i
    // didn't manage to do it just with the api
    return lang
        .toString()
        .replaceAll("refresh", "")
        // the "refresh" after the setGridValue ... changed the cell
        // size in a odd way
        .replaceFirst(
            "lines " + this.getSizeY() + " columns " + this.getSizeX(),
            "lines " + this.getSizeY() + " columns " + this.getSizeX()
                + " style matrix cellWidth 40 cellHeight 40 ");

    // i didn't find a way to define cellWidth and Heights with the
    // properties object
  }

  public static void main(String[] args) {
    Language l = new AnimalScript("Value Iteration", "M. Viering", 640, 640);
    ValueIteration vi = new ValueIteration(l);

    vi.animalQuestionWhile();
    // vi.valueIteration();

    try {
      // animal script becomes easy to long for the consol
      // =>
      // save it in a file
      FileWriter fstream = new FileWriter("VI_Auf3.asu");
      BufferedWriter out = new BufferedWriter(fstream);

      out.write(vi.animalScriptOutputHack());
      out.close();
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  /**
   * Value iteration works on valueFunction Actions are n,s,w,e + methods calls
   * for animal
   */
  public void valueIteration() {
    animalInitQuestions();
    // progState is used to define cleaning actions. (unhighlight nodes etc)
    animalInitVI(); // progState = 0
    double change = 0;
    int countIterations = 0;
    do {
      change = 0;
      animalVIbeforeAllStates(); // progState = 1
      for (int state = 0; state < valueFunction.length; state++) {

        animalVIallStates(state); // progState = 2

        List<Integer> neighborStates = getNeighborStates(state);
        animalVIgetNeighbors(state); // progState = 3
        if (askQuestions)
          animalQuestionWillVfChange(state);
        boolean firstCallAction = true;
        for (int action = 0; action < NUMBER_OF_ACTIONS; action++) {
          // progState = 4
          if (isStateChangeOk(state, action)) {
            double tmp = 0;

            animalVIAction(state, action, firstCallAction); // progState
            // = 5
            firstCallAction = false;
            boolean firstCallNS = true;
            for (int neighborStateIdx = 0; neighborStateIdx < neighborStates
                .size(); neighborStateIdx++) {
              if (askQuestions)
                animalQuestionStateActionChange(state, action,
                    neighborStates.get(neighborStateIdx));
              // progState = 6
              double oldTmp = tmp;
              tmp += getTransitionProbability(state, action,
                  neighborStates.get(neighborStateIdx))
                  * valueFunction[neighborStates.get(neighborStateIdx)];
              animalVICalcOneStep(
                  state,
                  action,
                  neighborStates.get(neighborStateIdx),
                  getTransitionProbability(state, action,
                      neighborStates.get(neighborStateIdx)),
                  valueFunction[neighborStates.get(neighborStateIdx)], tmp,
                  oldTmp, firstCallNS);
              firstCallNS = false;
            }
            tmp *= discountFactor;
            // animalVIAddDisFactor(tmp);
            animalVIIfUpdate(tmp, valueFunction[state]); // progState
            // = 7
            if (tmp > valueFunction[state]) {
              aniamVIInIfUpdate(state, tmp, valueFunction[state], change,
                  Math.max(change, tmp - valueFunction[state]));
              // progState = 8
              change = Math.max(change, tmp - valueFunction[state]);
              valueFunction[state] = tmp;
            }
          }
        }

      }
      // progState = 9
      animalAfterAllStates();
      countIterations++;
      if (askQuestions)
        animalQuestionWhile();
    } while (change > 0.1 && countIterations < 5);
    // progState = 10
    animalVIEnd();
    if (askQuestions)
      animalQuestionEndAnimation();
    lang.finalizeGeneration();
  }

  private void animalQuestionEndAnimation() {
    lang.nextStep();
    MultipleSelectionQuestionModel msqm = new MultipleSelectionQuestionModel(
        "discountQ");
    msqm.setPrompt("Bei der Value Iteration muss der Diskontierungsfaktor (DiscountFactor) zwischen 0 und  1 liegen. Was passiert wenn der Diskontierungsfaktor (DiscountFactor) größer als 1 ist?");
    msqm.addAnswer(
        "Es hat keine Konsequenzen. Es wurde einfach nur in dieser Art und Weise Definiert. ",
        -1, "falsch");
    msqm.addAnswer(
        "Die Value Iteration konvergiert unter Umständen nicht mehr.", 1,
        "richtig");
    msqm.addAnswer(
        "Man gewichtet Belohnungen die man erst in 2 oder mehr Schritten erreichen kann stärker als Belohnungen die man bereits im nächsten Schritt erhalten kann.",
        1, "richtig");
    msqm.setGroupID("discountQG");
    lang.addMSQuestion(msqm);
  }

  private int willVfChangeCount = 0;

  private void animalQuestionWillVfChange(int state) {
    if (willVfChangeCount == 0) {
      List<Integer> nsIdx = getNeighborStates(state);
      boolean willChange = false;
      for (int idx : nsIdx) {
        if (valueFunction[state] == 0 && valueFunction[idx] > 0) {
          willChange = true;
        }

      }
      if (willChange) {

        lang.nextStep();
        willVfChangeCount++;
        TrueFalseQuestionModel tfqm = new TrueFalseQuestionModel("willchangeQ",
            true, 2);
        tfqm.setFeedbackForAnswer(true, "Die Antword ist richtig");
        tfqm.setFeedbackForAnswer(
            false,
            "Die Antword ist falsch. Schau dir noch mal die Nachbarzustände des Zustandes S"
                + state
                + " an und überleg warum der Value Funktionswert erhöht wird, bevor du dir die nächsten Schritte animieren lässt.");
        tfqm.setGroupID("willChangTQG");
        tfqm.setPrompt("Wird der Value Funktionswert des Zustandes S" + state
            + " erhöht werden?");
        lang.addTFQuestion(tfqm);

      }

    }

  }

  private int stateActionNStateCount = 0;

  private void animalQuestionStateActionChange(int state, int action, int ns) {
    if (stateActionNStateCount < 2) {
      lang.nextStep();
      MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
          "qTransProb" + stateActionNStateCount);
      mcqm.setPrompt("Was ist die Wahrscheinlichkeit von Zustand S" + state
          + " bei der Aktion gehe nach " + actionIntToString(action)
          + " in den Zustand S" + ns + " zu wechseln?");
      // System.out.println((getTransitionProbability(state, action,
      // ns)));

      if (getTransitionProbability(state, action, ns) == 0.7) {
        mcqm.addAnswer("Die Wahrscheinlichkeit ist 0,7", 1,
            "Die Antword ist richtig");
        mcqm.addAnswer("Die Wahrscheinlichkeit ist 0,1", 0,
            "Die Antword ist falsch, die richtige Antword ist 0,7");
        mcqm.addAnswer("Die Wahrscheinlichkeit ist 1", 0,
            "falsch, die richtige Antword ist 0,7");
      } else {
        mcqm.addAnswer("Die Wahrscheinlichkeit ist 1", 0,
            "falsch, die richtige Antword ist 0,1");
        mcqm.addAnswer("Die Wahrscheinlichkeit ist 0,7", 0,
            "Die Antword ist falsch, die richtige Antword ist 0,1");
        mcqm.addAnswer("Die Wahrscheinlichkeit ist 0,1", 1,
            "Die Antword ist richtig");
      }

      mcqm.setID("transProbQ" + stateActionNStateCount);
      mcqm.setGroupID("transProbQ");
      lang.addMCQuestion(mcqm);
    }
    stateActionNStateCount++;

  }

  private int doWhileCounter = 1;

  private void animalQuestionWhile() {
    // lang.nextStep();
    // TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
    // "trueFalseQuestion", true, 5);
    // tfq.setPrompt("Is MoodleConnect cool?");
    // tfq.setGroupID("Second question group");
    // lang.addTFQuestion(tfq);
    //
    if (doWhileCounter == 1) {
      lang.nextStep();
      TrueFalseQuestionModel tfqm = new TrueFalseQuestionModel("dwQ1", false, 1);
      tfqm.setPrompt("Wenn change > delta gilt, MUSS es bei der nächsten Iteration der Value Funktion noch Änderung am Value Funktionswert geben?");
      tfqm.setGroupID("dwQ1g");

      tfqm.setFeedbackForAnswer(false, "richtig");
      tfqm.setFeedbackForAnswer(
          true,
          "Wenn bereits der exacte Value Funktionswert gefunden wurde gibt es in der nächsten Iteration keine Änderung mehr.");
      lang.addTFQuestion(tfqm);

    }
    if (doWhileCounter == 2) {
      lang.nextStep();
      TrueFalseQuestionModel tfqm = new TrueFalseQuestionModel("dwQ2", false, 1);
      tfqm.setPrompt("Wenn change > delta nicht gilt ist dann der Value Funktionswert gefunden? Oder anders gefragt findet diese Value Iteration immer den exakten Value Funktionswert (keine Approximation)?");
      tfqm.setGroupID("dwQ2g");
      tfqm.setFeedbackForAnswer(
          true,
          "Die hier dargestellte Value Iteration finde nur eine Approximation des Value Funktionswertes. Es kann aber bewiesen werden das wenn die Value Iteration gegen den Value Funktionswert konvergiert. ");
      tfqm.setFeedbackForAnswer(false, "richtig");
      lang.addTFQuestion(tfqm);

    }
    //

    doWhileCounter++;
  }

  private void animalInitQuestions() {

  }

  /**
   * all animation after the algo
   */
  private void animalVIEnd() {
    lang.nextStep();
    sc.hide();
    txtChange.setText("", null, null);
    txtVars.setText("", null, null);

    createSC(new Offset(0, 10, txtHeadline, "SW"), this.endText);
    for (int s = 0; s < valueFunction.length; s++) {
      int bestA = 0;
      double maxVf = Double.MIN_VALUE;
      for (int a = 0; a < 4; a++) {
        if (isStateChangeOk(s, a)) {
          if (maxVf < valueFunction[s + stateChange(a)]) {
            bestA = a;
            maxVf = valueFunction[s + stateChange(a)];
          }
        }
        if (maxVf > valueFunction[s]) {
          mdpGraph.highlightEdge(s, s + stateChange(bestA), null, null);
        }
      }

    }
  }

  private void animalAfterAllStates() {
    lang.nextStep();
    executeAllCleanActions(9);

  }

  private void animalVIallStates(int state) {
    // progState = 2
    lang.nextStep();
    animalVars.set("state", "" + state);
    // txtState.show();
    txtState.setText("state: s" + state, null, null);
    executeAllCleanActions(2);
    // clear last step
    graphToNodes[state].show();
    // mdpGraph.highlightNode(state, null, null);
    mdpMatrix.highlightElem(convertStateIdxTo2DArrayIdx(state).y,
        convertStateIdxTo2DArrayIdx(state).x, null, null);
    sc.highlight(5, 0, true);
    sc.highlight(19, 0, true);
    clean.add(new CleanActions() {

      @Override
      public void run() {
        txtState.setText("", null, null);
        sc.unhighlight(5, 0, true);
        sc.unhighlight(19, 0, true);
      }

      @Override
      public boolean now(int progState) {
        if (progState >= 9)
          return true;
        return false;
      }
    });
    clean.add(new CleanActionsWithState(state) {
      @Override
      public void run() {
        // mdpGraph.unhighlightNode(state, null, null);
        graphToNodes[state].hide();
        mdpMatrix.unhighlightCell(convertStateIdxTo2DArrayIdx(state).y,
            convertStateIdxTo2DArrayIdx(state).x, null, null);
      }

      @Override
      public boolean now(int progState) {
        if (progState == 2 || progState == 9)
          return true;
        return false;
      }
    });

  }

  private void aniamlGraph() {

    Node[] nodes = new Node[size];
    String[] labels = new String[size];
    int[][] adjMatrix = new int[size][];
    for (int i = 0; i < size; i++) {
      labels[i] = "s" + i;
      nodes[i] = new Offset(0 + graphDisBetweenNodes * (i % sizeX), 20
          + graphDisBetweenNodes * (i / sizeX), pCodeName,
          AnimalScript.DIRECTION_SW); // new Coordinates(graphStartX +
      // graphDisBetweenNodes * (i %
      // sizeX), graphStartY +
      // graphDisBetweenNodes * (i /
      // sizeX));
    }
    for (int s = 0; s < size; s++) {
      adjMatrix[s] = new int[size];
      for (int a = 0; a < NUMBER_OF_ACTIONS; a++) {
        if (isStateChangeOk(s, a)) {
          // System.out.println(y + "  " + (y* sizeX + x +
          // stateChange(a)));
          adjMatrix[s][s + stateChange(a)] = 1;
        }
      }

    }

    mdpGraph = lang.newGraph(graphName, adjMatrix, nodes, labels, null,
        graphProp);

    int[][] adj = new int[1][];
    adj[0] = new int[1];
    graphToNodes = new Graph[nodes.length];
    for (int i = 0; i < nodes.length; i++) {
      graphToNodes[i] = lang.newGraph("gToS" + i, adj, new Node[] { nodes[i] },
          new String[] { "s" + i }, null, nGraphProp);
      graphToNodes[i].hide();

    }
  }

  private void animalGrid() {

    // style matrix
    double[][] d2vf = new double[size / sizeX][];
    for (int i = 0; i < size / sizeX; i++) {
      d2vf[i] = new double[sizeX];
    }
    for (int i = 0; i < size; i++) {
      d2vf[i / sizeX][i % sizeX] = valueFunction[i];
    }
    //
    mdpMatrix = lang.newDoubleMatrix(new Offset(50, 0, mdpGraph,
        AnimalScript.DIRECTION_NE), d2vf, "mdpGrid", null,
        valueFunctionGridProp);
  }

  private void aniamVIInIfUpdate(int state, double tmp, double d,
      double change, double newChange) {
    lang.nextStep();
    executeAllCleanActions(8);
    txtChange.setText("Change: " + rD(newChange), null, null);
    animalVars.set("change", "" + rD(newChange));
    // progState = 8
    sc.highlight(14, 0, true);
    sc.highlight(15, 0, false);
    sc.highlight(16, 0, false);
    sc.highlight(17, 0, true);
    mdpMatrix.put(convertStateIdxTo2DArrayIdx(state).y,
        convertStateIdxTo2DArrayIdx(state).x, rD(tmp), null, null);
    clean.add(new CleanActions() {

      @Override
      public void run() {
        sc.unhighlight(14, 0, true);
        sc.unhighlight(15, 0, false);
        sc.unhighlight(16, 0, false);
        sc.unhighlight(17, 0, true);
      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });

  }

  private double rD(double d) {
    try {
      if (Double.isNaN(d) || Double.isInfinite(d))
        return 0;
      DecimalFormat twoDForm = new DecimalFormat("#.##");
      return Double.valueOf(twoDForm.format(d));
    } catch (Exception e) {

      System.out.println(d);
    }
    return 0;

  }

  private void animalVIIfUpdate(double tmp, double d) {
    lang.nextStep();
    // progState = 7
    animalVars.set("alternativeValue", "" + tmp);
    // txtIfCalc.show();
    txtIfCalc.setText("alternativeValue > valueFunction[neighborState] = "
        + rD(tmp) + " > " + rD(d) + " = " + (tmp > d), null, null);
    executeAllCleanActions(7);

    // sc.unhighlight(10);
    // sc.unhighlight(11);
    // sc.unhighlight(12);
    sc.highlight(13);
    sc.highlight(14);
    clean.add(new CleanActions() {
      @Override
      public void run() {
        sc.unhighlight(13);
        sc.unhighlight(14);
        txtIfCalc.setText("", null, null);
        // txtIfCalc.hide();
      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });

  }

  private void animalVICalcOneStep(int state, int action, Integer ns,
      double transitionProbability, double vf, double tmp, double oldTmp,
      boolean firstCallNS) {
    // progState = 6
    lang.nextStep();
    executeAllCleanActions(6);
    if (firstCallNS) {

      // txtTmpUpCalc.show();
      // txtNS.show();
      // txtTransProb.show();
      sc.highlight(9, 0, true);
      sc.highlight(12, 0, true);
      sc.highlight(10);
      sc.highlight(11);
      clean.add(new CleanActionsWithState(state) {
        @Override
        public void run() {
          sc.unhighlight(9, 0, true);
          sc.unhighlight(10);
          sc.unhighlight(11);
          sc.unhighlight(12);
          // txtTransProb.hide();
          // txtNS.hide();
          // txtTransProb.hide();
          txtTransProb.setText("", null, null);
          txtNS.setText("", null, null);
          txtTmpUpCalc.setText("", null, null);

        }

        @Override
        public boolean now(int progState) {
          if (progState == 7)
            return true;
          return false;
        }
      });
    }

    txtTmpUpCalc.setText(rD(oldTmp) + " += " + rD(transitionProbability)
        + " * " + rD(vf) + " = " + rD(tmp), null, null);
    txtNS.setText("neighborState: s" + ns, null, null);
    txtTransProb.setText("Die Übergangswahrscheinlichkeit fuer (S" + state
        + ", " + actionIntToString(action) + ", " + ns + ") ist "
        + rD(transitionProbability), null, null);
    animalVars.set("alternativeValue", "" + rD(tmp));
    animalVars.set("neighborState", "" + ns);

  }

  Point convertStateIdxTo2DArrayIdx(int stateIdx) {
    return new Point(stateIdx % sizeX, stateIdx / sizeX);
  }

  String actionIntToString(int action) {

    if (action == NORTH)
      return "Norden";
    if (action == EAST)
      return "Osten";
    if (action == WEST)
      return "Westen";
    if (action == SOUTH)
      return "Süden";
    return "";
  }

  private void animalVIAction(int state, int action, boolean firstCall) {
    // progState = 5
    lang.nextStep();
    executeAllCleanActions(5);
    if (firstCall) {
      // txtAV.show();
      sc.highlight(7, 0, true);
      sc.highlight(18, 0, true);

      clean.add(new CleanActions() {
        @Override
        public void run() {
          sc.unhighlight(7, 0, true);
          sc.unhighlight(18, 0, true);
          txtAV.setText("", null, null);
          txtAction.setText("", null, null);
          animalVars.set("alternativeValue", "0");
        }

        @Override
        public boolean now(int progState) {
          if (progState < 5 || progState > 8)
            return true;
          return false;
        }
      });
    }
    sc.highlight(8, 0, false);
    txtAction.setText("action: " + actionIntToString(action), null, null);
    txtAV.setText("alternativeValue: " + 0, null, null);
    animalVars.set("action", actionIntToString(action));
    animalVars.set("alternativeValue", "0");
    mdpGraph.highlightEdge(state, state + stateChange(action), null, null);
    clean.add(new CleanActionsWithStateAndAction(state, action) {
      @Override
      public void run() {

        mdpGraph
            .unhighlightEdge(state, state + stateChange(action), null, null);
      }

      @Override
      public boolean now(int progState) {
        if (progState < 6 || progState > 8)
          return true;
        return false;
      }
    });
    clean.add(new CleanActions() {
      @Override
      public void run() {
        sc.unhighlight(8, 0, false);
      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });

  }

  private void animalVIgetNeighbors(int state) {
    // progState = 3
    lang.nextStep();
    executeAllCleanActions(3);
    highlightAllNeighbors(state);
    // sc.unhighlight(5);
    sc.highlight(6);

    clean.add(new CleanActionsWithState(state) {
      @Override
      public void run() {
        for (int i = 0; i < getNeighborStates(state).size(); i++) {
          mdpGraph.unhighlightNode(getNeighborStates(state).get(i), null, null);
          mdpMatrix.unhighlightElem(
              convertStateIdxTo2DArrayIdx(getNeighborStates(state).get(i)).y,
              convertStateIdxTo2DArrayIdx(getNeighborStates(state).get(i)).x,
              null, null);
        }

      }

      @Override
      public boolean now(int progState) {
        if (progState < 3 || progState > 8)
          return true;
        return false;
      }
    });
    clean.add(new CleanActionsWithState(state) {
      @Override
      public void run() {
        sc.unhighlight(6);

      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });

  }

  private void highlightAllNeighbors(int state) {
    for (int i = 0; i < getNeighborStates(state).size(); i++) {
      mdpGraph.highlightNode(getNeighborStates(state).get(i), null, null);
      mdpMatrix.highlightElem(
          convertStateIdxTo2DArrayIdx(getNeighborStates(state).get(i)).y,
          convertStateIdxTo2DArrayIdx(getNeighborStates(state).get(i)).x, null,
          null);

    }
  }

  int viCount = 0;

  private void animalVIbeforeAllStates() {
    lang.nextStep("Value Iteration " + viCount);

    // progState = 1
    viCount++;

    // txtChange.show();
    txtChange.setText("change: 0.0", null, null);

    animalVars.set("change", "0");
    sc.highlight(3, 0, true);
    sc.highlight(4);
    sc.highlight(20, 0, true);
    clean.add(new CleanActions() {

      @Override
      public void run() {
        sc.unhighlight(4);
      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });
    clean.add(new CleanActions() {

      @Override
      public void run() {
        sc.unhighlight(3, 0, true);
        sc.unhighlight(20, 0, true);
        txtChange.setText("", null, null);
        // txtChange.hide();
      }

      @Override
      public boolean now(int progState) {
        return false;
      }
    });
  }

  /**
   * All animation steps before the actual algo
   */
  private void animalInitVI() {
    // progState = 0
    // lang.nextStep();
    animalVars = lang.newVariables();
    animalVars.declare("double", "change");
    animalVars.declare("string", "state");
    animalVars.declare("string", "action");
    animalVars.declare("int", "neighborState");
    animalVars.declare("double", "alternativeValue");

    txtHeadline = lang.newText(new Coordinates(20, 10), "Value Iteration",
        "headline", null, headlineTextProp);

    SourceCode vibes = lang.newSourceCode(new Coordinates(20, 20), "be", null);
    for (int i = 0; i < beginDescription.length; i++) {
      vibes.addCodeLine(beginDescription[i], "" + i, 0, null);
    }
    lang.nextStep();
    vibes.hide();
    animalpCode();
    txtHeadline
        .setText("Value Iteration - Pseudocode -- Erklärung", null, null);
    SourceCode pCb1 = createSC(new Offset(20, 0, sc, "NE"), pCodeBe1);
    sc.highlight(0);
    sc.highlight(1);

    lang.nextStep();
    pCb1.hide();
    sc.unhighlight(0);
    sc.unhighlight(1);

    SourceCode pCb2 = createSC(new Offset(20, 23, sc, "NE"), pCodeBe2);
    sc.highlight(2);

    lang.nextStep();
    pCb2.hide();
    sc.unhighlight(2);
    SourceCode pCb3 = createSC(new Offset(20, 150, sc, "NE"), pCodeBe3);
    sc.highlight(3);
    sc.highlight(20);

    lang.nextStep();
    pCb3.hide();
    sc.unhighlight(3);
    sc.unhighlight(20);
    SourceCode pCb4 = createSC(new Offset(20, 140, sc, "NE"), pCodeBe4);
    sc.highlight(9);
    sc.highlight(10);
    sc.highlight(11);
    sc.highlight(12);

    lang.nextStep();
    pCb4.hide();
    sc.unhighlight(10);
    sc.unhighlight(11);
    sc.unhighlight(12);
    sc.unhighlight(9);
    SourceCode pCb5 = createSC(new Offset(20, 220, sc, "NE"), pCodeBe5);
    sc.highlight(15);
    sc.highlight(16);
    sc.highlight(17);
    sc.highlight(14);

    lang.nextStep();
    pCb5.hide();
    sc.unhighlight(15);
    sc.unhighlight(16);
    sc.unhighlight(17);
    sc.unhighlight(14);
    sc.hide();
    SourceCode textBeforeExample = createSC(
        new Offset(0, 0, txtHeadline, "SW"), txtBeforExample);

    lang.nextStep();
    textBeforeExample.hide();
    sc.show();

    aniamlGraph();
    animalGrid();

    txtHeadline
        .setText("Value Iteration - Pseudocode -- Animation", null, null);

    txtVars = lang.newText(new Offset(100, 0, mdpMatrix, "NE"), "Variablen",
        "vars", null, headlineVarsTextProp);

    txtChange = lang.newText(new Offset(100, 25, mdpMatrix, "NE"), "",
        "change", null, infoTextsProp);
    txtState = lang.newText(new Offset(100, 45, mdpMatrix, "NE"), "", "state",
        null, infoTextsProp);
    txtAction = lang.newText(new Offset(100, 65, mdpMatrix, "NE"), "",
        "action", null, infoTextsProp);
    txtNS = lang.newText(new Offset(100, 85, mdpMatrix, "NE"), "",
        "neighborState", null, infoTextsProp);
    txtAV = lang.newText(new Offset(100, 105, mdpMatrix, "NE"), "",
        "alternativeValue", null, infoTextsProp);
    txtTransProb = lang.newText(new Offset(100, 125, mdpMatrix, "NE"), "",
        "transProb", null, infoTextsProp);
    int scLineH = 16;
    txtDoW = lang.newText(new Offset(50, 19 * scLineH, sc, "NE"), "", "doW",
        null, infoTextsProp);

    txtIfCalc = lang.newText(new Offset(50, 14 * scLineH, sc, "NE"), "",
        "ifBerechnung", null, infoTextsProp);

    txtTmpUpCalc = lang.newText(new Offset(50, 11 * scLineH, sc, "NE"), "",
        "tmpUpdateBerechnung", null, infoTextsProp);

  }

  /**
   * returns a new animal SourceCode object for a given string array
   * 
   * @param coordinates
   * @param pCodeBe12
   * @return
   */
  private SourceCode createSC(Node coordinates, String[] pCodeBe12) {
    SourceCode cs = lang.newSourceCode(coordinates, pCodeBe12[0], null);
    for (int i = 0; i < pCodeBe12.length; i++) {
      cs.addCodeLine(pCodeBe12[i], "" + i, 0, null);
    }
    return cs;
  }

  /**
   * creates the pseudo code for the value iteration
   */
  private void animalpCode() {

    // pCodeProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.red);
    // pCodeProp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
    // Color.blue);

    sc = lang
        .newSourceCode(new Coordinates(20, 20), pCodeName, null, pCodeProp);
    for (int i = 0; i < pCode.length; i++) {
      sc.addCodeLine(pCode[i], "" + i, 0, null);
    }

  }

  /**
   * returns for all neighbors of state the idx's
   * 
   * @param state
   * @return
   */
  List<Integer> getNeighborStates(int state) {
    List<Integer> neighborStates = new LinkedList<Integer>();
    for (int i = 0; i < 4; i++) {
      if (isStateChangeOk(state, i)) {
        int newState = state + stateChange(i);
        neighborStates.add(newState);
      }
    }
    return neighborStates;
  }

  /**
   * returns the transition probability for a tuple (s,a,s')
   * 
   * @param state
   * @param action
   * @param newState
   * @return
   */
  private double getTransitionProbability(int state, int action, int newState) {
    if (state + stateChange(action) == newState)
      return P_TRANSITION_TO_DESIRED_STATE;
    return P_TRANSITION_OTHER_N_STATE;
  }

  /**
   * true if it's possible to go from state (state) in the direction (direction)
   * we work on a grid world.
   * 
   * @param state
   * @param direction
   * @return
   */
  private boolean isStateChangeOk(int state, int direction) {
    if (direction == NORTH && state < sizeX)
      return false;
    if (direction == EAST && state % sizeX == sizeX - 1)
      return false;
    if (direction == WEST && state % sizeX == 0)
      return false;
    if (direction == SOUTH && state + sizeX >= size)
      return false;
    return true;
  }

  /**
   * returns the change for an actions (direction)
   * 
   * @param direction
   * @return
   */
  int stateChange(int direction) {
    switch (direction) {
      case NORTH:
        return -sizeX;
      case EAST:
        return +1;
      case WEST:
        return -1;
      case SOUTH:
        return +sizeX;
    }
    throw new RuntimeException("Invalide action");
  }

  /**
   * executes all cleaning actions for a step
   * 
   * @param progState
   */
  private void executeAllCleanActions(int progState) {
    // System.out.println(progState);
    for (Iterator<CleanActions> iterator = clean.iterator(); iterator.hasNext();) {
      CleanActions ca = iterator.next();
      if (ca.now(progState)) {
        ca.run();
        iterator.remove();
      }
    }
  }

  public void setGraphProp(GraphProperties graphProp) {
    this.graphProp = graphProp;
  }

  public void setNGraphProp(GraphProperties nGraphProp) {
    this.nGraphProp = nGraphProp;
  }

  public void setValueFunctionGridProp(MatrixProperties valueFunctionGridProp) {
    this.valueFunctionGridProp = valueFunctionGridProp;
  }

  public void setpCodeProp(SourceCodeProperties pCodeProp) {
    this.pCodeProp = pCodeProp;
  }

  public void setInfoTextsProp(TextProperties infoTextsProp) {
    this.infoTextsProp = infoTextsProp;
  }

  void setHeadlineTextProp(TextProperties headlineTextProp) {
    this.headlineTextProp = headlineTextProp;
  }

  void setHeadlineVarsTextProp(TextProperties headlineVarsTextProp) {
    this.headlineVarsTextProp = headlineVarsTextProp;
  }

  public void setAskQuestions(boolean askQuestions) {
    this.askQuestions = askQuestions;
  }

}

// Some classes and a interface that allows to define "clean action" that are
// executed in a certain valut iteration code step
