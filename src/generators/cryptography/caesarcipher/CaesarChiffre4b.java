package generators.cryptography.caesarcipher;


import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

/**
 * @author Gamze Canova and Stephan Moczygemba
 * @version 1.0 2007-05-30
 *
 */
public class CaesarChiffre4b extends AnnotatedAlgorithm implements Generator{
	
//	private String assignments = "Assignments";
//	private String compares = "Compares";
	
	/* IDs */
	private static final String ARRAY_ALPHABET_ID = "alphabetText";
	private static final String ARRAY_CIPHER_ALPH_ID = "alphabetShifted";
	private static final String ARRAY_PLAINTEXT_ID = "plaintext";
	private static final String HEADER_RECT_ID = "hRect";
	private static final String HEADER_TITLE_ID = "header";
//	private static final String DESCRIPTION_ID = "";
	private static final String MARKER_ID = "iMarker";
//	private static final String SOURCECODE_ID = "sourceCode";
	
	/* Properties */
	static ArrayProperties arrayAlphabetProps;
	static ArrayProperties arrayChiffreAlphProps;
	static ArrayProperties arrayPlaintextProps;
	static RectProperties headerRectProps;
	static TextProperties headerTitleProps;
	TextProperties descriptionProps;
	static ArrayMarkerProperties markerProps;
	static SourceCodeProperties scProps;
	
	public CaesarChiffre4b(){
		
	}

