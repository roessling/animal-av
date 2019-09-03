package generators.network;

public class Edge {
	private Graphnode A;
	private Graphnode B;
	private int weight;

	public Edge(Graphnode from, Graphnode to, int weight){
		A = from;
		B = to;
		this.weight = weight;
	}
	
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public Graphnode getA(){
		return A;
	}
	
	public void setA(Graphnode a) {
		A = a;
	}
	
	public Graphnode getB(){
		return B;
	}
	
	
	public void setB(Graphnode b) {
		B = b;
	}


}
