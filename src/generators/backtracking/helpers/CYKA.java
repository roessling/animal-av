package generators.backtracking.helpers;

import java.awt.Color;

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

public class CYKA {

  private Language     lang;
  // private Text header;
  // private Rect headerRect;
  private Rect         inRect;
  private Rect         outRect;
  private Text         description;
  // private Rect desRect;
  // private Text wort;
  // private String benutzerWort;
  // private Text AusgabeGrammatik;
  private Text         showGrammar;
  // private Text showWord;

  // private String benutzerCNF;
  private StringMatrix matrix;

  public CYKA(Language l) {
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
   * Entfernt aus einem String mehrfach vorkommende buchstaben zur besseren
   * Darstellung
   * 
   * @param str
   * @return
   */
  private String cleaner(String str) {
    String aString = str;
    for (int i = 1; i < aString.length(); i++) {
      if (aString.charAt(i) == aString.charAt(i - 1)) {
        // Doppelten Buchstaben entfernen
        aString = aString.substring(0, i) + aString.substring(i + 1);
        // i zurückstellen, damit auch Buchstaben, die oefter
        // als zwei mal vorkommen, nur einmal übrigbleiben
        --i;
      }
    }
    return aString;
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
      if (s.equals(parserGrammatik[i][1])) {
        ergebnis = ergebnis + parserGrammatik[i][0];
      }
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

  // public CYKA(Language l) {
  // lang = l;
  // // This initializes the step mode. Each pair of subsequent steps has to
  // // be divdided by a call of lang.nextStep();
  // lang.setStepMode(true);
  // }

  public void findWord(TextProperties headerProps,
      SourceCodeProperties deProps, TextProperties descriptionProps,
      TextProperties mProps, String benutzerCNF, String benutzerWort) {

    // headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY,30);

    // header =
    lang.newText(new Coordinates(400, 25), "Cocke-Younger-Kasami (CYK)",
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
    // matrixProps.set(AnimationPropertiesKeys.The_Answer_to_Life_the_Universe_and_Everything,
    // true);

    // /////STEP
    lang.nextStep();

    TicksTiming t = new TicksTiming(1);

    // descr

    SourceCode descr = lang.newSourceCode(new Coordinates(10, 100), "descr",
        null, deProps);

    descr
        .addCodeLine(
            "Der Cocke-Younger-Kasami-Algorithmus (CYK-Algorithmus) ist ein Algorithmus aus dem Gebiet der Theoretischen Informatik.",
            "descr", 0, t);
    descr
        .addCodeLine(
            "Mit ihm lässt sich feststellen, ob ein Wort zu einer bestimmten kontextfreien Sprache gehört. In der Fachsprache bezeichnet man dies als Lösen des Wortproblems für kontextfreie Sprachen.",
            "descr", 0, t);
    descr
        .addCodeLine(
            "Um den Algorithmus anzuwenden, muss zu der vorgegebenen Sprache eine Grammatik in Chomsky-Normalform vorliegen.",
            "descr", 0, t);

    RectProperties rectPropsZ = new RectProperties();

    rectPropsZ.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    inRect = lang.newRect(new Offset(-10, -2, "descr",
        AnimalScript.DIRECTION_NW), new Offset(10, 2, "descr", "SE"), "inRect",
        null, rectPropsZ);
    outRect = lang.newRect(new Offset(-11, -3, "descr",
        AnimalScript.DIRECTION_NW), new Offset(11, 3, "descr", "SE"),
        "outRect", null, rectPropsZ);

    lang.nextStep();

    // Next Step + hide
    descr.hide();
    inRect.hide();
    outRect.hide();

    description = lang.newText(new Offset(10, 40, "inRect", "SW"),
        "PseudoCode", "description", null, descriptionProps);

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
            "let P[n,n,r] be an array of booleans. Initialize all elements of P to false.",
            "sDescr", 0, null);
    sDescr.addCodeLine("for each i = 1 to n", "sDescr", 0, null);
    sDescr.addCodeLine("	for each unit production Rj -> ai", "sDescr", 0, null);
    sDescr.addCodeLine("		set P[i,1,j] = true", "sDescr", 0, null);
    sDescr.addCodeLine("for each i = 2 to n -- Length of span", "sDescr", 0,
        null);
    sDescr.addCodeLine("	for each j = 1 to n-i+1 -- Start of span", "sDescr",
        0, null);
    sDescr.addCodeLine("		for each k = 1 to i-1 -- Partition of span",
        "sDescr", 0, null);
    sDescr.addCodeLine(
        "			if P[j,k,B] and P[j+k,i-k,C] then set P[j,i,A] = true", "sDescr",
        0, null);
    sDescr
        .addCodeLine(
            "if any of P[1,n,x] is true (x is iterated over the set s, where s are all the indices for Rs) then",
            "sDescr", 0, null);
    sDescr.addCodeLine("	S is member of language", "sDescr", 0, null);
    sDescr.addCodeLine("else", "sDescr", 0, null);
    sDescr.addCodeLine("	S is not member of language", "sDescr", 0, null);

    // desRect =
    lang.newRect(new Offset(-10, -5, "description", "NW"), new Offset(10, 5,
        "sDescr", "SE"), "desRect", null, rectPropsZ);

    lang.nextStep();

    // hide
    description.hide();

    // benutzerCNF =
    // "L -> U,X -> V,Z -> UY,Z -> VY,Y -> b,A -> a,U -> AX,V -> AY";
    // benutzerWort = "ab";

    // showWord =
    lang.newText(new Offset(50, 0, "sDescr", "NE"), "Wort:", "word", null,
        mProps);

    // wort =
    lang.newText(new Offset(0, 40, "word", "SW"), benutzerWort, "wort", null,
        mProps);

    String[][] parserGrammatik = Parser.parseString(benutzerCNF);

    showGrammar = lang.newText(new Offset(300, 0, "header", "NE"), "Grammatik",
        "gramma", null, mProps);
    // AusgabeGrammatik =
    lang.newText(new Offset(0, 40, "gramma", "SW"), parserGrammatik[0][0]
        + "->" + parserGrammatik[0][1], "" + 0, null, mProps);

    for (int i = 1; i < parserGrammatik.length - 1; i++) {
      // AusgabeGrammatik =
      lang.newText(new Offset(0, 20, "" + (i - 1), "SW"), parserGrammatik[i][0]
          + "->" + parserGrammatik[i][1], "" + i, null, mProps);
    }

    lang.nextStep();
    showGrammar.hide();
    // showGrammar.hide();

    //Angeblich ab hier tritt ein Fehler auf index out ouf Bound......
    String[][] T = new String[benutzerWort.length()][benutzerWort.length()];
    matrix = lang.newStringMatrix(new Coordinates(50, 50), T, "matrix", null,
        matrixProps);

    // T leer setzen
    for (int i = 0; i < benutzerWort.length(); i++) {
      for (int j = 0; j < benutzerWort.length(); j++) {
        T[j][i] = "";
      }
    }

    sDescr.highlight(5);
    sDescr.highlight(6);
    sDescr.highlight(7);

    // Tabelle Fuellen erste for schleife 1. zeile, einzelnes wort
    for (int j = 0; j < benutzerWort.length(); j++) {
      String s = String.valueOf(benutzerWort.charAt(j));
      // T leer setzen
      T[j][0] = "";
      for (int i = 0; i < parserGrammatik.length - 1; i++) {
        if (s.equals(parserGrammatik[i][1])) {
          matrix.highlightCell(0, j, null, null);
          T[j][0] = T[j][0] + parserGrammatik[i][0];
          matrix.put(0, j, T[j][0], null, null);
          matrix.unhighlightCell(0, j, null, null);
          lang.nextStep();
        }
      }
    }
    sDescr.unhighlight(5);
    sDescr.unhighlight(6);
    sDescr.unhighlight(7);

    sDescr.highlight(8);
    sDescr.highlight(9);
    sDescr.highlight(10);
    sDescr.highlight(11);

    // Restliche Tabelle fuellen
    for (int j = 1; j < benutzerWort.length(); j++) {
      for (int i = 0; i < benutzerWort.length() - j; i++) {
        // T[i][j]="";
        for (int k = 0; k < j; k++) {
          matrix.highlightCell(j, i, null, null);
          T[i][j] = cleaner(T[i][j]
              + master(T[i][k], T[i + k + 1][j - k - 1], parserGrammatik));
          matrix.put(j, i, T[i][j], null, null);
          matrix.unhighlightCell(j, i, null, null);
          /*
           * System.out.println(j + "\t" + i + "\t" + k + "\t" + T[i][k] + "\t"
           * + T[i + k + 1][j - k - 1] + "\t" + master(T[i][k], T[i + k + 1][j -
           * k - 1], parserGrammatik) + "\t" + T[i][j]);
           */

        }
      }
    }
    // System.out.println(checker(T[0][T.length-1],parserGrammatik[0][0]));

    sDescr.unhighlight(8);
    sDescr.unhighlight(9);
    sDescr.unhighlight(10);
    sDescr.unhighlight(11);

    sDescr.highlight(12);
    if (checker(T[0][T.length - 1], parserGrammatik[0][0])) {
      sDescr.highlight(13);

    } else {
      sDescr.highlight(14);
      sDescr.highlight(15); // GR

    }

  }

}
