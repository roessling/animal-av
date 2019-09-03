/*
 * LindenmayerAPIGenerator.java
 * Deniz Emre Mohr, Murat Batu, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph.lindenmayer;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JOptionPane;

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
import algoanim.util.Offset;

public class LindenmayerAPIGenerator implements ValidatingGenerator {

	public static Language lang;
	public static SourceCodeProperties sourceCodeProperties;
	private RectProperties headerRectProperties;
	private TextProperties headerTextProperties;
	public static TextProperties shownTextProperties;
	public static PolylineProperties currentPolyline;
	public static PolylineProperties currentDirectionOfPolyline;
	public static CircleProperties currentCirclePosition;
	public static CircleProperties savedCirclePosition;
	public  static String regeln;
	public static int iteration;

	private PolylineProperties cdopDel;
	public static CircleProperties cpDel;
	public static PolylineProperties drawnPolyline;

	private Text header;
	private Rect hRect;
	public static SourceCode src;
	public static SourceCode rsrc;
	

	public String getDescription() {
		return "L-Trees werden bei Prozeduraler Content-Generierung benutzt."
				+ "\n" + "Sie basieren auf Lindenmayer's L-Systeme von 1974."
				+ "\n" + "L-Trees sind kontextfreie Grammatiken," + "\n"
				+ "die mit Hilfe von Strings dargestellt werden.";
	}

	public String getCodeExample() {
		return "Im Folgenden werden diese Regeln benutzt, um die Baeume zu generieren:"
				+ "\n"
				+ "F   |   Gehe ein Schritt vorwaerts"
				+ "\n"
				+ "+   |   Rotiere um 45 Grad nach links"
				+ "\n"
				+ "-   |   Rotiere um 45 Grad nach rechts"
				+ "\n"
				+ "[   |   Speichere die Position und Richtung des Turtles auf einem Stack"
				+ "\n"
				+ "]   |   Hole die Position und Richtung des Turtles aus dem Stack";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	private void start() {

		drawnPolyline = new PolylineProperties();
		drawnPolyline.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		drawnPolyline.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		// Folie 1
		// show the header with a heading surrounded by a rectangle
		header = lang.newText(new Coordinates(20, 30),
				"Lindenmayer-Systeme - L-Tree Animation", "header", null,
				headerTextProperties);

		hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, headerRectProperties);

		// setup the description page
		// Folie 2
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

		// Folie 3
		lang.nextStep("Erste Folie");
		lang.hideAllPrimitives();
		header.show();
		hRect.show();
		lang.newText(new Coordinates(20, 260),
				"Im Folgenden wird aus den eingegebenen Regeln",
				"rule0", null, shownTextProperties);
		lang.newText(new Offset(0, 25, "rule0", AnimalScript.DIRECTION_NW),
				"schrittweise der am Ende zu zeichnende String aufgebaut.",
				"rule1", null, shownTextProperties);
		rsrc = lang.newSourceCode(new Coordinates(20, 80), "ruleSource", null, sourceCodeProperties);
		int temp = 0;
		int rule = 1;
		for (int i = 0; i < regeln.length(); i++) {
			if (regeln.charAt(i) == ',') {
				if(temp == 0) {
					rsrc.addCodeLine("Regel: " + regeln.substring(0, i), "regel", 1, null);
					temp = i;
					rule++;
				}
				else if(temp != 0) {
					rsrc.addCodeLine("Regel: " + regeln.substring(temp+1, i), "regel"+rule, 1, null);
					rule++;
					temp = i;
				}
			}
		}
		rsrc.addCodeLine("Regel: " + regeln.substring(regeln.lastIndexOf(",")+1, regeln.length()), "regel"+rule, 1, null);
		
		ParserAnimal.parse(regeln, iteration, "S");
		String sresult = ParserAnimal.result;
		
		lang.newText(new Coordinates(20, 310),
				"Der zu zeichnende String: " + ParserAnimal.result, "result", null, shownTextProperties);
		
		lang.nextStep();
		
		// disable the first page(description) and show the
		// algorithm(sourcecode)
		lang.hideAllPrimitives();
		header.show();
		hRect.show();

		src = lang.newSourceCode(new Coordinates(20, 80), "ltreeSource", null,
				sourceCodeProperties);
		src.addCodeLine(
				"Im Folgenden werden diese Regeln benutzt, um den Baum zu generieren:",
				null, 0, null);
		src.addCodeLine("F   |   Gehe ein Schritt vorwaerts", "f", 0, null);
		src.addCodeLine("+   |   Rotiere um 45 Grad nach links", "+", 0, null);
		src.addCodeLine("-   |   Rotiere um 45 Grad nach rechts", "-", 0, null);
		src.addCodeLine("[   |   Speichere die Position und Richtung des Turtles auf einem Stack",
				"[", 0, null);
		src.addCodeLine("]   |   Hole die Position und Richtung des Turtles aus dem Stack",
				"]", 0, null);

		// show the String to be calculated
		//Folie 4
		lang.nextStep("Sourcecode");

		src.addCodeLine("", null, 0, null);
		src.addCodeLine("", null, 0, null);

		src.addCodeLine(
				"Wir generieren aus dem eben generierten String schrittweise einen Baum:",
				null, 0, null);
		src.addCodeLine(ParserAnimal.result, "bs", 0, null);

		// call the algorithm
		// zu Folie 5
		lang.nextStep("Baumgenerierung");
		//TODO
		LindenmayerDrawer.drawArrows(sresult);
//		ParserAnimal.result = "";
//		LindenmayerDrawer.ursprung = new Coordinates(900, 650);
//		LindenmayerDrawer.spitze = new Coordinates(900, 600);
//		LindenmayerDrawer.akoordX = LindenmayerDrawer.ursprung.getX();
//		LindenmayerDrawer.akoordY = LindenmayerDrawer.ursprung.getY();
//		LindenmayerDrawer.oldUrsprung = LindenmayerDrawer.ursprung;
//		LindenmayerDrawer.oldSpitze = LindenmayerDrawer.spitze;
//		ParserAnimal.open = 0;
//		ParserAnimal.close = 0;
		src.unhighlight("f");
		src.unhighlight("+");
		src.unhighlight("-");
		src.unhighlight("[");
		src.unhighlight("]");

		//Dieser Aufruf funktioniert unabhängig vom Parser mit den festegelegten Eigenwerten.
		//WARNUNG: Koordinaten sind anders. Graph wuerde sich mit dem Parsergraph oder anderen Textfeldern ueberschneiden.
	//	lindenmayer();

		// final text
		lang.nextStep("Letzte Folie");
		
		// Folie 28
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

	/*

	private void lindenmayer() {
		// Folie 5
		src.highlight(1);
		Text templang = lang.newText(new Coordinates(75, 480), "F", "t1", null,
				shownTextProperties);
		lang.newPolyline(new Node[] { new Coordinates(150, 470),
				new Coordinates(150, 420) }, "f1", null, currentPolyline);

		lang.newText(new Coordinates(450, 490), "Aktuelle Richtung des Pfeils",
				"cs", null, shownTextProperties);
		// N
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null,
				currentDirectionOfPolyline);
		lang.newCircle(new Coordinates(150, 420), 5, "currentPosition1", null,
				currentCirclePosition); // 1

		// Folie 6
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

		// Folie 7
		lang.nextStep();
		templang.setText("F[+", null, null);
		src.highlight(2);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null, cdopDel);
		// NW
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(465, 435) }, "c1", null,
				currentDirectionOfPolyline);

		// Folie 8
		lang.nextStep();
		templang.setText("F[+F", null, null);
		src.unhighlight(2);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(115, 385) }, "f2", null, currentPolyline);
		lang.newCircle(new Coordinates(115, 385), 5, "currentPosition2", null,
				currentCirclePosition); // 3

		// Folie 9
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

		// Folie 10
		lang.nextStep();
		src.unhighlight(5);
		src.highlight(4);
		templang.setText("F[+F][", null, null);
		lang.newCircle(new Coordinates(150, 420), 5, "currentPosition3", null,
				cpDel); // 4
		lang.newCircle(new Coordinates(150, 420), 5, "savedPosition2", null,
				savedCirclePosition); // 5

		// Folie 11
		lang.nextStep();
		src.highlight(3);
		templang.setText("F[+F][-", null, null);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null, cdopDel);
		// NE
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null,
				currentDirectionOfPolyline);

		// Folie 12
		lang.nextStep();
		templang.setText("F[+F][-F", null, null);
		src.unhighlight(3);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(185, 385) }, "f3", null, currentPolyline);
		lang.newCircle(new Coordinates(185, 385), 5, "currentPosition4", null,
				currentCirclePosition); // 6

		// Folie 13
		lang.nextStep();
		templang.setText("F[+F][-F[", null, null);
		src.unhighlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 420),
				new Coordinates(185, 385) }, "f3", null, drawnPolyline);
		lang.newCircle(new Coordinates(185, 385), 5, "currentPosition4", null,
				cpDel); // 6
		lang.newCircle(new Coordinates(185, 385), 5, "savedPosition3", null,
				savedCirclePosition); // 7

		// Folie 14
		lang.nextStep();
		src.highlight(3);
		templang.setText("F[+F][-F[-", null, null);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null, cdopDel);
		// E
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(540, 470) }, "c1", null,
				currentDirectionOfPolyline);

		// Folie 15
		lang.nextStep();
		templang.setText("F[+F][-F[-F", null, null);
		src.unhighlight(3);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(185, 385),
				new Coordinates(225, 385) }, "f4", null, currentPolyline);
		lang.newCircle(new Coordinates(225, 385), 5, "currentPosition5", null,
				currentCirclePosition); // 8

		// Folie 16
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

		// Folie 17
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

		// Folie 18
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

		// Folie 19
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

		// Folie 20
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

		// Folie 21
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

		// Folie 22
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F", null, null);
		src.unhighlight(2);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 370),
				new Coordinates(115, 335) }, "f7", null, currentPolyline);
		lang.newCircle(new Coordinates(115, 335), 5, "currentPosition10", null,
				currentCirclePosition); // 14

		// Folie 23
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

		// Folie 24
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F][", null, null);
		src.unhighlight(5);
		src.highlight(4);
		lang.newCircle(new Coordinates(150, 370), 5, "savedPosition5", null,
				savedCirclePosition); // 15->16

		// Folie 25
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F][-", null, null);
		src.highlight(3);

		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(500, 420) }, "c1", null, cdopDel);
		// NE
		lang.newPolyline(new Node[] { new Coordinates(500, 470),
				new Coordinates(535, 435) }, "c1", null,
				currentDirectionOfPolyline);

		// Folie 26
		lang.nextStep();
		templang.setText("F[+F][-F[-F]F]F[+F][-F", null, null);
		src.unhighlight(3);
		src.highlight(1);
		lang.newPolyline(new Node[] { new Coordinates(150, 370),
				new Coordinates(185, 335) }, "f8", null, currentPolyline);
		lang.newCircle(new Coordinates(185, 335), 5, "currentPosition12", null,
				currentCirclePosition); // 17

		// Folie 27
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
*/
//	public static void main(String[] args) {
//		// Create a new animation
//		// name, author, screen width, screen height
//		Language l = new AnimalScript("Lindenmayer", "Deniz Emre Mohr" + '"'
//				+ '"' + "Murat Batu", 1280, 720);
//		CopyOfLindenmayerAPIGenerator lnd = new CopyOfLindenmayerAPIGenerator(l);
//		lnd.start();
//		System.out.println(l);
//	}

	@Override
	public void init() {
		lang = new AnimalScript("Lindenmayer System - L-Tree Animation",
				"Deniz Emre Mohr, Murat Batu", 1280, 720);
		ParserAnimal.result = "";
		LindenmayerDrawer.ursprung = new Coordinates(900, 650);
		LindenmayerDrawer.spitze = new Coordinates(900, 600);
		LindenmayerDrawer.akoordX = LindenmayerDrawer.ursprung.getX();
		LindenmayerDrawer.akoordY = LindenmayerDrawer.ursprung.getY();
		LindenmayerDrawer.oldUrsprung = LindenmayerDrawer.ursprung;
		LindenmayerDrawer.oldSpitze = LindenmayerDrawer.spitze;
		ParserAnimal.open = 0;
		ParserAnimal.close = 0;
	}
	
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// in Animal sichtbar und veraenderbar
		init();
		sourceCodeProperties = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties");
		headerRectProperties = (RectProperties) props.getPropertiesByName("headerRectProperties");
		headerTextProperties = (TextProperties) props.getPropertiesByName("headerTextProperties");
		shownTextProperties = (TextProperties) props.getPropertiesByName("shownTextProperties");
		currentPolyline = (PolylineProperties) props.getPropertiesByName("currentPolyline");
		currentDirectionOfPolyline = (PolylineProperties) props.getPropertiesByName("currentDirectionOfPolyline");
		currentCirclePosition = (CircleProperties) props.getPropertiesByName("currentCirclePosition");
		savedCirclePosition = (CircleProperties) props.getPropertiesByName("savedCirclePosition");
		regeln = (String) primitives.get("regeln");
		iteration = (Integer) primitives.get("iteration");
		// in Animal nicht sichtbar
		cdopDel = new PolylineProperties();
		cdopDel.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		cdopDel.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		cpDel = new CircleProperties();
		cpDel.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		cpDel.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		cpDel.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cpDel.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		drawnPolyline = new PolylineProperties();
		drawnPolyline.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		drawnPolyline.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		
		lang.setStepMode(true);
		
		start();
		//lang.finalizeGeneration();
		return lang.toString();
	}

	
	
	public String getName() {
		return "Lindenmayer System - L-Tree Animation";
	}

	public String getAlgorithmName() {
		return "Lindenmayer";
	}

	public String getAnimationAuthor() {
		return "Deniz Emre Mohr, Murat Batu";
	}
	
	
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
//		// TODO Auto-generated method stub
//	 try
//	 ParserAnimal.parse(productionsString, iterations, startSymbol)
//	 
//	 catch
//	 exception
//	 
//	 return false
//		try{
//			ParserAnimal.parse(regeln, iteration, "S");
//		}
//		catch(IllegalArgumentException e){
//			String errorMessage = (regeln + " is not a valid rule. "); 
//			errorMessage = errorMessage.concat("Please correct your Rule coordinate and use only Letters from 'A-Z' and no Symbols and Numbers.");
//			showErrorWindow(errorMessage);
//			return false;
//		}
//		generate(props, primitives);
	return true;
	}

	public static void showErrorWindow(String message) {
    	//System.out.println("Aufruf showErrorWindow mit message: " + message);
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, "Fehler", JOptionPane.ERROR_MESSAGE);
	}

	
}
