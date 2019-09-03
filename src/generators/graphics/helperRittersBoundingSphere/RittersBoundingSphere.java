package generators.graphics.helperRittersBoundingSphere;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * @author Lukas Dietrich <lukasalexander.dietrich@stud.tu-darmstadt.de>
 * @version 4.0 20180602
 */
public class RittersBoundingSphere {

	private Translator strings;
	private Language lang;
	private double questionProbability;
	private List<Primitive> titlePrimitives;

	private TextProperties textProps;
	private SourceCodeProperties slideProps;
	private SourceCodeProperties codeProps;

	private Color candidateColor, bestCandidateColor;
	private Color codeHighlight, stepHighlight;
	private int coordWidth, coordHeight;
	private int coordScaling;

	public static final String SOURCECODE = ""
        + "func boundingSphere(p: [Point]) {\n"                             //  0 *
        + "    let x = p[0]\n"                                              //  1
        + "    let y = getMaximumDistancePoint(x, p)\n"                     //  2 +
        + "    let z = getMaximumDistancePoint(y, p)\n"                     //  3 +
        + "\n"                                                              //  4
        + "    let m = getCenterpoint(y, z)\n"                              //  5 +
        + "    let r = getDistance(y, z) / 2\n"                             //  6 +
        + "    drawSphere(m, r)\n"                                          //  7
        + "}\n"                                                             //  8
        + "\n"                                                              //  9
        + "func getMaximumDistancePoint(a: Point, p: [Point]) -> Point {\n" // 10 *
        + "    let i = 0\n"                                                 // 11
        + "    let max = getDistance(a, p[i])\n"                            // 12
        + "\n"                                                              // 13
        + "    for (j in 1 .. p.length) {\n"                                // 14
        + "        let distance = getDistance(a, p[j])\n"                   // 15
        + "        if (distance > max) {\n"                                 // 16
        + "            i = j\n"                                             // 17
        + "            max = distance\n"                                    // 18
        + "        }\n"                                                     // 19
        + "    }\n"                                                         // 20
        + "\n"                                                              // 21
        + "    return p[i]\n"                                               // 22
        + "}\n"                                                             // 23
        + "\n"                                                              // 24
        + "// Calculate distance between two points\n"                      // 25
        + "func getDistance(a: Point, b: Point) -> float\n"                 // 26 *
        + "\n"                                                              // 27
        + "// Calculate the centerpoint between two points\n"               // 28
        + "func getCenterpoint(a: Point, b: Point) -> Point\n"              // 29 *
        + "\n"                                                              // 30
        + "// Draws a sphere with a given centerpoint and radius\n"         // 31
        + "func drawSphere(center: Point, radius: float)";                  // 32 *

	public RittersBoundingSphere(
		Language lang, Translator strings,
		double questionProbability,
		int fontSize,
		SourceCodeProperties codeProps, SourceCodeProperties textProps,
		TextProperties stepProps,
		PolylineProperties candidateProps, PolylineProperties bestProps,
		int coordWidth, int coordHeight, int coordScaling
	) {
		this.lang = lang;
		this.strings = strings;
		this.questionProbability = questionProbability;

		this.codeHighlight = (Color) codeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
		this.stepHighlight = (Color) stepProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);

