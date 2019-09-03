/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generators.tree.helpers;

import static algoanim.animalscript.AnimalScript.DIRECTION_NE;
import static algoanim.animalscript.AnimalScript.DIRECTION_NW;
import static algoanim.animalscript.AnimalScript.DIRECTION_S;
import static algoanim.animalscript.AnimalScript.DIRECTION_SE;
import static algoanim.animalscript.AnimalScript.DIRECTION_SW;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import algoanim.animalscript.AnimalCircleGenerator;
import algoanim.animalscript.AnimalGraphGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringArrayGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.GraphGenerator;
import algoanim.primitives.generators.RectGenerator;
import algoanim.primitives.generators.StringArrayGenerator;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * 
 * @author Alex Krause, Markus Ermuth
 */
public class QuadtreeHelper {

  // private Color graphNodeHighlightColor;
  private Graph                       output;
  private int[][]                     workingMatrix;
  private int[][]                     graphAdjacencyMatrix;
  private AnimalScript                lang;
  private Tree                        quadtree;
  private int                         treeCounter;
  private SourceCode                  animCode;
  private Rect                        areaRect;
  private int                         elementsCounter;
  private static String[]             CODE            = new String[] {
      "forall points{", "    insert(point, root)", "}", "",
      "insert(point, currentNode){", "    if(currentNode is leaf){",
      "        if(currentNode is empty)",
      "            set point to currentNode", "       else{",
      "            split the Tree", "            insert(point, currentNode)",
      "        }", "    } else {",
      "        child = next quadrant for this point",
      "        insert(point, child)", "    }", "}"   };
  private static String[]             DESCRIPTION     = new String[] {
      "Ein Quadtree ist in der Informatik eine spezielle Baum-Struktur,",
      "in der jeder innere Knoten genau vier Kinder hat. Das Wort ",
      "Quadtree leitet sich von der Zahl der Kinder eines inneren ",
      "Knotens ab (quad (vier) + tree (Baum) = Quadtree). Diese ",
      "Struktur wird hauptsächlich zur Organisation zweidimensionaler ",
      "Daten im Bereich der Computergrafik eingesetzt. Die Wurzel des ",
      "Baumes repräsentiert dabei eine quadratische Fläche. Diese wird ",
      "rekursiv in je vier gleich große Quadranten zerlegt bis die ",
      "gewünschte Auflösung erreicht ist und die Rekursion in einem ",
      "Blatt endet. Durch rekursive Anwendung dieser Zerteilung kann die",
      " vom Wurzelknoten repräsentierte Fläche beliebig fein aufgelöst ",
      "werden. Für dreidimensionale Daten verwendet man gewöhnlich ",
      "Octrees.Da ein Blatt unter Umständen eine verhältnismäßig große ",
      "Fläche abdecken kann, ist die Datenstruktur relativ ",
      "speichersparend und schnell nach einem Blatt, das einen ",
      "bestimmten Punkt beinhaltet, zu durchsuchen.",
      "Quelle: http://de.wikipedia.org/wiki/Quadtree 13.09.2012" };
  private static String[]             FAZIT           = new String[] {
      "Die Komplexität, um einen Quadtree aufzubauen, hängt von der ",
      "Verteilung der einzelnen Punkte ab. Die Kosten zum Eingügen eines",
      " Punktes verhält sich dabei proportional zur Distanz zwischen dem",
      " Wurzelknoten und dem Knoten, wo der Punkt eingefügt wird. Liegen",
      " die Punkte alle nahe beieinander, kann die Komplexität sehr groß",
      " werden. Gerade für solche Fälle führt man dann eine Schranke ein,",
      " bei der man aufhört, den Baum aufzuteilen, welche dann ",
      "dementsprechend die obere Schranke für die Komplexität zum ",
      "Einfügen eines Punktes darstellt. Sind die Punkte hingegen ",
      "gleichmäßig verteilt, sodass ein balancierter Baum ensteht, d.h. ",
      "alle Punkte werden in einer Ebene des Quadtrees eingeordnet, so ",
      "minimiert sich die Komplexität zum Einfügen eines Knotens auf ",
      "O(n*log n), wobei n der Anzahl der einzufügenden Punkte entspricht." };
  private int                         highlightOldLine;
  private int                         recSize;
  private RectGenerator               rectGen;
  private GraphGenerator              graphGen;
  private GraphProperties             grProps;
  private final AnimalCircleGenerator circleGen;
  private RectProperties              headerbox_properties;
  private RectProperties              rect_properties;
  private final CircleProperties      point_properties;
  private TextProperties              header_properties;
  private SourceCodeProperties        sourcecode_properties;
  private SourceCodeProperties        description_properties;
  private ArrayProperties             array_properties;
  private int                         questionCounter = 0;

