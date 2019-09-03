package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Graph;
import algoanim.primitives.Point;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * Generator for Markov Chain Clustering (MCL) Algorithm.
 * 
 * @author Johannes Simon <johannes.simon@gmail.com>
 */
public class MCL implements Generator {
  Language                     m_l;

  // Transition matrix T
  double[][]                   m_t;
  // and its AnimalScript representation
  DoubleMatrix                 m_tView;

  // Input graph
  Graph                        m_g;
  Node[]                       m_nodes;
  String[]                     m_labels;
  private GraphProperties      G_Props;
  private MatrixProperties     T_Props;
  private SourceCodeProperties Source_Props;
  private SourceCodeProperties Explanation_Props;
  private TextProperties       Header_Props;
  private TextProperties       Counter_Props;
  Text                         m_counterAdd;
  Text                         m_counterMult;
  int                          m_counterAddValue  = 0;
  int                          m_counterMultValue = 0;

  // Explanation text
  SourceCode                   m_exp;
  // Pseudo code
  SourceCode                   m_source;

  // Basic layout
  final static Coordinates     POS_SOURCE         = new Coordinates(800, 50);
  final static Coordinates     POS_COUNTER1       = new Coordinates(
                                                      POS_SOURCE.getX(),
                                                      POS_SOURCE.getY() + 200);
  final static Coordinates     POS_COUNTER2       = new Coordinates(
                                                      POS_COUNTER1.getX(),
                                                      POS_COUNTER1.getY() + 20);
  final static Coordinates     POS_T              = new Coordinates(
                                                      POS_COUNTER2.getX(),
                                                      POS_COUNTER2.getY() + 30);
  final static Coordinates     POS_EXP            = new Coordinates(50, 50);
  final static Coordinates     POS_G              = new Coordinates(
                                                      POS_EXP.getX(),
                                                      POS_EXP.getY() + 150);

  // Precision of displayed floating point numbers
  static final int             NUM_PREC           = 2;

  /**
   * Initializes this generator. By calling this method, the same instance of
   * this generator can be re-used several times.
   */
  public void init() {
    m_g = null;
    m_nodes = null;
    m_labels = null;
    m_exp = null;
    m_source = null;
    m_t = null;
    m_tView = null;
    m_l = new AnimalScript("Markov Chain Clustering (MCL) Algorithm",
        "Johannes Simon", 1280, 1024);
    m_l.setStepMode(true);
    m_l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    G_Props = (GraphProperties) props.getPropertiesByName("G_Props");
    T_Props = (MatrixProperties) props.getPropertiesByName("T_Props");
    Header_Props = (TextProperties) props.getPropertiesByName("Header_Props");
    Font headerFont = (Font) Header_Props
        .get(AnimationPropertiesKeys.FONT_PROPERTY);
    Header_Props.set(AnimationPropertiesKeys.FONT_PROPERTY,
        headerFont.deriveFont(20.0f));
    Counter_Props = (TextProperties) props.getPropertiesByName("Counter_Props");
    Explanation_Props = (SourceCodeProperties) props
        .getPropertiesByName("Explanation_Props");
    Font expFont = (Font) Explanation_Props
        .get(AnimationPropertiesKeys.FONT_PROPERTY);
    Explanation_Props.set(AnimationPropertiesKeys.FONT_PROPERTY,
        expFont.deriveFont(16.0f));
    Source_Props = (SourceCodeProperties) props
        .getPropertiesByName("Source_Props");
    Font sourceFont = (Font) Source_Props
        .get(AnimationPropertiesKeys.FONT_PROPERTY);
    Source_Props.set(AnimationPropertiesKeys.FONT_PROPERTY,
        sourceFont.deriveFont(18.0f));
    Graph g = (Graph) primitives.get("graph");
    normalizeGraph(g);

    int n = g.getSize();
    m_nodes = new Node[n];
    m_labels = new String[n];
    for (int i = 0; i < n; i++) {
      m_nodes[i] = g.getNode(i);
      m_labels[i] = g.getNodeLabel(i);
    }

    m_g = m_l.newGraph("G1", g.getAdjacencyMatrix(), m_nodes, m_labels, null,
        G_Props);
    m_g.moveTo(null, null, POS_G, null, null);
    findClusters();

    m_l.finalizeGeneration();

    return m_l.toString();
  }

