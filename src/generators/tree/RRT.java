/*
 * RRT.java
 * Jasper Suess, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Set;

import translator.Translator;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import translator.Translator;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class RRT implements ValidatingGenerator {
	private Language lang;
	private double gDelta;
	private int[] goalPoint;
	private int[] startingNode;
	private double edgeLength;
	private int k;
	private SourceCode sc;
	private SourceCode sdc;
	private Tree tree;
	private Node base;
	private Node textBase;
	private int xBottomLeft;
	private int yBottomLeft;
	private int xTopRight;
	private int yTopRight;
	private int[] bottomLeft;
	private int[] topRight;
	private Color obstacleColor;
	private Color highlightColor1;
	private Color highlightColor2;
	private Color goalColorDelta;
    private Color goalColorNode;
	private Locale language;
    private Translator translator;
    

    
    public RRT(Locale language){
		this.language = language;
		translator = new Translator("resources/RRT" , language ) ;
	}

	public static void main(String[] args) {
		Generator generator = new RRT(Locale.GERMANY); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

	public void init(){
		lang = new AnimalScript("Rapidly-Exploring Random Tree (RRT)", "Jasper Suess", 1920, 1080);
		lang.setStepMode(true);
	}

	public void runRRT() {

		base = new Coordinates(500, 55);
		textBase = new Coordinates(50, 285);

		xBottomLeft = bottomLeft[0];
		yBottomLeft = bottomLeft[1];
		xTopRight = topRight[0];
		yTopRight = topRight[1];

		addDescriptionStart();
		lang.nextStep();
		lang.hideAllPrimitives();


		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1000);
		CircleProperties cp = new CircleProperties();
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 6000);

		Circle goalDelta = lang.newCircle(new Offset(goalPoint[0] * 5, (100 - goalPoint[1]) * 5, base, AnimalScript.DIRECTION_NW), (int)(gDelta) * 5, "goalDelta", null, cp);
		goalDelta.changeColor(AnimalScript.COLORCHANGE_COLOR, goalColorDelta, null, null);
		goalDelta.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColorDelta, null, null);

		Circle goal = lang.newCircle(new Offset(goalPoint[0] * 5, (100 - goalPoint[1]) * 5, base, AnimalScript.DIRECTION_NW), 5, "goal", null, cp);
		goal.changeColor(AnimalScript.COLORCHANGE_COLOR, goalColorNode, null, null);
		goal.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColorNode, null, null);
		cp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		

		addSourceCode();
		addGrid();

		Circle start = lang.newCircle(new Offset(startingNode[0] * 5, (100 - startingNode[1]) * 5, base, AnimalScript.DIRECTION_NW), 5, "start", null, cp);
		TreeNode root = new TreeNode(startingNode[0], 100 - startingNode[1], null, start);
		tree = new Tree(root);
		tree.addNode(root);

		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 16));
		Text kt = lang.newText(new Coordinates(50, 250), "i = 0", "kt", null, tp);
		kt.hide();


		lang.nextStep(translator.translateMessage("start"));
		lang.nextStep(translator.translateMessage("iteration") + "0");


		for(int i = 1; i <= k; i++) {

			double distance = 0;
			List<TreeNode> path = new LinkedList<TreeNode>();
			//Run algorithm itself first then animate
			int randX = (int) (Math.random() * 100);
			int randY = (int) (Math.random() * 100);
			while(badRand(randX, randY)) {
				randX = (int) (Math.random() * 100);
				randY = (int) (Math.random() * 100);
			}
			TreeNode nearest = tree.findNearestNode(new TreeNode(randX, randY, null, null));
			TreeNode qNew = getqNew(edgeLength, nearest, randX, randY);
			tree.addNode(qNew);
			boolean valid = isValid(qNew, nearest);

			if(!valid)
				tree.delete(qNew);
			else {
				distance = Math.sqrt(Math.pow(qNew.x - goalPoint[0], 2) + Math.pow(qNew.y - (100 - goalPoint[1]), 2));
				distance = (int)(distance*100)/100.0;
				if(distance < gDelta) {
					path = tree.getPath(qNew);
				}
			}

			sc.unhighlight(6);
			sc.unhighlight(8);

			//for i = 1 to k
			sc.highlight(0);
			kt.hide(new MsTiming(400));
			kt = lang.newText(new Offset(0, 24, textBase, AnimalScript.DIRECTION_NW), "i = " + i, "kt", null, tp);
			kt.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);
			kt.hide();
			kt.show(new MsTiming(400));



			lang.nextStep(translator.translateMessage("rand"));



			//qRand = RandomConfig()
			kt.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
			sc.toggleHighlight(0, 1);

			//Display the configuration to the user by text and visually
			Text rand = lang.newText(new Offset(0, 48, textBase, AnimalScript.DIRECTION_NW), "qRand = (" + randX + ", " + (100 - randY) + ")",  "rand", null, tp);
			rand.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);
			pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
			Polyline randC1 = lang.newPolyline(new Node[]{new Offset((randX - 2)*5, (randY - 2)*5, base, AnimalScript.DIRECTION_NW), new Offset((randX + 2)*5, (randY + 2)*5, base, AnimalScript.DIRECTION_NW)}, "randC1", null, pp);
			Polyline randC2 = lang.newPolyline(new Node[]{new Offset((randX + 2)*5, (randY - 2)*5, base, AnimalScript.DIRECTION_NW), new Offset((randX - 2)*5, (randY + 2)*5, base, AnimalScript.DIRECTION_NW)}, "randC2", null, pp);
			pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);

			randC1.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);
			randC2.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);
			blink(randC1, randC2);



			lang.nextStep(translator.translateMessage("near"));



			//qNear = NearestNode(qRand) 
			sc.highlight(2, 0, true);

			//Display the nearest node to the user by text and visually
			Text nearq = lang.newText(new Offset(0, 72, textBase, AnimalScript.DIRECTION_NW), "qNear = (" + Math.round(nearest.x) + ", " + (100 - Math.round(nearest.y)) + ")",  "nearq", null, tp);
			nearq.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor2, null, null);
			nearest.circle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, highlightColor2, null, null);
			blink(nearest.circle);



			lang.nextStep(translator.translateMessage("construct"));

			//Reset randomConfig() stuff to black color
			randC1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
			randC2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
			sc.toggleHighlight(1, 3);
			rand.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);



			//qNew = NewNode(qNear, qRand, edgeLength)
			Circle qNewCircle = lang.newCircle(new Offset((int)(Math.round(qNew.x * 5)), (int)(Math.round(qNew.y * 5)), base, AnimalScript.DIRECTION_NW), 5, "circle", null, cp);
			//Add the primitive to the node to access it later
			qNew.setCircle(qNewCircle);
			//Display the new node to the user by text and visually
			//qNewCircle.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);
			qNewCircle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, highlightColor1, null, null);
			Text newq = lang.newText(new Offset(0, 96, textBase, AnimalScript.DIRECTION_NW), "qNew = (" + Math.round(qNew.x) + ", " + (100 - Math.round(qNew.y)) + ")",  "nearq", null, tp);
			newq.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);
			blink(qNewCircle);


			lang.nextStep(translator.translateMessage("addEdge"));



			//Reset qNear and qNew, text + nodes, to black color
			sc.unhighlight(2, 0, true);
			sc.toggleHighlight(3, 4);
			newq.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
			nearq.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
			qNewCircle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.BLACK, null, null);
			qNewCircle.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
			nearest.circle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.BLACK, null, null);
			nearest.circle.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);

			//addEdge()
			Polyline edge = lang.newPolyline(new Node[]{qNewCircle.getCenter(), nearest.circle.getCenter()}, "randC1", null, pp);
			qNew.setEdge(edge);
			edge.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);
			blink(edge);



			lang.nextStep(translator.translateMessage("valid"));



			//Reset old stuff
			sc.toggleHighlight(4, 5);
			edge.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);

			qNewCircle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, highlightColor1, null, null);
			edge.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);


			//Delete node if inside obstacle or out of bounds
			if(!valid){
				lang.nextStep(translator.translateMessage("valid"));
				sc.toggleHighlight(5, 6);
				blink(qNewCircle, edge);

				qNewCircle.hide(new MsTiming(2000));
				edge.hide(new MsTiming(2000));

				lang.nextStep(translator.translateMessage("iteration") + i);

			}else {
				lang.nextStep(translator.translateMessage("measure"));
				edge.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
				sc.toggleHighlight(5,7);
				lang.nextStep();
				sc.toggleHighlight(7, 8);
				//Highlight goal and qNewCircle
				goal.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
				goal.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, highlightColor1, null, null);
				qNewCircle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, highlightColor1, null, null);
				goalDelta.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);
				blink(qNewCircle, goal);

				//Visualize distance
				String op;
				if(distance < gDelta)
					op = " < ";
				else
					op = " > ";

				Text distt = lang.newText(new Offset(0, 120, textBase, AnimalScript.DIRECTION_NW), "distance = " + distance + op + gDelta + " = gDelta",  "dist", null, tp);
				distt.changeColor(AnimalScript.COLORCHANGE_COLOR, highlightColor1, null, null);

				lang.nextStep(translator.translateMessage("iteration")+ i);

				qNewCircle.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
				qNewCircle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.BLACK, null, null);
				goalDelta.changeColor(AnimalScript.COLORCHANGE_COLOR, goalColorDelta, null, null);

				if(distance < gDelta) {
					sc.toggleHighlight(8, 9);

					for(TreeNode pathElem : path) {
						pathElem.circle.changeColor(AnimalScript.COLORCHANGE_COLOR, goalColorNode, null, null);
						pathElem.circle.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColorNode, null, null);
						if(pathElem != tree.root)
							pathElem.edgeToParent.changeColor(AnimalScript.COLORCHANGE_COLOR, goalColorNode, null, null);
					}
					goal.changeColor(AnimalScript.COLORCHANGE_COLOR, goalColorNode, null, null);
					goal.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColorNode, null, null);
					lang.nextStep();


					sc.toggleHighlight(9, 10);
					break;
				}

				distt.hide();

			}

			randC1.hide();
			randC2.hide();
			rand.hide();
			nearq.hide();
			newq.hide();
			goal.changeColor(AnimalScript.COLORCHANGE_COLOR, goalColorNode, null, null);
			goal.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColorNode, null, null);
			sc.unhighlight(5);

		}

		lang.nextStep();
		kt.hide();
		lang.hideAllPrimitives();
		addDescriptionEnd();

	}

	public boolean badRand(int x, int y) {
		for(TreeNode t : tree.nodes) {
			double dist = Math.sqrt(Math.pow(t.x - x, 2) + Math.pow(t.y - y, 2));
			if(dist < 3)
				return true;
		}
		return false;
	}

	public boolean pointInObstacle(double x, double y) {
		return (x > xBottomLeft && x < xTopRight && y > yBottomLeft && y < yTopRight);     	
	}

	public boolean isValid(TreeNode qNew, TreeNode nearest) {
		double x1 = qNew.x;
		double x2 = nearest.x;
		double y1 = 100 - qNew.y;
		double y2 = 100 - nearest.y;

		//Standard boundaries
		if(x1 > 100 || x1 < 0 || y1 > 100 || y1 < 0)
			return false;


		//Node inside Obstacle
		if(x1 > xBottomLeft && x1 < xTopRight && y1 > yBottomLeft && y1 < yTopRight) {
			return false;
		}

		//Edge crossing obstacle
		double xDelta = x1 - x2;
		double yDelta = y1 - y2;

		for(int i = 0; i < 20; i++) {
			if(pointInObstacle(x2 + (xDelta*i) / 20.0, y2 + (yDelta*i) / 20.0)){
				return false;
			}
		}

		return true;
	}

	public void blink(Primitive a) {
		a.hide(new MsTiming(400));
		a.show(new MsTiming(800));
		a.hide(new MsTiming(1200));
		a.show(new MsTiming(1600));
	}

	public void blink(Primitive a, Primitive b) {
		a.hide(new MsTiming(400));
		b.hide(new MsTiming(400));
		a.show(new MsTiming(800));
		b.show(new MsTiming(800));
		a.hide(new MsTiming(1200));
		b.hide(new MsTiming(1200));
		a.show(new MsTiming(1600));
		b.show(new MsTiming(1600));

	}

	public TreeNode getqNew(double qDelta, TreeNode nearest, int x, int y ) {

		double c = qDelta;
		double b = 0;
		if(x != nearest.x) {
			double slope = ((double) (y) - nearest.y) / ((double) (x) - nearest.x);
			b = c / (Math.sqrt(slope * slope + 1));
		}
		double a = Math.sqrt(c * c - b * b);

		if(y < nearest.y)
			a = -a;
		if(x < nearest.x)
			b = -b;

		return new TreeNode(nearest.x + b, nearest.y + a, nearest);
	}

	public void addSourceCode() {
		TextProperties tpHeader = new TextProperties();
		tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(25, 10), "Rapidly-Exploring Random Tree (RRT)", "header", null, tpHeader);

		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED,
				Font.PLAIN, 16));
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor1);
		scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, highlightColor2);

		sc = lang.newSourceCode(new Coordinates(40, 50), "sourceCode", null, scp);
		int i = 1;
			
		sc.addCodeLine(translator.translateMessage("sc" + i) + " " + k, "sdc" + i++, 0, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 2, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 2, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 3, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 3, null);

	}

	public void addDescriptionStart() {
		TextProperties tpHeader = new TextProperties();
		tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(25, 10), "Rapidly-Exploring Random Tree (RRT)", "header", null, tpHeader);

		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,
				Font.ROMAN_BASELINE, 16));

		sdc = lang.newSourceCode(new Coordinates(40, 40), "sourceCode", null, scp);
		for(int i = 1; i < 16; i++) {
			sdc.addCodeLine(translator.translateMessage("ds" + i), "sdc" + i, 0, null);
		}
	}

	public void addDescriptionEnd() {
		TextProperties tpHeader = new TextProperties();
		tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(25, 10), "Rapidly-Exploring Random Tree (RRT)", "header", null, tpHeader);

		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,
				Font.ROMAN_BASELINE, 16));

		sdc = lang.newSourceCode(new Coordinates(40, 40), "sourceCode", null, scp);
		for(int i = 1; i < 24; i++) {
			sdc.addCodeLine(translator.translateMessage("de" + i), "sdc" + i, 0, null);
		}
	}

	public void addGrid() {
		SquareProperties sp = new SquareProperties();		
		Square mainGrid = lang.newSquare(base, 500, "mainGrid", null, sp);
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, obstacleColor);
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1000);

		Node[] test = {base, new Offset(250, -6, base, AnimalScript.DIRECTION_NW)};
		PolylineProperties pp = new PolylineProperties();
		Polyline t50 = lang.newPolyline(new Node[]{new Offset(250, 7, base, AnimalScript.DIRECTION_NW), new Offset(250, -7, base, AnimalScript.DIRECTION_NW)}, "t50", null, pp);
		Polyline b50 = lang.newPolyline(new Node[]{new Offset(250, 507, base, AnimalScript.DIRECTION_NW), new Offset(250, 493, base, AnimalScript.DIRECTION_NW)}, "b50", null, pp);
		Polyline l50 = lang.newPolyline(new Node[]{new Offset(-7, 250, base, AnimalScript.DIRECTION_NW), new Offset(7, 250, base, AnimalScript.DIRECTION_NW)}, "l50", null, pp);
		Polyline r50 = lang.newPolyline(new Node[]{new Offset(493, 250, base, AnimalScript.DIRECTION_NW), new Offset(507, 250, base, AnimalScript.DIRECTION_NW)}, "r50", null, pp);

		Polyline t25 = lang.newPolyline(new Node[]{new Offset(125, 4, base, AnimalScript.DIRECTION_NW), new Offset(125, -4, base, AnimalScript.DIRECTION_NW)}, "t25", null, pp);
		Polyline b25 = lang.newPolyline(new Node[]{new Offset(125, 504, base, AnimalScript.DIRECTION_NW), new Offset(125, 496, base, AnimalScript.DIRECTION_NW)}, "b25", null, pp);
		Polyline l25 = lang.newPolyline(new Node[]{new Offset(-4, 375, base, AnimalScript.DIRECTION_NW), new Offset(4, 375, base, AnimalScript.DIRECTION_NW)}, "l25", null, pp);
		Polyline r25 = lang.newPolyline(new Node[]{new Offset(496, 375, base, AnimalScript.DIRECTION_NW), new Offset(504, 375, base, AnimalScript.DIRECTION_NW)}, "r25", null, pp);

		Polyline t75 = lang.newPolyline(new Node[]{new Offset(375, 4, base, AnimalScript.DIRECTION_NW), new Offset(375, -4, base, AnimalScript.DIRECTION_NW)}, "t75", null, pp);
		Polyline b75 = lang.newPolyline(new Node[]{new Offset(375, 504, base, AnimalScript.DIRECTION_NW), new Offset(375, 496, base, AnimalScript.DIRECTION_NW)}, "b75", null, pp);
		Polyline l75 = lang.newPolyline(new Node[]{new Offset(-4, 125, base, AnimalScript.DIRECTION_NW), new Offset(4, 125, base, AnimalScript.DIRECTION_NW)}, "l75", null, pp);
		Polyline r75 = lang.newPolyline(new Node[]{new Offset(496, 125, base, AnimalScript.DIRECTION_NW), new Offset(504, 125, base, AnimalScript.DIRECTION_NW)}, "r75", null, pp);

		Polyline lb0 = lang.newPolyline(new Node[]{new Offset(-5, 504, base, AnimalScript.DIRECTION_NW), new Offset(5, 494, base, AnimalScript.DIRECTION_NW)}, "lb0", null, pp);

		PolylineProperties ppGridLines = new PolylineProperties();
		ppGridLines.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		ppGridLines.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5000);
		Polyline v25 = lang.newPolyline(new Node[]{new Offset(125, 4, base, AnimalScript.DIRECTION_NW), new Offset(125, 496, base, AnimalScript.DIRECTION_NW)}, "v25", null, ppGridLines);
		Polyline v50 = lang.newPolyline(new Node[]{new Offset(250, 4, base, AnimalScript.DIRECTION_NW), new Offset(250, 496, base, AnimalScript.DIRECTION_NW)}, "v50", null, ppGridLines);
		Polyline v75 = lang.newPolyline(new Node[]{new Offset(375, 4, base, AnimalScript.DIRECTION_NW), new Offset(375, 496, base, AnimalScript.DIRECTION_NW)}, "v75", null, ppGridLines);
		Polyline h25 = lang.newPolyline(new Node[]{new Offset(4, 375, base, AnimalScript.DIRECTION_NW), new Offset(496, 375, base, AnimalScript.DIRECTION_NW)}, "h25", null, ppGridLines);
		Polyline h50 = lang.newPolyline(new Node[]{new Offset(4, 250, base, AnimalScript.DIRECTION_NW), new Offset(496, 250, base, AnimalScript.DIRECTION_NW)}, "h50", null, ppGridLines);
		Polyline h75 = lang.newPolyline(new Node[]{new Offset(4, 125, base, AnimalScript.DIRECTION_NW), new Offset(496, 125, base, AnimalScript.DIRECTION_NW)}, "h75", null, ppGridLines);



		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 12));
		Text b50t = lang.newText(new Offset(244, 510,  base,  AnimalScript.DIRECTION_NW), "50", "b50t", null, tp);
		Text b25t = lang.newText(new Offset(119, 510,  base,  AnimalScript.DIRECTION_NW), "25", "b25t", null, tp);
		Text b75t = lang.newText(new Offset(369, 510,  base,  AnimalScript.DIRECTION_NW), "75", "b75t", null, tp);
		Text l25t = lang.newText(new Offset(-23, 367,  base,  AnimalScript.DIRECTION_NW), "25", "l25t", null, tp);
		Text l50t = lang.newText(new Offset(-23, 242,  base,  AnimalScript.DIRECTION_NW), "50", "l50t", null, tp);
		Text l75t = lang.newText(new Offset(-23, 117,  base,  AnimalScript.DIRECTION_NW), "75", "l75t", null, tp);
		Text lb0t = lang.newText(new Offset(-14, 510,  base,  AnimalScript.DIRECTION_NW), "0", "lb0", null, tp);

		Rect obst = lang.newRect(new Offset(xBottomLeft*5, 500 - yTopRight*5, base, AnimalScript.DIRECTION_NW), new Offset(xTopRight*5, 500 - yBottomLeft*5, base, AnimalScript.DIRECTION_NW), "obst", null, rp);



	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		
		gDelta = (double)primitives.get("gDelta");
		goalPoint = (int[])primitives.get("goalPoint");
		startingNode = (int[])primitives.get("startingNode");
		edgeLength = (double)primitives.get("edgeLength");
		k = (Integer)primitives.get("k");    
		
        obstacleColor = (Color) ((RectProperties)props.getPropertiesByName("obstacleColor")).get(AnimationPropertiesKeys.FILL_PROPERTY);
        Set<String> a = ((SourceCodeProperties)props.getPropertiesByName("highlightColor1")).getAllPropertyNames();
        highlightColor1 = (Color) ((SourceCodeProperties)props.getPropertiesByName("highlightColor1")).get("highlightColor");
        highlightColor2 = (Color) ((SourceCodeProperties)props.getPropertiesByName("highlightColor2")).get("highlightColor");
        goalColorDelta = (Color) ((CircleProperties)props.getPropertiesByName("goalColor")).get(AnimationPropertiesKeys.FILL_PROPERTY);
        goalColorNode = (Color) ((CircleProperties)props.getPropertiesByName("goalColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		runRRT();
		
		return lang.toString();
	}

	public String getName() {
		return "Rapidly-Exploring Random Tree (RRT)";
	}

	public String getAlgorithmName() {
		return "Rapidly-Exploring Random Tree (RRT)";
	}

	public String getAnimationAuthor() {
		return "Jasper Suess";
	}

	public String getDescription(){
		
		String ret = "";

		for(int i = 1; i < 16; i++) {
			ret = ret + translator.translateMessage("ds" + i) + "\n";
		}
		return ret;
	}

	public String getCodeExample(){
		
		String ret = "";
		ret = ret + translator.translateMessage("sc1") + " " + k + "\n";

		for(int i = 2; i < 12; i++) {
			ret = ret + translator.translateMessage("sc" + i) + "\n";
		}
		return ret;
	}

	public String getFileExtension(){
		return "asu";
	}

	public Locale getContentLocale() {
		return language;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}


	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		gDelta = (double)primitives.get("gDelta");
		goalPoint = (int[])primitives.get("goalPoint");
		startingNode = (int[])primitives.get("startingNode");
		edgeLength = (double)primitives.get("edgeLength");
		bottomLeft = (int[])primitives.get("obstacleBottomLeft");
		topRight = (int[])primitives.get("obstacleTopRight");
		k = (Integer)primitives.get("k");   
		
		//plausible parameters
		if(gDelta < 1 || gDelta > 70 || k < 1 || edgeLength < 2 || edgeLength > 50)
			return false;
		
		//goalPoint inside grid
		if(goalPoint[0] < 0 || goalPoint[0] > 100 || goalPoint[1] < 0 || goalPoint[1] > 100)
			return false;
		
		//startingPoint inside grid
		if(startingNode[0] < 0 || startingNode[0] > 100 || startingNode[1] < 0 || startingNode[1] > 100)
			return false;
		
		//startingNode != goalPoint
		if(startingNode[0] == goalPoint[0] && startingNode[1] == goalPoint[1])
			return false;
		
		//startingNode inside obstacle
		if(startingNode[0] > bottomLeft[0] && startingNode[0] < topRight[0] && startingNode[1] > bottomLeft[1] && startingNode[1] < topRight[1])   	
			return false;
			
		//obstacles corners not plausible
		if(xBottomLeft > xTopRight || yBottomLeft > yTopRight)
			return false;
		
		return true;
	}

}