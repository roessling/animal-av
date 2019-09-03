package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to read a element of the matrix.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class Event_ReadRowElement implements ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        if (data.nVal[1] >= data.nrNodes || data.nVal[0] >= data.nrNodes) {
            return false;
        }

        data.matrix[data.nVal[0]][data.nVal[1]++] = (int) tok.nval;
        return true;
    }

}