  public String getName() {
    return "Markov Chain Clustering (MCL) Algorithm";
  }

  public String getAlgorithmName() {
    return "Markov Chain Clustering";
  }

  public String getAnimationAuthor() {
    return "Johannes Simon";
  }

  public String getDescription() {
    return "Markov Chain Clustering is used to find clusters in a graph."
        + "\n"
        + "It is based on the idea of random walks: In a graph, if you walk a small number of steps,"
        + "\n"
        + "you are more likely to stay within a cluster than to move into another cluster."
        + "\n"
        + "To obtain the probabilities of walking from one node to another, MCL uses"
        + "\n"
        + "Markov chains. This is a simple probability model that assumes that walking from X to Y"
        + "\n"
        + "depends solely on X and no other circumstances, like time (or the weather).";
  }

  public String getCodeExample() {
    return "function MCL(G, e, r)" + "\n" + "   G = G with self-loops added"
        + "\n" + "   T = transition probabilities of G" + "\n"
        + "   Until T converges, do" + "\n" + "      T = T^e" + "\n"
        + "      T = inflate(T, r)" + "\n" + "   Determine clusters from T";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * Finds clusters in m_g using MCL algorithm.
   */
  public void findClusters() {
    showHeader();
    showIntroduction();
    showPseudoCode();
    showInitialization();
    showTransitionMatrix();

    // *** Random Walk Explanation ***
    m_source.unhighlight(2);
    m_source.highlight(3);
    clearExplanation();
    addExplanationLine("We now simulate a number of random walks to identify the clusters.");
    m_l.nextStep();

    showFirstExpansionStep();
    showFirstInflationStep();

    // / *** Iterate until convergance of T ***
    clearExplanation();
    addExplanationLine("We now continue this iteration until T converges.");
    m_source.unhighlight(5);
    m_source.highlight(3);
    m_l.nextStep();

    m_source.unhighlight(3);
    double[][] lastT;
    do {
      lastT = roundDoubleMatrix(m_t);

      // *** Calculate T^2 ***
      m_source.highlight(4);
      expansionStep();
      m_source.unhighlight(4);

      // / *** Inflate ***
      m_source.highlight(5);
      inflationStep(false);
      m_source.unhighlight(5);
    } while (!matricesEqual(lastT, m_t));

    showClusters();
    m_source.unhighlight(6);

    clearExplanation();
    addExplanationLine("All the multiplications and additions required for this algorithm stem from expanding");
    addExplanationLine("and inflating T. Let n be the number of nodes in G. While matrix multiplication");
    addExplanationLine("(expansion step) is of complexity O(n^3), inflating T is of complexity O(n^2),");
    addExplanationLine("therefore each step requires O(n^3) operations.");
    m_l.nextStep("Algorithm complexity");

    clearExplanation();
    addExplanationLine("And this is it, we're done! :-)");
  }

  private void showHeader() {
    Text header = m_l.newText(new Coordinates(200, 30),
        "Markov Chain Clustering (MCL) Algorithm", "text", null, Header_Props);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);

    m_l.newRect(new Offset(-5, -5, header, "NW"),
        new Offset(5, 5, header, "SE"), "headerRect", null, rectProps);
  }

  private void showIntroduction() {
    // *** INTRO ***
    addExplanationLine("Markov Chain Clustering is used to find clusters in a graph.");
    addExplanationLine("It is based on the idea of random walks: In a graph, if you walk a small number of steps,");
    addExplanationLine("you are more likely to stay within a cluster than to move into another cluster.");
    addExplanationLine("To obtain the probabilities of walking from one node to another, MCL uses");
    addExplanationLine("Markov chains. This is a simple probability model that assumes that walking from X to Y");
    addExplanationLine("depends solely on X and no other circumstances, like time (or the weather).");
    m_l.nextStep("Introduction");

    // *** Graph Explanation ***
    clearExplanation();
    addExplanationLine("In this graph we now want to find clusters.");
    addExplanationLine("What clusters do you think the algorithm should find?");
    m_l.nextStep();

    // *** Graph to Probabilities ***
    m_g.hide();
    clearExplanation();
    addExplanationLine("To better explain how the MCL algorithm works, we here show the directed");
    addExplanationLine("version of the undirected input graph, which is entirely equivalent.");
    G_Props.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    m_g = m_l.newGraph("G2", m_g.getAdjacencyMatrix(), m_nodes, m_labels, null,
        G_Props);
    m_g.moveTo(null, null, POS_G, null, null);
    m_l.nextStep();

    // *** Markov chain probabilities ***
    m_g.hide();
    clearExplanation();
    addExplanationLine("We now mark each edge with the probability of randomly walking from X to Y.");
    addExplanationLine("The transition probabilities you see here correspond to a Markov model we mentioned earlier.");
    m_t = getMarkovTransitionMatrix(m_g);
    G_Props.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    updateGraph();
    m_l.nextStep();
  }

