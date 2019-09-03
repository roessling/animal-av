package generators.cryptography.helpers;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * 
 * @author Stefan Käsdorf, Marco Drebing
 */
public class MixColumns {

  private Language             lang;

  private int[][]              stateMatrix;
  private int[][]              resultMatrix = new int[4][4];
  private static final int[][] mdsMatrix    = { { 2, 3, 1, 1 }, { 1, 2, 3, 1 },
      { 1, 1, 2, 3 }, { 3, 1, 1, 2 }       };
  private String               result;

  private StringMatrix         animalStateMatrix;
  private StringMatrix         animalNewColumn;
  private StringMatrix         animalMDSMatrix;
  private StringMatrix         animalOldColumn;
  private StringMatrix         animalResultMatrix;

  private MatrixProperties     animalMatrixProps;

  private Text                 animalMDSMatrixText;
  private Text                 animalEquals;

  private Text                 animalResult0;
  private Text                 animalResult1;
  private Text                 animalCalc0;
  private Text                 animalCalc1;
  private Text                 animalCalc2;
  private Text                 animalCalc3;
  private Text                 animalCalc4;

  private Text                 animalRowText;
  private Text                 animalColText;
  private Text                 animalIText;

  private Polyline             animalHorizontalLine;

  private SourceCode           animalDescription;
  private SourceCode           animalClosingWord;
  private SourceCode           animalMixColumnsSc;

  private SourceCodeProperties animalSourceCodeProps;

  public MixColumns(Language l, int[][] stateMatrix,
      MatrixProperties matrixProps, SourceCodeProperties sourceCodeProps) {
    lang = l;
    lang.setStepMode(true);
    this.stateMatrix = stateMatrix;
    this.animalMatrixProps = matrixProps;
    this.animalSourceCodeProps = sourceCodeProps;
  }

