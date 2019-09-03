/*
 * DFTGenerator.java
 * Maximilian Scheid, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.maths.ComplexNumber;

import algoanim.primitives.DoubleArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.LinkedList;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import java.util.Locale;


import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;

public class DFTGenerator implements ValidatingGenerator {

	private Language language;
    private String Signal;
    private TextProperties headlineProperties;
    private TextProperties textProbs;
    private SourceCodeProperties introProps;
    private Variables variables;

    private final double scaleFactorY = 25.0;
    
    int baseLineSignalX = 70;
    int baseLineSignalY = 175;
    int baselineSinosBaseX = baseLineSignalX + 380;
    int baselineSinosBaseY = baseLineSignalY + 4 * (int) scaleFactorY;
    int baseLineProductY = (int) (baselineSinosBaseY * 1.5);
    int kindOfSignal;

    
    
    int axisWidth = 300;

    public void init(){
        
        language = new AnimalScript("Die Diskrete Fourier Transformation", "Maximilian Scheid", 740, 860);
        language.setStepMode(true);
        
        variables = language.newVariables();
        
        
        //won't make this editable, no way to set the font size in the generate window 
        headlineProperties = new TextProperties();
        headlineProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 24));
        
        textProbs = new TextProperties();
        textProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProbs.set(AnimationPropertiesKeys.RIGHT_PROPERTY, false);
        
        introProps = new SourceCodeProperties();
        introProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        introProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
            new Font("SansSerif", Font.PLAIN, 12));
        
        kindOfSignal = 0;
    }

    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        Signal = (String)primitives.get("Signal");

        
        if (Signal.equals("Cosinus") || Signal.equals("Sinus")) {
        	return true;
        } else {
        	throw new IllegalArgumentException("Für Signal können nur 'Sinus' oder 'Cosinus' verwendet werden");
        }
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Signal = (String)primitives.get("Signal");
        textProbs = (TextProperties)props.getPropertiesByName("Text Properties");
        introProps = (SourceCodeProperties)props.getPropertiesByName("Slide Text Properties");
        

        if (Signal.equals("Cosinus")) kindOfSignal = 0;
        if (Signal.equals("Sinus")) kindOfSignal = 1;

        animateDFT(DFTGenerator.sampleCos(8.0), kindOfSignal);

        
        return language.toString();
    }

    public String getName() {
        return "Die Diskrete Fourier Transformation";
    }

    public String getAlgorithmName() {
        return "Diskrete Fourier Transformation";
    }

    public String getAnimationAuthor() {
        return "Maximilian Scheid";
    }

    public String getDescription(){
        return "Dieser Generator animiert die diskrete Fourier Transformation. Um das Input-Signal zu \u00e4ndern, bitte den Wert des Primitives"
        		+ " auf Sinus oder auf Cosinus setzen.";
    }

    public String getCodeExample(){
        return "double[] eightSampleCos = {1.0, 0.707, 0.0, -0.707, -1.0, -0.707, 0.0, 0.707};"
 +"\n"
 +"\n"
 +"double[] frequencies = dft(eightSampleCos};"
 +"\n"
 +"\n"
 +"System.out.println(frequencies);"
 +"\n"
 +"\n"
 +"// prints [0.00000   3.99970   0.00000   0.00030   0.00000   0.00030   0.00000   3.99970]";
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
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public LinkedList<Primitive> drawLineFrom(double[] points, Coordinates StartPoint, Language language,
            double scaleFactorX, double scaleFactorY) {

        if (points.length == 0)
            return null;

        LinkedList<Primitive> lines = new LinkedList<Primitive>();

        Coordinates[] vertices = new Coordinates[points.length + 1];

        for (int i = 0; i < points.length; i++) {
            vertices[i] = new Coordinates((int) (StartPoint.getX() + i * scaleFactorX),
                    (int) (StartPoint.getY() - points[i] * scaleFactorY));
        }

        vertices[points.length] = new Coordinates(StartPoint.getX() + (int) (points.length * scaleFactorX),
                StartPoint.getY() - (int) (points[0] * scaleFactorY));

        lines.add(language.newPolyline(vertices, "null", null));

        return lines;

    }

    private void hideList(LinkedList<Primitive> toHide) {
        for (Primitive primitive : toHide) {
            primitive.hide();
        }
        toHide.clear();
    }

    public static double[] roundDoubleArray(double[] arr) {
        double[] roundedValues = new double[arr.length];
        
        for (int i = 0; i < arr.length; i++) {
            roundedValues[i] = (double)Math.round(arr[i] * 1000.0) / 1000.0;
        }
        
        return roundedValues;
        
        
    }

    public static double[] sampleSin(double numberOfSamples) {
        double[] sampledSin = new double[(int)numberOfSamples];
        
        double delta = 1.0 / numberOfSamples * 2.0 * java.lang.Math.PI;
        double i = 0.0;
        int j = 0;
        
        while (i < 2.0 * java.lang.Math.PI && j < numberOfSamples) {
            sampledSin[j] = (java.lang.Math.sin(i));
            j += 1;
            i += delta;
        }
        
        return sampledSin;
    }
    
    
    public static double[] sampleSinWithFrequency(double frequency,double sampleFrequency) {
        double[] sampledSin = new double[(int)sampleFrequency];
        
        double delta = 1.0 / sampleFrequency * 2.0 * java.lang.Math.PI * frequency;
        double i = 0.0;
        int j = 0;
        
        while (j < sampleFrequency) {
            sampledSin[j] = -1.0 * (java.lang.Math.sin(i));
            j += 1;
            i += delta;
        }
        
        return sampledSin;
    }
    
    public static double[] sampleCos(double numberOfSamples) {
        double[] sampledCos = new double[(int)numberOfSamples];
        
        double delta = 1.0 / numberOfSamples * 2.0 * java.lang.Math.PI;
        double i = 0.0;
        int j = 0;
        
        while (i < 2.0 * java.lang.Math.PI && j < numberOfSamples) {
            sampledCos[j] = (java.lang.Math.cos(i));
            j += 1;
            i += delta;
        }
        
        return sampledCos;
    }
    
    public static double[] sampleCosWithFrequency(double frequency,double sampleFrequency) {
        double[] sampledCos = new double[(int)sampleFrequency];
        
        double delta = 1.0 / sampleFrequency * 2.0 * java.lang.Math.PI * frequency;
        double i = 0.0;
        int j = 0;
        
        while (j < sampleFrequency) {
            sampledCos[j] = (java.lang.Math.cos(i));
            j += 1;
            i += delta;
        }
        
        return sampledCos;
    }
    
    private void createFinalSlide() {
        language.hideAllPrimitives();
        
        Coordinates textbegin = new Coordinates(5, 15);
        language.newText(textbegin, "Zusammenfassung", "null", null, headlineProperties);
        
        SourceCode sc = language.newSourceCode(new Offset(0, 20, textbegin, "SW"), "null", null, introProps);
        sc.addCodeLine("Nun solltest du verstanden haben, wie die diskrete Fourier Transformation funtkioniert", null,0,null);
        sc.addCodeLine("", null,0,null);
        sc.addCodeLine("Allerdings ist diese Art der Berechnung nicht besonders effizient (O(n*n)),", null,0,null);
        sc.addCodeLine("daher gibt es noch die Fast Fourier Transform, die das Ganze in O(n log(n))", null,0,null);
        sc.addCodeLine("schafft.", null,0,null);
        
    }
    
    private void createIntroductionSlides() {

        Coordinates textbegin = new Coordinates(5, 15);
        language.newText(textbegin, "Die Diskrete Fourier Transformation", "null", null, headlineProperties);
        
        SourceCode sc1 = language.newSourceCode(new Offset(0, 20, textbegin, "SW"), "null", null, introProps);
        
        sc1.addCodeLine("Mit der diskreten Fourier Transformation lassen sich die Frequenzspektren", null,0,null);
        sc1.addCodeLine("diskreter Signale ermitteln.", null,0,null);
        sc1.addCodeLine("", null,0,null);
        sc1.addCodeLine("Dieser Generator zeigt, was im Inneren der Diskreten Fourier Transfomation vor sich geht.", null,0,null);
        sc1.addCodeLine("", null,0,null);
                
        Coordinates part2TextBegin = new Coordinates(5, 150);
        language.newText(part2TextBegin, "Eine kurze Erklärung", "null", null, headlineProperties);
        
        SourceCode sc2 = language.newSourceCode(new Offset(0, 20, part2TextBegin, "SW"), "null", null, introProps);
        
        sc2.addCodeLine("Der Input der diskreten Fourier Transformation ist (wie der Name schon vermuten lässt) eine Liste an Werten.", null,0,null);
        sc2.addCodeLine("Diese Werte sind ein mit einer bestimmten Frequenz gesampletes Signal, 2Hz Sample Frequenz", null,0,null);
        sc2.addCodeLine("bedeutet zwei Werte pro Sekunde und zwei Frequenzen mit denen der Input verglichen wird.", null,0,null);
        sc2.addCodeLine("", null,0,null);
        sc2.addCodeLine("Die Wahl der Sampling Frequenz hat Einfluss auf den Output der DFT. Da wir hier aber die DFT veranschaulichen", null,0,null);
        sc2.addCodeLine("wollen, lassen wir die Sampling-Theorie als Übung für den/die LeserIn.", null,0,null);
        sc2.addCodeLine("", null,0,null);
        sc2.addCodeLine("Ein anderer wichtiger Teil der DFT sind die komplexen Zahlen und das Skalarprodukt.", null,0,null);
        sc2.addCodeLine("Die beiden Konzepte sollten bekannt sein, ohne die wird es schwierig die DFT zu verstehen.", null,0,null);
        sc2.addCodeLine("Eine kurze Anmerkung zu den komplexen Zahlen: die Kreise die in diesem Generator zu sehen sind,", null,0,null);
        sc2.addCodeLine("zeigen komplexe Zahlen in der komplexen Ebene.",null,0,null);
        
        language.nextStep();
        language.hideAllPrimitives();
    }

    public void animateDFT(double[] samples, int kindOfSignal) {
        LinkedList<Primitive> hideInNextStep = new LinkedList<Primitive>();
        

        createIntroductionSlides();
        
        int numberSamples = samples.length;

        ArrayProperties arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(237, 173, 59));

        CircleProperties pointprobs = new CircleProperties();
        pointprobs.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        
        language.newText(new Coordinates(5, 10), "Die Diskrete Fourier Transformation", "null", null, headlineProperties);

        PolylineProperties polylineProperties = new PolylineProperties();
        polylineProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

        CircleProperties cp = new CircleProperties();
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);

        double[] highFrequencySample;
        
        switch (kindOfSignal) {
        case 0:
            highFrequencySample = sampleCos(64.0);
            break;
        case 1:
            highFrequencySample = sampleSin(64.0);
            break;
        default:
            return;
        }

        
        TextProperties sampleLabelProbs = new TextProperties();
        textProbs.set(AnimationPropertiesKeys.RIGHT_PROPERTY, true);
        
        Coordinates edge1 = new Coordinates(baseLineSignalX + 50, 35);
        
        language.newText(new Offset(-55, -5, edge1, "W"), "Samples:", "null", null, sampleLabelProbs);
        DoubleArray daArray = language.newDoubleArray(edge1, roundDoubleArray(samples),
                "doubleArray", null, arrayProps);
        daArray.showIndices(false, null, null);

        // sampledSignal
        drawLineFrom(highFrequencySample, new Coordinates(baseLineSignalX, baseLineSignalY), language,(double) axisWidth / highFrequencySample.length, scaleFactorY);
        drawLineFrom(highFrequencySample, new Coordinates(baselineSinosBaseX, baseLineSignalY), language,(double) axisWidth / highFrequencySample.length, scaleFactorY);
        // axes
        Coordinates[] leftAxis = { new Coordinates(baseLineSignalX, baseLineSignalY),
                new Coordinates(baseLineSignalX + axisWidth, baseLineSignalY) };
        Coordinates[] rightAxis = { new Coordinates(baselineSinosBaseX, baseLineSignalY), new Coordinates(baselineSinosBaseX + axisWidth, baseLineSignalY) };

        language.newPolyline(leftAxis, "leftSampleAxis", null);
        language.newPolyline(rightAxis, "rightSampleAxis", null);

        // other axes
        Coordinates[] leftOtherAxis = { new Coordinates(baseLineSignalX, baselineSinosBaseY),
                new Coordinates(baseLineSignalX + axisWidth, baselineSinosBaseY) };
        Coordinates[] rightOtherAxis = { new Coordinates(baselineSinosBaseX, baselineSinosBaseY),
                new Coordinates(baselineSinosBaseX + axisWidth, baselineSinosBaseY) };
        Coordinates[] leftProductAxis = { new Coordinates(baseLineSignalX, baseLineProductY),
                new Coordinates(baseLineSignalX + axisWidth, baseLineProductY) };
        Coordinates[] rightProductAxis = { new Coordinates(baselineSinosBaseX, baseLineProductY),
                new Coordinates(baselineSinosBaseX + axisWidth, baseLineProductY) };

        language.newPolyline(leftOtherAxis, "leftSampleAxis", null);
        language.newPolyline(rightOtherAxis, "rightSampleAxis", null);
        language.newPolyline(leftProductAxis, "leftProductAxis", null);
        language.newPolyline(rightProductAxis, "rightProductAxis", null);
        
        TextProperties axisLabelsProbs = new TextProperties();
        axisLabelsProbs.set(AnimationPropertiesKeys.RIGHT_PROPERTY, false);
        axisLabelsProbs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
        
        language.newText(new Coordinates(5, baseLineSignalY), "Samples", "null", null, axisLabelsProbs);
        language.newText(new Coordinates(baseLineSignalX + axisWidth + 20, baseLineSignalY), "Samples", "null", null, axisLabelsProbs);
        
        language.newText(new Coordinates(5, baseLineProductY), "Product", "null", null, axisLabelsProbs);
        language.newText(new Coordinates(baseLineSignalX + axisWidth + 20, baseLineProductY), "Product", "null", null, axisLabelsProbs);
        
        hideInNextStep.add(language.newText(new Coordinates(5, baselineSinosBaseY), "cos(\u03C6)", "null", null, axisLabelsProbs));
        hideInNextStep.add(language.newText(new Coordinates(baseLineSignalX + axisWidth + 20, baselineSinosBaseY), "-sin(\u03C6)", "null", null, axisLabelsProbs));

        Coordinates[] dots = new Coordinates[numberSamples * 2];

        for (int i = 0; i < numberSamples; i++) {

            dots[i * 2] = new Coordinates((int) (baseLineSignalX + i * axisWidth / numberSamples),
                    (int) (baseLineSignalY - highFrequencySample[(int) (i * highFrequencySample.length / numberSamples)] * scaleFactorY));
            
            dots[i * 2 + 1] = new Coordinates(baselineSinosBaseX + (int) (i * (double) axisWidth / numberSamples),
                    baseLineSignalY - (int) (highFrequencySample[(int) (i * (highFrequencySample.length) / numberSamples)] * scaleFactorY));
            
            language.newCircle(dots[i * 2], 2, "null", null, pointprobs);
            language.newCircle(dots[i * 2 + 1], 2, "null", null, pointprobs);

        }

        Coordinates[] smallAxes = new Coordinates[4 * numberSamples];
        int width_2 = 22;

        for (int i = 0; i < 4 * numberSamples; i += 4) {
            
            int middleX = baselineSinosBaseX + axisWidth + 32;
            int middleY = ((25 + (i / 2) * 45) + (25 + (i / 2 + 1) * (45 - 10))) / 2;
            
            
            Coordinates horizontalLeftPoint = new Coordinates(middleX - width_2, middleY);
            Coordinates horizontalRightPoint = new Coordinates(middleX + width_2, middleY);
            
            Coordinates verticalLeftPoint = new Coordinates(middleX, middleY - width_2);
            Coordinates verticalRightPoint = new Coordinates(middleX, middleY + width_2);
            
            Coordinates[] horizontalVertices = { horizontalLeftPoint, horizontalRightPoint };
            Coordinates[] verticalVertices = { verticalLeftPoint, verticalRightPoint };

            language.newPolyline(horizontalVertices, "null", null);
            language.newPolyline(verticalVertices, "null", null);
            
            TextProperties verySmallLabel = new TextProperties();
            verySmallLabel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
            
            language.newText(new Coordinates(middleX - width_2 + 5, middleY + width_2 + 2), "DFT[" + (i / 4) + "]", "null", null, verySmallLabel);
            
            smallAxes[i] = horizontalLeftPoint;
            smallAxes[i + 1] = horizontalRightPoint;
            smallAxes[i + 2] = verticalLeftPoint;
            smallAxes[i + 3] = verticalRightPoint;

        }
        
        // "source code" - not really source is a mathematical formula
        
        hideInNextStep.add(language.newText(new Offset(-50, 50, edge1 , "S"), "DFT[n] = \u2211 x[n] \u22C5 (cos(\u03C6) - sin(\u03C6)i)", "null", null, headlineProperties));
        
        language.nextStep("Begin with DFT for each sample");
        hideList(hideInNextStep);
        
        double precision = 40.0;
        DecimalFormat dFormat = new DecimalFormat( "#,###,###,##0.000");
        DecimalFormat labelFormat = new DecimalFormat( "#,###,###,##0.0");
        String DFTFormula = "";

        for (int i = 0; i < numberSamples; i++) {

            double numberOfDrawSamples = (i * precision) + numberSamples;

            double[] sampSin = sampleSinWithFrequency(i, numberOfDrawSamples);
            double[] sampCos = sampleCosWithFrequency(i, numberOfDrawSamples);

            double scaleX = axisWidth / numberOfDrawSamples;

            hideInNextStep.addAll(drawLineFrom(sampCos, new Coordinates(baseLineSignalX, baselineSinosBaseY), language,
                    scaleX, scaleFactorY));
            hideInNextStep.addAll(
                    drawLineFrom(sampSin, new Coordinates(baselineSinosBaseX, baselineSinosBaseY), language, scaleX, scaleFactorY));

            double[] parts = new double[numberSamples * 2];

            for (int j = 0; j < numberSamples; j++) {
                hideInNextStep.add(language.newCircle(
                        new Coordinates((int) (baseLineSignalX + j * axisWidth / numberSamples),
                                (int) (baselineSinosBaseY
                                        - sampCos[(int) (j * numberOfDrawSamples / numberSamples)] * scaleFactorY)),
                        2, "null", null, pointprobs));
                ;
                hideInNextStep.add(language.newCircle(
                        new Coordinates((int) (baselineSinosBaseX + j * scaleX * (numberOfDrawSamples / numberSamples)),
                                (int) (baselineSinosBaseY
                                        - sampSin[(int) ((j * (numberOfDrawSamples)) / numberSamples)] * scaleFactorY)),
                        2, "null", null, pointprobs));
                
            }

            for (int j = 0; j < numberSamples; j++) {
                LinkedList<Primitive> hideLines = new LinkedList<Primitive>();
                
                String phi = dFormat.format((double) (i) / (double) samples.length * (double) j * 2.0) + "\u03C0";
                String freq = labelFormat.format((double) i * 2.0) + "\u03C0";
                
                DFTFormula = "DFT[" + i + "] = \u2211 " + dFormat.format(samples[j]) + " \u22C5 (cos(" + phi + ") - sin(" + phi + ")i)";
                
                hideLines.add(language.newText(new Coordinates(5, 75), DFTFormula, "null", null, headlineProperties));
                hideLines.add(language.newText(new Coordinates(5, baselineSinosBaseY), "cos("+freq+")", "null", null, axisLabelsProbs));
                hideLines.add(language.newText(new Coordinates(baseLineSignalX + axisWidth + 20, baselineSinosBaseY), "-sin("+freq+")", "null", null, axisLabelsProbs));
                
                daArray.highlightCell(j, null, null);
                
                Coordinates[] vertices = { dots[j * 2], new Coordinates(
                        (int) (baseLineSignalX + j * scaleX * (numberOfDrawSamples / numberSamples)),
                        (int) (baselineSinosBaseY
                                - sampCos[(int) ((j * (numberOfDrawSamples)) / numberSamples)] * scaleFactorY)) };

                Coordinates[] vertices1 = { dots[j * 2 + 1], new Coordinates(
                        (int) (baselineSinosBaseX + j * scaleX * (numberOfDrawSamples / numberSamples)), (int) (baselineSinosBaseY
                                - sampSin[(int) ((j * (numberOfDrawSamples)) / numberSamples)] * scaleFactorY)) };

                hideLines.add(language.newPolyline(vertices, "null", null));
                hideLines.add(language.newPolyline(vertices1, "null", null));
                
                double productCos = sampCos[(int) ((j * (numberOfDrawSamples)) / numberSamples)] * samples[j];
                double productSin = sampSin[(int) ((j * (numberOfDrawSamples)) / numberSamples)] * samples[j];
                
                Coordinates[] vertices2 = {vertices[1] , new Coordinates(vertices[1].getX(), (int) (baseLineProductY - productCos * scaleFactorY))};
                Coordinates[] vertices3 = {vertices1[1] , new Coordinates(vertices1[1].getX(), (int) (baseLineProductY - productSin * scaleFactorY))};
                
                hideInNextStep.add(language.newCircle(vertices2[1], 2, "null", null, pointprobs));
                hideInNextStep.add(language.newCircle(vertices3[1], 2, "null", null, pointprobs));
                
                hideLines.add(language.newPolyline(vertices2, "null", null));
                hideLines.add(language.newPolyline(vertices3, "null", null));
                
                parts[j * 2] = productCos; //Double.valueOf(dFormat.format(productCos));
                parts[j * 2 + 1] = productSin; //Double.valueOf(dFormat.format(productSin));
                
                StringBuilder formula = new StringBuilder();
                formula.append(dFormat.format(samples[j]));
                formula.append(" * ");
                formula.append(
                        dFormat.format(sampCos[(int) ((j * (numberOfDrawSamples)) / numberSamples)]));
                formula.append(" = ");
                formula.append(dFormat.format(parts[j * 2]));
                hideInNextStep
                        .add(language.newText(new Coordinates(baseLineSignalX + axisWidth / 2, 450 + j * 20), formula.toString(), "null", null, textProbs));
                

                StringBuilder formula1 = new StringBuilder();
                formula1.append(dFormat.format(samples[j]));
                formula1.append(" * ");
                formula1.append(
                        dFormat.format(sampSin[(int) ((j * (numberOfDrawSamples)) / numberSamples)]));
                formula1.append(" = ");
                formula1.append(dFormat.format(parts[j * 2 + 1]));
                
                hideInNextStep
                        .add(language.newText(new Coordinates(baselineSinosBaseX + axisWidth / 2, 450 + j * 20), formula1.toString(), "null", null,textProbs));
                
                if (j == 0) {
                    hideInNextStep
                    .add(language.newText(new Coordinates(baseLineSignalX, 450 + j * 20), "Real-Teil:", "null", null, textProbs));
                    hideInNextStep
                    .add(language.newText(new Coordinates(baseLineSignalX + axisWidth + 30, 450 + j * 20), "Imagin\u00e4r-Teil:", "null", null,textProbs));
                }

                language.nextStep("Calculated the Product for Sample: " + j + " in DFT[" + i + "]");
                daArray.unhighlightCell(j, null, null);
                hideList(hideLines);
            }

            double sum = 0.0;
            double sum1 = 0.0;
            for (int j = 0; j < parts.length / 2; j++) {
                sum += parts[j * 2];
                sum1 += parts[j * 2 + 1];
            }
            
            hideInNextStep.add(language.newText(new Coordinates(baseLineSignalX, 450 + numberSamples * 20),
                    "DFT[" + i + "] = ", "null", null));
            hideInNextStep.add(language.newText(new Coordinates(baseLineSignalX + axisWidth + 30, 450 + numberSamples * 20),
                    "+", "null", null));
            
            String variableName = "\"DFT\u005B"+i+"\u005D\"";
            String variableValue = dFormat.format(sum) + "+" + dFormat.format(sum1) + "i";
            variables.declare("String",variableName , variableValue);
//            variables.set(variableName, variableValue);

            hideInNextStep.add(language.newText(new Coordinates(145, 450 + numberSamples * 20),
                    dFormat.format(sum), "null", null));
            hideInNextStep.add(language.newText(new Coordinates(520, 450 + numberSamples * 20),
                    dFormat.format(sum1), "null", null));

            ComplexNumber result = ComplexNumber.initComplexNumberWithRealAndImaginaryPart(sum, sum1);

            double scaleFactorMiniAxes = 45.0 / numberSamples;

            int originX = smallAxes[i * 4 + 2].getX();
            int originY = smallAxes[i * 4].getY();

            double resultVectorX = result.getAbsolute() * Math.cos(result.getAngle()) * scaleFactorMiniAxes + originX;
            double resultVectorY = originY - result.getAbsolute() * Math.sin(result.getAngle()) * scaleFactorMiniAxes;

            Coordinates[] resultVector = { new Coordinates(originX, originY),
                    new Coordinates((int) resultVectorX, (int) resultVectorY) };
            
            language.newPolyline(resultVector, "null", null, polylineProperties);
            
            if ((int) (result.getAbsolute() * scaleFactorMiniAxes) > 0) {
                language.newCircle(new Coordinates(originX, originY),
                        (int) (result.getAbsolute() * scaleFactorMiniAxes), "null", null, cp);
            }

            language.nextStep("Calculte Dot Product from all the samples for DFT[" + i + "]");
            hideList(hideInNextStep);

        }
        daArray.hide();
        createFinalSlide();

    }

}