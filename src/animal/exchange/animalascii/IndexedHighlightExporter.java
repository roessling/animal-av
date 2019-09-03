package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;

/**
 * Exports an IndexedHighlight animator. Since IndexedHighlight uses no special
 * attribute nothing has to be overwritten
 */
public class IndexedHighlightExporter extends TimedAnimatorExporter {

  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param pw
   *          the PrintWriter to write to
   */
  public void exportTo(PrintWriter pw, Animator animator) {
    super.exportTo(pw, animator);
    pw.println("");
  }
}