  public void mixColumns() {
    // initialize the header
    initializeAnimalHeader();
    lang.nextStep();
    // initialize the description
    initializeAnimalDescription();
    lang.nextStep("Description");
    // initialize the calculation matrices
    initializeAnimalStateMatrix(stateMatrix);
    initializeAnimalCalculationMatrix();
    initializeAnimalResultMatrix();
    initializeAnimalSourceCode();
    initializeAnimalPolynomCalculation();
    initializeAnimalLoopVarText();
    initializeAnimalHorizontalLine();
    hideAnimalCalculationMatrix();
    hidePolynomCalculation();

    animalDescription.hide();
    animalResult0.hide();

    lang.nextStep();

    animalMixColumnsSc.highlight(0);
    animalMixColumnsSc.highlight(9);

    // for each statematrix column
    for (int col = 0; col < 4; col++) {

      lang.nextStep();

      // highlight matrices
      animalStateMatrix.highlightCellRowRange(0, 3, col, new TicksTiming(0),
          new TicksTiming(0));
      if (col > 0) {
        animalNewColumn.unhighlightCellRowRange(0, 3, 0, new TicksTiming(0),
            new TicksTiming(0));
        animalResultMatrix.unhighlightCellRowRange(0, 3, col - 1,
            new TicksTiming(0), new TicksTiming(0));
      }

      if (col == 0) {
        animalColText.show();
        animalHorizontalLine.show();
      }

      hideResult();

      // highlight sourceCode
      highlightText(animalColText);
      animalColText.setText("col = " + col, new TicksTiming(0),
          new TicksTiming(0));
      animalMixColumnsSc.highlight(1);
      animalMixColumnsSc.highlight(8);
      animalMixColumnsSc.unhighlight(7);
      animalMixColumnsSc.unhighlight(0);
      animalMixColumnsSc.unhighlight(9);
      // fill oldColumnVector
      fillStateVector(col);
      int[] colVec = new int[4];
      colVec[0] = stateMatrix[0][col];
      colVec[1] = stateMatrix[1][col];
      colVec[2] = stateMatrix[2][col];
      colVec[3] = stateMatrix[3][col];

      lang.nextStep("stateMatrix column = " + col);

      // highlight newColumn
      animalNewColumn.highlightCellRowRange(0, 3, 0, new TicksTiming(0),
          new TicksTiming(0));
      // highlight sourceCode
      unhighlightText(animalColText);
      animalMixColumnsSc.highlight(2);
      animalMixColumnsSc.unhighlight(1);
      animalMixColumnsSc.unhighlight(8);
      if (col == 0) {
        showAnimalCalculationMatrix();
      }

      // for each madsMatrix row
      for (int row = 0; row < 4; row++) {
        lang.nextStep();

        // highlight matrices
        animalNewColumn.unhighlightCellRowRange(0, 3, 0, new TicksTiming(0),
            new TicksTiming(0));
        animalOldColumn.highlightCellRowRange(0, 3, 0, new TicksTiming(0),
            new TicksTiming(0));
        animalStateMatrix.highlightCellRowRange(0, 3, col, new TicksTiming(0),
            new TicksTiming(0));
        animalMDSMatrix.highlightCellColumnRange(row, 0, 3, new TicksTiming(0),
            new TicksTiming(0));

        // highlight sourceCode
        unhighlightText(animalResult0);
        unhighlightText(animalResult1);
        highlightText(animalRowText);
        animalMixColumnsSc.unhighlight(2);
        animalMixColumnsSc.highlight(3);
        animalMixColumnsSc.highlight(6);
        animalMixColumnsSc.unhighlight(1);
        animalMixColumnsSc.unhighlight(8);
        animalMixColumnsSc.unhighlight(5);

        animalRowText.setText("row = " + row, new TicksTiming(0),
            new TicksTiming(0));
        if (row == 0) {
          animalRowText.show();
        }

        // set and highlight calculation
        result = "result = ";
        animalResult1.setText("", new TicksTiming(0), new TicksTiming(0));
        setResultText("");
        hideResult();

        lang.nextStep("    mdsMatrix row = " + row);

        // highlight sourceCode
        unhighlightText(animalRowText);
        animalMixColumnsSc.highlight(4);
        animalMixColumnsSc.unhighlight(3);
        animalMixColumnsSc.unhighlight(6);
        animalMixColumnsSc.highlight(14);
        animalMixColumnsSc.highlight(24);

        lang.nextStep();

        // highlight and set calculation text
        animalResult0.show();
        highlightText(animalResult0);

        // start calculation
        int[] rowVec = mdsMatrix[row];
        int val = multipyVector(rowVec, colVec);
        animalResult1.setText("           = "
            + Integer.toHexString(val).toUpperCase(), new TicksTiming(0),
            new TicksTiming(0));
        animalResult1.show();

        // highlight matrices
        animalNewColumn.highlightCellRowRange(row, row, 0, new TicksTiming(0),
            new TicksTiming(0));
        animalOldColumn.unhighlightCellRowRange(0, 3, 0, new TicksTiming(0),
            new TicksTiming(0));
        animalMDSMatrix.unhighlightCellColumnRange(row, 0, 3,
            new TicksTiming(0), new TicksTiming(0));
        animalStateMatrix.unhighlightCellRowRange(0, 3, col,
            new TicksTiming(0), new TicksTiming(0));

        lang.nextStep();

        // highlight matrices
        animalMixColumnsSc.unhighlight(23);
        animalMixColumnsSc.unhighlight(4);
        animalMixColumnsSc.highlight(5);

        // write down result
        animalNewColumn.put(row, 0, Integer.toHexString(val).toUpperCase(),
            new TicksTiming(0), new TicksTiming(0));
        resultMatrix[row][col] = val;

      }
      lang.nextStep();

      // highlight sourceCode
      animalMixColumnsSc.unhighlight(5);
      animalMixColumnsSc.highlight(3);
      animalMixColumnsSc.highlight(6);
      highlightText(animalRowText);
      animalRowText.setText("row = 4", new TicksTiming(0), new TicksTiming(0));

      // highlight calculation text
      unhighlightText(animalResult0);
      unhighlightText(animalResult1);
      hideResult();

      lang.nextStep("    mdsMatrix row = 4");

      // highlight matrices
      animalNewColumn.highlightCellRowRange(0, 3, 0, new TicksTiming(0),
          new TicksTiming(0));
      animalResultMatrix.highlightCellRowRange(0, 3, col, new TicksTiming(0),
          new TicksTiming(0));

      // highlight sourceCode
      unhighlightText(animalRowText);
      animalRowText.hide();
      animalMixColumnsSc.unhighlight(3);
      animalMixColumnsSc.unhighlight(6);
      animalMixColumnsSc.unhighlight(5);
      animalMixColumnsSc.highlight(7);

      lang.nextStep();

      // swap new column into the resultMatrix
      lang.addLine("swapGridValues \"newColumn[0][0]\" and \"resultMatrix[0]["
          + col + "]\" refresh after 0 ticks within 200 ticks");
      lang.addLine("swapGridValues \"newColumn[1][0]\" and \"resultMatrix[1]["
          + col + "]\" refresh after 0 ticks within 200 ticks");
      lang.addLine("swapGridValues \"newColumn[2][0]\" and \"resultMatrix[2]["
          + col + "]\" refresh after 0 ticks within 200 ticks");
      lang.addLine("swapGridValues \"newColumn[3][0]\" and \"resultMatrix[3]["
          + col + "]\" refresh after 0 ticks within 200 ticks");
    }
    lang.nextStep();

    // highlight sourceCode
    animalColText.setText("col = 4", new TicksTiming(0), new TicksTiming(0));
    highlightText(animalColText);
    animalMixColumnsSc.highlight(1);
    animalMixColumnsSc.highlight(8);
    animalMixColumnsSc.unhighlight(7);

    // highlightMatrices
    animalNewColumn.unhighlightCellRowRange(0, 3, 0, new TicksTiming(0),
        new TicksTiming(0));
    animalResultMatrix.unhighlightCellRowRange(0, 3, 3, new TicksTiming(0),
        new TicksTiming(0));

    lang.nextStep("stateMatrix column = 4");

    // highlight sourceCode
    unhighlightText(animalColText);
    animalColText.hide();
    animalHorizontalLine.hide();
    animalMixColumnsSc.unhighlight(1);
    animalMixColumnsSc.unhighlight(8);

    lang.nextStep();

    // highlight matrices
    hideAnimalCalculationMatrix();
    animalMixColumnsSc.hide();
    animalResultMatrix.highlightCellColumnRange(0, 0, 3, new TicksTiming(0),
        new TicksTiming(0));
    animalResultMatrix.highlightCellColumnRange(1, 0, 3, new TicksTiming(0),
        new TicksTiming(0));
    animalResultMatrix.highlightCellColumnRange(2, 0, 3, new TicksTiming(0),
        new TicksTiming(0));
    animalResultMatrix.highlightCellColumnRange(3, 0, 3, new TicksTiming(0),
        new TicksTiming(0));

    // show colsing word
    initializeAnimalColsingWord();

    lang.nextStep("Final slide");
  }

