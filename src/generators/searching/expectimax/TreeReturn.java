package generators.searching.expectimax;

public class TreeReturn {
	private ExpectimaxNode node;
	private int length;
	
	public ExpectimaxNode getNode() {
		return node;
	}
	public void setNode(ExpectimaxNode node) {
		this.node = node;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public TreeReturn(ExpectimaxNode node, int length) {
		super();
		this.node = node;
		this.length = length;
	}
	
}
