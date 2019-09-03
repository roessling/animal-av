package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;

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

public class CBCDecrypt implements Generator {
  private Language             lang;
  private int                  d;
  private int[]                iv;
  private int[]                c;
  private TextProperties       text;
  private SourceCodeProperties sourceCode;
  private TextProperties       highlightedText;
  private MatrixProperties     table;
  private TextProperties       headlines;

  public void init() {
    lang = new AnimalScript(
        "Cipher Block Chaining Mode (CBC): Entschlüsselung",
        "Stephan Cornelius Arndt<cornelius@alceta.de>,Felix Gündling<felix.guendling@gmx.de>",
        800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    d = (Integer) primitives.get("d");
    iv = (int[]) primitives.get("iv");
    c = (int[]) primitives.get("c");

    text = (TextProperties) props.getPropertiesByName("text");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    highlightedText = (TextProperties) props
        .getPropertiesByName("highlightedText");
    table = (MatrixProperties) props.getPropertiesByName("table");
    headlines = (TextProperties) props.getPropertiesByName("headlines");

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);
    lang.addQuestionGroup(groupInfo);

    decrypt(iv, c, d);

    lang.finalizeGeneration();

    return lang.toString();
  }

  public void decrypt(int[] iv, int[] c, int d) {
    // Title
    TextProperties headerProperties = new TextProperties();
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    Text title = lang.newText(new Coordinates(30, 40),
        "Cipher Block Chaining Mode (CBC): Entschlüsselung", "header", null,
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
    String descriptionString3 = "stets per XOR mit dem Chiffrat des vorherigen Klartextblocks verknöpft.";
    String descriptionString4 = "Bei der Entschlüsselung eines Chiffratblockes bedarf es demzufolge";
    String descriptionString5 = "nach Anwenden der Entschlüsselungsfunktion D der Verknüpfung per";
    String descriptionString6 = "XOR mit dem vorherigen Chiffratsblock.";
    Text description0 = lang.newText(new Offset(30, 70, "hRect", "SW"),
        descriptionString0, "description0", null, defaultTextProperties);
    Text description1 = lang.newText(new Offset(0, 10, "description0", "SW"),
        descriptionString1, "description1", null, defaultTextProperties);
    Text description2 = lang.newText(new Offset(0, 10, "description1", "SW"),
        descriptionString2, "description2", null, defaultTextProperties);
    Text description3 = lang.newText(new Offset(0, 10, "description2", "SW"),
        descriptionString3, "description3", null, defaultTextProperties);
    Text description4 = lang.newText(new Offset(0, 20, "description3", "SW"),
        descriptionString4, "description4", null, defaultTextProperties);
    Text description5 = lang.newText(new Offset(0, 10, "description4", "SW"),
        descriptionString5, "description5", null, defaultTextProperties);
    Text description6 = lang.newText(new Offset(0, 10, "description5", "SW"),
        descriptionString6, "description6", null, defaultTextProperties);

    lang.nextStep("Einführung");
    description0.hide();
    description1.hide();
    description2.hide();
    description3.hide();
    description4.hide();
    description5.hide();
    description6.hide();

    // Input values
    lang.newText(new Offset(0, 30, "hRect", "SW"), "Eingangswerte:",
        "input_values", null, headlineProperties);
    String cInput = "c = " + arrayToString(c) + ",";
    lang.newText(new Offset(10, 0, "input_values", "NE"), cInput, "cInput",
        null, defaultTextProperties);
    String ivInput = arrayToString(iv);
    lang.newText(new Offset(10, 0, "cInput", "NE"), "iv = " + ivInput,
        "ivInput", null, defaultTextProperties);
    lang.newText(new Offset(0, 10, "input_values", "SW"),
        "Entschlüsselungsfunktion:", "function_D_0", null, headlineProperties);
    lang.newText(new Offset(10, 0, "function_D_0", "NE"), "D", "function_D_1",
        null, defaultTextProperties);
    lang.newText(new Offset(1, 5, "function_D_1", "NE"), "d", "function_D_2",
        null, indicesTextProperties);
    String function = "(cj) = (cj "
        + ((d > 0) ? ("+ " + d) : ("- " + Math.abs(d))) + ") mod "
        + (1 << iv.length);
    lang.newText(new Offset(10, 2, "function_D_1", "NE"), function,
        "function_D_3", null, defaultTextProperties);

    // Visualization
    lang.newText(new Offset(30, 100, "hRect", "SW"), "c", "c_j_0", null,
        defaultTextProperties);
    Text cjIndex = lang.newText(new Offset(1, 5, "c_j_0", "NE"), "j", "c_j_1",
        null, indicesTextProperties);

    PolylineProperties p = new PolylineProperties();
    p.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    lang.newPolyline(new Node[] { new Offset(20, -8, "c_j_0", "SW"),
        new OffsetFromLastPosition(70, 0), new OffsetFromLastPosition(0, 50) },
        "c_j-D", null, p);
    lang.newPolyline(new Node[] { new Offset(26, 7, "c_j-D", "NW"),
        new Offset(40, -7, "c_j-D", "NW") }, "line_n_0", null);
    lang.newText(new Offset(27, -20, "c_j-D", "NW"), "n", "n_0", null,
        defaultTextProperties);
    lang.newSquare(new Offset(-30, 0, "c_j-D", "SE"), 60, "D_d", null);
    lang.newText(new Offset(23, 20, "D_d", "NW"), "D", "D_d_0", null,
        defaultTextProperties);
    lang.newText(new Offset(1, 5, "D_d_0", "NE"), "d", "D_d_1", null,
        indicesTextProperties);
    lang.newPolyline(new Node[] { new Offset(50, 30, "D_d", "NE"),
        new Offset(4, 30, "D_d", "NE") }, "d-D_d", null, p);
    lang.newText(new Offset(10, -7, "d-D_d", "NE"), "d", "d", null,
        defaultTextProperties);
    CircleProperties filled = new CircleProperties();
    filled.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    lang.newCircle(new Offset(0, 30, "D_d", "NE"), 4, "d_circle", null, filled);
    lang.newPolyline(new Node[] { new Offset(30, 0, "D_d", "SW"),
        new OffsetFromLastPosition(0, 66) }, "D_d-xor", null, p);
    lang.newPolyline(new Node[] { new Offset(-7, 32, "D_d-xor", "NW"),
        new Offset(7, 18, "D_d-xor", "NW") }, "line_n_1", null);
    lang.newText(new Offset(10, 18, "D_d-xor", "NW"), "n", "n_1", null,
        defaultTextProperties);
    lang.newCircle(new Offset(0, 14, "D_d-xor", "SW"), 10, "xor_0", null);
    lang.newPolyline(new Node[] { new Offset(10, 0, "xor_0", "NW"),
        new Offset(10, 20, "xor_0", "NW") }, "xor_1", null);
    lang.newPolyline(new Node[] { new Offset(-10, 10, "xor_1", "NW"),
        new Offset(10, 10, "xor_1", "NW") }, "xor_2", null);

    lang.newPolyline(
        new Node[] { new Offset(0, 0, "c_j-D", "NE"),
            new OffsetFromLastPosition(180, 0),
            new OffsetFromLastPosition(0, 165) }, "c_j-c_j-1", null, p);
    lang.newRect(new Offset(-70, 0, "c_j-c_j-1", "SE"), new Offset(70, 50,
        "c_j-c_j-1", "SE"), "rect_c_j-1", null);
    lang.newText(new Offset(60, 5, "rect_c_j-1", "NW"), "c", "c_j-1_0", null,
        defaultTextProperties);
    Text cj_1Index = lang.newText(new Offset(1, 5, "c_j-1_0", "NE"), "j-1",
        "c_j-1_1", null, indicesTextProperties);
    lang.newPolyline(new Node[] { new Offset(0, 25, "rect_c_j-1", "NW"),
        new OffsetFromLastPosition(-96, 0), }, "c_j-1-xor", null, p);
    lang.newPolyline(new Node[] { new Offset(10, 4, "xor_0", "SW"),
        new OffsetFromLastPosition(0, 66), }, "xor-m_j", null, p);
    lang.newPolyline(new Node[] { new Offset(-7, 32, "xor-m_j", "NW"),
        new Offset(7, 18, "xor-m_j", "NW"), }, "line_n_2", null);
    lang.newText(new Offset(10, 18, "xor-m_j", "NW"), "n", "n_2", null,
        defaultTextProperties);
    lang.newText(new Offset(-6, 10, "xor-m_j", "SW"), "m", "m_j_0", null,
        defaultTextProperties);
    Text mjIndex = lang.newText(new Offset(1, 5, "m_j_0", "NE"), "j", "m_j_1",
        null, indicesTextProperties);

    // Output value
    lang.newText(new Offset(0, 420, "hRect", "SW"), "Klartext von c:",
        "plaintext_of_c_0", null, headlineProperties);

    // Algorithm
    lang.newText(new Offset(500, 0, "input_values", "NW"), "Algorithmus:",
        "algorithm", null, headlineProperties);
    SourceCode sc = lang.newSourceCode(new Offset(30, 0, "algorithm", "SW"),
        "code", null, sourceCode);
    sc.addCodeLine("c0 = iv;", null, 0, null);
    sc.addCodeLine("for j = 1,...,k do", null, 0, null);
    sc.addCodeLine("Dd_cj = D_d(cj);", null, 1, null);
    sc.addCodeLine("mj = Dd_cj xor cj-1;", null, 1, null);
    sc.addCodeLine("end", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("m = mk mk-1 ... m1;", null, 0, null);
    lang.newText(new Offset(0, 20, "code", "SW"),
        "(wobei k die Anzahl der n Bit breiten Blöcke in m bezeichnet)",
        "algo_info", null, defaultTextProperties);

    // Split layout line
    Polyline splitline = lang.newPolyline(new Node[] {
        new Offset(-50, 0, "algorithm", "NW"),
        new Offset(-50, 410, "algorithm", "NW") }, "split_layout", null);
    splitline.changeColor("color", Color.LIGHT_GRAY, null, null);

    // Encrypt function D
    Function fun = new Fun(d, 1 << iv.length);

    // Protocol table
    lang.newText(new Offset(0, 220, "algorithm", "NW"), "Protokoll:",
        "protocol", null, headlineProperties);
    int lines = c.length / iv.length + 1;
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
    lang.addLine("setGridValue \"table[0][1]\" \"cj\"");
    lang.addLine("setGridValue \"table[0][2]\" \"cj-1\"");
    lang.addLine("setGridValue \"table[0][3]\" \"Dd_cj\"");
    lang.addLine("setGridValue \"table[0][4]\" \"mj\"");

    lang.nextStep("Animation");

    // decrypt blockwise
    int[] cj_1 = iv;
    lang.newText(new Offset(-30, 7, "c_j-1_0", "SW"), "=", "current_cj_1_0",
        null, defaultTextProperties);
    Text current_cj_1 = lang.newText(new Offset(10, 0, "current_cj_1_0", "NE"),
        ivInput, "current_cj_1_1", null, defaultTextProperties);
    current_cj_1.changeColor("color", highlightedTextColor, null, null);
    cj_1Index.setText("0", null, null);
    sc.highlight(0);

    lang.nextStep();
    lang.newText(new Offset(-30, 10, "c_j_0", "SW"), "=", "current_cj0", null,
        defaultTextProperties);
    Text current_cj = lang.newText(new Offset(10, 0, "current_cj0", "NE"), "",
        "current_cj1", null, defaultTextProperties);
    Text current_Dd_cj = lang.newText(new Offset(-80, 18, "D_d-xor", "NE"), "",
        "current_Dd_cj", null, defaultTextProperties);
    Text current_mj_0 = lang.newText(new Offset(14, 1, "m_j_0", "NE"), "",
        "current_mj0", null, defaultTextProperties);
    Text current_mj = lang.newText(new Offset(20, 0, "current_mj0", "NE"), "",
        "current_mj1", null, defaultTextProperties);

    int[] m = new int[c.length];
    int[] mj = new int[iv.length];
    int[] cj = new int[iv.length];
    int[] dd_cj = new int[iv.length];

    int step = 0;

    sc.unhighlight(0);
    for (int i = c.length - iv.length; i >= 0; i -= iv.length) {
      sc.highlight(1);
      step++;

      System.arraycopy(c, i, cj, 0, iv.length);
      cjIndex.setText(Integer.toString(step), null, null);
      current_cj.setText(arrayToString(cj), null, null);
      current_cj.changeColor("color", highlightedTextColor, null, null);
      current_cj_1.setText(arrayToString(cj_1), null, null);
      current_cj_1.changeColor("color", highlightedTextColor, null, null);
      cj_1Index.setText(Integer.toString(step - 1), null, null);
      lang.addLine("setGridValue \"table[" + step + "][0]\" \"" + step + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][0]\"");
      lang.addLine("setGridValue \"table[" + step + "][1]\" \""
          + arrayToString(cj) + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][1]\"");
      lang.addLine("setGridValue \"table[" + step + "][2]\" \""
          + arrayToString(cj_1) + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][2]\"");

      lang.nextStep();
      current_cj.changeColor("color", textColor, null, null);
      current_cj_1.changeColor("color", textColor, null, null);
      sc.toggleHighlight(1, 2);
      dd_cj = fun.apply(cj);
      lang.addLine("unhighlightGridCell \"table[" + step + "][0]\"");
      lang.addLine("unhighlightGridCell \"table[" + step + "][1]\"");
      lang.addLine("unhighlightGridCell \"table[" + step + "][2]\"");
      lang.addLine("setGridValue \"table[" + step + "][3]\" \""
          + arrayToString(dd_cj) + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][3]\"");
      current_Dd_cj.setText(arrayToString(dd_cj), null, null);
      current_Dd_cj.changeColor("color", highlightedTextColor, null, null);

