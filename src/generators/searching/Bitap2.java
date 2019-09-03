package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Bitap2 implements Generator {
  private Language              lang;
  private String                Text;
  private ArrayProperties       arrayProperties;
  private SourceCodeProperties  sourceCodeProperties;
  private String                Pattern;
  private TextProperties        textProperties;
  private int                   k;
  private MatrixProperties      matrixProperties;
  private ArrayProperties       arrayProps;
  private SourceCode            sc;
  private ArrayMarkerProperties amt, amp, ame;
  private StringArray           textArray, patternArray;
  private IntArray              errorArray;
  private ArrayMarker           mt;
  private ArrayMarker           mp;
  private ArrayMarker           me;
  private int                   tLength, pLength, eLength;
  private Text[]                explanations     = new Text[34];
  private StringMatrix          mask;
  private MatrixProperties      p                = new MatrixProperties();
  private int                   firstMatchedText = -1;

  public void init() {
    lang = new AnimalScript("Bitap[DE]", "Moritz Zysk", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    lang.setStepMode(true);
    Text t = lang.newText(new Coordinates(20, 10), "Bitap-Algorithm", "algo",
        null);
    t.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);

    Text = (String) primitives.get("Text");
    arrayProperties = (ArrayProperties) props
        .getPropertiesByName("arrayProperties");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    Pattern = (String) primitives.get("Pattern");
    textProperties = (TextProperties) props
        .getPropertiesByName("textProperties");
    k = (Integer) primitives.get("k");
    matrixProperties = (MatrixProperties) props
        .getPropertiesByName("matrixProperties");

    fuzzySearch(stringToStringArray(Text), stringToStringArray(Pattern), k);

    return lang.toString();
  }

  // /////////////////////////////////////////////////////////////////////
  // //////////////////////////////////////////////////////////////////////
  // //////////////////////////////////////////////////////////////////////

  private String[] stringToStringArray(String s) {
    String[] sArray = new String[s.length()];
    char[] cArray = s.toCharArray();
    for (int i = 0; i < s.length(); i++) {
      sArray[i] = "" + cArray[i];
    }
    return sArray;
  }

  private boolean exp  = true;
  private int     step = 0;

  private void introduction() {
    exp = false;
    if (step == 0) {
      text("Einfuehrung", 1, 0, "");
      text(
          "Zu einer Zeichenkette durchsucht der Bitap-Algorithmus einen gegebenen Text nach aehnlichen Zeichenketten.",
          2, 0, "");
      text(
          "Neben der exakten Zeichenkette werden dabei auch solche beruecksichtigt, welche sich innerhalb der gewuenschten Levenshtein-Distanz befinden.",
          3, 0, "");
      lang.nextStep();
      tHide(1);
      tHide(2);
      tHide(3);
      step++;
    } else {
      lang.nextStep();
      lang.hideAllPrimitives();
      mask.hide();
      lang.nextStep();
      text("Abschlussbemerkung", 1, 0, "");
      text(
          "Weitere Informationen bezueglich des Algorithmuses kann man in der deutschprachigen Wikipedia unter 'Baeza-Yates-Gonnet Algorithmus' finden.",
          2, 0, "");
      text(
          "Der Code wurde https://github.com/simon-watiau/Bitap-in-Java/blob/master/src/com/sw/bitap/Bitap.java entnommen.",
          3, 0, "");
      text(
          "Das Suchen nach aehnlichen Zeichenketten nennt man Fuzzy Suche. Einen interessanten Artikel findet man unter ntz-develop.blogspot.de/2011/03/fuzzy-string-search.html.",
          4, 0, "");
      text(
          "Da eine Erklaerung und ein Algorithmus zur Berechnung der Levenshtein-Distanz Algorithmus bereits in Animal vorhanden ist, wurde hier auf eine Erklaerung verzichtet.",
          5, 0, "");
      text(
          "Eine Levenshtein-Distanz von 0 entspricht der exakten Suche nach dem Muster.",
          6, 0, "");
      text(
          "Ein weiterer interessanter Algorithmus fuer diese Problemstellung ist der Knuth-Morris-Pratt Algorithmus der ebenfalls unter Animal zu finden ist.",
          7, 0, "");
      text(
          "Da der Bitap Algorithmus im wesentlichen auf Bit-Operationen zurueckgefuehrt werden kann, ist der Algorithmus ziemlich effizient.",
          8, 0, "");
      text(
          "Der eigentliche Algorithmus innerhalb der while-Schleife hat eine Komplexitaet von O(n*k).",
          9, 0, "");
      lang.nextStep();

    }
    exp = true;
  }

  int error;

  private void fuzzySearch(String[] text, String[] pattern, int error) {
    introduction();
    this.error = error;
    tLength = text.length;
    pLength = pattern.length;
    eLength = error;
    addCodeLines();
    lang.nextStep();
    th(0, 0);

    fillArrays(text, pattern, error);
    // 1-5
    th(0, 1);
    th(1, 2);
    th(2, 3);
    th(3, 4);
    text("for(" + 0 + "<" + eLength + ")", 5, 0, "");
    th(4, 5);
    text("error[" + 0 + "] = 1", 6, 0, "");
    for (int i = 0; i < eLength; i++) {
      th(5, 6);
      errorArray.put(i, 1, null, null);
      text("for(" + 0 + "<" + eLength + ")", 5, 0, "");
      // //tHide(5);
      th(6, 5);
      text("error[" + i + "] = 1", 6, 0, "");
      me.move(1, null, null);
    }

    me.hide();
    text("for(" + 0 + " < " + pLength + ")", 7, 0, "");
    th(5, 7);
    for (int i = 0; i < pLength; i++) {

      mp.move(i, null, null);
      int c = patternArray.getData(i).charAt(0);
      hCell(c);
      text("ascii = " + c, 8, 0, "");
      th(7, 8);
      text("patternMask[" + c + "] |= 1 << " + i, 9, 0, "");
      sCell(c, (gCell(c) | 1 << i) + "");
      th(8, 9);
      text("", 10, 4, "");
      th(9, 10);
      uCell(c);
      text("for(" + (i + 1) + " < " + pLength + ")", 7, 0, "");
      th(10, 7);

    }
    text("", 11, 4, "");
    // tHide(7);
    mp.hide();
    th(7, 11);// ////////////////////////////////////////////11

    whileT();
    introduction();
  }

  private void whileT() {
    // 11
    int i = 0;
    while (i < tLength) {
      text("while(" + i + " < " + tLength + ")", 12, 0, "");
      if (i == 0)
        th(11, 12);
      else
        th(24, 12);
      long old = 0;
      long nextOld = 0;
      text("", 12, 4, "");
      th(12, 13);
      th(13, 14);
      int asciiText = textArray.getData(i).charAt(0);
      text("ascii = " + asciiText + "    // getAscii(" + ((char) asciiText)
          + ");", 15, 0, "");
      hCell(asciiText);
      mt.move(i, null, null);
      th(14, 15);

      int maskAscii = gCell(asciiText);
      text("", 16, 0, "");
      text("for(" + 0 + " <= " + eLength + ")", 16, 0, "");
      th(15, 16);
      //
      for (int d = 0; d <= eLength; d++) {
        text("tmp = " + errorArray.getData(d) + "&" + maskAscii, 17, 1,
            "tmp = " + (errorArray.getData(d) & maskAscii));
        long tmp = errorArray.getData(d) & maskAscii;
        th(16, 17);
        long sub = (old | tmp) << 1;
        text("sub = (" + old + " | " + tmp + ")" + " << " + 1, 18, 1, "sub = "
            + ((old | tmp) << 1));
        th(17, 18);
        long ins = old | (tmp << 1);
        text("ins = " + old + " | (" + tmp + " << " + 1 + ")", 19, 1, "ins = "
            + (old | (tmp << 1)));
        th(18, 19);
        long del = (nextOld | tmp) << 1;
        text("del = (" + nextOld + " | " + tmp + ") << " + 1, 20, 1, "del = "
            + ((nextOld | tmp) << 1));
        th(19, 20);
        old = errorArray.getData(d);
        text("old = " + errorArray.getData(d), 21, 1,
            "old = " + errorArray.getData(d));
        th(20, 21);
        long l = sub | ins | del | 1;
        errorArray.put(d, (int) l, null, null);
        text("error[" + d + "] = " + sub + " | " + ins + " | " + del + " | "
            + 1, 22, 1, "");
        th(21, 22);
        nextOld = l;
        text("nextOld = " + l, 23, 1, "");
        th(22, 23);
        // tHide(16);//tHide(17);//tHide(18);//tHide(19);//tHide(20);//tHide(21);//tHide(22);//tHide(23);

        text((d + 1) + " <= " + eLength, 16, 0, "");
        th(23, 16);
      }
      // tHide(12);
      // tHide(16);
      // tHide(15);
      boolean b = 0 < (errorArray.getData(error) & (1 << pLength));
      text(
          "(" + errorArray.getData(error) + "& ("
              + (1 << Integer.valueOf(pLength)) + ")) <=> "
              + Integer.toBinaryString(errorArray.getData(error)) + "&"
              + Integer.toBinaryString(1 << pLength) + " <=> " + b, 25, 2, "");
      i++;

      th(16, 25);
      // tHide(25);
      if (b) {
        th(25, 26);
        if (firstMatchedText == -1 || (i - firstMatchedText > pLength)) {
          firstMatchedText = i;
          th(26, 27);
          indexes.add(i);
          th(27, 28);
          if (indexes.size() > 1)
            result.hide();
          result = lang.newText(new Offset(0, 20, t2, "SW"), "indexes = "
              + indexes.toString(), "r", null, tt);
          th(28, 29);
          th(29, 30);
        } else {
          th(26, 30);
        }
      } else {
        // tHide(25);
        th(25, 30);
      }

      th(30, 31);
      sc.unhighlight(31);
      uCell(asciiText);

    }

    if (indexes.size() == 0)
      result = lang.newText(new Offset(0, 20, t2, "SW"),
          "Das Pattern ist nicht im Text vorhanden", "r", null, tt);

  }

  private Text               result;
  private ArrayList<Integer> indexes = new ArrayList<Integer>();

  private void hCell(int c) {
    mask.highlightCell(c / 10 + 1, c % 10 + 1, null, null);
  }

  private void uCell(int c) {
    mask.unhighlightCell(c / 10 + 1, c % 10 + 1, null, null);
  }

  private void sCell(int c, String t) {
    mask.put(c / 10 + 1, c % 10 + 1, t, null, null);
  }

  private int gCell(int c) {
    return Integer.valueOf(mask.getElement(c / 10 + 1, c % 10 + 1).trim());
  }

  private void fillArrays(String[] text, String[] pattern, int error) {

    arrayProps = arrayProperties;
    // arrayProps = new ArrayProperties();
    // arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
    // Color.BLACK);
    // arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);

    amt = new ArrayMarkerProperties();
    amt.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    amt.set(AnimationPropertiesKeys.LABEL_PROPERTY, "text");
    amt.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
    // marker j
    amp = new ArrayMarkerProperties();
    amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "pattern");
    amp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
    // marker min
    ame = new ArrayMarkerProperties();
    ame.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    ame.set(AnimationPropertiesKeys.LABEL_PROPERTY, "error");
    ame.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);

    textArray = lang.newStringArray(new Coordinates(700, 600), text, "text",
        null, arrayProps);
    patternArray = lang.newStringArray(new Coordinates(700, 675), pattern,
        "pattern", null, arrayProps);
    errorArray = lang.newIntArray(new Coordinates(20, 700), new int[error + 1],
        "text", null, arrayProps);

    mt = lang.newArrayMarker(textArray, 0, "t", null, amt);
    mp = lang.newArrayMarker(patternArray, 0, "p", null, amp);
    me = lang.newArrayMarker(errorArray, 0, "e", null, ame);

    String[][] data = new String[14][11];
    for (int i = 1; i < 14; i++) {
      data[i][0] = (i - 1) + ".";
    }
    for (int i = 1; i < 11; i++) {
      data[0][i] = "." + (i - 1);
    }
    for (int i = 1; i < 14; i++)
      for (int j = 1; j < 11; j++)
        data[i][j] = " 0";

    // p.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
    p = matrixProperties;
    mask = lang.newStringMatrix(new Coordinates(700, 100), data, "ascii", null,
        p);
    mkCounter();
  }

  private void mkCounter() {
    TwoValueCounter counterM = lang.newCounter(mask);
    TwoValueCounter counterT = lang.newCounter(textArray);
    TwoValueCounter counterP = lang.newCounter(patternArray);
    TwoValueCounter counterE = lang.newCounter(errorArray);
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);

    lang.newCounterView(counterM, new Offset(0, -40, mask, "NW"), cp, true,
        true);
    lang.newCounterView(counterT, new Offset(100, 0, textArray, "E"), cp, true,
        true);
    lang.newCounterView(counterP, new Offset(100, 75, textArray, "E"), cp,
        true, true);
    lang.newCounterView(counterE, new Offset(0, 20, errorArray, "SW"), cp,
        true, true);
  }

  // private int x = 0;
  private Text           t;
  private Text           t2;
  private String         told = "";
  private TextProperties tt;

  private void text(String text, int line, int pos, String s) {

    tt = textProperties;

    if (exp) {
      // tt.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
      // Font.PLAIN, 12));
      // if (pos == 1)
      // tt.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
      // t = lang.newText(new Coordinates(430,33+16*line), text, "line" +
      // line, null,tt);
      try {
        t.hide();
        t2.hide();
      } catch (java.lang.NullPointerException e) {
        // first Time
      }
//      x++;

      if (pos == 1) {
        told = told + ";   " + s;
        t2 = lang.newText(new Offset(0, 20, t, "SW"), told, "line" + line,
            null, tt);
      } else {
        told = s;
      }
      if (pos != 4)
        t = lang.newText(new Offset(0, 20, sc, "SW"), line + ": " + text,
            "line" + line, null, tt);
      // t.setText(text, null, null);

    } else {
      tt.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
          Font.PLAIN, 16));
      tt.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      t = lang.newText(new Coordinates(30, 33 + 32 * line), text,
          "line" + line, null, tt);
    }
    explanations[line] = t;

  }