  /**
   * returns the polynomial representation of the given int
   * 
   * @param decimal
   *          - the int to use
   * @return the polynom
   */
  private String getPolynom(int decimal) {

    // String[] exps = {"⁰", "", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹"};
    String[] exps = { "", "", "^2", "^3", "^4", "^5", "^6", "^7", "^8", "^9" };
    StringBuffer sb = new StringBuffer();
    String binary = Integer.toBinaryString(decimal);
    int expo = binary.length() - 1;
    for (int i = 0; i < binary.length(); i++) {
      char chr = binary.charAt(i);
      if (chr == '1') {
        if (i == binary.length() - 1) {
          if (sb.length() == 0) {
            sb.append("1");
          } else {
            sb.append(" + 1");
          }
        } else {
          if (sb.length() == 0) {
            sb.append("x" + exps[expo]);
          } else {
            sb.append(" + x" + exps[expo]);
          }
        }
      }
      expo -= 1;
    }
    return sb.toString().equals("") ? "0" : sb.toString();
  }

  /**
   * Multiplies the given vectors an multiplies the result modulo the fix
   * polynom
   * 
   * @param rowVec
   *          - the row vector
   * @param colVec
   *          - the column vector
   * @return the result
   */
  private int multipyVector(int[] rowVec, int[] colVec) {
    String poly;
    String hex;

    int[] vals = new int[4];

    // highlight sourceCode
    animalMixColumnsSc.unhighlight(14);
    animalMixColumnsSc.unhighlight(24);
    animalMixColumnsSc.highlight(15);

    // for each value of the row vector
    for (int i = 0; i < 4; i++) {

      lang.nextStep();

      // highlight calculation text
      unhighlightText(animalResult0);

      // highlight sourceCode
      if (i == 0) {
        animalIText.show();
      }
      highlightText(animalIText);
      animalIText.setText("i = " + i, new TicksTiming(0), new TicksTiming(0));
      animalMixColumnsSc.unhighlight(20);
      animalMixColumnsSc.unhighlight(21);
      animalMixColumnsSc.unhighlight(15);
      animalMixColumnsSc.highlight(16);
      animalMixColumnsSc.highlight(22);
      lang.nextStep();

      // highlight sourceCode
      unhighlightText(animalIText);
      animalMixColumnsSc.unhighlight(16);
      animalMixColumnsSc.unhighlight(22);
      animalMixColumnsSc.highlight(17);
      animalMixColumnsSc.highlight(18);

      // resume the calculation
      int val = mod(mult(rowVec[i], colVec[i]));
      vals[i] = val;

      lang.nextStep();

      // highlight sourceCode
      animalMixColumnsSc.unhighlight(19);
      animalMixColumnsSc.highlight(20);

      // highlight calculation text
      unhighlightText(animalCalc2);
      highlightText(animalCalc3);
      highlightText(animalCalc4);

      // create calculation text
      poly = getPolynom(val);
      animalCalc3.setText("= " + poly + " (mod x^8 + x^4 + x^3 + x + 1)",
          new TicksTiming(0), new TicksTiming(0));
      hex = Integer.toHexString(val).toUpperCase();
      animalCalc3.show();
      animalCalc4.setText("= " + hex, new TicksTiming(0), new TicksTiming(0));
      animalCalc4.show();
      setResultText(hex);

      lang.nextStep();

      // highlight sourceCode
      animalMixColumnsSc.unhighlight(20);
      animalMixColumnsSc.highlight(21);

      // highlight calculation text
      unhighlightText(animalCalc3);
      unhighlightText(animalCalc4);
      highlightText(animalResult0);
      hidePolynomCalculation();
    }

    lang.nextStep();

    // highlight sourceCode
    animalIText.setText("i = 4", new TicksTiming(0), new TicksTiming(0));
    highlightText(animalIText);
    animalMixColumnsSc.unhighlight(20);
    animalMixColumnsSc.unhighlight(21);
    animalMixColumnsSc.highlight(16);
    animalMixColumnsSc.highlight(22);

    lang.nextStep();

    // highlight sourceCode
    unhighlightText(animalIText);
    animalIText.hide();
    highlightText(animalResult1);
    animalMixColumnsSc.unhighlight(16);
    animalMixColumnsSc.unhighlight(22);
    animalMixColumnsSc.highlight(23);

    // compute and return result
    return (vals[0] ^ vals[1] ^ vals[2] ^ vals[3]);
  }

