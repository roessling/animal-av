package generators.cryptography;

import generators.cryptography.helpers.TextArray;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class DES {
	private Locale language;
	protected Language lang;
	private ArrayProperties arrayProps;
	private SourceCodeProperties sourceCodeProps;
	private TextProperties textProps;
	private int arrays = 0, shifts = 0, assignments = 0;
	private Text arrays_text = null, shifts_text = null,
			assignments_text = null;

	public DES(Locale language) {
		this.language = language;
	}

	public void init(ArrayProperties arrayProps,
			SourceCodeProperties sourceCodeProps, TextProperties textProps) {
		lang = new AnimalScript(
				"DES (Data Encryption Standard)",
				"Andre Daube <andredaube@yahoo.de>, Kokilan Kanesalingam <kajan1@hotmail.de>",
				1600, 1020);
		lang.setStepMode(true);
		this.arrayProps = arrayProps;
		this.sourceCodeProps = sourceCodeProps;
		this.textProps = textProps;
	}

	public void start(String input_str, String key_str) {
		if (input_str.length() != 64 || key_str.length() != 64)
			return;
		for (int i = 0; i < input_str.length(); i++)
			if ((input_str.charAt(i) != '0' && input_str.charAt(i) != '1')
					|| (key_str.charAt(i) != '0' && key_str.charAt(i) != '1'))
				return;
		int[] input = new int[64], key = new int[64];
		for (int i = 0; i < input.length; i++) {
			input[i] = Integer.valueOf(String.valueOf(input_str.charAt(i)));
			key[i] = Integer.valueOf(String.valueOf(key_str.charAt(i)));
		}
		Text name_v = lang.newText(new Coordinates(10, 10),
				"DES (Data Encryption Standard)", "name", null);
		Rect name_frame_v = lang.newRect(new Offset(-4, -4, name_v,
				AnimalScript.DIRECTION_NW), new Offset(4, 4, name_v,
				AnimalScript.DIRECTION_SE), "name_frame", null);
		Text arrays_label_text, shifts_label_text, assignments_label_text;
		if (language == Locale.GERMAN) {
			arrays_label_text = lang.newText(new Coordinates(300, 10),
					"Angelegte Arrays:", "arrays_label", null);
			arrays_label_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			arrays_text = lang.newText(new Offset(10, 0, arrays_label_text,
					AnimalScript.DIRECTION_NE), "0", "arrays", null);
			arrays_text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color.RED, null, null);
			Rect arrays_text_frame = lang.newRect(new Offset(-4, -4,
					arrays_label_text, AnimalScript.DIRECTION_NW), new Offset(
					40, 4, arrays_label_text, AnimalScript.DIRECTION_SE),
					"arrays_frame", null);
			arrays_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			shifts_label_text = lang.newText(new Offset(50, 0, arrays_text,
					AnimalScript.DIRECTION_NE), "Anzahl Shifts:",
					"shifts_label", null);
			shifts_label_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			shifts_text = lang.newText(new Offset(10, 0, shifts_label_text,
					AnimalScript.DIRECTION_NE), "0", "shifts", null);
			shifts_text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color.RED, null, null);
			Rect shifts_text_frame = lang.newRect(new Offset(-4, -4,
					shifts_label_text, AnimalScript.DIRECTION_NW), new Offset(
					40, 4, shifts_label_text, AnimalScript.DIRECTION_SE),
					"shifts_frame", null);
			shifts_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			assignments_label_text = lang.newText(new Offset(50, 0,
					shifts_text, AnimalScript.DIRECTION_NE),
					"Anzahl Zuweisungen:", "assignments_label", null);
			assignments_label_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			assignments_text = lang.newText(new Offset(10, 0,
					assignments_label_text, AnimalScript.DIRECTION_NE), "0",
					"assignments", null);
			assignments_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			Rect assignments_text_frame = lang.newRect(new Offset(-4, -4,
					assignments_label_text, AnimalScript.DIRECTION_NW),
					new Offset(40, 4, assignments_label_text,
							AnimalScript.DIRECTION_SE), "assignments_frame",
					null);
			assignments_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
		} else if (language == Locale.ENGLISH) {
			arrays_label_text = lang.newText(new Coordinates(300, 10),
					"Created arrays:", "arrays_label", null);
			arrays_label_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			arrays_text = lang.newText(new Offset(10, 0, arrays_label_text,
					AnimalScript.DIRECTION_NE), "0", "arrays", null);
			arrays_text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color.RED, null, null);
			Rect arrays_text_frame = lang.newRect(new Offset(-4, -4,
					arrays_label_text, AnimalScript.DIRECTION_NW), new Offset(
					40, 4, arrays_label_text, AnimalScript.DIRECTION_SE),
					"arrays_frame", null);
			arrays_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			shifts_label_text = lang.newText(new Offset(50, 0, arrays_text,
					AnimalScript.DIRECTION_NE), "Amount shifts:",
					"shifts_label", null);
			shifts_label_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			shifts_text = lang.newText(new Offset(10, 0, shifts_label_text,
					AnimalScript.DIRECTION_NE), "0", "shifts", null);
			shifts_text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color.RED, null, null);
			Rect shifts_text_frame = lang.newRect(new Offset(-4, -4,
					shifts_label_text, AnimalScript.DIRECTION_NW), new Offset(
					40, 4, shifts_label_text, AnimalScript.DIRECTION_SE),
					"shifts_frame", null);
			shifts_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			assignments_label_text = lang.newText(new Offset(50, 0,
					shifts_text, AnimalScript.DIRECTION_NE),
					"Amount assignments:", "assignments_label", null);
			assignments_label_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			assignments_text = lang.newText(new Offset(10, 0,
					assignments_label_text, AnimalScript.DIRECTION_NE), "0",
					"assignments", null);
			assignments_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			Rect assignments_text_frame = lang.newRect(new Offset(-4, -4,
					assignments_label_text, AnimalScript.DIRECTION_NW),
					new Offset(40, 4, assignments_label_text,
							AnimalScript.DIRECTION_SE), "assignments_frame",
					null);
			assignments_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
		}
		lang.nextStep();
		Text description_title_v = lang.newText(new Coordinates(0, 0), "", "",
				null);
		SourceCode description_v = lang.newSourceCode(new Coordinates(0, 0),
				"", null);
		if (language == Locale.GERMAN) {
			description_title_v = lang.newText(new Offset(0, 50, name_frame_v,
					AnimalScript.DIRECTION_SW), "Funktionsweise",
					"description_title", null);
			description_v = lang.newSourceCode(new Offset(0, 20,
					description_title_v, AnimalScript.DIRECTION_SW),
					"description", null, sourceCodeProps);
			description_v
					.addCodeLine(
							"Bei DES handelt es sich um einen symmetrischen Algorithmus, das heisst zur Ver- und Entschluesselung wird derselbe Schluessel verwendet.",
							"", 0, null);
			description_v
					.addCodeLine(
							"DES funktioniert als Blockchiffre, jeder Block wird also unter Verwendung des Schluessels einzeln chiffriert.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Dabei werden die Daten in 16 Iterationen beziehungsweise Runden von Substitutionen und Transpositionen (Permutation) nach dem Schema von Feistel verwuerfelt.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Die Blockgroesse betraegt 64 Bits, das heisst ein 64-Bit-Block Klartext wird in einen 64-Bit-Block Chiffretext transformiert.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Auch der Schluessel, der diese Transformation kontrolliert, besitzt 64 Bits. Jedoch stehen dem Benutzer von diesen 64 Bits nur 56 Bits zur Verfuegung,",
							"", 0, null);
			description_v
					.addCodeLine(
							"die uebrigen 8 Bits (jeweils ein Bit aus jedem Byte) werden zum Paritaets-Check benoetigt. Die effektive Schluessellaenge betraegt daher nur 56 Bits.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Die Entschluesselung wird mit dem gleichen Algorithmus durchgefuehrt, wobei die einzelnen Rundenschluessel in umgekehrter Reihenfolge verwendet werden.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Auf den 64 Bit Block wird eine initiale Permutation angewandt. Danach wird der Block in zwei Teile aufgeteilt und jeder Teil in ein 32 Bit Register gespeichert.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Die beiden Blockhaelften werden in Folge als linke und rechte Haelfte bezeichnet. Auf die rechte Blockhaelfte wird die Feistel-Funktion angewandt.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Danach wird die rechte Haelfte mit der linken Haelfte XOR verknuepft und das Ergebnis im Register der naechsten Runde fuer die rechte Haelfte gespeichert.",
							"", 0, null);
			description_v
					.addCodeLine(
							"In das linke Register der naechsten Runde wird die urspruengliche rechte Blockhaelfte kopiert. Nach Ende der letzten Runde werden die beiden Haelften",
							"", 0, null);
			description_v
					.addCodeLine(
							"wieder zusammengefuehrt und eine finale Permutation durchgefuehrt. Dabei handelt es sich um die inverse Permutation zur initialen Permutation.",
							"", 0, null);
		} else if (language == Locale.ENGLISH) {
			description_title_v = lang.newText(new Offset(0, 50, name_frame_v,
					AnimalScript.DIRECTION_SW), "Operation",
					"description_title", null);
			description_v = lang.newSourceCode(new Offset(0, 20,
					description_title_v, AnimalScript.DIRECTION_SW),
					"description", null, sourceCodeProps);
			description_v
					.addCodeLine(
							"DES is a symmetric algorithm, which means that for encryption and decryption the same key is used.",
							"", 0, null);
			description_v
					.addCodeLine(
							"It works as a block cipher, each block is encrypted using the key individually.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Within 16 iterations or rounds of substitutions and transpositions (permutations) the data becomes scrambled according to the scheme of Feistel.",
							"", 0, null);
			description_v
					.addCodeLine(
							"The block size is 64 bits, which means a 64-bit plaintext block is transformed into a 64-bit cipher text block.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Also the key, which controls this transformation, consists of 64 bits. Though, only 56 bits of these 64 bits are available for the user.",
							"", 0, null);
			description_v
					.addCodeLine(
							"The remaining 8 bits (one bit for each byte) are required to check the parity. Hence, the effective key length is 56 bits.",
							"", 0, null);
			description_v
					.addCodeLine(
							"The decryption is performed with the same algorithm, except that the single round keys are used in reverse order.",
							"", 0, null);
			description_v
					.addCodeLine(
							"An initial permutation is applied on the 64-bit block. After that, the block is split into two parts and each part is stored in a 32 bit register.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Subsequently the two block parts are named as the left and right half. On the right half the Feistel function is applied.",
							"", 0, null);
			description_v
					.addCodeLine(
							"Then the right half becomes xored with the left half and the result is stored in the right half register of the next round.",
							"", 0, null);
			description_v
					.addCodeLine(
							"The initial right block half is copied to the left half register for the next round. After the last round, the two halves are",
							"", 0, null);
			description_v
					.addCodeLine(
							"back together and a final permutation is performed. It is the inverse permutation of the initial permutation.",
							"", 0, null);
		}
		lang.nextStep();
		description_title_v.hide();
		description_v.hide();
		SourceCode steps_v = lang
				.newSourceCode(new Coordinates(0, 0), "", null);
		Text input_v_text = lang.newText(new Coordinates(0, 0), "", "", null);
		if (language == Locale.GERMAN) {
			steps_v = lang.newSourceCode(new Coordinates(10, 600),
					"pseudo_code", null, sourceCodeProps);
			steps_v.addCodeLine(
					"1.  Permutieren (Mischen) der 64 Eingangsbits mit der Eingangspermutation IP",
					"", 0, null);
			steps_v.addCodeLine(
					"2.  Aufteilen dieser Bits in zwei 32 bit grosse Bloecke (L-Block und R-Block)",
					"", 0, null);
			steps_v.addCodeLine(
					"3.  Expandieren (Erweitern) des R-Blocks (32 bit) auf 48 bit mit Hilfe der Expansionsfunktion E",
					"", 0, null);
			steps_v.addCodeLine(
					"4.  Entfernen der acht Paritaetsbits (0=gerade/1=ungerade Anzahl der gesetzten Bits) des 64 bit grossen Schluessels (56 bit tatsaechlicher Schluessel) und Generierung zwei 28 bit grosser Teilschluessel C und D mittels der Permuted-Choice-Funktion PC1",
					"", 0, null);
			steps_v.addCodeLine(
					"5.  Zyklischer (Alle Bits, die beim Verschrieben rausgehen, kommen auf der entgegengesetzten Seite rein) Linksshift beider Teilschluessel C und D um je ein oder zwei Bits je nach Rundenindex (1 - 16)",
					"", 0, null);
			steps_v.addCodeLine(
					"6.  Erzeugung des 48-bit grossen Rundenschluessels mit der Permuted-Choice-Funktion PC2",
					"", 0, null);
			steps_v.addCodeLine(
					"7.  XOR Verknuepfen des 48-bit grossem Rundenschluessels mit dem auf 48-bit expandierten (erweiterten) R-Block",
					"", 0, null);
			steps_v.addCodeLine(
					"8.  Aufteilen des so entstandenen 48-bit Blocks in acht 6-bit grosse Teilbloecke",
					"", 0, null);
			steps_v.addCodeLine(
					"9.  Nicht-lineare (deshalb schwer zu brechen) Substitution (Ersetzung) dieser Bloecke durch die acht S-Boxen und Kompression (Verkleinerung) auf acht 4-bit Bloecke",
					"", 0, null);
			steps_v.addCodeLine(
					"10. Permutation des so entstandenen 32-bit Blocks durch die Permutation P",
					"", 0, null);
			steps_v.addCodeLine(
					"11. XOR Verknuepfen dieses 32-bit Blocks mit dem L-Block und Speicherung dieses Blocks in dem R-Block fuer die naechste Runde",
					"", 0, null);
			steps_v.addCodeLine(
					"12. Speicherung des urspruenglichen R-Blocks im L-Block fuer naechste Runde",
					"", 0, null);
			steps_v.addCodeLine(
					"13. Nach Abschluss aller 16 Runden Konkatenieren (Zusammenhaengen) des L- und R-Blocks und permutieren dieser 64-bit mit der Ausgangspermutation IP^(-1)",
					"", 0, null);
			input_v_text = lang.newText(new Offset(0, 10, name_frame_v,
					AnimalScript.DIRECTION_SW), "Eingangsbits", "input_text",
					null, textProps);
		} else if (language == Locale.ENGLISH) {
			steps_v = lang.newSourceCode(new Coordinates(10, 600),
					"pseudo_code", null, sourceCodeProps);
			steps_v.addCodeLine(
					"1. Permuting (Mixing) the 64 input bits with the input permutation IP",
					"", 0, null);
			steps_v.addCodeLine(
					"2. Dividing these bits into two 32 bit wide blocks (L- and R-block)",
					"", 0, null);
			steps_v.addCodeLine(
					"3. Expanding (Extending) the R-block (32-bit) on 48 bit using the expansion function E",
					"", 0, null);
			steps_v.addCodeLine(
					"4. Removing the eight parity (0=even/1=odd amount of set bits) bits of the 64 bit key (56-bit real key) and generate two 28-bit wide part keys C and D using the permuted-choice function PC1",
					"", 0, null);
			steps_v.addCodeLine(
					"5. Cyclically (Every bit that goes out while moving, comes in on the opposed side) left shifting both part keys C and D one or two bit wide depending on the round index (1-16)",
					"", 0, null);
			steps_v.addCodeLine(
					"6. Generation of a 48-bit wide round key with the permuted-choice function PC2",
					"", 0, null);
			steps_v.addCodeLine(
					"7. XOR combine the 48-bit large round key with the to 48-bit expanded (extended) R-block",
					"", 0, null);
			steps_v.addCodeLine(
					"8. Dividing the resulting 48-bit block into eight 6-bit wide part blocks",
					"", 0, null);
			steps_v.addCodeLine(
					"9. Non-linear (therefore hard to break) substitution (replacement) of these blocks by the eight S-boxes and compression to eight 4-bit blocks",
					"", 0, null);
			steps_v.addCodeLine(
					"10. Permutation of the resulting 32-bit block by the permutation P",
					"", 0, null);
			steps_v.addCodeLine(
					"11. XOR combine this 32-bit wide block with the L-block and storing this block in the R-block for the next round",
					"", 0, null);
			steps_v.addCodeLine(
					"12. Storing the originial R-block in the L-block for the next round",
					"", 0, null);
			steps_v.addCodeLine(
					"13. After completing all 16 laps concatenation (connection) of the L-and R-block and permutation of this with the 64-bit output permutation (inverse input permutation) IP^(-1)",
					"", 0, null);
			input_v_text = lang.newText(new Offset(0, 10, name_frame_v,
					AnimalScript.DIRECTION_SW), "Input bits", "input_text",
					null, textProps);
		}
		TextArray input_v = new TextArray(lang, new Coordinates(5, 60), input,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		lang.nextStep();
		steps_v.highlight(0);
		Text initial_permutation_v_text = lang.newText(new Coordinates(0, 0),
				"", "", null);
		if (language == Locale.GERMAN) {
			initial_permutation_v_text = lang.newText(new Coordinates(5, 80),
					"Eingangspermutation", "initial_permutation_text", null,
					textProps);
		} else if (language == Locale.ENGLISH) {
			initial_permutation_v_text = lang.newText(new Coordinates(5, 80),
					"Input permutation", "initial_permutation_text", null,
					textProps);
		}
		int[] initial_permutation = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52,
				44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56,
				48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51,
				43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55,
				47, 39, 31, 23, 15, 7 };
		IncreaseArrays();
		TextArray initial_permutation_v = new TextArray(lang, new Coordinates(
				5, 100), initial_permutation,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		lang.nextStep();
		int r = 0, g = 0, b = 0;
		initial_permutation_v.setColorAtPosition(0, new Color(r, g, b));
		initial_permutation_v.highlight(0);
		input_v.setColorAtPosition(initial_permutation[0] - 1, new Color(r, g,
				b));
		input_v.highlight(initial_permutation[0] - 1);
		lang.nextStep();
		initial_permutation_v.put(0, input[initial_permutation[0] - 1]);
		IncreaseAssignments(1);
		lang.nextStep();
		for (int i = 1; i < initial_permutation.length; i++) {
			initial_permutation_v.unhighlight(i - 1);
			input_v.unhighlight(initial_permutation[i - 1] - 1);
			r += 4;
			g += 4;
			b += 4;
			initial_permutation_v.setColorAtPosition(i, new Color(r, b, g));
			initial_permutation_v.highlight(i);
			input_v.setColorAtPosition(initial_permutation[i] - 1, new Color(r,
					b, g));
			input_v.highlight(initial_permutation[i] - 1);
			lang.nextStep();
			initial_permutation_v.put(i, input[initial_permutation[i] - 1]);
			IncreaseAssignments(1);
			lang.nextStep();
		}
		initial_permutation = initial_permutation_v.getData();
		int round_index = 1;
		input_v_text.hide();
		input_v.hide();
		initial_permutation_v_text.hide();
		steps_v.unhighlight(0);
		steps_v.highlight(1);
		initial_permutation_v.move(AnimalScript.DIRECTION_NW, input_v_text
				.getUpperLeft(), new MsTiming(1000), new MsTiming(1000));
		int[] left = new int[32], right = new int[32];
		for (int i = 0; i < left.length; i++) {
			left[i] = initial_permutation[i];
			right[i] = initial_permutation[i + 32];
		}
		TextArray left_v = new TextArray(lang, new Coordinates(5, 100), left,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY)),
		// Den rechten Teil weiter nach rechts schieben, da das Array nun
		// aufgrund der �nderung der Klasse TextArray gr��er ist.
		right_v = new TextArray(lang, new Coordinates(645, 100), right,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		initial_permutation_v.hide();
		// Den rechten Teil weiter nach rechts schieben, da das Array nun
		// aufgrund der �nderung der Klasse TextArray gr��er ist.
		right_v.move(AnimalScript.DIRECTION_SW, new Coordinates(665, 100),
				new MsTiming(1000), new MsTiming(1000));
		lang.nextStep();
		steps_v.unhighlight(1);
		steps_v.highlight(2);
		left_v.hide();
		right_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 60),
				new MsTiming(1000), new MsTiming(1000));
		Text expansion_v_text = lang.newText(new Coordinates(5, 85),
				"Expansion E", "expansion_text", null, textProps);
		expansion_v_text.hide();
		expansion_v_text.show(new MsTiming(2000));
		int[] expansion = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11,
				12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21,
				22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };
		IncreaseArrays();
		TextArray expansion_v = new TextArray(lang, new Coordinates(5, 110),
				expansion,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		expansion_v.hide();
		expansion_v.show(new MsTiming(2000));
		lang.nextStep();
		r = 0;
		g = 0;
		b = 0;
		expansion_v.setColorAtPosition(0, new Color(r, g, b));
		expansion_v.highlight(0);
		right_v.setColorAtPosition(expansion[0] - 1, new Color(r, g, b));
		right_v.highlight(expansion[0] - 1);
		lang.nextStep();
		expansion_v.put(0, right[expansion[0] - 1]);
		IncreaseAssignments(1);
		lang.nextStep();
		for (int i = 1; i < expansion.length; i++) {
			expansion_v.unhighlight(i - 1);
			right_v.unhighlight(expansion[i - 1] - 1);
			r += 5;
			g += 5;
			b += 5;
			expansion_v.setColorAtPosition(i, new Color(r, g, b));
			expansion_v.highlight(i);
			right_v.setColorAtPosition(expansion[i] - 1, new Color(r, g, b));
			right_v.highlight(expansion[i] - 1);
			lang.nextStep();
			expansion_v.put(i, right[expansion[i] - 1]);
			IncreaseAssignments(1);
			lang.nextStep();
		}
		expansion = expansion_v.getData();
		right_v.hide();
		expansion_v_text.hide();
		steps_v.unhighlight(2);
		steps_v.highlight(3);
		expansion_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 60),
				new MsTiming(1000), new MsTiming(1000));
		expansion_v.hide(new MsTiming(2000));
		Text key_v_text = lang.newText(new Coordinates(0, 0), "", "", null);
		if (language == Locale.GERMAN) {
			key_v_text = lang.newText(new Coordinates(5, 40), "Schluessel",
					"key_text", null, textProps);
		} else if (language == Locale.ENGLISH) {
			key_v_text = lang.newText(new Coordinates(5, 40), "Key",
					"key_text", null, textProps);
		}
		key_v_text.hide();
		key_v_text.show(new MsTiming(2000));
		TextArray key_v = new TextArray(lang, new Coordinates(5, 60), key,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		key_v.hide();
		key_v.show(new MsTiming(2000));
		lang.nextStep();
		int[] c = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10,
				2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36 }, d = { 63,
				55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61,
				53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };
		IncreaseArrays();
		IncreaseArrays();
		Text pc1_text = lang.newText(new Coordinates(0, 0), "", "", null);
		if (language == Locale.GERMAN) {
			pc1_text = lang
					.newText(new Coordinates(5, 80),
							"Permuted-Choice-Funktion PC1", "pc1_text", null,
							textProps);
		} else if (language == Locale.ENGLISH) {
			pc1_text = lang
					.newText(new Coordinates(5, 80),
							"Permuted-choice-function PC1", "pc1_text", null,
							textProps);
		}
		Text c_v_text = lang.newText(new Coordinates(5, 100), "C", "c_text",
				null, textProps);
		TextArray c_v = new TextArray(lang, new Coordinates(5, 120), c,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		Text d_v_text = lang.newText(new Coordinates(5, 140), "D", "d_text",
				null, textProps);
		TextArray d_v = new TextArray(lang, new Coordinates(5, 160), d,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		lang.nextStep();
		r = 0;
		g = 0;
		b = 0;
		c_v.setColorAtPosition(0, new Color(r, g, b));
		c_v.highlight(0);
		key_v.setColorAtPosition(c[0] - 1, new Color(r, g, b));
		key_v.highlight(c[0] - 1);
		lang.nextStep();
		c_v.put(0, key[c[0] - 1]);
		IncreaseAssignments(1);
		lang.nextStep();
		for (int i = 1; i < c.length; i++) {
			c_v.unhighlight(i - 1);
			key_v.unhighlight(c[i - 1] - 1);
			r += 4;
			g += 4;
			b += 4;
			c_v.setColorAtPosition(i, new Color(r, g, b));
			c_v.highlight(i);
			key_v.setColorAtPosition(c[i] - 1, new Color(r, g, b));
			key_v.highlight(c[i] - 1);
			lang.nextStep();
			c_v.put(i, key[c[i] - 1]);
			IncreaseAssignments(1);
			lang.nextStep();
		}
		c_v.unhighlight(c_v.size() - 1);
		key_v.unhighlight(c[c_v.size() - 1] - 1);
		c = c_v.getData();
		d_v.setColorAtPosition(0, new Color(r, g, b));
		d_v.highlight(0);
		key_v.highlight(d[0] - 1);
		lang.nextStep();
		d_v.put(0, key[d[0] - 1]);
		IncreaseAssignments(1);
		lang.nextStep();
		for (int i = 1; i < d.length; i++) {
			d_v.unhighlight(i - 1);
			key_v.unhighlight(d[i - 1] - 1);
			r += 4;
			g += 4;
			b += 4;
			d_v.setColorAtPosition(i, new Color(r, g, b));
			d_v.highlight(i);
			key_v.setColorAtPosition(d[i] - 1, new Color(r, g, b));
			key_v.highlight(d[i] - 1);
			lang.nextStep();
			d_v.put(i, key[d[i] - 1]);
			IncreaseAssignments(1);
			lang.nextStep();
		}
		d = d_v.getData();
		key_v_text.hide();
		key_v.hide();
		pc1_text.hide();
		c_v_text.hide();
		d_v_text.hide();
		steps_v.unhighlight(3);
		steps_v.highlight(4);
		c_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 60),
				new MsTiming(1000), new MsTiming(1000));
		d_v.move(AnimalScript.DIRECTION_NW, new Coordinates(425, 60),
				new MsTiming(1000), new MsTiming(1000));
		lang.nextStep();
		int[] shift_indices = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
		int tmp1, tmp2, tmp3, tmp4;
		if (shift_indices[round_index - 1] == 1) {
			tmp1 = c[0];
			tmp3 = d[0];
			IncreaseAssignments(2);
			for (int i = 1; i < c.length; i++) {
				c[i - 1] = c[i];
				d[i - 1] = d[i];
				IncreaseAssignments(2);
			}
			c[c.length - 1] = tmp1;
			d[d.length - 1] = tmp3;
			IncreaseAssignments(2);
		} else if (shift_indices[round_index - 1] == 2) {
			tmp1 = c[0];
			tmp2 = c[1];
			tmp3 = d[0];
			tmp4 = d[1];
			IncreaseAssignments(4);
			for (int i = 2; i < c.length; i++) {
				c[i - 2] = c[i];
				d[i - 2] = d[i];
				IncreaseAssignments(2);
			}
			c[c.length - 2] = tmp1;
			c[c.length - 1] = tmp2;
			d[d.length - 2] = tmp3;
			d[d.length - 1] = tmp4;
			IncreaseAssignments(4);
		}
		IncreaseShifts();
		TextArray c_shifted_v = new TextArray(lang, new Coordinates(5, 80), c,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY)), d_shifted_v = new TextArray(
				lang, new Coordinates(425, 80), d,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		Text c_shifted_v_text = lang.newText(new Coordinates(0, 0), "", "",
				null), d_shifted_v_text = lang.newText(new Coordinates(0, 0),
				"", "", null);
		if (language == Locale.GERMAN) {
			c_shifted_v_text = lang.newText(new Coordinates(5, 100),
					"C um 1 bit nach links geshifted", "c_shifted_text", null,
					textProps);
			d_shifted_v_text = lang.newText(new Coordinates(425, 100),
					"D um 1 bit nach links geshifted", "d_shifted_text", null,
					textProps);
		} else if (language == Locale.ENGLISH) {
			c_shifted_v_text = lang.newText(new Coordinates(5, 100),
					"C left shifted with 1 bit", "c_shifted_text", null,
					textProps);
			d_shifted_v_text = lang.newText(new Coordinates(425, 100),
					"D left shifted with 1 bit", "d_shifted_text", null,
					textProps);
		}
		lang.nextStep();
		int[] round_key = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19,
				12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30,
				40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29,
				32 };
		IncreaseArrays();
		c_v.hide();
		d_v.hide();
		c_shifted_v_text.hide();
		d_shifted_v_text.hide();
		steps_v.unhighlight(4);
		steps_v.highlight(5);
		c_shifted_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 60),
				new MsTiming(1000), new MsTiming(1000));
		d_shifted_v.move(AnimalScript.DIRECTION_NW, new Coordinates(425, 60),
				new MsTiming(1000), new MsTiming(1000));
		lang.nextStep();
		Text pc2_text = lang.newText(new Coordinates(0, 0), "", "", null);
		if (language == Locale.GERMAN) {
			pc2_text = lang.newText(new Coordinates(5, 80),
					"Permuted-Choice-Funktion PC2", "c_shifted_text", null,
					textProps);
		} else if (language == Locale.ENGLISH) {
			pc2_text = lang.newText(new Coordinates(5, 80),
					"Permuted-choice-function PC2", "c_shifted_text", null,
					textProps);
		}
		TextArray round_key_v = new TextArray(lang, new Coordinates(5, 100),
				round_key,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		lang.nextStep();
		r = 0;
		g = 0;
		b = 0;
		round_key_v.setColorAtPosition(0, new Color(r, g, b));
		round_key_v.highlight(0);
		c_shifted_v.setColorAtPosition(round_key[0] - 1, new Color(r, g, b));
		c_shifted_v.highlight(round_key[0] - 1);
		lang.nextStep();
		round_key_v.put(0, c[round_key[0] - 1]);
		IncreaseAssignments(1);
		lang.nextStep();
		for (int i = 1; i < round_key.length; i++) {
			if (round_key[i] <= 28) {
				round_key_v.unhighlight(i - 1);
				c_shifted_v.unhighlight(round_key[i - 1] - 1);
				r += 5;
				g += 5;
				b += 5;
				round_key_v.setColorAtPosition(i, new Color(r, g, b));
				round_key_v.highlight(i);
				c_shifted_v.setColorAtPosition(round_key[i] - 1, new Color(r,
						g, b));
				c_shifted_v.highlight(round_key[i] - 1);
				lang.nextStep();
				round_key_v.put(i, c[round_key[i] - 1]);
				lang.nextStep();
			} else if (round_key[i] > 28) {
				round_key_v.unhighlight(i - 1);
				d_shifted_v.unhighlight(round_key[i - 1] - 1);
				r += 5;
				g += 5;
				b += 5;
				round_key_v.setColorAtPosition(i, new Color(r, g, b));
				round_key_v.highlight(i);
				d_shifted_v.setColorAtPosition(round_key[i] - 1, new Color(r,
						g, b));
				d_shifted_v.highlight(round_key[i] - 1);
				lang.nextStep();
				round_key_v.put(i, d[round_key[i] - 29]);
				lang.nextStep();
			}
			IncreaseAssignments(1);
		}
		round_key = round_key_v.getData();
		c_shifted_v.hide();
		d_shifted_v.hide();
		pc2_text.hide();
		steps_v.unhighlight(5);
		steps_v.highlight(6);
		round_key_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 60),
				new MsTiming(1000), new MsTiming(1000));
		lang.nextStep();
		Text xor_text = lang.newText(new Coordinates(5, 80), "XOR", "xor_text",
				null, textProps);
		expansion_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 100),
				null, null);
		expansion_v.show();
		lang.nextStep();
		int[] xored = new int[48];
		for (int i = 0; i < expansion.length; i++) {
			xored[i] = xor(expansion[i], round_key[i]);
			IncreaseAssignments(1);
		}
		IncreaseArrays();
		Text equals_text = lang.newText(new Coordinates(5, 120), "=",
				"equals_text", null, textProps);
		TextArray xored_v = new TextArray(lang, new Coordinates(5, 140), xored,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		lang.nextStep();
		expansion_v.hide();
		xor_text.hide();
		round_key_v.hide();
		equals_text.hide();
		steps_v.unhighlight(6);
		steps_v.highlight(7);
		xored_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 60),
				new MsTiming(1000), new MsTiming(1000));
		int[] s1 = new int[6], s2 = new int[6], s3 = new int[6], s4 = new int[6], s5 = new int[6], s6 = new int[6], s7 = new int[6], s8 = new int[6];
		for (int i = 0; i < s1.length; i++) {
			s1[i] = xored[i];
			s2[i] = xored[i + 6];
			s3[i] = xored[i + 12];
			s4[i] = xored[i + 18];
			s5[i] = xored[i + 24];
			s6[i] = xored[i + 30];
			s7[i] = xored[i + 36];
			s8[i] = xored[i + 42];
		}
		int[] compressed = new int[32];
		for (int i = 0; i < 4; i++) {
			compressed[i] = s(1, s1)[i];
			compressed[i + 4] = s(2, s2)[i];
			compressed[i + 8] = s(3, s3)[i];
			compressed[i + 12] = s(4, s4)[i];
			compressed[i + 16] = s(5, s5)[i];
			compressed[i + 20] = s(6, s6)[i];
			compressed[i + 24] = s(7, s7)[i];
			compressed[i + 28] = s(8, s8)[i];
			IncreaseAssignments(48);
		}
		IncreaseArrays();
		lang.nextStep();
		steps_v.unhighlight(7);
		steps_v.highlight(8);
		Text s_box_substitution_text = lang.newText(new Coordinates(5, 80),
				"S Box Substitution", "s_box_substitution_text", null,
				textProps);
		TextArray compressed_v = new TextArray(lang, new Coordinates(5, 100),
				compressed,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		lang.nextStep();
		xored_v.hide();
		s_box_substitution_text.hide();
		steps_v.unhighlight(8);
		steps_v.highlight(9);
		compressed_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 60),
				new MsTiming(1000), new MsTiming(1000));
		lang.nextStep();
		int[] permutation = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5,
				18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11,
				4, 25 }, new_right = new int[32];
		IncreaseArrays();
		IncreaseArrays();
		Text permutation_text = lang.newText(new Coordinates(5, 80),
				"Permutation P", "permutation_text", null, textProps);
		TextArray permutation_v = new TextArray(lang, new Coordinates(5, 100),
				permutation,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		r = 0;
		g = 0;
		b = 0;
		permutation_v.setColorAtPosition(0, new Color(r, g, b));
		permutation_v.highlight(0);
		compressed_v.setColorAtPosition(permutation[0] - 1, new Color(r, g, b));
		compressed_v.highlight(permutation[0] - 1);
		lang.nextStep();
		permutation_v.put(0, compressed[permutation[0] - 1]);
		IncreaseAssignments(1);
		lang.nextStep();
		for (int i = 1; i < permutation.length; i++) {
			permutation_v.unhighlight(i - 1);
			compressed_v.unhighlight(permutation[i - 1] - 1);
			r += 8;
			g += 8;
			b += 8;
			permutation_v.setColorAtPosition(i, new Color(r, g, b));
			permutation_v.highlight(i);
			compressed_v.setColorAtPosition(permutation[i] - 1, new Color(r, g,
					b));
			compressed_v.highlight(permutation[i] - 1);
			lang.nextStep();
			permutation_v.put(i, compressed[permutation[i] - 1]);
			IncreaseAssignments(1);
			lang.nextStep();
		}
		permutation = permutation_v.getData();
		for (int i = 0; i < permutation.length; i++) {
			new_right[i] = xor(left[i], permutation[i]);
			IncreaseAssignments(1);
		}
		compressed_v.hide();
		permutation_text.hide();
		steps_v.unhighlight(9);
		steps_v.highlight(10);
		permutation_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 60),
				new MsTiming(1000), new MsTiming(1000));
		lang.nextStep();
		try {
			xor_text.moveTo(AnimalScript.DIRECTION_NW, "translate",
					new Coordinates(5, 80), null, null);
			equals_text.moveTo(AnimalScript.DIRECTION_NW, "translate",
					new Coordinates(5, 120), null, null);
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		left_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 100), null,
				null);
		xor_text.show();
		left_v.show();
		lang.nextStep();
		equals_text.show();
		TextArray new_right_v = new TextArray(lang, new Coordinates(5, 140),
				new_right,
				(Font) arrayProps.get(AnimationPropertiesKeys.FONT_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
				(Color) arrayProps.get(AnimationPropertiesKeys.FILL_PROPERTY),
				(Color) arrayProps
						.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		lang.nextStep();
		permutation_v.hide();
		xor_text.hide();
		left_v.hide();
		equals_text.hide();
		steps_v.unhighlight(10);
		steps_v.highlight(11);
		// Den rechten Teil weiter nach rechts schieben, da das Array nun
		// aufgrund der �nderung der Klasse TextArray gr��er ist.
		new_right_v.move(AnimalScript.DIRECTION_NW, new Coordinates(645, 140),
				new MsTiming(1000), new MsTiming(1000));
		right_v.move(AnimalScript.DIRECTION_NW, new Coordinates(5, 140),
				new MsTiming(1000), new MsTiming(1000));
		lang.nextStep();
		right_v.show();
		lang.nextStep();
		right_v.hide();
		new_right_v.hide();
		steps_v.unhighlight(11);
		steps_v.highlight(12);
		steps_v.hide(new MsTiming(2000));
		SourceCode last_words_v = lang.newSourceCode(new Coordinates(0, 0), "",
				null);
		Text complexity_text = lang
				.newText(new Coordinates(0, 0), "", "", null), memory_usage_text = lang
				.newText(new Coordinates(0, 0), "", "", null);
		if (language == Locale.GERMAN) {
			last_words_v = lang.newSourceCode(new Offset(0, 50, name_frame_v,
					AnimalScript.DIRECTION_SW), "last_words", null,
					sourceCodeProps);
			last_words_v
					.addCodeLine(
							"Dieser Vorgang wird nun noch weitere 15 Male wiederholt und der 64-bit Block nach Beendigung des letzten Vorgangs ist dann der verschluesselte Block.",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"Da die Rechenleistung in den letzten Jahren gestiegen und billiger geworden ist, laesst sich der effektive 56 Bits grosse DES-Schluessel, fuer den es 2 hoch 56 Moeglichkeiten gibt, leicht brechen.",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"Deshalb hat man spaeter die Triple DES Chiffre entwickelt bei der man drei DES-Schluessel verwendet. Man verschluesselt den Block mit dem ersten Schluessel, entschluesselt ihn mit dem zweiten und",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"verschluesselt ihn mit dem dritten nochmals. Auf diese Weise wird die effektive Schluessellaenge auf 168 Bits erhoeht. Mit Hilfe des Meet-in-the-Middle Angriffs kann man jedoch einen der drei Schluessel",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"genauso schnell brechen wie bei der DES Chiffre, die effektive Schluessellaenge betraegt also auch bei Triple DES nur 112 Bits. Deshalb hat sich heute das AES-Verschluesselungsverfahren durchgesetzt bei",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"der man eine effektive Schluessellaenge von 128, 192 oder 256 Bits hat.",
							"", 0, null);
			complexity_text = lang.newText(new Coordinates(10, 500),
					"Komplexitaet: O(n)", "", null);
			complexity_text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color.RED, null, null);
			Rect complexity_text_frame = lang.newRect(new Offset(-4, -4,
					complexity_text, AnimalScript.DIRECTION_NW), new Offset(4,
					4, complexity_text, AnimalScript.DIRECTION_SE),
					"complexity_text_frame", null);
			complexity_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			memory_usage_text = lang
					.newText(
							new Offset(20, 0, complexity_text,
									AnimalScript.DIRECTION_NE),
							"Benoetigter Arbeitsspeicher: 9 x 8 Bytes (pro 64-bit Array) = 72 Bytes",
							"", null);
			memory_usage_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			Rect memory_usage_text_frame = lang.newRect(new Offset(-4, -4,
					memory_usage_text, AnimalScript.DIRECTION_NW), new Offset(
					4, 4, memory_usage_text, AnimalScript.DIRECTION_SE),
					"memory_usage_text_frame", null);
			memory_usage_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
		} else if (language == Locale.ENGLISH) {
			last_words_v = lang.newSourceCode(new Offset(0, 50, name_frame_v,
					AnimalScript.DIRECTION_SW), "last_words", null,
					sourceCodeProps);
			last_words_v
					.addCodeLine(
							"This procedure is repeated 15 times and the 64-bit block after the last operation is then the final encoded block.",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"Since the computing power has increased in the last years and has become cheaper, the effective 56-bit wide DES key, for which there are 2 raised to 56 opportunities, can be broken easily.",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"This is why the triple DES cipher was developed later for which you use three DES keys. The block is encrypted with the first key, decrypted with the second one",
							"", 0, null);
			last_words_v
					.addCodeLine(
							" and encrypted with the third one again. Thus, the effective key length is increased to 168 bit. However, using the meet-in-the-middle attack, you can break one of the three keys",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"as fast as the DES cipher, so the effective key length only amounts to 112 bit also for Triple DES. For this reason, the AES encryption process,",
							"", 0, null);
			last_words_v
					.addCodeLine(
							"which has an effective key length of 128, 192 or 256 bit, has successfully established.",
							"", 0, null);
			complexity_text = lang.newText(new Coordinates(10, 500),
					"Complexity: O(n)", "", null);
			complexity_text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color.RED, null, null);
			Rect complexity_text_frame = lang.newRect(new Offset(-4, -4,
					complexity_text, AnimalScript.DIRECTION_NW), new Offset(4,
					4, complexity_text, AnimalScript.DIRECTION_SE),
					"complexity_text_frame", null);
			complexity_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			memory_usage_text = lang
					.newText(
							new Offset(20, 0, complexity_text,
									AnimalScript.DIRECTION_NE),
							"Required memory: 9 x 8 Bytes (pro 64-bit Array) = 72 Bytes",
							"", null);
			memory_usage_text.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
			Rect memory_usage_text_frame = lang.newRect(new Offset(-4, -4,
					memory_usage_text, AnimalScript.DIRECTION_NW), new Offset(
					4, 4, memory_usage_text, AnimalScript.DIRECTION_SE),
					"memory_usage_text_frame", null);
			memory_usage_text_frame.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null,
					null);
		}
		return;
	}

	private int[] StringToIntArray(String str) {
		int[] array = new int[4];
		char[] str_arr = str.toCharArray();
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.valueOf(String.valueOf(str_arr[i]));
		}
		return array;
	}

	private void IncreaseArrays() {
		if (arrays_text != null) {
			arrays++;
			arrays_text.setText(String.valueOf(arrays), null, null);
		}
	}

	private void IncreaseShifts() {
		if (shifts_text != null) {
			shifts++;
			shifts_text.setText(String.valueOf(shifts), null, null);
		}
	}

	private void IncreaseAssignments(int amount) {
		if (assignments_text != null) {
			assignments += amount;
			assignments_text.setText(String.valueOf(assignments), null, null);
		}
	}

	private int xor(int a, int b) {
		if (a == 0 && b == 0)
			return 0;
		else if (a == 0 && b == 1)
			return 1;
		else if (a == 1 && b == 0)
			return 1;
		else if (a == 1 && b == 1)
			return 0;
		else
			return 0;
	}

	private int[] s(int j, int[] values) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < values.length - 1; i++) {
			sb.append(values[i]);
		}
		String middle_vals = sb.toString();
		int s_index = 0;
		String[] s = { "0000", "0001", "0010", "0011", "0100", "0101", "0110",
				"0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110",
				"1111" };
		if (j == 1) {
			String[] s_1 = { "1110", "0100", "1101", "0001", "0010", "1111",
					"1011", "1000", "0011", "1010", "0110", "1100", "0101",
					"1001", "0000", "0111" };
			String[] s_2 = { "0000", "1111", "0111", "0100", "1110", "0010",
					"1101", "0001", "1010", "0110", "1100", "1011", "1001",
					"0101", "0011", "1000" };
			String[] s_3 = { "0100", "0001", "1110", "1000", "1101", "0110",
					"0010", "1011", "1111", "1100", "1001", "0111", "0011",
					"1010", "0101", "0000" };
			String[] s_4 = { "1111", "1100", "1000", "0010", "0100", "1001",
					"0001", "0111", "0101", "1011", "0011", "1110", "1010",
					"0000", "0110", "1101" };
			for (int i = 0; i < s.length; i++) {
				if (s[i] == middle_vals) {
					s_index = i;
					break;
				}
			}
			if (values[0] == 0 && values[5] == 0)
				return StringToIntArray(s_1[s_index]);
			else if (values[0] == 0 && values[5] == 1)
				return StringToIntArray(s_2[s_index]);
			else if (values[0] == 1 && values[5] == 0)
				return StringToIntArray(s_3[s_index]);
			else if (values[0] == 1 && values[5] == 1)
				return StringToIntArray(s_4[s_index]);
		} else if (j == 2) {
			String[] s_1 = { "1111", "0001", "1000", "1110", "0110", "1011",
					"0011", "0100", "1001", "0111", "0010", "1101", "1100",
					"0000", "0101", "1010" };
			String[] s_2 = { "0011", "1101", "0100", "0111", "1111", "0010",
					"1000", "1110", "1100", "0000", "0001", "1010", "0110",
					"1001", "1011", "0101" };
			String[] s_3 = { "0000", "1110", "0111", "1011", "1010", "0100",
					"1101", "0001", "0101", "1000", "1100", "0110", "1001",
					"0011", "0010", "1111" };
			String[] s_4 = { "1101", "1000", "1010", "0001", "0011", "1111",
					"0100", "0010", "1011", "0110", "0111", "1100", "0000",
					"0101", "1110", "1001" };
			for (int i = 0; i < s.length; i++) {
				if (s[i] == middle_vals) {
					s_index = i;
					break;
				}
			}
			if (values[0] == 0 && values[5] == 0)
				return StringToIntArray(s_1[s_index]);
			else if (values[0] == 0 && values[5] == 1)
				return StringToIntArray(s_2[s_index]);
			else if (values[0] == 1 && values[5] == 0)
				return StringToIntArray(s_3[s_index]);
			else if (values[0] == 1 && values[5] == 1)
				return StringToIntArray(s_4[s_index]);
		} else if (j == 3) {
			String[] s_1 = { "1010", "0000", "1001", "1110", "0110", "0011",
					"1111", "0101", "0001", "1101", "1100", "0111", "1011",
					"0100", "0010", "1000" };
			String[] s_2 = { "1101", "0111", "0000", "1001", "0011", "0100",
					"0110", "1010", "0010", "1000", "0101", "1110", "1100",
					"1011", "1111", "0001" };
			String[] s_3 = { "1101", "0110", "0100", "1001", "1000", "1111",
					"0011", "0000", "1011", "0001", "0010", "1100", "0101",
					"1010", "1110", "0111" };
			String[] s_4 = { "0001", "1010", "1101", "0000", "0110", "1001",
					"1000", "0111", "0100", "1111", "1110", "0011", "1011",
					"0101", "0010", "1100" };
			for (int i = 0; i < s.length; i++) {
				if (s[i] == middle_vals) {
					s_index = i;
					break;
				}
			}
			if (values[0] == 0 && values[5] == 0)
				return StringToIntArray(s_1[s_index]);
			else if (values[0] == 0 && values[5] == 1)
				return StringToIntArray(s_2[s_index]);
			else if (values[0] == 1 && values[5] == 0)
				return StringToIntArray(s_3[s_index]);
			else if (values[0] == 1 && values[5] == 1)
				return StringToIntArray(s_4[s_index]);
		} else if (j == 4) {
			String[] s_1 = { "0111", "1101", "1110", "0011", "0000", "0110",
					"1001", "1010", "0001", "0010", "1000", "0101", "1011",
					"1100", "0100", "1111" };
			String[] s_2 = { "1101", "1000", "1011", "0101", "0110", "1111",
					"0000", "0011", "0100", "0111", "0010", "1100", "0001",
					"1010", "1110", "1001" };
			String[] s_3 = { "1010", "0110", "1001", "0000", "1100", "1011",
					"0111", "1101", "1111", "0001", "0011", "1110", "0101",
					"0010", "1000", "0100" };
			String[] s_4 = { "0011", "1111", "0000", "0110", "1010", "0001",
					"1101", "1000", "1001", "0100", "0101", "1011", "1100",
					"0111", "0010", "1110" };
			for (int i = 0; i < s.length; i++) {
				if (s[i] == middle_vals) {
					s_index = i;
					break;
				}
			}
			if (values[0] == 0 && values[5] == 0)
				return StringToIntArray(s_1[s_index]);
			else if (values[0] == 0 && values[5] == 1)
				return StringToIntArray(s_2[s_index]);
			else if (values[0] == 1 && values[5] == 0)
				return StringToIntArray(s_3[s_index]);
			else if (values[0] == 1 && values[5] == 1)
				return StringToIntArray(s_4[s_index]);
		} else if (j == 5) {
			String[] s_1 = { "0010", "1100", "0100", "0001", "0111", "1010",
					"1011", "0110", "1000", "0101", "0011", "1111", "1101",
					"0000", "1110", "1001" };
			String[] s_2 = { "1110", "1011", "0010", "1100", "0100", "0111",
					"1101", "0001", "0101", "0000", "1111", "1010", "0011",
					"1001", "1000", "0110" };
			String[] s_3 = { "0100", "0010", "0001", "1011", "1010", "1101",
					"0111", "1000", "1111", "1001", "1100", "0101", "0110",
					"0011", "0000", "1110" };
			String[] s_4 = { "1011", "1000", "1100", "0111", "0001", "1110",
					"0010", "1101", "0110", "1111", "0000", "1001", "1010",
					"0100", "0101", "0011" };
			for (int i = 0; i < s.length; i++) {
				if (s[i] == middle_vals) {
					s_index = i;
					break;
				}
			}
			if (values[0] == 0 && values[5] == 0)
				return StringToIntArray(s_1[s_index]);
			else if (values[0] == 0 && values[5] == 1)
				return StringToIntArray(s_2[s_index]);
			else if (values[0] == 1 && values[5] == 0)
				return StringToIntArray(s_3[s_index]);
			else if (values[0] == 1 && values[5] == 1)
				return StringToIntArray(s_4[s_index]);
		} else if (j == 6) {
			String[] s_1 = { "1100", "0001", "1010", "1111", "1001", "0010",
					"0110", "1000", "0000", "1101", "0011", "0100", "1110",
					"0111", "0101", "1011" };
			String[] s_2 = { "1010", "1111", "0100", "0010", "0111", "1100",
					"1001", "0101", "0110", "0001", "1101", "1110", "0000",
					"1011", "0011", "1000" };
			String[] s_3 = { "1001", "1110", "1111", "0101", "0010", "1000",
					"1100", "0011", "0111", "0000", "0100", "1010", "0001",
					"1101", "1011", "0110" };
			String[] s_4 = { "0100", "0011", "0010", "1100", "1001", "0101",
					"1111", "1010", "1011", "1110", "0001", "0111", "0110",
					"0000", "1000", "1101" };
			for (int i = 0; i < s.length; i++) {
				if (s[i] == middle_vals) {
					s_index = i;
					break;
				}
			}
			if (values[0] == 0 && values[5] == 0)
				return StringToIntArray(s_1[s_index]);
			else if (values[0] == 0 && values[5] == 1)
				return StringToIntArray(s_2[s_index]);
			else if (values[0] == 1 && values[5] == 0)
				return StringToIntArray(s_3[s_index]);
			else if (values[0] == 1 && values[5] == 1)
				return StringToIntArray(s_4[s_index]);
		} else if (j == 7) {
			String[] s_1 = { "0100", "1011", "0010", "1110", "1111", "0000",
					"1000", "1101", "0011", "1100", "1001", "0111", "0101",
					"1010", "0110", "0001" };
			String[] s_2 = { "1101", "0000", "1011", "0111", "0100", "1001",
					"0001", "1010", "1110", "0011", "0101", "1100", "0010",
					"1111", "1000", "0110" };
			String[] s_3 = { "0001", "0100", "1011", "1101", "1100", "0011",
					"0111", "1110", "1010", "1111", "0110", "1000", "0000",
					"0101", "1001", "0010" };
			String[] s_4 = { "0110", "1011", "1101", "1000", "0001", "0100",
					"1010", "0111", "1001", "0101", "0000", "1111", "1110",
					"0010", "0011", "1100" };
			for (int i = 0; i < s.length; i++) {
				if (s[i] == middle_vals) {
					s_index = i;
					break;
				}
			}
			if (values[0] == 0 && values[5] == 0)
				return StringToIntArray(s_1[s_index]);
			else if (values[0] == 0 && values[5] == 1)
				return StringToIntArray(s_2[s_index]);
			else if (values[0] == 1 && values[5] == 0)
				return StringToIntArray(s_3[s_index]);
			else if (values[0] == 1 && values[5] == 1)
				return StringToIntArray(s_4[s_index]);
		} else if (j == 8) {
			String[] s_1 = { "1101", "0010", "1000", "0100", "0110", "1111",
					"1011", "0001", "1010", "1001", "0011", "1110", "0101",
					"0000", "1100", "0111" };
			String[] s_2 = { "0001", "1111", "1101", "1000", "1010", "0011",
					"0111", "0100", "1100", "0101", "0110", "1011", "0000",
					"1110", "1001", "0010" };
			String[] s_3 = { "0111", "1011", "0100", "0001", "1001", "1100",
					"1110", "0010", "0000", "0110", "1010", "1101", "1111",
					"0011", "0101", "1000" };
			String[] s_4 = { "0010", "0001", "1110", "0111", "0100", "1010",
					"1000", "1101", "1111", "1100", "1001", "0000", "0011",
					"0101", "0110", "1011" };
			for (int i = 0; i < s.length; i++) {
				if (s[i] == middle_vals) {
					s_index = i;
					break;
				}
			}
			if (values[0] == 0 && values[5] == 0)
				return StringToIntArray(s_1[s_index]);
			else if (values[0] == 0 && values[5] == 1)
				return StringToIntArray(s_2[s_index]);
			else if (values[0] == 1 && values[5] == 0)
				return StringToIntArray(s_3[s_index]);
			else if (values[0] == 1 && values[5] == 1)
				return StringToIntArray(s_4[s_index]);
		}
		return new int[] { 0, 0, 0, 0 };
	}
}