/*
 * Dxt1Generator.java
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

public class Dxt1Generator implements Generator {

    private final static int X_IN = 50;      // left image
    private final static int Y_IN = 120;
    private final static int X_OUT = 560;    // right image
    private final static int Y_OUT = 120;
    private final static int Y_SC = 550;     // source code

    private final static int X_COLORS = X_IN + PIXEL_SIZE * N + 20;

    private final static int RECT_LEFT = 190;
    private final static int RECT_RIGHT = 660;
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
        minHighlightColor = (Color) props.get("squareMin", "color");
        maxHighlightColor = (Color) props.get("squareMax", "color");
        sourceCodeColor = (Color) props.get("sourceCode", "color");
        sourceCodeHighlightColor = (Color) props.get("sourceCode", "highlightColor");
        textColor = (Color) props.get("text", "color");

        Color[] colors = new Color[NxN];
        boolean randomize = (boolean) primitives.get("randomize");
        boolean randomizeAlpha = (boolean) primitives.get("randomizeAlpha");
        if (randomize) {
            for (int i = 0; i < NxN; i++)
                colors[i] = randomColor(randomizeAlpha, false);
        } else {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    Color color = (Color) primitives.get("pixel_R" + r + "C" + c);
                    if (randomizeAlpha && Math.random() < 0.5)
                        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
                    colors[r * N + c] = color;
                }
            }
        }
        
        dxt(colors);
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "DXT1 Komprimierung";
    }

    public String getAlgorithmName() {
        return "DXT1";
    }

    public String getAnimationAuthor() {
        return "Jan Petto, Nikolas Asimyadis";
    }

    @Override
    public String getCodeExample() {
        return "dxt1(Color[] pixels)\n" +
                "    Color min, max\n" +
                "    [min, max] = findMinMax()\n" +
                "    Color c0, c1, c2, c3\n" +
                "    if contains transparency\n" +
                "        co = min\n" +
                "        c1 = max\n" +
                "        c2 = (c0 + c1) / 2\n" +
                "        c3 = transparent\n" +
                "    else\n" +
                "        c0 = max\n" +
                "        c1 = min\n" +
                "        c2 = (2 * c0 + c1) / 3\n" +
                "        c3 = (c0 + 2 * c1) / 3\n" +
                "    endif\n" +
                "    Color[] palette = {c0, c1, c2, c3}\n" +
                "    Bitstring output = c0c1\n" +
                "    foreach pixel in pixels\n" +
                "        output += findNearestColor(palette, pixel)\n" +
                "    endfor\n" +
                "end";
    }

    public String getDescription() {
        return "DXT1 wählt für jeden 4x4 Pixel-Block ein Minimum und ein Maximum. Beide RGB Farbwerte werden auf 16 bit RGB565 komprimiert.\n"
                + "Dabei ist die Anzahl der R:G:B Bits 5:6:5. Zwischen dem Minimum und Maximum werden zwei weitere Farbwerte berechnet.\n"
                + "Nun wird für jedes Pixel im 4x4 Feld der Index von einem passenden der vier Werte von Minimum bis Maximum gespeichert.\n"
                + "Für den 4x4 Block werden dann nurnoch das Minimum und das Maximum gespeichert (zusammen 32 bit) und für jedes Pixel ein 2 bit Index (16 * 2 bit = 32 bit).\n"
                + "Damit wird ein 16 * 32 bit = 512 bit Block zu einem 64 bit Block komprimiert. Das gesamte Bild benötigt dann 8 mal weniger Speicher.";
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
        lang = new AnimalScript("DXT1 Komprimierung", "Jan Petto, Nikolas Asimyadis", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
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

        createTitleBox(lang, X_IN, 20, 240, "DXT1 Compression");

        DxtMultilineText intro = new DxtMultilineText(lang, X_IN, Y_IN)
                .setColor(textColor)
                .addLine("Einführung:")
                .addLine();
        for (String line : getDescription().split("\n"))
            intro.addLine(line);
        lang.nextStep();
        intro.clear();

        // create "image"
        createText(lang, X_IN, Y_IN - 20, "Input:");
        Square[] squares = new Square[NxN];
        boolean hasTransparency = false;
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                int i = y * N + x;
                squares[i] = createPixel(lang, x, y, X_IN, Y_IN, colors[i], displayColors[i]);
                if (colors[i].getAlpha() < 255)
                    hasTransparency = true;
            }
        }
        createText(lang, X_IN + 50, Y_IN + PIXEL_SIZE * N + 15, "has transparency: " + String.valueOf(hasTransparency));

        SourceCode mainSc = new DxtSourceCodeBuilder(lang, X_IN, Y_SC, sourceCodeColor, sourceCodeHighlightColor)
                .addLine("dxt1(Color[] pixels) {")
                .incIndent()
                .addLine("Matrix matrix = new Matrix(" + NxN + ", 3)", "start1")
                .addLine("matrix.fillRows(pixels)", "start2")
                .addLine("Vector min, max")
                .addLine("[min, max] = findMinMax(matrix)", "minmax")
                .addLine("Color c0, c1, c2, c3", "palette")
                .addLine("if (hasTransparency) {", "transparent")
                .incIndent()
                .addLine("c0 = min.asInt() <= max.asInt() ? min : max", "c0t")
                .addLine("c1 = min.asInt() <= max.asInt() ? max : min", "c1t")
                .addLine("c2 = (c0 + c1) / 2", "c2t")
                .addLine("c3 = new Color(0, 0, 0, 0)", "c3t")
                .decIndent()
                .addLine("} else {", "opaque")
                .incIndent()
                .addLine("c0 = min.asInt() <= max.asInt() ? max : min", "c0o")
                .addLine("c1 = min.asInt() <= max.asInt() ? min : max", "c1o")
                .addLine("c2 = (2 * c0 + c1) / 3", "c2o")
                .addLine("c3 = (c0 + 2 * c1) / 3", "c3o")
                .decIndent()
                .addLine("}")
                .addLine("Color[] palette = new Color[] {c0, c1, c2, c3}", "fillpalette")
                .addLine("String output = new String()", "output1")
                .addLine("output += compressColor(c0)", "output2")
                .addLine("output += compressColor(c1)", "output3")
                .addLine("foreach pixel in pixels {")
                .incIndent()
                .addLine("int index = findNearestColor(palette, pixel)", "append1")
                .addLine("output += index", "append2")
                .decIndent()
                .addLine("}")
                .decIndent()
                .addLine("}")
                .getSourceCode();
        lang.nextStep();

        Text title = createText(lang, X_OUT, Y_SC + 20, "1. Vorbereitung");
        title.changeColor(COLOR_PROPERTY, textColor, null, null);
        DxtMultilineText explanation = new DxtMultilineText(lang, X_OUT, Y_SC + 50)
                .setColor(textColor)
                .addLine("Zunächst werden die Farbwerte der Pixel")
                .addLine("in eine 16x3 Matrix eingetragen. Diese Matrix")
                .addLine("dient der späteren Verarbeitung der Farben.");

        mainSc.highlight("start1");
        mainSc.highlight("start2");
        lang.nextStep("Min und Max Farbe finden");
        mainSc.unhighlight("start1");
        mainSc.unhighlight("start2");
        mainSc.highlight("minmax");

        title.setText("2. Min & Max Farben finden", null, null);
        explanation.clear()
                .addLine("Als erstes werden die “kleinste” und “größte” Farbe gesucht.")
                .addLine("Die Pixel werden dabei als 3D-Punktwolke betrachtet, in welcher")
                .addLine("eine Regressionsgerade berechnet wird. Die Pixel werden dann auf")
                .addLine("die Regressionsgerade projeziert und miteinander verglichen.");
        lang.nextStep();

        mainSc.hide();
        SourceCode minMaxSc = new DxtSourceCodeBuilder(lang, X_IN, Y_SC, sourceCodeColor, sourceCodeHighlightColor)
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
                squares[i].changeColor(COLOR_PROPERTY, minHighlightColor, null, null);
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
                squares[i].changeColor(COLOR_PROPERTY, maxHighlightColor, null, null);
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
        mainSc.highlight("palette");
        title.setText("3. Palette bilden", null, null);
        explanation.clear()
                .addLine("Die Palette wird gebildet.");
        lang.nextStep("Farbpalette Bilden");
        mainSc.highlight("transparent");
        explanation.addLine("Abhängig davon, ob die Pixel transparente Farbwerde enthalten,")
                .addLine("wird die Palette unterschiedlich aufgebaut.")
                .addLine()
                .addLine("Wenn transparente Pixel vorhanden sind, stellt c2")
                .addLine("den Mittelwert zwischen c0 und c1 dar. c3 ist tansparent.")
                .addLine()
                .addLine("Sind keine transparenten Pixel vorhanden, werden c2 und c3")
                .addLine("auf jeweils 1/3 von c0 und 2/3 von c1 bzw. umgekehrt gesetzt.");
        lang.nextStep();
        mainSc.unhighlight("palette");

        Color c0, c1, c2, c3;
        if (hasTransparency) {
            c0 = minColor.getRGB() <= maxColor.getRGB() ? minColor : maxColor;
            c1 = minColor.getRGB() <= maxColor.getRGB() ? maxColor : minColor;
            
            mainSc.unhighlight("transparent");
            mainSc.highlight("c0t");
            mainSc.highlight("c1t");
            createText(lang, X_COLORS + 20, Y_IN + 22, String.format("c0 = %d, %d, %d", c0.getRed(), c0.getGreen(), c0.getBlue()));
            createText(lang, X_COLORS + 20, Y_IN + 82, String.format("c1 = %d, %d, %d", c1.getRed(), c1.getGreen(), c1.getBlue()));
            createCircle(lang, X_COLORS, Y_IN + 30, 10, c0, c0);
            createCircle(lang, X_COLORS, Y_IN + 90, 10, c1, c1);
            
            lang.nextStep();

            c2 = new Color((c0.getRed() + c1.getRed()) / 2, (c0.getGreen() + c1.getGreen()) / 2, (c0.getBlue() + c1.getBlue()) / 2);
            c3 = new Color(0, 0, 0, 0);

            mainSc.unhighlight("c0t");
            mainSc.unhighlight("c1t");
            mainSc.highlight("c2t");
            mainSc.highlight("c3t");
            createText(lang, X_COLORS + 20, Y_IN + 142, String.format("c2 = (c0 + c1) / 2 = %d, %d, %d", c2.getRed(), c2.getGreen(), c2.getBlue()));
            createText(lang, X_COLORS + 20, Y_IN + 202, "c3 = 0, 0, 0, 0");
            createCircle(lang, X_COLORS, Y_IN + 150, 10, c2, c2);
            createCircle(lang, X_COLORS, Y_IN + 210, 10, Color.BLACK, Color.WHITE);
            lang.nextStep();
            mainSc.unhighlight("c2t");
            mainSc.unhighlight("c3t");
        } else {
            c0 = minColor.getRGB() <= maxColor.getRGB() ? maxColor : minColor;
            c1 = minColor.getRGB() <= maxColor.getRGB() ? minColor : maxColor;

            mainSc.unhighlight("transparent");
            mainSc.highlight("opaque");
            lang.nextStep();
            mainSc.unhighlight("opaque");
            mainSc.highlight("c0o");
            mainSc.highlight("c1o");
            createText(lang, X_COLORS + 20, Y_IN + 22, String.format("c0 = %d, %d, %d", c0.getRed(), c0.getGreen(), c0.getBlue()));
            createText(lang, X_COLORS + 20, Y_IN + 82, String.format("c1 = %d, %d, %d", c1.getRed(), c1.getGreen(), c1.getBlue()));
            createCircle(lang, X_COLORS, Y_IN + 30, 10, c0, c0);
            createCircle(lang, X_COLORS, Y_IN + 90, 10, c1, c1);
            
            c2 = new Color((2 * c0.getRed() + c1.getRed()) / 3, (2 * c0.getGreen() + c1.getGreen()) / 3, (2 * c0.getBlue() + c1.getBlue()) / 3);
            c3 = new Color((c0.getRed() + 2 * c1.getRed()) / 3, (c0.getGreen() + 2 * c1.getGreen()) / 3, (c0.getBlue() + 2 * c1.getBlue()) / 3);
            
            MultipleChoiceQuestionModel colorCalc = new MultipleChoiceQuestionModel("color2");
            colorCalc.setPrompt("Welche Farbe wird für c2 berechnet?");
            String c2Answer = "R:"+ c2.getRed()+" G: "+ c2.getGreen() +" B: "+c2.getBlue();
            Color wrong = randomColor(false, false);
            Color wrong1 = randomColor(false, false);
            colorCalc.addAnswer("R:"+ wrong.getRed()+" G: "+ wrong.getGreen()+" B: "+wrong.getBlue()+" ", 0, wrongAnswer + c2Answer);
            colorCalc.addAnswer("R:"+ wrong1.getRed()+" G: "+ wrong1.getGreen()+" B: "+wrong1.getBlue()+" ", 0, wrongAnswer + c2Answer);
            colorCalc.addAnswer(c2Answer, 1, correctAnswer);
            lang.addMCQuestion(colorCalc);
            
            lang.nextStep();

            mainSc.unhighlight("c0o");
            mainSc.unhighlight("c1o");
            mainSc.highlight("c2o");
            mainSc.highlight("c3o");
            createText(lang, X_COLORS + 20, Y_IN + 142, String.format("c2 = (2 * c0 + c1) / 3 = %d, %d, %d", c2.getRed(), c2.getGreen(), c2.getBlue()));
            createText(lang, X_COLORS + 20, Y_IN + 202, String.format("c3 = (c0 + 2 * c1) / 3 = %d, %d, %d", c3.getRed(), c3.getGreen(), c3.getBlue()));
            createCircle(lang, X_COLORS, Y_IN + 150, 10, c2, c2);
            createCircle(lang, X_COLORS, Y_IN + 210, 10, c3, c3);
            lang.nextStep();
            mainSc.unhighlight("c2o");
            mainSc.unhighlight("c3o");
        }
        mainSc.highlight("fillpalette");
        lang.nextStep();

        mainSc.unhighlight("fillpalette");
        mainSc.highlight("output1");
        mainSc.highlight("output2");
        mainSc.highlight("output3");
        title.setText("4. Farben zu 5,6,5 Bit RGB komprimieren", null, null);
        explanation.clear()
                .addLine("c0 und c1 werden als 5,6,5 Bit RGB Werte")
                .addLine("in den Output geschrieben. Somit sind die")
                .addLine("ersten 32 Bit belegt.");

        DxtCompressedColor c0c = new DxtCompressedColor(c0);
        DxtCompressedColor c1c = new DxtCompressedColor(c1);
        DxtCompressedColor c2c = new DxtCompressedColor(c2);
        DxtCompressedColor c3c = new DxtCompressedColor(c3);

        //output anzeigen
        lang.newRect(new Coordinates(RECT_LEFT, Y_TEXT_BOX), new Coordinates(RECT_RIGHT, Y_TEXT_BOX + 15), "", null);
        lang.newRect(new Coordinates(RECT_LEFT, Y_TEXT_BOX + 20), new Coordinates(RECT_RIGHT, Y_TEXT_BOX + 35), "", null);
        int xText = RECT_LEFT + 5;
        createText(lang, RECT_LEFT - 90, Y_TEXT_DEC, "Dezimal output:");
        createText(lang, RECT_LEFT - 90, Y_TEXT_BIN, "Binär output:");
        createText(lang, xText, Y_TEXT_TITLE, "c0");
        createText(lang, xText, Y_TEXT_DEC, String.valueOf(c0c.getRed()), Color.RED);
        createText(lang, xText, Y_TEXT_BIN, formatBinary(c0c.getRed(), R_SIZE), Color.RED);
        xText += TEXT_R_SIZE;
        createText(lang, xText, Y_TEXT_DEC, String.valueOf(c0c.getGreen()), Color.GREEN);
        createText(lang, xText, Y_TEXT_BIN, formatBinary(c0c.getGreen(), G_SIZE), Color.GREEN);
        xText += TEXT_G_SIZE;
        createText(lang, xText, Y_TEXT_DEC, String.valueOf(c0c.getBlue()), Color.BLUE);
        createText(lang, xText, Y_TEXT_BIN, formatBinary(c0c.getBlue(), B_SIZE), Color.BLUE);
        xText += TEXT_B_SIZE;
        createText(lang, xText, Y_TEXT_TITLE, "c1");
        createText(lang, xText, Y_TEXT_DEC, String.valueOf(c1c.getRed()), Color.RED);
        createText(lang, xText, Y_TEXT_BIN, formatBinary(c1c.getRed(), R_SIZE), Color.RED);
        xText += TEXT_R_SIZE;
        createText(lang, xText, Y_TEXT_DEC, String.valueOf(c1c.getGreen()), Color.GREEN);
        createText(lang, xText, Y_TEXT_BIN, formatBinary(c1c.getGreen(), G_SIZE), Color.GREEN);
        xText += TEXT_G_SIZE;
        createText(lang, xText, Y_TEXT_DEC, String.valueOf(c1c.getBlue()), Color.BLUE);
        createText(lang, xText, Y_TEXT_BIN, formatBinary(c1c.getBlue(), B_SIZE), Color.BLUE);
        xText += TEXT_B_SIZE;
        
        lang.nextStep();
        
        MultipleChoiceQuestionModel outputSize = new MultipleChoiceQuestionModel("outputSize");
        outputSize.setPrompt("Wie groß ist der output Array für jeden 4x4 Pixel Block?");
        String expl = " (Der output beinhaltet die Farben c0 unc c1 mit jeweils 16 bit, und 16 mal 2 bit für die zuordnung von jedem Pixel zu c0 bis c3.) ";
        outputSize.addAnswer("64 bit", 0, wrongAnswer + "32 bit"+expl);
        outputSize.addAnswer("32 bit", 1, correctAnswer);
        outputSize.addAnswer("256 bit", 0, wrongAnswer + "32 bit"+ expl);
        lang.addMCQuestion(outputSize);
        
        lang.nextStep("Komprimieren und zuordnen");

        title.setText("5. Pixel der Palette zuordnen", null, null);
        explanation.addLine()
                .addLine("Die hinteren 32 Bit werden mit einer")
                .addLine("4x4 2-Bit lookup table gefüllt.")
                .addLine("Das heißt jeweils zwei Bit bilden")
                .addLine("einen Verweis auf c0 bis c3.");
        mainSc.unhighlight("output1");
        mainSc.unhighlight("output2");
        mainSc.unhighlight("output3");
        mainSc.highlight("append1");
        mainSc.highlight("append2");

        // set new color values
        Square[] newSquares = new Square[NxN];
        Color[] palette = {c0c.getColor(), c1c.getColor(), c2c.getColor(), c3c.getColor()};
        Color[] newColors = new Color[NxN]; // new array for later comparison of both pictures
        Color[] newDisplayColors = new Color[NxN];
        
        // create new "image"
        Color colorI = Color.BLACK;
        createText(lang, xText, Y_TEXT_TITLE, "lookup-table");
        createText(lang, X_OUT, Y_IN - 20, "Output:");
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                int i = y * N + x;

                int nearest = findNearestColor(palette, colors[i], true);
                Color color = palette[nearest];
                Color displayColor = premultiplyAlpha(color);
                newColors[i] = color;
                newDisplayColors[i] = displayColor;

                newSquares[i] = createPixel(lang, x, y, X_OUT, Y_OUT, color, displayColor);
                newSquares[i].changeColor(COLOR_PROPERTY, getContrastColor(displayColor), null, null);

                //add information to imageData
                createText(lang, xText, Y_TEXT_DEC, String.valueOf(nearest), colorI);
                createText(lang, xText, Y_TEXT_BIN, formatBinary(nearest, 2), colorI);
                xText += TEXT_CI_SIZE;
                colorI = colorI == Color.BLACK ? Color.GRAY : Color.BLACK;

                if (i > 0)
                    newSquares[i - 1].changeColor(COLOR_PROPERTY, newDisplayColors[i - 1], null, null);
                lang.nextStep();
            }
        }
        newSquares[newSquares.length - 1].changeColor(COLOR_PROPERTY, newDisplayColors[NxN - 1], null, null);
        mainSc.unhighlight("append1");
        mainSc.unhighlight("append2");
        
        lang.nextStep();
        
        MultipleChoiceQuestionModel verlustBehaftet = new MultipleChoiceQuestionModel("verlustBehaftet");
        verlustBehaftet.setPrompt("Ist DXT1 eine verlustbehaftete Kompression?");
        String explV = " Die Farben c0-c3 selbst sind verlustbehaftet auf jeweils 16 bit komprimiert. Da jeder Pixel nur auf eine ähnliche farbe abgebildet wird, ist DXT1 verlustbehaftet. ";
        verlustBehaftet.addAnswer("Nein.", 0, wrongAnswer + "Ja."+explV);
        verlustBehaftet.addAnswer("Ja.", 1, correctAnswer);
        verlustBehaftet.addAnswer("Nur wenn das Bild sehr viele verschiedene Farben hat", 0, wrongAnswer + "Ja."+ explV+ "Wie viele Farben das Bild hat spielt dabei keine Rolle, da DXT1 immer nur mit 4x4 Pixel Blöcken arbeitet.");
        lang.addMCQuestion(verlustBehaftet);

        lang.nextStep();
        
        MultipleChoiceQuestionModel c2c3 = new MultipleChoiceQuestionModel("c2c3");
        c2c3.setPrompt("Wo werden c2 und c3 nach der Kompression gespeichert?");
        String explc = " C2 und c3 müssen nicht gespeichert werden, da sie aus c0 und c1 berechnet werden können. ";
        c2c3.addAnswer("In einem separaten Array.", 0, wrongAnswer + "Sie werden nicht gespeichert." + explc);
        c2c3.addAnswer("Direkt hinter c0 und c1.", 1, wrongAnswer + "Sie werden nicht gespeichert." + explc);
        c2c3.addAnswer("Sie werden nicht gespeichert.", 0, correctAnswer + explc);
        lang.addMCQuestion(c2c3);

        lang.nextStep();
        title.setText("Zusammenfassung", null, null);
        title.setFont(Font.decode("SansSerif Bold"), null, null);
        explanation.clear().setFont(Font.decode("SansSerif Bold"))
                .addLine("Damit erhalten wir das auf 64 Bit komprimierte Bild.")
                .addLine("Dieses enthält nur Farben aus vier Berechneten (c0-c3) in der Farbpalette.")
                .addLine("Der Algorithmus muss jedoch nur c0 und c1 speichern, da die anderen Farben daraus berechnet werden können.")
                .addLine("Außerdem wird für jedes Pixel ein Wert von 0 bis 3 für die ähnlichste Farbe aus c0 bis c3 gespeichert.")
                .addLine("Statt 16 mal 32Bit RGB Werte (jeweils 96 bit groß), werden nurnoch zwei Farben mit jeweils 16 bit benötigt.")
                .addLine("Außerdem werden 16 mal zwei bit für die zuordnung gespeichert.")
                .addLine("Damit wird jeder 4*4 Pixel-Block von 512 bit auf 64 bit, also um den Faktor 8, komprimiert.")
                .addLine()
                .addLine("Bei einem klaren Farbverlauf im Original Pixelblock, ist das Ergebnis ziemlich gut.")
                .addLine("Bei zufälligen Werten im Original Block, sieht man nur wenige gemeinsamkeiten zu dem komprimierten Block.")
                .addLine("Da es sich jedoch nur um 4*4 Pixelblöcke handelt, führt das bei dem gesamten Bild nur zu einem kleinen Qualitätsverlust.");

        lang.nextStep("Zusammenfassung");
    }
}