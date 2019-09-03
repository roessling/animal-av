package gfgaa.gui.exceptions;

/** Exception class<br>
  * Class for exceptions occurring while reading one of
  * the graphalgorithms talk table.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public class InvalidTalktableContentException extends Exception {

    /**
   * 
   */
  private static final long serialVersionUID = 6519679989055851159L;

    /** (constructor)<br>
      * Creates a InvalidTalktableContentException object.
      */
    public InvalidTalktableContentException() {
        super();
    }

    /** (constructor)<br>
      * Creates a InvalidTalktableContentException object.
      *
      * @param message      Error message
      */
    public InvalidTalktableContentException(final String message) {
        super(message);
    }
}
