package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.HighlightEdge;

/*
 * This class exports HighlightEdge animators in Animal's ASCII format
 *
 * @see animal.animator.HighlightEdge for details
 *
 * @author Pierre Villette
 * @version 1.0 2006-09-27
 */
public class HighlightEdgeExporter extends TimedAnimatorExporter {

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
    HighlightEdge hl = (HighlightEdge) animator;

    // 2. append space
    // and write out the animator description
    pw.print(" modifying cells ");

    // 3. write out the length of the highlight array
    boolean[][] cells = hl.getHighlightState();
    pw.print(cells.length);
    pw.print(" ");

    // 4. write out the indices of the highlighted cells or entries
    for (int i = cells.length - 1; i >= 0; i--) {
      for (int j = cells.length - 1; j >= 0; j--) {
        if (cells[i][j]) {
          pw.print(i);
          pw.print(" ");
          pw.print(j);
          pw.print(" ");
        }
      }
    }

    // 5. write out 'end' delimiter and append a final newline
    pw.println("end");
  }
}
