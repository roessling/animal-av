package generators.misc;

import java.util.ArrayList;
import java.util.Set;

/**
 * Takes a convex hull from GiftWrapping and digs out parts to make it concave.
 * Pseudocode and idea from https://www.iis.sinica.edu.tw/page/jise/2012/201205_10.pdf
 */
public class ConcaveHull implements IConcaveHullAlgorithms
{
	public static double decision = 1;
	
	public ConcaveHull(double dec)
	{
		decision = dec;	// decides when to dig. higher values dig more often, but sometimes dig too much. lower values go more towards a convex hull.
	}
	
	@Override
	public ArrayList<Star> constructHull(Set<Star> stars)
	{
		// Get concave hull
		ArrayList<Star> concaveList = new GiftWrapping().constructHull(stars);
		if(concaveList.size() < 5) return concaveList;
		
		for(int i=0;i<concaveList.size();i++)
		{
			// get edge star1 to star2
			Star a = concaveList.get(i);
			Star b = concaveList.get((i+1) % concaveList.size());
			
			// find nearest point to edge a->b
			Tuple<Star, Double> nearest = findNearestToEdge(a, b, stars);
			
			double edgeLength = Star.distance(a, b);
			double decisionDistance = nearest.second;
			
			if(Math.floor(edgeLength / decisionDistance) > decision && !concaveList.contains(nearest.first))
			{
				// insert p into list
				concaveList.add(i+1, nearest.first);
			}
		}
		return concaveList;
	}
	
	// Finds a star nearest to the edge a->b
	private Tuple<Star, Double> findNearestToEdge(Star a, Star b, Set<Star> stars)
	{
		Tuple<Star, Double> one = findNearestToStar(a, stars);
		Tuple<Star, Double> two = findNearestToStar(b, stars);
		one.second = pointDistanceToEdge(a, b, one.first);
		two.second = pointDistanceToEdge(a, b, two.first);
		if(one.second <= two.second) return one;
		return two;
	}
	
	// Calculates the distance from a point to an edge
	private double pointDistanceToEdge(Star edgeStart, Star edgeEnd, Star test)
	{
		double x0 = test.x;
		double y0 = test.y;
		double x1 = edgeStart.x;
		double y1 = edgeStart.y;
		double x2 = edgeEnd.x;
		double y2 = edgeEnd.y;
		
		double upper = Math.abs( (y2 - y1)*x0 - (x2 - x1)*y0 + x2*y1 - y2*x1 );
		double lower = Math.sqrt( Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2) );
		return upper / lower;
	}
	
	/**
	 * Iterates over stars and finds the star closest to a.
	 * @return	A tuple with the closest star and its distance to a.
	 */
	private Tuple<Star, Double> findNearestToStar(Star a, Set<Star> stars)
	{
		Star nearest = null;
		double distance = 0;
		for(Star s : stars)
		{
			if(!s.equals(a))
			{
				if(nearest == null)
				{
					nearest = s;
					distance = Star.distance(s, a);
				}
				else
				{
					double currentDistance = Star.distance(s, a);
					if(currentDistance < distance) 
					{
						nearest = s;
						distance = currentDistance;
					}
				}
			}
		}
		return new Tuple<Star, Double>(nearest, distance);
	}

}

/**
 * A tuple implementation so I don't have to recalculate the distance all the time.
 */
class Tuple<T1, T2>
{
	T1 first;
	T2 second;
	
	public Tuple(T1 one, T2 two)
	{
		first = one;
		second = two;
	}
}