  private void showPseudoCode() {
    // *** MCL Pseudo Code ***
    clearExplanation();
    addExplanationLine("Here's the pseudocode of the MCL algorithm. To determine cluster labels ");
    addExplanationLine("for a given input graph, it takes two parameters: e and r. You will later see ");
    addExplanationLine("what these are for. Here we assume e=r=2, which simplifies the calculations a lot.");
    addExplanationLine("The numbers above T indicate how many atomic operations have been performed on T.");

    updateCounters();

    m_source = m_l.newSourceCode(POS_SOURCE, "intro", null, Source_Props);
    m_source.addCodeLine("function MCL(G, e, r)", null, 0, null);
    m_source.addCodeLine("G = G with self-loops added", null, 1, null);
    m_source.addCodeLine("T = transition probabilities of G", null, 1, null);
    m_source.addCodeLine("Until T converges, do", null, 1, null);
    m_source.addCodeLine("T = T^e // e = 2", null, 2, null);
    m_source.addCodeLine("T = inflate(T, r) // r = 2", null, 2, null);
    m_source.addCodeLine("Determine clusters from T", null, 1, null);
    m_source.highlight(0);
    m_l.nextStep("MCL Pseudocode");
  }

  private void showInitialization() {
    // *** Add self-loops to graph ***
    m_source.unhighlight(0);
    m_source.highlight(1);
    clearExplanation();
    addExplanationLine("In the first step of the MCL algorithm, we add self-loops to every node.");
    addExplanationLine("This resembles the fact that we can stay on a node and don't have to move onto another.");
    addExplanationLine("Note how this changed the transition probabilities.");

    int[][] adjM = m_g.getAdjacencyMatrix();
    for (int i = 0; i < m_t.length; i++) {
      adjM[i][i] = 1;
    }
    m_t = getMarkovTransitionMatrix(m_g);
    updateGraph();
    m_l.nextStep("Initialization");
  }

  private void showTransitionMatrix() {
    // *** Show Table ***
    m_source.unhighlight(1);
    m_source.highlight(2);
    clearExplanation();
    addExplanationLine("Here's T with the transition probabilities we already marked in the graph.");
    updateT();
    m_l.nextStep("Transition matrix");

    // *** Demonstrate meaning of T entries ***
    clearExplanation();
    // Find out node from A with P(A->B) > 0
    int exampleOutNode = getExampleOutNode(0);
    int outDegree = getOutDegree(m_g, 0);
    String exampleOutNodeLabel = m_g.getNodeLabel(exampleOutNode);
    addExplanationLine("For example, walking from A to " + exampleOutNodeLabel
        + " randomly has a probability of " + "1/" + outDegree + ",");
    addExplanationLine("as it is one of " + outDegree
        + " possibilities from A.");
    m_g.highlightEdge(0, exampleOutNode, null, new TicksTiming(10));
    m_g.highlightNode(0, null, new TicksTiming(10));
    m_g.highlightNode(exampleOutNode, null, new TicksTiming(10));
    m_tView.highlightCell(exampleOutNode, 0, null, new TicksTiming(10));
    m_l.nextStep();
    m_g.unhighlightEdge(0, exampleOutNode, null, new TicksTiming(10));
    m_g.unhighlightNode(0, null, new TicksTiming(10));
    m_g.unhighlightNode(exampleOutNode, null, new TicksTiming(10));
    m_tView.unhighlightCell(exampleOutNode, 0, null, null);
  }

