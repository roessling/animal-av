package generators.searching;

import algoanim.util.Coordinates;
import algoanim.util.Node;

public class JPSNode {
	
	int x;
	int y;
	int drawX;
	int drawY;
	float g,h,f; //g = from start; h = to end, f = both together
	boolean walkable = true;
	boolean start = false;
	boolean end = false;
	private Node node;
	JPSNode parent;
	public JPSNode(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.drawX = x* size;
		this.drawY = y* size + 65;
		node = new Coordinates(drawX,drawY);
	}
	public Node getNode() {
		return node;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
	public boolean isWalkable() {
		return walkable;
	}
	public boolean isStart() {
		return start;
	}
	public boolean isEnd() {
		return end;
	}
	public void updateGHFP(float g, float h, JPSNode parent){
		this.parent = parent;
		this.g = g;
		this.h = h;
		this.f = g+h;
	}
	
	
}
