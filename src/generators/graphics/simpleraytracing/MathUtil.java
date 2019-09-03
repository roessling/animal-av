package generators.graphics.simpleraytracing;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import algoanim.util.Coordinates;

public class MathUtil {

	public static Point2D.Double createPoint(double x, double y) {
		return new Point2D.Double(x, y);
	}

	public static Line2D.Double createLine(Point2D p1, Point2D p2) {
		return new Line2D.Double(p1, p2);
	}

	public static Coordinates createCoordinatesForPoint2D(Point2D p) {
		return new Coordinates((int) p.getX(), (int) p.getY());
	}

	public static Point2D.Double rotatePointHelper(Point2D p, int camerAngle,
			Point2D around) {
		double q = Math.toRadians(-camerAngle);
		if (camerAngle % 90 == 0) {
			q += Math.pow(2, -53);
		}
		double cosq = Math.cos(q);
		double sinq = Math.sin(q);
		double x1 = around.getX();
		double y1 = around.getY();
		double x = p.getX();
		double y = p.getY();
		double xRot = x1 + cosq * (x - x1) - sinq * (y - y1);
		double yRot = y1 + sinq * (x - x1) + cosq * (y - y1);
		return createPoint(xRot, yRot);
	}

	public static Line2D.Double scaleToPoint(Point2D scaleToThisPoint,
			Line2D ray) {

		return scaleFromTo(scaleToThisPoint, ray.getP1(), ray);
	}
	
	public static  Line2D.Double scaleFromTo(Point2D scaleToThisPoint, Point2D fromThisPoint, Line2D ray){
		double maxX = scaleToThisPoint.getX();
		double maxY = scaleToThisPoint.getY();

		Double v1x = ray.getP1().getX();
		Double v1y = ray.getP1().getY();
		Double v2x = ray.getP2().getX();
		Double v2y = ray.getP2().getY();

		Double richtungX = v2x - v1x;
		Double richtungY = v2y - v1y;

		{ 
		// normalize direction vector
		Double norm = Math
				.sqrt(Math.pow(richtungX, 2) + Math.pow(richtungY, 2));
		richtungX = richtungX / norm;
		richtungY /= norm;
		}
		
		
		// war vorher v2x, v2y
		Double fromThisX = fromThisPoint.getX();
		Double fromThisY = fromThisPoint.getY();
		
		if (richtungX != 0)
			richtungX *= Math.abs((maxX - fromThisX) / richtungX);
		if (richtungY != 0)
			richtungY *= Math.abs((maxY - fromThisY) / richtungY);
		
		

		fromThisX += richtungX;
		fromThisY += richtungY;

		return createLine(fromThisPoint, createPoint(fromThisX, fromThisY));
		
	}

	public static Line2D.Double scaleRay(Line2D ray, double scaleFactor) {
		Point2D p1 = ray.getP1();
		Point2D p2 = ray.getP2();

		Double v1x = p1.getX();
		Double v1y = p1.getY();
		Double v2x = p2.getX();
		Double v2y = p2.getY();

		// direction vector
		Double richtungX = v2x - v1x;
		Double richtungY = v2y - v1y;

		// normalize direction vector
		Double norm = Math
				.sqrt(Math.pow(richtungX, 2) + Math.pow(richtungY, 2));
		richtungX = richtungX / norm;
		richtungY /= norm;

		// scale the direction vector
		richtungX *= scaleFactor;
		richtungY *= scaleFactor;

		v2x += richtungX;
		v2y += richtungY;

		return createLine(ray.getP1(), createPoint(v2x, v2y));
	}

	public static Point2D.Double addP(Point2D p1, Point2D p2) {

		return new Point2D.Double(p1.getX() + p2.getX(), p2.getY() + p1.getY());
	}

	/**
	 * Schnittpunkt von gerade und strecke
	 * 
	 * @param ray
	 *            eine halb gerade
	 * @param line
	 *            eine strecke
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Point2D.Double intersectTwoLines(Line2D ray, Line2D line)
			throws IllegalArgumentException {
		// Wegen der Lesbarkeit
		double x1 = ray.getX1();
		double x2 = ray.getX2();
		double x3 = line.getX1();
		double x4 = line.getX2();
		double y1 = ray.getY1();
		double y2 = ray.getY2();
		double y3 = line.getY1();
		double y4 = line.getY2();

		// Zaehler
		double zx = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2)
				* (x3 * y4 - y3 * x4);
		double zy = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2)
				* (x3 * y4 - y3 * x4);

		// Nenner
		double n = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		// Koordinaten des Schnittpunktes
		double x = zx / n;
		double y = zy / n;

		if (line.ptSegDist(new Point2D.Double(x, y)) > 0.001) {
			throw new IllegalArgumentException();
		}
		return new Point2D.Double(x, y);

	}

	public static Point2D.Double intersectTwoSegments(Line2D ray, Line2D line)
			throws IllegalArgumentException {
		// Wegen der Lesbarkeit
		double x1 = ray.getX1();
		double x2 = ray.getX2();
		double x3 = line.getX1();
		double x4 = line.getX2();
		double y1 = ray.getY1();
		double y2 = ray.getY2();
		double y3 = line.getY1();
		double y4 = line.getY2();

		// Zaehler
		double zx = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2)
				* (x3 * y4 - y3 * x4);
		double zy = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2)
				* (x3 * y4 - y3 * x4);

		// Nenner
		double n = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		// Koordinaten des Schnittpunktes
		double x = zx / n;
		double y = zy / n;


		if (line.ptSegDist(new Point2D.Double(x, y)) > 0.001
				|| ray.ptSegDist(new Point2D.Double(x, y)) > 0.001
				|| java.lang.Double.isNaN(x) || java.lang.Double.isInfinite(y)
				|| java.lang.Double.isNaN(y) || java.lang.Double.isInfinite(x)) {
			throw new IllegalArgumentException();
		}
		return new Point2D.Double(x, y);

	}


}
