package generators.graph.prim;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.EdgeElem;
import generators.helpers.NodeElem;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;



public class MyPrim extends AnnotatedAlgorithm implements Generator {	
	//@author Stephan Arneth <arneth@rbg.informatik.tu-darmstadt.de>
	
	ArrayList<NodeElem> nodeList;
	Graph graph;
	
	public MyPrim() {
		
	}
	
	
	public void run(int[][] nodescoord, int[] edgeWeights) {
		
		/*
		Node node1 = new Coordinates(20,30);
		Node node2 = new Coordinates(20, 500);
		Node node3 = new Coordinates(350, 80);
		Node node4 = new Coordinates(200, 600);
		Node node5 = new Coordinates(500, 200);
		Node node6 = new Coordinates(500, 500);
		*/
		Node node1 = new Coordinates(nodescoord[0][0], nodescoord[0][1]);
		Node node2 = new Coordinates(nodescoord[1][0], nodescoord[1][1]);
		Node node3 = new Coordinates(nodescoord[2][0], nodescoord[2][1]);
		Node node4 = new Coordinates(nodescoord[3][0], nodescoord[3][1]);
		Node node5 = new Coordinates(nodescoord[4][0], nodescoord[4][1]);
		Node node6 = new Coordinates(nodescoord[5][0], nodescoord[5][1]);

		nodeList = new ArrayList<NodeElem>();
		ArrayList<EdgeElem> edgeList = new ArrayList<EdgeElem>();
		
		NodeElem elem0 = new NodeElem(0, node1);
		NodeElem elem1 = new NodeElem(1, node2);
		NodeElem elem2 = new NodeElem(2, node3);
		NodeElem elem3 = new NodeElem(3, node4);
		NodeElem elem4 = new NodeElem(4, node5);
		NodeElem elem5 = new NodeElem(5, node6);
		
		nodeList.add(elem0);
		nodeList.add(elem1);
		nodeList.add(elem2);
		nodeList.add(elem3);
		nodeList.add(elem4);
		nodeList.add(elem5);
		
		Node[] graphNodes = {node1, node2, node3, node4, node5, node6};
		String[] graphNames = {"0", "1", "2", "3", "4", "5"};
		int[][] adjaMatrix = new int[6][6];
		adjaMatrix[0][1] = edgeWeights[0];
		EdgeElem edge0 = new EdgeElem(0, 1, 0, edgeWeights[0]);
		adjaMatrix[0][2] = edgeWeights[1];
		EdgeElem edge1 = new EdgeElem(0, 2, 1, edgeWeights[1]);
		adjaMatrix[0][3] = edgeWeights[2];
		EdgeElem edge2 = new EdgeElem(0, 3, 2, edgeWeights[2]);
		adjaMatrix[0][4] = edgeWeights[3];
		EdgeElem edge3 = new EdgeElem(0, 4, 3, edgeWeights[3]); 
		adjaMatrix[0][5] = edgeWeights[4];
		EdgeElem edge4 = new EdgeElem(0, 5, 4, edgeWeights[4]);
		adjaMatrix[1][2] = edgeWeights[5];
		EdgeElem edge5 = new EdgeElem(1, 2, 5, edgeWeights[5]);
		adjaMatrix[1][3] = edgeWeights[6];
		EdgeElem edge6 = new EdgeElem(1, 3, 6, edgeWeights[6]);
		adjaMatrix[2][3] = edgeWeights[7];
		EdgeElem edge7 = new EdgeElem(2, 3, 7, edgeWeights[7]);
		adjaMatrix[3][4] = edgeWeights[8];
		EdgeElem edge8 = new EdgeElem(3, 4, 8, edgeWeights[8]);
		adjaMatrix[4][5] = edgeWeights[9];
		EdgeElem edge9 = new EdgeElem(4, 5, 9, edgeWeights[9]);
		
		edgeList.add(edge0);
		edgeList.add(edge1);
		edgeList.add(edge2);
		edgeList.add(edge3);
		edgeList.add(edge4);
		edgeList.add(edge5);
		edgeList.add(edge6);
		edgeList.add(edge7);
		edgeList.add(edge8);
		edgeList.add(edge9);
		
		
		
		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
		graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
		graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
		graph = lang.newGraph("graph", adjaMatrix, graphNodes, graphNames, null, graphProps);
		
		NodeElem startKnoten = elem0;
		graph.setStartNode(node1);
		//NodeElem zielknoten = elem5;
		graph.setTargetNode(node6);
		
		lang.nextStep();
		
		// First node of MST is the startnode
		ArrayList<NodeElem> primNodes = new ArrayList<NodeElem>();
		exec("initprimnodes");
		lang.nextStep();
		primNodes.add(startKnoten);
		ArrayList<EdgeElem> primEdges = new ArrayList<EdgeElem>();
		exec("initprimedges");
		lang.nextStep();
		//ArrayList<Node> primnodes = new ArrayList<Node>();
		//primnodes.add(graph.getStartKnoten());
		
		graph.highlightNode(startKnoten.getnodeid(), null, new MsTiming(300));
		
		exec("beginwhile1");
		lang.nextStep();
		
		while(primNodes.size() < graph.getSize()) {
			
			exec("initminlength");
			int minLength = Integer.MAX_VALUE;
			lang.nextStep();
			
			exec("initbestoutgoingedge");
			EdgeElem bestOutgoingEdge = null;
			lang.nextStep();
			exec("initbestneighbornode");
			NodeElem bestNeighborNode = null;
			lang.nextStep();
			exec("initprimnodeit");
			Iterator<NodeElem> primNodeIt = primNodes.iterator();
			lang.nextStep();
			
			while(primNodeIt.hasNext()) {
				
				exec("getselectednode");
				NodeElem selectedNode = primNodeIt.next();
				lang.nextStep();
				graph.highlightNode(selectedNode.getnodeid(), null, new MsTiming(300));
				
				exec("getneighborID");
				int neighborID = getNewNeighborID(edgeList, primNodes, selectedNode);
				lang.nextStep();
				exec("initif1");
				if(neighborID != -1) {
					
					exec("getNodeElem");
					NodeElem neighborNode = getNodeByID(neighborID);
					lang.nextStep();
					
					exec("getbestoutgoingedge");
					EdgeElem bestEdge = getMinOutgoingEdge(edgeList, selectedNode.getnodeid(), neighborID);
					lang.nextStep();
					
					exec("initif2");
					if(bestEdge.getweight() < minLength)
					{
						exec("setbestneighbor");
						bestNeighborNode = neighborNode;
						lang.nextStep();
						exec("setbestedge");
						bestOutgoingEdge = bestEdge;
						lang.nextStep();
						exec("setminlength");
						minLength = bestEdge.getweight();
						lang.nextStep();
					}
					
				}
				
				graph.unhighlightNode(selectedNode.getnodeid(), null, new MsTiming(300));
				lang.nextStep();
			}
			
			graph.highlightEdge(bestOutgoingEdge.getsource(), bestOutgoingEdge.gettarget(), null, new MsTiming(300));
			
			exec("addbestneighbor");
			primNodes.add(bestNeighborNode);
			lang.nextStep();
			exec("addbestedge");
			primEdges.add(bestOutgoingEdge);
			lang.nextStep();
			
			//System.out.println("Added edge to MST: " + bestoutgoingedge.getedgeid() + " from " + bestoutgoingedge.getsource() + " to " + bestoutgoingedge.gettarget());
		}
		
		exec("endwhile1");
		lang.nextStep();
		exec("endfunction");
		lang.nextStep();

	}
	
