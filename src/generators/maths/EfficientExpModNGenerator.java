package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Thomas Schulz, Benjamin Lück
 * @version 1.0
 */
public class EfficientExpModNGenerator implements Generator {
  private Language              lang;
  private SourceCodeProperties  USERcodeProps;
  private MatrixProperties      USERsmp;
  private int                   n;
  private ArrayProperties       USERarrayProps;
  private TextProperties        USERhdProps;
  private SourceCodeProperties  USERprosaProps;
  private int                   y;
  private TextProperties        USERtextProps;
  private int                   x;

  /**
   * Relevant Object and Properties
   */
  private IntArray              y_array;

  private ArrayMarker           ami;
  private ArrayMarkerProperties amProps;

  private SourceCode            code;

  private StringMatrix          values;

  private Text                  subhd, step1, step2, step3, step4;

  // private Rect hRect;
  private RectProperties        rectProps;

  // private char[] Y;
  private int                   solution;

  public void init() {
    lang = new AnimalScript("Effizientes Exponenzieren modulo n",
        "Thomas Schulz,Benjamin Lueck", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    USERcodeProps = (SourceCodeProperties) props
        .getPropertiesByName("USERcodeProps");
    USERsmp = (MatrixProperties) props.getPropertiesByName("USERsmp");
    n = (Integer) primitives.get("n");
    USERarrayProps = (ArrayProperties) props
        .getPropertiesByName("USERarrayProps");
    USERhdProps = (TextProperties) props.getPropertiesByName("USERhdProps");
    USERprosaProps = (SourceCodeProperties) props
        .getPropertiesByName("USERprosaProps");
    y = (Integer) primitives.get("y");
    USERtextProps = (TextProperties) props.getPropertiesByName("USERtextProps");
    x = (Integer) primitives.get("x");

    initialize();
    executeAlgorithm();
    outro();

    return lang.toString();
  }

  public String getName() {
    return "Effizientes Exponenzieren modulo n";
  }

  public String getAlgorithmName() {
    return "Exponentiation";
  }

  public String getAnimationAuthor() {
    return "Thomas Schulz, Benjamin Lück";
  }

  public String getDescription() {
    return "In dieser Animation geht es um die Veranschaulichung eines" + "\n"
        + "effizienten Verfahrens zur Berechnung von ''x^y mod n''" + "\n"
        + "mittels square-and-multiply und Bilden des Restes nach" + "\n"
        + "jedem einzelnen Berechnungsschritt.";
  }

  public String getCodeExample() {
    return "Input: x, y, n" + "\n" + "\t r := 1;" + "\n" + "\t b := x;" + "\n"
        + "\t array Y := y in Binaerdarstellung" + "\n"
        + "\t for (i := Y.length-1 ... 0) {" + "\n" + "\t \t if (Y[i] = 1)"
        + "\n" + "\t \t \t r := (r * b) mod n;" + "\n"
        + "\t \t b := (b * b) mod n;" + "\n" + "\t } return r;";
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

  /**
   * Initializes the window and does initial unconditioned steps
   */
  private void initialize() {
    lang = new AnimalScript("EfficientExponentiationModN",
        "Thomas Schulz,Benjamin Lueck", 800, 600);
    lang.setStepMode(true);

    /**
     * ======== DEFAULTS ========
     */

    USERhdProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 24));
    USERprosaProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 16));
    USERtextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 16));
    USERcodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 16));
    USERarrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 16));

    /**
     * ====== HEADER ======
     */

    // title
    Coordinates hd = new Coordinates(20, 30);

    lang.newText(hd, "Effizientes Exponenzieren modulo n", "header", null,
        USERhdProps);

    // rectangle around the title
    Offset rectNW = new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW);
    Offset rectSE = new Offset(5, 5, "header", AnimalScript.DIRECTION_SE);

    // =======
    // DEFAULT
    // =======
    rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // =======
    // DEFAULT
    // =======

    lang.newRect(rectNW, rectSE, "hRect", null, rectProps);

    lang.nextStep();

    /**
     * ===== INTRO =====
     */

    // introduction of the algorithm
    Offset introSW = new Offset(5, 30, "hRect", AnimalScript.DIRECTION_SW);

    SourceCode intro = lang.newSourceCode(introSW, "intro", null,
        USERprosaProps);
    intro.addCodeLine(
        "Bei dem hier vorgestellten Verfahren geht es um die effiziente", null,
        0, null);
    intro.addCodeLine(
        "Berechnung von ''x^y mod n'' mittels square-and-multiply und", null,
        0, null);
    intro.addCodeLine(
        "Bilden des Restes nach jedem einzelnen Berechnungsschritt.", null, 0,
        null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine(
        "Um beispielsweise x^4 zu berechnen kann man (naiv) x*x*x*x", null, 0,
        null);
    intro.addCodeLine(
        "(3 Multiplikationen) oder (x^2)^2 (2 Multiplikationen) rechnen.",
        null, 0, null);
    intro
        .addCodeLine(
            "Ebenso koennen auch andere ganzzahlige Potenzen durch ''fortgesetztes",
            null, 0, null);
    intro
        .addCodeLine(
            "Quadrieren und gelegentliches Multiplizieren'' effizient berechnet werden.",
            null, 0, null);

    lang.nextStep("Einleitung");

    /**
     * =========== SOURCE-CODE ===========
     */

    intro.hide();

    showSourceCode();

    lang.nextStep("Zeige Source-Code");

    /**
     * ======== INITIALS ========
     */

    Offset sh = new Offset(5, 30, "hRect", AnimalScript.DIRECTION_SW);

    subhd = lang.newText(sh, "Startwerte", "subhd", null, USERtextProps);

    Offset s1 = new Offset(0, 5, "subhd", AnimalScript.DIRECTION_SW);

    // ===
    StringBuffer s1content = new StringBuffer(256);
    s1content.append("x := ").append(x).append("; y := ").append(y)
        .append("; n := ").append(n).append(";");
    // ===
    step1 = lang
        .newText(s1, s1content.toString(), "step1", null, USERtextProps);
    code.highlight(2);
    lang.nextStep("Initialisierung");

    Offset s2 = new Offset(0, 5, "step1", AnimalScript.DIRECTION_SW);
    step2 = lang.newText(s2, "r := 1", "step2", null, USERtextProps);
    code.toggleHighlight(2, 3);
    lang.nextStep();

    Offset s3 = new Offset(0, 5, "step2", AnimalScript.DIRECTION_SW);
    // ===
    StringBuffer s3content = new StringBuffer(256);
    s3content.append("b := x = ").append(x).append(";");
    // ===
    step3 = lang
        .newText(s3, s3content.toString(), "step3", null, USERtextProps);
    code.toggleHighlight(3, 4);
    lang.nextStep();

    Offset s4 = new Offset(25, 5, "step1", AnimalScript.DIRECTION_SE);
    step4 = lang.newText(s4, "Y :=", "step4", null, USERtextProps);
  }

  private void showSourceCode() {
    Offset codeSW = new Offset(5, 150, "hRect", AnimalScript.DIRECTION_SW);

    code = lang.newSourceCode(codeSW, "theCode", null, USERcodeProps);
    code.addCodeLine("Algorithmus in Pseudo-Code:", null, 0, null);
    code.addCodeLine("===========================", null, 0, null);
    code.addCodeLine("Input: x, y, n", null, 0, null);
    code.addCodeLine("r := 1;", null, 2, null);
    code.addCodeLine("b := x;", null, 2, null);
    code.addCodeLine("array Y := y in Binaerdarstellung", null, 2, null);
    code.addCodeLine("for (i := Y.length-1 ... 0) {", null, 2, null);
    code.addCodeLine("if (Y[i] = 1)", null, 4, null);
    code.addCodeLine("r := (r * b) mod n;", null, 6, null);
    code.addCodeLine("b := (b * b) mod n;", null, 4, null);
    code.addCodeLine("} return r;", null, 2, null);
  }

  private void executeAlgorithm() {

    /**
     * ========== SHOW ARRAY ==========
     */

    Offset arrayOffset = new Offset(8, 0, "step4", AnimalScript.DIRECTION_NE);

    USERarrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    // getting a bit representation of y
    char[] charY = Integer.toBinaryString(new Integer(y)).toCharArray();
    int[] Y = new int[charY.length];

    for (int i = 0; i < charY.length; i++)
      Y[i] = Integer.parseInt(String.valueOf(charY[i]));

    y_array = lang.newIntArray(arrayOffset, Y, "y_array", null, USERarrayProps);

    code.toggleHighlight(4, 5);
    lang.nextStep();

    /**
     * ==== LOOP ====
     */
    int r = 1;
    int b = x;

    // set ArrayMarker i

    // =======
    // DEFAULT
    // =======
    amProps = new ArrayMarkerProperties();
    amProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    // =======
    // DEFAULT
    // =======

    ami = lang.newArrayMarker(y_array, y_array.getLength() - 1, "bitValue",
        null, amProps);

    code.toggleHighlight(5, 6);

    // prepare Matrix
    Offset grid = new Offset(40, 0, "theCode", AnimalScript.DIRECTION_NE);
    String[][] sm = new String[4][y_array.getLength() + 1];
    for (int i = 0; i < 4; i++)
      for (int j = 0; j <= y_array.getLength(); j++)
        sm[i][j] = "";
    sm[0][0] = "i";
    sm[1][0] = "Y[i]";
    sm[2][0] = "r";
    sm[3][0] = "b";

    // create Matrix
    values = lang.newStringMatrix(grid, sm, "values", null, USERsmp);

    lang.nextStep();

    int col = 1;
    code.unhighlight(6);

    for (ami.getPosition(); ami.getPosition() >= 0; ami.decrement(null, null)) {

      code.highlight(7);
      values.put(0, col, String.valueOf(ami.getPosition()), null, null);
      // values.highlightElem(row, 0, null, null);
      lang.nextStep(String.valueOf(col) + ". Iteration");
      // values.unhighlightElem(row, 0, null, null);

      if (y_array.getData(ami.getPosition()) == 1) {
        values.put(1, col, String.valueOf(1), null, null);
        // values.highlightElem(row, 1, null, null);
        lang.nextStep();
        // values.unhighlightElem(row, 1, null, null);
        code.toggleHighlight(7, 8);
        r = (r * b) % n;
        values.put(2, col, String.valueOf(r), null, null);
        // values.highlightCell(row, 2, null, null);
        lang.nextStep();
      } else {
        // values.highlightCell(row, 1, null, null);
        values.put(1, col, String.valueOf(0), null, null);
        lang.nextStep();
        // values.unhighlightElem(row, 1, null, null);
        values.put(2, col, String.valueOf(r), null, null);
        // values.highlightElem(row, 2, null, null);
        lang.nextStep();
      }
      // values.unhighlightElem(row, 2, null, null);
      code.unhighlight(7);
      code.unhighlight(8);
      code.highlight(9);
      b = (b * b) % n;
      values.put(3, col, String.valueOf(b), null, null);
      // values.highlightElem(row, 3, null, null);
      lang.nextStep();
      code.unhighlight(9);
      // values.unhighlightElem(row, 3, null, null);
      col++;
    }
    solution = r;

    Offset sol = new Offset(0, 20, "values", AnimalScript.DIRECTION_SW);
    StringBuffer solContent = new StringBuffer(256);
    solContent.append("Loesung: ").append(solution);
    Text solText = lang.newText(sol, solContent.toString(), "solution", null,
        USERtextProps);

    code.highlight(10);
    // values.highlightElem(values.getNrRows()-1, 2, null, null);
    lang.nextStep("Loesung");
    code.unhighlight(10);

    Offset naiveSW = new Offset(0, 15, "theCode", AnimalScript.DIRECTION_SW);

    // ===
    StringBuffer naiveContenthd = new StringBuffer(256);
    naiveContenthd.append("Naiver Ansatz:");
    // ===

    // ===
    StringBuffer naiveContent1 = new StringBuffer(256);
    naiveContent1.append("(").append(x);
    for (int i = 1; i < y; i++)
      naiveContent1.append(" * ").append(x);
    naiveContent1.append(") mod ").append(n);
    // ===

    // ===
    StringBuffer naiveContent2 = new StringBuffer(256);
    naiveContent2.append("(").append(Math.pow(x, y));
    naiveContent2.append(") mod ").append(n);
    // ===

    // ===
    StringBuffer naiveContent3 = new StringBuffer(256);
    naiveContent3.append("(moeglicher Overflow)");
    // ===

    SourceCode naive = lang.newSourceCode(naiveSW, "naive", null,
        USERprosaProps);
    naive.addCodeLine(naiveContenthd.toString(), null, 0, null);
    naive.addCodeLine(naiveContent1.toString(), null, 2, null);
    naive.addCodeLine(naiveContent2.toString(), null, 2, null);
    naive.addCodeLine(naiveContent3.toString(), null, 2, null);

    lang.nextStep("Vergleich mit naivem Ansatz");

    solText.hide();
    naive.hide();
  }

  private void outro() {

    code.hide();
    subhd.hide();
    step1.hide();
    step2.hide();
    step3.hide();
    step4.hide();
    y_array.hide();
    values.hide();
    ami.hide();

    // conclusion
    Offset outroSW = new Offset(5, 30, "hRect", AnimalScript.DIRECTION_SW);
    SourceCode outro = lang.newSourceCode(outroSW, "outro", null,
        USERprosaProps);
    outro.addCodeLine("Wie man leicht einsieht, ist das Verfahren sehr viel",
        null, 0, null);
    outro.addCodeLine("effizienter als der naive Ansatz. Dieser verringerte",
        null, 0, null);
    outro.addCodeLine("Anspruch an die Rechenleistung ist bei grossen Basen",
        null, 0, null);
    outro.addCodeLine("und Exponenten enorm.", null, 0, null);

    lang.nextStep("Fazit");

    Offset comSW = new Offset(0, 20, "outro", AnimalScript.DIRECTION_SW);
    SourceCode complexity = lang.newSourceCode(comSW, "complexity", null,
        USERprosaProps);
    complexity.addCodeLine(
        "Wo eine naive Potenzierung (y-1) Multiplikationen benoetigt,", null,
        0, null);
    complexity.addCodeLine(
        "kommt der square-and-multiply-Ansatz mit log_2(y) Schleifen-", null,
        0, null);
    complexity.addCodeLine("durchlaeufen aus.", null, 0, null);

    lang.nextStep();

    Offset useSW = new Offset(0, 20, "complexity", AnimalScript.DIRECTION_SW);
    SourceCode usage = lang.newSourceCode(useSW, "usage", null, USERprosaProps);
    usage.addCodeLine(
        "Das Verfahren ist vor allem bei der Berechnung von Chiffraten", null,
        0, null);
    usage.addCodeLine("und bei der Dechiffrierung in der RSA-Verschluesselung",
        null, 0, null);
    usage.addCodeLine("von herausragender Bedeutung.", null, 0, null);

    lang.nextStep();

    Offset sourceSW = new Offset(0, 30, "usage", AnimalScript.DIRECTION_SW);
    lang.newText(sourceSW, "Bemerkungen angelehnt an wikipedia.org", "source",
        null, USERtextProps);

    lang.nextStep();

    Offset linkSW = new Offset(0, 10, "source", AnimalScript.DIRECTION_SW);
    lang.newText(
        linkSW,
        "http://de.wikipedia.org/wiki/Bin%C3%A4re_Exponentiation#Laufzeitanalyse",
        "link", null, USERtextProps);

  }

}
