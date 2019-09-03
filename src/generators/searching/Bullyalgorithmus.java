package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
//import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import generators.framework.properties.AnimationPropertiesContainer;

public class Bullyalgorithmus implements Generator {
  private Language             lang;
  private int                  Identifizierungsknoten;
  private SourceCodeProperties Highlight;
  private CircleProperties     Crashprozess;
  private int                  Prozessanzahl;

  private int                  foundCoordinator      = -1;
  private int                  messages              = 0;
  private ArrayList<Integer>   processesWhichElected = new ArrayList<Integer>();

  public void init() {
    lang = new AnimalScript("Bullyalgorithmus [DE]",
        "Bennet Jeutter, Max Mindt", 800, 600);
    lang.setStepMode(true);
  }

  private double degToRad(double deg) {
    return deg / 180 * Math.PI;
  }

  /**
   * Versteckt alle Kanten eines Graphen
   * 
   * @param g
   */
  private void hideAllEdges(Graph g) {
    int[][] adj = g.getAdjacencyMatrix();

    for (int i = 0; i < adj.length; i++) {
      for (int j = 0; j < adj.length; j++) {
        g.hideEdge(i, j, null, null);
      }
    }
  }

  /**
   * Zählt die tabs in einem String
   */
  private int countTabs(String s) {
    if (s.indexOf("\t") == 0) {
      return 1 + countTabs(s.substring(1));
    }

    return 0;

  }

  /**
   * Bully Algo
   * 
   * @param codeSupport
   *          the underlying code instance
   */
  private void bullyAlgo(Graph g, SourceCode codeSupport,
      Text currentEventText, Text currentEventText2, Text currentEventText3,
      Integer[] processes, int current_coordinator_going_offline,
      int detecting_process) throws LineNotExistsException {
    lang.nextStep();

    // Als erstes den crashenden Knoten als Coordinator beschreiben
    currentEventText.setText("Prozess " + current_coordinator_going_offline
        + " ist der aktuelle Koordinator", null, null);
    lang.nextStep();

    // dann den crashenden Knoten highlighten und den Crashprozess
    // beschreiben
    g.highlightNode(current_coordinator_going_offline - 1, null, null);
    currentEventText.setText("Prozess " + current_coordinator_going_offline
        + " crasht", null, null);
    currentEventText2.setText("Prozess " + detecting_process + " bemerkt dies",
        null, null);
    codeSupport.highlight(1);
    lang.nextStep();
    codeSupport.unhighlight(1);

    // Detecting Prozess startet eine Wahl
    recElection(g, currentEventText, currentEventText2, currentEventText3,
        codeSupport, detecting_process, current_coordinator_going_offline,
        processes);
  }

