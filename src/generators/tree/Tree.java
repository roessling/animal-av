package generators.tree;

import java.util.LinkedList;
import java.util.List;

public class Tree {

	public List<TreeNode> nodes;
	public TreeNode root;
	
	public Tree(TreeNode root) {
		this.root = root;
		nodes = new LinkedList<TreeNode>();
	}
	
	public TreeNode findNearestNode(TreeNode a) {
		double min = Double.MAX_VALUE;
		TreeNode minNode = null;
		
		for(TreeNode b : nodes) {
			
			double distance = Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
			
			if(distance < min) {
				min = distance;
				minNode = b;
			}		
		}
		
		return minNode;
	}
	
	public List<TreeNode> getPath(TreeNode goal){
		List<TreeNode> path = new LinkedList<TreeNode>();
		TreeNode current = goal;
		while(current.parent != null) {
			path.add(current);
			current = current.parent;
		}
		
		path.add(current);
		return path;
	}
	
	public void addNode(TreeNode n) {
		nodes.add(n);
	}
	
	public void delete(TreeNode n) {
		nodes.remove(n);
	}
		
}