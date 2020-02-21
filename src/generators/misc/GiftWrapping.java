package generators.misc;

import java.util.ArrayList;
import java.util.Set;

// https://www.geeksforgeeks.org/convex-hull-set-1-jarviss-algorithm-or-wrapping/
public class GiftWrapping implements IHullAlgorithms
{
	@Override
	public ArrayList<Star> constructHull(Set<Star> stars)
	{
		ArrayList<Star> hull = new ArrayList<Star>();	// the returned arraylist
		
		Object[] points = stars.toArray();				// all the stars as input
		if(points.length < 3)
		{
			for(int i=0;i<points.length;i++)
			{
				hull.add((Star) points[i]);
			}
			return hull;
		}
		
		int lowest = 0;
		for(int i=0;i<points.length;i++) if(((Star)points[i]).x < ((Star)points[lowest]).x) lowest = i;
		
		int start = lowest, next;
		int counter = 0;
		
		do
		{
			hull.add((Star)points[start]);
			next = (start+1) % points.length;
			counter++;
			
			for(int i=0;i<points.length;i++)
			{
				if(orientation((Star)points[start], (Star)points[i], (Star)points[next]) == 2)
				{
					next = i;
				}
			}
			start = next;
			
			if(counter > 1000)
			{	// there's a possible infinite loop that only happens sometimes
				// case: (4,13) (4,14) (4,15) (6,13)   infinite loop about every 5th try without the counter to terminate it 
				return removeDuplicates(hull);
			}
		} while( !((Star) points[start]).equals((Star)points[lowest]) );
		
		return removeDuplicates(hull);
	}
	
	public ArrayList<Star> removeDuplicates(ArrayList<Star> stars)
	{
		ArrayList<Star> noDupes = new ArrayList<Star>();
		for(int i=0;i<stars.size();i++)
		{
			Star current = stars.get(i);
			if(!noDupes.contains(current)) noDupes.add(current);
		}
		return noDupes;
	}
	
	// test if angle is clockwise, counterclockwise or 0degrees
	int orientation(Star a, Star b, Star c) 
	{
	    int val = (b.y - a.y) * (c.x - b.x) - 
	              (b.x - a.x) * (c.y - b.y); 
	    
	    if (val == 0) return 0;  // colinear 
	    return (val > 0) ? 1 : 2; // clock or counterclock wise 
	}
}
