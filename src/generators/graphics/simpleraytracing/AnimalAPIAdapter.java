package generators.graphics.simpleraytracing;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import translator.ResourceLocator;
import algoanim.animalscript.AnimalCircleGenerator;
import algoanim.animalscript.AnimalPointGenerator;
import algoanim.animalscript.AnimalPolygonGenerator;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalSourceCodeGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Circle;
import algoanim.primitives.Point;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.PolygonGenerator;
import algoanim.primitives.generators.PolylineGenerator;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.MsTiming;
import algoanim.util.Node;

public class AnimalAPIAdapter {

	private Language language;
	private PolylineGenerator lineGen;
	private AnimalPointGenerator pointGen;
	private AnimalCircleGenerator circleGen;
	private PolygonGenerator polyGen;
	private TextGenerator textGen;
	private AnimalRectGenerator rectGen;
	private AnimalSourceCodeGenerator codeGen;

	private CircleProperties circleProps = new CircleProperties();
	private PolylineProperties lineProps = new PolylineProperties();
	private PointProperties pointProps = new PointProperties();
	private PolygonProperties polyProps = new PolygonProperties();
	private TextProperties textProps = new TextProperties();
	private RectProperties rectProps = new RectProperties();
	private SourceCodeProperties codeProps = new SourceCodeProperties();
	private SourceCodeProperties descriptionPropsGeneral = new SourceCodeProperties();
	private SourceCodeProperties descriptionPropsCode = new SourceCodeProperties();
	
	private SourceCode code;
	private SourceCode descriptionGeneral;
	private SourceCode descriptionCode;
	private SourceCode firstSlideDescription; // for start slide
	private SourceCode lastSlide;


	
	private HashMap<String, Primitive> primitives = new HashMap<String, Primitive>();
	private HashMap<String, Integer> stepDescriptionMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> lastSlideMap = new HashMap<String, Integer>();

	public HashMap<String, Primitive> getPrimitives() {
		return primitives;
	}

	public AnimalAPIAdapter(String algoName, Language lang) {
		if(lang != null) {
			language = lang;
		} else {
			language = new AnimalScript("asd", "asd2", 1600, 1200);
		}

		language.setStepMode(true);

		lineGen = new AnimalPolylineGenerator(language);
		pointGen = new AnimalPointGenerator(language);
		circleGen = new AnimalCircleGenerator(language);
		polyGen = new AnimalPolygonGenerator(language);
		textGen = new AnimalTextGenerator(language);
		rectGen = new AnimalRectGenerator(language);
		codeGen = new AnimalSourceCodeGenerator(language);
	}
	
	public void setDescriptionGeneralProps(SourceCodeProperties descriptionPropsGeneral){
		this.descriptionPropsGeneral = descriptionPropsGeneral;
		
	}
	
	public SourceCode getDescriptionGeneral() {
		return descriptionGeneral;
	}

	public SourceCode getDescriptionCode() {
		return descriptionCode;
	}

	public void setDescriptionCodeProps(SourceCodeProperties descriptionPropsCode){
		this.descriptionPropsCode = descriptionPropsCode;
		
	}
	
