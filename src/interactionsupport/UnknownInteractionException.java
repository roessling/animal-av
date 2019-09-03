package interactionsupport;

/**
 * Exception which is thrown when a non existing interaction identifier is
 * referenced.
 * 
 * @author Gina Haeussge, huge(at)rbg.informatik.tu-darmstadt.de, Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class UnknownInteractionException extends Exception {

  /**
   * This class' serial version UID
   */
  private static final long serialVersionUID = -8501998034796063203L;

  /**
   * Standard Constructor.
   * 
   * @param e
   *          The exceptions text.
   */
  public UnknownInteractionException(String e) {
    super(e);
  }

}
