package gfgaa.gui.parser.event.basic;

import gfgaa.gui.graphs.basic.Graph;
import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to create a new (basic) graph.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class Event_Basic_CreateGraph implements ParserActionInterface {

    /** Graph Creation Modus. */
    private int modus;

    /** (Constructor)<br>
      * Creates a "Create a BasicGraph" Event.
      *
      * @param modus        Graph Creation Modus
      */
    public Event_Basic_CreateGraph(final int modus) {
        this.modus = modus;
    }

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        data.modus = modus;
        data.graph = new Graph(false, false);

        return true;
    }
}
