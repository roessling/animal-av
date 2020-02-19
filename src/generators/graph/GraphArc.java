package generators.graph;

public class GraphArc {
    int startID;
    int targetID;
    int capacity;
    int cost;
    int flow;
    boolean reversed;
    GraphArc partner;

    public GraphArc(int startID, int targetID, int capacity, int cost, int flow, boolean reversed) {
        this.startID = startID;
        this.targetID = targetID;
        this.capacity = capacity;
        this.cost = cost;
        this.flow = flow;
        this.reversed = reversed;
    }

    public int getCapacity() {
        return capacity;
    }
}
