package gfgaa.gui.parser.event.basic;

import gfgaa.gui.graphs.basic.Edge;
import gfgaa.gui.graphs.basic.Node;
import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to create a new (Basic)Edge.
  *
  * The following conditions will be tested:
  * - the weight
  * - the direction of the edge in relation to the graphs directed attribute
  * - if the edge is already contained
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class Event_Basic_CreateEdge implements ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        if (data.modus == ParserUnit.MODUS_READ_AS_GRAPH
            && data.done != data.give) {

            data.done++;

            if (data.nVal[0] < 1) {

                data.nVal[0] = 1;
                data.addErrorMessage(tok, 9);

            } else if (data.nVal[0] > 99) {

                data.nVal[0] = 99;
                data.addErrorMessage(tok, 10);
            }

            Node node1, node2;
            if (data.graph.isDirected() || data.cVal[0] <= data.cVal[1]) {

                node1 = (Node) data.graph.getNode(data.cVal[0]);
                node2 = (Node) data.graph.getNode(data.cVal[1]);

            } else if (!data.graph.isDirected()
                       && data.graph.containsTag(data.cVal[0]
                                                 + "->" + data.cVal[1])) {

                data.addErrorMessage(tok, 16);
                return false;

            } else {

                node1 = (Node) data.graph.getNode(data.cVal[1]);
                node2 = (Node) data.graph.getNode(data.cVal[0]);
            }

            if (data.graph.containsTag(node1.getTag() + "->"
                                       + node2.getTag())) {

                data.addErrorMessage(tok, 15);
                return false;
            }

            Edge edge = new Edge(node1, node2, data.nVal[0]);
            node1.addEdge(edge);
            node2.addAgainstEdge(edge);
        }

        return true;
    }
}
