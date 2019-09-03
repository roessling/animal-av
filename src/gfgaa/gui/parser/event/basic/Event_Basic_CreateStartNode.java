package gfgaa.gui.parser.event.basic;


import gfgaa.gui.graphs.basic.Node;
import gfgaa.gui.parser.ParserActionInterface;
import gfgaa.gui.parser.ParserUnit;

import java.io.StreamTokenizer;

//Madieha
public class Event_Basic_CreateStartNode implements ParserActionInterface {
@Override
     public boolean execute(StreamTokenizer tok, ParserUnit data) {
	
	 if (data.done != data.give) {
            data.done++;}
	 
	//data.graph.addNode(new Node(data.cVal[0]));
	
	 data.graph.setStartNode(new Node(data.cVal[0]));
	return true;
 }
}