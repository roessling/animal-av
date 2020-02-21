package generators.misc;

import java.util.Set;

// Different strategies for Nearest Neighbour Search with range
// Currently : KDNearestNeighbourSearch.java , LinearSearch.java
public interface ISearchStrategy
{
	// Finds all stars within linkingLength distance to s
	public Set<Star> nearestNeighbourSearch(Star star, double linkingLength, Set<Star> allStars);
}
