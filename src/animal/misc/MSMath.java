package animal.misc;

import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.PTPoint;

/**
 * provides some simple mathematic functions
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 24.08.1998
 */
public final class MSMath {
	/**
	 * square of <i>x</i>
	 * 
	 * @return x^2
	 */
	public static int sqr(int x) {
		return x * x;
	}

	/**
	 * square of <i>x</i>
	 * 
	 * @return x^2
	 */
	public static double sqr(double x) {
		return x * x;
	}

	/**
	 * difference of two points.
	 * 
	 * @return a-b
	 */
	public static Point diff(PTPoint a, PTPoint b) {
		if (a != null && b != null)
			return diff(a.toPoint(), b.toPoint());
		return new Point(0, 0);
	}
	
	/**
	 * difference of two points.
	 * 
	 * @return a-b
	 */
	public static Point diff(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

	/**
	 * sum of two points.
	 * 
	 * @return a+b
	 */
	public static Point sum(PTPoint a, PTPoint b) {
		if (a != null && b != null)
			return sum(a.toPoint(), b.toPoint());
		return new Point(0, 0);
	}
	
	/**
	 * sum of two points.
	 * 
	 * @return a+b
	 */
	public static Point sum(Point a, Point b) {
	  if (a == null)
	    return b;
	  else if (b == null)
	    return a;
		return new Point(a.x + b.x, a.y + b.y);
	}

	/**
	 * dot product of two Vectors.
	 * 
	 * @return a*b
	 */
	public static int innerProduct(PTPoint a, PTPoint b) {
		if (a != null && b != null)
			return innerProduct(a.toPoint(), b.toPoint());
		return 0;
	}
	
	/**
	 * dot product of two Vectors.
	 * 
	 * @return a*b
	 */
	public static int innerProduct(Point a, Point b) {
		return a.x * b.x + a.y * b.y;
	}

	/**
	 * euclidian distance between two points.
	 * 
	 * @return |a-b|
	 */
	public static int dist(Point a, Point b) {
		return (int) Math.sqrt(MSMath.sqr(a.x - b.x) + MSMath.sqr(a.y - b.y));
	}

	/**
	 * euclidian distance between two points.
	 * 
	 * @return |a-b|
	 */
	public static int dist(PTPoint a, PTPoint b) {
		if (a != null && b != null)
			return dist(a.toPoint(), b.toPoint());
		return 0;
	}
	
	/**
	 * Area of triangle ABC calculated as determinant
	 * 
	 * <pre>
	 *             | a.x  a.y  1 |
	 *   A = 1/2 * | b.x  b.y  1 |
	 *             | c.x  c.y  1 |
	 * </pre>
	 */
	public static int area(PTPoint a, PTPoint b, PTPoint c) {
		if (a != null && b != null && c != null)
			return area(a.toPoint(), b.toPoint(), c.toPoint());
		return 0;
	}
	
	/**
	 * Area of triangle ABC calculated as determinant
	 * 
	 * <pre>
	 *             | a.x  a.y  1 |
	 *   A = 1/2 * | b.x  b.y  1 |
	 *             | c.x  c.y  1 |
	 * </pre>
	 */
	public static int area(Point a, Point b, Point c) {
		return Math.abs(a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) / 2;
	}

	/**
	 * distance of point <i>p</i> from <em>segment</em>(not line!) AB
	 */
	public static int dist(PTPoint p, PTPoint a, PTPoint b) {
		if (p != null && a != null && b != null)
			return dist(p.toPoint(), a.toPoint(), b.toPoint());
		return 0;
	}
	
	/**
	 * distance of point <i>p</i> from <em>segment</em>(not line!) AB
	 */
	public static int dist(Point p, Point a, Point b) {
		// an inner product between two vectors is
		// 0 for the vectors being perpendicular
		// > 0 for the angle between the vectors being less than 90 degrees
		// < 0 for the angle between the vectors being between 90 and 270 degrees
		//
		// So check the position of point p relative to a,b by calculating
		// the inner product of vectors ap, ab.

		if (innerProduct(diff(p, a), diff(b, a)) < 0) {
			// beyond a
			return dist(a, p);
		} else if (innerProduct(diff(p, b), diff(a, b)) < 0) {
			// beyond b
			return dist(b, p);
		} else {
			// in the middle, then calculate area of triangle / basis / 2
			int d = dist(a, b);
			if (d == 0)
				// base is zero, so height is distance of one of the
				// "two" :) edges
				return dist(a, p);
			return area(a, b, p) / dist(a, b) * 2;
		}
	}

	/**
	 * distance of point <i>p</i> from rectangle <i>r</i>
	 */
	public static int dist(PTPoint p, Rectangle r) {
		if (p != null)
			return dist(p.toPoint(), r);
		return 0;
	}
	
	/**
	 * distance of point <i>p</i> from rectangle <i>r</i>
	 */
	public static int dist(Point p, Rectangle r) {
		// all four corners
		if (r.contains(p))
			return 0;
		Point ul = new Point(r.x, r.y);
		Point ur = new Point(r.x + r.width, r.y);
		Point ll = new Point(r.x, r.y + r.height);
		Point lr = new Point(r.x + r.width, r.y + r.height);
		// distance from upper side
		int result = dist(p, ul, ur);
		// distance from right side
		result = Math.min(result, dist(p, ur, lr));
		// distance from lower side
		result = Math.min(result, dist(p, lr, ll));
		// distance from left side
		return Math.min(result, dist(p, ll, ul));
	}

	/**
	 * angle between Point a and Point b relative to a horizontal line, i.e. the
	 * angle of the vector pointing from a to b.
	 * 
	 * @param a the first point
	 * @param b the second point
	 * @return angle: -PI < angle <= PI, clockwise being negative. to convert the
	 *         result to a value in the range 0 <= x < 2*Pi, add if (result < 0)
	 *         result += 2*Math.PI;
	 */
	public static double getAngle(PTPoint a, PTPoint b) {
		if (a != null && b != null)
			return getAngle(a.toPoint(), b.toPoint());
		return 0.0;
	}
	
	/**
	 * angle between Point a and Point b relative to a horizontal line, i.e. the
	 * angle of the vector pointing from a to b.
	 * 
	 * @param a the first point
	 * @param b the second point
	 * @return angle: -PI < angle <= PI, clockwise being negative. to convert the
	 *         result to a value in the range 0 <= x < 2*Pi, add if (result < 0)
	 *         result += 2*Math.PI;
	 */
	public static double getAngle(Point a, Point b) {
		if (a == null || b == null)
			return Double.NaN;
		// minus is because of wrong orientation of y-axis
		double t = -Math.atan((double) (b.y - a.y) / (b.x - a.x));
		if (b.x < a.x) {
			if (t <= 0)// <=> b.y <= a.y

				return t + Math.PI;

			return t - Math.PI;
		}
		return t;
	}

	/**
	 * returns the length of a vector, given as a point.
	 */
	public static double getLength(PTPoint a) {
		if (a != null)
			return getLength(a.toPoint());
		return 0.0;
	}
	
	/**
	 * returns the length of a vector, given as a point.
	 */
	public static double getLength(Point a) {
		return Math.sqrt(sqr(a.x) + sqr(a.y));
	}

}
