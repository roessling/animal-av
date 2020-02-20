package generators.misc.ershov;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.controller.CounterController;
import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.misc.ershov.parser.Parser;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ErshovNumbers {
    private String expr;
    private TreeNode tree;

    private Translator translator;

    private int nextQuestionID = 0;
    private Map<String, Integer> questionsByGroup = new HashMap<>();

    private Language lang;
    private TextProperties textPropertiesHeader = new TextProperties();
    private TextProperties textPropertiesDescription = new TextProperties();
    private TextProperties textPropertiesSubheading = new TextProperties();
    private SourceCodeProperties sourceCodeProperties = new SourceCodeProperties();
    private RectProperties rectPropertiesGreen = new RectProperties();
    private GraphProperties graphProperties = new GraphProperties();

    private Variables i;

    public ErshovNumbers(Language language, String expr, Translator translator) {
        this.expr = expr;

        tree = new Parser().parse(expr);

        lang = language;
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);

        this.translator = translator;
    }

    public void determineProperties(SourceCodeProperties graph, RectProperties rect, SourceCodeProperties source, TextProperties text) {
        String font = ((Font) text.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName();

        textPropertiesHeader = new TextProperties();
        textPropertiesHeader.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        textPropertiesHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.BOLD, 25));

        textPropertiesDescription = new TextProperties();
        textPropertiesDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.PLAIN, 20));

        textPropertiesSubheading = new TextProperties();
        textPropertiesSubheading.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.BOLD, 20));

        Color highlightCode = ((Color) source.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
        String fontCode = ((Font) source.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName();

        sourceCodeProperties = new SourceCodeProperties();
        sourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightCode);
        sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(fontCode, Font.PLAIN, 16));

        rectPropertiesGreen = rect;
        /*
        Color fillRect = ((Color) rect.get(AnimationPropertiesKeys.FILL_PROPERTY));

        rectPropertiesGreen = new RectProperties();
        rectPropertiesGreen.set(AnimationPropertiesKeys.FILL_PROPERTY, fillRect);
        rectPropertiesGreen.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectPropertiesGreen.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        */

        Color highlightGraph = ((Color) graph.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));

        graphProperties = new GraphProperties();
        graphProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightGraph);
        graphProperties.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
        graphProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    }

    public void doAnimation() {
        Rect hRect = doHeader();

        lang.nextStep(translator.translateMessage("introduction"));

        Text description = doAlgoDescription(hRect);

        lang.nextStep();

        description.hide();

        SourceCode exercise = doTaskDescription(hRect);

        lang.nextStep(translator.translateMessage("tree"));

        Graph graph = doGraph(exercise, tree);

        lang.nextStep();

        doTaskDescriptionRest(exercise);

        lang.nextStep(translator.translateMessage("pseudocode"));

        SourceCode pseudocode = doPseudocode(graph, tree, graph);

        lang.nextStep(translator.translateMessage("algorithm"));

        doCounter(pseudocode);
        int erschov = simulation(exercise, graph, pseudocode, tree);

        lang.nextStep(translator.translateMessage("conclusion"));

        end(exercise, erschov);

        lang.finalizeGeneration();
    }

    private Rect doHeader() {
        Text header = lang.newText(new Coordinates(20, 30),
                translator.translateMessage("header"),
                "header", null, textPropertiesHeader);

        return lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
                new Offset(5, 5, header, AnimalScript.DIRECTION_SE),
                "hRect", null, rectPropertiesGreen);
    }

    private Text doAlgoDescription(Primitive reference) {
        return lang.newText(
                new Offset(0, 30, reference, AnimalScript.DIRECTION_SW),
                translator.translateMessage("description"),
                "description",
                null,
                textPropertiesDescription);
    }

    private SourceCode doTaskDescription(Primitive reference) {
        Text subheading1 = lang.newText(
                new Offset(0, 30, reference, AnimalScript.DIRECTION_SW),
                translator.translateMessage("task"),
                "subheading1",
                null,
                textPropertiesSubheading
        );

        SourceCode exercise = lang.newSourceCode(
                new Offset(10, 0, subheading1, AnimalScript.DIRECTION_SW),
                "exercise",
                null,
                sourceCodeProperties
        );

        exercise.addCodeLine(translator.translateMessage("taskDescription1"), null, 0, null);
        exercise.addCodeLine(translator.translateMessage("taskDescription2"), null, 0, null);
        exercise.addCodeLine("'" + expr + "' ?", null, 2, null);

        lang.nextStep();

        exercise.addCodeLine("", null, 0, null);
        exercise.addCodeLine(translator.translateMessage("taskDescription3"), null, 0, null);

        return exercise;
    }

    private Graph doGraph(Primitive reference, TreeNode tree) {
        Text subheading2 = lang.newText(
                new Offset(30, -37, reference, AnimalScript.DIRECTION_NE),
                translator.translateMessage("graph"),
                "subheading2",
                null,
                textPropertiesSubheading
        );

        int nodeCount = tree.getNodeCount();

        // Koordinaten Berechnung
        Node[] graphNodes = new Node[nodeCount];
        String[] labels = new String[nodeCount];
        int[][] graphAdjacencyMatrix = new int[nodeCount][nodeCount];

        calculateGraph(tree, graphNodes, graphAdjacencyMatrix, labels, 0, 0, tree.getMaxLeafs() * 50, 0);

        TreeNode leftestNode = tree;
        while (leftestNode.hasLeft())
            leftestNode = leftestNode.getLeft();

        // LeftestXValue Berechnung
        int leftestXValue = ((Coordinates) graphNodes[leftestNode.getRepresentation()]).getX();

        for (int i = 0; i < graphNodes.length; i++) {
            int x = ((Coordinates) graphNodes[i]).getX();
            int y = ((Coordinates) graphNodes[i]).getY();
            graphNodes[i] = new Offset(x - leftestXValue + 30, y + 20, subheading2, AnimalScript.DIRECTION_SW);
        }

        Graph graph = lang.newGraph(
                "graph",
                graphAdjacencyMatrix,
                graphNodes,
                labels,
                null,
                graphProperties
        );

        // hide all edge weights
        for (int i = 0; i < nodeCount; i++)
            for (int j = 0; j < nodeCount; j++)
                graph.hideEdgeWeight(i, j, null, null);

        return graph;
    }

    private int calculateGraph(TreeNode tree, Node[] coords, int[][] graphAdjacencyMatrix, String[] labels, int i, int xStart, int xEnd, int y) {
        Coordinates c = new Coordinates((xEnd - xStart) / 2 + xStart, y);
        coords[i] = c;
        labels[i] = tree.getKey();
        tree.setRepresentation(i);
        int myI = i;
        i++;

        if (tree.hasLeft()) {
            graphAdjacencyMatrix[myI][i] = 1;
            i = calculateGraph(tree.getLeft(), coords, graphAdjacencyMatrix, labels, i, xStart, c.getX(), y + 100);
        }
        if (tree.hasRight()) {
            graphAdjacencyMatrix[myI][i] = 1;
            i = calculateGraph(tree.getRight(), coords, graphAdjacencyMatrix, labels, i, c.getX(), xEnd, y + 100);
        }
        return i;
    }

    private void doTaskDescriptionRest(SourceCode exercise) {
        exercise.addCodeLine("", null, 0, null);
        exercise.addCodeLine("", null, 0, null);
        exercise.addCodeLine(translator.translateMessage("taskDescription4"), null, 0, null);
        exercise.addCodeLine(translator.translateMessage("taskDescription5"), null, 0, null);
        exercise.addCodeLine("ershov('/')", null, 2, null);

    }

    private SourceCode doPseudocode(Primitive reference, TreeNode tree, Graph graph) {
        TreeNode rightestNode = tree;
        while (rightestNode.hasRight())
            rightestNode = rightestNode.getRight();

        int rightestX = ((Offset) graph.getNode(rightestNode.getRepresentation())).getX();

        Text subheading3 = lang.newText(
                new Offset(30, -38, reference, AnimalScript.DIRECTION_NE),
                translator.translateMessage("pseudocodeTitle"),
                "subheading3",
                null,
                textPropertiesSubheading
        );

        SourceCode pseudocode = lang.newSourceCode(
                new Offset(40, 0, subheading3, AnimalScript.DIRECTION_SW),
                "pseudocode",
                null,
                sourceCodeProperties
        );

        pseudocode.addCodeLine("int erschov(v)", null, 0, null);
        pseudocode.addCodeLine("if v.left is NIL and v.right is NIL", null, 2, null);
        pseudocode.addCodeLine("return 1", null, 4, null);
        pseudocode.addCodeLine("else if v.left is NIL", null, 2, null);
        pseudocode.addCodeLine("return ershov(v.right)", null, 4, null);
        pseudocode.addCodeLine("else if v.right is NIL", null, 2, null);
        pseudocode.addCodeLine("return ershov(v.left)", null, 4, null);
        pseudocode.addCodeLine("else", null, 2, null);
        pseudocode.addCodeLine("eLeft <- ershov(v.left)", null, 4, null);
        pseudocode.addCodeLine("eRight <- ershov(v.right)", null, 4, null);
        pseudocode.addCodeLine("if eLeft == eRight", null, 4, null);
        pseudocode.addCodeLine("return eLeft + 1", null, 6, null);
        pseudocode.addCodeLine("else", null, 4, null);
        pseudocode.addCodeLine("return max(eLeft, eRight)", null, 6, null);
        pseudocode.addCodeLine("endif", null, 4, null);
        pseudocode.addCodeLine("endif", null, 2, null);

        SourceCode pseudocodeLineNumbers = lang.newSourceCode(
                new Offset(10, 0, subheading3, AnimalScript.DIRECTION_SW),
                "pseudocodeLineNumbers",
                null,
                sourceCodeProperties
        );
        for (int i = 1; i <= 9; i++)
            pseudocodeLineNumbers.addCodeLine("  " + i + ".", null, 0, null);
        for (int i = 10; i <= 16; i++)
            pseudocodeLineNumbers.addCodeLine(i + ".", null, 0, null);

        return pseudocode;
    }

    private void doCounter(Primitive reference) {
        i = lang.newVariables();
        i.declare("int", "ershovCalled");
        i.set("ershovCalled", "0");

        TwoValueCounter counter = lang.newCounter(i);
        Text text = lang.newText(new Offset(-30, 20, reference, AnimalScript.DIRECTION_SW), translator.translateMessage("amountCall") + " " + counter.getAssigments(), "ershovCalledCounter", null, textPropertiesDescription);

        i.addObserver(new CounterController() {
            @Override
            public void handleOperations(PrimitiveEnum primitiveEnum) {
                text.setText(translator.translateMessage("amountCall") +" " + counter.getAssigments(), null, null);
            }
        });
    }

    private int simulation(SourceCode exercise, Graph graph, SourceCode pseudocode, TreeNode tree) {
        exercise.highlight(9);
        lang.nextStep();
        exercise.unhighlight(9);

        int ershov = ershov(graph, pseudocode, tree);

        exercise.highlight(9);
        exercise.addCodeLine("= " + ershov, null, 4, null);
        exercise.highlight(10);

        lang.nextStep();
        exercise.unhighlight(9);
        exercise.unhighlight(10);

        return ershov;
    }

    private int ershov(Graph graph, SourceCode pseudocode, TreeNode tree) {
        // Trigger counter
        i.set("ershovCalled", "0");

        int currentNode = tree.getRepresentation();

        // Mark the actual node
        graph.highlightNode(currentNode, null, null);

        pseudocode.highlight(0);
        lang.nextStep();

        pseudocode.unhighlight(0);
        pseudocode.highlight(1);
        lang.nextStep();

        // when is a leaf()
        if (tree.isLeaf()) {
            doRandomizedNodeQuestion(1, tree.getKey(), "leafQuestion", 1);

            pseudocode.unhighlight(1);
            pseudocode.highlight(2);
            lang.nextStep();
            pseudocode.unhighlight(2);

            // unmark the actual node, because here you return
            graph.unhighlightNode(currentNode, null, null);
            return 1;
        }

        pseudocode.unhighlight(1);
        pseudocode.highlight(3);
        lang.nextStep();

        // when node has only a right neighbor
        if (!tree.hasLeft()) {
            pseudocode.unhighlight(3);
            pseudocode.highlight(4);
            lang.nextStep();
            pseudocode.unhighlight(4);

            int eRight = ershov(graph, pseudocode, tree.getRight());
            pseudocode.highlight(4);
            graph.setEdgeWeight(currentNode, tree.getRight().getRepresentation(), eRight, null, null);
            graph.showEdgeWeight(currentNode, tree.getRight().getRepresentation(), null, null);

            lang.nextStep();

            doRandomizedNodeQuestion(eRight, tree.getKey(), "oneChildQuestion", 1);

            graph.unhighlightNode(currentNode, null, null);
            pseudocode.unhighlight(4);
            return eRight;
        }

        pseudocode.unhighlight(3);
        pseudocode.highlight(5);
        lang.nextStep();

        // when node only has a left neighbor
        if (!tree.hasRight()) {
            pseudocode.unhighlight(5);
            pseudocode.highlight(6);
            lang.nextStep();
            pseudocode.unhighlight(6);

            int eLeft = ershov(graph, pseudocode, tree.getLeft());
            pseudocode.highlight(6);
            graph.setEdgeWeight(currentNode, tree.getLeft().getRepresentation(), eLeft, null, null);
            graph.showEdgeWeight(currentNode, tree.getLeft().getRepresentation(), null, null);

            lang.nextStep();

            doRandomizedNodeQuestion(eLeft, tree.getKey(), "oneChildQuestion", 1);

            pseudocode.unhighlight(6);
            graph.unhighlightNode(currentNode, null, null);
            return eLeft;
        }

        pseudocode.unhighlight(5);
        pseudocode.highlight(7);
        lang.nextStep();

        pseudocode.unhighlight(7);
        pseudocode.highlight(8);
        lang.nextStep();
        pseudocode.unhighlight(8);

        // get the erschov(left)
        int eLeft = ershov(graph, pseudocode, tree.getLeft());
        graph.setEdgeWeight(currentNode, tree.getLeft().getRepresentation(), eLeft, null, null);
        graph.showEdgeWeight(currentNode, tree.getLeft().getRepresentation(), null, null);
        pseudocode.highlight(8);
        lang.nextStep();

        pseudocode.unhighlight(8);
        pseudocode.highlight(9);
        lang.nextStep();
        pseudocode.unhighlight(9);

        // get ershov(right)
        int eRight = ershov(graph, pseudocode, tree.getRight());
        graph.setEdgeWeight(currentNode, tree.getRight().getRepresentation(), eRight, null, null);
        graph.showEdgeWeight(currentNode, tree.getRight().getRepresentation(), null, null);
        pseudocode.highlight(9);
        lang.nextStep();

        pseudocode.unhighlight(9);
        pseudocode.highlight(10);
        lang.nextStep();

        doRandomizedNodeQuestion(
                eLeft == eRight ? eLeft + 1 : Math.max(eLeft, eRight),
                tree.getKey(),
                "twoChildrenQuestion",
                2);

        // line 10
        pseudocode.unhighlight(10);
        if (eLeft == eRight) {
            pseudocode.highlight(11);
            lang.nextStep();
            pseudocode.unhighlight(11);

            graph.unhighlightNode(currentNode, null, null);
            return eLeft + 1;
        }

        pseudocode.highlight(12);
        lang.nextStep();

        // line 12
        pseudocode.unhighlight(12);
        pseudocode.highlight(13);
        lang.nextStep();
        pseudocode.unhighlight(13);

        graph.unhighlightNode(currentNode, null, null);
        return Math.max(eLeft, eRight);
    }

    private void end(Primitive reference, int erschov) {
        Text end = lang.newText(
                new Offset(0, 30, reference, AnimalScript.DIRECTION_SW),
                translator.translateMessage("end1"),
                "end",
                null,
                textPropertiesDescription
        );

        Text end2 = lang.newText(
                new Offset(0, 5, end, AnimalScript.DIRECTION_SW),
                translator.translateMessage("end2"),
                "end2",
                null,
                textPropertiesDescription
        );

        lang.newText(
                new Offset(0, 5, end2, AnimalScript.DIRECTION_SW),
                translator.translateMessage("end3") + " " + erschov + " " +translator.translateMessage("end4"),
                "end3",
                null,
                textPropertiesDescription
        );
    }


    /*
     * Questions
     */
    private boolean doRandomizedNodeQuestion(int ershov, String nodeKey, String group, int limit) {
        Integer createdCount = questionsByGroup.getOrDefault(group, 0);
        if (createdCount < limit && new Random().nextBoolean()) {
            doNodeQuestion(ershov, nodeKey);
            questionsByGroup.put(group, createdCount + 1);
            return true;
        }
        return false;
    }

    private void doNodeQuestion(int ershov, String nodeKey) {
        FillInBlanksQuestionModel question = new FillInBlanksQuestionModel(nextQuestionID + "");
        question.setPrompt(translator.translateMessage("question") + "'" + nodeKey + "'?");
        question.addAnswer(ershov + "", 5, translator.translateMessage("feedback"));

        nextQuestionID++;
        lang.addFIBQuestion(question);
        lang.nextStep();
    }
}
