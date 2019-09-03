package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.Move;

/**
 * This class exports move effects in ASCII format
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0, 29.07.2004
 */
public class MoveExporter extends TimedAnimatorExporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param pw
   *          the PrintWriter to write to
   * @param animator
   *          the current Move object
   */
  public void exportTo(PrintWriter pw, Animator animator) {
    // 1. write out the info contained in the ancestor
    // note: this ends without a space
    super.exportTo(pw, animator);

    Move move = (Move) animator;

    // 2. append the space to the output
    pw.print(" via ");

    // 3. write out the object number of the move path and append a '\n'
    pw.println(move.getMoveBaseNum());
  }
}
