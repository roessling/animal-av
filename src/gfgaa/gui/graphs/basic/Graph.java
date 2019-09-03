package gfgaa.gui.graphs.basic;

import gfgaa.gui.graphs.AbstractGraph;

import java.util.ArrayList;

/** Graph object<br>
  * Contains all necassary informations and methods to work on a basic graph.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class Graph extends AbstractGraph {

    /** Constructor<br>
      * Creates a new basic graph object.
      *
      * @param directed     Directed attribut
      * @param weighted     Weighted attribut
      */
    public Graph(final boolean directed, final boolean weighted) {
        super(AbstractGraph.GRAPHSIZE_BASIC);

        this.setDirected(directed);
        this.setWeighted(weighted);
    }

    /** (<b>public method</b>)<br>
      * Returns the typ of the graph.
      *
      * @return typ     Graphtyp constant
      */
    public Integer getGraphTyp() {
        return AbstractGraph.GRAPHTYP_BASIC;
    }

    /** (<b>public method</b>)<br>
      * Returns the maximum size of this graphtyp.
      *
      * @return     Graphsize constant
      */
    public int maxsize() {
        return AbstractGraph.GRAPHSIZE_BASIC;
    }

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (<b>public method</b>)<br>
      * Initializes the node paramert that is used to save a
      * temporary-node specific-value.
      */
    public void initializeNodeValues() {
        int anz = this.getNumberOfNodes();
        for (int i = 0; i < anz; i++) {
            ((Node) this.getNode(i)).setNodeValue(0);
        }
    }

    /** (<b>public method</b>)<br>
      * Set all visited flags of the contained nodes to FALSE.
      */
    public void initializeVisitedFlags() {
        int anz = this.getNumberOfNodes();
        for (int i = 0; i < anz; i++) {
            ((Node) this.getNode(i)).setVisited(false);
        }
    }

    /** (internal data method)<br>
      * Transforms the graph into directed or not directed.
      *
      * @param directed     New directed attribute flag
      */
    public void setDirected(final boolean directed) {

        // Wenn der Graph ungerichtet ist ...
        if (!directed && this.isDirected()) {

            ArrayList<Edge> toRemove = new ArrayList<Edge>();
            Node node, target, source;
            Edge edge, newEdge;

            // überprüfe alle Knoten ...
            for (int i = 0; i < this.getNumberOfNodes(); i++) {
                node = (Node) getNode(i);

                // ..., ob ihre Kanten umgedreht werden müssen.
                int anz = node.getNumberOfEdges();
                for (int j = 0; j < anz; j++) {
                    edge = (Edge) node.getEdge(j);
                    String sTag = edge.getTag();

                    // überprüfe alphabetische Ordnung
                    if (sTag.charAt(0) > sTag.charAt(3)) {

                        String sEdge = sTag.charAt(3) + "->" + sTag.charAt(0);
                        if (!this.containsTag(sEdge)) {
                            target = (Node) edge.getTarget();
                            source = (Node) edge.getSource();

                            newEdge = new Edge(target, source,
                                               edge.getWeight());

                            target.addEdge(newEdge);
                            source.addAgainstEdge(newEdge);
                        }

                        toRemove.add(edge);
                    }
                }
            }

            // Entfernen der umgedrehten Kanten
            int anz = toRemove.size();
            for (int i = 0; i < anz; i++) {
                this.removeEdge((Edge) toRemove.get(i));
            }
        }

        // Anpassen des Attributes
        super.setDirected(directed);
    }
}
