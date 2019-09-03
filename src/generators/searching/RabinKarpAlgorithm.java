package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Thomas Schlosser
 * 
 */
public class RabinKarpAlgorithm implements Generator {

  private final static TicksTiming ticks20                           = new TicksTiming(
                                                                         20);
  private final static TicksTiming ticks40                           = new TicksTiming(
                                                                         40);

  private final static int         WIDTH_OFFSET_ARRAYELEMENT         = 5;
  private final static int         WIDTH_SINGLE_MONOSPACED_CHARACTER = 7;

  private Language                 lang;

  private TextProperties           titleProperties;
  private RectProperties           titleBoxProperties;
  private TextProperties           subTitleProperties;
  private TextProperties           textProperties;
  private TextProperties           hashLabelProperties;
  private ArrayProperties          textArrayProperties;
  private ArrayProperties          patternArrayProperties;
  private ArrayProperties          variablesArrayProperties;
  private ArrayMarkerProperties    firstLoopPMarkerProperties;
  private ArrayMarkerProperties    firstLoopTMarkerProperties;
  private ArrayMarkerProperties    secondLoopMarkerProperties;
  private PolylineProperties       hashCalcValueLineProperties;
  private SourceCodeProperties     sourceProperties;

  private List<Primitive>          neverHideList;
  private Rect                     hRect;
  private StringArray              variablesDesc;
  private StringArray              variablesName;
  private StringArray              variablesValues;
  private SourceCode               source;
  private Primitive                outputOffsetRef;
  private int                      outputNextYOffset;

  // variables for statistics
  private List<Integer>            matches;
  private List<Integer>            hashMatches;
  private int                      charCompCount;
  private int                      charCompEarlyTerminationCount;
  private int                      finalI;
  private int                      finalS;

