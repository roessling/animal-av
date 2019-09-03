package generators.cryptography.onetimepad;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * @author Malcolm Parsons
 */
public class OneTimePad implements generators.framework.Generator {
	protected Language lang;
	private ArrayProperties arrayProps;
	private ArrayProperties messageArrayProps;
	private SourceCodeProperties sourceCodeProps;
	private SourceCode scEncr;
	private SourceCode scDecr;
	private Text header;

	// array properties
	private Color arrayElementColor = Color.BLACK;
	private Color arraFillColor = Color.LIGHT_GRAY;
	private Color arrayElementHighlightColor = Color.YELLOW;
	private Color arrayCellHighlightColor = Color.YELLOW;
	private Coordinates sourceCodeCoordinates = new Coordinates(10, 400);

	private TreeMap<String, Integer> convertTable = new TreeMap<String, Integer>();
	// private HashMap<String, Integer> convertTable = new HashMap<String, Integer>();
	private HashMap<Integer, String> reconvertTable = new HashMap<Integer, String>();
	private ArrayList<Integer> numbers = new ArrayList<Integer>(10);
	

	/**
	 * Create a header
	 * 
	 * @return created header
	 */
	private Text getHeader() {
		Text header = lang.newText(new Coordinates(20, 30), "One-Time-Pad Animation", "header", null);
		header.setFont(new Font("SansSerif", Font.BOLD, 24), null, null);
		lang.nextStep();
		return header;
	}

	final private String sourceCodeToDisplay = "OneTimePadEncrypt(String message){" + "\n	for each letter in message {"
			+ "\n		convert letter and store in converted[];" + "\n	}" + "\n	generate OTP-Key[] with random numbers;"
			+ "\n	for each element in converted[] {" + "\n		subtract (modulo 10) equivalent element of OTP-Key[] and store in cipher[];" + "\n	}"
			+ "\n}" + "\n" + "\nOneTimePadDecrypt(int[] cipher){" + "\n	for each l in cipher[] {"
			+ "\n		add (modulo 10) letter to equivalent element of OTPkey[] and store in decipher[];" + "\n	}" + "\n	for each d in decipher[] {"
			+ "\n		convert d and store in clearMessage[]" + "\n	}" + "\n} ";

	private SourceCode getSourceCodeDecryption() {
		SourceCode sc = lang.newSourceCode(sourceCodeCoordinates, "sourceCodeDecrypt", null, sourceCodeProps);
		int indStep = 2;
		int indFirstLevel = 0;
		int indSecondLevel = indFirstLevel + indStep; // 2
		int indThirdLevel = indSecondLevel + indStep; // 4
		sc.addCodeLine("OneTimePadDecrypt(int[] cipher){", null, indFirstLevel, null);
		sc.addCodeLine("for each l in cipher[] {", null, indSecondLevel, null);
		sc.addCodeLine("add (modulo 10) letter to equivalent element of OTPkey[] and store in decipher[];", null, indThirdLevel, null);
		sc.addCodeLine("}", null, indSecondLevel, null);
		sc.addCodeLine("for each d in decipher[] {", null, indSecondLevel, null);
		sc.addCodeLine("convert d and store in clearMessage[]", null, indThirdLevel, null);
		sc.addCodeLine("}", null, indSecondLevel, null);
		sc.addCodeLine("}", null, indFirstLevel, null);

		lang.nextStep();
		return sc;
	}

	private SourceCode getSourceCodeEncryption() {
		SourceCode sc = lang.newSourceCode(sourceCodeCoordinates, "sourceCodeEnrypt", null, sourceCodeProps);
		int indStep = 2;
		int indFirstLevel = 0;
		int indSecondLevel = indFirstLevel + indStep; // 2
		int indThirdLevel = indSecondLevel + indStep; // 4
		sc.addCodeLine("OneTimePadEncrypt(String message){", null, indFirstLevel, null);
		sc.addCodeLine("for each letter in message {", null, indSecondLevel, null);
		sc.addCodeLine("convert letter and store in converted[];", null, indThirdLevel, null);
		sc.addCodeLine("}", null, indSecondLevel, null);
		sc.addCodeLine("generate OTP-Key[] with random numbers;", null, indSecondLevel, null);
		sc.addCodeLine("for each element in converted[] {", null, indSecondLevel, null);
		sc.addCodeLine("subtract (modulo 10) equivalent element of OTP-Key[] and store in cipher[];", null, indThirdLevel, null);
		sc.addCodeLine("}", null, indSecondLevel, null);
		sc.addCodeLine("}", null, indFirstLevel, null);

		lang.nextStep();
		return sc;
	}

