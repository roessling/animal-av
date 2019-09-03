package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Group;
import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class KVDiagramm implements Generator {
  private Language lang;
  private Color    Term_1;
  private Color    Term_2;
  private Color    Term_3;
  private Color    Term_4;
  private Color    Term_5;
  private Color    Term_6;
  private Color    Term_7;
  private Color    Term_8;
  private String   Funktion_0000;
  private String   Funktion_0001;
  private String   Funktion_0010;
  private String   Funktion_0011;
  private String   Funktion_0100;
  private String   Funktion_0101;
  private String   Funktion_0110;
  private String   Funktion_0111;
  private String   Funktion_1000;
  private String   Funktion_1001;
  private String   Funktion_1010;
  private String   Funktion_1011;
  private String   Funktion_1100;
  private String   Funktion_1101;
  private String   Funktion_1110;
  private String   Funktion_1111;

  public void init() {
    lang = new AnimalScript("Funktionsminimierung mittels KV-Diagramm",
        "Michael Rodenberg (Michael.Rodenberg@d120.de)", 1024, 740);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Term_1 = (Color) primitives.get("Term_1");
    Term_2 = (Color) primitives.get("Term_2");
    Term_3 = (Color) primitives.get("Term_3");
    Term_4 = (Color) primitives.get("Term_4");
    Term_5 = (Color) primitives.get("Term_5");
    Term_6 = (Color) primitives.get("Term_6");
    Term_7 = (Color) primitives.get("Term_7");
    Term_8 = (Color) primitives.get("Term_8");
    Funktion_0000 = (String) primitives.get("Funktion_0000");
    Funktion_0001 = (String) primitives.get("Funktion_0001");
    Funktion_0010 = (String) primitives.get("Funktion_0010");
    Funktion_0011 = (String) primitives.get("Funktion_0011");
    Funktion_0100 = (String) primitives.get("Funktion_0100");
    Funktion_0101 = (String) primitives.get("Funktion_0101");
    Funktion_0110 = (String) primitives.get("Funktion_0110");
    Funktion_0111 = (String) primitives.get("Funktion_0111");
    Funktion_1000 = (String) primitives.get("Funktion_1000");
    Funktion_1001 = (String) primitives.get("Funktion_1001");
    Funktion_1010 = (String) primitives.get("Funktion_1010");
    Funktion_1011 = (String) primitives.get("Funktion_1011");
    Funktion_1100 = (String) primitives.get("Funktion_1100");
    Funktion_1101 = (String) primitives.get("Funktion_1101");
    Funktion_1110 = (String) primitives.get("Funktion_1110");
    Funktion_1111 = (String) primitives.get("Funktion_1111");

    Color colors[] = { Term_1, Term_2, Term_3, Term_4, Term_5, Term_6, Term_7,
        Term_8 };
    String function[] = { Funktion_0000, Funktion_0001, Funktion_0010,
        Funktion_0011, Funktion_0100, Funktion_0101, Funktion_0110,
        Funktion_0111, Funktion_1000, Funktion_1001, Funktion_1010,
        Funktion_1011, Funktion_1100, Funktion_1101, Funktion_1110,
        Funktion_1111 };
    try {
      minimiere(function, colors);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    return lang.toString();
  }

  public String getName() {
    return "KV-Diagramm [DE]";
  }

  public String getAlgorithmName() {
    return "Funktionsminimierung mittels KV-Diagramm";
  }

  public String getAnimationAuthor() {
    return "Michael Rodenberg";
  }

  public String getDescription() {
    return "Das KV-Diagramm mit 16 Feldern ist ein graphisches Minimierungsverfahren f&uuml;r boolsche Ausdr&uuml;cke mit maximal vier Variablen. Dabei werden in die 16 Felder entsprechend des boolschen Ausdrucks jeweils die Werte logisch eins &prime;1&prime;, logisch null &prime;0&prime; oder don't care &prime;X&prime; eingetragen. Um eine minimale Abdeckung zu erreichen, m&uuml;ssen alle Felder in denen eine &prime;1&prime; steht abgedeckt werden. Die Abdeckungen sind so zu w&auml;hlen, dass sie m&ouml;glichst viele Felder abdecken. Dabei darf aber keine &prime;0&prime; mit abgedeckt werden. Felder in denen ein don`t care steht k&ouml;nnen mit abgedeckt werden, m&uuml;ssen aber nicht. Die Abdeckungen sind au&szlig;erdem so zu w&auml;hlen, dass sie ein Rechteck &uuml;ber die Felder bilden dessen Seitenl&auml;ngen 2^x Felder betragen. Die Abdeckungen k&ouml;nnen &uuml;ber die R&auml;nder hinaus auf der gegen&uuml;berliegenden Seite fortgesetzt werden. So sind z.B. die vier Eckfelder direkt benachbart."
        + "\n";
  }

  public String getCodeExample() {
    return "1. F&uuml;lle das KV-Diagramm entsprechend der Originalfunktion aus."
        + "\n"
        + "2. Pr&uuml;fe f&uuml;r jedes Feld im Hilfsdiagramm, was im gleichen Feld im KV-Diagramm steht..."
        + "\n"
        + "   - &prime;1&prime;: Z&auml;hle in wievielen der vier direkt benachbarten Felder eine &prime;1&prime;"
        + "\n"
        + "           oder ein &prime;X&prime; steht und trage die Anzahl in das Feld im Hilfsdiagramm ein."
        + "\n"
        + "   - &prime;0 oder &prime;X&prime;&prime;: Trage im Hilfsdiagramm ein &prime;-&prime; ein."
        + "\n"
        + "3. Suche im Hilfsdiagramm das Feld mit der kleinsten Zahl."
        + "\n"
        + "    Bei mehreren Feldern mit der gleichen Zahl nimm das erste gefundene."
        + "\n"
        + "4. Suche f&uuml;r das gefundene Feld im KV-Diagramm nach der gr&ouml;&szlig;tm&ouml;glichen Abdeckung ohne &prime;0&prime;."
        + "\n"
        + "    Bei mehreren m&ouml;glichen Abdeckungen mit der gleichen Gr&ouml;&szlig;e nimm die erste gefundene."
        + "\n"
        + "5. Makiere die gefundene Abdeckung dauerhaft im KV-Diagramm"
        + "\n"
        + "    und trage den entsprechenden Term in die Ergebnisfunktion ein."
        + "\n"
        + "6. Makiere im Hilfsdiagramm alle eben abgedeckten Felder mit einem &prime;-&prime;."
        + "\n"
        + "7. Gibt es im Hilfsdiagramm noch Felder in denen eine Zahl steht, mache bei Schritt 3 weiter."
        + "\n"
        + "    Sind alle Felder mit einem &prime;-&prime; makiert, ist der Algorithmus fertig."
        + "\n";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  // Eigener Code
  private String     hd[][]        = { { "", "", "", "" }, { "", "", "", "" },
      { "", "", "", "" }, { "", "", "", "" } };        // Hilfsdiagramm
  private String     kvd[][]       = { { "", "", "", "" }, { "", "", "", "" },
      { "", "", "", "" }, { "", "", "", "" } };        // KVDiagramm
  private int        frames[][]    = new int[4][4];    // Anzahl der
                                                        // Abdeckungen der
                                                        // Felder
  private boolean    frameType1    = true;             // aktuelle Abdeckung
                                                        // kann vom Typ 1 sein
                                                        // (siehe
                                                        // setFrame(Color[]
                                                        // colors))
  private boolean    frameType2    = true;             // aktuelle Abdeckung
                                                        // kann vom Typ 2 sein
                                                        // (siehe
                                                        // setFrame(Color[]
                                                        // colors))
  private boolean    frameType3    = true;             // aktuelle Abdeckung
                                                        // kann vom Typ 3 sein
                                                        // (siehe
                                                        // setFrame(Color[]
                                                        // colors))
  private int        border[][]    = { { 0, 28 }, { 28, 58 }, { 58, 86 },
      { 86, 112 }                 };                  // Grenzen der Felder im
                                                        // Diagramm
  private boolean    cover[][]     = new boolean[4][4]; // Aktuelle Abdeckung
  private String     fmin[]        = new String[8];    // Minimierte Funktion
  private int        i;                                // Aktuelle Zeilenangabe
                                                        // im Hilfsdiagramm
  private int        j;                                // Aktuelle
                                                        // Spaltenangabe im
                                                        // Hilfsdiagramm
  private boolean    foundSmallest = false;
  private int        termes;                           // Nächstes freies Feld
                                                        // in fmin[]
  private int        zeit          = 20;               // Standardzeitslot bis
                                                        // zum nächsten
                                                        // Animationsschritt
  private int        fillHD        = 0;                // Zeitfaktor zum Füllen
                                                        // des Hilfsdiagramms
  private int        coverTime     = 1;                // Zeitfaktor beim
                                                        // suchen der aktuellen
                                                        // Abdeckung
  private SourceCode sourceCode;
  private Group      originalfunktion_Group;
  private Group      kvd_Group;
  private Group      hd_Group;
  private Group      fmin_Group;
  private Group      titel_Group;

  /**
   * minimiert die gegebene Funktion mittels KV-Diagramm
   * 
   * @param function
   *          Originalfunktion
   * @param colors
   *          Farben der einzelnen Terme der minimierten Funktion
   * @throws IllegalDirectionException
   */
  public void minimiere(String[] function, Color[] colors)
      throws IllegalDirectionException {

    // first, set the visual properties (somewhat similar to CSS)
    MatrixProperties diagrammProps = new MatrixProperties();
    diagrammProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    diagrammProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    diagrammProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    diagrammProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.BLACK);
    diagrammProps
        .set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    diagrammProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.YELLOW);

    // first, set the visual properties (somewhat similar to CSS)
    MatrixProperties originalfunktionProps = new MatrixProperties();
    originalfunktionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);
    originalfunktionProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.WHITE);
    originalfunktionProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        Boolean.TRUE);
    originalfunktionProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.BLACK);
    originalfunktionProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        Color.RED);
    originalfunktionProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.YELLOW);

    // Einleitung
    zeigeTitel();
    einleitung();

    // Initialisierung des Algorithmus
    zeigeTitel();
    sourceCode = showSourceCode();
    initialisierung();

    // Diagramme
    TextProperties diagrammTextProps = new TextProperties();
    diagrammTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    diagrammTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    diagrammTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    TextProperties diagrammNameProps = new TextProperties();
    diagrammNameProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));
    diagrammNameProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    diagrammNameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    PointProperties pointProps = new PointProperties();
    pointProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    Point kvd_benchmark = lang.newPoint(new Coordinates(100, 330),
        "kvd_benchmark", null, pointProps);
    Point hd_benchmark = lang.newPoint(new Coordinates(400, 330),
        "hd_benchmark", null, pointProps);
    Point originalfunktion_benchmark = lang.newPoint(new Coordinates(820, 10),
        "originalfunktion_benchmark", null, pointProps);

    String originalfunktionMatrix[][] = { { "#", "A", "B", "C", "D", "F" },
        { " 0", "0", "0", "0", "0", function[0] },
        { " 1", "0", "0", "0", "1", function[1] },
        { " 2", "0", "0", "1", "0", function[2] },
        { " 3", "0", "0", "1", "1", function[3] },
        { " 4", "0", "1", "0", "0", function[4] },
        { " 5", "0", "1", "0", "1", function[5] },
        { " 6", "0", "1", "1", "0", function[6] },
        { " 7", "0", "1", "1", "1", function[7] },
        { " 8", "1", "0", "0", "0", function[8] },
        { " 9", "1", "0", "0", "1", function[9] },
        { "10", "1", "0", "1", "0", function[10] },
        { "11", "1", "0", "1", "1", function[11] },
        { "12", "1", "1", "0", "0", function[12] },
        { "13", "1", "1", "0", "1", function[13] },
        { "14", "1", "1", "1", "0", function[14] },
        { "15", "1", "1", "1", "1", function[15] } };

    // KVD
    Node[] kvd_1_h_node = {
        new Offset(-23, 0, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(112, 0, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_2_h_node = {
        new Offset(0, 28, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(135, 28, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_3_h_node = {
        new Offset(-23, 58, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(112, 58, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_4_h_node = {
        new Offset(0, 86, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(135, 86, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_5_h_node = {
        new Offset(0, 112, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(112, 112, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_1_v_node = {
        new Offset(0, -23, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(0, 112, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_2_v_node = {
        new Offset(28, 0, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(28, 135, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_3_v_node = {
        new Offset(58, -23, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(58, 112, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_4_v_node = {
        new Offset(86, 0, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(86, 135, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] kvd_5_v_node = {
        new Offset(112, 0, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(112, 112, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Polyline kvd_1_h = lang.newPolyline(kvd_1_h_node, "kvd_1_h", null);
    Polyline kvd_2_h = lang.newPolyline(kvd_2_h_node, "kvd_2_h", null);
    Polyline kvd_3_h = lang.newPolyline(kvd_3_h_node, "kvd_3_h", null);
    Polyline kvd_4_h = lang.newPolyline(kvd_4_h_node, "kvd_4_h", null);
    Polyline kvd_5_h = lang.newPolyline(kvd_5_h_node, "kvd_5_h", null);
    Polyline kvd_1_v = lang.newPolyline(kvd_1_v_node, "kvd_1_v", null);
    Polyline kvd_2_v = lang.newPolyline(kvd_2_v_node, "kvd_2_v", null);
    Polyline kvd_3_v = lang.newPolyline(kvd_3_v_node, "kvd_3_v", null);
    Polyline kvd_4_v = lang.newPolyline(kvd_4_v_node, "kvd_4_v", null);
    Polyline kvd_5_v = lang.newPolyline(kvd_5_v_node, "kvd_5_v", null);
    Text kvd_A = lang.newText(new Offset(20, -20, "kvd_benchmark",
        AnimalScript.DIRECTION_N), "A", "kvd_A", null, diagrammTextProps);
    Text kvd_B = lang.newText(new Offset(115, 42, "kvd_benchmark",
        AnimalScript.DIRECTION_N), "B", "kvd_B", null, diagrammTextProps);
    Text kvd_C = lang.newText(new Offset(48, 110, "kvd_benchmark",
        AnimalScript.DIRECTION_N), "C", "kvd_C", null, diagrammTextProps);
    Text kvd_D = lang.newText(new Offset(-20, 12, "kvd_benchmark",
        AnimalScript.DIRECTION_N), "D", "kvd_D", null, diagrammTextProps);
    Text kvd_name = lang.newText(new Offset(70, -30, "kvd_benchmark",
        AnimalScript.DIRECTION_N), "KV-Diagramm", "kvd_name", null,
        diagrammNameProps);

    StringMatrix kvd = lang.newStringMatrix(new Offset(0, 0, "kvd_benchmark",
        AnimalScript.DIRECTION_NW), this.kvd, "kvd", null, diagrammProps);

    LinkedList<Primitive> kvd_list = new LinkedList<Primitive>();
    kvd_list.add(kvd_benchmark);
    kvd_list.add(kvd_name);
    kvd_list.add(kvd_1_h);
    kvd_list.add(kvd_2_h);
    kvd_list.add(kvd_3_h);
    kvd_list.add(kvd_4_h);
    kvd_list.add(kvd_5_h);
    kvd_list.add(kvd_1_v);
    kvd_list.add(kvd_2_v);
    kvd_list.add(kvd_3_v);
    kvd_list.add(kvd_4_v);
    kvd_list.add(kvd_5_v);
    kvd_list.add(kvd_A);
    kvd_list.add(kvd_B);
    kvd_list.add(kvd_C);
    kvd_list.add(kvd_D);
    kvd_list.add(kvd);

    // HD
    Node[] hd_1_h_node = {
        new Offset(-23, 0, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(112, 0, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_2_h_node = {
        new Offset(0, 28, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(135, 28, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_3_h_node = {
        new Offset(-23, 58, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(112, 58, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_4_h_node = {
        new Offset(0, 86, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(135, 86, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_5_h_node = {
        new Offset(0, 112, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(112, 112, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_1_v_node = {
        new Offset(0, -23, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(0, 112, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_2_v_node = {
        new Offset(28, 0, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(28, 135, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_3_v_node = {
        new Offset(58, -23, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(58, 112, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_4_v_node = {
        new Offset(86, 0, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(86, 135, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Node[] hd_5_v_node = {
        new Offset(112, 0, "hd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(112, 112, "hd_benchmark", AnimalScript.DIRECTION_N) };
    Polyline hd_1_h = lang.newPolyline(hd_1_h_node, "hd_1_h", null);
    Polyline hd_2_h = lang.newPolyline(hd_2_h_node, "hd_2_h", null);
    Polyline hd_3_h = lang.newPolyline(hd_3_h_node, "hd_3_h", null);
    Polyline hd_4_h = lang.newPolyline(hd_4_h_node, "hd_4_h", null);
    Polyline hd_5_h = lang.newPolyline(hd_5_h_node, "hd_5_h", null);
    Polyline hd_1_v = lang.newPolyline(hd_1_v_node, "hd_1_v", null);
    Polyline hd_2_v = lang.newPolyline(hd_2_v_node, "hd_2_v", null);
    Polyline hd_3_v = lang.newPolyline(hd_3_v_node, "hd_3_v", null);
    Polyline hd_4_v = lang.newPolyline(hd_4_v_node, "hd_4_v", null);
    Polyline hd_5_v = lang.newPolyline(hd_5_v_node, "hd_5_v", null);
    Text hd_A = lang.newText(new Offset(20, -20, "hd_benchmark",
        AnimalScript.DIRECTION_N), "A", "hd_A", null, diagrammTextProps);
    Text hd_B = lang.newText(new Offset(115, 42, "hd_benchmark",
        AnimalScript.DIRECTION_N), "B", "hd_B", null, diagrammTextProps);
    Text hd_C = lang.newText(new Offset(48, 110, "hd_benchmark",
        AnimalScript.DIRECTION_N), "C", "hd_C", null, diagrammTextProps);
    Text hd_D = lang.newText(new Offset(-20, 12, "hd_benchmark",
        AnimalScript.DIRECTION_N), "D", "hd_D", null, diagrammTextProps);
    Text hd_name = lang.newText(new Offset(70, -30, "hd_benchmark",
        AnimalScript.DIRECTION_N), "Hilfsdiagramm", "hd_name", null,
        diagrammNameProps);

    StringMatrix hd = lang.newStringMatrix(new Offset(0, 0, "hd_benchmark",
        AnimalScript.DIRECTION_NW), this.hd, "hd", null, diagrammProps);

    LinkedList<Primitive> hd_list = new LinkedList<Primitive>();
    hd_list.add(hd_benchmark);
    hd_list.add(hd_name);
    hd_list.add(hd_1_h);
    hd_list.add(hd_2_h);
    hd_list.add(hd_3_h);
    hd_list.add(hd_4_h);
    hd_list.add(hd_5_h);
    hd_list.add(hd_1_v);
    hd_list.add(hd_2_v);
    hd_list.add(hd_3_v);
    hd_list.add(hd_4_v);
    hd_list.add(hd_5_v);
    hd_list.add(hd_A);
    hd_list.add(hd_B);
    hd_list.add(hd_C);
    hd_list.add(hd_D);
    hd_list.add(hd);

    // Originalfunktion
    Node[] originalfunktion_1_h_node = {
        new Offset(0, 25, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(160, 25, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N) };
    Node[] originalfunktion_1_v_node = {
        new Offset(25, 0, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(25, 485, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N) };
    Node[] originalfunktion_2_v_node = {
        new Offset(27, 0, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(27, 485, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N) };
    Node[] originalfunktion_3_v_node = {
        new Offset(136, 0, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(136, 485, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N) };

    Polyline originalfunktion_1_h = lang.newPolyline(originalfunktion_1_h_node,
        "originalfunktion_1_h", null);
    Polyline originalfunktion_1_v = lang.newPolyline(originalfunktion_1_v_node,
        "originalfunktion_1_v", null);
    Polyline originalfunktion_2_v = lang.newPolyline(originalfunktion_2_v_node,
        "originalfunktion_2_v", null);
    Polyline originalfunktion_3_v = lang.newPolyline(originalfunktion_3_v_node,
        "originalfunktion_3_v", null);

    StringMatrix originalfunktion = lang.newStringMatrix(new Offset(0, 0,
        "originalfunktion_benchmark", AnimalScript.DIRECTION_NW),
        originalfunktionMatrix, "originalfunktion", null, diagrammProps);

    LinkedList<Primitive> originalfunktion_list = new LinkedList<Primitive>();
    originalfunktion_list.add(originalfunktion_benchmark);
    originalfunktion_list.add(originalfunktion_1_h);
    originalfunktion_list.add(originalfunktion_1_v);
    originalfunktion_list.add(originalfunktion_2_v);
    originalfunktion_list.add(originalfunktion_3_v);
    originalfunktion_list.add(originalfunktion);

    // F(min)
    TextProperties fminProps = new TextProperties();
    fminProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    fminProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    Text fmin = lang.newText(new Coordinates(20, 480), "F(min) = ", "fmin",
        null, fminProps);
    LinkedList<Primitive> fmin_list = new LinkedList<Primitive>();
    fmin_list.add(fmin);

    lang.nextStep();
    fillKVD(originalfunktion, kvd);
    lang.nextStep();
    fillHD(kvd, hd);
    do {
      lang.nextStep();
      searchSmallest(hd);
      lang.nextStep();
      if (foundSmallest) {
        searchCover(kvd);
        lang.nextStep();
      }
      incFrames();
      searchTerm(hd, colors, fmin_list);
      if (!this.fmin[0].equals("0"))
        setFrame(colors, kvd_list);
      lang.nextStep();
      updateHD(hd);
      coverTime = 0;
      clearCover(kvd);
      lang.nextStep();
    } while (!checkReady(kvd, hd));
    lang.nextStep();
    abschluss(fmin_list, kvd_list, hd_list, originalfunktion_list);
  }

  /**
   * Zeigt die Abschlussfolie der Animation an.
   * 
   * @param fmin_list
   *          Liste mit allen Primitiven von F(min)
   * @param kvd_list
   *          Liste mit allen Primitiven vom KV-Diagramm
   * @param hd_list
   *          Liste mit allen Primitiven vom Hilfsdiagramm
   * @param originalfunktion_list
   *          Liste mit allen Primitiven der Originalfunktion
   * @throws IllegalDirectionException
   */
  private void abschluss(LinkedList<Primitive> fmin_list,
      LinkedList<Primitive> kvd_list, LinkedList<Primitive> hd_list,
      LinkedList<Primitive> originalfunktion_list)
      throws IllegalDirectionException {
    // Gruppen bilden
    fmin_Group = lang.newGroup(fmin_list, "fmin_group");
    kvd_Group = lang.newGroup(kvd_list, "kvd_group");
    hd_Group = lang.newGroup(hd_list, "hd_group");
    originalfunktion_Group = lang.newGroup(originalfunktion_list,
        "originalfunktion_group");

    // Moveline Properties (hidden)
    PolylineProperties movelineProps = new PolylineProperties();
    movelineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    // Pfeil Properties (hidden)
    PolylineProperties pfeilProps = new PolylineProperties();
    pfeilProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
    pfeilProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

    // Movelines erstellen
    Node[] kvd_moveline_node = {
        new Offset(0, 0, "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(300, -250, "kvd_benchmark", AnimalScript.DIRECTION_N) };
    Polyline kvd_moveline = lang.newPolyline(kvd_moveline_node, "kvd_moveline",
        null, movelineProps);

    Node[] originalfunktion_moveline_node = {
        new Offset(0, 0, "originalfunktion_benchmark", AnimalScript.DIRECTION_N),
        new Offset(-800, 0, "originalfunktion_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline originalfunktion_moveline = lang.newPolyline(
        originalfunktion_moveline_node, "originalfunktion_moveline", null,
        movelineProps);

    Node[] fmin_moveline_node = {
        new Offset(0, 0, "fmin", AnimalScript.DIRECTION_N),
        new Offset(250, -250, "fmin", AnimalScript.DIRECTION_N) };
    Polyline fmin_moveline = lang.newPolyline(fmin_moveline_node,
        "fmin_moveline", null, movelineProps);

    Node[] titel_moveline_node = {
        new Offset(0, 0, "hRect", AnimalScript.DIRECTION_N),
        new Offset(250, 0, "hRect", AnimalScript.DIRECTION_N) };
    Polyline titel_moveline = lang.newPolyline(titel_moveline_node,
        "titel_moveline", null, movelineProps);

    sourceCode.hide();
    hd_Group.hide();

    kvd_Group.moveVia(AnimalScript.DIRECTION_N, null, kvd_moveline, null,
        new TicksTiming(2 * zeit));
    originalfunktion_Group.moveVia(AnimalScript.DIRECTION_N, null,
        originalfunktion_moveline, null, new TicksTiming(2 * zeit));
    titel_Group.moveVia(AnimalScript.DIRECTION_N, null, titel_moveline, null,
        new TicksTiming(2 * zeit));
    fmin_Group.moveVia(AnimalScript.DIRECTION_N, null, fmin_moveline, null,
        new TicksTiming(2 * zeit));

    Node[] pfeil_node = {
        new Offset(100, -200, "kvd_benchmark", AnimalScript.DIRECTION_W),
        new Offset(250, -200, "kvd_benchmark", AnimalScript.DIRECTION_W) };
    lang.newPolyline(pfeil_node, "pfeil", null, pfeilProps);

    // Abschlusstext
    SourceCodeProperties abschlussProps = new SourceCodeProperties();
    abschlussProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    abschlussProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode abschluss = lang.newSourceCode(new Coordinates(250, 300),
        "abschluss", new TicksTiming(2 * zeit), abschlussProps);

    abschluss
        .addCodeLine(
            "F(min) ist eine minimierte äquivalente Funktion der Originalfunktion.",
            null, 0, null);
    abschluss
        .addCodeLine(
            "Es ist möglich, dass es noch weitere minimierte Funktionen gibt, allerdings",
            null, 0, null);
    abschluss
        .addCodeLine(
            "gibt es keine Funktion die mit weniger oder kleineren Termen auskommt.",
            null, 0, null);
  }

  /**
   * Prüft ob im Hilfsdiagramm nurnoch Felder mit '-' vorkommen
   * 
   * @param kvd
   *          KV-Diagramm
   * @param hd
   *          Hilfsdiagramm
   * @return true falls nurnoch '-' vorkommen, sonst false
   */
  private boolean checkReady(StringMatrix kvd, StringMatrix hd) {
    sourceCode.toggleHighlight(11, 12);
    sourceCode.highlight(13);
    for (int t = 0; t <= 3; t++)
      kvd.unhighlightCellColumnRange(t, 0, 3,
          new TicksTiming(coverTime * zeit), null);
    int crTime = 0;
    for (int i = 0; i <= 3; i++)
      for (int j = 0; j <= 3; j++) {
        hd.highlightCell(i, j, new TicksTiming(crTime * zeit), null);
        crTime++;
        if (!hd.getElement(i, j).equals("-"))
          return false;
        hd.unhighlightCell(i, j, new TicksTiming(crTime * zeit), null);
        crTime++;
      }
    return true;
  }

  /*
   * | A | TYP:0 | A | TYP:1 | A | TYP:2 | A | TYP:3 ___|_________|_________
   * ___|_________|_________ ___|_________|_________ ___|_________|_________ | 0
   * | 0 | 0 | 0 | | 1 | 0 | 0 | 1 | | 1 | 1 | 1 | 1 | | 1 | 0 | 0 | 1 | D
   * |____|____|____|____|___ D |____|____|____|____|___ D
   * |____|____|____|____|___ D |____|____|____|____|___ | 0 | 1 | 1 | 0 | | 1 |
   * 0 | 0 | 1 | | 0 | 0 | 0 | 0 | | 0 | 0 | 0 | 0 | ___|____|____|____|____| B
   * ___|____|____|____|____| B __|____|____|____|____| B
   * ___|____|____|____|____| B | 0 | 1 | 1 | 0 | | 1 | 0 | 0 | 1 | | 0 | 0 | 0
   * | 0 | | 0 | 0 | 0 | 0 | |____|____|____|____|___ |____|____|____|____|___
   * |____|____|____|____|___ |____|____|____|____|___ | 0 | 0 | 0 | 0 | | 1 | 0
   * | 0 | 1 | | 1 | 1 | 1 | 1 | | 1 | 0 | 0 | 1 | |____|____|____|____|
   * |____|____|____|____| |____|____|____|____| |____|____|____|____| | | | | |
   * | | | | C | | C | | C | | C |
   */

  /**
   * Prüft welcher Frametyp gebraucht wird und ruft die entsprechende Funktion
   * auf.
   * 
   * @Param colors Farbarray für die einzelnen Terme
   * @Param kvd_list Liste der einzelnen Primitiven vom KV-Diagramm
   */
  private void setFrame(Color[] colors, LinkedList<Primitive> kvd_list) {
    int offset = frameOffset();
    int left = -1;
    int right = -1;
    int top = -1;
    int bottom = -1;
    if (fmin[0].equals("1")) {
      frameType1 = false;
      frameType2 = false;
      frameType3 = false;
    }
    PolylineProperties framelineProps[] = { new PolylineProperties(),
        new PolylineProperties(), new PolylineProperties(),
        new PolylineProperties(), new PolylineProperties(),
        new PolylineProperties(), new PolylineProperties(),
        new PolylineProperties() };

    // Polyline Properties
    framelineProps[0].set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[0]);
    framelineProps[0].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    framelineProps[1].set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[1]);
    framelineProps[1].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    framelineProps[2].set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[2]);
    framelineProps[2].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    framelineProps[3].set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[3]);
    framelineProps[3].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    framelineProps[4].set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[4]);
    framelineProps[4].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    framelineProps[5].set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[5]);
    framelineProps[5].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    framelineProps[6].set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[6]);
    framelineProps[6].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    framelineProps[7].set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[7]);
    framelineProps[7].set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    if (frameType3)
      frameType3(offset, framelineProps, kvd_list);
    else {
      for (int i = 0; i < 4; i++)
        for (int j = 0; j < 4; j++) {
          if (cover[i][j] && top == -1)
            top = i;
          if (cover[i][j] && left == -1)
            left = j;
          if (cover[i][j] && top != -1)
            bottom = i;
          if (cover[i][j] && left != -1)
            right = j;
        }
      if (frameType2)
        frameType2(left, right, offset, framelineProps, kvd_list);
      else if (frameType1)
        frameType1(top, bottom, offset, framelineProps, kvd_list);
      else
        frameType0(left, right, top, bottom, offset, colors, kvd_list);
    }
  }

  /**
   * Zeichnet einen Rahmen vom Typ 0
   * 
   * @param left
   *          Linke Grenze des Termes
   * @param right
   *          Rechte Grenze des Termes
   * @param top
   *          Obere Grenze des Termes
   * @param bottom
   *          Untere Grenze des Termes
   * @param offset
   *          Abstand der Umrandung zum Diagrammgitter
   * @param colors
   *          Farbarray für die einzelnen Terme
   * @param kvd_list
   *          Liste der einzelnen Primitiven vom KV-Diagramm
   */
  private void frameType0(int left, int right, int top, int bottom, int offset,
      Color[] colors, LinkedList<Primitive> kvd_list) {
    // Termumrahmungen
    RectProperties frame0rectProps = new RectProperties();
    frame0rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[0]);
    frame0rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties frame1rectProps = new RectProperties();
    frame1rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[1]);
    frame1rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties frame2rectProps = new RectProperties();
    frame2rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[2]);
    frame2rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties frame3rectProps = new RectProperties();
    frame3rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[3]);
    frame3rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties frame4rectProps = new RectProperties();
    frame4rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[4]);
    frame4rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties frame5rectProps = new RectProperties();
    frame5rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[5]);
    frame5rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties frame6rectProps = new RectProperties();
    frame6rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[6]);
    frame6rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties frame7rectProps = new RectProperties();
    frame7rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[7]);
    frame7rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    switch (termes - 1) {
      case 0:
        Rect frame0 = lang.newRect(
            new Offset(border[left][0] + offset, border[top][0] + offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), new Offset(
                border[right][1] - offset, border[bottom][1] - offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), "frame0", null,
            frame0rectProps);
        kvd_list.add(frame0);
        break;
      case 1:
        Rect frame1 = lang.newRect(
            new Offset(border[left][0] + offset, border[top][0] + offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), new Offset(
                border[right][1] - offset, border[bottom][1] - offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), "frame1", null,
            frame1rectProps);
        kvd_list.add(frame1);
        break;
      case 2:
        Rect frame2 = lang.newRect(
            new Offset(border[left][0] + offset, border[top][0] + offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), new Offset(
                border[right][1] - offset, border[bottom][1] - offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), "frame2", null,
            frame2rectProps);
        kvd_list.add(frame2);
        break;
      case 3:
        Rect frame3 = lang.newRect(
            new Offset(border[left][0] + offset, border[top][0] + offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), new Offset(
                border[right][1] - offset, border[bottom][1] - offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), "frame3", null,
            frame3rectProps);
        kvd_list.add(frame3);
        break;
      case 4:
        Rect frame4 = lang.newRect(
            new Offset(border[left][0] + offset, border[top][0] + offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), new Offset(
                border[right][1] - offset, border[bottom][1] - offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), "frame4", null,
            frame4rectProps);
        kvd_list.add(frame4);
        break;
      case 5:
        Rect frame5 = lang.newRect(
            new Offset(border[left][0] + offset, border[top][0] + offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), new Offset(
                border[right][1] - offset, border[bottom][1] - offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), "frame5", null,
            frame5rectProps);
        kvd_list.add(frame5);
        break;
      case 6:
        Rect frame6 = lang.newRect(
            new Offset(border[left][0] + offset, border[top][0] + offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), new Offset(
                border[right][1] - offset, border[bottom][1] - offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), "frame6", null,
            frame6rectProps);
        kvd_list.add(frame6);
        break;
      case 7:
        Rect frame7 = lang.newRect(
            new Offset(border[left][0] + offset, border[top][0] + offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), new Offset(
                border[right][1] - offset, border[bottom][1] - offset,
                "kvd_benchmark", AnimalScript.DIRECTION_N), "frame7", null,
            frame7rectProps);
        kvd_list.add(frame7);
        break;
    }

  }

  /**
   * Zeichnet einen Rahmen vom Typ 1
   * 
   * @param top
   *          Obere Grenze des Termes
   * @param bottom
   *          Untere Grenze des Termes
   * @param offset
   *          Abstand der Umrandung zum Diagrammgitter
   * @param framelineProps
   *          Eigenschaften der Polylines
   * @param kvd_list
   *          Liste der einzelnen Primitiven vom KV-Diagramm
   */
  private void frameType1(int top, int bottom, int offset,
      PolylineProperties framelineProps[], LinkedList<Primitive> kvd_list) {
    Node[] nodesLeft = {
        new Offset(-5, border[top][0] + offset, "kvd_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(border[0][1] - offset, border[top][0] + offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(border[0][1] - offset, border[bottom][1] - offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(-5, border[bottom][1] - offset, "kvd_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline frameLeft = lang.newPolyline(nodesLeft,
        "leftFrame" + (termes - 1), null, framelineProps[termes - 1]);
    kvd_list.add(frameLeft);

    Node[] nodesRight = {
        new Offset(117, border[top][0] + offset, "kvd_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(border[3][0] + offset, border[top][0] + offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(border[3][0] + offset, border[bottom][1] - offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(117, border[bottom][1] - offset, "kvd_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline frameRight = lang.newPolyline(nodesRight, "rightFrame"
        + (termes - 1), null, framelineProps[termes - 1]);
    kvd_list.add(frameRight);
  }

  /**
   * Zeichnet einen Rahmen vom Typ 2
   * 
   * @param left
   *          Linke Grenze des Termes
   * @param right
   *          Rechte Grenze des Termes
   * @param offset
   *          Abstand der Umrandung zum Diagrammgitter
   * @param framelineProps
   *          Eigenschaften der Polylines
   * @param kvd_list
   *          Liste der einzelnen Primitiven vom KV-Diagramm
   */
  private void frameType2(int left, int right, int offset,
      PolylineProperties[] framelineProps, LinkedList<Primitive> kvd_list) {
    Node[] nodesTop = {
        new Offset(border[left][0] + offset, -5, "kvd_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(border[left][0] + offset, border[0][1] - offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(border[right][1] - offset, border[0][1] - offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(border[right][1] - offset, -5, "kvd_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline frameTop = lang.newPolyline(nodesTop, "topFrame" + (termes - 1),
        null, framelineProps[termes - 1]);
    kvd_list.add(frameTop);

    Node[] nodesBottom = {
        new Offset(border[left][0] + offset, 117, "kvd_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(border[left][0] + offset, border[3][0] + offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(border[right][1] - offset, border[3][0] + offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(border[right][1] - offset, 117, "kvd_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline frameBottom = lang.newPolyline(nodesBottom, "bottomFrame"
        + (termes - 1), null, framelineProps[termes - 1]);
    kvd_list.add(frameBottom);
  }

  /**
   * Zeichnet einen Rahmen vom Typ 3
   * 
   * @param offset
   *          Abstand der Umrandung zum Diagrammgitter
   * @param framelineProps
   *          Eigenschaften der Polylines
   * @param kvd_list
   *          Liste der einzelnen Primitiven vom KV-Diagramm
   */
  private void frameType3(int offset, PolylineProperties framelineProps[],
      LinkedList<Primitive> kvd_list) {
    Node[] nodesUpperLeft = {
        new Offset(-5, border[0][1] - offset, "kvd_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(border[0][1] - offset, border[0][1] - offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(border[0][1] - offset, -5, "kvd_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline frameUpperLeft = lang.newPolyline(nodesUpperLeft, "upperLeftFrame"
        + (termes - 1), null, framelineProps[termes - 1]);
    kvd_list.add(frameUpperLeft);

    Node[] nodesLowerLeft = {
        new Offset(-5, border[3][0] + offset, "kvd_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(border[0][1] - offset, border[3][0] + offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(border[0][1] - offset, 117, "kvd_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline frameLowerLeft = lang.newPolyline(nodesLowerLeft, "lowerLeftFrame"
        + (termes - 1), null, framelineProps[termes - 1]);
    kvd_list.add(frameLowerLeft);

    Node[] nodesUpperRight = {
        new Offset(border[3][0] + offset, -5, "kvd_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(border[3][0] + offset, border[0][1] - offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(117, border[0][1] - offset, "kvd_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline frameUpperRight = lang.newPolyline(nodesUpperRight,
        "upperRightFrame" + (termes - 1), null, framelineProps[termes - 1]);
    kvd_list.add(frameUpperRight);

    Node[] nodesLowerRight = {
        new Offset(border[3][0] + offset, 117, "kvd_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset(border[3][0] + offset, border[3][0] + offset,
            "kvd_benchmark", AnimalScript.DIRECTION_N),
        new Offset(117, border[3][0] + offset, "kvd_benchmark",
            AnimalScript.DIRECTION_N) };
    Polyline frameLowerRight = lang.newPolyline(nodesLowerRight,
        "lowerRightFrame" + (termes - 1), null, framelineProps[termes - 1]);
    kvd_list.add(frameLowerRight);
  }

  /**
   * Berechnet den nötigen Abstand der Rahmen zum Diagramm Gitter um keine 2
   * Rahmen uebereinander zu haben.
   * 
   * @return Abstand zum Diagrammgitter
   */
  private int frameOffset() {
    int offset = 0;
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++)
        if (cover[i][j] && frames[i][j] > offset)
          offset = frames[i][j];
    return offset + 1;
  }

  /**
   * In alle abgedeckten Felder im Hilfsdiagramm wird ein "-" eingetragen.
   * 
   * @param hd
   *          Hilfsdiagramm
   */
  private void updateHD(StringMatrix hd) {
    sourceCode.toggleHighlight(10, 11);
    sourceCode.unhighlight(9);
    for (int i = 0; i <= 3; i++)
      for (int j = 0; j <= 3; j++)
        if (cover[i][j] == true)
          hd.put(i, j, "-", null, null);
  }

  /**
   * Trägt den gefundenen Term in fmin[] ein
   * 
   * @param term
   *          Term der gefunden wurde
   * @param colors
   *          Farbarray aus dem die Farbe des Termes ausgelesen wird
   * @param fmin_list
   *          Liste mit Primitiven von F(min)
   */
  private void updateFmin(String term, Color[] colors,
      LinkedList<Primitive> fmin_list) {
    // plus
    TextProperties plusProps = new TextProperties();
    plusProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    plusProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // Term 1
    TextProperties term1Props = new TextProperties();
    term1Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    term1Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[0]);
    // Term 2
    TextProperties term2Props = new TextProperties();
    term2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    term2Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[1]);
    // Term 3
    TextProperties term3Props = new TextProperties();
    term3Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    term3Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[2]);
    // Term 4
    TextProperties term4Props = new TextProperties();
    term4Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    term4Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[3]);
    // Term 5
    TextProperties term5Props = new TextProperties();
    term5Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    term5Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[4]);
    // Term 6
    TextProperties term6Props = new TextProperties();
    term6Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    term6Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[5]);
    // Term 7
    TextProperties term7Props = new TextProperties();
    term7Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    term7Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[6]);
    // Term 8
    TextProperties term8Props = new TextProperties();
    term8Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    term8Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[7]);

    switch (termes) {
      case 0:
        Text term1 = lang.newText(new Offset(0, 0, "fmin",
            AnimalScript.DIRECTION_NE), fmin[termes] + " ", "term1", null,
            term1Props);
        fmin_list.add(term1);
        break;
      case 1:
        Text plus1 = lang.newText(new Offset(0, 0, "term1",
            AnimalScript.DIRECTION_NE), "+ ", "plus1", null, plusProps);
        Text term2 = lang.newText(new Offset(0, 0, "plus1",
            AnimalScript.DIRECTION_NE), fmin[termes] + " ", "term2", null,
            term2Props);
        fmin_list.add(plus1);
        fmin_list.add(term2);
        break;
      case 2:
        Text plus2 = lang.newText(new Offset(0, 0, "term2",
            AnimalScript.DIRECTION_NE), "+ ", "plus2", null, plusProps);
        Text term3 = lang.newText(new Offset(0, 0, "plus2",
            AnimalScript.DIRECTION_NE), fmin[termes] + " ", "term3", null,
            term3Props);
        fmin_list.add(plus2);
        fmin_list.add(term3);
        break;
      case 3:
        Text plus3 = lang.newText(new Offset(0, 0, "term3",
            AnimalScript.DIRECTION_NE), "+ ", "plus3", null, plusProps);
        Text term4 = lang.newText(new Offset(0, 0, "plus3",
            AnimalScript.DIRECTION_NE), fmin[termes] + " ", "term4", null,
            term4Props);
        fmin_list.add(plus3);
        fmin_list.add(term4);
        break;
      case 4:
        Text plus4 = lang.newText(new Offset(0, 0, "term4",
            AnimalScript.DIRECTION_NE), "+ ", "plus4", null, plusProps);
        Text term5 = lang.newText(new Offset(0, 0, "plus4",
            AnimalScript.DIRECTION_NE), fmin[termes] + " ", "term5", null,
            term5Props);
        fmin_list.add(plus4);
        fmin_list.add(term5);
        break;
      case 5:
        Text plus5 = lang.newText(new Offset(0, 0, "term5",
            AnimalScript.DIRECTION_NE), "+ ", "plus5", null, plusProps);
        Text term6 = lang.newText(new Offset(0, 0, "plus5",
            AnimalScript.DIRECTION_NE), fmin[termes] + " ", "term6", null,
            term6Props);
        fmin_list.add(plus5);
        fmin_list.add(term6);
        break;
      case 6:
        Text plus6 = lang.newText(new Offset(0, 0, "term6",
            AnimalScript.DIRECTION_NE), "+ ", "plus6", null, plusProps);
        Text term7 = lang.newText(new Offset(0, 0, "plus6",
            AnimalScript.DIRECTION_NE), fmin[termes] + " ", "term7", null,
            term7Props);
        fmin_list.add(plus6);
        fmin_list.add(term7);
        break;
      case 7:
        Text plus7 = lang.newText(new Offset(0, 0, "term7",
            AnimalScript.DIRECTION_NE), "+ ", "plus7", null, plusProps);
        Text term8 = lang.newText(new Offset(0, 0, "plus7",
            AnimalScript.DIRECTION_NE), fmin[termes] + " ", "term8", null,
            term8Props);
        fmin_list.add(plus7);
        fmin_list.add(term8);
        break;
    }
  }

  /**
   * Wertet die gefundene Abdeckung zu einem Term aus
   * 
   * @param hd
   *          Hilfsdiagramm
   * @param colors
   *          Farbarray fuer die Terme von F(min)
   * @param fmin_list
   *          Liste mit Primitiven von F(min)
   */
  private void searchTerm(StringMatrix hd, Color[] colors,
      LinkedList<Primitive> fmin_list) {
    frameType1 = true;
    frameType2 = true;
    frameType3 = true;
    sourceCode.toggleHighlight(7, 9);
    sourceCode.toggleHighlight(8, 10);
    sourceCode.unhighlight(5);
    sourceCode.unhighlight(6);
    String term = "";
    if (fmin[0] == null && hd.getElement(i, j).equals("4"))
      term = "1"; // es gibt keine 0 in der Funktion
    else if (fmin[0] == null && hd.getElement(i, j).equals("-"))
      term = "0"; // es gibt keine 1 in der Funktion
    else {
      term = term + checkA();
      term = term + checkB();
      term = term + checkC();
      term = term + checkD();

    }
    fmin[termes] = term;
    updateFmin(term, colors, fmin_list);
    termes++;
  }

  /**
   * Prüft ob "A" oder "-A" im Term vorkommt und gibt dieses an "updateFmin()"
   * zurück
   * 
   * @return "A " / "-A " / ""
   */
  private String checkA() {
    boolean termA = true;
    boolean termNotA = true;
    for (int j = 0; j <= 1; j++)
      for (int i = 0; i <= 3; i++)
        if (cover[i][j])
          termNotA = false;
    for (int j = 2; j <= 3; j++)
      for (int i = 0; i <= 3; i++)
        if (cover[i][j])
          termA = false;
    if (termA) {
      frameType1 = false;
      frameType3 = false;
      return "A ";
    }
    if (termNotA) {
      frameType1 = false;
      frameType3 = false;
      return "-A ";
    }
    return "";
  }

  /**
   * Prüft ob "B" oder "-B" im Term vorkommt und gibt dieses an "updateFmin()"
   * zurück
   * 
   * @return "B " / "-B " / ""
   */
  private String checkB() {
    boolean termB = true;
    boolean termNotB = true;
    for (int i = 1; i <= 2; i++)
      for (int j = 0; j <= 3; j++)
        if (cover[i][j])
          termNotB = false;
    i = 0;
    for (int j = 0; j <= 3; j++)
      if (cover[i][j])
        termB = false;
    i = 3;
    for (int j = 0; j <= 3; j++)
      if (cover[i][j])
        termB = false;

    if (termB) {
      frameType2 = false;
      frameType3 = false;
      return "B ";
    }
    if (termNotB)
      return "-B ";
    frameType2 = false;
    frameType3 = false;
    return "";
  }

  /**
   * Prüft ob "C" oder "-C" im Term vorkommt und gibt dieses an "updateFmin()"
   * zurück
   * 
   * @return "C " / "-C " / ""
   */
  private String checkC() {
    boolean termC = true;
    boolean termNotC = true;
    for (int j = 1; j <= 2; j++)
      for (int i = 0; i <= 3; i++)
        if (cover[i][j])
          termNotC = false;
    j = 0;
    for (int i = 0; i <= 3; i++)
      if (cover[i][j])
        termC = false;
    j = 3;
    for (int i = 0; i <= 3; i++)
      if (cover[i][j])
        termC = false;

    if (termC) {
      frameType1 = false;
      frameType3 = false;
      return "C ";
    }
    if (termNotC)
      return "-C ";
    frameType1 = false;
    frameType3 = false;
    return "";
  }

  /**
   * Prüft ob "D" oder "-D" im Term vorkommt und gibt dieses an "updateFmin()"
   * zurück
   * 
   * @return "D " / "-D " / ""
   */
  private String checkD() {
    boolean termD = true;
    boolean termNotD = true;
    for (int i = 0; i <= 1; i++)
      for (int j = 0; j <= 3; j++)
        if (cover[i][j])
          termNotD = false;
    for (int i = 2; i <= 3; i++)
      for (int j = 0; j <= 3; j++)
        if (cover[i][j])
          termD = false;
    if (termD) {
      frameType2 = false;
      frameType3 = false;
      return "D ";
    }
    if (termNotD) {
      frameType2 = false;
      frameType3 = false;
      return "-D ";
    }
    return "";
  }

  /**
   * Erhöht die Anzahl der gefundenen Abdeckungen der Felder
   */
  private void incFrames() {
    for (int i = 0; i <= 3; i++)
      for (int j = 0; j <= 3; j++)
        if (cover[i][j] == true)
          frames[i][j]++;
  }

  /**
   * Sucht im KV-Diagramm nach einer möglichst großen Abdeckung für das Feld
   * kvd[i][j]
   * 
   * @param kvd
   *          KV-Diagramm
   */
  private void searchCover(StringMatrix kvd) {
    sourceCode.toggleHighlight(5, 7);
    sourceCode.toggleHighlight(6, 8);
    boolean foundCover = false;
    foundCover = checkCover4x4(kvd);
    if (!foundCover)
      foundCover = checkCover4x2(i, kvd);
    if (!foundCover)
      foundCover = checkCover4x2(convert(i + 1), kvd);
    if (!foundCover)
      foundCover = checkCover2x4(j, kvd);
    if (!foundCover)
      foundCover = checkCover2x4(convert(j + 1), kvd);
    if (!foundCover)
      foundCover = checkCover4x1(i, kvd);
    if (!foundCover)
      foundCover = checkCover1x4(j, kvd);
    if (!foundCover)
      foundCover = checkCover2x2(i, j, kvd);
    if (!foundCover)
      foundCover = checkCover2x2(i, convert(j + 1), kvd);
    if (!foundCover)
      foundCover = checkCover2x2(convert(i + 1), j, kvd);
    if (!foundCover)
      foundCover = checkCover2x2(convert(i + 1), convert(j + 1), kvd);
    if (!foundCover)
      foundCover = checkCover2x1(i, j, kvd);
    if (!foundCover)
      foundCover = checkCover2x1(i, convert(j + 1), kvd);
    if (!foundCover)
      foundCover = checkCover1x2(i, j, kvd);
    if (!foundCover)
      foundCover = checkCover1x2(convert(i + 1), j, kvd);
    if (!foundCover)
      checkCover1x1(i, j, kvd);
  }

  /**
   * Setzt alle Felder von cover[][] auf false zurück
   */
  private void clearCover(StringMatrix kvd) {
    coverTime++;
    lang.addLine("setGridColor \"kvd[][]\" textColor black");
    for (int i = 0; i <= 3; i++) {
      kvd.unhighlightCellColumnRange(i, 0, 3,
          new TicksTiming(coverTime * zeit), null);
      for (int j = 0; j <= 3; j++) {
        cover[i][j] = false;
      }
    }
    coverTime++;
  }

  /**
   * Prüft eine 4x4 Abdeckung
   * 
   * @param kvd
   *          KV-Diagramm
   * @return true falls Abdeckung gefunden wird, sonst false
   */
  private boolean checkCover4x4(StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCellColumnRange(0, 0, 3, new TicksTiming(coverTime * zeit),
        null);
    kvd.highlightCellColumnRange(1, 0, 3, new TicksTiming(coverTime * zeit),
        null);
    kvd.highlightCellColumnRange(2, 0, 3, new TicksTiming(coverTime * zeit),
        null);
    kvd.highlightCellColumnRange(3, 0, 3, new TicksTiming(coverTime * zeit),
        null);
    boolean thisCover = true;
    for (int i = 0; i <= 3; i++)
      for (int j = 0; j <= 3; j++)
        if (kvd.getElement(i, j).equals("0")) {
          thisCover = false;
          lang.addLine("setGridColor \"kvd[" + i + "][" + j
              + "]\" textColor red after " + coverTime * zeit + " ticks");
        } else {
          cover[i][j] = true;
          lang.addLine("setGridColor \"kvd[" + i + "][" + j
              + "]\" textColor green after " + coverTime * zeit + " ticks");
        }
    return thisCover;
  }

  /**
   * Prüft eine 4x2 Abdeckung
   * 
   * @param x
   *          x-Position im KV-Diagramm die geprüft werden soll
   * @param kvd
   *          KV-Diagramm
   * @return true falls Abdeckung gefunden wird, sonst false
   */
  private boolean checkCover4x2(int x, StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCellColumnRange(convert(x - 1), 0, 3, new TicksTiming(
        coverTime * zeit), null);
    kvd.highlightCellColumnRange(x, 0, 3, new TicksTiming(coverTime * zeit),
        null);
    boolean thisCover = true;
    for (int i = x - 1; i <= x; i++) {
      int tempI = convert(i);
      for (int j = 0; j <= 3; j++)
        if (kvd.getElement(tempI, j).equals("0")) {
          thisCover = false;
          lang.addLine("setGridColor \"kvd[" + tempI + "][" + j
              + "]\" textColor red after " + coverTime * zeit + " ticks");
        } else {
          cover[tempI][j] = true;
          lang.addLine("setGridColor \"kvd[" + tempI + "][" + j
              + "]\" textColor green after " + coverTime * zeit + " ticks");
        }
    }
    return thisCover;
  }

  /**
   * Prüft eine 2x4 Abdeckung
   * 
   * @param y
   *          y-Position im KV-Diagramm die geprüft werden soll
   * @param kvd
   *          KV-Diagramm
   * @return true falls Abdeckung gefunden wird, sonst false
   */
  private boolean checkCover2x4(int y, StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCellRowRange(0, 3, convert(y - 1), new TicksTiming(coverTime
        * zeit), null);
    kvd.highlightCellRowRange(0, 3, y, new TicksTiming(coverTime * zeit), null);
    boolean thisCover = true;
    for (int j = y - 1; j <= y; j++) {
      int tempJ = convert(j);
      for (int i = 0; i <= 3; i++)
        if (kvd.getElement(i, tempJ).equals("0")) {
          thisCover = false;
          lang.addLine("setGridColor \"kvd[" + i + "][" + tempJ
              + "]\" textColor red after " + coverTime * zeit + " ticks");
        } else {
          cover[i][tempJ] = true;
          lang.addLine("setGridColor \"kvd[" + i + "][" + tempJ
              + "]\" textColor green after " + coverTime * zeit + " ticks");
        }
    }
    return thisCover;
  }

  /**
   * Prüft eine 4x1 Abdeckung
   * 
   * @param x
   *          x-Position im KV-Diagramm die geprüft werden soll
   * @param kvd
   *          KV-Diagramm
   * @return true falls Abdeckung gefunden wird, sonst false
   */
  private boolean checkCover4x1(int x, StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCellColumnRange(x, 0, 3, new TicksTiming(coverTime * zeit),
        null);
    boolean thisCover = true;
    for (int i = 0; i <= 3; i++)
      if (kvd.getElement(x, i).equals("0")) {
        thisCover = false;
        lang.addLine("setGridColor \"kvd[" + x + "][" + i
            + "]\" textColor red after " + coverTime * zeit + " ticks");
      } else {
        cover[x][i] = true;
        lang.addLine("setGridColor \"kvd[" + x + "][" + i
            + "]\" textColor green after " + coverTime * zeit + " ticks");
      }
    return thisCover;
  }

  /**
   * Prüft eine 1x4 Abdeckung
   * 
   * @param y
   *          y-Position im KV-Diagramm die geprüft werden soll
   * @param kvd
   *          KV-Diagramm
   * @return true falls Abdeckung gefunden wird, sonst false
   */
  private boolean checkCover1x4(int y, StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCellRowRange(0, 3, y, new TicksTiming(coverTime * zeit), null);
    boolean thisCover = true;
    for (int i = 0; i <= 3; i++)
      if (kvd.getElement(i, y).equals("0")) {
        thisCover = false;
        lang.addLine("setGridColor \"kvd[" + i + "][" + y
            + "]\" textColor red after " + coverTime * zeit + " ticks");
      } else {
        cover[i][y] = true;
        lang.addLine("setGridColor \"kvd[" + i + "][" + y
            + "]\" textColor green after " + coverTime * zeit + " ticks");
      }
    return thisCover;
  }

  /**
   * Prüft eine 2x2 Abdeckung
   * 
   * @param x
   *          x-Position im KV-Diagramm die geprüft werden soll
   * @param y
   *          y-Position im KV-Diagramm die geprüft werden soll
   * @param kvd
   *          KV-Diagramm
   * @return true falls Abdeckung gefunden wird, sonst false
   */
  private boolean checkCover2x2(int x, int y, StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCell(convert(x - 1), convert(y - 1), new TicksTiming(coverTime
        * zeit), null);
    kvd.highlightCell(convert(x - 1), y, new TicksTiming(coverTime * zeit),
        null);
    kvd.highlightCell(x, y, new TicksTiming(coverTime * zeit), null);
    kvd.highlightCell(x, convert(y - 1), new TicksTiming(coverTime * zeit),
        null);
    boolean thisCover = true;
    for (int i = x - 1; i <= x; i++) {
      int tempI = convert(i);
      for (int j = y - 1; j <= y; j++) {
        int tempJ = convert(j);
        if (kvd.getElement(tempI, tempJ).equals("0")) {
          thisCover = false;
          lang.addLine("setGridColor \"kvd[" + tempI + "][" + tempJ
              + "]\" textColor red after " + coverTime * zeit + " ticks");
        } else {
          cover[tempI][tempJ] = true;
          lang.addLine("setGridColor \"kvd[" + tempI + "][" + tempJ
              + "]\" textColor green after " + coverTime * zeit + " ticks");
        }
      }
    }
    return thisCover;
  }

  /**
   * Prüft eine 2x1 Abdeckung
   * 
   * @param x
   *          x-Position im KV-Diagramm die geprüft werden soll
   * @param y
   *          y-Position im KV-Diagramm die geprüft werden soll
   * @param kvd
   *          KV-Diagramm
   * @return true falls Abdeckung gefunden wird, sonst false
   */
  private boolean checkCover2x1(int x, int y, StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCell(x, y, new TicksTiming(coverTime * zeit), null);
    kvd.highlightCell(x, convert(y - 1), new TicksTiming(coverTime * zeit),
        null);
    boolean thisCover = true;
    for (int j = y - 1; j <= y; j++) {
      int tempJ = convert(j);
      if (kvd.getElement(x, tempJ).equals("0")) {
        thisCover = false;
        lang.addLine("setGridColor \"kvd[" + x + "][" + tempJ
            + "]\" textColor red after " + coverTime * zeit + " ticks");
      } else {
        cover[x][tempJ] = true;
        lang.addLine("setGridColor \"kvd[" + x + "][" + tempJ
            + "]\" textColor green after " + coverTime * zeit + " ticks");
      }
    }
    return thisCover;
  }

  /**
   * Prüft eine 1x2 Abdeckung
   * 
   * @param x
   *          x-Position im KV-Diagramm die geprüft werden soll
   * @param y
   *          y-Position im KV-Diagramm die geprüft werden soll
   * @param kvd
   *          KV-Diagramm
   * @return true falls Abdeckung gefunden wird, sonst false
   */
  private boolean checkCover1x2(int x, int y, StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCell(convert(x - 1), y, new TicksTiming(coverTime * zeit),
        null);
    kvd.highlightCell(x, y, new TicksTiming(coverTime * zeit), null);
    boolean thisCover = true;
    for (int i = x - 1; i <= x; i++) {
      int tempI = convert(i);
      if (kvd.getElement(tempI, y).equals("0")) {
        thisCover = false;
        lang.addLine("setGridColor \"kvd[" + tempI + "][" + y
            + "]\" textColor red after " + coverTime * zeit + " ticks");
      } else {
        cover[tempI][y] = true;
        lang.addLine("setGridColor \"kvd[" + tempI + "][" + y
            + "]\" textColor green after " + coverTime * zeit + " ticks");
      }
    }
    return thisCover;
  }

  /**
   * Prüft eine 1x1 Abdeckung
   * 
   * @param x
   *          x-Position im KV-Diagramm die geprüft werden soll
   * @param y
   *          y-Position im KV-Diagramm die geprüft werden soll
   * @param kvd
   *          KV-Diagramm
   */
  private void checkCover1x1(int x, int y, StringMatrix kvd) {
    clearCover(kvd);
    kvd.highlightCell(x, y, new TicksTiming(coverTime * zeit), null);
    if (!kvd.getElement(x, y).equals("0")) {
      cover[x][y] = true;
      lang.addLine("setGridColor \"kvd[" + x + "][" + y
          + "]\" textColor green after " + coverTime * zeit + " ticks");
    }

  }

  /**
   * Sucht im Hilfsdiagramm nach dem Feld mit der kleinsten Zahl
   * 
   * @param hd
   *          Hilfsdiagramm
   */
  private void searchSmallest(StringMatrix hd) {
    sourceCode.unhighlight(12);
    sourceCode.unhighlight(13);
    sourceCode.toggleHighlight(1, 5);
    sourceCode.highlight(6);
    int t = 0;
    int smallestI = 0;
    int smallestJ = 0;
    String smallest = "5";
    for (int x = 0; x <= 3; x++)
      for (int y = 0; y <= 3; y++) {
        hd.highlightCell(x, y, new TicksTiming(t * zeit), null);
        t++;
        if (!hd.getElement(x, y).equals("-")
            && hd.getElement(x, y).compareTo(smallest) < 0) {
          if (x != smallestI && y != smallestJ)
            hd.unhighlightCell(smallestI, smallestJ, new TicksTiming(t * zeit),
                null);
          smallestI = x;
          smallestJ = y;
          smallest = hd.getElement(smallestI, smallestJ);
        } else
          hd.unhighlightCell(x, y, new TicksTiming(t * zeit), null);
      }
    i = smallestI;
    j = smallestJ;
    if (!smallest.equals("5"))
      foundSmallest = true;
  }

  /**
   * Ruft setHDField für jedes Element im Hilfsdiagramm auf
   * 
   * @param kvd
   *          KV-Diagramm
   * @param hd
   *          Hilfsdiagramm
   */
  private void fillHD(StringMatrix kvd, StringMatrix hd) {
    sourceCode.toggleHighlight(0, 1);
    for (int i = 0; i <= 3; i++)
      for (int j = 0; j <= 3; j++) {
        fillHD++;
        hd.highlightCell(i, j, new TicksTiming(fillHD * zeit), null);
        kvd.highlightCell(i, j, new TicksTiming(fillHD * zeit), null);
        setHDField(i, j, kvd, hd);
        hd.unhighlightCell(i, j, new TicksTiming(fillHD * zeit), null);
        kvd.unhighlightCell(i, j, new TicksTiming(fillHD * zeit), null);

      }
  }

  /**
   * Prüft das Zeichen im KV-Diagramm an der Stelle kvd[i][j]
   * 
   * @param theI
   *          Zeile im KV-Diagramm
   * @param theJ
   *          Spalte im KV-Diagramm
   * @param kvd
   *          KV-Diagramm
   * @return 0 falls im Feld kvd[i][j], sonst 1
   */
  private int checkField(int theI, int theJ, StringMatrix kvd) {
    int i = convert(theI), j = convert(theJ);
    if (kvd.getElement(i, j).equals("0"))
      return 0;
    else
      return 1;
  }

  /**
   * Zählt in wievielen der benachbarten Felder von kvd[i][j] "1" oder "X" steht
   * und gibt den Wert als String zurück
   * 
   * @param i
   *          Zeile im KV-Diagramm
   * @param j
   *          Spalte im KV-Diagramm
   * @param kvd
   *          KV-Diagramm
   * @return gefundene Anzahl an "1" bzw. "X" als String
   */
  private String countNeighbors(int i, int j, StringMatrix kvd) {
    fillHD++;
    kvd.unhighlightCell(i, j, new TicksTiming(fillHD * zeit), null);
    kvd.highlightCell(convert(i - 1), j, new TicksTiming(fillHD * zeit), null);
    kvd.highlightCell(convert(i + 1), j, new TicksTiming(fillHD * zeit), null);
    kvd.highlightCell(i, convert(j - 1), new TicksTiming(fillHD * zeit), null);
    kvd.highlightCell(i, convert(j + 1), new TicksTiming(fillHD * zeit), null);
    int counter = 0;
    String field;
    counter = counter + checkField(i - 1, j, kvd);
    counter = counter + checkField(i, j + 1, kvd);
    counter = counter + checkField(i + 1, j, kvd);
    counter = counter + checkField(i, j - 1, kvd);
    switch (counter) {
      case 0:
        field = "0";
        break;
      case 1:
        field = "1";
        break;
      case 2:
        field = "2";
        break;
      case 3:
        field = "3";
        break;
      default:
        field = "4";
    }
    return field;
  }

  /**
   * Füllt im Hilfsdiagramm das Feld an Position hd[i][j] aus... - steht im Feld
   * kvd[i][j] eine "1" wird die Anzahl von countNeighbours eingetragen - steht
   * im Feld kvd[i][j] eine "0" oder "X" wird "-" eingetragen
   * 
   * @param i
   *          Zeile im Diagramm
   * @param j
   *          Spalte im Diagramm
   * @param kvd
   *          KV-Diagramm
   * @param hd
   *          Hilfsdiagramm
   */
  private void setHDField(int i, int j, StringMatrix kvd, StringMatrix hd) {
    if (!kvd.getElement(i, j).equals("1")) {
      sourceCode.highlight(4, 0, true, new TicksTiming(fillHD * zeit), null);
      hd.put(i, j, "-", new TicksTiming(fillHD * zeit), null);
      fillHD++;
      sourceCode.unhighlight(4, 0, true, new TicksTiming(fillHD * zeit), null);
    } else {
      sourceCode.highlight(2, 0, true, new TicksTiming(fillHD * zeit), null);
      sourceCode.highlight(3, 0, true, new TicksTiming(fillHD * zeit), null);
      hd.put(i, j, countNeighbors(i, j, kvd), new TicksTiming(fillHD * zeit),
          null);
      fillHD++;
      kvd.unhighlightCell(convert(i - 1), j, new TicksTiming(fillHD * zeit),
          null);
      kvd.unhighlightCell(convert(i + 1), j, new TicksTiming(fillHD * zeit),
          null);
      kvd.unhighlightCell(i, convert(j - 1), new TicksTiming(fillHD * zeit),
          null);
      kvd.unhighlightCell(i, convert(j + 1), new TicksTiming(fillHD * zeit),
          null);
      sourceCode.unhighlight(2, 0, true, new TicksTiming(fillHD * zeit), null);
      sourceCode.unhighlight(3, 0, true, new TicksTiming(fillHD * zeit), null);
    }
  }

  /**
   * Füllt das KV-Diagramm mit den Werten der Originalfunktion
   * 
   * @param function
   *          Originalfunktion
   * @param kvd
   *          KV-Diagramm
   */
  private void fillKVD(StringMatrix function, StringMatrix kvd) {
    sourceCode.highlight(0);

    /*
     * | A | ___|_________|_________ | 9 | 11 | 3 | 1 | D
     * |____|____|____|____|___ | 13 | 15 | 7 | 5 | ___|____|____|____|____| B |
     * 12 | 14 | 6 | 4 | |____|____|____|____|___ | 8 | 10 | 2 | 0 |
     * |____|____|____|____| | | | C |
     */
    int[][] diagrammfelder = { { 3, 3 }, { 0, 3 }, { 3, 2 }, { 0, 2 },
        { 2, 3 }, { 1, 3 }, { 2, 2 }, { 1, 2 }, { 3, 0 }, { 0, 0 }, { 3, 1 },
        { 0, 1 }, { 2, 0 }, { 1, 0 }, { 2, 1 }, { 1, 1 } };
    for (int i = 0; i < 16; i++) {
      function.highlightElemColumnRange(i + 1, 0, 5, new TicksTiming(i * zeit),
          null);
      kvd.highlightCell(diagrammfelder[i][0], diagrammfelder[i][1],
          new TicksTiming(i * zeit), null);
      kvd.put(diagrammfelder[i][0], diagrammfelder[i][1],
          function.getElement(i + 1, 5), new TicksTiming(i * zeit), null);
      function.unhighlightElemColumnRange(i + 1, 0, 5, new TicksTiming((i + 1)
          * zeit), null);
      kvd.unhighlightCell(diagrammfelder[i][0], diagrammfelder[i][1],
          new TicksTiming((i + 1) * zeit), null);
    }
  }

  /**
   * Erstellt den SourceCode vom Algorithmus
   * 
   * @return SourceCode
   */
  private SourceCode showSourceCode() {
    // Code
    SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);

    SourceCode sourceCode = lang.newSourceCode(new Coordinates(10, 40),
        "sourceCode", null, sourceCodeProps);

    sourceCode.addCodeLine(
        "1. Fülle das KV-Diagramm entsprechend der Originalfunktion aus.",
        null, 0, null); // 0
    sourceCode
        .addCodeLine(
            "2. Prüfe für jedes Feld im Hilfsdiagramm, was im gleichen Feld im KV-Diagramm steht...",
            null, 0, null);
    sourceCode
        .addCodeLine(
            "- '1': Zähle in wievielen der vier direkt benachbarten Felder eine '1'",
            null, 2, null);
    sourceCode
        .addCodeLine(
            "oder ein 'X' steht und trage die Anzahl in das Feld im Hilfsdiagramm ein.",
            null, 3, null); // 3
    sourceCode.addCodeLine(
        "- '0 oder 'X'': Trage im Hilfsdiagramm ein '-' ein.", null, 2, null); // 4
    sourceCode.addCodeLine(
        "3. Suche im Hilfsdiagramm das Feld mit der kleinsten Zahl.", null, 0,
        null); // 5
    sourceCode.addCodeLine(
        "Bei mehreren Feldern mit der gleichen Zahl nimm das erste gefundene.",
        null, 2, null); // 6
    sourceCode
        .addCodeLine(
            "4. Suche für das gefundene Feld im KV-Diagramm nach der größtmöglichen Abdeckung ohne '0'.",
            null, 0, null); // 7
    sourceCode
        .addCodeLine(
            "Bei mehreren möglichen Abdeckungen mit der gleichen Größe nimm die erste gefundene.",
            null, 2, null); // 8
    sourceCode.addCodeLine(
        "5. Makiere die gefundene Abdeckung dauerhaft im KV-Diagramm", null, 0,
        null); // 9
    sourceCode.addCodeLine(
        "und trage den entsprechenden Term in die Ergebnisfunktion ein.", null,
        2, null); // 10
    sourceCode
        .addCodeLine(
            "6. Makiere im Hilfsdiagramm alle eben abgedeckten Felder mit einem '-'.",
            null, 0, null); // 11
    sourceCode
        .addCodeLine(
            "7. Gibt es im Hilfsdiagramm noch Felder in denen eine Zahl steht, mache bei Schritt 3 weiter.",
            null, 0, null); // 12
    sourceCode.addCodeLine(
        "Sind alle Felder mit einem '-' makiert, ist der Algorithmus fertig.",
        null, 2, null); // 13
    return sourceCode;
  }

  /**
   * Erstellt den Titel der Animation
   */
  private void zeigeTitel() {
    TextProperties titelProps = new TextProperties();
    titelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    titelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    titelProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties hrectProps = new RectProperties();
    hrectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    hrectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    hrectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    hrectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    Text titel = lang.newText(new Coordinates(20, 20),
        "Funktionsminimierung mittels KV-Diagramm", "titel", null, titelProps);
    Rect hRect = lang.newRect(new Offset(-2, -2, "titel",
        AnimalScript.DIRECTION_NW), new Offset(2, 2, "titel",
        AnimalScript.DIRECTION_SE), "hRect", null, hrectProps);

    LinkedList<Primitive> titel_list = new LinkedList<Primitive>();
    titel_list.add(titel);
    titel_list.add(hRect);
    titel_Group = lang.newGroup(titel_list, "titel_group");
  }

  /**
   * Erstellt die Einleitungsseite
   */
  private void einleitung() {
    SourceCodeProperties einleitungProps = new SourceCodeProperties();
    einleitungProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    einleitungProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    einleitungProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode einleitung = lang.newSourceCode(new Coordinates(10, 40),
        "beschreibung", null, einleitungProps);

    einleitung
        .addCodeLine(
            "Das KV-Diagramm mit 16 Feldern ist ein graphisches Minimierungsverfahren für boolsche Ausdrücke mit maximal vier Variablen.",
            null, 0, null); // 0
    einleitung
        .addCodeLine(
            "Dabei werden in die 16 Felder entsprechend des boolschen Ausdrucks jeweils die Werte logisch eins '1', logisch null '0'",
            null, 0, null);
    einleitung
        .addCodeLine(
            "oder don't care 'X' eingetragen. Um eine minimale Abdeckung zu erreichen, müssen alle Felder in denen eine '1' steht abgedeckt",
            null, 0, null);
    einleitung
        .addCodeLine(
            "werden. Die Abdeckungen sind so zu wählen, dass sie möglichst viele Felder abdecken. Dabei darf aber keine '0' mit abgedeckt",
            null, 0, null); // 3
    einleitung
        .addCodeLine(
            "werden. Felder in denen ein don`t care steht können mit abgedeckt werden, müssen aber nicht.",
            null, 0, null); // 4
    einleitung
        .addCodeLine(
            "Die Abdeckungen sind so zu wählen, dass sie ein Rechteck über die Felder bilden dessen Seitenlängen 2^x Felder betragen.",
            null, 0, null); // 5
    einleitung
        .addCodeLine(
            "Die Abdeckungen können über die Ränder hinaus auf der gegenüberliegenden Seite fortgesetzt werden.",
            null, 0, null); // 6
    einleitung
        .addCodeLine(
            "So sind z.B. die vier Eckfelder direkt benachbart. Folgende Diagramme zeigen einige mögliche Abdeckungen.",
            null, 0, null); // 7

    // first, set the visual properties (somewhat similar to CSS)
    MatrixProperties abdeckungenProps = new MatrixProperties();
    abdeckungenProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.RED); // verbuggt

    TextProperties abdeckungTextProps = new TextProperties();
    abdeckungTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    abdeckungTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    abdeckungTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    String defaultMatrix[][] = { { "", "", "", "" }, { "", "", "", "" },
        { "", "", "", "" }, { "", "", "", "" } };
    int highlight[][][] = {
        { { 1, 3 } },
        { { 1, 3 }, { 2, 3 } },
        { { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 } },
        { { 0, 0 }, { 0, 1 }, { 3, 0 }, { 3, 1 } },
        { { 0, 0 }, { 0, 3 }, { 3, 0 }, { 3, 3 } },
        { { 0, 3 }, { 1, 3 }, { 2, 3 }, { 3, 3 }, { 0, 2 }, { 1, 2 }, { 2, 2 },
            { 3, 2 } },
        { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 1, 1 }, { 1, 2 },
            { 1, 3 }, { 2, 0 }, { 2, 1 }, { 2, 2 }, { 2, 3 }, { 3, 0 },
            { 3, 1 }, { 3, 2 }, { 3, 3 } } };

    PointProperties pointProps = new PointProperties();
    pointProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    lang.newPoint(new Coordinates(50, 230), "abdeckung1_benchmark", null,
        pointProps);
    lang.newPoint(new Coordinates(350, 230), "abdeckung2_benchmark", null,
        pointProps);
    lang.newPoint(new Coordinates(650, 230), "abdeckung3_benchmark", null,
        pointProps);
    lang.newPoint(new Coordinates(950, 230), "abdeckung4_benchmark", null,
        pointProps);
    lang.newPoint(new Coordinates(200, 360), "abdeckung5_benchmark", null,
        pointProps);
    lang.newPoint(new Coordinates(500, 360), "abdeckung6_benchmark", null,
        pointProps);
    lang.newPoint(new Coordinates(800, 360), "abdeckung7_benchmark", null,
        pointProps);

    // Diagrammgitter & Beschriftungen
    for (int i = 1; i < 8; i++) {
      Node[] abdeckung_1_h_node = {
          new Offset(-23, 0, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(112, 0, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_2_h_node = {
          new Offset(0, 28, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(135, 28, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_3_h_node = {
          new Offset(-23, 58, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(112, 58, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_4_h_node = {
          new Offset(0, 86, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(135, 86, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_5_h_node = {
          new Offset(0, 112, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(112, 112, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_1_v_node = {
          new Offset(0, -23, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(0, 112, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_2_v_node = {
          new Offset(28, 0, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(28, 135, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_3_v_node = {
          new Offset(56, -23, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(56, 112, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_4_v_node = {
          new Offset(84, 0, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(84, 135, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] abdeckung_5_v_node = {
          new Offset(112, 0, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(112, 112, "abdeckung" + i + "_benchmark",
              AnimalScript.DIRECTION_N) };
      lang.newPolyline(abdeckung_1_h_node, "abdeckung" + i + "_1_h", null);
      lang.newPolyline(abdeckung_2_h_node, "abdeckung" + i + "_2_h", null);
      lang.newPolyline(abdeckung_3_h_node, "abdeckung" + i + "_3_h", null);
      lang.newPolyline(abdeckung_4_h_node, "abdeckung" + i + "_4_h", null);
      lang.newPolyline(abdeckung_5_h_node, "abdeckung" + i + "_5_h", null);
      lang.newPolyline(abdeckung_1_v_node, "abdeckung" + i + "_1_v", null);
      lang.newPolyline(abdeckung_2_v_node, "abdeckung" + i + "_2_v", null);
      lang.newPolyline(abdeckung_3_v_node, "abdeckung" + i + "_3_v", null);
      lang.newPolyline(abdeckung_4_v_node, "abdeckung" + i + "_4_v", null);
      lang.newPolyline(abdeckung_5_v_node, "abdeckung" + i + "_5_v", null);
      lang.newText(new Offset(20, -30, "abdeckung" + i + "_benchmark",
          AnimalScript.DIRECTION_N), "A", "abdeckung" + i + "_A", null,
          abdeckungTextProps);
      lang.newText(new Offset(115, 42, "abdeckung" + i + "_benchmark",
          AnimalScript.DIRECTION_N), "B", "abdeckung" + i + "_B", null,
          abdeckungTextProps);
      lang.newText(new Offset(48, 110, "abdeckung" + i + "_benchmark",
          AnimalScript.DIRECTION_N), "C", "abdeckung" + i + "_C", null,
          abdeckungTextProps);
      lang.newText(new Offset(-20, 12, "abdeckung" + i + "_benchmark",
          AnimalScript.DIRECTION_N), "D", "abdeckung" + i + "_D", null,
          abdeckungTextProps);
    }

    StringMatrix abdeckung1 = lang.newStringMatrix(new Offset(0, 0,
        "abdeckung1_benchmark", AnimalScript.DIRECTION_NW), defaultMatrix,
        "abdeckung1", null, abdeckungenProps);
    StringMatrix abdeckung2 = lang.newStringMatrix(new Offset(0, 0,
        "abdeckung2_benchmark", AnimalScript.DIRECTION_NW), defaultMatrix,
        "abdeckung2", null, abdeckungenProps);
    StringMatrix abdeckung3 = lang.newStringMatrix(new Offset(0, 0,
        "abdeckung3_benchmark", AnimalScript.DIRECTION_NW), defaultMatrix,
        "abdeckung3", null, abdeckungenProps);
    StringMatrix abdeckung4 = lang.newStringMatrix(new Offset(0, 0,
        "abdeckung4_benchmark", AnimalScript.DIRECTION_NW), defaultMatrix,
        "abdeckung4", null, abdeckungenProps);
    StringMatrix abdeckung5 = lang.newStringMatrix(new Offset(0, 0,
        "abdeckung5_benchmark", AnimalScript.DIRECTION_NW), defaultMatrix,
        "abdeckung5", null, abdeckungenProps);
    StringMatrix abdeckung6 = lang.newStringMatrix(new Offset(0, 0,
        "abdeckung6_benchmark", AnimalScript.DIRECTION_NW), defaultMatrix,
        "abdeckung6", null, abdeckungenProps);
    StringMatrix abdeckung7 = lang.newStringMatrix(new Offset(0, 0,
        "abdeckung7_benchmark", AnimalScript.DIRECTION_NW), defaultMatrix,
        "abdeckung7", null, abdeckungenProps);

    // Highlight
    for (int[] coordinates : highlight[0]) {
      abdeckung1.highlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[1]) {
      abdeckung2.highlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[2]) {
      abdeckung3.highlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[3]) {
      abdeckung4.highlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[4]) {
      abdeckung5.highlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[5]) {
      abdeckung6.highlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[6]) {
      abdeckung7.highlightCell(coordinates[0], coordinates[1], null, null);
    }

    lang.nextStep();

    // Unhighlight
    for (int[] coordinates : highlight[0]) {
      abdeckung1.unhighlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[1]) {
      abdeckung2.unhighlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[2]) {
      abdeckung3.unhighlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[3]) {
      abdeckung4.unhighlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[4]) {
      abdeckung5.unhighlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[5]) {
      abdeckung6.unhighlightElem(coordinates[0], coordinates[1], null, null);
    }
    for (int[] coordinates : highlight[6]) {
      abdeckung7.unhighlightCell(coordinates[0], coordinates[1], null, null);
    }

    lang.hideAllPrimitives();
  }

  /**
   * Konvertiert Werte die über die Grenzen des KV-Diagramms gehen
   * 
   * @param theX
   *          der Wert der konvertiert wird (-1 -> 3 / 4 -> 0)
   * @return konvertierter x Wert
   */
  private int convert(int theX) {
    int x = theX;
    if (x == 4)
      x = 0;
    else if (x == -1)
      x = 3;
    return x;
  }

  /**
   * Initialisiert die Variable "termes" sowie die Inhalte von "frames[][]" mit
   * 0
   */
  private void initialisierung() {
    for (int i = 0; i <= 3; i++)
      for (int j = 0; j <= 3; j++)
        frames[i][j] = 0;
    termes = 0;
  }

}