package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to check the size of the given graph.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class Event_NodesRangeCheck implements ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        data.nrNodes = (int) tok.nval;
        if (data.nrNodes < 2
            || data.nrNodes > data.graph.maxsize()) {

            data.addErrorMessage(tok, 17);
            return false;
        }

        return true;
    }
}
