package gfgaa.gui.graphs;

import gfgaa.gui.graphs.basic.Uppercorner;

import java.util.ArrayList;
import java.util.HashSet;

/** Graph object<br>
  * Contains all necassary informations and methods to work on a graph.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public abstract class AbstractGraph {

    /** Constant used for describing the maximum size of a basic graph. */
    public static final int GRAPHSIZE_BASIC = 18;

    /** Constant used for describing the maximum size of a bipartit graph. */
    public static final int GRAPHSIZE_BIPARTIT = 17;

    /** Constant used for describing the maximum size of a manhattan graph. */
    public static final int GRAPHSIZE_MANHATTAN = 18;

    /** Constant used for describing the maximum size of a negative graph. */
    public static final int GRAPHSIZE_NEGATIVE = 15;

    /** Constant used for describing the maximum size of a residual graph. */
    public static final int GRAPHSIZE_RESIDUAL = 17;

    /** Constant used for describing the basic graph typ. */
    public static final Integer GRAPHTYP_BASIC = new Integer(0);

    /** Constant used for describing the bipartit graph typ. */
    public static final Integer GRAPHTYP_BIPARTIT = new Integer(1);

    /** Constant used for describing the manhattan graph typ. */
    public static final Integer GRAPHTYP_MANHATTAN = new Integer(2);

    /** Constant used for describing the negative graph typ. */
    public static final Integer GRAPHTYP_NEGATIVE = new Integer(3);

    /** Constant used for describing the residual graph typ. */
    public static final Integer GRAPHTYP_RESIDUAL = new Integer(4);

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Standard diameter. */
    private int diameter = 40;

    /** Directed attribute */
    private boolean directed;

    /** Contains all tags of nodes and edges. */
    private HashSet<String> knownTags = new HashSet<String>();

    /** Contains all nodes. */
    private ArrayList<AbstractNode> nodes;

    /** Weighted attribute */
    private boolean weighted;
    //bouchtra
    private AbstractNode startNode; // start Knoten des Graphes
    private AbstractNode targetNode; // ziel Knoten des Graphes

	private Uppercorner ecke;

    /** Constructor<br>
      * Creates an AbstractGraph object.
      *
      * @param size     Maximum graph size constant
      */
    public AbstractGraph(final int size) {
        knownTags = new HashSet<String>();
        nodes = new ArrayList<AbstractNode>(size);
    }

    /** (<b>public method</b>)<br>
      * Returns the typ of the graph.
      *
      * @return typ     Graphtyp constant
      */
    public abstract Integer getGraphTyp();
   

    /** (<b>public method</b>)<br>
      * Returns the maximum size of this graphtyp.
      *
      * @return     Graphsize constant
      */
    public abstract int maxsize();

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (<b>public method</b>)<br>
      * Returns whether the graph is directed or not.
      *
      * @return     TRUE | FALSE
      */
    public boolean isDirected() {
        return this.directed;
    }

    /** (<b>public method</b>)<br>
      * Returns whether the graph is weighted or not.
      *
      * @return     TRUE | FALSE
      */
    public boolean isWeighted() {
        return this.weighted;
    }

    /** (internal method)<br>
      * Sets the directed attribute of the graph.
      *
      * @param directed     New directed attribute
      */
    public void setDirected(final boolean directed) {
        this.directed = directed;
    }

    /** (internal method)<br>
      * Sets the weighted attribute of the graph.
      *
      * @param weighted     New weighted attribute
      */
    public void setWeighted(final boolean weighted) {
        this.weighted = weighted;
    }
 
    //Madieha
    public void setCorner(Uppercorner ecke){
    	this.ecke = ecke;
    }
    //Madieha
    public Uppercorner getCorner(){
    	return this.ecke;
    }

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Add the given node to the graph.
      *
      * @param node     New node
      */
    public void addNode(final AbstractNode node) {

        char cTag = node.getTag();
        if (!containsTag("" + cTag)) {

            addTag("" + node.getTag());
            node.setGraph(this);

            // Knoten wird nach seinem Tag eingefügt
            int anz = getNumberOfNodes();
            for (int i = 0; i < anz; i++) {
                if (cTag < getNode(i).getTag()) {
                    this.nodes.add(i, node);
                    return;
                }
            }

            this.nodes.add(node);
        }
    }

    /** (<b>public method</b>)<br>
      * Returns the node with the specified tag.
      *
      * @param cTag     Specified tag
      * @return         Node
      */
    public AbstractNode getNode(final char cTag) {
        for (int i = 0; i < nodes.size(); i++) {
            AbstractNode node = getNode(i);
            if (node.getTag() == cTag) {
                return node;
            }
        }
        return null;
    }

    /** (<b>public method</b>)<br>
      * Returns the pos-node of the graph.
      *
      * @param pos      Number of the node
      * @return         Node | NULL
     */
    public AbstractNode getNode(final int pos) {
        if (pos > -1 && pos < nodes.size()) {
            return (AbstractNode) this.nodes.get(pos);
        }
        return null;
    }

    /** (<b>public method</b>)<br>
      * Returns the number of nodes the graph contains.
      *
      * @return         Number of nodes
      */
    public int getNumberOfNodes() {
        return this.nodes.size();
    }

    /** (internal data method)<br>
      * Removes the speciefied node from the graph.
      *
      * @param cTag     Tag of the node to remove
      */
    public void removeNode(final char cTag) {
        AbstractNode node = (AbstractNode) getNode(cTag);

        while (node.getNumberOfEdges() > 0) {
            removeEdge(node.getEdge(0));
        }

        while (node.getNumberOfAgainstEdges() > 0) {
            removeEdge(node.getAgainstEdge(0));
        }

        this.nodes.remove(node);
        removeTag("" + cTag);
    }

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Removes the given edge from the graph.
      *
      * @param edge     Edge to remove
      */
    public void removeEdge(final AbstractEdge edge) {
        if (!edge.getSource().removeEdge(edge)) {
            System.out.println("Internal Error at graphen.removeEdge\n -> #1 "
                    + edge.toString() + " can't be removed.\n");
        }
        if (!edge.getTarget().removeAgainstEdge(edge)) {
            System.out.println("Internal Error at graphen.removeEdge\n -> #2 "
                    + edge.toString() + " can't be removed.\n");
        }
    }

    /** (internal data method)<br>
      * Removes the edge which is specified through the given start
      * and end node.
      *
      * @param from     Start node
      * @param to       End node
      */
    public void removeEdge(final AbstractNode from, final AbstractNode to) {
        AbstractEdge edge = from.getEdgeTo(to);
        if (edge == null) {
            System.out.println("Internal Error at graphen.removeEdge\n -> #5 "
                               + "Edge " + from.getTag() + " -> " + to.getTag()
                               + " does not exist.\n");
            return;
        }
        if (!from.removeEdge(edge)) {
            System.out.println("Internal Error at graphen.removeEdge\n -> #3 "
                               + edge.toString() + " can't be removed from"
                               + " Node " + from.getTag() + "\n");
        }
        if (!to.removeAgainstEdge(edge)) {
            System.out.println("Internal Error at graphen.removeEdge\n -> #4 "
                               + edge.toString() + " can't be removed from"
                               + " Node " + to.getTag() + "\n");
        }
    }

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal info method)<br>
      * Returns the diameter of the node animation.
      *
      * @return     Diameter
      */
    public int getDiameter() {
        return this.diameter;
    }

    /** (internal info method)<br>
      * Returns the radius of the node animation.
      *
      * @return     Radius
      */
    public int getRadius() {
        return this.diameter / 2;
    }

    /** (internal data method)<br>
      * Sets the diameter of the node animation.
      *
      * @param diameter     New diameter
      */
    public void setDiameter(final int diameter) {
        this.diameter = diameter;
    }