	public EdgeElem getMinOutgoingEdge(ArrayList<EdgeElem> edgelist, int source, int target) {
		Iterator<EdgeElem> edgeit = edgelist.iterator();
		
		int minvalue = Integer.MAX_VALUE;
		EdgeElem bestcandidate = null;
		
		while(edgeit.hasNext()) {
			EdgeElem current = edgeit.next();
			
			if(current.getsource() == source && current.gettarget() == target 
					&& current.getweight() < minvalue) {
				minvalue = current.getweight();
				bestcandidate = current;
			}
			
		}
		
		return bestcandidate; 
	}
	
	public NodeElem getNodeByID(int searchID) {
		Iterator<NodeElem> nodeit = nodeList.iterator();
		
		while(nodeit.hasNext()) {
			NodeElem currentnode = nodeit.next();
			if(currentnode.getnodeid() == searchID)
				return currentnode;
		}
		
		return null;
	}
	
	public EdgeElem getMinOutgoingEdge(ArrayList<EdgeElem> edgelist, ArrayList<NodeElem> primnodeslist, int source) {
		Iterator<EdgeElem> edgeit = edgelist.iterator();
		
		int minvalue = Integer.MAX_VALUE;
		EdgeElem bestcandidate = null;
		
		while(edgeit.hasNext()) {
			EdgeElem current = edgeit.next();
			
			if(current.getsource() == source && current.getweight() < minvalue && 
					(!(primnodeslist.contains(getNodeByID(current.gettarget()))))) {
				minvalue = current.getweight();
				bestcandidate = current;
			}
			
		}
		
		return bestcandidate; 
	}
	
