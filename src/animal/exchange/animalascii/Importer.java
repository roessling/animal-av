package animal.exchange.animalascii;

import java.io.StreamTokenizer;

/**
 * The base interface for any type of importer - must be able to import content
 * from a StreamTokenizer, using the given file version
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 1.0, 29.07.2004
 */
public interface Importer {
  /**
   * Import an object in ASCII format from the StreamTokenizer passed in.
   * 
   * @param version
   *          the version of the exported object
   * @param stok
   *          the StreamTokenizer from which to parse the object
   * @return the imported object
   */
  public Object importFrom(int version, StreamTokenizer stok);
}
