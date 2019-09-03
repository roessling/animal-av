package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.Highlight;

/**
 * This class exports Highlight animators in Animal's ASCII format.
 * 
 * @author Michael Schmitt
 * @version 1.1 2007-09-05
 * @see animal.animator.Highlight for details about the animator
 */
public class HighlightExporter extends TimedAnimatorExporter {

  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   *
   * @param pw
   *          the PrintWriter to write to
   * @param animator
   *          the current Animator object
   */
  public void exportTo(PrintWriter pw, Animator animator) {
    // 1. write out the info contained in the ancestor
    // note: this ends without a space
    super.exportTo(pw, animator);
    Highlight hl = (Highlight) animator;

    // 2. append space
    // and write out the animator description
    pw.print(" modifying cells ");

    // 3. write out the length of the highlight array
    boolean[] cells = hl.getHighlightState();
    pw.print(cells.length);
    pw.print(" ");

    // 4. write out the indices of the highlighted cells or entries
    for (int i = cells.length - 1; i >= 0; i--) {
      if (cells[i]) {
        pw.print(i);
        pw.print(" ");
      }
    }

    // 5. write out 'end' delimiter and append a final newline
    pw.println("end");
  }
}
