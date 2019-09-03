package generators.graph.utils;
import generators.graph.exceptions.FlowException;

public class PREdge implements Cloneable{

	private Integer capacity;
	private PRNode start;
	private PRNode end;
	private Integer currentFlow = 0;
	private boolean reverse = false;

	/**
	 * edge
	 * 
	 * @param start
	 * @param end
	 */
	public PREdge(PRNode start, PRNode end) {
		super();
		setStart(start);
		setEnd(end);
	}

	/**
	 * edge with capacity
	 * 
	 * @param start
	 * @param end
	 * @param capacity
	 */
	public PREdge(PRNode start, PRNode end, Integer capacity) {
		super();
		setStart(start);
		setEnd(end);
		setCapacity(capacity);
	}
	
	/**
	 * edge with capacity and reverse
	 * 
	 * @param start
	 * @param end
	 * @param capacity
	 * @param reverse
	 */
	public PREdge(PRNode start, PRNode end, Integer capacity, boolean reverse) {
		super();
		setStart(start);
		setEnd(end);
		setCapacity(capacity);
		setReverse(reverse);
	}
	
	/**
	 * edge with capacity and current flow
	 * 
	 * @param start
	 * @param end
	 * @param capacity
	 * @param currentFlow
	 */
	public PREdge(PRNode start, PRNode end, Integer capacity, Integer currentFlow) {
		super();
		setStart(start);
		setEnd(end);
		setCapacity(capacity);
		setCurrentFlow(currentFlow);
	}

	/**
	 * set the current flow
	 * 
	 * @param currentFlow
	 */
	public void setCurrentFlow(Integer currentFlow) {
		this.currentFlow = currentFlow;
		if (getCurrentFlow() > 0) {
			setReverse(true);
		} else {
			setReverse(false);
		}
	}

	/**
	 * set start node
	 * 
	 * @param start
	 */
	public void setStart(PRNode start) {
		this.start = start;
	}

	/**
	 * set end node
	 * 
	 * @param end
	 */
	public void setEnd(PRNode end) {
		this.end = end;
	}

	/**
	 * set capacity
	 * 
	 * @param capacity
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	/**
	 * set reverse
	 * 
	 * @param reverse
	 */
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	/**
	 * 
	 * 
	 * @param newFlow
	 * @return if flow can be increased
	 */
	public boolean canIncreaseFlowBy(Integer newFlow){
		return ((getCurrentFlow() + newFlow)<=getCapacity());
	}
	
	/**
	 * return if flow can be increased
	 * 
	 * @return
	 */
	public boolean canIncreaseFlow(){
		return (getCurrentFlow()<=getCapacity());
	}
	
	/**
	 * return if current flow can be increased
	 * 
	 * @param moreFlow
	 * @throws FlowException
	 */
	public void increaseCurrentFlow(Integer moreFlow) throws FlowException{
		if ((getCurrentFlow() + moreFlow)<=getCapacity())
			setCurrentFlow(getCurrentFlow() + moreFlow);
		else
			throw new FlowException("Flow capacity exceeded.");
	}
	
	/**
	 * return if current flow can be decreased
	 * 
	 * @param lessFlow
	 * @throws FlowException
	 */
	public void decreaseCurrentFlow(Integer lessFlow) throws FlowException{
		if ((getCurrentFlow() - lessFlow)>=0)
			setCurrentFlow(getCurrentFlow() - lessFlow);
		else
			throw new FlowException("No negative flow allowed.");
	}

	/**
	 * return if reverse
	 * 
	 * @return
	 */
	public boolean isReverse() {
		return this.reverse;
	}

	/**
	 * return if end
	 * 
	 * @param w
	 * @return
	 */
	public boolean isEnd(PRNode w) {
		return getEnd().equals(w);
	}

	/**
	 * return if start
	 * 
	 * @param v
	 * @return
	 */
	public boolean isStart(PRNode v) {
		return getStart().equals(v);
	}
	
	/**
	 * get end node
	 * 
	 * @return
	 */
	public PRNode getEnd() {
		return end;
	}

	/**
	 * get start node
	 * 
	 * @return
	 */
	public PRNode getStart() {
		return start;
	}

	/**
	 * get capacity
	 * 
	 * @return
	 */
	public Integer getCapacity() {
		return this.capacity;
	}

	/**
	 * get current flow
	 * 
	 * @return
	 */
	public Integer getCurrentFlow() {
		return this.currentFlow;
	}

	/**
	 * get reverse edge
	 * 
	 * @return
	 */
	public PREdge getReverseEdge() {
		return new PREdge(start, end, capacity, true);
	}

	/**
	 * get residual flow
	 * 
	 * @return
	 */
	public Integer getResidualFlow() {
		return (getCapacity()-getCurrentFlow());
	}

	/**
	 * get reverse residual flow
	 * 
	 * @return
	 */
	public Integer getReverseResidualFlow() {
		return this.getCurrentFlow();
	}

	/**
	 * return string
	 */
	public String toString(){
		return ""+start.getName()+"->"+end.getName()+" ("+getCapacity()+","+getCurrentFlow()+") reverse=\""+isReverse()+"\" residualFlow=\""+getResidualFlow()+"\" reverseResidualFlow=\""+getReverseResidualFlow()+"\"";
	}
}
