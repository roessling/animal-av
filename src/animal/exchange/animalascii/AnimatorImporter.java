package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.exchange.AnimalASCIIImporter;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;

/**
 * This class provides the common interface for importing animators.
 * 
 * @author Guido Roessling <roessling@acm.org>
 * @version 0.7 05.09.2007
 */
public class AnimatorImporter implements Importer {
  /**
   * Export this object in ASCII format to the PrintWriter passed in. This is
   * mapped to <em>importFrom(1, stepNr, stok)</em>.
   * 
   * @param stepNr
   *          the number of the target animation step
   * @param stok
   *          the StreamTokenizer used for parsing the animator
   * @return the resulting imported animator or null.
   * @see #importFrom(int, int, StreamTokenizer)
   */
  public Object importFrom(int stepNr, StreamTokenizer stok) {
    return importFrom(1, stepNr, stok);
  }

  /**
   * Imports an animator from ASCII format using the StreamTokenizer passed in.
   * Note that the method in this class will never be invoked, it must be
   * overridden by subclasses.
   * 
   * @param version
   *          the version number of the animator
   * @param stepNr
   *          the number of the target animation step
   * @param stok
   *          the StreamTokenizer used for parsing the animator
   * @return null for all invocations, after parsing the whole current line of
   *         input
   */
  public Object importFrom(int version, 
  int stepNr, StreamTokenizer stok) {
    MessageDisplay.errorMsg(AnimalASCIIImporter.translateMessage(
        "invalidImportCall", new String[] { String.valueOf(stok.lineno()),
            String.valueOf(version), getClass().getName() }),
        MessageDisplay.RUN_ERROR);
    try {
      MessageDisplay.errorMsg(AnimalASCIIImporter.translateMessage(
          "readErrorLine", new String[] { ParseSupport.consumeIncludingEOL(
              stok, "object") }), MessageDisplay.INFO);
    } catch (IOException e) {
      MessageDisplay.errorMsg(AnimalASCIIImporter.translateMessage(
          "ioErrorImporting", new String[] { e.getMessage() }),
          MessageDisplay.RUN_ERROR);
    }
    return null;
  }
}
