package gfgaa.gui.graphs;

import java.util.ArrayList;

/** Node object<br>
  * Contains references to all edges and against-edges which
  * point to or start from this node and a reference to the graph.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public abstract class AbstractNode {

    /** Tag of the node. */
    private char bez;

    /** x position of the node. */
    private int x;

    /** y position of the node. */
    private int y;

    /** Reference to the graph. */
    protected AbstractGraph graph;

    /** All edges that point to this node. */
    protected ArrayList<AbstractEdge> inArcs;

    /** All edges that start from this node. */
    protected ArrayList<AbstractEdge> outArcs;

    /** (Constructor)<br>
      * Creates a new Node-Object with the specified tag
      * and starting position (x, y).
      *
      * @param bez      New tag
      * @param x        New x position
      * @param y        New y position
      */
    public AbstractNode(final char bez, final int x, final int y) {
        this.bez = bez;
        this.x = x;
        this.y = y;

        this.inArcs = new ArrayList<AbstractEdge>();
        this.outArcs = new ArrayList<AbstractEdge>();
    }
    public AbstractNode(final char bez){
    	this.bez= bez;
    }

    /** (internal method)<br>
      * Sets the reference to the related graph.
      *
      * @param graph        Graph object
      */
    public abstract void setGraph(final AbstractGraph graph);

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Adds the given edge to this node.
      *
      * @param edge         Edge to add
      */
    public void addEdge(final AbstractEdge edge) {
    	this.graph.addTag(edge.getTag());
        this.outArcs.add(edge);
    }

    /** (<b>public method</b>)<br>
      * Returns the specified edge.
      *
      * @param pos          Position of the edge in the egdelist
      * @return             Specified edge
      */
    public AbstractEdge getEdge(final int pos) {
        return (AbstractEdge) this.outArcs.get(pos);
    }

    /** (<b>public method</b>)<br>
      * Returns the number of edge that starts from this node.
      *
      * @return             Number of edges
      */
    public int getNumberOfEdges() {
        return this.outArcs.size();
    }

    /** (internal data method)<br>
      * Removes the specified egde from this node.
      *
      * @param edge         Edge to remove
      * @return             TRUE  - if the edge could be removed
      *                     FALSE - if the edge can't be removed
     */
    public boolean removeEdge(final AbstractEdge edge) {
        int anz = outArcs.size();
        for (int i = 0; i < anz; i++) {
            if (edge == getEdge(i)) {
                outArcs.remove(edge);
                graph.removeTag(edge.getTag());
                return true;
            }
        }
        return false;
    }

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Adds the given against-edge to this node.
      *
      * @param gegenkante       Against-edge to add
      */
    public void addAgainstEdge(final AbstractEdge gegenkante) {
        this.inArcs.add(gegenkante);
    }

    /** (<b>public method</b>)<br>
      * Returns the specified against-edge.
      *
      * @param pos          Position of the against-edge in the
      *                     against-egde list
      * @return             Specified against-edge
      */
    public AbstractEdge getAgainstEdge(final int pos) {
        return (AbstractEdge) this.inArcs.get(pos);
    }

    /** (<b>public method</b>)<br>
      * Returns the number of edges that points to this node.
      *
      * @return             Number of against-edges
      */
    public int getNumberOfAgainstEdges() {
        return this.inArcs.size();
    }

    /** (internal data method)<br>
      * Removes the specified against-egde from this node.
      *
      * @param edge         Edge that should be removed
      * @return             TRUE  - if the against-edge could be removed
      *                     FALSE - if the against-edge can't be removed
      */
    public boolean removeAgainstEdge(final AbstractEdge edge) {
        int i, anz = inArcs.size();
        for (i = 0; i < anz; i++) {
            if (edge == getAgainstEdge(i)) {
                inArcs.remove(edge);
                graph.removeTag(edge.getTag());
                return true;
            }
        }
        return false;
    }

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Returns the graph that contains this node.
      *
      * @return         Graph
      */
    public AbstractGraph getGraph() {
        return this.graph;
    }

    /** (internal data method)<br>
      * Checks weather an edge to the current target exists or not.
      *
      * @param nach         Target node of the wanted edge
      * @return             Specified edge | NULL
      */
    public AbstractEdge getEdgeTo(final AbstractNode nach) {
        int i, anz = getNumberOfEdges();
        AbstractEdge edge;

        for (i = 0; i < anz; i++) {
            edge = getEdge(i);
            if (edge.getTarget() == nach) {
                return edge;
            }
        }
        return null;
    }

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (<b>public method</b>)<br>
      * Returns the tag of this node.
      *
      * @return         Tag of this node
      */
    public char getTag() {
        return this.bez;
    }

    /** (internal info method)<br>
      * Returns the value of the x position.
      *
      * @return     X-Axis position of the node
      */
    public int getXPos() {
        return this.x;
    }

    /** (internal info method)<br>
      * Returns the value of the y position.
      *
      * @return Y-Axis position of the node
      */
    public int getYPos() {
        return this.y;
    }

    /** (internal data method)<br>
      * Saves the position of the node.
      *
      * @param x     New x position
      * @param y     New y position
      */
    public void moveTo(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
}
