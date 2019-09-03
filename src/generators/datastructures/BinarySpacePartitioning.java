package generators.datastructures;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.binarySpacePartitioning.BspNode;
import generators.helpers.binarySpacePartitioning.Node;
import generators.helpers.binarySpacePartitioning.Polygon;
import generators.helpers.binarySpacePartitioning.Vector;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * The generator generating animal script code demonstrating the construction
 * and example usage of a node storing auto partitioned binary space 
 * partitioning tree.
 */
public class BinarySpacePartitioning implements Generator {

	private Language lang;
	
//	private static final String AUTHORS =
//		"Martin Tschirsich, Tjark Vandommele et errator";
	
	private static final String ALGORITHM = 
		"Binary Space Partitioning";
	
	private static final String DESCRIPTION = 
		"A Binary Space Partitioning (BSP) tree represents a " +
		"recursive, hierarchical partitioning, or subdivision, of " +
		"n-dimensional space into convex subspaces. BSP tree construction " +
		"is a process which takes a subspace and partitions it by any " +
		"hyperplane that intersects the interior of that subspace. The " +
		"result is two new subspaces that can be further partitioned by " +
		"recursive application of the method." +
        "\n" +
		"A \"hyperplane\" in n-dimensional space is an n-1 dimensional object " +
		"which can be used to divide the space into two half-spaces. For " +
		"example, in three dimensional space, the \"hyperplane\" is a plane. " +
		"In two dimensional space, a line is used. " +
		"\n" +
		"BSP trees are extremely versatile, because they are powerful " +
		"sorting and classification structures. They have uses ranging from " +
		"hidden surface removal and ray tracing hierarchies to solid " +
		"modeling and robot motion planning. ";
	
	private static final String SOURCE_CODE = 
		"P : Polygon-Set<br>" +
		"<br>" +
		"BUILD-BSP(P)<br>" +
		" s = PICK-SPLITTER(P)<br>" +
		"<br>" +
		" P+ = {p in P | CLASSIFY(s, p) == POSITIVE}<br>" +
		" P- = {p in P | CLASSIFY(s, p) == NEGATIVE}<br>" +
		"<br>" +
		" if not EMPTY(P+) then f = BUILD-BSP(P+)<br>" +
		" if not EMPTY(P-) then b = BUILD-BSP(P-)<br>" +
		"<br>" +
		" return MAKE-NODE(s, f, b)<br>" +
		"<br>" +
		"<br>" +
		"MAKE-NODE(s, f, b)<br>" +
		" creates a node with splitter s, <br>" +
		" children front f and back b<br>" +
		"<br>" +
		"<br>" +
		"x : BSP-Node<br>" +
		"c : Eye-Position<br>" +
		"<br>" +
		"PAINT-BSP(x, c)<br>" +
		" if CLASSIFY(splitter[x], c) == POSITIVE then<br>" +
		"  PAINT-BSP(back[x], c)<br>" +
		"  DRAW(splitter[x])<br>" +
		"  PAINT-BSP(front[x], c)<br>" +
		" else<br>" +
		"  PAINT-BSP(front[x], c)<br>" +
		"  DRAW(splitter[x])<br>" +
		"  PAINT-BSP(back[x], c)<br>" +
		"<br><br>You are visitor number <img src=\"http://usr.bplaced.de/PAVCounter/counter.php\"> asking himself the question why there is a webcounter where none should be.";

	public void init() {	
	}
	
	/**
	 * The script generating method.
	 * At first, general textual information concerning construction and 
	 * traversal are shown, followed by detailed construction animation.
	 * Thereafter, example traversal (drawing order) and example rendering in 
	 * 3D are displayed.
	 */
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		lang = new AnimalScript(ALGORITHM, "Martin Tschirsich, Tjark Vandommele", 1000, 1000);
		lang.setStepMode(true);
		
		// User-defined properties:
		Boolean balanced = (Boolean) primitives.get("Balanced tree?");
		Integer polygonCount = (Integer) primitives.get("Number of polygons");
		if (polygonCount < 1) polygonCount = 1;
		else if (polygonCount > 40) polygonCount = 40;
		