	@Override
	public String getAnimationAuthor() {
		return "Gamze Canova, Stephan Moczygemba";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
	
	
	@Override
	public String getAnnotatedSrc() {

		return 		
		"CaesarCipher(A)									@label(\"header\") @declare(\"int\", \"i\") @declare(\"int\", \"lengthA\") @declare(\"String\", \"c\") @declare(\"int\", \"asciiC\") @declare(\"String\", \"encode\") @declare(\"int\", \"asciiEncode\")\n" + 
		"  for i := 1 to length[A]							@label(\"for\") \n" + 								
		"    c := A[i]										@label(\"Ai\") \n" +
		"    if ascii(c) >= 65 and ascii(c) <= 90			@label(\"if1\") \n" + 
		"      if c + shift > 90							@label(\"if2\") \n" + 
		"        then encode := ascii(c) + shift + 65 - 91	@label(\"then\") \n" + 
		"      else											@label(\"else\")\n" + 
		"        encode := ascii(c) + shift					@label(\"else_do1\") \n" + 
		"      A[i] := encode								@label(\"else_do2\")"; 
				
	}
	
	@Override
	public void init() {
		super.init();
		
		 scProps = new SourceCodeProperties();
		 scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		 scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);  
		   
		// instantiate source code primitive to work with
		sourceCode = lang.newSourceCode(new Coordinates(20, 350), "sumupCode",
					null, scProps);

//		int shift = 3;
		//CaesarChiffre4 cc = new CaesarChiffre4(l, shift);		
//		String[] text = {"T", "h", "i", "s", " ", "i", "s", " ","a", " ", "s", "a", "m", "p", "l", "e", " ", "t", "e", "x", "t"};
		
		/* set properties for main excecution */
		Color darkGreen = new Color(0,51,51);
		Color lightGreen = new Color(204,255,204);

		//set header
		headerTitleProps = new TextProperties();
		headerTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 1, 24));			
		headerRectProps = new RectProperties();
		headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
		//headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		
		arrayAlphabetProps = new ArrayProperties();
		arrayAlphabetProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, darkGreen);
		arrayAlphabetProps.set(AnimationPropertiesKeys.FILL_PROPERTY, lightGreen);
		//arrayAlphabetProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayAlphabetProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, darkGreen);
		arrayAlphabetProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(255,102,0));
		arrayAlphabetProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
		
		arrayChiffreAlphProps = arrayAlphabetProps;
		
		arrayPlaintextProps = new ArrayProperties();
		arrayPlaintextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayPlaintextProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		//arrayPlaintextProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
		arrayPlaintextProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,Color.BLACK);
		arrayPlaintextProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,Color.RED);
		arrayPlaintextProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.YELLOW);

	    markerProps = new ArrayMarkerProperties();
	    markerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");   
	    markerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
	    
	    
	
		/*
		Text txt = lang.newText(new Coordinates(300, 20), "...", "complexity", null);
		TextUpdater tu = new TextUpdater(txt);
		tu.addToken("Compares: ");
		tu.addToken(vars.getVariable(compares));
		tu.addToken(" - Assignments: ");
		tu.addToken(vars.getVariable(assignments));
		tu.update();
		*/
	    
		// parsing anwerfen
		parse();
	}
	
	
	
	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		init();

		headerTitleProps = new TextProperties();
		Object tempProperty = (Object)props.getPropertiesByName("title");
		/* just to get a large and bold title for whatever font is assigned */
		if(tempProperty.equals("Serif")){
			headerTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 24));	
		}else if(tempProperty.equals("Monospaced")){
			headerTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24));	
		}else{
			headerTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));	
		}
		headerTitleProps = (TextProperties)props.getPropertiesByName("title");
		
		headerRectProps = new RectProperties();
		headerRectProps = (RectProperties)props.getPropertiesByName("headerRect");
		//headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		
		
		arrayAlphabetProps = new ArrayProperties();
		arrayAlphabetProps = (ArrayProperties)props.getPropertiesByName("alphabet");
		//arrayAlphabetProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	
		
		arrayChiffreAlphProps = new ArrayProperties();
		arrayChiffreAlphProps = (ArrayProperties)props.getPropertiesByName("chiffreAlphabet");
		//arrayChiffreAlphProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		
		
		String plaintext = (primitives.get("plaintext")).toString();
		arrayPlaintextProps = new ArrayProperties();
		arrayPlaintextProps = (ArrayProperties)props.getPropertiesByName("plaintext");
		//arrayPlaintextProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	   
		// now, create the IntArray object, linked to the properties
	    String textCheat = plaintext.substring(1);		
	    String[] stringArray = textCheat.split(""); 	/* he splits String into an array of Strings (single char per entry) */
	    stringArray[0] = plaintext.substring(0, 1);		/* needed, because otherwise there is a blank at the beginning -> just set first character manually */
		
	    int shift = ((Integer)primitives.get("shift")).intValue();
	    
	    
	    markerProps = new ArrayMarkerProperties();
	    markerProps = (ArrayMarkerProperties)props.getPropertiesByName("marker");
 
	    
	    scProps = new SourceCodeProperties();
	    scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
	    
	    //Diese Properties werden aus welchen Gründen auch immer nicht übernommen
	    //scProps.set(AnimationPropertiesKeys.SIZE_PROPERTY, props.get("sourceCode", AnimationPropertiesKeys.SIZE_PROPERTY));
	    //scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, props.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
	    //scProps.set(AnimationPropertiesKeys.BOLD_PROPERTY, props.get("sourceCode", AnimationPropertiesKeys.BOLD_PROPERTY));
	    //scProps.set(AnimationPropertiesKeys.ITALIC_PROPERTY, props.get("sourceCode", AnimationPropertiesKeys.ITALIC_PROPERTY));
	    //scProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, props.get("sourceCode", AnimationPropertiesKeys.HIDDEN_PROPERTY));
		encode(stringArray, shift);
		//System.out.println(lang.toString());
		return lang.toString();
	}
	
	
	
	
	private void encode(String[] text, int shift) {
				
//		Color darkGreen = new Color(0,51,51);
//		Color lightGreen = new Color(204,255,204);

		//set header
//		TextProperties textProps = new TextProperties();
		
		
		lang.newText(new Coordinates(50, 60), "Caesar Cipher with shift=" + shift, HEADER_TITLE_ID, null, headerTitleProps);
//		RectProperties rectProperties = new RectProperties();
//		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
//		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, HEADER_TITLE_ID, AnimalScript.DIRECTION_NW), new Offset(5, 5, HEADER_TITLE_ID, AnimalScript.DIRECTION_SE), HEADER_RECT_ID, null, headerRectProps);		

		
//		ArrayProperties arrayAlphabetProps = new ArrayProperties();
//		arrayAlphabetProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, darkGreen);
//		arrayAlphabetProps.set(AnimationPropertiesKeys.FILL_PROPERTY, lightGreen);
//		arrayAlphabetProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
//		arrayAlphabetProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, darkGreen);
//		arrayAlphabetProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(255,102,0));
//		arrayAlphabetProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
		
		
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));	
		lang.newText(new Offset(0, 30, "header", AnimalScript.DIRECTION_SW), "Alphabet:", ARRAY_ALPHABET_ID, null, textProps);
		
		
		String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String[] alphabetShifted = new String[26];
		StringArray alphabetArray = lang.newStringArray(new Offset(40,0,ARRAY_ALPHABET_ID, AnimalScript.DIRECTION_NE),alphabet, "alphabet", null, arrayAlphabetProps);
		
		//Generate ChiffreArray according to shift
		char ch;
		for(int i=0; i < alphabet.length; i++){
			if((int) alphabet[i].charAt(0) + shift > 90 ){
				ch = (char)(alphabet[i].charAt(0) + shift - 91 + 65);
			}else{
				ch = (char)(65 + shift + i);
			}
			alphabetShifted[i] = Character.toString(ch);
		}
		
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));	
		lang.newText(new Offset(0, 80, "header", AnimalScript.DIRECTION_SW), "Chiffrealph.:", "chiffre", null, textProps);
		
		StringArray shiftedAlphabetArray = lang.newStringArray(new Offset(20, 0,"chiffre", AnimalScript.DIRECTION_NE),alphabetShifted, ARRAY_CIPHER_ALPH_ID, null, arrayChiffreAlphProps);
		
