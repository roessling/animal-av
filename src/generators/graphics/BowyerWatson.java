/*
 * BowyerWatson.java
 * Arne-Tobias Rak, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Circle;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Offset;


public class BowyerWatson implements Generator {

	private Language lang;
	private final int minX=0, maxX=100, minY=0, maxY=100;
	private final int superDrawX=75, superDrawY=50;
	private int superDrawW=400, superDrawH=300;
	private final int superDrawOffset=50;
	
	private Circle circum;
	
	CircleProperties currentPointProps = new CircleProperties();
	CircleProperties normalPointProps = new CircleProperties();
	CircleProperties circumProps = new CircleProperties();
	TriangleProperties triangleProps = new TriangleProperties();
	TriangleProperties badTriangleProps = new TriangleProperties();
	TriangleProperties currentTriangleProps = new TriangleProperties();
	TriangleProperties superTriangleProps = new TriangleProperties();
	TriangleProperties triangleSharingEdgeProps = new TriangleProperties();
	PolylineProperties currentEdgeProps = new PolylineProperties();
	PolygonProperties polygonProps = new PolygonProperties();
	PolylineProperties polygonLineProps = new PolylineProperties();
	
	SourceCode sc;

	class Edge {
		public Coordinates c1, c2;
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
	
	class BWPolygon {
		private Polygon primitive;
		private Vector<Edge> edges;
		public BWPolygon() {
			edges = new Vector<Edge>();
		}
		public void addEdge(Edge edge) {
			edges.add(edge);
		}
		public void createPrimitive(String name, PolygonProperties props) {
			@SuppressWarnings("unchecked")
			Coordinates[] nodes = edgesToNodes((Vector<Edge>) edges.clone());
			if (nodes.length < 3)
				return;
			try {
				primitive = lang.newPolygon(nodes, name, null, props);
			} catch (NotEnoughNodesException e) {
				e.printStackTrace();
			}
		}
		private Coordinates[] edgesToNodes(Vector<Edge> edges) {
			if (edges.size() < 3)
				return new Coordinates[0];
			
			Vector<Coordinates> nodes = new Vector<Coordinates>();
			
			nodes.add(edges.firstElement().c1);
			nodes.add(edges.firstElement().c2);
			edges.remove(0);
			
			while (!edges.isEmpty()) {
				for (int i = 0; i < edges.size(); i++) {
					if (nodes.lastElement().equals(edges.get(i).c1))
						nodes.addElement(edges.remove(i).c2);
					else if (nodes.lastElement().equals(edges.get(i).c2))
						nodes.addElement(edges.remove(i).c1);
				}
			}
			
			return nodes.toArray(new Coordinates[0]);
		}
		public Polygon getPrimitive() {
			return primitive;
		}
	}
	
	class BWTriangle {
		private Triangle primitive;
		private Coordinates[] vertices = new Coordinates[3];
		private Edge[] edges = new Edge[3];
		public BWTriangle(Coordinates c1, Coordinates c2, Coordinates c3, String name, TriangleProperties props) {
			primitive = lang.newTriangle(c1, c2, c3, name, null, props);
			vertices[0] = c1;
			vertices[1] = c2;
			vertices[2] = c3;
			edges[0] = new Edge(c1, c2);
			edges[1] = new Edge(c2, c3);
			edges[2] = new Edge(c3, c1);
		}
		public BWTriangle(Coordinates[] cs, String name, TriangleProperties props) {
			this(cs[0], cs[1], cs[2], name, props);
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
		    
		    if (circum != null)
		    	circum.hide();
		    circum = lang.newCircle(new Coordinates((int)ccx, (int)ccy), (int) ccr, "circum", null, circumProps);

		    double dx = point.getX() - ccx;
		    double dy = point.getY() - ccy;
		    return Math.sqrt(dx * dx + dy * dy) <= ccr;
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
	
	public BowyerWatson() {
		lang = new AnimalScript("Bowyer-Watson Delaunay Triangulation", "Arne-Tobias Rak", 1280, 720);
		lang.setStepMode(true);
		setProperties();
	}
	
	private void setSourceCode() {

	    SourceCodeProperties scProps = new SourceCodeProperties();
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    
		sc = lang.newSourceCode(new Coordinates(superDrawX + superDrawW, superDrawY), "src", null, scProps);
		sc.addCodeLine("function triangulate (pointList)", null, 0, null);
		sc.addCodeLine("triangulation := empty list of triangles", null, 2, null);
		sc.addCodeLine("add super-triangle to triangulation", null, 2, null);
		sc.addCodeLine("for each point in pointList", null, 2, null);
		sc.addCodeLine("badTriangles := empty list of triangles", null, 4, null);
		sc.addCodeLine("for each triangle in triangulation", null, 4, null);
		sc.addCodeLine("if point is inside circumcircle of triangle", null, 6, null);
		sc.addCodeLine("add triangle to badTriangles", null, 8, null);
		sc.addCodeLine("remove triangle from triangulation", null, 8, null);
		sc.addCodeLine("polygon := empty list of edges", null, 4, null);
		sc.addCodeLine("for each edge in each triangle in badTriangles", null, 4, null);
		sc.addCodeLine("if edge is not shared by any other triangles in badTriangles", null, 6, null);
		sc.addCodeLine("add edge to polygon", null, 8, null);
		sc.addCodeLine("for each edge in polygon", null, 4, null);
		sc.addCodeLine("newTriangle := connect edge vertices to point", null, 6, null);
		sc.addCodeLine("add newTriangle to triangulation", null, 6, null);
		sc.addCodeLine("for each triangle in triangulation", null, 2, null);
		sc.addCodeLine("if triangle has a vertex from original super-triangle", null, 4, null);
		sc.addCodeLine("remove triangle from triangulation", null, 6, null);
	}
	
	private void animationIntro() {
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
	    
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
	    
		lang.newText(new Coordinates(8, 8), "Bowyer-Watson Delaunay Triangulation", "title", null, titleProps);
	    lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5, 5, "title", "SE"), "titleRect", null, rectProps);
	    
	    InfoBox desc = new InfoBox(lang, new Coordinates(8, 50), 10, "Description");
	    Vector<String> descText = new Vector<>();
	    descText.add("The Bowyer-Watson algorithm is a strategy for computing the Delaunay");
	    descText.add("triangulation (DT) of a finite set of points. A triangulation is a subdivision");
	    descText.add("of a planar object into triangles, such as a set of points. In a DT,");
	    descText.add("every triangle's circumcircle contains none of the points that were used");
	    descText.add("to compute the triangulation. The Bowyer-Watson algorithm works incrementally,");
	    descText.add("by sequentially adding points to a valid DT of a subset of the used points.");
	    descText.add("The initial DT is a super-triangle that is large enough to completely");
	    descText.add("contain all points in the point list. After adding a point, all triangles");
	    descText.add("whose circumcircle contain this point are removed. This results in a");
	    descText.add("polygonal hole, which is retriangulated using the newly inserted point.");
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
	    
		lang.newText(new Coordinates(8, 8), "Bowyer-Watson Delaunay Triangulation", "title", null, titleProps);
	    lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5, 5, "title", "SE"), "titleRect", null, rectProps);
	    
	    InfoBox desc = new InfoBox(lang, new Coordinates(8, 50), 10, "Outro");
	    Vector<String> descText = new Vector<>();	    
	    descText.add("Instead of iterating over all triangles to locate the ones to remove,");
	    descText.add("their connectivity can be used to do this efficiently. This can result");
	    descText.add("in O(N log N) operations to triangulate N points, although special ");
	    descText.add("cases exist where complexity would reach O(N^2). The computational");
	    descText.add("efficiency of the algorithm can be increased by pre-computing the");
	    descText.add("circumcircles of all triangles at the expense of additional memory usage.");
	    descText.add("");
	    descText.add("");
	    desc.setText(descText);
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
	
	private void legendPolygon(int offx, int offy, String name, PolygonProperties props, String text) {
		try {
			lang.newPolygon(new Offset[] { new Offset(offx - 6, offy + 4, "legendArea", "NW"),
					new Offset(offx + 6, offy + 4, "legendArea", "NW"),
					new Offset(offx + 3, offy - 4, "legendArea", "NW"),
					new Offset(offx - 3, offy - 4, "legendArea", "NW"),
					new Offset(offx - 6, offy + 4, "legendArea", "NW") }, name, null, props);
			lang.newText(new Offset(20, -5, name, "NW"), text, name + "text", null);
		} catch (NotEnoughNodesException e) {
			e.printStackTrace();
		}
	}
	
	private void legendLine(int offx, int offy, String name, PolylineProperties props, String text) {
		lang.newPolyline(new Offset[] { new Offset(offx-4, offy, "legendArea", "NW"),
				new Offset(offx+4, offy, "legendArea", "NW") }, name, null, props);
		lang.newText(new Offset(20, -5, name, "NW"), text, name + "text", null);
	}
	
	private void legend() {
		//final Coordinates drawStart = new Coordinates(superDrawX+superDrawW+50, superDrawY+superDrawH+50);
		final Coordinates drawStart = new Coordinates(superDrawX, superDrawY+superDrawH+50);
		final Coordinates drawEnd = new Coordinates(drawStart.getX()+400, drawStart.getY()+125);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		//rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		//rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(225, 225, 225));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
		lang.newRect(drawStart, drawEnd, "legendArea", null, rectProps);
		
		legendCircle(20, 20, "legend1", normalPointProps, "Point");
		legendCircle(20, 40, "legend2", currentPointProps, "Current Point");
		legendCircle(20, 60, "legend3", circumProps, "Current Circumcircle");
		legendPolygon(20, 80, "legend8", polygonProps, "Polygonal Hole");
		legendLine(20, 100, "legend9", currentEdgeProps, "Current Edge");
		legendTriangle(200, 20, "legend4", triangleProps, "Triangulation Triangle");
		legendTriangle(200, 40, "legend6", currentTriangleProps, "Current Triangle");
		legendTriangle(200, 60, "legend5", badTriangleProps, "Bad Triangle");
		legendTriangle(200, 80, "legend7", triangleSharingEdgeProps, "Triangle Sharing Current Edge");
	}
	
	public void triangulate(int[][] coords) {
		animationIntro();
		
		Vector<BWTriangle> triangulation = new Vector<BWTriangle>();
		Vector<Circle> circleList = new Vector<Circle>();
		
		setSourceCode();
		sc.highlight(0);
		legend();
		
		// compute coordinates of super triangle vertices
		final Coordinates c1 = new Coordinates(superDrawX+superDrawW/2, superDrawY);
		final Coordinates c2 = new Coordinates(superDrawX, superDrawY+superDrawH);
		final Coordinates c3 = new Coordinates(superDrawX+superDrawW, superDrawY+superDrawH);
		
		// compute coordinates of drawing area
		final Coordinates drawStart = new Coordinates((c1.getX()+c2.getX())/2+superDrawOffset, (c1.getY()+c2.getY())/2);
		final Coordinates drawEnd = new Coordinates(drawStart.getX()+superDrawW/2-superDrawOffset*2, c2.getY()-superDrawOffset);
		final int drawW = drawEnd.getX() - drawStart.getX();
		final int drawH = drawEnd.getY() - drawStart.getY();

		// create draw area
		lang.newRect(drawStart, drawEnd, "drawArea", new Hidden());

		// create point list and draw circles
		for (int i = 0; i < coords.length; i++) {
			int cx = drawStart.getX() + (int)(((float)coords[i][0] - minX) / (maxX - minX) * drawW);
			int cy = drawStart.getY() + (int)(((float)coords[i][1] - minY) / (maxY - minY) * drawH);
			circleList.add(lang.newCircle(new Coordinates(cx, cy), 3, "point" + i, null, normalPointProps));
		}
		
		lang.nextStep();
		sc.toggleHighlight(0, 2);

		// create super triangle
		BWTriangle superTriangle = new BWTriangle(c1, c2, c3, "superTriangle", superTriangleProps);
		
		// add super triangle to triangulation
		triangulation.add(superTriangle);
		
		lang.nextStep();
		sc.unhighlight(2);
		int counter = 1;
		for (Circle c : circleList) {
			sc.highlight(3);
			c.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, (Color) currentPointProps.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
			Coordinates point = (Coordinates) c.getCenter();
			
			lang.nextStep("Point " + counter++);
			sc.unhighlight(3);
			Vector<BWTriangle> badTriangles = new Vector<BWTriangle>();
			for (int i = 0; i < triangulation.size(); i++) {
				sc.highlight(5);
				BWTriangle currentTriangle = new BWTriangle(triangulation.get(i).vertices, "currentTriangle", currentTriangleProps);
				lang.nextStep();
				sc.toggleHighlight(5, 6);
				if (triangulation.get(i).pointInsideCircumcircle(point)) {
					lang.nextStep();
					sc.unhighlight(6); sc.highlight(7); sc.highlight(8);
					currentTriangle.getPrimitive().hide();
					triangulation.get(i).getPrimitive().hide();
					circum.hide();
					BWTriangle badTriangle = new BWTriangle(triangulation.remove(i--).vertices, "badTriangle"+i, badTriangleProps);
					badTriangles.add(badTriangle);
				}
				lang.nextStep();
				circum.hide();
				currentTriangle.getPrimitive().hide();
				sc.unhighlight(6); sc.unhighlight(7); sc.unhighlight(8);	
			}		
			
			// create polygon (boundary of polygonal hole)
			BWPolygon polygon = new BWPolygon();
			Vector<Polyline> polyLines = new Vector<Polyline>();
			for (int i = 0; i < badTriangles.size(); i++) {
				for (Edge edge : badTriangles.get(i).edges) {
					sc.highlight(10);
					Polyline cedge = lang.newPolyline(new Coordinates[] { edge.c1, edge.c2 }, "currentEdge", null, currentEdgeProps);
					lang.nextStep();
					sc.toggleHighlight(10, 11); 
					Vector<Triangle> trianglesSharingEdge = new Vector<Triangle>();
					boolean otherBadTriangleContainsEdge = false;
					for (int j = 0; j < badTriangles.size(); j++) {
						if (badTriangles.get(j).hasEdge(edge)) {
							trianglesSharingEdge.add(lang.newTriangle(badTriangles.get(j).vertices[0],
																	  badTriangles.get(j).vertices[1], 
																	  badTriangles.get(j).vertices[2], "temp", null,
																	  triangleSharingEdgeProps));
							if (i != j)
								otherBadTriangleContainsEdge = true;
						}
					}
					lang.nextStep();
					cedge.hide();
					for (Triangle sharingTriangle : trianglesSharingEdge)
						sharingTriangle.hide();
					
					if (!otherBadTriangleContainsEdge){
						sc.toggleHighlight(11,12);
						polygon.addEdge(edge);
						polyLines.add(lang.newPolyline(new Coordinates[] { edge.c1, edge.c2 }, "polygonline", null, polygonLineProps));
						lang.nextStep();
					}
					sc.unhighlight(11); sc.unhighlight(12);
				}
			}
			for (Polyline pl : polyLines)
				pl.hide();
			polygon.createPrimitive("polygon", polygonProps);
			
			for (BWTriangle t : badTriangles)
				t.getPrimitive().hide();
			
			for (Edge edge : polygon.edges) {
				sc.highlight(13);
				Polyline cedge = lang.newPolyline(new Coordinates[] { edge.c1, edge.c2 }, "currentEdge", null, currentEdgeProps);
				lang.nextStep();
				sc.toggleHighlight(13, 14); sc.highlight(15);
				cedge.hide();
				Triangle newTriangle = lang.newTriangle(edge.c1, edge.c2, point, "newTriangle", null, currentTriangleProps);
				lang.nextStep();
				newTriangle.hide();
				triangulation.add(new BWTriangle(edge.c1, edge.c2, point, "triangle", triangleProps));
				sc.unhighlight(14); sc.unhighlight(15);
			}
			
			polygon.getPrimitive().hide();
			c.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, (Color) normalPointProps.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
		}

		boolean setlabel = false;
		for (int i = 0; i < triangulation.size(); i++) {
			sc.highlight(16);
			BWTriangle triangle = triangulation.get(i);
			Triangle currentTriangle = lang.newTriangle(triangle.vertices[0],triangle.vertices[1],triangle.vertices[2], "currentTriangle", null, currentTriangleProps);
			lang.nextStep(setlabel ? null : "Clean up");setlabel=true;
			sc.toggleHighlight(16, 17);
			Circle cc1 = lang.newCircle(c1, 3, "c1", null, currentPointProps);
			Circle cc2 = lang.newCircle(c2, 3, "c2", null, currentPointProps);
			Circle cc3 = lang.newCircle(c3, 3, "c3", null, currentPointProps);
			lang.nextStep();
			cc1.hide(); cc2.hide(); cc3.hide();
			currentTriangle.hide();
			if (triangle.hasVertex(c1) || triangle.hasVertex(c2) || triangle.hasVertex(c3)) {
				sc.toggleHighlight(17, 18);
				triangle.getPrimitive().hide();
				triangulation.remove(i--);
				lang.nextStep();
			}
			sc.unhighlight(17); sc.unhighlight(18);
		}

		lang.nextStep();
		lang.hideAllPrimitives();
		animationOutro();
	}
	
	private double norm2(Coordinates vector) {
		return vector.getX() * vector.getX() + vector.getY() * vector.getY();
	}
	
	private void setProperties() {
		
		badTriangleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		badTriangleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
		
		superTriangleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		superTriangleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		
		triangleSharingEdgeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		triangleSharingEdgeProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(225, 225, 255));
		triangleSharingEdgeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		triangleSharingEdgeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 6);
		
		polygonLineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		polygonLineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
	}

    public void init(){
        lang = new AnimalScript("Bowyer-Watson Delaunay Triangulation", "Arne-Tobias Rak", 1280, 720);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        int[][] pointList = (int[][])primitives.get("pointList");
        superDrawH = (Integer)primitives.get("drawingAreaHeight");
        superDrawW = (Integer)primitives.get("drawingAreaWidth");
        
        polygonProps = (PolygonProperties)props.getPropertiesByName("polygonHole");
        circumProps = (CircleProperties)props.getPropertiesByName("circumcircle");
        currentTriangleProps = (TriangleProperties)props.getPropertiesByName("currentTriangle");
        currentEdgeProps = (PolylineProperties)props.getPropertiesByName("currentEdge");
        normalPointProps = (CircleProperties)props.getPropertiesByName("point");
        triangleProps = (TriangleProperties)props.getPropertiesByName("triangle");
        currentPointProps = (CircleProperties)props.getPropertiesByName("currentPoint");

		triangulate(pointList);
        return lang.toString();
    }
    
    public String getName() {
        return "Bowyer-Watson Delaunay Triangulation";
    }

    public String getAlgorithmName() {
        return "Bowyer-Watson";
    }

    public String getAnimationAuthor() {
        return "Arne-Tobias Rak";
    }

    public String getDescription(){
        return "The Bowyer-Watson algorithm is a strategy for computing the Delaunay "
 +"\n"
 +"triangulation (DT) of a finite set of points. A triangulation is a subdivision"
 +"\n"
 +"of a planar object into triangles, such as a set of points. In a DT,"
 +"\n"
 +"every triangle's circumcircle contains none of the points that were used"
 +"\n"
 +"to compute the triangulation. The Bowyer-Watson algorithm works incrementally,"
 +"\n"
 +"by sequentially adding points to a valid DT of a subset of the used points."
 +"\n"
 +"The initial DT is a super-triangle that is large enough to completely"
 +"\n"
 +"contain all points in the point list. After adding a point, all triangles"
 +"\n"
 +"whose circumcircle contain this point are removed. This results in a"
 +"\n"
 +"polygonal hole, which is retriangulated using the newly inserted point.";
    }

    public String getCodeExample(){
        return "function triangulate (pointList)"
 +"\n"
 +"    triangulation := empty list of triangles"
 +"\n"
 +"    add super-triangle to triangulation"
 +"\n"
 +"    for each point in pointList"
 +"\n"
 +"        badTriangles := empty list of triangles"
 +"\n"
 +"        for each triangle in triangulation"
 +"\n"
 +"            if point is inside circumcircle of triangle"
 +"\n"
 +"                add triangle to badTriangles"
 +"\n"
 +"                remove triangle from triangulation"
 +"\n"
 +"        polygon := empty list of edges"
 +"\n"
 +"        for each edge in each triangle in badTriangles"
 +"\n"
 +"            if edge is not shared by any other triangles in badTriangles"
 +"\n"
 +"                add edge to polygon"
 +"\n"
 +"        for each edge in polygon"
 +"\n"
 +"            newTriangle := connect edge vertices to point"
 +"\n"
 +"            add newTriangle to triangulation"
 +"\n"
 +"    for each triangle in triangulation"
 +"\n"
 +"        if triangle has a vertex from original super-triangle"
 +"\n"
 +"            remove triangle from triangulation";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
	
}
