/*
 * KargersAlgorithm.java
 * Hannah Drews, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.*;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;

import algoanim.primitives.generators.Language;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import generators.graph.kargersHelpers.KargersEdge;
import generators.graph.kargersHelpers.KargersSubset;
import interactionsupport.models.MultipleChoiceQuestionModel;

@SuppressWarnings({"unused","unchecked","rawtypes"})
public class KargersAlgorithm implements Generator {
    private Language lang;
    private PolylineProperties polylineProps;
    private TextProperties headerProps;
    private TextProperties textProps;
    private SourceCodeProperties sourceCodeProps;
    private RectProperties rectProps;
    private SourceCode code;
    private Text header;
    private Text introText;
    private Graph graph;
    private Locale locale;
    private HashMap<String, String> text;

    Node[] nodeList;
    private int coordinateX, coordinateY; //Coordinates of the user-input graph to translate them into our graph

    KargersSubset testAnimalSet = new KargersSubset();

    public KargersEdge[] edgeArray; // array to store all edges
    private int cutEdges;
    private int startContract = 0;
    private int endContract = 0;

    
    public KargersAlgorithm() {
        this(Locale.US);
    }

    public KargersAlgorithm(Locale language) {
        //including multiple languages
        this.locale = language;
        this.text = new HashMap();
        if (this.locale == Locale.GERMANY || this.locale == Locale.GERMAN) {
            this.text.put("firstmulti0", "Warum kann es sein, dass Karger’s Algorithmus nicht immer den minimalen Schnitt berechnet?");
            this.text.put("answer0_0", "Weil er die Kanten zufällig auswählt");
            this.text.put("answer0_1", "Weil falsche Kanten ausgewählt werden können");
            this.text.put("answer0_2", "Weil der Algorithmus nicht alle Kanten berücksichtigt");
            this.text.put("answer0_3", "Weil die Kanten nicht zufällig ausgewählt werden");
            this.text.put("feedback0_0", "Richtig! Weil die Kanten zufällig ausgewählt werden, kann nicht immer ein minimales Ergebnis garantiert werden..");
            this.text.put("feedback0_1", "Falsch. Die ausgewählten Kanten sind nicht falsch, sondern lediglich zufällig.");
            this.text.put("feedback0_2", "Falsch. Der Algorithmus berücksichtigt alle Kanten, er wählt jedoch zufällig die nächste Kante aus.");
            this.text.put("feedback0_3", "Falsch. Gerade weil die Kanten zufällig ausgewählt werden, kann es sein, dass das Ergebnis nicht immer minimal ist.");

            this.text.put("secondmulti1", "Wie wird ein Graph genannt, bei dem zwischen zwei Knoten mehrere Kanten liegen können?");
            this.text.put("answer1_0", "Wald");
            this.text.put("answer1_1", "Multigraph");
            this.text.put("answer1_2", "Tree");
            this.text.put("answer1_3", "zusammenhängender Graph");
            this.text.put("feedback1_0", "Falsch. Ein solcher Graph wird Multigraph genannt.");
            this.text.put("feedback1_1", "Richtig!");
            this.text.put("feedback1_2", "Falsch. Ein solcher Graph wird Multigraph genannt.");
            this.text.put("feedback1_3", "Falsch. Ein solcher Graph wird Multigraph genannt.");

            this.text.put("lastmulti2", "Wie wird ein Algorithmus genannt, der mit einer gewissen Wahrscheinlichkeit falsche Ergebnisse liefert?");
            this.text.put("answer2_0", "Atlantic City");
            this.text.put("answer2_1", "Monte Carlo");
            this.text.put("answer2_2", "Las Vegas");
            this.text.put("answer2_3", "San Antonio");
            this.text.put("feedback2_0", "Falsch. Ein solcher Algorithmus wird Monte Carlo Algorithmus genannt.");
            this.text.put("feedback2_1", "Richtig!");
            this.text.put("feedback2_2", "Falsch. Ein solcher Algorithmus wird Monte Carlo Algorithmus genannt.");
            this.text.put("feedback2_3", "Falsch. Ein solcher Algorithmus wird Monte Carlo Algorithmus genannt.");

            this.text.put("description", "Karger's algorithm for Minimum Cut sucht in einem ungerichteten zusammenhängenden Graphen den minimalen Schnitt. " +
                    "Er kontrahiert dabei so lange zufällig ausgewählte Kanten, bis nur noch zwei Knoten übrig sind. \t\t" +
                    "Kontrahieren bedeutet, dass zwei Knoten vereinigt werden und die Kante dazwischen entfernt wird. \t" +
                    "Alle Self-Loops, die hierbei entstehen, werden entfernt. Alle verschobenen Kanten bleiben bestehen. \t\t" +
                    "Die übrigen Kanten, die an dem entfernten Knoten dranhingen, gehen nun von dem neuen, vereinigten Knoten aus. \t" +
                    "Die Summe der übrig gebliebenen Kanten zwischen den letzten zwei Knoten wird als Schnitt bezeichnet. \t\t" +
                    "Anwendung findet der Algorithmus zum Beispiel beim Testen von Netzwerken, um eventuelle Schwächen zu beheben und deren Sicherheit zu erhöhen. \t" +
                    "Ein weiteres Anwendungsbeispiel ist die Bildverarbeitung. Hier wird der Algorithmus zur Segmentierung des Bildes verwendet, \t" +
                    "um benachbarte Pixel mit denselben Eigenschaften (Farbe, Textur, Dichte) zusammenzufassen, \t" +
                    "damit die Weiterverarbeitung und Analyse des Bildes einfacher und effizienter wird. \t");

            this.text.put("description1", "Karger's algorithm for Minimum Cut sucht in einem ungerichteten zusammenhängenden Graphen den minimalen Schnitt. ");
            this.text.put("description2", "Er kontrahiert dabei so lange zufällig ausgewählte Kanten, bis nur noch zwei Knoten übrig sind. ");
            this.text.put("description3", ""); //Zeilenumbruch
            this.text.put("description4", "Kontrahieren bedeutet, dass zwei Knoten vereinigt werden und die Kante dazwischen entfernt wird. ");
            this.text.put("description5", "Alle Self-Loops, die hierbei entstehen, werden entfernt. Alle verschobenen Kanten bleiben bestehen. ");
            this.text.put("description6", ""); //Zeilenumbruch
            this.text.put("description7", "Die übrigen Kanten, die an dem entfernten Knoten dranhingen, gehen nun von dem neuen, vereinigten Knoten aus. ");
            this.text.put("description8", "Die Summe der übrig gebliebenen Kanten zwischen den letzten zwei Knoten wird als Schnitt bezeichnet. ");
            this.text.put("description9", ""); //Zeilenumbruch
            this.text.put("description10", "Anwendung findet der Algorithmus zum Beispiel beim Testen von Netzwerken, um eventuelle Schwächen zu beheben und deren Sicherheit zu erhöhen. ");
            this.text.put("description11", "Ein weiteres Anwendungsbeispiel ist die Bildverarbeitung. Hier wird der Algorithmus zur Segmentierung des Bildes verwendet, ");
            this.text.put("description12", "um benachbarte Pixel mit denselben Eigenschaften (Farbe, Textur, Dichte) zusammenzufassen, ");
            this.text.put("description13", "damit die Weiterverarbeitung und Analyse des Bildes einfacher und effizienter wird. ");


            this.text.put("introDescription1", "Karger’s Algorithmus erzeugt zufällig den minimalen Schnitt eines zusammenhängenden, ungerichteten Graphen. ");
            this.text.put("introDescription2", "Er basiert auf der Überlegung der Kontraktion von zwei Knoten u und v in einen Knoten. ");
            this.text.put("introDescription3", "Dadurch wird die gesamte Anzahl der Knoten nach und nach reduziert, bis nur noch zwei Knoten übrig sind. ");
            this.text.put("introDescription4", "Die Kanten der kontrahierten Knoten werden dabei vom Ergebnisknoten zu den restlichen Knoten neu angefügt. ");
            this.text.put("introDescription5", "Dabei können auch mehrere Kanten zwischen zwei Knoten liegen, wobei solch ein Graph Multigraph genannt wird. ");
            this.text.put("introDescription6", "Karger’s Algorithmus kontrahiert so lange zufällig ausgewählte Kanten, bis nur noch zwei Knoten übrig sind. ");
            this.text.put("introDescription7", "Die Summe der übrig gebliebenen Kanten zwischen den letzten zwei Knoten wird als Schnitt bezeichnet. ");
            this.text.put("introDescription8", "Karger’s Algorithmus arbeitet dabei mit zufällig ausgewählten Kanten, weshalb das Ergebnis nicht immer der minimale Schnitt sein muss. ");
            this.text.put("introDescription9", "Diese Art von Algorithmen wird auch Monte Carlo Algorithmus genannt. ");

            this.text.put("results", "Karger's Minimum Cut ist: ");
            this.text.put("res1", "Das bedeutet, auf der letzten Kante, die du siehst, liegen ");
            this.text.put("res2", " Kanten aufeinander.");
        }
        else {
            this.text.put("firstmulti0", "Why can it be that Karger's algorithm does not always calculate the minimum cut?");
            this.text.put("answer0_0", "Because it randomly selects the edges.");
            this.text.put("answer0_1", "Because wrong edges can be selected.");
            this.text.put("answer0_2", "Because the algorithm does not take all edges into account.");
            this.text.put("answer0_3", "Because the edges are not selected randomly.");
            this.text.put("feedback0_0", "Correct! Because the edges are randomly selected, a minimum result cannot always be guaranteed.");
            this.text.put("feedback0_1", "Wrong. The selected edges are not wrong, just random.");
            this.text.put("feedback0_2", "Wrong. The algorithm takes all edges into account, but randomly selects the next one.");
            this.text.put("feedback0_3", "Wrong. Just because the edges are randomly selected, it is possible that the result is not always minimal.");

            this.text.put("secondmulti1", "What is a graph called in which several edges can lie between two nodes?");
            this.text.put("answer1_0", "forest");
            this.text.put("answer1_1", "multigraph");
            this.text.put("answer1_2", "tree");
            this.text.put("answer1_3", "connected graph");
            this.text.put("feedback1_0", "Wrong. Such a graph is called a multigraph.");
            this.text.put("feedback1_1", "Correct!");
            this.text.put("feedback1_2", "Wrong. Such a graph is called a multigraph.");
            this.text.put("feedback1_3", "Wrong. Such a graph is called a multigraph.");

            this.text.put("lastmulti2", "What is an algorithm called that is likely to produce false results?");
            this.text.put("answer2_0", "Atlantic City");
            this.text.put("answer2_1", "Monte Carlo");
            this.text.put("answer2_2", "Las Vegas");
            this.text.put("answer2_3", "San Antonio");
            this.text.put("feedback2_0", "Wrong. Such an algorithm is called Monte Carlo algorithm.");
            this.text.put("feedback2_1", "Correct!");
            this.text.put("feedback2_2", "Wrong. Such an algorithm is called Monte Carlo algorithm.");
            this.text.put("feedback2_3", "Wrong. Such an algorithm is called Monte Carlo algorithm.");

            this.text.put("description1", "Karger's algorithm for Minimum Cut computes a minimum cut of a connected graph. ");
            this.text.put("description2", "The idea is based on the concept of contraction of an edge in an undirected graph.");
            this.text.put("description3", ""); //Zeilenumbruch
            this.text.put("description4", "The concept of contraction means that two nodes unite and the edge inbetween gets deleted. ");
            this.text.put("description5", "Every resulting self-loop gets deleted as well. ");
            this.text.put("description6", ""); //Zeilenumbruch
            this.text.put("description7", "Every remaining edge gets moved such that it is now connected between the united node and all the other nodes that originally were connected to the deleted one. ");
            this.text.put("description8", "The sum of all remaining edges inbetween the last two nodes is called the 'cut'. ");
            this.text.put("description9", ""); //Zeilenumbruch
            this.text.put("description10", "Minimum Cut algorithms are useful for testing network structures. It finds weaknesses in connected network nodes and therefore improves its security.");
            this.text.put("description11", "Another example for using such algorithms is image processing. Here it is used for image segmentation ");
            this.text.put("description12", "to unite connected pixels with the same characteristics like color, texture and/or density. ");
            this.text.put("description13", "This makes the analysis and further processing of the image much easier. ");

            this.text.put("introDescription1", "Karger's algorithm for Minimum Cut computes a minimum cut of a connected graph. ");
            this.text.put("introDescription2", "The idea is based on the concept of contraction of an edge in an undirected graph.");
            this.text.put("introDescription3", "This way the number of nodes is reduced one by one, until only two nodes are left. ");
            this.text.put("introDescription4", "The edges of the contracted nodes are reattached to the remaining nodes. ");
            this.text.put("introDescription5", "It is possible that there are multiple edges between two nodes, therefore such a graph is called multigraph. ");
            this.text.put("introDescription6", "The algorithm conctracts randomly selected edges as long as there are only two nodes left. ");
            this.text.put("introDescription7", "The sum of the remaining edges between the last two nodes is called the 'cut'. ");
            this.text.put("introDescription8", "Karger's algorithm works with a random selection generator, which is why the result does not always have to be the minimum cut. ");
            this.text.put("introDescription9", "This kind of algorithm is called Monte Carlo algorithm. ");

            this.text.put("results", "Karger's Minimum Cut is: ");
            this.text.put("res1", "That means, on the last edge you see are ");
            this.text.put("res2"," edges on top of each other.");
        }
    }

    public void init(){
        this.lang = new AnimalScript("Karger's algorithm for Minimum Cut", "Hannah Drews, Yves Geib", 800, 600);
        this.lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {


        this.lang.setInteractionType(1024);
        //this.createGraph();

        this.textProps = (TextProperties)props.getPropertiesByName("textProps");
        this.sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        this.graph = (Graph)primitives.get("graph");

        this.intro();
        this.kargersMinCut();
        this.lang.finalizeGeneration();
        return this.lang.toString();
    }

    private void intro() {
        headerProps = new TextProperties();
        headerProps.set("font", new Font("SansSerif", 1, 24));
        header = this.lang.newText(new Coordinates(250, 30), "Karger's algorithm for Minimum Cut", "header", (DisplayOptions)null, headerProps);
        rectProps = new RectProperties();
        rectProps.set("fillColor", Color.CYAN);
        rectProps.set("filled", true);
        rectProps.set("depth", 2);
        lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "hrect", (DisplayOptions)null, rectProps);
        //sourceCodeProps.set("font", new Font("SansSerif", 0, 16));
        //sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        code = this.lang.newSourceCode(new Offset(-150, 30, header, "SW"), "intro", (DisplayOptions)null, sourceCodeProps);
        code.addCodeLine((String)this.text.get("description1"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description2"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description3"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description4"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description5"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description6"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description7"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description8"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description9"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description10"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description11"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description12"), "intro", 0, null);
        code.addCodeLine((String)this.text.get("description13"), "intro", 0, null);
        lang.nextStep("intro");
        code.hide();
    }

    public int kargersMinCut() {
        int i; //for for-loop
        int j; //for for-loop

        this.nodeList = this.graph.getNodes();

        int firstNodePosition = this.graph.getPositionForNode(nodeList[0]); //Needed for the offset for the code, such that code and graph do not overlap. //

        //sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

        code = lang.newSourceCode(new Coordinates(firstNodePosition + 500, 200), "sourceCode", null, sourceCodeProps);
        code.addCodeLine("while (nodes > 2)", null, 1, null);
        code.addCodeLine("choose random edge (u,v) from graph", null, 2, null);
        code.addCodeLine("merge u and v", null, 3, null);
        code.addCodeLine("reattach other edges //lines might lay on top of each other", null, 3, null);
        code.addCodeLine("remove self-loops", null, 3, null);
        code.addCodeLine("return cut represented by two nodes", null, 2, null);

        code.highlight(0);

        int[][] inputMatrix = this.graph.getAdjacencyMatrix();
        int[][] testMatrix = new int[inputMatrix.length][inputMatrix.length];
        int[][] transponierteMatrix = new int[inputMatrix.length][inputMatrix.length];
        int edgeCounter = 0; //this stores the value of edges in the input-Graph

        //Printe testMatrix, die vom User in Animal übergeben wurde
        //
        for(i = 0; i < inputMatrix.length; i++) {
            for (j = 0; j < inputMatrix.length; j++) {
                if(inputMatrix[i][j] == 1)
                    edgeCounter++;
            }
        }

        //Transponiere testMatrix
        for(i = 0; i < inputMatrix.length; i++) {
            for (j = 0; j < inputMatrix.length; j++) {
                transponierteMatrix[j][i] = inputMatrix[i][j];
            }
        }
        //Füge transponierte Matrix in testMatrix ein
        for(i = 0; i < testMatrix.length; i++) {
            for (j = 0; j < testMatrix.length; j++) {
                testMatrix[i][j] = inputMatrix[i][j] + transponierteMatrix[i][j];
            }
        }


        // get given Graph
        int nrOfVertices = this.graph.getSize();
        int nrOfEdges = edgeCounter;

        //fill edgeArray with given Graph
        edgeArray = new KargersEdge[nrOfEdges];
        int y = 0; //edgeArray Counter
        // Filling the edgeArray with the Matrix Input to let KargersAlgorithm execute with the Input Graph
        for (i = 0; i < inputMatrix.length; i++) {
            for (j = 0; j < inputMatrix.length; j++) {

                if (inputMatrix[i][j] == 1) {
                    edgeArray[y] = new KargersEdge(i, j);
                    y++;
                }
            }
        }


        textProps.set("font", new Font("SansSerif", 0, 12));
        CircleProperties cp = new CircleProperties();
        cp.set("color", Color.BLACK);
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        cp.set("depth", 2);
        polylineProps = new PolylineProperties();
        polylineProps.set("color", Color.BLACK);
        polylineProps.set("depth", 3);

        //Erstelle Platz für Primitives
        Text[] textArray = new Text[testMatrix.length];
        Circle[] circleArray = new Circle[testMatrix.length];

        //Erstelle Polyline Matrix, um Polylines an korrekten Stellen zu zeichnen
        Polyline[][] polyMatrix = new Polyline[testMatrix.length][testMatrix.length];

        /*Create Graph with coordinates and nodeLabels from user input
          0A 1B
          2C 3D
        */
        // this.nodeList = this.graph.getNodes(); //this gets done above because of Pseudo-Code-Position, but is mainly used for this loop.
        int z = 0;
        for (Node n : nodeList) {

            this.coordinateX = ((Coordinates) n).getX();
            this.coordinateY = ((Coordinates) n).getY();

            //create Text of node
            textArray[z] = lang.newText(new Coordinates(coordinateX, coordinateY), this.graph.getNodeLabel(n), "node " + this.graph.getNodeLabel(n), null, textProps);

            //create Circle around Text
            circleArray[z] = lang.newCircle(new Offset(0, 0, textArray[z], AnimalScript.DIRECTION_C), 30, "circle " + this.graph.getNodeLabel(n), null, cp);

            z++;
        }

