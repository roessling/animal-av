/**
 * @author Marcel Langer, Kevin Kampa
 * @version 1.0 2019-08-01
 * Beamsearch.java
 * Kevin Kampa, Macel langer 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.graph;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import java.awt.Color;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.TranslatableGUIElement;
import translator.Translator;
import java.util.Locale;

import java.awt.Font;
import java.util.*;

public class Beamsearch implements ValidatingGenerator {
    private Language language;
    private TextProperties textProps;
    private SourceCodeProperties sourceCodeProps;
    private RectProperties rectProps;
    private ArrayProperties arrayProperties;
    private int[] heuristic;
    private Graph inputGraph;
    private int beamwidth;
    private HashMap<Node, Node> doBeam, doVisited, doCandidates;
    public Translator  translator;
    private Locale loc;

    /**
     * Beamsearch Constructor
     */
    public Beamsearch(Locale loc) {
        translator = new Translator("resources/Beamsearch", loc) ;
        this.loc = loc;
        this.init();
    }
    public Beamsearch() {
        this(Locale.GERMANY);
    }

    /**
     * Init Generator
     */
    public void init() {
        this.language = new AnimalScript("BeamSearch", "Marcel Langer und Kevin Kampa", 800, 600);
        this.language.setStepMode(true);
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) throws IllegalArgumentException {
        int[] heuristic = (int[])primitives.get("intHeuristic");
        int beamwidth = (int)primitives.get("intBeamWidth");
                                Graph graph = (Graph)primitives.get("graphTestGraphInput");
        if(graph.getSize() == 0 || graph.getStartNode() == null || graph.getTargetNode() == null)
            return false;

        for (Node testme : graph.getNodes())
            if(testme == null)
                return false;

        for(int testMe : heuristic)
            if(testMe < 0)
                return false;

        if(beamwidth < 1 || beamwidth > graph.getSize())
            return false;

        return true;
    }

    /**
     * Generate data to start generator
     * @param props the Animation properties
     * @param primitives the animation primitives
     * @return
     */
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

        this.inputGraph = (Graph)primitives.get("graphTestGraphInput");
        this.sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        this.beamwidth = (Integer)primitives.get("intBeamWidth");
        this.heuristic = (int[])primitives.get("intHeuristic");
        this.arrayProperties = (ArrayProperties)props.getPropertiesByName("arrayProperties");
        this.rectProps = (RectProperties)props.getPropertiesByName("rectBackgroundProperties");

        Graph g = this.inputGraph;
        this.start(g);
        this.language.finalizeGeneration();
        return this.language.toString();
    }
    /**
     * start the animation using a given graph
     * @param graph
     */
    public void start(Graph graph) {
        this.language.setInteractionType(1024); //TODO: mb remove
        graph.hide();

        GraphProperties graphProps = new GraphProperties();
        graphProps.set("fillColor", Color.WHITE);
        graphProps.set("highlightColor", Color.GREEN);
        graphProps.set("edgeColor", Color.BLACK);
        graphProps.set("elemHighlight", Color.GREEN);
        String[] loclab = new String[graph.getSize()];

        for(int i=0;i<loclab.length;i++) {
            loclab[i] = graph.getNodeLabel(i);
        }
        Graph visibleGraph = this.language.newGraph("vgraph", graph.getAdjacencyMatrix(), graph.getNodes(), loclab , (DisplayOptions)null, graphProps);
        visibleGraph.setStartNode(graph.getStartNode());
        visibleGraph.setTargetNode(graph.getTargetNode());
        visibleGraph.hide();


        TextProperties headerProps = new TextProperties();
        headerProps.set("font", new Font("SansSerif", 1, 24));
        Text header = this.language.newText(new Coordinates(10, 10), translator.translateMessage("beam_title"), "header", (DisplayOptions)null, headerProps);
        Rect hRect = this.language.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header", "SE"), "hRect", (DisplayOptions)null, this.rectProps);
        this.language.nextStep();
        header.show();
        hRect.show();
        this.textProps = new TextProperties();
        this.textProps.set("font", new Font("SansSerif", 0, 14));
        this.language.newText(new Coordinates(10, 50), translator.translateMessage("beam_description1"), "description1", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description1", "NW"), translator.translateMessage("beam_description2"), "description2", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description2", "NW"), translator.translateMessage("beam_description3"), "description3", (DisplayOptions)null, this.textProps);
        this.language.nextStep();
        this.language.newText(new Offset(0, 20, "description3", "NW"), translator.translateMessage("beam_description4"), "description4", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description4", "NW"), translator.translateMessage("beam_description5"), "description5", (DisplayOptions)null, this.textProps);
        this.language.nextStep();
        this.language.newText(new Offset(0, 20, "description5", "NW"), translator.translateMessage("beam_description6"), "description6", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description6", "NW"), translator.translateMessage("beam_description7"), "description7", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description7", "NW"), translator.translateMessage("beam_description8"), "description8", (DisplayOptions)null, this.textProps);
        this.language.nextStep();
        this.language.newText(new Offset(0, 20, "description8", "NW"), translator.translateMessage("beam_description9"), "description9", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description9", "NW"), translator.translateMessage("beam_description10"), "description10", (DisplayOptions)null, this.textProps);

        this.language.newText(new Offset(0, 20, "description10", "NW"), translator.translateMessage("beam_description11"), "description11", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description11", "NW"), translator.translateMessage("beam_description12"), "description12", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description12", "NW"), translator.translateMessage("beam_description13"), "description13", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description13", "NW"), translator.translateMessage("beam_description14"), "description14", (DisplayOptions)null, this.textProps);
        this.language.nextStep();
        this.language.newText(new Offset(0, 20, "description14", "NW"), translator.translateMessage("beam_description15"), "description15", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description15", "NW"), translator.translateMessage("beam_description16"), "description16", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description16", "NW"), translator.translateMessage("beam_description17"), "description17", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "description17", "NW"), translator.translateMessage("beam_description18"), "description18", (DisplayOptions)null, this.textProps);



        this.language.nextStep("Startseite");
        this.language.hideAllPrimitives();
        header.show();
        hRect.show();


        SourceCode src = this.language.newSourceCode(new Coordinates(10, 50), "sourceCode", (DisplayOptions)null, this.sourceCodeProps);
        src.addCodeLine("private String beamsearch(Graph graph, int beamwidth, Node start,int heuristicGoal){", (String)null, 0, (Timing)null);
        src.addCodeLine("HashMap<Node, Node> beam, visited, candidates; ", (String)null, 1, (Timing)null);
        src.addCodeLine("beam = visited = candidates = new HashMap<Node, Node>(); ", (String)null, 1, (Timing)null);
        src.addCodeLine("beam.put(start, null);", (String)null, 1, (Timing)null);
        src.addCodeLine("visited.put(start, null);", (String)null, 1, (Timing)null);
        src.addCodeLine("", (String)null, 0, (Timing)null);
        src.addCodeLine("while (beam.size() != 0) {", (String)null, 1, (Timing)null);
        src.addCodeLine("for (Node current : beam.keySet()) {", (String)null, 2, (Timing)null);
        src.addCodeLine("for (Node candidate : graph.adjecantNodes(current)) {", (String)null, 3, (Timing)null);
        src.addCodeLine("if (candidate.heuristicValue == heuristicGoal)", (String)null, 4, (Timing)null);
        src.addCodeLine("return describeFoundPath(visited, candidate, current);", (String)null, 5, (Timing)null);
        src.addCodeLine("candidates.put(candidate, current);", (String)null, 4, (Timing)null);
        src.addCodeLine("}", (String)null, 3, (Timing)null);
        src.addCodeLine("}", (String)null, 2, (Timing)null);
        src.addCodeLine("beam = new HashMap<Node, Node>();", (String)null, 2, (Timing)null);
        src.addCodeLine("", (String)null, 0, (Timing)null);
        src.addCodeLine("while ((candidates.size() != 0) && (beamwidth > beam.size())) {", (String)null, 2, (Timing)null);
        src.addCodeLine("Node validCandidate = nextLowestHeuristic(candidates);", (String)null, 3, (Timing)null);
        src.addCodeLine("if (!visited.containsKey(validCandidate)) {", (String)null, 3, (Timing)null);
        src.addCodeLine("visited.put(validCandidate, candidates.get(validCandidate));", (String)null, 4, (Timing)null);
        src.addCodeLine("beam.put(validCandidate, candidates.get(validCandidate));", (String)null, 4, (Timing)null);
        src.addCodeLine("}", (String)null, 3, (Timing)null);
        src.addCodeLine("candidates = removeCandidate(candidates, validCandidate);", (String)null, 3, (Timing)null);
        src.addCodeLine("}", (String)null, 2, (Timing)null);
        src.addCodeLine("candidates.clear();", (String)null, 0, (Timing)null);
        src.addCodeLine("}", (String)null, 1, (Timing)null);
        src.addCodeLine("return 'Search Failed';", (String)null, 1, (Timing)null);
        src.addCodeLine("}", (String)null, 0, (Timing)null);
        this.language.nextStep("Java-Source-Ende");

        visibleGraph.show();

        String[] visitedNodes = new String[graph.getSize()];
        String[] candid = new String[graph.getSize()];
        String[] beam = new String[graph.getSize()];
        String[] heuristicVi = new String[graph.getSize()];

        //Collect Node Labels, beam and candidates and initialize them for visualization for conveniece for now / mb change
        for(int i = 0; i < graph.getSize(); ++i) {
            visitedNodes[i] = "   ";
            beam[i] = "  ";
            candid[i] = "  ";
            heuristicVi[i] = "(" + graph.getNodeLabel(graph.getNode(i)) + "=" + this.heuristic[i] + ")";
        }


        //Visualized Array of visited nodes
        StringArray visited = this.language.newStringArray(new Offset(10, 470, "sourceCode", "NW"), visitedNodes, "visited", (ArrayDisplayOptions)null, this.arrayProperties);
        this.language.newText(new Offset(-10, -16, "visited", "NW"), translator.translateMessage("beam_visitedHeading"), "visitedHeading", (DisplayOptions)null, this.textProps);
        //Visualized Array of candidates being considered
        StringArray candidates = this.language.newStringArray(new Offset(0, 55, "visited", "NW"), candid, "candidates", (ArrayDisplayOptions)null, this.arrayProperties);
        this.language.newText(new Offset(-10, -16, "candidates", "NW"), translator.translateMessage("beam_candidatesHeading"), "candidatesHeading", (DisplayOptions)null, this.textProps);
        //Visualized Array of the heuristic used
        StringArray heuristicVis = this.language.newStringArray(new Offset(0, -50, "vgraph", "NW"), heuristicVi , "heuristics", (ArrayDisplayOptions)null, this.arrayProperties);
        this.language.newText(new Offset(-10, -16, "heuristics", "NW"), translator.translateMessage("beam_heuristicHeading") + beamwidth + "Label=Heuristik: ", "heuristicHeading", (DisplayOptions)null, this.textProps);

        //Visualized Array of the nodes currently part of the beam
        StringArray beams = this.language.newStringArray(new Offset(0, 55, "candidates", "NW"), beam, "beam", (ArrayDisplayOptions)null, this.arrayProperties);
        this.language.newText(new Offset(-10, -16, "beam", "NW"), translator.translateMessage("beam_beamHeading"), "beamHeading", (DisplayOptions)null, this.textProps);


        Rect legendRect = this.language.newRect(new Offset(-10, 245, "vgraph", "NW"), new Offset(150, 160, "vgraph", "SE"), "legendRect", (DisplayOptions)null, this.rectProps);
        Text legendHeader = this.language.newText(new Offset(5, 5, "legendRect", "NW"), translator.translateMessage("beam_legend"), "legendHeader", (DisplayOptions)null, headerProps);
        SourceCode graphColorScheme = this.language.newSourceCode(new Offset(0, 20, "legendHeader", "NW"), "graphColorScheme", (DisplayOptions)null, this.sourceCodeProps);
        graphColorScheme.addCodeLine(translator.translateMessage("beam_legend_text1"), (String)null, 0, (Timing)null);
        graphColorScheme.addCodeLine(translator.translateMessage("beam_legend_text2"), (String)null, 1, (Timing)null);
        graphColorScheme.addCodeLine(translator.translateMessage("beam_legend_text3"), (String)null, 1, (Timing)null);
        graphColorScheme.addCodeLine(translator.translateMessage("beam_legend_text4"), (String)null, 1, (Timing)null);
        graphColorScheme.addCodeLine(translator.translateMessage("beam_legend_text5"), (String)null, 1, (Timing)null);
        this.language.nextStep("Done initialising - Algorithmus beginnt");

        MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel("multipleChoiceQuestion" + UUID.randomUUID());
        mcq.setPrompt(translator.translateMessage("beam_question1"));
        mcq.addAnswer(translator.translateMessage("beam_question1_answer1"), 1, translator.translateMessage("beam_question1_answer1_feedback"));
        mcq.addAnswer(translator.translateMessage("beam_question1_answer2"), 0, translator.translateMessage("beam_question1_answer2_feedback"));
        mcq.addAnswer(translator.translateMessage("beam_question1_answer3"), 0, translator.translateMessage("beam_question1_answer3_feedback"));

        mcq.setGroupID("First question group");
        this.language.addMCQuestion(mcq);
        this.language.nextStep();


        this.doBeamsearch(visibleGraph, this.heuristic, candidates, visited, beams, src);

        /** Abschließende Erklärung? vielleicht sowas:*/
        Rect fazitRect = this.language.newRect(new Offset(-50, 120, "graphColorScheme", "NW"), new Offset(580, 200, "graphColorScheme", "SE"), "fazitRect", (DisplayOptions)null, this.rectProps);
        Text fazitHeader = this.language.newText(new Offset(5, 5, "fazitRect", "NW"), "Fazit", "fazitHeader", (DisplayOptions)null, headerProps);
        this.language.newText(new Offset(0,  30, "fazitHeader", "NW"),  translator.translateMessage("beam_infobox1"), "infobox", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 15, "infobox", "NW"), translator.translateMessage("beam_infobox2")      , "infobox2", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 15, "infobox2", "NW"), translator.translateMessage("beam_infobox3")     , "infobox3", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 15, "infobox3", "NW"), translator.translateMessage("beam_infobox4")     , "infobox4", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 15, "infobox4", "NW"), translator.translateMessage("beam_infobox5")     , "infobox5", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 15, "infobox5", "NW"), translator.translateMessage("beam_infobox6")     , "infobox6", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 15, "infobox6", "NW"), translator.translateMessage("beam_infobox7")     , "infobox7", (DisplayOptions)null, this.textProps);

        this.language.nextStep("EndeDerAnimation");

    }

    /**
     * Returns a boolean value based on whether a node does equal the target node of a graph or uses the same Label
     * @param a the node
     * @param g the graph
     * @return whether it is equal
     */
    private boolean equalsTargetNode(Node a, Graph g){
        return a.equals(g.getTargetNode()) || g.getNodeLabel(g.getPositionForNode(a)).equals(g.getNodeLabel(g.getTargetNode()));
    }

    /**
     * Return all adjecant nodes of a given node in a given graph
     * @param g the graph
     * @param current the node
     * @return the adjecant nodes
     */
    private ArrayList<Node> getAdjecantNodes(Graph g, Node current) {
        ArrayList<Node> successors = new ArrayList<Node>();
        int[] currentEdges = g.getEdgesForNode(current);
        for(int i=0;i<currentEdges.length;i++) {
            if(currentEdges[i]==1 && !(g.getNode(i).equals(current) && !doVisited.containsKey(g.getNode(i)))) successors.add(g.getNode(i));
        }
        for(Node oups : doVisited.keySet())
            successors.remove(oups);

        return successors;
    }

    /** TODO vielleicht dynamische Farben
     * Highlights the found Path to the target node in green and prints the path using the node labels
     * @param candidate the goalnode
     * @param current the last node that was visited
     */
    private void describeFoundPath(/*HashMap<Node, Node> doVisited,*/ Node candidate, Node current, Graph g) {
        String pathText = "" + g.getNodeLabel(candidate);
        g.setNodeHighlightFillColor(candidate,Color.green,(Timing)null,(Timing)null);
        g.highlightNode(candidate,(Timing)null,(Timing)null);
        g.setEdgeHighlightPolyColor(current,candidate,Color.green,(Timing)null,(Timing)null);
        g.highlightEdge(current,candidate,(Timing)null,(Timing)null);
        Node ptr = current;
        while(doVisited.get(ptr)!= null) {

            pathText = g.getNodeLabel(ptr) + " -> " + pathText;
            g.setNodeHighlightFillColor(ptr,Color.green,(Timing)null,(Timing)null);
            g.setNodeHighlightTextColor(ptr,Color.BLACK,(Timing)null,(Timing)null);
            g.unhighlightNode(ptr,(Timing)null,(Timing)null);
            g.highlightNode(ptr,(Timing)null,(Timing)null);
            g.setEdgeHighlightPolyColor(ptr,doVisited.get(ptr),Color.green,(Timing)null,(Timing)null);
            g.highlightEdge(ptr,doVisited.get(ptr),(Timing)null,(Timing)null);
            ptr = doVisited.get(ptr);
        }
        g.setNodeHighlightFillColor(ptr,Color.green,(Timing)null,(Timing)null); //Root!?
        g.unhighlightNode(ptr,(Timing)null,(Timing)null);
        g.setNodeHighlightTextColor(ptr,Color.BLACK,(Timing)null,(Timing)null);
        g.highlightNode(ptr,(Timing)null,(Timing)null);
        pathText = g.getNodeLabel(ptr)+ " -> " + pathText;
        this.language.newText(new Offset(250, 0, "vgraph", "NW"), translator.translateMessage("beam_finished"), "finished", (DisplayOptions)null, this.textProps);
        this.language.newText(new Offset(0, 20, "finished", "NW"), pathText, "finished2", (DisplayOptions)null, this.textProps);
        MultipleChoiceQuestionModel mcq2 = new MultipleChoiceQuestionModel("multipleChoiceQuestion" + UUID.randomUUID());
        mcq2.setPrompt(translator.translateMessage("beam_question2"));
        mcq2.addAnswer(translator.translateMessage("beam_question2_answer1"), 1, translator.translateMessage("beam_question2_answer1_feedback"));
        mcq2.addAnswer(translator.translateMessage("beam_question2_answer2"), 0, translator.translateMessage("beam_question2_answer2_feedback"));
        mcq2.addAnswer(translator.translateMessage("beam_question2_answer3"), 1, translator.translateMessage("beam_question2_answer3_feedback"));

        mcq2.setGroupID("Second question group");
        this.language.addMCQuestion(mcq2);
        this.language.nextStep();

    }

    /**
     * Returns the node with the lowest heuristic value from a HashMap of candidates
     * @return the lowest-heuristical node
     */
    private Node getLowestHeuristic(/*HashMap<Node, Node> doCandidates,*/ Graph g, int[] heuristic) {
        Set<Node> findMin = doCandidates.keySet();
        Iterator<Node> iter = findMin.iterator();
        Node min = iter.next();
        Node current;
        while(iter.hasNext()){
            current = iter.next();
            if(current != null && min != null){
                if(heuristic[g.getPositionForNode(current)] < heuristic[g.getPositionForNode(min)]){
                    min = current;
                }
            }
        }
        return min;
    }

    private void doBeamsearch(Graph g, int[] heuristic, StringArray candidates, StringArray visited, StringArray beams, SourceCode src) {
        g.showNode(g.getStartNode(),(Timing)null,(Timing)null);
        src.highlight(0);
        language.nextStep();


        src.highlight(1);
        language.nextStep();

        doBeam = new HashMap<Node, Node>();
        doVisited = new HashMap<Node, Node>();
        doCandidates = new HashMap<Node, Node>();
        src.unhighlight(1);
        src.highlight(2);
        language.nextStep();

        doBeam.put(g.getStartNode(), null);
        src.unhighlight(2);
        src.highlight(3);
        beams.put(0, g.getNodeLabel(g.getStartNode()) + ",_" ,(Timing)null,(Timing)null);
        g.setNodeHighlightFillColor(g.getStartNode(),Color.ORANGE, (Timing)null,(Timing)null);
        g.highlightNode(g.getStartNode(),(Timing)null,(Timing)null);
        language.nextStep();

        doVisited.put(g.getStartNode(), null);
        src.unhighlight(3);
        src.highlight(4);
        visited.put(0,g.getNodeLabel(g.getStartNode()) + ",_",(Timing)null,(Timing)null);
        language.nextStep();

        src.highlight(6);
        src.unhighlight(4);
        language.nextStep();
        while (doBeam.size() != 0) {

            src.highlight(7);
            language.nextStep();
            for (Node current : doBeam.keySet()) {

                src.highlight(8);
                language.nextStep();
                for (Node candidate : this.getAdjecantNodes(g, current)) { // + Animation

                    src.highlight(9);
                    language.nextStep();
                    if (this.equalsTargetNode(candidate, g)) {


                        g.setNodeHighlightTextColor(candidate, Color.red, (Timing) null, (Timing) null); //target node red text
                        g.highlightNode(candidate,(Timing)null,(Timing)null);
                        language.nextStep();
                        doCandidates.clear();
                        for(Node remove : doCandidates.keySet()){
                            if(!remove.equals(candidate)){
                                g.unhighlightNode(remove,(Timing)null,(Timing)null);
                            }
                        }
                        this.describeFoundPath(/*doVisited,*/ candidate, current, g);
                        src.highlight(10);
                        language.nextStep();
                        g.setNodeHighlightTextColor(candidate, Color.BLACK, (Timing) null, (Timing) null); //target node red text
                        return;
                    }
                    src.unhighlight(10);
                    src.unhighlight(9);
                    src.highlight(11);
                    candidates.put(g.getPositionForNode(candidate),g.getNodeLabel(candidate),(Timing)null,(Timing)null);
                    g.setNodeHighlightFillColor(candidate,Color.MAGENTA,(Timing)null,(Timing)null);
                    g.highlightNode(candidate,(Timing)null,(Timing)null);
                    doCandidates.put(candidate, current); //TOdO Validate
                    language.nextStep();
                    src.unhighlight(11);
                }
            }
            src.unhighlight(11);
            src.unhighlight(8);
            src.unhighlight(7);
            src.highlight(14);
            if(beams.getLength() != 0){
                for(int i = 0;i<beams.getLength();i++){
                    beams.put(i," ",(Timing)null, (Timing)null);
                }
                for(Node nodes : doBeam.keySet()){
                    g.unhighlightNode(nodes,(Timing)null,(Timing)null);
                    g.setNodeHighlightFillColor(nodes,Color.GRAY,(Timing)null,(Timing)null); //no longer beam but visited
                    g.highlightNode(nodes,(Timing)null,(Timing)null);
                }
            }
            doBeam = new HashMap<Node, Node>(); /*Clear the beam to refill it since we processed all Nodes in old Beam*/
            language.nextStep();

            src.unhighlight(14);
            src.highlight(16);
            language.nextStep();

            while ((doCandidates.size() != 0) && (this.beamwidth > doBeam.size())) {

                src.highlight(17);
                Node validCandidate = this.getLowestHeuristic(g, heuristic);
                language.nextStep();

                src.highlight(18);
                src.unhighlight(17);
                language.nextStep();
                if (!doVisited.containsKey(validCandidate)) {

                    src.highlight(19);
                    g.unhighlightNode(validCandidate,(Timing)null,(Timing)null);
                    g.setNodeHighlightFillColor(validCandidate,Color.GRAY,(Timing)null,(Timing)null);
                    g.highlightNode(validCandidate,(Timing)null,(Timing)null);
                    visited.put(g.getPositionForNode(validCandidate),g.getNodeLabel(validCandidate) + "," + g.getNodeLabel(doCandidates.get(validCandidate)),(Timing)null,(Timing)null);
                    language.nextStep();


                    beams.put(g.getPositionForNode(validCandidate),g.getNodeLabel(validCandidate) + "," + g.getNodeLabel(doCandidates.get(validCandidate)),(Timing)null,(Timing)null);
                    src.unhighlight(19);
                    src.highlight(20);
                    g.unhighlightNode(validCandidate,(Timing)null,(Timing)null);
                    g.setNodeHighlightFillColor(validCandidate,Color.ORANGE,(Timing)null,(Timing)null);
                    g.highlightNode(validCandidate,(Timing)null,(Timing)null);
                    language.nextStep();

                    doVisited.put(validCandidate, doCandidates.get(validCandidate));
                    doBeam.put(validCandidate, doCandidates.get(validCandidate));
                }

                src.unhighlight(18);
                src.unhighlight(20);
                src.highlight(22);
                candidates.put(g.getPositionForNode(validCandidate)," ", (Timing)null,(Timing)null);
                language.nextStep();
                doCandidates.remove(validCandidate);
                src.unhighlight(22);
            }

            src.highlight(24);
            src.unhighlight(22);
            src.unhighlight(16);
            for(Node nodes : doCandidates.keySet()){
                candidates.put(g.getPositionForNode(nodes)," ",(Timing)null,(Timing)null);
            }
            language.nextStep();
            for(Node clear : doCandidates.keySet()) {
                g.unhighlightNode(clear,(Timing)null,(Timing)null);
            }
            doCandidates.clear();
            src.unhighlight(24);
        }
        src.unhighlight(6);
        src.highlight(26);
        this.language.newText(new Offset(250, 0, "vgraph", "NW"), translator.translateMessage("beam_errFinished"), "ErrFinished", (DisplayOptions)null, this.textProps);
        return;
    }

    public String getName() {
        return translator.translateMessage("beam_title");
    }

    public String getAlgorithmName() {
        return translator.translateMessage("beam_title");
    }

    public String getAnimationAuthor() {
        return "Kevin Kampa, Marcel Langer";
    }

    public String getDescription(){
        return translator.translateMessage("beam_description");
                /*"Der Beamsearch oder Strahlensuche Algorithmus ist ein Suchalgorithmus für Graphen, "
                +"\n"
                +"das grundsätzliche Vorgehen ist eine Abwandlung/Spezialisierung der Breadth-First Search."
                +"\n"
                +"Es wird jedoch auch eine Suchheuristik verwendet um entsprechend \"effektiver\" zu suchen"
                +"\n"
                +"und nicht alles zu besuchen womit enorm Speicherplatz gespart werden kann."
                +"\n"
                +"Hierbei dient der Parameter der Beamwidth oder Stahlenbreite als Obergrenze der besuchten "
                +"\n"
                +"Nachfolger jeder Iteration des Algorithmus. Der Algorithmus speichert die besuchten Knoten "
                +"\n"
                +"üblicherweise in einer Hashmap im Schema \" Knoten(vorherigerKnoten)\" somit kann der "
                +"\n"
                +"Algorithmus den gefundenen Pfad rekonstruieren. BeamSearch wird vorallem dann benutzt, "
                +"\n"
                +"wenn der zu durchsuchende Suchbaum enorm groß ist und eine BreadthFirstSearch somit "
                +"\n"
                +"nicht realistisch zu einem Ergebnis führen würde wegen dem enormen Speicherbedarf "
                +"\n"
                +"und Laufzeit. Im Gegensatz zur BreadthFirst Search ist jedoch keine optimale Lösung "
                +"\n"
                +"garantiert.";*/
    }


    public String getCodeExample() {
        return "private String beamsearch(Graph graph, int beamwidth, Node start,int heuristicGoal){ "
                + "\n"
                + " HashMap<Node, Node> beam, visited, candidates;"
                + "\n"
                + " beam = visited = candidates = new HashMap<Node, Node>();"
                + "\n"
                + " beam.put(start, null);"
                + "\n"
                + " visited.put(start, null);"
                + "\n"
                + " while (beam.size() != 0) {"
                + "\n"
                + "     for (Node current : beam.keySet()) {"
                + "\n"
                + "         for (Node candidate : graph.adjecantNodes(current)) {"
                + "\n"
                + "             if (candidate.heuristicValue == heuristicGoal)"
                + "\n"
                + "                 return describeFoundPath(visited, candidate, current);"
                + "\n"
                + "             candidates.put(candidate, current);"
                + "\n"
                + "         }"
                + "\n"
                + "     }"
                + "\n"
                + "     beam = new HashMap<Node, Node>();"
                + "\n"
                + "     while ((candidates.size() != 0) && (beamwidth > beam.size())) {"
                + "\n"
                + "         Node validCandidate = nextLowestHeuristic(candidates);"
                + "\n"
                + "         if (!visited.containsKey(validCandidate)) {"
                + "\n"
                + "             visited.put(validCandidate, candidates.get(validCandidate));"
                + "\n"
                + "             beam.put(validCandidate, candidates.get(validCandidate));"
                + "\n"
                + "         }"
                + "\n"
                + "         candidates = removeCandidate(candidates, validCandidate);"
                + "\n"
                + "     }"
                + "\n"
                + " }"
                + "\n"
                + " return \"Search Failed\";"
                + "}";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return this.loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
}
