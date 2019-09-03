package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class CBCEncrypt implements Generator {
  private Language             lang;
  private int                  e;
  private int[]                iv;
  private int[]                m;
  private TextProperties       text;
  private SourceCodeProperties sourceCode;
  private TextProperties       highlightedText;
  private MatrixProperties     table;
  private TextProperties       headlines;

  public void init() {
    lang = new AnimalScript(
        "Cipher Block Chaining Mode (CBC): Verschlüsselung",
        "Stephan Cornelius Arndt<cornelius@alceta.de>,Felix Gündling<felix.guendling@gmx.de>",
        800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    e = (Integer) primitives.get("e");
    iv = (int[]) primitives.get("iv");
    m = (int[]) primitives.get("m");

    text = (TextProperties) props.getPropertiesByName("text");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    highlightedText = (TextProperties) props
        .getPropertiesByName("highlightedText");
    table = (MatrixProperties) props.getPropertiesByName("table");
    headlines = (TextProperties) props.getPropertiesByName("headlines");

    encrypt(iv, m, e);

    return lang.toString();
  }

  public void encrypt(int[] iv, int[] m, int e) {
    // Title
    TextProperties headerProperties = new TextProperties();
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    Text title = lang.newText(new Coordinates(30, 40),
        "Cipher Block Chaining Mode (CBC): Verschlüsselung", "header", null,
        headerProperties);

    RectProperties hRectProperties = new RectProperties();
    hRectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    hRectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    hRectProperties
        .set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    Rect titleRect = lang.newRect(new Offset(-10, -5, "header", "NW"),
        new Offset(10, 5, "header", "SE"), "hRect", null, hRectProperties);

    // Properties

    // headline properties
    String headlineFontName = ((Font) headlines
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName();
    Color headlineColor = (Color) headlines
        .get(AnimationPropertiesKeys.COLOR_PROPERTY);
    TextProperties headlineProperties = new TextProperties();
    headlineProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        headlineFontName, Font.BOLD, 14));
    headlineProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        headlineColor);

    // default text properties
    String textFontName = ((Font) text
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName();
    Color textColor = (Color) text.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    TextProperties defaultTextProperties = new TextProperties();
    defaultTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        textFontName, Font.PLAIN, 14));
    defaultTextProperties
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);

    // highlighted text color
    Color highlightedTextColor = (Color) highlightedText
        .get(AnimationPropertiesKeys.COLOR_PROPERTY);

    // indices text properties
    TextProperties indicesTextProperties = new TextProperties();
    indicesTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        textFontName, Font.PLAIN, 12));
    indicesTextProperties
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);

    // bold text properties
    TextProperties boldTextProperties = new TextProperties();
    boldTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        textFontName, Font.BOLD, 14));
    boldTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);

    // table properties
    Color tableFillColor = (Color) table
        .get(AnimationPropertiesKeys.FILL_PROPERTY);
    Color tableTextColor = (Color) table
        .get(AnimationPropertiesKeys.COLOR_PROPERTY);
    String tableTextFont = ((Font) table
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName();
    Color tableCellHighlightColor = (Color) table
        .get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);

    // source code properties
    String sourceCodeFontName = ((Font) sourceCode
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName();
    int sourceCodeFontStyle = ((Font) sourceCode
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).getStyle();
    int sourceCodeFontSize = (Integer) sourceCode
        .get(AnimationPropertiesKeys.SIZE_PROPERTY);
    sourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        sourceCodeFontName, sourceCodeFontStyle, sourceCodeFontSize));

    // Introduction
    String descriptionString0 = "Der Cipher Block Chaining Mode (CBC) ist eine Betriebsart,";
    String descriptionString1 = "Klartexte blockweise zu verschlüsseln. Hierbei wird ein Klartextblock";
    String descriptionString2 = "vor dessen Verschlüsselung durch die Verschlüsselungsfunktion E";
    String descriptionString3 = "stets per XOR mit dem Chiffrat des vorherigen Klartextblocks verknüpft.";
    Text description0 = lang.newText(new Offset(30, 70, "hRect", "SW"),
        descriptionString0, "description0", null, defaultTextProperties);
    Text description1 = lang.newText(new Offset(0, 10, "description0", "SW"),
        descriptionString1, "description1", null, defaultTextProperties);
    Text description2 = lang.newText(new Offset(0, 10, "description1", "SW"),
        descriptionString2, "description2", null, defaultTextProperties);
    Text description3 = lang.newText(new Offset(0, 10, "description2", "SW"),
        descriptionString3, "description3", null, defaultTextProperties);

    lang.nextStep("Einführung");
    description0.hide();
    description1.hide();
    description2.hide();
    description3.hide();

    // Input values
    lang.newText(new Offset(0, 30, "hRect", "SW"), "Eingangswerte:",
        "input_values", null, headlineProperties);
    int[] m2 = m;
    String mInput = "m = " + arrayToString(m2) + ",";
    lang.newText(new Offset(10, 0, "input_values", "NE"), mInput, "mInput",
        null, defaultTextProperties);
    String ivInput = arrayToString(iv);
    lang.newText(new Offset(10, 0, "mInput", "NE"), "iv = " + ivInput,
        "ivInput", null, defaultTextProperties);
    lang.newText(new Offset(0, 10, "input_values", "SW"),
        "Verschlüsselungsfunktion:", "function_E_0", null, headlineProperties);
    lang.newText(new Offset(10, 0, "function_E_0", "NE"), "E", "function_E_1",
        null, defaultTextProperties);
    lang.newText(new Offset(1, 5, "function_E_1", "NE"), "e", "function_E_2",
        null, indicesTextProperties);
    String function = "(in) = (in "
        + ((e > 0) ? ("+ " + e) : ("- " + Math.abs(e))) + ") mod "
        + (1 << iv.length);
    lang.newText(new Offset(10, 2, "function_E_1", "NE"), function,
        "function_E_3", null, defaultTextProperties);

    // Visualization
    PolylineProperties p = new PolylineProperties();
    p.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    lang.newPolyline(
        new Node[] { new Offset(350, 400, "hRect", "SW"),
            new OffsetFromLastPosition(0, -300),
            new OffsetFromLastPosition(-200, 0),
            new OffsetFromLastPosition(0, 30) }, "c_j-c_j-1", null, p);

    lang.newRect(new Offset(-70, 30, "c_j-c_j-1", "NW"), new Offset(70, 80,
        "c_j-c_j-1", "NW"), "rect_c_j-1", null);
    lang.newText(new Offset(60, 5, "rect_c_j-1", "NW"), "c", "c_j-1_0", null,
        defaultTextProperties);
    Text cj_1Index = lang.newText(new Offset(1, 5, "c_j-1_0", "NE"), "j-1",
        "c_j-1_1", null, indicesTextProperties);

    lang.newText(new Offset(20, 20, "rect_c_j-1", "NE"), "c", "c_0_0", null,
        defaultTextProperties);
    lang.newText(new Offset(1, 5, "c_0_0", "NE"), "0", "c_0_1", null,
        indicesTextProperties);
    lang.newText(new Offset(10, 1, "c_0_0", "NE"), " = iv", "c_0_2", null,
        defaultTextProperties);

    lang.newPolyline(new Node[] { new Offset(70, 0, "rect_c_j-1", "SW"),
        new Offset(70, 40, "rect_c_j-1", "SW") }, "c_j-1-xor", null, p);

    lang.newCircle(new Offset(0, 14, "c_j-1-xor", "SW"), 10, "xor_0", null);
    lang.newPolyline(new Node[] { new Offset(10, 0, "xor_0", "NW"),
        new Offset(10, 20, "xor_0", "NW") }, "xor_1", null);
    lang.newPolyline(new Node[] { new Offset(-10, 10, "xor_1", "NW"),
        new Offset(10, 10, "xor_1", "NW") }, "xor_2", null);

    lang.newPolyline(new Node[] { new Offset(-70, 0, "xor_2", "NW"),
        new Offset(-4, 0, "xor_2", "NW") }, "m_j-xor", null, p);

    lang.newText(new Offset(-30, -7, "m_j-xor", "NW"), "m", "m_j_0", null,
        defaultTextProperties);
    Text mjIndex = lang.newText(new Offset(1, 5, "m_j_0", "NE"), "j", "m_j_1",
        null, indicesTextProperties);

    lang.newPolyline(new Node[] { new Offset(16, 7, "m_j-xor", "NW"),
        new Offset(30, -7, "m_j-xor", "NW") }, "line_n_0", null);
    lang.newText(new Offset(17, -20, "m_j-xor", "NW"), "n", "n_0", null,
        defaultTextProperties);

    lang.newPolyline(new Node[] { new Offset(0, 4, "xor_1", "SW"),
        new Offset(0, 44, "xor_1", "SW") }, "xor-E_e", null, p);

    lang.newSquare(new Offset(-30, 0, "xor-E_e", "SW"), 60, "E_e", null);
    lang.newText(new Offset(23, 20, "E_e", "NW"), "E", "E_e_0", null,
        defaultTextProperties);
    lang.newText(new Offset(1, 5, "E_e_0", "NE"), "e", "E_e_1", null,
        indicesTextProperties);

    lang.newPolyline(new Node[] { new Offset(-50, 30, "E_e", "NW"),
        new Offset(-4, 30, "E_e", "NW") }, "e-E_e", null, p);
    CircleProperties filled = new CircleProperties();
    filled.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    lang.newCircle(new Offset(0, 30, "E_e", "NW"), 4, "e_circle", null, filled);
    lang.newText(new Offset(-15, -7, "e-E_e", "NW"), "e", "e", null,
        defaultTextProperties);

    lang.newPolyline(
        new Node[] { new Offset(30, 0, "E_e", "SW"),
            new OffsetFromLastPosition(0, 53),
            new OffsetFromLastPosition(250, 0) }, "E_e-c_j", null, p);

    lang.newPolyline(new Node[] { new Offset(-7, 32, "E_e-c_j", "NW"),
        new Offset(7, 18, "E_e-c_j", "NW") }, "line_n_1", null);
    lang.newText(new Offset(10, 18, "E_e-c_j", "NW"), "n", "n_1", null,
        defaultTextProperties);

    lang.newText(new Offset(-150, 10, "c_j-c_j-1", "SE"), "c", "c_j_0", null,
        defaultTextProperties);
    Text cjIndex = lang.newText(new Offset(1, 5, "c_j_0", "NE"), "j", "c_j_1",
        null, indicesTextProperties);

    // Output value
    lang.newText(new Offset(0, 450, "hRect", "SW"), "Chiffrat von m:",
        "cipher_of_m_0", null, headlineProperties);

    // Algorithm
    lang.newText(new Offset(500, 0, "input_values", "NW"), "Algorithmus:",
        "algorithm", null, headlineProperties);
    SourceCode sc = lang.newSourceCode(new Offset(30, 0, "algorithm", "SW"),
        "code", null, sourceCode);
    sc.addCodeLine("c0 = iv;", null, 0, null);
    sc.addCodeLine("for j = 1,...,k do", null, 0, null);
    sc.addCodeLine("mj_xor_cj-1 = mj xor cj-1;", null, 1, null);
    sc.addCodeLine("cj = E_e(mj_xor_cj-1);", null, 1, null);
    sc.addCodeLine("end", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("c = ck ck-1 ... c1;", null, 0, null);
    lang.newText(new Offset(0, 20, "code", "SW"),
        "(wobei k die Anzahl der n Bit breiten Blöcke in m bezeichnet)",
        "algo_info", null, defaultTextProperties);

    // Split layout line
    Polyline splitline = lang.newPolyline(new Node[] {
        new Offset(-50, 0, "algorithm", "NW"),
        new Offset(-50, 440, "algorithm", "NW") }, "split_layout", null);
    splitline.changeColor("color", Color.LIGHT_GRAY, null, null);

    // Encrypt function E
    Function fun = new Fun(e, 1 << iv.length);

    // pad with zeros
    int fill = (iv.length - m2.length % iv.length) % iv.length;
    int[] tmp = m2;
    m2 = new int[m2.length + fill];
    System.arraycopy(tmp, 0, m2, fill, tmp.length);
    for (int i = 0; i < fill; i++) {
      m2[i] = 0;
    }

    // Protocol table
    lang.newText(new Offset(0, 220, "algorithm", "NW"), "Protokoll:",
        "protocol", null, headlineProperties);
    int lines = m2.length / iv.length + 1;
    String tableDecl = "grid \"table\" offset (30,10) from \"protocol\" SW lines "
        + lines
        + " columns 5 style table cellWidth 80 cellHeight 20 borderColor white ";
    tableDecl += "highlightFillColor (" + tableCellHighlightColor.getRed()
        + ", " + tableCellHighlightColor.getGreen() + ", "
        + tableCellHighlightColor.getBlue() + ") ";
    tableDecl += "font " + tableTextFont + " size 12";
    lang.addLine(tableDecl);
    String tableInit = "setGridColor \"table[][]\" color white ";
    tableInit += "textColor (" + tableTextColor.getRed() + ", "
        + tableTextColor.getGreen() + ", " + tableTextColor.getBlue() + ") ";
    tableInit += "fillColor (" + tableFillColor.getRed() + ", "
        + tableFillColor.getGreen() + ", " + tableFillColor.getBlue() + ")";
    lang.addLine(tableInit);
    lang.addLine("setGridValue \"table[][]\" \"\"");
    lang.addLine("setGridValue \"table[0][0]\" \"j\"");
    lang.addLine("setGridValue \"table[0][1]\" \"mj\"");
    lang.addLine("setGridValue \"table[0][2]\" \"cj-1\"");
    lang.addLine("setGridValue \"table[0][3]\" \"mj xor cj-1\"");
    lang.addLine("setGridValue \"table[0][4]\" \"cj\"");

    lang.nextStep("Animation");

    // encrypt blockwise
    int[] cj_1 = iv;
    lang.newText(new Offset(-30, 7, "c_j-1_0", "SW"), "=", "current_cj_1_0",
        null, defaultTextProperties);
    Text current_cj_1 = lang.newText(new Offset(10, 0, "current_cj_1_0", "NE"),
        ivInput, "current_cj_1_1", null, defaultTextProperties);
    current_cj_1.changeColor("color", highlightedTextColor, null, null);
    cj_1Index.setText("0", null, null);
    sc.highlight(0);

    lang.nextStep();
    lang.newText(new Offset(-30, 10, "m_j_0", "SW"), "=", "current_mj0", null,
        defaultTextProperties);
    Text current_mj = lang.newText(new Offset(10, 0, "current_mj0", "NE"), "",
        "current_mj1", null, defaultTextProperties);
    Text current_mj_xor_cj_1 = lang.newText(new Offset(20, 5, "xor-E_e", "NE"),
        "", "current_mj_xor_cj_1", null, defaultTextProperties);
    Text current_cj_0 = lang.newText(new Offset(14, 1, "c_j_0", "NE"), "",
        "current_cj0", null, defaultTextProperties);
    Text current_cj = lang.newText(new Offset(20, 0, "current_cj0", "NE"), "",
        "current_cj1", null, defaultTextProperties);

    int[] c = new int[m2.length];
    int step = 0;

    sc.unhighlight(0);
    for (int i = m2.length - iv.length; i >= 0; i -= iv.length) {
      sc.highlight(1);
      step++;

      int[] mj = new int[iv.length];
      System.arraycopy(m2, i, mj, 0, iv.length);
      current_mj.setText(arrayToString(mj), null, null);
      current_mj.changeColor("color", highlightedTextColor, null, null);
      mjIndex.setText(Integer.toString(step), null, null);
      current_cj_1.setText(arrayToString(cj_1), null, null);
      current_cj_1.changeColor("color", highlightedTextColor, null, null);
      cj_1Index.setText(Integer.toString(step - 1), null, null);
      lang.addLine("setGridValue \"table[" + step + "][0]\" \"" + step + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][0]\"");
      lang.addLine("setGridValue \"table[" + step + "][1]\" \""
          + arrayToString(mj) + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][1]\"");
      lang.addLine("setGridValue \"table[" + step + "][2]\" \""
          + arrayToString(cj_1) + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][2]\"");

      lang.nextStep();
      current_mj.changeColor("color", textColor, null, null);
      current_cj_1.changeColor("color", textColor, null, null);
      sc.toggleHighlight(1, 2);
      int[] mj_xor_cj_1 = xor(mj, cj_1);
      current_mj_xor_cj_1.setText(arrayToString(mj_xor_cj_1), null, null);
      current_mj_xor_cj_1
          .changeColor("color", highlightedTextColor, null, null);
      lang.addLine("unhighlightGridCell \"table[" + step + "][0]\"");
      lang.addLine("unhighlightGridCell \"table[" + step + "][1]\"");
      lang.addLine("unhighlightGridCell \"table[" + step + "][2]\"");
      lang.addLine("setGridValue \"table[" + step + "][3]\" \""
          + arrayToString(mj_xor_cj_1) + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][3]\"");

      lang.nextStep();
      current_mj_xor_cj_1.changeColor("color", textColor, null, null);
      sc.toggleHighlight(2, 3);
      cj_1 = fun.apply(mj_xor_cj_1);
      System.arraycopy(cj_1, 0, c, i, cj_1.length);
      cjIndex.setText(Integer.toString(step), null, null);
      current_cj_0.setText("=", null, null);
      current_cj.setText(arrayToString(cj_1), null, null);
      current_cj.changeColor("color", highlightedTextColor, null, null);
      lang.addLine("unhighlightGridCell \"table[" + step + "][3]\"");
      lang.addLine("setGridValue \"table[" + step + "][4]\" \""
          + arrayToString(cj_1) + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][4]\"");

      lang.nextStep();
      lang.addLine("unhighlightGridCell \"table[" + step + "][4]\"");
      sc.unhighlight(3);
      current_cj.changeColor("color", textColor, null, null);
    }

    sc.highlight(6);
    lang.newText(new Offset(10, 0, "cipher_of_m_0", "NE"), "c = ",
        "cipher_of_m_1", null, defaultTextProperties);
    Text cipher_of_m = lang.newText(new Offset(5, 1, "cipher_of_m_1", "NE"),
        arrayToString(c), "cipher_of_m_2", null, defaultTextProperties);
    cipher_of_m.changeColor("color", highlightedTextColor, null, null);

    lang.nextStep();

    lang.addLine("hideAll");
    lang.addLine("hide \"table\"");
    title.show();
    titleRect.show();

    String informationString0 = "Die Verschlüsselung eines Nachrichtenblocks hängt vom Chiffrat";
    String informationString1 = "des vorherigen Blocks ab. Eine vollständig parallele Verschlüsselung";
    String informationString2 = "der Blöcke ist daher nicht möglich.";
    String informationString3 = "Auf Grund der genannten Abhängigkeit führen Übertragungsfehler";
    String informationString4 = "dazu, dass neben dem direkt betroffenen Chiffratblock auch der";
    String informationString5 = "jeweils folgende Chiffratblock nicht korrekt entschlüsselt werden kann.";
    String informationString6 = "Näheres zur";
    String informationString7 = "Entschlüsselung";
    String informationString8 = "vermittelt die Animation 'Cipher Block";
    String informationString9 = "Chaining Mode (CBC): Entschlüsselung'.";
    lang.newText(new Offset(30, 70, "hRect", "SW"), informationString0,
        "information0", null, defaultTextProperties);
    lang.newText(new Offset(0, 10, "information0", "SW"), informationString1,
        "information1", null, defaultTextProperties);
    lang.newText(new Offset(0, 10, "information1", "SW"), informationString2,
        "information2", null, defaultTextProperties);
    lang.newText(new Offset(0, 20, "information2", "SW"), informationString3,
        "information3", null, defaultTextProperties);
    lang.newText(new Offset(0, 10, "information3", "SW"), informationString4,
        "information4", null, defaultTextProperties);
    lang.newText(new Offset(0, 10, "information4", "SW"), informationString5,
        "information5", null, defaultTextProperties);
    lang.newText(new Offset(0, 20, "information5", "SW"), informationString6,
        "information6", null, defaultTextProperties);
    lang.newText(new Offset(5, 0, "information6", "NE"), informationString7,
        "information7", null, boldTextProperties);
    lang.newText(new Offset(5, 0, "information7", "NE"), informationString8,
        "information8", null, defaultTextProperties);
    lang.newText(new Offset(0, 10, "information6", "SW"), informationString9,
        "information9", null, defaultTextProperties);

    lang.nextStep("Information");
  }

  private String arrayToString(int[] array) {
    String string = "";
    for (int arrayElement : array) {
      string += arrayElement;
    }
    return string;
  }

  interface Function {
    int[] apply(int[] input);
  }

  static class Fun implements Function {

    final int e, mod;

    public Fun(int e, int mod) {
      this.e = e;
      this.mod = mod;
    }

    @Override
    public int[] apply(int[] input) {
      long in = arr2num(input);
      in = (in + e) % mod;
      if (in < 0) {
        in += mod;
      }
      return pad(num2arr(in), input.length);
    }
  }

  public static long arr2num(int[] arr) {
    long result = 0;
    for (int i = arr.length - 1; i >= 0; i--) {
      assert (arr[i] == 0 || arr[i] == 1);
      if (arr[i] == 1) {
        result += Math.pow(2, arr.length - i - 1);
      }
    }
    return result;
  }

  public static int[] num2arr(long num) {
    // Write bits to ArrayList.
    ArrayList<Integer> arr = new ArrayList<Integer>();
    long num2 = num;
    while (num2 != 0) {
      if ((num2 & 1) == 1) {
        arr.add(1);
      } else {
        arr.add(0);
      }
      num2 >>= 1;
    }

    // Convert array list to Integer array.
    Integer[] tmp = new Integer[arr.size()];
    arr.toArray(tmp);

    // Convert Integer array to int array.
    int[] result = new int[arr.size()];
    for (int i = arr.size() - 1; i >= 0; i--) {
      result[arr.size() - i - 1] = tmp[i];
    }

    return result;
  }

  public static int[] pad(int[] arr, int length) {
    assert (arr.length <= length);
    int[] result = new int[length];
    System.arraycopy(arr, 0, result, length - arr.length, arr.length);
    return result;
  }

  public static int xor(int bit1, int bit2) {
    assert (bit1 == 0 || bit1 == 1);
    assert (bit2 == 0 || bit2 == 1);
    return !(bit1 == bit2) ? 1 : 0;
  }

  public static int[] xor(int[] bitStr1, int[] bitStr2) {
    int[] result = new int[bitStr1.length];
    for (int i = 0; i < bitStr1.length; i++) {
      result[i] = xor(bitStr1[i], bitStr2[i]);
    }
    return result;
  }

  public String getName() {
    return "Cipher Block Chaining Mode (CBC): Verschl&uuml;sselung";
  }

  public String getAlgorithmName() {
    return "Cipher Block Chaining Mode (CBC)";
  }

  public String getAnimationAuthor() {
    return "Stephan Cornelius Arndt, Felix Gündling";
  }

  public String getDescription() {
    return "Der Cipher Block Chaining Mode (CBC) ist eine Betriebsart, Klartexte <br/>"
        + "\n"
        + "blockweise zu verschl&uuml;sseln. Hierbei wird ein Klartextblock vor dessen <br/>"
        + "\n"
        + "Verschl&uuml;sselung durch die Verschl&uuml;sselungsfunktion E stets per XOR <br/>"
        + "\n"
        + "mit dem Chiffrat des vorherigen Klartextblocks verkn&uuml;pft. <br/>";
  }

  public String getCodeExample() {
    return "CBC-Verschl&uuml;sselung einer Nachricht 'm'"
        + "\n"
        + "mit Hilfe eines Initialisierungsvektors 'iv' und einer Funktion E_e:"
        + "\n"
        + "\n"
        + "cbc_encrypt(iv, m)"
        + "\n"
        + "\n"
        + "     c0 = iv;"
        + "\n"
        + "     for j = 1,...,k do"
        + "\n"
        + "          mj_xor_cj-1 = mj xor cj-1;"
        + "\n"
        + "          cj = E_e(mj_xor_cj-1);"
        + "\n"
        + "     end"
        + "\n"
        + "\n"
        + "     c = ck ck-1 ... c1;"
        + "\n"
        + "     return c;"
        + "\n"
        + "\n"
        + "k bezeichnet die Anzahl der n Bit breiten Bl&ouml;cke in m. Die hier"
        + "\n"
        + "vorgestellte Animation des Algorithmus arbeitet mit Funktionen E_e"
        + "\n"
        + "der Form E_e(in) = (in + e) mod (2^n), wobei e ein einstellbarer Schl&uuml;ssel ist.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public static void main(String[] args) {
    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    primitives.put("m", new int[] { 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1 });
    primitives.put("iv", new int[] { 1, 0, 0, 1, 1 });
    primitives.put("e", 50);

    AnimationPropertiesContainer props = new AnimationPropertiesContainer();

    TextProperties headlineProperties = new TextProperties("headlines");
    headlineProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 14));
    headlineProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    props.add(headlineProperties);

    TextProperties defaultTextProperties = new TextProperties("text");
    defaultTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    defaultTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);
    props.add(defaultTextProperties);

    TextProperties highlightedTextProperties = new TextProperties(
        "highlightedText");
    highlightedTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.RED);
    props.add(highlightedTextProperties);

    MatrixProperties tableProperties = new MatrixProperties("table");
    tableProperties
        .set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    tableProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tableProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    tableProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.RED);
    props.add(tableProperties);

    SourceCodeProperties sourceCodeProperties = new SourceCodeProperties(
        "sourceCode");
    sourceCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);
    sourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "MonoSpaced", Font.PLAIN, 12));
    sourceCodeProperties.set(AnimationPropertiesKeys.SIZE_PROPERTY, 14);
    props.add(sourceCodeProperties);

    CBCEncrypt encryption = new CBCEncrypt();
    encryption.init();
    System.out.println(encryption.generate(props, primitives));
  }

}