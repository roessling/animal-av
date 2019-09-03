package interactionsupport;

/**
 * Exception which is thrown when an unknown parser type is referenced or the
 * corresponding class cannot be instantiated.
 * 
 * @author Gina Haeussge, huge(at)rbg.informatik.tu-darmstadt.de, Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class UnknownParserException extends Exception {

  /** The serial version UID for this class */
  private static final long serialVersionUID = -1859949236413483926L;

  /**
   * Standard Constructor.
   * 
   * @param e
   *          The exceptions text.
   */
  public UnknownParserException(String e) {
    super(e);
  }

}
