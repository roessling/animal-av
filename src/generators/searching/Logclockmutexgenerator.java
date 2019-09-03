package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class Logclockmutexgenerator implements Generator {
  private Language             lang;
  private int                  Knotenanzahl;
  private int                  Ressource_hat;
  private int[]                Zugriffsreihenfolge;
  private CircleProperties     Highlightprozessfarbe;
  private TextProperties       Beschreibungstext;
  private SourceCodeProperties Sourcecode;

  private int                  nachrichten;
  private Graph                g;
  private Text                 currentEvent1;
  private Text                 text2;
  private Text                 text3;
  private Text                 text4;
  private int                  hatRessource;
  private int                  naechster;
  private int                  uebernaechster;

  private Text                 headerText;
//  private Rect                 headerBorder;
//  private TextProperties       textProps;
  private SourceCode           src;
  private SourceCodeProperties sourceCodeProps;
  private SourceCode           desc;
  private SourceCodeProperties descCodeProps;
  private TextProperties       Kopfzeile;
  private RectProperties       Headerbox;

  public void init() {
    lang = new AnimalScript("Mutex mit Logical Clocks [DE]", "Pascal Schardt",
        800, 600);
    nachrichten = 0;
  }

  public void hideAllEdges() {
    for (int i = 0; i < Knotenanzahl; i++) {
      for (int j = 0; j < Knotenanzahl; j++) {
        g.hideEdge(i, j, null, null);
      }
    }
  }

  public void gibtRessourceFrei(int prozess) {
    g.highlightNode(prozess - 1, null, null);
    currentEvent1.setText("Prozess " + prozess + " gibt die Ressource frei",
        null, null);
    src.highlight(5);
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);
    text4.setText("Die Ressource besitzt: Die Ressource ist frei", null, null);

    lang.nextStep("Ressource freigegeben");

    src.unhighlight(5);
    src.highlight(6);
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);

    lang.nextStep();

    src.unhighlight(6);
    src.highlight(7);
    g.setEdgeWeight(prozess - 1, naechster - 1, "OK", null, null);
    g.showEdge(prozess - 1, naechster - 1, null, null);
    nachrichten = nachrichten + 1;
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);
    // intArray.put(0, 9, null, null);

    lang.nextStep();

    g.unhighlightNode(prozess - 1, null, null);
    hideAllEdges();
    src.unhighlight(7);
    src.highlight(3);
    src.highlight(13);
    src.highlight(14);
    g.highlightNode(naechster - 1, null, null);
    hatRessource = naechster;
    naechster = uebernaechster;
    /*
     * if (uebernaechster !=0) { for (int i = 0; i<Knotenanzahl; i++) { if
     * ((i!=(naechster-1))&&(i!=(hatRessource-1))) { g.setEdgeWeight(i,
     * naechster-1, "OK", null, null); g.showEdge(i, naechster-1, null, null);
     * nachrichten = nachrichten + 1; } } }
     */
    currentEvent1.setText("Prozess " + hatRessource + " bekommt die Ressource",
        null, null);
    text2
        .setText(
            "Prozess "
                + hatRessource
                + " wird aus den Warteschlangen der anderen Prozesse geloescht und diese geben neues OK",
            null, null);
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);
    text4.setText("Die Ressource besitzt: Prozess " + hatRessource, null, null);
    // intArray.put(0, 10, null, null);
    // intArray.put(0, 11, null, null);
    lang.nextStep("Ressourcenfreigabe erledigt");
  }

  public void prozesseAntworten(int prozess) {
    currentEvent1.setText("Die Anfragen werden von den Prozessen verarbeitet",
        null, null);
    text2.setText("", null, null);
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);
    src.highlight(9);

    lang.nextStep("Prozesse antworten");

    src.unhighlight(9);
    currentEvent1.setText("Die Anfragen werden von den Prozessen verarbeitet",
        null, null);
    for (int i = 0; i < Knotenanzahl; i++) {
      if ((i != (prozess - 1)) && (i != (hatRessource - 1))) {
        g.setEdgeWeight(i, prozess - 1, "OK", null, null);
        g.showEdge(i, prozess - 1, null, null);
        nachrichten = nachrichten + 1;
      }
    }

    text2.setText("", null, null);
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);
    src.highlight(11);
    // intArray.put(0, 7, null, null);
    // intArray.put(0, 8, null, null);

    lang.nextStep();

    src.unhighlight(11);
    text3.setText("", null, null);
    text2.setText("", null, null);

    lang.nextStep("Antwortphase vorüber");
  }

  public void prozessWillRessource(int prozess, int wievielter) {
    g.highlightNode(prozess - 1, null, null);
    currentEvent1.setText("Prozess " + prozess
        + " will die kritische Ressource.", null, null);
    src.highlight(0);

    lang.nextStep();

    src.unhighlight(0);
    currentEvent1.setText("Prozess " + prozess
        + " will die kritische Ressource.", null, null);
    src.highlight(1);

    lang.nextStep("Prozesse fordern Ressource");

    src.unhighlight(1);
    text3.setText("Die Ressource hat: Prozess " + hatRessource, null, null);
    text3.setText("", null, null);
    text2.setText("", null, null);
    currentEvent1.setText("", null, null);
    src.highlight(2);
    currentEvent1.setText("Prozess " + prozess
        + " sendet REQUESTS an Prozesse mit Time Stamp 8", null, null);
    for (int i = 0; i < Knotenanzahl; i++) {
      if (i != (prozess - 1)) {
        g.setEdgeWeight(prozess - 1, i, "REQUEST " + (8 * wievielter), null,
            null);
        g.showEdge(prozess - 1, i, null, null);
        nachrichten = nachrichten + 1;
      }
    }
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);
    // intArray.put(0, 1, null, null);
    // intArray.put(0, 2, null, null);
    // intArray.put(0, 3, null, null);

    lang.nextStep();

    hideAllEdges();
    src.unhighlight(2);
    src.highlight(3);
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);

    lang.nextStep();

    src.unhighlight(3);
    g.unhighlightNode(prozess - 1, null, null);
    src.highlight(0);

    lang.nextStep("Ressourcenforderungen abgeschlossen");
  }

  // public String generate(AnimationPropertiesContainer props,Hashtable<String,
  // Object> primitives) {
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    Ressource_hat = (Integer) primitives.get("Ressource_hat");
    Knotenanzahl = (Integer) primitives.get("Knotenanzahl");
    Beschreibungstext = (TextProperties) props
        .getPropertiesByName("Beschreibungstext");
    Zugriffsreihenfolge = (int[]) primitives.get("Zugriffsreihenfolge");
    Highlightprozessfarbe = (CircleProperties) props
        .getPropertiesByName("Highlightprozessfarbe");
    Sourcecode = (SourceCodeProperties) props.getPropertiesByName("Sourcecode");
    Kopfzeile = (TextProperties) props.getPropertiesByName("Kopfzeile");
    Headerbox = (RectProperties) props.getPropertiesByName("Headerbox");

    // Beginn eigener Implementierung

    // INITIALISIERUNG ANIMATION
    lang.setInteractionType(1024);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    lang.setStepMode(true);

    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        Kopfzeile.get(AnimationPropertiesKeys.FONT_PROPERTY));
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Kopfzeile.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    headerText = lang.newText(new Coordinates(20, 30),
        "Mutex mit Logical Clocks Animation", "headerText", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Headerbox.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    lang.newRect(new Coordinates(10, 20), new Coordinates(260,
        50), "headerBorder", null, rectProps);

    if (Ressource_hat > Knotenanzahl) {
      headerText
          .setText(
              "Ungültige Werte angegeben, Ressource_hat darf keinen Knoten enthalten, die größer als die Knotenmenge sind!",
              null, null);
      return lang.toString();
    }

    for (int knoten : Zugriffsreihenfolge) {
      if (knoten > Knotenanzahl) {
        headerText
            .setText(
                "Ungültige Werte angegeben, die Zugriffsreihenfolge darf keine Knoten enthalten, die größer als die Knotenmenge sind!",
                null, null);
        return lang.toString();
      }
    }

    descCodeProps = new SourceCodeProperties();
    descCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        Beschreibungstext.get(AnimationPropertiesKeys.FONT_PROPERTY));
    descCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Beschreibungstext.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    descCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLACK);
    descCodeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    descCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        new Color(255, 0, 0));
    desc = lang.newSourceCode(new Coordinates(20, 65), "description", null,
        descCodeProps);

    desc.addCodeLine(
        "In verteilten System muss man haeufig bestimmen, welcher Prozess eine kritische Ressource erhaelt.",
        null, 0, null);
    desc.addCodeLine(
        "Ein Mutex mit Locigal Clocks regelt den Zugriff auf diese kritische Ressource.",
        null, 0, null);
    desc.addCodeLine(
        "Die folgende Animation soll die Funktionsweise dieses Algorithmus veranschaulichen.",
        null, 0, null);

    lang.nextStep("Initialisieren");

    int[][] graphAdjacencyMatrix = new int[Knotenanzahl][Knotenanzahl];

    for (int i = 0; i < Knotenanzahl; i++) {
      for (int j = 0; j < Knotenanzahl; j++) {
        graphAdjacencyMatrix[i][j] = 1;
      }
    }

    Node[] graphNodes = new Node[Knotenanzahl];

    String[] labels = new String[Knotenanzahl];

    for (int i = 0; i < Knotenanzahl; i++) {
      int winkel = (int) (360 / Knotenanzahl);
      int x = (int) (200 + (100 * Math
          .cos((winkel * (i + 1)) / 180.0D * 3.141592653589793D)));
      int y = (int) (300 + (100 * Math
          .sin((winkel * (i + 1)) / 180.0D * 3.141592653589793D)));
      graphNodes[i] = new Coordinates(x, y);
      labels[i] = (i + 1) + "";
    }

    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    Color highlight = (Color) Highlightprozessfarbe
        .get(AnimationPropertiesKeys.FILL_PROPERTY);

    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);

    g = lang.newGraph("processes", graphAdjacencyMatrix, graphNodes, labels,
        null, graphProps);

    hideAllEdges();

    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        Sourcecode.get(AnimationPropertiesKeys.FONT_PROPERTY));
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Sourcecode.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLACK);
    sourceCodeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Sourcecode.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    src = lang.newSourceCode(new Coordinates(500, 150), "sourceCode", null,
        sourceCodeProps);

    src.addCodeLine("Will ein Prozess X die kritische Ressource: ", null, 0,
        null);
    src.addCodeLine(" Setze STATE = WANTED", null, 1, null);
    src.addCodeLine("	REQUEST an alle Prozesse mit Time Stamp", null, 1, null);
    src.addCodeLine(
        "	Sobald OK von allen Prozessen kam, nimm die Ressource und setze STATE = HELD",
        null, 1, null);
    src.addCodeLine("", null, 0, null);
    src.addCodeLine("Beim Verlassen der kritischen Ressource: ", null, 0, null);
    src.addCodeLine("	Setze STATE = RELEASED", null, 1, null);
    src.addCodeLine(
        "	OK an den Prozess mit kleinstem Time Stamp in Warteschlange ", null,
        1, null);
    src.addCodeLine("", null, 0, null);
    src.addCodeLine("Wenn ein Prozess Y eine Anfrage bekommt: ", null, 0, null);
    src.addCodeLine(" Wenn Ressource frei, gib OK zurueck ", null, 1, null);
    src.addCodeLine(
        "	Ansonsten Anfrage in Warteschlange einsortieren und OK an den Prozess mit kleinstem Time Stamp",
        null, 1, null);
    src.addCodeLine("", null, 0, null);
    src.addCodeLine("Wenn ein Prozess Z die Ressource bekommt: ", null, 0, null);
    src.addCodeLine(
        "loesche diesen aus der Warteschlange und gib OK an kleinsten Time Stamp, wenn diese nicht leer ist ",
        null, 0, null);

    lang.nextStep();

    hatRessource = Ressource_hat;

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        Beschreibungstext.get(AnimationPropertiesKeys.FONT_PROPERTY));
    textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Beschreibungstext.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    currentEvent1 = lang.newText(new Coordinates(20, 450), "", "currentEvent1",
        null, textProps);
    text2 = lang
        .newText(new Coordinates(20, 470), "", "Text2", null, textProps);
    text3 = lang
        .newText(new Coordinates(20, 510), "", "Text3", null, textProps);
    text4 = lang
        .newText(new Coordinates(20, 490), "", "Text4", null, textProps);
    currentEvent1.setText("Prozess " + hatRessource
        + " hat die kritische Ressource", null, null);
    text4.setText("Die Ressource besitzt: Prozess " + hatRessource, null, null);

    // ANIMATIONSGENERIERUNG START

    //

    lang.nextStep("Animationsgenerierung");

    int j = 1;
    for (int k : Zugriffsreihenfolge) {
      prozessWillRessource(k, j);
      j = j + 1;
    }

    naechster = 0;

    // WAS PASSIERT JETZT?
    MultipleChoiceQuestionModel msq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion");
    msq.setPrompt("Was passiert jetzt?");
    msq.addAnswer(
        "Der Prozess " + Zugriffsreihenfolge[0] + " bekommt die Ressource",
        0,
        "Falsch, dieser bekommt die Ressource erst, wenn ihm alle anderen Prozesse ihr Okay gegeben haben.");
    msq.addAnswer("Die Prozesse geben ihr Okay an den Prozess "
        + Zugriffsreihenfolge[0] + "", 1,
        "Richtig, da er den kleinsten Timestamp hat.");
    msq.addAnswer("Nichts passiert, der Algorithmus terminiert hier", 0,
        "Nein, denn es hatte ja noch kein Prozess, der angefragt hatte, die Ressource.");
    msq.setGroupID("QG");
    this.lang.addMCQuestion(msq);

    this.lang.nextStep("Algorithmus-Start");

    for (int p : Zugriffsreihenfolge) {
      uebernaechster = p;

      if (naechster != 0) {

        prozesseAntworten(naechster);

        gibtRessourceFrei(hatRessource);

      }

      naechster = p;
    }
    uebernaechster = 0;
    prozesseAntworten(naechster);

    gibtRessourceFrei(hatRessource);

    src.unhighlight(3);
    src.unhighlight(13);
    src.unhighlight(14);
    text3.setText("Es wurden bisher " + nachrichten + " Nachrichten versendet",
        null, null);

    lang.nextStep();

    lang.nextStep("Fazit");

    SourceCodeProperties sumCodeProps = new SourceCodeProperties();
    sumCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
    sumCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sumCodeProps
        .set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    sumCodeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    sumCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        new Color(255, 0, 0));
    SourceCode sum = lang.newSourceCode(new Coordinates(20, 65), "sum", null,
        sumCodeProps);

    sum.addCodeLine("Alle Ressourcenanfragen wurden befriedigt", null, 0, null);
    sum.addCodeLine("", null, 0, null);
    int prozessanzahl = Zugriffsreihenfolge.length;
    sum.addCodeLine("Es wurden " + nachrichten + " Nachrichten geschickt, um "
        + prozessanzahl + " Prozessen die Ressource zu gewaehren", null, 0,
        null);

    desc.hide();
    g.hide();
    currentEvent1.hide();
    text2.hide();
    text3.hide();
    text4.hide();
    src.hide();

    // Ende eigener Implementierung

    lang.nextStep("Ende des Algorithmus");
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Mutex mit Logical Clocks [DE]";
  }

  public String getAlgorithmName() {
    return "Mutex mit Logical Clocks [DE]";
  }

  public String getAnimationAuthor() {
    return "Pascal Schardt";
  }

  public String getDescription() {
    return "In verteilten System muss man haeufig bestimmen, welcher Prozess eine kritische Ressource erhaelt."
        + "\n"
        + "Ein Mutex mit Locigal Clocks regelt den Zugriff auf diese kritische Ressource.";
  }

  public String getCodeExample() {
    return "Will ein Prozess X die kritische Ressource:"
        + "\n"
        + "     Setze STATE = WANTED"
        + "\n"
        + "     REQUEST an alle Prozesse mit Time Stamp"
        + "\n"
        + "     Sobald OK von allen Prozessen kam, nimm die Ressource und setze STATE = HELD"
        + "\n"
        + "\n"
        + "Beim Verlassen der kritischen Ressource:"
        + "\n"
        + "     Setze STATE = RELEASED"
        + "\n"
        + "     OK an den Prozess mit kleinstem Time Stamp in Warteschlange"
        + "\n"
        + "\n"
        + "Wenn ein Prozess Y eine Anfrage bekommt:"
        + "\n"
        + "     Wenn Ressource frei, gib OK zurueck "
        + "\n"
        + "     Ansonsten Anfrage in Warteschlange einsortieren und OK an den Prozess mit kleinstem Time Stamp"
        + "\n"
        + "\n"
        + "Wenn ein Prozess Z die Ressource bekommt:"
        + "\n"
        + "     loesche diesen aus der Warteschlange und gib OK an kleinsten Time Stamp, wenn diese nicht leer ist";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}