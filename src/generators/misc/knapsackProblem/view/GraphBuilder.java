package generators.misc.knapsackProblem.view;

import java.util.ArrayList;

import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class GraphBuilder {

	/**
	 * builds an animal.graph from a node tree
	 * 
	 * @param root root node of a tree
	 */
	public static Graph buildGraph(Language lang, SimpleNode root, GraphProperties graphProps) {
		int depth = GraphBuilder.recDepth(root);
		int size = GraphBuilder.recCountNodes(root, new Accumulator(0)).getValue();
		
		System.out.println("built graph with depth " + depth);
		System.out.println("node count is " + size);
		
		if (size == 0) return null;
		
		//build adjacency matrix
		int[][] adjacency = new int[size][size];
		for (int i = 0; i < adjacency.length; i++) {
			for (int j = 0; j < adjacency[i].length; j++) {
				adjacency[i][j] = 0;
			}
		}
		adjacency = GraphBuilder.buildAdjacency(root, adjacency, new Accumulator(0));
		
		GraphBuilder.printMatrix(adjacency);
		
		//calculate coordinates for each node from anchor point
		int nodeSize = 4;
		int hMargin = 4;
		int vMargin = 50;
		int graphXOffset = 800;
		//anchor for root node
		Coordinates anchor = new Coordinates(graphXOffset + (nodeSize + hMargin * (int)Math.pow(2, depth)) * 2, 10);
		// build layout and manually add root=anchor at the start
		ArrayList<Node> starter = new ArrayList<Node>();
		starter.add(anchor);
		ArrayList<Node> tmp = GraphBuilder.recBuildLayout(root, starter, anchor, nodeSize, hMargin, vMargin, depth);
		Node[] layout = new Node[tmp.size()];
		layout = tmp.toArray(layout);

		String[] labels = new String[size];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = "";
		}
		
		System.out.println("Layout node count is " + layout.length);
		System.out.println("Label count is " + labels.length);
		
		Graph g = lang.newGraph("graph", adjacency, layout, labels, null, graphProps);
		return g;
	}
	
	public static int recDepth(SimpleNode node) {
		if (node != null) {
			return Math.max(1 + GraphBuilder.recDepth(node.getLeft()), 1 + GraphBuilder.recDepth(node.getRight()));
		}
		return 0;
	}
	
	public static Accumulator recCountNodes(SimpleNode node, Accumulator acc) {
		acc.inc();
		if (node.getLeft() != null) {
			GraphBuilder.recCountNodes(node.getLeft(), acc);
		}
		
		if (node.getRight() != null) {
			GraphBuilder.recCountNodes(node.getRight(), acc);
		}
		return acc;
	}
	
	public static Accumulator recCountLeafs(SimpleNode node, Accumulator acc) {
		if (node.getLeft() != null) {
			GraphBuilder.recCountLeafs(node.getLeft(), acc);
		}
		
		if (node.getRight() != null) {
			GraphBuilder.recCountLeafs(node.getRight(), acc);
		}
		
		if (node.getLeft() == null && node.getRight() == null) {
			acc.inc();
		}
		return acc;
	}
	
	public static int[][] buildAdjacency(SimpleNode node, int[][] adjacency, Accumulator offset) {
		int current = offset.getValue();
		node.id = current;
		if (node.getLeft() != null) {
			adjacency[current][offset.getValue()+1] = 1;
			adjacency[offset.getValue()+1][current] = 1;
			
			offset.inc();
			buildAdjacency(node.getLeft(), adjacency, offset);
		}
		
		if (node.getRight() != null) {
			adjacency[current][offset.getValue()+1] = 1;
			adjacency[offset.getValue()+1][current] = 1;
			
			offset.inc();
			buildAdjacency(node.getRight(), adjacency, offset);
		}
		
		return adjacency;
	}
	
	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j]);
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * builds a layout out of a binary tree (root node has to be pre-positioned for the layout)
	 * 
	 * @param node current node of the tree
	 * @param layout node accumulator
	 * @param anchor anchor coordinates of the root
	 * @param nodeSize size of a single node
	 * @param hMargin horizontal margin between nodes
	 * @param vMargin vertical margin between nodes
	 * @param depth depth of the tree
	 * @return calculated node positions for the given tree
	 */
	public static ArrayList<Node> recBuildLayout(SimpleNode node, ArrayList<Node> layout, Coordinates anchor, int nodeSize, int hMargin, int vMargin, int depth) {
		if (node.getLeft() != null) {
			Node left = new Coordinates(anchor.getX() - (nodeSize/2 + hMargin * (int)Math.pow(2, depth)), anchor.getY() + nodeSize + vMargin);
			layout.add(left);
			GraphBuilder.recBuildLayout(node.getLeft(), layout, (Coordinates) left, nodeSize, hMargin, vMargin, depth - 1);
		}
		
		if (node.getRight() != null) {
			Node right = new Coordinates(anchor.getX() + (nodeSize/2 + hMargin * (int)Math.pow(2, depth)), anchor.getY() + nodeSize + vMargin);
			layout.add(right);
			GraphBuilder.recBuildLayout(node.getRight(), layout, (Coordinates) right, nodeSize, hMargin, vMargin, depth - 1);
		}
		return layout;
	}
}
