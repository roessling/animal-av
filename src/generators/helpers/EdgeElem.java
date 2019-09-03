package generators.helpers;

public class EdgeElem {

	private int source;
	private int target;
	private int edgeid;
	private int weight;
	
	
	public EdgeElem(int source, int target, int edgeid, int weight) {
		this.source = source;
		this.target = target;
		this.edgeid = edgeid;
		this.weight = weight;
	}
	
	public int getsource() {
		return source;
	}
	
	public int gettarget() {
		return target;
	}
	
	public int getedgeid() {
		return edgeid;
	}
	
	public int getweight() {
		return weight;
	}
}