	private void setSourceCodeProps(AnimationPropertiesContainer props) {
		sourceCodeProps = new SourceCodeProperties();
		if (props == null) {
			sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.MAGENTA);
			sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 12));
			sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.MAGENTA);
			sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		} else {
			sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, props.get("sourceCodeProps",
					AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
			sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, props.get("sourceCodeProps",
					AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
			sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, props.get("sourceCodeProps", AnimationPropertiesKeys.FONT_PROPERTY));
			sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("sourceCodeProps", AnimationPropertiesKeys.COLOR_PROPERTY));
		}
	}

	private void setArrayProps(AnimationPropertiesContainer props) {
		arrayProps = new ArrayProperties();
		if (props == null) {
			arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, arrayElementColor);
			arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
			arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, arraFillColor);
			arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, arrayElementHighlightColor);
			arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, arrayCellHighlightColor);
		} else {
			// use the ones from the generator
			arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("arrayProps", AnimationPropertiesKeys.COLOR_PROPERTY));
			arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, props.get("arrayProps", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
			arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, props.get("arrayProps", AnimationPropertiesKeys.FILL_PROPERTY));
			arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, props.get("arrayProps", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
			arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, props.get("arrayProps", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		}
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
	}

	private void setMessageArrayProps(AnimationPropertiesContainer props) {
		messageArrayProps = new ArrayProperties();
		messageArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, arrayElementColor);
		messageArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
		messageArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		messageArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, arrayElementHighlightColor);
		messageArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, arrayCellHighlightColor);
		messageArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
	}

	@Override
	public void init() {
		lang = new AnimalScript(getName(), getAnimationAuthor(), 640, 480);
		lang.setStepMode(true);
		createConvertTable();
		getConvertTableText();
	}

	/**
	 * Displays the used convert table
	 */
	private void getConvertTableText() {
		Text label = lang.newText(new Coordinates(20, 580), "Convert Table (CT-37c)", "convertTableLabel", null);
		label.changeColor(null, Color.BLUE, null, null);
		label.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);

//		String sb = "";
		Set<String> keySet = convertTable.keySet();
		int count = 0;
		int entryLength = 20;
		String space = "     ";
