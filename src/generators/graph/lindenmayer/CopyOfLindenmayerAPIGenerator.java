package generators.graph.lindenmayer;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * 
 * @author Deniz Emre Mohr, Murat Batu
 * 
 */

public class CopyOfLindenmayerAPIGenerator {
	private CircleProperties currentCirclePosition;
	private CircleProperties savedCirclePosition;

	private SourceCodeProperties sourceCodeProperties;
	private RectProperties rectProperties;
	private TextProperties headerTextProperties;
	private PolylineProperties currentDirectionOfPolyline;
	private TextProperties shownTextProperties;
	private PolylineProperties currentPolyline;
	private String Eingabe;

	private PolylineProperties cdopDel;
	private CircleProperties cpDel;
	private PolylineProperties drawnPolyline;

	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;
	/**
	 * The header text including the headline
	 */
	private Text header;

	/**
	 * The rectangle around the headline
	 */
	private Rect hRect;

	/**
	 * Globally defined text properties
	 */
	private TextProperties textProps;

	/**
	 * the source code shown in the animation
	 */
	private SourceCode src;
	/**
	 * Globally defined source code properties
	 */
	private SourceCodeProperties sourceCodeProps;

	public void init() {

	}

	public CopyOfLindenmayerAPIGenerator(Language lang) {
		this.lang = lang;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}

	private static final String DESCRIPTION = "L-Trees werden bei Prozeduraler Content-Generierung benutzt."
			+ "Sie basieren auf Lindenmayer's L-Systeme von 1974."
			+ "L-Trees sind kontextfreie Grammatiken, "
			+ "die mit Hilfe von Strings dargestellt werden.";

	private static final String SOURCECODE = "Im Folgenden werden diese Regeln benutzt, um die Baeume zu generieren:"
			+ "F   |   Gehe ein Schritt vorwaerts"
			+ "+   |   Rotiere um 45 Grad nach links"
			+ "-    |   Rotiere um 45 Grad nach rechts"
			+ "[    |   Speichere die Position und Richtung des Turtles auf einem Stack"
			+ "]    |   Hole die Position und Richtung des Turtles aus dem Stack";

