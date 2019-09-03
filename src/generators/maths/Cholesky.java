package generators.maths;

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
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Peter Baumann, Oren Avni
 * @version 0.0.1, 22.09.2011
 * 
 */

public class Cholesky implements Generator {
  private Language                        lang;
  private Hashtable<String, StringMatrix> matrices            = new Hashtable<String, StringMatrix>();
  private SourceCode                      code;
  private Hashtable<String, String[][]>   colors              = new Hashtable<String, String[][]>();
  private int[][]                         inputMatrix;
  private double[][]                      workingMatrix;
  private Color                           Matrix_Background   = Color.cyan;
  private Color                           Matrix_Schriftfarbe = Color.black;
  private Color                           Highlight1          = Color.green;
  private Color                           Highlight2          = Color.red;
  String                                  h1                  = "("
                                                                  + Highlight1
                                                                      .getRed()
                                                                  + ","
                                                                  + Highlight1
                                                                      .getGreen()
                                                                  + ","
                                                                  + Highlight1
                                                                      .getBlue()
                                                                  + ")";
  String                                  h2                  = "("
                                                                  + Highlight2
                                                                      .getRed()
                                                                  + ","
                                                                  + Highlight2
                                                                      .getGreen()
                                                                  + ","
                                                                  + Highlight2
                                                                      .getBlue()
                                                                  + ")";
  String                                  standard            = "("
                                                                  + Matrix_Schriftfarbe
                                                                      .getRed()
                                                                  + ","
                                                                  + Matrix_Schriftfarbe
                                                                      .getGreen()
                                                                  + ","
                                                                  + Matrix_Schriftfarbe
                                                                      .getBlue()
                                                                  + ")";
  private SourceCodeProperties            sourceCode;
  Text                                    sum;
  private boolean                         first               = true;

  public void init() {
    lang = new AnimalScript("Cholesky Verfahren", "Peter Baumann, Oren Avni",
        1000, 800);
    lang.setStepMode(true);
    matrices = new Hashtable<String, StringMatrix>();
    code = null;
    colors = new Hashtable<String, String[][]>();
    first = true;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    inputMatrix = (int[][]) primitives.get("Eingabe Matrix");
    Matrix_Background = (Color) primitives.get("Matrix Hintergrundfarbe");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    Highlight2 = (Color) primitives.get("Matrix Highlight2");
    Highlight1 = (Color) primitives.get("Matrix Highlight1");
    Matrix_Schriftfarbe = (Color) primitives.get("Matrix Textfarbe");
    h1 = "(" + Highlight1.getRed() + "," + Highlight1.getGreen() + ","
        + Highlight1.getBlue() + ")";
    h2 = "(" + Highlight2.getRed() + "," + Highlight2.getGreen() + ","
        + Highlight2.getBlue() + ")";
    standard = "(" + Matrix_Schriftfarbe.getRed() + ","
        + Matrix_Schriftfarbe.getGreen() + "," + Matrix_Schriftfarbe.getBlue()
        + ")";

    buildIntro();
    decompose();
    buildOutro();

    return workAround(lang.toString());
  }

  private String[][] numberToStringArray(double[][] matrix) {
    String[][] mystring = new String[matrix.length][matrix[0].length];
    for (int i = 0; i < mystring.length; i++) {
      for (int j = 0; j < mystring[i].length && j < matrix[i].length; j++) {
        mystring[i][j] = matrix[i][j] + "";
      }
    }
    return mystring;
  }

  private double[][] intToDoubleArray(int[][] matrix) {
    double[][] mydouble = new double[matrix.length][matrix[0].length];
    for (int i = 0; i < mydouble.length; i++) {
      for (int j = 0; j < mydouble[i].length && j < matrix[i].length; j++) {
        mydouble[i][j] = matrix[i][j] + 0.0;
      }
    }
    return mydouble;
  }

  private void initGrid(String gridId, Offset coord, String[][] datasource) {
    MatrixProperties properties = new MatrixProperties();
    properties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");

    StringMatrix temp_grid = lang.newStringMatrix(coord, datasource, gridId,
        null, properties);
    matrices.put(gridId, temp_grid);

    String[][] temp_colors = new String[workingMatrix.length][workingMatrix.length];
    for (int i = 0; i < temp_colors.length; i++)
      Arrays.fill(temp_colors[i], standard);
    colors.put(gridId, temp_colors);
  }

