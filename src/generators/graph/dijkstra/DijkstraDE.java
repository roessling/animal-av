package generators.graph.dijkstra;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class DijkstraDE implements Generator {
  private Language             lang;
  // private TextProperties info;

  /**
   * the source code shown in the animation
   */
  private SourceCode           src;
  private SourceCode           initialSrc;
  private SourceCode           dijkstraSrc;
  private SourceCode           distanceUpdateSrc;
  private SourceCode           shortestPathSrc;
  private SourceCodeProperties sourceCodeProperties;

  /**
   * the graph shown in the animation
   */
  private Graph                graph;
  private GraphProperties      graphProps;

  private int[][]              graphAdjacencyMatrix;
  private int[]                distance;
  private int[]                predecessor;

  // Menge Q :
  private LinkedList<Node>     q;
  private StringMatrix         tableQ;
  private Text                 listQ;
  // private TextProperties listQProp;

  // Anfang und Ende
  private int                  startindex;
  private int                  endindex;

  // Distanztabelle
  private StringMatrix         distanceTable;

  private StringMatrix         predecessorTable;

  private Text                 result;
  private TextProperties       resultProps;
  private String               resultString;

  private Text                 text;

  private TwoValueCounter      counter;
  private CounterProperties    cp;

  public void init() {
    lang = new AnimalScript("Graph[DE]", "Thanh Tung Do & Hoang Minh Duc", 800,
        600);
    lang.setStepMode(true);
  }

  public DijkstraDE(Language language) {
    this.lang = language;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    language.setStepMode(true);
  }

  public DijkstraDE() {
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    // get the user defined properties
    resultProps = (TextProperties) props.getPropertiesByName("resultProps");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    // listQProp = (TextProperties) props.getPropertiesByName("listQProp");
    // info = (TextProperties) props.getPropertiesByName("info");
    graphProps = (GraphProperties) props.getPropertiesByName("graphProps");

    // get the user defined primitive
    graph = (Graph) primitives.get("graph");

    graphAdjacencyMatrix = graph.getAdjacencyMatrix();
    for (int i = 0; i < graphAdjacencyMatrix.length; i++) {
      for (int j = 0; j < graphAdjacencyMatrix[0].length; j++) {
        System.out.print(graphAdjacencyMatrix[i][j] + "\t");
      }
      System.out.println();
    }

    boolean isNull = true;
    for (int i = 0; i < graphAdjacencyMatrix.length; i++)
      for (int j = 0; j < graphAdjacencyMatrix[0].length; j++)
        if (graphAdjacencyMatrix[i][j] != 0)
          isNull = false;

    if (isNull) {
      System.err.println("Overriding graph...");
      graph = getDefaultGraph();
    }
    // create the graph again in order to be able to set the graph properties
    int size = graph.getSize();
    Node[] nodes = new Node[size];
    String[] nodeLabels = new String[size];
    for (int i = 0; i < size; i++) {
      nodes[i] = graph.getNode(i);
      nodeLabels[i] = graph.getNodeLabel(i);
    }

    // // define graph properties

    lang.addGraph(graph, null, graphProps);

    // // initial source properpies for Action
    // add code to source code
    src = lang.newSourceCode(new Coordinates(600, 50), "listElement", null,
        sourceCodeProperties);
    src.addCodeLine("1  Funktion Dijkstra(Graph, Startknoten): ", null, 0, null);// 0
    src.addCodeLine(
        "2      initialisiere(Graph,Startknoten,abstand[],vorgaenger[],Q) ",
        null, 0, null); // 1
    src.addCodeLine(
        "3      solange Q nicht leer:                       // Der eigentliche Algorithmus",
        null, 0, null); // 2
    src.addCodeLine(
        "4          u := Knoten in Q mit kleinstem Wert in abstand[] ", null,
        0, null); // 3
    src.addCodeLine(
        "5          entferne u aus Q                        // für u ist der kuerzeste Weg nun bestimmt",
        null, 0, null); // 4
    src.addCodeLine("6          fuer jeden Nachbarn v von u:", null, 0, null); // 5
    src.addCodeLine("7              falls v in Q:", null, 0, null); // 6
    src.addCodeLine(
        "8                 distanz_update(u,v,abstand[],vorgaenger[]) // pruefe Abstand vom Startknoten zu v",
        null, 0, null); // 7
    src.addCodeLine("9      return vorgaenger[]  ;", null, 0, null); // 8
    src.addCodeLine(
        "1  Methode initialisiere(Graph,Startknoten,abstand[],vorgaenger[],Q):",
        null, 0, null); // 9
    src.addCodeLine("2      fuer jeden Knoten v in Graph:", null, 0, null); // 10
    src.addCodeLine("3          abstand[v] := unendlich", null, 0, null); // 11
    src.addCodeLine("4          vorgaenger[v] := null", null, 0, null); // 12
    src.addCodeLine("5      abstand[Startknoten] := 0", null, 0, null); // 13
    src.addCodeLine("6      Q := Die Menge aller Knoten in Graph ", null, 0,
        null); // 14
    src.addCodeLine("1  Methode distanz_update(u,v,abstand[],vorgaenger[]):",
        null, 0, null); // 15
    src.addCodeLine(
        "2      alternativ := abstand[u] + abstand_zwischen(u, v)// Weglaenge vom Startknoten nach v ueber u",
        null, 0, null); // 16
    src.addCodeLine("3      falls alternativ < abstand[v]:", null, 0, null); // 17
    src.addCodeLine("4          abstand[v] := alternativ", null, 0, null); // 18
    src.addCodeLine("5          vorgaenger[v] := u ", null, 0, null); // 19
    src.addCodeLine(
        "1  Funktion erstelle KuerzestenPfad(Zielknoten,vorgaenger[])", null,
        0, null); // 20
    src.addCodeLine("2   Weg[] := [Zielknoten]", null, 0, null); // 21
    src.addCodeLine("3   u := Zielknoten", null, 0, null); // 22
    src.addCodeLine(
        "4   solange vorgaenger[u] nicht null:   // Der Vorgaenger des Startknotens ist null",
        null, 0, null); // 23
    src.addCodeLine("5       u := vorgaenger[u]", null, 0, null); // 24
    src.addCodeLine("6       fuege u am Anfang von Weg[] ein", null, 0, null); // 25
    src.addCodeLine("7   return Weg[] ", null, 0, null); // 26
    src.hide();

    dijkstraSrc = lang.newSourceCode(new Coordinates(600, 200), "listElement",
        null, sourceCodeProperties);

    dijkstraSrc.addCodeLine("1  Funktion Dijkstra(Graph, Startknoten): ", null,
        0, null);// 0
    dijkstraSrc.addCodeLine(
        "2      initialisiere(Graph,Startknoten,abstand[],vorgaenger[],Q) ",
        null, 0, null); // 1
    dijkstraSrc
        .addCodeLine(
            "3      solange Q nicht leer:                       // Der eigentliche Algorithmus",
            null, 0, null); // 2
    dijkstraSrc.addCodeLine(
        "4          u := Knoten in Q mit kleinstem Wert in abstand[] ", null,
        0, null); // 3
    dijkstraSrc
        .addCodeLine(
            "5          entferne u aus Q                        // fuer u ist der kuerzeste Weg nun bestimmt",
            null, 0, null); // 4
    dijkstraSrc.addCodeLine("6          fuer jeden Nachbarn v von u:", null, 0,
        null); // 5
    dijkstraSrc.addCodeLine("7              falls v in Q:", null, 0, null); // 6
    dijkstraSrc
        .addCodeLine(
            "8                 distanz_update(u,v,abstand[],vorguenger[]) // prüfe Abstand vom Startknoten zu v",
            null, 0, null); // 7
    dijkstraSrc.addCodeLine("9      return vorguenger[]  ;", null, 0, null); // 8
    dijkstraSrc.hide();

    initialSrc = lang.newSourceCode(new Coordinates(600, 370), "listElement",
        null, sourceCodeProperties);

    initialSrc
        .addCodeLine(
            "1  Methode initialisiere(Graph,Startknoten,abstand[],vorgaenger[],Q):",
            null, 0, null); // 9
    initialSrc.addCodeLine("2      fuer jeden Knoten v in Graph:", null, 0,
        null); // 10
    initialSrc.addCodeLine("3          abstand[v] := unendlich", null, 0, null); // 11
    initialSrc.addCodeLine("4          vorgaenger[v] := null", null, 0, null); // 12
    initialSrc.addCodeLine("5      abstand[Startknoten] := 0", null, 0, null); // 13
    initialSrc.addCodeLine("6      Q := Die Menge aller Knoten in Graph ",
        null, 0, null); // 14
    initialSrc.hide();

    distanceUpdateSrc = lang.newSourceCode(new Coordinates(600, 370),
        "listElement", null, sourceCodeProperties);

    distanceUpdateSrc
        .addCodeLine("1  Methode distanz_update(u,v,abstand[],vorgaenger[]):",
            null, 0, null); // 15
    distanceUpdateSrc
        .addCodeLine(
            "2      alternativ := abstand[u] + abstand_zwischen(u, v)// Weglaenge vom Startknoten nach v ueber u",
            null, 0, null); // 16
    distanceUpdateSrc.addCodeLine("3      falls alternativ < abstand[v]:",
        null, 0, null); // 17
    distanceUpdateSrc.addCodeLine("4          abstand[v] := alternativ", null,
        0, null); // 18
    distanceUpdateSrc.addCodeLine("5          vorgaenger[v] := u ", null, 0,
        null); // 19
    distanceUpdateSrc.hide();

    shortestPathSrc = lang.newSourceCode(new Coordinates(600, 200),
        "listElement", null, sourceCodeProperties);

    shortestPathSrc.addCodeLine(
        "1  Funktion erstelle KuerzestenPfad(Zielknoten,vorgaenger[])", null,
        0, null); // 20
    shortestPathSrc.addCodeLine("2   Weg[] := [Zielknoten]", null, 0, null); // 21
    shortestPathSrc.addCodeLine("3   u := Zielknoten", null, 0, null); // 22
    shortestPathSrc
        .addCodeLine(
            "4   solange vorgaenger[u] nicht null:   // Der Vorgaenger des Startknotens ist null",
            null, 0, null); // 23
    shortestPathSrc.addCodeLine("5       u := vorgaenger[u]", null, 0, null); // 24
    shortestPathSrc.addCodeLine("6       fuege u am Anfang von Weg[] ein",
        null, 0, null); // 25
    shortestPathSrc.addCodeLine("7   return Weg[] ", null, 0, null); // 26
    shortestPathSrc.hide();

    String[][] temp = new String[2][graph.getSize()];

    tableQ = lang.newStringMatrix(new Coordinates(1024, 800), temp, "tableQ",
        null);

    distance = new int[graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      temp[1][i] = "~";
      temp[0][i] = graph.getNodeLabel(i).toString();

    }

    distanceTable = lang.newStringMatrix(new Coordinates(600, 100), temp,
        "distanceTable", null);
    distanceTable.hide();

    // //////////////

    for (int i = 0; i < graph.getSize(); i++) {
      temp[1][i] = "null";
    }

    predecessorTable = lang.newStringMatrix(new Coordinates(600, 150), temp,
        "predecessorTable", null);
    predecessorTable.hide();

    // ////////////////////////////
    result = lang.newText(new Coordinates(30, 470), "Die kuerzeste Pfad : ",
        "Ergebniss", null, resultProps);
    result.hide();

    // //////////////////////////
    text = lang
        .newText(new Coordinates(30, 520), "", "text", null, resultProps);
    text.hide();

    tableQ = lang.newStringMatrix(new Coordinates(10240, 800), temp, "tableQ",
        null);
    tableQ.hide();
    listQ = lang.newText(new Coordinates(20, 330), "Q = {} ", "listQ", null,
        resultProps);
    listQ.hide();

    lang.nextStep();
    // start compute distance
    compute(graph.getStartNode());

    // show shortest Path
    shortestPath(graph.getTargetNode());
    lang.nextStep();

    return lang.toString();
  }

  private void init(Node start) {
    text.setText(getDescription(), null, null);
    lang.nextStep();

    initialSrc.show();
    initialSrc.highlight(0);
    lang.nextStep();

    initialSrc.unhighlight(0);
    initialSrc.highlight(1);
    lang.nextStep();

    initialSrc.unhighlight(1);
    initialSrc.highlight(2);
    for (int i = 0; i < graph.getSize(); i++) {
      distance[i] = Integer.MAX_VALUE;
    }
    predecessor = new int[graph.getSize()];

    text.setText(
        "Die Tabelle der Abstanden von Startknote nach jeden Knoten wird erstellt.",
        null, null);
    text.show();
    distanceTable.show();
    lang.nextStep();

    text.setText(
        "Die Tabelle der Vorgaenger von jeden Knoten in den kuerzesten Pfaden wird erstellt.",
        null, null);
    predecessorTable.show();
    initialSrc.unhighlight(2);
    initialSrc.highlight(3);
    lang.nextStep();

    listQ.show();

    text.setText(
        "Weise allen Knoten die beiden Eigenschaften Distanz und Vorgaenger zu.",
        null, null);

    lang.nextStep();

    text.setText(
        "Initialisiere die Distanz im Startknoten mit 0 und in allen anderen Knoten mit unendlich(~).",
        null, null);

    lang.nextStep();

    initialSrc.unhighlight(3);

    highlightSource(initialSrc, 4, 5);
    startindex = graph.getPositionForNode(start);
    distance[startindex] = 0;
    distanceTable.put(1, startindex, "0", null, null);
    distanceTable.highlightCell(1, startindex, null, null);
    predecessorTable.put(1, startindex, graph.getNodeLabel(startindex), null,
        null);
    predecessorTable.highlightCell(0, startindex, null, null);

    StringBuffer temp = new StringBuffer();
    temp.append("Q = { ");

    // Q := Die Menge aller Knoten in Graph"+
    q = new LinkedList<Node>();
    for (int i = 0; i < graph.getSize(); i++) {
      temp.append(graph.getNodeLabel(i));
      temp.append(" , ");
      q.add(graph.getNode(i));
    }

    temp.append("}");
    listQ.setText(temp.toString(), null, null);

    lang.nextStep();

    unhighlightSource(initialSrc, 4, 5);
    lang.nextStep();

    initialSrc.hide();
    dijkstraSrc.highlight(2);
    distanceTable.unhighlightCell(1, startindex, null, null);
    predecessorTable.unhighlightCell(0, startindex, null, null);
    lang.nextStep();

    counter = lang.newCounter(tableQ); // Zaehler anlegen.
    cp = new CounterProperties(); // Zaehler-Properties anlegen
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau

    // view anlegen, Parameter:
    // 1. Counter
    // 2. linke obere Ecke;
    // 3. CounterProperties;
    // 4. Anzeige Zaehlerwert als Zahl?
    // 5. Anzeige Zaehlerwert als Balken?
    // Alternativ: nur Angabe Counter, Koordinate und Properties
    lang.newCounterView(counter, new Coordinates(30, 400), cp, true, true);

  }

  private Graph getDefaultGraph() {
    // add default graph and default graphAdjacencyMatrix
    int[][] graphAdjacencyMatrix = new int[7][7];
    // initialize adjacency matrix with zeros
    for (int i = 0; i < graphAdjacencyMatrix.length; i++)
      for (int j = 0; j < graphAdjacencyMatrix[0].length; j++)
        graphAdjacencyMatrix[i][j] = 0;
    // set the edges with the corresponding weights
    graphAdjacencyMatrix[0][1] = 8;
    graphAdjacencyMatrix[1][0] = 8;
    graphAdjacencyMatrix[0][2] = 2;
    graphAdjacencyMatrix[2][0] = 2;
    graphAdjacencyMatrix[1][2] = 5;
    graphAdjacencyMatrix[2][1] = 5;
    graphAdjacencyMatrix[1][3] = 4;
    graphAdjacencyMatrix[3][1] = 4;
    graphAdjacencyMatrix[2][4] = 1;
    graphAdjacencyMatrix[4][2] = 1;
    graphAdjacencyMatrix[3][4] = 6;
    graphAdjacencyMatrix[4][3] = 6;
    graphAdjacencyMatrix[3][5] = 3;
    graphAdjacencyMatrix[5][3] = 3;
    graphAdjacencyMatrix[3][6] = 2;
    graphAdjacencyMatrix[6][3] = 2;
    graphAdjacencyMatrix[4][5] = 7;
    graphAdjacencyMatrix[5][4] = 7;
    graphAdjacencyMatrix[5][6] = 6;
    graphAdjacencyMatrix[6][5] = 6;

    // define the nodes and their positions
    Node[] graphNodes = new Node[7];
    graphNodes[0] = (Node) new Coordinates(40, 40);
    graphNodes[1] = (Node) new Coordinates(40, 150);
    graphNodes[2] = (Node) new Coordinates(190, 40);
    graphNodes[3] = (Node) new Coordinates(190, 150);
    graphNodes[4] = (Node) new Coordinates(340, 40);
    graphNodes[5] = (Node) new Coordinates(340, 150);
    graphNodes[6] = (Node) new Coordinates(340, 300);

    // define the names of the nodes
    String[] labels = { "A", "B", "C", "D", "E", "F", "G" };

    Graph g = lang.newGraph("graph", graphAdjacencyMatrix, graphNodes, labels,
        null, graphProps);
    g.hide();

    return g;
  }

  private void compute(Node start) {
    src.show();
    lang.nextStep();

    src.hide();
    dijkstraSrc.show();
    lang.nextStep();

    dijkstraSrc.highlight(0);
    lang.nextStep();

    dijkstraSrc.unhighlight(0);
    // highlight line 1
    dijkstraSrc.highlight(1);
    lang.nextStep();

    // unhighlight line 1
    dijkstraSrc.unhighlight(1);

    init(start);

    dijkstraSrc.unhighlight(2);
    while (!q.isEmpty()) {
      highlightSource(dijkstraSrc, 3, 4);
      // u := Knoten in Q mit kleinstem Wert in abstand[]
      Node u = q.element();
      int min = Integer.MAX_VALUE;

      for (Node temp : q) {
        if (distance[graph.getPositionForNode(temp)] < min) {
          u = temp;
          min = distance[graph.getPositionForNode(temp)];
          counter.assignmentsInc(1);

        }
      }

      // entferne u aus Q
      graph.highlightNode(u, null, null);

      q.remove(u);

      StringBuffer temp = new StringBuffer();
      temp.append("Q = { ");

      for (Node tempNode : q) {
        temp.append(graph.getNodeLabel(tempNode));
        temp.append(" , ");
      }

      temp.append("}");
      listQ.setText(temp.toString(), null, null);

      temp = new StringBuffer();
      temp.append(graph.getNodeLabel(u));
      temp.append(" wurde besucht.");
      text.setText(temp.toString(), null, null);

      lang.nextStep();

      unhighlightSource(dijkstraSrc, 3, 4);

      lang.nextStep();

      for (int i = 0; i < graph.getSize(); i++) {
        if (graphAdjacencyMatrix[graph.getPositionForNode(u)][i] != 0
            && q.contains(graph.getNode(i))) {
          highlightSource(dijkstraSrc, 5, 6);
          lang.nextStep();

          distance_update(graph.getPositionForNode(u), i);

        }

      }

    }

    text.setText("Es gibt keine unbesuchte Knote mehr. ", null, null);
    dijkstraSrc.highlight(8);
    lang.nextStep();

    dijkstraSrc.hide();

  }

  private void distance_update(int pos_u, int pos_v) {
    distanceUpdateSrc.show();
    unhighlightSource(dijkstraSrc, 5, 6);
    distanceUpdateSrc.highlight(0);
    lang.nextStep();

    distanceUpdateSrc.unhighlight(0);
    distanceUpdateSrc.highlight(1);
    int alternativ = distance[pos_u] + graphAdjacencyMatrix[pos_u][pos_v];
    graph.highlightEdge(graph.getNode(pos_u), graph.getNode(pos_v), null, null);
    lang.nextStep();

    distanceUpdateSrc.unhighlight(1);
    distanceUpdateSrc.highlight(2);
    lang.nextStep();

    distanceUpdateSrc.unhighlight(2);
    if (alternativ < distance[pos_v]) {

      StringBuffer temp = new StringBuffer();
      temp.append("Mit Information von der Strecke von ");
      temp.append(graph.getNodeLabel(pos_u));
      temp.append(" nach ");
      temp.append(graph.getNodeLabel(pos_v));
      temp.append(" wird der Abstand vom Startknoten zum Knoten ");
      temp.append(graph.getNodeLabel(pos_v));
      temp.append(" aktualisiert.");

      text.setText(temp.toString(), null, null);

      distanceUpdateSrc.highlight(3);
      distance[pos_v] = alternativ;
      distanceTable.put(1, pos_v, Integer.toString(alternativ), null, null);
      distanceTable.highlightCell(1, pos_v, null, null);
      lang.nextStep();

      distanceTable.unhighlightCell(1, pos_v, null, null);
      distanceUpdateSrc.unhighlight(3);
      distanceUpdateSrc.highlight(4);
      predecessor[pos_v] = pos_u;
      predecessorTable.put(1, pos_v, graph.getNodeLabel(pos_u), null, null);
      predecessorTable.highlightCell(0, pos_v, null, null);
      lang.nextStep();

      predecessorTable.unhighlightCell(0, pos_v, null, null);
      distanceUpdateSrc.unhighlight(4);
    }

    graph.unhighlightEdge(graph.getNode(pos_u), graph.getNode(pos_v), null,
        null);
    lang.nextStep();
    counter.accessInc(1);
    distanceUpdateSrc.hide();
  }

  private void shortestPath(Node end) {

    shortestPathSrc.show();
    result.show();
    shortestPathSrc.highlight(0);
    distanceTable.highlightCell(1, graph.getPositionForNode(end), null, null);
    lang.nextStep();

    shortestPathSrc.unhighlight(0);
    highlightSource(shortestPathSrc, 1, 2);
    endindex = graph.getPositionForNode(end);
    resultString = graph.getNodeLabel(endindex);
    result.setText("Die kuerzester Pfad : ".concat(resultString), null, null);
    int pre = graph.getPositionForNode(end);
    graph.unhighlightNode(endindex, null, null);

    StringBuffer temp = new StringBuffer();
    temp.append("Dann suchen wir die kuerzester Pfad von Knote ");
    temp.append(graph.getNodeLabel(startindex));
    temp.append(" nach Knote ");
    temp.append(graph.getNodeLabel(endindex));
    temp.append(" .");
    text.setText(temp.toString(), null, null);

    lang.nextStep();

    unhighlightSource(shortestPathSrc, 1, 2);

    lang.nextStep();

    while (pre != startindex) {
      shortestPathSrc.highlight(3);
      lang.nextStep();

      temp = new StringBuffer();
      temp.append("Knote ");
      temp.append(graph.getNodeLabel(predecessor[pre]));
      temp.append(" ist Vorgaenger von der Knote ");
      temp.append(graph.getNodeLabel(pre));
      temp.append(" in dem kuerzesten Pfad zum ");
      temp.append(graph.getNodeLabel(pre));
      text.setText(temp.toString(), null, null);

      shortestPathSrc.unhighlight(3);
      shortestPathSrc.highlight(4);
      graph.highlightEdge(predecessor[pre], pre, null, null);

      predecessorTable.highlightCell(0, pre, null, null);
      graph.unhighlightNode(predecessor[pre], null, null);
      lang.nextStep();

      predecessorTable.unhighlightCell(0, pre, null, null);
      pre = predecessor[pre];

      lang.nextStep();

      predecessorTable.unhighlightCell(0, pre, null, null);
      resultString = graph.getNodeLabel(pre).concat(">>").concat(resultString);
      shortestPathSrc.unhighlight(4);
      shortestPathSrc.highlight(5);
      result.setText("Die kuerzester Pfad : ".concat(resultString), null, null);
      lang.nextStep();

      shortestPathSrc.unhighlight(5);

    }

    result.setFont(new Font("SansSerif", Font.ITALIC, 30), null, null);
    text.setText("Die kuerzester Pfad wird gefunden", null, null);
    text.show();
    src.highlight(26);

  }

  private void highlightSource(SourceCode source, int first, int last) {
    for (int i = first; i <= last; i++)
      source.highlight(i);
  }

  private void unhighlightSource(SourceCode source, int first, int last) {
    for (int i = first; i <= last; i++)
      source.unhighlight(i);
  }

  public String getName() {
    return "Dijkstra[DE]";
  }

  public String getAlgorithmName() {
    return "Dijkstra";
  }

  public String getAnimationAuthor() {
    return "Do Thanh Tung, Hoang Minh Duc";
  }

  public String getDescription() {
    return "Die Grundidee des Algorithmus ist es, immer derjenigen Kante zu folgen, die den kürzesten Streckenabschnitt vom Startknoten aus verspricht. Andere Kanten werden erst dann verfolgt, wenn alle kürzeren Streckenabschnitte beachtet wurden. Dieses Vorgehen gewährleistet, dass bei Erreichen eines Knotens kein kürzerer Pfad zu ihm existieren kann. Eine einmal berechnete Distanz zwischen dem Startknoten und einem erreichten Knoten wird nicht mehr geändert. Dieses Vorgehen wird fortgesetzt, bis die Distanz des Zielknotens berechnet wurde (single-pair shortest path) oder die Distanzen aller Knoten zum Startknoten bekannt sind (single-source shortest path)."
        + "\n";
  }

  public String getCodeExample() {
    return "Funktion Dijkstra(Graph, Startknoten):"
        + "\n"
        + "        initialisiere(Graph,Startknoten,abstand[],vorgänger[],Q)"
        + "\n"
        + "       solange Q nicht leer:                       // Der eigentliche Algorithmus"
        + "\n"
        + "           u := Knoten in Q mit kleinstem Wert in abstand[]"
        + "\n"
        + "           entferne u aus Q                                // für u ist der kürzeste Weg nun bestimmt"
        + "\n"
        + "          für jeden Nachbarn v von u:"
        + "\n"
        + "              falls v in Q:"
        + "\n"
        + "                 distanz_update(u,v,abstand[],vorgänger[])   // prüfe Abstand vom Startknoten zu v"
        + "\n"
        + "       return vorgänger[]\""
        + "\n"
        + " "
        + "\n"
        + "Methode initialisiere(Graph,Startknoten,abstand[],vorgänger[],Q):"
        + "\n"
        + "      für jeden Knoten v in Graph:"
        + "\n"
        + "           abstand[v] := unendlich"
        + "\n"
        + "           vorgänger[v] := null"
        + "\n"
        + "       abstand[Startknoten] := 0"
        + "\n"
        + "       Q := Die Menge aller Knoten in Graph"
        + "\n"
        + "\n"
        + "Methode distanz_update(u,v,abstand[],vorgänger[]):"
        + "\n"
        + "      alternativ := abstand[u] + abstand_zwischen(u, v)   // Weglänge vom Startknoten nach v über u"
        + "\n" + "       falls alternativ < abstand[v]:\"" + "\n"
        + "            abstand[v] := alternativ" + "\n"
        + "          vorgänger[v] := u\";";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}