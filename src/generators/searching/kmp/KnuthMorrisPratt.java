package generators.searching.kmp;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.TicksTiming;

public class KnuthMorrisPratt implements Generator {

  private Language              language;

  private ArrayMarkerProperties srcArrayMarkerProperties;
  private ArrayMarkerProperties patArrayMarkerProperties;

  private SourceCodeProperties  sourceCodeProperties;
  private TextProperties        textProperties;
  private ArrayProperties       arrayProperties;
  private RectProperties        rectProperties;

//  private final String          ALGORITHMNAME = "Knuth-Morris-Pratt-Algorithmus";
//  private final String          NAME          = "Knuth-Morris-Pratt";
//  private final String          AUTHOR        = "Roger Ponka";

  private int                   i, j = 0;
  private int[]                 border;
  private int                   n, m;

  // GR for AH
  TwoValueCounter myCounter = null;
  TwoValueView myCounterView = null;
  
  private static final String   DESCRIPTION   = "Der Algorithmus von Knuth, Morris und Pratt vergleicht den Text mit dem Muster, versucht "
                                                  + "aber bei einem Mismatch m&ouml;glichst weit im Text weiterzuspringen."
                                                  + "<br />Das Muster wird so weit nach rechts verschoben, dass die verglichenen Symbole nach dem "
                                                  + "Verschieben wieder in dem Bereich der zu vergleichenden Symbole des Suchtextes enthalten sind."
                                                  + "<p>In einer Vorlaufphase wird eine Tabelle (Verschiebetabelle) dadurch gewonnen, indem das Muster "
                                                  + "analysiert wird und die Informationen &uuml;ber dessen Struktur in einem Array der L&auml;nge <em>m</em> "
                                                  + "(m: L&auml;nge des Musters) gespeichert werden.<br />"
                                                  + "Um die maximale Laufzeit zu verbessern, wird nun die Idee verfolgt, dass fr&uuml;here "
                                                  + "erfolgreiche Vergleiche genutzt werden, um das Suchwort so weit nach rechts zu "
                                                  + "verschieben, ohne m&ouml;gliche Vorkommen des Suchwortes auszulassen. </p><p>"
                                                  + "Da nach einem Mismatch nicht noch einmal alle Symbole wiederholt verglichen werden, "
                                                  + "ben&ouml;tigt der Algorithmus in der Suchphase nur noch <em>O(n)</em> Vergleiche.</p>";

  private static final String   SOURCE_CODE   = "public boolean knuthMorrisPratt(String source, String pattern)\n"
                                                  + "{\n"
                                                  + "   int n = source.length(); \n"
                                                  + "   int m = pattern.length(); \n"
                                                  + "   int border[] = new int[m+1]; \n"
                                                  + "   computeBorders(border, m, pattern); \n"
                                                  + "   int i = 0, j = 0; \n"
                                                  + "   while (i &le n-m) \n"
                                                  + "   { \n"
                                                  + "      while (source.charAt(i+j) == pattern.charAt(j)) \n"
                                                  + "      { \n"
                                                  + "        j++ ; \n"
                                                  + "        if (j == m) return true; // Matching ! \n"
                                                  + "      } \n"
                                                  + "      i = i + j - border[j]; \n"
                                                  + "      j = Math.max(0, border[j]); \n"
                                                  + "   } \n"
                                                  + "   return false; \n"
                                                  + "} \n\n"
                                                  +

                                                  "private void computeBorder(int border[], int m, String pattern) \n"
                                                  + "{\n"
                                                  + "   border[0] = -1; \n"
                                                  + "   int r = border[1] = 0; \n"
                                                  + "   for (int k = 2; j &le m ; j++)\n"
                                                  + "   { \n"
                                                  + "      while ((r &ge 0) && (pattern.charAt(r) !=  pattern.charAt(k-1))\n"
                                                  + "                       r = border[r]; \n"
                                                  + "      r++; \n"
                                                  + "      border[k] = r; \n"
                                                  + "   } \n" + "}"

                                              ;

  public KnuthMorrisPratt() {

  }

