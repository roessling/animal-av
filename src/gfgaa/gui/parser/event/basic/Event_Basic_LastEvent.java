package gfgaa.gui.parser.event.basic;

import gfgaa.gui.graphs.basic.Edge;
import gfgaa.gui.graphs.basic.Node;
import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to check if the pending operation has already finished.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class Event_Basic_LastEvent implements ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        if (data.give != data.done) {
            switch (data.state) {
            	//Madieha
            	case ParserUnit.STATE_LOADING_OBEREECKE:
            			return new Event_Basic_CreateObereEcke().execute(tok, data);
                case ParserUnit.STATE_LOADING_NODE:
                        return new Event_Basic_CreateNode().execute(tok, data);
          	
                case ParserUnit.STATE_LOADING_EDGE:
                        return new Event_Basic_CreateEdge().execute(tok, data);
                
                case ParserUnit.STATE_LOADING_MATRIX:

                        int i, j;
                        Node nodeA, nodeB;
                        Edge edge;

                        for (i = 0; i < data.nrNodes; i++) {

                            nodeA = (Node) data.graph.getNode(i);
                            for (j = 0; j < data.nrNodes; j++) {

                                if (data.matrix[i][j] < 0) {

                                } else if (data.matrix[i][j] > 0) {

                                    if (data.graph.isWeighted()) {
                                        if (data.matrix[i][j] > 1) {
                                            data.matrix[i][j] = 1;
                                        }

                                    } else if (data.matrix[i][j] > 99) {
                                        data.matrix[i][j] = 99;
                                    }

                                    nodeB = (Node) data.graph.getNode(j);

                                    if (i > j && !data.graph.isDirected()) {

                                        if (!data.graph.containsTag(
                                                nodeB.getTag() + "->"
                                                + nodeA.getTag())) {

                                            edge = new Edge(nodeB, nodeA,
                                                            data.matrix[i][j]);

                                            nodeB.addEdge(edge);
                                            nodeA.addAgainstEdge(edge);
                                        }

                                    } else {
                                        edge = new Edge(nodeA, nodeB,
                                                        data.matrix[i][j]);
                                        nodeA.addEdge(edge);
                                        nodeB.addAgainstEdge(edge);
                                    }
                                }
                            }
                        }

                    break;
            }
        }

        return true;
    }
}