	public void setSourceCodeProps(SourceCodeProperties codeProps){
		this.codeProps = codeProps;
		
	}
	
	
	public void initSourceCode(Point2D upperLeft){
		code = new SourceCode(codeGen, MathUtil.createCoordinatesForPoint2D(upperLeft), "thecode", null, codeProps);
		BufferedReader reader;
		StringBuffer sb = new StringBuffer();
		try {
			reader = getFile("pseudoCode.txt");
			String line = null;
			while ((line = reader.readLine()) != null) {
			    sb.append(line).append("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		code.addMultilineCode(sb.toString(), "theWholeCode", null);
	}
	
	public void initStepDescriptionGenral(Point2D upperLeft){
		descriptionGeneral = new SourceCode(codeGen, MathUtil.createCoordinatesForPoint2D(upperLeft), "descriptiongeneral", null, descriptionPropsGeneral);
		BufferedReader reader;
		try {
			reader = getFile("stepDescriptionGeneral.txt");
			String line = null;
			String key = "";
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				if( line.startsWith("{")){
					key = line.substring(1);
				} else if (line.startsWith("}")){
					descriptionGeneral.addCodeLine("", "none", 0, null);
					stepDescriptionMap.put(key, counter);
					counter = 0 ;
				} else {
					descriptionGeneral.addCodeLine(line, key+counter, 0, null);
					counter++;
				} 
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initDescription(Point2D upperLeft){
		SourceCodeProperties sp = new SourceCodeProperties();
		sp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		firstSlideDescription = new SourceCode(codeGen, MathUtil.createCoordinatesForPoint2D(upperLeft), "descriptiongeneral", null, sp);
		BufferedReader reader;
		try {
			reader = getFile("description.txt");
			String line = null;
			String key = "";
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				if( line.startsWith("{")){
					key = line.substring(1);
				} else if (line.startsWith("}")){
					firstSlideDescription.addCodeLine("", "none", 0, null);
//					stepDescriptionMap.put(key, counter);
					counter = 0 ;
				} else {
					firstSlideDescription.addCodeLine(line, key+counter, 0, null);
					counter++;
				} 
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void toggleCodeHighlighting(String oldLabel, String newLabel, SourceCode sc){
		for (int i = 0; i < stepDescriptionMap.get(oldLabel); i++) {
			sc.unhighlight(oldLabel+i);
		}
		
		for (int i = 0; i < stepDescriptionMap.get(newLabel); i++) {
			sc.highlight(newLabel+i);
		}
	}
	
	public void highlightCode(String label, SourceCode sc){
		for (int i = 0; i < stepDescriptionMap.get(label); i++) {
			sc.highlight(label+i);
		}
	}
	
	public void unhighlightCode(String label, SourceCode sc){
		for (int i = 0; i < stepDescriptionMap.get(label); i++) {
			sc.unhighlight(label+i);
		}
	}
	
	public void initStepDescriptionCode(Point2D upperLeft){
		descriptionCode = new SourceCode(codeGen, MathUtil.createCoordinatesForPoint2D(upperLeft), "descriptiongeneralcode", null, descriptionPropsCode);
		BufferedReader reader;
		try {
			reader = getFile("stepDescriptionCode.txt");
			String line = null;
			String key = "";
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				if( line.startsWith("{")){
					key = line.substring(1);
				} else if (line.startsWith("}")){
					descriptionCode.addCodeLine("", "none", 0, null);
					stepDescriptionMap.put(key, counter);
					counter = 0 ;
				} else {
					descriptionCode.addCodeLine(line, key+counter, 0, null);
					counter++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initFinalSlide(Point2D upperLeft){
		SourceCodeProperties sp = new SourceCodeProperties();
		sp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		lastSlide = new SourceCode(codeGen, MathUtil.createCoordinatesForPoint2D(upperLeft), "last", null, sp);
		BufferedReader reader;
		try {
			reader = getFile("lastslide.txt");
			String line = null;
			String key = "";
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				if( line.startsWith("{")){
					key = line.substring(1);
				} else if (line.startsWith("}")){
					lastSlide.addCodeLine("", "none", 0, null);
					stepDescriptionMap.put(key, counter);
					counter = 0 ;
				} else {
					lastSlide.addCodeLine(line, key+counter, 0, null);
					counter++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawLine(Line2D line, String name, Color color) {
		if(primitives.containsKey(name)){
			hidePrimitive(name);
		}
		Point2D p1 = line.getP1();
		Point2D p2 = line.getP2();
		Node[] nodes = new Node[] { MathUtil.createCoordinatesForPoint2D(p1),
				MathUtil.createCoordinatesForPoint2D(p2) };

		lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

		Polyline polyLine = new Polyline(lineGen, nodes, name, null, lineProps);
		primitives.put(name, polyLine);
		lineProps = new PolylineProperties();

	}


	public void drawPoint(Point2D p, String name, Color color) {
		pointProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		Point pp = new Point(pointGen, MathUtil.createCoordinatesForPoint2D(p), name,
				null, pointProps);
		primitives.put(name, pp);
		pointProps = new PointProperties();
	}

	public void drawCircle(Point2D p, int size, String name, boolean fill,
			Color color) {

		circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, fill);
		circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
		circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		
		Circle c = new Circle(circleGen, MathUtil.createCoordinatesForPoint2D(p), size,
				name, null, circleProps);
		primitives.put(name, c);

		circleProps = new CircleProperties();

	}
	
	public void drawCircle(Point2D p, int size, String name, CircleProperties cp){
		drawCircle(p, size, name, (boolean) cp.get(AnimationPropertiesKeys.FILLED_PROPERTY), (Color) cp.get(AnimationPropertiesKeys.COLOR_PROPERTY));

		}
	
	public void drawSquare(Point2D p, double size, String name, boolean fill,
			Color color) {
		double halfSize = size; /// 2;
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, fill);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		Rect c = new Rect(rectGen, MathUtil.createCoordinatesForPoint2D(MathUtil.createPoint(p.getX() - halfSize , p.getY() - halfSize)), MathUtil.createCoordinatesForPoint2D(MathUtil.createPoint(p.getX() + halfSize , p.getY() + halfSize)), name, null, rectProps );
		primitives.put(name, c);
		rectProps = new RectProperties();

	}
	
	
	public void rotate(String name, int degree, Node around){
		if(primitives.containsKey(name)) {
			Primitive p = primitives.get(name);
			if (p instanceof Rect){
				p.rotate(around, degree, new MsTiming(0), new MsTiming(0));
			} else {
				System.err.println("this rotation is not supported yet");
			}
			
		}
	}
	
	public void drawText(Point2D p, String text, String name){
		Text t = new Text(textGen, MathUtil.createCoordinatesForPoint2D(p), text, name, null, textProps);
		primitives.put(name, t);
		textProps = new TextProperties();
	}
	
	public void drawText(Point2D p, String text, String name, Font font, boolean centered){
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
		textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, centered);
		Text t = new Text(textGen, MathUtil.createCoordinatesForPoint2D(p), text, name, null, textProps);
		primitives.put(name, t);
		textProps = new TextProperties();
	}
	
	public boolean hidePrimitive(String name){
		if(primitives.containsKey(name)){
			primitives.get(name).hide();
		}
		return primitives.containsKey(name);
	}
	
	public boolean changeColorOfPrimitive(String name, Color newColor){
		if(primitives.containsKey(name)){
			primitives.get(name).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, newColor, new MsTiming(0), new MsTiming(0));
			primitives.get(name).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, newColor, new MsTiming(0), new MsTiming(0));
		}
		return primitives.containsKey(name);
	}
	
	
	public boolean changeFillPropertyOfPrimitive(String name, boolean fill){
		return changeFillPropertyOfPrimitive( name,  fill,  0, null);
	}
	
	public boolean changeFillPropertyOfPrimitive(String name, boolean fill, int degree, Node around){
		if(primitives.containsKey(name)){
			Primitive p = primitives.get(name);
			if(p instanceof Circle) {			
				circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, fill);
				circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, ((Circle) p).getProperties().get(AnimationPropertiesKeys.FILL_PROPERTY));
				circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, ((Circle) p).getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY));
				circleProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, ((Circle) p).getProperties().get(AnimationPropertiesKeys.HIDDEN_PROPERTY));
				
				p.hide();
				Circle c = new Circle(circleGen,((Circle) p).getCenter(), ((Circle) p).getRadius(),
						name, null, circleProps);
				primitives.put(name, c);
				circleProps = new CircleProperties();

			} else if (p instanceof Rect) {
				rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, fill);
				rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, ((Rect) p).getProperties().get(AnimationPropertiesKeys.FILL_PROPERTY));
				rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, ((Rect) p).getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY));
				rectProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, ((Rect) p).getProperties().get(AnimationPropertiesKeys.HIDDEN_PROPERTY));
				p.hide();
				Rect c = new Rect(rectGen, ((Rect) p).getUpperLeft(), ((Rect) p).getLowerRight(), name, null, rectProps );
				primitives.put(name, c);
				if(around != null){
					c.rotate(around, degree, new MsTiming(0), new MsTiming(0));
				}
				rectProps = new RectProperties();
			}
		
		}
		return primitives.containsKey(name);
	}

	public void drawPolygon(LinkedList<Line2D> edges, String name, Color color,
			boolean fill) {
		Node[] nodes = createNodesFromLines(edges);
		
		drawPolygon(nodes, name, color,fill);

	}

	public void drawPolygon(Point2D[] points, String name, Color color,
			boolean fill) throws NotEnoughNodesException {
		drawPolygon(createNodesFromPoints(points),name, color, fill);
	}
	
	public void drawShape(MyShape shape, String name){
		drawPolygon(shape.getLines(), name, shape.getColor(), shape.isFill());
	}

	public void drawPolygon(Node[] points, String name, Color color,
			boolean fill)  {

		polyProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, fill);
		polyProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);

		polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

		Polygon p;
		try {
			p = new Polygon(polyGen, points, name, null, polyProps);
			primitives.put(name, p);
		} catch (NotEnoughNodesException e) {
			System.err.println("Could not draw Polygon:"+name);
			e.printStackTrace();
		}
		
		
		
		polyProps = new PolygonProperties();

	}

	public Node[] createNodesFromPoints(Point2D[] points) {
		Node[] res = new Node[points.length];

		for (int i = 0; i < res.length; i++) {
			res[i] = MathUtil.createCoordinatesForPoint2D(points[i]);
		}

		return res;
	}

	public Node[] createNodesFromPoints(LinkedList<Point2D> points) {
		Node[] res = new Node[points.size()];

		for (int i = 0; i < res.length; i++) {
			res[i] = MathUtil.createCoordinatesForPoint2D(points.get(i));
		}

		return res;
	}

	public Node[] createNodesFromLines(LinkedList<Line2D> edges) {
		Node[] nodes = new Node[edges.size() * 2 - 1];
		LinkedList<Point2D> l1 = new LinkedList<Point2D>();

		for (Line2D edge : edges) {
			l1.add(edge.getP1());
			l1.add(edge.getP2());
		}
		nodes = createNodesFromPoints(l1);

		return nodes;
	}
	
	public void drawRectangle(Point2D p, double length, double height, String name, boolean fill,
			Color color) {
		double l2 = length; // / 2;
		double h2 = height; // / 2;
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, fill);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		Rect c = new Rect(rectGen, MathUtil.createCoordinatesForPoint2D(MathUtil.createPoint(p.getX() - l2 , p.getY() - h2)), MathUtil.createCoordinatesForPoint2D(MathUtil.createPoint(p.getX() + l2 , p.getY() + h2)), name, null, rectProps );
		primitives.put(name, c);
		rectProps = new RectProperties();
	}

	public String getASUString() {
		return language.toString();
	}

	public void nextStep() {
		language.nextStep();
	}
	
	public void nextStep(String s) {
		language.nextStep(s);
	}
	
	public SourceCode getCode(){
		return code;
	}
	
	public SourceCode getSc() {
		return firstSlideDescription;
	}
	
	public void hideAll(){
		language.hideAllPrimitives();
	}
	
	public SourceCode getLastSlide() {
		return lastSlide;
	}

	private static BufferedReader getFile(final String filename) {
		return new BufferedReader(new InputStreamReader(
				ResourceLocator.getResourceLocator().getResourceStream(
						"generators/graphics/simpleraytracing/" + filename)));
	}
}
