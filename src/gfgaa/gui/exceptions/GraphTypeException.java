package gfgaa.gui.exceptions;

import gfgaa.gui.graphs.GraphEntry;

import java.util.ArrayList;

/** Exception class<br>
  * Class for exceptions occuring while adding Graph
  * objects to the GUI / GraphDatabase.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public final class GraphTypeException extends Exception {

    /**
   * 
   */
  private static final long serialVersionUID = 6101420403683909223L;
    /** String containing the error message. */
    private String sMessage = null;

    /** (constructor)<br>
      * Creates a GraphAlgoException object.
      *
      * @param message      Error message
      */
    public GraphTypeException(final String message) {
        super("Error while adding a Graph to the GUI:\n"
              + message + "\n");
    }

    /** (constructor)<br>
      * Creates a GraphTypeException object.
      *
      * @param entry         Reference to the class
      * @param message      Error message
      */
    public GraphTypeException(final GraphEntry entry,
                              final String message) {
        super("Error while adding a Graph to the GUI:\n"
              + "class  -> " + entry + "\n"
              + "reason -> " + message + "\n");
    }

    /** (constructor)<br>
      * Creates a GraphTypeException object.
      *
      * @param entry         Reference to the class
      * @param eMessage     Error message
      */
    public GraphTypeException(final GraphEntry entry,
                              final ArrayList<String> eMessage) {
      StringBuffer sbuf = new StringBuffer(80+eMessage.size()*60);
      sbuf.append("Error(s) occured while adding a");
      sbuf.append(" Graph to the GUI:\n");
      sbuf.append("Graph class -> ").append(entry).append("\n");
        for (int i = 0; i < eMessage.size(); i++) {
            sbuf.append(eMessage.get(i)).append("\n");
        }
        sMessage = sbuf.toString();
    }

    /** (<b>public method</b>)<br>
      * Returns the error message.
      *
      * @return             ErrorMessage
      */
    public String getMessage() {
        if (sMessage == null) {
            return super.getMessage();
        }
        return sMessage;
    }
}
