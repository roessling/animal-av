package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class FloydCycle implements ValidatingGenerator {
  private Language             lang;
  private Color                highlightcolor;
  private RectProperties       stepbarProps;
  private int[]                graph;
  private TextProperties       infotextProps;
  private SourceCodeProperties sourceCodeProps;
  private Color                fillcolor;
  private TextProperties       textProps;
  private Color                indicecolor;

  // own stuff
  private Graph                graphP;
  private Vertex               root;
  private GraphProperties      graphProps;

  public FloydCycle() {
    this.init();
  }

  public void init() {
    lang = new AnimalScript("Hase & Igel Algorithmus",
        "Martin Distler & Simon Werner", 1400, 1050);
    lang.setStepMode(true);

    // nicht entfernen
    stepbarProps = new RectProperties();
    stepbarProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    stepbarProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.magenta);
    stepbarProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    // nicht entfernen
    infotextProps = new TextProperties();
    infotextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    infotextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 14));
    infotextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    if (graph.length < 10)
      return true;
    else
      return false;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    if (props == null | primitives == null) {
      graph = new int[9];
      for (int i = 0; i < graph.length - 1; i++)
        graph[i] = i + 1;

      graph[graph.length - 1] = 7;

      sourceCodeProps = new SourceCodeProperties();
      textProps = new TextProperties();

      graphProps = new GraphProperties();
      graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
          Color.MAGENTA);
      graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK); // Farbes
                                                                               // der
                                                                               // Labels
      graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN); // ?
      graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.pink); // Farbe
                                                                         // der
                                                                         // Nodes
      graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false); // ungewichtete
                                                                        // Kanten
      graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true); // directed
                                                                       // graph

    } else {
      highlightcolor = (Color) primitives.get("highlightcolor");

      stepbarProps = (RectProperties) props.getPropertiesByName("stepbarProps");

      graph = (int[]) primitives.get("ListenArray");
      for (int i = 0; i < graph.length; i++) {
        System.out.println("graph[" + i + "] = " + graph[i]);
      }

      infotextProps = (TextProperties) props
          .getPropertiesByName("infotextProps");
      sourceCodeProps = (SourceCodeProperties) props
          .getPropertiesByName("sourceCodeProps");
      fillcolor = (Color) primitives.get("fillcolor");
      textProps = (TextProperties) props.getPropertiesByName("textProps");
      indicecolor = (Color) primitives.get("indicecolor");

      graphProps = new GraphProperties();
      graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
          this.highlightcolor);
      graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY,
          this.indicecolor); // Farbes der Labels
      graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN); // ?
      graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, this.fillcolor); // Farbe
                                                                             // der
                                                                             // Nodes
      graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, false); // ungewichtete
                                                                        // Kanten
      graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true); // directed
                                                                       // graph

    }

    this.createSlides();

    return lang.toString();

  }

  public String getName() {
    return "Hase und Igel Algorithmus";
  }

  public String getAlgorithmName() {
    return "Hase und Igel Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Martin Distler, Simon Werner";
  }

  public String getDescription() {
    return "Das Ziel dieses Generators ist den Begriff \"Zyklus\" im Rahmen der Informatik und Mathematik zu erklaeren und den Hase & Igel Algorithmus, ein Algorithmus zum finden von Zyklen, zu erlaeutern."
        + "\n Des Weiteren werden kurz Alternativen zum finden von Zyklen aufgewiesen sowie eine davon, die Iterative Suche mit Merken der besuchten Knoten (auch \"Merken\" genannt), angewendet und mit dem Hase & Igel Algorithmus verglichen.";
  }

  public String getCodeExample() {
    return "public boolean FloydCycle(Vertex root) {" + "\n"
        + "		Vertex sonic = root;" + "\n" + "		Vertex hare = root.getNext();"
        + "\n" + "\n" + "		while (hare != sonic) {" + "\n"
        + "			sonic = sonic.getNext();" + "\n" + "			" + "\n"
        + "			hare = hare.getNext();" + "\n" + "			if (hare != null) " + "\n"
        + "				hare = hare.getNext();" + "\n" + "\n"
        + "			if (sonic == null || hare == null)" + "\n" + "				return false;"
        + "\n" + "		}" + "\n" + "		" + "\n" + "		return true;" + "\n" + "    }";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void createSlides() {
    this.createIntro();
    this.createAlgorithmSlides();
    this.createIterativeSlides();
    this.createSummary();
  }

  public void createIntro() {
    // Page 1
    Text header = lang.newText(new Coordinates(20, 10),
        "Hase & Igel - Herleitung und die Bedeutung von Zyklen / Schleifen",
        "headerIntro", null, textProps);
    Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
    Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
    lang.newRect(offsetLeft, offsetRight, "headerRect", null);

    SourceCode einleitung = lang.newSourceCode(new Coordinates(20, 40),
        "einleitung", null, sourceCodeProps);

    einleitung.addCodeLine("Ueber Zyklen im Allgemeinen", "", 0, null);
    einleitung
        .addCodeLine(
            "Eim Zyklus in der Informatik und Mathematik beschreibt eine sich wiederholende Sequenz in iterativen Funktionswerten.",
            "", 1, null);
    einleitung
        .addCodeLine(
            "Ein einfaches Beispiel waere die Funktion f(x) = (2x + 1) mod 3 mit Startwert X_0 = 1 und der iterationsvorschrift X_n = f(X_n-1).",
            "", 1, null);

    lang.nextStep();
    einleitung.addCodeLine("Daraus folgt nun fuer X_1 bis X_5:", "", 1, null);
    einleitung.addCodeLine("X_1 = f(X_0) = (2 + 1) mod 3 = 0", "", 3, null);
    einleitung.addCodeLine("X_2 = f(X_1) = (0 + 1) mod 3 = 1", "", 3, null);
    einleitung.addCodeLine("X_3 = f(X_2) = (2 + 1) mod 3 = 0", "", 3, null);
    einleitung.addCodeLine("X_4 = f(X_3) = (0 + 1) mod 3 = 1", "", 3, null);
    einleitung.addCodeLine("X_5 = f(X_4) = (2 + 1) mod 3 = 0", "", 3, null);
    einleitung.addCodeLine("", "", 0, null);

    lang.nextStep();
    einleitung
        .addCodeLine(
            "Der hier enstandene Zyklus ist also 0, 1, 0, 1, 0 und man kann sich darunter nun einen Zustandsgraphen vorstellen,",
            "", 1, null);
    einleitung
        .addCodeLine(
            "der 2 Zustaende (0 und 1) besitzt und bei jeder Iteration zum jeweils anderen Zustand springt.",
            "", 1, null);
    einleitung.addCodeLine("", "", 0, null);

    lang.nextStep();
    einleitung.addCodeLine("Zyklen in der Praxis", "", 0, null);
    einleitung
        .addCodeLine(
            "Ein Anwendungsgebiet ist zum Beispiel das Entwickeln eines RNG (Random number generator). Hier stellt die laenge der Zyklen",
            "", 1, null);
    einleitung
        .addCodeLine(
            "einen Wert dar ueber den sich die Staerke eines Generators beurteilen laesst denn je laenger die Periodenlaenge, desto zufaelliger wirken die Zahlen.",
            "", 1, null);
    einleitung.addCodeLine("", "", 0, null);

    lang.nextStep();
    einleitung
        .addCodeLine(
            "Ein weiteres Anwendungsgebiet stellt die Kryptographie dar: um zu beurteilen ob ein kryptographisches Verfahren, bspw. ein Hashverfahren,",
            "", 1, null);
    einleitung
        .addCodeLine(
            "moeglichst sicher ist, wird sie auf Zyklen untersucht. Wenn es moeglich ist mit zwei Unterschiedlichen Werten X_n und X_m den gleichen Wert",
            "", 1, null);
    einleitung
        .addCodeLine(
            "aus der Funktion f(x) zu erhalten, so weist die Funktion schwaechen auf und ist potenziell Unsicher.",
            "", 1, null);
    einleitung.addCodeLine("", "", 0, null);

    lang.nextStep();
    einleitung
        .addCodeLine(
            "Und natuerlich gibt es auch in der Mathematik Anwendungsfaelle, wie zum Beispiel die Pollard-Rho-Methoden.",
            "", 1, null);
    einleitung
        .addCodeLine(
            "Diese Methoden bestimmen die Periodenlaenge einer Zahlenfolge, die anhand einer nathematischen Funktion berechnet wird.",
            "", 1, null);
    einleitung
        .addCodeLine(
            "Mit diesen Methoden lassen sich schwierige mathematische Probleme wie der diskrete Logarithmus und die Faktorisierung berechnen.",
            "", 1, null);

    // Page 2
    lang.nextStep();
    lang.addLine("hideAll");

    header = lang.newText(new Coordinates(20, 10),
        "Hase & Igel - der Algorithmus", "headerIntro", null, textProps);
    offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
    offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
    lang.newRect(offsetLeft, offsetRight, "headerRect", null);

    einleitung = lang.newSourceCode(new Coordinates(20, 40), "einleitung",
        null, sourceCodeProps);

    einleitung.addCodeLine("Hase & Igel Algorithmus", "", 0, null);
    einleitung
        .addCodeLine(
            "Der Hase & Igel Algorithmus ist ein von Robert Floyd entwickelter Algorithmus zum finden von Schleifen",
            "", 1, null);
    einleitung
        .addCodeLine(
            "in einfach verketteten Listen mit der Zeitkomplexitaet O(n) und einer Platzkomplexitaet von O(1). ",
            "", 1, null);
    einleitung.addCodeLine(
        "Mathematisch betrachtet dient er zum Auffinden von Zyklen in Folgen.",
        "", 1, null);
    einleitung
        .addCodeLine(
            "Dieser Algorithmus darf nicht mit Floyds Algorithmus aus der Graphentheorie (Floyd-Warshall) verwechselt werden.",
            "", 1, null);
    einleitung.addCodeLine("", "", 1, null);

    lang.nextStep();
    einleitung.addCodeLine("Prinzip", "", 0, null);
    einleitung
        .addCodeLine(
            "Der Algorithmus besteht aus dem gleichzeitigen Durchlauf der Liste anhand 2 Zeiger mit unterschiedlichen Schrittweiten.",
            "", 1, null);
    einleitung
        .addCodeLine(
            "Der erste Zeiger (Igel) startet auf dem ersten Feld und bewegt sich jede Iteration auf das naechste Feld.",
            "", 1, null);
    einleitung
        .addCodeLine(
            "Der zweite Zeiger (Hase) hingegen startet auf dem 2. Feld und bewegt sich jede Iteration aufs uebernaechste Feld.",
            "", 1, null);
    einleitung.addCodeLine(
        "Wenn sich beide Zeiger treffen, wurde ein Zyklus gefunden.", "", 1,
        null);
    einleitung
        .addCodeLine(
            "Wenn einer der beiden Zeiger das Ende der Liste erreicht hat, so hat die Liste keine Schleife.",
            "", 1, null);
    einleitung.addCodeLine("", "", 1, null);

    lang.nextStep();
    einleitung.addCodeLine("Trivia", "", 0, null);
    einleitung
        .addCodeLine(
            "Der Algorithmus terminiert in endlicher Zeit, da der Hase in jeder Iteration ein Feld naeher an den Igel rankommt.",
            "", 1, null);
    einleitung
        .addCodeLine(
            "Der Hase & Igel Algorithmus wird im englischen auch als 'Tortoise and hare algorithm' bezeichnet und wurde nach der gleichnamigen Fabel benannt.",
            "", 1, null);
    einleitung
        .addCodeLine(
            "In Bewerbungsgespraechen im Bereich der Informatik werden oftmals Probleme dargestellt und nach Loesungsansaetzen gefragt",
            "", 1, null);
    einleitung
        .addCodeLine(
            "wobei der Hase & Igel Algorithmus ein oft und gern gesehener Ansatz fuer das auffinden von Zyklen ist.",
            "", 1, null);

    // Page 3
    lang.nextStep();
    lang.addLine("hideAll");

    header = lang.newText(new Coordinates(20, 10),
        "Hase & Igel - Alternativen", "headerIntro", null, textProps);
    offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
    offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
    lang.newRect(offsetLeft, offsetRight, "headerRect", null);

    einleitung = lang.newSourceCode(new Coordinates(20, 40), "einleitung",
        null, sourceCodeProps);

    einleitung.addCodeLine("Verschiedene Ansaetze", "", 0, null);
    einleitung
        .addCodeLine(
            "Es gibt mehrere Varianten zum Auffinden von Zyklen / Schleifen in Listen, unter anderem:",
            "", 1, null);
    einleitung.addCodeLine("Iterative Suche mit Merken der besuchten Elemente",
        "", 2, null);
    einleitung.addCodeLine("Ansatz", "", 3, null);
    einleitung
        .addCodeLine(
            "Es wird iterativ durch jedes Element der Liste gegangen und die besuchten werden in einer exta Liste gespeichert.",
            "", 4, null);
    einleitung
        .addCodeLine(
            "Ist nun das naechste zu untersuchende Element bereits in der Besucht-Liste, wurde eine Schleife gefunden.",
            "", 4, null);
    einleitung.addCodeLine("", "", 0, null);

    lang.nextStep();
    einleitung.addCodeLine("Ausnutzen der doppelten Verkettung", "", 2, null);
    einleitung.addCodeLine("Ansatz", "", 3, null);
    einleitung
        .addCodeLine(
            "Jedes Element in einer doppelt verketteten Liste hat einen Zeiter auf das folgende als auch",
            "", 4, null);
    einleitung
        .addCodeLine(
            "auf das vorhergehende Element. Beim Durchlauf einer solchen Liste muss also jedes Element",
            "", 4, null);
    einleitung.addCodeLine(
        "das vorher besuchte als vorhergehendes referenzieren.", "", 4, null);
    einleitung
        .addCodeLine(
            "Wenn dem nicht so ist (Korrektheit der Verkettung vorrausgesetzt!) wurde eine Schleife gefunden, da zu diesem Element",
            "", 4, null);
    einleitung.addCodeLine("zwei Zeiger existieren muessen.", "", 4, null);
    einleitung.addCodeLine("", "", 0, null);

    lang.nextStep();
    einleitung.addCodeLine("Vergleich mit dem Startelement", "", 2, null);
    einleitung.addCodeLine("Ansatz", "", 3, null);
    einleitung
        .addCodeLine(
            "Der Zeiger auf das naechste Element jedes Listenelements wird mit dem Startelement verglichen.",
            "", 4, null);
    einleitung.addCodeLine(
        "Zeigt ein Element auf das Startelement wurde eine Schleife gefunden.",
        "", 4, null);
    einleitung.addCodeLine("", "", 0, null);

    lang.nextStep();
    einleitung
        .addCodeLine(
            "Im Folgenden wird nun der Hase & Igel Algorithmus sowie zum direkten Vergleich der naive Ansatz",
            "", 1, null);
    einleitung
        .addCodeLine(
            "der Iterativen Suche mit Merken der besuchten Elemente angwandt. Wir starten mit dem Hase & Igel Algorithmus.",
            "", 1, null);
  }

  public void createAlgorithmSlides() {
    this.createGraph();

    this.doFloyd(root);
    this.doIterative(root);
  }

  /**
   * Calculates coordinates on a circle
   * 
   * @param middle
   *          of the Circle
   * @param radius
   *          of the Circle
   * @param anz
   *          of Notes
   * @return the coordinates
   */
  public List<Coordinates> CircleCoords(Coordinates middle, int radius, int anz) {
    List<Coordinates> result = new ArrayList<Coordinates>();

    double deg = (Math.PI * 2) / anz;
    int x = 0, y = 0;

    for (int i = 0; i < anz; i++) {
      x = (int) Math.round((Math.cos(i * deg) * radius) + middle.getX());
      y = (int) Math.round((Math.sin(i * deg) * radius) + middle.getY());
      result.add(new Coordinates(x, y));
    }

    return result;
  }

  /**
   * Floyds cycle algorithm - finds cycles in a given linked list / graph
   * 
   * @param root
   *          Starting vertex / item
   * @returns true if a cycle was found, else false
   */
  public boolean doFloyd(Vertex root) {
    lang.nextStep();
    lang.addLine("hideAll");
    graphP.show();

    Text header = lang.newText(new Coordinates(20, 10),
        "Hase & Igel - Anwendung", "headerAlgorithm", null, textProps);
    Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
    Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
    lang.newRect(offsetLeft, offsetRight, "headerRect", null);

    SourceCode code = lang.newSourceCode(new Coordinates(20, 40), "sourceCode",
        null, sourceCodeProps);
    code.addCodeLine("public boolean algorithm(Node root) {", "", 0, null);
    code.addCodeLine("Node sonic = root;", "", 1, null);
    code.addCodeLine("Node rabbit = root.getNext();", "", 1, null);
    code.addCodeLine("", "", 0, null);
    code.addCodeLine("while (rabbit != sonic) ", "", 1, null);
    code.addCodeLine("{", "", 1, null);
    code.addCodeLine("sonic = sonic.getNext();", "", 2, null);
    code.addCodeLine("rabbit = rabbit.getNext();", "", 2, null);
    code.addCodeLine("if (rabbit != null) ", "", 2, null);
    code.addCodeLine("rabbit = rabbit.getNext();", "", 3, null);
    code.addCodeLine("", "", 0, null);
    code.addCodeLine("if (sonic == null || rabbit == null)", "", 2, null);
    code.addCodeLine("return false;", "", 3, null);
    code.addCodeLine("}", "", 1, null);
    code.addCodeLine("", "", 0, null);
    code.addCodeLine("return true;", "", 1, null);
    code.addCodeLine("}", "", 0, null);

    Vertex sonic = root;
    Vertex rabbit = root.getNext();

    List<Coordinates> rabbitPos = CircleCoords(new Coordinates(600, 150), 80,
        graph.length);
    List<Coordinates> sonicPos = CircleCoords(new Coordinates(600, 150), 120,
        graph.length);

    Text rabbitT = lang.newText(rabbitPos.get(rabbit.getId() - 1), "R",
        "rabbitName", null, textProps);
    Text sonicT = lang.newText(sonicPos.get(sonic.getId() - 1), "S",
        "sonicName", null, textProps);

    Rect step = lang.newRect(new Coordinates(560, 300), new Coordinates(565,
        320), "stepRect", null, stepbarProps);
    lang.newText(new Coordinates(490, 296), "Schritte: ", "steps_txt", null,
        infotextProps);
    Text stepCnt = lang.newText(new Coordinates(570, 300), "0", "stepCnt",
        null, infotextProps);

    code.highlight(1);
    code.highlight(2);

    lang.nextStep();
    code.unhighlight(1);
    code.unhighlight(2);

    code.highlight(4);

    Integer i = 0;
    while (rabbit != sonic) {
      lang.nextStep();

      i++;
      step.moveBy("translate #2", 4, 0, null, null);
      stepCnt.hide();
      stepCnt = lang.newText(new Coordinates(570 + i * 4, 300), i.toString(),
          "stepCnt", null, infotextProps);

      code.unhighlight(4);

      code.highlight(6);
      code.highlight(7);

      sonic = sonic.getNext();
      sonicT.hide();
      sonicT = lang.newText(sonicPos.get(sonic.getId() - 1), "S", "sonicName",
          null, textProps);

      rabbit = rabbit.getNext();

      if (rabbit != null) {
        rabbitT.hide();
        rabbitT = lang.newText(rabbitPos.get(rabbit.getId() - 1), "R",
            "rabbitName", null, textProps);
      } else {
        rabbitT.hide();
        rabbitT = lang.newText(new Coordinates(550, 150), "R", "rabbitName",
            null, textProps);
      }

      lang.nextStep();
      code.unhighlight(6);
      code.unhighlight(7);
      code.highlight(8);

      if (rabbit != null) {
        lang.nextStep();
        code.unhighlight(8);
        code.highlight(9);
        rabbit = rabbit.getNext();
        if (rabbit != null) {
          rabbitT.hide();
          rabbitT = lang.newText(rabbitPos.get(rabbit.getId() - 1), "R",
              "rabbitName", null, textProps);
        } else {
          rabbitT.hide();
          rabbitT = lang.newText(new Coordinates(550, 150), "R", "rabbitName",
              null, textProps);
        }
      }

      lang.nextStep();
      code.unhighlight(8);
      code.unhighlight(9);
      code.highlight(11);
      if (sonic == null || rabbit == null) {
        lang.nextStep();
        code.unhighlight(11);
        code.highlight(12);

        lang.newText(new Coordinates(100, 400), "Kein Zyklus gefunden",
            "result", null, textProps);

        lang.nextStep();

        return false;
      }
      lang.nextStep();
      code.unhighlight(11);
      code.unhighlight(12);
      code.highlight(4);
    }
    lang.nextStep();
    code.unhighlight(4);
    code.highlight(15);

    lang.newText(new Coordinates(100, 400), "Zyklus gefunden", "result", null,
        textProps);

    lang.nextStep();

    return true;
  }

  /**
   * Iterative search for cycles
   * 
   * @param root
   *          Starting vertex / item
   * @returns true if a cycle was found, else false
   */
  public boolean doIterative(Vertex root) {
    // lang.nextStep();
    lang.addLine("hideAll");
    graphP.show();

    Text header = lang.newText(new Coordinates(20, 10),
        "Im Vergleich - Iterative Suche mit Merken der Elemente", "headerBSF",
        null, textProps);
    Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
    Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
    lang.newRect(offsetLeft, offsetRight, "headerRect", null);

    Rect step = lang.newRect(new Coordinates(560, 300), new Coordinates(565,
        320), "stepRect", null, stepbarProps);
    lang.newText(new Coordinates(490, 296), "Schritte: ", "steps_txt", null,
        infotextProps);
    Text stepCnt = lang.newText(new Coordinates(570, 300), "0", "stepCnt",
        null, infotextProps);

    SourceCode code = lang.newSourceCode(new Coordinates(20, 40), "sourceCode",
        null, sourceCodeProps);
    code.addCodeLine("public boolean doIterative(Vertex root) {", "", 0, null);
    code.addCodeLine("List<Vertex> visited = new ArrayList<Vertex>();", "", 1,
        null);
    code.addCodeLine("Vertex vertex = root;", "", 1, null);
    code.addCodeLine("", "", 0, null);
    code.addCodeLine("while(vertex.getNext() != null)", "", 1, null);
    code.addCodeLine("{", "", 2, null);
    code.addCodeLine("if(visited.contains(vertex.getNext()))", "", 3, null);
    code.addCodeLine("return true;", "", 4, null);
    code.addCodeLine(" ", "", 0, null);
    code.addCodeLine("visited.add(vertex);", "", 3, null);
    code.addCodeLine("vertex = vertex.getNext();", "", 3, null);
    code.addCodeLine("}", "", 2, null);
    code.addCodeLine("", "", 0, null);
    code.addCodeLine("return false;", "", 1, null);
    code.addCodeLine("}", "", 0, null);
    code.addCodeLine("", "", 0, null);

    List<Vertex> visited = new ArrayList<Vertex>();
    Vertex vertex = root;
    boolean cycle = false;

    SourceCode scVisited = lang.newSourceCode(new Coordinates(735, 25),
        "visited", null, sourceCodeProps);
    scVisited.addCodeLine("Visited:", "", 0, null);

    int tmp;
    Integer i = 0;
    code.highlight(4);
    graphP.highlightNode(vertex.getId() - 1, null, null);
    while (vertex.getNext() != null && !cycle) {
      lang.nextStep();

      i++;
      step.moveBy("translate #2", 4, 0, null, null);
      stepCnt.hide();
      stepCnt = lang.newText(new Coordinates(570 + i * 4, 300), i.toString(),
          "stepCnt", null, infotextProps);

      code.toggleHighlight(4, 6);
      if (visited.contains(vertex.getNext())) {
        scVisited.highlight(vertex.getNext().getId());

        lang.nextStep();
        code.toggleHighlight(6, 7);
        cycle = true;
        graphP.highlightNode(vertex.getNext().getId() - 1, null, null);
        graphP.unhighlightNode(vertex.getId() - 1, null, null);
        break;
      }

      lang.nextStep();
      code.toggleHighlight(6, 9);

      visited.add(vertex);
      scVisited.addCodeLine(Integer.toString(vertex.getId()), "", 1, null);
      lang.nextStep();
      code.toggleHighlight(9, 10);
      tmp = vertex.getId();
      vertex = vertex.getNext();

      graphP.highlightNode(vertex.getId() - 1, null, null);
      graphP.unhighlightNode(tmp - 1, null, null);

      lang.nextStep();
      code.toggleHighlight(10, 4);
    }

    if (!cycle) {
      code.toggleHighlight(4, 13);
      graphP.highlightNode(vertex.getId() - 1, null, null);
      lang.newText(new Coordinates(100, 400), "Kein Zyklus gefunden", "result",
          null, textProps);
    } else
      lang.newText(new Coordinates(100, 400), "Zyklus gefunden", "result",
          null, textProps);

    return cycle;
  }

  /**
   * Creates the Iterative-Slides
   */
  public void createIterativeSlides() {
    lang.nextStep();
    lang.addLine("hideAll");

    Text header = lang.newText(new Coordinates(20, 10),
        "Iterative Suche mit Merken der Elemente - Garnicht so schlecht?",
        "headerIntro", null, textProps);
    Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
    Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
    lang.newRect(offsetLeft, offsetRight, "headerRect", null);

    SourceCode text = lang.newSourceCode(new Coordinates(20, 40),
        "iterative_text", null, sourceCodeProps);

    int nodes = graph.length;
    text.addCodeLine(
        "Auf den ersten Blick sieht die Iterative Suche mit Merken der bereits besuchten Elemente",
        "", 0, null);
    text.addCodeLine("garnicht so schlecht aus, schliesslich hat sie nur "
        + nodes + " Schritte benoetigt ", "", 0, null);
    text.addCodeLine(
        "und liegt somit in der Zeitkomplexitaet von O(n) fuer n Elemente in der Liste.",
        "", 0, null);
    text.addCodeLine("", "", 0, null);
    text.addCodeLine("...oder?", "", 0, null);

    lang.nextStep();
    text.addCodeLine("Nein, nicht wirklich.", "", 0, null);
    text.addCodeLine(
        "Der grosse Performanzfaktor ist hier die staendig aufgerufene Methode contains().",
        "", 0, null);
    text.addCodeLine(
        "Die Implementierung von contains() entspricht im Grunde einer For-Schleife, welche",
        "", 0, null);
    text.addCodeLine(
        "ueber jedes Element der Liste iteriert und dabei in jedem Schritt die equals() Methode des Objekts aufruft.",
        "", 0, null);
    text.addCodeLine(
        "Das fuehrt also zu einer Komplexitaet von O(m) bei m-Elementen in der Liste.",
        "", 0, null);
    text.addCodeLine("", "", 0, null);

    lang.nextStep();
    text.addCodeLine("Also bedeutet das fuer unser Beispiel konkret:", "", 0,
        null);
    text.addCodeLine(
        "Zunaechst haben wir fuer das iterieren ueber alle Elemente der Liste bereits eine Komplexitaet von O("
            + nodes + "), ", "", 0, null);
    text.addCodeLine("da wir insgesamt " + nodes + " Elemente haben.", "", 0,
        null);
    text.addCodeLine(
        "Innerhalb dieser Schleife rufen wir visited.contains() auf in der die bereits besuchten Elemente durchgegangen werden.",
        "", 0, null);
    text.addCodeLine(
        "Im Falle, dass das letzte Element einen Zyklus erzeugt, sind das also "
            + (nodes - 1) + " Elemente.", "", 0, null);
    text.addCodeLine(
        "Damit ergibt sich also als Zeitkomplexitaetsklasse fuer die Iterative Suche mit Merken der Elemente insgesamt",
        "", 0, null);
    text.addCodeLine("O(" + nodes + " * " + (nodes - 1) + ") = O("
        + (nodes * (nodes - 1)) + ").", "", 0, null);
    text.addCodeLine("", "", 0, null);

    lang.nextStep();
    text.addCodeLine(
        "Zusaetzlich zur erhoehten Zeitkomplexitaet kommt allerdings noch die Platzkomplexitaet.",
        "", 0, null);
    text.addCodeLine("Diese entspricht hier also ebenfalls O(" + (nodes - 1)
        + "), da " + (nodes - 1) + " Elemente gespeichert werden muessen.", "",
        0, null);
    text.addCodeLine(
        "Man sieht also ganz gut anhand der Zeit- und Platzkomplexitaet das die Iterative Suche mit Merken der Elemente",
        "", 0, null);
    text.addCodeLine(
        "keine besonders gute Skalierbarkeit aufweist, denn schon bei Insgesamt 1 000 Knoten,",
        "", 0, null);
    text.addCodeLine(
        "was in grossen Listen nicht sonderlich viel ist, haben wir insgesamt eine Zeitkomplexitaet von",
        "", 0, null);
    text.addCodeLine(
        "O(1 000 * 999) = O(999 000) mit einer Platzkomplexitaet von O(999).",
        "", 0, null);
    text.addCodeLine("", "", 0, null);

    lang.nextStep();
    text.addCodeLine(
        "Allgemein kann man sagen, dass bei Listen mit n Elementen annaehernd eine Zeitkomplexitaet von O(n^2)",
        "", 0, null);
    text.addCodeLine("und eine Platzkomplexitaet von O(n) vorliegt.", "", 0,
        null);

  }

  /**
   * Creates the Summary-Slides
   */
  public void createSummary() {
    lang.nextStep();
    lang.addLine("hideAll");

    Text header = lang.newText(new Coordinates(20, 10),
        "Hase & Igel - Zusammenfassung", "headerIntro", null, textProps);
    Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
    Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
    lang.newRect(offsetLeft, offsetRight, "headerRect", null);

    SourceCode summary = lang.newSourceCode(new Coordinates(20, 40), "summary",
        null, sourceCodeProps);

    summary
        .addCodeLine(
            "Zyklen und ihre Laengen sind ein wichtiges Hilfsmittel zum Messen von Sicherheit im Bereich der Kryptographie.",
            "", 0, null);
    summary.addCodeLine(" ", "", 0, null);
    summary
        .addCodeLine(
            "Ebenfalls sind Zyklen in der Mathematik von grosser Bedeutung, bspw. zur Berechnung einer Faktorisierung.",
            "", 0, null);
    summary.addCodeLine(" ", "", 0, null);
    summary
        .addCodeLine(
            "Der Hase & Igel Algorithmus ist ein leicht zu implementieren sowie sehr effizienter (selbst bei grossen Listen) Ansatz, ",
            "", 0, null);
    summary
        .addCodeLine(
            "zum Auffinden von Zyklen / Schleifen in Listen und besitzt eine Zeitkomplexitaet von O(n) bei einer Platzkomplexitaet von O(1) auf.",
            "", 0, null);
    summary.addCodeLine(" ", "", 0, null);
    summary
        .addCodeLine(
            "Die Iterative Suche mit Merken der besuchten Elemente (auch markieren genannt) ist",
            "", 0, null);
    summary
        .addCodeLine(
            "nicht zufriedenstellend skalierbar und somit keine Alternative zum Hase & Igel Algorithmus bei sehr grossen Listen.",
            "", 0, null);
  }

  /**
   * Creates a graph of the given int[] graph
   */
  public void createGraph() {
    Node[] nodes = new Node[graph.length];
    List<Coordinates> coords = CircleCoords(new Coordinates(600, 150), 100,
        graph.length);
    int[][] adjacencyMatrix = new int[graph.length][graph.length];
    String[] label = new String[graph.length];

    Vertex[] tmpVertices = new Vertex[graph.length];
    for (int i = 0; i < tmpVertices.length; i++) {
      tmpVertices[i] = new Vertex(i + 1);
    }

    Node tmp;
    for (int i = 0; i < graph.length; i++) {
      if (graph[i] != -1) {
        adjacencyMatrix[i][graph[i]] = 1;
        tmpVertices[i].setNext(tmpVertices[graph[i]]);
      }
      tmp = (Node) coords.get(i);
      nodes[i] = tmp;
      label[i] = Integer.toString(i + 1);
    }

    root = tmpVertices[0];
    graphP = lang.newGraph("graph", adjacencyMatrix, nodes, label, null,
        graphProps);
    graphP.hide();
  }

  public class Vertex {
    private int    id;
    private Vertex next;

    public Vertex(int id) {
      this.id = id;
      this.next = null;
    }

    public Vertex(int id, Vertex next) {
      this.id = id;
      this.next = next;
    }

    public Vertex getNext() {
      return next;
    }

    public void setNext(Vertex next) {
      this.next = next;
    }

    public int getId() {
      return this.id;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || this.getClass() != o.getClass())
        return false;

      Vertex other = (Vertex) o;

      if (this.getNext() != other.getNext() || this.id != other.id)
        return false;

      return true;
    }

    @Override
    public String toString() {
      if (this.next == null)
        return this.id + "-> null";
      return this.id + "->" + this.next.getId();
    }
  }
}