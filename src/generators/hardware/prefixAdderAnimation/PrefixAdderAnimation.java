package generators.hardware.prefixAdderAnimation;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import generators.hardware.prefixAdderAnimation.util.Adder;
import generators.hardware.prefixAdderAnimation.util.HwGateDrawer;
import generators.hardware.prefixAdderAnimation.util.MyText;
import generators.hardware.prefixAdderAnimation.util.Util;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


/**
 * Created by philipp on 02.05.15.
 */
public class PrefixAdderAnimation{
    /** global animation parameters **/
    private final static double LOG2 = Math.log(2);
    private final int HEIGHT = 600;
    private final int WIDTH = 800;
    int desWidth = WIDTH/4;
    private int adderWidth = 8;
    private int adderHeight;
    private int delayTicks = 50;
    private int adderBlockWidth = (int) (WIDTH / (adderWidth * 2.5));
    private int adderBlockMargin = WIDTH / (adderWidth * 5);
    private int adderPlaceMargin = adderBlockMargin + adderBlockWidth;
    private int marginToTop = 180;
    private int descBlockSize = HEIGHT/5;
    private int descBlockMargin = HEIGHT/12;
    private int textOverBlock = HEIGHT/20;

    /** global animation properties **/
    private SquareProperties blocksProp;
    private SquareProperties descBlockProp;
    private PolylineProperties wireProp;
    private PolylineProperties arrow;
    private TextProperties inOutProp;
    private TextProperties blockNameProp;
    private RectProperties canvasProp;
    private SourceCodeProperties descriptionProp;
    private TextProperties headerProp;
    private TextProperties textProp;
    private Color highlight;
    private Color blockColor;
    private Color textColor;

    private Color inputHighlight1;
    private Color inputHighlight2;
    private Color inputHighlight3;
    /** Lists and Arrays for primitives **/
    private Square inputBlocks[];
    private Square internalBlocks[][];
    private Square outputBlocks[];
    private Text outputNumbers[];
    private Text inputNumbers[];
    private Text inputAText[];
    private Text inputBText[];
    private Text inputSignalsG[];
    private Text inputSignalsP[];
    private Text internalSignalsG[][];
    private Text internalSignalsP[][];
    private Text outputSignals[];
    private ArrayList<Polyline> wires;
    /**highlighted inputs */
    private ArrayList<Text> formula;
    private ArrayList<Text> values;

    /** global primitives **/
    private Polyline border;
    private Text header;
    private Text descriptiveHeader;

    private Text descInputBlockText;
    private Text descInternalBlockText;
    private Text descOutputBlockText;


    private Square descInputBlockSq;
    private Square descInternalBlockSq;
    private Square descOutputBlockSq;


    /** helper Objects **/
    private HwGateDrawer gateDrawer;
    private Adder adder;
    /** data **/
    private int inA=1;
    private int inB=11;
    boolean subtract = false;
    boolean signed = true;
    private int[] inputA;
    private int[] inputB;


    private TicksTiming instant = new TicksTiming(0);
    private Language language;

    public PrefixAdderAnimation(Language lang){
        adderHeight = (int) Math.ceil(Math.log(adderWidth)/LOG2);
        language = lang;
        language.setStepMode(true);
        gateDrawer = new HwGateDrawer(language);
        initArraysAndLists();
    }
    public void animateAdder(){
        drawFirstFrames();
        introduceInputBlock();
        introduceInternalBlock();
        wireInputAndInternal();
        lAndUExamples();
        introduceOutputBlock();
        animateAddition();
        drawConclusion();
    }

    private void drawFirstFrames(){
        Coordinates[] borderEndPoints = {new Coordinates(WIDTH-desWidth,0),new Coordinates(WIDTH-desWidth,HEIGHT)};
        border = language.newPolyline(borderEndPoints, "borderLine", instant);
        header = language.newText(new Coordinates(10, 10), "Prefix Adder", "header", instant, headerProp);
        Rect headerRect = language.newRect(new Coordinates(2, 2), new Offset(5, 5, header, "SE"), "headerRect", instant);
        descriptiveHeader = language.newText(new Offset(15,0, header, "NE"), "Introduction", "descHeader", instant, headerProp);
        SourceCode introduction = language.newSourceCode(new Coordinates(20,55),"introduction", instant , descriptionProp);
        introduction.addMultilineCode(MyText.INTRODUCTION, "intro", instant);
        language.nextStep("Introduction");
        introduction.hide();
        descriptiveHeader.setText("Basic Idea", instant, instant);
        SourceCode description = language.newSourceCode(new Coordinates(20,55),"description",instant, descriptionProp);
        description.addMultilineCode(MyText.DESCRIPTION_FIRST_PAGE, "desc", instant);
        language.nextStep();
        description.hide();
    }

