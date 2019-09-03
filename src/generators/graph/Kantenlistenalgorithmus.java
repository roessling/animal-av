package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;

//import algoanim.interactionsupport.MultipleChoiceQuestion;

public class Kantenlistenalgorithmus implements Generator {

  private Language                  lang;
  private DisplayOptions            defaultDisOp;
  private PolylineProperties        koordinatensystem    = new PolylineProperties();
  private PolylineProperties        lineHighlight        = new PolylineProperties();
  private PolylineProperties        lineStandart         = new PolylineProperties();
  private TextProperties            beschreibung         = new TextProperties();
  private TextProperties            header               = new TextProperties();
  private SourceCodeProperties      pseudoCodeProperties = new SourceCodeProperties();
  private SourceCode                PseudoCode;
  private SourceCode                Spalte1;
  private SourceCode                Spalte2;
  private CircleProperties          pixelHighlight       = new CircleProperties();
  private CircleProperties          pixelStandart        = new CircleProperties();
  private StringArray               GeordnetX;
  private StringArray               GeordnetY;
  private RectProperties            headerBox            = new RectProperties();
  private int[][]                   input;
  private LinkedList<Point2D.Float> intersectionsAll;
  private ArrayDisplayOptions       arrayDisOp;

  private static final String       Description1         = "Der Kantenlistenalgorithmus wird benutzt um Polygone zu rasterisieren. Das Polygon wird als Liste";
  private static final String       Description2         = "von Kanten bergeben. Zu Anschauungszwecken wird das Polygon zuerst gezeichnet. Dann werden die";
  private static final String       Description3         = "Schnittpunkte der Kanten mit den Bildzeilen ermittelt. Horizontale Kanten werden ignoriert. Danach";
  private static final String       Description4         = "werden die Punkte absteigend nach y-Koordinate sortiert. Bei gleicher y-Koordinate wird aufsteigend nach";
  private static final String       Description5         = "der x-Koordinate sortiert. Dadurch ergeben sich immer Paare mit gleicher y-Koordinate (x1, y) , (x2, y).";
  private static final String       Description6         = "Wobei x1 den kleineren x-Wert repr\u00E4sentiert. Alle Punkte von (x1-0,5, y) bis (x2+0,5, y) werden rasterisiert.";

  public void init() {
    lang = new AnimalScript("Kantenlistenalgorithmus Animation",
        "Alvar Gamez Zerban, Axel Heimann, Nikos Kombouris", 1280, 720);
    lang.setStepMode(true);
  }

