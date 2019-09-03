package generators.cryptography.caesarcipher;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;



public class MyCaesar extends AnnotatedAlgorithm implements Generator {

	//@author Stephan Arneth <arneth@rbg.informatik.tu-darmstadt.de>
	
	public MyCaesar() {
		
		
	}
	
	
	public void run(String[] input, int shiftoffset, ArrayProperties arrayProp, MatrixProperties mxp) {
		
		// Nur Großschreibung
		for(int i=0; i<input.length; i++) {
			input[i] = input[i].toUpperCase();
		}
		
		TextProperties textProp1 = new TextProperties();
		textProp1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textProp1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 20));
		
		TextProperties textProp2 = new TextProperties();
		textProp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textProp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.ITALIC, 14));
		
		TextProperties textProp3 = new TextProperties();
		textProp3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textProp3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 14));
		
		lang.newText(new Coordinates(20, 50), "Caesar-Chiffre", "title", null, textProp1);
		
		String shift = "shift = " + shiftoffset + ";";
		lang.newText(new Coordinates(300, 100), shift, "shifttext", null, textProp3);
		
		lang.newText(new Coordinates(20, 150), "Eingabewort", "inputheader", null, textProp2);
		
		StringArray eingabeArray = lang.newStringArray(new Coordinates(20, 200), input, "input", null, arrayProp);
		
		lang.nextStep();
		
		lang.newText(new Coordinates(20, 250), "Codiertes Chiffrat", "outputheader", null, textProp2);
		
		String[] output = initialiseArray(eingabeArray.getLength());
		
		StringArray ausgabeArray = lang.newStringArray(new Coordinates(20, 300), output, "output", null, arrayProp);
		
		
		ArrayList<String> alphabet = new ArrayList<String>();
		alphabet.add("A");
		alphabet.add("B");
		alphabet.add("C");
		alphabet.add("D");
		alphabet.add("E");
		alphabet.add("F");
		alphabet.add("G");
		alphabet.add("H");
		alphabet.add("I");
		alphabet.add("J");
		alphabet.add("K");
		alphabet.add("L");
		alphabet.add("M");
		alphabet.add("N");
		alphabet.add("O");
		alphabet.add("P");
		alphabet.add("Q");
		alphabet.add("R");
		alphabet.add("S");
		alphabet.add("T");
		alphabet.add("U");
		alphabet.add("V");
		alphabet.add("W");
		alphabet.add("X");
		alphabet.add("Y");
		alphabet.add("Z");
		
		
		Hashtable<String, Integer> alphabetpos = new Hashtable<String, Integer>();
		
		alphabetpos.put("A", new Integer(0));
		alphabetpos.put("B", new Integer(1));
		alphabetpos.put("C", new Integer(2));
		alphabetpos.put("D", new Integer(3));
		alphabetpos.put("E", new Integer(4));
		alphabetpos.put("F", new Integer(5));
		alphabetpos.put("G", new Integer(6));
		alphabetpos.put("H", new Integer(7));
		alphabetpos.put("I", new Integer(8));
		alphabetpos.put("J", new Integer(9));
		alphabetpos.put("K", new Integer(10));
		alphabetpos.put("L", new Integer(11));
		alphabetpos.put("M", new Integer(12));
		alphabetpos.put("N", new Integer(13));
		alphabetpos.put("O", new Integer(14));
		alphabetpos.put("P", new Integer(15));
		alphabetpos.put("Q", new Integer(16));
		alphabetpos.put("R", new Integer(17));
		alphabetpos.put("S", new Integer(18));
		alphabetpos.put("T", new Integer(19));
		alphabetpos.put("U", new Integer(20));
		alphabetpos.put("V", new Integer(21));
		alphabetpos.put("W", new Integer(22));
		alphabetpos.put("X", new Integer(23));
		alphabetpos.put("Y", new Integer(24));
		alphabetpos.put("Z", new Integer(25));
		
		String[][] data = new String[26][1];
		String[][] shiftdata = new String[26][1];
		
		// Generating original alphabet
		for(int i=0; i<26; i++) {
				data[i][0] = alphabet.get(i);
		}
		
		// Generating shifted alphabet
		
		for(int i=0; i<26; i++) {
				shiftdata[i][0] = alphabet.get((i+shiftoffset) % 26); 
		}
		
		StringMatrix strmatrix1 = lang.newStringMatrix(new Offset(400, -120, eingabeArray, AnimalScript.DIRECTION_NE), data, "tab1", null, mxp);
		
		StringMatrix strmatrix2 = lang.newStringMatrix(new Offset(600, -120, eingabeArray, AnimalScript.DIRECTION_NE), shiftdata, "tab2", null, mxp);
		
		PolylineProperties arcp = new PolylineProperties();
		arcp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		
		Node[] linepoints = new Node[2];
		linepoints[0] = new Offset(20, 150, strmatrix1, AnimalScript.DIRECTION_NE);
		linepoints[1] = new Offset(120, 150, strmatrix1, AnimalScript.DIRECTION_NE);
		
		lang.newPolyline(linepoints, "line", null, arcp);
		
		
		
		lang.nextStep();
		
		// Encrypting input
		
		exec("forbegin");
		
		lang.nextStep();
		
		for(int i=0; i<eingabeArray.getLength(); i++) {
			
			eingabeArray.highlightCell(i, null, new MsTiming(300));
			exec("var_actualposition");
			
			lang.nextStep();
			
			String eingabeArray_entry = eingabeArray.getData(i);
			
			int actualEingabePos;
			
			if(alphabet.contains(eingabeArray_entry))
			{
				// Nur vorhandenes Alphabet wird bearbeitet, Zeichen wird sonst übersprungen
				actualEingabePos = alphabetpos.get(eingabeArray_entry);
			}
			else
			{
				ausgabeArray.put(i, eingabeArray_entry, null, new MsTiming(300));
				lang.nextStep();
				continue;
			}
			
			exec("var_ausgabearray");
			
			ausgabeArray.put(i, alphabet.get((actualEingabePos+shiftoffset+26)%26), null, new MsTiming(300));
			
			
			strmatrix1.highlightCell(actualEingabePos, 0, null, new MsTiming(300));
			strmatrix2.highlightCell(actualEingabePos, 0, null, new MsTiming(300));
			
			lang.nextStep();
			
			strmatrix1.unhighlightCell(actualEingabePos, 0, null, null);
			strmatrix2.unhighlightCell(actualEingabePos, 0, null, null);
			
			exec("forend");
			
			lang.nextStep();
			
		}
		
	}
	
	public String[] initialiseArray(int length) {
		String[] array = new String[length];
		
		for(int i=0; i<length; i++) {
			array[i] = "     ";
		}
		
		return array;
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		init();
		String[] arrayData = (String[]) primitives.get("inputArray");
		int shift = (Integer) primitives.get("shift");
		
		ArrayProperties arrayProp = (ArrayProperties) props.getPropertiesByName("arrayprop");
		
		MatrixProperties mxp = (MatrixProperties) props.getPropertiesByName("matrixprop");
		
		run(arrayData, shift, arrayProp, mxp);
		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Caesar Cipher";
	}

	@Override
	public String getAnimationAuthor() {
		return "Stephan Arneth";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return "Illustration of Caesar Encrypting with Translation Matrix. " +
				"In this demonstration the entered text will be encrypted. " +
				"While the encryption is done step by step the Translation is illustrated " + 
				"by two matrices. The Caesar-Encryption principle is very simple, each character " + 
				"is shifted via a predefined step.";
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
		return "Caesar-Encryption";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		super.init();
		
		SourceCodeProperties sourceProps = new SourceCodeProperties();
		sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
		sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 13));
		sourceCode = lang.newSourceCode(new Coordinates(20, 400), "Source", null, sourceProps);
		
		parse();
		
	}
	
	@Override
	public String getAnnotatedSrc() {
		return "for (int i=0; i &lt; eingabeArray.length(); i++) { @label(\"forbegin\") \n" +
		    "  int actualCharPosition = alphabetPosition.get(eingabeArray[i]); @label(\"var_actualposition\") \n" +
		    "  ausgabearray[i] = (actualCharPosition + shift + 26) % 26; @label(\"var_ausgabearray\") \n" +
		    "} @label(\"forend\")";
	}
	
}
