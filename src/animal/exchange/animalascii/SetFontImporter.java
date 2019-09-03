package animal.exchange.animalascii;

import java.awt.Font;
import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.SetFont;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * This class imports setFont effects from ASCII format
 * based on MoveImporter by Guido R&ouml;&szlig;ling
 * @author Christoph Prei&szlig;er
 * @version 0.95, 24.11.2006
 */

public class SetFontImporter extends TimedAnimatorImporter {
	  public Object importFrom(int version, int stepNr, StreamTokenizer stok)
	  {
	    XProperties props = new XProperties();
	    int currentStep = version;
	    try {
	      // 2. set the current step
	      props.put(Animator.STEP_LABEL, stepNr);

	      // 3. parse the objects concerned
	      props.put(Animator.OID_LABEL,
	                ParseSupport.parseObjectIDs(stok, "SetFont"));
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

	      // 3. parse font attributes family, name, style and size
	      ParseSupport.parseWord(stok, "Fontfamily"); //not used
	      Font newFont = new Font(
                                  ParseSupport.parseWord(stok, "Fontname"),
                                  ParseSupport.parseInt(stok, "Fontstyle"),
	    		                  ParseSupport.parseInt(stok, "Fontsize"));
	      
	      props.put(SetFont.FONT_PROPERTY_KEY, newFont);
	    }
	    catch(IOException e)
	    {
	    	MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
	    }
	    return new SetFont(props);
	  }

}
