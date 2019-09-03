package gfgaa.gui.parser;

import java.io.StreamTokenizer;

/** Interface<br>
  * This interface represents the link to the ParserAction Events.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public interface ParserActionInterface {

    /** (Parser method)<br>
      * Executes the ParserAction Event.
      *
      * @param tok          StreamTokenizer
      * @param data         ParserUnit
      * @return             TRUE if the action was succesfull,
      *                     FALSE otherwise
      */
    boolean execute(final StreamTokenizer tok, final ParserUnit data);
}
