package generators.misc;

import java.util.Set;

public class KDNode
{
	Star value;
	
	KDNode left;
	KDNode right;
	
	int direction;
	
	public KDNode(Star val, KDNode l, KDNode r, int dir)
	{
		this.value = val;
		this.left = l;
		this.right = r;
		this.direction = dir;
	}
	
	/**
	 * Does a range search on the tree/subtree recursively
	 * @param star			The point to search around
	 * @param linkingLength	The distance to check around
	 * @param ret			Keeps a list of currently found points
	 * @return				Returns all points within linking length of the point
	 */
	public Set<Star> rangeSearch(Star star, double linkingLength, Set<Star> ret)
	{
		// check if current is within x and y bounds, then add to list
		int xDist = star.x - this.value.x;
		int yDist = star.y - this.value.y;
		double dist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
		if(dist <= linkingLength) ret.add(this.value);
		
		if(this.direction == 0)
		{
			double rangeXLow = star.x - linkingLength;
			double rangeXHigh = star.x + linkingLength;
			
			// get all in range from left and right subtree
			if(this.value.x >= rangeXLow && left != null) ret.addAll( left.rangeSearch(star, linkingLength, ret) );
			if(this.value.x <= rangeXHigh && right != null) ret.addAll( right.rangeSearch(star, linkingLength, ret) );
		} else
		{
			double rangeYLow = star.y - linkingLength;
			double rangeYHigh = star.y + linkingLength;
			
			// get all in range from left and right subtree
			if(this.value.y >= rangeYLow && left != null) ret.addAll( left.rangeSearch(star, linkingLength, ret) );
			if(this.value.y <= rangeYHigh && right != null) ret.addAll( right.rangeSearch(star, linkingLength, ret) );
		}
		return ret;
	}
}
