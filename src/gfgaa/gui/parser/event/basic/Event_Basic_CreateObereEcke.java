package gfgaa.gui.parser.event.basic;

import gfgaa.gui.graphs.basic.Uppercorner;
import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

public class Event_Basic_CreateObereEcke implements ParserActionInterface {

	@Override
	public boolean execute(StreamTokenizer tok, ParserUnit data) {
		 if (data.done != data.give) {
	            data.done++;}
		 data.graph.setCorner(new Uppercorner( data.nVal[0],
                 data.nVal[1]));
		return true;
	}

}
