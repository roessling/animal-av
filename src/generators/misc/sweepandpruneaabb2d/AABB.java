package generators.misc.sweepandpruneaabb2d;

public class AABB {
	
	static int id = 0;
	
	private int myID = -1;
	
	public AABB(int minX, int maxX, int minY, int maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		
		myID = id;
		id++;
	}
	
	private int minX = 0;
	private int maxX = 0;
	private int minY = 0;
	private int maxY = 0;
	
	public int getMinX() {
		return minX;
	}
	public void setMinX(int minX) {
		this.minX = minX;
	}
	public int getMaxX() {
		return maxX;
	}
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	public int getMinY() {
		return minY;
	}
	public void setMinY(int minY) {
		this.minY = minY;
	}
	public int getMaxY() {
		return maxY;
	}
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	public int getMyID() {
		return myID;
	}
}
