package generators.tree.id3.tree;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.graphics.helpers.Coordinate;

public class Tree {

	private Node root;

	private List<Node> nodes = new LinkedList<Node>();

	private List<Edge> edges = new LinkedList<Edge>();



	public void addRootNode(Node node) {
		nodes.add(node);
		root = node;
	}


	public Node getRootNode() {
		return root;
	}


	public void addSubTrees(Node featureNode, LinkedList<Tree> subTrees, LinkedList<String> values) {

		nodes.add(featureNode);

		root = featureNode;

		for (int i = 0; i < subTrees.size(); i++) {

			Tree subTree = subTrees.get(i);
			String value = values.get(i);

			nodes.addAll(subTree.getNodes());

			edges.addAll(subTree.getEdges());
			edges.add(new Edge(featureNode, subTree.getRootNode(), value));
		}
	}


	public List<Node> getNodes() {
		return nodes;
	}


	public List<Edge> getEdges() {
		return edges;
	}


	private boolean edgeContained(Node n1, Node n2) {

		for (Edge e : edges) {
			if (e.getFrom() == n1 && e.getTo() == n2) {
				return true;
			}
		}
		return false;

	}


	private boolean isLeaf(Node node) {
		return children(node).isEmpty();
	}


	private LinkedList<Node> children(Node node) {

		LinkedList<Node> children = new LinkedList<Node>();

		for (Edge e : edges) {
			if (e.getFrom() == node) {
				children.add(e.getTo());
			}
		}
		return children;
	}


	public int depthOf(Node node) {

		Node current = node;
		int height = 1;

		while (current != root) {
			for (Edge e : edges) {
				if (e.getTo() == current) {
					current = e.getFrom();
					height++;
					break;
				}
			}
		}
		return height;
	}


	public int widthOfSubtreeFrom(Node node) {

		if (isLeaf(node)) {
			return 1;
		}

		List<Node> children = children(node);

		int maxChildren = 0;
		for (Node child : children) {
			maxChildren = maxChildren + widthOfSubtreeFrom(child);
		}

		return Math.max(maxChildren, children.size());

	}


