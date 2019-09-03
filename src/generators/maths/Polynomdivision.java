/*
 * Polynomdivision.java
 * Mai Ly Tran, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;

public class Polynomdivision implements ValidatingGenerator {
    private Language lang;
    private Text header;
    private TextProperties textProps;
    private int width;
    private int[] deminator;
    private int[] numerator;

    public static void main(String[] args) {
        // Create a new language object for generating animation code
        // this requires type, name, author, screen width, screen height
        Polynomdivision s = new Polynomdivision();
        s.init();
        int[] divident = {2, 0, -2, -4};
        int[] divisor = {1, -1};
        Hashtable<String, Object> prims = new Hashtable<String, Object>();
        prims.put("Polynom-Koeffizienten", divident);
        prims.put("Nullstelle", divisor);
        AnimationPropertiesContainer t = new AnimationPropertiesContainer();
        //s.generate(t, prims);
        System.out.print(s.generate(t, prims));
    }

    public void init() {
        lang = new AnimalScript("Polynomdivision", "Mai Ly Tran", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        numerator = (int[]) primitives.get("Polynom-Koeffizienten");
        deminator = (int[]) primitives.get("Nullstelle");
        getQuotient(numerator, deminator);
        this.lang.finalizeGeneration();
        return lang.toString();
    }

    public boolean validateInput(AnimationPropertiesContainer
                                         props, Hashtable<String, Object>
                                         primitives) {
        numerator = (int[]) primitives.get("Polynom-Koeffizienten");
        deminator = (int[]) primitives.get("Nullstelle");

        return numerator.length != 0 && deminator.length == 2;
    }

    public void getQuotient(int[] num, int[] dem) {
        // Header
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        header = lang.newText(new Coordinates(20, 30), "Polynomdivision",
                "header", null, headerProps);

        // Source Code Settings
        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, 12));

        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        // DescCode
        SourceCode desc = lang.newSourceCode(new Coordinates(25, 80), "sourceCode",
                null, scProps);

        desc.addCodeLine("Bei der Polynomdivision wird ein Polynom durch eine seiner Nullstellen geteilt. Dies geschieht wie folgt:", null, 0, null);
        desc.addCodeLine("", null, 0, null);
        desc.addCodeLine("1. Es wie bei der schriftlichen Division zuerst mit dem Glied mit dem", null, 0, null);
        desc.addCodeLine("höchsten Grad gestartet und teilt dieses durch die Nullstelle.", null, 0, null);
        desc.addCodeLine("2. Das Ergebnis davon schreibt man auf die rechte Seite.", null, 0, null);
        desc.addCodeLine("3. Nun rechnet man wieder zurück und schreibt dies unter das Glied in die nächste Zeile.", null, 0, null);
        desc.addCodeLine("Das Glied und die Rückrechnung werden voneinander abgezogen.", null, 0, null);
        desc.addCodeLine("4. Das Ergebnis davon kommt in die nächste Zeile und man schreibt zum Rest das nächste Polynomglied hinzu.", null, 0, null);
        desc.addCodeLine("5. Nun wiederholt sich dieses Schema für diese Reste, bis man kein weiteres Glied mehr runterziehen kann", null, 0, null);
        desc.addCodeLine("und als Rest 0 rauskommt.", null, 0, null);
        desc.addCodeLine("", null, 0, null);
        desc.addCodeLine("Das Endergebnis ist ein Polynom eines niedrigeren Grades, welches durch", null, 0, null);
        desc.addCodeLine("Multiplikation mit der Nullstelle wieder das Anfangspolynom ergeben sollte.", null, 0, null);

        lang.nextStep("Initialisierung");
        desc.hide();

        // SourceCode
        SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
                null, scProps);

        sc.addCodeLine("public int[] getQuotient(int[] num, int[] dem){", null, 0, null);
        sc.addCodeLine("int degN = num.length - 1;", null, 1, null);
        sc.addCodeLine("int degD = dem.length - 1;", null, 1, null);
        sc.addCodeLine("int[] result = new int[degN];", null, 1, null);
        sc.addCodeLine("for(int i = 0; i < degN - degD + 1; i++){", null, 1, null); // 4
        sc.addCodeLine("result[i] = num[i] / dem[0];", null, 2, null);
        sc.addCodeLine("for (int j = 0; j < dem.length; j++)", null, 2, null);
        sc.addCodeLine("num[i+j] -= dem[j] * result[i];", null, 3, null);
        sc.addCodeLine("}", null, 1, null);
        sc.addCodeLine("return result;", null, 1, null);
        sc.addCodeLine("}", null, 0, null);

        // Matrix Properties
        MatrixProperties matrixProps = new MatrixProperties();
        matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                Color.YELLOW);

        String[][] matrixValues = createMatrix(num, dem);

        // Matrix
        StringMatrix max = lang.newStringMatrix(new Coordinates(680, 100),
                matrixValues, "stringMatrix", null, matrixProps);

        // Text Properties
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.MONOSPACED, Font.PLAIN, 14));

        lang.newText(new Coordinates(100, 380),
                "Rechenschritt:", "r", null, textProps);

        lang.nextStep();
        divide(max, num, dem, sc);
    }

    private void divide(StringMatrix max, int[] num, int[] dem, SourceCode sc) {
        sc.highlight(0, 0, false);
        lang.nextStep();
        sc.toggleHighlight(0, 1);
        lang.nextStep();
        sc.toggleHighlight(1, 2);
        lang.nextStep();
        sc.toggleHighlight(2, 3);
        lang.nextStep();
        sc.toggleHighlight(3, 4); // 4 = for
        // Frage
        MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel("polyChoice1");
        mc.setPrompt("Was wird nun als nächstes gerechnet?");
        mc.addAnswer(getPart(num[0], num.length-1) + " wird gleich durch " + getPolynom(dem) + "geteilt.", 0,
                "Falsch, es wird zuerst nur durch x geteilt.");
        mc.addAnswer(getPart(num[0], num.length-1) + " wird erstmal nur durch x geteilt.", 1,
                "Korrekt.");
        lang.addMCQuestion(mc);
        lang.nextStep("Teilen durch die Nullstelle");
        int[] res = new int[num.length];
        int height = 0;
        int offset = 1;
        int lineStart = 700;
        int lineHeight = 155;
        width++;
        for (int i = 0; i < num.length - 1; i++) {
            sc.toggleHighlight(4, 5);
            // result[i] = num[i] / dem[0];
            int deg = num.length - 2 - i; // deg of result
            max.highlightCell(height, offset, null, null);
            max.highlightCell(0, num.length * 2 + 1, null, null); // Nullstelle x
            res[i] = num[i] / dem[0];

            // Ergebnis
            if (i == 0)
                max.put(0, width, getPart(res[i], deg), null, null);
            else {
                max.put(0, width, getSign(res[i]), null, null);
                max.put(0, ++width, getPart(Math.abs(res[i]), deg), null, null);
            }
            width++;
            Text step = lang.newText(new Coordinates(100, 400),
                    calcStep(" : ", new int[]{num[i], dem[0], res[i]}, new int[]{deg + 1, 1, deg}),
                    "step", null, textProps);
            lang.nextStep();

            // for (int j = 0; j < dem.length; j++)
            sc.toggleHighlight(5, 6);
            step.hide();
            // nur einmal, um nicht zu nerven
            if (i == 0) {
                mc = new MultipleChoiceQuestionModel("polyChoice2");
                mc.setPrompt("Was geschieht als nächstes?");
                mc.addAnswer("Ich multipliziere die Nullstelle mit meinem letzten Ergebnis " +
                        "(" + getPart(res[i], deg + 1) + ") und ziehe das dann vom Polynom ab.", 1, "Korrekt.");
                mc.addAnswer("Ich multipliziere die Nullstelle mit meinem letzten Ergebnis " +
                        "(" + getPart(res[i], deg + 1) + ") und addiere es dem Polynom hinzu.", 0,
                        "Falsch, es wird vom Polynom abgezogen.");
                mc.addAnswer("Ich multipliziere mein letztes Ergebnis " + "(" + getPart(res[i], deg + 1) + ") mit dem Polynom.", 0,
                        "Falsch, das Ergebnis wird mit der Nullstelle multipliziert und dann vom Polynom abgezogen.");
                mc.addAnswer("Ich ziehe mein letztes Ergebnis " +
                        "(" + getPart(res[i], deg + 1) + "vom Polynom ab.", 0,
                        "Falsch, zuerst wird das Ergebnis mit der Nullstelle multipliziert..");
                lang.addMCQuestion(mc);
            }
            lang.nextStep();

            // num[i+j] -= dem[j] * result[i];
            sc.toggleHighlight(6, 7);
            height++;
            max.put(height, offset - 1, "-", null, null);
            max.put(height, offset, "(" + getPart(res[i], deg + 1), null, null);
            // Rechenschritte
            step = lang.newText(new Coordinates(100, 400),
                    calcStep(" * ", new int[]{res[i], 1, num[i]}, new int[]{deg, 1, deg + 1}),
                    "step", null, textProps);
            Text step2 = lang.newText(new Coordinates(100, 420),
                    calcStep(" - ", new int[]{num[i], res[i], num[i] - res[i]}, new int[]{deg + 1, deg + 1, 0}),
                    "step2", null, textProps);
            // Linie
            lang.newPolyline(new Coordinates[]{new Coordinates(lineStart, lineHeight),
                    new Coordinates(lineStart + (deg != 0? 85 : 70), lineHeight)},"line", null);
            lineStart += 53;
            lineHeight += 52;
            lang.nextStep();

            step.hide();
            step2.hide();
            sc.toggleHighlight(7, 6);
            lang.nextStep();

            sc.toggleHighlight(6, 7);
            max.unhighlightCell(0, num.length * 2 + 1, null, null);
            max.highlightCell(0, num.length * 2 + 2, null, null);
            max.highlightCell(0, num.length * 2 + 3, null, null);
            // über Strich
            int temp = res[i] * dem[1];
            max.put(height, offset + 1, getSign(temp), null, null);
            max.put(height, offset + 2, getPart(Math.abs(temp), deg) + ")", null, null);
            height++;
            // unter Strich
            max.put(height, offset + 2, getPart(num[i + 1] - temp, deg), null, null);
            // Rechenschritte
            step = lang.newText(new Coordinates(100, 400),
                    calcStep(" * ", new int[]{res[i], dem[1], temp}, new int[]{deg, 0, deg}),
                    "step", null, textProps);
            step2 = lang.newText(new Coordinates(100, 420),
                    calcStep(" - ", new int[]{num[i + 1], temp, num[i + 1] - temp}, new int[]{deg, deg, deg}),
                    "step2", null, textProps);
            num[i + 1] -= temp;
            lang.nextStep();

            max.unhighlightCell(0, num.length * 2 + 2, null, null);
            max.unhighlightCell(0, num.length * 2 + 3, null, null);
            max.unhighlightCell(height - 2, offset, null, null);
            step.hide();
            step2.hide();
            // nächstes Polynomglied runterziehen
            if (i + 2 < num.length) {
                sc.toggleHighlight(7, 4);
                max.put(height, offset + 3, getSign(num[i + 2]), null, null);
                max.put(height, offset + 4, getPart(Math.abs(num[i + 2]), deg - 1), null, null);
                offset += 2;
                lang.nextStep();
            }
            else{
                sc.toggleHighlight(7,9);
                mc = new MultipleChoiceQuestionModel("polyChoice3");
                mc.setPrompt("Was bedeutet es, wenn ein Rest übrig bleibt?");
                mc.addAnswer(getPolynom(deminator) + " ist keine Nullstelle.", 1,
                        "Korrekt, bei einer Division durch die Nullstelle würde es keinen Rest geben.");
                mc.addAnswer("Es bleibt am Ende immer ein Rest übrig.", 0,
                        "Falsch, es bleibt nur ein Rest übrig, wenn der Divisor keine Nullstelle ist.");
                mc.addAnswer("Der Rest ist ein Binomialfaktor des Polynoms", 0,
                        "Falsch, es bleibt nur ein Rest übrig, wenn der Divisor keine Nullstelle ist.");
                lang.addMCQuestion(mc);
                lang.nextStep("Fazit");
            }
        }

        //Abschlussfolie
        lang.hideAllPrimitives();
        header.show();
        if (num[num.length - 1] != 0)
        {
            lang.newText(new Coordinates(25, 80),
                    "Da als Rest " + num[num.length - 1] + " übrig geblieben ist, ist "
                    + getPolynom(deminator) + "somit keine Nullstelle von " + getPolynom(numerator) + ".",
                    "abs", null, textProps);
        }
        else {
            lang.newText(new Coordinates(25, 80),
                    "Bleibt kein Rest übrig, ist somit das Ergebnis",
                    "abs", null, textProps);
            lang.newText(new Offset(0, 20, "abs",
                            AnimalScript.DIRECTION_SW),
                    getPolynom(res), "poly", null, textProps);
        }
    }

    // --------Hilfsfunktionen--------
    private String getPolynom(int[] p) {
        String poly = "";
        for (int i = 0; i < p.length; i++) {
            String sign = i == 0 ? "" : getSign(p[i]);
            poly += sign + " " + getPart(Math.abs(p[i]), p.length - 1 - i) + " ";
        }
        return poly;
    }

    private String calcStep(String operator, int[] coeff, int[] deg) {
        return getPart(coeff[0], deg[0])
                + operator + getPart(coeff[1], deg[1]) + " = " + getPart(coeff[2], deg[2]);
    }

    private String getPart(int num, int exp) {
        String isOne = num == 1 ? "" : Integer.toString(num);
        String expIsOne = exp == 1 ? "x" : "x^" + exp;
        return exp > 0 ? isOne + expIsOne : Integer.toString(num);
    }

    private String getSign(int num) {
        return num >= 0 ? "+" : "-";
    }

    private String[][] createMatrix(int[] a, int[] b) {
        String[][] max = new String[a.length * 5][a.length * 5];
        width = 0;
        for (int i = 0; i < a.length; i++) {
            int deg = a.length - 1 - i;
            if (i == 0) {
                max[0][++width] = getPart(a[i], deg);
                continue;
            }
            max[0][++width] = getSign(a[i]);
            max[0][++width] = getPart(Math.abs(a[i]), deg);
        }
        max[0][++width] = ":";
        max[0][++width] = "(" + (b[0] == 1 ? "" : b[0]) + "x";
        max[0][++width] = getSign(b[1]);
        max[0][++width] = Math.abs(b[1]) + ")";
        max[0][++width] = "=";

        for (int i = 0; i < a.length * 5; i++) {
            for (int j = 0; j < a.length * 5; j++) {
                if (max[i][j] == null) max[i][j] = "";
            }
        }
        return max;
    }

    // --------------------------------
    public String getName() {
        return "Polynomdivision";
    }

    public String getAlgorithmName() {
        return "Polynomdivision";
    }

    public String getAnimationAuthor() {
        return "Mai Ly Tran";
    }

    public String getDescription() {
        return "Ein Polynom wird �hnlich wie in der schriftlichen Division durch eine seiner Nullstellen der Form (x - n )"
                + "\n"
                + "dividiert, sodass das Ergebnis ein Polynom eines geringeren Grades ergibt.";
    }

    public String getCodeExample() {
        return "public int[] getQuotient(int[] num, int[] dem){"
                + "\n"
                + "        int degN = num.length - 1;"
                + "\n"
                + "        int degD = dem.length - 1;"
                + "\n"
                + "        int[] result = new int[degN];"
                + "\n"
                + "        for(int i = 0; i < degN - degD + 1; i++){"
                + "\n"
                + "            result[i] = num[i] / dem[0];"
                + "\n"
                + "            for (int j = 0; j < dem.length; j++)"
                + "\n"
                + "                num[i+j] -= dem[j] * result[i];"
                + "\n"
                + "        }"
                + "\n"
                + "        return result;"
                + "\n"
                + "    }";
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