  public void showDescription() {
    lang.newText(new Coordinates(20, 30), "Kantenlistenalgorithmus", "header",
        defaultDisOp, header);
    lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header",
        "SE"), "headerRect", defaultDisOp, headerBox);
    lang.newText(new Offset(0, 15, "header", "SW"), Description1,
        "Description1", defaultDisOp, beschreibung);
    lang.newText(new Offset(0, 5, "Description1", "SW"), Description2,
        "Description2", defaultDisOp, beschreibung);
    lang.newText(new Offset(0, 5, "Description2", "SW"), Description3,
        "Description3", defaultDisOp, beschreibung);
    lang.newText(new Offset(0, 5, "Description3", "SW"), Description4,
        "Description4", defaultDisOp, beschreibung);
    lang.newText(new Offset(0, 5, "Description4", "SW"), Description5,
        "Description5", defaultDisOp, beschreibung);
    lang.newText(new Offset(0, 5, "Description5", "SW"), Description6,
        "Description6", defaultDisOp, beschreibung);
    lang.nextStep();
  }

  public void drawCartesian() {
    int x, y;
    for (x = 700; x < 1151; x = x + 50) {
      Node[] nodes = new Node[] { new Coordinates(x, 25),
          new Coordinates(x, 425) };
      lang.newPolyline(nodes, "x" + x, defaultDisOp, koordinatensystem);
    }
    for (y = 425; y > 0; y = y - 50) {
      Node[] nodes = new Node[] { new Coordinates(700, y),
          new Coordinates(1150, y) };
      lang.newPolyline(nodes, "y" + y, defaultDisOp, koordinatensystem);
    }
    for (x = 0; x < 10; x++) {
      Offset OffsetX = new Offset(-3, -2, "x" + (700 + 50 * x), "S");
      lang.newText(OffsetX, "" + x, "tx" + x, defaultDisOp);
    }
    for (y = 1; y < 9; y++) {
      Offset OffsetY = new Offset(-10, -8, "y" + (425 - 50 * y), "W");
      lang.newText(OffsetY, "" + y, "tx" + y, defaultDisOp);
    }
  }

  public void rasterisiere(int[][] input) {
    lang.nextStep("Zeichne Polygon");
    Spalte1 = lang.newSourceCode(new Offset(0, 15, "Description6", "SW"),
        "Spalte1", defaultDisOp, pseudoCodeProperties);
    Spalte1.addCodeLine("Kante", null, 0, null);
    for (int i = 0; i < input.length; i++) {
      drawPolygon(i, input);
      highlightSpalte1(i, input);
      /*
       * MultipleChoiceQuestion MCQ1 = new MultipleChoiceQuestion(lang, "MCQ1");
       * MCQ1.setPrompt("Was passiert als n&auml;chstes?");
       * MCQ1.addAnswerOption("Nichts. Algorithmus ist fertig.", false,
       * "Es wurden noch keine Pixel rasteriesiert.", 10);
       * MCQ1.addAnswerOption("Die Eckpunkte des Polygons werden berechnet",
       * false, "Die Eckpunkte reichen zum rasterisieren nicht aus.", 10);
       * MCQ1.addAnswerOption("Alle Punkte auf den Geraden werden berechnet",
       * true, "Richtig!", 10);
       */
      lang.nextStep();
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
    PseudoCode.addCodeLine("1. Schnittpunkte der Kanten mit Pixeln berechnen",
        null, 0, null);
    PseudoCode
        .addCodeLine(
            "2. Schnittpunkte absteigend nach y-Koordinate und aufsteigend nach x-Koordinate sortieren.",
            null, 0, null);
    PseudoCode
        .addCodeLine(
            "3. Punktepaare betrachten und dazwischenliegende Punkte rasterisieren.",
            null, 0, null);
    lang.nextStep("Eckpunkte berechnen");
  }

  private String IntersectionsToString(LinkedList<Point2D.Float> intersections) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < intersections.size(); i++)
      sb.append("(" + intersections.get(i).getX() + "; "
          + intersections.get(i).getY() + ") ");
    return sb.toString();
  }

  private void calcIntersection(int[][] input) {
    PseudoCode.highlight(0);
    Spalte2 = lang.newSourceCode(new Offset(150, 15, "Description6", "SW"),
        "Spalte2", defaultDisOp, pseudoCodeProperties);
    Spalte2.addCodeLine("Gespeicherte Schnittpunkte", null, 0, null);
    Spalte2.highlight(0);
    for (int i = 0; i < input.length; i++) {
      drawLine("Kante" + i, input[i][0], input[i][1], input[i][2], input[i][3],
          lineHighlight);
      if (i > 0) {
        drawLine("Kante" + (i - 1), input[i - 1][0], input[i - 1][1],
            input[i - 1][2], input[i - 1][3], lineStandart);
      }
      lang.nextStep();
      if (input[i][1] == input[i][3]) {
        // horizontal
        Spalte2.addCodeLine("Ignoriert, da horizontale Kante", null, 0, null);
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
      Spalte2.highlight(i + 1);
      Spalte2.unhighlight(i);
      drawPolygon(i, input);

      lang.nextStep();

    }
    lang.nextStep("Schnittpunkte sortieren");
    /*
     * MultipleChoiceQuestion MCQ2 = new MultipleChoiceQuestion(lang, "MCQ2");
     * MCQ2.setPrompt("Was passiert als n&auml;chstes?");
     * MCQ2.addAnswerOption("Nichts. Algorithmus ist fertig.", false,
     * "Es wurden noch keine Pixel rasteriesiert.", 10); MCQ2.addAnswerOption(
     * "Die Schnittpunkte werden in beliebiger Reihenfolge traversiert.", false,
     * "Die Schnittpunkte m&uuml;ssen geordnet vorliegen", 10);
     * MCQ2.addAnswerOption("Die Schnittpunkte werden sortiert.", true,
     * "Richtig!", 10);
     */

    Spalte2.unhighlight(input.length);
    PseudoCode.unhighlight(0);
    drawLine("Kante" + (input.length - 1), input[input.length - 1][0],
        input[input.length - 1][1], input[input.length - 1][2],
        input[input.length - 1][3], lineStandart);
  }

  /*
   * private void senkrecht(int[] input){ LinkedList<Point2D.Float>
   * intersections = new LinkedList<Point2D.Float>(); int j=0; if
   * (input[1]<input[3]){ while(input[1]+0.5+j < input[3]){
   * intersections.add(new Point2D.Float(input[0], (float) (input[1]+0.5+j)));
   * intersectionsAll.add(new Point2D.Float(input[0], (float)
   * (input[1]+0.5+j))); j++; }
   * Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null); }
   * else{ while(input[1]-0.5-j > input[3]){ intersections.add(new
   * Point2D.Float(input[0], (float) (input[1]-0.5-j)));
   * intersectionsAll.add(new Point2D.Float(input[0], (float)
   * (input[1]-0.5-j))); j++; }
   * Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null); }
   * }
   */

  private void senkrecht(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float x1 = input[0];
    if (input[1] < input[3]) {
      for (float y = input[1]; y < input[3]; y++) {
        intersections.add(new Point2D.Float(x1, (float) (y + 0.5)));
        intersectionsAll.add(new Point2D.Float(x1, (float) (y + 0.5)));
      }
    } else {
      for (float y = input[1]; y > input[3]; y--) {
        intersections.add(new Point2D.Float(x1, (float) (y - 0.5)));
        intersectionsAll.add(new Point2D.Float(x1, (float) (y - 0.5)));
      }
    }
    Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null);
  }

  /*
   * private void quadrant1(int[] input){ LinkedList<Point2D.Float>
   * intersections = new LinkedList<Point2D.Float>(); int j=0; float gradient=
   * ((input[3]-input[1])/(input[2]-input[0])); //((y2-y1)/(x2-x1))
   * while(input[0]+j/gradient+0.5 <= input[2]){ intersections.add(new
   * Point2D.Float((float) (input[0]+j/gradient+0.5), (float)
   * (input[1]+0.5+j))); intersectionsAll.add(new Point2D.Float((float)
   * (input[0]+j/gradient+0.5), (float) (input[1]+0.5+j))); j++; }
   * Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null); }
   */

  private void quadrant1(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float gradient = ((input[3] - input[1]) / (input[2] - input[0])); // ((y2-y1)/(x2-x1))
    float x = input[0];
    for (float y = input[1]; y <= input[3]; y++) {
      intersections.add(new Point2D.Float((float) (x + (0.5 / gradient)),
          (float) (y + (0.5 / gradient))));
      intersectionsAll.add(new Point2D.Float((float) (x + (0.5 / gradient)),
          (float) (y + (0.5 / gradient))));
      // System.out.println("x1:" + x + " y1:" + y);
      x++;
      // x += (1 /Math.abs(gradient));
    }
    Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null);
  }

  /*
   * private void quadrant2(int[] input){ LinkedList<Point2D.Float>
   * intersections = new LinkedList<Point2D.Float>(); int j=0; float gradient=
   * ((input[3]-input[1])/(input[2]-input[0])); //((y2-y1)/(x2-x1))
   * while(input[0]+j/gradient-0.5 >= input[2]){ intersections.add(new
   * Point2D.Float((float) (input[0]+j/gradient-0.5), (float)
   * (input[1]+0.5-j*gradient))); intersectionsAll.add(new Point2D.Float((float)
   * (input[0]+j/gradient-0.5), (float) (input[1]+0.5-j*gradient))); j++; }
   * Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null); }
   */

  private void quadrant2(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float gradient = ((input[3] - input[1]) / (input[2] - input[0])); // ((y2-y1)/(x2-x1))
    float x1 = input[0];
    float y1 = input[1];
    // hinzufügen vom Startpkt
    // intersections.add(new Point2D.Float(x1,y1));
    // x1 -= (1 /Math.abs(gradient));
    for (float y = y1; y < input[3]; y++) {
      intersections.add(new Point2D.Float((float) (x1 + (0.5 / gradient)),
          (float) (y - (0.5 / gradient))));
      intersectionsAll.add(new Point2D.Float((float) (x1 + (0.5 / gradient)),
          (float) (y - (0.5 / gradient))));
      // System.out.println("x1:" + x1 + " y1:" + y);
      x1--;
      // x1 -= (1 /Math.abs(gradient));
    }
    Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null);
  }

  /*
   * private void quadrant3(int[] input){ LinkedList<Point2D.Float>
   * intersections = new LinkedList<Point2D.Float>(); int j=0; float gradient=
   * ((input[3]-input[1])/(input[2]-input[0])); //((y2-y1)/(x2-x1))
   * while(input[0]-j/gradient-0.5 >= input[2]){ intersections.add(new
   * Point2D.Float((float)(input[0]-j/gradient-0.5), (float)
   * (input[1]-0.5-j*gradient))); intersectionsAll.add(new Point2D.Float((float)
   * (input[0]-j/gradient-0.5), (float) (input[1]-0.5-j*gradient))); j++; }
   * Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null); }
   */

  private void quadrant3(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float gradient = ((input[3] - input[1]) / (input[2] - input[0])); // ((y2-y1)/(x2-x1))
    float x = input[0];
    // intersections.add(new Point2D.Float(x,input[1]));
    // x -= (1 /Math.abs(gradient));
    for (float y = input[1]; y > input[3]; y--) {
      intersections.add(new Point2D.Float((float) (x - (0.5 / gradient)),
          (float) (y - (0.5 / gradient))));
      intersectionsAll.add(new Point2D.Float((float) (x - (0.5 / gradient)),
          (float) (y - (0.5 / gradient))));
      // System.out.println("x1:" + x + " y1:" + y);
      x--;
      // x -= (1 /Math.abs(gradient));
    }
    Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null);
  }

  /*
   * private void quadrant4(int[] input){ LinkedList<Point2D.Float>
   * intersections = new LinkedList<Point2D.Float>(); int j=0; float gradient=
   * ((input[3]-input[1])/(input[2]-input[0])); //((y2-y1)/(x2-x1))
   * while(input[0]-j/gradient+0.5 <= input[2]){ intersections.add(new
   * Point2D.Float((float) (input[0]-j/gradient+0.5), (float)
   * (input[1]-0.5+j*gradient))); intersectionsAll.add(new Point2D.Float((float)
   * (input[0]-j/gradient+0.5), (float) (input[1]-0.5+j*gradient))); j++; }
   * Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null); }
   */

  private void quadrant4(int[] input) {
    LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
    float gradient = ((input[3] - input[1]) / (input[2] - input[0])); // ((y2-y1)/(x2-x1))

    float x = input[0];
    for (float y = input[1]; y > input[3]; y--) {
      intersections.add(new Point2D.Float((float) (x - (0.5 / gradient)),
          (float) (y + (0.5 / gradient))));
      intersectionsAll.add(new Point2D.Float((float) (x - (0.5 / gradient)),
          (float) (y + (0.5 / gradient))));
      // System.out.println("x1:" + x + " y1:" + y);
      x++;
      // x += (1 /Math.abs(gradient));
    }
    Spalte2.addCodeLine(IntersectionsToString(intersections), null, 0, null);
  }

  private void sort(LinkedList<Point2D.Float> intersectionsAll) {
    int n = intersectionsAll.size();
    int j;
    float t, u;
    for (int i = 1; i < n; i++) {
      j = i;
      t = intersectionsAll.get(j).y;
      u = intersectionsAll.get(j).x;
      while (j > 0) {
        if (intersectionsAll.get(j - 1).getY() < t) {
          Point2D.Float temp = intersectionsAll.remove(j);
          intersectionsAll.add(j - 1, temp);
        } else if (intersectionsAll.get(j - 1).getY() == t) {
          if (intersectionsAll.get(j - 1).getX() > u) {
            Point2D.Float temp = intersectionsAll.remove(j);
            intersectionsAll.add(j - 1, temp);
          }
        }
        j--;
      }
    }

  }

  public void displaySortedGrid(LinkedList<Point2D.Float> intersectionsAll) {
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);

    String[] X = new String[intersectionsAll.size() + 1];
    String[] Y = new String[intersectionsAll.size() + 1];

    X[0] = "X: ";
    Y[0] = "Y: ";

    for (int i = 0; i < intersectionsAll.size(); i++) {
      X[i + 1] = java.lang.Float.toString(intersectionsAll.get(i).x);
      Y[i + 1] = java.lang.Float.toString(intersectionsAll.get(i).y);
    }

    GeordnetX = lang.newStringArray(new Coordinates(20, 450), X, "GeordnetX",
        arrayDisOp, arrayProps);
    GeordnetY = lang.newStringArray(new Coordinates(20, 470), Y, "GeordnetY",
        arrayDisOp, arrayProps);

    PseudoCode.highlight(1);
    GeordnetX.highlightElem(0, intersectionsAll.size(), null, null);
    GeordnetY.highlightElem(0, intersectionsAll.size(), null, null);
    lang.nextStep("Pixel einf\u00E4rben");
  }

  public void displayPixels(LinkedList<Point2D.Float> intersectionsAll) {
    GeordnetX.unhighlightElem(0, 18, null, null);
    GeordnetY.unhighlightElem(0, 18, null, null);
    PseudoCode.unhighlight(1);
    PseudoCode.highlight(2);

    // System.out.println(intersectionsAll.toString());

    for (int i = 0; i < intersectionsAll.size(); i = i + 2) {
      lang.nextStep();

      GeordnetX.highlightElem(i + 1, i + 2, null, null);
      GeordnetY.highlightElem(i + 1, i + 2, null, null);

      if (i > 0) {
        GeordnetX.unhighlightElem(i - 1, i, null, null);
        GeordnetY.unhighlightElem(i - 1, i, null, null);
      }
      for (float j = (int) intersectionsAll.get(i).x; j <= intersectionsAll
          .get(i + 1).x; j++) {
        if (intersectionsAll.get(i).x - 0.5 <= j
            && j <= intersectionsAll.get(i + 1).x - 0.5) {
          lang.newCircle(new Coordinates((int) j * 50 + 700,
              (int) (intersectionsAll.get(i).y) * (-50) + 425), 20, "Pixel",
              defaultDisOp, pixelHighlight);
        }
      }
    }
    lang.nextStep();
    GeordnetX.unhighlightElem(intersectionsAll.size() - 1,
        intersectionsAll.size(), null, null);
    GeordnetY.unhighlightElem(intersectionsAll.size() - 1,
        intersectionsAll.size(), null, null);
    PseudoCode.unhighlight(2);
    lang.newText(new Offset(0, 25, "GeordnetY", "SW"),
        "Polygon komplett rasterisiert!", "Fertig", defaultDisOp, header);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    headerBox = (RectProperties) props.getPropertiesByName("headerBox");
    input = (int[][]) primitives.get("input");
    lineHighlight = (PolylineProperties) props
        .getPropertiesByName("lineHighlight");
    beschreibung = (TextProperties) props.getPropertiesByName("beschreibung");
    lineStandart = (PolylineProperties) props
        .getPropertiesByName("lineStandart");
    pixelHighlight = (CircleProperties) props
        .getPropertiesByName("pixelHighlight");
    pixelStandart.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    koordinatensystem = (PolylineProperties) props
        .getPropertiesByName("koordinatensystem");
    pseudoCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("pseudoCodeProperties");
    header = (TextProperties) props.getPropertiesByName("header");

    intersectionsAll = new LinkedList<Point2D.Float>();

    showDescription();
    drawCartesian();
    rasterisiere(input);
    showSourceCode();
    calcIntersection(input);
    sort(intersectionsAll);
    displaySortedGrid(intersectionsAll);
    displayPixels(intersectionsAll);
    // lang.finalizeGeneration();

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Kantenlistenalgorithmus";
  }

  @Override
  public String getAnimationAuthor() {
    return "Alvar Gamez Zerban, Axel Heimann, Nikos Kombouris";
  }

  @Override
  public String getCodeExample() {
    return "Der Kantenlistenalgorithmus wird benutzt um Polygone zu rasterisieren. Das Polygon wird als Liste"
        + "\n"
        + "von Kanten &uuml;bergeben. Zu Anschauungszwecken wird das Polygon zuerst gezeichnet. Dann werden die"
        + "\n"
        + "Schnittpunkte der Kanten mit den Bildzeilen ermittelt. Horizontale Kanten werden ignoriert. Danach"
        + "\n"
        + "werden die Punkte absteigend nach y-Koordinate sortiert. Bei gleicher y-Koordinate wird aufsteigend nach"
        + "\n"
        + "der x-Koordinate sortiert. Dadurch ergeben sich immer Paare mit gleicher y-Koordinate (x1, y) , (x2, y)."
        + "\n"
        + "Wobei x1 den kleineren x-Wert repr&auml;sentiert. Alle Punkte von (x1-0,5, y) bis (x2+0,5, y) werden rasterisiert.";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "1. Schnittpunkte der Kanten mit Pixeln berechnen"
        + "\n"
        + "2. Schnittpunkte absteigend nach y-Koordinate und aufsteigend nach x-Koordinate sortieren."
        + "\n"
        + "3. Punktepaare betrachten und dazwischenliegende Punkte rasterisieren.";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  @Override
  public String getName() {
    return "Kantenlistenalgorithmus Animation";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}
