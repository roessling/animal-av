/*
 * Created on 04.04.2007 by Guido Roessling (roessling@acm.org>
 */
package algoanim.animalscript;

import java.awt.Color;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.generators.GraphGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.util.Node;
import algoanim.util.Timing;

public class AnimalGraphGenerator extends AnimalGenerator implements
		GraphGenerator {
	/**
	 * @param as
	 *          the associated <code>Language</code> object.
	 */
	public AnimalGraphGenerator(AnimalScript as) {
		super(as);
	}
	
	
	public void create(Graph graph) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("graph \"").append(graph.getName()).append("\" size ");
		sb.append(graph.getSize()).append(" ");
		
		GraphProperties graphProps = graph.getProperties();
		addColorOption(graphProps, sb);
		addColorOption(graphProps, AnimationPropertiesKeys.FILL_PROPERTY, " bgColor ", sb);
		addColorOption(graphProps, 
				AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, " highlightColor ", sb);
		addColorOption(graphProps, 
				AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, " elemHighlightColor ", sb);
		addColorOption(graphProps, 
				AnimationPropertiesKeys.NODECOLOR_PROPERTY, " nodeFontColor ", sb);
		addColorOption(graphProps, 
				AnimationPropertiesKeys.EDGECOLOR_PROPERTY, " edgeFontColor ", sb);
		addBooleanOption(graphProps, AnimationPropertiesKeys.DIRECTED_PROPERTY, " directed ", sb);
		addBooleanOption(graphProps, AnimationPropertiesKeys.WEIGHTED_PROPERTY, " weighted ", sb);
		
		// nodes { label [at] coord }
		sb.append("nodes {");
		int nrNodes = graph.getSize();
		for (int node = 0; node < nrNodes; node++) {
			sb.append("\"").append(graph.getNodeLabel(node)).append("\" ");
			sb.append(AnimalGenerator.makeNodeDef(graph.getNode(node)));
			if (node < nrNodes - 1)
				sb.append(',');
			sb.append(" ");
		}
		
		// edges { (start, end[, weight]) }
		// nodes { label [at] coord }
		sb.append("} edges {");
		for (int row = 0; row < nrNodes; row++) {
			for (int col = 0; col < nrNodes; col++) {
				if (graph.getEdgeWeight(row, col) != 0) {
					sb.append("(").append(row).append(", ").append(col);
					sb.append(", \"").append(graph.getEdgeWeight(row, col)).append("\") ");
				}
			}
		}
		sb.append("} ");
		addIntOption(graphProps, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", sb);
//		if (graphProps.get(AnimationPropertiesKeys.DEPTH_PROPERTY) != null) {
//			sb.append("depth ");
//			sb.append(graphProps.get(AnimationPropertiesKeys.DEPTH_PROPERTY));
//			sb.append(" ");
//		}
		lang.addLine(sb.toString());
	}
	
	

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
			Timing offset, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("highlightEdge on \"").append(graph.getName());
		sb.append("\" (").append(startNode).append(", ");
		sb.append(endNode).append(") ");
		addWithTiming(sb, offset, duration);
	}

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
			Timing offset, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("unhighlightEdge on \"").append(graph.getName());
		sb.append("\" (").append(startNode).append(", ");
		sb.append(endNode).append(") ");
		addWithTiming(sb, offset, duration);
	}

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
			Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("highlightNode on \"").append(graph.getName());
		sb.append("\" nodes ").append(node);
		addWithTiming(sb, offset, duration);
	}

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
			Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("unhighlightNode on \"").append(graph.getName());
		sb.append("\" nodes ").append(node);
		addWithTiming(sb, offset, duration);
	}

	public void hideNode(Graph graph, int index, Timing offset, Timing duration) {
		generateShowHideNode(graph, false, index, offset, duration);
	}

	public void hideNodes(Graph graph, int[] indices, Timing offset, 
			Timing duration) {
		generateShowHideNodes(graph, false, indices, offset, duration);
	}

	private void generateShowHideNode(Graph graph, boolean isShow, int index,
			Timing offset, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		String tag = (isShow) ? "show" : "hide";
		sb.append(tag).append(" \"").append(graph.getName());
		sb.append("\" type \"").append(tag).append("Node ");
		sb.append(index).append("\" ");
		addWithTiming(sb, offset, duration);
	}
	
	private void generateShowHideNodes(Graph graph, boolean isShow, int[] indices,
			Timing offset, Timing duration) {
		String tag = (isShow) ? "show" : "hide";
		StringBuilder sb = new StringBuilder(128);
		sb.append(tag).append(" \"").append(graph.getName());
		sb.append("\" type \"").append(tag).append("Nodes");
		for (int i = 0; i < indices.length; i++)
			sb.append(" ").append(indices[i]);
		sb.append("\" ");
		addWithTiming(sb, offset, duration);
	}
	
	
	public void showNode(Graph graph, int index, Timing offset, Timing duration) {
		generateShowHideNode(graph, true, index, offset, duration);
	}

	public void showNodes(Graph graph, int[] indices, Timing offset, Timing duration) {
		generateShowHideNodes(graph, true, indices, offset, duration);
	}

	public void hideEdge(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration) {
		generateShowHideEdge(graph, false, startNode, endNode, offset, duration);
	}
	
	public void hideEdgeWeight(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("highlightEdge on \"").append(graph.getName());
		sb.append("\" type \"hide edge weight\" (").append(startNode).append(", ");
		sb.append(endNode).append(") ");
		addWithTiming(sb, offset, duration);
	}


	public void showEdge(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration) {
		generateShowHideEdge(graph, true, startNode, endNode, offset, duration);
	}

	public void showEdgeWeight(Graph graph, int startNode, int endNode, 
			Timing offset, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("highlightEdge on \"").append(graph.getName());
		sb.append("\" type \"show edge weight\" (").append(startNode).append(", ");
		sb.append(endNode).append(") ");
		addWithTiming(sb, offset, duration);
	}

	private void generateShowHideEdge(Graph graph, boolean isShow, int startNode,
			int endNode, Timing offset, Timing duration) {
		String tag = (isShow) ? "show" : "hide";
		StringBuilder sb = new StringBuilder(128);
		sb.append(tag).append(" \"").append(graph.getName());
		sb.append("\" type \"").append(tag).append("Edge (");
		sb.append(startNode).append(", ").append(endNode).append(")\" ");
		addWithTiming(sb, offset, duration);
	}

	public void setEdgeWeight(Graph graph, int startNode, int endNode,
			String weight, Timing offset, Timing duration) {
		StringBuilder str = new StringBuilder();
		str.append("setText of \"").append(graph.getName());
		str.append("\" type \"setEdgeWeight(").append(startNode).append(",");
		str.append(endNode).append(")\" to \"");
		str.append(weight).append("\"");
		addWithTiming(str, offset, duration);		
	}

	
	public void translateNode(Graph graph, int nodeIndex, Node location, 
			Timing offset, Timing duration)   {
		try {
			moveTo(graph, null, "translate #" + nodeIndex, location, offset, 
					duration);
		} catch (IllegalDirectionException ide) {
			System.err.println("IllegalDirectionException@AnimalGraphGenerator.translateNode");
		}
	}
	
	public void translateNodes(Graph graph, int[] nodeIndices, Node location, 
			Timing offset, Timing duration)   {
		if (nodeIndices != null) {
			StringBuilder sb = new StringBuilder(64);
			sb.append("translateNodes");
			for (int i = 0; i < nodeIndices.length; i++) {
				sb.append(' ').append(nodeIndices[i]);
			}
			try {
				moveTo(graph, null, sb.toString(), location, offset, duration);
			} catch (IllegalDirectionException ide) {
				System.err.println("IllegalDirectionException@AnimalGraphGenerator.translateNodes");
			}
		}
	}

	public void translateWithFixedNodes(Graph graph, int[] nodeIndices, 
			Node location, Timing offset, Timing duration) {
		if (nodeIndices != null) {
			StringBuilder sb = new StringBuilder(64);
			sb.append("translateWithFixedNodes");
			for (int i = 0; i < nodeIndices.length; i++) {
				sb.append(' ').append(nodeIndices[i]);
			}
			try {
				moveTo(graph, null, sb.toString(), location, offset, duration);
			} catch (IllegalDirectionException ide) {
				System.err.println("IllegalDirectionException@AnimalGraphGenerator.translateWithFixedNodes");
			}
		}
	}


	@Override
	public void setColorType(Graph graph, int node, String typ, Color c, Timing offset, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append(typ+" on \"").append(graph.getName());
		sb.append("\" nodes ").append(node);
		sb.append(" with color (").append(c.getRed()+","+c.getGreen()+","+c.getBlue()).append(")");
		addWithTiming(sb, offset, duration);
	}
	
	@Override
	public void setColorType(Graph graph, int node1, int node2, String typ, Color c, Timing offset, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append(typ+" on \"").append(graph.getName());
		sb.append("\" edge ").append(node1).append(" ").append(node2);
		sb.append(" with color (").append(c.getRed()+","+c.getGreen()+","+c.getBlue()).append(")");
		addWithTiming(sb, offset, duration);
	}

	public void setNodeLabel(Graph graph, int node,
			String label, Timing offset, Timing duration) {
		StringBuilder str = new StringBuilder();
		str.append("setNodeLabel of \"").append(graph.getName());
		str.append("\" node ").append(node).append(" to \"");
		str.append(label).append("\"");
		addWithTiming(str, offset, duration);		
	}

	public void setNodeRadius(Graph graph, int node,
			String radius, Timing offset, Timing duration) {
		StringBuilder str = new StringBuilder();
		str.append("setNodeRadius of \"").append(graph.getName());
		str.append("\" node ").append(node).append(" to \"");
		str.append(radius).append("\"");
		addWithTiming(str, offset, duration);		
	}
}
