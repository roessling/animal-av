package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class GaussFilter implements Generator {
  private Language             lang;
  private Color                srcCenterHighlightColor;
  private Color                srcBorderHighlightColor;
  private Color                axisHighlightColor;
  private SourceCodeProperties sourceCodeProps;
  private int[][]              src;
  private Color                dstHighlightColor;
  private TextProperties       headerProperties;
  SourceCodeProperties         calculationProps;
  private double               varianz;
  private int                  size;
  private Color                filterMatrixHighlightColor;

  // Klassenvariaben von Objekten die vorher einen zu kleinen Scope hatten

  SourceCode                   sourceCode;
  Rect[][]                     dstRectArray;
  Rect[][]                     srcRectArray;
  Text[][]                     dstTextArray;
  Text[][]                     srcTextArray;
  Point                        srcBenchmark;
  Point                        dstBenchmark;
  TextProperties               arrayValuesTextProperty;
  Rect[][]                     filterRectArray;
  double[][]                   filterMatrix;
  Text[][]                     filterTextArray;

  java.text.DecimalFormat      doubleFormat = new java.text.DecimalFormat(
                                                "#0.###");

  public void init() {
    lang = new AnimalScript("Gauss Filter",
        "Tobias Otterbein, Florian Reimold", 1024, 1024);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    src = (int[][]) primitives.get("src");
    srcCenterHighlightColor = (Color) primitives.get("srcCenterHighlightColor");
    filterMatrixHighlightColor = (Color) primitives
        .get("filterMatrixHighlightColor");
    srcBorderHighlightColor = (Color) primitives.get("srcBorderHighlightColor");
    axisHighlightColor = (Color) primitives.get("axisHighlightColor");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");
    src = (int[][]) primitives.get("src");
    dstHighlightColor = (Color) primitives.get("dstHighlightColor");
    headerProperties = (TextProperties) props
        .getPropertiesByName("headerProperties");
    calculationProps = (SourceCodeProperties) props
        .getPropertiesByName("calculationProps");
    varianz = (Double) primitives.get("varianz");
    size = (Integer) primitives.get("size");

    // ==============================================
    // transform array / matrix to gain compatibility
    // ==============================================

    int[][] srcOld = src;
    int widthOld = srcOld.length;
    int heightOld = srcOld[0].length;
    src = new int[heightOld][widthOld];
    for (int x = 0; x < widthOld; x++) {
      for (int y = 0; y < heightOld; y++) {
        src[y][x] = srcOld[x][y];
      }
    }

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    lang.setStepMode(true);
    gaussFilter(src, varianz, size);
    lang.finalizeGeneration();

    return lang.toString();
  }

  // ==============================================
  //
  // FILTER function
  //
  // ==============================================

  /**
   * Gauss Filter
   * 
   * @param src
   * @param varianz
   * @param size
   * @return the result of the filter
   */
  public int[][] gaussFilter(int[][] src, double varianz, int size) {

    int width = src.length;
    int height = src[0].length;

    int[][] dst = new int[width][height];

    // ==============================================
    // Description
    // ==============================================

    Font font = (Font) headerProperties
        .get(AnimationPropertiesKeys.FONT_PROPERTY);
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(font.getFontName(), Font.BOLD, 24));

    Text header = lang.newText(new Coordinates(20, 30), "Gauss-Filter",
        "header", null, headerProperties);

    RectProperties headerHighlightProperties = new RectProperties();
    headerHighlightProperties
        .set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headerHighlightProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.LIGHT_GRAY);
    headerHighlightProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    Rect headerHighlight = lang.newRect(new Offset(-5, -5, header, "NW"),
        new Offset(5, 5, header, "SE"), "header highlight", null,
        headerHighlightProperties);

    SourceCodeProperties descriptionProperties = new SourceCodeProperties();
    descriptionProperties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    descriptionProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    descriptionProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    descriptionProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);

    SourceCode description = lang.newSourceCode(new Offset(0, 60,
        headerHighlight, "SW"), "description", null, descriptionProperties);
    description.addCodeLine(
        "Der Gauss Filter ist ein Filter fuer Grafiken. Er ermoeglicht,", null,
        0, null);
    description.addCodeLine(
        "in einer Grafik Artefakte wie Alising zu entfernen. Auch eine", null,
        0, null);
    description.addCodeLine(
        "Rauschentfernung kann erreicht werden, da der Gauss Filter in", null,
        0, null);
    description.addCodeLine("solchen Bereichen eine Weichzeichnung erzeugt.",
        null, 0, null);
    description.addCodeLine(
        "Der Gauss Filter nutzt dazu eine Filtermatrix, die ueber das", null,
        0, null);
    description.addCodeLine(
        "Bild gelegt und fuer jeden Pixel weiter verschoben wird. Die", null,
        0, null);
    description.addCodeLine(
        "Filtermatrix kann dabei eine beliebige Groesse haben, jedoch", null,
        0, null);
    description.addCodeLine(
        "sollte die Gesamtbreite und -hoehe der Matrix ungerade sein.", null,
        0, null);
    description.addCodeLine(
        "Pixel am Rand werden im Gauss Filter weniger stark gewichtet", null,
        0, null);
    description.addCodeLine(
        "als Pixel in der Mitte. Die Verteilung (= Gaussche Glockenkurve)",
        null, 0, null);
    description.addCodeLine(
        "kann ueber die Varianz (Sigma)^2 eingestellt werden.", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine("Die Formel lautet dabei wie folgt:", null, 0, null);
    description.addCodeLine(
        "h(x,y) = 1 / (2 * pi * varianz) * exp(-(x^2 + y^2) / (2 * varianz))",
        null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine(
        "Mit diesem Verfahren kann ein besseres Ergebnis erreicht werden,",
        null, 0, null);
    description.addCodeLine(
        "als es beispielsweise mit dem einfacheren Box Filter moeglich ist.",
        null, 0, null);
    description.addCodeLine(
        "Es sind unterschiedliche Randbehandlungen moeglich, wir werden hier",
        null, 0, null);
    description.addCodeLine(
        "einfach den Rand aus dem Ursprungsbild uebernehmen.", null, 0, null);

    lang.nextStep("Initialisierung");

    // ==============================================
    // SourceCode
    // ==============================================

    description.hide();

    sourceCode = lang.newSourceCode(new Offset(0, 100 + height * 30,
        headerHighlight, "SW"), "sourceCode", null, sourceCodeProps);

    sourceCode
        .addCodeLine(
            "public static int[][] gaussFilter(int[][] src, double varianz, int size) {",
            null, 0, null);
    sourceCode.addCodeLine("	int width = src.length;", null, 1, null);
    sourceCode.addCodeLine("	int height = src[0].length;", null, 1, null);
    sourceCode.addCodeLine("	", null, 1, null);
    sourceCode.addCodeLine("	int[][] dst = new int[width][height];", null, 1,
        null);
    sourceCode.addCodeLine("	copyBorder(src, dst, size);", null, 1, null);
    sourceCode.addCodeLine("	", null, 1, null);
    sourceCode.addCodeLine(
        "	double[][] filterMatrix = calcFilterMatrix(size, varianz);", null, 1,
        null);
    sourceCode
        .addCodeLine(
            "	normaliseFilterMatrix(filterMatrix);          // Die Summe aller Felder ist nun 1",
            null, 1, null);
    sourceCode.addCodeLine("", null, 0, null);
    sourceCode.addCodeLine("	for (int x = size; x < width - size; x++) {",
        null, 1, null);
    sourceCode.addCodeLine("		for (int y = size; y < height - size; y++) {",
        null, 2, null);
    sourceCode.addCodeLine(
        "			dst[x][y] = applyFilterMatrix(src, filterMatrix, x, y, size);",
        null, 3, null);
    sourceCode.addCodeLine("		}", null, 2, null);
    sourceCode.addCodeLine("	}", null, 1, null);
    sourceCode.addCodeLine("	return dst;", null, 1, null);
    sourceCode.addCodeLine("}", null, 0, null);
    sourceCode.addCodeLine("", null, 0, null);
    sourceCode
        .addCodeLine(
            "private static int applyFilterMatrix(int[][] src, double[][] filterMatrix, int xPos, int yPos, int size){",
            null, 0, null);
    sourceCode.addCodeLine("	double filteredValue = 0;", null, 1, null);
    sourceCode.addCodeLine("	for (int y = 0; y <= size*2; y++) {", null, 1,
        null);
    sourceCode.addCodeLine("		for (int x = 0; x < size*2+1; x++) {", null, 2,
        null);
    sourceCode
        .addCodeLine(
            "			filteredValue += filterMatrix[x][y] * ((double) src[xPos-size+x][yPos-size+y]);",
            null, 3, null);
    sourceCode.addCodeLine("		}", null, 2, null);
    sourceCode.addCodeLine("	}", null, 1, null);
    sourceCode.addCodeLine("	return (int)filteredValue;", null, 1, null);
    sourceCode.addCodeLine("}", null, 0, null);

    lang.nextStep();

    // ==============================================
    // SRC Array
    // ==============================================

    PointProperties benchmarkPointProperties = new PointProperties();
    benchmarkPointProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    srcBenchmark = lang.newPoint(new Offset(30, 60, headerHighlight, "SW"),
        "src benchmark", null, benchmarkPointProperties);

    RectProperties arrayRectProperty = new RectProperties();
    arrayRectProperty.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayRectProperty.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayRectProperty.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    srcRectArray = new Rect[width][height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        srcRectArray[x][y] = lang.newRect(new Offset(30 * x, 30 * y,
            srcBenchmark, "SE"), new Offset(30 * (x + 1), 30 * (y + 1),
            srcBenchmark, "SE"), "src" + x + "," + y, null, arrayRectProperty);
      }
    }

    arrayValuesTextProperty = new TextProperties();
    arrayValuesTextProperty.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 12));
    arrayValuesTextProperty.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);
    arrayValuesTextProperty.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    arrayValuesTextProperty
        .set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    srcTextArray = new Text[width][height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        srcTextArray[x][y] = lang.newText(new Offset(30 * x + 15, 30 * y,
            srcBenchmark, "SE"), src[x][y] + "", "srcText" + x + "," + y, null,
            arrayValuesTextProperty);
      }
    }

    TextProperties axisTextProperty = new TextProperties();
    axisTextProperty.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    axisTextProperty.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    axisTextProperty.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    Text[] srcAxisX = new Text[width];
    Text[] srcAxisY = new Text[height];
    for (int x = 0; x < width; x++) {
      srcAxisX[x] = lang.newText(new Offset(15, -20, srcRectArray[x][0], "NW"),
          (x == 0 ? "x=0" : x + ""), "srcAxisX" + x, null, axisTextProperty);
    }
    for (int y = 0; y < height; y++) {
      srcAxisY[y] = lang.newText(new Offset(-15, 0, srcRectArray[0][y], "NW"),
          (y == 0 ? "y=0" : y + ""), "srcAxisY" + y, null, axisTextProperty);
    }

    TextProperties arrayLabelProperty = new TextProperties();
    arrayLabelProperty.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 18));
    arrayLabelProperty.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    lang.newText(new Offset(0, -45, srcBenchmark, "NW"), "src:", "scrLabel",
        null, arrayLabelProperty);

    sourceCode.highlight(0);

    lang.nextStep();

    // ==============================================
    // DST Array
    // ==============================================

    sourceCode.unhighlight(0);
    sourceCode.highlight(4);

    dstBenchmark = lang.newPoint(new Offset(30 * width + 100, 0, srcBenchmark,
        "SW"), "src benchmark", null, benchmarkPointProperties);

    dstRectArray = new Rect[width][height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        dstRectArray[x][y] = lang.newRect(new Offset(30 * x, 30 * y,
            dstBenchmark, "SE"), new Offset(30 * (x + 1), 30 * (y + 1),
            dstBenchmark, "SE"), "dst" + x + "," + y, null, arrayRectProperty);
      }
    }

    Text[] dstAxisX = new Text[width];
    Text[] dstAxisY = new Text[height];
    for (int x = 0; x < width; x++) {
      dstAxisX[x] = lang.newText(new Offset(15, -20, dstRectArray[x][0], "NW"),
          (x == 0 ? "x=0" : x + ""), "dstAxisX" + x, null, axisTextProperty);
    }
    for (int y = 0; y < height; y++) {
      dstAxisY[y] = lang.newText(new Offset(-15, 0, dstRectArray[0][y], "NW"),
          (y == 0 ? "y=0" : y + ""), "dstAxisY" + y, null, axisTextProperty);
    }

    lang.newText(new Offset(0, -45, dstBenchmark, "NW"), "dst:", "dstLabel",
        null, arrayLabelProperty);

    lang.nextStep("Randbehandlung");

    // ==============================================
    // CopyBorder()
    // ==============================================

    sourceCode.unhighlight(4);
    sourceCode.highlight(5);

    // src color
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x < size || x >= width - size || y < size || y >= height - size) {
          srcRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
              srcBorderHighlightColor, new TicksTiming(0), new TicksTiming(50));
        }
      }
    }

    // Arrows
    PolylineProperties arrowProperties = new PolylineProperties();
    arrowProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    Polyline[] arrows = new Polyline[height];

    for (int y = 0; y < height; y++) {
      Node[] arrowNodes = {
          new Offset(0, 15, srcRectArray[width - 1][y], "NE"),
          new Offset(-30, 15, dstRectArray[0][y], "NW") };
      arrows[y] = lang.newPolyline(arrowNodes, "arrow" + y, null,
          arrowProperties);
    }

    lang.nextStep();

    // Destination Color
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x < size || x >= width - size || y < size || y >= height - size) {
          dstRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
              dstHighlightColor, new TicksTiming(0), new TicksTiming(50));
        }
      }
    }

    // Destination Text
    dstTextArray = new Text[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x < size || x >= width - size || y < size || y >= height - size) {
          dst[x][y] = src[x][y];
          dstTextArray[x][y] = lang.newText(new Offset(30 * x + 15, 30 * y,
              dstBenchmark, "SE"), dst[x][y] + "", "dstText" + x + "," + y,
              new TicksTiming(50), arrayValuesTextProperty);
        }
      }
    }

    lang.nextStep("FilterMatrix");

    // Unhighlight border and arrows
    for (Polyline arrow : arrows) {
      arrow.hide();
    }

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x < size || x >= width - size || y < size || y >= height - size) {
          srcRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
              Color.WHITE, new TicksTiming(0), new TicksTiming(0));
          dstRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
              Color.WHITE, new TicksTiming(0), new TicksTiming(0));
        }
      }
    }

    // ==============================================
    // Filter Matrix
    // ==============================================

    sourceCode.unhighlight(5);
    sourceCode.highlight(7);

    filterMatrix = new double[size * 2 + 1][size * 2 + 1];
    filterTextArray = new Text[size * 2 + 1][size * 2 + 1];

    Point filterBenchmark = lang
        .newPoint(new Offset(30 * width + 100, 0, dstBenchmark, "SW"),
            "filter benchmark", null, benchmarkPointProperties);

    filterRectArray = new Rect[size * 2 + 1][size * 2 + 1];

    for (int x = 0; x <= size * 2; x++) {
      for (int y = 0; y <= size * 2; y++) {
        filterRectArray[x][y] = lang.newRect(new Offset(40 * x, 30 * y,
            filterBenchmark, "SE"), new Offset(40 * (x + 1), 30 * (y + 1),
            filterBenchmark, "SE"), "filter" + x + "," + y, null,
            arrayRectProperty);
        filterMatrix[x][y] = impulsantwort(x - size, y - size, varianz);
        filterTextArray[x][y] = lang.newText(new Offset(40 * x + 20, 30 * y,
            filterBenchmark, "SE"), doubleFormat.format(filterMatrix[x][y]),
            "filterText" + x + "," + y, new TicksTiming(50),
            arrayValuesTextProperty);
      }
    }

    lang.newText(new Offset(0, -45, filterBenchmark, "NW"), "filterMatrix:",
        "filterLabel", null, arrayLabelProperty);

    lang.nextStep();

    // ==============================================
    // normalise Filter Matrix
    // ==============================================

    sourceCode.unhighlight(7);
    sourceCode.highlight(8);

    normaliseFilterMatrix(filterMatrix);

    for (int x = 0; x <= size * 2; x++) {
      for (int y = 0; y <= size * 2; y++) {
        filterRectArray[x][y].changeColor(
            AnimationPropertiesKeys.FILL_PROPERTY, filterMatrixHighlightColor,
            new TicksTiming(0), new TicksTiming(0));
        filterTextArray[x][y].hide(new TicksTiming(250));
        filterTextArray[x][y] = lang.newText(new Offset(40 * x + 20, 30 * y,
            filterBenchmark, "SE"), doubleFormat.format(filterMatrix[x][y]),
            "filterText" + x + "," + y, new TicksTiming(300),
            arrayValuesTextProperty);
      }
    }

    lang.nextStep();

    filterMatrixColoring();

    lang.nextStep();

    for (int x = 0; x <= size * 2; x++) {
      for (int y = 0; y <= size * 2; y++) {
        filterRectArray[x][y].changeColor(
            AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
            new TicksTiming(0), new TicksTiming(0));
        filterTextArray[x][y].changeColor(
            AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK,
            new TicksTiming(0), new TicksTiming(0));
      }
    }

    sourceCode.unhighlight(8);

    // ==============================================
    // Iterate over Everything :-)
    // ==============================================

    for (int y = size; y < height - size; y++) {
      for (int x = size; x < width - size; x++) {
        // =========
        // Question
        // =========
        TrueFalseQuestionModel reihenfolge = new TrueFalseQuestionModel(
            "reihenfolge", false, 1);
        reihenfolge
            .setPrompt("Ist es wichtig, in welcher Reihenfolge das SRC Array durchlaufen wird?");
        reihenfolge
            .setFeedbackForAnswer(
                true,
                "Das Array SRC wird nicht verändert. Daher ist die Reihenfolge für das Iterieren egal.");
        reihenfolge
            .setFeedbackForAnswer(
                false,
                "Das Array SRC wird nicht verändert. Daher ist die Reihenfolge für das Iterieren egal.");
        lang.addTFQuestion(reihenfolge);

        srcAxisX[x].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            axisHighlightColor, new TicksTiming(0), new TicksTiming(0));
        dstAxisX[x].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            axisHighlightColor, new TicksTiming(0), new TicksTiming(0));
        srcAxisY[y].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            axisHighlightColor, new TicksTiming(0), new TicksTiming(0));
        dstAxisY[y].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            axisHighlightColor, new TicksTiming(0), new TicksTiming(0));

        sourceCode.highlight(10);
        sourceCode.highlight(11);

        srcAxisX[x].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            axisHighlightColor, new TicksTiming(0), new TicksTiming(0));
        dstAxisX[x].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            axisHighlightColor, new TicksTiming(0), new TicksTiming(0));
        srcAxisY[y].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            axisHighlightColor, new TicksTiming(0), new TicksTiming(0));
        dstAxisY[y].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            axisHighlightColor, new TicksTiming(0), new TicksTiming(0));

        lang.nextStep("");

        // ==============================================
        // applyFilterMatrix()
        // ==============================================
        dst[x][y] = applyFilterMatrix(src, filterMatrix, x, y, size);

        srcAxisX[x].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.BLACK, new TicksTiming(0), new TicksTiming(0));
        dstAxisX[x].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.BLACK, new TicksTiming(0), new TicksTiming(0));
        srcAxisY[y].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.BLACK, new TicksTiming(0), new TicksTiming(0));
        dstAxisY[y].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
            Color.BLACK, new TicksTiming(0), new TicksTiming(0));
      }
    }

    // ==============================================
    // Summary
    // ==============================================

    // =========
    // Question
    // =========
    MultipleSelectionQuestionModel sinnUndZweck = new MultipleSelectionQuestionModel(
        "sinnUndZweck");
    sinnUndZweck
        .setPrompt("Der Der Algorithmus ist terminiert. Wozu kann das Ergebnis verwendet werden?");
    sinnUndZweck.addAnswer("Antialising", 1,
        "Antialising: KORREKT, die Artefakte können reduziert werden.<br>");
    sinnUndZweck
        .addAnswer(
            "Rauschunterdrückung",
            1,
            "Rauschunterdrückung: KORREKT, Rauschen durch Mittelwertbildung verringert.<br>");
    sinnUndZweck
        .addAnswer(
            "Weichzeichnung",
            1,
            "Weichzeichnung: KORREKT, durch die Mittelwertbildung werden Kanten weicher.<br>");
    sinnUndZweck
        .addAnswer(
            "Beleuchtungskorrektur",
            -1,
            "Beleuchtungskorrektur: FALSCH, der Filter verändert keine Beleuchtungseigenschaften.<br>");
    sinnUndZweck
        .addAnswer("Kantenbetonung", -1,
            "Kantenbetonung: FALSCH, die Kanten werden vielmehr leicht verwaschen.<br>");
    sinnUndZweck
        .addAnswer(
            "Korrektur des Weißabgleiches",
            -1,
            "Korrektur des Weißabgleiches: FALSCH, der Weißabgleich (sollte das Bild überhaupt Farbe haben) bleibt unbeeinflusst.<br>");
    lang.addMSQuestion(sinnUndZweck);

    sourceCode.hide();

    SourceCodeProperties summaryProperty = new SourceCodeProperties();
    summaryProperty.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    summaryProperty.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    summaryProperty.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    summaryProperty.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode summary = lang.newSourceCode(new Offset(0, 100 + height * 30,
        headerHighlight, "SW"), "sourceCode", null, summaryProperty);
    summary
        .addCodeLine(
            "Der Algorithmus ist nun terminiert und hat seine Ausgabe in das dst Array",
            null, 0, null);
    summary.addCodeLine("geschrieben. Das bearbeitete " + (size * 2 + 1) + "x"
        + (size * 2 + 1) + " grosse Feld weist nun keine starken", null, 0,
        null);
    summary
        .addCodeLine(
            "Auslenkungen mehr auf, sondern alle Werte befinden sich ungefaehr in der",
            null, 0, null);
    summary.addCodeLine("Mitte des Wertebereiches.", null, 0, null);
    summary.addCodeLine("", null, 0, null);
    summary
        .addCodeLine(
            "In der Realitaet wird der Gauss Filter fuer Antialising oder Rauschunterdrueckung eingesetzt.",
            null, 0, null);
    summary
        .addCodeLine(
            "Er fuehrt jedoch im Gegensatz zu anderen Filtern zu einer relativ grossen Unschaerfe.",
            null, 0, null);
    summary
        .addCodeLine(
            "Soll das Bild schaerfer bleiben, kann beispielsweise der einfacher zu berechnende Box Filter ",
            null, 0, null);
    summary
        .addCodeLine(
            "verwendet werden, der jedoch keine Unterschiedliche Gewichtung der Werte unterstuetzt",
            null, 0, null);

    lang.nextStep("Ergebnis");
    // =========
    // Question
    // =========
    MultipleChoiceQuestionModel komplexitaet = new MultipleChoiceQuestionModel(
        "komplexitaet");
    komplexitaet
        .setPrompt("Ist es wichtig, in welcher Reihenfolge das SRC Array durchlaufen wird?<br>");
    komplexitaet
        .addAnswer(
            "O(1)",
            0,
            "Falsch! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)<br>");
    komplexitaet
        .addAnswer(
            "O(breite)",
            0,
            "Falsch! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)<br>");
    komplexitaet
        .addAnswer(
            "O(hoehe)",
            0,
            "Falsch! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)<br>");
    komplexitaet
        .addAnswer(
            "O(breite*hoehe)",
            1,
            "Richtig! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)<br>");
    komplexitaet
        .addAnswer(
            "O((breite*hoehe)^2)",
            0,
            "Falsch! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)<br>");
    lang.addMCQuestion(komplexitaet);

    return dst;
  }

  /**
   * Hilfsfunktion
   * 
   * @param src
   * @param filterMatrix
   * @param xPos
   * @param yPos
   * @param size
   * @return
   */
  private int applyFilterMatrix(int[][] src, double[][] filterMatrix, int xPos,
      int yPos, int size) {

    sourceCode.unhighlight(10);
    sourceCode.unhighlight(11);
    sourceCode.highlight(12);
    sourceCode.highlight(18);

    // Highlight Source
    for (int y = 0; y <= size * 2; y++) {
      for (int x = 0; x < size * 2 + 1; x++) {
        if (xPos - size + x == xPos && yPos - size + y == yPos) {
          srcRectArray[xPos - size + x][yPos - size + y].changeColor(
              AnimationPropertiesKeys.FILL_PROPERTY, srcCenterHighlightColor,
              new TicksTiming(0), new TicksTiming(50));
        } else {
          srcRectArray[xPos - size + x][yPos - size + y].changeColor(
              AnimationPropertiesKeys.FILL_PROPERTY, srcBorderHighlightColor,
              new TicksTiming(0), new TicksTiming(50));
        }
      }
    }
    lang.nextStep();

    sourceCode.unhighlight(18);
    sourceCode.highlight(20);
    sourceCode.highlight(21);
    sourceCode.highlight(22);

    for (int x = 0; x <= size * 2; x++) {
      for (int y = 0; y <= size * 2; y++) {
        filterRectArray[x][y].changeColor(
            AnimationPropertiesKeys.FILL_PROPERTY, filterMatrixHighlightColor,
            new TicksTiming(0), new TicksTiming(50));
      }
    }

    // Calculate
    double filteredValue = 0;
    SourceCode multiplication = lang.newSourceCode(new Offset(30, 0,
        filterRectArray[size * 2][0], "NE"), "Multiplication", null,
        calculationProps);
    for (int y = 0; y <= size * 2; y++) {
      for (int x = 0; x < size * 2 + 1; x++) {
        String codeLine = "";
        codeLine += doubleFormat.format(filterMatrix[x][y]);
        codeLine += " * ";
        codeLine += src[xPos - size + x][yPos - size + y];
        while (codeLine.length() < 12)
          codeLine += " ";
        codeLine += "=";
        multiplication.addCodeLine(codeLine, "multiplication " + x + "," + y,
            0, null);
        filteredValue += filterMatrix[x][y]
            * ((double) src[xPos - size + x][yPos - size + y]);
      }
    }

    lang.nextStep();

    multiplication.hide();
    SourceCode addition = lang
        .newSourceCode(new Offset(30, 0, filterRectArray[size * 2][0], "NE"),
            "addition", null, calculationProps);
    for (int y = 0; y <= size * 2; y++) {
      for (int x = 0; x < size * 2 + 1; x++) {
        double value = filterMatrix[x][y]
            * ((double) src[xPos - size + x][yPos - size + y]);
        String codeLine = "";
        codeLine += doubleFormat.format(filterMatrix[x][y]);
        codeLine += " * ";
        codeLine += src[xPos - size + x][yPos - size + y];
        while (codeLine.length() < 12)
          codeLine += " ";
        codeLine += "=";
        codeLine += "  ";
        if (value < 10)
          codeLine += " ";
        if (value < 100)
          codeLine += " ";
        codeLine += doubleFormat.format(value);
        addition.addCodeLine(codeLine, "addition " + x + "," + y, 0, null);
      }
    }

    // =========
    // Question
    // =========
    if (xPos == 2 && yPos == 1) {
      FillInBlanksQuestionModel nextValue = new FillInBlanksQuestionModel(
          "nextValue");
      nextValue
          .setPrompt("welcher Wert wird in das nächste DST-Feld eingetragen?");
      nextValue.addAnswer("" + (int) filteredValue, 1,
          "Es wird Die Summe, also " + (int) (filteredValue) + " eingetragen");
      lang.addFIBQuestion(nextValue);
    }

    lang.nextStep();
    addition.addCodeLine("              +_______", null, 0, null);
    String result = "               ";
    if (filteredValue < 10)
      result += " ";
    if (filteredValue < 100)
      result += " ";
    result += doubleFormat.format(filteredValue);
    addition.addCodeLine(result, null, 0, null);
    addition.highlight((size * 2 + 1) * (size * 2 + 1) + 1);

    lang.nextStep();

    // highlight dst and print value
    sourceCode.unhighlight(20);
    sourceCode.unhighlight(21);
    sourceCode.unhighlight(22);
    sourceCode.highlight(25);

    for (int x = 0; x <= size * 2; x++) {
      for (int y = 0; y <= size * 2; y++) {
        filterRectArray[x][y].changeColor(
            AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
            new TicksTiming(0), new TicksTiming(0));
      }
    }

    dstRectArray[xPos][yPos].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
        dstHighlightColor, new TicksTiming(0), new TicksTiming(50));
    dstTextArray[xPos][yPos] = lang.newText(new Offset(30 * xPos + 15,
        30 * yPos, dstBenchmark, "SE"), (int) filteredValue + "", "dstText"
        + xPos + "," + yPos, new TicksTiming(50), arrayValuesTextProperty);

    lang.nextStep();

    addition.hide();

    // unhighlight
    for (int y = 0; y <= size * 2; y++) {
      for (int x = 0; x < size * 2 + 1; x++) {
        srcRectArray[xPos - size + x][yPos - size + y].changeColor(
            AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
            new TicksTiming(0), new TicksTiming(0));
      }
    }
    dstRectArray[xPos][yPos].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.WHITE, new TicksTiming(0), new TicksTiming(0));
    sourceCode.unhighlight(12);
    sourceCode.unhighlight(25);

    return (int) filteredValue;
  }

  private void filterMatrixColoring() {
    double maxValue = filterMatrix[size][size];
    for (int x = 0; x <= size * 2; x++) {
      for (int y = 0; y <= size * 2; y++) {
        int colorValue = (int) (255 * filterMatrix[x][y] / maxValue);
        filterRectArray[x][y]
            .changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(
                colorValue, colorValue, colorValue), new TicksTiming(0),
                new TicksTiming(0));
        if (colorValue < 128) {
          filterTextArray[x][y].changeColor(
              AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE,
              new TicksTiming(0), new TicksTiming(0));
        }
      }
    }
  }

  // /**
  // * copies the border of the specific size from src to dst
  // *
  // * @param src
  // * @param dst
  // * @param size
  // */
  // private static void copyBorder(int[][] src, int[][] dst, int size) {
  // int width = src.length;
  // int height = src[0].length;
  // for (int x = 0; x < width; x++) {
  // for (int y = 0; y < height; y++) {
  // if (x < size || x >= width - size || y < size || y >= height - size) {
  // dst[x][y] = src[x][y];
  // }
  // }
  // }
  // }
  //
  //
  //
  // private static double[][] calcFilterMatrix(int size, double varianz) {
  // double[][] matrix = new double[size * 2 + 1][size * 2 + 1];
  //
  // for (int x = 0; x <= size * 2; x++) {
  // for (int y = 0; y <= size * 2; y++) {
  // matrix[x][y]=impulsantwort(x-size, y-size, varianz);
  // }
  // }
  //
  // return matrix;
  // }

  private static double impulsantwort(int x, int y, double varianz) {
    double x2 = x * x;
    double y2 = y * y;
    return 1 / (2 * Math.PI * varianz) * Math.exp(-(x2 + y2) / (2 * varianz));
  }

  // private static String arrayToString(int[][] array) {
  // int width = array.length;
  // int height = array[0].length;
  //
  // StringBuffer stringBuffer = new StringBuffer();
  // for (int y = 0; y < height; y++) {
  // for (int x = 0; x < width; x++) {
  // stringBuffer.append(array[x][y]);
  // stringBuffer.append("\t");
  // }
  // stringBuffer.append("\n");
  // }
  // return stringBuffer.toString();
  // }
  //
  // private static String arrayToString(double[][] array) {
  // int width = array.length;
  // int height = array[0].length;
  //
  // java.text.DecimalFormat format = new java.text.DecimalFormat("#0.#####");
  //
  // StringBuffer stringBuffer = new StringBuffer();
  // for (int y = 0; y < height; y++) {
  // for (int x = 0; x < width; x++) {
  // stringBuffer.append(format.format(array[x][y]));
  // stringBuffer.append("\t");
  // }
  // stringBuffer.append("\n");
  // }
  // return stringBuffer.toString();
  // }

  private static double sum(double[][] array) {
    double sum = 0;
    int width = array.length;
    int height = array[0].length;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        sum += array[x][y];
      }
    }
    return sum;
  }

  private static void normaliseFilterMatrix(double[][] filterMatrix) {
    int width = filterMatrix.length;
    int height = filterMatrix[0].length;
    double sum = sum(filterMatrix);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        filterMatrix[x][y] /= sum;
      }
    }
  }

  // private static int[][] getSrcArray(int width, int height) {
  // int[][] src = new int[width][height];
  //
  // int[] randomNumbers = { 159, 161, 202, 228, 115, 0, 140, 9, 172, 120,
  // 229, 145, 133, 216, 35, 199, 55, 50, 180, 43, 221, 38, 189, 91,
  // 113, 10, 36, 121, 69, 58, 204, 82, 48, 92, 120, 39, 213, 212,
  // 62, 191, 246, 219, 3, 113, 198, 171, 82, 100, 253, 235, 63,
  // 124, 36, 69, 48, 220, 220, 188, 22, 47, 244, 137, 7, 103, 182,
  // 49, 62, 183, 168, 45, 189, 174, 68, 214, 221, 133, 20, 42, 219,
  // 131, 132, 106, 245, 253, 96, 75, 128, 139, 135, 234, 68, 115,
  // 133, 183, 112, 233, 193, 197, 30, 253 };
  //
  // for (int y = 0; y < height; y++) {
  // for (int x = 0; x < width; x++) {
  // src[x][y] = randomNumbers[x + width * y];
  // }
  // }
  //
  // return src;
  // }

  public String getName() {
    return "Gauss Filter";
  }

  public String getAlgorithmName() {
    return "Gauss Filter";
  }

  public String getAnimationAuthor() {
    return "Tobias Otterbein, Florian Reimold";
  }

  public String getDescription() {
    return "Der Gauss Filter ist ein Filter fuer Grafiken. Er ermoeglicht, in einer Grafik Artefakte wie Alising zu entfernen. Auch eine Rauschentfernung kann erreicht werden, da der Gauss Filter in solchen Bereichen eine Weichzeichnung erzeugt."
        + "\n"
        + "Der Gauss Filter nutzt dazu eine Filtermatrix, die ueber das Bild gelegt und fuer jeden Pixel weiter verschoben wird. Die Filtermatrix kann dabei eine beliebige Groesse haben, jedoch sollte die Gesamtbreite und -hoehe der Matrix ungerade sein. Pixel am Rand werden im Gauss Filter weniger stark gewichtet als Pixel in der Mitte. Die Verteilung (= Gaussche Glockenkurve) kann ueber die Varianz (Sigma)^2 eingestellt werden."
        + "\n"
        + "Die Formel lautet dabei wie folgt:"
        + "\n"
        + "h(x,y) = 1 / (2 * pi * varianz) * exp(-(x^2 + y^2) / (2 * varianz))"
        + "\n"
        + "\n"
        + "Mit diesem Verfahren kann ein besseres Ergebnis erreicht werden, als es beispielsweise mit dem einfacheren Box Filter moeglich ist."
        + "\n"
        + "Es sind unterschiedliche Randbehandlungen moeglich, wir werden hier einfach den Rand aus dem Ursprungsbild uebernehmen.";
  }

  public String getCodeExample() {
    return "public static int[][] gaussFilter(int[][] src, double varianz, int size) {"
        + "\n"
        + "	int width = src.length;"
        + "\n"
        + "	int height = src[0].length;"
        + "\n"
        + "\n"
        + "	int[][] dst = new int[width][height];"
        + "\n"
        + "	copyBorder(src, dst, size);"
        + "\n"
        + "	"
        + "\n"
        + "	double[][] filterMatrix = calcFilterMatrix(size, varianz);"
        + "\n"
        + "	normaliseFilterMatrix(filterMatrix);"
        + "\n"
        + "\n"
        + "	for (int x = size; x < width - size; x++) {"
        + "\n"
        + "		for (int y = size; y < height - size; y++) {"
        + "\n"
        + "			dst[x][y] = applyFilterMatrix(src, filterMatrix, x, y, size);"
        + "\n"
        + "		}"
        + "\n"
        + "	}"
        + "\n"
        + "	return dst;"
        + "\n"
        + "}"
        + "\n"
        + "\n"
        + "private static int applyFilterMatrix(int[][] src, double[][] filterMatrix, int xPos, int yPos, int size){"
        + "\n"
        + "	double filteredValue = 0;"
        + "\n"
        + "	for (int y = 0; y <= size*2; y++) {"
        + "\n"
        + "		for (int x = 0; x < size*2+1; x++) {"
        + "\n"
        + "			filteredValue += filterMatrix[x][y] * ((double) src[xPos-size+x][yPos-size+y]);"
        + "\n"
        + "		}"
        + "\n"
        + "	}"
        + "\n"
        + "	return (int)filteredValue;"
        + "\n" + "}";
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
    return Generator.JAVA_OUTPUT;
  }

}