//		int newArrayCount = 0;
		for (String key : keySet) {
			if (key == "'") {
				continue;
			}
			if (count == 5) {
				count = 0;

				// lang.newText(new Coordinates(20, 620 + newArrayCount * 20), sb.toString(),
				// "convertTable", null);
				// System.err.println(sb.toString());
				// =: 97 a: 1 b: 70 c: 70 code: 0
				// d: 72 e: 2 f: 73 fig: 90 g: 74
				// h: 75 i: 3 j: 76 k: 77 l: 78
				// m: 79 n: 4 o: 5 p: 80 q: 81
				// r: 82 req: 98 s: 83 t: 6 u: 84
//				newArrayCount++;
//				sb = "";
			}
			String tmp = key + ":" + space + convertTable.get(key);
//			sb += tmp;
			for (int i = tmp.length(); i < entryLength; i++) {
//				sb += " ";
			}
			count++;
		}
		lang.newText(new Coordinates(20, 620), "=:     97          a:     1            b:     70           c:     70           code:    0",
				"convertTable", null);
		lang.newText(new Coordinates(20, 640), "d:     72           e:     2            f:     73           fig:    90          g:     74",
				"convertTable", null);
		lang.newText(new Coordinates(20, 660), "h:     75           i:      3             j:     76           k:     77           l:      78",
				"convertTable", null);
		lang.newText(new Coordinates(20, 680), "m:     79          n:     4            o:     5            p:      80           q:     81",
				"convertTable", null);
		lang.newText(new Coordinates(20, 700), "r:      82           req:   98        s:     83           t:      6             u:     84",
				"convertTable", null);
		lang.newText(new Coordinates(20, 720), "v:      85           w:     86         x:     87           y:      88           z:     89","convertTable", null);
		lang.newText(new Coordinates(20, 740), "spc:  99          +:     95          -:     96           .:       91           ::     92", "convertTable", null);

		lang.newText(new Coordinates(20, 600), "Each number is converted into a sequence of 3 in oder to detect transmission errors.",
				"convertTableLabel", null);
	}
	
	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		// 1. get the information out of the parameters
		String message = "Lets meet at 10 pm.";
		if (arg1 != null) {
			message = (String) arg1.get("string");
		}
		initialize(arg0);
		header = getHeader();
		SourceCode explanation = lang.newSourceCode(new Coordinates(50, 80), "explanation", null, sourceCodeProps);
		explanation.addCodeLine("Introduction: ", "inr", 0, null);
		explanation.addCodeLine("", "inr1", 0, null);
		explanation.addCodeLine("One-time pad or OTP, also called Vernam-cipher or the perfect cipher, is a crypto algorithm where plaintext is", "explanationText", 0, null);
		explanation.addCodeLine("combined with a random key. It is the only known method to perform mathematically unbreakable encryption.", "explanationText1", 0, null);
		explanation.addCodeLine("We can only talk about one-time pad if four important rules are followed. If these rules are applied correctly, the ", "explanationText2", 0, null);
		explanation.addCodeLine("one-time pad can be proven unbreakable (see Claude Shannon's Communication Theory of Secrecy Systems). ", "explanationText3", 0, null);
		explanation.addCodeLine("However, if only one of these rules is disregarded, the cipher is no longer unbreakable.", "explanationText4", 0, null);
		explanation.addCodeLine("- The key is as long as the plaintext.", "explanationText5", 2, null);
		explanation.addCodeLine("- The key is truly random (not generated by simple computer Rnd functions or such!)", "explanationText6", 2, null);
		explanation.addCodeLine("- There should only be two copies of the key: one for the sender and one for the ", "explanationText6", 2, null);
		explanation.addCodeLine("receiver (some exceptions exist for multiple receivers)", "explanationTextAA", 6, null);
		explanation.addCodeLine("- The keys are used only once, and both sender and receiver must destroy their key after use.", "explanationText7", 2, null);
		explanation.addCodeLine("(Taken from http://users.telenet.be/d.rijmenants/en/onetimepad.htm, July 2010)", "a", 20, null);
		explanation.addCodeLine("", "aa", 0, null);
		explanation.addCodeLine("In this version each number to be transferred, is transferred in a pack of three repetitions of that number. This ", "aaa", 0, null);
		explanation.addCodeLine("is done so that the receiver side can detect transmission errors.", "aaaa", 0, null);