  public boolean knuthMorrisPratt(String source, String pattern) {

    n = source.length();
    m = pattern.length();

    final MsTiming msTiming = new MsTiming(100);
    final TicksTiming ticksTiming = new TicksTiming(20);

    String src[] = new String[n];
    String mustern[] = new String[m];

    /**
     * Convert string to a string array
     */

    for (int i = 0; i < n; i++) {

      src[i] = Character.toString(source.charAt(i));
    }

    for (int i = 0; i < m; i++) {
      mustern[i] = Character.toString(pattern.charAt(i));
    }

    /**
     * The Title of the animation
     */

    language.newRect(new Coordinates(40, 5), new Coordinates(300, 36),
        "TitleRect", null, rectProperties);
    TextProperties tp = new TextProperties();
    tp.set("font", new Font("Monospaced", 1, 20));
    language.newText(new Coordinates(65, 15), "Knuth-Morris-Pratt", "Title",
        null, tp);
    language.newRect(new Coordinates(460, 5), new Coordinates(460, 500),
        "vertLine", null, rectProperties);

    language.nextStep();

    SourceCode s = language.newSourceCode(new Coordinates(10, 170), "princode",
        null, sourceCodeProperties);
    s.addCodeLine(
        "public boolean knuthMorrisPratt(String source,String pattern)", null,
        0, null); // 0
    s.addCodeLine("{", null, 0, null); // 1
    s.addCodeLine("int n = source.length();", null, 1, null); // 2
    s.addCodeLine("int m = pattern.length();", null, 1, null); // 3
    s.addCodeLine("int i = 0, j = 0;", null, 1, null); // 4
    s.addCodeLine("int border[] = new int[m+1];", null, 1, null); // 5
    s.addCodeLine("computeBorders(border, m, pattern);", null, 1, null); // 6
    s.addCodeLine("while (i <= n-m)", null, 1, null); // 7
    s.addCodeLine("{", null, 1, null); // 8
    s.addCodeLine("while (source.charAt(i+j) == pattern.charAt(j))", null, 2,
        null); // 9
    s.addCodeLine("{", null, 2, null); // 10
    s.addCodeLine("j++;", null, 3, null); // 11
    s.addCodeLine("if (j == m) return true;", null, 3, null); // 12
    s.addCodeLine("}", null, 2, null); // 13
    s.addCodeLine("i = i + j - border[j];", null, 2, null); // 14
    s.addCodeLine("j = Math.max(0, border[j]);", null, 2, null); // 15
    s.addCodeLine("}", null, 1, null); // 16
    s.addCodeLine("return false;", null, 1, null); // 17
    s.addCodeLine("}", null, 0, null); // 18

    SourceCode s2 = language.newSourceCode(new Coordinates(480, 300), "s2",
        null, sourceCodeProperties);
    s2.addCodeLine(
        "private void computeBorder(int border[], int m, String pattern)",
        null, 0, null); // 0
    s2.addCodeLine("{", null, 0, null); // 1
    s2.addCodeLine("border[0] = -1;", null, 1, null); // 2
    s2.addCodeLine("int r = border[1] = 0;", null, 1, null); // 3
    s2.addCodeLine("for (int k = 2; k <= m ; k++)", null, 1, null); // 4
    s2.addCodeLine("{", null, 1, null); // 5
    s2.addCodeLine(
        "while (( r >= 0) && (pattern.charAt(r) !=  pattern.charAt(k-1))",
        null, 2, null); // 6
    s2.addCodeLine("r = border[r];", null, 3, null); // 7
    s2.addCodeLine("r++;", null, 2, null); // 8
    s2.addCodeLine("border[k] = r;", null, 2, null); // 9
    s2.addCodeLine("}", null, 1, null); // 10
    s2.addCodeLine("}", null, 0, null); // 11

    language.nextStep();

    s.highlight(0, 0, false);

    // Text srcTitleText =
    language.newText(new Coordinates(500, 140), "source", "srcText", null,
        textProperties);
    // Text musterText =
    language.newText(new Coordinates(500, 240), "pattern", "musterText", null,
        textProperties);
    StringArray srcArray = language.newStringArray(new Coordinates(560, 140),
        src, "srcarray", null, arrayProperties);
    
    // GR for AH
    myCounter = language.newCounter(srcArray);
    myCounterView = language.newCounterView(myCounter, new Coordinates(20, 85));
    
    StringArray musArray = language.newStringArray(new Coordinates(560, 240),
        mustern, "musarray", null, arrayProperties);

    language.nextStep();

    s.toggleHighlight(0, 0, false, 2, 0);
    // Text nText =
    language.newText(new Coordinates(10, 60), "n = " + n, "n", null,
        textProperties);
    language.nextStep();

    s.toggleHighlight(2, 0, false, 3, 0);
    // Text mText =
    language.newText(new Coordinates(200, 60), "m = " + m, "m", null,
        textProperties);
    language.nextStep();

    s.toggleHighlight(3, 0, false, 4, 0);
    Text iText = language.newText(new Coordinates(10, 90), "i = " + i, "i",
        null, textProperties);
    Text jText = language.newText(new Coordinates(200, 90), "j = " + j, "j",
        null, textProperties);
    language.nextStep();

    border = new int[m + 1];

    s.toggleHighlight(4, 0, false, 5, 0);
//    Text borderTitleText = 
    language.newText(new Coordinates(500, 40),
        "border[]", "borderText", null, textProperties);
    Text borderAtText = language.newText(new Coordinates(10, 150),
        "border[j] = " + border[j], "border[j]", null, textProperties);
    IntArray borderArray = language.newIntArray(new Coordinates(560, 40),
        border, "borderArray", null, arrayProperties);

    language.nextStep();

    s.toggleHighlight(5, 0, false, 6, 0);
    s2.highlight(0, 0, false);

    Rect rec1 = language.newRect(new Coordinates(740, 5), new Coordinates(995,
        170), "ComRect", null, rectProperties);
    SourceCode com1 = language.newSourceCode(new Coordinates(750, 10), "com1",
        null, sourceCodeProperties);

    com1.addCodeLine("Hier wird das Muster analysiert.", null, 0, null);
    com1.addCodeLine("Diese Analyse hilft, eine ", null, 0, null);
    com1.addCodeLine("Verschiebelle(border[]) zu ", null, 0, null);
    com1.addCodeLine("erstellen. Die Tabelle wird ", null, 0, null);
    com1.addCodeLine("dann im Fall eines Mismacht ", null, 0, null);
    com1.addCodeLine("benutzt, um zu ermitteln, wie ", null, 0, null);
    com1.addCodeLine("weit rechts mit dem Vergleich ", null, 0, null);
    com1.addCodeLine("weitergemacht wird.", null, 0, null);
    language.nextStep();

    s2.toggleHighlight(0, 0, false, 2, 0);
    borderArray.put(0, -1, msTiming, ticksTiming);
    borderArray.highlightCell(0, msTiming, ticksTiming);

    language.nextStep();

    int r = 0;
    Text rText = language.newText(new Coordinates(460, 510), "r = " + r, "r",
        null, textProperties);
    borderArray.put(1, 0, msTiming, ticksTiming);
    borderArray.unhighlightCell(0, null, null);
    borderArray.highlightCell(1, msTiming, ticksTiming);
    s2.toggleHighlight(2, 0, false, 3, 0);
    language.nextStep();

    s2.toggleHighlight(3, 0, false, 4, 0);
    int k = 2;
    Text kText = language.newText(new Coordinates(540, 510), "k = " + k, "k",
        null, textProperties);

    language.nextStep();

    Text patternAtRText = language.newText(new Coordinates(630, 510),
        "pattern.charAt(r) = " + pattern.charAt(r), "pattern.charAt(r)", null,
        textProperties);
    Text patternAtkText = language.newText(new Coordinates(780, 510),
        "pattern.charAt(k-1) = " + pattern.charAt(k - 1),
        "pattern.charAt(k-1)", null, textProperties);

    for (k = 2; k < m; k++) {

      patternAtRText.setText("pattern.charAt(r) = " + pattern.charAt(r),
          msTiming, ticksTiming);
      patternAtkText.setText("pattern.charAt(k-1) = " + pattern.charAt(k - 1),
          msTiming, ticksTiming);

      s2.toggleHighlight(4, 0, false, 6, 0);
      language.nextStep();

      while ((r >= 0) && (pattern.charAt(r) != pattern.charAt(k - 1))) {
        r = border[r];
        s2.toggleHighlight(6, 0, false, 7, 0);
        rText.setText("r = " + r, msTiming, ticksTiming);
        language.nextStep();
        s2.toggleHighlight(7, 0, false, 6, 0);
        language.nextStep();
      }

      r++;
      rText.setText("r = " + r, msTiming, ticksTiming);
      s2.toggleHighlight(6, 0, false, 8, 0);
      language.nextStep();
      border[k] = r;
      borderArray.put(k, r, msTiming, ticksTiming);
      borderArray.unhighlightCell(k - 1, null, null);
      borderArray.highlightCell(k, msTiming, ticksTiming);
      s2.toggleHighlight(8, 0, false, 9, 0);
      language.nextStep();

      s2.toggleHighlight(9, 0, false, 4, 0);
      kText.setText("k = " + (k + 1), msTiming, ticksTiming);
      language.nextStep();
    }

    s2.unhighlight(4);
    rText.hide();
    kText.hide();
    patternAtkText.hide();
    patternAtRText.hide();
    borderArray.unhighlightCell(m - 1, null, null);
    rec1.hide();
    com1.hide();
    borderAtText.setText("border[j] = " + border[j], msTiming, ticksTiming);
    s.toggleHighlight(6, 0, false, 7, 0);

//    Rect rec2 = 
    language.newRect(new Coordinates(740, 5), new Coordinates(995,
        90), "ComRect", null, rectProperties);
    SourceCode com2 = language.newSourceCode(new Coordinates(750, 10), "com2",
        null, sourceCodeProperties);
    com2.addCodeLine("Die Erste Phase ist worbei.", null, 0, null);
    com2.addCodeLine("Die Veschiebetabelle wurde ertellt.", null, 0, null);
    com2.addCodeLine("Jetzt kann man mit der Suche ", null, 0, null);
    com2.addCodeLine("weitermachen.", null, 0, null);

    language.nextStep();

    SourceCode com3 = null;

    Text patternTextJ = language.newText(new Coordinates(200, 120),
        "pattern.charAt(j) = " + pattern.charAt(j), "patternTextI", null,
        textProperties);
    Text srcAtText = language.newText(new Coordinates(200, 150),
        "source.charAt(i + j) = " + source.charAt(i + j),
        "source.charAt(i + j)", null, textProperties);

    ArrayMarker srcArrayMarker = language.newArrayMarker(srcArray, 0,
        "srcMarker", null, srcArrayMarkerProperties);

    patArrayMarkerProperties = new ArrayMarkerProperties();
    patArrayMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
    patArrayMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.GREEN);
    ArrayMarker patArrayMarker = language.newArrayMarker(musArray, 0,
        "patMarker", null, patArrayMarkerProperties);

