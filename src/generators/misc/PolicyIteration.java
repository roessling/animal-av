package generators.misc;

import generators.misc.helpers.CleanActions;
import generators.misc.helpers.CleanActionsWithState;
import generators.misc.helpers.CleanActionsWithStateAndAction;
import generators.misc.helpers.LinearEquationSolver;
import generators.misc.helpers.Point;

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
import algoanim.util.TicksTiming;

public class PolicyIteration {
  // Some constants
  private static List<CleanActions> clean                         = new LinkedList<CleanActions>();
  private static final int          NORTH                         = 0,
      EAST = 1, SOUTH = 2, WEST = 3;
  private static final int          NUMBER_OF_ACTIONS             = 4;
  private static final double       P_TRANSITION_TO_DESIRED_STATE = 0.7;
  private static final double       P_TRANSITION_OTHER_N_STATE    = (1 - P_TRANSITION_TO_DESIRED_STATE) / 3;
  private static final String       pCodeName                     = "listSource";
  private static final String       graphName                     = "graphMDP";
  private static final int          NO_ACTION                     = 5;
  private int                       sizeX                         = 3;
  private int                       size                          = sizeX * 2;
  private int                       graphDisBetweenNodes          = 150;
  private double                    discountFactor                = 0.9;

  // Importent animal objects
  Language                          lang;
  Graph                             mdpGraph;
  SourceCode                        sc;
  DoubleMatrix                      mdpMatrix;
  Variables                         animalVars;
  Graph[]                           graphToNodes;
  Graph[]                           graphToTerminationNodes;
  // All animal prop objects
  GraphProperties                   graphProp                     = new GraphProperties();
  GraphProperties                   nGraphProp                    = new GraphProperties();
  GraphProperties                   terGraphProp                  = new GraphProperties();

  MatrixProperties                  valueFunctionGridProp         = new MatrixProperties();
  SourceCodeProperties              pCodeProp                     = new SourceCodeProperties();
  TextProperties                    infoTextsProp                 = new TextProperties();
  TextProperties                    headlineTextProp              = new TextProperties();
  TextProperties                    subHeadlineProp               = new TextProperties();

  // All primitivs
  double                            rewardForAState[];

  // All printed text
  private String[]                  pCode                         = {
      "FUNCTION policyIteration",
      "PARAMTER states, actions, reward",
      "RETURN policy",
      "CONSTANTS DiscountFactor",
      "VARIABLE ValueFunction",
      "DO",
      "   SET change TO false",
      " "
          + "  SET ValueFunction To SOLVE_LINEAR_EQUATIONSYSTEM (policy, rewardForStates)",
      "",
      "   FORALL states BEGIN",
      "      SET rewardBestAction TO bestRewardLastIteration()",
      "      SET neighborStates TO getNeighborStates(state)",
      "      FORALL possible actions BEGIN",
      "         SET rewardAction TO 0",
      "         FORALL neighborStates BEGIN",
      "            INCREMENT reward BY",
      "                           transitionProbability(state, action, neighborState) * ValueFunction[neighborState]",
      "         END",
      "         SET rewardAction TO rewardAction * DiscountFactor",
      "         IF rewardAction> rewardBestAction BEGIN",
      "            SET rewardBestAction TO rewardAction",
      "            SET policy[state] TO action",
      "            SET change TO true", "         END", "      END", "   END",
      "WHILE change", "END FUNCTION", "",                        };
  private String[]                  beginDescription              = {
      "Ist ein Verfahren, welches beim Bestärkenden Lernen (engl. Reinforcement learning) verwendet wird. ",
      "Policy Iteration arbeitet dabei auf dem Markow-Entscheidungsproblem (engl. Markov decision process). ",
      "Die Policy Iteration findet für jeden Zustand die beste Aktion. Die beste Aktion ist die Aktion mit der die maximale Belohnung, die im Markow-Entscheidungsproblem für diesen Zustand erreichbar ist, ericht wird.",
      "Policy (deutsch Strategie) ist die Zuweisung einer Aktion zu einem Zustand.",
      "",
      "Das Markow-Entscheidungsproblem ist hier definiert als ein Tupel(S,A,P,R). ",
      "Dabei ist: ",
      "   S die Menge der Zustände,",
      "   A die Menge der Aktionen, ",
      "   P die Wahrscheinlichkeit, dass bei der Ausführung der Aktion a im Zustand s in den Zustand s_neu gewechselt wird, und",
      "   R die Belohnungsfunktion, welche einem Zustand s eine Belohnung r zuordnet.",
      "Für mehr Informationen zum Markow-Entscheidungsproblem siehe:",
      "en.wikipedia.org/wiki/Markov_decision_process. ",
      "",
      "",
      "Die Policy Iteration ist dabei der Value Iteration ähnlich. Bei der Value Iteration wird zu erste die optimale Value Funktion iterative berechnet.",
      "Wenn die optimale Value Funktion berechnet wurde kann über diese die beste Aktion für jeden Zustand bestimmt werden. Die Policy Iteration funktioniert dagegen wie folgt:",
      "1.	Erzeuge eine zufällige Strategie",
      "2.	Wiederhole bis Konvergenz erreicht",
      "   a.	Berechne die zur Strategie gehörige Value Funktion V",
      "   b.	Bestimmt die beste Strategie zu V "                 };

