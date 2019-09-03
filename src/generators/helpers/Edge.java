package generators.helpers;

public class Edge implements Comparable<Edge>{
    public int from;
    public int to;
    public int weight;
    public boolean visited = false;
    
    public Edge(int from,int to, int weight) {
	this.from = from;
	this.to = to;
	this.weight = weight;
    }
    
    
    
    public int compareTo(Edge cmp) throws ClassCastException {
	if (!(cmp instanceof Edge))
	    throw new ClassCastException("An Edge object expected.");
	else {
	    int cmp2 = ((Edge) cmp).weight;  
	    
	    if(cmp2<this.weight) return 1;
	    else if(cmp2>this.weight) return -1;
	    else return 0;
	}
    }

}
