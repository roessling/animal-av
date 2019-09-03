package generators.graph.depthfirstsearch;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.DFSVertex;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class DFSDSDM extends AnnotatedAlgorithm implements Generator {

  private Coordinates[]        nodes;
  private DFSVertex[]          vertex;
  private String[]             labels;
  private int                  time;
  // private SourceCode DFS_sc, DFSvisit_sc;
  private SourceCodeProperties scProps;                                                                   // ,
                                                                                                           // DFS_scProp,
                                                                                                           // DFSvisit_scProp;
  private Graph                overlayGraph;                                                              // ,
                                                                                                           // underlayGraph;
  private TextProperties       visited, finished, txtProp;
  private GraphProperties      overlayGraphProps, underlayGraphProps;
  private int[][]              adjazenz;
  private int                  graphsize;

  private String               timeStamp      = "Time Increase";
  private String               gray           = "Grayed Nodes";
  private String               black          = "Blacked Nodes";

  private final String         DFS_annotation = "public void DFS() { @label(\"header\") 			@openContext \n"
                                                  + "	for (Node node : graph) { 						@label(\"forInit\") \n"
                                                  + "		node.setColor(WHITE); 						@label(\"whiteInit\") @ \n"
                                                  + "		node.setBefore(null);						@label(\"beforeInit\") \n"
                                                  + "	} 												@label(\"endinit\") \n"
                                                  + "	time = 0;										@label(\"timeInit\") \n"
                                                  + "	for (Node node : graph) { 						@label(\"mainFor\") \n"
                                                  + "		if (node.getColor() == Color.WHITE) { 		@label(\"ifWhite\") \n"
                                                  + "			dfs_Visit(node); 						@label(\"VisitCall\") @closeContext  \n"
                                                  + "		}											@label(\"endIfWhite\")  @openContext \n"
                                                  + "	}												@label(\"endMainFor\") \n"
                                                  + "}												@label(\"endHeader\") \n"
                                                  + "public void dfs_visit(Node node) {				@label(\"header2\") @openContext \n"
                                                  + "	node.getColor() = Color.GRAY;					@label(\"setColorGray\") @inc(\""
                                                  + gray
                                                  + "\")\n"
                                                  + "	time++;	 										@label(\"incTime\") @inc(\""
                                                  + timeStamp
                                                  + "\") \n"
                                                  + "	node.setFirstVisitTime(time); 					@label(\"setVTime\") \n"
                                                  + "	for (Node neighbour : neighbours) {      		@label(\"for\") \n"
                                                  + "		if (neighbour.getColor == Color.WHITE) {	@label(\"ifNeigh\") \n"
                                                  + "			neighbour.setBefore(null);        		@label(\"setBefore\") \n"
                                                  + "			dfs_Visit(neighbour);              		@label(\"methodCall\") @closeContext \n"
                                                  + "		}                                      		@label(\"endIfNeigh\") @openContext \n"
                                                  + "	}                                          		@label(\"endFor\") \n"
                                                  + "	node.setColor(Color.BLACK);              		@label(\"setColorBlack\") @inc(\""
                                                  + black
                                                  + "\") \n"
                                                  + "	node.setFinishTime(time++);						@label(\"setFTime\") @inc(\""
                                                  + timeStamp
                                                  + "\") \n"
                                                  + "}												@label(\"endHeader2\") @closeContext\n";

  private final String         DFS_code       = "public void DFS() {"
                                                  + " \n 	for (Node node : graph) {"
                                                  + " \n 		node.setColor(WHITE);"
                                                  + " \n 		node.setBefore(null);"
                                                  + " \n 	}"
                                                  + " \n 	time = 0;"
                                                  + " \n 	for (Node node : graph) {"
                                                  + " \n 		if (node.getColor() == Color.WHITE) {"
                                                  + " \n 			dfs_Visit(node);"
                                                  + " \n 		}" + " \n 	}"
                                                  + " \n }";
  private final String         DFS_Visit_code = "public void dfs_visit(Node node) {"
                                                  + "\n 	node.getColor() = Color.GRAY;"
                                                  + "\n 	time++;"
                                                  + "\n 	node.setFirstVisitTime(time);"
                                                  + "\n 	for (Node neighbour : neighbours) {"
                                                  + "\n 		if (neighbour.getColor == Color.WHITE) {"
                                                  + "\n 			neighbour.setBefore(null);"
                                                  + "\n 			dfs_Visit(neighbour);"
                                                  + "\n 		}"
                                                  + "\n 	}"
                                                  + "\n 	node.setColor(Color.BLACK);"
                                                  + "\n 	node.setFinishTime(time++);"
                                                  + "\n }";

  public DFSDSDM() {
    // nothing to be done here
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    init();

    vars.declare("int", timeStamp, "0");
    vars.setGlobal(timeStamp);
    vars.declare("int", gray, "0");
    vars.setGlobal(gray);
    vars.declare("int", black, "0");
    vars.setGlobal(black);

    // für jeden einzeln machen
    Text compText = lang.newText(new Offset(100, 0, "scCode",
        AnimalScript.DIRECTION_NE), "...", "complexText", null);
    TextUpdater compTU = new TextUpdater(compText);
    compTU.addToken(timeStamp + ": ");
    compTU.addToken(vars.getVariable(timeStamp));
    compTU.addToken("  " + gray + ": ");
    compTU.addToken(vars.getVariable(gray));
    compTU.addToken("  " + black + ": ");
    compTU.addToken(vars.getVariable(black));
    compTU.update(); // zum Initialisieren

    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));

    RectProperties rectProp = new RectProperties();

    rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
    rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

    // DFS_scProp = scProps;
    // DFSvisit_scProp = scProps;

    // txtProp = (TextProperties) arg0.getPropertiesByName("txtProp");
    // DFS_scProp = (SourceCodeProperties) arg0
    // .getPropertiesByName("DFS_scProp");
    // DFSvisit_scProp = (SourceCodeProperties) arg0
    // .getPropertiesByName("DFSvisit_scProp");
    lang.newText(new Coordinates(20, 30), "Depth-first Search", "title", null,
        txtProp);
    lang.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "title", AnimalScript.DIRECTION_SE), "rect1", null,
        rectProp);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    lang.newText(new Offset(0, 10, "title", AnimalScript.DIRECTION_SW),
        "by Dennis Mueller & Dennis Siebert", "title2", null, txtProp);
    // get properties and primitives
    if (arg1 != null) {

      Graph copyGraph = (Graph) arg1.get("graph");
      adjazenz = copyGraph.getAdjacencyMatrix();
      graphsize = adjazenz.length;

      vertex = new DFSVertex[graphsize];
      // copy primitive graph for our behavior

      nodes = new Coordinates[graphsize];
      for (int i = 0; i < graphsize; i++) {
        nodes[i] = (Coordinates) copyGraph.getNode(i);
        vertex[i] = new DFSVertex(i, nodes[i].getX(), nodes[i].getY());
      }
    } else {

    }

    // create the two graphs
    overlayGraph = lang.newGraph("dfsOver", adjazenz, nodes,
        generateNodeLabels(), null, overlayGraphProps);
    lang.newGraph("dfsUnder", adjazenz, nodes, generateNodeLabels(), null,
        underlayGraphProps);

    //
    // showDFSSourceCode(DFS_scProp);
    // showDFSvisitSourceCode(DFSvisit_scProp);
    lang.nextStep();

    exec("header");
    lang.nextStep();

    exec("forInit");
    exec("whiteInit");
    exec("beforeInit");
    exec("endinit");
    exec("timeInit");
    lang.nextStep();

    dfs();

    // System.out.println("Wort: " + lang.toString());
    return lang.toString();
  }

  private void dfs() {
    exec("mainFor");
    lang.nextStep();

    for (DFSVertex vx : vertex) {

      exec("ifWhite");
      lang.nextStep();

      if (vx.getColor().equals(Color.WHITE)) {

        exec("VisitCall");
        lang.nextStep();
        dfs_visit(vx);

      }
      exec("endIfWhite");
      lang.nextStep();

      exec("mainFor");
      lang.nextStep();
    }
    exec("endMainFor");
    lang.nextStep();

    exec("endHeader");
    lang.nextStep();

  }

  private void dfs_visit(DFSVertex vx) {

    exec("header2");
    lang.nextStep();

    // update node with color
    vx.setColor(Color.GRAY);
    overlayGraph.highlightNode(vx.getNr(), null, null);
    exec("setColorGray");
    lang.nextStep();

    // increase global time
    time = time + 1;
    exec("incTime");

    // show new visited time in node
    vx.setVisitedTime(time);
    lang.nextStep();

    Text text = lang.newText(new Coordinates(vx.getX(), vx.getY()),
        Integer.toString(vx.getVisitedTime()) + "/",
        "name" + Integer.toString(vx.getNr()), null, visited);

    exec("setVTime");

    // System.out.println("Vertex " + vx.getNr() + "discovered: "
    // + vx.getVisitedTime());

    lang.nextStep();
    // show loop code
    exec("for");

    for (int i = 0; i < graphsize; i++) {

      // private code for functionality

      if (adjazenz[vx.getNr()][i] == 1) {

        // highlight next edge
        overlayGraph.highlightEdge(vx.getNr(), i, null, null);
        lang.nextStep();

        // show if code
        exec("ifNeigh");

        lang.nextStep();

        if (vertex[i].getColor().equals(Color.WHITE)) {

          exec("setBefore");

          lang.nextStep();

          // show recursiv method code
          exec("methodCall");
          lang.nextStep();

          dfs_visit(vertex[i]);

        }

        // show closed IF brace
        exec("endIfNeigh");
        lang.nextStep();
        exec("for");
        lang.nextStep();
      }

    }

    // show closed FOR brace
    exec("endFor");
    lang.nextStep();

    // update node to finished, show finished time
    vx.setColor(Color.BLACK);
    time = time + 1;
    vx.setFinishedTime(time);

    overlayGraph.hideNode(vx.getNr(), null, null);
    text.exchange(lang.newText(new Coordinates(vx.getX(), vx.getY()),
        vx.getVisitedTime() + "/" + vx.getFinishedTime(),
        Integer.toString(vx.getNr()), null, finished));
    exec("setColorBlack");
    lang.nextStep();
    exec("setFTime");

    // System.out.println("Vertex " + vx.getNr() + "finished: "
    // + vx.getFinishedTime());

    // last line of method
    exec("endHeader2");
    lang.nextStep();

  }

  @Override
  public String getAlgorithmName() {
    return "Depth-first search";
  }

  @Override
  public String getAnimationAuthor() {
    return "Dennis Siebert, Dennis Müller";
  }

  @Override
  public String getCodeExample() {
    return DFS_code + "\n" + DFS_Visit_code;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "Depth-first search (DFS) is an algorithm  for traversing or searching a tree, tree structure, or graph. One starts at the root (selecting some node as the root in the graph case) and explores as far as possible along each branch before backtracking.";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  @Override
  public String getName() {
    return "Depth-first search";
  }

  @Override
  public String getOutputLanguage() {
    return generators.framework.Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {

    super.init();

    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(20, 280), "scCode", null,
        scProps);
    // System.out.println(annotatedSrc);

    // initialize the different graph properties
    overlayGraphProps = setOverlayGraphProperties();
    underlayGraphProps = setUnderlayGraphProperties();

    // initialize the two different text properties for visited and finished
    // nodes
    visited = setVisitTextProperties();
    finished = setFinishTextProperties();

    time = 0;
    // parsing anwerfen
    parse();
  }

  private TextProperties setVisitTextProperties() {
    visited = new TextProperties();
    visited.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    return visited;
  }

  private TextProperties setFinishTextProperties() {
    finished = new TextProperties();
    finished.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    return finished;
  }

  // private void showDFSSourceCode(SourceCodeProperties DFS_scProp) {
  // // init sourcecode
  // DFS_sc = lang.newSourceCode(new Offset(0, 25, "dfsOver",
  // AnimalScript.DIRECTION_SW), "dfs_Code", null, DFS_scProp);
  // DFS_sc.addCodeLine("public void DFS() {", null, 0, null);
  // DFS_sc.addCodeLine("for (Node node : graph) {", null, 1, null);
  // DFS_sc.addCodeLine("node.setColor(WHITE);", null, 2, null);
  // DFS_sc.addCodeLine("node.setBefore(null);", null, 2, null);
  // DFS_sc.addCodeLine("}", null, 1, null);
  // DFS_sc.addCodeLine("time = 0;", null, 1, null);
  // DFS_sc.addCodeLine("for (Node node : graph) {", null, 1, null);
  // DFS_sc.addCodeLine("if (node.getColor() == Color.WHITE) {", null, 2, null);
  // DFS_sc.addCodeLine("dfs_Visit(node);", null, 3, null);
  // DFS_sc.addCodeLine("}", null, 2, null);
  // DFS_sc.addCodeLine("}", null, 1, null);
  // DFS_sc.addCodeLine("}", null, 0, null);
  //
  // }
  //
  // private void showDFSvisitSourceCode(SourceCodeProperties DFSvisit_scProp) {
  // DFSvisit_sc = lang.newSourceCode(new Offset(0, 25, "DFS_Code",
  // AnimalScript.DIRECTION_SW), "DFSvisit_Code", null,
  // DFSvisit_scProp);
  // DFSvisit_sc.addCodeLine("public void dfs_visit(Node node) {", null, 0,
  // null);
  // DFSvisit_sc.addCodeLine("node.getColor() = Color.GRAY;", null, 1, null);
  // DFSvisit_sc.addCodeLine("time++;", null, 1, null);
  // DFSvisit_sc.addCodeLine("node.setFirstVisitTime(time);", null, 1, null);
  // DFSvisit_sc.addCodeLine("for (Node neighbour : neighbours) {", null, 1,
  // null);
  // DFSvisit_sc.addCodeLine("if (neighbour.getColor == Color.WHITE) {", null,
  // 2, null);
  // DFSvisit_sc.addCodeLine("neighbour.setBefore(null);", null, 3, null);
  // DFSvisit_sc.addCodeLine("dfs_Visit(neighbour);", null, 3, null);
  // DFSvisit_sc.addCodeLine("}", null, 2, null);
  // DFSvisit_sc.addCodeLine("}", null, 1, null);
  // DFSvisit_sc.addCodeLine("node.setColor(Color.BLACK);", null, 1, null);
  // DFSvisit_sc.addCodeLine("node.setFinishTime(time++);", null, 1, null);
  // DFSvisit_sc.addCodeLine("}", null, 0, null);
  // }

  /**
   * For each node an empty label is set. The number of whitespaces influence
   * the size of the nodes
   * 
   * @return all node labels
   */
  private String[] generateNodeLabels() {
    labels = new String[graphsize];
    for (int i = 0; i < graphsize; i++) {
      labels[i] = "    ";
    }
    return labels;
  }

  /**
   * Set the specification for the overlaying graph
   * 
   * @return overlayGraph specifications
   */
  private GraphProperties setOverlayGraphProperties() {
    overlayGraphProps = new GraphProperties("graphProperties");
    overlayGraphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    overlayGraphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    overlayGraphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.LIGHT_GRAY);
    overlayGraphProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    return overlayGraphProps;
  }

  /**
   * Set the specifications for the underlying graph
   * 
   * @return underlayGraph specifications
   */
  private GraphProperties setUnderlayGraphProperties() {
    underlayGraphProps = new GraphProperties("graphProperties");
    underlayGraphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    underlayGraphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.BLACK);
    underlayGraphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    underlayGraphProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
    return underlayGraphProps;
  }

  public void setGraphsize(int graphsize) {
    this.graphsize = graphsize;
  }

  public int getGraphsize() {
    return graphsize;
  }

  @Override
  public String getAnnotatedSrc() {
    return DFS_annotation;
  }
}
