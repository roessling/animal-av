package generators.tree;

import java.util.ArrayList;

public class PrueferNode {
	private int data;
	private int degree;
	private int x;
	private int y;
	private ArrayList<PrueferNode> neighbors;

	public PrueferNode(int data) {
		super();
		this.data = data;
		this.degree = 1;
		this.neighbors = new ArrayList<PrueferNode>();
		this.setX(800);
		this.setY(400);
	}

	public int getData() {
		return this.data;
	}

	public int getDegree() {
		return this.degree;
	}

	public void increaseDegree() {
		this.degree++;
	}

	public void decreaseDegree() {
		this.degree--;
	}

	public void setNeighbors(ArrayList<PrueferNode> n) {
		this.neighbors = n;
	}

	public ArrayList<PrueferNode> getNeighbors() {
		return this.neighbors;
	}

	public String getNeighborsString() {
		String output = "";
		for (int i = 0; i < neighbors.size(); i++) {
			if (i == neighbors.size() - 1) {
				output = output + neighbors.get(i).getData();
			} else {
				output = output + neighbors.get(i).getData() + ", ";
			}
		}
		return output;
	}

	public void addNeighbor(PrueferNode n) {
		this.neighbors.add(n);
	}

	public void deleteprueferNode(PrueferNode n) {
		for (PrueferNode prueferNode : this.neighbors) {
			if (prueferNode.getData() == n.getData()) {
				neighbors.remove(n);
				break;
			}
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
