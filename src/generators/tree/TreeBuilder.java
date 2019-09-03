package generators.tree;

import java.util.ArrayList;
import java.util.List;

import algoanim.primitives.Graph;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;

/**
 * 
 * @author Pascal Weisenburger
 * 
 */

public class TreeBuilder {

	private int graphNodeCount;
	private int[][] adjacencyMatrix;
	private Coordinates[] coords;
	private String[] labels;
	private Text[] intervals;
	private Text[] values;
	private int[] chosenPath;

	private Graph tree;

	private static final int GRAPH_HORIZONTAL_DISTANCE = 100;
	private static final int GRAPH_VERTICAL_DISTANCE = 70;

	public static final class TreeNode {
		/** the tree node label */
		public final String label;

		/** the tree node children */
		public final TreeNode[] children;

		/** constructs a new the tree node with the given label */
		public TreeNode(String label) {
			this.label = label;
			this.children = null;
		}

		/**
		 * constructs a new the tree node with the given label and the given
		 * tree node children
		 */
		public TreeNode(String label, TreeNode[] children) {
			this.label = label;
			this.children = children == null || children.length == 0 ? null
					: children;
		}

		/**
		 * retrieves the count of all nodes in the tree whose root is this tree
		 * node
		 */
		public int getNodeCount() {
			int count = 1;
			if (children != null)
				for (TreeNode child : children)
					count += child.getNodeCount();
			return count;
		}

		/** retrieves the depth of the tree whose root is this tree node */
		public int getDepth() {
			if (children != null) {
				int depth = 0;
				for (TreeNode child : children)
					depth = Math.max(depth, child.getDepth());
				return depth + 1;
			}
			return 1;
		}
	}

	/**
	 * 
	 * @param origin
	 * @param horizontalDistance
	 * @param verticalDistance
	 * @param node
	 * @param nodeIndex
	 * @param depth
	 * @param adjacencyMatrix
	 * @param coords
	 * @param labels
	 * @return
	 */
	private static int buildGraph(final Coordinates origin,
			final int horizontalDistance, final int verticalDistance,
			TreeNode node, final int nodeIndex, final int depth,
			int[][] adjacencyMatrix, Coordinates[] coords, String[] labels) {

		int childIndex = nodeIndex + 1;
		if (node.children == null) {
			// leaf node
			Coordinates coord = null;
			for (int i = nodeIndex; i >= 0; i--)
				if (coords[i] != null) {
					// node is placed horizontally next to the previous leaf
					coord = new Coordinates(coords[i].getX()
							+ horizontalDistance, origin.getY() + depth
							* verticalDistance);
					break;
				}
			if (coord == null)
				// node is the first leaf to be placed
				coord = new Coordinates(origin.getX(), origin.getY() + depth
						* verticalDistance);
			coords[nodeIndex] = coord;
		} else {
			// inner node
			int xcoord = 0, xcoordcount = 0;
			for (int i = 0; i < node.children.length; i++) {
				// build sub tree for each child
				int nextChildIndex = buildGraph(origin, horizontalDistance,
						verticalDistance, node.children[i], childIndex,
						depth + 1, adjacencyMatrix, coords, labels);

				// set node edge link to its children in the adjacency matrix
				adjacencyMatrix[nodeIndex][childIndex] = 1;
				adjacencyMatrix[childIndex][nodeIndex] = 1;

				
				
				if (node.children.length == 1) {
					if (Integer.parseInt(node.children[0].label) > Integer
							.parseInt(node.label)) {
					
						xcoord += coords[childIndex].getX() -15;
						xcoordcount++;
					}
					
					if (Integer.parseInt(node.children[0].label) < Integer
							.parseInt(node.label)) {
						xcoord += coords[childIndex].getX() +15;
						xcoordcount++;
					}
				}
				
				// calculate node position centered over the middle child(ren)
				else if (Math.abs(2 * i - node.children.length + 1) <= 1) {
					
				
					xcoord += coords[childIndex].getX();
					xcoordcount++;
				}
			

				childIndex = nextChildIndex;
			}
			coords[nodeIndex] = new Coordinates(xcoord / xcoordcount,
					origin.getY() + depth * verticalDistance);
		}

		labels[nodeIndex] = node.label;
		return childIndex;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public TreeNode parseString(String string) {
		String string2 = string;
		int childrenStart = string2.indexOf('{');
		if (childrenStart == -1) {
			string2 = string2.trim();
			if (string2.indexOf(' ') != -1 || string2.isEmpty())
				return null;
			return new TreeNode(string2);

		}

		char ch;
		int pos = childrenStart + 1;
		boolean childrenComplete = false;
		List<TreeNode> children = new ArrayList<TreeNode>();
		for (int c = 0, i = pos; i < string2.length(); i++)
			switch (ch = string2.charAt(i)) {
			case ' ':
			case '\t':
				break;
			case '{':
				c++;
				break;
			case '}':
				if (--c >= 0)
					break;
				if (i < string2.length() - 1
						&& string2.substring(i).trim().isEmpty())
					return null;
				childrenComplete = true;
			default:
				if (ch != '}') {
					ch = string2.charAt(i - 1);
					if (ch != ' ' && ch != '\t' && ch != '{' && ch != '}')
						break;
				}

				if (c <= 0) {
					String childString = string2.substring(pos, i).trim();
					pos = i;
					if (childString.isEmpty())
						break;

					TreeNode child = parseString(childString);
					if (child == null)
						return null;
					children.add(child);
				}
			}

		if (!childrenComplete)
			return null;

		string2 = string2.substring(0, childrenStart).trim();
		if (string2.isEmpty())
			return null;

		return new TreeNode(string2, children.toArray(new TreeNode[children

		.size()]));
	}

	public TreeBuilder() {

	}

	/**
	 * 
	 * @param lang
	 * @param textualTree
	 * @param treeProperties
	 * @return Graph representing the tree
	 */
	public Graph buildTree(Language lang, String textualTree,
			GraphProperties treeProperties) {

		TreeNode root = parseString(textualTree);

		graphNodeCount = root.getNodeCount();
		adjacencyMatrix = new int[graphNodeCount][graphNodeCount];
		coords = new Coordinates[graphNodeCount];
		labels = new String[graphNodeCount];
		intervals = new Text[graphNodeCount];
		values = new Text[graphNodeCount];

		buildGraph(new Coordinates(60, 100), GRAPH_HORIZONTAL_DISTANCE,
				GRAPH_VERTICAL_DISTANCE, root, 0, 0, adjacencyMatrix, coords,
				labels);

		tree = lang.newGraph("graph", adjacencyMatrix, coords, labels, null,
				treeProperties);

		tree.moveBy(null, 0, 50, null, null);
		tree.hide();
		return tree;

	}

}