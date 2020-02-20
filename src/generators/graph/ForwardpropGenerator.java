/*
 * ForwardpropGenerator.java
 * Alexander Kreusser, Kevin Mayer, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import translator.Translator;

public class ForwardpropGenerator implements ValidatingGenerator {
    private Translator translator;
    private Locale loc;
    private static Language lang;

    private static TextProperties tProp, tHeadProp, dProp, txProp, twProp, tFormProp;
    private static RectProperties rProp, rHeadProp;
    private static CircleProperties cProp;
    private static PolylineProperties pProp;

    private int[] x0;
    private int[][] W0;
    private int[][] W1;
    private int actMode;

    private static Color xColor;
    private static Color wColor;
    private static Color highlightColor;

    private static double[][][] W;
    private static double[][] x;

    //private final static int RELU = 0;
    private final static int SIGMOID = 1;

    public void init() {
        lang = new AnimalScript("Neural Network Forward Propagation", "Alexander Kreusser, Kevin Mayer", 800, 600);
    }

    public ForwardpropGenerator(String resources, Locale loc) {
        this.loc = loc;
        translator = new Translator(resources, loc);
    }

    public static void main(String[] args) {
        Generator generator = new ForwardpropGenerator("resources/ForwardpropGenerator", Locale.US); // Generator erzeugen
        Animal.startGeneratorWindow(generator); // Animal mit Generator starten
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
        Text header = lang.newText(new Coordinates(20, 30), "Neural Network Forwardpropagation", "header",
                null, tHeadProp);
        Rect headerRect = lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
                new Offset(5, 5, header, AnimalScript.DIRECTION_SE),
                "hRect", null, rHeadProp);

        // show description
        Text[] description = new Text[8];
        description[0] = lang.newText(new Offset(0, 75, header, AnimalScript.DIRECTION_NW),
                translator.translateMessage("description0"), "description0", null, dProp);
        description[1] = lang.newText(new Offset(0, 25, "description0", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description1"), "description1", null, dProp);
        description[2] = lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description2"), "description2", null, dProp);
        description[3] = lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description3"), "description3", null, dProp);
        description[4] = lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description4"), "description4", null, dProp);
        description[5] = lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description5"), "description5", null, dProp);
        description[6] = lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description6"), "description6", null, dProp);
        description[7] = lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
                translator.translateMessage("description7"), "description7", null, dProp);

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
        x[0] = Arrays.stream(x0).asDoubleStream().toArray();

        // forward propagation
        x[1] = act(mult(W[0], x[0]));
        x[2] = act(mult(W[1], x[1]));

        Node nnOrigin = new Coordinates(60, 100);
        Node compTableOrigin = new Coordinates(380, 225);
        // Node formulaOrigin = new Coordinates(220, 470);

        ArrayList<Primitive> alwaysShow = new ArrayList<>();
        alwaysShow.add(header);
        alwaysShow.add(headerRect);

        lang.nextStep(translator.translateMessage("intro"));

        for(Text t : description) {
            t.hide();
        }

        // draw layers for forward propagation
        // ---------------------------------------------------

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
                if (i > 0) {
                    nValTexts[i][j] = lang.newText(new Offset(0, 15, nTexts[i][j], AnimalScript.DIRECTION_C),
                            "?", "neuron_val" + getNameSuffix(i, j), null, tProp);
                } else {
                    nValTexts[i][j] = lang.newText(new Offset(0, 15, nTexts[i][j], AnimalScript.DIRECTION_C),
                            getString(x[i][j]), "neuron_val" + getNameSuffix(i, j), null, tProp);
                }
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

        lang.nextStep(translator.translateMessage("NN"));

        // draw selected activation function (can be extended)
        Text actText;
        if (actMode == SIGMOID) {
            actText = lang.newText(new Offset(90, 125, header, AnimalScript.DIRECTION_NE),
                    "act ( n ) = 1 / ( 1 + " + '\u212F' + '\u207B' + '\u207F' + " )", "actText", null, dProp);
        } else {
            actText = lang.newText(new Offset(90, 125, header, AnimalScript.DIRECTION_NE),
                    "act ( n ) = max ( n , 0 )", "actText", null, dProp);
        }
        alwaysShow.add(actText);

        // create computation graph for each output neuron with its input neurons
        // ----------------------------------------------------------------------

        // iterate over layers, from first through to second to last
        for (int i = 0; i < x.length - 1; i++) {
            // iterate over output neurons
            for (int j = 0; j < x[i+1].length; j++) {
                lang.hideAllPrimitivesExcept(alwaysShow);

                Text[] wTexts = new Text[x[i].length];
                Text[] xTexts = new Text[x[i].length];
                Polyline[] wLines = new Polyline[x[i].length];
                Polyline[] xLines = new Polyline[x[i].length];

                Text[] multTexts = new Text[x[i].length];
                Text[] addInput = new Text[x[i].length];
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
                    wTexts[k] = lang.newText(new Offset(150, 200 * k, compTableOrigin, AnimalScript.DIRECTION_NW),
                            "w" + (char) ('\u2080' + k) + (char) ('\u2080' + j), "w" + getNameSuffix(i, j, k), null, twProp);
                    Circle c0 = lang.newCircle(new Offset(0, 0, wTexts[k], AnimalScript.DIRECTION_C),
                            30, "w_c" + getNameSuffix(i, j, k), null, cProp);
                    c0.hide();

                    // input node
                    xTexts[k] = lang.newText(new Offset(150, 200 * k + 100, compTableOrigin, AnimalScript.DIRECTION_NW),
                            getLabel(i) + (char) ('\u2080' + k), "x" + getNameSuffix(i, j, k), null, txProp);
                    Circle c1 = lang.newCircle(new Offset(0, 0, xTexts[k], AnimalScript.DIRECTION_C),
                            30, "x_c" + getNameSuffix(i, j, k), null, cProp);
                    c1.hide();

                    // mult node
                    sum += W[i][j][k] * x[i][k];
                    multTexts[k] = lang.newText(new Offset(300, 200 * k + 50, compTableOrigin, AnimalScript.DIRECTION_NW),
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
                Text addText = lang.newText(new Offset(450, 100 * (x[i].length - 1) + 50, compTableOrigin, AnimalScript.DIRECTION_NW),
                        "+", "plus" + getNameSuffix(i, j), null, tProp);
                Circle addc = lang.newCircle(new Offset(0, 0, addText, AnimalScript.DIRECTION_C),
                        30, "add_c" + getNameSuffix(i, j), null, cProp);
                for (int k = 0; k < x[i].length; k++) {
                    multLines[k] = lang.newPolyline(new Node[]{new Offset(0, 0, multCircles[k], AnimalScript.DIRECTION_E),
                                    new Offset(0, 0, addc, AnimalScript.DIRECTION_W)},
                            "mult_line" + getNameSuffix(i, j, k), null, pProp);
                    addInput[k] = lang.newText(new Offset(0, -20, multLines[k], AnimalScript.DIRECTION_C),
                            "?", "mult_val" + getNameSuffix(i, j, k), null, tProp);
                }

                // act node
                Text maxText = lang.newText(new Offset(600, 100 * (x[i].length - 1) + 50, compTableOrigin, AnimalScript.DIRECTION_NW),
                        "act", "act" + getNameSuffix(i, j), null, tProp);
                Circle maxC = lang.newCircle(new Offset(0, 0, maxText, AnimalScript.DIRECTION_C),
                        30, "max_c" + getNameSuffix(i, j), null, cProp);
                Polyline addLine = lang.newPolyline(new Node[]{new Offset(0, 0, addc, AnimalScript.DIRECTION_E),
                                new Offset(0, 0, maxC, AnimalScript.DIRECTION_W)},
                        "add_line" + getNameSuffix(i, j), null, pProp);
                Text maxInput = lang.newText(new Offset(0, -20, addLine, AnimalScript.DIRECTION_C),
                        "?", "max_val" + getNameSuffix(i, j), null, tProp);

                // output
                Text outText = lang.newText(new Offset(750, 100 * (x[i].length - 1) + 50, compTableOrigin, AnimalScript.DIRECTION_NW),
                        getLabel(i+1) + (char)('\u2080' + j), "out" + getNameSuffix(i, j), null, txProp);
                Circle outc = lang.newCircle(new Offset(0, 0, outText, AnimalScript.DIRECTION_C),
                        30, "out_c" + getNameSuffix(i, j), null, cProp);
                outc.hide();
                Polyline maxLine = lang.newPolyline(new Node[]{new Offset(0, 0, maxC, AnimalScript.DIRECTION_E),
                                new Offset(0, 0, outc, AnimalScript.DIRECTION_W)},
                        "max_line" + getNameSuffix(i, j), null, pProp);
                Text outInput = lang.newText(new Offset(0, -20, maxLine, AnimalScript.DIRECTION_C),
                        "?", "y_val" + getNameSuffix(i, j), null, txProp);

                // formula text
                Text formulaLeft = lang.newText(new Offset(100, 75, header, AnimalScript.DIRECTION_NE),
                        outText.getText(), "formulaLeft" + getNameSuffix(i, j), null, txProp);
                Text equals = lang.newText(new Offset(20, 0, formulaLeft, AnimalScript.DIRECTION_NE),
                        " = ", "equals" + getNameSuffix(i, j), null, dProp);
                Text actFunc = lang.newText(new Offset(20, 0, equals, AnimalScript.DIRECTION_NE),
                        "act", "actFunc" + getNameSuffix(i, j), null, dProp);
                Text[] parenthesis = new Text[2];
                parenthesis[0] = lang.newText(new Offset(20, 0, actFunc, AnimalScript.DIRECTION_NE),
                        " ( ", "parenthesis0" + getNameSuffix(i, j), null, dProp);
                Text[] multFunc = new Text[x[i].length];
                Text[] plus = new Text[x[i].length - 1];
                for (int k = 0; k < x[i].length; k++) {
                    if (k > 0) {
                        multFunc[k] = lang.newText(new Offset(20, 0, plus[k - 1], AnimalScript.DIRECTION_NE),
                                wTexts[k].getText() + " " + xTexts[k].getText(), "multFunc" + k + getNameSuffix(i, j), null, dProp);
                    } else {
                        multFunc[k] = lang.newText(new Offset(20, 0, parenthesis[0], AnimalScript.DIRECTION_NE),
                                wTexts[k].getText() + " " + xTexts[k].getText(), "multFunc" + k + getNameSuffix(i, j), null, dProp);
                    }
                    if (k < x[i].length - 1) {
                        plus[k] = lang.newText(new Offset(20, 0, multFunc[k], AnimalScript.DIRECTION_NE),
                                " + ", "plus" + k + getNameSuffix(i, j), null, dProp);
                    } else {
                        parenthesis[1] = lang.newText(new Offset(20, 0, multFunc[k], AnimalScript.DIRECTION_NE),
                                " ) ", "parenthesis1" + getNameSuffix(i, j), null, dProp);
                    }
                }

                // forwardpropagation
                // ---------------
                // Computation of nTexts[i + 1][j].getText()
                lang.nextStep(translator.translateMessage("comp" + (i + 1) + j));

                for (int k = 0; k < x[i].length; k++) {
                    // highlight k-th mult node
                    Rect multRect = lang.newRect(new Offset(-20, -40, wLines[k], AnimalScript.DIRECTION_C),
                            new Offset(170, 40, xLines[k], AnimalScript.DIRECTION_C), "multRect" + getNameSuffix(i, j, k), null, rProp);
                    addInput[k].setText(getString(W[i][j][k] * x[i][k]), null, null);
                    addInput[k].changeColor("color", highlightColor, null, null);
                    multFunc[k].changeColor("color", highlightColor, null, null);

                    lang.nextStep();

                    multRect.hide();
                    addInput[k].changeColor("color", Color.BLACK, null, null);
                    multFunc[k].changeColor("color", Color.BLACK, null, null);
                }

                Rect addRect = lang.newRect(new Offset(-20, -40, multLines[0], AnimalScript.DIRECTION_C),
                        new Offset(170, 40, multLines[x[i].length-1], AnimalScript.DIRECTION_C), "addRect" + getNameSuffix(i, j), null, rProp);
                maxInput.setText(getString(sum), null, null);
                maxInput.changeColor("color", highlightColor, null, null);
                for (Text t : plus) {
                    t.changeColor("color", highlightColor, null, null);
                }

                lang.nextStep();

                addRect.hide();
                maxInput.changeColor("color", Color.BLACK, null, null);
                for (Text t : plus) {
                    t.changeColor("color", Color.BLACK, null, null);
                }
                lang.newRect(new Offset(-20, -40, addLine, AnimalScript.DIRECTION_C),
                        new Offset(20, 40, maxLine, AnimalScript.DIRECTION_C),
                        "maxRect", null, rProp);
                outInput.setText(getString(x[i + 1][j]), null, null);
                nValTexts[i+1][j].setText(outInput.getText(), null, null);
                actFunc.changeColor("color", highlightColor, null, null);
                for (Text t : parenthesis) {
                    t.changeColor("color", highlightColor, null, null);
                }

                lang.nextStep();

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

        alwaysShow.remove(actText);
        lang.hideAllPrimitivesExcept(alwaysShow);

        // show endnote
        Text[] endnote = new Text[8];
        endnote[0] = lang.newText(new Offset(-25, 100 * largestLayer, nnOrigin, AnimalScript.DIRECTION_NW),
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
        endnote[6] = lang.newText(new Offset(0, 25, "endnote5", AnimalScript.DIRECTION_NW),
                translator.translateMessage("endnote6"),
                "endnote6", null, dProp);
        endnote[7] = lang.newText(new Offset(0, 25, "endnote6", AnimalScript.DIRECTION_NW),
                translator.translateMessage("endnote7"),
                "endnote7", null, dProp);

        lang.nextStep(translator.translateMessage("outro"));
        
        return lang.toString();
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
    private double reLU(double in) {
        return (in > 0)? in : 0;
    }

    // Sigmoid activation function
    private double sigmoid(double in) {
        return (1 / (1 + Math.exp(-in)));
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
        tFormProp = new TextProperties();
        tFormProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        tFormProp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        tFormProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));

        // properties for geometric objects
        rProp = new RectProperties();
        rProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        rProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
        rProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, highlightColor);
        cProp = new CircleProperties();
        cProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        pProp = new PolylineProperties();

        // header rectangle
        rHeadProp = new RectProperties();
        rHeadProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        rHeadProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    }

    public String getName() {
        return "Neural Network Forward Propagation";
    }

    public String getAlgorithmName() {
        return "Neural Network Forward Propagation";
    }

    public String getAnimationAuthor() {
        return "Alexander Kreusser, Kevin Mayer";
    }

    public String getDescription(){
        return translator.translateMessage("guiDescription");
    }

    public String getCodeExample(){
        return "for all neurons of hidden layer:"
                +"\n"
                +"  computeOutput( W0, x )"
                +"\n"
                +"for all neurons of output layer:"
                +"\n"
                +"  computeOutput( W1, h )";
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