package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class KMedianPPGenerator2 implements ValidatingGenerator {
  private Language             lang;
  private SourceCodeProperties sourceCode;
  private TextProperties       headerProps;
  private TextProperties       statProps;
  private int                  n;
  private int                  k;
  private int                  sizeSquare;
  private int                  medianCounter          = 0;
  private Text                 medianCounterText      = null;
  private int                  centroidCounter        = 0;
  private Text                 centroidCounterText    = null;
  private int                  clusterAssignments     = 0;
  private Text                 clusterAssignmentsText = null;
  private int                  sortInvocations        = 0;
  private Text                 sortInvocationsText    = null;

  /*
   * fehlzuweisungen: aktuelle clusterList mit vorheriger clusterList
   * vergleichen (z.B. in isEqual()) wie viele haben einen neuen centroid
   * 
   * number of sort invocations
   */

  public void init() {
    lang = new AnimalScript("k-median++", "Nora und Gregor", 800, 600);
    lang.setStepMode(true);
  }

  @SuppressWarnings("unchecked")
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourcecode");
    headerProps = (TextProperties) props.getPropertiesByName("header");
    statProps = (TextProperties) props.getPropertiesByName("statistics");
    n = (Integer) primitives.get("n");
    k = (Integer) primitives.get("k");
    sizeSquare = (Integer) primitives.get("sizeOfSquares");

    int k2 = k * 2;
    // int k3 = k;

    /*
     * sonstige Parameter
     */
    int x = 600;
    int y = 500;
    int counter = 0;

    /*
     * setup
     */
    sourceCode.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    TextProperties mono18 = new TextProperties();
    mono18.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    mono18.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    /*
     * first step
     */
    setHeader();
    SourceCode desc = setDesc();

    /*
     * next step
     */
    lang.nextStep();
    setCanvas();
    setStatistics();
    SourceCode sc = setSourceCode(sourceCode);
    Text kcounter = setCounter(counter, k, mono18, n);
    medianCounterText = setMedianCounter(medianCounter);
    centroidCounter++;
    centroidCounterText = setCentroidCounter(centroidCounter++);
    clusterAssignmentsText = setClusterAssignmentsCounter(clusterAssignments);
    sortInvocationsText = setSortInvocations(sortInvocations);

    ArrayList<Text> formulas = setFormulas();
    setFormulaDesc();
    TextProperties tpBig = new TextProperties();
    tpBig.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    Text formulaD1 = lang.newText(new Offset(0, 195, "sc",
        AnimalScript.DIRECTION_SW), "P(x) = D(x)² / Sum(D(x)²)", "formulaD1",
        null, tpBig);
    Text formulaD2 = lang
        .newText(new Offset(0, 175, "sc", AnimalScript.DIRECTION_SW),
            "D(x) = longest Distance to closest centroid", "formulaD2", null,
            tpBig);
    desc.hide();
    ArrayList<Coordinates> coords = setRandomCoords(n, x, y);
    ArrayList<Square> squares = printSquares(coords);

    // find random square and set as centroid and add to centroid list
    Square randomSquare = squares.get(0);
    ArrayList<Square> centroids = new ArrayList<Square>();
    centroids.add(randomSquare);

    // set color to RED of centroid
    randomSquare.changeColor("Color", Color.RED, null, null);
    sc.highlight(0);
    counter = 1;
    kcounter.hide();
    kcounter = lang.newText(new Offset(0, 40, "statistics",
        AnimalScript.DIRECTION_NW), "k counter = " + counter, "kcounter", null,
        statProps);
    ArrayList<Square> squaresWithoutC = new ArrayList<Square>();

    squaresWithoutC = (ArrayList<Square>) squares.clone();
    squaresWithoutC.remove(randomSquare);
    lang.nextStep();

    ArrayList<HashMap<Square[], Double>> clusterMap = new ArrayList<HashMap<Square[], Double>>();
    ArrayList<Primitive> primitivesToHide = new ArrayList<Primitive>();
    HashMap<Square[], Double> distMap = new HashMap<Square[], Double>();
    Square nextCentroid = null;
    Square nextCentroidsCentroid = null;
    Polyline redPl = null;

    // find smallest distance to closest centroids
    while (k2 > 0) {
      if (k2 % 2 == 0) {
        sc.unhighlight(0);
        sc.highlight(2);
        sc.unhighlight(3);
        formulaD2.changeColor("Color", Color.RED, null, null);
        formulaD1.changeColor("Color", Color.BLACK, null, null);
        // hide polylines of last step
        for (Primitive pl : primitivesToHide) {
          pl.hide();
        }
        // for each square set distance to their closest centroid
        for (Square swc : squaresWithoutC) {
          Square[] pair = { null, null };
          double dist = 0;
          dist = Double.POSITIVE_INFINITY;
          for (Square c : centroids) {
            if (getDistance(c, swc) < dist) {
              dist = getDistance(c, swc);
              pair[0] = c;
              pair[1] = swc;
            }
          }
          distMap.put(pair, dist);
        }
        ArrayList<Double> l = new ArrayList<Double>(distMap.values());
        Collections.sort(l);
        sortInvocationsText.hide();
        sortInvocationsText = setSortInvocations(sortInvocations++);
        Double distNextCentroid = l.get(l.size() - 1);

        for (Entry<Square[], Double> entry : distMap.entrySet()) {
          if (entry.getValue().equals(distNextCentroid)) {
            nextCentroid = entry.getKey()[1];
            nextCentroidsCentroid = entry.getKey()[0];
          }
        }

        // print polylines
        for (Square[] sa : distMap.keySet()) {
          Node[] vertices = {
              new Offset(0, 0, sa[0].getName(), AnimalScript.DIRECTION_C),
              new Offset(0, 0, sa[1].getName(), AnimalScript.DIRECTION_C) };
          PolylineProperties pp = new PolylineProperties();
          pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
          Polyline pl = lang.newPolyline(vertices, "pl", null, pp);
          primitivesToHide.add(pl);
          Double dist = distMap.get(sa);
          Text textPoly = lang.newText(new Offset(0, 12, sa[1].getName(),
              AnimalScript.DIRECTION_NW), Integer.toString(dist.intValue()),
              "textPoly", null);
          // Text textPoly = lang.newText(new Offset(0, 12, sa[1]
          // .getName(), AnimalScript.DIRECTION_NW), sa[1]
          // .getName(), "textPoly", null);
          primitivesToHide.add(textPoly);
        }
        clusterMap.add(distMap);
        distMap = new HashMap<Square[], Double>();
        lang.nextStep();
        k2--;
      }
      if (k2 % 2 == 1) {
        sc.highlight(3);
        sc.unhighlight(2);
        formulaD1.changeColor("Color", Color.RED, null, null);
        formulaD2.changeColor("Color", Color.BLACK, null, null);

        // do not do this in the last step
        if (k2 > 2) {
          centroids.add(nextCentroid);
          nextCentroid.changeColor("Color", Color.RED, null, null);
          centroidCounterText.hide();
          centroidCounterText = setCentroidCounter(centroidCounter++);
          squaresWithoutC.remove(nextCentroid);

          // update counter
          counter++;
          kcounter.hide();
          kcounter = lang.newText(new Offset(0, 40, "statistics",
              AnimalScript.DIRECTION_NW), "k counter = " + counter, "text2",
              null, statProps);

          // red polyline

          PolylineProperties polyProps = new PolylineProperties();
          polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
          Node[] node = {
              new Offset(0, 0, nextCentroidsCentroid.getName(),
                  AnimalScript.DIRECTION_C),
              new Offset(0, 0, nextCentroid.getName(), AnimalScript.DIRECTION_C) };
          redPl = lang.newPolyline(node, "nora", null, polyProps);
        }
        lang.nextStep();
        // redPl.changeColor("Color", Color.BLACK, null, null);
        primitivesToHide.add(redPl);
        k2--;
      }
    }
    formulaD1.changeColor("Color", Color.BLACK, null, null);
    formulaD2.changeColor("Color", Color.BLACK, null, null);

    lang.nextStep();
    sc.unhighlight(3);
    sc.highlight(4);
    lang.nextStep();

    // hide primitives (polylines and texts)
    for (Primitive primitive : primitivesToHide) {
      primitive.hide();
    }
    sc.unhighlight(4);
    sc.highlight(6);
    ArrayList<Primitive> polyList = new ArrayList<Primitive>();
    int k4 = k;
    while (k4 > 0) {
      ArrayList<Primitive> temp = new ArrayList<Primitive>();
      temp = colorClusters(k, k4, clusterMap, centroids);
      k4--;
      clusterAssignmentsText.hide();
      clusterAssignmentsText = setClusterAssignmentsCounter(clusterAssignments);
      lang.nextStep(500);
      polyList.addAll(temp);
    }

    lang.nextStep();

    /*
     * setup
     */
    HashMap<Square, LinkedList<Square>> copiedClusterList = new HashMap<Square, LinkedList<Square>>();
    HashMap<Square, LinkedList<Square>> clusterList = setClusterList(centroids,
        squares);
    // ArrayList<Square> copyNewCentroids = null;
    ArrayList<Square> newCentroids = centroids;
    // int i = 0;
    // ++ weil irgendwas nicht richtig hochgezählt hat
    medianCounter++;
    while (!isEqual(clusterList.keySet(), copiedClusterList.keySet(), k)) {
      sc.unhighlight(6);
      sc.highlight(7);
      colorFormulas(formulas, Color.RED);
      hideSquares(newCentroids);
      // copyNewCentroids = newCentroids;
      // hide(polyList);
      clusterList = setClusterList(newCentroids, squares);
      newCentroids = new ArrayList<Square>();
      // polyList = printPolylines(clusterList);

      // find and print new centroids
      for (Square centroid : clusterList.keySet()) {

        Square newCentroid = getCentroidFromCluster(clusterList.get(centroid));
        newCentroids.add(newCentroid);
        // System.out.println("centroid "+centroid.getName());
        // sysOutNames(clusterList.get(centroid));
        // System.out.println("copied centroid "+newCentroid.getName());
        // sysOutNames(clusterList.get(newCentroid));
      }
      lang.nextStep();
      sc.unhighlight(7);
      sc.highlight(6);
      colorFormulas(formulas, Color.BLACK);
      hide(polyList);
      copiedClusterList = setClusterList(newCentroids, squares);

      checkWrongAssignments(clusterList, copiedClusterList);

      changeColor(copiedClusterList);
      polyList = printPolylines(copiedClusterList);
      clusterAssignmentsText.hide();
      clusterAssignmentsText = setClusterAssignmentsCounter(clusterAssignments);
      lang.nextStep();

    }
    sc.unhighlight(6);
    sc.highlight(7);
    colorFormulas(formulas, Color.RED);
    lang.nextStep();
    sc.unhighlight(7);
    sc.highlight(8);
    colorFormulas(formulas, Color.BLACK);
    lang.nextStep();
    setFinalText(n, k);
    sc.hide();

    return lang.toString();
  }

  private Text setSortInvocations(int sortInvocations2) {
    statProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    Text counter = lang.newText(new Offset(0, 160, "statistics",
        AnimalScript.DIRECTION_NW), "number of sort invocations: "
        + sortInvocations2, "sort", null, statProps);
    return counter;
  }

  // private void sysOutNames(LinkedList<Square> linkedList) {
  // for (Square square : linkedList) {
  // System.out.println(square.getName());
  // }
  // }

  private void checkWrongAssignments(
      HashMap<Square, LinkedList<Square>> clusterList,
      HashMap<Square, LinkedList<Square>> copiedClusterList) {
    ArrayList<Square> squares = new ArrayList<Square>();
    ArrayList<ArrayList<Square>> list = new ArrayList<ArrayList<Square>>();
    for (Square centroid : clusterList.keySet()) {
      ArrayList<Square> temp = new ArrayList<Square>();
      for (Square square : clusterList.get(centroid)) {
        temp.add(square);
        squares.add(square);
      }
      list.add(temp);
      temp.remove(centroid);
      squares.remove(centroid);
    }
    ArrayList<ArrayList<Square>> list2 = new ArrayList<ArrayList<Square>>();
    for (Square centroid : copiedClusterList.keySet()) {
      ArrayList<Square> temp = new ArrayList<Square>();
      for (Square square : copiedClusterList.get(centroid)) {
        temp.add(square);
      }
      list2.add(temp);
      temp.remove(centroid);
    }

    // int count = 0;
    for (int i = 0; i < list.size(); i++) {
      for (int j = 0; j < list.get(i).size(); j++) {
        if (!list2.get(i).contains(list.get(i).get(j))
            && squares.contains(list.get(i).get(j))
            && list.get(i).get(j) != null) {
          // count++;
          squares.remove(list.get(i).get(j));
        }
      }
    }
  }

  // private Text setWrongClusterAssignments(int wrongClusterAssignments2) {
  // TextProperties tp = new TextProperties();
  // tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
  // Font.PLAIN, 18));
  // Text counter = lang.newText(new Offset(0, 160, "statistics",
  // AnimalScript.DIRECTION_NW),
  // "number of false cluster assignments: "
  // + wrongClusterAssignments2 + " (beta)", "assignments",
  // null, tp);
  // return counter;
  // }

  private Text setClusterAssignmentsCounter(int input) {
    statProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    Text counter = lang.newText(new Offset(0, 130, "statistics",
        AnimalScript.DIRECTION_NW), "number of cluster assignments: " + input,
        "assignments", null, statProps);
    return counter;
  }

  private Text setCentroidCounter(int centroidCounter2) {
    statProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    Text cCounter = lang.newText(new Offset(0, 100, "statistics",
        AnimalScript.DIRECTION_NW), "number of calculated centroids: "
        + centroidCounter2, "cCounter", null, statProps);
    return cCounter;
  }

  private Text setMedianCounter(int medianCounter) {
    statProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    Text median = lang.newText(new Offset(0, 70, "statistics",
        AnimalScript.DIRECTION_NW), "number of calculations of median: "
        + medianCounter, "median", null, statProps);
    return median;
  }

  private void setStatistics() {
    statProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    lang.newText(new Offset(40, 0, "rect5", AnimalScript.DIRECTION_NE),
        "Statistics:", "statistics", null, statProps);
  }

  private SourceCode setFormulaDesc() {
    SourceCodeProperties scProps3 = new SourceCodeProperties();
    scProps3.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    SourceCode formulaDesc = lang.newSourceCode(new Offset(0, 100, "sc",
        AnimalScript.DIRECTION_SW), "sc", null, scProps3);
    formulaDesc
        .addCodeLine(
            "The n expresses the number of data points. Xn is the n-th element of a list",
            null, 0, null);
    formulaDesc
        .addCodeLine(
            "of sorted coordinates. To find the coordinates of the new median data point",
            null, 0, null);
    formulaDesc
        .addCodeLine(
            "the calculation of the median is done for x and y coordinate separately.",
            null, 0, null);
    formulaDesc.addCodeLine("", null, 0, null);
    return formulaDesc;
  }

  private void colorFormulas(ArrayList<Text> formulas, Color color) {
    for (Text text : formulas) {
      text.changeColor("Color", color, null, null);
    }

  }

  private ArrayList<Text> setFormulas() {
    ArrayList<Text> result = new ArrayList<Text>();
    TextProperties tpBig = new TextProperties();
    tpBig.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    TextProperties tpSmall = new TextProperties();
    tpSmall.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 12));

    Text formula1 = lang.newText(new Offset(0, 30, "sc",
        AnimalScript.DIRECTION_SW), "if n odd: Median = X", "formula1", null,
        tpBig);
    result.add(formula1);
    Text formula2 = lang.newText(new Offset(3, -15, "formula1",
        AnimalScript.DIRECTION_SE), "(n+1)/2", "formula2", null, tpSmall);
    result.add(formula2);
    Text formula3 = lang.newText(new Offset(0, 70, "sc",
        AnimalScript.DIRECTION_SW), "if n even: Median = 0.5(X", "formula3",
        null, tpBig);
    result.add(formula3);
    Text formula4 = lang.newText(new Offset(3, -15, "formula3",
        AnimalScript.DIRECTION_SE), "n/2", "formula4", null, tpSmall);
    result.add(formula4);
    Text formula5 = lang.newText(new Offset(18, 5, "formula3",
        AnimalScript.DIRECTION_NE), " + X", "formula5", null, tpBig);
    result.add(formula5);
    Text formula6 = lang.newText(new Offset(3, -15, "formula5",
        AnimalScript.DIRECTION_SE), "(n/2 + 1)", "formula6", null, tpSmall);
    result.add(formula6);
    Text formula7 = lang.newText(new Offset(68, 5, "formula5",
        AnimalScript.DIRECTION_NE), ")", "formula7", null, tpBig);
    result.add(formula7);
    return result;
  }

  private void changeColor(HashMap<Square, LinkedList<Square>> copiedClusterList) {
    for (Square centroid : copiedClusterList.keySet()) {
      Random rand = new Random();
      float r = rand.nextFloat();
      float g = rand.nextFloat();
      float b = rand.nextFloat();
      Color randomColor = new Color(r, g, b);
      for (Square s : copiedClusterList.get(centroid)) {
        s.changeColor("Color", randomColor, null, null);
      }
    }

  }

  private void hide(ArrayList<Primitive> allPrimitives) {
    for (Primitive primitive : allPrimitives) {
      primitive.hide();
    }
  }

  private void hideSquares(ArrayList<Square> newCentroids) {
    for (Square square : newCentroids) {
      square.hide();
    }
  }

  private ArrayList<Primitive> printPolylines(
      HashMap<Square, LinkedList<Square>> clusterList) {
    ArrayList<Primitive> polyList = new ArrayList<Primitive>();
    for (Square centroid : clusterList.keySet()) {
      for (Square square : clusterList.get(centroid)) {

        PolylineProperties plP = new PolylineProperties();
        plP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
        Node[] node = {
            new Offset(0, 0, centroid.getName(), AnimalScript.DIRECTION_C),
            new Offset(0, 0, square.getName(), AnimalScript.DIRECTION_C) };
        Polyline grey = lang.newPolyline(node, "grey", null, plP);
        polyList.add(grey);
        clusterAssignments++;
      }
      clusterAssignments--;
    }
    return polyList;

  }

  private Square getCentroidFromCluster(LinkedList<Square> linkedList) {
    ArrayList<Integer> xCoor = new ArrayList<Integer>();
    ArrayList<Integer> yCoor = new ArrayList<Integer>();
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));

    for (Square square : linkedList) {
      xCoor.add(((Coordinates) square.getUpperLeft()).getX());
      yCoor.add(((Coordinates) square.getUpperLeft()).getY());
    }
    Collections.sort(xCoor);
    Collections.sort(yCoor);
    int x = 0;
    int y = 0;
    if (xCoor.size() % 2 == 1) {
      x = xCoor.get(((xCoor.size() + 1) / 2) - 1);
      y = yCoor.get(((yCoor.size() + 1) / 2) - 1);
    } else {
      int x1 = xCoor.get((xCoor.size() / 2) - 1);
      int x2 = xCoor.get((xCoor.size() / 2));
      x = (x1 + x2) / 2;

      int y1 = yCoor.get((yCoor.size() / 2) - 1);
      int y2 = yCoor.get((yCoor.size() / 2));
      y = (y1 + y2) / 2;

    }
    lang.nextStep();
    ArrayList<Text> xOrdnungList = new ArrayList<Text>();
    ArrayList<Text> yOrdnungList = new ArrayList<Text>();

    PolylineProperties polyProps = new PolylineProperties();
    polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    Node[] xNode = {
        new Offset(x - 595, 0, "rect5", AnimalScript.DIRECTION_NW),
        new Offset(x - 595, 0, "rect5", AnimalScript.DIRECTION_SW) };
    Polyline xLine = lang.newPolyline(xNode, "xLine", null, polyProps);
    medianCounterText.hide();
    medianCounterText = setMedianCounter(medianCounter++);
    sortInvocationsText.hide();
    sortInvocationsText = setSortInvocations(sortInvocations++);
    Text xText = lang.newText(new Offset(10, 20, "rect5",
        AnimalScript.DIRECTION_SW), "data points ordered by x coordinate",
        "xText", null, tp);
    for (Square square : linkedList) {
      for (int i = 0; i < xCoor.size(); i++) {
        if (((Coordinates) square.getUpperLeft()).getX() == xCoor.get(i)) {
          Text ordnung = lang.newText(new Offset(0, 12, square.getName(),
              AnimalScript.DIRECTION_NW), Integer.toString(i + 1), "ordnung",
              null);
          xOrdnungList.add(ordnung);
        }
      }
    }
    lang.nextStep();
    xText.hide();
    for (Text text : xOrdnungList) {
      text.hide();
    }
    Node[] yNode = { new Offset(0, y - 25, "rect5", AnimalScript.DIRECTION_NW),
        new Offset(0, y - 25, "rect5", AnimalScript.DIRECTION_NE) };
    Polyline yLine = lang.newPolyline(yNode, "yLine", null, polyProps);
    medianCounterText.hide();
    medianCounterText = setMedianCounter(medianCounter++);
    sortInvocationsText.hide();
    sortInvocationsText = setSortInvocations(sortInvocations++);
    Text yText = lang.newText(new Offset(10, 40, "rect5",
        AnimalScript.DIRECTION_SW), "data points ordered by y coordinate",
        "yText", null, tp);
    for (Square square : linkedList) {
      for (int i = 0; i < yCoor.size(); i++) {
        if (((Coordinates) square.getUpperLeft()).getY() == yCoor.get(i)) {
          Text ordnung = lang.newText(new Offset(0, 12, square.getName(),
              AnimalScript.DIRECTION_NW), Integer.toString(i + 1), "ordnung",
              null);
          yOrdnungList.add(ordnung);
        }
      }
    }
    lang.nextStep();
    yText.hide();
    for (Text text : yOrdnungList) {
      text.hide();
    }

    Coordinates c1 = new Coordinates(x, y);
    SquareProperties sp = new SquareProperties();
    sp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    sp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
    sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    Square square = lang.newSquare((Node) c1, sizeSquare, "square", null, sp);
    centroidCounterText.hide();
    centroidCounterText = setCentroidCounter(centroidCounter++);
    lang.nextStep(500);
    xLine.hide();
    yLine.hide();
    return square;
  }

  private HashMap<Square, LinkedList<Square>> setClusterList(
      ArrayList<Square> centroids, ArrayList<Square> squares) {
    HashMap<Square, LinkedList<Square>> result = new HashMap<Square, LinkedList<Square>>();
    HashMap<Square, Square> preResult = new HashMap<Square, Square>();
    LinkedList<Square> centroidsSquares = new LinkedList<Square>();
    Square a = null;
    Square b = null;
    for (Square square : squares) {
      double dist = Double.POSITIVE_INFINITY;
      for (Square centroid : centroids) {
        if (getDistance(centroid, square) < dist) {
          dist = getDistance(centroid, square);
          a = square;
          b = centroid;
        }
      }
      preResult.put(a, b);
    }
    Object[] array1 = preResult.keySet().toArray();
    Object[] array2 = preResult.values().toArray();

    for (Square centroid : centroids) {
      centroidsSquares = new LinkedList<Square>();
      for (int i = 0; i < array2.length; i++) {
        if (array2[i] == centroid) {
          if (!result.containsKey(centroid)) {
            result.put(centroid, centroidsSquares);
          }
          centroidsSquares.add((Square) array1[i]);
        }
      }

    }
    return result;
  }

  // private HashMap getSquaresToCentroid(Square centroid,
  // ArrayList<Square> squares) {
  // HashMap result = new HashMap();
  // HashMap squareMap = new HashMap();
  // double dist = Double.POSITIVE_INFINITY;
  //
  // return result;
  // }

  private ArrayList<Primitive> colorClusters(int k, int k4,
      ArrayList<HashMap<Square[], Double>> clusterMap,
      ArrayList<Square> centroids) {
    ArrayList<Primitive> polyList = new ArrayList<Primitive>();
    Random rand = new Random();
    float r = rand.nextFloat();
    float g = rand.nextFloat();
    float b = rand.nextFloat();
    Color randomColor = new Color(r, g, b);
    // Color randomColor = hex2Rgb(ColorList.get(0).toString());
    // ColorList.remove(0);
    for (Square[] pair : clusterMap.get(k - 1).keySet()) {

      if (pair[0] == centroids.get(k4 - 1)) {
        pair[0].changeColor("Color", randomColor, null, null);
        Coordinates newC = new Coordinates(
            ((Coordinates) pair[0].getUpperLeft()).getX(),
            ((Coordinates) pair[0].getUpperLeft()).getY());

        SquareProperties sp = new SquareProperties();
        sp.set(AnimationPropertiesKeys.FILL_PROPERTY, randomColor);
        sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        sp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        lang.newSquare(newC, sizeSquare, "squ", null, sp);
        pair[1].changeColor("Color", randomColor, null, null);
        PolylineProperties plP = new PolylineProperties();
        plP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
        Node[] node = {
            new Offset(0, 0, pair[0].getName(), AnimalScript.DIRECTION_C),
            new Offset(0, 0, pair[1].getName(), AnimalScript.DIRECTION_C) };
        Polyline grey = lang.newPolyline(node, "grey", null, plP);
        polyList.add(grey);
        clusterAssignments++;
      }

    }
    return polyList;
  }

  private ArrayList<Square> printSquares(ArrayList<Coordinates> coords) {
    SquareProperties sp = new SquareProperties();
    sp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    sp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    ArrayList<Square> squares = new ArrayList<Square>();
    for (Coordinates c : coords) {
      Square s = lang.newSquare(c, sizeSquare, "s", null, sp);
      squares.add(s);
    }
    return squares;
  }

  private ArrayList<Coordinates> setRandomCoords(int theN, int x, int y) {
    int n = theN;
    ArrayList<Coordinates> coords = new ArrayList<Coordinates>();
    while (n > 0) {
      Coordinates c = new Coordinates((int) (Math.random() * x) + 600,
          (int) (Math.random() * y) + 30);
      coords.add(c);
      n--;
    }
    return coords;
  }

  // +10, damit squares komplett im canvas liegen.
  private void setCanvas() {
    lang.newRect(new Coordinates(600, 30), new Coordinates(1210, 540), "rect5",
        null);
  }

  private SourceCode setSourceCode(SourceCodeProperties scProps) {
    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(20, 120), "sc", null,
        scProps);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display delay
    sc.addCodeLine("Select one point randomly as initial centroid", null, 0,
        null); // 0
    sc.addCodeLine("repeat", null, 0, null);
    sc.addCodeLine(
        "For each point, compute distance between point and the centroid;",
        null, 1, null); // 1
    sc.addCodeLine(
        "Choose the point with the highest probability to be the next centroid",
        null, 1, null); // 2
    sc.addCodeLine("until k centroids have been chosen", null, 0, null); // 3
    sc.addCodeLine("repeat", null, 0, null); // 4
    sc.addCodeLine(
        "Form k clusters by assigning each point to its closest centroid",
        null, 1, null); // 5
    sc.addCodeLine(
        "Recompute the centroid of each cluster based on their median", null,
        1, null); // 6
    sc.addCodeLine("until centroids do not change", null, 0, null); // 7
    return sc;
  }

  private Text setCounter(int counter, int k, TextProperties tp2, int n) {
    lang.newText(new Offset(100, 10, "rect1", AnimalScript.DIRECTION_NE),
        "k = " + k, "text2", null, tp2);
    lang.newText(new Offset(0, 10, "text2", AnimalScript.DIRECTION_SW), "n = "
        + n, "nnumber", null, tp2);
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 239, 213));
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    lang.newRect(new Offset(-5, -5, "text2", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "nnumber", AnimalScript.DIRECTION_SE), "rect2", null,
        rp);
    // Counter
    Text kcounter = lang.newText(new Offset(0, 40, "statistics",
        AnimalScript.DIRECTION_NW), "k counter = 0", "kcounter", null,
        statProps);
    return kcounter;
  }

  // Überschrift setzen
  private void setHeader() {
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 18));
    headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    lang.newText(new Coordinates(20, 30), "k-median++", "text1", null,
        headerProps);
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 239, 213));
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    lang.newRect(new Offset(-5, -5, "text1", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "text1", AnimalScript.DIRECTION_SE), "rect1", null, rp);

  }

  private double getDistance(Square a, Square b) {
    double x = Math.pow(((Coordinates) a.getUpperLeft()).getX()
        - ((Coordinates) b.getUpperLeft()).getX(), 2.0);
    double y = Math.pow(((Coordinates) a.getUpperLeft()).getY()
        - ((Coordinates) b.getUpperLeft()).getY(), 2.0);

    return Math.sqrt(x + y);
  }

  private SourceCode setFinalText(int n, int k) {
    SourceCodeProperties scProps3 = new SourceCodeProperties();
    scProps3.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    // now, create the source code entity
    SourceCode finale = lang.newSourceCode(new Coordinates(20, 120), "desc",
        null, scProps3);
    finale.addCodeLine("The k-median++ algorithm has partitioned the " + n
        + " elements into " + k, null, 0, null); // 0
    finale
        .addCodeLine("clusters. The complexity of the algorithm is O(log n).",
            null, 0, null); // 0
    return finale;
  }

  private SourceCode setDesc() {
    // first, set the visual properties for the source code
    SourceCodeProperties scProps2 = new SourceCodeProperties();
    scProps2.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    SourceCode desc = lang.newSourceCode(new Coordinates(20, 120), "desc",
        null, scProps2);
    desc.addCodeLine(
        "The k-median++ algorithm is a method of cluster analysis.", null, 0,
        null); // 0
    desc.addCodeLine(
        "It partitions n observations into k clusters. To do so the", null, 0,
        null); // 0
    desc.addCodeLine("algorithm calculates the median for each cluster to",
        null, 0, null); // 0
    desc.addCodeLine(
        "determine its centroid. In difference to the k-median algorithm",
        null, 0, null); // 0
    desc.addCodeLine(
        "it does not choose all initial centroids randomly. The first one",
        null, 0, null); // 0
    desc.addCodeLine(
        "is still selected randomly but the other k-1 will be chosen based",
        null, 0, null); // 0
    desc.addCodeLine("on the distance to all of the already chosen centroids",
        null, 0, null); // 0

    return desc;
  }

  private boolean isEqual(Set<Square> newCentroids,
      Set<Square> copiedNewCentroids, int k) {
    LinkedList<Node> list1 = new LinkedList<Node>();
    LinkedList<Node> list2 = new LinkedList<Node>();
    int counter = 0;
    for (Square s : copiedNewCentroids) {
      list1.add(s.getUpperLeft());
    }
    for (Square s : newCentroids) {
      list2.add(s.getUpperLeft());
    }
    for (Node node : list2) {
      if (contains(node, list1)) {
        counter++;
      }
    }
    if (counter == k) {
      return true;
    }
    return false;
  }

  private boolean contains(Node node, LinkedList<Node> list1) {
    int counter = 0;
    for (Node node2 : list1) {
      if (((Coordinates) node2).getX() == ((Coordinates) node).getX()
          && ((Coordinates) node2).getY() == ((Coordinates) node).getY()) {
        counter++;
      }
    }
    if (counter == 1)
      return true;
    return false;
  }

  // private HashMap sortByValue(HashMap map) {
  // LinkedList list = new LinkedList(map.entrySet());
  // Collections.sort(list, new Comparator() {
  // public int compare(Object o1, Object o2) {
  // return ((Comparable) ((Map.Entry) (o1)).getValue())
  // .compareTo(((Map.Entry) (o2)).getValue());
  // }
  // });
  //
  // HashMap result = new LinkedHashMap();
  // for (Iterator it = list.iterator(); it.hasNext();) {
  // Map.Entry entry = (Map.Entry) it.next();
  // result.put(entry.getKey(), entry.getValue());
  // }
  // return result;
  // }

  // private double getDistanceCircle(Circle a, Square b) {
  // double x = Math.pow(((Coordinates) a.getCenter()).getX()
  // - ((Coordinates) b.getUpperLeft()).getX(), 2.0);
  // double y = Math.pow(((Coordinates) a.getCenter()).getY()
  // - ((Coordinates) b.getUpperLeft()).getY(), 2.0);
  //
  // return Math.sqrt(x + y);
  // }
  //
  // private Color hex2Rgb(String colorStr) {
  // return new Color(Integer.valueOf(colorStr.substring(0, 2), 16),
  // Integer.valueOf(colorStr.substring(2, 4), 16), Integer.valueOf(
  // colorStr.substring(4, 6), 16));
  // }

  public String getName() {
    return "k-median++";
  }

  public String getAlgorithmName() {
    return "k-median++";
  }

  public String getAnimationAuthor() {
    return "Nora und Gregor";
  }

  public String getDescription() {
    return "The k-median++ algorithm is a method of cluster analysis." + "\n"
        + "It partitions n observations into k clusters. To do so" + "\n"
        + "the algorithm calculates the median for each cluster to" + "\n"
        + "determine its centroid. In difference to the k-median algorithm"
        + "\n"
        + "it does not choose all initial centroids randomly. The first one"
        + "\n"
        + "is still selected randomly but the other k-1 will be chosen based"
        + "\n" + "on the distance to all of the already chosen centroids.  ";
  }

  public String getCodeExample() {
    return "Select one point randomly as initial centroid"
        + "\n"
        + " repeat"
        + "\n"
        + "       For each point, compute distance between point and the centroid"
        + "\n"
        + "       Choose the point with the highest probability to be the next centroid"
        + "\n"
        + " until k centroids have been chosen"
        + "\n"
        + " repeat"
        + "\n"
        + "       Form k clusters by assigning each point to its closest centroid"
        + "\n"
        + "       Recompute the centroid of each cluster based on their median"
        + "\n" + " until centroids do not change";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {

    int n = (Integer) arg1.get("n");
    int k = (Integer) arg1.get("k");
    int size = (Integer) arg1.get("sizeOfSquares");

    if (size < 5)
      throw new IllegalArgumentException(
          "The size of the squares has to be greater than or equal to 5");

    if (n < 10)
      throw new IllegalArgumentException(
          "The number of data points n has to be greater than or equal to 10");

    if (k < 2)
      throw new IllegalArgumentException(
          "The number of clusters k has to be greater than or equal to 2");

    return true;
  }

}