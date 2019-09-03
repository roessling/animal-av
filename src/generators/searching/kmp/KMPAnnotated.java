package generators.searching.kmp;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * Knuth-Morris-Pratt Algorithm
 * 
 * @author David Sharma, Bj&ouml;rn Pantel
 * 
 */
public class KMPAnnotated extends AnnotatedAlgorithm implements
    ValidatingGenerator {

  private String[]             text;
  private String[]             pattern;
  private int[]                prefixTable;
  private int[]                legend = new int[3];
  private Language             lang;

  private ArrayProperties      arrayProps;
  private IntArray             prefixTbl, legendArray;
  private StringArray          textArray, patternArray;
  private TextProperties       headerProps, textProps, warningProps, iVarProps,
      jVarProps;
  private Text                 header, borderLength, patternHeader,
      prefixHeader, notEqual, jumpTo, foundString, iVar, jVar, hint1, hint2; // ,
                                                                             // textHeader
  private Text                 introHeader, intro1a, intro1b, intro1c, intro1d,
      intro1e, intro1f, intro1g, intro1h;                                   // ,
                                                                             // intro1i,
                                                                             // intro1j,
                                                                             // intro1k;
  private SourceCode           sc;
  private SourceCodeProperties scProps;

  private String               comp   = "Compares";
  private String               assi   = "Assignments";

  @Override
  public void init() {
    lang = new AnimalScript("Knuth-Morris-Pratt Animation",
        "Björn Pantel und David Sharma", 640, 480);

    lang.setStepMode(true);

    super.init();

    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(700, 100), "sumupCode",
        null, props);

    vars.declare("int", comp, "0");
    vars.setGlobal(comp);
    vars.declare("int", assi, "0");
    vars.setGlobal(assi);
    // parsing anwerfen
    // parse();

    initProps();

  }

  public void initProps() {

    headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 24));

    /*
     * arrayProps = new ArrayProperties();
     * arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
     * Color.BLUE); arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
     * true); arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
     * arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
     * Color.ORANGE);
     * arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
     * Color.RED);
     * 
     * 
     * 
     * textProps = new TextProperties();
     * textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
     * "SansSerif", Font.PLAIN, 18));
     * 
     * warningProps = new TextProperties();
     * warningProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
     * "SansSerif", Font.PLAIN, 18));
     * warningProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
     * 
     * iVarProps = new TextProperties();
     * iVarProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
     * "SansSerif", Font.PLAIN, 18));
     * iVarProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     * 
     * jVarProps = new TextProperties();
     * jVarProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
     * "SansSerif", Font.PLAIN, 18));
     * jVarProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
     */

  }

  public void showIntro() {
    introHeader = lang.newText(new Coordinates(20, 30),
        "Knuth-Morris-Pratt Algorithmus", "header", null, headerProps);

    intro1a = lang
        .newText(
            new Offset(20, 50, "header", AnimalScript.DIRECTION_SW),
            "Der Knuth-Morris-Pratt Algorithmus ist ein String-Matching Algorithmus. ",
            "intro1a", null, textProps);

    intro1b = lang
        .newText(
            new Offset(0, 20, "intro1a", AnimalScript.DIRECTION_SW),
            "Ein Vorteil zur naiven Suche ist, dass wiederholende Vergleiche im Algorithmus vermieden werden.",
            "intro1b", null, textProps);

    intro1c = lang
        .newText(
            new Offset(0, 20, "intro1b", AnimalScript.DIRECTION_SW),
            "Der Algorithmus teilt sich in zwei Phasen auf. Die erste Phase nennt man 'Praefix-Analyse', die zweite Phase ist der eigentliche Sting-Matching Algorithmus .",
            "intro1c", null, textProps);

    intro1d = lang
        .newText(
            new Offset(0, 20, "intro1c", AnimalScript.DIRECTION_SW),
            "In der Praefix-Analyse wird eine Praefix Tabelle erstellt, die fuer Teilworte des gesuchten Musters die Laenge des Randes enthaelt.",
            "intro1d", null, textProps);

    intro1e = lang
        .newText(
            new Offset(0, 20, "intro1d", AnimalScript.DIRECTION_SW),
            "Der Rand eines Wortes w ist ein Teilwort w', das gleichzeitig (echtes) Praefix und (echtes) Suffix von w ist.",
            "intro1e", null, textProps);

    intro1f = lang
        .newText(
            new Offset(0, 20, "intro1e", AnimalScript.DIRECTION_SW),
            "In der zweiten Phase wird dann in einem Text das gegebene Muster gesucht.",
            "intro1f", null, textProps);

    intro1g = lang
        .newText(
            new Offset(0, 20, "intro1f", AnimalScript.DIRECTION_SW),
            "Wenn der Vergleich zweier Zeichen scheitert, kommt die Praefix-Tabelle ins Spiel. Mit den gespeicherten Laengen des Randes kann man die Schiebedistanz berechnen.",
            "intro1g", null, textProps);

    intro1h = lang
        .newText(
            new Offset(0, 20, "intro1g", AnimalScript.DIRECTION_SW),
            "Die Schiebedistanz entspricht einer Verschiebung des Musters im Text-Array. Von da aus wird dann weitergemacht.",
            "intro1h", null, textProps);

    lang.nextStep();
    introHeader.hide();
    intro1a.hide();
    intro1b.hide();
    intro1c.hide();
    intro1d.hide();
    intro1e.hide();
    intro1f.hide();
    intro1g.hide();
    intro1h.hide();

  }

  public void initPhase1() {
    header = lang.newText(new Coordinates(10, 10),
        "Phase 1: Praefix Analyse (Erstellung der Praefix-Tabelle)", "header",
        null, headerProps);
    lang.nextStep();

    patternHeader = lang.newText(new Coordinates(50, 50), "Muster:", "pHeader",
        null, textProps);

    patternArray = lang.newStringArray(new Offset(75, 0, "pHeader",
        AnimalScript.DIRECTION_NE), pattern, "pattern", null, arrayProps);

    prefixHeader = lang.newText(new Offset(0, 15, "pHeader",
        AnimalScript.DIRECTION_SW), "Prefixtabelle:", "sHeader", null,
        textProps);

    prefixTbl = lang.newIntArray(new Offset(0, 15, "pattern",
        AnimalScript.DIRECTION_SW), prefixTable, "shift", null, arrayProps);

    borderLength = lang.newText(new Offset(0, 10, "shift",
        AnimalScript.DIRECTION_SW), "", "border", null, textProps);
    borderLength.hide();

    iVar = lang.newText(
        new Offset(50, 0, "pattern", AnimalScript.DIRECTION_NE), "", "iVar",
        null, iVarProps);

    jVar = lang.newText(new Offset(50, 0, "iVar", AnimalScript.DIRECTION_NE),
        "", "jVar", null, jVarProps);
  }

  public void initPhase2() {
    header = lang.newText(new Coordinates(10, 10),
        "Phase 2: K-M-P Algorithmus", "header", null, headerProps);

    // textHeader =
    lang.newText(new Coordinates(50, 100), "Text:", "tHeader", null, textProps);

    textArray = lang.newStringArray(new Offset(55, 0, "tHeader",
        AnimalScript.DIRECTION_NE), text, "text", null, arrayProps);

    patternHeader = lang.newText(new Offset(0, 50, "tHeader",
        AnimalScript.DIRECTION_SW), "Muster: ", "pHeader", null, textProps);

    patternArray = lang.newStringArray(new Offset(40, 45, "text",
        AnimalScript.DIRECTION_SW), pattern, "pattern", null, arrayProps);

    prefixHeader = lang.newText(new Offset(0, 125, "pHeader",
        AnimalScript.DIRECTION_SW), "Prefixtabelle: ", "pHeader", null,
        textProps);

    prefixTbl = lang.newIntArray(new Offset(0, 125, "pattern",
        AnimalScript.DIRECTION_SW), prefixTable, "shift", null, arrayProps);

    notEqual = lang.newText(new Offset(0, 150, "shift",
        AnimalScript.DIRECTION_SW), "", "notEqual", null, warningProps);
    notEqual.hide();

    jumpTo = lang.newText(new Offset(0, 20, "notEqual",
        AnimalScript.DIRECTION_SW), "", "jumpTo", null, warningProps);
    jumpTo.hide();

    foundString = lang.newText(new Offset(0, 20, "notEqual",
        AnimalScript.DIRECTION_SW), "", "foundString", null, warningProps);
    foundString.hide();

    legendArray = lang.newIntArray(new Offset(0, 50, "foundString",
        AnimalScript.DIRECTION_SW), legend, "legendArray", null, arrayProps);
    lang.newText(new Offset(-100, 0, "legendArray", AnimalScript.DIRECTION_NW),
        "Legende: ", "legendLine", null, textProps);
    legendArray.highlightCell(1, null, null);
    legendArray.highlightCell(2, null, null);
    legendArray.highlightElem(2, null, null);

    lang.newText(
        new Offset(0, 20, "legendArray", AnimalScript.DIRECTION_SW),
        "1. Slot: Noch nicht bearbeitetes Element oder ein fuer das Muster ausgeschlossenes Element.",
        "legendLine", null, textProps);

    lang.newText(
        new Offset(0, 40, "legendArray", AnimalScript.DIRECTION_SW),
        "2. Slot: Element wird gerade verglichen (Element ganz rechts) oder ein in dem Muster enthaltenes Element.",
        "legendLine", null, textProps);

    lang.newText(new Offset(0, 60, "legendArray", AnimalScript.DIRECTION_SW),
        "3. Slot: Aktuell verglichene Buchstaben sind ungleich. ",
        "legendLine", null, textProps);

    iVar = lang.newText(new Offset(50, 0, "text", AnimalScript.DIRECTION_NE),
        "", "iVar", null, warningProps);

    jVar = lang.newText(new Offset(100, 0, "text", AnimalScript.DIRECTION_NE),
        "", "jVar", null, warningProps);

    // parsing anwerfen
    parse();

    Text comparisonText = lang.newText(new Offset(0, 15, "sumupCode",
        AnimalScript.DIRECTION_SW), "", "comparisonText", null);
    TextUpdater comptu = new TextUpdater(comparisonText);
    comptu.addToken("Vergleiche: ");
    comptu.addToken(vars.getVariable(comp));
    comptu.update();

    Text assiText = lang.newText(new Offset(0, 10, "comparisonText",
        AnimalScript.DIRECTION_SW), "", "assiText", null);
    TextUpdater assitu = new TextUpdater(assiText);
    assitu.addToken("Zuweisungen: ");
    assitu.addToken(vars.getVariable(assi));
    assitu.update(); // zum Initialisieren

    exec("header");

  }

  public void showSourceCodePhase1() {
    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    sc = lang.newSourceCode(new Coordinates(50, 150), "sourceCode", null,
        scProps);

    sc.addCodeLine("public void berechnePrefixTable(String muster) {", null, 0,
        null);
    sc.addCodeLine("int i = 0;", null, 1, null);
    sc.addCodeLine("int j = -1;", null, 1, null);
    sc.addCodeLine("prefixTabelle[0] = j;", null, 1, null);
    sc.addCodeLine("while (i < muster.length) {", null, 1, null);
    sc.addCodeLine("while(j>=0 && !muster[j].equals(muster[i])) {", null, 2,
        null);
    sc.addCodeLine("j = prefixTabelle[j];", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("i++;", null, 2, null);
    sc.addCodeLine("j++;", null, 2, null);
    sc.addCodeLine("prefixTabelle[i] = j;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

  }

  /*
   * public void showSourceCodePhase2() { scProps = new SourceCodeProperties();
   * scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
   * scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
   * Font.PLAIN, 12));
   * scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
   * scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
   * 
   * sc = lang.newSourceCode(new Coordinates(650,130), "sourceCode", null,
   * scProps);
   * 
   * sc.addCodeLine("public int runKMP(String muster) {", null, 0, null);
   * sc.addCodeLine("int i = 0;", null, 1, null); sc.addCodeLine("int j = 0;",
   * null, 1, null); sc.addCodeLine("while (i < text.length) {", null, 1, null);
   * sc.addCodeLine("while(j>=0 && !text[i].equals(muster[j])) {", null, 2,
   * null); sc.addCodeLine("j = prefixTabelle[j];", null, 3, null);
   * sc.addCodeLine("}", null, 2, null); sc.addCodeLine("i++;", null, 2, null);
   * sc.addCodeLine("j++;", null, 2, null);
   * sc.addCodeLine("if(j== muster.length) {",null, 2, null);
   * sc.addCodeLine("return i - muster.length;", null, 3, null);
   * sc.addCodeLine("}", null, 2, null); sc.addCodeLine("}", null, 1, null);
   * sc.addCodeLine("return -1;", null, 1, null); sc.addCodeLine("}", null, 0,
   * null);
   * 
   * }
   */

  @Override
  public String getAnnotatedSrc() {
    return "public int runKMP( String muster ) {					@label(\"header\")\n"
        + "	int i = 0;											@label(\"initI\") @declare(\"int\", \"i\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "	int j = 0;											@label(\"initJ\") @declare(\"int\", \"j\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "	while ( i < text.length ) {							@label(\"whileOut\") @inc(\""
        + comp
        + "\")\n"
        + "		while( j>=0										@label(\"whileIn1\") @inc(\""
        + comp
        + "\")\n"
        + "				 && ! text[i].equals(muster[j]) ) {		@label(\"whileIn2\") @continue @inc(\""
        + comp
        + "\") \n"
        + "			j = prefixTabelle[j];						@label(\"calcJ\") @inc(\""
        + assi
        + "\")\n"
        + "		}												@label(\"endWhileIn\")\n"
        + "		i++;											@label(\"IncI\") @inc(\"i\") @inc(\""
        + assi
        + "\")\n"
        + "		j++;											@label(\"IncJ\") @inc(\"j\") @inc(\""
        + assi
        + "\")\n"
        + "		if( j== muster.length ) {							@label(\"checkMusterLength\")@inc(\""
        + comp
        + "\")\n"
        + "			return i - muster.length;					@label(\"returnSpot\")\n"
        + "		}												@label(\"endIf\")\n"
        + "	}													@label(\"endWhileOut\")\n"
        + "	return -1;											@label(\"nothingFound\")\n"
        + "}														@label(\"endMethod\")\n";
  }

  public void runKMP(String pat, String t) {

    showIntro();

    pattern = new String[pat.length()];
    prefixTable = new int[pat.length() + 1];

    for (int i = 0; i < pattern.length; i++) {
      pattern[i] = Character.toString(pat.charAt(i));
    }

    initPhase1();
    showSourceCodePhase1();
    sc.highlight(0);
    lang.nextStep();

    computePrefixTable();

    // showSourceCodePhase2();
    sc.highlight(0);
    text = new String[t.length()];

    // Einfügen des Texts ins TextArray
    for (int i = 0; i < text.length; i++) {
      char c = t.charAt(i);
      String s = Character.toString(c);
      text[i] = s;
    }

    initPhase2(); // Initialisierung der Displayeigenschaften
    lang.nextStep();

    findPattern();
  }

  /**
   * Phase 1: Analyse des Musters. Von jedem Präfix des Musters wird der
   * breiteste Rand ermittelt und in die Tabelle gespeichert.
   */
  public void computePrefixTable() {

    int i = 0;
    patternArray.highlightCell(i, null, null);
    iVar.setText("i: " + i, null, null);

    sc.toggleHighlight(0, 1);

    lang.nextStep();

    int j = -1;
    jVar.setText("j: " + j, null, null);
    sc.toggleHighlight(1, 2);

    lang.nextStep();

    sc.toggleHighlight(2, 3);

    prefixTbl.put(0, j, null, null);
    prefixTbl.highlightCell(0, null, null);

    lang.nextStep();

    sc.toggleHighlight(3, 4);
    lang.nextStep();

    while (i < pattern.length) {

      sc.toggleHighlight(4, 5);
      lang.nextStep();

      while (j >= 0 && !pattern[j].equals(pattern[i])) {

        sc.toggleHighlight(5, 6);
        j = prefixTable[j];
        jVar.setText("j: " + j, null, null);
        lang.nextStep();
        sc.unhighlight(6);

        if (j >= 0 && !pattern[j].equals(pattern[i])) {
          sc.highlight(5);
          lang.nextStep();
        } else {
          sc.highlight(5);
          lang.nextStep();
        }
      }

      sc.unhighlight(5);
      sc.highlight(8);
      i++;
      patternArray.highlightCell(i, null, null);
      iVar.setText("i: " + i, null, null);
      lang.nextStep();
      sc.toggleHighlight(8, 9);
      j++;
      jVar.setText("j: " + j, null, null);
      lang.nextStep();
      sc.toggleHighlight(9, 10);
      prefixTbl.put(i, j, null, null);
      prefixTbl.highlightCell(i, null, null);

      if (i == 1) {
        borderLength.setText("Der Rand des ersten Zeichens ist " + j, null,
            null);
        borderLength.show();
        lang.nextStep();
      } else if (i < pattern.length) {
        borderLength.setText("Der Rand der ersten " + i + " Zeichen ist " + j,
            null, null);
        borderLength.show();
        lang.nextStep();
      } else {
        borderLength
            .setText("Der Rand des ganzen Musters ist " + j, null, null);
        borderLength.show();
        lang.nextStep();
      }
      borderLength.hide();
      sc.toggleHighlight(10, 4);
      lang.nextStep();

    }

    hint1 = lang
        .newText(
            new Offset(0, 100, "sourceCode", AnimalScript.DIRECTION_SW),
            "Der letzte Wert der Praefixtabelle wird fuer weitere Berechnungen nicht gebraucht, ",
            "hint1", null, textProps);
    hint2 = lang
        .newText(
            new Offset(0, 10, "hint1", AnimalScript.DIRECTION_SW),
            "wird aber aus Performancegruenden berechnet, da man dadurch eine if- Abfrage vermeiden kann.",
            "hint2", null, textProps);
    lang.nextStep();

    patternArray.hide();
    prefixTbl.hide();
    header.hide();
    borderLength.hide();
    patternHeader.hide();
    prefixHeader.hide();
    iVar.hide();
    jVar.hide();
    sc.hide();
    hint1.hide();
    hint2.hide();

  }

  public void findPattern() {

    exec("initI");
    int i = 0; // Laufvariable des Textes
    iVar.setText("i: " + i, null, null);
    lang.nextStep();

    exec("initJ");
    int j = 0;// Laufvariable für Shiftable and Pattern
    jVar.setText("j: " + j, null, null);

    lang.nextStep();
    exec("whileOut");
    lang.nextStep();
    while (i < text.length) {

      textArray.highlightCell(i, null, null);
      patternArray.highlightCell(j, null, null);
      prefixTbl.highlightCell(j, null, null);

      exec("whileIn1");
      lang.nextStep();
      exec("whileIn2");
      while (j >= 0 && !(text[i].equals(pattern[j]))) {

        int temp = j;
        lang.nextStep();
        notEqual.setText("Die Zeichen " + text[i] + " und " + pattern[j]
            + " sind ungleich.", null, null);
        notEqual.show();
        textArray.highlightElem(i, null, null);
        patternArray.highlightElem(temp, null, null);
        lang.nextStep();

        exec("calcJ");
        j = prefixTable[j];
        jVar.setText("j: " + j, null, null);
        lang.nextStep();

        if (j == -1)
          jumpTo.setText(
              "=> Starte Algorithmus neu bei naechster Position im Text.",
              null, null);
        else
          jumpTo.setText("=> Springe im Pattern zur Position " + j, null, null);

        jumpTo.show();

        lang.nextStep();

        prefixTbl.unhighlightCell(temp, null, null);

        textArray.unhighlightCell(i - temp, i - j - 1, null, null);
        patternArray.unhighlightCell(j + 1, temp, null, null);

        textArray.unhighlightElem(i, null, null);
        patternArray.unhighlightElem(temp, null, null);

        prefixTbl.highlightCell(j, null, null);

        lang.nextStep();
        notEqual.hide();
        jumpTo.hide();

        exec("endWhileIn");
        lang.nextStep();
        exec("whileIn1");
        if (j != -1) {
          lang.nextStep();
          exec("whileIn2");
        }

      }

      notEqual.hide();

      lang.nextStep();
      prefixTbl.unhighlightCell(j, null, null);
      exec("IncI");
      i++;
      iVar.setText("i: " + i, null, null);

      lang.nextStep();
      exec("IncJ");
      j++;
      jVar.setText("j: " + j, null, null);

      lang.nextStep();
      exec("checkMusterLength");
      lang.nextStep();
      if (j == pattern.length) {
        exec("returnSpot");
        foundString.setText("Muster wurde an Position " + (i - j)
            + " gefunden.", null, null);
        foundString.show();
        lang.nextStep();
        break;
      }
      exec("endIf");
      lang.nextStep();
      exec("endWhileOut");
      lang.nextStep();
      exec("whileOut");
      lang.nextStep();

    }

    if (i == text.length && j != pattern.length) {
      exec("nothingFound");
      foundString.setText("Muster nicht gefunden. Return -1", null, null);
      foundString.show();
      lang.nextStep();
    }

    exec("endMethod");
  }

  public void getBorderTable() {
    for (int i = 0; i < prefixTable.length; i++) {
      System.out.print(prefixTable[i] + " ");
    }
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    init();
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    textProps = (TextProperties) props.getPropertiesByName("textProps");
    warningProps = (TextProperties) props.getPropertiesByName("warningProps");
    iVarProps = (TextProperties) props.getPropertiesByName("iVariable");
    jVarProps = (TextProperties) props.getPropertiesByName("jVariable");

    String t = (String) primitives.get("text");
    String pat = (String) primitives.get("muster");

    runKMP(pat, t);
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Knuth, Morris, Pratt (1977)";
  }

  @Override
  public String getAnimationAuthor() {
    return "David Sharma, Björn Pantel";
  }

  /**
   * Pseudocode einfügen
   */
  @Override
  public String getCodeExample() {
    return "public int runKMP(String muster) { <br>" + " int i = 0;<br>"
        + " int j = 0;<br>" + " while (i < text.length) {<br>"
        + "   while(j>=0 && !text[i].equals(muster[j])) {<br>"
        + "      j = prefixTabelle[j];<br>" + "   }<br>" + "   i++;<br>"
        + "   j++;<br>" + "   if(j== muster.length) {<br>"
        + "      return i - muster.length;<br>" + "   }<br>" + " }<br>"
        + " return -1;<br>" + "}<br>";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Der Knuth-Morris-Pratt Algorithmus ist ein String-Matching Algorithmus.<br> "
        + "Ein Vorteil zur naiven Suche ist, dass wiederholende Vergleiche im Algorithmus vermieden werden.<br>"
        + "Der Algorithmus teilt sich in zwei Phasen auf. Die erste Phase nennt man 'Pr&auml;fix-Analyse', die zweite Phase ist der eigentliche Sting-Matching Algorithmus.<br>"
        + "In der Pr&auml;fix-Analyse wird eine Pr&auml;fix Tabelle erstellt, die fuer Teilworte des gesuchten Musters die L&auml;nge des Randes enth&auml;lt.<br>"
        + "Der Rand eines Wortes w ist ein Teilwort w', das gleichzeitig (echtes) Pr&auml;fix und (echtes) Suffix von w ist.<br>"
        + "In der zweiten Phase wird dann in einem Text das gegebene Muster gesucht.<br>"
        + "Wenn der Vergleich zweier Zeichen scheitert, kommt die Pr&auml;fix-Tabelle ins Spiel. Mit den gespeicherten L&auml;ngen des Randes kann man die Schiebedistanz berechnen.<br>"
        + "Die Schiebedistanz entspricht einer Verschiebung des Musters im Text-Array. Von da aus wird dann weitergemacht.";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  @Override
  public String getName() {
    return "Knuth-Morris-Pratt Algorithmus (annotiert)";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    String t = (String) primitives.get("text");
    String pat = (String) primitives.get("muster");
    if (t == null) {
      throw new IllegalArgumentException(
          "Der Eingabetext muss mit der Enter-Taste bestaetigt werden.");
    }
    if (pat == null)
      throw new IllegalArgumentException(
          "Der Mustertext muss mit der Enter-Taste bestaetigt werden.");
    if (pat.length() > t.length())
      throw new IllegalArgumentException(
          "Das Mustertext muss kleiner oder gleich dem Eingabetext sein.");
    return true;
  }

}
