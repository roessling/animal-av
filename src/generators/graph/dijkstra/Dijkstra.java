package generators.graph.dijkstra;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Dijkstra implements Generator {
  private Language             lang;
  private GraphProperties      graphProp;
  private Graph                graph;
  private SourceCodeProperties sourceCode;
  private int                  start;
  private MatrixProperties     matrixProperties;
  private SourceCode           code;
  private Text                 kommentar;
  private StringMatrix         stringMatrix;
  private boolean              directed;

  public void init() {
    lang = new AnimalScript("Dijkstra", "Konstantin Ramig", 800, 800);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    graphProp = (GraphProperties) props.getPropertiesByName("graphProp");
    graph = (Graph) primitives.get("graph");
    start = (Integer) primitives.get("start");
    // Sourcecode Properties
    sourceCode = new SourceCodeProperties();
    sourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));
    sourceCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sourceCode.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // Matrix Prop
    matrixProperties = new MatrixProperties();
    matrixProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.YELLOW);
    matrixProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.GREEN);

    GraphProperties tmp = graph.getProperties();
    directed = (Boolean) tmp.get(AnimationPropertiesKeys.DIRECTED_PROPERTY);
    graphProp.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, directed);

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    intro();
    showCodeGraph();
    initQuestionGroups();
    dijkstra();

    lang.finalizeGeneration();
    return lang.toString();
  }

  private void intro() {
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(200, 50), "Dijkstra", "header",
        null, tp);
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hrect", null, rp);

    // Intro
    SourceCodeProperties introP = new SourceCodeProperties();
    introP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 18));
    introP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode intro = lang.newSourceCode(new Offset(-150, 30, header,
        AnimalScript.DIRECTION_SW), "intro", null, introP);
    intro
        .addCodeLine(
            "Der Diijkstra Algorithmus dient dazu innerhalb eines Graphen von einem gegebenen",
            null, 0, null);
    intro
        .addCodeLine(
            "Knoten aus die kuerzeste Route zu allen anderen Knoten zu finden. Der Graph kann",
            null, 0, null);
    intro
        .addCodeLine(
            "hierbei sowohl gerichtet als auch ungerichet sein. Als Entfernung zwischen zwei",
            null, 0, null);
    intro
        .addCodeLine(
            "Knoten wird in der Regel das Gewicht der auf der Route liegenden Kanten gewählt.",
            null, 0, null);
    intro
        .addCodeLine(
            "Bei einem ungewichteten Graphen wird als Entfernung die Anzahl der dazwischen",
            null, 0, null);
    intro
        .addCodeLine(
            "liegenden Knoten genommen. Es darf jedoch niemals ein negatives Gewicht existieren",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "Der Algorithmus arbeitet indem er sich zu jeden Knoten merkt wie weit er vom",
            null, 0, null);
    intro
        .addCodeLine(
            "Start entfernt ist, wer sein Vorgänger auf der Route ist und ob er bereits",
            null, 0, null);
    intro
        .addCodeLine(
            "bearbeitet wurde. Wird ein Knoten bearbeitet so wird er als besucht markiert,",
            null, 0, null);
    intro
        .addCodeLine(
            "für seine Nachbarn wird berechnet wie weit sie entfernt sind sollte die Route",
            null, 0, null);
    intro
        .addCodeLine(
            "über den aktuell bearbeiteten Knoten gehen. Ist die neu berechnete Entfernung ",
            null, 0, null);
    intro
        .addCodeLine(
            "kürzer als die bereits gemerkte, so wird diese als Entfernung für den Nachbarn",
            null, 0, null);
    intro
        .addCodeLine(
            "eingetragen. Als Vorgänger dieses Nachbarns wird der bearbeitete Knoten eingetragen",
            null, 0, null);
    intro.addCodeLine("und der Nachbar wird als unbesucht markiert.", null, 0,
        null);
    intro
        .addCodeLine(
            "Anfangs gild jeder Knoten als unbesucht ihr Vorgänger ist unbekannt und ihre Entfernung",
            null, 0, null);
    intro
        .addCodeLine(
            "ist unendlich. Für den Startknoten wird dann die Entfernung auf 0 gesetzt.",
            null, 0, null);

    lang.nextStep("Intro");
    intro.hide();
  }

  private void showCodeGraph() {

    Node[] graphNodes = new Node[graph.getSize()];
    String[] labels = new String[graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      System.out.println(Arrays.toString(graph.getAdjacencyMatrix()[i]));
      graphNodes[i] = graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
    }
    graph = lang.newGraph("graph", graph.getAdjacencyMatrix(), graphNodes,
        labels, null, graphProp);
    try {
      graph.moveTo(AnimalScript.DIRECTION_NE, null, new Coordinates(20, 100),
          null, null);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    code = lang.newSourceCode(new Offset(200, 0, graph,
        AnimalScript.DIRECTION_NE), "code", null, sourceCode);
    code.addCodeLine("public int[] dijkstra (WeightedGraph G, int s) {", null,
        0, null);
    code.addCodeLine("int[] dist = new int [G.size()];", null, 1, null);
    code.addCodeLine("int[] pred = new int [G.size()];", null, 1, null);
    code.addCodeLine("boolean [] visited = new boolean [G.size()];", null, 1,
        null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("for (int i=0; i<dist.length; i++) {", null, 1, null);
    code.addCodeLine("dist[i] = Integer.MAX_VALUE;", null, 2, null);
    code.addCodeLine("pred[i] = null;", null, 2, null);
    code.addCodeLine("visited[i] = false;", null, 2, null);
    code.addCodeLine("}", null, 1, null);
    code.addCodeLine("dist[s] = 0;", null, 1, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("int next;", null, 1, null);
    code.addCodeLine("while ((next = minVertex(dist, visited)) != -1) {", null,
        1, null);
    code.addCodeLine("visited[next] = true;", null, 2, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("int [] n = G.neighbors (next);", null, 2, null);
    code.addCodeLine("for (int j=0; j<n.length; j++) {", null, 2, null);
    code.addCodeLine("if(n[j] != 0) {", null, 3, null);
    code.addCodeLine("int d = dist[next] + n[j];", null, 4, null);
    code.addCodeLine("if (dist[v] > d) {", null, 4, null);
    code.addCodeLine("dist[v] = d;", null, 5, null);
    code.addCodeLine("pred[v] = next;", null, 5, null);
    code.addCodeLine("visited[v]= false;", null, 5, null);
    code.addCodeLine("}", null, 4, null);
    code.addCodeLine("}", null, 3, null);
    code.addCodeLine("}", null, 2, null);
    code.addCodeLine("}", null, 1, null);
    code.addCodeLine("}", null, 0, null);
    code.registerLabel("//neuer Knoten", 13);
    code.registerLabel("//neue Kante", 18);

    // Kommentar
    TextProperties kommP = new TextProperties();
    kommP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.BOLD, 12));
    kommP.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 150, 0));
    kommentar = lang.newText(new Offset(-20, 20, code,
        AnimalScript.DIRECTION_SW), "", "kommentar", null, kommP);
    lang.nextStep();
  }

  private void dijkstra() {

    MsTiming graphanimation = new MsTiming(200);

    String nextKey = "next";
    String neighborKey = "neighbor";

    boolean[] visited = new boolean[graph.getSize()];
    int[] pred = new int[graph.getSize()];
    int[] dist = new int[graph.getSize()];

    code.highlight(0);
    kommentar.setText(
        "//Dijkstra auf den Graphen mit Startknoten "
            + graph.getNodeLabel(start) + " anwenden", null, null);
    lang.nextStep("Algorithmus Start");
    code.unhighlight(0);
    code.highlight(1);
    code.highlight(2);
    code.highlight(3);
    kommentar.setText("//Arrays zum speichern der Werte anlegen", null, null);
    stringMatrix = lang.newStringMatrix(new Offset(-10, 50, graph,
        AnimalScript.DIRECTION_SW), new String[4][graph.getSize() + 1], "data",
        null, matrixProperties);

    int x = 10;
    int y = 550;
    for (int i = 0; i < graph.getSize(); i++) {
      Coordinates c = (Coordinates) graph.getNode(i);
      if (c.getY() > y - 10) {
        y = c.getY() + 10;
      }

    }

    try {
      stringMatrix.moveTo(null, "translate", new Coordinates(x, y), null, null);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    stringMatrix.put(0, 0, "", null, graphanimation);
    stringMatrix.put(1, 0, "Distanz", null, graphanimation);
    stringMatrix.put(2, 0, "Vorgaenger", null, graphanimation);
    stringMatrix.put(3, 0, "Besucht", null, graphanimation);
    for (int i = 0; i < graph.getSize(); i++) {
      String tmp = graph.getNodeLabel(i);
      stringMatrix.put(0, i + 1, tmp, null, graphanimation);
    }

    Variables vars = lang.newVariables();

    lang.nextStep("Hilfsvariablen anlegen");
    code.unhighlight(1);
    code.unhighlight(2);
    code.unhighlight(3);

    code.highlight(4);
    code.highlight(5);
    code.highlight(6);
    code.highlight(7);
    code.highlight(8);
    code.highlight(9);
    kommentar.setText("//Arrays initialisieren", null, graphanimation);
    stringMatrix.highlightCellColumnRange(1, 1, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    stringMatrix.highlightCellColumnRange(2, 1, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    stringMatrix.highlightCellColumnRange(3, 1, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    for (int i = 0; i < dist.length; i++) {
      dist[i] = -1;
      stringMatrix.put(1, i + 1, "" + dist[i], null, graphanimation);
      pred[i] = -1;
      stringMatrix.put(2, i + 1, "--", null, graphanimation);
      visited[i] = false;
      stringMatrix.put(3, i + 1, "" + visited[i], null, graphanimation);
    }

    lang.nextStep("Hilfsvariablen initialisieren");
    stringMatrix.unhighlightCellColumnRange(1, 1, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    stringMatrix.unhighlightCellColumnRange(2, 1, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    stringMatrix.unhighlightCellColumnRange(3, 1, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    code.unhighlight(4);
    code.unhighlight(5);
    code.unhighlight(6);
    code.unhighlight(7);
    code.unhighlight(8);
    code.unhighlight(9);
    code.highlight(10);
    stringMatrix.highlightCellRowRange(0, 3, start + 1, null, graphanimation);
    stringMatrix.put(1, start + 1, "0", null, graphanimation);
    dist[start] = 0;
    kommentar.setText("//dist von " + graph.getNodeLabel(start)
        + " auf 0 setzten", null, graphanimation);
    lang.nextStep("Bearbeitung der Knoten");
    code.unhighlight(10);
    stringMatrix.unhighlightCellRowRange(0, 3, start + 1, null, graphanimation);
    int knoten = 0;
    int kanten = 0;
    int next;
    vars.declare("int", nextKey, "", "Aktuell bearbeiteter Knoten");
    vars.setGlobal(nextKey);
    boolean first = true;
    while ((next = minVertex(dist, visited)) != -1) {
      knoten++;
      if (first) {
        first = false;
      } else {
        nextNodeQuestion(dist, visited, next);
      }
      code.highlight(13);
      kommentar
          .setText(
              "//Nächsten Knoten beziehen (unbesuchter Knoten mit kleiner Distanz falls vorhaden)",
              null, graphanimation);
      lang.nextStep();

      graph.highlightNode(next, null, graphanimation);
      vars.set(nextKey, "" + next);
      visited[next] = true;
      stringMatrix.put(3, next + 1, "" + visited[next], null, graphanimation);

      stringMatrix.highlightCellRowRange(0, 3, next + 1, null, graphanimation);
      kommentar.setText("//Knoten " + graph.getNodeLabel(next)
          + " als bearbeitet/besucht merken", null, graphanimation);
      code.unhighlight(13);
      code.highlight(14);
      lang.nextStep();

      code.unhighlight(14);
      code.highlight(16);
      kommentar.setText("//Nachbarn von " + graph.getNodeLabel(next)
          + " beziehen", null, graphanimation);
      int[] n = getEdgesForNode(next);
      for (int i = 0; i < n.length; i++) {
        graph.highlightEdge(next, i, null, graphanimation);
      }
      lang.nextStep();

      for (int i = 0; i < n.length; i++) {
        graph.unhighlightEdge(next, i, null, graphanimation);
      }
      code.unhighlight(16);
      code.highlight(17);
      code.highlight(18);
      kommentar
          .setText(
              "//über alle Nachbarn( ausgehenden Kanten) iterieren, if zum ignorieren der 0en in der Adjazenzmatrix",
              null, graphanimation);
      lang.nextStep();
      code.unhighlight(17);
      code.unhighlight(18);

      vars.openContext();
      vars.declare("int", neighborKey, "", "Aktuell betrachteter Nachbar");
      for (int j = 0; j < n.length; j++) {
        if (n[j] != 0) {
          vars.set(neighborKey, "" + j);
          kanten++;
          kommentar.setText("//Distanz für " + graph.getNodeLabel(j) + " über "
              + graph.getNodeLabel(next), null, graphanimation);
          code.highlight(19);
          graph.highlightEdge(next, j, null, graphanimation);
          int d = dist[next] + n[j];
          lang.nextStep();
          code.unhighlight(19);
          code.highlight(20);
          code.highlight(24);
          if (dist[j] > d || dist[j] == -1) {
            code.highlight(21);
            code.highlight(22);
            code.highlight(23);
            kommentar.setText("//Werte für " + graph.getNodeLabel(j)
                + " aktualisieren, da " + d + "<" + dist[j], null,
                graphanimation);
            dist[j] = d;
            stringMatrix.put(1, j + 1, "" + dist[j], null, graphanimation);
            pred[j] = next;
            stringMatrix.put(2, j + 1, "" + graph.getNodeLabel(next), null,
                graphanimation);
            visited[j] = false;
            stringMatrix.put(3, j + 1, "" + visited[j], null, graphanimation);
            // stringMatrix.unhighlightElemRowRange(0, 3, j+1,null,
            // graphanimation );
            lang.nextStep();
            code.unhighlight(21);
            code.unhighlight(22);
            code.unhighlight(23);
          } else {
            kommentar.setText("//Werte von " + graph.getNodeLabel(j)
                + " bleiben unverändert, da " + d + ">" + dist[j], null,
                graphanimation);
            lang.nextStep();
          }
          code.unhighlight(20);
          code.unhighlight(24);
          graph.unhighlightEdge(next, j, null, graphanimation);

        }
      }
      vars.closeContext();
      graph.unhighlightNode(next, null, graphanimation);
      stringMatrix
          .unhighlightCellRowRange(0, 3, next + 1, null, graphanimation);
    }

    stringMatrix.highlightCellColumnRange(0, 0, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    stringMatrix.highlightCellColumnRange(1, 0, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    stringMatrix.highlightCellColumnRange(2, 0, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    stringMatrix.highlightCellColumnRange(3, 0, stringMatrix.getNrCols() - 1,
        null, graphanimation);
    for (int i = 0; i < 29; i++) {
      code.highlight(i);
    }
    for (int i = 0; i < graph.getSize(); i++) {
      if (pred[i] != -1 || i == start) {
        graph.highlightNode(i, null, graphanimation);
      }
    }
    kommentar
        .setText(
            "//Fertig, für jeden erreichbaren Knoten wurde ein kürzester Pfad gefunden",
            null, graphanimation);
    lang.nextStep("Fazit");
    code.hide();
    for (int i = 0; i < pred.length; i++) {
      if (pred[i] != -1) {
        graph.highlightEdge(pred[i], i, null, graphanimation);
      }
    }

    SourceCodeProperties introP = new SourceCodeProperties();
    introP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 18));
    SourceCode end = lang.newSourceCode(code.getUpperLeft(), "end", null,
        introP);
    end.addCodeLine("Für das Ergebniss wurden: ", null, 0, null);
    end.addCodeLine(kanten + " Kanten bearbeitet", null, 1, null);
    end.addCodeLine(knoten + " Knoten bearbeitet", null, 1, null);
    kommentar.hide();

  }

  private int minVertex(int[] dist, boolean[] visited) {
    int x = Integer.MAX_VALUE;
    int y = -1;
    for (int i = 0; i < dist.length; i++) {
      if (!visited[i] && dist[i] < x && dist[i] >= 0) {
        x = dist[i];
        y = i;
      }
    }
    return y;
  }

  private void initQuestionGroups() {
    QuestionGroupModel groupInfo = new QuestionGroupModel("1", 3);
    lang.addQuestionGroup(groupInfo);
    QuestionGroupModel groupInfo2 = new QuestionGroupModel("2", 4);
    lang.addQuestionGroup(groupInfo2);
  }

  private int nextQuestionCount = 0;

  private void nextNodeQuestion(int[] dist, boolean[] visited, int next) {

    MultipleChoiceQuestionModel model = new MultipleChoiceQuestionModel(
        "chooseNextNode" + nextQuestionCount);
    nextQuestionCount++;
    model.setPrompt("Welcher Knoten wird als nächstes bearbeitet?");
    int points = 0;
    for (int i = 0; i < dist.length; i++) {
      points = 0;
      final StringBuilder promt = new StringBuilder("Diese Antwort ist");
      if (i == next) {
        promt.append(" richtig");
        points = 10;
      } else if (dist[i] == dist[next]) {
        promt
            .append(" fast richtig. Die Entfernung ist die kleinste, jedoch existiert ein Knoten mi der selben Entfernung der früher gefunden wird.\n");
        points = 5;
      } else {
        promt.append(" leider falsch. Die Richtige antwort wäre der Knoten "
            + graph.getNodeLabel(next) + " gewesen");
      }

      if (visited[i]) {
        promt.append("\n Außerdem ist der Knoten " + graph.getNodeLabel(i)
            + " (noch) als bereits bearbeitet markiert");
        points = 0;
      }
      model.addAnswer(graph.getNodeLabel(i), points, promt.toString());
    }

    model.setGroupID("1");

    lang.addMCQuestion(model);
  }

  private int[] getEdgesForNode(int node) {
    int[] edges = graph.getEdgesForNode(node);

    if (!directed) {
      for (int i = 0; i < edges.length; i++) {
        if (i != node) {
          edges[i] = edges[i] + graph.getAdjacencyMatrix()[i][node];
        }
      }
    }

    return edges;
  }

  public String getName() {
    return "Dijkstra";
  }

  public String getAlgorithmName() {
    return "Dijkstra";
  }

  public String getAnimationAuthor() {
    return "Konstantin Ramig";
  }

  public String getDescription() {
    return "Der Algorithmus, Dijkstra, berechnet zu einem gegebenen Knoten"
        + "\n" + "die Entfernung zu allen anderen Koten des Graphen" + "\n"
        + "und merkt sich diesen kuerzesten Pfad. Dazu wird jeweils eine"
        + "\n" + "Distanz und der Vorgaengerknoten gespeichert. Ausserdem"
        + "\n"
        + "merkt sich der Algorithmus welche Knoten er bereits besucht hat"
        + "\n" + "Nun Iteriert er ueber alle unbesuchten Knoten. Als" + "\n"
        + "naechster Knoten wird dabei immer der mit der geringsten Distanz"
        + "\n" + "gewaehlt.";
  }

  public String getCodeExample() {
    return "public int[] dijkstra (WeightedGraph G, int s) {" + "\n"
        + "	int [] dist = new int [G.size()];" + "\n"
        + "	int [] pred = new int [G.size()]; " + "\n"
        + "	boolean [] visited = new boolean [G.size()];" + "\n" + "\n"
        + "	for (int i=0; i<dist.length; i++) {" + "\n"
        + "		dist[i] = Integer.MAX_VALUE;" + "\n" + "	}" + "\n"
        + "	dist[s] = 0;" + "\n" + "\n"
        + "	for (int i=0; i<dist.length; i++) {" + "\n"
        + "		int next = minVertex (dist, visited);" + "\n"
        + "		visited[next] = true;" + "\n" + "\n"
        + "		int [] n = G.neighbors (next);" + "\n"
        + "		for (int j=0; j<n.length; j++) {" + "\n" + "			int v = n[j];"
        + "\n" + "			int d = dist[next] + G.getWeight(next,v);" + "\n"
        + "			if (dist[v] > d) {" + "\n" + "				dist[v] = d;" + "\n"
        + "				pred[v] = next;" + "\n" + "				visited[v]= false;\"" + "\n"
        + "			}" + "\n" + "		}" + "\n" + "	} " + "\n" + "}";
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
    return Generator.JAVA_OUTPUT;
  }

}