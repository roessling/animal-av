package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Thomas Hartmann, Wail Shakir
 * @version 1.0 2013-06-07
 * 
 */
public class KMeans implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private HashMap<String, Primitive> allPrimitives = new HashMap<String, Primitive>();
  private int[][]                    distanz;
  private String[][]                 distanzString;
  private int[]                      minDistanz;
  private ArrayList<Point>           data;
  private ArrayList<Point>           cluster;
  private ArrayList<Point>           newCluster;
  private ArrayList<Color>           clusterColor;
  private TwoValueView               animalCounterView;
  private Language                   lang;
  private int                        anzahlDaten;
  private int                        anzahlCluster;
  private int[][]                    datenKoordinaten;
  private int[][]                    clusterKoordinaten;
  private SourceCodeProperties       pseudoCode;
  private MatrixProperties           distanzMatrix;
  private Variables                  variables;
  private static final Font          BOLDFont      = new Font("SansSerif",
                                                       Font.BOLD, 12);
  private static final Font          NORMALFont    = new Font("SansSerif",
                                                       Font.PLAIN, 12);
  private static final Font          HEADERFont    = new Font("SansSerif",
                                                       Font.BOLD, 16);
  private static final Font          FINISHFont    = new Font("SansSerif",
                                                       Font.BOLD, 14);

  private static final String        Description   = "Ein k-Means-Algorithmus ist ein Verfahren zur Clusteranalyse. Dabei wird aus einer\n "
                                                       + "Menge von ähnlichen Objekten eine vorher bekannte Anzahl von k Gruppen gebildet.\n "
                                                       + "Der Algorithmus ist eine der am häufigsten verwendeten Techniken zur Gruppierung\n "
                                                       + "von Objekten, da er schnell die Zentren der Cluster findet. Der Algorithmus hat\n"
                                                       + "starke Ähnlichkeiten mit dem Expectation-Maximization-Algorithmus und zeichnet\n"
                                                       + "sich durch seine Einfachheit aus. Erweiterungen sind der k-Median-Algorithmus\n"
                                                       + "und der k-Means++ Algorithmus.";

  private static final String        PSEUDOTEXT    = "1. Die k Cluster-Schwerpunkte werden zufällig verteilt. \n"
                                                       + "2. Jedes Objekt wird demjenigen Cluster zugeordnet, dessen Schwerpunkt ihm am nächsten liegt. \n"
                                                       + "#Berechnung des euklidischen Abstands \n"
                                                       + "#Zuordnung \n"
                                                       + "3. Für jeden Cluster wird der Schwerpunkt neu berechnet. \n"
                                                       + "4. Basierend auf den neu berechneten Zentren werden die Objekte wieder wie in Schritt 2 auf die Cluster verteilt, bis \n"
                                                       + "#eine festgelegte maximale Iterationstiefe erreicht wurde oder \n"
                                                       + "#sich die Schwerpunkte nicht mehr bewegen, d. h. bei der Neuverteilung kein Objekt einem anderen Cluster zugeordnet wurde. \n";
  private static final String        CONCLUSION    = "k-Means ist ein leistungsfähiger Algorithmus, jedoch nicht ohne Schwachstellen. \n"
                                                       + "Ein k-Means-Algorithmus muss nicht die beste mögliche Lösung finden. \n"
                                                       + "Die gefundene Lösung hängt stark von den gewählten Startpunkten ab. Der einfachste Ansatz ist,"
                                                       + "den Algorithmus mehrmals hintereinander mit verschiedenen Startwerten zu starten und die beste Lösung zu nehmen. \n"
                                                       + " \n"
                                                       + "Ein weiterer Nachteil ist, dass die Anzahl der Clusterzentren k im Voraus gewählt wird. \n"
                                                       + "Bei Verwendung eines ungeeigneten k können sich komplett andere, unter Umständen unintuitive Lösungen ergeben. \n"

                                                       + "Bei einem 'falschen' k kann kein gutes Clustering erfolgen. Die Lösung ist, verschiedene Werte für k zu probieren  \n"
                                                       + "und dann ein geeignetes zu wählen, zum Beispiel mit Hilfe des Silhouettenkoeffizient, oder durch Vergleich der verschiedenen Clusteringkosten. \n";

  private static final String        TEXTFINISH    = "Iterationsende: Keine Veränderung der Clusterschwerpunkte mehr!";
  private static final String        DISTANZTEXT   = "Distanz Matrix mit Cluster C und Objekte D";
  private Text                       feldbase;

  public void init() {
    lang = new AnimalScript("K-Means [DE]", "Thomas Hartmann,Wail Shakir", 960,
        800);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    boolean randomCD = (Boolean) primitives
        .get("zufallsgenerierte Daten und Cluster");
    this.anzahlDaten = (Integer) primitives
        .get("zufallsgenerierte Datenanzahl");
    this.anzahlCluster = (Integer) primitives
        .get("zufallsgenerierte Clusteranzahl");
    this.datenKoordinaten = (int[][]) primitives.get("Daten Koordinaten");
    this.clusterKoordinaten = (int[][]) primitives.get("Cluster Koordinaten");
    this.pseudoCode = (SourceCodeProperties) props
        .getPropertiesByName("pseudoCode");
    this.distanzMatrix = (MatrixProperties) props
        .getPropertiesByName("distanzMatrix");

    init();

    this.data = new ArrayList<Point>();
    this.cluster = new ArrayList<Point>();
    this.clusterColor = new ArrayList<Color>();
    this.newCluster = new ArrayList<Point>();

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    lang.setStepMode(true);
    checkEnter(randomCD);
    create();

    lang.finalizeGeneration();
    return lang.toString();
  }

  private void checkEnter(boolean randomCD) {
    boolean data = false;
    boolean cluster = false;
    if (randomCD) {
      createPoints(this.cluster, this.anzahlCluster);
      createPoints(this.data, this.anzahlDaten);
    } else {
      int x = 0;
      int y = 0;
      if (this.datenKoordinaten.length > 0) {
        if (this.datenKoordinaten[0].length < 2) {
          x += 200;
          y += 200;
          data = true;
          this.anzahlDaten = this.datenKoordinaten.length;
          createPoints(this.data, this.datenKoordinaten.length);
          createTextWithBox(
              "errorDMessage",
              "Ihre Daten Matrix beinhält nur x-Werte. Diese wurde nun zufallsgenerierte.",
              new Coordinates(x, y), true, Color.red);

        } else if (this.datenKoordinaten[0].length >= 2) {
          matrix2D(this.datenKoordinaten, this.data);
        }
      }
      if (this.clusterKoordinaten.length > 0) {
        if (this.clusterKoordinaten[0].length < 2) {
          this.anzahlCluster = this.clusterKoordinaten.length;
          cluster = true;
          createPoints(this.cluster, this.clusterKoordinaten.length);
          createTextWithBox(
              "errorCMessage",
              "Ihre Cluster Matrix beinhält nur x-Werte. Diese wurde nun zufallsgenerierte.",
              new Coordinates(x, y + 50), true, Color.red);

        } else if (this.clusterKoordinaten[0].length >= 2) {
          matrix2D(this.clusterKoordinaten, this.cluster);
        }
      }
      if (data || cluster) {
        lang.nextStep();
        if (data) {

          this.allPrimitives.get("errorDMessage").hide();
          this.allPrimitives.get("errorDMessageHRect").hide();
        }
        if (cluster) {
          this.allPrimitives.get("errorCMessageHRect").hide();
          this.allPrimitives.get("errorCMessage").hide();
        }
      }

    }

  }

  private void matrix2D(int[][] matrix, ArrayList<Point> list) {
    for (int i = 0; i < matrix.length; i++) {
      list.add(new Point(matrix[i][0], matrix[i][1]));
    }
  }

  public String getName() {
    return "K-Means [DE]";
  }

  public String getAlgorithmName() {
    return "K-Means";
  }

  public String getAnimationAuthor() {
    return "Thomas Hartmann, Wail Shakir";
  }

  public String getDescription() {
    return "Ein k-Means-Algorithmus ist ein Verfahren zur Clusteranalyse. Dabei wird aus einer Menge von &auml;hnlichen Objekten eine vorher bekannte Anzahl von k Gruppen gebildet.<br />"
        + "\n"
        + "Der Algorithmus ist eine der am h&auml;ufigsten verwendeten Techniken zur Gruppierung von Objekten, da er schnell die Zentren der Cluster findet.<br />"
        + "\n"
        + "Der Algorithmus hat starke &Auml;hnlichkeiten mit dem Expectation-Maximization-Algorithmus und zeichnet sich durch seine Einfachheit aus.<br />"
        + "\n"
        + "Erweiterungen sind der k-Median-Algorithmus und der k-Means++ Algorithmus.";
  }

  public String getCodeExample() {
    return "1. Die k Cluster-Schwerpunkte werden zufällig verteilt."
        + "\n"
        + "2. Jedes Objekt wird demjenigen Cluster zugeordnet, dessen Schwerpunkt ihm am nächsten liegt."
        + "\n"
        + "\t Berechnung des euklidischen Abstands"
        + "\n"
        + "\t Zuordnung"
        + "\n"
        + "3. Für jeden Cluster wird der Schwerpunkt neu berechnet."
        + "\n"
        + "4. Basierend auf den neu berechneten Zentren werden die Objekte wieder wie in Schritt 2 auf die Cluster verteilt, bis"
        + "\n"
        + "\t eine festgelegte maximale Iterationstiefe erreicht wurde oder \n"
        + "\t sich die Schwerpunkte nicht mehr bewegen, d. h. bei der Neuverteilung kein Objekt einem anderen Cluster zugeordnet wurde.";
  }

  protected String getAlgorithmCode() {
    return PSEUDOTEXT;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * Default constructor
   */
  public KMeans() {

  }

  private void createPoints(ArrayList<Point> points, int counter) {
    for (int i = 0; i < counter; i++) {
      points.add(new Point((int) (Math.random() * 580) + 30, (int) (Math
          .random() * 420) + 30));

    }
  }

  public void create() {
    this.variables = lang.newVariables();
    for (int i = 0; i < cluster.size(); i++) {
      clusterColor.add(new Color((2000 + i * 5555000) % 16777215));
    }
    createHeader();

    createIntroduction();
    lang.nextStep("Einleitung");

    hideIntroduction();

    createPseudoCode();
    lang.nextStep("PseudoCode");

    createFeldbase();

    createDataObjects();
    lang.nextStep("Daten erstellen");
    highlightPseudoCode(0);
    createClusterCenter();

    createClusterCenterTable(700, 260);
    lang.nextStep("Cluster erstellen");
    unhighlightPseudoCode(0);
    highlightPseudoCode(1);
    setDistanzString();
    createDistanceMatrix(new Coordinates(700, 550));
    createAnimalCounter(new Coordinates(850, 260));
    lang.nextStep("Distanz Matrix erstellen");

    int counter = 0;
    boolean b = true;
    do {

      unhighlightPseudoCode(5);
      unhighlightPseudoCode(6);
      unhighlightPseudoCode(7);
      highlightPseudoCode(1);
      highlightPseudoCode(2);
      calculateNewCluster();
      updateDistanceMatrix(false);
      if (counter == 0) {
        MultipleChoiceQuestionModel mcQuestion = new MultipleChoiceQuestionModel(
            "clusterAbfrage");
        mcQuestion.setPrompt("Welchem Cluster wird D1 zugeordnet?");
        for (int i = 0; i < this.cluster.size(); i++) {
          mcQuestion.addAnswer("C" + (i + 1),
              (i == this.minDistanz[0]) ? 1 : 0,
              (i == this.minDistanz[0]) ? "Richtig!" : "Leider falsch!");
        }
        lang.addMCQuestion(mcQuestion);
      }
      lang.nextStep("Iteration: " + (counter + 1));
      unhighlightPseudoCode(2);
      highlightPseudoCode(3);

      for (int i = 0; i < this.minDistanz.length; i++) {
        highlightMinDistanz(this.minDistanz[i] + 1, i + 1);
        colorizeData(i, clusterColor.get(this.minDistanz[i]));
        connectClusterWithData(counter, this.minDistanz[i], i);
        lang.nextStep();

      }
      unhighlightPseudoCode(1);
      unhighlightPseudoCode(3);
      highlightPseudoCode(4);
      b = sameClusterPosition();
      this.cluster = this.newCluster;
      for (int i = 0; i < this.cluster.size(); i++) {
        if (i > 0)
          unhighlightClusterPositionText(i - 1);
        moveCluster(i, this.cluster.get(i).x, this.cluster.get(i).y);
        for (int j = 0; j < this.minDistanz.length; j++) {
          if (this.minDistanz[j] == i) {
            disconnectLine(counter, this.minDistanz[j], j);
            unhighlightMinDistanz(this.minDistanz[j] + 1, j + 1);
          }
        }
        // gibt aktuelle postionen der cluster in der Tabelle aus
        changeClusterPositionText(i, this.cluster.get(i).x,
            this.cluster.get(i).y);
        highlightClusterPositionText(i);
        this.variables.set("cluster" + (i + 1), " {" + this.cluster.get(i).x
            + "," + this.cluster.get(i).y + "}");
        lang.nextStep();

      }
      unhighlightClusterPositionText(this.cluster.size() - 1);
      unhighlightPseudoCode(4);
      highlightPseudoCode(5);
      highlightPseudoCode(6);
      highlightPseudoCode(7);
      if (!b) {
        setDistanzString();
        updateDistanceMatrix(true);
        for (int i = 0; i < this.data.size(); i++) {
          colorizeData(i, Color.gray);
        }

      }
      lang.nextStep();
      counter++;
    } while (!b);
    textFinish();

    TrueFalseQuestionModel tfQuestion = new TrueFalseQuestionModel(
        "immerBesteLoesung");
    tfQuestion
        .setPrompt("Findet der K-Means Algorithmus immer die beste mögliche Lösung?");
    tfQuestion.setCorrectAnswer(false);
    tfQuestion.setPointsPossible(1);
    lang.addTFQuestion(tfQuestion);

    lang.nextStep();

    hideAll();

    createConclusion();

    lang.nextStep("Schluss");
  }

  private void createAnimalCounter(Coordinates coordinates) {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, HEADERFont);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Text text = lang.newText(coordinates, "Matrix Counter", "matrixCounter",
        null, textProperties);
    this.allPrimitives.put(text.getName(), text);
    TwoValueCounter counter = lang.newCounter((StringMatrix) this.allPrimitives
        .get("distanzMatrix"));
    CounterProperties cp = new CounterProperties();

    this.animalCounterView = lang.newCounterView(counter, new Coordinates(
        coordinates.getX(), coordinates.getY() + 20), cp, true, false);

  }

  private void createTextWithBox(String name, String title,
      Coordinates coordinates, boolean putPrimitive, Color bgColor) {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, HEADERFont);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Text text = lang.newText(coordinates, title, name, null, textProperties);

    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, bgColor);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect rect = lang.newRect(
        new Offset(-5, -5, text, AnimalScript.DIRECTION_NW), new Offset(5, 5,
            text, AnimalScript.DIRECTION_SE), name + "HRect", null,
        rectProperties);

    if (putPrimitive) {
      this.allPrimitives.put(text.getName(), text);
      this.allPrimitives.put(rect.getName(), rect);
    }
  }

  private void createHeader() {
    createTextWithBox("header", "K-Means Algorithmus", new Coordinates(20, 30),
        false, Color.lightGray);
  }

  private SourceCode createSourceCode(Coordinates coordinates, String name,
      String text, SourceCodeProperties scProperties) {
    SourceCodeProperties properties = scProperties;
    if (properties == null) {
      properties = new SourceCodeProperties();
      properties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      properties.set(AnimationPropertiesKeys.FONT_PROPERTY, NORMALFont);

      properties
          .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    }
    SourceCode code = lang.newSourceCode(coordinates, name, null, properties);
    String[] splitText = text.split("\n");

    for (int i = 0; i < splitText.length; i++) {
      String str = splitText[i];
      int index = str.indexOf("#");
      String[] splitText2 = str.split("#");
      code.addCodeLine(index == -1 ? splitText2[0] : splitText2[1], null,
          index == -1 ? 2 : 4, null);
    }

    return code;
  }

  private void createIntroduction() {
    SourceCode code = createSourceCode(new Coordinates(5, 50),
        "introductionText", KMeans.Description, null);
    this.allPrimitives.put(code.getName(), code);
  }

  private void hideIntroduction() {
    this.allPrimitives.get("introductionText").hide();
  }

  private void createPseudoCode() {
    createTextWithBox("pseudoCodeheader", "Pseudo-Code",
        new Coordinates(20, 70), true, Color.WHITE);
    SourceCode code = createSourceCode(new Coordinates(20, 90), "pseudoCode",
        KMeans.PSEUDOTEXT, this.pseudoCode);
    this.allPrimitives.put(code.getName(), code);
  }

  private void highlightPseudoCode(int lineNumber) {
    SourceCode code = (SourceCode) this.allPrimitives.get("pseudoCode");
    code.highlight(lineNumber);
  }

  private void unhighlightPseudoCode(int lineNumber) {
    SourceCode code = (SourceCode) this.allPrimitives.get("pseudoCode");
    code.unhighlight(lineNumber);
  }

  private void createFeldbase() {
    // text "feldbase" "" (10,250) font SansSerif size 14
    this.feldbase = lang.newText(new Coordinates(10, 250), "", "feldbase",
        null, new TextProperties());
    // rectangle "feld" offset (0, 0) from "feldbase" SW offset (640, 480)
    // from "feldbase" SW color (0, 0, 0) depth 5
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
    Rect feld = lang.newRect(new Offset(0, 0, feldbase,
        AnimalScript.DIRECTION_SW), new Offset(640, 480, this.feldbase,
        AnimalScript.DIRECTION_SW), "feld", null, rectProperties);
    allPrimitives.put(feldbase.getName(), feldbase);
    allPrimitives.put(feld.getName(), feld);
  }

  private void createDataObjects() {
    for (int i = 0; i < data.size(); i++) {
      SquareProperties squareProperties = new SquareProperties();
      squareProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
      squareProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      squareProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
      Square dataSquare = lang.newSquare(new Offset(data.get(i).x,
          data.get(i).y, feldbase, AnimalScript.DIRECTION_SW), 30, "data"
          + (i + 1), null, squareProperties);
      allPrimitives.put(dataSquare.getName(), dataSquare);
      TextProperties datatextproperties = new TextProperties();
      datatextproperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.WHITE);
      datatextproperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
      Text dataText = lang.newText(new Offset(data.get(i).x + 5,
          data.get(i).y + 6, feldbase, AnimalScript.DIRECTION_SW), "D"
          + (i + 1), "dataText" + (i + 1), null, datatextproperties);
      allPrimitives.put(dataText.getName(), dataText);

    }
  }

  private void createClusterCenter() {
    for (int i = 0; i < cluster.size(); i++) {
      this.variables.declare("String", "cluster" + (i + 1));
      CircleProperties circleProperties = new CircleProperties();
      circleProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
          clusterColor.get(i));
      circleProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      circleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
      Circle clusterCircle = lang.newCircle(new Offset(cluster.get(i).x + 15,
          cluster.get(i).y + 15, feldbase, AnimalScript.DIRECTION_SW), 15,
          "cluster" + (i + 1), null, circleProperties);
      allPrimitives.put(clusterCircle.getName(), clusterCircle);
      TextProperties clustertextproperties = new TextProperties();
      clustertextproperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.WHITE);
      clustertextproperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
      Text clusterText = lang.newText(
          new Offset(cluster.get(i).x + 6, cluster.get(i).y + 7, feldbase,
              AnimalScript.DIRECTION_SW), "C" + (i + 1), "clusterText"
              + (i + 1), null, clustertextproperties);
      allPrimitives.put(clusterText.getName(), clusterText);
      this.variables.set("cluster" + (i + 1), " {" + this.cluster.get(i).x
          + "," + this.cluster.get(i).y + "}");
    }
  }

  private void createClusterCenterTable(int xPos, int yPos) {
    Text clusterTableText = lang.newText(new Coordinates(xPos, yPos),
        "Cluster", "cluster_TableText", null, new TextProperties());
    allPrimitives.put(clusterTableText.getName(), clusterTableText);
    PolylineProperties plineProperties = new PolylineProperties();
    Polyline line = lang.newPolyline(new Offset[] {
        new Offset(0, 5, clusterTableText, AnimalScript.DIRECTION_SW),
        new Offset(115, 5, clusterTableText, AnimalScript.DIRECTION_SW) },
        "cluster_TableHeadLine", null, plineProperties);
    allPrimitives.put(line.getName(), line);
    line = lang.newPolyline(
        new Offset[] {
            new Offset(0, 10 + 20 * cluster.size(), clusterTableText,
                AnimalScript.DIRECTION_SW),
            new Offset(115, 10 + 20 * cluster.size(), clusterTableText,
                AnimalScript.DIRECTION_SW) }, "cluster_TableFootLine", null,
        plineProperties);
    allPrimitives.put(line.getName(), line);
    Text text = lang.newText(new Offset(25, 0, clusterTableText,
        AnimalScript.DIRECTION_NE), "x", "cluster_TableTextX", null,
        new TextProperties());
    allPrimitives.put(text.getName(), text);
    text = lang.newText(new Offset(55, 0, clusterTableText,
        AnimalScript.DIRECTION_NE), "y", "cluster_TableTextY", null,
        new TextProperties());
    allPrimitives.put(text.getName(), text);
    line = lang.newPolyline(new Offset[] {
        new Offset(5, 0, clusterTableText, AnimalScript.DIRECTION_NE),
        new Offset(5, 35 + cluster.size() * 20, clusterTableText,
            AnimalScript.DIRECTION_NE) }, "cluster_TableLeftLine", null,
        plineProperties);
    allPrimitives.put(line.getName(), line);
    for (int i = 0; i < cluster.size(); i++) {
      text = lang.newText(new Offset(-25, 10 + i * 20, clusterTableText,
          AnimalScript.DIRECTION_SE), (i + 1) + "", "ClusterNr" + (i + 1),
          null, new TextProperties());
      allPrimitives.put(text.getName(), text);
      Text xText = lang.newText(new Offset(15, 12 + i * 20, clusterTableText,
          AnimalScript.DIRECTION_SE), cluster.get(i).x + "", "cluster0:" + i,
          null, new TextProperties());
      allPrimitives.put(xText.getName(), xText);
      Text yText = lang.newText(new Offset(48, 12 + i * 20, clusterTableText,
          AnimalScript.DIRECTION_SE), cluster.get(i).y + "", "cluster1:" + i,
          null, new TextProperties());
      allPrimitives.put(yText.getName(), yText);
    }
  }

  private void changeClusterPositionText(int clusterNumber, int newX, int newY) {
    Text xText = (Text) allPrimitives.get("cluster0:" + clusterNumber);
    xText.setText(newX + "", new TicksTiming(0), new TicksTiming(10));
    Text yText = (Text) allPrimitives.get("cluster1:" + clusterNumber);
    yText.setText(newY + "", new TicksTiming(0), new TicksTiming(10));
  }

  private void highlightClusterPositionText(int clusterNumber) {
    Text xText = (Text) allPrimitives.get("cluster0:" + clusterNumber);
    Text yText = (Text) allPrimitives.get("cluster1:" + clusterNumber);
    xText.setFont(BOLDFont, new TicksTiming(0), new TicksTiming(10));
    yText.setFont(BOLDFont, new TicksTiming(0), new TicksTiming(10));
  }

  private void unhighlightClusterPositionText(int clusterNumber) {
    Text xText = (Text) allPrimitives.get("cluster0:" + clusterNumber);
    Text yText = (Text) allPrimitives.get("cluster1:" + clusterNumber);
    xText.setFont(NORMALFont, new TicksTiming(0), new TicksTiming(10));
    yText.setFont(NORMALFont, new TicksTiming(0), new TicksTiming(10));
  }

  private void connectClusterWithData(int counter, int clusterNumber,
      int dataNumber) {

    Point clusterPoint = cluster.get(clusterNumber);
    PolylineProperties plineProperties = new PolylineProperties();
    plineProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 6);
    Polyline line = lang.newPolyline(
        new Offset[] {
            new Offset(clusterPoint.x + 14, clusterPoint.y + 15, feldbase,
                AnimalScript.DIRECTION_SW),
            new Offset(0, 15, allPrimitives.get("data" + (dataNumber + 1)),
                AnimalScript.DIRECTION_N) }, "line" + counter + "_"
            + clusterNumber + "_" + dataNumber, null, plineProperties);

    allPrimitives.put(line.getName(), line);
  }

  private void disconnectLine(int counter, int clusterNumber, int dataNumber) {
    String key = "line" + counter + "_" + clusterNumber + "_" + dataNumber;
    Polyline line = (Polyline) allPrimitives.get(key);
    line.hide();

    allPrimitives.remove(key);
  }

  private void colorizeData(int dataNumber, Color color) {
    Square data = (Square) allPrimitives.get("data" + +(dataNumber + 1));
    data.changeColor("fillColor", color, new TicksTiming(0),
        new TicksTiming(10));
  }

  private void moveCluster(int clusterNumber, int xPos, int yPos) {
    // move "cluster3" to offset (360, 105) from "feldbase" SW
    Circle clusterCircle = (Circle) allPrimitives.get("cluster"
        + (clusterNumber + 1));
    Text clusterText = (Text) allPrimitives.get("clusterText"
        + (clusterNumber + 1));
    try {
      clusterCircle.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(xPos,
          yPos, feldbase, AnimalScript.DIRECTION_SW), new TicksTiming(0),
          new TicksTiming(10));
      clusterText.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(xPos + 6,
          yPos + 7, feldbase, AnimalScript.DIRECTION_SW), new TicksTiming(0),
          new TicksTiming(10));
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
  }

  private void createDistanceMatrix(Coordinates coordinates) {

    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, HEADERFont);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Text text = lang.newText(
        new Coordinates(coordinates.getX(), coordinates.getY() - 20),
        DISTANZTEXT, "distanzText", null, textProperties);
    this.allPrimitives.put(text.getName(), text);
    if (this.distanzMatrix == null) {
      this.distanzMatrix = new MatrixProperties();
      this.distanzMatrix.set(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.black);
      this.distanzMatrix.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
          Color.red);
      this.distanzMatrix.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
          Color.red);
    }

    StringMatrix matrix = lang.newStringMatrix(coordinates, this.distanzString,
        "distanzMatrix", null, this.distanzMatrix);

    this.allPrimitives.put(matrix.getName(), matrix);
  }

  private int[][] calculateDistanceMatrix() {
    int[][] distanz = new int[this.cluster.size()][this.data.size()];

    for (int i = 0; i < this.cluster.size(); i++) {

      Point cluster = this.cluster.get(i);
      for (int j = 0; j < this.data.size(); j++) {
        Point point = this.data.get(j);

        distanz[i][j] = (int) Math.round(Math.sqrt((Math.pow(cluster.getX()
            - point.getX(), 2) + Math.pow(cluster.getY() - point.getY(), 2))));
      }
    }
    return distanz;
  }

  private void calculateNewCluster() {
    this.distanz = calculateDistanceMatrix();
    minDistanz(this.distanz);
    int[] cluster = new int[this.cluster.size()];
    this.newCluster = new ArrayList<Point>();
    for (int i = 0; i < cluster.length; i++) {
      cluster[i] = 0;
      this.newCluster.add(new Point(0, 0));
    }
    for (int i = 0; i < this.minDistanz.length; i++) {
      Point point = this.newCluster.get(this.minDistanz[i]);
      int x = (int) (point.getX() + this.data.get(i).getX());
      int y = (int) (point.getY() + this.data.get(i).getY());
      cluster[this.minDistanz[i]] = cluster[this.minDistanz[i]] + 1;

      this.newCluster.set(this.minDistanz[i], new Point(x, y));
    }
    for (int i = 0; i < this.newCluster.size(); i++) {
      if (cluster[i] != 0) {
        int x = this.newCluster.get(i).x / cluster[i];
        int y = this.newCluster.get(i).y / cluster[i];
        this.newCluster.set(i, new Point(x, y));
      } else {
        this.newCluster.set(i, (Point) this.cluster.get(i).clone());
      }
    }
  }

  private void minDistanz(int[][] distanz) {
    this.minDistanz = new int[distanz[0].length];
    StringMatrix matrix = (StringMatrix) this.allPrimitives
        .get("distanzMatrix");
    for (int j = 0; j < distanz[0].length; j++) {
      int min = Integer.MAX_VALUE;
      for (int i = 0; i < distanz.length; i++) {
        int oldMin = min;
        min = Math.min(min, distanz[i][j]);
        matrix.getElement(i + 1, j + 1);
        if (min != oldMin) {
          this.minDistanz[j] = i;
        }
      }
    }
  }

  private boolean sameClusterPosition() {
    boolean bool = true;

    for (int i = 0; i < this.cluster.size(); i++) {

      if (this.cluster.get(i).x != this.newCluster.get(i).x
          || this.cluster.get(i).y != this.newCluster.get(i).y) {
        bool = false;
        break;
      }
    }
    return bool;
  }

  private void setDistanzString() {
    // { "C/D", "D1", "D2" }, { "C1", "-", "-" },
    // { "C2", "-", "-" }, { "C3", "-", "-" } };

    this.distanzString = new String[this.cluster.size() + 1][this.data.size() + 1];
    for (int i = 0; i < this.distanzString.length; i++) {
      for (int j = 0; j < this.distanzString[i].length; j++) {
        if (i == 0 && j == 0)
          this.distanzString[i][j] = "C/D";
        else if (i == 0 && j != 0)
          this.distanzString[i][j] = "D" + j;
        else if (i != 0 && j == 0)
          this.distanzString[i][j] = "C" + i;
        else
          this.distanzString[i][j] = "-";
      }
    }
  }

  private void updateDistanceMatrix(boolean clear) {

    StringMatrix matrix = (StringMatrix) this.allPrimitives
        .get("distanzMatrix");

    for (int i = 1; i <= this.cluster.size(); i++) {
      for (int j = 1; j <= this.data.size(); j++) {
        if (!clear)
          this.distanzString[i][j] = "" + this.distanz[i - 1][j - 1];
        matrix.put(i, j, this.distanzString[i][j], null, null);

      }
    }
  }

  private void highlightMinDistanz(int indexCluster, int indexData) {
    StringMatrix matrix = (StringMatrix) this.allPrimitives
        .get("distanzMatrix");
    matrix.highlightElem(indexCluster, indexData, new TicksTiming(0),
        new TicksTiming(0));
  }

  private void unhighlightMinDistanz(int indexCluster, int indexData) {
    StringMatrix matrix = (StringMatrix) this.allPrimitives
        .get("distanzMatrix");
    matrix.unhighlightElem(indexCluster, indexData, new TicksTiming(0),
        new TicksTiming(0));
  }

  private void hideAll() {
    Iterator<Map.Entry<String, Primitive>> it = allPrimitives.entrySet()
        .iterator();
    while (it.hasNext()) {
      Map.Entry<String, Primitive> pairs = it.next();
      Primitive element = pairs.getValue();
      element.hide();
      it.remove();
    }
    this.animalCounterView.hide();
  }

  private void textFinish() {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, FINISHFont);
    Text text = lang.newText(new Offset(40, 10, feldbase,
        AnimalScript.DIRECTION_SW), TEXTFINISH, "textFinish", null,
        textProperties);
    this.allPrimitives.put(text.getName(), text);
  }

  private void createConclusion() {
    SourceCode code = createSourceCode(new Coordinates(20, 110),
        "ConclusionText", KMeans.CONCLUSION, null);
    this.allPrimitives.put(code.getName(), code);
  }

}