  public QuadtreeHelper(AnimalScript script, int[][] input,
      Color graphNodeHighlightColor, AnimationPropertiesContainer props) {
    quadtree = new Tree(new Node());
    workingMatrix = input;
//    this.graphNodeHighlightColor = graphNodeHighlightColor;
    lang = script;
    lang.setStepMode(true);
    treeCounter = 0;
    highlightOldLine = -1;
    elementsCounter = 0;
    recSize = 24;
    rectGen = new AnimalRectGenerator(lang);

    graphGen = new AnimalGraphGenerator(lang);

    grProps = new GraphProperties("Graph");
    grProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
    grProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        graphNodeHighlightColor);
    grProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        graphNodeHighlightColor);
    grProps.set("fillColor", Color.WHITE);
    grProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);

    circleGen = new AnimalCircleGenerator(lang);

    headerbox_properties = (RectProperties) props
        .getPropertiesByName("headerbox_properties");
    rect_properties = (RectProperties) props
        .getPropertiesByName("rect_properties");
    point_properties = (CircleProperties) props
        .getPropertiesByName("point_properties");
    header_properties = (TextProperties) props
        .getPropertiesByName("header_properties");
    header_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    sourcecode_properties = (SourceCodeProperties) props
        .getPropertiesByName("sourcecode_properties");
    description_properties = (SourceCodeProperties) props
        .getPropertiesByName("description_properties");
    array_properties = (ArrayProperties) props
        .getPropertiesByName("array_properties");
  }

  public void buildQuadtree() {
    TextGenerator textGen = new AnimalTextGenerator(lang);

    // AnimalInit

    Text header = new Text(textGen, new Coordinates(10, 10), "Quadtree",
        "header", null, header_properties);
    Rect headerBox = new Rect(rectGen, new Offset(-10, -10, header,
        DIRECTION_NW), new Offset(10, 10, header, DIRECTION_SE), "headerBox",
        null, headerbox_properties);

    lang.nextStep("Einführung");
    SourceCode animDescription = makeCode(DESCRIPTION, description_properties,
        new Offset(10, 10, headerBox, DIRECTION_SW), null);

    lang.nextStep("Ausführung");
    animDescription.hide();
    animCode = makeCode(CODE, sourcecode_properties, new Offset(10, 10,
        headerBox, DIRECTION_SW), new int[] { 0, 2, 0, 0, 0, 2, 4, 6, 4, 6, 6,
        4, 2, 4, 4, 2, 0 });
    StringArrayGenerator arrayGen = new AnimalStringArrayGenerator(lang);

    String[] arrayLabels = new String[workingMatrix.length + 1];
    arrayLabels[0] = "Points:";
    for (int i = 1; i < arrayLabels.length; ++i) {
      arrayLabels[i] = "(" + workingMatrix[i - 1][1] + ", "
          + workingMatrix[i - 1][0] + ")";
    }
    StringArray pointArray = new StringArray(arrayGen, new Offset(10, 0,
        animCode, DIRECTION_NE), arrayLabels, "points_matrix", null,
        array_properties);
    areaRect = new Rect(rectGen, new Offset(10, 0, pointArray, DIRECTION_NE),
        new Offset(recSize * maxCoordinate() + 10, recSize * maxCoordinate(),
            pointArray, DIRECTION_NE), "areaRect", null, rect_properties);

    if (workingMatrix[0].length > 1) {
      for (int y = 0; y < workingMatrix.length; y++) {
        pointArray.highlightCell(y + 1, null, null);
        highlight(0);
        highlightNext();
        insert(new int[] { workingMatrix[y][0], workingMatrix[y][1], 1 });
      }
      highlight(0);
      highlight(2);
    }

    lang.nextStep("Fazit");
    animCode.hide();
    pointArray.hide();
    makeCode(FAZIT, description_properties, new Offset(10, 10, headerBox,
        DIRECTION_SW), null);

  }

  private void insert(int[] point) {
    insert(point, quadtree.root, new double[] { maxCoordinate() / 2,
        maxCoordinate() / 2 }, maxCoordinate());
  }

  private void insert(int[] point, Node node, double[] splitLines, double range) {
    highlight(4);
    highlightNext();
    double range2 = range;
    if (!node.hasChildren()) {
      createTFQuestion("Muss in diesem Schritt gesplitet werden?",
          node.value[2] != -1, "Absolut korrekt",
          "Leider falsch. Denk mal darüber nach, wann gesplitet werden muss.");
      highlightNext();
      if (node.value[0] == point[0] && node.value[1] == point[1]) {
        return;
      } else if (node.value[2] == -1) {
        node.value = point;
        makeTree();
        new Circle(circleGen, new Offset(point[1] * recSize,
            point[0] * recSize, areaRect, DIRECTION_NW), 3, "Markus", null,
            point_properties);
        highlightNext();
      } else {
        highlight(8);

        Node parentCloneNode = new Node(node.value);
        node.value = new int[] { 0, 0, 0 };
        quadtree.insertNode(node, parentCloneNode,
            getQuadrantForPoint(parentCloneNode.value, splitLines));
        makeTree();
        // System.out.println(String.format("Point: %1d, %2d \tLine: %3d, %4d \tRange: %5d",
        // point[0], point[1], splitLines[0], splitLines[1], range));
        makeRectangle(splitLines, range2);
        highlightNext();
        highlightNext();
        insert(point, node, splitLines, range2);
      }
    } else {
      highlight(12);
      highlightNext();
      Node[] children = node.getChildren();
      int quadrant = getQuadrantForPoint(point, splitLines);

      // TO DO: koordinaten dürfen nicht verändert werden
      // => statt range koordinaten, an denen gesplitet wird, verwenden?!
      range2 /= 2;
      if (quadrant == 0) {
        splitLines[0] -= range2 / 2;
        splitLines[1] -= range2 / 2;
      } else if (quadrant == 1) {
        splitLines[0] -= range2 / 2;
        splitLines[1] += range2 / 2;
      } else if (quadrant == 2) {
        splitLines[0] += range2 / 2;
        splitLines[1] += range2 / 2;
      } else {
        splitLines[0] += range2 / 2;
        splitLines[1] -= range2 / 2;
      }
      highlightNext();
      insert(point, children[quadrant], splitLines, range2);
    }
  }

  private void makeRectangle(double[] lines, double size) {
    double size2 = size;
    size2 *= recSize;
    size2 /= 2;
    double[] lines2 = lines;
    lines2 = new double[] { lines2[1], lines2[0] };
    lines2[0] *= recSize;
    lines2[1] *= recSize;
    new Rect(rectGen, new Offset(round(lines2[0] - size2), round(lines2[1]
        - size2), areaRect, DIRECTION_NW), new Offset(round(lines2[0]),
        round(lines2[1]), areaRect, DIRECTION_NW),
        "newRect" + elementsCounter++, null, rect_properties);
    new Rect(rectGen, new Offset(round(lines2[0]), round(lines2[1] - size2),
        areaRect, DIRECTION_NW), new Offset(round(lines2[0] + size2),
        round(lines2[1]), areaRect, DIRECTION_NW),
        "newRect" + elementsCounter++, null, rect_properties);
    new Rect(rectGen, new Offset(round(lines2[0]), round(lines2[1]), areaRect,
        DIRECTION_NW), new Offset(round(lines2[0] + size2), round(lines2[1]
        + size2), areaRect, DIRECTION_NW), "newRect" + elementsCounter++, null,
        rect_properties);
    new Rect(rectGen, new Offset(round(lines2[0] - size2), round(lines2[1]),
        areaRect, DIRECTION_NW), new Offset(round(lines2[0]), round(lines2[1]
        + size2), areaRect, DIRECTION_NW), "newRect" + elementsCounter++, null,
        rect_properties);
  }

  private int round(double n) {
    return (int) Math.round(n);
  }

  private int getQuadrantForPoint(int[] point, double[] splitLines) {
    if (point[0] <= splitLines[0]) {
      if (point[1] <= splitLines[1]) {
        return 0;
      } else {
        return 1;
      }
    } else {
      if (point[1] <= splitLines[1]) {
        return 3;
      } else {
        return 2;
      }
    }
  }

