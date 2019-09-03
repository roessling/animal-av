package generators.graph.helpers;
/**
 * 
 * Based on javafx.geometry.Point2D
 *
 */
public class Point2D {
	double x,y;
	
	public Point2D(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public Point2D(int x, int y) {
		this((double)x,(double)y);
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Point2D subtract(Point2D point2d) {
		return new Point2D(x-point2d.getX(), y-point2d.getY());
	}
	
	public Point2D add(Point2D point2d) {
		return new Point2D(x+point2d.getX(), y+point2d.getY());
	}

	public Point2D multiply(double d) {
		return new Point2D(x*d, y*d);
	}

	public double magnitude() {
		
		return Math.sqrt((x*x+y*y));
	}
	
    public double distance(double x1, double y1) {
    	double a = getX() - x1;
        double b = getY() - y1;
        return Math.sqrt(a * a + b * b);
    }
	
    
    public double distance(Point2D point) {
    	return distance(point.getX(), point.getY());
    }
    
    public Point2D normalize() {
        double mag = magnitude();
        if (mag == 0.0) return new Point2D(0.0, 0.0);
        return new Point2D(getX() / mag,getY() / mag);
    }

}
