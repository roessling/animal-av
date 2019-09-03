package generators.maths;

import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import animal.graphics.meta.PolygonalShape;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;

public class GaussLegendreGenerator implements ValidatingGenerator {

    // algorithm fields
    private double a_n, b_n, t_n, p_n, pi;
    private int steps;

    // animation fields
    private Language language;
    private Text header, endText;
    private Text[] intro, info;
    private StringArray pi1, pi2, pi3, pi4, pi5, pi6, pi7, pi8;
    private TextProperties headerProperties, introProperties, infoProperties;
    private ArrayProperties piProperties;
    private SourceCodeProperties codeProperties;
    private SourceCode codeCalculatePi, codeNextStep;
    private Variables variableTable;

    private final String VAR_A = "a";
    private final String VAR_B = "b";
    private final String VAR_T = "t";
    private final String VAR_P = "p";
    private final String VAR_PI = "pi";

    public GaussLegendreGenerator() {
    }

    public GaussLegendreGenerator(Language lang) {
        language = lang;
        language.setStepMode(true);
    }

    private static final String DESCRIPTION = "" +
            "Der Algorithmus von Gauss Legendre ist ein Algorithmus " +
            "zur Annährung der Kreiszahl Pi. Hierfür werden Rekursiv 4 Werte berechnet: a, b, t und p. " +
            "Die Werte werden zunächst mit a = 1, b = 1 / sqrt(2), t = 1/4 und p = 1 initialisiert. " +
            "Dann werden sie rekursiv mit Hilfe folgender Formeln berechnet:\n" +
            "a_n+1 = (a_n + b_n) / 2;\n" +
            "b_n+1 = sqrt(a_n + b_n);\n" +
            "t_n+1 = t_n - p_n * (a_n - a_n+1)^2;\n" +
            "p_n+1 = 2 * p_n.\n" +
            "Nachdem man die gewünschte Anzahl an Iterationen durchgeführt hat, kann man Pi mit der folgenden " +
            "Formel annähren:\n" +
            "pi1 = ((a_n+1 + b_n+1)^2) / (4 * t_n+1);\n" +
            "Die Anzahl an korrekten Stellen verdoppelt sich ungefähr mit jedem Rekursionsschritt.\n\n" +
            "Da float nicht viele Nachkommastellen unterstützt, ist es sinnvoll, den Gauss-Legendre Algorithmus mit " +
            "Big Decimal zu implementieren. Da float aber lesbarer und dadurch einfacher verständlich ist, " +
            "wird der Beispielcode mit float gegeben.";

    private final String[] DESCRIPTION_LINES = {
            "Der Algorithmus von Gauss Legendre ist ein Algorithmus",
            "zur Annährung der Kreiszahl Pi. Hierfür werden Rekursiv 4 Werte berechnet: a, b, t und p.",
            "Die Werte werden zunächst mit a = 1, b = 1 / sqrt(2), t = 1/4 und p = 1 initialisiert.",
            "Dann werden sie rekursiv mit Hilfe folgender Formeln berechnet:",
            "",
            "a_n+1 = (a_n + b_n) / 2;",
            "",
            "b_n+1 = sqrt(a_n + b_n);",
            "",
            "t_n+1 = t_n - p_n * (a_n - a_n+1)^2;",
            "",
            "p_n+1 = 2 * p_n.",
            "",
            "Nachdem man die gewünschte Anzahl an Iterationen durchgeführt hat, kann man Pi mit der folgenden",
            "Formel annähren:",
            "pi1 = ((a_n+1 + b_n+1)^2) / (4 * t_n+1);",
            "",
            "Die Anzahl an korrekten Stellen verdoppelt sich ungefähr mit jedem Rekursionsschritt.",
            "",
            "",
            "Da float nicht viele Nachkommastellen unterstützt, ist es sinnvoll, den Gauss-Legendre Algorithmus mit",
            "Big Decimal zu implementieren. Da float aber lesbarer und dadurch einfacher verständlich ist,",
            "wird der Beispielcode mit float gegeben."
    };

    private static final String SOURCE_CODE = "" +
            "private float a;\n" +
            "private float b;\n" +
            "private float t;\n" +
            "private float p;\n" +
            "\n" +
            "public float calculatePi(int steps) {\n" +
            "   a = 1;\n" +
            "   b = 1 / sqrt(2);\n" +
            "   t = 1 / 4;\n" +
            "   p = 1;\n" +
            "   for (int i = 0; i < steps; i++)" +
            "       calculateNextStep();" +
            "   return ((a + b)^2) / (4 * t);\n" +
            "}\n" +
            "\n" +
            "private void calculateNextStep() {\n" +
            "   float a1 = ( a + b ) / 2;\n" +
            "   float b1 = sqrt( a + b );\n" +
            "   float t1 = t - p * ( a - a1 ) ^ 2;\n" +
            "   float p1 = 2 * p;\n" +
            "   a = a1;\n" +
            "   b = b1;\n" +
            "   t = t1;\n" +
            "   p = p1;\n" +
            "}";