	public void defaultProperties() {
		headerTextProperties = new TextProperties();
		headerTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.BOLD, 24));

		rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		sourceCodeProperties = new SourceCodeProperties();
		sourceCodeProperties.set(
				AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		sourceCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.RED);
		sourceCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);

		shownTextProperties = new TextProperties();
		shownTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.PLAIN, 22));

		currentPolyline = new PolylineProperties();
		// depth_properties auch bei circle (evtl. �ber wizard)
		currentPolyline.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		currentPolyline.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		drawnPolyline = new PolylineProperties();
		drawnPolyline.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		drawnPolyline.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		currentCirclePosition = new CircleProperties();
		currentCirclePosition.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.MAGENTA);
		// cpCurr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		savedCirclePosition = new CircleProperties();
		savedCirclePosition.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.GREEN);
		// cpSave.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		cpDel = new CircleProperties();
		cpDel.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);

		currentDirectionOfPolyline = new PolylineProperties();
		currentDirectionOfPolyline.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.DARK_GRAY);
		currentDirectionOfPolyline.set(
				AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		cdopDel = new PolylineProperties();
		cdopDel.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		cdopDel.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
	}

	public void start() {
		defaultProperties();
		// show the header with a heading surrounded by a rectangle
		header = lang.newText(new Coordinates(20, 30),
				"Lindenmayer-Systeme - L-Tree Animation", "header", null,
				headerTextProperties);

		hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, rectProperties);

		// setup the description page
		lang.nextStep();

		lang.newText(new Coordinates(20, 80),
				"L-Trees werden bei Prozeduraler Content-Generierung benutzt.",
				"desc1", null, shownTextProperties);
		lang.newText(new Offset(0, 25, "desc1", AnimalScript.DIRECTION_NW),
				"Sie basieren auf Lindenmayer's L-Systeme von 1974.", "desc2",
				null, shownTextProperties);
		lang.newText(new Offset(0, 25, "desc2", AnimalScript.DIRECTION_NW),
				"L-Trees sind kontextfreie Grammatiken,", "desc3", null,
				shownTextProperties);
		lang.newText(new Offset(0, 25, "desc3", AnimalScript.DIRECTION_NW),
				"die mit Hilfe von Strings dargestellt werden.", "desc4", null,
				shownTextProperties);

		lang.nextStep();
		// disable the first page(description) and show the
		// algorithm(sourcecode)
		lang.hideAllPrimitives();
		header.show();
		hRect.show();

		src = lang.newSourceCode(new Coordinates(20, 80), "ltreeSource", null,
				sourceCodeProperties);
		src.addCodeLine(
				"Im Folgenden werden diese Regeln benutzt, um die Baeume zu generieren:",
				null, 0, null);
		src.addCodeLine("F   |   Gehe ein Schritt vorwaerts", null, 0, null);
		src.addCodeLine("+   |   Rotiere um 45 Grad nach links", null, 0, null);
		src.addCodeLine("-    |   Rotiere um 45 Grad nach rechts", null, 0,
				null);
		src.addCodeLine(
				"[    |   Speichere die Position und Richtung des Turtles auf einem Stack",
				null, 0, null);
		src.addCodeLine(
				"]    |   Hole die Position und Richtung des Turtles aus dem Stack",
				null, 0, null);

		// show the example to be calculated
		lang.nextStep();

		src.addCodeLine("", null, 0, null);
		src.addCodeLine("", null, 0, null);

		src.addCodeLine(
				"Wir generieren aus dem Folgenden String schrittweise einen Baum:",
				null, 0, null);
		src.addCodeLine("F[+F][-F[-F]F]F[+F][-F]", "bs", 0, null);

		// call the algorithm
		lang.nextStep();

		lindenmayer();

		// final text
		lang.nextStep();

		lang.hideAllPrimitives();
		header.show();
		hRect.show();

		lang.newText(new Coordinates(20, 80),
				"Mit Hilfe von L-Trees koennen bei Generieren", "desc1", null,
				shownTextProperties);
		lang.newText(new Offset(0, 25, "desc1", AnimalScript.DIRECTION_NW),
				"von Landschaften zufaellige Baeume erstellt werden", "desc2",
				null, shownTextProperties);
		lang.newText(new Offset(0, 25, "desc2", AnimalScript.DIRECTION_NW),
				"Hierzu muss man als Eingabe verschiedene Strings", "desc3",
				null, shownTextProperties);
		lang.newText(new Offset(0, 25, "desc3", AnimalScript.DIRECTION_NW),
				"in der vorgegebenen Grammatik uebergeben", "desc4", null,
				shownTextProperties);
		lang.newText(new Offset(0, 25, "desc4", AnimalScript.DIRECTION_NW),
				"Dadurch hat man auf einer 2D-Landschaft", "desc5", null,
				shownTextProperties);
		lang.newText(new Offset(0, 25, "desc5", AnimalScript.DIRECTION_NW),
				"keine identisch aussehenden Baeume stehen.", "desc6", null,
				shownTextProperties);
		lang.newText(new Offset(0, 25, "desc6", AnimalScript.DIRECTION_NW),
				"Man kann die Grammatik auch um die dritte Dimension", "desc7",
				null, shownTextProperties);
		lang.newText(new Offset(0, 25, "desc7", AnimalScript.DIRECTION_NW),
				"erweitern. Dazu muss die Grammatik eine weitere", "desc8",
				null, shownTextProperties);
		lang.newText(new Offset(0, 25, "desc8", AnimalScript.DIRECTION_NW),
				"'Rotation' um die 'z-Achse' erkennen und darstellen koennen.",
				"desc9", null, shownTextProperties);
		lang.newText(
				new Offset(0, 25, "desc9", AnimalScript.DIRECTION_NW),
				"Die Neigung der Aeste kann ebenfalls zufaellig gewaehlt werden.",
				"desc10", null, shownTextProperties);
	}

	private void lindenmayer() {
		defaultProperties();
		// 5
		src.highlight(1);
		Text templang = lang.newText(new Coordinates(75, 480), "F", "t1", null,
				shownTextProperties);
		lang.newPolyline(new Node[] { new Coordinates(150, 470),
				new Coordinates(150, 420) }, "f1", null, currentPolyline);

		Text currentSight = lang
				.newText(new Coordinates(450, 490),
						"Aktuelle Richtung des Pfeils", "cs", null,
						shownTextProperties);
		// N
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null,
				currentDirectionOfPolyline);
		lang.newCircle(new Coordinates(150, 420), 5, "currentPosition1", null,
				currentCirclePosition); // 1

		// 6
		lang.nextStep();
		lang.newPolyline(new Node[] { new Coordinates(150, 470),
				new Coordinates(150, 420) }, "f1", null, drawnPolyline);
		src.unhighlight(1);
		templang.setText("F[", null, null);
		src.highlight(4);

		lang.newCircle(new Coordinates(150, 420), 5, "currentPosition1", null,
				cpDel); // 1
		lang.newCircle(new Coordinates(150, 420), 5, "savedPosition1", null,
				savedCirclePosition); // 2

		// 7
		lang.nextStep();
		templang.setText("F[+", null, null);
		src.highlight(2);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null, cdopDel);
		// NW
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(465, 435) }, "c1", null,
				currentDirectionOfPolyline);

		// 8
		lang.nextStep();
		templang.setText("F[+F", null, null);
		src.unhighlight(2);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(115, 385) }, "f2", null, currentPolyline);
		lang.newCircle(new Coordinates(115, 385), 5, "currentPosition2", null,
				currentCirclePosition); // 3

		// 9
		lang.nextStep();
		templang.setText("F[+F]", null, null);
		src.unhighlight(1);
		src.unhighlight(4);
		src.highlight(5);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(115, 385) }, "f2", null, drawnPolyline);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(465, 435) }, "c1", null, cdopDel);
		// N
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null,
				currentDirectionOfPolyline);
		lang.newCircle(new Coordinates(115, 385), 5, "currentPosition2", null,
				cpDel); // 3
		lang.newCircle(new Coordinates(150, 420), 5, "savedPosition1", null,
				cpDel); // 2
		lang.newCircle(new Coordinates(150, 420), 5, "currentPosition3", null,
				currentCirclePosition); // 4

		// 10
		lang.nextStep();
		src.unhighlight(5);
		src.highlight(4);
		templang.setText("F[+F][", null, null);
		lang.newCircle(new Coordinates(150, 420), 5, "currentPosition3", null,
				cpDel); // 4
		lang.newCircle(new Coordinates(150, 420), 5, "savedPosition2", null,
				savedCirclePosition); // 5

		// 11
		lang.nextStep();
		src.highlight(3);
		templang.setText("F[+F][-", null, null);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null, cdopDel);
		// NE
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null,
				currentDirectionOfPolyline);

		// 12
		lang.nextStep();
		templang.setText("F[+F][-F", null, null);
		src.unhighlight(3);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(185, 385) }, "f3", null, currentPolyline);
		lang.newCircle(new Coordinates(185, 385), 5, "currentPosition4", null,
				currentCirclePosition); // 6

		// 13
		lang.nextStep();
		templang.setText("F[+F][-F[", null, null);
		src.unhighlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(185, 385) }, "f3", null, drawnPolyline);
		lang.newCircle(new Coordinates(185, 385), 5, "currentPosition4", null,
				cpDel); // 6
		lang.newCircle(new Coordinates(185, 385), 5, "savedPosition3", null,
				savedCirclePosition); // 7

		// 14
		lang.nextStep();
		src.highlight(3);
		templang.setText("F[+F][-F[-", null, null);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null, cdopDel);
		// E
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(540, 470) }, "c1", null,
				currentDirectionOfPolyline);

		// 15
		lang.nextStep();
		templang.setText("F[+F][-F[-F", null, null);
		src.unhighlight(3);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(185, 385),
				new Coordinates(225, 385) }, "f4", null, currentPolyline);
		lang.newCircle(new Coordinates(225, 385), 5, "currentPosition5", null,
				currentCirclePosition); // 8

		// 16
		lang.nextStep();
		templang.setText("F[+F][-F[-F]", null, null);
		src.unhighlight(1);
		src.unhighlight(4);
		src.highlight(5);
		lang.newPolyline(new Node[] { new Coordinates(185, 385),
				new Coordinates(225, 385) }, "f4", null, drawnPolyline);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(540, 470) }, "c1", null, cdopDel);
		// NE
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null,
				currentDirectionOfPolyline);
		lang.newCircle(new Coordinates(225, 385), 5, "currentPosition5", null,
				cpDel); // 8
		lang.newCircle(new Coordinates(185, 385), 5, "currentPosition6", null,
				currentCirclePosition); // 9

		// 17
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F", null, null);
		src.unhighlight(5);
		src.highlight(1);
		src.highlight(4);
		lang.newPolyline(new Node[] { new Coordinates(185, 385),
				new Coordinates(215, 350) }, "f5", null, currentPolyline);
		lang.newCircle(new Coordinates(185, 385), 5, "currentPosition6", null,
				cpDel); // 9
		lang.newCircle(new Coordinates(215, 350), 5, "currentPosition7", null,
				currentCirclePosition); // 10

		// 18
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]", null, null);
		src.unhighlight(1);
		src.unhighlight(4);
		src.highlight(5);
		lang.newPolyline(new Node[] { new Coordinates(185, 385),
				new Coordinates(215, 350) }, "f5", null, drawnPolyline);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null, cdopDel);
		// N
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null,
				currentDirectionOfPolyline);
		lang.newCircle(new Coordinates(215, 350), 5, "currentPosition7", null,
				cpDel); // 10
		lang.newCircle(new Coordinates(150, 420), 5, "savedPosition2", null,
				cpDel); // 5
		lang.newCircle(new Coordinates(150, 420), 5, "currentPosition8", null,
				currentCirclePosition); // 11

		// 19
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F", null, null);
		src.unhighlight(5);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(150, 370) }, "f6", null, currentPolyline);
		lang.newCircle(new Coordinates(150, 420), 5, "currentPosition8", null,
				cpDel); // 11
		lang.newCircle(new Coordinates(150, 370), 5, "currentPosition9", null,
				currentCirclePosition); // 12

		// 20
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[", null, null);
		src.unhighlight(1);
		src.highlight(4);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(150, 370) }, "f6", null, drawnPolyline);
		lang.newCircle(new Coordinates(150, 370), 5, "currentPosition9", null,
				cpDel); // 12
		lang.newCircle(new Coordinates(150, 370), 5, "savedPosition4", null,
				savedCirclePosition); // 13

		// 21
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+", null, null);
		src.unhighlight(1);
		src.highlight(2);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null, cdopDel);
		// NW
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(465, 435) }, "c1", null,
				currentDirectionOfPolyline);

		// 22
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F", null, null);
		src.unhighlight(2);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 370),
				new Coordinates(115, 335) }, "f7", null, currentPolyline);
		lang.newCircle(new Coordinates(115, 335), 5, "currentPosition10", null,
				currentCirclePosition); // 14

		// 23
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F]", null, null);
		src.unhighlight(4);
		src.unhighlight(1);
		src.highlight(5);
		lang.newPolyline(new Node[] { new Coordinates(150, 370),
				new Coordinates(115, 335) }, "f7", null, drawnPolyline);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(465, 435) }, "c1", null, cdopDel);
		// N
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null,
				currentDirectionOfPolyline);
		lang.newCircle(new Coordinates(115, 335), 5, "currentPosition10", null,
				cpDel); // 14
		lang.newCircle(new Coordinates(150, 370), 5, "savedPosition4", null,
				cpDel); // 13
		lang.newCircle(new Coordinates(150, 370), 5, "currentPosition11", null,
				currentCirclePosition); // 15

		// 24
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F][", null, null);
		src.unhighlight(5);
		src.highlight(4);
		lang.newCircle(new Coordinates(150, 370), 5, "savedPosition5", null,
				savedCirclePosition); // 15->16

		// 25
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F][-", null, null);
		src.highlight(3);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null, cdopDel);
		// NE
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null,
				currentDirectionOfPolyline);

		// 26
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F][-F", null, null);
		src.unhighlight(3);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 370),
				new Coordinates(185, 335) }, "f8", null, currentPolyline);
		lang.newCircle(new Coordinates(185, 335), 5, "currentPosition12", null,
				currentCirclePosition); // 17

		// 27
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F][-F]", null, null);
		src.unhighlight(4);
		src.unhighlight(1);
		src.highlight(5);
		lang.newPolyline(new Node[] { new Coordinates(150, 370),
				new Coordinates(185, 335) }, "f8", null, drawnPolyline);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null, cdopDel);
		// N
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null,
				currentDirectionOfPolyline);
		lang.newCircle(new Coordinates(185, 335), 5, "currentPosition12", null,
				cpDel); // 17
		lang.newCircle(new Coordinates(150, 370), 5, "currentPosition13", null,
				currentCirclePosition); // 16->18
	}

	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height
		Language l = new AnimalScript(
				"Lindenmayer Algorithmus - L-Tree Animation", "Deniz Emre Mohr"
						+ '"' + '"' + "Murat Batu", 640, 480);
		CopyOfLindenmayerAPIGenerator lnd = new CopyOfLindenmayerAPIGenerator(l);
		lnd.start();
		System.out.println(l);
	}

}
