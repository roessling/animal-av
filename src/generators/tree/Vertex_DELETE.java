package generators.tree;

import algoanim.util.Coordinates;
import algoanim.util.Node;


/**
 * eine interne Klasse, die später auch ausgelagert werden kann
 * 
 * Vertex-Klasse dient als eine vergleichbare (da implements Comperable)
 * Datenstruktur, die (wie der Name schon sagt) einen roten oder
 * schwarzen Knoten darstellt
 * @author matze
 *
 */
public class Vertex_DELETE extends InfoVertex implements Comparable<Vertex_DELETE> {
	private Integer value = null; // >= 0
	private char color; // 'r' = red, 'b' = black
	private Vertex_DELETE leftChild;
	private Vertex_DELETE rightChild;
	private Vertex_DELETE parent = null;
	private Node[] vertexCoordinates = new Node[1];
	private int indexVertex = -1;
	private int[] adjacencyList;
	private int x;
	private int y;
	private boolean deleted = false;

	public void initAdjList(int size) {
		this.adjacencyList = new int[size];
		for (int i = 0; i < adjacencyList.length; i++) {
			this.adjacencyList[i] = 0;
		}
	}
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Vertex_DELETE(Integer value) {
		this.value = value;
		adjacencyList = new int[30];
		for(int i = 0; i < adjacencyList.length; i++)
			adjacencyList[i] = 0;
	}
	
	public void nullChilder() {
		leftChild = new Vertex_DELETE(null);
		rightChild = new Vertex_DELETE(null);
		
		leftChild.setParent(this);
		leftChild.setColor('b');
		rightChild.setParent(this);
		rightChild.setColor('b');
	}

	/**
	 * compare ist eine Methode aus dem Interface Comparable
	 * return 1 wenn this größeren Value hat als der Knoten im Parameter
	 * return 0 wenn gleich
	 * return -1 wenn kleiner
	 */
	@Override
	public int compareTo(Vertex_DELETE v) {
		if(this.value > v.getValue())
			return 1;
		else if(this.value == v.getValue())
			return 0;
		else
			return -1;
	}

	public Node getVertexCoordinates() {
		return vertexCoordinates[0];
	}

	public void setVertexCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
		this.vertexCoordinates[0] = new Coordinates(x,y);
	}

	public Integer getValue() {
		return value;
	}

	public Vertex_DELETE getLeftChild() {
		return this.leftChild;
	}

	public Vertex_DELETE getRightChild() {
		return this.rightChild;
	}

	public void setParent(Vertex_DELETE x) {
		this.parent = x;
	}
	public int[] getAdjacencyList() {
		return adjacencyList;
	}

	public void setChildInAdjacencyList(int childIndex) {
		this.adjacencyList[childIndex] = 1;
	}

	public char getColor() {
		return color;
	}

	public void setColor(char color) {
		this.color = color;
	}

	public Vertex_DELETE getParent() {
		return parent;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setLeftChild(Vertex_DELETE leftChild) {
		this.leftChild = leftChild;
	}

	public void setRightChild(Vertex_DELETE rightChild) {
		this.rightChild = rightChild;
	}

	public int getVertexIndex() {
		return indexVertex;
	}

	public void setVertexIndex(int indexVertex) {
		this.indexVertex = indexVertex;
	}
	
	public String getVertexLabel() {
		String label;
		StringBuffer sb = new StringBuffer();
		sb.append(this.value);
		label = sb.toString();
		return label;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setIndexVertex(int indexVertex) {
		this.indexVertex = indexVertex;
	}

}