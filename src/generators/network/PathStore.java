package generators.network;
import java.util.List;

public class PathStore {
	int length;
	int capacity;
	List<Edge> path;
	
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public List<Edge> getPath() {
		return path;
	}
	public void setPath(List<Edge> path) {
		this.path = path;
	}
	
}
