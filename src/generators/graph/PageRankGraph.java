package generators.graph;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Timing;
import algoanim.primitives.*;

/**
 * This a graph for animal script, that is optimized for the page rank algorithm.
 * It converts a normal animal graph into a PageRankGraph.
 * It has the following features:
 * Setting the radius of individual nodes.
 * Setting the base and highlight color of individual nodes and edges.
 * Detection of dangling nodes and separate features for dangling nodes like adding dangling edges
 * and separate functions for dangling edges.
 * Highlighting, unhighliting, show, hide of individual nodes and edges.
 * @author Jan Ulrich Schmitt
 *
 */
public class PageRankGraph{

	private Graph graph;
	
	PageRankNode nodes[];
	int[][] adjacencyMatrix;
	PageRankEdge[][] edgeMatrix;
	Language lang;
	
	int minRadius = 15;
	int maxRadius = 80;
	
	int drawDeph = 3;
	int textDrawDepth = 0;
	int edgeDrawDepth = 1;
	int nodeDrawDepth = 2;
	
	
	int minX = Integer.MAX_VALUE;
	int minY = Integer.MAX_VALUE;
	int maxX = Integer.MIN_VALUE;
	int maxY = Integer.MIN_VALUE;
	
	public PageRankGraph(Graph graph, Language lang) {
		this.graph = graph;
		adjacencyMatrix = graph.getAdjacencyMatrix();
		edgeMatrix = new PageRankEdge[adjacencyMatrix.length][adjacencyMatrix[0].length];
		this.lang = lang;
		
		init();
	}
	
	/**
	 * 
	 * @return the minimal x coordinate.
	 */
	public int getMinX()
	{
		return minX;
	}
	
	/**
	 * 
	 * @return the maximal x coordinate.
	 */
	public int getMaxX()
	{
		return maxX;
	}
	
	/**
	 * 
	 * @return the minimal y coordinate.
	 */
	public int getMinY()
	{
		return minY;
	}
	
	/**
	 * 
	 * @return the maximal y coordinate.
	 */
	public int getMaxY()
	{
		return maxY;
	}
	
	/**
	 * 
	 * @return the minimal radius of the nodes.
	 */
	public int getminRadius(){
		return minRadius;
	}
	
	/**
	 * 
	 * @return the maximal radius of the nodes.
	 */
	public int getmaxRadius(){
		return maxRadius;
	}
	
	/**
	 * 
	 * @return the total draw depth of the node circle.
	 */
	protected int getNodeDrawDepth()
	{
		return nodeDrawDepth+drawDeph;
	}
	
	/**
	 * 
	 * @return the total draw depth of the node text.
	 */
	protected int getTextDrawDepth()
	{
		return textDrawDepth+drawDeph;
	}
	
	/**
	 * 
	 * @return the total draw depth of the edges.
	 */
	protected int getEdgeDrawDepth()
	{
		return edgeDrawDepth+drawDeph;
	}
	
	/**
	 * Initializes the graph.
	 */
	private void init()
	{
		// setup nodes
		nodes = new PageRankNode[graph.getSize()];
		for(int i = 0; i < graph.getSize(); i++)
		{
			
			nodes[i] = new PageRankNode();
			
			CircleProperties cp = getCircleProperties(null);
			nodes[i].circle = lang.newCircle(graph.getNode(i),minRadius , graph.getNodeLabel(i), null, cp);
			nodes[i].radius = minRadius;
			//nodes[i].circle.moveTo("C", "translate", nodes[i].circle.getCenter(), null, null);
			TextProperties tp = getTextProperties();
			nodes[i].text = lang.newText(new Coordinates(((Coordinates)nodes[i].circle.getCenter()).getX(),((Coordinates)nodes[i].circle.getCenter()).getY()), graph.getNodeLabel(i), graph.getNodeLabel(i)+" text", null, tp);	
			nodes[i].text.moveTo("C", "translate", nodes[i].circle.getCenter(), null, null);
			nodes[i].text.moveBy("translate", -4, -8, null, null);
		}
		
		// set up normal edges
		for(int to = 0; to < graph.getSize(); to++)
		{
			for(int from = 0; from < graph.getSize(); from++)
			{
				if(adjacencyMatrix[from][to] != 0)
				{
					PageRankEdge edge = new PageRankEdge();				
					
					Coordinates lineNodes[] = getEdgeCoordinates(nodes[from], nodes[to]);
					edge.line = createLine(lineNodes);			
					edge.from = lineNodes[0];
					edge.to = lineNodes[1];
					
					edgeMatrix[from][to] = edge;
				}
			}
		}
	    
		// set up dangling Edges
		
		List<Integer> danglingNodes = getAllDanglingNodeNrs();
		
		for(Integer dNodeNr : danglingNodes)
		{
			for(int to = 0; to < nodes.length; to++)
			{
				PageRankEdge dEdge = new PageRankEdge();
				
				dEdge.isDanglingEdge = true;
				Coordinates lineNodes[] = getEdgeCoordinates(nodes[dNodeNr], nodes[to]);
				dEdge.line = createLine(lineNodes);			
				dEdge.from = lineNodes[0];
				dEdge.to = lineNodes[1];
				edgeMatrix[dNodeNr][to] = dEdge;
				
				hideEdge(dNodeNr, to);
			}
		}
		
		setMinMaxCoordinates();
		
	}
	
