/*
 * RB_Raeume_INSERT.java
 * Maciej Aleksander Mokwinski + Julian Harbarth, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

import java.util.Hashtable;

import translator.Translator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class RB_TREES_INSERT implements Generator {

    private static Language                lang;
    public static int                      amoundOfInsertedVertices = 0;
    /**
     * array with the values of the vertices that should be inserted
     */
    private static int[]                   vertexValues;
    private static int                     graphCounter             = 0;

    private int                            questionProbability;

    Random                                 randomGenerator          = new Random();

    private static int                     border_px                = 20;
    private Text[]                         valuesText;
    private Circle[]                       valuesCircles;
    public static Graph                    graph;
    public static RB_TREES_INSERT_SRC      t;
    public static ArrayList<Vertex_INSERT> vertices                 = new ArrayList<>();
    public static int                      updateCounter            = 0;
    private AnimationPropertiesContainer   props;

    private static Color                   GRAPH_RED_COLOR          = Color.RED;
    private static int                     black_value              = 84;
    private static Color                   GRAPH_BLACK_COLOR        = new Color(black_value,
                                                                            black_value,
                                                                            black_value);
    private Color                          notActiveColor;

    private Rect                           hRect;
    private Text                           header;

    // -- SourceCode -- //
    private SourceCode                     srcINSERT_FIX;
    private SourceCode                     srcRightRotate;
    private SourceCode                     srcLeftRotate;
    private SourceCode                     srcINSERT;

    private SourceCodeProperties           sourceCodePropsINSERT_FIX;
    private SourceCodeProperties           sourceCodePropsRightRotate;
    private SourceCodeProperties           sourceCodePropsLeftRotate;
    private SourceCodeProperties           sourceCodePropsINSERT;
    // -- SourceCode -- //

    private static TextProperties          infoTextProperties       = new TextProperties();

    private Variables                      vars;

    /**
     * x-Axis offset for the Node, we want to paint it vary, depending on the
     * depth of the vertex depth 0 = 150 depth 1 = 75 ...
     */
    private static int[]                   offsetsGraph             = { 150, 75, 50, 25, 15 };

    // public static Graph[] graphenArray;

    private Locale                         alocale;
    private Translator                     translator;

    public RB_TREES_INSERT(String resourceName, Locale locale) {

        this.alocale = locale;
        translator = new Translator(resourceName, alocale);
        init();
    }

    @Override
    public void init() {
        lang = new AnimalScript(translator.translateMessage("algorithmName"),
                "Maciej Aleksander Mokwinski + Julian Harbarth", 600, 400);
        
        lang.setStepMode(true);

        lang.setInteractionType(
                Language.INTERACTION_TYPE_AVINTERACTION);
    }

    /**
     * method shows the info about the Visualisation and info about the vertex
     * that should be inserted
     * 
     * @param verticesValues
     */
    private void start(int[] verticesValues) {
        // Title: text
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        header = lang.newText(new Coordinates(border_px, border_px * 2),
                translator.translateMessage("algorithmName"),
                "header", null, headerProps);

        // Title: rectangle
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        hRect = lang.newRect(new Offset(-5, -5, "header",
                AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, rectProps);

        // InfoText properties

        infoTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 14));

        // Introduction:
        TextProperties textProps = new TextProperties();
        TextProperties textPropsHeader = new TextProperties();
        Text[] intro = new Text[30];
        
       
        textPropsHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 14));

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));

        intro[0] = lang.newText(new Coordinates(border_px, 100),
                translator.translateMessage("intro01"),
                "intro01", null, textPropsHeader);

        intro[1] = lang.newText(new Offset(0, 25, "intro01", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro02"),
                "intro02", null, textProps);

        intro[2] = lang.newText(new Offset(0, 25, "intro02", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro03"),
                "intro03", null, textProps);

        intro[3] = lang.newText(new Offset(0, 25, "intro03", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro04"),
                "intro04", null, textProps);

        lang.nextStep(translator.translateMessage("tableOfContentsStart")); // STEP_1

        intro[4] = lang.newText(new Offset(0, 50, "intro04", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro11"),
                "intro11", null, textPropsHeader);

        intro[5] = lang.newText(new Offset(0, 25, "intro11", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro12"),
                "intro12", null, textProps);

        intro[6] = lang.newText(new Offset(0, 25, "intro12", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro13"),
                "intro13", null, textProps);

        intro[7] = lang.newText(new Offset(0, 25, "intro13", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro14"),
                "intro14", null, textProps);

        intro[8] = lang.newText(new Offset(0, 25, "intro14", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro15"),
                "intro15", null, textProps);

        lang.nextStep();

        intro[9] = lang.newText(new Offset(0, 50, "intro15", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro21"),
                "intro21", null, textPropsHeader);

        intro[10] = lang.newText(new Offset(0, 25, "intro21", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro22"),
                "intro22", null, textProps);

        intro[11] = lang.newText(new Offset(0, 25, "intro22", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro23"),
                "intro23", null, textProps);

        intro[12] = lang.newText(new Offset(0, 25, "intro23", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro24"),
                "intro24", null, textProps);

        intro[13] = lang.newText(new Offset(0, 25, "intro24", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro25"),
                "intro25", null, textProps);
        
        intro[28] = lang.newText(new Offset(0, 25, "intro25", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro26"),
                "intro26", null, textProps);

        lang.nextStep();

        intro[14] = lang.newText(new Offset(0, 50, "intro26", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro31"),
                "intro31", null, textPropsHeader);

        intro[15] = lang.newText(new Offset(0, 25, "intro31", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro32"),
                "intro32", null, textProps);

        intro[16] = lang.newText(new Offset(0, 25, "intro32", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro33"),
                "intro33", null, textProps);

        intro[17] = lang.newText(new Offset(0, 25, "intro33", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro34"),
                "intro34", null, textProps);

        intro[18] = lang.newText(new Offset(0, 25, "intro34", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro35"),
                "intro35", null, textProps);

        intro[19] = lang.newText(new Offset(0, 25, "intro35", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro36"),
                "intro36", null, textProps);

        intro[20] = lang.newText(new Offset(0, 25, "intro36", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro37"),
                "intro37", null, textProps);

        intro[21] = lang.newText(new Offset(0, 25, "intro37", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro38"),
                "intro38", null, textProps);

        lang.nextStep();
        
        intro[22] = lang.newText(new Offset(0, 50, "intro38", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro41"),
                "intro41", null, textPropsHeader);
                
        intro[23] = lang.newText(new Offset(0, 25, "intro41", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro42"),
                "intro42", null, textProps);
        
        intro[24] = lang.newText(new Offset(0, 25, "intro42", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro43"),
               "intro43", null, textProps);
        
        intro[25] = lang.newText(new Offset(0, 25, "intro43", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro44"),
                "intro44", null, textProps);
        
        intro[26] = lang.newText(new Offset(0, 25, "intro44", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro45"),
                "intro45", null, textProps);
                
        intro[27] = lang.newText(new Offset(0, 25, "intro45", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro46"),
                "intro46", null, textProps);
        
        lang.nextStep();
        lang.hideAllPrimitives();
        header.show();
        hRect.show();

        // Draw not inserted vertices
        valuesText = new Text[verticesValues.length];
        valuesCircles = new Circle[verticesValues.length];
        drawNotInsertedVertices();

        // Draw NULL node
        drawNullNode();

        // graphenArray = new Graph[4];

        drawRules();

    }

    private void drawNotInsertedVertices() {

        CircleProperties circleProps = new CircleProperties();
        circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        TextProperties vertexValueProps = new TextProperties();
        vertexValueProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 14));

        int value_x;

        int size = 20;

        String off = "hRect";

        for (int i = 0; i < vertexValues.length; i++) {

            valuesCircles[i] = lang.newCircle(
                    new Offset(30, size, off, "NE"),
                    size,
                    "vertexCircle" + i,
                    null,
                    circleProps);

            String value = String.valueOf(vertexValues[i]);
            int length = value.length();

            value_x = size - (length * 4);

            valuesText[i] = lang.newText(new Offset(value_x, size - 10, "vertexCircle" + i, "NW"),
                    value, value, null, vertexValueProps);

            off = "vertexCircle" + i;
        }
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

        this.props = props;

        // Resetting the list from possible former executions
        vertices = new ArrayList<Vertex_INSERT>();

        vars = lang.newVariables();

        vertexValues = (int[]) primitives.get("nodeValues");
        System.out.println("NodeValues: " + Arrays.toString(vertexValues));
        Vertex_INSERT v;

        questionProbability = (int) primitives.get("questionProbability");

        // show infos
        start(vertexValues);

        SourceCodeProperties notActiveColorProps = (SourceCodeProperties) props
                .getPropertiesByName("notActiveColor");

        this.notActiveColor = (Color) notActiveColorProps.get("color");

        // draw Pseudo-Code
        drawPseudoCode();

        t = new RB_TREES_INSERT_SRC(lang, translator, border_px, infoTextProperties,
                srcINSERT, srcINSERT_FIX, srcLeftRotate, srcRightRotate, notActiveColor, vars,
                questionProbability);

        int random = randomGenerator.nextInt(100);

        System.out.println("prob: " + questionProbability);
        System.out.println("rnd: " + random);

        if (random <= questionProbability) {
            MultipleSelectionQuestionModel msqm = new MultipleSelectionQuestionModel("color");

            msqm.setPrompt(translator.translateMessage("questionWhichColor"));

            msqm.addAnswer(translator.translateMessage("questionAnswer1"), 1,
                    translator.translateMessage("questionCorrectAnswerColor") + "\n\n");
            msqm.addAnswer(translator.translateMessage("questionAnswer2"), 1,
                    translator.translateMessage("questionCorrectAnswerColor") + "\n\n");

            msqm.addAnswer(translator.translateMessage("questionAnswer3"),
                    0,
                    translator.translateMessage("questionWrongAnswer",
                            translator.translateMessage("questionAnswer1"))
                            + " "
                            + translator.translateMessage("questionand") + " "
                            + translator.translateMessage("questionAnswer2") + "!\n\n");
            msqm.addAnswer(
                    translator.translateMessage("questionAnswer4"),
                    0,
                    translator.translateMessage("questionWrongAnswer",
                            translator.translateMessage("questionAnswer1"))
                            + " "
                            + translator.translateMessage("questionand") + " "
                            + translator.translateMessage("questionAnswer2") + "!\n\n");
            msqm.addAnswer(
                    translator.translateMessage("questionAnswer5"),
                    0,
                    translator.translateMessage("questionWrongAnswer",
                            translator.translateMessage("questionAnswer1"))
                            + " "
                            + translator.translateMessage("questionand") + " "
                            + translator.translateMessage("questionAnswer2") + "!\n\n");

            lang.addMSQuestion(msqm);
        }

        // insert the given values into the tree t
        for (int i = 0; i < vertexValues.length; i++) {

            // create Vertex
            v = new Vertex_INSERT(vertexValues[i]);

            // add vertex to the list of the vertices
            // this has to be done before the insertion
            // so that invocation of updateGraph()
            // can consider the new vertex
            vertices.add(v);
            // set it's index

            lang.nextStep(translator.translateMessage("tableOfContentsInsert",
                    Integer.toString(i + 1)));

            v.setVertexIndex(vertices.size() - 1);
            // insert the vertex
            t.insert(v);

            // Color the inserted vertex green
            valuesCircles[i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
                    Color.GREEN, null, null);

            // lang.nextStep();

        }
        // go to the summarize

        random = randomGenerator.nextInt(100);

        if (random <= questionProbability) {

            MultipleChoiceQuestionModel msqm = new MultipleChoiceQuestionModel("runtime");

            msqm.setPrompt(translator.translateMessage("questionRuntime"));

            msqm.addAnswer("O(n²)", 0, translator.translateMessage("questionWrongAnswerRuntime"));
            msqm.addAnswer("O(n)", 0, translator.translateMessage("questionWrongAnswerRuntime"));
            msqm.addAnswer("O(n log n)", 0,
                    translator.translateMessage("questionWrongAnswerRuntime"));
            msqm.addAnswer("O(log n)", 1,
                    translator.translateMessage("questionCorrectAnswerRuntime"));

            lang.addMCQuestion(msqm);
        }

        lang.nextStep(translator.translateMessage("tableOfContentsFinished"));

        t.hide_pointers();
        lang.hideAllPrimitives();
        header.show();
        hRect.show();

        // SUMMARISE:
        // Title "INSERT for Red Black Trees"

        TextProperties textProps = new TextProperties();
        Text[] outro = new Text[20];

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));

        outro[0] = lang.newText(new Coordinates(border_px, 100),
                translator.translateMessage("outro0"),
                "outro0", null, textProps);

        outro[1] = lang.newText(new Offset(0, 50, "outro0", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro1"),
                "outro1", null, textProps);

        outro[14] = lang.newText(new Offset(0, 50, "outro1", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro105"),
                "outro105", null, textProps);

        outro[2] = lang.newText(new Offset(0, 25, "outro105", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro2"),
                "outro2", null, textProps);

        outro[3] = lang.newText(new Offset(0, 25, "outro2", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro3"),
                "outro3", null, textProps);

        outro[4] = lang.newText(new Offset(0, 50, "outro3", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro4"),
                "outro4", null, textProps);

        outro[5] = lang.newText(new Offset(0, 25, "outro4", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro5"),
                "outro5", null, textProps);

        outro[6] = lang.newText(new Offset(0, 25, "outro5", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro6"),
                "outro6", null, textProps);

        outro[7] = lang.newText(new Offset(0, 25, "outro6", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro7"),
                "outro7", null, textProps);

        outro[7] = lang.newText(new Offset(0, 25, "outro7", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro8"),
                "outro8", null, textProps);

        outro[8] = lang.newText(new Offset(0, 25, "outro8", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro9"),
                "outro9", null, textProps);

        outro[9] = lang.newText(new Offset(0, 50, "outro9", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro10"),
                "outro10", null, textProps);

        outro[10] = lang.newText(new Offset(0, 25, "outro10", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro11"),
                "outro11", null, textProps);

        outro[11] = lang.newText(new Offset(0, 25, "outro11", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro12"),
                "outro12", null, textProps);

        outro[12] = lang.newText(new Offset(0, 25, "outro12", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro13"),
                "outro13", null, textProps);

        outro[13] = lang.newText(new Offset(0, 50, "outro13", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro14"),
                "outro14", null, textProps);

        lang.nextStep(translator.translateMessage("tableOfContentsFinal"));

        lang.finalizeGeneration();
        return lang.toString();
    }

    private void drawRules() {
        int x, y;
        String name;
        SourceCodeProperties codeProperties = new SourceCodeProperties();

        codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));

        // Coordinates:
        x = border_px;
        y = 620;

        Coordinates ruleCoordinates = new Coordinates(x, y);
        // prepare name
        name = "rules";

        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        String ruleString =
                translator.translateMessage("propertyHead")
                        + "\n \n"
                        + translator.translateMessage("property1")
                        + "\n"
                        + translator.translateMessage("property2")
                        + "\n"
                        + translator.translateMessage("property3")
                        + "\n"
                        + translator.translateMessage("property4")
                        + "\n"
                        + translator.translateMessage("property5")
                        + "\n"
                        + translator.translateMessage("property51");

        // draw x:

        SourceCode sc = lang.newSourceCode(ruleCoordinates, name, null, codeProperties);
        sc.addMultilineCode(ruleString, "_0", null);
        sc.show();

        Rect rRect = lang.newRect(new Offset(-5, -5, "rules", "NW"),
                new Offset(5, 5, "rules", "SE"),
                "rRect",
                null, rectProps);
        rRect.show();
    }

    /**
     * updates (or draws if it's null) the graph this method bases on the global
     * defined ArrayList vertices which contains all the inserted vertices
     */
    public static void updateGraph() {

        if (graph != null) {
            graph.hide();
        }

        RB_TREES_INSERT.amoundOfInsertedVertices = RB_TREES_INSERT.vertices.size();

        if (RB_TREES_INSERT.amoundOfInsertedVertices > 0) {

            // Adjacency list update
            int[][] adj = updateAdjacencyMatrix();

            // Koordinates update
            Node[] n = updateCoordinates();

            // prepare labels
            String[] labels = updateLabels();

            // create default Graph
            // draw the Graph
            makeGraph(adj, n, labels);
            // graphen.add(blah.graph);
            // blah.graph.show();

            // Highlight black vertices
            highlightBlackVertices();

        }
    }

    private static void highlightBlackVertices() {
        Node vertex;

        if (RB_TREES_INSERT.graph == null)
            return;

        for (Vertex_INSERT n : RB_TREES_INSERT.vertices) {

            if (n.getColor() == 'b') {

                vertex = RB_TREES_INSERT.graph.getNode(n.getIndexVertex());

                if (vertex != null) {
                    RB_TREES_INSERT.graph.highlightNode(vertex, null, null);
                }

            } else {

                vertex = RB_TREES_INSERT.graph.getNode(n.getIndexVertex());

                RB_TREES_INSERT.graph.unhighlightNode(vertex, null, null);
            }
        }
    }

    private static String[] updateLabels() {
        String[] labels = new String[RB_TREES_INSERT.amoundOfInsertedVertices];
        for (int i = 0; i < RB_TREES_INSERT.amoundOfInsertedVertices; i++) {
            labels[i] = RB_TREES_INSERT.vertices.get(i).getVertexLabel();
        }

        return labels;
    }

    private static Node[] updateCoordinates() {
        int x = 385, y = 100;

        x = (int) (Math.pow(2, (int) (Math.log(vertexValues.length) / Math.log(2))) * (offsetsGraph[(int) (Math
                .log(vertexValues.length) / Math.log(2))] + border_px));

        x = x + 25;

        int depth = 0;
        Vertex_INSERT n;
        Vertex_INSERT parent;
        int stage = 0;
        Node[] coordinates = new Node[RB_TREES_INSERT.amoundOfInsertedVertices];
        RB_TREES_INSERT.t.root.setVertexCoordinates(x, y);

        for (int k = 0; k < 7; k++) {

            for (int i = 0; i < RB_TREES_INSERT.amoundOfInsertedVertices; i++) {
                depth = 0;
                n = RB_TREES_INSERT.vertices.get(i);
                if (RB_TREES_INSERT.t.root == n) {
                    coordinates[i] = n.getVertexCoordinates();
                    continue;
                }
                // figure out the depth of the vertex n
                depth = t.getDepth(t.root, n.getValue(), depth);

                if (depth == 1 + stage) {
                    parent = n.getParent();

                    y = parent.getY() + 50;
                    // Graph = {150, 75, 50};
                    if (n == parent.getLeftChild()) { // n is left child
                        x = parent.getX() - RB_TREES_INSERT.offsetsGraph[depth - 1];
                        n.setVertexCoordinates(x, y);
                    } else { // else n is the right child
                        x = parent.getX() + RB_TREES_INSERT.offsetsGraph[depth - 1];
                        n.setVertexCoordinates(x, y);

                    }

                    coordinates[i] = n.getVertexCoordinates();
                } else
                    continue;
            }
            stage++;
        }

        return coordinates;
    }

    private void drawPseudoCode() {

        boolean hidden = false;

        SourceCodeProperties sourceCode = (SourceCodeProperties) props
                .getPropertiesByName("sourceCode");

        hidden = (boolean) sourceCode.get("hidden");

        Color color = (Color) sourceCode.get("color");
        Color contextColor = (Color) sourceCode.get("contextColor");
        Color highlightColor = (Color) sourceCode.get("highlightColor");

        String name = ((Font) sourceCode.get("font")).getFamily();
        int style = Font.PLAIN;
        int size = 12;

        if ((boolean) sourceCode.get("bold")) {
            style = style ^ Font.BOLD;
        }

        if ((boolean) sourceCode.get("italic")) {
            style = style ^ Font.ITALIC;
        }

        size = (int) sourceCode.get("size");

        Font font = new Font(name, style, size);

        // INSERT

        sourceCodePropsINSERT = new SourceCodeProperties();

        sourceCodePropsINSERT.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        sourceCodePropsINSERT.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        sourceCodePropsINSERT.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
        sourceCodePropsINSERT.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor);

        srcINSERT = lang.newSourceCode(new Offset(-120, -220, "rRect", "NE"), "srcINSERT",
                null, sourceCodePropsINSERT);

        srcINSERT.addCodeLine("RB-INSERT(T, z)",
                "srcINSERT_0", 10, null);
        srcINSERT.addCodeLine("    y = T.null",
                "srcINSERT_1", 10, null);
        srcINSERT.addCodeLine("    x = T.root",
                "srcINSERT_2", 10, null);
        srcINSERT.addCodeLine("    while(x != T.null)", // 3
                "srcINSERT_3", 10, null);
        srcINSERT.addCodeLine("        y = x", // 4
                "srcINSERT_4", 10, null);
        srcINSERT.addCodeLine("        if(z.key < x.key)", // 5
                "srcINSERT_5", 10, null);
        srcINSERT.addCodeLine("            x = x.left", // 6
                "srcINSERT_6", 10, null);
        srcINSERT.addCodeLine("        else", // 7
                "srcINSERT_7", 10, null);
        srcINSERT.addCodeLine("            x = x.right", // 8
                "srcINSERT_8", 10, null);
        srcINSERT.addCodeLine("    z.p = y", // 9
                "srcINSERT_9", 10, null);
        srcINSERT.addCodeLine("    if(y == T.null)", // 10
                "srcINSERT_10", 10, null);
        srcINSERT.addCodeLine("        T.root = z", // 11
                "srcINSERT_11", 10, null);
        srcINSERT.addCodeLine("    else", // 12
                "srcINSERT_12", 10, null);
        srcINSERT.addCodeLine("        if(z.key < y.key)", // 13
                "srcINSERT_13", 10, null);
        srcINSERT.addCodeLine("            y.left = z", // 14
                "srcINSERT_14", 10, null);
        srcINSERT.addCodeLine("        else", // 15
                "srcINSERT_15", 10, null);
        srcINSERT.addCodeLine("            y.right = z", // 16
                "srcINSERT_16", 10, null);
        // srcINSERT.addCodeLine("    z.left = T.null", // 17
        // "srcINSERT_17", 10, null);
        // srcINSERT.addCodeLine("    z.right = T.null", // 18
        // "srcINSERT_18", 10, null);
        // srcINSERT.addCodeLine("    z.color = RED", // 19
        // "srcINSERT_19", 10, null);
        srcINSERT.addCodeLine("    RB-INSERT-FIXUP(T, z)", // 20
                "srcINSERT_20", 10, null);

        srcINSERT.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);

        // INSERT FIX
        // SourceCode Props init:
        sourceCodePropsINSERT_FIX = new SourceCodeProperties();

        sourceCodePropsINSERT_FIX.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        sourceCodePropsINSERT_FIX.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        sourceCodePropsINSERT_FIX.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
        sourceCodePropsINSERT_FIX.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
                highlightColor);

        // SourceCode itself init
        srcINSERT_FIX = lang.newSourceCode(new Offset(-120, -320, "srcINSERT", "NE"),
                "srcINSERT_FIX",
                null, sourceCodePropsINSERT_FIX);

        srcINSERT_FIX.addCodeLine("RB-INSERT-FIXUP(T, z)", // 0
                "SRC_INSERT_FIX_0", 10, null);
        srcINSERT_FIX.addCodeLine("while(z.p.color == RED)", // 1
                "SRC_INSERT_FIX_1", 10, null);
        srcINSERT_FIX.addCodeLine("    if(z.p == z.p.p.left)", // 2
                "SRC_INSERT_FIX_2", 10, null);
        srcINSERT_FIX.addCodeLine("        y = z.p.p.right", // 3
                "SRC_INSERT_FIX_3", 10, null);
        srcINSERT_FIX.addCodeLine("        if(y.color == RED)", // 4
                "SRC_INSERT_FIX_4", 10, null);
        srcINSERT_FIX.addCodeLine("            z.p.color = BLACK", // 5
                "SRC_INSERT_FIX_5", 10, null);
        srcINSERT_FIX.addCodeLine("            y.color = BLACK", // 6
                "SRC_INSERT_FIX_6", 10, null);
        srcINSERT_FIX.addCodeLine("            z.p.p.color = RED", // 7
                "SRC_INSERT_FIX_7", 10, null);
        srcINSERT_FIX.addCodeLine("            z = z.p.p", // 8
                "SRC_INSERT_FIX_8", 10, null);
        srcINSERT_FIX.addCodeLine("        else", // 9
                "SRC_INSERT_FIX_9", 10, null);
        srcINSERT_FIX.addCodeLine("            if(z == z.p.right)", // 10
                "SRC_INSERT_FIX_10", 10, null);
        srcINSERT_FIX.addCodeLine("                z = z.p", // 11
                "SRC_INSERT_FIX_11", 10, null);
        srcINSERT_FIX.addCodeLine("                LEFT-ROTATE(T, z)", // 12
                "SRC_INSERT_FIX_12", 10, null);
        srcINSERT_FIX.addCodeLine("            z.p.color = BLACK", // 13
                "SRC_INSERT_FIX_13", 10, null);
        srcINSERT_FIX.addCodeLine("            z.p.p.color = RED", // 14
                "SRC_INSERT_FIX_14", 10, null);
        srcINSERT_FIX.addCodeLine("            RIGHT-ROTATE(T, z.p.p)", // 15
                "SRC_INSERT_FIX_15", 10, null);
        srcINSERT_FIX.addCodeLine("    else // z.p == z.p.p.right", // 16
                "SRC_INSERT_FIX_16", 10, null);
        srcINSERT_FIX.addCodeLine("        y = z.p.p.left", // 17
                "SRC_INSERT_FIX_17", 10, null);
        srcINSERT_FIX.addCodeLine("        if(y.color == RED)", // 18
                "SRC_INSERT_FIX_18", 10, null);
        srcINSERT_FIX.addCodeLine("            z.p.color = BLACK", // 19
                "SRC_INSERT_FIX_19", 10, null);
        srcINSERT_FIX.addCodeLine("            y.color = BLACK", // 20
                "SRC_INSERT_FIX_20", 10, null);
        srcINSERT_FIX.addCodeLine("            z.p.p.color = RED", // 21
                "SRC_INSERT_FIX_21", 10, null);
        srcINSERT_FIX.addCodeLine("            z = z.p.p", // 22
                "SRC_INSERT_FIX_22", 10, null);
        srcINSERT_FIX.addCodeLine("        else", // 23
                "SRC_INSERT_FIX_23", 10, null);
        srcINSERT_FIX.addCodeLine("            if(z == z.p.left)", // 24
                "SRC_INSERT_FIX_24", 10, null);
        srcINSERT_FIX.addCodeLine("                z = z.p", // 25
                "SRC_INSERT_FIX_25", 10, null);
        srcINSERT_FIX.addCodeLine("                RIGHT-ROTATE(T, z)", // 26
                "SRC_INSERT_FIX_26", 10, null);
        srcINSERT_FIX.addCodeLine("            z.p.color = BLACK", // 27
                "SRC_INSERT_FIX_27", 10, null);
        srcINSERT_FIX.addCodeLine("            z.p.p.color = RED", // 28
                "SRC_INSERT_FIX_28", 10, null);
        srcINSERT_FIX.addCodeLine("            LEFT-ROTATE(T, z.p.p)", // 29
                "SRC_INSERT_FIX_29", 10, null);
        srcINSERT_FIX.addCodeLine("T.root.color = BLACK", // 30
                "SRC_INSERT_FIX_30", 10, null);

        srcINSERT_FIX.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

        // RIGHT ROTATE:
        // SourceCode Props init:
        sourceCodePropsRightRotate = new SourceCodeProperties();

        sourceCodePropsRightRotate.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        sourceCodePropsRightRotate.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        sourceCodePropsRightRotate.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
        sourceCodePropsRightRotate.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
                highlightColor);

        // SourceCode itself init
        srcRightRotate = lang.newSourceCode(new Offset(-120, 0, "srcINSERT_FIX", "NE"),
                "srcRightRotate",
                null, sourceCodePropsRightRotate);

        srcRightRotate.addCodeLine("RIGHT-ROTATE(T, x)", // 0
                "srcRightRotate_0", 10, null);
        srcRightRotate.addCodeLine("    y = x.left", // 1
                "srcRightRotate_1", 10, null);
        srcRightRotate.addCodeLine("    x.left = y.right", // 2
                "srcRightRotate_2", 10, null);
        srcRightRotate.addCodeLine("    if (y.right != T.null)", // 3
                "srcRightRotate_3", 10, null);
        srcRightRotate.addCodeLine("        y.right.p = x", // 4
                "srcRightRotate_4", 10, null);
        srcRightRotate.addCodeLine("    y.p = x.p", // 5
                "srcRightRotate_5", 10, null);
        srcRightRotate.addCodeLine("    if (x.p == T.null)", // 6
                "srcRightRotate_6", 10, null);
        srcRightRotate.addCodeLine("        T.root = y", // 7
                "srcRightRotate_7", 10, null);
        srcRightRotate.addCodeLine("    else", // 8
                "srcRightRotate_8", 10, null); // if(y.p.left = y)
        srcRightRotate.addCodeLine("        if (x == x.p.right)", // 9
                "srcRightRotate_9", 10, null);
        srcRightRotate.addCodeLine("            x.p.right = y", // 10
                "srcRightRotate_10", 10, null);
        srcRightRotate.addCodeLine("        else", // 11
                "srcRightRotate_11", 10, null);
        srcRightRotate.addCodeLine("            x.p.left = y", // 12
                "srcRightRotate_12", 10, null);
        srcRightRotate.addCodeLine("    y.right = x", // 13
                "srcRightRotate_13", 10, null);
        srcRightRotate.addCodeLine("    x.p = y", // 14
                "srcRightRotate_14", 10, null);

        srcRightRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

        // LEFT ROTATE
        // SourceCode Props init:
        sourceCodePropsLeftRotate = new SourceCodeProperties();

        sourceCodePropsLeftRotate.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        sourceCodePropsLeftRotate.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        sourceCodePropsLeftRotate.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
        sourceCodePropsLeftRotate.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
                highlightColor);

        // SourceCode itself init
        srcLeftRotate = lang.newSourceCode(new Offset(-120, 370, "srcINSERT_FIX", "NE"),
                "srcLeftRotate",
                null, sourceCodePropsLeftRotate);

        srcLeftRotate.addCodeLine("LEFT-ROTATE(T, x)", // 0
                "srcLeftRotate_0", 10, null);
        srcLeftRotate.addCodeLine("    y = x.right", // 1
                "srcLeftRotate_1", 10, null);
        srcLeftRotate.addCodeLine("    x.right = y.left", // 2
                "srcLeftRotate_2", 10, null);
        srcLeftRotate.addCodeLine("    if (y.left != T.null)", // 3
                "srcLeftRotate_3", 10, null);
        srcLeftRotate.addCodeLine("    y.left.p = x", // 4
                "srcLeftRotate_4", 10, null);
        srcLeftRotate.addCodeLine("    y.p = x.p", // 5
                "srcLeftRotate_5", 10, null);
        srcLeftRotate.addCodeLine("        if (x.p == T.null)", // 6
                "srcLeftRotate_6", 10, null);
        srcLeftRotate.addCodeLine("    T.root = y", // 7
                "srcLeftRotate_7", 10, null);
        srcLeftRotate.addCodeLine("        else", // 8
                "srcLeftRotate_8", 10, null);
        srcLeftRotate.addCodeLine("            if (x == x.p.left)", // 9
                "srcLeftRotate_9", 10, null);
        srcLeftRotate.addCodeLine("        x.p.left = y", // 10
                "srcLeftRotate_10", 10, null);
        srcLeftRotate.addCodeLine("            else", // 11
                "srcLeftRotate_11", 10, null);
        srcLeftRotate.addCodeLine("    x.p.right = y", // 12
                "srcLeftRotate_12", 10, null);
        srcLeftRotate.addCodeLine("    y.left = x", // 13
                "srcLeftRotate_13", 10, null);
        srcLeftRotate.addCodeLine("    x.p = y", // 14
                "srcLeftRotate_14", 10, null);

        srcLeftRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

        if (hidden) {
            srcLeftRotate.hide();
            srcRightRotate.hide();
            srcINSERT.hide();
            srcINSERT_FIX.hide();
        }

    }

    /**
     * init a graph-primitive and return it
     * 
     * @param n
     * @param adj
     * @return Graph - initialised graph primitive
     */
    private static void makeGraph(int[][] adj, Node[] n, String[] labels) {
        // GRAPH

        GraphProperties graphProps = new GraphProperties();
        graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, GRAPH_RED_COLOR);
        graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, GRAPH_BLACK_COLOR);

        getDefaultGraph(graphProps, adj, n, labels);

    }

    private void drawNullNode() {

        TextProperties null_text_properties = new TextProperties();
        null_text_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 14));

        lang.newText(new Coordinates(border_px * 2, border_px * 5),
                "T.null", "null_text", null, null_text_properties);

        CircleProperties null_circle_properties = new CircleProperties();
        null_circle_properties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        null_circle_properties.set(AnimationPropertiesKeys.FILL_PROPERTY, GRAPH_BLACK_COLOR);
        null_circle_properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        lang.newCircle(new Offset(18, 10, "null_text", "NW"), 25,
                "null_circle", null, null_circle_properties);
    }

    /**
     * puts the children of the inserted vertices on its adjacency list, doing
     * this the conection (vertex) line will be drawn in the animal Animation
     * 
     * @param amoundOfInsertedNodes123
     * @param x
     */
    private static int[][] updateAdjacencyMatrix() {

        int i = 0;
        int leftChildVertexIndex = -1;
        int rightChildVertexIndex = -1;
        int[] list;
        int[][] adjMatrix = new int[RB_TREES_INSERT.amoundOfInsertedVertices][RB_TREES_INSERT.amoundOfInsertedVertices];

        for (Vertex_INSERT n : RB_TREES_INSERT.vertices) {

            leftChildVertexIndex = -1;
            rightChildVertexIndex = -1;

            if (n != null) {
                // create new adj List, that has
                // "amoundOfInsertedVertex" fields
                n.initAdjList(RB_TREES_INSERT.amoundOfInsertedVertices);
                leftChildVertexIndex = n.getLeftChild().getIndexVertex();
                rightChildVertexIndex = n.getRightChild().getIndexVertex();

                if (leftChildVertexIndex != -1)
                    n.setChildInAdjacencyList(leftChildVertexIndex);

                if (rightChildVertexIndex != -1)
                    n.setChildInAdjacencyList(rightChildVertexIndex);

                list = n.getAdjacencyList();

                for (int j = 0; j < list.length; j++) {
                    adjMatrix[i][j] = list[j];
                }

                i++;
            }
        }

        return adjMatrix;
    }

    private static void getDefaultGraph(GraphProperties graphProps, int[][] adj, Node[] n,
            String[] givenLabels) {


        // define the vertices and their positions
        Node[] graphVertices = new Node[RB_TREES_INSERT.amoundOfInsertedVertices];

        graphVertices = n;

        // define the names of the vertices
        String[] labels = new String[amoundOfInsertedVertices];

        labels = givenLabels;

        StringBuffer sb = new StringBuffer();
        sb.delete(0, sb.length());
        sb.append("graph").append(graphCounter);
        graphCounter++;

        graph = lang.newGraph(sb.toString(), adj, graphVertices, labels, null,
                graphProps);

    }

    public String getName() {
        return translator.translateMessage("algorithmName");
    }

    public String getAlgorithmName() {
        return translator.translateMessage("algorithmName");
    }

    public String getAnimationAuthor() {
        return "Maciej Aleksander Mokwinski + Julian Harbarth";
    }

    public String getDescription() {
        return translator.translateMessage("descriptionLine1")
                + "\n"
                + translator.translateMessage("descriptionLine2")
                + "\n\n"
                + translator.translateMessage("descriptionLine3")
                + "\n\n"
                + translator.translateMessage("descriptionLine4")
                + "\n\n"
                + translator.translateMessage("propertyHead")
                + "\n"
                + translator.translateMessage("property1")
                + "\n"
                + translator.translateMessage("property2")
                + "\n"
                + translator.translateMessage("property3")
                + "\n"
                + translator.translateMessage("property4")
                + "\n"
                + translator.translateMessage("property5")
                + translator.translateMessage("property51");
    }

    public String getCodeExample() {
        return ""
                + "RB-Insert(T, z) {                    RB-INSERT-FIXUP(T, z)"
                + "\n"
                + "    y = null                             while(z.p.color == RED)"
                + "\n"
                + "    x = T.root                               if(z.p == z.p.p.left)"
                + "\n"
                + "    while (x != null)                            y = z.p.p.right"
                + "\n"
                + "        y = x                                    if(y.color == RED)"
                + "\n"
                + "        if (z.key < x.key)                           z.p.color = BLACK"
                + "\n"
                + "            x = x.left                               y.color = BLACK"
                + "\n"
                + "        else                                         z.p.p.color = RED"
                + "\n"
                + "            x = x.right                              z = z.p.p"
                + "\n"
                + "    z.p = y                                      else"
                + "\n"
                + "    if (y == null)                                   if(z == z.p.right)"
                + "\n"
                + "        T.root = z                                       z = z.p"
                + "\n"
                + "    else                                                 LEFT-ROTATE(T, z)"
                + "\n"
                + "        if (z.key < y.key)                           z.p.color = BLACK"
                + "\n"
                + "            y.left = z                               z.p.p.color = RED"
                + "\n"
                + "        else                                         RIGHT-ROTATE(T, z.p.p)"
                + "\n"
                + "        y.right = z                                                        "
                + "\n"
                + "    z.left = null                            else // z.p == z.p.p.right"
                + "\n"
                + "    z.right = null                               y = z.p.p.left"
                + "\n"
                + "    z.color = RED                                if(y.color == RED)"
                + "\n"
                + "    RB-Insert-Fixup(T, z)                            z.p.color = BLACK"
                + "\n"
                + "}                                                    y.color = BLACK"
                + "\n"
                + "                                                     z.p.p.color = RED"
                + "\n"
                + "                                                     z = z.p.p"
                + "\n"
                + "                                                 else"
                + "\n"
                + "                                                     if(z == z.p.left)"
                + "\n"
                + "                                                         z = z.p"
                + "\n"
                + "                                                         RIGHT-ROTATE(T, z)"
                + "\n"
                + "                                                     z.p.color = BLACK"
                + "\n"
                + "                                                     z.p.p.color = RED"
                + "\n"
                + "                                                     LEFT-ROTATE(T, z.p.p)"
                + "\n"
                + "                                         T.root.color = BLACK"
                + "\n";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return alocale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}