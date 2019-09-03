package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.IndexedSetText;
import animal.animator.SetText;
import animal.exchange.AnimalASCIIImporter;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

public class IndexedSetTextImporter extends SetTextImporter {

  public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
    XProperties props = new XProperties();
    int currentStep = version;
    try {
      // 2. set the current step
      props.put(Animator.STEP_LABEL, stepNr);

      // 3. parse the objects concerned
      props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok,
          "IndexedSetText"));

      if (stok.ttype != StreamTokenizer.TT_EOL) {
        // 2. parse super attributes
        parseASCIIWithoutIDs(stok, currentStep, props
            .getProperty(Animator.METHOD_LABEL), props);
      }
      // 2. parse keyword "to"
      ParseSupport.parseMandatoryWord(stok,
          AnimalASCIIImporter.translateMessage("keyword", "to"), "to");

      // 3. parse string to be used as new caption
      String newCaption = ParseSupport.parseText(stok,
          AnimalASCIIImporter.translateMessage("caption"));
      props.put(SetText.TEXT_PROPERTY_KEY, newCaption);
    } catch (IOException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return new IndexedSetText(props);
  }
}