/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Add the tag to the knownTags Set.
      *
      * @param sTag         New tag
      */
    public void addTag(final String sTag) {
        this.knownTags.add(sTag);
    }

    /** (internal info method)<br>
      * Returns true if the given tag allready exists or false otherwise.
      *
      * @param sTag     Tag that should be checked.
      * @return         TRUE | FALSE
      */
    public boolean containsTag(final String sTag) {
        return this.knownTags.contains(sTag);
    }

    /** (internal data method)<br>
      * Removes the tag from the knownTags Set.
      *
      * @param sTag          Tag to remove
      */
    public void removeTag(final String sTag) {
        this.knownTags.remove(sTag);
    }

    /** (<b>public method</b>)<br>
      * Returns an array containg the node tags in alphabetic order.
      *
      * @return         Array containg the node tags.
      */
    public char[] getNodeTagArray() {
        int anz = this.getNumberOfNodes();
        char[] tags = new char[anz];

        for (int i = 0; i < anz; i++) {
            tags[i] = getNode(i).getTag();
        }

        return tags;
    }
    
    //bo
    public void setStartNode(AbstractNode node){
    	this.startNode = node;
    }
    public AbstractNode getStartNode(){
    	return this.startNode;
    }
    public void setTargetNode(AbstractNode node){
    	this.targetNode = node;
    }
    public AbstractNode getTargetNode(){
    	return this.targetNode;
    }
}