  // // Initialize the grid for being drawn
  // private void initGrid(String gridId, Coordinates coord, String[][]
  // datasource) {
  // MatrixProperties properties = new MatrixProperties();
  // properties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
  //
  // StringMatrix temp_grid = lang.newStringMatrix(coord, datasource, gridId,
  // null, properties);
  // matrices.put(gridId, temp_grid);
  //
  // String[][] temp_colors = new
  // String[workingMatrix.length][workingMatrix.length];
  // for (int i = 0; i < temp_colors.length; i++)
  // Arrays.fill(temp_colors[i], standard);
  // colors.put(gridId, temp_colors);
  // }

  private void setGridColor(String gridId, int zeile, int spalte, String color) {
    String[][] temp_colors = colors.get(gridId);
    String line = "setGridColor \"" + gridId + "[" + zeile + "][" + spalte
        + "]\" TextColor " + color;
    lang.addLine(line);
    temp_colors[zeile][spalte] = color;
    colors.put(gridId, temp_colors);
  }

  private void setAllGridColor(String gridId, String color) {
    String[][] temp_colors = colors.get(gridId);
    for (int i = 0; i < workingMatrix.length; i++)
      for (int j = 0; j < workingMatrix.length; j++)
        if (temp_colors[i][j] != color)
          setGridColor(gridId, i, j, color);
  }

  private String[][] emptyMatrix() {
    String[][] empty = new String[workingMatrix.length][workingMatrix.length];
    for (int i = 0; i < empty.length; i++) {
      Arrays.fill(empty[i], "");
    }
    return empty;
  }

