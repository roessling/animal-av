package gfgaa.gui.graphs.basic;

import gfgaa.gui.graphs.AbstractEdge;
import gfgaa.gui.graphs.AbstractNode;

/** Object for the edges of the basic graph<br>
  * Basic edges contains their start and target node and the specified weight.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class Edge extends AbstractEdge {

    /** Weight of the edge. */
    private int weight;

    /** (constructor)<br>
      * Creates a new Edge object with references to a start and a target node.
      *
      * @param source           Start node
      * @param target           Target node
      * @param weight           New weight
      */
    public Edge(final AbstractNode source, final AbstractNode target,
                final int weight) {

        super(source, target);
        this.weight = weight;
    }

    /** (<b>public method</b>)<br>
      * Returns the weight of this edge.
      *
      * @return     Weight of the edge
      */
    public int getWeight() {
        if (source.getGraph().isWeighted()) {
            return weight;
        } else if (weight == 0) {
            return 0;
        }
        return 1;
    }

    /** (internal data method)<br>
      * Sets the weight of this edge.
      *
      * @param weight      New weight
      */
    public void setWeight(final int weight) {
        this.weight = weight;
    }
}
