package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This Event is used to initialize the temporary used ParserUnit values
  * and to indicate that a EdgeAction is pending.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class Event_EdgeAction implements ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        data.state = ParserUnit.STATE_LOADING_EDGE;

        data.nVal[0] = 1;
        data.nVal[1] = 0;
        data.nVal[2] = -1;
        
        
        data.cVal[0] = 0;
        data.cVal[1] = 0;

        data.give++;
        return true;
    }
}
