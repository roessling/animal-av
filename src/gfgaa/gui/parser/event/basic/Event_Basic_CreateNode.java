package gfgaa.gui.parser.event.basic;

import gfgaa.gui.graphs.basic.Node;
import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to create a new (Basic)Node.
  *
  * The following conditions will be tested:
  * - the number of added nodes
  * - the x-axis placement
  * - the y-axis placement
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class Event_Basic_CreateNode implements ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        if (data.done != data.give) {
            data.done++;

            int size = data.graph.getNumberOfNodes();
           
            if (size >= data.graph.maxsize()) {
            	

                data.addErrorMessage(tok, 1);
                return true;

            } else if (size > data.nrNodes) {

                data.nrNodes++;
                data.addErrorMessage(tok, 2);
            }

            if (data.nVal[0] < 30) {

                data.nVal[0] = 30;
                data.addErrorMessage(tok, 3);

            } else if (data.nVal[0] > 999) {

                data.nVal[0] = 999;
                data.addErrorMessage(tok, 4);
            }

            if (data.nVal[1] < 30) {

                data.nVal[1] = 30;
                data.addErrorMessage(tok, 5);

            } else if (data.nVal[1] > 999) {

                data.nVal[1] = 999;
                data.addErrorMessage(tok, 6);
            }

            data.graph.addNode(new Node(data.cVal[0], data.nVal[0],
                                        data.nVal[1]));
        }

        return true;
    }
}
