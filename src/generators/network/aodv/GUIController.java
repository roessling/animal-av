package generators.network.aodv;

import algoanim.primitives.Graph;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.primitives.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.aodv.guielements.GUIGraph;
import generators.network.aodv.guielements.GeometryToolBox;
import generators.network.aodv.guielements.InfoBox;
import generators.network.aodv.guielements.Tables.InfoTable;
import generators.network.aodv.guielements.Tables.StatisticTable;
import generators.network.aodv.guielements.TextToolBox;
import translator.Translator;

/**
 * The GUI controller coordinates and manages all GUI elements. It acts as an interface to
 * other classes to access those elements and perform actions on them.
 *
 * @author Sascha Bleidner, Jan David Nose
 */
public class GUIController implements AODVNodeListener{

    /**
     * The language object
     */
    private  Language lang;

    /**
     * The translator instance
     */
    private Translator translator;

    /**
     * AODV Nodes with corresponding InfoTable objects
     */
    private HashMap<AODVNode, InfoTable> tables;

    /**
     * A list of InfoTables that were updated since the last check
     */
    private ArrayList<InfoTable> lastUpdated;

    /**
     * Coordinates for the GUI Elements
     */
    private final Coordinates infoBoxUpperLeft = new Coordinates(40, 470);
    private final Coordinates infoBoxLowerRight = new Coordinates(840, 600);
    private final Coordinates tableStartingPont = new Coordinates(500, 20);
    private final Coordinates statisticTableStartingPoint = new Coordinates(500,400);
    private final int distanceBetweenTables = 30;
    private final AnimationPropertiesContainer props;

    /**
     * Text Elements
     */
    Text textDestinationNode;
    Text textStartNode;

    /**
     * GUI Elements
     */
    private StatisticTable statTable;
    private InfoBox info;
    private GUIGraph graph;

    /**
     * Properties for the highlighted color of TableCells
     */
    private RectProperties highlightCellProps;

    /**
     * Constructor for a GUIController
     * @param language object to control animal
     * @param animalGraph from animal loaded graph object
     * @param translator translator instance to translate strings for the GUI
     * @param props properties for the GUI elements
     */
    public GUIController(Language language, Graph animalGraph,Translator translator,AnimationPropertiesContainer props) {
        lang = language;
        tables = new HashMap<AODVNode, InfoTable>();
        lastUpdated = new ArrayList<InfoTable>();

        GraphProperties graphProps = (GraphProperties) props.getPropertiesByName("GraphProperties");

        graph = new GUIGraph(lang,animalGraph,graphProps);
        this.translator = translator;
        this.props = props;
    }

    /**
     * Draws the GUIGraph on the screen
     */
    public void drawGUIGraph(){
        graph.show();
    }

    /**
     * Draws the InfoTable for the given AODVNodes on the screen
     * @param nodes nodes which are connected to the InfoTables
     */
    public void drawInfoTable(ArrayList<AODVNode> nodes) {

        /**
         * Check how many tables have to be drawn in a row
         */
        int numOfTablesX = (int) Math.round((nodes.size() + 0.5) / 2);

        RectProperties cellHighlight = (RectProperties) props.getPropertiesByName("highlightColor");

        /**
         * Draw initial table in order to get the width and height for the
         * following tables
         */
        InfoTable table = new InfoTable(lang, this, nodes.get(0),
                tableStartingPont, nodes.size(), cellHighlight);

        tables.put(nodes.get(0),table);

        int offsetX = distanceBetweenTables + table.getWidth();
        int offsetY = distanceBetweenTables + table.getHeight();

        for (int i = 1; i < nodes.size(); i++) {
            table = new InfoTable(lang, this, nodes.get(i),
                    (GeometryToolBox.moveCoordinate(tableStartingPont, i
                            % numOfTablesX * offsetX, i / numOfTablesX
                            * offsetY)), nodes.size(), cellHighlight);

            tables.put(nodes.get(i), table);

        }

    }

    /**
     * Draws the startnode and endnode information on the screen
     * @param startNode startNode to be displayed
     * @param endNode endNode to be displayed
     */
    public void drawNodeInfo(AODVNode startNode, AODVNode endNode){

        TextProperties textProps = (TextProperties) props.getPropertiesByName("InfoBoxText");

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(translator.translateMessage("startNode")).append(":").append(" ");
        sBuffer.append(startNode.getNodeIdentifier());

        if (textStartNode != null){
            textStartNode.hide();
        }
        textStartNode = lang.newText(new Coordinates(860,500),sBuffer.toString(),"nodeInfo1",null,textProps);


        sBuffer = new StringBuffer();
        sBuffer.append(translator.translateMessage("endNode")).append(":").append(" ");
        sBuffer.append(endNode.getNodeIdentifier());

        if (textDestinationNode != null){
            textDestinationNode.hide();
        }
        textDestinationNode = lang.newText(new Coordinates(860,530),sBuffer.toString(),"nodeInfo2",null,textProps);
    }


    /**
     * Draws the statistic table on the screen
     * @param title title for the Statistic to draw
     */
    public void drawStatisticTable(String title){
        RectProperties cellHighlight = (RectProperties) props.getPropertiesByName("highlightColor");
        statTable = new StatisticTable(lang,statisticTableStartingPoint, title, cellHighlight);
    }

