package generators.graph;

import algoanim.util.Coordinates;

class GraphNode extends Coordinates {
    int id;
    int balance;
    //List<GraphArc> outgoingArcs = new ArrayList<>();
    //Circle coloredCircle;

    GraphNode(int id, Coordinates coordinates, int balance) {
        super(coordinates.getX(), coordinates.getY());
        this.id = id;
        this.balance = balance;
    }

}
