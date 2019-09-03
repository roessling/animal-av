package generators.cryptography;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.OffsetCoords;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;


public class NeedhamSchroederAsym implements Generator {

	//
	// Variablen 								
	//

	// Language
	static Language lang;

	// Properties
	private CircleProperties circleprops = new CircleProperties();
	private RectProperties rectprops = new RectProperties();
	private TextProperties textprops = new TextProperties();
	private TriangleProperties triangleprops = new TriangleProperties();
	SourceCodeProperties sourceprops = new SourceCodeProperties();

	// Schriftart
	private String globalfont;
	private Font gfont;

	// Kreise der Teilnehmer
	private Circle A;
	private Circle B;
//	private Circle AS;

	// Namen der Teilnehmer
	private String nA;
	private String nB;
	private String nAS;

	// Farben der Teilnehmer
	private Color cA;
	private Color cB;
	private Color cAS;

	// Farben der Schlüssel
	private Color kAS;
	private Color kA;
	private Color kB;
	
	// Mit optionalem Teil
	private boolean challenge;
	private String challengeA;
	private String challengeB;

	// 
	// Konstruktor, Init() & Generate 		
	// 

	public NeedhamSchroederAsym() {
		init();
		// zum Testen

		nAS = "T";
		nA = "C";
		nB = "D";

		cAS = Color.GRAY;
		cA = Color.LIGHT_GRAY;
		cB = Color.LIGHT_GRAY;

		kA = Color.MAGENTA;
		kB = Color.ORANGE;
		kAS = Color.GREEN;

		globalfont = Font.SANS_SERIF;
		
        challengeA = "Wie alt sind Sie?";
        challengeB = "Ich bin 23 Jahre alt";
        challenge = true;

		//
		protokoll();
	}

	public void init() {
		lang = new AnimalScript("Needham-Schroeder (Asymmetrisch) [DE]",
				"Florian Oswald", 800, 600);
		lang.setStepMode(true);	
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		init();
		
		kA = (Color)primitives.get("Keys Alice");
		kB = (Color)primitives.get("Keys Bob");
		kAS = (Color)primitives.get("Keys T");
		cB = (Color)primitives.get("Color Bob");
        cA = (Color)primitives.get("Color Alice");
        cAS = (Color)primitives.get("Color T");
        nAS = (String)primitives.get("Name T");
        nA = (String)primitives.get("Name Alice");
        nB = (String)primitives.get("Name Bob");
        challenge = (Boolean)primitives.get("Challenge Response");
        challengeA = (String)primitives.get("Question");
        challengeB = (String)primitives.get("Answer");
        gfont = (Font)primitives.get("Font");
        globalfont = gfont.getFontName();
        
		protokoll();
		
		lang.finalizeGeneration();
		return lang.toString();
	}

	//
	// Auto-generated Methods 
	// 
    public String getName() {
        return "Needham-Schroeder (Asymmetrisch) [DE]";
    }

    public String getAlgorithmName() {
        return "Needham-Schroeder";
    }

    public String getAnimationAuthor() {
        return "Florian Oswald";
    }

    public String getDescription(){
        return "Das Needham-Schroeder Protokoll dient, in der hier vorgestellten asymmetrische Variante, zur Authentifizierung zweier Parteien. Dabei kann es zum Beispiel zum Austausch von einem gemeinsamen Sitzungsschluuml;ssel genutzt werden."
 +"\n"
 +"Das Protokoll wurde 1978 von Roger Needham und Michael Schroeder am MIT entwickelt. Die hier vorgestellte Variante erm&ouml;glicht es zwei Parteien sich gegenseitig zu authentifizieren, sodass jede Partei sich sicher sein kann, "
 +"\n"
 +"dass der gegen&uuml; ber der ist f&uuml;r den man ihn h&auml;lt. Hierf&uuml; r werden asymmetrische Krytoverfahren verwendet und eine dritte vertrauenw&uuml; rdige Partei, welche die &ouml;ffentlichen Schl&uuml;ssel der beiden Parteien besitzt."
 +"\n"
 +"\n"
 +"Vorrausgesetzt wird ein Grundverst&auml;ndnis f&uuml;r Kryptographische Verfahren, wie zum Beispiel die asymmetrische Verschl&uuml;sselung und digitale Signaturen."
 +"\n"
 +"\n"
 +"\n";
    }

    public String getCodeExample(){
        return "(1) A  sendet  T  - A,B"
 +"\n"
 +"(2) T  sendet  A  - B public[B] signiert von T"
 +"\n"
 +"(3) A  sendet  B  - A, I(A) verschlüsselt mit public[B]"
 +"\n"
 +"(4) B  sendet  T  - B,A"
 +"\n"
 +"(5) T  sendet  B  - A public[A] signiert von T"
 +"\n"
 +"(6) B  sendet  A  - I(B)"
 +"\n"
 +"(7) A  sendet  B  - I(B) - 1";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	// 
	// eigene Methoden für die Animation 
	// 

	public void createA(int x, int y) {
		circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cA);
		circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		A = lang.newCircle(new Coordinates(x, y), 20, "A", null, circleprops);
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		lang.newText(new Offset(-5, 10, "A", AnimalScript.DIRECTION_N), nA,
				"Text_A", null, textprops);
	}

