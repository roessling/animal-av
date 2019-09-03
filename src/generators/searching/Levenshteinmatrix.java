package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

/**
 * Levenshtein Distanz Algorithmus Generator
 * 
 * @author Martin Dingeldey, Thomas Poepl, Ulf Karrock
 * @version 1.0 2008-06-03
 * 
 */
public class Levenshteinmatrix implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  public Levenshteinmatrix() {

  }

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */
  public Levenshteinmatrix(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  private static final String DESCRIPTION = "Dies eine Animation des Levenstein Distanz Algorithmus";

  private static final String SOURCE_CODE = "public int getLevenshteinDistance(String s, String t)" // 0
                                              + "\n{" // 1
                                              + "\n  int n = s.length();int m = t.length();" // 2
                                              + "\n  if (n == 0)" // 3
                                              + "\n  {" // 4
                                              + "\n    return m;" // 5
                                              + "\n  }" // 6
                                              + "\n  else if(m == 0)" // 7
                                              + "\n  {" // 8
                                              + "\n    return n;" // 9
                                              + "\n  }" // 10
                                              + "\n  int p[][] = new int[n+1][m+1]" // 11
                                              + "\n  int i, j, cost; char t_j;" // 12
                                              + "\n  for (i = 0; i<=n; i++)" // 13
                                              + "\n  {" // 14
                                              + "\n    p[i][0] = i;" // 15
                                              + "\n  }" // 16
                                              + "\n  for (j = 0; j<=n; j++)" // 17
                                              + "\n  {" // 18
                                              + "\n    p[0][j] = j;" // 19
                                              + "\n  }" // 20
                                              + "\n  for (i = 0; i<=n; i++)" // 21
                                              + "\n  {" // 22
                                              + "\n    for (j = 1; j<=m; j++)" // 23
                                              + "\n    {" // 24
                                              + "\n    t_j = t.charAt(j-1);" // 19
                                              + "\n    cost = s.charAt(i-1)==t_j ? 0 : 1;" // 23
                                              + "\n    p[i][j] = Math.min(Math.min(p[i-1][j]+1, p[i][j-1]+1),  p[i-1][j-1]+cost)" // 24
                                              + "\n    }" // 25
                                              + "\n  }" // 29
                                              + "\n  return p[n][m];" // 30
                                              + "\n}";                                               // 31

  public void levenshteinDistance(String s, String t,
      MatrixProperties matrixProps, SourceCodeProperties scProps,
      TextProperties textProps) {

    /*
     * MatrixProperties matrixProps = new MatrixProperties();
     * matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
     * matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
     * matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
     * matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
     * Color.BLUE);
     * matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
     * Color.RED);
     * matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
     * Color.YELLOW);
     * 
     * 
     * 
     * TextProperties textProps = new TextProperties();
     * textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     * 
     * // first, set the visual properties for the source code
     * SourceCodeProperties scProps = new SourceCodeProperties();
     * scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
     * scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
     * Font.PLAIN, 12));
     * 
     * scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
     * scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     */
    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, scProps);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy

    sc.addCodeLine("public int getLevenshteinDistance(String s, String t))",
        null, 0, null); // 0
    sc.addCodeLine("{", null, 0, null); // 1
    sc.addCodeLine("int n = s.length();int m = t.length();", null, 1, null); // 2
    sc.addCodeLine("if (n == 0)", null, 1, null); // 3
    sc.addCodeLine("{", null, 1, null); // 4
    sc.addCodeLine("return m;", null, 2, null); // 5
    sc.addCodeLine("}", null, 1, null); // 6
    sc.addCodeLine("else if(m == 0)", null, 1, null); // 7
    sc.addCodeLine("{", null, 1, null); // 8
    sc.addCodeLine("return n;", null, 2, null); // 9
    sc.addCodeLine("}", null, 1, null); // 10
    sc.addCodeLine("int p[][] = new int[n+1][m+1];", null, 1, null); // 11
    sc.addCodeLine("int i, j, cost; char t_j;", null, 1, null); // 12
    sc.addCodeLine("for (i = 0; i<=n; i++)", null, 1, null); // 13
    sc.addCodeLine("{", null, 1, null); // 14
    sc.addCodeLine("p[i][0] = i;", null, 2, null); // 15
    sc.addCodeLine("}", null, 1, null); // 16
    sc.addCodeLine("for (j = 0; j<=m; j++)", null, 1, null); // 17
    sc.addCodeLine("{", null, 1, null); // 18
    sc.addCodeLine("p[0][j] = j;", null, 2, null); // 19
    sc.addCodeLine("}", null, 1, null); // 20
    sc.addCodeLine("for (i = 1; i<=n; i++)", null, 1, null); // 20
    sc.addCodeLine("{", null, 1, null); // 21
    sc.addCodeLine("for (j = 1; j<=m; j++)", null, 2, null); // 22
    sc.addCodeLine("{", null, 2, null); // 23
    sc.addCodeLine("t_j = t.charAt(j-1);", null, 3, null); // 24
    sc.addCodeLine("cost = s.charAt(i-1)==t_j ? 0 : 1;", null, 3, null); // 29
    sc.addCodeLine(
        "p[i][j] = Math.min(Math.min(p[i-1][j]+1, p[i][j-1]+1),  p[i-1][j-1]+cost)",
        null, 3, null); // 30
    sc.addCodeLine("}", null, 2, null); // 31
    sc.addCodeLine("return p[n][m];", null, 1, null); // 36
    sc.addCodeLine("};", null, 0, null); // 37

    lang.newText(new Coordinates(10, 10),
        "Berechnung der Levenshtein-Distanz zwischen", "label1", null,
        textProps);
    lang.newText(new Coordinates(10, 30), "String s: " + s + " und", "label2",
        null, textProps);
    lang.newText(new Coordinates(10, 50), "String t: " + t, "label3", null,
        textProps);

    lang.nextStep();
    // Highlight all cells
    try {
      // Start Levenstein Distance
      getLevenshteinDistance(sc, matrixProps, textProps, s, t);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  private int getLevenshteinDistance(SourceCode codeSupport,
      MatrixProperties matrixProps, TextProperties textProps, String s, String t)
      throws LineNotExistsException {
    // Highlight first line
    // Line, Column, use context colour?, display options, duration
    codeSupport.highlight(0, 0, false);
    lang.nextStep();

    codeSupport.toggleHighlight(0, 0, false, 2, 0);
    lang.nextStep();
    int n = s.length(); // length of s

    int m = t.length(); // length of t

    codeSupport.toggleHighlight(2, 0, false, 3, 0);
    lang.nextStep();
    if (n == 0) {
      codeSupport.toggleHighlight(3, 0, false, 5, 0);
      lang.nextStep();
      return m;
    } else {
      codeSupport.toggleHighlight(3, 0, false, 7, 0);
      lang.nextStep();
      if (m == 0) {
        codeSupport.toggleHighlight(7, 0, false, 9, 0);
        lang.nextStep();
        return n;
      }
    }
    codeSupport.toggleHighlight(7, 0, false, 11, 0);
    lang.newText(new Coordinates(410, 10), "Matrix p:", "labelim", null,
        textProps);

    int p[][] = new int[n + 1][m + 1]; // 'previous' cost array, horizontally

    // matrix zur ausgabe
    String q[][] = new String[n + 1][m + 1];
    for (int a = 0; a < n + 1; a++) {
      for (int b = 0; b < m + 1; b++) {

        q[a][b] = " ";
      }

    }

    // wort1 an erste zeile schreiben
    for (int i = 1; i <= n; i++) {
      q[i][0] = s.substring(i - 1, i);
      // q[i][1] = String.valueOf(i);
    }

    // wort 2 an erste spalte schreiben
    for (int j = 1; j <= m; j++) {
      q[0][j] = t.substring(j - 1, j);
      // q[1][j] = String.valueOf(j);
    }

    // IntMatrix im = lang.newIntMatrix(new Coordinates(410, 20), p,
    // "intMatrix", null, matrixProps);
    StringMatrix im = lang.newStringMatrix(new Coordinates(440, 20), q,
        "stringMatrix", null, matrixProps);

    lang.nextStep();

    codeSupport.toggleHighlight(11, 0, false, 12, 0);
    lang.nextStep();

    // indexes into strings s and t
    int i; // iterates through s

    int j; // iterates through t

    char t_j; // jth character of t

    int cost; // cost

    codeSupport.toggleHighlight(12, 0, false, 13, 0);
    lang.nextStep();
    // initialize for string s
    for (i = 1; i <= n; i++) {
      codeSupport.toggleHighlight(13, 0, false, 15, 0);
      im.put(i, 0, s.substring(i - 1, i), new MsTiming(0), new MsTiming(0));
      im.highlightCell(i, 1, new MsTiming(0), new MsTiming(250));
      im.put(i, 1, String.valueOf(i), new MsTiming(0), new MsTiming(0));
      lang.nextStep();
      p[i][0] = 99;
      p[i][1] = i;
      codeSupport.toggleHighlight(15, 0, false, 13, 0);
      im.unhighlightCell(i, 1, new MsTiming(750), new MsTiming(250));
      lang.nextStep();
    }

    codeSupport.toggleHighlight(13, 0, false, 17, 0);
    lang.nextStep();
    // initialize for string t
    for (j = 1; j <= m; j++) {
      codeSupport.toggleHighlight(17, 0, false, 19, 0);
      im.put(0, j, t.substring(j - 1, j), new MsTiming(0), new MsTiming(0));
      im.highlightCell(1, j, new MsTiming(0), new MsTiming(250));
      im.put(1, j, String.valueOf(j), new MsTiming(0), new MsTiming(0));
      lang.nextStep();
      p[0][j] = 99;
      p[1][j] = j;
      codeSupport.toggleHighlight(19, 0, false, 17, 0);
      im.unhighlightCell(1, j, new MsTiming(750), new MsTiming(250));
      lang.nextStep();
    }
    codeSupport.toggleHighlight(17, 0, false, 21, 0);
    lang.nextStep();

    for (i = 2; i <= n; i++) {
      codeSupport.toggleHighlight(21, 0, false, 23, 0);
      lang.nextStep();
      for (j = 2; j <= m; j++) {
        codeSupport.toggleHighlight(23, 0, false, 25, 0);
        lang.nextStep();
        t_j = t.charAt(j - 1);

        codeSupport.toggleHighlight(25, 0, false, 26, 0);
        lang.nextStep();
        cost = s.charAt(i - 1) == t_j ? 0 : 1;
        // minimum of cell to the left+1, to the top+1, diagonally left and up
        // +cost

        codeSupport.toggleHighlight(26, 0, false, 27, 0);
        im.highlightCell(i, j, new MsTiming(0), new MsTiming(250));
        p[i][j] = Math.min(Math.min(p[i - 1][j] + 1, p[i][j - 1] + 1),
            p[i - 1][j - 1] + cost);
        im.put(i, j, String.valueOf(p[i][j]), new MsTiming(0), new MsTiming(0));
        lang.nextStep();
        codeSupport.toggleHighlight(27, 0, false, 23, 0);
        im.unhighlightCell(i, j, new MsTiming(750), new MsTiming(250));
        lang.nextStep();
      }

      codeSupport.toggleHighlight(23, 0, false, 21, 0);
      lang.nextStep();

    }

    codeSupport.toggleHighlight(21, 0, false, 29, 0);
    lang.nextStep();

    lang.newText(new Coordinates(10, 70), "Die Levenshtein-Distanz ist: "
        + p[n][m], "Textres", null, textProps);
    im.highlightCell(n, m, new MsTiming(0), new MsTiming(0));
    return p[n][m];

  }

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  public String getName() {
    return "Levenshtein Distanz Algorithmus";
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    // Create a new animation
    // name, author, screen width, screen height
    Language l = new AnimalScript("Levenstein Distanz Algorithmus",
        "Martin Dingeldey, Thomas Poepl, Ulf Karrock", 640, 480);
    Levenshteinmatrix ls = new Levenshteinmatrix(l);
    // String a = "Informatik";
    // String a = arg0.get(prop, item)
    String a = arg1.get("a").toString();
    // String b = "Bionik";
    String b = arg1.get("b").toString();

    MatrixProperties matrixProps = (MatrixProperties) arg0
        .getPropertiesByName("mProps");
    SourceCodeProperties sourceProps = (SourceCodeProperties) arg0
        .getPropertiesByName("sourceCodeProps");
    TextProperties textProps = (TextProperties) arg0
        .getPropertiesByName("textProps");

    // String b = arg0.getPropertiesByName("b");
    ls.levenshteinDistance(a, b, matrixProps, sourceProps, textProps);
    return l.toString();
  }

  public String getAlgorithmName() {
    return "Levenshtein-Distanz";
  }

  public String getAnimationAuthor() {
    return "Martin Dingeldey, Thomas Pöpl, Ulf Karrock";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }
}