    boolean backHighlight = false;
    int stepBack = 0;
    int preJ = 0;

    while (i <= n - m) {
      s.toggleHighlight(7, 0, false, 9, 0);
      srcAtText.setText("source.charAt(i + j) = " + source.charAt(i + j),
          msTiming, ticksTiming);
      patternTextJ.setText("pattern.charAt(j) = " + pattern.charAt(j),
          msTiming, ticksTiming);
      srcArrayMarker.move(i + j, msTiming, ticksTiming);
      patArrayMarker.move(j, msTiming, ticksTiming);
      srcArray.highlightCell(i + j, msTiming, ticksTiming);

      if (j + i > 0) {

        srcArray.unhighlightCell((i + j) - 1, msTiming, ticksTiming);
      }
      srcArray.highlightCell(i + j, msTiming, ticksTiming);

      if (backHighlight) {
        for (int t = preJ; t >= stepBack; --t) {
          musArray.unhighlightCell(t, null, null);
        }
      }

      for (int p = 0; p <= j; ++p) {
        musArray.highlightCell(p, msTiming, ticksTiming);
      }
      language.nextStep();

      while (source.charAt(i + j) == pattern.charAt(j)) {
        j++;
        jText.setText("j = " + j, msTiming, ticksTiming);
        borderAtText.setText("border[j] = " + border[j], msTiming, ticksTiming);
        s.toggleHighlight(9, 0, false, 11, 0);
        language.nextStep();

        s.toggleHighlight(11, 0, false, 12, 0);

        language.nextStep();

        if (j == m) {
          com2.hide();
          com3 = language.newSourceCode(new Coordinates(770, 30), "com3", null,
              sourceCodeProperties);
          com3.addCodeLine("Die Suche war erfolgreich !", null, 0, ticksTiming);
          s.unhighlight(12);
          s.unhighlight(12);
          srcArrayMarker.hide();
          patArrayMarker.hide();
          language.nextStep();
          return true;
        }

        if (j < m) {
          patternTextJ.setText("pattern.charAt(j) = " + pattern.charAt(j),
              msTiming, ticksTiming);
          patArrayMarker.move(j, msTiming, ticksTiming);
        }

        srcArrayMarker.move(i + j, msTiming, ticksTiming);
        if (j + i > 0) {

          srcArray.unhighlightCell((i + j) - 1, msTiming, ticksTiming);
        }
        srcArray.highlightCell(i + j, msTiming, ticksTiming);
        for (int p = 0; p <= j; ++p) {
          musArray.highlightCell(p, msTiming, ticksTiming);
        }
        srcAtText.setText("source.charAt(i + j) = " + source.charAt(i + j),
            msTiming, ticksTiming);
        s.toggleHighlight(12, 0, false, 9, 0);
        language.nextStep();

      }
      s.toggleHighlight(9, 0, false, 14, 0);
      i = i + j - border[j];
      borderAtText.setText("border[j] = " + border[j], msTiming, ticksTiming);
      iText.setText("i = " + i, msTiming, ticksTiming);
      language.nextStep();

      s.toggleHighlight(14, 0, false, 15, 0);
      int q = Math.max(0, border[j]);

      if (j > q) {
        backHighlight = true;
        stepBack = j - q;
      }

      preJ = j;
      j = q;
      jText.setText("j = " + j, msTiming, ticksTiming);
      language.nextStep();

      s.toggleHighlight(15, 0, false, 7, 0);
      language.nextStep();
    }