  /**
   * multiplies the value with the given factor
   * 
   * @param factor
   *          - the factor to use
   * @param val
   *          - the value to use
   * @return the result
   */
  private int mult(int factor, int val) {

    // crate the calculationtext
    animalCalc2.hide();
    animalCalc3.hide();
    animalCalc4.hide();
    String hex0 = Integer.toHexString(factor);
    String hex1 = Integer.toHexString(val);
    animalCalc0.setText("   " + hex0 + " * " + hex1, new TicksTiming(0),
        new TicksTiming(0));
    animalCalc0.show();
    highlightText(animalCalc0);
    String poly0 = getPolynom(factor);
    String poly1 = getPolynom(val);
    animalCalc1.setText("= (" + poly0 + ") * (" + poly1 + ")", new TicksTiming(
        0), new TicksTiming(0));
    animalCalc1.show();
    highlightText(animalCalc1);
    switch (factor) {
      case 1:
        return val;
      case 2:
        return val << 1;
      case 3:
        return (val << 1) ^ val;
      default:
        return -1;
    }
  }

  /**
   * Multiplies the given value modulo the fix polynom
   * 
   * @param val
   *          - the value to use
   * @return the result
   */
  private int mod(int val) {
    lang.nextStep();

    // highlight the calculation text
    unhighlightText(animalCalc0);
    unhighlightText(animalCalc1);

    // highlight the sourceCode
    animalMixColumnsSc.unhighlight(17);
    animalMixColumnsSc.unhighlight(18);
    animalMixColumnsSc.highlight(19);

    // start the calculation
    int val2 = val;
    String poly = getPolynom(val2);
    animalCalc2.setText("= " + poly, new TicksTiming(0), new TicksTiming(0));
    animalCalc2.show();
    highlightText(animalCalc2);
    if (val2 >= 256) {
      byte b = (byte) val2;
      val2 = b & 0xFF;
      val2 = val2 ^ 27;
    }
    return val2;
  }

