package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class LongestCommonSubsequence implements Generator {
  private Language             lang;
  private SourceCodeProperties scPr;
  // private String string2;
  // private String string1;
  // private MatrixProperties matrixPr2;
  // private MatrixProperties matrixPr1;

  private MatrixProperties     matProps, matProps2;
  private TextProperties       textProps2, textL, textD;
  static String[][]            Laenge   = new String[0][0];
  static String[][]            Richtung = new String[0][0];
  static String                first;
  static String                second;
  String[][]                   l;
  String[][]                   d;
  private SourceCodeProperties scProps, scProps1, scProps2;
  private SourceCode           sc, sc2, sc_desc, sc_conc, desc, title;
  StringMatrix                 mat;
  private static String        StringOne, StringTwo;

  public void init() {
    // initialize the main elements
    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript("Longest_common_subsequence Animation",
        "Abid Chahine _ Hfaiedh Najib", 640, 480);
    // Activate step control
    lang.setStepMode(true);
    // create array properties with default values

    textProps2 = new TextProperties();

    textProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
    textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 23));

  }

  public void showSourceCodedescription() {
    // set the visual properties for the title code
    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    // now, create the source code entity
    title = lang.newSourceCode(new Coordinates(25, 0), "Title", null, scProps);

    title.addCodeLine("Longest Common Subsequence Problem", null, 0, null);

    // set the visual properties for the sourcedescription code
    scProps1 = new SourceCodeProperties();
    scProps1.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    scProps1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    // now, create the source code entity
    desc = lang.newSourceCode(new Coordinates(120, 60), "sourceCodeDesc", null,
        scProps1);

    desc.addCodeLine("Beschreibung des Algorithmus", null, 0, null);
    // set the visual properties for the source code
    scProps2 = new SourceCodeProperties();
    scProps2.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 17));
    scProps2.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity
    sc_desc = lang.newSourceCode(new Coordinates(35, 130), "sourceCodeDesc",
        null, scProps2);
    // add a code line
    // parameters: code itself; name (can be null); indentation level;
    // display options
    lang.nextStep();
    sc_desc.addCodeLine("Der Algorithmus in Worten :", null, 2, null);
    sc_desc.addCodeLine("", null, 0, null);
    sc_desc.addCodeLine("", null, 0, null);

    sc_desc
        .addCodeLine(
            "Das Longest Common Subsequence (LCS) Problem ist von Bedeutung in verschiedenen "
                + "Bereichen der Informatik ", null, 1, null);

    sc_desc.addCodeLine(
        "(z.B : - in der Bioinformatik beim Vergleich von DNA-Sequenzen, ",
        null, 0, null);
    sc_desc
        .addCodeLine(
            "- in Software-Engineering beim Vergleich bzgl. Gemeinsamkeiten zweier Versionen "
                + "von Programmcode).", null, 3, null);

    lang.nextStep();
    sc_desc.addCodeLine("", null, 0, null);
    sc_desc
        .addCodeLine(
            "Gegeben sind zwei Zeichenketten X = <x1,...,xm> und  Y = <y1,...,yn> .  ",
            null, 1, null);
    sc_desc
        .addCodeLine(
            "Das Longest Common Subsequence Problem besteht darin, die laengste Teilfolge zu finden, die sowohl in X ",
            null, 0, null);
    sc_desc.addCodeLine("als auch in Y enthalten ist.", null, 0, null);

    sc_desc.addCodeLine("", null, 0, null);
    lang.nextStep();
    sc_desc
        .addCodeLine(
            "Das heisst, dass der Algorithmus eine Sequenz von maximaler Laenge, die in X und Y vorkommt, liefert.",
            null, 1, null);
    sc_desc
        .addCodeLine(
            "Elemente der Sequenz muessen nicht konsekutiv , jedoch in korrekter Reihenfolge in X und Y vorkommen",
            null, 0, null);
    lang.nextStep();

  }

  public void showConclusionSlide(String first, String second, String value,
      String length) {

    scProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 20));

    sc_conc = lang.newSourceCode(new Coordinates(15, 75), "sourceCodeDesc",
        null, scProps2);

    sc_conc.addCodeLine(
        "Brechnen Sie die laengste gemeinsame Teilsequenze von '" + first
            + "' und '" + second + "' ! ", null, 6, null);

    sc_conc.addCodeLine("", null, 0, null);
    lang.nextStep();
    sc_conc.addCodeLine(
        "Mit der Length_Matrix [i,j]-Werte , haben wir die Laenge der "
            + "laengsten gemeinsamen Teilsequenz von ", null, 3, null);

    sc_conc.addCodeLine("'" + first + "' und '" + second + "'  bestimmt.",
        null, 1, null);

    sc_conc.addCodeLine("Die Laenge der LCS ist : " + length + ". ", null, 3,
        null);

    sc_conc.addCodeLine("Die laengste gemeinsame Teilsequenz ist : '" + value
        + "' . ", null, 3, null);

    sc_conc.addCodeLine("", null, 0, null);

    int fl = first.length();
    int sl = second.length();

    int mul = sl * fl;

    String fLen = String.valueOf(fl);
    String sLen = String.valueOf(sl);
    String mulstr = String.valueOf(mul);

    lang.nextStep();

    sc_conc
        .addCodeLine(
            " Die Zeitkomlexitaet des 'longest common subsequence' Algorithmus ist : ",
            null, 3, null);
    sc_conc.addCodeLine(
        "O (Laenge des ersten Wortes.Laenge des zweiten Wortes).  ", null, 1,
        null);

    sc_conc.addCodeLine("", null, 0, null);

    sc_conc.addCodeLine(
        "Bei der Berechnung der laengsten gemeinsamen Teilsequenz von '"
            + first + "'", null, 3, null);

    sc_conc.addCodeLine(" und '" + second
        + "' ist die Zeitkomlexitaet des LCS Algorithmus O ( " + fLen + " * "
        + sLen + " ) = O (" + mulstr + ") . ", null, 0, null);
    lang.nextStep();
  }

  public void showSourceCode() {

    // create the source code entity

    sc = lang.newSourceCode(
        new Offset(-12, 7, title, AnimalScript.DIRECTION_SW), "sourceCode",
        null, scPr);

    sc.addCodeLine("public void lcs (String a , String b) {", null, 0, null);
    sc.addCodeLine("int lcs_Length = 0 ;", null, 1, null);
    sc.addCodeLine("int up , left ;", null, 1, null);
    sc.addCodeLine("String direction ;", null, 1, null);

    sc.addCodeLine("for (int i = 2 ; i < a.length() + 2; i++) {", null, 1, null);
    sc.addCodeLine("for (int j = 2 ; j < b.length() +2 ; j++) { ", null, 2,
        null);
    sc.addCodeLine("if (a.charAt(i) == b.charAt(j)) {", null, 3, null);
    // sc.addCodeLine("diago = Length_Matrix[i - 1][j - 1]);", null, 4, null);
    sc.addCodeLine("Length_Matrix[i][j] = Length_Matrix[i-1][j-1] + 1 ;", null,
        4, null);

    sc.addCodeLine("Direction_Matrix [i][j] = 'diag' ;", null, 4, null);
    sc.addCodeLine("if (Length_Matrix[i][j] > lcs_Length) ", null, 4, null);
    sc.addCodeLine("lcs_Length = Length_Matrix[i][j] ;  ", null, 5, null);
    sc.addCodeLine("}", null, 3, null);

    sc.addCodeLine("else {	", null, 3, null);

    sc.addCodeLine("up = Length_Matrix[i - 1][j]; ", null, 4, null);
    sc.addCodeLine("left = Length_Matrix[i][j - 1];", null, 4, null);
    sc.addCodeLine("if (up >= left) { ", null, 4, null);
    sc.addCodeLine("Length_Matrix[i][j] = up ; ", null, 5, null);
    sc.addCodeLine("Direction_Matrix [i][j] = 'up' ; ", null, 5, null);
    sc.addCodeLine("} ", null, 4, null);
    sc.addCodeLine("else { ", null, 4, null);
    sc.addCodeLine("Length_Matrix[i][j] = left ; ", null, 5, null);
    sc.addCodeLine("Direction_Matrix [i][j] = 'left' ; ", null, 5, null);
    sc.addCodeLine("} ", null, 4, null);

    sc.addCodeLine("}  ", null, 2, null); // innere Schleife
    // sc.addCodeLine(" Length_Matrix [i][j] = current ;  ", null, 2, null);
    // sc.addCodeLine(" Direction_Matrix [i][j] = direction ;  ", null, 2,

    sc.addCodeLine("}", null, 1, null); // äußere Schleife

  }

  public void showSourceCode2() {

    sc2 = lang.newSourceCode(
        new Offset(-38, 0, mat, AnimalScript.DIRECTION_SW), "sourceCode2",
        null, scPr);

    sc.addCodeLine("String lcs_Value = new String() ;  ", null, 1, null);
    sc.addCodeLine("int i = first.length() + 1 ;", null, 1, null);
    sc.addCodeLine("int j = second.length() + 1 ;  ", null, 1, null);

    sc2.addCodeLine("while (i != 1 && j != 1) { ", null, 1, null);
    sc2.addCodeLine("if (Direction_Matrix[i][j] == 'diag') {", null, 2, null);
    sc2.addCodeLine("lcs_Value = a.charAt(i - 2) + lcs_Value ;  ", null, 3,
        null);
    sc2.addCodeLine("i = i - 1 ;  j = j - 1;  ", null, 3, null);
    sc2.addCodeLine("}  ", null, 2, null);

    sc2.addCodeLine("if (Direction_Matrix[i][j] == 'up') ", null, 2, null);
    sc2.addCodeLine("i = i - 1 ;  ", null, 3, null);

    sc2.addCodeLine("if (Direction_Matrix[i][j] == 'left') ", null, 2, null);
    sc2.addCodeLine("j = j - 1 ;  ", null, 3, null);
    sc2.addCodeLine("}", null, 1, null); // while
    sc2.addCodeLine("}", null, 0, null);

  }

  public void getLCS(String firstString, String secondString) {
    String first = firstString.toUpperCase(), second = secondString
        .toUpperCase();
    fillMatrices(first, second);

    // int s = 0;

    String laenge = "0";
    String str = "";
    Timing defaultTiming = new TicksTiming(15);
    // String str = String.valueOf(s) ;
    // lang.nextStep();
    showSourceCodedescription();

    sc_desc.hide();
    desc.hide();

    showSourceCode();

    sc.highlight(0);

    mat = lang.newStringMatrix(new Offset(21, 18, title,
        AnimalScript.DIRECTION_NE), Laenge, "matrix", null, matProps);

    Text matrix_length = lang.newText(new Offset(20, -20, mat,
        AnimalScript.DIRECTION_NW), "Length Matrix :", "len_matrix", null,
        textL);

    Text lcs_length = lang.newText(new Offset(95, 70, mat,
        AnimalScript.DIRECTION_SW), "lcs_Length = ", "lcs", null, textProps2);
    Text length = lang.newText(new Offset(0, 0, lcs_length,
        AnimalScript.DIRECTION_NE), laenge, "lcsL", null, textProps2);

    StringMatrix mat2 = lang.newStringMatrix(new Offset(11, 0, mat,
        AnimalScript.DIRECTION_NE), Richtung, "matrix2", null, matProps2);

    Text matrix_direction = lang.newText(new Offset(20, -20, mat2,
        AnimalScript.DIRECTION_NW), "Direction Matrix :", "dir_matrix", null,
        textD);

    // mat.highlightCellColumnRange(0, 2, second.length() + 1,

    sc.unhighlight(0);
    lang.nextStep();

    // mat.highlightCellRowRange(2, first.length() + 1, 0, n

    // String q;
    int ij = 0;
    int up, diago, left;

    // dynamic programming /
    for (int i = 2; i < first.length() + 2; i++) {
      sc.highlight(4);
      mat.highlightCell(i, 0, null, defaultTiming);
      mat2.highlightCell(i, 0, null, defaultTiming);

      lang.nextStep();
      sc.unhighlight(4);
      for (int j = 2; j < second.length() + 2; j++) {
        sc.highlight(5);

        mat.highlightCell(0, j, null, defaultTiming);
        mat2.highlightCell(0, j, null, defaultTiming);

        lang.nextStep();

        // diag

        if (first.charAt(i - 2) == second.charAt(j - 2)) {
          sc.toggleHighlight(5, 6);
          lang.nextStep();

          diago = Integer.parseInt(Laenge[i - 1][j - 1]);
          ij = diago + 1;
          mat.put(i, j, String.valueOf(ij), null, defaultTiming);
          mat.highlightCell(i, j, null, defaultTiming);
          Richtung[i][j] = "diag";
          mat2.put(i, j, Richtung[i][j], null, defaultTiming);
          mat2.highlightCell(i, j, null, defaultTiming);
          sc.toggleHighlight(6, 7);
          sc.highlight(8);
          lang.nextStep();

          sc.unhighlight(8);
          sc.unhighlight(7);
          if (ij > Integer.parseInt(laenge)) {
            sc.highlight(9);
            mat.highlightCell(i, j, null, defaultTiming);
            length.changeColor("color", Color.BLACK, null, null);

            lang.nextStep();
            laenge = String.valueOf(ij);
            length.setText(laenge, null, defaultTiming);

            sc.toggleHighlight(9, 10);
            mat.unhighlightCell(i, j, null, defaultTiming);
            length.changeColor("color", Color.LIGHT_GRAY, null, null);
            lang.nextStep();
            sc.unhighlight(10);
          }

        } else {
          sc.toggleHighlight(5, 12);
          lang.nextStep();
          up = Integer.parseInt(Laenge[i - 1][j]);
          left = Integer.parseInt(Laenge[i][j - 1]);
          sc.unhighlight(12);
          mat.highlightCell(i - 1, j, null, defaultTiming);
          mat.highlightCell(i, j - 1, null, defaultTiming);

          // sc.highlight(13) ; sc.highlight(14) ;

          // sc.unhighlight(13) ; sc.unhighlight(14) ;

          if (up >= left) { // up
            sc.highlight(15);

            lang.nextStep();
            ij = up;
            mat.put(i, j, String.valueOf(ij), null, defaultTiming);
            Richtung[i][j] = "up";
            mat2.put(i, j, Richtung[i][j], null, defaultTiming);

            mat.highlightCell(i, j, null, defaultTiming);
            mat2.highlightCell(i, j, null, defaultTiming);
            sc.toggleHighlight(15, 16);
            sc.highlight(17);
            lang.nextStep();

            sc.unhighlight(16);
            sc.unhighlight(17);

          } else { // left
            // Laenge[i][j]=Laenge[i][j-1];
            sc.highlight(19);
            lang.nextStep();
            ij = left;

            mat.put(i, j, String.valueOf(ij), null, defaultTiming);
            Richtung[i][j] = "left";
            mat2.put(i, j, Richtung[i][j], null, defaultTiming);

            mat.highlightCell(i, j, null, defaultTiming);
            mat2.highlightCell(i, j, null, defaultTiming);

            sc.toggleHighlight(19, 20);
            sc.highlight(21);

            lang.nextStep();

            sc.unhighlight(20);
            sc.unhighlight(21);
          }

          mat.unhighlightCell(i - 1, j, null, defaultTiming);
          mat.unhighlightCell(i, j - 1, null, defaultTiming);
        }
        // q = String.valueOf(ij);
        // mat.put(i, j, q, null, defaultTiming);

        mat.unhighlightCell(0, j, null, defaultTiming);
        mat2.unhighlightCell(0, j, null, defaultTiming);

        mat.unhighlightCell(i, j, null, defaultTiming);
        mat2.unhighlightCell(i, j, null, defaultTiming);
      }
      mat.unhighlightCell(i, 0, null, defaultTiming);
      mat2.unhighlightCell(i, 0, null, defaultTiming);
    }

    // Backtracking //
    // String lcs = new String();
    int i = first.length() + 1;
    int j = second.length() + 1;

    showSourceCode2();
    lcs_length.moveBy("translate", 210, 70, null, null);
    length.moveBy("translate", 210, 70, null, null);

    Text lcs_value = lang.newText(new Offset(305, 55, mat,
        AnimalScript.DIRECTION_SW), "lcs_Value = ", "lcsL", null, textProps2);
    Text value = lang.newText(new Offset(0, 0, lcs_value,
        AnimalScript.DIRECTION_NE), str, "lcsLV", null, textProps2);

    // sc2.highlight(0) ;

    while (i != 1 && j != 1) {
      sc2.highlight(0);
      lang.nextStep();
      mat2.highlightCell(i, j, null, defaultTiming);

      sc2.unhighlight(0);
      if (Richtung[i][j] == "diag") { // diagonal //
        sc2.highlight(1);

        lang.nextStep();
        sc2.toggleHighlight(1, 2);

        mat2.highlightCell(i, 0, null, defaultTiming);
        mat2.highlightCell(0, j, null, defaultTiming);

        str = first.charAt(i - 2) + str;
        value.setText(str, null, defaultTiming);
        lang.nextStep();
        mat2.unhighlightCell(i, 0, null, defaultTiming);
        mat2.unhighlightCell(0, j, null, defaultTiming);
        mat2.unhighlightCell(i, j, null, defaultTiming);

        i = i - 1;
        j = j - 1;

        sc2.toggleHighlight(2, 3);

        mat2.highlightCell(i, j, null, defaultTiming);
        lang.nextStep();
        sc2.unhighlight(3);
      }

      if (Richtung[i][j] == "up") { // up //
        sc2.highlight(5);
        mat2.highlightCell(i, j, null, defaultTiming);
        lang.nextStep();

        sc2.toggleHighlight(5, 6);

        i = i - 1;
        mat2.unhighlightCell(i + 1, j, null, defaultTiming);
        mat2.highlightCell(i, j, null, defaultTiming);
        lang.nextStep();
        sc2.unhighlight(6);

      }
      if (Richtung[i][j] == "left") { // left
        sc2.highlight(7);
        // mat2.highlightCell(i, j, null, defaultTiming);
        lang.nextStep();
        sc2.toggleHighlight(7, 8);

        j = j - 1;
        mat2.unhighlightCell(i, j + 1, null, defaultTiming);
        mat2.highlightCell(i, j, null, defaultTiming);
        lang.nextStep();
        sc2.unhighlight(8);

      }

    }
    mat2.unhighlightCell(i, j, null, defaultTiming);

    mat.hide();
    matrix_direction.hide();
    mat2.hide();
    matrix_length.hide();
    sc.hide();
    sc2.hide();
    lcs_value.hide();
    value.hide();
    lcs_length.hide();
    length.hide();

    showConclusionSlide(first, second, str, laenge);

  }

  public void fillMatrices(String firstString, String secondString) {
    String first = firstString.toUpperCase(), second = secondString
        .toUpperCase();

    int f, g;
    f = first.length();
    g = second.length();

    Laenge = new String[f + 2][g + 2];
    Richtung = new String[f + 2][g + 2];
    Laenge[0][0] = "0";
    Laenge[0][1] = "0";
    Laenge[1][0] = "0";
    Richtung[0][0] = "*";
    Richtung[0][1] = "-";
    Richtung[1][0] = "-";

    for (int i = 2; i < f + 2; i++) // 8
    {
      Laenge[i][0] = String.valueOf(first.charAt(i - 2));
      Richtung[i][0] = String.valueOf(first.charAt(i - 2));
    }

    for (int j = 2; j < g + 2; j++) // 6
    {
      Laenge[0][j] = String.valueOf(second.charAt(j - 2));
      Richtung[0][j] = String.valueOf(second.charAt(j - 2));
    }

    for (int i = 2; i < f + 2; i++) // 8
    {
      for (int j = 2; j < g + 2; j++) // 6
      {
        Laenge[i][j] = "";
        Richtung[i][j] = "";
      }
    }

    /* original[i][0] = 0 for 0<=i<=n */
    for (int i = 1; i < f + 2; i++) {
      Laenge[i][1] = "0";
      Richtung[i][1] = "-";
    }

    /* original[0][j] = 0 for 0<=j<=m */

    for (int j = 1; j < g + 2; j++) {
      Laenge[1][j] = "0";
      Richtung[1][j] = "-";
    }

  }

  public static void main(String[] args) {
    LongestCommonSubsequence lcs = new LongestCommonSubsequence();
    lcs.init();

    // second = "aedile"; // aedile ABCABCBA weather human
    // first = "audile"; // audile BACBBA whether chimpanzee

    System.out.println(lcs.generate(new AnimationPropertiesContainer(),
        new Hashtable<String, Object>()));

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    if (primitives.get("string1") != null && primitives.get("string2") != null) {

      StringTwo = (String) primitives.get("string2");
      StringOne = (String) primitives.get("string1");
    }

    if (primitives.get("string1") != null && primitives.get("string2") != null
        && StringTwo.length() < 8 && StringOne.length() < 8) {
      scPr = (SourceCodeProperties) props.getPropertiesByName("scPr");
      // string2 = (String)primitives.get("string2");
      // string1 = (String)primitives.get("string1");
      // matrixPr2 = (MatrixProperties)props.getPropertiesByName("matrixPr2");
      // matrixPr1 = (MatrixProperties)props.getPropertiesByName("matrixPr1");
      matProps2 = (MatrixProperties) props.getPropertiesByName("matrixPr2");
      matProps = (MatrixProperties) props.getPropertiesByName("matrixPr1");

    }

    else {

      StringTwo = "aedile";
      StringOne = "audile";

      matProps = new MatrixProperties();

      matProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.getHSBColor((float) 0.43, (float) 0.76, (float) 0.73));

      matProps2 = new MatrixProperties();

      matProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.getHSBColor((float) 0.83, (float) 0.90, (float) 1.00));

      scPr = new SourceCodeProperties();

      scPr.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      scPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
          Font.PLAIN, 12));
      scPr.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      scPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    }
    textD = new TextProperties();
    textD.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        matProps2.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    textD.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 19));

    textL = new TextProperties();
    textL.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        matProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    textL.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 19));

    this.getLCS(StringOne, StringTwo);
    return lang.toString();
  }

  public String getName() {
    return "Longest common subsequence";
  }

  public String getAlgorithmName() {
    return "Longest common subsequence";
  }

  public String getAnimationAuthor() {
    return "Chahine Abid, Najib Hfaiedh";
  }

  public String getDescription() {
    return "&nbsp&nbsp  Das Longest Common Subsequence (LCS) Problem ist von Bedeutung in"

        + "verschiedenen Bereichen der Informatik."
        + "<br>"

        + "&nbsp&nbsp &nbsp&nbsp  (z.B : - in der Bioinformatik beim Vergleich von DNA-Sequenzen, "
        + "<br>"
        + "&nbsp&nbsp &nbsp&nbsp &nbsp&nbsp &nbsp&nbsp &nbsp  - in Software-Engineering beim Vergleich bzgl. Gemeinsamkeiten zweier Versionen von Programmcode)."

        + "		<br>	"
        + "		<br>"
        + "&nbsp &nbsp	Gegeben sind zwei Zeichenketten X = &#60x1,...,xm&#62  und  Y = &#60y1,...,ym&#62. "
        + "<br>"
        + "&nbsp	Das Longest Common Subsequence Problem besteht darin, die längste Teilfolge zu finden, die sowohl in X	als auch in Y enthalten ist."

        + "			<br><br>"
        + "&nbsp &nbsp  Das heißt, dass der Algorithmus eine Sequenz von maximaler Länge, die in X und Y vorkommt, liefert."
        + "<br>"
        + "&nbsp Elemente der Sequenz müssen nicht konsekutiv , jedoch in korrekter Reihenfolge in X und Y vorkommen.";

  }

  public String getCodeExample() {
    return "public void lcs (String a , String b) { <br>" + "<br>"
        + " int lcs_Length = 0 ; <br>"

        + " int up , left ;<br>"

        + " String direction ;<br>"

        + "<br>" + " for (int i = 2 ; i < a.length() + 2; i++) {<br>"

        + "  for (int j = 2 ; j < b.length() +2 ; j++) { <br>"

        + "   if (a.charAt(i) == b.charAt(j)) {	<br>"

        + "    Length_Matrix[i][j] = Length_Matrix[i-1][j-1] + 1 ;<br>"

        + "    Direction_Matrix [i][j] = 'diag' ;<br>"

        + "    if (Length_Matrix[i][j] > lcs_Length) <br>"

        + "     lcs_Length = Length_Matrix[i][j] ;  <br>"

        + "   } // end if<br>"

        + "   else {	<br>"

        + "    up = Length_Matrix[i - 1][j]; <br>"

        + "    left = Length_Matrix[i][j - 1];<br>"

        + "    if (up >= left) { <br>"

        + "     Length_Matrix[i][j] = up ; <br>"

        + "     Direction_Matrix [i][j] = 'up' ; <br>"

        + "    } // end if <br>"

        + "    else { <br>"

        + "     Length_Matrix[i][j] = left ; "

        + "<br>"

        + "     Direction_Matrix [i][j] = 'left' ; "

        + "<br>"

        + "    } "

        + "<br>"

        + "  }   //  innere Schleife "

        + "<br>"

        + " } // äußere Schleife <br>"

        + "<br>"

        + " String lcs_Value = new String() ;  "

        + "<br>"

        + " int i = first.length() + 1 ;"

        + "<br>"

        + " int j = second.length() + 1 ;  "

        + "<br>"

        + " while (i != 1 && j != 1) { "

        + "<br>"

        + "  if (Direction_Matrix[i][j] == 'diag') {"

        + "<br>"

        + "   lcs_Value = a.charAt(i - 2) + lcs_Value ; "

        + "<br>"

        + "   i = i - 1 ;  j = j - 1;  "

        + "<br>"

        + "  } // end if  "

        + "<br>"

        + "  if (Direction_Matrix[i][j] == 'up') "

        + "<br>"

        + "   i = i - 1 ;  "

        + "<br>"

        + "  if (Direction_Matrix[i][j] == 'left') "

        + "<br>"

        + "   j = j - 1 ;  "

        + "<br>"

        + " } // while"

        + "<br>"

        + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}