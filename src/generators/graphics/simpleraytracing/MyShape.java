package generators.graphics.simpleraytracing;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;

public class MyShape {

	private LinkedList<Line2D> lines;
	private Color color;
	private boolean fill;
	private String name;

	public String getName() {
		return name;
	}

	public MyShape(LinkedList<Line2D> lines, Color color, boolean fill,
			String name) {

		this.lines = lines;
		this.color = color;
		this.fill = fill;
		this.name = name;
	}

	public MyShape(LinkedList<Line2D> lines, Color color, boolean fill) {
		this.lines = lines;
		this.color = color;
		this.fill = fill;
		this.name = "";
	}

	// return list with all intersections of this shape
	public LinkedList<java.awt.geom.Point2D.Double> intersectLines(Line2D a)
			throws IllegalArgumentException {
		LinkedList<Point2D.Double> intersections = new LinkedList<Point2D.Double>();

		for (Iterator<Line2D> iterator = lines.iterator(); iterator.hasNext();) {
			Line2D type = (Line2D) iterator.next();
			try {
				intersections.add(MathUtil.intersectTwoLines(a, type));
			} catch (IllegalArgumentException e) {
				
			}
		}
		return intersections;
	}

	public LinkedList<java.awt.geom.Point2D.Double> intersectSegments(Line2D a)
			throws IllegalArgumentException {
		LinkedList<Point2D.Double> intersections = new LinkedList<Point2D.Double>();

		for (Iterator<Line2D> iterator = lines.iterator(); iterator.hasNext();) {
			Line2D type = (Line2D) iterator.next();
			try {
				intersections.add(MathUtil.intersectTwoSegments(a, type));
			} catch (IllegalArgumentException e) {

			}
		}
		return intersections;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public LinkedList<Line2D> getLines() {
		return lines;
	}

	public boolean isFill() {
		return fill;
	}

}
