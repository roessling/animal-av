package generators.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import algoanim.util.Coordinates;
import generators.graph.utils.PREdge;
import generators.graph.utils.PRGraph;
import generators.graph.utils.PRNode;

public class PushRelabelAlgorithm {

	public static void main(String[] args) {
		
		parseGraph("%graphscript\n"
				+ "\n"
				+ "\n"
				+ "graph 6  directed weighted\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "graphcoordinates at 100 300\n"
				+ "\n"
				+ "startknoten S\n"
				+ "zielknoten T\n"
				+ "\n"
				+ "\n"
				+ "node S at 100  300\n"
				+ "node A at 200 200\n"
				+ "node B at 200 400\n"
				+ "node C at 300 200\n"
				+ "node D at 300 400\n"
				+ "node T at 400 300\n"
				+ "\n"
				+ "edge S A weight 1\n"
				+ "edge S B weight 2\n"
				+ "edge S D weight 1\n"
				+ "edge B C weight 1\n"
				+ "edge D B weight 1\n"
				+ "edge C D weight 2\n"
				+ "edge D A weight 1\n"
				+ "edge A C weight 1\n"
				+ "edge A T weight 1\n"
				+ "edge C T weight 3\n");
		
		PRGraph graph = loadExampleGraph2();		
		
		Integer flow = graph.calcMaxFlow();
		System.out.println(flow);
	}

	/**
	 * parsing the graph with the graph script
	 * 
	 * @param graphScript
	 * @return
	 */
	public static PRGraph parseGraph(String graphScript) {
		
		int pos = graphScript.indexOf("%graphscript");
		if (pos == -1){
			throw new IllegalArgumentException("No %graphscript");
		}
		graphScript = graphScript.substring(pos+12);
		
		pos = graphScript.indexOf("graph");
		if (pos == -1){
			throw new IllegalArgumentException("No %graphscript");
		}

		graphScript = graphScript.substring(pos+6);
		pos = graphScript.indexOf(" ");
		
		int numberOfNodes = Integer.valueOf(graphScript.substring(0, pos));
		
		System.out.println(numberOfNodes);
		
		graphScript = graphScript.substring(pos+1);
		
		pos = graphScript.indexOf("directed weighted");
		if (pos == -1){
			throw new IllegalArgumentException("Must be directed weighted graph");
		}
		
		graphScript = graphScript.substring(pos+17);
		
		pos = graphScript.indexOf("graphcoordinates at");
		if (pos == -1){
			throw new IllegalArgumentException("No Graph coordinates");
		}
		graphScript = graphScript.substring(pos+20);
		pos = graphScript.indexOf(" ");
		int graphCoorx = Integer.valueOf(graphScript.substring(0, pos));
		
		graphScript = graphScript.substring(pos+1);
		pos = graphScript.indexOf("\n");
		
		int graphCoory = Integer.valueOf(graphScript.substring(0, pos));
		graphScript = graphScript.substring(pos);
		
		pos = graphScript.indexOf("startknoten");
		if (pos == -1){
			throw new IllegalArgumentException("No starting node");
		}
		graphScript = graphScript.substring(pos+12);
		pos = graphScript.indexOf("\n");
		String startnode = String.valueOf(graphScript.substring(0, pos).trim());
		graphScript = graphScript.substring(pos);
		
		pos = graphScript.indexOf("zielknoten");
		if (pos == -1){
			throw new IllegalArgumentException("No end node");
		}
		graphScript = graphScript.substring(pos+11);
		pos = graphScript.indexOf("\n");
		String endnode = String.valueOf(graphScript.substring(0, pos).trim());
		graphScript = graphScript.substring(pos);
		
		Coordinates[] positions = new Coordinates[numberOfNodes];
		HashMap<String,PRNode> nodes_map = new HashMap<String,PRNode>();
		List<PRNode> nodes = new LinkedList<PRNode>();
		for (int i = 1; i <= numberOfNodes; i++) {
			pos = graphScript.indexOf("node");
			if (pos == -1){
				throw new IllegalArgumentException("error with the nodes");
			}
			graphScript = graphScript.substring(pos+5);
			pos = graphScript.indexOf(" ");
			String name = String.valueOf(graphScript.substring(0, pos)).toLowerCase().trim();

			if (name.toLowerCase().equals(startnode.toLowerCase())){
				PRNode node = new PRNode(name,"start");
				nodes_map.put(name,node);
				nodes.add(node);
			}else if (name.toLowerCase().equals(endnode.toLowerCase())){
				PRNode node = new PRNode(name,"end");
				nodes_map.put(name,node);
				nodes.add(node);
			}else{
				PRNode node = new PRNode(name);
				nodes_map.put(name,node);
				nodes.add(node);
			}
			
			graphScript = graphScript.substring(pos+4);
			pos = graphScript.indexOf(" ");
			
			int x = Integer.valueOf(graphScript.substring(0,pos).trim());
			
			graphScript = graphScript.substring(pos+1);
			pos = graphScript.indexOf("\n");

			int y = Integer.valueOf(graphScript.substring(0,pos).trim());
			
			positions[i-1] = new Coordinates(x, y);
		}
		
		List<PREdge> edges = new ArrayList<PREdge>();
		boolean cond = true;
		
		do {
			pos = graphScript.indexOf("edge");
			graphScript = graphScript.substring(pos+5);
			
			pos = graphScript.indexOf(" ");
			String start = graphScript.substring(0,pos);
			
			graphScript = graphScript.substring(pos+1);
			
			pos = graphScript.indexOf(" ");
			String end = graphScript.substring(0,pos);
			
			graphScript = graphScript.substring(pos+1);
			
			if (graphScript.indexOf("weight")==-1){
				throw new IllegalArgumentException("no weight key word");
			}
			
			pos = graphScript.indexOf("weight");
			graphScript = graphScript.substring(pos+7);

			pos = graphScript.indexOf("\n");
			
			Integer weight = Integer.valueOf(graphScript.substring(0,pos).trim());
			
			PREdge edge = new PREdge(nodes_map.get(start.toLowerCase()), nodes_map.get(end.toLowerCase()),weight);
			
			edges.add(edge);
			
			graphScript = graphScript.substring(pos+1);
			
			cond = graphScript.indexOf("edge") != -1;
		} while (cond);

		PRGraph graph = new PRGraph(nodes, edges);

		return null;
	}

