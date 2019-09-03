package generators.sorting;

import algoanim.primitives.Circle;
import algoanim.util.Node;

public class TreeSortNode extends Node {

	public TreeSortNode left = null;
	public TreeSortNode right = null;
	public Circle circle = null;
	public String data = "";
	public int level = 0;
	public int NoInArr = 0;

	public TreeSortNode(TreeSortNode left, TreeSortNode right, Circle circle, String data, int level, int NoInArr) {
		this.left = left;
		this.right = right;
		this.circle = circle;
		this.data = data;
		this.level = level;
		this.NoInArr = NoInArr;
	}

	public int getNextEmptyChildLevel() {
		if (this.left == null || this.right == null) {
			return this.level + 1;
		} else {
			if (this.left.getNextEmptyChildLevel() < this.right.getNextEmptyChildLevel()) {
				return this.left.getNextEmptyChildLevel();
			} else {
				this.right.getNextEmptyChildLevel();
			}
		}

		return -1;
	}

	public void addChild(TreeSortNode child) {
		if (this.left == null) {
			this.left = child;
		} else {
			if (this.right == null) {
				this.right = child;
			}
		}
	}

	public TreeSortNode(String IN_data, int NoInArr) {
		this.left = null;
		this.right = null;
		this.circle = null;
		this.data = IN_data;
		this.level = 0;
		this.NoInArr = NoInArr;
	}

	public TreeSortNode() {
		// TODO Auto-generated constructor stub
	}
}

// TODO: Level by level traversierung synchrones hinzuf�gen