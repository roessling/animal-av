package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Levenshtein implements Generator {
  private Language                     lang;
  private String                       firstWord;
  private TextProperties               text;
  private TextProperties               textHighlight;
  private TextProperties               matrix;
  private SourceCodeProperties         code;
  private String                       secondWord;
  private TextProperties               header;

  private HashMap<String, Text>        textElements;
  private HashMap<String, Object>      properties;
  private HashMap<String, StringArray> arrayElements;
  private HashMap<String, SourceCode>  codeElements;

  public void init() {
    lang = new AnimalScript("Levenshtein-Algorithmus [DE]",
        "Arvid Lange, Marco Torsello", 1024, 768);

    lang.setStepMode(true);

    this.textElements = new HashMap<String, Text>();
    this.properties = new HashMap<String, Object>();
    this.arrayElements = new HashMap<String, StringArray>();
    this.codeElements = new HashMap<String, SourceCode>();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    firstWord = (String) primitives.get("first word");
    text = (TextProperties) props.getPropertiesByName("text");
    textHighlight = (TextProperties) props
        .getPropertiesByName("text highlight");
    matrix = (TextProperties) props.getPropertiesByName("matrix");
    code = (SourceCodeProperties) props.getPropertiesByName("code");
    secondWord = (String) primitives.get("second word");
    header = (TextProperties) props.getPropertiesByName("header");

    text.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));
    properties.put("infoProps", text);

    textHighlight.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    properties.put("infoRedProps", textHighlight);

    properties.put("algoProps", code);

    matrix.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    properties.put("matrixProps", matrix);

    header.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    properties.put("headerProps", header);

    run(firstWord, secondWord);

    return lang.toString();
  }

  public String getName() {
    return "Levenshtein-Algorithmus [DE]";
  }

  public String getAlgorithmName() {
    return "Levenshtein";
  }

  public String getAnimationAuthor() {
    return "Arvid Lange, Marco Torsello";
  }

  public String getDescription() {
    return "Der Levenshtein Algorithmus errechnet die Levenshtein-Distanz, die den Unterschied zwischen "
        + "\n"
        + "zwei W&ouml;rtern als nat&uuml;rliche Zahl ausdr&uuml;ckt."
        + "\n"
        + "Die Distanz wird &uuml;ber die Anzahl der Operationen berrechnet, die man ben&ouml;tigt, um das zweite"
        + "\n"
        + "Wort in das Erste zu &uuml;berf&uuml;hren, wobei sich alle Operationen auf einzelne Buchstaben beziehen.";
  }

  public String getCodeExample() {
    return "for (int i = 1; i <= m; i++){"
        + "\n"
        + "    for (int j = 1; j <= n; j++){"
        + "\n"
        + "        // if u[i] == v[j] then the current position is filled with"
        + "\n"
        + "        // matrix[i-1][j-1] + 0"
        + "\n"
        + "        if (uArray[i] == vArray[j]){"
        + "\n"
        + "            same = matrix[i-1][j-1];"
        + "\n"
        + "        } else {"
        + "\n"
        + "            same = Integer.MAX_VALUE;"
        + "\n"
        + "        }"
        + "\n"
        + "\n"
        + "        // replace has to be automatically set to matrix[i-1][j-1] + 1 to"
        + "\n"
        + "        // have a value to compare, even if u[i] == v[i]"
        + "\n"
        + "        replace = matrix[i-1][j-1] + 1;"
        + "\n"
        + "    "
        + "\n"
        + "        // delete has to be filled with matrix[i-1][j] for comparison"
        + "\n"
        + "        delete = matrix[i-1][j] + 1;"
        + "\n"
        + "    			"
        + "\n"
        + "        // paste has to be filled with matrix[i][j-1] for comparison"
        + "\n"
        + "        paste = matrix[i][j-1] + 1;"
        + "\n"
        + "    "
        + "\n"
        + "         // matrix[i][j] is set to the minimum of the gathered values"
        + "\n"
        + "         matrix[i][j] = Math.min(Math.min(same, replace), Math.min(paste, delete));"
        + "\n" + "    }" + "\n" + "}";
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

  /**
   * Properties for elements used in the animation
   */
  private void defineProps() {

    // array properties
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    arrayProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("arrayProps", arrayProps);

    // matrix properties for highlighted elements
    TextProperties matrixRedProps = new TextProperties();
    matrixRedProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    matrixRedProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    matrixRedProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
    properties.put("matrixRedProps", matrixRedProps);

    // square properties
    SquareProperties squareProps = new SquareProperties();
    squareProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    squareProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    squareProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("squareProps", squareProps);
  }

  /**
   * Text for the animation
   */
  private void defineText() {
    // header text
    Text header = lang.newText(new Coordinates(20, 20),
        "Levenshtein Algorithmus", "header", null,
        (TextProperties) properties.get("headerProps"));
    textElements.put("header", header);

    // intro text
    Text intro0 = lang
        .newText(
            new Offset(0, 50, "header", AnimalScript.DIRECTION_NW),
            "Als Levenshtein-Distanz wird die Anzahl an Operationen auf Buchstaben bezeichnet, die man benötigt,",
            "intro0", null, (TextProperties) properties.get("infoProps"));

    Text intro1 = lang.newText(new Offset(0, 30, "intro0",
        AnimalScript.DIRECTION_NW),
        "um ein Wort in ein Zweites zu überführen.", "intro1", null,
        (TextProperties) properties.get("infoProps"));

    Text intro2 = lang
        .newText(
            new Offset(0, 30, "intro1", AnimalScript.DIRECTION_NW),
            "Ein populäres Beispiel für die Anwendung des Levenshtein-Algorithmus sind Rechtschreibhilfen an",
            "intro2", null, (TextProperties) properties.get("infoProps"));

    Text intro3 = lang
        .newText(
            new Offset(0, 30, "intro2", AnimalScript.DIRECTION_NW),
            "Eingabefeldern, wie zum Beispiel in der Suchmaschine Google (Meinten Sie: ... ).",
            "intro3", null, (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("intro0", intro0);
    textElements.put("intro1", intro1);
    textElements.put("intro2", intro2);
    textElements.put("intro3", intro3);

    // operation info text
    Text info0 = lang
        .newText(
            new Offset(0, 50, "header", AnimalScript.DIRECTION_NW),
            "Um ein Wort zu verändern sind folgende drei Operationen auf den Buchstaben möglich:",
            "info0", null, (TextProperties) properties.get("infoProps"));
    Text info1 = lang.newText(new Offset(10, 40, "info0",
        AnimalScript.DIRECTION_NW), "- Ersetzen", "info1", null,
        (TextProperties) properties.get("infoRedProps"));
    Text info2 = lang.newText(new Offset(0, 30, "info1",
        AnimalScript.DIRECTION_NW), "- Löschen", "info2", null,
        (TextProperties) properties.get("infoRedProps"));
    Text info3 = lang.newText(new Offset(0, 30, "info2",
        AnimalScript.DIRECTION_NW), "- Einfügen", "info3", null,
        (TextProperties) properties.get("infoRedProps"));
    // fill HashMap
    textElements.put("info0", info0);
    textElements.put("info1", info1);
    textElements.put("info2", info2);
    textElements.put("info3", info3);

    // text for individual word input
    Text input0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Das Beispiel behandelt die beiden Wörter: ", "input0", null,
        (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("input0", input0);

    // length info text
    Text info4 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Es wird eine (m+1 x n+1)-Matrix erzeugt, wobei", "info4", null,
        (TextProperties) properties.get("infoProps"));
    Text info5 = lang.newText(new Offset(10, 40, "info4",
        AnimalScript.DIRECTION_NW), "m = |u|", "info5", null,
        (TextProperties) properties.get("infoRedProps"));
    Text info6 = lang.newText(new Offset(-10, 40, "info5",
        AnimalScript.DIRECTION_NW), "die Länge des ersten Wortes u und",
        "info6", null, (TextProperties) properties.get("infoProps"));
    Text info7 = lang.newText(new Offset(10, 40, "info6",
        AnimalScript.DIRECTION_NW), "n = |v|", "info7", null,
        (TextProperties) properties.get("infoRedProps"));
    Text info8 = lang.newText(new Offset(-10, 40, "info7",
        AnimalScript.DIRECTION_NW), "die Länge des zweiten Wortes v ist.",
        "info7", null, (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("info4", info4);
    textElements.put("info5", info5);
    textElements.put("info6", info6);
    textElements.put("info7", info7);
    textElements.put("info8", info8);

    // matrix text
    Text matrix0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Anschliessend wird die Matrix D initiert, sodass sie aus", "matrix0",
        null, (TextProperties) properties.get("infoProps"));
    Text matrix1 = lang.newText(new Offset(10, 40, "matrix0",
        AnimalScript.DIRECTION_NW), "m+1 Spalten", "matrix1", null,
        (TextProperties) properties.get("infoRedProps"));
    Text matrix2 = lang.newText(new Offset(-10, 40, "matrix1",
        AnimalScript.DIRECTION_NW), "und", "matrix2", null,
        (TextProperties) properties.get("infoProps"));
    Text matrix3 = lang.newText(new Offset(10, 40, "matrix2",
        AnimalScript.DIRECTION_NW), "n+1 Zeilen", "matrix3", null,
        (TextProperties) properties.get("infoRedProps"));
    Text matrix4 = lang.newText(new Offset(-10, 40, "matrix3",
        AnimalScript.DIRECTION_NW), "besteht.", "matrix4", null,
        (TextProperties) properties.get("infoProps"));
    Text matrix5 = lang.newText(new Offset(0, 30, "matrix4",
        AnimalScript.DIRECTION_NW), "Nun wird D[0][0] auf den Wert 0 gesetzt.",
        "matrix5", null, (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("matrix0", matrix0);
    textElements.put("matrix1", matrix1);
    textElements.put("matrix2", matrix2);
    textElements.put("matrix3", matrix3);
    textElements.put("matrix4", matrix4);
    textElements.put("matrix5", matrix5);

    // Description of the matrix operations
    Text desc0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Nun folgt die Initiierung der ersten Zeile mit", "desc0", null,
        (TextProperties) properties.get("infoProps"));
    Text desc1 = lang.newText(new Offset(10, 40, "desc0",
        AnimalScript.DIRECTION_NW), "D[i][0] = i, 1 <= i <= m.", "desc1", null,
        (TextProperties) properties.get("infoRedProps"));
    Text desc2 = lang.newText(new Offset(-10, 40, "desc1",
        AnimalScript.DIRECTION_NW), "Gleiches gilt für die erste Spalte mit",
        "desc2", null, (TextProperties) properties.get("infoProps"));
    Text desc3 = lang.newText(new Offset(10, 40, "desc2",
        AnimalScript.DIRECTION_NW), "D[0][j] = j, 1 <= j <= n.", "desc3", null,
        (TextProperties) properties.get("infoRedProps"));
    // fill HashMap
    textElements.put("desc0", desc0);
    textElements.put("desc1", desc1);
    textElements.put("desc2", desc2);
    textElements.put("desc3", desc3);

    Text desc4 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Nach diesen Vorbereitungen folgt das eigentliche", "desc4", null,
        (TextProperties) properties.get("infoProps"));
    Text desc5 = lang.newText(new Offset(0, 30, "desc4",
        AnimalScript.DIRECTION_NW),
        "Vergleichen der Buchstaben beider Wörter.", "desc5", null,
        (TextProperties) properties.get("infoProps"));
    Text desc6 = lang.newText(new Offset(0, 30, "desc5",
        AnimalScript.DIRECTION_NW),
        "Nun werden zwei Buchstaben aus den Wörtern an", "desc6", null,
        (TextProperties) properties.get("infoProps"));
    Text desc7 = lang.newText(new Offset(0, 30, "desc6",
        AnimalScript.DIRECTION_NW),
        "der gleichen Position verglichen, sowie die", "desc7", null,
        (TextProperties) properties.get("infoProps"));
    Text desc8 = lang.newText(new Offset(0, 30, "desc7",
        AnimalScript.DIRECTION_NW), "genannten Operation durchgeführt.",
        "desc8", null, (TextProperties) properties.get("infoProps"));
    Text desc9 = lang.newText(new Offset(0, 30, "desc8",
        AnimalScript.DIRECTION_NW), "Die Operationen funktionieren wie folgt:",
        "desc9", null, (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("desc4", desc4);
    textElements.put("desc5", desc5);
    textElements.put("desc6", desc6);
    textElements.put("desc7", desc7);
    textElements.put("desc8", desc8);
    textElements.put("desc9", desc9);

    // Operation "replace" text
    Text replace0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW), "Ersetzen", "replace0", null,
        (TextProperties) properties.get("infoRedProps"));
    Text replace1 = lang.newText(new Offset(0, 30, "replace0",
        AnimalScript.DIRECTION_NW),
        "Man nimmt sich aus der Matrix den Wert diagonal", "replace1", null,
        (TextProperties) properties.get("infoProps"));
    Text replace2 = lang.newText(new Offset(0, 30, "replace1",
        AnimalScript.DIRECTION_NW),
        "über dem aktuellen Feld, also D[i-1][j-1].", "replace2", null,
        (TextProperties) properties.get("infoProps"));
    Text replace3 = lang.newText(new Offset(0, 30, "replace2",
        AnimalScript.DIRECTION_NW),
        "Auf diesen addiert man nun 1. Mathematisch also:", "replace3", null,
        (TextProperties) properties.get("infoProps"));
    Text replace4 = lang.newText(new Offset(10, 40, "replace3",
        AnimalScript.DIRECTION_NW), "D[i-1][j-1] + 1", "replace4", null,
        (TextProperties) properties.get("infoRedProps"));
    // fill HashMap
    textElements.put("replace0", replace0);
    textElements.put("replace1", replace1);
    textElements.put("replace2", replace2);
    textElements.put("replace3", replace3);
    textElements.put("replace4", replace4);

    // Operation "delete" text
    Text delete0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW), "Löschen", "delete0", null,
        (TextProperties) properties.get("infoRedProps"));
    Text delete1 = lang.newText(new Offset(0, 30, "delete0",
        AnimalScript.DIRECTION_NW),
        "Für die Löschen Operation nimmt man sich den Wert D[i-1][j]",
        "delete1", null, (TextProperties) properties.get("infoProps"));
    Text delete2 = lang.newText(new Offset(0, 30, "delete1",
        AnimalScript.DIRECTION_NW),
        "und addiert auf diesen 1. Mathematisch also wieder:", "delete2", null,
        (TextProperties) properties.get("infoProps"));
    Text delete3 = lang.newText(new Offset(10, 40, "delete2",
        AnimalScript.DIRECTION_NW), "D[i-1][j] + 1", "delete3", null,
        (TextProperties) properties.get("infoRedProps"));
    // fill HashMap
    textElements.put("delete0", delete0);
    textElements.put("delete1", delete1);
    textElements.put("delete2", delete2);
    textElements.put("delete3", delete3);

    // Operation "insert" text
    Text insert0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW), "Einfügen", "insert0", null,
        (TextProperties) properties.get("infoRedProps"));
    Text insert1 = lang.newText(new Offset(0, 30, "insert0",
        AnimalScript.DIRECTION_NW), "Hier nimmt man sich den Wert D[i][j-1]",
        "insert1", null, (TextProperties) properties.get("infoProps"));
    Text insert2 = lang.newText(new Offset(0, 30, "insert1",
        AnimalScript.DIRECTION_NW), "und addiert auf diesen 1. Mathematisch:",
        "insert2", null, (TextProperties) properties.get("infoProps"));
    Text insert3 = lang.newText(new Offset(10, 40, "insert2",
        AnimalScript.DIRECTION_NW), "D[i][j-1] + 1", "insert3", null,
        (TextProperties) properties.get("infoRedProps"));
    // fill HashMap
    textElements.put("insert0", insert0);
    textElements.put("insert1", insert1);
    textElements.put("insert2", insert2);
    textElements.put("insert3", insert3);

    // Operation "compare" text
    Text compare0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Ebenfalls wird ein Vergleich der Buchstaben auf der", "compare0",
        null, (TextProperties) properties.get("infoProps"));
    Text compare1 = lang.newText(new Offset(0, 30, "compare0",
        AnimalScript.DIRECTION_NW),
        "gleichen Position beider Wörter durchgeführt", "compare1", null,
        (TextProperties) properties.get("infoProps"));
    Text compare2 = lang.newText(new Offset(0, 30, "compare1",
        AnimalScript.DIRECTION_NW),
        "Hierbei wird, wie bei der Ersetzen-Operation, der", "compare2", null,
        (TextProperties) properties.get("infoProps"));
    Text compare3 = lang.newText(new Offset(0, 30, "compare2",
        AnimalScript.DIRECTION_NW),
        "Wert aus D[i-1][j-1] genommen, allerdings ohne Addition.", "compare3",
        null, (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("compare0", compare0);
    textElements.put("compare1", compare1);
    textElements.put("compare2", compare2);
    textElements.put("compare3", compare3);

    // matrix-hopping text
    Text hopp0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Die Bewegung innerhalb der Matrix bei den einzelnen", "hopp0", null,
        (TextProperties) properties.get("infoProps"));
    Text hopp1 = lang.newText(new Offset(0, 30, "hopp0",
        AnimalScript.DIRECTION_NW),
        "Operationen erklärt sich aus dem überprüfen", "hopp1", null,
        (TextProperties) properties.get("infoProps"));
    Text hopp2 = lang.newText(new Offset(0, 30, "hopp1",
        AnimalScript.DIRECTION_NW),
        "der Buchstaben vor und hinter der aktuellen Position.", "hopp2", null,
        (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("hopp0", hopp0);
    textElements.put("hopp1", hopp1);
    textElements.put("hopp2", hopp2);

    // chose minimal text
    Text min0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Alle Operationen und der Vergleich liefern einen", "min0", null,
        (TextProperties) properties.get("infoProps"));
    Text min1 = lang.newText(new Offset(0, 30, "min0",
        AnimalScript.DIRECTION_NW),
        "natürlichen Wert. Aus diesen sucht man sich nun den", "min1", null,
        (TextProperties) properties.get("infoProps"));
    Text min2 = lang.newText(new Offset(0, 30, "min1",
        AnimalScript.DIRECTION_NW),
        "kleinsten Wert und setzt ihn auf D[i][j].", "min2", null,
        (TextProperties) properties.get("infoProps"));
    Text min3 = lang.newText(new Offset(0, 30, "min2",
        AnimalScript.DIRECTION_NW),
        "Zusammengefasst sieht es folgerndermassen aus:", "min3", null,
        (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("min0", min0);
    textElements.put("min1", min1);
    textElements.put("min2", min2);
    textElements.put("min3", min3);

    // algorithm "source-code"
    SourceCode algo = lang.newSourceCode(new Offset(20, 50, "min3",
        AnimalScript.DIRECTION_NW), "algo", null,
        (SourceCodeProperties) properties.get("algoProps"));
    algo.addCodeLine("D[i][j] = min(", null, 0, null);
    algo.addCodeLine("   D[i-1][j-1] falls u[i] = v[j]", null, 0, null);
    algo.addCodeLine("   D[i-1][j-1] + 1 (Ersetzen)", null, 0, null);
    algo.addCodeLine("   D[i-1][j] + 1 (Löschen)", null, 0, null);
    algo.addCodeLine("   D[i][j-1] + 1 (Einfügen)", null, 0, null);
    algo.addCodeLine(")", null, 0, null);
    algo.hide();
    // fill HashMap
    codeElements.put("algo", algo);

    Text algo0 = lang.newText(new Offset(-20, 30, "algo",
        AnimalScript.DIRECTION_SW), "mit 1 <= i <= m und 1 <= j <= n", "algo0",
        null, (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("algo0", algo0);

    // conclusion text
    Text conclusion0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Das Ergebnis des Levenshtein-Algorithmus steht nun", "conclusion0",
        null, (TextProperties) properties.get("infoProps"));
    Text conclusion1 = lang.newText(new Offset(0, 30, "conclusion0",
        AnimalScript.DIRECTION_NW),
        "in der letzten Zeile und Spalte, also in D[m][n].", "conclusion1",
        null, (TextProperties) properties.get("infoProps"));
    Text conclusion2 = lang.newText(new Offset(0, 30, "conclusion1",
        AnimalScript.DIRECTION_NW),
        "Die Komplexität des Algorithmus beträgt O(mn).", "conclusion2", null,
        (TextProperties) properties.get("infoProps"));
    Text conclusion3 = lang.newText(new Offset(0, 30, "conclusion2",
        AnimalScript.DIRECTION_NW),
        "Eine Alternative des Algorithmus mit weniger Speicheraufwand",
        "conclusion3", null, (TextProperties) properties.get("infoProps"));
    Text conclusion4 = lang.newText(new Offset(0, 30, "conclusion3",
        AnimalScript.DIRECTION_NW), "ist der Hirschberg-Algorithmus.",
        "conclusion4", null, (TextProperties) properties.get("infoProps"));
    // fill HashMap
    textElements.put("conclusion0", conclusion0);
    textElements.put("conclusion1", conclusion1);
    textElements.put("conclusion2", conclusion2);
    textElements.put("conclusion3", conclusion3);
    textElements.put("conclusion4", conclusion4);

  }

  /**
   * Method for generating the arrays and the text elements using dynamic input
   * 
   * @param u
   *          first word
   * @param v
   *          second word
   */
  private void defineArray(String u, String v) {

    // fill String u in String array
    int m = u.length();
    String[] uSA = new String[m];
    char[] uCA = u.toCharArray();

    for (int i = 0; i < m; i++) {
      uSA[i] = "" + uCA[i];
    }

    // fill String v in String array
    int n = v.length();
    String[] vSA = new String[n];
    char[] vCA = v.toCharArray();

    for (int i = 0; i < n; i++) {
      vSA[i] = "" + vCA[i];
    }

    // word array u & v
    StringArray uArray = lang.newStringArray(new Offset(500, 50, "header",
        AnimalScript.DIRECTION_NW), uSA, "uArray", null,
        (ArrayProperties) properties.get("arrayProps"));
    StringArray vArray = lang.newStringArray(new Offset(0, 10, "uArray",
        AnimalScript.DIRECTION_SW), vSA, "vArray", null,
        (ArrayProperties) properties.get("arrayProps"));
    // fill HashMap
    arrayElements.put("uArray", uArray);
    arrayElements.put("vArray", vArray);

    // m = |u| & n = |v|
    Text uLength = lang.newText(new Offset(20, 0, "uArray",
        AnimalScript.DIRECTION_NE), "m = |u| = " + String.valueOf(m),
        "uLength", null, (TextProperties) properties.get("infoRedProps"));
    Text vLength = lang.newText(new Offset(20, 0, "vArray",
        AnimalScript.DIRECTION_NE), "n = |v| = " + String.valueOf(n),
        "vLength", null, (TextProperties) properties.get("infoRedProps"));
    // fill HashMap
    textElements.put("uLength", uLength);
    textElements.put("vLength", vLength);

    // input words from generator
    Text uWord = lang.newText(new Offset(10, 40, "input0",
        AnimalScript.DIRECTION_NW), "u = " + u, "uWord", null,
        (TextProperties) properties.get("infoRedProps"));
    Text vWord = lang.newText(new Offset(0, 30, "uWord",
        AnimalScript.DIRECTION_NW), "v = " + v, "vWord", null,
        (TextProperties) properties.get("infoRedProps"));
    // fill HashMap
    textElements.put("uWord", uWord);
    textElements.put("vWord", vWord);
  }

  /**
   * Calling the functions for initiation of animation elements
   */
  private void defineElements(String u, String v) {
    defineProps();
    defineText();
    defineArray(u, v);
  }

  /**
   * Description of Operations
   */
  private void description() {
    textElements.get("desc0").hide();
    textElements.get("desc1").hide();
    textElements.get("desc2").hide();
    textElements.get("desc3").hide();

    textElements.get("desc4").show();
    textElements.get("desc5").show();
    textElements.get("desc6").show();
    textElements.get("desc7").show();
    textElements.get("desc8").show();
    textElements.get("desc9").show();
    lang.nextStep();

    textElements.get("desc4").hide();
    textElements.get("desc5").hide();
    textElements.get("desc6").hide();
    textElements.get("desc7").hide();
    textElements.get("desc8").hide();
    textElements.get("desc9").hide();

    textElements.get("replace0").show();
    textElements.get("replace1").show();
    textElements.get("replace2").show();
    textElements.get("replace3").show();
    textElements.get("replace4").show();
    lang.nextStep();

    textElements.get("replace0").hide();
    textElements.get("replace1").hide();
    textElements.get("replace2").hide();
    textElements.get("replace3").hide();
    textElements.get("replace4").hide();

    textElements.get("delete0").show();
    textElements.get("delete1").show();
    textElements.get("delete2").show();
    textElements.get("delete3").show();
    lang.nextStep();

    textElements.get("delete0").hide();
    textElements.get("delete1").hide();
    textElements.get("delete2").hide();
    textElements.get("delete3").hide();

    textElements.get("insert0").show();
    textElements.get("insert1").show();
    textElements.get("insert2").show();
    textElements.get("insert3").show();
    lang.nextStep();

    textElements.get("insert0").hide();
    textElements.get("insert1").hide();
    textElements.get("insert2").hide();
    textElements.get("insert3").hide();

    textElements.get("compare0").show();
    textElements.get("compare1").show();
    textElements.get("compare2").show();
    textElements.get("compare3").show();
    lang.nextStep();

    textElements.get("compare0").hide();
    textElements.get("compare1").hide();
    textElements.get("compare2").hide();
    textElements.get("compare3").hide();

    textElements.get("hopp0").show();
    textElements.get("hopp1").show();
    textElements.get("hopp2").show();
    lang.nextStep();

    textElements.get("hopp0").hide();
    textElements.get("hopp1").hide();
    textElements.get("hopp2").hide();

    textElements.get("min0").show();
    textElements.get("min1").show();
    textElements.get("min2").show();
    textElements.get("min3").show();
    lang.nextStep();

    codeElements.get("algo").show();
    textElements.get("algo0").show();
    lang.nextStep();

  }

  /**
   * Method for the animation elements to be handled in
   * 
   * @param u
   *          first word
   * @param v
   *          second word
   */
  private void run(String u, String v) {
    defineElements(u, v);

    lang.nextStep();

    textElements.get("intro0").show();
    textElements.get("intro1").show();
    textElements.get("intro2").show();
    textElements.get("intro3").show();
    lang.nextStep();

    textElements.get("intro0").hide();
    textElements.get("intro1").hide();
    textElements.get("intro2").hide();
    textElements.get("intro3").hide();

    textElements.get("info0").show();
    lang.nextStep();

    textElements.get("info1").show();
    lang.nextStep();

    textElements.get("info2").show();
    lang.nextStep();

    textElements.get("info3").show();
    lang.nextStep();

    textElements.get("info0").hide();
    textElements.get("info1").hide();
    textElements.get("info2").hide();
    textElements.get("info3").hide();

    textElements.get("input0").show();
    lang.nextStep();

    textElements.get("uWord").show();
    arrayElements.get("uArray").show();
    lang.nextStep();

    textElements.get("vWord").show();
    arrayElements.get("vArray").show();
    lang.nextStep();

    textElements.get("input0").hide();
    textElements.get("uWord").hide();
    textElements.get("vWord").hide();

    textElements.get("info4").show();
    lang.nextStep();

    textElements.get("info5").show();
    textElements.get("info6").show();
    textElements.get("uLength").show();
    lang.nextStep();

    textElements.get("info7").show();
    textElements.get("info8").show();
    textElements.get("vLength").show();
    lang.nextStep();

    arrayElements.get("uArray").show();
    arrayElements.get("vArray").show();
    lang.nextStep();

    textElements.get("info4").hide();
    textElements.get("info5").hide();
    textElements.get("info6").hide();
    textElements.get("info7").hide();
    textElements.get("info8").hide();

    textElements.get("matrix0").show();
    lang.nextStep();

    textElements.get("matrix1").show();
    textElements.get("matrix2").show();
    lang.nextStep();

    textElements.get("matrix3").show();
    textElements.get("matrix4").show();
    lang.nextStep();

    // call algorithm
    runLevenshtein(u, v);

    textElements.get("min0").hide();
    textElements.get("min1").hide();
    textElements.get("min2").hide();
    textElements.get("min3").hide();
    codeElements.get("algo").hide();
    textElements.get("algo0").hide();

    textElements.get("conclusion0").show();
    textElements.get("conclusion1").show();
    textElements.get("conclusion2").show();
    textElements.get("conclusion3").show();
    textElements.get("conclusion4").show();
  }

  /**
   * 
   * @param u
   *          first word
   * @param v
   *          second word
   */
  private void runLevenshtein(String u, String v) {

    // length of the first word
    int m = u.length();
    // length of the second word
    int n = v.length();

    // variables for possible change-operations on chars on the same position
    int same = 0; // chars are equal
    int replace = 0; // chars are not equal
    int paste = 0; // previous char is equal
    int delete = 0; // next char is equal

    // matrix for levenshtein distance
    int[][] matrix = new int[m + 1][n + 1];

    // Words into chars, while the first char has to be empty for the algorithm
    char[] uA = u.toCharArray();
    char[] vA = v.toCharArray();
    char[] uArray = new char[m + 1];
    char[] vArray = new char[n + 1];

    // initiation of the first matrix value
    matrix[0][0] = 0;

    // --- animation block
    lang.newText(new Offset(0, 50, "vArray", AnimalScript.DIRECTION_SW), " ",
        "init", null, (TextProperties) properties.get("matrixProps"));
    Square square = lang.newSquare(new Offset(15, 20, "init",
        AnimalScript.DIRECTION_NW), 20, "", null, (SquareProperties) properties
        .get("squareProps"));
    // ---

    // initiating first row of the matrix and filling of the first char-array
    for (int i = 1; i <= m; i++) {
      matrix[i][0] = i;
      uArray[i] = uA[i - 1];

      // --- animation block: Matrix column
      lang.newText(
          new Offset(20 + i * 20, 0, "init", AnimalScript.DIRECTION_NW), ""
              + uArray[i], "i" + String.valueOf(i), null,
          (TextProperties) properties.get("matrixProps"));
      // ---
    }
    arrayElements.get("uArray").hide();
    textElements.get("uLength").hide();
    lang.nextStep();

    // initiating first column of the matrix and filling of the second
    // char-array
    for (int j = 1; j <= n; j++) {
      matrix[0][j] = j;
      vArray[j] = vA[j - 1];

      // --- animation block: Matrix row
      lang.newText(
          new Offset(0, 20 + j * 20, "init", AnimalScript.DIRECTION_NW), ""
              + vArray[j], "j" + String.valueOf(j), null,
          (TextProperties) properties.get("matrixProps"));
      // ---
    }
    arrayElements.get("vArray").hide();
    textElements.get("vLength").hide();

    // Begin: Animation Block
    lang.nextStep();

    textElements.get("matrix5").show();
    lang.newText(new Offset(20, 20, "init", AnimalScript.DIRECTION_NW), "0",
        "m0", null, (TextProperties) properties.get("matrixProps"));
    square.show();
    lang.nextStep();

    textElements.get("matrix0").hide();
    textElements.get("matrix1").hide();
    textElements.get("matrix2").hide();
    textElements.get("matrix3").hide();
    textElements.get("matrix4").hide();
    textElements.get("matrix5").hide();

    textElements.get("desc0").show();
    lang.nextStep();

    textElements.get("desc1").show();
    lang.nextStep();

    // fill first line
    for (int i = 1; i <= m; i++) {
      square.moveBy(null, i + 17, 0, null, null);
      lang.newText(new Offset(20 + i * 20, 20, "init",
          AnimalScript.DIRECTION_NW), String.valueOf(i),
          "i" + String.valueOf(i), null, (TextProperties) properties
              .get("matrixProps"));
      lang.nextStep();
    }

    square.moveBy(null, -m * 17 - 17, 0, null, null);
    lang.nextStep();

    textElements.get("desc2").show();
    lang.nextStep();

    textElements.get("desc3").show();

    // fill first row
    for (int j = 1; j <= n; j++) {
      square.moveBy(null, 0, j + 17, null, null);
      lang.newText(new Offset(20, 20 + j * 20, "init",
          AnimalScript.DIRECTION_NW), String.valueOf(j),
          "j" + String.valueOf(j), null, (TextProperties) properties
              .get("matrixProps"));
      lang.nextStep();
    }

    square.moveBy(null, 17, -n * 17 - 17, null, null);
    square.hide();
    lang.nextStep();

    // Description of operations
    description();

    square.show();

    // Value for current matrix operation
    Text val = lang.newText(new Offset(0, 50, "j" + String.valueOf(n),
        AnimalScript.DIRECTION_NW), "aktueller Wert", "val", null,
        (TextProperties) properties.get("infoProps"));
    val.show();
    // End: Animation Block

    // filling of the matrix values not initialized
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        // move square
        square.moveBy(null, 0, 20, null, null);

        // if u[i] == v[j] then the current position is filled with
        // matrix[i-1][j-1] + 0
        if (uArray[i] == vArray[j]) {
          same = matrix[i - 1][j - 1];
          val.setText("u[i] = v[j]: " + String.valueOf(same), null, null);
        } else {
          same = Integer.MAX_VALUE;
          val.setText("u[i] = v[j]: -", null, null);
        }
        // highlight compare line
        codeElements.get("algo").highlight(1, 0, false, null, null);

        lang.nextStep();

        // replace has to be automatically set to matrix[i-1][j-1] + 1 to
        // have a value to compare, even if u[i] == v[i]
        replace = matrix[i - 1][j - 1] + 1;
        // highlight replace line & unhighlight compare
        codeElements.get("algo").highlight(2, 0, false, null, null);
        codeElements.get("algo").unhighlight(1);
        // show actual event & value
        val.setText("Ersetzen: " + String.valueOf(replace), null, null);
        lang.nextStep();

        // delete has to be filled with matrix[i-1][j] for comparison
        delete = matrix[i - 1][j] + 1;
        // highlight delete line & unhighlight replace
        codeElements.get("algo").highlight(3, 0, false, null, null);
        codeElements.get("algo").unhighlight(2);
        // show actual event & value
        val.setText("Löschen: " + String.valueOf(delete), null, null);
        lang.nextStep();

        // paste has to be filled with matrix[i][j-1] for comparison
        paste = matrix[i][j - 1] + 1;
        // highlight insert line & unhighlight delete
        codeElements.get("algo").highlight(4, 0, false, null, null);
        codeElements.get("algo").unhighlight(3);
        // show actual event & value
        val.setText("Einfügen: " + String.valueOf(paste), null, null);
        lang.nextStep();

        // matrix[i][j] is set to the minimum of the gathered values
        matrix[i][j] = Math.min(Math.min(same, replace),
            Math.min(paste, delete));
        // show value in matrix
        lang.newText(new Offset(20 + i * 20, 20 + j * 20, "init",
            AnimalScript.DIRECTION_NW), String.valueOf(matrix[i][j]), "ij"
            + String.valueOf(i) + String.valueOf(j), null,
            (TextProperties) properties.get("matrixProps"));
        // unhightlight delete
        codeElements.get("algo").unhighlight(4);
        // show actual event & value
        val.setText("Minimaler Wert: " + String.valueOf(matrix[i][j]), null,
            null);
        lang.nextStep();
      }
      // move square
      square.moveBy(null, 20, -n * 20, null, null);
    }

    // move square
    square.moveBy(null, -20, n * 20, null, null);
    val.setText("Levenshtein-Distanz: " + String.valueOf(matrix[m][n]), null,
        null);
    lang.nextStep();

    // levenshtein distance is the number in the last column and row

  }
}