//  private int rinc = 0;
  Rect        rect;

  // private void rinc() {
  // if (rect != null)
  // rect.hide();
  // rinc++;
  // rect = lang.newRect(new Coordinates(0, 33), new Coordinates(600,
  // 49 + 16 * rinc), "r" + rinc, null);
  // }

  private void th(int current, int next) {
    sc.toggleHighlight(current, next);
    lang.nextStep();
  }

  private void tHide(int line) {
    if (explanations[line] != null)
      explanations[line].hide();
  }

  private void addCodeLines() {
    // first visual properties
    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "SansSerif", Font.PLAIN, 12));
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCodeProperties scProps = sourceCodeProperties;

    // sourceCode entity
    sc = lang.newSourceCode(new Coordinates(20, 20), "sourceCode", null,
        scProps);
    // parameters: code itself; name (can be null); indentation level;
    // display options

    sc.addCodeLine(
        " 0.  public static void find (String text, String pattern, int k){",
        null, 0, null);
    sc.addCodeLine(" 1.    int firstMatchedText = -1;", null, 0, null);
    sc.addCodeLine(
        " 2.    ArrayList<Integer> indexes = new ArrayList<Integer>();", null,
        0, null);
    sc.addCodeLine(" 3.    long[] error = new long[k + 1];", null, 0, null);
    sc.addCodeLine(" 4.    long[] patternMask = new long[128];", null, 0, null);
    sc.addCodeLine(" 5.    for (int i = 0; i < k; i++)", null, 0, null);
    sc.addCodeLine(" 6.      error[i] = 1;", null, 0, null);
    sc.addCodeLine(" 7.    for (int i = 0; i < pattern.length();++i){", null,
        0, null);

    sc.addCodeLine(" 8.       int ascii = (int) pattern.charAt(i)", null, 0,
        null);
    sc.addCodeLine(" 9.       patternMask[ascii] |= 1 << i;", null, 0, null);
    sc.addCodeLine("10.    }", null, 0, null);
    sc.addCodeLine("11.    int i = 0;", null, 0, null);
    sc.addCodeLine("12.    while (i < text.length()) {", null, 0, null);
    sc.addCodeLine("13.      long old = 0;", null, 0, null);
    sc.addCodeLine("14.      long nextOld = 0", null, 0, null);

    sc.addCodeLine("15.      int ascii = text.charAt(i);", null, 0, null);
    sc.addCodeLine("16.      for(int d = 0; d <= k; ++d){", null, 0, null);
    sc.addCodeLine("17.        long tmp = (error[d] & patternMask[ascii]);",
        null, 0, null);
    sc.addCodeLine("18.        long sub = (old | tmp) << 1;", null, 0, null);
    sc.addCodeLine("19.        long ins = old | (tmp << 1)", null, 0, null);
    sc.addCodeLine("20.        long del = (nextOld | tmp) << 1;", null, 0, null);

    sc.addCodeLine("21.        old = error[d];", null, 0, null);
    sc.addCodeLine("22.        error[d] = sub|ins|del|1;", null, 0, null);
    sc.addCodeLine("23.        nextOld = error[d];", null, 0, null);
    sc.addCodeLine("24.      }", null, 0, null);
    sc.addCodeLine("25.      if(0 < (error[k] & (1 << pattern.length()))){",
        null, 0, null);
    sc.addCodeLine(
        "26.        if((firstMatchedText == -1) || (i - firstMatchedText > pattern.length())){",
        null, 0, null);
    sc.addCodeLine("27.          firstMatchedText = i;", null, 0, null);
    sc.addCodeLine("28.          indexes.add(i);", null, 0, null);
    sc.addCodeLine("29.        }", null, 0, null);
    sc.addCodeLine("30.        i++;", null, 0, null);
    sc.addCodeLine("31.      }", null, 0, null);
    sc.addCodeLine("32.   }", null, 0, null);
    sc.addCodeLine("33. }", null, 0, null);
  }

  // //////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////

  public String getName() {
    return "Bitap[DE]";
  }

  public String getAlgorithmName() {
    return "Bitap";
  }

  public String getAnimationAuthor() {
    return "Moritz Zysk";
  }

  public String getDescription() {
    return "String-Matching-Algorithmen dienen dazu Vorkommen eines Suchpatterns in einem l&auml;ngeren Text zu finden."
        + "\n"
        + "Dabei kann zwischen der exakten Suche und der Fuzzy-Suche unterschieden werden."
        + "\n"
        + "Bei der Fuzzy-Suche werden neben &uuml;bereinstimmungen auch Strings ber&uuml;cksichtigt, welche dem Pattern bez&uuml;glich bestimmten Kriterien hinreichend &auml;hnlich sind."
        + "\n"
        + "Der Bitap-Algorithmus, auch bekannt als Baeza-Yates-Gonnet-Algorithmus, ist ein solcher String-Matching-Algorithmus der die Fuzzy-Suche implementiert, wobei die Levenshtein-Distanz als &Auml;hnlichkeitsmasstab verwendet wird."
        + "\n"
        + "Die Levenshtein-Distanz wird ermittelt, indem die notwendigen Einf&uuml;ge-/L&ouml;sch- und Ersetz-Operationen einzelner Buchstaben gez&auml;hlt werden, um einen String in den anderen String zu verwandeln."
        + "\n"
        + "N&auml;heres bez&uuml;glich der Levenshtein-Distanz findet man  bei dem bereits in Animal vorhandenen Levenshtein-Generator im Bereich searching."
        + "\n"
        + " "
        + "\n"
        + "Bitap kann also bei der Suche eventuelle Tippfehler ber&uuml;cksichtigen und eignet sich deshalb beispielsweise zur Rechtschreibpr&uuml;fung,"
        + "\n"
        + " f&uuml;r Suchmaschinen oder auch in der Bioinformatik bei der Suche nach &auml;hnlichen Nukleotidsequenzen der DNA."
        + "\n"
        + "Eine Abwandlung wird f&uuml;r das Unix-Tool grep benutzt."
        + "\n" + " " + "\n" + "\n";
  }

  public String getCodeExample() {
    return "public List<Integer> find (String doc, String pattern, int k){"
        + "\n"
        + "  int firstMatchedText = -1;"
        + "\n"
        + "  ArrayList<Integer> indexes = new ArrayList<Integer>();"
        + "\n"
        + "  long[] r = new long[k + 1];"
        + "\n"
        + "  long[] patternMask = new long[128];"
        + "\n"
        + "  for (int i = 0; i < k; i++){"
        + "\n"
        + "    r[i] = 1;"
        + "\n"
        + "  }    "
        + "\n"
        + "  for (int i = 0; i<pattern.length();++i){"
        + "\n"
        + "    patternMask[(int) pattern.charAt(i)] |= 1 << i;"
        + "\n"
        + "  }"
        + "\n"
        + "  int i = 0;"
        + "\n"
        + "  while (i < doc.length()) {"
        + "\n"
        + "    long old = 0;"
        + "\n"
        + "    long nextOld = 0;"
        + "\n"
        + "    for(int d = 0; d <= k; ++d){"
        + "\n"
        + "      long sub = (old | (r[d] & patternMask[doc.charAt(i)])) << 1;"
        + "\n"
        + "      long ins = old | ((r[d] & patternMask[doc.charAt(i)]) << 1);"
        + "\n"
        + "      long del = (nextOld | (r[d] & patternMask[doc.charAt(i)])) << 1;"
        + "\n"
        + "      old = r[d];"
        + "\n"
        + "      r[d] = sub|ins|del|1;"
        + "\n"
        + "      nextOld = r[d];         "
        + "\n"
        + "    }"
        + "\n"
        + "    if(0 < (r[k] & (1 << pattern.length()))){                "
        + "\n"
        + "      if ((firstMatchedText == -1) || (i - firstMatchedText > pattern.length())){"
        + "\n" + "        firstMatchedText = i;" + "\n"
        + "        indexes.add(i);" + "\n" + "      }" + "\n" + "    }" + "\n"
        + "    i++;" + "\n" + "  }" + "\n" + "  return indexes;" + "\n" + "}";
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
    return Generator.JAVA_OUTPUT;
  }

}
