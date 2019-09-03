package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * Sets the graphs directed attribute.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class Event_DirectedGraph implements ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        data.graph.setDirected(true);
        return true;
    }
}