//		ArrayProperties arrayProps = new ArrayProperties();
//	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
//	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
//	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
//	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,Color.BLACK);
//	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,Color.RED);
//	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.YELLOW);
    
	    // now, create the IntArray object, linked to the properties
//	    String textCheat = text.substring(1);		
//	    String[] stringArray = textCheat.split(""); /* he splits String into an array of Strings (single char per entry) */
//	    stringArray[0] = text.substring(0, 1);		/* needed, because otherwise there is a blank at the beginning -> just set first character manually */
	    
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));	
		Text plaintext = lang.newText(new Coordinates(50, 280), "Plaintext:", ARRAY_PLAINTEXT_ID, null, textProps);

	    
	    StringArray sa = lang.newStringArray(new Coordinates(200, 280), text, "caesarArray", 
				null, arrayPlaintextProps);
		
		// start a new step after the array was created
		//lang.nextStep();		
		
		// Create SourceCode: coordinates, name, display options, 
		// default properties

	   
	    /*
		SourceCode sc = lang.newSourceCode(new Offset(0, 80, ARRAY_PLAINTEXT_ID, AnimalScript.DIRECTION_SW), SOURCECODE_ID,
		        null, scProps);
		
		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("CaesarCipher(A)", null, 0, null);  							// 0
		sc.addCodeLine("for i := 1 to length[A]", null, 1, null); 					// 1
		sc.addCodeLine("c := A[i]", null, 2, null); 								// 2
		sc.addCodeLine("if ascii(c) >= 65 and ascii(c) <= 90 then", null, 2, null); // 3
		sc.addCodeLine("if c + shift > 90", null, 3, null);							// 4
		sc.addCodeLine("then encode := ascii(c) + shift + 65 - 91", null, 4, null);	// 5
		sc.addCodeLine("else", null, 3, null);										// 6
		sc.addCodeLine("encode := ascii(c) + shift", null, 4, null);				// 7
		sc.addCodeLine("A[i] := encode", null, 3, null);							// 8		
		
		*/
		
	    exec("header");
		lang.nextStep();
		
		char encode = 'a';
//		String encodedText = "";
		
		// Create two markers to point on i and j
		// Array, current index, name, display options, properties