  // pcodeBeX explains some pCode lines
  private String[]                  pCodeBe1                      = {
      "Policy Iteration Funktionsdefinition  ",
      "stats ist eine Datenstruktur mit allen Zustanden",
      "actions ist eine Datenstruktur mit allen Aktionen",
      "reward ist eine Datenstruktur welche Zuständen eine Belohnung zuweist",
      "policy ist der Rückgabewert (beste Aktion für jeden Zustand)." };
  private String[]                  pCodeBe2                      = { "Der Diskontierungsfaktor gibt an, wie stark zukünftige Belohnungen gewichtet werden. " };
  private String[]                  pCodeBe3                      = { "Bei der Policy Iteration wird die Value Funktion nur lokal berechnet." };
  private String[]                  pCodeBe4                      = {
      "Die Do-While-Schleife wiederholt eine Iteration der Policy-Iteration (Aktualisierung der Policy)  ",
      "Die Do-While-Schleife iteriert dabei, bis das Konvergenzkriterium erfüllt ist.",
      "Das Konvergenzkriterium ist erfüllt wenn es keine Policy Änderung in der letzten Iteration gab." };
  private String[]                  pCodeBe5                      = {
      "Hier wird die zu einer Policy gehöhrende Value Funktion berechnet.",
      "Die Berechnung basiert auf dem Optimalitätsprinzip von Bellman.",
      "Dabei muss ein lineares Gleichungssystem der Größe |states| gelöst werden. " };
  private String[]                  pCodeBe6                      = {
      "Hier wird die beste Aktion für jeden Zustand (Policy) bestimmt.",
      "Der Pseudo Quelltext hier ist allgemein gehalten.",
      "In den meisten fällten ist die beste Aktion einfach:",
      "   wechsle in den Nachbarzustand mit dem höchsten Value Funktionswert.",
      "Da der Zustandsübergang aber nicht Deterministisch ist kann es Fälle geben",
      "wo die hier dargestellt komplexere Berechnung nötig ist." };

  private String[]                  pCodeBe7                      = {
      "Da der Zustandsübergang nicht deterministisch ist, müssen für jede Aktion   ",
      "alle Nachbarn des aktuellen Zustandes abhängig von der Übergangswahrscheinlichkeit berücksichtig werden.",
      "Hier wird die Aktion gesucht welche die höchste Belohnung verspricht." };
  private String[]                  pCodeBe8                      = { "Wurde ein bessere Aktion gefunden? Ja -> update." };
  private String[]                  txtBeforExample               = {
      "Policy Iteration anhand eines Beispiels: ",
      "Die Policy Iteration wird hier für ein Markow-Entscheidungsproblem, das eine nicht deterministische Rasterwelt darstellt, animiert.  ",
      "Es gibt die Aktionen Wechsel nach Norden, Süden, Osten und Westen in den nächsten Zustand.",
      "Wenn dies nicht möglich ist, wird der Zustand nicht gewechselt.",
      "Beim Ausfuehren der Aktion: wechsle den Zustand nach Norden, wird mit der Wahrscheinlichkeit 0.7 nach Norden gewechselt",
      "und mit der Wahrscheinlichkeit 0.1 nach Osten, Westen oder Sueden. (Der Zustandsübergang ist nicht deterministisch)",
      "Dasselbe gilt für die anderen Aktionen. ",
      "",
      "Einigen Zuständen ist eine Belohnung (engl. reward) zugeordnet. Die Belohnung kann positive (es ist gut den Zustand zu erreichen)",
      "oder negative (der Zustand sollte vermieden werden) sein.",
      "Die Zustände welchen eine Belohnung zugewiesen ist sind Terminierungszustände, d.H. es ist nicht möglich diese Zustände wider zu verlassen." };

  // Text that is printed after the algo
  private String[]                  endText                       = {
      "Die Policy Iteration findet verglichen zur Value Iteration für kleiner MDPs in der Regel schneller eine optimal Lösung.",
      "Für große MDPs muss bei der Policy Iteration ein großes lineares Gleichungssystem gelöst werden was die Policy Iteration unter Umständen unpraktikabel macht",
      "Mehr Informationen zur Policy Iteration wie auch zum Bestärkendem-Lernen sind in den Vorlesungsunterlagen des Machine Learning Kurses vom Stanford zu finden.  ",
      "Die URL ist http://cs229.stanford.edu/notes/cs229-notes12.pdf. Die Unterlagen sind in English." };

  // All aniaml text fields
  Text                              txtChange;
  Text                              txtAction;
  Text                              txtState;
  Text                              txtNS;
  // private Text txtAV;
  Text                              txtTransProb;
  Text                              txtIfCalc;
  Text                              txtDoW;
  Text                              txtTmpUpCalc;
  Text                              txtHeadline;
  Text                              txtVars;
  Text                              txtSoLEHeadling;
  Text[]                            txtSoLE;

