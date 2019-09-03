package generators.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class GrahamScan {

	private Vector<Point> inputSetValues = new Vector<Point>();
	private Stack<Point> stackValues = new Stack<Point>();

	private String[] inputSet;
	private String[] stack;

	// Animal related variables
	private Language lang;
	private SourceCode sc;
	private ArrayMarker iMarker;

	private StringArray inputSetArray;
	private StringArray stackArray;

	private int numPoints = 10;
	private CircleProperties cProps;
	private ArrayProperties arrayProps = new ArrayProperties();

	public GrahamScan(String plaintext, int numPoints, Language lang) {
		// Create a String Array from the incoming String
		this.lang = lang;
		this.lang.setStepMode(true);

		int numPoints2 = numPoints;
    if(numPoints2 < 3)
		{
			numPoints2 = 3;
		}
		
		this.numPoints = numPoints2;
		
		prepareGrahamScan();
	}

	private void prepareGrahamScan() {
		inputSetValues.clear();
		stackValues.clear();
		
		inputSet = new String[numPoints];
		stack = new String[numPoints];

		for (int i = 0; i < numPoints; i++) {
			inputSetValues.add(new Point(i));
			inputSet[i] = inputSetValues.get(i).name;
			stack[i] = new String("    ");
		}
	}
	
	public ArrayProperties getArrayProps()
	{
		return arrayProps;
	}

	private void prepareAnimal() {

		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(20, 20), "Graham Scan - Convex Hulls", "header", null, textProps);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
		lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "headerBackground", null, rectProps);

		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		Text inputSetLabel = lang.newText(new Coordinates(10, 130), "Input Set Q:", "inputSetLabel", null, textProps);

		inputSetArray = lang.newStringArray(new Offset(10, 2, inputSetLabel, "NE"), inputSet, "inputSet", null, arrayProps);

		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		Text stackLabel = lang.newText(new Offset(0, 20, inputSetLabel, "SW"), "Stack S:", "stackLabel", null, textProps);

		stackArray = lang.newStringArray(new Offset(10, -2, stackLabel, "NE"), stack, "stack", null, arrayProps);

		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// now, create the source code entity
		sc = lang.newSourceCode(new Offset(0, 70, stackLabel, "SW"), "sourceCode", null, scProps);
		// Line, name, indentation, display delay
		sc.addCodeLine("let Q[0] be the point in Q with the lowest y-coordinate", null, 0, null);
		sc.addCodeLine("if there's a draw, choose the one with the lowest x-coordinate", null, 1, null);
		sc.addCodeLine("let Q[1], Q[2], ..., Q[m] be the remaining points in Q", null, 0, null);
		sc.addCodeLine("sorted by polar angle in counterclockwise order around Q[0]", null, 1, null);
		sc.addCodeLine("(if some Q[i] have the same angle, discard all but the farthest from Q[0])", null, 1, null);
		sc.addCodeLine("PUSH(Q[0], S)", null, 0, null);
		sc.addCodeLine("PUSH(Q[1], S)", null, 0, null);
		sc.addCodeLine("PUSH(Q[2], S)", null, 0, null);
		sc.addCodeLine("for i=3 to m", null, 0, null);
		sc.addCodeLine("do while the angle formed by points NEXT-TO-TOP(S), TOP(S) and Q[i] makes a nonleft turn", null, 1, null);
		sc.addCodeLine("POP(S)", null, 2, null);
		sc.addCodeLine("PUSH(Q[i], S)", null, 1, null);
		sc.addCodeLine("return S", null, 0, null);

		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode descripion = lang.newSourceCode(new Offset(50, 0, sc, "NE"), "description", null, scProps);

		descripion.addCodeLine("Graham Scan finds a convex hull to a set of 2D points by starting", null, 0, null);
		descripion.addCodeLine(" at the point with the lowest y-coordinate and walking along the other points", null, 0, null);
		descripion.addCodeLine(" in an angular manner.", null, 0, null);
		descripion.addCodeLine(" It then discards points already lying inside the hull unless there are", null, 0, null);
		descripion.addCodeLine(" no points left.", null, 0, null);

		cProps = new CircleProperties();
		cProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		cProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		for(int i = 0; i < inputSetValues.size(); i++)
		{
			inputSetValues.get(i).label = lang.newText(new Coordinates((int)inputSetValues.get(i).x, 245 - (int)inputSetValues.get(i).y), inputSetValues.get(i).name, inputSetValues.get(i).name, null);
			inputSetValues.get(i).circle = lang.newCircle(new Coordinates((int)inputSetValues.get(i).x, 260 - (int)inputSetValues.get(i).y), 3, inputSetValues.get(i).name, null, cProps);
		}
		

		//Polyline convexHull = lang.newPolyline(new Coordinates[0], "convexHull", polyProps);

		lang.nextStep("Beginning of Animation - find Point with lowest y-coordinate");
	}

	public float computeDistance(Point p0, Point p1)
	{
		return (float)Math.sqrt(p0.x * p1.x + p0.y * p1.y);
	}
	
	//helper method neede for algorithm
	public Point nextToTop()
	{
		Point p0, p1;
		p0 = stackValues.pop();
		p1 = stackValues.peek();
		stackValues.push(p0);
		return p1;
	}
	
	public void findConvexHull() {
		//--------------------------------------------------
		// find the point with the lowest y coordinate
		//--------------------------------------------------
				
		int lowest = 0;
		for(int i = 1; i < numPoints; i++)
		{
			if(inputSetValues.get(i).y < inputSetValues.get(lowest).y)
			{
				lowest = i;
			}
			else if(inputSetValues.get(i).y == inputSetValues.get(lowest).y)
			{
				if(inputSetValues.get(i).x < inputSetValues.get(lowest).x)
				{
					lowest = i;
				}
			}
		}
		
		//--------------------------------------------------
		// compute angles for all points
		//--------------------------------------------------
		
		for(int i = 0; i < numPoints; i++)
		{
			if(i == lowest)
			{
				inputSetValues.get(i).angle = 0;
			}
			else
			{
				float dx = inputSetValues.get(i).x - inputSetValues.get(lowest).x;
				float dy = inputSetValues.get(i).y - inputSetValues.get(lowest).y;
				
				if(dx > 0.0)
				{
					inputSetValues.get(i).angle = (float)Math.atan(dy / dx);
				}
				else if(dx < 0.0)
				{
					inputSetValues.get(i).angle = (float)Math.atan(dy / dx) + ((float)Math.PI );
				}
				else // as we have the lowest point, the angle can only lie between 0 and 180° 
				{//the only way to divide by zero here is at 90°
					inputSetValues.get(i).angle = ((float)Math.PI ) * 0.5f;
				}
			}
		}
		
		Point p = inputSetValues.remove(lowest);
				
		System.out.println("First Point: \n" + p.toString());

		//--------------------------------------------------
		// sort by angles, removing ones with same angle
		//--------------------------------------------------
		Collections.sort(inputSetValues);
		
		//insert p0 back to the front
		inputSetValues.insertElementAt(p, 0);
		
		for(int i = 0; i < inputSetValues.size(); i++)
		{
			inputSetValues.get(i).name = "P" + i;
			System.out.println(inputSetValues.get(i));
		}
		
		//animal code was kept on hold till here
		//so we can label the points in the correct order
		prepareAnimal();
		sc.highlight(0);
		sc.highlight(1);
		lang.nextStep("Compute Angles and sort");
		
		
		p.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN, null, null);
		p.circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN, null, null);
		
		lang.nextStep();
		
		sc.unhighlight(0);
		sc.unhighlight(1);
		sc.highlight(2);
		sc.highlight(3);
		sc.highlight(4);
		
		Vector<Integer> markedForRemoval = new Vector<Integer>();
		
		int j = 0;
		while(j < inputSetValues.size() - 1)
		{
			int offset = 1;
			while(j + offset < inputSetValues.size() && inputSetValues.get(j).angle == inputSetValues.get(j+offset).angle && j < inputSetValues.size() - 1)
			{
				float distA = computeDistance(p, inputSetValues.get(j));
				float distB = computeDistance(p, inputSetValues.get(j+offset));
				
				if(distA < distB)
				{
					System.out.println("Found point to remove: " + j);
					markedForRemoval.add(j);
					j++;
				}
				else
				{
					System.out.println("Found point to remove: " + (j+offset));
					markedForRemoval.add(j+offset);
					offset++;
				}
			}
			
			j += offset;
		}
		
		//remove elements, back to front
		for(int i = markedForRemoval.size() - 1; i >= 0; i--)
		{
			int delIdx = markedForRemoval.get(i);
			System.out.println("Deleting: " + delIdx);
			Point p1 = inputSetValues.remove(delIdx);
			p1.circle.hide();
			p1.label.hide();
			//bubble sort like change:
			inputSetArray.put(delIdx, "", null, null);
			for(int j1 = delIdx; j1 < numPoints - 1; j1++)
			{
				inputSetArray.swap(j1, j1+1, null, null);
			}
		}
		
		System.out.println("\n\nAfter cleaning up:");
		for(int i = 0; i < inputSetValues.size(); i++)
		{
			System.out.println(inputSetValues.get(i));
		}		
		
		lang.nextStep("Push(Q[0], S)");
		//--------------------------------------------------
		//insert first 3 points into stack
		//--------------------------------------------------
		sc.unhighlight(2);
		sc.unhighlight(3);
		sc.unhighlight(4);
		sc.highlight(5);
		stackValues.push(p);
		stackArray.put(0, p.name, null, null);
		inputSetValues.get(0).circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED, null, null);
		inputSetValues.get(0).circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null, null);
				
		lang.nextStep("Push(Q[1], S)");
		sc.unhighlight(5);
		sc.highlight(6);		
		stackValues.push(inputSetValues.get(1));
		stackArray.put(1, inputSetValues.get(1).name, null, null);
		inputSetValues.get(1).circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED, null, null);
		inputSetValues.get(1).circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null, null);
		
		lang.nextStep("Push(Q[2], S)");
		sc.unhighlight(6);
		sc.highlight(7);
		stackValues.push(inputSetValues.get(2));
		stackArray.put(2, inputSetValues.get(2).name, null, null);
		inputSetValues.get(2).circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED, null, null);
		inputSetValues.get(2).circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null, null);
		
		//--------------------------------------------------
		//main loop comes here
		//--------------------------------------------------
		PolylineProperties polyProps = new PolylineProperties();
		polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);

		Node[] anglePoints = new Coordinates[3];
		for(int i = 0; i < 3; i++)
		{
			anglePoints[i] = new Coordinates((int)inputSetValues.get(0).x, (int)inputSetValues.get(0).y);
		}		
		
		lang.nextStep("for all further points..");
		sc.unhighlight(7);
		ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
		arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		iMarker = lang.newArrayMarker(inputSetArray, 0, "i", null, arrayIMProps);
		
		for(int i = 3; i < inputSetValues.size(); i++){
			iMarker.move(i, new TicksTiming(15), null);
			sc.unhighlight(11);
			sc.highlight(8);
			inputSetValues.get(i).circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE, new TicksTiming(15), null);
			inputSetValues.get(i).circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE, new TicksTiming(15), null);
			lang.nextStep();
			
			sc.unhighlight(8);
			sc.highlight(9);
			
			boolean nonleft = nonleftTurn(nextToTop(), stackValues.peek(), inputSetValues.get(i), anglePoints);
			Polyline angle = lang.newPolyline(anglePoints, "anglePoints", null, polyProps);
			
			while(nonleft)
			{
				lang.nextStep();
				sc.unhighlight(9);
				sc.highlight(10);
				System.out.println("Found nonleft Turn for " + i);
				Point popped = stackValues.pop();
				stackArray.put(stackValues.size() , "    ", new TicksTiming(15), null);
				popped.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK, new TicksTiming(15), null);
				popped.circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, new TicksTiming(15), null);
				
				lang.nextStep();
				sc.unhighlight(10);
				sc.highlight(9);
				nonleft = nonleftTurn(nextToTop(), stackValues.peek(), inputSetValues.get(i), anglePoints);
				angle.hide();
				angle = lang.newPolyline(anglePoints, "anglePoints", null, polyProps);
			}
			
			lang.nextStep();
			sc.unhighlight(10);
			sc.unhighlight(9);
			sc.highlight(11);
			stackArray.put(stackValues.size() , inputSetValues.get(i).name, new TicksTiming(15), null);
			stackValues.push(inputSetValues.get(i));
			angle.hide();
			
			
			lang.nextStep();
			inputSetArray.unhighlightElem(0, i, null, null);
			sc.unhighlight(8);
			inputSetValues.get(i).circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED, new TicksTiming(15), null);
			inputSetValues.get(i).circle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, new TicksTiming(15), null);
		}
		
		lang.nextStep("finished algorithm");
		
		System.out.println("\n\nDone Computing, convex hull is:");
		Node[] nodes = new Coordinates[stackValues.size() + 1];
		for(int i = 0; i < stackValues.size(); i++)
		{
			System.out.println(stackValues.get(i));
			nodes[i] = new Coordinates((int)stackValues.get(i).x, 260 - (int)stackValues.get(i).y);
		}
		nodes[stackValues.size()] = new Coordinates((int)stackValues.get(0).x, 260 - (int)stackValues.get(0).y);
		
		polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		lang.newPolyline(nodes, "convexHull", null, polyProps);
	}

	//performing a cross product of the vectors embedded in the x-y-plane results in a non-negative z-coordinate
	//if we have a nonleft turn
	private boolean nonleftTurn(Point nextToTop, Point top, Point point, Node[] anglePoints) {
		float dx1, dy1, dx2, dy2;
		dx1 = top.x - nextToTop.x;
		dy1 = top.y - nextToTop.y;
		
		dx2 = top.x - point.x;
		dy2 = top.y - point.y;
		
		anglePoints[0] = new Coordinates((int)nextToTop.x, 260 - (int)nextToTop.y);
		anglePoints[1] = new Coordinates((int)top.x, 260 - (int)top.y);
		anglePoints[2] = new Coordinates((int)point.x, 260 - (int)point.y);
		
		return 0.0f <= (dx1*dy2 - dy1*dx2);
	}
}
