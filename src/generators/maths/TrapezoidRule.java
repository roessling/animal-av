/*
 * TrapezoidRule.java
 * Christian Hack, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.*;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import util.text.FormattedText;
import generators.maths.trapezoidhelpers.Function;
import generators.maths.trapezoidhelpers.IntegralResult;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

public class TrapezoidRule implements ValidatingGenerator {
    private Language lang;
    private int funktion;
    private Color resultExact, resultNodes, resultError;
    private Color resultColorCalls;
    private Color resultColorB;
    private Color resultColorResult;
    private Color resultColorA;
    private Color finishedIntervall;
    private Color infoboxHighlighting;
    private PolylineProperties startIntervall;
    private SourceCodeProperties sourceProperties;
    private PolylineProperties graphProperties;
    private PolylineProperties currentIntervall;

    private final static Timing defaultTickDuration = new TicksTiming(30);
    private final static Timing defaultMsDuration = new MsTiming(150);
    private double scaleX, scaleY, xMin;
    private double min = Double.MAX_VALUE;
    private double max = -Double.MAX_VALUE;
    private int yCordForXAxis;
    private int width = 500;
    private int x_offset = 100;
    private int y_offset = 200;
    private int count = 0;
    private SourceCode source;
    private Text recNr, a, b, h, simpson, trapeze, tolerance, integralOne, integralTwo, integral, leftLabel, rightLabel;
    private DecimalFormat numberFormat = new DecimalFormat("#.####");
    private Polyline left, right;
    private Primitive[] sourcePrimitives, infoPrimitives;
    private InfoBox infoBox;
    private double tol;
    private String functionString;

    private void executeAnimation(double a, double b, double tolerance) {
        double h = b - a;

        Function function;
        Function df;
        switch (funktion) {
            case 0:
                function = x -> x*x -2;
                df = x -> (x * x * x)/3 - 2 * x;
                functionString = "f(x) = x^2 - 2";
                break;
            case 1:
                function = x -> x * x * x;
                df = x -> (Math.pow(x,4))/4;
                functionString = "f(x) = x^3";
                break;
            case 2:
                function = Math::atan;
                df = x -> x * Math.atan(x) -  Math.log(x * x + 1)/2;
                functionString = "f(x) = atan(x)";
                break;
            case 3:
                function = x -> x;
                df = x ->  (x * x)/2;
                functionString = "f(x) = x";
                break;
            case 4:
                function = x -> (x-1)*(x-0.5)*(x+0.5)*(x+1);
                df = x -> (Math.pow(x,5))/5 - (5 * Math.pow(x,3))/12 + x/4;
                functionString = "f(x) = x^4 – 1.25*x^2 + 0.25";
                break;
            default:
                function = Math::sqrt;
                df = x -> (2 * Math.pow(x,3.0/2.0))/3;
                functionString = "f(x) = sqrt(x)";
                break;
        }

        double tol = Math.pow(10, tolerance)/h; //Math.pow(10, -3) / h;


        initialiseAnimation(function, a, b);
        startAnimation(function, a, b, tol, df);
    }

    private void initialiseAnimation(Function f, double a, double b) {
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        scaleX = width / (b - a);
        xMin = a;

        // Title
        TextProperties titleProps = new TextProperties();
        titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
        titleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        lang.newText(new Coordinates(50,50), "Adaptive Quadratur unter Verwendung der Trapezregel", "title", null, titleProps);
        titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        lang.newText(new Offset(225, 2, "title", AnimalScript.DIRECTION_SW),"von Christian Hack", "author",null, titleProps);
        RectProperties titleRect = new RectProperties();
        titleRect.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        titleRect.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        titleRect.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        lang.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW), new Offset(5, 50, "title", AnimalScript.DIRECTION_SE), "titleRect", null, titleRect);

        lang.nextStep("Description");
        showDescription();

        drawAxis(f, a, b);
        drawGraph(f, a);
        drawInitialInterval(f, a, b);
        lang.nextStep("Show source and info area");

        showSourceCode();
        showInfoBox();
        lang.nextStep("Start algorithm.");
    }

    private void showDescription() {
        RectProperties titleRect = new RectProperties();
        titleRect.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        titleRect.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        titleRect.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        Rect introRect = lang.newRect(new Offset(0, 40, "titleRect", AnimalScript.DIRECTION_SW),
                new Offset(0, 450, "titleRect", AnimalScript.DIRECTION_SE), "introRect", defaultTickDuration, titleRect);

        TextProperties t = new TextProperties();
        t.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        Text introText = lang.newText(new Offset(10, 50, "titleRect", AnimalScript.DIRECTION_SW), "Einführung", "introText", defaultTickDuration, t);

        t = new TextProperties();
        t.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
        FormattedText startInfo = new FormattedText("", lang, new Offset(14, 50, "titleRect", AnimalScript.DIRECTION_SW), t, true, 0.2D, 0.2D);
        startInfo.addInNextLine("");
        startInfo.addInNextLine("Bei der adaptiven Trapezregel handelt es sich um ein numerisches Verfahren zur");
        startInfo.addInNextLine("Annäherung des Integrals einer Funktion f(x) über ein Intervall [a, b]. Dabei ersetzt");
        startInfo.addInNextLine("man die Fläche unter dem Funktionsgraphen durch ein oder mehrere Trapeze.");
        startInfo.addInNextLine("Trapezregel: (b-a)/2 * (f(a) + f(b))");
        startInfo.addInNextLine("");
        startInfo.addInNextLine("Um hierbei auf ein besseres Ergebnis zu kommen, wird die Schrittweite zwischen den");
        startInfo.addInNextLine("einzelnen Stützstellen adaptiv bestimmt. Das bedeutet, dass die Intervalle, auf denen der");
        startInfo.addInNextLine("geschätzte Fehler größer als eine zuvor festgelegte Toleranz ist, halbiert werden und die");
        startInfo.addInNextLine("Rechnung rekursiv auf den dadurch entstandenen Teilintervallen wiederholt wird.");
        startInfo.addInNextLine("");
        startInfo.addInNextLine("Zur Berechnung des geschätzten Fehlers bietet sich die Verwendung der");
        startInfo.addInNextLine("Simpsonregel an. Die Simpsonregel ist ebenfalls ein numerisches Quadraturverfahren,");
        startInfo.addInNextLine("besitzt aber eine höhere Ordnung als die Trapezregel. Dadurch nähert sie die exakte Lösung");
        startInfo.addInNextLine("des bestimmten Integrals besser an und eignet sich besonders gut für die Fehlerberechnung ");
        startInfo.addInNextLine("der Trapzregel.");
        startInfo.addInNextLine("Simpsonregel: (b-a)/6 * (f(a) +  4 * f((b + a)/2) + f(b))");
        lang.nextStep();
        startInfo.hide();
        introRect.hide();
        introText.hide();
    }

    private void showSourceCode() {
        sourcePrimitives = new Primitive[2];
        RectProperties sourceRect = new RectProperties();
        sourceRect.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
        sourceRect.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        sourceRect.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 500);

        sourcePrimitives[0] = source = lang.newSourceCode(new Coordinates(750, 150), "source", defaultTickDuration, sourceProperties);

        source.addCodeLine("private double quadrature(Function f, double a, double b, double tolerance) {",  null, 0, defaultTickDuration);
        source.addCodeLine("double h = (b - a);",  null, 1, defaultTickDuration);
        source.addCodeLine("double trapezoid = h/2 * (f.eval(a) + f.eval(b));",  null, 1, defaultTickDuration);
        source.addCodeLine("double simpson =  h/6 * (f.eval(a) + 4 * f.eval((b + a)/2) + f.eval(b));",  null, 1, defaultTickDuration);
        source.addCodeLine("", null, 1, defaultTickDuration);
        source.addCodeLine("if (Math.abs(trapezoid - simpson) >= tolerance * h) {",  null, 1, defaultTickDuration);
        source.addCodeLine("double trapezoid1 = quadrature(f, a, (b + a)/2, tolerance);",  null, 2, defaultTickDuration);
        source.addCodeLine("double trapezoid2 = quadrature(f, (b + a)/2, b, tolerance);",  null, 2, defaultTickDuration);
        source.addCodeLine("return trapezoid1 + trapezoid2;",  null, 2, defaultTickDuration);
        source.addCodeLine("}",  null, 1, defaultTickDuration);
        source.addCodeLine("return trapezoid;",  null, 1, defaultTickDuration);
        source.addCodeLine("}",  null, 0, defaultTickDuration);

        sourcePrimitives[1] = lang.newRect(new Offset(-5, -5, "source", AnimalScript.DIRECTION_NW), new Offset(5, 5, "source", AnimalScript.DIRECTION_SE), "sourceRect", defaultTickDuration, sourceRect);
    }

    private void showInfoBox() {
        infoPrimitives = new Primitive[42];
        // Headline and area
        TextProperties infoProps = new TextProperties();
        infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));
        infoPrimitives[0] = lang.newText(new Coordinates(750,430),"Info", "infoLabel", defaultTickDuration, infoProps);

        RectProperties infoRect = new RectProperties();
        infoRect.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
        infoRect.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        infoRect.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 500);
        infoPrimitives[1] = lang.newRect(new Coordinates(745, 450), new Coordinates(1273, 750),"infoRectBorder", defaultTickDuration, infoRect);

        // create infoboxes
        TextProperties headlineProp = new TextProperties();
        headlineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 12));
        TextProperties numberProp = new TextProperties();
        numberProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

        // col 1
        infoPrimitives[2] = lang.newRect(new Offset(30, 25, "infoRectBorder", AnimalScript.DIRECTION_NW), new Offset(80, 75, "infoRectBorder", AnimalScript.DIRECTION_NW),"infoRec1",defaultTickDuration);
        infoPrimitives[3] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec1", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec1", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[4] =  lang.newText(new Offset(-24,2, "infoRec1", AnimalScript.DIRECTION_N), "Aufrufe", "", defaultTickDuration, headlineProp);
        infoPrimitives[5] =  recNr = lang.newText(new Offset(-15, -21, "infoRec1", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        infoPrimitives[6] = lang.newRect(new Offset(0, 50, "infoRec1", AnimalScript.DIRECTION_SW), new Offset(0, 100, "infoRec1", AnimalScript.DIRECTION_SE),"infoRec5",defaultTickDuration);
        infoPrimitives[7] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec5", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec5", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[8] = lang.newText(new Offset(-10,2, "infoRec5", AnimalScript.DIRECTION_N), "Tol", "", defaultTickDuration, headlineProp);
        infoPrimitives[9] = tolerance = lang.newText(new Offset(-15, -21, "infoRec5", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        infoPrimitives[10] =  lang.newRect(new Offset(0, 50, "infoRec5", AnimalScript.DIRECTION_SW), new Offset(0, 100, "infoRec5", AnimalScript.DIRECTION_SE),"infoRec9",defaultTickDuration);
        infoPrimitives[11] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec9", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec9", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[12] = lang.newText(new Offset(-16,2, "infoRec9", AnimalScript.DIRECTION_N), "Trap1", "", defaultTickDuration, headlineProp);
        infoPrimitives[13] = integralOne = lang.newText(new Offset(-20, -21, "infoRec9", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        // col 2
        infoPrimitives[14] = lang.newRect(new Offset(50, 0, "infoRec1", AnimalScript.DIRECTION_NE), new Offset(100, 0, "infoRec1", AnimalScript.DIRECTION_SE),"infoRec2",defaultTickDuration);
        infoPrimitives[15] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec2", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec2", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[16] = lang.newText(new Offset(-5, 5, "infoRec2", AnimalScript.DIRECTION_N), "a", "", defaultTickDuration, headlineProp);
        infoPrimitives[17] = a = lang.newText(new Offset(-20, -21, "infoRec2", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        infoPrimitives[18] = lang.newRect(new Offset(0, 50, "infoRec2", AnimalScript.DIRECTION_SW), new Offset(0, 100, "infoRec2", AnimalScript.DIRECTION_SE),"infoRec6",defaultTickDuration);
        infoPrimitives[19] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec6", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec6", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[20] = lang.newText(new Offset(-20,2, "infoRec6", AnimalScript.DIRECTION_N), "Trapez", "", defaultTickDuration, headlineProp);
        infoPrimitives[21] = trapeze = lang.newText(new Offset(-20, -21, "infoRec6", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        infoPrimitives[22] = lang.newRect(new Offset(0, 50, "infoRec6", AnimalScript.DIRECTION_SW), new Offset(0, 100, "infoRec6", AnimalScript.DIRECTION_SE),"infoRec10",defaultTickDuration);
        infoPrimitives[23] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec10", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec10", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[24] = lang.newText(new Offset(-16,2, "infoRec10", AnimalScript.DIRECTION_N), "Trap2", "", defaultTickDuration, headlineProp);
        infoPrimitives[25] = integralTwo = lang.newText(new Offset(-20, -21, "infoRec10", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        // col 3
        infoPrimitives[26] = lang.newRect(new Offset(50, 0, "infoRec2", AnimalScript.DIRECTION_NE), new Offset(100, 0, "infoRec2", AnimalScript.DIRECTION_SE),"infoRec3",defaultTickDuration);
        infoPrimitives[27] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec3", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec3", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[28] = lang.newText(new Offset(-5, 5, "infoRec3", AnimalScript.DIRECTION_N), "b", "", defaultTickDuration, headlineProp);
        infoPrimitives[29] = b = lang.newText(new Offset(-20, -21, "infoRec3", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        infoPrimitives[30] = lang.newRect(new Offset(0, 50, "infoRec3", AnimalScript.DIRECTION_SW), new Offset(0, 100, "infoRec3", AnimalScript.DIRECTION_SE),"infoRec7",defaultTickDuration);
        infoPrimitives[31] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec7", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec7", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[32] = lang.newText(new Offset(-24,2, "infoRec7", AnimalScript.DIRECTION_N), "Simpson", "", defaultTickDuration, headlineProp);
        infoPrimitives[33] = simpson = lang.newText(new Offset(-20, -21, "infoRec7", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        infoPrimitives[34] = lang.newRect(new Offset(0, 50, "infoRec7", AnimalScript.DIRECTION_SW), new Offset(0, 100, "infoRec7", AnimalScript.DIRECTION_SE),"infoRec11",defaultTickDuration);
        infoPrimitives[35] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec11", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec11", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[36] = lang.newText(new Offset(-25,2, "infoRec11", AnimalScript.DIRECTION_N), "Integr.", "", defaultTickDuration, headlineProp);
        infoPrimitives[37] = integral = lang.newText(new Offset(-20, -21, "infoRec11", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        // col 4
        infoPrimitives[38] = lang.newRect(new Offset(50, 0, "infoRec3", AnimalScript.DIRECTION_NE), new Offset(100, 0, "infoRec3", AnimalScript.DIRECTION_SE),"infoRec4",defaultTickDuration);
        infoPrimitives[39] = lang.newPolyline(new Node[]{new Offset(0, 25, "infoRec4", AnimalScript.DIRECTION_NW), new Offset(0, 25, "infoRec4", AnimalScript.DIRECTION_NE)}, "", defaultTickDuration);
        infoPrimitives[40] = lang.newText(new Offset(-5, 5, "infoRec4", AnimalScript.DIRECTION_N), "h", "", defaultTickDuration, headlineProp);
        infoPrimitives[41] = h = lang.newText(new Offset(-20, -21, "infoRec4", AnimalScript.DIRECTION_S), "", "",  defaultTickDuration, numberProp);

        infoBox = new InfoBox(lang, new Offset(55, 0, "infoRec7", AnimalScript.DIRECTION_NE), 7, "Legende");
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add("");
        tmp.add("Aufrufe = Rekursionsaufrufe");
        tmp.add("Tol = Toleranz");
        tmp.add("Trapez = trapezoid");
        tmp.add("Trap1 = trapezoid1");
        tmp.add("Trap2 = trapezoid2");
        tmp.add("Intgr. = trapezoid1 + trapezoid2");
        infoBox.setText(tmp);
    }

    private void drawAxis(Function f, double a, double b) {
        // Properties
        PolylineProperties axisProperties = new PolylineProperties();
        axisProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        axisProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

        for (int i = 0; i <= width; i++) {
            double tmp = f.eval(a + i /scaleX);
            if(tmp > max)
                max = tmp;
            if(tmp < min)
                min = tmp;
        }

        scaleY = 500 / (max - min);
        double xAxisScaling = (b - a) / 10;
        double yAxisScaling = (max - min) / 10;

        // Coordinate calculation for the x and y axis
        if (min >= 0)
            yCordForXAxis = 700;
        else if (max < 0)
            yCordForXAxis = 200;
        else
            yCordForXAxis = (int) (Math.abs(max) * scaleY + 200);

        int xCordForYAxis;
        if ((a >= 0 && b >= 0) || (a < 0 && b < 0))
            xCordForYAxis = 50;
        else
            xCordForYAxis = (int) (Math.abs(a) * scaleX + 100);

        // X - Axis
        PolylineProperties scalingProp = new PolylineProperties();
        scalingProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);

        lang.newPolyline(new Coordinates[]{new Coordinates(50, yCordForXAxis), new Coordinates(650, yCordForXAxis)},"xAxis", defaultTickDuration, axisProperties);
        lang.newText(new Coordinates(650, yCordForXAxis + 5), "X", "", defaultTickDuration);

        double j = a;
        for (int i = 50; i < 650; i += 50) {
            lang.newPolyline(new Coordinates[] {new Coordinates(i, yCordForXAxis + 3), new Coordinates(i, yCordForXAxis - 3)}, "", defaultTickDuration, scalingProp);
            if (i >= 100) {
                if (xCordForYAxis != i)
                    lang.newText(new Coordinates(i, yCordForXAxis + 5), String.valueOf(numberFormat.format(j)), "", defaultTickDuration);
                j += xAxisScaling;
            }
        }

        // Y - Axis
        lang.newPolyline(new Coordinates[]{new Coordinates(xCordForYAxis, 750), new Coordinates(xCordForYAxis, 150)}, "yAxis", defaultTickDuration, axisProperties);
        lang.newText(new Coordinates(xCordForYAxis + 10, 150), "Y", "", defaultTickDuration);
        j = max;
        for (int i = 200; i < 750; i += 50) {
            lang.newPolyline(new Coordinates[] {new Coordinates(xCordForYAxis -3, i), new Coordinates(xCordForYAxis + 3, i)}, "", defaultTickDuration, scalingProp);
            if (i >= 100) {
                lang.newText(new Coordinates(xCordForYAxis + 6, i - 9), String.valueOf(numberFormat.format(j)), "", defaultTickDuration);
                j -= yAxisScaling;
            }
        }
    }

    private void drawGraph(Function f, double a) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i <= width; i++) {
            nodes.add(new Coordinates(i + x_offset, (int) Math.round(- ((f.eval(a + i / scaleX) - max) * scaleY)) + y_offset));
        }

        lang.newPolyline(nodes.toArray(new Node[nodes.size()]),"graph", defaultMsDuration, graphProperties);
    }

    private void drawInitialInterval(Function f, double a, double b) {
        TextProperties textProp = new TextProperties();
        textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, startIntervall.get(AnimationPropertiesKeys.COLOR_PROPERTY));

        left = lang.newPolyline(new Node[]{new Coordinates((int) Math.round((a - a) * scaleX + x_offset), (int) Math.round(- ((f.eval(a) - max) * scaleY)) + y_offset),
                new Coordinates((int) Math.round((a - a) * scaleX + x_offset),yCordForXAxis)},"intA", new MsTiming(250), startIntervall);
        leftLabel = lang.newText(new Coordinates((int) Math.round((a - a) * scaleX + x_offset - 10), yCordForXAxis + 20),"a", "a", new MsTiming(250), textProp);
        right = lang.newPolyline(new Node[]{new Coordinates((int) Math.round((b - a) * scaleX + x_offset), (int) Math.round(- ((f.eval(b) - max) * scaleY)) + y_offset),
                new Coordinates((int) Math.round((b - a) * scaleX + x_offset),yCordForXAxis)},"intB", new MsTiming(250), startIntervall);
        rightLabel = lang.newText(new Coordinates((int) Math.round((b - a) * scaleX + x_offset + 10), yCordForXAxis - 20),"b", "b", new MsTiming(250), textProp);
    }

    private Polyline[] drawInterval(Function f, double a, double b) {
        Polyline[] polylines = new Polyline[2];

        polylines[0] = lang.newPolyline(new Node[]{new Coordinates((int) Math.round((a - xMin) * scaleX + x_offset), (int) Math.round(- ((f.eval(a) - max) * scaleY)) + y_offset),
                new Coordinates((int) Math.round((a - xMin) * scaleX + x_offset),yCordForXAxis)},"intA", new MsTiming(250), currentIntervall);

        polylines[1] = lang.newPolyline(new Node[]{new Coordinates((int) Math.round((b - xMin) * scaleX + x_offset), (int) Math.round(- ((f.eval(b) - max) * scaleY)) + y_offset),
                new Coordinates((int) Math.round((b - xMin) * scaleX + x_offset),yCordForXAxis)},"intB", new MsTiming(250), currentIntervall);

        return polylines;
    }

    private generators.maths.trapezoidhelpers.IntegralResult quadrature(Function f, double a, double b, double tolerance) {
        count++;
        source.highlight(0);
        this.a.setText(numberFormat.format(a), defaultTickDuration, defaultMsDuration);
        this.a.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);
        this.b.setText(numberFormat.format(b), defaultTickDuration, defaultMsDuration);
        this.b.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);
        this.recNr.setText(String.valueOf(count), defaultTickDuration, defaultMsDuration);
        this.recNr.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);
        Polyline[] tmpInt = drawInterval(f, a, b);

        lang.nextStep();
        this.a.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);
        this.b.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);
        this.recNr.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);

        source.unhighlight(0);
        source.highlight(1);
        double h = (b - a);
        this.h.setText(numberFormat.format(h), defaultTickDuration, defaultMsDuration);
        this.h.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);

        lang.nextStep();
        this.h.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);

        source.unhighlight(1);
        source.highlight(2);
        double trapezoid = h/2 * (f.eval(a) + f.eval(b));
        this.trapeze.setText(numberFormat.format(trapezoid), defaultTickDuration, defaultMsDuration);
        this.trapeze.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);

        IntegralResult integralResult = new IntegralResult(trapezoid, new double[]{a, b});

        lang.nextStep();
        this.trapeze.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);

        source.unhighlight(2);
        source.highlight(3);
        double simpson =  h/6 * (f.eval(a) + 4 * f.eval((b + a)/2) + f.eval(b));
        this.simpson.setText(numberFormat.format(simpson), defaultTickDuration, defaultMsDuration);
        this.simpson.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);

        lang.nextStep();
        this.simpson.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);

        source.unhighlight(3);
        source.highlight(5);
        lang.nextStep();

        if (Math.abs(trapezoid - simpson) >= tolerance * h) {
            source.unhighlight(5);
            source.highlight(6);
            lang.nextStep();

            tmpInt[0].hide();
            tmpInt[1].hide();
            source.unhighlight(6);
            IntegralResult trapezoid1 = quadrature(f, a, (b + a)/2,  tolerance);
            this.integralOne.setText(numberFormat.format(trapezoid1.getValue()), defaultTickDuration, defaultMsDuration);
            this.integralOne.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);

            source.highlight(7);
            lang.nextStep();
            this.integralOne.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);

            source.unhighlight(7);
            IntegralResult trapezoid2 = quadrature(f, (b + a)/2, b, tolerance);
            this.integralTwo.setText(numberFormat.format(trapezoid2.getValue()), defaultTickDuration, defaultMsDuration);
            this.integralTwo.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);

            source.highlight(8);
            this.integral.setText(numberFormat.format(trapezoid1.getValue() + trapezoid2.getValue()), defaultTickDuration, defaultMsDuration);
            this.integral.changeColor(AnimalScript.COLORCHANGE_COLOR, infoboxHighlighting, defaultTickDuration, defaultMsDuration);
            lang.nextStep();

            this.integralTwo.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);
            this.integral.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, defaultTickDuration, defaultMsDuration);
            source.unhighlight(8);

            // merge intervals
            double[] interval = new double[trapezoid1.getNodes().length + trapezoid2.getNodes().length -1];
            System.arraycopy(trapezoid1.getNodes(), 0, interval, 0, trapezoid1.getNodes().length);
            System.arraycopy(trapezoid2.getNodes(), 1, interval, trapezoid1.getNodes().length, trapezoid2.getNodes().length -1);
            return new IntegralResult(trapezoid1.getValue() + trapezoid2.getValue(), interval);
        }

        source.unhighlight(5);
        source.highlight(10);
        tmpInt[0].changeColor(AnimalScript.COLORCHANGE_COLOR, finishedIntervall, defaultTickDuration, defaultMsDuration);
        tmpInt[1].changeColor(AnimalScript.COLORCHANGE_COLOR, finishedIntervall, defaultTickDuration, defaultMsDuration);
        lang.nextStep();
        source.unhighlight(10);
        return integralResult;
    }

    private void startAnimation(Function f, double a, double b, double tolerance, Function df) {
        this.tolerance.setText(String.format("%1.0e", tolerance), defaultTickDuration, defaultMsDuration);
        left.hide();
        right.hide();
        leftLabel.hide();
        rightLabel.hide();
        IntegralResult result = quadrature(f, a, b, tolerance);

        lang.nextStep("Result");

        // hide source
        Arrays.stream(sourcePrimitives).forEach(Primitive::hide);
        infoBox.hide();

        // move info box and convert to result box
        Arrays.stream(infoPrimitives).forEach(primitive -> primitive.moveBy("translate", 0, -280, defaultTickDuration, defaultMsDuration));

        // change and move texts
        Text tmp = (Text) infoPrimitives[0];
        tmp.setText("Result", defaultTickDuration, defaultMsDuration);

        tmp = (Text) infoPrimitives[40];
        tmp.moveBy("translate", -18, 0, defaultTickDuration, defaultMsDuration);
        tmp.setText("Stützst", defaultTickDuration, defaultMsDuration);
        h.setText(numberFormat.format(result.getNodes().length), defaultTickDuration, defaultMsDuration);
        this.h.changeColor(AnimalScript.COLORCHANGE_COLOR, resultNodes, defaultTickDuration, new MsTiming(650));
        this.a.changeColor(AnimalScript.COLORCHANGE_COLOR, resultColorA, defaultTickDuration, new MsTiming(650));
        this.b.changeColor(AnimalScript.COLORCHANGE_COLOR, resultColorB, defaultTickDuration, new MsTiming(650));
        this.recNr.changeColor(AnimalScript.COLORCHANGE_COLOR, resultColorCalls, defaultTickDuration, new MsTiming(650));

        tmp = (Text) infoPrimitives[8];
        tmp.moveBy("translate", -9, 0, defaultTickDuration, defaultMsDuration);
        tmp.setText("Result", defaultTickDuration, defaultMsDuration);
        this.tolerance.moveBy("translate", -5, 0, defaultTickDuration, defaultMsDuration);
        this.tolerance.setText(String.valueOf(numberFormat.format(result.getValue())),defaultTickDuration, defaultMsDuration);
        this.tolerance.changeColor(AnimalScript.COLORCHANGE_COLOR, resultColorResult, defaultTickDuration, new MsTiming(650));

        tmp = (Text) infoPrimitives[20];
        tmp.moveBy("translate", 5, 0, defaultTickDuration, defaultMsDuration);
        tmp.setText("Exact", defaultTickDuration, defaultMsDuration);
        trapeze.setText(String.valueOf(numberFormat.format(calcExact(df, a, b))),defaultTickDuration, defaultMsDuration);
        this.trapeze.changeColor(AnimalScript.COLORCHANGE_COLOR, resultExact, defaultTickDuration, new MsTiming(650));

        tmp = (Text) infoPrimitives[32];
        tmp.moveBy("translate", 5, 0, defaultTickDuration, defaultMsDuration);
        tmp.setText("Fehler", defaultTickDuration, defaultMsDuration);
        simpson.moveBy("translate", -3, 0, defaultTickDuration, defaultMsDuration);
        simpson.setText(String.valueOf(String.format("%#.0e", Math.abs(result.getValue() - calcExact(df, a, b)))),defaultTickDuration, defaultMsDuration);
        this.simpson.changeColor(AnimalScript.COLORCHANGE_COLOR, resultError, defaultTickDuration, new MsTiming(650));

        // hide unnecessary stuff
        int j = 0;
        for (int i = 10; i < 38; i++) {
            infoPrimitives[i].hide();
            j++;
            if(j == 4) {
                i += 8;
                j = 0;
            }
        }

        // increase rec
        Rect border = (Rect) infoPrimitives[1];
        RectProperties borderProps = border.getProperties();
        border.hide();
        lang.newRect(new Coordinates(745, 170), new Coordinates(1273, 800), "", defaultTickDuration, borderProps);

        // Update some values
        this.a.setText(String.valueOf(a),defaultTickDuration, defaultMsDuration);
        this.b.setText(String.valueOf(b),defaultTickDuration, defaultMsDuration);
        recNr.setText(String.valueOf(count),defaultTickDuration, defaultMsDuration);

        lang.nextStep();

        MultipleChoiceQuestionModel frage1 = new MultipleChoiceQuestionModel("Frage1");
        frage1.setNumberOfTries(1);
        frage1.setPrompt("Welches Problem versucht man mit durch die Anwendung einer nummerischen Quadratur zu lösen ?");
        frage1.addAnswer("Es wird versucht eine Funktion anhand von Stützstellen anzunähern.", 0,
                "Diese Antwort ist leider falsch. Nummerische Quadraturen nähern ein bestimmtes Integral an.");
        frage1.addAnswer("Die Ableitung der Funktion wird bestimmt.", 0,
                "Diese Antwort ist leider falsch. Nummerische Quadraturen nähern ein bestimmtes Integral an.");
        frage1.addAnswer("Das bestimmte Integral einer Funktion wird nummerisch bestimmt.", 1, "Richtig, gut gemacht!");

        lang.addMCQuestion(frage1);

        MultipleChoiceQuestionModel frage2 = new MultipleChoiceQuestionModel("Frage2");
        frage2.setNumberOfTries(1);
        frage2.setPrompt("Wieso eignet sich die Simpsonregel zur Fehlerabschätzung der Trapezregel ?");
        frage2.addAnswer("Weil die Berechnung der Simpsonregel wesentlich schneller ist.", 0,
                "Diese Antwort ist leider falsch. Die Simpsonregel ist aufgrund ihrer höheren Ordnung genauer und eignet sich deshalb für die Fehlerabschätzung.");
        frage2.addAnswer("Aufgrund der höheren Ordnung der Simpsonregel führt diese zu genaueren Ergebnissen.", 1,
                "Richtig, gut gemacht!");
        frage2.addAnswer("Die Simpsonregel liefert ein analytisches Ergebnis für ein bestimmtes Integral.", 0,
                "Dies ist leider falsch. Auch die Simpsonregel ist ein nummerisches Verfahren. Doch aufgrund ihrer höheren " +
                        "Ordnung genauer und eignet sich deshalb für die Fehlerabschätzung.");

        lang.addMCQuestion(frage2);

        MultipleChoiceQuestionModel frage3 = new MultipleChoiceQuestionModel("Frage3");
        frage3.setNumberOfTries(1);
        frage3.setPrompt("Was ist das Hauptmerkmal von adaptiven  Verfahren bei nummerischen Quadraturen ?");
        frage3.addAnswer("Sie sind wesentlich schneller als nicht adaptive Verfahren.", 0,
                "Diese Antwort ist leider falsch. Adaptive Verfahren passen die Schrittweite an den Funktionsverlauf an und führen so zu genaueren Annäherungen der Ergebnissen.");
        frage3.addAnswer("Sie führen durch eine ständige Anpassung der Schrittweite an den Funktionsverlauf zu einem genaueren Ergebnis", 1,
                "Richtig, gut gemacht!");
        frage3.addAnswer("Sie ermöglichen es eine analytische Lösung des Problems zu berechnen.", 1,
                "Diese Antwort ist leider falsch. Adaptive Verfahren passen die Schrittweite an den Funktionsverlauf an und führen so zu genaueren Annäherungen der Ergebnissen.");

        lang.addMCQuestion(frage3);

        lang.nextStep("Fragen");

        // Initiate final screen
        InfoBox legende = new InfoBox(lang, new Coordinates(1075, 300), 2 ,"Legende");
        ArrayList<String> leg = new ArrayList<>();
        leg.add("");
        leg.add("Stützst = Anzahl der Stützstellen.");
        legende.setText(leg);

        // Result Screen
        TextProperties tp = new TextProperties();
        tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        lang.newText(new Coordinates(775, 370), "Fazit", "finishText", new TicksTiming(0), tp);

        tp = new TextProperties();
        tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
        FormattedText finishInfo = new FormattedText("finishInfo", lang, new Offset(4, 2, "finishText", AnimalScript.DIRECTION_SW), tp, true, 0.2D, 0.2D);
        finishInfo.beginItemize(FormattedText.ItemForm.CIRCLE, 20, Color.BLACK);
        finishInfo.addAsNewPar("Ergebnis", true, false);
        finishInfo.addInNextLine("Die Abweichung vom Ergebnis der nummerischen Annäherung des");
        finishInfo.addInNextLine("bestimmten Integrals von " + functionString + " auf dem");
        finishInfo.addInNextLine("angegebenen Intervall entspricht in etwa der festgelegten Toleranz.");
        finishInfo.addInNextLine(numberFormat.format(Math.abs(result.getValue() - calcExact(df, a, b))), resultError);
        finishInfo.addAsNewPar("Anzahl der Stützstellen", true, false);
        finishInfo.addInNextLine("Die Anzahl der Stützstellen ist von der Genauigkeit der zuvor festgelegten");
        finishInfo.addInNextLine("Toleranz abhängig. Verfeinert man z.B. die Toleranz um eine Stelle");
        finishInfo.addInNextLine("würde man bei der selben Funktion auf dem angegebenen Intervall");
        finishInfo.addInNextLine(String.valueOf(quad(f, a, b, Math.pow(10, tol - 1)/(b-a)).getNodes().length), resultNodes);
        finishInfo.add("Stützstellen erhalten.");
        finishInfo.addAsNewPar("Schrittweite des Verfahrens", true, false);
        finishInfo.addInNextLine("Die Schrittweite adaptiver Verfahren variiert je nach Funktionsverlauf.");
        finishInfo.addInNextLine("So ergibt sich beispielsweise bei gleichmäßigen Funktionen eine größere");
        finishInfo.addInNextLine("Schrittweite und somit weniger Stützstellen als bei stark schwankenden");
        finishInfo.addInNextLine("Funktionen, wobei sich bei letzteren die Schrittweite stets ändert und");
        finishInfo.addInNextLine("dadurch besser an den Funktionsverlauf anpasst wird. Vergleichen sie");
        finishInfo.addInNextLine("hierzu doch einfach mal die Funktion f(x) = x und g(x) = sqrt(x) auf dem");
        finishInfo.addInNextLine("Intervall [0, 1] miteinander.");
        finishInfo.endItemize();
    }

    private generators.maths.trapezoidhelpers.IntegralResult quad(Function f, double a, double b, double tolerance) {
        double h = (b - a);
        double trapezoid = h/2 * (f.eval(a) + f.eval(b));
        IntegralResult integralResult = new IntegralResult(trapezoid, new double[]{a, b});
        double simpson =  h/6 * (f.eval(a) + 4 * f.eval((b + a)/2) + f.eval(b));

        if (Math.abs(trapezoid - simpson) >= tolerance * h) {
            IntegralResult trapezoid1 = quad(f, a, (b + a)/2,  tolerance);
            IntegralResult trapezoid2 = quad(f, (b + a)/2, b, tolerance);

            double[] interval = new double[trapezoid1.getNodes().length + trapezoid2.getNodes().length -1];
            System.arraycopy(trapezoid1.getNodes(), 0, interval, 0, trapezoid1.getNodes().length);
            System.arraycopy(trapezoid2.getNodes(), 1, interval, trapezoid1.getNodes().length, trapezoid2.getNodes().length -1);
            return new IntegralResult(trapezoid1.getValue() + trapezoid2.getValue(), interval);
        }
        return integralResult;
    }



    private double calcExact(Function f, double a, double b) {
        return f.eval(b) - f.eval(a);
    }

    /*
           Below generated stuff
     */
    public void init(){
        lang = new AnimalScript("Adaptive Quadratur mit Trapezregel", "Christian Hack", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        double a = (double)primitives.get("a");
        double b = (double)primitives.get("b");
        double toleranz = (double)primitives.get("Toleranz");
        resultExact = (Color)primitives.get("Result Farbe Exact");
        resultNodes = (Color)primitives.get("Result Farbe Stützstellen");
        resultError = (Color)primitives.get("Result Farbe Fehler");
        startIntervall = (PolylineProperties)props.getPropertiesByName("Start Intervall");
        sourceProperties = (SourceCodeProperties)props.getPropertiesByName("Source");
        graphProperties = (PolylineProperties)props.getPropertiesByName("Graph");
        infoboxHighlighting = (Color)primitives.get("Infobox highlighting");
        currentIntervall = (PolylineProperties)props.getPropertiesByName("Aktuelles Intervall");
        resultColorB = (Color)primitives.get("Result Farbe b");
        resultColorResult = (Color)primitives.get("Result Farbe Result");
        resultColorA = (Color)primitives.get("Result Farbe a");
        funktion = (Integer)primitives.get("Funktion");
        finishedIntervall = (Color)primitives.get("Fertiges Intervall");
        resultColorCalls = (Color)primitives.get("Result Farbe Aufrufe");
        //
        tol = toleranz;
        executeAnimation(a, b, toleranz);

        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Adaptive Quadratur mit Trapezregel";
    }

    public String getAlgorithmName() {
        return "Trapezregel";
    }

    public String getAnimationAuthor() {
        return "Christian Hack";
    }

    public String getDescription(){
        return "Bei der adaptiven Trapezregel handelt es sich um ein numerisches Verfahren zur Annäherung des Integrals einer Funktion f(x) über ein Intervall [a, b]. "
 +"Dabei ersetzt man die Fläche unter dem Funktionsgraphen durch ein oder mehrere Trapeze."
 +"\n"
 +"\n"
 +"Trapezregel: (b-a)/2 * (f(a) + f(b))"
 +"\n"
 +"\n"
 +"Um hierbei auf ein besseres Ergebnis zu kommen, wird die Schrittweite zwischen den einzelnen Stützstellen adaptiv bestimmt. "
 +"Das bedeutet, dass die Intervalle, auf denen der geschätzte Fehler größer als eine zuvor festgelegte Toleranz ist, halbiert werden "
 +"und die Rechnung rekursiv auf den dadurch entstandenen Teilintervallen wiederholt wird."
 +"\n"
 +"\n"
 +"Zur Berechnung des geschätzten Fehlers bietet sich die Verwendung der Simpsonregel an. Die Simpsonregel ist ebenfalls ein numerisches Quadraturverfahren, "
 +"besitzt aber eine höhere Ordnung als die Trapezregel. Dadurch nähert sie die exakte Lösung des bestimmten Integrals besser an und eignet sich besonders gut für die"
 +"Fehlerberechnung der Trapzregel."
 +"\n"
 +"\n"
 +"Simpsonregel: (b-a)/6 * (f(a) +  4 * f((b + a)/2) + f(b))"
 +"\n"
 +"\n"
 +"Parameter:"
 +"\n"
 +"Bei diesem Generator sind die folgenden Parameter einstellbar."
 +"\n"
 +"\n"
 +"A = linke Schranke des Intervalls."
 +"\n"
 +"B = rechte Schranke des Intervalls."
 +"\n"
 +"Toleranz = die zu Verwendende Toleranz. "
 +"\n"
 +"Bitte beachten sie, dass dieses Verfahren je nach Toleranz andere Ergebnisse liefert. Außerdem sollten sie ihre Toleranz in Abhängigkeit von der Funktion und des Intervalls wählen. "
 +"Eine zu feine Toleranz (in der Regel kleiner als 10^(-3)) führt zur Generierung von sehr sehr vielen Animationsschritten und einer langen Ladezeit bei Animal. "
 +"Um eine einigermaßen vernünftige Toleranz zu garantieren wird diese intern durch 10^(x)/(b - a) berechnet. "
 +"Deshalb sind Sie hier lediglich auf die Angabe des Exponenten beschränkt."
 +"\n"
 +"\n"
 +"Funktion = Zu verwendende Funktion. Geben sie hier bitte die ID einer der vorgeschlagenen Funktionen an (0 - 4)."
 +"\n"
 +"Durch die Eingabe einer ungültigen ID wird die Standardfunktion gewählt:"
 +"\n"
 +"\n"
 +"0 = f(x) = x^2 - 2"
 +"\n"
 +"1 = f(x) = x^3"
 +"\n"
 +"2 = f(x) = atan(x)"
 +"\n"
 +"3 = f(x) = x"
 +"\n"
 +"4 = f(x) = x^4 - 1.25*x^2 + 0.25"
 +"\n"
 +"default = f(x) = sqrt(x), für diese Funktion bitte nur ein positives Intervall angeben. Z.b. [0, 1]. "
 +"\n"
 +"\n"
 +"Des weiteren lassen sich die folgenden Eigenschaften einstellen:"
 +"\n"
 +"\n"
 +"Farbe des Graphen."
 +"\n"
 +"Farbe des Startintervalls."
 +"\n"
 +"Farbe des momentan betrachteten Intervalls."
 +"\n"
 +"Farbe der fertig berechneten Intervalle."
 +"\n"
 +"Highlightfarbe des Quellcodes."
 +"\n"
 +"Hightlightfarbe der Infobox."
 +"\n"
 +"Farbe der Aufrufe, a, b, Stützstellen bei der Resultbox."
 +"\n"
 +"Farbe des Ergebnisses und der exakten Lösung."
 +"\n"
 +"Farbe des errechneten Fehlers. "
 +"\n"
 +" ";
    }

    public String getCodeExample(){
        return "private double quadrature(Function f, double a, double b, double tolerance)  {"
 +"\n"
 +"    double h = (b - a);"
 +"\n"
 +"    double trapezoid = h/2 * (f.eval(a) + f.eval(b));"
 +"\n"
 +"    double simpson =  h/6 * (f.eval(a) + 4 * f.eval((b + a)/2) + f.eval(b));"
 +"\n"
 +"	"
 +"\n"
 +"    if (Math.abs(trapezoid - simpson) >= tolerance * h)  {"
 +"\n"
 +"        double trapezoid1 = quadrature(f, a, (b + a)/2, tolerance);"
 +"\n"
 +"        double trapezoid2 = quadrature(f, (b + a)/2, b, tolerance);"
 +"\n"
 +"        return trapezoid1 + trapezoid2;"
 +"\n"
 +"    }"
 +"\n"
 +"    return trapezoid;"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
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

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        double a = (double)hashtable.get("a");
        double b = (double)hashtable.get("b");
        funktion = (Integer)hashtable.get("Funktion");
        double[] allowed = {0, 1, 2, 3, 4};

        if (!Arrays.stream(allowed).anyMatch(n -> n == funktion))
            return !(a < 0 || b < 0);
        else
            return (b > a);
    }
}