  int getSizeX() {
    return sizeX;
  }

  int getSizeY() {
    return size / sizeX;
  }

  // private double[] valueFunction;

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

  PolicyIteration(Language l) {
    lang = l;
    lang.setStepMode(true);

    infoTextsProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 12));
    subHeadlineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 13));
    headlineTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 14));
    // rewardForAState = new double[size];
    // rewardForAState[size - 1] = 1;
    // rewardForAState[2] = -1;
    //
    // // set properties
    // setDefaultProps();
  }

  void setDefaultProps() {
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
    valueFunctionGridProp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY,
        MatrixProperties.alignOptions.get(1));
    headlineTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 14));
    infoTextsProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 12));
    subHeadlineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
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
    rewardForAState = vF;
    this.sizeX = sizeX;
    discountFactor = disFactor;
    rewardForAState = new double[size];
  }

  public void setProperties(int[][] vF, double disFactor) {
    policy = new int[vF.length * vF[0].length];
    rewardForAState = new double[vF.length * vF[0].length];
    valueFunction = new double[vF.length * vF[0].length];
    int idx = 0;
    for (int[] row : vF)
      for (int el : row)
        rewardForAState[idx++] = el;
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
                + " style matrix cellWidth 60 cellHeight 60 ");
    // i didn't find a way to define cellWidth and Heights with the
    // properties object
  }

  double valueFunction[] = new double[size];
  int    policy[]        = new int[size];

  public void policyIteration() {

    animalInitPI();
    int coundDoWhileLoops = 0;
    double bestRewardLasterIteration[] = new double[size];
    for (int i = 0; i < size; i++)
      bestRewardLasterIteration[i] = -Double.MAX_VALUE;
    boolean change = false;

    do {
      AnimalVisuStep1();

      change = false;

      valueFunction = solveLSoE();
      AnimalVisuStep2();

      // System.out.println("");
      // for(double r : valueFunction) System.out.println(r);

      for (int state = 0; state < size; state++) {
        if (rewardForAState[state] == 0) {
          AnimalVisuStep3(state);

          List<Integer> neighborStates = getNeighborStates(state);
          double bestReward = bestRewardLasterIteration[state];
          AnimalVisuStep4(state, bestRewardLasterIteration[state]);

          int policyChange = 0;
          for (int action = 0; action < NUMBER_OF_ACTIONS; action++) {
            if (isStateChangeOk(state, action)) {
              AnimalVisuStep5(state, action);
              double reward = 0;

              boolean firstAllNRound = true;
              for (int neighborStateIdx = 0; neighborStateIdx < neighborStates
                  .size(); neighborStateIdx++) {

                double oldReward = reward;
                reward += getTransitionProbability(state, action,
                    neighborStates.get(neighborStateIdx))
                    * valueFunction[neighborStates.get(neighborStateIdx)];

                AnimalVisuStep6(
                    state,
                    neighborStates.get(neighborStateIdx),
                    action,
                    reward,
                    oldReward,
                    getTransitionProbability(state, action,
                        neighborStates.get(neighborStateIdx)), firstAllNRound);
                firstAllNRound = false;

              }
              AnimalVisuStep7(state, reward, bestReward);

              reward *= discountFactor;
              if (reward > bestReward) {
                bestReward = reward;
                bestRewardLasterIteration[state] = reward;
                policyChange = action;
                policy[state] = policyChange;
                change = true;
                if (rewardForAState[state] != 0)
                  policy[state] = NO_ACTION;
                // if(valueFunction[state] > reward)
                // policy[state] = NO_ACTION;
                AnimalVisuStep8(state, bestReward);
              }
            }
          }
        }

      }
      AnimalVisuStep9();

      // System.out.println("policy");
      // for(int i = 0; i < size; i++)
      // System.out.println(policy[i]);
      //

      // System.out.println("\n\n#########");
      coundDoWhileLoops++;
    } while (change && coundDoWhileLoops < 10);
    AnimalVisuStep10();
  }

  private void AnimalVisuStep10() {
    lang.nextStep();
    executeAllCleanActions(10);
    sc.hide();
    animalHighlighPolicy();
    txtChange.setText("", null, null);
    txtVars.setText("", null, null);
    createSC(new Offset(0, 20, txtHeadline, "SW"), endText);
    txtHeadline.setText("Policy Iteration - Ende", null, null);

  }

  private void AnimalVisuStep9() {
    lang.nextStep();
    executeAllCleanActions(9);
    animalHighlighPolicy();
    clean.add(new CleanActions() {

      @Override
      public void run() {
        animalUnHighlighPolicy();

      }

      @Override
      public boolean now(int progState) {

        return true;
      }
    });

  }

  private void AnimalVisuStep8(int state, double bestReward) {
    lang.nextStep();
    executeAllCleanActions(8);
    animalVars.set("change", "true");
    animalVars.set("rewardBestAction", "" + rD(bestReward));
    txtRewardBestAction.setText("rewardBestAction: " + rD(bestReward), null,
        null);
    txtChange.setText("change: true", null, null);
    sc.highlight(19, 0, true);
    sc.highlight(20);
    sc.highlight(21);
    sc.highlight(22);
    sc.highlight(23, 0, true);
    clean.add(new CleanActions() {

      @Override
      public void run() {
        sc.unhighlight(19, 0, true);
        sc.unhighlight(20);
        sc.unhighlight(21);
        sc.unhighlight(22);
        sc.unhighlight(23, 0, true);
        txtRewardBestAction.setText("", null, null);
        animalVars.set("rewardBestAction", "0");
      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });
  }

  private void AnimalVisuStep7(int state, double reward, double bestReward) {
    lang.nextStep();
    executeAllCleanActions(7); // progState = 7
    animalVars.set("rewardAction", "" + rD(reward));
    // txtIfCalc.show();
    txtIfCalc.setText("rewardAction > rewardBestAction = " + rD(reward) + " > "
        + rD(bestReward) + " = " + (reward > bestReward), null, null);
    txtTmpUpCalc.setText("", null, null);
    sc.highlight(18);
    sc.highlight(19);

    clean.add(new CleanActions() {

      @Override
      public void run() {
        txtIfCalc.setText("", null, null);
        sc.unhighlight(18);
        sc.unhighlight(19);

      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });

  }

  private void AnimalVisuStep6(int state, int nstate, int action,
      double reward, double oldReward, double transPro, boolean firstAllNRound) {
    lang.nextStep();
    executeAllCleanActions(6);
    animalVars.set("rewardAction", "" + rD(reward));
    animalVars.set("neighborState", "S" + nstate);
    txtNS.setText("neighborState: S" + nstate, null, null);
    txtRewardAction.setText("rewardAction: " + rD(reward), null, null);
    txtTmpUpCalc.setText(rD(oldReward) + " += " + rD(transPro) + " * "
        + rD(valueFunction[nstate]) + " = " + rD(reward), null, null);
    txtTransProb.setText("Die Übergangswahrscheinlichkeit fuer (S" + state
        + ", " + actionIntToString(action) + ", " + nstate + ") ist "
        + rD(transPro), null, null);

    if (firstAllNRound) {
      sc.highlight(14, 0, true);
      sc.highlight(17, 0, true);
      sc.highlight(15);
      sc.highlight(16);

      clean.add(new CleanActions() {

        @Override
        public void run() {
          sc.unhighlight(14, 0, true);
          sc.unhighlight(17, 0, true);
          sc.unhighlight(15);
          sc.unhighlight(16);
          txtTmpUpCalc.setText("", null, null);
          txtNS.setText("", null, null);
          txtTransProb.setText("", null, null);
          animalVars.set("neighborState", "");
        }

        @Override
        public boolean now(int progState) {
          return progState != 6;
        }
      });
    }

  }

  private void AnimalVisuStep5(final int state, final int action) {

    lang.nextStep();
    executeAllCleanActions(5);
    animalVars.set("action", actionIntToString(action) + "");
    animalVars.set("rewardAction", "0");
    txtState.setText("State: S" + state, null, null);
    txtRewardAction.setText("rewardAction: " + 0, null, null);
    txtAction.setText("Action: " + actionIntToString(action), null, null);
    sc.highlight(12, 0, true);
    sc.highlight(24, 0, true);
    sc.highlight(13);
    mdpGraph.highlightEdge(state, state + stateChange(action), null, null);

    clean.add(new CleanActionsWithStateAndAction(state, action) {
      @Override
      public void run() {
        sc.unhighlight(13);
      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });
    clean.add(new CleanActionsWithStateAndAction(state, action) {

      @Override
      public void run() {
        mdpGraph
            .unhighlightEdge(state, state + stateChange(action), null, null);
        sc.unhighlight(12, 0, true);
        sc.unhighlight(24, 0, true);
        animalVars.set("action", "");
        animalVars.set("rewardAction", "0");
        txtAction.setText("", null, null);
        txtRewardAction.setText("", null, null);
        txtState.setText("", null, null);
      }

      @Override
      public boolean now(int progState) {
        return progState <= 5 || progState >= 9;
      }
    });

  }

  private void AnimalVisuStep4(final int state, double bestRewardLasterIteration) {
    lang.nextStep();

    executeAllCleanActions(4);
    animalVars.set("rewardBestAction", rD(bestRewardLasterIteration) + "");
    txtRewardBestAction.setText("rewardBestAction: "
        + rD(bestRewardLasterIteration), null, null);
    highlightAllNeighbors(state);
    sc.highlight(10);
    sc.highlight(11);

    clean.add(new CleanActionsWithState(state) {

      @Override
      public void run() {
        sc.unhighlight(10);
        sc.unhighlight(11);
      }

      @Override
      public boolean now(int progState) {
        return true;
      }
    });
    clean.add(new CleanActionsWithState(state) {

      @Override
      public void run() {
        unHighlightAllNeighbors(state);
        animalVars.set("rewardBestAction", "0");
        txtRewardBestAction.setText("", null, null);
      }

      @Override
      public boolean now(int progState) {
        return progState == 9 || progState == 3;
      }
    });

  }

  private void AnimalVisuStep3(final int state) {
    lang.nextStep();
    executeAllCleanActions(3);
    animalVars.set("state", "S" + state);
    txtState.setText("state: S" + state, null, null);
    sc.highlight(9, 0, true);
    sc.highlight(25, 0, true);
    graphToNodes[state].show();
    mdpMatrix.highlightElem(convertStateIdxTo2DArrayIdx(state).y,
        convertStateIdxTo2DArrayIdx(state).x, null, null);
    clean.add(new CleanActionsWithState(state) {

      @Override
      public void run() {
        sc.unhighlight(9, 0, true);
        sc.unhighlight(25, 0, true);
        graphToNodes[state].hide();
        animalVars.set("state", "");
        txtState.setText("", null, null);
        mdpMatrix.unhighlightElem(convertStateIdxTo2DArrayIdx(state).y,
            convertStateIdxTo2DArrayIdx(state).x, null, null);
      }

      @Override
      public boolean now(int progState) {
        return progState == 9 || progState == 3;
      }
    });

  }

  private void AnimalVisuStep2() {
    lang.nextStep("Policy Iteration " + picounter);
    picounter++;
    executeAllCleanActions(2);
    animalVars.set("change", "false");
    txtChange.setText("chagen: false", null, null);
    sc.highlight(6);
    sc.highlight(7);
    animalSoLERefresh();
    animalGridRefresh();
    clean.add(new CleanActions() {

      @Override
      public void run() {
        sc.unhighlight(6);
        sc.unhighlight(7);
        animalHideSoLE();

      }

      @Override
      public boolean now(int progState) {
        return progState == 3;
      }
    });

  }

  void unHighlightAllNeighbors(int state) {
    for (int i = 0; i < getNeighborStates(state).size(); i++) {
      int ns = getNeighborStates(state).get(i);
      if (rewardForAState[ns] != 0)
        graphToTerminationNodes[ns].show();
      mdpGraph.unhighlightNode(getNeighborStates(state).get(i), null, null);
      mdpMatrix.unhighlightElem(
          convertStateIdxTo2DArrayIdx(getNeighborStates(state).get(i)).y,
          convertStateIdxTo2DArrayIdx(getNeighborStates(state).get(i)).x, null,
          null);
    }
  }

  private double[] solveLSoE() {
    double[][] matrix = new double[size][];
    // create lineare syste
    for (int state = 0; state < rewardForAState.length; state++) {
      matrix[state] = new double[size];
      matrix[state][state] = 1;
      if (rewardForAState[state] != 0) { // policy[state] == NO_ACTION
        // ){

      } else {
        for (int ns : getNeighborStates(state)) {
          if (state + stateChange(policy[state]) == ns) {
            matrix[state][ns] = -discountFactor * 0.7;
          } else {
            matrix[state][ns] = -discountFactor * 0.1;
          }
        }
      }
    }

    // printQMatrix(matrix,rewardForAState);

    double[] tmp = new double[size];
    for (int i = 0; i < size; i++)
      tmp[i] = rewardForAState[i];

    return LinearEquationSolver.solve(matrix, tmp);
  }

  boolean staretAn  = true;
  int     picounter = 1;

  private void AnimalVisuStep1() {
    if (staretAn)
      lang.nextStep("Start Animation");
    else
      lang.nextStep();
    staretAn = false;
    executeAllCleanActions(1);
    sc.highlight(5, 0, true);
    sc.highlight(26, 0, true);

    clean.add(new CleanActions() {

      @Override
      public void run() {
        sc.unhighlight(5, 0, true);
        sc.highlight(26, 0, true);
      }

      @Override
      public boolean now(int progState) {
        return progState >= 10;
      }
    });

  }

//  private void animalUnHighlighAllActions() {
//    for (int s = 0; s < valueFunction.length; s++) {
//      for (int a = 0; a < 4; a++) {
//        if (isStateChangeOk(s, a)) {
//          mdpGraph.unhighlightEdge(s, s + stateChange(a), null, null);
//        }
//
//      }
//
//    }
//  }

  private void animalHighlighPolicy() {
    for (int s = 0; s < valueFunction.length; s++) {
      if (isStateChangeOk(s, policy[s])) {
        mdpGraph.highlightEdge(s, s + stateChange(policy[s]), null,
            new TicksTiming(0));
      }
    }
    for (int s = 0; s < valueFunction.length; s++) {
      if (isStateChangeOk(s, policy[s])) {
        mdpGraph.unhighlightEdge(s, s + stateChange(policy[s]),
            new TicksTiming(50), null);
      }
    }
    for (int s = 0; s < valueFunction.length; s++) {
      if (isStateChangeOk(s, policy[s])) {
        mdpGraph.highlightEdge(s, s + stateChange(policy[s]), null,
            new TicksTiming(100));
      }
    }
    txtMPDHeadline.setText("MDP: Mit aktueller Policy", null, null);
  }

  void animalUnHighlighPolicy() {
    for (int s = 0; s < valueFunction.length; s++) {
      if (isStateChangeOk(s, policy[s])) {
        mdpGraph.unhighlightEdge(s, s + stateChange(policy[s]), null, null);
      }
    }
    txtMPDHeadline.setText("MDP:", null, null);

  }

  public static void main(String[] args) {
    Language l = new AnimalScript("Value Iteration", "M. Viering", 640, 640);
    PolicyIteration vi = new PolicyIteration(l);

    vi.policyIteration();
    // System.out.println(l.toString());
    try {
      // animal script becomes easy to long for the consol
      // =>
      // save it in a file
      FileWriter fstream = new FileWriter("VI_Auf5_2.asu");
      BufferedWriter out = new BufferedWriter(fstream);

      out.write(vi.animalScriptOutputHack());
      out.close();
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  private void animalGraph() {

    Node[] nodes = new Node[size];
    String[] labels = new String[size];
    int[][] adjMatrix = new int[size][];
    for (int i = 0; i < size; i++) {
      labels[i] = "s" + i + " r=" + rD(rewardForAState[i]);
      nodes[i] = new Offset(0 + graphDisBetweenNodes * (i % sizeX), 60
          + graphDisBetweenNodes * (i / sizeX), pCodeName,
          AnimalScript.DIRECTION_SW); // new
      // Coordinates(graphStartX
      // +
      // graphDisBetweenNodes
      // *
      // (i
      // %
      // sizeX),
      // graphStartY
      // +
      // graphDisBetweenNodes
      // *
      // (i
      // /
      // sizeX));
    }
    for (int s = 0; s < size; s++) {
      adjMatrix[s] = new int[size];
      if (rewardForAState[s] == 0) {
        for (int a = 0; a < NUMBER_OF_ACTIONS; a++) {
          if (isStateChangeOk(s, a)) {
            // System.out.println(y + "  " + (y* sizeX + x +
            // stateChange(a)));
            adjMatrix[s][s + stateChange(a)] = 1;
          }
        }
      }

    }

    mdpGraph = lang.newGraph(graphName, adjMatrix, nodes, labels, null,
        graphProp);

    int[][] adj = new int[1][];
    adj[0] = new int[1];
    graphToNodes = new Graph[nodes.length];
    graphToTerminationNodes = new Graph[nodes.length];
    for (int i = 0; i < nodes.length; i++) {
      graphToNodes[i] = lang.newGraph("gToS" + i, adj, new Node[] { nodes[i] },
          new String[] { "s" + i + " r=" + rD(rewardForAState[i]) }, null,
          nGraphProp);
      graphToNodes[i].hide();
      if (rewardForAState[i] != 0) {
        graphToTerminationNodes[i] = lang.newGraph("ter" + i, adj,
            new Node[] { nodes[i] }, new String[] { "s" + i + " r="
                + rD(rewardForAState[i]) }, null, terGraphProp);

      }

    }
  }

  private void animalGrid() {

    // style matrix
    double[][] d2vf = new double[size / sizeX][];
    for (int i = 0; i < size / sizeX; i++) {
      d2vf[i] = new double[sizeX];
    }
    // System.out.println("size: " + size + " sizeX " + sizeX);
    for (int i = 0; i < size; i++) {
      // System.out.println(i + " - " + (i / sizeX) + " - "+ (i % sizeX));
      d2vf[i / sizeX][i % sizeX] = rewardForAState[i];
    }
    //
    mdpMatrix = lang.newDoubleMatrix(new Offset(50, 0, mdpGraph,
        AnimalScript.DIRECTION_NE), d2vf, "mdpGrid", null,
        valueFunctionGridProp);
  }

  private void animalGridRefresh() {
    for (int state = 0; state < size; state++) {
      mdpMatrix.put(convertStateIdxTo2DArrayIdx(state).y,
          convertStateIdxTo2DArrayIdx(state).x, rD(valueFunction[state]), null,
          null);
    }
    for (int i = 0; i < sizeX; i++) {
      mdpMatrix.highlightCellRowRange(0, size / sizeX - 1, i, null,
          new TicksTiming(80));
    }
    for (int i = 0; i < sizeX; i++) {
      mdpMatrix.unhighlightCellRowRange(0, size / sizeX - 1, i,
          new TicksTiming(150), null);
    }

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

  private void highlightAllNeighbors(int state) {
    for (int i = 0; i < getNeighborStates(state).size(); i++) {
      int ns = getNeighborStates(state).get(i);
      if (rewardForAState[ns] != 0)
        graphToTerminationNodes[ns].hide();
      mdpGraph.highlightNode(getNeighborStates(state).get(i), null, null);
      mdpMatrix.highlightElem(
          convertStateIdxTo2DArrayIdx(getNeighborStates(state).get(i)).y,
          convertStateIdxTo2DArrayIdx(getNeighborStates(state).get(i)).x, null,
          null);

    }
  }

  int    viCount = 0;
  String preTxtSoLE;
  Text   txtSoLEInfo[];
  Text   txtMPDHeadline;
  Text   txtVFHeadline;
  Text   txtRewardBestAction;
  Text   txtRewardAction;

  /**
   * All animation steps before the actual algo
   */
  private void animalInitPI() {

    // progState = 0
    // lang.nextStep();
    animalVars = lang.newVariables();
    animalVars.declare("string", "change");
    animalVars.declare("string", "state");
    animalVars.declare("string", "action");
    animalVars.declare("string", "neighborState");
    animalVars.declare("double", "rewardAction");
    animalVars.declare("double", "rewardBestAction");

    //
    txtHeadline = lang.newText(new Coordinates(20, 10), "Policy Iteration",
        "headline", null, headlineTextProp);

    SourceCode vibes = lang.newSourceCode(new Coordinates(20, 20), "be", null);
    for (int i = 0; i < beginDescription.length; i++) {
      vibes.addCodeLine(beginDescription[i], "" + i, 0, null);
    }
    lang.nextStep();
    vibes.hide();
    animalpCode();
    txtHeadline.setText("Policy Iteration - Pseudocode -- Erklärung", null,
        null);

    lang.nextStep();
    SourceCode pCb1 = createSC(new Offset(20, 0, sc, "NE"), pCodeBe1);
    sc.highlight(0);
    sc.highlight(1);
    sc.highlight(2);

    lang.nextStep();
    pCb1.hide();
    sc.unhighlight(0);
    sc.unhighlight(1);
    sc.unhighlight(2);
    SourceCode pCb2 = createSC(new Offset(20, 40, sc, "NE"), pCodeBe2);
    sc.highlight(3);

    lang.nextStep();
    pCb2.hide();
    sc.unhighlight(3);
    SourceCode pCb3 = createSC(new Offset(20, 50, sc, "NE"), pCodeBe3);
    sc.highlight(4);

    lang.nextStep();
    pCb3.hide();
    sc.unhighlight(4);
    SourceCode pCb4 = createSC(new Offset(20, 180, sc, "NE"), pCodeBe4);
    sc.highlight(5);
    sc.highlight(26);

    lang.nextStep();
    pCb4.hide();
    sc.unhighlight(5);
    sc.unhighlight(26);
    SourceCode pCb5 = createSC(new Offset(20, 85, sc, "NE"), pCodeBe5);
    sc.highlight(7);

    lang.nextStep();
    pCb5.hide();
    sc.unhighlight(7);
    SourceCode pCb6 = createSC(new Offset(20, 190, sc, "NE"), pCodeBe6);
    for (int i = 9; i < 26; i++)
      sc.highlight(i);

    lang.nextStep();
    pCb6.hide();
    SourceCode pCb7 = createSC(new Offset(20, 220, sc, "NE"), pCodeBe7);
    for (int i = 9; i < 26; i++)
      sc.unhighlight(i);
    sc.highlight(17);
    sc.highlight(14);
    sc.highlight(15);
    sc.highlight(16);

    lang.nextStep();
    pCb7.hide();
    sc.unhighlight(17);
    sc.unhighlight(14);
    sc.unhighlight(15);
    sc.unhighlight(16);

    SourceCode pCb8 = createSC(new Offset(20, 300, sc, "NE"), pCodeBe8);

    sc.highlight(19);
    sc.highlight(20);
    sc.highlight(21);
    sc.highlight(22);
    sc.highlight(23);

    lang.nextStep();
    pCb8.hide();
    sc.unhighlight(19);
    sc.unhighlight(20);
    sc.unhighlight(21);
    sc.unhighlight(22);
    sc.unhighlight(23);

    sc.hide();
    SourceCode textBeforeExample = createSC(
        new Offset(0, 0, txtHeadline, "SW"), txtBeforExample);

    lang.nextStep();
    textBeforeExample.hide();
    sc.show();
    txtHeadline.setText("Policy Iteration - Animation", null, null);

    animalGraph();
    animalGrid();

    animalSoLE();

    txtVars = lang.newText(new Offset(100, -20, mdpMatrix, "NE"), "Variablen:",
        "vars", null, subHeadlineProp);

    txtChange = lang.newText(new Offset(0, 15, txtVars, "SW"), "", "change",
        null, infoTextsProp);
    txtState = lang.newText(new Offset(0, 10, txtChange, "SW"), "", "state",
        null, infoTextsProp);
    txtAction = lang.newText(new Offset(0, 10, txtState, "SW"), "", "action",
        null, infoTextsProp);
    txtNS = lang.newText(new Offset(0, 10, txtAction, "SW"), "",
        "neighborState", null, infoTextsProp);
    txtTransProb = lang.newText(new Offset(0, 10, txtNS, "SW"), "",
        "transProb", null, infoTextsProp);
    txtRewardBestAction = lang.newText(new Offset(0, 10, txtTransProb, "SW"),
        "", "txtrebeac", null, infoTextsProp);
    txtRewardAction = lang.newText(
        new Offset(0, 10, txtRewardBestAction, "SW"), "", "txtreac", null,
        infoTextsProp);

    int scLineH = 16;
    txtDoW = lang.newText(new Offset(50, 25 * scLineH, sc, "NE"), "", "doW",
        null, infoTextsProp);

    txtIfCalc = lang.newText(new Offset(50, 19 * scLineH, sc, "NE"), "",
        "ifBerechnung", null, infoTextsProp);

    txtTmpUpCalc = lang.newText(new Offset(50, 16 * scLineH, sc, "NE"), "",
        "tmpUpdateBerechnung", null, infoTextsProp);
    txtMPDHeadline = lang.newText(new Offset(0, -20, mdpGraph, "NW"), "MDP:",
        "mdp", null, subHeadlineProp);
    txtVFHeadline = lang.newText(new Offset(0, -20, mdpMatrix, "NW"),
        "Value Funktion:", "vfhead", null, subHeadlineProp);

  }

  private void animalSoLE() {
    txtSoLEHeadling = lang.newText(new Offset(20, 0, sc, "NE"), "",
        "SoLEHeadline", null, subHeadlineProp);
    txtSoLE = new Text[size];
    for (int i = 0; i < size; i++) {
      preTxtSoLE = "Gleichung " + (i + 1) + ": ";
      Offset of;
      if (i == 0) {
        of = new Offset(0, 5, txtSoLEHeadling, "SW");
      } else {
        of = new Offset(0, 5, txtSoLE[i - 1], "SW");
      }
      String app = "ValueFunction[" + i + "] = reward[i] (="
          + rD(rewardForAState[i]) + ") + DiscountFactor (="
          + rD(discountFactor) + ") * {(Action=" + actionIntToString(policy[i])
          + ")";
      for (int ns : getNeighborStates(i)) {
        app += rD(getTransitionProbability(i, policy[i], ns))
            + " * ValueFunction[" + ns + "] +";
      }
      app = app.substring(0, app.length() - 2) + "}";

      txtSoLE[i] = lang.newText(of, "", "txtSoLE" + i, null);

    }
    txtSoLEInfo = new Text[3];
    txtSoLEInfo[0] = lang.newText(new Offset(0, 10,
        txtSoLE[txtSoLE.length - 1], "SW"), " ", "txtSoLEInfo0", null);
    txtSoLEInfo[1] = lang.newText(new Offset(0, 5, txtSoLEInfo[0], "SW"), " ",
        "txtSoLEInfo1", null);
    txtSoLEInfo[2] = lang.newText(new Offset(0, 5, txtSoLEInfo[1], "SW"), "",
        "txtSoLEInfo2", null);

  }

  private void animalSoLERefresh() {
    txtSoLEHeadling.setText("System linearer Gleichungen:", null, null);
    for (int i = 0; i < size; i++) {

      String app = "Policy[" + i + "] = " + actionIntToString(policy[i])
          + " : ValueFunction[" + i + "] = reward[i] (="
          + rD(rewardForAState[i]) + ") + DiscountFactor (="
          + rD(discountFactor) + ") * {";
      for (int ns : getNeighborStates(i)) {
        app += rD(getTransitionProbability(i, policy[i], ns))
            + " * ValueFunction[" + ns + "] +";
      }
      app = app.substring(0, app.length() - 2) + "}";

      txtSoLE[i].setText(app, null, null);

    }

    txtSoLEInfo[0]
        .setText(
            "Das oben dargestellte System von linearen Gleichungen muss in diesem Schritt gelöst werden. ",
            null, null);
    txtSoLEInfo[1]
        .setText(
            "Die Unbekannten sind V[0]... V["
                + (size - 1)
                + "]. Dies kann z.B. mit dem Gaußsches Eliminationsverfahren Verfahren gemacht werden. ",
            null, null);
    txtSoLEInfo[2]
        .setText(
            "Zum Gaußschen Eliminationsverfahren enthält Animal auch eine Animation. ",
            null, null);
  }

  void animalHideSoLE() {
    for (int i = 0; i < size; i++) {
      txtSoLE[i].setText("", null, null);
    }
    txtSoLEHeadling.setText("", null, null);
    txtSoLEInfo[0].setText("", null, null);
    txtSoLEInfo[1].setText("", null, null);
    txtSoLEInfo[2].setText("", null, null);
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
  private List<Integer> getNeighborStates(int state) {
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
      case NO_ACTION:
        return 0;
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

  void setGraphProp(GraphProperties graphProp) {
    this.graphProp = graphProp;
  }

  void setNGraphProp(GraphProperties nGraphProp) {
    this.nGraphProp = nGraphProp;
  }

  void setValueFunctionGridProp(MatrixProperties valueFunctionGridProp) {
    this.valueFunctionGridProp = valueFunctionGridProp;
  }

  void setpCodeProp(SourceCodeProperties pCodeProp) {
    this.pCodeProp = pCodeProp;
  }

  void setInfoTextsProp(TextProperties infoTextsProp) {
    this.infoTextsProp = infoTextsProp;
  }

  void setHeadlineTextProp(TextProperties headlineTextProp) {
    this.headlineTextProp = headlineTextProp;
  }

  void setSubHeadlineProp(TextProperties headlineVarsTextProp) {
    this.subHeadlineProp = headlineVarsTextProp;
  }

  void setTerGraphProp(GraphProperties terGraphProp) {
    this.terGraphProp = terGraphProp;
  }
}

// Some classes and a interface that allows to define "clean action" that are
// executed in a certain valut iteration code step
