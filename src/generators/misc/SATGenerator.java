/*
 * Separating Axis Theorem (Circle - Triangle).java
 * Bekir �zkara, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
 package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpersSatGenerator.CircleSAT;
import generators.misc.helpersSatGenerator.TriangleSAT;
import generators.misc.helpersSatGenerator.Vector2fSAT;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalCircleGenerator;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.animalscript.AnimalTriangleGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class SATGenerator implements ValidatingGenerator {
	// auto gen
	private Language lang;
	
	// primitives
	private double vertex_a_x;
	private double vertex_a_y;
	private double vertex_b_x;
    private double vertex_b_y;
    private double vertex_c_x;
    private double vertex_c_y;
    private double circle_center_x;
    private double circle_center_y;
    private double radius;
    
    // properties
    private CircleProperties circleProps;
    private TriangleProperties triangleProps;
    	// new
    private RectProperties boxProps;
    private PolylineProperties coordSystemProps;
    private SourceCodeProperties sourceCodeProps;
    private TextProperties textProps;
    private TextProperties vertexNameProps;
    private CircleProperties vertexHighlightProps;
    private PolylineProperties edgeHighlightProps;
    private PolylineProperties radiusHighlightProps;
    private PolylineProperties distanceHighlightProps;
    private PolylineProperties vectorProps;
    private PolylineProperties vectorHighlightProps;
    
    
    // useful vars
    Color radiusColor;
    Color distanceColor;
    Color vectorDefaultColor;
    Color vectorHighlightColor;
    
    // my own
			Text text;
			Point point;
			Circle circle;
			Polyline line;
			Polyline edge;
			Polyline vector;
			Polyline separatingAxis;
			Triangle triangle;
			
			// temp
			Polyline[] vectors = new Polyline[3];
			
			SourceCode sc;
			
			// might need these properties
			TextProperties textPropsOld;
			RectProperties rectProps;
			PolylineProperties lineProps;
			PolylineProperties vectorPropsOld;
			
			int x = 40;
			int y = 140;
			int startY;
			int offsetY = 20;
			String vertexName;
			String neighbor1Name;
			String neighbor2Name;
			
			Vector2fSAT separatingAxisStart;
			Vector2fSAT separatingAxisEnd;
			
			List<Text> textList = new ArrayList<>();
			
			// needed for clearing the screen
			Rect sideBox;
			List<Polyline> coordLines = new ArrayList<>();
			Triangle t;
			Circle c;
			List<Text> verticesText = new ArrayList<>();
	// end my own

    public void init(){
//        lang = new AnimalScript("Separating Axis Theorem (Circle - Triangle)", "Bekir �zkara", 800, 600);
    	lang = new AnimalScript("Separating Axis Theorem (Circle-Triangle)", "Bekir Oezkara", 640, 480);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		lineProps = new PolylineProperties();
		lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		vectorPropsOld = new PolylineProperties();
		vectorPropsOld.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		vectorPropsOld.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
//    	lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    	
    	// primitives
    	vertex_a_x = (double)primitives.get("vertex_a_x");
    	vertex_a_y = (double)primitives.get("vertex_a_y");
        vertex_b_x = (double)primitives.get("vertex_b_x");
        vertex_b_y = (double)primitives.get("vertex_b_y");
        vertex_c_x = (double)primitives.get("vertex_c_x");
        vertex_c_y = (double)primitives.get("vertex_c_y");
        circle_center_x = (double)primitives.get("circle_center_x");
        circle_center_y = (double)primitives.get("circle_center_y");
        radius = (double)primitives.get("radius");
        
        // properties
        circleProps = (CircleProperties)props.getPropertiesByName("circleProps");
        triangleProps = (TriangleProperties)props.getPropertiesByName("triangleProps");
        	// new ones
        boxProps = (RectProperties)props.getPropertiesByName("boxProps");
        coordSystemProps = (PolylineProperties)props.getPropertiesByName("coordSystemProps");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        textProps = (TextProperties)props.getPropertiesByName("textProps");
        vertexNameProps = (TextProperties)props.getPropertiesByName("vertexNameProps");
        vertexHighlightProps = (CircleProperties)props.getPropertiesByName("vertexHighlightProps");
        edgeHighlightProps = (PolylineProperties)props.getPropertiesByName("edgeHighlightProps");
        radiusHighlightProps = (PolylineProperties)props.getPropertiesByName("radiusHighlightProps");
        distanceHighlightProps = (PolylineProperties)props.getPropertiesByName("distanceHighlightProps");
        vectorProps = (PolylineProperties)props.getPropertiesByName("vectorProps");
        vectorHighlightProps = (PolylineProperties)props.getPropertiesByName("vectorHighlightProps");
        
        
        
        radiusColor = (Color)radiusHighlightProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        distanceColor = (Color)distanceHighlightProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        vectorDefaultColor = (Color)vectorProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		vectorHighlightColor = (Color)vectorHighlightProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        
        init();
        
        makeHeader();
        List<Text> texts = showIntroText();
        lang.nextStep("Intro");
		for(Text t : texts) {
			t.hide();
		}
		showIntroQuestion();
		
		createScene();
		
		CircleSAT C = new CircleSAT(new Vector2fSAT((float)circle_center_x, (float)circle_center_y), (float)radius);
		// auxiliary.Triangle T = new auxiliary.Triangle(new Vector2f(1, 1), new Vector2f(3, 1), new Vector2f(2, 4));
		TriangleSAT T = new TriangleSAT(
				new Vector2fSAT((float)vertex_a_x, (float)vertex_a_y), 
				new Vector2fSAT((float)vertex_b_x, (float)vertex_b_y), 
				new Vector2fSAT((float)vertex_c_x, (float)vertex_c_y));
		hasSA(C, T);
		
		lang.nextStep("QUIZ");
		clearScreen();
		circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		triangleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		showOutroQuestions();
		showOutroText();
        
        
        lang.finalizeGeneration();
        return lang.toString();
    }
    
    // ====================================================================================================================================
 	// ====================================================================================================================================
 	// ====================================================================================================================================
 	// ====================================================================================================================================
 	// ====================================================================================================================================
 	// ====================================================================================================================================

    private void createScene() {
		makeSideBox();
		drawCoordSystem();
	}
    
    private void clearScreen() {
    	sc.hide();
    	t.hide();
    	c.hide();
    	sideBox.hide();
    	separatingAxis.hide();
    	for(Text v : verticesText) {
    		v.hide();
    	}
    	for(Polyline l : coordLines) {
    		l.hide();
    	}
    	clearText();
	}
	
	
	public boolean hasSA(CircleSAT circle, TriangleSAT triangle) {
		
		c = makeCircle(circle.center.x, circle.center.y, circle.radius);
		t = makeTriangle(triangle.A, triangle.B, triangle.C);
		showSourceCodeVertexSA();
		verticesText.add(makeText("A", 600 + 40 * (int)triangle.A.x + 5 , 300 - 40 * (int)triangle.A.y, vertexNameProps));
		verticesText.add(makeText("B", 600 + 40 * (int)triangle.B.x + 5 , 300 - 40 * (int)triangle.B.y, vertexNameProps));
		verticesText.add(makeText("C", 600 + 40 * (int)triangle.C.x + 5 , 300 - 40 * (int)triangle.C.y, vertexNameProps));
		lang.nextStep();
		
		x = 910;
		startY = 160;
		y = startY;
		
		// vertices
		Circle vertexHighlightCircle;
		Color vertexHighlightColor = (Color)vertexHighlightProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		boolean isFilled = (boolean)vertexHighlightProps.get(AnimationPropertiesKeys.FILLED_PROPERTY);
		
		vertexName = "A";
		neighbor1Name = "B";
		neighbor2Name = "C";
		vertexHighlightCircle = makeCircle(triangle.A.x, triangle.A.y, 0.1f, vertexHighlightColor, isFilled);
		textAndStep("Vertex A", "Vertex A");
		vertexHighlightCircle.hide();
		boolean separatedByVertexA = isVertexSA(circle, triangle, triangle.A);
		
		clearText();
		y = startY;
		
		vertexName = "B";
		neighbor1Name = "A";
		neighbor2Name = "C";
		vertexHighlightCircle = makeCircle(triangle.B.x, triangle.B.y, 0.1f, Color.PINK, isFilled);
		textAndStep("Vertex B", "Vertex B");
		vertexHighlightCircle.hide();
		boolean separatedByVertexB = isVertexSA(circle, triangle, triangle.B);
		
		clearText();
		y = startY;
		
		vertexName = "C";
		neighbor1Name = "A";
		neighbor2Name = "B";
		vertexHighlightCircle = makeCircle(triangle.C.x, triangle.C.y, 0.1f, Color.PINK, isFilled);
		textAndStep("Vertex C", "Vertex C");
		vertexHighlightCircle.hide();
		boolean separatedByVertexC = isVertexSA(circle, triangle, triangle.C);
		
		clearText();
		y = startY;
		
		// edges
		lang.nextStep();
		sc.changeColor("", Color.WHITE, null, null);
		showSourceCodeEdgeSA();
		Color edgeHighlightColor = (Color)edgeHighlightProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		edge = makeLine(triangle.A, triangle.B, false, true);
		edge.changeColor("", edgeHighlightColor, null, null);
		textAndStep("Edge from A to B", "Edge AB");
		boolean separatedByEdgeAB = isEdgeSA(circle, triangle, triangle.edgeAB, triangle.A, triangle.B, triangle.C);
		edge.hide();
		
		clearText();
		y = startY;
		
		edge = makeLine(triangle.A, triangle.C, false, true);
		edge.changeColor("", edgeHighlightColor, null, null);
		textAndStep("Edge from A to C", "Edge AC");
		boolean separatedByEdgeAC = isEdgeSA(circle, triangle, triangle.edgeAC, triangle.A, triangle.C, triangle.B);
//		edge.changeColor("", Color.BLACK, null, null);
		edge.hide();
		
		clearText();
		y = startY;
		
		edge = makeLine(triangle.B, triangle.C, false, true);
		edge.changeColor("", edgeHighlightColor, null, null);
		textAndStep("Edge from B to C", "Edge BC");
		boolean separatedByEdgeBC = isEdgeSA(circle, triangle, triangle.edgeBC, triangle.B, triangle.C, triangle.A);
		edge.hide();
		
		clearText();
		y = startY;
		
		boolean result = separatedByVertexA || separatedByVertexB || separatedByVertexC || separatedByEdgeAB || separatedByEdgeAC || separatedByEdgeBC;
		if(result) {
			separatingAxis = makeLine(separatingAxisStart, separatingAxisEnd, false, true);
			separatingAxis.changeColor("", Color.GREEN, null, null);
		}
//		textAndStep("we found separating axis = " + result);
		textList.add(makeText("we found separating axis = " + result, this.x, this.y));
		return result;
	}
	
	
	/**
	 * 
	 * @param circle
	 * @param triangle
	 * @param vertex
	 * @return
	 */
	private boolean isVertexSA(CircleSAT circle, TriangleSAT triangle, Vector2fSAT vertex) {
		Polyline radiusLine = makeLine(circle.center.x, circle.center.y, circle.center.x + circle.radius, circle.center.y, false, true);
		radiusLine.changeColor("", radiusColor, null, null);
		textAndStep("radius = " + circle.radius);
		radiusLine.hide();
		
		sc.highlight(1);
		float distToCenter = vertex.dist(circle.center);
		Polyline distToCentereLine = makeLine(vertex, circle.center, false, true);
		distToCentereLine.changeColor("", distanceColor, null, null);
		textAndStep("distance to center = " + distToCenter);
		distToCentereLine.hide();
		sc.unhighlight(1);
		
		
		sc.highlight(2);
		Vector2fSAT vertexToCenter = circle.center.sub(vertex);
		vectors[0] = makeLine(vertex, circle.center, true, vectorProps);
		textAndStep("vector from vertex to center = " + vertexToCenter);
		sc.unhighlight(2);
		
		Vector2fSAT vertexToNeighbor1;
		Vector2fSAT vertexToNeighbor2;
		
		if(vertex == triangle.A) {
			sc.highlight(6, 0, true);
			sc.highlight(7);
			sc.highlight(9);
			vertexToNeighbor1 = triangle.B.sub(vertex);	// AB
			vertexToNeighbor2 = triangle.C.sub(vertex);	// AC
			neighbor1Name = "B";
			neighbor2Name = "C";
			vectors[1] = makeLine(vertex, triangle.B, true, vectorProps);
			textAndStep("vector from " + vertexName + " to " + neighbor1Name + " = " + vertexToNeighbor1);
			sc.unhighlight(7);
			
			sc.highlight(8);
			vectors[2] = makeLine(vertex, triangle.C, true, vectorProps);
			textAndStep("vector from " + vertexName + " to " + neighbor2Name + " = " + vertexToNeighbor2);
			sc.unhighlight(6);
			sc.unhighlight(8);
			sc.unhighlight(9);
		}
		else if(vertex == triangle.B) {
			sc.highlight(10, 0, true);
			sc.highlight(11);
			sc.highlight(13);
			vertexToNeighbor1 = triangle.A.sub(vertex);	// BA
			vertexToNeighbor2 = triangle.C.sub(vertex); // BC
			neighbor1Name = "A";
			neighbor2Name = "C";
			vectors[1] = makeLine(vertex, triangle.A, true, vectorProps);
			textAndStep("vector from " + vertexName + " to " + neighbor1Name + " = " + vertexToNeighbor1);
			sc.unhighlight(11);
			
			sc.highlight(12);
			vectors[2] = makeLine(vertex, triangle.C, true, vectorProps);
			textAndStep("vector from " + vertexName + " to " + neighbor2Name + " = " + vertexToNeighbor2);
			sc.unhighlight(10);
			sc.unhighlight(12);
			sc.unhighlight(13);
		}
		else if(vertex == triangle.C) {
			sc.highlight(14, 0, true);
			sc.highlight(15);
			sc.highlight(17);
			vertexToNeighbor1 = triangle.A.sub(vertex); // CA
			vertexToNeighbor2 = triangle.B.sub(vertex); // CB
			neighbor1Name = "A";
			neighbor2Name = "B";
			vectors[1] = makeLine(vertex, triangle.A, true, vectorProps);
			textAndStep("vector from " + vertexName + " to " + neighbor1Name + " = " + vertexToNeighbor1);
			sc.unhighlight(15);
			
			sc.highlight(16);
			vectors[2] = makeLine(vertex, triangle.B, true, vectorProps);
			textAndStep("vector from " + vertexName + " to " + neighbor2Name + " = " + vertexToNeighbor2);
			sc.unhighlight(14);
			sc.unhighlight(16);
			sc.unhighlight(17);
		}
		else {
			throw new IllegalArgumentException("The provided vertex: " + vertex + " does not match with any of the triangle's vertices.");
		}

		// they are on opposite sides if the dot product is less than zero
		vectors[0].changeColor("", vectorHighlightColor, null, null);
		
		sc.highlight(21);
		sc.highlight(22);
		boolean isOppositeNeighbor1 = vertexToCenter.dot(vertexToNeighbor1) < 0;
		vectors[1].changeColor("", vectorHighlightColor, null, null);
		textAndStep(neighbor1Name + " is on opposite side = " + isOppositeNeighbor1);
		vectors[1].changeColor("", vectorDefaultColor, null, null);
		sc.unhighlight(21);
		sc.unhighlight(22);
		
		sc.highlight(23);
		sc.highlight(24);
		boolean isOppositeNeighbor2 = vertexToCenter.dot(vertexToNeighbor2) < 0;
		vectors[2].changeColor("", vectorHighlightColor, null, null);
		textAndStep(neighbor2Name + " is on opposite side = " + isOppositeNeighbor2);
		sc.unhighlight(23);
		sc.unhighlight(24);
		
		vectors[0].hide();
		vectors[1].hide();
		vectors[2].hide();
		
		sc.highlight(26);
		boolean radius_less_than_dist = circle.radius < distToCenter;
		radiusLine.show();
		distToCentereLine.show();
		textAndStep("radius smaller than distance = " + radius_less_than_dist);
		radiusLine.hide();
		distToCentereLine.hide();
		
		
		boolean result = radius_less_than_dist && isOppositeNeighbor1 && isOppositeNeighbor2;
		line = makeLine(vertex, circle.center, false, true);
		line.changeColor("", result ? Color.GREEN : Color.RED, null, null);
		if(result) {
			separatingAxisStart = new Vector2fSAT(vertex.x, vertex.y);
			separatingAxisEnd	= new Vector2fSAT(circle.center.x, circle.center.y);
		}
		sc.highlight(27);
//		textAndStep("vertex is separating axis = " + result);
			textList.add(makeText("vertex is separating axis = " + result, this.x, this.y));
			y += offsetY;
			lang.nextStep();
		line.hide();
		sc.unhighlight(26);
		sc.unhighlight(27);
		return result;
	}
	
	
	/**
	 * 
	 * @param circle
	 * @param triangle
	 * @param edge
	 * @param start
	 * @param end
	 * @param other
	 * @return
	 */
	private boolean isEdgeSA(CircleSAT circle, TriangleSAT triangle, Vector2fSAT edge, Vector2fSAT start, Vector2fSAT end, Vector2fSAT other) {
		Polyline radiusLine = makeLine(circle.center.x, circle.center.y, circle.center.x + circle.radius, circle.center.y, false, true);
		radiusLine.changeColor("", radiusColor, null, null);
		textAndStep("radius = " + circle.radius);
		radiusLine.hide();
		
		sc.highlight(1);
		Vector2fSAT normalizedEdge = edge.normalize();
		vector = makeLine(start.x, start.y, start.x + normalizedEdge.x, start.y + normalizedEdge.y, true, vectorProps);
		vector.changeColor("", vectorDefaultColor, null, null);
		textAndStep("normalized edge = " + normalizedEdge);
		vector.hide();
		sc.unhighlight(1);
		
		Vector2fSAT vecToCircle;
		if(edge == triangle.edgeAB) {
			sc.highlight(4, 0, true);
			sc.highlight(5);
			sc.highlight(6);
			vecToCircle = circle.center.sub(triangle.A);
			vector = makeLine(triangle.A, circle.center, true, vectorProps);
			textAndStep("vector to circle = " + vecToCircle);
			vector.hide();
			sc.unhighlight(4);
			sc.unhighlight(5);
			sc.unhighlight(6);
		}
		else if(edge == triangle.edgeAC) {
			sc.highlight(7, 0, true);
			sc.highlight(8);
			sc.highlight(9);
			vecToCircle = circle.center.sub(triangle.A);
			vector = makeLine(triangle.A, circle.center, true, vectorProps);
			textAndStep("vector to circle = " + vecToCircle);
			vector.hide();
			sc.unhighlight(7); 
			sc.unhighlight(8); 
			sc.unhighlight(9); 
		}
		else if(edge == triangle.edgeBC) {
			sc.highlight(10, 0, true);
			sc.highlight(11);
			sc.highlight(12);
			vecToCircle = circle.center.sub(triangle.B);
			vector = makeLine(triangle.B, circle.center, true, vectorProps);
			textAndStep("vector to circle = " + vecToCircle);
			vector.hide();
			sc.unhighlight(10);
			sc.unhighlight(11);
			sc.unhighlight(12);
		}
		else {
			throw new IllegalArgumentException("The provided edge: " + edge + " does not match with any of the triangle's edges.");
		}
		
		sc.highlight(13);
		float dot = normalizedEdge.dot(vecToCircle);
		line = makeLine(start, start.add(normalizedEdge.mul(dot)), false, true);
		line.changeColor("", Color.CYAN, null, null);
		textAndStep("dot product of normalizedEdge and vectorToCircle = " + dot);
		line.hide();
		sc.unhighlight(13);
		
		Vector2fSAT closestPointToCircle;
		int codeLine1 = 0;
		int codeLine2 = 0;
		if(dot <= 0) {
			codeLine1 = 16;
			sc.highlight(codeLine1 - 1, 0, true);
			sc.highlight(codeLine1);
			closestPointToCircle = start;
		}
		else if(dot >= edge.length()) {
			codeLine1 = 19;
			sc.highlight(codeLine1 - 1, 0, true);
			sc.highlight(codeLine1);
			closestPointToCircle = end;
		}
		else {
			codeLine1 = 22;
			codeLine2 = 23;
			sc.highlight(codeLine1 - 1, 0, true);
			sc.highlight(codeLine1);
			sc.highlight(codeLine2);
			closestPointToCircle = normalizedEdge.mul(dot).add(start);
		}
		
		Circle pointToHighlight;
		boolean isFilled = true;
		
		line = makeLine(start, closestPointToCircle, false, true);
		line.changeColor("", Color.CYAN, null, null);
		pointToHighlight = makeCircle(closestPointToCircle.x, closestPointToCircle.y, 0.1f, Color.PINK, isFilled);
		textAndStep("closest point to circle = " + closestPointToCircle);
		pointToHighlight.hide();
		line.hide();
		sc.unhighlight(codeLine1 - 1);
		sc.unhighlight(codeLine1);
		sc.unhighlight(codeLine2);
		
		// check if the other vertex (the one that is not part of the current edge) and the circle-center lie on opposite sides of the edge
		sc.highlight(29);
		sc.highlight(30);
		Vector2fSAT vecFromClosestPointToCircle 		= circle.center.sub(closestPointToCircle);
		vectors[0] = makeLine(closestPointToCircle, circle.center, true, vectorProps);
		textAndStep("vector from closest point to circle = " + vecFromClosestPointToCircle);
		sc.unhighlight(29);
		sc.unhighlight(30);
		
		sc.highlight(31);
		sc.highlight(32);
		Vector2fSAT vecFromClosestPointToOtherVertex 	= other.sub(closestPointToCircle); 
		vectors[1] = makeLine(closestPointToCircle, other, true, vectorProps);
		textAndStep("vector from closest point to other vertex = " + vecFromClosestPointToOtherVertex);
		sc.unhighlight(31);
		sc.unhighlight(32);
		
		sc.highlight(33);
		boolean isOppositeOtherVertex 				= vecFromClosestPointToCircle.dot(vecFromClosestPointToOtherVertex) < 0;
		Color clr = vectorDefaultColor != Color.BLUE ? Color.BLUE : Color.ORANGE;
		vectors[0].changeColor("", clr, null, null);
		vectors[1].changeColor("", clr, null, null);
		textAndStep("other vertex on opposite side of circle = " + isOppositeOtherVertex);
		sc.unhighlight(33);
		
		vectors[0].hide();
		vectors[1].hide();
		
		sc.highlight(35);
		radiusLine.show();
		Polyline distToCenterLine = makeLine(closestPointToCircle, circle.center, false, true);
		distToCenterLine.changeColor("", distanceColor, null, null);
		boolean radius_less_than_dist_to_closest_point = circle.radius < vecFromClosestPointToCircle.length();
		textAndStep("radius less than distance to closest point = " + radius_less_than_dist_to_closest_point);
		radiusLine.hide();
		distToCenterLine.hide();
		
		boolean result = radius_less_than_dist_to_closest_point && isOppositeOtherVertex;
		line = makeLine(closestPointToCircle, circle.center, false, true);
		line.changeColor("", result ? Color.GREEN : Color.RED, null, null);
		if(result) {
			separatingAxisStart = new Vector2fSAT(closestPointToCircle.x, closestPointToCircle.y);
			separatingAxisEnd   = new Vector2fSAT(circle.center.x, circle.center.y);
		}
		textAndStep("is separating axis = " + result);
		line.hide();
		sc.unhighlight(35);
		return result;
	}
	
	
	// ====================================================================================================================================
	// ====================================================================================================================================
	
	private Circle makeCircle(float x, float y, float radius) {
		return new Circle(new AnimalCircleGenerator(lang), transformCoords(x, y), transformRadius(radius), "circle", null, circleProps);
	}
	
	private Circle makeCircle(float x, float y, float radius, Color color, boolean isFilled) {
		CircleProperties props = new CircleProperties();
		props.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
		props.set(AnimationPropertiesKeys.FILLED_PROPERTY, isFilled);
		return new Circle(new AnimalCircleGenerator(lang), transformCoords(x, y), transformRadius(radius), "circle", null, props);
	}
	
	private Triangle makeTriangle(Vector2fSAT A, Vector2fSAT B, Vector2fSAT C) {
		return makeTriangle(A.x, A.y, B.x, B.y, C.x, C.y);
	}
	
	private Triangle makeTriangle(float Ax, float Ay, float Bx, float By, float Cx, float Cy) {
		return new Triangle(new AnimalTriangleGenerator(lang), 
				transformCoords(Ax, Ay),
				transformCoords(Bx, By),
				transformCoords(Cx, Cy),
				"triangle",
				null,
				triangleProps);
	}
	
	private Coordinates transformCoords(float x, float y) {
		float newX = 600 + 40 * x;
		float newY = 300 - 40 * y;
		return new Coordinates((int)newX, (int)newY);
	}
	
	private int transformRadius(float radius) {
		float newRadius = 40 * radius;
		return (int)newRadius;
	}
	
	private Rect makeRect(int upperLeftX, int upperLeftY, int lowerRightX, int lowerRightY) {
		return new Rect(new AnimalRectGenerator(lang), new Coordinates(upperLeftX, upperLeftY), new Coordinates(lowerRightX, lowerRightY), "rect", null, rectProps);
	}
	
	private Rect makeRect(int upperLeftX, int upperLeftY, int lowerRightX, int lowerRightY, RectProperties rectProps) {
		return new Rect(new AnimalRectGenerator(lang), new Coordinates(upperLeftX, upperLeftY), new Coordinates(lowerRightX, lowerRightY), "rect", null, rectProps);
	}
	
	private Text makeText(String text, int x, int y) {
		return new Text(new AnimalTextGenerator(lang), new Coordinates(x, y), text, "text", null, textProps);
	}
	
	private Text makeText(String text, int x, int y, TextProperties textProps) {
		return new Text(new AnimalTextGenerator(lang), new Coordinates(x, y), text, "text", null, textProps);
	}
	
	private void textAndStep(String text) {
		textAndStep(text, this.x, this.y);
	}
	
	private void textAndStep(String text, int x, int y) {
		textList.add(makeText(text, x, y));
		this.y += offsetY;
		lang.nextStep();
	}
	
	private void textAndStep(String text, String content) {
		textAndStep(text, this.x, this.y, content);
	}
	
	private void textAndStep(String text, int x, int y, String content) {
		textList.add(makeText(text, x, y));
		this.y += offsetY;
		lang.nextStep(content);
	}
	
	private void clearText() {
		for(Text text : textList) {
			text.setText("", null, null);
		}
		
		textList = new ArrayList<>();
	}
	
	
	private Polyline makeLine(Vector2fSAT from, Vector2fSAT to, boolean isVector, boolean needsTransform) {
		return makeLine(from.x, from.y, to.x, to.y, isVector, needsTransform);
	}
	
	private Polyline makeLine(float startX, float startY, float endX, float endY, boolean isVector, boolean needsTransform) {
		Node[] nodeArray;
		if(needsTransform) {
			nodeArray = new Node[] {
					transformCoords(startX, startY), 
					transformCoords(endX, endY)
			};
		}
		else {
			nodeArray = new Node[] {
					new Coordinates((int)startX, (int)startY), 
					new Coordinates((int)endX, (int)endY)
			};
		}
		
		return new Polyline(new AnimalPolylineGenerator(lang), nodeArray, "line", null, isVector ? vectorPropsOld : lineProps);
	}
	
	private Polyline makeLine(Vector2fSAT from, Vector2fSAT to, boolean needsTransform, PolylineProperties lineProps) {
		return makeLine(from.x, from.y, to.x, to.y, needsTransform, lineProps);
	}
	
	private Polyline makeLine(float startX, float startY, float endX, float endY, boolean needsTransform, PolylineProperties lineProps) {
		Node[] nodeArray;
		if(needsTransform) {
			nodeArray = new Node[] {
					transformCoords(startX, startY), 
					transformCoords(endX, endY)
			};
		}
		else {
			nodeArray = new Node[] {
					new Coordinates((int)startX, (int)startY), 
					new Coordinates((int)endX, (int)endY)
			};
		}
		
		return new Polyline(new AnimalPolylineGenerator(lang), nodeArray, "line", null, lineProps);
	}
	
	
	private void makeHeader() {
		makeRect(430, 25, 790, 50);
		
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		new Text(new AnimalTextGenerator(lang), new Coordinates(450, 27), "Separating Axis Theorem (Circle-Triangle)", "title", null, titleProps);
	}
	
	private void makeSideBox() {
		sideBox = makeRect(900, 150, 1400, 380, boxProps);
	}
	
	private void drawCoordSystem() {
		int xAxis 		= 300;
		int xAxisStart 	= 400;
		int xAxisEnd 	= 800;
		
		int yAxis 		= 600;
		int yAxisStart 	= 500;
		int yAxisEnd 	= 100;
		
		coordLines.add(makeLine(xAxisStart, xAxis, xAxisEnd, xAxis, false, coordSystemProps));	// x-axis (600px)
		coordLines.add(makeLine(yAxis, yAxisStart, yAxis, yAxisEnd, false, coordSystemProps));	// y-axis (400px)
		
		int offset = 40;	// 60px = 1cm
		
		// x-axis
		for(int i=xAxisStart; i <= xAxisEnd; i+=offset) {
			coordLines.add(makeLine(i, xAxis-5, i, xAxis+5, false, false));
		}
		
		// y-axis
		for(int i=yAxisStart; i >= yAxisEnd; i-=offset) {
			coordLines.add(makeLine(yAxis-5, i, yAxis+5, i, false, false));
		}
	}
    
    
    // ====================================================================================================================================
 	// ====================================================================================================================================
 	// ====================================================================================================================================
 	// ====================================================================================================================================
 	// ====================================================================================================================================
 	// ====================================================================================================================================
	
	public List<Text> showIntroText() {
		List<Text> texts = new ArrayList<>();
		
		Text header = makeText("Separating Axis Theorem (Circle - Triangle)", 100, 100);
		header.changeColor("", Color.BLUE, null, null);
		
		String t1 = "The Separating Axis Theorem (SAT) can be used to check for collisions between two polygons or between a circle and a polygon.";
		String t2 = "The polygons have to be convex, which, roughly speaking, means that any line drawn through the shape crosses it twice, and no more.";
		String t3 = "To help visualizing it in your head: if there is a separating axis, you can draw a line (perpendicular to that separating axis) ";
		String t4 = "which does not touch the objects -> no collision.";
		String t5 = "In the animation there is a small coordinate system. A smaller range was needed because otherwise normalized vectors wouldn't be drawn correctly.";
		String t6 = "The box on the right hand side keeps track of important values/calculations that are needed for the test.";
		String t7 = "As for the algorithm itself: it takes a point of the triangle (P1) and checks it against the center point of the circle (P2).";
		String t8 = "When certain conditions are fulfilled, the resulting direction from (P2 - P1) is a separating axis.";
		String t9 = "In the case of the vertices (A, B, and C), P1 is the vertex itself. In the case of the edges, P1 is the point on the edge";
		String t10 = "that is closest to the circle's center.";
		
		texts.add(header);
		texts.add(makeText(t1, 100, 150));
		texts.add(makeText(t2, 100, 170));
		texts.add(makeText(t3, 100, 190));
		texts.add(makeText(t4, 100, 210));
		texts.add(makeText(t5, 100, 250));
		texts.add(makeText(t6, 100, 270));
		texts.add(makeText(t7, 100, 310));
		texts.add(makeText(t8, 100, 330));
		texts.add(makeText(t9, 100, 350));
		texts.add(makeText(t10, 100, 370));
		
		return texts;
	}
	
	public void showIntroQuestion() {
		// for clearing
		List<Object> objects = new ArrayList<>();
		
		Text questionHeader = makeText("PREPARATION QUESTION", 50, 75);
		questionHeader.setFont(new Font("Monospaced", Font.BOLD, 28), null, null);
		questionHeader.changeColor("", Color.BLUE, null, null);
		objects.add(questionHeader);
		
		objects.add(makeText("This algorithm makes a lot of use of the dot product, so the following question is meant "
				+ "to ease the understanding of the usage of the dot product within the algorithm:", 50, 100));
		
		// question
		Text q = makeText("Q: The dot product of two vectors is less than zero. What can we infer from this?", 50, 140);
		q.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);
		objects.add(q);
		objects.add(makeText("As a reminder: the dot product for two vectors v and w is defined as dot(v, w) := (v.x * w.x + v.y * w.y)", 50, 160));
				
		MultipleChoiceQuestionModel introQuestion = new MultipleChoiceQuestionModel("Intro");
		introQuestion.setPrompt("The dot product of two vectors is less than zero. What can we infer from this?");
		introQuestion.addAnswer("The angle between them is exactly 90 degrees (~they are perpendicular to each other)", 0, 
				"Wrong! That would be the case if their dot product was equal to zero.");
		introQuestion.addAnswer("The angle between them is less than 90 degrees (~ they point in the same direction)", 0,
				"Wrong! That would be the case if the dot product was greater than zero.");
		introQuestion.addAnswer("The angle between them is greater than 90 degrees (~ they point in opposite directions)", 0, "Correct!");
		lang.addMCQuestion(introQuestion);
		
		
		lang.nextStep("Preparation");
		// insert answer
		Text a = makeText("A: If the dot product of two vectors is less than zero, then the angle between them is greater than 90 degrees, i.e., "
				+ "they point in opposite directions.", 50, 190);
		a.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);
		objects.add(a);
		objects.add(makeText("The cases are illustrated below.", 50, 210));
		
		// perpendicular
		Polyline vecPerp11 = makeLine(-11, 0, -11, 1, true, true);
		Polyline vecPerp12 = makeLine(-11, 0, -10, 0, true, true);
		Text txtPerp = makeText("The vectors are perpendicular to each other, that means their dot product EQUALS zero.", 250, 275);
		vecPerp11.changeColor("", Color.RED, null, null);
		vecPerp12.changeColor("", Color.RED, null, null);
		txtPerp.changeColor("", Color.RED, null, null);
		objects.add(vecPerp11);
		objects.add(vecPerp12);
		objects.add(txtPerp);
		
		// same
		Polyline vecSame11 = makeLine(-11, -2, -10.5f, -1, true, true);
		Polyline vecSame12 = makeLine(-11, -2, -10, -2, true, true);
		Text txtSame = makeText("The angle between them is less than 90 degrees (~same direction), that means their dot product is GREATER THAN zero.", 250, 350);
		vecSame11.changeColor("", Color.MAGENTA, null, null);
		vecSame12.changeColor("", Color.MAGENTA, null, null);
		txtSame.changeColor("", Color.MAGENTA, null, null);
		objects.add(vecSame11);
		objects.add(vecSame12);
		objects.add(txtSame);
		
		// opposite
		Polyline vecOpposite11 = makeLine(-11, -4, -11.5f, -3, true, true);
		Polyline vecOpposite12 = makeLine(-11, -4, -10, -4, true, true);
		Text txtOpposite = makeText("The angle between them is greater than 90 degrees (~opposite direction), that means their dot product is LESS THAN zero.", 250, 425);
		vecOpposite11.changeColor("", Color.BLUE, null, null);
		vecOpposite12.changeColor("", Color.BLUE, null, null);
		txtOpposite.changeColor("", Color.BLUE, null, null);
		objects.add(vecOpposite11);
		objects.add(vecOpposite12);
		objects.add(txtOpposite);
		
		lang.nextStep();
		// clear everything!!
		for(Object o : objects) {
			if(o instanceof Text) {
				((Text)o).hide();
			}
			else if(o instanceof Polyline) {
				((Polyline)o).hide();
			}
		}
	}
	
	public void showOutroQuestions() {
		// for clearing
		List<Object> objects = new ArrayList<>();
		
		Text questionHeader = makeText("QUIZ TIME", 50, 75);
		questionHeader.setFont(new Font("Monospaced", Font.BOLD, 28), null, null);
		questionHeader.changeColor("", Color.BLUE, null, null);
		objects.add(questionHeader);
		
		objects.add(makeText("(1) Which is a separating axis, the cyan/horizontal line, ", 50, 100));
		objects.add(makeText("or the orange/vertical line?", 50, 120));
		
		objects.add(makeCircle(-11, 3, 0.5f, Color.BLACK, false));
		objects.add(makeTriangle(-10, 2.5f, -9, 2.5f, -9.5f, 3.5f));
		Polyline lineHorizontal = makeLine(-12, 2, -8.5f, 2, false, true);
		lineHorizontal.changeColor("", Color.CYAN, null, null);
		Polyline lineVertical = makeLine(-10.25f, 3.75f, -10.25f, 1.5f, false, true);
		lineVertical.changeColor("", Color.ORANGE, null, null);
		
		objects.add(lineHorizontal);
		objects.add(lineVertical);
		
		// insert quiz 1
		MultipleChoiceQuestionModel axisQuestion = new MultipleChoiceQuestionModel("Axis");
		axisQuestion.setPrompt("Which is a separating axis, the cyan/horizontal line, or the orange/vertical line?");
		axisQuestion.addAnswer("cyan/horizontal", 1, "Correct!");
		axisQuestion.addAnswer("orange/vertical", 0, 
				"Wrong! This line simply goes through the objects, whereby it shows that a separating axis exists, but it itself is not one.");
		lang.addMCQuestion(axisQuestion);
		
		lang.nextStep();
		// insert answer to quiz 1
		Text questionAnswer = makeText("The correct answer is the cyan/horizontal line.", 50, 250);
		Text a11 = makeText("A more formal way of defining the separating axis goes like this: ", 50, 270);
		a11.changeColor("", Color.BLUE, null, null);
		Text a12 = makeText("Project all points of the objects onto the axis, and get their min and max values respectively.", 50, 290);
		a12.changeColor("", Color.BLUE, null, null);
		Text a13 = makeText("The axis is a separating axis iff max1 < min2 or max2 < min1, i.e. if the projections do not overlap.", 50, 310);
		a13.changeColor("", Color.BLUE, null, null);
		Text a14 = makeText("In our case we can see that the projections on the orange/vertical line completely overlap, as opposed to the cyan/horizontal line.", 50, 330);
		Text a15 = makeText("(The projections on the cyan/horizontal line are highlighted for clarity.)", 50, 350);
		
		objects.add(questionAnswer);
		objects.add(a11);
		objects.add(a12);
		objects.add(a13);
		objects.add(a14);
		objects.add(a15);
		
		Polyline projCircle = makeLine(-11.5f, 2, -10.5f, 2, false, true);
		projCircle.changeColor("", Color.RED, null, null);
		Polyline projTriangle = makeLine(-10, 2, -9, 2, false, true);
		projTriangle.changeColor("", Color.RED, null, null);
		
		objects.add(projCircle);
		objects.add(projTriangle);
		
		
		lang.nextStep();
		// question 2
		objects.add(makeText("(2) The objects are clearly not colliding. The algorithm currently iterates over vertex A and says there is no separating axis. Why?", 50, 400));
		objects.add(makeText("Does this mean the algorithm has failed?", 50, 420));
		
		objects.add(makeCircle(-6, -5, 1, Color.BLACK, false));
		objects.add(makeTriangle(-10, -6, -8, -5, -11, -4));
		objects.add(makeText("A", 600 + 40 * (-10) + 5 , 300 - 40 * (-6)));
		objects.add(makeText("B", 600 + 40 * (-8) + 5 , 300 - 40 * (-5)));
		objects.add(makeText("C", 600 + 40 * (-11) + 5 , 300 - 40 * (-4)));
		
		// insert quiz 2.1
		MultipleChoiceQuestionModel question21 = new MultipleChoiceQuestionModel("Q2.1");
		question21.setPrompt("The below objects are clearly not colliding. The algorithm currently iterates over vertex A and says there is no separating axis. Why?");
		question21.addAnswer("The distance from A to the center of the circle is greater than the circle's radius", 0, "Wrong!");
		question21.addAnswer("Vertex B is not on the opposite side of the center", 1, "Correct!");
		question21.addAnswer("Vertex C is not on the opposite side of the center", 0, "Wrong!");
		lang.addMCQuestion(question21);
		
		lang.nextStep();
		// insert quiz 2.2
		MultipleChoiceQuestionModel question22 = new MultipleChoiceQuestionModel("Q2.2");
		question22.setPrompt("Does this mean the algorithm has failed?");
		question22.addAnswer("No, it will find a separating axis when it iterates over B", 1, "Correct!");
		question22.addAnswer("No, it will find a separating axis when it iterates over C", 0, "Wrong!");
		question22.addAnswer("Yes, the algorithm cannot find a separating axis for this case", 0, "Wrong!");
		lang.addMCQuestion(question22);
		
		lang.nextStep();
		// insert answer to question 2
		objects.add(makeText("The algorithm fails at this stage because vertex B and the circle center are not on opposite sites relative to A", 50, 560));
		objects.add(makeText("The vectors from A to B and from A to the circle's center are shown to illustrate this (recall the preparation question).", 50, 580));
		objects.add(makeText("The algorithm will still succeed, though, once it iterates over vertex B. The resulting separating axis is shown in green.", 50, 600));
		
		Polyline vecAtoB = makeLine(-10, -6, -8, -5, true, true);
		vecAtoB.changeColor("", Color.RED, null, null);
		Polyline vecAtoCenter = makeLine(-10, -6, -6, -5, true, true);
		vecAtoCenter.changeColor("", Color.RED, null, null);
		Polyline sepAxisBtoCenter = makeLine(-8, -5, -6, -5, false, true);
		sepAxisBtoCenter.changeColor("", Color.GREEN, null, null);
		
		objects.add(vecAtoB);
		objects.add(vecAtoCenter);
		objects.add(sepAxisBtoCenter);
		
		lang.nextStep("Final words");
		// clear everything!!
		for(Object o : objects) {
			if(o instanceof Text) {
				((Text)o).hide();
			}
			else if(o instanceof Triangle) {
				((Triangle)o).hide();
			}
			else if(o instanceof Circle) {
				((Circle)o).hide();
			}
			else if(o instanceof Polyline) {
				((Polyline)o).hide();
			}
		}
	}
	
	public void showOutroText() {
		// final words
		Text header = makeText("Final words", 100, 100);
		header.changeColor("", Color.BLUE, null, null);
		
		makeText("SAT for circles and triangles is a special case.", 100, 150);
		makeText("The case can be generalized for circle/polygon or polygon/polygon, as long as the polygons are convex.", 100, 170);
		makeText("The principles remain roughly the same, the biggest difference being the number of vertices and edges that are involved.", 100, 190);
		makeText("Also, keep in mind that we could short-circuit out of the algorithm the moment we find one separating axis.", 100, 230);
		makeText("I have not done so for illustration purposes, so that all cases are shown at all times.", 100, 250);
		makeText("Finally, when doing the test in 3D, you would also have to take the triangle's face (the plane spanned by its vertices) into account.", 100, 290);
	}
	
	
	public void showSourceCodeVertexSA() {
		sc = lang.newSourceCode(new Coordinates(10, 50), "sourceCode", null, sourceCodeProps);
		
		// add code lines (the actual sorting algo)
		// parameters: code itself, name (can be null), indention level, display options
		sc.addCodeLine("boolean isVertexSA(Circle circle, Triangle triangle, Vector2f vertex) {", null, 0, null);
		sc.addCodeLine("float distToCenter = vertex.dist(circle.center);", null, 1, null);
		sc.addCodeLine("Vector2f vertexToCenter = circle.center.sub(vertex);", null, 1, null);
		sc.addCodeLine("Vector2f vertexToNeighbor1;", null, 1, null);
		sc.addCodeLine("Vector2f vertexToNeighbor2;", null, 1, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("if(vertex == triangle.A) {", null, 1, null);
		sc.addCodeLine("vertexToNeighbor1 = triangle.B.sub(vertex);	// AB", null, 2, null);
		sc.addCodeLine("vertexToNeighbor2 = triangle.C.sub(vertex);	// AC", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("else if(vertex == triangle.B) {", null, 1, null);
		sc.addCodeLine("vertexToNeighbor1 = triangle.A.sub(vertex);	// BA", null, 2, null);
		sc.addCodeLine("vertexToNeighbor2 = triangle.C.sub(vertex); // BC", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("else if(vertex == triangle.C) {", null, 1, null);
		sc.addCodeLine("vertexToNeighbor1 = triangle.A.sub(vertex); // CA", null, 2, null);
		sc.addCodeLine("vertexToNeighbor2 = triangle.B.sub(vertex); // CB", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("// they are on opposite sides", null, 1, null);
		sc.addCodeLine("// if the dot product is less than zero", null, 1, null);
		sc.addCodeLine("boolean isOppositeNeighbor1 = ", null, 1, null);
		sc.addCodeLine("vertexToCenter.dot(vertexToNeighbor1) < 0;", null, 2, null);
		sc.addCodeLine("boolean isOppositeNeighbor2 = ", null, 1, null);
		sc.addCodeLine("vertexToCenter.dot(vertexToNeighbor2) < 0;", null, 2, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("return circle.radius < distToCenter", null, 1, null);
		sc.addCodeLine("&& isOppositeNeighbor1 && isOppositeNeighbor2;", null, 2, null);
	} // showSourceCodeVertexSA()
	
	
	public void showSourceCodeEdgeSA() {
		sc = lang.newSourceCode(new Coordinates(10, 50), "sourceCode", null, sourceCodeProps);
		
		// add code lines (the actual sorting algo)
		// parameters: code itself, name (can be null), indention level, display options
		sc.addCodeLine("boolean isEdgeSA(Circle circle, Triangle triangle, Vector2f edge, Vector2f start, Vector2f end, Vector2f other) {", null, 0, null);
		sc.addCodeLine("Vector2f normalizedEdge = edge.normalize();", null, 1, null);
		sc.addCodeLine("Vector2f vecToCircle;", null, 1, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("if(edge == triangle.edgeAB) {", null, 1, null);
		sc.addCodeLine("vecToCircle = circle.center.sub(triangle.A);", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("else if(edge == triangle.edgeAC) {", null, 1, null);
		sc.addCodeLine("vecToCircle = circle.center.sub(triangle.A);", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("else if(edge == triangle.edgeBC) {", null, 1, null);
		sc.addCodeLine("vecToCircle = circle.center.sub(triangle.B);", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("float dot = normalizedEdge.dot(vecToCircle);", null, 1, null);
		sc.addCodeLine("Vector2f closestPointToCircle;", null, 1, null);
		sc.addCodeLine("if(dot <= 0) {", null, 1, null);
		sc.addCodeLine("closestPointToCircle = start;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("else if(dot >= edge.length()) {", null, 1, null);
		sc.addCodeLine("closestPointToCircle = end;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("else {", null, 1, null);
		sc.addCodeLine("closestPointToCircle = ", null, 2, null);
		sc.addCodeLine("normalizedEdge.mul(dot).add(start);", null, 3, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("// check if the other vertex", null, 1, null);
		sc.addCodeLine("// (the one that is not part of the current edge)", null, 1, null);
		sc.addCodeLine("// and the circle-center lie on opposite sides of the edge", null, 1, null);
		sc.addCodeLine("Vector2f vecFromClosestPointToCircle 		= ", null, 1, null);
		sc.addCodeLine("circle.center.sub(closestPointToCircle);", null, 2, null);
		sc.addCodeLine("Vector2f vecFromClosestPointToOtherVertex 	= ", null, 1, null);
		sc.addCodeLine("other.sub(closestPointToCircle); ", null, 2, null);
		sc.addCodeLine("boolean isOppositeOtherVertex 				= vecFromClosestPointToCircle.dot(vecFromClosestPointToOtherVertex) < 0;", null, 1, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("return circle.radius < vecFromClosestPointToCircle.length() && isOppositeOtherVertex;", null, 1, null);
	} // showSourceCodeEdgeSA()
    
    
    
    
    
    
    
    
    // ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
	// ================================================================================================================================
    
    

    public String getName() {
        return "Separating Axis Theorem (Circle - Triangle)";
    }

    public String getAlgorithmName() {
        return "Separating Axis Theorem";
    }

    public String getAnimationAuthor() {
        return "Bekir �zkara";
    }

    public String getDescription(){
        return " The Separating Axis Theorem (SAT) can be used to check for collisions between two polygons or"
 +"\n"
 +" between a circle and a polygon. The polygons have to be convex, which, roughly speaking, means that "
 +"\n"
 +"any line drawn through the shape crosses it twice, and no more. "
 +"\n"
 +"To help visualizing it in your head: if there is a separating axis, you can draw a line (perpendicular to that "
 +"\n"
 +"separating axis) which does not touch the objects -> no collision."
 +"\n"
 +"\n"
 +"In the animation there is a small coordinate system. A smaller range was needed because otherwise "
 +"\n"
 +"normalized vectors wouldn't be drawn correctly."
 +"\n"
 +"The box on the right hand side keeps track of important values/calculations that are needed for the test."
        +"\n"
        +"\n"
        +"When adjusting the primitives, you must obey the following conditions:"
        +"\n"
        +"\tEvery vertex must have a value that is in the interval [-5,5]"
        +"\n"
        +"\tradius + |circle_center_x/y| <= 5"
        +"\n"
        +"These conditions ensure that everything is kept within the coordinate system."
        +"\n"
        +"Also note that nothing prevents you from putting all three vertices onto the same position."
        +" So make sure you construct your triangle in a semantically correct manner, which shouldn't be hard to do.";
    }

    public String getCodeExample(){
        return "// the implementations of isVertexSA() and isEdgeSA() are shown in the animation"
 +"\n"
 +"public static boolean hasSA(Circle circle, Triangle triangle) {"
 +"\n"
 +"  boolean separatedByVertexA = isVertexSA(circle, triangle, triangle.A);"
 +"\n"
 +"  boolean separatedByVertexB = isVertexSA(circle, triangle, triangle.B);"
 +"\n"
 +"  boolean separatedByVertexC = isVertexSA(circle, triangle, triangle.C);"
 +"\n"
 +"		"
 +"\n"
 +"  boolean separatedByEdgeAB = isEdgeSA(circle, triangle, triangle.edgeAB, triangle.A, triangle.B, triangle.C);"
 +"\n"
 +"  boolean separatedByEdgeAC = isEdgeSA(circle, triangle, triangle.edgeAC, triangle.A, triangle.C, triangle.B);"
 +"\n"
 +"  boolean separatedByEdgeBC = isEdgeSA(circle, triangle, triangle.edgeBC, triangle.B, triangle.C, triangle.A);"
 +"\n"
 +"		"
 +"\n"
 +"  return separatedByVertexA || separatedByVertexB || separatedByVertexC || separatedByEdgeAB || separatedByEdgeAC || separatedByEdgeBC;"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		vertex_a_x = (double)primitives.get("vertex_a_x");
		vertex_a_y = (double)primitives.get("vertex_a_y");
        vertex_b_x = (double)primitives.get("vertex_b_x");
        vertex_b_y = (double)primitives.get("vertex_b_y");
        vertex_c_x = (double)primitives.get("vertex_c_x");
        vertex_c_y = (double)primitives.get("vertex_c_y");
        circle_center_x = (double)primitives.get("circle_center_x");
        circle_center_y = (double)primitives.get("circle_center_y");
        radius = (double)primitives.get("radius");
        
        if(!(radius > 0)) {
        	throw new IllegalArgumentException("Radius must be greater than zero!");
        }
        else if(!(radius + Math.abs(circle_center_x) <= 5)) {
        	throw new IllegalArgumentException("radius + |circle_center_x| must be at most 5");
        }
        else if(!(radius + Math.abs(circle_center_y) <= 5)) {
        	throw new IllegalArgumentException("radius + |circle_center_y| must be at most 5");
        }
        else if(!vertexWithinBounds(vertex_a_x)) {
        	throw new IllegalArgumentException("The value of vertex A.x must be within [-5, 5]");
        }
		else if(!vertexWithinBounds(vertex_a_y)) {
			throw new IllegalArgumentException("The value of vertex A.y must be within [-5, 5]");
		}
		else if(!vertexWithinBounds(vertex_b_x)) {
			throw new IllegalArgumentException("The value of vertex B.x must be within [-5, 5]");
		}
		else if(!vertexWithinBounds(vertex_b_y)) {
			throw new IllegalArgumentException("The value of vertex B.y must be within [-5, 5]");
		}
		else if(!vertexWithinBounds(vertex_c_x)) {
			throw new IllegalArgumentException("The value of vertex C.x must be within [-5, 5]");
		}
		else if(!vertexWithinBounds(vertex_c_y)) {
			throw new IllegalArgumentException("The value of vertex C.y must be within [-5, 5]");
		}
        
		return true;
	}
	
	private boolean vertexWithinBounds(double vertex) {
		return vertex >= -5 && vertex <= 5;
	}
}
