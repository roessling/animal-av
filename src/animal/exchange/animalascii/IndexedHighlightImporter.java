package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.IndexedHighlight;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * Imports an IndexedHighlight animator.
 */
public class IndexedHighlightImporter extends TimedAnimatorImporter {

  public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
    XProperties props = new XProperties();
    int currentStep = version;

    // create a new indexed highlight animator
    IndexedHighlight ih = new IndexedHighlight();

    try {
      // 2. set the current step
      props.put(Animator.STEP_LABEL, stepNr);

      // 3. parse the objects concerned
      props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok,
          "IndexedHighlight"));

      if (stok.ttype != StreamTokenizer.TT_EOL) {
        // 3.1. parse super attributes
        parseASCIIWithoutIDs(stok, currentStep, props
            .getProperty(Animator.METHOD_LABEL), props);
      }
      // set properties
      ih.setProperties(props);

    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return ih;
  }
}
