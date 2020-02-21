/**
 * @author Marcel Langer, Kevin Kampa
 * @version 1.0 2019-08-01
 * HillClimb.java
 * Marcel Langer. Kevin Kampa, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import java.util.UUID;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

import javax.swing.*;

public class HillClimb implements ValidatingGenerator {
    private static Language language;
    private boolean RandomValues;
    private boolean DefaultValues;
    private int[][] dataMatrix;
    private int delay;
    private int[] startposition;
    private Color ColorForPossibleWays;
    private Color ColorForPosition;
    private Color ColorForBestValue;
    private SourceCode src;
    private SourceCodeProperties sourceCodeProps;
    private IntMatrix dataForAnimal;
    private MatrixProperties matrixProperties;
    private RectProperties rectProps;
    private RectProperties legendProps;
    public Translator  translator;
    private Locale loc;

    public static void main(String[] args) {
        //         Generator generator = new HillClimb(Locale.GERMANY); // Generator erzeugen
        Generator generator = new HillClimb(Locale.US); // Generator erzeugen
        Animal.startGeneratorWindow(generator); // Animal mit Generator starten



    }


    public HillClimb(Locale loc) {
        /* DOPPELT
         language = new AnimalScript("HillClimb [DE]", "Marcel Langer, Kevin Kampa",800, 600);
        language.setStepMode(true);*/
        translator = new Translator("HillClimb", loc) ;
        this.loc = loc;
        init();


    }
    public HillClimb(Language language) {
        this.language = language;
        // This initializes the step mode. Each pair of subsequent steps has to
        // be divdided by a call of lang.nextStep();
        language.setStepMode(true);
    }


    public void init() {
        language = new AnimalScript("HillClimb", "Marcel Langer. Kevin Kampa", 800, 600);
        language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        language.setStepMode(true);


    }
    public void start(){
        src = language.newSourceCode(new Coordinates(300, 0), "sourceCode", (DisplayOptions)null, this.sourceCodeProps);
        src.addCodeLine("function Hillclimb(IntMatrix data, int[] startposition){", null, 0, null);
        src.addCodeLine("int[] position = startposition;", null, 1, null);
        src.addCodeLine("int[] nextposition;", null, 1, null);
        src.addCodeLine("boolean positionchanged=true;", null, 1, null);
        src.addCodeLine("", null, 1, null);
        src.addCodeLine("while(positionchanged){", null, 1, null);
        src.addCodeLine("nextposition=doHillClimbStep (data, position);", null, 2, null);
        src.addCodeLine("positionchanged=!Arrays.equals(position,nextPosition);", null, 2, null);
        src.addCodeLine("position=nextposition;", null, 2, null);
        src.addCodeLine("}", null, 1, null);
        src.addCodeLine("}", null, 0, null);
        src.addCodeLine("", null, 0, null);
        src.addCodeLine("", null, 0, null);
        src.addCodeLine("", null, 0, null);
        src.addCodeLine("function int[] doHillClimbStep (IntMatrix data, int[] position){", null, 0, null);
        src.addCodeLine("int[] nextposition={position[0],position[1]}; //initialize with current position", null, 1, null);
        src.addCodeLine("int maxValue=data[position[0]-1][position[1]];", null, 1, null);
        src.addCodeLine("int NorthValue=data[position[0]-1][position[1]];", null, 1, null);
        src.addCodeLine("int EastValue=data[position[0]-1][position[1]];", null, 1, null);
        src.addCodeLine("int SouthValue=data[position[0]-1][position[1]];", null, 1, null);
        src.addCodeLine("int WestValue=data[position[0]-1][position[1]];", null, 1, null);
        src.addCodeLine("", null, 0, null);
        src.addCodeLine("if(maxValue<NorthValue){", null, 1, null);
        src.addCodeLine("maxValue=Nortvalue", null, 2, null);
        src.addCodeLine("nextPosition[0]=position[0]-1;", null, 2, null);
        src.addCodeLine("nextPosition[1]=position[1];", null, 2, null);
        src.addCodeLine("}", null, 1, null);
        src.addCodeLine("if(maxValue<EastValue){", null, 1, null);
        src.addCodeLine("maxValue=EastValue", null, 2, null);
        src.addCodeLine("nextPosition[0]=position[0];", null, 2, null);
        src.addCodeLine(" nextPosition[1]=position[1]+1;", null, 2, null);
        src.addCodeLine("}", null, 1, null);
        src.addCodeLine("if(maxValue<SouthValue){", null, 1, null);
        src.addCodeLine("maxValue=Southvalue", null, 2, null);
        src.addCodeLine("nextPosition[0]=position[0]+1;", null, 2, null);
        src.addCodeLine("nextPosition[1]=position[1];", null, 2, null);
        src.addCodeLine("}", null, 1, null);
        src.addCodeLine("if(maxValue<WestValue){", null, 1, null);
        src.addCodeLine("maxValue=WestValue", null, 2, null);
        src.addCodeLine("nextPosition[0]=position[0];", null, 2, null);
        src.addCodeLine("nextPosition[1]=position[1]-1;", null, 2, null);
        src.addCodeLine("}", null, 1, null);
        src.addCodeLine("return nextposition;", null, 1, null);
        src.addCodeLine("}", null, 0, null);
        language.nextStep("Java-Source-Ende");
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        RandomValues = (Boolean) primitives.get("RandomValues");
        startposition = (int[]) primitives.get("startPosition");
        DefaultValues = (Boolean) primitives.get("DefaultValues");
        delay = (int) primitives.get("SlideDelay");
        dataMatrix = (int[][]) primitives.get("intMatrixdata");
        ColorForPossibleWays = (Color) primitives.get("ColorForPossibleWays");
        ColorForPosition = (Color) primitives.get("ColorForPosition");
        ColorForBestValue = (Color) primitives.get("ColorForBestValue");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        rectProps = (RectProperties)props.getPropertiesByName("rectBackgroundProperties");
        legendProps = (RectProperties)props.getPropertiesByName("rectBackgroundProperties");
        if (RandomValues) {
            dataMatrix = getRandomValues();
        }
        if (DefaultValues) {
            dataMatrix = getDefaultValues();
        }




        TextProperties headerProps = new TextProperties();
        headerProps.set("font", new Font("SansSerif", 1, 24));
        Text header = language.newText(new Coordinates(10, 10), "HillClimb", "header", (DisplayOptions)null, headerProps);
        Rect hRect = language.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header", "SE"), "hRect", (DisplayOptions)null, this.rectProps);
        language.nextStep();

        TextProperties textProps = new TextProperties();
        textProps.set("font", new Font("SansSerif", 0, 14));
        language.newText(new Coordinates(10, 50), translator.translateMessage("line1"), "description1", (DisplayOptions)null, textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description1", "NW"), translator.translateMessage("line2"), "description2", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description2", "NW"), translator.translateMessage("line3"), "description3", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description3", "NW"), translator.translateMessage("line4"), "description4", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description4", "NW"), translator.translateMessage("line5"), "description5", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description5", "NW"), translator.translateMessage("line6"), "description6", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description6", "NW"), translator.translateMessage("line7"), "description7", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description7", "NW"), translator.translateMessage("line8"), "description8", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description8", "NW"), translator.translateMessage("line9"), "description9", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description9", "NW"), translator.translateMessage("line10"), "description10", (DisplayOptions)null,textProps);

        language.nextStep();


        language.hideAllPrimitives();

        dataForAnimal = language.newIntMatrix(new Coordinates(10, 50), dataMatrix, "matrixA", (DisplayOptions) null);



        TextProperties legendTextProps = new TextProperties();
        legendTextProps.set("font", new Font("SansSerif", 1, 12));

        int possibleposOffset=0;
        if(loc==Locale.US){possibleposOffset=-7;}

        legendTextProps.set("color",calculateTextColor(ColorForPossibleWays));
        Text legendtext = language.newText(new Offset(-100+possibleposOffset,30*dataMatrix[0].length+10,"header","SE"),translator.translateMessage("possiblepos"), "legendtext", (DisplayOptions)null, legendTextProps);
        legendProps.set("fillColor",ColorForPossibleWays);

        Rect legend = language.newRect(new Offset(-5, -5, "legendtext", "NW"), new Offset(5, 5, "legendtext", "SE"), "lRect", (DisplayOptions)null, this.legendProps);


        int bestnextposOffset=0;
        if(loc==Locale.US){bestnextposOffset=24;}
        legendTextProps.set("color",calculateTextColor(ColorForBestValue));
        Text legendtextbw = language.newText(new Offset(-154+bestnextposOffset, 13, "legendtext", "SE"), translator.translateMessage("bestnextpos"), "legendtextbw", (DisplayOptions)null, legendTextProps);
        legendProps.set("fillColor",ColorForBestValue);
        Rect legendbw = language.newRect(new Offset(-5, -5, "legendtextbw", "NW"), new Offset(5, 5, "legendtextbw", "SE"), "lRect", (DisplayOptions)null, this.legendProps);

        int posOffset=0;
        if(loc==Locale.US){posOffset=23;}

        legendTextProps.set("color",calculateTextColor(ColorForPosition));
        Text legendtextpos = language.newText(new Offset(-154+posOffset, 42, "legendtext", "SE"), translator.translateMessage("pos"), "legendtextpos", (DisplayOptions)null, legendTextProps);
        legendProps.set("fillColor",ColorForPosition);
        Rect legendpos = language.newRect(new Offset(-5, -5, "legendtextpos", "NW"), new Offset(5, 5, "legendtextpos", "SE"), "lRect", (DisplayOptions)null, this.legendProps);


        hRect.show();
        legend.show();
        legendbw.show();
        legendpos.show();
        legendtext.show();
        legendtextbw.show();
        legendtextpos.show();
        header.show();
        language.nextStep(translator.translateMessage("datainit"));
        start();





        language.nextStep(translator.translateMessage("questiontitle1"));
        MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel("multipleChoiceQuestion" + UUID.randomUUID());
        mcq.setPrompt(translator.translateMessage("question1"));
        mcq.addAnswer(translator.translateMessage("answer1q1"), 1, translator.translateMessage("feedback1q1"));
        mcq.addAnswer(translator.translateMessage("answer2q1"), 1, translator.translateMessage("feedback2q1"));
        mcq.addAnswer(translator.translateMessage("answer3q1"), 1, translator.translateMessage("feedback3q1"));

        mcq.setGroupID("First question group");
        language.addMCQuestion(mcq);
        language.nextStep();

        doHillClimb(dataMatrix);


        language.nextStep(translator.translateMessage("questiontitle2"));
        MultipleChoiceQuestionModel mcq2 = new MultipleChoiceQuestionModel("multipleChoiceQuestion" + UUID.randomUUID());
        mcq2.setPrompt(translator.translateMessage("question2"));
        mcq2.addAnswer(translator.translateMessage("answer1q2"), 1, translator.translateMessage("feedback1q2"));
        mcq2.addAnswer(translator.translateMessage("answer2q2"), 0, translator.translateMessage("feedback2q2"));
        mcq2.addAnswer(translator.translateMessage("answer3q2"), 0, translator.translateMessage("feedback3q2"));



        mcq2.setGroupID("Second question group");
        language.addMCQuestion(mcq2);
        language.nextStep();


        language.nextStep(translator.translateMessage("questiontitle2"));
        MultipleChoiceQuestionModel mcq3 = new MultipleChoiceQuestionModel("multipleChoiceQuestion" + UUID.randomUUID());
        mcq3.setPrompt(translator.translateMessage("question3"));
        mcq3.addAnswer(translator.translateMessage("answer1q3"), 1, translator.translateMessage("feedback1q3"));
        mcq3.addAnswer(translator.translateMessage("answer2q3"), 0, translator.translateMessage("feedback2q3"));
        mcq3.addAnswer(translator.translateMessage("answer3q3"), 0, translator.translateMessage("feedback3q3"));


        mcq3.setGroupID("Third question group");
        language.addMCQuestion(mcq3);
        language.nextStep();

        language.nextStep();
        language.hideAllPrimitives();
        legendtextbw.hide(); //TODO these are primitives why does it not work?
        legendtextpos.hide();
        legendtext.hide();
        legend.hide();
        legendbw.hide();
        legendpos.hide();
        language.nextStep("Outrotext");


        hRect.show();
        header.show();
        language.nextStep();
        language.newText(new Coordinates(10, 50), translator.translateMessage("outline1"), "description1", (DisplayOptions)null, textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description1", "NW"), translator.translateMessage("outline2"), "description2", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description2", "NW"), translator.translateMessage("outline3"), "description3", (DisplayOptions)null,textProps);
        language.nextStep();
        language.newText(new Offset(0, 20, "description3", "NW"), translator.translateMessage("outline4"), "description4", (DisplayOptions)null,textProps);
        language.nextStep();

        language.finalizeGeneration();

        return language.toString();

    }

    public String getName() {
        return "Hill-Climbing Search "+getLocalShort();
    }

    public String getAlgorithmName() {
        return "Hill-Climbing Search "+getLocalShort();
    }

    public String getAnimationAuthor() {
        return "Marcel Langer. Kevin Kampa";
    }

    public String getDescription() {
        return "Hillclimbing Search"
                + "\n"
                + "\n"
                + translator.translateMessage("line1") + "\n"
                + translator.translateMessage("line2") + "\n"
                + translator.translateMessage("line3") + "\n"
                + translator.translateMessage("line4") + "\n"
                + translator.translateMessage("line5") + "\n"
                + translator.translateMessage("line6") + "\n"
                + translator.translateMessage("line7") + "\n"
                + translator.translateMessage("line8")   + "\n"
                + translator.translateMessage("line9")  + "\n"
                + translator.translateMessage("line10")   + "\n"
                + "\n"
                + "\n";
    }

    public String getCodeExample() {
        return "function Hillclimb(IntMatrix data, int[] startposition){"
                + "\n"
                + "	int[] position = startposition;"
                + "\n"
                + "	int[] nextposition;"
                + "\n"
                + "	boolean positionchanged=true;"
                + "\n"
                + "\n"
                + "	while(positionchanged){"
                + "\n"
                + "		nextposition=doHillClimbStep (data, position);"
                + "\n"
                + "		positionchanged=!Arrays.equals(position,nextPosition);"
                + "\n"
                + "		position=nextposition;"
                + "\n"
                + "		}"
                + "\n"
                + "}"
                + "\n"
                + "\n"
                + "function int[] doHillClimbStep (IntMatrix data, int[] position){"
                + "\n"
                + "	int[] nextposition={position[0],position[1]}; //initialize with current position"
                + "\n"
                + "	int maxValue=data[position[0]-1][position[1]];"
                + "\n"
                + "	int NorthValue=data[position[0]-1][position[1]];"
                + "\n"
                + "	int EastValue=data[position[0]-1][position[1]];"
                + "\n"
                + "	int SouthValue=data[position[0]-1][position[1]];"
                + "\n"
                + "	int WestValue=data[position[0]-1][position[1]];"
                + "\n"
                + "\n"
                + "\n"
                + "	if(maxValue<NorthValue){"
                + "\n"
                + "		maxValue=Nortvalue"
                + "\n"
                + "		nextPosition[0]=position[0]-1;"
                + "\n"
                + "	    nextPosition[1]=position[1];"
                + "\n"
                + "\n"
                + "	}"
                + "\n"
                + "\n"
                + "\n"
                + "	if(maxValue<EastValue){"
                + "\n"
                + "		maxValue=EastValue"
                + "\n"
                + "		nextPosition[0]=position[0];"
                + "\n"
                + "	    nextPosition[1]=position[1]+1;"
                + "\n"
                + "\n"
                + "	}"
                + "\n"
                + "	if(maxValue<SouthValue){"
                + "\n"
                + "		maxValue=Southvalue"
                + "\n"
                + "		nextPosition[0]=position[0]+1;"
                + "\n"
                + "		nextPosition[1]=position[1];"
                + "\n"
                + "\n"
                + "	}"
                + "\n"
                + "	if(maxValue<WestValue){"
                + "\n"
                + "		maxValue=WestValue"
                + "\n"
                + "		nextPosition[0]=position[0];"
                + "\n"
                + "		nextPosition[1]=position[1]-1;"
                + "\n"
                + "\n"
                + "	}"
                + "\n"
                + "\n"
                + "	return nextposition;"
                + "\n"
                + "}"
                + "\n";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return this.loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public String getLocalShort(){
        if(loc==Locale.GERMANY){
            return "[DE]";
        }
        if(loc==Locale.US){
            return "[EN]";
        }
        return "";

    }

    //------------------------------------------------------------------------


    private void doHillClimb(int[][] data) {

        int[] position = startposition;
        boolean positionchanged = true;
        boolean once = true;
        src.highlight(1);
        src.highlight(2);
        src.highlight(3);
        language.nextStep(delay);
        src.unhighlight(1);
        src.unhighlight(2);
        src.unhighlight(3);
        language.nextStep();
        src.highlight(5);
        language.nextStep();



        int[] nextPosition = doHillClimbStep(data, position);
        int counter=1;
        while (positionchanged || once) {
            src.highlight(6);
            src.highlight(14);
            dataForAnimal.setGridHighlightFillColor(position[0], position[1], ColorForPosition, null, null);// changed
            dataForAnimal.highlightCell(position[0], position[1], null, null);
            language.nextStep(delay,counter+"iteration");
            HighlightMultipleLines(15,20);
            if (positionchanged) {
                dataForAnimal.unhighlightCell(nextPosition[0], nextPosition[1], null, null);
            }
            if (Notupper(position, data)) {
                dataForAnimal.setGridHighlightFillColor(position[0] - 1, position[1], ColorForPossibleWays, null, null);
                dataForAnimal.highlightCell(position[0] - 1, position[1], null, null);
            }
            if (NotLower(position, data)) {
                dataForAnimal.setGridHighlightFillColor(position[0] + 1, position[1], ColorForPossibleWays, null, null);
                dataForAnimal.highlightCell(position[0] + 1, position[1], null, null);
            }
            if (NotLeft(position, data)) {
                dataForAnimal.setGridHighlightFillColor(position[0], position[1] - 1, ColorForPossibleWays, null, null);
                dataForAnimal.highlightCell(position[0], position[1] - 1, null, null);
            }
            if (NotRight(position, data)) {
                dataForAnimal.setGridHighlightFillColor(position[0], position[1] + 1, ColorForPossibleWays, null, null);
                dataForAnimal.highlightCell(position[0], position[1] + 1, null, null);
            }
            language.nextStep(delay);
            UnHighlightMultipleLines(15,20);
            HighlightMultipleLines(22,41);
            src.highlight(43);
            dataForAnimal.setGridHighlightFillColor(nextPosition[0], nextPosition[1], ColorForBestValue, null, null);
            dataForAnimal.highlightCell(nextPosition[0], nextPosition[1], null, null);
            language.nextStep(delay);
            UnHighlightMultipleLines(22,41);
            if (Notupper(position, data)) {
                dataForAnimal.unhighlightCell(position[0] - 1, position[1], null, null);
            }
            if (NotLower(position, data)) {
                dataForAnimal.unhighlightCell(position[0] + 1, position[1], null, null);
            }
            if (NotLeft(position, data)) {
                dataForAnimal.unhighlightCell(position[0], position[1] - 1, null, null);
            }
            if (NotRight(position, data)) {
                dataForAnimal.unhighlightCell(position[0], position[1] + 1, null, null);
            }
            if (positionchanged) {
                dataForAnimal.unhighlightCell(position[0], position[1], null, null);
            }
            // language.nextStep(delay);
            dataForAnimal.unhighlightElem(nextPosition[0], nextPosition[1], null, null);
            src.unhighlight(43);
            position = nextPosition;
            nextPosition = doHillClimbStep(data, position);

            if (!positionchanged) {

                once = false;

            }
            positionchanged = !Arrays.equals(position, nextPosition);
            counter++;

        }


    }

    /**
     * Executes one iteration of Hillclimb
     *
     * @param data     contains values for costfnc at every position
     * @param position indices of position with  position[0]=row & position (1)=column
     * @return next position
     */
    private int[] doHillClimbStep(int[][] data, int[] position) {
        int zeilenanzahl = data.length;
        int spaltenanzahl = data[0].length;
        int Nortvalue = Integer.MIN_VALUE;
        int Westvalue = Integer.MIN_VALUE;
        int Southvalue = Integer.MIN_VALUE;
        int Eastvalue = Integer.MIN_VALUE;
        int MaxValue = winfunction(data, position[0], position[1]);
        int[] nextPosition = {position[0], position[1]};
        if (position[0] >= 1) {
            Nortvalue = winfunction(data, position[0] - 1, position[1]);
            if (Nortvalue > MaxValue) {
                MaxValue = Nortvalue;
                nextPosition[0] = position[0] - 1;
                nextPosition[1] = position[1];

            }

        }
        if (position[0] < zeilenanzahl - 1) {
            Southvalue = winfunction(data, position[0] + 1, position[1]);
            if (Southvalue > MaxValue) {
                MaxValue = Southvalue;
                nextPosition[0] = position[0] + 1;
                nextPosition[1] = position[1];

            }

        }
        if (position[1] >= 1) {
            Westvalue = winfunction(data, position[0], position[1] - 1);
            if (Westvalue > MaxValue) {
                MaxValue = Westvalue;
                nextPosition[0] = position[0];
                nextPosition[1] = position[1] - 1;

            }

        }
        if (position[1] < spaltenanzahl - 1) {
            Eastvalue = winfunction(data, position[0], position[1] + 1);
            if (Eastvalue > MaxValue) {
                MaxValue = Eastvalue;
                nextPosition[0] = position[0];
                nextPosition[1] = position[1] + 1;

            }

        }
           return nextPosition;
    }

    private int winfunction(int[][] data, int row, int column) {
        return data[row][column];

    }

    public int[][] getRandomValues() {
        for (int i = 0; i < dataMatrix.length; i++) {
            for (int j = 0; j < dataMatrix[i].length; j++) {

                dataMatrix[i][j] = (int) Math.round((Math.random() * 99 + 1));
            }
        }
        return dataMatrix;
    }

    public int[][] getDefaultValues() {
        for (int i = 0; i < dataMatrix.length; i++) {
            for (int j = 0; j < dataMatrix[i].length; j++) {

                dataMatrix[i][j] = i + dataMatrix[i].length * j;

            }
        }
        return dataMatrix;
    }
    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        boolean InputCorrect = true;
        RandomValues = (Boolean) primitives.get("RandomValues");
        DefaultValues = (Boolean) primitives.get("DefaultValues");
        dataMatrix = (int[][]) primitives.get("intMatrixdata");
        startposition = (int[]) primitives.get("startPosition");
        //check for correct size of position
        if (startposition == null || startposition.length != 2) {
            CustomErrorMessage("startposition not valid");
            return false;
        }
        //check for correct position
        if (startposition[0] >= dataMatrix.length || startposition[0] < 0) {
            CustomErrorMessage("startposition[0] not valid");
            return false;
        }

        if (startposition[1] >= dataMatrix[0].length || startposition[1] < 0) {
            CustomErrorMessage("startposition[1] not valid");
            return false;
        }

        if (RandomValues && DefaultValues) {
            CustomErrorMessage("You can't use DefaultValues and RandomValues");
            return false;
        }
        //Set max size to 15
        InputCorrect = InputCorrect && dataMatrix.length <= 15;
        for (int i = 0; i < dataMatrix.length; i++) {
            InputCorrect = InputCorrect && dataMatrix[i].length <= 15;
        }
        if(!InputCorrect){
            CustomErrorMessage("Matrix too large(max size: 15*15)");
        }
        return InputCorrect;
    }


    /**
     * calculates if North would be out of bounds
     */
    private boolean Notupper(int[] position, int[][] dataMatrix) {
        return position[0] >= 1;
    }

    /**
     * calculates if South would be out of bounds
     */
    private boolean NotLower(int[] position, int[][] dataMatrix) {
        return position[0] < dataMatrix.length - 1;
    }

    /**
     * calculates if West would be out of bounds
     */
    private boolean NotLeft(int[] position, int[][] dataMatrix) {
        return position[1] >= 1;
    }

    /**
     * calculates if East would be out of bounds
     */
    private boolean NotRight(int[] position, int[][] dataMatrix) {
        return position[1] < dataMatrix[0].length - 1;
    }




    private void HighlightMultipleLines(int startline, int endline){
        int i=startline;
        while(i<=endline){
            src.highlight(i);
            i++;
        }



    }

    private void UnHighlightMultipleLines(int startline, int endline){
        int i=startline;
        while(i<=endline){
            src.unhighlight(i);
            i++;
        }



    }
        public Color calculateTextColor(Color textcolor){
        // calculate Y of YCbCr
        double yValue=0.299*textcolor.getRed()+0.587*textcolor.getGreen()+0.114*textcolor.getBlue();
        if(yValue>127.5){
            return Color.black;
        }
        else{
            return Color.white;
        }


        }


    public void CustomErrorMessage(String errormsg){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),errormsg,"Input not valid",0);
    }
}


