/*
 * Mandelbrot.java
 * Marvin Kaster, Andre Markard, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
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
import translator.Translator;

import java.awt.Point;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Locale;

public class Mandelbrot implements ValidatingGenerator {

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        yMin = (double) primitives.get("yMin");
        maxIterations = (Integer) primitives.get("maxIterations");
        yMax = (double) primitives.get("yMax");
        pixelyCount = (Integer) primitives.get("pixelyCount");
        xMax = (double) primitives.get("xMax");
        xMin = (double) primitives.get("xMin");
        pixelxCount = (Integer) primitives.get("pixelxCount");

        if (!(yMax > yMin))
            return false;

        if (!(xMax > xMin))
            return false;

        if (maxIterations <= 0)
            return false;

        if (pixelxCount <= 0)
            return false;

        if (pixelyCount <= 0)
            return false;

        return true;
    }

    public class ComplexNumber {

        double re;
        double im;

        ComplexNumber(double re, double im) {
            this.re = re;
            this.im = im;
        }

        ComplexNumber() {
            re = 0;
            im = 0;
        }

        ComplexNumber(ComplexNumber other) {
            this.re = other.re;
            this.im = other.im;
        }

        void add(ComplexNumber otherNumber) {
            this.re += otherNumber.re;
            this.im += otherNumber.im;
        }


        void mult(ComplexNumber otherNumber) {
            double re = this.re;
            this.re = re * otherNumber.re - this.im * otherNumber.im;
            this.im = re * otherNumber.im + otherNumber.re * this.im;
        }

        double norm() {
            return Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
        }

        @Override
        public String toString() {
            String sign = im < 0 ? " - " : " + ";
            DecimalFormat df = new DecimalFormat(",##0.0000");
            return df.format(re) + sign + df.format(Math.abs(im)) + "i";
        }
    }

    private Language lang;

    /*
     * xMin The starting value in x direction representing the minimal real value.
     * xMax The ending value in x direction representing the maximum real value.
     * yMin The starting value in y direction representing the minimal imaginary value.
     * yMax The ending value in y direction representing the maximum imaginary value.
     * pixelxCount The pixel amount in x direction used to split the real number range.
     * pixelyCount The pixel amount in y direction used to split the imaginary number range.
     * maxIterations The maximum depth check for convergence.
     */
    private double yMin;
    private int maxIterations;
    private double yMax;
    private int pixelyCount;
    private double xMax;
    private double xMin;
    private int pixelxCount;

    TextProperties titleProps;
    TextProperties textProps;
    TextProperties textProps2;

    private SourceCode sC;
    private IntMatrix iM;

    private int lastLine = 0;
    private Point lastCell = null;

    private int zCount;
    private int konvCount;

    private double xDiff;
    private double yDiff;

    private DecimalFormat df;

    private int[][] iterations;
    private ComplexNumber[][] numbers;

    private MatrixProperties matProps, mat2Props;
    private SourceCodeProperties scProps;

    // Textfields
    Text beschreibung;
    Text xMinText;
    Text xMaxText;
    Text yMinText;
    Text yMaxText;
    Text pixelxCountText;
    Text pixelyCountText;
    Text maxIterationsText;
    Text zCountText;

    Text xDiffText;
    Text yDiffText;
    Text iText;
    Text reText;
    Text jText;
    Text imText;
    Text zText;
    Text iterationText;

    private Variables variables;

    private  Translator translator;
    private Locale locale;

    public Mandelbrot(String resPrefix, Locale locale) {
        this.locale = locale;
        translator = new Translator(resPrefix, locale);
    }

    public void init() {

        this.lang = new AnimalScript("Mandelbrot", "Marvin Kaster, Andre Markard", 800, 600);
        this.lang.setStepMode(true);
    }

    /**
     * Helper funcation for extracting the given parameters
     * <p>
     * primitives The Hashtable containing the user-set parameters
     */
    private void getSettings(Hashtable<String, Object> primitives) {

        yMin = (double) primitives.get("yMin");
        maxIterations = (Integer) primitives.get("maxIterations");
        yMax = (double) primitives.get("yMax");
        pixelyCount = (Integer) primitives.get("pixelyCount");
        xMax = (double) primitives.get("xMax");
        xMin = (double) primitives.get("xMin");
        pixelxCount = (Integer) primitives.get("pixelxCount");
    }

    /**
     * Helper funcation for extracting the given properties
     * <p>
     * props The Hashtable containing the user-set properties
     */
    private void getProps(AnimationPropertiesContainer props) {

        scProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
        matProps = (MatrixProperties) props.getPropertiesByName("iterationsMatrix");
        mat2Props = (MatrixProperties) props.getPropertiesByName("visualizationMatrix");
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

        // Load user settings
        getSettings(primitives);
        getProps(props);

        Coordinates orientationPoint = new Coordinates(20, 20);

        // Calculate animation
        genIntro(orientationPoint);
        calc(orientationPoint);
        genOutro(new Coordinates(20, 200));

        return lang.toString();
    }

    public String getName() {
        return translator.translateMessage("title");
    }

    public String getAlgorithmName() {
        return "Mandelbrot";
    }

    public String getAnimationAuthor() {
        return "Marvin Kaster, Andre Markard";
    }

    public String getDescription() {
        return translator.translateMessage("description");
    }

    public String getCodeExample() {
        return "public void calc(double xMin, double xMax, double yMin, double yMax, int pixelxCount, int pixelyCount, int maxIterations) {"
                + "\n"
                + "        iterations = new int[pixelxCount][pixelyCount];"
                + "\n"
                + "        double xDiff = (xMax - xMin) / pixelxCount;"
                + "\n"
                + "        double yDiff = (yMax - yMin) / pixelyCount;"
                + "\n"
                + "        for (int i = 0; i < pixelxCount; i++) {"
                + "\n"
                + "            double re = xMin + xDiff * i;"
                + "\n"
                + "            for (int j = 0; j < pixelyCount; j++) {"
                + "\n"
                + "                double im = yMax - yDiff * j;"
                + "\n"
                + "\n"
                + "                ComplexNumber z = new ComplexNumber(0, 0);"
                + "\n"
                + "                int iteration;"
                + "\n"
                + "                for (iteration = 2; iteration < maxIterations && z.norm() < 16; iteration++) {"
                + "\n"
                + "                    z.mult(new ComplexNumber(z));"
                + "\n"
                + "                    z.add(new ComplexNumber(re, im));"
                + "\n"
                + "                }"
                + "\n"
                + "                iterations[i][j] = iteration;"
                + "\n"
                + "                draw(iterations);	"
                + "\n"
                + "            }"
                + "\n"
                + "        }"
                + "\n"
                + "    }"
                + "\n";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    /**
     * @param orientationPoint
     */
    private void genIntro(Coordinates orientationPoint) {

        titleProps = new TextProperties();
        textProps = new TextProperties();
        textProps2 = new TextProperties();


        titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24));
        titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
        textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 18));

        Text title = lang.newText(orientationPoint, translator.translateMessage("title"), "title", null, titleProps);

        //new Offset(-5, -5, orientationPoint, "NW");
        Rect rec = lang.newRect(new Offset(-5, -5, title, "NW"), new Offset(5, 5, title, "SE"), "Rect", null);


        //Intro
        Text intro = lang.newText(new Offset(0, 40, title, "SW"), translator.translateMessage("descriptionHead"), "intro", null, textProps2);
        Text intro2 = lang.newText(new Offset(0, 20, intro, "SW"), translator.translateMessage("descr1"), "intro2", null, textProps);
        Text intro3 = lang.newText(new Offset(0, 20, intro2, "SW"), translator.translateMessage("descr2"), "intro3", null, textProps);
        Text intro4 = lang.newText(new Offset(0, 20, intro3, "SW"), translator.translateMessage("descr3"), "intro4", null, textProps);
        Text intro5 = lang.newText(new Offset(0, 20, intro4, "SW"), translator.translateMessage("descr4"), "intro5", null, textProps);
        Text intro6 = lang.newText(new Offset(0, 20, intro5, "SW"), translator.translateMessage("descr5"), "intro6", null, textProps);

        lang.nextStep();

        intro.hide();
        intro2.hide();
        intro3.hide();
        intro4.hide();
        intro5.hide();
        intro6.hide();
    }

    private void genOutro(Coordinates orientationPoint) {

        Text outro = lang.newText(orientationPoint, translator.translateMessage("outro1"), "outro", null);
        Text outro2 = lang.newText(new Offset(0, 0, outro, "SW"), translator.translateMessage("outro2a") + " " + maxIterations + translator.translateMessage("outro2b"), "outro2", null);
        Text outro3 = lang.newText(new Offset(0, 0, outro2, "SW"), translator.translateMessage("outro3") + " " + konvCount, "outro3", null);
        Text outro4 = lang.newText(new Offset(0, 0, outro3, "SW"), translator.translateMessage("outro4") + " " + ((pixelxCount * pixelyCount) - konvCount), "outro4", null);

        String[][] stringArray = new String[pixelxCount][pixelyCount];
        for (int i = 0; i < stringArray.length; i++)
            for (int j = 0; j < stringArray[i].length; j++)
                stringArray[i][j] = " ";


        StringMatrix sM = lang.newStringMatrix(new Offset(0, 20, outro4, "SW"), stringArray, "grid2", null, mat2Props);

        iM.unhighlightCell(lastCell.x, lastCell.y, null, null);

        for (int i = 0; i < iterations.length; i++)
            for (int j = 0; j < iterations[i].length; j++)
                if (iterations[i][j] == maxIterations)
                    sM.highlightCell(i, j, null, null);

        lang.nextStep();
    }

    private void setupText(Coordinates orientationPoint) {

        beschreibung = lang.newText(new Offset(300, 10, orientationPoint, "SW"), translator.translateMessage("vars"), "beschreibung", null, textProps2);
        xMinText = lang.newText(new Offset(0, 0, beschreibung, "SW"), "xMin: " + xMin, "xMin", null, textProps);
        xMaxText = lang.newText(new Offset(0, 0, xMinText, "SW"), "xMax: " + xMax, "xMax", null, textProps);
        yMinText = lang.newText(new Offset(0, 0, xMaxText, "SW"), "yMin: " + yMin, "yMin", null, textProps);
        yMaxText = lang.newText(new Offset(0, 0, yMinText, "SW"), "yMax: " + yMax, "yMax", null, textProps);
        pixelxCountText = lang.newText(new Offset(0, 0, yMaxText, "SW"), "pixelxCount: " + pixelxCount, "pixelxCount", null, textProps);
        pixelyCountText = lang.newText(new Offset(0, 0, pixelxCountText, "SW"), "pixelyCount: " + pixelyCount, "pixelyCount", null, textProps);
        maxIterationsText = lang.newText(new Offset(0, 0, pixelyCountText, "SW"), "maxIterations: " + maxIterations, "maxIterations", null, textProps);
        zCountText = lang.newText(new Offset(0, 0, maxIterationsText, "SW"), translator.translateMessage("zIterations") + " " + zCount, "outro4", null, textProps);


        xDiffText = lang.newText(new Offset(10, 60, orientationPoint, "SW"), "xDiff: -", "xDiff", null, textProps);
        yDiffText = lang.newText(new Offset(0, 0, xDiffText, "SW"), "yDiff: -", "yDiff", null, textProps);
        iText = lang.newText(new Offset(0, 0, yDiffText, "SW"), "i: -", "i", null, textProps);
        reText = lang.newText(new Offset(0, 0, iText, "SW"), "re: -", "re", null, textProps);
        jText = lang.newText(new Offset(0, 0, reText, "SW"), "j: -", "j", null, textProps);
        imText = lang.newText(new Offset(0, 0, jText, "SW"), "im: -", "im", null, textProps);
        zText = lang.newText(new Offset(0, 0, imText, "SW"), "z: -", "z", null, textProps);
        iterationText = lang.newText(new Offset(0, 0, zText, "SW"), "iteration: -", "iteration", null, textProps);

        iM = lang.newIntMatrix(new Offset(20, -150, zCountText, "SE"), iterations, "grid", null, matProps);
        sC = lang.newSourceCode(new Offset(0, 00, iterationText, "SW"), "sourceCode", null, scProps);

        sC.addCodeLine("public void mandelbrot() {", null, 0, null);
        sC.addCodeLine("iterations = new int[pixelxCount][pixelyCount];", null, 2, null);
        sC.addCodeLine("double xDiff = (xMax - xMin) / pixelxCount;", null, 2, null);
        sC.addCodeLine("double yDiff = (yMax - yMin) / pixelyCount;", null, 2, null);
        sC.addCodeLine("for (int i = 0; i < pixelxCount; i++) {", null, 2, null);
        sC.addCodeLine("double re = xMin + xDiff * i;", null, 4, null);
        sC.addCodeLine("for (int j = 0; j < pixelyCount; j++) {", null, 4, null);
        sC.addCodeLine("double im = yMax - yDiff * j;", null, 6, null);
        sC.addCodeLine("", null, 6, null);
        sC.addCodeLine("ComplexNumber2 z = new ComplexNumber2(0, 0);", null, 6, null);
        sC.addCodeLine("int iteration;", null, 6, null);
        sC.addCodeLine("for (iteration = 0; iteration < maxIterations && z.norm() < 16; iteration++) {", null, 6, null);
        sC.addCodeLine("z.mult(new ComplexNumber2(z));", null, 8, null);
        sC.addCodeLine("z.add(new ComplexNumber2(re, im));", null, 8, null);
        sC.addCodeLine("}", null, 6, null);
        sC.addCodeLine("iterations[i][j] = iteration;", null, 6, null);
        sC.addCodeLine("draw(iterations);", null, 6, null);
        sC.addCodeLine("}", null, 4, null);
        sC.addCodeLine("}", null, 2, null);
        sC.addCodeLine("}", null, 0, null);
    }

    /**
     * The calc function for generating the mandelbrot set and the animation.
     * <p>
     * orientationPoint The base position used for alignment
     */
    private void calc(Coordinates orientationPoint) {

        konvCount = 0;
        zCount = 0;


        df = new DecimalFormat(",##0.0000");
        iterations = new int[pixelxCount][pixelyCount];
        numbers = new ComplexNumber[pixelxCount][pixelyCount];
        xDiff = (xMax - xMin) / pixelxCount;
        yDiff = (yMax - yMin) / pixelyCount;


        // Create all necessary textfields

        setupText(orientationPoint);

        Variables variables = lang.newVariables();
        DecimalFormat df2 = new DecimalFormat(",##0,0000");
        variables.declare("double", "xMin", String.valueOf(xMin));
        variables.declare("double", "xMax", String.valueOf(xMax));
        variables.declare("double", "yMin", String.valueOf(yMin));
        variables.declare("double", "yMax", String.valueOf(yMax));
        variables.declare("int", "pixelxCount", String.valueOf(pixelxCount));
        variables.declare("int", "pixelyCount", String.valueOf(pixelyCount));
        variables.declare("int", "maxIterations", String.valueOf(maxIterations));

        highlight(0);
        lang.nextStep();

        highlight(1);
        lang.nextStep();

        highlight(2);
        xDiffText.setText("xDiff: " + xDiff, null, null);
        variables.declare("double", "xDiff", String.valueOf(xDiff));
        lang.nextStep();

        highlight(3);
        yDiffText.setText("yDiff: " + yDiff, null, null);
        variables.declare("double", "yDiff", String.valueOf(yDiff));
        lang.nextStep();

        for (int i = 0; i < pixelxCount; i++) {

            highlight(4);
            variables.declare("int", "i", String.valueOf(i));
            variables.discard("re");
            variables.discard("im");
            variables.discard("z");
            variables.discard("iteration");
            iText.setText("i: " + i, null, null);
            reText.setText("re: -", null, null);
            imText.setText("im: -", null, null);
            zText.setText("z: -", null, null);
            iterationText.setText("iteration: -", null, null);
            lang.nextStep();

            double re = xMin + xDiff * i;

            highlight(5);
            reText.setText("re: " + df.format(re), null, null);
            variables.declare("double", "re", String.valueOf(re));
            lang.nextStep();

            for (int j = 0; j < pixelyCount; j++) {

                highlight(6);
                variables.declare("int", "j", String.valueOf(j));
                variables.discard("im");
                variables.discard("z");
                variables.discard("iteration");
                jText.setText("j: " + j, null, null);
                imText.setText("im: -", null, null);
                zText.setText("z: -", null, null);
                iterationText.setText("iteration: -", null, null);
                highlightGrid(i, j);
                lang.nextStep();


                double im = yMax - yDiff * j;

                highlight(7);
                variables.declare("double", "im", String.valueOf(im));
                imText.setText("im: " + df.format(im), null, null);
                lang.nextStep();

                ComplexNumber z = new ComplexNumber(0, 0);

                highlight(9);
                variables.declare("string", "z", z.toString());
                zText.setText("z: " + z.toString(), null, null);
                lang.nextStep();


                int iteration;

                for (iteration = 0; iteration < maxIterations && z.norm() < 16; iteration++) {

                    zCount++;
                    zCountText.setText(translator.translateMessage("zIterations") + " " + zCount, null, null);

                    highlight(11);
                    variables.declare("int", "iteration", String.valueOf(iteration));
                    iterationText.setText("iteration: " + iteration, null, null);
                    lang.nextStep();

                    z.mult(new ComplexNumber(z));

                    highlight(12);
                    variables.set("z", z.toString());
                    zText.setText("z: " + z.toString(), null, null);
                    lang.nextStep();

                    z.add(new ComplexNumber(re, im));

                    highlight(13);
                    variables.set("z", z.toString());
                    zText.setText("z: " + z.toString(), null, null);
                    lang.nextStep();

                }

                variables.set("iteration", String.valueOf(iteration));
                iterationText.setText("iteration: " + iteration, null, null);

                if (iteration == 10)
                    konvCount++;

                iterations[i][j] = iteration;

                highlight(15);
                iM.put(i, j, iteration, null, null);
                lang.nextStep();


                numbers[i][j] = z;
            }
        }

        sC.hide();
        xDiffText.hide();
        yDiffText.hide();
        iText.hide();
        reText.hide();
        jText.hide();
        imText.hide();
        zText.hide();
        iterationText.hide();

    }

    private void highlight(int lineNo) {
        sC.unhighlight(lastLine);
        sC.highlight(lineNo);
        lastLine = lineNo;
    }

    private void highlightGrid(int i, int j) {

        if (lastCell != null) {
            iM.unhighlightCell(lastCell.x, lastCell.y, null, null);
        }

        iM.highlightCell(i, j, null, null);
        lastCell = new Point(i, j);
    }

    public int[][] getIterations() {
        return iterations;
    }

    public ComplexNumber[][] getNumbers() {
        return numbers;
    }


}