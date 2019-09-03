package gfgaa.gui.parser;


import algoanim.primitives.Graph;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * 
 * @author Nora Wester
 *
 */

public class GraphWriter{
	
	Graph graph;
	
	public GraphWriter(Graph graph){
		this.graph = graph;
	}
	
	public StringBuffer getScript() {


        StringBuffer buf = new StringBuffer("%graphscript\n\n");
        
        buf.append("graph ");
        
        int size = graph.getSize();
        buf.append(size);
        
        GraphProperties gP = graph.getProperties();
        boolean directed = (Boolean)gP.get(AnimationPropertiesKeys.DIRECTED_PROPERTY);
        if (directed) {
            buf.append(" directed");
        }

        boolean weighted = (Boolean)gP.get(AnimationPropertiesKeys.WEIGHTED_PROPERTY);
        if (weighted) {
            buf.append(" weighted\n\n");
        }
        
        if(!directed && !weighted) {
          buf.append("\n\n");
        }

        Node start = graph.getStartNode();
        Node end = graph.getTargetNode();
        
        buf.append("graphcoordinates at 0 0 \n\n");
        
    	if (start != null){
    		buf.append("startknoten "+ graph.getNodeLabel(start)+"\n");
    	}
    	if (end != null){
    		buf.append("zielknoten "+ graph.getNodeLabel(end)+"\n\n");
    	}
    	
    	StringBuffer edges = new StringBuffer();
    	
    	for(int i=0; i<size; i++){
    		Node node = graph.getNodeForIndex(i);
    		int x = 0;
    		int y = 0;
    		if(node instanceof Coordinates){
    			x = ((Coordinates)node).getX();
    			y = ((Coordinates)node).getY();
    		}else{
    			if(node instanceof Offset){
    				x = ((Offset)node).getX();
    				y = ((Offset)node).getY();
    			}
    		}
    		
    		String nodeName = graph.getNodeLabel(node);
    		
    		buf.append("node " + nodeName +" at "+ x + " " + y + "\n");
    		
    		int[] edgeArray = graph.getEdgesForNode(node);
    		
    		if(!directed){
    			for(int j=i; j<edgeArray.length; j++){
    				int value = edgeArray[j];
    				if(value != 0){
    					String targetName = graph.getNodeLabel(j);
    					edges.append("edge " + nodeName + " " + targetName);
    					if(weighted)
    						edges.append(" weight " + value);
    					
    					edges.append("\n");
    				}
    			}
    		}else{
    			for(int j=0; j<edgeArray.length; j++){
    				int value = edgeArray[j];
    				if(value != 0){
    					String targetName = graph.getNodeLabel(j);
    					edges.append("edge " + nodeName + " " + targetName);
    					if(weighted)
    						edges.append(" weight " + value);
    					
    					edges.append("\n");
    				}
    			}
    		}
    	}
    	
        
        buf.append("\n");
        buf.append(edges);
        buf.append("\n");
       
        
        return buf;
    }
	
}