    private final String PI = "3." +
            "141592653589793238462643383279" +
            "502884197169399375105820974944" +
            "592307816406286208998628034825" +
            "342117067982148086513282306647" +
            "093844609550582231725359408128" +
            "481117450284102701938521105559" +
            "644622948954930381964428810975" +
            "665933446128475648233786783165" +
            "271201909145648566923460348610" +
            "454326648213393607260249141273" +
            "724587006606315588174881520920" +
            "962829254091715364367892590360";

    private final String[] infoArray = {
            "",
            "",
            "",
            "",
            "",
            "An diesem Punkt reicht die Genauigkeit von Double nicht mehr aus.",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "Wie man sieht ist die Berechnung von Pi schon mit wenigen Schritten sehr genau."};

    private void start(int steps) {
        setupIntro();

        showHeader();
        showAndHideIntro();

        setupAlgorithm();

        gaussLegendre(steps);

        language.nextStep("Ende");

        endText = language.newText(new Coordinates(20, 500), "Am Ende des Algorithmus wird Pi berechnet und zurück gegeben", "endText", null, infoProperties);

        codeNextStep.unhighlight(5);
        codeNextStep.unhighlight(6);
        codeNextStep.unhighlight(7);
        codeNextStep.unhighlight(8);

        codeCalculatePi.unhighlight(5);
        codeCalculatePi.unhighlight(6);
        codeCalculatePi.highlight(7);
    }

    private void setupIntro() {
        setupHeaderProperties();
        setupIntroProperties();
    }

    private void setupHeaderProperties() {
        headerProperties = new TextProperties();
        headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
        headerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    }

    private void setupIntroProperties() {
        introProperties = new TextProperties();
        introProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
    }

    private void showHeader() {
        header = language.newText(new Coordinates(20, 20), "Gauss-Legendre", "header", null, headerProperties);
    }

    private void showAndHideIntro() {
        intro = stringArrayToTextArray(DESCRIPTION_LINES, "introLines", new Coordinates(20, 60), introProperties, 18);
        language.nextStep("intro");
        for (Text line : intro) {
            line.hide();
        }
    }

    private Text[] stringArrayToTextArray(String[] text, String name, Coordinates coordinates, TextProperties properties, int offset) {
        Text[] result = new Text[text.length];

        for (int i = 0; i < text.length; i++) {
            result[i] = language.newText(new Coordinates(coordinates.getX(), coordinates.getY() + offset * i), text[i], name, null, properties);
        }

        return result;
    }

    private void setupAlgorithm() {
        setupCodeProperties();
        setupVariables();
        setupPi();
        setupCodeCalculatePi();
        setupCodeNextStep();
        setupInformation();
    }

