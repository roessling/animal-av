package generators.misc;

import algoanim.util.Node;

import java.util.ArrayList;


public class FloydsPath {
	private ArrayList<FloydsNode> FloydNodeList;
	private int IndexCounter;
		
	public FloydsPath(int circleLength, int nonCircleLength) {
		FloydNodeList = new ArrayList<FloydsNode>();
		IndexCounter = 0;
		fillPathNodeList(circleLength, nonCircleLength);
	}
	
	private void addFloydNode(int xPos, int yPos) {
		FloydNodeList.add(new FloydsNode(xPos, yPos, IndexCounter));
		IndexCounter++;
	}
	
	public String[] getLabelArray() {
		ArrayList<String> labelList = new ArrayList<String>();
		for (FloydsNode fn : FloydNodeList) {
			labelList.add(fn.getLabel());
		}
		return labelList.toArray(new String[labelList.size()]);
	}
	
	public Node[] getNodeArray() {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		for (FloydsNode fn : FloydNodeList) {
			nodeList.add(fn.getMainNode());
		}
		return nodeList.toArray(new Node[nodeList.size()]);
	}
	
	public Node getUpperTagFromIndex(int index) {
		return FloydNodeList.get(index).getUpperTag();
	}
	
	public Node getLowerTagFromIndex(int index) {
		return FloydNodeList.get(index).getLowerTag();
	}
	
    private void fillPathNodeList(int cl, int ncl) {
		int halfCircleLength = (int) (cl - 1) / 2;

		// Non-circle nodes and first circle node
		int xPos = 600;
		int yPos = 250;
		for (int i = 0; i < ncl + 1; i++) {
			this.addFloydNode(xPos, yPos);
			xPos += 100;
		}

		// Upper-circle nodes
		yPos -= 50;
		for (int i = 0; i < halfCircleLength; i++) {
			this.addFloydNode(xPos, yPos);
			xPos += 100;
		}

		// Single node at the end  (only for odd circles)
		yPos += 50;
		if (cl % 2 == 0) {
			this.addFloydNode(xPos, yPos);
		}

		// Lower-circle nodes
		yPos += 50;
		xPos -= 100;
		for (int i = 0; i < halfCircleLength; i++) {
			this.addFloydNode(xPos, yPos);
			xPos -= 100;
		}
	}
}
