/*
 * Interp2DScattered.java
 * Arne-Tobias Rak, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.concurrent.ThreadLocalRandom;

public class Interp2DScattered implements ValidatingGenerator {
	private Language lang;
	private final int drawX=25, drawY=55;
	private final int drawW=200, drawH=200;
	private final int cbOffX=20, cbW=15;
	private final int minX=0, maxX=100;
	private final int minY=0, maxY=100;
	private final int minZ=0, maxZ=100;
	private final int scOff=100;

	CircleProperties pointProps = new CircleProperties();
	RectProperties interpPointProps = new RectProperties();
	PolylineProperties currentEdgeProps = new PolylineProperties();
	TriangleProperties triangleProps = new TriangleProperties();
	TriangleProperties currentTriangleProps = new TriangleProperties();
	TriangleProperties nextTriangleProps = new TriangleProperties();

	SourceCode sc;
	
	int[][] colorMap;
	
	class Point {
		public double x, y;
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	class Edge {
		public Coordinates c1, c2;
		public BWTriangle neighbor = null;
		public Edge(Coordinates c1, Coordinates c2) {
			this.c1 = c1;
			this.c2 = c2;
		}
		public boolean equals(Edge other) {
			return (c1.getX() == other.c1.getX() && c1.getY() == other.c1.getY() &&
					c2.getX() == other.c2.getX() && c2.getY() == other.c2.getY()) ||
					(c1.getX() == other.c2.getX() && c1.getY() == other.c2.getY() &&
					c2.getX() == other.c1.getX() && c2.getY() == other.c1.getY()) ;
		}
	}
	
	class BWTriangle {
		private Triangle primitive;
		private Coordinates[] vertices = new Coordinates[3];
		private Edge[] edges = new Edge[3];
		public BWTriangle(Coordinates c1, Coordinates c2, Coordinates c3) {
			vertices[0] = c1;
			vertices[1] = c2;
			vertices[2] = c3;
			orderCounterClockwise();
		}
		public BWTriangle(Coordinates[] cs) {
			this(cs[0], cs[1], cs[2]);
		}
		public Triangle draw(String name, TriangleProperties props) {
			if (primitive != null)
				primitive.hide();
			primitive = lang.newTriangle(vertices[0], vertices[1], vertices[2], name, null, props);
			return primitive;
		}
		public boolean pointInsideCircumcircle(Coordinates point) {
			Coordinates p1 = vertices[0];
			Coordinates p2 = vertices[1];
			Coordinates p3 = vertices[2];
			
			double a = norm2(p1);
			double b = norm2(p2);
			double c = norm2(p3);
			
			double ccx = (a * (p3.getY() - p2.getY()) + b * (p1.getY() - p3.getY()) + c * (p2.getY() - p1.getY())) / (p1.getX() * (p3.getY() - p2.getY()) + p2.getX() * (p1.getY() - p3.getY()) + p3.getX() * (p2.getY() - p1.getY())) / 2.0;
			double ccy = (a * (p3.getX() - p2.getX()) + b * (p1.getX() - p3.getX()) + c * (p2.getX() - p1.getX())) / (p1.getY() * (p3.getX() - p2.getX()) + p2.getY() * (p1.getX() - p3.getX()) + p3.getY() * (p2.getX() - p1.getX())) / 2.0;
			double ccr = Math.sqrt(((p1.getX() - ccx) * (p1.getX() - ccx)) + ((p1.getY() - ccy) * (p1.getY() - ccy)));
		    
			double dx = point.getX() - ccx;
			double dy = point.getY() - ccy;
			return Math.sqrt(dx * dx + dy * dy) <= ccr;
		}
		public boolean containsPoint(Coordinates c) {
			return containsPoint(new Point(c.getX(), c.getY()));
		}
		public boolean containsPoint(Point c) {
			double s1 = sign(c, edges[0]);
			double s2 = sign(c, edges[1]);
			double s3 = sign(c, edges[2]);
			boolean b1 = s1 >= -0.00001;
			boolean b2 = s2 >= -0.00001;
			boolean b3 = s3 >= -0.00001;
			return (b1 && b2 && b3);
		}
		private double sign(Point p1, Point p2, Point p3) {
			return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
		}
		private double sign(Point p, Edge e) {
			return sign(p, c2p(e.c1), c2p(e.c2));
		}
		private void orderCounterClockwise() {
			Point pc = center();
			
			if (!(sign(pc, c2p(vertices[0]), c2p(vertices[1])) >= -0.00001)) {
				Coordinates temp = vertices[0];
				vertices[0] = vertices[1];
				vertices[1] = temp;
			}

			edges[0] = new Edge(vertices[0], vertices[1]);
			edges[1] = new Edge(vertices[1], vertices[2]);
			edges[2] = new Edge(vertices[2], vertices[0]);
		}
		public Point center() {
			return new Point((vertices[0].getX() + vertices[1].getX() + vertices[2].getX()) / 3.0f,
					(vertices[0].getY() + vertices[1].getY() + vertices[2].getY()) / 3.0f);
		}
		public boolean hasVertex(Coordinates coord) {
			for (Coordinates c : vertices) {
				if (c.getX() == coord.getX() && c.getY() == coord.getY())
					return true;
			}
			return false;
		}
		public boolean hasEdge(Edge edge) {
			for (Edge e : edges)
				if (e.equals(edge))
					return true;
			return false;
		}
		public Triangle getPrimitive() {
			return primitive;
		}
	}
	private double norm2(Coordinates vector) {
		return vector.getX() * vector.getX() + vector.getY() * vector.getY();
	}

	private Point c2p(Coordinates c) {
		return new Point(c.getX(), c.getY());
	}
	
	public Interp2DScattered() {
        lang = new AnimalScript("Linear Interpolation of 2D Scattered Data", "Arne-Tobias Rak", 800, 600);
		lang.setStepMode(true);
		setProperties();
	}
	
	private Vector<BWTriangle> triangulate(int[][] pointList) {
		Vector<BWTriangle> triangulation = new Vector<BWTriangle>();		
		
		// compute coordinates of super triangle vertices
		final Coordinates c1 = new Coordinates(-1000, -1000);
		final Coordinates c2 = new Coordinates(1000, -1000);
		final Coordinates c3 = new Coordinates(0, 1000);

		// create super triangle
		BWTriangle superTriangle = new BWTriangle(c1, c2, c3);
		
		// add super triangle to triangulation
		triangulation.add(superTriangle);
		
		for (int[] p : pointList) {
			Coordinates point = new Coordinates(p[0], p[1]);
			Vector<BWTriangle> badTriangles = new Vector<BWTriangle>();
			for (int i = 0; i < triangulation.size(); i++) {
				if (triangulation.get(i).pointInsideCircumcircle(point)) {
					BWTriangle badTriangle = new BWTriangle(triangulation.remove(i--).vertices);
					badTriangles.add(badTriangle);
				}
			}
			
			// create polygon (boundary of polygonal hole)
			Vector<Edge> polygon = new Vector<Edge>();
			for (int i = 0; i < badTriangles.size(); i++) {
				for (Edge edge : badTriangles.get(i).edges) {
					boolean otherBadTriangleContainsEdge = false;
					for (int j = 0; j < badTriangles.size(); j++) {
						if (i != j && badTriangles.get(j).hasEdge(edge)) {
							otherBadTriangleContainsEdge = true;
							break;
						}
					}

					if (!otherBadTriangleContainsEdge){
						polygon.add(edge);
					}
				}
			}
			
			for (Edge edge : polygon) {
				triangulation.add(new BWTriangle(edge.c1, edge.c2, point));
			}
		}

		for (int i = 0; i < triangulation.size(); i++) {
			BWTriangle triangle = triangulation.get(i);
			if (triangle.hasVertex(c1) || triangle.hasVertex(c2) || triangle.hasVertex(c3)) {
				triangulation.remove(i--);
			}
		}
		
		// find neighbors
		for (int i=0; i < triangulation.size(); i++) {
			BWTriangle t1 = triangulation.get(i);
			for (int ei1=0; ei1 < 3; ei1++) {
				Edge e1 = t1.edges[ei1];
				if (e1.neighbor != null) continue;
				for (int j=0; j < triangulation.size(); j++) {
					if (i == j) continue;
					BWTriangle t2 = triangulation.get(j);
					for (int ei2=0; ei2 < 3; ei2++) {
						Edge e2 = t2.edges[ei2];
						if (e1.equals(e2)) {
							e1.neighbor = t2;
							e2.neighbor = t1;
						}
					}
				}
			}
		}
		
		return triangulation;
	}

	private void animationIntro() {
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
	    
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
	    
		lang.newText(new Coordinates(8, 8), "Interpolation of 2D Scattered Data", "title", null, titleProps);
	    lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5, 5, "title", "SE"), "titleRect", null, rectProps);
	    
	    InfoBox desc = new InfoBox(lang, new Coordinates(8, 50), 11, "Description");
	    Vector<String> descText = new Vector<>();
	    descText.add("With scattered sampling points no regular grid");
	    descText.add("spanning over the data can be computed. Thus,");
	    descText.add("finding the closest sample points to an arbitrary ");
	    descText.add("point is not trivial. To accomplish this efficiently,");
	    descText.add("a data structure encoding the spatial relationship of");
	    descText.add("the sampling points is needed. State of the art");
	    descText.add("algorithms, such as Matlab scatteredInterpolant, use");
	    descText.add("a Delaunay triangulation for this purpose. Here, an");
	    descText.add("algorithm using edge hopping to find an arbitrary point");
	    descText.add("inside a triangulation to then interpolate its value ");
	    descText.add("using barycentric coordinates is shown.");
	    desc.setText(descText);
		lang.nextStep();
		desc.hide();
	}
	
	private void animationOutro() {
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
	    
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
	    
		lang.newText(new Coordinates(8, 8), "Interpolation of 2D Scattered Data", "title", null, titleProps);
	    lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5, 5, "title", "SE"), "titleRect", null, rectProps);
	    
	    InfoBox desc = new InfoBox(lang, new Coordinates(8, 50), 10, "Outro");
	    Vector<String> descText = new Vector<>();	    
	    descText.add("Especially in large triangulation meshes, hopping over ");
	    descText.add("triangle edges to find the triangle containing arbitrary ");
	    descText.add("interpolation points is much faster than iterating over ");
	    descText.add("all triangles. Higher performance may be achieved");
	    descText.add("by choosing the previously found triangle as a starting");
	    descText.add("point for the edge hopping algorithm, instead of randomizing");
	    descText.add("the choice, as interpolation points often are close to each");
	    descText.add("other in sequential interpolation calls.");
	    descText.add("");
	    descText.add("");
	    desc.setText(descText);
	}
	
	private void setSourceCode() {

	    SourceCodeProperties scProps = new SourceCodeProperties();
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    
		sc = lang.newSourceCode(new Offset(scOff, 0, "cbArea", "NE"), "src", null, scProps);
		sc.addCodeLine("function interpolate (pointList, values, interpPointList)", null, 0, null);
		sc.addCodeLine("triangulation := triangulate pointList", null, 2, null);
		sc.addCodeLine("for each point in interpPointList", null, 2, null);
		sc.addCodeLine("currentTriangle := random triangle from triangulation", null, 4, null);
		sc.addCodeLine("while point is not inside currentTriangle", null, 4, null);
		sc.addCodeLine("nextTriangle := null", null, 6, null);
		sc.addCodeLine("for each edge in currentTriangle", null, 6, null);
		sc.addCodeLine("if point is inside outer half-plane created by edge", null, 8, null);
		sc.addCodeLine("nextTriangle = neighbor triangle of edge", null, 10, null);
		sc.addCodeLine("break", null, 10, null);
		sc.addCodeLine("if nextTriangle is null", null, 6, null);
		sc.addCodeLine("skip this interpolation point // point outside triangulation", null, 8, null);
		sc.addCodeLine("currentTriangle = nextTriangle", null, 6, null);
		sc.addCodeLine("b := barycentric coordinates of point in currentTriangle", null, 4, null);
		sc.addCodeLine("interpolated value of point := b * values of currentTriangle", null, 4, null);
	}

	private void legendCircle(int offx, int offy, String name, CircleProperties props, String text) {
		lang.newCircle(new Offset(offx, offy, "legendArea", "NW"), 3, name, null, props);
		lang.newText(new Offset(20, -5, name, "NW"), text, name + "text", null);
	}
	
	private void legendTriangle(int offx, int offy, String name, TriangleProperties props, String text) {
		lang.newTriangle(new Offset(offx - 4, offy + 4, "legendArea", "NW"),
				new Offset(offx + 4, offy + 4, "legendArea", "NW"),
				new Offset(offx, offy - 4, "legendArea", "NW"), name, null, props);
		lang.newText(new Offset(20, -5, name, "NW"), text, name + "text", null);
	}
	
	private void legendLine(int offx, int offy, String name, PolylineProperties props, String text) {
		lang.newPolyline(new Offset[] { new Offset(offx-4, offy, "legendArea", "NW"),
				new Offset(offx+4, offy, "legendArea", "NW") }, name, null, props);
		lang.newText(new Offset(20, -5, name, "NW"), text, name + "text", null);
	}
	
	private void legendRect(int offx, int offy, String name, RectProperties props, String text) {
		lang.newRect(new Offset(offx - 3, offy - 3, "legendArea", "NW"), 
				new Offset(offx + 3, offy + 3, "legendArea", "NW"), name, null, props);
		lang.newText(new Offset(20, -5, name, "NW"), text, name + "text", null);
	}
	
	private void legend() {
		//final Coordinates drawStart = new Coordinates(superDrawX+superDrawW+50, superDrawY+superDrawH+50);
		final Offset drawStart = new Offset(0, 10, "src", "SW");
		final Offset drawEnd = new Offset(375, 90, "src", "SW");

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		//rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		//rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(225, 225, 225));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
		lang.newRect(drawStart, drawEnd, "legendArea", null, rectProps);
		
		legendCircle(20, 20, "legend1", pointProps, "Point");
		legendRect(20, 40, "legend2", interpPointProps, "Interpolation Point");
		legendLine(20, 60, "legend4", currentEdgeProps, "Current Edge");
		legendTriangle(200, 20, "legend5", triangleProps, "Triangulation Triangle");
		legendTriangle(200, 40, "legend6", currentTriangleProps, "Current Triangle");
		legendTriangle(200, 60, "legend7", nextTriangleProps, "Next Triangle");
	}
	
	public void interpolate(int[][] pointList, int[] values, int[][] interpPointList) {
		int[][] pointList2 = new int[pointList.length][pointList[0].length];
		animationIntro();
		
		Vector<Circle> circleList = new Vector<Circle>();
		Map<Integer, Map<Integer, Integer>> pointValMap = new HashMap<Integer, Map<Integer,Integer>>();
		BWTriangle inside=null;
		
		Coordinates drawStart = new Coordinates(drawX, drawY);
		Coordinates drawEnd = new Coordinates(drawX + drawW, drawY + drawH);
		lang.newRect(drawStart, drawEnd, "drawArea", new Hidden());
		
		drawColorBar();
		setSourceCode();
		legend();
		
		sc.highlight(0);
		// create point list and draw circles
		for (int i = 0; i < pointList.length; i++) {
			int cx = drawStart.getX() + (int)(((float)pointList[i][0] - minX) / (maxX - minX) * drawW);
			int cy = drawStart.getY() + (int)(((float)pointList[i][1] - minY) / (maxY - minY) * drawH);
			pointList2[i][0] = cx; pointList2[i][1] = cy;
			if (!pointValMap.containsKey(cx))
				pointValMap.put(cx, new HashMap<Integer,Integer>());
			pointValMap.get(cx).put(cy, values[i]);
			pointProps.set(AnimationPropertiesKeys.FILL_PROPERTY, colorFromValue(values[i]));
			circleList.add(lang.newCircle(new Coordinates(cx, cy), 4, "point" + i, null, pointProps));
		}

		lang.nextStep();
		sc.toggleHighlight(0,1);
		Vector<BWTriangle> triangulation = triangulate(pointList2);
		for (BWTriangle t : triangulation)
			t.draw("tri", triangleProps);
		
		lang.nextStep();
		sc.unhighlight(1);
		int counter = 1;
		for (int[] point : interpPointList) {
			sc.highlight(2);
			
			int px = drawStart.getX() + (int)(((float)point[0] - minX) / (maxX - minX) * drawW);
			int py = drawStart.getY() + (int)(((float)point[1] - minY) / (maxY - minY) * drawH);
			Rect interpPoint = lang.newRect(new Coordinates(px-3, py-3), new Coordinates(px+3, py+3), "interpolationPoint", null, interpPointProps);
			
			lang.nextStep("Point " + counter++);
			sc.unhighlight(2);
			
			inside = searchFastAnimated(triangulation, new Point(px, py));
			if (inside == null) {
				interpPoint.hide();
				continue;
			}
			
			sc.highlight(13);
			double[] b = computeBarycentric(px, py, inside.vertices);
			String bs = String.format(Locale.ROOT, "(%.2f, %.2f, %.2f)", b[0], b[1], b[2]);
			String txt = "Barycentric Coordinates: " + bs;
			Text bary = lang.newText(new Offset(0, 20, "drawArea", "SW"), txt, "bary", null);
			
			lang.nextStep();
			sc.toggleHighlight(13, 14);
			int[] currentTriVals = { pointValMap.get(inside.vertices[0].getX()).get(inside.vertices[0].getY()),
					pointValMap.get(inside.vertices[1].getX()).get(inside.vertices[1].getY()),
					pointValMap.get(inside.vertices[2].getX()).get(inside.vertices[2].getY()) };
			double val = b[0] * currentTriVals[0] +	b[1] * currentTriVals[1] + b[2] * currentTriVals[2] ;
			Color c = colorFromValue(val);
			interpPoint.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, c, null, null);

			String vals = String.format(Locale.ROOT, "(%d, %d, %d)", currentTriVals[0], currentTriVals[1], currentTriVals[2]);
			txt = String.format(Locale.ROOT, "Result: %s * %s^T = %.2f", bs, vals, val);
			Text valComp = lang.newText(new Offset(0, 5, "bary", "SW"), txt, "valComp", null);
			
			lang.nextStep();
			sc.unhighlight(14);
			bary.hide();
			valComp.hide();
			inside.draw("triangle", triangleProps);
		}
		
		sc.hide();
		lang.newText(new Offset(0, 0, "src", "NW"), "Interpolating the whole triangulated surface.", "hellothere", null);
		
		for (int x = 0; x < drawW; x+=5) {
			for (int y = 0; y < drawH; y+=5) {
				Point p = new Point(drawX+x,drawY+y);
				inside = searchFast(triangulation, p);
				if (inside != null) {
					double[] b = computeBarycentric(p.x, p.y, inside.vertices);
					double val = b[0] * pointValMap.get(inside.vertices[0].getX()).get(inside.vertices[0].getY()) +
							b[1] * pointValMap.get(inside.vertices[1].getX()).get(inside.vertices[1].getY()) +
							b[2] * pointValMap.get(inside.vertices[2].getX()).get(inside.vertices[2].getY()) ;
					Color c = colorFromValue(val);

					CircleProperties rectProps = new CircleProperties();
					rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, c);
					rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, c);
					rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
					rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
					lang.newCircle(new Coordinates((int)p.x, (int)p.y), 3, "pixel_" + x + "," + y, null, rectProps);
				}
			}
		}
		
		lang.nextStep("Interpolated Surface");
		lang.hideAllPrimitives();
		animationOutro();
	}
	
	double[] computeBarycentric(double x, double y, Coordinates[] vertices) {
		double f = (vertices[1].getY() - vertices[2].getY()) * 
				(vertices[0].getX() - vertices[2].getX()) +
				(vertices[2].getX() - vertices[1].getX()) *
				(vertices[0].getY() - vertices[2].getY());
		if (f == 0)
			return new double[] { 1, 0, 0 };
		
		double wp1 = ((vertices[1].getY() - vertices[2].getY()) * (x - vertices[2].getX()) + (vertices[2].getX() - vertices[1].getX()) * (y - vertices[2].getY())) / f;
		double wp2 = ((vertices[2].getY() - vertices[0].getY()) * (x - vertices[2].getX()) + (vertices[0].getX() - vertices[2].getX()) * (y - vertices[2].getY())) / f;
		double wp3 = 1 - wp1 - wp2;
		
		return new double[] { wp1, wp2, wp3 };
	}
	
	BWTriangle searchFast(Vector<BWTriangle> triangulation, Point p) {
		int randomIndex = ThreadLocalRandom.current().nextInt(0, triangulation.size());
		BWTriangle currentTriangle = triangulation.get(randomIndex);
		int maxIterations = triangulation.size();
		while (!currentTriangle.containsPoint(p)) {
			if (maxIterations-- < 0)
				return null;
			
			BWTriangle nextTriangle = null;
			for (Edge edge : currentTriangle.edges) {
				if (currentTriangle.sign(p, edge) < -0.00001) {
					nextTriangle = edge.neighbor;
					break;
				}
			}
			if (nextTriangle == null)
				return null;

			currentTriangle = nextTriangle;
		}
		return currentTriangle;
	}
	
	BWTriangle searchFastAnimated(Vector<BWTriangle> triangulation, Point p) {
		sc.highlight(3);
		int randomIndex = ThreadLocalRandom.current().nextInt(0, triangulation.size());
		BWTriangle currentTriangle = triangulation.get(randomIndex);
		currentTriangle.draw("current", currentTriangleProps);
		lang.nextStep();
		sc.toggleHighlight(3, 4);
		int maxIterations = triangulation.size();
		while (!currentTriangle.containsPoint(p)) {
			lang.nextStep();
			sc.toggleHighlight(4, 6);
			if (maxIterations-- < 0)
				return null;
			
			BWTriangle nextTriangle = null;
			for (Edge edge : currentTriangle.edges) {
				Polyline currentEdge = lang.newPolyline(new Coordinates[] { edge.c1, edge.c2 }, "currentEdge", null, currentEdgeProps);
				lang.nextStep();
				Polyline halfspaceLine = drawHalfspace(edge);
				sc.toggleHighlight(6, 7);
				if (currentTriangle.sign(p, edge) < -0.00001) {
					lang.nextStep();
					sc.toggleHighlight(7,8); sc.highlight(9);
					if (nextTriangle != null) nextTriangle.draw("triangle", triangleProps);
					nextTriangle = edge.neighbor;
					if (nextTriangle != null) nextTriangle.draw("nextTriangle", nextTriangleProps);
					lang.nextStep();
					sc.unhighlight(7); sc.unhighlight(8); sc.unhighlight(9);
					halfspaceLine.hide();
					currentEdge.hide();
					break;
				}
				lang.nextStep();
				halfspaceLine.hide();
				currentEdge.hide();
				sc.unhighlight(7); sc.unhighlight(8); sc.highlight(6);
			}
			sc.toggleHighlight(6,10);
			lang.nextStep();
			if (nextTriangle == null) {
				sc.toggleHighlight(10, 11);
				lang.nextStep();
				sc.unhighlight(11);
				currentTriangle.draw("triangle", triangleProps);
				return null;
			}

			sc.toggleHighlight(10,12);
			currentTriangle.draw("tri", triangleProps);
			currentTriangle = nextTriangle;

			currentTriangle.draw("current", currentTriangleProps);
			lang.nextStep();
			sc.toggleHighlight(12, 4);
		}
		lang.nextStep();
		sc.unhighlight(4);
		return currentTriangle;
	}
	
	private Polyline drawHalfspace(Edge edge) {
		double m = (edge.c2.getY() - edge.c1.getY()) / (double)(edge.c2.getX() - edge.c1.getX());
		double b = edge.c2.getY() - m * edge.c2.getX();
		
		int xmin = drawX, xmax = drawX+drawW;
		int ymin = drawY, ymax = drawY+drawH;
		
		double y1 = m * xmax + b;
		double y2 = m * xmin + b;
		double x1 = -(b-ymin)/m;
		double x2 = -(b-ymax)/m;
		
		Vector<Coordinates> nodes = new Vector<>();
		if (y1 >= ymin && y1 <= ymax) {
			nodes.add(new Coordinates(xmax, (int) y1));
		}
		if (y2 >= ymin && y2 <= ymax) {
			nodes.add(new Coordinates(xmin, (int) y2));
		}
		if (x1 >= xmin && x1 <= xmax) {
			nodes.add(new Coordinates((int) x1, ymin));
		}
		if (x2 >= xmin && x2 <= xmax) {
			nodes.add(new Coordinates((int) x2, ymax));
		}
		
		return lang.newPolyline(nodes.toArray(new Coordinates[0]), "halfspaceline", null, currentEdgeProps);
	}
	
	private void drawColorBar() {
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		Offset cbStart = new Offset(cbOffX, 0, "drawArea", "NE");
		Offset cbEnd = new Offset(cbOffX+cbW, 0, "drawArea", "SE");
		lang.newRect(cbStart, cbEnd, "cbArea", null, rectProps);
		
		for (int i = 1; i < drawH; i++) {
			double val = maxZ - ((double)i / drawH * (maxZ - minZ) + minZ);
			Color c = colorFromValue(val);
			rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, c);
			rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, c);
			lang.newRect(new Offset(1, i, "cbArea", "NW"), new Offset(cbW-1, i, "cbArea", "NW"), "cbb"+i, null, rectProps);
		}
		
		lang.newText(new Offset(0, -20, "cbArea", "NW"), "Value", "cbtitle", null);
		lang.newText(new Offset(2, 0, "cbArea", "NE"), ""+maxZ, "cblabel1", null);
		lang.newText(new Offset(2, -10, "cbArea", "SE"), ""+minZ, "cblabel2", null);
	}
	
	private Color colorFromValue(double val) {
		double normalizedValue = (double)(val - minZ) / maxZ;
		int li = colorMap.length - 1;
		double x = normalizedValue * li;
		int cmi = (int)(x);
		if (cmi == li)
			return new Color(colorMap[li][0], colorMap[li][1], colorMap[li][2]);
		int r1 = colorMap[cmi][0]; int r2 = colorMap[cmi+1][0];
		int g1 = colorMap[cmi][1]; int g2 = colorMap[cmi+1][1];
		int b1 = colorMap[cmi][2]; int b2 = colorMap[cmi+1][2];
		int r = (int)(r1 + (x - cmi) * (r2 - r1));
		int g = (int)(g1 + (x - cmi) * (g2 - g1));
		int b = (int)(b1 + (x - cmi) * (b2 - b1));
		return new Color(r, g, b);
	}

	private void setProperties() {
		
		pointProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		pointProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		pointProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		pointProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		interpPointProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		interpPointProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		interpPointProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		interpPointProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);		
		
		triangleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
	}
	

    public void init(){
        lang = new AnimalScript("Linear Interpolation of 2D Scattered Data", "Arne-Tobias Rak", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        int[][] pointList = (int[][])primitives.get("pointList");
        int[] values = (int[])primitives.get("values");
        int[][] interpPointList = (int[][])primitives.get("interpPointList");
        
        nextTriangleProps = (TriangleProperties)props.getPropertiesByName("nextTriangle");
        currentTriangleProps = (TriangleProperties)props.getPropertiesByName("currentTriangle");
        currentEdgeProps = (PolylineProperties)props.getPropertiesByName("currentEdge");
        colorMap = (int[][])primitives.get("colorMap");
        
        interpolate(pointList, values, interpPointList);
        return lang.toString();
    }

    public String getName() {
        return "Linear Interpolation of 2D Scattered Data";
    }

    public String getAlgorithmName() {
        return "Interpolation of 2D Scattered Data";
    }

    public String getAnimationAuthor() {
        return "Arne-Tobias Rak";
    }

    public String getDescription(){
        return "With scattered sampling points no regular grid"
 +"\n"
 +"spanning over the data can be computed. Thus,"
 +"\n"
 +"finding the closest sample points to an arbitrary "
 +"\n"
 +"point is not trivial. To accomplish this efficiently,"
 +"\n"
 +"a data structure encoding the spatial relationship of"
 +"\n"
 +"the sampling points is needed. State of the art"
 +"\n"
 +"algorithms, such as Matlab scatteredInterpolant, use"
 +"\n"
 +"a Delaunay triangulation for this purpose. Here, an"
 +"\n"
 +"algorithm using edge hopping to find an arbitrary point"
 +"\n"
 +"inside a triangulation to then interpolate its value "
 +"\n"
 +"using barycentric coordinates is shown.";
    }

    public String getCodeExample(){
        return "function interpolate (pointList, values, interpPointList)"
 +"\n"
 +"    triangulation := triangulate pointList"
 +"\n"
 +"    for each point in interpPointList"
 +"\n"
 +"        currentTriangle := random triangle from triangulation"
 +"\n"
 +"        while point is not inside currentTriangle"
 +"\n"
 +"            nextTriangle := null"
 +"\n"
 +"            for each edge in currentTriangle"
 +"\n"
 +"                if point is inside outer half-plane created by edge"
 +"\n"
 +"                    nextTriangle = neighbor triangle of edge"
 +"\n"
 +"                    break"
 +"\n"
 +"            if nextTriangle is null"
 +"\n"
 +"                skip this interpolation point // point outside triangulation"
 +"\n"
 +"            currentTriangle = nextTriangle"
 +"\n"
 +"        b := barycentric coordinates of point in currentTriangle"
 +"\n"
 +"        interpolated value of point := b * values of currentTriangle";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
        int[][] pointList = (int[][])primitives.get("pointList");
        int[] values = (int[])primitives.get("values");
        int[][] interpPointList = (int[][])primitives.get("interpPointList");

        if (pointList.length != values.length)
        	return false;
        
        if (pointList[0].length != 2)
        	return false;
        
        if (interpPointList[0].length != 2)
        	return false;
        
		return true;
	}
	
	public static void main(String[] args) {
		Generator generator = new Interp2DScattered();
		Animal.startGeneratorWindow(generator);
	}
}
