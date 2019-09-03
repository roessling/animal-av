package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.SetText;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * This class imports setText effects from ASCII format
 * based on MoveImporter by Guido R&ouml;&szlig;ling
 * @author Christoph Prei&szlig;er
 * @version 0.95, 24.11.2006
 */
public class SetTextImporter extends TimedAnimatorImporter {
	  public Object importFrom(int version, int stepNr, StreamTokenizer stok)
	  {
	    XProperties props = new XProperties();
	    int currentStep = version;
	    try {
	      // 2. set the current step
	      props.put(Animator.STEP_LABEL, stepNr);

	      // 3. parse the objects concerned
	      props.put(Animator.OID_LABEL,
	                ParseSupport.parseObjectIDs(stok, "SetText"));
	      // read in the rest, provided we didn't read EOL...
	      if (stok.ttype != StreamTokenizer.TT_EOL)
	      {
	        // 2. parse super attributes
	        parseASCIIWithoutIDs(stok, currentStep, 
	                             props.getProperty(Animator.METHOD_LABEL),
	                             props);        
	      }
	      // 2. parse keyword "to"
	      ParseSupport.parseMandatoryWord(stok, "Keyword 'to'", "to");

	      // 3. parse string to be used as new caption
	      String newCaption = ParseSupport.parseText(stok, "caption to set");
	      props.put(SetText.TEXT_PROPERTY_KEY, newCaption);
	    }
	    catch(IOException e)
	    {
	    	MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
	    }
	    return new SetText(props);
	  }
}
