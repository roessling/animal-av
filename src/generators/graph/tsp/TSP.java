package generators.graph.tsp;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.FoundRoute;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class TSP implements Generator {
  private Language             language;
  private GraphProperties      graphProp;
  private Graph                graph;
  private int                  startNode;

  /**
   * The header text including the headline
   */
  private Text                 header;

  /**
   * The header text including the headline
   */
  // private Text debug;

  /**
   * the source code shown in the animation
   */
  private SourceCode           src;

  /**
   * info text to show the current city
   */
  private Text                 currentCity;

  /**
   * info text to show the current route
   */
  private Text                 currentRouteText;

  /**
   * info text to show the remaining cities
   */
  private Text                 remainingCitiesText;

  /**
   * info text to show the cost of the current route
   */
  private Text                 currentCost;

  /**
   * info text to show the current minimal route
   */
  private Text                 minRoute;

  /**
   * info text to show the cost of the current minimal route
   */
  private Text                 minCost;

  /**
   * counter for the recursive calls of the algorithm
   */
  private IntArray             recursiveCalls;

  /**
   * counter for the found routes
   */
  private IntArray             foundRoutes;

  private boolean[]            remainingCitiesMap;
  private TextProperties       headerProps;
  private TextProperties       textProps;
  private SourceCodeProperties sourceCodeProps;

  public void init() {
    language = new AnimalScript("Das Travelling Salesman Problem",
        "Moritz Moxter, Nico Wombacher", 1300, 700);
    language.setStepMode(true);
    language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    /*
     * // set the default header for the animation TextProperties headerProps =
     * new TextProperties();
     * headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
     * Font(Font.SANS_SERIF, Font.BOLD, 24)); header = language.newText(new
     * Coordinates(20, 30), "Das Travelling Salesman Problem","header", null,
     * headerProps);
     * 
     * // set the source code for the animation SourceCodeProperties
     * sourceCodeProps = new SourceCodeProperties();
     * sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
     * Font.SANS_SERIF, Font.PLAIN, 16));
     * sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
     * Color.RED); src = language.newSourceCode(new Coordinates(10, 550),
     * "sourceCode", null, sourceCodeProps); src.addCodeLine(
     * "def brute_force_recursive_find(current_node, delta, remaining_cities, route):"
     * , null, 0, null); // 0
     * //src.addCodeLine("if size(remaining_cities) == 0:", null, 1, null); // 1
     * src.addCodeLine(
     * "if size(remaining_cities) == 0 AND delta+distance(current_node, start) < cost(found_route):"
     * , null, 1, null); // 1 //src.addCodeLine(
     * "found_route.append(delta + distance(current_node, start), route)", null,
     * 2, null); // 2 src.addCodeLine(
     * "found_route.set(delta + distance(current_node, start), route)", null, 2,
     * null); // 2 src.addCodeLine("else:", null, 1, null); // 3
     * src.addCodeLine("for k in remaining_cities:", null, 2, null); // 4
     * src.addCodeLine("route.append(k)", null, 3, null); // 5
     * src.addCodeLine("remaining_cities.remove(k)", null, 3, null); // 6
     * src.addCodeLine(
     * "brute_force_recursive_find(k, distance(current_node, k)+delta, remaining_cities, route)"
     * , null, 3, null); // 7 src.addCodeLine("route.pop()", null, 3, null); //
     * 8
     * 
     * // create text fields for information during algorithm run TextProperties
     * textProps = new TextProperties();
     * textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
     * Font(Font.SANS_SERIF, Font.PLAIN, 14)); currentCity =
     * language.newText(new Coordinates(800, 250), "", "current_city_value",
     * null, textProps); currentCost = language.newText(new Coordinates(800,
     * 300), "", "current_cost_value", null, textProps); currentRouteText =
     * language.newText(new Coordinates(800, 350), "",
     * "current_route_text_value", null, textProps); remainingCitiesText =
     * language.newText(new Coordinates(800, 400), "",
     * "remaining_cities_text_value", null, textProps); minCost =
     * language.newText(new Coordinates(800, 450), "",
     * "current_minimal_cost_value", null, textProps); minRoute =
     * language.newText(new Coordinates(800, 500), "", "current_minimal_value",
     * null, textProps); debug = language.newText(new Coordinates(800, 550), "",
     * "debug_value", null, textProps);
     */
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // language.setStepMode(true);
    // headerProps = (TextProperties)props.getPropertiesByName("headerProp");
    textProps = (TextProperties) props.getPropertiesByName("textProp");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProp");
    graphProp = (GraphProperties) props.getPropertiesByName("graphProp");
    graph = (Graph) primitives.get("graph");
    // startNode = (Integer) primitives.get("startNode");

    startNode = graph.getPositionForNode(graph.getStartNode());

    headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    header = language.newText(new Coordinates(20, 30),
        "Das Travelling Salesman Problem", "header", null, headerProps);

    src = language.newSourceCode(new Coordinates(10, 550), "sourceCode", null,
        sourceCodeProps);
    src.addCodeLine(
        "def brute_force_recursive_find(current_node, delta, remaining_cities, route):",
        null, 0, null); // 0
    // src.addCodeLine("if size(remaining_cities) == 0:", null, 1, null); // 1
    src.addCodeLine(
        "if size(remaining_cities) == 0 AND delta+distance(current_node, start) < cost(found_route):",
        null, 1, null); // 1
    // src.addCodeLine("found_route.append(delta + distance(current_node, start), route)",
    // null, 2, null); // 2
    src.addCodeLine(
        "found_route.set(delta + distance(current_node, start), route)", null,
        2, null); // 2
    src.addCodeLine("else:", null, 1, null); // 3
    src.addCodeLine("for k in remaining_cities:", null, 2, null); // 4
    src.addCodeLine("route.append(k)", null, 3, null); // 5
    src.addCodeLine("remaining_cities.remove(k)", null, 3, null); // 6
    src.addCodeLine(
        "brute_force_recursive_find(k, distance(current_node, k)+delta, remaining_cities, route)",
        null, 3, null); // 7
    src.addCodeLine("route.pop()", null, 3, null); // 8

    currentCity = language.newText(new Coordinates(800, 250), "",
        "current_city_value", null, textProps);
    currentCost = language.newText(new Coordinates(800, 300), "",
        "current_cost_value", null, textProps);
    currentRouteText = language.newText(new Coordinates(800, 350), "",
        "current_route_text_value", null, textProps);
    remainingCitiesText = language.newText(new Coordinates(800, 400), "",
        "remaining_cities_text_value", null, textProps);
    minCost = language.newText(new Coordinates(800, 450), "",
        "current_minimal_cost_value", null, textProps);
    minRoute = language.newText(new Coordinates(800, 500), "",
        "current_minimal_value", null, textProps);
    language.newText(new Coordinates(800, 550), "", "debug_value", null,
        textProps);

    int start = startNode;

    /*
     * the Graph in Animal is really buggy. the graph generated via the generate
     * dialog in animal is not working. we need to check for errors and use the
     * default graph if there is an error
     */
    System.out.println("-----------------DEBUG-----------------");
    System.out.println("Warum ist die Adjanzenmatrix mit Nullen gefüllt?");
    int[][] adjmatrix = graph.getAdjacencyMatrix();
    for (int i = 0; i < adjmatrix.length; i++) {
      for (int j = 0; j < adjmatrix[0].length; j++) {
        System.out.print(adjmatrix[i][j] + "\t");
      }
      System.out.println();
    }

    boolean isNull = true;
    for (int i = 0; i < adjmatrix.length; i++)
      for (int j = 0; j < adjmatrix[0].length; j++)
        if (adjmatrix[i][j] != 0)
          isNull = false;

    if (isNull)
      graph = makeDefaultGraph(graphProp);
    // create the graph again in order to be able to set the graph properties
    int size = graph.getSize();
    Node[] nodes = new Node[size];
    String[] nodeLabels = new String[size];
    for (int i = 0; i < size; i++) {
      nodes[i] = graph.getNode(i);
      nodeLabels[i] = graph.getNodeLabel(i);
    }
    graph = language.newGraph(graph.getName(), graph.getAdjacencyMatrix(),
        nodes, nodeLabels, graph.getDisplayOptions(), graphProp);

    // init map for remaining cities info text
    this.remainingCitiesMap = new boolean[graph.getSize()];
    for (int i = 0; i < remainingCitiesMap.length; i++) {
      remainingCitiesMap[i] = true;
    }
    remainingCitiesMap[start] = false;

    ArrayProperties arrayProps = new ArrayProperties();
    // Redefine properties: border red, filled with gray
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED); // color
                                                                              // red
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY); // fill
                                                                       // color
                                                                       // gray

    // first step show a title page only with a header
    language.hideAllPrimitives();
    header.show();
    // language.nextStep();
    // next show a page with the description
    this.showInfoPage();
    language.nextStep("Start");
    // hide the info text
    language.hideAllPrimitives();
    header.show();
    graph.show();
    src.show();
    this.showVariables();
    language.nextStep();

    // //
    // now the algorithm
    // //

    // create the necessary variables
    ConcurrentLinkedQueue<Integer> remaining_cities = new ConcurrentLinkedQueue<Integer>();
    // add all nodes of the graph to the remaining cities
    for (int i = 0; i < graph.getSize(); i++) {
      remaining_cities.add(i);
    }
    // the start node is not in the list of remaining cities. we are already
    // there
    remaining_cities.remove(start);
    // remaining_cities.add(start);
    // the route that we already found. at the beginning it is empty with cost
    // +INF
    FoundRoute found_route = new FoundRoute();
    // the route is so far empty except the start node
    ConcurrentLinkedQueue<Integer> route = new ConcurrentLinkedQueue<Integer>();
    route.add(start);
    // at the beginning the current node is the start node
    // the costs of the current route is zero
    int delta = 0;

    // //
    // counter
    // //
    int[] inputArray = new int[1];
    int[] inputArray2 = new int[1];

    // counter properties
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    // create a counter for the recursive calls
    recursiveCalls = language.newIntArray(new Coordinates(800, 100),
        inputArray, "recursive_calls", null, arrayProps);
    recursiveCalls.hide();
    TwoValueCounter recursiveCallCounter = language.newCounter(recursiveCalls);
    language.newCounterView(recursiveCallCounter, new Coordinates(820, 85), cp,
        true, true);
    //
    foundRoutes = language.newIntArray(new Coordinates(800, 150), inputArray2,
        "found_routes", null, arrayProps);
    foundRoutes.hide();
    TwoValueCounter foundRoutesCounter = language.newCounter(foundRoutes);
    language.newCounterView(foundRoutesCounter,
        new Coordinates(820, 135), cp, true, true);

    // create a counter for possible routes found
    // IntegerVariable foundRoutes = new IntegerVariable();
    // foundRoutes.setValue(0);
    MultipleChoiceQuestionModel numOfRecursiveCallsQuestion = new MultipleChoiceQuestionModel(
        "numOfRecursiveCallsQuestion");
    numOfRecursiveCallsQuestion
        .setPrompt("Wie viele rekursive Aufrufe finden bei dem gegebenen Graphen statt?");
    numOfRecursiveCallsQuestion.addAnswer(String.valueOf(graph.getSize() * 2),
        0, "");
    numOfRecursiveCallsQuestion.addAnswer(
        String.valueOf(Math.pow(2, graph.getSize())), 1,
        "es finden genau 2^(Anzahl der Knoten) Aufrufe statt");
    numOfRecursiveCallsQuestion.addAnswer(
        String.valueOf(fac(graph.getSize() - 1)), 0, "");
    language.addMCQuestion(numOfRecursiveCallsQuestion);

    MultipleChoiceQuestionModel numOfRoutesQuestion = new MultipleChoiceQuestionModel(
        "numOfRoutesQuestion");
    numOfRoutesQuestion
        .setPrompt("Wie viele mögliche Pfade gibt es in dem gegebenen Graphen?");
    numOfRoutesQuestion.addAnswer(String.valueOf(graph.getSize() * 4), 0, "");
    numOfRoutesQuestion.addAnswer(String.valueOf(Math.pow(2, graph.getSize())),
        0, "");
    numOfRoutesQuestion.addAnswer(String.valueOf(fac(graph.getSize() - 1)), 1,
        "es gibt genau (Anzahl Knoten -1)! viele Pfade");
    language.addMCQuestion(numOfRoutesQuestion);

    //
    src.highlight(0);
    language.nextStep("Erster Aufruf");
    graph.highlightNode(start, null, null);
    this.findPath2(start, delta, remaining_cities, route, found_route, start);
    language.nextStep("Schlussbemerkung");
    this.showOutroPage();
    MultipleChoiceQuestionModel efficiencyQuestion = new MultipleChoiceQuestionModel(
        "efficiencyQuestion");
    efficiencyQuestion
        .setPrompt("Gibt es einen Algorithmus der das Travelling Salesman Problem effizienter lösen kann als in exponentieller Laufzeit?");
    efficiencyQuestion.addAnswer("Ja", 0, "");
    efficiencyQuestion
        .addAnswer(
            "nein",
            1,
            "Das Travelling Salesman Problem gehört zu den NP-vollständigen Problemen. Für Probleme dieser Klasse nimmt man an, dass es keinen deterministischen efficienten Algorithmus gibt.");
    language.addMCQuestion(efficiencyQuestion);

    language.finalizeGeneration();

    return language.toString();

  }

  public String getName() {
    return "Das Travelling Salesman Problem";
  }

  public String getAlgorithmName() {
    return "TSP Brute Force";
  }

  public String getAnimationAuthor() {
    return "Moritz Moxter, Nico Wombacher";
  }

  public String getDescription() {
    return "Ein Handlungsreisender (Traveling Salesman) besucht nacheinander eine Menge von St&auml;dten"
        + "\n"
        + "und kehrt anschließend in seine Ausgangsstadt zurück."
        + "\n"
        + "Dabei entstehen bei der Reise von einer Stadt zur n&auml;chsten bestimmte Kosten."
        + "\n"
        + "Die Kosten k&ouml;nnen bei jedem &Uuml;bergang von einer bestimmten Stadt zu einer anderen verschieden sein."
        + "\n"
        + "In der Praxis k&ouml;nnen das z.B. Benzinkosten sein. Die einzelnen St&auml;dte werden durch Knoten in einem"
        + "\n"
        + "Graphen dargestellt, die „Flugverbindungen“ durch die Kanten im Graphen. Die Kosten f&uml;r eine"
        + "\n"
        + "Flugverbindung wird durch die Kantenwichtung modelliert."
        + "\n"
        + "Das Ziel ist es, einen Pfad zu finden, bei dem jede Stadt genau einmal besucht wird, der Reisende am Ende"
        + "\n"
        + "wieder in der Anfangsstadt ist und die Kosten f&uuml;r die Reise minimal sind."
        + "\n"
        + "\n"
        + "Der hier vorgestellte primitive Algorithmus „probiert“ nacheinander alle m&ouml;glichen Rundreisen aus und"
        + "\n"
        + "setzt die gefundene Route als die Beste, wenn die Kosten der gefundenen Route geringer sind als die der bisher besten."
        + "\n"
        + "Das „ausprobieren“ kann mitunter sehr lange dauern (Exponentielle Laufzeit, O(2^n))."
        + "\n"
        + "\n"
        + "Als Eingabe bekommt der Algorithmus einen vollst&auml;ndigen ungerichteten Graphen der wie o.g. aufgebaut ist."
        + "\n";
  }

  public String getCodeExample() {
    return "\"\"\""
        + "\n"
        + "@param current_node: die aktuelle Stadt"
        + "\n"
        + "@param delta: die bisherigen Kosten der (Teil-)Route"
        + "\n"
        + "@param remaining_cities: noch nicht besuchte Städte"
        + "\n"
        + "@param route: die bisherige (Teil-)Route"
        + "\n"
        + "\"\"\""
        + "\n"
        + "def brute_force_recursive_find(current_node, delta, remaining_cities, route):"
        + "\n"
        + "    if size(remaining_cities) == 0:"
        + "\n"
        + "        found_routes.append(delta + distance(current_node, start), route)"
        + "\n"
        + "    else:"
        + "\n"
        + "        for k in remaining_cities:"
        + "\n"
        + "            route.append(k)"
        + "\n"
        + "            remaining_cities.remove(k)"
        + "\n"
        + "            brute_force_recursive_find(k, distance(current_node,k) + delta, remaining_cities, route)"
        + "\n" + "            route.pop()";
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

  private void showInfoPage() {
    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font(Font.SANS_SERIF, Font.PLAIN, 14));
    language
        .newText(
            new Coordinates(200, 230),
            "Ein Handlungsreisender (Traveling Salesman) besucht nacheinander eine Menge von Städten",
            "description1", null, textProps);
    language.newText(new Coordinates(200, 260),
        "und kehrt anschließend in seine Ausgangsstadt zurück.",
        "description2", null, textProps);
    language
        .newText(
            new Coordinates(200, 290),
            "Dabei entstehen bei der Reise von einer Stadt zur nächsten bestimmte Kosten.",
            "description3", null, textProps);
    language
        .newText(
            new Coordinates(200, 320),
            "Die Kosten können bei jedem Übergang von einer bestimmten Stadt zu einer anderen verschieden sein.",
            "description4", null, textProps);
    language
        .newText(
            new Coordinates(200, 350),
            "In der Praxis können das z.B. Benzinkosten sein. Die einzelnen Städte werden durch Knoten in einem",
            "description5", null, textProps);
    language
        .newText(
            new Coordinates(200, 380),
            "Graphen dargestellt, die „Flugverbindungen“ durch die Kanten im Graphen.",
            "description6", null, textProps);
    language
        .newText(
            new Coordinates(200, 410),
            "Das Ziel ist es, einen Pfad zu finden, bei dem jede Stadt genau einmal besucht wird und die Kosten",
            "description7", null, textProps);
    language.newText(new Coordinates(200, 440), "für die Reise minimal sind.",
        "description8", null, textProps);
    language
        .newText(
            new Coordinates(200, 470),
            "Der hier vorgestellte primitive Algorithmus „probiert“ nacheinander alle möglichen Rundreisen aus.",
            "description9", null, textProps);
    language
        .newText(
            new Coordinates(200, 500),
            "Das „ausprobieren“ kann mitunter sehr lange dauern (Exponentielle Laufzeit, O(2^n)).",
            "description10", null, textProps);
    language
        .newText(
            new Coordinates(200, 530),
            "Im Anschluss kann mit einer einfachen Suche die Route mit den geringsten Kosten gefunden werden.",
            "description11", null, textProps);
    language
        .newText(
            new Coordinates(200, 560),
            "Als Eingabe bekommt der Algorithmus einen vollständigen ungerichteten Graphen.",
            "description12", null, textProps);
  }

  private void showOutroPage() {
    this.language.hideAllPrimitives();
    this.header.show();
    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font(Font.SANS_SERIF, Font.PLAIN, 14));
    language
        .newText(
            new Coordinates(200, 230),
            "Man sieht sehr schön, dass selbst bei der geringen Problemgröße von nur vier Städten",
            "description1", null, textProps);
    language.newText(new Coordinates(200, 260),
        "bereits 2^4 = 16 Rekursive Funktionsaufrufe stattfinden.",
        "description2", null, textProps);
    language
        .newText(
            new Coordinates(200, 290),
            "TSP Instanzen mit mittlerer zweistelliger Anzahl von Knoten benötigen so bereits",
            "description3", null, textProps);
    language.newText(new Coordinates(200, 320),
        "einige Jahrtausende zu Lösung.", "description4", null, textProps);
    language
        .newText(
            new Coordinates(200, 350),
            "Das Travelling Salesman Problem ist nicht nur von theoretischem Interesse.",
            "description4", null, textProps);
    language.newText(new Coordinates(200, 380),
        "Beispielsweise in der Logistik ist das TSP ein tägliches Problem:",
        "description4", null, textProps);
    language.newText(new Coordinates(200, 410),
        "Ein Paketauto liefert m Pakete an n verschiedenen Orten aus.",
        "description4", null, textProps);
    language
        .newText(
            new Coordinates(200, 440),
            "Das Auto startet morgens in der Poststelle und muss am Abenend wieder dort sein.",
            "description4", null, textProps);
    language
        .newText(
            new Coordinates(200, 470),
            "Die Route mit der alle Auslieferungsorte abgefahren wird soll dabei möglichst kurz sein.",
            "description4", null, textProps);
    language.newText(new Coordinates(200, 500),
        "Damit haben wir eine Instanz des Travelling Salesman Problems",
        "description4", null, textProps);
  }

  private void showVariables() {
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 14));

    language.newText(new Coordinates(600, 100), "Rekursive Aufrufe",
        "recursive_calls", null, textProps);
    language.newText(new Coordinates(600, 150), "gefundene Routen",
        "number_found_routes", null, textProps);
    language.newText(new Coordinates(600, 250), "aktuelle Stadt",
        "current_city", null, textProps);
    currentCity.show();
    language.newText(new Coordinates(600, 300), "bisherige Kosten",
        "current_cost", null, textProps);
    currentCost.show();
    language.newText(new Coordinates(600, 350), "besuchte Städte",
        "current_route_list", null, textProps);
    currentRouteText.show();
    language.newText(new Coordinates(600, 400), "verbleibende Städte",
        "remaining_cities_list", null, textProps);
    remainingCitiesText.show();
    language.newText(new Coordinates(600, 450), "Kosten beste Route",
        "current_minimal_cost", null, textProps);
    minCost.show();
    language.newText(new Coordinates(600, 500), "Beste Route",
        "current_minimal", null, textProps);
    minRoute.show();
  }

  private void findPath2(int current_node, float delta,
      ConcurrentLinkedQueue<Integer> remainingCities,
      ConcurrentLinkedQueue<Integer> route, FoundRoute min, int start) {
    // update counter
    recursiveCalls.put(0, recursiveCalls.getData(0) + 1, null, null);
    // highlight current node of the graph an the source line
    graph.highlightNode(current_node, null, null);
    src.unhighlight(0);
    src.unhighlight(7);
    src.highlight(1);
    // update info text
    currentCity.setText(new Integer(current_node).toString(), null, null);
    currentCost.setText(new Float(delta).toString(), null, null);
    language.nextStep();
    //
    if (remainingCities.size() == 0) {
      // update src highlighting
      src.unhighlight(1);
      src.highlight(2);
      foundRoutes.put(0, foundRoutes.getData(0) + 1, null, null);
      graph.highlightEdge(current_node, startNode, null, null);
      int tmp = 0;
      if (graph.getEdgeWeight(current_node, start) == 0)
        tmp = graph.getEdgeWeight(start, current_node);
      else
        tmp = graph.getEdgeWeight(current_node, start);
      currentCost.setText(new Float(delta + tmp).toString(), null, null);
      language.nextStep();
      // check if this new route is better than the old one
      if ((delta + tmp) < min.cost) {
        // could be modified not to use this helper variables
        min.nodes = new LinkedList<Integer>(route);
        min.nodes.add(start);
        min.cost = delta + graph.getEdgeWeight(current_node, start);
        // update the info text
        minCost.setText(new Float(delta + tmp).toString(), null, null);
        // / @bug the text is not correctly set
        String tmp2 = new String("");
        for (Iterator<Integer> i = route.iterator(); i.hasNext();) {
          tmp2 = tmp2 + i.next() + ", ";
        }
        tmp2 = tmp2 + start;
        minRoute.setText(tmp2, null, null);
      }
      graph.unhighlightNode(current_node, null, null);
      graph.unhighlightEdge(current_node, startNode, null, null);
    } else {
      // update src highlighting
      src.unhighlight(1);
      src.unhighlight(2);
      src.highlight(3);
      language.nextStep();
      src.unhighlight(3);
      src.highlight(4);
      language.nextStep();
      for (Iterator<Integer> i = remainingCities.iterator(); i.hasNext();) {
        int k = i.next();
        ConcurrentLinkedQueue<Integer> ttp = new ConcurrentLinkedQueue<Integer>();
        for (Iterator<Integer> j = remainingCities.iterator(); j.hasNext();) {
          Integer tmp = new Integer(j.next());
          ttp.add(tmp);
        }
        // update src highlighting
        src.unhighlight(8);
        src.unhighlight(4);
        src.highlight(5);
        language.nextStep();
        // add the current node to the route
        graph.highlightEdge(current_node, k, null, null);
        route.add(k);
        remainingCitiesMap[k] = false;
        currentRouteText.setText("", null, null);
        // currentRouteText.setText(route.toString(), null, null);
        currentRouteText.setText(getVisitedCities(), null, null);
        src.unhighlight(5);
        src.highlight(6);
        language.nextStep();
        // remove current node from list of remaining nodes
        ttp.remove(k);
        remainingCitiesText.setText("", null, null);
        // remainingCitiesText.setText(ttp.toString(), null, null);
        remainingCitiesText.setText(getRemainingCities(), null, null);
        src.unhighlight(6);
        src.highlight(7);
        language.nextStep("rekursiver Aufruf");

        // recursive call
        int tmp = 0;
        if (graph.getEdgeWeight(current_node, k) == 0)
          tmp = graph.getEdgeWeight(k, current_node);
        else
          tmp = graph.getEdgeWeight(current_node, k);
        findPath2(k, delta + tmp, ttp, route, min, start);
        remainingCitiesText.setText("", null, null);
        currentRouteText.setText("", null, null);
        // remainingCitiesText.setText(ttp.toString(), null, null);
        remainingCitiesText.setText(getRemainingCities(), null, null);
        // currentRouteText.setText(route.toString(), null, null);
        currentRouteText.setText(getVisitedCities(), null, null);
        src.unhighlight(7);
        src.unhighlight(2);
        src.highlight(8);
        language.nextStep();
        // pop()
        // route = this.remove(route);
        route.remove(currentCity);
        remainingCitiesMap[k] = true;
        graph.unhighlightEdge(k, current_node, null, null);
        /*
         * ConcurrentLinkedQueue<Integer> newRoute = new
         * ConcurrentLinkedQueue<Integer>(); int soze = route.size() - 1; for (
         * int lol=0; lol<soze; lol++ ) { newRoute.add(route.poll()); } route =
         * null; route = newRoute;
         * 
         * /* for(int l=0; l<route.size()-1; l++) { route.p
         * newRoute.add(route.poll()); } route = null; route = newRoute;
         */
        currentRouteText.setText("", null, null);
        // currentRouteText.setText(route.toString(), null, null);
        currentRouteText.setText(getVisitedCities(), null, null);
        // update info text
        currentCity.setText(new Integer(current_node).toString(), null, null);
        currentCost.setText(new Float(delta).toString(), null, null);

      }
      graph.unhighlightNode(current_node, null, null);
      // remainingCitiesText.setText(tmp, null, null);
      remainingCitiesText.setText("", null, null);
      remainingCitiesText.setText(getRemainingCities(), null, null);
    }
  }

  /**
   * create a default graph for the TSP instance
   */
  private Graph makeDefaultGraph(GraphProperties gProps) {
    // properties for the graph
    /*
     * GraphProperties gProps = new GraphProperties();
     * gProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
     * gProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
     * gProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.black);
     * gProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
     * gProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
     * gProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
     */
    int[][] adjMatrix = new int[4][4];
    adjMatrix[0][0] = 0;
    adjMatrix[0][1] = 8;
    adjMatrix[0][2] = 5;
    adjMatrix[0][3] = 6;
    adjMatrix[1][0] = 8;
    adjMatrix[1][1] = 0;
    adjMatrix[1][2] = 1;
    adjMatrix[1][3] = 7;
    adjMatrix[2][0] = 5;
    adjMatrix[2][1] = 1;
    adjMatrix[2][2] = 0;
    adjMatrix[2][3] = 4;
    adjMatrix[3][0] = 6;
    adjMatrix[3][1] = 7;
    adjMatrix[3][2] = 4;
    adjMatrix[3][3] = 0;

    Node[] graphNodes = new Node[4];
    graphNodes[0] = new Coordinates(100, 300);
    graphNodes[1] = new Coordinates(400, 120);
    graphNodes[2] = new Coordinates(200, 150);
    graphNodes[3] = new Coordinates(120, 100);

    String[] labels = { "0", "1", "2", "3" };

    graph = language.newGraph("graph", adjMatrix, graphNodes, labels, null,
        gProps);
    return graph;
  }

  private String getRemainingCities() {
    String result = "";
    for (int i = 0; i < this.remainingCitiesMap.length; i++) {
      if (remainingCitiesMap[i]) {
        result = result + String.valueOf(i) + ", ";
      }
    }
    return result;
  }

  private String getVisitedCities() {
    String result = "";
    for (int i = 0; i < this.remainingCitiesMap.length; i++) {
      if (!remainingCitiesMap[i]) {
        result = result + String.valueOf(i) + ", ";
      }
    }
    return result;
  }

  /*
   * private ConcurrentLinkedQueue<Integer>
   * remove(ConcurrentLinkedQueue<Integer> q) {
   * 
   * ConcurrentLinkedQueue<Integer> temp = new ConcurrentLinkedQueue<Integer>();
   * 
   * int t = q.size(); while(t > 1){ temp.add(q.poll()); t--; }
   * this.debug.setText(q.toString(), null, null); this.debug.show(); q.clear();
   * return temp; }
   */

  private int fac(int num) {
    if (num == 0)
      return 1;
    else
      return num * fac(num - 1);
  }
}