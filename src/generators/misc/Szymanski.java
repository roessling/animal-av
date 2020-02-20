/*
 * Szymanski.java
 * Hanna Holtdirk, Robin Satter, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.animalscript.addons.Slide;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.properties.RectProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

import interactionsupport.models.InteractionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import translator.Translator;

import java.util.Locale;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.StringBuilder;
import java.awt.Color;
import java.awt.Font;


public class Szymanski implements ValidatingGenerator {
    private Language lang;
    private Locale loc;
    private Translator trans;
    // translations
    private final String title;
    private final String name;
    private final String algoName;
    // primitives
    private int numProcs;
    private double probChangeProc;
    private double probQuestion;
    private boolean askQuestions;
    private boolean chooseProcInCS;
    private TextProperties headerProps;
    private RectProperties rectProps;
    private TextProperties textProps;
    private SourceCodeProperties codeProps;
    private MatrixProperties tableProps;

    private HashMap<String, Primitive> primitivesMap;
    private int currentProc;
    private boolean[] hidden;
    private boolean entryOpen;
    private int procInCS;
    private boolean finished;
    private int[][] questions;

    public Szymanski(String path, Locale l){
        loc = l;
        trans = new Translator(path, loc);
        title = trans.translateMessage("title");
        name = trans.translateMessage("name");
        algoName = trans.translateMessage("algoName");
    }

    @Override
    public void init(){
        lang = new AnimalScript(title, "Hanna Holtdirk, Robin Satter", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        // get primitives and properties from settings
        numProcs = (Integer) primitives.get("numProcs");
        probChangeProc = (Double) primitives.get("probChangeProc");
        askQuestions = (Boolean) primitives.get("askQuestions");
        probQuestion = (Double) primitives.get("probQuestion");
        chooseProcInCS = (Boolean) primitives.get("chooseProcInCS");
        textProps = (TextProperties) props.getPropertiesByName("text");
        rectProps = (RectProperties) props.getPropertiesByName("headerBox");
        codeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
        tableProps = (MatrixProperties) props.getPropertiesByName("processTable");

        // initialize variables
        primitivesMap = new HashMap<String, Primitive>();
        currentProc = -1;
        hidden = new boolean[numProcs];
        entryOpen = true;
        procInCS = -1;
        finished = false;
        questions = new int[8][2]; // for each question: 1. times to answer question, 2. number of possible answers
        questions[0][0] = 1;
        questions[0][1] = 4;
        questions[1][0] = 1;
        questions[1][1] = 4;
        questions[2][0] = 1;
        questions[2][1] = 3;
        questions[3][0] = numProcs;
        questions[3][1] = 3;
        questions[4][0] = 1;
        questions[4][1] = 3;
        questions[5][0] = (int) numProcs/2;
        questions[5][1] = numProcs;
        questions[6][0] = (int) numProcs/2;
        questions[6][1] = 2;
        questions[7][0] = 1;
        questions[7][1] = 2;

        // display all elements initially (title, description, pseudo code, process table, waiting room)
        this.intro();

        // run szymanski until one cycle is finished
        while(!finished) {
            this.szymanskiStep();
        }

        // end
        this.end();
        
        return lang.toString();
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives){
        numProcs = (Integer) primitives.get("numProcs");
        probChangeProc = (Double) primitives.get("probChangeProc");
        probQuestion = (Double) primitives.get("probQuestion");

        return numProcs > 1 && numProcs <= 8
                && 0 <= probChangeProc && probChangeProc <= 1
                && 0 <= probQuestion && probQuestion <= 1;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public String getAlgorithmName() {
        return algoName;
    }

    @Override
    public String getAnimationAuthor() {
        return "Hanna Holtdirk, Robin Satter";
    }

    @Override
    public String getDescription(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < 5; i++)
            sb.append(trans.translateMessage("descrU" + i) + "\n");
        return sb.toString();
    }

    @Override
    public String getCodeExample(){
        return "/* "
        +"\n"
        +"N: " + trans.translateMessage("N")
        +"\n"
        +"flags: "
        +"\n"
        +"  0: " + trans.translateMessage("flagU0")
        +"\n"
        +"  1: " + trans.translateMessage("flagU1")
        +"\n"
        +"  2: " + trans.translateMessage("flagU2")
        +"\n"
        +"  3: " + trans.translateMessage("flagU3")
        +"\n"
        +"  4: " + trans.translateMessage("flagU4")
        +"\n"
        +"*/"
        +"\n"
        +"\n"
        +"1:  flag[self] = 1"
        +"\n"
        +"2:  for(j=0; j<N; j++) "
        +"\n"
        +"3:     if(flag[j] >= 3) "
        +"\n"
        +"4:         goto 2"
        +"\n"
        +"5:  flag[self] = 3"
        +"\n"
        +"6:  for(j=0; j<N; j++)"
        +"\n"
        +"7:     if(flag[j] == 1)"
        +"\n"
        +"8:        flag[self] = 2"
        +"\n"
        +"9:        for(i=0; i<N; i++)"
        +"\n"
        +"10:          if(flag[i] == 4) "
        +"\n"
        +"11:              goto 13"
        +"\n"
        +"12:       goto 9"
        +"\n"
        +"13: flag[self] = 4"
        +"\n"
        +"14: for(j=0; j<self; j++)"
        +"\n"
        +"15:    if(flag[j] >= 2) "
        +"\n"
        +"16:       goto 14"
        +"\n"
        +"\n"
        +"17: /* Critical section */"
        +"\n"
        +"\n"
        +"18: for(j=self+1; j<N; j++)"
        +"\n"
        +"19:    if(flag[j] == 2 || flag[j] == 3)"
        +"\n"
        +"20:        goto 18"
        +"\n"
        +"21: flag[self] = 0 "
        +"\n";
    }

    @Override
    public String getFileExtension(){
        return "asu";
    }

    @Override
    public Locale getContentLocale() {
        return loc;
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    @Override
    public String getOutputLanguage() {
        return ValidatingGenerator.PSEUDO_CODE_OUTPUT;
    }

    private void intro(){
        Font font = (Font) textProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
        Color color = (Color) textProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);

        // header
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font.deriveFont(Font.BOLD, 18));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        lang.newText(new Coordinates(30, 30), this.name, "header", null, headerProps);
        lang.newRect(new Offset(-15, -15, "header", "NW"), new Offset(15, 15, "header", "SE"),
                "headerBox", null, rectProps);
        lang.nextStep(trans.translateMessage("intro"));

        // description
        List<String> descrList = new ArrayList<String>();
        for(int i = 0; i < 11; i++)
            descrList.add(trans.translateMessage("descr" + i));
        InfoBox descriptionBox = new InfoBox(lang, new Coordinates(10, 100), descrList.size(), trans.translateMessage("descrTitle"));
        descriptionBox.setText(descrList);

        lang.nextStep();
        descriptionBox.hide();

        // source code
        this.displayCode();
        lang.nextStep();

        // process table
        this.displayTable(font, color);
        lang.nextStep();

        // waiting room
        this.displayWaitingRoom(font, color);
        lang.nextStep(trans.translateMessage("start"));
    }

    private void displayCode() {
        SourceCode sc = lang.newSourceCode(new Coordinates(10, 75), "sourceCode", null, codeProps);
        // add code lines
        // description of variables
        sc.addCodeLine("/* ", null, 0, null);
        sc.addCodeLine("N: " + trans.translateMessage("N"), null, 1, null);
        sc.addCodeLine("N: " + trans.translateMessage("N"), null, 1, null);
        sc.addCodeLine("flags:", null, 1, null);
        sc.addCodeLine("0: " + trans.translateMessage("flag0"), null, 2, null);
        sc.addCodeLine("1: " + trans.translateMessage("flag1"), null, 2, null);
        sc.addCodeLine("2: " + trans.translateMessage("flag2"), null, 2, null);
        sc.addCodeLine("3: " + trans.translateMessage("flag3"), null, 2, null);
        sc.addCodeLine("4: " + trans.translateMessage("flag4"), null, 2, null);
        sc.addCodeLine("*/ ", null, 0, null);
        sc.addCodeLine("", null, 0, null);
        sc.addCodeLine("", null, 0, null);
        // code
        sc.addCodeLine("1:  flag[self] = 1", null, 0, null);
        sc.addCodeLine("2:  for(j=0; j<N; j++) ", null, 0, null);
        sc.addCodeLine("3:     if(flag[j] >= 3) ", null, 0, null);
        sc.addCodeLine("4:         goto 2", null, 0, null);
        sc.addCodeLine("5:  flag[self] = 3", null, 0, null);
        sc.addCodeLine("6:  for(j=0; j<N; j++)", null, 0, null);
        sc.addCodeLine("7:     if(flag[j] == 1)", null, 0, null);
        sc.addCodeLine("8:        flag[self] = 2", null, 0, null);
        sc.addCodeLine("9:        for(i=0; i<N; i++)", null, 0, null);
        sc.addCodeLine("10:          if(flag[i] == 4) ", null, 0, null);
        sc.addCodeLine("11:              goto 13", null, 0, null);
        sc.addCodeLine("12:       goto 9", null, 0, null);
        sc.addCodeLine("13: flag[self] = 4", null, 0, null);
        sc.addCodeLine("14: for(j=0; j<self; j++)", null, 0, null);
        sc.addCodeLine("15:    if(flag[j] >= 2) ", null, 0, null);
        sc.addCodeLine("16:       goto 14", null, 0, null);
        sc.addCodeLine("17: /* Critical section */", null, 0, null);
        sc.addCodeLine("18: for(j=self+1; j<N; j++)", null, 0, null);
        sc.addCodeLine("19:    if(flag[j] == 2 || flag[j] == 3)", null, 0, null);
        sc.addCodeLine("20:        goto 18", null, 0, null);
        sc.addCodeLine("21: flag[self] = 0 ", null, 0, null);
        // add to map
        primitivesMap.put("sourceCode", sc);

        lang.nextStep();

        // describe code
        describeCode("descrCode1", "titleCode1", -60, -75, 50);
        describeCode("descrCode2", "titleCode2", 35, -20, 110);
        describeCode("descrCode3", "titleCode3", 135, 120, 50);
        describeCode("descrCode4", "titleCode4", 195, 180, 50);

    }

    private void describeCode(String descriptionName, String title, int offsetYBox, int offsetYBracket, int bracketSize) {
        List<String> description = new ArrayList<String>();
        description.add(trans.translateMessage(descriptionName));
        InfoBox descriptionBox = new InfoBox(lang, new Offset(320, offsetYBox, "sourceCode", "W"), description.size(), trans.translateMessage(title));
        descriptionBox.setText(description);
        TextProperties bracketProps = new TextProperties();
        bracketProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, bracketSize));
        Text bracket = lang.newText(new Offset(280, offsetYBracket, "sourceCode", "W"), "}", "bracket", null, bracketProps);
        lang.nextStep();
        descriptionBox.hide();
        bracket.hide();
    }

    private void displayTable(Font font, Color color) {
        String[][] entries = new String[numProcs + 1][4];
        entries[0] = new String[]{"process", "line", "flag", "j", "i"};

        for(int i=0; i<numProcs; i++){
            entries[i+1] = new String[]{"p"+i, "0", "0", "-", "-"};
        }
        primitivesMap.put("procTable", lang.newStringMatrix(new Offset(300, 0, "sourceCode", "W"),
                entries, "procTable", null, tableProps));

        // title
        TextProperties tableTitleProps = new TextProperties();
        tableTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font.deriveFont(Font.BOLD, 16));
        tableTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        tableTitleProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        primitivesMap.put("tableTitle", lang.newText(new Offset(0, -30, "procTable", "N"), trans.translateMessage("procTable"), "tableTitle", null, tableTitleProps));

        lang.nextStep();

        // describe table
        List<String> descrTable = new ArrayList<String>();
        for(int i = 0; i < 5; i++)
            descrTable.add(trans.translateMessage("descrTable" + i));
        InfoBox descrBox = new InfoBox(lang, new Offset(50, 20, "procTable", "NE"), descrTable.size(), trans.translateMessage("procTable"));
        descrBox.setText(descrTable);
        lang.nextStep();
        descrBox.hide();
    }

    private void displayWaitingRoom(Font font, Color color) {
        TextProperties circleTextProps = new TextProperties();
        circleTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font.deriveFont(Font.BOLD));
        circleTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

        // state nodes
        lang.newText(new Offset(100, -50, "procTable", "E"), "0", "t0", null, circleTextProps);
        lang.newCircle(new Offset(0, 0, "t0", "C"), 20, "s0", null, new CircleProperties());
        lang.newText(new Offset(200, -50, "procTable", "E"), "1", "t1", null,  circleTextProps);
        lang.newCircle(new Offset(0, 0, "t1", "C"), 20, "s1", null, new CircleProperties());
        lang.newText(new Offset(300, -50, "procTable", "E"), "3", "t3", null,  circleTextProps);
        lang.newCircle(new Offset(0, 0, "t3", "C"), 20, "s3", null, new CircleProperties());
        lang.newText(new Offset(400, -50, "procTable", "E"), "2", "t2", null,  circleTextProps);
        lang.newCircle(new Offset(0, 0, "t2", "C"), 20, "s2", null, new CircleProperties());
        lang.newText(new Offset(400, 50, "procTable", "E"), "4", "t4", null,  circleTextProps);
        lang.newCircle(new Offset(0, 0, "t4", "C"), 20, "s4", null, new CircleProperties());
        lang.newText(new Offset(400, 175, "procTable", "E"), "CS", "tCS", null,  circleTextProps);
        lang.newCircle(new Offset(0, 0, "tCS", "C"), 25, "sCS", null, new CircleProperties());

        // room
        lang.newPolyline(new Offset[]{new Offset(-22, -22, "t3", "N"),
                new Offset(-22, -80, "t3", "N"),
                new Offset(200, -80, "t3", "N"),
                new Offset(200, 140, "t3", "N"),
                new Offset(130, 140, "t3", "N")}, "lNE", null, new PolylineProperties());
        lang.newPolyline(new Offset[]{new Offset(-22, 40, "t3", "N"),
                new Offset(-22, 140, "t3", "N"),
                new Offset(70, 140, "t3", "N")}, "lSW", null, new PolylineProperties());
        primitivesMap.put("entryDoor", lang.newPolyline(new Offset[]{
                new Offset(-22, 40, "t3", "N"),
                new Offset(-22, -20, "t3", "N")},
                "entryDoor", null, new PolylineProperties()));
        primitivesMap.put("exitDoor", lang.newPolyline(new Offset[]{
                        new Offset(130, 140, "t3", "N"),
                        new Offset(70, 140, "t3", "N")},
                "exitDoor", null, new PolylineProperties()));
        CircleProperties pointProps = new CircleProperties();
        pointProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        lang.newCircle( new Offset(0, 0, "entryDoor", "S"), 3, "pointEntry", null, pointProps);
        lang.newCircle( new Offset(0, 0, "exitDoor", "E"), 3, "pointExit", null, pointProps);

        // title
        TextProperties roomTitleProps = new TextProperties();
        roomTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font.deriveFont(Font.BOLD, 16));
        roomTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        roomTitleProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        lang.newText(new Offset(0, -30, "lNE", "N"), trans.translateMessage("waitingRoom"), "roomTitle", null, roomTitleProps);

        lang.nextStep();
        openDoor("entryDoor");

        // movelines
        PolylineProperties movelineProps = new PolylineProperties();
        movelineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
        primitivesMap.put("moveline01", lang.newPolyline(new Offset[]{
                        new Offset(0, 0, "s0", "N"),
                        new Offset(0, 0, "s1", "N")},
                "moveline01", null, movelineProps));
        primitivesMap.put("moveline13", lang.newPolyline(new Offset[]{
                        new Offset(0, 0, "s1", "N"),
                        new Offset(0, 0, "s3", "N")},
                "moveline13", null, movelineProps));
        primitivesMap.put("moveline32", lang.newPolyline(new Offset[]{
                        new Offset(0, 0, "s3", "N"),
                        new Offset(0, 0, "s2", "N")},
                "moveline32", null, movelineProps));
        primitivesMap.put("moveline24", lang.newPolyline(new Offset[]{
                        new Offset(0, 0, "s2", "N"),
                        new Offset(0, 0, "s4", "N")},
                "moveline24", null, movelineProps));
        primitivesMap.put("moveline34", lang.newPolyline(new Offset[]{
                        new Offset(0, 0, "s3", "N"),
                        new Offset(0, 0, "s4", "N")},
                "moveline34", null, movelineProps));
        primitivesMap.put("moveline4CS", lang.newPolyline(new Offset[]{
                        new Offset(0, 0, "s4", "N"),
                        new Offset(0, 0, "sCS", "N")},
                "moveline4CS", null, movelineProps));
        primitivesMap.put("movelineCS0", lang.newPolyline(new Offset[]{
                        new Offset(0, 0, "sCS", "N"),
                        new Offset(0, 0, "s0", "N")},
                "movelineCS0", null, movelineProps));

        // process arrows
        PolylineProperties arrowProps = new PolylineProperties();
        arrowProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
        TextProperties arrowTitleProps = new TextProperties();
        arrowTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font.deriveFont(Font.PLAIN, 12));
        arrowTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        arrowTitleProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        for(int i=0; i < numProcs; i++){
            primitivesMap.put("p"+i, lang.newPolyline(new Offset[]{
                            new Offset(0, 0, "s0", "N"),
                            new Offset(0, -40, "s0", "N")},
                    "p"+i, null, arrowProps));
            Text t = lang.newText(new Offset(0, -20, "p"+i, "N"), "p"+i, "titleP"+i, null, arrowTitleProps);
            primitivesMap.put("titleP"+i, t);
            if(i != 0){
                t.hide();
                hidden[i] = true;
            }
        }
    }

    private void openDoor(String doorName) {
        Polyline door = (Polyline) primitivesMap.get(doorName);
        door.rotate(door.getNodes()[0], 135, new TicksTiming(0), new TicksTiming(20));
    }

    private void closeDoor(String doorName) {
        Polyline door = (Polyline) primitivesMap.get(doorName);
        door.rotate(door.getNodes()[0], -135, new TicksTiming(0), new TicksTiming(20));
    }


    private void szymanskiStep () {
        int proc = getNextProcess();
        int oldProcInCS = procInCS;
        boolean oldEntryOpen = entryOpen;
        StringMatrix m = (StringMatrix) primitivesMap.get("procTable");
        SourceCode sc = (SourceCode) primitivesMap.get("sourceCode");
        int line = Integer.parseInt(m.getElement(proc+1, 1));
        int state = Integer.parseInt(m.getElement(proc+1, 2));
        String jCounter = m.getElement(proc+1, 3);
        String iCounter = m.getElement(proc+1, 4);
        int j = jCounter.equals("-") ? -1 : Integer.parseInt(jCounter);
        int i = iCounter.equals("-") ? -1 : Integer.parseInt(iCounter);
        int flagJ = j == -1 || j == numProcs ? -1 : Integer.parseInt(m.getElement(j+1, 2));
        int flagI = i == -1 || i == numProcs ? -1 : Integer.parseInt(m.getElement(i+1, 2));
        boolean questionAsked = false;

        int newLine;
        // line switch
        sc.unhighlight(line+10);
        switch (line) {
            // goto's
            case 4:
                newLine = changeLineTo(proc,2);
                setCounter(0, proc, 0, true, false);
                break;
            case 11:
                newLine = changeLineTo(proc,13);
                setCounter(0, proc, 0, false, true);
                setCounter(1, proc, 0, false, true);
                break;
            case 12:
                newLine = changeLineTo(proc,9);
                setCounter(1, proc, 0, true, false);
                break;
            case 16:
                newLine = changeLineTo(proc,14);
                setCounter(0, proc, 0, true, false);
                if(askQuestions && Math.random() <= probQuestion)
                    questionAsked = generateMSQuestion(3, proc);
                break;
            case 20:
                newLine = changeLineTo(proc,18);
                setCounter(0, proc, proc+1, true, false);
                break;
            // if's
            case 3:
                if(flagJ >= 3) { // flag[j] >= 3
                    newLine = changeLineTo(proc,line+1);
                } else {
                    newLine = changeLineTo(proc,2);
                }
                break;
            case 7:
                if(flagJ == 1) { // flag[j] == 1
                    newLine = changeLineTo(proc,line+1);
                } else {
                    newLine = changeLineTo(proc,6);
                }
                break;
            case 10:
                if(flagI == 4) { // flag[i] == 4
                    newLine = changeLineTo(proc,line+1);
                } else {
                    newLine = changeLineTo(proc,9);
                }
                break;
            case 15:
                if(flagJ >= 2) { // flag[j] >= 2
                    newLine = changeLineTo(proc,line+1);
                } else {
                    newLine = changeLineTo(proc,14);
                }
                break;
            case 19:
                if(flagJ == 2 || flagJ == 3) { // flag[j] == 2 || flag[j] == 3
                    newLine = changeLineTo(proc,line+1);
                } else {
                    newLine = changeLineTo(proc,18);
                }
                break;

            // for's
            case 2:
                if(j == numProcs) {
                    newLine = changeLineTo(proc, 5);
                    setCounter(0, proc, 0, false, true);
                } else {
                    newLine = changeLineTo(proc,line+1);
                }
                break;
            case 6:
                if(j == numProcs) {
                    newLine = changeLineTo(proc, 13);
                    setCounter(0, proc, 0, false, true);
                } else {
                    newLine = changeLineTo(proc,line+1);
                }
                break;
            case 9:
                if(i == numProcs) {
                    newLine = changeLineTo(proc, 12);
                    setCounter(1, proc, 0, true, false);
                } else {
                    newLine = changeLineTo(proc,line+1);
                }
                break;
            case 14:
                if(j == proc) {
                    newLine = changeLineTo(proc, 17);
                    setCounter(0, proc, 0, false, true);
                } else {
                    newLine = changeLineTo(proc,line+1);
                }
                break;
            case 18:
                if(j == numProcs) {
                    newLine = changeLineTo(proc, 21);
                    setCounter(0, proc, 0, false, true);
                } else {
                    newLine = changeLineTo(proc,line+1);
                }
                break;

            // last line
            case 21: newLine = changeLineTo(proc, 1); break;

            default: newLine = changeLineTo(proc,line+1);
        }

        // state switch
        switch (newLine) {
            case 1:
                moveArrow(proc, "0", "1");
                if(askQuestions && Math.random() <= probQuestion)
                    questionAsked = generateMSQuestion(4, proc);
                break;
            case 5: moveArrow(proc, "1", "3"); break;
            case 8: moveArrow(proc, "3", "2"); break;
            case 13:
                moveArrow(proc, "" + state, "4");
                if(entryOpen) {
                    closeDoor("entryDoor");
                    openDoor("exitDoor");
                    entryOpen = false;
                }
                break;
            case 21:
                moveArrow(proc, "CS", "0");
                procInCS = -1;
                boolean last = true;
                for(int p = proc; p < numProcs; p++) {
                    if(Integer.parseInt(m.getElement(p+1, 2)) == 4) {
                        last = false;
                        break;
                    }
                }
                if(last) {
                    openDoor("entryDoor");
                    closeDoor("exitDoor");
                    entryOpen = true;
                    finished = true;
                }
                break;
            case 17: moveArrow(proc, "4", "CS"); break;
        }

        // for-loop counter
        switch (newLine) {
            case 2:
                if(line != 4)
                    setCounter(0, proc, 0, false, false);
                break;
            case 6:
                setCounter(0, proc, 0, false, false);
                break;
            case 9:
                if(line != 12)
                    setCounter(1, proc, 0, false, false);
                break;
            case 14:
                if(line != 16)
                    setCounter(0, proc, 0, false, false);
                break;
            case 18:
                if(line != 20)
                    setCounter(0, proc, proc+1, false, false);
                break;
        }

        // questions
        if(!questionAsked && askQuestions && Math.random() <= probQuestion) {
            int qNum = (int) (Math.random() * (questions.length - 2)); // random question
            if(qNum == 3 || qNum == 4)
                qNum += questions.length - 5;
            if(questions[qNum][0] > 0) {
                switch(qNum) {
                    case 0:
                        if(entryOpen)
                            generateMCQuestion(qNum);
                        break;
                    case 1:
                        if(!entryOpen)
                            generateMCQuestion(qNum);
                        break;
                    case 2: generateMCQuestion(qNum); break;
                    case 5:
                        int numFour = 0;
                        int answer = -1;
                        for(int k=0; k < numProcs; k++) {
                            if("234".contains(m.getElement(k+1, 2)) && answer == -1 && procInCS != k)
                                answer = k;
                            if(m.getElement(k+1, 2).equals("4"))
                                numFour++;
                        }
                        if(numFour >= 2)
                            generateMSQuestion(qNum, answer);
                        break;
                    case 6:
                        int procIn01 = -1;
                        for(int k=0; k < numProcs; k++) {
                            if(m.getElement(k+1, 2).equals("0") || m.getElement(k+1, 2).equals("1")) {
                                procIn01 = k;
                                break;
                            }
                        }
                        if(procIn01 >= 0)
                            generateTFQuestion(6, entryOpen, proc);
                        break;
                    case 7:
                        generateTFQuestion(7, false, proc);
                }
            }
        }

        // next step
        if(oldProcInCS != procInCS && procInCS != -1)
            lang.nextStep("P" + procInCS + " " + trans.translateMessage("reachedCS"));
        else if(oldEntryOpen != entryOpen)
            lang.nextStep(trans.translateMessage("entryClosed"));
        else
            lang.nextStep();
    }

    private boolean generateMCQuestion(int num) {
        if(questions[num][0] > 0) {
            MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel("question" + num + "." + questions[num][0]);
            mcq.setPrompt(trans.translateMessage("question" + num));
            for(int i = 1; i <= questions[num][1]; i++) {
                String feedback = trans.translateMessage("feedback" + num + "_" + i);
                int points = 0;
                if (feedback.toLowerCase().contains("richtig") || feedback.toLowerCase().contains("correct"))
                    points = 1;
                mcq.addAnswer(trans.translateMessage("answer" + num + "_" + i), points, feedback);
            }
            lang.addMCQuestion(mcq);
            questions[num][0]--;
            return true;
        }
        return false;
    }

    private boolean generateMSQuestion(int num, int process) {
        if(questions[num][0] > 0) {
            MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel("question" + num + "." + questions[num][0]);
            if(num == 3) {
                msq.setPrompt(trans.translateMessage("question" + num + "_1") + process + " " + trans.translateMessage("question" + num + "_2"));
                String posFeedback = trans.translateMessage( "posFeedback" + num);
                String negFeedback = trans.translateMessage( "negFeedback" + num);
                boolean correct = procInCS != - 1;
                msq.addAnswer(trans.translateMessage("answer" + num + "_1"),
                        correct ? 1 : -1, correct ? posFeedback : negFeedback);
                StringMatrix m = (StringMatrix) primitivesMap.get("procTable");
                correct = false;
                for(int i=0; i < process; i++)
                    if("234".contains(m.getElement(i+1, 2)) && procInCS != i)
                        correct = true;
                msq.addAnswer(trans.translateMessage("answer" + num + "_2"),
                        correct ? 1 : -1, correct ? posFeedback : negFeedback);
                correct = entryOpen;
                msq.addAnswer(trans.translateMessage("answer" + num + "_3"),
                        correct ? 1 : -1, correct ? posFeedback : negFeedback);
            } else if(num == 5) {
                msq.setPrompt(trans.translateMessage("question" + num));
                for(int i=0; i < numProcs; i++) {
                    if(i == process)
                        msq.addAnswer("p"+i, 1, "P" + i + " " + trans.translateMessage("posFeedback" + num));
                    else
                        msq.addAnswer("p"+i, -1, "P" + i + " " + trans.translateMessage("negFeedback" + num));
                }
            } else {
                msq.setPrompt(trans.translateMessage("question" + num));
                for (int i = 1; i <= questions[num][1]; i++) {
                    String feedback = trans.translateMessage("feedback" + num + "_" + i);
                    int points = -1;
                    if (feedback.toLowerCase().contains("richtig") || feedback.toLowerCase().contains("correct"))
                        points = 1;
                    msq.addAnswer(trans.translateMessage("answer" + num + "_" + i), points, feedback);
                }
            }
            lang.addMSQuestion(msq);
            questions[num][0]--;
            return true;
        }
        return false;
    }

    private boolean generateTFQuestion(int num, boolean answer, int process) {
        if(questions[num][0] > 0) {
            TrueFalseQuestionModel tfq = new TrueFalseQuestionModel("question" + num + "." + questions[num][0]);
            if(num == 6)
                tfq.setPrompt(trans.translateMessage("question" + num + "_1") + process + " " + trans.translateMessage("question" + num + "_2"));
            else
                tfq.setPrompt(trans.translateMessage("question" + num));
            tfq.setCorrectAnswer(answer);
            tfq.setFeedbackForAnswer(answer, trans.translateMessage("posFeedbackTF"));
            tfq.setFeedbackForAnswer(!answer, trans.translateMessage("negFeedbackTF"));
            tfq.setPointsPossible(1);
            lang.addTFQuestion(tfq);
            questions[num][0]--;
            return true;
        }
        return false;
    }

    private int getNextProcess() {
        StringMatrix m = (StringMatrix) primitivesMap.get("procTable");
        SourceCode sc = (SourceCode) primitivesMap.get("sourceCode");

        // choose next process depending on given probability
        int newProc = (int) (Math.random() * (numProcs + numProcs*(1-probChangeProc)/probChangeProc));
        if(newProc >= numProcs)
            newProc = currentProc >=0 ? currentProc : 0;

        // choose proc in CS if it can leave the CS (to decrese waiting time for procs for more clarity)
        if(chooseProcInCS && procInCS != -1) {
            boolean canLeave = true;
            for (int i = procInCS + 1; i < numProcs; i++) {
                int flag = Integer.parseInt(m.getElement(i + 1, 2));
                if (flag == 2 || flag == 3)
                    canLeave = false;
            }
            if (canLeave)
                newProc = procInCS;
        }

        // highlight table row and current code line of the new process
        if(currentProc >= 0){
            m.unhighlightCell(currentProc+1, 0, null, null);
            m.unhighlightCell(currentProc+1, 1, null, null);
            m.unhighlightCell(currentProc+1, 2, null, null);
            m.unhighlightCell(currentProc+1, 3, null, null);
            m.unhighlightCell(currentProc+1, 4, null, null);
            sc.unhighlight(Integer.parseInt(m.getElement(currentProc+1, 1)) + 10);
        }
        m.highlightCellColumnRange(newProc+1, 0, 4, null, null);
        sc.highlight(Integer.parseInt(m.getElement(newProc+1, 1)) + 10);
        currentProc = newProc;

        return currentProc;
    }

    private void moveArrow(int proc, String from, String to) {
        String movelineName = "moveline" + from + to;
        StringMatrix m = (StringMatrix) primitivesMap.get("procTable");
        Polyline moveline = (Polyline) primitivesMap.get(movelineName);

        Polyline pArrow = (Polyline) primitivesMap.get("p"+proc);
        Text pTitle = (Text) primitivesMap.get("titleP"+proc);

        if(to.equals("CS"))
            procInCS = proc;

        if(hidden[proc]) {
            pTitle.show();
            hidden[proc] = false;
        }

        boolean showFirst = false;
        for(int i=0; i<numProcs; i++) {
            if(i != proc && m.getElement(i+1, 2).equals(from) && procInCS != i) {
                if(hidden[i] && !showFirst){
                    primitivesMap.get("titleP"+i).show();
                    hidden[i] = false;
                }
                if(!hidden[i] && showFirst) {
                    primitivesMap.get("titleP" + i).hide();
                    hidden[i] = true;
                }
                showFirst = true;
            }
            if(m.getElement(i+1, 2).equals(to) && !hidden[i] && procInCS != i) {
                primitivesMap.get("titleP" + i).hide(new TicksTiming(20));
                hidden[i] = true;
            }
        }

        pArrow.moveVia(null, "translate", moveline, new TicksTiming(0), new TicksTiming(20));
        pTitle.moveVia(null, "translate", moveline, new TicksTiming(0), new TicksTiming(20));

        // change flag
        if(!movelineName.equals("moveline4CS")){
            m.put(proc+1, 2, movelineName.substring(movelineName.length() - 1), null, null);
        }
    }

    private int changeLineTo(int proc, int line) {
        StringMatrix m = (StringMatrix) primitivesMap.get("procTable");
        SourceCode sc = (SourceCode) primitivesMap.get("sourceCode");
        sc.highlight(line + 10);
        m.put(proc+1, 1, "" + line, null, null);
        return line;
    }

    private void setCounter(int counterNum, int proc, int initValue, boolean init, boolean reset) {
        StringMatrix table = (StringMatrix) primitivesMap.get("procTable");
        String oldVal = table.getElement(proc+1, 3+counterNum);
        String newVal;
        if(oldVal.equals("-") || init)
            newVal = "" + initValue;
        else if(reset)
            newVal = "-";
        else
            newVal = "" + (Integer.parseInt(oldVal) + 1);
        table.put(proc+1, 3+counterNum, newVal, null, null);
    }

    private void end() {
        primitivesMap.get("procTable").hide();
        primitivesMap.get("sourceCode").hide();
        primitivesMap.get("tableTitle").hide();
        List<String> concl = new ArrayList<String>();
        for(int i = 0; i < 13; i++)
            concl.add(trans.translateMessage("concl" + i));
        InfoBox conclBox = new InfoBox(lang, new Coordinates(20, 200), concl.size(), trans.translateMessage("conclTitle"));
        conclBox.setText(concl);
        lang.nextStep(trans.translateMessage("conclTitle"));

        lang.finalizeGeneration();
    }
}