		// Draw header:
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		algoanim.primitives.Text h1 = lang.newText(new Coordinates(20, 15), "Binary Space Partitioning: Auto Partitioning Node Storing Tree", "h1", null, textProps);
		
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		lang.newRect(new Offset(-15, 5, h1, "SW"), new Offset(10, 10, h1, "SE"), "h1Rect1", null, rectProps);
		lang.newRect(new Offset(10, 0, h1, "NE"), new Offset(12, 10, h1, "SE"), "h1Rect2", null, rectProps);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
		lang.newRect(new Offset(-15, 0, h1, "NW"), new Offset(12, 10, h1, "SE"), "h1Rect1", null, rectProps);
		
		// Draw introduction to construction:
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.BOLD, 20));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
		SourceCode sc0 = lang.newSourceCode(new Coordinates(10, 50), "Code", null, scProps);
	
		sc0.addCodeLine("Introduction:", null, 0, null);
		sc0.addCodeLine(" ", null, 0, null);
		sc0.addCodeLine("How to build a BSP-Tree given a set of polygones?", null, 0, null);
		sc0.addCodeLine(" ", null, 1, null);
		sc0.addCodeLine("1. Choose the partition plane from the set of polygons.", null, 1, null);
		sc0.addCodeLine("   If a polygon happens to span the partition plane, it will be split.", null, 1, null);
		sc0.addCodeLine("   A poor choice of the partition plane can result in many such splits", null, 1, null);
		sc0.addCodeLine("   => trade off between well balanced tree and large number of splits. (not discussed)", null, 1, null);
		sc0.addCodeLine(" ", null, 1, null);
		sc0.addCodeLine("2. Classify the remaining polygons according to the partition plane", null, 1, null);
		sc0.addCodeLine("   => front polygons / back polygons", null, 1, null);
		sc0.addCodeLine(" ", null, 1, null);
		sc0.addCodeLine("3. Recursively build child nodes from front polygons / back polygons", null, 1, null);
		lang.nextStep();
		sc0.hide();
		
		// Introduction to drawing:
		SourceCode sc3 = lang.newSourceCode(new Coordinates(10, 50), "Code", null, scProps);
		
		sc3.addCodeLine("Introduction:", null, 0, null);
		sc3.addCodeLine(" ", null, 0, null);
		sc3.addCodeLine("How to draw polygons from back to front using a BSP-Tree?", null, 0, null);
		sc3.addCodeLine(" ", null, 1, null);
		sc3.addCodeLine("BSP trees are used to improve rendering performance in calculating visible triangles", null, 0, null);
		sc3.addCodeLine("for the painter's algorithm. (Linear time traversal from an arbitrary viewpoint.)", null, 0, null);
		sc3.addCodeLine("Since a painter's algorithm works by drawing polygons farthest from the eye first,", null, 0, null);
		sc3.addCodeLine("the following code recurses to the bottom of the tree and draws the polygons. As the", null, 0, null);
		sc3.addCodeLine("recursion unwinds, polygons closer to the eye are drawn over far polygons.", null, 0, null);
		sc3.addCodeLine("Because the BSP tree already splits polygons into trivial (convex) pieces,", null, 0, null);
		sc3.addCodeLine("the hardest part of the painter's algorithm is already solved.", null, 0, null);
		sc3.addCodeLine(" ", null, 1, null);
		sc3.addCodeLine("Given an eye position (camera) and the BSP-root:", null, 1, null);
		sc3.addCodeLine(" ", null, 1, null);
		sc3.addCodeLine("    1. paint the childnode behind the splitter (partition plane)", null, 1, null);
		sc3.addCodeLine("    2. draw the splitter", null, 1, null);
		sc3.addCodeLine("    3. paint the polygons in front of the splitter (partition plane)", null, 1, null);
		lang.nextStep();
		sc3.hide();
		
		// Create random polygons:
		LinkedList<Polygon> polygons = new LinkedList<Polygon>();
		int posX = 10;
		int posY = 50;
		int sizeX = 400;
		int sizeY = 400;
		for (int i = 0; i < polygonCount; i++) {
			Vector[] points = new Vector[] {new Vector(posX + (int)(Math.random()*sizeX), posY + (int)(Math.random()*sizeY)), new Vector(posX + (int)(Math.random()*sizeX), posY + (int)(Math.random()*sizeY))};
			Polygon polygon = new Polygon(points);
			
			polygons.add(polygon);
		}

		// Draw polygons:
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
		lang.newRect(new Coordinates(10, 50), new Coordinates(410, 450), "Rectangle", null, rectProps);
		
		for (Polygon polygon : polygons) {
			polygon.draw(lang, new Color(200, 200, 200));
		}
		
		// Display some information about coloring etc:
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(200, 255, 200));
		Rect fieldRect1 = lang.newRect(new Coordinates(430, 50), new Coordinates(450, 70), "fieldRect1", null, rectProps);
		
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text fieldText1 = lang.newText(new Offset(10, 0, fieldRect1, "NE"), "= positive halfspace", "fieldRect1Text1", null, textProps);

		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 200, 200));
		Rect fieldRect2 = lang.newRect(new Coordinates(430, 75), new Coordinates(450, 95), "fieldRect2", null, rectProps);
		
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text fieldText2 = lang.newText(new Offset(10, 0, fieldRect2, "NE"), "= negative halfspace", "fieldRect2Text1", null, textProps);
		
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 200, 200));
		Rect fieldRect3 = lang.newRect(new Coordinates(430, 110), new Coordinates(450, 110), "fieldRect3", null, rectProps);
		
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text fieldText3 = lang.newText(new Offset(10, -10, fieldRect3, "NE"), "= partition plane", "fieldRect3Text1", null, textProps);	
		
		// Show construction algorithm:
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.BOLD, 20));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
		SourceCode sc1 = lang.newSourceCode(new Coordinates(430, 120), "Code", null, scProps);
		
		sc1.addCodeLine("P : Polygon-Set", null, 0, null);
		sc1.addCodeLine(" ", null, 0, null);
		sc1.addCodeLine("BUILD-BSP(P)", null, 0, null);
		sc1.addCodeLine("s = PICK-SPLITTER(P)", null, 1, null);
		sc1.addCodeLine(" ", null, 1, null);
		sc1.addCodeLine("P- = {p in P | CLASSIFY(s, p) == NEGATIVE}", null, 1, null);
		sc1.addCodeLine("P+ = {p in P | CLASSIFY(s, p) == POSITIVE}", null, 1, null);
		sc1.addCodeLine(" ", null, 1, null);
		sc1.addCodeLine("if not EMPTY(P+) then f = BUILD-BSP(P+)", null, 1, null);
		sc1.addCodeLine("if not EMPTY(P-) then b = BUILD-BSP(P-)", null, 1, null);
		sc1.addCodeLine(" ", null, 1, null);
		sc1.addCodeLine("return MAKE-NODE(s, f, b)", null, 1, null);
		sc1.addCodeLine(" ", null, 0, null);
		sc1.addCodeLine(" ", null, 0, null);
		sc1.addCodeLine("MAKE-NODE(s, f, b)", null, 0, null);
		sc1.addCodeLine("creates a node with splitter s, ", null, 1, null);
		sc1.addCodeLine("children front f and back b", null, 1, null);
		
		lang.nextStep();
		
		// Define clipping region for BSP-drawing area:
		LinkedList<Polygon> clippers = new LinkedList<Polygon>();
		clippers.push(new Polygon(new Vector[] {new Vector(posX, posY), new Vector(posX + sizeX, posY)}));
		clippers.push(new Polygon(new Vector[] {new Vector(posX + sizeX, posY), new Vector(posX + sizeX, posY + sizeY)}));
		clippers.push(new Polygon(new Vector[] {new Vector(posX + sizeX, posY + sizeY), new Vector(posX, posY + sizeY)}));
		clippers.push(new Polygon(new Vector[] {new Vector(posX, posY + sizeY), new Vector(posX, posY)}));
		
		// Build real BSP-tree displaying recursion steps:
		BspNode bspNode = new BspNode(polygons, lang, clippers, new Node(10, 450, 400), balanced);
		lang.nextStep();
		
		// Draw camera position:
		CircleProperties circleProps = new CircleProperties();
		circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0,0,0));
		circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0,0,0));
		
		Coordinates center = new Coordinates(390, 225);
		lang.newCircle(center, 8, "outerCircle", null, circleProps);
		
		circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,0,0));
		lang.newCircle(center, 6, "innerCircle", null, circleProps);
		
		// Display some information about coloring etc:
		fieldRect1.hide();
		fieldRect2.hide();
		fieldRect3.hide();
		fieldText1.hide();
		fieldText2.hide();
		fieldText3.hide();
		
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 255, 0));
		Rect fieldRect4 = lang.newRect(new Coordinates(430, 50), new Coordinates(450, 70), "fieldRect4", null, rectProps);
		
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text fieldText4 = lang.newText(new Offset(10, 0, fieldRect4, "NE"), "= displayed polygon", "fieldRect4Text1", null, textProps);

		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 0, 0));
		Rect fieldRect5 = lang.newRect(new Coordinates(430, 75), new Coordinates(450, 95), "fieldRect5", null, rectProps);
		
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text fieldText5 = lang.newText(new Offset(10, 0, fieldRect5, "NE"), "= traversed polygon", "fieldRect5Text1", null, textProps);
		
		circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0,0,0));
		Coordinates center2 = new Coordinates(440, 110);
		Circle outerCircle2 = lang.newCircle(center2, 8, "outerCircle", null, circleProps);
		
		circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,0,0));
		Circle innerCircle2 = lang.newCircle(center2, 6, "innerCircle", null, circleProps);
		
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text fieldText6 = lang.newText(new Offset(10, -5, outerCircle2, "NE"), "= camera position", "fieldRect5Text1", null, textProps);

		// Show traversal algorithm:
		sc1.hide();
		SourceCode sc2 = lang.newSourceCode(new Coordinates(430, 120), "Code", null, scProps);
		sc2.addCodeLine("x : BSP-Node", null, 0, null);
		sc2.addCodeLine("c : Eye-Position", null, 0, null);
		sc2.addCodeLine("", null, 0, null);
		sc2.addCodeLine("PAINT-BSP(x, c)", null, 0, null);
		sc2.addCodeLine(" ", null, 1, null);
		sc2.addCodeLine("if CLASSIFY(splitter[x], c) == POSITIVE then", null, 1, null);
		sc2.addCodeLine("PAINT-BSP(back[x], c)", null, 2, null);
		sc2.addCodeLine("DRAW(splitter[x])", null, 2, null);
		sc2.addCodeLine("PAINT-BSP(front[x], c)", null, 2, null);
		sc2.addCodeLine("else", null, 1, null);
		sc2.addCodeLine("PAINT-BSP(front[x], c)", null, 2, null);
		sc2.addCodeLine("DRAW(splitter[x])", null, 2, null);
		sc2.addCodeLine("PAINT-BSP(back[x], c)", null, 2, null);
		
		lang.nextStep();
		
		// Traverse and draw BSP in correct paint order:
		Vector camera = new Vector(390, 225);
		bspNode.traverse(camera, lang);
		lang.nextStep();

		// Draw precalculated 3D example rendering:
		fieldRect4.hide();
		fieldRect5.hide();
		outerCircle2.hide();
		innerCircle2.hide();
		
		fieldText4.hide();
		fieldText5.hide();
		fieldText6.hide();
		
		sc2.hide();
		SourceCode sc4 = lang.newSourceCode(new Coordinates(430, 50), "sc4", null, scProps);
		
		sc4.addCodeLine("Example rendering in 3D:", null, 0, null);
		sc4.addCodeLine("", null, 0, null);
		sc4.addCodeLine("A BSP-Tree was used to determine", null, 0, null);
		sc4.addCodeLine("the correct painting order.", null, 0, null);
		
		lang.addLine("polygon \"0\" (10, 50) (410, 50) (410, 650) (10, 650) color (0, 0, 0) depth 1 filled fillColor (0, 0, 0)");
		sc2.hide();
		lang.nextStep();
		
		lang.setStepMode(false);
		lang.addLine("polygon \"0\" (193, 284) (167, 283) (166, 330) (192, 340) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (151, 287) (129, 284) (125, 339) (149, 353) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)1");
		lang.addLine("polygon \"0\" (92, 290) (77, 286) (70, 352) (84, 372) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (223, 283) (193, 284) (192, 340) (224, 331) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (193, 284) (151, 287) (149, 353) (192, 340) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (151, 287) (92, 290) (84, 372) (149, 353) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (229, 287) (193, 284) (192, 340) (230, 355) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (185, 290) (151, 287) (149, 353) (184, 375) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (117, 295) (92, 290) (84, 372) (109, 407) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (259, 284) (229, 287) (230, 355) (262, 341) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (229, 287) (185, 290) (184, 375) (230, 355) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (185, 290) (117, 295) (109, 407) (184, 375) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (309, 287) (282, 290) (289, 377) (315, 356) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (282, 290) (229, 287) (230, 355) (289, 377) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (282, 290) (241, 296) (245, 411) (289, 377) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (241, 296) (185, 290) (184, 375) (245, 411) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (165, 306) (117, 295) (109, 407) (159, 478) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (241, 296) (165, 306) (159, 478) (245, 411) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (197, 281) (167, 283) (193, 284) (223, 283) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (223, 283) (193, 284) (229, 287) (259, 284) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (259, 284) (229, 287) (282, 290) (309, 287) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (167, 283) (129, 284) (151, 287) (193, 284) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (193, 284) (151, 287) (185, 290) (229, 287) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (229, 287) (185, 290) (241, 296) (282, 290) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (129, 284) (77, 286) (92, 290) (151, 287) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (151, 287) (92, 290) (117, 295) (185, 290) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (185, 290) (117, 295) (165, 306) (241, 296) color (2, 200, 200)  depth 1 filled fillColor (165, 165, 186)");
		lang.addLine("polygon \"0\" (193, 186) (170, 198) (168, 238) (193, 233) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (156, 173) (135, 188) (132, 234) (154, 227) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (105, 154) (89, 174) (83, 227) (99, 217) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (222, 197) (193, 186) (193, 233) (222, 238) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (193, 186) (156, 173) (154, 227) (193, 233) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (156, 173) (105, 154) (99, 217) (154, 227) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (226, 171) (193, 186) (193, 233) (227, 226) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (187, 151) (156, 173) (154, 227) (186, 216) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (129, 122) (105, 154) (99, 217) (124, 202) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (254, 185) (226, 171) (227, 226) (256, 233) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (226, 171) (187, 151) (186, 216) (227, 226) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (187, 151) (129, 122) (124, 202) (186, 216) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (297, 170) (272, 149) (277, 215) (303, 225) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (272, 149) (226, 171) (227, 226) (277, 215) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (272, 149) (235, 119) (237, 200) (277, 215) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (235, 119) (187, 151) (186, 216) (237, 200) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (172, 68) (129, 122) (124, 202) (169, 173) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (235, 119) (172, 68) (169, 173) (237, 200) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (197, 242) (222, 238) (193, 233) (168, 238) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (222, 238) (256, 233) (227, 226) (193, 233) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (256, 233) (303, 225) (277, 215) (227, 226) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (168, 238) (193, 233) (154, 227) (132, 234) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (193, 233) (227, 226) (186, 216) (154, 227) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (227, 226) (277, 215) (237, 200) (186, 216) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (132, 234) (154, 227) (99, 217) (83, 227) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (154, 227) (186, 216) (124, 202) (99, 217) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (186, 216) (237, 200) (169, 173) (124, 202) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (193, 233) (168, 238) (167, 283) (193, 284) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (154, 227) (132, 234) (129, 284) (151, 287) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (99, 217) (83, 227) (77, 286) (92, 290) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (222, 238) (193, 233) (193, 284) (223, 283) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (193, 233) (154, 227) (151, 287) (193, 284) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (154, 227) (99, 217) (92, 290) (151, 287) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (227, 226) (193, 233) (193, 284) (229, 287) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (186, 216) (154, 227) (151, 287) (185, 290) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (124, 202) (99, 217) (92, 290) (117, 295) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (256, 233) (227, 226) (229, 287) (259, 284) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (227, 226) (186, 216) (185, 290) (229, 287) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (186, 216) (124, 202) (117, 295) (185, 290) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (303, 225) (277, 215) (282, 290) (309, 287) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (277, 215) (227, 226) (229, 287) (282, 290) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (277, 215) (237, 200) (241, 296) (282, 290) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (237, 200) (186, 216) (185, 290) (241, 296) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (169, 173) (124, 202) (117, 295) (165, 306) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
		lang.addLine("polygon \"0\" (237, 200) (169, 173) (165, 306) (241, 296) color (2, 200, 200)  depth 1 filled fillColor (40, 40, 50)");
			
		return lang.toString();
	}
		
	public String getAlgorithmName() {
		return ALGORITHM;
	}
	
	public String getDescription() {
		return DESCRIPTION;
	}
	
	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public String getAnimationAuthor() {
		return "Martin Tschirsich, Tjark Vandommele";
	}

	public Locale getContentLocale() {
		return Locale.US;
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public String getName() {
		return "Autopartitioned node storing BSP";
	}
}
