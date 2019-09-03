/*
 * NegaMax.java
 * Adrian Wagener, Ivelin Ivanov, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */


package generators.searching;

import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.properties.*;
import algoanim.util.*;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;
import java.util.List;

import algoanim.primitives.generators.Language;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import generators.searching.minmax.TreeParserWithNodeNames;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;

import javax.swing.*;

@SuppressWarnings("unused")
public class NegaMax implements ValidatingGenerator {
    private Language lang;
    private Translator translator;
    private Text title;
    private TextProperties titleProperties;
    private Rect titleRect;
    private RectProperties smallTitleRectangleProperties;
    private RectProperties bigTitleRectangleProperties;
    private Polyline hLine;
    private PolylineProperties titleLineProperties;
    private Text desc;
    private TextProperties descProperties;
    private String gametree;
    private Graph graph;
    private GraphProperties graphProperties;
    private SourceCode sc;
    private SourceCodeProperties sourceCodeProperties;
    private Text[] nodeBestValues;
    private TextProperties nodeValueProperties;
    private Text actualPlayer;
    private TextProperties actualPlayerProperties;
    private Text[] out = new Text[4];
    private TextProperties outroProperties;
    private Rect[] outRect = new Rect[3];
    private int value = Integer.MIN_VALUE;
    private int finalValue = Integer.MIN_VALUE;
    private int lastHighlighted = 0;
    private int startPlayer;
    private Coordinates[] graphNodes;
    private int treeDepth;
    private int leftBorder;
    private int upperBorder;
    private int xLeafDist;
    private int yNodeDist;
    private int xBestValOffset;
    private int yBestValOffset;
    private boolean askQuestion;
    private Random rand = new Random();
    private int lastAskedQuestion;
    private int r;


