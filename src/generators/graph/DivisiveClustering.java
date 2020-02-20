/*
 * DivisiveClustering.java
 * Aino Schwarte, David Steinmann, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import translator.Translator;

public class DivisiveClustering implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private CircleProperties clusterChangeColor;
    private PolylineProperties activeClusterColor;
    private int[][] nodes;
    private CircleProperties standardCircleColor;
    private CircleProperties activeNodeColor;

    
    // The probabilities for the three different question types, each between zero and one
    private double iterationProb;
    
    private double centerProb;
    
    private double clusterProb;
    
    private double nodeProb;
    
    /**
     * Tracks if there is already asked one question in the current iteration.
     */
    private boolean questionInIteration;
    
    private QuestionGroupModel iterationGroup;
    private QuestionGroupModel clusterGroup;
    private QuestionGroupModel centerGroup;
    private QuestionGroupModel nodeGroup;
    
    private int centerQuestionId = 0;
    private int nodeQuestionId = 0;
    
    
    private Text header;

    private Translator trans;
    
    private Random rand;

    /**
     * The standard text properties
     */
    private TextProperties textProps;

    /**
     * The properties for the polylines connecting the clusters
     */
    private PolylineProperties polyProps;

    /**
     * The highlight color of the active node
     */
    private Color highlightNodeColor;

    /**
     * The color for all nodes which change their cluster
     */
    private Color changingNodeColor;

    /**
     * The color to highlight the active cluster
     */
    private Color highlightClusterColor;

    /**
     * The standard color for all nodes
     */
    private Color standardNodeColor;
    /**
     * The list of all current clusters, starts with one cluster containing all
     * nodes.
     */
    private List<Cluster> clusters = new ArrayList<Cluster>();

    /**
     * The total number of nodes
     */
    private int numberOfNodes;

    /**
     * The background for the header
     */
    private Rect headerBackground;

    /**
     * This text holds information about the number of clusters and nodes at each
     * point of the animation
     */
    private Text clusterInformation;

    /**
     * The source code shown next to the animation
     */
    private SourceCode src;

    /**
     * The properties for the labels of the nodes
     */
    private TextProperties labelProps;

    public static void main(String[] args) {
        Generator generator = new DivisiveClustering();
        Animal.startGeneratorWindow(generator);
    }

    public DivisiveClustering() {
        trans = new Translator("resources/DivisiveClustering", Locale.GERMANY);
    }

    public void init() {
        lang = new AnimalScript("Divisive Clustering", "Aino Schwarte, David Steinmann", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }
    
    /**
     * Creates the initial description of the algorithm and the source code used
     * while executing. Calls the divisiveClustering method at the end.
     */
    public void start(int[][] nodes) {
        clusters = new ArrayList<Cluster>();
        
        NameBuilder.init();
        
        rand = new Random();

        // initialize the probabilities for the different question types:
        iterationProb = 0.6;
        clusterProb = 0.6;
        centerProb = 0.6;
        nodeProb = 0.6;
        // create the QuestionGroupModels for the different question types
        iterationGroup = new QuestionGroupModel("iteration", 3);
        clusterGroup = new QuestionGroupModel("cluster", 3);
        centerGroup = new QuestionGroupModel("center", 3);
        nodeGroup = new QuestionGroupModel("node", 3);
        
        lang.addQuestionGroup(iterationGroup);
        lang.addQuestionGroup(clusterGroup);
        lang.addQuestionGroup(centerGroup);
        lang.addQuestionGroup(nodeGroup);
        
        // Extract the colors for the circles and the polylines out of the properties.
        highlightNodeColor = (Color) activeNodeColor.get(AnimationPropertiesKeys.FILL_PROPERTY);
        highlightClusterColor = (Color) activeClusterColor.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        changingNodeColor = (Color) clusterChangeColor.get(AnimationPropertiesKeys.FILL_PROPERTY);
        standardNodeColor = (Color) standardCircleColor.get(AnimationPropertiesKeys.FILL_PROPERTY);

        // Create nodes
        labelProps = new TextProperties();
        labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        numberOfNodes = nodes[0].length;

        HashSet<MyNode> c1 = new HashSet<>();
        for (int i = 0; i < nodes[0].length; i++) {

            // create circles and names for the nodes
            String circleName = NameBuilder.getNextCircleName();

            Circle newPoint = lang.newCircle(new Coordinates(nodes[0][i], nodes[1][i]), 5, circleName, null,
                    standardCircleColor);
            Text newLabel = lang.newText(new Offset(5, 5, circleName, "C"), circleName, circleName + "label", null,
                    labelProps);

            c1.add(new MyNode(nodes[0][i], nodes[1][i], circleName, newPoint, newLabel));

            newPoint.hide();
            newLabel.hide();

        }

        clusters.add(new Cluster(c1));
        lang.nextStep(trans.translateMessage("cont1"));

        // Create the header and the background rectangle
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
        header = lang.newText(new Coordinates(20, 30), trans.translateMessage("title"), "header", null, headerProps);

        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        headerBackground = lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(10, 5, "header", "SE"), "hrect",
                null, rectProps);
        lang.nextStep();

        // description and pseudo code algorithm
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        /*
         * Gesamte Beschreibung: Das Divisive Clustering Verfahren ist ein Verfahren zum
         * Clustern von mehrdimensionalen Daten. Es wird zuerst davon ausgegangen, dass
         * alle Knoten zusammen in einen Cluster gehören und dieser wird dann nach und
         * nach aufgeteilt. Dadurch entsteht eine Cluster-Hierarchie. Je nachdem, wie
         * viele Cluster man sucht, kann man das Verfahren an der passenden Stelle
         * unterbrechen oder am Ende die passende Menge an Clustern aus der Hierarchie
         * entnehmen. Hier wird das Verfahren beispielhaft so lange ausgeführt, bis die
         * Cluster jeweils nur noch aus einem einzelnen Knoten bestehen.
         */
        
        /*
         * Whole Description:
         * Divisive Clustering is a procedure to cluster multidimensional data. The algorithm assumes, 
         * that initially all nodes belong to the same cluster, and it is continuously split up into 
         * smaller clusters. This way, the algorithm produces a hierarchy of clusters. Depending on how 
         * many clusters are searched, the procedure can be stopped at the right depth or the desired 
         * amount of clusters can be picked at the end. In this animation, the procedure is continued until
         * all remaining clusters consist only of a single node.
         */

        lang.newText(new Coordinates(10, 100), trans.translateMessage("desc1"), "desc1", null, textProps);
        lang.newText(new Offset(0, 25, "desc1", "NW"), trans.translateMessage("desc2"), "desc2", null, textProps);
        lang.newText(new Offset(0, 25, "desc2", "NW"), trans.translateMessage("desc3"), "desc3", null, textProps);
        lang.newText(new Offset(0, 25, "desc3", "NW"), trans.translateMessage("desc4"), "desc4", null, textProps);
        lang.newText(new Offset(0, 25, "desc4", "NW"), trans.translateMessage("desc5"), "desc5", null, textProps);
        lang.newText(new Offset(0, 25, "desc5", "NW"), trans.translateMessage("desc6"), "desc6", null, textProps);

        lang.nextStep();

        lang.newText(new Offset(0, 50, "desc6", "NW"), trans.translateMessage("algo1"), "algo1", null, textProps);
        lang.newText(new Offset(0, 25, "algo1", "NW"), trans.translateMessage("algo2"), "algo2", null, textProps);
        lang.newText(new Offset(0, 25, "algo2", "NW"), trans.translateMessage("algo3"), "algo3", null, textProps);
        lang.newText(new Offset(0, 25, "algo3", "NW"), trans.translateMessage("algo4"), "algo4", null, textProps);
        lang.newText(new Offset(18, 25, "algo4", "NW"), trans.translateMessage("algo5"), "algo5", null, textProps);
        lang.newText(new Offset(-18, 25, "algo5", "NW"), trans.translateMessage("algo6"), "algo6", null, textProps);
        lang.newText(new Offset(18, 25, "algo6", "NW"), trans.translateMessage("algo7"), "algo7", null, textProps);
        lang.newText(new Offset(-18, 25, "algo7", "NW"), trans.translateMessage("algo8"), "algo8", null, textProps);

        lang.nextStep();

        // hide the starting information, show the used source code

        lang.hideAllPrimitives();
        header.show();
        headerBackground.show();

        src = lang.newSourceCode(new Coordinates(30, 100), "srcCode", null, sourceCode);
        src.addCodeLine(trans.translateMessage("code1"), null, 0, null);
        src.addCodeLine(trans.translateMessage("code2"), null, 2, null);
        src.addCodeLine(trans.translateMessage("code3"), null, 2, null);
        src.addCodeLine(trans.translateMessage("code4"), null, 2, null);
        src.addCodeLine(trans.translateMessage("code5"), null, 4, null);
        src.addCodeLine(trans.translateMessage("code6"), null, 2, null);

        // explanation
        lang.newText(new Coordinates(600, 25), trans.translateMessage("leg1"), "leg1", null, textProps);
        lang.newText(new Offset(0, 25, "leg1", "NW"), trans.translateMessage("leg2"), "leg2", null, textProps);
        lang.newText(new Offset(0, 25, "leg2", "NW"), trans.translateMessage("leg3"), "leg3", null, textProps);

        Circle lc1 = lang.newCircle(new Offset(130, 15, "leg2", "NE"), 5, "legC1", null, standardCircleColor);
        Circle lc2 = lang.newCircle(new Offset(0, 25, "legC1", "C"), 5, "legC2", null, standardCircleColor);
        lc1.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlightNodeColor, Timing.INSTANTEOUS,
                Timing.INSTANTEOUS);
        lc2.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, changingNodeColor, Timing.INSTANTEOUS,
                Timing.INSTANTEOUS);

        lang.nextStep(trans.translateMessage("cont2"));

        // Start Algorithm
        divisiveClustering();
    }

    /**
     * Execute and display the main algorithm
     */
    private void divisiveClustering() {

        src.highlight(0);

        // Add information about the current clusters
        lang.newText(new Coordinates(30, 250), trans.translateMessage("leg4"), NameBuilder.getNextListName(), null,
                textProps);
        lang.newText(new Offset(0, 25, NameBuilder.getCurrentListName(), "NW"), createClusterList(),
                NameBuilder.getNextListName(), null, textProps);

        clusterInformation = lang.newText(new Coordinates(30, 80), createClusterInfo(), "clusterInfo", null, textProps);

        // Create the properties for the cluster polylines:
        polyProps = new PolylineProperties();
        polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        polyProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
        polyProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
        polyProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

        // Show all nodes and labels, create polylines for all clusters
        clusters.forEach((Cluster c) -> {
            c.showAll();
            createPolyLineForCluster(c);
        });

        lang.nextStep();

        // As long as there are less clusters than nodes - i.e. it exists at least one
        // cluster which contains more than one node
        int iterationCounter = 0;
        
        while (clusters.size() < numberOfNodes) {
            src.unhighlight(5);
            src.highlight(1);
            
            questionInIteration = false;
            
            if (!questionInIteration && (rand.nextFloat() <= iterationProb) ) {
                
                questionInIteration = true;
                
                int iterations = numberOfNodes - iterationCounter - 1;
                
                FillInBlanksQuestionModel q = new FillInBlanksQuestionModel("iteration" + iterationCounter);
                q.setGroupID("iteration");
                q.setPrompt(trans.translateMessage("q4"));
                q.addAnswer("" + iterations, 1, trans.translateMessage("a4t"));

                lang.addFIBQuestion(q);
                
                // update the probability for iteration questions this is done independent of the answer of the question
                iterationProb -= 0.2;
            }

            clusters.forEach((Cluster c) -> c.getNodes()
                    .forEach((MyNode m) -> m.getCircle().changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
                            standardNodeColor, Timing.INSTANTEOUS, Timing.INSTANTEOUS)));

            lang.nextStep(trans.translateMessage("cont3") + iterationCounter);

            iterationCounter++;
            
            // Determine the cluster with the highest diameter
            double maxClusterDiameter = 0;
            Cluster chosenCluster = null;

            Iterator<Cluster> it = clusters.iterator();
            List<String> questionAnswers = new ArrayList<String>();
            String correctAnswer = "";

            boolean askClusterSelectionQuestion = true;
            
            while (it.hasNext()) {
                Cluster c = it.next();
                questionAnswers.add(createClusterString(c));

                double dia = c.getDiameter();
                if (dia > maxClusterDiameter) {
                    if (dia > maxClusterDiameter*1.2) {
                        askClusterSelectionQuestion = true;
                    }
                    else {
                        askClusterSelectionQuestion = false;
                    }
                    chosenCluster = c;
                    maxClusterDiameter = dia;
                    correctAnswer = createClusterString(c);
                }
            }
            
            // ask a cluster selection question
            if (!questionInIteration && askClusterSelectionQuestion && rand.nextFloat() <= clusterProb && clusters.size() > 1) {
                
                questionInIteration = true;
                
                MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("cluster" + iterationCounter);
                q.setPrompt(trans.translateMessage("q1"));
                q.setGroupID("cluster");
                
                // add the answers to the question
                Iterator<String> answerIt = questionAnswers.iterator();
                while (answerIt.hasNext()) {
                    String s = answerIt.next();
                    if (s.equals(correctAnswer))
                        q.addAnswer(s, 1, trans.translateMessage("a1t"));
                    else
                        q.addAnswer(s, 0, trans.translateMessage("a1f"));
                }
                
                lang.addMCQuestion(q);
                
                clusterProb -= 0.2;
            }

            chosenCluster.getLine().changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlightClusterColor,
                    Timing.INSTANTEOUS, Timing.MEDIUM);
            // Determine the outlier

            lang.nextStep();

            src.unhighlight(1);
            src.highlight(2);

            MyNode outlier = determineOutlier(chosenCluster);

            lang.nextStep();

            outlier.getCircle().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, changingNodeColor,
                    Timing.INSTANTEOUS, Timing.FAST);

            lang.nextStep();
            src.unhighlight(2);
            src.highlight(3);
            src.highlight(4);

            lang.nextStep();
            // Determine if any points of the old cluster belong to the new one

            Set<MyNode> changingNodes = new HashSet<MyNode>();

            changingNodes.addAll(getClusterChangingNodes(chosenCluster, outlier));

            changingNodes.add(outlier);
            // language.nextStep();
            src.unhighlight(3);
            src.unhighlight(4);
            src.highlight(5);

            Cluster newCluster = new Cluster(changingNodes);

            chosenCluster.removeNodes(changingNodes);

            createPolyLineForCluster(newCluster);

            createPolyLineForCluster(chosenCluster);

            clusters.add(newCluster);

            // sort clusters by their size
            clusters.sort((Cluster c1, Cluster c2) -> {
                if (c1.size() > c2.size())
                    return 1;
                if (c1.size() == c2.size())
                    return 0;
                else
                    return -1;
            });

            lang.newText(new Offset(0, 20, NameBuilder.getCurrentListName(), "NW"), createClusterList(),
                    NameBuilder.getNextListName(), null, textProps);

            clusterInformation.setText(createClusterInfo(), Timing.INSTANTEOUS, Timing.INSTANTEOUS);

            lang.nextStep();
        }

        src.unhighlight(0);
        src.unhighlight(4);
        lang.nextStep();
        finish();
    }

    /**
     * Display a summary after the algorithm has finished. Also hides all the
     * previously used elements.
     */
    private void finish() {
        lang.hideAllPrimitives();
        header.show();
        headerBackground.show();

        lang.newText(new Coordinates(10, 100), trans.translateMessage("end1"), "end1", null, textProps);
        lang.newText(new Offset(0, 25, "end1", "NW"), trans.translateMessage("end2"), "end2", null, textProps);
        lang.newText(new Offset(0, 25, "end2", "NW"), trans.translateMessage("end3"), "end3", null, textProps);
        lang.newText(new Offset(0, 25, "end3", "NW"), trans.translateMessage("end4"), "end4", null, textProps);
        lang.newText(new Offset(0, 25, "end4", "NW"), trans.translateMessage("end5"), "end5", null, textProps);
        lang.newText(new Offset(0, 25, "end5", "NW"), trans.translateMessage("end6"), "end6", null, textProps);
        lang.newText(new Offset(0, 50, "end6", "NW"), trans.translateMessage("end7"), "end7", null, textProps);
        lang.newText(new Offset(0, 25, "end7", "NW"), trans.translateMessage("end8"), "end8", null, textProps);
        lang.newText(new Offset(0, 25, "end8", "NW"), trans.translateMessage("end9"), "end9", null, textProps);
        lang.newText(new Offset(0, 25, "end9", "NW"), trans.translateMessage("end10"), "end10", null, textProps);
        lang.newText(new Offset(0, 50, "end10", "NW"), trans.translateMessage("end11"), "end11", null, textProps);

        lang.nextStep(trans.translateMessage("cont4"));
        /*
         * Gesamter Text: Dies war ein Beispiel für die Divisive Clustering Procedure
         * (DIANA) von Kaufmann und Rousseuw (1990). Dieses Verfahren bietet eine
         * alternative zu den vielen agglomorativen Verfahren zum Clustering, welche die
         * Cluster nach und nach aus den einzelnen Punkten aufbauen. Es gibt viele
         * Möglichkeiten, den genauen Ablauf des Algorithmus anzupassen. So kann man zum
         * Beispiel die Abbruchbedingung des Verfahrens anpassen, indem man entweder nur
         * nach einer gewissen Anzahl von Clustern sucht (sofern die Zahl der gesuchten
         * Clustern bekannt ist) oder indem man eine minimale Größe für Cluster
         * festlegt.
         * 
         * Weiterer Spielraum existiert bei der Berechnungen von Distanzen mehreren
         * Datenpunkten. In diesem Beispiel wurde der Durchmesser eines Clusters als
         * maximale Distanz zwischen zwei Punkten des Clusters berechnet, es sind aber
         * auch andere Berechnungen möglich. Mit einer passenden Distanzfunktion lässt
         * sich dieser Ansatz auch auf mehrdimensionale Daten übertragen.
         * 
         * Weitere Informationen gibt es zum Beispiel unter:
         * https://de.wikipedia.org/wiki/Hierarchische_Clusteranalyse
         */
        
        /*
         * Whole text:
         * This was an example for the Divisive Clustering Procedure (DIANA) of Kaufmann and Rousseuw (1990).
         * This method is an alternative to many agglomerative clustering procedures, which combine points to
         * create the clusters. There are many possibilities to adjust this algorithm. For example, it is
         * possible to add an explicit stopping criteria, such as a maxmimum number of created clusters or a
         * minimal size for all clusters.
         * 
         * Further variations are possible at the way distances between data points are measured. In this
         * example, the diameter of a cluster is the maximum distance between any two points in the cluster,
         * but there are also other methods possible. With a suitable distance function, it is easily possible
         * to adapt this algorithm for multidimensional data.
         * 
         * More information is available for example at:
         * https://en.wikipedia.org/wiki/Hierarchical_clustering
         */
        
        // finalize generation for interactive questions
        lang.finalizeGeneration();
    }

    /**
     * Create a new polyline for the given cluster, connecting all points with the
     * center of the cluster.
     * 
     * @param c cluster to create a polyline for
     * @return the polyline for the cluster
     */
    private Polyline createPolyLineForCluster(Cluster c) {

        List<Node> nodeCenters = new ArrayList<Node>();
        StringBuilder polyName = new StringBuilder();

        c.getNodes().forEach((MyNode n) -> nodeCenters.add(n.getCircle().getCenter()));

        Node polyCenter = c.getCenter();
        Node[] polyLineNodes = new Node[c.size() * 2];

        for (int i = 0; i < c.size(); i++) {
            polyLineNodes[2 * i] = nodeCenters.get(i);
            polyLineNodes[2 * i + 1] = polyCenter;
        }

        Polyline polyLine = lang.newPolyline(polyLineNodes, polyName.toString(), null, polyProps);
        c.updateLine(polyLine);

        return polyLine;

    }

    /**
     * Determine which node of the cluster is the "outlier" - i.e. which node has
     * the maximum summed distance to all other nodes of the cluster
     * 
     * @param cluster the cluster
     * @return the node with the maximum summed distance to all other nodes of the
     *         cluster
     */
    private MyNode determineOutlier(Cluster cluster) {
        // Determine if a question concerning the outlier of a cluster should be asked
        boolean askOutlierQuestion = true;

        if (cluster.size() == 1) {
            askOutlierQuestion = false; 
            return cluster.getNodes().toArray(new MyNode[0])[0];
        }

        MyNode outlier = null;
        double currentMaxDistance = 0;

        Iterator<MyNode> outerIt = cluster.getNodes().iterator();
        Iterator<MyNode> innerIt;

        double currentDistance = 0;
        
        List<String> answers = new ArrayList<String>();

        // Check all nodes and remember the one with the highest summed distance
        while (outerIt.hasNext()) {

            innerIt = cluster.getNodes().iterator();
            MyNode currentNode = outerIt.next();

            // add the distance to all other nodes
            while (innerIt.hasNext()) {
                currentDistance += currentNode.calcDistance(innerIt.next());
            }

            if (currentDistance >= currentMaxDistance) {
                // set askOutlierQuestion true if it is at least 20 percent larger than the previous maximal distance.
                if (currentDistance <= 1.2*currentMaxDistance) {
                    askOutlierQuestion = false;
                }
                else {
                    askOutlierQuestion = true;
                }
                currentMaxDistance = currentDistance;
                outlier = currentNode;
            }
            answers.add(currentNode.name);
            currentDistance = 0;
        }
        
        // ask a question about the center of the new cluster
        
        if (!questionInIteration && cluster.size() > 2 && askOutlierQuestion && rand.nextFloat() <= centerProb) {
            
            questionInIteration = true;
            MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("center" + centerQuestionId);
            centerQuestionId++;

            String correctAnswer = outlier.getName();
            
            q.setPrompt(trans.translateMessage("q2"));
            q.setGroupID("center");
            
            
            Iterator<String> answerIt = answers.iterator();
            while(answerIt.hasNext()) {
                String s = answerIt.next();
                
                if(s.equals(correctAnswer))
                    q.addAnswer(correctAnswer, 1, trans.translateMessage("a2t"));
                else
                    q.addAnswer(s, 0, trans.translateMessage("a2f"));   
            }
            
            lang.addMCQuestion(q);
            
            centerProb -= 0.2;
        }
        
        
        
        
        
        return outlier;
    }

    /**
     * Return a list of the current clusters as String. Clusters are grouped by {}
     * and contain the name of their nodes. Sort the nodes in the clusters by their
     * name.
     * 
     * @return list of the current clusters
     */
    private String createClusterList() {

        StringBuilder result = new StringBuilder();

        clusters.forEach((Cluster c) -> {
            result.append(createClusterString(c));
        });

        // Remove the last two chars ("," and " ")
        result.deleteCharAt(result.length() - 1);
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }
    
    
    /**
     * For the given cluster, create a String with the following scheme:
     * {n1, n2, n3, n4, ..., nx} if n1 to nx are the nodes of the cluster.
     * @param c the cluster to create a string for
     * @return a string containing all nodes of the cluster in curly braces
     */
    private String createClusterString(Cluster c) {
        
        StringBuilder result = new StringBuilder();
        result.append("{");

        List<MyNode> clusterList = new ArrayList<MyNode>();
        clusterList.addAll(c.getNodes());
        clusterList.sort((MyNode n1, MyNode n2) -> {
            // get the numbers of the two nodes
            int num1 = Integer.parseInt(n1.getName().substring(1));
            int num2 = Integer.parseInt(n2.getName().substring(1));

            if (num1 > num2)
                return 1;
            if (num1 == num2)
                return 0;
            else
                return -1;
        });
        
        clusterList.forEach((MyNode n) -> result.append(n.getName()).append(", "));

        // Remove the last two chars ("," and " ")
        result.deleteCharAt(result.length() - 1);
        result.deleteCharAt(result.length() - 1);
        result.append("}, ");
        
        return result.toString();
    }

    /**
     * Return the String which has the information about the current amount of
     * clusters and nodes.
     * 
     * @return current amount of clusters and nodes
     */
    private String createClusterInfo() {
        StringBuilder result = new StringBuilder();

        result.append("Cluster: ").append(clusters.size());
        result.append("     Knoten: ").append(numberOfNodes);

        return result.toString();

    }

    /**
     * Determine which nodes of the given cluster need to change to the new cluster.
     * A node changes, if the average distance between him and all other points of
     * the old cluster (except the center of the new cluster) is higher than the
     * distance to the center of the new cluster.
     * 
     * @param cluster the cluster (includes the center of the new cluster)
     * @param center  the center of the new cluster
     * @return list of all nodes which need to switch to the new cluster
     */
    private List<MyNode> getClusterChangingNodes(Cluster cluster, MyNode center) {

        List<MyNode> result = new ArrayList<MyNode>();

        Iterator<MyNode> outerIt = cluster.getNodes().iterator();
        Iterator<MyNode> innerIt;

        double distToCenter;
        double averageDistance = 0;

        // Iterate over all nodes and check any needs to switch the cluster
        while (outerIt.hasNext()) {

            MyNode currentNode = outerIt.next();

            // if the current node is the center, ignore it
            if (currentNode.equals(center))
                continue;

            distToCenter = currentNode.calcDistance(center);
            innerIt = cluster.getNodes().iterator();

            // Calculate the average distance to all other nodes except the center
            while (innerIt.hasNext()) {

                MyNode comparisionNode = innerIt.next();

                // If the comparisionNode is the center, ignore it, otherwise add the distance
                // to the node to the averageDistance
                if (comparisionNode.equals(center))
                    continue;
                else
                    averageDistance += currentNode.calcDistance(comparisionNode);

            }

            // Average Distance now holds the sum of all the distances to all other nodes of
            // the cluster, except the center

            averageDistance = averageDistance / (cluster.size() - 2);
            // color the node yellow to highlight it
            currentNode.getCircle().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlightNodeColor,
                    Timing.INSTANTEOUS, Timing.FAST);

            // If the average distance to the center is less or equal than the average
            // distance to all other nodes, add this node to the new cluster
            
            if ((distToCenter*1.2 <= averageDistance || distToCenter >= averageDistance*1.2) && !questionInIteration && rand.nextFloat() <= nodeProb) {
                
                questionInIteration = true;
                MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("node" + nodeQuestionId);
                nodeQuestionId++;
                q.setPrompt(trans.translateMessage("q3"));
                q.setGroupID("node");
                                
                if  (distToCenter <= averageDistance) {
                    q.addAnswer(trans.translateMessage("true"), 0, trans.translateMessage("a3f1"));
                    q.addAnswer(trans.translateMessage("false"), 1, trans.translateMessage("a3t1"));
                }
                else {
                    q.addAnswer(trans.translateMessage("true"), 1, trans.translateMessage("a3t2"));
                    q.addAnswer(trans.translateMessage("false"), 0, trans.translateMessage("a3f2"));
                }
                  
                lang.addMCQuestion(q);
                
                nodeProb -= 0.2;
            }
           
            lang.nextStep();
            
            if (distToCenter <= averageDistance) {
                result.add(currentNode);
                // color the node red to show that it belongs to the new cluster
                currentNode.getCircle().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, changingNodeColor,
                        Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            } else {
                // color the nodes green to show that it didn't change the cluster
                currentNode.getCircle().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, standardNodeColor,
                        Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            }
            lang.nextStep();

            averageDistance = 0;
        }

        return result;
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
        clusterChangeColor = (CircleProperties) props.getPropertiesByName("clusterChangeColor");
        activeClusterColor = (PolylineProperties) props.getPropertiesByName("activeClusterColor");
        nodes = (int[][]) primitives.get("nodes");
        standardCircleColor = (CircleProperties) props.getPropertiesByName("standardCircleColor");
        activeNodeColor = (CircleProperties) props.getPropertiesByName("activeNodeColor");

        start(nodes);

        return lang.toString();
    }

    public String getName() {
        return "Divisive Clustering";
    }

    public String getAlgorithmName() {
        return "Divisive Clustering (DIANA)";
    }

    public String getAnimationAuthor() {
        return "Aino Schwarte, David Steinmann";
    }

    public String getDescription() {
        if ( trans == null) 
            System.out.println("bl");
        String result = trans.translateMessage("pd1") + "\n"
                + trans.translateMessage("pd2") + "\n"
                + trans.translateMessage("pd3") + "\n"
                + trans.translateMessage("pd4") + "\n"
                + trans.translateMessage("pd5") + "\n"
                + trans.translateMessage("pd6") + "\n";
        return result;
    }

    public String getCodeExample() {
        return trans.translateMessage("pc1") + "\n"
                + trans.translateMessage("pc2") + "\n"
                + trans.translateMessage("pc3") + "\n" 
                + trans.translateMessage("pc4") + "\n"
                + trans.translateMessage("pc5") + "\n"
                + trans.translateMessage("pc6") + "\n"
                + trans.translateMessage("pc7");
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

// Inner classes used for the animation

    /**
     * A class which holds all information about a node of the graph.
     * 
     * @author David Steinmann
     *
     */
    private class MyNode {

        private int x;
        private int y;
        private String name;
        private Circle circ;
        private Text label;

        /**
         * Create a new MyNode with the given properties
         * 
         * @param x     x-coordinate
         * @param y     y-coordinate
         * @param name  name
         * @param circ  circle object used to display the nodes
         * @param label label object used to display the name of the node
         */
        public MyNode(int x, int y, String name, Circle circ, Text label) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.circ = circ;
            this.label = label;
        }

        /**
         * Return the name of the node
         * 
         * @return name of the node
         */
        public String getName() {
            return name;
        }

        /**
         * Return the circle of the node - its graphical representation
         * 
         * @return circle of the node
         */
        public Circle getCircle() {
            return circ;
        }

        /**
         * Return the label of the node - the graphical representation of its name
         * 
         * @return the label of the node
         */
        public Text getLabel() {
            return label;
        }

        /**
         * Return the x-coordinate of the node
         * 
         * @return x-coordinate of the node
         */
        public int getX() {
            return x;
        }

        /**
         * Return the y-coordinate of the node
         * 
         * @return y-coordinate of the node
         */
        public int getY() {
            return y;
        }

        /**
         * Two nodes are equal if they have the same coordinates
         * 
         * @param cmp the object to compare with this node
         * @return true if the nodes have the same coordinates, false otherwise
         */
        public boolean equals(MyNode cmp) {
            return this.x == cmp.getX() && this.y == cmp.getY();
        }

        /**
         * Return the euclidean distance of this node to the given node
         * 
         * @param n2 the node to calculate the distance
         * @return the distance between this node and the given node
         */
        public double calcDistance(MyNode n2) {
            return Math.sqrt((Math.pow(x - n2.getX(), 2) + Math.pow(y - n2.getY(), 2)));
        }
    }

    /**
     * A cluster is a set of nodes connected by a Polyline. The nodes are
     * represented by a set of MyNode.
     * 
     * @author David Steinmann
     *
     */
    private class Cluster {

        private Set<MyNode> nodes;
        private Polyline line;
        private Double diameter;
        private Node center;

        /**
         * Create a new cluster with the given nodes
         * 
         * @param nodes nodes of the new cluster
         */
        public Cluster(Set<MyNode> nodes) {
            this.nodes = nodes;
            diameter = 0.0;

            updateDiameterAndCenter();

        }

        /**
         * Return the nodes of this cluster
         * 
         * @return the nodes of the cluster
         */
        public Set<MyNode> getNodes() {
            return nodes;
        }

        /**
         * Set all circles and labels of the nodes in this cluster visible.
         */
        public void showAll() {
            nodes.forEach((MyNode n) -> {
                n.getCircle().show();
                n.getLabel().show();
            });
        }

        /**
         * Hide the current Line of the cluster if not null, afterwards overwrite it
         * with the new line.
         * 
         * @param newLine the new line for the cluster
         * @param hideOld
         */
        public void updateLine(Polyline newLine) {
            if (!(line == null))
                line.hide();

            line = newLine;
        }

        /**
         * Return the polyline conneccting all points of the cluster
         * 
         * @return the polyline of the cluster
         */
        public Polyline getLine() {
            return line;
        }

        /**
         * Remove the given set of nodes from the cluster, update center and diameter
         * afterwards. (This does not modify the polyline of this cluster by itself).
         * 
         * @param s nodes to be removed
         */
        public void removeNodes(Set<MyNode> s) {
            nodes.removeAll(s);
            updateDiameterAndCenter();
        }

        /**
         * Return the diameter of the cluster, i.e. the maximum distance between any
         * pair of nodes.
         */
        public double getDiameter() {
            return diameter;
        }

        /**
         * Return the center of the cluster, i.e. a Node which has the average x and y
         * coordiantes of all nodes of the cluster
         * 
         * @return the center of the cluster
         */
        public Node getCenter() {
            return center;
        }

        /**
         * Return the number of nodes in the cluster
         * 
         * @return the number of nodes in the cluster
         */
        public int size() {
            return nodes.size();
        }

        /**
         * Updates the diameter and the center of the cluster.
         */
        private void updateDiameterAndCenter() {
            MyNode[] clusterCopy = nodes.toArray(new MyNode[0]);
            double currentDistance = 0;

            diameter = 0.0;
            // calculate the cluster diameter
            for (int i = 0; i < clusterCopy.length; i++) {
                for (int j = i + 1; j < clusterCopy.length; j++) {
                    currentDistance = clusterCopy[i].calcDistance(clusterCopy[j]);
                    if (currentDistance > diameter)
                        diameter = currentDistance;
                }
            }

            // calculate the cluster center
            double polyCenterX = nodes.stream().mapToDouble((MyNode m) -> m.getX()).average().getAsDouble();
            double polyCenterY = nodes.stream().mapToDouble((MyNode m) -> m.getY()).average().getAsDouble();
            center = Coordinates.convertToNode(new Point((int) polyCenterX, (int) polyCenterY));
        }

    }

    /**
     * Static class which allows it to get names for all elements, which are used
     * multiple times, but where the exact number depends on the graph used.
     * 
     * @author David Steinmann
     */
    private static class NameBuilder {

        private static int circleCount = 0;
        private static String currentCircleName = "n0";
        private static int listCount = 0;
        private static String currentListName = "l0";
        
        public static void init() {
            circleCount = 0;
            listCount = 0;
        }

        /**
         * Return a name for the next node. The names consists of a "n" and a number
         * which is incremented for each node.
         * 
         * @return Name for the next circle
         */
        public static String getNextCircleName() {

            currentCircleName = "n" + circleCount;
            circleCount++;
            return currentCircleName;

        }

        /**
         * Return a name for the next list of clusters. The name consists of an "l" and
         * a number which is incremented for each node.
         * 
         * @return name for the next list of clusters
         */
        public static String getNextListName() {

            currentListName = "l" + listCount;
            listCount++;
            return currentListName;
        }

        /**
         * Return the name for the current list (is used for offset calculations)
         * 
         * @return the name of the current list
         */
        public static String getCurrentListName() {
            return currentListName;
        }
    }

}