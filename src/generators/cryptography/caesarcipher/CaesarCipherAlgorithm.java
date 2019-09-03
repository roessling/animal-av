package generators.cryptography.caesarcipher;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Leonid Khaylov <leonid.khaylov@gmail.com>
 * @version 1.0 2010-05-17
 */
public class CaesarCipherAlgorithm extends AnnotatedAlgorithm implements generators.framework.Generator {

	private static final String DESCRIPTION = "In cryptography, a Caesar cipher, also known as a Caesar's cipher, the shift cipher, Caesar's code or Caesar shift,"
			+ " is one of the simplest and most widely known encryption techniques. It is a type of substitution cipher in which"
			+ " each letter in the plaintext is replaced by a letter some fixed number of positions down the alphabet."
			+ " For example, with a shift of 3, A would be replaced by D, B would become E, and so on."
			+ " The method is named after Julius Caesar, who used it to communicate with his generals.";

	private int SHIFT = 5;

	private int timing;

	private static final String[] ALPHABET = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * Highlight range of array elements. Works in the cyclic manner.
	 * 
	 * @param array
	 *            array to use
	 * @param from
	 *            pointer start position in the array
	 * @param to
	 *            pointer end position in the array
	 */
	private void highlightArrayRange(StringArray array, int from, int to) {
		int i = from, num = 0;
		while (i != to) {
			array.highlightCell(i, new TicksTiming(this.timing * num
					/ this.SHIFT), null);

			i = ++i % array.getLength();
			num++;
		}
		array.highlightCell(to, new TicksTiming(this.timing), null);
	}

	/**
	 * Unhighlight range of array elements. Works in the cyclic manner.
	 * 
	 * @param array
	 *            array to use
	 * @param from
	 *            pointer start position in the array
	 * @param to
	 *            pointer end position in the array
	 */
	private void unhighlightArrayRange(StringArray array, int from, int to) {
		int i = from;
		while (i != to) {
			array.unhighlightCell(i, null, null);

			i = ++i % array.getLength();
		}
		array.unhighlightCell(to, null, null);
	}

	/**
	 * Gets a shifted by SHIFT letter from given one in the ALPHABET or returns
	 * the same string if it is not a letter
	 * 
	 * @param letter
	 *            letter to shift
	 * @return a letter from the ALPHABET shifted by SHIFT to the right from
	 *         <i>letter</i> or the given string itself if it is not a letter
	 */
	private String getShiftedLetterFor(String letter) {
		int letterPosition;
		String shiftedLetter = letter;

		letterPosition = Arrays.asList(ALPHABET).indexOf(letter.toUpperCase());

		if (letterPosition > -1)
			shiftedLetter = ALPHABET[(letterPosition + SHIFT) % ALPHABET.length];

		return shiftedLetter;
	}

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	public String getAnnotatedSrc() {
		return "1. Choose next array element starting from first one.																@label(\"cond\")" + "@inc(\"steps\")\r\n"	// 0
		+ "2. If it is a letter, then replace it with another one from the alphabet shifted by " + SHIFT + " to the right.			@label(\"repl\")" + "\r\n" // 1
		+ "3. Repeat from step 1 while there are still unvisited elements in the array												@label(\"rep\")" + "\r\n"; // 2;
	}

	public String getName() {
		return "Caesar-Cipher algorithm with variable shift value";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	/*public String getCodeExample() {
		return SOURCE_CODE;
	}*/
	
	/**
	 * Makes a string to a string array.
	 * @param string original string
	 * @return resulting string array. Each string element becomes array element.
	 */
	public static String[] stringToArray(String string)
	{
		String[] arMessage = new String[string.length()];
		for (int i=0; i< arMessage.length; i++)
			arMessage[i] = string.substring(i, i+1);
		
		return arMessage;
	}


	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		this.init();
		
		//vars.declare("int", step, "0");   vars.setGlobal(step);

		// Retrieve optional values
		String message = (String) primitives.get("Message");
		this.SHIFT = (Integer) primitives.get("Shift Value");
		this.timing = (Integer) primitives.get("Highlight Time");

		// Retrieve properties
		arrayProps = (ArrayProperties) props.getPropertiesByName("Array Properties");
		scProps = (SourceCodeProperties) props.getPropertiesByName("Source Code Properties");
		
		this.encrypt( stringToArray(message) );

		return lang.toString();
	}