//	    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
//	    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");   
//	    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		ArrayMarker iMarker = lang.newArrayMarker(sa, 0, MARKER_ID, 
        null, markerProps);		
		
		MsTiming tShort = new MsTiming(100);
		MsTiming tLong = new MsTiming(200);
		
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));	
		Text shiftText = lang.newText(new Offset(0, 30, "caesarArray", AnimalScript.DIRECTION_SW), "Shift Character", "shift", null, textProps);
		Text shiftTextOld = lang.newText(new Offset(10, 0, "shift", AnimalScript.DIRECTION_NE), "A", "shiftTextOld", null, textProps);
		Text shiftTextTo = lang.newText(new Offset(10, 0, "shiftTextOld", AnimalScript.DIRECTION_NE), "to", "shiftTextTo", null, textProps);	
		Text ignoreText = lang.newText(new Offset(0, 30, "caesarArray", AnimalScript.DIRECTION_SW), "Ignore Special Character", "ignoreText", null, textProps);	
		
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));	
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		Text shiftTextNew = lang.newText(new Offset(10, 0, "shiftTextTo", AnimalScript.DIRECTION_NE), "A", "shiftTextNew", null, textProps);

		char c;
		int arrayPos = 0;
		

		exec("for");
		vars.set("lengthA", String.valueOf(text.length));
		iMarker.show();
		
		for (int i = 0; i<text.length; i++) {
	    	iMarker.move(i, null, tShort);
	    	//Annotation i anpassen
	    	vars.set("i", String.valueOf(i+1));
	
			shiftText.hide();
			shiftTextNew.hide();
			shiftTextOld.hide();
			shiftTextTo.hide();
			ignoreText.hide();
			
			lang.nextStep();
			
			exec("Ai");
			c = (text[i].toUpperCase()).charAt(0);
			vars.set("c", String.valueOf(c));
			sa.highlightCell(i, null, tShort);
	    	
	    	lang.nextStep(); 	/* next step needed to refresh i */
   	
			exec("if1");
			
			
			vars.set("asciiC", String.valueOf((int) c));
					
			lang.nextStep();
			/* ascii: 65-90 A-Z; 97-122 a-z; 32: Space */
	    	if((int)c >= 65 && (int)c <= 90){

	    		exec("if2");
	    		lang.nextStep();
				if((int)c + shift > 90){

					exec("then");
					encode = (char)((int)c + shift - 91 + 65);
					arrayPos = c - 65;
					vars.set("asciiEncode", String.valueOf((int)encode));
					vars.set("encode", String.valueOf(encode));

				}else{
					exec("else");
					lang.nextStep();
					exec("else_do1");
					encode = (char)((int)c + shift);
					vars.set("asciiEncode", String.valueOf((int)encode));
					vars.set("encode", String.valueOf(encode));
					
					arrayPos = c - 65;
				}
				alphabetArray.highlightCell(arrayPos, null, tLong);
				shiftedAlphabetArray.highlightCell(arrayPos,null,tLong);
				
				alphabetArray.highlightElem(arrayPos, null, tLong);
				shiftedAlphabetArray.highlightElem(arrayPos,null,tLong);

				shiftTextOld.setText(Character.toString(c), null, null);
				shiftTextNew.setText(Character.toString(encode), null, null);
		    	shiftText.show();
				shiftTextNew.show();
				shiftTextOld.show();
				shiftTextTo.show();	 				
				
				lang.nextStep();
				
				//sc.toggleHighlight(5, 8);
				//sc.toggleHighlight(7, 8);
				exec("else_do2");
				sa.put(i, Character.toString(encode), null, tShort);
				sa.highlightElem(i, null, tShort);
				lang.nextStep();
				exec("for");
			}else{
				shiftTextNew.hide();
				shiftTextTo.hide();
				shiftTextOld.hide();
				shiftText.hide();
				ignoreText.show();	
				//sc.toggleHighlight(3, 1);
				exec("for");
			}	    	
			lang.nextStep();
			
			alphabetArray.unhighlightCell(arrayPos, null, null);
			shiftedAlphabetArray.unhighlightCell(arrayPos, null, null);
			
			alphabetArray.unhighlightElem(arrayPos, null, null);
			shiftedAlphabetArray.unhighlightElem(arrayPos, null, null);
			
			//sa.unhighlightCell(i, null, null);
			sa.unhighlightElem(i, null, null);	
		}
		
		plaintext.setText("Chiffre:", null, tShort);
		iMarker.moveOutside(null, tShort);
		lang.nextStep();
		//sc.toggleHighlight(1, 0);
		exec("header");
	}
	


	
	@Override
	public String getAlgorithmName() {
		return "Caesar Cipher";
	}

	/*
	
	@Override
	public String getCodeExample() {
		return 		"CaesarCipher(A)\n" + 
					"  for i := 1 to length[A]\n" + 
	    			"    c := A[i]\n" +
	    			"    if ascii(c) &gt= 65 and ascii(c) &lt= 90\n" + 
	    			"      if c + shift &gt 90\n" + 
	    			"        then encode := ascii(c) + shift + 65 - 91\n" + 
	    			"      else\n" + 
	    			"        encode := ascii(c) + shift\n" + 
	    			"      A[i] := encode";
	}
	 */
	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return "Animates the Caesar Chiffre of a given plaintext. Shifts every letter with the given shift. It ignores special characters and digits! Displays also the alphabet with the related chiffre alphabet.";
	}

	/*
	@Override
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}
	 */
	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	@Override
	public String getName() {
		return "Caesar Cipher";
	}



	


}
