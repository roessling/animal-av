package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Caesar implements Generator {

	/**
	 * The concrete language object used for creating output
	 */
	private Language				lang;

	private boolean					hidden_am_pABC;
	private boolean					hidden_am_sABC;

	private TextProperties	textProps;
	private ArrayProperties	plaintextArrayProps, plainABCArrayProps,
			secretABCArrayProps, ciphertextArrayProps;
	private ArrayMarkerProperties	amPlainProps, amSecretProps;
	private SourceCodeProperties	scProps;

	/**
	 * (Default) constructor
	 * 
	 * @param l
	 *          the concrete language object used for creating output
	 */
	public Caesar(Language l) {
		lang = l;
		lang.setStepMode(true);
	}

	/**
	 * The standard constructor.
	 */
	public Caesar() {
		this(new AnimalScript("Caesar Chiffre", "Dmytro Vronskyi", 640, 480));
	}

	private static final String[]	PLAIN_ABC			= { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z"						};
	private static final int			width					= 16;

	private static final String		DESCRIPTION		= "Bei der Caesar-Chiffre"
																									+ " (auch als Verschiebechiffre, Caesar-Verschluesselung oder Caesar-Verschiebung"
																									+ " bezeichnet) wird zum Zwecke der Verschluesselung jeder Buchstabe"
																									+ " des lateinischen Standardalphabets um eine bestimmte Anzahl von Positionen"
																									+ " zyklisch verschoben (rotiert). Die Anzahl bestimmt den Schluessel (key),"
																									+ " der fuer die gesamte Verschluesselung unveraendert bleibt."
																									+ " Es ist eine der einfachsten (und unsichersten) Formen einer Geheimschrift.\n"
																									+ "Der Name der Caesar-Verschluesselung leitet sich vom roemischen Feldherrn"
																									+ " Gaius Julius Caesar ab, der diese Art der geheimen Kommunikation"
																									+ " für seine militaerische Korrespondenz verwendet hat. Dabei benutzte"
																									+ " Caesar selbst haeufig den Verschiebewert von drei Buchstaben."
																									+ " Im Allgemeinen kann aber der Schluessel beliebig gewaelt werden.";

	private static final String		CODE_EXAMPLE	= "Verschluesselung vom \"Plaintext\": \n"
																									+ "Um das Geheimtextalphabet \"SecretABC\" zu erhalten, wird das "
																									+ "Klartextalphabet \"PlainABC\" um key Stellen nach links verschoben. \n"
																									+ "Damit wird der an einer beliebigen Stelle i im \"PlainABC\" stehende "
																									+ "Buchstabe an die Position ((i-key) mod 26) ins \"SecretABC\" geschrieben. \n"
																									+ "Um einen Buchstaben aus dem \"Plaintext\" zu verschluesseln, muss er "
																									+ "zunaechst im \"PlainABC\" gefunden werden. Der Buchstabe mit dem gleichen "
																									+ "Index im \"SecretABC\" verschluesselt dann den Buchstaben aus dem \"PlainABC\" "
																									+ "und wird an die entsprechende Position (=Position des zu verschluesselnden "
																									+ "Buchstabens im \"Plaintext\") ins \"Ciphertext\" geschrieben. \nDie 26 Buchstaben"
																									+ " des lateinischen Standardalphabets werden unabhaengig von der Schreibweise "
																									+ "(gross oder klein) im \"Plaintext\" als Grossbuchstaben im \"Ciphertext\" "
																									+ "verschluesselt. Das Leerzeichen wird durch sich selbst verschluesselt, und "
																									+ "alle anderen Buchstaben, die nicht zum lateinischen Standardalphabet gehoeren, "
																									+ "werden bei der Verschluesselung ignoriert.";

	/**
	 * Verschluesselt den <code>plaintext</code> mittels Caesar-Chiffre mit dem
	 * Schluessel <code>key</code>.
	 * 
	 * @param plaintext
	 *          Der zu verschluessende Text
	 * @param key
	 *          Der Schluessel, der dem Verschiebewert entspricht.
	 */
	private void encode(String plaintext, int key) {
		MsTiming delay = new MsTiming(1000);
		MsTiming delay2 = new MsTiming(2000);

		TextProperties titleTextProp = new TextProperties();
		titleTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.BOLD, 24));

		lang.newText(new Coordinates(480, 40), "Caesar Cipher Algorithm", "title",
				null, titleTextProp);

		lang.nextStep();

		String[] temparray = plaintext.split("");
		String[] plainArray = new String[temparray.length - 1];
		for (int i = 0; i < plainArray.length; i++) {
			plainArray[i] = temparray[i + 1];
		}

		StringArray pa = lang.newStringArray(new Coordinates(20, 100), plainArray,
				"plainArray", null, plaintextArrayProps);

		lang.newText(new Offset(20, 0, pa, AnimalScript.DIRECTION_NE), "Plaintext",
				"Plaintext", null, textProps);

		SourceCode sc = lang.newSourceCode(new Offset(400, -20, pa,
				AnimalScript.DIRECTION_NE), "code", null, scProps);
		sc.addCodeLine("Verschlüsselung vom Plaintext:", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("Bestimme SecretABC durch Verschiebung vom PlainABC "
				+ "um key Stellen nach links", null, 0, null);
		sc.addCodeLine("Für jeden Buchstaben c aus dem Plaintext", null, 0, null);
		sc.addCodeLine("Finde c im PlainABC", null, 1, null);
		sc.addCodeLine("Finde den Buchstaben mit dem gleichen Index im SecretABC",
				null, 1, null);
		sc.addCodeLine(
				"Schreibe den gefundenen verschlüssenden Buchstaben aus dem "
						+ "SecretABC ins Ciphertext an die Position vom c", null, 1, null);

		lang.nextStep();

		StringArray pABC = lang.newStringArray(new Coordinates(20, 220), PLAIN_ABC,
				"plainABC", null, plainABCArrayProps);

		ArrayMarker am_pABC = lang.newArrayMarker(pABC, 0, "am_pABC", null,
				amPlainProps);
		am_pABC.hide(null);
		hidden_am_pABC = true;

		Text pABC_label = lang.newText(new Offset(20, 8, pABC,
				AnimalScript.DIRECTION_E), "PlainABC", "PlainABC", null, textProps);

		String[] secretABC = new String[26];

		for (int i = 0; i < secretABC.length; i++) {
			secretABC[(i - key + 26) % 26] = PLAIN_ABC[i];
		}

		lang.nextStep();

		lang.newText(new Offset(0, 50, pABC_label, AnimalScript.DIRECTION_NW),
				"key = " + key, "key", null, textProps);
		pABC.highlightCell(key, pABC.getLength() - 1, delay2, delay);

		StringArray sABC = lang.newStringArray(new Coordinates(20, 320), secretABC,
				"secretABC", new ArrayDisplayOptions(delay2, delay, false),
				secretABCArrayProps);
		sABC.highlightCell(0, sABC.getLength() - key - 1, delay2, delay);

		ArrayMarker am_sABC = lang.newArrayMarker(sABC, 0, "am_sABC", delay2,
				amSecretProps);
		am_sABC.hide(delay2);
		hidden_am_sABC = true;

		lang.newText(new Offset(20, 8, sABC, AnimalScript.DIRECTION_E),
				"SecretABC", "SecretABC", delay2, textProps);

		sc.highlight(2);

		PolylineProperties plp = new PolylineProperties();
		plp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		Node[] nodes1 = new Node[2];
		nodes1[0] = new Offset(key * width, 0, pABC, AnimalScript.DIRECTION_SW);
		nodes1[1] = sABC.getUpperLeft();
		Polyline pl1 = lang.newPolyline(nodes1, "", delay2, plp);

		Node[] nodes2 = new Node[2];
		nodes2[0] = new Offset(0, 0, pABC, AnimalScript.DIRECTION_SE);
		nodes2[1] = new Offset(-key * width, 40, sABC, AnimalScript.DIRECTION_NE);
		Polyline pl2 = lang.newPolyline(nodes2, "", delay2, plp);

		lang.nextStep();

		pABC.unhighlightCell(key, pABC.getLength() - 1, null, null);
		sABC.unhighlightCell(0, sABC.getLength() - key - 1, null, null);
		sc.unhighlight(2);
		pl1.hide();
		pl2.hide();

		String[] chiffreArray = new String[plainArray.length];
		for (int i = 0; i < chiffreArray.length; i++) {
			chiffreArray[i] = "...";
		}

		StringArray ca = lang.newStringArray(new Coordinates(20, 420),
				chiffreArray, "chiffreArray", null, ciphertextArrayProps);

		lang.newText(new Offset(20, 0, ca, AnimalScript.DIRECTION_NE),
				"Ciphertext", "ChiffreArray", null, textProps);

		for (int i = 0; i < chiffreArray.length; i++) {
			lang.nextStep();

			sc.highlight(3);
			pa.highlightCell(i, null, null);
			int index = plainArray[i].toLowerCase().codePointAt(0) - 97;
			pa.highlightElem(i, null, null);

			lang.nextStep();

			sc.toggleHighlight(3, 4);
			pa.unhighlightElem(i, null, null);
			// ignore characters that are not [a-z][A-Z]
			if (index < 0 || index > 122) {
				// space... 32-97
				if (index == -65) {
					ca.put(i, " ", null, null);
					// chiffre += chiffreArray[i];
				}
				continue;
			}
			am_pABC.move(index, null, null);
			if (hidden_am_pABC)
				am_pABC.show();
			pABC.highlightElem(index, null, null);
			pABC.highlightCell(index, null, null);

			lang.nextStep();

			sc.toggleHighlight(4, 0, false, 5, 0, delay, null);
			am_sABC.move(index, null, null);
			if (hidden_am_sABC)
				am_sABC.show(null);
			sABC.highlightElem(index, delay, null);
			sABC.highlightCell(index, null, null);
			pABC.unhighlightElem(index, delay, null);

			lang.nextStep();

			sc.toggleHighlight(5, 6);
			sABC.unhighlightElem(index, null, null);
			ca.put(i, sABC.getData(index), null, null);
			ca.highlightCell(i, null, null);
			pABC.unhighlightCell(index, null, null);
			sABC.unhighlightCell(index, null, null);

			lang.nextStep();

			sc.unhighlight(6);
		}
	}

	/**
	 * Erhaelt die vom Nutzer getaetigten Einstellungen und liefert den
	 * Animationscode als <code>java.lang.String</code> zurueck.
	 * 
	 * @return Der Animationscode als <code>java.lang.String</code>.
	 * 
	 * @see generators.framework.Generator#generate(generators.framework.properties.AnimationPropertiesContainer,
	 *      java.util.Hashtable)
	 */
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		textProps = (TextProperties) props.getPropertiesByName("text");
		plaintextArrayProps = (ArrayProperties) props
				.getPropertiesByName("plaintextArrayProps");
		plainABCArrayProps = (ArrayProperties) props
				.getPropertiesByName("plainABCArrayProps");
		secretABCArrayProps = (ArrayProperties) props
				.getPropertiesByName("secretABCArrayProps");
		ciphertextArrayProps = (ArrayProperties) props
				.getPropertiesByName("ciphertextArrayProps");
		amPlainProps = (ArrayMarkerProperties) props
				.getPropertiesByName("plainABCMarker");
		amSecretProps = (ArrayMarkerProperties) props
				.getPropertiesByName("secretABCMarker");
		scProps = (SourceCodeProperties) props.getPropertiesByName("code");
		String text = (String) primitives.get("Plaintext");
		int key = (Integer) primitives.get("key");
		encode(text, key);
		lang.finalizeGeneration();
		return lang.getAnimationCode();
	}

	/**
	 * Gibt den Basisnamen des Algorithmus zurueck.
	 * 
	 * @return Der Basisname des Algorithmus.
	 * 
	 * @see generators.framework.Generator#getAlgorithmName()
	 */
	public String getAlgorithmName() {
    return "Caesar Cipher";
	}

	/**
	 * Liefert den verwendeten Sourcecode.
	 * 
	 * @return Der Sourcecode.
	 * 
	 * @see generators.framework.Generator#getCodeExample()
	 */
	public String getCodeExample() {
		return CODE_EXAMPLE;
	}

	/**
	 * Liefert die <code>java.util.Locale</code> der Inhalte.
	 * 
	 * @return die <code>java.util.Locale</code> der Inhalte.
	 * 
	 * @see generators.framework.Generator#getContentLocale()
	 */
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	/**
	 * Liefert eine kurze textuelle Beschreibung des Algorithmus.
	 * 
	 * @return Beschreibung des Algorithmus.
	 * 
	 * @see generators.framework.Generator#getDescription()
	 */
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * Liefert die Dateiendung.
	 * 
	 * @return die Dateiendung.
	 * 
	 * @see generators.framework.Generator#getFileExtension()
	 */
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	/**
	 * Liefert den Typ des Algorithmus zurueck.
	 * 
	 * @return Der Typ des Algorithmus.
	 * 
	 * @see generators.framework.Generator#getGeneratorType()
	 */
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	/**
	 * Gibt den Namen des Generators zurueck.
	 * 
	 * @return der Name des Generators.
	 * 
	 * @see generators.framework.Generator#getName()
	 */
	public String getName() {
		return "Caesar Chiffre";
	}

	/**
	 * Definiert die Ausgabesprache.
	 * 
	 * @return die Ausgabesprache.
	 * 
	 * @see generators.framework.Generator#getOutputLanguage()
	 */
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public String getAnimationAuthor() {
		return "Dmytro Vronskyi";
	}

	public void init() {
		// TODO Auto-generated method stub

	}
}