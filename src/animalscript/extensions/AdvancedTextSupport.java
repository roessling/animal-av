package animalscript.extensions;

import java.awt.Font;
import java.io.IOException;
import java.util.Hashtable;

import animal.animator.SetFont;
import animal.animator.SetText;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

/**
 * This class provides support for AnimalScript commands to change
 * captions or fonts of textObjects. (Commands "setText" and "setFont").
 * 
 * @author <a href="mailto:here@christoph-preisser.de">Christoph Prei&szlig;er</a>
 * @version 1.0 2006-12-21 
 */
public class AdvancedTextSupport extends BasicParser implements AnimalScriptInterface {

	public AdvancedTextSupport(){
		handledKeywords = new Hashtable<String, Object>(); //Keywords and their corresponding parse methods
		handledKeywords.put("settext","parseSetText");
	
		handledKeywords.put("setfont", "parseSetFont");
	}
	
	/**
	 * Parses a command to set a text to a given value.<br>
	 * The commands syntax is
	 * <code>settext &lt;textObject&gt; &lt;value&gt; 
	 * [after &lt;n&gt;] [within &lt;n&gt;]</code>
	 * @throws IOException
	 */
	public void parseSetText() throws IOException {
		//setText [of] <textObject> [to] <value>
	    int duration = 0;
	    int offset = 0;
	    String unit = "ticks";
		
		// first word - should be "settext"
		//String first = 
	    ParseSupport.parseWord(stok, "type").toLowerCase();
		
	    //	 read in OID (object name) with Location of the cell 
	    ParseSupport.parseOptionalWord(stok, "keyword 'of'", "of");
	    String textOID = ParseSupport.parseText(stok, "textObject");
        // parse the objects concerned
//        int[] targetOIDs = ParseSupport.parseOIDNames(stok, getObjectIDs(), anim
//                .getNextGraphicObjectNum());

			// check for method definition, if given
			String methodName = AnimalParseSupport.parseMethod(stok, "setText type",
					"type", "setText");
			
			ParseSupport.parseOptionalWord(stok, "keyword 'to'", "to");
	    String textValue = AnimalParseSupport.parseText(stok, "textValue");
	
	    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
	    	offset = ParseSupport.parseInt(stok,"offset value",0);
	    	unit = ParseSupport.parseWord(stok, "unit");
	    }

	    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
	    	duration = ParseSupport.parseInt(stok,"offset value",0);
	    	unit = ParseSupport.parseWord(stok, "unit");
	    }
	    //Create the animator
        //TODO Check why this does not work properly!
	    SetText setText = new SetText(currentStep, 
	    		getObjectIDs().getIntProperty(textOID),
//            targetOIDs,
	    		duration, offset, unit, methodName, textValue);
	    BasicParser.addAnimatorToAnimation(setText,anim);
	}

	/**
	 * Parses a command to set a text to a given font.<br>
	 * The commands syntax is
	 * <code>setFont &lt;textObject&gt; &lt;fontdefinition&gt; 
	 * [after &lt;n&gt;] [within &lt;n&gt;]</code>
	 * @throws IOException
	 */
	public void parseSetFont() throws IOException {
		//setText <textObject> <value>
	    int duration = 0;
	    int offset = 0;
	    String unit = "ticks";
		
		// first word - should be "settext"
		//String first = 
	    ParseSupport.parseWord(stok, "type").toLowerCase();

	    ParseSupport.parseOptionalWord(stok, "keyword 'of'", "of");

	    //	 read in OID (object name) with Location of the cell 
	    String textOID = ParseSupport.parseText(stok, "textObjectID");
	    ParseSupport.parseOptionalWord(stok, "keyword 'to'", "to");

	    Font textFont = AnimalParseSupport.parseFontInfo(stok, textOID);
	
	    if (ParseSupport.parseOptionalWord(stok, "keyword 'after'", "after")) {
	    	offset = ParseSupport.parseInt(stok,"offset value",0);
	    	unit = ParseSupport.parseWord(stok, "unit");
	    }

	    if (ParseSupport.parseOptionalWord(stok, "keyword 'within'", "within")) {
	    	duration = ParseSupport.parseInt(stok,"offset value",0);
	    	unit = ParseSupport.parseWord(stok, "unit");
	    }
	    //Create the animator
	    SetFont setFont = new SetFont(currentStep, 
	    		getObjectIDs().getIntProperty(textOID),
	    		duration, offset,unit,textFont);
	    BasicParser.addAnimatorToAnimation(setFont,anim);
	}
}
