package generators.cryptography;
//package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.OffsetCoords;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class NeedhamSchroederSym implements Generator {
	
	private Text terms;

	// Grundbegriffe
	private Text grundbegriffe_1;
	private Text grundbegriffe_2;
	private Text grundbegriffe_3;
	
	// Grundbegriffe
	private Text definition_1;
	private Text definition_2;
	private Text definition_3;
	private Text definition_4;

	// Links
	private Text link_1;
	private Text link_2;

	private static Language lang;
	
	private Color cryptAS;
	private Color cryptA;
	private Color cryptB;

	private Circle A;
	private Circle B;
	private Circle AS;

	private Rect KASr;
	private Rect KASrA;
	private Rect KASrB;
	
	private String labelA;
	private String labelB;
	private String labelT;
	
	private Color ColorA;
	private Color ColorB;
	private Color ColorT;

	RectProperties rectprops = new RectProperties();
	TextProperties textprops = new TextProperties();
	CircleProperties circleprops= new CircleProperties();

	private String challengeA;
	private String challengeB;
	
	private boolean challenge;
	
	private Font gFont;
	private String globalfont;
	
	public void init() {
		lang = new AnimalScript("Needham-Schroeder (Symmetrisch) [DE]",
				"Florian Oswald", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props,
		Hashtable<String, Object> primitives) {
		init();
		
		cryptAS = (Color) primitives.get("Keys T");
		cryptA = (Color) primitives.get("Keys Alice");
		cryptB = (Color) primitives.get("Keys Bob");
		
		challengeA = (String) primitives.get("Question");
		challengeB = (String) primitives.get("Answer");
		challenge = (Boolean) primitives.get("Challenge Response");
		
		gFont = (Font) primitives.get("Font");
		globalfont = gFont.getFontName();
		
		labelA = (String) primitives.get("Name Alice");
		labelB = (String) primitives.get("Name Bob");
		labelT = (String) primitives.get("Name T");
		
		ColorA = (Color) primitives.get("Color Alice");
		ColorB = (Color) primitives.get("Color Bob");
		ColorT = (Color) primitives.get("Color T");
		
		protokoll();
		lang.finalizeGeneration();
		return lang.toString();
	}
	
	public NeedhamSchroederSym(){
		
		init();
		// zum Testen

		labelT = "T";
		labelA = "A";
		labelB = "D";

		ColorT = Color.GRAY;
		ColorA = Color.LIGHT_GRAY;
		ColorB = Color.LIGHT_GRAY;

		cryptA = Color.MAGENTA;
		cryptB = Color.ORANGE;
		cryptAS = Color.GREEN;

		globalfont = Font.SANS_SERIF;
		
        challengeA = "Wie alt sind Sie?";
        challengeB = "Ich bin 23 Jahre alt";
        challenge = true;

		//
		protokoll();
		
	}

	public void createA(int x, int y, Language l) {
		circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, ColorA);
		circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		A = l.newCircle(new Coordinates(x, y), 20, "A", null, circleprops);
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		l.newText(new Offset(-5, 10, "A", AnimalScript.DIRECTION_N), labelA,
				"Text A", null, textprops);
	}

	public void createB(int x, int y, Language l) {
		circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, ColorB);
		circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		B = l.newCircle(new Coordinates(x, y), 20, "B", null, circleprops);
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		l.newText(new Offset(-5, 10, "B", AnimalScript.DIRECTION_N), labelB,
				"Text B", null, textprops);
	}

	public void createAS(int x, int y, Language l) {
		circleprops.set(AnimationPropertiesKeys.FILL_PROPERTY, ColorT);
		circleprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		AS = l.newCircle(new Coordinates(x, y), 20, "AS", null, circleprops);
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));
		l.newText(new Offset(-10, 10, "AS", AnimalScript.DIRECTION_N), labelT,
				"Text AS", null, textprops);
	}

	public void createKA(Language l, String at) {
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		rectprops = new RectProperties();
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cryptA);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		if (at.equals("AS")) {
			l.newText(new Offset(-55, -5, "KAS", AnimalScript.DIRECTION_W),
					"K["+ labelA +"]", "KAAS", null, textprops);
			l.newRect(new Offset(-5, -5, "KAAS", AnimalScript.DIRECTION_NW),
					new Offset(5, 5, "KAAS", "SE"), "KAASr", null, rectprops);
		} else {
			l.newText(new Offset(-50, -5, at, AnimalScript.DIRECTION_W),
					"K["+ labelA +"]", "KA", null, textprops);
			l.newRect(new Offset(-5, -5, "KA", AnimalScript.DIRECTION_NW),
					new Offset(5, 5, "KA", "SE"), "KAr", null, rectprops);
		}

	}

	public void createKB(Language l, String at) {
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		rectprops = new RectProperties();
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cryptB);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		if (at.equals("AS")) {
			l.newText(new Offset(20, -5, "KAS", AnimalScript.DIRECTION_E),
					"K["+ labelB +"]", "KBAS", null, textprops);
			l.newRect(new Offset(-5, -5, "KBAS", AnimalScript.DIRECTION_NW),
					new Offset(5, 5, "KBAS", "SE"), "KBASr", null, rectprops);
		} else {
			l.newText(new Offset(20, -5, at, AnimalScript.DIRECTION_E), "K["+ labelB +"]",
					"KB", null, textprops);
			l.newRect(new Offset(-5, -5, "KB", AnimalScript.DIRECTION_NW),
					new Offset(5, 5, "KB", "SE"), "KBr", null, rectprops);
		}

	}

	public Rect createKAS(Language l, String at) {
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		if (at.equals("AS")) {
			rectprops = new RectProperties();
			rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY,
					cryptAS);
			rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			l.newText(new Offset(-20, 10, at, AnimalScript.DIRECTION_S),
					"K["+ labelT +"]", "KAS", null, textprops);
			KASr = l.newRect(new Offset(-5, -5, "KAS",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "KAS", "SE"),
					"KASr", null, rectprops);
			return KASr;
		} else if (at.equals("A")) {
			rectprops = new RectProperties();
			rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
			rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cryptAS);
			rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			l.newText(
					new Offset(-20, -40, at, AnimalScript.DIRECTION_N),
					"K["+ labelT +"]", "KASA", null, textprops);
			KASrA = l.newRect(new Offset(-5, -5, "KASA",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "KASA", "SE"),
					"KASrA", null, rectprops);
			return KASrA;
		} else if (at.equals("B")) {
			rectprops = new RectProperties();
			rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
			rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cryptAS);
			rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			l.newText(
					new Offset(-20, -40, at, AnimalScript.DIRECTION_N),
					"K["+ labelT +"]", "KASB", null, textprops);
			KASrB = l.newRect(new Offset(-5, -5, "KASB",
					AnimalScript.DIRECTION_NW), new Offset(5, 5, "KASB", "SE"),
					"KASrB", null, rectprops);
			return KASrB;
		}

		return null;
	}

	public void placeCircles(Language l) {
		createA(150, 400, l);
		createB(750, 400, l);
		createAS(450, 470, l);
	}

	public Node getA() {
		Node A = this.A.getCenter();
		return A;
	}

	public Node getB() {
		Node B = this.B.getCenter();
		return B;
	}

	public Node getAS() {
		Node AS = this.AS.getCenter();
		return AS;
	}

	public String getName() {
		return "Needham-Schroeder (Symmetrisch) [DE]";
	}

	public String getAlgorithmName() {
		return "Needham-Schroeder";
	}

	public String getAnimationAuthor() {
		return "Florian Oswald";
	}

	public String getDescription() {
		return "Das Needham-Schroeder Protokoll dienst zum Austausch von Daten &uuml;ber ein nicht sicheres Netzwerk. Es erm&ouml;glicht zwei Parteien einen gemeinsamen"
				+ "\n"
				+ "Schl&uuml;ssel auszutauschen, welcher dann f&uuml;r eine symmetrisch verschl&uuml;sselte Kommunikation benutzt werden kann. Das Protokoll wurde 1978 von "
				+ "\n"
				+ "Roger Needham und Michael Schroeder am MIT entwickelt."
				+ "\n"
				+ "\n"
				+ "Die hier vorgestellt Variante beschreibt das Symmetrische Protokoll mit den drei Parteien A, B und einer vetrauten dritten Partei AS. Die Sicherheit beruht auf"
				+ "\n"
				+ "einem sicheren Symmetrischen Kryptoverfahren (z.B. AES oder Triple-DES)"
				+ "\n"
				+ "\n"
				+ "Verst&auml;ndnis f&uuml;r Grundlegende Kryptographische Konzepte, wie die Symmetrische Verschl&uuml;sselung werden vorrausgesetzt. ";
	}

	public String getCodeExample() {
		return "A     sendet an AS : { A,B,I(A) } - unverschluesselt"
				+ "\n"
				+ "AS  sendet an A   : { I(A), B, K[AS] { K[AS], A } - K[B] } - K[A]"
				+ "\n" + "A     sendet an B  : { K[AS], A } - K[A]" + "\n"
				+ "B     sendet an A  : { I(B) } - K[AS]" + "\n"
				+ "A     sendet an B  : { I(B) - 1 } - K[AS]";
	}

	public String getFileExtension() {
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

	public void protokoll(){
		
		rectprops = new RectProperties();
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		// Step 1
		// ueberschrift + Definitionen setzen
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 20));

		lang.newText(new Coordinates(20, 30),
				"Needham-Schroeder (Symmetrisch)", "header", null, textprops);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));
		
		definition_1 = lang
				.newText(
						new Coordinates(20, 75),
						"Definition: Das Needham-Schroeder Protokoll, hier die symmetrische Variante, ermoeglicht es, ein Geheimnis",
						"Definition 1", null, textprops);
		definition_2 = lang
				.newText(
						new Coordinates(20, 105),
						"ueber ein dezentrales Netzwerk auszutauschen. Dabei sollen am Ende die beiden Parteien einen gemeinsamen symmetrischen Sitzungsschluessel",
						"Definition 2", null, textprops);
		definition_3 = lang
				.newText(
						new Coordinates(20, 135),
						"besitzen. Darueber hinaus sollen beide auch davon ueberzeugt sein, dass der Schluessel auch echt ist, sprich von der zu erwartenden Partei stammt",
						"Definition 3", null, textprops);
		definition_4 = lang
				.newText(
						new Coordinates(20, 165),
						"In spaeteren Algorithmus durch die gruenfaerbung des Schluessles gekennzeichnet",
						"Definition 4", null, textprops);
		link_1 = lang
				.newText(
						new Coordinates(20, 225),
						"Weiterfuehrende Links: http://de.wikipedia.org/wiki/Needham-Schroeder-Protokoll",
						"Link 1", null, textprops);
		link_2 = lang
				.newText(
						new Coordinates(20, 255),
						"Weiterfuehrende Links: http://de.wikipedia.org/wiki/Symmetrisches_Kryptosystem",
						"Link 2", null, textprops);

		// Step 2
		// Grundbegriffe einblenden


		
		lang.nextStep();
		


		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		terms = lang.newText(new Coordinates(20, 300), "Grundbegriffe",
				"Grungbegriffe", null, textprops);

		// Step 3
		// Erklaerung: Die Teilnehmer des Protokolls werden erstellt und
		// angezeigt


		
		MultipleChoiceQuestionModel mc_1 = new MultipleChoiceQuestionModel("Frage_1");
		mc_1.setPrompt("Wie viele Schluessel besitzt man in einer symmetrischen Verschluesselung?");
		mc_1.addAnswer("0", 0, "Ohne Schluessel keine Kryptographie");
		mc_1.addAnswer("1", 5, "Richtig");
		mc_1.addAnswer("2", 0, "Asymmetrische Systeme benutzen zwei Schluessel");
		lang.addMCQuestion(mc_1);
		lang.nextStep();

		placeCircles(lang);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		grundbegriffe_1 = lang
				.newText(
						new Coordinates(50, 330),
						"Die Teilnehmer des Protokoll " + labelA + ", " + labelB + " und die vertrauenswuerdige Partei " + labelT + "",
						"Grungbegriffe 1", null, textprops);

		// Step 4
		// Erklaerung: AS besitzt einen Situngsschluessel

		lang.nextStep();
		
		MultipleChoiceQuestionModel mc_2 = new MultipleChoiceQuestionModel("Frage_2");
		
		mc_2.setPrompt("Wieso wird eine dritte Partei benoetigt?");
		mc_2.addAnswer("Vermeidung von Man-In-The Middle", 5, "Richtung");
		mc_2.addAnswer("Nur die dritte Partei kann einen Schluessel generieren", 0, "Falsch, jeder kann einen Schluessel generieren");
		mc_2.addAnswer("Man kann dem gegenüber nicht trauen", 2, "Stimmt teilweise, ist aber nicht dir korrekte Antwort");
		lang.addMCQuestion(mc_2);
		grundbegriffe_1.hide();

		createKAS(lang, "AS");

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		grundbegriffe_2 = lang.newText(new Coordinates(50, 330),
				"Der Teilnehmer AS besitzt den auszutauschenden Schluessel",
				"Grungbegriffe 2", null, textprops);

		// Step 5
		// Erklaerung: Schluesselverteilung

		lang.nextStep();
		grundbegriffe_2.hide();

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		createKA(lang, "A");
		createKB(lang, "B");
		createKB(lang, "AS");
		createKA(lang, "AS");

		// KA und KB an KAS

		grundbegriffe_3 = lang.newText(new Coordinates(50, 330),
				"Teilnehmer " + labelA + " und " + labelB + " besitzen jeweils einen geheimen Schluessel mit " + labelT + "",
				"Grungbegriffe 3", null, textprops);

		// Step 7
		// Erklaerung: Protokoll + Code

		lang.nextStep();
		
		MultipleChoiceQuestionModel mc_3 = new MultipleChoiceQuestionModel("Frage_3");
		
		mc_3.setPrompt("Koennen Sie die Schluesselverteilung weiter berechen? Wie viele Schluessel gibt es wenn 5 Teilnehmer Symmetrisch miteinander kommunizieren wollen?");
		mc_3.addAnswer("3", 0, "Falsch");
		mc_3.addAnswer("6", 0, "Falsch");
		mc_3.addAnswer("10", 5, "Richtig");
		lang.addMCQuestion(mc_3);
		
		grundbegriffe_3.hide();

		terms.hide();

		definition_1.hide();
		definition_2.hide();
		definition_3.hide();
		definition_4.hide();

		link_1.hide();
		link_2.hide();

		String[][] sendReceive = new String[6][2];
		String[][] Data = new String[6][1];

		sendReceive[0][0] = "Sender";
		sendReceive[0][1] = "Empfaenger";
		sendReceive[1][0] = labelA;
		sendReceive[1][1] = labelT;
		sendReceive[2][0] = labelT;
		sendReceive[2][1] = labelA;
		sendReceive[3][0] = labelA;
		sendReceive[3][1] = labelB;
		sendReceive[4][0] = labelB;
		sendReceive[4][1] = labelA;
		sendReceive[5][0] = labelA;
		sendReceive[5][1] = labelB;

		Data[0][0] = "Inhalt";
		Data[1][0] = labelA + ", " + labelB + ", I(" + labelA + ")";
		Data[2][0] = "{ I(" + labelA + "), " + labelB + ", K[" + labelT + "], {K[" + labelT + "], A}-K[" + labelB + "] }-K[" + labelA + "]";
		Data[3][0] = "{ K[" + labelT + "], A }-K[" + labelB + "]";
		Data[4][0] = "{ I(" + labelB + ") }-K[" + labelT + "]";
		Data[5][0] = "{ I(" + labelB + ") - 1 }-K[" + labelT + "]";

		StringMatrix sendReceiveList = lang.newStringMatrix(new Coordinates(
				100, 100), sendReceive, "sendReceiveList", null);
		StringMatrix Content = lang.newStringMatrix(new Coordinates(300, 100),
				Data, "Content", null);

		// Step 8
		// Erklaerung: A -> AS | A,B,I(A)

		lang.nextStep();
		sendReceiveList.highlightCellColumnRange(1, 0, 1, null, null);
		Content.highlightCell(1, 0, null, null);

		Text aToAS = lang.newText(new OffsetCoords(getA(), 30, -5),
				Data[1][0], "step1", null);
		try {
			aToAS.moveTo(AnimalScript.DIRECTION_NW, null, new OffsetCoords(getAS(), -50, -40), null,
					new TicksTiming(200));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}

		// Step 9
		// Erklaerung: AS -> A | I(A), B, K[AS], {K[AS], A} K[B] K[A]

		lang.nextStep();
		aToAS.hide();
		sendReceiveList.unhighlightCellColumnRange(1, 0, 1, null, null);
		sendReceiveList.highlightCellColumnRange(2, 0, 1, null, null);
		Content.unhighlightCell(1, 0, null, null);
		Content.highlightCell(2, 0, null, null);

		rectprops = new RectProperties();
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Text asToA = lang.newText(new OffsetCoords(getAS(), -110, -45),
				"I(" + labelA + "), " + labelB + " K[" + labelT + "]", "step2", null);
		Text BInA = lang.newText(new OffsetCoords(getAS(), -30, -45),
				"{K[" + labelT + "], A}-K[" + labelB + "] }-K[" + labelA + "]", "BInA", null);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cryptB);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect BInAr = lang.newRect(new Offset(-5, -5, "BInA",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "BInA",
				AnimalScript.DIRECTION_SE), "KryptKB", null, rectprops);
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cryptA);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		Rect asToAr = lang.newRect(new Offset(-10, -10, "step2",
				AnimalScript.DIRECTION_NW), new Offset(10, 10, "BInA",
				AnimalScript.DIRECTION_SE), "KryptKA", null, rectprops);
		try {
			asToA.moveTo("SE", null, new OffsetCoords(getA(), 35, 15), null,
					new TicksTiming(200));
			asToAr.moveTo("SE", null, new OffsetCoords(getA(), 30, 8), null,
					new TicksTiming(200));
			BInA.moveTo("SE", null, new OffsetCoords(getA(), 115, 15), null,
					new TicksTiming(200));
			BInAr.moveTo("SE", null, new OffsetCoords(getA(), 110, 12), null,
					new TicksTiming(200));
		} catch (IllegalDirectionException e) {

			e.printStackTrace();
		}



		createKAS(lang, "A");
		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		lang.newRect(new Offset(-5, -5, "KASA", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "KASA", "SE"), "KASrA", null, rectprops);

		// Step 10
		// Erklaerung: A -> B | K[AS], A K[B]

		lang.nextStep();
		asToA.hide();
		asToAr.hide();
		BInA.hide();
		BInAr.hide();

		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cryptB);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		Text AB = lang.newText(new OffsetCoords(getA(), 30, 0), Data[3][0],
				"step3", null);
		Rect ABr = lang.newRect(new Offset(-5, -5, "step3",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "step3",
				AnimalScript.DIRECTION_SE), "step3" + "rect", null, rectprops);
		try {
			AB.moveTo("E", null, new OffsetCoords(getB(), -80, 0), null,
					new TicksTiming(400));
			ABr.moveTo("E", null, new OffsetCoords(getB(), -85, -5), null,
					new TicksTiming(400));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}


		sendReceiveList.unhighlightCellColumnRange(2, 0, 1, null, null);
		sendReceiveList.highlightCellColumnRange(3, 0, 1, null, null);
		Content.unhighlightCell(2, 0, null, null);
		Content.highlightCell(3, 0, null, null);

		createKAS(lang, "B");

		// Step 11
		// Erklaerung: Protokoll + Code
		
		lang.nextStep();
		
		
		if(challenge){
			
			Text challenge_1;
			Text challenge_2;
			Text challenge_3;
			
			sendReceiveList.hide();
			Content.hide();
			AB.hide();
			ABr.hide();
			
			textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					globalfont, Font.PLAIN, 16));
			
			challenge_1 = lang
					.newText(
							new Coordinates(20, 75),
							"In den letzten beiden Protokollschritten wird nun noch die Gegenseite verifiziert, dies erfolgt ueber das Challenge-Response verfahren.",
							"challenge_1", null, textprops);
			
			lang.nextStep();
			
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

			lang.nextStep();
			
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
			lang.nextStep();
			
			challenge_4.hide();
			
			Text challenge_5;
			
			challenge_5 = lang
					.newText(
							new Coordinates(500, 200),
							this.challengeB,
							"challenge_5", null, textprops);
			
			lang.nextStep();
			
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
			sendReceiveList.show();
			Content.show();
		}

		lang.nextStep();
		AB.hide();
		ABr.hide();
		sendReceiveList.unhighlightCellColumnRange(3, 0, 1, null, null);
		sendReceiveList.highlightCellColumnRange(4, 0, 1, null, null);
		Content.unhighlightCell(3, 0, null, null);
		Content.highlightCell(4, 0, null, null);

		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, cryptAS);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		Text BA = lang.newText(new OffsetCoords(getB(), -30, 0), Data[4][0],
				"step4", null);
		Rect BAr = lang.newRect(new Offset(-5, -5, "step4",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "step4",
				AnimalScript.DIRECTION_SE), "step4" + "rect", null, rectprops);
		try {
			BA.moveTo("E", null, new OffsetCoords(getA(), 30, 0), null,
					new TicksTiming(400));
			BAr.moveTo("E", null, new OffsetCoords(getA(), 25, -5), null,
					new TicksTiming(400));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}


		// Step 12
		// Erklaerung: Protokoll + Code

		lang.nextStep();
		BA.hide();
		BAr.hide();
		sendReceiveList.unhighlightCellColumnRange(4, 0, 1, null, null);
		sendReceiveList.highlightCellColumnRange(5, 0, 1, null, null);
		Content.unhighlightCell(4, 0, null, null);
		Content.highlightCell(5, 0, null, null);

		Text AB2 = lang.newText(new OffsetCoords(getA(), 30, 0),  Data[5][0],
				"step5", null);
		Rect ABr2 = lang.newRect(new Offset(-5, -5, "step5",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "step5",
				AnimalScript.DIRECTION_SE), "step5" + "rect", null, rectprops);
		try {
			AB2.moveTo("E", null, new OffsetCoords(getB(), -80, 0), null,
					new TicksTiming(400));
			ABr2.moveTo("E", null, new OffsetCoords(getB(), -85, -5), null,
					new TicksTiming(400));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}

		rectprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
		rectprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		lang.newRect(new Offset(-5, -5, "KASB", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "KASB", "SE"), "KASrB", null, rectprops);

		// Step 13
		// Erklaerung: Abschlussbericht

		lang.nextStep();
		AB2.hide();
		ABr2.hide();
		sendReceiveList.unhighlightCellColumnRange(5, 0, 1, null, null);
		Content.unhighlightCell(5, 0, null, null);
		sendReceiveList.hide();
		Content.hide();

		Node[] Line = new Node[2];
		Line[0] = new OffsetCoords(getA(), 30, 0);
		Line[1] = new OffsetCoords(getB(), -30, 0);

		lang.newText(new Coordinates(350, 380), "Sichere Verbindung mit K[AS]",
				"Abschluss 0", null, textprops);

		lang.newPolyline(Line, "Connection", null);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		lang.newText(
				new Coordinates(20, 75),
				"Am Ende des Protokoll haben sowohl " + labelA + " und " + labelB + " den Sitzungsschluessel K[" + labelT + "]",
				"Abschluss 1", null, textprops);
		lang.newText(
				new Coordinates(20, 105),
				"Mit diesem Schluessel ist es nun moeglich eine verschluesselte Verbindung zu fuehren. Zum Beispiel eine SSL Verbindung (HTTPS).",
				"Abschluss 2", null, textprops);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.BOLD, 16));

		lang.newText(new Coordinates(20, 165),
				"Fuer was sind die Zufallszahlen I(A) und I(B)?",
				"Abschluss 3", null, textprops);

		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				globalfont, Font.PLAIN, 16));

		lang.newText(
				new Coordinates(20, 195),
				"Die Zufallszahlen sind ein weitverbreitetes Mittel in der Kryptographie um sogenannte Reply Attacken zu verhinden",
				"Abschluss 4", null, textprops);
		lang.newText(
				new Coordinates(20, 225),
				"Durch die beiden Zufallszahlen ist es moeglich festzustellen, dass der Sitzungschluessel nicht wiederverwendet wird bzw.",
				"Abschluss 5", null, textprops);
		lang.newText(
				new Coordinates(20, 255),
				"eine alte Verbindung genutzt wird. Dies ist also ein zusaetzliches Sicherheitsmerkmal des Protkolls.",
				"Abschluss 6", null, textprops);
		lang.newText(
				new Coordinates(20, 285),
				"Weiterfuehrende Links: http://de.wikipedia.org/wiki/Replay-Angriff",
				"Abschluss 7", null, textprops);

	}

	public static void main(String[] args){

	}
}
