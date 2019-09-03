package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.main.Link;

/**
 * This class exports Link objects in Animal's ASCII format
 * 
 * @see animal.main.Link
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0, 29.07.2004
 */
public class LinkExporter implements Exporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param pw
   *          the PrintWriter to write to
   * @param link
   *          the current Link object
   */
  public void exportTo(PrintWriter pw, Link link) {
    // 1. Write out the file version
    pw.print(Link.getFileVersion());
    pw.print(" Link ");

    int step = link.getStep();
    int nextStep = link.getNextStep();
    int mode = link.getMode();

    // 2. write out the step number
    if (step != Link.END) {
      pw.print(step);

      // 3. write out the number of the next step
      pw.print(" next step ");
      pw.print(nextStep);
      pw.print(" after ");

      // 4. write out the mode (WAIT_KEY, WAIT_TIME)
      switch (mode) {
      case Link.WAIT_TIME:
        pw.print("time ");
        pw.print(link.getTime());
        pw.print(" ms"); // in the future, might also be " ticks"

        break;

      case Link.WAIT_CLICK:
        pw.print("click on ID ");
        pw.print(link.getTargetObjectID());

        break;

      default:
        pw.print("key press");
      }

      if ((link.getLinkLabel() != null) && (link.getLinkLabel().length() > 0)) {
        pw.print(" label \"");
        pw.print(link.getLinkLabel());
        pw.print("\"");
      }
    } else {
      pw.print("END");
    }

    // end the current line
    pw.println();
  }
}