  private void recElection(Graph g, Text currentEventText,
      Text currentEventText2, Text currentEventText3, SourceCode codeSupport,
      int from, int offline, Integer[] processes) {
    if (from != this.foundCoordinator) {
      // aktuellen Eventtext setzen
      currentEventText3.setText("", null, null);
      currentEventText2.setText("", null, null);
      currentEventText.setText("", null, null);

      // Entsprechende Zeilen markieren
      codeSupport.unhighlight(11);
      codeSupport.unhighlight(12);
      codeSupport.unhighlight(8);
      codeSupport.unhighlight(9);
      codeSupport.unhighlight(14);
      codeSupport.unhighlight(15);

      // Höhere Prozesse filtern
      Integer[] higherProcesses = getProcessesWithHigherId(from, processes);

      ArrayList<Integer> respondingProcesses = new ArrayList<Integer>();
      String eventMessage = "Prozesse ";
      processesWhichElected.add(from);
      // Falls höhere Prozesse existieren
      if (higherProcesses.length > 0) {
        codeSupport.highlight(2);
        codeSupport.highlight(3);

        currentEventText.setText("Prozess " + from
            + " startet eine Wahl und sendet Election an höhere Prozesse",
            null, null);

        // Diesen eine Electionnachricht zukommen lassen
        sendMessageToNodes(g, from, higherProcesses, "ELECTION");
        currentEventText3.setText("Es wurden bisher " + messages
            + " Nachrichten versendet", null, null);
        lang.nextStep();

        // Entsprechende Zeilen markieren
        codeSupport.unhighlight(2);
        codeSupport.unhighlight(3);
        codeSupport.highlight(4);
        codeSupport.highlight(14);

        currentEventText3.setText("Es wurden bisher " + messages
            + " Nachrichten versendet", null, null);
        lang.nextStep();

        codeSupport.unhighlight(14);

        // Die Knoten antworten alle
        hideAllEdges(g);
        for (int h : higherProcesses) {
          if (h != offline) {
            sendMessageToNode(g, h, from, "OK", false);
            codeSupport.highlight(8);
            codeSupport.highlight(9);
            codeSupport.highlight(15);
            respondingProcesses.add(h);
            eventMessage += h + ",";
          }
        }
        eventMessage.substring(0, eventMessage.length() - 2);
        eventMessage += " bestätigen mit OK";
      }

      codeSupport.unhighlight(4);
      // Falls es Knoten gibt, die geantwortet haben haben
      if (!respondingProcesses.isEmpty()) {
        // codeSupport.highlight(11);
        // codeSupport.highlight(12);
        // codeSupport.highlight(8);
        // codeSupport.highlight(9);
        currentEventText.setText(eventMessage, null, null);
        currentEventText2.setText("Prozess " + from
            + " hat seinen Job getan und wartet auf den neuen Koordinator",
            null, null);
        currentEventText3.setText("Es wurden bisher " + messages
            + " Nachrichten versendet", null, null);
      } else { // ansonsten
        lang.nextStep();

        MultipleChoiceQuestionModel msq = new MultipleChoiceQuestionModel(
            "multipleChoiceQuestion");
        msq.setPrompt("Was passiert jetzt?");
        msq.addAnswer(
            "Kein Koordinator wurde ermittelt",
            0,
            "Nein, er selber ist der neue Koordinator und teilt dies allen anderen Prozessen mit");
        msq.addAnswer(
            "Er selber ist der neue Koordinator und sendet dies allen anderen Prozessen",
            1, "Richtig");
        msq.addAnswer(
            "Nichts passiert, der Algorithmus terminiert hier",
            0,
            "Nein, er selber ist der neue Koordinator und teilt dies allen anderen Prozessen mit");
        msq.setGroupID("QG");
        lang.addMCQuestion(msq);

        currentEventText3.setText("Es wurden bisher " + messages
            + " Nachrichten versendet", null, null);
        lang.nextStep();

        // Sich selbst als Coordinator markieren
        foundCoordinator = from;

        // Allen anderen Knoten coordinator Nachricht schicken
        sendCoordinator(g, currentEventText, currentEventText2,
            currentEventText3, codeSupport, from, offline, processes);

        return;
      }

      // Rekursiv absteigen
      for (int h : higherProcesses) {
        if (h != offline && !processesWhichElected.contains(h)) {
          lang.nextStep();
          recElection(g, currentEventText, currentEventText2,
              currentEventText3, codeSupport, h, offline, processes);
        }
      }
    }
  }

  private void sendMessageToNode(Graph g, int from, int to, String message,
      boolean hide) {
    if (hide) {
      hideAllEdges(g);
    }

    g.setEdgeWeight(from - 1, to - 1, message, null, null);
    g.showEdge(from - 1, to - 1, null, null);

    // Nachrichtenzähler erhöhen
    messages++;
  }

  private void sendMessageToNodes(Graph g, int from, Integer[] to,
      String message) {
    hideAllEdges(g);
    for (int recv : to) {
      sendMessageToNode(g, from, recv, message, false);
    }
  }

  private Integer[] getProcessesWithHigherId(int process, Integer[] processes) {
    List<Integer> list = new LinkedList<Integer>();

    for (int p : processes) {
      if (p != process && p > process) {
        list.add(p);
      }
    }

    return list.toArray(new Integer[0]);
  }

  private void sendCoordinator(Graph g, Text currentEventText,
      Text currentEventText2, Text currentEventText3, SourceCode codeSupport,
      int from, int offline, Integer[] processes) {
    // Alle Kanten hiden
    hideAllEdges(g);

    // Eventtext entsprechend setzen
    currentEventText2.setText("", null, null);
    currentEventText.setText("Prozess " + from
        + " sendet Coordinator zu allen anderen Prozessen", null, null);

    // Entsprechende Zeilen highlighten
    codeSupport.highlight(5);
    codeSupport.highlight(6);
    codeSupport.highlight(11);
    codeSupport.highlight(12);

    // An alle Prozesse, außer dem Offline Prozess, eine Coordinator
    // Nachricht schicken
    for (int p : processes) {
      if (p != from && p != offline) {
        sendMessageToNode(g, from, p, "COORDINATOR", false);
      }
    }

    currentEventText3.setText("Es wurden bisher " + messages
        + " Nachrichten versendet", null, null);

    // Nächster Schritt
    lang.nextStep();
  }

