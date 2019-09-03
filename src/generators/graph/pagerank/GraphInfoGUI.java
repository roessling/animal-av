/**
 * 
 */
package generators.graph.pagerank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algoanim.primitives.Graph;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author marc
 *
 */
public class GraphInfoGUI extends PageRankGUI {
	// A list of all nodes in the graph
	private List<Node> nodes;

	// Map of the nodes to their textlabels
	private Map<Node, String> nodeLabels;
	
	private GraphProperties graphProperties;
	// The shown graph
	private Graph guiGraph;
	
	public GraphInfoGUI(Language language, Text header, Graph graph, GraphProperties graphProperties) {
		super(language, header);
		
		this.graphProperties = graphProperties;
		
		this.initGraph(graph);
	}
	
	private void initGraph(Graph graph) {
		this.guiGraph = cloneGraph(graph);

		this.nodes = Arrays.asList(guiGraph.getNodes());

		this.nodeLabels = new HashMap<>();
		for (Node node : nodes) {
			this.nodeLabels.put(node, guiGraph.getNodeLabel(node));
			this.guiGraph.setNodeLabel(node, String.format("%s (%.3g)", nodeLabels.get(node), 1d), null, null);
		}
	}
	
	private Graph cloneGraph(Graph graph) {
		int numberOfNodes = graph.getSize();

		Node[] nodes = new Node[numberOfNodes];
		String[] nodeLabels = new String[numberOfNodes];
		for (int i = 0; i < numberOfNodes; i++) {
			Coordinates coordinates = (Coordinates) graph.getNode(i);
			nodes[i] = new Offset(coordinates.getX(), coordinates.getY(), this.header, "SW");
			nodeLabels[i] = graph.getNodeLabel(i);
		}

		return lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), nodes, nodeLabels, graph.getDisplayOptions(),
				this.graphProperties);
	}
	
	public Graph getGraph() {
		return this.guiGraph;
	}
	
	public List<Node> getNodes() {
		return this.nodes;
	}
	
	public int indexOf(Node node) {
		return this.nodes.indexOf(node);
	}

	public Node getNode(int index) {
		return this.nodes.get(index);
	}
	
	public String getNodeLabel(Node node) {
		return this.nodeLabels.get(node);
	}
	
	public String getNodeLabel(int nodeIndex) {
		return this.nodeLabels.get(this.getNode(nodeIndex));
	}

	public int numberOfNodes() {
		return this.nodes.size();
	}
	
	public void setNodeLabel(Node node, String newNodeLabel) {
		this.guiGraph.setNodeLabel(node, newNodeLabel, null, null);
	}
	
	@Override
	public void hide() {
		this.guiGraph.hide();
	}
	
	@Override
	public void show() {
		this.guiGraph.show();
	}
}
