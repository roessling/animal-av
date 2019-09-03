/*
 * ForceDirectedGraphDrawingAPI.java
 * Timm Welz, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.forcedirectedgraphdrawing.algo.AnimalSpringElectricalForces;
import generators.graph.forcedirectedgraphdrawing.algo.GUI;
import generators.graph.forcedirectedgraphdrawing.algo.TextDatabase;
import generators.graph.forcedirectedgraphdrawing.algo.Util;
import generators.graph.helpers.CoordinatesPatternBuilder;

import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;

public class ForceDirectedGraphDrawingAPI implements ValidatingGenerator {
    private Language lang;
    private double initialDelta;
    private int frameHeight;
    private int maxIterations;
    private int graphSize;
    private int[][] adjacencyMatrix;
    private int[][] initialCoordinates;
    private PolylineProperties repVector;
    private double threshold;
    private int numberOfExplainedIterations;
    private double optimalDistance;
    private PolylineProperties displacementVector;
    private int frameWidth;
    private int numberOfExplainedNodes;
    private boolean randomInitCoordinates;
    private double c_attr;
    private PolylineProperties attrVector;
    private SourceCodeProperties text;
    private boolean decreasingDelta;
    private double c_rep;
    private boolean randomAdjacencyMatrix;

    public void init(){
        lang = new AnimalScript("Force Directed Graph Drawing: Spring-Embedder", "Timm Welz", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        adjacencyMatrix = (int[][])primitives.get("adjacencyMatrix");
        initialCoordinates = (int[][])primitives.get("initialCoordinates");

        frameWidth = (Integer)primitives.get("frameWidth");
        frameHeight = (Integer)primitives.get("frameHeight");
        maxIterations = (Integer)primitives.get("maxIterations");
        graphSize = (Integer)primitives.get("graphSize");
        numberOfExplainedNodes = (Integer)primitives.get("numberOfExplainedNodes");
        numberOfExplainedIterations = (Integer)primitives.get("numberOfExplainedIterations");

        threshold = (double)primitives.get("threshold");
        optimalDistance = (double)primitives.get("optimalDistance");
        initialDelta = (double)primitives.get("initialDelta");
        c_attr = (double)primitives.get("c_attr");
        c_rep = (double)primitives.get("c_rep");

        decreasingDelta = (Boolean)primitives.get("decreasingDelta");
        randomAdjacencyMatrix = (Boolean)primitives.get("randomAdjacencyMatrix");
        randomInitCoordinates = (Boolean)primitives.get("randomInitCoordinates");

        displacementVector = (PolylineProperties)props.getPropertiesByName("displacementVector");
        repVector = (PolylineProperties)props.getPropertiesByName("repVector");
        attrVector = (PolylineProperties)props.getPropertiesByName("attrVector");
        text = (SourceCodeProperties)props.getPropertiesByName("text");

        lang.setStepMode(true);

        TextProperties titleProps = new TextProperties();
        titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 1, 32));

        MatrixProperties matrProbs = new MatrixProperties();
        MatrixProperties matrProbs2 = new MatrixProperties();
        matrProbs.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        matrProbs2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        matrProbs2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        matrProbs2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 15);
        SourceCodeProperties sourceCodeProperties = new SourceCodeProperties();
        sourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
        RectProperties bgProbs = new RectProperties();
        bgProbs.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        bgProbs.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        bgProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
        bgProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 15);
        GUI gui = new GUI(lang);
        gui.initTitle(titleProps);
        gui.showFirstPage(text);

        Coordinates[] pattern;
        int[][] adjM;

        if(randomAdjacencyMatrix)adjM = Util.getRandomAdjMatrix(graphSize);
        else{
            adjM = adjacencyMatrix;
            graphSize = adjacencyMatrix.length;
        }

        if(randomInitCoordinates)pattern = CoordinatesPatternBuilder.getRandomPattern(graphSize, 0, 0, frameWidth, frameHeight);
        else{
            pattern = new Coordinates[adjM.length];
            for (int i = 0; i < adjM.length; i++) {
                pattern[i]=new Coordinates(initialCoordinates[i][0], initialCoordinates[i][1]);
            }
        }


        String[] labels = new String[graphSize];
        for (int i = 0; i < graphSize; i++) {
            labels[i] = "q" + (i+1);
        }
        GraphProperties graphProperties = new GraphProperties();
        graphProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);



        gui.initAlgoCode(text,bgProbs );
        gui.setupFrame(frameWidth,frameHeight);
        gui.initStructure(graphProperties, labels, pattern, adjM);
        gui.initForceMatrix(matrProbs);
        gui.initInfoBox(maxIterations, threshold, c_rep, c_attr, optimalDistance, matrProbs);
        gui.initLowerValueBox(matrProbs2);
        AnimalSpringElectricalForces forces = new AnimalSpringElectricalForces(lang, gui, c_attr, c_rep, optimalDistance, displacementVector, repVector, attrVector, decreasingDelta);


        lang.nextStep("Algorithm Start");
        forces.runAlgo(maxIterations, threshold, initialDelta);
//        graph = lang.newGraph("aGraph", graph2D.getAdjacencyMatrix(), Util.pointsToNodes(graph2D.getPoints()), labels, null, graphProperties);
        lang.finalizeGeneration();

        return lang.toString();
    }

    public String getName() {
        return "Force Directed Graph Drawing: Spring-Embedder";
    }

    public String getAlgorithmName() {
        return "Force Directed Graph Drawing";
    }

    public String getAnimationAuthor() {
        return "Timm Welz";
    }

    public String getDescription(){
        return "Force Directed Graph Drawing algorithms are used to calculate aesthetically-\n" +
                "pleasing layouts for graphs.\n" +
                "Graphs drawn by these algorithms should e.g. have edges with nearly equal length,\n" +
                "and as few crossing edges as possible.\n \n"
                +"A group of algorithms, like the presented 'Spring Embedder'(Eades 1984), assign forces\n" +
                "to the edges and nodes and simulate motion, which should converge to a minimal energy state.\n \n"
                +"For this, 'Spring Embedder' uses logarithmic springs as attractive force between two adjacent nodes\n" +
                "\tf_spring(p_u,p_v) = c_spring * log( (||p_u - p_v||)/length ) * uv(p_v,p_u)\n" +
                "and repulsive force between two non-adjacent nodes\n" +
                "\tf_rep(p_u,p_v) = c_rep / (||p_u - p_v||²) * uv(p_v,p_u)\n" +
                "which are used to calculate the total force applied to a node.\n \n"
                + "C-attr and c-attr are given constants and 'length' denotes the ideal distance \n" +
                "between two adjacent nodes (spring-length).\n" +
                "delta(t) > 0 is multiplied with the total force, and either constant, or decreases\n" +
                "as a 'Temperature' over time in a linear fashion, to weaken the influence of later changes.\n" +
                "The algorithm terminates after a finite number of steps, or if the \n" +
                "current maximum force falls below a specific threshold.\n \n"
                +
                "According to Eades, experience has shown that\n" +
                "\tc_attr = 2,  c_rep = 1,  delta = 0.1 (constant or starting value)\n" +
                "are appropriate values for most graphs, and that the algorithm should terminate\n" +
                "in less than 100 steps."


                +"\n\n\nThis animation describes the algorithm in three phases.\n" +
                "First, for a specific number of nodes we will see how each attracting and\n" +
                "repulsive force is applied and thus the vector for the new position is calculated.\n \n"

                +"For the remaining nodes of an iteration (e.g. if we have 18 nodes and don't want\n" +
                "to visualize the calculation of the forces for all of them), only the displacement vectors are shown.\n" +
                "The length of phase 1 and 2 are configurable at 'Primitives'.\n \n"

                +"Finally, the remaining iterations show how the nodes move to their new layout over time." +
                "\nNOTE: In this phase, the Animation takes one extra step per iteration to avoid ANIMAL-malfunction regarding multiple node-transitions (as of ANIMAL 2.5.6 Patch 2018-08-17)" +
                "\n\nConstants and algorithm parameters are configurable at 'Primitives', as well as Graph information, which can be randomly-generated if needed." +
                "\n\n\nSources:" +
                "\n\t'Spring Embedders and Force Directed Graph Drawing Algorithms' " +
                "\n\t    by S.G.Kobourov, University of Arizona, 01.2012" +
                "\n\t 'Algorithms for Graph Visualization: Force-Directed Algorithms' " +
                "\n\t    by T.Mchedlidze, Karlsruhe Institute of Technology, 12.2016";
    }

    public String getCodeExample(){
        return         "t <- 1"
                +"\nwhile( t < K && max ||F_n(t)|| > threshold ) do"
                +"\n\tforeach Node n do"
                +"\n\t\tF_n(t) <- "
                +"\n\t\t//for all neighbours"
                +"\n\t\tsum f_attr(p_u, p_v) +"
                +"\n\t\t//for all non-neighbours"
                +"\n\t\tsum f_rep(p_u, p_v)"
                +"\n\tforeach Node n do"
                +"\n\t\tpos_n <- pos_n + delta(t)*F_n(t)"
                +"\n\tt++"

                +"\n\n\nf_attr(p_u, p_v) ="
                +TextDatabase.CONSTANT_ATTR+" * log( dist / "+TextDatabase.LENGTH+" ) * uv(p_u,p_v)"
                +"\n"
                +"\nf_rep(p_u, p_v) ="
                +TextDatabase.CONSTANT_REP+" / dist²  * uv(p_u,p_v)"
                +"\n"
                +"\n//dist = ||p_u - p_v||  and"
                +"\n//uv being the unitvector of two points";
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
        boolean result = true;
        adjacencyMatrix = (int[][])primitives.get("adjacencyMatrix");
        initialCoordinates = (int[][])primitives.get("initialCoordinates");

        frameWidth = (Integer)primitives.get("frameWidth");
        frameHeight = (Integer)primitives.get("frameHeight");
        maxIterations = (Integer)primitives.get("maxIterations");
        graphSize = (Integer)primitives.get("graphSize");
        numberOfExplainedNodes = (Integer)primitives.get("numberOfExplainedNodes");
        numberOfExplainedIterations = (Integer)primitives.get("numberOfExplainedIterations");

        threshold = (double)primitives.get("threshold");
        optimalDistance = (double)primitives.get("optimalDistance");
        initialDelta = (double)primitives.get("initialDelta");
        c_attr = (double)primitives.get("c_attr");
        c_rep = (double)primitives.get("c_rep");

        decreasingDelta = (Boolean)primitives.get("decreasingDelta");
        randomAdjacencyMatrix = (Boolean)primitives.get("randomAdjacencyMatrix");
        randomInitCoordinates = (Boolean)primitives.get("randomInitCoordinates");

        result = frameWidth>0 && frameHeight>0 && graphSize>0 && numberOfExplainedNodes>=0 && numberOfExplainedIterations >=0 && optimalDistance>0
                && adjacencyMatrix.length == initialCoordinates.length && adjacencyMatrix.length>0 && adjacencyMatrix[0].length == adjacencyMatrix.length
        && initialCoordinates[0].length==2;


        return result;
    }
}