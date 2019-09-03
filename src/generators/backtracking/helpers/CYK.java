package generators.backtracking.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
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
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class CYK {

  private Language     lang;
  // private Text header;
  // private Rect headerRect;
  private Rect         inRect;
//  private Rect         outRect;
  // private Text description;
  private Text         headerDescription;
  // private Rect desRect;
  // private Text wort;
  // private String benutzerWort;
  // private Text AusgabeGrammatik;
  // private Text showGrammar;
  // private Text showWord;
  // private Text showMatrix;
  private Text         showI;
  private Text         showJ;
  private Text         showK;
  private Text         showFirstI;
  private Text         showFirstJ;
  private Text         showFirstK;
  private SourceCode   grammatikGroup;

  // private String benutzerCNF;
  private StringMatrix matrix;

  public CYK(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  /**
   * Prueft ob das Wort in der Grammatik enthalten ist
   * 
   * @param t
   * @param s
   * @return
   */
  private boolean checker(String t, String s) {

    return t.contains(s);
  }

  /**
   * Entfernt aus einem String mehrfach vorkommende buchstaben
   * 
   * @param str
   * @return
   */
  private static String cleaner(String str) {
    StringBuffer sb = new StringBuffer();
    Map<String, Integer> map = new HashMap<String, Integer>();
    for (int i = 0; i < str.length(); i++) {
      String s = str.substring(i, i + 1);
      if (map.containsKey(s)) {
        map.put(s, (Integer) map.get(s) + 1);
      } else {
        map.put(s, 1);
        sb.append(s);
      }
    }
    return sb.toString();
  }

  /**
   * Sucht nach der Produktion in der Grammatik
   * 
   * @param s
   * @param parserGrammatik
   * @return
   */
  private String search(String s, String[][] parserGrammatik) {
    String ergebnis = "";
    for (int i = 0; i < parserGrammatik.length - 1; i++) {
      grammatikGroup.highlight(i);
      if (s.equals(parserGrammatik[i][1])) {
        ergebnis = ergebnis + parserGrammatik[i][0];
      }
      lang.nextStep("searchInGrammar " + i);
      grammatikGroup.unhighlight(i);
    }
    return ergebnis;
  }

  /**
   * 
   * @param tik
   * @param tikjk
   * @param parserGrammatik
   * @return
   */
  private String master(String tik, String tikjk, String[][] parserGrammatik) {
    String ergebnis = "";
    String[] array = merge(splitter(tik), splitter(tikjk));
    for (String permutationen : array) {
      ergebnis = ergebnis + search(permutationen, parserGrammatik);
    }
    return ergebnis;
  }

  /**
   * splitted ein gegebenen String in einzelne
   * 
   * @param split
   * @return
   */
  private String[] splitter(String split) {
    String[] array = new String[split.length()];
    for (int i = 0; i < split.length(); i++) {
      array[i] = String.valueOf(split.charAt(i));
    }
    return array;
  }

  /**
   * Merged zwei gegebene String arrays zu einer zusammen
   * 
   * @param left
   * @param right
   * @return
   */
  private String[] merge(String[] left, String[] right) {

    int counter = 0;
    String[] array = new String[left.length * right.length];
    for (int i = 0; i < left.length; i++) {
      for (int j = 0; j < right.length; j++) {
        array[counter] = left[i] + right[j];
        counter++;
      }
    }
    return array;
  }

  public void findWord(TextProperties headerProps,
      SourceCodeProperties deProps, SourceCodeProperties grammatikProps,
      TextProperties wortProps, String benutzerCNF, String benutzerWort,
      TextProperties show_i, TextProperties show_j, TextProperties show_k,
      TextProperties Text) {

    // header =
    lang.newText(new Coordinates(300, 25), "Cocke-Younger-Kasami (CYK)",
        "header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    // headerRect =
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "headerRect", null, rectProps);

    MatrixProperties matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.CYAN);
    // matrixProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
    // Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLUE);
    // matrixProps.set(AnimationPropertiesKeys.CASCADED_PROPERTY, true);

    // /////STEP
    lang.nextStep("description");

    TicksTiming t = new TicksTiming(1);

    // descr

    headerDescription = lang.newText(new Offset(-150, 75, "header", "SW"),
        "Description", "headerDescription", null, headerProps);
    SourceCode descr = lang.newSourceCode(new Offset(0, 15,
        "headerDescription", "SW"), "descr", null, deProps);

    descr
        .addCodeLine(
            "The Cocke-Younger-Kasami (CYK) algorithm (alternatively called CKY) is a parsing algorithm,for context-free grammars.",
            "descr", 0, t);
    descr
        .addCodeLine(
            "The standard version of CYK operates only on context-free grammars given in Chomsky normal form (CNF).",
            "descr", 0, t);
    descr
        .addCodeLine(
            "However any context-free grammar may be transformed to a CNF grammar expressing the same language.",
            "descr", 0, t);
    descr
        .addCodeLine(
            "The importance of the CYK algorithm stems from its high efficiency in certain situations.",
            "descr", 0, t);

    RectProperties rectPropsZ = new RectProperties();

    rectPropsZ.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    inRect = lang.newRect(new Offset(-10, -5, "headerDescription", "NW"),
        new Offset(10, 5, "descr", "SE"), "inRect", null, rectPropsZ);

    lang.nextStep("Pseudocode");

    // Next Step + hide
    descr.hide();
    inRect.hide();
    headerDescription.hide();
    // outRect.hide();

    TextProperties descriptionProps = new TextProperties();
    descriptionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 16));
    // description =
    lang.newText(new Offset(-120, 40, "headerDescription", "SW"), "PseudoCode",
        "description", null, descriptionProps);

    // sdescr

    SourceCode sDescr = lang.newSourceCode(new Offset(0, 10, "description",
        "SW"), "sDescr", null, deProps);

    sDescr.addCodeLine(
        "let the input be a string S consisting of n characters: a1 ... an.",
        "sDescr", 0, null);
    sDescr.addCodeLine(
        "let the grammar contain r nonterminal symbols R1 ... Rr.", "sDescr",
        0, null);
    sDescr
        .addCodeLine(
            "This grammar contains the subset Rs which is the set of start symbols.",
            "sDescr", 0, null);
    sDescr
        .addCodeLine(
            "let T[n,n] be an matrix of nonterminals. Initialize all elements of T to the empty string.",
            "sDescr", 0, null);
    sDescr.addCodeLine("for each i = 0 to n", "sDescr", 0, null);
    sDescr.addCodeLine("    for each unit production Rj -> ai", "sDescr", 0,
        null);
    sDescr.addCodeLine("        set T[i,1] = Rj", "sDescr", 0, null);
    sDescr.addCodeLine("for each j = 1 to n -- Length of span", "sDescr", 0,
        null);
    sDescr.addCodeLine("    for each i = 0 to n-j -- Start of span", "sDescr",
        0, null);
    sDescr.addCodeLine("        for each k = 0 to j-1 -- Partition of span",
        "sDescr", 0, null);
    sDescr.addCodeLine(
        "            set T[i,j] = T[i,j] union T[i,k] and T[i+k,j-k]",
        "sDescr", 0, null);
    sDescr.addCodeLine("if any of T[1,n] equals any nonterminals of Rs then",
        "sDescr", 0, null);
    sDescr.addCodeLine("    S is member of language", "sDescr", 0, null);
    sDescr.addCodeLine("else", "sDescr", 0, null);
    sDescr.addCodeLine("    S is not member of language", "sDescr", 0, null);

    // desRect =
    lang.newRect(new Offset(-10, -5, "description", "NW"), new Offset(10, 5,
        "sDescr", "SE"), "desRect", null, rectPropsZ);

    lang.nextStep("grammar");

    // hide
    // description.hide();

    // benutzerCNF =
    // "L -> U,X -> V,Z -> UY,Z -> VY,Y -> b,A -> a,U -> AX,V -> AY";
    // benutzerWort = "ab";

    // showWord =
    lang.newText(new Offset(50, -50, "sDescr", "NE"), "Wort:", "word", null,
        Text);

    // wort =
    lang.newText(new Offset(10, 0, "word", "NE"), benutzerWort, "wort", null,
        wortProps);

    String[][] parserGrammatik = Parser.parseString(benutzerCNF);

    // showGrammar =
    lang.newText(new Offset(0, 30, "word", "SW"), "Grammatik:", "gramma", null,
        Text);

    grammatikGroup = lang.newSourceCode(new Offset(4, 0, "gramma", "NE"),
        "grammatikGroup", null, grammatikProps);
    grammatikGroup.addCodeLine(parserGrammatik[0][0] + "->"
        + parserGrammatik[0][1], "grammatikGroup", 0, null);

    for (int i = 1; i < parserGrammatik.length - 1; i++) {
      grammatikGroup.addCodeLine(parserGrammatik[i][0] + "->"
          + parserGrammatik[i][1], "grammatikGroup", 0, null);
    }

    lang.nextStep("start");

    String[][] T = new String[benutzerWort.length()][benutzerWort.length()];

    // T leer setzen
    for (int i = 0; i < benutzerWort.length(); i++) {
      for (int j = 0; j < benutzerWort.length(); j++) {
        T[j][i] = "";
      }
    }

    // showMatrix =
    lang.newText(new Offset(200, 0, "word", "NE"),
        "Anwenden des CYK Algorithmus:", "showMatrix", null, Text);
    matrix = lang.newStringMatrix(new Offset(0, 20, "showMatrix", "SW"), T,
        "matrix", null, matrixProps);

    showFirstJ = lang.newText(new Offset(80, 0, "showMatrix", "NE"), "j:",
        "showFirstJ", null, show_j);
    showFirstI = lang.newText(new Offset(0, 42, "showFirstJ", "SW"), "i:",
        "showFirstI", null, show_i);
    showFirstK = lang.newText(new Offset(0, 42, "showFirstI", "SW"), "k:",
        "showFirstK", null, show_k);

    // Tabelle Fuellen erste for schleife 1. zeile, einzelnes wort
    for (int j = 0; j < benutzerWort.length(); j++) {
      sDescr.highlight(4);
      showJ = lang.newText(new Offset(15, 0, "showFirstJ", "NE"), "" + j,
          "showJ" + j, null, show_j);
      String s = String.valueOf(benutzerWort.charAt(j));
      // T leer setzen
      T[j][0] = "";
      for (int i = 0; i < parserGrammatik.length - 1; i++) {
        sDescr.highlight(5);
        grammatikGroup.highlight(i);
        showI = lang.newText(new Offset(15, 0, "showFirstI", "NE"), "" + i,
            "showI" + i, null, show_i);
        if (s.equals(parserGrammatik[i][1])) {
          sDescr.highlight(6);
          matrix.highlightCell(0, j, null, null);
          T[j][0] = T[j][0] + parserGrammatik[i][0];
          matrix.put(0, j, T[j][0], null, null);
          matrix.unhighlightCell(0, j, null, null);
          // lang.nextStep("Matrix["+ j +"][0]");
        }
        lang.nextStep("Matrix[" + j + "][0]");
        sDescr.unhighlight(6);
        lang.nextStep("setFirstRow " + i);
        grammatikGroup.unhighlight(i);
        showI.hide();
      }
      showJ.hide();
      sDescr.unhighlight(5);
    }
    sDescr.unhighlight(4);

    // Restliche Tabelle fuellen
    for (int j = 1; j < benutzerWort.length(); j++) {
      sDescr.highlight(7);
      showJ = lang.newText(new Offset(15, 0, "showFirstJ", "NE"), "" + j,
          "showJ" + j, null, show_j);
      for (int i = 0; i < benutzerWort.length() - j; i++) {
        sDescr.highlight(8);
        showI = lang.newText(new Offset(15, 0, "showFirstI", "NE"), "" + i,
            "showI" + i + j, null, show_i);
        for (int k = 0; k < j; k++) {
          sDescr.highlight(9);
          sDescr.highlight(10);
          matrix.highlightCell(j, i, null, null);
          T[i][j] = cleaner(T[i][j]
              + master(T[i][k], T[i + k + 1][j - k - 1], parserGrammatik));
          matrix.put(j, i, T[i][j], null, null);
          matrix.unhighlightCell(j, i, null, null);
          showK = lang.newText(new Offset(15, 0, "showFirstK", "NE"), "" + k,
              "showK" + k + i + j, null, show_k);
          lang.nextStep("Matrix[" + i + "][" + j + "]");
          showK.hide();
        }
        sDescr.unhighlight(9);
        sDescr.unhighlight(10);
        lang.nextStep("step " + i);
        showI.hide();
      }
      sDescr.unhighlight(8);
      lang.nextStep("step " + j);
      showJ.hide();
    }
    sDescr.unhighlight(7);
    showFirstJ.hide();
    showFirstI.hide();
    showFirstK.hide();

    if (checker(T[0][T.length - 1], parserGrammatik[0][0])) {
      sDescr.highlight(12);
    } else {
      sDescr.highlight(14);
    }
  }
}