//  private int nextChild(int predecessor, int previousChild) {
//    for (int child = previousChild + 1; child < graphAdjacencyMatrix.length; child++) {
//      if (graphAdjacencyMatrix[child][predecessor] == 1) {
//        return child;
//      }
//    }
//    return -1;
//  }

  public static String implodeArray(int[] inputArray, String glueString) {
    String output = "";
    if (inputArray.length > 0) {
      StringBuilder sb = new StringBuilder();
      sb.append(inputArray[0]);
      for (int i = 1; i < inputArray.length; i++) {
        sb.append(glueString);
        sb.append(inputArray[i]);
      }
      output = sb.toString();
    }
    return output;
  }

  private SourceCode makeCode(String[] source, SourceCodeProperties scProps,
      Offset off, int[] indentationArgs) {
    SourceCode code = lang.newSourceCode(off, "sourceCode", null, scProps);
    int indentation;
    for (int i = 0; i < source.length; i++) {
      indentation = 0;
      if (indentationArgs != null) {
        indentation = indentationArgs[i];
      }

      code.addCodeLine(source[i], null, indentation, null);
    }
    return code;
  }

  private void makeTree() {
    int height = quadtree.getHeight();
    int maxWidth = (int) java.lang.Math.pow(4, height - 1) * 50;// platz
                                                                // zwischen 2
                                                                // knoten
    ArrayList<Coordinates> nodes = new ArrayList<Coordinates>();
    positionNodes(quadtree.root, maxWidth / 2, 0, maxWidth, nodes);
    if (output != null) {
      output.hide();

    }

    graphAdjacencyMatrix = quadtree.getAdjacencyMatrix();

    output = new Graph(graphGen, "quadtree" + treeCounter++,
        graphAdjacencyMatrix, nodes.toArray(new Coordinates[nodes.size()]),
        quadtree.getLabels(), null, grProps);
    Node[] treeNodes = quadtree.getAllNodes();
    for (int i = 0; i < treeNodes.length; i++) {
      if (treeNodes[i].value[2] > 0) {
        output.highlightNode(i, null, null);
      }
    }
    try {
      output.moveTo(null, null, new Offset(0, 10, animCode, DIRECTION_S), null,
          null);
    } catch (IllegalDirectionException ex) {
      Logger.getLogger(QuadtreeHelper.class.getName()).log(Level.SEVERE, null,
          ex);
    }
  }

  private void positionNodes(Node currentNode, int horizontalPosition,
      int verticalPosition, int width, List<Coordinates> positionedNodes) {
    Coordinates node = new Coordinates(horizontalPosition,
        verticalPosition * 100);
    positionedNodes.add(node);
    if (currentNode == null) {
      return;
    }
    if (currentNode.hasChildren()) {
      int newWidth = width;

      int horizontalStep = newWidth
          / ((int) java.lang.Math.pow(4, verticalPosition + 1));
      int newHorizontalPosition = horizontalPosition
          - (int) (horizontalStep * 1.5);
      for (int i = 0; i < 4; i++) {

        positionNodes(currentNode.getChildren()[i], newHorizontalPosition,
            verticalPosition + 1, width, positionedNodes);
        newHorizontalPosition += horizontalStep;
      }
    }
  }

