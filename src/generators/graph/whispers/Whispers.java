package generators.graph.whispers;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class Whispers implements Generator {
  private Language             lang;

  private Graph                graph;
  private Color                cHighlight;
  private Color                cNormal;
  private Color                cText;
  private Color                cTextHighlight;
  private SourceCodeProperties codeProperties;
  private MatrixProperties     matrixProperties;
  private GraphProperties      graphProperties;

  private Circle[]             circles;
  private Text[]               names;
  private Timing               duration;
  private Random               rand;
  private boolean              directed;

  private Text                 header;
  private Rect                 headerR;
  private SourceCode           code;

  private final int            nodeSize = 15;

  private Color[]              groupC;
  private ArrayList<Integer>   iterations;
  private Vector<int[]>        results;

  public void init() {
    lang = new AnimalScript("Chinese Whispers Graph Clustering",
        "Konstantin Ramig", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    initQuestionGroups();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    graph = (Graph) primitives.get("graph");
    graphProperties = (GraphProperties) props.getPropertiesByName("graphProp");

    cNormal = (Color) graphProperties
        .get(AnimationPropertiesKeys.NODECOLOR_PROPERTY);
    cHighlight = (Color) graphProperties
        .get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
    cText = (Color) graphProperties
        .get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
    cTextHighlight = (Color) graphProperties
        .get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY);

    // codeProperties = (SourceCodeProperties)
    // props.getPropertiesByName("sourceCode");
    // matrixProperties = (MatrixProperties)
    // props.getPropertiesByName("matrix");

    if (matrixProperties == null) {
      matrixProperties = new MatrixProperties();
      matrixProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
          Color.YELLOW);
      matrixProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
          Color.GREEN);
    }

    if (codeProperties == null) {
      codeProperties = new SourceCodeProperties();
      codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.MONOSPACED, Font.BOLD, 18));
      codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
          Color.RED);
      codeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    }

    duration = new MsTiming(300);
    rand = new Random();
    results = new Vector<int[]>();
    iterations = new ArrayList<Integer>();
    groupC = rainbow(graph.getSize());

    GraphProperties prop = graph.getProperties();
    directed = (Boolean) prop.get(AnimationPropertiesKeys.DIRECTED_PROPERTY);
    graphProperties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, directed);

    intro();
    showCodeGraph();
    whispers();
    showResult();

    int g = groupCount(results.get(0));
    int g2 = 0;
    int toShow = 1;
    for (int i = 1; i < 10; i++) {
      whispersfast();
      g2 = groupCount(results.lastElement());
      if (g != g2)
        toShow = i;
    }

    showAlternateResult(toShow);

    lang.finalizeGeneration();
    return lang.toString();
  }

  private void intro() {
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.BOLD, 24));
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    header = lang.newText(new Coordinates(200, 50), getAlgorithmName(),
        "header", null, tp);
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    headerR = lang.newRect(
        new Offset(-5, -5, header, AnimalScript.DIRECTION_NW), new Offset(5, 5,
            header, AnimalScript.DIRECTION_SE), "hrect", null, rp);
    // Intro
    SourceCodeProperties introP = new SourceCodeProperties();
    introP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 18));
    introP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode intro = lang.newSourceCode(new Offset(-160, 30, header,
        AnimalScript.DIRECTION_SW), "intro", null, introP);
    intro
        .addCodeLine(
            "Chinese Whispers Graph Clustering ist ein Algorithmus zum finden von Clustern in",
            null, 0, null);
    intro
        .addCodeLine(
            "Graphen. Er ist Parameterfrei( Die Anzahl der zu findenden Cluster muss vorher nicht ",
            null, 0, null);
    intro
        .addCodeLine(
            "bestimmt werden etc.) und ist sehr effizient mit einer Komplexität von O(n^2) mit ",
            null, 0, null);
    intro
        .addCodeLine(
            "n = Anzahl der Kanten. Um die Cluster zu finden wird jedem Knoten erst einmal seine",
            null, 0, null);
    intro
        .addCodeLine(
            "eigene Farbe( Gruppe) zugewiesen. So lange wie Änderungen auftreten wird in einer zufälligen",
            null, 0, null);
    intro
        .addCodeLine(
            "Reihenfolge über jeden Knoten im Graphen iteriert. Der bearbeitete Knoten nimmt dabei die",
            null, 0, null);
    intro
        .addCodeLine(
            "Farbe( Gruppe) an zu der er die stärkste Verbindung hat. Existieren mehrere Möglichkeiten",
            null, 0, null);
    intro
        .addCodeLine(
            "wählt der Knoten zufällig eine von diesen aus. Durch die Zufallsprozesse ist der Algorithmus",
            null, 0, null);
    intro
        .addCodeLine(
            "weder Deterministisch noch konvergiert er gegen eine spezielle Lösung.",
            null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "Achtung: Durch die nicht deterministische Natur des Algorithmus kann es zu endlosschleifen kommen",
            null, 0, null);
    intro
        .addCodeLine(
            "zum Beispiel durch Knoten die zwei Gruppen angehören können und sich in jeder Iteration um entscheiden ",
            null, 0, null);
    intro.addCodeLine("diesen Fall gilt es zu beachten!", null, 0, null);

    lang.nextStep("Intro");
    intro.hide();

  }

  private void showCodeGraph() {

    circles = new Circle[graph.getSize()];
    names = new Text[graph.getSize()];

    Coordinates[] graphNodes = new Coordinates[graph.getSize()];
    String[] labels = new String[graph.getSize()];
    for (int i = 0; i < graph.getSize(); i++) {
      graphNodes[i] = (Coordinates) graph.getNode(i);
      labels[i] = graph.getNodeLabel(i);
    }
    graph = lang.newGraph("graph", graph.getAdjacencyMatrix(), graphNodes,
        labels, null, graphProperties);

    try {
      graph.moveTo(null, "translate", new Coordinates(10, 100), null, null);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    CircleProperties circleProperties = new CircleProperties();
    circleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    circleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);

    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, nodeSize + 10));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, cText);

    for (int i = 0; i < graphNodes.length; i++) {
      final int x = graphNodes[i].getX() - nodeSize / 2;
      final int y = graphNodes[i].getY() + 90 - nodeSize / 2;
      this.circles[i] = lang.newCircle(new Coordinates(x, y), nodeSize, "circ"
          + i, null, circleProperties);
      this.names[i] = lang.newText(new Coordinates(x - nodeSize + 8, y
          - nodeSize), labels[i], "name" + i, null, textProperties);
    }

    code = lang.newSourceCode(new Offset(40, 70, graph,
        AnimalScript.DIRECTION_NE), "code", null, codeProperties);
    code.addCodeLine("initialize:", null, 0, null);
    code.addCodeLine("forall vi in V: class(vi)=i;", null, 1, null);
    code.addCodeLine("", null, 0, null);
    code.addCodeLine("while changes:", null, 0, null);
    code.addCodeLine("randomiz order;", null, 1, null);
    code.addCodeLine("forall v in V:", null, 1, null);
    code.addCodeLine("class(v) = highest ranked class", null, 2, null);
    code.addCodeLine("in neighborhood of v;", null, 5, null);

    lang.nextStep();
  }

  private void whispers() {

    int[] group = new int[graph.getSize()];
    int[] weight = new int[graph.getSize()];
    ArrayList<Integer> nodes = new ArrayList<Integer>();

    int iterations = 1;
    boolean change = true;

    StringMatrix rArray = lang.newStringMatrix(new Offset(10, 150, graph,
        AnimalScript.DIRECTION_SW), new String[2][graph.getSize() + 1], "data",
        null, matrixProperties);

    code.highlight(0);
    code.highlight(1);
    rArray.put(0, 0, "Reihenfolge", null, null);
    rArray.put(1, 0, "Gruppe akt.", null, null);
    for (int i = 0; i < graph.getSize(); i++) {
      nodes.add(i);
      group[i] = i;
      circles[i].changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, groupC[i],
          null, duration);
      rArray.put(0, i + 1, graph.getNodeLabel(i), null, null);
      rArray.put(1, i + 1, "g" + group[i], null, null);
      weight[i] = 0;
    }

    lang.nextStep("Initialisierung");

    code.unhighlight(0);
    code.unhighlight(1);
    ArrayList<Integer> options = new ArrayList<Integer>();

    while (change && iterations < graph.getSize()) {
      change = false;
      Collections.shuffle(nodes);

      for (int i = 0; i < nodes.size(); i++) {
        rArray.put(0, i + 1, graph.getNodeLabel(nodes.get(i)), null, duration);
        rArray.put(1, i + 1, "g" + group[nodes.get(i)], null, duration);
      }

      code.highlight(3);
      code.highlight(4);
      lang.nextStep("Iteration" + iterations);
      code.unhighlight(3);
      code.unhighlight(4);

      code.highlight(5);
      code.highlight(6);
      code.highlight(7);
      for (int i : nodes) {
        rArray
            .highlightCellRowRange(0, 1, nodes.indexOf(i) + 1, null, duration);

        circles[i].changeColor(AnimalScript.COLORCHANGE_COLOR, cHighlight,
            null, duration);
        names[i].changeColor(AnimalScript.COLORCHANGE_COLOR, cTextHighlight,
            null, duration);

        for (int e = 0; e < graph.getSize(); e++) {
          int g = group[e];
          weight[g] = weight[g] + graph.getEdgeWeight(i, e);
          if (!directed) {
            weight[g] = weight[g] + graph.getEdgeWeight(e, i);
          }

          if (weight[g] != 0) {
            graph.highlightEdge(i, e, null, duration);
            graph.highlightEdge(e, i, null, duration);
          }
        }

        int max = 0;
        for (int j = 0; j < weight.length; j++) {
          if (weight[j] > max) {
            options.clear();
            options.add(j);
            max = weight[j];
          } else if (max == weight[j] && max != 0) {
            options.add(j);
          }
        }

        if (rand.nextBoolean())
          nextNodeQuestion(i, max, weight);
        lang.nextStep();

        int g = rand.nextInt(options.size());
        if (group[i] != options.get(g)) {
          group[i] = options.get(g);
          change = true;
          circles[i].changeColor(AnimalScript.COLORCHANGE_FILLCOLOR,
              groupC[group[i]], null, duration);
          rArray.put(1, nodes.indexOf(i) + 1, "g" + group[i], null, duration);
        }
        lang.nextStep();

        rArray.unhighlightCellRowRange(0, 1, nodes.indexOf(i) + 1, null,
            duration);

        circles[i].changeColor(AnimalScript.COLORCHANGE_COLOR, cNormal, null,
            duration);
        names[i].changeColor(AnimalScript.COLORCHANGE_COLOR, cTextHighlight,
            null, duration);

        for (int j = 0; j < weight.length; j++) {
          graph.unhighlightEdge(i, j, null, duration);
          graph.unhighlightEdge(j, i, null, duration);
        }
        Arrays.fill(weight, 0);
      }
      code.unhighlight(5);
      code.unhighlight(6);
      code.unhighlight(7);

      iterations++;
    }

    results.add(group);
    this.iterations.add(iterations);

    code.hide();
    rArray.hide();
  }

  private void whispersfast() {

    int[] group = new int[graph.getSize()];
    int[] weight = new int[graph.getSize()];
    ArrayList<Integer> nodes = new ArrayList<Integer>();

    int iterations = 1;
    boolean change = true;

    for (int i = 0; i < graph.getSize(); i++) {
      nodes.add(i);
      group[i] = i;
    }
    ArrayList<Integer> options = new ArrayList<Integer>();

    while (change && iterations < graph.getSize()) {
      change = false;
      Collections.shuffle(nodes);

      for (int i : nodes) {

        Arrays.fill(weight, 0);

        for (int e = 0; e < graph.getSize(); e++) {
          int g = group[e];
          weight[g] = weight[g] + graph.getEdgeWeight(i, e);
          if (!directed) {
            weight[g] = weight[g] + graph.getEdgeWeight(e, i);
          }
        }

        int max = 0;
        for (int j = 0; j < weight.length; j++) {
          if (weight[j] > max) {
            options.clear();
            options.add(j);
            max = weight[j];
          } else if (max == weight[j] && max != 0) {
            options.add(j);
          }
        }

        int g = rand.nextInt(options.size());
        if (group[i] != options.get(g)) {
          group[i] = options.get(g);
          change = true;
        }
      }
      iterations++;
    }

    results.add(group);
    this.iterations.add(iterations);
  }

  private void showResult() {
    graph.moveBy("translate", 350, 30, null, duration);
    for (Circle c : circles) {
      c.moveBy("translate", 350, 30, null, duration);
    }
    for (Text c : names) {
      c.moveBy("translate", 350, 30, null, duration);
    }

    SourceCodeProperties introP = new SourceCodeProperties();
    introP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 18));
    SourceCode end = lang.newSourceCode(header.getUpperLeft(), "end", null,
        introP);
    end.addCodeLine("Nach " + iterations.get(0) + " Iterationen", null, 0, null);
    end.addCodeLine("wurden diese " + groupCount(results.get(0))
        + " Cluster gefunden", null, 0, null);
    end.moveBy("translate", -150, 70, null, null);
    lang.nextStep("Ergebniss");
    end.hide();
  }

  private void showAlternateResult(int toShow) {
    int[] result = results.get(toShow);
    for (int i = 0; i < result.length; i++) {
      circles[i].changeColor(AnimalScript.COLORCHANGE_FILLCOLOR,
          groupC[result[i]], null, duration);
    }
    SourceCodeProperties introP = new SourceCodeProperties();
    introP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 18));
    SourceCode end = lang.newSourceCode(header.getUpperLeft(), "end", null,
        introP);
    end.addCodeLine("nach " + toShow + " weiteren Anwendungen", null, 0, null);
    end.addCodeLine("des Algorithmus wurde dieses Clustering", null, 0, null);
    end.addCodeLine("mit " + groupCount(result) + " Clustern gefunden", null,
        0, null);
    end.addCodeLine("dafür wurden " + iterations.get(toShow)
        + " Iterationen benötigt", null, 0, null);
    end.addCodeLine("", null, 0, null);
    end.addCodeLine("weitere anwedungen könnten auch noch", null, 0, null);
    end.addCodeLine("weitere Gruppierungen( Clusterings) finden", null, 0, null);
    end.moveBy("translate", -190, 70, null, null);

    lang.nextStep("Alternatives Ergebniss");
    graph.hide();
    end.hide();
    for (int i = 0; i < circles.length; i++) {
      circles[i].hide();
      names[i].hide();
    }

    Hashtable<Integer, Integer> clusterCounts = new Hashtable<Integer, Integer>();
    int count;
    for (int[] res : results) {
      count = groupCount(res);
      if (clusterCounts.containsKey(count)) {
        clusterCounts.put(count, clusterCounts.get(count) + 1);
      } else {
        clusterCounts.put(count, 1);
      }
    }

    SourceCode alt = lang.newSourceCode(new Offset(-100, 50, headerR,
        AnimalScript.DIRECTION_SW), "alt", null, introP);
    alt.addCodeLine("Nach " + results.size()
        + " Anwendungen des Algorithmus auf den Graphen wurden", null, 0, null);
    alt.addCodeLine("unterschiedlich viele Cluster pro Clustering gefunden",
        null, 0, null);
    StringMatrix mat = lang.newStringMatrix(new Offset(10, 30, alt,
        AnimalScript.DIRECTION_SW), new String[2][clusterCounts.entrySet()
        .size() + 1], "mat", null, new MatrixProperties());
    mat.put(0, 0, "Cluster", null, null);
    mat.put(1, 0, "Häufigkeit", null, null);
    int i = 1;
    for (int key : clusterCounts.keySet()) {
      mat.put(0, i, "" + key, null, null);
      mat.put(1, i, "" + clusterCounts.get(key), null, null);
      i++;
    }
  }

  private void initQuestionGroups() {
    QuestionGroupModel groupInfo = new QuestionGroupModel("1", 5);
    lang.addQuestionGroup(groupInfo);
    QuestionGroupModel groupInfo2 = new QuestionGroupModel("2", 5);
    lang.addQuestionGroup(groupInfo2);
  }

  private int selectionQuestion = 0;

  private void nextNodeQuestion(int node, int max, int[] weight) {

    MultipleSelectionQuestionModel model = new MultipleSelectionQuestionModel(
        "possibleGroupsQuestion" + selectionQuestion);

    selectionQuestion++;
    model.setPrompt("Zwischen welchen Gruppe kann der Knoten "
        + graph.getNodeLabel(node) + " wählen?");

    int w = 0;
    for (int i = 0; i < weight.length; i++) {
      w = weight[i];
      if (w == max) {
        model.addAnswer("Gruppe g" + i, 2, "richtig");
      } else if (w == 0) {
        model.addAnswer("Gruppe g" + i, -2, "leider Falsch. Der Knoten "
            + graph.getNodeLabel(node)
            + " besitzt keinerlei verbindung zur Gruppe g" + i);
      } else if (w < max) {
        model
            .addAnswer(
                "Gruppe g" + i,
                -2,
                "leider Falsch. Der Knoten "
                    + graph.getNodeLabel(node)
                    + " ist zu mindestens einer Gruppe stärker verbunden als zur Gruppe g"
                    + i);
      }
    }

    model.setGroupID("1");

    lang.addMSQuestion(model);
  }

  private int groupCount(int[] result) {
    ArrayList<Integer> groups = new ArrayList<Integer>();

    for (int i : result) {
      if (!groups.contains(i)) {
        groups.add(i);
      }
    }

    return groups.size();
  }

  private Color[] rainbow(int colorCount) {
    float r, g, b;

    float min, max;
    min = 0.2f;
    max = 0.95f;

    Color[] colors = new Color[colorCount];

    max = max - min;
    for (int i = 0; i < colors.length; i = i + 1) {
      r = min + rand.nextFloat() * max;
      g = min + rand.nextFloat() * max;
      b = min + rand.nextFloat() * max;

      colors[i] = new Color(r, g, b);

    }
    return colors;
  }

  public String getName() {
    return "Chinese Whispers Graph Clustering";
  }

  public String getAlgorithmName() {
    return "Chinese Whispers Graph Clustering";
  }

  public String getAnimationAuthor() {
    return "Konstantin Ramig";
  }

  public String getDescription() {
    return "Chinese Whispers Graph Clustering ist ein Algorithmus zum finden von Clustern in Graphen. Er ist"
        + "\n"
        + "Parameterfrei( Die Anzahl der zu findenden Cluster muss vorher nicht bestimmt werden etc.) und ist "
        + "\n"
        + "sehr effizient mit einer Komplexität von O(n^2) mit n = Anzahl der Kanten."
        + "\n"
        + "Um die Cluster zu finden wird jedem Knoten erst einmal seine eigene Farbe( Gruppe) zugewiesen. So "
        + "\n"
        + "lange wie Änderungen auftreten wird in einer zufälligen Reihenfolge über jeden Knoten im Graphen "
        + "\n"
        + "iteriert. Der bearbeitete Knoten nimmt dabei die Farbe( Gruppe) an zu der er die stärkste Verbindung "
        + "\n"
        + "hat. Existieren mehrere Möglichkeiten wählt der Knoten zufällig eine von diesen aus."
        + "\n"
        + "Durch die Zufallsprozesse ist der Algorithmus weder Deterministisch noch konvergiert er gegen "
        + "\n" + "eine spezielle Lösung." + "\n";
  }

  public String getCodeExample() {
    return "initialize:" + "\n" + "	forall vi in V: class(vi)=i;" + "\n"
        + "while changes:" + "\n" + "	forall v in V, randomized order:" + "\n"
        + "		class(v)=highest ranked" + "\n" + "		class in neighborhood of v;";
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