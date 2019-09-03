/*
 * Dxt4Generator.java
 * Jan Petto, Nikolas Asimyadis, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.compression;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import generators.compression.helpers.DxtCompressedColor;
import generators.compression.helpers.DxtMultilineText;
import generators.compression.helpers.DxtSourceCodeBuilder;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import static algoanim.properties.AnimationPropertiesKeys.COLOR_PROPERTY;
import static generators.compression.helpers.Dxt.*;

public class Dxt4Generator implements Generator {

    private final static int X_IN = 50;      // left image
    private final static int Y_IN = 120;
    private final static int X_OUT = 700;    // right image
    private final static int Y_OUT = 120;
    private final static int Y_EXPLANATION = 550;     // explanation
    private final static int X_SC = 1100;   // source code

    private final static int X_ALPHA = X_IN + PIXEL_SIZE * N + 20;
    private final static int X_COLORS = X_IN + PIXEL_SIZE * N + 100;

    private final static int RECT_LEFT = 100;
    private final static int RECT_RIGHT = 1040;
    private final static int Y_TEXT_BOX = 450;
    private final static int Y_TEXT_TITLE = Y_TEXT_BOX - 21;
    private final static int Y_TEXT_DEC = Y_TEXT_BOX - 1;
    private final static int Y_TEXT_BIN = Y_TEXT_BOX + 19;
    
    private final static String correctAnswer = "Richtige Antwort! :)";
    private final static String wrongAnswer = "Leider falsch, die richtige Antwort ist: ";

    private Color minHighlightColor;
    private Color maxHighlightColor;
    private Color sourceCodeColor;
    private Color sourceCodeHighlightColor;
    private Color textColor;

    private Language lang;

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        init();

        //get propertie colors
        minHighlightColor = (Color) props.get("squareMin", "color");//fehlt noch
        maxHighlightColor = (Color) props.get("squareMax", "color");//fehlt noch
        sourceCodeColor = (Color) props.get("sourceCode", "color");//fehlt noch
        sourceCodeHighlightColor = (Color) props.get("sourceCode", "highlightColor");//fehlt noch
        textColor = (Color) props.get("text", "color");//eingebaut im code

        Color[] colors = new Color[NxN];
        boolean randomize = (boolean) primitives.get("randomize");
        boolean randomizeAlpha = (boolean) primitives.get("randomizeAlpha");
        if (randomize) {
            for (int i = 0; i < NxN; i++)
                colors[i] = randomColor(randomizeAlpha, true);
        } else {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    Color color = (Color) primitives.get("pixel_R" + r + "C" + c);
                    if (randomizeAlpha)
                        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (Math.random() * 255));
                    colors[r * N + c] = color;
                }
            }
        }

        dxt(colors);
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "DXT4 Komprimierung";
    }

    public String getAlgorithmName() {
        return "DXT4";
    }

    public String getAnimationAuthor() {
        return "Jan Petto, Nikolas Asimyadis";
    }

    @Override
    public String getCodeExample() {
        return "dxt4(Color[] pixels)\n" +
                "    int minAlpha, maxAlpha\n" +
                "    [minAlpha, maxAlpha] = findMinMaxAlpha()\n" +
                "    int a0, a1, a2, a3, a4, a5, a6, a7\n" +
                "    if has total transparency or total opaqueness\n" +
                "        a0 = minAlpha\n" +
                "        a1 = maxAlpha\n" +
                "        a2 = (4 * minAlpha + maxAlpha) / 5\n" +
                "        a3 = (3 * minAlpha + 2 * maxAlpha) / 5\n" +
                "        a4 = (2 * minAlpha + 3 * maxAlpha) / 5\n" +
                "        a5 = (minAlpha + 4 * maxAlpha) / 5\n" +
                "        a6 = 0\n" +
                "        a7 = 255\n" +
                "    else\n" +
                "        a0 = maxAlpha\n" +
                "        a1 = minAlpha\n" +
                "        a2 = (6 * minAlpha + maxAlpha) / 7\n" +
                "        a3 = (5 * minAlpha + 2 * maxAlpha) / 7\n" +
                "        a4 = (4 * minAlpha + 3 * maxAlpha) / 7\n" +
                "        a5 = (3 * minAlpha + 4 * maxAlpha) / 7\n" +
                "        a6 = (2 * minAlpha + 5 * maxAlpha) / 7\n" +
                "        a7 = (minAlpha + 6 * maxAlpha) / 7\n" +
                "    endif\n" +
                "    int[] alphapalette = {a0, a1, a2, a3, a4, a5, a6, a7}\n" +
                "    Color min, max\n" +
                "    [min, max] = findMinMax()\n" +
                "    Color c0, c1, c2, c3\n" +
                "    c0 = min\n" +
                "    c1 = max\n" +
                "    c2 = (2 * c0 + c1) / 3\n" +
                "    c3 = (c0 + 2 * c1) / 3\n" +
                "    Color[] colorpalette = {c0, c1, c2, c3}\n" +
                "    Bitstring alphaoutput = a0a1\n" +
                "    Bitstring coloroutput = c0c1\n" +
                "    foreach pixel in pixels\n" +
                "        alphaoutput += findNearestAlpha(alphapalette, pixel)\n" +
                "        coloroutput += findNearestColor(colorpalette, pixel)\n" +
                "    endfor\n" +
                "    return alphaoutput + coloroutput\n" +
                "end";
    }

    public String getDescription() {
        return "Dxt4 komprimiert 4x4 Pixel-Blöcke zu 128-Bit Daten. Dabei werden Alphawerte mit zwei 8-Bit Werten und einem 4x4 3-Bit\n" +
                "Lookup-Table gespeichert, und Farbwerte wie bei DXT1 mit zwei 16-Bit RGB565 Werten und einem 4x4 2-Bit Lookup-Table komprimiert.\n" +
                "Dabei werden zwischen den beiden Alphawerten sechs weitere Stufen und zwischen den beiden Farbwerten zwei weitere Stufen interpoliert.\n" +
                "Nun wird für jedes Pixel im 4x4 Feld der Index von einem passenden der vier Farbwerte und einem passenden der acht Alphawerte gespeichert.\n" +
                "Für den 4x4 Block werden dann nurnoch das Minimum und das Maximum des Alphas und der Farbe gespeichert, zusammen mit den Lookup-Tables.\n" +
                "Der Output besteht also aus 16 Bit Alphawerten, 48 Bit Alphaindizes, 32 Bit Farbwerten und 32 Bit Farbindizes.";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }


    //****************************************************************************************generator code:


    public void init() {
        lang = new AnimalScript("DXT4 Komprimierung", "Jan Petto, Nikolas Asimyadis", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(
        Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public void dxt(Color[] colors) {
        // check if color size is correct
        if (colors.length != NxN)
            throw new IllegalArgumentException("Invalid color size, has to be " + N + "x" + N);

        //create display colors with premultiplied alpha
        Color[] displayColors = new Color[NxN];
        for (int i = 0; i < NxN; i++) {
            Color c = colors[i];
            if (c.getAlpha() == 255)
                displayColors[i] = c;
            else displayColors[i] = premultiplyAlpha(c);
        }

        createTitleBox(lang, X_IN, 20, 240, "DXT4 Compression");

        DxtMultilineText intro = new DxtMultilineText(lang, X_IN, Y_IN)
                .setColor(textColor)
                .addLine("Einführung:")
                .addLine();
        for (String line : getDescription().split("\n"))
            intro.addLine(line);
        lang.nextStep("Einführung");
        intro.clear();

        // create "image"
        createText(lang, X_IN, Y_IN - 20, "Input:");
        Square[] squares = new Square[NxN];
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                int i = y * N + x;
                squares[i] = createPixel(lang, x, y, X_IN, Y_IN, colors[i], displayColors[i]);
            }
        }


        SourceCode mainSc = new DxtSourceCodeBuilder(lang, X_SC, Y_IN, sourceCodeColor, sourceCodeHighlightColor)

                .addLine("dxt1(Color[] pixels) {")
                .incIndent()
                .addLine("int minAlpha, maxAlpha")
                .addLine("boolean hasTotalTransparency = false")
                .addLine("foreach pixel in pixels {", "foralpha")
                .incIndent()
                .addLine("int alpha = pixel.getAlpha()")
                .addLine("if (alpha < minAlpha) minAlpha = alpha", "minalpha")
                .addLine("if (alpha > maxAlpha) maxAlpha = alpha", "maxalpha")
                .addLine("if (alpha == 0 || alpha == 255) hasTotalTransparency = true", "totalalpha")
                .decIndent()
                .addLine("}", "foralphaend")
                .addLine("int a0, a1, a2, a3, a4, a5, a6, a7", "alphapalette")
                .addLine("if (hasTotalTransparency) {", "iftotal")
                .incIndent()
                .addLine("a0 = minAlpha", "total1")
                .addLine("a1 = maxAlpha", "total2")
                .addLine("a2 = (4 * minAlpha + maxAlpha) / 5", "total3")
                .addLine("a3 = (3 * minAlpha + 2 * maxAlpha) / 5", "total4")
                .addLine("a4 = (2 * minAlpha + 3 * maxAlpha) / 5", "total5")
                .addLine("a5 = (minAlpha + 4 * maxAlpha) / 5", "total6")
                .addLine("a6 = 0", "total7")
                .addLine("a7 = 255", "total8")
                .decIndent()
                .addLine("} else {", "elsetotal")
                .incIndent()
                .addLine("a0 = maxAlpha", "part1")
                .addLine("a1 = minAlpha", "part2")
                .addLine("a2 = (6 * minAlpha + maxAlpha) / 7", "part3")
                .addLine("a3 = (5 * minAlpha + 2 * maxAlpha) / 7", "part4")
                .addLine("a4 = (4 * minAlpha + 3 * maxAlpha) / 7", "part5")
                .addLine("a5 = (3 * minAlpha + 4 * maxAlpha) / 7", "part6")
                .addLine("a6 = (2 * minAlpha + 5 * maxAlpha) / 7", "part7")
                .addLine("a7 = (minAlpha + 6 * maxAlpha) / 7", "part8")
                .decIndent()
                .addLine("}")
                .addLine("int[] alphapalette = {a0, a1, a2, a3, a4, a5, a6, a7}", "fillalphapalette")
                .addLine("Matrix matrix = new Matrix(" + NxN + ", 3)", "start1")
                .addLine("matrix.fillRows(pixels)", "start2")
                .addLine("Vector min, max")
                .addLine("[min, max] = findMinMax(matrix)", "minmax")
                .addLine("Color c0, c1, c2, c3", "colorpalette")
                .addLine("c0 = min", "c0")
                .addLine("c1 = max", "c1")
                .addLine("c2 = (2 * c0 + c1) / 3", "c2")
                .addLine("c3 = (c0 + 2 * c1) / 3", "c3")
                .addLine("Color[] colorpalette = {c0, c1, c2, c3}", "fillcolorpalette")
                .addLine("String alphaoutput = a0 + a1", "alphaoutput")
                .addLine("String coloroutput = compressColor(c0) + compressColor(c1)", "coloroutput")
                .addLine("foreach pixel in pixels {")
                .incIndent()
                .addLine("alphaoutput += findNearestAlpha(alphapalette, pixel)", "appendalpha")
                .addLine("coloroutput += findNearestColor(colorpalette, pixel)", "appendcolor")
                .decIndent()
                .addLine("}")
                .addLine("return alphaoutput + coloroutput")
                .decIndent()
                .addLine("}")
                .getSourceCode();
        lang.nextStep();

        Text title = createText(lang, X_IN, Y_EXPLANATION, "1. Min- und Maxalpha finden");
        title.changeColor(COLOR_PROPERTY, textColor, null, null);
        DxtMultilineText explanation = new DxtMultilineText(lang, X_IN, Y_EXPLANATION + 30)
                .setColor(textColor)
                .addLine("Zunächst werden das kleinste und größte Alpha")
                .addLine("gefunden und geprüft, ob absolut transparente oder")
                .addLine("absolut gedeckte Pixel vorhanden sind.");

        boolean hasTotalAlpha = false;
        int minAlpha = Integer.MAX_VALUE;
        int maxAlpha = Integer.MIN_VALUE;
        int minAlphaIndex = 0;
        int maxAlphaIndex = 0;

        Text minAlphaText = createText(lang, X_ALPHA + 28, Y_IN + 5, "minAlpha = 0");
        Text maxAlphaText = createText(lang, X_ALPHA + 28, Y_IN + 35, "maxAlpha = 255");
        Text totalAlphaText = createText(lang, X_IN + 50, Y_IN + PIXEL_SIZE * N + 15, "totalTransparency = false");
        mainSc.highlight("foralpha");
        mainSc.highlight("foralphaend");
        lang.nextStep("Min- und Max-Alpha finden");

        for (int i = 0; i < NxN; i++) {
            Color c = colors[i];
            if (c.getAlpha() < minAlpha && c.getAlpha() != 0) {
                if (minAlphaIndex != maxAlphaIndex)
                    squares[minAlphaIndex].changeColor(COLOR_PROPERTY, displayColors[minAlphaIndex], null, null);
                squares[i].changeColor(COLOR_PROPERTY, minHighlightColor, null, null);
                mainSc.highlight("minalpha");
                minAlphaText.setText("minalpha = " + c.getAlpha(), null, null);

                minAlpha = c.getAlpha();
                minAlphaIndex = i;
            }
            if (c.getAlpha() > maxAlpha && c.getAlpha() != 255) {
                if (maxAlphaIndex != minAlphaIndex)
                    squares[maxAlphaIndex].changeColor(COLOR_PROPERTY, displayColors[maxAlphaIndex], null, null);
                squares[i].changeColor(COLOR_PROPERTY, maxHighlightColor, null, null);
                mainSc.highlight("maxalpha");
                maxAlphaText.setText("maxalpha = " + c.getAlpha(), null, null);

                maxAlpha = c.getAlpha();
                maxAlphaIndex = i;
            }
            if (c.getAlpha() == 0 || c.getAlpha() == 255) {
                hasTotalAlpha = true;
                mainSc.highlight("totalalpha");
                totalAlphaText.setText("totalTransparency = true", null, null);
            }
            if (i > 0 && i - 1 != minAlphaIndex && i - 1 != maxAlphaIndex)
                squares[i - 1].changeColor(COLOR_PROPERTY, displayColors[i - 1], null, null);
            if (i != minAlphaIndex && i != maxAlphaIndex)
                squares[i].changeColor(COLOR_PROPERTY, getContrastColor(displayColors[i]), null, null);
            lang.nextStep();
            mainSc.unhighlight("minalpha");
            mainSc.unhighlight("maxalpha");
            mainSc.unhighlight("totalalpha");
        }

        if (NxN - 1 != minAlphaIndex && NxN - 1 != maxAlphaIndex)
            squares[NxN - 1].changeColor(COLOR_PROPERTY, displayColors[NxN - 1], null, null);
        squares[minAlphaIndex].changeColor(COLOR_PROPERTY, displayColors[minAlphaIndex], null, null);
        squares[maxAlphaIndex].changeColor(COLOR_PROPERTY, displayColors[maxAlphaIndex], null, null);
        mainSc.unhighlight("foralpha");
        mainSc.unhighlight("foralphaend");

        title.setText("2. Alpha Palette bilden", null, null);
        explanation.clear()
                .addLine("Als nächstes wird die Alpha Palette gebildet.")
                .addLine("Diese besteht aus acht Integer Werten zwischen 0 und 255.")
                .addLine("Abhängig davon, ob absolut transparente bzw nicht transparente")
                .addLine("Pixel (0 bzw 255) vorhanden sind, werden die verschiedenen")
                .addLine("Einträge in der Palette verschieden interpoliert.");
        mainSc.highlight("alphapalette");
        lang.nextStep("Alphapalette bilden");

        explanation
                .addLine()
                .addLine("Sollten absolute Transparenzwerte vorhanden sein,")
                .addLine("wird a0 auf das minimum und a1 auf das maximum gesetzt")
                .addLine("und a2 bis a5 dazwischen interpoliert. a6 und a7 stellen")
                .addLine("absolut transparent bzw nicht transparent dar.")
                .addLine()
                .addLine("Wenn keine absoluten Transparenzwerte vorhanden sind,")
                .addLine("wird a0 auf das maximum und a1 auf das minimum gesetzt")
                .addLine("und a2 bis a7 dazwischen interpoliert.");
        mainSc.unhighlight("alphapalette");
        minAlphaText.hide();
        maxAlphaText.hide();

        int a0, a1, a2, a3, a4, a5, a6, a7;
        Text a0Text, a1Text, a2Text, a3Text, a4Text, a5Text, a6Text, a7Text;
        if (hasTotalAlpha) {
            a0 = minAlpha;
            a1 = maxAlpha;
            a2 = (4 * a0 + a1) / 5;
            a3 = (3 * a0 + a1 * 2) / 5;
            a4 = (2 * a0 + a1 * 3) / 5;
            a5 = (a0 + a1 * 4) / 5;
            a6 = 0;
            a7 = 255;

            a0Text = createText(lang, X_ALPHA, Y_IN + 5, "a0 = minAlpha = " + a0);
            a1Text = createText(lang, X_ALPHA, Y_IN + 35, "a1 = maxAlpha = " + a1);
            a2Text = createText(lang, X_ALPHA, Y_IN + 65, "a2 = (4 * a0 + a1) / 5 = " + a2);
            a3Text = createText(lang, X_ALPHA, Y_IN + 95, "a3 = (3 * a0 + 2 * a1) / 5 = " + a3);
            a4Text = createText(lang, X_ALPHA, Y_IN + 125, "a4 = (2 * a0 + 3 * a1) / 5 = " + a4);
            a5Text = createText(lang, X_ALPHA, Y_IN + 155, "a5 = (a0 + 4 * a1) / 5 = " + a5);
            a6Text = createText(lang, X_ALPHA, Y_IN + 185, "a6 = 0");
            a7Text = createText(lang, X_ALPHA, Y_IN + 215, "a7 = 255");

            mainSc.highlight("total1");
            mainSc.highlight("total2");
            mainSc.highlight("total3");
            mainSc.highlight("total4");
            mainSc.highlight("total5");
            mainSc.highlight("total6");
            mainSc.highlight("total7");
            mainSc.highlight("total8");
            lang.nextStep();
            mainSc.unhighlight("total1");
            mainSc.unhighlight("total2");
            mainSc.unhighlight("total3");
            mainSc.unhighlight("total4");
            mainSc.unhighlight("total5");
            mainSc.unhighlight("total6");
            mainSc.unhighlight("total7");
            mainSc.unhighlight("total8");
        } else {
            a0 = maxAlpha;
            a1 = minAlpha;
            a2 = (6 * a0 + a1) / 7;
            a3 = (5 * a0 + a1 * 2) / 7;
            a4 = (4 * a0 + a1 * 3) / 7;
            a5 = (3 * a0 + a1 * 4) / 7;
            a6 = (2 * a0 + a1 * 5) / 7;
            a7 = (a0 + a1 * 6) / 7;

            a0Text = createText(lang, X_ALPHA, Y_IN + 5, "a0 = maxAlpha = " + a0);
            a1Text = createText(lang, X_ALPHA, Y_IN + 35, "a1 = minAlpha = " + a1);
            a2Text = createText(lang, X_ALPHA, Y_IN + 65, "a2 = (6 * a0 + a1) / 7 = " + a2);
            a3Text = createText(lang, X_ALPHA, Y_IN + 95, "a3 = (5 * a0 + 2 * a1) / 7 = " + a3);
            a4Text = createText(lang, X_ALPHA, Y_IN + 125, "a4 = (4 * a0 + 3 * a1) / 7 = " + a4);
            a5Text = createText(lang, X_ALPHA, Y_IN + 155, "a5 = (3 * a0 + 4 * a1) / 7 = " + a5);
            a6Text = createText(lang, X_ALPHA, Y_IN + 185, "a6 = (2 * a0 + 5 * a1) / 7 = " + a6);
            a7Text = createText(lang, X_ALPHA, Y_IN + 215, "a7 = (a0 + 6 * a1) / 7 = ");
            
            MultipleChoiceQuestionModel alphaCalc = new MultipleChoiceQuestionModel("alpha7");
            alphaCalc.setPrompt("Welcher Wert wird für a7 berechnet?");
            String c2Answer = String.valueOf(a7);
            String wrong = String.valueOf((a0+a1)/2);
            String wrong1 = String.valueOf((a1 + 6 * a0) / 7);
            alphaCalc.addAnswer(wrong1+" ", 0, wrongAnswer + c2Answer);
            alphaCalc.addAnswer(wrong+" ", 0, wrongAnswer + c2Answer);
            alphaCalc.addAnswer(c2Answer, 1, correctAnswer);
            lang.addMCQuestion(alphaCalc);

            lang.nextStep();
            
            a7Text.setText("a7 = (a0 + 6 * a1) / 7 = " + a7, null, null);

            mainSc.highlight("part1");
            mainSc.highlight("part2");
            mainSc.highlight("part3");
            mainSc.highlight("part4");
            mainSc.highlight("part5");
            mainSc.highlight("part6");
            mainSc.highlight("part7");
            mainSc.highlight("part8");
            lang.nextStep();
            mainSc.unhighlight("part1");
            mainSc.unhighlight("part2");
            mainSc.unhighlight("part3");
            mainSc.unhighlight("part4");
            mainSc.unhighlight("part5");
            mainSc.unhighlight("part6");
            mainSc.unhighlight("part7");
            mainSc.unhighlight("part8");
        }
        a0Text.setText("a0 = " + a0, null, null);
        a1Text.setText("a1 = " + a1, null, null);
        a2Text.setText("a2 = " + a2, null, null);
        a3Text.setText("a3 = " + a3, null, null);
        a4Text.setText("a4 = " + a4, null, null);
        a5Text.setText("a5 = " + a5, null, null);
        a6Text.setText("a6 = " + a6, null, null);
        a7Text.setText("a7 = " + a7, null, null);
        mainSc.highlight("fillalphapalette");
        lang.nextStep();
        mainSc.unhighlight("fillalphapalette");

        title.setText("3. Vorbereitung der Min-Max-Findung", null, null);
        title.changeColor(COLOR_PROPERTY, textColor, null, null);
        explanation.clear()
                .addLine("Zunächst werden die Farbwerte der Pixel")
                .addLine("in eine 16x3 Matrix eingetragen. Diese Matrix")
                .addLine("dient der späteren Verarbeitung der Farben.");

        mainSc.highlight("start1");
        mainSc.highlight("start2");
        lang.nextStep("Min- und Max-Farbe finden");
        mainSc.unhighlight("start1");
        mainSc.unhighlight("start2");
        mainSc.highlight("minmax");

        title.setText("4. Min & Max Farben finden", null, null);
        explanation.clear()
                .addLine("Als erstes werden die “kleinste” und “größte” Farbe gesucht.")
                .addLine("Die Pixel werden dabei als 3D-Punktwolke betrachtet, in welcher")
                .addLine("eine Regressionsgerade berechnet wird. Die Pixel werden dann auf")
                .addLine("die Regressionsgerade projeziert und miteinander verglichen.");
        lang.nextStep();

        mainSc.hide();

        SourceCode minMaxSc = new DxtSourceCodeBuilder(lang, X_SC, Y_IN, sourceCodeColor, sourceCodeHighlightColor)

                .addLine("findMinMax(Matrix pixels) {", "start")
                .incIndent()
                .addLine("Vector avg = findAverageColor()", "findavg")
                .addLine("Matrix M = new Matrix(" + NxN + ", 3)", "initmatrix")
                .addLine("foreach pixel in pixels {")
                .incIndent()
                .addLine("M.setRow(i, avg - pixels[i])", "fillmatrix")
                .decIndent()
                .addLine("}")
                .addLine("Matrix C = (M.transpose() * M) * 1/" + NxN, "getc")
                .addLine("Vector regression = findDominantEigenvector(C)", "findreg")
                .addLine("Vector min, max")
                .addLine("foreach pixel in pixels {")
                .incIndent()
                .addLine("Vector p = pixel.projection(regression).add(avg)", "projectp")
                .addLine("if (p < min)", "ifmin").incIndent()
                .addLine("min = p", "setmin").decIndent()
                .addLine("if (p > max)", "ifmax").incIndent()
                .addLine("max = p", "setmax").decIndent()
                .decIndent()
                .addLine("}")
                .addLine("return [min, max]", "return")
                .decIndent()
                .addLine("}")
                .getSourceCode();
        minMaxSc.highlight("start");
        lang.nextStep();

        // find minimum and maximum using Principal Component Analysis
        // treat colors as points in 3D space
        // average color point
        double[] avg = new double[]{0, 0, 0};
        for (Color color : colors) {
            avg[0] += color.getRed();
            avg[1] += color.getGreen();
            avg[2] += color.getBlue();
        }
        avg[0] /= NxN;
        avg[1] /= NxN;
        avg[2] /= NxN;
        RealVector mean = MatrixUtils.createRealVector(avg);

        Text avgText = createText(lang, X_COLORS, Y_IN + 30, "avg =");
        IntMatrix avgPrmtv = createMatrix(lang, X_COLORS + 50, Y_IN, avg);
        minMaxSc.unhighlight("start");
        minMaxSc.highlight("findavg");
        explanation.clear()
                .addLine("Die durchschnittliche Farbe wird als")
                .addLine("arithmetisches Mittel aus allen Punkten berechnet.");
        lang.nextStep();

        // create matrix with columns as distance from each pixel to the middle
        Text matrixText = createText(lang, X_COLORS + 110, Y_IN + 30, "M =");
        IntMatrix matrixPrmtv = createMatrix(lang, X_COLORS + 140, Y_IN, new int[NxN][3]);
        minMaxSc.unhighlight("findavg");
        minMaxSc.highlight("initmatrix");
        explanation.clear()
                .addLine("Jeder Punkt wird mit dem Durschnitt normalisiert.");
        lang.nextStep();

        RealMatrix matrix = MatrixUtils.createRealMatrix(NxN, 3);
        minMaxSc.unhighlight("initmatrix");
        minMaxSc.highlight("fillmatrix");
        for (int i = 0; i < NxN; i++) {
            double[] row = new double[]{colors[i].getRed() - avg[0], colors[i].getGreen() - avg[1], colors[i].getBlue() - avg[2]};
            matrix.setRow(i, row);

            squares[i].changeColor(COLOR_PROPERTY, getContrastColor(displayColors[i]), null, null);
            if (i > 0)
                squares[i - 1].changeColor(COLOR_PROPERTY, displayColors[i - 1], null, null);
            matrixPrmtv.put(i, 0, (int) row[0], null, null);
            matrixPrmtv.put(i, 1, (int) row[1], null, null);
            matrixPrmtv.put(i, 2, (int) row[2], null, null);
            lang.nextStep();
        }
        squares[squares.length - 1].changeColor(COLOR_PROPERTY, displayColors[NxN - 1], null, null);

        // compute covariance
        RealMatrix covaraince = matrix.transpose().multiply(matrix).scalarMultiply(1D / NxN);

        Text covarianceText = createText(lang, X_COLORS + 265, Y_IN + 30, "C =");
        IntMatrix covariancePrmtv = createMatrix(lang, X_COLORS + 300, Y_IN, covaraince.getData());
        minMaxSc.unhighlight("fillmatrix");
        minMaxSc.highlight("getc");
        explanation.clear()
                .addLine("Die Kovarianz-Matrix wird berechnet.");
        lang.nextStep();

        // apply singular value decomposition
        SingularValueDecomposition svd = new SingularValueDecomposition(covaraince);

        // the regression
        RealVector regression = MatrixUtils.createRealVector(svd.getU().getColumn(0)).mapMultiplyToSelf(mean.getNorm());

        Text regressionText = createText(lang, X_COLORS + 430, Y_IN + 30, "regression =");
        IntMatrix regressionPrmtv = createMatrix(lang, X_COLORS + 510, Y_IN, new double[]{regression.getEntry(0), regression.getEntry(1), regression.getEntry(2)});
        minMaxSc.unhighlight("getc");
        minMaxSc.highlight("findreg");
        explanation.addLine("Der größte Eigenvektor der Kovarianz-Matrix")
                .addLine("bildet die Regressionsgerade.");
        lang.nextStep();

        matrixText.hide();
        matrixPrmtv.hide();
        covarianceText.hide();
        covariancePrmtv.hide();
        //regressionText.moveTo(null, null, new Coordinates(X_COLORS + 100, Y_IN + 30), null, Timing.FAST);
        //regressionPrmtv.moveTo(null, null, new Coordinates(X_COLORS + 190, Y_IN), null, Timing.FAST);
        lang.nextStep();

        // create "maximum" point on line, out of reach of actual color values, for comparison
        RealVector maximum = mean.add(regression.mapMultiply(2));

        // project each color point onto regression and compare        
        // initialize with first
        RealVector minPoint = null;
        double min = Double.MAX_VALUE;
        int minIndex = 0;
        RealVector maxPoint = null;
        double max = Double.MIN_VALUE;
        int maxIndex = 0;

        Text pText = createText(lang, X_COLORS + 20, Y_IN + 180, "p =");
        IntMatrix pPrmtv = createMatrix(lang, X_COLORS + 50, Y_IN + 150, new int[3][1]);
        Text minText = createText(lang, X_COLORS + 100, Y_IN + 180, "min =");
        IntMatrix minPrmtv = createMatrix(lang, X_COLORS + 140, Y_IN + 150, new int[3][1]);
        Text maxText = createText(lang, X_COLORS + 200, Y_IN + 180, "max =");
        IntMatrix maxPrmtv = createMatrix(lang, X_COLORS + 240, Y_IN + 150, new int[3][1]);
        minMaxSc.unhighlight("findreg");
        explanation.clear()
                .addLine("Jeder Pixel wird auf die Regressionsgerade projeziert")
                .addLine("und die beiden am weitesten voneinander entfernten")
                .addLine("Punkte bilden das Minimum bzw Maximum.");

        // iterate pixels
        for (int i = 0; i < NxN; i++) {
            //project color onto regression and move it
            RealVector p = matrix.getRowVector(i).projection(regression).add(mean);
            double length = maximum.subtract(p).getNorm();

            squares[i].changeColor(COLOR_PROPERTY, getContrastColor(displayColors[i]), null, null);
            if (i > 0 && i - 1 != minIndex && i - 1 != maxIndex)
                squares[i - 1].changeColor(COLOR_PROPERTY, displayColors[i - 1], null, null);
            pPrmtv.put(0, 0, (int) p.getEntry(0), null, null);
            pPrmtv.put(1, 0, (int) p.getEntry(1), null, null);
            pPrmtv.put(2, 0, (int) p.getEntry(2), null, null);
            minMaxSc.highlight("projectp");
            lang.nextStep();

            if (length < min) {
                if (minIndex != maxIndex)
                    squares[minIndex].changeColor(COLOR_PROPERTY, displayColors[minIndex], null, null);
                squares[i].changeColor(COLOR_PROPERTY, Color.GREEN, null, null);
                minPrmtv.put(0, 0, (int) p.getEntry(0), null, null);
                minPrmtv.put(1, 0, (int) p.getEntry(1), null, null);
                minPrmtv.put(2, 0, (int) p.getEntry(2), null, null);
                minMaxSc.unhighlight("projectp");
                minMaxSc.highlight("ifmin");
                minMaxSc.highlight("setmin");
                lang.nextStep();

                min = length;
                minIndex = i;
                minPoint = p;

                minMaxSc.unhighlight("ifmin");
                minMaxSc.unhighlight("setmin");
            }
            if (length > max) {
                if (maxIndex != minIndex)
                    squares[maxIndex].changeColor(COLOR_PROPERTY, displayColors[maxIndex], null, null);
                squares[i].changeColor(COLOR_PROPERTY, Color.RED, null, null);
                maxPrmtv.put(0, 0, (int) p.getEntry(0), null, null);
                maxPrmtv.put(1, 0, (int) p.getEntry(1), null, null);
                maxPrmtv.put(2, 0, (int) p.getEntry(2), null, null);
                minMaxSc.unhighlight("projectp");
                minMaxSc.highlight("ifmax");
                minMaxSc.highlight("setmax");
                lang.nextStep();

                max = length;
                maxIndex = i;
                maxPoint = p;

                minMaxSc.unhighlight("ifmax");
                minMaxSc.unhighlight("setmax");
            }
        }
        if (NxN - 1 != minIndex && NxN - 1 != maxIndex)
            squares[NxN - 1].changeColor(COLOR_PROPERTY, displayColors[NxN - 1], null, null);

        Color minColor = new Color(checkColorRange(minPoint.getEntry(0)), checkColorRange(minPoint.getEntry(1)), checkColorRange(minPoint.getEntry(2)));
        Color maxColor = new Color(checkColorRange(maxPoint.getEntry(0)), checkColorRange(maxPoint.getEntry(1)), checkColorRange(maxPoint.getEntry(2)));

        pText.hide();
        pPrmtv.hide();
//        minText.moveTo(null, null, new Coordinates(X_COLORS + 10, Y_IN + 180), null, Timing.FAST);
//        minPrmtv.moveTo(null, null, new Coordinates(X_COLORS + 50, Y_IN + 150), null, Timing.FAST);
//        maxText.moveTo(null, null, new Coordinates(X_COLORS + 145, Y_IN + 180), null, Timing.FAST);
//        maxPrmtv.moveTo(null, null, new Coordinates(X_COLORS + 190, Y_IN + 150), null, Timing.FAST);
        minMaxSc.highlight("return");
        squares[minIndex].changeColor(COLOR_PROPERTY, displayColors[minIndex], null, null);
        squares[maxIndex].changeColor(COLOR_PROPERTY, displayColors[maxIndex], null, null);
        lang.nextStep();

        avgText.hide();
        avgPrmtv.hide();
        regressionText.hide();
        regressionPrmtv.hide();
        minText.hide();
        minPrmtv.hide();
        maxText.hide();
        maxPrmtv.hide();
        minMaxSc.hide();
        mainSc.show();
        lang.nextStep();

        mainSc.unhighlight("minmax");
        mainSc.highlight("colorpalette");
        mainSc.highlight("c0");
        mainSc.highlight("c1");
        title.setText("5. Palette bilden", null, null);
        explanation.clear()
                .addLine("Die Palette wird gebildet.")
                .addLine("c0 und c1 werden auf min bzw max gesetzt,")
                .addLine("c2 und c3 werden mit jeweils 1/3 von c0")
                .addLine("und 2/3 von c1 bzw. umgekehrt interpoliert.");
        mainSc.unhighlight("colorpalette");

        Color c0, c1, c2, c3;
        c0 = minColor;
        c1 = maxColor;

        createText(lang, X_COLORS + 20, Y_IN + 22, String.format("c0 = %d, %d, %d", c0.getRed(), c0.getGreen(), c0.getBlue()));
        createText(lang, X_COLORS + 20, Y_IN + 82, String.format("c1 = %d, %d, %d", c1.getRed(), c1.getGreen(), c1.getBlue()));
        createCircle(lang, X_COLORS, Y_IN + 30, 10, c0, c0);
        createCircle(lang, X_COLORS, Y_IN + 90, 10, c1, c1);
        lang.nextStep("Farbpalette bilden");

        c2 = new Color((2 * c0.getRed() + c1.getRed()) / 3, (2 * c0.getGreen() + c1.getGreen()) / 3, (2 * c0.getBlue() + c1.getBlue()) / 3);
        c3 = new Color((c0.getRed() + 2 * c1.getRed()) / 3, (c0.getGreen() + 2 * c1.getGreen()) / 3, (c0.getBlue() + 2 * c1.getBlue()) / 3);

        mainSc.unhighlight("c0");
        mainSc.unhighlight("c1");
        mainSc.highlight("c2");
        mainSc.highlight("c3");
        createText(lang, X_COLORS + 20, Y_IN + 142, String.format("c2 = (2 * c0 + c1) / 3 = %d, %d, %d", c2.getRed(), c2.getGreen(), c2.getBlue()));
        createText(lang, X_COLORS + 20, Y_IN + 202, String.format("c3 = (c0 + 2 * c1) / 3 = %d, %d, %d", c3.getRed(), c3.getGreen(), c3.getBlue()));
        createCircle(lang, X_COLORS, Y_IN + 150, 10, c2, c2);
        createCircle(lang, X_COLORS, Y_IN + 210, 10, c3, c3);
        lang.nextStep();
        mainSc.unhighlight("c2");
        mainSc.unhighlight("c3");
        mainSc.highlight("fillcolorpalette");
        lang.nextStep();

        mainSc.unhighlight("fillcolorpalette");
        mainSc.highlight("alphaoutput");
        title.setText("6. Alpha Palette eintragen", null, null);
        explanation.clear()
                .addLine("a0 und a1 werden als 8 Bit Werte")
                .addLine("in den Output geschrieben.");

        //output anzeigen
        lang.newRect(new Coordinates(RECT_LEFT, Y_TEXT_BOX), new Coordinates(RECT_RIGHT, Y_TEXT_BOX + 15), "", null);
        lang.newRect(new Coordinates(RECT_LEFT, Y_TEXT_BOX + 20), new Coordinates(RECT_RIGHT, Y_TEXT_BOX + 35), "", null);
        int xTextA = RECT_LEFT + 5;
        createText(lang, RECT_LEFT - 90, Y_TEXT_DEC, "Dezimal output:");
        createText(lang, RECT_LEFT - 90, Y_TEXT_BIN, "Binär output:");

        createText(lang, xTextA, Y_TEXT_TITLE, "a0");
        createText(lang, xTextA, Y_TEXT_DEC, String.valueOf(a0));
        createText(lang, xTextA, Y_TEXT_BIN, formatBinary(a0, 8));
        xTextA += TEXT_A_SIZE;
        createText(lang, xTextA, Y_TEXT_TITLE, "a1");
        createText(lang, xTextA, Y_TEXT_DEC, String.valueOf(a1), Color.GRAY);
        createText(lang, xTextA, Y_TEXT_BIN, formatBinary(a1, 8), Color.GRAY);
        xTextA += TEXT_A_SIZE;

        lang.nextStep("Output bilden");

        mainSc.unhighlight("alphaoutput");
        mainSc.highlight("coloroutput");
        title.setText("7. Farben zu 5,6,5 Bit RGB komprimieren", null, null);
        explanation.clear()
                .addLine("c0 und c1 werden als 5,6,5 Bit RGB Werte")
                .addLine("in den Output geschrieben.");

        DxtCompressedColor c0c = new DxtCompressedColor(c0);
        DxtCompressedColor c1c = new DxtCompressedColor(c1);
        DxtCompressedColor c2c = new DxtCompressedColor(c2);
        DxtCompressedColor c3c = new DxtCompressedColor(c3);

        int xTextC = xTextA + NxN * TEXT_AI_SIZE;
        createText(lang, xTextC, Y_TEXT_TITLE, "c0");
        createText(lang, xTextC, Y_TEXT_DEC, String.valueOf(c0c.getRed()), Color.RED);
        createText(lang, xTextC, Y_TEXT_BIN, formatBinary(c0c.getRed(), R_SIZE), Color.RED);
        xTextC += TEXT_R_SIZE;
        createText(lang, xTextC, Y_TEXT_DEC, String.valueOf(c0c.getGreen()), Color.GREEN);
        createText(lang, xTextC, Y_TEXT_BIN, formatBinary(c0c.getGreen(), G_SIZE), Color.GREEN);
        xTextC += TEXT_G_SIZE;
        createText(lang, xTextC, Y_TEXT_DEC, String.valueOf(c0c.getBlue()), Color.BLUE);
        createText(lang, xTextC, Y_TEXT_BIN, formatBinary(c0c.getBlue(), B_SIZE), Color.BLUE);
        xTextC += TEXT_B_SIZE;
        createText(lang, xTextC, Y_TEXT_TITLE, "c1");
        createText(lang, xTextC, Y_TEXT_DEC, String.valueOf(c1c.getRed()), Color.RED);
        createText(lang, xTextC, Y_TEXT_BIN, formatBinary(c1c.getRed(), R_SIZE), Color.RED);
        xTextC += TEXT_R_SIZE;
        createText(lang, xTextC, Y_TEXT_DEC, String.valueOf(c1c.getGreen()), Color.GREEN);
        createText(lang, xTextC, Y_TEXT_BIN, formatBinary(c1c.getGreen(), G_SIZE), Color.GREEN);
        xTextC += TEXT_G_SIZE;
        createText(lang, xTextC, Y_TEXT_DEC, String.valueOf(c1c.getBlue()), Color.BLUE);
        createText(lang, xTextC, Y_TEXT_BIN, formatBinary(c1c.getBlue(), B_SIZE), Color.BLUE);
        xTextC += TEXT_B_SIZE;
        lang.nextStep();

        title.setText("8. Pixel der Palette zuordnen", null, null);
        explanation.clear()
                .addLine("Die restlichen 48 bzw 32 Bit werden mit einer")
                .addLine("4x4 3-Bit Lookup-Table für die Alphawerte und")
                .addLine("einer 4x4 2-Bit Lookup-Table für die Farbwerte gefüllt.");
        mainSc.unhighlight("coloroutput");
        mainSc.unhighlight("alphaoutput");
        mainSc.highlight("appendalpha");
        mainSc.highlight("appendcolor");

        // set new color values
        Square[] newSquares = new Square[NxN];
        Color[] palette = {c0c.getColor(), c1c.getColor(), c2c.getColor(), c3c.getColor()};
        int[] alphaPalette = {a0, a1, a2, a3, a4, a5, a6, a7};
        Color[] newColors = new Color[NxN]; // new array for later comparison of both pictures
        Color[] newDisplayColors = new Color[NxN];

        // create new "image"
        Color colorI = Color.BLACK;
        createText(lang, xTextA, Y_TEXT_TITLE, "alpha lookup-table");
        createText(lang, xTextC, Y_TEXT_TITLE, "color lookup-table");
        createText(lang, X_OUT, Y_IN - 20, "Output:");
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                int i = y * N + x;

                int nearestColor = findNearestColor(palette, colors[i], false);
                int nearestAlpha = findNearestAlpha(alphaPalette, colors[i]);

                Color color = palette[nearestColor];
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alphaPalette[nearestAlpha]);
                Color displayColor = premultiplyAlpha(color);
                newColors[i] = color;
                newDisplayColors[i] = displayColor;

                newSquares[i] = createPixel(lang, x, y, X_OUT, Y_OUT, color, displayColor);
                newSquares[i].changeColor(COLOR_PROPERTY, getContrastColor(displayColor), null, null);

                //add information to imageData
                createText(lang, xTextA, Y_TEXT_DEC, String.valueOf(nearestAlpha), colorI);
                createText(lang, xTextA, Y_TEXT_BIN, formatBinary(nearestAlpha, 3), colorI);
                createText(lang, xTextC, Y_TEXT_DEC, String.valueOf(nearestColor), colorI);
                createText(lang, xTextC, Y_TEXT_BIN, formatBinary(nearestColor, 2), colorI);
                xTextA += TEXT_AI_SIZE;
                xTextC += TEXT_CI_SIZE;
                colorI = colorI == Color.BLACK ? Color.GRAY : Color.BLACK;

                if (i > 0)
                    newSquares[i - 1].changeColor(COLOR_PROPERTY, newDisplayColors[i - 1], null, null);
                lang.nextStep();
            }
        }
        
        MultipleChoiceQuestionModel outputSize = new MultipleChoiceQuestionModel("outputSize");
        outputSize.setPrompt("Wie groß ist der output Array für jeden 4x4 Pixel Block?");
        String expl = " (Der output beinhaltet die Alphawerte a0, a1 und 16 mal 3 bit für die zuordnung von jedem Pixel, die Farben c0 unc c1 mit jeweils 16 bit und 16 mal 2 bit für die zuordnung von jedem Pixel zu c0 bis c3.) ";
        outputSize.addAnswer("64 bit", 0, wrongAnswer + "32 bit"+expl);
        outputSize.addAnswer("128 bit", 1, correctAnswer);
        outputSize.addAnswer("256 bit", 0, wrongAnswer + "32 bit"+ expl);
        lang.addMCQuestion(outputSize);
        
        newSquares[newSquares.length - 1].changeColor(COLOR_PROPERTY, newDisplayColors[NxN - 1], null, null);
        mainSc.unhighlight("appendalpha");
        mainSc.unhighlight("appendcolor");

        title.setText("Zusammenfassung", null, null);
        title.setFont(Font.decode("SansSerif Bold"), null, null);
        explanation.clear().setFont(Font.decode("SansSerif Bold"))
                .addLine("Damit erhalten wir das auf 128 Bit komprimierte Bild.")
                .addLine("")
                .addLine("Dieses enthält nur Farben aus der Kombination der vier Berechneten Farben (c0 bis c3) in der Farbpalette und den acht Alphawerten.")
                .addLine("Der Algorithmus muss jedoch nur a0, a1, c0 und c1 speichern, da die anderen Werte daraus berechnet werden können.")
                .addLine("Außerdem wird für jedes Pixel ein Wert von 0 bis 7 für den nächsten Alphawert aus a0 bis c7 gespeichert.")
                .addLine("Zudem wird für jedes Pixel ein Wert von 0 bis 3 für die ähnlichste Farbe aus c0 bis c3 gespeichert.")
                .addLine("Statt 16 mal 32Bit RGB Werte (jeweils 96 bit groß), werden nurnoch zwei Alphawerte, mit jeweils 8 bit, und zwei Farben, mit jeweils 16 bit, benötigt.")
                .addLine("Außerdem werden 16 mal drei bit und 16 mal zwei bit für die zuordnungen gespeichert.")
                .addLine("Damit wird jeder 4*4 Pixel-Block von 512 bit auf 128 bit, also um den Faktor 4, komprimiert.")
                .addLine()
                .addLine("DXT4 ist erzeugt ein wesentlich besseres Resultat asl DXT1, dieses benötigt jedoch auch doppelt so viel Speicher.");

        lang.nextStep("Zusammenfassung");
    }
}