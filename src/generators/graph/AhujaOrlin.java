package generators.graph;
/*
 * AhujaOrlinFrame.java
 * Amer Shafa, Ahmed Zukic, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;

import algoanim.primitives.generators.Language;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class AhujaOrlin implements ValidatingGenerator {
    @SuppressWarnings("unused")
    private Language lang;
    private int[][] arcs;
    private int[][] nodePositions;

    public void init(){
        lang = new AnimalScript("Ahuja-Orlin - Maximum Flow Problem", "Amer Shafa, Ahmed Zukic", 800, 600);
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

    @SuppressWarnings("unused")
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
        Text title = lang.newText(new Coordinates(50, 30), "Ahuja-Orlin - Maximum Flow Problem", "title", null, titleProps);
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
        Text intro3 = lang.newText(new Coordinates(50, 150), "about this online. We show you how the algorithm of Ahuja-Orlin works", "intro3", null, apIntro);
        Text intro4 = lang.newText(new Coordinates(50, 175), "to solve this problem.", "intro4", null, apIntro);

        lang.nextStep();

        intro1.hide();
        intro2.hide();
        intro3.hide();
        intro4.hide();

        Text tipHeader = lang.newText(new Coordinates(50, 100), "Important Notes", "tipHeader", null, apIntro);
        tipHeader.changeColor("color", Color.RED, null, null);
        Text tip0 = lang.newText(new Coordinates(50, 120), "Ahuja-Orlin works with custom DISTANCES. The way we use this can be compared with needed hops from one node to the target node.", "tip0", null, apIntro);
        Text tip1 = lang.newText(new Coordinates(50, 140), "For this we just use a Breadth-First-Search algorithm starting from target node and mark each node with the minimum number of hops.", "tip1", null, apIntro);
        Text tip2 = lang.newText(new Coordinates(50, 160), "(Animal has a generator for explaining this)", "tip2", null, apIntro);
        Text tip3 = lang.newText(new Coordinates(50, 180), "Admissible Arcs:", "tip3", null, apIntro);
        Text tip4 = lang.newText(new Coordinates(50, 200), "An admissible arc is an arc which distance of it's source node is the distance of it's target node + 1! ", "tip4", null, apIntro);
        Text tip5 = lang.newText(new Coordinates(50, 220), "AND if the flow through this arc is currently not exhausted! (this means we can increase the flow a bit on this arc)", "tip5", null, apIntro);
        Text tip6 = lang.newText(new Coordinates(50, 240), "This is very important to remember to understand the algorithm.", "tip6", null, apIntro);
        Text tip7 = lang.newText(new Coordinates(50, 350), "More detailed definition on: https://wiki.algo.informatik.tu-darmstadt.de/Ahuja-Orlin", "tip7", null, apIntro);

        lang.nextStep();

        tipHeader.hide();
        tip0.hide();
        tip1.hide();
        tip2.hide();
        tip3.hide();
        tip4.hide();
        tip5.hide();
        tip6.hide();
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

        // graph.hide();

        /*
            RESIDUAL GRAPH
         */
        GraphProperties residualGraphProps = new GraphProperties();
        residualGraphProps.set("color", Color.WHITE);
        residualGraphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.decode("#fff3cd"));
        residualGraphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.decode("#856404"));
        residualGraphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("#F8F9FA"));
        residualGraphProps.set("weighted", true);
        residualGraphProps.set("directed", false);

        int weightsUndir[][] = getAdjacenceMatrixUndirected(arcs, allNodesInt);
        Graph rGraph = lang.newGraph("rGraph", weightsUndir, nodes, nodeLabels, null, residualGraphProps);

        rGraph.setStartNode(rGraph.getNode(0));
        rGraph.setTargetNode(rGraph.getNode(allNodesInt.length-1));

        /*
            SET EDGE WEIGHT
         */
        for (int i = 0; i < arcs.length; i++)
            rGraph.setEdgeWeight(arcs[i][0], arcs[i][1], "0|" + String.valueOf(arcs[i][2]), null, null);

        rGraph.hide();

        /*
            CURRENT TOP NODE
         */
        TextProperties currentTopNodeProps = new TextProperties();
        Text currentTopNodeTitle = lang.newText(new Offset(20, 0, title, AnimalScript.DIRECTION_NE), "currentNode:", "currentTopNodeTitle", null, currentTopNodeProps);
        Text currentTopNode = lang.newText(new Offset(5, 0, currentTopNodeTitle, AnimalScript.DIRECTION_NE), "", "currentTopNode", null);

        /*
            NODE DISTANCE ARRAY
         */
        Text distanceTitle = lang.newText(new Offset(15, 0, currentTopNode, AnimalScript.DIRECTION_NE), "Distance:", "distanceTitle", null);
        // Einstellen der Füllfarbe gelb für das distance array
        ArrayProperties apDistance = new ArrayProperties();
        apDistance.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        apDistance.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        apDistance.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

        // Erzeugen eines IntArray-Objects zur Visualisierung des Arrays; nutzt die Properties
        IntArray distArray = lang.newIntArray(new Offset(5, -5, distanceTitle, AnimalScript.DIRECTION_NE), allNodesInt, "distanceArray", null, apDistance);

        /*
            NEXT ARC Arrays
         */
        Text nextArcTitle = lang.newText(new Offset(0, 50, distanceTitle, AnimalScript.DIRECTION_NW), "nextArc:", "nextArcTitle", null);
        ArrayProperties apNextArc = new ArrayProperties();
        apNextArc.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        apNextArc.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        apNextArc.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
        StringArray nextArcArray[] = new StringArray[allNodesInt.length];
        for (int i = 0; i < allNodesInt.length-1; i++) { // -1, cause T has no outgoing arcs
            int counter = 0;
            int l = Arrays.stream(rGraph.getEdgesForNode(i)).filter(x -> x != 0).toArray().length;
            String[] nextarcs = new String[l];
            for (int j = 0; j < allNodesInt.length; j++)
                if (rGraph.getEdgesForNode(i)[j] != 0)
                    nextarcs[counter++] = rGraph.getNodeLabel(j);
            nextArcArray[i] = lang.newStringArray(new Offset(10, -5, nextArcTitle, AnimalScript.DIRECTION_NE), nextarcs, "nextArcArray"+String.valueOf(i), null, apNextArc);
            nextArcArray[i].hide();
        }

        lang.nextStep();

        /*
            PSEUDO CODE
         */
        SourceCodeProperties apSourceCode = new SourceCodeProperties();
        apSourceCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        SourceCode sourceCode = lang.newSourceCode(new Offset(80, 5, graph, AnimalScript.DIRECTION_NE), "code", null, apSourceCode);
        sourceCode.addCodeLine("Path = new Stack", "code", 0, null);
        sourceCode.addCodeLine("push(s) on Path", "code", 0, null);
        sourceCode.addCodeLine("setDistanceWithBFS_from(t)", "code", 0, null);
        sourceCode.addCodeLine("while (distance(top(path)) < n) {", "code", 0, null);
        sourceCode.addCodeLine("if (top(Path) == s && !hasAdmissibles(s) )", "code", 1, null);
        sourceCode.addCodeLine("terminate", "code", 2, null);
        sourceCode.addCodeLine("if (top(Path) == t)", "code", 1, null);
        sourceCode.addCodeLine("augment(Path)", "code", 2, null);
        sourceCode.addCodeLine("clear(Path)", "code", 2, null);
        sourceCode.addCodeLine("else", "code", 1, null);
        sourceCode.addCodeLine("foreach (arc from top(Path))", "code", 2, null);
        sourceCode.addCodeLine("if (isAdmissible(arc))", "code", 3, null);
        sourceCode.addCodeLine("push(arc) on Path", "code", 4, null);
        sourceCode.addCodeLine("break", "code", 4, null);
        sourceCode.addCodeLine("distance(top(Path)) = 1 + min(allDistancesOfResidualArcs())", "code", 2, null);
        sourceCode.addCodeLine("pop() from Path", "code", 2, null);
        sourceCode.addCodeLine("}", "code", 0, null);

        lang.nextStep();

        /*

            ALGORITHM -> AHUJA-ORLIN

         */
        sourceCode.highlight(0);
        LinkedList<Integer> Path = new LinkedList<>();
        lang.nextStep();

        sourceCode.unhighlight(0);
        sourceCode.highlight(1);
        Path.addLast(0);
        graph.highlightNode(Path.getLast(), null, null);
        rGraph.highlightNode(Path.getLast(), null, null);
        lang.nextStep();

        // set distances with BFS
        sourceCode.unhighlight(1);
        sourceCode.highlight(2);

        LinkedList<Integer> Nodes = new LinkedList<>();
        Nodes.addFirst(allNodesInt.length-1);
        HashMap<Integer, Boolean> markedNodes = new HashMap<>(allNodesInt.length);
        distArray.put(allNodesInt.length-1, 0, null, null); // mark T wth distance 0
        for (int i = 0; i < allNodesInt.length; i++) {
            markedNodes.put(i, false); // init visited
            markedNodes.put(allNodesInt.length-1, true);
        }
        while (!Nodes.isEmpty()) {
            // iterate through all edges
            for (int i = 0; i < allNodesInt.length; i++) {
                // is Edge valid ?
                if (weightsUndir[Nodes.getFirst()][i] != 0) {
                    // is next node already visited ?
                    if (!markedNodes.get(i)) {
                        // mark end node with distance value
                        markedNodes.put(i, true);
                        distArray.put(i, distArray.getData(Nodes.getFirst())+1, null, null); // mark T wth distance 0
                        Nodes.addLast(i);
                    }
                }
            }
            Nodes.removeFirst();
        }

        lang.nextStep();

        ArrayList<HashMap<Integer, Integer>> Flow = new ArrayList<>();
        // Init Zero Flow
        for (int i = 0; i < allNodesInt.length; i++) {
            Flow.add(new HashMap<>(allNodesInt.length));
            for (int j = 0; j < allNodesInt.length; j++)
                Flow.get(i).put(j, 0);
        }
        while (distArray.getData(0) < allNodesInt.length)
        {
            if (Path.size() > 1) nextArcArray[Path.get(Path.size()-2)].hide();
            if (Path.getLast() != allNodesInt.length-1) {
                nextArcArray[Path.getLast()].show();
                for (int k = 0; k < nextArcArray[Path.getLast()].getLength(); k++) {
                    nextArcArray[Path.getLast()].unhighlightElem(k,null, null);
                    nextArcArray[Path.getLast()].unhighlightCell(k,null, null);
                }
            }
            currentTopNode.setText(graph.getNodeLabel(Path.getLast()), null, null);
            graph.highlightNode(Path.getLast(), null, null);
            rGraph.highlightNode(Path.getLast(), null, null);

            sourceCode.unhighlight(2);
            sourceCode.unhighlight(8);
            sourceCode.unhighlight(13);
            sourceCode.unhighlight(15);
            sourceCode.highlight(3);
            lang.nextStep("next iteration");

            graph.hide();
            rGraph.show();

            sourceCode.unhighlight(3);
            sourceCode.highlight(4);
            lang.nextStep();
            if (Path.getLast() == 0 && !hasAdmissibles(Flow, weightsUndir, 0)) {
                sourceCode.unhighlight(4);
                sourceCode.highlight(5);
                lang.nextStep();
                break;
            }

            sourceCode.unhighlight(4);
            sourceCode.highlight(6);
            lang.nextStep();
            if (Path.getLast() == allNodesInt.length-1) {
                // augment
                augment(Path, weights, Flow, graph, rGraph);
                sourceCode.unhighlight(6);
                sourceCode.highlight(7);
                lang.nextStep("augment next feasible path");

                //clear(Path);
                while (Path.size() > 1) {
                    graph.unhighlightNode(Path.getLast(), null, null);
                    rGraph.unhighlightNode(Path.getLast(), null, null);
                    Integer second = Path.getLast();
                    Path.removeLast(); // only S stays
                    graph.unhighlightEdge(Path.getLast(), second, null, null);
                    rGraph.unhighlightEdge(Path.getLast(), second, null, null);
                }
                sourceCode.unhighlight(7);
                sourceCode.highlight(8);
                lang.nextStep();

                graph.show();
                rGraph.hide();
            }
            else {
                sourceCode.unhighlight(6);
                sourceCode.highlight(9);
                lang.nextStep();

                Boolean pushed = false;
                int nextArcIndex = 0;
                for (int i = 0; i < allNodesInt.length; i++) {
                    if (weightsUndir[Path.getLast()][i] != 0) {
                        graph.highlightNode(i, null, null);
                        rGraph.highlightNode(i, null, null);
                        nextArcArray[Path.getLast()].highlightCell(nextArcIndex, null, null);

                        sourceCode.unhighlight(9);
                        sourceCode.unhighlight(11);
                        sourceCode.highlight(10);
                        lang.nextStep();
                        sourceCode.unhighlight(10);
                        sourceCode.highlight(11);
                        lang.nextStep();
                        if (isAdmissible(Path.getLast(), i, weights, Flow, distArray) && !Path.contains(i)) {
                            graph.highlightEdge(Path.getLast(), i, null, null);
                            rGraph.highlightEdge(Path.getLast(), i, null, null);

                            sourceCode.unhighlight(11);
                            sourceCode.highlight(12);
                            lang.nextStep("found next admissible arc");

                            graph.show();
                            rGraph.hide();
                            nextArcArray[Path.getLast()].hide();

                            Path.addLast(i);

                            sourceCode.unhighlight(12);
                            sourceCode.highlight(13);
                            lang.nextStep();
                            pushed = true;
                            break;
                        } else {
                            // highlight as unadmissible
                            nextArcArray[Path.getLast()].highlightElem(nextArcIndex, null, null);
                            nextArcArray[Path.getLast()].unhighlightCell(nextArcIndex, null, null);
                        }
                        nextArcIndex++;
                    }
                    if (!Path.contains(i)) {
                        graph.unhighlightNode(i, null, null);
                        rGraph.unhighlightNode(i, null, null);
                    }
                }

                if (pushed) continue;
                distArray.highlightCell(Path.getLast(), null, null);
                sourceCode.unhighlight(11);
                sourceCode.highlight(14);
                lang.nextStep();

                // increase distance, no next arc possible
                Integer min = Integer.MAX_VALUE -1 ; // to prevent overflow if no outgoing arcs exist
                for (int i = 0; i < allNodesInt.length; i++) {
                    if (weightsUndir[Path.getLast()][i] != 0) {
                        if (
                                (weights[Path.getLast()][i] != 0 && (Flow.get(Path.getLast()).get(i)) < weights[Path.getLast()][i]) // forward
                                        || ( (weights[Path.getLast()][i] == 0) && (Flow.get(i).get(Path.getLast()) > 0) ) // backward
                        ) {
                            if (min > distArray.getData(i)) min = distArray.getData(i);
                        }
                    }
                }
                distArray.put(Path.getLast(), min+1, null, null);
                lang.nextStep("increase distance, step back");

                distArray.unhighlightCell(Path.getLast(), null, null);
                if (Path.getLast() != 0) {
                    graph.unhighlightNode(Path.getLast(), null, null);
                    rGraph.unhighlightNode(Path.getLast(), null, null);
                    Integer second = Path.getLast();
                    Path.removeLast();
                    graph.unhighlightEdge(Path.getLast(), second, null, null);
                    rGraph.unhighlightEdge(Path.getLast(), second, null, null);

                    nextArcArray[second].hide();
                }

                graph.show();
                rGraph.hide();

                sourceCode.unhighlight(14);
                sourceCode.highlight(15);
                lang.nextStep();
            }
        }

        /*
            FINAL PAGE
         */
        sourceCode.hide();
        nextArcTitle.hide();
        nextArcArray[0].hide();
        distanceTitle.hide();
        distArray.hide();
        currentTopNodeTitle.hide();
        currentTopNode.hide();

        rGraph.hide();
        graph.show();
        graph.unhighlightNode(0, null, null);

        Text t1 = lang.newText(new Coordinates(800, 100), "So this flow is now the maximum possible flow for this graph.", "t1", null, apIntro);
        Text t2 = lang.newText(new Coordinates(800, 130), "Either S can't augment any of it's outgoing arcs", "t2", null, apIntro);
        Text t3 = lang.newText(new Coordinates(800, 150), "OR it's distance is same as the number of all nodes.", "t3", null, apIntro);
        Text t4 = lang.newText(new Coordinates(800, 200), "Complexity: O(n^2 * m),   where n = number of nodes, m = number of arcs", "t4", null, apIntro);

        lang.nextStep();





        return lang.toString();
    }

    public String getName() {
        return "Ahuja-Orlin - Maximum Flow Problem";
    }

    public String getAlgorithmName() {
        return "Ahuja-Orlin";
    }

    public String getAnimationAuthor() {
        return "Amer Shafa, Ahmed Zukic";
    }

    public String getDescription(){
        return "Ahuja-Orlin is an algorithm for solving the Maximum-Flow-Problem. A special feature "
 +"\n"
 +"in this algorithm is the use of custom distances for reducing complexity to O(n^2 * m), "
 +"\n"
 +"where n = number of nodes, m = number of arcs.";
    }

    public String getCodeExample(){
        return "Path = new Stack"
 +"\n"
 +"push(s) on Path"
 +"\n"
 +"setDistancesWithBFS_from(t)"
 +"\n"
 +"while (distance(top(path)) < n) {"
 +"\n"
 +"    if (top(Path) == s && !hasAdmissibles(s) )"
 +"\n"
 +"        terminate"
 +"\n"
 +"    if (top(Path) == t)"
 +"\n"
 +"        augment(Path)"
 +"\n"
 +"        clear(Path)"
 +"\n"
 +"    else"
 +"\n"
 +"        foreach (arc from top(Path))"
 +"\n"
 +"            if (isAdmissible(arc))"
 +"\n"
 +"                push(arc) on Path"
 +"\n"
 +"                break"
 +"\n"
 +"        distance(top(Path)) = 1 + min(allDistancesOfResidualArcs())"
 +"\n"
 +"        pop() from Path"
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

}