//		explanation.addCodeLine("", "aa98", 0, null);
		
		lang.nextStep("introduction");
		explanation.hide();
		
		
		scEncr = getSourceCodeEncryption();

		scEncr.highlight(0);
		lang.nextStep("encryption-part");

		// make text
		StringArray messageArray = displayMessageText(message);

		// transform to IntArray
		IntArray convertedArray = getConvertedArray(message, messageArray);

		// generate random key
		IntArray randomKey = getRandomArray(convertedArray);

		// addition of keys ---> encrypt
		IntArray cipher = getCipher(convertedArray, randomKey);
		highlightFinhishedEncrypt();

		scEncr.hide();
		lang.nextStep("decryption-part");
		scDecr = getSourceCodeDecryption();
		highlightDecrMethod();

		// decrypt
		IntArray deciphered = getDecipher(randomKey, cipher);

		// re-convert
		getClearMessage(deciphered);

		// finalize
		Text link = lang.newText(new Offset(40, 0, header, AnimalScript.DIRECTION_NE),
				"More Information can be found on http://users.telenet.be/d.rijmenants/en/otp.htm ", "link", null);
		link.changeColor(null, Color.BLUE, null, null);

		return lang.toString();
	}

	private StringArray getClearMessage(IntArray decipher) {
		String[] tmp = { "-1" };

		highlightDecrConvert();
		highlighCell(decipher, 0);

		boolean inFigEnvironment = false;
		StringArray clearText = lang
				.newStringArray(new Offset(0, 20, decipher, AnimalScript.DIRECTION_SW), tmp, "clearText", null, messageArrayProps);
		clearText.hide();
		for (int index = 0; index < decipher.getLength(); index++) {
			lang.nextStep();
			highlighCell(decipher, index);

			// check if we are at the end of the array
			if (index + 1 < decipher.getLength()) {

				// check if current is fig symbol
				if (decipher.getData(index) == 9 && decipher.getData(index + 1) == 0) {
					decipher.highlightCell(index + 1, null, null);
					if (inFigEnvironment) {
						inFigEnvironment = false;
					} else {
						inFigEnvironment = true;
					}
					index++;
					continue;
				}

				// check if we currently are in a fig-environment
				if (inFigEnvironment) {
					decipher.highlightCell(index + 1, null, null);
					decipher.highlightCell(index + 2, null, null);
					clearText = add(clearText, "" + decipher.getData(index));
					index += 2;
					continue;
				}

				// check next element belongs to current one
				if (decipher.getData(index) >= 7 && decipher.getData(index) <= 9) {
					decipher.highlightCell(index + 1, null, null);
					clearText = add(clearText, reConvert(decipher.getData(index) * 10 + decipher.getData(index + 1)));
					index++;
					continue;
				} else {
					clearText = add(clearText, reConvert(decipher.getData(index)));
					continue;
				}

			}

			else {
				// not at the end of the array
				// look at end of array
				clearText = add(clearText, reConvert(decipher.getData(index)));
			}
		}
		lang.nextStep("clear message");
		unhighlightAllCells(decipher, null, null);
		return clearText;
	}

	private StringArray add(StringArray clearText, String toAdd) {
		boolean isInitial = false;
		String[] data = new String[clearText.getLength()];
		for (int i = 0; i < data.length; i++) 
		  data[i] = clearText.getData(i);

		if (clearText.getLength() == 1 && clearText.getData(0).equalsIgnoreCase("-1")) {
			isInitial = true;
		}

		if (isInitial) {
			data[0] = toAdd;
			return lang.newStringArray(clearText.getUpperLeft(), data, "clearText", null, clearText.getProperties());
		}
		return lang.newStringArray(clearText.getUpperLeft(), padArray(data, toAdd), "clearText", null, clearText.getProperties());

	}
	
	private String[] padArray(String[] oldArray, String toAdd) {
	   String[] newArray = new String[oldArray.length + 1];

	    for (int i = 0; i < oldArray.length; i++) {
	      newArray[i] = oldArray[i];
	    }

	    newArray[newArray.length - 1] = toAdd;
	    return newArray;
	}

