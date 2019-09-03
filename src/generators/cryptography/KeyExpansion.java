package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Locale;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import animal.main.Animal;

/**
 * Generator for the Key Expansion from the Advanced Encryption Standard.
 * @author Bernd Conrad, Sandra Amend 
 */
public class KeyExpansion implements ValidatingGenerator {
    private Language lang;
    /** The cipher key given by the user. */
    private String[] cipherKey;
    /** The cipher key given by the user converted for performing the expansion. */
    private int[] key;
    /** The number of rounds. */
    private int nr;
    /** The number of words in the state. */
	private static final int Nb = 4;
	/** The bit size of a word. */
	private static final int wordSize = 32;
	/** True if questions should be displayed. */
	private boolean questions;
	/** The delay for inserting elements during calculation. */
	private MsTiming putDelay = new MsTiming(500);
	/** The delay for unhighlighting elements during calculation. */
	private MsTiming unhighlightDelay = new MsTiming(500);
	/** The duration for highlighting elements. */
	private MsTiming highlightDuration = new MsTiming(500);
	
	/** ArrayList to store the result matrices. */
	private ArrayList<StringMatrix> resultMatrices;
	/** ArrayList to store the column meaning strings. */
	private ArrayList<StringMatrix> columnMeaningMatrices;
	/** HashMap for the texts displayed during the animation (name to corresponding Text). */
	private HashMap<String, Text> textsMap;

	/** The header text. */
	private Text header;
	/** Rectangle behind the header. */
	private Rect hRect;
	/** Frame rectangle. */
//	private Rect bRect;
	/** Color for the rectangle behind the header text. */
	private Color headerBackgroundColor;
	/** Color used for the context highlight in the codes. Also used to highlight in the texts. */
	private Color contextHighlight;
	/** Color used for the highlight in the codes. */
	private Color highlight;
	/** Color for the code. */
	private Color codeColor;
	/** The sbox. */
	private StringMatrix sboxSMat;
	/** Properties for the texts. */
	private TextProperties textProps;
	/** Properties for the header. */
	private TextProperties headerProps;
	/** Properties for the texts shown during calculation. */
	private TextProperties calculationProps;
	/** Font for the source code. Later used for the computations. */
	private Font codeFont;
	/** Font for the texts. */
	private Font textFont;
	/** Properties for the rectangle while highlighting the source code. */
	private RectProperties highlightRectProps;
	/** Properties for the source codes. */
	private SourceCodeProperties codeProps;
	/** Properties for the extended key matrix and sbox matrix. */
	private MatrixProperties mp;
	/** Properties for the column meaning matrix. */
	private MatrixProperties mpp;
	/** Properties for the key array. */
	private ArrayProperties apk;
	/** Properties for the computation arrays. */
	private ArrayProperties apc;
	/** Set with the String identifiers of the primitives we have to hide ourselves. */
	private HashSet<String> primitivesToHide = new HashSet<String>();
	/** The height of a textline. */
	private int textHeight;
	/** The height of a code textline. */
	private int codeTextHeight;
	
	private static final int xOffsetFromHeader = -220, yOffsetFromHeader = 25, 
			/*textHeight = 25,*/ textSpace = 5, zeroOffset = 0, rectOffset = 5,
			animationWidth = 850, animationHeigth = 800;
	
	/** Array with subscript numbers to create the wᵢ Strings. **/
	private final String[] subscripts = {"₀","₁","₂","₃","₄","₅","₆","₇","₈","₉"};
	
	/** Array for the round constants. */
	private static final int[] rcon = {
			0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36,
	};
	
	/** The S-Box for the substitution. */
	private static final int[][] sbox = {
		{0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76},
		{0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0},
		{0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15},
		{0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75},
		{0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84},
		{0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF},
		{0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8},
		{0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2},
		{0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73},
		{0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB},
		{0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79},
		{0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08},
		{0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A},
		{0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E},
		{0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF},
		{0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16}};
	
	private static final String[][] sboxString = {
		{"63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76"},
		{"CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0"},
		{"B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15"},
		{"04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75"},
		{"09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84"},
		{"53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF"},
		{"D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8"},
		{"51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2"},
		{"CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73"},
		{"60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB"},
		{"E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79"},
		{"E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08"},
		{"BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A"},
		{"70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E"},
		{"E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF"},
		{"8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16"}};
	
    public void init(){
        lang = new AnimalScript("Advanced Encryption Standard - Key Expansion", 
        		"Bernd Conrad, Sandra Amend", 
        		animationWidth, 
        		animationHeigth);
        lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String[][] cipherKey = (String[][])primitives.get("cipherKey");
		
		if (!(cipherKey.length == 4 || cipherKey.length == 6 || cipherKey.length == 8))
			throw new IllegalArgumentException("Ungültige Schlüssellänge!\nGültige Schlüssellängen sind: 128, 192 und 256 (4x4, 6x4, 8x4)");
		
		for (String[] part1 : cipherKey) {
			for (String part2 : part1) {
				if (convertHexStringToInt(part2) < 0x00)
					throw new IllegalArgumentException(part2.toLowerCase() + " < 00!\nSchlüsselteil muss größer sein als \"0x00\".");
				if (convertHexStringToInt(part2) > 0xFF)
					throw new IllegalArgumentException(part2.toLowerCase() + " > ff!\nSchlüsselteil darf nicht größer als \"0xff\" sein.");
				}
		}
		
		return true;
	}
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	// convert given String[] into a int[] by interpreting the hex value
    	String[][] cipherKeyUser = (String[][])primitives.get("cipherKey");
    	cipherKey = new String[cipherKeyUser.length*cipherKeyUser[0].length];
    	for (int i = 0; i < cipherKeyUser.length; ++i) {
    		for (int j = 0; j < cipherKeyUser[0].length; ++j) {
    			cipherKey[i*Nb+j] = cipherKeyUser[i][j];
    		}    		
    	}
    	
        key = new int[cipherKey.length];
        
        for (int i = 0; i < cipherKey.length; ++i) {
        	key[i] = convertHexStringToInt(cipherKey[i]);
        	cipherKey[i] = cipherKey[i].toLowerCase();
        }
        
        switch (cipherKey.length) {
        case 16:
        	nr = 10;
        	break;
        case 24:
        	nr = 12;
        	break;
        case 32:
        	nr = 14;
        	break;
        }

        float textSize = (float)((Integer)primitives.get("textSize"));
        
        codeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		codeFont = (Font)codeProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		
        textProps = (TextProperties)props.getPropertiesByName("text");
        textFont = (Font)textProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
        textFont = textFont.deriveFont(textSize);
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, textFont);
        
        mp = (MatrixProperties)props.getPropertiesByName("matrix");
        
        headerBackgroundColor = (Color)primitives.get("headerBackground");
        
        questions = (Boolean)primitives.get("questions");
        
        // perform keyExpansion
        generateKeyExpansion();
        
        if (questions) lang.finalizeGeneration();
        
        // Prevent Matrix Display Bug
        String refreshBug = lang.toString().replaceAll("refresh", "");
        
        return refreshBug;
    }

    public String getName() {
        return "Advanced Encryption Standard - Key Expansion";
    }

    public String getAlgorithmName() {
        return "Advanced Encryption Standard (AES)";
    }

    public String getAnimationAuthor() {
        return "Bernd Conrad, Sandra Amend";
    }

    public String getDescription(){
        return "Der AES Algorithmus beginnt mit der Schl&uuml;sselexpansion, auch Key Schedule genannt. "
 +"\n"
 +"Dabei wird der Chiffrier-Schl&uuml;ssel zum expandierten Schl&uuml;ssel erweitert. Dieser "
 +"\n"
 +"erweiterte Schl&uuml;ssel enth&auml;lt f&uuml;r die Verschl&uuml;sselungsrunden die Rundenschl&uuml;ssel.";
    }

    public String getCodeExample(){
        return "KeyExpansion( byte key[4*Nk], word w[Nb*(Nr+1)], Nk )"
 +"\n"
 +"begin"
 +"\n"
 +"word temp"
 +"\n"
 +"  i = 0"
 +"\n"
 +"  "
 +"\n"
 +"  while ( i < Nk )"
 +"\n"
 +"    w[i] = word( key[4*i], key[4*i+1], key[4*i+2], key[4*i+3] )"
 +"\n"
 +"    i = i+1"
 +"\n"
 +"  end while"
 +"\n"
 +"  "
 +"\n"
 +"  while ( i < Nb * (Nr + 1) )"
 +"\n"
 +"    temp = w[i-1]"
 +"\n"
 +"    "
 +"\n"
 +"    if ( i mod Nk = 0 )"
 +"\n"
 +"      temp = SubWord( RotWord( temp ) ) ⨁ Rcon[i/Nk]"
 +"\n"
 +"    else if ( Nk > 6 ∧ i mod Nk = 4 )"
 +"\n"
 +"      temp = SubWord( temp )"
 +"\n"
 +"    end if"
 +"\n"
 +"    "
 +"\n"
 +"    w[i] = w[i-Nk] ⨁ temp"
 +"\n"
 +"    i = i + 1"
 +"\n"
 +"  end while"
 +"\n"
 +"end";
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
    
    // utility functions for converting
	/**
	 * Converts the given value to the hexadecimal string representation with a leading 0
	 * if it would only require one character.
	 * @param value unsigned
	 * @return string with the value represented in base 16 (hexadecimal)
	 */
	public String convertIntToHexString(int value) {
		StringBuilder sb = new StringBuilder();
		String element = Integer.toHexString(value);
    	if (element.length() == 1)
    		sb.append("0");
    	sb.append(element);
    	return sb.toString();
	}
	
	/**
	 * Converts the given String value to a integer value.
	 * @param value hexadecimal String representation of a number
	 * @return the integer value of the given hex string
	 */
	public int convertHexStringToInt(String value) {
		int intValue = Integer.valueOf(value, 16);
    	return intValue;
	}
	
	/**
	 * Return the given 2D matrix as a formatted string.
	 * @param mat the matrix to print
	 * @return the formatted string
	 */
	public String mat2ToString(int[][] mat, String name) {
		StringBuilder matString = new StringBuilder();
		matString.append(name).append(" =\n");
	    for (int i = 0; i < mat.length; ++i) {
	    	matString.append("\t");
	    	for (int j = 0; j < mat[0].length; ++j) {
	    		matString.append(String.format("%2s ", Integer.toHexString(mat[i][j])));
	    	}
	    	matString.append("\n");
	    }
	    return matString.toString();
	}
	
	/**
	 * Return the given 1D matrix as a formatted string.
	 * @param mat the matrix to print
	 * @return the formatted string
	 */
	public String mat1ToString(int[] mat, String name) {
		StringBuilder matString = new StringBuilder();
		matString.append(name).append(" =\n");
	    for (int i = 0; i < mat.length; ++i) {
	    	matString.append(String.format("\t%2s\n", Integer.toHexString(mat[i])));
	    }
	    return matString.toString();
	}
	
	/**
	 * Return the given 1D matrix as a one line string.
	 * @param mat the matrix to print
	 * @return the string
	 */
	public String mat1ToOneLineString(int[] mat, String name, boolean withName, boolean insertSeparator) {
		StringBuilder matString = new StringBuilder();
		
		if (withName)
			matString.append(name).append(" = ");
		
	    for (int i = 0; i < mat.length; ++i) {
	    	String element = Integer.toHexString(mat[i]);
	    	if (element.length() == 1)
	    		matString.append("0");
	    	matString.append(String.format("%s", element));
	    	if (insertSeparator && i < mat.length-1)
	    		matString.append(", ");
	    	else
	    		matString.append(" ");
	    }
	    return matString.toString();
	}
	
	/**
	 * Converts the given int array into a Sting array in hexadecimal format.
	 * @param intArray the array with the values (unsigned)
	 * @return string array with the values represented in base 16 (hexadecimal)
	 */
	public String[] convertIntArrayToHexStringArray(int[] intArray, boolean appendEmptyCell) {
		int arrayLength = intArray.length;
		if (appendEmptyCell)
			++arrayLength;
		String[] stringArray = new String[arrayLength];
		for (int i = 0; i < intArray.length; ++i)
			stringArray[i] = convertIntToHexString(intArray[i]);
		if (appendEmptyCell)
			stringArray[arrayLength-1] = "  ";
		return stringArray;
	}
	
	// helper functions for performing the key expansion
	/**
	 * Rotates the given word to the left.
	 * @param word the word to rotate
	 * @return the rotated word
	 */
	private int[] rotWord(int[] word) {
		int[] result = {word[1], word[2], word[3], word[0]};
		return result;
	}
	
	/**
	 * Looks up the corresponding byte in the S-Box.
	 * -> Looks up every byte in the given word.
	 * @param b the byte to look up
	 * @return the looked up byte
	 */
	private int[] subWord(int[] b) {
		int[] temp = new int[b.length];
		for (int i = 0; i < b.length; ++i) {
			int firstIndex = (b[i] & 0x00f0) >> 4;
			int secondIndex = b[i] & 0x000f;
			temp[i] = sbox[firstIndex][secondIndex];
		}
		return temp;
	}

	/**
	 * xor
	 * @param array with 4 elements
	 * @param rcon the rcon value to xor with
	 * @return
	 */
	private int[] xor(int[] array, int rcon) {
		int[] result = {array[0] ^ rcon, array[1], array[2], array[3]};
		return result;
	}
	/**
	 * xor
	 * @param 
	 * @param rcon the rcon value to xor with
	 * @param i the iteration
	 * @return
	 */
	private StringArray xor(StringArray array, int rcon, int i, Text rconText) {
		String[] rconContent = {convertIntToHexString(rcon), "00", "00", "00"};
		StringArray rconArray = lang.newStringArray(new Offset(zeroOffset, zeroOffset, rconText, AnimalScript.DIRECTION_NE), 
				rconContent, 
				"rconArray"+i, null, apc);
		lang.nextStep(putDelay.getDelay());
		
		int j = 0;
		
		array.highlightCell(j, null, highlightDuration);
		rconArray.highlightCell(j, null, highlightDuration);
		lang.nextStep(0);
		
		String tmp = array.getData(j);
		int number = Integer.parseInt(tmp, 16);
		number = number ^ rcon;
		rconArray.put(j, "  ", putDelay, null);
		array.put(j, convertIntToHexString(number), putDelay, null);
		lang.nextStep(0);
		rconArray.unhighlightCell(j, unhighlightDelay, null);
		array.unhighlightCell(j, unhighlightDelay, null);
		++j;
		
		while (j < 4) {
			rconArray.highlightCell(j, null, highlightDuration);
			array.highlightCell(j, null, highlightDuration);
			lang.nextStep(0);
			rconArray.put(j, "  ", putDelay, null);
			lang.nextStep(0);
			rconArray.unhighlightCell(j, unhighlightDelay, null);
			array.unhighlightCell(j, unhighlightDelay, null);
			++j;			
		}
		return array;
	}
		
	// utility methods for creating AnimalScript Code
	/**
	 * Generates the code for unhighlighting a range of cells in an array.
	 * The generated AnimalScript line works with Animal. In contrast to the code the API generates.
	 * @param arrayName
	 * @param from 
	 * @param to
	 */
	private void generateUnhighlightArrayCell(String arrayName, int from, int to) {
		// unhighlightArrayCell on "name" from i to j
		StringBuilder sb = new StringBuilder("unhighlightArrayCell on ");
		sb.append("\"").append(arrayName).append("\"");
		sb.append(" from ").append(from).append(" to ").append(to);
		sb.append(" after ").append(unhighlightDelay.getDelay()).append(" ").append(unhighlightDelay.getUnit());
		lang.addLine(sb);
	}

	/**
	 * Hides all Primitives except the header.
	 */
	private void hideAllButHeader() {
		lang.hideAllPrimitives();
		
		// hide all primitives listed in the set and remove them from the set
		if (!primitivesToHide.isEmpty()) {
			ArrayList<String> removeIds = new ArrayList<String>();
			
			for (String identifier : primitivesToHide) {
				lang.addLine("hide \"" + identifier + "\"");
				removeIds.add(identifier);
			}
			primitivesToHide.removeAll(removeIds);
		}
		
		header.show();
		hRect.show();
//		bRect.show();
	}
	
	private void initProps() {		
		highlightRectProps = new RectProperties();
		highlightRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		highlightRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		
		mpp = new MatrixProperties();
		mpp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");
		mpp.set(AnimationPropertiesKeys.FONT_PROPERTY, mp.get(AnimationPropertiesKeys.FONT_PROPERTY));
		mpp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
				
		apk = new ArrayProperties();
		apk.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		apk.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.white);
		apk.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		apk.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(255, 255, 170));
		apk.set(AnimationPropertiesKeys.FONT_PROPERTY, 
				(Font)textProps.get(AnimationPropertiesKeys.FONT_PROPERTY));
		
		calculationProps = new TextProperties();
		calculationProps.set(AnimationPropertiesKeys.FONT_PROPERTY, codeFont);
		calculationProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
				(Color)codeProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		
		apc = new ArrayProperties();
		apc.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		apc.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.white);
		apc.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		apc.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(255, 255, 170));
		apc.set(AnimationPropertiesKeys.FONT_PROPERTY, codeFont);
		
		contextHighlight = (Color)codeProps.get(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY);
		highlight = (Color)codeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
		codeColor = (Color)codeProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		headerProps = new TextProperties();
		Font headerFont = (Font)textProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		headerFont = headerFont.deriveFont((float)headerFont.getSize()+6);
		headerFont = headerFont.deriveFont(Font.BOLD);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);
		Color headerColor = Color.white;
		float luminance = 0.299f*((float)headerBackgroundColor.getRed()/255) +
				0.587f*((float)headerBackgroundColor.getGreen()/255) +
				0.114f*((float)headerBackgroundColor.getBlue()/255);
		if (luminance > 0.5f)
			headerColor = Color.black;
		headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, headerColor);
		
		Font textFont = (Font)textProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		FontMetrics fm = Animal.getConcreteFontMetrics(textFont);
		textHeight = fm.getAscent() + fm.getDescent();
		
		fm = Animal.getConcreteFontMetrics(codeFont);
		codeTextHeight = fm.getAscent() + fm.getDescent();
	}
	
	// key expansion function
	/**
	 * Creates the AnimalScript code for the KeyExpansion.
	 */
	public void generateKeyExpansion() {
		// initialize the general text properties
		initProps();
		
		if (questions) {
			// Question Groups
			lang.addQuestionGroup(new QuestionGroupModel("intro", 1));
			lang.addQuestionGroup(new QuestionGroupModel("exponce", 1));
			lang.addQuestionGroup(new QuestionGroupModel("exprepeat", 1));
		}
		
		// header and description
		showHeader();
		showGeneralDescription();		
		lang.nextStep("Description");
		
		// explain functions
		hideAllButHeader();
		showFunctions();
		lang.nextStep("   2");
		
		// show source code
		hideAllButHeader();
		showSource();
		lang.nextStep("   2");
		
		performKeyExpansion();
	}
	
	/**
	 * Computes the expanded key containing the round keys.
	 * @return the expanded key
	 */
	public int[][] performKeyExpansion() {
		int expandedLength = Nb * (nr + 1);
		int nk = key.length / Nb;
		int matrixParts = 0;
		if (key.length == 16)
			matrixParts = 5;
		else if (key.length == 24)
			matrixParts = 4;
		else
			matrixParts = 3;
		
		// array to store the result matrices
		resultMatrices = new ArrayList<StringMatrix>();
		// array to store the column meaning strings
		columnMeaningMatrices = new ArrayList<StringMatrix>();
		// hashmap for the texts displayed during the animation (name to corresponding Text)
		textsMap = new HashMap<String, Text>();
		// list to store the ranges (for selecting the right resultMatrix)
		ArrayList<Integer> ranges = new ArrayList<Integer>();
		
		// array for the expanded key -> we can only show a part of the expanded key at once, so we keep one array with the whole key
		int[][] w = new int[expandedLength][4];
		
		hideAllButHeader();
		Text cipherKeyText = lang.newText(new Offset(zeroOffset, zeroOffset, "source", AnimalScript.DIRECTION_NW), 
				"Schlüssel = ",
				"cipherKeyText", null, textProps);
		textsMap.put(cipherKeyText.getName(), cipherKeyText);
		StringArray cipherKeyArray = lang.newStringArray(new Offset(zeroOffset, -2, cipherKeyText, AnimalScript.DIRECTION_NE), 
				cipherKey,
				"cipherKeyArray", null, apk);
		Text expandedKeyHead = lang.newText(new Offset(zeroOffset, (int)(1.5*textHeight), cipherKeyText, AnimalScript.DIRECTION_NW), 
				"Expandierter Schlüssel:", 
				"expandedKeyText", null, textProps);
		textsMap.put(expandedKeyHead.getName(), expandedKeyHead);
		TextProperties justInfoProps = new TextProperties();
		Font justInfoFont = textFont.deriveFont((float)textFont.getSize()-2);
		justInfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, justInfoFont);
		Text justInfo = lang.newText(new Offset(zeroOffset, textHeight, expandedKeyHead, AnimalScript.DIRECTION_NW),
				"Während der Berechnung wird hier nur ein Teil des erweiterten Schlüssels angezeigt.", 
				"justInfo", null, justInfoProps);
		justInfo.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
		textsMap.put(justInfo.getName(), justInfo);
		
		String[] colMeaning = new String[expandedLength];
		
		// get all Strings from w_0 to the end
		for (int i = 0; i < expandedLength; ++i) {
			int leftDigit;
			int rightDigit;
			
			if (i < 0) {
				leftDigit = 0;
				rightDigit = i;
			} else {
				rightDigit = i % 10;
				leftDigit = (i - rightDigit) / 10;
			}
			
			if (leftDigit > 0)
				colMeaning[i] = "w"+subscripts[leftDigit]+subscripts[rightDigit];
			else
				colMeaning[i] = "w"+subscripts[rightDigit];
		}
		
		int n = 0;
		// initialize the column meaning matrices and (empty) expanded key matrices		
		for (int i = 0; i < expandedLength; i+=(matrixParts-1)*nk, ++n) {
			// compute the current indices
			int from = i;
			int to = i + (matrixParts*nk);
			
			if (to > expandedLength)
				to = expandedLength;
			
			// add the last index to the ranges list
			ranges.add(to);
			
			int dummyLength = to - from;
			
			// for same width
			String[][] dummyMeaning = new String[1][dummyLength];
			for (int j = 0; j < dummyMeaning[0].length; ++j)
				dummyMeaning[0][j] = "  ";
			
			
			// all column meaning matrices will be displayed at the same position
			StringMatrix columnMeaning = lang.newStringMatrix(new Offset(textHeight, (int)(1.5*textHeight), justInfo, AnimalScript.DIRECTION_NW), 
					dummyMeaning, 
					"columnMeaning"+n, null, mpp);
			for (int j = 0; j < (matrixParts*nk); ++j) {
				int wIndex = from + j;
				if (!(wIndex > to-1))
					columnMeaning.put(0, j, colMeaning[wIndex], null, null);
			}
						
			// store the created matrix
			columnMeaningMatrices.add(columnMeaning);
			
			// create the empty matrices for the expanded key
			String[][] dummy = new String[4][dummyLength];
			for (int y = 0; y < dummy.length; ++y)
				for (int x = 0; x < dummy[0].length; ++x)
					dummy[y][x] = "  ";
			// the expanded key gets displayed under the column meaning
			StringMatrix expandedKeyPart = lang.newStringMatrix(new Offset(zeroOffset, (int)(1.5*textHeight), columnMeaningMatrices.get(0), AnimalScript.DIRECTION_NW),
					dummy, 
					"extendedKeyMat"+n, null, mp);
			
			// store the created matrix
			resultMatrices.add(expandedKeyPart);
			
			// only show the first column meaning -> others will be displayed later
			if (n != 0) {
				columnMeaningMatrices.get(columnMeaningMatrices.size()-1).hide();
				resultMatrices.get(resultMatrices.size()-1).hide();
			}
		}
				
		int keyLength = nk * wordSize;
		Text values = lang.newText(new Offset(-textHeight, textHeight, resultMatrices.get(0), AnimalScript.DIRECTION_SW), 
				"" + keyLength + " Bit Schlüssel → Nk = " + nk + ", Nb = " + Nb + ", Nr = " + nr, 
				"values", null, textProps);
		textsMap.put(values.getName(), values);
		
		SourceCode source1 = lang.newSourceCode(new Offset(zeroOffset, textHeight, values, AnimalScript.DIRECTION_SW), 
				"source1", null, codeProps);
		source1.addCodeLine("KeyExpansion(", "", 0, null);
		source1.addCodeElement("byte key[4*Nk]", "", 0, null);
		source1.addCodeElement(",", "", 0, null);
		source1.addCodeElement("word w[Nb*(Nr+1)]", "", 0, null);
		source1.addCodeElement(",", "", 0, null);
		source1.addCodeElement("Nk", "", 0, null);
		source1.addCodeElement(")", "", 0, null);
		source1.addCodeLine("begin", "", 0, null);
		source1.addCodeLine("i = 0", "", 1, null);
		source1.addCodeLine("", "", 1, null);
		source1.addCodeLine("while (", "", 1, null);
		source1.addCodeElement("i < Nk", "", 1, null);
		source1.addCodeElement(")", "", 1, null);
		source1.addCodeLine("w[i] = word( key[4*i], key[4*i+1], key[4*i+2], key[4*i+3] )", "", 2, null);
		source1.addCodeLine("i = i+1", "", 2, null);
		source1.addCodeLine("end while", "", 1, null);
		source1.addCodeLine("...", "", 1, null);
		
		if (questions) {
			lang.nextStep();
			
			FillInBlanksQuestionModel wArrayQuestion = new FillInBlanksQuestionModel("wArrayQuestion");
			wArrayQuestion.setGroupID("exponce");
			wArrayQuestion.setPrompt("Wie viele Wörter enthält das Array \'w\'?");
			wArrayQuestion.addAnswer(Integer.toString(expandedLength), 1, "Richtig. Das Array enthält " + expandedLength + ", denn " + Nb + "*(" + nr + "+1) = " + expandedLength + ".");
			lang.addFIBQuestion(wArrayQuestion);
		}
		
		lang.nextStep("Berechnung");

		justInfo.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.lightGray, null, null);
		
		source1.highlight(0, 0, true);
		source1.highlight(0, 1, false);
		source1.highlight(0, 2, true);
		source1.highlight(0, 3, true);
		source1.highlight(0, 4, true);
		source1.highlight(0, 5, true);
		source1.highlight(0, 6, true);
		
		Text keyText = lang.newText(new Offset(codeTextHeight, zeroOffset, source1, AnimalScript.DIRECTION_NE), 
				"→ key[4*"+nk+"] = key["+4*nk+"]", 
				"keyText", null, calculationProps); 
		keyText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
		textsMap.put(keyText.getName(), keyText);
		
		lang.nextStep();
		
		source1.highlight(0, 1, true);
		source1.highlight(0, 3, false);
		
		keyText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextHighlight, null, null);

		// only displayed once, so don't store it
		Text wText = lang.newText(new Offset(zeroOffset, codeTextHeight, keyText, AnimalScript.DIRECTION_NW), 
				"→ w["+Nb+"*("+nr+"+1)] = w["+Nb*(nr+1)+"]", 
				"wText", null, calculationProps); 
		wText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
		
		lang.nextStep();

		source1.highlight(0, 3, true);
		source1.highlight(0, 5, false);
		
		wText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,  contextHighlight, null, null);

		// only displayed once, so don't store it
		Text nkText = lang.newText(new Offset(zeroOffset, codeTextHeight, wText, AnimalScript.DIRECTION_NW), 
				"→ Nk = "+nk, 
				"nkText", null, calculationProps); 
		nkText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
		
		lang.nextStep();
		
		nkText.hide();
		wText.hide();
		keyText.setText("→ key["+4*nk+"], w["+Nb*(nr+1)+"]", null, null);
		
		lang.nextStep();
		
		source1.unhighlight(0);
		source1.unhighlight(0, 1, false);
		source1.unhighlight(0, 2, false);
		source1.unhighlight(0, 3, false);
		source1.unhighlight(0, 4, false);
		source1.unhighlight(0, 5, false);
		source1.unhighlight(0, 6, false);
		
		source1.highlight(1);
		
		lang.nextStep();
		
		source1.unhighlight(1);
		
		// temporary array for the current computation
		int[] temp = new int[4];
		
		int i = 0;

		source1.highlight(2);
		String iVarLeft = "i = ";
		Text iVar = lang.newText(new Offset(zeroOffset, 2*codeTextHeight, keyText, AnimalScript.DIRECTION_NW),
				iVarLeft.concat(Integer.toString(i)),
				"iVar", null, calculationProps); 
		iVar.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
		textsMap.put(iVar.getName(), iVar);

		lang.nextStep("   1st while");
		
		iVar.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextHighlight, null, null);
		source1.unhighlight(2);
		source1.highlight(4, 0, true);
		source1.highlight(4, 1, false);
		source1.highlight(4, 2, true);
		
		// condition for the first while
		boolean condition = i < nk;
		
		String cond1Left = Integer.toString(i) + " < " + nk + " = ";
		Text cond = lang.newText(new Offset(zeroOffset, codeTextHeight, iVar, AnimalScript.DIRECTION_SW), 
				cond1Left.concat(Boolean.toString(condition)), 
				"condition", null, calculationProps); 
		cond.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
		textsMap.put(cond.getName(), cond);
		
		lang.nextStep();
		
		// copy key into w
		// first while -> here the current result matrix is always the first
		while (condition) {
			source1.unhighlight(4, 0, false);
			source1.unhighlight(4, 1, false);
			source1.unhighlight(4, 2, false);
			source1.highlight(5);
			cond.hide();
			
			String wLeft = "w[".concat(Integer.toString(i)).concat("]");
			String[] currentWOne = {wLeft, 								// 0
					" = word(key[" + Integer.toString(4*i) + "], ",   	// 1
					"key[" + Integer.toString(4*i+1) + "], ",     		// 2
					"key[" + Integer.toString(4*i+2) + "], ",     		// 3
					"key[" + Integer.toString(4*i+3) + "])" 			// 4
					};
			
			if (!textsMap.containsKey("currentWFirst")) {
				Text currentWFirstLine = lang.newText(new Offset(zeroOffset, codeTextHeight, cond, AnimalScript.DIRECTION_NW), 
						currentWOne[0]+currentWOne[1], 
						"currentWFirst", null, calculationProps); 
				currentWFirstLine.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				Text currentWSecondLine = lang.newText(new Offset(zeroOffset, codeTextHeight, currentWFirstLine, AnimalScript.DIRECTION_NW),
						"  "+currentWOne[2]+currentWOne[3]+currentWOne[4], 
						"currentWSecond", null,  calculationProps); 
				currentWSecondLine.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				textsMap.put(currentWFirstLine.getName(), currentWFirstLine);
				textsMap.put(currentWSecondLine.getName(), currentWSecondLine);
			} else {
				Text currentWFirstLine = textsMap.get("currentWFirst");
				Text currentWSecondLine = textsMap.get("currentWSecond");
				currentWFirstLine.setText(currentWOne[0]+currentWOne[1], null, null);
				currentWSecondLine.setText("  "+currentWOne[2]+currentWOne[3]+currentWOne[4], null, null);
				currentWFirstLine.show();
				currentWSecondLine.show();				
			}
			
			lang.nextStep(putDelay.getDelay());
			
			cipherKeyArray.highlightCell(4*i, 4*i+3, null, highlightDuration);
			
			lang.nextStep(highlightDuration.getDelay());
			
			textsMap.get("currentWFirst").hide();
			textsMap.get("currentWSecond").hide();
			
			w[i][0] = key[4*i];
			w[i][1] = key[4*i+1];
			w[i][2] = key[4*i+2];
			w[i][3] = key[4*i+3];
			
			if (!textsMap.containsKey("currentW")) {
				Text currentW = lang.newText(new Offset(zeroOffset, codeTextHeight, cond, AnimalScript.DIRECTION_NW),
						mat1ToOneLineString(w[i], wLeft, true, false), 
						"currentW", null, calculationProps); 
				currentW.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				textsMap.put(currentW.getName(), currentW);
			} else {
				Text currentW = textsMap.get("currentW");
				currentW.setText(mat1ToOneLineString(w[i], wLeft, true, false), null, null);
				currentW.show();
			}
			
			lang.nextStep(0);
			
			resultMatrices.get(0).highlightCellRowRange(0, 3, i, null, highlightDuration);
			
			resultMatrices.get(0).put(0, i, convertIntToHexString(w[i][0]), putDelay, null);
			resultMatrices.get(0).put(1, i, convertIntToHexString(w[i][1]), putDelay, null);
			resultMatrices.get(0).put(2, i, convertIntToHexString(w[i][2]), putDelay, null);
			resultMatrices.get(0).put(3, i, convertIntToHexString(w[i][3]), putDelay, null);
			
			lang.nextStep(highlightDuration.getDelay());

			resultMatrices.get(0).unhighlightCellRowRange(0, 3, i, unhighlightDelay, null);
			
//			cipherArray.unhighlightCell(4*i, 4*i+3, null, null); // The Api generates: highlightArrayElem on "cipherArray" from 0 to 3  -> I don't want to highlight here ...
//			cipherArray.unhighlightElem(4*i, 4*i+3, null, null); // The Api generates: unhighlightArrayElem on "cipherArray" from 0 to 3  -> Animal does not unhighlight it ...
			generateUnhighlightArrayCell(cipherKeyArray.getName(), 4*i, 4*i+3); // If we do this it gets unhighlighted by Animal
			
			lang.nextStep();
			
			source1.unhighlight(5);
			source1.highlight(6);
			
			++i;
			
			textsMap.get("iVar").setText(iVarLeft.concat(Integer.toString(i)), null, null);
			textsMap.get("iVar").changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
			textsMap.get("currentW").hide();
			
			lang.nextStep();
			
			// condition  for first while
			condition = i < nk;
			
			source1.unhighlight(6);
			source1.highlight(4, 0, true);
			source1.highlight(4, 1, false);
			source1.highlight(4, 2, true);
			cond.show();
			cond1Left = Integer.toString(i) + " < " + nk + " = ";
			cond.setText(cond1Left.concat(Boolean.toString(condition)), null, null);
			
			textsMap.get("iVar").changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextHighlight, null, null);
			
			lang.nextStep();
		}
		
		source1.unhighlight(4, 0, true);
		source1.unhighlight(4, 1, true);
		source1.unhighlight(4, 2, true);
		source1.highlight(7);
		
		cond.hide();
		
		lang.nextStep();
		
		source1.hide();
		
		SourceCode source2 = lang.newSourceCode(new Offset(zeroOffset, textHeight, values, AnimalScript.DIRECTION_NW), 
				"source2", null, codeProps);
		source2.addCodeLine("...", "lempty2", 1, null); 					// 0
		source2.addCodeLine("while (", "lwhile2", 1, null); 				// 1
		source2.addCodeElement("i < Nb * (Nr + 1)", "", 1, null); 			// 1.1
		source2.addCodeElement(")", "", 1, null);							// 1.2
		source2.addCodeLine("temp = w[i-1]", "ltemp2", 2, null);			// 2
		source2.addCodeLine("", "lempty3", 2, null);						// 3
		source2.addCodeLine("if (", "lif1", 2, null);						// 4
		source2.addCodeElement("i mod Nk == 0", "", 2, null);				// 4.1
		source2.addCodeElement(")", "", 2, null);							// 4.2
		source2.addCodeLine("temp =", "ltemp3", 3, null);					// 5
		source2.addCodeElement("SubWord(", "", 3, null);					// 5.1
		source2.addCodeElement("RotWord( temp )", "", 3, null);				// 5.2
		source2.addCodeElement(")", "", 3, null);							// 5.3
		source2.addCodeElement(" ⨁ Rcon[i/Nk]", "", 3, null);				// 5.4
		source2.addCodeLine("else if (", "lif2", 2, null);					// 6
		source2.addCodeElement("Nk > 6", "", 2, null);						// 6.1
		source2.addCodeElement("∧", "", 2, null);							// 6.2
		source2.addCodeElement("i mod Nk == 4", "", 2, null);				// 6.3
		source2.addCodeElement(")", "", 2, null);							// 6.4
		source2.addCodeLine("temp = SubWord( temp )", "ltemp4", 3, null);	// 7
		source2.addCodeLine("end if", "lif3", 2, null);						// 8
		source2.addCodeLine("", "lempty4", 2, null);						// 9
		source2.addCodeLine("w[i] = w[i-Nk] ⨁ temp", "lw2", 2, null);		// 10
		source2.addCodeLine("i = i + 1", "li3", 2, null);					// 11
		source2.addCodeLine("end while", "lendwhile2", 1, null);			// 12
		source2.addCodeLine("end", "lend", 0, null);						// 13
		
		lang.nextStep("   2nd while");
		
		source2.highlight(1, 0, true);
		source2.highlight(1, 1, false);
		source2.highlight(1, 2, true);
		
		// condition second while
		condition = i < expandedLength;
		
		cond1Left = Integer.toString(i) + " < " + expandedLength + " = ";
		cond.show();
		cond.setText(cond1Left.concat(Boolean.toString(condition)), null, null);
		cond.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
		
		lang.nextStep();
		
		String tempLeft;
		Text tempLeftText, tempLeftTextAdditional, xorText, xorText2, rconText;
		StringArray tempArray, tempArray2, wArray;
		
		// second while
		int lastMatrix = 0;
		while (condition) {
			cond.hide();
			// unhighlight while line
			source2.unhighlight(1, 0, false);
			source2.unhighlight(1, 1, false);
			source2.unhighlight(1, 2, false);
			
			// find the right resultMatrix
			int currentMatrix = 0;
			for (ListIterator<Integer> it = ranges.listIterator(ranges.size()); it.hasPrevious(); ) {
				int currentElement = it.previous();
				if (!(i >= currentElement))
					currentMatrix = ranges.indexOf(currentElement);
			}
			
			if (currentMatrix > lastMatrix) {
				// the matrix has changed, so hide old matrix and display new
				resultMatrices.get(lastMatrix).hide();
				columnMeaningMatrices.get(lastMatrix).hide();
				resultMatrices.get(currentMatrix).show();
				columnMeaningMatrices.get(currentMatrix).show();
				// copy last for w into current matrix
				for (int j = 0; j < nk; ++j) {
					resultMatrices.get(currentMatrix).put(0, j, convertIntToHexString(w[i-nk+j][0]), null, null);
					resultMatrices.get(currentMatrix).put(1, j, convertIntToHexString(w[i-nk+j][1]), null, null);
					resultMatrices.get(currentMatrix).put(2, j, convertIntToHexString(w[i-nk+j][2]), null, null);
					resultMatrices.get(currentMatrix).put(3, j, convertIntToHexString(w[i-nk+j][3]), null, null);
				}
				lang.nextStep("      Teil "+currentMatrix);
			}
			
			// highlight temp line
			source2.highlight(2);			
						
			for (int j = 0; j < Nb; ++j)
				temp[j] = w[i-1][j];

			// we don't align the arrays -> too many issues with the bounding boxes (of the texts)
			tempLeft = "temp = ";			
			String tempLeftAdditional = "w[" + (i-1) + "]";
			if (!textsMap.containsKey("tempLeft")) {
				tempLeftText = lang.newText(new Offset(zeroOffset, codeTextHeight, cond, AnimalScript.DIRECTION_NW), 
						tempLeft, 
						"tempLeft", null, calculationProps); 
				tempLeftText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				tempLeftTextAdditional = lang.newText(new Offset(zeroOffset, zeroOffset, tempLeftText, AnimalScript.DIRECTION_NE), 
						tempLeftAdditional, 
						"tempLeftAdditional", null, calculationProps); 
				tempLeftTextAdditional.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				textsMap.put(tempLeftText.getName(), tempLeftText);
				textsMap.put(tempLeftTextAdditional.getName(), tempLeftTextAdditional);
			} else {
				tempLeftText = textsMap.get("tempLeft");
				tempLeftTextAdditional = textsMap.get("tempLeftAdditional");
				tempLeftText.show();
				tempLeftTextAdditional.show();
				tempLeftText.setText(tempLeft, null, null);
				tempLeftTextAdditional.setText(tempLeftAdditional, null, null);
				tempLeftText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				tempLeftTextAdditional.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
			}
			
			lang.nextStep(putDelay.getDelay());
			
			// compute the index in the current result matrix
			int indexCurrentResultMatrix = i - currentMatrix*(matrixParts*nk);
			if (currentMatrix > 0)
				indexCurrentResultMatrix += currentMatrix*nk;
			
			resultMatrices.get(currentMatrix).highlightCellRowRange(0, 3, indexCurrentResultMatrix-1, null, highlightDuration);
			
			lang.nextStep(putDelay.getDelay());
			
			tempLeftTextAdditional.hide();
			tempLeftText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, 
					codeColor, null, null);
			
			tempArray = lang.newStringArray(new Offset(zeroOffset, zeroOffset, tempLeftText, AnimalScript.DIRECTION_NE), 
					convertIntArrayToHexStringArray(temp, true), 
					"tempArray"+i, null, apc);	
			
			resultMatrices.get(currentMatrix).unhighlightCellRowRange(0, 3, indexCurrentResultMatrix-1, unhighlightDelay, null);
			
			if (questions && i % 2 == 0) {		
				MultipleChoiceQuestionModel tempArrayQuestion = new MultipleChoiceQuestionModel("tempArrayQuestion"+i);	 
				tempArrayQuestion.setGroupID("exprepeat");
				tempArrayQuestion.setPrompt("Was wird im \'temp\' Array stehen nach den if-Anweisungen?");
				
				String answer1 = mat1ToOneLineString(w[i-1], "", false, true);
				String answer2 = mat1ToOneLineString(subWord(w[i-1]), "", false, true);
				String answer3 = mat1ToOneLineString(xor(subWord(rotWord(w[i-1])), rcon[i/nk]), "", false, true);
				
				if (i % nk == 0) {
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer1 + "]", 
							0, "Falsch. \'temp\' enthält [ " + answer3 + "]");
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer2 + "]",
							0, "Falsch. \'temp\' enthält [ " + answer3 + "]");
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer3 + "]",
							1, "Richtig. \'temp\' enthält [ " + answer3 + "]");					
				} else if (nk > 6 && i % nk == 4) {
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer1 + "]", 
							0, "Falsch. \'temp\' enthält [ " + answer2 + "]");
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer2 + "]",
							1, "Richtig. \'temp\' enthält [ " + answer2 + "]");
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer3 + "]",
							0, "Falsch. \'temp\' enthält [ " + answer2 + "]");
				} else {
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer1 + "]", 
							1, "Richtig. \'temp\' enthält [ " + answer1 + "]");
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer2 + "]",
							0, "Falsch. \'temp\' enthält immernoch [ " + answer1 + "]");
					tempArrayQuestion.addAnswer("Das Array enthält [ " + answer3 + "]",
							0, "Falsch. \'temp\' enthält immernoch [ " + answer1 + "]");
				}
				lang.addMCQuestion(tempArrayQuestion);
			}
			
			if (questions && i == nk) {
				lang.nextStep();
				MultipleSelectionQuestionModel secondIfQuestion = new MultipleSelectionQuestionModel("secondIfQuestion");
				secondIfQuestion.setGroupID("exponce");
				
				secondIfQuestion.setPrompt("Unter welchen Vorraussetzungen geht der Algorithmus in die zweite if-Abfrage?");
				secondIfQuestion.addAnswer("Der Schlüssel hat eine Länge von 256 Bit.", 1, "Richtig. Zu erkennen ist dies an Nk = 8 > 6.\n");
				secondIfQuestion.addAnswer("Der Schlüssel hat eine Länge von 192 Bit.", 0, "Falsch, da Nk = 6 <= 6.\n");
				secondIfQuestion.addAnswer("i ist mit Rest 4 durch Nk teilbar.", 1, "Richtig. Zu erkennen ist dies an i % Nk == 4.\n");
				secondIfQuestion.addAnswer("i ist restlos durch Nk teilbar.", 0, "Falsch.\n");
				
				lang.addMSQuestion(secondIfQuestion);
			}
			
			lang.nextStep();
			
			source2.unhighlight(2);
			source2.highlight(4, 0, true);
			source2.highlight(4, 1, false);
			source2.highlight(4, 2, true);
			
			// condition first if
			boolean conditionIf1 = i % nk == 0;
			
			String cond2Left = "(" + Integer.toString(i) + " mod " + nk + " == 0 ) = ";
			cond.show();
			cond.setText(cond2Left.concat(Boolean.toString(conditionIf1)), null, null);
			cond.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
			
			lang.nextStep();
			
			// If we don't have to go into the first if part we have to print the check for the second part here
			if (!conditionIf1) {
				cond.hide();
				// get the conditions ready
				boolean conditionIf2Part1 = nk > 6;
				boolean conditionIf2Part2 = i % nk == 4;
				boolean conditionIf2 = conditionIf2Part1 && conditionIf2Part2;
				// unhighlight first if condition
				source2.unhighlight(4, 0, true);
				source2.unhighlight(4, 1, false);
				source2.unhighlight(4, 2, true);
				// highlight second if condition
				// first part
				source2.highlight(6, 0, true);
				source2.highlight(6, 1, false);
				source2.highlight(6, 2, true);
				source2.highlight(6, 3, true);
				source2.highlight(6, 4, true);
				
				String cond3Left = Integer.toString(nk) + " > 6 = ";
				cond.show();
				cond.setText(cond3Left.concat(Boolean.toString(conditionIf2Part1)), null, null);
				cond.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				
				lang.nextStep();
				// second part
				source2.highlight(6, 1, true);
				source2.highlight(6, 3, false);
				String cond4Left = "(" + Integer.toString(i) + " mod " + nk + " == 4) = ";
				cond.setText(cond4Left.concat(Boolean.toString(conditionIf2Part2)), null, null);
				cond.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				
				lang.nextStep();		
				// whole condition
				source2.highlight(6, 1, false);
				source2.highlight(6, 2, false);
				source2.highlight(6, 3, false);
				String cond5Left = Boolean.toString(conditionIf2Part1) + " ∧ " + Boolean.toString(conditionIf2Part2) + " = ";
				cond.setText(cond5Left.concat(Boolean.toString(conditionIf2)), null, null);
				cond.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				
				lang.nextStep();

				source2.unhighlight(6, 0, true);
				source2.unhighlight(6, 1, false);
				source2.unhighlight(6, 2, true);
				source2.unhighlight(6, 3, true);
				source2.unhighlight(6, 4, true);
				
				if (!conditionIf2) {
					source2.highlight(8);
					cond.hide();
				} else { 
					source2.highlight(7);
					cond.hide();
				}
				
				lang.nextStep();
			}
			
			// generate code inside the if
			if (conditionIf1)
			{
				source2.unhighlight(4, 0, true);
				source2.unhighlight(4, 1, false);
				source2.unhighlight(4, 2, true);
				source2.highlight(5, 0, false);
				source2.highlight(5, 1, false);
				source2.highlight(5, 2, false);
				source2.highlight(5, 3, false);
				source2.highlight(5, 4, false);
				cond.hide();
				
				lang.nextStep();
				
				source2.highlight(5, 0, true);
				source2.highlight(5, 1, true);
				source2.highlight(5, 2, false);
				source2.highlight(5, 3, true);
				source2.highlight(5, 4, true);
				
				lang.nextStep(0);
				
				temp = rotWord(temp); 
				// rotword
				tempArray.highlightCell(0, null, highlightDuration);
				tempArray.highlightCell(4, null, highlightDuration);
				lang.nextStep(highlightDuration.getDelay());
				
				tempArray.put(0, "  ", putDelay, null);
				tempArray.put(4, convertIntToHexString(temp[3]), putDelay, null);
				lang.nextStep(0);
				
				tempArray.unhighlightCell(0, unhighlightDelay, null);
				tempArray.unhighlightCell(4, unhighlightDelay, null);
				lang.nextStep(0);
				// move all elements one left
				String tmp = tempArray.getData(4); // save last entry
				String tmp2 = tempArray.getData(0);
				tempArray.put(4, tmp2, putDelay, null);
				tmp2 = tempArray.getData(1);
				tempArray.put(0, tmp2, putDelay, null);
				tmp2 = tempArray.getData(2);
				tempArray.put(1, tmp2, putDelay, null);
				tmp2 = tempArray.getData(3);
				tempArray.put(2, tmp2, putDelay, null);
				tempArray.put(3, tmp, putDelay, null);
				lang.nextStep();
				///////////////////////////////
				
				source2.highlight(5, 1, false);
				source2.highlight(5, 3, false);
				
				lang.nextStep(0);
				
				temp = subWord(temp);
				// subword
				for (int j = 0; j < Nb; ++j) {
					tempArray.highlightCell(j, null, highlightDuration);
					lang.nextStep(highlightDuration.getDelay());
					tempArray.put(j, convertIntToHexString(temp[j]), putDelay, null);
					lang.nextStep(0);
					tempArray.unhighlightCell(j, unhighlightDelay, null);
				}
				
				lang.nextStep();
				
				source2.highlight(5, 4, false);
				
				lang.nextStep(0);
				
				if (!textsMap.containsKey("xorText")) {
					xorText = lang.newText(new Offset(zeroOffset, zeroOffset, tempLeftText, AnimalScript.DIRECTION_SW), 
							"⨁", 
							"xorText", null, calculationProps);
					textsMap.put(xorText.getName(), xorText);
					
				} else {
					xorText = textsMap.get("xorText");
					xorText.show();
				}

				String rconLeft = "Rcon[" + (i/nk) + "] = ";
				if (!textsMap.containsKey("rconText")) {
					rconText = lang.newText(new Offset(zeroOffset, zeroOffset, xorText, AnimalScript.DIRECTION_SW),
							rconLeft, 
							"rconText", null, calculationProps);
					textsMap.put(rconText.getName(), rconText);
				} else {
					rconText = textsMap.get("rconText");
					rconText.setText(rconLeft, null, null);
					rconText.show();
				}
				
				temp = xor(temp, rcon[i/nk]);
				
				tempArray = xor(tempArray, rcon[i/nk], i, rconText);

				lang.nextStep(putDelay.getDelay());
				
				xorText.hide();
				rconText.hide();
				cond.hide();
				
				lang.nextStep();

				source2.unhighlight(5, 0, false);
				source2.unhighlight(5, 1, false);
				source2.unhighlight(5, 2, false);
				source2.unhighlight(5, 3, false);
				source2.unhighlight(5, 4, false);
				source2.highlight(8);
				
				lang.nextStep();
			}
			else if (nk > 6 && i % nk == 4)
			{								
				temp = subWord(temp);
				// subword
				for (int j = 0; j < Nb; ++j) {
					tempArray.highlightCell(j, null, highlightDuration);
					lang.nextStep(highlightDuration.getDelay());
					tempArray.put(j, convertIntToHexString(temp[j]), putDelay, null);
					lang.nextStep(0);
					tempArray.unhighlightCell(j, unhighlightDelay, null);
				}
				
				lang.nextStep();
				
				source2.unhighlight(7);
				source2.highlight(8);
				
				lang.nextStep();
			}
			
			// unhighlight end if
			source2.unhighlight(8);
			// highlight w[i]
			source2.highlight(10);
			
			cond.hide();
			
			// show w 
			String wLeft = "w[".concat(Integer.toString(i)).concat("]");
			String wRight = "w[".concat(Integer.toString(i-nk)).concat("] ⨁ temp");
			
			if (!textsMap.containsKey("currentWSecondWhile1")) {
				Text currentWSecondWhile1 = lang.newText(new Offset(zeroOffset, (int)(1.5*codeTextHeight), tempLeftText, AnimalScript.DIRECTION_NW), 
						wLeft + " = " + wRight, 
						"currentWSecondWhile1", null, calculationProps);
				textsMap.put(currentWSecondWhile1.getName(), currentWSecondWhile1);
			} else {
				Text currentWSecondLine = textsMap.get("currentWSecondWhile1");
				currentWSecondLine.setText(wLeft + " = " + wRight, null, null);
				currentWSecondLine.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
				currentWSecondLine.show();
			}
						
			lang.nextStep(putDelay.getDelay());
			
			textsMap.get("currentWSecondWhile1").hide();
			
			wLeft = "w[" + i + "]";
			String equals = " = ";
			if (!textsMap.containsKey("currentWSecondWhile2")) {
				Text currentWSecondWhile2 = lang.newText(new Offset(zeroOffset, (int)(1.5*codeTextHeight), tempLeftText, AnimalScript.DIRECTION_NW), 
						wLeft, 
						"currentWSecondWhile2", null, calculationProps);
				Text equalsText = lang.newText(new Offset(zeroOffset, zeroOffset, currentWSecondWhile2, AnimalScript.DIRECTION_NE), 
								equals, 
								"equalsText", null, calculationProps);	
				textsMap.put(currentWSecondWhile2.getName(), currentWSecondWhile2);
				textsMap.put(equalsText.getName(), equalsText);
			} else {
				Text currentWSecondWhile = textsMap.get("currentWSecondWhile2");
				Text equalsText = textsMap.get("equalsText");
				currentWSecondWhile.setText(wLeft, null, null);
				currentWSecondWhile.show();
				equalsText.show();
			}
			
			String[] wStringArray = new String[w[i-nk].length];
			for (int j = 0; j < wStringArray.length; ++j)
				wStringArray[j] = convertIntToHexString(w[i-nk][j]);
			
			wArray = lang.newStringArray(new Offset(zeroOffset, zeroOffset, textsMap.get("equalsText"), AnimalScript.DIRECTION_NE), 
					wStringArray, 
					"wArray"+i, null, apc);	
			
			if (!textsMap.containsKey("xorText2")) {
				xorText2 = lang.newText(new Offset(zeroOffset, codeTextHeight, textsMap.get("equalsText"), AnimalScript.DIRECTION_NW),
						" ⨁ ", 
						"xorText2", null, calculationProps); 
				textsMap.put(xorText2.getName(), xorText2);
			} else {
				xorText2 = textsMap.get("xorText2");
				xorText2.show();
			}
			
			tempArray2 = lang.newStringArray(new Offset(zeroOffset, zeroOffset, xorText2, AnimalScript.DIRECTION_NE), 
					convertIntArrayToHexStringArray(temp, false), 
					"tempArray2n"+i, null, apc);
			
			// xor
			for(int j = 0; j < Nb; ++j) {
				w[i][j] = w[i-nk][j] ^ temp[j];
			
				// generate animation code for xor
				tempArray2.highlightCell(j, null, highlightDuration);
				wArray.highlightCell(j, null, highlightDuration);
				lang.nextStep(highlightDuration.getDelay());
				tempLeftText.hide();
				tempArray.hide();
				tempArray2.put(j, "  ", putDelay, null);
				wArray.put(j, convertIntToHexString(w[i][j]), putDelay, null);
				lang.nextStep(putDelay.getDelay());
				tempArray2.unhighlightCell(j, unhighlightDelay, null);
				wArray.unhighlightCell(j, unhighlightDelay, null);
			}
	
			xorText2.hide();
			tempArray2.hide();				
			
			// highlight where we put the new elements
			resultMatrices.get(currentMatrix).highlightCellRowRange(0, 3, indexCurrentResultMatrix, null, highlightDuration);
			
			resultMatrices.get(currentMatrix).put(0, indexCurrentResultMatrix, convertIntToHexString(w[i][0]), putDelay, null);
			resultMatrices.get(currentMatrix).put(1, indexCurrentResultMatrix, convertIntToHexString(w[i][1]), putDelay, null);
			resultMatrices.get(currentMatrix).put(2, indexCurrentResultMatrix, convertIntToHexString(w[i][2]), putDelay, null);
			resultMatrices.get(currentMatrix).put(3, indexCurrentResultMatrix, convertIntToHexString(w[i][3]), putDelay, null);
			
			lang.nextStep(highlightDuration.getDelay());

			resultMatrices.get(currentMatrix).unhighlightCellRowRange(0, 3, indexCurrentResultMatrix, unhighlightDelay, null);
			
			lang.nextStep();
			
			source2.unhighlight(10);
			source2.highlight(11);
			wArray.hide();
			textsMap.get("equalsText").hide();
			textsMap.get("currentWSecondWhile2").hide();
			
			++i;
			
			iVar.setText(iVarLeft.concat(Integer.toString(i)), null, null);
			iVar.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
			
			lang.nextStep();
			
			source2.unhighlight(11);
			tempLeftText.hide();
			tempArray.hide();
			
			condition = i < expandedLength;

			source2.unhighlight(11);
			source2.highlight(1, 0, true);
			source2.highlight(1, 1, false);
			source2.highlight(1, 2, true);
			cond.show();
			
			cond1Left = Integer.toString(i) + " < " + expandedLength + " = ";
			cond.setText(cond1Left.concat(Boolean.toString(condition)), null, null);
			cond.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, highlight, null, null);
			
			iVar.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextHighlight, null, null);
			
			// update the last matrix index
			lastMatrix = currentMatrix;
			
			lang.nextStep();
		}

		// show the whole result
		cond.hide();
		source2.unhighlight(1, 0, true);
		source2.unhighlight(1, 1, true);
		source2.unhighlight(1, 2, true);
		lang.nextStep();
		
		resultMatrices.get(lastMatrix).hide();
		columnMeaningMatrices.get(lastMatrix).hide();
		source2.hide();
		keyText.hide();
		iVar.hide();
		values.hide();
		justInfo.hide();
		
		int currentResMatIndex = 0;
		// create Array for the result matrices
		int numberOfResultMats = (int) Math.ceil(expandedLength / (double) (matrixParts*nk));
		StringMatrix[] resultMatsFinal = new StringMatrix[numberOfResultMats];
		StringMatrix[] meaningMatsFinal = new StringMatrix[numberOfResultMats];
		// add already computed part from the animation
		resultMatsFinal[currentResMatIndex] = resultMatrices.get(0);
		meaningMatsFinal[currentResMatIndex] = columnMeaningMatrices.get(0);
		resultMatsFinal[currentResMatIndex].show();
		meaningMatsFinal[currentResMatIndex].show();
		currentResMatIndex++;
		while (currentResMatIndex < numberOfResultMats) {
			lang.newText(new Offset(textHeight, zeroOffset, resultMatsFinal[currentResMatIndex-1], AnimalScript.DIRECTION_E), 
					"· · ·", 
					"dotdotdot"+currentResMatIndex, null, textProps);
			
			Offset currentOffsetForArray = new Offset(zeroOffset, textHeight, resultMatsFinal[currentResMatIndex-1], AnimalScript.DIRECTION_SW);
			
			int from = currentResMatIndex*(matrixParts*nk);
			int to = (currentResMatIndex+1)*(matrixParts*nk);
			if (to > expandedLength)
				to = expandedLength;
			
			int dummyLength = matrixParts*nk - (((matrixParts*nk)+from) - to);
						
			String[][] dummyMeaning = new String[1][dummyLength];
			for (int j = 0; j < dummyMeaning[0].length; ++j)
				dummyMeaning[0][j] = "  ";

			String[][] dummy = new String[4][dummyLength];
			for (int y = 0; y < dummy.length; ++y)
				for (int x = 0; x < dummy[0].length; ++x)
					dummy[y][x] = "  ";
			
			StringMatrix currentMeaningMatrix = lang.newStringMatrix(
					currentOffsetForArray, 
					dummyMeaning, 
					"colMeaning"+currentResMatIndex, 
					null, mpp);
			
			Offset currentOffsetForMatrix = new Offset(zeroOffset, (int)(1.5*textHeight), currentMeaningMatrix, AnimalScript.DIRECTION_NW);
						
			StringMatrix currentResMatrix = lang.newStringMatrix(
					currentOffsetForMatrix, 
					dummy, 
					"resultMat"+currentResMatIndex, 
					null, mp);
			
			for (int j = 0; j < (matrixParts*nk); ++j) {
				int wIndex = from + j;
				if (wIndex < to) {
					currentMeaningMatrix.put(0, j, colMeaning[wIndex], null, null);
					for (int k = 0; k < Nb; ++k)
						currentResMatrix.put(k, j, convertIntToHexString(w[wIndex][k]), null, null);
				}
				else 
					break;
			}
			
			resultMatsFinal[currentResMatIndex] = currentResMatrix;
			meaningMatsFinal[currentResMatIndex] = currentMeaningMatrix;
			currentResMatIndex++;
		}		
		lang.nextStep("Ergebnis");
		
		// some matrices wouldn't hide when calling hideAll
		for (int j = 0; j < resultMatsFinal.length; ++j) {
			primitivesToHide. add(resultMatsFinal[j].getName());
			primitivesToHide. add(meaningMatsFinal[j].getName());
		}
		hideAllButHeader();
		showSummary();
		
		return w;
	}

	// methods to create the intro slides and final slide
	/**
	 * Display the header.
	 */
	private void showHeader() {
		// add header
		header = lang.newText(new Coordinates(250, 25), 
				"KeyExpansion", 
				"header", null, headerProps);
		
		RectProperties hRectProps = new RectProperties();
		hRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		hRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, headerBackgroundColor);
		hRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hRect = lang.newRect(new Offset(-rectOffset, -rectOffset, "header", AnimalScript.DIRECTION_NW), 
				new Offset(rectOffset, rectOffset, "header", AnimalScript.DIRECTION_SE), 
				"hRect", null, hRectProps);
		
