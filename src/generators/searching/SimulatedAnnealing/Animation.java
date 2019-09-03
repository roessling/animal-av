package generators.searching.SimulatedAnnealing;

import algoanim.primitives.*;
import animal.variables.VariableRoles;
import generators.searching.SimulatedAnnealing.Algorithm.SimulatedAnnealing;
import generators.searching.SimulatedAnnealing.Util.*;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import generators.searching.SimulatedAnnealing.Algorithm.TemperatureFunction;
/**
 * Created by philipp on 04.08.15.
 */
public class Animation {
    //generators.searching.SimulatedAnnealing.Animation
    private Language language;
    private int WIDTH = 800;
    private int HEIGHT = 600;
    private int nrDetailed = 1;
    private double tZero = 30;
    //Primitives
    private TicksTiming instant = new TicksTiming(0);
    private Square[] referenceSq;
    private SourceCode sourceCode;
    private TwoValueCounter counter;
    //display
    private Text[] displayLines;

    // Properties
    private PolylineProperties markerProp;
    private TextProperties headerProp;
    private RectProperties canvasProp;
    private SourceCodeProperties conclusionProp;
    private PolylineProperties dataProp;
    private SourceCodeProperties textProp;
    //Computation
    private DataGenerator dataGenerator;
    private SimulatedAnnealing simulatedAnnealing;
    private double[] data;
    private int dataSize = 50;
    private int iterations = 50;
    private TemperatureFunction function  = TemperatureFunction.fast;
    //Radius in which a new point is picked. May be changed, not possible for this generator.
    private final int rad = 1;
    // Helper
    private int[] distToGround = new int[dataSize];
    private HashMap<Cases,Integer> caseMap;
    private int maxIndex;
    //Variable window
    private Variables variables;

    public Animation(Language lang){
        //init
        language = lang;
        language.setStepMode(true);
        //init Computational Classes
        this.dataGenerator = new DataGenerator();
        this.simulatedAnnealing = new SimulatedAnnealing();
        //init properties and case map
        initCaseMap();
        initFixedProperties();
    }

    /**
     * produces the actual animation by calling the single steps
     */
    public void animate(){
        //data and algorithm
        data = dataGenerator.generateData(dataSize);
        maxIndex = Tools.getMax(data);
        simulatedAnnealing.initialize(data, iterations, rad, tZero, function);
        // animation
        initVarWindow();
        drawIntroduction();
        drawBasics();
        animateCounter();
        drawData();
        initDisplay();
        markGlobalMax();
        drawFirst();
        while ( ! simulatedAnnealing.isTerminated()){
            doStep();
        }
        drawConclusion();
    }

