package generators.graph;
/*
 * pav_ssp.java
 * Balint Klement, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import algoanim.primitives.Graph;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
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
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class SuccessiveShortestPaths implements ValidatingGenerator {
    private Language l;

    // for extracted graph painting
    private int[][] arc_capacities;
    private TextProperties arcTextProperties;
    private GraphProperties graphProperties;
    private Graph graph;
    private List<Primitive> notHide;

    // for calculations
    private int graphSize;
    private GraphNode[] graphNodes;
    private GraphArc[] graphArcs;
    private List<Integer> nodesWithPosBalance = new ArrayList<>();

    public static void main(String[] args) {
        Generator generator = new SuccessiveShortestPaths(); // Generator erzeugen
        Animal.startGeneratorWindow(generator); // Animal mit Generator starten
    }

    public void init(){
        l = new AnimalScript("Successive Shortest Paths", "Balint Klement", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arc_capacities = (int[][])primitives.get("arc_capacities");
        graphSize = (Integer)primitives.get("number_nodes");
        int[][] arc_costs = (int[][])primitives.get("arc_costs");
        int[] initial_balances = (int[])primitives.get("initial_balances");

        l.setStepMode(true);
        l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        // 1. Step: show title and description
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
        Primitive header = l.newText(new Coordinates(20, 30), "Successive Shortest Paths", "header", null, headerProps);
        Primitive hRect = l.newRect(new Offset(-5, -5, "header", "NW"),
                new Offset(5, 5, "header", "SE"), "hRect", null);

        SourceCodeProperties descriptionProps = new SourceCodeProperties();
        descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        SourceCode description = l.newSourceCode(new Coordinates(10, 100), "description", null, descriptionProps);
        description.addCodeLine("Successive shortest paths is an algorithm to solve the min-cost flow problem.", null, 0, null);
        description.addCodeLine("It successively picks nodes with positive balance (a surplus of flow), ", null, 0, null);
        description.addCodeLine("and tries to redirect flow on the shortest path to nodes with negative balance.", null, 0, null);
        description.addCodeLine("", null, 0, null);
        description.addCodeLine("Min-cost flow tasks are not always solvable - the algorithm notices infeasibility during runtime.", null, 0, null);

        l.nextStep("Introduction");


        // 2. Step (partA): hide description and create and paint Graph
        notHide = new ArrayList<>();
        notHide.add(header);
        notHide.add(hRect);
        l.hideAllPrimitivesExcept(notHide);

        arcTextProperties = new TextProperties();
        arcTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 12));
        arcTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        arcTextProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

        graphProperties = new GraphProperties();
        graphProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.YELLOW);
        graphProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        graphProperties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);

        initializeGraph(initial_balances, arc_capacities, arc_costs);
        paintGraph();

        // 2. Step (partB): show and highlight PseudoCode and Information
        SourceCodeProperties pseudoCodeProps = new SourceCodeProperties();
        pseudoCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        pseudoCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        SourceCode pseudoCode = l.newSourceCode(new Offset(60, -10, "graph", "NE"), "pseudoCode",
                null, pseudoCodeProps);
        pseudoCode.addCodeLine("initialize", null, 0, null);
        pseudoCode.addCodeLine("while (there are nodes with pos. balance)", null, 0, null);
        pseudoCode.addCodeLine("pick a node with pos. balance, find shortest path to a node with neg. balance", null, 1, null);
        pseudoCode.addCodeLine("if there is no such path, terminate the algorithm: the task is infeasible", null, 2, null);
        pseudoCode.addCodeLine("increase flow on the path as much as possible", null, 1, null);
        pseudoCode.addCodeLine("terminate: the task was successfully completed", null, 0, null);
        TextProperties pseudoHeaderProps = new TextProperties();
        pseudoHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
        Text pseudoHeader = l.newText(new Offset(0, -20, "pseudoCode", "WE"), "PseudoCode",
                "pseudoHeader", null, pseudoHeaderProps);

        SourceCodeProperties informationProps = new SourceCodeProperties();
        informationProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        SourceCode information = l.newSourceCode(new Offset(0, 15, "pseudoCode", "SW"), "information",
                null, informationProps);
        information.addCodeLine("1. the current balance (= surplus of flow) of a node is written inside it", null, 0, null);
        information.addCodeLine("current balance = initial balance + incoming flow - outgoing flow", null, 1, null);
        information.addCodeLine("2. each arc has a text like 'x/y [z]' shown approximately in its middle", null, 0, null);
        information.addCodeLine("x: current flow; y: max. capacity; z: cost", null, 1, null);
        information.addCodeLine("3. the amount flow by which to increase is calculated the following way:", null, 0, null);
        information.addCodeLine("min. of {capacity along the path, surplus at start node, shortage at the start/end node ", null, 1, null);
        Text informationHeader = l.newText(new Offset(0, -20, "information", "NW"), "Additional Information", "informationHeader",
                null, pseudoHeaderProps);

        notHide.add(pseudoCode);
        notHide.add(pseudoHeader);
        notHide.add(information);
        notHide.add(informationHeader);

        pseudoCode.highlight(0);

        l.nextStep("Initialization");


        // 3
        pseudoCode.unhighlight(0);
        pseudoCode.highlight(1);
        nodesWithPosBalance.forEach(nodeID -> graph.highlightNode(nodeID, null, null));

        l.nextStep("Main-Loop");

        int summaryBalance = 0;
        int summarySteps = 0;

        while (!nodesWithPosBalance.isEmpty()) {
            pseudoCode.unhighlight(1);
            pseudoCode.highlight(2);
            nodesWithPosBalance.forEach(nodeID -> graph.unhighlightNode(nodeID, null, null));

            int startNodeID = nodesWithPosBalance.get(0);
            java.util.List<GraphArc> path = findShortestPathFromNode(startNodeID);
            if (path == null) {
                break;
            }
            summarySteps++;

            graph.highlightNode(startNodeID, null, null);
            path.forEach(arc -> graph.highlightEdge(arc.startID, arc.targetID, null, null));

            l.nextStep();


            pseudoCode.unhighlight(2);
            pseudoCode.highlight(4);
            int flowIncreasedBy = increaseFlowOnPath(path);
            summaryBalance += flowIncreasedBy;
            paintGraph();
            int targetNodeID = path.get(path.size() - 1).targetID;
            graph.highlightNode(startNodeID, null, null);
            graph.highlightNode(targetNodeID, null, null);
            path.forEach(arc -> {
                if (arc.reversed)
                    graph.highlightEdge(arc.targetID, arc.startID, null, null);
                else
                    graph.highlightEdge(arc.startID, arc.targetID, null, null);
            });

            FillInBlanksQuestionModel flowIncrease = new FillInBlanksQuestionModel("flowInc");
            flowIncrease.setPrompt("What amount of flow will be redirected through the highlighted path?");
            flowIncrease.addAnswer(String.valueOf(flowIncreasedBy), 2, "That's right! Look at point 3 of \"Additional Information\" for more.");
            flowIncrease.setGroupID("flowIncrease");
            flowIncrease.setNumberOfTries(2);
            l.addFIBQuestion(flowIncrease);

            l.nextStep();

            pseudoCode.unhighlight(4);
            pseudoCode.highlight(1);
            graph.unhighlightNode(startNodeID, null, null);
            graph.unhighlightNode(targetNodeID, null, null);
            path.forEach(arc -> {
                if (arc.reversed)
                    graph.unhighlightEdge(arc.targetID, arc.startID, null, null);
                else
                    graph.unhighlightEdge(arc.startID, arc.targetID, null, null);
            });
            nodesWithPosBalance.forEach(nodeID -> graph.highlightNode(nodeID, null, null));

            MultipleChoiceQuestionModel whatsShortestPath = new MultipleChoiceQuestionModel("whatsShortestPath");
            whatsShortestPath.setPrompt("What is considered the shortest Path in this Algorithm?");
            whatsShortestPath.addAnswer("Path with least edges", 0, "That's wrong. Try again!");
            whatsShortestPath.addAnswer("Path with minimal cost per flow", 3, "That's correct! As we have costs on the edges, \"short\" means with minimal cost");
            whatsShortestPath.setNumberOfTries(2);
            l.addMCQuestion(whatsShortestPath);


            l.nextStep();
        }

        pseudoCode.unhighlight(1);
        pseudoCode.unhighlight(2);
        notHide.remove(information);
        notHide.add(graph);
        l.hideAllPrimitivesExcept(notHide);
        paintGraph();
        SourceCode summary = l.newSourceCode(new Offset(0, 15, "pseudoCode", "SW"), "summary",
                null, informationProps);
        informationHeader.setText("Summary", null, null);

        int summaryCost = Arrays.stream(graphArcs).filter(arc -> !arc.reversed).mapToInt(arc -> arc.flow * arc.cost).sum();

        // termination can occur in two cases
        if (nodesWithPosBalance.isEmpty()) {
            // there are no more nodes with balance != 0
            pseudoCode.highlight(5);
            summary.addCodeLine("FINISHED! There are no more imbalanced nodes.", null, 0, null);
            summary.addCodeLine("Number of bellman-ford algorithms (single-source shortest paths) conducted: " + summarySteps, null, 0, null);
            summary.addCodeLine("A total of " + summaryBalance + " flow was redirected", null, 0, null);
            summary.addCodeLine("The total cost of the solution is: " + summaryCost, null, 0, null);
        } else {
            // there are nodes with pos balance with no path to nodes with neg balance
            pseudoCode.highlight(3);
            summary.addCodeLine("ABORTED! There exists no solution to this task.", null, 0, null);
            summary.addCodeLine("Number of bellman-ford algorithms (single-source shortest paths) conducted until abortion: " + summarySteps, null, 0, null);
            summary.addCodeLine("Until abortion, a total of " + summaryBalance + " flow was redirected.", null, 0, null);
            summary.addCodeLine("The total cost of the flow so far redirected is: " + summaryCost, null, 0, null);
        }


        l.nextStep("Summary");

        l.finalizeGeneration();
        return l.toString();
    }

    private void initializeGraph(int[] balances, int[][] capacities, int[][] costs) {
        graphSize = balances.length;
        graphNodes = new GraphNode[graphSize];
        java.util.List<GraphArc> arcs = new ArrayList<>();
        for (int i = 0; i < graphSize; i++) {
            graphNodes[i] = new GraphNode(i, getNodeCoordinateValue(i, graphSize), balances[i]);
            if (balances[i] > 0)
                nodesWithPosBalance.add(i);
            for (int j = 0; j < graphSize; j++) {
                if (capacities[i][j] > 0) {
                    GraphArc arc = new GraphArc(i, j, capacities[i][j], costs[i][j], 0, false);
                    GraphArc reverseArc = new GraphArc(j, i, capacities[i][j], -costs[i][j], capacities[i][j], true);
                    arc.partner = reverseArc;
                    reverseArc.partner = arc;
                    arcs.add(arc);
                    arcs.add(reverseArc);
                }
            }
        }
        graphArcs = arcs.toArray(new GraphArc[0]);
    }

    private void paintGraph() {
        l.hideAllPrimitivesExcept(notHide);
        String[] nodeNames = new String[graphSize];
        for (int i = 0; i < graphSize; i++) {
            nodeNames[i] = String.valueOf(graphNodes[i].balance);
        }
        for (GraphArc arc : graphArcs) {
            if (!arc.reversed) {
                Coordinates startCoords = getNodeCoordinateValue(arc.startID, graphSize);
                Coordinates endCoords = getNodeCoordinateValue(arc.targetID, graphSize);
                Coordinates arcTextCoords = new Coordinates((startCoords.getX() + endCoords.getX()) / 2, (startCoords.getY() + endCoords.getY()) / 2);
                l.newText(arcTextCoords, String.format("%s/%s [%s]", arc.flow, arc.capacity, arc.cost), "", null, arcTextProperties);
            }

        }
        graph = l.newGraph("graph", arc_capacities, graphNodes, nodeNames, null, graphProperties);
    }

    private Coordinates getNodeCoordinateValue(int id, int graphSize) {
        switch (graphSize) {
            case 5:
                switch (id) {
                    case 0:
                        return new Coordinates(40, 180);
                    case 1:
                        return new Coordinates(140, 100);
                    case 2:
                        return new Coordinates(140, 260);
                    case 3:
                        return new Coordinates(240, 130);
                    case 4:
                        return new Coordinates(240, 230);
                }
            case 6:
                switch (id) {
                    case 0: return new Coordinates(40, 130);
                    case 1: return new Coordinates(40, 230);
                    case 2: return new Coordinates(140, 100);
                    case 3: return new Coordinates(140, 260);
                    case 4: return new Coordinates(240, 150);
                    case 5: return new Coordinates(240, 210);
                }
            case 7:
                switch (id) {
                    case 0: return new Coordinates(40, 130);
                    case 1: return new Coordinates(40, 230);
                    case 2: return new Coordinates(140, 100);
                    case 3: return new Coordinates(140, 260);
                    case 4: return new Coordinates(240, 100);
                    case 5: return new Coordinates(240, 260);
                    case 6: return new Coordinates(240, 180);
                }

            default:
                System.out.println("error in getNodeCoordinateValue! number of nodes not supported!");
                return new Coordinates(0, 0);
        }
    }

    private java.util.List<GraphArc> findShortestPathFromNode(int pathStartID) {
        // create and initialize arrays
        int[] distance = new int[graphSize];
        GraphArc[] predecessor = new GraphArc[graphSize];

        Arrays.fill(distance, Integer.MAX_VALUE / 2);
        distance[pathStartID] = 0;
        Arrays.fill(predecessor, null);

        // perform the Bellman-Ford algorithm
        for (int k = 0; k < graphSize; k++) {
            for (GraphArc arc : graphArcs) {
                if ((distance[arc.startID] + arc.cost < distance[arc.targetID]) && (arc.capacity - arc.flow > 0)) {
                    distance[arc.targetID] = distance[arc.startID] + arc.cost;
                    predecessor[arc.targetID] = arc;
                }
            }
        }

        // find the node with the shortest path and a neg balance
        int pathTargetID = Integer.MAX_VALUE;
        int targetDistance = Integer.MAX_VALUE / 2;
        for (int i = 0; i < graphSize; i++) {
            if ((graphNodes[i].balance < 0) && (distance[i] < targetDistance)) {
                pathTargetID = i;
                targetDistance = distance[i];
            }
        }
        if (pathTargetID == Integer.MAX_VALUE) return null;

        // recreate the path from the predecessors-array
        java.util.List<GraphArc> path = new ArrayList<>();
        path.add(predecessor[pathTargetID]);
        while (path.get(0).startID != pathStartID) {
            path.add(0, predecessor[path.get(0).startID]);
        }

        return path;
    }

    private int increaseFlowOnPath(List<GraphArc> path) {
        int minCapacityOnPath = path.stream().map(arc -> arc.capacity).min(Comparator.comparing(Integer::valueOf)).get();
        int startNodeID = path.get(0).startID;
        int targetNodeID = path.get(path.size() - 1).targetID;

        int increaseBy = Math.min(minCapacityOnPath, Math.min(graphNodes[startNodeID].balance,
                -graphNodes[targetNodeID].balance));
        graphNodes[startNodeID].balance -= increaseBy;
        graphNodes[targetNodeID].balance += increaseBy;
        for (GraphArc arc : path) {
            arc.flow += increaseBy;
            arc.partner.flow -= increaseBy;
        }

        if (graphNodes[startNodeID].balance == 0)
            nodesWithPosBalance.remove(0);

        return increaseBy;
    }

    public String getName() {
        return "Successive Shortest Paths";
    }

    public String getAlgorithmName() {
        return "Min-cost flow problem";
    }

    public String getAnimationAuthor() {
        return "Balint Klement";
    }

    public String getDescription(){
        return "Successive shortest paths is an algorithm to solve the min-cost flow problem."
 +"\n"
 +"It successively picks nodes with positive balance (a surplus of flow),"
 +"\n"
 +"and tries to redirect flow on the shortest path to nodes with negative balance."
 +"\n"
 +"\n"
 +"Min-cost flow tasks are not always solvable - the algorithm notices infeasibility during runtime.";
    }

    public String getCodeExample(){
        return "initialize"
 +"\n"
 +"while (there are nodes with pos. balance)"
 +"\n"
 +"    pick a node with pos. balance, find shortest path to a node with neg. balance"
 +"\n"
 +"        if there is no such path, terminate the algorithm: the task is infeasible"
 +"\n"
 +"    increase flow on the path as much as possible"
 +"\n"
 +"terminate: the task was successfully completed";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        int graphSize = (Integer)primitives.get("number_nodes");
        int[] initial_balances = (int[])primitives.get("initial_balances");
        int[][] arc_capacities = (int[][])primitives.get("arc_capacities");
        int[][] arc_costs = (int[][])primitives.get("arc_costs");

        if ((graphSize < 5) || (graphSize > 7)) return false;
        if (initial_balances.length != graphSize) return false;
        if ((arc_capacities.length != graphSize) || Arrays.stream(arc_capacities).anyMatch(row -> row.length != graphSize)) return false;
        if ((arc_costs.length != graphSize) || Arrays.stream(arc_costs).anyMatch(row -> row.length != graphSize)) return false;

        for (int i = 0; i < graphSize; i++) {
            for (int j = 0; j < graphSize; j++) {
                if ((arc_capacities[i][j] < 0) || (arc_costs[i][j] < 0)) return false;
            }
        }

        return true;
    }
}
