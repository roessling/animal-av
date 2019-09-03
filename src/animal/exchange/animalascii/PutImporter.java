/*
 * PutImporter.java
 * Importer for a put animator
 *
 * Created on 15. Dezember 2005, 14:14
 *
 * @author Michael Schmitt
 * @version 0.2.1a
 * @date 2006-02-15
 */

package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.Put;
import animal.animator.TimedAnimator;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

public class PutImporter extends TimedAnimatorImporter {
  /**
   * Import an object in ASCII format from the StreamTokenizer passed in.
   * 
   * @param step
   *          the number of the target animation step
   * @param stok
   *          the StreamTokenizer used for parsing the animator
   * @return the imported object
   */
  public Object importFrom(int version, int step, StreamTokenizer stok) {
    int currentStep = step;
    String val = "";
    int cellIndex = 0;
//    int[] idList = null;
    Put put = null;
    XProperties props = new XProperties();
    try {
      // 3. parse the objects concerned
    	int[] oids = ParseSupport.parseObjectIDs(stok, "Put");

      // read in the rest, provided we didn't read EOL...
      if (stok.ttype != StreamTokenizer.TT_EOL) {
        // 3.1. parse super attributes
        parseASCIIWithoutIDs(stok, currentStep, props
            .getProperty(Animator.METHOD_LABEL), props);
      }
//      put = new Put(props);
//      put.setStep(step);
//      put.setObjectNums(oids);

      // 4. parse the new cell content
      val = ParseSupport.parseText(stok, "New cell content", "entering");

      // 5. parse mandatory words "into cell"
      ParseSupport.parseMandatoryWord(stok, "Keyword 'into'", "into");
      ParseSupport.parseMandatoryWord(stok, "Keyword 'cell'", "cell");

      // 6. parse index of the array cell
      cellIndex = ParseSupport.parseInt(stok, "Modified array cell", 0);

      // 7. parse mandatory words "using IDs"
      ParseSupport.parseMandatoryWord(stok, "Keyword 'using'", "using");
      ParseSupport.parseMandatoryWord(stok, "Keyword 'IDs'", "IDs");

      // 8. read in the IDs of the animated objects so that they are
      // reassigned and will not be overwritten by another animator
//      int[] idList = 
      ParseSupport.parseObjectIDsTillEOL(stok, "Used Object IDs");

      // !!! Changing the position of this command will lead to serious
      // problems and exceptions!!!
      //TODO CHECK THIS OUT
//      put.restoreIDs(idList);
      int oid = oids[0];
      put = new Put(step, oid, 
      		props.getIntProperty(TimedAnimator.DURATION_LABEL, 0), 
      		props.getIntProperty(TimedAnimator.OFFSET_LABEL, 0), val, cellIndex);
      put.setContent(val);
      // also calls init()
      put.updateAnimation(oid, oid, -1, cellIndex);
//      put.setCell(cellIndex);
//      put.setContent(val);
//      put.setArray(put.getObjectNums()[0]);
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return put;
  }
}