package gfgaa.gui.graphs;

/** Object for the edges of the graph<br>
  * Edges contains their start and target node and the specified weight.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public abstract class AbstractEdge {

    /** Start node of the edge. */
    protected AbstractNode source;

    /** Target node of the edge. */
    protected AbstractNode target;

    /** (constructor)<br>
      * Creates a new Edge object with references to a start and a target node.
      *
      * @param source       Start node
      * @param target       Target node
      */
    public AbstractEdge(final AbstractNode source,
                         final AbstractNode target) {

        this.source = source;
        this.target = target;
    }

    /** (<b>public method</b>)<br>
      * Returns the weight of the edge.
      *
      * @return     Weight of the edge
      */
    public abstract int getWeight();

    /** (<b>public method</b>)<br>
      * Sets the weight of the edge.
      *
      * @param weight       Weight of the edge
      */
    public abstract void setWeight(final int weight);

    /** (<b>public method</b>)<br>
      * Returns the start node of this edge.
      *
      * @return     Start node
      */
    public AbstractNode getSource() {
        return this.source;
    }

    /** (<b>public method</b>)<br>
      * Returns the target node of this edge.
      *
      * @return     Target node
      */
    public AbstractNode getTarget() {
        return this.target;
    }

    /** (<b>public method</b>)<br>
      * Return the other node.
      *
      * @param node         One node
      * @return             Other node
      */
    public AbstractNode getOtherEnd(final AbstractNode node) {
        if (node == source) {
            return target;
        } else {
            return source;
        }
    }

    /** (<b>public method</b>)<br>
      * Returns the tag of this edge.
      *
      * @return     Tag of this edge
      */
    public String getTag() {
        return source.getTag() + "->" + target.getTag();
    }
}
