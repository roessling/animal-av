package generators.graph;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Andre Pacak
 * @author Marc Semmler
 */
public class HillClimbing implements ValidatingGenerator {
    // properties which can be set with Generator
    private CircleProperties currentNodeHighlightColor;
    private CircleProperties xNodeHighlightColor;
    private CircleProperties nodesToCheckHighlightColor;
    private int[] nodeValues;
    private int[][] coordinates;
    private SourceCodeProperties sourceCodeHighlightColor;
    private int[][] adjacencyMatrix;
    private int startNode;

    private int probOfQuestions;

    private Language language;
    private Text header;
    private TextProperties textProps;
    private Rect headerBorder;
    private SourceCode src;
    private Graph graph = null;
    private ArrayList<Circle> circleList = null;

    public boolean showQuestion() {
        Random rnd = new Random();
        int generatedNumber = rnd.nextInt(99) + 1;
        return probOfQuestions != 0 && generatedNumber < this.probOfQuestions;
    }

    private void showStepDescription(String text, List<Text> lines) {
        String[] words = text.split(" ");
        int currentLineIndex = 0;
        String currentLineText = "";
        int maxWidth = Math.min(60, text.length());
        for (int i = 0; i < words.length; i++) {
            if ((currentLineText.length() + words[i].length()) <= maxWidth) {
                currentLineText += words[i] + " ";
            }
            if (i != words.length - 1) {
                if ((currentLineText.length() + words[i + 1].length()) > maxWidth) {
                    lines.get(currentLineIndex).setText(currentLineText, null, null);
                    currentLineText = "";
                    currentLineIndex++;
                }
            } else {
                lines.get(currentLineIndex).setText(currentLineText, null, null);
                currentLineText = "";
                currentLineIndex++;
            }
        }
        for (int i = currentLineIndex; i < lines.size(); i++) {
            lines.get(i).setText("", null, null);
        }
    }

