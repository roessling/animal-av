package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.Highlight;
import animal.exchange.AnimalASCIIImporter;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/*
 * This class imports a Highlight animator from Animal's ASCII format
 *
 * Created on 30. December 2005, 21:56
 *
 * @see animal.animator.Highlight for details
 *
 * @author Michael Schmitt, Guido R&ouml;&szlig;ling (roessling@acm.org>)
 * @version 1.1
 * @date 2007-09-05
 */

public class HighlightImporter extends TimedAnimatorImporter {

  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param version
   *          the version of the Highlight animator to be parsed
   * @param step the step in which this animator takes place         
   * @param stok
   *          the StreamTokenizer used for parsing the animator
   */
  public Object importFrom(int version, int step, StreamTokenizer stok) {
    XProperties props = new XProperties();
    int currentStep = version;

    // create a new highlight animator
    Highlight hl = new Highlight();

    try {
      // 2. set the current step
      props.put(Animator.STEP_LABEL, step);

      // 3. parse the objects concerned
      props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok,
          "Highlight"));

      // read in the rest, provided we didn't read EOL...
      if (stok.ttype != StreamTokenizer.TT_EOL) {
        // 3.1. parse super attributes
        parseASCIIWithoutIDs(stok, currentStep, props
            .getProperty(Animator.METHOD_LABEL), props);
      }
      // 4. parse keywords "modifying cells"
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", "Highlight", "modifying"), "modifying");
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", "Highlight", "cells"), "cells");

      // 5. parse and set the length of the highlight array
      hl = new Highlight(ParseSupport.parseInt(stok, AnimalASCIIImporter
          .translateMessage("tc", "Highlight", AnimalASCIIImporter
              .translateMessage("lenHLArray")), 1));

      // 6. Set the properties of the new Highlight animator
      hl.setProperties(props);

      // 7. parse the highlighted cell indices delimited by 'end'
      int[] cells = ParseSupport.parseObjectIDs(stok, AnimalASCIIImporter
          .translateMessage("tc", "Highlight", AnimalASCIIImporter
              .translateMessage("hlCells")), "end");

      // 8. set highlighting of the cell indices read before
      for (int i = 0; i < cells.length; i++) {
        hl.setHighlightState(cells[i], true);
      }
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return hl;
  }
}
