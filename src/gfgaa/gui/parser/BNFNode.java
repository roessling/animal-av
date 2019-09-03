package gfgaa.gui.parser;

/** The BNFNode object represents a node in the ParserTree's.
  * It contains the necassary informations and actions used
  * from the GraphScriptParser.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public class BNFNode {

    /** Expected Keyword. */
    private String code;

    /** Children of the current BNFNode. */
    private BNFNode[] tail;

    /** Expected token. */
    private int token;

    /** Reference to the optional ParserAction Event. */
    private ParserActionInterface action;

    /** (constructor)<br>
      * Creates a node object without a ParserAction Event.
      *
      * @param typ          Excepted type of token
      * @param n            Fanout of the node
      * @param keyword      Expected keyword
      */
    public BNFNode(final int typ, final int n, final String keyword) {

        this.token = typ;
        this.tail = new BNFNode[n];
  
        for (int a = 0; a < n; a++) {
            this.tail[a] = null;
        }

        this.code = keyword;
    }

    /** (constructor)<br>
      * Creates a node object with a ParserAction Event.
      *
      * @param typ          Excepted type of token
      * @param n            Fanout of the node
      * @param keyword      Expected keyword
      * @param event        ParserAction Event
      */
    public BNFNode(final int typ, final int n, final String keyword,
                   final ParserActionInterface event) {

        this.token = typ;
        this.tail = new BNFNode[n];
    
        for (int a = 0; a < n; a++) {
            this.tail[a] = null;
        }

        this.code = keyword;
        this.action = event;
    }

    /** (internal parser method)<br>
      * Returns the ParserAction Event.
      *
      * @return         ParserAction Event
      */
    public final ParserActionInterface getAction() {
        return action;
    }

    /** (internal parser method)<br>
      * Returns the expected keyword of the child.
      *
      * @param pos      Childs position
      * @return         Expected keyword of the child
      */
    public final String getCode(final int pos) {
    	/*for( int i=0; i<tail.length;i++){
    		System.out.println(tail[i].code);
    	}*/
        return tail[pos].code;
    }

    /** (internal parser method)<br>
      * Returns the fanout of the node.
      *
      * @return         Fanout
      */
    public final int getLength() {
        return tail.length;
    }

    /** (internal parser method)<br>
      * Returns the expected token typ of the child.
      *
      * @param pos      Childs position
      * @return         Token child
      */
    public final int getToken(final int pos) {
       return tail[pos].token;
    }

    /** (internal parser method)<br>
      * Add the current node to the given node.
      *
      * @param parent    New parent of this node
      */
    public final void newInstance(final BNFNode parent) {
        boolean found = false;
        int gr = parent.tail.length;
        int a = 0;

        while ((a < gr) && (!found)) {
            if (parent.tail[a] == null) {
                found = true;
                parent.tail[a] = this;
            }
            a++;
        }

        if (!found) {
            throw new IllegalArgumentException(
                        "ParserTree Build Error:"
                        + " Number of Children to low");
        }
    }

    /** (internal parser method)<br>
      * Returns the specified child of this node to continue parsing.
      *
      * @param pos      Childs position
      * @return         Next node
      */
    public final BNFNode setNext(final int pos) {
        return this.tail[pos];
    }
}
