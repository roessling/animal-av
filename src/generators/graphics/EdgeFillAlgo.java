package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;

//import java.awt.Color;
//import java.awt.Font;
import java.awt.geom.Point2D;
//import java.awt.geom.Point2D.Float;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
//import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;

//import generators.framework.Generator; 
//import generators.framework.GeneratorType;

import algoanim.animalscript.AnimalScript;

public class EdgeFillAlgo implements Generator {

  private Language             lang;
  private DisplayOptions       defaultDisOp;
  private PolylineProperties   coordinatensystem    = new PolylineProperties();
  private PolylineProperties   lineHighlight        = new PolylineProperties();
  private PolylineProperties   lineStandart         = new PolylineProperties();
  private TextProperties       description          = new TextProperties();
  private TextProperties       header               = new TextProperties();
  private SourceCodeProperties pseudoCodeProperties = new SourceCodeProperties();
  private SourceCode           PseudoCode;
  private SourceCode           Spalte1;
  private CircleProperties     circleHighlight      = new CircleProperties();
  private CircleProperties     circleStandart       = new CircleProperties();
  private int[][]              input;
  private boolean[][]          currentState;

  private static final String  Description1         = "Der gr\u00F6\u00DFte Nachteil der Kantenlisten-Algorithmen ist der Aufwand zur Sortierung und Manipulation";
  private static final String  Description2         = "der Listen. Der sehr einfache Edge-fill-Algorithmus kommt ohne diesen Aufwand aus. Beim ";
  private static final String  Description3         = "Edge-fill-Algorithmus werden f\u00FCr jede Bildzeile, die beim x-Schnittpunkt eine Polygonkante schneidet, alle ";
  private static final String  Description4         = "Pixel dieser Bildzeile mit einer x-Koordinate strikt gr\u00F6\u00DFer als x-Schnittpunkt + 0,5 invertiert. 'Invertierung' ";
  private static final String  Description5         = "bedeutet hier, dass eingef\u00E4rbte Pixel in den Ausgangszustand zur\u00FCckgesetzt werden und umgekehrt.";
  private static final String  Description6         = "Die Reihenfolge, in der die Polygonkanten abgearbeitet werden, ist beliebig.";

  private void initCurrentStateArray() {
    currentState = new boolean[9][10];
    for (int i = 0; i < currentState.length; i++) {
      for (int j = 0; j < currentState[i].length; j++) {
        currentState[i][j] = false;
      }
    }

  }

  public void init() {
    lang = new AnimalScript("EdgeFillAlgorithmus Animation",
        "Alvar Gamez Zerban, Axel Heimann, Nikos Kombouris", 1280, 720);
    lang.setStepMode(true);
  }

  public void showDescription() {
    lang.newText(new Coordinates(20, 30), "Edge Fill Algorithmus", "header",
        defaultDisOp, header);
    lang.newText(new Offset(0, 15, "header", "SW"), Description1,
        "Description1", defaultDisOp, description);
    lang.newText(new Offset(0, 5, "Description1", "SW"), Description2,
        "Description2", defaultDisOp, description);
    lang.newText(new Offset(0, 5, "Description2", "SW"), Description3,
        "Description3", defaultDisOp, description);
    lang.newText(new Offset(0, 5, "Description3", "SW"), Description4,
        "Description4", defaultDisOp, description);
    lang.newText(new Offset(0, 5, "Description4", "SW"), Description5,
        "Description5", defaultDisOp, description);
    lang.newText(new Offset(0, 5, "Description5", "SW"), Description6,
        "Description6", defaultDisOp, description);
    lang.nextStep("Koordinatensystem anzeigen");
  }

  public void drawCartesian() {
    int x, y;
    for (x = 700; x < 1151; x = x + 50) {
      Node[] nodes = new Node[] { new Coordinates(x, 25),
          new Coordinates(x, 425) };
      lang.newPolyline(nodes, "x" + x, defaultDisOp, coordinatensystem);
    }
    for (y = 425; y > 24; y = y - 50) {
      Node[] nodes = new Node[] { new Coordinates(700, y),
          new Coordinates(1150, y) };
      lang.newPolyline(nodes, "y" + y, defaultDisOp, coordinatensystem);
    }
    for (x = 0; x < 10; x++) {
      Offset OffsetX = new Offset(-3, 25, "x" + (700 + 50 * x), "S");
      lang.newText(OffsetX, "" + x, "tx" + x, defaultDisOp);
    }
    for (y = 1; y < 9; y++) {
      Offset OffsetY = new Offset(-35, -8, "y" + (425 - 50 * y), "W");
      lang.newText(OffsetY, "" + y, "tx" + y, defaultDisOp);
    }
  }

