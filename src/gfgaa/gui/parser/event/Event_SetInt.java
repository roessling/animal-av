package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to set an integer.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class Event_SetInt implements ParserActionInterface {

    /** Position that should be used to save the integer. */
    private int position = 0;

    /** The given integer value. */
    private int value = 0;

    /** (Constructor)<br>
      * Creates a "Set a Integer" Event.
      *
      * @param position     Position that should be used to save the integer.
      * @param value        The integer value.
      */
    public Event_SetInt(final int position, final int value) {
        this.position = position;
        this.value = value;
    }

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        data.nVal[position] = value;
        return true;
    }
}
