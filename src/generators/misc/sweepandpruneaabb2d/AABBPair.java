package generators.misc.sweepandpruneaabb2d;

public class AABBPair {
	public AABB first;
	public AABB second;
	
	public boolean equals(AABBPair other) {
		if (first.equals(other.first)) {
			if (second.equals(other.second)){
				return true;
			}
		} else if (first.equals(other.second)) {
			if (second.equals(other.first)){
				return true;
			}
		}
		
		return false;
	}
}
