/*
 * test.java
 * Julian Fischer, Christian Seybert, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

public class BoundingSphereGenerator implements Generator {
	private Language	lang;
	private int[][]		points;

	@Override
	public void init() {
		lang = new AnimalScript("Bouncing Bubble", "Julian Fischer, Christian Seybert", 800, 600);
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		points = (int[][]) primitives.get("points");
		Point[] points2 = new Point[points.length];
		for (int i = 0; i < points.length; i++)
			points2[i] = new Point(points[i][0], points[i][1]);

		{
			Language l = lang;// Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Bouncing Bubble", "Julian Fischer, Christian Seybert", 800, 600);
			l.setStepMode(true);
			Text title = l.newText(new Coordinates(20, 20), "Bounding sphere calculation using Bouncing Bubble algorithm", "title", null);
			title.setFont(new Font("SansSerif", 1, 20), null, null);

			SourceCode description = l.newSourceCode(new Coordinates(20, 70), "description", null);
			description.addMultilineCode("Bei dem Bounding-Sphere-Problem geht es darum, einen kleinstmöglichen Kreis\n" +
					"(oder mehrdimensionale Äquivalente) um eine gegebene Punktmenge zu ziehen. Der Bouncing Bubble Algorithmus\n" +
					"löst dieses Problem näherungsweise, indem bei einem Punkt gestartet wird und immmer mehr Punkte mit einbezogen\n" +
					"werden. Dabei \"springt\" der Kreis immer wieder durch die Gegend, wonach er benannt wurde.\n.\n" +
					"In den ersten beiden Phasen wird nach Punkten gesucht, die außerhalb des Kreises liegen. Wird einer gefunden,\n" +
					"wird der Kreis ein wenig erweitert und solange zu diesem Punkt hin bewegt, bis er im Kreis enthalten ist. Dabei\n" +
					"kann es passieren, dass ehemals enthaltene Punkte wieder den Kreis verlassen. Dies wird insgesamt zwei Mal für alle\n" +
					"Punkte gemacht. Danach ist der Kreis meistens schon eine ziemlich gute Näherung an die Bounding Sphere, allerdings\n" +
					"kann es sein, dass einzelne Punkte immer noch nicht enthalten sind.\n.\n" +
					"Für diese gibt es dann eine dritte Phase. Sie funktioniert genauso wie die ersten beiden, nur dass der Kreis jedes Mal\n" +
					"so stark vergrößert wird, dass alle Punkte in ihm bleiben, wenn er zu dem fehlenden Punkt verschoben wird.", null, null);
			l.nextStep("Einleitung");
			description.hide();

			calculateBoundSphere(points2, l);
			l.nextStep("Abschluss");
			l.hideAllPrimitivesExcept(Arrays.asList(title));

			description = l.newSourceCode(new Coordinates(20, 70), "description", null);
			description.addMultilineCode("Der Algorithmus iteriert drei Mal über die Punktmenge, was einer linearen Laufzeitkomplexität\n" +
					"entspricht. Das Ergebnis ist immer korrekt, d.h. es ist immer eine Bounding Sphere. Gleichzeitig ist das Ergebnis in der Regel\n" +
					"nur wenige Prozent größer als das Optimum.", null, null);
		}

		return lang.toString();
	}

	@Override
	public String getName() {
		return "Bouncing Bubble";
	}

	@Override
	public String getAlgorithmName() {
		return "Minimal bounding sphere";
	}

	@Override
	public String getAnimationAuthor() {
		return "Julian Fischer, Christian Seybert";
	}

	@Override
	public String getDescription() {
		return "Bei dem Bounding-Sphere-Problem geht es darum, einen kleinstmöglichen Kreis\n" +
				"(oder mehrdimensionale Äquivalente) um eine gegebene Punktmenge zu ziehen.";
	}

	@Override
	public String getCodeExample() {
		return "public Circle boundingCircle(Point[] points) {\n"
				+ "\tPoint center = points[0]\n;"
				+ "\tfloat radius = 0.00001;\n"
				+ "\trepeat(2) {\n"
				+ "\t\tfor point in points {\n"
				+ "\t\t\tdouble len = ||point-center||\n"
				+ "\t\t\tif len > r {\n"
				+ "\t\t\t\tdouble alpha = len / r\n"
				+ "\t\t\t\tr *= (alpha+1)/(alpha*2)\n"
				+ "\t\t\t\tcenter = center * (1 + 1 / alpha²) / 2 + pos * (1 - 1 / alpha²) / 2\n"
				+ "\t\t\t}\n"
				+ "\t\t}\n"
				+ "\t}\n"
				+ "\tfor point in points {\n"
				+ "\t\tdouble len = ||point-center||\n"
				+ "\t\tif len > r {\n"
				+ "\t\t\tr = (r + len) / 2\n"
				+ "\t\t\tcenter += (len - r) * (point-center) / len\n"
				+ "\t\t}\n"
				+ "\t}\n"
				+ "\treturn (center, radius)\n"
				+ "}";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	// ####################################################################################################

	static class Point {
		double x, y;

		Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	};

	static class Sphere {
		Point	p;
		double	r;
	};

	double calculateDistanceBetweenTwoPoints(Point point0, Point point1) {
		if (point0.x == point1.x && point0.y == point1.y)
			return 0;
		else
			return Math.sqrt(Math.pow(point0.x - point1.x, 2)
					+ Math.pow(point0.y - point1.y, 2));
	}

	boolean pointOutside(Sphere ball, Point[] points) {
		for (int i = 0; i < points.length; i++) {
			double distance = calculateDistanceBetweenTwoPoints(ball.p, points[i]);
			if (distance > ball.r) {
				return true;
			}
		}
		return false;
	}

	Point calculateCenter(Point point0, Point point1) {
		Point center = new Point(0, 0);
		center.x = (point0.x + point1.x) / 2;
		center.y = (point0.y + point1.y) / 2;
		return center;
	}

	Point calculateDiff(Point point0, Point point1) {
		Point diff = new Point(0, 0);
		diff.x = point0.x - point1.x;
		diff.y = point0.y - point1.y;
		return diff;
	}

	final Timing timing = new MsTiming(500);

	Sphere calculateBoundSphere(Point[] vertices, Language l) {
		Circle[] points = new Circle[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			CircleProperties circleProps = new CircleProperties();
			circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			points[i] = l.newCircle(new Coordinates((int) vertices[i].x, (int) vertices[i].y), 3, null, null, circleProps);
		}

		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		SourceCode sc = l.newSourceCode(new Coordinates(500, 40), "sourceCode", null, scProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay
		sc.addCodeLine("public Circle boundingCircle(Point[] points) {", null, 0, null); // 0
		sc.addCodeLine("Point center = points[0];", null, 1, null); // 1
		sc.addCodeLine("float radius = 0.00001;", null, 1, null); // 2
		sc.addCodeLine("repeat(2) {", null, 1, null); // 3
		sc.addCodeLine("for point in points {", null, 2, null); // 4
		sc.addCodeLine("double len = ||point-center||", null, 3, null); // 5
		sc.addCodeLine("if len > r {", null, 3, null); // 6
		sc.addCodeLine("double alpha = len / r", null, 4, null); // 7
		sc.addCodeLine("r *= (alpha+1)/(alpha*2)", null, 4, null); // 8
		sc.addCodeLine("center = center * (1 + 1 / alpha²) / 2 + pos * (1 - 1 / alpha²) / 2", null, 4, null); // 9
		sc.addCodeLine("}", null, 3, null); // 10
		sc.addCodeLine("}", null, 2, null); // 11
		sc.addCodeLine("}", null, 1, null); // 12
		sc.addCodeLine("for point in points {", null, 1, null); // 13
		sc.addCodeLine("double len = ||point-center||", null, 2, null); // 14
		sc.addCodeLine("if len > r {", null, 2, null); // 15
		sc.addCodeLine("r = (r + len) / 2", null, 3, null); // 16
		sc.addCodeLine("center += (len - r) * (point-center) / len", null, 3, null); // 17
		sc.addCodeLine("}", null, 2, null); // 18
		sc.addCodeLine("}", null, 1, null); // 19
		sc.addCodeLine("return (center, radius)", null, 1, null); // 20
		sc.addCodeLine("}", null, 0, null); // 21

		Text phase = l.newText(new Coordinates(40, 500), "", "phase", null);

		l.nextStep();

		Sphere s = new Sphere();
		s.p = new Point(0, 0);
		s.p.x = vertices[0].x;
		s.p.y = vertices[0].y;
		s.r = 1;
		CircleProperties circleProps = new CircleProperties();
		circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		Circle circle = l.newCircle(new Coordinates((int) s.p.x, (int) s.p.y), (int) s.r, "Bounding sphere", null, circleProps);
		circleProps = new CircleProperties();
		circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
		circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		Circle center = l.newCircle(new Coordinates((int) s.p.x, (int) s.p.y), 2, "Bounding center", null, circleProps);
		center.changeColor(null, Color.GREEN, null, null);
		center.moveTo(null, null, new Coordinates((int) s.p.x, (int) s.p.y), null, null);
		circle.moveTo(null, null, new Coordinates((int) (s.p.x - s.r), (int) (s.p.y - s.r)), null, null);

		sc.highlight(1);
		sc.highlight(2);
		l.nextStep("Phase 1");
		sc.unhighlight(1);
		sc.unhighlight(2);
		sc.highlight(3, 0, true);
		sc.highlight(4, 0, true);
		for (int i = 0; i < 2; i++) {
			phase.setText("Phase " + (i + 1), null, null);
			if (i == 1)
				l.nextStep("Phase 2");
			for (int j = 0; j < vertices.length; j++) {
				Point pos = vertices[j];
				double len = calculateDistanceBetweenTwoPoints(pos, s.p);
				Polyline dist = l.newPolyline(new Node[] { new Coordinates((int) s.p.x, (int) s.p.y), new Coordinates((int) pos.x, (int) pos.y) }, null, null);

				if (len > s.r) {
					sc.highlight(5);
					points[j].changeColor("fillColor", Color.ORANGE, null, null);
					l.nextStep();
					double alpha = len / s.r, alphaSq = alpha * alpha;
					s.r = 0.5f * (alpha + 1 / alpha) * s.r;
					s.p.x = 0.5f * ((1 + 1 / alphaSq) * s.p.x + (1 - 1 / alphaSq) * pos.x);
					s.p.y = 0.5f * ((1 + 1 / alphaSq) * s.p.y + (1 - 1 / alphaSq) * pos.y);
					circle.moveTo(null, "translateRadius", new Coordinates((int) s.r, 0), timing, timing);
					sc.highlight(6, 0, true);
					sc.toggleHighlight(5, 0, false, 8, 0);
					l.nextStep();
					center.moveTo(null, null, new Coordinates((int) s.p.x, (int) s.p.y), timing, timing);
					circle.moveTo(null, null, new Coordinates((int) (s.p.x - s.r), (int) (s.p.y - s.r)), timing, timing);
					sc.toggleHighlight(8, 0, false, 9, 0);
					l.nextStep();
					sc.unhighlight(9);
					sc.unhighlight(6);
					points[j].changeColor("fillColor", Color.BLACK, null, null);
				} else {
					sc.highlight(5);
					points[j].changeColor("fillColor", Color.GREEN, null, null);
					l.nextStep();
					sc.unhighlight(5);
					points[j].changeColor("fillColor", Color.BLACK, null, null);
				}
				dist.hide();
			}
		}
		phase.setText("Phase 3", null, null);
		sc.unhighlight(3);
		sc.unhighlight(4);
		sc.highlight(13, 0, true);
		l.nextStep("Phase 3");
		for (int j = 0; j < vertices.length; j++) {
			Point pos = vertices[j];
			double len = calculateDistanceBetweenTwoPoints(pos, s.p);
			Polyline dist = l.newPolyline(new Node[] { new Coordinates((int) s.p.x, (int) s.p.y), new Coordinates((int) pos.x, (int) pos.y) }, null, null);
			if (len > s.r) {
				sc.highlight(14);
				points[j].changeColor("fillColor", Color.ORANGE, null, null);
				l.nextStep();
				Point diff = calculateDiff(pos, s.p);
				s.r = (s.r + len) / 2.0f;
				s.p.x = s.p.x + ((len - s.r) * diff.x / len);
				s.p.y = s.p.y + ((len - s.r) * diff.y / len);
				circle.moveTo(null, "translateRadius", new Coordinates((int) s.r, 0), timing, timing);
				sc.highlight(15, 0, true);
				sc.toggleHighlight(14, 0, false, 16, 0);
				l.nextStep();
				center.moveTo(null, null, new Coordinates((int) s.p.x, (int) s.p.y), timing, timing);
				circle.moveTo(null, null, new Coordinates((int) (s.p.x - s.r), (int) (s.p.y - s.r)), timing, timing);
				sc.toggleHighlight(16, 0, false, 17, 0);
				l.nextStep();
				sc.unhighlight(17);
				sc.unhighlight(15);
				points[j].changeColor("fillColor", Color.BLACK, null, null);
			} else {
				sc.highlight(14);
				points[j].changeColor("fillColor", Color.GREEN, null, null);
				l.nextStep();
				sc.unhighlight(14);
				points[j].changeColor("fillColor", Color.BLACK, null, null);
			}
			dist.hide();
		}
		sc.unhighlight(13);
		return s;
	}

	public static void main(String[] args) {
		Generator generator = new BoundingSphereGenerator();
		Animal.startGeneratorWindow(generator);
	}
}