  public void bully(Integer[] processes, int current_coordinator_going_offline,
      int detecting_process) {
    // Überschrift
    lang.newText(new Coordinates(20, 30), "Bullyalgorithm Animation",
        "headerText", null);
    lang.newRect(new Coordinates(10, 20), new Coordinates(180, 50),
        "headerBorder", null);

    // Description anzeigen
    SourceCode description = lang.newSourceCode(new Coordinates(20, 65),
        "description", null, Highlight);

    String[] desc = DESCRIPTION.split("\n");
    for (int i = 0; i < desc.length; i++) {
      description.addCodeLine(desc[i], null, 0, null);
    }

    // Einen Frame warten
    lang.nextStep();

    // Grapheneinstellungen (später durch Generator einstellbar)
    int GRAPH_POS_X = 300;
    int GRAPH_POS_Y = 300;
    int DISTANCE_X = 100;
    int DISTANCE_Y = 100;

    // Graphendaten erstellen
    int[][] adjacenceMatrix = new int[processes.length][processes.length];
    Node[] nodes = new Node[processes.length];
    String[] labels = new String[processes.length];
    for (int i = 0; i < processes.length; i++) {
      labels[i] = "" + (i + 1);
      // Knoten im Kreis anordnen
      nodes[i] = new Coordinates(
          (int) (GRAPH_POS_X - Math.cos(degToRad(processes.length
              * processes.length + (360 / processes.length) * i))
              * DISTANCE_X),
          (int) (GRAPH_POS_Y - (Math.sin(degToRad(processes.length
              * processes.length + (360 / processes.length) * i)) * DISTANCE_Y)));

      // Adjazensmatrix erstmal so setzten, dass jeder Knoteen mit jedem
      // Knoten verbunden ist
      for (int j = 0; j < processes.length; j++) {
        if (i != j) {
          adjacenceMatrix[i][j] = 1;
        }
      }
    }

    // Graphendarstellung einstellen
    GraphProperties props = new GraphProperties();
    props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // props.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        (Color) Crashprozess.get("fillColor"));
    props.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
    props.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.TRUE);
    props.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.TRUE);

    // Graphen erstellen
    Graph g = lang.newGraph("processes", adjacenceMatrix, nodes, labels, null,
        props);

    // Alle Kanten verstecken
    hideAllEdges(g);

    // Spourcecode anlegen
    /*
     * SourceCodeProperties scProps = new SourceCodeProperties();
     * scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
     * scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
     * "Monospaced", Font.PLAIN, 12));
     * 
     * scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
     * scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     */
    // Sourcecode erstellen unter dem Graphen
    SourceCode sc = lang.newSourceCode(new Coordinates(
        (int) (GRAPH_POS_X + DISTANCE_X * 2), GRAPH_POS_Y - DISTANCE_Y),
        "sourceCode", null, Highlight);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    String[] code = SOURCE_CODE.split("\n");
    for (int i = 0; i < code.length; i++) {
      sc.addCodeLine(code[i], null, countTabs(code[i]), null);
    }

    // sc.show();

    lang.nextStep();

    // Text für "aktuelles Ereignisse
    Text currentEvent1 = lang.newText(new Coordinates(20,
        (int) (GRAPH_POS_Y + DISTANCE_Y * 1.5f)), "", "currentEvent1", null);
    Text currentEvent2 = lang.newText(new Coordinates(20,
        (int) (GRAPH_POS_Y + DISTANCE_Y * 1.5f) + 20), "", "currentEvent1",
        null);
    Text currentEvent3 = lang.newText(new Coordinates(20,
        (int) (GRAPH_POS_Y + DISTANCE_Y * 1.5f) + 40), "", "currentEvent1",
        null);

    // start Bully
    bullyAlgo(g, sc, currentEvent1, currentEvent2, currentEvent3, processes,
        current_coordinator_going_offline, detecting_process);

    // Hide Code
    lang.nextStep();

    // Abschlussfolie
    description.hide();
    g.hide();
    currentEvent1.hide();
    currentEvent2.hide();
    currentEvent3.hide();
    sc.hide();

    // Sourcecode erstellen unter dem Graphen
    SourceCode sum = lang.newSourceCode(new Coordinates(20, 65), "sum", null,
        Highlight);
    sum.addCodeLine("Der neue Koordinator wurde nun gefunden", null, 0, null);
    sum.addCodeLine("Es ist Prozess " + foundCoordinator, null, 0, null);
    sum.addCodeLine("Es wurden " + messages
        + " Nachrichten geschickt um den Koordinator zu ermitteln", null, 0,
        null);
  }

  /**
   * BullyAlgo wird aus dieser Methode gestartet
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    QuestionGroupModel groupInfo = new QuestionGroupModel("QG", 3);
    lang.addQuestionGroup(groupInfo);

    Identifizierungsknoten = (Integer) primitives.get("Identifizierungsknoten");
    Highlight = (SourceCodeProperties) props.getPropertiesByName("Highlight");
    Crashprozess = (CircleProperties) props.getPropertiesByName("Crashprozess");
    Prozessanzahl = (Integer) primitives.get("Prozessanzahl");

    Integer[] processes = new Integer[Prozessanzahl];

    for (int i = 0; i < Prozessanzahl; i++) {
      processes[i] = i + 1;
    }

    bully(processes, Prozessanzahl, Identifizierungsknoten);

    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Bullyalgorithmus [DE]";
  }

  public String getAlgorithmName() {
    return "Bullyalgorithm";
  }

  public String getAnimationAuthor() {
    return "Bennet Jeutter, Max Mindt";
  }

  public String getDescription() {
    return "In verteilten Systemen ist es oft n&ouml;tig, dass ein Prozess die Koordination &uuml;bernimmt. "
        + "\n"
        + "Der Bullyalgorithmus ist ein rekursiv, verteilter Algorithmus und dient zur Ermittlung eines solchen Koordinators. "
        + "\n"
        + "F&auml;llt ein Koordinator im laufenden System aus, z.B. durch einen Timeout, "
        + "\n"
        + "erkennt der Algorithmus dies und ermittelt einen neuen Koordinator.";
  }

  public String getCodeExample() {
    return "Der Algorithmus sieht wie folgt aus f&uuml;r einen Prozess X:"
        + "\n"
        + "Wenn Prozess X den Ausfall seines Koordinators bemerkt"
        + "\n"
        + "     Dann starte eine Wahl:"
        + "\n"
        + "          Sende eine ELECTION Nachricht an alle Prozesse mit einer h&ouml;heren Nummer"
        + "\n"
        + "     Warte auf eine Antwort der Prozesse"
        + "\n"
        + "     Falls niemand antwortet, ist der Prozess X selber der neue Koordinator"
        + "\n"
        + "          Sende eine COORDINATOR Nachricht an alle anderen Prozesse"
        + "\n"
        + "\n"
        + "     Wenn Prozess X eine Antwort eines Prozesses erh&auml;lt"
        + "\n"
        + "          Warte auf COORDINATOR Nachricht"
        + "\n"
        + "\n"
        + "     Wenn Prozess X eine COORDINATOR Nachricht von einem Prozess Y bekommt"
        + "\n"
        + "          Neuer Koordinator ist Prozess Y"
        + "\n"
        + "\n"
        + "     Wenn Prozess X eine ELECTION Nachricht von einem Prozess Y beommt und X > Y"
        + "\n" + "          Sende OK an Y";
  }

  private static final String DESCRIPTION = "In verteilten Systemen ist es oft nötig, dass ein Prozess die Koordination übernimmt.\n"
                                              + "Der Bullyalgorithmus dient dem Ermitteln eines solchen Koordinators.\n"
                                              + "Die folgende Animation soll die Funktionsweise dieses Algorithmus veranschaulichen.";
  private static final String SOURCE_CODE = "Der Algorithmus sieht wie folgt aus für einen Prozess X: "
                                              + "\nWenn Prozess X den Ausfall seines Koordinators bemerkt"
                                              + "\n\tDann starte eine Wahl:"
                                              + "\n\t\t Sende eine ELECTION Nachricht an alle Prozesse mit einer höheren Nummer"
                                              + "\n\tWarte auf Antwort der Prozesse"
                                              + "\n\tFalls niemand antwortet, ist der Prozess X selber der neue Koordinator"
                                              + "\n\t\tSende eine COORDINATOR Nachricht an alle anderen Prozesse"
                                              + "\n"
                                              + "\n\tWenn Prozess X eine Antwort eines Prozesses erhält"
                                              + "\n\t\tWarte auf COORDINATOR Nachricht"
                                              + "\n"
                                              + "\n\tWenn Prozess X eine COORDINATOR Nachricht von einem Prozess Y bekommt"
                                              + "\n\t\tNeuer Koordinator ist Prozess Y"
                                              + "\n"
                                              + "\n\tWenn Prozess X eine ELECTION Nachricht von einem Prozess Y bekommt und X > Y"
                                              + "\n\t\tSende OK an Y";

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}