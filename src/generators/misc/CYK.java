package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpers.Rule;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;

/**
 * Implementation of the CYK-algorithm for an Animal-generator
 * 
 * @author paskuda, zöller
 */
public class CYK implements Generator {
  private Language lang;
  private String   S_left;
  private String   A_terminal;
  private String   S_terminal;
  private String   A_right;
  private String   B_left;
  private String   A_left;
  private String   B_terminal;
  private String   B_right;
  private String   S_right;

  private String[] word;

  /**
   * Initialize the animation
   */
  public void init() {
    lang = new AnimalScript("CYK [DE]", "Barbara Zöller, Malte Paskuda", 800,
        600);
  }

  /**
   * Generate the animalscript-code
   * 
   * @return String The animalscript-code
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    S_left = (String) primitives.get("S-left");
    A_terminal = (String) primitives.get("A-terminal");
    S_terminal = (String) primitives.get("S-terminal");
    A_right = (String) primitives.get("A-right");
    B_left = (String) primitives.get("B-left");
    A_left = (String) primitives.get("A-left");
    B_terminal = (String) primitives.get("B-terminal");
    B_right = (String) primitives.get("B-right");
    S_right = (String) primitives.get("S-right");
    lang.setStepMode(true);
    word = (String[]) primitives.get("word");
    this.cyk(word);
    return lang.toString();
  }

  /**
   * Check by using the CYK-algorithm whether the given word is part of the
   * language defined by stored grammar
   * 
   * @param word
   *          Word to check
   */
  public void cyk(String[] word) {

    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "CYK-Algorithmus",
        "header", new MsTiming(0), tp);

    Rect headerBackground = lang.newRect(new Offset(-10, -10, header, "NW"),
        new Offset(10, 10, header, "SE"), "headerBackground", new MsTiming(0));
    headerBackground.changeColor("color", Color.BLACK, null, null);

    Text intro1 = lang
        .newText(
            new Offset(0, 10, header, "South"),
            "Der CYK-Algorithmus wird verwendet um zu überprüfen, ob ein Wort zu einer",
            "description", new MsTiming(0));
    Text intro2 = lang
        .newText(
            new Offset(0, 10, intro1, "SW"),
            "bestimmten kontextfreien Sprache gehört. Dazu muss zu der angegebenen Sprache eine Grammatik in",
            "description", new MsTiming(0));
    Text intro3 = lang.newText(new Offset(0, 10, intro2, "SW"),
        "Chomsky-Normalform (Abk.: CNF) vorliegen.", "description",
        new MsTiming(0));
    Text intro4 = lang
        .newText(
            new Offset(0, 10, intro3, "SW"),
            "Eine formale Grammatik G ist in CNF, wenn aus jeder Regel entweder in",
            "description", new MsTiming(0));
    Text intro5 = lang
        .newText(
            new Offset(0, 10, intro4, "SW"),
            "zwei neue Nichtterminalsymbole oder in ein Terminalsymbol gewechselt werden kann.",
            "description", new MsTiming(0));
    Text intro6 = lang.newText(new Offset(0, 10, intro5, "SW"),
        "Die Komplexität des Algorithmus ist nicht günstig, sondern O(n³).",
        "description", new MsTiming(0));

    lang.nextStep("Ausgangssituation");

    intro1.setText("", null, null);
    intro2.setText("", null, null);
    intro3.setText("", null, null);
    intro4.setText("", null, null);
    intro5.setText("", null, null);
    intro6.setText("", null, null);

    MsTiming basicDisplay = new MsTiming(0);
    int n = word.length;
    String[][] V = new String[n + 1][n + 1];
    for (int i = 0; i <= n; i++) {
      for (int j = 0; j <= n; j++) {
        V[i][j] = "";
      }

    }
    V[0][0] = "V_i_j";
    for (int i = 1; i <= n; i++) {
      V[0][i] = "" + i;
      V[i][0] = "" + i;
    }
    StringMatrix tabelle = lang.newStringMatrix(new Coordinates(100, 100), V,
        "Tabelle", basicDisplay);
    lang.newText(new Offset(-10, 0, tabelle, "W"), "i ", "i", basicDisplay);
    lang.newText(new Offset(0, -10, tabelle, "N"), "j ", "j", basicDisplay);
    SourceCode sourceCode = this.paintSourceCode(tabelle);
    Text varI = lang.newText(new Offset(0, -90, sourceCode, "NW"), "i: ", "i",
        basicDisplay);
    Text varJ = lang.newText(new Offset(0, 10, varI, "SW"), "j: ", "j",
        basicDisplay);
    Text varK = lang.newText(new Offset(0, 10, varJ, "SW"), "k: ", "k",
        basicDisplay);
    Text varN = lang.newText(new Offset(0, 10, varK, "SW"), "n: ", "n",
        basicDisplay);