		this.candidateColor = (Color) candidateProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		this.bestCandidateColor = (Color) bestProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);

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

	public void run(List<Point2D> points) {
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

		this.runIntro(points);
		Sphere2D sphere = this.runAlgorithm(points);
		this.runOutro(points, sphere);
	}

	/**
	 * Show the initial intro slide
	 */
	private void runIntro(List<Point2D> points) {
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

		for (int i = 1; i <= 4; i++) {
			String s = strings.translateMessage("step" + i);
			intro.addCodeLine(s, null, 2, null);
			lang.nextStep();
		}

		intro.addCodeLine("", null, 1, null);
		intro.addMultilineCode(strings.translateMessage("task", formatPoints(points)), null, null);
		lang.nextStep();

		lang.hideAllPrimitivesExcept(titlePrimitives);
	}

	/**
	 * Show the main part of the animation: the actual algorithm
	 */
	private Sphere2D runAlgorithm(List<Point2D> points) {
		SourceCode code = lang.newSourceCode(
			new Offset(0, 20, titlePrimitives.get(0), "SW"),
			nextId(),
			null,
			codeProps);

		for (String line : SOURCECODE.split("\n")) {
			int indentation = 0;
			while (line.startsWith("    ")) {
				indentation++;
				line = line.substring(4);
			}
			code.addCodeLine(line, null, indentation, null);
		}

		CoordinateSystem cs = new CoordinateSystem(code, "NE", 100, 0, coordWidth, coordHeight, coordScaling);
		cs.addPoints(points);

		TextStack status = new TextStack(cs.yAxis, "SW", 0, 50);
		TextStack coords = new TextStack(cs.yAxis, "NW", coordWidth*coordScaling + 50, 0);

		List<Text> pointCoords = new ArrayList<>();

		for (int i = 0; i < points.size(); i++) {
			pointCoords.add(coords.push(String.format("p%d = %s",
				i, formatPoint(points.get(i))), 1));
		}

		lang.nextStep(strings.translateMessage("stepLabelInit"));

		// func boundingSphere
		code.highlight(0);
		lang.nextStep();

		// choose x
		Point2D y, z;

		Text step1 = status.push(strings.translateMessage("step1"), 1);
		append(pointCoords.get(0),  " = x");
		highlight(step1, stepHighlight);
		code.toggleHighlight(0, 1);
		cs.fillPoint(0, stepHighlight);

		{
			TrueFalseQuestionModel q = new TrueFalseQuestionModel(nextId());
				q.setPointsPossible(5);
				q.setPrompt(strings.translateMessage("questionX.prompt"));
				q.setCorrectAnswer(false);
				q.setFeedbackForAnswer(false, strings.translateMessage("questionX.feedback.correct"));
				q.setFeedbackForAnswer(true, strings.translateMessage("questionX.feedback.wrong"));
			lang.addTFQuestion(q);
		}

		lang.nextStep(strings.translateMessage("stepLabelX"));

		// find y
		Text step2 = status.push(strings.translateMessage("step2"), 1);
		unhighlight(step1);
		highlight(step2, stepHighlight);
		code.toggleHighlight(1, 2);
		lang.nextStep(strings.translateMessage("stepLabelY"));

		int yIndex = runFindMaximumDistancePoint(code, cs, status, 'x', 0, points);
		y = points.get(yIndex);
		cs.fillPoint(yIndex, stepHighlight);
		append(pointCoords.get(yIndex),  " = y");
		lang.nextStep();

		// find z
		Text step3 = status.push(strings.translateMessage("step3"), 1);
		unhighlight(step2);
		highlight(step3, stepHighlight);
		code.toggleHighlight(2, 3);
		lang.nextStep(strings.translateMessage("stepLabelZ"));

		int zIndex = runFindMaximumDistancePoint(code, cs, status, 'y', yIndex, points);
		z = points.get(zIndex);
		cs.fillPoint(zIndex, stepHighlight);
		append(pointCoords.get(zIndex),  " = z");
		lang.nextStep();

		// find m
		Text step4 = status.push(strings.translateMessage("step4"), 1);
		unhighlight(step3);
		highlight(step4, stepHighlight);

		Point2D center = new Point2D.Double(
			Math.min(y.getX(), z.getX()) + Math.abs(y.getX() - z.getX()) / 2,
			Math.min(y.getY(), z.getY()) + Math.abs(y.getY() - z.getY()) / 2);

		code.toggleHighlight(3, 5);
		code.highlight(26);
		status.push(String.format("let m = %s", formatPoint(center)), 2);
		lang.nextStep(strings.translateMessage("stepLabelCenter"));

		// find r
		double radius = y.distance(z) / 2d;
		status.push(String.format("let r = %.1f", radius), 2);
		code.toggleHighlight(5, 6);
		code.toggleHighlight(26, 29);
		lang.nextStep(strings.translateMessage("stepLabelRadius"));

		// draw sphere
		code.toggleHighlight(6, 7);
		code.toggleHighlight(29, 32);

		Sphere2D sphere = new Sphere2D(center, y.distance(z) / 2);
		cs.drawSphere(sphere);
		lang.nextStep(strings.translateMessage("stepLabelSphere"));

		unhighlight(step4);
		code.unhighlight(7);
		code.unhighlight(32);

		for (Primitive p : sphere.getAllPrimitives()) {
			p.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		}

		for (int i = 0; i < points.size(); i++) {
			cs.fillPoint(i, Color.WHITE);
		}

		lang.nextStep();

		List<Primitive> keepPrimitives = new ArrayList<>();
		keepPrimitives.addAll(titlePrimitives);
		keepPrimitives.addAll(cs.getAllPrimitives());
		lang.hideAllPrimitivesExcept(keepPrimitives);

		return sphere;
	}

	private void append(Text text, String suffix) {
		text.setText(text.getText() + suffix, null, null);
	}

	private int runFindMaximumDistancePoint(SourceCode code, CoordinateSystem cs, TextStack status, char name, int aIndex, List<Point2D> p) {
		Point2D a = p.get(aIndex);

		code.highlight(10);
		lang.nextStep();

		int i = 0;
		Text textIndex = status.push("let i = 0", 2);
		highlight(textIndex, bestCandidateColor);

		code.toggleHighlight(10, 11);
		lang.nextStep();

		double max = a.distance(p.get(i));
		cs.showConnection(aIndex, i, bestCandidateColor);
		Text textMax = status.push(String.format("let max = getDistance(%c, p%d) = %.2f", name, i, max), 2);
		highlight(textMax, bestCandidateColor);

		code.toggleHighlight(11, 12);
		code.highlight(26);
		lang.nextStep();

		QuestionGroupModel qGroup = new QuestionGroupModel(nextId(), 2);
		showDistanceQuestion(qGroup, i, 1, p);

		code.toggleHighlight(12, 14); // skip blank line
		code.unhighlight(26);
		lang.nextStep();

		for (int j = 1; j < p.size(); j++) {
			double distance = a.distance(p.get(j));
			cs.showConnection(aIndex, j, candidateColor);
			Text textCandidate = status.push(String.format("let distance = getDistance(%c, p%d) = %.2f", name, j, distance), 3);
			highlight(textCandidate, candidateColor);

			code.toggleHighlight(14, 15);
			code.highlight(26);
			lang.nextStep();

			code.toggleHighlight(15, 16);
			code.unhighlight(26);
			lang.nextStep();

			if (distance > max) {
				highlight(textIndex, bestCandidateColor);
				textIndex.setText("let i = " + j, null, null);

				code.toggleHighlight(16, 17);
				lang.nextStep();

				cs.hideConnection(aIndex, i);
				cs.hideConnection(aIndex, j);
				cs.showConnection(aIndex, j, bestCandidateColor);
				max = distance;
				i = j;

				highlight(textMax, bestCandidateColor);
				textMax.setText(String.format("let max = getDistance(%c, p%d) = %.2f", name, j, distance), null, null);

				code.toggleHighlight(17, 18);
				lang.nextStep();

				code.toggleHighlight(18, 14);
			} else {
				cs.hideConnection(aIndex, j);
				code.toggleHighlight(16, 14);
			}

			showDistanceQuestion(qGroup, i, j+1, p);

			status.pop(); // distance
			lang.nextStep();
		}

		code.toggleHighlight(14, 22);
		lang.nextStep();

		code.unhighlight(22);
		cs.hideConnection(aIndex, i);
		status.pop(); // max
		status.pop(); // index

		return i;
	}

	private void showDistanceQuestion(QuestionGroupModel group, int i, int j, List<Point2D> p) {
		if (i >= p.size() || j >= p.size()) {
			return;
		}

		if (Math.random() > questionProbability) {
			return;
		}

		MultipleSelectionQuestionModel q = new MultipleSelectionQuestionModel(nextId());
			q.setPrompt(strings.translateMessage("questionDistance.prompt"));
			for (int k = 0; k < p.size(); k++) {
				boolean correct = k == i || k == j;
				q.addAnswer(
					"p" + k, 
					correct ? 5 : 0,
					strings.translateMessage("questionDistance.feedback." +
						(correct ? "correct" : "wrong"))
				);
			}
			q.setGroupID(group.getID());
		lang.addMSQuestion(q);
	}

	/**
	 * Show the concluding outro slide
	 */
	private void runOutro(List<Point2D> points, Sphere2D sphere) {
		SourceCode outro = lang.newSourceCode(
			new Offset(0, 20, titlePrimitives.get(0), "SW"),
			nextId(),
			null,
			slideProps);

		outro.addMultilineCode(strings.translateMessage("outro1", new Object[] {
			formatPoints(points),
			formatPoint(sphere.getCenter()),
			String.format("%.1f", sphere.getRadius()),
		}), null, null);
		outro.addCodeLine("", null, 1, null);
		for (Primitive p : sphere.getAllPrimitives()) {
			p.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight, null, null);
		}
		lang.nextStep(strings.translateMessage("stepLabelOutro"));

		outro.addMultilineCode(strings.translateMessage("outro2"), null, null);
		outro.addCodeLine("", null, 1, null);
		lang.nextStep();

		outro.addMultilineCode(strings.translateMessage("outro3", new Object[] {
			points.size(),
			2 * points.size(),
		}), null, null);
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

	// highlight a Text primitive
	private void highlight(Text text, Color color) {
		text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
	}

	// unhighlight a Text primitive to Color.BLACK
	private void unhighlight(Text text) {
		highlight(text, (Color) textProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
	}

	private String nextId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Represent a sphere in the 2D space
	 */
	private class Sphere2D extends PrimitiveGroup {

		private Point2D center;
		private double radius;

		private Sphere2D(Point2D center, double radius) {
			this.center = center;
			this.radius = radius;
		}

		private Point2D getCenter() {
			return center;
		}

		private double getRadius() {
			return radius;
		}

	}

	/**
	 * Visual helper class for an euclidean coordinate system using circles and polylines
	 */
	private class CoordinateSystem extends PrimitiveGroup {

		private Polyline yAxis, xAxis;
		private int scaling;

		private List<Point2D> coords;
		private List<Circle> points;
		private List<Map<Integer, Polyline>> connections;

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

			this.points = new ArrayList<>();
			this.coords = new ArrayList<>();
			this.connections = new ArrayList<>();
		}

		private Node relativePosition(double x, double y) {
			return new Offset((int) Math.round(x*scaling), (int) Math.round(-y*scaling), yAxis, "SW");
		}

		private void addPoints(Collection<Point2D> points) {
			for (Point2D p : points) {
				CircleProperties props = new CircleProperties();
				props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

				Circle circle = lang.newCircle(relativePosition((int) p.getX(), (int) p.getY()), 3, nextId(), null, props);
				super.addPrimitive(lang.newText(new Offset(0, 3, circle, "SW"), "p" + this.points.size(), nextId(), null));

				for (int i = 0; i < coords.size(); i++) {
					if (connections.size() <= i) {
						connections.add(new HashMap<>());
					}


					Point2D p0 = coords.get(i);
					Polyline connection = lang.newPolyline(new Node[] {
						relativePosition((int) p0.getX(), (int) p0.getY()),
						relativePosition((int) p.getX(), (int) p.getY())
					}, nextId(), null);

					connection.hide();
					connections.get(i).put(coords.size(), connection);
					super.addPrimitive(connection);
				}

				this.coords.add(p);
				this.points.add(circle);

				super.addPrimitive(circle);
			}
		}

		private void fillPoint(int i, Color color) {
			points.get(i).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, color, null, null);
		}

		private void showConnection(int a, int b, Color color) {
			if (a == b) {
				return;
			}

			int i0 = Math.min(a, b);
			int i1 = Math.max(a, b);

			Polyline connection = connections
				.get(i0)
				.get(i1);
			connection.show();
			connection.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, new MsTiming(100));
		}

		private void hideConnection(int a, int b) {
			if (a == b) {
				return;
			}

			int i0 = Math.min(a, b);
			int i1 = Math.max(a, b);

			connections.get(i0).get(i1).hide();
		}

		private void drawSphere(Sphere2D sphere) {
			CircleProperties sphereProps = new CircleProperties();
			sphereProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight);

			Node position = relativePosition(
				sphere.getCenter().getX(),
				sphere.getCenter().getY());

			Circle spherePrimitive = lang.newCircle(
				position,
				(int) Math.ceil(sphere.getRadius()*scaling),
				nextId(),
				null,
				sphereProps);

			Circle center = lang.newCircle(
				position,
				3,
				nextId(),
				null,
				sphereProps);

			TextProperties labelProps = new TextProperties();
			labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight);
			Text centerLabel = lang.newText(new Offset(0, 3, center, "SW"), "m", nextId(), null, labelProps);

			PolylineProperties radiusProps = new PolylineProperties();
			radiusProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeHighlight);

			Polyline radius = lang.newPolyline(new Node[] {
				new Offset(0, 0, center, "C"),
				new Offset((int) Math.ceil(sphere.getRadius()*scaling), 0, center, "C"),
			}, nextId(), null, radiusProps);

			Text radiusLabel = lang.newText(new Offset(0, 3, radius, "C"), "r", nextId(), null, labelProps);

			super.addPrimitive(radiusLabel);
			super.addPrimitive(radius);
			super.addPrimitive(centerLabel);
			super.addPrimitive(spherePrimitive);
			super.addPrimitive(center);

			sphere.addPrimitive(radiusLabel);
			sphere.addPrimitive(radius);
			sphere.addPrimitive(centerLabel);
			sphere.addPrimitive(spherePrimitive);
			sphere.addPrimitive(center);
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