    /*
     * Hilfsfunktionen für Graphenoperationen
     */
    private ArrayList<Circle> convertGraphToCircles(int numberOfDigits) {
        if (graph != null) {
            ArrayList<Circle> circleList = new ArrayList<>();
            for (int i = 0; i < graph.getSize(); i++) {
                Coordinates coords = (Coordinates) graph.getNode(i);
                CircleProperties circleProps = new CircleProperties();
                circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
                circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
                        Color.white);
                circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
                Circle c = language.newCircle(new Coordinates(coords.getX() + 4
                                * numberOfDigits, coords.getY() + 8),
                        12 + 4 * numberOfDigits, "circle" + i, null,
                        circleProps);
                c.hide();
                circleList.add(c);
            }
            return circleList;
        }
        return null;
    }

    private ArrayList<Integer> getNeighbours(int nodeIndex) {
        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        ArrayList<Integer> neighbours = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[nodeIndex][i] != 0) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    private String getNeighboursString(ArrayList<Integer> neighbours) {
        StringBuilder sb = new StringBuilder("{");
        for (int index : neighbours) {
            sb.append(graph.getNodeLabel(index)).append(", ");
        }
        if (sb.length() > 2) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("}");
        return sb.toString();
    }

    private void highlightNode(int index, CircleProperties cp) {
        circleList.get(index).changeColor(
                AnimationPropertiesKeys.FILL_PROPERTY,
                (Color) cp.get(AnimationPropertiesKeys.FILL_PROPERTY), null,
                null);
    }

    private void unhighlightNode(int index) {
        circleList.get(index).changeColor(
                AnimationPropertiesKeys.FILL_PROPERTY, Color.white, null, null);
    }

    private boolean isGlobalMax(Graph graph, int y) {
        if (graph != null) {
            int max = 0;
            for (int i = 0; i < graph.getSize(); i++) {
                if (Integer.parseInt(graph.getNodeLabel(i)) >= max) {
                    max = Integer.parseInt(graph.getNodeLabel(i));
                }
            }
            return y == max;
        }
        return false;
    }

    /*
     * Einzelne Schritte der Animation.
     */
    public void showHeader() {
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 25));
        header = language.newText(new Coordinates(20, 20),
                "Hill Climbing Algorithmus", "headline", null, headerProps);
        RectProperties headerBorderProps = new RectProperties();
        headerBorderProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        headerBorderProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
                Color.WHITE);
        headerBorderProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        headerBorder = language.newRect(new Offset(-5, -5, "headline",
                        AnimalScript.DIRECTION_NW), new Offset(5, 5, "headline",
                        AnimalScript.DIRECTION_SE), "headerBorder", null,
                headerBorderProps);
    }

    public void showDescription() {
        SourceCode description = language.newSourceCode(new Offset(0, 60,
                        "headline", AnimalScript.DIRECTION_NW), "sourceCode", null,
                sourceCodeHighlightColor);
        description
                .addCodeLine(
                        "Der Hill Climbing Algorithmus ist ein heuristisches Optimierungsverfahren. Wie der Name schon nahelegt,",
                        null, 0, null);
        description
                .addCodeLine(
                        "wird sich einer potenziellen Loesung Schritt fuer Schritt angenaehert (wie bei einem Bergsteiger, der einen Berg erklimmt).",
                        null, 0, null);
        description
                .addCodeLine(
                        "Dabei sucht der Algorithmus in jedem Schritt nach der besten lokalen Veraenderung. Somit naehert er",
                        null, 0, null);
        description
                .addCodeLine(
                        "sich durch jede Iteration an ein lokales Maximum, also eine potentielle Loesung, an. ",
                        null, 0, null);
        description
                .addCodeLine(
                        "Der Algorithmus bricht ab wenn er keine lokale Verbesserung mehr finden kann. Dadurch ist er nicht ",
                        null, 0, null);
        description
                .addCodeLine(
                        "immer in der Lage das globale Maximum (Bergspitze) zu finden.",
                        null, 0, null);
        description
                .addCodeLine(
                        "Das finden einer Loesung ist stark abhaengig vom Startpunkt und der vorgegebenen Heuristik des Verfahrens.",
                        null, 0, null);
        language.nextStep("Zeige Beschreibung");
    }

    public void showAlgorithm() {
        if (showQuestion()) {
            MultipleChoiceQuestionModel globalMax = new MultipleChoiceQuestionModel(
                    "globalMax");
            globalMax
                    .setPrompt("Ist der Algorithmus immer in der Lage das globale Maximum/Minimum zu finden?");
            globalMax.addAnswer("Ja", 0, "Leider falsch");
            globalMax.addAnswer("Nein", 1, "Diese Antwort ist korrekt.");
            language.addMCQuestion(globalMax);
        }
        language.hideAllPrimitives();
        header.show();
        headerBorder.show();
        src = language.newSourceCode(new Offset(0, 50, "headline",
                        AnimalScript.DIRECTION_NW), "sourceCode", null,
                sourceCodeHighlightColor);
        src.addCodeLine("hillclimbing(startNode)", null, 0, null);
        src.addCodeLine("currentNode = startNode", null, 1, null); // 1
        src.addCodeLine("loop do", null, 1, null); // 2
        src.addCodeLine("L=NEIGHBORS(currentNode)", null, 2, null); // 3
        src.addCodeLine("nextEval = -1", null, 2, null);
        src.addCodeLine("nextNode = NULL", null, 2, null);
        src.addCodeLine("for all x in L", null, 2, null);
        src.addCodeLine("if(EVAL(x)>nextEval)", null, 3, null);
        src.addCodeLine("nextEval = EVAL(x)", null, 4, null);
        src.addCodeLine("nextNode = x", null, 4, null);
        src.addCodeLine("if(nextEval <= EVAL(currentNode))", null, 2, null);
        src.addCodeLine("return currentNode", null, 3, null);
        src.addCodeLine("currentNode = nextNode", null, 2, null); // 12

        showVariableState();
        showLegend();
        graph.show();

        circleList.forEach(Circle::show);
        stepThroughAlgorithm(startNode);
    }

    public void showConclusion() {
        language.hideAllPrimitives();
        header.show();
        headerBorder.show();
        SourceCode conclusion = language.newSourceCode(new Offset(0, 60,
                        "headline", AnimalScript.DIRECTION_NW), "sourceCode", null,
                sourceCodeHighlightColor);
        conclusion.addCodeLine("Konklusion", null, 0, null);
        conclusion
                .addCodeLine(
                        "Wie schon beschrieben wurde, findet der Algorithmus nicht immer das globale Maximum. Dies liegt",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "daran, dass der Algorithmus gierig ist. Gierige Algorithmen zeichen sich dadurch aus, dass sie ",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "sich nur schrittweise den Folgezustand anschauen und den waehlen, der zu diesem Zeitpunkt das beste ",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "Ergebnis erzielt.",
                        null, 0, null);
        conclusion.addCodeLine("", null, 0, null);
        conclusion
                .addCodeLine(
                        "Dem kann man entgegenwirken indem man den Algorithmus auf den gleichen Graphen mit mehreren randomisierten  ",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "Startpunkten ausfuehrt. Somit wird die Chance erhoeht das globale Maximum zu finden. ",
                        null, 0, null);
        conclusion.addCodeLine("", null, 0, null);
        conclusion
                .addCodeLine(
                        "Das Verfahren kann auch dazu verwendet werden lokale Minima zu suchen. Um dies zu erreichen muss man ",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "im Pseudocode die Vergleichoperatoren folgendermaßen aendern: < in > und <= in >=.",
                        null, 0, null);
        conclusion.addCodeLine("", null, 0, null);
        conclusion
                .addCodeLine(
                        "Der Hill Climbing Algorithmus ist nicht optimal und nicht immer in der Lage das globale Maximum zu finden. ",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "Deswegen ist die Komplexitaet des Algorithmus O(∞).",
                        null, 0, null);
        conclusion.addCodeLine("", null, 0, null);
        conclusion
                .addCodeLine("In dem Beispiel sind die Namen der Knoten gleich mit dem Wert des Knotens, der von der Heurstik berechnet wurde."
                        , null, 0, null);
        conclusion
                .addCodeLine("In einem realen Szenario haben die Knoten einen Namen und einen Wert, welche unterschiedlich sind."
                        , null, 0, null);
        // zeige Konkulsion an
        language.nextStep("Zeige Konkulsion an");
    }

	/*
     * Hilfsmethoden für die Visualisierung des Algorithmus.
	 */

    public void stepThroughAlgorithm(int startNode) {
        Text currentNodeValue = language.newText(new Offset(100, -10,
                        "variableStateLines2", AnimalScript.DIRECTION_W), "xxx",
                "currentNodeValue", null, textProps);
        Text LValue = language.newText(new Offset(0, 20, "currentNodeValue",
                AnimalScript.DIRECTION_NW), "xxx", "LValue", null, textProps);
        Text nextEvalValue = language.newText(new Offset(0, 40,
                        "currentNodeValue", AnimalScript.DIRECTION_NW), "xxx",
                "nextEvalValue", null, textProps);
        Text nextNodeValue = language.newText(new Offset(0, 60,
                        "currentNodeValue", AnimalScript.DIRECTION_NW), "xxx",
                "nextNodeValue", null, textProps);
        Text xValue = language.newText(new Offset(0, 80, "currentNodeValue",
                AnimalScript.DIRECTION_NW), "xxx", "xValue", null, textProps);
        TextProperties stepDescriptionProps = new TextProperties();
        Text stepDescriptionText = language.newText(
                new Offset(0, 200, "sourceCode", AnimalScript.DIRECTION_W),
                "", "stepDescription", null, stepDescriptionProps);
        Text stepDescriptionContText = language.newText(
                new Offset(0, 220, "sourceCode", AnimalScript.DIRECTION_W),
                "", "stepDescriptionCont", null, stepDescriptionProps);
        Text stepDescriptionCont2Text = language.newText(
                new Offset(0, 240, "sourceCode", AnimalScript.DIRECTION_W),
                "", "stepDescriptionCont", null, stepDescriptionProps);
        List<Text> stepDescriptionTextField =
                Arrays.asList(stepDescriptionText, stepDescriptionContText, stepDescriptionCont2Text);
        Variables vars = language.newVariables();
        vars.declare("String", "currentNode", "", "aktueller Knoten");
        vars.declare("String", "L", "", "Liste der Nachbarn des aktuellen Knoten");
        vars.declare("String", "nextEval", "", "größster Nachbar Wert");
        vars.declare("String", "nextNode", "", "größster Nachbar Knoten");
        vars.declare("String", "x", "", "aktuell betrachterer Nachbar");
        language.nextStep("Zeige Sourcecode und Graphen");
        src.highlight(0);

        showStepDescription("Starte Algorithmus.", Arrays.asList(stepDescriptionText, stepDescriptionContText));
        language.nextStep("Starte Algorithmus");
        int currentNode = startNode;
        currentNodeValue.setText(graph.getNodeLabel(currentNode), null, null);
        vars.set("currentNode", graph.getNodeLabel(currentNode));
        highlightNode(currentNode, this.currentNodeHighlightColor);
        boolean nextNQu = true;
        src.highlight(1);
        src.unhighlight(0);
        showStepDescription("Initalisiere currentNode.", stepDescriptionTextField);
        language.nextStep();
        int iterationStep = 1;

        while (true) {
            highlightNode(currentNode, this.currentNodeHighlightColor);
            // currentNodeValue.setText(graph.getNodeLabel(currentNode),null,
            // null);
            src.highlight(2);
            src.unhighlight(1);
            src.unhighlight(12);
            // neuer whileschleifen durchlauf
            showStepDescription("Beginn der Whileschleife.", stepDescriptionTextField);
            language.nextStep("Schleifendurchlauf " + iterationStep);
            ArrayList<Integer> neighbours = getNeighbours(currentNode);
            for (int x : neighbours) {
                highlightNode(x, this.nodesToCheckHighlightColor);
            }
            String neighbourString = getNeighboursString(neighbours);
            LValue.setText(neighbourString, null, null);
            vars.set("L", neighbourString);
            src.unhighlight(2);
            src.highlight(3);
            // Liste von Nachbarn anlegen
            showStepDescription("Lege Liste von Nachbarn des currentNode an.", stepDescriptionTextField);
            if (nextNQu && showQuestion()) {
                String a = graph.getNodeLabel(neighbours.get(0));
                String b = graph.getNodeLabel(currentNode);
                MultipleChoiceQuestionModel nextNodeQuestion = new MultipleChoiceQuestionModel("xxx");
                nextNodeQuestion.setPrompt("Welcher Knoten wird als erstes überprueft?");
                nextNodeQuestion.addAnswer(a, 1, "Richtig");
                nextNodeQuestion.addAnswer(b, 0, "Falsch");
                for (Integer i : neighbours.subList(1, neighbours.size())) {
                    nextNodeQuestion.addAnswer(graph.getNodeLabel(i), 0, "Falsch");
                }
                language.addMCQuestion(nextNodeQuestion);
                nextNQu = false;
            }

            language.nextStep();
            src.unhighlight(3);
            src.highlight(4);
            int nextValue = -1;
            nextEvalValue.setText("-1", null, null);
            vars.set("nextEval", "-1");

            // Setze den Startwert von nextEval
            showStepDescription("Setze den initialen Wert von nextEval, da noch kein Nachbar besucht wurde und es einen Startwert zum vergleichen geben muss.", stepDescriptionTextField);
            language.nextStep();

            src.unhighlight(4);
            src.highlight(5);
            int nextNode = -1;
            nextNodeValue.setText("NULL", null, null);
            vars.set("nextNode", "NULL");
            // Setze den Startwert von nextNodeValue
            showStepDescription("Setze den initialen Wert von nextNode, da noch kein Nachbar besucht wurde und es einen Startwert geben muss.", stepDescriptionTextField);

            language.nextStep();
            for (int x : neighbours) {
                String nodeLabel = graph.getNodeLabel(x);
                vars.set("x", nodeLabel);
                xValue.setText(nodeLabel, null, null);
                highlightNode(x, this.xNodeHighlightColor);
                showStepDescription("Betrachte Knoten " + graph.getNodeLabel(x) + ".", stepDescriptionTextField);

                src.unhighlight(1);
                src.unhighlight(2);
                src.unhighlight(3);
                src.unhighlight(4);
                src.unhighlight(5);
                src.unhighlight(7);
                src.unhighlight(8);
                src.unhighlight(9);
                src.highlight(6);
                // Betrachte Node x
                language.nextStep();
                src.unhighlight(6);
                src.highlight(7);

                showStepDescription("Schaue ob der Nachbar eine bessere Loesung ist, als die bis jetzt betrachteten Nachbarn.", stepDescriptionTextField);
                language.nextStep();

                if (Integer.parseInt(graph.getNodeLabel(x).replace(" ", "")) > nextValue) {
                    nextNode = x;
                    String nextEval = graph.getNodeLabel(nextNode);
                    nextEvalValue.setText(nextEval, null, null);
                    vars.set("nextEval", nextEval);
                    src.unhighlight(7);
                    src.highlight(8);
                    // x ist neues Maximum

                    showStepDescription("Groesseren Nachbar gefunden.", stepDescriptionTextField);
                    language.nextStep();

                    src.unhighlight(8);
                    src.highlight(9);
                    nextValue = Integer.parseInt(graph.getNodeLabel(x).replaceAll(" ", ""));
                    nextNodeValue.setText(graph.getNodeLabel(nextNode), null, null);
                    vars.set("nextNode", graph.getNodeLabel(nextNode));
                    // setze neues maximum

                    showStepDescription("Setze x als Kandidaten fuer den naechsten currentNode.", stepDescriptionTextField);
                    language.nextStep();

                    unhighlightNode(x);
                } else {
                    src.unhighlight(7);
                    unhighlightNode(x);
                    // x ist nicht neues Maximum
                }
            }
            src.unhighlight(8);
            src.unhighlight(9);
            src.highlight(10);
            showStepDescription("Schaue ob lokales Maximum gefunden wurde.", stepDescriptionTextField);
            language.nextStep();
            if (nextValue <= Integer.parseInt(graph.getNodeLabel(currentNode).replaceAll(" ", ""))) {
                if (showQuestion()) {
                    MultipleChoiceQuestionModel maxQuestion = new MultipleChoiceQuestionModel("maxQuestion");

                    maxQuestion.setPrompt("Wurde das globale Maximum gefunden?");
                    maxQuestion.addAnswer(String.valueOf(isGlobalMax(graph,
                                    Integer.parseInt(graph.getNodeLabel(currentNode)))), 1,
                            "Das ist korrekt");
                    maxQuestion.addAnswer(String.valueOf(!isGlobalMax(graph,
                                    Integer.parseInt(graph.getNodeLabel(currentNode)))), 0,
                            "Das ist leider falsch.");
                    language.addMCQuestion(maxQuestion);
                }
                src.unhighlight(10);
                src.highlight(11);
                showStepDescription("Gebe lokales Maximum zurueck.", stepDescriptionTextField);
                break;
            }
            src.unhighlight(10);
            unhighlightNode(currentNode);
            for (int x : neighbours) {
                unhighlightNode(x);
            }
            currentNode = nextNode;
            highlightNode(currentNode, this.currentNodeHighlightColor);
            currentNodeValue.setText(graph.getNodeLabel(currentNode), null, null);
            vars.set("currentNode", graph.getNodeLabel(currentNode));
            src.unhighlight(8);
            src.unhighlight(9);
            src.unhighlight(11);
            src.highlight(12);
            // gehe zum anfang der whileschleife
            iterationStep++;
            showStepDescription("Setze currentNode auf den groessten Nachbarn des alten currentNode.", stepDescriptionTextField);
            language.nextStep();
        }
        language.nextStep("Algorithmus terminiert");
        showStepDescription("Algorithmus terminiert.", stepDescriptionTextField);
        for (int i = 0; i < graph.getSize(); i++) {
            unhighlightNode(i);
            circleList.get(i).hide();
        }
        graph.hide();
        // Algorithmus terminiert
    }

    public void showLegend() {
        language.newText(
                new Offset(380, 0, "variableStateLines1", AnimalScript.DIRECTION_W), "Legende:",
                "legendLines1", null, textProps);
        RectProperties recProps = new RectProperties();
        recProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        recProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        recProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        language.newRect(new Offset(-5, -5, "legendLines1",
                AnimalScript.DIRECTION_NW), new Offset(5, 5, "legendLines1",
                AnimalScript.DIRECTION_SE), "legRec", null, recProps);
        language.newText(new Offset(0, 30, "legendLines1",
                        AnimalScript.DIRECTION_NW), "currentNode", "legendLines2",
                null, textProps);
        language.newText(new Offset(0, 20, "legendLines2",
                        AnimalScript.DIRECTION_NW), "Nodes to check", "legendLines3",
                null, textProps);
        language.newText(new Offset(0, 20, "legendLines3",
                        AnimalScript.DIRECTION_NW), "Node x", "legendLines4", null,
                textProps);
        Node[] squareNodes = new Node[4];
        squareNodes[0] = new Offset(200, 30,
                "legendLines1", AnimalScript.DIRECTION_NW);
        squareNodes[1] = new Offset(200, 50,
                "legendLines1", AnimalScript.DIRECTION_NW);
        squareNodes[2] = new Offset(200, 70,
                "legendLines1", AnimalScript.DIRECTION_NW);

        SquareProperties currentNodeLegendProps = new SquareProperties();
        currentNodeLegendProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

        currentNodeLegendProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
                currentNodeHighlightColor
                        .get(AnimationPropertiesKeys.FILL_PROPERTY));
        language.newSquare(squareNodes[0], 15, "", null, currentNodeLegendProps);
        SquareProperties nodesToCheckLegendProps = new SquareProperties();
        nodesToCheckLegendProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        nodesToCheckLegendProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
                nodesToCheckHighlightColor.get(AnimationPropertiesKeys.FILL_PROPERTY));
        language.newSquare(squareNodes[1], 15, "", null, nodesToCheckLegendProps);
        SquareProperties xNodeLegendProps = new SquareProperties();
        xNodeLegendProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        xNodeLegendProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
                xNodeHighlightColor.get(AnimationPropertiesKeys.FILL_PROPERTY));
        language.newSquare(squareNodes[2], 15, "", null, xNodeLegendProps);

    }

    public void showVariableState() {
        language.newText(new Offset(0, 350,
                        "sourceCode", AnimalScript.DIRECTION_NW), "Variablenstatus:",
                "variableStateLines1", null, textProps);
        RectProperties recProps = new RectProperties();
        recProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        recProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        recProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        language.newRect(new Offset(-5, -5,
                        "variableStateLines1", AnimalScript.DIRECTION_NW), new Offset(
                        5, 5, "variableStateLines1", AnimalScript.DIRECTION_SE),
                "varRec", null, recProps);
        language.newText(new Offset(0, 30,
                        "variableStateLines1", AnimalScript.DIRECTION_NW),
                "currentNode:", "variableStateLines2", null, textProps);
        language.newText(new Offset(0, 20,
                        "variableStateLines2", AnimalScript.DIRECTION_NW), "L:",
                "variableStateLines3", null, textProps);
        language.newText(new Offset(0, 20,
                        "variableStateLines3", AnimalScript.DIRECTION_NW), "nextEval:",
                "variableStateLines4", null, textProps);
        language.newText(new Offset(0, 20,
                        "variableStateLines4", AnimalScript.DIRECTION_NW), "nextNode:",
                "variableStateLines5", null, textProps);
        language.newText(new Offset(0, 20,
                        "variableStateLines5", AnimalScript.DIRECTION_NW), "x:",
                "variableStateLines6", null, textProps);
    }

    /*
     * Abstrakte Methodenimplementationen von Generator
     */
    public void init() {
        language = new AnimalScript("Hillclimbing [DE]",
                "Andre Pacak, Marc Semmler", 800, 600);
        language.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,
                           Hashtable<String, Object> primitives) {
        language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        currentNodeHighlightColor = (CircleProperties) props.getPropertiesByName("currentNodeHighlightColor");
        xNodeHighlightColor = (CircleProperties) props.getPropertiesByName("xNodeHighlightColor");
        nodesToCheckHighlightColor = (CircleProperties) props.getPropertiesByName("nodesToCheckHighlightColor");
        nodeValues = (int[]) primitives.get("nodeValues");
        coordinates = (int[][]) primitives.get("coordinates");
        sourceCodeHighlightColor = (SourceCodeProperties) props.getPropertiesByName("sourceCodeHighlightColor");
        adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
        startNode = (Integer) primitives.get("startNode");
        probOfQuestions = (Integer) primitives.get("percentageOfProbQuestion");
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        GraphProperties graphProps = new GraphProperties();
        graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        graphProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        String[] labels = new String[nodeValues.length];
        Node[] graphNodes = new Node[coordinates.length];
        int numberOfDigits = getMaxNumberOfDigits();
        for (int i = 0; i < labels.length; i++) {
            labels[i] = String.format("%0" + numberOfDigits + "d", nodeValues[i]);
            graphNodes[i] = new Coordinates(coordinates[i][0], coordinates[i][1]);
        }
        graph = language.newGraph("graph", adjacencyMatrix, graphNodes, labels, null, graphProps);
        graph.hide();
        this.circleList = convertGraphToCircles(numberOfDigits);
        this.showHeader();
        this.showDescription();
        this.showAlgorithm();
        this.showConclusion();

        language.finalizeGeneration();
        return language.toString();
    }

    private int getMaxNumberOfDigits() {
        int[] copyOfNodeValues = Arrays.copyOf(nodeValues, nodeValues.length);
        Arrays.sort(copyOfNodeValues);
        int maxElement = nodeValues[nodeValues.length - 1];
        return (int) Math.log10(maxElement) + 1;
    }

    public String getName() {
        return "Hillclimbing [DE]";
    }

    public String getAlgorithmName() {
        return "Hillclimbing";
    }

    public String getAnimationAuthor() {
        return "Andre Pacak, Marc Semmler";
    }

    public String getDescription() {
        return "Der Hill Climbing Algorithmus ist ein heuristisches Optimierungsverfahren. Wie der Name schon nahelegt,"
                + "wird sich einer potenziellen Loesung Schritt fuer Schritt angenaehert (wie bei einem Bergsteiger, der einen Berg erklimmt)."
                + "Dabei sucht der Algorithmus in jedem Schritt nach der besten lokalen Veraenderung. Somit naehert er"
                + "sich durch jede Iteration an ein lokales Maximum, also eine potentielle Loesung, an. "
                + "Der Algorithmus bricht ab wenn er keine lokale Verbesserung mehr finden kann. Dadurch ist er nicht "
                + "immer in der Lage das globale Maximum (Bergspitze) zu finden."
                + "Das finden einer Loesung ist stark abhaengig vom Startpunkt und der vorgegebenen Heuristik des Verfahrens.";
    }

    public String getCodeExample() {
        return "hillclimbing(startNode)" + "\n" + "    currentNode = startNode"
                + "\n" + "    loop do" + "\n"
                + "        L=NEIGHBORS(currentNode)" + "\n"
                + "                nextEval = -1" + "\n"
                + "                nextNode = NULL" + "\n"
                + "                for all x in L" + "\n"
                + "                    if(EVAL(x)>nextEval)" + "\n"
                + "                        nextEval = EVAL(x)" + "\n"
                + "                        nextNode = x" + "\n"
                + "                    if(nextEval <= EVAL(currentNode))"
                + "\n" + "                        return currentNode" + "\n"
                + "                        currentNode = nextNode";
    }

    private void showErrorWindow(String message) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message,
                "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props,
                                 Hashtable<String, Object> primitives)
            throws IllegalArgumentException {
        int[] nodeValues = (int[]) primitives.get("nodeValues");
        int[][] coordinates = (int[][]) primitives.get("coordinates");
        int[][] adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
        int startNode = (Integer) primitives.get("startNode");
        int probOfQuestion = (Integer) primitives.get("percentageOfProbQuestion");
        boolean error = false;
        StringBuilder errorMessage = new StringBuilder("");
        if (startNode < 0 || startNode >= nodeValues.length) {
            errorMessage.append("Der startNode Wert auserhalb der Indizes!\n");
            error = true;
        }
        if (nodeValues.length != coordinates.length || adjacencyMatrix.length != nodeValues.length) {
            errorMessage
                    .append("Die Anzahl der Werte von nodeValues, coordinates und adjacencyMatrix stimmen nicht ueberein!\n");
            error = true;
        }
        if (adjacencyMatrix.length != adjacencyMatrix[0].length) {
            errorMessage.append("Die adjacencyMatrix ist nicht quadratisch!\n");
            error = true;
        }
        if (coordinates[0].length < 2) {
            errorMessage.append("Coordinates hat nicht mindestens zwei Spalten!\n");
            error = true;
        }
        if (probOfQuestion < 0 || probOfQuestion > 100) {
            errorMessage.append("Wahrscheinlichkeit der Fragen muss mindestends 0 und hoechsten 100 sein");
            error = true;
        }
        if (error) {
            showErrorWindow(errorMessage.toString());
        }
        return !error;
    }
}
