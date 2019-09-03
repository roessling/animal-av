/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.graph.forcedirectedgraphdrawing.algo;

import generators.graph.helpers.Point2D;

import java.util.ArrayList;
import java.util.List;

public class SpringElectricalForces {
    double idealDistance;
    double C_REP;
    double C_SPRING;
    Graph2D graph;
    final int RADIUS = 25;
    int minX, minY, maxX, maxY;
    boolean bounded = false;
    int coolingType =1;
    double maxIterations=1;
    double constant;

    public SpringElectricalForces(double c_SPRING, double c_REP, Graph2D graph, double idealDistance) {
        C_REP = c_REP;
        C_SPRING = c_SPRING;
        this.graph = graph;
        this.idealDistance = idealDistance;
    }

    public SpringElectricalForces(double c_SPRING, double c_REP, Graph2D graph, Point2D upperLeftCorner, int width, int height, double idealDistance, boolean withCooling) {
        C_REP = c_REP;
        C_SPRING = c_SPRING;
        this.graph = graph;
        bounded = true;
        this.minX = (int) upperLeftCorner.getX();
        this.minY = (int) upperLeftCorner.getY();
        this.maxX = (int) upperLeftCorner.getX() + width - 1;
        this.maxY = (int) upperLeftCorner.getY() + height - 1;
        this.idealDistance = idealDistance;
        if(withCooling)coolingType=1;
        else coolingType =0;
    }

    Point2D calcRepulsiveForce(int node1, int node2) {
        Point2D point1 = graph.getPositionOfNode(node1);
        Point2D point2 = graph.getPositionOfNode(node2);
        Point2D result = Util.unitVectorOfTwoPoints(point1, point2);
        double tmp, dist;
        dist = point1.distance(point2);
        if (dist <= 2 * RADIUS) dist = 0.1;
        tmp = C_REP / Math.pow(dist, 2);
        if (tmp == Double.NaN || tmp == Double.NEGATIVE_INFINITY) {
            System.out.println("MathErr: REP: " + node1 + " " + node2 + " " + tmp + " " + result);
            System.out.println("MathErr: dist: " + point1.distance(point2));
            tmp = Double.MIN_VALUE;
        } else if (tmp == Double.POSITIVE_INFINITY) {
            System.out.println("MathErr: rep posinf");
            tmp = 1000;
        }
        result = result.multiply(tmp);
        signalCalcRepulsiveForce(node1, node2, Util.unitVectorOfTwoPoints(point1, point2), dist, result);
        return result;
    }

    Point2D calcAttractiveForce(int node1, int node2) {
        Point2D point1 = graph.getPositionOfNode(node1);
        Point2D point2 = graph.getPositionOfNode(node2);
        Point2D result = Util.unitVectorOfTwoPoints(point1, point2);
//        System.out.println("unitvektor: "+result);
        double tmp, dist;
        dist = point1.distance(point2);
        if (dist <= 2 * RADIUS) dist = 0.1;
        tmp = C_SPRING * Math.log(dist / getL(node1, node2));
        if (tmp == Double.NaN || tmp == Double.NEGATIVE_INFINITY) {
            System.out.println("MathErr: SPRING: " + node1 + " " + node2 + " " + tmp + " " + result);
            System.out.println("MathErr: dist: " + point1.distance(point2));
            System.out.println("MathErr: log: " + Math.log(point1.distance(point2)));
            tmp = Double.MIN_VALUE;
        } else if (tmp == Double.POSITIVE_INFINITY) {
            System.out.println("MathErr: attr posinf");
            tmp = 1000;
        }
        result = result.multiply(tmp);
        signalCalcAttractiveForce(node1, node2, Util.unitVectorOfTwoPoints(point1, point2), dist, getL(node1, node2), result);
        return result;
    }


    Point2D calcForces(int node) {
        Point2D result;
        List<Integer> nodes = graph.getAdjacentNodes(node);
        List<Integer> notNodes = graph.getNonAdjacentNodes(node);
        Point2D fSpring = new Point2D(0, 0);
        Point2D fRep = new Point2D(0, 0);
        Point2D tempForce;
        for (Integer node2 : nodes) {
            tempForce = calcAttractiveForce(node2, node);
            fSpring = tempForce.add(fSpring);
            signalNormalizedAttrVector(node, node2, fSpring, tempForce);
        }
        for (Integer node2 : notNodes) {
            tempForce = calcRepulsiveForce(node2, node);
            fRep = tempForce.add(fRep);
            signalNormalizedRepVector(node, node2, fRep.add(fSpring), tempForce);
        }
        result = fRep.add(fSpring);
        return result;
    }

