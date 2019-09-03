package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class DTWAnimation implements Generator {

  protected Language       lang;
  private MatrixProperties matrixProps;
  private TextProperties   textProps, headlineProps, subHeadProps; // ,
                                                                   // varProps;
  private RectProperties   rectProps;
  private ArrayProperties  arrayProps;
  private ArrayMarkerProperties iProps, jProps;
  private SourceCodeProperties  srcProps;
  private SourceCode            src;
  private Timing                defaultTiming;
  private Text                  headline;
  // private Rect title_underline;
  private StringMatrix          the_matrix;
  private IntArray              arr_seq_1, arr_seq_2;
  private Rect                  src_underline;
  private int[]                 input_seq_1, input_seq_2;

  public void init() {

    lang = new AnimalScript("Dynamic Time Warping",
        "The An Binh Nguyen, Nam Truong Le", 640, 480);
    // Activate step control
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    defaultTiming = new TicksTiming(15);

    // create properties with default values

    iProps = new ArrayMarkerProperties();
    jProps = new ArrayMarkerProperties();
    matrixProps = new MatrixProperties();
    arrayProps = new ArrayProperties();
    textProps = new TextProperties();
    headlineProps = new TextProperties();
    subHeadProps = new TextProperties();
    // varProps = new TextProperties();
    srcProps = new SourceCodeProperties();
    rectProps = new RectProperties();

    // Redefine source code properties
    srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    srcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(56,
        122, 255));
    srcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // Redefine matrix properties
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(56, 122,
        255));
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 255,
        255));

    // Redefine array properties
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(233, 240,
        255));
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(
        149, 184, 255));
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(149, 184,
        255));
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 15));

    // array marker properties
    iProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    iProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

    jProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    jProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");

    // Redefine text properties
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    // Redefine headline properties
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    headlineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(56,
        122, 255));

    // Redefine sub headline properties
    subHeadProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 14));
    subHeadProps
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0, 0));

    // Redefine sub headline properties
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(149, 184,
        255));
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(233, 240,
        255));

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    input_seq_1 = (int[]) primitives.get("input_seq_1");
    input_seq_2 = (int[]) primitives.get("input_seq_2");
    srcProps = (SourceCodeProperties) props
        .getPropertiesByName("input_srcProps");
    matrixProps = (MatrixProperties) props
        .getPropertiesByName("input_matrixProps");
    jProps = (ArrayMarkerProperties) props
        .getPropertiesByName("input_j_markerProps");
    arrayProps = (ArrayProperties) props
        .getPropertiesByName("input_arrayProps");
    iProps = (ArrayMarkerProperties) props
        .getPropertiesByName("input_i_markerProps");

    dtw(input_seq_1, input_seq_2);
    lang.finalizeGeneration();
    return lang.toString();
  }

  public void dtw(int[] seq_1, int[] seq_2) {

    // Show headline
    headline = lang.newText(new Coordinates(20, 35),
        "Der Dynamic Time Warping (DTW) - Algorithmus", "headline", null,
        headlineProps);

    // Underline rect
    lang.newRect(new Offset(-5, 2, headline, AnimalScript.DIRECTION_SW),
        new Offset(400, 5, headline, AnimalScript.DIRECTION_SE),
        "title_underline", defaultTiming, rectProps);

    // Show description

    Text desc_1 = lang
        .newText(
            new Coordinates(20, 120),
            "Der 'Dynamic Time Warping' (DTW) - Algorithmus ist ein Algorithmus zum Messen",
            "desc_1", null, textProps);

    Text desc_2 = lang.newText(new Coordinates(20, 155),
        "der Ähnlichkeit zweier sich unterscheidender Listen.", "desc_2", null,
        textProps);

    Text desc_3 = lang
        .newText(
            new Coordinates(20, 200),
            "Er findet Anwendung in den unterschiedlichsten Bereichen der Informatik, ",
            "desc_3", null, textProps);

    Text desc_4 = lang.newText(new Coordinates(20, 235),
        "zum Beispiel: Audio-, Video- und Grafikverarbeitung. Das wohl",
        "desc_4", null, textProps);

    Text desc_5 = lang.newText(new Coordinates(20, 270),
        "bekannteste Gebiet ist die automatische Spracherkennung.", "desc_5",
        null, textProps);

    Text desc_6 = lang.newText(new Coordinates(20, 315),
        "Der Algorithmus kann alles vergleichen, was in lineare Darstellung",
        "desc_6", null, textProps);

    Text desc_7 = lang.newText(new Coordinates(20, 350),
        "gebracht werden kann, für die Animation wurden für das einfachste",
        "desc_7", null, textProps);

    Text desc_8 = lang.newText(new Coordinates(20, 385),
        "Verständnis zwei Integerlisten gewählt.", "desc_8", null, textProps);

    Text desc_9 = lang.newText(new Coordinates(20, 430),
        "Der Algorithmus arbeitet auf einer Matrix und gibt eine Integerzahl",
        "desc_9", null, textProps);

    Text desc_10 = lang.newText(new Coordinates(20, 465),
        "zurück, die als Maß für den Unterschied der Eingabelisten dient.",
        "desc_10", null, textProps);

    lang.nextStep();

    Text srcHeadline = lang.newText(new Coordinates(385, 170), "Pseudocode:",
        "srcHeadline", null, subHeadProps);

    showSourceCode();

    desc_1.hide();
    desc_2.hide();
    desc_3.hide();
    desc_4.hide();
    desc_5.hide();
    desc_6.hide();
    desc_7.hide();
    desc_8.hide();
    desc_9.hide();
    desc_10.hide();

    src.highlight(0);

    // show input
    lang.newText(new Coordinates(35, 130), "seq_1:", "in_seq_1", null,
        subHeadProps);

    arr_seq_1 = lang.newIntArray(new Coordinates(95, 130), seq_1, "arr_seq_1",
        null, arrayProps);

    lang.newText(new Coordinates(35, 220), "seq_2:", "in_seq_2", null,
        subHeadProps);

    arr_seq_2 = lang.newIntArray(new Coordinates(95, 220), seq_2, "arr_seq_2",
        null, arrayProps);

    lang.nextStep();

    // vars

    Text title_vars = lang.newText(new Coordinates(35, 510), "Variablen:",
        "title_vars", null, subHeadProps);

    // Underline vars
    lang.newRect(new Offset(0, 15, title_vars, AnimalScript.DIRECTION_SW),
        new Offset(150, 130, title_vars, AnimalScript.DIRECTION_SE),
        "title_underline", defaultTiming, rectProps);

    int n = seq_1.length;

    lang.newText(new Coordinates(45, 550), "n = " + n, "var_n", null, textProps);

    src.toggleHighlight(0, 1);

    lang.nextStep();

    int m = seq_2.length;

    lang.newText(new Coordinates(45, 570), "m = " + m, "var_m", null, textProps);

    src.toggleHighlight(1, 2);

    lang.nextStep();

    // create matrix
    int[][] dtw_matrix = new int[n + 1][m + 1];

    the_matrix = lang.newStringMatrix(new Coordinates(30, 290),
        new String[n + 1][m + 1], "dtw_matrix", null, matrixProps);

    src.toggleHighlight(2, 3);

    lang.nextStep();

    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "Fragen zur Initialisierung", 1);

    lang.addQuestionGroup(groupInfo);

    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion");
    mcq.setPrompt("Warum wurde das [0][0]-te Element der Matrix mit 0 initialisiert?");
    mcq.addAnswer(
        "Das [0][0]-te Element dient als Startpunkt und hat sonst keine Bedeutung",
        5, "Richtige Antwort!");
    mcq.addAnswer("Der Algorithmus würde sonst in eine Endlosschleife laufen",
        0, "Falsche Antwort!");
    mcq.setGroupID("Fragen zur Initialisierung");
    lang.addMCQuestion(mcq);

    Text currentStep = lang.newText(new Coordinates(385, 120),
        "Setze [0][0]-tes Element auf 0.", "current_step", null, subHeadProps);

    dtw_matrix[0][0] = 0;

    src.toggleHighlight(3, 4);

    the_matrix.put(0, 0, 0 + "", null, defaultTiming);

    lang.nextStep("Initialisierung der Matrix");

    currentStep
        .setText(
            "Initialisiere die Matrix mit unendlich auf den Randstellen ausgehend vom Ursprung.",
            null, defaultTiming);

    src.unhighlight(4);
    for (int i = 0; i < seq_1.length; i++) {
      src.highlight(5);
      lang.nextStep();

      src.unhighlight(5);
      src.highlight(6);
      dtw_matrix[i + 1][0] = Integer.MAX_VALUE;
      the_matrix.put(i + 1, 0, "∞", null, defaultTiming);

      lang.nextStep();
      src.unhighlight(6);
    }

    src.unhighlight(5);
    for (int i = 0; i < seq_2.length; i++) {
      src.highlight(8);
      lang.nextStep();

      src.unhighlight(8);
      src.highlight(9);
      dtw_matrix[0][i + 1] = Integer.MAX_VALUE;
      the_matrix.put(0, i + 1, "∞", null, defaultTiming);

      lang.nextStep();
      src.unhighlight(9);
    }

    // set marker
    ArrayMarker i_marker = lang.newArrayMarker(arr_seq_1, 0, "i", null, iProps);
    ArrayMarker j_marker = lang.newArrayMarker(arr_seq_2, 0, "j", null, jProps);
    j_marker.hide();

    currentStep.setText(
        "Fülle Matrix mit den absoluten Distanzen der Eingabesequenzen.", null,
        defaultTiming);
    lang.nextStep("Füllen der Matrix");

    int i = 0, j = 0;

    QuestionGroupModel groupInfo2 = new QuestionGroupModel(
        "Fragen zur Distanzberechnung", 1);

    lang.addQuestionGroup(groupInfo2);

    for (; i_marker.getPosition() < n; i_marker.increment(null, defaultTiming)) {

      src.highlight(11);
      lang.nextStep();

      src.unhighlight(11);

      j_marker.move(0, null, defaultTiming);

      for (; j_marker.getPosition() < m; j_marker
          .increment(null, defaultTiming)) {

        src.highlight(12);
        j_marker.show();
        lang.nextStep();

        src.toggleHighlight(12, 13);
        int temp_cost = Math.abs(seq_1[i_marker.getPosition()]
            - seq_2[j_marker.getPosition()]);
        dtw_matrix[i_marker.getPosition() + 1][j_marker.getPosition() + 1] = temp_cost;

        if ((i == 1) && (j == 1)) {

          FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
              "fillInBlanksQuestion");

          fibq.setGroupID("Fragen zur Distanzberechnung");
          fibq.setPrompt("Welches Element wird an der Stelle " + "[" + (i + 1)
              + "]" + "[" + (j + 1) + "] in die Matrix eingefügt?");
          fibq.addAnswer(temp_cost + "", 5, "Richtige Antwort!");

          lang.addFIBQuestion(fibq);

          lang.nextStep();

        }

        the_matrix.put(i_marker.getPosition() + 1, j_marker.getPosition() + 1,
            temp_cost + "", null, defaultTiming);

        lang.nextStep();
        src.unhighlight(13);
        j++;
      }
      j = 0;
      i++;
    }

    i_marker.move(1, null, defaultTiming);
    j_marker.hide();

    Text var_min = lang.newText(new Coordinates(45, 590), "min = ", "var_min",
        null, textProps);
    var_min.hide();

    currentStep.setText("Beginn des main loop.", null, defaultTiming);

    lang.nextStep("Beginn des main loop");

    int k = 0, l = 0;

    QuestionGroupModel groupInfo3 = new QuestionGroupModel(
        "Fragen zum Time-Warping", 1);

    lang.addQuestionGroup(groupInfo3);

    for (; i_marker.getPosition() <= n; i_marker.increment(null, defaultTiming)) {
      src.highlight(16);
      lang.nextStep();

      src.unhighlight(16);
      j_marker.move(1, null, defaultTiming);
      j_marker.show();

      for (; j_marker.getPosition() <= m; j_marker.increment(null,
          defaultTiming)) {
        src.highlight(17);
        lang.nextStep();

        currentStep.setText(
            "Bestimme das Minimum der markierten Matrixeinträgen.", null,
            defaultTiming);

        the_matrix.highlightCell(i_marker.getPosition() - 1,
            j_marker.getPosition(), null, defaultTiming);

        the_matrix.highlightCell(i_marker.getPosition(),
            j_marker.getPosition() - 1, null, defaultTiming);

        the_matrix.highlightCell(i_marker.getPosition() - 1,
            j_marker.getPosition() - 1, null, defaultTiming);

        src.unhighlight(17);
        src.highlight(18);
        src.highlight(19);
        src.highlight(20);
        src.highlight(21);

        int min = Math
            .min(
                dtw_matrix[i_marker.getPosition() - 1][j_marker.getPosition()],
                Math.min(dtw_matrix[i_marker.getPosition()][j_marker
                    .getPosition() - 1],
                    dtw_matrix[i_marker.getPosition() - 1][j_marker
                        .getPosition() - 1]));

        if ((k == 1) && (l == 1)) {

          FillInBlanksQuestionModel fibq3 = new FillInBlanksQuestionModel(
              "fillInBlanksQuestion3");

          fibq3.setGroupID("Fragen zum Time-Warping");
          fibq3
              .setPrompt("Wie lautet das Minimum der markierten Matrixeinträge?");
          fibq3.addAnswer(min + "", 5, "Richtige Antwort!");

          lang.addFIBQuestion(fibq3);

          lang.nextStep();

        }

        var_min.show();
        var_min.setText("min = " + min, null, defaultTiming);

        lang.nextStep();
        src.unhighlight(18);
        src.unhighlight(19);
        src.unhighlight(20);
        src.unhighlight(21);

        src.highlight(22);

        the_matrix.unhighlightCell(i_marker.getPosition() - 1,
            j_marker.getPosition(), null, defaultTiming);

        the_matrix.unhighlightCell(i_marker.getPosition(),
            j_marker.getPosition() - 1, null, defaultTiming);

        the_matrix.unhighlightCell(i_marker.getPosition() - 1,
            j_marker.getPosition() - 1, null, defaultTiming);

        the_matrix.highlightCell(i_marker.getPosition(),
            j_marker.getPosition(), null, defaultTiming);

        dtw_matrix[i_marker.getPosition()][j_marker.getPosition()] += min;

        currentStep
            .setText(
                "Summiere das bestimmte Minimum auf den bestehenden Matrixeintrag.",
                null, defaultTiming);

        the_matrix.put(i_marker.getPosition(), j_marker.getPosition(),
            dtw_matrix[i_marker.getPosition()][j_marker.getPosition()] + "",
            null, defaultTiming);

        lang.nextStep();
        src.unhighlight(22);
        the_matrix.unhighlightCell(i_marker.getPosition(),
            j_marker.getPosition(), null, defaultTiming);
        l++;

      }
      l = 0;
      k++;

    }

    src.highlight(25);
    currentStep.setText("Ende des Algorithmus.", null, defaultTiming);

    lang.nextStep();

    src.hide();
    srcHeadline.hide();
    src_underline.hide();

    lang.newText(new Coordinates(385, 160),
        "Die Distanz nach dem DTW-Algorithmus zwischen Sequenz 1", "end_1",
        null, textProps);

    lang.newText(new Coordinates(385, 180), "und Sequenz 2 beträgt:", "end_2",
        null, textProps);

    lang.newText(new Coordinates(385, 200), "dtw_matrix[n][m]: <"
        + dtw_matrix[n][m] + ">", "end_3", null, subHeadProps);

    lang.newText(new Coordinates(385, 250),
        "Die Laufzeit des Dynamic Time Warping - Algorithmus beträgt O(n²).",
        "end_4", null, subHeadProps);

    lang.finalizeGeneration();
  }

  public int min(int a, int b) {

    if (b < a)
      return b;
    else
      return a;
  }

  public void showSourceCode() {

    // Create the source code entity
    src = lang.newSourceCode(new Coordinates(400, 210), "sourceCode", null,
        srcProps);

    // Underline rect
    src_underline = lang.newRect(new Coordinates(385, 208), new Coordinates(
        805, 708), "src_underline", defaultTiming, rectProps);

    // Add code lines
    src.addCodeLine("int dtw(int[] seq_1, int[] seq_2) {", null, 0, null);

    src.addCodeLine("int n = seq_1.length;", null, 1, null);
    src.addCodeLine("int m = seq_2.length;", null, 1, null);
    src.addCodeLine("int[][] dtw_matrix = new int[n + 1][m + 1];", null, 1,
        null);
    src.addCodeLine("dtw_matrix[0][0] = 0;", null, 1, null);

    src.addCodeLine("for (int i = 0; i < n; i++) {", null, 1, null);

    src.addCodeLine("dtw_matrix[i + 1][0] = Integer.MAX_VALUE;", null, 2, null);

    src.addCodeLine("}", null, 1, null);

    src.addCodeLine("for (int k = 0; k < m; k++) {", null, 1, null);

    src.addCodeLine("dtw_matrix[0][k + 1] = Integer.MAX_VALUE;", null, 2, null);

    src.addCodeLine("}", null, 1, null);

    src.addCodeLine("for (int i = 0; i < n; i++) {", null, 1, null);

    src.addCodeLine("for (int 0 = 1; j < m; j++) {", null, 2, null);

    src.addCodeLine("dtw[i + 1][j + 1] = Math.abs(seq_1[i] - seq_2[j]);", null,
        3, null);

    src.addCodeLine("}", null, 2, null);

    src.addCodeLine("}", null, 1, null);

    src.addCodeLine("for (int i = 1; i <= n; i++) {", null, 1, null);

    src.addCodeLine("for (int j = 1; j <= m; j++) {", null, 2, null);

    src.addCodeLine("int min = Math.min(", null, 3, null);

    src.addCodeLine("dtw_matrix[i - 1][j], Math.min(", null, 7, null);

    src.addCodeLine("dtw_matrix[i][j - 1], ", null, 11, null);

    src.addCodeLine("dtw_matrix[i - 1][j - 1]));", null, 11, null);

    src.addCodeLine("dtw_matric[i][j] += cost;", null, 3, null);

    src.addCodeLine("}", null, 2, null);

    src.addCodeLine("}", null, 1, null);

    src.addCodeLine("return dtw_matrix[n][m];", null, 1, null);

    src.addCodeLine("}", null, 0, null);

  }

  public String getName() {
    return "Dynamic Time Warping  - Algorithmus";
  }

  public String getAlgorithmName() {
    return "Dynamic Time Warping";
  }

  public String getAnimationAuthor() {
    return "The An Binh Nguyen, Nam Truong Le";
  }

  public String getDescription() {
    return "Der 'Dynamic Time Warping' (DTW) - Algorithmus ist ein Algorithmus zum Messen der"
        + "\n"
        + "Ähnlichkeit zweier sich unterscheidender Listen."
        + "\n"
        + "\n"
        + "Er findet Anwendung in den unterschiedlichsten Bereichen der Informatik zum Beispiel:"
        + "\n"
        + "Audio-, Video- und Grafikverarbeitung. Das wohl bekannteste Gebiet ist die automatische"
        + "\n"
        + "Spracherkennung."
        + "\n"
        + "\n"
        + "Der Algorithmus kann alles vergleichen, was in lineare Darstellung gebracht werden kann,"
        + "\n"
        + "für die Animation wurden für das einfachste Verständnis zwei Integerlisten gewählt."
        + "\n"
        + "\n"
        + "Der Algorithmus arbeitet auf einer Matrix und gibt eine Integerzahl zurück, die als Maß für"
        + "\n" + "den Unterschied der Eingabelisten dient.";
  }

  public String getCodeExample() {
    return "public static int distance(int[] seq_1, int[] seq_2) {"
        + "\n"
        + "	int n = seq_1.length;"
        + "\n"
        + "	int m = seq_2.length;"
        + "\n"
        + "	int[][] dtw = new int[n + 1][m + 1];"
        + "\n"
        + "	dtw[0][0] = 0;"
        + "\n"
        + "	"
        + "\n"
        + "	for (int i = 0; i < seq_1.length; i++) {"
        + "\n"
        + "		dtw[i + 1][0] = Integer.MAX_VALUE;"
        + "\n"
        + "	}"
        + "\n"
        + "\n"
        + "	for (int i = 0; i < seq_2.length; i++) {"
        + "\n"
        + "		dtw[0][i + 1] = Integer.MAX_VALUE;"
        + "\n"
        + "	}"
        + "\n"
        + "	"
        + "\n"
        + "	for (int i = 0; i < seq_1.length; i++) {	"
        + "\n"
        + "		for (int j = 0; j < seq_2.length; j++) {	"
        + "\n"
        + "			dtw[i + 1][j + 1] = Math.abs(seq_1[i] - seq_2[j]);"
        + "\n"
        + "		}"
        + "\n"
        + "	}"
        + "\n"
        + "\n"
        + "	for (int i = 1; i <= n; i++) {"
        + "\n"
        + "		 for (int j = 1; j <= m; j++) {"
        + "\n"
        + "			 dtw[i][j] += Math.min(dtw[i - 1][j], Math.min(dtw[i][j - 1], dtw[i - 1][j - 1]));"
        + "\n" + "		 }" + "\n" + "	}" + "\n" + "	" + "\n"
        + "   return dtw[n][m];" + "\n" + "}";
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

}
