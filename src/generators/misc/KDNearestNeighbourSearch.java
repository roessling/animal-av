package generators.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KDNearestNeighbourSearch implements ISearchStrategy
{
	public static KDNode root;
	
	public KDNearestNeighbourSearch()
	{
		root = null;
	}
	
	@Override
	public Set<Star> nearestNeighbourSearch(Star star, double linkingLength, Set<Star> allStars)
	{
		// make kdtree, save root
		if(root == null) root = makeKDTree(new ArrayList<Star>(allStars), 0);
		
		// get all stars within linkingLength
		return root.rangeSearch(star, linkingLength, new HashSet<Star>());
	}
	
	private KDNode makeKDTree(List<Star> stars, int depth)
	{
		if(stars == null || stars.size() < 1) return null;
		
		int currentAxis = depth % 2;
		if(stars.size() == 1)
		{
			return new KDNode(stars.get(0), null, null, currentAxis);
		}
		if(currentAxis == 0) Collections.sort(stars, Star.compareByX);
		if(currentAxis == 1) Collections.sort(stars, Star.compareByY);
		
		if(stars.size() == 2)
		{
			Star two = stars.get(1);
			return new KDNode(two, makeKDTree(stars.subList(0, 1), depth+1), null, currentAxis);
		}
		
		int middle = (int) Math.floor(stars.size() / 2);
		List<Star> lowerList = stars.subList(0, middle);				// < middle
		List<Star> higherList = stars.subList(middle + 1, stars.size());	// >= middle
		Star median = stars.get(middle);
		
		return new KDNode(median, makeKDTree(lowerList, depth+1), makeKDTree(higherList, depth+1), currentAxis);
	}
}
