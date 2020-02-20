/*
 * EasyNTP.java
 * Leon Würsching, Antonia Wüst, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.*;
import algoanim.primitives.Graph;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import extras.lifecycle.script.generated.node.AScript;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;

import algoanim.primitives.generators.Language;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class EasyNTP implements ValidatingGenerator {
    private static final String PR_DESCRIPTION = "desc";
    private static final String PR_ALGORITHM = "code";
    private static final String PR_OFFSET_MATRIX = "times";
    private static final String PR_GRAPH = "fullGraph";
    private static final String PR_AVG_LABEL = "avg";
    private static final String PR_SUMMARY = "summary";
    private static final String PR_CLIENT_DATE = "cDate";
    private static final String PR_CLIENT_TIME = "cTime";
    private static final String PR_CLIENT_TIME_RECT = "cTimeRect";
    private static final String PR_UPDATED_ARROW = "updatedArrow";
    private static final String PR_LOCAL_OFFSET = "determined";
    private static final String PR_DETERMINED_ARROW = "detArrow";
    private static final String PR_DETERMINED_LABEL = "determined";
    private static final String PR_DETERMINED_RECT = "detRect";
    private static final String PR_AVG_ARROW = "arrow";
    private static final String PR_TITLE = "title";
    private static final String PR_TITLE_RECT = "titleRect";

    private int clientSeconds;
    private int[] serverOffsets;
    private int localOffset;
    private Language lang;
    private TextProperties textProps;
    private TextProperties titleProps;
    private int amountOfServers;
    private PolylineProperties arrowProps;
    private MatrixProperties matrixProps;
    private RectProperties rectProps;
    private String[] serverTimes;
    private SourceCodeProperties sourceCodeProps;
    private RectProperties rectHighlightProps;
    private Node clientCoords;
    private Map<String, Primitive> primitives = new HashMap<>();

    public void init(){
        lang = new AnimalScript("Easy Network Time Protocol", "Leon Würsching, Antonia Wüst", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        textProps = (TextProperties)props.getPropertiesByName("textProps");
        titleProps = (TextProperties)props.getPropertiesByName("titleProps");
        amountOfServers = (Integer)primitives.get("amountOfServers");
        arrowProps = (PolylineProperties)props.getPropertiesByName("arrowProps");
        matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
        rectProps = (RectProperties)props.getPropertiesByName("rectProps");
        serverTimes = (String[])primitives.get("serverTimes");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        rectHighlightProps = (RectProperties)props.getPropertiesByName("rectHighlightProps");

        doNTP();

        return lang.toString();
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        int amountOfServers = (Integer) primitives.get("amountOfServers");
        String[] serverTimes = (String[]) primitives.get("serverTimes");

        if(serverTimes == null || amountOfServers <= 0 || serverTimes.length != amountOfServers) {
            return false;
        }

        this.serverOffsets = new int[amountOfServers];

        for(int i = 0; i < serverTimes.length; i++) {
            try {
                String[] parts = serverTimes[i].split(":");
                if(parts.length != 3) {
                    return false;
                }
                int h = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                int s = Integer.parseInt(parts[2]);

                if(h < 0 || m < 0 || s < 0 || h > 23 || m > 59 || s > 59) {
                    return false;
                }

                serverOffsets[i] += h * 60 * 60;
                serverOffsets[i] += m * 60;
                serverOffsets[i] += s;
                serverOffsets[i] -= 12 * 60 * 60;

            } catch(Exception e) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return "Easy Network Time Protocol";
    }

    public String getAlgorithmName() {
        return "Network Time Protocol";
    }

    public String getAnimationAuthor() {
        return "Leon Würsching, Antonia Wüst";
    }

    public String getDescription(){
        return "In dieser vereinfachten Simation des Network Time Protocols synchronisiert ein Client"
                +"\n"
                +"seine Systemzeit. Dazu kommuniziert er zun&auml;chst mit drei Servern und berechnet"
                +"\n"
                +"so den Offset zu jedem Server. Anschlie&szlig;end berechnet der Client das Mittel der"
                +"\n"
                +"Systemzeiten der Server und &uuml;bernimmt dieses als neue Systemzeit.";
    }

    public String getCodeExample(){
        return "1. Generate client"
                +"\n"
                +"2. Generate some servers"
                +"\n"
                +"3. Client gets time from servers"
                +"\n"
                +"4. Client determines new time"
                +"\n"
                +"5. Client updates time";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    private String getTitle() {
        return "Easy NTP";
    }

    private GraphProperties getGraphProps() {
        GraphProperties graphProps = new GraphProperties();
        graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, java.awt.Color.GREEN);
        graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
        graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.WHITE);
        graphProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
        graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

        return graphProps;
    }

    private void showTitleAndDescription() {
        /*
         * Show title
         */
        TextProperties myTitleProps = titleProps;
        myTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
        Text title = lang.newText(new Coordinates(10,10), getTitle(), PR_TITLE, null, myTitleProps);

        /*
         * Draw rectangle around title
         */
        drawRectangle(title, title, PR_TITLE_RECT);

        /*
         * Show description
         */
        SourceCode desc = lang.newSourceCode(new Offset(0,40, PR_TITLE, AnimalScript.DIRECTION_SW), PR_DESCRIPTION, null, sourceCodeProps);
        desc.addCodeLine("In dieser vereinfachten Simation des Network Time Protocols synchronisiert ein Client", null, 0, null);
        desc.addCodeLine("seine Systemzeit. Dazu kommuniziert er zunächst mit drei Servern und berechnet so den Offset", null, 0, null);
        desc.addCodeLine("zu jedem Server. Anschließend berechnet der Client das Mittel der Systemzeiten der Server", null, 0, null);
        desc.addCodeLine("und übernimmt dieses als neue Systemzeit.", null, 0, null);

        primitives.put(PR_TITLE, title);
        primitives.put(PR_DESCRIPTION, desc);
    }

    private void showAlgorithm() {
        /*
         * Show algorithm
         */
        SourceCode algorithm = lang.newSourceCode(new Offset(0,500, PR_TITLE, AnimalScript.DIRECTION_SW), PR_ALGORITHM,null, sourceCodeProps);
        algorithm.addCodeLine("1. Generate client", null, 0, null);
        algorithm.addCodeLine("2. Generate some servers", null, 0, null);
        algorithm.addCodeLine("3. Client gets time from servers", null, 0, null);
        algorithm.addCodeLine("4. Client determines new time", null, 0, null);
        algorithm.addCodeLine("5. Client updates time", null, 0, null);

        primitives.put(PR_ALGORITHM, algorithm);
    }

    private void showGraphClientOnly() {
        /*
         * Position client node above client date label, store position for later reference
         */
        clientCoords = new Offset(25, -40, PR_CLIENT_DATE, AnimalScript.DIRECTION_NW);

        /*
         * Show graph
         */
        Graph graph = lang.newGraph(PR_GRAPH, new int[1][1], new Node[]{clientCoords}, new String[]{"C"}, null, getGraphProps());

        primitives.put(PR_GRAPH, graph);
    }

    private void showClientLabels(String time) {
        /*
         * Position client date label centered below server labels,
         * position client time label below client date label
         */
        Primitive cDate = lang.newText(new Offset(100 + (amountOfServers / 2 * 80 + (amountOfServers % 2 == 0 ? -40 : 0)), 350, PR_TITLE, AnimalScript.DIRECTION_NE), "30.04.2019", PR_CLIENT_DATE, null, textProps);
        Primitive cTime = lang.newText(new Offset(0,5, PR_CLIENT_DATE, AnimalScript.DIRECTION_SW), time, PR_CLIENT_TIME, null, textProps);

        primitives.put(PR_CLIENT_DATE, cDate);
        primitives.put(PR_CLIENT_TIME, cTime);
    }

    private void showGraph(int[][] edges) {
        /*
         * Prepare nodes and labels for the client and all servers
         */
        Node[] positions = new Node[1+amountOfServers];
        String[] ids = new String[1+amountOfServers];

        /*
         * Set position and text of client node
         */
        positions[0] = clientCoords;
        ids[0] = getClientName();

        /*
         * Set position and text of all servers
         */
        for(int i = 0; i < amountOfServers; i++) {
            /*
             * Position server nodes below corresponding server labels
             */
            positions[1+i] = new Offset(25,20, getServerTimeId(i), AnimalScript.DIRECTION_SW);
            ids[1+i] = getServerName(i);
        }

        /*
         * Show graph
         */
        Graph graph = lang.newGraph(PR_GRAPH, edges, positions, ids, null, getGraphProps());

        primitives.put(PR_GRAPH, graph);
    }

    private void showGraphNoEdges() {
        /*
         * Show graph without edges
         */
        showGraph(new int[1+amountOfServers][1+amountOfServers]);
    }

    private void showServerLabels() {
        /*
         * Show labels for date and time for each server
         */
        for(int i = 0; i < amountOfServers; i++) {
            String dateLabelId = getServerDateId(i);
            String timeLabelId = getServerTimeId(i);

            Primitive dateLabel = lang.newText(new Offset(100+i*80, 40, PR_TITLE, AnimalScript.DIRECTION_NE), "30.04.2019", dateLabelId, null, textProps);
            Primitive timeLabel = lang.newText(new Offset(0,5, dateLabelId, AnimalScript.DIRECTION_SW), toTimeString(clientSeconds + serverOffsets[i]), timeLabelId, null, textProps);

            primitives.put(dateLabelId, dateLabel);
            primitives.put(timeLabelId, timeLabel);
        }
    }

    private void showGraph() {
        /*
         * Set edges so the client is connected to all servers
         */
        int[][] edges = new int[1+amountOfServers][1+amountOfServers];
        for(int i = 1; i < 1+amountOfServers; i++) {
            edges[0][i] = 1;
        }

        showGraph(edges);
    }

    private void showOffsetMatrix() {
        /*
         * Initialize offset matrix with 9 character white space strings for formatting reasons
         */
        String[][] data = new String[amountOfServers][1];
        for(int i = 0; i < data.length; i++) {
            data[i][0] = "         ";
        }

        /*
         * Show offset matrix
         */
        MatrixProperties timesProps = matrixProps;
        timesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 12));
        timesProps.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 50);
        StringMatrix offsetMatrix = lang.newStringMatrix(new Offset(0, 40, PR_TITLE, AnimalScript.DIRECTION_SW), data, PR_OFFSET_MATRIX, null, timesProps);

        primitives.put(PR_OFFSET_MATRIX,offsetMatrix);
    }

    private void showView(StringMatrix times) {
        /*
         * Create counter
         */
        TwoValueCounter counter = lang.newCounter(times);
        CounterProperties counterProps = new CounterProperties();
        counterProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        counterProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);

        /*
         * Create counter view
         */
        lang.newCounterView(counter, new Offset(0, -60, PR_ALGORITHM, AnimalScript.DIRECTION_NW), counterProps, true, true);
    }

    private void showAverageLabel() {
        /*
         * Show average label as string matrix
         */
        String[][] data = new String[1][1];
        data[0][0] = "AVG";
        Primitive avgLabel = lang.newStringMatrix(new Offset(23, 40, PR_OFFSET_MATRIX, AnimalScript.DIRECTION_SW), data, PR_AVG_LABEL, null, matrixProps);

        /*
         * Draw arrow from offset matrix to average label
         */
        drawVerticalArrow(PR_OFFSET_MATRIX, PR_AVG_LABEL, PR_AVG_ARROW);

        primitives.put(PR_AVG_LABEL, avgLabel);
    }

    private void showDeterminedOffset() {
        /*
         * Calculate local offset
         */
        int sum = Arrays.stream(serverOffsets)
                .filter(o -> Math.abs(o) < 10000)
                .sum();
        int count = (int) Arrays.stream(serverOffsets)
                .filter(o -> Math.abs(o) < 10000)
                .count();

        localOffset = count != 0 ? sum / count : clientSeconds;

        primitives.get(PR_OFFSET_MATRIX);

        /*
         * Draw rectangle around determined local offset
         */
        Text determined = lang.newText(new Offset(50, 0, PR_AVG_LABEL, AnimalScript.DIRECTION_NE), toTimeString(localOffset), PR_DETERMINED_LABEL, null, textProps);
        drawRectangle(determined, determined, PR_DETERMINED_RECT);

        /*
         * Draw arrow from local offset label to determined local offset
         */
        drawHorizontalArrow(PR_AVG_LABEL, PR_DETERMINED_LABEL, PR_DETERMINED_ARROW);

        primitives.put(PR_DETERMINED_LABEL, determined);
    }

    private void updateClientTime() {
        /*
         * Get references to client date, client time and update client time
         */
        Text oldDate = (Text) primitives.get(PR_CLIENT_DATE);
        Text oldTime = (Text) primitives.get(PR_CLIENT_TIME);
        oldTime.setText(toTimeString(localOffset + 12*60*60),null, null);

        /*
         * Draw highlighted rectangle around updated client time
         */
        drawRectangle(oldDate, oldTime, PR_CLIENT_TIME_RECT);

        /*
         * Draw arrow from determined local offset to updated client time
         */
        drawHorizontalArrow(PR_LOCAL_OFFSET, PR_CLIENT_TIME, PR_UPDATED_ARROW);
    }

    private void showSummary() {
        SourceCode summary = lang.newSourceCode(new Coordinates(10,80), PR_SUMMARY, null, sourceCodeProps);
        summary.addCodeLine("Durch Abfrage der Systemzeiten der Server konnte der Client den Mittelwert der im", null, 0, null);
        summary.addCodeLine("Netzwerk vorliegenden Systemzeiten berechnen und seine Lokalzeit dementsprechend", null, 0, null);
        summary.addCodeLine("anpassen.", null, 0, null);
        summary.addCodeLine("Im Gegensatz zu NTP wurden hierbei Faktoren wie die Umlaufzeit der Pakete sowie", null, 0, null);
        summary.addCodeLine("eine genauere Untersuchung der ermittelten Serverzeiten und der tatsächliche", null, 0, null);
        summary.addCodeLine("Algorithmus zur Ermittlung der neuen Lokalzeit stark vereinfacht.", null, 0, null);

        primitives.put(PR_SUMMARY, summary);
    }

    private void doNTP() {
        /*
         * Set client time to 12:00:00 and choose random server times
         */
        clientSeconds = 12*60*60;
        serverOffsets = chooseServerOffsets();

        /*
         * Show title and description
         */
        showTitleAndDescription();
        lang.nextStep("Titel und Beschreibung");

        /*
         * Hide description
         */
        primitives.get(PR_DESCRIPTION).hide();
        lang.nextStep();

        /*
         * Show showAlgorithm
         */
        showAlgorithm();
        lang.nextStep("Algorithmus");

        /*
         * Show client with date and time
         */
        SourceCode algorithm = ((SourceCode) primitives.get(PR_ALGORITHM));
        algorithm.highlight(0);
        showClientLabels(toTimeString(clientSeconds));
        showGraphClientOnly();
        lang.nextStep("1. Generiere Client");

        /*
         * Show servers with date and time
         */
        algorithm.unhighlight(0);
        algorithm.highlight(1);
        showServerLabels();
        showGraphNoEdges();
        lang.nextStep("2. Generiere Server");

        /*
         * Show connection between client and servers
         */
        showGraph();
        lang.nextStep();

        /*
         * Show matrix for server offsets
         */
        algorithm.unhighlight(1);
        algorithm.highlight(2);
        showOffsetMatrix();
        StringMatrix matrix = (StringMatrix) primitives.get(PR_OFFSET_MATRIX);
        showView(matrix);
        lang.nextStep("3. Frage Zeiten bei Servern an");

        /*
         * For each server, highlight connection and store offset in matrix
         */
        Graph graph = (Graph) primitives.get(PR_GRAPH);

        for(int i = 0; i < amountOfServers; i++) {
            if( i > 0) {
                graph.unhighlightEdge(0,i,null,null);
                matrix.unhighlightCell(i-1,0, null, null);
            }
            matrix.put(i,0, toTimeString(serverOffsets[i]), null, null);
            graph.highlightEdge(0,i+1,null,null);
            matrix.highlightCell(i, 0,null, null);
            lang.nextStep();
        }

        /* q
         * Show localOffset label and highlight all offsets in matrix
         */
        graph.unhighlightEdge(0,amountOfServers, null, null);
        matrix.unhighlightCell(amountOfServers-1,0,null,null);
        algorithm.unhighlight(2);
        algorithm.highlight(3);
        for(int i = 0; i < amountOfServers; i++) {
            matrix.highlightCell(i,0,null,null);
        }
        showAverageLabel();
        lang.nextStep("4. Werte Serverzeiten aus");

        /*
         * Unhighlight matrix and show highlighted determined local offset
         */
        for(int i = 0; i < amountOfServers; i++) {
            /*
             * Simulate read access, so it is shown in view
             */
            matrix.getElement(i,0);
            matrix.unhighlightCell(i,0,null,null);
        }
        algorithm.unhighlight(3);
        algorithm.highlight(4);
        showDeterminedOffset();
        lang.nextStep();

        /*
         * Unhighlight determined local offset and highlight updated client time
         */
        primitives.get(PR_DETERMINED_RECT).changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.WHITE, null, null);
        updateClientTime();
        lang.nextStep("5. Passe die Lokalzeit an");

        /*
         * Hide all primitives and show end text
         */
        lang.hideAllPrimitives();
        primitives.get(PR_TITLE).show();
        primitives.get(PR_TITLE_RECT).show();
        showSummary();
        lang.nextStep("Fazit");
    }

    private String toTimeString(int t) {
        StringBuilder sb = new StringBuilder();

        if (t < 0) {
            sb.append("-");
            t *= -1;
        }

        int s = t % 60;
        int m = (t/60) % 60;
        int h = t/3600;

        String hf = h < 10 ? "0" + h : "" + h;
        String mf = m < 10 ? "0" + m : "" + m;
        String sf = s < 10 ? "0" + s : "" + s;

        sb.append(hf).append(":").append(mf).append(":").append(sf);

        return sb.toString();
    }

    private int[] chooseServerOffsets() {
        if(serverOffsets != null) {
            return serverOffsets;
        }

        int[] times = new int[amountOfServers];

        /*
         * Randomly choose offset for each server
         */
        for(int i = 0; i < amountOfServers; i++) {
            /*
             * Choose if offset is positive or negative
             */
            int r = (int) (Math.random() * 2);

            /*
             * Choose offset between 0s and 10000s
             */
            int t = (int) (Math.random() * 10000);

            /*
             * Store randomly chosen server offset
             */
            times[i] = r == 1 ? t : -t;
        }

        return times;
    }

    private void drawRectangle(Primitive p1, Primitive p2, String id) {
        Node nw = new Offset(-5, -5, p1, AnimalScript.DIRECTION_NW);
        Node se = new Offset(5, 5, p2, AnimalScript.DIRECTION_SE);
        Primitive rect = lang.newRect(nw, se, PR_TITLE_RECT, null, rectProps);

        primitives.put(id, rect);
    }

    private void drawVerticalArrow(String top, String bottom, String id) {
        drawArrow(top, bottom, AnimalScript.DIRECTION_S, AnimalScript.DIRECTION_N, id);
    }

    private void drawHorizontalArrow(String left, String right, String id) {
        drawArrow(left, right, AnimalScript.DIRECTION_E, AnimalScript.DIRECTION_W, id);
    }

    private void drawArrow(String p1, String p2, String d1, String d2, String id) {
        Node from = new Offset(0,0, p1, d1);
        Node to = new Offset(0,0, p2, d2);
        Polyline arrow = lang.newPolyline(new Node[]{from, to}, id, null, arrowProps);
        primitives.put(id, arrow);
    }

    private String getClientName() {
        return "C";
    }

    private String getServerName(int i) {
        return "S" + (i+1);
    }

    private String getServerTimeId(int i) {
        return getServerName(i) + "Time";
    }

    private String getServerDateId(int i) {
        return getServerName(i) + "Date";
    }

    public static void main(String[] args) {
        Animal.startGeneratorWindow(new EasyNTP());
    }
}