	public void createB(int x, int y) {
		circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cB);
		circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		B = lang.newCircle(new Coordinates(x, y), 20, "B", null, circleprops);
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		lang.newText(new Offset(-5, 10, "B", AnimalScript.DIRECTION_N), nB,
				"Text_B", null, textprops);
	}

	public void createAS(int x, int y) {
		circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cAS);
		circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		lang.newCircle(new Coordinates(x, y), 20, "AS", null, circleprops);
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		lang.newText(new Offset(-10, 10, "AS", AnimalScript.DIRECTION_N), nAS,
				"Text_AS", null, textprops);
	}

	public void createKA(String at) {

		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kA);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		if (at.equals("A")) {
			lang.newText(new Offset(-45, 0, "A", AnimalScript.DIRECTION_W),
					"K[" + nA + "]", "Text_A_KA", null, textprops);
			lang.newRect(new Offset(-5, -5, "Text_A_KA",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_A_KA",
					AnimalScript.DIRECTION_SE), "Text_A_KA_R", null, rectprops);
		} else if (at.equals("B")) {
			Text tempB = lang.newText(new Offset(0, 30, "Text_B_PB", AnimalScript.DIRECTION_SW), "K["
					+ nA + "]", "Text_B_KA", null, textprops);
			tempB.hide();
			tempB.show(new TicksTiming(600));
			Rect tempBr = lang.newRect(new Offset(-5, -5, "Text_B_KA",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_B_KA",
					AnimalScript.DIRECTION_SE), "Text_B_KA_R", null, rectprops);
			tempBr.hide();
			tempBr.show(new TicksTiming(600));
		} else {
			lang.newText(new Offset(-45, 0, "AS", AnimalScript.DIRECTION_W),
					"K[" + nA + "]", "Text_AS_KA", null, textprops);
			lang.newRect(new Offset(-5, -5, "Text_AS_KA",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_AS_KA",
					AnimalScript.DIRECTION_SE), "Text_AS_KA_R", null, rectprops);
		}
	}

	public void createKB(String at) {

		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kB);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		if (at.equals("A")) {
			Text tempA = lang.newText(new Offset(0, 30, "Text_A_PA", AnimalScript.DIRECTION_SW), "K["
					+ nB + "]", "Text_A_KB", null, textprops);
			tempA.hide();
			tempA.show(new TicksTiming(600));
			Rect tempAr =lang.newRect(new Offset(-5, -5, "Text_A_KB",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_A_KB",
					AnimalScript.DIRECTION_SE), "Text_A_KB_R", null, rectprops);
			tempAr.hide();
			tempAr.show(new TicksTiming(600));
		} else if (at.equals("B")) {
			lang.newText(new Offset(10, 0, "B", AnimalScript.DIRECTION_E), "K["
					+ nB + "]", "Text_B_KB", null, textprops);
			lang.newRect(new Offset(-5, -5, "Text_B_KB",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_B_KB",
					AnimalScript.DIRECTION_SE), "Text_B_KB_R", null, rectprops);
		} else {
			lang.newText(new Offset(10, 0, "AS", AnimalScript.DIRECTION_E),
					"K[" + nB + "]", "Text_AS_KB", null, textprops);
			lang.newRect(new Offset(-5, -5, "Text_AS_KB",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_AS_KB",
					AnimalScript.DIRECTION_SE), "Text_AS_KB_R", null, rectprops);
		}
	}

	public void createKAS(String at) {

		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kAS);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		if (at.equals("A")) {
			lang.newText(new Offset(0, -40, "A", AnimalScript.DIRECTION_NW),
					"K[" + nAS + "]", "Text_A_KAS", null, textprops);
			lang.newRect(new Offset(-5, -5, "Text_A_KAS",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_A_KAS",
					AnimalScript.DIRECTION_SE), "Text_A_KAS_R", null, rectprops);
		} else if (at.equals("B")) {
			lang.newText(new Offset(0, -40, "B", AnimalScript.DIRECTION_NW),
					"K[" + nAS + "]", "Text_B_KAS", null, textprops);
			lang.newRect(new Offset(-5, -5, "Text_B_KAS",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_B_KAS",
					AnimalScript.DIRECTION_SE), "Text_B_KAS_R", null, rectprops);
		} else {
			lang.newText(new Offset(0, 30, "AS", AnimalScript.DIRECTION_SW),
					"K[" + nAS + "]", "Text_AS_KAS", null, textprops);
			lang.newRect(new Offset(-5, -5, "Text_AS_KAS",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "Text_AS_KAS",
					AnimalScript.DIRECTION_SE), "Text_AS_KAS_R", null,
					rectprops);
		}
	}

	public void createPrivateK(String at) {

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		if (at.equals("A")) {
			triangleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kA);
			triangleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			triangleprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);			

			lang.newText(new Offset(0, 30, "Text_A_KA",
					AnimalScript.DIRECTION_SW), "K[" + nA + "]", "Text_A_PA",
					null, textprops);
			
			lang.newTriangle(new Offset(-10, 0, "Text_A_PA",
					AnimalScript.DIRECTION_SW), new Offset(10, 0, "Text_A_PA",
					AnimalScript.DIRECTION_SE), new Offset(0, -15, "Text_A_PA",
					AnimalScript.DIRECTION_N), "Text_A_PA_R", null,
					triangleprops);


		} else if (at.equals("B")) {
			triangleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kB);
			triangleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			triangleprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);			

			lang.newText(new Offset(0, 30, "Text_B_KB",
					AnimalScript.DIRECTION_SW), "K[" + nB + "]", "Text_B_PB",
					null, textprops);
			
			lang.newTriangle(new Offset(-10, 0, "Text_B_PB",
					AnimalScript.DIRECTION_SW), new Offset(10, 0, "Text_B_PB",
					AnimalScript.DIRECTION_SE), new Offset(0, -15, "Text_B_PB",
					AnimalScript.DIRECTION_N), "Text_B_PB_R", null,
					triangleprops);
		} else {
			triangleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kAS);
			triangleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			triangleprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);			

			lang.newText(new Offset(0, 30, "Text_AS_KAS",
					AnimalScript.DIRECTION_SW), "K[" + nAS + "]", "Text_AS_PAS",
					null, textprops);
			
			lang.newTriangle(new Offset(-10, 0, "Text_AS_PAS",
					AnimalScript.DIRECTION_SW), new Offset(10, 0, "Text_AS_PAS",
					AnimalScript.DIRECTION_SE), new Offset(0, -15, "Text_AS_PAS",
					AnimalScript.DIRECTION_N), "Text_AS_PAS_R", null,
					triangleprops);
		}
	}

	public void placeCirclesAB() {
		createA(100, 400);
		createB(900, 400);
	}

	public void placeCircleAS() {
		createAS(500, 700);
	}

	// /////////////////////////////////////////////
	// / eigentliche Animation protokoll() ///
	// /////////////////////////////////////////////

	public void protokoll() {

		// Schritt 1
		// Ueberschrift
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 20));
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newText(new Coordinates(20, 30),
				"Needham-Schroeder (Asymmetrisch)", "header", null, textprops);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", AnimalScript.DIRECTION_SE),
				"headerR", null, rectprops);

		// Schritt 2

		lang.nextStep("Das Needham-Schroeder Protokoll");
		
		Text definition_1;
		Text definition_2;
		Text definition_3;

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		definition_1 = lang.newText(new Coordinates(20, 75),
				"Das Needham-Schroeder Protokoll dient dazu die zwei Teilnehmer "
						+ nA + " und " + nB
						+ " sich gegenseitig zu authentisieren.",
				"definition_1", null, textprops);
		definition_2 = lang
				.newText(
						new Offset(0, 10, "definition_1",
								AnimalScript.DIRECTION_SW),
						"Ziel des Protokolls ist es, dass nach gewissen Protokollsschritten, dass beide Parteien sich sicher sein koennen,",
						"definition_2", null, textprops);
		definition_3 = lang
				.newText(
						new Offset(0, 10, "definition_2",
								AnimalScript.DIRECTION_SW),
						"mit dem richtigen Gespraechspartner sicher kommunizieren zu koennen.",
						"definition_3", null, textprops);

		placeCirclesAB();

		Node[] Line = new Node[2];
		Line[0] = new OffsetCoords(A.getCenter(), 30, 0);
		Line[1] = new OffsetCoords(B.getCenter(), -30, 0);

		Polyline verbindung = lang.newPolyline(Line, "verbindung", null);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		Text verbindung_text = lang.newText(new Coordinates(400, 380),
				"Vertrauenwuerdige Verbindung", "verbindung_Text", null,
				textprops);

		// Schritt 3

		lang.nextStep("Schluesselverteilung");
		
		MultipleChoiceQuestionModel mc_3 = new MultipleChoiceQuestionModel("Frage_3");
		
		mc_3.setPrompt("Wie viele Schluessel besitzt ein asymmetrisches Schluesselpaar?");
		mc_3.addAnswer("2", 5, "Richtig");
		mc_3.addAnswer("1", 0, "Falsch");
		mc_3.addAnswer("0", 0, "Falsch");
		lang.addMCQuestion(mc_3);

		definition_1.hide();
		definition_2.hide();
		definition_3.hide();
		verbindung.hide();
		verbindung_text.hide();

		Text definition_4;
		Text definition_5;
		Text definition_6;
		Text definition_7;

		Text link_1;

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		definition_4 = lang
				.newText(
						new Coordinates(20, 75),
						"Die Nachrichten koennen nach dem das Vertrauen aufgebaut ist, mit den jeweiligen oeffentlichen Schluesseln",
						"definition_4", null, textprops);
		definition_5 = lang.newText(new Offset(0, 10, "definition_4",
				AnimalScript.DIRECTION_SW),
				"der Gespraechspartner verschluesselt ausgetauscht werden. "
						+ nA + " mit dem oeffentlichen Schluessel von " + nB
						+ " ,", "definition_5", null, textprops);
		definition_6 = lang.newText(new Offset(0, 10, "definition_5",
				AnimalScript.DIRECTION_SW), nA
				+ " mit dem oeffentlichen Schluessel von " + nB
				+ ". Dabei besitzen die Parteien den oeffentlichen",
				"definition_6", null, textprops);
		definition_7 = lang.newText(new Offset(0, 10, "definition_6",
				AnimalScript.DIRECTION_SW),
				"Schluessel des gegenueber am Anfang nicht.", "definition_7",
				null, textprops);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.ITALIC, 16));

		link_1 = lang
				.newText(
						new Offset(0, 30, "definition_7",
								AnimalScript.DIRECTION_SW),
						"Weitere Infos: http://de.wikipedia.org/wiki/Asymmetrisches_Kryptosystem",
						"link_1", null, textprops);

		// Schritt 4

		lang.nextStep("Die dritte Partei " + nAS);

		definition_4.hide();
		definition_5.hide();
		definition_6.hide();
		definition_7.hide();
		link_1.hide();

		Text definition_8;
		Text definition_9;
		Text definition_10;

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		definition_8 = lang
				.newText(
						new Coordinates(20, 75),
						"Damit das Protokoll funktionieren kann, benoetigt man eine dritte vertrauenwuerdige Partei "
								+ nAS + ".", "definition_8", null, textprops);
		definition_9 = lang.newText(new Offset(0, 10, "definition_8",
				AnimalScript.DIRECTION_SW), nAS
				+ " besitzt die oeffentlichen Schluessel von " + nA + " und "
				+ nB + ". Die restlichen Teilnehmer im Protokoll",
				"definition_9", null, textprops);
		definition_10 = lang.newText(new Offset(0, 10, "definition_9",
				AnimalScript.DIRECTION_SW),
				"besitzen den oeffentlichen Schluessel von " + nAS + ".",
				"definition_10", null, textprops);

		placeCircleAS();

		// äffentliche Schlüssel setzen

		lang.nextStep("Public Key Verteilung");

		createKA("AS");

		createKB("AS");

		createKAS("A");
		createKAS("B");
		createKAS("AS");

		// Keys

		// Schritt 5
		// Text + Beispiel Verschlüsselung

		lang.nextStep();

		definition_8.hide();
		definition_9.hide();
		definition_10.hide();

		Text definition_11;
		Text definition_12;
		Text definition_13;
		Text hinweis_1;

		Text link_2;

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		definition_11 = lang
				.newText(
						new Coordinates(20, 75),
						"Zusaetzlich zu der Verteilung der oeffentlichen Schluessel besitzen die Teilnehmer jeweils ihr eigenes asymmetrisches",
						"definition_11", null, textprops);
		definition_12 = lang
				.newText(
						new Offset(0, 10, "definition_11",
								AnimalScript.DIRECTION_SW),
						"Schluesselpaar. Nun besitzen alle Teilnehmer die richtigen Schluessel.",
						"definition_12", null, textprops);
		definition_13 = lang
				.newText(
						new Offset(0, 10, "definition_12",
								AnimalScript.DIRECTION_SW),
						"Mit diesen Schluessel koennen nun Nachrichten verschluesselt uebertragen werden und signiert werden.",
						"definition_13", null, textprops);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		textprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		
		hinweis_1 = lang
				.newText(
						new Offset(0, 10, "definition_13",
								AnimalScript.DIRECTION_SW),
						"Die aktuelle Verschluesselung bzw. Signatur wird in einem extra Fenster angegeben (Statusfenster).",
						"hinweis_1", null, textprops);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.ITALIC, 16));
		
		textprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);

		link_1.show();
		
		link_2 = lang
				.newText(
						new Offset(0, 10, "link_1", AnimalScript.DIRECTION_SW),
						"Weitere Infos: http://de.wikipedia.org/wiki/Digitale_Signatur",
						"link_2", null, textprops);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
	
		Text oeffentlich = lang.newText(new Coordinates(350, 380),
				"Oeffentliche Teil des Schluesselpaars", "oeffentlich", null,
				textprops);

		createKA("A");
		createKB("B");

		// Private Schluessel setzen
		lang.nextStep("Private Key Verteilung");

		oeffentlich.hide();

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		Text privater = lang.newText(new Coordinates(350, 380),
				"Privater Teil des Schluesselpaars", "privater", null,
				textprops);

		createPrivateK("A");
		createPrivateK("B");
		createPrivateK("AS");
		
		// Frage: 
		MultipleChoiceQuestionModel mc_1 = new MultipleChoiceQuestionModel("Frage_1");
		
		mc_1.setPrompt("Mit welchem Schluessel verschluesselt man einer Asymmetrischen Kommunikation?");
		mc_1.addAnswer("(Symmetrischer) Session Key", 0, "Es handelt sich um eine Asymmetrische Kommunikation. Antwort 2 ist richtig");
		mc_1.addAnswer("Private Key", 5, "Richtig");
		mc_1.addAnswer("Public Key", 0, "Vielleicht sollten Sie sich das ganze nochmal anschauen: http://de.wikipedia.org/wiki/Asymmetrisches_Kryptosystem");
		lang.addMCQuestion(mc_1);

		// Schritt 6

		lang.nextStep("Zusammenfassung der Informationen");

		privater.hide();
		definition_11.hide();
		definition_12.hide();
		definition_13.hide();
		hinweis_1.hide();
		link_2.hide();
		link_1.hide();
		
		Text definition_14;
		Text definition_15;
		Text definition_16;
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 18));
		textprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		
		definition_14 = lang
				.newText(
						new Coordinates(20, 75),
						"WIEDERHOLUNG:",
						"definition_14", null, textprops);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));
		textprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		definition_15 = lang
				.newText(
						new Offset(0, 10, "definition_14",
								AnimalScript.DIRECTION_SW),
						"Oeffentlicher Schluessel: Verschluesseln und Verifizieren",
						"definition_15", null, textprops);
		definition_16 = lang
				.newText(
						new Offset(0, 10, "definition_15",
								AnimalScript.DIRECTION_SW),
						"Privater Schluessel: Entschluesseln und Signieren",
						"definition_16", null, textprops);

		// Text + Definitionen ausblenden + Tabelle einblenden

		lang.nextStep("Initiale Bedingungen vor dem Start");

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 18));
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Text protokoll = lang.newText(new Coordinates(20, 100),
				"Protokoll", "protokoll", null, textprops);
		Rect protokollr = lang.newRect(new Offset(-5, -5, "protokoll", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "protokoll", AnimalScript.DIRECTION_SE),
				"protokollr", null, rectprops);
		
		definition_14.hide();
		definition_15.hide();
		definition_16.hide();
		
		sourceprops.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		sourceprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(globalfont,Font.PLAIN, 16));
		sourceprops.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,Color.RED);   
		sourceprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		    
		SourceCode sc1 = lang.newSourceCode(new Coordinates(20, 135), "sourceCode",null, sourceprops);
	    
		sc1.addCodeLine(nA + "  sendet an " + nAS, null, 0, null);  
		sc1.addCodeLine(nAS + " sendet an " + nA, null, 0, null); 
		sc1.addCodeLine(nA + "  sendet an " + nB, null, 0, null);
		sc1.addCodeLine(nB + "  sendet an " + nAS, null, 0, null);  
		sc1.addCodeLine(nAS + " sendet an " + nB, null, 0, null);  
		sc1.addCodeLine(nB + "  sendet an " + nA, null, 0, null);  
		sc1.addCodeLine(nA + "  sendet an " + nB, null, 0, null); 
		
		SourceCode sc2 = lang.newSourceCode(new Coordinates(180, 135), "sourceCode",null, sourceprops);
	    
		sc2.addCodeLine(nA + " " + nB, null, 0, null);  
		sc2.addCodeLine("{ " + nB + " ,public[" + nB + "] }", null, 0, null); 
		sc2.addCodeLine("{ " + nA + " ,I("  + nA +  ") }", null, 0, null);
		sc2.addCodeLine(nB + " " + nA, null, 0, null); 
		sc2.addCodeLine("{ " + nA + " ,public[" + nA + "] } ", null, 0, null); 
		sc2.addCodeLine("{ I("  + nA +  ") ,I("  + nB +  ") }  ", null, 0, null); 
		sc2.addCodeLine("{ I("  + nB +  " - 1) } ", null, 0, null);  
		
		Text status = lang.newText(new Coordinates(450,300), "", "status", null, textprops);
		
		LinkedList<String> aktuellerStatus = new LinkedList<String>();
		aktuellerStatus.add("unverschluesselt");
		aktuellerStatus.add("signiert von "  + nAS);
		aktuellerStatus.add("verschluesselt mit public[" + nB + "]");
		aktuellerStatus.add("unverschluesselt");
		aktuellerStatus.add("signiert von " + nAS);
		aktuellerStatus.add("verschluesselt mit public[" + nA + "]");
		aktuellerStatus.add("verschluesselt mit public[" + nB + "]");
	
		
		
		// Schritt 7
		// Text + Ziel
		
		Text message_1;
		Text message_2;
		Text message_3;
		Text message_4;
		Text message_5;
		Text message_6;
		Text message_7;
		
		Rect message_1_r;
		Rect message_2_r;
		Rect message_3_r;
		Rect message_4_r;
		Rect message_5_r;
		Rect message_6_r;
		Rect message_7_r;

		// Schritt 8
		// Protokoll 1
		
		lang.nextStep("Step 1");
		sc1.highlight(0);
		sc2.highlight(0);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 14));
		
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		message_1 = lang.newText(new Offset(10, 10, "A", AnimalScript.DIRECTION_NE), nA + " " + nB, "message_1", null, textprops);
		lang.nextStep();
		status.setText(aktuellerStatus.get(0), null, null);
		message_1_r = lang.newRect(new Offset(-5, -5, "message_1", AnimalScript.DIRECTION_NW), new Offset(5, 5, "message_1", AnimalScript.DIRECTION_SE), "message_1r", null, rectprops);
		lang.nextStep();
		
		try {
			message_1.moveTo(AnimalScript.DIRECTION_SE, null, new Offset(-50, -30, "AS", AnimalScript.DIRECTION_NW), null, new TicksTiming(400));
			message_1_r.moveTo(AnimalScript.DIRECTION_SE, null, new Offset(-55, -35, "AS", AnimalScript.DIRECTION_NW), null, new TicksTiming(400));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}

		// Schritt 9
		// Protokoll 2
		
		lang.nextStep("Step 2");
		status.hide();
		message_1.hide();
		message_1_r.hide();
		
		
		sc1.highlight(1);
		sc2.highlight(1);
		sc1.unhighlight(0);
		sc2.unhighlight(0);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 14));
		
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, kAS);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		message_2 = lang.newText(new Offset(-50, -30, "AS", AnimalScript.DIRECTION_NW), "{ " + nB + " ,public[" + nB + "] }", "message_2", null, textprops); 
		lang.nextStep();
		status.show();
		status.setText(aktuellerStatus.get(1), null, null);
		message_2_r = lang.newRect(new Offset(-5, -5, "message_2", AnimalScript.DIRECTION_NW), new Offset(5, 5, "message_2", AnimalScript.DIRECTION_SE), "message_2r", null, rectprops);
		lang.nextStep();

		try {
			message_2.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(10, 10, "A", AnimalScript.DIRECTION_NE), null, new TicksTiming(400));
			message_2_r.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(5, 5, "A", AnimalScript.DIRECTION_NE), null, new TicksTiming(400));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		
		rectprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		createKB("A");
				
		// Schritt 10
		// Protokoll 4
		
		lang.nextStep("Step 3");
		status.hide();
		message_2.hide();
		message_2_r.hide();
		
		sc1.highlight(2);
		sc2.highlight(2);
		sc1.unhighlight(1);
		sc2.unhighlight(1);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 14));
		
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kB);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		message_3 = lang.newText(new Offset(10, 10, "A", AnimalScript.DIRECTION_NE), "{ " + nA + " ,I("  + nA +  ") }", "message_3", null, textprops); 
		lang.nextStep();
		status.show();
		status.setText(aktuellerStatus.get(2), null, null);
		message_3_r = lang.newRect(new Offset(-5, -5, "message_3", AnimalScript.DIRECTION_NW), new Offset(5, 5, "message_3", AnimalScript.DIRECTION_SE), "message_3r", null, rectprops);
		lang.nextStep();
		
		try {
			message_3.moveTo(AnimalScript.DIRECTION_E, null, new Offset(-120, 10, "B", AnimalScript.DIRECTION_NE), null, new TicksTiming(800));
			message_3_r.moveTo(AnimalScript.DIRECTION_E, null, new Offset(-125, 5, "B", AnimalScript.DIRECTION_NE), null, new TicksTiming(800));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		
		// Schritt 11
		// Protokoll 4
		
		lang.nextStep("Step 4");
		status.hide();
		message_3.hide();
		message_3_r.hide();

		sc1.highlight(3);
		sc2.highlight(3);
		sc1.unhighlight(2);
		sc2.unhighlight(2);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 14));
		
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		message_4 = lang.newText(new Offset(-80, 10, "B", AnimalScript.DIRECTION_NE), nB + " " + nA, "message_4", null, textprops); 
		lang.nextStep();
		status.show();
		status.setText(aktuellerStatus.get(3), null, null);
		message_4_r = lang.newRect(new Offset(-5, -5, "message_4", AnimalScript.DIRECTION_NW), new Offset(5, 5, "message_4", AnimalScript.DIRECTION_SE), "message_4r", null, rectprops);
		lang.nextStep();
		
		try {
			message_4.moveTo(AnimalScript.DIRECTION_SW, null, new Offset(20, -30, "AS", AnimalScript.DIRECTION_NW), null, new TicksTiming(400));
			message_4_r.moveTo(AnimalScript.DIRECTION_SW, null, new Offset(15, -35, "AS", AnimalScript.DIRECTION_NW), null, new TicksTiming(400));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		
		// Schritt 12
		// Protokoll 5
		
		lang.nextStep("Step 5");
		status.hide();
		message_4.hide();
		message_4_r.hide();
		
		sc1.highlight(4);
		sc2.highlight(4);
		sc1.unhighlight(3);
		sc2.unhighlight(3);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 14));
		
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, kAS);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		message_5 = lang.newText(new Offset(20, -30, "AS", AnimalScript.DIRECTION_NW), "{ " + nA + " ,public[" + nA + "] } ", "message_5", null, textprops); 
		lang.nextStep();
		status.show();
		status.setText(aktuellerStatus.get(4), null, null);
		message_5_r = lang.newRect(new Offset(-5, -5, "message_5", AnimalScript.DIRECTION_NW), new Offset(5, 5, "message_5", AnimalScript.DIRECTION_SE), "message_5r", null, rectprops);
		lang.nextStep();
		
		try {
			message_5.moveTo(AnimalScript.DIRECTION_NE, null, new Offset(-140, 10, "B", AnimalScript.DIRECTION_NE), null, new TicksTiming(400));
			message_5_r.moveTo(AnimalScript.DIRECTION_NE, null, new Offset(-145, 5, "B", AnimalScript.DIRECTION_NE), null, new TicksTiming(400));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		
		rectprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		createKA("B");
				
		// Schritt 13
		// Protokoll 6
		
		MultipleChoiceQuestionModel mc_2 = new MultipleChoiceQuestionModel("Frage_2");
		
		mc_2.setPrompt("Wieso ist die Kommunikation noch nicht fertig?");
		mc_2.addAnswer("Die letzten beiden Schritte sind optional, Sie sind noch relikte aus einem alten ISO-Standart", 0, "Falsch");
		mc_2.addAnswer("Damit kein Timeout entsteht, muessen stets Pakete gesendet werden", 0, "Falsch");
		mc_2.addAnswer("B kann sich noch nicht sicher sein, dass A der richtige Partner ist und es keine Replay Attacke ist", 5, "Richtig");
		lang.addMCQuestion(mc_2);
		
		/*
		 * Optionaler Teil: Challenge Response-Verfahren
		 */
		
		lang.nextStep("Step 6");
		
		
		if(challenge){
			
			Text challenge_1;
			Text challenge_2;
			Text challenge_3;
			
			sc1.hide();
			sc2.hide();
			protokoll.hide();
			protokollr.hide();
			status.hide();
			message_5.hide();
			message_5_r.hide();
			
			textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					globalfont, Font.PLAIN, 16));
			
			challenge_1 = lang
					.newText(
							new Coordinates(20, 75),
							"In den letzten beiden Protokollschritten wird nun noch die Gegenseite verifiziert, dies erfolgt ueber das Challenge-Response verfahren.",
							"challenge_1", null, textprops);
			
			lang.nextStep("Step 6.1");
			
			challenge_1.hide();
			
			challenge_2 = lang
					.newText(
							new Coordinates(20, 75),
							"Ziel ist es, dass man sich sicher sein kann, dass die Gegenseite die ist, fuer die man sie haelt.",
							"challenge_2", null, textprops);
			
			challenge_3 = lang
					.newText(
							new Coordinates(20, 110),
							"Die geschieht ueber einen Frage - Antwort wechsel. Im Protokoll geschieht dies ueber die Zufallszahl und als Antwort die Zahl - 1",
							"challenge_3", null, textprops);
			
			circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
			circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			Circle challengeA = lang.newCircle(new Coordinates(100, 250), 20, "challengeA", null, circleprops);
			textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					globalfont, Font.BOLD, 16));
			Text challengeA_t = lang.newText(new Offset(-5, 10, "challengeA", AnimalScript.DIRECTION_N), "A",
					"challengeA_t", null, textprops);
			
			circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
			circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			Circle challengeB = lang.newCircle(new Coordinates(500, 250), 20, "challengeB", null, circleprops);
			textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					globalfont, Font.BOLD, 16));
			Text challengeB_t = lang.newText(new Offset(-5, 10, "challengeB", AnimalScript.DIRECTION_N), "B",
					"challengeB_t", null, textprops);

			lang.nextStep("Step 6.2");
			
			Text challenge_4;
			
			challenge_4 = lang
					.newText(
							new Coordinates(100, 200),
							this.challengeA,
							"challenge_4", null, textprops);
			
			lang.nextStep();
			
			try {
				challenge_4.moveTo(AnimalScript.DIRECTION_E, null, new Coordinates(500, 200), null, new TicksTiming(600));
			} catch (IllegalDirectionException e) {
				e.printStackTrace();
			}
			lang.nextStep("Step 6.3");
			
			challenge_4.hide();
			
			Text challenge_5;
			
			challenge_5 = lang
					.newText(
							new Coordinates(500, 200),
							this.challengeB,
							"challenge_5", null, textprops);
			
			lang.nextStep("Step 6.4");
			
			try {
				challenge_5.moveTo(AnimalScript.DIRECTION_W, null, new Coordinates(100, 200), null, new TicksTiming(600));
			} catch (IllegalDirectionException e) {
				e.printStackTrace();
			}
			
			lang.nextStep();
			
			challenge_5.hide();			
			challengeA.hide();
			challengeB.hide();
			challengeA_t.hide();
			challengeB_t.hide();
			challenge_1.hide();
			challenge_2.hide();
			challenge_3.hide();
			sc1.show();
			sc2.show();
			protokoll.show();
			protokollr.show();
		}
		
		
		
		status.hide();
		message_5.hide();
		message_5_r.hide();
		
		sc1.highlight(5);
		sc2.highlight(5);
		sc1.unhighlight(4);
		sc2.unhighlight(4);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 14));
		
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kA);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		message_6 = lang.newText(new Offset(-140, 10, "B", AnimalScript.DIRECTION_NE), "{ I("  + nA +  ") ,I("  + nB +  ") }  ", "message_6", null, textprops); 
		lang.nextStep();
		status.show();
		status.setText(aktuellerStatus.get(5), null, null);
		message_6_r = lang.newRect(new Offset(-5, -5, "message_6", AnimalScript.DIRECTION_NW), new Offset(5, 5, "message_6", AnimalScript.DIRECTION_SE), "message_6r", null, rectprops);
		lang.nextStep();
		
		try {
			message_6.moveTo(AnimalScript.DIRECTION_W, null, new Offset(10, 10, "A", AnimalScript.DIRECTION_NE), null, new TicksTiming(800));
			message_6_r.moveTo(AnimalScript.DIRECTION_W, null, new Offset(5, 5, "A", AnimalScript.DIRECTION_NE), null, new TicksTiming(800));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		
		// Schritt 14
		// Protokoll 7
		
		lang.nextStep("Step 7");
		status.hide();
		message_6.hide();
		message_6_r.hide();
		
		sc1.highlight(6);
		sc2.highlight(6);
		sc1.unhighlight(5);
		sc2.unhighlight(5);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 14));
		
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, kB);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		message_7 = lang.newText(new Offset(10, 10, "A", AnimalScript.DIRECTION_NE), "{ I("  + nB +  " - 1) } ", "message_7", null, textprops); 
		lang.nextStep();
		status.show();
		status.setText(aktuellerStatus.get(6), null, null);
		message_7_r = lang.newRect(new Offset(-5, -5, "message_7", AnimalScript.DIRECTION_NW), new Offset(5, 5, "message_7", AnimalScript.DIRECTION_SE), "message_7r", null, rectprops);
		lang.nextStep();
		
		try {
			message_7.moveTo(AnimalScript.DIRECTION_E, null, new Offset(-120, 10, "B", AnimalScript.DIRECTION_NE), null, new TicksTiming(800));
			message_7_r.moveTo(AnimalScript.DIRECTION_E, null, new Offset(-125, 5, "B", AnimalScript.DIRECTION_NE), null, new TicksTiming(800));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}

		
		// Schritt 16
		// Text + Ziel erreicht
		
		lang.nextStep("Protokoll beendet");
		status.hide();
		message_7.hide();
		message_7_r.hide();
		
		sc1.unhighlight(6);
		sc2.unhighlight(6);
		
		verbindung.show();
		verbindung_text.show();
		
		// Schritt 17
		
		lang.nextStep("Ausblick - Verwendung der Signatur");
		sc1.hide();
		sc2.hide();
		
		Text abschluss_0;
		Text abschluss_1;
		Text abschluss_2;
		Text abschluss_3;
		Text abschluss_4;
		Text abschluss_5;
		
		protokoll.hide();
		protokollr.hide();		
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		
		abschluss_0 = lang.newText(new Coordinates(20, 75),
				"Warum werden die Nachrichten von " + nAS + " signiert und nicht verschluesselt?",
				"abschluss_0", null, textprops);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		abschluss_1 = lang.newText(
				new Coordinates(20, 110),
				"Die Signatur der Schluessel verhindert, dass ein Man-In-The-Middle Attack stattfindet. Potentiell haben alle Teilnehmer",
				"abschluss_1", null, textprops);
		abschluss_2 = lang.newText(
				new Coordinates(20, 140),
				"oeffentliche Schluessel von anderen Teilnehmern, da diese nicht geheim sein muessen. Bekommt also nun " + nA + " oder " + nB + " eine Nachricht",
				"abschluss_2", null, textprops);
		abschluss_3 = lang.newText(
				new Coordinates(20, 170),
				"verschluesset mit ihrem oeffentlichen Schluessel koennen Sie es zwar lesen aber sich nicht sicher sein ob dies auch vertrauenwuerdig ist.",
				"abschluss_3", null, textprops);
		
		abschluss_4 = lang.newText(
				new Coordinates(20, 200),
				"Die Signatur jeddoch kann nur mit dem privaten Schluessel erstellt worden sein. Die Signatur garantiert Inegritaet und Authenzitaet, trotz des Klartextes.",
				"abschluss_4", null, textprops);
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.ITALIC, 16));	
				
		abschluss_5 = lang.newText(
				new Coordinates(20, 250),
				"Weiterfuehrende Links: http://de.wikipedia.org/wiki/Man-in-the-middle-Angriff",
				"abschluss_5", null, textprops);
		
		// Schritt 18
		
		lang.nextStep("Ausblick - Grenzen des Protokolls");
		abschluss_0.hide();
		abschluss_1.hide();
		abschluss_2.hide();
		abschluss_3.hide();
		abschluss_4.hide();
		abschluss_5.hide();
		
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		
		lang.newText(new Coordinates(20, 75),
				"Wo liegen die Grenzen des Protokolls?",
				"abschluss_5", null, textprops);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		lang.newText(
				new Coordinates(20, 110),
				"Die staerke des Protokolls basiert auf der Sicherheit der eingesetzten Schluesselkryptographie.",
				"abschluss_6", null, textprops);
		lang.newText(
				new Coordinates(20, 140),
				"Hier koennen z.B. Verfahren wie das RSA oder El-Gamal verfahren angewandt werden. Werden jedoch diese",
				"abschluss_7", null, textprops);
		lang.newText(
				new Coordinates(20, 170),
				"Schluessel gebrochen, ist das Verfahren auch nicht mehr sicher und eine Verbindung kann komprementiert werden.",
				"abschluss_8", null, textprops);
		
	}

	public static void main(String[] args) {
		new NeedhamSchroederAsym();
	}
}