      lang.nextStep();
      mj = xor(cj_1, dd_cj);

      // Interaction: Question
      if (step == 2) {
        FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
            "fillInBlanksQuestion");
        fibq.setPrompt("Wie lautet der Klartextblock m" + step
            + " zum Chiffratblock c" + step + "?");
        fibq.addAnswer(arrayToString(mj), 10, "Korrekte Antwort!");
        fibq.setGroupID("First question group");
        lang.addFIBQuestion(fibq);
        lang.nextStep();
      }

      current_Dd_cj.changeColor("color", textColor, null, null);
      sc.toggleHighlight(2, 3);
      lang.addLine("unhighlightGridCell \"table[" + step + "][3]\"");
      lang.addLine("setGridValue \"table[" + step + "][4]\" \""
          + arrayToString(mj) + "\"");
      lang.addLine("highlightGridCell \"table[" + step + "][4]\"");
      mjIndex.setText(Integer.toString(step), null, null);
      current_mj_0.setText("=", null, null);
      current_mj.setText(arrayToString(mj), null, null);
      current_mj.changeColor("color", highlightedTextColor, null, null);

      lang.nextStep();
      lang.addLine("unhighlightGridCell \"table[" + step + "][4]\"");
      sc.unhighlight(3);
      current_mj.changeColor("color", textColor, null, null);
      System.arraycopy(cj, 0, cj_1, 0, iv.length);
      System.arraycopy(mj, 0, m, i, iv.length);
    }

    sc.highlight(6);
    lang.newText(new Offset(10, 0, "plaintext_of_c_0", "NE"), "m = ",
        "plaintext_of_c_1", null, defaultTextProperties);
    Text plaintext_of_c = lang.newText(new Offset(5, 1, "plaintext_of_c_1",
        "NE"), arrayToString(m), "plaintext_of_c_2", null,
        defaultTextProperties);
    plaintext_of_c.changeColor("color", highlightedTextColor, null, null);

    lang.nextStep();

    lang.addLine("hideAll");
    lang.addLine("hide \"table\"");
    title.show();
    titleRect.show();

    String informationString0 = "Die Entschlüsselung eines Chiffratblocks hängt vom vorherigen";
    String informationString1 = "Chiffratblock ab. Da alle Chiffratblöcke bekannt sind, ist auch";
    String informationString2 = "eine parallele Entschlüsselung der Blöcke möglich.";
    String informationString3 = "Auf Grund der genannten Abhängigkeit führen Übertragungsfehler";
    String informationString4 = "dazu, dass neben dem direkt betroffenen Chiffratblock auch der";
    String informationString5 = "jeweils folgende Chiffratblock nicht korrekt entschlüsselt werden kann.";
    String informationString6 = "Näheres zur";
    String informationString7 = "Verschlüsselung";
    String informationString8 = "vermittelt die Animation 'Cipher Block";
    String informationString9 = "Chaining Mode (CBC): Verschlüsselung'.";
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

    final int add, mod;

    public Fun(int add, int mod) {
      this.add = add;
      this.mod = mod;
    }

    @Override
    public int[] apply(int[] input) {
      long in = arr2num(input);
      in = (in + add) % mod;
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
    return "Cipher Block Chaining Mode (CBC): Entschl&uuml;sselung";
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
        + "mit dem Chiffrat des vorherigen Klartextblocks verkn&uuml;pft. <br/>"
        + "\n"
        + "<br/>"
        + "\n"
        + "Die Entschl&uuml;sselung eines Chiffrats erfolgt dann ebenfalls blockweise. <br/>"
        + "\n"
        + "Nach der Entschl&uuml;sselung eines Chiffratblocks durch die Entschl&uuml;sselungsfunktion <br/>"
        + "\n"
        + "D wird das so enstehende Zwischenresultat per XOR mit dem vorherigen <br/>"
        + "\n"
        + "Chiffratblock verkn&uuml;pft. Die so resultierenden Klartextbl&ouml;cke ergeben dann <br/>"
        + "\n" + "zusammen den Klartext zum gegebenen Chiffrat.";
  }

  public String getCodeExample() {
    return "CBC-Entschl&uuml;sselung eines Chiffrats 'c'"
        + "\n"
        + "mit Hilfe eines Initialisierungsvektors 'iv' und einer Funktion D_d:"
        + "\n"
        + "\n"
        + "cbc_decrypt(iv, c)"
        + "\n"
        + "\n"
        + "     c0 = iv;"
        + "\n"
        + "     for j = 1,...,k do"
        + "\n"
        + "          Dd_cj = D_d(cj);"
        + "\n"
        + "          mj = Dd_cj xor cj-1;"
        + "\n"
        + "     end"
        + "\n"
        + "\n"
        + "     m = mk mk-1 ... m1;"
        + "\n"
        + "     return m;"
        + "\n"
        + "\n"
        + "k bezeichnet die Anzahl der n Bit breiten Bl&ouml;cke in c. Die hier"
        + "\n"
        + "vorgestellte Animation des Algorithmus arbeitet mit Funktionen D_d"
        + "\n"
        + "der Form D_d(cj) = (cj + d) mod (2^n), wobei d ein einstellbarer Schl&uuml;ssel ist.";
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
    primitives.put("c",
        new int[] { 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0 });
    primitives.put("iv", new int[] { 1, 0, 0, 1, 1 });
    primitives.put("d", -50);

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

    CBCDecrypt decryption = new CBCDecrypt();
    decryption.init();
    System.out.println(decryption.generate(props, primitives));
  }

}