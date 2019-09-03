package generators.network;
import java.util.ArrayList;
import java.util.List;

public class Graph {

	private List<Edge> edges;
	private List<Graphnode> nodes;

	public Graph(List<Edge> edges, List<Graphnode> nodes) {
		this.nodes = nodes;
		this.edges = edges;
	}

	public List<Graphnode> getNeighbors(Graphnode A) {
		List<Graphnode> neighborList = new ArrayList<Graphnode>();

		for (int i = 0; i < edges.size(); i++) {
			if (A.equals(edges.get(i).getA()) && !neighborList.contains(edges.get(i).getB())) {
				neighborList.add(edges.get(i).getB());
			}
			if (A.equals(edges.get(i).getB()) && !neighborList.contains(edges.get(i).getA())) {
				neighborList.add(edges.get(i).getA());
			} else {
				continue;
			}
		}
		return neighborList;
	}

	public Edge getEdge(Graphnode a, Graphnode b) {
		for (int i = 0; i < edges.size(); i++) {
			if ((edges.get(i).getA().equals(a) && edges.get(i).getB().equals(b))
					|| (edges.get(i).getB().equals(a) && edges.get(i).getA().equals(b))) {
				return edges.get(i);
			} else
				continue;
		}
		return null;
	}
	
	public Graphnode getNode(int id){
		for(int i = 0; i< nodes.size(); i++){
			if(nodes.get(i).getId()==id){
				return nodes.get(i);
			}
			else
				continue;
		}
		return null;
	}
	
	public int[][] getAdjacecyMatrix(){
		int[][] adja = new int[nodes.size()][nodes.size()];
		
		for(int j = 0; j < nodes.size(); j++ ){
			for(int k = 0; k<nodes.size(); k++){
				adja[j][k] = 0;
			}
		}
		
		for(int i = 0; i < edges.size(); i++ ){
			adja[edges.get(i).getA().getId()][edges.get(i).getB().getId()]= edges.get(i).getWeight();
			adja[edges.get(i).getB().getId()][edges.get(i).getA().getId()]= edges.get(i).getWeight();	
		}
		return adja;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public List<Graphnode> getNodes() {
		return nodes;
	}

	public void setNodes(List<Graphnode> nodes) {
		this.nodes = nodes;
	}

}
