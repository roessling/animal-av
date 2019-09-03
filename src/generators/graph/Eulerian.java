/*
 * Eulerian.java
 * Joel Benedikt Koschier, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Eulerian implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCodeProps;
    private TextProperties titleProperties;
    private GraphProperties graphProps;
    private ArrayProperties pathProps;
    private Graph graph;
    private ArrayList<Text> paths;
    private ArrayList<StringArray> pathDisplays;
    private Rect graphContainer;
    private Text iterationText;
    
    
    private SourceCode sc;
    
    private void createTitle(){
		lang.newText(new Coordinates(20, 30), "Eulerian Cycle", "TitleText", null, titleProperties);
    	lang.newRect(
    			new Offset(-5, -5, "TitleText", AnimalScript.DIRECTION_NW), 
    			new Offset(5, 5, "TitleText", AnimalScript.DIRECTION_SE), 
    			"", null);
    }
    
    public void init(){
    	lang = new AnimalScript("Eulerian Cycle", "Joel Benedikt Koschier", 1200, 600);
    	lang.setStepMode(true);
    	paths = new ArrayList<Text>();
    	pathDisplays = new ArrayList<StringArray>();
    }
    
    public Eulerian() {
    	
    }
    
    private void createSourceCode(){
		sc = lang.newSourceCode(new Coordinates(40, 50), "sourceCode", null, sourceCodeProps);
		
		sc.addCodeLine("1. Initialize a new path p containing only the start node s", null, 0, null);
		sc.addCodeLine("2. Set the active node x equal to s", null, 0, null);
		sc.addCodeLine("3. While there are unused edges adjacent x:", null, 0, null);
		sc.addCodeLine("1. Choose one such edge (x,y)", null, 1, null);
		sc.addCodeLine("2. Set x equal to y", null, 1, null);
		sc.addCodeLine("3. Append y to p", null, 1, null);
		sc.addCodeLine("4. Remove (x,y) from the graph", null, 1, null);
		sc.addCodeLine("4. If x does not equal s, then this Graph does not contain a eulerian cycle", null, 0, null);
		sc.addCodeLine("5. Otherwise: While there are nodes v with unused leaving edges on the path:", null, 0, null);
		sc.addCodeLine("1. Call the procedure recursively with v as new s, giving path p'", null, 1, null);
		sc.addCodeLine("2. Replace v in p by p'", null, 1, null);
		sc.addCodeLine("3. RETURN", null, 1, null);
	}
    
    
	private void findCycle(int startNodeIndex){
		int nodeCount = graph.getSize();
		assert(startNodeIndex < nodeCount && startNodeIndex >= 0);
		
		//Transform adjacencyMatrix into a AdjacencyList where we can easily remove edges
		AdjacencyList[] adjacencyList = new AdjacencyList[nodeCount];
		for(int i = 0; i < nodeCount; i++){
			adjacencyList[i] = new AdjacencyList();
			int[] edges = graph.getEdgesForNode(i);
			for(int j = 0; j < nodeCount; j++){
				if(edges[j] > 0)
					adjacencyList[i].add(j);
			}
		}
		
		ArrayList<Integer> path;
		iterationText = lang.newText(new Offset(15, -20, graph, AnimalScript.DIRECTION_NE), "Path in each recursive Iteration:", "", null);
		try{
			path = findCycleRec(startNodeIndex, adjacencyList, 0);

			lang.nextStep();
			graph.hide();
			graphContainer.hide();
			sc.hide();
			iterationText.hide();
			for(Text pathText : paths){
				pathText.hide();
			}
			lang.newText(new Coordinates(40, 70), "Conclusion:", "succ1", null, titleProperties);
			lang.newText(new Offset(0, 25, "succ1", AnimalScript.DIRECTION_NW),
			        "This graph does contain a eulerian cycle:", "succ2", null, titleProperties);
			lang.newText(new Offset(0, 25, "succ2", AnimalScript.DIRECTION_NW),
			        pathToString(path), "succ3", null, titleProperties);
			
		}
		catch(Exception e){
			lang.nextStep();
			graph.hide();
			sc.hide();
			for(Text pathText : paths){
				pathText.hide();
			}
			iterationText.hide();
			lang.newText(new Coordinates(40, 70), "Conclusion:", "fail1", null, titleProperties);
			lang.newText(new Offset(0, 25, "fail1", AnimalScript.DIRECTION_NW),
			        "This graph does not contain a eulerian cycle.", "fail2", null, titleProperties);
			lang.newText(new Offset(0, 25, "fail2", AnimalScript.DIRECTION_NW), 
					"A graph only contains a eulerian cycle iff:", "fail3", null, titleProperties);
			lang.newText(new Offset(0, 25, "fail3", AnimalScript.DIRECTION_NW), 
					"    - every node has a positive even number of edges", "fail4", null, titleProperties);
			lang.newText(new Offset(0, 25, "fail4", AnimalScript.DIRECTION_NW), 
					"    - the graph is connected", "fail5", null, titleProperties);
			
			
			
			lang.nextStep();
		}
	}
	
	private String pathToString(ArrayList<Integer> path){
		StringBuilder pathString = new StringBuilder();
		
		pathString.append("Path p");
		
		
		pathString.append(" = ");
		if(path.size() == 0)
			pathString.append("\u2205");
		else{
			pathString.append("{ ");
			for(int i = 0; i < path.size() - 1; i++){
				
				pathString.append(graph.getNodeLabel(path.get(i)));
				pathString.append(" -> ");
			}
			pathString.append(graph.getNodeLabel(path.get(path.size() - 1)));
			pathString.append(" }");
		}

		return pathString.toString();
	}
	
	private ArrayList<Integer> findCycleRec(int startNodeIndex, AdjacencyList[] adjacencyList, int depth) throws Exception{
		//1.Init Path containing startnode
		lang.nextStep();
		sc.unhighlight(9); //necessary if this function was called recursively
		sc.unhighlight(11);
		sc.highlight(0);
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(startNodeIndex);
		//Text pathText = lang.newText(new Coordinates(600, depth * 20 + 50), pathToString(path), "Path"+depth, null);
		/*
		 * HIGHLY EXPERIMENTAL
		 */
		
		String[] pathText = new String[path.size()];
		for(int i = 0; i < pathText.length; i++){
			pathText[i] = graph.getNodeLabel(path.get(i));
		}
		
		StringArray pathToDisplay;
		
		if(depth == 0){
			
			pathToDisplay = lang.newStringArray(
					new Offset(15, 0, "graph", AnimalScript.DIRECTION_NE), 
					pathText, "path0", null, pathProps
					);
		}else{
			pathToDisplay = lang.newStringArray(
					new Offset(0, 10, pathDisplays.get(depth-1), AnimalScript.DIRECTION_SW), 
					pathText, "path" + depth, null, pathProps
					);
		}
		
		if(depth < pathDisplays.size()){
			pathDisplays.remove(depth);
			pathDisplays.add(depth, pathToDisplay);
		}else{
			pathDisplays.add(pathToDisplay);
		}
		
		/*
		 * END
		 */
		//paths.add(pathText);
		//2.Set active Node equal to startnode
		lang.nextStep();
		sc.unhighlight(0);
		sc.highlight(1);
		graph.highlightNode(startNodeIndex, null, null);
		int lastAddedNode = startNodeIndex;
		
		
		//3.While there are unused edges...
		//as long as we can move through the Graph lets do that!
		lang.nextStep();
		sc.unhighlight(1);
		sc.highlight(2);
		lang.nextStep();
		sc.unhighlight(2);
		while(!adjacencyList[lastAddedNode].isEmpty()){
			//3.1 choose such a edge
			int nextNode = adjacencyList[lastAddedNode].get(0);
			sc.highlight(3);
			graph.highlightEdge(lastAddedNode, nextNode, null, null);
			graph.highlightEdge(nextNode, lastAddedNode, null, null);
			
			//3.4 Set x equal to new Node
			lang.nextStep();
			sc.unhighlight(3);
			sc.highlight(4);
			graph.unhighlightNode(lastAddedNode, null, null);
			graph.highlightNode(nextNode, null, null);
			removeEdgeFromList(adjacencyList, lastAddedNode, nextNode);
			
			//3.3 Append Node to path
			lang.nextStep();
			sc.unhighlight(4);
			sc.highlight(5);
			path.add(nextNode);
			//pathText.setText(pathToString(path), null, null);
			
			pathText = new String[path.size()];
			for(int i = 0; i < pathText.length; i++){
				pathText[i] = graph.getNodeLabel(path.get(i));
			}
			pathToDisplay.hide();
			pathDisplays.remove(pathDisplays.size()-1);
			if(depth == 0){
				pathToDisplay = lang.newStringArray(
						new Offset(15, 0, "graph", AnimalScript.DIRECTION_NE), 
						pathText, "path0", null, pathProps
						);
			}else{
				pathToDisplay = lang.newStringArray(
						new Offset(0, 10, pathDisplays.get(depth-1), AnimalScript.DIRECTION_SW), 
						pathText, "path" + depth, null, pathProps
						);
			}
			pathDisplays.add(pathToDisplay);
			
			
			//3.2 remove edge from graph
			lang.nextStep();
			sc.unhighlight(5);
			sc.highlight(6);
			graph.hideEdge(nextNode, lastAddedNode, null, null);
			graph.hideEdge(lastAddedNode, nextNode, null, null);
			lastAddedNode = nextNode;
			
			//prep animation of next step
			lang.nextStep();
			sc.unhighlight(6);
		}
		
		//4. check if an eulerian cycle exists
		sc.highlight(7);
		if(lastAddedNode != startNodeIndex)
			throw new Exception("No Path found");
		
		//5. While there are nodes v with unused leaving edges
		lang.nextStep();
		sc.unhighlight(7);
		sc.highlight(8);
		lang.nextStep();
		sc.unhighlight(8);
		for(int i = 0; i < path.size(); i++){
			Integer node = path.get(i);
			if(!adjacencyList[node].isEmpty()){
				//5.1 call procedure recursively with v as new s
				sc.highlight(9);
				graph.unhighlightNode(lastAddedNode, null, null);
				graph.highlightNode(node, null, null);
				lastAddedNode = node;
				ArrayList<Integer> subPath = findCycleRec(path.get(i), adjacencyList, depth + 1);
				
				//5.2 Replace v in p by p'
				lang.nextStep();
				sc.highlight(10);
				path.remove(i);
				path.addAll(i,subPath);
				//pathText.setText(pathToString(path), null, null);
				pathText = new String[path.size()];
				for(int j = 0; j < pathText.length; j++){
					pathText[j] = graph.getNodeLabel(path.get(j));
				}
				pathToDisplay.hide();
				pathDisplays.remove(pathDisplays.size()-1);
				if(depth == 0){
					pathToDisplay = lang.newStringArray(
							new Offset(15, 0, "graph", AnimalScript.DIRECTION_NE), 
							pathText, "path0", null, pathProps
							);
				}else{
					pathToDisplay = lang.newStringArray(
							new Offset(0, 10, pathDisplays.get(depth-1), AnimalScript.DIRECTION_SW), 
							pathText, "path" + depth, null, pathProps
							);
				}
				pathDisplays.add(pathToDisplay);
				lang.nextStep();
			}
		}
		lang.nextStep();
		sc.unhighlight(9);
		sc.unhighlight(10);
		sc.highlight(11);
		//pathText.hide();
		pathToDisplay.hide();
		lang.nextStep();
		sc.unhighlight(11);
		return path;
	}
	
	private void removeEdgeFromList(AdjacencyList[] adjacencyList, int from, int to){
		adjacencyList[from].remove((Object)to);
		adjacencyList[to].remove((Object)from);
	}
    
	@SuppressWarnings("serial")
	class AdjacencyList extends ArrayList<Integer>{}
	
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("Source Code");
        titleProperties = (TextProperties)props.getPropertiesByName("Title");
        graph = (Graph) primitives.get("Graph");
        graphProps = (GraphProperties)props.getPropertiesByName("Graph");
        pathProps = (ArrayProperties) props.getPropertiesByName("Path");
        
        init();
        
        int size = graph.getSize();
        Node[] nodes = new Node[size];
        String[] nodeLabels = new String[size];
        for (int i = 0; i < size; i++) {
          nodes[i] = graph.getNode(i);
          nodeLabels[i] = graph.getNodeLabel(i);
        }
        
        graph = lang.newGraph("graph", graph.getAdjacencyMatrix(),
            nodes, nodeLabels, graph.getDisplayOptions(), graphProps);
        
        graphContainer = lang.newRect(
    			new Offset(-5, -5, "graph", AnimalScript.DIRECTION_NW), 
    			new Offset(5, 5, "graph", AnimalScript.DIRECTION_SE), 
    			"", null);
        createTitle();
        createSourceCode();
        
        findCycle(0);

        
        return lang.toString();
    }

    public String getName() {
        return "Eulerian Cycle";
    }

    public String getAlgorithmName() {
        return "Hierholzer's Algorithm";
    }

    public String getAnimationAuthor() {
        return "Joel Benedikt Koschier";
    }

    public String getDescription(){
        return "An eulerian cycle is a path through a graph, which starts and ends"
 +"\n"
 +"at the same node and contains all edges of the graph on it's way."
 +"\n"
 +"This classical implementation will find a possible eulerian"
 +"\n"
 +"cycle if a eulerian cycle exists. If that's not the case, this"
 +"\n"
 +"algorithm will indicate that no eulerian cycle exists."
 +"\n"
 +"This implementation assumes, that the graph is connected, which"
 +"\n"
 +"means any two nodes are always connected by a path.";
    }

    public String getCodeExample(){
        return "1. Initialize a new path p containing only the start node s"
 +"\n"
 +"2. Set the active node x equal to s"
 +"\n"
 +"3. While there are unused edges adjacent x:"
 +"\n"
 +"    1. Choose one such edge (x,y)"
 +"\n"
 +"    2. Set x equal to y "
 +"\n"
 +"    3. Append y to p"
 +"\n"
 +"    4. Remove (x,y) from the graph "
 +"\n"
 +"4. If x does not equal s, then this Graph does not contain a eulerian cycle "
 +"\n"
 +"5. Otherwise: While there are nodes v with unused leaving edges on the path: "
 +"\n"
 +"    1. Call the procedure recursively with v as new s, giving path p'"
 +"\n"
 +"    2. Replace v in p by p'";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}