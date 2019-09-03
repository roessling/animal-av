package gfgaa.gui.graphs.basic;

import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.AbstractNode;

/** Object for the nodes of the basic graph<br>
  * Contains references to all edges and against-edges which point to or start
  * from this node and a reference to the graph.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class Node extends AbstractNode {

    /** Temporary value. */
    private int nodeValue = 0;

    /** Contains whether the current node has already been visited or not.<br>
      * (used by some GraphAlgorithm)
      */
    private boolean visited = false;

    /** (Constructor)<br>
      * Creates a new Node object with the specified tag and
      * starting position (x, y).
      *
      * @param bez      New tag
      * @param x        New x position
      * @param y        New y position
      */
    public Node(final char bez, final int x, final int y) {
        super(bez, x, y);
    }
    public Node(final char bez){
    	super(bez);
    }

    /** (<b>public method</b>)<br>
      * Returns the value of this node.
      *
      * @return         Value of the Node
      */
    public int getNodeValue() {
        return nodeValue;
    }

    /** (<b>public method</b>)<br>
      * Returns weather the node has already been visited or not.
      *
      * @return         Visited flag
      */
    public boolean isVisited() {
        return this.visited;
    }

    /** (internal data method)<br>
      * Sets the graph that contains this node.
      *
      * @param graph    New graph
      */
    public void setGraph(final AbstractGraph graph) {
        if (graph == null || !(graph instanceof Graph)) {
            throw new IllegalArgumentException("Graph can't be NULL.");
        }
        this.graph = (Graph) graph;
    }

    /** (<b>public method</b>)<br>
      * Adds a temporary value (used by some GraphAlgorithm) to the node.
      *
      * @param nodeValue         New value of the Node
      */
    public void setNodeValue(final int nodeValue) {
        this.nodeValue = nodeValue;
    }

    /** (<b>public method</b>)<br>
      * Sets the visited flag of this node.
      *
      * @param visited          New visited flag
      */
    public void setVisited(final boolean visited) {
        this.visited = visited;
    }
}