  public void rasterisiere(int[][] input) {
    lang.nextStep();
    Spalte1 = lang.newSourceCode(new Offset(0, 15, "Description6", "SW"),
        "Spalte1", defaultDisOp, pseudoCodeProperties);
    Spalte1.addCodeLine("Kante", null, 0, null);
    for (int i = 0; i < input.length; i++) {
      drawPolygon(i, input);
      highlightSpalte1(i, input);
      lang.nextStep("Kante " + i + " anzeigen");
    }
    drawLine("Kante" + (input.length - 1), input[input.length - 1][0],
        input[input.length - 1][1], input[input.length - 1][2],
        input[input.length - 1][3], lineStandart);
    Spalte1.unhighlight(input.length);
  }

  private String line2DToString(int[] line) {
    return "(" + line[0] + ", " + line[1] + ") bis (" + line[2] + ", "
        + line[3] + ")";
  }

  private void drawLine(String name, int x1, int y1, int x2, int y2,
      PolylineProperties farbe) {
    Coordinates x1y1 = new Coordinates(x1 * 50 + 700, 425 - y1 * 50);
    Coordinates x2y2 = new Coordinates(x2 * 50 + 700, 425 - y2 * 50);
    Node[] nodes = new Node[] { x1y1, x2y2 };
    lang.newPolyline(nodes, name, defaultDisOp, farbe);
  }

  public void drawPolygon(int i, int[][] input) {
    drawLine("Kante" + i, input[i][0], input[i][1], input[i][2], input[i][3],
        lineHighlight);
    if (i > 0) {
      drawLine("Kante" + (i - 1), input[i - 1][0], input[i - 1][1],
          input[i - 1][2], input[i - 1][3], lineStandart);
    }
  }

  private void highlightSpalte1(int i, int[][] input) {
    Spalte1.addCodeLine(line2DToString(input[i]), null, 0, null);
    Spalte1.highlight(i + 1);
    Spalte1.unhighlight(i);
  }

  public void showSourceCode() {
    PseudoCode = lang.newSourceCode(new Offset(0, 150, "Description6", "SW"),
        "PseudoCode", defaultDisOp, pseudoCodeProperties);
    PseudoCode.addCodeLine(
        "1. Linie betrachten (Traversierung gegen den Uhrzeigersinn).", null,
        0, null);
    PseudoCode.addCodeLine(
        "2. Pixel mit gr\u00F6\u00DFerer X-Koordinate invertieren.", null, 0,
        null);
    lang.nextStep("Initialisiere Pixel");
  }

  public void initPixels() {
    for (int x = 700; x < 1151; x = x + 50)
      for (int y = 425; y > 24; y = y - 50) {
        lang.newCircle(new Coordinates(x, y), 20, "Pixel" + ((425 - y) / 50)
            + 6 * ((x / 50) - 15), defaultDisOp, circleStandart);
      }
  }

  private void calcIntersection(int[][] input) {
    PseudoCode.highlight(0);
    for (int i = 0; i < input.length; i++) {
      drawLine("Kante" + i, input[i][0], input[i][1], input[i][2], input[i][3],
          lineHighlight);
      if (i > 0) {
        drawLine("Kante" + (i - 1), input[i - 1][0], input[i - 1][1],
            input[i - 1][2], input[i - 1][3], lineStandart);
      }
      lang.nextStep("Kante " + i + " betrachten");
      if (input[i][1] == input[i][3]) {
        // horizontal
      } else {
        if (input[i][0] == input[i][2]) {
          senkrecht(input[i]);
        } else {
          float gradient = ((input[i][3] - input[i][1]) / (input[i][2] - input[i][0])); // ((y2-y1)/(x2-x1))
          if (gradient > 0) {
            if (input[i][0] > input[i][2]) {
              // 3.Quadrant
              quadrant3(input[i]);
            } else {
              // 1.Quadrant
              quadrant1(input[i]);
            }
          } else {
            if (input[i][0] > input[i][2]) {
              // 2.Quadrant
              quadrant2(input[i]);
            } else {
              // 4.Quadrant
              quadrant4(input[i]);
            }
          }
        }
      }
      drawPolygon(i, input);
      lang.nextStep();
    }
    PseudoCode.unhighlight(0);
    drawLine("Kante" + (input.length - 1), input[input.length - 1][0],
        input[input.length - 1][1], input[input.length - 1][2],
        input[input.length - 1][3], lineStandart);
  }

  private void senkrecht(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float x1 = input[0];
    if (input[1] < input[3]) {
      for (float y = input[1]; y < input[3]; y++) {
        intersections.add(new Point2D.Float(x1, y));
      }
    } else {
      for (float y = input[1] - 1; y >= input[3]; y--) {
        intersections.add(new Point2D.Float(x1, y));
      }
    }
    invertPixels(intersections);
  }

  private void quadrant1(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float gradient = ((input[3] - input[1]) / (input[2] - input[0])); // ((y2-y1)/(x2-x1))
    float x = input[0];
    for (float y = input[1]; y < input[3]; y++) {
      intersections.add(new Point2D.Float(x, y));
      System.out.println("x1:" + x + " y1:" + y);
      x += (1 / Math.abs(gradient));
    }

    invertPixels(intersections);
  }

