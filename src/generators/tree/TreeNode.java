package generators.tree;

import java.util.List;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;

public class TreeNode {

	public TreeNode parent;
	public double x;
	public double y;
	public Circle circle;
	public Polyline edgeToParent;
	
	public TreeNode(double x, double y, TreeNode parent, Circle v) {
		this.x = x;
		this.y = y;
		this.parent = parent;
		circle = v;
	}
	
	public TreeNode(double x, double y, TreeNode parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
	}
	
	public void setCircle(Circle v) {
		circle = v;
	}
	
	public void setEdge(Polyline p) {
		edgeToParent = p;
	}
	
}