//	private String[] padArray(StringArray oldArray, String toAdd) {
//		String[] newArray = new String[oldArray.getLength() + 1];
//
//		for (int i = 0; i < oldArray.getLength(); i++) {
//			newArray[i] = oldArray.getData(i);
//		}
//
//		newArray[newArray.length - 1] = toAdd;
//		return newArray;
//	}

	private String reConvert(int i) {
		String letter = reconvertTable.get(i);
		if (letter == null) {
			return "nA";
		}
		return letter;
	}

	private IntArray getDecipher(IntArray randomKey, IntArray cipher) {
		highlightDecrDecrypt();
		highlighCell(randomKey, 0);
		highlighCell(cipher, 0);

		int[] tmp = { decrypt(cipher.getData(0), randomKey.getData(0)) };

		// create IntArray and text field for the Deciphered Array
		IntArray decipher = lang.newIntArray(new Offset(0, 30, cipher, AnimalScript.DIRECTION_SW), tmp, "decipher", null, arrayProps);

		Text messageText = lang.newText(new Offset(-90, 0, decipher, AnimalScript.DIRECTION_NW), "Decipher:", "decipherText", null);
		messageText.setFont(new Font("SansSerif", Font.BOLD, 16), null, null);
		lang.nextStep();

		for (int i = 1; i < cipher.getLength(); i++) {
			highlighCell(cipher, i);
			highlighCell(randomKey, i);

			decipher = lang.newIntArray(decipher.getUpperLeft(), padArray(decipher, decrypt(cipher.getData(i), randomKey.getData(i))),
					"decipher", null, arrayProps);
			
			// if last iteration
			if(i != cipher.getLength() - 1)
				lang.nextStep();
		}
		unhighlightAllCells(cipher, null, null);
		unhighlightAllCells(randomKey, null, null);
		lang.nextStep("deciphered message");
		return decipher;
	}

	private void highlightDecrMethod() {
		scDecr.highlight(0);
		lang.nextStep();
	}

	private void initialize(AnimationPropertiesContainer arg0) {
		init();
		setArrayProps(arg0);
		setMessageArrayProps(arg0);
		setSourceCodeProps(arg0);
	}

	private IntArray getCipher(IntArray convertedArray, IntArray randomKey) {
		highlightEncrypt();
		highlighCell(convertedArray, 0);
		highlighCell(randomKey, 0);
		int[] tmp = { encrypt(convertedArray.getData(0), randomKey.getData(0)) };

		// create IntArray and text field for the OTP-Key
		IntArray cipher = lang.newIntArray(new Offset(0, 20, randomKey, AnimalScript.DIRECTION_SW), tmp, "cipher", null, arrayProps);

		Text messageText = lang.newText(new Offset(-90, 0, cipher, AnimalScript.DIRECTION_NW), "Cipher:", "cipherText", null);
		messageText.setFont(new Font("SansSerif", Font.BOLD, 16), null, null);
		lang.nextStep();

		for (int i = 1; i < convertedArray.getLength(); i++) {
			highlighCell(convertedArray, i);
			highlighCell(randomKey, i);

			cipher = lang.newIntArray(cipher.getUpperLeft(), padArray(cipher, encrypt(convertedArray.getData(i), randomKey.getData(i))),
					"cipher", null, arrayProps);

			// if last iteration
			if(i != convertedArray.getLength() - 1)
				lang.nextStep();
		}
		unhighlightAllCells(convertedArray, null, null);
		unhighlightAllCells(randomKey, null, null);
		lang.nextStep("full Cipher");
		return cipher;
	}

	private int encrypt(int value, int key) {
		int value2 = value;
    if (value2 < key) {
			value2 += 10;
		}
		return value2 - key;
	}

	private int decrypt(int cip, int key) {
		return (cip + key) % 10;
	}

	/**
	 * creates the random array filled with random numbers between 0-9 and a text field left to it
	 * 
	 * @return the created array
	 */
	private IntArray getRandomArray(IntArray convertedArray) {
		highlightGenerateRandEnrypt();
		int[] tmp = { ((int) (Math.random() * 10d)) };

		// create IntArray and text field for the OTP-Key
		IntArray randArray = lang.newIntArray(new Offset(0, 20, convertedArray, AnimalScript.DIRECTION_SW), tmp, "randArray", null, arrayProps);
		randArray.hide();
		Text messageText = lang.newText(new Offset(-90, 0, randArray, AnimalScript.DIRECTION_NW), "OTP-Key:", "randKey", null);
		messageText.setFont(new Font("SansSerif", Font.BOLD, 16), null, null);

		for (int i = 1; i < convertedArray.getLength(); i++) {
			randArray = lang.newIntArray(randArray.getUpperLeft(), padArray(randArray, ((int) (Math.random() * 10d))), "randArray", null,
					arrayProps);
		}
		lang.nextStep("OTP-Key");
		return randArray;
	}

	private IntArray getConvertedArray(String message, StringArray messageArray) {
		highlightConvertEnrypt();
		int[] init = { -1 };
		IntArray convertArray = lang.newIntArray(new Offset(-10, 40, messageArray, AnimalScript.DIRECTION_SW), init, "convertIntArray", null,
				arrayProps);
		convertArray.hide();
		Text messageText = lang.newText(new Offset(-90, 0, convertArray, AnimalScript.DIRECTION_NW), "Convert.:", "randKey", null);
		messageText.setFont(new Font("SansSerif", Font.BOLD, 16), null, null);
		
		boolean inFIGEnviroment = false;

		int messageHighl = 0;

		// fill arrayList with converted letters
		for (int i = 0; i < messageArray.getLength(); i++) {
			messageArray.unhighlightCell(messageHighl - 1, null, null);
			messageArray.highlightCell(messageHighl, null, null);

			// check if were are looking at a number
			String currentLiteral = messageArray.getData(i);

			if (isNumber(currentLiteral)) {
				if (!inFIGEnviroment) {
					inFIGEnviroment = true;
					convertArray = addToIntArray(convertArray, add(convertArray, "FIG"));
					convertArray = addToIntArray(convertArray, add(convertArray, currentLiteral));
				} else {
					// we are currently in a FIG-Environment and therefore we only add the number
					convertArray = addToIntArray(convertArray, add(convertArray, currentLiteral));
				}
			}

			else {
				// the current literal is a letter
				if (!inFIGEnviroment) {
					convertArray = addToIntArray(convertArray, add(convertArray, currentLiteral));
				} else {
					// in the last step we were in a FIG-Environment and our current literal is a
					// letter, so we have to close the FIG-Environment and add the letter
					inFIGEnviroment = false;
					convertArray = addToIntArray(convertArray, add(convertArray, "FIG"));
					convertArray = addToIntArray(convertArray, add(convertArray, currentLiteral));
				}
			}
			
			// if last iteration do last to steps out of the iteration scope
			if(i != messageArray.getLength() - 1) {
				lang.nextStep();
				messageHighl++;
			}
		}
		// commands for the last iteration
		lang.nextStep("converted message");
		messageHighl++;
		
		messageArray.unhighlightCell(messageArray.getLength() - 1, null, null);
		return convertArray;
	}

	/**
	 * returns a new intArray with a new int[]
	 */
	private IntArray addToIntArray(IntArray conv, int[] toAdd) {
		return lang.newIntArray(conv.getUpperLeft(), toAdd, "convertIntArray", null, arrayProps);
	}

	/**
	 * add a number-representation of a literal to an int-array
	 * 
	 * @param numberRep int-array
	 * @param currentLiteral literal to add in its number-representation
	 * @return
	 */
	private int[] add(IntArray numberRep, String currentLiteral) {
	  int[] helper = new int[numberRep.getLength()];
	  for (int i = 0; i < helper.length; i++)
	    helper[i] = numberRep.getData(i);
	  return add(helper, currentLiteral);
	}
	
	private int[] add(int[] numberRep, String currentLiteral) {
		int[] added = numberRep;
		boolean freshInit = false;

		// check if we are dealing with a freshly initialized array here
		if (added.length == 1 && added[0] == -1) {
			freshInit = true;
		}

		if (isNumber(currentLiteral)) {
			if (freshInit) {
				added[0] = convertLetter(currentLiteral);
			} else {
				added = padArray(added, convertLetter(currentLiteral));
			}

			added = padArray(added, convertLetter(currentLiteral));
			added = padArray(added, convertLetter(currentLiteral));
		} else {
			int i = convertLetter(currentLiteral);
			if (i > 10) {
				int second = i % 10;
				int first = i / 10;
				if (freshInit) {
					added[0] = first;
				} else {
					added = padArray(added, first);
				}
				added = padArray(added, second);
			} else {

				if (freshInit) {
					added[0] = i;
				} else {
					added = padArray(added, i);
				}

			}
		}
		return added;
	}

	private StringArray displayMessageText(String mes) {
		String message = mes.trim();

		String[] messageArray = makeStringArray(message);

		Text messageText = lang.newText(new Coordinates(20, 100), "Message:", "messageText", null);
		messageText.setFont(new Font("SansSerif", Font.BOLD, 16), null, null);
		lang.nextStep();

		StringArray stringArray = lang.newStringArray(new Offset(30, 0, messageText, AnimalScript.DIRECTION_NE), messageArray, "messageIntString",
				null, messageArrayProps);

		lang.nextStep("Message Text");
		return stringArray;
	}

	private void createConvertTable() {
		convertTable.put("code", 0);
		convertTable.put("a", 1);
		convertTable.put("e", 2);
		convertTable.put("i", 3);
		convertTable.put("n", 4);
		convertTable.put("o", 5);
		convertTable.put("t", 6);
		convertTable.put("b", 70);
		convertTable.put("c", 70);
		convertTable.put("d", 72);
		convertTable.put("f", 73);
		convertTable.put("g", 74);
		convertTable.put("h", 75);
		convertTable.put("j", 76);
		convertTable.put("k", 77);
		convertTable.put("l", 78);
		convertTable.put("m", 79);
		convertTable.put("p", 80);
		convertTable.put("q", 81);
		convertTable.put("r", 82);
		convertTable.put("s", 83);
		convertTable.put("u", 84);
		convertTable.put("v", 85);
		convertTable.put("w", 86);
		convertTable.put("x", 87);
		convertTable.put("y", 88);
		convertTable.put("z", 89);
		convertTable.put("fig", 90);
		convertTable.put(".", 91);
		convertTable.put(":", 92);
		convertTable.put("'", 93);
		convertTable.put("+", 95);
		convertTable.put("-", 96);
		convertTable.put("=", 97);
		convertTable.put("req", 98);
		convertTable.put(" ", 99);

		for (int i = 0; i < 10; i++) {
			numbers.add(i);
		}

		// fill re-convertTable
		Set<String> keySet = convertTable.keySet();
		for (String key : keySet) {
			reconvertTable.put(convertTable.get(key), key);
		}
	}

	/*
	 * ****************************************************
	 * Helper Functions
	 * ****************************************************
	 */
	
	private void highlightDecrConvert() {
		scDecr.unhighlight(1);
		scDecr.unhighlight(2);
		scDecr.unhighlight(3);
		scDecr.highlight(4);
		scDecr.highlight(5);
		scDecr.highlight(6);
		lang.nextStep();

	}

	private void highlightDecrDecrypt() {
		scDecr.unhighlight(0);
		scDecr.highlight(1);
		scDecr.highlight(2);
		scDecr.highlight(3);
		lang.nextStep();
	}

	private void highlighCell(IntArray array, int position) {
		unhighlightAllCells(array, null, null);
		array.highlightCell(position, null, null);
	}

	private void highlightFinhishedEncrypt() {
		scEncr.unhighlight(5);
		scEncr.unhighlight(6);
		scEncr.unhighlight(7);
		scEncr.highlight(8);
		lang.nextStep();
	}

	private void highlightEncrypt() {
		scEncr.unhighlight(4);
		scEncr.highlight(5);
		scEncr.highlight(6);
		scEncr.highlight(7);
		lang.nextStep();
	}

	private void highlightGenerateRandEnrypt() {
		scEncr.unhighlight(1);
		scEncr.unhighlight(2);
		scEncr.unhighlight(3);
		scEncr.highlight(4);
		lang.nextStep();
	}

	private void highlightConvertEnrypt() {
		scEncr.unhighlight(0);
		scEncr.highlight(1);
		scEncr.highlight(2);
		scEncr.highlight(3);
		lang.nextStep();
	}

	/**
	 * creates a string array out of a received string.
	 * 
	 * @param message string to fill the array
	 * @return array
	 */
	private String[] makeStringArray(String message) {
		String[] messageArray = new String[message.length()];
		for (int i = 0; i < message.length(); i++) {
			messageArray[i] = Character.toString(message.charAt(i));
		}
		return messageArray;
	}

	private boolean isNumber(String s) {
		try {
			if (numbers.contains(Integer.valueOf(s))) {
				return true;
			}
		} catch (NumberFormatException e) {
		}
		return false;
	}

	private Integer convertLetter(String s) {
		// check if it is a number
		if (isNumber(s)) {
			int value = Integer.valueOf(s);
			return value;
		}

		Integer value = convertTable.get(s.toLowerCase());
		if (value != null) {
			return value;
		}
		return -1;
	}

	/**
	 * adds one element to an older array -> increasing the size of the old array by one and adding
	 * the new element
	 * 
	 * @param oldArray array
	 * @param newElement element to add
	 * @return padded array
	 */
	private int[] padArray(int[] oldArray, int newElement) {
		int[] newArray = new int[oldArray.length + 1];

		for (int i = 0; i < oldArray.length; i++) {
			newArray[i] = oldArray[i];
		}

		newArray[newArray.length - 1] = newElement;
		return newArray;
	}

	 private int[] padArray(IntArray oldArray, int newElement) {
	    int[] newArray = new int[oldArray.getLength() + 1];

	    for (int i = 0; i < oldArray.getLength(); i++) {
	      newArray[i] = oldArray.getData(i);
	    }

	    newArray[newArray.length - 1] = newElement;
	    return newArray;
	  }

	private void unhighlightAllCells(IntArray array, Timing delay, Timing duration) {
		array.unhighlightCell(0, array.getLength() - 1, delay, duration);
	}

	@Override
	public String getAlgorithmName() {
		return "One-Time Pad";
	}

	@Override
	public String getAnimationAuthor() {
		return "Malcolm Parsons";
	}

	@Override
	public String getCodeExample() {
		return sourceCodeToDisplay;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return "This Animation illustrates a version of the One-Time-Pad Algorithm. The first part of this animation shows how a clear-text messages gets encoded an encrypted. The second part how an enrypted message is decrypted, encoded again and results in the initial clear-text message.";
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
	public String getName() {
		return "One-Time-Pad";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}