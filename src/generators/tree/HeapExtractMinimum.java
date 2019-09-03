package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class HeapExtractMinimum implements ValidatingGenerator {
  private Language              lang;

  private static final String[] DESCRIPTION       = {
      "Ein Heap ist ein partiell geordneter Baum, bei dem alle Kinder eines Knotens N größer oder gleich sind, wie N. Heaps werden häufig dann verwendet, ",
      "wenn es darauf ankommt schnell ein Element mit der höchsten Priorität auszuwählen und zu entfernen. Sowohl das Einfügen eines Elementes, ",
      "als auch das Entfernen, haben eine logarithmische Komplexität. Häufig werden Heaps mit der Hilfe von Arrays implementiert. ",
      "Dabei haben die Kinder eines Knotens mit dem Index k die Indizes (k+1)*2-1 und (k+1)*2. ",
      " ",
      "Ich habe den Heap als Array implementiert. Da ich aber der Meinung bin, dass sich der Algorithmus besser ",
      "anhand eines Baumes verstehen lässt, gebe ich diesen immer parallel mit an. ",
      "n ist die Anzahl der sich im Heap befindenden Elemente. ",
      " ",
      "Das erste Arrayelement ist immer das kleinste Element. Deshalb wird bei der Methode extractMinimum() zuerst das erste Element durch das letzte",
      "ersetzt und solange wie dieses Element größer ist als einer der Kindsknoten, wird dieses Element mit dem kleinsten Kindsknoten vertauscht." };

  private static final String[] SOURCE_CODE       = {
      "public int extractMinimum(){", "    if(n>0){",
      "        int result = array[0];", "        array[0] = array[n-1];",
      "        int k = 0;", "        n--;", "        boolean ready = false;",
      "        while((k+1)*2-1<n && !ready){", "            int index;",
      "            if((k+1)*2<n && array[(k+1)*2] < array[(k+1)*2-1])",
      "                index = (k+1)*2;", "            else",
      "                index = (k+1)*2-1;",
      "            if(array[k] > array[index]){",
      "                int a = array[k];",
      "                array[k] = array[index];",
      "                array[index] = a;", "                k = index;",
      "            }", "            else", "                ready = true;",
      "        }", "        return result;", "    }", "    return -1;", "}" };

  private static final String   HEADER            = "Heap: extractMinimum";

  private static final Color    highlightColor    = Color.RED;

  private static final int      yDistance         = 70;
  private static final int      yDistanceToArray  = 100;

  private ArrayProperties       arrayProps;
  private ArrayMarkerProperties arrayMarkerProps;
  private TextProperties        headerProps, normalTextProps, headlineProps,
      circleTextProps, conclusionProps, descriptionProps;
  private GraphProperties       graphProps;
  private SourceCodeProperties  sourceCodeProps;
  private ArrayProperties       exampleArrayProps;
  private CircleProperties      circleProps;
  private CounterProperties     counterProps;

  private StringArray           sa;
  private Graph                 graph;
  private Text                  header, hints, text_n, text_k, equation,
      equation2, text_a, text_result, explanation1, explanation2, explanation3,
      sc_header, text_ready, text_index;
  private SourceCode            sc;
  private ArrayMarker           marker;
  private int                   graphCounter, graphWidth;

  private int                   counterComps      = 0;
  private int                   counterAllocs     = 0;
  private int                   counterIterations = 0;
  private int                   circleCounter     = 0;
  private TwoValueCounter       twoValueCounter;

  private int                   n, k, result, index, a;
  private boolean               ready;

  public void extractMinimum(String[] a, int[] ia) {
    setProperties();

    header = lang.newText(new Coordinates(20, 20), HEADER, "header", null,
        headerProps);
    graphCounter = -1;

    graphWidth = 120;
    int b = a.length;
    int h = 0;
    while (b > 1) {
      if (h <= 3)
        graphWidth *= 1.5;
      else
        graphWidth *= 2;
      b = b / 2;
      h++;
    }

    showDescription();
    showAlgorithm(a, ia);
    showConclusion();
  }

  private void setProperties() {
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) arrayProps
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont((float) 20));
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) arrayProps
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.PLAIN));

    exampleArrayProps = new ArrayProperties();
    exampleArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    exampleArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.LIGHT_GRAY);
    exampleArrayProps
        .set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    exampleArrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 20));

    arrayMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "k");

    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) headerProps
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont((float) 24));
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) headerProps
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.BOLD));

    normalTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) normalTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont((float) 16));

    descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) descriptionProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont((float) 16));
    conclusionProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) conclusionProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont((float) 16));

    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) headlineProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont((float) 20));
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) headlineProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont(Font.BOLD));

    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) sourceCodeProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont((float) 16));

    graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.YELLOW);
    graphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);

    circleProps = new CircleProperties();
    circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    circleTextProps = new TextProperties();
    circleTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 20));

    counterProps = new CounterProperties();
    counterProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    counterProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
  }

  private void showDescription() {
    Primitive[] des = new Primitive[DESCRIPTION.length];
    Primitive prev = (Primitive) header;
    for (int i = 0; i < DESCRIPTION.length; i++) {
      des[i] = lang.newText(new Offset(0, 8, prev, AnimalScript.DIRECTION_SW),
          DESCRIPTION[i], "description" + i, null, descriptionProps);
      prev = des[i];
    }

    Text example = lang.newText(new Offset(0, 40, des[DESCRIPTION.length - 1],
        null), "Beispiel:", "example", null, headlineProps);
    int[] exampleArray = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
        13, 14 };

    Text[] circleText = new Text[15];
    Circle[] c = new Circle[15];
    c[0] = createCircle(new Offset(200, 0, example, AnimalScript.DIRECTION_SW),
        new Color(158, 149, 242), "0", circleText);
    c[1] = createCircle(
        new Offset(100, 70, example, AnimalScript.DIRECTION_SW), new Color(255,
            255, 136), "1", circleText);
    c[2] = createCircle(
        new Offset(300, 70, example, AnimalScript.DIRECTION_SW), new Color(255,
            255, 136), "2", circleText);
    c[3] = createCircle(
        new Offset(50, 140, example, AnimalScript.DIRECTION_SW), new Color(255,
            128, 128), "3", circleText);
    c[4] = createCircle(
        new Offset(150, 140, example, AnimalScript.DIRECTION_SW), new Color(
            255, 128, 128), "4", circleText);
    c[5] = createCircle(
        new Offset(250, 140, example, AnimalScript.DIRECTION_SW), new Color(
            255, 128, 128), "5", circleText);
    c[6] = createCircle(
        new Offset(350, 140, example, AnimalScript.DIRECTION_SW), new Color(
            255, 128, 128), "6", circleText);
    c[7] = createCircle(
        new Offset(25, 210, example, AnimalScript.DIRECTION_SW), new Color(128,
            255, 128), "7", circleText);
    c[8] = createCircle(
        new Offset(75, 210, example, AnimalScript.DIRECTION_SW), new Color(128,
            255, 128), "8", circleText);
    c[9] = createCircle(
        new Offset(125, 210, example, AnimalScript.DIRECTION_SW), new Color(
            128, 255, 128), "9", circleText);
    c[10] = createCircle(new Offset(175, 210, example,
        AnimalScript.DIRECTION_SW), new Color(128, 255, 128), "10", circleText);
    c[11] = createCircle(new Offset(225, 210, example,
        AnimalScript.DIRECTION_SW), new Color(128, 255, 128), "11", circleText);
    c[12] = createCircle(new Offset(275, 210, example,
        AnimalScript.DIRECTION_SW), new Color(128, 255, 128), "12", circleText);
    c[13] = createCircle(new Offset(325, 210, example,
        AnimalScript.DIRECTION_SW), new Color(128, 255, 128), "13", circleText);
    c[14] = createCircle(new Offset(375, 210, example,
        AnimalScript.DIRECTION_SW), new Color(128, 255, 128), "14", circleText);

    Polyline[] line = new Polyline[14];
    line[0] = lang.newPolyline(new Node[] {
        new Offset(5, -5, c[0], AnimalScript.DIRECTION_SW),
        new Offset(-5, 5, c[1], AnimalScript.DIRECTION_NE) }, "line0", null);
    line[1] = lang.newPolyline(new Node[] {
        new Offset(-5, -5, c[0], AnimalScript.DIRECTION_SE),
        new Offset(5, 5, c[2], AnimalScript.DIRECTION_NW) }, "line1", null);
    line[2] = lang.newPolyline(new Node[] {
        new Offset(5, -5, c[1], AnimalScript.DIRECTION_SW),
        new Offset(-5, 5, c[3], AnimalScript.DIRECTION_NE) }, "line2", null);
    line[3] = lang.newPolyline(new Node[] {
        new Offset(-5, -5, c[1], AnimalScript.DIRECTION_SE),
        new Offset(5, 5, c[4], AnimalScript.DIRECTION_NW) }, "line3", null);
    line[4] = lang.newPolyline(new Node[] {
        new Offset(5, -5, c[2], AnimalScript.DIRECTION_SW),
        new Offset(-5, 5, c[5], AnimalScript.DIRECTION_NE) }, "line4", null);
    line[5] = lang.newPolyline(new Node[] {
        new Offset(-5, -5, c[2], AnimalScript.DIRECTION_SE),
        new Offset(5, 5, c[6], AnimalScript.DIRECTION_NW) }, "line5", null);
    line[6] = lang.newPolyline(new Node[] {
        new Offset(5, -5, c[3], AnimalScript.DIRECTION_SW),
        new Offset(0, 0, c[7], AnimalScript.DIRECTION_N) }, "line6", null);
    line[7] = lang.newPolyline(new Node[] {
        new Offset(-5, -5, c[3], AnimalScript.DIRECTION_SE),
        new Offset(0, 0, c[8], AnimalScript.DIRECTION_N) }, "line7", null);
    line[8] = lang.newPolyline(new Node[] {
        new Offset(5, -5, c[4], AnimalScript.DIRECTION_SW),
        new Offset(0, 0, c[9], AnimalScript.DIRECTION_N) }, "line8", null);
    line[9] = lang.newPolyline(new Node[] {
        new Offset(-5, -5, c[4], AnimalScript.DIRECTION_SE),
        new Offset(0, 0, c[10], AnimalScript.DIRECTION_N) }, "line9", null);
    line[10] = lang.newPolyline(new Node[] {
        new Offset(5, -5, c[5], AnimalScript.DIRECTION_SW),
        new Offset(0, 0, c[11], AnimalScript.DIRECTION_N) }, "line10", null);
    line[11] = lang.newPolyline(new Node[] {
        new Offset(-5, -5, c[5], AnimalScript.DIRECTION_SE),
        new Offset(0, 0, c[12], AnimalScript.DIRECTION_N) }, "line11", null);
    line[12] = lang.newPolyline(new Node[] {
        new Offset(5, -5, c[6], AnimalScript.DIRECTION_SW),
        new Offset(0, 0, c[13], AnimalScript.DIRECTION_N) }, "line12", null);
    line[13] = lang.newPolyline(new Node[] {
        new Offset(-5, -5, c[6], AnimalScript.DIRECTION_SE),
        new Offset(0, 0, c[14], AnimalScript.DIRECTION_N) }, "line13", null);

    IntArray[] ia = new IntArray[15];

    exampleArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(158,
        149, 242));
    ia[0] = lang.newIntArray(
        new Offset(150, 0, c[2], AnimalScript.DIRECTION_E),
        new int[] { exampleArray[0] }, "exampleArray", null, exampleArrayProps);
    for (int i = 1; i < 15; i++) {
      if (i < 3)
        exampleArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(
            255, 255, 136));
      else if (i < 7)
        exampleArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(
            255, 128, 128));
      else
        exampleArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(
            128, 255, 128));
      ia[i] = lang.newIntArray(new Offset(0, 0, ia[i - 1],
          AnimalScript.DIRECTION_NE), new int[] { exampleArray[i] },
          "exampleArray", null, exampleArrayProps);
    }

    lang.nextStep("Beschreibung");
    for (int i = 0; i < DESCRIPTION.length; i++) {
      des[i].hide();
    }
    example.hide();
    for (int i = 0; i < 15; i++) {
      ia[i].hide();
      c[i].hide();
      circleText[i].hide();
      if (i < 14)
        line[i].hide();
    }
  }

  private Circle createCircle(Offset position, Color c, String value,
      Text[] circleText) {
    circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, c);
    Circle circle = lang.newCircle(position, 20, "Circle" + circleCounter,
        null, circleProps);
    circleText[circleCounter] = lang.newText(new Offset(11, 5, circle,
        AnimalScript.DIRECTION_NW), value, "circleText" + circleCounter, null,
        circleTextProps);
    circleCounter++;

    return circle;
  }

  private void showAlgorithm(String[] arr, int[] myArray) {
    int[] array = myArray;
    n = arr.length;
    sa = lang.newStringArray(new Offset(0, 100, header,
        AnimalScript.DIRECTION_SW), arr, "arr", null, arrayProps);
    graph = createGraph(array, n, yDistance, yDistanceToArray, sa);

    sc_header = lang.newText(new Offset(100, -220, graph,
        AnimalScript.DIRECTION_NE), "Sourcecode", "sourceCodeHeader", null,
        headlineProps);
    sc = lang.newSourceCode(new Offset(0, 5, sc_header,
        AnimalScript.DIRECTION_SW), "sourceCode", null, sourceCodeProps);
    for (int i = 0; i < SOURCE_CODE.length; i++) {
      sc.addCodeLine(SOURCE_CODE[i], null, 0, null);
    }

    hints = lang.newText(new Offset(50, -50, sc, AnimalScript.DIRECTION_NE),
        "Hinweise:", "hints", null, headlineProps);
    explanation1 = lang.newText(new Offset(0, 5, hints,
        AnimalScript.DIRECTION_SW), "", "explanation1", null, normalTextProps);
    explanation2 = lang.newText(new Offset(0, 5, explanation1,
        AnimalScript.DIRECTION_SW), "", "explanation2", null, normalTextProps);
    explanation3 = lang.newText(new Offset(0, 5, explanation2,
        AnimalScript.DIRECTION_SW), "", "explanation3", null, normalTextProps);
    text_n = lang.newText(new Offset(0, 20, explanation3,
        AnimalScript.DIRECTION_SW), "n = " + String.valueOf(n), "n", null,
        normalTextProps);

    twoValueCounter = lang.newCounter(sa);
    TwoValueView tvv = lang.newCounterView(twoValueCounter, new Offset(0, 300,
        text_n, AnimalScript.DIRECTION_SW), counterProps, true, true);

    MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
        "complexity");
    mcqm.setPrompt("In welcher Komplexitätsklasse ist das löschen aus einem Heap?");
    mcqm.addAnswer("O(n^2)", 0, "Falsch. Die richtige Antwort wäre O(log n).");
    mcqm.addAnswer("O(n log n)", 0,
        "Falsch. Die richtige Antwort wäre O(log n).");
    mcqm.addAnswer("O(log n)", 1, "Richtig.");
    mcqm.addAnswer("O(n)", 0, "Falsch. Die richtige Antwort wäre O(log n).");
    lang.addMCQuestion(mcqm);

    lang.nextStep();

    sc.highlight(0);

    lang.nextStep();

    sc.toggleHighlight(0, 1);

    if (n > 0) {
      counterComps++;
      lang.nextStep();

      sc.toggleHighlight(1, 2);
      result = Integer.parseInt(sa.getData(0));
      text_result = lang.newText(new Offset(0, 5, text_n,
          AnimalScript.DIRECTION_SW), "result = " + result, "result", null,
          normalTextProps);
      text_result.changeColor(null, highlightColor, null, null);
      explanation1.setText("Das Datenfeld der Wurzel wird in", null, null);
      explanation2.setText("result abgespeichert.", null, null);

      lang.nextStep("Initialisierung");

      sc.toggleHighlight(2, 3);
      array[0] = array[n - 1];
      sa.put(0, sa.getData(n - 1), null, null);
      sa.highlightCell(0, null, null);
      sa.highlightElem(0, null, null);
      text_result.changeColor(null, Color.BLACK, null, null);
      graph = createGraph(array, n, yDistance, yDistanceToArray, sa);
      graph.highlightNode(0, null, null);
      explanation1.setText("Die Wurzel wird durch das", null, null);
      explanation2.setText("letzte Arrayelement ersetzt.", null, null);

      lang.nextStep();

      k = 0;
      sc.toggleHighlight(3, 4);
      sa.unhighlightCell(0, null, null);
      sa.unhighlightElem(0, null, null);
      graph.unhighlightNode(0, null, null);
      text_k = lang.newText(new Offset(0, 5, text_result,
          AnimalScript.DIRECTION_SW), "k = 0", "k", null, normalTextProps);
      text_k.changeColor(null, highlightColor, null, null);
      marker = lang.newArrayMarker(sa, k, "marker", null, arrayMarkerProps);

      lang.nextStep();

      n--;
      sc.toggleHighlight(4, 5);
      text_n.setText("n = " + String.valueOf(n), null, null);
      text_n.changeColor(null, highlightColor, null, null);
      text_k.changeColor(null, Color.BLACK, null, null);

      int[] array2 = new int[n];
      System.arraycopy(array, 0, array2, 0, n);
      array = array2;
      sa.hide();
      arr[n] = "";
      sa = lang.newStringArray(new Offset(0, 100, header,
          AnimalScript.DIRECTION_SW), arr, "arr2", null, arrayProps);
      graph = createGraph(array, n, yDistance, yDistanceToArray, sa);
      marker = lang.newArrayMarker(sa, k, "marker", null, arrayMarkerProps);
      twoValueCounter = lang.newCounter(sa);
      twoValueCounter.addCounterToView(tvv);

      lang.nextStep();

      sc.toggleHighlight(5, 6);
      text_n.changeColor(null, Color.BLACK, null, null);
      text_ready = lang.newText(new Offset(100, 0, text_n,
          AnimalScript.DIRECTION_NE), "ready = false", "ready", null,
          normalTextProps);
      text_ready.changeColor(null, highlightColor, null, null);
      explanation1.setText("", null, null);
      explanation2.setText("", null, null);

      lang.nextStep();

      text_ready.changeColor(null, Color.BLACK, null, null);
      sc.toggleHighlight(6, 7);

      counterAllocs += 5;

      while ((k + 1) * 2 - 1 < n && !ready) {
        counterComps++;
        counterIterations++;
        equation = lang.newText(new Offset(0, 30, text_k,
            AnimalScript.DIRECTION_SW), "(k+1)*2-1 = " + ((k + 1) * 2 - 1)
            + " < " + n + " = n", "equation", null, normalTextProps);
        equation.changeColor(null, highlightColor, null, null);
        equation2 = lang.newText(new Offset(0, 5, equation,
            AnimalScript.DIRECTION_SW), "", "equation2", null, normalTextProps);
        equation2.changeColor(null, highlightColor, null, null);
        explanation1.setText("Das aktuell betrachtete Element hat", null, null);
        explanation2.setText("mindestens einen Kindknoten.", null, null);
        explanation3.setText("", null, null);

        mcqm = new MultipleChoiceQuestionModel("interchange"
            + counterIterations);
        mcqm.setPrompt("Wird das Element an der k-ten Stelle mit einem anderen Element vertauscht? Wenn ja mit welchem?");
        if ((k + 1) * 2 < n && array[(k + 1) * 2] < array[(k + 1) * 2 - 1]
            && array[(k + 1) * 2] < array[k]) {
          mcqm.addAnswer("Nein, es wird nicht vertauscht.", 0,
              "Falsch. Es wird mit dem Element im rechten Unterbaum vertauscht.");
          mcqm.addAnswer(
              "Ja, es wird mit dem Element im linken Unterbaum vertauscht.", 0,
              "Falsch. Es wird mit dem Element im rechten Unterbaum vertauscht.");
          mcqm.addAnswer(
              "Ja, es wird mit dem Element im rechten Unterbaum vertauscht.",
              1, "Richtig.");
          mcqm.addAnswer(
              "Ja, es wird mit dem Vaterknoten vertauscht.",
              0,
              "Falsch. Es wird mit dem Element im rechten Unterbaum vertauscht. Mit dem Vaterknoten wird es nie vertauscht.");
        } else if (array[(k + 1) * 2 - 1] < array[k]) {
          mcqm.addAnswer("Nein, es wird nicht vertauscht.", 0,
              "Falsch. Es wird mit dem Element im linken Unterbaum vertauscht.");
          mcqm.addAnswer(
              "Ja, es wird mit dem Element im rechten Unterbaum vertauscht.",
              0,
              "Falsch. Es wird mit dem Element im linken Unterbaum vertauscht.");
          mcqm.addAnswer(
              "Ja, es wird mit dem Element im linken Unterbaum vertauscht.", 1,
              "Richtig.");
          mcqm.addAnswer(
              "Ja, es wird mit dem Vaterknoten vertauscht.",
              0,
              "Falsch. Es wird mit dem Element im linken Unterbaum vertauscht. Mit dem Vaterknoten wird es nie vertauscht.");
        }
        lang.addMCQuestion(mcqm);

        lang.nextStep("Iteration " + counterIterations);

        sc.toggleHighlight(7, 8);
        equation.setText("", null, null);
        equation2.setText("", null, null);
        explanation1.setText("", null, null);
        explanation2.setText("", null, null);

        lang.nextStep();

        sc.toggleHighlight(8, 9);

        sa.highlightCell((k + 1) * 2, null, null);
        sa.highlightElem((k + 1) * 2, null, null);
        sa.highlightCell((k + 1) * 2 - 1, null, null);
        sa.highlightElem((k + 1) * 2 - 1, null, null);
        graph.highlightNode((k + 1) * 2, null, null);
        graph.highlightNode((k + 1) * 2 - 1, null, null);

        explanation1.setText("index wird auf den Index", null, null);
        explanation2.setText("des kleinsten Kindknoten gesetzt.", null, null);

        if ((k + 1) * 2 < n
            && Integer.parseInt(sa.getData((k + 1) * 2)) < Integer.parseInt(sa
                .getData((k + 1) * 2 - 1))) {
          equation.setText("array[(k+1)*2] = array[" + ((k + 1) * 2) + "] = "
              + array[(k + 1) * 2], null, null);
          equation2.setText("< " + array[(k + 1) * 2 - 1] + " = array["
              + ((k + 1) * 2 - 1) + "] = array[(k+1)*2-1]", null, null);
          counterComps += 2;

          lang.nextStep();

          sc.toggleHighlight(9, 10);

          sa.unhighlightElem((k + 1) * 2, null, null);
          sa.unhighlightCell((k + 1) * 2 - 1, null, null);
          sa.unhighlightElem((k + 1) * 2 - 1, null, null);
          graph.unhighlightNode((k + 1) * 2 - 1, null, null);
          equation.setText("", null, null);
          equation2.setText("", null, null);

          index = (k + 1) * 2;

          text_index = lang.newText(new Offset(0, 5, text_ready,
              AnimalScript.DIRECTION_SW), "index = " + index, "index", null,
              normalTextProps);
          text_index.changeColor(null, highlightColor, null, null);

          lang.nextStep();

          sc.toggleHighlight(10, 13);
        } else {
          if ((k + 1) * 2 >= n) {
            equation.setText(
                "!((k+1)*2 = " + (k + 1) * 2 + " < " + n + " = n)", null, null);
            counterComps++;
          } else {
            equation.setText("!(array[(k+1)*2] = array[" + ((k + 1) * 2)
                + "] = " + array[(k + 1) * 2], null, null);
            equation2.setText("< " + array[(k + 1) * 2 - 1] + " = array["
                + ((k + 1) * 2 - 1) + "] = array[(k+1)*2-1])", null, null);
            counterComps += 2;
          }

          lang.nextStep();

          sc.toggleHighlight(9, 11);
          sa.unhighlightCell((k + 1) * 2, null, null);
          sa.unhighlightElem((k + 1) * 2, null, null);
          sa.unhighlightElem((k + 1) * 2 - 1, null, null);
          graph.unhighlightNode((k + 1) * 2, null, null);
          equation.setText("", null, null);
          equation2.setText("", null, null);

          lang.nextStep();

          index = (k + 1) * 2 - 1;
          sc.toggleHighlight(11, 12);
          text_index = lang.newText(new Offset(0, 5, text_ready,
              AnimalScript.DIRECTION_SW), "index = " + index, "index", null,
              normalTextProps);
          text_index.changeColor(null, highlightColor, null, null);

          lang.nextStep();

          sc.toggleHighlight(12, 13);
        }

        counterAllocs++;

        text_index.changeColor(null, Color.BLACK, null, null);
        sa.highlightCell(k, null, null);
        sa.highlightElem(k, null, null);
        sa.highlightElem(index, null, null);
        graph.highlightNode(k, null, null);

        counterComps++;
        if (Integer.parseInt(sa.getData(k)) > Integer.parseInt(sa
            .getData(index))) {
          equation.setText("array[k] = " + array[k] + " > " + array[index]
              + " = array[index]", null, null);

          explanation1.setText("Da das Element größer als der", null, null);
          explanation2.setText("kleinste Kindknoten ist, wird es", null, null);
          explanation3.setText("mit diesem vertauscht.", null, null);

          if (index == (k + 1) * 2
              && array[(k + 1) * 2] < array[(k + 1) * 2 - 1]
              && array[(k + 1) * 2 - 1] < array[k]
              && array[(k + 1) * 2] < array[k]) {
            mcqm = new MultipleChoiceQuestionModel("wrongElement");
            mcqm.setPrompt("Darf das Element an der Stelle k auch mit dem Element im linken Teilbaum vertauscht werden?");
            mcqm.addAnswer(
                "Ja, das Element an der Stelle k ist größer, als das Element im linken Unterbaum.",
                0, "Falsch. ");
            mcqm.addAnswer(
                "Nein, da das Element im rechten Unterbaum kleiner ist, als das im linken, wäre die Heapeigenschaft zwischen diesen beiden Elementen verletzt.",
                1, "Richtig.");
            lang.addMCQuestion(mcqm);
          } else if (index == (k + 1) * 2
              && array[(k + 1) * 2] > array[(k + 1) * 2 - 1]
              && array[(k + 1) * 2 - 1] < array[k]
              && array[(k + 1) * 2] < array[k]) {
            mcqm = new MultipleChoiceQuestionModel("wrongElement");
            mcqm.setPrompt("Darf das Element an der Stelle k auch mit dem Element im rechten Teilbaum vertauscht werden?");
            mcqm.addAnswer(
                "Ja, das Element an der Stelle k ist größer, als das Element im rechten Unterbaum.",
                0, "Falsch. ");
            mcqm.addAnswer(
                "Nein, da das Element im linken Unterbaum kleiner ist, als das im rechten, wäre die Heapeigenschaft zwischen diesen beiden Elementen verletzt.",
                1, "Richtig.");
            lang.addMCQuestion(mcqm);
          }

          lang.nextStep();

          a = Integer.parseInt(sa.getData(k));
          sa.unhighlightElem(k, null, null);
          sa.unhighlightElem(index, null, null);
          sc.toggleHighlight(13, 14);
          text_a = lang.newText(new Offset(0, 5, text_index,
              AnimalScript.DIRECTION_SW), "a = " + a, "a", null,
              normalTextProps);
          text_a.changeColor(null, highlightColor, null, null);
          equation.setText("", null, null);
          equation2.setText("", null, null);

          lang.nextStep();

          sc.toggleHighlight(14, 15);
          text_a.changeColor(null, Color.BLACK, null, null);
          array[k] = array[index];
          sa.put(k, sa.getData(index), null, null);
          sa.highlightElem(k, null, null);
          graph = createGraph(array, n, yDistance, yDistanceToArray, sa);
          graph.highlightNode(k, null, null);
          graph.highlightNode(index, null, null);

          lang.nextStep();

          sc.toggleHighlight(15, 16);
          array[index] = a;
          sa.put(index, String.valueOf(a), null, null);
          sa.unhighlightElem(k, null, null);
          sa.highlightElem(index, null, null);
          graph = createGraph(array, n, yDistance, yDistanceToArray, sa);
          graph.highlightNode(k, null, null);
          graph.highlightNode(index, null, null);

          lang.nextStep();

          k = index;
          sc.toggleHighlight(16, 17);
          sa.unhighlightElem(index, null, null);
          text_k.setText("k = " + k, null, null);
          text_k.changeColor(null, highlightColor, null, null);
          marker.move(k, null, null);

          lang.nextStep();

          sa.unhighlightCell(k, null, null);
          sa.unhighlightCell((k + 1) / 2 - 1, null, null);
          graph.unhighlightNode(k, null, null);
          graph.unhighlightNode((k + 1) / 2 - 1, null, null);
          sc.toggleHighlight(17, 7);
          text_k.changeColor(null, Color.BLACK, null, null);

          counterAllocs += 4;
        } else {
          equation.setText("!(array[k] = " + array[k] + " > " + array[index]
              + " = array[index])", null, null);

          explanation1.setText("Das Element ist nicht größer als", null, null);
          explanation2.setText("der kleinste Kindknoten", null, null);

          lang.nextStep();

          sa.unhighlightCell(k, null, null);
          sa.unhighlightCell(index, null, null);
          sa.unhighlightElem(k, null, null);
          sa.unhighlightElem(index, null, null);
          graph.unhighlightNode(k, null, null);
          graph.unhighlightNode(index, null, null);
          sc.toggleHighlight(13, 19);

          lang.nextStep();

          sc.toggleHighlight(19, 20);
          ready = true;
          equation.setText("", null, null);
          equation2.setText("", null, null);
          text_ready.setText("ready = true", null, null);
          text_ready.changeColor(null, highlightColor, null, null);

          lang.nextStep();

          sc.toggleHighlight(20, 7);
          text_ready.changeColor(null, Color.BLACK, null, null);

          counterAllocs++;
        }

        text_a.hide();
        text_index.hide();
      }
      counterComps++;

      if (!((k + 1) * 2 - 1 < n)) {
        equation.setText("!((k+1)*2-1 = " + ((k + 1) * 2 - 1) + " < " + n
            + " = n)", null, null);
        explanation1.setText("Das Element besitzt keinen", null, null);
        explanation2.setText("Kindknoten", null, null);
        explanation3.setText("", null, null);
      } else {
        equation.setText("ready = true", null, null);
        explanation1.hide();
        explanation2.hide();
        explanation3.hide();
      }
      lang.nextStep();

      sc.toggleHighlight(7, 22);
      equation.hide();
      equation2.hide();
      explanation1.hide();
      explanation2.hide();

      lang.nextStep();

      sc.unhighlight(22);
      marker.hide();
      tvv.hide();
      text_ready.hide();
      text_k.hide();

      lang.nextStep("Endzustand");

      sc.hide();
      graph.hide();
      sa.hide();
      hints.hide();
      text_n.hide();
      text_result.hide();
      sc_header.hide();
    }
  }

  private void showConclusion() {

    Text c1 = lang.newText(
        new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "Es wurden "
            + counterAllocs + " Zuweisungen, " + counterComps
            + " Vergleiche und " + counterIterations
            + " Schleifendurchläufe benötigt.", "conclusion", null,
        conclusionProps);
    Text c2 = lang
        .newText(
            new Offset(0, 50, c1, AnimalScript.DIRECTION_SW),
            "In jeder Iteration, in welcher die Heapeigenschaft noch nicht erfüllt ist, wird k verdoppelt (bzw verdoppelt und um 1 verringert).",
            "conclusion2", null, conclusionProps);
    lang.newText(
        new Offset(0, 5, c2, AnimalScript.DIRECTION_SW),
        "Somit muss das Wiederherstellen der Heapeigenschaften nach dem ersetzen der Wurzel eine logarithmische Komplexität haben.",
        "conclusion3", null, conclusionProps);

    lang.nextStep("Fazit");
  }

  private Graph createGraph(int[] array, int n, int yDistance,
      int yDistanceToElem, Primitive elem) {
    if (graphCounter >= 0)
      graph.hide();
    graphCounter++;

    int[][] am = new int[n][n];
    for (int x = 0; x < n; x++) {
      for (int y = 0; y < n; y++) {
        if (x == (y + 1) / 2 - 1 || y == (x + 1) / 2 - 1)
          am[x][y] = 1;
        else
          am[x][y] = 0;
      }
    }

    String[] labels = new String[n];
    for (int i = 0; i < n; i++) {
      labels[i] = String.valueOf(array[i]);
    }

    Offset[] nodes = new Offset[n];

    int elementsOnLevel = 1;
    int elementsOnLevelPlaced = 0;
    int level = 0;
    for (int i = 0; i < n; i++) {
      if (elementsOnLevelPlaced >= elementsOnLevel) {
        level++;
        elementsOnLevel *= 2;
        elementsOnLevelPlaced = 0;
      }
      nodes[i] = new Offset((graphWidth / elementsOnLevel)
          * elementsOnLevelPlaced + (graphWidth / (elementsOnLevel * 2)), level
          * yDistance + yDistanceToElem, elem, AnimalScript.DIRECTION_SW);
      elementsOnLevelPlaced++;
    }
    return lang.newGraph("graph_" + graphCounter, am, nodes, labels, null,
        graphProps);
  }

  public void init() {
    lang = new AnimalScript("Heap: extractMinimum [DE]", "Michael Sandforth",
        800, 600);
    counterComps = 0;
    counterAllocs = 0;
    counterIterations = 0;
    circleCounter = 0;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    normalTextProps = (TextProperties) props.getPropertiesByName("text");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    descriptionProps = (TextProperties) props
        .getPropertiesByName("description");
    int[] intArray = (int[]) primitives.get("intArray");
    headlineProps = (TextProperties) props.getPropertiesByName("subtitle");
    conclusionProps = (TextProperties) props.getPropertiesByName("conclusion");
    headerProps = (TextProperties) props.getPropertiesByName("header");
    arrayMarkerProps = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker");
    arrayProps = (ArrayProperties) props.getPropertiesByName("array");

    String[] stringArray = new String[intArray.length];
    for (int i = 0; i < intArray.length; i++)
      stringArray[i] = String.valueOf(intArray[i]);

    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    extractMinimum(stringArray, intArray);

    lang.finalizeGeneration();

    return lang.toString();
  }

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[] intArray = (int[]) primitives.get("intArray");
    if (intArray.length <= 1)
      throw new IllegalArgumentException(
          "Das Array muss mindestens 2 Elemente enthalten.");

    return true;
  }

  public String getName() {
    return "Heap: extractMinimum [DE]";
  }

  public String getAlgorithmName() {
    return "Heap";
  }

  public String getAnimationAuthor() {
    return "Michael Sandforth";
  }

  public String getDescription() {
    return "Ein Heap ist ein partiell geordneter Baum, bei dem alle Kinder eines Knotens N gr&ouml;&szlig;er oder gleich sind, wie N. Heaps werden h&auml;ufig dann verwendet, "
        + "\n"
        + "wenn es darauf ankommt schnell ein Element mit der h&ouml;chsten Priorit&auml;t auszuw&auml;hlen und zu entfernen. Sowohl das Einf&uuml;en eines Elementes, "
        + "\n"
        + "als auch das Entfernen, haben eine logarithmische Komplexit&auml;t. H&auml;ufig werden Heaps mit der Hilfe von Arrays implementiert."
        + "\n"
        + "\n"
        + "Das erste Arrayelement ist immer das kleinste Element. Deshalb wird bei der Methode extractMinimum() zuerst das erste Element durch das letzte"
        + "\n"
        + "ersetzt und solange wie dieses Element gr&ouml;&szlig;er ist als einer der Kindsknoten, wird dieses Element mit dem kleinsten Kindsknoten vertauscht.";
  }

  public String getCodeExample() {
    return "public int extractMinimum(){" + "\n" + "    if(n>0){" + "\n"
        + "        int result = array[0];" + "\n"
        + "        array[0] = array[n-1];" + "\n" + "        int k = 0;" + "\n"
        + "        n--;" + "\n" + "        boolean ready = false;" + "\n"
        + "        while((k+1)*2-1<n && !ready){" + "\n"
        + "            int index;" + "\n"
        + "            if((k+1)*2<n && array[(k+1)*2] < array[(k+1)*2-1])"
        + "\n" + "                index = (k+1)*2;" + "\n" + "            else"
        + "\n" + "                index = (k+1)*2-1;" + "\n"
        + "            if(array[k] > array[index]){" + "\n"
        + "                int a = array[k];" + "\n"
        + "                array[k] = array[index];" + "\n"
        + "                array[index] = a;" + "\n"
        + "                k = index;" + "\n" + "            }" + "\n"
        + "            else" + "\n" + "                ready = true;" + "\n"
        + "        }" + "\n" + "        return result;" + "\n" + "    }" + "\n"
        + "    return -1;" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}