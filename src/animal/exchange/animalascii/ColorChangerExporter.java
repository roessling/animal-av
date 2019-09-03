package animal.exchange.animalascii;

import java.awt.Color;
import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.ColorChanger;

/**
 * Exports an animal.animator.ColorChanger in Animal's ASCII format.
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.1 05.09.2007
 */
public class ColorChangerExporter extends TimedAnimatorExporter {
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
    ColorChanger colorChanger = (ColorChanger) animator;

    // 2. append the space to the output
    pw.print(" set to (");

    // 3. write out the color object used as a RGB triplet
    Color color = colorChanger.getColor();
    pw.print(color.getRed());
    pw.print(',');
    pw.print(color.getGreen());
    pw.print(',');
    pw.print(color.getBlue());
    pw.println(")");
  }
}
