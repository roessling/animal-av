package generators.graph.bully;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.Process;
import generators.graph.helpers.Status;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class BullyGenerator implements Generator {

  /**
   * The header text including the headline
   */
  private Text                 header;

  /**
   * the source code shown in the animation
   */
  private SourceCode           src;

  private Text                 electingProcess;

  private Text                 coordinatorText;
  private Text                 message;

  private Text                 infotext;

  ArrayList<Process>           pool;
  ArrayList<Process>           newElec;
  Process                      coordinator;

  private Language             lang;
  private GraphProperties      graphPropsRed;
  private Graph                graph;

  private Text                 electingProcessLabel;

  private Text                 coordinatorLabel;

  // private GraphProperties graphPropsGreen;

  // private GraphProperties graphPropsBlue;

  // private Graph ggraph;

  // private Graph rgraph;

  private Text                 mLabel;

  private int                  probability;

  private TextProperties       textProps;

  private SourceCodeProperties sourceCodeProps;

  public void init() {
    lang = new AnimalScript("Der Bully Algorithmus",
        "Moritz Moxter, Nico Wombacher", 1000, 1000);
    lang.setStepMode(true);

    // set the default header for the animation
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    header = lang.newText(new Coordinates(20, 30), "Der Bully Algorithmus",
        "header", null, headerProps);

    // set the source code for the animation
    SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);

    //
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 14));

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    graphPropsRed = (GraphProperties) props.getPropertiesByName("graphRed");
    graph = (Graph) primitives.get("graph");
    textProps = (TextProperties) props.getPropertiesByName("textProp");
    TextProperties textProps2 = (TextProperties) props
        .getPropertiesByName("infoTextProp");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProp");
    probability = (Integer) primitives.get("timeout_probability");
    int start = (Integer) primitives.get("start");

    src = lang.newSourceCode(new Coordinates(500, 50), "sourceCode", null,
        sourceCodeProps);
    src.addCodeLine("function hold_election()", null, 0, null); // 0
    src.addCodeLine("for each (Process Q) {", null, 1, null); // 0
    src.addCodeLine("if (Id(Q) > Id(P))", null, 2, null); // 0
    src.addCodeLine("send(Q, ELECTION);", null, 3, null); // 0
    src.addCodeLine("end if", null, 2, null); // 0
    src.addCodeLine("}", null, 1, null); // 0
    src.addCodeLine("if exists(Q): (receive(Q, ANSWER)) then", null, 1, null); // 0
    src.addCodeLine("do_something_else();", null, 2, null); // 0
    src.addCodeLine("else", null, 1, null); // 0
    src.addCodeLine("for each (Process Q) {", null, 2, null); // 0
    src.addCodeLine("if (Id(Q) < Id(P))", null, 3, null); // 0
    src.addCodeLine("send(Q, COORDINATOR);", null, 4, null); // 0
    src.addCodeLine("endif", null, 3, null); // 0
    src.addCodeLine("}", null, 2, null); // 0
    src.addCodeLine("endif", null, 1, null); // 0
    src.addCodeLine("", null, 0, null); // 0
    src.addCodeLine("function continue_election()", null, 0, null); // 0
    src.addCodeLine("send(pred, ANSWER);", null, 1, null); // 0
    src.addCodeLine("hold_election();", null, 1, null); // 0

    electingProcessLabel = lang.newText(new Coordinates(150, 450),
        "Wählender Knoten", "Wählender Knoten", null, textProps);
    mLabel = lang.newText(new Coordinates(150, 475), "gesendete Nachricht",
        "msg", null, textProps);
    electingProcess = lang.newText(new Coordinates(300, 450), "",
        "electing_process_value", null, textProps);
    message = lang.newText(new Coordinates(300, 475), "", "message_value",
        null, textProps);
    coordinatorLabel = lang.newText(new Coordinates(150, 500), "Koordinator",
        "Koordinator", null, textProps);
    coordinatorText = lang.newText(new Coordinates(300, 500), "",
        "coordinator_value", null, textProps);
    infotext = lang.newText(new Coordinates(150, 400), "", "info", null,
        textProps2);

    lang.hideAllPrimitives();
    header.show();
    this.showInfoPage();
    lang.nextStep("Start");
    lang.hideAllPrimitives();

    // lang.hideAllPrimitives();

    header.show();
    src.show();
    electingProcessLabel.show();
    electingProcess.show();
    coordinatorLabel.show();
    coordinatorText.show();
    message.show();
    infotext.show();
    mLabel.show();

    int size = graph.getSize();
    Node[] nodes = new Node[size];
    String[] nodeLabels = new String[size];
    for (int i = 0; i < size; i++) {
      nodes[i] = graph.getNode(i);
      nodeLabels[i] = String.valueOf(i);
    }
    graph = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), nodes,
        nodeLabels, graph.getDisplayOptions(), graphPropsRed);
    initBullyAlgo(graph.getSize());
    graph.show();
    coordinatorText.setText(String.valueOf(coordinator.getId()), null, null);
    graph.highlightNode(coordinator.getId(), null, null);
    lang.nextStep("Koordinator Ausfall");

    infotext.setText("Der bisherige Koordinator (Knoten " + coordinator.getId()
        + ") fällt aus", null, null);

    lang.nextStep();

    graph.unhighlightNode(coordinator.getId(), null, null);
    graph.hideNode(coordinator.getId(), null, null);
    killCoordinator();
    coordinatorText.setText("", null, null);

    lang.nextStep();

    // Process theSmartOne = someOneRealizeTheCoordinatorisAway(start);
    Process theSmartOne = pool.get(start);
    infotext.setText("Knoten " + theSmartOne.getId()
        + " stellt Ausfall des Koordinators fest", null, null);
    graph.highlightNode(theSmartOne.getId(), null, null);
    electingProcess.setText(String.valueOf(theSmartOne.getId()), null, null);
    lang.nextStep("Start der Coordinator Election");
    infotext.setText("Wahl des neuen Koordinators beginnt", null, null);
    lang.nextStep();
    newElection(theSmartOne);

    lang.nextStep("Schlussbemerkung");
    graph.hide();
    lang.hideAllPrimitives();

    header.show();
    this.showOutroPage();

    return lang.toString();

  }

  public String getName() {
    return "Der Bully Algorithmus";
  }

  public String getAlgorithmName() {
    return "Bully Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Nico Wombacher, Moritz Moxter";
  }

  public String getDescription() {
    return "Der Bully Algorithmus ist ein sog. Wahlalgorithmus zur Bestimmung eines Koordinators in einem Netzwerk."
        + "<br/>"
        + "Alle anderen Knoten im Netzwerk erkennen den Koordinator quasi als Server an. "
        + "<br/>"
        + "<br/>"
        + "Zur Durchführung des Bully Algorimus müssen einige Annahmen getroffen werden:"
        + "<br/>"
        + "<br/>"
        + "1. w&auml;hrend der Wahl d&uuml;rfen Prozesse abst&uuml;rzen"
        + "<br/>"
        + "2. das System ist synchron"
        + "<br/>"
        + "3. fehlerhafte Prozesse werden anhand von timeouts erkannt"
        + "<br/>"
        + "4. jeder Prozess kennt alle anderen Prozesse und kann auch mit jedem kommunizieren"
        + "<br/>"
        + "<br/>"
        + "&Uuml;ber den Parameter timeout_probability kann man die Ausfallwahrscheinlichkeit von Knoten anpassen. Default Wert ist 80, d.h. ein Knoten wird mit einer Wahrscheinlichkeit von 80% antworten."
        + "<br/>"
        + "Der Parameter start gibt an, welcher Knoten den Ausfall des Koordinators als erstes feststellt und die neue Wahl startet";
  }

  public String getCodeExample() {
    return "function hold_election()" + "\n" + "   for each (Process Q) {"
        + "\n" + "     if (Id(Q) > Id(P))" + "\n" + "       send(Q, ELECTION);"
        + "\n" + "     end if" + "\n" + "   }" + "\n" + "\n"
        + "  if exists(Q): (receive(Q, ANSWER))" + "\n" + "  then" + "\n"
        + "    do_something_else();" + "\n" + "  else" + "\n"
        + "    for each (Process Q) {" + "\n" + "      if (Id(Q) < Id(P))"
        + "\n" + "        send(Q, COORDINATOR);" + "\n" + "      endif" + "\n"
        + "    }" + "\n" + "  endif" + "\n" + "\n" + "\n"
        + "function continue_election()" + "\n" + "  send(pred, ANSWER);"
        + "\n" + "  hold_election();" + "\n";
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

  private void newElection(Process theSmartOne) {

    // System.out.println("Wahl wurde von " + theSmartOne.getId() +
    // " gestartet!");

    this.newElec = new ArrayList<Process>();

    infotext
        .setText(
            "Knoten "
                + theSmartOne.getId()
                + " startet die Wahl durch senden einer Election Nachricht an alle Knoten mit einer höheren ID als er selbst.",
            null, null);

    for (Process p : this.pool) {
      // lang.nextStep();
      src.highlight(2);
      if (p.getId() > theSmartOne.getId()) {
        graph.highlightNode(theSmartOne.getId(), null, null);
        graph.highlightEdge(theSmartOne.getId(), p.getId(), null, null);
        src.unhighlight(2);
        this.unhighlightsrc();
        src.highlight(3);
        message.setText(theSmartOne.getId() + " -Election-> " + p.getId(),
            null, null);
        lang.nextStep();
        message.setText("", null, null);
      }
    }

    for (Process p : this.pool) {
      // lang.nextStep();
      if (p.getId() > theSmartOne.getId()) {
        // Simulation ob alle Prozesse antworten ...
        if (p.getAlive() == Status.UNSET) {
          Random gen = new Random();
          int ran = gen.nextInt(100);
          if (ran < probability) {
            p.setAlive(Status.ALIVE);
          } else {
            p.setAlive(Status.DEAD);
          }
        }

        if (p.getAlive() == Status.DEAD) {
          message.setText("Timeout von " + p.getId(), null, null);

          infotext
              .setText(
                  "Knoten "
                      + p.getId()
                      + " ist nicht erreichbar. Der Knoten wird deshalb als nicht existent angenommen",
                  null, null);
          graph.unhighlightEdge(theSmartOne.getId(), p.getId(), null, null);
          graph.unhighlightNode(theSmartOne.getId(), null, null);
          graph.hideNode(p.getId(), null, null);
          lang.nextStep();
          message.setText("", null, null);
        }

        if (p.getAlive() == Status.ALIVE) {
          this.unhighlightsrc();
          src.unhighlight(3);
          src.highlight(17);
          infotext.setText("Knoten " + p.getId() + " ist erreichbar.", null,
              null);
          message.setText(p.getId() + " -Answer-> " + theSmartOne.getId(),
              null, null);
          lang.nextStep();
          message.setText("", null, null);
          this.unhighlightsrc();
          src.highlight(18);
          graph.unhighlightEdge(theSmartOne.getId(), p.getId(), null, null);
          graph.unhighlightNode(theSmartOne.getId(), null, null);
          this.newElec.add(p);
          lang.nextStep();
        }

      }
    }
    // lang.nextStep();
    this.unhighlightsrc();
    src.unhighlight(18);
    if (this.newElec.size() == 0) {
      infotext.setText("keine Knoten mit höherer ID als " + theSmartOne.getId()
          + " erreichbar", null, null);
      lang.nextStep();
      for (Process p : pool) {
        lang.nextStep();
        if ((p.getAlive() == Status.ALIVE && p != theSmartOne)
            || (p.getAlive() == Status.UNSET && p != theSmartOne)) {
          this.unhighlightsrc();
          src.highlight(10);
          lang.nextStep();
          this.unhighlightsrc();
          src.unhighlight(10);
          src.highlight(11);
          graph.highlightEdge(theSmartOne.getId(), p.getId(), null, null);
          infotext
              .setText(
                  "Knoten "
                      + theSmartOne.getId()
                      + " ist der neue Koordinator und informiert die anderen Knoten darüber",
                  null, null);
          message.setText(theSmartOne.getId() + " -Coordinator-> " + p.getId(),
              null, null);

        }
        graph.highlightNode(theSmartOne.getId(), null, null);
        coordinatorText
            .setText(String.valueOf(theSmartOne.getId()), null, null);
      }
    } else {
      lang.nextStep();
      electingProcess.setText("" + newElec.get(0).getId(), null, null);
      this.newElection(this.newElec.get(0));
    }
  }

  // private Process someOneRealizeTheCoordinatorisAway(boolean start) {
  //
  // Process smartie = this.pool.get(2);
  //
  // if(start){
  // return null;
  // // Random gen = new Random();
  // // smartie = this.pool.get(gen.nextInt(this.pool.size()-1)+1);
  // }
  //
  // smartie.setAlive(Status.ALIVE);
  // return smartie;
  //
  // }

  private void killCoordinator() {
    pool.remove(this.coordinator);

  }

  public void initBullyAlgo(int numberOfProcesses) {

    pool = new ArrayList<Process>();

    for (int i = 0; i < numberOfProcesses; i++) {
      pool.add(new Process(i));
    }

    this.coordinator = pool.get(numberOfProcesses - 1);

  }

  // private void switchGraph(GraphProperties gProps){
  //
  //
  // int[][] adjMatrix = new int[4][4];
  // adjMatrix[0][0] = 0;
  // adjMatrix[0][1] = 1;
  // adjMatrix[0][2] = 1;
  // adjMatrix[0][3] = 1;
  // adjMatrix[1][0] = 1;
  // adjMatrix[1][1] = 0;
  // adjMatrix[1][2] = 1;
  // adjMatrix[1][3] = 1;
  // adjMatrix[2][0] = 1;
  // adjMatrix[2][1] = 1;
  // adjMatrix[2][2] = 0;
  // adjMatrix[2][3] = 1;
  // adjMatrix[3][0] = 1;
  // adjMatrix[3][1] = 1;
  // adjMatrix[3][2] = 1;
  // adjMatrix[3][3] = 0;
  //
  //
  // Node[] graphNodes = new Node[4];
  // graphNodes[0] = new Coordinates(300,300);
  // graphNodes[1] = new Coordinates(400,200);
  // graphNodes[2] = new Coordinates(300,100);
  // graphNodes[3] = new Coordinates(200,200);
  //
  //
  // String[] labels2 = {"0", "1", "2", "3"};
  //
  // graph = lang.newGraph("graph2", adjMatrix, graphNodes, labels2, null,
  // gProps);
  // }
  // /**
  // * create a default graph for the TSP instance
  // */
  // private void makeDefaultGraph(GraphProperties gProps) {
  //
  // int[][] adjMatrix = new int[5][5];
  // for (int i = 0; i < 5; i++) {
  // for (int j = 0; j < 5; j++) {
  // if (i == j) {
  // adjMatrix[i][j] = 0;
  // } else {
  // adjMatrix[i][j] = 1;
  // }
  // }
  // }
  //
  // Node[] graphNodes = new Node[5];
  // graphNodes[0] = new Coordinates(300, 300);
  // graphNodes[1] = new Coordinates(400, 200);
  // graphNodes[2] = new Coordinates(300, 100);
  // graphNodes[3] = new Coordinates(200, 200);
  // graphNodes[4] = new Coordinates(180, 120);
  //
  // String[] labels = { "0", "1", "2", "3", "4" };
  //
  // graph = lang.newGraph("graph", adjMatrix, graphNodes, labels, null,
  // gProps);
  //
  // }

  private void unhighlightsrc() {
    for (int i = 0; i < 19; i++) {
      src.unhighlight(i);
    }
  }

  private void showInfoPage() {
    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font(Font.SANS_SERIF, Font.PLAIN, 14));
    lang.newText(
        new Coordinates(200, 230),
        "Der Bully Algorithmus ist ein sog. Wahlalgorithmus zur Bestimmung eines Koordinators in einem Netzwerk.",
        "description1", null, textProps);
    lang.newText(
        new Coordinates(200, 260),
        "Fällt der bisherige Koordinator aus startet ein anderer Knoten eine neue Coordinator Election.",
        "description2", null, textProps);
    lang.newText(
        new Coordinates(200, 290),
        "Der Bully Algorithmus garantiert, dass am Ende der Wahl ein neuer Koordinator fest steht.",
        "description2", null, textProps);
    lang.newText(
        new Coordinates(200, 320),
        "Alle anderen Knoten im Netzwerk erkennen den Koordinator quasi als Server an.",
        "description2", null, textProps);
    lang.newText(
        new Coordinates(200, 350),
        "Zur korrekten Durchführung des Bully Algorimus müssen einige Annahmen getroffen werden:",
        "description3", null, textProps);
    lang.newText(new Coordinates(200, 380), "", "description8", null, textProps);
    lang.newText(new Coordinates(200, 410),
        "1. während der Wahl dürfen Prozesse abstürzen", "description4", null,
        textProps);
    lang.newText(new Coordinates(200, 440), "2. das System ist synchron",
        "description5", null, textProps);
    lang.newText(new Coordinates(200, 470),
        "3. fehlerhafte Prozesse werden anhand von timeouts erkannt",
        "description6", null, textProps);
    lang.newText(
        new Coordinates(200, 500),
        "4. jeder Prozess kennt alle anderen Prozesse und kann auch mit jedem kommunizieren",
        "description7", null, textProps);
    lang.newText(new Coordinates(200, 530), "", "description11", null,
        textProps);
    lang.newText(
        new Coordinates(200, 560),
        "Der Bully Algorithmus wählt stets den Knoten mit der höchsten Priorität als neuen Koordinator.",
        "description12", null, textProps);
    lang.newText(
        new Coordinates(200, 590),
        "In diesem Beispiel wird ein Ausfall des bisherigen Koordinators und die anschließende Wahl",
        "description13", null, textProps);
    lang.newText(new Coordinates(200, 620),
        "eines neuen Koordinators mit dem Bully Algorithmus gezeigt",
        "description11", null, textProps);
    lang.newText(new Coordinates(200, 650), "", "description11", null,
        textProps);
    lang.newText(
        new Coordinates(200, 680),
        "Der Generator sorgt dafür, dass ein Knoten mit der Wahrscheinlichkeit von 80% (default) erreichbar ist.",
        "description11", null, textProps);
    lang.newText(new Coordinates(200, 710),
        "Entsprechend lonht es sich den Generator ein zweites mal zu nutzen.",
        "description11", null, textProps);
  }

  private void showOutroPage() {
    this.lang.hideAllPrimitives();
    this.header.show();
    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font(Font.SANS_SERIF, Font.PLAIN, 14));
    lang.newText(new Coordinates(200, 430),
        "Wie man sieht wurde ein neuer Koordinator korrekt gewählt.",
        "description1", null, textProps);
    lang.newText(new Coordinates(200, 460),
        "Dies ist möglich, da die nötigen Voraussetzungn erfüllt sind.",
        "description2", null, textProps);
    lang.newText(
        new Coordinates(200, 490),
        "Diese Voraussetzung stellen auch einen Nachteil des Bully Algorithmus dar.",
        "description3", null, textProps);
    lang.newText(
        new Coordinates(200, 520),
        "Sobald eine der Voraussetzungen nicht mehr erfüllt ist wird dieser nicht mehr korrekt funktionieren.",
        "description4", null, textProps);
    lang.newText(
        new Coordinates(200, 550),
        "Klare Vorteile sind die simple Struktur und einfache Durchführbarkeit:",
        "description3", null, textProps);
    lang.newText(
        new Coordinates(200, 580),
        "jeder andere Knoten im Netzwerk ist in der Lage eine neue Coordinator Election zu starten.",
        "description3", null, textProps);
  }

}