	private int getParentID(Node node) {

		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			if (edge.getTo() == node) {
				return nodes.indexOf(edge.getFrom());
			}
		}
		return -1;
	}


	private LinkedList<Node> getChildren(Node node) {

		LinkedList<Node> children = new LinkedList<Node>();

		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			if (edge.getFrom() == node) {
				children.add(edge.getTo());
			}
		}
		return children;
	}


	private int[] fromIndexToIndex(Edge edge) {

		int[] fromTo = new int[2];

		fromTo[0] = nodes.indexOf(edge.getFrom());
		fromTo[1] = nodes.indexOf(edge.getTo());

		return fromTo;
	}


	public LinkedList<Integer> getEdgeIndicesForParentNodeID(int id) {

		LinkedList<Integer> indices = new LinkedList<Integer>();

		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			if (nodes.indexOf(edge.getFrom()) == id) {
				indices.add(i);
			}
		}

		return indices;
	}


	public Graph constructAnimalScriptTree(Language lang, GraphProperties properties, LinkedList<Text> edgeLabels, LinkedList<Group> markers,
			LinkedList<Text> nodeTitles, String newNode, String newLeaf, Color nodeColor, Color yesColor, Color noColor) {

		int numberOfNodes = nodes.size();

		int[][] adjacencyMatrix = new int[numberOfNodes][numberOfNodes];
		for (int i = 0; i < numberOfNodes; i++) {
			for (int j = 0; j < numberOfNodes; j++) {
				adjacencyMatrix[i][j] = edgeContained(nodes.get(i), nodes.get(j)) ? 1 : 0;
			}
		}

		algoanim.util.Node[] treeNodes = new algoanim.util.Node[numberOfNodes];

		HashMap<Integer, Integer> heightCount = new HashMap<Integer, Integer>();

		LinkedList<Coordinate> coords = new LinkedList<Coordinate>();

		int maxLabelSize = 0;
		for (int i = 0; i < numberOfNodes; i++) {
			String label = nodes.get(i).toString();
			if (label.length() > maxLabelSize) {
				maxLabelSize = label.length();
			}

		}

		maxLabelSize = Math.min(maxLabelSize, 10);


		for (int i = 0; i < numberOfNodes; i++) {

			Node node = nodes.get(i);

			int nodeHeight = depthOf(node);
			int reservedSpace = widthOfSubtreeFrom(node) * 140;
			if (heightCount.containsKey(nodeHeight)) {
				heightCount.put(nodeHeight, heightCount.get(nodeHeight) + reservedSpace);
			} else {
				heightCount.put(nodeHeight, reservedSpace);
			}

			int parentID = getParentID(node);
			int posX = 0;

			if (parentID != -1) {
				int parentX = coords.get(parentID).getX();
				// getChildren(nodes.get(parentID)).indexOf(node) -
				// getChildren(nodes.get(parentID)).size() / 2
				// posX = parentX + (int) (- 1 + (2.0 /
				// getChildren(nodes.get(parentID)).size()) *
				// getChildren(nodes.get(parentID)).indexOf(node)) * 130;
				// posX = parentX + ( ) * 130; //parentX -
				// ((getChildren(nodes.get(parentID)).size() - 1) * 30) / 2 + 30
				// * getChildren(nodes.get(parentID)).indexOf(node);
				posX = parentX - ((getChildren(nodes.get(parentID)).size() - 1) * (335 - 70 * nodeHeight)) / 2
						+ (335 - 70 * nodeHeight) * getChildren(nodes.get(parentID)).indexOf(node);
				// posX = parentX - ((getChildren(nodes.get(parentID)).size() -
				// 1) * 140) / 2 + 140 *
				// getChildren(nodes.get(parentID)).indexOf(node);
			} else {
				posX = 935; // 980; 70 + heightCount.get(nodeHeight) -
							// reservedSpace
							// / 2;
			}

			int posY = 70 + (depthOf(node) - 1) * 150;

			Coordinate newC = new Coordinate(posX, posY);
			coords.add(newC);
			treeNodes[i] = new Coordinates(posX, posY);

			// markers
			TriangleProperties newNodeMarkerProperties = new TriangleProperties();
			newNodeMarkerProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			newNodeMarkerProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);

			TextProperties newNodeMarkerTextProperties = new TextProperties();
			newNodeMarkerTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 9));

			Triangle newNodeMarker = null;
			Text newNodeMarkerText = null;
			if (isLeaf(nodes.get(i))) {
				newNodeMarker = lang.newTriangle(new Coordinates(posX - 20, posY + 10), new Coordinates(posX - 20 - 50, posY + 30),
						new Coordinates(posX - 20 - 50, posY - 10), "newnodemaker" + i, null, newNodeMarkerProperties);
				newNodeMarkerText = lang.newText(new Offset(3, 15, newNodeMarker, AnimalScript.DIRECTION_NW), newLeaf, "newnodemarkertext" + i, null,
						newNodeMarkerTextProperties);
			} else {
				int xRel = 33;
				newNodeMarker = lang.newTriangle(new Coordinates(posX - xRel, posY + 10), new Coordinates(posX - xRel - 50, posY + 30),
						new Coordinates(posX - xRel - 50, posY - 10), "newnodemaker" + i, null, newNodeMarkerProperties);
				newNodeMarkerText = lang.newText(new Offset(3, 15, newNodeMarker, AnimalScript.DIRECTION_NW), newNode, "newnodemarkertext" + i, null,
						newNodeMarkerTextProperties);
			}

			LinkedList<Primitive> primitives = new LinkedList<Primitive>();
			primitives.add(newNodeMarker);
			primitives.add(newNodeMarkerText);
			Group g = lang.newGroup(primitives, "group" + i);

			markers.add(g);


			// node titles
			TextProperties nodeTitleTextProperties = new TextProperties();
			nodeTitleTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 9));

			int xPosNew;
			int yPosNew = posY - 50;

			if (isLeaf(nodes.get(i))) {
				xPosNew = posX - 30;
			} else {
				xPosNew = posX + 5;
			}

			Text text = lang.newText(new Coordinates(xPosNew, yPosNew), "", "nodeTitle" + i, null, nodeTitleTextProperties);

			nodeTitles.add(text);

		}

		String[] labels = new String[numberOfNodes];
		for (int i = 0; i < numberOfNodes; i++) {
			String label = nodes.get(i).toString();
			labels[i] = label;
		}


		for (int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			int[] fromTo = fromIndexToIndex(e);
			Coordinate from = coords.get(fromTo[0]);
			Coordinate to = coords.get(fromTo[1]);

			TextProperties textProperties = new TextProperties();
			textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 9));
			Text text = lang.newText(new Coordinates((from.getX() + to.getX()) / 2, (from.getY() + to.getY()) / 2), edges.get(i).getLabel(),
					"edge_label_" + i, null, textProperties);
			edgeLabels.add(text);
		}


		Graph newTree = lang.newGraph("tree", adjacencyMatrix, treeNodes, labels, null, properties);

		for (int i = 0; i < nodes.size(); i++) {

			Color color;

			if (isLeaf(nodes.get(i))) {
				if (nodes.get(i).getLabel().equals("yes") || nodes.get(i).getLabel().equals("ja")) {
					color = yesColor;
				} else {
					color = noColor;
				}
				newTree.setNodeRadius(i, 20, null, null);

			} else {

				color = nodeColor;
				newTree.setNodeRadius(i, (int) (maxLabelSize * 4.5), null, null);
			}

			newTree.setNodeHighlightFillColor(i, color, null, null);

			if (!isLeaf(nodes.get(i))) {
				newTree.highlightNode(i, null, null);
			}
		}

		return newTree;
	}


	@Override
	public String toString() {
		return nodes + System.lineSeparator() + edges;
	}

}