    Vector<Rule> rules = new Vector<Rule>();
    Rule s = new Rule();
    s.father = "S";
    s.leftChild = this.S_left;
    s.rightChild = this.S_right;
    s.terminal = this.S_terminal;
    Rule a = new Rule();
    a.father = "A";
    a.leftChild = this.A_left;
    a.rightChild = this.A_right;
    a.terminal = this.A_terminal;
    Rule b = new Rule();
    b.father = "B";
    b.leftChild = this.B_left;
    b.rightChild = this.B_right;
    b.terminal = this.B_terminal;

    rules.add(s);
    rules.add(a);
    rules.add(b);

    Map<String, Text> ruleDisplayStore = new HashMap<String, Text>();
    Offset rulePos = new Offset(50, 0, varI, "NE");
    for (Rule r : rules) {
      Text temp = lang.newText(rulePos, r.father + "--> " + r.leftChild + ""
          + r.rightChild + "|" + r.terminal, "rule1", basicDisplay);
      ruleDisplayStore.put(r.father, temp);
      rulePos = new Offset(0, 10, temp, "SW");

    }

    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    StringArray testWord = lang.newStringArray(new Offset(150, 0, varI, "NE"),
        word, "word", null, arrayProps);
    lang.newText(new Offset(-16, 0, testWord, "NW"), "w:", "w:", basicDisplay);

    ArrayMarker wordMarker = lang.newArrayMarker(testWord, 0, "i", null);

    wordMarker.hide();

    Text arrayCounter = lang.newText(new Offset(1, -40, testWord, "SW"), "1",
        "arrayCounter", null);
    arrayCounter.hide();
    Offset[] points = new Offset[] { new Offset(1, -3, arrayCounter, "Center"),
        new Offset(7, -10, arrayCounter, "Center"),
        new Offset(14, -3, arrayCounter, "Center") };
    Polyline traveller = lang.newPolyline(points, "test", null);
    traveller.hide();

    points = new Offset[] { new Offset(10, 0, arrayCounter, "Center"),
        new Offset(0, -30, arrayCounter, "Center"),
        new Offset((-14 * (word.length - 2)), 0, varN, "E") };
    Polyline traveller2 = lang.newPolyline(points, "test", null);
    traveller2.hide();

    lang.nextStep();

    // The Animation of the counting for n:
    arrayCounter.show();
    for (int i = 1; i < word.length; i++) {
      try {
        // Note the system used by the animations: Start from the last step
        // after the given delay
        arrayCounter.moveVia("E", "translate", traveller,
            new MsTiming(i * 1000), new MsTiming(1000));
        arrayCounter.setText("" + (i + 1), new MsTiming((i + 1) * 1000),
            new MsTiming(0));
      } catch (IllegalDirectionException e) {
        e.printStackTrace();
      }
    }

