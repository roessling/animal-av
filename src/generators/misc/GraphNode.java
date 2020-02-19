package generators.misc;

import algoanim.util.Coordinates;

class GraphNode extends Coordinates {
    int id;
    int graph;
    int level;
    int columnOffset;
    int graphOffset;
    boolean processed = false;

    GraphNode(int id, int graph, int level, int columnOffset, int graphOffset) {
        super(40 + 40 * columnOffset + 60 * graph + 40 * graphOffset, 100 + 60 * level);
        this.id = id;
        this.graph = graph;
        this.level = level;
        this.columnOffset = columnOffset;
        this.graphOffset = graphOffset;
    }

}
