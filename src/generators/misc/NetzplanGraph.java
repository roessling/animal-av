package generators.misc;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringMatrixGenerator;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.StringMatrix;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class NetzplanGraph {
	
	public enum CellID{EarliestStartTime, LatestStartTime, EarliestEndTime, LatestEndTime, ProcessTime, Name};
	
	HashMap<Integer, NetzplanNode> nodes = new HashMap<Integer, NetzplanNode>();
	
	AnimalScript lang;
	
	String invalidChar = "-";
	
	int cellWidth = 50;
	int cellHeight = 20;
	
	int estColumn = 1;
	int estRow = 0;
	int lstColumn = 1;
	int lstRow = 1;
	int eetColumn = 2;
	int eetRow = 0;
	int letColumn = 2;
	int letRow = 1;
	int ptColumn = 0;
	int ptRow = 1;
	int nameColumn = 0;
	int nameRow = 0;
	
	int drawDeph = 3;
	int edgeDrawDepth = 0;
	int nodeDrawDepth = 1;
	
	MatrixProperties defaultMatrixProperties = null;
	
	private int minX = Integer.MAX_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxY = Integer.MIN_VALUE;
	
	
	public NetzplanGraph(AnimalScript lang, Graph graph)
	{
		this(lang, graph, null);
	}
	
	public NetzplanGraph(AnimalScript lang, Graph graph, MatrixProperties defaultMatrixProperties)
	{
		this.defaultMatrixProperties = defaultMatrixProperties;
		this.lang = lang;
		init(graph);
	}
	
	public int getMinX()
	{
		return minX;
	}
	
	public int getMinY()
	{
		return minY;
	}
	
	public int getMaxX()
	{
		return maxX;
	}
	
	public int getMaxY()
	{
		return maxY;
	}
	
	public void setProcessTime(int id, int time)
	{
		this.setEntry(id, CellID.ProcessTime, String.valueOf(time));
	}
	
	public int getProcessTime(int id)
	{
		int result = -1;
		try
		{
			result = Integer.valueOf(this.getEntry(id, CellID.ProcessTime));
		}catch(NumberFormatException e)
		{
			result = -1;
		}
		return result;
	}
	
	
	public void setEarliestStartTime(int id, int time)
	{
		this.setEntry(id, CellID.EarliestStartTime, String.valueOf(time));
	}
	
	public int getEarliestStartTime(int id)
	{
		int result = -1;
		try
		{
			result = Integer.valueOf(this.getEntry(id, CellID.EarliestStartTime));
		}catch(NumberFormatException e)
		{
			result = -1;
		}
		return result;
	}
	
	public void setLatestStartTime(int id, int time)
	{
		this.setEntry(id, CellID.LatestStartTime, String.valueOf(time));
	}
	
	public int getLatestStartTime(int id)
	{
		int result = -1;
		try
		{
			result = Integer.valueOf(this.getEntry(id, CellID.LatestStartTime));
		}catch(NumberFormatException e)
		{
			result = -1;
		}
		return result;
	}
	
	public void setEarliestEndTime(int id, int time)
	{
		this.setEntry(id, CellID.EarliestEndTime, String.valueOf(time));
	}
	
	public int getEarliestEndTime(int id)
	{
		int result = -1;
		try
		{
			result = Integer.valueOf(this.getEntry(id, CellID.EarliestEndTime));
		}catch(NumberFormatException e)
		{
			result = -1;
		}
		return result;
	}
	
	public void setLatestEndTime(int id, int time)
	{
		this.setEntry(id, CellID.LatestEndTime, String.valueOf(time));
	}
	
	public int getLatestEndTime(int id)
	{
		int result = -1;
		try
		{
			result = Integer.valueOf(this.getEntry(id, CellID.LatestEndTime));
		}catch(NumberFormatException e)
		{
			result = -1;
		}
		return result;
	}
	
	public void setName(int id, String name)
	{
		this.setEntry(id, CellID.Name, name);
	}
	
	public String getName(int id)
	{
		return this.getEntry(id, CellID.Name);
	}
	
	public List<Integer> getSuccessors(int id)
	{
		if(hasNode(id))
		{
			return getNetzplanNode(id).sucsessors;
		}
		return new LinkedList<Integer>();
	}
	
	public List<Integer> getPredecessors(int id)
	{
		if(hasNode(id))
		{
			return getNetzplanNode(id).predecessors;
		}
		return new LinkedList<Integer>();
	}
	
	public List<Integer> getAllNodes()
	{
		LinkedList<Integer> allNodes = new LinkedList<Integer>();
		
		for(Integer nodeId : nodes.keySet())
		{
			allNodes.add(nodeId);
		}
		return allNodes;
	}
	
	public boolean isStartNode(int id)
	{
		if(hasNode(id))
		{
			
			return getNetzplanNode(id).predecessors.size() == 0;
		}
		return false;
	}
	
	public boolean isEndNode(int id)
	{
		if(hasNode(id))
		{
			
			return getNetzplanNode(id).sucsessors.size() == 0;
		}
		return false;
	}
	
	public List<Integer> getEndNodes()
	{
		List<Integer> endNodes =  new LinkedList<Integer>();
		
		for(Integer nodeId : nodes.keySet())
		{
			if(isEndNode(nodeId))
			{
				endNodes.add(nodeId);
			}
		}
		
		return endNodes;
	}
	
	
	public List<Integer> getStartNodes()
	{
		List<Integer> startNodes =  new LinkedList<Integer>();
		
		for(Integer nodeId : nodes.keySet())
		{
			if(isStartNode(nodeId))
			{
				startNodes.add(nodeId);
			}
		}
		
		return startNodes;
	} 
	
	public void hideGraph()
	{
		for(NetzplanNode node: nodes.values())
		{
			node.values.hide();
			
			for(NetzplanEdge edge: node.edges.values())
			{
				edge.line.hide();
			}
		}
	}
	
	
	public void showGraph()
	{
		for(NetzplanNode node: nodes.values())
		{
			node.values.show();
			
			for(NetzplanEdge edge: node.edges.values())
			{
				edge.line.show();
			}
		}
	}
	
	public void highlightNode(int id)
	{
		if(hasNode(id))
		{
			highlightCell(id, CellID.EarliestEndTime);
			highlightCell(id, CellID.EarliestStartTime);
			highlightCell(id, CellID.LatestEndTime);
			highlightCell(id, CellID.LatestStartTime);
			highlightCell(id, CellID.ProcessTime);
			highlightCell(id, CellID.Name);
			
		}
	}
	
	public void unhighlightNode(int id)
	{
		if(hasNode(id))
		{
			unhighlightCell(id, CellID.EarliestEndTime);
			unhighlightCell(id, CellID.EarliestStartTime);
			unhighlightCell(id, CellID.LatestEndTime);
			unhighlightCell(id, CellID.LatestStartTime);
			unhighlightCell(id, CellID.ProcessTime);
			unhighlightCell(id, CellID.Name);	
		}
	}
	
	
	public void highlightCell(int id, CellID cell)
	{
		if(hasNode(id))
		{
			NetzplanNode node = getNetzplanNode(id);
			
			switch(cell)
			{
			case EarliestStartTime:
				node.values.highlightCell(estRow, estColumn, null, null);
				break;
			case LatestStartTime:
				node.values.highlightCell(lstRow, lstColumn, null, null);
				break;
			case EarliestEndTime:
				node.values.highlightCell(eetRow, eetColumn, null, null);
				break;
			case LatestEndTime:
				node.values.highlightCell(letRow, letColumn, null, null);
				break;
			case ProcessTime:
				node.values.highlightCell(ptRow, ptColumn, null, null);
				break;
			case Name:
				node.values.highlightCell(nameRow, nameColumn, null, null);
				break;
			}
		}
	}
	
	public void unhighlightCell(int id, CellID cell)
	{
		if(hasNode(id))
		{
			NetzplanNode node = getNetzplanNode(id);
			
			switch(cell)
			{
			case EarliestStartTime:
				node.values.unhighlightCell(estRow, estColumn, null, null);
				break;
			case LatestStartTime:
				node.values.unhighlightCell(lstRow, lstColumn, null, null);
				break;
			case EarliestEndTime:
				node.values.unhighlightCell(eetRow, eetColumn, null, null);
				break;
			case LatestEndTime:
				node.values.unhighlightCell(letRow, letColumn, null, null);
				break;
			case ProcessTime:
				node.values.unhighlightCell(ptRow, ptColumn, null, null);
				break;
			case Name:
				node.values.unhighlightCell(nameRow, nameColumn, null, null);
				break;
			}
		}
	}
	
	public void highlightEdge(int from, int to)
	{
		if(hasEdge(from, to))
		{
			NetzplanEdge edge = getNetzplanEdge(from, to);
			if(!edge.isHighlighted)
			{
				edge.line.changeColor("color", edge.highlightColor, null, null);
				edge.isHighlighted = true;
			}
		}
		
	}
	
	public void unHighlightEdge(int from, int to)
	{
		if(hasEdge(from, to))
		{
			NetzplanEdge edge = getNetzplanEdge(from, to);
			if(edge.isHighlighted)
			{
				edge.line.changeColor("color", edge.baseColor, null, null);
				edge.isHighlighted = false;
			}
		}
	}
	
	public void setAllNodeHighlightColor(Color color)
	{
		for(Integer nodeId : nodes.keySet())
		{
			setNodeHighlightColor(nodeId, color);
		}
	}
	
	public void setNodeHighlightColor(int nodeId, Color color)
	{
		if(hasNode(nodeId))
		{
			//TODO: implement
			//nodes.get(nodeId).values.getProperties().set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, color);
			//lang.addLine(line);
			//nodes.get(nodeId).values.changeColor(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, color, null, null);
		}
	}
	
	public void setAllNodeBaseColor(Color color)
	{
		for(Integer nodeId : nodes.keySet())
		{
			setNodeBaseColor(nodeId, color);
		}
	}
	
	public void setNodeBaseColor(int nodeId, Color color)
	{
		if(hasNode(nodeId))
		{
			nodes.get(nodeId).values.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, color, null, null);
		}
	}
	
	public void setAllEdgeHightlightColor(Color color)
	{
		for(Integer from : nodes.keySet())
		{
			NetzplanNode node =  nodes.get(from);
			
			for(Integer to : node.edges.keySet())
			{
				setEdgeHighlightColor(from, to, color);
			}
		}
	}
	
	public void setEdgeHighlightColor(int from, int to, Color color)
	{
		if(hasEdge(from, to))
		{
			NetzplanEdge edge = getNetzplanEdge(from, to);
			
			edge.highlightColor = color;
			if(edge.isHighlighted)
			{
				edge.line.changeColor("color", edge.highlightColor, null, null);
			}
		}
	}
	
	public void setAllEdgeBaseColor(Color color)
	{
		for(Integer from : nodes.keySet())
		{
			NetzplanNode node =  nodes.get(from);
			
			for(Integer to : node.edges.keySet())
			{
				setEdgeBaseColor(from, to, color);
			}
		}
	}
	
	public void setEdgeBaseColor(int from, int to, Color color)
	{
		if(hasEdge(from, to))
		{
			NetzplanEdge edge = getNetzplanEdge(from, to);
			
			edge.baseColor = color;
			if(!edge.isHighlighted)
			{
				edge.line.changeColor("color", edge.baseColor, null, null);
			}
		}
	}
	
	
	public boolean hasValidEntry(int id, CellID cell)
	{
		
		if(getEntry(id, cell).equals(invalidChar) || getEntry(id, cell) == null)
		{
			return false;
		}
		
		return true;
	}
	
	
	public boolean hasNode(int id)
	{
		return nodes.containsKey(id);
	}
	
	public boolean hasEdge(int from, int to)
	{
		if(hasNode(from))
		{
			NetzplanNode node = getNetzplanNode(from);
			
			return node.edges.containsKey(to);
		}
		
		return false;
	}
	
	public boolean hasNegativeProcessTime()
	{
		for(int nodeId: getAllNodes())
		{
			if(getProcessTime(nodeId) < 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasLoops()
	{
		if(getStartNodes().isEmpty() || getEndNodes().isEmpty())
		{
			return true;
		}
		
		LinkedList<Integer> visitedNodes = new LinkedList<Integer>();
		LinkedList<Integer> state = new LinkedList<Integer>();
		int currentId;
		
		for(Integer nodeId : getStartNodes())
		{
			currentId = nodeId;
			state.push(0);
			visitedNodes.push(nodeId);
			
			while(!state.isEmpty())
			{
				if(state.getLast() < getSuccessors(currentId).size())
				{
					
					currentId = getSuccessors(currentId).get(state.getLast());
					
					if(visitedNodes.contains(currentId))
					{
						return true;
					}else
					{
						visitedNodes.push(currentId);
						state.push(currentId);
					}
					
				}else
				{
					state.removeLast();
					
					if(!state.isEmpty())
					{
						state.push(state.removeLast()+1);
						visitedNodes.removeLast();
						currentId = visitedNodes.getLast();
					}
				}
			}
			
		}
		
		return false;
	}
	
	private String getEntry(int id, CellID cell)
	{
		NetzplanNode node = getNetzplanNode(id);
		
		if(node != null)
		{
			switch(cell)
			{
			case EarliestStartTime:
				return node.values.getElement(estRow, estColumn);
			case LatestStartTime:
				return node.values.getElement(lstRow, lstColumn);
			case EarliestEndTime:
				return node.values.getElement(eetRow, eetColumn);
			case LatestEndTime:
				return node.values.getElement(letRow, letColumn);
			case ProcessTime:
				return node.values.getElement(ptRow, ptColumn);
			case Name:
				return node.values.getElement(nameRow, nameColumn);
			}
		} 
		
		return invalidChar;
		
	}
	
	private boolean setEntry(int id, CellID cell, String entry)
	{
		NetzplanNode node = getNetzplanNode(id);
		
		if(node != null)
		{
			switch(cell)
			{
			case EarliestStartTime:
				node.values.put(estRow, estColumn, entry, null, null);
				return true;
			case LatestStartTime:
				node.values.put(lstRow, lstColumn, entry, null, null);
				return true;
			case EarliestEndTime:
				node.values.put(eetRow, eetColumn, entry, null, null);
				return true;
			case LatestEndTime:
				node.values.put(letRow, letColumn, entry, null, null);
				return true;
			case ProcessTime:
				node.values.put(ptRow, ptColumn, entry, null, null);
				return true;
			case Name:
				node.values.put(nameRow, nameColumn, entry, null, null);
				return true;
			}
		} 
		
		return false;
	}
	
	
	
	private NetzplanEdge getNetzplanEdge(int from, int to)
	{
		NetzplanNode node = getNetzplanNode(from);
		
		if(node != null)
		{
			if(node.edges.containsKey(to))
			{
				return node.edges.get(to);
			}
		}
		
		return null;
	}
	
	private NetzplanNode getNetzplanNode(int id)
	{
		if(nodes.containsKey(id))
		{
			return nodes.get(id);
		}else
		{
			return null;
		}
	}
	
	
	
	private boolean isEndNodeOfAnimalGraph(int id, Graph graph)
	{
		int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
		
		boolean isEndNode;
		
		isEndNode = true;
		for(int child = 0; child < graph.getSize(); child++)
		{
			if(adjacencyMatrix[id][child] != 0)
			{
				isEndNode = false;
				break;
			}
		}
		
		
		return isEndNode;
	}
	
	
	private void init(Graph graph)
	{
				
		// init cration of all nodes
		for(int i = 0; i < graph.getSize(); i++)
		{
			if(!isEndNodeOfAnimalGraph(i,graph))
			{
				NetzplanNode node = new NetzplanNode();

				graph.getNodeForIndex(i);
				node.values = createStringTable(((Coordinates)graph.getNodeForIndex(i)).getX(), ((Coordinates)graph.getNodeForIndex(i)).getY());
				node.id = i;
				nodes.put(i, node);
			}
			
		}
		
		//create all edges
		int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
		for(int to = 0; to < graph.getSize(); to++)
		{
			if(!isEndNodeOfAnimalGraph(to,graph))
			{
				for(int from = 0; from < graph.getSize(); from++)
				{
					
					if(!isEndNodeOfAnimalGraph(from,graph))
					{
						if(adjacencyMatrix[from][to] != 0)
						{
							NetzplanEdge edge = new NetzplanEdge();
							
							nodes.get(from).sucsessors.add(to);
							nodes.get(to).predecessors.add(from);
							
							Coordinates lineNodes[] = createEdgeCoordinates(nodes.get(from), nodes.get(to));
							edge.line = createLine(lineNodes);			
							edge.from = lineNodes[0];
							edge.to = lineNodes[1];
							
							nodes.get(from).edges.put(to,edge);
						}
					}		
				}
			}
			
		}
		
		// set starting values
		for(int i = 0; i < graph.getSize(); i++)
		{
			if(!isEndNodeOfAnimalGraph(i, graph))
			{
				this.setName(i, graph.getNodeLabel(i));
				
				if(!nodes.get(i).sucsessors.isEmpty())
				{
					this.setProcessTime(i, graph.getEdgeWeight(i, nodes.get(i).sucsessors.get(0)));
				}else
				{
					
					
				}
			}else
			{
				for(int from = 0; from < graph.getSize(); from++)
				{
					if(adjacencyMatrix[from][i] != 0)
					{
						this.setProcessTime(from,graph.getEdgeWeight(from, i));
					}
				}
			}
			
			
		}
		
		// calculates and set the min max Coordinate
		calculateMinMaxCoordinates();
	}
	
	
	private void calculateMinMaxCoordinates()
	{
		
		int height = getValueTableHeight();
		int width = getValueTableWidth();
		for(NetzplanNode node :  nodes.values())
		{
			Coordinates upperLefPosition =  (Coordinates)node.values.getUpperLeft();
			int tmpMinX = upperLefPosition.getX();
			int tmpMinY = upperLefPosition.getY();
			int tmpMaxX = upperLefPosition.getX()+width;
			int tmpMaxY = upperLefPosition.getY()+height;
			
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
	
	private Coordinates[] createEdgeCoordinates(NetzplanNode from, NetzplanNode to)
	{
		
		int additionalWidth = 8;
		int additionalHeight = 8;
		Coordinates fromUpperLeft = (Coordinates)from.values.getUpperLeft();
		Coordinates fromDownRight = new Coordinates(fromUpperLeft.getX()+getValueTableWidth(),
				fromUpperLeft.getY()+getValueTableHeight());
		
		Coordinates toUpperLeft = (Coordinates)to.values.getUpperLeft();
		Coordinates toDownRight = new Coordinates(toUpperLeft.getX()+getValueTableWidth(),
				toUpperLeft.getY()+getValueTableHeight());
		
		float fromCenterX = (float)(fromUpperLeft.getX()+fromDownRight.getX())/2f;
		float fromCenterY = (float)(fromUpperLeft.getY()+fromDownRight.getY())/2f;
		
		float toCenterX = (float)(toUpperLeft.getX()+toDownRight.getX())/2f;
		float toCenterY = (float)(toUpperLeft.getY()+toDownRight.getY())/2f;
		
		Coordinates pointA = createLineCoordinate(fromUpperLeft.getX(), fromUpperLeft.getY(), fromDownRight.getX(), fromDownRight.getY(),
				toCenterX-fromCenterX, toCenterY - fromCenterY);
		
		Coordinates pointB = createLineCoordinate(toUpperLeft.getX(), toUpperLeft.getY(), toDownRight.getX(), toDownRight.getY(),
				fromCenterX-toCenterX, fromCenterY - toCenterY);
		
		
		Coordinates result[] = {pointA, pointB};
		
		return result;
	}
	
	private int getValueTableWidth()
	{
		int additionalWidth = 8;
		int colloums = 3;
		
		return colloums * (cellWidth+additionalWidth);
		
	}
	
	private int getValueTableHeight()
	{
		int additionalHeight = 8;
		int rows = 2;
		
		return rows*(cellHeight+additionalHeight);
		
	}
	
	private Coordinates createLineCoordinate(float minX, float minY, float maxX, float maxY, float dirX, float dirY)
	{
		float centerX = (float)(minX+maxX)/2f;
		float centerY = (float)(minY+maxY)/2f;
		float lminX = minX-centerX;
		float lminY = minY-centerY;
		float lmaxX = maxX-centerX;
		float lmaxY = maxY-centerY;
		
		float nearestX = 0;
		float nearestY = 0;
		if(dirX < 0)
			nearestX = lminX;
		else
			nearestX = lmaxX;
		
		if(dirY < 0)
			nearestY = lminY;
		else
			nearestY = lmaxY;
		
		float x;
		float y;
		
		if(dirX == 0 || Math.abs(dirY/dirX) > Math.abs(nearestY/nearestX))
		{
			//y is bigger than x
			y = nearestY;
			x = (nearestY/dirY) * dirX;
		}else
		{
			// x is bigger than y
			y = (nearestX/dirX) * dirY;
			x = nearestX;
		}
		 y += centerY;
		 x += centerX;
		
		return new Coordinates((int)x, (int)y);
	}
	
	private Polyline createLine(Node lineNodes[])
	{
		
		PolylineProperties pp = createPolyLineProperties(null);
		return lang.newPolyline(lineNodes, "edge", null, pp);
	}
	
	private StringMatrix createStringTable(int x, int y)
	{
		AnimalStringMatrixGenerator matrixGenerator = new AnimalStringMatrixGenerator(lang);
		MatrixProperties matProp = createStringTableProperties();
		String[][] strValues = new String[2][3];
		for(int i = 0; i < 3; i++){
    		strValues[0][i] = invalidChar;
    		strValues[1][i] = invalidChar;
    	}
		
		StringMatrix smat = new StringMatrix(matrixGenerator,
				new Coordinates(x, y), strValues, "Values", null,
				matProp);

		return smat;
	}
	
	private MatrixProperties createStringTableProperties()
	{
		MatrixProperties matProp;
		if(defaultMatrixProperties != null)
		{
			matProp = defaultMatrixProperties;
		}else
		{
			matProp = new MatrixProperties();
		}
        
        matProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, getNodeDrawDepth());
        
        matProp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        //matProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        matProp.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, cellHeight);
        matProp.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, cellWidth);

        return matProp;
	}
	
	private PolylineProperties createPolyLineProperties(NetzplanEdge edge)
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
	
	private int getEdgeDrawDepth()
	{
		return this.drawDeph + edgeDrawDepth;
	}
	
	private int getNodeDrawDepth()
	{
		return this.drawDeph + nodeDrawDepth;
	}
	
	protected class NetzplanNode
	{
		int id;
		List<Integer> predecessors = new LinkedList<Integer>();
		List<Integer> sucsessors = new LinkedList<Integer>();	
		HashMap<Integer, NetzplanEdge> edges = new HashMap<Integer, NetzplanEdge>();
		StringMatrix values = null;
	}
	
	protected class NetzplanEdge
	{
		public Polyline line = null;
		public Color highlightColor = Color.GREEN;
		public Color baseColor = Color.BLACK;
		public boolean isHighlighted = false;
		public Coordinates from;
		public Coordinates to;
	}

}