	public int getNewNeighborID(ArrayList<EdgeElem> edgelist, ArrayList<NodeElem> primnodeslist, NodeElem rootnode) {
		// also checks for already included nodes
		int sourceID = rootnode.getnodeid();
		
		EdgeElem bestedge = getMinOutgoingEdge(edgelist, primnodeslist, sourceID);
		
		if(bestedge != null) {
			return bestedge.gettarget();
		}
		
		return -1;
		
	}

	public boolean alreadyVisited(Node[] nodelist, int nodenr) {
		for(int k=0; k<nodelist.length; k++) {
			if((nodenr == k)) {
				return false;
			}
		}
		
			return true;
	}


	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		init();
		int[][] nodescoord = (int[][]) primitives.get("nodescoord");
		int[] edgeweights = (int[]) primitives.get("edgeweights");
		
		// GraphProperties possible? Not in PropertiesGUI listed....
		
		run(nodescoord, edgeweights);
		return lang.toString();
	}


	@Override
	public String getAlgorithmName() {
		return "Prim";
	}


	@Override
	public String getAnimationAuthor() {
		return "Stephan Arneth";
	}


	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}


	@Override
	public String getDescription() {
		return "Illustration of the Prim-Algorithm. Step-by-Step with Source-Code. The Prim-Algorithm uses an bottom-up approach. ";
	}


	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}


	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}


	@Override
	public String getName() {
		return "Prim-Algorithm";
	}


	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}


	@Override
	public void init() {
		super.init();
		
		SourceCodeProperties sourceProps = new SourceCodeProperties();
		sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
		sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 11));
		sourceCode = lang.newSourceCode(new Coordinates(500, 40), "Source", null, sourceProps);
		
		parse();
	}

	@Override
	public String getAnnotatedSrc() {
		return "prim (String[] graphEdges, String[] graphNodes) { @label(\"call\") \n" +
		"  String[] primNodes = {node1};    @label(\"initprimnodes\") \n" + 
		"  String[] primEdges = null;    @label(\"initprimedges\") \n" + 
		//"String[] queue = graphnodes; @label(\"initqueue\") \n" + 
		"  while (primNodes.length() &lt; graphNodes.length() {       @label(\"beginwhile1\") \n" +
		"    int minLength = Integer.MAX_VALUE;     @label(\"initminlength\") \n" +
		"    EdgeElem bestOutgoingEdge = null;     @label(\"initbestoutgoingedge\") \n" +
		"    NodeElem bestNeighborNode = null;     @label(\"initbestneighbornode\") \n" +
		"    Iterator&lt;NodeElem$gt; primNodeIt = primNodes.iterator();     @label(\"initprimnodeit\") \n" +
		"    while (primNodeIt.hasNext()) {     @label(\"initwhile2\") \n" +
		"      NodeElem selectedNode = primNodeIt.next();      @label(\"getselectednode\") \n" +
		"      int neighborID = getNewNeighborID(edgeList, primNodes, selectedNode);      @label(\"getneighborID\") \n" +
		"      if (neighborID != -1) {      @label(\"initif1\") \n" +
		"        NodeElem neighborNode = getNodeByID(neighborID);        @label(\"getNodeElem\") \n" +
		"        EdgeElem bestEdge = getMinOutgoingEdge(edgeList, selectedNode.getNodeId(), neighborID);        @label(\"getbestoutgoingedge\") \n" +
		"        if (bestEdge.getWeight() &lt; minLength) {        @label(\"initif2\") \n" +
		"          bestNeighborNode = neighborNode;         @label(\"setbestneighbor\") \n" +
		"          bestOutgoingEdge = bestEdge;         @label(\"setbestedge\") \n" +
		"          minLength = bestEdge.getWeight();         @label(\"setminlength\") \n" +
		"        }        @label(\"endif2\") \n" +
		"      }      @label(\"endif1\") \n" +
		"    }    @label(\"endwhile2\")  \n" +
		"    primNodes.add(bestNeighborNode);     @label(\"addbestneighbor\") \n" +
		"    primEdges.add(bestOutgoingEdge);     @label(\"addbestedge\") \n" +
		"  } @label(\"endwhile1\")  \n" + 
		"} @label(\"endfunction\")\n";
	}
	
}
