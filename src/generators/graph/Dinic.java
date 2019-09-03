package generators.graph;
/*
 * Dinic.java
 * Amer Shafa, Ahmed Zukic, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;

import algoanim.primitives.generators.Language;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

@SuppressWarnings("unused")
public class Dinic implements ValidatingGenerator {
    private Language lang;
    private int[][] arcs;
    private int[][] nodePositions;

    public void init(){
        lang = new AnimalScript("Dinic - Maximum Flow Problem", "Amer Shafa, Ahmed Zukic", 800, 600);
    }

    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arcs = (int[][])primitives.get("intEdges");
        nodePositions = (int[][])primitives.get("intNodePositions");
        // no negative numbers
         for (int i = 0; i < arcs.length; i++) {
             for (int j = 0; j < 3; j++)
                 if (arcs[i][j] < 0) return false;
         }
         // no overlay nodes
        ArrayList<HashMap<Integer, Boolean>> positions = new ArrayList<>();
        for (int i = 0; i < nodePositions.length; i++) {
            positions.add(new HashMap<>());
        }
        for (int i = 0; i < nodePositions.length; i ++) {
             // only positions >= 1
             if (nodePositions[i][1] < 1 || nodePositions[i][2] < 1) return false;
             // already a node on same position?
             if (positions.get(nodePositions[i][1]).get(nodePositions[i][2]) != null) return false;
             // add position
             else positions.get(nodePositions[i][1]).put(nodePositions[i][2], true);
             // input node positions in order, starting with source node 0
             if (nodePositions[i][0] != i) return false;
        }
        return true;
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        if (!validateInput(props, primitives)) throw new IllegalArgumentException();

        arcs = (int[][])primitives.get("intEdges");
        nodePositions = (int[][])primitives.get("intNodePositions");

        // get all nodes
        int[] allNodesInt = getNodesAsIntArray(nodePositions);

        // int[] allNodes = new int[arcs.length];
        // Erzeugen einer Instanz der Basis-Visualisierungsklasse für AnimalScript
        Language lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Ahuja-Orlin", "Amer Shafa, Ahmed Zukic", 1000, 680);
        lang.setStepMode(true); // aktivieren des Schrittmodus

        /*
            TITLE
        */
        TextProperties titleProps = new TextProperties();
        titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.decode("#155724"));
        titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        titleProps.set("depth", 1);
        Text title = lang.newText(new Coordinates(50, 30), "Dinic - Maximum Flow Problem", "title", null, titleProps);
        RectProperties rectProps = new RectProperties();
        rectProps.set("fillColor", Color.decode("#d4edda"));
        rectProps.set("filled", true);
        rectProps.set("depth", 2);
        Offset rectNW = new Offset(-5, -5, title, AnimalScript.DIRECTION_NW);
        Offset rectSE = new Offset(5, 5, title, AnimalScript.DIRECTION_SE);
        Rect titleBG = lang.newRect(rectNW, rectSE, "titleBG", null, rectProps);

        /*
            INTRODUCTION
         */
        TextProperties apIntro = new TextProperties();
        apIntro.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        Text intro1 = lang.newText(new Coordinates(50, 100), "This algorithm solves the maximum flow problem.", "intro1", null, apIntro);
        Text intro2 = lang.newText(new Coordinates(50, 125), "We will not explain this problem, you can find many informations", "intro2", null, apIntro);
        Text intro3 = lang.newText(new Coordinates(50, 150), "about this online. We show you how the algorithm of Dinic works", "intro3", null, apIntro);
        Text intro4 = lang.newText(new Coordinates(50, 175), "to solve this problem.", "intro4", null, apIntro);

        lang.nextStep();

        intro1.hide();
        intro2.hide();
        intro3.hide();
        intro4.hide();

        Text tipHeader = lang.newText(new Coordinates(50, 100), "Important Notes", "tipHeader", null, apIntro);
        tipHeader.changeColor("color", Color.RED, null, null);
        Text tip0 = lang.newText(new Coordinates(50, 120), "Dinic works in a simple way. In each step we are looking for all shortest paths and create then a blocking flow.", "tip0", null, apIntro);
        Text tip1 = lang.newText(new Coordinates(50, 140), "Important to note is: the length of the next shortest path strictly increases.", "tip1", null, apIntro);
        Text tip2 = lang.newText(new Coordinates(50, 160), "There are several blocking flow algorithms you can choose. In this case we use the", "tip2", null, apIntro);
        Text tip3 = lang.newText(new Coordinates(50, 180), "blocking flow algorithm of Dinic, but we will not cover this part in detail.", "tip3", null, apIntro);

        Text tip7 = lang.newText(new Coordinates(50, 350), "More detailed definition on: https://wiki.algo.informatik.tu-darmstadt.de/Dinic", "tip7", null, apIntro);

        lang.nextStep();

        tipHeader.hide();
        tip0.hide();
        tip1.hide();
        tip2.hide();
        tip3.hide();

        tip7.hide();

        /*
            GRAPH
         */
        GraphProperties graphProps = new GraphProperties();
        graphProps.set("color", Color.WHITE);
        graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.decode("#d4edda"));
        graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.decode("#155724"));
        graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("#F8F9FA"));
        graphProps.set("directed", true);
        graphProps.set("weighted", true);
        int weights[][] = getAdjacenceMatrixDirected(arcs, allNodesInt);

        Coordinates nodes[] = new Coordinates[nodePositions.length];
        for (int i = 0; i < nodePositions.length; i++) {
            int x = 50 + ((nodePositions[i][1]-1) * 200);
            int y = 150 + ((nodePositions[i][2]-1) * 200);
            nodes[i] = new Coordinates(x, y);
        }

        String nodeLabels[] = new String[allNodesInt.length];
        /*
            INIT NODE LABELS
         */
        for (int i = 0; i < allNodesInt.length; i++) {
            if (i == 0) nodeLabels[i] = "S";
            else if (i == allNodesInt.length-1) nodeLabels[i] = "T";
            else nodeLabels[i] = String.valueOf(i);
        }
        Graph graph = lang.newGraph("Graph", weights, nodes, nodeLabels, null, graphProps);

        graph.setStartNode(graph.getNode(0));
        graph.setTargetNode(graph.getNode(allNodesInt.length-1));

        /*
            SET EDGE WEIGHT
         */
        for (int i = 0; i < arcs.length; i++)
            graph.setEdgeWeight(arcs[i][0], arcs[i][1], "0/" + String.valueOf(arcs[i][2]), null, null);

        graph.hide();

        /*
            LAYERED GRAPH
         */
        GraphProperties residualGraphProps = new GraphProperties();
        residualGraphProps.set("color", Color.WHITE);
        residualGraphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.decode("#d4edda"));
        residualGraphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.decode("#856404"));
        residualGraphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("#F8F9FA"));
        residualGraphProps.set("weighted", true);
        residualGraphProps.set("directed", false);

        int weightsUndir[][] = getAdjacenceMatrixUndirected(arcs, allNodesInt);
        Graph rGraph = lang.newGraph("residualGraph", weights, nodes, nodeLabels, null, residualGraphProps);

        rGraph.setStartNode(rGraph.getNode(0));
        rGraph.setTargetNode(rGraph.getNode(allNodesInt.length-1));

        /*
            SET EDGE WEIGHT
         */
        for (int i = 0; i < arcs.length; i++)
            rGraph.setEdgeWeight(arcs[i][0], arcs[i][1], "0|" + String.valueOf(arcs[i][2]), null, null);

        rGraph.hide();

        /*
            PSEUDO CODE
         */
        SourceCodeProperties apSourceCode = new SourceCodeProperties();
        apSourceCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        SourceCode sourceCode = lang.newSourceCode(new Offset(80, 5, graph, AnimalScript.DIRECTION_NE), "code", null, apSourceCode);
        sourceCode.addCodeLine("while (true) {", "code", 0, null);
        sourceCode.addCodeLine("Graph layeredGraph = findAllShortestPaths(graph)", "code", 1, null);
        sourceCode.addCodeLine("if (hasNoPath(layeredGraph))", "code", 1, null);
        sourceCode.addCodeLine("terminate", "code", 2, null);
        sourceCode.addCodeLine("forEach (path in layerdGraph) {", "code", 1, null);
        sourceCode.addCodeLine("findBlockingFlow(path)", "code", 2, null);
        sourceCode.addCodeLine("augment(path)", "code", 2, null);
        sourceCode.addCodeLine("}", "code", 1, null);
        sourceCode.addCodeLine("}", "code", 0, null);


        /*

            ALGORITHM -> Dinic

         */
        LinkedList<Integer> Path = new LinkedList<>();

        ArrayList<HashMap<Integer, Integer>> Flow = new ArrayList<>();
        // Init Zero Flow
        for (int i = 0; i < allNodesInt.length; i++) {
            Flow.add(new HashMap<>(allNodesInt.length));
            for (int j = 0; j < allNodesInt.length; j++)
                Flow.get(i).put(j, 0);
        }

        while (true)
        {
            graph.show();
            rGraph.hide();
            sourceCode.unhighlight(6);
            sourceCode.highlight(0);
            lang.nextStep("next iteration");

            graph.hide();
            rGraph.show();
            // show all edges
            for (int i = 0; i < weights.length; i++)
                for (int j = 0; j < weights.length; j++)
                    if (weights[i][j] != 0) {
                        if (i < j) rGraph.showEdge(i, j, null, null);
                        else rGraph.showEdge(j, i, null, null);
                    }
            lang.nextStep();

            LinkedList<LinkedList<Integer>> shortestPaths = findAllShortestPaths(weights, Flow);

            int [][] cpWeights = getAdjacenceMatrixDirected(arcs, allNodesInt);
            // show all nodes and edges on shortest path
            for (LinkedList<Integer> path : shortestPaths) {
                Integer first = path.getFirst();
                for (Integer second : path) {
                    rGraph.showNode(second, null, null);
                    if (first == second) continue;
                    if (first < second) {
                        rGraph.showEdge(first, second, null, null);
                        cpWeights[first][second] = 0;
                    }
                    else {
                        rGraph.showEdge(second, first, null, null);
                        cpWeights[second][first] = 0;
                    }
                    first = second;
                }
            }

            // hide irrelevant edges
            for (int i = 0; i < weights.length; i++)
                for (int j = 0; j < weights.length; j++)
                    if (cpWeights[i][j] != 0) {
                        if (i < j) rGraph.hideEdge(i, j, null, null);
                        else rGraph.hideEdge(j, i, null, null);
                    }


            sourceCode.unhighlight(0);
            sourceCode.unhighlight(2);
            sourceCode.unhighlight(4);
            sourceCode.highlight(1);
            lang.nextStep();

            sourceCode.unhighlight(1);
            sourceCode.highlight(2);
            lang.nextStep();
            if (shortestPaths.size() == 0) {
                sourceCode.unhighlight(2);
                sourceCode.highlight(3);
                lang.nextStep();
                break; // no path found
            } else {
                sourceCode.unhighlight(2);
                sourceCode.highlight(4);
                lang.nextStep();
                // AUGMENTING each path: Dinic Blocking Flow
                for (LinkedList<Integer> path : shortestPaths) {
                    sourceCode.unhighlight(2);
                    sourceCode.unhighlight(4);
                    sourceCode.unhighlight(6);
                    sourceCode.highlight(5);
                    Integer first = path.getFirst();
                    for (Integer second : path) {
                        rGraph.highlightNode(second, null, null);
                        if (first == second) continue;
                        rGraph.highlightEdge(first, second, null, null);
                        first = second;
                    }
                    lang.nextStep("findBlockingFlow");

                    // augment
                    augment(path, weights, Flow, graph, rGraph);
                    sourceCode.unhighlight(5);
                    sourceCode.highlight(6);
                    lang.nextStep();
                    // unhighlight again
                    for (Integer second : path) {
                        rGraph.unhighlightNode(second, null, null);
                        if (first == second) continue;
                        rGraph.unhighlightEdge(first, second, null, null);
                        first = second;
                    }
                }
            }
        }

        /*
            FINAL PAGE
         */
         sourceCode.hide();
         rGraph.hide();
         graph.show();

        Text t1 = lang.newText(new Offset(80, 5, graph, AnimalScript.DIRECTION_NE), "So this flow is now the maximum possible flow for this graph.", "t1", null, apIntro);
        Text t2 = lang.newText(new Offset(80, 25, graph, AnimalScript.DIRECTION_NE), "There is no more feasible flow. No more increase i possible.", "t2", null, apIntro);
        Text t4 = lang.newText(new Offset(80, 65, graph, AnimalScript.DIRECTION_NE), "Complexity: O(n * T(n,m)),  where n = number of nodes, m = number of arcs", "t4", null, apIntro);
        Text t5 = lang.newText(new Offset(80, 85, graph, AnimalScript.DIRECTION_NE), "and T is the complexity of the chosen blocking flow algorithm.", "t5", null, apIntro);

        lang.nextStep();

        return lang.toString();
    }

    public String getName() {
        return "Dinic - Maximum Flow Problem";
    }

    public String getAlgorithmName() {
        return "Dinic";
    }

    public String getAnimationAuthor() {
        return "Amer Shafa, Ahmed Zukic";
    }

    public String getDescription(){
        return "Dinic is an algorithm for solving the Maximum-Flow-Problem."
 +"\n"
 +"Complexity: O(n * T(n,m)),  where n = number of nodes, m = number of arcs"
 +"\n"
 +"and T is the complexity of chosen blocking flow algorithm";
    }

    public String getCodeExample(){
        return "while (true) {"
 +"\n"
 +"     Graph layeredGraph = findAllShortestPaths(graph)"
 +"\n"
 +"     if (hasNoPath(layeredGraph))"
 +"\n"
 +"         terminate"
 +"\n"
 +"    forEach (path in layerdGraph) {"
 +"\n"
 +"        findBlockingFlow(path)"
 +"\n"
 +"        augment(path)"
 +"\n"
 +"    }"
 +"\n"
 +"}";
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



    ///////////////////////
    //////////////////////

    // HELPER FUNCTIONS //

    /////////////////////
    /////////////////////
    private int[] getNodesAsIntArray(int[][] nodes) {
        int[] allNodesInt = new int[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            allNodesInt[i] = i;
        }

        return allNodesInt;
    }

    private int[][] getAdjacenceMatrixDirected(int[][] arcs, int[] nodes) {
        int[][] matrix = new int[nodes.length][nodes.length];
        // init matrix
        for (int i = 0; i < nodes.length; i++)
            for (int j = 0; j < nodes.length; j++)
                matrix[i][j] = 0;

        for (int i = 0; i < arcs.length; i++) {
            matrix[arcs[i][0]][arcs[i][1]] = arcs[i][2];
        }

        return matrix;
    }

    private int[][] getAdjacenceMatrixUndirected(int[][] arcs, int[] nodes) {
        int[][] matrix = new int[nodes.length][nodes.length];
        // init matrix
        for (int i = 0; i < nodes.length; i++)
            for (int j = 0; j < nodes.length; j++)
                matrix[i][j] = 0;

        for (int i = 0; i < arcs.length; i++) {
            matrix[arcs[i][0]][arcs[i][1]] = arcs[i][2];
            matrix[arcs[i][1]][arcs[i][0]] = arcs[i][2];
        }

        return matrix;
    }

    private Boolean hasAdmissibles(ArrayList<HashMap<Integer, Integer>> flow, int weightedAdjacence[][], int node) {
        for (int i = 0; i < weightedAdjacence.length; i++) {
            if (weightedAdjacence[node][i] != 0 && flow.get(node).get(i) < weightedAdjacence[node][i]) return true;
        }

        return false;
    }

    private void augment(LinkedList<Integer> path, int matrixDirected[][], ArrayList<HashMap<Integer, Integer>> flow, Graph graph, Graph rGraph) {
        Integer first = path.getFirst();
        Integer min = Integer.MAX_VALUE;
        for (Integer second : path) {
            if (second == first) continue; // skip first iteration
            Integer value = matrixDirected[first][second];
            // calculate residual capacity
            if (matrixDirected[first][second] == 0)
                value = flow.get(second).get(first); // backward
            else
                value -= flow.get(first).get(second); // forward
            if (min > value) min = value; // set min value for augmentation
            first = second;
        }
        // augment
        first = path.getFirst();
        for (Integer second : path) {
            int source; int target;
            if (second == first) continue; // skip first iteration
            Integer oldVal;
            if (matrixDirected[first][second] == 0) {
                source = second;
                target = first;
                oldVal = flow.get(second).get(first);
                flow.get(second).put(first, oldVal - min); // decrease (backward edge)
            }
            else {
                source = first;
                target = second;
                oldVal = flow.get(first).get(second);
                flow.get(first).put(second, oldVal + min); // increase (forward edge)
            }
            // adapt graph labels
            graph.setEdgeWeight(source, target, String.valueOf(flow.get(source).get(target)) + "/" + String.valueOf(matrixDirected[source][target]), null, null );
            rGraph.setEdgeWeight(source, target, String.valueOf(flow.get(source).get(target)) + "|" + String.valueOf(matrixDirected[source][target] - flow.get(source).get(target)), null, null );
            first = second;
        }
    }


    private Boolean isAdmissible(int first, int second, int weightsDir[][], ArrayList<HashMap<Integer, Integer>> flow, IntArray distances) {
        if (weightsDir[first][second] == 0) { // backward
            if (flow.get(second).get(first) > 0
                    && distances.getData(first) == distances.getData(second)+1) return true;
        }
        else { // forward
            if (weightsDir[first][second] > flow.get(first).get(second)
                    && distances.getData(first) == distances.getData(second)+1) return true;
        }
        return false;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static LinkedList<LinkedList<Integer>> findAllShortestPaths( int[][] weights, ArrayList<HashMap<Integer, Integer>> Flow ) {
        LinkedList<LinkedList<Integer>> allPaths = new LinkedList<>();
        Integer shortestLength = Integer.MAX_VALUE - 1;
        // find all shortest paths
        LinkedList<Integer> iterators = new LinkedList<>();
        LinkedList<Integer> nodesIters = new LinkedList<>();
        nodesIters.addLast(0); // start with S
        Integer iter = 0;
        while (!nodesIters.isEmpty()) {
            for (; iter < weights.length; iter++) {
                if (nodesIters.contains(iter)) { continue; }
                    if (isFeasible(weights, nodesIters.getLast(), iter, Flow)) {
                        nodesIters.addLast(iter); // add arc to path
                        iterators.addLast(iter);

                        if (iter == weights.length - 1) { // Path found
                            allPaths.addLast(new LinkedList(nodesIters));
                            if (nodesIters.size() < shortestLength)
                                shortestLength = nodesIters.size(); // keep shortest path length
                            nodesIters.removeLast();
                            iterators.removeLast(); // back from target node

                            nodesIters.removeLast();
                            iter = iterators.getLast();
                            iterators.removeLast();
                            continue;
                        }

                        iter = 0; // reset for next arc; no arc to source node;
                    }
                    // else no arc feasible
                    if (iter == weights.length - 1 && nodesIters.size() > 1) {
                        // go one node back and continue
                        nodesIters.removeLast();
                        iter = iterators.getLast();
                        iterators.removeLast();
                    }

            }
            // go one node back and continue
            nodesIters.removeLast();
            if (iterators.isEmpty()) {
                break;
            }
            iter = iterators.getLast() + 1;
            iterators.removeLast();
        }

        // remove all Paths greater than shortest path length
        LinkedList<LinkedList<Integer>> shortestPaths = new LinkedList<>();
        for (LinkedList<Integer> path : allPaths)
            if (path.size() == shortestLength)
                shortestPaths.addLast(path);

            return shortestPaths;
    }

    private static Boolean isFeasible(int[][] weights, int first, int second, ArrayList<HashMap<Integer, Integer>> Flow) {
        Boolean forward = weights[first][second] != 0;
        Boolean backward = weights[second][first] != 0;
        return ((forward && Flow.get(first).get(second) < weights[first][second])  ||  (backward && Flow.get(second).get(first) > 0));
    }

}