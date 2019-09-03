package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.searching.helpers.Message;
import generators.searching.helpers.Observer;
import generators.searching.helpers.Process;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import algoanim.animalscript.AnimalScript;
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

public class ElectionOnRings implements Generator, Observer {
  private Language              lang;
  private int                   Identifizierungsknoten;
  private SourceCodeProperties  Highlight;
  private CircleProperties      Crashprozess;
  private int                   Prozessanzahl;
  private int[]                 Chrashprozesse;

  boolean                       run;
  private int                   messages         = 0;
  private int                   foundCoordinator = -1;
  private Graph                 g;
  private SourceCode            codeSupport;
  private Text                  currentEventText1, currentEventText2,
      currentEventText3;
  private Map<Integer, Process> processes;

  public void init() {
    lang = new AnimalScript("Election on Rings [DE]",
        "Bennet Jeutter, Max Mindt", 800, 600);
    lang.setStepMode(true);
  }

  private String crashedProcesses() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < Chrashprozesse.length; i++) {
      sb.append(Chrashprozesse[i]);
      if (i != Chrashprozesse.length - 1)
        sb.append(",");
    }
    return sb.toString();
  }

  private void startElectionOnRings() {
    lang.nextStep();

    // Als erstes den crashenden Knoten als Coordinator beschreiben
    currentEventText1.setText("Prozess " + Prozessanzahl
        + " ist der aktuelle Koordinator", null, null);
    lang.nextStep();

    // dann den crashenden Knoten highlighten und den Crashprozess
    // beschreiben
    for (int i : Chrashprozesse)
      g.highlightNode(i - 1, null, null);

    currentEventText1.setText(
        "Die Prozesse " + crashedProcesses() + " crashen", null, null);
    currentEventText2.setText("Prozess " + Identifizierungsknoten
        + " bemerkt dies", null, null);

    codeSupport.highlight(0);

    lang.nextStep();

    codeSupport.unhighlight(0);
    codeSupport.highlight(1);

    // Prozesse und deren Beziehungen untereinander festlegen
    processes = new HashMap<Integer, Process>(6);

    for (int i = 1; i <= Prozessanzahl; i++)
      processes.put(i, new Process(Prozessanzahl, null, i, this));

    for (int i = 1; i < Prozessanzahl; i++)
      processes.get(i).setSuccessor(processes.get(i + 1));

    processes.get(Prozessanzahl).setSuccessor(processes.get(1));

    // Koordinator crashed
    for (int i : Chrashprozesse)
      if (processes.containsKey(i))
        processes.get(i).crash();

    // Initiator starten
    processes.get(Identifizierungsknoten).initiate();

    // Gefundenen Koordinator setzen
    foundCoordinator = processes.get(Identifizierungsknoten).getCoordinator();

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    QuestionGroupModel groupInfo = new QuestionGroupModel("QG", 3);
    lang.addQuestionGroup(groupInfo);

    Identifizierungsknoten = (Integer) primitives.get("Identifizierungsknoten");
    Highlight = (SourceCodeProperties) props.getPropertiesByName("Highlight");
    Crashprozess = (CircleProperties) props.getPropertiesByName("Crashprozess");
    Prozessanzahl = (Integer) primitives.get("Prozessanzahl");
    Chrashprozesse = (int[]) primitives.get("Chrashprozesse");
    run = true;

    // Überschrift
    lang.newText(new Coordinates(20, 30), "Election on Rings Animation",
        "headerText", null);
    lang.newRect(new Coordinates(10, 20), new Coordinates(180, 50),
        "headerBorder", null);

    // Description anzeigen
    SourceCode description = lang.newSourceCode(new Coordinates(20, 65),
        "description", null, Highlight);

    String[] desc = getCleanDescription().split("\n");
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

    // Graph erstellen
    int[][] adjacenceMatrix = new int[Prozessanzahl][Prozessanzahl];
    Node[] nodes = new Node[Prozessanzahl];
    String[] labels = new String[Prozessanzahl];
    for (int i = 0; i < Prozessanzahl; i++) {
      labels[i] = "" + (i + 1);
      // Knoten im Kreis anordnen
      nodes[i] = new Coordinates(
          (int) (GRAPH_POS_X - Math.cos(degToRad(Prozessanzahl * Prozessanzahl
              + (360 / Prozessanzahl) * i))
              * DISTANCE_X),
          (int) (GRAPH_POS_Y - (Math.sin(degToRad(Prozessanzahl * Prozessanzahl
              + (360 / Prozessanzahl) * i)) * DISTANCE_Y)));

      // Adjazensmatrix erstmal so setzten, dass jeder Knoteen mit jedem
      // Knoten verbunden ist
      for (int j = 0; j < Prozessanzahl; j++) {
        if (i != j) {
          adjacenceMatrix[i][j] = 1;
        }
      }
    }

    // Graphendarstellung einstellen
    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // graphProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
    // Boolean.TRUE);
    graphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        (Color) Crashprozess.get("fillColor"));
    graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.TRUE);
    graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.TRUE);

    // Graphen erstellen
    g = lang.newGraph("processes", adjacenceMatrix, nodes, labels, null,
        graphProps);

    // Alle Kanten verstecken
    hideAllEdges();

    // Sourcecode erstellen unter dem Graphen
    codeSupport = lang.newSourceCode(new Coordinates(
        (int) (GRAPH_POS_X + DISTANCE_X * 2), GRAPH_POS_Y - DISTANCE_Y),
        "sourceCode", null, Highlight);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    String[] code = getCleanCodeExample().split("\n");
    for (int i = 0; i < code.length; i++) {
      codeSupport.addCodeLine(code[i], null, countTabs(code[i]), null);
    }

    lang.nextStep();

    // Text für "aktuelles Ereignisse
    currentEventText1 = lang.newText(new Coordinates(20,
        (int) (GRAPH_POS_Y + DISTANCE_Y * 1.5f)), "", "currentEvent1", null);
    currentEventText2 = lang.newText(new Coordinates(20,
        (int) (GRAPH_POS_Y + DISTANCE_Y * 1.5f) + 20), "", "currentEvent1",
        null);
    currentEventText3 = lang.newText(new Coordinates(20,
        (int) (GRAPH_POS_Y + DISTANCE_Y * 1.5f) + 40), "", "currentEvent1",
        null);

    // Startet den Algorithmus Election on Rings
    startElectionOnRings();

    // Hide Code
    lang.nextStep();

    // Abschlussfolie
    description.hide();
    g.hide();
    currentEventText1.hide();
    currentEventText2.hide();
    currentEventText3.hide();
    codeSupport.hide();

    // Sourcecode erstellen unter dem Graphen
    SourceCode sum = lang.newSourceCode(new Coordinates(20, 65), "sum", null,
        Highlight);
    sum.addCodeLine("Der neue Koordinator wurde nun gefunden", null, 0, null);
    sum.addCodeLine("Es ist Prozess " + foundCoordinator, null, 0, null);
    sum.addCodeLine("Es wurden " + messages
        + " Nachrichten geschickt um den Koordinator zu ermitteln", null, 0,
        null);

    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Election on Rings [DE]";
  }

  public String getAlgorithmName() {
    return "Election on Rings";
  }

  public String getAnimationAuthor() {
    return "Bennet Jeutter, Max Mindt";
  }

  public String getCleanDescription() {
    return "In verteilten Systemen ist es nötig, dass ein Prozess die Koordination übernimmt."
        + "\n"
        + "Der Election on Rings Algorithmus ist ein rekursiv, verteilter Algorithmus und dient zur Ermittlung eines solchen Koordinators."
        + "\n"
        + "Fällt der Koordinator im laufenden aus, z.B. durch einen Timeout, erkennt der Algorithmus dies und ermittelt einen neuen Koordinator."
        + "\n"
        + "Im Unterschied zum Bullyalgorithmus sind die Prozesse, in Election on Rings, im Ring angeordnet und jeder Prozess kennt seinen Nachfolger. ";
  }

  public String getCleanCodeExample() {
    return "1. Wenn ein Prozess bemerkt, dass der Koordinator nicht mehr erreichbar ist dann bildet er eine ELECTION mit seiner eigenen Prozessnummer."
        + "\n"
        + "	Sende die Nachricht zum Nachfolger"
        + "\n"
        + "	Ist der Nachfolger nicht erreichbar dann sende die Nachricht zum Nachfolger vom Nachfolger. So lange bis ein laufender Prozess gefunden wurde."
        + "\n"
        + "2. In jedem Schritt fügen die Prozesse ihre eigene Nummer der Nachricht hinzu"
        + "\n"
        + "3. Erreicht die Nachricht den Initiator, wechlse den Nachrichtentyp zu COORDINATOR und zirkuliere erneut"
        + "\n"
        + "     	Der Initiator erkennt sich wenn er seine Nummer in der Nachricht sieht"
        + "\n"
        + "4. Wenn der COORDINATOR einmal komplett zirkuliert ist"
        + "\n" + "	Kennt jeder Prozess den neuen COORDINATOR";
  }

  public String getDescription() {
    return "In verteilten Systemen ist es n&ouml;tig, dass ein Prozess die Koordination &uuml;bernimmt."
        + "\n"
        + "Der Election on Rings Algorithmus ist ein rekursiv, verteilter Algorithmus und dient zur Ermittlung eines solchen Koordinators."
        + "\n"
        + "F&auml;llt der Koordinator im laufenden aus, z.B. durch einen Timeout, erkennt der Algorithmus dies und ermittelt einen neuen Koordinator."
        + "\n"
        + "Im Unterschied zum Bullyalgorithmus sind die Prozesse, in Election on Rings, im Ring angeordnet und jeder Prozess kennt seinen Nachfolger. ";
  }

  public String getCodeExample() {
    return "1. Wenn ein Prozess bemerkt, dass der Koordinator nicht mehr erreichbar ist dann bildet er eine ELECTION mit seiner eigenen Prozessnummer."
        + "\n"
        + "	Sende die Nachricht zum Nachfolger"
        + "\n"
        + "	Ist der Nachfolger nicht erreichbar dann sende die Nachricht zum Nachfolger vom Nachfolger. So lange bis ein laufender Prozess gefunden wurde."
        + "\n"
        + "2. In jedem Schritt f&uuml;gen die Prozesse ihre eigene Nummer der Nachricht hinzu"
        + "\n"
        + "3. Erreicht die Nachricht den Initiator, wechlse den Nachrichtentyp zu COORDINATOR und zirkuliere erneut"
        + "\n"
        + "     	Der Initiator erkennt sich wenn er seine Nummer in der Nachricht sieht"
        + "\n"
        + "4. Wenn der COORDINATOR einmal komplett zirkuliert ist"
        + "\n" + "	Kennt jeder Prozess den neuen COORDINATOR";
  }

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

  private void unhighlight() {
    for (int i = 0; i < 8; i++)
      codeSupport.unhighlight(i);

  }

  public void statusMessage(STATUS status) {
    if (status.equals(Observer.STATUS.SUCCESSOR_ONLINE)) {
      codeSupport.highlight(1);
      codeSupport.highlight(3);
      currentEventText1.setText("Prozess leitet ELECTION weiter", null, null);
      currentEventText2.setText("", null, null);
      currentEventText3.setText("Es wurden bisher " + messages
          + " Nachrichten versendet", null, null);
    } else if (status.equals(Observer.STATUS.SUCCESSOR_OFFLINE)) {

      MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel(
          "multipleSelectionQuestion");
      msq.setPrompt("Was macht der Prozess jetzt alles?");
      msq.addAnswer("Auf Timeout des Prozess " + Prozessanzahl + " warten", 1,
          "Richtig");
      msq.addAnswer("Seine Prozessnummer der ELECTION dazulegen", 1, "Richtig");
      msq.addAnswer("ELECTION weiterschicken an den Nachfolger des Nachfolger",
          1, "Richtig");
      msq.setGroupID("QG");
      lang.addMSQuestion(msq);

      lang.nextStep();

      codeSupport.highlight(2);
      currentEventText1.setText("Prozess leitet ELECTION weiter", null, null);
      currentEventText2.setText("Gecrashter Prozess meldet sich nicht", null,
          null);
      currentEventText3.setText("Es wurden bisher " + messages
          + " Nachrichten versendet", null, null);
    } else if (status.equals(Observer.STATUS.COORDINATOR_CIRCULATE)) {

      TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
          "trueFalseQuestion", true, 5);
      tfq.setPrompt("Kennt jeder Prozess jetzt den neuen Koordinator?");
      tfq.setGroupID("QG");
      lang.addTFQuestion(tfq);

      lang.nextStep();

      codeSupport.highlight(6);
      codeSupport.highlight(7);
      currentEventText1.setText("COORDINATOR ist komplett zirkuliert", null,
          null);
      currentEventText2.setText("", null, null);
      currentEventText3.setText("Es wurden bisher " + messages
          + " Nachrichten versendet", null, null);
    } else if (status.equals(Observer.STATUS.INITIATOR_RECEIVE)) {

      MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
          "multipleChoiceQuestion");
      mcq.setPrompt("Wer ist Coordinator?");
      mcq.addAnswer("Der Initiator" + Identifizierungsknoten, 0,
          "Nein, er hat nur bemerkt, dass der ehm. Coordinator gecrasht ist");
      mcq.addAnswer("Der Prozess " + Prozessanzahl, 0,
          "Nein, er war Coordinator bevor er gecrasht ist");
      mcq.addAnswer("Der Prozess " + getCoordinator(), 1, "Richtige Antwort");
      mcq.setGroupID("QG");
      lang.addMCQuestion(mcq);

      lang.nextStep();

      codeSupport.highlight(4);
      codeSupport.highlight(5);
      currentEventText1.setText("ELECTION ist komplett zirkuliert", null, null);
      currentEventText2
          .setText("Prozess leitet COORDINATOR weiter", null, null);
      currentEventText3.setText("Es wurden bisher " + messages
          + " Nachrichten versendet", null, null);
    }

  }

  private int getCoordinator() {
    int j = -1;
    for (Process p : processes.values()) {
      if (p.iSonline() && p.getNumber() > j)
        j = p.getNumber();
    }
    return j;
  }

  @Override
  public void fireMessageChange(Process from, Process to, Message msg) {
    hideAllEdges();

    messages++;

    g.setEdgeWeight(from.getNumber() - 1, to.getNumber() - 1, msg.getType()
        + ":" + msg.getContent(), null, null);
    g.showEdge(from.getNumber() - 1, to.getNumber() - 1, null, null);

    currentEventText3.setText("Es wurden bisher " + messages
        + " Nachrichten versendet", null, null);

    lang.nextStep();

    unhighlight();
  }

  private double degToRad(double deg) {
    return deg / 180 * Math.PI;
  }

  /**
   * Versteckt alle Kanten eines Graphen
   * 
   * @param g
   */
  private void hideAllEdges() {
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
}