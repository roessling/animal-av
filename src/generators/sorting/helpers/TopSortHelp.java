package generators.sorting.helpers;

import static algoanim.animalscript.AnimalScript.DIRECTION_NE;
import static algoanim.animalscript.AnimalScript.DIRECTION_NW;
import static algoanim.animalscript.AnimalScript.DIRECTION_SE;
import static algoanim.animalscript.AnimalScript.DIRECTION_SW;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalGraphGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.RectGenerator;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alex Krause, Markus Ermuth
 */
public class TopSortHelp {

    private int[][] graph;
  // private int[][] graphNodes;
    private String[] labels;
    private int[] visited;
    private int z;
    private int[] sorted;
    private int size;
    private AnimalScript lang;
    private SourceCode codeAnimal;
    private SourceCode codeDescription;
    private int highlightOldLine;
    private StringArray visitedAnimal;
    private Graph graphAnimal;
    private StringArray sortedAnimal;
    private int iterations;
    private Color HighlightColorCode, HighlightColorArrays, HighlightColorGraph;
    
    private static String[] DESCRIPTION = new String[]{
        "Das Topologische Sortieren ist ein Algorithmus, um in einem azyklischen Graphen"
            , "eine Reihenfolge zu bestimmen."
            , "Bei der von uns verwendeten Bottom-Up Variante wird dafuer eine Tiefensuche"
            , "genutzt, um die Knoten von unten nach oben zu traversierenund dabei Besuchsnummern zu vergeben."};
    private static String[] SOURCE = new String[]{
        "markiere alle v in V als unvisited;", //0
        "z = |V|;  /* maximale inzahl in TS */", //1
        "FORALL v in V DO {", //2
        "/* alle Startknoten */", //3
        "IF( d-(v) == 0 ) THEN topSort(v);", //4
        "}", //5
        "", //6
        "void topSort(Vertex v) { /* DFS-Traversierung */", //7
        "visit(v);", //8
        "FORALL Knoten w in V mit eingehender Kante (v,w) DO", //9
        "IF (unvisited(w)) THEN topSort(w);", //10
        "TS[ z ] = v; /* TS am Ende zuweisen */", //11
        "z--;", //12
        "}" //13
    };
    
    private static String[] ENDTEXT = new String[]{
        "Die Topologische Suche hat allgemein eine",
        "Komplexitaet von O(max(|V|,|E|)). In unserem",
        "Fall kommt man also in ",
        "Schritten zur Loesung."};
    
//    private static String SOURCEP = implodeArray(SOURCE, "\n");
    

//    public static void main(String[] args) {
//        AnimalScript script = new AnimalScript("TopSort Animation", "Markus Ermuth, Alex Krause", 640, 480);
//        //Build AdjacentMatrix
//        int[][] matrix = new int[][]{
//            {0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0},
//            {1, 1, 0, 0, 0, 0, 0},
//            {0, 0, 1, 0, 0, 0, 0},
//            {0, 0, 1, 0, 0, 0, 0},
//            {0, 0, 0, 1, 1, 0, 0},
//            {0, 0, 0, 0, 1, 0, 0}
//        };
//        
//        //Coordinates of Nodes in Graph
//        int[][] graphPositions = new int[][]{
//            {0, 0},
//            {400, 0},
//            {200, 100},
//            {0, 200},
//            {400, 200},
//            {200, 300},
//            {400, 300}
//        };
//        
//        TopSortHelp top = new TopSortHelp(script, matrix, graphPositions);
//        top.sort();
//
//
////        for (int y = 0; y < top.size; y++) {
////            top.sorted[y]++;
////            System.out.println(Arrays.toString(top.graph[y]));
////        }
////        System.out.println();
////        System.out.println(Arrays.toString(top.sorted));
////        System.out.println();
//        System.out.println(script);
//    }


