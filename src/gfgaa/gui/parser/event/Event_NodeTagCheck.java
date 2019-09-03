package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

/** ParserAction Event<br>
  * This event is used to read and to validate a node tag.
  *
  * The following conditions will be tested:
  * - the correct length of the tag
  * - the range of the tag [A-Z]
  * - whether the node exist or not
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class Event_NodeTagCheck implements ParserActionInterface {

    /** Flag that indicates wheter the given tag should exist or not. */
    private boolean exist = false;

    /** Position that should be used to save the readed tag. */
    private int position = 0;

    /** (Constructor)<br>
      * Creates a "NodeTag Check" Event.
      */
    public Event_NodeTagCheck() {
    }

    /** (Constructor)<br>
      * Creates a "NodeTag Check" Event.
      *
      * @param position     Position that should be used to
      *                     save the readed tag.
      */
    public Event_NodeTagCheck(final int position) {
        this.position = position;
    }

    /** (Constructor)<br>
      * Creates a "NodeTag Check" Event.
      *
      * @param exist        Indicates wheter the given tag
      *                     should exist or not.
      */
    public Event_NodeTagCheck(final boolean exist) {
        this.exist = exist;
    }

    /** (Constructor)<br>
      * Creates a "NodeTag Check" Event.
      *
      * @param position     Position that should be used
      *                     to save the readed tag.
      * @param exist        Indicates wheter the given tag
      *                     should exist or not.
      */
    public Event_NodeTagCheck(final int position, final boolean exist) {
        this.position = position;
        this.exist = exist;
    }

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             Whether the ParserAction was succesfull or not.
      */
    public boolean execute(final StreamTokenizer tok, final ParserUnit data) {

        String sval = tok.sval.toUpperCase();
        data.cVal[position] = sval.charAt(0);

        // Länge des Tags überprüfen (nur Warnung)
        if (sval.length() != 1) {
            data.addErrorMessage(tok, 21);
        }

        // Korrekten Bereich des Tags sicherstellen
        if (data.cVal[position] < 'A' || data.cVal[position] > 'Z') {

            data.addErrorMessage(tok, 18);
            return false;
        }

        // Stelle sicher das das Tag bereits existiert.
        if (!exist && data.graph.containsTag(sval)) {

            data.addErrorMessage(tok, 19);
            return false;

        // Stelle sicher das das Tag nicht existiert.
        } else if (exist && !data.graph.containsTag(sval)) {

            data.addErrorMessage(tok, 20);
            return false;
        }

        return true;
    }
}