    private void setupCodeProperties() {
        codeProperties = new SourceCodeProperties();
        codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Verdana", Font.BOLD, 12));
        codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(0xfa, 0x63, 0x05));
        codeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    }

    private void setupVariables() {
        variableTable = language.newVariables();

        variableTable.declare("double", VAR_A);
        variableTable.declare("double", VAR_B);
        variableTable.declare("double", VAR_P);
        variableTable.declare("double", VAR_T);
        variableTable.declare("double", VAR_PI);
    }

    private void setupPi() {
        setupPiProperties();
        pi1 = stringToStringArray(PI.substring(0, 46), new Coordinates(20, 80), "piArray1", piProperties);
        pi2 = stringToStringArray(PI.substring(46, 90), new Coordinates(42, 100), "piArray2", piProperties);
        pi3 = stringToStringArray(PI.substring(90, 134), new Coordinates(42, 120), "piArray3", piProperties);
        pi4 = stringToStringArray(PI.substring(134, 178), new Coordinates(42, 140), "piArray4", piProperties);
        pi5 = stringToStringArray(PI.substring(178, 222), new Coordinates(42, 160), "piArray5", piProperties);
        pi6 = stringToStringArray(PI.substring(222, 266), new Coordinates(42, 180), "piArray6", piProperties);
        pi7 = stringToStringArray(PI.substring(266, 310), new Coordinates(42, 200), "piArray7", piProperties);
        pi8 = stringToStringArray(PI.substring(310, 354), new Coordinates(42, 220), "piArray8", piProperties);
    }

    private void setupPiProperties() {
        piProperties = new ArrayProperties();
        piProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
        piProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        piProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        piProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        piProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(0x00, 0xe6, 0x6b));
    }

    private StringArray stringToStringArray(String string, Coordinates coordinates, String name, ArrayProperties properties) {
        int length = string.length();
        String[] result = new String[length];

        for (int i = 0; i < length; i++) {
            result[i] = string.substring(i, i + 1);
        }

        return language.newStringArray(coordinates, result, name, null, properties);
    }

    private void setupCodeCalculatePi() {
        codeCalculatePi = language.newSourceCode(new Coordinates(20, 250), "codeCalculatePi", null, codeProperties);

        codeCalculatePi.addCodeLine("public float calculatePi(int steps)", null, 0, null);
        codeCalculatePi.addCodeLine("a = 1;", null, 1, null);
        codeCalculatePi.addCodeLine("b = 1 / sqrt( 2 );", null, 1, null);
        codeCalculatePi.addCodeLine("t = 1 / 4;", null, 1, null);
        codeCalculatePi.addCodeLine("p = 1;", null, 1, null);
        codeCalculatePi.addCodeLine("for ( int i = 0; i < steps; i++)", null, 1, null);
        codeCalculatePi.addCodeLine("calculateNextStep( );", null, 2, null);
        codeCalculatePi.addCodeLine("return ( ( a + b ) ^ 2 ) / ( 4 * t );", null, 1, null);
        codeCalculatePi.addCodeLine("}", null, 0, null);
    }

    private void setupCodeNextStep() {
        codeNextStep = language.newSourceCode(new Coordinates(300, 250), "codeNextStep", null, codeProperties);

        codeNextStep.addCodeLine("private void calculateNextStep( ) {", null, 0, null);
        codeNextStep.addCodeLine("float a1 = ( a + b ) / 2;", null, 1, null);
        codeNextStep.addCodeLine("float b1 = sqrt( a + b );", null, 1, null);
        codeNextStep.addCodeLine("float t1 = t - p * ( a - a1 ) ^ 2;", null, 1, null);
        codeNextStep.addCodeLine("float p1 = 2 * p;", null, 1, null);
        codeNextStep.addCodeLine("a = a1;", null, 1, null);
        codeNextStep.addCodeLine("b = b1;", null, 1, null);
        codeNextStep.addCodeLine("t = t1;", null, 1, null);
        codeNextStep.addCodeLine("p = p1;", null, 1, null);
        codeNextStep.addCodeLine("}", null, 0, null);
    }

    private void setupInformation() {
        infoProperties = new TextProperties();
        infoProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
        info = new Text[14];
    }

    private void gaussLegendre(int steps) {
        initializeValues();

        startCalculatingSteps(steps);
    }

    private void initializeValues() {
        language.nextStep();

        codeCalculatePi.highlight(1);
        a_n = 1;
        variableTable.set(VAR_A, String.valueOf(a_n));

        codeCalculatePi.highlight(2);
        b_n = 1 / Math.sqrt(2);
        variableTable.set(VAR_B, String.valueOf(b_n));

        codeCalculatePi.highlight(3);
        t_n = 0.25;
        variableTable.set(VAR_T, String.valueOf(t_n));

        codeCalculatePi.highlight(4);
        p_n = 1;
        variableTable.set(VAR_P, String.valueOf(p_n));

        pi = 0;
        variableTable.set(VAR_PI, String.valueOf(pi));

        language.nextStep();

        for (int i = 1; i <= 4; i++) {
            codeCalculatePi.unhighlight(i);
        }
    }

    private void startCalculatingSteps(int steps) {
        codeCalculatePi.highlight(5);
        codeCalculatePi.highlight(6);
        for (int i = 0; i < steps * 2; i++) {
            language.nextStep();
            highlightCalculateNextStepsCode(i % 2);
            if (i % 2 == 1) {
                nextStep();
                highlightPiNumbers(i);
            }
            showInformation(i);
        }
    }

    private void highlightCalculateNextStepsCode(int i) {
        switch (i) {
            case 0:
                codeNextStep.unhighlight(5);
                codeNextStep.unhighlight(6);
                codeNextStep.unhighlight(7);
                codeNextStep.unhighlight(8);
                codeNextStep.highlight(1);
                codeNextStep.highlight(2);
                codeNextStep.highlight(3);
                codeNextStep.highlight(4);
                break;
            case 1:
                codeNextStep.unhighlight(1);
                codeNextStep.unhighlight(2);
                codeNextStep.unhighlight(3);
                codeNextStep.unhighlight(4);
                codeNextStep.highlight(5);
                codeNextStep.highlight(6);
                codeNextStep.highlight(7);
                codeNextStep.highlight(8);
                break;
            default:
                break;
        }
    }

    private void nextStep() {
        double aNew = (a_n + b_n) / 2;                      // a_n+1 = (a_n + b_n) / 2
        double bNew = Math.sqrt(a_n * b_n);                 // b_n+1 = sqrt(a_n * b_b)
        double tNew = t_n - (p_n * Math.pow((a_n - aNew), 2));  // t_n+1 = t_n - p_n * (a_n - a_n+1)^2
        double pNew = 2 * p_n;                               // p_n+1 = 2 * p_n

        a_n = aNew;
        variableTable.set(VAR_A, String.valueOf(a_n));
        b_n = bNew;
        variableTable.set(VAR_B, String.valueOf(b_n));
        t_n = tNew;
        variableTable.set(VAR_T, String.valueOf(t_n));
        p_n = pNew;
        variableTable.set(VAR_P, String.valueOf(p_n));

        pi = calculatePi(a_n, b_n, t_n);
        variableTable.set(VAR_PI, String.valueOf(pi));
    }

    private double calculatePi(double a, double b, double t) {
        double numerator = Math.pow((a + b), 2);
        double denumerator = 4 * t;

        return numerator / denumerator;                 // pi = (a_n+1 + b_n+1)^2 / (4 * t_n+1)
    }

    private void highlightPiNumbers(int i) {
        switch (i) {
            case 1:
                pi1.highlightCell(0, 3, null, null);
                break;
            case 3:
                pi1.highlightCell(4, 8, null, null);
                break;
            case 5:
                pi1.highlightCell(9, 19, null, null);
                break;
            case 7:
                pi1.highlightCell(20, 41, null, null);
                break;
            case 9:
                pi1.highlightCell(42, 45, null, null);
                pi2.highlightCell(0, 38, null, null);
                break;
            case 11:
                pi2.highlightCell(39, 43, null, null);
                pi3.highlightCell(0, 43, null, null);
                pi4.highlightCell(0, 37, null, null);
                break;
            case 13:
                pi4.highlightCell(38, 43, null, null);
                pi5.highlightCell(0, 43, null, null);
                pi6.highlightCell(0, 43, null, null);
                pi7.highlightCell(0, 43, null, null);
                pi8.highlightCell(0, 35, null, null);
        }
    }

    private void showInformation(int i) {
        if (i > 0) {
            info[i - 1].hide();
        }
        if (i < 15) {
            System.out.println(infoArray[i]);
            info[i] = language.newText(new Coordinates(20, 460), infoArray[i], "info", null, infoProperties);
        }
    }

    public void init() {
        language = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, this.getAlgorithmName(),
                this.getAnimationAuthor(), 800, 600);
        language.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) {
        a_n = (Double) primitives.get("a");
        b_n = (Double) primitives.get("b");
        t_n = (Double) primitives.get("t");
        p_n = (Double) primitives.get("p");
        pi = (Double) primitives.get("pi");
        steps = (Integer) primitives.get("steps");
        piProperties = (ArrayProperties) properties.getPropertiesByName("piProperties");
        codeProperties = (SourceCodeProperties) properties.getPropertiesByName("codeProperties");
        infoProperties = (TextProperties) properties.getPropertiesByName("infoProperties");
        headerProperties = (TextProperties) properties.getPropertiesByName("headerProperties");
        introProperties = (TextProperties) properties.getPropertiesByName("introProperties");

//        init();
        start(steps);

        return language.toString();
    }

    public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
            throws IllegalArgumentException {
        steps = (Integer) primitives.get("steps");

        if (steps < 1) {
            throw new IllegalArgumentException("You have to make at least one step!");
        } else if (steps > 7) {
            throw new IllegalArgumentException("Please make for visualization purposes at least 1 step or at most 7 steps!");
        } else {
            return true;
        }

    }

    public static void main(String[] args) {
        Language language = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Gauss-Legendre", "Fatih Inan & Yildiz Kasimay", 800, 600);
        GaussLegendreGenerator gaussLegendre = new GaussLegendreGenerator(language);
        int steps = 7;
        gaussLegendre.start(steps);
        System.out.println(language);
    }

    public String getName() {
        return "Gauss-Legendre";
    }

    public String getAlgorithmName() {
        return "Gauss-Legendre";
    }

    public String getAnimationAuthor() {
        return "Fatih Inan & Yildiz Kasimay";
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getCodeExample() {
        return SOURCE_CODE;
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
}