    public TopSortHelp(AnimalScript script, Graph graphAnimal, Color HighlightColorCode, Color HighlightColorArrays, Color HighlightColorGraph) {
        lang = script;
        lang.setStepMode(true);
//        this.graphNodes = graphNodes;
        this.graph = graphAnimal.getAdjacencyMatrix();
        makeBackEdges(this.graph);
        size = graph.length;
        sorted = new int[size];
        visited = new int[size];
        highlightOldLine = -1;
        iterations = 0;
		//Graph Node Labels
        labels = new String[graph.length];
        for(int i = 0; i < graph.length; i++){
            labels[i] = graphAnimal.getNodeLabel(i);
        }
        this.HighlightColorCode = HighlightColorCode;
        this.HighlightColorArrays = HighlightColorArrays;
        this.HighlightColorGraph = HighlightColorGraph;
        this.graphAnimal = graphAnimal;
    }

    public void sort() throws IllegalDirectionException {
        //AnimalProperties Just Copy this if needed again
        ArrayProperties arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, HighlightColorArrays);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, HighlightColorArrays);

        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, HighlightColorCode);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        
        GraphProperties grProps = new GraphProperties("Graph");
        grProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
        grProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        grProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, HighlightColorGraph);
        grProps.set("fillColor", Color.WHITE);
        grProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
        
        TextGenerator textGen = new AnimalTextGenerator(lang);
        TextProperties textProps = new TextProperties("Header");
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
        
        RectGenerator rectGen = new AnimalRectGenerator(lang);
        RectProperties rectProps = new RectProperties("headerBox");
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        

        //AnimalInit
        
        Text header = new Text(textGen, new Coordinates(10, 10), "Topologisches Sortieren", "header", null, textProps);
        Rect headerBox = new Rect(rectGen, new Offset(-10, -10, header, DIRECTION_NW), new Offset(10, 10, header, DIRECTION_SE), "headerBox", null, rectProps);
        lang.nextStep("Einführung");
        codeDescription = makeCode(DESCRIPTION, scProps, new Offset(0, 10, header, DIRECTION_SW), null);
        lang.nextStep();
        codeDescription.hide();
        codeAnimal = makeCode(SOURCE, scProps, new Offset(0, 10, header, DIRECTION_SW), new int[]{
            0,
            0,
            0,
            2,
            2,
            0,
            0,
            0,
            2,
            2,
            4,
            2,
            2,
            0
        });
        
        
        visitedAnimal = lang.newStringArray(new Offset(20, 0, codeAnimal, DIRECTION_NE), labels, "visitedArray", null, arrayProps);
        new Text(textGen, new Offset(0, -15, visitedAnimal, DIRECTION_NW), "Nodes", "visitedLabel", null, textProps);
        sortedAnimal = lang.newStringArray(new Offset(20, 0, visitedAnimal, DIRECTION_NE), new String[labels.length], "sortedArray", null, arrayProps);
         new Text(textGen, new Offset(0, -15, sortedAnimal, DIRECTION_NW), "sorted Nodes", "sortedLabel", null, textProps);
        
        graphAnimal = new Graph(new AnimalGraphGenerator(lang), graphAnimal, null, grProps);

