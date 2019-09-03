package gfgaa.gui.parser;

import java.io.StreamTokenizer;

/** Interface<br>
  * This interface represents the link to the parser tree classes.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public interface ParserTreeInterface {

    /** Constant - Tokenizer Value for EOF. */
    int EOF = StreamTokenizer.TT_EOF;

    /** Constant -  Tokenizer Value for EOL. */
    int EOL = StreamTokenizer.TT_EOL;

    /** Constant -  Tokenizer Value for NUMBER. */
    int NUMBER = StreamTokenizer.TT_NUMBER;

    /** Constant -  Tokenizer Value for WORD. */
    int WORD = StreamTokenizer.TT_WORD;

    /** (parser method)<br>
      * Returns the root node of the parser tree.
      *
      * @return     Root node
      */
    BNFNode getRoot();
}