  private void showFirstExpansionStep() {
    // *** Explanation of what T^2 means ***
    m_source.unhighlight(3);
    m_source.highlight(4);
    clearExplanation();
    addExplanationLine("T^2 are the probabilities of going from node X to node Y in 2 steps, following transition");
    addExplanationLine("probabilities from T in each of the 2 steps. T^2 is simply a short form of T*T, i.e. multiplying");
    addExplanationLine("T with itself. More generally, T^e are the probabilities of walking from node X to node Y");
    addExplanationLine("within e steps, given transitions in each step follow probabilities from T.");
    m_l.nextStep("Explanation of expansion step");

    // *** Explanation of what T^2 means ***
    // Find out node from A with P(A->B) > 0
    int exampleOutNode = getExampleOutNode(0);
    listWalkPossibilities(0, exampleOutNode);

    // *** Calculate T^2 ***
    expansionStep();

    MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("q2");
    q2.setPrompt("For a given number of steps n, what do you think will the possiblity to randomly walk from X to Y, P(X->Y), approach as n approaches infinity?\n"
        + "Hint: In other words, what probabilities P(X->Y) for two nodes X and Y would T hold if you multiplied it with itself infinitely often?");
    String wrongAnswerNote = "Oops, that is incorrect. See the following slide for the answer.";
    q2.addAnswer("P(Y->X)", 1, wrongAnswerNote);
    q2.addAnswer("P(X)", 1, wrongAnswerNote);
    q2.addAnswer("P(Y)", 1, "That is correct!");
    q2.addAnswer("0", 1, wrongAnswerNote);
    q2.addAnswer("1", 1, wrongAnswerNote);
    m_l.addMCQuestion(q2);

    // *** Explanation of what T^2 means ***
    clearExplanation();
    addExplanationLine("Probably the graph is getting a bit messy by now. Let's simplify it a bit to");
    addExplanationLine("keep an overview over the effects of the iterations.");
    G_Props.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false);
    G_Props.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
    updateGraph();
    m_l.nextStep();

    clearExplanation();
    addExplanationLine("What we just simulated was a random walk of 2 steps. If we had an infinite");
    addExplanationLine("number of steps, P(X->Y) will approach P(Y), i.e. the probability of");
    addExplanationLine("ending up at Y would be independent of X.");
    m_l.nextStep("Interpretation of T");

    clearExplanation();
    addExplanationLine("However, we want to use P(X->Y) (i.e., T) to determine which node X \\\"belongs\\\" to.");
    addExplanationLine("Therefore, we need to use a relatively small number of steps to stay within its cluster.");
    addExplanationLine("Ideally, the entry with the highest value in each column represents the cluster label of the node.");
    m_l.nextStep();

