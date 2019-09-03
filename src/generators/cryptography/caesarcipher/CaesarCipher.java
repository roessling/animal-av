/**
 * 
 */
package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Tim, Jan
 * 
 */
public class CaesarCipher implements generators.framework.Generator {

	private Language lang;

	/**
	 * Animiert die Caesar-Verschluesselung
	 * 
	 * @param l
	 */
	public CaesarCipher(Language l) {
		lang = l;
		lang.setStepMode(true);
	}
	
	// Default-Konstruktor, der fuer den Generator-Baum benoetigt wird
	public CaesarCipher() {
		this(new AnimalScript(algName, "Jan H. Post, Tim Grube", 640, 480));
	}

	// Name des Algorithmus
	private final static String algName = "Caesar Cipher";
	// Autoren
//	private final static String authorName = "Jan H. Post, Tim Grube";
	// um wieviel positionen wird rotiert
	private int rotation;
	// zu verschluesselnde Nachricht
	private String message;
	// Farbe in der der Sourcecode gehighlighted wird
	private Color sc_highlight;
	// Farbe in der der Sourcecode dargestellt wird
	private Color sc_text;
	// Schriftart in der der SourceCode dargestellt wird
	private Font sc_font;
	// Contextfarbe fuer den Sourcecode
	private Color sc_context;
	// Schriftart fuer die ueberschrift
	private Font header_font;
	// farbe der arrays
	private Color array_color;
	// fuellfarbe der arrays
	private Color array_fill;
	// array gefuellt?
	private Boolean array_filled;
	// farbe der array-elemente
	private Color array_elementcolor;
	// farbe der gehighlighteten array_elemente
	private Color array_elementhighlight;
	// farbe der gehighlighteten array-zellen
	private Color array_cellhighlight;
	// farbe des ergebnisses
	private Color result_color;

	/**
	 * Kurzbeschreibung des animierten Algorithmus
	 */
	private static final String DESCRIPTION = "Die Caesarverschlüsselung ist eine einfache"
			+ "Verschiebechiffre welche Nachrichten durch Verschiebungen des Alphabets"
			+ "verschlüsselt.\n"
			+ "Bei einer Verschiebung um 3 wird das A durch ein D substituiert, "
			+ "das B durch ein E, ...\n";

	/**
	 * Sourcecode des animierten Algorithmus
	 */
	private static final String SOURCE_CODE = "public String caesar(String message, int verschiebung)\n"
			+ "{\n"
			+ "String[] encryptedAlph = rotateAlphabet(verschiebung);\n"
			+ "\n"
			+ "for(int i = 0; i < message.length(); i++)\n"
			+ "{\n"
			+ "char current = message.charAt(i);\n"
			+ "char encryptedChar = encryptedAlph[current];\n"
			+ "encryptedMessage += enchryptedChar;\n"
			+ "}\n"
			+ "\n"
			+ "return encryptedMessage;\n" + "}";

	/**
	 * gibt beschreibung des algo. zurueck
	 * 
	 * @return
	 */
	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	/**
	 * gibt beispielcode des algo. zurueck
	 * 
	 * @return
	 */
	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	/**
	 * gibt den namen der animation zurueck
	 * 
	 * @return der Name der Animation
	 */
	public String getName() {
		return "Verschlüsselung: Caesar";
	}

	/**
	 * gibt eine beschreibung des algorithmus zurueck
	 * 
	 * @return die Beschreibung des Algorithmus
	 */
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * gibt ein code-beispiel (source-code) zurueck
	 * 
	 * @return ein Codebeispiel
	 */
	public String getCodeExample() {
		return SOURCE_CODE;
	}

	/**
	 * Bereit die Animation der Verschluesselung vor
	 * 
	 * @param msg
	 *            zu verschluesselnde Nachricht
	 * @param rot
	 *            #-stellen um die rotiert werden muss
	 */
	private void crypt(String msg, int rot) {

		// Definiere die Ueberschrift
		TextProperties txtProps = new TextProperties();
		txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, header_font);

		@SuppressWarnings("unused")
		Text txt = lang.newText(new Coordinates(40, 30),
				"Caesarverschlüsselung", "header", null, txtProps);

