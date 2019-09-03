package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to initialize the matrix and to count the readed rows.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class Event_ReadMatrixRow implements ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        data.state = ParserUnit.STATE_LOADING_MATRIX;

        if (data.matrix == null) {
            data.give++;

            data.matrix = new int[data.nrNodes][data.nrNodes];
            data.nVal[0] = 0;
            data.nVal[1] = 0;

            return true;
        }

        if (data.nVal[0] >= data.nrNodes) {
            return false;
        }

        data.nVal[0]++;
        data.nVal[1] = 0;

        return true;
    }

}
