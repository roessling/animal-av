package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class GaussElim implements ValidatingGenerator {

  private Language             lang;
  private int[][]              intMatrix;
  private SourceCodeProperties scProps;
  private TextProperties       varsProp;
  private MatrixProperties     matrixProp;

  private static final String  ALGORITHMNAME = "Gauss Eliminationsverfahren";

  private static final String  AUTHOR        = "Mateusz Umstädter, Aidin Dinkhah, Jakub Pilarski";

  private static final String  DESCRIPTION   = "Das Gau&szlig;sche Eliminationsverfahren ist ein Algorithmus aus den mathematischen Teilgebieten der linearen Algebra und der Numerik.<br />"
                                                 + "Es ist ein wichtiges Verfahren zum L&ouml;sen von linearen Gleichungssystemen und beruht darauf, <br />"
                                                 + "dass elementare Umformungen zwar das Gleichungssystem &auml;ndern, aber die L&ouml;sung erhalten. <br />"
                                                 + "Dies erlaubt es, jedes Gleichungssystem auf Stufenform zu bringen, an der die L&ouml;sung <br />"
                                                 + "durch sukzessive Elimination der Unbekannten leicht ermittelt oder die L&ouml;sungsmenge abgelesen werden kann.";

  private static final String  SOURCE_CODE   = "GrundKonzept des Gau&szlig;schen Eliminationsverfahrens" // 0
                                                 + "\n 	F&uuml;r k = 0, 1, ... , n-1:" // 1
                                                 + "\n 	1. Spaltenpivotsuche: Für i=k+1, ..., n-1: Bestimme max|A[i][k]|." // 2
                                                 + "\n 		Falls A[i][k] == 0, A ist singul&auml;r." // 3
                                                 + "\n	 	Vertausche Zeilen von i und k" // 4
                                                 + "\n 	2. Elimination: Substrahiere die Zeile k multipliziert mit dem Faktor Alpha = A[i][k] / A[k][k]" // 5
                                                 + "\n 		von der Zeile i, i=k+1,...,n-1 und f&uuml;ge Alpha an der Stelle A[i][k] ein." // 6
                                                 + "\n 	3. Iteration: Wende f&uuml;r k=2, ... , n-1 Schritt 1. und 2. an.";                                          // 7

  public GaussElim() {

  }

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    boolean result = true;
    intMatrix = (int[][]) primitives.get("intMatrix");
    int length1 = intMatrix.length;
    int length2 = intMatrix[0].length;
    if ((length1 + 1) != length2) {
      result = false;
      throw new IllegalArgumentException(
          "Die Matrix hat unterschiedliche Dimensionen.");
    }
    return result;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    intMatrix = (int[][]) primitives.get("intMatrix");
    scProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    varsProp = (TextProperties) props.getPropertiesByName("textProperties");
    matrixProp = (MatrixProperties) props
        .getPropertiesByName("matrixProperties");

    if (validateInput(props, primitives)) {
      double[][] doubleMatrix = new double[intMatrix.length][intMatrix[0].length];

      for (int i = 0; i < intMatrix.length; i++) {
        for (int j = 0; j < intMatrix[i].length; j++) {
          doubleMatrix[i][j] = intMatrix[i][j];
        }
      }

      calculate(doubleMatrix, props);
    }
    return lang.toString();
  }

  public void init() {
    lang = new AnimalScript(ALGORITHMNAME, AUTHOR, 800, 600);
    lang.setStepMode(true);

    TextProperties titleProp = new TextProperties();
    titleProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 20));

    Text title = lang.newText(new Coordinates(10, 35),
        "Gauss Eliminationsverfahren", "title", null, titleProp);

    RectProperties rectProp = new RectProperties();
    rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.FALSE);

    Rect rect = lang.newRect(new Offset(-5, -5, title,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, title,
        AnimalScript.DIRECTION_SE), "rectangle", null, rectProp);

    rect.show();

    TextProperties descrhdProp = new TextProperties();
    descrhdProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 18));

    Text descrhd = lang.newText(new Coordinates(20, 80),
        "Gauss Eliminationsverfahren: Beschreibung", "descrhd", null,
        descrhdProp);

    SourceCodeProperties descrProp = new SourceCodeProperties();
    descrProp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    descrProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    descrProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    descrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode descr = lang.newSourceCode(new Offset(-40, 50, title,
        AnimalScript.DIRECTION_S), "descr", null, descrProp);
    descr.addCodeLine(
        "Das gausssche Eliminationsverfahren ist ein Algorithmus aus den ",
        null, 0, null);
    descr.addCodeLine(
        "mathematischen Teilgebieten der linearen Algebra und der Numerik.",
        null, 0, null);
    descr
        .addCodeLine(
            "Es ist ein wichtiges Verfahren zum Loesen von linearen Gleichungssystemen und beruht darauf, ",
            null, 0, null);
    descr
        .addCodeLine(
            "dass elementare Umformungen zwar das Gleichungssystem aendern, aber die Loesung erhalten. ",
            null, 0, null);
    descr
        .addCodeLine(
            "Dies erlaubt es, jedes Gleichungssystem auf Stufenform zu bringen, an der die Loesung ",
            null, 0, null);
    descr.addCodeLine(
        "durch sukzessive Elimination der Unbekannten leicht ermittelt ", null,
        0, null);
    descr.addCodeLine("oder die Loesungsmenge abgelesen werden kann.", null, 0,
        null);

    lang.nextStep();
    descrhd.hide();
    descr.hide();

  }

  private void calculate(double[][] inputA, AnimationPropertiesContainer props) {
    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, props.get(
        "sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));

    SourceCode code = lang.newSourceCode(new Coordinates(30, 100), "code",
        null, scProps);

    code.addCodeLine("Grundkonzept des Gaußschen Eliminationsverfahrens", null,
        0, null); // 0
    code.addCodeLine("Fuer k = 0, 1, ... , n-1:", null, 1, null); // 1
    code.addCodeLine(
        "1. Spaltenpivotsuche: Fuer i=k+1, ..., n-1: Bestimme max|A[i][k]|.",
        null, 1, null); // 2
    code.addCodeLine("Falls A[i][k] == 0, A ist singulaer.", null, 2, null); // 3
    code.addCodeLine("Vertausche Zeilen von i und k.", null, 2, null); // 4
    code.addCodeLine(
        "2. Elimination: Substrahiere die Zeile k multipliziert mit dem Faktor Alpha = A[i][k] / A[k][k]",
        null, 1, null); // 5
    code.addCodeLine(
        "von der Zeile i, i=k+1,...,n-1 und f&uuml;ge Alpha an der Stelle A[i][k] ein.",
        null, 2, null); // 6
    code.addCodeLine(
        "3. Iteration: Wende fuer k=2, ... , n-1 Schritt 1. und 2. an.", null,
        1, null); // 7

    matrixProp = new MatrixProperties();
    matrixProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.FALSE);
    matrixProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
        props.get("matrixProperties", AnimationPropertiesKeys.FONT_PROPERTY));
    matrixProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("matrixProperties", AnimationPropertiesKeys.COLOR_PROPERTY));
    matrixProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, props.get(
        "matrixProperties", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    matrixProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    matrixProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);

    DoubleMatrix a = lang.newDoubleMatrix(new Coordinates(710, 100), inputA,
        "Matrix", null, matrixProp);

    varsProp = new TextProperties();
    varsProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("textProperties", AnimationPropertiesKeys.COLOR_PROPERTY));
    varsProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
        props.get("textProperties", AnimationPropertiesKeys.FONT_PROPERTY));

    Text maximum = lang.newText(new Offset(0, 150, code, null), "", "Maximum",
        null, varsProp);
    Text rkrow = lang.newText(new Offset(0, 150, code, null), "", "rk", null,
        varsProp);
    Text substr2 = lang.newText(new Offset(0, 150, code, null), "", "b[]",
        null, varsProp);
    Text substr = lang.newText(new Offset(0, 20, substr2, null), "", "A[][]",
        null, varsProp);

    SourceCodeProperties sumProps = new SourceCodeProperties();
    sumProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    int n = inputA.length - 1;
    int len = inputA[0].length;
    double EPSILON = 1e-10;
    code.highlight(0, 0, false);
    lang.nextStep();
    code.toggleHighlight(0, 0, false, 1, 0);
    lang.nextStep();
    for (int i = 0; i < n; i++) {
      code.unhighlight(7);
      code.toggleHighlight(1, 0, false, 2, 0);
      int counter = i + 1;
      lang.nextStep("Schritt " + counter);
      int max = i;
      a.highlightCellColumnRange(i, 0, len - 1, null, null);
      maximum.setText(("Maximum Spalte: " + max + " (Initialisierung)"), null,
          null);
      lang.nextStep();
      for (int j = i + 1; j < n; j++) {
        a.highlightCellColumnRange(j, 0, len - 1, null, null);
        if (Math.abs(inputA[j][i]) > Math.abs(inputA[max][i])) {
          maximum.setText("Maximum Spalte: " + max + ", da |" + inputA[j][i]
              + "| > |" + inputA[max][i] + "|", null, null);
          max = j;
          a.unhighlightCellColumnRange(i, 0, len - 1, null, null);

        }
        lang.nextStep();
        a.unhighlightCellColumnRange(i, 0, len - 1, null, null);
        a.unhighlightCellColumnRange(j, 0, len - 1, null, null);
      }
      a.highlightCellColumnRange(i, 0, len - 1, null, null);
      a.highlightCellColumnRange(max, 0, len - 1, null, null);
      code.toggleHighlight(2, 0, false, 4, 0);
      maximum.setText("", null, null);
      rkrow.setText("r=" + max + ", k=" + i, null, null);
      lang.nextStep("Vertauschen.");

      double[] temp = new double[len];
      for (int x = 0; x < len; x++) {
        temp[x] = inputA[i][x];
      }

      for (int x = 0; x < len; x++) {
        a.put(i, x, inputA[max][x], null, null);
        a.put(max, x, temp[x], null, null);
      }

      lang.nextStep();
      a.unhighlightCellColumnRange(i, 0, len - 1, null, null);
      a.unhighlightCellColumnRange(max, 0, len - 1, null, null);
      rkrow.setText("", null, null);
      code.toggleHighlight(4, 0, false, 5, 0);
      code.highlight(6);
      lang.nextStep();
      if (Math.abs(inputA[i][i]) <= EPSILON) {
        code.toggleHighlight(2, 0, false, 3, 0);
        maximum.setText("Matrix ist Singulaer!", null, null);
        break;
      }
      for (int k = i + 1; k < len - 1; k++) {
        double alpha = inputA[k][i] / inputA[i][i];
        a.highlightCellColumnRange(k, 0, len - 1, null, null);
        a.put(k, inputA[0].length - 1, (inputA[k][len - 1] -= alpha
            * inputA[i][len - 1]), null, null);
        substr2.setText("b[" + k + "] = b[" + i + "] * (A[" + k + "][" + i
            + "] / A[" + i + "][" + i + "]) = " + alpha + " * "
            + inputA[i][len - 1] + " = " + inputA[k][len - 1], null, null);
        lang.nextStep();
        for (int l = i; l < len - 1; l++) {
          a.put(k, l, (inputA[k][l] -= alpha * inputA[i][l]), null, null);
          substr
              .setText("A[" + k + "][" + l + "] = A[" + i + "][" + l
                  + "] * (A[" + k + "][" + i + "] / A[" + i + "][" + i
                  + "]) = " + inputA[i][l] + " * " + alpha + " = "
                  + inputA[k][l], null, null);
          lang.nextStep();
        }
        lang.nextStep();
        a.unhighlightCellColumnRange(k, 0, len - 1, null, null);
        substr.setText("", null, null);

      }
      substr.setText("", null, null);
      substr2.setText("", null, null);
      code.toggleHighlight(6, 0, false, 7, 0);
      code.unhighlight(5);
      lang.nextStep();
    }
    code.unhighlight(5);
    code.unhighlight(7);
    substr.hide();
    substr2.hide();
    rkrow.hide();
    SourceCode summary2 = lang.newSourceCode(new Offset(0, 140, code, null),
        "summary2", null, sumProps);
    SourceCode summary3 = lang.newSourceCode(new Offset(0, 300, code, null),
        "summary3", null, sumProps);
    summary2.addCodeLine("Somit erhalten wir die Gleichungen:", null, 0, null);
    // Backsubstitution
    double[] x = new double[len - 1];
    String sumText = "";
    String sumText2 = "";
    for (int i = n; i >= 0; i--) {
      double sum = 0.0;
      for (int j = i + 1; j < n + 1; j++) {
        sum += inputA[i][j] * x[j];
      }
      x[i] = (inputA[i][len - 1] - sum) / inputA[i][i];
    }

    // Print backsubstitution
    for (int i = 0; i < inputA.length; i++) {
      for (int j = 0; j < inputA[i].length; j++) {
        if (j == inputA[i].length - 2) {
          sumText += "A[" + i + "][" + j + "] * x[" + j + "] = b[" + i + "]";
          sumText2 += inputA[i][j] + " * " + x[j] + " = " + inputA[i][len - 1];
          break;
        } else {
          sumText += "A[" + i + "][" + j + "] * x[" + j + "] + ";
          sumText2 += inputA[i][j] + " * " + x[j] + " + ";
        }
      }
      summary2.addCodeLine(sumText, null, 0, null);
      summary3.addCodeLine(sumText2, null, 0, null);
      sumText = "";
      sumText2 = "";
    }
    summary2.show();
    summary3.show();
    lang.nextStep("Zusammenfassung.");
  }

  public String getName() {
    return ALGORITHMNAME;
  }

  public String getAlgorithmName() {
    return ALGORITHMNAME;
  }

  public String getAnimationAuthor() {
    return AUTHOR;
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
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
