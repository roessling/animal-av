package generators.graph.utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import generators.graph.exceptions.NoResidualEdgesException;

public class PRNode implements Cloneable {

	private String name;
	private List<PREdge> outgoingEdges = new ArrayList<PREdge>();
	private List<PREdge> ingoingEdges = new ArrayList<PREdge>();
	private Integer distance = 0;
	private String startOrEnd = null;

	public PRNode(String name) {
		super();
		this.name = name;
	}

	public PRNode(String name, String startOrEnd) {
		super();
		this.name = name;
		this.startOrEnd = startOrEnd;
	}

	public PRNode(String name, List<PREdge> outgoingEdges, List<PREdge> ingoingEdges,
			String startOrEnd) {
		super();
		this.name = name;
		this.outgoingEdges = outgoingEdges;
		this.ingoingEdges = ingoingEdges;
		this.startOrEnd = startOrEnd;
	}

	public PRNode(String name, List<PREdge> outgoingEdges, List<PREdge> ingoingEdges) {
		super();
		this.name = name;
		this.outgoingEdges = outgoingEdges;
		this.ingoingEdges = ingoingEdges;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PREdge> getOutgoingEdges() {
		return this.outgoingEdges;
	}

	public void setOutgoingEdges(List<PREdge> outgoingEdges) {
		this.outgoingEdges = outgoingEdges;
	}

	public List<PREdge> getIngoingEdges() {
		return this.ingoingEdges;
	}

	public void setIngoingEdges(List<PREdge> ingoingEdges) {
		this.ingoingEdges = ingoingEdges;
	}

	public Integer getFlowExcess() {

		Integer result = 0;

		for (PREdge edge : ingoingEdges) {
			result += edge.getCurrentFlow();
		}

		for (PREdge edge : outgoingEdges) {
			result -= edge.getCurrentFlow();
		}

		return result;
	}

	public boolean isActive() {
		return isActive(false);
	}

	public boolean isActive(boolean reverse) {
		if (!reverse) {
			boolean temp = getFlowExcess() > 0 && getStartOrEnd() == null;
			return temp;
		}
		return false;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Integer getDistance() {
		return this.distance;
	}

	public List<PREdge> getResidualGraphEdgesWithStart()
			throws NoResidualEdgesException {
		List<PREdge> residualEdges = new ArrayList<PREdge>();
		for (PREdge edge : getOutgoingEdges()) {
			if (edge.getResidualFlow() > 0) {
				residualEdges.add(edge);
			}
		}
		for (PREdge edge : getIngoingEdges()) {
			if (edge.getReverseResidualFlow() > 0) {
				residualEdges.add(edge);
			}
		}
		if (residualEdges.size() == 0)
			throw new NoResidualEdgesException("No residual edges found");

		Collections.sort(residualEdges, new Comparator<PREdge>() {

			@Override
			public int compare(PREdge edge1, PREdge edge2) {
				// if edge1 < edge2 -> -1
				if (edge1.getResidualFlow() < edge2.getResidualFlow()) {
					return -1;
				} else if (edge1.getResidualFlow() > edge2.getResidualFlow()) {
					return 1;
				} else {
					if (edge1.getStart().isS()) {
						return -1;
					} else if (edge2.getStart().isS()) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		});

		return residualEdges;
	}

	public boolean isS() {
		return this.startOrEnd == "start";
	}

	public boolean isT() {
		return this.startOrEnd == "end";
	}

	public String toString() {
		return "name=\"" + getName() + "\" startOrEnd=\""
				+ getStartOrEnd() + "\" active=\"" + isActive()
				+ "\" distance=\"" + getDistance() + "\" flowExcess=\""
				+ getFlowExcess() + "\"";

	}

	public String getStartOrEnd() {
		return this.startOrEnd;
	}

	public PREdge getMinDistanceEdge(List<PREdge> edges) {
		List<PREdge> possibleEdges = new ArrayList<PREdge>();

		for (PREdge edge : edges) {
			if (outgoingEdges.contains(edge)) {
				if (edge.getEnd().getDistance() + 1 == this.getDistance()) {
					possibleEdges.add(edge);
					if (edge.getEnd().isT()) {
						return edge;
					}
				}
			} else if (ingoingEdges.contains(edge)) {
				if (edge.getStart().getDistance() + 1 == this.getDistance()) {
					possibleEdges.add(edge);
				}
			}
		}

		if (possibleEdges.size() == 1) {
			return possibleEdges.get(0);
		}

		Integer min = null;
		PREdge minEdge = null;
		for (PREdge edge : edges) {
			if (edge.isReverse()) {
				if (min == null || edge.getStart().getDistance() <= min) {// ||
																			// (edge.getStart().getDistance()
																			// <=
																			// min
																			// &&
																			// edge.getStart().isT())){
					min = edge.getStart().getDistance();
					minEdge = edge;
				}
			} else {
				if (min == null || edge.getEnd().getDistance() <= min) {
					min = edge.getEnd().getDistance();
					minEdge = edge;
				}
			}
		}

		return minEdge;
	}

}
