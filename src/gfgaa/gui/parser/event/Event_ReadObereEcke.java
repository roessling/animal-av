package gfgaa.gui.parser.event;

import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

public class Event_ReadObereEcke implements ParserActionInterface{

	@Override
	public boolean execute(StreamTokenizer tok, ParserUnit data) {
		data.state = ParserUnit.STATE_LOADING_OBEREECKE;
		
		data.nVal[0] = 0;
	    data.nVal[1] = 0;
	    
	    
	    data.cVal[0]=0;
	    data.cVal[1]=0;
	    data.give++;
        return true;
	    
		
	}
	

}
