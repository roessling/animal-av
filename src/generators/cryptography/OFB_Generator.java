package generators.cryptography;

import generators.cryptography.util.*;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.parser.AnimalscriptParser;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;

import javax.crypto.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.ahrgr.animal.kohnert.asugen.PolyLine;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringArrayGenerator;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.StringArrayGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import algorithm.animalTranslator.codeItems.Off;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.Arrays;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;

public class OFB_Generator implements Generator {
	public KeyInterface key;
	protected Language lang;
	private Text header;
	private Rect box;
	private Text cipher;
	private Text lengthR;
	private Text initVector;
	private TextProperties headerProperty;
	private RectProperties rectProperty;
	private TextProperties cipherProps;
	private TextProperties rProps;
	private TextProperties initalVectorProps;
	private TextProperties textProps;
	private ArrayProperties arrayProps;
	private ArrayMarkerProperties marker;
	private SourceCodeProperties scProps;
	private TwoValueCounter counter;
	private StringMatrix table;
	private TextProperties propsTable;
	private SourceCode sc;
	private Label codeLabel;
	// private String IV;// value of initial vector
	private String initial_vector;
	private String cipherText;
	private int r; // length of r;
	private int[] E_is_Permutation;
	private String E_is_Function;
	private int n = 0;
	private StringArray strArray;
	private boolean isKeyPermutation;
	private int lengthOfArray = 0;
	private int number_Of_Zero_Append = 0;
	private int scaleColumn;
	private int scaleOfRow;
	private int numberOfCharacter = 0;
	private int numberofCharacter_I = 0;
	private StringBuilder result;
	private StringBuilder errorMessage;

	public OFB_Generator() {
		// this.isKeyFunktion = isKeyFunction;

	}

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	public Text getInitVector() {
		return initVector;
	}

	public void setInitVector(Text initVector) {
		this.initVector = initVector;
	}

	public boolean isKeyPermutation() {
		return isKeyPermutation;
	}

	public void setIsKeyPermutation(boolean isKeyPermutation) {
		this.isKeyPermutation = isKeyPermutation;
	}

	public String getKeyFunction() {
		return E_is_Function;
	}

	public void set_E_is_Function(String E_is_Function) {
		this.E_is_Function = E_is_Function;
	}

	public int[] getKeyPermutation() {
		return E_is_Permutation;
	}

	public void set_E_is_Permutation(int[] E_is_Permutation) {
		this.E_is_Permutation = E_is_Permutation;
	}

	public KeyInterface getKey() {
		return key;
	}

	public void setKey() {

		this.key = (KeyInterface) (this.isKeyPermutation ? new KeyIsPermutation(
				this.E_is_Permutation) : new KeyIsFunction(this.E_is_Function));

	}

