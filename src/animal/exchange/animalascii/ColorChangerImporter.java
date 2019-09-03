package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.ColorChanger;
import animal.exchange.AnimalASCIIImporter;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * Imports an animal.animator.ColorChanger instance from Animal's ASCII format.
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 0.7 05.09.2007
 */
public class ColorChangerImporter extends TimedAnimatorImporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param version
   *          the version of the ColorChanger to be parsed
   * @param stepNr
   *          the number of the target animation step
   * @param stok
   *          the StreamTokenizer used for parsing the animator
   */
  public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
    XProperties props = new XProperties();
    int currentStep = version;
    try {
      String command = stok.sval;

      // 1. store the command
      props.put(Animator.METHOD_LABEL, command);

      // 2. set the current step
      props.put(Animator.STEP_LABEL, stepNr);

      // 3. parse the objects concerned
      props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok,
          "ColorChanger"));

      // read in the rest, provided we didn't read EOL...
      if (stok.ttype != StreamTokenizer.TT_EOL) {
        // 2. parse super attributes
        parseASCIIWithoutIDs(stok, currentStep, props
            .getProperty(Animator.METHOD_LABEL), props);

      }

      // 2. parse keywords "set to"
      ParseSupport.parseMandatoryWord(stok, AnimalASCIIImporter
          .translateMessage("otKw", "ColorChanger", "set"), "set");

      // 3. parse color with leading "to"
      props.put(ColorChanger.COLOR_LABEL, ParseSupport.parseColor(stok,
          AnimalASCIIImporter.translateMessage("tc", "ColorChanger",
              AnimalASCIIImporter.translateMessage("color")), "to"));
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return new ColorChanger(props);
  }
}