//  private String[] makeLabels() {
//    String[] labels = new String[quadtree.getSize()];
//    for (int i = 0; i < quadtree.getSize(); i++) {
//      labels[i] = "" + i;
//    }
//    return labels;
//  }

  private int maxCoordinate() {
    int res = 0;
    for (int y = 0; y < workingMatrix.length; y++) {
      for (int x = 0; x < workingMatrix[y].length; x++) {
        res = java.lang.Math.max(res, workingMatrix[y][x]);
      }
    }
    if (res % 2 != 0) {
      res++;
    }
    return res;
  }

  private void highlight(int line, String label) {
    if (highlightOldLine != -1) {
      animCode.toggleHighlight(highlightOldLine, line);
    } else {
      animCode.highlight(line);
    }
    highlightOldLine = line;
    if (label != null) {
      lang.nextStep(label);
    } else {
      lang.nextStep();
    }
  }

  private void highlight(int line) {
    highlight(line, null);
  }

//  private void highlightNext(String label) {
//    highlight(highlightOldLine + 1, label);
//  }

  private void highlightNext() {
    highlight(highlightOldLine + 1, null);
  }

  private void createTFQuestion(String question, boolean correctAnswer,
      String correct, String incorrect) {
    TrueFalseQuestionModel tfQuestion = new TrueFalseQuestionModel("TFQuestion"
        + questionCounter++);
    tfQuestion.setPrompt(question);
    tfQuestion.setFeedbackForAnswer(true, correct);
    tfQuestion.setFeedbackForAnswer(false, incorrect);
    tfQuestion.setCorrectAnswer(correctAnswer);
    tfQuestion.setPointsPossible(1);
    lang.addTFQuestion(tfQuestion);

  }
}
