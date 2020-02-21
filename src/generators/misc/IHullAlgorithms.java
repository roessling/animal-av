package generators.misc;

import java.util.ArrayList;
import java.util.Set;

// Different Hull construction algorithms
// Split again in IConvexHullAlgorithms and IConcaveHullAlgorithms
// Currently : GiftWrapping.java , ConcaveHull.java
public interface IHullAlgorithms
{
	// Returns all stars that make up the hull
	public ArrayList<Star> constructHull(Set<Star> stars);
}