    private void introduceInputBlock(){
        //change header
        descriptiveHeader.setText("Input Layer Blocks: ", instant, instant);
        //draw blocks
        for (int i = 0; i < inputBlocks.length; i++) {
            TicksTiming appearance = new TicksTiming(delayTicks*i);
            inputBlocks[i] = language.newSquare(new Coordinates(600 - (i + 1) * adderPlaceMargin, marginToTop),
                    adderBlockWidth, "inputBlock" + i, appearance, blocksProp);
            inputNumbers[i] = language.newText(new Offset(10, 10, inputBlocks[i], "NW"), (i-1) + "", "number", appearance, textProp);
        }
        language.nextStep("Introduction Input Blocks");
        // marker and description of cIn Field
        inputBlocks[0].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        SourceCode cInDescription = language.newSourceCode(new Coordinates(50, HEIGHT / 2), "cInDes", instant);
        cInDescription.addMultilineCode(MyText.DESCRIPTION_CARRY_IN, "des", instant);
        Node cInMarkerCoord[] = {new Offset(2,2,inputBlocks[0],"SW"), new Offset(0,0,cInDescription,"N")};
        Polyline cInMarker = language.newPolyline(cInMarkerCoord, "cInMarker", instant, arrow);
        language.nextStep();
        //hide
        inputBlocks[0].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        cInDescription.hide();
        cInMarker.hide();
        //description
        // input description
        SourceCode descriptionInput = language.newSourceCode(new Coordinates(50, 55), "descriptionInput", instant, descriptionProp);
        descriptionInput.addMultilineCode(MyText.DESCRIPTION_INPUT_BLOCK, "Input", instant);
        // Frame
        descInputBlockSq = language.newSquare(new Offset(25, descBlockMargin, border, "NW"),
                descBlockSize, "InputBlockRep", instant, descBlockProp);
        descInputBlockText = language.newText(new Offset(0, -textOverBlock, descInputBlockSq, "NW"),
                "Input Block", "InputBlockTxt", instant, blockNameProp);
        gateDrawer.drawInputBlock(descInputBlockSq, descBlockSize);
        //Highlight
        descInputBlockSq.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        descInputBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        language.nextStep();
        //dehighlight input
        descriptionInput.hide();
        descInputBlockSq.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        descInputBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
    }
    private void introduceInternalBlock(){
        descriptiveHeader.setText("Internal Layer Blocks: ", instant, instant);
        int l = 0;
        for (int i = 0; i < internalBlocks.length; i++) {
            int k = 0;
            boolean setBlock = false;
            for (int j = 0; j < internalBlocks[i].length; j++) {
                k++;
                if (k == Math.pow(2, i)) {
                    k = 0;
                    setBlock = !setBlock;
                }
                if (setBlock) {
                    l++;
                    internalBlocks[i][j] = language.newSquare(new Coordinates(600 - (j + 2) * adderPlaceMargin, marginToTop + (2 * i + 2) * adderBlockWidth),
                            adderBlockWidth, "internalBlock" + j + "/" + i, new TicksTiming(50 * l), blocksProp);
                }
            }
        }
        language.nextStep("Introduction Internal Blocks");
        SourceCode descriptionInternal[] = new SourceCode[3];
        descriptionInternal[0] =language.newSourceCode(new Coordinates(50, 55), "descriptionInternal", instant, descriptionProp);
        descriptionInternal[0].addMultilineCode(MyText.DESCRIPTION_INTERNAL_BLOCK[0], "Internal", instant);
        //Frame
        descInternalBlockSq = language.newSquare(new Offset(0, descBlockMargin, descInputBlockSq, "SW"),
                descBlockSize, "InternalBlockRep", instant, descBlockProp);
        descInternalBlockText = language.newText(new Offset(0, -textOverBlock, descInternalBlockSq, "NW"),
                "Internal Block", "InternalBlockTxt", instant, blockNameProp);
        gateDrawer.drawInternalBlock(descInternalBlockSq, descBlockSize);
        //Highlight
        descInternalBlockSq.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        descInternalBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        language.nextStep();
        descriptionInternal[0].hide();
        descriptionInternal[1] =language.newSourceCode(new Coordinates(50, 55), "descriptionInternal", instant, descriptionProp);
        descriptionInternal[1].addMultilineCode(MyText.DESCRIPTION_INTERNAL_BLOCK[1], "Internal", instant);
        language.nextStep();
        descriptionInternal[1].hide();
        descriptionInternal[2] =language.newSourceCode(new Coordinates(50, 55), "descriptionInternal", instant, descriptionProp);
        descriptionInternal[2].addMultilineCode(MyText.DESCRIPTION_INTERNAL_BLOCK[2], "Internal", instant);
        language.nextStep();
        //dehighlight internal
        descriptionInternal[2].hide();
        descInternalBlockSq.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        descInternalBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
    }
    private void wireInputAndInternal(){
        descriptiveHeader.setText("Wiring",instant,instant);
        SourceCode desWiring = language.newSourceCode(new Coordinates(50,50),"wireDes",instant);
        desWiring.addMultilineCode(MyText.DESCRIPTION_WIRING, "desWiring", instant);
        for (int i = 0; i < adderWidth; i++) {
            Node coords[] = {new Offset(0, 0, inputBlocks[i], "S"), new Offset(0, 2 * adderHeight * adderBlockWidth + adderBlockWidth / 2, inputBlocks[i], "S")};
            wires.add(language.newPolyline(coords, "wire", instant, wireProp));
        }
        for (int i = 0; i < internalBlocks.length; i++) {
            int startPoint = (int) Math.pow(2,i)-1;
            for (int j = startPoint; j < internalBlocks[i].length; j = (int) (j + Math.pow(2,i+1))){
                int endOffsetX = adderPlaceMargin/2 - (int)(Math.pow(2,i)-1)*adderPlaceMargin;
                Node coords[] ={new Offset( adderPlaceMargin,-adderBlockWidth/2,internalBlocks[i][j],"N"),
                        new Offset( endOffsetX, -adderBlockWidth/2,internalBlocks[i][j],"N")};
                wires.add(language.newPolyline(coords, "wire", instant, wireProp));
                for(int k = j; k < j+Math.pow(2,i); k++){
                    coords = new Node[]{new Offset(adderPlaceMargin / 2, -adderBlockWidth / 2, internalBlocks[i][k], "N"),
                            new Offset(0, 0, internalBlocks[i][k], "N")};
                    wires.add(language.newPolyline(coords, "wire", instant, wireProp));

                }
            }
        }
        language.nextStep("Wiring");
        desWiring.hide();

    }
    private void lAndUExamples(){
        descriptiveHeader.setText("'Upper' and 'Lower' Input to Internal Block", instant, instant);
        SourceCode des1Ex = language.newSourceCode(new Coordinates(50,30),"des1Ex",instant);
        des1Ex.addMultilineCode(MyText.DESCRIPTION_LUEXAMPLE_1, "LUE1", instant);
        internalBlocks[0][2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        inputBlocks[2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN, instant, instant);
        inputBlocks[3].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, instant, instant);
        language.nextStep("Upper and Lower Input to Internal Block");
        des1Ex.hide();
        internalBlocks[0][2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        inputBlocks[2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        inputBlocks[3].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        //2 Example
        SourceCode des2Ex = language.newSourceCode(new Coordinates(50,30),"des2Ex",instant);
        des2Ex.addMultilineCode(MyText.DESCRIPTION_LUEXAMPLE_2, "LUE2", instant);
        internalBlocks[2][4].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight  , instant, instant);
        internalBlocks[1][2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN, instant, instant);
        internalBlocks[0][4].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, instant, instant);
        language.nextStep();
        des2Ex.hide();
        internalBlocks[2][4].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        internalBlocks[1][2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        internalBlocks[0][4].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);

    }
    private void introduceOutputBlock(){
        descriptiveHeader.setText("Output Layer Blocks: ", instant, instant);
        for (int i = 0; i < adderWidth; i++) {
            TicksTiming delay = new TicksTiming(delayTicks*i);
            outputBlocks[i] = language.newSquare(new Coordinates(600 - (i + 2) * adderPlaceMargin, marginToTop + ((3 * adderHeight) - 1) * adderBlockWidth),
                    adderBlockWidth, "outputBlock" + i, new TicksTiming(delayTicks * i), blocksProp);
            outputNumbers[i] = language.newText(new Offset(10, 10, outputBlocks[i], "NW"), i + "", "outputNumber", delay, textProp);
            Node[] coords = {new Offset(adderPlaceMargin, -adderBlockWidth/2, outputBlocks[i], "N"),
                    new Offset(0, 0, outputBlocks[i], "N")};
            wires.add(language.newPolyline(coords, "wire", delay, wireProp));
        }
        language.nextStep("Introduction Output Blocks");
        SourceCode descriptionOutput = language.newSourceCode(new Coordinates(50,55), "descriptionOutput",instant, descriptionProp);
        descriptionOutput.addMultilineCode(MyText.DESCRIPTION_OUTPUT_BLOCK, "Internal", instant);
        // Frame
        descOutputBlockSq = language.newSquare(new Offset(0, descBlockMargin, descInternalBlockSq, "Sw"),
                descBlockSize, "OutputBlockRep", instant, descBlockProp);
        descOutputBlockText = language.newText(new Offset(0, -textOverBlock, descOutputBlockSq, "NW"),
                "Output Block", "OutputBlockTxt", instant, blockNameProp);
        gateDrawer.drawOutputBlock(descOutputBlockSq, descBlockSize);
        // Highlight
        descOutputBlockSq.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        descOutputBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        language.nextStep();
        // Dehighlight output
        descriptionOutput.hide();
        descOutputBlockSq.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        descOutputBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);

    }

    private void animateAddition(){
        String str  = signed ? "signed" : "unsigned";
        if(subtract){
            descriptiveHeader.setText("Subtraction: " + inA +" - "+ inB + " ("+str+")" , instant,instant);
        }
        else {
            descriptiveHeader.setText("Addition: " + inA + " + " + inB + " ("+str+")", instant, instant);
        }
        for(int i = 0; i < adderWidth; i++){
            inputAText[i] = language.newText(new Offset(0,-130, inputBlocks[i+1],"N"),inputA[i]+"","inputA",instant,inOutProp);
            inputBText[i] = language.newText(new Offset(0, -100, inputBlocks[i+1],"N"),inputB[i]+"","inputB",instant,inOutProp);
        }
        inputAText[adderWidth] = language.newText(new Offset(-adderPlaceMargin,-130,inputBlocks[adderWidth],"N"), "A:", "inputA", instant, inOutProp);
        inputBText[adderWidth] = language.newText(new Offset(-adderPlaceMargin, -100,inputBlocks[adderWidth],"N"), "B:", "inputB", instant, inOutProp);

        str = this.subtract ? "1" : "0";
        Text cInText = language.newText(new Offset(adderPlaceMargin/2,0,inputAText[0],"E"),"C_in: " + str, "cIn",instant,inOutProp );
        language.nextStep("Addition");
        /** input layer **/
        //hide input numbers

        for (int i = 0; i < inputNumbers.length; i++){
            inputNumbers[i].hide();
        }
        descInputBlockSq.  changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        descInputBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        for (int i = 0; i < adderWidth + 1; i++){
            if(i > 0){
                if(i == 1){
                    cInText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
                    generateTextInputInit();
                }
                else {
                    inputAText[i - 2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
                    inputBText[i - 2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
                }
                inputBlocks[i-1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);

            }
            inputBlocks[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
            if(i == 0){
                generateTextCin();
                cInText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
            }
            else {
                inputAText[i - 1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
                inputBText[i - 1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
            }
            inputSignalsG[i] =
                    language.newText(new Offset(3,  3, inputBlocks[i], "NW"), "G: "+ adder.getInputBlockValuesG()[i], "inG", instant, textProp);
            inputSignalsP[i] =
                    language.newText(new Offset(3, 20, inputBlocks[i], "NW"), "P: "+ adder.getInputBlockValuesP()[i], "inP", instant, textProp);
            if(i >= 1) {
                generateTextInput(i);
            }
            if(i == 0) {
                language.nextStep("Addition in Input Blocks");
            }
            else{
                language.nextStep();
            }
        }
        //dehighlight
        descInputBlockSq.  changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        descInputBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
        inputBlocks[adderWidth].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        inputAText[adderWidth -1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
        inputBText[adderWidth -1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
        /** internal layer **/
        descInternalBlockSq.  changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        descInternalBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        generateTextInternalInit();
        for(int i = 0; i < adderHeight; i++) {
            for (int j = 0; j < adderWidth; j++) {
                if (internalBlocks[i][j] != null) {
                    generateTextInternal(i, j);
                    getUpperSquare(i, j).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
                    getLowerSquare(i, j).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
                    internalBlocks[i][j].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
                    internalSignalsG[i][j] =
                            language.newText(new Offset(3, 3, internalBlocks[i][j], "NW"), "G: " + adder.getInternalBlockValuesG()[i][j+1], "intG", instant, textProp);
                    internalSignalsP[i][j] =
                            language.newText(new Offset(3, 20, internalBlocks[i][j], "NW"), "P: " + adder.getInternalBlockValuesP()[i][j+1], "intP", instant, textProp);
                    if (i == 0 && j == 0){
                        language.nextStep("Addition in Internal Blocks");
                    }
                    else {
                        language.nextStep();
                    }
                    internalBlocks[i][j].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
                    getUpperSquare(i, j).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
                    getLowerSquare(i, j).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);



                }
            }
        }
        descInternalBlockSq.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        descInternalBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
        /** output layer **/
        for(int i = 0; i < adderWidth; i++){
            outputNumbers[i].hide();
        }
        generateTextOutputInit();
        descOutputBlockSq  .changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        descOutputBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        for(int i = 0; i < adderWidth; i++){
            inputAText[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
            inputBText[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
            generateTextOutput(i);
            getThirdInputSquare(i).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight3, instant, instant);
            outputBlocks[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
            outputSignals[i] =
                    language.newText(new Offset(3, 3, outputBlocks[i],"NW"),"S: " +adder.getOutputBlockValues()[i], "outS",instant , textProp);
            if(i == 0) {
                language.nextStep("Addition in Output Blocks");
            }
            else{
                language.nextStep();
            }
            outputBlocks[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
            inputAText[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
            inputBText[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
            getThirdInputSquare(i).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
        }
        descOutputBlockSq.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, blockColor, instant, instant);
        descOutputBlockText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, textColor, instant, instant);
        clearText();
        Text res = language.newText(new Coordinates(50,marginToTop-70), getOutputMsg(), "res",instant, inOutProp);
        res.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        language.nextStep();
    }


    private void drawConclusion(){
        Rect canvas = language.newRect(new Coordinates(10, 45), new Coordinates(WIDTH - desWidth - 5, HEIGHT), "canvas", instant, canvasProp);
        descriptiveHeader.setText("Conclusion", instant, instant);
        SourceCode descriptionInternal = language.newSourceCode(new Coordinates(30, 55), "conclusion", instant, descriptionProp);
        descriptionInternal.addMultilineCode(MyText.CONCLUSION, "conclusion", instant);
        language.nextStep("Conclusion");
    }


    private void initArraysAndLists(){
        inputBlocks = new Square[adderWidth + 1];
        internalBlocks = new Square[adderHeight][adderWidth];
        outputBlocks = new Square[adderWidth];
        outputNumbers = new Text[adderWidth];
        inputNumbers = new Text[adderWidth+1];
        inputAText = new Text[adderWidth+1];
        inputBText = new Text[adderWidth+1];
        inputSignalsG = new Text[adderWidth+1];
        inputSignalsP = new Text[adderWidth+1];
        internalSignalsG = new Text[adderHeight][adderWidth];
        internalSignalsP = new Text[adderHeight][adderWidth];
        outputSignals = new Text[adderWidth];
        wires = new ArrayList<>();
    }

    /**
     * Initialize Properties
     */
    private void initProperties(){
        textColor = (Color) textProp.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        //
        blockColor = (Color) blocksProp.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        blocksProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        blocksProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        descBlockProp = new SquareProperties("descBlockProp");
        descBlockProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,blockColor);
        //"wires" (polylines)
        wireProp = new PolylineProperties("wireProp");
        wireProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        //"arrow" (polyline)
        arrow = new PolylineProperties("Arrow");
        arrow.set(AnimationPropertiesKeys.BWARROW_PROPERTY,true);
        //input/output number prop
        inOutProp = new TextProperties("inOutProp");
        Font inOutFont = new Font(Font.SANS_SERIF,Font.PLAIN,14);
        inOutProp.set(AnimationPropertiesKeys.FONT_PROPERTY,inOutFont);
        inOutProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);
        // block desc names
        blockNameProp = new TextProperties("tp");
        Font blockNamesFont = new Font(Font.SANS_SERIF,Font.BOLD,14);
        blockNameProp.set(AnimationPropertiesKeys.FONT_PROPERTY,blockNamesFont);
        blockNameProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);
        // canvas prop
        canvasProp = new RectProperties("canvasProp");
        canvasProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        canvasProp.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.WHITE);
        canvasProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.WHITE);
        //
        descriptionProp = new SourceCodeProperties("descriptionProp");
        descriptionProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        //header
        headerProp = new TextProperties("headerProp");
        Font headerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
        headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);
        headerProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);
    }
    private String getOutputMsg(){
        String outMsg;
        int result = subtract ? (inA - inB) : (inA + inB);
        int inAdder = Util.fromBinary(adder.getOutputBlockValues(),signed);
        if(inAdder != result){
            outMsg = "Overflow detected! Value in adder: "+ inAdder +" (Real Value: " +result + ")";
        }
        else{
            outMsg = "Result: " + result;
        }
        return outMsg;
    }

    /**
     * converts to binary and precomputes values
     */

    public void setInputs(int inA, int inB,boolean signed, boolean subtract){
        this.signed = signed;
        boolean warningA;
        boolean warningB;
        boolean invalidSub;
        if(signed) {
            warningA = inA < -128 || inA > 127;
            warningB = inB < -128 || inB > 127;
            invalidSub = false;
        }
        else{
            warningA = inA < 0 || inA > 255;
            warningB = inB < 0 || inB > 255;
            invalidSub = inB > inA;
        }
        this.inA = warningA ? 42 : inA;
        this.inB = warningB ? 32 : inB;
        if(invalidSub){
            inA = 42;
            inB = 32;
        }
        warning(warningA, warningB,invalidSub);
        this.subtract = subtract;
        this.inputA= Util.toBinary(inA,adderWidth);
        this.inputB= Util.toBinary(inB,adderWidth);
        if(subtract){
            inputB = Util.invert(inputB);
        }
        adder = new Adder(adderWidth);
        adder.add(inputA, inputB, subtract ? 1 : 0);
    }
    public void setProperties(TextProperties textProp, SquareProperties squareProperties, Color highlightColor,
    Color inputHighlight1, Color inputHighlight2, Color inputHighlight3){
        this.textProp= textProp;
        this.blocksProp = squareProperties;
        this.highlight = highlightColor;
        this.inputHighlight1 = inputHighlight1;
        this.inputHighlight2 = inputHighlight2;
        this.inputHighlight3 = inputHighlight3;
        initProperties();
    }
    private void warning(boolean warnA, boolean warnB, boolean invalidSub){
        TextProperties warnProp = new TextProperties("warnProp");
        Font warnFont = new Font(Font.SANS_SERIF,Font.PLAIN,20);
        warnProp.set(AnimationPropertiesKeys.FONT_PROPERTY,warnFont);
        warnProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        if(warnA || warnB || invalidSub) {
            Text warning = language.newText(new Coordinates(10, 10), "Warning! invalid input value(s) - using default value(s)",
                    "warn", instant,warnProp);
            Text warnExt;
            if(signed) {
                warnExt = language.newText(new Coordinates(10, 35), "For signed representation values should be between -128 and 127!" +
                                " ->  click to continue","extW", instant, textProp);
            }
            else{
                warnExt = language.newText(new Coordinates(10,35), "For unsigned representation values should be between 0 and 255 and " +
                        "A greater or equal to B! ->  click to continue","extW", instant, textProp);
            }
            language.nextStep();
            warning.hide();
            warnExt.hide();
        }
    }


    private Square getUpperSquare(int i, int j){
        switch(i){
            case 0:
               return inputBlocks[j+1];
            case 1:
                switch (j){
                    case 1: return inputBlocks[2];
                    case 2: return internalBlocks[0][2];
                    case 5: return inputBlocks[6];
                    case 6: return internalBlocks[0][6];
                }
                break;
            case 2:
                switch (j) {
                    case 3: return inputBlocks[4];
                    case 4: return internalBlocks[0][4];
                    case 5: return internalBlocks[1][5];
                    case 6: return internalBlocks[1][6];
                }
        }
        return null;
    }

    private int getUpperValue(int i, int j, boolean p){
        switch(i){
            case 0:
                    return p ? adder.getInputBlockValuesP()[j + 1] : adder.getInputBlockValuesG()[j + 1];
            case 1:
                switch (j){
                    case 1: return p ? adder.getInputBlockValuesP()[2] : adder.getInputBlockValuesG()[2];
                    case 2: return p ? adder.getInternalBlockValuesP()[0][3] : adder.getInternalBlockValuesG()[0][3];
                    case 5: return p ? adder.getInputBlockValuesP()[6] : adder.getInputBlockValuesG()[6];
                    case 6: return p ? adder.getInternalBlockValuesP()[0][7] : adder.getInternalBlockValuesG()[0][7];
                }
                break;
            case 2:
                switch (j) {
                    case 3: return p ? adder.getInputBlockValuesP()[4] : adder.getInputBlockValuesG()[4];
                    case 4: return p ? adder.getInternalBlockValuesP()[0][5] : adder.getInternalBlockValuesG()[0][5];
                    case 5: return p ? adder.getInternalBlockValuesP()[1][6] : adder.getInternalBlockValuesG()[1][6];
                    case 6: return p ? adder.getInternalBlockValuesP()[1][7] : adder.getInternalBlockValuesG()[1][7];
                }
        }
        return 0;

    }


    private Square getLowerSquare(int i, int j){
        switch(i){
            case 0:
                return inputBlocks[j];
            case 1:
                switch (j){
                    case 1: return internalBlocks[0][0];
                    case 2: return internalBlocks[0][0];
                    case 5: return internalBlocks[0][4];
                    case 6: return internalBlocks[0][4];
                }
                break;
            case 2:
                return internalBlocks[1][2];
        }
        return null;
    }
    private int getLowerValue(int i, int j, boolean p){
        switch(i){
            case 0:
                return p ? adder.getInputBlockValuesP()[j] : adder.getInputBlockValuesG()[j];
            case 1:
                switch (j) {
                    case 1:
                        return p ? adder.getInternalBlockValuesP()[0][1] : adder.getInternalBlockValuesG()[0][1];
                    case 2:
                        return p ? adder.getInternalBlockValuesP()[0][1] : adder.getInternalBlockValuesG()[0][1];
                    case 5:
                        return p ? adder.getInternalBlockValuesP()[0][5] : adder.getInternalBlockValuesG()[0][5];
                    case 6:
                        return p ? adder.getInternalBlockValuesP()[0][5] : adder.getInternalBlockValuesG()[0][5];
                }
                break;
            case 2:
                return p ? adder.getInternalBlockValuesP()[1][3] : adder.getInternalBlockValuesG()[1][3];
        }
        return 0;

    }

    private Square getThirdInputSquare(int i){
        switch (i){
            case 0: return inputBlocks[0];
            case 1: return internalBlocks[0][0];
            case 2: return internalBlocks[1][1];
            case 3: return internalBlocks[1][2];
            case 4: return internalBlocks[2][3];
            case 5: return internalBlocks[2][4];
            case 6: return internalBlocks[2][5];
            case 7: return internalBlocks[2][6];
        }
        return null;
    }
    private int getThirdInputValue(int i){
        switch (i){
            case 0: return adder.getInputBlockValuesG()[0];
            case 1: return adder.getInternalBlockValuesG()[0][1];
            case 2: return adder.getInternalBlockValuesG()[1][2];
            case 3: return adder.getInternalBlockValuesG()[1][3];
            case 4: return adder.getInternalBlockValuesG()[2][4];
            case 5: return adder.getInternalBlockValuesG()[2][5];
            case 6: return adder.getInternalBlockValuesG()[2][6];
            case 7: return adder.getInternalBlockValuesG()[2][7];
        }
        return 0;

    }
    private void generateTextCin(){
        formula = new ArrayList<Text>();
        values = new ArrayList<Text>();
        Text refF = language.newText(new Coordinates(50, marginToTop - 70),"P", "p", instant, inOutProp);
        formula.add(refF);
        formula.get(0).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        formula.add(language.newText(new Offset(15, 0, refF, "NW"), "= 0", "res", instant, inOutProp));
        formula.add(language.newText(new Offset(150, 0, refF, "NW"), "G", "g", instant, inOutProp));
        formula.get(2).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        formula.add(language.newText(new Offset(165, 0, refF, "NW"), "=", "equal", instant, inOutProp));
        formula.add(language.newText(new Offset(180, 0, refF, "NW"), "C_in", "c", instant, inOutProp));
        formula.get(4).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        Text refV = language.newText(new Coordinates(200, marginToTop - 50), "G", "g", instant, inOutProp);
        values.add(refV);
        values.get(0).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        values.add(language.newText(new Offset(15, 0, refV, "NW"), "=", "equal", instant, inOutProp));
        values.add(language.newText(new Offset(30, 0, refV, "NW"), (subtract ? 1 : 0)+"", "c", instant, inOutProp));
        values.get(2).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
    }

    private void generateTextInputInit(){
        clearText();
        formula = new ArrayList<Text>();
        Text ref = language.newText(new Coordinates(50, marginToTop - 70), "P", "p", instant, inOutProp);
        formula.add(ref);
        formula.get(0).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        formula.add(language.newText(new Offset(15, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        formula.add(language.newText(new Offset(30, 0, ref, "NW"), "A", "a", instant, inOutProp));
        formula.get(2).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        formula.add(language.newText(new Offset(45, 0, ref, "NW"), "or", "or", instant, inOutProp));
        formula.add(language.newText(new Offset(70, 0, ref, "NW"), "B", "b", instant, inOutProp));
        formula.get(4).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        formula.add(language.newText(new Offset(150, 0, ref, "NW"), "G", "g", instant, inOutProp));
        formula.get(5).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        formula.add(language.newText(new Offset(165, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        formula.add(language.newText(new Offset(180, 0, ref, "NW"), "A", "a", instant, inOutProp));
        formula.get(7).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        formula.add(language.newText(new Offset(195, 0, ref, "NW"), "and", "and", instant, inOutProp));
        formula.add(language.newText(new Offset(230, 0, ref, "NW"), "B" ,"b" ,instant, inOutProp));
        formula.get(9).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
    }
    private void generateTextInput(int i){
        for (Text t: values){
            t.hide();
        }
        values = new ArrayList<Text>();
        Text ref = language.newText(new Coordinates(50, marginToTop - 50), "P", "p", instant, inOutProp);
        values.add(ref);
        values.get( 0).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        values.add(language.newText(new Offset(15, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        values.add(language.newText(new Offset(30, 0, ref, "NW"), inputA[i - 1] + "", "a", instant, inOutProp));
        values.get( 2).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        values.add(language.newText(new Offset(45, 0, ref, "NW"), "or", "or", instant, inOutProp));
        values.add(language.newText(new Offset(70, 0, ref, "NW"), inputB[i - 1] + "", "b", instant, inOutProp));
        values.get( 4).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        values.add(language.newText(new Offset(85, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        values.add(language.newText(new Offset(100, 0, ref, "NW"), adder.getInputBlockValuesP()[i] + "", "res",
                instant, inOutProp));
        values.add(language.newText(new Offset(150, 0, ref, "NW"), "G", "g", instant, inOutProp));
        values.get( 7).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        values.add(language.newText(new Offset(165, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        values.add(language.newText(new Offset(180, 0, ref, "NW"), inputA[i - 1] + "", "a", instant, inOutProp));
        values.get( 9).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        values.add(language.newText(new Offset(195, 0, ref, "NW"), "and", "and", instant, inOutProp));
        values.add(language.newText(new Offset(230, 0, ref, "NW"), inputB[i - 1] + "", "b", instant, inOutProp));
        values.get(11).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        values.add(language.newText(new Offset(245, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        values.add(language.newText(new Offset(260, 0, ref, "NW"), adder.getInputBlockValuesG()[i] + "", "res",
                instant, inOutProp));

    }

    private void generateTextInternalInit(){
        clearText();
        formula = new ArrayList<Text>();
        Text ref = language.newText(new Coordinates(50, marginToTop - 70), "P" , "p", instant, inOutProp);
        formula.add(ref);
        formula.get( 0).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        formula.add(language.newText(new Offset( 15, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        formula.add(language.newText(new Offset( 30, 0, ref, "NW"), "P_u", "Pu", instant, inOutProp));
        formula.get( 2).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        formula.add(language.newText(new Offset( 65, 0, ref, "NW"), "and", "and", instant, inOutProp));
        formula.add(language.newText(new Offset(100, 0, ref, "NW"), "P_l", "Pl", instant, inOutProp));
        formula.get( 4).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        formula.add(language.newText(new Offset(200, 0, ref, "NW"), "G", "g", instant, inOutProp));
        formula.get( 5).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        formula.add(language.newText(new Offset(215, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        formula.add(language.newText(new Offset(230, 0, ref, "NW"), "(", "bOpen", instant, inOutProp));
        formula.add(language.newText(new Offset(240, 0, ref, "NW"), "P_u", "Pu", instant, inOutProp));
        formula.get( 8).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        formula.add(language.newText(new Offset(275, 0, ref, "NW"), "and", "and", instant, inOutProp));
        formula.add(language.newText(new Offset(310, 0 ,ref, "NW"), "G_l", "Gl", instant, inOutProp));
        formula.get(10).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        formula.add(language.newText(new Offset(340, 0, ref, "NW"), ") or", "bClose", instant, inOutProp));
        formula.add(language.newText(new Offset(375, 0 ,ref, "NW"), "G_u", "Gu", instant, inOutProp));
        formula.get(12).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
    }
    private void generateTextInternal(int i, int j) {
        for (Text t: values){
            t.hide();
        }
        values = new ArrayList<Text>();
        Text ref = language.newText(new Coordinates(50, marginToTop - 50), "P", "p", instant, inOutProp);
        values.add(ref);
        values.get(0).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        values.add(language.newText(new Offset(15, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        values.add(language.newText(new Offset(30, 0, ref, "NW"), getUpperValue(i, j, true) + "", "Pu", instant, inOutProp));
        values.get(2).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        values.add(language.newText(new Offset(65, 0, ref, "NW"), "and", "and", instant, inOutProp));
        values.add(language.newText(new Offset(100, 0, ref, "NW"), getLowerValue(i, j, true) + "", "Pl", instant, inOutProp));
        values.get(4).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        values.add(language.newText(new Offset(135, 0, ref, "NW"), "= " + adder.getInternalBlockValuesP()[i][j + 1], "eq", instant, inOutProp));
        values.add(language.newText(new Offset(200, 0, ref, "NW"), "G", "g", instant, inOutProp));
        values.get(6).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        values.add(language.newText(new Offset(215, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        values.add(language.newText(new Offset(230, 0, ref, "NW"), "(", "bOpen", instant, inOutProp));
        values.add(language.newText(new Offset(240, 0, ref, "NW"), getUpperValue(i, j, true) + "", "Pu", instant, inOutProp));
        values.get(9).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        values.add(language.newText(new Offset(275, 0, ref, "NW"), "and", "and", instant, inOutProp));
        values.add(language.newText(new Offset(310, 0, ref, "NW"), getLowerValue(i, j, false) + "", "Gl", instant, inOutProp));
        values.get(11).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        values.add(language.newText(new Offset(340, 0, ref, "NW"), ") or", "bClose", instant, inOutProp));
        values.add(language.newText(new Offset(375, 0, ref, "NW"), getUpperValue(i, j, false) + "", "Gu", instant, inOutProp));
        values.get(13).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        values.add(language.newText(new Offset(410, 0, ref, "NW"), "= " + adder.getInternalBlockValuesG()[i][j + 1], "eq", instant, inOutProp));
    }
    private void generateTextOutputInit(){
        clearText();
        formula = new ArrayList<Text>();
        Text ref = language.newText(new Coordinates(50, marginToTop - 70), "S" , "s", instant, inOutProp);
        formula.add(ref);
        formula.get( 0).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        formula.add(language.newText(new Offset( 15, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        formula.add(language.newText(new Offset(30, 0, ref, "NW"), "A", "a", instant, inOutProp));
        formula.get( 2).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        formula.add(language.newText(new Offset( 45, 0, ref, "NW"), "xor", "xor1", instant, inOutProp));
        formula.add(language.newText(new Offset(75, 0, ref, "NW"), "B", "b", instant, inOutProp));
        formula.get( 4).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        formula.add(language.newText(new Offset( 90, 0, ref, "NW"), "xor", "xor2", instant, inOutProp));
        formula.add(language.newText(new Offset(120, 0, ref, "NW"), "G", "g", instant, inOutProp));
        formula.get( 6).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight3, instant, instant);
    }
    private void generateTextOutput(int i){
        for (Text t: values){
            t.hide();
        }
        values = new ArrayList<Text>();
        Text ref = language.newText(new Coordinates(50, marginToTop - 50), "S" , "s", instant, inOutProp);
        values.add(ref);
        values.get( 0).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, instant, instant);
        values.add(language.newText(new Offset(15, 0, ref, "NW"), "=", "equal", instant, inOutProp));
        values.add(language.newText(new Offset( 30, 0, ref, "NW"), inputA[i]+"", "a", instant, inOutProp));
        values.get( 2).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight1, instant, instant);
        values.add(language.newText(new Offset(45, 0, ref, "NW"), "xor", "xor1", instant, inOutProp));
        values.add(language.newText(new Offset( 75, 0, ref, "NW"), inputB[i]+"", "b", instant, inOutProp));
        values.get( 4).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight2, instant, instant);
        values.add(language.newText(new Offset(90, 0, ref, "NW"), "xor", "xor2", instant, inOutProp));
        values.add(language.newText(new Offset(120, 0, ref, "NW"), getThirdInputValue(i)+"", "g", instant, inOutProp));
        values.get( 6).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, inputHighlight3, instant, instant);
        values.add(language.newText(new Offset(135, 0, ref, "NW"), "=", "eq", instant, inOutProp));
        values.add(language.newText(new Offset(150, 0, ref, "NW"), adder.getOutputBlockValues()[i]+"", "res", instant ,inOutProp));
    }
    private void clearText(){
        for (Text t : formula){
            t.hide();
        }
        for (Text t: values){
            t.hide();
        }
    }




    public static void main(String args[]){
        Language lang = new AnimalScript("Prefix Adder", "Philipp Becker", 800, 600);
        PrefixAdderAnimation anim = new PrefixAdderAnimation(lang);
        anim.setInputs(34,56,true,false);
        anim.setProperties(new TextProperties("textProp"), new SquareProperties("squarePros"),Color.RED
        ,Color.BLUE, Color.GREEN, Color.CYAN);
        anim.animateAdder();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("AdderSim.asu"), "utf-8"))) {
            writer.write(lang.toString());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