    try {
      arrayCounter.moveVia("E", "translate", traveller2, new MsTiming(
          (word.length + 1) * 1000), new MsTiming(1500));
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    lang.nextStep();
    for (int i = 1; i <= n; i++) {
      sourceCode.highlight(0);
      sourceCode.unhighlight(3);
      varI.setText("i: " + i, null, null);
      if (i != 1) {
        wordMarker.increment(null, null);
      }
      lang.nextStep();
      for (Rule r : rules) {
        sourceCode.unhighlight(0);
        sourceCode.unhighlight(3);
        sourceCode.highlight(1);
        this.highlight(ruleDisplayStore.get(r.father));
        try {
          lang.nextStep();
          sourceCode.unhighlight(1);
          sourceCode.highlight(2);
          wordMarker.show();
          Text iLabel = lang.newText(new Offset(-8, -12, wordMarker, "Center"),
              "i", "i", basicDisplay);
          lang.nextStep();
          iLabel.hide();
          wordMarker.hide();
          if (r.terminal.equals(word[i - 1])) {
            sourceCode.unhighlight(2);
            sourceCode.highlight(3);
            V[i][i] = V[i][i].concat(r.father);
            tabelle.put(i, i, V[i][i], null, null);
            tabelle.highlightCell(i, i, null, null);
            lang.nextStep();
            tabelle.unhighlightCell(i, i, null, null);
          }
          sourceCode.unhighlight(2);
          this.unhighlight(ruleDisplayStore.get(r.father));
        } catch (StringIndexOutOfBoundsException sioobe) {

        }
      }
    }

    sourceCode.unhighlight(3);
    for (int j = 2; j <= n; j++) {
      sourceCode.highlight(4);
      sourceCode.unhighlight(9);
      varJ.setText("j: " + j, null, null);
      lang.nextStep();
      for (int i = j - 1; i >= 1; i--) {

        sourceCode.unhighlight(4);
        sourceCode.unhighlight(9);
        sourceCode.highlight(5);
        varI.setText("i: " + i, null, null);
        lang.nextStep();
        for (int k = i; k <= j - 1; k++) {

          sourceCode.unhighlight(5);
          sourceCode.unhighlight(9);
          sourceCode.highlight(6);
          varK.setText("k: " + k, null, null);
          lang.nextStep();
          for (Rule r : rules) {

            sourceCode.unhighlight(6);
            sourceCode.unhighlight(9);
            sourceCode.highlight(7);
            this.highlight(ruleDisplayStore.get(r.father));
            try {
              lang.nextStep();
              sourceCode.unhighlight(7);
              sourceCode.highlight(8);
              tabelle.highlightElem(i, k, null, null);
              tabelle.highlightElem(k + 1, j, null, null);
              lang.nextStep();
              if (V[i][k].contains(r.leftChild)
                  && V[k + 1][j].contains(r.rightChild)) {
                sourceCode.unhighlight(8);
                sourceCode.highlight(9);
                if (!V[i][j].contains(r.father)) {
                  V[i][j] = V[i][j].concat(r.father);
                }
                tabelle.put(i, j, V[i][j], null, null);
                tabelle.highlightElem(i, j, null, null);
                tabelle.highlightElem(i, k, null, null);
                tabelle.highlightElem(k + 1, j, null, null);
                lang.nextStep();
              }
              tabelle.unhighlightElem(i, k, null, null);
              tabelle.unhighlightElem(k + 1, j, null, null);
              tabelle.unhighlightElem(i, j, null, null);
              sourceCode.unhighlight(8);
              this.unhighlight(ruleDisplayStore.get(r.father));
            } catch (NullPointerException npe) {

            }
          }
        }
      }
    }

    try {
      sourceCode.highlight(10);
      tabelle.highlightCell(1, n, null, null);
      lang.nextStep("Ergebnis");
      if (V[1][n].contains(rules.get(0).father)) {
        Text result1 = lang.newText(new Offset(0, 20, sourceCode, "SW"),
            "Das Wort gehört zur gegebenen kontextfreien Sprache,", "success",
            basicDisplay);
        lang.newText(new Offset(0, 10, result1, "SW"),
            "da der Startzustand S in V_1_n enthalten ist.", "success",
            basicDisplay);
        tabelle.unhighlightCell(1, n, null, null);
        return;
      }
    } catch (NullPointerException npe) {

    }
    lang.nextStep();
    sourceCode.unhighlight(10);
    sourceCode.highlight(11);
    ;
    tabelle.unhighlightCell(1, n, null, null);
    lang.newText(
        new Offset(0, 20, sourceCode, "SW"),
        "Das Wort ist nicht Teil der Sprache, was man daran erkennen kann, dass S nicht in V_1_n enthalten ist.",
        "success", basicDisplay);
  }

  private void highlight(Primitive prim) {
    // used blue highlightcolor cause of common red-green-blindness
    prim.changeColor("color", Color.BLUE, null, null);
  }

  private void unhighlight(Primitive prim) {
    prim.changeColor("color", Color.BLACK, null, null);
  }

