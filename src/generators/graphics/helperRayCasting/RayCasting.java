package generators.graphics.helperRayCasting;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * @author Lukas Dietrich <lukasalexander.dietrich@stud.tu-darmstadt.de>
 * @version 1.0 20180602
 */
public class RayCasting {

	private Translator strings;
	private Language lang;

	private double questionProbability;
	private List<Primitive> titlePrimitives;

	private TextProperties textProps;
	private SourceCodeProperties slideProps;
	private SourceCodeProperties codeProps;

	private PolygonProperties polygonProps;
	private PolylineProperties edgeProps, intersectionProps, rayProps;

	private int coordWidth, coordHeight;
	private int coordScaling;

	public static final String SOURCECODE = ""
		+ "// test if a point is within a polygon\n"                         //  0 
		+ "func pointInPolygon(point: Point, vertices: [Point]) -> bool {\n" //  1 *
		+ "  let ray = castRay(point)\n"                                     //  2 +
		+ "  let intersections = 0\n"                                        //  3
		+ "\n"                                                               //  4
		+ "  for (i = 0 .. vertices.length) {\n"                             //  5
		+ "    let edge = new Line(\n"                                       //  6
		+ "      vertices[i],\n"                                             //  7
		+ "      vertices[modulo(i+1, vertices.length)]\n"                   //  8
		+ "    )\n"                                                          //  9
		+ "\n"                                                               // 10
		+ "    if (rayIntersectsEdge(ray, edge)) {\n"                        // 11 +
		+ "      intersections += 1\n"                                       // 12
		+ "    }\n"                                                          // 13
		+ "  }\n"                                                            // 14
		+ "\n"                                                               // 15
		+ "  return isOdd(intersections)\n"                                  // 16
		+ "}\n"                                                              // 17
		+ "\n"                                                               // 18
		+ "\n"                                                               // 19
		+ "// cast a ray (= a line) from the point to the very right\n"      // 20
		+ "func castRay(p: Point) -> Line\n"                                 // 21 *
		+ "\n"                                                               // 22
		+ "// test if the ray intersects with the edge\n"                    // 23
		+ "func rayIntersectsEdge(ray: Line, edge: Line) -> bool\n";         // 24 *

	public RayCasting(
		Language lang, Translator strings,
		double questionProbability,
		int fontSize,
		SourceCodeProperties codeProps, SourceCodeProperties textProps,
		PolygonProperties polygonProps, PolylineProperties rayProps,
		PolylineProperties edgeProps, PolylineProperties intersectionProps,
		int coordWidth, int coordHeight, int coordScaling
	) {
		this.lang = lang;
		this.strings = strings;
		this.questionProbability = questionProbability;

		this.polygonProps = polygonProps;
		this.edgeProps = edgeProps;
		this.intersectionProps = intersectionProps;
		this.rayProps = rayProps;

		this.textProps = new TextProperties();
		this.textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
			(Color) textProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		this.textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
			((Font) textProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
				.deriveFont((float) fontSize));

		this.codeProps = codeProps;
		this.codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
			((Font) codeProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
				.deriveFont((float) fontSize));

		this.slideProps = textProps;
		this.slideProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
			((Font) textProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
				.deriveFont((float) fontSize));

		this.coordWidth = coordWidth;
		this.coordHeight = coordHeight;
		this.coordScaling = coordScaling;

		lang.setStepMode(true);
	}

	public void run(List<Point2D> vertices, Point2D point) {
		{
			TextProperties titleProps = new TextProperties();
			titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF, Font.BOLD, 28));

			Text title = lang.newText(
				new Coordinates(20, 20),
				strings.translateMessage("title"),
				nextId(),
				null,
				titleProps);

