package generators.helpers;

import algoanim.util.Node;

public class NodeElem {

	
	private int NodeId;
	private String nodename;
	private Node nodepos;
	
	public NodeElem(int nodeid, Node nodepos) {
		this.NodeId = nodeid;
		this.nodename = Integer.toString(nodeid);
		this.nodepos = nodepos;
	}
	
	public int getnodeid() { 
		return NodeId;
	}
	
	public String getnodename() {
		return nodename;
	}
	
	public Node getnodepos() {
		return nodepos;
	}
}
