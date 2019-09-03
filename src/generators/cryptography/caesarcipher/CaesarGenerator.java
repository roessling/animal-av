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
import algoanim.primitives.IntArray;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class CaesarGenerator extends AnnotatedAlgorithm implements generators.framework.Generator {
	public Language lang;
  // private SourceCode sc;
	private ArrayProperties arrayProps;
	private TextProperties textProps;
	private ArrayMarkerProperties arrayMarkerProps;
	private SourceCodeProperties sourceProps;
	
	private String read = "Lesezugriff";
	private String write = "Schreibzugriff";
	
	public CaesarGenerator() {
		init();		
	}
	
	@Override
	public String getAnnotatedSrc() {
		String code =
			"Das ChiffreAlphabet erzeugen (Charwert des normalen Alphabets + Verschiebung rotieren) @label(\"line0\") @inc(\""+read+"\") @inc(\""+write+"\") \n"+
			"Buchstabe aus dem Klartext auslesen 													@label(\"line1\") @inc(\""+read+"\")\n"+
			"Dazugehoerigen Chiffrebuchstaben mittels des Chiffrealphabets suchen					@label(\"line2\") @inc(\""+read+"\")\n"+	
			"Im Chiffretext den Klarbuchstaben durch Chiffrebuchstaben ersetzen						@label(\"line3\") @inc(\""+write+"\") \n"+
			"Naechster Buchstabe																	@label(\"line4\") \n"+
			"Fertig																					@label(\"line5\") \n";
		
		return code;
	}
	
	public void startchiffre(String text, int verschiebung	) {
		lang.newText(new Coordinates(20,20), "Caesar Chiffre mit Verschiebung "+verschiebung, "headline",null, textProps);

//	    Timing changeTime = new TicksTiming(20);

	    //Klartext:
	    lang.newText(new Coordinates(20,145), "Klartext", "klartexttitle",null, textProps);	    
	    IntArray clearvalues = lang.newIntArray(new Coordinates(20,90), stringToIntarray(text.toLowerCase()), "clearvalues", null, arrayProps);
	    String[] pff = stringToStringarray(text.toLowerCase()); 
		StringArray cleartext = lang.newStringArray(new Coordinates(20,120), pff, "cleartext", null, arrayProps);
		
		
		ArrayMarker clearMarker = lang.newArrayMarker(clearvalues, 0, "a",  null, arrayMarkerProps);
		
		// Chiffrierter text
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		lang.newText(new Coordinates(20,245), "Chiffrierter Text", "chiffretitle",null, textProps);
		IntArray chiffrevalues = lang.newIntArray(new Coordinates(20,190), stringToIntarray(text.toLowerCase()), "chiffrevalues", null, arrayProps);
		StringArray chiffretext = lang.newStringArray(new Coordinates(20,220), pff, "chiffretext", null, arrayProps);
		
		arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ArrayMarker chiffreMarker = lang.newArrayMarker(chiffrevalues, 0, "a",  null, arrayMarkerProps);
		
		//Alphabete
		int[] alphaint = stringToIntarray("abcdefghijklmnopqrstuvwxyz");
		lang.newText(new Coordinates(20,280), "Alphabet unverschluesselt (als Ascii Werte)", "alphatitle",null, textProps);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		IntArray alphabet = lang.newIntArray(new Coordinates(20, 300), alphaint, "alphabet", null, arrayProps);
		
		
		
		
		/* Sourcecode erzeugen */

	    // Sourcecode erstellen
		sourceCode = lang.newSourceCode(new Coordinates(20, 450), "code", null, sourceProps);
		/*sc.addCodeLine("Das ChiffreAlphabet erzeugen (Charwert des normalen Alphabets + Verschiebung rotieren)", null, 0, null);
		sc.addCodeLine("Buchstabe aus dem Klartext auslesen", "line1", 0, null);
		sc.addCodeLine("Dazugehoerigen Chiffrebuchstaben mittels des Chiffrealphabets suchen", "line2", 4, null);
		sc.addCodeLine("Im Chiffretext den Klarbuchstaben durch Chiffrebuchstaben ersetzen", "line3", 4, null);
		sc.addCodeLine("Naechster Buchstabe", "line4", 0, null);
		sc.addCodeLine("Fertig", null, 0, null);
		*/
		
		// setup complexity
		vars.declare("int", write); vars.setGlobal(write);
		vars.declare("int", read); vars.setGlobal(read);
		
		Text complex = lang.newText(new Coordinates(300, 20), "...", "complexity", null);
		TextUpdater tu = new TextUpdater(complex);
		tu.addToken("Lesezugriffe: ");
		tu.addToken(vars.getVariable(read));
		tu.addToken(" - Schreibzugriffe: ");
		tu.addToken(vars.getVariable(write));
		tu.update();
		
		// parsing anwerfen
		parse();
		
		
		int basevalue = (int) 'a';
		int[] chiffreint = new int[alphaint.length];
		//Alphabet chiffrieren:
		for(int i=0; i<alphaint.length; i++) {
			int val = alphaint[i];
			int newcharval = ((val-basevalue)+verschiebung)%alphaint.length + basevalue;
			exec("line0");
			chiffreint[i] = newcharval;
		}
		
		lang.nextStep();
		
		lang.newText(new Coordinates(20,340), "Zielalphabet (als Ascii Werte)", "chiffretitle",null);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		IntArray alphachiff = lang.newIntArray(new Coordinates(20, 360), chiffreint, "chiffre", null, arrayProps);
		sourceCode.highlight(0);
		
		//Fies coole animation: (dauert leider zu lange :/ )
//		
//		int incStep = 20;
//		for(int i=0; i<alphachiff.getLength(); i++) {
//			Timing inctime = new TicksTiming(incStep);
//			alphachiff.put(i, chiffreint[i], null, null);
//			alphachiff.highlightCell(i, null, null);
//			lang.nextStep();
//			alphachiff.unhighlightCell(i, null, null);
//		}
		lang.nextStep();
		sourceCode.unhighlight(0);
		
		caesar(cleartext, chiffretext, clearvalues, chiffrevalues, alphabet, alphachiff, clearMarker, chiffreMarker);
	}
	
	public void caesar(StringArray cleartext, StringArray chiffretext, IntArray clearvalues, IntArray chiffrevalues, 
						IntArray alphabet, IntArray alphachiff, ArrayMarker clearMarker, ArrayMarker chiffreMarker) {
		int baseval = (int) 'a';
		int c = 0;
		int newval = 0;
		 Timing changeTime = new TicksTiming(20);
		 Timing preOne = new TicksTiming(50);
//		 Timing preTwo = new TicksTiming(100);
		 Timing preThree = new TicksTiming(200);
		 Timing preFour = new TicksTiming(250);
		 Timing preFive = new TicksTiming(350);
		
		int length = clearvalues.getLength();
		for(int i=0; i<length; i++) {

			sourceCode.highlight("line1", true, null, null);
			sourceCode.unhighlight("line1", true, preOne, changeTime);
			exec("line1");
			clearvalues.highlightCell(i, null, null);
			cleartext.highlightCell(i, null, changeTime);
			
			sourceCode.highlight("line3", true, preThree, null);
			sourceCode.unhighlight("line3", true, preFive, changeTime);
			exec("line3");
			chiffrevalues.highlightCell(i, preThree, null);
			chiffretext.highlightCell(i, preThree, changeTime);
			clearMarker.move(i, null, changeTime);
			chiffreMarker.move(i, null, changeTime);
			c = clearvalues.getData(i);
			newval = alphachiff.getData(c - baseval);
			
			sourceCode.highlight("line2", true, preOne, null);
			sourceCode.unhighlight("line2", true, preThree, changeTime);
			exec("line2");
			alphabet.highlightCell(c-baseval, preOne, changeTime);
			alphachiff.highlightCell(c-baseval, preThree, changeTime);
			
			char newchar = (char) newval;
			
			chiffrevalues.put(i, newval, preFour, changeTime);
			chiffretext.put(i, pumpUpChar(newchar), preFour, changeTime);
			
			
			
			lang.nextStep();
			//sourceCode.highlight("line4", true, null, null);
			//sourceCode.unhighlight("line4", true, preThree, changeTime);
			
			clearvalues.unhighlightCell(i, null, null);
			cleartext.unhighlightCell(i, null, null);
			chiffrevalues.unhighlightCell(i, null, null);
			chiffretext.unhighlightCell(i, null, null);
			alphabet.unhighlightCell(c-baseval, null, null);
			alphachiff.unhighlightCell(c-baseval, null, null);
			
			lang.nextStep();	//schritt ist LEIDER notwendig sonst wird das array nicht richtig dargestellt
			
		}
		sourceCode.highlight(5);
		exec("line5");
	}
	/* Converts a string to an array of integers with the char-values of the string */
	private int[] stringToIntarray(String a) {
		int[] result = new int[a.length()];
		for(int i=0; i<a.length(); i++) {
			result[i] = (int) a.charAt(i);
		}
		return result;
	}
	private String[] stringToStringarray(String a) {
		String[] result = new String[a.length()];
		for(int i=0; i<a.length(); i++) {
			result[i] = pumpUpChar(a.charAt(i));
		}
		return result;
	}
	
	private String pumpUpChar(char a) {
		return "   "+a+"   ";
	}

	/*
	 * 				################################################## GENERATE ######################
	 * (non-Javadoc)
	 * @see generator.Generator#generate(generator.properties.AnimationPropertiesContainer, java.util.Hashtable)
	 */
	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		String text = "geheim";
		int verschiebung = 14;
		
		//Einlesen der übergebenen Werte - könnte Fehlerhaft sein?!
		if(arg1.containsKey("ArrayProperties")) this.arrayProps = (ArrayProperties) arg1.get("ArrayProperties");
		if(arg1.containsKey("ArrayMarkerProperties")) this.arrayMarkerProps = (ArrayMarkerProperties) arg1.get("ArrayMarkerProperties");
		if(arg1.containsKey("SourceCodeProperties")) this.sourceProps = (SourceCodeProperties) arg1.get("SourceCodeProperties");
		if(arg1.containsKey("TextProperties")) this.textProps = (TextProperties) arg1.get("TextProperties");		
		if(arg1.containsKey("verschiebung")) verschiebung = Integer.parseInt((String) arg1.get("verschiebung"));
		if(arg1.containsKey("klartext")) text = (String) arg1.get("klartext");
		
		this.startchiffre(text, verschiebung);
		return(lang.toString());

	}

	@Override
	public String getAlgorithmName() {
    return "Caesar-Verschl\u00fcsselung";
	}

	@Override
	public String getAnimationAuthor() {
		return "Sebastian Kauschke, Manuel Pistner";
	}

	@Override
	public String getCodeExample() {
		return "Das ChiffreAlphabet erzeugen (Charwert des normalen Alphabets + Verschiebung rotieren)"+
		"Buchstabe aus dem Klartext auslesen"+
		"\tDazugehoerigen Chiffrebuchstaben mittels des Chiffrealphabets suchen"+
		"\tIm Chiffretext den Klarbuchstaben durch Chiffrebuchstaben ersetzen"+
		"Naechster Buchstabe"+
		"Fertig";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Demonstriert die Caesar Chiffre anhand eines Eingabestrings, zugehöriger Ascii Werte, des Klartextalphabets"+
				" und des Chiffrealphabets.";
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
		return "Caesar Chiffre Generator";
	}

	@Override
	public String getOutputLanguage() {
		return PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
		super.init();
		lang = new AnimalScript("Animation", "Sebastian Kauschke, Manuel Pistner", 800, 500);
		lang.setStepMode(true);
		
		arrayProps = new ArrayProperties();
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
	        Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
	        Color.RED);
	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
	        Color.YELLOW);
	    
	    arrayMarkerProps = new ArrayMarkerProperties();
		arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		textProps = new TextProperties();
		textProps.set("color", Color.BLACK);
		
		sourceProps = new SourceCodeProperties();
		sourceProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
	        Font.PLAIN, 12));

		sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
	        Color.RED);   
		sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}


}