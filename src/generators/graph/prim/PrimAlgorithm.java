package generators.graph.prim;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class PrimAlgorithm implements Generator {

  private Language             lang;
  private GraphProperties      graphProps;
  private TextProperties       textProps;
  private SourceCodeProperties scProps;

  static int                   mincost = 0;
  static int                   k, l;

  public PrimAlgorithm() {

  }

  public void init() {
    lang = new AnimalScript("Prim Algorithm", "Xiaofan Fan", 640, 480);
    lang.setStepMode(true);

    /*
     * textProps = new TextProperties();
     * textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
     * textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
     * Font("Monospaced", Font.BOLD, 18));
     * 
     * scProps = new SourceCodeProperties();
     * scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
     * scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
     * Font.BOLD, 13));
     * scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
     * scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     */

    graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.black); // color
                                                                             // red
    graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.black);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    graphProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
  }

  private static final String DESCRIPTION = "Prim's algorithm is an algorithm  that finds a minimum spanning tree (MST) for a connected weighted undirected graph. "
                                              + "The main idea of Prim's algorithm is to find the shortest path in a given graph. "
                                              + "Prim's algorithm has the property that the edges in the set A always form a single tree. "
                                              + "<br>We begin with some vertex v in a given graph G =(V, E), defining the initial set of vertices A. "
                                              + "Then, in each iteration, we choose a minimum-weight edge (u, v), "
                                              + "connecting a vertex v in the set A to the vertex u  outside of set A. "
                                              + "Then vertex u is brought in to set A. "
                                              + "This process is repeated until a spanning tree is formed. "
                                              + "<br>The important fact about MSTs is we always choose the smallest-weight edge joining a vertex inside set A to the one outside the set A. "
                                              + "The implication of this fact is that it adds only edges that are safe for A; "
                                              + "therefore when the algorithm terminates, the edges in set A form a MST. ";

  private static final String SOURCE_CODE = "public void prim(int[][] graph, int n, int[][] t,int[] near)"
                                              + "\n{"
                                              + "\n	int[] kl = new int[2];"
                                              + "\n	kl = getMinKL(graph,n);//we get the smallest-weight edge in the graph."
                                              + "\n	int k = kl[0];"
                                              + "\n	int l = kl[1];"
                                              + "\n	int mincost = graph[k][l];"
                                              + "\n	t[0][0] = l;"
                                              + "\n	t[0][1] = k;"
                                              + "\n	for(int i = 0; i < n; i++)"
                                              + "\n		near[i] = ( graph[i][l] < graph[i][k]) ? l : k;"
                                              + "\n	near[k] = near[l] =	1001;"
                                              + "\n	for(int i =1; i < n-1; i++){"
                                              + "\n		int j = getMin(graph,near,n);// we find the next node."
                                              + "\n		t[i][0] = j;"
                                              + "\n		t[i][1] = near[j];"
                                              + "\n		mincost = mincost + graph[j][near[j]];"
                                              + "\n		near[j] =1001;"
                                              + "\n		for (int k = 0; k < n; k++)"
                                              + "\n			if((near[k] != 1001) && graph[k][ near[k] ] > graph[k][j] )"
                                              + "\n				near[k] = j;"
                                              + "\n	}"
                                              + "\n}";

  public void start(int[][] g, int x, int[][] y, int[] z) {
    lang.newText(new Coordinates(15, 30), "Prim Algorithm", "text", null,
        textProps);
    String[] labels = { "0", "1", "2", "3", "4", "5" };
    Node[] b = new Node[x];
    b[0] = new Coordinates(325, 150);
    b[1] = new Coordinates(200, 250);
    b[2] = new Coordinates(325, 300);
    b[3] = new Coordinates(450, 250);
    b[4] = new Coordinates(200, 450);
    b[5] = new Coordinates(450, 450);

    Graph prim = lang.newGraph("prim", g, b, labels, null, graphProps);

    CheckpointUtils.checkpointEvent(this, "initial", new Variable("x", x));// ///////////////////////////

    lang.nextStep();
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < x; j++) {
        if ((i != j) && (i < j)) {

          g[j][i] = g[i][j];
          if (g[i][j] == 0)
            g[j][i] = g[i][j] = 7001;
        }
        if (i == j)
          g[i][j] = 7001;
      }
    }
    SourceCode sc = lang.newSourceCode(new Coordinates(700, 100), "sourceCode",
        null, scProps);
    sc.addCodeLine(
        "public void prim(int[][] graph, int n, int[][] t,int[] near){", null,
        0, null); // line 0
    sc.addCodeLine("int[] kl = new int[2];", null, 1, null); // 1
    sc.addCodeLine("kl = getMinKL(graph,n);//we get the smallest-weight edge.",
        null, 1, null); // 2
    sc.addCodeLine("int k = kl[0];", null, 1, null); // 3
    sc.addCodeLine("int l = kl[1];", null, 1, null); // 4
    sc.addCodeLine("int mincost = graph[k][l];", null, 1, null); // 5
    sc.addCodeLine("t[0][0] = l;", null, 1, null);// 6
    sc.addCodeLine("t[0][1] = k;", null, 1, null);// 7
    sc.addCodeLine("for(int i=0; i<n; i++)", null, 1, null);// 8
    sc.addCodeLine("near[i] = (graph[i][l]<graph[i][k])?l:k;", null, 2, null);// 9
    sc.addCodeLine("near[k] = near[l] =	1001;", null, 1, null);// 10
    sc.addCodeLine("for(int i=1; i<n-1; i++){", null, 1, null);// 11
    sc.addCodeLine("int j = getMin(graph,near,n);// we find the next node.",
        null, 2, null);// 12
    sc.addCodeLine(
        "t[i][0] = j;// we find the startnode of the next smallest-weight edge in the rest graph.",
        null, 2, null);// 13
    sc.addCodeLine(
        "t[i][1] = near[j];// we find the endnode of this new smallest-weight edge in the rest graph.",
        null, 2, null);// 14
    sc.addCodeLine("mincost = mincost+graph[j][near[j]];", null, 2, null);// 15
    sc.addCodeLine("near[j] =1001;", null, 2, null);// 16
    sc.addCodeLine("for (int k=0; k<n; k++)", null, 2, null);// 17
    sc.addCodeLine(
        "if( (near[k] !=1001) && graph[k][ near[k] ]> graph[k][j] )", null, 3,
        null);// 18
    sc.addCodeLine("near[k] =j;", null, 4, null);// 19
    sc.addCodeLine("}", null, 1, null);// 20
    sc.addCodeLine("}", null, 0, null);// 21

    lang.nextStep();

    try {
      CheckpointUtils.checkpointEvent(this, "primstart", new Variable("x", x));// /////////////////////
      prims(prim, g, x, y, z, sc);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param graph
   *          is a given graph
   * @param n
   *          is the number of nodes.
   * @param t
   *          is used to store all edges that we have chosen to build a MST.
   * @param near
   *          tell us, which node that we haven't chosen is near to the two
   *          nodes of the edge that we have found.
   */
  private void prims(Graph inputGraph, int[][] graph, int n, int[][] t,
      int[] near, SourceCode codeSupport) throws LineNotExistsException {
    Timing time = new TicksTiming(5);
    int[] kl = new int[2];
    kl = getMinKL(graph, n);
    codeSupport.highlight(2);
    k = kl[0];
    l = kl[1];

    Timing t1 = new TicksTiming(k);
    inputGraph.highlightNode(k, null, t1);

    lang.nextStep();
    Timing t2 = new TicksTiming(l);
    inputGraph.highlightNode(l, null, t2);

    codeSupport.highlight(6);
    codeSupport.highlight(7);
    lang.nextStep();
    mincost = graph[k][l];
    CheckpointUtils.checkpointEvent(this, "minimalCost", new Variable("cost",
        mincost));// weight of the minimal edge

    t[0][0] = l;
    t[0][1] = k;
    if (t[0][0] < t[0][1]) {
      inputGraph.highlightEdge(t[0][0], t[0][1], null, time);
    } else {
      inputGraph.highlightEdge(t[0][1], t[0][0], null, time);
    }
    lang.nextStep();
    CheckpointUtils.checkpointEvent(this, "minimalEdge", new Variable(
        "animstep", lang.getStep()));// first edge in graph
    codeSupport.unhighlight(2);
    codeSupport.unhighlight(6);
    codeSupport.unhighlight(7);
    lang.nextStep();

    for (int i = 0; i < n; i++)
      near[i] = (graph[i][l] < graph[i][k]) ? l : k;
    near[k] = near[l] = 1001;

    for (int i = 1; i < n - 1; i++) {
      int j = getMin(graph, near, n);
      codeSupport.highlight(12);
      lang.nextStep();

      Timing t3 = new TicksTiming(j);
      inputGraph.highlightNode(j, null, t3);
      lang.nextStep();

      t[i][0] = j;
      t[i][1] = near[j];
      codeSupport.highlight(13);
      codeSupport.highlight(14);
      lang.nextStep();
      if (t[i][0] < t[i][1]) {
        inputGraph.highlightEdge(t[i][0], t[i][1], null, time);
      } else {
        inputGraph.highlightEdge(t[i][1], t[i][0], null, time);
      }
      lang.nextStep();
      CheckpointUtils.checkpointEvent(this, "minimalEdge", new Variable(
          "animstep", lang.getStep()));// count the number of edges
      codeSupport.unhighlight(12);
      codeSupport.unhighlight(13);
      codeSupport.unhighlight(14);
      lang.nextStep();

      mincost = mincost + graph[j][near[j]];
      int nextedge = graph[j][near[j]];
      CheckpointUtils.checkpointEvent(this, "edges", new Variable("edge",
          nextedge));// ................
      near[j] = 1001;
      for (int k = 0; k < n; k++)
        if ((near[k] != 1001) && graph[k][near[k]] > graph[k][j])
          near[k] = j;
    }
    CheckpointUtils.checkpointEvent(this, "totalCost", new Variable("weight",
        mincost));// total weight of MST
  }

  public int getMin(int[][] graph, int[] closed, int m) {
    int j = 0;
    for (int i = 0; i < m; i++)
      if (closed[i] != 1001) {
        j = i;
        break;
      }

    for (int i = 0; i < m; i++)
      if (closed[i] != 1001)
        if (graph[j][closed[j]] > graph[i][closed[i]])
          j = i;
    return j;
  }

  public int[] getMinKL(int[][] graph, int length) {
    int[] KL = new int[2];
    KL[0] = 1;
    KL[1] = 2;
    int k1 = KL[0];
    int l1 = KL[1];
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        if ((i != j) && (i < j)) {
          if ((graph[i][j] < graph[k1][l1]) && graph[i][j] != 0) {
            k1 = i;
            l1 = j;
          }
        }
      }
    }
    if (graph[k1][l1] != 0) {
      k = k1;
      l = l1;
      KL[0] = k;
      KL[1] = l;
    }

    return KL;

  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    Object o = primitives.get("graph");
    Graph myGraph = null;
    int[][] adjacencyMatrix = null;
    if (o instanceof Graph && o != null) {
      myGraph = (Graph) o;
      adjacencyMatrix = myGraph.getAdjacencyMatrix();
    } else {
      adjacencyMatrix = new int[6][6];

      adjacencyMatrix[0][1] = 6;
      adjacencyMatrix[0][2] = 1;
      adjacencyMatrix[0][3] = 5;
      adjacencyMatrix[1][2] = 5;
      adjacencyMatrix[1][4] = 3;
      adjacencyMatrix[2][3] = 5;
      adjacencyMatrix[2][4] = 6;
      adjacencyMatrix[2][5] = 4;
      adjacencyMatrix[3][5] = 2;
      adjacencyMatrix[4][5] = 6;
    }
    int n = adjacencyMatrix.length;
    int[][] t = new int[n][2];
    int[] near = new int[n];
    init();

    textProps = (TextProperties) props.getPropertiesByName("text");
    scProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    // graphProps = (GraphProperties)props.getPropertiesByName("graph");
    start(adjacencyMatrix, n, t, near);
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {

    return "Prim Algorithm";
  }

  @Override
  public String getAnimationAuthor() {

    return "Xiaofan Fan";
  }

  @Override
  public String getCodeExample() {

    return SOURCE_CODE;
  }

  @Override
  public Locale getContentLocale() {

    return Locale.US;
  }

  @Override
  public String getDescription() {

    return DESCRIPTION;
  }

  @Override
  public String getFileExtension() {

    return ".asu";
  }

  @Override
  public GeneratorType getGeneratorType() {

    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  @Override
  public String getName() {

    return "Prim Algorithm";
  }

  @Override
  public String getOutputLanguage() {

    return Generator.JAVA_OUTPUT;
  }

}
