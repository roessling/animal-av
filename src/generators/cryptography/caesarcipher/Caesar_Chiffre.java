package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author Galin Bobev, Mir-Misagh Zayyeni
 * 
 */
public class Caesar_Chiffre implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private Language                     lg;
  private AnimationPropertiesContainer container;
  private Hashtable<String, Object>    primitives;
  private SourceCodeProperties         sourceProperties;
  private SourceCodeProperties         titleProperties;
  private TextProperties               textProperties;
  private SourceCode                   title;
  // private Rect rect_source_code;
  private SourceCode                   end;

  /*
   * 
   * Global variables
   */
  private int                          factor;                        // Factor
                                                                       // of
                                                                       // Caesar-Chiffre
  private Timing                       ticks_0  = new TicksTiming(0);
  private Timing                       ticks_40 = new TicksTiming(40);
  private SourceCode                   source_code;                   // source
                                                                       // code
                                                                       // primitive

  /**
   * Default constructor
   */
  public Caesar_Chiffre() {
  }

  private static final String[] DESCRIPTION = {
      "Die Caesar-Verschlüsselung (Caesar-Verschiebung) ist ein besonders", // 0
      "simpler Sonderfall einer einfachen (das heißt monographischen)", // 1
      "monoalphabetischen Substitution. Zum Zwecke der Verschlüsselung wird dabei", // 2
      "jeder Buchstabe des lateinischen Standardalphabets um eine bestimmte Anzahl", // 3
      "von Positionen zyklisch verschoben (rotiert). Die Anzahl bestimmt den Schlüssel,", // 4
      "der für die gesamte Verschlüsselung unverändert bleibt.", // 5
      "Es ist eine der einfachsten und unsichersten Formen einer Geheimschrift." // 6
                                            };

  private StringBuffer          SOURCE_CODE = new StringBuffer();

  void initSourceCode() {
    SOURCE_CODE.append("public String code(String src) {\n");
    SOURCE_CODE.append("   char[] src_char = src.toCharArray();\n");
    SOURCE_CODE.append("   String coded = \"\";\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE.append("   for (int i = 0; i < src.length(); i++) {\n");
    SOURCE_CODE.append("       //check bounds\n");
    SOURCE_CODE
        .append("       if(src_char[i] >= (char) ('z' - factor) && src_char[i] <= 'z')\n");
    SOURCE_CODE
        .append("           src_char[i] = (char) ('a' - ((src_char[i] - 'z') % factor) - 1);\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE
        .append("       if(src_char[i] >= (char) ('Z' - factor) && src_char[i] <= 'Z')\n");
    SOURCE_CODE
        .append("           src_char[i] = (char) ('A' - ((src_char[i] - 'Z') % factor) - 1);\n");
    SOURCE_CODE.append("\n");
    SOURCE_CODE.append("       //code\n");
    SOURCE_CODE.append("       coded += (char) (src_char[i] + factor);\n");
    SOURCE_CODE.append("   }\n");
    SOURCE_CODE.append("   return code;\n");
    SOURCE_CODE.append("}\n");
  }

  private static final String[] SOURCE_CODE2 = {
      "public String code(String src){", // 0
      "char[] src_char = src.toCharArray();", // 1
      "String coded;", // 2
      "", // (3)
      "for (int i = 0; i < src.length(); i++){", // 4
      "//check bounds", // (5)
      "if(src_char[i] >= (char) ('z' - factor) && src_char[i] <= 'z')", // 6
      "src_char[i] = (char) ('a' - ((src_char[i] - 'z') % factor) - 1);", // 7
      "", // (8)
      "if(src_char[i] >= (char) ('Z' - factor) && src_char[i] <= 'Z')", // 9
      "src_char[i] = (char) ('A' - ((src_char[i] - 'Z') % factor) - 1);", // 10
      "", // (11)
      "//code", // (12)
      "coded += (char) (src_char[i] + factor);", // 13
      "}", // 14
      "return code;", // 15
      "}" // 16
                                             };

  @Override
  public void init() {
    lg = new AnimalScript("Caesar-Chiffre Animation",
        "Galin Bobev , Mir-Misagh Zayyeni", 640, 380);
    lg.setStepMode(true);

    setProperties();
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    this.container = new AnimationPropertiesContainer();
    this.primitives = new Hashtable<String, Object>();

    this.container = arg0;
    this.primitives = arg1;

    // properties handle
    init();

    // data
    String data = (String) primitives.get("data");
    factor = (Integer) primitives.get("factor");

    // secondary structure of the animation
    title();
    source();

    // algorithm
    caesarChiffre(data);

    //
    String CODE = lg.toString();
    System.out.println(CODE);

    return CODE;
  }

  private void caesarChiffre(String data) {
    char[] dataCharArray = data.toCharArray();
    String[] dataStrArray = new String[dataCharArray.length];
    String coded = "";

    // show
    title.show();
    source_code.show();

    // fill String Array of row data
    for (int i = 0; i < dataCharArray.length; i++) {
      dataStrArray[i] = data.substring(i, i + 1);
    }

    // visual properties of the String[]
    ArrayProperties arrayProperties = new ArrayProperties();
    arrayProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.BLACK);
    arrayProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.RED);
    arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.YELLOW);

    lg.nextStep();
    /*
     * Start algorithm
     * 
     * public String code(String src){ // Line 0
     */
    source_code.highlight(0);
    Text dataRow = lg.newText(new Coordinates(40, 170), data, "dataRow", null,
        textProperties);
    lg.nextStep();

    /*
     * String to char[]
     * 
     * char[] src_char = src.toCharArray(); Line 1
     */
    source_code.unhighlight(0);
    source_code.highlight(1);

    // StringArray of row data + StringArray of coded data
    StringArray aStrArray = lg.newStringArray(new Coordinates(40, 250),
        dataStrArray, "stringArray", null, arrayProperties);

    lg.nextStep();

    /*
     * String coded - init
     * 
     * String coded = ""; Line 2
     */
    source_code.unhighlight(1);
    source_code.highlight(2);

    Text dataCoded = lg.newText(new Coordinates(40, 200), "", "dataCoded",
        null, textProperties);
    lg.nextStep();

    /*
     * FOR - Loop
     * 
     * for (int i = 0; i < src.length(); i++){ Line 4
     */
    source_code.unhighlight(2);

    for (int i = 0; i < dataCharArray.length; i++) {
      source_code.highlight(4);
      lg.nextStep();

      /*
       * In the LOOP
       */
      source_code.unhighlight(4);
      source_code.highlight(6);
      aStrArray.highlightElem(i, ticks_0, ticks_40);
      lg.nextStep();

      /*
       * First IF
       * 
       * if(src_char[i] >= (char) ('z' - factor) && src_char[i] <= 'z') Line 6
       */
      if (dataCharArray[i] >= (char) ('z' - factor) && dataCharArray[i] <= 'z') {
        /*
         * if == true
         * 
         * src_char[i] = (char) ('a' - ((src_char[i] - 'z') % factor) - 1); Line
         * 7
         */
        source_code.unhighlight(6);
        source_code.highlight(7);
        dataCharArray[i] = (char) ('a' - ((dataCharArray[i] - 'z') % factor) - 1);

        // change array element
        aStrArray.put(i, ("" + (char) dataCharArray[i]), ticks_40, ticks_40);

        lg.nextStep();
        source_code.unhighlight(7);
      }

      source_code.unhighlight(6);
      source_code.highlight(9);
      lg.nextStep();

      /*
       * Second IF
       * 
       * if(src_char[i] >= (char) ('Z' - factor) && src_char[i] <= 'Z') Line 9
       */
      if (dataCharArray[i] >= (char) ('Z' - factor) && dataCharArray[i] <= 'Z') {
        /*
         * if == true
         * 
         * src_char[i] = (char) ('A' - ((src_char[i] - 'Z') % factor) - 1); Line
         * 10
         */
        source_code.unhighlight(9);
        source_code.highlight(10);
        dataCharArray[i] = (char) ('A' - ((dataCharArray[i] - 'A') % factor) - 1);

        // change array element
        aStrArray.put(i, "" + ((char) dataCharArray[i]), ticks_40, ticks_40);

        lg.nextStep();
        source_code.unhighlight(10);
      }

      source_code.unhighlight(9);

      /*
       * Set coded i-th char
       * 
       * coded += (char) (src_char[i] + factor); Line 13
       */
      coded += (char) (dataCharArray[i] + factor);
      source_code.highlight(13);
      dataCoded.setText(coded, ticks_40, ticks_40);
      lg.nextStep();

      /*
       * AT THE END OF THE FOR-LOOP
       */
      source_code.unhighlight(13);
      aStrArray.unhighlightElem(i, ticks_0, ticks_40);
    }

    /*
     * Out of the FOR-Loop
     * 
     * return code; Line 15
     */
    source_code.unhighlight(4);
    source_code.highlight(15);
    lg.nextStep();

    /*
     * end of the methode
     * 
     * } Line 16
     */
    source_code.unhighlight(15);
    source_code.highlight(16);
    lg.nextStep();

    /*
     * Finished
     */
    source_code.unhighlight(16);
    lg.nextStep();

    source_code.hide();
    dataRow.hide();
    aStrArray.hide();
    dataCoded.hide();
    end();
  }

  /*----------------------------------------------------------------------------
   * |
   * |	
   * |
   * */

  private void title() {
    title = lg.newSourceCode(new Coordinates(35, 15), "title", null,
        titleProperties);
    title.addCodeLine("Caesar-Chiffre", "caesar", 0, null);
    title.show();

    RectProperties title_rect_prop = new RectProperties();
    title_rect_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    title_rect_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    title_rect_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    lg.newRect(new Offset(-20, -20, "title", "NW"), new Offset(20, 20, "title",
        "SE"), "rect2", null, title_rect_prop);
  }

  private void source() {
    source_code = lg.newSourceCode(new Coordinates(450, 150), "sourceCode",
        null, sourceProperties);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    source_code.addCodeLine(SOURCE_CODE2[0], null, 0, null); // 0
    source_code.addCodeLine(SOURCE_CODE2[1], null, 1, null); // 1
    source_code.addCodeLine(SOURCE_CODE2[2], null, 1, null); // 2
    source_code.addCodeLine(SOURCE_CODE2[3], null, 1, null); // 3
    source_code.addCodeLine(SOURCE_CODE2[4], null, 1, null); // 4
    source_code.addCodeLine(SOURCE_CODE2[5], null, 2, null); // 5
    source_code.addCodeLine(SOURCE_CODE2[6], null, 2, null); // 6
    source_code.addCodeLine(SOURCE_CODE2[7], null, 3, null); // 7
    source_code.addCodeLine(SOURCE_CODE2[8], null, 2, null); // 8
    source_code.addCodeLine(SOURCE_CODE2[9], null, 2, null); // 9
    source_code.addCodeLine(SOURCE_CODE2[10], null, 3, null); // 10
    source_code.addCodeLine(SOURCE_CODE2[11], null, 2, null); // 11
    source_code.addCodeLine(SOURCE_CODE2[12], null, 2, null); // 12
    source_code.addCodeLine(SOURCE_CODE2[13], null, 2, null); // 13
    source_code.addCodeLine(SOURCE_CODE2[14], null, 1, null); // 14
    source_code.addCodeLine(SOURCE_CODE2[15], null, 1, null); // 15
    source_code.addCodeLine(SOURCE_CODE2[16], null, 0, null); // 16

    RectProperties source_rect_prop = new RectProperties();
    source_rect_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    source_rect_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    source_rect_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    lg.newRect(new Offset(-20, -20, "sourceCode", "NW"), new Offset(20, 20,
        "sourceCode", "SE"), "rect2", null, source_rect_prop);
  }

  private void end() {
    end = lg.newSourceCode(new Coordinates(40, 130), "end_info", null);

    end.addCodeLine("Thank you for using Animal !", "l1", 1, ticks_0);
    end.addCodeLine("", "l2", 1, ticks_0);
    end.addCodeLine("Authors: Galin Bobev , Mir-Misagh Zayyeni ", "l3", 1,
        ticks_0);
  }

  private void setSource() {
    sourceProperties = (SourceCodeProperties) container
        .getPropertiesByName("source");
  }

  private void setDefaultTitle() {
    // title Properties
    titleProperties = new SourceCodeProperties();
    titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 40));
    titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  private void setDefaultSource() {
    // Source Code Properties
    sourceProperties = new SourceCodeProperties();
    sourceProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    sourceProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    sourceProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.red);
  }

  private void setDefaultText() {
    // Text Properties
    textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
  }

  private void setProperties() {
    setDefaultTitle();
    setDefaultText();

    if (container.getPropertiesByName("source") != null)
      setSource();
    else
      setDefaultSource();
  }

  @Override
  public String getAlgorithmName() {
    return "Caesar Cipher";
  }

  @Override
  public String getAnimationAuthor() {
    return "Galin Bobev, Mir-Misagh Zayyeni";
  }

  @Override
  public String getCodeExample() {
    initSourceCode();
    return SOURCE_CODE.toString();
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    String description = "";

    for (int i = 0; i < DESCRIPTION.length; i++) {
      description += DESCRIPTION[i];
    }

    return description;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  @Override
  public String getName() {
    return "Caesar-Chiffre Animation";
  }

  @Override
  public String getOutputLanguage() {
    return generators.framework.Generator.JAVA_OUTPUT;
  }

}