    clearExplanation();
    addExplanationLine("Obviously, we don't have a winner yet for all nodes. We don't want to increase the");
    addExplanationLine("number of steps in each path though, so what can we do?");
    addExplanationLine("Solution: We increase contrast between the current probabilities and continue then!");
    m_l.nextStep();
  }

  private void showFirstInflationStep() {
    clearExplanation();
    m_source.unhighlight(4);
    m_source.highlight(5);
    addExplanationLine("For this we raise each column element-wise to the power of r and then");
    addExplanationLine("normalize the values in this column again. Normalizing is done by dividing each");
    addExplanationLine("element of the column by the sum of all column values.");
    addExplanationLine("This step is called inflation of T.");
    m_l.nextStep("Explanation of inflation step");

    // / *** Inflate ***
    inflationStep(true);
  }

  private void expansionStep() {
    clearExplanation();
    addExplanationLine("Calculating T^2 yields this matrix.");
    addExplanationLine("Watch out for possible changes in the paths between nodes.");
    addExplanationLine("No path between two nodes X and Y means P(X->Y) ~= 0.");

    m_t = matrixMult(m_t, m_t);
    updateCounters();
    updateT();
    updateGraph();
    m_l.nextStep();
  }

  private void inflationStep(boolean showSubSteps) {
    int n = m_t.length;
    // i = from
    for (int i = 0; i < n; i++) {
      double colSum = 0.0;
      // j = to
      for (int j = 0; j < n; j++) {
        m_t[j][i] = m_t[j][i] * m_t[j][i];
        m_counterMultValue++;
        colSum += m_t[j][i];
        if (j > 0)
          m_counterAddValue++;
        if (showSubSteps) {
          m_tView.highlightCell(j, i, null, new TicksTiming(5));
        }
      }
      if (showSubSteps) {
        updateCounters();
        clearExplanation();
        addExplanationLine("Raise the elements of column " + i
            + " to the power of 2.");
        updateT();
        m_l.nextStep();
      }
      // normalize column
      for (int j = 0; j < n; j++) {
        m_t[j][i] = m_t[j][i] / colSum;
        m_counterMultValue++;
      }
      if (showSubSteps) {
        updateCounters();
        clearExplanation();
        addExplanationLine("Then normalize it by dividing each element by the sum of the column ("
            + roundDouble(colSum) + ")");
        updateT();
        m_l.nextStep();
        for (int j = 0; j < n; j++) {
          m_tView.unhighlightCell(j, i, null, null);
        }
      }
    }

    updateGraph();

    if (!showSubSteps) {
      updateCounters();
      clearExplanation();
      addExplanationLine("Inflating T with the power of 2 yields this.");
      addExplanationLine("Watch out for possible changes in the paths between nodes.");
      addExplanationLine("No path between two nodes X and Y means P(X->Y) ~= 0.");
      updateT();
      m_l.nextStep();
    }
  }

  int __gCounter = 0;

  private void updateGraph() {
    if (m_g != null) {
      m_g.hide();
    }

    int[][] adjMatrix = getAdjacencyMatrix(m_t);
    m_g = m_l.newGraph("_G" + __gCounter, adjMatrix, m_nodes, m_labels, null,
        G_Props);
    m_g.moveTo(null, null, POS_G, null, null);
    double[][] probs = roundDoubleMatrix(m_t);
    setEdgeWeights(m_g, probs);
    // highlightClusterEdges(m_g, probs);
    __gCounter++;
  }

  /**
   * Updates m_tView with rounded probabilities from m_t.
   */
  private void updateT() {
    double[][] probsRounded = roundDoubleMatrix(m_t);
    if (m_tView == null) {
      // MatrixProperties mp = new MatrixProperties("TProps");
      // mp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
      // mp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
      // mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
      m_tView = m_l.newDoubleMatrix(POS_T, probsRounded, "T", null, T_Props);

      /*
       * TwoValueCounter counter = m_l.newCounter(m_tView); CounterProperties cp
       * = new CounterProperties();
       * cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
       * cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); TwoValueView
       * view = m_l.newCounterView(counter, new Coordinates(100, 100), null,
       * true, true);
       */
    }
    int n = m_t.length;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        m_tView.put(i, j, probsRounded[i][j], null, new TicksTiming(10));
      }
    }
  }

  private void updateCounters() {
    if (m_counterAdd == null) {
      m_counterAdd = m_l.newText(POS_COUNTER1, "Additions: "
          + m_counterAddValue, "counterAdd", null, Counter_Props);
      m_counterMult = m_l.newText(POS_COUNTER2, "Multiplications: "
          + m_counterMultValue, "counterMult", null, Counter_Props);
    } else {
      m_counterAdd.setText("Additions: " + m_counterAddValue, null, null);
      m_counterMult.setText("Multiplications: " + m_counterMultValue, null,
          null);
    }
  }

  private void showQuestion3() {
    double[][] m_squared = matrixMult(m_t, m_t);

    int a = m_squared.length - 1;
    int b = m_squared.length - 2;
    String labelA = m_g.getNodeLabel(a);
    String labelB = m_g.getNodeLabel(b);

    double prob = roundDouble(m_squared[b][a]);

    FillInBlanksQuestionModel q3 = new FillInBlanksQuestionModel("q3");
    // Bug in animal: period causes weird problems
    String answer = Double.toString(prob).replace(".", ",");
    q3.setPrompt("What is the possibility of randomly walking from "
        + labelA
        + " to "
        + labelB
        + " in 2 steps, given the current transition probabilities? (Use comma as decimal separator, round to 2 decimal places, therefore Answer = x,xx)");
    q3.addAnswer(answer, 1, "That is correct!");
    m_l.addFIBQuestion(q3);
  }

  /**
   * Presents clusters extracted from T.
   */
  private void showClusters() {
    HashMap<Cluster, Set<Integer>> clusterLabels = getClusterLabels(m_t);
    int numClusters = clusterLabels.size();
    clearExplanation();
    addExplanationLine("As you noticed in the last 2 steps, T has stabilized. This means the");
    addExplanationLine("MCL algorithm is done. It discovered " + numClusters
        + " cluster(s):");
    m_source.highlight(6);
    m_l.nextStep("Determining clusters from T");

    // Determine nodes in each cluster
    for (Cluster cluster : clusterLabels.keySet()) {
      Set<Integer> clusterNodes = clusterLabels.get(cluster);
      String label = cluster.label;

      clearExplanation();
      addExplanationLine("Cluster " + label + ", containing "
          + clusterNodes.size() + " nodes:");
      String nodeList = "";
      for (Integer node : clusterNodes) {
        String nodeLabel = m_g.getNodeLabel(node);
        if (!nodeList.isEmpty())
          nodeList += ", ";
        nodeList += nodeLabel;
        m_g.highlightNode(node, null, new TicksTiming(5));
        for (int attractorNode : cluster.attractorNodes)
          m_tView.highlightCell(attractorNode, node, null, new TicksTiming(5));
      }
      addExplanationLine(nodeList);
      if (cluster.attractorNodes.size() > 1) {
        addExplanationLine("Note that this is a cluster containing multiple so-called attractor nodes ("
            + cluster.label + "). The probability");
        addExplanationLine("to go from the cluster nodes to these attractor nodes is equal, therefore they form one cluster.");
      }
      m_l.nextStep();
      for (Integer node : clusterNodes) {
        m_g.unhighlightNode(node, null, null);
        for (int attractorNode : cluster.attractorNodes)
          m_tView.unhighlightCell(attractorNode, node, null, null);
      }
    }
  }

  private int getExampleOutNode(int node) {
    int exampleOutNode = -1;
    int[] outNodes = m_g.getEdgesForNode(node);
    // Skip A itself as neighbor
    for (int i = 0; i < outNodes.length; i++) {
      if (i != node && outNodes[i] == 1) {
        exampleOutNode = i;
        break;
      }
    }
    return exampleOutNode;
  }

  private void normalizeGraph(Graph g) {
    int[][] adjMatrix = g.getAdjacencyMatrix();
    for (int i = 0; i < adjMatrix.length; i++) {
      for (int j = 0; j < adjMatrix.length; j++) {
        if (adjMatrix[i][j] > 0) {
          adjMatrix[i][j] = 1;
          adjMatrix[j][i] = 1;
        }
      }
    }
    g.setAdjacencyMatrix(adjMatrix);
  }

  private int getOutDegree(Graph g, int node) {
    int[] outNodes = g.getEdgesForNode(node);
    int outDegree = 0;
    for (int i : outNodes) {
      outDegree += i;
    }
    return outDegree;
  }

  /**
   * Determines transition probabilities of G using Markov assumptions.
   */
  private double[][] getMarkovTransitionMatrix(Graph g) {
    int n = g.getSize();
    double[][] result = new double[n][n];
    // i = from
    for (int i = 0; i < g.getSize(); i++) {
      int[] outNodes = g.getEdgesForNode(i);
      int outDegree = getOutDegree(g, i);
      // j = to
      for (int j = 0; j < outNodes.length; j++) {
        result[j][i] = (double) outNodes[j] / outDegree;
      }
    }

    return result;
  }

  private int[][] getAdjacencyMatrix(double[][] m) {
    int[][] result = new int[m.length][m.length];
    for (int i = 0; i < m.length; i++) {
      for (int j = 0; j < m.length; j++) {
        result[i][j] = (m[j][i] >= Math.pow(10.0, -NUM_PREC)) ? 1 : 0;
      }
    }
    return result;
  }

  class Cluster {
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((label == null) ? 0 : label.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Cluster other = (Cluster) obj;
      if (!getOuterType().equals(other.getOuterType()))
        return false;
      if (label == null) {
        if (other.label != null)
          return false;
      } else if (!label.equals(other.label))
        return false;
      return true;
    }

    String       label;
    Set<Integer> attractorNodes;

    private MCL getOuterType() {
      return MCL.this;
    }
  }

  private String nodeSetToString(Set<Integer> nodeSet) {
    String result = "";

    for (int node : nodeSet) {
      if (!result.isEmpty())
        result += ",";
      result += m_g.getNodeLabel(node);
    }

    return "{" + result + "}";
  }

  private HashMap<Cluster, Set<Integer>> getClusterLabels(double[][] probs) {
    double[][] roundedProbs = roundDoubleMatrix(probs);
    HashMap<Cluster, Set<Integer>> clusterLabels = new HashMap<Cluster, Set<Integer>>();

    // i = from
    for (int i = 0; i < roundedProbs.length; i++) {
      Set<Integer> attractorSet = new TreeSet<Integer>();
      // j = to
      for (int j = 0; j < roundedProbs.length; j++) {
        if (roundedProbs[j][i] > 0) {
          attractorSet.add(j);
        }
      }
      Cluster c = new Cluster();
      c.attractorNodes = attractorSet;
      c.label = nodeSetToString(attractorSet);
      if (!clusterLabels.containsKey(c)) {
        clusterLabels.put(c, new TreeSet<Integer>());
      }
      clusterLabels.get(c).add(i);
    }

    return clusterLabels;
  }

  /**
   * private void highlightClusterEdges(Graph g, double[][] probs) { int[]
   * clusterLabels = getClusterLabels(probs); for (int i = 0; i <
   * clusterLabels.length; i++) { g.highlightEdge(i, clusterLabels[i], null, new
   * TicksTiming(5)); } }
   */

  private void clearExplanation() {
    m_exp.hide();
    m_exp = null;
  }

  private void addExplanationLine(String line) {
    if (m_exp == null) {
      m_exp = m_l.newSourceCode(POS_EXP, "exp", null, Explanation_Props);
    }
    m_exp.addCodeLine(line, null, 0, null);
  }

  private void setEdgeWeights(Graph g, double[][] probs) {
    for (int i = 0; i < probs.length; i++) {
      for (int j = 0; j < probs.length; j++) {
        if (probs[j][i] > 0.0) {
          g.setEdgeWeight(i, j, Double.toString(probs[j][i]), null,
              new TicksTiming(5));
        }
      }
    }
  }

  private void listWalkPossibilities(int a, int b) {
    String labelA = m_g.getNodeLabel(a);
    String labelB = m_g.getNodeLabel(b);

    List<Integer> intermediateNodes = new LinkedList<Integer>();
    for (int i = 0; i < m_tView.getNrCols(); i++) {
      if (m_tView.getElement(i, a) > 0 && m_tView.getElement(a, i) > 0)
        intermediateNodes.add(i);
    }
    m_g.highlightNode(a, null, new TicksTiming(10));
    m_g.highlightNode(b, null, new TicksTiming(10));
    m_tView.highlightCell(b, a, null, new TicksTiming(10));

    String pCalc = "";
    String pCalcNumbers = "";
    double pTotal = 0;

    int numIN = intermediateNodes.size();

    MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("q1");
    q1.setPrompt("Here's a short warmup question before we continue: How many possibilities do you think are there to get from "
        + labelA + " to " + labelB + " within 2 steps?");
    q1.addAnswer(Integer.toString(numIN), 1, "That is correct!");
    String wrongAnswerNote = "Oops, there's only " + numIN
        + " possible paths. Continue the animation to see why.";
    q1.addAnswer(Integer.toString(numIN + 1), 0, wrongAnswerNote);
    q1.addAnswer("1", 0, wrongAnswerNote);
    m_l.addMCQuestion(q1);

    clearExplanation();
    addExplanationLine("For example, to get from " + labelA + " to " + labelB
        + " within 2 steps, we have the following possibilities,");
    addExplanationLine("corresponding to what multiplication of T with itself yields for element T("
        + b + ", " + a + ") = P(" + labelA + "->" + labelB + ").");
    m_l.nextStep();

    for (Integer i : intermediateNodes) {
      String labelI = m_g.getNodeLabel(i);
      clearExplanation();
      addExplanationLine("P(" + labelA + "->" + labelI + "->" + labelB + ") = "
          + "P(" + labelA + "->" + labelI + ") * P(" + labelI + "->" + labelB
          + ") = " + m_tView.getElement(i, a) + "*" + m_tView.getElement(b, i)
          + " = " + roundDouble(m_t[a][i] * m_t[i][b]));
      if (!pCalc.isEmpty()) {
        pCalc += " + ";
        pCalcNumbers += " + ";
      }
      pCalc += "P(" + labelA + "->" + labelI + "->" + labelB + ")";
      pCalcNumbers += roundDouble(m_t[a][i] * m_t[i][b]);
      pTotal += m_t[a][i] * m_t[i][b];
      m_g.highlightEdge(a, i, null, new TicksTiming(10));
      m_g.highlightEdge(i, b, null, new TicksTiming(10));
      m_tView.highlightCell(i, a, null, new TicksTiming(10));
      m_tView.highlightCell(b, i, null, new TicksTiming(10));
      m_l.nextStep();
      m_g.unhighlightEdge(a, i, null, null);
      m_g.unhighlightEdge(i, b, null, null);
      m_tView.unhighlightCell(i, a, null, null);
      m_tView.unhighlightCell(b, i, null, null);
    }

    clearExplanation();
    addExplanationLine("In total, the probability to go from " + labelA
        + " to " + labelB + " within 2 steps, ");
    addExplanationLine("P(" + labelA + "->*->" + labelB + ") = " + pCalc + " =");
    addExplanationLine(pCalcNumbers + " = " + roundDouble(pTotal));
    showQuestion3();
    m_l.nextStep();

    m_g.unhighlightNode(a, null, null);
    m_g.unhighlightNode(b, null, null);
    m_tView.unhighlightCell(b, a, null, null);
  }

  // *** MATRIX HELPER METHODS ***

  private double[][] matrixMult(double[][] m1, double[][] m2) {
    int n = m1.length;
    double[][] result = new double[n][n];
    // matrix multiplication
    // i = row
    for (int i = 0; i < n; i++) {
      // j = col
      for (int j = 0; j < n; j++) {
        double d = 0;
        for (int k = 0; k < n; k++) {
          d += m_t[i][k] * m_t[k][j];
          m_counterMultValue++;
          if (k > 0)
            m_counterAddValue++;
        }
        result[i][j] = d;
      }
    }
    return result;
  }

  private boolean matricesEqual(double[][] m1, double[][] m2) {
    int n = m1.length;
    double[][] roundedM1 = roundDoubleMatrix(m1);
    double[][] roundedM2 = roundDoubleMatrix(m2);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (roundedM1[i][j] != roundedM2[i][j])
          return false;
      }
    }
    return true;
  }

  private double roundDouble(double d) {
    int m = (int) Math.pow(10, NUM_PREC);
    return Math.round(d * m) / (double) m;
  }

  private double[][] roundDoubleMatrix(double[][] d) {
    int l = d.length;
    double[][] result = new double[l][l];

    for (int i = 0; i < l; i++) {
      for (int j = 0; j < l; j++) {
        result[i][j] = roundDouble(d[i][j]);
      }
    }
    return result;
  }

  public static void main(String[] argv) throws FileNotFoundException {
    MCL mcl = new MCL();
    mcl.init();
    Language l = mcl.m_l;

    int[][] adjacencyMatrix = new int[][] { { 0, 1, 1, 0, 0, 0 },
        { 1, 0, 1, 0, 0, 0 }, { 1, 1, 0, 1, 0, 0 }, { 0, 0, 1, 0, 1, 1 },
        { 0, 0, 0, 1, 0, 1 }, { 0, 0, 0, 1, 1, 0 } };
    Point graphPos = l.newPoint(POS_G, "graphPos", null, new PointProperties());
    Node[] nodes = new Node[] { new Offset(0, 0, graphPos, "SW"),
        new Offset(0, 200, graphPos, "SW"),
        new Offset(200, 100, graphPos, "SW"),
        new Offset(400, 100, graphPos, "SW"),
        new Offset(600, 200, graphPos, "SW"),
        new Offset(600, 0, graphPos, "SW") };
    String[] labels = new String[] { "A", "B", "C", "D", "E", "F" };

    AnimationPropertiesContainer aps = new AnimationPropertiesContainer();
    GraphProperties gProps = new GraphProperties("G_Props");
    gProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    gProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    aps.add(gProps);

    MatrixProperties tProps = new MatrixProperties("T_Props");
    tProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    tProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
    tProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    aps.add(tProps);

    SourceCodeProperties sourceProps = new SourceCodeProperties("Source_Props");
    sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    aps.add(sourceProps);

    SourceCodeProperties expProps = new SourceCodeProperties(
        "Explanation_Props");
    expProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    aps.add(expProps);

    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    aps.add(headerProps);

    TextProperties counterProps = new TextProperties();
    counterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 12));
    aps.add(counterProps);

    Hashtable<String, Object> primitives = new Hashtable<String, Object>();

    GraphProperties graphProps = new GraphProperties();
    Graph g = l.newGraph("G", adjacencyMatrix, nodes, labels, null, graphProps);
    g.hide();
    primitives.put("graph", g);

    PrintWriter out = new PrintWriter(argv[0]);
    out.println(mcl.generate(aps, primitives));
    out.close();
  }
}