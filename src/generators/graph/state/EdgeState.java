package generators.graph.state;

import generators.graph.utils.PREdge;

public class EdgeState {

	private NodeState start;
	private NodeState end;
	private Integer capacity;
	private boolean reverse;
	private Integer currentFlow;
	private Integer residualFlow;
	private Integer reverseResidualFlow;

	/**
	 * Constructor
	 * 
	 * @param edge
	 */
	public EdgeState(PREdge edge) {
		this.start = new NodeState(edge.getStart());
		this.end = new NodeState(edge.getEnd());
		this.capacity = edge.getCapacity();
		this.currentFlow = edge.getCurrentFlow();
		this.reverse = edge.isReverse();
		this.residualFlow = edge.getResidualFlow();
		this.reverseResidualFlow = edge.getReverseResidualFlow();
	}

	/**
	 * returns start node of edge
	 * 
	 * @return
	 */
	public NodeState getStart() {
		return start;
	}

	/**
	 * returns end node of edge
	 * 
	 * @return
	 */
	public NodeState getEnd() {
		return end;
	}

	/**
	 * returns capacity
	 * 
	 * @return
	 */
	public Integer getCapacity() {
		return capacity;
	}

	/**
	 * returns reverse edge
	 * 
	 * @return
	 */
	public boolean isReverse() {
		return reverse;
	}

	/**
	 * returns current flow
	 * 
	 * @return
	 */
	public Integer getCurrentFlow() {
		return currentFlow;
	}

	/**
	 * returns residual flow
	 * 
	 * @return
	 */
	public Integer getResidualFlow() {
		return residualFlow;
	}

	/**
	 * returns reverse residual flow
	 * 
	 * @return
	 */
	public Integer getReverseResidualFlow() {
		return reverseResidualFlow;
	}

	/**
	 * returns names to string
	 */
	public String toString(){
		return ""+start.getName()+"->"+end.getName()+" ("+getCapacity()+","+getCurrentFlow()+") reverse=\""+isReverse()+"\" residualFlow=\""+getResidualFlow()+"\" reverseResidualFlow=\""+getReverseResidualFlow()+"\"";
	}
}