    private void drawFirst() {
        for(int i = 0; i < displayLines.length; i++){
            displayLines[i].setText("", instant, instant);
        }
        displayLines[0].setText("Starting animation at: ", instant, instant);
        int current = simulatedAnnealing.getCurrent();
        displayLines[1].setText("Index: " + current + " Value: " + data[current], instant, instant);
        Polyline marker = drawMarker();
        language.nextStep("Start Algorithm");
        if(current == maxIndex){
            marker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, instant, instant);
        }
        else {
            marker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY, instant, instant);
        }
    }

    private void drawIntroduction(){
        Text header = language.newText(new Coordinates(7,7),"Simulated Annealing","simAnneal",instant, headerProp);
        Node[] coordsHeaderLine = {new Coordinates(0,HEIGHT/20), new Coordinates(WIDTH/2,HEIGHT/20)};
        Polyline headerLine = language.newPolyline(coordsHeaderLine,"headerLine",instant);
        SourceCode introduction = language.newSourceCode(new Offset(20, 20, headerLine, "NW"),
                "introTxt", instant);
        introduction.addMultilineCode(Txt.DESCRIPTION, "introTxt", instant);
        language.nextStep("Introduction");
        introduction.hide();
    }
    private void drawBasics(){
        Text tempFunc = language.newText(new Coordinates(WIDTH/2+5, 5),"Temperature Function: " + Txt.getTempFuncString(simulatedAnnealing.getFunction()),
                "TempFunc",instant);
        Node[] coordsHorizontalLine = {new Coordinates(WIDTH/2,0), new Coordinates(WIDTH/2,HEIGHT/2)};
        Polyline horizontalLine = language.newPolyline(coordsHorizontalLine,"horizontalLine", instant);
        Node[] coordsVerticalLine = {new Coordinates(0,HEIGHT/2), new Coordinates(WIDTH,HEIGHT/2)};
        Polyline verticalLine = language.newPolyline(coordsVerticalLine, "verticalLine", instant);
        this.sourceCode = language.newSourceCode(new Coordinates(450,20) ,"sc",instant, textProp);
        this.sourceCode.addMultilineCode(Txt.SOURCECODE, "code", instant);
    }

    private void animateCounter(){
        CounterProperties counterProperties = new CounterProperties("cProp");
        counterProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        counterProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
        String[] names = {"","Iteration"};
        MyCounterView twoValueView = new MyCounterView(language, new Coordinates(10,250), false, true,
                                                        counterProperties, names, 200/iterations,1);
        counter = new TwoValueCounter();
        counter.addCounterToView(twoValueView);
        counter.activateCounting();
        //counter.assignmentsInc((int)maxTempBarLength);
    }

    private void initCaseMap(){
        caseMap = new HashMap<Cases,Integer>();
        caseMap.put(Cases.BETTER, 0);
        caseMap.put(Cases.WORSE_ACCEPTED, 0);
        caseMap.put(Cases.WORSE_REJECTED, 0);
    }

    private void initVarWindow(){
        variables = language.newVariables();
        variables.declare("int","k");
        variables.declare("int","currentIndex");
        variables.declare("double", "currentValue");
        variables.declare("double", "maxValue", data[maxIndex] + "");
        variables.declare("double", "temperature");
        variables.declare("double", "maxIndex", maxIndex+"");

    }
    private void updateVarWindow(){
        variables.set("k", simulatedAnnealing.getIteration()+"");
        variables.set("currentIndex", simulatedAnnealing.getCurrent()+"");
        variables.set("currentValue", data[simulatedAnnealing.getCurrent()]+"");
        variables.set("temperature", simulatedAnnealing.getTemp()+"");
    }


    private void doStep(){
        simulatedAnnealing.doStep();
        counter.accessInc(1);
        updateVarWindow();
       /* double temperature = simulatedAnnealing.getTemp();
        double tempChange = tOld - temperature;
        tOld = temperature;
        counter.assignmentsInc((int) (simulatedAnnealing.getTemp() * maxTempBarLength/tZero));*/
        Polyline marker = drawMarker();
        Cases currentCase = simulatedAnnealing.getCurrentCase();
        boolean detailed = caseMap.get(currentCase) < nrDetailed;
        caseMap.put(currentCase, caseMap.get(currentCase) + 1);
        updateDisplay(detailed);
        if(! simulatedAnnealing.isLastStep()) {
            language.nextStep();
            if(simulatedAnnealing.getChosen() == maxIndex){
                marker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, instant, instant);
            }
            else {
                marker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY, instant, instant);
            }
        }
        else{
            System.out.println(simulatedAnnealing.getIteration());
        }
    }

    private Polyline drawMarker(){
        int current = simulatedAnnealing.returnCurrentResult();
        Node[] markerCoords = { new Offset(0, 0                    , referenceSq[current], "C"),
                new Offset(0, distToGround[current], referenceSq[current], "C")};
        return language.newPolyline(markerCoords, "marker", instant,markerProp);
    }


    private void drawData(){
        //parameter
        int startWidth = 50;
        int endWidth = WIDTH - 50;
        int groundLine = 550;
        int iterSize = (endWidth-startWidth)/ dataSize;
        Node[] polylineCoords = new Node[dataSize];
        referenceSq = new Square[dataSize];
        double minValue = data[Tools.getMin(data)];
        double maxValue = data[Tools.getMax(data)];
        System.out.println("min: "+ minValue+ " max: "+ maxValue);
        double normFact = (220.0) / (maxValue - minValue);
        double offset = normFact * Math.abs(minValue);

        System.out.println(normFact);
        for( int i = 0; i < dataSize; i++){
            distToGround[i] = (int) (normFact*data[i]+offset);
            Coordinates sqCoord = new Coordinates(startWidth + i * iterSize, groundLine - distToGround[i]);
            referenceSq[i] = language.newSquare(sqCoord,1,"refSq",instant);
            polylineCoords[i] = sqCoord;
        }
        Polyline graph = language.newPolyline(polylineCoords, "graph", instant,dataProp);
    }

    private void initFixedProperties(){
        headerProp = new TextProperties("txtProp");
        Font headerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
        headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);
        canvasProp = new RectProperties("canvasProp");
        canvasProp.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
        canvasProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        canvasProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        canvasProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
        conclusionProp = new SourceCodeProperties("conclusionProp");
        conclusionProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY,0);
    }

    private void initDisplay(){
        displayLines = new Text[10];
        for(int i = 0; i < displayLines.length; i++){
            displayLines[i] = language.newText(new Coordinates(20,(i+2)*20)," ", "display",instant);
        }

    }

    private void updateDisplay(boolean detailed){
        for(int i = 0; i < 15; i++){
            sourceCode.unhighlight(i);
        }
        for(int i = 0; i< displayLines.length; i++){
            displayLines[i].setText(" ",instant,instant);
        }
        if(!detailed){
            colorSourceCode();
        }
        Cases currentCase = simulatedAnnealing.getCurrentCase();
        displayLines[6].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, instant, instant);
        displayLines[9].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, instant, instant);

        displayLines[0].setText("Iteration (k): " + simulatedAnnealing.getIteration(), instant, instant);
        int old = simulatedAnnealing.getOld();
        displayLines[1].setText("Before iteration: Index: "+old   +" Value: " +data[old], instant, instant);
        if(detailed){
            String str= "";
            switch (currentCase){
                case BETTER: str = "Example: Better";
                        break;
                case WORSE_ACCEPTED: str = "Example: Worse but accepted";
                    break;
                case WORSE_REJECTED: str = "Example: Worse and rejected";
            }

            language.nextStep(str);
            sourceCode.highlight(1);
        }
        displayLines[2].setText("Current temperature: " + simulatedAnnealing.getTemp(), instant, instant);
        if(detailed){
            language.nextStep();
            sourceCode.toggleHighlight(1,2);
        }
        int chosen = simulatedAnnealing.getChosen();
        displayLines[3].setText("Chosen:           Index: " + chosen + " Value: " + data[chosen], instant, instant);
        if(detailed){
            language.nextStep();
            sourceCode.toggleHighlight(2,3);
        }
        double delta = simulatedAnnealing.getDelta();
        displayLines[4].setText("   => delta = " + delta, instant, instant);
        int newIx = simulatedAnnealing.getCurrent();
        if (currentCase == Cases.BETTER) {
            if(detailed){
                language.nextStep();
                sourceCode.toggleHighlight(3,5);
            }
            displayLines[5].setText("Delta positive: Take new value",instant, instant);

            displayLines[6].setText("  => New: Index: " + newIx + " Value: " + data[newIx], instant, instant);
            displayLines[6].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, instant, instant);
            for(int i = 7; i < displayLines.length; i++){
                displayLines[i].setText(" ",instant,instant);
            }
        } else {
            if(detailed){
                language.nextStep();
                sourceCode.toggleHighlight(3,8);
            }
            displayLines[5].setText("Delta negative: ",instant,instant);
            displayLines[6].setText("     => probability = " + simulatedAnnealing.getProp(), instant, instant);
            if(detailed){
                language.nextStep();
                sourceCode.toggleHighlight(8,9);
            }
            displayLines[7].setText("Random result: " + simulatedAnnealing.getRand(), instant, instant);
            if(currentCase == Cases.WORSE_ACCEPTED){
                if(detailed){
                    language.nextStep();
                    sourceCode.toggleHighlight(9,11);
                }
                displayLines[8].setText("Value accepted: ", instant, instant);
            }
            else{
                displayLines[8].setText("Value rejected:", instant, instant);
            }
            displayLines[9].setText("  => New: Index: " + newIx + " Value: " + data[newIx], instant, instant);
            displayLines[9].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, instant, instant);
        }
    }
    private void colorSourceCode(){
        Cases currentCase = simulatedAnnealing.getCurrentCase();
        switch (currentCase){
            case BETTER:
                sourceCode.highlight(4);
                sourceCode.highlight(5);
                sourceCode.highlight(6);
                break;
            case WORSE_ACCEPTED:
                sourceCode.highlight(7);
                sourceCode.highlight(8);
                sourceCode.highlight(9);
                sourceCode.highlight(10);
                sourceCode.highlight(11);
                sourceCode.highlight(12);
                sourceCode.highlight(13);
                break;
            case WORSE_REJECTED:
                sourceCode.highlight(7);
                sourceCode.highlight(8);
                sourceCode.highlight(9);
                sourceCode.highlight(13);
        }
    }

    private void markGlobalMax(){
        maxIndex = Tools.getMax(data);
        displayLines[0].setText("Global Maximum at: "+ maxIndex, instant, instant);
        displayLines[1].setText("Value: " + data[maxIndex],instant,instant);
        Node[] markerCoords = { new Offset(0, 0                    , referenceSq[maxIndex], "C"),
                new Offset(0, distToGround[maxIndex], referenceSq[maxIndex], "C")};
        PolylineProperties maxMarkerProps = new PolylineProperties();
        maxMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        language.newPolyline(markerCoords, "marker", instant, maxMarkerProps);
        language.nextStep();

    }

    private void drawConclusion(){
        language.nextStep("End Algorithm");
        Rect canvas = language.newRect(new Coordinates(0,35),new Coordinates(WIDTH,HEIGHT), "canvas", instant,canvasProp);
        SourceCode conclusion = language.newSourceCode(new Coordinates(10,40), "conclusion",instant,conclusionProp);
        conclusion.addMultilineCode(Txt.CONCLUSION,"con",instant);
        language.nextStep("Conclusion");
    }
    public void initPrimitives(int tZero, int dataPoints, int iterations, TemperatureFunction function){
        this.tZero = tZero;
        this.dataSize = dataPoints;
        this.iterations = iterations;
        this.function = function;
    }
    public void initProperties(SourceCodeProperties textProp, PolylineProperties markerProp, PolylineProperties dataProp){
        this.markerProp = markerProp;
        this.dataProp = dataProp;
        this.textProp = textProp;
    }

    public String toString(){
        return language.toString();
    }



    public static void main(String args[]){
        Language lang = new AnimalScript("Simulated Annealing", "Philipp Becker", 800, 600);
        Animation anim = new Animation(lang);
        anim.initPrimitives(30, 50, 50, TemperatureFunction.fast);
        PolylineProperties markerProp = new PolylineProperties();
        markerProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        SourceCodeProperties scProp = new SourceCodeProperties();
        scProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        anim.initProperties(scProp, markerProp, new PolylineProperties());
        anim.animate();

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("SimAnnealingSim.asu"), "utf-8"))) {
            writer.write(anim.toString());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