//        int[] indices = new int[labels.length];
//        for(int i = 0; i < labels.length; ++i)
//            indices[i] = i;
//        graphAnimal.translateNodes(indices, new Offset(0, 10, codeAnimal, "SW"), null, null);
        graphAnimal.moveTo(null, null, new Offset(0, 10, codeAnimal, "SW"), null, null);
        //graphAnimal = new Graph(new AnimalGraphGenerator(lang), "TestGraph", graph, convertNodes(graphNodes), labels, null, grProps);
        lang.nextStep("Initialisierung");
        



        //Run part
        visited = new int[size];  highlightNext();// markiere alle v ∈ V als unvisited;
        z = size - 1;   highlightNext("Ausführung");// z = |V|;  !/* maximale Elementzahl in TS */
        for (int node = 0; node < size; node++) {
            highlight(2);
            highlight(4);
            if (isRoot(node)) {
                topSort(node);
            }
        }
        codeAnimal.unhighlight(2);
        codeAnimal.hide();
        
        graphAnimal.moveTo(null, null, new Offset(10, 20, headerBox, DIRECTION_SW), null, null);
        createMCQuestion("Wie viele Iterationen wurden benötigt, um die topologische Sortierung zu bekommen?", iterations);
        lang.nextStep();
        //End
        ENDTEXT[2] = "Fall kommt man also in " + iterations;
        makeCode(ENDTEXT, scProps, new Offset(0, 20, visitedAnimal, DIRECTION_SW), null);
        lang.nextStep("Fazit");
    }

    private void topSort(int node) {
        iterations++;
        graphAnimal.highlightNode(node, null, null); highlight(7);
        visit(node); highlightNext();
        int child = -1;
        while ((child = nextChild(node, child)) > -1) {
            highlight(9);
            highlightNext();
            if (!isVisited(child)) {
                graphAnimal.unhighlightNode(node, null, null); graphAnimal.highlightNode(child, null, null);
                graphAnimal.highlightEdge(node, child, null, null);
                topSort(child); highlightNext();
                graphAnimal.highlightNode(node, null, null);graphAnimal.unhighlightNode(child, null, null);
                graphAnimal.unhighlightEdge(node, child, null, null);
            }
        }
        sorted[z] = node; sortedAnimal.put(z, labels[node], null, null); highlight(11); 
        z--; highlightNext();
        graphAnimal.unhighlightNode(node, null, null);
    }

    
    // THINK ABOUT REUSING THOSE METHODS!
    // THINK ABOUT REUSING THOSE METHODS!
    // THINK ABOUT REUSING THOSE METHODS!
    // THINK ABOUT REUSING THOSE METHODS!
    // THINK ABOUT REUSING THOSE METHODS!
    private void makeBackEdges(int[][] matrix) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                if (matrix[y][x] == 1) {
                    matrix[x][y] = -1;
                }
            }
        }
    }

    private boolean isRoot(int node) {
        for (int connection = 0; connection < size; connection++) {
            if (graph[node][connection] == -1) {
                return false;
            }
        }
        return true;
    }
    
    private void visit(int node) {
        visited[node] = 1; visitedAnimal.put(node, labels[node], null, null); visitedAnimal.highlightCell(node, null, null);
    }

    private boolean isVisited(int node) {
        return visited[node] == 1;
    }

    private int nextChild(int predecessor, int previousChild) {
        for (int child = previousChild + 1; child < size; child++) {
            if (graph[predecessor][child] == 1) {
                return child;
            }
        }
        return -1;
    }

//    private static String implodeArray(String[] inputArray, String glueString) {
//        String output = "";
//        if (inputArray.length > 0) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(inputArray[0]);
//            for (int i = 1; i < inputArray.length; i++) {
//                sb.append(glueString);
//                sb.append(inputArray[i]);
//            }
//            output = sb.toString();
//        }
//        return output;
//    }
    
    private void highlight(int line, String label){
        if(highlightOldLine != -1)
            codeAnimal.toggleHighlight(highlightOldLine, line);
        else
            codeAnimal.highlight(line);
        highlightOldLine = line;
        if(label != null)
            lang.nextStep(label);
        else
            lang.nextStep();
    }

    private void highlight(int line){
        highlight(line, null);
    }
    
    private void highlightNext(String label){
        highlight(highlightOldLine+1, label);
    }
    private void highlightNext(){
        highlight(highlightOldLine+1, null);
    }

    private SourceCode makeCode(String[] source, SourceCodeProperties scProps, Offset off, int[] indentationArgs) {
        SourceCode code = lang.newSourceCode(off, "sourceCode", null, scProps);
        int indentation;
        for(int i = 0; i < source.length; i++){
            indentation = 0;
            if(indentationArgs != null){
                indentation = indentationArgs[i];
            }
    //            if(i >= 3 && i <= 4 || i >= 8 && i <= 12) { // This is hacked!!!
//                if(i == 10)
//                    indentation = 4;
//                else
//                    indentation = 2;
//            }
            code.addCodeLine(source[i], null, indentation, null);
        }
        return code;
    }
    
    private void createMCQuestion(String question, int correctAnswer){
        MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("Question");
        q.setPrompt(question);
        q.addAnswer(""+ (correctAnswer+(int)(Math.round(Math.random()*correctAnswer/2.0+1))*(((int)Math.round(Math.random())==0) ? 1 : -1)), 0, "incorrect");
        q.addAnswer(""+correctAnswer, 0, "correct");
        q.addAnswer(""+ (correctAnswer+(int)(Math.round(Math.random()*correctAnswer/2.0+1))*(((int)Math.round(Math.random())==0) ? 1 : -1)), 0, "incorrect");
        lang.addMCQuestion(q);
    }
}