	public void start() {
		// TODO Auto-generated method stub
		lang = new AnimalScript("OFB Decryption", "Tien Truong Nguyen", 800,
				600);
		lang.setStepMode(true);
		lang.nextStep();
		
		this.rectProperty = new RectProperties();
		this.headerProperty.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("Monospaced", Font.BOLD, 24));
		header = lang.newText(new Coordinates(30, 40), "OFB Decryption",
				"header", null, headerProperty);
		box = lang.newRect(new Offset(-10, -5, header,
				AnimalScript.DIRECTION_NW), new Offset(10, 5, header,
				AnimalScript.DIRECTION_SE), "box", null, rectProperty);
		lang.nextStep("Description");
		description();
		lang.nextStep();
		lang.hideAllPrimitives();
		header.show();
		box.show();
		lang.nextStep();
		// FIXME: this creates a blank page

	}

	@Override
	public void init() {

	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		this.isKeyPermutation = (boolean) primitives.get("isKeyPermutation");
		this.initial_vector = (String) primitives.get("initial_vector");
		this.r = (int) primitives.get("r");
		this.cipherText = (String) primitives.get("cipherText");
		this.E_is_Permutation = (int[]) primitives.get("E_is_Permutation");
		this.E_is_Function = (String) primitives.get("E_is_Function");

		this.headerProperty = (TextProperties) props
				.getPropertiesByName("headerProperty");
		this.scProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProperties");
		this.arrayProps = (ArrayProperties) props
				.getPropertiesByName("arrayProperties");
		this.marker = (ArrayMarkerProperties) props
				.getPropertiesByName("arrayMarkerProperties");
		this.propsTable = (TextProperties) props
				.getPropertiesByName("textProperties");

		if (checkValidData()) {
			// init();
			start();

			setKey();
			lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
			decryption();
			lang.finalizeGeneration();
			return lang.toString();
		}
		return null;

	}

	public void description() {
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		lang.newText(
				new Coordinates(10, 100),
				"Output Feedback (OFB)  Mode ist eine Betriebsart (Modus),in der Blockchiffren als ",
				"description1", null, textProps);
		lang.newText(
				new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
				"Stromchiffren betrieben werden.Die Entschluesselung beim Empfaenger funktioniert ",
				"description2", null, textProps);
		lang.newText(
				new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
				" wie Verschluesselung, erzeugt also bei gleichem Initialisierungsvektor(IV)",
				"description3", null, textProps);
		lang.newText(
				new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
				"und  gleichem Schluessel die gleiche binaere Datenfolge mit der XOR-Operation  ",
				"description4", null, textProps);
		lang.newText(
				new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
				"des Sender rueckgaengig gemacht werden kann. Die Blockchiffre auf Sender-und Empfaengerseite ",
				"description5", null, textProps);
		lang.newText(
				new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
				" kann fast simultan berechnet werden.Bei der Entschluesselung wirken sich Uebertragungsfehler ",
				"description6", null, textProps);
		lang.newText(
				new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
				"(Bitfehler) im Chiffrat nur auf die entsprechende Bitstelle im entschluesselten Klartext aus und ",
				"description7", null, textProps);
		lang.newText(new Offset(0, 25, "description7",
				AnimalScript.DIRECTION_NW),
				"pflanzt sich der Fehler nicht im Klartext fort. ",
				"description8", null, textProps);

	}

	public void inputData() {

		cipherProps = new TextProperties();
		cipherProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		cipher = lang
				.newText(new Offset(0, 15, box, AnimalScript.DIRECTION_SW),
						"Cipher text c=" + this.cipherText, "cipher", null,
						cipherProps);
		initalVectorProps = new TextProperties();
		initalVectorProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		initVector = lang
				.newText(new Offset(0, 5, cipher, AnimalScript.DIRECTION_SW),
						"Initialisierungsvektor IV=" + this.initial_vector,
						"initialVector", null, initalVectorProps);
		
		lengthR = lang.newText(new Offset(10, 0, initVector,
				AnimalScript.DIRECTION_NE), "r=" + this.r, "lengthR", null,
				cipherProps);
		if (isKeyPermutation) {
			lang.newText(new Offset(0, 20, "initialVector",
					AnimalScript.DIRECTION_SW), "Permutation E= ", "E", null,
					initalVectorProps);
		} else {
			lang.newText(new Offset(0, 12, "initialVector",
					AnimalScript.DIRECTION_SW), "Function E= ", "E", null,
					initalVectorProps);
		}
		key.drawKey(lang, "E");
		lang.nextStep("Show code and table");

	}

	public void showSourceCode() {
		this.codeLabel = new Label("code");

		// create the source code entity
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.MONOSPACED, Font.PLAIN, 16));
		SourceCode sc = lang.newSourceCode(new Offset(0, 100, "E",
				AnimalScript.DIRECTION_SW), "sourceCode", null, this.scProps);
		// add a code line
		sc.addCodeLine("function OFB_Decryption(cipherText,r,IV){", null, 0,
				null);
		sc.addCodeLine("blocks=spilitCipherText(cipherText,r);", null, 1, null);
		sc.addCodeLine("I_1=IV;", null, 1, null);
		sc.addCodeLine("c=Array(blocks.length());#array of cipher text", null,
				1, null);
		sc.addCodeLine("for (block in blocks){", null, 1, null);
		sc.addCodeLine("O_i=E(I_i);#calculate with Key", null, 2, null);
		sc.addCodeLine("t_i=O_i[1:r];# the first r bits from =O_i", null, 2,
				null);
		sc.addCodeLine("m[i]=c_i xor t_i ;# with c_i=one block in blocks",
				null, 2, null);
		sc.addCodeLine("I_(i+1)=O_i;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		this.sc = sc;

	}

	/**
	 * @param numberOfRow
	 * @param r
	 */
	public void tableDescription(int scaleColumn, int scaleOfRow,
			int sizeOfCharacter) {

		this.scaleColumn = scaleColumn;
		this.scaleOfRow = scaleOfRow;
		int lengthText = cipherText.length();
		this.lengthOfArray = (lengthText % r == 0) ? (lengthText / r)
				: (lengthText / r + 1);
		this.number_Of_Zero_Append = (lengthText % r == 0) ? 0
				: (r - lengthText % r);
		// //horizontal Line
		int lengOf_n = Integer.toBinaryString(n).length();
		numberOfCharacter = this.isKeyPermutation ? (this.initial_vector
				.length())
				: ((lengOf_n > this.initial_vector.length()) ? lengOf_n
						: this.initial_vector.length());
		numberOfCharacter = (this.isKeyPermutation
				&& (r == 1 || r == 2 || r == 3) && (this.initial_vector
				.length() <= 3)) ? 4 : numberOfCharacter;
		numberofCharacter_I = (!this.isKeyPermutation && numberOfCharacter < this.initial_vector
				.length()) ? this.initial_vector.length() : numberOfCharacter;
		Node[] vertices = {
				new Offset(100, scaleOfRow, "sourceCode",
						AnimalScript.DIRECTION_NE),
				new Offset(
						100
								+ 7
								* (scaleColumn + numberOfCharacter
										* sizeOfCharacter)
								+ (scaleColumn + numberofCharacter_I
										* sizeOfCharacter), scaleOfRow,
						"sourceCode", AnimalScript.DIRECTION_NE) };
		int numberOfRow = this.lengthOfArray;
		PolylineProperties polyLineProp = new PolylineProperties();
		lang.newPolyline(vertices, "hline", null, polyLineProp);
		TextProperties props = new TextProperties();
		Font font = (Font) this.propsTable
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		props.set(AnimationPropertiesKeys.FONT_PROPERTY,
				font.deriveFont(Font.PLAIN, 24));
		Color cl = (Color) this.propsTable
				.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		props.set(AnimationPropertiesKeys.COLOR_PROPERTY, cl);

		lang.newText(new Offset(100 + scaleColumn / 2 + numberOfCharacter
				* sizeOfCharacter / 2, -scaleOfRow / 2, "sourceCode",
				AnimalScript.DIRECTION_NE), "i", "Line_i", null, props);
		Node[] vertices_1 = {
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						-scaleOfRow / 2, "Line_i", AnimalScript.DIRECTION_NE),
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						(numberOfRow + 1) * (scaleOfRow + sizeOfCharacter),
						"Line_i", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(vertices_1, "line_1", null, polyLineProp);
		//
		lang.newText(new Offset((scaleColumn + numberofCharacter_I
				* sizeOfCharacter) / 2, scaleOfRow / 2, "line_1",
				AnimalScript.DIRECTION_NE), "I_i", "Line_I_i", null, props);
		Node[] vertices_2 = {
				new Offset(
						(scaleColumn + numberofCharacter_I * sizeOfCharacter) / 2 - 10,
						-scaleOfRow / 2, "Line_I_i", AnimalScript.DIRECTION_NE),
				new Offset(
						(scaleColumn + numberofCharacter_I * sizeOfCharacter) / 2 - 10,
						(numberOfRow + 1) * (scaleOfRow + sizeOfCharacter),
						"Line_I_i", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(vertices_2, "line_2", null, polyLineProp);
		//
		lang.newText(new Offset((scaleColumn + numberOfCharacter
				* sizeOfCharacter) / 2, scaleOfRow / 2, "line_2",
				AnimalScript.DIRECTION_NE), "O_i", "Line_O_i", null, props);
		Node[] vertices_3 = {
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						-scaleOfRow / 2, "Line_O_i", AnimalScript.DIRECTION_NE),
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						(numberOfRow + 1) * (scaleOfRow + sizeOfCharacter),
						"Line_O_i", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(vertices_3, "line_3", null, polyLineProp);
		//
		lang.newText(new Offset((scaleColumn + numberOfCharacter
				* sizeOfCharacter) / 2, scaleOfRow / 2, "line_3",
				AnimalScript.DIRECTION_NE), "t_i", "Line_t_i", null, props);
		Node[] vertices_4 = {
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						-scaleOfRow / 2, "Line_t_i", AnimalScript.DIRECTION_NE),
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						(numberOfRow + 1) * (scaleOfRow + sizeOfCharacter),
						"Line_t_i", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(vertices_4, "line_4", null, polyLineProp);
		//
		lang.newText(new Offset((scaleColumn + numberOfCharacter
				* sizeOfCharacter) / 2, scaleOfRow / 2, "line_4",
				AnimalScript.DIRECTION_NE), "c_i", "Line_C_i", null, props);
		Node[] vertices_5 = {
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						-scaleOfRow / 2, "Line_C_i", AnimalScript.DIRECTION_NE),
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						(numberOfRow + 1) * (scaleOfRow + sizeOfCharacter),
						"Line_C_i", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(vertices_5, "line_5", null, polyLineProp);
		//
		lang.newText(new Offset((scaleColumn + numberOfCharacter
				* sizeOfCharacter) / 2, scaleOfRow / 2, "line_5",
				AnimalScript.DIRECTION_NE), "m_i", "Line_m_i", null, props);
		Node[] vertices_6 = {
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						-scaleOfRow / 2, "Line_m_i", AnimalScript.DIRECTION_NE),
				new Offset(
						(scaleColumn + numberOfCharacter * sizeOfCharacter) / 2 - 10,
						(numberOfRow + 1) * (scaleOfRow + sizeOfCharacter),
						"Line_m_i", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(vertices_6, "line_6", null, polyLineProp);
	}

	public String appendUnderLine() {
		StringBuffer strBuffer = new StringBuffer("");
		for (int i = 0; i < this.r; i++)
			strBuffer.append("_");

		return strBuffer.toString();

	}

	@SuppressWarnings("deprecation")
	public void spiltToArray() {
		Timing defaultTiming_marker = new TicksTiming(0);

		Timing defaultTiming_setValue = new TicksTiming(10);

		ArrayMarkerProperties marker = new ArrayMarkerProperties();
		marker.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);
		marker.set(AnimationPropertiesKeys.LABEL_PROPERTY, "marker");
		sc.toggleHighlight(0, 1);
		String underLine = appendUnderLine();
		String[] arrayCipherText = new String[this.lengthOfArray];
		for (int i = 0; i < this.lengthOfArray; i++) {

			arrayCipherText[i] = underLine;
		}
		TextProperties props = new TextProperties();
		props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));

		lang.newText(new Offset(100, -150, "sourceCode",
				AnimalScript.DIRECTION_NE), "blocks", "blocks", null, props);

		this.strArray = new StringArray(new AnimalStringArrayGenerator(lang),
				new Offset(20, -10, "blocks", AnimalScript.DIRECTION_NE),
				arrayCipherText, "arrayCipher", null, arrayProps);
		counter = lang.newCounter(strArray);

		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.blue);
		TwoValueView view = lang.newCounterView(counter, new Offset(100, -1,
				"arrayCipher", AnimalScript.DIRECTION_NE), cp, true, true);
		lang.nextStep();
		ArrayMarker pt = null;

		int i = 0;
		while (i < this.lengthOfArray) {

			if ((i + 1) * r <= this.cipherText.length()) {
				strArray.put(i, cipherText.substring(i * r, (i + 1) * r), null,
						defaultTiming_setValue);
				strArray.highlightCell(i, null, defaultTiming_setValue);
				lang.nextStep();
			} else {
				if (this.number_Of_Zero_Append != 0) {

					String temp = cipherText.substring(i * r, (i + 1) * r
							- this.number_Of_Zero_Append);
					StringBuffer temp_buffer = new StringBuffer(temp);

					for (int k = 0; k < this.number_Of_Zero_Append; k++) {
						temp_buffer.append("_");
					}

					strArray.put(i, temp_buffer.toString(), null,
							defaultTiming_setValue);
					TextProperties pr = new TextProperties();
					pr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
							Font.SANS_SERIF, Font.PLAIN, 14));

					Text appendZero = lang.newText(new Offset(-50, -20,
							"arrayCipher", AnimalScript.DIRECTION_NE),
							"append Zero", "append Zero", null, pr);

					int timeTicks = 200;
					appendZero
							.hide(new TicksTiming(
									(timeTicks + (this.number_Of_Zero_Append - 1) * 100)));
					String theLastBlock = temp_buffer.toString();
					for (int j = 0; j < this.number_Of_Zero_Append; j++) {
						theLastBlock = theLastBlock.replaceFirst("_", "0");
						strArray.put(i, theLastBlock, null, new TicksTiming(
								timeTicks));
						timeTicks = timeTicks + 100;

					}

					// appendZero.hide();
					strArray.highlightCell(i, null, defaultTiming_setValue);
				}
				// lang.nextStep();

			}
			i++;

		}
		// pt.hide();

	}

	public void calculate() {
		// lang.nextStep();
		Timing defaultTiming = new TicksTiming(30);
		lang.nextStep("calculate");
		result = new StringBuilder();
		TextProperties props = new TextProperties();
		props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.MONOSPACED, Font.PLAIN, 16));
		props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

		this.sc.toggleHighlight(1, 2);

		Text tx_0 = lang.newText(new Offset(0, scaleOfRow + 3, "Line_i",
				AnimalScript.DIRECTION_SW), "1", "i_1", null, props);

		Text tx_1 = lang.newText(new Offset(-20 * numberofCharacter_I / 6,
				scaleOfRow, "Line_I_i", AnimalScript.DIRECTION_SW),
				this.initial_vector, "I_1", null, props);

		lang.nextStep();
		this.sc.toggleHighlight(2, 3);
		tx_0.changeColor("color", Color.black, null, defaultTiming);
		tx_1.changeColor("color", Color.black, null, defaultTiming);
		lang.nextStep();
		this.sc.toggleHighlight(3, 4);
		ArrayMarker pt = lang.newArrayMarker(strArray, 0, "pt", null, marker);
		lang.nextStep();
		this.sc.toggleHighlight(4, 5);
		// /////////////////Start to calculate with
		// inital_vector/////////////////////////

		int[] O_1 = this.key.calculateWithDecryptionKey(initial_vector);

		Text tx_O_1 = lang.newText(new Offset(-20 * numberOfCharacter / 6,
				scaleOfRow, "Line_O_i", AnimalScript.DIRECTION_SW),
				intArrayToString(O_1), "O_1", null, props);
		lang.nextStep("calculte_c_1_and_t_1");
		this.sc.toggleHighlight(5, 6);
		tx_O_1.changeColor("color", Color.black, null, defaultTiming);
		int[] t = calculate_T(O_1);
		Text tx_t_1 = lang.newText(new Offset(-20 * r / 4, scaleOfRow,
				"Line_t_i", AnimalScript.DIRECTION_SW), intArrayToString(t),
				"t_1", null, props);

		String c_1 = strArray.getData(pt.getPosition());
		// Display c_1
		Text tx_c_1 = lang
				.newText(new Offset(-20 * r / 4, scaleOfRow, "Line_C_i",
						AnimalScript.DIRECTION_SW), c_1, "c_1", null, props);
		lang.nextStep();
		this.sc.toggleHighlight(6, 7);
		tx_t_1.changeColor("color", Color.black, null, defaultTiming);
		tx_c_1.changeColor("color", Color.black, null, defaultTiming);
		// Display m_1
		result.append(xor(stringToArray(c_1), t));
		Text tx_m_1 = lang.newText(new Offset(-20 * r / 4, scaleOfRow,
				"Line_m_i", AnimalScript.DIRECTION_SW),
				xor(stringToArray(c_1), t), "m_1", null, props);
		lang.nextStep();
		this.sc.toggleHighlight(7, 8);
		tx_m_1.changeColor("color", Color.black, null, defaultTiming);
		Text tx_i_2 = lang.newText(new Offset(0, scaleOfRow / 2, "i_1",
				AnimalScript.DIRECTION_SW), "2", "i_2", null, props);
		// String I_2 = intArrayToString(O_1);
		Text tx_I_2 = lang.newText(new Offset(0, scaleOfRow / 2, "I_1",
				AnimalScript.DIRECTION_SW), intArrayToString(O_1), "I_2", null,
				props);
		lang.nextStep();
		this.sc.toggleHighlight(8, 4);
		tx_i_2.changeColor("color", Color.black, null, defaultTiming);
		tx_I_2.changeColor("color", Color.black, null, defaultTiming);
		String I = intArrayToString(O_1);
		int i = 2;

		while (i < this.strArray.getLength() + 1) {

			this.sc.highlight(4);
			strArray.unhighlightCell(pt.getPosition(), null, defaultTiming);
			pt.increment(null, defaultTiming);
			lang.nextStep("calculte_O_" + i);

			int[] O_i = this.key.calculateWithDecryptionKey(I);
			this.sc.toggleHighlight(4, 5);
			Text tx_O_i = lang.newText(new Offset(0, scaleOfRow / 2, "O_"
					+ (i - 1), AnimalScript.DIRECTION_SW),
					intArrayToString(O_i), "O_" + i, null, props);
			lang.nextStep("calculte_t_" + i + "_and_c_" + i);
			this.sc.toggleHighlight(5, 6);
			tx_O_i.changeColor("color", Color.black, null, defaultTiming);
			int[] t_i = calculate_T(O_i);
			Text tx_t_i = lang.newText(new Offset(0, scaleOfRow / 2, "t_"
					+ (i - 1), AnimalScript.DIRECTION_SW),
					intArrayToString(t_i), "t_" + i, null, props);

			String c_i = strArray.getData(pt.getPosition());
			// Display c_1
			Text tx_c_i = lang.newText(new Offset(0, scaleOfRow / 2, "c_"
					+ (i - 1), AnimalScript.DIRECTION_SW), c_i, "c_" + i, null,
					props);
			lang.nextStep("calculte_m_" + i);
			this.sc.toggleHighlight(6, 7);
			tx_t_i.changeColor("color", Color.black, null, defaultTiming);
			tx_c_i.changeColor("color", Color.black, null, defaultTiming);
			// Display m_1

			/*
			 * if(i==2){ FillInBlanksQuestionModel CFB=new
			 * FillInBlanksQuestionModel("CFB"); CFB.setPrompt(
			 * "Welcher Wert von Klartext wird in diesem Schritt erwartet?");
			 * CFB.addAnswer(xor(stringToArray(c_i), t_i), 1,
			 * "m_2="+xor(stringToArray(c_i), t_i)); lang.addFIBQuestion(CFB); }
			 * if(i==5){ FillInBlanksQuestionModel CFB=new
			 * FillInBlanksQuestionModel("CFB"); CFB.setPrompt(
			 * "Welcher Wert von Klartext wird in diesem Schritt erwartet?");
			 * CFB.addAnswer(xor(stringToArray(c_i), t_i), 1,
			 * "m_5="+xor(stringToArray(c_i), t_i)); lang.addFIBQuestion(CFB); }
			 * if(i==6){ FillInBlanksQuestionModel CFB=new
			 * FillInBlanksQuestionModel("CFB"); CFB.setPrompt(
			 * "Welcher Wert von Klartext wird in diesem Schritt erwartet?");
			 * CFB.addAnswer(xor(stringToArray(c_i), t_i), 1,
			 * "m_6="+xor(stringToArray(c_i), t_i)); lang.addFIBQuestion(CFB); }
			 */

			result.append(xor(stringToArray(c_i), t_i));
			Text tx_m_i = lang.newText(new Offset(0, scaleOfRow / 2, "m_"
					+ (i - 1), AnimalScript.DIRECTION_SW),
					xor(stringToArray(c_i), t_i), "m_" + i, null, props);
			lang.nextStep("calculte_t_" + (i + 1));
			this.sc.toggleHighlight(7, 8);
			tx_m_i.changeColor("color", Color.black, null, defaultTiming);
			Text tx_i_next = lang.newText(new Offset(0, scaleOfRow / 2, "i_"
					+ i, AnimalScript.DIRECTION_SW), String.valueOf(i + 1),
					"i_" + (i + 1), null, props);
			Text tx_I_next = lang.newText(new Offset(0, scaleOfRow / 2, "I_"
					+ i, AnimalScript.DIRECTION_SW), intArrayToString(O_i),
					"I_" + (i + 1), null, props);
			i++;
			I = intArrayToString(O_i);
			lang.nextStep();

			this.sc.unhighlight(8);
			tx_i_next.changeColor("color", Color.black, null, defaultTiming);
			tx_I_next.changeColor("color", Color.black, null, defaultTiming);

		}
		if (i == (this.strArray.getLength() + 1)) {
			strArray.unhighlightCell(pt.getPosition(), null, defaultTiming);
			this.sc.toggleHighlight(8, 9);
			this.sc.unhighlight(4);
			lang.nextStep();
			this.sc.toggleHighlight(9, 10);
			pt.hide();
			lang.nextStep("result");
			this.sc.unhighlight(10);
			TextProperties prop = new TextProperties();
			prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					Font.SANS_SERIF, Font.PLAIN, 16));
			props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			lang.newText(
					new Offset(0, 15, "blocks", AnimalScript.DIRECTION_SW),
					"plaintext", "plaintext", null, prop);
			TextProperties propsPlaintext = new TextProperties();
			propsPlaintext.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					Font.SANS_SERIF, Font.PLAIN, 18));
			lang.newText(
					new Offset(5, 0, "plaintext", AnimalScript.DIRECTION_NE),
					result.toString().substring(
							0,
							result.toString().length()
									- this.number_Of_Zero_Append),
					"valuesOfPlaintext", null, propsPlaintext);
		}
		// //////////////////////////////////////////////////////////////////////////////
	}

	public String intArrayToString(int[] array) {
		int i = 0;
		StringBuffer buffer = new StringBuffer();
		while (i < array.length) {
			buffer.append(String.valueOf(array[i++]));
		}
		return buffer.toString();
	}

	public int[] calculate_T(int[] O_i) {
		int[] t = new int[r];
		for (int i = 0; i < r; i++) {
			t[i] = O_i[i];
		}
		return t;
	}

	public int[] stringToArray(String str) {
		int[] array = new int[str.length()];
		for (int j = 0; j < array.length; j++) {
			array[j] = Integer.valueOf(String.valueOf(str.charAt(j)));
		}
		return array;
	}

	public String xor(int[] c_i, int[] t_i) {
		int[] m_i = new int[c_i.length];
		for (int j = 0; j < m_i.length; j++) {
			m_i[j] = c_i[j] ^ t_i[j];
		}

		return intArrayToString(m_i);

	}

	public String next_I(String I_i, String c_i) {

		return (new StringBuffer(I_i.substring(this.r, I_i.length()))
				.append(c_i)).toString();
	}

	public boolean isStringBinary(String str) {
		String binary = null;
		for (int i = 0; i < str.length(); i++) {
			if ((str == null) || (str.equals("")) || (str.equals(" "))) {
				this.errorMessage
						.append("+Der Chiffretext und der initialisierte Vector  muessen nicht leer sein.\n ");
				return false;
			} else {
				binary = String.valueOf(str.charAt(i));
				if ((!binary.equals("0") && !binary.equals("1"))) {
					this.errorMessage
							.append("+Der Chiffretext und der initialisierte Vektor  muessen die binaere Folge sein.\n ");
					return false;
				}
			}
		}
		return true;

	}

	public boolean checkBinary() {
		if ((!(this.r > 0)) || (!(this.r <= this.initial_vector.length()))
				|| (!(this.r <= this.cipherText.length())))
			this.errorMessage
					.append("+Die Laenge von r muss groesser als 0 und kleiner als die Laenge von initialisierten Vektor und Chiffretext sein.\n");
		return isStringBinary(this.cipherText)
				&& isStringBinary(this.initial_vector) && this.r > 0
				&& (this.r <= this.initial_vector.length())
				&& this.r <= this.cipherText.length();
	}

	public boolean checkValidData() {
		boolean validPermutation = false;
		this.errorMessage = new StringBuilder(
				"Die Eingabe ist falsch.Bitte pruefen Sie jetzt noch einmal.\nBei den folgenden Fehlern:\n");
		if (this.isKeyPermutation
				&& !(this.initial_vector.length() == this.E_is_Permutation.length)) {
			this.errorMessage
					.append("+Die Laenge von dem initialisierten Vektor musss gleich von Permutation sein.\n");

		}
		if (this.isKeyPermutation) {
			validPermutation = checkValueOfPermutation();
		}

		boolean isBinary = checkBinary();
		boolean isValid = this.isKeyPermutation ? ((this.initial_vector
				.length() == this.E_is_Permutation.length) && isBinary && validPermutation)
				: (checkValidOfFunction() && isBinary);
		if (!isValid) {
			// create a jframe
			JFrame frame = new JFrame("JOptionPane showMessageDialog");

			// show a joptionpane dialog using showMessageDialog
			JOptionPane.showMessageDialog(frame, this.errorMessage.toString());

		}
		return isValid;

	}

	public boolean checkValueOfPermutation() {
		boolean temp = true;
		for (int i = 0; i < this.E_is_Permutation.length; i++) {
			for (int j = 0; j < this.E_is_Permutation.length; j++) {
				if (this.E_is_Permutation[i] < 0
						|| (this.E_is_Permutation[i] > this.E_is_Permutation.length)
						|| (i != j && E_is_Permutation[i] == E_is_Permutation[j])) {
					this.errorMessage
							.append("+Der Wert von jedem Element von Permutation oder die Laenge von Permutation.\n");
					temp = false;
					return false;

				}
			}
		}

		return temp;
	}

	/**
	 * use R Expression
	 * 
	 * @return
	 */
	public boolean checkValidOfFunction() {
		String regex_1 = "^(\\+|-|)?(\\d*)(x)(mod)(\\d+)";
		String regex_2 = "^(\\+|-|)?(\\d*)(x)(\\+|-)(\\d+)(mod)(\\d+)";
		String regex_3 = "^(\\+|-|)?(\\d+)(\\*)(x)(mod)(\\d+)";
		String regex_4 = "^(\\+|-|)?(\\d+)(\\*)(x)(\\+|-)(\\d+)(mod)(\\d+)";

		String regex_5 = "^(\\()(\\+|-|)?(\\d*)(x)(\\))(mod)(\\d+)";
		String regex_6 = "^(\\()(\\+|-|)?(\\d*)(x)(\\+|-)(\\d+)(\\))(mod)(\\d+)";
		String regex_7 = "^(\\()(\\+|-|)?(\\d+)(\\*)(x)(\\))(mod)(\\d+)";
		String regex_8 = "^(\\()(\\+|-|)?(\\d+)(\\*)(x)(\\+|-)(\\d+)(\\))(mod)(\\d+)";
		boolean temp = false;

		if (!E_is_Function.equals(" ")) {
			String function = E_is_Function.trim().replaceAll(" ", "");
			temp = matchesFunction(regex_1, function)
					|| matchesFunction(regex_2, function)
					|| matchesFunction(regex_3, function)
					|| matchesFunction(regex_4, function)
					|| matchesFunction(regex_5, function)
					|| matchesFunction(regex_6, function)
					|| matchesFunction(regex_7, function)
					|| matchesFunction(regex_8, function);
			Pattern p = Pattern.compile("(mod)(\\d+)");
			Matcher m = p.matcher(function);
			if (m.find()) {
				String N = m.group(2);
				n = Integer.valueOf(N);

			}
			if (!temp)
				this.errorMessage
						.append("+Die Form der Funktion muss nach der folgenden Definition in Beschreibung sein.\n");
			if (n > 0) {
				if (!(Math.pow(2, r) <= n + 1))
					this.errorMessage
							.append("+Die Laenge von r muss kleiner als 2^(m+1) sein.\n");
			}
			return temp && (Math.pow(2, r) <= n + 1);

		} else
			return false;

	}

	public boolean matchesFunction(String regex, String str) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);

		return (m.matches());

	}

	public void decryption() {
		inputData();
		Timing defaultTiming = new TicksTiming(15);
		showSourceCode();
		tableDescription(8, 18, 14);
		this.sc.highlight(0);
		lang.nextStep("split Array");
		spiltToArray();

		calculate();

	}

	public String getName() {
		return "OFB Decryption";
	}

	public String getAlgorithmName() {
		return "OFB Decryption";
	}

	public String getAnimationAuthor() {
		return "Tien Truong Nguyen";
	}

	public String getDescription() {
		return "Output Feedback Mode (CFB) ist eine Betriebsart (Modus),in der Blockchiffren als Stromchiffren betrieben werden .Die Entschl\u00fcsselung beim Empf\u00e4nger funktioniert"
				+ "\n"
				+ "wie Verschl\u00fcsselung, erzeugt also bei gleichem Initialisierungsvektor (IV) und gleichem Schl\u00fcssel die gleiche bin\u00e4re Datenfolge mit der XOR-Operation des Sender"
				+ "\n"
				+ "r\u00fcckg\u00e4ngig gemacht werden kann. Die Blockchiffre auf Sender-und Empf\u00e4ngerseite kann fast simultan berechnet werden."
				+ "\n"
				+ " Bei der Entschl�sselung wirken sich �bertragungsfehler (Bitfehler) im Chiffrat nur auf die entsprechende Bitstelle im entschl�sselten Klartext aus und"
				+ "\n"
				+ "pflanzt sich der Fehler nicht im Klartext fort."
				+ "<br/><br/>"
				+ "<strong>Anforderungen:</strong>"
				+ "<ul>"
				+ "                      <li>  Ben\u00f6tigt wird Initialisierungsvektor (IV) die Folge von { 0 , 1}.Der Schlusseltext  geh\u00f6rt zur {0,1}^n ."
				+ "</li>"
				+ "                     <li>   Die L\u00e4nge von Block ist r mit Bedingung 1&#60=r&#60=n"
				+ "</li>"
				+ "                      <li>  Die Entschl\u00fcsselungsfunktion(E_k) kann entweder eine Permutation oder eine Funktion sein"
				+ "</li>"
				+ "                     <li>   Wenn sie eine Funktion ist ,muss sie die folgenden Bedingungen erf\u00fcllen."
				+ "<ul>"
				+ "                                  <li>    Die Form von Funktion ist  E=f(x)=ax+b mod m  oder E=f(x)=a*x+b mod m ,m&#8712N."
				+ "</li>"
				+ "                                  <li>    Oder die Form E= f(x)=(ax+b) mod m oder E= f(x)=(a*x+b) mod m ,m&#8712N."
				+ "</li>" + "</ul></li></ul>";
	}

	public String getCodeExample() {
		return "function OFB_Decryption(cipherText,r,IV){"
				+ "\n"
				+ "           blocks=spilitCipherText(cipherText,r);"
				+ "\n"
				+ "           I_1=IV;"
				+ "\n"
				+ "           c=Array(blocks.length());//calculate with Key"
				+ "\n"
				+ "           for ( block in blocks) {"
				+ "\n"
				+ "           O_i=E(I_i);//calculate with Key"
				+ "\n"
				+ "           t_i=O_i[1:r];  // the first r bits from O_i"
				+ "\n"
				+ "           m[i]=c_i xor t_i ;  // with c_i=one block in blocks;"
				+ "\n" + "           I_i+1=O_i; // next I" + "\n"
				+ "           }" + "\n" + " }";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}
