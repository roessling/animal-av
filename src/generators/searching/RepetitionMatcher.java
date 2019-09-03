package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
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
import algoanim.properties.RectProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Thomas Schlosser
 * 
 */
public class RepetitionMatcher implements Generator {
  private Language                 lang;

  private final static TicksTiming ticks20                            = new TicksTiming(
                                                                          20);
  private final static TicksTiming ticks40                            = new TicksTiming(
                                                                          40);

  private final static int         AVG_WIDTH_SANS_SERIF_CHARACTER_S16 = 9;

  private final static int         WIDTH_ARRAYELEMENT                 = 12;

  private TextProperties           titleProperties;
  private RectProperties           titleBoxProperties;
  private TextProperties           subTitleProperties;
  private TextProperties           textProperties;
  private ArrayProperties          charArrayProperties;
  private SourceCodeProperties     sourceProperties;
  private RectProperties           sourceBoxProperties;
  private SourceCodeProperties     subSourceProperties;
  private RectProperties           subSourceBoxProperties;
  private ArrayProperties          variablesArrayProperties;
  private ArrayMarkerProperties    sMarkerProperties;
  private ArrayMarkerProperties    nmMarkerProperties;
  private PolylineProperties       selfmadeArrayMarkerLineProperties;
  private PolylineProperties       selfmadeArrayMarkerLineDeactivatedProperties;
  private TextProperties           selfMadeArrayMarkerTextProperties;
  private RectProperties           compTrueRectProperties;
  private RectProperties           compFalseRectProperties;
  private TextProperties           compTextProperties;
  private TextProperties           maxCalcTextProperties;
  private PolylineProperties       maxCalcLineProperties;

  private List<Primitive>          neverHideList;
  private Rect                     hRect;
  private StringArray              variablesDesc;
  private StringArray              variablesName;
  private StringArray              variablesValues;
  private SourceCode               source;
  private Primitive                outputOffsetRef;
  private int                      outputNextYOffset;

  private Polyline                 selfMadeArrayMarkerLine;
  private Polyline                 selfMadeArrayMarkerLineDeactivated;
  private Text                     selfMadeArrayMarkerText;

  // variables for statistics
  private List<Integer>            matches;
  private int                      charCompCount;
  private int                      numLoop;
  private int                      numIf1;
  private int                      numIf2;

