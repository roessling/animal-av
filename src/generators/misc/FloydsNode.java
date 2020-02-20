package generators.misc;

import algoanim.util.Coordinates;
import algoanim.util.Node;

public class FloydsNode {
	private Node MainNode;
	private Node UpperTag;
	private Node LowerTag;
	private String Label;
	
	private static final int upperXoffset = -18;
	private static final int upperYoffset = -30;
	private static final int lowerXoffset = -15;
	private static final int lowerYoffset = +27;
	
	
	public FloydsNode(int xPos, int yPos, int index) {
		MainNode = new Coordinates(xPos, yPos);
		UpperTag = new Coordinates(xPos + upperXoffset, yPos + upperYoffset);
		LowerTag = new Coordinates(xPos + lowerXoffset, yPos + lowerYoffset);
		
		Label = String.valueOf((char) (index + 65));
	}
	
	public String getLabel() {
		return Label;
	}
	
	public Node getMainNode() {
		return MainNode;
	}
	
	public Node getUpperTag() {
		return UpperTag;
	}
	
	public Node getLowerTag() {
		return LowerTag;
	}
}
