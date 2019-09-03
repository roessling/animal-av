/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.graph.forcedirectedgraphdrawing.algo;

import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.graph.helpers.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static Point2D unitVectorOfTwoPoints(Point2D node1, Point2D node2) {
        Point2D result = node1.subtract(node2);
        result.normalize();
        return result;
    }

    public static List<Point2D> nodesToPoints(Node[] nodes) {
        List<Point2D> list = new ArrayList<>();
        for (Node node : nodes) {
            list.add(nodeToPoint(node));
        }
        return list;
    }

    public static Point2D nodeToPoint(Node node) {
        return new Point2D(((Coordinates) node).getX(), ((Coordinates) node).getY());
    }

    public static Node[] pointsToNodes(List<Point2D> points) {
        Node[] nodes = new Node[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            nodes[i] = pointToNode(point);
        }
        return nodes;
    }

    public static Coordinates pointToNode(Point2D point) {
        return new Coordinates((int) point.getX(), (int) point.getY());
    }

    public static int[][] getRandomAdjMatrix(int n) {
        int[][] adjM = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) adjM[i][j] = (int) Math.round(Math.random());
            }
            System.out.println(Arrays.toString(adjM[i]));
        }
        return adjM;
    }

    public static Point2D cutVectorInbound(Point2D vector, int minX, int minY, int maxX, int maxY) {
        Point2D result;

        double x, y;
        if (vector.getX() < minX)
            x = minX;
        else x = Math.min(maxX, vector.getX());

        if (vector.getY() < minY)
            y = minY;
        else y = Math.min(maxY, vector.getY());
        result = new Point2D(x, y);

        return result;
    }

    public static Point2D transformVectorInbound(Point2D target, Point2D start, int minX, int minY, int maxX, int maxY) {
        Point2D result;

        double x, y, tmp;
        x = target.getX();
        y = target.getY();
        double length = start.distance(target);
        if (x < minX || x > maxX) {
            if (x < minX) {
                x = minX;
            } else x = maxX;
            y = calcCircleCoord(target, x, length, start.getY());

            if (y < minY) y = minY;
            else if (y > maxY) y = maxY;
        } else if (y < minY || y > maxY) {
            if (y < minY) {
                y = minY;
            } else y = maxY;
            x = calcCircleCoord(target, y, length, start.getX());

            if (x < minX)
                x = minX;
            else if (x > maxX) x = maxX;
        }

        result = new Point2D(x, y);

        return result;
    }

    static double calcCircleCoord(Point2D target, double coord, double length, double centerCoord) {
        double result;
        double tmp;
        result = calcCircleCoord(length, coord);
        tmp = result - centerCoord;
        result = result + centerCoord;
        if (target.distance(coord, tmp) < target.distance(coord, result)) {
            result = tmp;
        }
        return result;
    }

    public static List<Point2D> transformInbounds(List<Point2D> point2DS, int minX, int minY, int maxX, int maxY) {
        List<Point2D> result = new ArrayList<>();

        for (Point2D point2D : point2DS) {
            result.add(cutVectorInbound(point2D, minX, minY, maxX, maxY));
        }

        return result;
    }

    static double calcCircleCoord(double radius, double coord) {
        return Math.sqrt(radius * radius - coord * coord);
    }


    public static double getMaxForce(List<Point2D> forces) {
        double result = 0;
        if (forces != null) {
            for (Point2D force : forces) {
                result = getMaxForce(result, force);
            }
        } else result = Double.MAX_VALUE;
//        System.out.println("threshold: "+result);
        return result;
    }

    public static double getMaxForce(double value, Point2D force) {
        value = Math.max(value, force.magnitude());
        if (value == Double.POSITIVE_INFINITY) value = Double.MAX_VALUE;
        return value;
    }

    public static int getForceIndex(List<Point2D> forces, double value) {
        int result = -1;
        if (forces != null) {
            for (int i=0; i<forces.size(); i++) {
                if(forces.get(i).magnitude()==value)result=i;
            }
        }
        return result;
    }

    public static double roundToDecimal(double value, int decimal) {
        double decVal = Math.pow(10, decimal);
        return ((double)((int)(Math.floor(value *decVal))))/decVal;
    }

    public static void main(String[] args) {
        Point2D node1 = new Point2D(5, 10);
        Point2D node2 = new Point2D(20, 8);
        System.out.println(unitVectorOfTwoPoints(node1, node2));
        System.out.println(unitVectorOfTwoPoints(node1, node2).multiply(1/Math.pow(node2.distance(node1),2)));
         node1 = new Point2D(55, 110);
         node2 = new Point2D(70, 108);
        System.out.println(unitVectorOfTwoPoints(node1, node2));
        System.out.println(unitVectorOfTwoPoints(node1, node2).multiply(1/Math.pow(node2.distance(node1),2)));
    }
}