  /**
   * fills the newColumn vector with the stateMatrix values of the given column
   * 
   * @param col
   *          - the given column
   */
  private void fillStateVector(int col) {
    animalOldColumn.put(0, 0, animalStateMatrix.getElement(0, col),
        new TicksTiming(0), new TicksTiming(0));
    animalOldColumn.put(1, 0, animalStateMatrix.getElement(1, col),
        new TicksTiming(0), new TicksTiming(0));
    animalOldColumn.put(2, 0, animalStateMatrix.getElement(2, col),
        new TicksTiming(0), new TicksTiming(0));
    animalOldColumn.put(3, 0, animalStateMatrix.getElement(3, col),
        new TicksTiming(0), new TicksTiming(0));
  }

  /**
   * Converts the given int matrix into a string matrix with hex values
   * 
   * @param inputMatrix
   *          - the given matrix
   * @return the string matrix
   */
  private String[][] convertMatrixToHex(int[][] inputMatrix) {
    String[][] outputMatrix = new String[inputMatrix.length][inputMatrix[0].length];
    for (int i = 0; i < inputMatrix.length; i++) {
      for (int j = 0; j < inputMatrix[0].length; j++) {
        outputMatrix[i][j] = Integer.toHexString(inputMatrix[i][j])
            .toUpperCase();
      }
    }
    return outputMatrix;
  }

