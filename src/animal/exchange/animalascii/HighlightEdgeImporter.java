package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.HighlightEdge;
import animal.exchange.AnimalASCIIImporter;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/*
 * This class imports HighlightEdge animators in Animal's ASCII format
 *
 * @see animal.animator.HighlightEdge for details
 *
 * @author Pierre Villette, Guido R&ouml;&szlig;ling (roessling@acm.org>
 * @version 1.1 2007-09-05
 */

public class HighlightEdgeImporter extends TimedAnimatorImporter {

  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param version
   *          the version of the HighlightEdge animator to be parsed
   * @param stok
   *          the StreamTokenizer used for parsing the animator
   */
  public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
    XProperties props = new XProperties();
    int currentStep = stepNr;

    // create a new highlightedge animator
    HighlightEdge hl = new HighlightEdge();

    try {
      // 2. set the current step
      props.put(Animator.STEP_LABEL, stepNr);

      // 3. parse the objects concerned
      props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok,
          "HighlightEdge"));

      // read in the rest, provided we didn't read EOL...
      if (stok.ttype != StreamTokenizer.TT_EOL) {
        // 3.1. parse super attributes
        parseASCIIWithoutIDs(stok, currentStep, props
            .getProperty(Animator.METHOD_LABEL), props);
      }
      // 4. parse keywords "modifying cells"
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", "HighlightEdge", "modifying"), "modifying");
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", "HighlightEdge", "cells"), "cells");

      // 5. parse and set the length of the highlight array
      hl = new HighlightEdge(ParseSupport.parseInt(stok, AnimalASCIIImporter
          .translateMessage("tc", "HighlightEdge", AnimalASCIIImporter
              .translateMessage("lenHLArray")), 1));

      // 6. Set the properties of the new Highlight animator
      hl.setProperties(props);

      // 7. parse the highlighted cell indices delimited by 'end'
      int[] cells = ParseSupport.parseObjectIDs(stok, AnimalASCIIImporter
          .translateMessage("tc", "HighlightEdge", AnimalASCIIImporter
              .translateMessage("hlCells")), "end");

      // 8. set highlighting of the cell indices read before
      for (int i = 0; i < (cells.length / 2); i++) {
        hl.setHighlightState(cells[i], cells[i + 1], true);
      }
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return hl;
  }
}
