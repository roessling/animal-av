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

public class BoxFilter implements Generator {
  private Language             lang;
  private Color                srcCenterHighlightColor;
  private Color                srcBorderHighlightColor;
  private Color                axisHighlightColor;
  private SourceCodeProperties sourceCodeProps;
  private int[][]              src;
  private Color                dstHighlightColor;
  private TextProperties       headerProperties;
  SourceCodeProperties         calculationProps;

  public void init() {
    lang = new AnimalScript("Box Filter", "Tobias Otterbein,Florian Reimold",
        800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    srcCenterHighlightColor = (Color) primitives.get("srcCenterHighlightColor");
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

    filter(src);
    return lang.toString();
  }

  public String getName() {
    return "Box Filter";
  }

  public String getAlgorithmName() {
    return "Box Filter";
  }

  public String getAnimationAuthor() {
    return "Tobias Otterbein, Florian Reimold";
  }

  public String getDescription() {
    return "Bei dem Box Filter handelt es sich um einen Filter fuer Grafiken. Er wird eingesetzt, um Bildartefakte wie Aliasing-Effekte zu entfernen."
        + "\n"
        + "Dafuer wird fuer jeden Bildpixel das ihn umgebene 3*3 Pixel grosse Quadrat gemittelt.<br>"
        + "\n"
        + "Das Verhalten fuer die Randpixel ist nicht genau definiert. Wir werden hier Die Randpixel einfach aus dem Ursprungsbild uebernehmen.";
  }

  public String getCodeExample() {
    return "public static int[][] boxFilter(int[][] src) {" + "\n"
        + "	int width = src.length;" + "\n" + "	int height = src[0].length;"
        + "\n" + "\n" + "	int[][] dst = new int[width][height];" + "\n"
        + "	copyBorder(src, dst);" + "\n"
        + "	for (int x = 1; x < width - 1; x++) {" + "\n"
        + "		for (int y = 1; y < height - 1; y++) {" + "\n"
        + "			int sumAround = sumOneOffset(src, x, y);" + "\n"
        + "			dst[x][y] = sumAround / 9;" + "\n" + "		}" + "\n" + "	}" + "\n"
        + "	return dst;" + "\n" + "}";
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

  // ==============================================
  //
  // FILTER function
  //
  // ==============================================

  public void filter(int[][] src) {

    int width = src.length;
    int height = src[0].length;
    int[][] dst = new int[width][height];

    // ==============================================
    // Description
    // ==============================================

    // TextProperties headerProperties = new TextProperties();
    // headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "SansSerif", Font.BOLD, 24));
    // headerProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    Font font = (Font) headerProperties
        .get(AnimationPropertiesKeys.FONT_PROPERTY);
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(font.getFontName(), Font.BOLD, 24));

    Text header = lang.newText(new Coordinates(20, 30), "Box-Filter", "header",
        null, headerProperties);

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
    description
        .addCodeLine(
            "Bei dem Box Filter handelt es sich um einen Filter fuer Grafiken. Er wird",
            null, 0, null);
    description
        .addCodeLine(
            "eingesetzt, um Bildartefakte wie Aliasing-Effekte zu entfernen. Dafuer wird",
            null, 0, null);
    description
        .addCodeLine(
            "fuer jeden Bildpixel das ihn umgebene 3*3 Pixel grosse Quadrat gemittelt.",
            null, 0, null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine(
            "Das Verhalten fuer die Randpixel ist nicht genau definiert. Wir werden hier",
            null, 0, null);
    description.addCodeLine(
        "Die Randpixel einfach aus dem Ursprungsbild uebernehmen.", null, 0,
        null);

    lang.nextStep("Initialisierung");

    // ==============================================
    // SourceCode
    // ==============================================

    description.hide();

    // SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
    // sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
    // Color.BLUE);
    // sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 16));
    // sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.RED);
    // sourceCodeProps
    // .set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode sourceCode = lang.newSourceCode(new Offset(0, 100 + height * 30,
        headerHighlight, "SW"), "sourceCode", null, sourceCodeProps);
    sourceCode.addCodeLine("public static int[][] boxFilter(int[][] src) {",
        null, 0, null);
    sourceCode.addCodeLine("	int width = src.length;", null, 1, null);
    sourceCode.addCodeLine("	int height = src[0].length;", null, 1, null);
    sourceCode.addCodeLine("", null, 0, null);
    sourceCode.addCodeLine("	int[][] dst = new int[width][height];", null, 1,
        null);
    sourceCode.addCodeLine("	copyBorder(src, dst);", null, 1, null);
    sourceCode.addCodeLine("	for (int x = 1; x < width - 1; x++) {", null, 1,
        null);
    sourceCode.addCodeLine("		for (int y = 1; y < height - 1; y++) {", null, 2,
        null);
    sourceCode.addCodeLine("			int sumAround = sumOneOffset(src, x, y);", null,
        3, null);
    sourceCode.addCodeLine("			dst[x][y] = sumAround / 9;", null, 3, null);
    sourceCode.addCodeLine("		}", null, 2, null);
    sourceCode.addCodeLine("	}", null, 1, null);
    sourceCode.addCodeLine("	return dst;", null, 1, null);
    sourceCode.addCodeLine("}", null, 0, null);

    lang.nextStep();

    // ==============================================
    // SRC Array
    // ==============================================

    PointProperties benchmarkPointProperties = new PointProperties();
    benchmarkPointProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    Point srcBenchmark = lang.newPoint(
        new Offset(30, 60, headerHighlight, "SW"), "src benchmark", null,
        benchmarkPointProperties);

    RectProperties arrayRectProperty = new RectProperties();
    arrayRectProperty.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayRectProperty.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayRectProperty.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect[][] srcRectArray = new Rect[width][height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        srcRectArray[x][y] = lang.newRect(new Offset(30 * x, 30 * y,
            srcBenchmark, "SE"), new Offset(30 * (x + 1), 30 * (y + 1),
            srcBenchmark, "SE"), "src" + x + "," + y, null, arrayRectProperty);
      }
    }

