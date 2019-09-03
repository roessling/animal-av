/*
 * AntOptimization.java
 * Dmitrij Kress, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class AntOptimization implements Generator {
    private Language lang;
    
    private double maxPheromon = 2;

    private double[][] graph;
    private double[][] pheromon;
    private double[][] probabilities;
    private double[] deltas;
    private int start = 0;
    private int goal = 3;
    
    private int[][] nodesCoordinates;
    private int[][] edges;
    private Polygon[] pheramonHighlight;
    private Rect[][] table;
    private Text[][] tableText;
    private Circle[] nodeCircles;
    private Text[] nodeNumbers;
    private Polyline[] edgesLines;
    
    private CircleProperties circleProps;
    private SourceCodeProperties codeProps;
    
    private double evaporationCoeff = 0.2;
    private double pheromonStart = 0.01;
    private int numberAnts = 10;
    private double alpha = 0.75;
    private int numIterations = 15;
    
    private int speed = 85;

    private SourceCode code;
    private Text comment;
    
    private Variables vars;
    

    public void init(){
        lang = new AnimalScript("Ant colony optimization [EN]", "Dmitrij Kress", 800, 600);
        lang.setStepMode(true);
    }
    

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        codeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        circleProps = (CircleProperties)props.getPropertiesByName("nodes");
        numberAnts = (Integer)primitives.get("numberOfAnts");
        alpha = Double.valueOf(primitives.get("alpha").toString());
        numIterations = (Integer)primitives.get("numberOfIterations");
        evaporationCoeff = Double.valueOf(primitives.get("evaporationCoefficient").toString());
        
        
        vars = lang.newVariables();
        vars.declare("int", "numberOfIterations");
        vars.set("numberOfIterations", numIterations + "");
        vars.declare("int", "iteration");
        vars.declare("int", "numberOfAnts");
        vars.set("numberOfAnts", numberAnts + "");
        vars.declare("double", "alpha");
        vars.set("alpha", alpha + "");
        vars.declare("double", "rho");
        vars.set("rho", evaporationCoeff + "");
        vars.setGlobal("iteration");
        vars.set("iteration", "1");
  
        introduction1();
        initCode();
        initGraph();
        initTable();
        intro();
        solve();
        showFoundPath();
        outro();
        
        maxPheromon = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (pheromon[i][j] > maxPheromon) {
                    maxPheromon = pheromon[i][j];
                }
            }
        }
        
        
        
        return lang.toString();
    }

    public String getName() {
        return "AntOptimization";
    }

    public String getAlgorithmName() {
        return "Ant Colony Optimization";
    }

    public String getAnimationAuthor() {
        return "Dmitrij Kress";
    }

    public String getDescription(){
        return "Ant colony optimization algorithms are an efficient probabilistic method for finding good paths\n"
 +"in graphs. It mimics the way in which real ants find short paths between their nest and a\n"
 +"source of food. Every ant leaves a pheromone trail and the less time it takes the ant to travel\n"
 +"in both directions, the more dense the trail is going to be. Ants are more likely to choose\n"
 +"paths with higher pheromone levels. As a result shorter paths tend to accumulate more\n"
 +"pheromones than the longer ones.\n"
 +"The following visualization shows an algorithm which is based on similar principles. ";
    }

    public String getCodeExample(){
        return " for (i=1 to numberOfIterations) {"
 +"\n"
 +"     for (edge \u2208 Edges)"
 +"\n"
 +"         \u0394τ(edge) := 0"
 +"\n"
 +"     }"
 +"\n"
 +"     for (j=1 to numberOfAnts) {"
 +"\n"
 +"         antAt := startNode, antPath := {}"
 +"\n"
 +"         while (antAt != finishNode) {"
 +"\n"
 +"             nextNode := chooseNextNode(antAt)"
 +"\n"
 +"             antPath.add((antAt, nextNode))"
 +"\n"
 +"             antAt := nextNode"
 +"\n"
 +"         }"
 +"\n"
 +"         for (edge \u2208 antPath) {"
 +"\n"
 +"             \u0394τ(edge) := \u0394τ(edge) + 1 / length(antPath)"
 +"\n"
 +"         }"
 +"\n"
 +"     for (edge \u2208 Edges) {"
 +"\n"
 +"         τ(edge) := (1 - ρ)*τ(edge) + \u0394τ(edge)"
 +"\n"
 +"     }"
 +"\n"
 +" }";
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
    
    
    
    
    
    
    
    
    /**
     * Show introductory text.
     */
    public void introduction1() {
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        Text header = lang.newText(new Coordinates(150, 20), "Ant colony optimization", "header", null, headerProps);
        
        RectProperties headerRectProps = new RectProperties();
        headerRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);

        int border = 2;
        Rect headerRect = lang.newRect(new Offset(-border , -border, header, "NW"), 
                                       new Offset(border, border, header, "SE"), "headerRect", null, headerRectProps);
                 
        
        SourceCode intro = lang.newSourceCode(new Coordinates(15, 40), "text", null, codeProps);
        intro.addCodeLine("Ant colony optimization algorithms are an efficient probabilistic method for finding good paths", null, 0, null);
        intro.addCodeLine("in graphs. It mimics the way in which real ants find short paths between their nest and a", null, 0, null);
        intro.addCodeLine("source of food.", null, 0, null);
        intro.addCodeLine("A real world ant leaves a pheromone trail and the less time it takes the ant to travel in both", null, 0, null);
        intro.addCodeLine("directions, the more dense the trail is going to be. Ants are more likely to choose paths with", null, 0, null);
        intro.addCodeLine("higher pheromone levels. As a result shorter paths tend to accumulate more pheromones than the", null, 0, null);
        intro.addCodeLine("longer ones. ", null, 0, null);
        intro.addCodeLine("The following example shows an algorithm which is based on similar principles. ", null, 0, null);
         
        lang.nextStep("Introduction");

        //headerRect.hide();
        //header.hide();
        intro.hide();
    }
    
    
    
    
    /**
     * Show conclusion.
     */
    public void outro() {
        for (Polygon p : pheramonHighlight) {
            p.hide();
        }
        for (Circle circ : nodeCircles) {
            circ.hide();
        }

        for (Text text : nodeNumbers) {
            text.hide();
        }
        for (Polyline poly : edgesLines) {
            poly.hide();
        }
               
        
        SourceCode outro = lang.newSourceCode(new Coordinates(15, 40), "text", null, codeProps);
        outro.addCodeLine("There are many different variation of the algorithm that introduce additional features.", null, 0, null);
        outro.addCodeLine("Other variant may also use edges' lengths as heuristic for choosing a route.", null, 0, null);
        outro.addCodeLine("Moreover, only pheromone levels for the shortest tour may be updated.", null, 0, null);

         
        lang.nextStep("Conclusion");
    }
    
    
    
    /**
     * Show the text that explains the example.
     */
    public void initCode() { 
        SourceCodeProperties textProps = new SourceCodeProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        
        SourceCode intro = lang.newSourceCode(new Coordinates(40, 250), "text", null, textProps);

        
        
        
        code = lang.newSourceCode(new Coordinates(275, 55), "text", null, codeProps);
        code.addCodeLine("for (i=1 to numberOfIterations) {", null, 0, null);
        code.addCodeLine("    for (edge \u2208 Edges)", null, 0, null);
        code.addCodeLine("        \u0394τ(edge) := 0", null, 0, null);
        code.addCodeLine("    }", null, 0, null);
        code.addCodeLine("    for (j=1 to numberOfAnts) {", null, 0, null);
        code.addCodeLine("        antAt := startNode, antPath := {}", null, 0, null);
        code.addCodeLine("        while (antAt != finishNode) {", null, 0, null);
        code.addCodeLine("            nextNode := chooseNextNode(antAt)", null, 0, null);
        code.addCodeLine("            antPath.add((antAt, nextNode))", null, 0, null);
        code.addCodeLine("            antAt := nextNode", null, 0, null);
        code.addCodeLine("        }", null, 0, null);
        code.addCodeLine("        for (edge \u2208 antPath) {", null, 0, null);
        code.addCodeLine("            \u0394τ(edge) := \u0394τ(edge) + 1 / length(antPath)", null, 0, null);
        code.addCodeLine("        }", null, 0, null);
        code.addCodeLine("    }", null, 0, null);
        code.addCodeLine("    for (edge \u2208 Edges) {", null, 0, null);
        code.addCodeLine("        τ(edge) := (1 - ρ)*τ(edge) + \u0394τ(edge)", null, 0, null);
        code.addCodeLine("    }", null, 0, null);
        code.addCodeLine("}", null, 0, null);
        
        comment = lang.newText(new Coordinates(275, 375), "", "", null, new TextProperties());
       
    }
    
    
    /**
     * Set start values and graphics for the graph. 
     */
    private void initGraph() {        
        int posX = 50;
        int posY = 55;
        int scale = 20;
        
        nodesCoordinates = new int[7][2];
        nodesCoordinates[0][0] = 4; nodesCoordinates[0][1] = 13;
        nodesCoordinates[1][0] = 5; nodesCoordinates[1][1] = 8;
        nodesCoordinates[2][0] = 4; nodesCoordinates[2][1] = 4;
        nodesCoordinates[3][0] = 4; nodesCoordinates[3][1] = 0;
        nodesCoordinates[4][0] = 10; nodesCoordinates[4][1] = 11;
        nodesCoordinates[5][0] = -1; nodesCoordinates[5][1] = 12;
        nodesCoordinates[6][0] = -1; nodesCoordinates[6][1] = 1;
        
        edges = new int[10][2];
        edges[0][0] = 0; edges[0][1] = 5;
        edges[1][0] = 0; edges[1][1] = 1;
        edges[2][0] = 0; edges[2][1] = 4;
        edges[3][0] = 5; edges[3][1] = 6;
        edges[4][0] = 1; edges[4][1] = 2;
        edges[5][0] = 4; edges[5][1] = 1;
        edges[6][0] = 4; edges[6][1] = 3;
        edges[7][0] = 2; edges[7][1] = 6;
        edges[8][0] = 2; edges[8][1] = 3;
        edges[9][0] = 6; edges[9][1] = 3;
        
        edgesLines = new Polyline[edges.length];
        PolylineProperties lineProps = new PolylineProperties();
        int n = 0;
        for (int[] edge : edges) {
            Coordinates[] vertices = new Coordinates[2];
            vertices[0] = new Coordinates(nodesCoordinates[edge[0]][0]*scale + posX, 
                    nodesCoordinates[edge[0]][1]*scale + posY);
            vertices[1] = new Coordinates(nodesCoordinates[edge[1]][0]*scale + posX, 
                    nodesCoordinates[edge[1]][1]*scale + posY);
            edgesLines[n] = lang.newPolyline(vertices, "edge", null, lineProps);
            n++;
        }

        nodeNumbers = new Text[nodesCoordinates.length];
        nodeCircles = new Circle[nodesCoordinates.length];
        int nodeNumber = 0;
        for (int[] points : nodesCoordinates) {
            Circle c = lang.newCircle(new Coordinates(points[0]*scale + posX, points[1]*scale + posY),
                    10, "circle", null, circleProps);
            if (nodeNumber == start) {
                c.changeColor("fillColor", Color.CYAN, null, null);
            }
            if (nodeNumber == goal) {
                c.changeColor("fillColor", Color.RED, null, null);
            }
            nodeNumbers[nodeNumber] = lang.newText(new Offset(-4, 2, c, "N"), (nodeNumber + 1) + "", "node_name", null, new TextProperties());
            nodeCircles[nodeNumber] = c;
            nodeNumber++;
        }
        
        // lengths of the edges
        graph = new double[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                graph[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int[] edge : edges) {
            double x1 = nodesCoordinates[edge[0]][0];
            double y1 = nodesCoordinates[edge[0]][1];
            double x2 = nodesCoordinates[edge[1]][0];
            double y2 = nodesCoordinates[edge[1]][1];
            graph[edge[0]][edge[1]] = round(Math.sqrt(Math.abs(x1 - x2)*Math.abs(x1 - x2) 
                                                 + Math.abs(y1 - y2)*Math.abs(y1 - y2)));
         }
        
        // set polygons that will be used for highlighting pheromone traces
        int width = 5;
        PolygonProperties polyProps = new PolygonProperties();
        polyProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        polyProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        polyProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
        pheramonHighlight = new Polygon[edges.length];
        for (int i = 0; i < edges.length; i++) {
            double x1 = nodesCoordinates[edges[i][0]][0];
            double y1 = nodesCoordinates[edges[i][0]][1];
            double x2 = nodesCoordinates[edges[i][1]][0];
            double y2 = nodesCoordinates[edges[i][1]][1];
            
            double x, y;
            if (x1 == x2) {
                x = width;
                y = 0;
            } else if (y1 == y2) {
                x = 0;
                y = width;
            } else {
                x = -(y1 - y2)/(x1 - x2);
                double l = Math.sqrt(x*x + 1);
                x *= (double)(width)/l;
                y = (double)(width)/l;
            }
                        
            Coordinates[] vertices = new Coordinates[4];
            vertices[0] = new Coordinates((int)(x1*scale + posX + x), 
                                          (int)(y1*scale + posY + y));
            vertices[1] = new Coordinates((int)(x2*scale + posX + x), 
                                          (int)(y2*scale + posY + y));
            vertices[3] = new Coordinates((int)(x1*scale + posX - x), 
                                          (int)(y1*scale + posY - y));
            vertices[2] = new Coordinates((int)(x2*scale + posX - x), 
                                          (int)(y2*scale + posY - y)); 
            
            try {
                pheramonHighlight[i] = lang.newPolygon(vertices, "edge", null, polyProps);
            } catch (NotEnoughNodesException e) {}
        }
        
        pheromon = new double[graph.length][graph.length];
        probabilities = new double[graph.length][graph.length];
        deltas = new double[edges.length];
        
        
        
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                pheromon[i][j] = pheromonStart;
            }
        }
        
        updateProbabilities();
    }
    
    
    
    
    
    /**
     * Initialize table with default values.
     */
    private void initTable() {
        int posX = 610;
        int posY = 50;
        int width = 60;
        int height = 22;
        int fontSize = 13;
 
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
         
        table = new Rect[5][edges.length + 1];
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < edges.length + 1; j++) {
                table[i][j] = lang.newRect(new Coordinates(posX + i*width, posY + j*height), 
                                               new Coordinates(posX + (i + 1)*width, posY + (j + 1)*height), 
                                               "Field_" + i + "_" + j, null, rectProps);            }
            table[i][0].changeColor("fillColor", Color.LIGHT_GRAY, null, null);
        }
        
        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, fontSize));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        
        
        tableText = new Text[5][edges.length + 1];
        tableText[0][0] = lang.newText(new Offset(0, 5, table[0][0], AnimalScript.DIRECTION_N),
                "Edge", "", null, textProps);
        tableText[1][0] = lang.newText(new Offset(0, 5, table[1][0], AnimalScript.DIRECTION_N),
                "length", "", null, textProps);
        tableText[2][0] = lang.newText(new Offset(0, 5, table[2][0], AnimalScript.DIRECTION_N),
                "τ", "", null, textProps);
        tableText[3][0] = lang.newText(new Offset(0, 5, table[3][0], AnimalScript.DIRECTION_N),
                "P", "", null, textProps);
        tableText[4][0] = lang.newText(new Offset(0, 5, table[4][0], AnimalScript.DIRECTION_N),
                "\u0394τ", "", null, textProps);
        
        for (int i = 1; i < edges.length + 1; i++) {
            tableText[0][i] = lang.newText(new Offset(0, 5, table[0][i], AnimalScript.DIRECTION_N),
                    "(" + (edges[i-1][0]+1)  +", " + (edges[i-1][1]+1) +")", "", null, textProps);
            table[0][i].changeColor("fillColor", Color.getHSBColor(0.90f, 0, 0.9f), null, null);
        }
        
        
        
        for (int i = 1; i < edges.length + 1; i++) {
            tableText[1][i] = lang.newText(new Offset(-8, 5, table[1][i], AnimalScript.DIRECTION_N),
                    "       ", "", null, textProps);
            tableText[1][i].setText(round(graph[edges[i-1][0]][edges[i-1][1]]) + "", null, null);
            tableText[2][i] = lang.newText(new Offset(-8, 5, table[2][i], AnimalScript.DIRECTION_N),
                    "       ", "", null, textProps);
            tableText[2][i].setText(round(pheromonStart) + "", null, null);
            tableText[3][i] = lang.newText(new Offset(-8, 5, table[3][i], AnimalScript.DIRECTION_N),
                    "       ", "", null, textProps);
            tableText[3][i].setText("" + "", null, null);
            tableText[4][i] = lang.newText(new Offset(-8, 5, table[4][i], AnimalScript.DIRECTION_N),
                    "       ", "", null, textProps);
            tableText[4][i].setText("" + "", null, null);
            
        }
        
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, fontSize));
        lang.newText(new Offset(0, -fontSize - 4, table[2][0], AnimalScript.DIRECTION_N), "", "tmp", null, textProps);

    }
    
    
    /**
     * Show text explaining the example.
     */
    private void intro() {
        comment.setText("In this example we're looking for an optimal path from node " + (start + 1)
                        + " to node " + (goal + 1) + " using the algorith.", null, null);
        lang.nextStep();
        
        String text = "Probabilities P(a, b) of an ant choosing the edge (a, b) are calculated according to the rule:";
        comment.setText(text, null, null);
        
        TextProperties textProps = new TextProperties();
        Text t0 = lang.newText(new Offset(0, 30, comment, "SW"), 
                "where τ(a, b) is the amount of pheromone on the edge (a, b). \u03B1 in this example equals " + alpha, 
                "t1", null, textProps);
        Text t01 = lang.newText(new Offset(0, 0, t0, "SW"), 
                "\u03B1 controls how much influence τ(a, b) has when a node is being chosen.", "t01", null, textProps);
        Text t1 = lang.newText(new Offset(0, 4, comment, "SW"), "P(a, b) = τ(a, b)   /      τ(a, c)", "t1", null, textProps);
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
        Text t2 = lang.newText(new Offset(1, -4, t1, "NE"), "\u03B1", "t1", null, textProps);
        Text t3 = lang.newText(new Offset(100, -4, t1, "NW"), "\u03B1", "t1", null, textProps);
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20)); 
        Text t4 = lang.newText(new Offset(-55, -9, t1, "SE"), "Σ", "t1", null, textProps);
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 8)); 
        Text t5 = lang.newText(new Offset(-80, -10, t1, "SE"), "c adjacent to a", "t1", null, textProps);
        
        updateTable(null, 3);
        lang.nextStep();
        
        comment.setText("", null, null);
        t0.hide();
        t01.hide();
        t1.hide();
        t2.hide();
        t3.hide();
        t4.hide();
        t5.hide();
    }
    
    
    
    
    /**
     * Update table entries after delay.
     * @param delay
     * @param column 
     */
    private void updateTable(Timing delay, Integer column) {
        if (column == null) {
            for (int i = 1; i < edges.length + 1; i++) {
                tableText[2][i].setText(round(pheromon[edges[i-1][0]][edges[i-1][1]]) + "", delay, null);
                tableText[3][i].setText(round(probabilities[edges[i-1][0]][edges[i-1][1]]) + "", delay, null);
                tableText[4][i].setText(round(deltas[i-1]) + "", delay, null);
            }
        } else {
            for (int i = 1; i < edges.length + 1; i++) {
                if (column == 2) {
                    tableText[2][i].setText(round(pheromon[edges[i-1][0]][edges[i-1][1]]) + "", delay, null);
                } else if (column == 3) {
                    tableText[3][i].setText(round(probabilities[edges[i-1][0]][edges[i-1][1]]) + "", delay, null);
                } else {
                    tableText[4][i].setText(round(deltas[i-1]) + "", delay, null);
                }
            }
        }

    }
    
    
    private void showFoundPath() {
        Color color = Color.BLUE;
        
        code.hide();
        for (Polygon p : pheramonHighlight) {
            p.hide();
        }
        for (Rect[] rects : table) {
            for (Rect rect : rects) {
                rect.hide();
            }
        }
        for (Text[] texts : tableText) {
            for (Text text : texts) {
                text.hide();
            }
        }
        
        LinkedList<Integer> path = new LinkedList<Integer>();
        path.add(start);
        
        int node = start;
        while (node != goal) {
            int nextNode = 0;
            double maxPheromone = Double.NEGATIVE_INFINITY;
            double phs[] = pheromon[node]; 
            int i = 0;
            for (Double d : phs) {
                if( d > maxPheromone) {
                    nextNode = i;
                    maxPheromone = d;
                }
                i++;
            }
            
            int edge = -1;
            for (int j = 0; j < edges.length; j++) {
                if (edges[j][0] == node && edges[j][1] == nextNode) {
                    edge = j;
                    break;
                }
            }
            
            
            pheramonHighlight[edge].changeColor("fillColor", color, null, null);
            pheramonHighlight[edge].changeColor("color", color, null, null);
            pheramonHighlight[edge].show();     
            
            node = nextNode;
            path.addLast(node);
        }
        
        boolean isShortest = path.size() == 4 && path.get(1) == 1 && path.get(2) == 2;        
        
        SourceCode text = lang.newSourceCode(new Offset(0, 0, code, "NW"), "text", null, codeProps);
        text.addCodeLine("Here is shown the path that the algorithm has found.", null, 0, null);
        if (isShortest) {
            text.addCodeLine("This is also the shortest path from node " + 
                    (start + 1) + " to node " + (goal + 1) + " .", null, 0, null);
        } else {
            text.addCodeLine("This however isn't the shortest path from node " + 
                    (start + 1) + " to node " + (goal + 1) + " .", null, 0, null);
        }
        
        
        lang.nextStep("Result");
        
        text.hide();
    }
    
    
    
    
    
    /**
     * Highlight edges (after delay) according to pheromone-levels.
     */
    private void highlightEdges(Timing delay) {
        // for esthetical reasons edges with less pheromone are highlighted first.
        LinkedList<Integer> ints = new LinkedList<Integer>();
        for (int i = 0; i < edges.length; i++) {
            ints.add(i);
        }   
        while (!ints.isEmpty()) {                 
            double min = Double.POSITIVE_INFINITY;
            int minInd = 0, edgeInd = 0;
            for (int i = 0; i < ints.size(); i++) {
                if (pheromon[edges[ints.get(i)][0]][edges[ints.get(i)][1]] < min) {
                    min = pheromon[edges[ints.get(i)][0]][edges[ints.get(i)][1]];
                    edgeInd = ints.get(i);
                    minInd = i;
                }
            }
                       
            ints.remove(minInd);
            
            try {
            
                PolygonProperties polyProps = pheramonHighlight[edgeInd].getProperties();
                pheramonHighlight[edgeInd].hide(delay);
                pheramonHighlight[edgeInd] = lang.newPolygon(pheramonHighlight[edgeInd].getNodes(), "edge", 
                        null, polyProps);
                pheramonHighlight[edgeInd].hide();
                pheramonHighlight[edgeInd].show(delay);
                
                
                float ph = (float) (pheromon[edges[edgeInd][0]][edges[edgeInd][1]]/maxPheromon);
                ph = ph > 1 ? 1 : ph;
                Color c = Color.getHSBColor(1, ph, 1);
                pheramonHighlight[edgeInd].changeColor("fillColor", c, delay, null);
                pheramonHighlight[edgeInd].changeColor("color", c, delay, null);
                table[0][edgeInd + 1].changeColor("fillColor", c, delay, null);
            } catch (NotEnoughNodesException e) {}
        }
        
    }
    
    
    /**
     * Calculate probabilities of taking paths according to current pheromone-levels.
     */
    private void updateProbabilities() {
        LinkedList<Integer> edgesList = new LinkedList<Integer>();
        for (int i = 0; i < edges.length; i++) {
            edgesList.add(i);
        }
        while (!edgesList.isEmpty()) {
            LinkedList<Integer> currentEdges = new LinkedList<Integer>();
            int e = edgesList.pollFirst();
            currentEdges.add(e);
            for (int i = 0; i < edgesList.size();) {
                if (edges[edgesList.get(i)][0] == edges[e][0]) {
                    currentEdges.add(edgesList.get(i));
                    edgesList.remove(i);
                } else {
                    i++;
                }
            }
            double sum = 0;
            for (int j : currentEdges) {
                sum += Math.pow(pheromon[edges[j][0]][edges[j][1]], alpha);
            }
            for (int j : currentEdges) {                    
                probabilities[edges[j][0]][edges[j][1]] = Math.pow(pheromon[edges[j][0]][edges[j][1]], alpha)/sum;
            }
        }
    }
    
    
    
    
    /**
     * The actual algorithm. 
     */
    public void solve() {
        updateTable(null, null);
        
        
        boolean detailed = true;
        boolean rhoExplained = false;
        AntWalker ant = new AntWalker();
        

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                pheromon[i][j] = pheromonStart;
            }
        }

        LinkedList<LinkedList<Integer>> walks;

        
        
        for (int iteration = 0; iteration < numIterations; iteration++) {
            
            LinkedList<Integer> walk;
            LinkedList<Integer> nextNodes;
            
            Timing timeWalk = null;
            
            walks = new LinkedList<LinkedList<Integer>>();
            updateProbabilities();
            
            for (int i = 0; i < deltas.length; i++) {
                deltas[i] = 0;
            }
            
            updateTable(null, 4);
            if (detailed) {
                code.highlight(2);  
                
                lang.nextStep("Iteration 1 (detailed)");
                code.unhighlight(2);
            }
            
            for (int n = 0; n < numberAnts; n++) {
                walk = new LinkedList<Integer>();
                int antState = start;
                
                

                
                while (antState != goal) {   
                    nextNodes = new LinkedList<Integer>();
                    for (int i = 0; i < graph.length; i++) {
                        if (graph[antState][i] != Double.POSITIVE_INFINITY) {
                            nextNodes.add(i);
                        }
                    }
                    
                    double randomDouble = Math.random();
                    for (int i : nextNodes) {
                        if (randomDouble < probabilities[antState][i]) {
                            walk.add(antState);
                            antState = i;
                            break;
                        }
                        randomDouble -= probabilities[antState][i];
                    }
                    
                }
                
                walk.add(goal);
                walks.add(walk);
                
                timeWalk = ant.walkAnt(walk, n*speed, detailed);
                
                updateDelta(walk);
                if (detailed) {
                    code.highlight(12);
                    comment.setText("", null, null);                    
                    updateTable(null, null);
                    lang.nextStep();
                    code.unhighlight(12);
                }
            }
            
            pheromoneUpdate(walks);
            updateProbabilities();
            if (detailed) {
                highlightEdges(null);
                updateTable(null, null);
                code.highlight(16);
                if (!rhoExplained) {
                    comment.setText("ρ is the pheromone evaporation coefficient and equals " + evaporationCoeff, null, null);
                    rhoExplained = true;
                } else {
                    comment.setText("", null, null);
                }                
                updateTable(null, null);
            } else {
                updateTable(new MsTiming(timeWalk.getDelay() - speed*7), 4);
                for (int i = 0; i < deltas.length; i++) {
                    deltas[i] = 0;
                }
                updateTable(timeWalk, 2);
                updateTable(timeWalk, 3);
                //updateTable(timeWalk, 4);
                highlightEdges(timeWalk);
                
            }
            
             
            if (iteration == 0) {
                lang.nextStep();
            } else {
                vars.set("iteration", Integer.toString(iteration + 1));
                lang.nextStep("Iteration " + (iteration + 1));                
            }
            
            if (detailed) {
                code.unhighlight(16);
                comment.setText("", null, null);
            }
            
            
            if (iteration >= 0) {
                detailed = false;
            }
        }
              
    }
    
    
    /**
     * 
     * @param walk
     */
    private void updateDelta(LinkedList<Integer> walk) {
        double L = pathLength(walk);
        for (int i = 1; i < walk.size(); i++) {
            for (int j = 0; j < edges.length; j++) {
                if (edges[j][0] == walk.get(i-1) && edges[j][1] == walk.get(i)) {
                    deltas[j] += 1 / L;
                }
            }            
        }

    }
    
    
    
    
    /**
     * Finds the length of a path.
     * @param walk nodes through which the walk goes
     * @return length of walk
     */
    private double pathLength(LinkedList<Integer> walk) {
        double L = 0;
        for (int i = 1; i < walk.size(); i++) {
            L += graph[walk.get(i-1)][walk.get(i)];
        }
        return L;
    }
    
    
    
    /**
     * Update pheromone levels after all ants finished their walks.
     * @param walks
     */
    private void pheromoneUpdate(LinkedList<LinkedList<Integer>> walks) {
        double walkCost;
        double q = 1;


        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                pheromon[i][j] = pheromon[i][j]*(1 - evaporationCoeff);
            }
        }
        
        for (LinkedList<Integer> walk : walks) {
            walkCost = 0;
            for (int i = 0; i < walk.size() - 1; i++) {
                walkCost += graph[walk.get(i)][walk.get(i+1)];
            }
            
            for (int i = 0; i < walk.size() - 1; i++) {
                pheromon[walk.get(i)][walk.get(i+1)] += q / walkCost;
            }
        }
    }
    
    
    
    
    /**
     * Class for showing ant-animations.
     */
    private class AntWalker {
        int posX = 50;
        int posY = 55;
        int scale = 20;
        
        
        
        private Timing walkAnt(LinkedList<Integer> walk, int delay, boolean detailed) {       
            int x = nodesCoordinates[walk.getFirst()][0]*scale + posX - 4;
            int y = nodesCoordinates[walk.getFirst()][1]*scale + posY - 8;
            
            CircleProperties circleProps = new CircleProperties();
            circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
            Circle ant = lang.newCircle(new Coordinates(x+4, y+8), 5, "ant", null, circleProps);
            
            if (detailed) {
                code.highlight(5);
                comment.setText("antPath = {}", null, null);
                lang.nextStep();
                code.unhighlight(5);
            }
            
            int time = delay;
            for (int i = 1; i < walk.size(); i++) {
                Coordinates target = new Coordinates(nodesCoordinates[walk.get(i)][0]*scale + posX, 
                        nodesCoordinates[walk.get(i)][1]*scale + posY);
                
                int moveX = nodesCoordinates[walk.get(i)][0]*scale - nodesCoordinates[walk.get(i - 1)][0]*scale;
                int moveY = nodesCoordinates[walk.get(i)][1]*scale - nodesCoordinates[walk.get(i - 1)][1]*scale;

                if (!detailed) {
                    ant.moveBy(null, moveX, moveY, new MsTiming(time), 
                            (new MsTiming((int)graph[walk.get(i-1)][walk.get(i)]*speed)));                    
                    time += graph[walk.get(i-1)][walk.get(i)]*speed;
                } else {

                    code.highlight(7);
                    nodeCircles[walk.get(i)].changeColor("fillColor", Color.MAGENTA, null, null);
                    lang.nextStep();
                    
                    code.unhighlight(7);
                    
                    
                    code.highlight(8);

                    String text = comment.getText().replace("}", "") + ", (" + (walk.get(i-1)+1) + ", " + (walk.get(i)+1) + ")}";
                    text = text.replace("{, ", "{");
                    comment.setText(text, null, null);
                    
                    lang.nextStep();
                    code.unhighlight(8);
                    code.highlight(9);
                    
                    
                    ant.moveBy(null, moveX, moveY, null, 
                            (new MsTiming((int)graph[walk.get(i-1)][walk.get(i)]*speed)));
                    
                    lang.nextStep();
                    if (walk.get(i) != goal) {
                        nodeCircles[walk.get(i)].changeColor("fillColor", Color.LIGHT_GRAY, null, null);
                    } else {
                        nodeCircles[walk.get(i)].changeColor("fillColor", Color.RED, null, null);
                    }
                    
                    code.unhighlight(7);
                    
                    code.unhighlight(9);
                }
                
            }
            
            if (!detailed) {
                ant.hide(new MsTiming(time));
            } else {
                ant.hide();
            }
            
            return new MsTiming(time);
        }
    }
    
    
    
    
    
    

    
    
    
    
    
    private static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        return (double) Math.round(value) / factor;
    }
    
    
    
    
    

}
