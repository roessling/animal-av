package generators.misc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Finds all stars within linking length by iterating over allStars and calculating the distance from a to b.
 */
public class LinearSearch implements ISearchStrategy
{
	/**
	 * Iterates over all points.
	 * Compares input point to all other points.
	 * Returns all points within linking length. 
	 */
	@Override
	public Set<Star> nearestNeighbourSearch(Star star, double linkingLength, Set<Star> allStars)
	{
		Set<Star> ret = new HashSet<Star>();
		
		Iterator<Star> it = allStars.iterator();
		while(it.hasNext())
		{
			Star current = it.next();
			int xDist = star.x - current.x;
			int yDist = star.y - current.y;
			double dist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
			if(dist <= linkingLength) ret.add(current);
		}
		return ret;
	}

}
