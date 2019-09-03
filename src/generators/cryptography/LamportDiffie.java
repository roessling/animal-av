package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class LamportDiffie implements Generator {
	private Language lang;
	private Color colorHashfunction;
	private Color colorVerification;
	private String document;
	private Color colorHashfunction2;
	private Color colorSigning;
	private boolean randomSignatureKey;
	private String[][] signatureKey;
	private Color colorSignature;
	private String errorText = "";

	public LamportDiffie() {
		// nothing to be done here...
	}

	public void init() {
		lang = new AnimalScript("Lamport-Diffie Einmal-Signaturverfahren", "Nikolaus Korfhage", 800, 600);
		lang.setStepMode(true); // step mode on
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		colorHashfunction = (Color) primitives.get("colorHashfunction");
		colorVerification = (Color) primitives.get("colorVerification");
		document = (String) primitives.get("document");
		colorHashfunction2 = (Color) primitives.get("colorHashfunction2");
		colorSigning = (Color) primitives.get("colorSigning");
		randomSignatureKey = (Boolean) primitives.get("randomSignatureKey");
		signatureKey = (String[][]) primitives.get("signatureKey");
		colorSignature = (Color) primitives.get("colorSignature");
		
		// if wrong user input		
		if (docContainsInvalidChar(document)) {
			document = "001";
			this.errorText = "Dokument unpassend, Dokument 001 wird verwendet und Signaturschlüssel wird zufällig erzeugt.";
		}
		else if(!this.randomSignatureKey && (this.signatureKey.length != 3 || this.signatureKey[0].length != document.length() * 2 || matrixContainsInvalidChar(signatureKey))) {		
			this.errorText = "Signaturschlüssel unpassend, Signaturschlüssel wird zufällig erzeugt.";
		}
		
		try {
			startAnimation();
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}

		return lang.toString();
	}

	public String getName() {
		return "Lamport-Diffie Einmal-Signaturverfahren";
	}

	public String getAlgorithmName() {
		return "Lamport-Diffie OTS";
	}

	public String getAnimationAuthor() {
		return "Nikolaus Korfhage";
	}

	public String getDescription() {
		return "Das Lamport-Diffie Einmal-Signaturverfahren (engl. Lamport-Diffie One-Time Signature Scheme, kurz: LD-OTS) "
				+ " wurde 1979 von Leslie Lamport und Whitfield Diffie entwickelt."
				+ "\n"
				+ "Für die Erzeugung der Signaturen wird eine Einwegfunktionen, normalerweise eine kryptographische Hashfunktionen, verwendet."
				+ "\n"
				+ "Jede Lamport Signatur kann genau einmal zum Signieren eines Dokumentes verwendet werden."
				+ "\n"
				+ "Im Gegensatz zu g&auml;ngigen Signaturverfahren wie RSA kann das LD-OTS nicht von m&ouml;glichen zuk&uuml;nftigen Quantencomputern gebrochen werden."
				+ "\n" + "\n" + "Hier wird das Verfahren mit anschaulichen (und daher unrealistischen) Parametern betrachtet.";
	}

	public String getCodeExample() {
		return "1. Schl&uuml;sselerzeugung"
				+ "\n"
				+ "    Alice benutzt einen Zufallszahlengenerator und erzeugt 256 Paare (0 und 1) von Zufallszahlen von jeweils 256 Bit (insgesamt 2x256x256=16 KiB)."
				+ "\n"
				+ "    Diese Zahl ist der private Schl&uuml;ssel x. Mit einer Hashfunktion H wird aus dem privaten Schl&uuml;ssel der &ouml;ffentliche Schl&uuml;ssel y berechnet."
				+ "\n"
				+ "    Die Hashfunktion H ist ebenfalls &ouml;ffentlich."
				+ "\n"
				+ "\n"
				+ "2. Signierung"
				+ "\n"
				+ "    Zuerst wird die Nachricht von Alice zu einer 256-Bit Hashsumme gehasht. Die Hashsumme entspricht dem Dokument d im Beispiel.  "
				+ "\n"
				+ "    F&uuml;r jedes Bit der Hashsumme wird die entsprechende Nummer aus dem privaten Schl&uuml;ssel gew&auml;hlt."
				+ "\n"
				+ "    Daraus ergibt sich eine Signatur von 256x256 Bits, also 8 KiB."
				+ "\n"
				+ "    Die nicht benutzten verbleibenden 256 d&uuml;rfen nicht mehr verwendet oder ver&ouml;ffentlicht werden, sonst k&ouml;nnte ein Angreifer falsche Signaturen erzeugen."
				+ "\n"
				+ "\n"
				+ "3. Verfizierung"
				+ "\n"
				+ "    Bob erzeugt ebenfalls eine 256-Bit Hashsumme der Nachricht (Dokument d im Beispiel). " +
				"\n"
				+		"Ensprechend den Bits in d werden die Nummern aus dem Verifikationsschl&uuml;ssel gew&auml;hlt."
				+ "\n" + "    Falls diese den Nummern in der Signatur von Alice entsprechen akzeptiert Bob die Signatur." + "\n" + "    " + "\n"
				+ "\n";
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


	/**
	 * Generate signature key e.g. a random number or user specified key
	 * 
	 * @param len
	 *            length of document
	 */
	public String[][] genSigKey(int len) {

		// if user specified key should be used or user has entered an invalid
		// custom key
		if (!this.randomSignatureKey
				&& (this.signatureKey.length == 3 && this.signatureKey[0].length == document.length() * 2 && !matrixContainsInvalidChar(signatureKey)))
			return this.signatureKey;

		String[][] sigKey = new String[3][len * 2];
		Random generator = new Random();

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 2 * len; j++)
				sigKey[i][j] = "" + generator.nextInt(2);
		return sigKey;
	}

	/**
	 * Generate verification key
	 * 
	 * @param sigKey
	 *            signature key
	 */
	public String[][] genVerKey(String[][] sigKey) {

		String[][] verKey = new String[3][sigKey[0].length];
		for (int i = 0; i < sigKey[0].length; i++) {
			verKey[0][i] = sigKey[2][i];
			verKey[1][i] = sigKey[1][i];
			verKey[2][i] = sigKey[0][i];
		}
		return verKey;

	}

	/**
	 * Generate signature
	 * 
	 * @param sigKey
	 *            signature key
	 * @param doc
	 *            document
	 * @return signature
	 */
	public String[][] genSig(String[][] sigKey, String doc) {
		String[][] sig = new String[3][doc.length()];
		int j = 0;
		for (int i = 0; i < doc.length(); i++) {
			if (doc.charAt(i) == '0') {
				sig[0][i] = sigKey[0][j];
				sig[1][i] = sigKey[1][j];
				sig[2][i] = sigKey[2][j];
			} else {
				sig[0][i] = sigKey[0][j + 1];
				sig[1][i] = sigKey[1][j + 1];
				sig[2][i] = sigKey[2][j + 1];
			}
			j = j + 2;
		}

		return sig;
	}

	/**
	 * Applies H(x) on sig
	 * 
	 * @param sig
	 *            signature
	 * @return H(sig)
	 */
	public String[][] genSigHash(String[][] sig) {
		String[][] sigH = new String[3][sig[0].length];
		for (int i = 0; i < sig[0].length; i++) {
			sigH[0][i] = sig[2][i];
			sigH[1][i] = sig[1][i];
			sigH[2][i] = sig[0][i];
		}
		return sigH;
	}

	/**
	 * Checks if all values of the matrix are either 0 or 1
	 * 
	 * @param sigKey
	 * @return true if no wrong values
	 */
	public boolean matrixContainsInvalidChar(String[][] sigKey) {
		for (int i = 0; i < signatureKey.length; i++)
			for (int j = 0; j < signatureKey[0].length; j++)
				if (!(sigKey[i][j].equals("0") || sigKey[i][j].equals("1")))
					return true;
		return false;
	}
	
	/**
	 * Checks if all values of the document are either 0 or 1
	 * 
	 * @param doc the document
	 * @return true if no wrong values
	 */
	public boolean docContainsInvalidChar(String doc) {
		for (int i = 0; i < doc.length(); i++)
				if (!(doc.charAt(i) == '0' || doc.charAt(i) == '1'))
					return true;
		return false;
	}

	/**
	 * 
	 * @return an empty matrix
	 */
	String[][] generateEmptyMatrixBig() {
		String[][] m = new String[3][this.document.length() * 2];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < this.document.length() * 2; j++)
				m[i][j] = " ";
		return m;
	}

	/**
	 * 
	 * @return an empty matrix
	 */
	String[][] generateEmptyMatrixSmall() {
		String[][] m = new String[3][this.document.length()];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < this.document.length(); j++)
				m[i][j] = " ";
		return m;
	}

	/**
	 * Gives format '(r, g, b)' for Color
	 * 
	 * @param c
	 *            Color
	 * @return String '(r, g, b)'
	 */
	private String printColor(Color c) {
		return "(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ")";
	}

	/**
	 * String representation for String, format: "1" "2" "3" ...
	 * 
	 * @param doc
	 * @return String
	 */
	private String docToArrayCode(String doc) {
		String out = "";
		for (int i = 0; i < doc.length(); i++)
			out += "\"" + doc.charAt(i) + "\" ";
		return out;
	}

	/**
	 * The whole animation
	 * 
	 * @throws IllegalDirectionException
	 */
	private void startAnimation() throws IllegalDirectionException {
		/*
		 * Create icons
		 */
		// Create a (red) signing key icon
		CircleProperties keyCircleProp = new CircleProperties();
		keyCircleProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		Circle keyCircle = lang.newCircle(new Coordinates(20, 20), 5, "keyCircle", new Hidden(), keyCircleProp);
		RectProperties keyR1Prop = new RectProperties();
		keyR1Prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		Rect keyR1 = lang.newRect(new Offset(0, -2, keyCircle, "C"), new Offset(16, 1, keyCircle, "C"), "keyR1", new Hidden(), keyR1Prop);
		Rect keyR2 = lang.newRect(new Offset(-6, 0, keyR1, "NE"), new Offset(-4, 6, keyR1, "NE"), "keyR2", new Hidden(), keyR1Prop);
		Rect keyR3 = lang.newRect(new Offset(-3, 0, keyR1, "NE"), new Offset(-2, 4, keyR1, "NE"), "keyR3", new Hidden(), keyR1Prop);
		Rect keyR4 = lang.newRect(new Offset(-1, 0, keyR1, "NE"), new Offset(1, 6, keyR1, "NE"), "keyR4", new Hidden(), keyR1Prop);
		LinkedList<Primitive> sigKeyList = new LinkedList<Primitive>();
		sigKeyList.add(keyCircle);
		sigKeyList.add(keyR1);
		sigKeyList.add(keyR2);
		sigKeyList.add(keyR3);
		sigKeyList.add(keyR4);
		Group sigKey = lang.newGroup(sigKeyList, "sigKey");
		sigKey.changeColor("color", colorSigning, null, null);
		sigKey.changeColor("fillColor", colorSigning, null, null);

		// Create a green verification key icon (no clone?)
		CircleProperties keyCirclePropV = new CircleProperties();
		keyCirclePropV.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		Circle keyCircleV = lang.newCircle(new Coordinates(20, 20), 5, "keyCircleV", new Hidden(), keyCircleProp);
		RectProperties keyR1PropV = new RectProperties();
		keyR1PropV.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		Rect keyR1V = lang.newRect(new Offset(0, -2, keyCircleV, "C"), new Offset(16, 1, keyCircleV, "C"), "keyR1", new Hidden(), keyR1PropV);
		Rect keyR2V = lang.newRect(new Offset(-6, 0, keyR1V, "NE"), new Offset(-4, 6, keyR1V, "NE"), "keyR2", new Hidden(), keyR1PropV);
		Rect keyR3V = lang.newRect(new Offset(-3, 0, keyR1V, "NE"), new Offset(-2, 4, keyR1V, "NE"), "keyR3", new Hidden(), keyR1PropV);
		Rect keyR4V = lang.newRect(new Offset(-1, 0, keyR1V, "NE"), new Offset(1, 6, keyR1V, "NE"), "keyR4", new Hidden(), keyR1PropV);
		LinkedList<Primitive> sigKeyListV = new LinkedList<Primitive>();
		sigKeyListV.add(keyCircleV);
		sigKeyListV.add(keyR1V);
		sigKeyListV.add(keyR2V);
		sigKeyListV.add(keyR3V);
		sigKeyListV.add(keyR4V);
		Group verKey = lang.newGroup(sigKeyListV, "verKey");
		verKey.changeColor("color", colorVerification, null, null);
		verKey.changeColor("fillColor", colorVerification, null, null);
		verKey.moveTo("NW", "translate", new Coordinates(100, 100), null, null);

		// Create a dokument icon
		Rect docFrame = lang.newRect(new Coordinates(80, 80), new Coordinates(95, 100), "docFrame", null);
		Node[] docHeadlineArray = { new Offset(4, 5, docFrame, "NW"), new Offset(10, 5, docFrame, "NW") };
		Polyline docHeadline = lang.newPolyline(docHeadlineArray, "docHeadline", null);
		Node[] docLine1Array = { new Offset(2, 10, docFrame, "NW"), new Offset(12, 10, docFrame, "NW") };
		Polyline docLine1 = lang.newPolyline(docLine1Array, "docLine1", null);
		Node[] docLine2Array = { new Offset(0, 2, docLine1, "NW"), new Offset(10, 2, docLine1, "NW") };
		Polyline docLine2 = lang.newPolyline(docLine2Array, "docLine2", null);
		Node[] docLine3Array = { new Offset(0, 2, docLine2, "NW"), new Offset(9, 2, docLine2, "NW") };
		Polyline docLine3 = lang.newPolyline(docLine3Array, "docLine3", null);
		Node[] docLine4Array = { new Offset(0, 2, docLine3, "NW"), new Offset(10, 2, docLine3, "NW") };
		Polyline docLine4 = lang.newPolyline(docLine4Array, "docLine4", null);
		LinkedList<Primitive> docList = new LinkedList<Primitive>();
		docList.add(docFrame);
		docList.add(docHeadline);
		docList.add(docLine1);
		docList.add(docLine2);
		docList.add(docLine3);
		docList.add(docLine4);
		Group doc = lang.newGroup(docList, "doc");
		doc.hide();

		/*
		 * Header for whole animation
		 */
		TextProperties headerProp = new TextProperties();
		headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(20, 30), "Lamport-Diffie Einmal-Signaturverfahren", "header", null, headerProp);

		RectProperties hRectProp = new RectProperties();
		hRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		hRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		hRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "hRect", null, hRectProp);
		
		if (this.errorText != "") {
			TextProperties errorProp = new TextProperties();
			errorProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			errorProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
			Text error = lang.newText(new Coordinates(20, 100), errorText, "error", null, errorProp);

			RectProperties eRectProp = new RectProperties();
			eRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
			eRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			eRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
			eRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			Rect eRect = lang.newRect(new Offset(-5, -5, error, "NW"), new Offset(5, 5, error, "SE"), "eRect", null, eRectProp);
			lang.nextStep();
			eRect.hide();
			error.hide();
		}
		else
			lang.nextStep();
				
		/*
		 * Print intruduction
		 */
		TextProperties descTextProp = new TextProperties();
		descTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		Text introText1 = lang.newText(new Offset(0, 100, header, "SW"), "Mit dem Lamport-Diffie Einmal-Signaturverfahren (LD-OTS) können Dokumente",
				"introText1", null, descTextProp);
		Text introText2 = lang.newText(new Offset(0, 5, introText1, "SW"), "signiert und verifierziert werden.", "introText1", null, descTextProp);
		Text introText3 = lang.newText(new Offset(0, 10, introText2, "SW"),
				"In diesem Beispiel werden Dokumente der Länge 3 signiert und verifiziert.", "introText1", null, descTextProp);
		Text introText4 = lang.newText(new Offset(0, 5, introText3, "SW"), "Es wird eine Hashfunktion H und der Sicherheitsparameter 3 verwendet.",
				"introText1", null, descTextProp);
		Text introText5 = lang.newText(new Offset(0, 5, introText4, "SW"), "Der Sicherheitsparameter ist hier die Zeilenanzahl der Matrizen.",
				"introText1", null, descTextProp);
		Text introText6 = lang.newText(new Offset(0, 10, introText5, "SW"), "Nach der Schlüsselgenerierung wird das Dokument zunächst signiert und",
				"introText1", null, descTextProp);
		Text introText7 = lang.newText(new Offset(0, 5, introText6, "SW"), "anschließend verifiziert.", "introText1", null, descTextProp);
		/*
		 * step
		 */
		lang.nextStep();
		/*
		 * Print icons and introduction
		 */
		doc.moveTo("NW", "translate", new Offset(40, 60, introText7, "SW"), null, null);
		doc.show();
		Text introDocText = lang.newText(new Offset(80, 63, introText7, "SW"), "=  " + document, "introDocText", null, descTextProp);
		Text introHashText = lang.newText(new Offset(80, 100, introText7, "SW"), "H(d1, d2, d3)  =  (d3, d2, d1)", "introHashText", null,
				descTextProp);
		// Hash function icon
		TextProperties hashIconProp = new TextProperties();
		hashIconProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 22));
		hashIconProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHashfunction);
		Text iconHash = lang.newText(new Offset(37, 103, introText7, "SW"), "H", "iconHash", null, hashIconProp);
		/*
		 * step
		 */
		lang.nextStep("Schlüsselerzeugung");
		/*
		 * Print key generation, explanation
		 */
		// TO DO: Sourcecode?
		iconHash.hide();
		doc.hide();
		introDocText.hide();
		introHashText.hide();
		introText1.hide();
		introText2.hide();
		introText3.hide();
		introText4.hide();
		introText5.hide();
		introText6.hide();
		introText7.hide();
		// lang.addLine("label \"Schlüsselerzeugung\"");
		TextProperties captionText = new TextProperties();
		captionText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
		Text caption = lang.newText(new Coordinates(20, 100), "Schlüsselerzeugung", "caption", null, captionText);
		/*
		 * step
		 */
		lang.nextStep();
		Text keyGenDescr1 = lang.newText(new Offset(0, 100, header, "SW"), "Der Signaturschlüssel ist eine zufällig erzeugte Zahl:", "keyGenDescr1",
				null, descTextProp);
		Text keyGenDescr2 = lang.newText(new Offset(0, 10, keyGenDescr1, "SW"), "x = x(0, 1), x(1, 1), x(0, 2), x(1, 2), ..., x(0, n), x(1, n)",
				"keyGenDescr2", null, descTextProp);
		/*
		 * step
		 */
		lang.nextStep();
		Text keyGenDescr3 = lang.newText(new Offset(0, 30, keyGenDescr2, "SW"),
				"Daraus wird mit einer Hashfunktion H der Verifikationsschlüssel erzeugt: ", "keyGenDescr3", null, descTextProp);
		Text keyGenDescr4 = lang.newText(new Offset(0, 10, keyGenDescr3, "SW"), "y = y(0, 1), y(1, 1), y(0, 2), y(1, 2), ..., y(0, n), y(1, n)",
				"keyGenDescr4", null, descTextProp);
		Text keyGenDescr5 = lang.newText(new Offset(0, 10, keyGenDescr4, "SW"),
				"  = H(x(0, 1)), H(x(1, 1)), H(x(0, 2)), H(x(1, 2)), ..., H(x(0, n), H(x(1, n))", "keyGenDescr5", null, descTextProp);
		/*
		 * step
		 */
		lang.nextStep();
		Text keyGenDescr6 = lang.newText(new Offset(0, 30, keyGenDescr5, "SW"), "Der private Schlüssel ist also x, öffentlich sind y und H.",
				"keyGenDescr6", null, descTextProp);
		/*
		 * step
		 */
		lang.nextStep();
		sigKey.moveTo("NW", "translate", new Offset(0, 60, keyGenDescr6, "SW"), null, null);
		sigKey.show();
		Text sigKeyText = lang.newText(new Offset(35, 60, keyGenDescr6, "SW"), "Signierschlüssel x = ", "sigKeyText", null, descTextProp);
		
		/**
		 * Matrix in benötigter Form leider nicht unterstützt!
		 * lang.newStringMatrix(new Offset (300, 20, keyGenDescr6, "SW"), generateEmptyMatrixBig(), "sigKeyMat", null);
		 */
		
		lang.addLine("grid \"sigKeyMat\" offset (300, 20) from \"keyGenDescr6\" SW lines 3 columns " + document.length() * 2
				+ " style matrix cellWidth 12 cellHeight 12 highlightTextColor " + printColor(colorHashfunction2)
				+ " highlightFillColor gold highlightBordercolor black font SansSerif size 14 align center depth 2");
		LinkedList<Primitive> sigKeyGroupList = new LinkedList<Primitive>();
		sigKeyGroupList.add(sigKey);
		sigKeyGroupList.add(sigKeyText);
		Group sigKeyGroup = lang.newGroup(sigKeyGroupList, "sigKeyGroup");
		lang.addLine("group \"sigKeyGroup\" \"sigKeyText\" \"sigKey\" \"sigKeyMat\""); // M
		/*
		 * step
		 */
		lang.nextStep();
		/*
		 * Print signature key matrix
		 */
		String[][] JSigKey = genSigKey(document.length());
		int j = 0;
		for (int i = 0; i < document.length(); i++) {
			lang.addLine("setGridValue \"sigKeyMat[0][" + j + "]\" \"" + JSigKey[0][j] + "\""); // M
			lang.addLine("setGridValue \"sigKeyMat[1][" + j + "]\" \"" + JSigKey[1][j] + "\""); // M
			lang.addLine("setGridValue \"sigKeyMat[2][" + j + "]\" \"" + JSigKey[2][j] + "\""); // M
			j++;

			lang.addLine("setGridValue \"sigKeyMat[0][" + j + "]\" \"" + JSigKey[0][j] + "\""); // M
			lang.addLine("setGridValue \"sigKeyMat[1][" + j + "]\" \"" + JSigKey[1][j] + "\""); // M
			lang.addLine("setGridValue \"sigKeyMat[2][" + j + "]\" \"" + JSigKey[2][j] + "\" refresh"); // M

			lang.nextStep();
			j++;
		}
		/*
		 * step
		 */
		verKey.moveTo("NW", "translate", new Offset(0, 210, keyGenDescr6, "SW"), null, null);
		verKey.show();
		Text verKeyText = lang.newText(new Offset(35, 210, keyGenDescr6, "SW"), "Verifikationsschlüssel y = ", "verKeyText", null, descTextProp);
		lang.addLine("grid \"verKeyMat\" offset (300, 165) from \"keyGenDescr6\" SW lines 3 columns " + document.length() * 2
				+ " style matrix cellWidth 12 cellHeight 12 highlightTextColor " + printColor(colorHashfunction2)
				+ " highlightFillColor gold highlightBordercolor black font SansSerif size 14 align center depth 2");
		LinkedList<Primitive> verKeyGroupList = new LinkedList<Primitive>();
		verKeyGroupList.add(verKey);
		verKeyGroupList.add(verKeyText);
		Group verKeyGroup = lang.newGroup(verKeyGroupList, "verKeyGroup");
		lang.addLine("group \"verKeyGroup\" \"verKey\" \"verKeyText\" \"verKeyMat\""); // M
		/*
		 * step
		 */
		lang.nextStep();
		// Hash arrow and descriptopn
		Node[] hashArrowArray = { new Coordinates(341, 472), new Coordinates(341, 512) };
		PolylineProperties hashArrowProp = new PolylineProperties();
		hashArrowProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHashfunction);
		hashArrowProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
		Polyline hashArrow = lang.newPolyline(hashArrowArray, "hashArrow", null, hashArrowProp);

		TextProperties hashArrowText1Prop = new TextProperties();
		hashArrowText1Prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		hashArrowText1Prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 10));
		hashArrowText1Prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHashfunction);
		Text hashArrowText1 = lang
				.newText(new Offset(5, -6, hashArrow, "NW"), "H(d1,d2,d3) = (d3,d2,d1)", "hashArrowText1", null, hashArrowText1Prop);

		TextProperties hashArrowText2Prop = new TextProperties();
		hashArrowText2Prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		hashArrowText2Prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 10));
		hashArrowText2Prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHashfunction2);
		Text hashArrowText2 = lang.newText(new Offset(5, 7, hashArrow, "NW"), "H(1,1,1) = (1,1,1)", "hashArrowText2", null, hashArrowText2Prop);

		LinkedList<Primitive> hashArrowGroupList = new LinkedList<Primitive>();
		hashArrowGroupList.add(hashArrow);
		hashArrowGroupList.add(hashArrowText1);
		hashArrowGroupList.add(hashArrowText2);
		Group hashArrowGroup = lang.newGroup(hashArrowGroupList, "hashArrowGroup");

		// Print verification key matrix
		String[][] JVerKey = genVerKey(JSigKey);
		int x = 341, y = 472;
		for (int i = 0; i < JVerKey[0].length; i++) {
			lang.addLine("setGridValue \"verKeyMat[0][" + i + "]\" \"" + JVerKey[0][i] + "\" refresh"); // M
			lang.addLine("setGridValue \"verKeyMat[1][" + i + "]\" \"" + JVerKey[1][i] + "\" refresh"); // M
			lang.addLine("setGridValue \"verKeyMat[2][" + i + "]\" \"" + JVerKey[2][i] + "\" refresh"); // M
			hashArrowText2.setText("H(" + JSigKey[0][i] + "," + JSigKey[1][i] + "," + JSigKey[2][i] + ") = (" + JSigKey[2][i] + "," + JSigKey[1][i]
					+ "," + JSigKey[0][i] + ")", null, null);
			if (i > 0) {
				Node[] hpl1Array = { new Coordinates(x, 472), new Coordinates(x = x + 24, 472) };
				Polyline hpl = lang.newPolyline(hpl1Array, "hashArrowHelpLine" + i, new Hidden());
				hashArrowGroup.moveVia("NW", "translate", hpl, null, new TicksTiming(30));
			}
			if (i==JVerKey[0].length-1) lang.nextStep("Signierung");
			else lang.nextStep();
		}

		/*
		 * Signing
		 */
		// lang.addLine("label \"Signierung\""); // M
		caption.setText("Signierung", null, null);
		keyGenDescr1.hide();
		keyGenDescr2.hide();
		keyGenDescr3.hide();
		keyGenDescr4.hide();
		keyGenDescr5.hide();
		keyGenDescr6.hide();
		hashArrowGroup.hide();
		verKeyGroup.hide();

		Node[] moveSigKeyGroupLineArray = { new Offset(0, 350, header, "SW"), new Offset(0, 115, header, "SW") };
		Polyline moveSigKeyGroupLine = lang.newPolyline(moveSigKeyGroupLineArray, "moveSigKeyGroupLine", new Hidden());
		sigKeyGroup.moveVia("NW", "translate", moveSigKeyGroupLine, null, new TicksTiming(30));

		/*
		 * step
		 */
		lang.nextStep();
		doc.moveTo("NW", "translate", new Offset(5, 260, header, "SW"), null, null);
		doc.show();
		Text docSigDescr = lang.newText(new Offset(40, 265, header, "SW"), "Dokument d = ", "docSigDescr", null, descTextProp);
		lang
				.addLine("array \"docArray\" at offset (320, 255) from \"header\" SW color black fillColor white elementColor black elemHighlight red cellHighlight "
						+ printColor(colorSignature)
						+ " horizontal length "
						+ document.length()
						+ " "
						+ docToArrayCode(document)
						+ "depth 1 cascaded within 30 ticks");

		/*
		 * step
		 */
		lang.nextStep();
		TextProperties matrixDescrProp = new TextProperties();
		matrixDescrProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		matrixDescrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		matrixDescrProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		x = 317;
		y = 70;
		LinkedList<Primitive> matrixDescrGroupList = new LinkedList<Primitive>();
		hashArrowGroupList.add(hashArrow);
		Text matrixDescr = lang.newText(new Offset(x, y, header, "SW"), "0", "matrixDescr0", null, matrixDescrProp);
		Text matrixDescrPred;
		matrixDescrGroupList.add(matrixDescr);
		x = x + 24;
		for (int i = 1; i < document.length() * 2; i++) {
			matrixDescrPred = matrixDescr;
			matrixDescr = lang.newText(new Offset(24, 0, matrixDescrPred, "NW"), "" + i % 2, "matrixDescr" + i, null, matrixDescrProp);
			matrixDescrGroupList.add(matrixDescr);
			x = x + 24;
		}
		Group matDescrGroup = lang.newGroup(matrixDescrGroupList, "matrixDescrGroup");

		TextProperties sigIconProp = new TextProperties();
		sigIconProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.ITALIC, 22));
		sigIconProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSignature);
		Text iconSig = lang.newText(new Offset(5, 380, header, "SW"), "S", "iconSig", null, sigIconProp);
		Text textSig = lang.newText(new Offset(40, 370, header, "SW"), "Signatur s = ", "textSig", null, descTextProp);
		lang.addLine("grid \"sigMat\" offset (300, 360) from \"header\" NW lines 3 columns " + document.length()
				+ " style matrix cellWidth 12 cellHeight 12 highlightTextColor " + printColor(colorHashfunction2)
				+ " highlightFillColor gold highlightBordercolor black font SansSerif size 14 align center depth 2");

		/*
		 * step
		 */
		lang.nextStep();
		// first step
		String[][] JSig = genSig(JSigKey, document);
		x = 341;
		Text prevMatrDescr;
		Text currentMatrDescr = (Text) matrixDescrGroupList.get(Integer.parseInt("" + document.charAt(0)));
		currentMatrDescr.changeColor("color", colorSignature, null, null);
		x += Integer.parseInt("" + document.charAt(0)) * 24;
		Node[] matDescArrowArray = { new Coordinates(x, 230), new Coordinates(x, 260) };
		PolylineProperties matDescArrowProp = new PolylineProperties();
		matDescArrowProp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, Boolean.TRUE);
		matDescArrowProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(208, 0, 0));
		matDescArrowProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		Polyline matDescArrow = lang.newPolyline(matDescArrowArray, "matDescArrow", null, matDescArrowProp);
		lang.addLine("highlightArrayCell on \"docArray\" position 0 within 30 ticks");
		lang.addLine("setGridValue \"sigMat[0][0]\" \"" + JSig[0][0] + "\"");
		lang.addLine("setGridValue \"sigMat[1][0]\" \"" + JSig[1][0] + "\"");
		lang.addLine("setGridValue \"sigMat[2][0]\" \"" + JSig[2][0] + "\" refresh");
		lang.nextStep();
		// following steps
		j = 2;

		for (int i = 1; i < document.length(); i++) {

			Node[] mv2 = { new Coordinates(x, 230), new Coordinates(x = 341 + i * 48 + Integer.parseInt("" + document.charAt(i)) * 24, 230) };
			Polyline mv2l = lang.newPolyline(mv2, "moveMDAHLine" + i, new Hidden());
			matDescArrow.moveVia("NW", "translate", mv2l, null, new TicksTiming(20));

			prevMatrDescr = currentMatrDescr;
			currentMatrDescr = (Text) matrixDescrGroupList.get(j + Integer.parseInt("" + document.charAt(i)));
			currentMatrDescr.changeColor("color", colorSignature, null, null);
			prevMatrDescr.changeColor("color", Color.LIGHT_GRAY, null, null);

			lang.addLine("unhighlightArrayCell on \"docArray\" position " + (i - 1) + " within 30 ticks");
			lang.addLine("highlightArrayCell on \"docArray\" position " + i + " within 30 ticks");
			lang.addLine("setGridValue \"sigMat[0][" + i + "]\" \"" + JSig[0][i] + "\"");
			lang.addLine("setGridValue \"sigMat[1][" + i + "]\" \"" + JSig[1][i] + "\"");
			lang.addLine("setGridValue \"sigMat[2][" + i + "]\" \"" + JSig[2][i] + "\" refresh");

			j += 2;
			lang.nextStep();
		}
		lang.addLine("unhighlightArrayCell on \"docArray\" position " + (document.length() - 1) + " within 30 ticks");
		currentMatrDescr.changeColor("color", Color.LIGHT_GRAY, null, null);

		matDescrGroup.hide();
		TextProperties docSignedProp = new TextProperties();
		docSignedProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		docSignedProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSignature);
		Text docSigned = lang.newText(new Coordinates(540, 305), "Dokument signiert", "docSigned", null, docSignedProp);
		RectProperties docSignedRectProp = new RectProperties();
		docSignedRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		docSignedRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, colorSignature);
		docSignedRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorSignature);
		docSignedRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect docSignedRect = lang.newRect(new Offset(0, 2, docSigned, "SW"), new Offset(0, 4, docSigned, "SE"), "docSignedRect", null,
				docSignedRectProp);

		/*
		 * step
		 */
		lang.nextStep("Verifikation");
		// lang.addLine("label \"Verifikation\"");
		lang.addLine("unhighlightArrayCell on \"docArray\" position " + (document.length() - 1) + " within 30 ticks");

		LinkedList<Primitive> sigGroupList = new LinkedList<Primitive>();
		sigGroupList.add(iconSig);
		sigGroupList.add(textSig);
		Group sigGroup = lang.newGroup(sigGroupList, "sigGroup");
		lang.addLine("group \"sigGroup\" \"sigMat\" \"textSig\" \"iconSig\"");
		LinkedList<Primitive> docGroupList = new LinkedList<Primitive>();
		docGroupList.add(doc);
		docGroupList.add(docSigDescr);
		Group docGroup = lang.newGroup(docGroupList, "docGroup");
		lang.addLine("group \"docGroup\" \"doc\" \"docGroup\" \"docArray\"");
		sigKeyGroup.hide();
		docSigned.hide();
		docSignedRect.hide();
		docGroup.hide();
		matDescArrow.hide();
		caption.setText("Verifikation", null, null);
		Node[] mv3 = { new Offset(0, 350, header, "SW"), new Offset(0, 113, header, "SW") };
		Polyline mv3l = lang.newPolyline(mv3, "moveSigGroupLine", new Hidden());
		sigGroup.moveVia("NW", "translate", mv3l, null, new TicksTiming(30));
		/*
		 * step
		 */
		lang.nextStep();
		Node[] mv4 = { new Offset(0, 0, iconHash, "NW"), new Offset(5, 277, header, "SW") };
		Polyline mv4l = lang.newPolyline(mv4, "moveIconHash", new Hidden());
		iconHash.moveVia("NW", "translate", mv4l, null, null);
		iconHash.show();
		lang.newText(new Offset(40, 280, header, "SW"), "H(s1,s2,s3) = ", "hashText", null, descTextProp);
		lang.addLine("grid \"hashMat\" offset (300, 260) from \"header\" NW lines 3 columns " + document.length()
				+ " style matrix cellWidth 12 cellHeight 12 highlightTextColor " + printColor(colorHashfunction2)
				+ " highlightFillColor gold highlightBordercolor black font SansSerif size 14 align center depth 2");

		/*
		 * step
		 */
		lang.nextStep();
		String[][] JsigHash = genSigHash(JSig);
		hashArrowGroup.moveTo("NW", "translate", new Offset(320, 220, header, "NW"), null, null);
		hashArrowText2.setText("H(" + JSig[0][0] + "," + JSig[1][0] + "," + JSig[2][0] + ") = (" + JSig[2][0] + "," + JSig[1][0] + "," + JSig[0][0]
				+ ")", null, null);
		hashArrowGroup.show();
		lang.addLine("setGridValue \"hashMat[0][0]\" \"" + JsigHash[0][0] + "\"");
		lang.addLine("setGridValue \"hashMat[1][0]\" \"" + JsigHash[1][0] + "\"");
		lang.addLine("setGridValue \"hashMat[2][0]\" \"" + JsigHash[2][0] + "\" refresh");
		lang.nextStep();
		x = 320;
		for (int i = 1; i < document.length(); i++) {

			Node[] mv5 = { new Coordinates(x, 195), new Coordinates(x += 24, 195) };
			Polyline mv5l = lang.newPolyline(mv5, "moveSigHashLine" + i, new Hidden());
			hashArrowGroup.moveVia("NW", "translate", mv5l, null, new TicksTiming(30));
			hashArrowText2.setText("H(" + JSig[0][i] + "," + JSig[1][i] + "," + JSig[2][i] + ") = (" + JSig[2][i] + "," + JSig[1][i] + ","
					+ JSig[0][i] + ")", null, null);

			lang.addLine("setGridValue \"hashMat[0][" + i + "]\" \"" + JsigHash[0][i] + "\"");
			lang.addLine("setGridValue \"hashMat[1][" + i + "]\" \"" + JsigHash[1][i] + "\"");
			lang.addLine("setGridValue \"hashMat[2][" + i + "]\" \"" + JsigHash[2][i] + "\" refresh");
			lang.nextStep();
		}
		hashArrowGroup.hide();

		Node[] mv6 = { new Offset(0, 350, header, "SW"), new Offset(0, 270, header, "SW") };
		Polyline mv6l = lang.newPolyline(mv6, "moveverKeyGroupLine", new Hidden());
		verKeyGroup.moveVia("NW", "translate", mv6l, null, null);
		verKeyGroup.show();
		/*
		 * step
		 */
		lang.nextStep();

		Node[] mv7 = { new Offset(0, 0, docGroup, "NW"), new Offset(0, 550, header, "NW") };
		Polyline mv7l = lang.newPolyline(mv7, "moveDocGroupLine", new Hidden());
		docGroup.moveVia("NW", "translate", mv7l, null, null);
		docGroup.show();

		// Matrix description
		Node[] moveMatDescrArray = { new Offset(0, 0, matDescrGroup, "SW"), new Offset(0, 302, matDescrGroup, "SW") };
		Polyline moveMatDescrLine = lang.newPolyline(moveMatDescrArray, "moveMatDescrLine", new Hidden());
		matDescrGroup.moveVia("NW", "translate", moveMatDescrLine, null, null);
		matDescrGroup.show();

		/*
		 * step
		 */
		lang.nextStep();
		lang.addLine("highlightArrayCell on \"docArray\" position 0 within 30 ticks");
		Node[] mv8 = { new Offset(320, 316, header, "SW"), new Offset(320 + Integer.parseInt("" + document.charAt(0)) * 24, 390, header, "SW") };
		PolylineProperties verificationArrowProp = new PolylineProperties();
		verificationArrowProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorVerification);
		verificationArrowProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
		verificationArrowProp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, Boolean.TRUE);
		Polyline verHash = lang.newPolyline(mv8, "verHash0", null, verificationArrowProp);
		TextProperties hashSigCompProp = new TextProperties();
		hashSigCompProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
		hashSigCompProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorVerification);
		Text hashSigComp = lang.newText(new Offset(140, 355, header, "SW"), "H(s1) == y(" + document.charAt(0) + ", 1)?", "hashSigComp", null,
				hashSigCompProp);
		/*
		 * step
		 */
		lang.nextStep();
		Polyline verHashOld;
		int x_start = 320;
		for (int i = 1; i < document.length(); i++) {
			verHashOld = verHash;
			lang.addLine("highlightArrayCell on \"docArray\" position " + i + " within 30 ticks");
			verHashOld.hide();
			Node[] verHashArray = { new Offset(x_start += 24, 316, header, "SW"),
					new Offset(320 + i * 48 + Integer.parseInt("" + document.charAt(i)) * 24, 390, header, "SW") };
			verHash = lang.newPolyline(verHashArray, "verHash" + i, null, verificationArrowProp);
			hashSigComp.setText("H(s" + (i + 1) + ") == y(" + document.charAt(i) + ", " + (i + 1) + ")?", null, null);
			lang.nextStep();
		}
		hashSigComp.hide();
		verHash.hide();
		matDescrGroup.hide();
		TextProperties docVerifiedProp = new TextProperties();
		docVerifiedProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		docVerifiedProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorVerification);
		Text docVerified = lang.newText(new Coordinates(540, 305), "Dokument verfifiziert", "docSigned", null, docVerifiedProp);
		RectProperties docVerifiedRectProp = new RectProperties();
		docVerifiedRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		docVerifiedRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, colorVerification);
		docVerifiedRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorVerification);
		docVerifiedRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(0, 2, docSigned, "SW"), new Offset(0, 4, docVerified, "SE"), "docVerifiedRect", null,
				docVerifiedRectProp);

	}

}