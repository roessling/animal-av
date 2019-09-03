package algoanim.primitives.generators;

import java.awt.Color;

import algoanim.primitives.Graph;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * <code>GraphGenerator</code> offers methods to request the included 
 * Language object to
 * append graph-related script code lines to the output.
 * It is designed to be included by a <code>Graph</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Graph</code> primitive has to 
 * implement its own <code>GraphGenerator</code>, which is then 
 * responsible to create proper script code.  
 * 
 * @author Dr. Guido Roessling (roessling@acm.org> 
 * @version 0.7 2007-04-04
 */
public interface GraphGenerator extends GeneratorInterface { 
	/**
	 * Creates the originating script code for a given <code>graph</code>,
	 * due to the fact that before a primitive can be worked with it has to be 
	 * defined and made known to the script language.
	 * 
	 * @param graph the <code>graph</code> for which the initiate script code
	 * shall be created. 
	 */
	public void create(Graph graph);
	
	/**
	 * sets the weight of a given edge
	 * 
	 * @param graph the underlying graph
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 * @param weight the new weight of the edge
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void setEdgeWeight(Graph graph, int startNode, int endNode,
			String weight, Timing offset, Timing duration);
	
	
	/**
	 * sets the position of a given node
	 * 
	 * @param graph the underlying graph
	 * @param nodeIndex the index of the node to be moved
	 * @param location the target position of the node
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void translateNode(Graph graph, int nodeIndex, 
			Node location, Timing offset, Timing duration);

	/**
	 * sets the position of a given set of nodes
	 * 
	 * @param graph the underlying graph
	 * @param nodeIndices the indices of the nodes to be moved
	 * @param location the target position of the node
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void translateNodes(Graph graph, int[] nodeIndices, 
			Node location, Timing offset, Timing duration);

	/**
	 * sets the position of a given set of nodes
	 * 
	 * @param graph the underlying graph
	 * @param nodeIndices the indices of the nodes to be moved
	 * @param location the target position of the node
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void translateWithFixedNodes(Graph graph, int[] nodeIndices, 
			Node location, Timing offset, Timing duration);

	
	/**
	 * hides a selected (previously visible?) graph edge by turning it invisible
	 * 
	 * @param startNode the start index of the edge to be hidden
	 * @param endNode the end index of the edge to be hidden
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void hideEdge(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration);
	
	/**
	 * hides a selected (previously visible?) graph edge weight by turning it invisible
	 * 
	 * @param startNode the start index of the edge weight to be hidden
	 * @param endNode the end index of the edge weight to be hidden
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void hideEdgeWeight(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration);
	
	/**
	 * hide a selected graph node by turning it invisible
	 * 
	 * @param index the index of the node to be hidden
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void hideNode(Graph graph, int index, Timing offset, 
			Timing duration);

	
	/**
	 * hide a selected set of graph nodes by turning them invisible
	 * 
	 * @param indices the set of node indices to be hidden
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void hideNodes(Graph graph, int[] indices, Timing offset, 
			Timing duration);

	/**
	 * show a selected (previously hidden?) graph edge  by turning it visible
	 * 
	 * @param startNode the start index of the edge to be shown
	 * @param endNode the end index of the node to be shown
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void showEdge(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration);

	/**
	 * show a selected (previously hidden?) graph edge weightby turning it visible
	 * 
	 * @param startNode the start index of the edge weight to be shown
	 * @param endNode the end index of the node weight to be shown
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void showEdgeWeight(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration);


	/**
	 * show a selected (previously hidden) graph node by turning it visible
	 * 
	 * @param index the index of the node to be shown
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void showNode(Graph graph, int index, Timing offset, 
			Timing duration);
	
	/**
	 * show a selected (previously hidden) graph node by turning it visible
	 * 
	 * @param indices the index of the node to be shown
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void showNodes(Graph graph, int[] indices, Timing offset, 
			Timing duration);
	
	
	
	/**
	 * Highlights the graph edge at a given position after a distinct offset.
	 * 
	 * @param graph the graph on which the operation is performed
	 * @param startNode the start node of the edge to highlight.
	 * @param endNode the end node of the edge to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void highlightEdge(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration);
	
	/**
	 * Unhighlights the graph edge at a given position after a distinct offset.
	 * 
	 * @param graph the graph on which the operation is performed
	 * @param startNode the start node of the edge to unhighlight.
	 * @param endNode the end node of the edge to unhighlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void unhighlightEdge(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration);
	
	/**
	 * Highlights the chosen graph node after a distinct offset.
	 * 
	 * @param graph the graph on which the operation is performed
	 * @param node the node to highlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void highlightNode(Graph graph, int node, Timing offset, 
			Timing duration);
	
	/**
	 * Unhighlights the chosen graph node after a distinct offset.
	 * 
	 * @param graph the graph on which the operation is performed
	 * @param node the node to unhighlight.
	 * @param offset [optional] the offset after which the operation shall be 
	 * started.
	 * @param duration [optional] the duration this operation lasts.
	 */
	public void unhighlightNode(Graph graph, int node, Timing offset,
			Timing duration);
	

	public void setColorType(Graph graph, int node, String typ, Color c, Timing offset, Timing duration);
	public void setColorType(Graph graph, int node1, int node2, String typ, Color c, Timing offset, Timing duration);
	
	public void setNodeLabel(Graph graph, int node, String label, Timing offset, Timing duration);
	public void setNodeRadius(Graph graph, int node, String radius, Timing offset, Timing duration);
}
