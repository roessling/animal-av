package generators.misc;/*
 * hus_algo.java
 * Balint Klement, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.*;
import java.util.List;
import java.util.*;

public class HusAlgorithm implements ValidatingGenerator {
    private Language l;
    private int nmb_machines;
    //private ArrayProperties machineProps;
    private int[][] adjacencyMatrix;
    private String[] nodeNames;

    private Graph g;
    private StringArray[] machines;
    private int[] machinePacked;
    private GraphNode[] graphNodes;
    private int nmbNodes;

    public static void main(String[] args) {
        Generator generator = new HusAlgorithm(); // Generator erzeugen
        Animal.startGeneratorWindow(generator); // Animal mit Generator starten
    }

    public void init() {
        l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Hu's Algorithm", "Balint Klement", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        nmb_machines = (Integer) primitives.get("nmb_machines");
        //machineProps = (ArrayProperties) props.getPropertiesByName("machineProps");
        adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
        nodeNames = (String[]) primitives.get("nodeNames");

        nmb_machines = (Integer) primitives.get("nmb_machines");
        adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
        nodeNames = (String[]) primitives.get("nodeNames");

        l.setStepMode(true);
        l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        // 1. Step: show title and description
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
        Primitive header = l.newText(new Coordinates(20, 30), "Hu's Algorithm", "header", null, headerProps);
        Primitive hRect = l.newRect(new Offset(-5, -5, "header", "NW"),
                new Offset(5, 5, "header", "SE"), "hRect", null);

        SourceCodeProperties descriptionProps = new SourceCodeProperties();
        descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        SourceCode description = l.newSourceCode(new Coordinates(10, 100), "description", null, descriptionProps);
        description.addCodeLine("A scheduling problem tries to assig n jobs to machines and timeslots,", null, 0, null);
        description.addCodeLine("while minimizing some function - in this case the total length of the createContentFromInput.", null, 0, null);
        description.addCodeLine("", null, 0, null);
        description.addCodeLine("Hu's algorithm solves scheduling problems with predecessor-constraints for multiple machines.", null, 0, null);
        description.addCodeLine("We assume, that jobs have unit length and the constraints are given as a directed acyclic forest.", null, 0, null);

        l.nextStep("Introduction");


        // 2. Step: hide description and show input, pseudoCode and variables
        java.util.List<Primitive> notHide = new ArrayList<>();
        notHide.add(header);
        notHide.add(hRect);
        l.hideAllPrimitivesExcept(notHide);

        populateGraphNodes();
        createContentFromInput();

        SourceCodeProperties pseudoCodeProps = new SourceCodeProperties();
        pseudoCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        pseudoCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        SourceCode pseudoCode = l.newSourceCode(new Offset(100, -10, "graph", "NE"), "pseudoCode",
                null, pseudoCodeProps);
        pseudoCode.addCodeLine("initialize the variables: ranks, j and m", null, 0, null);
        pseudoCode.addCodeLine("while (there are available jobs)", null, 0, null);
        pseudoCode.addCodeLine("schedule up to m available jobs, priorize the high ranked ones", null, 1, null);
        pseudoCode.addCodeLine("terminate", null, 0, null);
        TextProperties pseudoHeaderProps = new TextProperties();
        pseudoHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
        Text pseudoHeader = l.newText(new Offset(0, -20, "pseudoCode", "WE"), "PseudoCode",
                "pseudoHeader", null, pseudoHeaderProps);

        SourceCodeProperties variablesProps = new SourceCodeProperties();
        SourceCode variables = l.newSourceCode(new Offset(0, 100, "pseudoCode", "NW"), "variables",
                null, variablesProps);
        variables.addCodeLine("rank: describes the number of successors of each node", null, 0, null);
        variables.addCodeLine("nodes become available, when all their predecessors are processed", null, 1, null);
        variables.addCodeLine("red rank: unavailable; green rank: available; black rank: processed", null, 1, null);
        l.newText(new Offset(0, -20, "variables", "NW"), "Variables", "variablesHeader",
                null, pseudoHeaderProps);
        l.nextStep("Initialization");


        // 3. Step: initialize the variables (including the ranks)
        pseudoCode.highlight(0);
        variables.addCodeLine("j (number of jobs): " + nodeNames.length, null, 0, null);
        variables.addCodeLine("m (number of machines): " + nmb_machines, null, 0, null);
        TextProperties rankProps = new TextProperties();
        rankProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
        Text[] ranks = new Text[graphNodes.length];
        Arrays.stream(graphNodes).forEach(node ->
                ranks[node.id] = l.newText(new Coordinates(node.getX() - 18, node.getY()),
                        String.valueOf(node.level), "label_" + node.id, null, rankProps));
        Arrays.stream(ranks).forEach(text -> text.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null));

        List<Integer> available = findAvailableJobs();
        available.forEach(nodeID -> ranks[nodeID].changeColor(AnimalScript.COLORCHANGE_COLOR, Color.GREEN, null, null));

        l.nextStep();


        // 4. Step: start the while-loop (we assume no empty input)
        FillInBlanksQuestionModel maxNmbOfJobs = new FillInBlanksQuestionModel("maxJobs");
        maxNmbOfJobs.setPrompt("How many jobs could be scheduled at most simultaneously?");
        maxNmbOfJobs.addAnswer(String.valueOf(nmb_machines), 2, "That's right! In each timestep, a machine can work on zero or one jobs, so the correct answer is the number of machines");
        //maxNmbOfJobs.setGroupID("maxJobs");
        //maxNmbOfJobs.setNumberOfTries(1);
        l.addFIBQuestion(maxNmbOfJobs);

        MultipleChoiceQuestionModel unavailableJobs = new MultipleChoiceQuestionModel("unavailableJobs");
        unavailableJobs.addAnswer("Other equally important jobs where scheduled.", 0, "That's not the right answer. Try again!");
        unavailableJobs.addAnswer("It has already been processed in earlier steps.", 0, "That's not the right answer. Try again!");
        unavailableJobs.addAnswer("It is not available yet.", 3, "That's right! Until a job has some unprocessed predecessors, it can't be picked.");
        unavailableJobs.setGroupID("unavailable");
        maxNmbOfJobs.setNumberOfTries(2);

        pseudoCode.highlight(1);
        pseudoCode.unhighlight(0);

        l.nextStep("Loop");


        // 5. Step: loop-part of the algorithm
        do {

            // part a: pick fitting jobs, highlight graph and stringArrays
            pseudoCode.highlight(2);
            pseudoCode.unhighlight(1);

            List<Integer> unavailableNodes = new ArrayList<>();
            for (int nodeID = 0; nodeID < graphNodes.length; nodeID++) {
                if (!available.contains(nodeID) && !graphNodes[nodeID].processed)
                    unavailableNodes.add(nodeID);
            }

            List<GraphNode> processed = new ArrayList<>();
            available.sort(Comparator.comparingInt(nodeID -> graphNodes[nodeID].level));
            int bound = Math.min(nmb_machines, available.size());
            for (int m = 0; m < bound; m++) {
                GraphNode node = graphNodes[available.remove(available.size() - 1)];
                g.highlightNode(node.id, null, null);
                processed.add(node);
                machines[m].put(machinePacked[m], nodeNames[node.id], null, null);
                machines[m].highlightCell(machinePacked[m], null, null);
                machinePacked[m]++;
            }

            l.nextStep();


            if (unavailableNodes.size() > 0) {
                System.out.println("unavailable...");
                unavailableJobs.setPrompt("Why was the job " + nodeNames[unavailableNodes.get(0)] + " not scheduled in this step?");
                l.addMCQuestion(unavailableJobs);
            }

            // step A: update rank colors; unhighlight cells and nodes; check for available jobs
            pseudoCode.highlight(1);
            pseudoCode.unhighlight(2);

            processed.forEach(node -> {
                ranks[node.id].changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
                graphNodes[node.id].processed = true;
                Arrays.fill(adjacencyMatrix[node.id], 0);
                g.unhighlightNode(node.id, null, null);
            });
            for (int m = 0; m < bound; m++) {
                machines[m].unhighlightCell(machinePacked[m] - 1, null, null);
            }
            available = findAvailableJobs();
            available.forEach(nodeID -> ranks[nodeID].changeColor(AnimalScript.COLORCHANGE_COLOR, Color.GREEN, null, null));

            l.nextStep();

        } while (!available.isEmpty());

        // last step: show summary, highlight final schedule
        pseudoCode.highlight(3);
        pseudoCode.unhighlight(1);

        for (int m = 0; m < nmb_machines; m++) {
            for (int i = 0; i < machinePacked[m]; i++) {
                machines[m].highlightCell(i, null, null);
            }
        }

        notHide.addAll(Arrays.asList(machines));
        notHide.addAll(Arrays.asList(ranks));
        notHide.add(g);
        notHide.add(pseudoCode);
        notHide.add(pseudoHeader);
        l.hideAllPrimitivesExcept(notHide);
        SourceCodeProperties summaryProp = new SourceCodeProperties();
        SourceCode summary = l.newSourceCode(new Offset(0, 100, "pseudoCode", "NW"), "summary",
                null, summaryProp);
        summary.addCodeLine("FINISHED! all jobs have been scheduled", null, 0, null);
        int solutionLength = Arrays.stream(machinePacked).max().getAsInt() - 1;
        summary.addCodeLine("The solution has length: " + solutionLength, null, 0, null);
        int longestDependencyGraphLength = (Arrays.stream(graphNodes).max(Comparator.comparingInt(node -> node.level)).get().level + 1);
        summary.addCodeLine("The longest dependency graph has " + longestDependencyGraphLength +
                " levels (" + longestDependencyGraphLength + " is a lower bound for the solution)", null, 0, null);
        summary.addCodeLine(String.format("There are %s jobs for %s machines (%s/%s=%s is a lower bound for the solution)", nodeNames.length, nmb_machines,
                nodeNames.length, nmb_machines, nodeNames.length / nmb_machines + 1), null, 0, null);
        summary.addCodeLine("Machines are utilized " + Math.round((nodeNames.length * 1d) / (solutionLength * nmb_machines) * 100.0) / 100.0 + " of the time", null, 0, null);
        l.newText(new Offset(0, -20, "variables", "NW"), "Summary", "summaryHeader",
                null, pseudoHeaderProps);

        l.nextStep("Summary");

        l.finalizeGeneration();

        return l.toString();
    }

    private List<Integer> findAvailableJobs() {
        List<Integer> available = new ArrayList<>();
        for (int column = 0; column < nmbNodes; column++) {
            int dependency = 0;
            for (int[] ints : adjacencyMatrix) {
                if (ints[column] > 0) dependency++;
            }
            if ((dependency == 0) && (!graphNodes[column].processed)) {
                available.add(column);
            }
        }
        return available;
    }

/**
 * creates the graph and array depending on the input
 */
    private void createContentFromInput() {
        //graph...
        GraphProperties graphProperties = new GraphProperties();
        graphProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        graphProperties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
        graphProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.YELLOW);
        g = l.newGraph("graph", adjacencyMatrix, graphNodes, nodeNames, null, graphProperties);

        //arrays...
        machines = new StringArray[nmb_machines];
        ArrayProperties machineProps = new ArrayProperties();
        machineProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        machineProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

        String[] content = new String[nmbNodes + 1];
        content[0] = "M1";
        for (int j = 1; j < content.length; j++) content[j] = " ";

        machines[0] = l.newStringArray(new Offset(0, 40, "graph", "SW"), content, "m1", null, machineProps);
        for (int i = 1; i < nmb_machines; i++) {
            content[0] = "M" + (i + 1);
            machines[i] = l.newStringArray(new Offset(0, 0, "m" + i, "SW"), content, "m" + (i + 1), null, machineProps);
            //TwoValueCounter counter = l.newCounter(machines[i]);
            //l.newCounterView(counter, new Offset(20, 0, "m"+i, "W"), null, true, true);
        }
        //maybe anzeigen?
        machinePacked = new int[nmb_machines];
        Arrays.fill(machinePacked, 1);
    }

    /**
     * creates the Nodes of the graph(s) (especially calculating their position) based on the adjacencyMatrix
     */
    private void populateGraphNodes() {
        nmbNodes = adjacencyMatrix.length;
        graphNodes = new GraphNode[nmbNodes];

        ArrayList<GraphNode> q = new ArrayList<>();
        int graphCt = 0;
        int graphOffset = 0;
        int totalGraphOffset = 0;

        for (int i = 0; i < nmbNodes; i++) {
            if (Arrays.stream(adjacencyMatrix[i]).noneMatch(edge -> edge > 0)) {
                GraphNode sink = new GraphNode(i, graphCt, 0, 0, totalGraphOffset);
                graphNodes[i] = sink;
                q.add(sink);

                while (!q.isEmpty()) {
                    GraphNode currentNode = q.remove(0);
                    int columnOffset = 0;
                    for (int j = 0; j < nmbNodes; j++) {
                        if (adjacencyMatrix[j][currentNode.id] > 0) {
                            GraphNode n = new GraphNode(j, currentNode.graph, currentNode.level + 1,
                                    currentNode.columnOffset + columnOffset, currentNode.graphOffset);
                            graphNodes[j] = n;
                            q.add(n);
                            columnOffset += 1;
                        }
                    }
                    graphOffset = Math.max(graphOffset, columnOffset - 1);
                }

                totalGraphOffset += graphOffset;
                graphOffset = 0;
                graphCt++;
            }
        }
    }


    public String getName() {
        return "Hu's Algorithm";
    }

    public String getAlgorithmName() {
        return "Scheduling";
    }

    public String getAnimationAuthor() {
        return "Balint Klement";
    }

    public String getDescription() {
        return "A scheduling problem tries to assign jobs to machines and timeslots,"
                + "\n"
                + "while minimizing some function - in this case the total length of the schedule."
                + "\n"
                + "\n"
                + "Hu's algorithm solves scheduling problems with predecessor-constraint for multiple machines."
                + "\n"
                + "We assume, that jobs have unit length and the constraints are given as a directed acyclic forest.";
    }

    public String getCodeExample() {
        return "initialize the variables: ranks, j and m"
                + "\n"
                + "while (there are available jobs)"
                + "\n"
                + "     schedule up to m available jobs, priorize the high ranked ones"
                + "\n"
                + "terminate";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public int takeWhile(int[] array) {
    	int count = 0, pos = 0;
    	for (; pos < array.length && array[pos] == 0; pos++) ;
    	if (pos > 0)
    	  count = pos - 1;
    	return count;
    }
    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        adjacencyMatrix = (int[][]) primitives.get("adjacencyMatrix");
        nodeNames = (String[]) primitives.get("nodeNames");
        System.out.println(Arrays.deepToString(adjacencyMatrix));

        boolean adjacencyMatrixProperSize =
                (nodeNames.length == adjacencyMatrix.length) && Arrays.stream(adjacencyMatrix).allMatch(row -> row.length == nodeNames.length);
        boolean outDeegreMaxOne =
                Arrays.stream(adjacencyMatrix).allMatch(row -> Arrays.stream(row).filter(entry -> entry != 0).count() <= 1);

        if (adjacencyMatrixProperSize && outDeegreMaxOne) {
            for (int i = 0; i < nodeNames.length; i++) {
                List<Integer> visited = new ArrayList<>();
                visited.add(i);
                int edge = takeWhile(adjacencyMatrix[i]);
                // was: (int) Arrays.stream(adjacencyMatrix[i]).takeWhile(entry -> entry == 0).count();
                while (edge < nodeNames.length) {
                    // valid edge found
                    if (visited.contains(edge)) return false;
                    else {
                        visited.add(edge);
                        edge = takeWhile(adjacencyMatrix[edge]);
                        //was:  (int) Arrays.stream(adjacencyMatrix[edge]).takeWhile(entry -> entry == 0).count();
                    }
                }
            }
            return true;
        }
        else
            return false;
    }
}