  public void init() {
    lang = new AnimalScript("Repetition-Matcher", "Thomas Schlosser", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    String pattern = (String) primitives.get("Muster (P)");
    String text = (String) primitives.get("Text (T)");

    compTrueRectProperties = (RectProperties) props
        .getPropertiesByName("Zeichenvergleich (wahr)");
    charArrayProperties = (ArrayProperties) props
        .getPropertiesByName("Textarray");
    compFalseRectProperties = (RectProperties) props
        .getPropertiesByName("Zeichenvergleich (falsch)");
    titleProperties = (TextProperties) props.getPropertiesByName("Überschrift");
    selfMadeArrayMarkerTextProperties = (TextProperties) props
        .getPropertiesByName("Text des Zeigers auf Muster");
    variablesArrayProperties = (ArrayProperties) props
        .getPropertiesByName("Variablen");
    maxCalcTextProperties = (TextProperties) props
        .getPropertiesByName("Text der Maximum-Berechnung");
    selfmadeArrayMarkerLineProperties = (PolylineProperties) props
        .getPropertiesByName("Zeiger auf Muster");
    subSourceProperties = (SourceCodeProperties) props
        .getPropertiesByName("Quellcode der Subprozedur");
    subTitleProperties = (TextProperties) props
        .getPropertiesByName("Unterüberschriften");
    subSourceBoxProperties = (RectProperties) props
        .getPropertiesByName("Box des Quellcodes der Subprozedur");
    selfmadeArrayMarkerLineDeactivatedProperties = (PolylineProperties) props
        .getPropertiesByName("Deaktivierter Zeiger auf Muster");
    sMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("Zeiger auf Text");
    nmMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("Zeiger auf 'n-m' Position");
    maxCalcLineProperties = (PolylineProperties) props
        .getPropertiesByName("Linie der Maximum-Berechnung");
    titleBoxProperties = (RectProperties) props
        .getPropertiesByName("Box der Überschrift");
    compTextProperties = (TextProperties) props
        .getPropertiesByName("Zeichenvergleichstext");
    sourceProperties = (SourceCodeProperties) props
        .getPropertiesByName("Quellcode");
    sourceBoxProperties = (RectProperties) props
        .getPropertiesByName("Box des Quellcodes");

    // fix missing configurability of current XML-Format definition (especially
    // 'bold' and 'font-size'!=12)
    titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    subTitleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SERIF, Font.BOLD, 16));
    variablesArrayProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(Font.MONOSPACED, Font.BOLD, 12));
    sourceProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    subSourceProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    selfMadeArrayMarkerTextProperties.set(
        AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
            Font.PLAIN, 16));
    maxCalcTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        sourceProperties.get(AnimationPropertiesKeys.FONT_PROPERTY));

    textProperties = new TextProperties("textProperties");
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 14));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // reset global variables
    neverHideList = new ArrayList<Primitive>();
    hRect = null;
    variablesDesc = null;
    variablesName = null;
    variablesValues = null;
    source = null;
    outputOffsetRef = null;
    outputNextYOffset = 0;

    selfMadeArrayMarkerLine = null;
    selfMadeArrayMarkerLineDeactivated = null;
    selfMadeArrayMarkerText = null;

    // (p)reset variables for statistics
    matches = new ArrayList<Integer>();
    charCompCount = 0;
    numLoop = 0;
    numIf1 = 0;
    numIf2 = 0;

    matcher(pattern.toCharArray(), text.toCharArray());

    return lang.toString();
  }

  public void matcher(char[] pattern, char[] text) {
    Text header = lang.newText(new Coordinates(350, 30), "Repetition-Matcher",
        "header", null, titleProperties);
    neverHideList.add(header);
    hRect = lang.newRect(new Offset(-335, -5, header, "NW"), new Offset(335, 5,
        header, "SE"), "hRect", null, titleBoxProperties);
    neverHideList.add(hRect);

    Text subTitle1 = lang.newText(new Offset(0, 15, hRect, "SW"),
        "Beschreibung", "subTitle1", null, subTitleProperties);
    Text description1 = lang
        .newText(
            new Offset(0, 5, subTitle1, "SW"),
            "Der 'Repetition-Matcher' sucht innerhalb einer Zeichenkette nach jedem Vorkommen eines bestimmten fixen Musters.",
            "description1", null, textProperties);
    Text description2 = lang
        .newText(
            new Offset(0, 0, description1, "SW"),
            "Dazu iteriert er einmal über die Zeichenkette und beginnt an der aktuellen Position die Übereinstimmung mit dem",
            "description2", null, textProperties);
    Text description3 = lang
        .newText(
            new Offset(0, 0, description2, "SW"),
            "Muster zu prüfen. Dies geschieht so lange, bis er von dieser Position ausgehend eine Übereinstimmung mit dem",
            "description3", null, textProperties);
    Text description4 = lang
        .newText(
            new Offset(0, 0, description3, "SW"),
            "kompletten Muster gefunden hat, oder eines der Folgezeichen nicht mit dem Muster übereinstimmt. In beiden Fällen",
            "description4", null, textProperties);
    Text description5 = lang
        .newText(
            new Offset(0, 0, description4, "SW"),
            "wird anschließend, um unnötige Vergleiche einzusparen, die aktuelle Position in der Zeichenkette um einen",
            "description5", null, textProperties);
    Text description6 = lang
        .newText(
            new Offset(0, 0, description5, "SW"),
            "variierenden Wert (>=1) erhöht. Dieser Wert basiert beim 'Repetition-Matcher' auf einer Kombination von",
            "description6", null, textProperties);
    Text description7 = lang
        .newText(
            new Offset(0, 0, description6, "SW"),
            "Wiederholungen im Muster und der Länge (q) des an der aktuellen Position übereinstimmenden (Teil-)Musters. Die",
            "description7", null, textProperties);
    Text description8 = lang
        .newText(
            new Offset(0, 0, description7, "SW"),
            "Verschiebung ergibt sich aus dem aufgerundeten Verhältnis zwischen der Länge q  und dem aus den Wiederholungen",
            "description8", null, textProperties);
    Text description9 = lang
        .newText(
            new Offset(0, 0, description8, "SW"),
            "im Muster bestimmt Wert k (siehe unten). Damit zu jedem Zeitpunkt einer Verschiebung der Position stattfindet,",
            "description9", null, textProperties);
    Text description10 = lang.newText(new Offset(0, 0, description9, "SW"),
        "wird diese mindestens um den Wert 1 erhöht.", "description10", null,
        textProperties);
    Text description11 = lang
        .newText(
            new Offset(0, 5, description10, "SW"),
            "Grundlage zur Bestimmung des Wertes k stellen der Wiederholungsfaktor r und die daraus abgeleiteten Funktionen",
            "description11", null, textProperties);
    Text description12 = lang
        .newText(
            new Offset(0, 0, description11, "SW"),
            "w und w* dar. Eine Zeichenkette x besitzt den Wiederholungsfaktor r, wenn für eine beliebige Zeichenkette y und",
            "description12", null, textProperties);
    Text description13 = lang
        .newText(
            new Offset(0, 0, description12, "SW"),
            "ein beliebiges r>0 die Gleichung x=y^r erfüllt ist. Die Funktion w(x) bestimmt das größte r, für das x einen",
            "description13", null, textProperties);
    Text description14 = lang
        .newText(
            new Offset(0, 0, description13, "SW"),
            "Wiederholungsfaktor r hat - das heißt den maximalen Wiederholungsfaktor. Das Ergebnis der w*-Funktion (auf das",
            "description14", null, textProperties);
    Text description15 = lang
        .newText(
            new Offset(0, 0, description14, "SW"),
            "Muster angewendet) wird durch das Maximum der Anwendung der w-Funktion auf alle Präfixe des Musters bestimmt.",
            "description15", null, textProperties);
    Text description15_1 = lang.newText(new Offset(0, 0, description15, "SW"),
        "w* ist somit definiert als:", "description15_1", null, textProperties);
    Text description16 = lang.newText(new Offset(0, 5, description15_1, "SW"),
        "   - w*(P) = max({w(P[1..i]) | 1 <= i <= länge[P]})", "description16",
        null, textProperties);
    Text description17 = lang
        .newText(
            new Offset(0, 15, description16, "SW"),
            "Basierend auf der w*-Funktion lässt sich der Wert k, der zur Berechnung der nächsten Position benötigt wird wie folgt",
            "description17", null, textProperties);
    Text description17_1 = lang.newText(new Offset(0, 0, description17, "SW"),
        "berechnen:", "description17_1", null, textProperties);
    Text description18 = lang.newText(new Offset(0, 5, description17_1, "SW"),
        "   - k = 1 + w*(P)", "description18", null, textProperties);
    Text description19 = lang
        .newText(
            new Offset(0, 5, description18, "SW"),
            "Da k lediglich von P abhängig ist, muss dieser Wert nur ein einziges Mal berechnet werden. Dadurch ist der",
            "description19", null, textProperties);
    Text description20 = lang
        .newText(
            new Offset(0, 0, description19, "SW"),
            "'Repetition-Matcher' - sogar ohne fortlaufende komplexe Berechnungen - in der Lage, gewisse Positionen in",
            "description20", null, textProperties);
    Text description21 = lang
        .newText(
            new Offset(0, 0, description20, "SW"),
            "der Zeichenkette zu überspringen und aufwändige Zeichen-Vergleiche einzusparen.",
            "description21", null, textProperties);

    Text subTitle2 = lang.newText(new Offset(0, 20, description21, "SW"),
        "Parameter", "subTitle2", null, subTitleProperties);
    Text param1 = lang.newText(new Offset(0, 5, subTitle2, "SW"),
        "P: Muster, nach dem in der Zeichenkette gesucht werden soll",
        "param1", null, textProperties);
    Text param2 = lang
        .newText(
            new Offset(0, 0, param1, "SW"),
            "T: Zeichenkette (Text), in der alle Vorkommnisse des Musters gefunden werden sollen",
            "param2", null, textProperties);

    Text subTitle3 = lang.newText(new Offset(0, 20, param2, "SW"),
        "Weitere Variablen", "subTitle3", null, subTitleProperties);
    Text descVar1 = lang.newText(new Offset(0, 5, subTitle3, "SW"),
        "m: Länge des Musters P", "descVar1", null, textProperties);
    Text descVar2 = lang.newText(new Offset(0, 0, descVar1, "SW"),
        "n: Länge der Zeichenkette T", "descVar2", null, textProperties);
    Text descVar3 = lang.newText(new Offset(0, 0, descVar2, "SW"),
        "k: Maximaler Wiederholungsfaktor innerhalb aller Muster-Präfixe",
        "descVar3", null, textProperties);
    Text descVar4 = lang.newText(new Offset(0, 0, descVar3, "SW"),
        "q: Index zur Iteration über das (partiell) matchende Muster",
        "descVar4", null, textProperties);
    Text descVar5 = lang.newText(new Offset(0, 0, descVar4, "SW"),
        "s: Index zur Iteration über die Zeichenkette", "descVar5", null,
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
    description7.hide();
    description8.hide();
    description9.hide();
    description10.hide();
    description11.hide();
    description12.hide();
    description13.hide();
    description14.hide();
    description15.hide();
    description15_1.hide();
    description16.hide();
    description17.hide();
    description17_1.hide();
    description18.hide();
    description19.hide();
    description20.hide();
    description21.hide();

    param1.hide();
    param2.hide();

    descVar1.hide();
    descVar2.hide();
    descVar3.hide();
    descVar4.hide();
    descVar5.hide();

    Text textLabel = lang.newText(new Offset(10, 65, hRect, "SW"), "Text",
        "textLabel", null, subTitleProperties);
    StringArray textStringArray = lang.newStringArray(new Offset(35, 0,
        textLabel, "NE"), new String(text).split("(?<!^)"), "textStringArray",
        null, charArrayProperties);
    Text patternLabel = lang.newText(new Offset(0, 42, textLabel,
        "baseline start"), "Muster", "patternLabel", null, subTitleProperties);
    StringArray patternStringArray = lang.newStringArray(new Offset(0, 40,
        textStringArray, "NW"), new String(pattern).split("(?<!^)"),
        "patternStringArray", null, charArrayProperties);

    Text variablesLabel = lang.newText(new Offset(0, 50, patternLabel, "SW"),
        "Weitere Variablen", "variablesLabel", null, subTitleProperties);
    variablesDesc = lang.newStringArray(
        new Offset(0, 10, variablesLabel, "SW"), new String[] {
            " Beschreibung ", "  Musterlänge  ", "  Textlänge  ",
            "  Wiederholungsfaktor  ", "  Pattern-Index  ", "  Text-Index  " },
        "variablesDesc", null, variablesArrayProperties);
    String[] varNameArray = new String[] { getVariableValueString(0, "Name"),
        getVariableValueString(1, "m"), getVariableValueString(2, "n"),
        getVariableValueString(3, "k"), getVariableValueString(4, "q"),
        getVariableValueString(5, "s") };
    variablesName = lang.newStringArray(new Offset(0, 0, variablesDesc, "SW"),
        varNameArray, "variablesName", null, variablesArrayProperties);
    String[] varValueArray = new String[] { getVariableValueString(0, "Wert"),
        getVariableValueString(1, "-"), getVariableValueString(2, "-"),
        getVariableValueString(3, "-"), getVariableValueString(4, "-"),
        getVariableValueString(5, "-") };
    variablesValues = lang.newStringArray(
        new Offset(0, 0, variablesName, "SW"), varValueArray,
        "variablesValues", null, variablesArrayProperties);

    source = lang.newSourceCode(new Offset(0, 30, variablesValues, "SW"),
        "source", null, sourceProperties);
    source.addCodeLine("REPETITION-MATCHER(P,T)", null, 0, null);
    source.addCodeLine("m := länge[P]", null, 1, null);
    source.addCodeLine("n := länge[T]", null, 1, null);
    source.addCodeLine("k := 1 + w*(P)", null, 1, null);
    source.addCodeLine("q := 0", null, 1, null);
    source.addCodeLine("s := 0", null, 1, null);
    source.addCodeLine("while s := n-m do", null, 1, null);
    source.addCodeLine("if T[s+q+1]=P[q+1] then", null, 2, null);
    source.addCodeLine("q := q + 1", null, 3, null);
    source.addCodeLine("if q=m then", null, 3, null);
    source.addCodeLine("print 'Muster tritt mit der Verschiebung' s 'auf.'",
        null, 4, null);
    source.addCodeLine("if q=m oder T[s+q+1] == P[q+1] then", null, 2, null);
    source.addCodeLine("s := s + max(1, ceil(q/k))", null, 3, null);
    source.addCodeLine("q := 0", null, 3, null);
    lang.newRect(new Offset(-5, -5, source, "NW"), new Offset(5, 5, source,
        "SE"), "sourceRect", null, sourceBoxProperties);

    outputOffsetRef = lang.newText(new Offset(20, 0, source, "NE"), "Ausgabe",
        "outputLabel", null, subTitleProperties);
    outputNextYOffset = 10;

    lang.nextStep();

    matcher(patternStringArray, textStringArray);

    lang.nextStep();

    hideAllBut();
    showStatistics();
  }

  private void matcher(StringArray pattern, StringArray text) {
    source.highlight(1);

    lang.nextStep("Initialisierung");

    int m = pattern.getLength();
    putVariable(1, m);
    source.unhighlight(1);
    source.highlight(2, 0, false, ticks20, null);

    lang.nextStep();

    int n = text.getLength();
    putVariable(2, n);
    source.unhighlight(2);
    source.highlight(3, 0, false, ticks20, null);

    ArrayMarker nmMarker = lang.newArrayMarker(text, n - m, "nmMarker", null,
        nmMarkerProperties);

    int k = 1 + roStar(pattern);

    putVariable(3, k);
    source.unhighlight(3);
    source.highlight(4, 0, false, ticks20, null);

    lang.nextStep();

    int q = 0;
    putVariable(4, q);
    source.unhighlight(4);
    source.highlight(5, 0, false, ticks20, null);
    createNewSelfmadeArrayMarker(pattern, q);

    lang.nextStep();

    int s = 0;
    putVariable(5, s);
    source.unhighlight(5);
    source.highlight(6, 0, false, ticks20, null);
    ArrayMarker markerText = lang.newArrayMarker(text, s, "s" + s, null,
        sMarkerProperties);

    while (s <= n - m) {
      // for statistics
      numLoop++;

      lang.nextStep(numLoop + ". Iteration");

      source.unhighlight(6);
      source.highlight(7, 0, false, ticks20, null);

      boolean charComp = text.getData(s + q).equals(pattern.getData(q));

      Rect compRect = lang.newRect(getSWOfArrayElement(text, s + q),
          getSWPlusYOffsetOfArrayElement(text, s + q + 1, 20), "compEqualRect",
          null, (charComp ? compTrueRectProperties : compFalseRectProperties));
      Text compText = lang.newText(
          getSWPlusOffsetsOfArrayElement(text, s + q, 2, 2), "=",
          "compEqualText", null, compTextProperties);

      lang.nextStep();
      compRect.hide();
      compText.hide();

      source.unhighlight(7);

      // for statistics
      charCompCount++;
      if (charComp) {
        // for statistics
        numIf1++;

        source.highlight(8, 0, false, ticks20, null);

        lang.nextStep();

        q = q + 1;
        putVariable(4, q);
        moveSelfmadeArrayMarker(1);
        if (q == pattern.getLength()) {
          selfMadeArrayMarkerLineDeactivated.show(ticks40);
          selfMadeArrayMarkerText.setText(" q=m", ticks40, null);
        }
        source.unhighlight(8);
        source.highlight(9, 0, false, ticks20, null);

        lang.nextStep();

        source.unhighlight(9);

        if (q == m) {
          matches.add(s);

          source.highlight(10, 0, false, ticks20, null);

          lang.nextStep(matches.size() + ". Treffer");

          outputOffsetRef = lang.newText(new Offset(0, outputNextYOffset,
              outputOffsetRef, "SW"), "Muster tritt mit der Verschiebung " + s
              + " auf.", "output" + s, null, textProperties);
          outputNextYOffset = 0;

          source.unhighlight(10);
        }
      }

      source.highlight(11, 0, false, ticks20, null);

      if (q != m) {
        // for statistics
        charCompCount++;
        // check charComp with current s and q
        charComp = text.getData(s + q).equals(pattern.getData(q));

        compRect = lang.newRect(getSWOfArrayElement(text, s + q),
            getSWPlusYOffsetOfArrayElement(text, s + q + 1, 20),
            "compUnequalRect", null, (!charComp ? compTrueRectProperties
                : compFalseRectProperties));
        compText = lang.newText(
            getSWPlusOffsetsOfArrayElement(text, s + q, 2, 2), "!=",
            "compUnequalText", null, compTextProperties);
      }

      lang.nextStep();

      if (q != m) {
        compRect.hide();
        compText.hide();
      }

      source.unhighlight(11);

      if (q == m || !charComp) {
        // for statistics
        numIf2++;

        source.highlight(12, 0, false, ticks20, null);
        int sOffset = (int) Math.max(1, Math.ceil((float) q / k));

        Text maxCalcHintText = lang.newText(getMaxCalcHintOffset(), "max(1,"
            + (int) Math.ceil((float) q / k) + ")=" + sOffset,
            "maxCalcHintText", ticks20, maxCalcTextProperties);
        Node nodeStart = getMaxCalcLineStartOffset();
        Node nodeEnd = getMaxCalcLineEndOffset();
        ;
        Polyline maxCalcHintLine = lang.newPolyline(new Node[] { nodeStart,
            nodeEnd }, "maxCalcLine", ticks20, maxCalcLineProperties);

        lang.nextStep();
        maxCalcHintText.hide();
        maxCalcHintLine.hide();

        s = s + sOffset;
        putVariable(5, s);
        markerText.move(s, null, ticks40);
        if (s > n - m)
          markerText.changeColor(null, Color.RED, ticks40, null);
        movePattern(pattern, sOffset);
        for (int tmp = 0; tmp < sOffset; tmp++)
          if (!matchedCharacter(s - sOffset + tmp, m))
            text.highlightCell(s - sOffset + tmp, null, ticks40);

        source.unhighlight(12);
        source.highlight(13, 0, false, ticks20, null);

        lang.nextStep();

        moveSelfmadeArrayMarker(-q);
        selfMadeArrayMarkerLineDeactivated.hide();
        selfMadeArrayMarkerText.setText("(q+1)", null, null);
        q = 0;
        putVariable(4, q);
        source.unhighlight(13);
      }
      source.highlight(6, 0, false, ticks20, null);
    }

    lang.nextStep();

    // clean up markers
    if (markerText != null)
      markerText.hide();
    hideSelfmadeArrayMarker();
    nmMarker.hide();

    source.unhighlight(6);

    for (int tmp = n - m + 1; tmp < n; tmp++)
      if (!matchedCharacter(tmp, m))
        text.highlightCell(tmp, null, null);
  }

  /**
   * Calculates: max ro(P_i) | 1<=i<=m.
   */
  private int roStar(StringArray array) {
    SourceCode subSource = lang.newSourceCode(
        new Offset(450, 25, source, "NW"), "subSource", ticks20,
        subSourceProperties);
    subSource.addCodeLine("= 1 + max({(P[1..i]) | 1 <= i <= m })", null, 0,
        null);
    Rect subSourceRect = lang.newRect(new Offset(-5, -5, subSource, "NW"),
        new Offset(5, 5, subSource, "SE"), "subSourceRect", ticks20,
        subSourceBoxProperties);
    Node startNode = new Offset(125, 64, source, "NW");
    Polyline p1 = lang.newPolyline(new Node[] { startNode,
        new Offset(0, 0, subSourceRect, "NW") }, "subSourceLine1", ticks20);
    startNode = new Offset(125, 74, source, "NW");
    Polyline p2 = lang.newPolyline(new Node[] { startNode,
        new Offset(0, 0, subSourceRect, "SW") }, "subSourceLine1", ticks20);

    subSource.highlight(0);
    lang.nextStep("w*-Berechnung");

    subSourceRect.hide();
    p2.hide();
    subSource.addCodeLine("= 1 + max(", null, 0, null);
    int max = 0;
    int maxLine = 0;
    for (int i = 1; i <= array.getLength(); i++) {
      char[] prefix = getPrefix(array, i);
      int ro = ro(prefix);
      subSource.addCodeLine("w('" + String.valueOf(prefix) + "')=" + ro, null,
          2, null);
      subSource.addCodeElement("(da '" + String.valueOf(prefix) + "'='"
          + String.valueOf(getPrefix(prefix, prefix.length / ro)) + "'^" + ro
          + ")", null, false, 0, null);
      if (ro > max) {
        maxLine = i - 1;
        max = ro;
      }
    }
    subSource.addCodeLine(")", null, 0, null);
    subSource.highlight(maxLine + 2, 0, true);
    subSource.unhighlight(0);
    subSource.highlight(1);
    subSource.highlight(2 + array.getLength());

    subSourceRect = lang.newRect(new Offset(-5, -5, subSource, "NW"),
        new Offset(5, 5, subSource, "SE"), "subSourceRect", null,
        sourceBoxProperties);
    p2 = lang.newPolyline(new Node[] { startNode,
        new Offset(0, 0, subSourceRect, "SW") }, "subSourceLine1", null);

    lang.nextStep();

    subSourceRect.hide();
    p2.hide();

    subSource.addCodeLine("= " + (max + 1), null, 0, null);
    subSource.unhighlight(maxLine + 2);
    subSource.unhighlight(1);
    subSource.unhighlight(2 + array.getLength());
    subSource.highlight(3 + array.getLength());

    subSourceRect = lang.newRect(new Offset(-5, -5, subSource, "NW"),
        new Offset(5, 5, subSource, "SE"), "subSourceRect", null,
        sourceBoxProperties);
    p2 = lang.newPolyline(new Node[] { startNode,
        new Offset(0, 0, subSourceRect, "SW") }, "subSourceLine1", null);

    lang.nextStep();

    subSource.hide();
    subSourceRect.hide();
    p1.hide();
    p2.hide();

    return max;
  }

  /**
   * Calculates repetition factor r>0 for some string y. x = y^r. ro(x) denotes
   * the largest r such that x has repetition factor r.
   * 
   * @return repetition factor r
   */
  private int ro(char[] x) {
    Integer[] divisors = getDivisors(x.length);
    for (int div : divisors) {
      int repFactor = getRepetitionFactor(getPrefix(x, div), x);
      if (repFactor != -1) {
        return repFactor;
      }
    }
    return 1;
  }

  private int getRepetitionFactor(char[] y, char[] x) {
    int factor = x.length / y.length;
    for (int i = 0; i < factor; i++)
      for (int j = 0; j < y.length; j++)
        if (y[j] != x[i * y.length + j])
          return -1;
    return factor;
  }

  private char[] getPrefix(StringArray array, int length) {// String[] array,
                                                           // int length){
    char[] result = new char[length];
    for (int i = 0; i < length; i++)
      result[i] = array.getData(i).charAt(0);
    return result;
  }

  private char[] getPrefix(char[] array, int length) {
    char[] result = new char[length];
    for (int i = 0; i < length; i++)
      result[i] = array[i];
    return result;
  }

  private Integer[] getDivisors(int n) {
    List<Integer> result = new ArrayList<Integer>();
    for (int i = 1; i <= n / 2; i++)
      if (n % i == 0)
        result.add(i);
    result.add(n);
    return result.toArray(new Integer[0]);
  }

  private String getVariableValueString(int index, String value) {
    StringBuffer sbPre = new StringBuffer();
    StringBuffer sbPost = new StringBuffer();
    for (int i = 0; i < (variablesDesc.getData(index).length() - value.length()) / 2; i++) {
      sbPre.append(' ');
      sbPost.append(' ');
    }
    if (1 == (variablesDesc.getData(index).length() - value.length()) % 2)
      sbPost.append(' ');
    return sbPre.toString() + value + sbPost.toString();
  }

  private String getVariableValueString(int index, int value) {
    return getVariableValueString(index, String.valueOf(value));
  }

  private void putVariable(int index, int value) {
    variablesValues
        .put(index, getVariableValueString(index, value), null, null);
  }

  private void createNewSelfmadeArrayMarker(StringArray array, int index) {
    int yOffset = 20;
    Offset nodeTop = getSOfArrayElement(array, index);
    Node nodeBottom = getSPlusYOffsetOfArrayElement(array, index, yOffset);
    selfMadeArrayMarkerLine = lang.newPolyline(
        new Node[] { nodeBottom, nodeTop }, "qArrayMarker", ticks20,
        selfmadeArrayMarkerLineProperties);
    nodeTop = getSOfArrayElement(array, array.getLength());
    nodeBottom = getSPlusYOffsetOfArrayElement(array, array.getLength(),
        yOffset);
    selfMadeArrayMarkerLineDeactivated = lang.newPolyline(new Node[] {
        nodeBottom, nodeTop }, "qArrayMarkerDeactivated", null,
        selfmadeArrayMarkerLineDeactivatedProperties);
    selfMadeArrayMarkerLineDeactivated.hide();
    Offset labelOffset = getNWSelfmadeArrayMarkerLabelOfArrayElement(array,
        index, yOffset + 3);
    selfMadeArrayMarkerText = lang.newText(labelOffset, "(q+1)",
        "qArrayMarkerText", ticks20, selfMadeArrayMarkerTextProperties);
  }

  private void moveSelfmadeArrayMarker(int posOffset) {
    selfMadeArrayMarkerLine.moveBy("translate", posOffset * WIDTH_ARRAYELEMENT,
        0, null, ticks40);
    selfMadeArrayMarkerText.moveBy("translate", posOffset * WIDTH_ARRAYELEMENT,
        0, null, ticks40);
  }

  private void hideSelfmadeArrayMarker() {
    selfMadeArrayMarkerLine.hide();
    selfMadeArrayMarkerText.hide();
  }

  private void movePattern(StringArray array, int posOffset) {
    array.moveBy("translate", posOffset * WIDTH_ARRAYELEMENT, 0, null, ticks40);
    selfMadeArrayMarkerLineDeactivated.moveBy("translate", posOffset
        * WIDTH_ARRAYELEMENT, 0, null, ticks40);
    moveSelfmadeArrayMarker(posOffset);
  }

  private boolean matchedCharacter(int pos, int patternLength) {
    for (Integer m : matches)
      if (pos >= m && pos < m + patternLength)
        return true;
    return false;
  }

  private Offset getSWOfArrayElement(StringArray array, int index) {
    int xOffset = WIDTH_ARRAYELEMENT * index;
    return new Offset(xOffset - 3, 5, array.getName() + "[0]", "SW"); // - fix
                                                                      // offset
  }

  private Offset getSOfArrayElement(StringArray array, int index) {
    int xOffset = WIDTH_ARRAYELEMENT * index + WIDTH_ARRAYELEMENT / 2;
    return new Offset(xOffset - 3, 5, array.getName() + "[0]", "SW"); // - fix
                                                                      // offset
  }

  private Offset getSWPlusYOffsetOfArrayElement(StringArray array, int index,
      int yOffset) {
    return getSWPlusOffsetsOfArrayElement(array, index, 0, yOffset);
  }

  private Offset getSWPlusOffsetsOfArrayElement(StringArray array, int index,
      int xOffset, int yOffset) {
    int xOffset2 = WIDTH_ARRAYELEMENT * index;
    return new Offset(xOffset + xOffset2 - 3, 5 + yOffset, array.getName()
        + "[0]", "SW"); // - fix offset
  }

  private Offset getSPlusYOffsetOfArrayElement(StringArray array, int index,
      int yOffset) {
    int xOffset = WIDTH_ARRAYELEMENT * index + WIDTH_ARRAYELEMENT / 2;
    return new Offset(xOffset - 3, 5 + yOffset, array.getName() + "[0]", "SW"); // -
                                                                                // fix
                                                                                // offset
  }

  private Offset getNWSelfmadeArrayMarkerLabelOfArrayElement(StringArray array,
      int index, int yOffset) {
    int xOffset = WIDTH_ARRAYELEMENT * index + WIDTH_ARRAYELEMENT / 2
        - (AVG_WIDTH_SANS_SERIF_CHARACTER_S16 * 5) / 2;
    return new Offset(xOffset - 3, 5 + yOffset, array.getName() + "[0]", "SW"); // -
                                                                                // fix
                                                                                // offset
  }

  private Offset getMaxCalcLineStartOffset() {
    return new Offset(220, 249, source, "NW");
  }

  private Offset getMaxCalcLineEndOffset() {
    return new Offset(305, 249, source, "NW");
  }

  private Offset getMaxCalcHintOffset() {
    return new Offset(315, 243, source, "NW");
  }

  private void showStatistics() {
    Text overviewTitle1 = lang.newText(new Offset(0, 15, hRect, "SW"),
        "Abschließende Übersicht", "overviewTitle1", null, subTitleProperties);
    Text overviewDescription1 = lang
        .newText(
            new Offset(0, 5, overviewTitle1, "SW"),
            "Der soeben animierte Repetition-Matcher weist folgende Statistiken auf:",
            "overviewDescription1", null, textProperties);

    Text matchesStatistic = lang.newText(new Offset(0, 15,
        overviewDescription1, "SW"), "# Treffer: " + matches.size(),
        "matchesStatistic", null, textProperties);

    Text charCompStatistic = lang.newText(new Offset(0, 15, matchesStatistic,
        "SW"), "# Zeichenvergleiche: " + charCompCount, "charCompStatistic",
        null, textProperties);

    Text overviewDescription2 = lang
        .newText(
            new Offset(0, 30, charCompStatistic, "SW"),
            "Die Laufzeit des soeben animierten Repetition-Matchers lässt sich anhand folgender Werte festmachen:",
            "overviewDescription2", null, textProperties);
    int statementsCount = 6 + numLoop * 3 + numIf1 * 2 + matches.size()
        + numIf2 * 2;
    Text statementsStatistic = lang.newText(new Offset(0, 15,
        overviewDescription2, "SW"), "# ausgeführter Anweisungen (Zeilen): "
        + statementsCount, "statementsStatistic", null, textProperties);
    int assignmentCount = 5 + numIf1 + 2 * numIf2;
    Text assignmentsStatistic = lang.newText(new Offset(0, 5,
        statementsStatistic, "SW"), "# Zuweisungen: " + assignmentCount,
        "assignmentsStatistic", null, textProperties);
    int maxCount = numIf2;
    Text maxStatistic = lang.newText(new Offset(0, 5, assignmentsStatistic,
        "SW"), "# Max-Operationen: " + maxCount, "maxStatistic", null,
        textProperties);
    int compEqualsCount = numLoop * 2 + numIf1;
    Text compEqualsStatistic = lang.newText(
        new Offset(0, 5, maxStatistic, "SW"), "# =-Vergleiche: "
            + compEqualsCount, "compEqualsStatistic", null, textProperties);
    int compUnequalsCount = charCompCount - numLoop;
    Text compUnequalsStatistic = lang.newText(new Offset(0, 5,
        compEqualsStatistic, "SW"), "# !=-Vergleiche: " + compUnequalsCount,
        "compUnequalsStatistic", null, textProperties);
    int compLessEqualCount = 1 + numLoop;
    Text compLessEqualStatistic = lang.newText(new Offset(0, 5,
        compUnequalsStatistic, "SW"), "# <=-Vergleiche: " + compLessEqualCount,
        "compLessEqualStatistic", null, textProperties);
    int arrayAccessCount = charCompCount * 2;
    Text arrayAccessStatistic = lang.newText(new Offset(0, 5,
        compLessEqualStatistic, "SW"), "# Array-Zugriffe: " + arrayAccessCount,
        "arrayAccessStatistic", null, textProperties);
    int addCount = 1 + charCompCount * 3 + numIf1 + numIf2;
    Text addStatistic = lang.newText(new Offset(0, 5, arrayAccessStatistic,
        "SW"), "# Additionen: " + addCount, "addStatistic", null,
        textProperties);
    int subCount = 1; // one subtraction is n-m in the for loop
    Text subStatistic = lang.newText(new Offset(0, 5, addStatistic, "SW"),
        "# Subtraktionen: " + subCount, "subStatistic", null, textProperties);
    int ceilCount = numIf2;
    Text ceilStatistic = lang.newText(new Offset(0, 5, subStatistic, "SW"),
        "# Aufrunde-Operationen: " + ceilCount, "ceilStatistic", null,
        textProperties);
    Text roStarStatistic = lang.newText(new Offset(0, 5, ceilStatistic, "SW"),
        "# w*-Operationen: 1", "roStarStatistic", null, textProperties);
    Text lengthOpStatistic = lang.newText(new Offset(0, 5, roStarStatistic,
        "SW"), "# Längen-Operationen: 2", "lengthOpStatistic", null,
        textProperties);
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
    return "Repetition-Matcher";
  }

  public String getAlgorithmName() {
    return "Repetition-Matcher";
  }

  public String getAnimationAuthor() {
    return "Thomas Schlosser";
  }

  public String getDescription() {
    return "Der 'Repetition-Matcher' sucht innerhalb einer Zeichenkette nach jedem Vorkommen eines bestimmten fixen Musters."
        + "\n<br/>"
        + "Dazu iteriert er einmal über die Zeichenkette und beginnt an der aktuellen Position die Übereinstimmung mit dem"
        + "\n<br/>"
        + "Muster zu prüfen. Dies geschieht so lange, bis er von dieser Position ausgehend eine Übereinstimmung mit dem"
        + "\n<br/>"
        + "kompletten Muster gefunden hat, oder eines der Folgezeichen nicht mit dem Muster übereinstimmt. In beiden Fällen"
        + "\n<br/>"
        + "wird anschließend, um unnötige Vergleiche einzusparen, die aktuelle Position in der Zeichenkette um einen"
        + "\n<br/>"
        + "variierenden Wert (>=1) erhöht. Dieser Wert basiert beim 'Repetition-Matcher' auf einer Kombination von"
        + "\n<br/>"
        + "Wiederholungen im Muster und der Länge (q) des an der aktuellen Position übereinstimmenden (Teil-)Musters. Die"
        + "\n<br/>"
        + "Verschiebung ergibt sich aus dem aufgerundeten Verhältnis zwischen der Länge q  und dem aus den Wiederholungen"
        + "\n<br/>"
        + "im Muster bestimmt Wert k (siehe unten). Damit zu jedem Zeitpunkt einer Verschiebung der Position stattfindet,"
        + "\n<br/>"
        + "wird diese mindestens um den Wert 1 erhöht."
        + "\n<br/>"
        + "\n<br/>"
        + "Grundlage zur Bestimmung des Wertes k stellen der Wiederholungsfaktor r und die daraus abgeleiteten Funktionen"
        + "\n<br/>"
        + "w und w* dar. Eine Zeichenkette x besitzt den Wiederholungsfaktor r, wenn für eine beliebige Zeichenkette y und"
        + "\n<br/>"
        + "ein beliebiges r>0 die Gleichung x=y^r erfüllt ist. Die Funktion w(x) bestimmt das größte r, für das x einen"
        + "\n<br/>"
        + "Wiederholungsfaktor r hat -das heißt den maximalen Wiederholungsfaktor. Das Ergebnis der w*-Funktion (auf das"
        + "\n<br/>"
        + "Muster angewendet) wird durch das Maximum der Anwendung der w-Funktion auf alle Präfixe des Musters bestimmt."
        + "\n<br/>"
        + "w* ist somit definiert als:"
        + "\n<br/>"
        + "   - w*(P) = max({w(P[1..i]) | 1 <= i <= länge[P]})"
        + "\n<br/>"
        + "Basierend auf der w*-Funktion lässt sich der Wert k, der zur Berechnung der nächsten Position benötigt wird wie folgt"
        + "\n<br/>"
        + "berechnen:"
        + "\n<br/>"
        + "   - k = 1 + w*(P)"
        + "\n<br/>"
        + "\n<br/>"
        + "Da k lediglich von P abhängig ist, muss dieser Wert nur ein einziges Mal berechnet werden. Dadurch ist der"
        + "\n<br/>"
        + "'Repetition-Matcher' - sogar ohne fortlaufende komplexe Berechnungen - in der Lage, gewisse Positionen in"
        + "\n<br/>"
        + "der Zeichenkette zu üerspringen und aufwändige Zeichen-Vergleiche einzusparen.";
  }

  public String getCodeExample() {
    return "REPETITION-MATCHER(P,T)" + "\n" + "  m := länge[P]" + "\n"
        + "  n := länge[T]" + "\n" + "  k := 1 + w(P)" + "\n" + "  q := 0"
        + "\n" + "  s := 0" + "\n" + "  while s := n-m do" + "\n"
        + "    if T[s+q+1]=P[q+1] then" + "\n" + "      q := q + 1" + "\n"
        + "      if q=m then" + "\n"
        + "        print 'Muster tritt mit der Verschiebung' s 'auf.'" + "\n"
        + "    if q=m oder T[s+q+1] ==P[q+1] then" + "\n"
        + "      s := s + max(1, ceil(q/k))" + "\n" + "      q := 0";
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