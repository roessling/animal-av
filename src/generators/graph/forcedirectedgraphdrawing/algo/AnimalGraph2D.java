/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.graph.forcedirectedgraphdrawing.algo;

import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.graph.helpers.Point2D;

import java.awt.*;
import java.util.HashSet;
import java.util.List;


public class AnimalGraph2D extends Graph2D {
    Graph graph;
    Language lang;
    Coordinates baseNode;
    public final int NODERADIUS = 15;
    public final static int BASENODE_X_DIFF = 4;
    public final static int BASENODE_Y_DIFF = 10;
    HashSet<Integer> highlightedStates = new HashSet<>();
    String[] labels;

    boolean doubleStepMode = true;
    boolean signalRestPhase = false;

    public AnimalGraph2D(Graph graph, Language lang) {
        super(Util.nodesToPoints(graph.getNodes()), graph.getAdjacencyMatrix());
        this.lang = lang;
        this.graph = graph;
        labels = new String[graph.getSize()];
        for (int i = 0; i < graph.getSize(); i++) {
            labels[i]=graph.getNodeLabel(i);
        }
        initBaseNode();
    }

    public AnimalGraph2D(List<Point2D> nodes, int[][] adjMatrix, String[] labels, Language lang, GraphProperties properties) {
        super(nodes, adjMatrix);
        this.lang = lang;
        this.labels = labels;
        this.graph = lang.newGraph("graph", this.adjacencyMatrix, Util.pointsToNodes(this.points), labels, null, properties);
        initBaseNode();
    }

    public String getLabel(int node){
        return labels[node];
    }

    public void setDoubleStepMode(boolean doubleStepMode) {
        this.doubleStepMode = doubleStepMode;
    }

    public Coordinates normalizeVectorToGraphBaseNode(Node movingNode, Node target) {
        Point2D nodeVector, targetVector;
        System.out.println("movingNode: " + Util.nodeToPoint(movingNode));
        System.out.println("target: " + Util.nodeToPoint(target));
        nodeVector = Util.nodeToPoint(movingNode).subtract(Util.nodeToPoint(baseNode));
        System.out.println("nodeVector: " + nodeVector.toString());
        targetVector = Util.nodeToPoint(target).subtract(nodeVector);
        System.out.println("targetVector: " + targetVector.toString());
        return Util.pointToNode(targetVector);
    }

    private void initBaseNode() {
        int minX, minY;
        minX = minY = Integer.MAX_VALUE;
        for (int i = 0; i < graph.getSize(); i++) {
            graph.setNodeRadius(i, NODERADIUS, null, null);
            minX = Math.min((int) this.points.get(i).getX(), minX);
            minY = Math.min((int) this.points.get(i).getY(), minY);
        }
//        System.out.println("min: " + minX + " " + minY);
        if (NODERADIUS > BASENODE_X_DIFF) minX = minX - (NODERADIUS - BASENODE_X_DIFF);
        if (NODERADIUS > BASENODE_Y_DIFF) minY = minY - (NODERADIUS - BASENODE_Y_DIFF);
//        System.out.println("base: " + minX + " " + minY);
        baseNode = new Coordinates(minX, minY);
    }

    public void highlightState(int state, Color color){
        if(!highlightedStates.contains(state)) {
            graph.setNodeHighlightFillColor(state, color, null, null);
            graph.highlightNode(state, null, null);
            highlightedStates.add(state);
        }
    }

    public void unHighlightState(int state){
        if(highlightedStates.contains(state)) {
            graph.unhighlightNode(state, null, null);
            highlightedStates.remove(state);
        }
    }

    @Override
    public void translateNode(int node, Point2D targetPos) {
        Point2D oldPoint = this.getPositionOfNode(node);
        super.translateNode(node, targetPos);
        if(!(((int)oldPoint.getX())== ((int)targetPos.getX()) && ((int)oldPoint.getY())== ((int)targetPos.getY()))) {

            graph.translateNode(node, Util.pointToNode(targetPos), null, null);
//            lang.nextStep();
        }

    }

    //

    @Override
    public void updateGraph(List<Point2D> newPoints) {
//        super.updateGraph(newPoints);

        for (int i = 0; i < graph.getSize(); i++) {

            translateNode(i,newPoints.get(i) );
        }

//Currently Animal needs two steps to correctly update the new Graph positions, or else the transitions become buggy/misplaced
        //Update: Seems to be PARTIALLY fixed with ANIMAL 2.5.6 Patch 2018-08-17, with the end-state being correct, intermediate not
        lang.nextStep();
        if(doubleStepMode){
//            System.out.println("doubleStep!");
            if(!signalRestPhase){
                lang.nextStep("Phase 3: Remaining Iterations");
                signalRestPhase = true;
            }else
                lang.nextStep();
        }
    }


}
