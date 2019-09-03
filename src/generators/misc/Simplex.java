package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

/**
 * @author Michael Scharf
 * 
 *         Praktikum - Visualisierung & Animation von Algorithmen und
 *         Datenstrukturen Aufgabe 7
 * 
 *         Simplex Algorithmus zur Loesung von Optimierungsproblemen.
 * 
 */
public class Simplex implements generators.framework.Generator {

  private Language            lang;
  private SourceCode          sc;
  private StringMatrix        matrix;
  private String[][]          sMatrix;

  private int[][]             a           = null;

  private static final String NAME        = "SimplexAlgorithm";

  private static final String DESCRIPTION = "Start mit zulässiger Basislösung."
                                              + " Falls nicht optimal: Übergang zu benachbarter Basislösung"
                                              + " durch Austauschen einer Basisvariable."
                                              + " Bei jedem Schritt: Verbesserung des Zielfunktionswertes."
                                              + " Fortsetzung, bis keine bessere Nachbarlösung mehr existiert: optimale Lösung identifiziert";

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */
  public Simplex(Language l) {

    // Store the language object
    lang = l;

    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);

    this.init();
  }

  /**
   * Default constructor
   */
  public Simplex() {
    this(new AnimalScript("SimplexAlgorithm", "Michael Scharf", 640, 480));
  }

  /**
   * This method transforms the given matrix of restrictions into a Simplex
   * table with descriptions.
   * 
   * @param a
   */
  public void generateSimplexTable(int[][] a) {

    // add one line for the column desctiptions
    // add one column for the line descriptions
    // add n columns for the slack variables
    String[][] st = new String[a.length + 1][a[0].length + a.length];

    // set the column descriptions
    for (int i = 1; i < st[0].length - 1; i++)
      st[0][i] = "x" + i + "   ";
    st[0][st[0].length - 1] = "b";

    // set the line descriptions
    for (int i = 1; i < st.length - 1; i++)
      st[i][0] = "x" + (a[0].length + i - 1);

    st[0][0] = "BV"; // base variables
    st[st.length - 1][0] = "F"; // F-Line

    // now coppy the values;
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[0].length - 1; j++) {
        st[i + 1][j + 1] = Integer.toString(a[i][j]);
      }
      st[i + 1][st[0].length - 1] = Integer.toString(a[i][a[i].length - 1]);
    }

    // now set the slack variables
    for (int i = 1; i < st.length; i++) {
      for (int j = a[0].length; j < st[0].length - 1; j++) {
        if (j - a[0].length + 1 == i)
          st[i][j] = "1";
        else
          st[i][j] = "0";
      }
    }
    sMatrix = st;
  }

  /**
   * Print the Simplex Table
   * 
   * @param prop
   */
  public void printTable(AnimationPropertiesContainer prop) {

    // The MatrixPropperties dont have enough Styles, so the next line is
    // necessary.
    lang.addLine("grid \"simplexTableau\"(20, 100) lines 5 columns 7 style table font Monospaced size 14");
    lang.addLine("hide \"simplexTableau\"");

    // first, set the visual properties (somewhat similar to CSS)
    MatrixProperties matrixProps = (MatrixProperties) prop
        .getPropertiesByName("matrix");

    // now, create the Matrix object, linked to the properties
    matrix = lang.newStringMatrix(new Coordinates(20, 100), sMatrix,
        "simplexTableau", null, matrixProps);

    lang.addLine("setGridColor \"simplexTableau[][]\"	fillcolor (220, 220, 220) ");
    lang.addLine("setGridColor \"simplexTableau[][0]\"  fillcolor white");
    lang.addLine("setGridColor \"simplexTableau[0][]\"  fillcolor white");

    SourceCodeProperties scProps = (SourceCodeProperties) prop
        .getPropertiesByName("source");

    // now, create the source code entity
    sc = lang.newSourceCode(new Coordinates(400, 140), "sourceCode", null,
        scProps);

    // Add the lines to the SourceCode object.
    sc.addCodeLine("while (keine optimale Lösung gefunden){", null, 0, null); // 0

    sc.addCodeLine("suche kleinstetes negatives Element F(t) in der F-Zeile;",
        null, 1, null);

    sc.addCodeLine("if (kein negatives Element gefunden)", null, 1, null);

    sc.addCodeLine("return aktuelle Lösung ist optimal;", null, 2, null); // 3

    sc.addCodeLine("else", null, 1, null); // 4

    sc.addCodeLine("Pivotspalte = t;", null, 2, null); // 5

    sc.addCodeLine("suche  min{ b(s) / a(s,t) mit a(s,t) > 0}", null, 1, null); // 6

    sc.addCodeLine("if (nichts gefunden)", null, 1, null); // 7

    sc.addCodeLine("return es existiert keine optimale Lösung;", null, 2, null); // 8

    sc.addCodeLine("else", null, 1, null); // 9

    sc.addCodeLine("Pivotzeile = s;", null, 2, null); // 10

    sc.addCodeLine("Pivotelement = a(s,t);", null, 1, null); // 11

    sc.addCodeLine("", null, 1, null); // 12

    sc.addCodeLine("x(t) wird neue Basisvariable;", null, 1, null); // 13

    sc.addCodeLine(
        "Bilde Einheitsvektor unter x(t) durch Lineartransformation", null, 1,
        null); // 14

    sc.addCodeLine("}", null, 0, null); // 15
  }

  /**
   * Print the Results of the Simplex Algorithm
   */
  public void printSolution() {

    Text[] results = new Text[sMatrix.length];

    results[0] = lang.newText(new Coordinates(20, 350), "Die Lösung lautet:",
        "statusText", null);

    for (int i = 0; i < a.length - 1; i++) {
      String varName = "x" + i;

      for (int j = 1; j < sMatrix.length; j++) {
        if (matrix.getElement(j, 0).contains(varName)) {
          results[i + 1] = lang.newText(
              new Coordinates(20, 350 + (i + 1) * 12),
              varName + "= " + matrix.getElement(j, sMatrix[0].length - 1),
              "statusText", null);
        }
      }
    }
    results[a.length - 1] = lang.newText(
        new Coordinates(20, 350 + (a.length) * 12),
        "Zielfunktionswert" + "= "
            + matrix.getElement(sMatrix.length - 1, sMatrix[0].length - 1),
        "statusText", null);

  }

  /**
   * Print the Restrictions of the Simplex. The restrictions can be generated
   * form the simplex table.
   */
  public void printRestrictions() {
    Text[] info = new Text[a.length + 3];

    info[0] = lang
        .newText(
            new Coordinates(20, 30),
            "Der Simplex-Algorithmus ist ein Verfahren zur Lösung linearer Optimierungsprobleme.",
            "statusText0", null);

    info[1] = lang.newText(new Coordinates(20, 50),
        "Gegeben ist folgendes Optimierungsproblem", "statusText1", null);

    StringBuffer sb = new StringBuffer();
    // Zielfunktion bestimmen
    sb.append("Maximiere: ");
    for (int j = 0; j < a[0].length - 1; j++) {

      sb.append(a[a.length - 1][j] * -1);
      sb.append(" * x");
      sb.append(j + 1);
      if (j < a[0].length - 2)
        sb.append(" + ");
    }

    info[2] = lang.newText(new Coordinates(20, 70), sb.toString(),
        "statusText2", null);

    info[3] = lang.newText(new Coordinates(20, 90),
        "Unter den Nebenbedingungen", "statusText3", null);

    // Nebenbedingungen bestimmen
    for (int i = 0; i < a.length - 1; i++) {
      sb = new StringBuffer();

      for (int j = 0; j < a[i].length - 1; j++) {

        sb.append(a[i][j]);
        sb.append(" * x");
        sb.append(j + 1);
        if (j < a[0].length - 2)
          sb.append(" + ");
      }

      sb.append(" = ");
      sb.append(a[i][a[i].length - 1]);

      info[4 + i] = lang.newText(new Coordinates(20, 110 + i * 20),
          sb.toString(), "statusText" + (i + 4), null);
    }

    lang.nextStep();

    for (int i = 0; i < info.length; i++) {
      info[i].hide();
    }
  }

  /**
   * Run Simplex Algorithm.
   */
  public void runSimplex() {

    // String[][] dataM = matrix.getData();

    int height = matrix.getNrRows();// dataM.length;
    int width = matrix.getNrCols();// dataM[0].length;
    boolean unsolved = true;

    while (unsolved) {
      // Highlight first line
      sc.highlight(0, 0, false);

      lang.nextStep();

      // Highlight next line
      sc.toggleHighlight(0, 0, false, 1, 0);
      // bestimme Pivotspalte
      int min_col = 0;
      double min_value = 0;
      for (int i = 1; i < width - 1; i++) {

        double el = Double.parseDouble(matrix.getElement(height - 1, i));

        // neues Minimum gefunden
        if (el < min_value) {
          matrix.unhighlightCell(height - 1, min_col, null, null);
          min_col = i;
          min_value = el;
          matrix.highlightCell(height - 1, i, null, null);
          lang.nextStep();
        }
      }

      // Highlight next line
      sc.toggleHighlight(1, 0, false, 2, 0);
      if (min_col == 0) {
        lang.nextStep();
        sc.toggleHighlight(2, 0, false, 3, 0);
        lang.newText(new Coordinates(20, 50),
            "aktuelle Basislösung ist optimal", "statusText", null);
        return;
      }
      // else
      lang.nextStep();
      sc.toggleHighlight(2, 0, false, 5, 0);

      lang.nextStep();
      sc.toggleHighlight(5, 0, false, 6, 0);

      // bestimme Pivotzeile
      int min_row = 0;
      min_value = Integer.MAX_VALUE;
      for (int i = 1; i < height - 1; i++) {

        double x_value = Double.parseDouble(matrix.getElement(i, min_col));
        double b = Double.parseDouble(matrix.getElement(i, width - 1));
        double el = b / x_value;
        // neues Minimum gefunden
        if (x_value > 0 && el < min_value) {
          matrix.unhighlightCell(min_row, width - 1, null, null);
          min_row = i;
          min_value = el;
          matrix.highlightCell(i, width - 1, null, null);
          lang.nextStep();
        }
      }
      sc.toggleHighlight(6, 0, false, 7, 0);
      if (min_row == 0) {
        lang.nextStep();
        sc.toggleHighlight(7, 0, false, 8, 0);

        lang.newText(new Coordinates(20, 50), "Modell ist unbeschränkt",
            "statusText", null);
        return;
      }
      // else
      lang.nextStep();
      sc.toggleHighlight(7, 0, false, 10, 0);

      lang.nextStep();
      sc.toggleHighlight(10, 0, false, 11, 0);
      // Pivot -Element bestimmen
      double pivot = Double.parseDouble(matrix.getElement(min_row, min_col));
      matrix.highlightCell(min_row, min_col, null, null);
      matrix.unhighlightCell(min_row, width - 1, null, null);
      matrix.unhighlightCell(height - 1, min_col, null, null);

      matrix.highlightCell(0, min_col, null, null);
      matrix.highlightCell(min_row, 0, null, null);

      lang.nextStep();
      sc.toggleHighlight(11, 0, false, 13, 0);

      // Pivotspalte in Basis aufnehmen
      matrix.put(min_row, 0, matrix.getElement(0, min_col), null, null);

      lang.nextStep();

      sc.toggleHighlight(13, 0, false, 14, 0);
      matrix.unhighlightCell(0, min_col, null, null);
      matrix.unhighlightCell(min_row, 0, null, null);

      lang.nextStep();

      // Gauß-Jordan-Algorithmus anwenden.
      for (int i = 1; i < height; i++) {
        matrix.highlightCellColumnRange(i, 1, width - 1, null, null);
        if (i == min_row) { // Soderfall Pivotzeile
          for (int j = 1; j < width; j++) {
            double value = Double.parseDouble(matrix.getElement(i, j));
            value = r(value / pivot);
            matrix.put(i, j, Double.toString(value), null, null);
          }
          pivot = 1;
        } else {
          double multipliaktor = Double.parseDouble(matrix.getElement(i,
              min_col)) / pivot;
          for (int j = 1; j < width; j++) {
            double x_value = Double.parseDouble(matrix.getElement(i, j));
            double p_value = Double.parseDouble(matrix.getElement(min_row, j));
            double value = r(x_value - multipliaktor * p_value);
            matrix.put(i, j, Double.toString(value), null, null);
          }
        }

        lang.nextStep();
        matrix.unhighlightCellColumnRange(i, 1, width - 1, null, null);
        matrix.highlightCell(min_row, min_col, null, null);

      }
      matrix.unhighlightCell(min_row, min_col, null, null);
      sc.unhighlight(14, 0, false);
    }

  }

  /**
   * @param myDouble
   * @return the scaled value
   */
  public double r(double myDouble) {
    BigDecimal bd = new BigDecimal(myDouble);
    bd = bd.setScale(3, BigDecimal.ROUND_HALF_DOWN);
    return bd.doubleValue();
  }

  public String getAlgorithmCode() {
    return sc.toString();
  }

  public String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {

    return "while (keine optimale Lösung gefunden){ \n"
        + "suche kleinstetes negatives Element F(t) in der F-Zeile; \n"
        + "if (kein negatives Element gefunden) \n"
        + "return aktuelle Lösung ist optimal; \n" + "else \n"
        + "	Pivotspalte = t; \n"
        + "suche  min{ b(s) / a(s,t) mit a(s,t) > 0} \n"
        + "if (nichts gefunden) \n"
        + "	return es existiert keine optimale Lösung; \n" + "else \n"
        + "	Pivotzeile = s; \n" + "Pivotelement = a(s,t); \n"
        + "x(t) wird neue Basisvariable; \n"
        + "Bilde Einheitsvektor unter x(t) durch Lineartransformation \n"
        + "} \n";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * generator.Generator#generate(generator.properties.AnimationPropertiesContainer
   * , java.util.Hashtable)
   */
  @Override
  public String generate(AnimationPropertiesContainer prop,
      Hashtable<String, Object> values) {

    init();
    a = (int[][]) values.get("optimierungsproblem");

    this.generateSimplexTable(a);
    this.printRestrictions();
    this.printTable(prop);
    this.runSimplex();
    this.printSolution();

    return lang.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getAlgorithmName()
   */
  @Override
  public String getAlgorithmName() {
    return "Simplex-Algorithmus";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getName()
   */
  @Override
  public String getName() {
    return NAME;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getAnimationAuthor()
   */
  @Override
  public String getAnimationAuthor() {
    return "Michael Scharf";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getContentLocale()
   */
  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getFileExtension()
   */
  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getGeneratorType()
   */
  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getOutputLanguage()
   */
  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#init()
   */
  @Override
  public void init() {
    matrix = null;
    sMatrix = null;
  }

  /**
   * @return the language object used
   */
  public Language getLanguage() {
    return lang;
  }

}