    /**
     * returns the ideal spring length for the edge
     *
     * @param node1
     * @param node2
     * @return
     */
    double getL(int node1, int node2) {
        return idealDistance;
    }

    double coolingFunction(double index, double value){
        double result = value;

        if(coolingType == 1){
            //linear cooling, better results
            result = (((-index) / maxIterations) + 1) * value;
        }else if(coolingType==2){
            //hyperbolic
            result = value/index;
        }
        //else -> no cooling
//        System.out.println("temperature: "+result);
        return result;
    }

    public void runAlgo(int maxIterations, double threshold, double constant) {
        int t = 1;
        this.maxIterations = maxIterations;
        this.constant = constant;

        List<Point2D> forces = null;
        List<Point2D> coords;
        Point2D tempCoord;
        Point2D tempForce;
        double cooledConstant;
        double maxForce = Util.getMaxForce(forces);
//        signalMaxForce(0, maxForce);
        signalCoords(0, this.graph.getPoints());
        while (t <= maxIterations && maxForce > threshold) {
            signalIteration(t);
            forces = new ArrayList<>();
            coords = new ArrayList<>();
            for (int i = 0; i < graph.getSize(); i++) {
                tempForce = calcForces(i);
                signalDisplacementVectorForce(t, i, tempForce);
                forces.add(tempForce);
            }
            for (int i = 0; i < graph.getSize(); i++) {
                tempCoord = calcNewCoordinate(forces.get(i), t, i);
                signalSingleCoord(t, i, tempCoord);
                coords.add(tempCoord);
//                graph.translateNode(i, tempCoord);
            }
//            System.out.println("update " + coords.toString());
//            System.out.println("forces " + forces.toString());
            maxForce = Util.getMaxForce(forces);
            signalMaxForce(t, maxForce);
            signalForces(t, forces);
            signalCoords(t, coords);
            graph.updateGraph(coords);
            t++;
        }
        signalResult(t-1, maxIterations, maxForce, threshold);
    }

    Point2D calcNewCoordinate(Point2D force, int iteration, int node) {
        double cooledConstant;
        Point2D tempCoord;
        cooledConstant = coolingFunction(iteration, constant);
        signalCoolingDelta(iteration, cooledConstant);
        tempCoord = graph.getPositionOfNode(node).add(force.multiply(cooledConstant));
        if (bounded) {
            tempCoord = Util.cutVectorInbound(tempCoord, minX, minY, maxX, maxY);
//                    tempCoord = Util.transformVectorInbound(tempCoord, graph.getPositionOfNode(i), minX, minY, maxX, maxY);
            signalCutVectorInbound(iteration, node, tempCoord);
        }
        return tempCoord;
    }

    void signalIteration(int iteration){

    }

    void signalMaxForce(int t, double maxForce) {
        System.out.println("iter end: " + t + " " + maxForce);
    }

    void signalForces(int t, List<Point2D> forces){

    }

    void signalCoords(int t, List<Point2D> coords){

    }

    void signalSingleCoord(int iteration, int node, Point2D coord){

    }

    void signalCutVectorInbound(int iteration, int node, Point2D coord){

    }

    void signalDisplacementVectorForce(int iteration, int node, Point2D force){}

    void signalCalcAttractiveForce(int node, int node2, Point2D unitVector, double distance, double idealDistance, Point2D resultForce){}
    void signalCalcRepulsiveForce(int node1, int node2, Point2D unitVector, double distance, Point2D resultForce){}

    void signalNormalizedAttrVector(int baseNode, int node2, Point2D totalForce, Point2D currentForce){}
    void signalNormalizedRepVector(int baseNode, int node2, Point2D totalForce, Point2D currentForce){}

    void signalCoolingDelta(int iteration, double delta){}

    void signalResult(int t, int maxIterations, double maxForce, double threshold) {}

}