		// Definiere den Rahmen um die Ueberschrift
		RectProperties rectProps = new RectProperties();
		@SuppressWarnings("unused")
		Rect rect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header",
				AnimalScript.DIRECTION_SE), "hRect", null, rectProps);
		
		
		// Definiere die warnung
		SourceCodeProperties warnProps = new SourceCodeProperties();
		warnProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
			new Font("Monospaced", Font.PLAIN, 12));
		warnProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

		SourceCode warning = lang.newSourceCode(new Coordinates(450, 120),
				"warning", null, warnProps);
		
		warning.addCodeLine("Diese Animation kann lediglich Nachrichten verschlüsseln", null, 0, null);
		warning.addCodeLine("welche nur aus Buchstaben bestehen.", null, 0, null);
		warning.addCodeLine("Leerzeichen werden unverschlüsselt übernommen", null, 0, null);


		// Definiere den Rahmen um die warnung
		@SuppressWarnings("unused")
		Rect warnRect = lang.newRect(new Offset(-5, -5, "warning",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "warning",
				AnimalScript.DIRECTION_SE), "warnRect", null, rectProps);

		

		// im ersten Schritt ist nur die Ueberschrift zu sehen - der Inhalt
		// kommt einen Schritt spaeter
		lang.nextStep();

		// Definiere das Aussehen der Arrays
		ArrayProperties arrayProps = new ArrayProperties();

		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, array_color);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, array_fill);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, array_filled);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				array_elementcolor);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				array_elementhighlight);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				array_cellhighlight);

		// Alphabet
		String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z" };

		// zielalphabet
		String[] rotatedalph = new String[alphabet.length];
		rotatedalph = rotateArray(alphabet, rot);

		// Stringarrays zum Anzeigen der Informationen in der Animation
		StringArray ca = lang.newStringArray(new Coordinates(40, 115),
				alphabet, "base", null, arrayProps);

		StringArray ea = lang.newStringArray(new Coordinates(40, 180),
				rotatedalph, "destination", null, arrayProps);
		ea.hide();
		
		
		

		// Pfeile zum animieren der Rotation
		PolylineProperties lineProps = new PolylineProperties();
		lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		lineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
		lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		Coordinates[] line1Coordinates = { new Coordinates(70, 250),
				new Coordinates(130, 250) };
		Polyline arrow1 = lang.newPolyline(line1Coordinates, "arrow1", null,
				lineProps);
		Coordinates[] line2Coordinates = { new Coordinates(140, 250), new Coordinates(200, 250) };
		Polyline arrow2 = lang.newPolyline(line2Coordinates, "arrow2", null, lineProps);
		Coordinates[] line3Coordinates = { new Coordinates(210, 250), new Coordinates(270, 250) };
		Polyline arrow3 = lang.newPolyline(line3Coordinates, "arrow3", null, lineProps);
		
		Polyline arrows[] = {arrow1, arrow2, arrow3};

		// zum animieren der rotation...
		StringArray[] steps = null;

		// ...berechne zwischenalphabete
		if (rot > 0) {
			steps = new StringArray[rot];

			for (int i = 0; i < rot; i++) {
				String[] ra = rotateArray(alphabet, i);
				steps[i] = lang.newStringArray(new Coordinates(40, 180), ra,
						"step" + i, null, arrayProps);
				steps[i].hide();
			}
		}

		lang.nextStep();

		// Definiere das Aussehen des Source-Codes
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, sc_context);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sc_font);
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				sc_highlight);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sc_text);

		// Zeichne das Source-Code Objekt
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 300),
				"sourceCode", null, scProps);

		// Fuege Zeile fuer Zeile des Source-Codes ein.
		// Line, name, indentation, display dealy
		sc.addCodeLine(
				"public String caesar(String message, int verschiebung)", null,
				0, null); // 0
		sc.addCodeLine("{", null, 0, null); // 1
		sc.addCodeLine(
				"String[] encryptedAlph = rotateAlphabet(verschiebung);", null,
				1, null); // 2
		sc.addCodeLine("String encryptedMessage;", null, 1, null); // 2
		sc.addCodeLine("", null, 1, null); // 4 (leerzeile zur uebersicht)
		sc.addCodeLine("for(int i = 0; i < message.length(); i++)", null, 1,
				null); // 5
		sc.addCodeLine("{", null, 1, null); // 6
		sc.addCodeLine("char current = message.charAt(i);", null, 2, null); // 7
		sc.addCodeLine("char encryptedChar = encryptedAlph[current];", null, 2,
				null); // 8
		sc.addCodeLine("encryptedMessage += enchryptedChar;", null, 2, null); // 9
		sc.addCodeLine("}", null, 1, null); // 10
		sc.addCodeLine("", null, 1, null); // 11
		sc.addCodeLine("return encryptedMessage;", null, 1, null); // 12
		sc.addCodeLine("}", null, 0, null); // 13

		// Zeichne das Kommentar-Code Objekt
		SourceCode com = lang.newSourceCode(new Coordinates(450, 300),
				"sourceCode", null, scProps);

		// Fuege Zeile fuer Zeile des Kommentar-Codes ein.
		// Line, name, indentation, display dealy
		com.addCodeLine(
				"// Caesarverschlüsselung auf message bei Alphabetverschiebung "
						+ "um verschiebung anwenden", null, 0, null); // 0
		com.addCodeLine("", null, 0, null); // 1
		com.addCodeLine("// verschiebe/rotiere Alphabet", null, 0, null); // 2
		com.addCodeLine(
				"// Variable zur Speicherung der verschlüsselten Nachricht",
				null, 0, null); // 2
		com.addCodeLine("", null, 0, null); // 4 (leerzeile zur uebersicht)
		com.addCodeLine("// Verschlüssele Zeichen für Zeichen", null, 0, null); // 5
		com.addCodeLine("", null, 0, null); // 6
		com.addCodeLine("// aktuelles Zeichen holen", null, 0, null); // 7
		com.addCodeLine("// verschlüsselte Repräsentation des Zeichens holen",
				null, 0, null); // 8
		com.addCodeLine("// füge verschlüsseltes Zeichen zur verschlüsselten "
				+ "Nachricht hinzu", null, 0, null); // 9
		com.addCodeLine("", null, 0, null); // 10
		com.addCodeLine("", null, 0, null); // 11
		com.addCodeLine("// verschlüsselte Nachricht zurückgeben", null, 0,
				null); // 12
		com.addCodeLine("", null, 0, null); // 13

		lang.nextStep();

		// highlighte den funktionsaufruf und starte algorithmus
		sc.highlight(0);
		encrypt(msg, sc, ca, ea, steps, arrows);

	}

	// Zaehler fuer die Anzahl der Pointer
	private int pointerCounter = 0;

	/**
	 * Verschluessele die Nachricht und animiere entsprechend
	 * 
	 * @param nachricht
	 *            zu verschluesselnde nachricht
	 * @param sc
	 *            source-code
	 * @param ca
	 *            clear-alphabet (nicht verschoben)
	 * @param ea
	 *            encrypted-alphabet (verschoben)
	 * @param rotsteps
	 *            zwischenschritte der rotation des alphabets
	 */
	private void encrypt(String nachricht, SourceCode sc, StringArray ca,
			StringArray ea, StringArray[] rotsteps, Polyline[] arrows) {

		// zu verschluesselnde nachricht
		String msg = nachricht;
		msg = msg.toLowerCase();
		// verschluesselte nachricht
		String crypted = "";

		// aktuelles zeichen
		char cur;
		// gehighlightete Zelle
		int hlc = 0;
		// gehighlightete zeile
		int hll = 0;

		// aussehen der texte mit klar- und chiffre-text
		TextProperties txtProps = new TextProperties();
		txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));

		// klartext anzeigen
		@SuppressWarnings("unused")
		Text klartitel = lang.newText(new Coordinates(40, 580),
				"Klartextnachricht:", "klartitel", null, txtProps);
		@SuppressWarnings("unused")
		Text klartext = lang.newText(new Coordinates(200, 580), nachricht,
				"klartext", null, txtProps);

		// verschluesselter text anzeigen
		@SuppressWarnings("unused")
		Text encryptedtitel = lang.newText(new Coordinates(40, 600),
				"verschlüsselter Text:", "enctitel", null, txtProps);
		Text encrypted = lang.newText(new Coordinates(200, 600), crypted,
				"chiffretext", null, txtProps);

		// definiere rahmen um chiffretext zur hervorhebung bei aenderungen
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				array_cellhighlight);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect enc_rect = lang.newRect(new Offset(-4, -4, "chiffretext",
				AnimalScript.DIRECTION_NW), new Offset(10, 4, "chiffretext",
				AnimalScript.DIRECTION_SE), "chiffrect", null, rectProps);
		enc_rect.hide();

		// Erzeuge Array-Pointer ...
		pointerCounter++;
		// ... und setzte seine Properties
		ArrayMarkerProperties arrayMIProps = new ArrayMarkerProperties();
		arrayMIProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
		arrayMIProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, array_color);
		ArrayMarker iMarker = lang.newArrayMarker(ea, 0, "i" + pointerCounter,
				new Hidden(), arrayMIProps);
		iMarker.hide();

		lang.nextStep();

		// highlighte "rotations"-zeile
		sc.toggleHighlight(hll, 2);
		hll = 2;

		// zeige rotation an...
		TicksTiming timing = new TicksTiming(0);
		TicksTiming timing1 = new TicksTiming(33);
		TicksTiming timing2 = new TicksTiming(66);
		for (Polyline arrow : arrows) {
			arrow.show();
		}
		if (rotsteps != null) {
			for (int i = 0; i < rotsteps.length; i++) {
				arrows[0].changeColor(null, Color.GRAY, timing, null);
				arrows[1].changeColor(null, Color.LIGHT_GRAY, timing, null);
				arrows[2].changeColor(null, Color.LIGHT_GRAY, timing, null);
				arrows[0].changeColor(null, Color.LIGHT_GRAY, timing1, null);
				arrows[1].changeColor(null, Color.GRAY, timing1, null);
				arrows[2].changeColor(null, Color.LIGHT_GRAY, timing1, null);
				arrows[0].changeColor(null, Color.LIGHT_GRAY, timing2, null);
				arrows[1].changeColor(null, Color.LIGHT_GRAY, timing2, null);
				arrows[2].changeColor(null, Color.GRAY, timing2, null);
				if (i > 0) {
					rotsteps[i - 1].hide(timing);
				}
				rotsteps[i].show(timing);
				timing = new TicksTiming(100 * (i + 1));
				timing1 = new TicksTiming(100 * (i + 1) + 33);
				timing2 = new TicksTiming(100 * (i + 1) + 66);
			}
			if (rotsteps.length > 0) {
				rotsteps[rotsteps.length - 1].hide(timing);
			}
		}
		// endgueltiges alphabet einblenden...
		ea.show(timing);
		iMarker.hide();

		lang.nextStep();
		
		// Pointer auf Array eiblenden...
		iMarker.show();
		for (Polyline arrow : arrows) {
			arrow.hide();
		}
		
		lang.nextStep();

		// highlighte bereitstellung der variable fuer den verschluesselten text
		sc.toggleHighlight(hll, 3);
		enc_rect.show();
		hll = 3;
		lang.nextStep();

		// highlighte for-schleife
		sc.toggleHighlight(hll, 5);
		hll = 5;
		enc_rect.hide();

		// verschluessele zeichen fuer zeichen
		for (int i = 0; i < msg.length(); i++) {

			// gehighlightete zellen unhighlighten
			ca.unhighlightCell(hlc, null, null);
			ea.unhighlightCell(hlc, null, null);
			lang.nextStep();

			// highlighte "zeichen-holen"-zeile
			sc.toggleHighlight(hll, 7);
			hll = 7;
			// hole zeichen
			cur = msg.charAt(i);

			// a ist ASCII(97) daher ist a an position 0 im array...
			hlc = cur - 97;
			// entsprechende zelle im "normalen" alphabet highlighten
			ca.highlightCell(hlc, null, new TicksTiming(75));
			lang.nextStep();

			// pruefe ob leerzeichen
			if (cur != ' ') {
				// hole verschluesselte repraesentation des zeichens
				sc.toggleHighlight(hll, 8);
				hll = 8;
				crypted += ea.getData(cur - 97);// a ist ASCII(97)

				// pointer bewegen und zelle im unteren verschobenen array
				// highlighten
				iMarker.move(hlc, null, new TicksTiming(75));
				iMarker.show();
				ea.highlightCell(hlc, null, new TicksTiming(75));
			} else {
				// leerzeichen hinzuefuegen
				sc.toggleHighlight(hll, 8);
				hll = 8;
				crypted += " ";
			}

			lang.nextStep();

			// fuege verschluesseltes zeichen zur nachricht hinzu
			sc.toggleHighlight(hll, 9);
			hll = 9;
			encrypted.setText(crypted, new TicksTiming(75), null);

			// hebe verschluesselte nachricht hervor...
			enc_rect.hide();
			enc_rect = lang.newRect(new Offset(-4, -4, "chiffretext",
					AnimalScript.DIRECTION_NW), new Offset(10, 4,
					"chiffretext", AnimalScript.DIRECTION_SE), "chiffrect",
					null, rectProps);

			lang.nextStep();

			enc_rect.hide();
			// highlighte for-schleife
			sc.toggleHighlight(hll, 5);
			hll = 5;
			// das unhighlight der zellen passiert auch "hier"
			// ist nur wegen der for-schleife vorne stehend -
			// wird aber in diesem schritt ausgefuehrt

		}

		// highlight der zellen entfernen
		ca.unhighlightCell(hlc, null, null);
		ea.unhighlightCell(hlc, null, null);
		iMarker.hide();
		lang.nextStep();

		// highlighte return-zeile
		sc.toggleHighlight(hll, 12);
		hll = 12;

		lang.nextStep();

		sc.unhighlight(hll);

		// ergebnis hervorheben
		encrypted.changeColor("color", result_color, null, null);

	}

	/**
	 * rotiere ein array um rot-positionen
	 * 
	 * @param array
	 *            zu rotierendes array
	 * @param rot
	 *            anzahl positionen
	 * @return rotiertes array
	 */
	private String[] rotateArray(String[] array, int rot) {
		String[] ar = new String[array.length];

		/*
		 * rotiert das array:
		 * 
		 * a, b, c, d, e (2) -> d, e, a, b, c
		 */

		
		int rotation = rot % array.length;
		// mod array.length damit ich auch mehrfach im kreis rotieren kann...
		for (int i = 0; i < array.length; i++) {
			if (i < rot) {
				ar[i] = array[array.length - rotation + i];
			} else {
				ar[i] = array[i - rotation];
			}
		}

		return ar;
	}

	/**
	 * erstellt den animalscript code zur animierung des algo. die paramenter
	 * enthalten die darstellungsoptionen die vom nutzer gewaehlt wurden.
	 */
	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		Language l = new AnimalScript(getAlgorithmName(), getAnimationAuthor(),
				800, 600);
		CaesarCipher s = new CaesarCipher(l);

		// initialisieren um immer gleich zu starten
		s.init();
		s.message = (String) primitives.get("Klartextnachricht");
		s.rotation = (Integer) primitives.get("Rotation");
		s.sc_highlight = (Color) primitives.get("Farbe der Code-Markierung");
		s.array_cellhighlight = (Color) primitives.get("Farbe der Array-Markierung");
		

		// verschluesselung animieren
		s.crypt(s.message, s.rotation);

		// animalscript welcher die animation enthaelt zurueckgeben
		return l.toString();
	}

	/**
	 * gibt den namen des alg. zurueck
	 */
	@Override
	public String getAlgorithmName() {
    return "Caesar Cipher";
	}

	/**
	 * gibt den/die autor(en) der anim. zurueck
	 */
	@Override
	public String getAnimationAuthor() {
		return "Jan H. Post, Tim Grube";
	}

	/**
	 * gibt die lokalisierung der anim. zurueck
	 */
	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	/**
	 * gibt die dateiendung des animationsfiles zurueck
	 */
	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	/**
	 * gibt die kategorie der animation zurueck
	 */
	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	/**
	 * welche programmiersprache wurde verwendet?
	 */
	@Override
	public String getOutputLanguage() {
		return generators.framework.Generator.JAVA_OUTPUT;
	}

	/**
	 * initialisiert den generator auf default werte
	 */
	@Override
	public void init() {
		rotation = 20;
		message = "Die Caesarverschluesselung ist eine Verschiebechiffre";

		sc_context = Color.BLUE;
		sc_font = new Font("Monospaced", Font.PLAIN, 12);
		sc_highlight = Color.RED;
		sc_text = Color.BLACK;

		header_font = new Font("SansSerif", Font.BOLD, 24);

		array_color = Color.BLACK;
		array_fill = Color.WHITE;
		array_filled = Boolean.TRUE;
		array_elementcolor = Color.BLACK;
		array_elementhighlight = Color.RED;
		array_cellhighlight = Color.YELLOW;

		result_color = Color.BLUE;
	}

}
