package gfgaa.gui.parser;

import gfgaa.gui.graphs.AbstractGraph;

import java.io.StreamTokenizer;
import java.util.ArrayList;

/** Parser Data class<br>
  * This class saves all necessary data that is used while parsing a file.
  *
  * @author S. Kulessa
  * @version 0.97b
  */
public final class ParserUnit {

/*+PARSER+MODE++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Parser mode variable. */
    public int modus;

    /** Constant used to indicate that the graphscript stream used
      * the graph notation. */
    public static final int MODUS_READ_AS_GRAPH = 1;

    /** Constant used to indicate that the graphscript stream used
      * the matrix notation. */
    public static final int MODUS_READ_AS_MATRIX = 2;

/*+PARSER+STATE+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Parser state variable. */
    public int state;

    /** Constant used to indicate that the parser is currently inactive. */
    public static final int STATE_INACTIVE = -1;

    /** Constant used to indicate that the parser is currently inactive. */
    public static final int STATE_LOADING_INFO = 1;

    /** Constant used to indicate that the parser is
      * currently reading node informations. */
    public static final int STATE_LOADING_NODE = 2;

    /** Constant used to indicate that the parser is
      * currently reading edge informations. */
    public static final int STATE_LOADING_EDGE = 3;

    /** Constant used to indicate that the parser is
      * currently reading matrix informations. */
    public static final int STATE_LOADING_MATRIX = 4;
    
    //Madieha
    public static final int STATE_LOADING_OBEREECKE = 5;
    //Madieha
    public static final int STATE_LOADING_StartKnoten = 8;
    public static final int STATE_LOADING_ZielKnoten = 9;
    /** Constant used to indicate that the parser has finished succesfull. */
    public static final int STATE_FINISHED_PARSING = 6;

    /** Constant used to indicate that the parser has been terminated. */
    public static final int STATE_PARSING_CANCELD = 7;

/*+CONSTRUCTOR++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Number of temporary usable integer slots. */
    private int nSize;

    /** Number of temporary usable character slots. */
    private int cSize;

    /** (constructor)<br>
      * Creates a new ParserUnit object.
      *
      * @param nSize    Number of temporary usable integer slots.
      * @param cSize    Number of temporary usable character slots.
      */
    public ParserUnit(final int nSize, final int cSize) {

        this.state = STATE_INACTIVE;
        this.nSize = nSize;
        this.cSize = cSize;
    }

/*+TEMPORARY+PARSER+DATA++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Temporary value - Number of expected nodes. */
    public int nrNodes;

    /** Temporary counter - number of given objectives. */
    public int give;

    /** Temporary counter - number of processed objectives. */
    public int done;

    /** Temporary usable integer slots. */
    public int[] nVal;

    /** Temporary usable character slots. */
    public char[] cVal;

    /** Temporary used variable to save the complete matrix. */
    public int[][] matrix;

    /** The parsed graph. */
    public AbstractGraph graph;

    /** (internal parser method)<br>
      * Initializes the parser specific data.
      */
    public void init() {
        this.state = STATE_LOADING_INFO;

        this.nVal = new int[nSize];
        this.cVal = new char[cSize];

        this.matrix = null;

        errorMessages = new ArrayList<String>();

        this.give = 0;
        this.done = 0;
    }

/*+PARSER+ERROR+MESSAGES++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** A list that contains all errors that occurred while parsing. */
    public ArrayList<String> errorMessages;

    /** (parser method)<br>
      * Adds an error message to the parser log.
      *
      * @param tok          StreamTokenizer
      * @param errorId      Error Id
      */
    public void addErrorMessage(final StreamTokenizer tok,
                                final int errorId) {
        String sMes;
        switch (errorId) {
            case  1: sMes = "Graph size error -> "
                          + "The graph has already achieved his maximum size.";
                break;
            case  2: sMes = "Graph size error -> "
                          + "The graph size has been increased to "
                          + nrNodes + ".";
                break;
            case  3: sMes = "x-axis position invalid -> "
                          + "xPos has been increased to " + nVal[0] + ".";
                break;
            case  4: sMes = "x-axis position invalid -> "
                          + "xPos has been decreased to " + nVal[0] + ".";
                break;
            case  5: sMes = "y-axis position invalid -> "
                          + "yPos has been increased to " + nVal[1] + ".";
                break;
            case  6: sMes = "y-axis position invalid -> "
                          + "yPos has been decreased to " + nVal[1] + ".";
                break;
            case  7: sMes = "Group value invalid -> "
                          + "groupId has been adjusted to 0";
                break;
            case  8: sMes = "Function value invalid -> "
                          + "function has been adjusted to <leiter>";
                break;
            case  9: sMes = "Weight invalid -> "
                          + "Weight was increased to " + nVal[0] + ".";
                break;
            case 10: sMes = "Weight invalid -> "
                          + "Weight was decreased to " + nVal[0] + ".";
                break;
            case 11: sMes = "Capacity invalid -> "
                          + "Capacity was increased to " + nVal[0] + ".";
                break;
            case 12: sMes = "Capacity invalid -> "
                          + "Capacity was decreased to " + nVal[0] + ".";
                break;
            case 13: sMes = "Flow invalid -> "
                          + "Flow was increased to " + nVal[1] + ".";
                break;
            case 14: sMes = "Flow invalid -> "
                          + "Flow was decreased to " + nVal[1] + ".";
                break;
            case 15: sMes = "Invalid Edge Entry -> "
                          + "Edge already exist -> Line ignored";
                break;
            case 16: sMes = "Invalid Edge Entry -> "
                          + "similar Edge already exist -> Line ignored";
                break;
            case 17: sMes = "Graph size error -> "
                          + "The size of the graph must be in the interval"
                          + " [2, " + graph.maxsize() + "].";
                break;
            case 18: sMes = "Invalid Node Tag"
                          + " -> Node was already contained -> Line ignored";
                break;
            case 19: sMes = "Invalid Node Tag"
                          + " -> Node Tag out of Range -> Line ignored";
               break;
            case 20: sMes = "Invalid Edge Entry"
                          + " -> One of the Nodes is unknown -> Line ignored";
                break;
            case 21: sMes = "Invalid Node Tag -> Invalid tag Length ->"
                          + " Only first char is used";
                break;
            default:
                sMes = "Internal Error" + " -> Unknown Error Id";
        }

        errorMessages.add("Error in Line " + tok.lineno()
                          + "\t-> " + sMes + "\n");
    }
}
