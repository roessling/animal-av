package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

/**
 * Levenstein Distanz Algorithmus Generator
 * 
 * @author Martin Dingeldey, Thomas Poepl, Ulf Karrock
 * @version 1.0 2008-05-28
 * 
 */
public class Levenstein implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  public Levenstein() {

  }

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */
  public Levenstein(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  private static final String DESCRIPTION = "Dies eine Animation des Levenstein Distanz Algorithmus";

  private static final String SOURCE_CODE = "public int getLevensteinDistance(String s, String t)" // 0
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
                                              + "\n  int p[] = new int[n+1];int d[] = new int[n+1];" // 11
                                              + "\n  int _d[]; int i, j, cost; char t_j;" // 12
                                              + "\n  for (i = 0; i<=n; i++)" // 13
                                              + "\n  {" // 14
                                              + "\n    p[i] = i;" // 15
                                              + "\n  }" // 16
                                              + "\n  for (j = 1; j<=m; j++)" // 17
                                              + "\n  {" // 18
                                              + "\n    t_j = t.charAt(j-1);" // 19
                                              + "\n    d[0] = j;" // 20
                                              + "\n    for (i=1; i<=n; i++)" // 21
                                              + "\n    {" // 22
                                              + "\n    cost = s.charAt(i-1)==t_j ? 0 : 1;" // 23
                                              + "\n    d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);" // 24
                                              + "\n    }" // 25
                                              + "\n    _d = p;" // 26
                                              + "\n    p = d;" // 27
                                              + "\n    d = _d;" // 28
                                              + "\n  }" // 29
                                              + "\n  return p[n];" // 30
                                              + "\n}";                                               // 31

  public void levensteinDistance(SourceCodeProperties scProps,
      ArrayProperties arrayProps, TextProperties textProps, String s, String t) {
    // Create SourceCode: coordinates, name, display options,
    // default properties
    /*
     * ArrayProperties arrayProps = new ArrayProperties();
     * arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     * arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
     * arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
     * arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
     * Color.BLACK);
     * arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
     * Color.RED);
     * arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
     * Color.YELLOW);
     * 
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

    sc.addCodeLine("public int getLevensteinDistance(String s, String t))",
        null, 0, null); // 0
    sc.addCodeLine("{", null, 0, null); // 1
    sc.addCodeLine("int n = s.length();int m = t.length();", null, 1, null); // 2
    // sc.addCodeLine("int m = t.length();", null, 1, null); // 3
    sc.addCodeLine("if (n == 0)", null, 1, null); // 4
    sc.addCodeLine("{", null, 1, null); // 5
    sc.addCodeLine("return m;", null, 2, null); // 6
    sc.addCodeLine("}", null, 1, null); // 7
    sc.addCodeLine("else if(m == 0)", null, 1, null); // 8
    sc.addCodeLine("{", null, 1, null); // 9
    sc.addCodeLine("return n;", null, 2, null); // 10
    sc.addCodeLine("}", null, 1, null); // 11
    sc.addCodeLine("int p[] = new int[n+1];int d[] = new int[n+1];", null, 1,
        null); // 12
    // sc.addCodeLine("int d[] = new int[n+1];", null, 1, null); // 13
    sc.addCodeLine("int _d[]; int i, j, cost; char t_j;", null, 1, null); // 14
    // sc.addCodeLine("int i;", null, 1, null); // 15
    // sc.addCodeLine("int j;", null, 1, null); // 16
    // sc.addCodeLine("char t_j;", null, 1, null); // 17
    // sc.addCodeLine("int cost;", null, 1, null); // 18
    sc.addCodeLine("for (i = 0; i<=n; i++)", null, 1, null); // 19
    sc.addCodeLine("{", null, 1, null); // 20
    sc.addCodeLine("p[i] = i;", null, 2, null); // 21
    sc.addCodeLine("}", null, 1, null); // 22
    sc.addCodeLine("for (j = 1; j<=m; j++)", null, 1, null); // 23
    sc.addCodeLine("{", null, 1, null); // 24
    sc.addCodeLine("t_j = t.charAt(j-1);", null, 2, null); // 25
    sc.addCodeLine("d[0] = j;", null, 2, null); // 26
    sc.addCodeLine("for (i=1; i<=n; i++)", null, 2, null); // 27
    sc.addCodeLine("{", null, 2, null); // 28
    sc.addCodeLine("cost = s.charAt(i-1)==t_j ? 0 : 1;", null, 3, null); // 29
    sc.addCodeLine(
        "d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);", null, 3,
        null); // 30
    sc.addCodeLine("}", null, 2, null); // 31
    sc.addCodeLine("_d = p.clone();", null, 2, null); // 32
    sc.addCodeLine("p = d.clone();", null, 2, null); // 33
    sc.addCodeLine("d = _d.clone();", null, 2, null); // 34
    sc.addCodeLine("}", null, 1, null); // 35
    sc.addCodeLine("return p[n];", null, 1, null); // 36
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
      getLevensteinDistance(sc, arrayProps, textProps, s, t);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  private int getLevensteinDistance(SourceCode codeSupport,
      ArrayProperties arrayProps, TextProperties textProps, String s, String t)
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
    int p[] = new int[n + 1]; // 'previous' cost array, horizontally
    for (int i = 0; i < n + 1; i++) {
      p[i] = 0;
    }
    IntArray ia1 = lang.newIntArray(new Coordinates(20, 90), p, "intArray",
        null, arrayProps);
    lang.newText(new Coordinates(20, 70), "Array p:", "labelia1", null,
        textProps);

    int d[] = new int[n + 1]; // cost array, horizontally
    for (int j = 0; j < n + 1; j++) {
      d[j] = 0;
    }

    IntArray ia2 = lang.newIntArray(new Coordinates(220, 90), d, "intArray",
        null, arrayProps);
    lang.newText(new Coordinates(220, 70), "Array d:", "labelia2", null,
        textProps);
    lang.nextStep();
    codeSupport.toggleHighlight(11, 0, false, 12, 0);

    int _d[] = new int[n + 1]; // placeholder to assist in swapping p and d
    lang.nextStep();
    // indexes into strings s and t
    int i; // iterates through s

    int j; // iterates through t

    char t_j; // jth character of t

    int cost; // cost

    codeSupport.toggleHighlight(12, 0, false, 13, 0);
    lang.nextStep();
    for (i = 0; i <= n; i++) {
      codeSupport.toggleHighlight(13, 0, false, 15, 0);
      lang.nextStep();
      p[i] = i;
      ia1.put(i, i, new MsTiming(0), new MsTiming(0));
      ia1.highlightCell(i, i, new MsTiming(0), new MsTiming(250));
      ia1.unhighlightCell(i, i, new MsTiming(750), new MsTiming(250));
      codeSupport.toggleHighlight(15, 0, false, 13, 0);
      lang.nextStep();
    }
    codeSupport.toggleHighlight(13, 0, false, 17, 0);
    lang.nextStep();

    for (j = 1; j <= m; j++) {
      codeSupport.toggleHighlight(17, 0, false, 19, 0);
      lang.nextStep();
      t_j = t.charAt(j - 1);

      codeSupport.toggleHighlight(19, 0, false, 20, 0);
      lang.nextStep();
      d[0] = j;
      ia2.put(0, j, new MsTiming(0), new MsTiming(0));
      ia2.highlightCell(j, j, new MsTiming(0), new MsTiming(250));
      ia2.unhighlightCell(j, j, new MsTiming(750), new MsTiming(250));

      codeSupport.toggleHighlight(20, 0, false, 21, 0);
      lang.nextStep();
      for (i = 1; i <= n; i++) {
        codeSupport.toggleHighlight(21, 0, false, 23, 0);
        lang.nextStep();
        cost = s.charAt(i - 1) == t_j ? 0 : 1;
        // minimum of cell to the left+1, to the top+1, diagonally left and up
        // +cost

        codeSupport.toggleHighlight(23, 0, false, 24, 0);
        lang.nextStep();
        d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
        ia2.highlightElem(i - 1, new MsTiming(0), new MsTiming(250));
        ia2.unhighlightElem(i - 1, new MsTiming(750), new MsTiming(250));
        ia1.highlightElem(i, new MsTiming(0), new MsTiming(250));
        ia1.unhighlightElem(i, new MsTiming(750), new MsTiming(250));
        ia1.highlightElem(i - 1, new MsTiming(0), new MsTiming(250));
        ia1.unhighlightElem(i - 1, new MsTiming(750), new MsTiming(250));
        ia2.put(i, d[i], new MsTiming(0), new MsTiming(0));
        ia2.highlightCell(i, new MsTiming(0), new MsTiming(250));
        ia2.unhighlightCell(i, new MsTiming(750), new MsTiming(250));

        codeSupport.toggleHighlight(24, 0, false, 21, 0);
        lang.nextStep();
      }
      codeSupport.toggleHighlight(21, 0, false, 26, 0);
      lang.nextStep();
      Text labelas = lang.newText(new Coordinates(160, 120), "Tausche Arrays",
          "labelas", null, textProps);
      labelas.changeColor("", Color.green, new MsTiming(0), new MsTiming(0));
      _d = p.clone();

      codeSupport.toggleHighlight(26, 0, false, 27, 0);
      lang.nextStep();
      p = d.clone();
      for (int z = 0; z < n + 1; z++) {
        ia1.put(z, d[z], new MsTiming(0), new MsTiming(0));
      }

      codeSupport.toggleHighlight(27, 0, false, 28, 0);
      lang.nextStep();
      d = _d.clone();
      for (int y = 0; y < n + 1; y++) {
        ia2.put(y, _d[y], new MsTiming(0), new MsTiming(0));
      }

      labelas.hide();
      codeSupport.toggleHighlight(28, 0, false, 17, 0);
      lang.nextStep();
    }
    codeSupport.toggleHighlight(17, 0, false, 30, 0);
    lang.nextStep();

    // our last action in the above loop was to switch d and p, so p now
    // actually has the most recent cost counts
    ia1.highlightElem(n, new MsTiming(0), new MsTiming(0));
    ia1.highlightCell(n, new MsTiming(0), new MsTiming(0));
    Text res = lang.newText(new Coordinates(240, 120),
        "Die Levenshtein-Distanz ist: " + p[n], "Textres", null, textProps);
    res.changeColor("", Color.cyan, new MsTiming(0), new MsTiming(0));
    return p[n];

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
    Language l = new AnimalScript("Levenstein Distanz Algorithmusmit Array",
        "Martin Dingeldey, Thomas Poepl, Ulf Karrock", 640, 480);
    Levenstein ls = new Levenstein(l);
    // String a = "Informatik";
    // String a = arg0.get(prop, item)
    String a = arg1.get("a").toString();
    // String b = "Bionik";
    String b = arg1.get("b").toString();

    ArrayProperties arrayProps = (ArrayProperties) arg0
        .getPropertiesByName("arrayProps");
    SourceCodeProperties sourceProps = (SourceCodeProperties) arg0
        .getPropertiesByName("sourceCodeProps");
    TextProperties textProps = (TextProperties) arg0
        .getPropertiesByName("textProps");

    // String b = arg0.getPropertiesByName("b");
    ls.levensteinDistance(sourceProps, arrayProps, textProps, a, b);
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
