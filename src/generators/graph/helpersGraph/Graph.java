package generators.graph.helpersGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
	
	public ArrayList<Node> nodes;
	public ArrayList<Edge> edges;
	
	public Graph(int n) {
		this.nodes = new ArrayList<>(n);
		this.edges = new ArrayList<>();
	}
	
	public static Graph createLargeDefaultGraph() {
		Graph gr = new Graph(9);
		char ch = 'A';
		int i;
		for(i = 0; i < 9; i++) {
			Node n = new Node(ch);
			n.routingTable = new HashMap<>(9);
			gr.nodes.add(n);
			ch = (char) (ch + 1);
		}
		
		for(Node n : gr.nodes) {
			for(int j = 0; j < gr.nodes.size(); j++) {
				n.routingTable.put(gr.nodes.get(j).name, Integer.MAX_VALUE);
			}
			n.routingTable.put(n.name, 0);
		}
		ArrayList<Node> list = gr.nodes;
		Edge a = new Edge(list.get(0), list.get(1));
		Edge b = new Edge(list.get(0), list.get(2));
		Edge c = new Edge(list.get(1), list.get(2));
		Edge d = new Edge(list.get(2), list.get(3));
		Edge e = new Edge(list.get(3), list.get(4));
		Edge f = new Edge(list.get(3), list.get(5));
		Edge g = new Edge(list.get(4), list.get(6));
		Edge h = new Edge(list.get(5), list.get(7));
		Edge I = new Edge(list.get(6), list.get(8));
		Edge j = new Edge(list.get(5), list.get(8));
		
		gr.edges.add(a);
		gr.edges.add(b);
		gr.edges.add(c);
		gr.edges.add(d);
		gr.edges.add(e);
		gr.edges.add(f);
		gr.edges.add(g);
		gr.edges.add(h);
		gr.edges.add(I);
		gr.edges.add(j);
		
		gr.fillNeighbours();
		return gr;
	}
	
	public static Graph createSmallDefaultGraph() {
		Graph gr = new Graph(5);
		char ch = 'A';
		int i;
		for(i = 0; i < 5; i++) {
			Node n = new Node(ch);
			n.routingTable = new HashMap<>(5);
			gr.nodes.add(n);
			ch = (char) (ch + 1);
		}
		
		for(i = 0; i < 5; i++) {
			Node n = gr.nodes.get(i);
			for(int j = 0; j < gr.nodes.size(); j++) {
				n.routingTable.put(gr.nodes.get(j).name, Integer.MAX_VALUE);
			}
		}
		ArrayList<Node> list = gr.nodes;
		Edge a = new Edge(list.get(0), list.get(1));
		Edge b = new Edge(list.get(0), list.get(2));
		Edge c = new Edge(list.get(0), list.get(3));
		Edge d = new Edge(list.get(1), list.get(3));
		Edge e = new Edge(list.get(1), list.get(4));
		Edge f = new Edge(list.get(2), list.get(3));
		Edge g = new Edge(list.get(3), list.get(4));
		
		gr.edges.add(a);
		gr.edges.add(b);
		gr.edges.add(c);
		gr.edges.add(d);
		gr.edges.add(e);
		gr.edges.add(f);
		gr.edges.add(g);
		
		gr.fillNeighbours();
		return gr;
	}
	
	public void fillNeighbours() {
		ArrayList<Node> nodeList = this.nodes;
		ArrayList<Edge> edgeList = this.edges;
		
		for(int i = 0; i < nodeList.size(); i++) {
			Node n = nodeList.get(i);
			ArrayList<Neighbour> nL = n.neighbours;
			
			for(int j = 0; j < edgeList.size(); j++) {
				Edge e = edgeList.get(j);
				if(e.first.name == n.name) {
					Neighbour input = new Neighbour(e.second, e.weight);
					if(!nL.contains(input)) nL.add(input);
				}
				else if(e.second.name == n.name) {
					Neighbour input2 = new Neighbour(e.first, e.weight);
					if(!nL.contains(input2)) nL.add(input2);
				}
			}
		}
	}
	
	public Node getNode(char c) {
		for(int i = 0; i < this.nodes.size(); i++) if(this.nodes.get(i).name == c) return this.nodes.get(i);
		return null;
	}
	
	public void backwardLearning(char startNode) {
		LinkedList<Node> fifo = new LinkedList<>();
		Node n = this.getNode(startNode);
		n.via = n;
		//n.routingTable.put(n.name, 0);
		fifo.add(n);
		
		while(fifo.size() != 0) {
			Node no = fifo.getFirst();
			fifo.removeFirst();
			no.visited = true;
			System.out.println("Arbeite Node " + no.name + " ab");
			int lastDistance = no.routingTable.get(n.name);
			//System.out.println("Letzte Distanz :" + lastDistance);
			int i;
		
			//gebe Nachbarn Pakete
			for(i = 0; i < no.neighbours.size(); i++) {
				Node nb = no.neighbours.get(i).neighbour;
				if(nb.visited) continue;
				else nb.visited = true;
				System.out.println("Nachbar " + nb.name + " bekommt ein Paket");
				
				Packet p = new Packet(no, lastDistance + no.neighbours.get(i).distance);
				nb.packet = p;
			}
			
			//arbeite Pakete ab
			for(i = 0; i < this.nodes.size(); i++) {
				Node node = this.nodes.get(i);
				Packet p = node.packet;
				HashMap<Character, Integer> table = node.routingTable;
				if(p != null) {
					System.out.println(node.name + " hat ein Paket mit aktueller Distanz " + p.sumDistance + ". Vorherige Distanz war: " + node.routingTable.get(n.name));
					//if(table.get(n.name) > p.sumDistance) {
						table.put(n.name, p.sumDistance);
						node.via = p.lastNode;
						//System.out.println(node.name + " wurde geupdated, kommt also in den Stack");
						fifo.add(node);
					//}
					System.out.println("Aktualisierte Distanz ist nun: " + table.get(n.name) + " �ber den Node " + node.via.name);
				}
			}
			
			//L�sche alle Pakete
			for(i = 0; i < this.nodes.size(); i++) this.nodes.get(i).packet = null;
		}
	}
	
	public static void main(String[] args) {
		Graph g = Graph.createLargeDefaultGraph();
		char c;
		g.backwardLearning('A');
		//for(c = 'A'; c <= 'I'; c = (char) (c + 1)) g.backwardLearning(c);
		
		for(Node n: g.nodes) {
			System.out.println("Node " + n.name);
			System.out.println("----------------");
			for(c = 'A'; c <= 'I'; c = (char) (c + 1)) {
				System.out.println(c + "" + n.routingTable.get(c));
			}
			System.out.println("##################");
		}
	}

}
