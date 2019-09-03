/*
 * AnnotatedRingGenerator.java
 * (c) 2010, Dominik Fischer, all rights reserved.
 */
package generators.graph;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * <p>
 * A {@link Generator} that creates an Animation displaying the Ring Leader
 * Election.
 * </p>
 * 
 * @author Dominik Fischer
 */
public class AnnotatedRingGenerator extends AnnotatedAlgorithm {

  private String generateError(final String errorText) {
    final Language language = new AnimalScript("Ring Election",
        "Dominik Fischer", 640, 480);
    language.newText(new Coordinates(0, 0), errorText, "error", null);
    return language.toString();
  }

  @Override
  public String generate(final AnimationPropertiesContainer props,
      final Hashtable<String, Object> primitives) {
    init();

    // Check hops on uniqueness and find start index.
    int start;
    int[] hops = (int[]) primitives.get("hops");
    {
      int startID = (Integer) primitives.get("start");
      final Collection<Integer> alreadyKnown = new HashSet<Integer>();
      int startIndex = -1;
      for (int i = 0; i < hops.length; i++) {
        if (alreadyKnown.contains(hops[i])) {
          return generateError("IDs shall be unique.");
        }
        alreadyKnown.add(hops[i]);
        if (hops[i] == startID) {
          startIndex = i;
        }
      }
      if (startIndex == -1) {
        return generateError("Start is not an ID.");
      }
      start = startIndex;
    }

    lang = new AnimalScript("Ring Election", "Dominik Fischer", 640, 480);
    lang.setStepMode(true);

    // Setup.
    { // Title.
      final TextProperties properties = new TextProperties();
      properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 24));
      lang.newText(new Coordinates(40, 30), "Ring Election", "title", null,
          properties);
    }
    { // Title frame.
      final RectProperties properties = new RectProperties();
      properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
      properties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      properties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
      lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5, 5, "title",
          "SE"), "titleFrame", null, properties);
    }
    final Graph graph;
    {
      final GraphProperties properties = new GraphProperties();
      properties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
      properties
          .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      properties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
      properties.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
      final int radius = hops.length * 15;
      graph = lang.newGraph(
          "ring",
          createRingAdjacency(hops.length),
          createCircle(new Coordinates(radius + 350, radius + 100), radius,
              hops.length), arrayToString(hops), null, properties);
      int node = 0;
      int nextNode = 1;
      do {
        node = nextNode;
        nextNode = (node + 1) % hops.length;
        graph.hideEdgeWeight(node, nextNode, null, null);
      } while (nextNode != 1);
      graph.hide();
    }
    final SourceCode intro;
    { // Introduction text.
      final SourceCodeProperties properties = new SourceCodeProperties();
      properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.PLAIN, 18));
      intro = lang.newSourceCode(new Coordinates(20, 75), "intro", null,
          properties);
      intro.addCodeLine(
          "Dieser Algorithmus bestimmt den neuen Leiter in einem ringförmigen",
          "1", 0, null);
      intro.addCodeLine(
          "Rechnernetz unter Berücksichtigung eindeutiger Priorität.", "2", 0,
          null);
      intro
          .addCodeLine(
              "Der Initiator des Algorithmus versendet zunächst seine eigene Identi-",
              "3", 0, null);
      intro.addCodeLine(
          "fikationsnummer. Jeder Rechner, der daraufhin eine solche Nummer",
          "4", 0, null);
      intro.addCodeLine(
          "empfängt schickt entweder diese oder, wenn sie größer ist, seine",
          "5", 0, null);
      intro
          .addCodeLine(
              "eigene weiter. Sobald ein Rechner seine eigene Nummer zurück erhält,",
              "6", 0, null);
      intro
          .addCodeLine(
              "ernennt er sich zum Leiter und meldet dies durch die Nachricht g<ID>.",
              "7", 0, null);
    }
    final SourceCode begin;
    { // Source codes.
      final SourceCodeProperties properties = (SourceCodeProperties) props
          .getPropertiesByName("Quellcode");
      sourceCode = lang.newSourceCode(new Coordinates(20, 75), "code", null,
          properties);
      begin = lang.newSourceCode(new Coordinates(20, 75), "begin", null,
          properties);
      begin
          .addCodeLine("Rechner " + hops[start] + " initiiert den Algorithmus",
              "1", 0, null);
      begin.addCodeLine("indem er seine eigene ID versendet.", "1", 0, null);
      begin.hide();
    }
    parse();
    sourceCode.hide();
    lang.nextStep();

    intro.hide();
    graph.show();
    begin.show();
    lang.nextStep();

    begin.highlight(0);
    int node = start;
    graph.highlightNode(node, null, null);
    lang.nextStep();

    begin.toggleHighlight(0, 1);
    graph.unhighlightNode(node, null, null);
    int nextNode = (node + 1) % hops.length;
    graph.highlightEdge(node, nextNode, null, null);
    int value = hops[node];
    graph.setEdgeWeight(node, nextNode, value, null, null);
    graph.showEdgeWeight(node, nextNode, null, null);
    vars.declare("int", "messages", "1");
    {
      final Text messages = lang.newText(new Coordinates(20, 300), "...",
          "messages", null);
      final TextUpdater messageUpdater = new TextUpdater(messages);
      messageUpdater.addToken("versandte Nachrichten: ");
      messageUpdater.addToken(vars.getVariable("messages"));
      messageUpdater.update();
    }
    lang.nextStep();

    begin.hide();
    sourceCode.show();
    while (value != hops[nextNode]) {
      exec("incoming");
      graph.highlightNode(nextNode, null, null);
      lang.nextStep(hops[nextNode] + " erhält eine Wahl-Nachricht");

      exec("w");
      lang.nextStep();

      exec("wmyid");
      lang.nextStep();

      exec("wotherid");
      lang.nextStep();

      exec("sendmax");
      graph.hideEdgeWeight(node, nextNode, null, null);
      graph.unhighlightEdge(node, nextNode, null, null);
      node = nextNode;
      nextNode = (node + 1) % hops.length;
      value = Math.max(hops[node], value);
      graph.setEdgeWeight(node, nextNode, value, null, null);
      graph.showEdgeWeight(node, nextNode, null, null);
      graph.highlightEdge(node, nextNode, null, null);
      lang.nextStep();
      CheckpointUtils.checkpointEvent(this, "chooseId", new Variable("choose",
          value));// /////////////////////
      graph.unhighlightNode(node, null, null);
    }
    exec("incoming");
    graph.highlightNode(nextNode, null, null);
    lang.nextStep(hops[nextNode] + " erhält eine Wahl-Nachricht");

    exec("w");
    lang.nextStep();

    exec("wmyid");
    lang.nextStep();

    exec("sendmyid");
    graph.unhighlightEdge(node, nextNode, null, null);
    graph.hideEdgeWeight(node, nextNode, null, null);
    node = nextNode;
    nextNode = (node + 1) % hops.length;
    graph.setEdgeWeight(node, nextNode, "g" + value, null, null);
    graph.showEdgeWeight(node, nextNode, null, null);
    graph.highlightEdge(node, nextNode, null, null);
    lang.nextStep(hops[node] + " gibt bekannt, Leiter zu sein");

    graph.unhighlightNode(node, null, null);
    while (value != hops[nextNode]) {
      exec("incoming");
      graph.highlightNode(nextNode, null, null);
      lang.nextStep(hops[nextNode] + " erhält die Gewählt-Nachricht");

      exec("w");
      lang.nextStep();

      exec("g");
      lang.nextStep();

      exec("gmyid");
      lang.nextStep();

      exec("gotherid");
      lang.nextStep();

      exec("sendmsg");
      graph.hideEdgeWeight(node, nextNode, null, null);
      graph.unhighlightEdge(node, nextNode, null, null);
      node = nextNode;
      nextNode = (node + 1) % hops.length;
      value = Math.max(hops[node], value);
      graph.setEdgeWeight(node, nextNode, "g" + value, null, null);
      graph.showEdgeWeight(node, nextNode, null, null);
      graph.highlightEdge(node, nextNode, null, null);
      lang.nextStep();
      CheckpointUtils.checkpointEvent(this, "chosenId", new Variable("chosen",
          value));// /////////////////////
      graph.unhighlightNode(node, null, null);
    }

    exec("incoming");
    graph.highlightNode(nextNode, null, null);
    lang.nextStep();

    exec("w");
    lang.nextStep();

    exec("g");
    lang.nextStep();

    exec("gmyid");
    lang.nextStep();

    exec("end");
    graph.unhighlightEdge(node, nextNode, null, null);
    graph.hideEdgeWeight(node, nextNode, null, null);
    lang.nextStep("Leiter etabliert");

    sourceCode.hide();
    TextProperties properties = (TextProperties) props
        .getPropertiesByName("Ergebnistext");
    lang.newText(new Coordinates(50, 125),
        value + " ist als Leiter etabliert.", "result", null, properties);
    lang.nextStep();
    return lang.toString();
  }

  /**
   * <p>
   * Creates a given number of {@link Coordinates} placed in a circle.
   * </p>
   * 
   * @param center
   *          center of the circle.
   * @param radius
   *          radius of the circle.
   * @param n
   *          number of points to create.
   * @return a circle of points.
   */
  private Coordinates[] createCircle(final Coordinates center,
      final int radius, int n) {
    final Coordinates[] circle = new Coordinates[n];
    for (int i = 0; i < n; i++) {
      final double ratio = Math.PI * 2 * i / n;
      circle[i] = new Coordinates(
          (int) (Math.sin(ratio) * radius + center.getX()),
          (int) (Math.cos(ratio) * radius + center.getY()));
    }
    return circle;
  }

  /**
   * <p>
   * Creates an nxn adjacency matrix that links each graph knot to its follower.
   * </p>
   * 
   * @param n
   *          matrix dimension.
   * @return the matrix.
   */
  private int[][] createRingAdjacency(final int n) {
    int[][] result = new int[n][];
    int i = n - 1;
    result[i] = new int[n];
    result[i][0] = 1;
    while (i-- > 0) {
      result[i] = new int[n];
      result[i][i + 1] = 1;
    }
    return result;
  }

  /**
   * <p>
   * Transforms an array to String representations element-wise.
   * </p>
   * 
   * @param array
   *          the array to transform.
   * @return the String array.
   */
  private String[] arrayToString(int[] array) {
    final String[] result = new String[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = String.valueOf(array[i]);
    }
    return result;
  }

  @Override
  public String getAlgorithmName() {
    return "Ring Leader Election";
  }

  @Override
  public String getAnimationAuthor() {
    return "Dominik Fischer";
  }

  @Override
  public String getAnnotatedSrc() {
    return "Eingehende Nachricht. @label(\"incoming\")\n"
        + "Die Nachricht ist eine \\\"Wahl\\\"-Nachricht: @label(\"w\")\n"
        + "  Empfangene ID ist eigene ID: @label(\"wmyid\")\n"
        + "    Sende Gewählt-Nachricht mit eigener ID weiter. @label(\"sendmyid\")  @inc(\"messages\")\n"
        + "  Empfangene ID ist andere ID:  @label(\"wotherid\")\n"
        + "    Sende max(eingegangene ID, eigene ID) weiter @label(\"sendmax\") @inc(\"messages\")\n"
        + "Die Nachricht ist eine \\\"Gewählt\\\"-Nachricht @label(\"g\")\n"
        + "  Empfangene ID ist eigene ID: @label(\"gmyid\")\n"
        + "    Ende. @label(\"end\")\n"
        + "  Empfangene ID ist andere ID: @label(\"gotherid\")\n"
        + "    Sende Nachricht unverändert weiter. @label(\"sendmsg\") @inc(\"messages\")\n";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Dieser Algorithmus bestimmt den neuen Leiter in einem ringförmigen "
        + "Rechnernetz unter Berücksichtigung eindeutiger Priorität. Der "
        + "Initiator des Algorithmus versendet zunächst seine eigene Identi"
        + "fikationsnummer. Jeder Rechner, der daraufhin eine solche Nummer "
        + "empfängt schickt entweder diese oder, wenn sie größer ist, seine"
        + "eigene weiter. Sobald ein Rechner seine eigene Nummer zurück erhält, "
        + "ernennt er sich zum Leiter und meldet dies durch die Nachricht g<ID>.";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  @Override
  public String getName() {
    return getAlgorithmName();
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

} // End of class AnnotatedRingGenerator.