	/**
	 * sets the min max coordinate of the graph using the current position and maximal radius of the nodes.
	 */
	protected void setMinMaxCoordinates()
	{
		for(int i = 0; i < nodes.length; i++)
		{
			Coordinates centerPosition =  (Coordinates)nodes[i].circle.getCenter();
			
			int tmpMinX = centerPosition.getX()-getmaxRadius();
			int tmpMinY = centerPosition.getY()-getmaxRadius();
			int tmpMaxX = centerPosition.getX()+getmaxRadius();
			int tmpMaxY = centerPosition.getY()+getmaxRadius();
			
			if(tmpMinX < minX)
				minX= tmpMinX;
			
			if(tmpMinY < minY)
				minY = tmpMinY;
			
			if(tmpMaxX > maxX)
				maxX = tmpMaxX;
			
			if(tmpMaxY > maxY)
				maxY = tmpMaxY;
			
		}
	}

	protected Polyline createLine(Node lineNodes[])
	{
		
		PolylineProperties pp = getPolyLineProperties(null);
		return lang.newPolyline(lineNodes, "edge", null, pp);
	}
	
	/**
	 * Sets the label text of the the node.
	 * @param nodeNr the node id of the node.
	 * @param text the new text. The old one will be overridden.
	 */
	public void setNodeText(int nodeNr, String text){
		PageRankNode prn = nodes[nodeNr];
		prn.text.setText(text, null, null);
		
	}
	
	/**
	 * Sets the color of the node text.
	 * @param nodeNr the node id of the node.
	 * @param textColor the new color for the text.
	 */
	public void setTextColor(int nodeNr, Color textColor)
	{
		if(textColor != null)
		{
			nodes[nodeNr].text.changeColor("color", textColor, null, null);
		}
	}
	
	/**
	 * Sets the text color for all nodes.
	 * @param textColor the new text color.
	 */
	public void setAllTextColor(Color textColor)
	{
		for(int i = 0; i < nodes.length; i++)
		{
			setTextColor(i, textColor);
		}
	}
	
	/**
	 * Highlights the node with the given node number.
	 * @param nodeNr the node id.
	 */
	public void highlightNode(int nodeNr)
	{
		PageRankNode prn = nodes[nodeNr];
		
		if(prn.isHighlighted)
			return;
		
		prn.isHighlighted = true;
		
		
		prn.circle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, prn.highlightColor, null, null);
		
		//CircleProperties cp = getCircleProperties(prn);
		//prn.circle.hide();
		//prn.circle = lang.newCircle(prn.circle.getCenter(),prn.circle.getRadius() , prn.circle.getName(), null, cp);
		
	}
	
	/**
	 * Unhighlights the node with the given node number.
	 * @param nodeNr the node id.
	 */
	public void unhighlightNode(int nodeNr)
	{
		PageRankNode prn = nodes[nodeNr];
		
		if(!prn.isHighlighted)
			return;
	
		prn.isHighlighted = false;
		
		prn.circle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, prn.fillColor, null, null);
