/*
 * BackpropGenerator.java
 * Alexander Kreusser, Kevin Mayer, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.*;
import animal.main.Animal;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Hashtable;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;


public class BackpropGenerator implements ValidatingGenerator {
    private final static String del = String.valueOf('\u2202');
    //private final static int RELU = 0;
    private final static int SIGMOID = 1;

    private Translator translator;
    private Locale loc;
    private Language lang;

    private static TextProperties tProp, dProp, tbProp, tHeadProp, txProp, twProp, tFormProp, tFormBoldProp;
    private static RectProperties rProp;
    private static CircleProperties cProp;
    private static PolylineProperties pProp;
    private static MatrixProperties matrixProps;

    private int[] x0;
    private int[][] W0;
    private int[][] W1;
    private int actMode;

    private static Color xColor;
    private static Color wColor;
    private static Color highlightColor;

    private static double[][][] W;
    private static double[][] x;


    public static void main(String[] args) {
        Generator generator = new BackpropGenerator("resources/BackpropGenerator", Locale.US);
        Animal.startGeneratorWindow(generator);
    }

    public void init(){
        lang = new AnimalScript("Neural Network Backpropagation", "Alexander Kreusser, Kevin Mayer", 800, 600);
    }

    public BackpropGenerator(String resources, Locale loc) {
        this.loc = loc;
        translator = new Translator(resources, loc);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        TextProperties xColorProp = (TextProperties)props.getPropertiesByName("neuron color");
        xColor = (Color)xColorProp.get("color");
        TextProperties wColorProp = (TextProperties)props.getPropertiesByName("weight color");
        wColor = (Color)wColorProp.get("color");
        TextProperties highlightColorProp = (TextProperties)props.getPropertiesByName("step highlight color");
        highlightColor = (Color)highlightColorProp.get("color");

        lang.setStepMode(true);
        initAnimProps();

        // header
        Text header = lang.newText(new Coordinates(20, 30), "Neural Network Backpropagation", "header",
                null, tHeadProp);

        // show description
        Text[] description = new Text[9];
        description[0] = lang.newText(new Offset(0, 75, header, AnimalScript.DIRECTION_NW),
                translator.translateMessage("description0"), "description0", null, dProp);
        for (int i = 1; i < description.length; i++) {
            description[i] = lang.newText(new Offset(0, 25, "description" + (i-1), AnimalScript.DIRECTION_NW),
                    translator.translateMessage("description" + i), "description" + i, null, dProp);
        }

        lang.nextStep(translator.translateMessage("label_introduction"));
        for (Text t : description) {
            t.hide();
        }

        int largestLayer = Math.max(Math.max(x0.length, W0.length), W1.length); // highest number of neurons in one layer

        W = new double[2][largestLayer][largestLayer]; // [# layers-1, # input neurons, # output neurons]
        W[0] = new double[W0.length][W0[0].length];
        for (int i = 0; i < W0.length; i++) {
            for (int j = 0; j < W0[0].length; j++) {
                W[0][i][j] = W0[i][j];
            }
        }
        W[1] = new double[W1.length][W1[0].length];
        for (int i = 0; i < W1.length; i++) {
            for (int j = 0; j < W1[0].length; j++) {
                W[1][i][j] = W1[i][j];
            }
        }
        x = new double[3][largestLayer];
        x[0] = Arrays.stream(x0).asDoubleStream().toArray();;

        // forward propagation
        x[1] = act(mult(W[0], x[0]));
        x[2] = act(mult(W[1], x[1]));

        // partial derivatives of output w.r.t. weights/neurons
        double[][][] dW = new double[2][largestLayer][largestLayer];
        dW[0] = new double[W0.length][W0[0].length];
        dW[1] = new double[W1.length][W1[0].length];
        double[][] dx = new double[3][largestLayer];

        // set derivatives of output neurons w.r.t. themselves to 1
        Arrays.fill(dx[x.length - 1], 1);

        Node nnOrigin = new Coordinates(60, 100);
        Node compTableOrigin = new Coordinates(60, 300 + 50*largestLayer);
        Node formulaOrigin = new Coordinates(800, 350 + 50*largestLayer);
        Node matricesOrigin = new Coordinates(800, 150);

        ArrayList<Primitive> alwaysShow = new ArrayList<>();
        alwaysShow.add(header);

        // matrices
        // --------
        IntMatrix W0mat = lang.newIntMatrix(new Offset(100, 0, matricesOrigin, AnimalScript.DIRECTION_C), W0, "W0_matrix", null, matrixProps);
        Text W0text = lang.newText(new Offset(-40, -8, W0mat, AnimalScript.DIRECTION_W), "W\u2080 =", "W0_text", null, tProp);

        IntMatrix W1mat = lang.newIntMatrix(new Offset(0, 50, W0mat, AnimalScript.DIRECTION_SW), W1, "W1_matrix", null, matrixProps);
        Text W1text = lang.newText(new Offset(-40, -8, W1mat, AnimalScript.DIRECTION_W), "W\u2081 =", "W1_text", null, tProp);

        alwaysShow.add(W0mat);
        alwaysShow.add(W0text);
        alwaysShow.add(W1mat);
        alwaysShow.add(W1text);

        String[][] dW0s = new String[W0.length][W0[0].length];
        for (int i = 0; i < W0.length; i++) {
            for (int j = 0; j < W0[i].length; j++) {
                dW0s[i][j] = "?";
            }
        }
        StringMatrix dW0mat = lang.newStringMatrix(new Offset(200, 0, W0mat, AnimalScript.DIRECTION_NE), dW0s, "dW0_matrix", null, matrixProps);
        Text eq0 = lang.newText(new Offset(-25, -10, dW0mat, AnimalScript.DIRECTION_W), "=",
                "dW0_eq", null, tFormBoldProp);
        Text dW0text = newFraction(lang, new Offset(-35, -18, eq0, AnimalScript.DIRECTION_W),
                "dW0_text", del + "L", del + "W\u2080", alwaysShow, dProp);

        String[][] dW1s = new String[W1.length][W1[0].length];
        for (int i = 0; i < W1.length; i++) {
            for (int j = 0; j < W1[i].length; j++) {
                dW1s[i][j] = "?";
            }
        }
        StringMatrix dW1mat = lang.newStringMatrix(new Offset(0, 50, dW0mat, AnimalScript.DIRECTION_SW), dW1s, "dW0_matrix", null, matrixProps);
        Text eq1 = lang.newText(new Offset(-25, -10, dW1mat, AnimalScript.DIRECTION_W), "=",
                "dW1_eq", null, tFormBoldProp);
        Text dW1text = newFraction(lang, new Offset(-35, -18, eq1, AnimalScript.DIRECTION_W),
                "dW1_text", del + "L", del + "W\u2081", alwaysShow, dProp);

        alwaysShow.add(dW0mat);
        alwaysShow.add(eq0);
        alwaysShow.add(dW0text);

        alwaysShow.add(dW1mat);
        alwaysShow.add(eq1);
        alwaysShow.add(dW1text);

        // separation lines
        Polyline sep0 = lang.newPolyline(new Node[]{new Coordinates(50, 250 + 50*largestLayer),
                        new Coordinates(1500, 250 + 50*largestLayer)},
                "sep_line0", null, pProp);
        Polyline sep1 = lang.newPolyline(new Node[]{new Coordinates(750, 50),
                        new Coordinates(750, 1000)},
                "sep_line1", null, pProp);
        alwaysShow.add(sep0);
        alwaysShow.add(sep1);


        // draw completed forward propagation
        // ----------------------------------

        Text[][] nTexts = new Text[3][largestLayer];
        Text[][] nValTexts = new Text[3][largestLayer];
        Circle[][] nCircles = new Circle[3][largestLayer];
        Polyline[][][] nnLines = new Polyline[2][largestLayer][largestLayer];
        Text[][][] nnLinesText = new Text[2][largestLayer][largestLayer];

        // iterate over layers
        for (int i = 0; i < x.length; i++) {
            nTexts[i] = new Text[x[i].length];
            nValTexts[i] = new Text[x[i].length];
            nCircles[i] = new Circle[x[i].length];

            // iterate over neurons in layer
            for (int j = 0; j < x[i].length; j++) {
                // neuron
                nTexts[i][j] = lang.newText(new Offset(150 * i, 50 * (largestLayer - x[i].length) + 100 * j,
                                nnOrigin, AnimalScript.DIRECTION_NW), getLabel(i) + (char) ('\u2080' + j),
                        "neuron" + getNameSuffix(i, j), null, tProp);
                nValTexts[i][j] = lang.newText(new Offset(0, 15, nTexts[i][j], AnimalScript.DIRECTION_C),
                        getString(x[i][j]), "neuron_val" + getNameSuffix(i, j), null, tProp);
                nCircles[i][j] = lang.newCircle(new Offset(0, 15, nTexts[i][j], AnimalScript.DIRECTION_C),
                        30, "neuron_c" + getNameSuffix(i, j), null, cProp);
                alwaysShow.add(nTexts[i][j]);
                alwaysShow.add(nValTexts[i][j]);
                alwaysShow.add(nCircles[i][j]);
            }

            // draw lines from each neuron of previous layer to this neuron
            if (i > 0) {
                nnLines[i-1] = new Polyline[x[i].length][x[i-1].length];
                for (int j = 0; j < x[i].length; j++) {
                    for (int k = 0; k < x[i-1].length; k++) {
                        nnLines[i-1][j][k] = lang.newPolyline(new Node[]{new Offset(0, 0, nCircles[i-1][k], AnimalScript.DIRECTION_E),
                                        new Offset(0, 0, nCircles[i][j], AnimalScript.DIRECTION_W)},
                                "nn_line" + getNameSuffix(i, j, k), null, pProp);
                        alwaysShow.add(nnLines[i-1][j][k]);
                        nnLinesText[i-1][j][k] = lang.newText(new Offset(0, -20, nnLines[i-1][j][k], AnimalScript.DIRECTION_C),
                                "w" + (char) ('\u2080' + k) + (char) ('\u2080' + j), "nn_line_text" + getNameSuffix(i, j, k), new Hidden(), twProp);
                    }
                }
            }
        }

        lang.nextStep(translator.translateMessage("label_nn"));

        // create computation graph for each output neuron with its input neurons
        // ----------------------------------------------------------------------

        // iterate over layers, from second to last through to first
        for (int i = x.length - 2; i >= 0; i--) {
            // iterate over output neurons
            for (int j = 0; j < x[i+1].length; j++) {
                lang.hideAllPrimitivesExcept(alwaysShow);

                Text[] wTexts = new Text[x[i].length];
                Text[] xTexts = new Text[x[i].length];
                Polyline[] wLines = new Polyline[x[i].length];
                Polyline[] xLines = new Polyline[x[i].length];

                Text[] multTexts = new Text[x[i].length];
                Circle[] multCircles = new Circle[x[i].length];
                Polyline[] multLines = new Polyline[x[i].length];
                double sum = 0;

                // highlight current output neuron
                nTexts[i+1][j].changeColor("color", xColor, null, null);
                nValTexts[i+1][j].changeColor("color", xColor, null, null);
                nCircles[i+1][j].changeColor("color", xColor, null, null);

                // iterate over input neurons
                for (int k = 0; k < x[i].length; k++) {
                    // highlight current input neurons
                    nTexts[i][k].changeColor("color", xColor, null, null);
                    nValTexts[i][k].changeColor("color", xColor, null, null);
                    nCircles[i][k].changeColor("color", xColor, null, null);
                    nnLines[i][j][k].changeColor("color", wColor, null, null);
                    nnLinesText[i][j][k].show();

                    // weight node
                    wTexts[k] = lang.newText(new Offset(0, 200 * k, compTableOrigin, AnimalScript.DIRECTION_NW),
                            "w" + (char) ('\u2080' + k) + (char) ('\u2080' + j), "w" + getNameSuffix(i, j, k), null, twProp);
                    Circle c0 = lang.newCircle(new Offset(0, 0, wTexts[k], AnimalScript.DIRECTION_C),
                            30, "w_c" + getNameSuffix(i, j, k), null, cProp);
                    c0.hide();

                    // input node
                    xTexts[k] = lang.newText(new Offset(0, 200 * k + 100, compTableOrigin, AnimalScript.DIRECTION_NW),
                            getLabel(i) + (char) ('\u2080' + k), "x" + getNameSuffix(i, j, k), null, txProp);
                    Circle c1 = lang.newCircle(new Offset(0, 0, xTexts[k], AnimalScript.DIRECTION_C),
                            30, "x_c" + getNameSuffix(i, j, k), null, cProp);
                    c1.hide();

                    // mult node
                    sum += W[i][j][k] * x[i][k];
                    multTexts[k] = lang.newText(new Offset(150, 200 * k + 50, compTableOrigin, AnimalScript.DIRECTION_NW),
                            "*", "mult" + getNameSuffix(i, j, k), null, tProp);
                    multCircles[k] = lang.newCircle(new Offset(0, 0, multTexts[k], AnimalScript.DIRECTION_C),
                            30, "mult_c" + getNameSuffix(i, j, k), null, cProp);

                    // lines
                    wLines[k] = lang.newPolyline(new Node[]{new Offset(0, 0, c0, AnimalScript.DIRECTION_E),
                                    new Offset(0, 0, multCircles[k], AnimalScript.DIRECTION_W)},
                            "w_line" + getNameSuffix(i, j, k), null, pProp);
                    lang.newText(new Offset(0, -20, wLines[k], AnimalScript.DIRECTION_C),
                            getString(W[i][j][k]), "w_val" + getNameSuffix(i, j, k), null, twProp);

                    xLines[k] = lang.newPolyline(new Node[]{new Offset(0, 0, c1, AnimalScript.DIRECTION_E),
                                    new Offset(0, 0, multCircles[k], AnimalScript.DIRECTION_W)},
                            "x_line" + getNameSuffix(i, j, k), null, pProp);
                    lang.newText(new Offset(0, -20, xLines[k], AnimalScript.DIRECTION_C),
                            getString(x[i][k]), "x_val" + getNameSuffix(i, j, k), null, txProp);
                }

                // add node
                Text addText = lang.newText(new Offset(300, 100 * (x[i].length - 1) + 50, compTableOrigin, AnimalScript.DIRECTION_NW),
                        "+", "plus" + getNameSuffix(i, j), null, tProp);
                Circle addc = lang.newCircle(new Offset(0, 0, addText, AnimalScript.DIRECTION_C),
                        30, "add_c" + getNameSuffix(i, j), null, cProp);
                for (int k = 0; k < x[i].length; k++) {
                    multLines[k] = lang.newPolyline(new Node[]{new Offset(0, 0, multCircles[k], AnimalScript.DIRECTION_E),
                                    new Offset(0, 0, addc, AnimalScript.DIRECTION_W)},
                            "mult_line" + getNameSuffix(i, j, k), null, pProp);
                    lang.newText(new Offset(0, -20, multLines[k], AnimalScript.DIRECTION_C),
                            getString(W[i][j][k] * x[i][k]), "mult_val" + getNameSuffix(i, j, k), null, tProp);
                }

                // act node
                Text maxText = lang.newText(new Offset(450, 100 * (x[i].length - 1) + 50, compTableOrigin, AnimalScript.DIRECTION_NW),
                            (actMode == SIGMOID)? "\u03C3" : "max", "max" + getNameSuffix(i, j), null, tProp);
                Circle maxC = lang.newCircle(new Offset(0, 0, maxText, AnimalScript.DIRECTION_C),
                        30, "max_c" + getNameSuffix(i, j), null, cProp);
                Polyline addLine = lang.newPolyline(new Node[]{new Offset(0, 0, addc, AnimalScript.DIRECTION_E),
                                new Offset(0, 0, maxC, AnimalScript.DIRECTION_W)},
                        "add_line" + getNameSuffix(i, j), null, pProp);
                lang.newText(new Offset(0, -20, addLine, AnimalScript.DIRECTION_C),
                        getString(sum), "max_val" + getNameSuffix(i, j), null, tProp);

                // output
                Text outText = lang.newText(new Offset(600, 100 * (x[i].length - 1) + 50, compTableOrigin, AnimalScript.DIRECTION_NW),
                        getLabel(i + 1) + (char) ('\u2080' + j), "out" + getNameSuffix(i, j), null, txProp);
                Circle outc = lang.newCircle(new Offset(0, 0, outText, AnimalScript.DIRECTION_C),
                        30, "out_c" + getNameSuffix(i, j), null, cProp);
                outc.hide();
                Polyline maxLine = lang.newPolyline(new Node[]{new Offset(0, 0, maxC, AnimalScript.DIRECTION_E),
                                new Offset(0, 0, outc, AnimalScript.DIRECTION_W)},
                        "max_line" + getNameSuffix(i, j), null, pProp);
                lang.newText(new Offset(0, -20, maxLine, AnimalScript.DIRECTION_C),
                        getString(act(sum)), "y_val" + getNameSuffix(i, j), null, txProp);

                // backpropagation
                // ---------------

                double db = dx[i+1][j];
                lang.newText(new Offset(0, 5, maxLine, AnimalScript.DIRECTION_C),
                        getString(db), "dmax" + getNameSuffix(i, j), null, tProp);

                lang.nextStep(translator.translateMessage("label_neuron_ij") + " " + nTexts[i + 1][j].getText());

                double da;
                if (actMode == SIGMOID) {
                    da = db*(sigmoid(sum)*(1 - sigmoid(sum)));
                } else {
                    da = (sum > 0) ? db : 0;

                }
                lang.newText(new Offset(0, 5, addLine, AnimalScript.DIRECTION_C),
                        getString(db), "dadd" + getNameSuffix(i, j), null, tProp);

                // highlight max node
                Rect maxRect = lang.newRect(new Offset(-20, -40, addLine, AnimalScript.DIRECTION_C),
                        new Offset(20, 40, maxLine, AnimalScript.DIRECTION_C),
                        "maxRect", null, rProp);

                ArrayList<Primitive> inputPrimitives = new ArrayList<>();

                // formula pane
                Text funcText = lang.newText(new Offset(200, 60, formulaOrigin, AnimalScript.DIRECTION_C), (actMode == SIGMOID)? "\u03C3" : "max", "form_max" + getNameSuffix(i, j), null, tHeadProp);
                Circle funcC = lang.newCircle(new Offset(0, 0, funcText, AnimalScript.DIRECTION_C),
                        40, "max_c" + getNameSuffix(i, j), null, cProp);
                lang.newRect(new Offset(-180, -55, funcC, AnimalScript.DIRECTION_NW),
                        new Offset(180, 55, funcC, AnimalScript.DIRECTION_SE),
                        "funcRect" + getNameSuffix(i, j), null, rProp);

                // ---------- max node ----------
                inputPrimitives.addAll(drawInputLines(lang, sum, funcC, i, j, 0, 1));
                inputPrimitives.addAll(drawInputText(lang, da, funcC, i, j, 0, 1));

                Polyline funcOutLine = lang.newPolyline(new Node[]{new Offset(0, 0, funcC, AnimalScript.DIRECTION_E),
                                new Offset(200, 0, funcC, AnimalScript.DIRECTION_E)},
                        "func_out_line" + getNameSuffix(i, j), null, pProp);
                Text funcOut = lang.newText(new Offset(0, -20, funcOutLine, AnimalScript.DIRECTION_C),
                        "b = " + getString((sum > 0) ? sum : 0), "func_out" + getNameSuffix(i, j), null, tFormBoldProp);
                Text dFuncOutLabel = newFraction(lang, new Offset(0, 5, funcOutLine, AnimalScript.DIRECTION_C),
                        "dfunc_out" + getNameSuffix(i, j), del + "L", del + "b", null);
                Text dFuncOut = lang.newText(new Offset(5, 0, dFuncOutLabel, AnimalScript.DIRECTION_E), "= " + getString(db),
                        "dfunc_out_val" + getNameSuffix(i, j), null, tFormBoldProp);

                // local gradient
                Text localGradLabel = newFraction(lang, new Offset(-50, 50, funcC, AnimalScript.DIRECTION_C),
                        "local_grad1" + getNameSuffix(i, j), del + "b", del + "a", inputPrimitives);
                inputPrimitives.add(lang.newText(new Offset(5, 0, localGradLabel, AnimalScript.DIRECTION_E), "=",
                        "local_grad2" + getNameSuffix(i, j), null, tFormBoldProp));

                if (actMode == SIGMOID) {
                    inputPrimitives.add(lang.newText(new Offset(25, 0, localGradLabel, AnimalScript.DIRECTION_E), "(1 - b)\u00B7b",
                            "local_grad4" + getNameSuffix(i, j), null, tFormProp));
                } else {
                    inputPrimitives.add(lang.newText(new Offset(25, -14, localGradLabel, AnimalScript.DIRECTION_E), "{",
                            "local_grad3" + getNameSuffix(i, j), null, tbProp));
                    inputPrimitives.add(lang.newText(new Offset(50, -10, localGradLabel, AnimalScript.DIRECTION_E), "0,   a \u2264 0",
                            "local_grad4" + getNameSuffix(i, j), null, tFormProp));
                    inputPrimitives.add(lang.newText(new Offset(50, 10, localGradLabel, AnimalScript.DIRECTION_E), "1,   a > 0",
                            "local_grad5" + getNameSuffix(i, j), null, tFormProp));

                }

                lang.nextStep();

                // ---------- add node ----------
                db = da;

                for (Primitive p : inputPrimitives) {
                    p.hide();
                }
                inputPrimitives = new ArrayList<>();
                maxRect.hide();
                // highlight add node
                Rect addRect = lang.newRect(new Offset(-20, -40, multLines[0], AnimalScript.DIRECTION_C),
                        new Offset(170, 40, multLines[x[i].length-1], AnimalScript.DIRECTION_C), "addRect" + getNameSuffix(i, j), null, rProp);

                funcText.setText((actMode == SIGMOID)? "+" : "  +", null, null);
                funcOut.setText("b = " + getString(sum), null, null);
                dFuncOut.setText("= " + getString(db), null, null);

                for (int k = 0; k < x[i].length; k++) {
                    inputPrimitives.addAll(drawInputLines(lang, W[i][j][k] * x[i][k], funcC, i, j, k, x[i].length));
                }

                for (int k = 0; k < x[i].length; k++) {
                    inputPrimitives.addAll(drawInputText(lang, db, funcC, i, j, k, x[i].length));
                    lang.newText(new Offset(0, 5, multLines[k], AnimalScript.DIRECTION_C),
                            getString(db), "dmul" + getNameSuffix(i, j, k), null, tProp);

                    // local gradient
                    ArrayList<Primitive> localGradText = new ArrayList<>();
                    localGradLabel = newFraction(lang, new Offset(-30, 50, funcC, AnimalScript.DIRECTION_C),
                            "local_grad1" + getNameSuffix(i, j), del + "b", del + "a" + (char) ('\u2080' + k), localGradText);
                    localGradText.add(lang.newText(new Offset(5, 0, localGradLabel, AnimalScript.DIRECTION_E), "= 1",
                            "local_grad2" + getNameSuffix(i, j), null, tFormBoldProp));

                    lang.nextStep();

                    for (Primitive p: localGradText) {
                        p.hide();
                    }
                }

                // ---------- mult nodes ----------
                for (Primitive p : inputPrimitives) {
                    p.hide();
                }
                inputPrimitives = new ArrayList<>();
                addRect.hide();

                for (int k = 0; k < x[i].length; k++) {
                    // highlight k-th mult node
                    Rect multRect = lang.newRect(new Offset(-30, -40, wLines[k], AnimalScript.DIRECTION_C),
                            new Offset(170, 40, xLines[k], AnimalScript.DIRECTION_C), "multRect" + getNameSuffix(i, j, k), null, rProp);

                    funcText.setText((actMode == SIGMOID)? " *" : "   *", null, null);
                    funcOut.setText("b = " + getString(W[i][j][k] * x[i][k]), null, null);
                    dFuncOut.setText("= " + getString(db), null, null);

                    inputPrimitives.addAll(drawInputLines(lang, W[i][j][k], funcC, i, j, 0, 2));
                    inputPrimitives.addAll(drawInputLines(lang, x[i][k], funcC, i, j, 1, 2));

                    dW[i][j][k] = db * x[i][k];
                    if (i == 0) {
                        dW0mat.put(j, k, getString(dW[i][j][k]), null, null);
                    } else {
                        dW1mat.put(j, k, getString(dW[i][j][k]), null, null);
                    }
                    lang.newText(new Offset(0, 5, wLines[k], AnimalScript.DIRECTION_C),
                            getString(dW[i][j][k]), "dw" + getNameSuffix(i, j, k), null, tProp);
                    inputPrimitives.addAll(drawInputText(lang, dW[i][j][k], funcC, i, j, 0, 2));

                    // local gradient
                    ArrayList<Primitive> localGradText = new ArrayList<>();
                    localGradLabel = newFraction(lang, new Offset(-30, 50, funcC, AnimalScript.DIRECTION_C),
                            "local_grad1" + getNameSuffix(i, j), del + "b", del + "a\u2080", localGradText);
                    localGradText.add(lang.newText(new Offset(5, 0, localGradLabel, AnimalScript.DIRECTION_E), "= a\u2081",
                            "local_grad2" + getNameSuffix(i, j), null, tFormBoldProp));

                    lang.nextStep();

                    for (Primitive p: localGradText) {
                        p.hide();
                    }

                    dx[i][k] += db * W[i][j][k];
                    lang.newText(new Offset(0, 5, xLines[k], AnimalScript.DIRECTION_C),
                            getString(db * W[i][j][k]), "dx" + getNameSuffix(i, j, k), null, tProp);

                    inputPrimitives.addAll(drawInputText(lang, db * W[i][j][k], funcC, i, j, 1, 2));

                    // local gradient
                    localGradText = new ArrayList<>();
                    localGradLabel = newFraction(lang, new Offset(-30, 50, funcC, AnimalScript.DIRECTION_C),
                            "local_grad1" + getNameSuffix(i, j), del + "b", del + "a\u2081", localGradText);
                    localGradText.add(lang.newText(new Offset(5, 0, localGradLabel, AnimalScript.DIRECTION_E), "= a\u2080",
                            "local_grad2" + getNameSuffix(i, j), null, tFormBoldProp));

                    lang.nextStep();

                    for (Primitive p: localGradText) {
                        p.hide();
                    }

                    for (Primitive p : inputPrimitives) {
                        p.hide();
                    }
                    multRect.hide();
                }

                // reset colors of weight lines
                for (int k = 0; k < x[i].length; k++) {
                    nTexts[i][k].changeColor("color", Color.BLACK, null, null);
                    nValTexts[i][k].changeColor("color", Color.BLACK, null, null);
                    nCircles[i][k].changeColor("color", Color.BLACK, null, null);
                    nnLines[i][j][k].changeColor("color", Color.BLACK, null, null);
                }

                nTexts[i+1][j].changeColor("color", Color.BLACK, null, null);
                nValTexts[i+1][j].changeColor("color", Color.BLACK, null, null);
                nCircles[i+1][j].changeColor("color", Color.BLACK, null, null);
            }
        }

        alwaysShow.remove(sep0);
        alwaysShow.remove(sep1);
        lang.hideAllPrimitivesExcept(alwaysShow);

        // show endnote
        Text[] endnote = new Text[8];
        endnote[0] = lang.newText(new Offset(25, 100 * largestLayer, nnOrigin, AnimalScript.DIRECTION_NW),
                translator.translateMessage("endnote0"),
                "endnote0", null, dProp);
        endnote[1] = lang.newText(new Offset(0, 25, "endnote0", AnimalScript.DIRECTION_NW),
                translator.translateMessage("endnote1"),
                "endnote1", null, dProp);
        endnote[2] = lang.newText(new Offset(0, 25, "endnote1", AnimalScript.DIRECTION_NW),
                translator.translateMessage("endnote2"),
                "endnote2", null, dProp);
        endnote[3] = lang.newText(new Offset(0, 25, "endnote2", AnimalScript.DIRECTION_NW),
                translator.translateMessage("endnote3"),
                "endnote3", null, dProp);
        endnote[4] = lang.newText(new Offset(0, 25, "endnote3", AnimalScript.DIRECTION_NW),
                translator.translateMessage("endnote4"),
                "endnote4", null, dProp);
        endnote[5] = lang.newText(new Offset(0, 25, "endnote4", AnimalScript.DIRECTION_NW),
                translator.translateMessage("endnote5"),
                "endnote5", null, dProp);

        lang.nextStep(translator.translateMessage("closingNote"));

        return lang.toString();
    }

    private static ArrayList<Primitive> drawInputLines(Language lang, double a, Circle funcC, int i, int j, int k, int num) {
        ArrayList<Primitive> inputPrimitives = new ArrayList<>();
        int y = -50*(num-1) + 100*k;

        Polyline funcInLine1 = lang.newPolyline(new Node[]{new Offset(-50, y, funcC, AnimalScript.DIRECTION_W),
                        new Offset(0, 0, funcC, AnimalScript.DIRECTION_W)},
                "func_in_line" + getNameSuffix(i, j), null, pProp);
        Polyline funcInLine2 = lang.newPolyline(new Node[]{new Offset(-200, y, funcC, AnimalScript.DIRECTION_W),
                        new Offset(-50, y, funcC, AnimalScript.DIRECTION_W)},
                "func_in_line" + getNameSuffix(i, j), null, pProp);
        Text funcIn = lang.newText(new Offset(0, -20, funcInLine2, AnimalScript.DIRECTION_C),
                "a" + ((num > 1)? (char) ('\u2080' + k) : "") + " = " + getString(a), "func_in" + getNameSuffix(i, j), null, tProp);

        inputPrimitives.add(funcInLine1);
        inputPrimitives.add(funcInLine2);
        inputPrimitives.add(funcIn);

        return inputPrimitives;
    }

    private static ArrayList<Primitive> drawInputText(Language lang, double da, Circle funcC, int i, int j, int k, int num) {
        ArrayList<Primitive> inputPrimitives = new ArrayList<>();
        int y = -50*(num-1) + 100*k;

        Text dFuncInLabel = newFraction(lang, new Offset(-170, y + 5, funcC, AnimalScript.DIRECTION_W),
                "dfunc_in1" + getNameSuffix(i, j), del + "L", del + "a" + ((num > 1)? (char) ('\u2080' + k) : ""), inputPrimitives);
        Text eq = lang.newText(new Offset(5, 0, dFuncInLabel, AnimalScript.DIRECTION_E), "=",
                "dfunc_in2" + getNameSuffix(i, j), null, tFormBoldProp);
        dFuncInLabel = newFraction(lang, new Offset(25, 0, dFuncInLabel, AnimalScript.DIRECTION_NE),
                "dfunc_in3" + getNameSuffix(i, j), del + "L", del + "b", inputPrimitives);
        dFuncInLabel = newFraction(lang, new Offset(5, 0, dFuncInLabel, AnimalScript.DIRECTION_NE),
                "dfunc_in4" + getNameSuffix(i, j), del + "b", del + "a" + ((num > 1)? (char) ('\u2080' + k) : ""), inputPrimitives);
        Text dFuncIn = lang.newText(new Offset(5, 0, dFuncInLabel, AnimalScript.DIRECTION_E), "= " + getString(da),
                "dfunc_in_val" + getNameSuffix(i, j), null, tFormBoldProp);

        inputPrimitives.add(eq);
        inputPrimitives.add(dFuncIn);
        return inputPrimitives;
    }

    private static Text newFraction(Language lang, Node anchor, String name, String num, String denom, ArrayList<Primitive> p) {
        if (p == null) {
            p = new ArrayList<>();
        }
        Text t = lang.newText(anchor, num, name + "_num", null, tFormProp);
        p.add(t);
        p.add(lang.newText(new Offset(0, 10, t, AnimalScript.DIRECTION_W),
                denom, name + "_denom", null, tFormProp));
        p.add(lang.newPolyline(new Node[]{new Offset(0, 0, t, AnimalScript.DIRECTION_SE),
                new Offset(0, 0, t, AnimalScript.DIRECTION_SW)}, name + "_line", null, pProp));
        return t;
    }

    private static Text newFraction(Language lang, Node anchor, String name, String num, String denom, ArrayList<Primitive> p, TextProperties prop) {
        if (p == null) {
            p = new ArrayList<>();
        }
        Text t = lang.newText(anchor, num, name + "_num", null, prop);
        p.add(t);
        p.add(lang.newText(new Offset(0, 10, t, AnimalScript.DIRECTION_W),
                denom, name + "_denom", null, prop));
        p.add(lang.newPolyline(new Node[]{new Offset(0, 0, t, AnimalScript.DIRECTION_SE),
                new Offset(0, 0, t, AnimalScript.DIRECTION_SW)}, name + "_line", null, pProp));
        return t;
    }

    private static String getLabel(int i) {
        switch(i) {
            case 0: return "x";
            case 2: return "y";
            default: return "h";
        }
    }

    // scalar multiplication W * x
    private static double[] mult(double[][] W, double[] x) {
        double[] res = new double[W.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = 0;
            for (int j = 0; j < W[i].length; j++) {
                res[i] += W[i][j] * x[j];
            }
        }
        return res;
    }

    // activation function (ReLU or sigmoid)
    private double act(double in) {
        if (actMode == SIGMOID) {
            return sigmoid(in);
        } else {
            return reLU(in);
        }
    }

    // activation function (ReLU or sigmoid)
    private double[] act(double[] in) {
        double[] res = new double[in.length];
        for (int i = 0; i < res.length; i++) {
            if (actMode == SIGMOID) {
                res[i] = sigmoid(in[i]);
            } else {
                res[i] = reLU(in[i]);
            }
        }
        return res;
    }

    // ReLU activation function
    private double reLU(double x) {
        return (x > 0)? x : 0;
    }

    // Sigmoid activation function
    private double sigmoid(double x) {
        return (1 / (1 + Math.exp(-x)));
    }

    // rounds to two decimal places
    private static double round(double in) {
        return Math.round(in * 100.0) / 100.0;
    }

    private static String getString(double x) {
        if ((double)(int)x == x) {
            return String.valueOf((int)x);
        } else {
            return String.valueOf(round(x));
        }
    }

    private static String getNameSuffix(int i, int j, int k) {
        return "_" + i + "_" + j + "_" + k;
    }

    private static String getNameSuffix(int i, int j) {
        return "_" + i + "_" + j;
    }

    private static void initAnimProps() {
        // default text
        tProp = new TextProperties();
        tProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        tProp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        tProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));

        // not centered default text
        dProp = new TextProperties();
        dProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        dProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));


        // header text
        tHeadProp = new TextProperties();
        tHeadProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        tHeadProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));

        // x highlighted text
        txProp = new TextProperties();
        txProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        txProp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        txProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        txProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, xColor);

        // weight highlighted text
        twProp = new TextProperties();
        twProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        twProp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        twProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        twProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, wColor);

        // formula text
        tFormBoldProp = new TextProperties();
        tFormBoldProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        tFormBoldProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));

        // formula text
        tFormProp = new TextProperties();
        tFormProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        tFormProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));

        // bracket text
        tbProp = new TextProperties();
        tbProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        tbProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 36));

        // properties for geometric objects
        rProp = new RectProperties();
        rProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        rProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
        rProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, highlightColor);
        cProp = new CircleProperties();
        cProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        pProp = new PolylineProperties();

        // matrix
        matrixProps = new MatrixProperties();
        matrixProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
        matrixProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
        matrixProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
    }

    public String getName() {
        return "Neural Network Backpropagation";
    }

    public String getAlgorithmName() {
        return "Neural Network Backpropagation";
    }

    public String getAnimationAuthor() {
        return "Alexander Kreusser, Kevin Mayer";
    }

    public String getDescription(){
        return translator.translateMessage("guiDescription");
    }

    public String getCodeExample(){
        return "for all neurons of output layer:"
                +"\n"
                +"  propagateGradients( W1, h )"
                +"\n"
                +"for all neurons of hidden layer:"
                +"\n"
                +"  propagateGradients( W0, x )";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        x0 = (int[])primitives.get("x");
        W0 = (int[][])primitives.get("W0");
        W1 = (int[][])primitives.get("W1");
        actMode = (int)primitives.get("act");
        return x0.length <= 4 && W0.length <= 4 && W1.length <= 4 && x0.length == W0[0].length && W0.length == W1[0].length
                && actMode >= 0 && actMode <= 1;
    }
}