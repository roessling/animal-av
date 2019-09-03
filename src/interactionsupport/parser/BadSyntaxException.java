package interactionsupport.parser;

/**
 * BadSyntaxException is an Exception thrown by the Parser to indicate syntax
 * errors in a parsed file.
 *
 * @author Gina Haeussge
 */
public class BadSyntaxException extends Exception {

  private static final long serialVersionUID = 445368268562478282L;

  /** Text containing a description of the error that occured */
  private String errortext = "";

  /**
   * Constructor
   *
   * @param e A String containing a description of the error
   */
  public BadSyntaxException(String e) {
    super(e);
    errortext = e;
  }

  /**
   * Output the errortext
   *
   * @return DOCUMENT ME!
   */
  public String toString() {
    return errortext;
  }

}