    /**
     * Draws the InfoBox on the screen
     * @param title title for the InfoBox
     */
    public void drawInfoBox(String title) {
        TextProperties textProps = (TextProperties) props.getPropertiesByName("InfoBoxText");
        RectProperties cellHighlight = (RectProperties) props.getPropertiesByName("BackgroundColor");
        info = new InfoBox(lang, title, infoBoxUpperLeft,
                infoBoxLowerRight,textProps,cellHighlight);
    }

    /**
     * Displays the startPage with description of the algorithm
     */
    public void drawStartPage(){

        lang.nextStep("StartPage");

        TextProperties title = (TextProperties) props.getPropertiesByName("TitleText");

        TextProperties bigTitle = title;
        bigTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 25));

        TextProperties text = (TextProperties) props.getPropertiesByName("DescriptionText");

        String[][] textElements = new String[][]{{"algoName","animDesc"},{"startFunctionality","aodvFunc"},{"startAnimation","aodvAnimation"}};

        int startCoordinates = 20;
        int endOfText =  startCoordinates;
        int distanceTitleToText = 20;

        for (String[] currentText: textElements){
            lang.newText(new Coordinates(50,endOfText+40),translator.translateMessage(currentText[0]),"algoName",null,title);
            endOfText = TextToolBox.multipleTextLines(lang, new Coordinates(50, endOfText+distanceTitleToText+40), translator.translateMessage(currentText[1]), text, 100);
        }

        lang.nextStep();
    }

    /**
     * Displays the end page with the complexity information of the algorithm
     */
    public void drawEndPage(){

        lang.hideAllPrimitives();

        TextProperties title = (TextProperties) props.getPropertiesByName("TitleText");

        TextProperties bigTitle = title;
        bigTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 25));

        TextProperties text = (TextProperties) props.getPropertiesByName("DescriptionText");

        String[][] textElements = new String[][]{{"algoComplexTitle","algoComplexity"}};

        int startCoordinates = 20;
        int endOfText =  startCoordinates;
        int distanceTitleToText = 20;

        for (String[] currentText: textElements){
            lang.newText(new Coordinates(50,endOfText+40),translator.translateMessage(currentText[0]),"algoName",null,title);
            endOfText = TextToolBox.multipleTextLines(lang, new Coordinates(50, endOfText+distanceTitleToText+40), translator.translateMessage(currentText[1]), text, 100);
        }

        lang.nextStep();
    }

    /**
     * Hides the start page
     */
    public void hideStartPage(){
        lang.hideAllPrimitives();
    }

    /**
     * Updates the Text in the InfoBox
     *
     * @param update Text to be displayed in the InfoBox
     */
    public void updateInfoBoxText(String update) {
        info.updateText(update);
    }

    /**
     * Marks table as updated. Should be called whenever a table gets updated
     *
     * @param table table which was updated
     */
    public void tableUpdated(InfoTable table) {
        lastUpdated.add(table);
    }

    /**
     * Resets the highlights for the last updated tables
     */
    public void tableRefresh() {
        for (InfoTable table : lastUpdated) {
            table.refresh();
        }

        lastUpdated.clear();
    }

    /**
     * Returns the animalGraph from te AODVGraph object
     *
     * @return Graph as an Graph object
     */
    public Graph getAnimalGraph(){
        return graph.getAnimalGraph();
    }

    /**
     * Returns the current instance of Translator
     * @return The current instance of Translator
     */
    @Override
    public Translator getTranslator() {
        return translator;
    }

    /**
     * Highlights the given AODVNode on the screen
     *
     * @param node Node to be highlighted
     */
    @Override
    public void highlightNode(AODVNode node) {
        graph.highlightNode(node);
    }

    /**
     * Highlights the edge from the given startnode to the given endnode
     *
     * @param startNode node from which the edge starts
     * @param endNode node to which the edge leads
     */
    @Override
    public void highlightEgde(AODVNode startNode, AODVNode endNode) {
        graph.highlightEdge(startNode,endNode);
    }

    /**
     * Tell the language object to insert a new step.
     */
    @Override
    public void nextStep() {
        lang.nextStep();
    }

    /**
     * Tell the language object to insert a new step, and set the given label.
     *
     * <b>CAUTION</b> The label will NOT be translated!
     *
     * @param label The label for the current step
     */
    @Override
    public void nextStep(String label) {
        lang.nextStep(label);
    }

    /**
     * Reset all highlights on the graph.
     */
    @Override
    public void unhighlightAll() {
        graph.unHighlightLastChanges();
    }

    /**
     * Updates the InfoTable for the given AODVNode
     *
     * @param node Node for which the table needs to be updated
     */
    @Override
    public void updateInfoTable(AODVNode node) {
        if (tables.get(node) != null) {
            tables.get(node).updateTable();
            statTable.updateStatisticTable();
        }
    }

    /**
     * Updates the InfoBox with the given message.
     *
     * <b>CAUTION</b> The message will NOT be translated!
     *
     * @param message The message to print
     */
    @Override
    public void updateInfoText(String message) {
        updateInfoBoxText(message);
    }

}