  private SourceCode paintSourceCode(Primitive root) {
    SourceCodeProperties scProps = new SourceCodeProperties();
    // used blue highlightcolor cause of common red-green-blindness
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    SourceCode sc = lang.newSourceCode(new Offset(50, 70, root, "NE"),
        "sourceCode", null, scProps);

    // \n won't be respected by the codedisplay of the animation
    for (String line : this.getAlgorithmCode().split("\n")) {
      sc.addCodeLine(line, null, 0, null);
    }

    return sc;
  }

  private static final String SOURCE_CODE = "Für i = 1 ... n"
                                              + "\n"
                                              + "    Für jede Regel (X->t)"
                                              + "\n"
                                              + "        Falls t = w_i "
                                              + "\n"
                                              + "             Setze V_i_i += X"
                                              + "\n"
                                              + "Für j = 2 ... n"
                                              + "\n"
                                              + "    Für i = j-1 ... 1"
                                              + "\n"
                                              + "        Für k = i ... j - 1"
                                              + "\n"
                                              + "            Für jede Regel (X->YZ)"
                                              + "\n"
                                              + "                 Falls Y in V_i_k und Z in V_k+1_j"
                                              + "\n"
                                              + "                      Setze V_i_j += X"
                                              + "\n"
                                              + "Falls S in V_1_n, return true"
                                              + "\n" + "return false";

  /**
   * Returns the sourcecode
   * 
   * @return String Sourcecode
   */
  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  /**
   * Returns the general name of the algorithm
   * 
   * @return String The name
   */
  public String getName() {
    return "CYK [DE]";
  }

  /**
   * Returns the specific name of the algorithm
   * 
   * @return String The name
   */
  public String getAlgorithmName() {
    return "Cocke–Younger–Kasami";
  }

  /**
   * Returns the Authors of this generator
   * 
   * @return String The authors
   */
  public String getAnimationAuthor() {
    return "Barbara Zöller, Malte Paskuda";
  }

  /**
   * Returns the html-encoded description
   * 
   * @return String The description
   */
  public String getDescription() {
    return "Der CYK-Algorithmus wird verwendet um zu &uuml;berpr&uuml;fen, ob ein Wort zu einer"
        + "\n"
        + "bestimmten kontextfreien Sprache geh&ouml;rt. Dazu muss zu der angegebenen Sprache eine Grammatik in"
        + "\n"
        + "Chomsky-Normalform (Abk.: CNF) vorliegen. "
        + "\n"
        + "\n"
        + "Eine formale Grammatik G ist in CNF, wenn aus jeder Regel entweder in zwei neue Nichtterminalsymbole"
        + "\n"
        + "oder in ein Terminalsymbol gewechselt werden kann."
        + "\n"
        + "In diesem Beispiel ist folgende Grammatik gegeben:"
        + "\n"
        + "\n"
        + "     S -> AB | a"
        + "\n"
        + "     A -> AB | a"
        + "\n"
        + "     B -> BA | b"
        + "\n"
        + "\n"
        + "Die Grammatik kann angepasst werden, in einem begrenzten Rahmen: Es bleibt immer bei den drei Regeln. "
        + "\n"
        + "Jede Regel verweist auf zwei Regeln (left und right) und ein Terminalsymbol."
        + "\n"
        + "Bei langen W&ouml;rtern wird die Animation sehr lange (8 Buchstaben = 1000 Schritte)";
  }

  /**
   * Returns the html-encoded pseudocode
   * 
   * @return String
   */
  public String getCodeExample() {
    return "F&uuml;r i = 1 ... n" + "\n" + "    F&uuml;r jede Regel (X->t)"
        + "\n" + "        Falls t = w_i " + "\n"
        + "              Setze V_i_i += X" + "\n" + " F&uuml;r j = 2 ... n"
        + "\n" + "     F&uuml;r i = j-1 ... 1" + "\n"
        + "        F&uuml;r k = i ... j - 1" + "\n"
        + "           F&uuml;r jede Regel (X->YZ)" + "\n"
        + "                  Falls Y in V_i_k und Z in V_k+1_j" + "\n"
        + "                       Setze V_i_j += X" + "\n"
        + "Falls S in V_1_n, return true" + "\n" + "return false";
  }

  /**
   * The file-extension used for the animation
   * 
   * @return String
   */
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /**
   * Language used throughout the animation
   * 
   * @return String
   */
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  /**
   * Language used for the code of the algorithm
   * 
   * @return String
   */
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}