  public void init() {
    lang = new AnimalScript("Rabin-Karp-Algorithm", "Thomas Schlosser", 700,
        600); // max values for x=685 and y=594
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    String text = (String) primitives.get("Text (T)");
    String pattern = (String) primitives.get("Muster (P)");
    int d = (Integer) primitives
        .get("Basis (d) [für den ASCII-Zeichensatz gilt d=256]");
    int q = (Integer) primitives
        .get("Modulus (q) [gewöhnlich als Primzahl gewählt]");
    secondLoopMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("Zeiger (2. For-Schleife)");
    textArrayProperties = (ArrayProperties) props
        .getPropertiesByName("Textarrays");
    firstLoopPMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("Zeiger auf Muster (1. For-Schleife)");
    titleProperties = (TextProperties) props.getPropertiesByName("Überschrift");
    variablesArrayProperties = (ArrayProperties) props
        .getPropertiesByName("Variablen");
    subTitleProperties = (TextProperties) props
        .getPropertiesByName("Unterüberschriften");
    hashCalcValueLineProperties = (PolylineProperties) props
        .getPropertiesByName("Hashwertberechnungspfeile");
    patternArrayProperties = (ArrayProperties) props
        .getPropertiesByName("Musterarrays");
    titleBoxProperties = (RectProperties) props
        .getPropertiesByName("Box der Überschrift");
    sourceProperties = (SourceCodeProperties) props
        .getPropertiesByName("Quellcode");
    firstLoopTMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("Zeiger auf Text (1. For-Schleife)");

    // fix missing configurability of current XML-Format definition (especially
    // 'bold' and 'font-size'!=12)
    titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    subTitleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SERIF, Font.BOLD, 16));
    variablesArrayProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(Font.MONOSPACED, Font.BOLD, 12));

    textProperties = new TextProperties("textProperties");
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 14));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    hashLabelProperties = new TextProperties("textProperties");
    hashLabelProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));
    hashLabelProperties
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // reset global variables
    neverHideList = new ArrayList<Primitive>();
    hRect = null;
    variablesDesc = null;
    variablesName = null;
    variablesValues = null;
    source = null;
    outputOffsetRef = null;
    outputNextYOffset = 0;

    // (p)reset variables for statistics
    matches = new ArrayList<Integer>();
    hashMatches = new ArrayList<Integer>();
    charCompCount = 0;
    charCompEarlyTerminationCount = 0;
    finalI = -1;
    finalS = -1;

    matcher(text.toCharArray(), pattern.toCharArray(), d, q);

    return lang.toString();
  }

  public void matcher(char[] text, char[] pattern, int d, int q) {
    Text header = lang.newText(new Coordinates(350, 30),
        "Rabin-Karp-Algorithm", "header", null, titleProperties);
    neverHideList.add(header);
    hRect = lang.newRect(new Offset(-335, -5, header, "NW"), new Offset(335, 5,
        header, "SE"), "hRect", null, titleBoxProperties);
    neverHideList.add(hRect);
    Text subTitle1 = lang.newText(new Offset(0, 15, hRect, "SW"),
        "Beschreibung", "subTitle1", null, subTitleProperties);
    Text description1 = lang
        .newText(
            new Offset(0, 5, subTitle1, "SW"),
            "Der 'Rabin-Karp-Algorithm' sucht innerhalb einer Zeichenkette nach jedem Vorkommen eines bestimmten fixen Musters.",
            "description1", null, textProperties);
    Text description2 = lang
        .newText(
            new Offset(0, 0, description1, "SW"),
            "Durch die Verwendung einer Hashfunktion in einem Vorvergleich wird die Dauer eines Vergleichs zwischen dem Muster",
            "description2", null, textProperties);
    Text description3 = lang
        .newText(
            new Offset(0, 0, description2, "SW"),
            "und den einzelnen Teilen der Zeichenkette stark verringert. Erst wenn der Vorvergleich beim Iterieren über die",
            "description3", null, textProperties);
    Text description4 = lang
        .newText(
            new Offset(0, 0, description3, "SW"),
            "Zeichenkette eine mögliche Übereinstimmung feststellt, wird ein zeichenweiser Vergleich zwischen dem Muster",
            "description4", null, textProperties);
    Text description5 = lang
        .newText(
            new Offset(0, 0, description4, "SW"),
            "und der aktuellen Teil-Zeichenkette vorgenommen. Die Hashwerte lassen sich durch die Verwendung einer rollende",
            "description5", null, textProperties);
    Text description6 = lang.newText(new Offset(0, 0, description5, "SW"),
        "Hashfunktion in konstanter Zeit berechnen.", "description6", null,
        textProperties);

    Text subTitle2 = lang.newText(new Offset(0, 20, description6, "SW"),
        "Parameter", "subTitle2", null, subTitleProperties);
    Text param1 = lang
        .newText(
            new Offset(0, 5, subTitle2, "SW"),
            "T: Zeichenkette (Text), in der alle Vorkommnisse des Musters gefunden werden sollen",
            "param1", null, textProperties);
    Text param2 = lang.newText(new Offset(0, 0, param1, "SW"),
        "P: Muster, nach dem in der Zeichenkette gesucht werden soll",
        "param2", null, textProperties);
    Text param3 = lang
        .newText(
            new Offset(0, 0, param2, "SW"),
            "d: Basis, zu der jedes Zeichen dargestellt werden kann (für den ASCII-Zeichensatz gilt d=256)",
            "param3", null, textProperties);
    Text param4 = lang
        .newText(
            new Offset(0, 0, param3, "SW"),
            "q: Alle Berechnungen werden modulo q ausgeführt (gewöhnlich als Primzahl gewählt)",
            "param4", null, textProperties);

    Text subTitle3 = lang.newText(new Offset(0, 20, param4, "SW"),
        "Weitere Variablen", "subTitle3", null, subTitleProperties);
    Text descVar1 = lang.newText(new Offset(0, 5, subTitle3, "SW"),
        "n: Länge der Zeichenkette T", "descVar1", null, textProperties);
    Text descVar2 = lang.newText(new Offset(0, 0, descVar1, "SW"),
        "m: Länge des Musters P", "descVar2", null, textProperties);
    Text descVar3 = lang.newText(new Offset(0, 0, descVar2, "SW"),
        "h: Faktor des höchsten Zeichens im Muster", "descVar3", null,
        textProperties);
    Text descVar4 = lang.newText(new Offset(0, 0, descVar3, "SW"),
        "p: Hashwert des Musters", "descVar4", null, textProperties);
    Text descVar5 = lang.newText(new Offset(0, 0, descVar4, "SW"),
        "t: Hashwert der aktuellen Teil-Zeichenkette", "descVar5", null,
        textProperties);
    Text descVar6 = lang.newText(new Offset(0, 0, descVar5, "SW"),
        "i: Index zur Berechnung der initialen Hashwerte", "descVar6", null,
        textProperties);
    Text descVar7 = lang.newText(new Offset(0, 0, descVar6, "SW"),
        "s: Index zur Iteration über die Zeichenkette", "descVar7", null,
        textProperties);

    lang.nextStep();

    subTitle1.hide();
    subTitle2.hide();
    subTitle3.hide();
    description1.hide();
    description2.hide();
    description3.hide();
    description4.hide();
    description5.hide();
    description6.hide();

    param1.hide();
    param2.hide();
    param3.hide();
    param4.hide();

    descVar1.hide();
    descVar2.hide();
    descVar3.hide();
    descVar4.hide();
    descVar5.hide();
    descVar6.hide();
    descVar7.hide();

    source = lang.newSourceCode(new Offset(0, 15, hRect, "SW"), "source", null,
        sourceProperties);

    source.addCodeLine("RABIN-KARP-MATCHER(T,P,d,q)", null, 0, null);
    source.addCodeLine("n := länge[T]", null, 1, null);
    source.addCodeLine("m := länge[P]", null, 1, null);
    source.addCodeLine("h := d^(m-1) mod q", null, 1, null);
    source.addCodeLine("p := 0", null, 1, null);
    source.addCodeLine("t := 0", null, 1, null);
    source.addCodeLine("for i := 1 to m do", null, 1, null);
    source.addCodeLine("p := (dp + P[i]) mod q", null, 2, null);
    source.addCodeLine("t := (dt + T[i]) mod q", null, 2, null);
    source.addCodeLine("for s := 0 to n-m do", null, 1, null);
    source.addCodeLine("if p=t then", null, 2, null);
    source.addCodeLine("if P[1..m]=T[s+1..s+m] then", null, 3, null);
    source.addCodeLine("print 'Muster tritt mit der Verschiebung' s 'auf.'",
        null, 4, null);
    source.addCodeLine("if s < n-m then", null, 2, null);
    source.addCodeLine("t := (d(t-T[s+1]h)+T[s+m+1]) mod q", null, 3, null);

    lang.nextStep();

    source.highlight(1);
    String[] textString = new String[text.length];
    int[] textInt = new int[text.length];
    String[] patternString = new String[pattern.length];
    int[] patternInt = new int[pattern.length];
    convertToArrays(text, textString, textInt);
    convertToArrays(pattern, patternString, patternInt);

    Text textLabel = lang.newText(new Offset(0, 75, source, "baseline start"),
        "Text", "textLabel", null, subTitleProperties);
    StringArray textStringArray = lang.newStringArray(new Offset(185, 0,
        textLabel, "baseline start"), textString, "textStringArray", null,
        textArrayProperties);
    Text patternLabel = lang.newText(new Offset(0, 25, textLabel,
        "baseline start"), "Muster", "patternLabel", null, subTitleProperties);
    StringArray patternStringArray = lang.newStringArray(new Offset(185, 0,
        patternLabel, "baseline start"), patternString, "patternStringArray",
        null, patternArrayProperties);

    Text textIntLabel = lang.newText(new Offset(0, 100, patternLabel,
        "baseline start"), "Text [Dezimal]", "textIntLabel", null,
        subTitleProperties);
    IntArray textIntArray = lang.newIntArray(new Offset(185, 0, textIntLabel,
        "baseline start"), textInt, "textIntArray", null, textArrayProperties);
    Text patternIntLabel = lang.newText(new Offset(0, 75, textIntLabel,
        "baseline start"), "Muster [Dezimal]", "patternIntLabel", null,
        subTitleProperties);
    IntArray patternIntArray = lang.newIntArray(new Offset(185, 0,
        patternIntLabel, "baseline start"), patternInt, "patternIntArray",
        null, patternArrayProperties);

    Text variablesLabel = lang.newText(new Offset(50, 0, source, "NE"),
        "Variablen", "variablesLabel", null, subTitleProperties);
    variablesDesc = lang.newStringArray(
        new Offset(0, 10, variablesLabel, "SW"), new String[] {
            " Beschreibung ", "  Text  ", "  Muster  ", "  Basis  ",
            "  Modulus  ", "  Textlänge  ", "  Musterlänge  ", "  Faktor  ",
            "  Hashwert  ", "  Hashwert  ", "  Index  ", "  Index  " },
        "variablesDesc", null, variablesArrayProperties);
    variablesName = lang.newStringArray(new Offset(0, 0, variablesDesc, "NE"),
        new String[] { " Name ", "  T  ", "  P  ", "  d  ", "  q  ", "  n  ",
            "  m  ", "  h  ", "  p  ", "  t  ", "  i  ", "  s  " },
        "variablesName", null, variablesArrayProperties);
    variablesValues = lang.newStringArray(
        new Offset(0, 0, variablesName, "NE"), new String[] { " Wert ",
            " (siehe unten) ", " (siehe unten) ", getVariableValueString(d),
            getVariableValueString(q), " - ", " - ", " - ", " - ", " - ",
            " - ", " - " }, "variablesValues", null, variablesArrayProperties);

    outputOffsetRef = lang.newText(new Offset(200, 0, variablesLabel,
        "baseline"), "Ausgabe", "outputLabel", null, subTitleProperties);
    outputNextYOffset = 10;

    lang.nextStep("Initialisierung");

    rabinKarpMatcher(textStringArray, textIntArray, patternStringArray,
        patternIntArray, d, q);

    lang.nextStep();

    hideAllBut();
    showStatistics();
  }

  private void rabinKarpMatcher(StringArray textStringArray,
      IntArray textIntArray, StringArray patternStringArray,
      IntArray patternIntArray, int d, int q) {
    int n = textIntArray.getLength();
    variablesValues.put(5, getVariableValueString(n), null, null);
    source.unhighlight(1);
    source.highlight(2, 0, false, ticks20, null);

    lang.nextStep();

    int m = patternIntArray.getLength();
    variablesValues.put(6, getVariableValueString(m), null, null);
    source.unhighlight(2);
    source.highlight(3, 0, false, ticks20, null);

    lang.nextStep();

    int h = modexp(d, m - 1, q);
    variablesValues.put(7, getVariableValueString(h), null, null);
    source.unhighlight(3);
    source.highlight(4, 0, false, ticks20, null);

    lang.nextStep();

    int p = 0;
    variablesValues.put(8, getVariableValueString(p), null, null);
    source.unhighlight(4);
    source.highlight(5, 0, false, ticks20, null);

    lang.nextStep();

    int t = 0;
    variablesValues.put(9, getVariableValueString(t), null, null);
    source.unhighlight(5);

    ArrayMarker pMarkerI = null;
    ArrayMarker tMarkerI = null;
    for (int i = 1; i <= m; i++) {
      source.highlight(6, 0, false, ticks20, null);

      lang.nextStep();

      source.unhighlight(6);
      variablesValues.put(10, getVariableValueString(i), null, null);
      source.highlight(7, 0, false, ticks20, null);

      if (pMarkerI != null)
        pMarkerI.hide();
      firstLoopPMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY,
          "i=" + i);
      pMarkerI = lang.newArrayMarker(patternIntArray, i - 1, "i" + i + "p",
          ticks20, firstLoopPMarkerProperties);

      lang.nextStep();

      p = mod(d * p + patternIntArray.getData(i - 1), q);
      variablesValues.put(8, getVariableValueString(p), null, null);
      source.unhighlight(7);
      source.highlight(8, 0, false, ticks20, null);

      if (tMarkerI != null)
        tMarkerI.hide();
      firstLoopTMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY,
          "i=" + i);
      tMarkerI = lang.newArrayMarker(textIntArray, i - 1, "i" + i + "t",
          ticks20, firstLoopTMarkerProperties);

      pMarkerI.changeColor(null, Color.GRAY, null, null);

      lang.nextStep();

      t = mod(d * t + textIntArray.getData(i - 1), q);
      variablesValues.put(9, getVariableValueString(t), null, null);
      source.unhighlight(8);

      tMarkerI.changeColor(null, Color.GRAY, null, null);

      // store for statistics
      finalI = i;
    }

    // clean up markers
    if (pMarkerI != null)
      pMarkerI.hide();
    if (tMarkerI != null)
      tMarkerI.hide();
    variablesValues.put(10, " - ", null, null);

    ArrayMarker tMarkerS = null;
    ArrayMarker tMarkerSInt = null;
    for (int s = 0; s <= n - m; s++) {
      source.highlight(9, 0, false, ticks20, null);

      lang.nextStep((s + 1) + ". Iteration");

      variablesValues.put(11, getVariableValueString(s), null, null);
      source.unhighlight(9);
      source.highlight(10, 0, false, ticks20, null);

      if (tMarkerS != null)
        tMarkerS.hide();
      if (tMarkerSInt != null)
        tMarkerSInt.hide();
      secondLoopMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY,
          "(s+1)=" + (s + 1));
      tMarkerS = lang.newArrayMarker(textStringArray, s, "s" + s + "t", null,
          secondLoopMarkerProperties);
      tMarkerSInt = lang.newArrayMarker(textIntArray, s, "s" + s + "ti", null,
          secondLoopMarkerProperties);

      variablesDesc.highlightElem(8, ticks20, null);
      variablesName.highlightElem(8, ticks20, null);
      variablesValues.highlightElem(8, ticks20, null);
      variablesDesc.highlightElem(9, ticks20, null);
      variablesName.highlightElem(9, ticks20, null);
      variablesValues.highlightElem(9, ticks20, null);

      if (s > 0) {
        patternStringArray.moveBy("translate",
            getArrayElementWidthForLength(textStringArray.getData(s - 1)
                .length()), 0, null, ticks40);
        patternIntArray
            .moveBy("translate",
                getArrayElementWidth(textIntArray.getData(s - 1)), 0, null,
                ticks40);
        if (!matchedCharacter(s - 1, m)) {
          textStringArray.highlightCell(s - 1, null, ticks40);
          textIntArray.highlightCell(s - 1, null, ticks40);
        }
      }

      if (p == t && !charComparison(patternIntArray, textIntArray, s))
        lang.nextStep((hashMatches.size() - matches.size() + 1)
            + ". unechter Treffer");
      else
        lang.nextStep();

      source.unhighlight(10);
      variablesDesc.unhighlightElem(8, null, null);
      variablesName.unhighlightElem(8, null, null);
      variablesValues.unhighlightElem(8, null, null);
      variablesDesc.unhighlightElem(9, null, null);
      variablesName.unhighlightElem(9, null, null);
      variablesValues.unhighlightElem(9, null, null);

      if (p == t) {
        hashMatches.add(s);
        source.highlight(11, 0, false, ticks20, null);
        for (int tmp = 0; tmp < m; tmp++) {
          charCompCount++;
          if (patternIntArray.getData(tmp) != textIntArray.getData(tmp + s)) {
            textStringArray.highlightElem(s + tmp, null, ticks20);
            textIntArray.highlightElem(s + tmp, null, ticks20);
          } else {
            patternStringArray.highlightElem(tmp, null, ticks20);
            patternStringArray.highlightCell(tmp, null, ticks20);
            patternIntArray.highlightElem(tmp, null, ticks20);
            patternIntArray.highlightCell(tmp, null, ticks20);
          }
        }

        lang.nextStep();
        source.unhighlight(11);
        for (int tmp = 0; tmp < m; tmp++) {
          if (patternIntArray.getData(tmp) != textIntArray.getData(tmp + s)) {
            textStringArray.unhighlightElem(s + tmp, null, null);
            textIntArray.unhighlightElem(s + tmp, null, null);
          } else {
            patternStringArray.unhighlightElem(tmp, null, null);
            patternStringArray.unhighlightCell(tmp, null, null);
            patternIntArray.unhighlightElem(tmp, null, null);
            patternIntArray.unhighlightCell(tmp, null, null);
          }
        }

        if (charComparison(patternIntArray, textIntArray, s)) {
          matches.add(s);

          source.highlight(12, 0, false, ticks20, null);

          lang.nextStep(matches.size() + ". echter Treffer");
          source.unhighlight(12);

          // System.out.println("Muster tritt mit der Verschiebung " + s +
          // " auf.");

          outputOffsetRef = lang.newText(new Offset(0, outputNextYOffset,
              outputOffsetRef, "SW"), "Muster tritt mit der Verschiebung " + s
              + " auf.", "output" + s, null, textProperties);
          outputNextYOffset = 0;
        }
      }

      source.highlight(13, 0, false, ticks20, null);
      variablesDesc.highlightElem(5, ticks20, null);
      variablesName.highlightElem(5, ticks20, null);
      variablesValues.highlightElem(5, ticks20, null);
      variablesDesc.highlightElem(6, ticks20, null);
      variablesName.highlightElem(6, ticks20, null);
      variablesValues.highlightElem(6, ticks20, null);
      variablesDesc.highlightElem(11, ticks20, null);
      variablesName.highlightElem(11, ticks20, null);
      variablesValues.highlightElem(11, ticks20, null);

      lang.nextStep();

      source.unhighlight(13);
      variablesDesc.unhighlightElem(5, null, null);
      variablesName.unhighlightElem(5, null, null);
      variablesValues.unhighlightElem(5, null, null);
      variablesDesc.unhighlightElem(6, null, null);
      variablesName.unhighlightElem(6, null, null);
      variablesValues.unhighlightElem(6, null, null);
      variablesDesc.unhighlightElem(11, null, null);
      variablesName.unhighlightElem(11, null, null);
      variablesValues.unhighlightElem(11, null, null);

      if (s < n - m) {
        source.highlight(14, 0, false, ticks20, null);

        String hashCalcString = "(d*(t-" + textIntArray.getData(s) + "*h)+"
            + textIntArray.getData(s + m) + ") mod q";
        Text hashCalcText = lang
            .newText(
                getHashCalcLabelNW(textIntArray, s + 1, m,
                    hashCalcString.length()), hashCalcString, "hashCalcLabel"
                    + s, ticks20, hashLabelProperties);
        Offset nodeL1Top = getSWOfArrayElement(textIntArray, s + 1);
        Node nodeL1Bottom = new Offset(0, 0, hashCalcText, "NW");
        Polyline line1 = lang.newPolyline(
            new Node[] { nodeL1Top, nodeL1Bottom }, "l1" + s, ticks20);

        Offset nodeL2Top = getSEOfArrayElement(textIntArray, s + m);
        Node nodeL2Bottom = new Offset(0, 0, hashCalcText, "NE");
        Polyline line2 = lang.newPolyline(
            new Node[] { nodeL2Top, nodeL2Bottom }, "l2" + s, ticks20);

        Offset nodeL3Top = getEOfArrayElement(textIntArray, s);
        Node nodeL3Bottom = getNodeOfLeftTHashCalcLabel(textIntArray, s);
        Polyline line3 = lang.newPolyline(
            new Node[] { nodeL3Top, nodeL3Bottom }, "l3" + s, ticks20,
            hashCalcValueLineProperties);

        Offset nodeL4Top = getEOfArrayElement(textIntArray, s + m);
        Node nodeL4Bottom = getNodeOfRightTHashCalcLabel(textIntArray, s, m, h);
        Polyline line4 = lang.newPolyline(
            new Node[] { nodeL4Top, nodeL4Bottom }, "l4" + s, ticks20,
            hashCalcValueLineProperties);

        lang.nextStep();

        hashCalcText.hide();
        line1.hide();
        line2.hide();
        line3.hide();
        line4.hide();

        t = mod(
            d * (t - textIntArray.getData(s) * h) + textIntArray.getData(s + m),
            q);
        variablesValues.put(9, getVariableValueString(t), null, null);
        source.unhighlight(14);
      }

      // store for statistics
      finalS = s;
    }

    // clean up markers
    if (tMarkerS != null)
      tMarkerS.hide();
    if (tMarkerSInt != null)
      tMarkerSInt.hide();
    variablesValues.put(11, " - ", null, null);

    for (int k = n - m + 1; k < n; k++) {
      if (!matchedCharacter(k, m)) {
        textStringArray.highlightCell(k, null, null);
        textIntArray.highlightCell(k, null, null);
      }
    }
  }

  private boolean charComparison(IntArray pattern, IntArray text, int offset) {
    for (int i = 0; i < pattern.getLength(); i++) {
      charCompEarlyTerminationCount++;
      if (pattern.getData(i) != text.getData(offset + i))
        return false;
    }
    return true;
  }

  private int mod(int x, int y) {
    int result = x % y;
    return result < 0 ? result + y : result;
  }

  private void convertToArrays(char[] input, String[] output1, int[] output2) {
    for (int i = 0; i < input.length; i++) {
      output1[i] = String.valueOf(input[i]);
      output2[i] = input[i];
    }
  }

  private boolean matchedCharacter(int pos, int patternLength) {
    for (Integer m : matches)
      if (pos >= m && pos < m + patternLength)
        return true;
    return false;
  }

  private String getVariableValueString(int value) {
    return " " + value + " ";
  }

  private int getArrayElementWidth(int value) {
    return getArrayElementWidthForLength(numberOfDigits(value));
  }

  private int getArrayElementWidthForLength(int size) {
    return WIDTH_SINGLE_MONOSPACED_CHARACTER * size + WIDTH_OFFSET_ARRAYELEMENT;
  }

  private int getArrayWidthExclusiveElem(IntArray array, int startElem,
      int endElem) {
    int result = 0;
    for (int i = startElem; i < endElem; i++) {
      result += getArrayElementWidth(array.getData(i));
    }
    return result;
  }

  private int getArrayWidthInclusiveElem(IntArray array, int startElem,
      int endElem) {
    return getArrayWidthExclusiveElem(array, startElem, endElem + 1);
  }

  private Offset getSWOfArrayElement(IntArray array, int elem) {
    int xOffset = getArrayWidthExclusiveElem(array, 0, elem);
    return new Offset(xOffset - 3, 5, "textIntArray[0]", "SW"); // - fix offset
  }

  private Offset getEOfArrayElement(IntArray array, int elem) {
    int xOffset = getArrayWidthInclusiveElem(array, 0, elem);
    xOffset -= (getArrayElementWidth(array.getData(elem)) / 2);
    return new Offset(xOffset - 3, 5, "textIntArray[0]", "SW"); // - fix offset
  }

  private Offset getSEOfArrayElement(IntArray array, int elem) {
    int xOffset = getArrayWidthInclusiveElem(array, 0, elem);
    return new Offset(xOffset - 3, 5, "textIntArray[0]", "SW"); // - fix offset
  }

  private Node getHashCalcLabelNW(IntArray array, int elem, int patternLength,
      int labelLength) {
    int xOffset = getArrayWidthExclusiveElem(array, 0, elem);
    xOffset -= ((labelLength * WIDTH_SINGLE_MONOSPACED_CHARACTER) - getArrayWidthInclusiveElem(
        array, elem, elem + patternLength - 1)) / 2;
    return new Offset(xOffset - 3, 25, "textIntArray[0]", "SW"); // - fix offset
  }

  private Offset getNodeOfLeftTHashCalcLabel(IntArray array, int elem) {
    int xOffset = (numberOfDigits(array.getData(elem)) * WIDTH_SINGLE_MONOSPACED_CHARACTER) / 2;
    xOffset += 6 * WIDTH_SINGLE_MONOSPACED_CHARACTER; // fix prefix length;
                                                      // "(d*(t-"
    return new Offset(xOffset, 0, "hashCalcLabel" + elem, "NW");
  }

  private Offset getNodeOfRightTHashCalcLabel(IntArray array, int elem,
      int patternLength, int h) {
    int xOffset = (numberOfDigits(array.getData(elem + patternLength)) * WIDTH_SINGLE_MONOSPACED_CHARACTER) / 2;
    xOffset += (6 + numberOfDigits(array.getData(elem)) + 4)
        * WIDTH_SINGLE_MONOSPACED_CHARACTER; // fix prefix length
    return new Offset(xOffset, 0, "hashCalcLabel" + elem, "NW");
  }

  private int numberOfDigits(int value) {
    return (int) Math.ceil(Math.log10(value + 1));
  }

  /**
   * calculates x^i % q
   */
  public static int modexp(int x, int i, int q) {
    if (i == 0)
      return 1;
    long t = modexp(x, i / 2, q);
    long b = t * t;
    long c = (b % q);
    if (i % 2 == 1)
      c = (c * x) % q;
    return (int) c;
  }

  private void showStatistics() {
    Text overviewTitle1 = lang.newText(new Offset(0, 15, hRect, "SW"),
        "Abschließende Übersicht", "overviewTitle1", null, subTitleProperties);
    Text overviewDescription1 = lang
        .newText(
            new Offset(0, 5, overviewTitle1, "SW"),
            "Der soeben animierte Rabin-Karp-Algorithmus weist folgende Statistiken auf:",
            "overviewDescription1", null, textProperties);

    Text wrongMatchesStatistic = lang.newText(new Offset(0, 15,
        overviewDescription1, "SW"),
        "# unechte Treffer: " + (hashMatches.size() - matches.size()),
        "wrongMatchesStatistic", null, textProperties);
    Text matchesStatistic = lang.newText(new Offset(0, 5,
        wrongMatchesStatistic, "SW"), "# Treffer: " + matches.size(),
        "matchesStatistic", null, textProperties);

    Text hashCompStatistic = lang.newText(new Offset(0, 15, matchesStatistic,
        "SW"), "# Hashwertvergleiche: " + (finalS != -1 ? finalS + 1 : 0),
        "hashCompStatistic", null, textProperties);
    Text charCompStatistic = lang.newText(new Offset(0, 5, hashCompStatistic,
        "SW"), "# Zeichenvergleiche: " + charCompCount, "charCompStatistic",
        null, textProperties);
    Text charCompEarlyTerminationStatistic = lang.newText(new Offset(0, 5,
        charCompStatistic, "SW"),
        "# Zeichenvergleiche (bei 'early termination'): "
            + charCompEarlyTerminationCount,
        "charCompEarlyTerminationStatistic", null, textProperties);

    Text overviewDescription2 = lang
        .newText(
            new Offset(0, 30, charCompEarlyTerminationStatistic, "SW"),
            "Die Laufzeit des soeben animierten Rabin-Karp-Algorithmus lässt sich anhand folgender Werte festmachen:",
            "overviewDescription2", null, textProperties);
    int statementsCount = 5 + (finalI != -1 ? 3 * finalI : 0) + 4
        * (finalS + 1) - (finalS != -1 ? 1 : 0) + hashMatches.size()
        + matches.size();
    Text statementsStatistic = lang.newText(new Offset(0, 15,
        overviewDescription2, "SW"), "# ausgeführter Anweisungen (Zeilen): "
        + statementsCount, "statementsStatistic", null, textProperties);
    int assignmentCount = 5 + (finalI != -1 ? 3 * finalI : 0) + 2
        * (finalS + 1) + 1;
    Text assignmentsStatistic = lang.newText(new Offset(0, 5,
        statementsStatistic, "SW"), "# Zuweisungen: " + assignmentCount,
        "assignmentsStatistic", null, textProperties);
    int moduloCount = 1 + (finalI != -1 ? 2 * finalI : 0) + (finalS + 1);
    Text moduloStatistic = lang.newText(new Offset(0, 5, assignmentsStatistic,
        "SW"), "# Modulo-Operationen: " + moduloCount, "moduloStatistic", null,
        textProperties);
    int compEqualsCount = charCompCount + (finalI != -1 ? finalI : 0)
        + (finalS != -1 ? 2 * (finalS + 1) : 0);
    Text compEqualsStatistic = lang.newText(new Offset(0, 5, moduloStatistic,
        "SW"), "# =-Vergleiche: " + compEqualsCount, "compEqualsStatistic",
        null, textProperties);
    int compLessThanCount = finalS != -1 ? finalS + 1 : 0;
    Text compLessThanStatistic = lang.newText(new Offset(0, 5,
        compEqualsStatistic, "SW"), "# <-Vergleiche: " + compLessThanCount,
        "compLessThanStatistic", null, textProperties);
    int arrayAccessCount = (finalI != -1 ? 2 * finalI : 0) + charCompCount * 2
        + (finalS != -1 ? 2 * finalS : 0);
    Text arrayAccessStatistic = lang.newText(new Offset(0, 5,
        compLessThanStatistic, "SW"), "# Array-Zugriffe: " + arrayAccessCount,
        "arrayAccessStatistic", null, textProperties);
    int addCount = (finalI != -1 ? 3 * finalI : 0)
        + (finalS != -1 ? (5 * finalS + 2) : 0); // includes increment of
                                                 // indices
    Text addStatistic = lang.newText(new Offset(0, 5, arrayAccessStatistic,
        "SW"), "# Additionen: " + addCount, "addStatistic", null,
        textProperties);
    int subCount = 2 + (finalS != -1 ? 2 * finalS : 0); // one subtraction is
                                                        // n-m in the for loop
    Text subStatistic = lang.newText(new Offset(0, 5, addStatistic, "SW"),
        "# Subtraktionen: " + subCount, "subStatistic", null, textProperties);
    int multCount = (finalI != -1 ? 2 * finalI : 0)
        + (finalS != -1 ? 2 * finalS : 0);
    Text multStatistic = lang.newText(new Offset(0, 5, subStatistic, "SW"),
        "# Multiplikationen: " + multCount, "multStatistic", null,
        textProperties);
    Text expStatistic = lang.newText(new Offset(0, 5, multStatistic, "SW"),
        "# Exponentationen: 1", "expStatistic", null, textProperties);
    Text lengthOpStatistic = lang.newText(new Offset(0, 5, expStatistic, "SW"),
        "# Längen-Operationen: 2", "lengthOpStatistic", null, textProperties);
    lang.newText(new Offset(0, 5, lengthOpStatistic, "SW"),
        "# Ausgabe-Operationen: " + matches.size(), "printOpStatistic", null,
        textProperties);

  }

  private void hideAllBut() {
    lang.addLine("hideAll");
    for (Primitive p : neverHideList) {
      p.show();
    }
  }

  public String getName() {
    return "Rabin-Karp-Algorithm";
  }

  public String getAlgorithmName() {
    return "Rabin-Karp";
  }

  public String getAnimationAuthor() {
    return "Thomas Schlosser";
  }

  public String getDescription() {
    return "Der 'Rabin-Karp-Algorithm' sucht innerhalb einer Zeichenkette nach jedem Vorkommen eines bestimmten fixen Musters. "
        + "\n"
        + "Durch die Verwendung einer Hashfunktion in einem Vorvergleich wird die Dauer eines Vergleichs zwischen dem Muster "
        + "\n"
        + "und den einzelnen Teilen der Zeichenkette stark verringert. Erst wenn der Vorvergleich beim Iterieren über die"
        + "\n"
        + "Zeichenkette eine mögliche Übereinstimmung feststellt, wird ein zeichenweiser Vergleich zwischen dem Muster"
        + "\n"
        + "und der aktuellen Teil-Zeichenkette vorgenommen. Die Hashwerte lassen sich durch die Verwendung einer rollende"
        + "\n" + "Hashfunktion in konstanter Zeit berechnen.";
  }

  public String getCodeExample() {
    return "RABIN-KARP-MATCHER(T,P,d,q)\n"
        + "    n := länge[T]\n"
        + "    m := länge[P]\n"
        + "    h := d^(m-1) mod q\n"
        + "    p := 0\n"
        + "    t := 0\n"
        + "    for i := 1 to m do\n"
        + "        p := (dp + P[i]) mod q\n"
        + "        t := (dt + T[i]) mod q\n"
        + "    for s := 0 to n-m do\n"
        + "        if p=t then\n"
        + "            if P[1..m]=T[s+1..s+m] then\n"
        + "                print 'Muster tritt mit der Verschiebung' s 'auf.'\n"
        + "        if s < n-m then\n"
        + "            t := (d(t-T[s+1]h)+T[s+m+1]) mod q\n";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}