//		CircleProperties cp = getCircleProperties(prn);
//		prn.circle.hide();
//		prn.circle = lang.newCircle(prn.circle.getCenter(),prn.circle.getRadius() , prn.circle.getName(), null, cp);
		
	}
	
	
	/**
	 * Highlights the edge.
	 * @param from the node id of the node where the edge comes from.
	 * @param to the node id of the node where the edge points to.
	 * @param offset the offset when the highlight will start.
	 * @param duration the duration of the change.
	 */
	public void highlightEdge(int from, int to, Timing offset, Timing duration)
	{
		PageRankEdge edge = edgeMatrix[from][to];
		
		if(edge != null)
		{
			if(!edge.isHighlighted)
			{
				edge.line.changeColor("color", edge.highlightColor, offset, duration);
				edge.isHighlighted = true;
			}
		}
	}
	
	/**
	 * Unhighlights the edge.
	 * @param from the node id of the node where the edge comes from.
	 * @param to the node id of the node where the edge points to.
	 * @param offset the offset when the unhighlight will start.
	 * @param duration the duration of the change.
	 */
	public void unhighlightEdge(int from, int to, Timing offset, Timing duration)
	{
		PageRankEdge edge = edgeMatrix[from][to];
		
		if(edge != null)
		{
			if(edge.isHighlighted)
			{
				edge.line.changeColor("color", edge.baseColor, offset, duration);
				edge.isHighlighted = false;
			}
		}
	}
	
	/**
	 * Hides the complete graph.
	 */
	public void hideGraph(){
		
		//hide nodes
		for(int i = 0; i < nodes.length; i++)
		{
			nodes[i].circle.hide();
			nodes[i].text.hide();
		}
		
		// hide edges
		for(int from = 0; from < graph.getSize(); from++)
		{
			for(int to = 0; to < graph.getSize(); to++)
			{
				if(edgeMatrix[from][to] != null)
				{
					edgeMatrix[from][to].line.hide();
				}
			}
		}
	}
	
	/**
	 * Shows all elements of the graph except dangling edges.
	 */
	public void showGraph()
	{
		//show nodes
		for(int i = 0; i < nodes.length; i++)
		{
			nodes[i].circle.show();
			nodes[i].text.show();
		}
		
		// show edges
		for(int from = 0; from < graph.getSize(); from++)
		{
			for(int to = 0; to < graph.getSize(); to++)
			{
				if(edgeMatrix[from][to] != null && !edgeMatrix[from][to].isDanglingEdge)
				{
					edgeMatrix[from][to].line.show();
				}
			}
		}
				
	}
	
	/**
	 * Sets the base color of the given edge.
	 * If the edge is not highlighted the changes will be seen immediately.
	 * @param from the node id of the node where the edge comes from.
	 * @param to the node id of the node where the edge points to.
	 * @param newColor the new color of the edge.
	 */
	public void setEdgeBaseColor(int from, int to, Color newColor)
	{
		if(newColor == null)
			return;
		
		PageRankEdge edge = edgeMatrix[from][to];
		
		if(edge == null)
			return;
		
		edge.baseColor = newColor;
		
		if(!edge.isHighlighted)
		{
			edge.isHighlighted = true;
			unhighlightEdge(from, to, null,null);
		}
	}
	
	/**
	 * Sets the base color of all edges except for the dangling edges.
	 * @param edgeBaseColor
	 */
	public void setAllEdgesBaseColor(Color edgeBaseColor)
	{
		setAllEdgesBaseColor(edgeBaseColor, false);
			
	}
	
	/**
	 * Sets the base edge color for all edges. If setDanglingEdgesColor == false the color for
	 * the dangling edges won't be effected.
	 * @param edgeBaseColor the new base color of the edges.
	 * @param setDanglingEdgesColor controls if the dangling nodes should be set too.
	 */
	public void setAllEdgesBaseColor(Color edgeBaseColor, boolean setDanglingEdgesColor)
	{
		for(int from = 0; from < nodes.length; from++)
		{
			for(int to = 0; to < nodes.length; to ++)
			{
				if(setDanglingEdgesColor || adjacencyMatrix[from][to] != 0)
				{
					setEdgeBaseColor(from,to,edgeBaseColor);
				}
				
			}
		}
			
	}
	
	/**
	 * Sets the base color for all dangling edges.
	 * @param edgeBaseColor the new base color.
	 */
	public void setAllDangingEdgeBaseColor(Color edgeBaseColor)
	{
		PageRankEdge edge = null;
		for(int from = 0; from < nodes.length; from++)
		{
			for(int to = 0; to < nodes.length; to ++)
			{
				edge = edgeMatrix[from][to];
				if(edge != null && edge.isDanglingEdge)
				{
					setEdgeBaseColor(from,to,edgeBaseColor);
				}
			}
		}
	}
	
	/**
	 * Sets the highlight color for all the given edge.
	 * If the edge is highlighted the change will be seen immediately.
	 * @param from the node id of the node where the edge comes from.
	 * @param to the node id of the node where the edge points to.
	 * @param newColor the new color of the node.
	 */
	public void setEdgeHighlightColor(int from, int to, Color newColor )
	{
		if(newColor == null)
			return;
		
		PageRankEdge edge = edgeMatrix[from][to];
		
		if(edge == null)
			return;
		
		edge.highlightColor = newColor;
		
		if(edge.isHighlighted)
		{
			edge.isHighlighted = false;
			highlightEdge(from, to, null,null);
		}
		
	}
	
	
	/**
	 * Sets the highlight color for all edges except for dangling edges.
	 * @param edgeHighlightColor the new highlight color
	 */
	public void setAllEdgesHighlightColor(Color edgeHighlightColor)
	{
		setAllEdgesHighlightColor(edgeHighlightColor, false);
			
	}
	
	/**
	 * Sets the highlight color for all edges. If setDanglingEdgesHColor is false, 
	 * the highlight color for dangling edges won't be effected.
	 * @param edgeHighlightColor the new highlight color.
	 * @param setDanglingEdgesHColor if the highlight color for dangling edges should be set too.
	 */
	public void setAllEdgesHighlightColor(Color edgeHighlightColor,boolean setDanglingEdgesHColor )
	{
		for(int from = 0; from < nodes.length; from++)
		{
			for(int to = 0; to < nodes.length; to ++)
			{
				if(setDanglingEdgesHColor || adjacencyMatrix[from][to] != 0)
				{
					setEdgeHighlightColor(from,to,edgeHighlightColor);
				}
			}
		}
			
	}
	
	/**
	 * Set the highlight color for all dangling edges.
	 * @param edgeHighlightColor
	 */
	public void setAllDanglingEdgesHighlightColor(Color edgeHighlightColor)
	{
		PageRankEdge edge = null;
		for(int from = 0; from < nodes.length; from++)
		{
			for(int to = 0; to < nodes.length; to ++)
			{
				edge = edgeMatrix[from][to];
				if(edge != null && edge.isDanglingEdge)
				{
					setEdgeHighlightColor(from,to,edgeHighlightColor);
				}
			}
		}

	}
	
	/**
	 * Shows the given edge.
	 * @param from the node id of  the node where the edge comes from.
	 * @param to the node id of the node where the edge points to.
	 */
	public void showEdge(int from, int to)
	{
		PageRankEdge edge = edgeMatrix[from][to];
		
		if(edge != null)
		{
			edge.line.show();
		}
		
	}
	
	/**
	 * Hides the given edge.
	 * @param from the node id of  the node where the edge comes from.
	 * @param to the node id of the node where the edge points to.
	 */
	public void hideEdge(int from, int to)
	{
		PageRankEdge edge = edgeMatrix[from][to];
		
		if(edge != null)
		{
			edge.line.hide();
		}
	}
	
	/**
	 * Shows all Dangling edges.
	 */
	public void showAllDanglingEdges()
	{
		PageRankEdge edge = null;
		for(int from = 0; from < nodes.length; from++)
		{
			for(int to = 0; to < nodes.length; to ++)
			{
				edge = edgeMatrix[from][to];
				if(edge != null && edge.isDanglingEdge)
				{
					showEdge(from, to);
				}
			}
		}
	}
	
	/**
	 * Hides all dangling edges.
	 */
	public void hideAllDanglingEdges()
	{
		PageRankEdge edge = null;
		for(int from = 0; from < nodes.length; from++)
		{
			for(int to = 0; to < nodes.length; to ++)
			{
				edge = edgeMatrix[from][to];
				if(edge != null && edge.isDanglingEdge)
				{
					hideEdge(from, to);
				}
			}
		}
	}
	
	/**
	 * Sets the fill color of the given node.
	 * If the node is not highlighted, the change will be shown immediately.
	 * @param nodeNr the node id.
	 * @param newColor the new base color for the node.
	 */
	public void setNodeFillColor(int nodeNr, Color newColor)
	{
		if(newColor == null)
			return;
		
		PageRankNode node = nodes[nodeNr];
		
		node.fillColor = newColor;
		
		if(!node.isHighlighted)
		{
			node.isHighlighted = true;
			unhighlightNode(nodeNr);
		}
	}
	
	/**
	 * Sets the highlight color for the given node.
	 * If the node is highlighted, the change will be shown immediately.
	 * @param nodeNr the node id.
	 * @param newColor the new highlight color.
	 */
	public void setNodeHighlightColor(int nodeNr, Color newColor)
	{
		if(newColor == null)
			return;
		
		PageRankNode node = nodes[nodeNr];
		
		node.highlightColor = newColor;
		
		if(node.isHighlighted)
		{
			node.isHighlighted = false;
			highlightNode(nodeNr);
		}
	}
	
	/**
	 * Sets the highlight color for all nodes.
	 * @param highlightColor the new highlight color.
	 */
	public void setAllNodeHighlightColor(Color highlightColor)
	{
		for(int i = 0; i < nodes.length; i++)
		{
			setNodeHighlightColor(i, highlightColor);
		}
	}
	
	/**
	 * Sets the node size of the given node.
	 * The size will be clipped to the minimal radius and maximal radius.
	 * @param nodeNr the node id.
	 * @param newRadius the new radius.
	 */
	public void setNodeSize(int nodeNr, int newRadius)
	{
		Timing duration = new MsTiming(500);
		PageRankNode prn = nodes[nodeNr];
		
		if(newRadius < minRadius)
		{
			newRadius = minRadius;
		}else if(newRadius > maxRadius)
		{
			newRadius = maxRadius;
		}
	
		prn.circle.moveBy("translateRadius", newRadius-prn.radius, newRadius-prn.radius, null,duration);
		prn.radius = newRadius;
//		CircleProperties cp = getCircleProperties(prn);
//		prn.circle.hide();
//		prn.circle = lang.newCircle(prn.circle.getCenter(),newRadius , prn.circle.getName(), null, cp);
//		
		updateEdgesForNode(nodeNr,duration);
	
	}
	
	/**
	 * Checks weather the given node is a dangling node.
	 * @param nodeNr the node id of  the node to check.
	 * @return true if the node is a dangling node, false otherwise.
	 */
	public boolean isDanglingNode(int nodeNr)
	{
		for(int to = 0; to < nodes.length; ++to)
		{
			if(adjacencyMatrix[nodeNr][to] != 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the ids of all dangling nodes.
	 * @return a new list with the ids of all dangling nodes.
	 */
	public List<Integer> getAllDanglingNodeNrs()
	{
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		for(int i = 0; i < nodes.length; ++i)
		{
			if(isDanglingNode(i))
			{
				list.add(i);
			}
		}
		
		return list;
	}
	
	/**
	 * Update the positions of all edges that comes from or points to the node.
	 * @param nodeNr the node id.
	 * @param duration the duration of the change.
	 */
	protected void updateEdgesForNode(int nodeNr, Timing duration)
	{
		for(int to = 0; to < graph.getSize(); to++)
		{
			if(edgeMatrix[nodeNr][to] != null)
			{
				updateEdge(edgeMatrix[nodeNr][to], nodes[nodeNr], nodes[to],duration);
			}
			
			if(edgeMatrix[to][nodeNr] != null)
			{
				updateEdge(edgeMatrix[to][nodeNr], nodes[to], nodes[nodeNr],duration);
			}
								
		}
	}
	
	/**
	 * Updates the position for the given edge.
	 * @param edge the edge to update.
	 * @param from the node where the edge comes from.
	 * @param to the node where the edge points to.
	 * @param duration the duration of the change.
	 */
	protected void updateEdge(PageRankEdge edge, PageRankNode from, PageRankNode to, Timing duration)
	{
		Coordinates lineNodes[] = getEdgeCoordinates(from, to);
//		edge.line.moveTo("C","translateNodes 1",lineNodes[0] ,null, null);
//		edge.line.moveTo("C","translateNodes 2",lineNodes[1] ,null, null);
		edge.line.moveBy("translateNodes 1", lineNodes[0].getX()-edge.from.getX(),lineNodes[0].getY()-edge.from.getY(), null, duration);
		edge.line.moveBy("translateNodes 2", lineNodes[1].getX()-edge.to.getX(),lineNodes[1].getY()-edge.to.getY(), null, duration);
		
//		PolylineProperties pp = getPolyLineProperties(edge);
//		
//		edge.line.hide();
//		edge.line = lang.newPolyline(lineNodes, "edge", null, pp); 
		edge.from = lineNodes[0];
		edge.to = lineNodes[1];
	}
	
	/**
	 * @param from the node where the edge comes from.
	 * @param to the node where the edge points to.
	 * @return the coordinates of the edge.
	 */
	protected Coordinates[] getEdgeCoordinates(PageRankNode from, PageRankNode to)
	{
		Coordinates c1 = ((Coordinates)from.circle.getCenter());
		Coordinates c2 = ((Coordinates)to.circle.getCenter());
				
		int vecx = c2.getX()-c1.getX();
		int vecy = c2.getY()-c1.getY();
		
		double length =  Math.sqrt(vecx*vecx+vecy*vecy);
		
		if(length <= from.radius + to.radius)
		{
			//TODO: do something other when nodes are overlapping
		}
		
		int fromX = c1.getX() + (int)((float)vecx/(float)length * (float)from.radius);
		int fromY = c1.getY() +(int)((float)vecy/(float)length * (float)from.radius);
		
		int toX = c1.getX() + (int)((float)vecx/(float)length * (float)(length - to.radius));
		int toY = c1.getY() +(int)((float)vecy/(float)length * (float)(length - to.radius));
		
		Coordinates result[] = {new Coordinates(fromX, fromY),new Coordinates(toX, toY)};
		
		return result;
		
	}
	
	/**
	 * Return a new default property for a circle representing the node.
	 * @param node the node id.
	 * @return a new circle property for the given node.
	 */
	protected CircleProperties getCircleProperties(PageRankNode node)
	{
		CircleProperties cp = new CircleProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
		cp.set(AnimationPropertiesKeys.DEPTH_PROPERTY,getNodeDrawDepth());
		
		if(node != null)
		{
			if(node.isHighlighted)
			{
				cp.set(AnimationPropertiesKeys.FILL_PROPERTY,node.highlightColor);
			}else
			{
				cp.set(AnimationPropertiesKeys.FILL_PROPERTY,node.fillColor);
			}
		}else
		{
			cp.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.WHITE);
		}
		
		return cp;
	}
	
	/**
	 * Creates a default text property for the node text.
	 * @return a new text property.
	 */
	protected TextProperties getTextProperties()
	{
		TextProperties tp = new TextProperties();
    	tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.PLAIN, 12));
    	tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY,getTextDrawDepth());
    	return tp;
	}
	
	/**
	 * Returns a new default PolylineProperties for the given edge.
	 * @param edge
	 * @return a new PolylineProperties that fits to the given edge.
	 */
	protected PolylineProperties getPolyLineProperties(PageRankEdge edge)
	{
		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, getEdgeDrawDepth());
		
		if(edge != null)
		{
			if(edge.isHighlighted)
			{
				pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, edge.highlightColor);
			}else
			{
				pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, edge.baseColor);
			}
		}else
		{
			pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		}
		
		return pp;
	}
	
	/**
	 * Represents a node in PageRankGraph.
	 * @author Rouh
	 *
	 */
	protected class PageRankNode
	{
		public Text text = null;
		public Circle circle = null;
		public int radius = minRadius;
		public Color highlightColor = Color.GREEN;
		public Color fillColor = Color.WHITE;
		public boolean isHighlighted = false;
	}
	
	/**
	 * Represents an edge in the PageRankGraph.
	 * @author Rouh
	 *
	 */
	protected class PageRankEdge
	{
		public Polyline line = null;
		public Color highlightColor = Color.GREEN;
		public Color baseColor = Color.BLACK;
		public boolean isHighlighted = false;
		public Coordinates from;
		public Coordinates to;
		public boolean isDanglingEdge = false;
	}
	
	
}