  private void quadrant2(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float gradient = ((input[3] - input[1]) / (input[2] - input[0])); // ((y2-y1)/(x2-x1))
    float x1 = input[0];
    float y1 = input[1];
    // hinzufügen vom Startpkt
    intersections.add(new Point2D.Float(x1, y1));
    x1 -= (1 / Math.abs(gradient));
    for (float y = y1 + 1; y < input[3]; y++) {

      intersections.add(new Point2D.Float(x1, y));
      System.out.println("x1:" + x1 + " y1:" + y);
      x1 -= (1 / Math.abs(gradient));
    }

    invertPixels(intersections);
  }

  private void quadrant3(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float gradient = ((input[3] - input[1]) / (input[2] - input[0])); // ((y2-y1)/(x2-x1))
    float x = input[0];
    x -= (1 / Math.abs(gradient));
    for (float y = input[1] - 1; y >= input[3]; y--) {
      intersections.add(new Point2D.Float(x + 1, y));
      System.out.println("x1:" + x + " y1:" + y);
      x -= (1 / Math.abs(gradient));
    }

    invertPixels(intersections);
  }

  private void quadrant4(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float gradient = ((input[3] - input[1]) / (input[2] - input[0])); // ((y2-y1)/(x2-x1))

    float x = input[0];
    for (float y = input[1] - 1; y >= input[3]; y--) {
      intersections.add(new Point2D.Float(x + 1, y));
      System.out.println("x1:" + x + " y1:" + y);
      x += (1 / Math.abs(gradient));
    }

    invertPixels(intersections);
  }

  private void invertPixels(LinkedList<Point2D.Float> intersections) {
    for (int i = 0; i < intersections.size(); i++) {
      float x = intersections.get(i).x;
      float y = intersections.get(i).y;
      for (int j = (int) Math.ceil(x); j <= 9; j++) {
        invertPixel(j, (int) y);
      }
    }
  }

  public void invertPixel(int x, int y) {
    if (currentState[y][x] == true) {
      currentState[y][x] = false;
      drawCircle(x, y, circleStandart);
    } else {
      currentState[y][x] = true;
      drawCircle(x, y, circleHighlight);
    }
  }

  public void finished() {
    lang.newText(new Offset(0, 75, "PseudoCode", "SW"),
        "Polygon komplett rasterisiert!", "Fertig", defaultDisOp, header);
  }

  private void drawCircle(int x, int y, CircleProperties cp) {
    lang.newCircle(new Coordinates(50 * x + 700, 425 - 50 * y), 20, "Pixel",
        defaultDisOp, cp);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    input = ((int[][]) primitives.get("input"));
    lineHighlight = (PolylineProperties) props
        .getPropertiesByName("lineHighlight");
    circleStandart = (CircleProperties) props
        .getPropertiesByName("circleStandart");
    coordinatensystem = (PolylineProperties) props
        .getPropertiesByName("coordinatensystem");
    circleHighlight = (CircleProperties) props
        .getPropertiesByName("circleHighlight");
    description = (TextProperties) props.getPropertiesByName("description");
    lineStandart = (PolylineProperties) props
        .getPropertiesByName("lineStandart");
    pseudoCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("pseudoCodeProperties");
    header = (TextProperties) props.getPropertiesByName("header");

    initCurrentStateArray();
    showDescription();
    drawCartesian();
    rasterisiere(input);
    showSourceCode();
    initPixels();
    calcIntersection(input);
    finished();

    return lang.toString();
  }

  public String getName() {
    return "Edge Fill Algorithmus";
  }

  public String getAlgorithmName() {
    return "Edge Fill Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Alvar Gamez Zerban, Axel Heimann, Nikos Kombouris";
  }

  public String getDescription() {
    String s = "Der gr\u00F6\u00DFte Nachteil der Kantenlisten-Algorithmen ist der Aufwand zur Sortierung und Manipulation"
        + "\n"
        + "der Listen. Der sehr einfache Edge-fill-Algorithmus kommt ohne diesen Aufwand aus. Beim "
        + "\n"
        + "Edge-fill-Algorithmus werden f\u00FCr jede Bildzeile, die beim x-Schnittpunkt eine Polygonkante schneidet, alle "
        + "\n"
        + "Pixel dieser Bildzeile mit einer x-Koordinate strikt gr\u00F6\u00DFer als x-Schnittpunkt + 0,5 invertiert. 'Invertierung' "
        + "\n"
        + "bedeutet hier, dass eingef\u00E4rbte Pixel in den Ausgangszustand zur\u00FCckgesetzt werden und umgekehrt. ."
        + "\n"
        + "Die Reihenfolge, in der die Polygonkanten abgearbeitet werden, ist beliebig.";

    return s;
  }

  public String getCodeExample() {
    return "1. Linie betrachten (Traversierung gegen den Uhrzeigersinn)."
        + "\n" + "2. Pixel mit gr\u00F6\u00DFerer X-Koordinate invertieren.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}