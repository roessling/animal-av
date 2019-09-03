package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to read an integer.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class Event_ReadInt implements ParserActionInterface {

    /** Position that should be used to save the readed integer. */
    private int position = 0;

    /** (Constructor)<br>
      * Creates a "Read a Integer" Event.
      */
    public Event_ReadInt() {
    }

    /** (Constructor)<br>
      * Creates a "Read a Integer" Event.
      *
      * @param position     Position that should be used to save
      *                     the readed integer.
      */
    public Event_ReadInt(final int position) {
        this.position = position;
    }

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        data.nVal[position] = (int) tok.nval;
        return true;
    }
}