//		bRect = lang.newRect(new Coordinates(10, 30), 
//				new Coordinates(animationWidth-40, animationHeigth-200), 
//				"hRect", null, highlightRectProps);
	}
	
	/**
	 * Display the Source.
	 */
	private void showSource() {
		SourceCode source = lang.newSourceCode(
				new Offset(zeroOffset, zeroOffset, "descr1", AnimalScript.DIRECTION_NW), 
				"source", null, codeProps);
		source.addCodeLine("KeyExpansion( byte key[4*Nk], word w[Nb*(Nr+1)], Nk )", "lHeader", 0, null);
		source.addCodeLine("begin", "lbegin", 0, null);
		source.addCodeLine("word temp", "ltemp1", 1, null);
		source.addCodeLine("i = 0", "li1", 1, null);
		source.addCodeLine("", "lempty", 1, null);
		source.addCodeLine("while ( i < Nk )", "lwhile1", 1, null);
		source.addCodeLine("w[i] = word( key[4*i], key[4*i+1], key[4*i+2], key[4*i+3] )", "lw1", 2, null);
		source.addCodeLine("i = i+1", "li2", 2, null);
		source.addCodeLine("end while", "lendwhile1", 1, null);
		source.addCodeLine("", "lempty2", 1, null);
		source.addCodeLine("while ( i < Nb * (Nr + 1) )", "lwhile2", 1, null);
		source.addCodeLine("temp = w[i-1]", "ltemp2", 2, null);
		source.addCodeLine("", "lempty3", 2, null);
		source.addCodeLine("if ( i mod Nk == 0 )", "lif1", 2, null);
		source.addCodeLine("temp = SubWord( RotWord( temp ) ) ⨁ Rcon[i/Nk]", "ltemp3", 3, null);
		source.addCodeLine("else if ( Nk > 6 ∧ i mod Nk == 4 )", "lif2", 2, null);
		source.addCodeLine("temp = SubWord( temp )", "ltemp4", 3, null);
		source.addCodeLine("end if", "lif3", 2, null);
		source.addCodeLine("", "lempty4", 2, null);
		source.addCodeLine("w[i] = w[i-Nk] ⨁ temp", "lw2", 2, null);
		source.addCodeLine("i = i + 1", "li3", 2, null);
		source.addCodeLine("end while", "lendwhile2", 1, null);
		source.addCodeLine("end", "lend", 0, null);
		
		Rect rectSource = lang.newRect(new Offset(-5 , -5, "source", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "source", AnimalScript.DIRECTION_SE), 
				"rectSource", null, highlightRectProps);
			
		lang.nextStep("Source Code");
		
		source.highlight(5);
		source.highlight(6);
		source.highlight(7);
		source.highlight(8);
		
		SourceCode firstWhileE = lang.newSourceCode(
				new Offset(textHeight, zeroOffset, rectSource, AnimalScript.DIRECTION_NE), 
				"firstWhileE", null, codeProps);
		firstWhileE.addCodeLine("Kopieren des Schlüssels", "", 0, null);
		firstWhileE.addCodeLine("in den ersten Teil des", "", 0, null);
		firstWhileE.addCodeLine("erweiterten Schlüssels.", "", 0, null);
		
		lang.newRect(new Offset(-5 , -5, firstWhileE, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, firstWhileE, AnimalScript.DIRECTION_SE), 
				"rectFirstWhileE", null, highlightRectProps);
		
		lang.nextStep("   1");
		hideAllButHeader();
		source.show();
		rectSource.show();
		
		source.unhighlight(5);
		source.unhighlight(6);
		source.unhighlight(7);
		source.unhighlight(8);
		source.highlight(10);
		source.highlight(11);
		source.highlight(12);
		source.highlight(13);
		source.highlight(14);
		source.highlight(15);
		source.highlight(16);
		source.highlight(17);
		source.highlight(18);
		source.highlight(19);
		source.highlight(20);
		source.highlight(21);
		
		SourceCode secondWhileE = lang.newSourceCode(
				new Offset(textHeight, zeroOffset, rectSource, AnimalScript.DIRECTION_NE), 
				"secondWhileE", null, codeProps);
		secondWhileE.addCodeLine("Berechnen der Schlüssel", "", 0, null);
		secondWhileE.addCodeLine("für die weiteren Runden", "", 0, null);
		secondWhileE.addCodeLine("basierend auf den vorher-", "", 0, null);
		secondWhileE.addCodeLine("igen Rundenschlüsseln.", "", 0, null);
		
		lang.newRect(new Offset(-5 , -5, secondWhileE, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, secondWhileE, AnimalScript.DIRECTION_SE), 
				"rectSecondWhileE", null, highlightRectProps);
	}
	
	/**
	 * Display the introduction description of the algorithm.
	 */
	private void showGeneralDescription() {
		// show decription
		lang.newText(new Offset(xOffsetFromHeader, yOffsetFromHeader, "hRect", AnimalScript.DIRECTION_SW),
				"Der Advanced Encryption Standard (AES) Algorithmus beginnt mit der Schlüssel-",
				"descr1", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "descr1", AnimalScript.DIRECTION_NW),
				"expansion, auch Key Expansion oder Key Schedule genannt. Dabei wird der",
				"descr2", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "descr2", AnimalScript.DIRECTION_NW),
				"Schlüssel zum expandierten Schlüssel erweitert. Dieser erweiterte Schlüssel", 
				"descr3", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "descr3", AnimalScript.DIRECTION_NW),
				"enthält die Rundenschlüssel für die einzelnen Verschlüsselungsrunden.", 
				"descr4", null, textProps);
		lang.nextStep("Description");

		// explain Nb, Nk and Nr
		TextProperties smallHeader = new TextProperties();
		Font smallHeaderFont = 
				((Font)textProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.BOLD);
		smallHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, smallHeaderFont);
		
		lang.newText(new Offset(zeroOffset, 2*textHeight, "descr4", AnimalScript.DIRECTION_SW), 
				"Definitionen", 
				"defsHead", null, smallHeader);
		
		lang.newText(new Offset(zeroOffset, textHeight, "defsHead", AnimalScript.DIRECTION_NW), 
				"Nk ist die Anzahl der 32 Bit Wörter im Schlüssel. (Nk = 4, 6 oder 8)", 
				"defs1", null, textProps);
		
		lang.newText(new Offset(zeroOffset, textHeight, "defs1", AnimalScript.DIRECTION_NW), 
				"Nb ist die Anzahl der 32 Bit Wörter im Zustand. (Nb = 4)", 
				"defs2", null, textProps);
		
		lang.newText(new Offset(zeroOffset, textHeight, "defs2", AnimalScript.DIRECTION_NW), 
				"Nr ist die Anzahl der Runden. (Nr = 10, 12 oder 14)", 
				"defs3", null, textProps);
		
		if (questions) {
			lang.nextStep();
			
			FillInBlanksQuestionModel nkQuestion = new FillInBlanksQuestionModel("nkQuestion1");
			nkQuestion.setGroupID("intro");
			nkQuestion.setPrompt("Welchen Wert hat Nk bei einem 128 Bit Schlüssel?");
			nkQuestion.addAnswer("4", 1, "Richtig. Die Variable Nk hat den Wert 4, denn 4*32 = 128.");
			lang.addFIBQuestion(nkQuestion);
			
			lang.nextStep();
			
			nkQuestion = new FillInBlanksQuestionModel("nkQuestion2");
			nkQuestion.setGroupID("intro");
			nkQuestion.setPrompt("Welchen Wert hat Nk bei einem 256 Bit Schlüssel?");
			nkQuestion.addAnswer("8", 1, "Richtig. Die Variable Nk hat den Wert 8, denn 8*32 = 256.");
			lang.addFIBQuestion(nkQuestion);
		}
	}
	
	/**
	 * Display the function explanations.
	 */
	private void showFunctions() {
		lang.newText(new Offset(zeroOffset, zeroOffset, "descr1", AnimalScript.DIRECTION_NW),
				"Die Schlüsselexpansion verwendet zwei Funktionen", 
				"descr5", null, textProps);
		Text subWord = lang.newText(
				new Offset(textSpace, zeroOffset, "descr5", AnimalScript.DIRECTION_NE), 
				"SubWord()", 
				"subWordDescr", null, textProps);
		subWord.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, 
				contextHighlight, 
				null, null);
		
		lang.newText(new Offset(textSpace, zeroOffset, subWord, AnimalScript.DIRECTION_NE), 
				"und", 
				"descr6", null, textProps);
		Text rotWord = lang.newText(
				new Offset(textSpace, zeroOffset, "descr6", AnimalScript.DIRECTION_NE), 
				"RotWord()", 
				"rotWordDescr", null, textProps);
		rotWord.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, 
				contextHighlight, 
				null, null);
		
		lang.nextStep("Functions");
		
		// Subword
		Text subWord2 = lang.newText(new Offset(zeroOffset, textHeight, "descr5", AnimalScript.DIRECTION_SW),
				subWord.getText(),
				"subWord", null, textProps);	
		subWord2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, 
				contextHighlight, 
				null, null);
		lang.newText(new Offset(textHeight, zeroOffset, "subWord", AnimalScript.DIRECTION_NE),
				"bekommt ein Wort als Eingabe und wendet die S-Box auf jedes", 
				"subWord1", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "subWord1", AnimalScript.DIRECTION_NW), 
				"der vier Bytes im Wort an.", 
				"subWord2", null, textProps);

		lang.nextStep();
		
		// RotWord
		Text rotWord2 = lang.newText(new Offset(zeroOffset, 2*textHeight, "subWord", AnimalScript.DIRECTION_SW),
				rotWord.getText(),
				"rotWord", null, textProps);	
		rotWord2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, 
				contextHighlight, 
				null, null);
		lang.newText(new Offset(textHeight, zeroOffset, "rotWord", AnimalScript.DIRECTION_NE),
				"bekommt ein Wort als Eingabe und rotiert die Bytes zyklisch eine", 
				"rotWord1", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "rotWord1", AnimalScript.DIRECTION_NW), 
				"Position nach links. [a₀, a₁, a₂, a₃] → [a₁, a₂, a₃, a₀]", 
				"rotWord2", null, textProps);

		if (questions) {
			lang.nextStep();
			
			MultipleChoiceQuestionModel rotateQuestion = new MultipleChoiceQuestionModel("rotateQuestion");
			rotateQuestion.setGroupID("intro");
			String[] rotateWord = {cipherKey[0], cipherKey[1], cipherKey[2], cipherKey[3]};
			String question = "[" + rotateWord[0] + ", " + rotateWord[1] + ", " + rotateWord[2] + ", " + rotateWord[3] + "]";
			String answer1 = "[" + rotateWord[3] + ", " + rotateWord[1] + ", " + rotateWord[2] + ", " + rotateWord[0] + "]";
			String answer2 = "[" + rotateWord[3] + ", " + rotateWord[2] + ", " + rotateWord[1] + ", " + rotateWord[0] + "]";
			String answer3 = "[" + rotateWord[2] + ", " + rotateWord[1] + ", " + rotateWord[3] + ", " + rotateWord[0] + "]";
			String rightAnswer = "[" + rotateWord[1] + ", " + rotateWord[2] + ", " + rotateWord[3] + ", " + rotateWord[0] + "]";
			rotateQuestion.setPrompt("Was ist das Ergebnis von RotWord(" + question + ")?");
			rotateQuestion.addAnswer(answer1, 0, "Falsch. Bei der Antwort wurde nur der erste und letzte Eintrag ausgetauscht.");
			rotateQuestion.addAnswer(answer2, 0, "Falsch. Hier wurde das Array umgedreht und nicht rotiert.");
			rotateQuestion.addAnswer(answer3, 0, "Falsch. Hier wurde das Array nicht rotiert.");
			rotateQuestion.addAnswer(rightAnswer, 1, "Richtig.");
			lang.addMCQuestion(rotateQuestion);
		}
		
		lang.nextStep();
		
		// Rcon
		lang.newText(new Offset(zeroOffset, 2*textHeight, "rotWord", AnimalScript.DIRECTION_SW), 
				"Die Rundenkonstanten", 
				"rcon", null, textProps);
		Text rcon1 = lang.newText(new Offset(textSpace, zeroOffset, "rcon", AnimalScript.DIRECTION_NE), 
				"Rcon[i]", 
				"rcon1", null, textProps);
		rcon1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, 
				contextHighlight, 
				null, null);
		lang.newText(new Offset(textSpace, zeroOffset, "rcon1", AnimalScript.DIRECTION_NE), 
				"berechnen sich durch [xⁱ⁻¹, {00}, {00}, {00}]", 
				"rcon2", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "rcon", AnimalScript.DIRECTION_NW), 
				"im endlichen Feld GF(2⁸).", 
				"rcon3", null, textProps);
		
		if (questions) {
			lang.nextStep();
			
			TrueFalseQuestionModel rconQuestion1 = new TrueFalseQuestionModel("rconQuestion1");
			rconQuestion1.setGroupID("intro");
			rconQuestion1.setPrompt("Der Wert von Rcon[3] ist [" + rcon[3] + ", {00}, {00}, {00}]."); // {00} transforms to {0} ?
			rconQuestion1.setCorrectAnswer(true);
			rconQuestion1.setPointsPossible(1);
			lang.addTFQuestion(rconQuestion1);
			
			lang.nextStep();
			
			TrueFalseQuestionModel rconQuestion2 = new TrueFalseQuestionModel("rconQuestion2");
			rconQuestion2.setGroupID("intro");
			rconQuestion2.setPrompt("Der Wert von Rcon[5] ist [" + rcon[4] + ", {00}, {00}, {00}].");
			rconQuestion2.setCorrectAnswer(false);
			rconQuestion2.setPointsPossible(1);
			lang.addTFQuestion(rconQuestion2);
		}
		
		// s-box
		lang.nextStep("   1");
		hideAllButHeader();
		
		lang.newText(new Offset(zeroOffset, zeroOffset, "descr1", AnimalScript.DIRECTION_NW), 
				"Die", 
				"sbox1", null, textProps);
		Text sbox = lang.newText(new Offset(textSpace, zeroOffset, "sbox1", AnimalScript.DIRECTION_NE), 
				"S-Box", 
				"sbox2", null, textProps);
		sbox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, 
				contextHighlight, 
				null, null);
		lang.newText(new Offset(textSpace, zeroOffset, "sbox2", AnimalScript.DIRECTION_NE), 
				"hat die folgende Form. Für eine genaue Beschreibung, wie die", 
				"sbox3", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "sbox1", AnimalScript.DIRECTION_NW), 
				"Substitution funktioniert, schauen Sie bitte die Animation von SubByte an.", 
				"sbox4", null, textProps);
		
		Offset sboxMatOffset = new Offset(4*textHeight, textHeight, "sbox4", AnimalScript.DIRECTION_SW);

		sboxSMat  = lang.newStringMatrix(sboxMatOffset, sboxString, "sboxMat", null, mp);
		primitivesToHide.add(sboxSMat.getName());
		
		if (questions) {
			lang.nextStep();
			
			MultipleChoiceQuestionModel sboxQuestion = new MultipleChoiceQuestionModel("sboxQuestion");
			sboxQuestion.setGroupID("intro");
			String[] questionStr = {"b3", "b4", "a3", "3b"};
			int[] question = {Integer.parseInt(questionStr[0], 16), Integer.parseInt(questionStr[1], 16), 
					Integer.parseInt(questionStr[2], 16), Integer.parseInt(questionStr[3], 16)};
			int[] substitute = subWord(question);
			sboxQuestion.setPrompt("Was ist die Substitution von " + questionStr[0] + "?");
			sboxQuestion.addAnswer(Integer.toHexString(substitute[1]), 0, "Falsch. Die Antwort ist die Substitution von " + questionStr[1]);
			sboxQuestion.addAnswer(Integer.toHexString(substitute[2]), 0, "Falsch. Die Antwort ist die Substitution von " + questionStr[2]);
			sboxQuestion.addAnswer(Integer.toHexString(substitute[3]), 0, "Falsch. Die Antwort ist die Substitution von " + questionStr[3]);
			sboxQuestion.addAnswer(Integer.toHexString(substitute[0]), 1, "Richtig.");
			lang.addMCQuestion(sboxQuestion);
		}
	}
	
	/**
	 * Display the summary slide at the end of the animation.
	 */
	private void showSummary() {
		lang.newText(new Offset(xOffsetFromHeader, yOffsetFromHeader, "hRect", AnimalScript.DIRECTION_SW),
				"Die Schlüsselexpansion ist nicht linear, nicht invertierbar und ohne",
				"summary1", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary1", AnimalScript.DIRECTION_NW),
				"Kenntnis des Schlüssels können eventuell bekannte Teile des erweiterten",
				"summary2", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary2", AnimalScript.DIRECTION_NW),
				"Schlüssels in keiner Richtung vollständig rekonstruiert werden.", 
				"summary3", null, textProps);
		
		lang.newText(new Offset(zeroOffset, 2*textHeight, "summary3", AnimalScript.DIRECTION_NW),
				"Sie stellt auch sicher, dass AES keine schwachen Schlüssel hat. Ein", 
				"summary4", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary4", AnimalScript.DIRECTION_NW),
				"schwacher Schlüssel verringert die Sicherheit in einer vorhersehbaren", 
				"summary5", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary5", AnimalScript.DIRECTION_NW),
				"Weise. DES zum Beispiel hat schwache Schlüssel, welche identische", 
				"summary6", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary6", AnimalScript.DIRECTION_NW),
				"Rundenschlüssel für jede Runde erzeugen wodurch die Verschlüsselung", 
				"summary7", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary7", AnimalScript.DIRECTION_NW),
				"selbst invertierend ist.", 
				"summary8", null, textProps);
		
		lang.newText(new Offset(zeroOffset, 2*textHeight, "summary8", AnimalScript.DIRECTION_NW),
				"Die Schlüsselexpansion kann effizient auf vielen verschiedenen Plattformen", 
				"summary9", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary9", AnimalScript.DIRECTION_NW),
				"implementiert werden.", 
				"summary10", null, textProps);
		
		lang.newText(new Offset(zeroOffset, 2*textHeight, "summary10", AnimalScript.DIRECTION_NW),
				"Es reichen zehn Rcon Werte aus, welche entweder als statische Tabelle", 
				"summary11", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary11", AnimalScript.DIRECTION_NW),
				"vorliegen oder zur Laufzeit berechnet werden können.", 
				"summary12", null, textProps);
		
		lang.newText(new Offset(zeroOffset, 2*textHeight, "summary12", AnimalScript.DIRECTION_NW),
				"Die in der Schlüsselexpansion verwendete S-Box ist dieselbe die der Haupt-", 
				"summary13", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary13", AnimalScript.DIRECTION_NW),
				"algorithmus auch verwendet. Es wird also kein extra Speicher dafür benötigt.", 
				"summary14", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary14", AnimalScript.DIRECTION_NW),
				"Des Weiteren kann auch die S-Box zur Laufzeit berechnet werden um Speicher", 
				"summary15", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary15", AnimalScript.DIRECTION_NW),
				"zu sparen.", 
				"summary16", null, textProps);
		
		lang.newText(new Offset(zeroOffset, 2*textHeight, "summary16", AnimalScript.DIRECTION_NW),
				"Die Anzahl der Zugriffe ist für die verschiedenen Schlüssellängen jeweils", 
				"summary17", null, textProps);
		lang.newText(new Offset(zeroOffset, textHeight, "summary17", AnimalScript.DIRECTION_NW),
				"konstant.", 
				"summary18", null, textProps);
		
		lang.nextStep("Zusammenfassung");
	}
}