			RectProperties titleBoxProps = new RectProperties();
			titleBoxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			titleBoxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			titleBoxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 200, 200));

			Rect titleBox = lang.newRect(
				new Offset(-5, -5, title, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, title, AnimalScript.DIRECTION_SE),
				nextId(), null, titleBoxProps);

			titlePrimitives = Arrays.asList(title, titleBox);
			lang.nextStep(strings.translateMessage("stepLabelIntro"));
		}

		this.runIntro(point, vertices);
		boolean inside = this.runAlgorithm(point, vertices);
		this.runOutro(point, vertices, inside);
	}

	/**
	 * Show the initial intro slide
	 */
	private void runIntro(Point2D point, List<Point2D> vertices) {
		SourceCode intro = lang.newSourceCode(
			new Offset(0, 20, titlePrimitives.get(0), "SW"),
			nextId(),
			null,
			slideProps);

		for (int i = 1; i <= 3; i++) {
			String s = strings.translateMessage("intro" + i);
			intro.addMultilineCode(s, null, null);
			intro.addCodeLine("", null, 1, null);
			lang.nextStep();
		}

		intro.addCodeLine("- " + strings.translateMessage("case.even"), null, 2, null);
		lang.nextStep();

		intro.addCodeLine("- " + strings.translateMessage("case.odd"), null, 2, null);
		lang.nextStep();

		intro.addCodeLine("", null, 1, null);
		intro.addMultilineCode(strings.translateMessage("intro4"), null, null);
		lang.nextStep();

		intro.addCodeLine("", null, 1, null);
		intro.addMultilineCode(strings.translateMessage("task",
			formatPoint(point), formatPoints(vertices)
		), null, null);
		lang.nextStep();

		lang.hideAllPrimitivesExcept(titlePrimitives);
	}

	/**
	 * Show the main part of the animation: the actual algorithm
	 */
	private boolean runAlgorithm(Point2D point, List<Point2D> vertices) {
		SourceCode code = lang.newSourceCode(
			new Offset(0, 20, titlePrimitives.get(0), "SW"),
			nextId(),
			null,
			codeProps);

		for (String line : SOURCECODE.split("\n")) {
			int indentation = 0;
			while (line.startsWith("  ")) {
				indentation++;
				line = line.substring(2);
			}

			code.addCodeLine(line, null, indentation, null);
		}

		CoordinateSystem cs = new CoordinateSystem(code, "NE", 100, 0, coordWidth, coordHeight, coordScaling);
		cs.drawPolygon(vertices);
		cs.drawPoint(point, "point");

		TextStack coords = new TextStack(cs.yAxis, "NW", coordWidth * coordScaling + 50, 0);
		TextStack status = new TextStack(cs.yAxis, "SW", 0, 50);

		List<Text> coordTexts = new ArrayList<>();

		highlight(coords.push(String.format("point = %s", formatPoint(point)), 1),
			(Color) rayProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
			
		for (int i = 0; i < vertices.size(); i++) {
			coordTexts.add(coords.push(String.format("v%d = %s",
				i, formatPoint(vertices.get(i))), 1));
		}

		lang.nextStep(strings.translateMessage("stepLabelInit"));

		code.highlight(1);
		lang.nextStep();

		code.toggleHighlight(1, 2);
		code.highlight(21);
		cs.drawRay(point);
		lang.nextStep();

		code.toggleHighlight(2, 3);
		code.unhighlight(21);
		int intersections = 0;
		Text textIntersections = status.push(formatLetIntersections(intersections), 1);
		highlight(textIntersections, (Color) intersectionProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		lang.nextStep();

		code.toggleHighlight(3, 5);
		lang.nextStep();

		QuestionGroupModel predictEdgeGroup = new QuestionGroupModel(nextId(), 2);
		QuestionGroupModel predictIntersectionGroup = new QuestionGroupModel(nextId(), 2);

		showPredictEdgeQuestion(predictEdgeGroup, 0, vertices.size());

		for (int i = 0; i < vertices.size(); i++) {
			int j = (i+1) % vertices.size();

			Color edgeHighlight = (Color) edgeProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
			highlight(status.push(String.format("let edge = new Line(v%d, v%d)", i, j), 2), 
				edgeHighlight);
			highlight(coordTexts.get(i), edgeHighlight);
			highlight(coordTexts.get(j), edgeHighlight);

			Line2D edge = new Line2D.Double(vertices.get(i), vertices.get(j));
			Polyline edgeLine = cs.drawEdge(edge);

			code.toggleHighlight(5, 6);
			for (int k = 7; k <= 9; k++)
				code.highlight(k);
			lang.nextStep(strings.translateMessage("stepLabelIteration", String.valueOf(i+1)));

			code.toggleHighlight(6, 11);
			code.highlight(24);
			for (int k = 7; k <= 9; k++)
				code.unhighlight(k);

			boolean rayIntersects = rayIntersectsEdge(point, edge);

			if (Math.random() <= questionProbability) {
				TrueFalseQuestionModel q = new TrueFalseQuestionModel(nextId());
					q.setPointsPossible(5);
					q.setPrompt(strings.translateMessage("questionPredictIntersection.prompt"));
					q.setCorrectAnswer(rayIntersects);
					q.setFeedbackForAnswer(rayIntersects, strings.translateMessage("questionPredictIntersection.feedback.correct"));
					q.setFeedbackForAnswer(!rayIntersects, strings.translateMessage("questionPredictIntersection.feedback.wrong"));

				q.setGroupID(predictIntersectionGroup.getID());
				lang.addTFQuestion(q);
			}

			lang.nextStep();

			code.unhighlight(24);

			if (rayIntersects) {
				code.toggleHighlight(11, 12);
				intersections++;
				textIntersections.setText(formatLetIntersections(intersections), null, null);
				highlight(edgeLine, (Color) intersectionProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
				lang.nextStep();

				code.toggleHighlight(12, 5);
			} else {
				edgeLine.hide();
				code.toggleHighlight(11, 5);
			}

			unhighlight(coordTexts.get(i));
			unhighlight(coordTexts.get(j));
			status.pop();

			showPredictEdgeQuestion(predictEdgeGroup, i+1, vertices.size());
			lang.nextStep();
		}

		code.toggleHighlight(5, 16);
		status.push(String.format("return isOdd(%d)", intersections), 2);
		lang.nextStep();

		code.unhighlight(16);
		status.push(strings.translateMessage("result." + ((intersections % 2 == 0) ? "even" : "odd"), String.valueOf(intersections)), 3);
		lang.nextStep(strings.translateMessage("stepLabelReturn"));

		List<Primitive> keep = new ArrayList<>();
		keep.addAll(titlePrimitives);
		keep.addAll(cs.getAllPrimitives());

		lang.hideAllPrimitivesExcept(keep);

		return intersections % 2 != 0;
	}

	private boolean rayIntersectsEdge(Point2D point, Line2D edge) {
		if (point.getY() == edge.getP1().getY()) {
			return edge.getP2().getY() < point.getY();
		}

		if (point.getY() == edge.getP2().getY()) {
			return edge.getP1().getY() < point.getY();
		}

		Line2D ray = new Line2D.Double(point, new Point2D.Double(coordWidth, point.getY()));
		return ray.intersectsLine(edge);
	}

	private String formatLetIntersections(int count) {
		return String.format("let intersections = %d", count);
	}

	private void showPredictEdgeQuestion(QuestionGroupModel group, int i, int size) {
		if (i >= size) {
			return;
		}

		if (Math.random() > questionProbability) {
			return;
		}

		MultipleSelectionQuestionModel q = new MultipleSelectionQuestionModel(nextId());
			q.setPrompt(strings.translateMessage("questionPredictEdge.prompt"));
			for (int v = 0; v < size; v++) {
				boolean correct = v == i || v == ((i+1)%size);
				q.addAnswer(
					"v" + v,
					correct ? 5 : 0,
					strings.translateMessage("questionPredictEdge.feedback." +
						(correct ? "correct" : "wrong"))
				);
			}

		q.setGroupID(group.getID());
		lang.addMSQuestion(q);
	}

	/**
	 * Show the concluding outro slide
	 */
	private void runOutro(Point2D point, List<Point2D> vertices, boolean inside) {
		SourceCode outro = lang.newSourceCode(
			new Offset(0, 20, titlePrimitives.get(0), "SW"),
			nextId(),
			null,
			slideProps);

		outro.addMultilineCode(strings.translateMessage("outro1",
			formatPoint(point),
			inside ? "" : strings.translateMessage("not") + " ",
			formatPoints(vertices)), null, null);
		outro.addCodeLine("", null, 1, null);
		lang.nextStep(strings.translateMessage("stepLabelOutro"));

		outro.addMultilineCode(strings.translateMessage("outro2",
			String.valueOf(vertices.size())), null, null);
		outro.addCodeLine("", null, 1, null);
		lang.nextStep();

		outro.addMultilineCode(strings.translateMessage("outro3"), null, null);
		lang.nextStep();
	}

	private String formatPoint(Point2D point) {
		double x = point.getX();
		double y = point.getY();
		return "("
			+ ((((int) x) == x) ? ((int) x) : String.format("%.1f", x))
			+ "|"
			+ ((((int) y) == y) ? ((int) y) : String.format("%.1f", y))
			+ ")";
	}

	private String formatPoints(List<Point2D> points) {
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < points.size(); i++) {
			if (i > 0) {
				s.append(", ");
			}

			if (i % 4 == 0) {
				if (i > 0)
					s.append("\n");
				s.append("    ");
			}

			s.append(String.format("p%d: %s", i, formatPoint(points.get(i))));
		}

		return s.toString();
	}

	// highlight a primitive
	private void highlight(Primitive primitive, Color color) {
		primitive.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
	}

	// unhighlight a primitive to Color.BLACK
	private void unhighlight(Primitive primitive) {
		highlight(primitive, (Color) textProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
	}

	private String nextId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Visual helper class for an euclidean coordinate system using circles and polylines
	 */
	private class CoordinateSystem extends PrimitiveGroup {

		private Polyline yAxis, xAxis;
		private int scaling;

		private CoordinateSystem(Primitive relativeTo, String direction, int x, int y, int width, int height, int scaling) {
			this.scaling = scaling;

			PolylineProperties axisProps = new PolylineProperties();
			axisProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			axisProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

			this.yAxis = lang.newPolyline(new Node[] {
				new Offset(x, y+height*scaling, relativeTo, direction),
				new Offset(x, y, relativeTo, direction),
			}, nextId(), null, axisProps);

			this.xAxis = lang.newPolyline(new Node[] {
				new Offset(x, y+height*scaling, relativeTo, direction),
				new Offset(x+width*scaling, y+height*scaling, relativeTo, direction),
			}, nextId(), null, axisProps);

			super.addPrimitive(xAxis);
			super.addPrimitive(yAxis);

			TextProperties labelProps = new TextProperties();
			labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 11));
			labelProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

			super.addPrimitive(lang.newText(new Offset(0, -20, yAxis, "NW"), "y", nextId(), null, labelProps));
			super.addPrimitive(lang.newText(new Offset(10, -6, xAxis, "NE"), "x", nextId(), null, labelProps));

			PolylineProperties gridProps = new PolylineProperties();
			gridProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
			gridProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

			for (int i = 0; i < width; i += 10) {
				Polyline gridLine = lang.newPolyline(new Node[] {
					new Offset(i*scaling, 0, yAxis, "NW"),
					new Offset(i*scaling, 5, yAxis, "SW")
				}, nextId(), null, gridProps);

				super.addPrimitive(gridLine);

				Text gridLabel = lang.newText(new Offset(i*scaling, 5, xAxis, "SW"),
						String.valueOf(i), nextId(), null, labelProps);
				super.addPrimitive(gridLabel);
			}

			for (int i = 0; i < height; i += 10) {
				Polyline gridLine = lang.newPolyline(new Node[] {
					new Offset(-5, -i*scaling, xAxis, "SW"),
					new Offset(0, -i*scaling, xAxis, "SE")
				}, nextId(), null, gridProps);

				super.addPrimitive(gridLine);

				Text gridLabel = lang.newText(new Offset(-20, -i*scaling-6, yAxis, "SW"),
						String.valueOf(i), nextId(), null, labelProps);
				super.addPrimitive(gridLabel);
			}
		}

		private Node relativePosition(double x, double y) {
			return new Offset((int) Math.round(x*scaling), (int) Math.round(-y*scaling), yAxis, "SW");
		}

		private Node relativePosition(Point2D p) {
			return relativePosition(p.getX(), p.getY());
		}

		private void drawPolygon(List<Point2D> vertices) {
			Node[] nodes = new Node[vertices.size()];
			for (int i = 0; i < nodes.length; i++) {
				nodes[i] = relativePosition(vertices.get(i));
			}

			try {
				// cant throw an exception, because we will check for enough
				// vertices in the ValidatingGenerator method
				super.addPrimitive(lang.newPolygon(nodes, nextId(), null, polygonProps));
			} catch (Exception e) { }
		}

		private void drawPoint(Point2D point, String label) {
			CircleProperties circleProps = new CircleProperties();
			circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
				(Color) rayProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));

			Circle circle = lang.newCircle(relativePosition(point), 3, nextId(), null, circleProps);

			super.addPrimitive(circle);

			TextProperties labelProps = new TextProperties();
			labelProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

			super.addPrimitive(lang.newText(new Offset(0, 5, circle, "C"), label, nextId(), null, labelProps));
		}

		private void drawRay(Point2D point) {
			super.addPrimitive(lang.newPolyline(new Node[] {
				relativePosition(point),
				relativePosition(coordWidth, point.getY())
			}, nextId(), null, rayProps));
		}

		private Polyline drawEdge(Line2D edge) {
			Polyline line = lang.newPolyline(new Node[] {
				relativePosition(edge.getP1()),
				relativePosition(edge.getP2())
			}, nextId(), null, edgeProps);

			super.addPrimitive(line);
			return line;
		}

	}

	/**
	 * Visual helper class, because "ConceptualStack" didn't work out with
	 * relative positioning.
 	 */
	private class TextStack extends PrimitiveGroup {

		private Node root;

		private Stack<Text> elements;
		private Stack<Integer> indentation;

		private TextStack(Primitive relativeTo, String direction, int x, int y) {
			super();

			this.elements = new Stack<>();
			this.indentation =  new Stack<>();
			this.root = new Offset(x, y, relativeTo, direction);
		}

		private Text push(String text, int indentation) {
			Node position = elements.empty()
				? root
				: new Offset((indentation - this.indentation.peek()) * 10, 5, elements.peek(), "SW");

			this.indentation.push(indentation);
			Text element = elements.push(lang.newText(position, text, nextId(), null, textProps));
			super.addPrimitive(element);
			return element;
		}

		private void pop() {
			Text primitive = elements.pop();
			if (primitive == null) {
				return;
			}

			super.removePrimitive(primitive);

			indentation.pop();
			primitive.hide();
		}

	}

	private class PrimitiveGroup {

		private List<Primitive> group;

		protected PrimitiveGroup() {
			this.group = new ArrayList<>();
		}

		protected void addPrimitive(Primitive p) {
			this.group.add(p);
		}

		protected void removePrimitive(Primitive p) {
			this.group.remove(p);
		}

		public List<Primitive> getAllPrimitives() {
			return group;
		}

	}
}