//        System.out.print("\nNodes verbu/nden mit Edges:\n");
        //polyMatrix wird an allen Stellen der testMatrix gezeichnet  versehen, an denen in testMatrix[i][j] eine 0 steht.
        for (i = 0; i < testMatrix.length; i++) {
            for (j = 0; j < testMatrix.length; j++) {
                if (testMatrix[i][j] == 1) {
                    // wir können verhindern, dass wir nicht mehr Wissen welche Edge zu welchen Nodes gehört, indem wir einfach das Array mit Null füllen wo keine Edges sind dann ist Anzahl Plätze in der Matrix = Länge des Arrays
                    polyMatrix[i][j] = lang.newPolyline(new Node[]{new Offset(0, 0, textArray[i], AnimalScript.DIRECTION_C), new Offset(0, 0, textArray[j], AnimalScript.DIRECTION_C)}, "Edge " + textArray[i] + "-" + textArray[j] + "after being contracted", null, polylineProps);
//                    System.out.println(textArray[i].getText() + " " + textArray[j].getText());
                }
            }
        }
        lang.nextStep("Drawing graph");

        // allocate memory for creating i subsets
        KargersSubset[] subset = new KargersSubset[nrOfVertices];

        // create i subsets of single elements
        for (i = 0; i < nrOfVertices; i++) {
            subset[i] = new KargersSubset();
            subset[i].parent = i;
            subset[i].rank = 0;
        }
        // here should be a single edge...
        Random r = new Random();

        // initially there are nrOfVertices in given Graph
        int vertices = nrOfVertices;

        // graph is contracted until there are two vertices left
        while (vertices > 2) {
            // generates a random int between 0 and nrOfEdges
            int x = r.nextInt(nrOfEdges);
            // find vertices (sets) of current randomly picked edge
            int subset1 = testAnimalSet.find(subset, edgeArray[x].src);
            int subset2 = testAnimalSet.find(subset, edgeArray[x].dest);

            code.unhighlight(0);
            code.highlight(1);

            // if the vertices belong to the same subset, this edge is not considered
            // also if the vertices are already contracted, they should not be considered.
            if (subset1 == subset2) {
                continue;
            }
            // else contract the edge (combine the subsets and combine the nodes of the edge into one)
            else {

                startContract = subset1;
                endContract = subset2;
//                System.out.println("\nContracting edge" + edgeArray[x].src + edgeArray[x].dest);

//                System.out.println("startContract: " + startContract + " endContract: " + endContract);

                //Highlighten der Polyline, die entfernt werden soll.
                //Wenn an beiden Stellen in der Matrix eine Eins steht, highlighte nur eine Edge, ansonsten highlighte die Edge, die in der Matrix vorhanden ist.
                /*
                if (polyMatrix[startContract][endContract] != null && polyMatrix[endContract][startContract] != null)
                    polyMatrix[startContract][endContract].changeColor("color", Color.RED, null, null);
                    polyMatrix[endContract][startContract].hide();
                if (polyMatrix[startContract][endContract] != null && polyMatrix[endContract][startContract] == null)
                    polyMatrix[startContract][endContract].changeColor("color", Color.RED, null, null);
                if (polyMatrix[endContract][startContract] != null && polyMatrix[startContract][endContract] == null)
                    polyMatrix[endContract][startContract].changeColor("color", Color.RED, null, null);
                */

                if (polyMatrix[startContract][endContract] != null && polyMatrix[endContract][startContract] != null)
                    polyMatrix[startContract][endContract].changeColor("color", Color.RED, null, null);

                if (polyMatrix[startContract][endContract] != null)
                    polyMatrix[startContract][endContract].changeColor("color", Color.RED, null, null);

                if (polyMatrix[endContract][startContract] != null)
                    polyMatrix[endContract][startContract].changeColor("color", Color.RED, null, null);

                lang.nextStep("Highlight Polyline that is about to be cut");

                // highlighten der beiden Nodes, die zusammengeführt werden, allerdings aktuell nur für den zweiten Cut
                // 
                // dynamisch machen, damit alle Cuts gehighlightet werden (Das geschieht dann mit Verschieben des Codes in die whileSchleife der kargersMinCut Methode
                textArray[startContract].changeColor("color", Color.RED, null, null);
                circleArray[startContract].changeColor("color", Color.RED, null, null);
                textArray[endContract].changeColor("color", Color.RED, null, null);
                circleArray[endContract].changeColor("color", Color.RED, null, null);

                lang.nextStep("Highlighten nodes that are about to be contracted");

                code.unhighlight(1);
                code.highlight(2);
                code.highlight(3);
                code.highlight(4);

                //Hide den betroffenen Node
                textArray[endContract].hide();
                circleArray[endContract].hide();

                //Ausgelagert: Benennung des neuen Nodes (Code-Refactoring)
                String nodename = textArray[startContract].getText() + " " + textArray[endContract].getText();
//                System.out.println("cut: " + nodename);

                //Färbe den contracteten Node wieder schwarz und füge den entfernten Node hinzu (Also Node A wird zu Node A B mit .setText)
                //textArray[startContract].setText(nodename, null, null);
                textArray[startContract].hide();
                textArray[startContract] = this.lang.newText(this.graph.getNode(startContract), nodename, "node: " + nodename, null, textProps);

                //create new Circle around new Text so that it is centered
                if (nodename.length() > 2) {
                    circleArray[startContract].hide();
                    circleArray[startContract] = this.lang.newCircle(new Offset(0, 0, textArray[startContract], AnimalScript.DIRECTION_C), 30, "circle " + nodename, null, cp);
                    //circleArray[startContract].show();
                }
                if (nodename.length() > 4) {
                    circleArray[startContract].hide();
                    circleArray[startContract] = this.lang.newCircle(new Offset(0, 0, textArray[startContract], AnimalScript.DIRECTION_C), 35, "circle " + nodename, null, cp);
                    //circleArray[startContract].show();
                }
                if (nodename.length() > 6) {
                    circleArray[startContract].hide();
                    circleArray[startContract] = this.lang.newCircle(new Offset(0, 0, textArray[startContract], AnimalScript.DIRECTION_C), 40, "circle " + nodename, null, cp);
                    //circleArray[startContract].show();
                }
                if (nodename.length() > 8) {
                    circleArray[startContract].hide();
                    circleArray[startContract] = this.lang.newCircle(new Offset(0, 0, textArray[startContract], AnimalScript.DIRECTION_C), 45, "circle " + nodename, null, cp);
                    //circleArray[startContract].show();
                }
                if (nodename.length() > 10) {
                    circleArray[startContract].hide();
                    circleArray[startContract] = this.lang.newCircle(new Offset(0, 0, textArray[startContract], AnimalScript.DIRECTION_C), 50, "circle " + nodename, null, cp);
                    //circleArray[startContract].show();
                }
                textArray[startContract].changeColor("color", Color.BLACK, null, null);
                circleArray[startContract].changeColor("color", Color.BLACK, null, null);


                polylineProps.set("color", Color.GREEN);

                //Hide all lines that are attached to endContract (here the value j in polyMatrix)
                for (j = 0; j < polyMatrix.length; j++) {
                    int[] rememberedNodes = new int[polyMatrix.length]; //Erstelle int-Array für das Speichern aller Nodes, die an dem contracteten Node dranhängen. Damit können nachher die neuen Lines gezeichnet werden.

                    //Befülle rememberedNodes mit den Nodes, die an dem contracteten Node dranhängen, damit später die Polylines zu diesen Nodes gezeichnet werden können.
                    if (startContract != j) { //Schaue nur die Nodes an, die an dem contracteten Node dranhängen. Alle anderen werden ignoriert.

                        // Hide and delete last line which could still be visible from previous iteration.
                        if (polyMatrix[startContract][endContract] != null) {
                            polyMatrix[startContract][endContract].hide();
                            polyMatrix[startContract][endContract] = null;
                        }
                        if (polyMatrix[endContract][startContract] != null) {
                            polyMatrix[endContract][startContract].hide();
                            polyMatrix[endContract][startContract] = null;
                        }

                        //Hier geschieht das Hiden, oberes Dreieck der Matrix
                        if (polyMatrix[endContract][j] != null) {
                            rememberedNodes[j] = j; //Merken der Knoten, zu denen die Lines gezeichnet werden.
                            polyMatrix[endContract][j].hide();
                            polyMatrix[endContract][j] = null;

                            //Wenn an beiden symmetrischen Stellen in der Matrix eine eins steht, dann...
                            if (polyMatrix[rememberedNodes[j]][startContract] != null && polyMatrix[startContract][rememberedNodes[j]] != null) {

                                //...hide beide und...
                                polyMatrix[startContract][rememberedNodes[j]].hide();
                                polyMatrix[rememberedNodes[j]][startContract].hide();

                                //...zeichne beide mit korrekten Koordinaten neu.
                                polyMatrix[rememberedNodes[j]][startContract] = lang.newPolyline(new Node[]{new Offset(0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C),
                                        new Offset(0, 0, circleArray[startContract], AnimalScript.DIRECTION_C)}, "newLine", null, polylineProps);
                                polyMatrix[startContract][rememberedNodes[j]] = lang.newPolyline(new Node[]{new Offset(0, 0, circleArray[startContract], AnimalScript.DIRECTION_C),
                                        new Offset(0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null, polylineProps);
                            }

                            //Wenn an nur jeweils einer Stelle in der Matrix eine eins steht, dann...
                            if (polyMatrix[startContract][rememberedNodes[j]] != null) {

                                //...hide die obere polyline und...
                                polyMatrix[startContract][rememberedNodes[j]].hide();

                                //zeichne sie mit korrekten Koordinaten neu.
                                polyMatrix[startContract][rememberedNodes[j]] = lang.newPolyline(new Node[]{new Offset(0, 0, circleArray[startContract], AnimalScript.DIRECTION_C),
                                        new Offset(0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null, polylineProps);
                            }

                            //Wenn an beiden Stellen eine 0 steht, dann...
                            if (polyMatrix[rememberedNodes[j]][startContract] == null && polyMatrix[startContract][rememberedNodes[j]] == null) {

                                //...zeichne eine neue polyline and die obere Stelle der Matrix.
                                polyMatrix[startContract][rememberedNodes[j]] = lang.newPolyline(new Node[]{new Offset(0, 0, circleArray[startContract], AnimalScript.DIRECTION_C),
                                        new Offset(0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null, polylineProps);
                            }

                        }

                        //Unteres Dreieck der Matrix
                        if (polyMatrix[j][endContract] != null) {
                            rememberedNodes[j] = j; //Merken der Knoten, zu denen die Lines gezeichnet werden.
                            polyMatrix[j][endContract].hide();
                            polyMatrix[j][endContract] = null;

                            //Wenn an beiden symmetrischen Stellen in der Matrix eine eins steht, dann...
                            if (polyMatrix[rememberedNodes[j]][startContract] != null && polyMatrix[startContract][rememberedNodes[j]] != null) {

                                //...hide beide und...
                                polyMatrix[startContract][rememberedNodes[j]].hide();
                                polyMatrix[rememberedNodes[j]][startContract].hide();

                                //...zeichne beide mit korrekten Koordinaten neu.
                                polyMatrix[rememberedNodes[j]][startContract] = lang.newPolyline(new Node[]{new Offset(0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C),
                                        new Offset(0, 0, circleArray[startContract], AnimalScript.DIRECTION_C)}, "newLine", null, polylineProps);
                                polyMatrix[startContract][rememberedNodes[j]] = lang.newPolyline(new Node[]{new Offset(0, 0, circleArray[startContract], AnimalScript.DIRECTION_C),
                                        new Offset(0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null, polylineProps);
                            }

                            //Wenn an nur jeweils einer Stelle in der Matrix eine eins steht, dann...
                            if (polyMatrix[rememberedNodes[j]][startContract] != null) {

                                //...hide die untere polyline und...
                                polyMatrix[rememberedNodes[j]][startContract].hide();

                                //zeichne sie mit korrekten Koordinaten neu.
                                polyMatrix[rememberedNodes[j]][startContract] = lang.newPolyline(new Node[]{new Offset(0, 0, circleArray[startContract], AnimalScript.DIRECTION_C),
                                        new Offset(0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null, polylineProps);
                            }

                            //Wenn an beiden Stellen eine 0 steht, dann...
                            if (polyMatrix[rememberedNodes[j]][startContract] == null && polyMatrix[startContract][rememberedNodes[j]] == null) {

                                //...zeichne eine neue polyline and die untere Stelle der Matrix.
                                polyMatrix[rememberedNodes[j]][startContract] = lang.newPolyline(new Node[]{new Offset(0, 0, circleArray[startContract], AnimalScript.DIRECTION_C),
                                        new Offset(0, 0, circleArray[rememberedNodes[j]], AnimalScript.DIRECTION_C)}, "newLine", null, polylineProps);
                            }

                        }
                    }
                }
                lang.nextStep("Restart loop");

                code.unhighlight(2);
                code.unhighlight(3);
                code.unhighlight(4);

//                System.out.println("Contracting subsets" + subset[edgeArray[x].src].parent + subset[edgeArray[x].dest].parent + "\n");

                // number of Vertices is one less
                vertices--;
                testAnimalSet.union(subset, subset1, subset2);
            }
        }

        // there are now two subsets left in the contracted graph
        // so the results are the edges between the components
        cutEdges = 0;

        for (i = 0; i < nrOfEdges; i++) {
            int subset1 = testAnimalSet.find(subset, edgeArray[i].src);
            int subset2 = testAnimalSet.find(subset, edgeArray[i].dest);
//            System.out.println();
            if (subset1 != subset2) {
                cutEdges++;
            }
        }
//        System.out.println("FINAL: cutedges = " + cutEdges);

        lang.nextStep("Final result and highlight last codeline");
        code.highlight(5);

        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
        rectProps.set("depth", 1);

        textProps.set("depth", 0);
        Text rest = lang.newText(new Offset(0, 50, code, "SW"), (String)this.text.get("results") + cutEdges, "scr", (DisplayOptions)null, textProps);
        Rect resrect = lang.newRect(new Offset(-7, -7, rest, "NW"), new Offset(7, 7, rest, "SE"), "resrect", (DisplayOptions)null, rectProps);
        //There are multiple on top of each other. This text is here to explain that.
        Text explain = lang.newText(new Offset( 0, 10, rest, "SW"), (String)this.text.get("res1") + cutEdges + (String)this.text.get("res2"), "rest", null, textProps);

        lang.nextStep("first MultipleChoice");
        MultipleChoiceQuestionModel firstmulti = new MultipleChoiceQuestionModel("first");
        firstmulti.setPrompt((String)this.text.get("firstmulti0"));
        firstmulti.addAnswer((String)this.text.get("answer0_0"), 1, (String)this.text.get("feedback0_0"));
        firstmulti.addAnswer((String)this.text.get("answer0_1"), 1, (String)this.text.get("feedback0_1"));
        firstmulti.addAnswer((String)this.text.get("answer0_2"), 1, (String)this.text.get("feedback0_2"));
        firstmulti.addAnswer((String)this.text.get("answer0_3"), 1, (String)this.text.get("feedback0_3"));
        this.lang.addMCQuestion(firstmulti);

        lang.nextStep("second MultipleChoice");

        MultipleChoiceQuestionModel secmulti = new MultipleChoiceQuestionModel("second");
        secmulti.setPrompt("secondmulti1");
        secmulti.addAnswer((String)this.text.get("answer1_0"), 0, (String)this.text.get("feedback1_0"));
        secmulti.addAnswer((String)this.text.get("answer1_1"), 0, (String)this.text.get("feedback1_1"));
        secmulti.addAnswer((String)this.text.get("answer1_2"), 0, (String)this.text.get("feedback1_2"));
        secmulti.addAnswer((String)this.text.get("answer1_3"), 0, (String)this.text.get("feedback1_3"));
        this.lang.addMCQuestion(secmulti);

        lang.nextStep("third MultipleChoice");
        MultipleChoiceQuestionModel lastmulti = new MultipleChoiceQuestionModel("last");
        lastmulti.setPrompt((String)this.text.get("lastmulti2"));
        lastmulti.addAnswer((String)this.text.get("answer2_0"), 0, (String)this.text.get("feedback2_0"));
        lastmulti.addAnswer((String)this.text.get("answer2_1"), 0, (String)this.text.get("feedback2_1"));
        lastmulti.addAnswer((String)this.text.get("answer2_2"), 0, (String)this.text.get("feedback2_2"));
        this.lang.addMCQuestion(lastmulti);

        return cutEdges;

    }

    public String getName() {
        return "Karger's algorithm for Minimum Cut";
    }

    public String getAlgorithmName() {
        return "Karger's algorithm for Minimum Cut, by David Karger, published in 1993";
    }

    public String getAnimationAuthor() {
        return "Hannah Drews, Yves Geib";
    }

    public String getDescription(){
        return (this.text.get("introDescription1") +
                this.text.get("introDescription2") +
                "\n" +
                this.text.get("introDescription3") +
                this.text.get("introDescription4") +
                this.text.get("introDescription5") +
                "\n" +
                this.text.get("introDescription6") +
                this.text.get("introDescription7") +
                "\n\n" +
                this.text.get("introDescription8") +
                this.text.get("introDescription9")
        );
    }

    public String getCodeExample(){
        return "while (nodes > 2)\n"
                + "\tchoose random edge (u,v) from graph\n"
                + "\t\tmerge u and v\n"
                + "\t\reattach other edges\n"
                + "\t\tremove self-loops\n"
                + "return cut represented by two nodes\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return this.locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}