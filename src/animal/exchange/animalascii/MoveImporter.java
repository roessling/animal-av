package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.Move;
import animal.exchange.AnimalASCIIImporter;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

public class MoveImporter extends TimedAnimatorImporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param version
   *          the version of the Move animator to be parsed
   * @param stepNr
   *          the number of the target animation step
   * @param stok
   *          the StreamTokenizer used for parsing the animator
   */
  public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
    XProperties props = new XProperties();
    int currentStep = version;
    try {
      // 2. set the current step
      props.put(Animator.STEP_LABEL, stepNr);

      // 3. parse the objects concerned
      props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok, "Move"));
      // read in the rest, provided we didn't read EOL...
      if (stok.ttype != StreamTokenizer.TT_EOL) {
        // 2. parse super attributes
        parseASCIIWithoutIDs(stok, currentStep, props
            .getProperty(Animator.METHOD_LABEL), props);
      }
      // 2. parse keyword "via"
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", new String[] { "Move", "via" }), "via");

      // 3. parse object ID to be used as a path
      props.put(Move.MOVE_BASE_LABEL, ParseSupport.parseInt(stok,
          AnimalASCIIImporter.translateMessage("moveBaseObjectID")));
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return new Move(props);
  }
}
