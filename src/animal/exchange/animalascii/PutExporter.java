/*
 * PutExporter.java
 * Exporter for a put animator
 *
 * Created on 15. Dezember 2005, 14:14
 *
 * @author Michael Schmitt
 * @version 0.2.1
 * @date 2006-01-04
 */

package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.Put;
import animal.graphics.PTText;

public class PutExporter extends TimedAnimatorExporter {

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
    Put put = (Put) animator;

    // 2. write out the new cell content
    pw.print(" entering \"");
    pw.print(PTText.escapeText(put.getContent()));

    // 3. write out the cell number
    pw.print("\" into cell ");
    pw.print(put.getCell());

    // 5. write out the used IDs
    int[] IDs = put.exportIDs();
    pw.print(" using IDs ");
    pw.print(IDs[0]);
    pw.print(" ");
    pw.println(IDs[1]);
  }
}