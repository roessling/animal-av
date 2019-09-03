package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class JaroWinkler implements Generator {
  private Language              lang;
  private SourceCodeProperties  scPr;
  private ArrayProperties       arrayPr1;
  // private String st1;
  private ArrayMarkerProperties arrayMarker;
  private ArrayProperties       arrayPr2;
  // private String st2;

  private ArrayProperties       arrayProps3;
  // private ArrayMarkerProperties ami;
  private TextProperties        textProps;
  private RectProperties        rectProps;
  private SourceCodeProperties  scProps, scProps1, scProps2;
  private SourceCode            sc, sc2, sc_desc, sc_conc, desc, title;
  boolean                       bool = true;

  int                           comp = 0;

  StringArray                   a1, a2, m, t1, t2;
  Text                          commonPrefNr, matching, comText, commonPref,
      matchingNr, tex, zahl;

  Rect                          matchingZaehler, transpo;
  ArrayMarker                   m1;
  private static String         StringOne, StringTwo;
  private static String         theMatchA = "", theMatchB = "";

  static String                 arr1[], arr2[];

  static int                    mRange;
  boolean                       commonP   = true;
  int                           matches   = 0, commonPrefix = 0,
      transPositions = 0;

  public void init() { // initialize the main elements
    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript("Jaro-Winkler Animation",
        "Abid Chahine _ Hfaiedh Najib", 640, 480);
    // Activate step control
    lang.setStepMode(true);

    textProps = new TextProperties();
    rectProps = new RectProperties();

    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 20));

    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2); //
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN); // filled

    arrayProps3 = new ArrayProperties();

    arrayProps3.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);

    arrayProps3.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayProps3.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray); // fill

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
    title = lang.newSourceCode(new Coordinates(350, 0), "Title", null, scProps);

    title.addCodeLine("JaroWinkler Distance", null, 0, null);

    // set the visual properties for the sourcedescription code
    scProps1 = new SourceCodeProperties();
    scProps1.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    scProps1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    // now, create the source code entity
    desc = lang.newSourceCode(new Coordinates(40, 40), "sourceCodeDesc", null,
        scProps1);

    desc.addCodeLine("Beschreibung des Algorithmus", null, 0, null);
    // set the visual properties for the source code
    scProps2 = new SourceCodeProperties();
    scProps2.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));
    scProps2.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity
    sc_desc = lang.newSourceCode(new Coordinates(30, 85), "sourceCodeDesc",
        null, scProps2);
    // add a code line
    // parameters: code itself; name (can be null); indentation level;
    // display options

    lang.nextStep();
    sc_desc.addCodeLine("Der Algorithmus in Worten", null, 0, null);
    sc_desc.addCodeLine("", null, 0, null);

    sc_desc
        .addCodeLine(
            "Die Jaro-Distanz ist ein verbreitetes Ahnlichkeitsmass im Bereich der Informationsintegration , die auf der Zahl und Anordnung gleicher ",
            null, 1, null);
    sc_desc.addCodeLine("Zeichen in zwei Strings basiert.", null, 0, null);
    sc_desc
        .addCodeLine(
            "Dabei werden sowohl gemeinsame Zeichen, als auch ihre gemeinsame Reihenfolge beruecksichtigt : ",
            null, 1, null);
    sc_desc
        .addCodeLine(
            "Gegeben sind zwei Zeichenketten 'a' und 'b'. Ein Zeichen a [ i ] heisst passend, "
                + "wenn es ein a[ i ] = b[ j ] gibt mit :", null, 1, null);

    sc_desc.addCodeLine(
        " | j - i | <= mRange , wobei mRange := 1/2 ( max ( |a| , |b| )", null,
        0, null);
    sc_desc
        .addCodeLine(
            "mit  |a|  bezeichnet man die Laenge von 'a'  ( fuer das wort 'b' ist  |b|  analog definiert ) .",
            null, 9, null);

    sc_desc.addCodeLine("", null, 5, null);
    lang.nextStep("Einleitung");
    sc_desc.addCodeLine(" -  'm'  ist die Anzahl der Zeichen, zu denen es "
        + "in der anderen Zeichenkette eine Entsprechung gibt.", null, 1, null);

    sc_desc.addCodeLine(
        " -  'matchA'  bezeichnet die Folge der passenden Zeichen aus 'a' , "
            + " wobei jedes Zeichen aus 'b' nur einmal als Entsprechung  ",
        null, 1, null);

    sc_desc
        .addCodeLine(
            "vorkommen darf. Die Folge der passenden Zeichen in 'b' ist analog als 'matchB' definiert.",
            null, 0, null);

    sc_desc
        .addCodeLine(
            "'matchA' und 'matchB' enthalten somit immer gleich viele Zeichen : |matchA| = |matchB|",
            null, 0, null);

    sc_desc.addCodeLine(
        " -  't'  bezeichnet die Anzahl der Stellen, an denen sich 'matchA'"
            + " und 'matchB' unterscheiden, die Zeichen also in einer ", null,
        1, null);
    sc_desc.addCodeLine(
        "anderen Reihenfolge auftreten (sogenannte Transpositionen).", null, 0,
        null);

    sc_desc.addCodeLine("", null, 5, null);
    lang.nextStep();

    sc_desc
        .addCodeLine(
            "Die Ahnlichkeit der Zeichenketten 'a' und 'b' laesst sich folgendermassen berechnen:",
            null, 2, null);

    sc_desc
        .addCodeLine(
            "      Jaro distance : jd =  1/3 ( m / |a|  +  m / |b|  +  (m - t / 2) / m) ",
            null, 6, null);

    sc_desc.addCodeLine(
        "Winkler distance : jw = jd + p * l * (1 - jd)     wobei :", null, 6,
        null);

    sc_desc
        .addCodeLine(
            " -  'p'  ist die Laenge des gemeinsamen Praefix bis zu einer festgelegten Laenge (maximum 4)",
            null, 1, null);

    sc_desc
        .addCodeLine(
            " -  'l'  ist oft 0.1 , kann aber beliebig gesetzt werden. In dem Fall l = 0 ergibt sich wiederum der Jaro Abstand.",
            null, 1, null);

  }

  public void showConclusionSlide(String first, String second, String jd,
      String jw) {

    scProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 20));

    sc_conc = lang.newSourceCode(new Coordinates(15, 85), "sourceCodeDesc",
        null, scProps2);

    sc_conc.addCodeLine("Wie gross ist die JaroWinkler-Ahnlichkeit von '"
        + first + "' und '" + second + "' ? ", null, 7, null);

    sc_conc.addCodeLine("", null, 0, null);
    lang.nextStep("Fazit");
    sc_conc.addCodeLine("'match_" + first
        + "'  bezeichnet die Folge der passenden Zeichen aus '" + first + "', "
        + " wobei jedes Zeichen aus ", null, 2, null);

    sc_conc.addCodeLine("'" + second
        + "' nur einmal als Entsprechung vorkommen darf. ", null, 0, null);
    sc_conc.addCodeLine("Die Folge der passenden Zeichen in '" + second
        + "' ist analog als 'match_" + second + "' definiert.", null, 2, null);

    sc_conc.addCodeLine("", null, 5, null);

    lang.nextStep();

    sc_conc
        .addCodeLine("'match_" + first + "'  =  " + theMatchA, null, 0, null);
    sc_conc.addCodeLine("'match_" + second + "'  =  " + theMatchB, null, 0,
        null);

    sc_conc.addCodeLine("", null, 0, null);
    lang.nextStep();

    sc_conc.addCodeLine("Die Jaro-Ahnlichkeit von '" + first + "' und '"
        + second + "' ist :  " + jd + " % ", null, 2, null);

    sc_conc
        .addCodeLine(
            "Setzt man den standardmaessigen Wert 0.1, dann betraegt die Winkler-Ahnlichkeit von ",
            null, 2, null);

    sc_conc.addCodeLine("'" + first + "' und '" + second + "'  :  " + jw
        + " % ", null, 0, null);
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
            " Die Zeitkomplexitaet des JaroWinkler Algorithmus ist O (Laenge des ersten Wortes.Laenge des zweiten Wortes). ",
            null, 0, null);

    sc_conc.addCodeLine("Beim Vergleich von '" + first + "' und '" + second
        + "' ist die Zeitkomlexitaet des JaroWinkler Algorithmus O ( " + fLen
        + " * " + sLen + " ) = O (" + mulstr + ") . ", null, 0, null);

  }

  public void showSourceCode() {

    // create the source code entity
    sc = lang.newSourceCode(new Coordinates(10, 98), "sourceCode", null, scPr);

    sc.addCodeLine("public void Jaro_Wink (String a , String b) {", null, 0,
        null);
    sc.addCodeLine("int k = Math.min(a.length(), b.length()) ;", null, 1, null);
    sc.addCodeLine("for (int j = 0; j < Math.min(4,k)) ; j++) { ", null, 1,
        null);
    sc.addCodeLine("if (a.charAt(j) == b.charAt(j))  ", null, 2, null);
    sc.addCodeLine("commonPrefix++; 	", null, 4, null);
    sc.addCodeLine("else j = 5 ;    ", null, 3, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine(
        "int m = 0 ;  int mRange = Math.max(a.length(), b.length())/ 2 - 1 ;",
        null, 1, null);
    sc.addCodeLine(
        "// Vergleich wird zweimal ausgefuehrt : a mit b vergleichen (und ungekehrt) ",
        null, 1, null);
    sc.addCodeLine("for (int i = 0; i < a.length(); i++)  {", null, 1, null);
    sc.addCodeLine("int c = 0 ; ", null, 2, null);
    sc.addCodeLine("while (c <= mRange && i >= 0 && c <= i) {", null, 2, null);
    sc.addCodeLine("if (a.charAt(i) == b.charAt(i - c)) {", null, 3, null);
    sc.addCodeLine("m++ ;        MatchB = MatchB + a.charAt(i) ;", null, 4,
        null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("c++;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("c = 1;", null, 2, null);
    sc.addCodeLine("while (c <= mRange && i < b.length && c + i < b.length) {",
        null, 2, null);
    sc.addCodeLine("if ((a.charAt(i) == b.charAt(i + c))) {", null, 3, null);
    sc.addCodeLine("m++ ;        MatchB = MatchB + a.charAt(i) ;", null, 4,
        null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("c++;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("MatchA = MatchB", null, 0, null);

  }

  public void showSourceCode2() {

    sc2 = lang.newSourceCode(new Offset(23, -10, "sourceCode",
        AnimalScript.DIRECTION_NE), "sourceCode2", null, scPr);
    sc2.addCodeLine("int t = 0 ;      double JaroD , WinklerD , mt ;   ", null,
        1, null);
    sc2.addCodeLine("for (int i = 0; i < MatchA.length(); i++) {", null, 0,
        null);
    sc2.addCodeLine("int c = 0 ;  int t = 0 ; ", null, 1, null);
    sc2.addCodeLine("while (c <= mRange && i >= 0 && c <= i) { ", null, 1, null);
    sc2.addCodeLine("if (MatchA.charAt(i) == MatchB.charAt(i-c)) && c > 0) {",
        null, 2, null);
    sc2.addCodeLine("t++; ", null, 3, null);
    sc2.addCodeLine("}", null, 2, null); // if
    sc2.addCodeLine("c++;", null, 2, null);
    sc2.addCodeLine("} ", null, 1, null); // while
    sc2.addCodeLine("c = 1;", null, 1, null);
    sc2.addCodeLine("while (c <= mRange && i < MatchB.length()", null, 1, null);
    sc2.addCodeLine("&& (c + i) < MatchB.length()) { ", null, 1, null);
    sc2.addCodeLine("if (MatchA.charAt(i) == MatchB.charAt(i+c)) && c > 0) {",
        null, 2, null);
    sc2.addCodeLine("t++ ;   ", null, 3, null);
    sc2.addCodeLine("}", null, 2, null); // if
    sc2.addCodeLine("c++ ;  ", null, 2, null);
    sc2.addCodeLine("} ", null, 1, null); // while
    sc2.addCodeLine("} ", null, 0, null); // for

  }

  public static void main(String[] args) {
    JaroWinkler jw = new JaroWinkler();
    jw.init();

    System.out.println(jw.generate(new AnimationPropertiesContainer(),
        new Hashtable<String, Object>()));

  }

  public void getMatch(String[] st1, String[] st2, String[] matches_arr) {

    Timing defaultTiming = new TicksTiming(15);
    theMatchB = "";
    int x;
    matches = 0;
    comp++;

    a1 = lang
        .newStringArray(new Coordinates(25, 40), st1, "a1", null, arrayPr1);

    String s1 = a1.getName();

    m = lang.newStringArray(new Offset(0, 0, s1, AnimalScript.DIRECTION_SW),
        matches_arr, "m", null, arrayProps3);
    a2 = lang.newStringArray(new Offset(0, 0, "m", AnimalScript.DIRECTION_SW),
        st2, "array2", null, arrayPr2);

    matching = lang.newText(new Offset(25, 37, s1, AnimalScript.DIRECTION_NE),
        "matching Characters = ", "matchingText", null, textProps);

    String s2 = matching.getName();
    matchingNr = lang.newText(new Offset(0, 0, s2, AnimalScript.DIRECTION_NE),
        "0", "matchingNr", null, textProps);

    matchingZaehler = lang.newRect(new Offset(10, 0, matchingNr.getName(),
        AnimalScript.DIRECTION_NE), new Offset(10, 0, matchingNr.getName(),
        AnimalScript.DIRECTION_SE), "matchingZaehler", null, rectProps);

    if (bool) {
      lang.nextStep("Initialisierung");
      showSourceCode();
      sc.highlight(0);
    }
    ;

    lang.nextStep();
    sc.unhighlight(0);

    m1 = lang.newArrayMarker(a1, 0, "i", null, arrayMarker);

    if (commonP)
      getCommonPrefix(st1, st2);

    sc.unhighlight(25);
    for (int i = 0; i < st1.length && st1[i] != "  "; i++) {

      boolean t = true;
      sc.toggleHighlight(8, 9);

      m1.move(i, null, defaultTiming);

      int counter = 0;
      // lang.nextStep();
      if (bool)
        lang.nextStep("Iteration Nr " + String.valueOf(i + 1)
            + " im Vergleich von '" + StringOne + "' mit '" + StringTwo + "'");
      //
      else

        lang.nextStep("Iteration Nr " + String.valueOf(i + 1)
            + " im Vergleich von '" + StringTwo + "' mit '" + StringOne + "'");

      // lang.nextStep("Iteration Nr "+String.valueOf(i+1)+" im Vergleich von "
      // +String.valueOf(comp));

      sc.unhighlight(9);

      // Look backward
      while (t && counter <= mRange && i >= 0 && counter <= i) {
        sc.highlight(11);
        x = i - counter;
        a2.highlightCell(x, null, defaultTiming);
        lang.nextStep();
        sc.unhighlight(11);

        if (st1[i].equals(st2[i - counter])) {
          sc.highlight(12);
          lang.nextStep();
          sc.toggleHighlight(12, 13);

          matches++;
          matchingZaehler.moveBy("translate #2", 4, 0, null, null);
          matchingNr.setText(String.valueOf(matches), null, defaultTiming);

          m.put(i, String.valueOf(i - counter), null, defaultTiming);
          lang.nextStep();

          theMatchB = theMatchB + st2[i - counter];

          a2.put(i - counter, "  ", null, defaultTiming);
          st2[i - counter] = "  ";

          sc.unhighlight(13);
          t = false;

        }
        counter++;
        a2.unhighlightCell(x, null, defaultTiming);

      }
      // Look forward
      counter = 1;

      while (t && counter <= mRange && i < st2.length
          && counter + i < st2.length && st2[i + counter] != "_") {
        sc.highlight(18);

        x = i + counter;
        a2.highlightCell(x, null, defaultTiming);
        lang.nextStep();
        sc.unhighlight(18);

        if (st1[i].equals(st2[i + counter])) {
          sc.highlight(19);
          lang.nextStep();
          matches++;
          matchingZaehler.moveBy("translate #2", 4, 0, null, null);
          matchingNr.setText(String.valueOf(matches), null, defaultTiming);

          m.put(i, String.valueOf(x), null, defaultTiming);

          sc.toggleHighlight(19, 20);

          lang.nextStep();

          theMatchB = theMatchB + st1[i];

          a2.put(i + counter, "  ", null, defaultTiming);
          st2[i + counter] = "  ";

          t = false;

          sc.unhighlight(20);

        }
        counter++;
        a2.unhighlightCell(x, null, defaultTiming);

      }
    }
    m1.hide();

    bool = !bool;
  }

  public void getCommonPrefix(String[] st1, String[] st2) {
    Timing defaultTiming = new TicksTiming(15);
    commonPrefix = 0;

    commonPref = lang.newText(new Offset(5, -20, "matchingText",
        AnimalScript.DIRECTION_NW), "common Prefix = ", "commonPrefix", null,
        textProps);

    commonPrefNr = lang.newText(new Offset(0, 0, "commonPrefix",
        AnimalScript.DIRECTION_NE), "0", "commonPref", null, textProps);

    // common prefix
    for (int j = 0; j < Math.min(4,
        Math.min(StringOne.length(), StringTwo.length())); j++) {
      sc.highlight(2);
      m1.move(j, null, defaultTiming);
      a2.highlightCell(j, null, defaultTiming);
      lang.nextStep();
      sc.unhighlight(2);
      if (st1[j].equals(st2[j])) {
        sc.highlight(3);

        lang.nextStep();
        sc.toggleHighlight(3, 4);
        commonPrefix++;
        commonPrefNr.setText(String.valueOf(commonPrefix), null, defaultTiming);
        lang.nextStep();
        sc.unhighlight(4);
      } else {
        sc.highlight(5);
        lang.nextStep();
        a2.unhighlightCell(j, null, defaultTiming);
        j = Math.max(StringOne.length(), StringTwo.length()) + 2;

        sc.unhighlight(5);
      }
      a2.unhighlightCell(j, null, defaultTiming);
    }

    sc.highlight(8);
    lang.nextStep();

  }

  public void getMissMatch(String[] st1, String[] st2) {
    transPositions = 0;
    int x;
    Timing defaultTiming = new TicksTiming(15);

    t1 = lang.newStringArray(new Offset(300, -22, "matchingZaehler",
        AnimalScript.DIRECTION_NE), st1, "t1", null, arrayPr1);

    t2 = lang.newStringArray(new Offset(0, 0, "t1", AnimalScript.DIRECTION_SW),
        st2, "t2", null, arrayPr2);

    tex = lang.newText(new Offset(22, 10, "t1", AnimalScript.DIRECTION_NE), ""
        + "TransPositions = ", "transpoText", null, textProps);
    zahl = lang.newText(new Offset(0, 0, "transpoText",
        AnimalScript.DIRECTION_NE), "0", "Transpositionen", null, textProps);

    transpo = lang.newRect(new Offset(10, 0, "Transpositionen",
        AnimalScript.DIRECTION_NE), new Offset(10, 0, "Transpositionen",
        AnimalScript.DIRECTION_SE), "transpoZaehler", null, rectProps);

    lang.nextStep();
    ArrayMarker n1 = lang.newArrayMarker(t1, 0, "i", null, arrayMarker);

    for (int i = 0; i < st1.length; i++) { // Look Backward
      sc2.highlight(1);
      n1.move(i, null, defaultTiming);

      lang.nextStep("Iteration Nr " + String.valueOf(i + 1)
          + " im Vergleich von '" + theMatchA + "' mit '" + theMatchB + "'");

      sc2.unhighlight(1);
      int counter = 0;

      // Look backward
      while (counter <= mRange && i >= 0 && counter <= i) {
        sc2.highlight(3);
        x = i - counter;
        t2.highlightCell(x, null, defaultTiming);
        lang.nextStep();

        sc2.unhighlight(3);

        if (st1[i].equals(st2[i - counter]) && counter > 0) {
          sc2.highlight(4);

          lang.nextStep();

          sc2.toggleHighlight(4, 5);

          transPositions++;
          transpo.moveBy("translate #2", 4, 0, null, null);
          zahl.setText(String.valueOf(transPositions), null, defaultTiming);
          lang.nextStep();
          sc2.unhighlight(5);
        }
        t2.unhighlightCell(x, null, defaultTiming);
        counter++;
      }
      // Look forward
      counter = 1;
      while (counter <= mRange && i < st2.length && (counter + i) < st2.length) {
        sc2.highlight(10);
        sc2.highlight(11);
        t2.highlightCell(i + counter, null, defaultTiming);
        lang.nextStep();
        sc2.unhighlight(11);
        sc2.unhighlight(10);
        if (st1[i].equals(st2[i + counter]) && counter > 0) {
          sc2.highlight(12);
          lang.nextStep();

          sc2.toggleHighlight(12, 13);

          transPositions++;
          transpo.moveBy("translate #2", 4, 0, null, null);
          zahl.setText(String.valueOf(transPositions), null, defaultTiming);
          lang.nextStep();
          sc2.unhighlight(13);
        }
        t2.unhighlightCell(i + counter, null, defaultTiming);
        counter++;
      }

    }
    n1.hide();

  }

  public void getSimilarity(String first, String second) {

    showSourceCodedescription();

    lang.nextStep();
    sc_desc.hide();
    desc.hide();

    mRange = Math.max(first.length(), second.length()) / 2 - 1;

    fillArrays(first, second);

    // sc.highlight(0);

    getMatch(arr1, arr2, ComparisonArray(first, second));

    theMatchA = theMatchB;

    a1.hide();
    a2.hide();
    m.hide();
    matchingZaehler.hide();
    matchingNr.hide();
    commonPrefNr.hide();
    commonPref.hide();
    matching.hide();
    commonP = false;
    sc.highlight(8);
    sc.highlight(25);
    lang.nextStep();
    fillArrays(second, first);
    commonPref.show();
    commonPrefNr.show();
    getMatch(arr1, arr2, ComparisonArray(second, first));

    fillArrays(theMatchA, theMatchB);
    showSourceCode2();

    if (matches > 0)
      getMissMatch(arr1, arr2);

    Timing defaultTiming = new TicksTiming(15);
    sc2.addCodeLine("// m := matching Characters = " + String.valueOf(matches)
        + " ,  cP :=  common Prefix = " + String.valueOf(commonPrefix), null,
        0, null);
    sc2.addCodeLine(
        "// t := Transpositions = " + String.valueOf(transPositions), null, 0,
        null);

    sc2.addCodeLine(
        "// JaroD := Jaro Distance , WinklerD := Winkler Distance ", null, 0,
        null);

    sc2.addCodeLine("mt = (m - t/2)/ m) ", null, 0, null);
    sc2.addCodeLine("JaroD = 1/3 ( m / a.length() + m / b.length() + mt ) ; ",
        null, 0, null);
    sc2.addCodeLine("WinklerD = JaroD + cP * 0.1 * (1 - JaroD) ; ", null, 0,
        null);
    sc2.addCodeLine("", null, 0, null);
    sc2.addCodeLine("}", null, 0, null);

    lang.nextStep();

    sc2.highlight(20);

    Text JaroText = lang.newText(new Offset(50, -10, "matchingZaehler",
        AnimalScript.DIRECTION_NE), "Jaro distance = ", "JaroText", null,
        textProps);

    Text JaroDistance = lang.newText(new Offset(0, 0, "JaroText",
        AnimalScript.DIRECTION_NE), "0", "JaroNr", null, textProps);

    Text WinklerText = lang.newText(new Offset(-5, 5, "JaroText",
        AnimalScript.DIRECTION_SW), "Winkler distance = ", "WinklerText", null,
        textProps);
    Text WinklerDistance = lang.newText(new Offset(0, 0, "WinklerText",
        AnimalScript.DIRECTION_NE), "0", "WinklerNr", null, textProps);
    lang.nextStep();

    double jw = 0.0;
    int l1 = first.length();
    int l2 = second.length();
    double f = 0.33333;

    double mt = (double) ((matches - transPositions * 0.5) / matches);

    double jd = f
        * ((double) matches / l1 + (double) matches / l2 + (double) mt);

    jd = (double) Math.round(jd * 1000) / 1000;
    double jd2 = (double) Math.round(jd * 100000) / 1000;
    String jd_str = String.valueOf(jd2);
    sc2.toggleHighlight(20, 22);
    JaroDistance.setText(String.valueOf(jd), null, defaultTiming);
    lang.nextStep();

    jw = jd + commonPrefix * (0.1 * (1.0 - jd));
    jw = (double) Math.round(jw * 1000) / 1000;
    double jw2 = (double) Math.round(jw * 100000) / 1000;
    sc2.toggleHighlight(22, 23);
    String jw_str = String.valueOf(jw2);
    WinklerDistance.setText(String.valueOf(jw), null, defaultTiming);
    lang.nextStep();
    sc2.unhighlight(23);

    lang.nextStep();
    sc.hide();
    sc2.hide();

    a1.hide();
    a2.hide();
    m.hide();

    if (matches > 0) {
      t1.hide();
      t2.hide();
      WinklerText.hide();
      WinklerDistance.hide();
      JaroText.hide();
      JaroDistance.hide();
      transpo.hide();
      tex.hide();
      zahl.hide();
    }

    matching.hide();
    matchingNr.hide();
    commonPref.hide();
    commonPrefNr.hide();

    matchingZaehler.hide();

    showConclusionSlide(first, second, jd_str, jw_str);

  }

  public void fillArrays(String firstString, String secondString) {
    String first = firstString.toUpperCase(), second = secondString
        .toUpperCase();
    int f, g;
    f = first.length();
    g = second.length();

    int max = Math.max(f, g);
    arr1 = new String[max];
    arr2 = new String[max];

    String def = "  ";

    for (int i = 0; i < f; i++) {
      arr1[i] = String.valueOf(first.charAt(i));
    }

    for (int j = 0; j < g; j++) {
      arr2[j] = String.valueOf(second.charAt(j));
    }

    if (g < f)
      for (int i = g; i < f; i++) {
        arr2[i] = def;
      }
    else
      for (int i = f; i < g; i++) {
        arr1[i] = def;
      }
  }

  public String[] ComparisonArray(String first, String second) {
    int f, g;
    f = first.length();
    g = second.length();

    int max = Math.max(f, g);
    String[] matches_arr = new String[max];

    for (int j = 0; j < max; j++) {
      matches_arr[j] = "  ";
    }
    return matches_arr;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    if (primitives.get("st1") != null && primitives.get("st1") != null) {

      StringOne = (String) primitives.get("st1");
      StringTwo = (String) primitives.get("st2");
      arrayPr1 = (ArrayProperties) props.getPropertiesByName("arrayPr1");
      arrayMarker = (ArrayMarkerProperties) props
          .getPropertiesByName("arrayMarker");
      arrayPr2 = (ArrayProperties) props.getPropertiesByName("arrayPr2");
      scPr = (SourceCodeProperties) props.getPropertiesByName("scPr");
    } else {

      StringOne = "Berlin";
      StringTwo = "Ballerina";

      // create array properties (first array)with default values
      arrayPr1 = new ArrayProperties();
      // Redefine properties:
      arrayPr1.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE); // color
                                                                               // blue
      arrayPr1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
      arrayPr1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW); // filled
      // with
      // yellow
      arrayPr1.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.WHITE); //

      // create array properties (second array)with default values
      arrayPr2 = new ArrayProperties();
      // Redefine properties:
      arrayPr2.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED); // color
                                                                              // red

      arrayPr2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
      arrayPr2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN); // filled
      // with
      // green
      arrayPr2.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.WHITE); //

      // marker
      arrayMarker = new ArrayMarkerProperties();
      arrayMarker.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
      arrayMarker.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

      // set the visual properties for the source code
      scPr = new SourceCodeProperties();

      scPr.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      scPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
          Font.PLAIN, 11));
      scPr.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      scPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    }

    this.getSimilarity(StringOne, StringTwo);
    return lang.toString();
  }

  public String getName() {
    return "JaroWinkler";
  }

  public String getAlgorithmName() {
    return "JaroWinkler";
  }

  public String getAnimationAuthor() {
    return "Chahine Abid, Najib Hfaiedh ";
  }

  public String getDescription() {
    return "&nbsp&nbsp  Die Jaro-Distanz ist ein verbreitetes Ähnlichkeitsmaß im Bereich der Informationsintegration ,"
        + "\n"
        + "die auf der Zahl und Anordnung gleicher Zeichen in zwei Strings basiert."
        + "\n"
        + "		<br>"
        + "\n"
        + " &nbsp Dabei werden sowohl gemeinsame Zeichen, als auch ihre gemeinsame Reihenfolge "
        + "\n"
        + " berücksichtigt : <br>"
        + "\n"
        + "\n"
        + "&nbsp Gegeben sind zwei Zeichenketten 'a' und 'b'. Ein Zeichen a [i] &nbsp"
        + "\n"
        + " heißt passend, wenn es ein a[i] = b[j] gibt mit :<br>"
        + "\n"
        + " |j - i| &#8804 mRange , &nbsp wobei mRange :=  max ( &nbsp|a| &nbsp, &nbsp|b| &nbsp) &nbsp&nbsp"
        + "\n"
        + " mit |a| bezeichnet man die Länge von 'a' "
        + "\n"
        + " ( für das wort 'b' ist  |b|  analog definiert ). <br>"
        + "\n"
        + "  <br>"
        + "\n"
        + " - 'm' ist die Anzahl der Zeichen, zu denen es in der anderen Zeichenkette eine "
        + "\n"
        + " Entsprechung gibt. <br>"
        + "\n"
        + " - 't' bezeichnet die Anzahl der Stellen, "
        + "\n"
        + " an denen sich die passende Zeichen von 'a' und 'b' unterscheiden, die Zeichen also"
        + "\n"
        + " in einer anderen Reihenfolge auftreten (sogenannte Transpositionen). <br>"
        + "\n"
        + " <br>"
        + "\n"
        + " &nbsp &nbsp Die Ähnlichkeit der Zeichenketten 'a' und 'b' lässt sich"
        + "\n"
        + " folgendermaßen  berechnen:  <br>"
        + "\n"
        + "\n"
        + " &nbsp Jaro distance : jd = 1/3 ( m / |a| + m / |b| + (m - t / 2) / m) <br>"
        + "\n"
        + " &nbsp Winkler distance : jw = jd + p * l * (1 - jd) &nbsp wobei : <br>"
        + "\n"
        + "  - 'p' ist die Länge des gemeinsamen Präfix bis zu einer "
        + "\n"
        + " festgelegten Länge  (maximum 4)  <br>"
        + "\n"
        + " - 'l' ist oft 0.1 , kann aber beliebig gesetzt werden.<br>"
        + "\n"
        + " &nbsp In dem Fall l = 0 ergibt sich wiederum der Jaro Abstand. <br>"
        + "\n" + " " + "\n";
  }

  public String getCodeExample() {
    return " "
        + "\n"
        + "public void Jaro_Wink (String a , String b) { <br>"

        + "&nbsp int k = Math.min(a.length(), b.length()) ;<br>"

        + "&nbsp for (int j = 0; j &#60 Math.min(4,k)) ; j++) { <br>"

        + "&nbsp&nbsp if (a.charAt(j) == b.charAt(j))   <br>"

        + "&nbsp&nbsp&nbsp&nbsp  commonPrefix++; 	\"<br>"

        + "&nbsp&nbsp&nbsp else j = 5 ;<br>"

        + "&nbsp } // end for <br> "

        + " <br>"

        + "&nbsp int matches = 0 ;  int mRange = Math.max(a.length(), b.length())/ 2 - 1 ;<br>"

        + "&nbsp // Vergleich wird zweimal ausgeführt : a mit b vergleichen (und ungekehrt) \"<br>"

        + "&nbsp for (int i = 0; i &#60 a.length(); i++)  { <br>"

        + "&nbsp&nbsp int count= 0 ;  <br>"

        + "&nbsp&nbsp while (count &#8804 mRange && i &#8805 0 && count &#8804 i) {<br>"

        + "&nbsp&nbsp&nbsp if (a.charAt(i) == b.charAt(i - count)) { <br>"

        + "&nbsp&nbsp&nbsp&nbsp matches++ ;        MatchB = MatchB + a.charAt(i) ;<br>"

        + "&nbsp&nbsp&nbsp } // end if <br>"

        + "&nbsp&nbsp&nbsp count++; <br>"

        + "&nbsp&nbsp } // end while <br>"

        + "&nbsp&nbsp count= 1; <br>"

        + "&nbsp&nbsp while (count &#8804 mRange && i &#60 b.length && count + i &#60 b.length) {<br>"

        + "&nbsp&nbsp&nbsp if ((a.charAt(i) == b.charAt(i + count))) { <br>"

        + "&nbsp&nbsp&nbsp&nbsp matches ++ ;        MatchB = MatchB + a.charAt(i) ;<br>"

        + "&nbsp&nbsp&nbsp } // end if <br>"

        + "&nbsp&nbsp  count++; <br>"

        + "&nbsp&nbsp } // end while<br>"
        + "\n"
        + "&nbsp } // end for <br>"

        + "&nbsp MatchA = MatchB ; <br>"

        + " <br>"

        + "&nbsp int transpositions = 0 ; <br>"

        + "&nbsp for (int i = 0 ; i &#60 MatchA.length() ; i++) <br>"

        + "&nbsp&nbsp int count = 0  ;<br>"

        + "&nbsp&nbsp while (count &#8804 mRange && i &#8805 0 && count &#8804 i) <br>"

        + "&nbsp&nbsp&nbsp if (MatchA.charAt(i) == MatchB.charAt(i-count)) && count &#62 0) <br>"

        + "&nbsp&nbsp&nbsp&nbsp transpositions++; <br>"

        + "&nbsp&nbsp&nbsp } // end if<br>"

        + "&nbsp&nbsp&nbsp count++;<br>"

        + "&nbsp&nbsp }  // end while<br>"

        + "&nbsp&nbsp count= 1; <br>"

        + "&nbsp&nbsp while (count &#8804 mRange && i &#60 MatchB.length() && (count + i) &#60 MatchB.length()) {  <br>"

        + "&nbsp&nbsp&nbsp if (MatchA.charAt(i) == MatchB.charAt(i+count)) && count &#62 0) { <br>"

        + "&nbsp&nbsp&nbsp&nbsp transpositions++ ;    <br>"

        + "&nbsp&nbsp&nbsp } // end if <br>"

        + "&nbsp&nbsp&nbsp count++ ;   <br>"

        + "&nbsp&nbsp }  // end while <br>"

        + "&nbsp }  // end for <br>"

        + "double JaroD , WinklerD , mt ;<br>"

        + "mt = (matches - transpositions/2) / matches) ;<br>"

        + "JaroD = 1/3 ( matches / a.length() + matches  / b.length() + mt ) ; <br>"

        + "WinklerD = JaroD + commonPrefix * 0.1 * (1 - JaroD) ; <br>"
        + "}   <br>" + "\n";
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