    public NegaMax(String resource, Locale locale) {
        // Translator
        translator = new Translator(resource, locale);
        // Language
        lang = new AnimalScript(translator.translateMessage("algorithmName"), "Adrian Wagener, Ivelin Ivanov", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }
    
    /************************************************************************************
     **************************** Generator Framework ***********************************
     ************************************************************************************/
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        titleLineProperties = (PolylineProperties) props.getPropertiesByName("titleLine");
        gametree = (String) primitives.get("gametree");
        nodeValueProperties = (TextProperties) props.getPropertiesByName("nodeValue");
        bigTitleRectangleProperties = (RectProperties) props.getPropertiesByName("bigTitleRectangle");
        sourceCodeProperties = (SourceCodeProperties) props.getPropertiesByName("sourcecode");
        titleProperties = (TextProperties) props.getPropertiesByName("title");
        descProperties = (TextProperties) props.getPropertiesByName("description");
        smallTitleRectangleProperties = (RectProperties) props.getPropertiesByName("smallTitleRectangle");
        startPlayer = (Integer) primitives.get("startplayer");
        graphProperties = (GraphProperties) props.getPropertiesByName("graph");
        graph = getGraph(graphProperties, gametree);
        graph.hide();
        actualPlayerProperties = (TextProperties) props.getPropertiesByName("actualPlayer");
        outroProperties = (TextProperties) props.getPropertiesByName("outro");
        askQuestion = (Boolean) primitives.get("askQuestion");

        start();
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getAlgorithmName() {
        return translator.translateMessage("algorithmName");
    }

    public String getName() {
        return translator.translateMessage("generatorName");
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return translator.getCurrentLocale();
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public String getAnimationAuthor() {
        return "Adrian Wagener, Ivelin Ivanov";
    }

    public String getDescription() {
        return translator.translateMessage("descFile[0]") + translator.translateMessage("descFile[1]") + translator.translateMessage("descFile[2]")
                + translator.translateMessage("descFile[3]") + translator.translateMessage("descFile[4]") + translator.translateMessage("descFile[5]")
                + translator.translateMessage("descFile[6]") + translator.translateMessage("descFile[7]") + translator.translateMessage("descFile[8]")
                + translator.translateMessage("descFile[9]") + translator.translateMessage("descFile[10]") + translator.translateMessage("descFile[11]")
                + translator.translateMessage("descFile[12]") + translator.translateMessage("descFile[13]") + translator.translateMessage("descFile[14]") + translator.translateMessage("intermission")
                + translator.translateMessage("descFile[15]") + " { " + translator.translateMessage("descFile[16]") + " { " + translator.translateMessage("descFile[17]") + " { "
                + translator.translateMessage("descFile[18]") + " { " + translator.translateMessage("descFile[19]") + " { " + translator.translateMessage("descFile[20]")
                + translator.translateMessage("descFile[21]") + translator.translateMessage("descFile[22]") + " { " + translator.translateMessage("descFile[23]") + " { "
                + translator.translateMessage("descFile[24]") + " { " + translator.translateMessage("descFile[25]") + " { " + translator.translateMessage("descFile[26]")
                + translator.translateMessage("descFile[27]") + translator.translateMessage("descFile[28]");
    }

    public String getCodeExample() {
        return "    public int run(Node state, int player) {"
                + "\n"
                + "        if (state.isLeaf()) {"
                + "\n"
                + "            return player * state.getValue();"
                + "\n"
                + "        }"
                + "\n"
                + "        value = Integer.MIN_VALUE;"
                + "\n"
                + "        for (Node childState : state.getChildren()) {"
                + "\n"
                + "            value = Math.max(value, -run(childState, -player));"
                + "\n"
                + "        }"
                + "\n"
                + "        return value;"
                + "\n"
                + "    }";
    }

    public void init() {
        // Language setup
        lang = new AnimalScript(translator.translateMessage("algorithmName"), "Adrian Wagener, Ivelin Ivanov", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(1024);
        // Offset setup for graph generation
        upperBorder = 125;
        leftBorder = 66;
        xLeafDist = 38;
        yNodeDist = 60;
        treeDepth = 0;
    }

    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        String errormsg = "";
        boolean valid = true;
        String treeString = (String) primitives.get("gametree");
        int startplayer = (Integer) primitives.get("startplayer");
        TreeParserWithNodeNames treeparser = new TreeParserWithNodeNames();
        treeparser.parseText(treeString);
        if (!treeparser.isValid()) {
            errormsg = errormsg + translator.translateMessage("parseError[0]") + treeparser.getMessage() + translator.translateMessage("parseError[1]") + treeString;
            valid = false;
        } else if (startplayer != 1 && startplayer != -1) {
            valid = false;
            if (!treeparser.isValid())
                errormsg = errormsg + translator.translateMessage("parseError[2]") + startplayer;
            else
                errormsg = errormsg + translator.translateMessage("parseError[3]") + startplayer;
        }

        if (!valid)
            showErrorWindow(errormsg);

        return valid;
    }

    /************************************************************************************
     *************************** Starting the Animation *********************************
     ************************************************************************************/
    public void start() {
        // titleProperties
        Font fontTitle = (Font) titleProperties.get("font");
        fontTitle = fontTitle.deriveFont(1, 36);
        titleProperties.set("font", fontTitle);
        title = lang.newText(new Coordinates(50, 30), translator.translateMessage("algorithmName"), "title", null, titleProperties);
        // horizontal line under the title
        Node[] node = new Node[]{new Offset(0, 10, "title", "SW"), new Offset(0, 10, "title", "SE")};
        hLine = lang.newPolyline(node, "title", null, titleLineProperties);
        hLine.hide();
        // rectangle for the title
        titleRect = lang.newRect(new Offset(-5, 10, "title", "SW"), new Offset(5, -10, "title", "NE"), "titleRect", null, smallTitleRectangleProperties);
        // description
        Font fontDesc = (Font) descProperties.get("font");
        fontDesc = fontDesc.deriveFont(0, 16);
        descProperties.set("font", fontDesc);
        desc = lang.newText(new Offset(0, 20, "title", "SW"), translator.translateMessage("description[0]"), "desc[0]", null, descProperties);
        desc = lang.newText(new Offset(0, 10, "desc[0]", "SW"), translator.translateMessage("description[1]"), "desc[1]", null, descProperties);
        desc = lang.newText(new Offset(0, 10, "desc[1]", "SW"), translator.translateMessage("description[2]"), "desc[2]", null, descProperties);
        desc = lang.newText(new Offset(0, 10, "desc[2]", "SW"), translator.translateMessage("description[3]"), "desc[3]", null, descProperties);
        desc = lang.newText(new Offset(0, 10, "desc[3]", "SW"), translator.translateMessage("description[4]"), "desc[4]", null, descProperties);
        desc = lang.newText(new Offset(0, 10, "desc[4]", "SW"), translator.translateMessage("description[5]"), "desc[5]", null, descProperties);
        desc = lang.newText(new Offset(0, 5, "desc[5]", "SW"), translator.translateMessage("description[6]"), "desc[6]", null, descProperties);
        desc = lang.newText(new Offset(0, 5, "desc[6]", "SW"), translator.translateMessage("description[7]"), "desc[7]", null, descProperties);
        // rectangle for the description
        titleRect = lang.newRect(new Offset(-5, -10, "title", "NW"), new Offset(10, 120, "desc[4]", "SE"), "descRect", null, bigTitleRectangleProperties);
        // Sourcecode
        Font fontSourcecode = (Font) sourceCodeProperties.get("font");
        fontSourcecode = fontSourcecode.deriveFont(0, 16);
        sourceCodeProperties.set("font", fontSourcecode);
        sc = lang.newSourceCode(new Offset(0, 50, "graph", "SW"), "sourceCode", null, sourceCodeProperties);
        sc.addCodeLine("public int negamax(Node state, int player) {", null, 0, null);                    //0
        sc.addCodeLine("if (state.isLeaf()) ", null, 1, null);                                            //1
        sc.addCodeLine("return player * state.getValue();", null, 2, null);                               //2
        sc.addCodeLine("value = Integer.MIN_VALUE;", null, 1, null);                                      //3
        sc.addCodeLine("for (Node childState : state.getChildren())", null, 1, null);                     //4
        sc.addCodeLine("value = Math.max(value, -negamax(childState, -player));", null, 2, null);         //5
        sc.addCodeLine("return value;", null, 1, null);                                                   //6
        sc.addCodeLine("}", null, 0, null);                                                               //7
        sc.hide();
        lang.nextStep();
        lang.hideAllPrimitives();
        title.show();
        hLine.show();
        lang.nextStep(translator.translateMessage("sourcecode"));
        sc.show();
        lang.nextStep();
        graph.show();
        lang.nextStep(translator.translateMessage("animationstart"));
        // highlight the whole source code and graph/tree
        for (int i = 0; i < 8; i++)
            sc.highlight(i);
        for (int i = 0; i < graph.getSize(); i++)
            graph.highlightNode(i, null, null);
        actualPlayer = lang.newText(new Offset(20, 25, "graph", "NE"), translator.translateMessage("actualPlayer"), "actualPlayer", null, actualPlayerProperties);
        actualPlayer.show();
        String nowPlaying;
        if (startPlayer == 1)
            nowPlaying = " MAX";
        else
            nowPlaying = " MIN";
        actualPlayer.setText(translator.translateMessage("actualPlayer") + nowPlaying, null, null);
        lang.nextStep();
        // unhighlight whole code (except 0st and last line) and whole graph except root
        for (int i = 1; i < 7; i++)
            sc.unhighlight(i);
        for (int i = 1; i < graph.getSize(); i++) {
            graph.unhighlightNode(i, null, null);
        }
        lang.nextStep();
        Node root = graph.getNode(0);
        nodeBestValues = new Text[graph.getSize()];
        int x;
        for (int i = 0; i < nodeBestValues.length; ++i) {
            String str = "" + i;
            Coordinates pos = graphNodes[i];
            x = pos.getX() + xBestValOffset;
            int y = pos.getY() + yBestValOffset;
            nodeBestValues[i] = lang.newText(new Coordinates(x, y + 30), "", str, null, nodeValueProperties);
        }
        // ask question
        r = rand.nextInt(3);
        lastAskedQuestion = r;
        ask(r);
        // Call the algorithm
        finalValue = negamax(root, startPlayer);
        // Make sure to not ask the same question as in the beginning
        while (lastAskedQuestion == r) {
            r = rand.nextInt(3);
        }
        ask(r);
        lang.nextStep(translator.translateMessage("summary"));
        lang.hideAllPrimitives();
        title.show();
        hLine.show();
        graph.show();
        // Outro
        String spieler = "";
        if (startPlayer == 1)
            spieler = " MAX ";
        else
            spieler = " MIN ";
        Font fontOutro = (Font) outroProperties.get("font");
        fontOutro = fontOutro.deriveFont(0, 16);
        outroProperties.set("font", fontOutro);
        ask(3);
        lang.nextStep();
        // First Outro
        out[0] = lang.newText(new Offset(0, 40, "graph", "SW"), translator.translateMessage("outro[0]") + " " + finalValue, "out[0]", null, outroProperties);
        out[1] = lang.newText(new Offset(0, 80, "graph", "SW"), translator.translateMessage("outro[1]") + spieler + translator.translateMessage("outro[2]") + " " + finalValue + translator.translateMessage("outro[3]"), "out[1]", null, outroProperties);
        // outro rectangle
        RectProperties rectOutProps = new RectProperties();
        rectOutProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        outRect[0] = lang.newRect(new Offset(-10, -10, "out[0]", "NW"), new Offset(80, 10, "out[1]", "SE"), "outRect", null, rectOutProps);
        lang.nextStep();
        // Next Outro
        out[0].setText(translator.translateMessage("outro[4]"), null, null);
        out[1].setText(translator.translateMessage("outro[5]"), null, null);
        out[2] = lang.newText(new Offset(0, 120, "graph", "SW"), translator.translateMessage("outro[6]"), "out[2]", null, outroProperties);
        outRect[0].hide();
        outRect[1] = lang.newRect(new Offset(-5, -10, "out[0]", "NW"), new Offset(250, 50, "out[1]", "SE"), "outRect", null, rectOutProps);
        lang.nextStep();
        // Last Outro
        out[0].setText(translator.translateMessage("outro[7]"), null, null);
        out[1].setText(translator.translateMessage("outro[8]"), null, null);
        out[2].setText(translator.translateMessage("outro[9]"), null, null);
        out[3] = lang.newText(new Offset(0, 160, "graph", "SW"), translator.translateMessage("outro[10]"), "out[3]", null, outroProperties);
        outRect[1].hide();
        outRect[2] = lang.newRect(new Offset(-5, -10, "out[0]", "NW"), new Offset(-150, 100, "out[1]", "SE"), "outRect", null, rectOutProps);
    }

    /************************************************************************************
     ***************************** NegaMax Algorithm ************************************
     ************************************************************************************/
    private int negamax(Node state, int player) {
        actualPlayer.show();
        String nowPlaying;
        if (player == 1)
            nowPlaying = " MAX";
        else
            nowPlaying = " MIN";
        actualPlayer.setText(translator.translateMessage("actualPlayer") + nowPlaying, null, null);
        int childValue = Integer.MIN_VALUE;
        int pos = graph.getPositionForNode(state);
        // Highlight 1st line - Unhighlight 0st line and last
        sc.toggleHighlight(0, 0, false, 1, 0);
        sc.unhighlight(7);
        // Highlight the actual node
        graph.highlightNode(graph.getPositionForNode(state), null, null);
        lastHighlighted = graph.getPositionForNode(state);
        if (isLeaf(state)) {
            lang.nextStep();
            // Highlight 2nd line - Unhighlight 1st line
            sc.unhighlight(1);
            sc.highlight(2);
            lang.nextStep();
            sc.unhighlight(2);
            childValue = player * Integer.parseInt(graph.getNodeLabel(state));
            nodeBestValues[pos].hide();
            nodeBestValues[pos].setText(("" + player * Integer.parseInt(graph.getNodeLabel(state))), null, null);
            nodeBestValues[pos].show();
            return player * Integer.parseInt(graph.getNodeLabel(state));
        } else {
            lang.nextStep();
        }
        sc.unhighlight(1);
        sc.unhighlight(2);
        sc.highlight(3);
        value = Integer.MIN_VALUE;
        lang.nextStep();
        // Highlight 4th, 5th line (for loop) and unhighlight 3rd line
        sc.unhighlight(3);
        sc.highlight(4);
        sc.highlight(5);
        for (Node childState : children(state)) {
            lang.nextStep();
            // unhighlight the actual node
            graph.unhighlightNode(lastHighlighted, null, null);
            sc.unhighlight(4);
            sc.unhighlight(5);
            value = Math.max(value, -negamax(childState, -player));
            if (value > childValue) {
                nodeBestValues[pos].hide();
                nodeBestValues[pos].setText(String.valueOf(value * player), null, null);
                nodeBestValues[pos].show();
            }
        }
        sc.unhighlight(4);
        sc.unhighlight(5);
        sc.highlight(6);
        lang.nextStep();
        sc.unhighlight(6);
        // Make sure to unhighlight all Nodes of the graph
        for (int i = 0; i < graph.getSize(); i++) {
            graph.unhighlightNode(i, null, null);
        }
        return value;
    }

    private void ask(int random) {
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel(Integer.toString(rand.nextInt(150) + lastAskedQuestion * rand.nextInt(100)));
        switch (random) {
            case 0:
                question.setPrompt(translator.translateMessage("question[0]"));
                if (startPlayer == 1) {
                    question.addAnswer("MAX", 1, translator.translateMessage("questionFeedbackT[0.0]"));
                    question.addAnswer("MIN", 0, translator.translateMessage("questionFeedback[0.0]"));
                } else {
                    question.addAnswer("MAX", 0, translator.translateMessage("questionFeedback[0.1]"));
                    question.addAnswer("MIN", 1, translator.translateMessage("questionFeedbackT[0.1]"));
                }
                break;
            case 1:
                question.setPrompt(translator.translateMessage("question[1]"));
                question.addAnswer("O(1)", 0, translator.translateMessage("questionFeedback[1.1]"));
                question.addAnswer("O(n)", 0, translator.translateMessage("questionFeedback[1.1]"));
                question.addAnswer("O(log(n))", 0, translator.translateMessage("questionFeedback[1.1]"));
                question.addAnswer("O(nlog(n))", 0, translator.translateMessage("questionFeedback[1.1]"));
                question.addAnswer("O(|V|+|E|+C)", 1, translator.translateMessage("questionFeedbackT[1.0]"));
                question.addAnswer("O(n^2)", 0, translator.translateMessage("questionFeedback[1.1]"));
                break;
            case 2:
                question.setPrompt(translator.translateMessage("question[2]"));
                question.addAnswer("max(a,b) = -min(-a,-b)", 1, translator.translateMessage("questionFeedbackT[2.0]"));
                question.addAnswer("max(a,b) = min(-a,-b)", 0, translator.translateMessage("questionFeedback[2.1]"));
                question.addAnswer("max(a,b) = -min(a,b)", 0, translator.translateMessage("questionFeedback[2.1]"));
                question.addAnswer("max(a,b) = -min(-a,b)", 0, translator.translateMessage("questionFeedback[2.1]"));
                question.addAnswer("max(a,b) = -min(a,-b)", 0, translator.translateMessage("questionFeedback[2.1]"));
                question.addAnswer("max(a,b) = min(a,b)", 0, translator.translateMessage("questionFeedback[2.1]"));
                break;
            case 3:
                question.setPrompt(translator.translateMessage("question[3]"));
                question.addAnswer(Integer.toString(finalValue), 1, translator.translateMessage("questionFeedbackT[3.0]"));
                for (int i = 0; i < graph.getSize(); i++) {
                    if (isLeaf(graph.getNode(i)) && Integer.parseInt(graph.getNodeLabel(i)) != finalValue)
                        question.addAnswer(graph.getNodeLabel(i), 0, translator.translateMessage("questionFeedback[3.1]"));
                }
                break;
        }
        lang.addMCQuestion(question);
    }

    @SuppressWarnings("rawtypes")
    /************************************************************************************
     **************************** Operations for Trees **********************************
     ************************************************************************************/
    private Graph getGraph(GraphProperties graphProps, String treeStructure) {
        TreeParserWithNodeNames tp = new TreeParserWithNodeNames();
        generators.searching.helpers.Node node = tp.parseText(treeStructure);
        if (!tp.isValid()) {
            return getDefaultGraph(graphProps);
        } else {
            List nodeList = getFlattened(node);
            int nodeListSize = nodeList.size();
            int[][] newGraph = new int[nodeListSize][nodeListSize];
            int j;
            for (int i = 0; i < newGraph.length; ++i) {
                for (j = 0; j < newGraph[0].length; ++j) {
                    if (connected((generators.searching.helpers.Node) nodeList.get(i), (generators.searching.helpers.Node) nodeList.get(j))) {
                        newGraph[i][j] = 1;
                    } else {
                        newGraph[i][j] = 0;
                    }
                }
            }
            graphNodes = new Coordinates[nodeListSize];
            String[] nodeValues = new String[nodeListSize];
            for (j = 0; j < nodeListSize; ++j) {
                generators.searching.helpers.Node newNode = (generators.searching.helpers.Node) nodeList.get(j);
                if (newNode.isLeaf()) {
                    nodeValues[j] = newNode.getValue().toString();
                } else {
                    nodeValues[j] = newNode.getId();
                }
            }
            setupTree(getIndexedTree(node, 0));
            Graph generatedGraph = lang.newGraph("graph", newGraph, graphNodes, nodeValues, null, graphProps);
            generatedGraph.hide();
            return generatedGraph;
        }
    }

    private Graph getDefaultGraph(GraphProperties var1) {
        int[][] graphMatrix = new int[13][13];
        for (int i = 0; i < graphMatrix.length; ++i) {
            for (int j = 0; j < graphMatrix[0].length; ++j) {
                graphMatrix[i][j] = 0;
            }
        }
        graphMatrix[0][1] = 1;
        graphMatrix[1][0] = 1;
        graphMatrix[0][2] = 1;
        graphMatrix[2][0] = 1;
        graphMatrix[0][3] = 1;
        graphMatrix[3][0] = 1;
        graphMatrix[1][4] = 1;
        graphMatrix[4][1] = 1;
        graphMatrix[1][5] = 1;
        graphMatrix[5][1] = 1;
        graphMatrix[1][6] = 1;
        graphMatrix[6][1] = 1;
        graphMatrix[2][7] = 1;
        graphMatrix[7][2] = 1;
        graphMatrix[2][8] = 1;
        graphMatrix[8][2] = 1;
        graphMatrix[2][9] = 1;
        graphMatrix[9][2] = 1;
        graphMatrix[3][10] = 1;
        graphMatrix[10][3] = 1;
        graphMatrix[3][11] = 1;
        graphMatrix[11][3] = 1;
        graphMatrix[3][12] = 1;
        graphMatrix[12][3] = 1;
        graphNodes = new Coordinates[13];
        graphNodes[0] = new Coordinates(275, 125);
        graphNodes[1] = new Coordinates(125, 225);
        graphNodes[2] = new Coordinates(275, 225);
        graphNodes[3] = new Coordinates(425, 225);
        graphNodes[4] = new Coordinates(75, 325);
        graphNodes[5] = new Coordinates(125, 325);
        graphNodes[6] = new Coordinates(175, 325);
        graphNodes[7] = new Coordinates(225, 325);
        graphNodes[8] = new Coordinates(275, 325);
        graphNodes[9] = new Coordinates(325, 325);
        graphNodes[10] = new Coordinates(375, 325);
        graphNodes[11] = new Coordinates(425, 325);
        graphNodes[12] = new Coordinates(475, 325);
        String[] graphStructure = new String[]{"A", "B", "C", "D", "1", "13", "4", "3", "6", "8", "9", "5", "14"};
        Graph defaultGraph = lang.newGraph("graph", graphMatrix, graphNodes, graphStructure, null, var1);
        defaultGraph.hide();
        return defaultGraph;
    }

    @SuppressWarnings("rawtypes")
    private void setupTree(generators.searching.helpers.Node node) {
        int y = upperBorder + node.getDepth() * yNodeDist;
        int depth = node.getDepth();
        if (depth > treeDepth) {
            treeDepth = depth;
        }
        if (node.isLeaf()) {
            setupNode(leftBorder, y, node);
            leftBorder += xLeafDist;
        } else {
            List children = node.getChildren();
            Iterator iterator = children.iterator();
            while (iterator.hasNext()) {
                generators.searching.helpers.Node childNode = (generators.searching.helpers.Node) iterator.next();
                setupTree(childNode);
            }
            int x = 0;
            generators.searching.helpers.Node nextNode;
            for (Iterator it = node.getChildren().iterator(); it.hasNext(); x += graphNodes[nextNode.getValue()].getX()) {
                nextNode = (generators.searching.helpers.Node) it.next();
            }
            x /= children.size();
            setupNode(x, y, node);
        }
    }

    /************************************************************************************
     **************************** Operations for Nodes **********************************
     ************************************************************************************/
    private boolean isLeaf(Node node) {
        int[] edges = graph.getEdgesForNode(node);
        int position = graph.getPositionForNode(node);
        for (int i = position; i < edges.length; ++i) {
            if (edges[i] > 0) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Node[] children(Node node) {
        LinkedList children = new LinkedList();
        int[] edges = graph.getEdgesForNode(node);
        int position = graph.getPositionForNode(node);
        for (int i = position; i < edges.length; ++i) {
            if (edges[i] > 0) {
                children.add(graph.getNode(i));
            }
        }
        return (Node[]) children.toArray(new Node[children.size()]);
    }

    @SuppressWarnings("rawtypes")
    private boolean connected(generators.searching.helpers.Node node1, generators.searching.helpers.Node node2) {
        List children1 = node1.getChildren();
        List children2 = node2.getChildren();
        return children1.contains(node2) || children2.contains(node1);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<generators.searching.helpers.Node> getFlattened(generators.searching.helpers.Node node) {
        LinkedList nodeList = new LinkedList();
        if (node == null) {
            return nodeList;
        } else {
            nodeList.add(node);
            List var3 = node.getChildren();

            for (int var4 = 0; var4 < var3.size(); ++var4) {
                nodeList.addAll(getFlattened((generators.searching.helpers.Node) var3.get(var4)));
            }
            return nodeList;
        }
    }

    private int getNodeCount(generators.searching.helpers.Node node) {
        return getFlattened(node).size();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private generators.searching.helpers.Node getIndexedTree(generators.searching.helpers.Node node, int value) {
        int var1 = value;
        node.setValue(value);
        ArrayList var2 = new ArrayList();
        int var3 = 0;
        List children = node.getChildren();
        for (int i = 0; i < children.size(); ++i) {
            var2.add(getIndexedTree((generators.searching.helpers.Node) children.get(i), var3 + var1 + 1));
            var3 += getNodeCount((generators.searching.helpers.Node) children.get(i));
        }
        node.setChildren(var2);
        return node;
    }

    private void setupNode(int x, int y, generators.searching.helpers.Node node) {
        int value = node.getValue();
        graphNodes[value] = new Coordinates(x, y);
    }

    /************************************************************************************
     ******************************** Error Window **************************************
     ************************************************************************************/
    private void showErrorWindow(String error) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), error, "Error", 0);
    }
}