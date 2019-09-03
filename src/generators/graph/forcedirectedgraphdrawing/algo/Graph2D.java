/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.graph.forcedirectedgraphdrawing.algo;

import generators.graph.helpers.Point2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Graph2D {

    List<Point2D> points;
    int[][] adjacencyMatrix;


    public Graph2D(List<Point2D> points, int[][] adjacencyMatrix) {
        this.points = points;
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public Point2D getPositionOfNode(int node) {
        return points.get(node);
    }

    public void translateNode(int node, Point2D targetPos) {
        points.set(node, targetPos);
    }

    public int getSize() {
        return points.size();
    }

    public List<Integer> getAdjacentNodes(int node) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (node != i && adjacencyMatrix[node][i] != 0) result.add(i);
        }
        for (int i = 0; i < adjacencyMatrix[0].length; i++) {
            if (node != i && adjacencyMatrix[i][node] != 0 && !result.contains(i)) result.add(i);
        }
        Collections.sort(result, Comparator.naturalOrder());
        return result;
    }

    public List<Integer> getNonAdjacentNodes(int node) {
        List<Integer> result = new ArrayList<>();
        List<Integer> adjNodes = getAdjacentNodes(node);
        for (int i = 0; i < this.getSize(); i++) {
            if (node != i && !adjNodes.contains(i) && !result.contains(i)) result.add(i);
        }
        return result;

    }

    public void updateGraph(List<Point2D> points) {
         this.points = points;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }
}
