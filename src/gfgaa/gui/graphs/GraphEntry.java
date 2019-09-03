package gfgaa.gui.graphs;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.others.LanguageInterface;
import gfgaa.gui.parser.BNFNode;

/** GraphAlgorithm Database Entry<br>
  * This class represents the superclass of all
  * graphalgorithm database entries.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public abstract class GraphEntry implements LanguageInterface {

    /** Reference to the graph. */
    protected AbstractGraph graph;

    /** Reference to the projects mainclass. */
    protected GraphAlgController mainclass;

    /** (constructor)<br>
      * Creates a new graphalgorithm entry object.
      *
      * @param mainclass        Reference to the projects mainclass
      */
    public GraphEntry(final GraphAlgController mainclass) {
        this.mainclass = mainclass;
    }

    /** (internal info method)<br>
      * Returns the graphtyp identifier.
      *
      * @return     Graphtyp identifier
      */
    public final Integer getTyp() {
        return graph.getGraphTyp();
    }

    /** (internal data method)<br>
      * Returns a reference on the graph object.
      *
      * @return     Graph object
      */
    public final AbstractGraph getGraph() {
        return this.graph;
    }

    /** (internal info method)<br>
      * Returns the name of the graphtyp.
      *
      * @return     Name of the graphtyp.
      */
    public final String toString() {
        return getTitle();
    }

    /** (internal info method)<br>
      * Returns the name of the graphtyp.
      *
      * @return     Name of the graphtyp.
      */
    public abstract String getTitle();

    /** (internal info method)<br>
      * Returns a description of the graphtyp.
      *
      * @return     Description
      */
    public abstract String getDescription();

    /** (internal data method)<br>
      * Returns the BNF Pane for this graphtyp.
      *
      * @return     BNFPane
      */
    //public abstract SyntaxPane getBNFPane();

    /** (internal data method)<br>
      * Returns the root node of the parsertree.
      *
      * @return     Root node of the parsertree.
      */
    public abstract BNFNode getParserRoot();

    /** (internal construction method)<br>
      * Creates an empty graph object.
      *
      * @return         Reference to an empty graph object
      */
    public abstract AbstractGraph createGraph();

    /** (internal data method)<br>
      * Saves a graph object.
      *
      * @param graph    Reference to a graph object
      */
    public abstract void setGraph(final AbstractGraph graph);

    /** (internal data method)<br>
      * Transforms the saved graph into graphscript notation.
      *
      * @return     A StringBuffer containing the graphscript notation
      */
    public abstract StringBuffer transfer();

    public abstract SPanel createKantenPanel();
    public abstract SPanel createMatrixPanel();
    public abstract SPanel createCreateGraphPanel();
}
