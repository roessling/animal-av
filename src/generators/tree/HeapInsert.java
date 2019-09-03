package generators.tree;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Graph;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Node;
import algoanim.primitives.Text;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.*;

public class HeapInsert implements Generator {

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
      "Beim Einfügen wird das eingefügte Element am Ende des Arrays hinzugefügt. ",
      "Danach wird es so lange mit dem Vaterknoten vertauscht, bis es die Wurzel ist oder bis der Vaterknoten kleiner ist." };

  private static final String[] SOURCE_CODE       = {
      "public void insert(int number){", "    array[n] = number;",
      "    int k = n;", "    n++;",
      "    while(k != 0 && array[(k+1)/2-1] > array[k]){",
      "        int a = array[k];", "        array[k] = array[(k+1)/2-1];",
      "        array[(k+1)/2-1] = a;", "        k = (k+1)/2-1;", "    }", "}" };

  private static final String   HEADER            = "Heap: insert";

  private static final Color    highlightColor    = Color.RED;

  private static final int      yDistance         = 70;
  private static final int      yDistanceToArray  = 100;

  private ArrayProperties       arrayProps;
  private ArrayMarkerProperties arrayMarkerProps;
  private TextProperties        headerProps, normalTextProps, descriptionProps,
      headlineProps, conclusionProps, circleTextProps;
  private GraphProperties       graphProps;
  private SourceCodeProperties  sourceCodeProps;
  private ArrayProperties       exampleArrayProps;
  private CircleProperties      circleProps;
  private CounterProperties     counterProps;

  private StringArray           sa;
  private Graph                 graph;
  private Text                  header, hints, text_n, text_number, text_k,
      text_k2, equation, text_a, explanation1, explanation2;
  private SourceCode            sc;
  private ArrayMarker           marker;
  private int                   graphCounter, graphWidth;

  private int                   counterComps      = 0;
  private int                   counterAllocs     = 0;
  private int                   counterIterations = 0;
  private int                   circleCounter     = 0;
  private TwoValueCounter       twoValueCounter;

  private int                   n, k;

  public void init() {
    lang = new AnimalScript("Heap: insert [DE]", "Michael Sandforth", 800, 600);

    counterComps = 0;
    counterAllocs = 0;
    counterIterations = 0;
    circleCounter = 0;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    headerProps = (TextProperties) props.getPropertiesByName("title");
    normalTextProps = (TextProperties) props.getPropertiesByName("text");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    descriptionProps = (TextProperties) props
        .getPropertiesByName("description");
    int[] intArray = (int[]) primitives.get("intArray");
    headlineProps = (TextProperties) props.getPropertiesByName("subtitle");
    conclusionProps = (TextProperties) props.getPropertiesByName("conclusion");
    int addedElement = (Integer) primitives.get("addedElement");
    arrayMarkerProps = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker");
    arrayProps = (ArrayProperties) props.getPropertiesByName("array");

    String[] stringArray = new String[intArray.length + 1];
    for (int i = 0; i < intArray.length; i++)
      stringArray[i] = String.valueOf(intArray[i]);
    stringArray[stringArray.length - 1] = " ";

    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    insert(stringArray, addedElement);

    lang.finalizeGeneration();

    return lang.toString();
  }

  public void insert(String[] a, int number) {
    setProperties();

    header = lang.newText(new Coordinates(20, 20), HEADER, "header", null,
        headerProps);
    graphCounter = -1;

    graphWidth = 120;
    int b = a.length + 1;
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
    showAlgorithm(a, number);
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

  private void showAlgorithm(String[] arr, int number) {
    n = arr.length - 1;
    int[] array = new int[arr.length];
    for (int i = 0; i < arr.length - 1; i++)
      array[i] = Integer.parseInt(arr[i]);

    sa = lang.newStringArray(new Offset(0, 100, header,
        AnimalScript.DIRECTION_SW), arr, "arr", null, arrayProps);

    graph = createGraph(array, n, yDistance, yDistanceToArray, sa);

    sc = lang.newSourceCode(new Offset(150, -220, graph,
        AnimalScript.DIRECTION_NE), "sourceCode", null, sourceCodeProps);
    for (int i = 0; i < SOURCE_CODE.length; i++) {
      sc.addCodeLine(SOURCE_CODE[i], null, 0, null);
    }

    twoValueCounter = lang.newCounter(sa);
    TwoValueView tvv = lang.newCounterView(twoValueCounter, new Offset(100, 0,
        sc, AnimalScript.DIRECTION_NE), counterProps, true, true);

    hints = lang.newText(new Offset(0, 50, sc, AnimalScript.DIRECTION_SW),
        "Hinweise:", "hints", null, headlineProps);
    explanation1 = lang.newText(new Offset(0, 5, hints,
        AnimalScript.DIRECTION_SW), "", "explanation1", null, normalTextProps);
    explanation2 = lang.newText(new Offset(0, 5, explanation1,
        AnimalScript.DIRECTION_SW), "", "explanation2", null, normalTextProps);
    text_n = lang.newText(new Offset(0, 20, explanation2,
        AnimalScript.DIRECTION_SW), "n = " + String.valueOf(n), "n", null,
        normalTextProps);

    MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
        "complexity");
    mcqm.setPrompt("In welcher Komplexitätsklasse ist das einfügen in einen Heap?");
    mcqm.addAnswer("O(n^2)", 0, "Falsch. Die richtige Antwort wäre O(log n).");
    mcqm.addAnswer("O(n log n)", 0,
        "Falsch. Die richtige Antwort wäre O(log n).");
    mcqm.addAnswer("O(log n)", 1, "Richtig.");
    mcqm.addAnswer("O(n)", 0, "Falsch. Die richtige Antwort wäre O(log n).");
    lang.addMCQuestion(mcqm);

    lang.nextStep();

    sc.highlight(0);
    text_number = lang.newText(
        new Offset(0, 5, "n", AnimalScript.DIRECTION_SW), "number = " + number,
        "number", null, normalTextProps);
    text_number.changeColor(null, highlightColor, null, null);

    lang.nextStep();

    sc.toggleHighlight(0, 1);
    sa.put(n, String.valueOf(number), null, null);
    sa.highlightCell(n, null, null);
    sa.highlightElem(n, null, null);
    text_number.changeColor(null, Color.BLACK, null, null);
    array[n] = number;
    graph = createGraph(array, n + 1, yDistance, yDistanceToArray, sa);
    graph.highlightNode(n, null, null);
    explanation1 = lang.newText(new Offset(0, 5, hints,
        AnimalScript.DIRECTION_SW), "Als erstes wird das einzufügende Element",
        "explanation1", null, normalTextProps);
    explanation2 = lang.newText(new Offset(0, 5, explanation1,
        AnimalScript.DIRECTION_SW),
        "hinter dem letzten Arrayeintrag eingefügt.", "explanation2", null,
        normalTextProps);

    lang.nextStep("Initialisierung");

    k = n;
    sc.toggleHighlight(1, 2);
    sa.unhighlightCell(n, null, null);
    sa.unhighlightElem(n, null, null);
    text_k = lang.newText(
        new Offset(0, 5, "number", AnimalScript.DIRECTION_SW),
        "k = " + String.valueOf(n), "k", null, normalTextProps);
    text_k.changeColor(null, highlightColor, null, null);
    text_k2 = lang.newText(new Offset(100, 0, "k", AnimalScript.DIRECTION_NE),
        "(k+1)/2-1 = " + ((k + 1) / 2 - 1), "k2", null, normalTextProps);
    marker = lang.newArrayMarker(sa, k, "marker", null, arrayMarkerProps);
    graph.unhighlightNode(n, null, null);

    lang.nextStep();

    n++;
    sc.toggleHighlight(2, 3);
    text_n.setText("n = " + String.valueOf(n), null, null);
    text_k.changeColor(null, Color.BLACK, null, null);
    text_n.changeColor(null, highlightColor, null, null);
    counterAllocs += 3;

    lang.nextStep();

    boolean ready = false;
    sc.unhighlight(3);
    text_n.changeColor(null, Color.BLACK, null, null);

    while (!ready) {

      mcqm = new MultipleChoiceQuestionModel("heapProperty");
      mcqm.setPrompt("Zwischen welchen Elementen kann es passieren, dass nach dem Vertauschen die Heapeigenschaft nicht erfüllt ist?");
      mcqm.addAnswer(
          "Eingefügtes Element und anderer Kindknoten",
          0,
          "Falsch. Zwischen dem alten Vaterknoten und einem Kindknoten (der nicht das eingefügte Element beinhaltet), besteht die Heapeigenschaft. Wenn der Vaterknoten V kleiner ist als sein Kindknoten K und der eingefügte Knoten E kleiner ist als V, dann muss E auch kleiner sein als K sein (Transitivität).");
      mcqm.addAnswer("Eingefügtes Element und neuer Vaterknoten", 1, "Richtig.");
      lang.addMCQuestion(mcqm);

      sc.highlight(4);
      sa.highlightCell(k, null, null);
      sa.highlightCell((k + 1) / 2 - 1, null, null);
      sa.highlightElem(k, null, null);
      sa.highlightElem((k + 1) / 2 - 1, null, null);
      graph.highlightNode(k, null, null);
      graph.highlightNode((k + 1) / 2 - 1, null, null);
      if (k != 0
          && Integer.parseInt(sa.getData((k + 1) / 2 - 1)) > Integer
              .parseInt(sa.getData(k))) {
        explanation1.setText("Da das eingefügte Element kleiner ist als sein",
            null, null);
        explanation2.setText(
            "Vaterknoten, muss es mit diesem vertauscht werden.", null, null);
        equation = lang.newText(
            new Offset(0, 5, "k", AnimalScript.DIRECTION_SW),
            "array[(k+1)/2-1] = array[" + ((k + 1) / 2 - 1) + "] = "
                + array[(k + 1) / 2 - 1] + " > " + array[k] + " = array[" + k
                + "] = array[k]", "equation", null, normalTextProps);
        equation.changeColor(null, highlightColor, null, null);
        counterComps += 2;
        counterIterations++;

        lang.nextStep("Iteration " + counterIterations);

        int a = Integer.parseInt(sa.getData(k));
        equation.hide();
        sc.toggleHighlight(4, 5);
        sa.unhighlightElem(k, null, null);
        sa.unhighlightElem((k + 1) / 2 - 1, null, null);
        text_a = lang.newText(new Offset(0, 5, "k", AnimalScript.DIRECTION_SW),
            "a = " + String.valueOf(a), "a", null, normalTextProps);
        text_a.changeColor(null, highlightColor, null, null);

        lang.nextStep();

        array[k] = Integer.parseInt(sa.getData((k + 1) / 2 - 1));
        sc.toggleHighlight(5, 6);
        text_a.changeColor(null, Color.BLACK, null, null);
        sa.put(k, String.valueOf(array[(k + 1) / 2 - 1]), null, null);
        sa.highlightElem(k, null, null);
        graph = createGraph(array, n, yDistance, yDistanceToArray, sa);
        graph.highlightNode(k, null, null);
        graph.highlightNode((k + 1) / 2 - 1, null, null);

        lang.nextStep();

        array[(k + 1) / 2 - 1] = a;
        sc.toggleHighlight(6, 7);
        sa.unhighlightElem(k, null, null);
        sa.put((k + 1) / 2 - 1, String.valueOf(a), null, null);
        sa.highlightElem((k + 1) / 2 - 1, null, null);
        graph = createGraph(array, n, yDistance, yDistanceToArray, sa);
        graph.highlightNode(k, null, null);
        graph.highlightNode((k + 1) / 2 - 1, null, null);

        lang.nextStep();

        sa.unhighlightCell(k, null, null);
        graph.unhighlightNode(k, null, null);
        k = (k + 1) / 2 - 1;
        sc.toggleHighlight(7, 8);
        text_k.setText("k = " + String.valueOf(k), null, null);
        text_k2.setText("(k+1)/2-1 = " + String.valueOf((k + 1) / 2 - 1), null,
            null);
        text_k.changeColor(null, highlightColor, null, null);
        marker.move(k, null, null);
        counterAllocs += 4;

        mcqm = new MultipleChoiceQuestionModel("heapProperty"
            + counterIterations);
        mcqm.setPrompt("Ist die Heapeigenschaft nun erfüllt?");
        if (k != 0
            && Integer.parseInt(sa.getData((k + 1) / 2 - 1)) > Integer
                .parseInt(sa.getData(k))) {
          mcqm.addAnswer(
              "Ja",
              0,
              "Falsch. Der Vaterknoten des eingefügten Elements ist größer, als das eingefügte Element.");
          mcqm.addAnswer(
              "Nein",
              1,
              "Richtig. Der Vaterknoten des eingefügten Elements ist größer, als das eingefügte Element.");
        } else {
          mcqm.addAnswer(
              "Ja",
              1,
              "Richtig. Für jeden Knoten gilt, dass seine Kindsknoten größer oder gleich sind, als er selbst.");
          mcqm.addAnswer(
              "Nein",
              0,
              "Falsch. Für jeden Knoten gilt, dass seine Kindsknoten größer oder gleich sind, als er selbst.");
        }

        lang.addMCQuestion(mcqm);

        lang.nextStep();

        sc.unhighlight(8);
        sa.unhighlightCell(k, null, null);
        sa.unhighlightElem(k, null, null);
        graph.unhighlightNode(k, null, null);
        text_k.changeColor(null, Color.BLACK, null, null);
        text_a.hide();
      } else if (k == 0) {
        explanation1.setText("Das eingefügte Element ist nun die Wurzel.",
            null, null);
        explanation2.setText("", null, null);
        ready = true;
        equation = lang.newText(
            new Offset(0, 5, "k", AnimalScript.DIRECTION_SW), "k == 0",
            "equation", null, normalTextProps);
        equation.changeColor(null, highlightColor, null, null);
        counterComps++;
      } else {
        explanation1.setText(
            "Da das eingefügte Element nicht kleiner ist als sein", null, null);
        explanation2.setText(
            "Vaterknoten, müssen diese nicht vertauscht werden.", null, null);
        ready = true;
        equation = lang.newText(
            new Offset(0, 5, "k", AnimalScript.DIRECTION_SW),
            "!(array[(k+1)/2-1] = array[" + ((k + 1) / 2 - 1) + "] = "
                + array[(k + 1) / 2 - 1] + " > " + array[k] + " = array[" + k
                + "] = array[k])", "equation", null, normalTextProps);
        equation.changeColor(null, highlightColor, null, null);
        counterComps += 2;
      }
    }
    lang.nextStep();

    sc.unhighlight(4);
    sa.unhighlightCell((k + 1) / 2 - 1, null, null);
    sa.unhighlightCell(k, null, null);
    sa.unhighlightElem(0, k, null, null);
    graph.unhighlightNode(k, null, null);
    graph.unhighlightNode((k + 1) / 2 - 1, null, null);
    equation.hide();
    text_k.hide();
    text_k2.hide();
    text_number.hide();
    marker.hide();
    explanation1.hide();
    explanation2.hide();

    lang.nextStep("Endzustand");

    sc.hide();
    hints.hide();
    text_n.hide();
    sa.hide();
    graph.hide();
    tvv.hide();
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
            "In jeder Iteration wird die Position des eingefügten Elements halbiert.",
            "conclusion2", null, conclusionProps);
    lang.newText(
        new Offset(0, 5, c2, AnimalScript.DIRECTION_SW),
        "Dies hat zur Folge, dass das Einfügen eines Elements in einen Heap eine logarithmische Komplexität hat.",
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

  public String getName() {
    return "Heap: insert [DE]";
  }

  public String getAlgorithmName() {
    return "Heap";
  }

  public String getAnimationAuthor() {
    return "Michael Sandforth";
  }

  public String getDescription() {
    return "Ein Heap ist ein partiell geordneter Baum, bei dem alle Kinder eines Knotens N gr&ouml;&szlig;er oder gleich sind, wie N. Heaps werden h&auml;ufig dann verwendet,"
        + "\n"
        + "wenn es darauf ankommt schnell ein Element mit der h&ouml;chsten Priorit&auml;t auszuw&auml;hlen und zu entfernen. Sowohl das Einf&uuml;gen eines Elementes, als auch"
        + "\n"
        + "das Entfernen, haben eine logarithmische Koplexit&auml;t. H&auml;ufig werden Heaps mit der Hilfe von Arrays implementiert."
        + "\n"
        + "\n"
        + "Beim Einf&uuml;gen wird das eingef&uuml;gte Element am Ende des Arrays hinzugef&uuml;gt."
        + "\n"
        + "Danach wird es so lange mit dem Vaterknoten vertauscht, bis es die Wurzel ist oder bis der Vaterknoten kleiner ist.";
  }

  public String getCodeExample() {
    return "public void insert(int number){" + "\n" + "    array[n] = number;"
        + "\n" + "    int k = n;" + "\n" + "    n++;" + "\n"
        + "    while(k != 0 && array[(k+1)/2-1] > array[k]){" + "\n"
        + "        int a = array[k];" + "\n"
        + "        array[k] = array[(k+1)/2-1];" + "\n"
        + "        array[(k+1)/2-1] = a;" + "\n" + "        k = (k+1)/2-1;"
        + "\n" + "   }" + "\n" + "}";
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