  private double[][] checkMatrix(double[][] matrix) {
    double[][] matrix2 = matrix;
    if (!isSquare(matrix2) || !isSymmetric(matrix2)) {
      // if not a Square matrix or not Symmetric, then animate the following
      // Problem
      double[][] temp = { { 5, 7, 3 }, { 7, 11, 2 }, { 3, 2, 6 } };
      matrix2 = temp;
      Text alert = lang
          .newText(
              new Offset(0, 5, "header", "SW"),
              "Die Eingabe Matrix war keine symmetrische n x n Matrix! Animiere daher Beispiel Matrix.",
              "alert", null);
      alert.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red,
          null, null);
    }
    return matrix2;
  }

  private void animate(int id, int p1, int p2, int p3, double p4) {
    switch (id) {
      case 1: {
        // Write the working matrix. If invalid Matrix, use a simple problem of
        // a 3 x 3 matrix
        workingMatrix = checkMatrix(intToDoubleArray(inputMatrix));

        // Matrix A
        initGrid("A", new Offset(0, 50, "header", "SW"),
            numberToStringArray(workingMatrix));
        lang.newText(new Offset(30, -20, "A", "NW"), "Matrix A", "Atitle", null);
        this.lang
            .addLine("hide \"i0\" \"i1\" \"i2\" \"i3\" \"i4\" \"i5\" \"i6\" \"i7\" \"i8\" \"i9\" \"i10\" \"i11\" ");

        // Matrix L
        initGrid("L", new Offset(200, 0, "A", "NE"), emptyMatrix());
        lang.addLine("hide \"L\"");

        // Arrow
        PolylineProperties pfeilProps = new PolylineProperties();
        pfeilProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        Node[] pfeil_node = { new Offset(20, 70, "A", "NE"),
            new Offset(-20, 70, "L", "NW") };
        lang.newPolyline(pfeil_node, "pfeil", null, pfeilProps);

        // Sum Text
        sum = lang.newText(new Offset(30, -20, "pfeil", "Nw"), "Summe: " + p4,
            "sum", null);
        sum.hide();

        lang.nextStep();
        lang.newText(new Offset(30, -20, "L", "NW"), "Matrix L", "Ltitle", null);
        lang.addLine("show \"L\"");
        lang.nextStep();
        initCode();

        lang.nextStep("Der Algorithmus");
        for (int i = 0; i < workingMatrix.length; i++) {
          if (matrices.get("L").getElement(i, i) != "  D")
            matrices.get("L").put(i, i, "  D", null, null);
        }
        code.highlight(0);
        break;
      }
      case 2: {
        lang.nextStep("");
        // TO DO soll ich die Farbe lassen?
        setGridColor("L", p1, p2, h1);
        matrices.get("L").put(p1, p2, "0.0", null, null);
        code.highlight(0);
        break;
      }
      case 3: {
        lang.nextStep();
        setAllGridColor("L", standard);
        for (int i = 0; i < workingMatrix.length; i++)
          matrices.get("L").put(i, i, "", null, null);
        break;
      }
      case 4: {
        if (first) {
          lang.nextStep("Initialisierung");
          first = false;
        } else {
          lang.nextStep(p1 + ". Diag. Element");
        }

        code.unhighlight(0);
        code.unhighlight(7);
        code.unhighlight(8);
        code.unhighlight(9);
        code.unhighlight(10);
        code.highlight(2);
        setAllGridColor("L", standard);
        setAllGridColor("A", standard);
        sum.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black,
            null, null);
        sum.setText("Summe: 0.0", null, null);
        sum.hide();
        for (int i = p1; i < workingMatrix.length; i++) {
          if (matrices.get("L").getElement(i, 0) == "")
            matrices.get("L").put(i, 0, "  X", null, null);
        }

        break;
      }
      case 5: {

        lang.nextStep();
        code.unhighlight(2);
        code.highlight(3);
        code.unhighlight(7);
        code.unhighlight(8);
        code.unhighlight(9);
        code.unhighlight(10);
        setAllGridColor("L", standard);
        setAllGridColor("A", standard);
        sum.setText("Summe: 0.0", null, null);
        sum.hide();

        for (int i = p1; i < workingMatrix.length; i++)
          if (matrices.get("L").getElement(i, 0) == "  X")
            matrices.get("L").put(i, 0, "", null, null);

        for (int j = 0; j <= p1; j++)
          if (matrices.get("L").getElement(p1, j) == "")
            matrices.get("L").put(p1, j, "  X", null, null);
        lang.nextStep();
        code.unhighlight(3);
        code.highlight(4);
        for (int j = 0; j <= p1; j++)
          if (matrices.get("L").getElement(p1, j) == "  X")
            matrices.get("L").put(p1, j, "", null, null);
        sum.show();

        lang.nextStep();
        code.unhighlight(4);
        code.highlight(5);

        // Highlight Spalten k kleiner j
        for (int k = 0; k < p2; k++) {
          setGridColor("L", p1, k, h2);
        }
        break;
      }
      case 6: {
        lang.nextStep();
        code.highlight(6);
        code.unhighlight(5);
        setAllGridColor("L", standard);
        setGridColor("L", p1, p2, h2);
        setGridColor("L", p3, p2, h2);
        sum.setText("Summe: " + p4, null, null);
        break;
      }
      case 7: {
        lang.nextStep();
        setAllGridColor("A", standard);
        setAllGridColor("L", standard);
        setGridColor("A", p1, p1, h2);
        setGridColor("L", p1, p1, h2);
        sum.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Highlight2,
            null, null);
        code.unhighlight(5);
        code.unhighlight(6);
        code.highlight(7);
        code.highlight(8);
        break;
      }
      case 8: {
        lang.nextStep();
        setAllGridColor("A", standard);
        setAllGridColor("L", standard);
        setGridColor("A", p1, p2, h2);
        setGridColor("L", p2, p2, h2);
        sum.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Highlight2,
            null, null);
        code.unhighlight(5);
        code.unhighlight(6);
        code.highlight(9);
        code.highlight(10);
        break;
      }
      case 9: {
        setGridColor("L", p1, p2, h1);
        matrices.get("L").put(p1, p2, workingMatrix[p1][p2] + "", null, null);
        break;
      }
      case 10: {
        setAllGridColor("L", h2);
        setAllGridColor("A", h2);
        code.highlight(12);
        code.highlight(13);
        code.unhighlight(8);
        code.unhighlight(7);
        for (int i = 0; i < workingMatrix.length; i++) {
          for (int j = 0; j < workingMatrix.length; j++) {
            matrices.get("L").put(i, j, " :-(", null, null);
          }
        }
      }
    }
  }

  // return Cholesky factor L of psd matrix A = L L^T
  public double[][] decompose() {
    // Initialisieriung: Setze alle Elemente von L oberhalb der...
    animate(1, 0, 0, 0, 0);

    // set the aij with j > i to 0
    for (int j = 1; j < workingMatrix.length; j++) {
      for (int i = j; i < workingMatrix.length; i++) {
        animate(2, j - 1, i, 0, 0);
        workingMatrix[j - 1][i] = 0;
      }

    }
    animate(3, 0, 0, 0, 0);
    for (int i = 0; i < workingMatrix.length; i++) {
      // Fuer alle Zeilen i von L
      animate(4, i, 0, 0, 0);
      for (int j = 0; j <= i; j++) {
        // Fuer alle Spalten j kleiner gleich i von L
        animate(5, i, j, 0, 0);

        double sum = 0.0;
        for (int k = 0; k < j; k++) {
          // Fuer alle Spalten k kleiner j
          sum += workingMatrix[i][k] * workingMatrix[j][k];
          lang.addLine("#workingMatrix[" + i + "][" + k + "] * workingMatrix["
              + j + "][" + k + "]");
          animate(6, i, k, j, Math.round(sum * 100) / 100.0);
        }

        if (i == j) {
          workingMatrix[i][i] = Math
              .round(Math.sqrt(workingMatrix[i][i] - sum) * 100) / 100.0;
          animate(7, i, 0, 0, 0);
        } else {
          double temp = 1.0 / workingMatrix[j][j] * (workingMatrix[i][j] - sum);
          if (temp < 0) {
            temp = Math.round(temp * 10) / 10.0;
          } else {
            temp = Math.round(temp * 100) / 100.0;
          }
          workingMatrix[i][j] = temp;
          animate(8, i, j, 0, 0);
        }

        if (workingMatrix[i][i] > 0) {
          animate(9, i, j, 0, 0);
          // Matrix nicht positiv definit
        }
      }
      if (workingMatrix[i][i] <= 0) {
        animate(10, 0, 0, 0, 0);
        break;
      }
    }
    return workingMatrix;
  }

  private void initCode() {
    // // - just for now later to be removed
    // sourceCode = new SourceCodeProperties();
    // sourceCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.red);
    // //**************************************
    //
    code = lang.newSourceCode(new Offset(5, 5, "A", "SW"), "listSource", null,
        sourceCode);
    code.addCodeLine(
        "Initialisierung: Setze alle Elemente von L oberhalb der Hauptdiagonalen D auf 0",
        "", 0, null);
    code.addCodeLine("", "", 0, null);
    code.addCodeLine("Fuer alle Zeilen i von L:", "", 0, null);
    code.addCodeLine("Fuer alle Spalten j kleiner gleich i von L:", "", 2, null);
    code.addCodeLine("sum := 0", "", 4, null);
    code.addCodeLine("Fuer alle Spalten k kleiner j:", "", 4, null);
    code.addCodeLine("sum := sum + L[i][k] * L[j][k]", "", 6, null);
    code.addCodeLine("Falls j gleich i:", "", 4, null);
    code.addCodeLine("L[j][j] := Wurzel aus(A[j][j] - sum)", "", 6, null);
    code.addCodeLine("Sonst:", "", 4, null);
    code.addCodeLine("L[i][j] := (A[i][j] - sum) / L[j][j]", "", 6, null);
    code.addCodeLine("", "", 0, null);
    code.addCodeLine("Falls L[j][j] = 0 oder komplexe Zahl:", "", 2, null);
    code.addCodeLine("ABBRUCH: A ist nicht positiv definit!", "", 4, null);
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
            " lines "
                + workingMatrix.length
                + " columns "
                + workingMatrix.length
                + "  color (0, 0, 0) elementColor (0, 0, 0) fillColor (0, 0, 0) highlightTextColor (0, 0, 0) highlightBackColor (0, 0, 0) depth 1",
            " lines "
                + workingMatrix.length
                + " columns "
                + workingMatrix.length
                + " style matrix cellWidth 35 cellHeight 30 color "
                + tc
                + " FillColor "
                + bg
                + " highlightTextColor black highlightFillColor blue highlightBorderColor red align left");

    input2 = input2.replaceAll(" refresh", "");
    input2 = input2.replaceAll(" row 0", "");

    String[] temp = input2.split("}");
    String newOut = "";
    for (int i = 0; i < temp.length - 1; i++) {
      if (temp[i].contains("1914") && temp[i].contains("i11")) {
        temp[i] = temp[i] + "}" + System.getProperty("line.separator")
            + "Label \"Einleitung\"";
      } else {
        temp[i] = temp[i] + "}";
      }
      newOut = newOut + temp[i];
    }
    return newOut + System.getProperty("line.separator")
        + "label \"Zusammenfassung\"";
  }

  // is symmetric
  public static boolean isSymmetric(double[][] A) {
    for (int i = 0; i < A.length; i++)
      for (int j = 0; j < i; j++)
        if (A[i][j] != A[j][i])
          return false;

    return true;
  }

  // is square
  public static boolean isSquare(double[][] A) {
    for (int i = 0; i < A.length; i++)
      if (A[i].length != A.length)
        return false;

    return true;
  }

  public void print(double[][] arr) {
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < arr[0].length; j++) {
        System.out.print(arr[i][j] + " ");
      }
      System.out.println();
    }
  }

  public void buildIntro() {
    TextProperties fp = new TextProperties();
    fp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 26));
    fp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);

    lang.newText(new Coordinates(20, 30), "Cholesky-Verfahren", "header", null,
        fp);
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.cyan);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 11);
    lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header",
        "SE"), "headerRec", null, rp);

    lang.nextStep();
    buildText("i0", "header", 280, -35, "( Einleitung )", 24);
    lang.nextStep();

    buildText(
        "i1",
        "i0",
        -280,
        40,
        "Das Cholesky-Verfahren bezeichnet in der numerischen Mathematik die Zerlegung einer symmetrischen",
        16);
    buildText(
        "i2",
        "i1",
        0,
        5,
        "positiven definiten Matrix A in das Produkt einer unteren Dreiecksmatrix L und ihrer Transponierten L^T.",
        16);
    buildText(
        "i3",
        "i2",
        0,
        5,
        "Nach Berechnung von L bzw. L^T kann ein lineares Gleichungssystem A*x = b (wie beim Gauss-Algorithmus) ",
        16);
    buildText("i4", "i3", 0, 5,
        "mit Hilfe der beiden folgenden Gleichungssysteme geloest werden:", 16);
    buildText("i5", "i4", 0, 30, "1.) L*y = b", 16);
    buildText("i6", "i5", 0, 5, "2.) L^T*x = y", 16);
    buildText(
        "i7",
        "i6",
        0,
        30,
        "Das Verfahren wurde von dem gleichnahmigen franzoesischen Mathematiker Andre-Louis Cholesky vor",
        16);
    buildText(
        "i8",
        "i7",
        0,
        5,
        "1914 entwickelt, jedoch erst nach seinem Tod (31.08.1918) von seinem befreundeten Offizier",
        16);
    buildText("i9", "i8", 0, 5, "Commandant Benoit in 1924 veroeffentlicht.",
        16);
    buildText(
        "i10",
        "i9",
        0,
        30,
        "Quellen: http://www-history.mcs.st-andrews.ac.uk/Biographies/Cholesky.html",
        16);
    buildText("i11", "i10", 0, 5,
        "               http://www.mathematik.tu-darmstadt.de", 16);
  }

  public void buildOutro() {
    lang.nextStep();
    setAllGridColor("L", h2);
    setAllGridColor("A", standard);
    sum.setText("A = L * L^T", null, null);
    code.hide();
    lang.nextStep("Ergebnis");
    buildText("t0", "header", 280, -25, "( Intressante Eigenschaften )", 24);
    buildText(
        "t1",
        "t0",
        -284,
        25,
        "Das Cholesky-Verfahren hat neben der eigentlichen Zerlegung der Eingabematrix A in die Matrizen L und L^T weitere",
        16);
    buildText("t2", "t1", 0, 5,
        "nuetzliche Eigenschaften, die im Folgenden kurz erlaeutert werden:",
        16);

    buildText(
        "t3",
        "t2",
        0,
        15,
        "- Das Cholesky-Verfahren ist numerisch stabil. Der Grund hierfuer ist das Ziehen der Quadratwurzel beim Berechnen der Hauptdiagonalelemente.",
        16);
    buildText(
        "t4",
        "t3",
        0,
        5,
        "  Dabei werden die wesentlichen Ziffern naeher an das Komma herangezogen, was in jedem Berechnungsschritt einer Normalisierung aehnelt.",
        16);
    buildText(
        "t5",
        "t4",
        0,
        5,
        "  Auf eine Pivotisierung (wie z.B. beim Gauss-Verfahren) kann daher verzichtet werden.",
        16);

    buildText(
        "t6",
        "t5",
        0,
        15,
        "- Die Matrix A wird auf Definitheit geprueft (nur positiv definite Matrizen garantieren reelle Zahlen bei der Cholesky-Zerlegung). Dies",
        16);
    buildText(
        "t7",
        "t6",
        0,
        5,
        "  ist jedoch keine Einschraenkung der Anwendbarkeit des Verfahrens, denn fast alle symmetrischen Matrizen bei der Loesung von",
        16);
    buildText("t8", "t7", 0, 5,
        "  naturwissenschaftlich-technischen Problemen sind positiv definit.",
        16);

    buildText(
        "t9",
        "t8",
        0,
        15,
        "- Das Verfahren ist praedestiniert fuer die Programmierung. Das Element A[i][j] wird bei der Berechnung von L[i][j] letztmalig gebraucht,",
        16);
    buildText(
        "t10",
        "t9",
        0,
        5,
        "  sodass A von L ueberschrieben werden kann (minimaler Speicherbedarf). Weiterhin wird eine vorhandene Bandstruktur von A auf L uebertragen,",
        16);
    buildText(
        "t11",
        "t10",
        0,
        5,
        "  welches die Moeglichkeit bietet den Aufwand drastisch zu reduzieren Diese Eigenschaft wird bei Finite-Elemente Berechnungen ausgenutzt.",
        16);

    buildText(
        "t12",
        "t11",
        0,
        15,
        "- Die Determinante der Matrix A faellt bei dessen Zerlegung nebenbei an. Nach den Regeln fuer die Berechnung der Determinante eines Matrizenprodukts",
        16);
    buildText(
        "t13",
        "t12",
        0,
        5,
        "  und dem Laplace'schen Entwicklungssatzes ergibt sich die Formel: det(A) = det(L^T) * det(L) = ( L[1][1] * L[2][2] * ... * L[n][n] )^2",
        16);

    buildText(
        "t14",
        "t13",
        0,
        15,
        "- Da das Cholesky-Verfahren die Symmetrie ausnutzt, benoetigt es neben n Quadratwurzeln nur noch ca. (n*n*n)/6 Operationen.",
        16);
    buildText(
        "t15",
        "t14",
        0,
        5,
        "  Dies ist etwa die Haelfte der beim Gauss-Verfahren benoetigten Operationen.",
        16);

    buildText("t16", "t15", 0, 15, "Quellen: http://www.rzbt.haw-hamburg.de",
        16);
    buildText("t17", "t16", 0, 5,
        "               http://www.mathematik.tu-darmstadt.de", 16);
    lang.addLine("hide \"A\" \"L\" \"sum\" \"Atitle\" \"Ltitle\" \"pfeil\"");
  }

  public String getName() {
    return "Cholesky Verfahren";
  }

  public String getAlgorithmName() {
    return "Cholesky Verfahren";
  }

  public String getAnimationAuthor() {
    return "Peter Baumann, Oren Avni";
  }

  public String getDescription() {
    return "Das Cholesky-Verfahren bezeichnet in der numerischen Mathematik die Zerlegung einer symmetrischen "
        + "\n"
        + "positiven definiten Matrix A in das Produkt einer unteren Dreiecksmatrix L und ihrer Transponierten L^T."
        + "\n"
        + "Nach Berechnung von L bzw. L^T kann ein lineares Gleichungssystem A*x = b (wie beim Gauss-Algorithmus) "
        + "\n"
        + "mit Hilfe der folgenden gestaffelten Systeme geloest werden:"
        + "\n"
        + "\n"
        + "1.) L*y = b"
        + "\n"
        + "2.) L^T*x = y"
        + "\n"
        + "\n"
        + "Das Verfahren wurde von dem gleichnahmigen franzoesischen Mathematiker Andre-Louis Cholesky vor "
        + "\n"
        + "1914 entwickelt, jedoch erst nach seinem Tod (31.08.1918) von seinem befreundeten Offizier"
        + "\n"
        + "Commandant Benoit in 1924 veroeffentlicht.  "
        + "\n"
        + "  "
        + "\n"
        + "Quelle: http://www-history.mcs.st-andrews.ac.uk/Biographies/Cholesky.html  "
        + "\n" + "        http://www.mathematik.tu-darmstadt.de";
  }

  public String getCodeExample() {
    return "Initialisierung: Setze alle Elemente von L oberhalb der Hauptdiagonalen D auf 0"
        + "\n"
        + "\n"
        + "Fuer alle Zeilen i von L:"
        + "\n"
        + "	Fuer alle Spalten j kleiner gleich i von L:"
        + "\n"
        + "		sum := 0"
        + "\n"
        + "		Fuer alle Spalten k kleiner j:"
        + "\n"
        + "			sum := sum + L[i][k] * L[j][k]"
        + "\n"
        + "		Falls j gleich i:"
        + "\n"
        + "			L[j][j] := Wurzel aus(A[j][j] - sum)"
        + "\n"
        + "		Sonst:"
        + "\n"
        + "			L[i][j] := (A[i][j] - sum) / L[j][j]"
        + "\n"
        + "\n"
        + "	Falls L[j][j] = 0 oder komplexe Zahl:"
        + "\n"
        + "		ABBRUCH: A ist nicht positiv definit!";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}
