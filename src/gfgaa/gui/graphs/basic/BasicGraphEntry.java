package gfgaa.gui.graphs.basic;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.GraphScriptWriter;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.GraphEntry;
import gfgaa.gui.parser.BNFNode;
import gfgaa.gui.parser.bnftree.BasicTree;

/** GraphAlgorithm Database Entry<br>
  * This class represents the graphalgorithm database entry
  * for the basic graphtyp.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class BasicGraphEntry extends GraphEntry {

    /** Reference to the parsertree. */
    private BasicTree parserTree;

    /** Reference to the BNF Pane. */
    //private BasicSyntax bnfPane;

    /** (constructor)<br>
      * Creates a new graphalgorithm entry object.
      *
      * @param mainclass        Reference to the projects mainclass
      */
    public BasicGraphEntry(final GraphAlgController mainclass) {
        super(mainclass);
        parserTree = new BasicTree();
        //bnfPane = new BasicSyntax(mainclass);
        createGraph();
    }

    /** (internal info method)<br>
      * Returns the name of the graphtyp.
      *
      * @return     Name of the graphtyp.
      */
    public String getTitle() {
        if (mainclass.getLanguageSettings() == LANGUAGE_GERMAN) {
            return "Basis Graph";
        } else {
            return "Basic graph";
        }
    }

    /** (internal info method)<br>
      * Returns a description of the graphtyp.
      *
      * @return     Description
      */
    public String getDescription() {
        if (mainclass.getLanguageSettings() == LANGUAGE_GERMAN) {
            return "Die Auswahl Basis Graph repäsentiert den\n"
                  + "Standard Graphen. Dieser kann gewichtet\n"
                  + "oder ungewichtet, wie auch gerichtet oder\n"
                  + "ungerichtet sein.\n\n"
                  + "Der Basis Graph kann maximal 18 Knoten\n"
                  + "enthalten. Seine Kantengewichte sind echt\n"
                  + "positiv und auf das Intervall [1, 99] beschränkt.\n\n"
                  + "Diese Graphklasse eignet sich beispielsweise\n"
                  + "für DFS, Kruskal oder Färbungsproblem\n"
                  + "Animationen.";
        } else {
            return "The selection basicgraph represents the\n"
                 + "standard graph. This graphtyp can optionally\n"
                 + "be weighted and/or directed.\n\n"
                 + "The basic graph can contain up to 18 nodes.\n"
                 + "The weights of the edges have to be strict\n"
                 + "positiv. Only integers from the interval of\n"
                 + "[1, 99] may be used.\n\n"
                 + "This graphtyp is applicable e.g. for dfs, kruskal\n"
                 + "or graphcoloring animations.";
        }
    }

    /** (internal data method)<br>
      * Returns the BNF Pane for basic graphs.
      *
      * @return     Basicgraph BNFPane
      */
   /* public SyntaxPane getBNFPane() {
        return bnfPane;
    }*/

    /** (internal data method)<br>
      * Returns the first root node of the basic parsertree.
      *
      * @return     First root node of the basic parsertree.
      */
    public BNFNode getParserRoot() {
        return parserTree.getRoot();
    }

    /** (internal data method)<br>
      * Returns the second root node of the basic parsertree.
      *
      * @return     Second root node of the basic parsertree.
      */
    public BNFNode getSecondParserRoot() {
        return parserTree.getSecondRoot();
    }

    /** (internal construction method)<br>
      * Creates an empty basic graph.
      *
      * @return         Reference to an empty basic graph
      */
    public AbstractGraph createGraph() {
        this.graph = new Graph(true, true);
        return this.graph;
    }

    /** (internal data method)<br>
      * Saves a basic graph object.
      *
      * @param graph    Reference to a basic graph object
      */
    public void setGraph(final AbstractGraph graph) {
        if (graph instanceof Graph) {
            this.graph = graph;
        }
    }

    /** (internal data method)<br>
      * Transforms the saved graph into graphscript notation.
      *
      * @return     A StringBuffer containing the graphscript notation
      */
    public StringBuffer transfer() {

        int transferModus = mainclass.getTransferMode();

        StringBuffer buf = new StringBuffer("%graphscript\n\n");
        
        
        if (transferModus == GraphScriptWriter.WRITE_AS_MATRIX) {
            buf.append("matrix ");
        } else {
            buf.append("graph ");
        }

        buf.append(graph.getNumberOfNodes());
        
        if (graph.isDirected()) {
            buf.append(" directed");
        }

        boolean weighted = graph.isWeighted();
        if (weighted) {
            buf.append(" weighted\n\n");
        }
       //bouchra
        if(graph.getCorner()==null){graph.setCorner(new Uppercorner(0,0));}
        buf.append("\ngraphcoordinates"+" "+"at"+" "+graph.getCorner().x+
        	" "+graph.getCorner().y+ "\n\n");
        
    	if (graph.getStartNode() != null){
    		buf.append("startknoten "+ graph.getStartNode().getTag()+"\n");
    	}
    	if (graph.getTargetNode() != null){
    		buf.append("zielknoten "+ graph.getTargetNode().getTag()+"\n\n");
    	}

        Node node;
        Edge edge;

        StringBuffer edgeBuf = new StringBuffer("");
        int i, j, anz, size = graph.getNumberOfNodes();

        for (i = 0; i < size; i++) {
            node = (Node) graph.getNode(i);

            buf.append("node " + node.getTag());
            buf.append(" at " + node.getXPos() + " " + node.getYPos() + "\n");
           

            if (transferModus == GraphScriptWriter.WRITE_AS_GRAPH) {
            	

                anz = node.getNumberOfEdges();
                for (j = 0; j < anz; j++) {
                    edge = (Edge) node.getEdge(j);

                    edgeBuf.append("edge " + edge.getSource().getTag());
                    edgeBuf.append(" " + edge.getTarget().getTag());

                    if (weighted) {
                        edgeBuf.append(" weight " + edge.getWeight());
                    }

                    edgeBuf.append("\n");
                }
            }
        }
        
        
        if (transferModus == GraphScriptWriter.WRITE_AS_MATRIX) {

            for (i = 0; i < size; i++) {
                node = (Node) graph.getNode(i);
                edgeBuf.append("[");

                for (j = 0; j < size;) {

                    edge = (Edge) node.getEdgeTo(graph.getNode(j));
                    if (edge == null) {
                        edgeBuf.append("0");
                    } else if (weighted) {
                        edgeBuf.append(edge.getWeight());
                    } else {
                        edgeBuf.append("1");
                    }

                    if (++j != size) {
                        edgeBuf.append("|");
                    }
                }

                edgeBuf.append("]\n");
            }
        }
        
        buf.append("\n");
        buf.append(edgeBuf);
        buf.append("\n");
       
        
        return buf;
    }

    /** (internal creation method)<br>
      * Returns the associated panel to create nodes and edges
      * for this graphtyp.
      *
      * @return     KantenPanel
      */
    public SPanel createKantenPanel() {
        return new KantenPanel(this.mainclass);
    }

    /** (internal creation method)<br>
      * Returns the associated panel to create nodes and edges
      * via the graphs adjacency matrix.
      *
      * @return     MatrixPanel
      */
    public SPanel createMatrixPanel() {
        return new MatrixPanel(this.mainclass);
    }

    /** (internal creation method)<br>
      * Returns the associated panel to controll the attributes
      * of a graph.
      *
      * @return     CreateGraphPanel
      */
    public SPanel createCreateGraphPanel() {
        return new CreateBasicGraphPanel(this.mainclass);
    }
}