    TextProperties arrayValuesTextProperty = new TextProperties();
    arrayValuesTextProperty.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 12));
    arrayValuesTextProperty.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);
    arrayValuesTextProperty.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    arrayValuesTextProperty
        .set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    Text[][] srcTextArray = new Text[width][height];
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

    Point dstBenchmark = lang.newPoint(new Offset(30 * width + 100, 0,
        srcBenchmark, "SW"), "src benchmark", null, benchmarkPointProperties);

    Rect[][] dstRectArray = new Rect[width][height];
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

    // Source Color
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
          srcRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
              srcBorderHighlightColor, new TicksTiming(0), new TicksTiming(50));
        }
      }
    }

    lang.nextStep();

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
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
          dstRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
              dstHighlightColor, new TicksTiming(0), new TicksTiming(50));
        }
      }
    }

    // Destination Text
    Text[][] dstTextArray = new Text[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
          dst[x][y] = src[x][y];
          dstTextArray[x][y] = lang.newText(new Offset(30 * x + 15, 30 * y,
              dstBenchmark, "SE"), dst[x][y] + "", "dstText" + x + "," + y,
              new TicksTiming(50), arrayValuesTextProperty);
        }
      }
    }

    lang.nextStep("Hauptalgorithmus");

    // Unhighlight border and arrows
    for (Polyline arrow : arrows) {
      arrow.hide();
    }

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
          srcRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
              Color.WHITE, new TicksTiming(0), new TicksTiming(0));
          dstRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
              Color.WHITE, new TicksTiming(0), new TicksTiming(0));
        }
      }
    }

    // ==============================================
    // Iterate over Everything :-)
    // ==============================================

    sourceCode.unhighlight(5);
    sourceCode.highlight(6);
    sourceCode.highlight(7);

    // SourceCodeProperties calculationProps = new SourceCodeProperties();
    // calculationProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 16));
    // calculationProps
    // .set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    for (int y = 1; y < height - 1; y++) {
      for (int x = 1; x < width - 1; x++) {

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

        // ===================
        // Sum Around
        // ===================
        sourceCode.highlight(8);

        SourceCode calculation = lang.newSourceCode(new Offset(60, 0,
            dstRectArray[width - 1][0], "NE"), "calculation" + x + "," + y,
            null, calculationProps);
        calculation.addCodeLine("Summe:", "summe", 0, null);

        int sum = 0;
        for (int tempX = x - 1; tempX <= x + 1; tempX++) {
          for (int tempY = y - 1; tempY <= y + 1; tempY++) {

            if (src[tempX][tempY] < 10) {
              calculation.addCodeLine("     " + src[tempX][tempY], "pos"
                  + tempX + "," + tempY, 0, null);
            } else if (src[tempX][tempY] < 100) {
              calculation.addCodeLine("    " + src[tempX][tempY], "pos" + tempX
                  + "," + tempY, 0, null);
            } else {
              calculation.addCodeLine("   " + src[tempX][tempY], "pos" + tempX
                  + "," + tempY, 0, null);
            }
            sum += src[tempX][tempY];

            if (tempX == x && tempY == y) {
              srcRectArray[tempX][tempY].changeColor(
                  AnimationPropertiesKeys.FILL_PROPERTY,
                  srcCenterHighlightColor, new TicksTiming(0), new TicksTiming(
                      50));
            } else {
              srcRectArray[tempX][tempY].changeColor(
                  AnimationPropertiesKeys.FILL_PROPERTY,
                  srcBorderHighlightColor, new TicksTiming(0), new TicksTiming(
                      50));
            }
          }
        }
        lang.nextStep();
        calculation.addCodeLine("+_____", null, 0, null);
        calculation.addCodeLine("  " + sum, null, 0, null);

        // =========
        // Question
        // =========
        if (x == 2 && y == 1) {
          FillInBlanksQuestionModel nextValue = new FillInBlanksQuestionModel(
              "nextValue");
          nextValue
              .setPrompt("welcher Wert wird in das nächste DST-Feld eingetragen?");
          nextValue.addAnswer("" + (sum / 9), 1, "sum/9 = " + (sum / 9));
          lang.addFIBQuestion(nextValue);
        }

        lang.nextStep();

        // ===================
        // Divide & set Value
        // ===================

        sourceCode.unhighlight(8);
        sourceCode.highlight(9);
        dst[x][y] = sum / 9;

        calculation.addCodeLine("", null, 0, null);
        calculation.addCodeLine(sum + " / 9 = " + sum / 9, null, 0, null);

        dstRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
            dstHighlightColor, new TicksTiming(0), new TicksTiming(50));
        dstTextArray[x][y] = lang.newText(new Offset(30 * x + 15, 30 * y,
            dstBenchmark, "SE"), dst[x][y] + "", "dstText" + x + "," + y,
            new TicksTiming(50), arrayValuesTextProperty);

        lang.nextStep();

        calculation.hide();
        sourceCode.unhighlight(9);

        for (int tempX = x - 1; tempX <= x + 1; tempX++) {
          for (int tempY = y - 1; tempY <= y + 1; tempY++) {
            srcRectArray[tempX][tempY].changeColor(
                AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
                new TicksTiming(0), new TicksTiming(0));
          }
        }
        dstRectArray[x][y].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
            Color.WHITE, new TicksTiming(0), new TicksTiming(0));

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
    // Return
    // ==============================================
    sourceCode.unhighlight(6);
    sourceCode.unhighlight(7);
    sourceCode.highlight(12);

    // =========
    // Question
    // =========
    MultipleSelectionQuestionModel sinnUndZweck = new MultipleSelectionQuestionModel(
        "sinnUndZweck");
    sinnUndZweck
        .setPrompt("Der Der Algorithmus ist terminiert. Wozu kann das Ergebnis verwendet werden?");
    sinnUndZweck.addAnswer("Antialising", 1,
        "Antialising: KORREKT, die Artefakte können reduziert werden.\n");
    sinnUndZweck
        .addAnswer("Rauschunterdrückung", 1,
            "Rauschunterdrückung: KORREKT, Rauschen durch Mittelwertbildung verringert.\n");
    sinnUndZweck
        .addAnswer("Weichzeichnung", 1,
            "Weichzeichnung: KORREKT, durch die Mittelwertbildung werden Kanten weicher.\n");
    sinnUndZweck
        .addAnswer(
            "Beleuchtungskorrektur",
            -1,
            "Beleuchtungskorrektur: FALSCH, der Filter verändert keine Beleuchtungseigenschaften.\n");
    sinnUndZweck
        .addAnswer("Kantenbetonung", -1,
            "Kantenbetonung: FALSCH, die Kanten werden vielmehr leicht verwaschen.\n");
    sinnUndZweck
        .addAnswer(
            "Korrektur des Weißabgleiches",
            -1,
            "Korrektur des Weißabgleiches: FALSCH, der Weißabgleich (sollte das Bild überhaupt Farbe haben) bleibt unbeeinflusst.\n");
    lang.addMSQuestion(sinnUndZweck);

    lang.nextStep("Zusammenfassung");

    // ==============================================
    // Summary
    // ==============================================

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
    summary.addCodeLine(
        "geschrieben. Das bearbeitete 3x3 grosse Feld weist nun keine starken",
        null, 0, null);
    summary
        .addCodeLine(
            "Auslenkungen mehr auf, sondern alle Werte befinden sich ungefaehr in der",
            null, 0, null);
    summary.addCodeLine("Mitte des Wertebereiches.", null, 0, null);
    summary.addCodeLine("", null, 0, null);
    summary
        .addCodeLine(
            "In der Realitaet kann der Box Filter zwar fuer Antialising eingesetzt werden,",
            null, 0, null);
    summary
        .addCodeLine(
            "jedoch gibt es andere Filterarten, welche bessere Ergebnisse liefern. Dazu",
            null, 0, null);
    summary
        .addCodeLine(
            "gehoert beispielsweise der Gauss Filter, welcher die Werte der Umgebung",
            null, 0, null);
    summary.addCodeLine(
        "abhaengig von ihrem Abstand unterschiedlich stark gewichtet.", null,
        0, null);

    // =========
    // Question
    // =========
    MultipleChoiceQuestionModel komplexitaet = new MultipleChoiceQuestionModel(
        "komplexitaet");
    komplexitaet
        .setPrompt("Welche Komplexitaet besitzt der hier vorgestellte Gauss Filter?");
    komplexitaet
        .addAnswer(
            "O(1)",
            0,
            "Falsch! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)");
    komplexitaet
        .addAnswer(
            "O(breite)",
            0,
            "Falsch! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)");
    komplexitaet
        .addAnswer(
            "O(hoehe)",
            0,
            "Falsch! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)");
    komplexitaet
        .addAnswer(
            "O(breite*hoehe)",
            1,
            "Richtig! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)");
    komplexitaet
        .addAnswer(
            "O((breite*hoehe)^2)",
            0,
            "Falsch! Jedes Feld muss 1 Mal behandelt werden. Daher ist die Komplexitaet O(breite*hoehe)");
    lang.addMCQuestion(komplexitaet);

    lang.finalizeGeneration();
  }

}