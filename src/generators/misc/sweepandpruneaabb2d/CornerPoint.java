package generators.misc.sweepandpruneaabb2d;

public class CornerPoint implements Comparable<CornerPoint> {
	private int value;
	private MinMax type;
	private AABB owner;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public MinMax getType() {
		return type;
	}
	public void setType(MinMax type) {
		this.type = type;
	}
	public AABB getOwner() {
		return owner;
	}
	public void setOwner(AABB owner) {
		this.owner = owner;
	}
	@Override
	public int compareTo(CornerPoint o) {
		if (this.getValue() > o.getValue()) {
			return 1;
		} else if (this.getValue() < o.getValue()) {
			return -1;
		} else if (this.getType().equals(MinMax.MIN) && o.getType().equals(MinMax.MAX)) {
			return 1;
		} else if (this.getType().equals(MinMax.MAX) && o.getType().equals(MinMax.MIN)) {
			return -1;
		} else 
			return 0;
	}
}
