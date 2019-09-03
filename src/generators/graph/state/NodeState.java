package generators.graph.state;

import generators.graph.utils.PRNode;

public class NodeState {

	private String name;
	private String startOrEnd;
	private boolean active;
	private Integer distance;
	private Integer flowExcess;

	public NodeState(PRNode node) {
		this.name = node.getName();
		this.startOrEnd = node.getStartOrEnd();
		this.active = node.isActive();
		this.distance = node.getDistance();
		this.flowExcess = node.getFlowExcess();
	}

	/**
	 * returns node name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns start or end node
	 * 
	 * @return
	 */
	public String getStartOrEnd() {
		return startOrEnd;
	}

	/**
	 * returns active node
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * returns distance
	 * 
	 * @return
	 */
	public Integer getHeight() {
		return distance;
	}

	/**
	 * returns flow excess
	 * 
	 * @return
	 */
	public Integer getFlowExcess() {
		return flowExcess;
	}
	
	/**
	 * returns values to string
	 */
	@Override
	public String toString() {
		return "name=\"" + getName() + "\" startOrEnd=\""
		+ getStartOrEnd() + "\" active=\"" + isActive()
		+ "\" distance=\"" + getHeight() + "\" flowExcess=\""
		+ getFlowExcess() + "\"";
	}
	
	/**
	 * 
	 */
	public boolean equals(Object node) {
		return ((NodeState)node).getName().equals(this.getName()); // && node.getDistance().equals(this.getDistance()) && node.getFlowExcess().equals(this.getFlowExcess()) && node.isActive()==this.isActive();
	}

}