    s.unhighlight(7);

//    if (com3 != null) {
//      com3.hide();
//    }

    if (com2 != null) {
      com2.hide();
    }

    s.toggleHighlight(7, 0, false, 17, 0);
    SourceCode com4 = language.newSourceCode(new Coordinates(760, 25), "com4",
        null, sourceCodeProperties);
    com4.addCodeLine("Die Suche war nicht erfolgreich !", null, 0, ticksTiming);
    language.nextStep();

    s.unhighlight(17);
    srcArrayMarker.hide();
    patArrayMarker.hide();
    for (int v = 0; v < n; ++v) {
      srcArray.unhighlightCell(v, msTiming, ticksTiming);
    }
    for (int v = 0; v < m; ++v) {
      musArray.unhighlightCell(v, msTiming, ticksTiming);
    }

    language.nextStep();

    return false;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    srcArrayMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("srcArrayMarkerProperties");
    patArrayMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("patArrayMarkerProperties");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    textProperties = (TextProperties) props
        .getPropertiesByName("textProperties");
    rectProperties = (RectProperties) props
        .getPropertiesByName("rectProperties");
    arrayProperties = (ArrayProperties) props
        .getPropertiesByName("arrayProperties");

//    init();
    String source = (String) primitives.get("source");
    String pattern = (String) primitives.get("pattern");
    knuthMorrisPratt(source, pattern);

    String re = language.toString();
//    System.out.println(re);

    return re;
  }

  public String getAlgorithmName() {
    return "Knuth, Morris, Pratt (1977)";
  }

  public String getAnimationAuthor() {
    return "Roger Ponka";
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getName() {
    return "Knuth-Morris-Pratt";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void init() {
    language = new AnimalScript(getAlgorithmName(), getAnimationAuthor(), 1024,
        768);
    language.setStepMode(true);
  }

}
