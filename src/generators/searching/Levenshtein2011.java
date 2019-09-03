package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class Levenshtein2011 implements Generator {
  private Language             lang;
  private String               WortA;
  private String               WortB;
  private SourceCodeProperties sourceCode;
  private StringMatrix         grid;
  private SourceCode           code;
  private String[][]           colors;
  private String[][]           data;
  private Color                Matrix_Schriftfarbe;
  private Color                Highlight1;
  private Color                Highlight2;
  private Color                Matrix_Background;

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    WortA = (String) primitives.get("WortA");
    WortB = (String) primitives.get("WortB");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    Matrix_Schriftfarbe = (Color) primitives.get("Matrix Schriftfarbe");
    Highlight1 = (Color) primitives.get("Matrix Highlight1 Farbe");
    Highlight2 = (Color) primitives.get("Matrix Highlight2 Farbe");
    Matrix_Background = (Color) primitives.get("Matrix Background");

    buildIntro();
    levenshteinDistance();
    buildOutro();

    return workAround(lang.toString());
  }

  public void init() {
    lang = new AnimalScript("Levenshtein Distanz", "Peter Baumann, Oren Avni",
        1000, 800);
    lang.setStepMode(true);
    WortA = WortB = null;
    sourceCode = null;
    grid = null;
    code = null;
    Highlight1 = Highlight2 = null;
    colors = data = null;
  }

  // ******************************OUR
  // CODE**************************************************************

  public void levenshteinDistance() {
    int matrix[][] = new int[WortB.length() + 1][WortA.length() + 1];

    // Initialization
    initAll();
    animate(1, 0, 0, 0);

    // 0,1,2,3....
    for (int n = 0; n < WortB.length() + 1; n++) {
      matrix[n][0] = n;
    }
    for (int n = 0; n < WortA.length() + 1; n++) {
      matrix[0][n] = n;
    }

    // Fuer alle Elemente von A; i = index von A
    for (int i = 1; i < WortA.length() + 1; i++) {
      animate(2, i, 0, 0);
      // Fuer alle Elemente von B, j = index von B
      for (int j = 1; j < WortB.length() + 1; j++) {
        animate(3, j, 0, 0);
        int neq = 0;

        // Vergleiche A mit B
        if (WortA.charAt(i - 1) != WortB.charAt(j - 1))
          neq = 1;

        // s:= Minimum der 2x2 Matrix
        int s = matrix[j - 1][i] + 1;
        if (matrix[j][i - 1] + 1 < s)
          s = matrix[j][i - 1] + 1;

        // Setze den rechten unteren Wert auf s + neq
        if (matrix[j - 1][i - 1] + neq < s)
          s = matrix[j - 1][i - 1] + neq;
        matrix[j][i] = s;

        animate(4, j, i, s);
      }
    }
    result(matrix[WortB.length()][WortA.length()]);
  }

  private void initAll() {
    // preAlgorithm();
    initData();
    initGrid();
    initCode();
  }

  // Initialize the grid for being drawn
  private void initGrid() {
    MatrixProperties properties = new MatrixProperties();
    properties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    grid = lang.newStringMatrix(new Coordinates(15, 80), data, "grid", null,
        properties);
    this.lang
        .addLine("hide \"t0\" \"t1\" \"t2\" \"t3\" \"t4\" \"t5\" \"t6\" \"t7\" \"t8\" \"t9\"");
  }

  // Initialize the data for the grid
  private void initData() {
    String[] arrayA = WortA.split("");
    String[] arrayB = WortB.split("");

    data = new String[WortB.length() + 2][WortA.length() + 2];
    data[1][1] = "0";
    data[0][0] = data[0][1] = data[1][0] = "";
    for (int i = 0; i < WortA.length(); i++) {
      data[0][i + 2] = arrayA[i + 1];
      data[1][i + 2] = String.valueOf(i + 1);
    }

    for (int i = 0; i < WortB.length(); i++) {
      data[i + 2][0] = arrayB[i + 1];
      data[i + 2][1] = String.valueOf(i + 1);
    }

    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        if (data[i][j] == null) {
          data[i][j] = "";
        }
      }
    }

    colors = new String[WortB.length() + 2][WortA.length() + 2];
    for (int i = 0; i < colors.length; i++)
      Arrays.fill(colors[i], "black");
  }

  // Initialize the SourceCode
  private void initCode() {
    // scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new
    // Color(255,0,0));
    // scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    // Font.PLAIN, 18));

    // Tried hacking font size, but failed...
    // sourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font(sourceCode.getName(), Font.PLAIN,
    // Integer.valueOf(sourceCode.getsize())));
    code = lang.newSourceCode(new Offset(25, 20, "grid", "NE"), "listSource",
        null, sourceCode);

    code.addCodeLine("Initialisierung der Matrix mit A = '" + WortA
        + "' und B = '" + WortB + "'", "", 0, null);
    code.addCodeLine("", "", 0, null);
    code.addCodeLine("Fuer alle Elemente von A; a = Zeichen, i = Index von a:",
        "", 0, null);
    code.addCodeLine("Fuer alle Elemente von B; b = Zeichen, i = Index von b:",
        "", 1, null);
    code.addCodeLine("1. Vergleiche a mit b", "", 2, null);
    code.addCodeLine("a) wenn a = b setze neq := 0", "", 3, null);
    code.addCodeLine("b) sonst setze neq := 1", "", 3, null);
    code.addCodeLine(
        "2. s := Minimum der 2 x 2 Matrix mit i,j als rechten unteren Index",
        "", 2, null);
    code.addCodeLine(
        "3. Setze den rechten unteren Wert der 2 x 2 Matrix auf s + neq", "",
        2, null);
  }

  // private void preAlgorithm() {
  // // header
  // TextProperties fp = new TextProperties();
  // fp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
  // Font.BOLD, 25));
  // fp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
  // lang.newText(new Coordinates(20, 30), "Levenshtein Distanz", "header",
  // null, fp);
  // RectProperties rp = new RectProperties();
  // rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
  // lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5,
  // "header", "SE"), "headerRec", null, rp);
  // }

  private void animate(int id, int p1, int p2, int p3) {
    String color1 = "(" + Highlight1.getRed() + "," + Highlight1.getGreen()
        + "," + Highlight1.getBlue() + ")";
    String color2 = "(" + Highlight2.getRed() + "," + Highlight2.getGreen()
        + "," + Highlight2.getBlue() + ")";
    switch (id) {
      case 1: {
        lang.nextStep("Der Algorithmus");
        // Highlight A
        for (int k = 0; k < WortA.length(); k++) {
          setGridColor(0, k + 2, color1);
        }
        // Highlight B
        for (int k = 0; k < WortB.length(); k++) {
          setGridColor(k + 2, 0, color1);
        }
        code.highlight(0);
        // **********************************
        break;
      }
      case 2: {
        // Highlight remaining Chars of A
        lang.nextStep();
        setAllGridColor("black");
        for (int k = p1; k < WortA.length() + 1; k++) {
          setGridColor(0, k + 1, color1);
        }
        code.unhighlight(0);
        code.unhighlight(8);
        code.highlight(2);
        // **********************************
        break;
      }
      case 3: {
        // Highlight remaining Chars of B
        lang.nextStep();
        setAllGridColor("black");
        for (int k = p1; k < WortB.length() + 1; k++) {
          setGridColor(k + 1, 0, color1);
        }
        code.unhighlight(2);
        code.unhighlight(8);
        code.highlight(3);
        // **********************************
        break;
      }
      case 4: {
        // Highlight chars, which are compared
        lang.nextStep();
        setAllGridColor("black");
        setGridColor(p1 + 1, 0, color1);
        setGridColor(0, p2 + 1, color1);
        code.unhighlight(3);
        code.highlight(4);
        // **********************************

        // Highlight if chars are equal or not
        lang.nextStep();
        if (grid.getElement(p1 + 1, 0).equals(grid.getElement(0, p2 + 1)))
          code.highlight(5);
        else
          code.highlight(6);

        // Highlight the 2x2 Matrix
        lang.nextStep();
        setAllGridColor("black");
        setGridColor(p1, p2, color1);
        setGridColor(p1, p2 + 1, color1);
        setGridColor(p1 + 1, p2, color1);
        code.unhighlight(4);
        code.unhighlight(5);
        code.unhighlight(6);
        code.highlight(7);
        // **********************************

        // Add Result and highlight green
        lang.nextStep();
        grid.put(p1 + 1, p2 + 1, String.valueOf(p3), null, null);
        setGridColor(p1 + 1, p2 + 1, color2);
        code.unhighlight(7);
        code.highlight(8);
        // **********************************
        break;
      }

    }
  }

  private void result(int distance) {
    TextProperties fp = new TextProperties();
    fp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));
    fp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
    lang.newText(new Offset(10, 80, "grid", "SW"),
        "Unser Ergebnis ist der Wert im Punkt i,j. In diesem Falle der Wert "
            + distance + ".", "Ergebnis", null, fp);
  }

  private void setGridColor(int zeile, int spalte, String color) {
    String line = "setGridColor \"grid[" + zeile + "][" + spalte
        + "]\" TextColor " + color;
    lang.addLine(line);
    colors[zeile][spalte] = color;
  }

  private void setAllGridColor(String color) {
    for (int i = 0; i < WortB.length() + 2; i++)
      for (int j = 0; j < WortA.length() + 2; j++)
        if (colors[i][j] != color)
          setGridColor(i, j, color);
  }

  /**
   * This helper-function builds a Text object with its corresponding properties
   * It is used in order to build the outro text after the Levensthein algorithm
   * stops.
   */
  public Text buildText(String id, String idRef, int x, int y, String text,
      int fontsize) {
    DisplayOptions displayOptions = new TicksTiming(0);
    TextGenerator textGenerator = new AnimalTextGenerator(lang);
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, fontsize));

    return new Text(textGenerator, new Offset(x, y, idRef, "SW"), text, id,
        displayOptions, textProperties);
  }

  public void buildIntro() {
    TextProperties fp = new TextProperties();
    fp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 26));
    fp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);

    lang.newText(new Coordinates(20, 30), "Levenshtein Distanz", "header",
        null, fp);
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.cyan);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 11);
    lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header",
        "SE"), "headerRec", null, rp);

    lang.nextStep();

    buildText("t0", "header", 0, 50,
        "Vorgeschichte zu dem Levenshtein Algorithmus...", 24);

    lang.nextStep();

    buildText(
        "t1",
        "t0",
        0,
        30,
        "Die sogenannte Levenshtein-Distanz ist eine Metrik um feststellen zu koennen, inwieweit sich zwei Strings aehneln.",
        16);
    buildText(
        "t2",
        "t1",
        0,
        5,
        "Mithilfe der Metrik kann die Auskunft ueber die minimale Anzahl von Einfuege-, Loesch- und Substitution-Operationen,",
        16);
    buildText("t3", "t2", 0, 5,
        "um den ersten String in den zweiten umzuwandeln, erbracht werden.", 16);
    buildText(
        "t4",
        "t3",
        0,
        30,
        "Sie wurde 1965 von dem russischen Wissenschaftler Wladimir Lewenstein eingefuehrt und findet bis heute noch",
        16);
    buildText("t5", "t4", 0, 5, "Anwendung in den folgenden Gebieten:", 16);
    buildText("t6", "t5", 300, 5, "   - Rechtschreibkorrektur", 16);
    buildText("t7", "t6", 0, 10, "   - Spracherkennung", 16);
    buildText("t8", "t7", 0, 10, "   - DNA Analyse", 16);
    buildText("t9", "t8", 0, 10, "   - Plagiarismus Detektion", 16);
  }

  public void buildOutro() {
    lang.nextStep("Das Ergebnis");
    this.lang.addLine("hide \"grid\" \"listSource\" \"Ergebnis\"");
    buildText("f0", "header", 0, 50,
        "Levensthein Implementierung & Schwierigkeiten...", 24);

    buildText(
        "f1",
        "f0",
        0,
        30,
        "So nuetzlich der Levenshtein-Algorithmus auch ist, umso schwieriger ist es diesen in der Praxis anzuwenden. Warum?",
        16);
    buildText(
        "f2",
        "f1",
        0,
        5,
        "Sowohl die Laufzeit als auch der Speicherbedarf des Algorithmus belaufen sich auf: O(m*n). Dies wird dadurch begruendet",
        16);
    buildText(
        "f3",
        "f2",
        0,
        5,
        "das saemtliche Elemente der Matrix berechnet werden muessen und der Rechenaufwand fuer jedes Matrixelement",
        16);
    buildText(
        "f4",
        "f3",
        0,
        5,
        "dadurch konstant ist. Fuer zwei Strings die die selbe Laenge haben ergibt sich somit eine quadratische Laufzeit,",
        16);
    buildText(
        "f5",
        "f4",
        0,
        5,
        "welche fuer eine eher kleine Wortliste anwendbar ist. Fuehrt man jedoch String-Vergleiche im grossen Stil mit mehreren",
        16);
    buildText(
        "f6",
        "f5",
        0,
        5,
        "Millionen Eintraegen durch (z.B. Google), so ist die Anwendung des Algorithmus nicht wirklich praxistauglich.",
        16);
    buildText(
        "f7",
        "f6",
        0,
        30,
        "Es existieren Firmen wie z.B. die Exorbyte GmbH, welche es nach aufwaendiger Forschung und Entwicklung geschafft",
        16);
    buildText(
        "f8",
        "f7",
        0,
        5,
        "haben mit dem Levenshtein-Algorithmus String-Vergleiche auf einer Datenbasis von c.a. 2,5 Mio. Daten unter",
        16);
    buildText(
        "f9",
        "f8",
        0,
        5,
        "10 Millisekunden durchzufuehren. Ihre Technologie wird z.B. von Suchmaschinen wie Yahoo!, Overture, Miva.com oder T-info genutzt.",
        16);
    buildText("f10", "f9", 0, 30,
        "Quelle: http://www.levenshtein.de/levenshtein_implementation.htm", 16);

    lang.nextStep();
  }

  // work around some Animal bugs
  private String workAround(String input) {
    String bg = "(" + Matrix_Background.getRed() + ","
        + Matrix_Background.getGreen() + "," + Matrix_Background.getBlue()
        + ")";
    String tc = "(" + Matrix_Schriftfarbe.getRed() + ","
        + Matrix_Schriftfarbe.getGreen() + "," + Matrix_Schriftfarbe.getBlue()
        + ")";

    String input2 = input;
    input2 = input2
        .replace(
            "grid \"grid\" (15, 80) lines "
                + String.valueOf(WortB.length() + 2)
                + " columns "
                + String.valueOf(WortA.length() + 2)
                + "  color (0, 0, 0) elementColor (0, 0, 0) fillColor (0, 0, 0) highlightTextColor (0, 0, 0) highlightBackColor (0, 0, 0) depth 1",
            "grid \"grid\" (15,80) lines "
                + String.valueOf(WortB.length() + 2)
                + " columns "
                + String.valueOf(WortA.length() + 2)
                + " style table cellWidth 30 cellHeight 30 color "
                + tc
                + " FillColor "
                + bg
                + " highlightTextColor black highlightFillColor blue highlightBorderColor red align center");
    input2 = input2.replaceAll(" refresh", "");
    input2 = input2.replaceAll(" row 0", "");

    String[] temp = input2.split("}");
    String newOut = "";
    for (int i = 0; i < temp.length - 1; i++) {
      if (temp[i].contains("Plagiarismus") && temp[i].contains("t9")) {
        temp[i] = temp[i] + "}" + System.getProperty("line.separator")
            + "Label \"Intro\"";
      } else {
        temp[i] = temp[i] + "}";
      }
      newOut = newOut + temp[i];
    }
    return newOut + System.getProperty("line.separator")
        + "label \"Zusammenfassung\"";
  }

  // ******************************************************************************************************

  public String getName() {
    return "Levenshtein Distanz";
  }

  public String getAlgorithmName() {
    return "Levenshtein-Distanz";
  }

  public String getAnimationAuthor() {
    return "Peter Baumann, Oren Avni";
  }

  public String getDescription() {
    return " Die sogenannte Levenshtein-Distanz ist eine Metrik um feststellen zu k&ouml;nnen, inwieweit sich zwei Strings &auml;hneln."
        + "\n"
        + " Mithilfe der Metrik kann die Auskunft &uuml;ber die minimale Anzahl von Einf&uuml;ge-, L&ouml;sch- und Substitution-Operationen,"
        + "\n"
        + " um den ersten String in den zweiten umzuwandeln, erbracht werden."
        + "\n"
        + " Sie wurde 1965 von dem russischen Wissenschaftler Wladimir Lewenstein eingef&uuml;hrt und findet bis heute noch"
        + "\n"
        + " Anwendung in den folgenden Gebieten:"
        + "\n"
        + "   - Rechtschreibkorrektur"
        + "\n"
        + "   - Spracherkennung"
        + "\n"
        + "   - DNA Analyse" + "\n" + "   - Plagiarismus Detektion";
  }

  public String getCodeExample() {
    return "  Initialisierung der Matrix mit A = 'OPA' und B = 'POP'"
        + "\n"
        + "  "
        + "\n"
        + "  F&uuml;r alle Elemente von A; a = Zeichen, i = Index von a:"
        + "\n"
        + "  	F&uuml;r alle Elemente von B; b = Zeichen, i = Index von b:"
        + "\n"
        + "  		1. Vergleiche a mit b"
        + "\n"
        + "  			a) wenn a = b setze neq := 0"
        + "\n"
        + "  			b) sonst setze neq := 1"
        + "\n"
        + " 		 2. s := Minimum der 2 x 2 Matrix mit i,j als rechten unteren Index"
        + "\n"
        + " 		 3. Setze den rechten unteren Wert der 2 x 2 Matrix auf s + neq";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}