  private void initializeAnimalHeader() {
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    lang.newText(new Coordinates(20, 30), "MixColumns", "header", null,
        textProps);

    RectProperties hRectProps = new RectProperties();
    hRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null,
        hRectProps);
  }

  private void initializeAnimalDescription() {
    animalDescription = lang.newSourceCode(new Coordinates(200, 100), "desc",
        null);
    animalDescription
        .addCodeLine(
            "The MixColumns is an operation of the Rijndael-Cipher. Each step computes",
            null, 0, null);
    animalDescription
        .addCodeLine(
            "one column of the resultmatrix for the given statematrix. It takes four",
            null, 0, null);
    animalDescription
        .addCodeLine(
            "bytes as input and outputs four bytes, where each input byte affects all",
            null, 0, null);
    animalDescription
        .addCodeLine(
            "four output bytes. Along with the shift-rows step, it is the primary source",
            null, 0, null);
    animalDescription.addCodeLine("of diffusion in Rijndael.", null, 0, null);
    animalDescription
        .addCodeLine(
            "Each column is converted into a polynomial and is then multiplied with",
            null, 0, null);
    animalDescription.addCodeLine(
        "the fixed MDS-Matrix. The result will be multiplied modulo the fixed",
        null, 0, null);
    animalDescription.addCodeLine("polynomial x^8 + x^4 + x^3 + x + 1.", null,
        0, null);
  }

  private void initializeAnimalStateMatrix(int[][] matrix) {
    String[][] stringMatrix = convertMatrixToHex(matrix);
    animalMatrixProps
        .set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    animalStateMatrix = lang.newStringMatrix(new Coordinates(100, 200),
        stringMatrix, "stateMatrix", null, animalMatrixProps);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    lang.newText(new Offset(0, -30, "stateMatrix", AnimalScript.DIRECTION_NW),
        "Statematrix", "stateMatrixText", null, textProps);
  }

  private void initializeAnimalResultMatrix() {
    String[][] stringMatrix = { { "", "", "", "" }, { "", "", "", "" },
        { "", "", "", "" }, { "", "", "", "" } };
    animalMatrixProps
        .set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    animalResultMatrix = lang.newStringMatrix(new Offset(0, 100, "stateMatrix",
        AnimalScript.DIRECTION_SW), stringMatrix, "resultMatrix", null,
        animalMatrixProps);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    lang.newText(new Offset(0, -30, "resultMatrix", AnimalScript.DIRECTION_NW),
        "Resultmatrix", "resultMatrixText", null, textProps);
  }

  private void initializeAnimalSourceCode() {
    animalMixColumnsSc = lang
        .newSourceCode(
            new Offset(50, 0, "oldColumn", AnimalScript.DIRECTION_NE),
            "mixColumnsSc", null, animalSourceCodeProps);

    animalMixColumnsSc.addCodeLine("mixColumns(Matrix hexMatrix){", null, 0,
        null);
    animalMixColumnsSc.addCodeLine(
        "for(int column = 0; column < 4; column++){", null, 1, null);
    animalMixColumnsSc.addCodeLine("Hex[] resultColumn = new Hex[4];", null, 2,
        null);
    animalMixColumnsSc.addCodeLine("for(int row = 0; row < 4; row++){", null,
        2, null);
    animalMixColumnsSc.addCodeLine(
        "hexValue = vectorMultplication(stateMatrix.column, mdsMatrix.row);",
        null, 3, null);
    animalMixColumnsSc.addCodeLine("resultColumn[row] = hexValue;", null, 3,
        null);
    animalMixColumnsSc.addCodeLine("}", null, 2, null);
    animalMixColumnsSc.addCodeLine("resultMatrix.addColumn(resultColumn);",
        null, 2, null);
    animalMixColumnsSc.addCodeLine("}", null, 1, null);
    animalMixColumnsSc.addCodeLine("}", null, 0, null);

    animalMixColumnsSc.addCodeLine("", null, 0, null);
    animalMixColumnsSc.addCodeLine("", null, 0, null);
    animalMixColumnsSc.addCodeLine("", null, 0, null);
    animalMixColumnsSc.addCodeLine("", null, 0, null);
    animalMixColumnsSc.addCodeLine(
        "calculation(Column stateMatrix.column, Row mdsMatrix.row){", null, 0,
        null);
    animalMixColumnsSc.addCodeLine("Hex[] result = new Hex[4];", null, 1, null);
    animalMixColumnsSc
        .addCodeLine("for(int i = 0; i < 4; i++){", null, 1, null);
    animalMixColumnsSc.addCodeLine(
        "statePolynom = stateMatrix.column[i].toPolynom;", null, 2, null);
    animalMixColumnsSc.addCodeLine("mdsPolynom = mdsMatrix.row[i].toPolynom;",
        null, 2, null);
    animalMixColumnsSc.addCodeLine(
        "multipliedPolynom = statePolynom * mdsPolynom;", null, 2, null);
    animalMixColumnsSc.addCodeLine(
        "resultPolynom = multiploedPolynom mod (x⁸ + x⁴ + x³ + x + 1);", null,
        2, null);
    animalMixColumnsSc.addCodeLine("result[i] = resultPolynom.toHex;", null, 2,
        null);
    animalMixColumnsSc.addCodeLine("}", null, 1, null);
    animalMixColumnsSc.addCodeLine(
        "return result[0] xor result[1] xor result[2] xor result[3];", null, 1,
        null);
    animalMixColumnsSc.addCodeLine("}", null, 0, null);
  }

  private void highlightText(Text text) {
    text.changeColor(AnimalScript.COLORCHANGE_COLOR,
        (Color) animalSourceCodeProps
            .get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY),
        new TicksTiming(0), new TicksTiming(0));
  }

  private void unhighlightText(Text text) {
    text.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
        new TicksTiming(0), new TicksTiming(0));
  }

  private void initializeAnimalLoopVarText() {
    animalColText = lang.newText(new Offset(35, 16, "mixColumnsSc",
        AnimalScript.DIRECTION_NE), "col = ", "colText", null);
    animalRowText = lang.newText(new Offset(35, 48, "mixColumnsSc",
        AnimalScript.DIRECTION_NE), "row = ", "rowText", null);
    animalIText = lang.newText(new Offset(35, 256, "mixColumnsSc",
        AnimalScript.DIRECTION_NE), "i = ", "iText", null);
    animalColText.hide();
    animalRowText.hide();
    animalIText.hide();
  }

  private void initializeAnimalHorizontalLine() {
    Node[] nodes = {
        new Offset(20, -10, "mixColumnsSc", AnimalScript.DIRECTION_NE),
        new Offset(20, 10, "mixColumnsSc", AnimalScript.DIRECTION_SE) };
    animalHorizontalLine = lang.newPolyline(nodes, "hoizontalLine", null);
    animalHorizontalLine.hide();
  }

  private void initializeAnimalCalculationMatrix() {
    initializeAnimalNewColumn();
    initializeAnimalEquals();
    initializeAnimalMDSMatrix();
    initializeAnimalOldColumn();
  }

  private void initializeAnimalNewColumn() {
    String[][] stringNewColumn = { { "" }, { "" }, { "" }, { "" } };
    animalMatrixProps
        .set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    animalNewColumn = lang.newStringMatrix(new Offset(100, 0, "stateMatrix",
        AnimalScript.DIRECTION_NE), stringNewColumn, "newColumn", null,
        animalMatrixProps);
  }

  private void initializeAnimalEquals() {
    TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("SansSerif", Font.BOLD, 24));
    // textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    animalEquals = lang.newText(new Offset(15, 0, "newColumn",
        AnimalScript.DIRECTION_E), "=", "equals", null, textProps);
  }

  private void initializeAnimalMDSMatrix() {
    String[][] stringMDSMatrix = convertMatrixToHex(mdsMatrix);
    animalMatrixProps
        .set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    animalMDSMatrix = lang.newStringMatrix(new Offset(25, 0, "newColumn",
        AnimalScript.DIRECTION_NE), stringMDSMatrix, "mdsMatrix", null,
        animalMatrixProps);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    animalMDSMatrixText = lang.newText(new Offset(0, -30, "mdsMatrix",
        AnimalScript.DIRECTION_NW), "MDSMatrix", "mdsMatrixText", null,
        textProps);
  }

  private void initializeAnimalOldColumn() {
    String[][] stringColumn = { { "" }, { "" }, { "" }, { "" } };
    animalMatrixProps
        .set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    animalOldColumn = lang.newStringMatrix(new Offset(5, 0, "mdsMatrix",
        AnimalScript.DIRECTION_NE), stringColumn, "oldColumn", null,
        animalMatrixProps);
  }

  private void initializeAnimalPolynomCalculation() {
    animalResult0 = lang.newText(new Offset(0, 30, "newColumn",
        AnimalScript.DIRECTION_SW), "result = ", "result0", null);
    animalResult1 = lang.newText(new Offset(0, 5, "result0",
        AnimalScript.DIRECTION_SW), "", "result1", null);
    animalCalc0 = lang.newText(new Offset(0, 30, "result1",
        AnimalScript.DIRECTION_SW), "  hex x hex", "calc0", null);
    animalCalc1 = lang.newText(new Offset(0, 5, "calc0",
        AnimalScript.DIRECTION_SW), "= poly x poly", "calc1", null);
    animalCalc2 = lang.newText(new Offset(0, 5, "calc1",
        AnimalScript.DIRECTION_SW), "= poly 3", "calc2", null);
    animalCalc3 = lang.newText(new Offset(0, 5, "calc2",
        AnimalScript.DIRECTION_SW), "= poly4 (mod poly5)", "calc3", null);
    animalCalc4 = lang.newText(new Offset(0, 5, "calc3",
        AnimalScript.DIRECTION_SW), "= hex", "calc4", null);
  }

  private void initializeAnimalColsingWord() {
    animalClosingWord = lang.newSourceCode(new Offset(50, 0, "resultMatrix",
        AnimalScript.DIRECTION_NE), "closingWord", null);
    animalClosingWord.addCodeLine("MixColumns finished successfully.", null, 0,
        null);
    animalClosingWord.addCodeLine(
        "The resulting matrix may now be used as the new state", null, 0, null);
    animalClosingWord.addCodeLine(
        "matrix for the next steps of the AES algorithm.", null, 0, null);
  }

  private void showAnimalCalculationMatrix() {
    animalNewColumn.show();
    animalEquals.show();
    animalMDSMatrix.show();
    animalOldColumn.show();
    animalMDSMatrixText.show();
  }

  private void hideAnimalCalculationMatrix() {
    animalNewColumn.hide();
    animalEquals.hide();
    animalMDSMatrix.hide();
    animalOldColumn.hide();
    animalMDSMatrixText.hide();
  }

  private void setResultText(String hex) {
    if ("result = ".equals(result)) {
      result = result + hex;
      animalResult0.setText(result, new TicksTiming(0), new TicksTiming(0));
    } else {
      result = result + " xor " + hex;
      animalResult0.setText(result, new TicksTiming(0), new TicksTiming(0));
    }
  }

  private void hideResult() {
    animalResult0.hide();
    animalResult1.hide();
  }

  private void hidePolynomCalculation() {
    animalCalc0.hide();
    animalCalc1.hide();
    animalCalc2.hide();
    animalCalc3.hide();
    animalCalc4.hide();
  }
}