	/**
	 * example 1
	 * 
	 * @return
	 */
	public static PRGraph loadExampleGraph1() {
		PRNode s = new PRNode("s", "start");
		PRNode a = new PRNode("a");
		PRNode b = new PRNode("b");
		PRNode c = new PRNode("c");
		PRNode d = new PRNode("d");
		PRNode t = new PRNode("t", "end");

		List<PRNode> nodes = new ArrayList<PRNode>();
		nodes.add(s);
		nodes.add(a);
		nodes.add(b);
		nodes.add(c);
		nodes.add(d);
		nodes.add(t);

		PREdge sa = new PREdge(s, a, 1);
		PREdge sb = new PREdge(s, b, 2);
		PREdge sd = new PREdge(s, d, 1);
		PREdge bc = new PREdge(b, c, 1);
		PREdge db = new PREdge(d, b, 1);
		PREdge cd = new PREdge(c, d, 2);
		PREdge da = new PREdge(d, a, 1);
		PREdge ac = new PREdge(a, c, 1);
		PREdge at = new PREdge(a, t, 1);
		PREdge ct = new PREdge(c, t, 3);

		List<PREdge> edges = new ArrayList<PREdge>();
		edges.add(sa);
		edges.add(sb);
		edges.add(sd);
		edges.add(bc);
		edges.add(db);
		edges.add(cd);
		edges.add(da);
		edges.add(ac);
		edges.add(at);
		edges.add(ct);

		return new PRGraph(nodes, edges);
	}

	/**
	 * example 2
	 * 
	 * @return
	 */
	public static PRGraph loadExampleGraph2() {
		PRNode s = new PRNode("s", "start");
		PRNode a = new PRNode("a");
		PRNode b = new PRNode("b");
		PRNode c = new PRNode("c");
		PRNode d = new PRNode("d");
		PRNode t = new PRNode("t", "end");

		List<PRNode> nodes = new ArrayList<PRNode>();
		nodes.add(s);
		nodes.add(a);
		nodes.add(b);
		nodes.add(c);
		nodes.add(d);
		nodes.add(t);

		PREdge sa = new PREdge(s, a, 15);
		PREdge sb = new PREdge(s, b, 10);
		PREdge ab = new PREdge(a, b, 7);
		PREdge ac = new PREdge(a, c, 6);
		PREdge bc = new PREdge(b, c, 11);
		PREdge bd = new PREdge(b, d, 4);
		PREdge cd = new PREdge(c, d, 2);
		PREdge dc = new PREdge(d, c, 4);
		PREdge ct = new PREdge(c, t, 5);
		PREdge dt = new PREdge(d, t, 20);

		List<PREdge> edges = new ArrayList<PREdge>();
		edges.add(sa);
		edges.add(sb);
		edges.add(ab);
		edges.add(ac);
		edges.add(bc);
		edges.add(bd);
		edges.add(cd);
		edges.add(dc);
		edges.add(ct);
		edges.add(dt);

		return new PRGraph(nodes, edges);
	}

	/**
	 * run the program
	 * 
	 * @param graph
	 */
	public static void run(PRGraph graph) {
		Integer flow = graph.calcMaxFlow();
		System.out.println(flow);
	}
}