	/**
	 * Encrypt the given message with Caesar-Cipher algorithm
	 * 
	 * @param arMessage
	 *            message to encode
	 */
	private void encrypt(String[] arMessage) {
		int alphabetOldValueIndex = -1, alphabetNewValueIndex = -1;

		// Create title and subtitle
		Text title = lang.newText(new Coordinates(20, 30),
				"Caesar-Cipher Algorithm", "header", null, titleProps), subTitle = lang
				.newText(new Offset(0, 30, title, AnimalScript.DIRECTION_NW),
						"shift: " + this.SHIFT, "options", null, subTitleProps);

		// create the original message array
		Text originalMessage = lang.newText(new Offset(0, 40, subTitle,
				AnimalScript.DIRECTION_NW), "Message:", "originalMessage",
				null, subTitleProps);
		lang.newStringArray(new Offset(10, 0, originalMessage,
				AnimalScript.DIRECTION_NE), arMessage, "original", null,
				arrayProps);

		lang.nextStep();

		// create the code message array
		Text code = lang.newText(new Offset(0, 90, originalMessage,
				AnimalScript.DIRECTION_NW), "Code:", "code", null,
				subTitleProps);
		StringArray message = lang.newStringArray(new Offset(10, 0, code,
				AnimalScript.DIRECTION_NE), arMessage, "message", null,
				arrayProps);

		// create the Alphabet array
		Text alphabetLabel = lang.newText(new Offset(120, 0, message,
				AnimalScript.DIRECTION_NE), "Alphabet:", "alphabetLabel", null,
				subTitleProps);
		StringArray alphabet = lang.newStringArray(new Offset(10, 0,
				alphabetLabel, AnimalScript.DIRECTION_NE), ALPHABET,
				"alphabet", null, arrayProps);
		ArrayMarkerProperties alphabetPointerOldProps = new ArrayMarkerProperties(), alphabetPointerNewProps = new ArrayMarkerProperties();
		alphabetPointerOldProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY,
				true);
		alphabetPointerOldProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
				"old");

		alphabetPointerNewProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY,
				true);
		alphabetPointerNewProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
				"new");

		ArrayMarker alphabetPointerOld = lang.newArrayMarker(alphabet, 0,
				"alphabetPointerOld", null, alphabetPointerOldProps), alphabetPointerNew = lang
				.newArrayMarker(alphabet, 0, "alphabetPointerNew", null,
						alphabetPointerNewProps);

		// Create pseudo-code label
		lang.newText(new Offset(0, 50, code,
				AnimalScript.DIRECTION_SW), "Pseudo-Code:", "pseudoCodeLabel",
				null, subTitleProps);

		lang.nextStep();

		// create array pointer for first array element
		ArrayMarker pMessage = lang
				.newArrayMarker(message, 0, "pMessage", null);

		// while there are unvisited elements in the array
		while (pMessage.getPosition() < message.getLength()) {
			// Choose and highlight next array element
			exec("cond");
			message.highlightElem(pMessage.getPosition(), null, null);

			// next frame
			lang.nextStep();

			// highlight and replace a letter
			exec("repl");
			message.unhighlightElem(pMessage.getPosition(), null, null);

			// get current array element value
			String value = message.getData(pMessage.getPosition());
			// get shifted letter
			String newValue = this.getShiftedLetterFor(value);

			// If current array element is a letter, then replace it with
			// another one from the alphabet shifted by SHIFT to the right.
			if (!newValue.equals(value)) {
				// highlight new and old letters in the alphabet
			  for (int i = 0; i < alphabet.getLength(); i++) {
			    if (alphabet.getData(i).equals(value.toUpperCase()))
			      alphabetOldValueIndex = i;
			    if (alphabet.getData(i).equals(newValue))
			      alphabetNewValueIndex = i;
			  }
//				alphabetOldValueIndex = Arrays.asList(alphabet.getData())
//						.indexOf(value.toUpperCase());
//				alphabetNewValueIndex = Arrays.asList(alphabet.getData())
//						.indexOf(newValue);
				alphabetPointerOld.move(alphabetOldValueIndex, null, null);
				alphabetPointerOld.show();
				this.highlightArrayRange(alphabet, alphabetOldValueIndex,
						alphabetNewValueIndex);
				alphabetPointerNew.move(alphabetNewValueIndex, null, null);
				alphabetPointerNew.show(new TicksTiming(this.timing));

				// highlight current letter in the message
				message.highlightCell(pMessage.getPosition(), null, null);
				message.put(pMessage.getPosition(), newValue, new TicksTiming(
						this.timing), null);
			}

			// next frame
			lang.nextStep();

			// unhighlight alphabet array values
			if (alphabetOldValueIndex > -1 && alphabetNewValueIndex > -1) {
				alphabetPointerOld.hide();
				alphabetPointerNew.hide();
				this.unhighlightArrayRange(alphabet, alphabetOldValueIndex,
						alphabetNewValueIndex);
			}

			// unhighlight array cells and check whether there are still
			// unvisited elements in the array
			message.unhighlightCell(pMessage.getPosition(), null, null);
			exec("rep");

			// next frame
			lang.nextStep();

			// move the array pointer to the next element
			pMessage.increment(null, null);
		}

		// unhighlight source code lines and move the array pointer outside of
		// the array
		pMessage.moveOutside(null, null);
	}

	@Override
	public String getAlgorithmName() {
		return "Caesar Cipher";
	}

	@Override
	public String getAnimationAuthor() {
		return "Leonid Khaylov";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	private TextProperties titleProps, subTitleProps;

	private ArrayProperties arrayProps;

  private SourceCodeProperties scProps;

	@Override
	public void init() {
		super.init();
		
		// Create text properties
		this.titleProps = new TextProperties();
		this.titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 24));

		this.subTitleProps = new TextProperties();
		this.subTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 16));
		
//		SourceCodeProperties props = new SourceCodeProperties();
//		props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
//		props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
//		props.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
		// now, create the pseudo-source code entity
		sourceCode = lang.newSourceCode(new Coordinates(400, 20), "listSource", null, scProps);
		
		vars.declare("int", "steps", "0");
		
		parse();
	}
}
