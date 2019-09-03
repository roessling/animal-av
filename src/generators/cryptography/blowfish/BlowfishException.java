package generators.cryptography.blowfish;

/**
 * Runtime exception thrown by Blowfish algorithm.
 *
 * @author Dani El-Soufi (dani.el-soufi@stud.tu-darmstadt.de)
 * @author Deniz Can Franz Ertan (deniz_can_franz.ertan@stud.tu-darmstadt.de)
 */
public class BlowfishException extends Exception {

    /**
     * Constructor.
     *
     * @param error
     *              Error message to be displayed
     */
    BlowfishException(String error) {
        super(error);
    }

}
