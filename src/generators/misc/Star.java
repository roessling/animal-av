package generators.misc;

import java.util.Comparator;

// Implementation of a star / point
public class Star
{
	public int x;
	public int y;
	public int group;
	public boolean visited;

	public Star(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.group = -1;
		this.visited = false;
	}
	
	public static double distance(Star a, Star b)
	{
		int distX = a.x - b.x;
		int distY = a.y - b.y;
		return Math.sqrt( Math.pow(distX, 2) + Math.pow(distY, 2) );
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Star other = (Star) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
	
	// For sorting a list of stars by their x-coordinate. Used in KDNearestNeighbourSearch.java.
	public static Comparator<Star> compareByX = new Comparator<Star>() {
	    @Override
	    public int compare(Star s1, Star s2) {
	        return Integer.compare(s1.x, s2.x);
	    }
	};
	
	// For sorting a list of stars by their y-coordinate. Used in KDNearestNeighbourSearch.java.
	public static Comparator<Star> compareByY = new Comparator<Star>() {
	    @Override
	    public int compare(Star s1, Star s2) {
	        return Integer.compare(s1.y, s2.y);
	    }
	};
}
