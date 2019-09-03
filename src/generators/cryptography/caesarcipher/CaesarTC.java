package generators.cryptography.caesarcipher;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;



public class CaesarTC extends AnnotatedAlgorithm implements generators.framework.Generator {

	private StringArray wa;
	private StringArray ra;
	private StringArray alphabet;
	private SourceCode lblKey;
	private SourceCode sc;
	
	//Klartextalphabet
	private String[] alph = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", 
							"q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	
	private static Language lang;
	private static String algoName = "Caesar Chiffre";
//	private static String author = "Can Gueler & Tayfun Atik";
	private static int[] resolution = new int[]{640,480};
	
	// Annotations
	private String assi = "Assignments";
	private String comp = "Compares";
	
	//Input Word to encrypt and the key
	private static String word = null;
	private static String key = null;
	
	//Font Style and Color
	private Font font = null;
	private Color fontColor = null;
	
	//Element Highlight Color
	private Color waEH = null;
	private Color raEH = null;
	private Color alphEH = null;

	//Element Color
	private Color waEC = null;
	private Color raEC = null;
	private Color alphEC = null;

	//Color Highlight
	private Color waCH = null;
	private Color raCH = null;
	private Color alphCH = null;

	//Marker Color
	private Color waMC = null;
	private Color raMC = null;
	private Color alphMRC = null;
	private Color alphMLC = null;

	
	public CaesarTC (){
		
	}

	/**
	 * Initialisiere Klartextalphabet und die zu verwendeten Arrays
	 * 
	 * @param a
	 * @param r
	 */
	public void init(String[] a, String[] r) {

			//Array mit dem gesamten Alphabet
		ArrayProperties arrayProps3 = new ArrayProperties();
	    arrayProps3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps3.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    arrayProps3.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    arrayProps3.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, alphEC);
	    arrayProps3.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, alphEH);
	    arrayProps3.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, alphCH);
		
			//Array mit Klartext-Buchstaben
		ArrayProperties arrayProps = new ArrayProperties();
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, waEC);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, waEH);
	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, waCH);

	    	//Array mit kodierten Werten, am Anfang unsichtbar
	    ArrayProperties arrayProps2 = new ArrayProperties();
	    arrayProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);
	    arrayProps2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
	    arrayProps2.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    arrayProps2.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, raEC);
	    arrayProps2.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, raEH);
	    arrayProps2.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, raCH);
	    
	    // StringArray mit den entsprechenden Properties
		wa = lang.newStringArray(new Coordinates(200, 200), a, "wArray", null, arrayProps);
		ra = lang.newStringArray(new Coordinates(200, 300), r, "rArray",null, arrayProps2);
		alphabet = lang.newStringArray(new Coordinates(200, 400), alph, "Alphabet",null, arrayProps3);
		
		/* Create
		 * the Labels
		 */
			
		// Die Ueberschrift
		SourceCodeProperties titleProps = new SourceCodeProperties();
		titleProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getFontName(), Font.PLAIN, 18));
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		
		
		SourceCode title = lang.newSourceCode(new Coordinates(60, 30), "sourceCode", null, titleProps);
		title.addCodeLine("Caesar Chiffre", null, 0, null);
		title.show();
			
		// Die Key-Angabe
		SourceCodeProperties keyProps = new SourceCodeProperties();
		keyProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		keyProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getFontName(), Font.PLAIN, 14));
		keyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		    
		lblKey = lang.newSourceCode(new Coordinates(40, 60), "sourceCode", null, keyProps);
		lblKey.addCodeLine("", null, 0, null);
		lblKey.hide();
			
		// Klartext-Array-Ueberschrift
		SourceCodeProperties wordProps = new SourceCodeProperties();
		wordProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		wordProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getFontName(),Font.PLAIN, 14));
		wordProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		    
		SourceCode word = lang.newSourceCode(new Coordinates(40, 197), "sourceCode", null, wordProps);
		word.addCodeLine("Plain Text", null, 0, null);
		word.show();
			
		// Verschluesseltes-Array-Ueberschrift
		SourceCodeProperties resultProps = new SourceCodeProperties();
		resultProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    resultProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getFontName(),Font.PLAIN, 14));
		resultProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		    
		SourceCode result = lang.newSourceCode(new Coordinates(40, 297), "sourceCode", null, wordProps);
		result.addCodeLine("Encrypted Text", null, 0, null);
		result.show();

		// Alphabet-Array-Ueberschrift
		SourceCodeProperties alphProps = new SourceCodeProperties();
		alphProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		alphProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getFontName(),Font.PLAIN, 14));
		alphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		    
		SourceCode alph1 = lang.newSourceCode(new Coordinates(40, 397), "sourceCode", null, alphProps);
		alph1.addCodeLine("Alphabet", null, 0, null);
		alph1.show();
		
		
		// Source-Code Anzeige
		
		 SourceCodeProperties scProps = new SourceCodeProperties(); 
		 scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE); 
		 scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12)); 
		 scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
		 scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK); 
		 
		 // now, create the source code entity 
		 sc = lang.newSourceCode(new Coordinates(400, 130), "sourceCode",null, scProps); 
		 
		 // add a code line 
		 // parameters: code itself; name (can be null); indentation level; display options 
		 sc.addCodeLine("for (int i=0; i<text.length(); i++)", null, 0, null); 
		 sc.addCodeLine("{", null, 0, null); 
		 sc.addCodeLine(" zeichen = text.charAt(i);", null, 0, null);
		 sc.addCodeLine(" ascii = (byte)zeichen;", null, 0, null); 
		 sc.addCodeLine(" if (ascii<91 && ascii>64)", null, 0, null); 
		 sc.addCodeLine(" {", null, 0, null); 
		 sc.addCodeLine("  ascii += verschieben;", null, 0, null); 
		 sc.addCodeLine("  zeichen = (char)ascii;", null, 0, null); 
		 sc.addCodeLine(" }", null, 0, null); 
		 sc.addCodeLine("}", null, 0, null); 
		
	}
	
	
	
	/**
	 * Caesar Chiffre: Verschiebeschiffre
	 * 
	 * @param String of the word which should be encrypted
	 * @param String of the key
	 * 
	 */
	private void doCaesar(String word_in, String key_in) throws LineNotExistsException, NumberFormatException {

		int tmp1 = 0;
		int tmp2 = 0;
		char [] wordArray = word_in.toCharArray ();				// KlartextString in CharArray umwandeln
				
		String[] wArray = new String[wordArray.length];			// Für die grafische Ausgabe
		for (int i = 0; i < wordArray.length; i++) {
			wArray[i] = ""+wordArray[i];
		}
		
		String[] rArray = new String[wordArray.length];			// Für die grafische Ausgabe
		for (int i = 0; i < wordArray.length; i++) {
			rArray[i] = "?";
		}
		
		// Die leeren Startboxen aufstellen, wobei result erst mal unsichtbar ist
	    init(wArray, rArray);
		lang.nextStep();

		char [] resultArray = new char[word_in.length()];		// Verschluesselter ErgebnisString
		int [] intArray = new int[word_in.length()];			// IntArray erstellen für die ASCII Darstellung des Wortes

		char key = key_in.charAt(0);							// String in Char umwandeln
		int moveCount = key;									// ASCII-Wert aus dem Key rausholen
			
		if(moveCount > 64 && moveCount < 91) moveCount += 32;	// Falls key Großbuchstabe war in Kleinbuchstabe umwandeln
		moveCount -= 96;										// Schrittweite ermittelt
	
		CheckpointUtils.checkpointEvent(this, "schrittweiteEvent", new Variable ("schrittweite", moveCount));
		
		lblKey.addCodeLine("Ihr gewaehlter Schluessel ist - "+ key_in 
				+ " - somit werden " + moveCount 
				+ " Schritte im Alphabet verschoben", null, 0, null);
		lblKey.show();
		
		/* ArrayMarker aufstellen */	
		// AusgangsWort
		ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
	    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Character (i)");   
	    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, waMC);
		ArrayMarker wMarker = lang.newArrayMarker(wa, 0, "Character",null, arrayIMProps);
		wMarker.hide();
		
		// Alphabet_Aktueller_Buchstabe
		ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
		arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");   
		arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, alphMRC);
		ArrayMarker awMarker = lang.newArrayMarker(alphabet, 0, "alphAct", null, arrayJMProps);
		awMarker.hide();
		
		// Alphabet_Zu_Verschluesselnder_Buchstabe
		ArrayMarkerProperties arrayKMProps = new ArrayMarkerProperties();
		arrayKMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "+"+moveCount);   
		arrayKMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, alphMLC);
		ArrayMarker arMarker = lang.newArrayMarker(alphabet, 0, "alphRes", null, arrayKMProps);
		arMarker.hide();
		
		// Codiertes Wort
		ArrayMarkerProperties arrayLMProps = new ArrayMarkerProperties();
		arrayLMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");   
		arrayLMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, raMC);
		ArrayMarker rMarker = lang.newArrayMarker(ra, 0, "result", null, arrayLMProps);
		rMarker.hide();
		sc.highlight(0);
		lang.nextStep();
		
		int temp = 0;
		boolean tmp = false;
		wMarker.show();
		sc.unhighlight(0);
	
		
		lang.nextStep();
		rMarker.show();
		awMarker.show();
		arMarker.show();
		
	/* Wort als ASCII-Zahlen im Array abspeichern 
	 * um im gleichen Schritt auch die Verschiebung 
	 * zu machen
	 */
		for (int i = 0; i < wordArray.length; i++) {
			intArray[i] = wordArray[i];
			if(intArray[i] > 64 && intArray[i] < 91) intArray[i] += 32;		// Gross->Kleinschreibung
		
			if(intArray[i]+moveCount > 122) {								// Wenn am Ende des Alphabets um ...
				temp = 122 - intArray[i];									// 
				temp = moveCount - temp;									// ... wieder ...
				temp += 96;													//
				intArray[i] = temp;											//
				tmp = true;													// ... von Vorne weiter zu machen
			}
			if(tmp) 
			{
				resultArray[i] = (char)intArray[i];
				
			}
			if(!tmp)
			{
				intArray[i] += moveCount;
				resultArray[i] = (char)intArray[i];
			}
			tmp = false;
			sc.highlight(2);
			sc.highlight(3);
			if(i > 0) sc.unhighlight(7);
			
			
			if(i > 0) 
			{ 
				tmp1 = (int)wordArray[i-1]-97;
				if(tmp1 <0){
					alphabet.unhighlightCell(tmp1+32, null, null);
					
				}
				
				else {
					alphabet.unhighlightCell(tmp1, null, null);
					
				}
				wa.unhighlightCell(i-1, null, null);
				alphabet.unhighlightCell((int)resultArray[i-1]-97, null, null);
			}
			;
			tmp1 = (int)wordArray[i]-97;
			tmp2 = (int)resultArray[i]-97;
			
			if(tmp1 < 0){
			awMarker.move(tmp1+32, null, null);
			alphabet.highlightCell(tmp1+32, null, null);
			}
			else{
				awMarker.move(tmp1, null, null);
				alphabet.highlightCell(tmp1, null, null);
			}
			wa.highlightCell(i, null, null);
			wMarker.move(i, null, null);
			
		  
			
			
			lang.nextStep();
			
			if(tmp2 < 0){
			arMarker.move(0, null, null);
			alphabet.highlightCell(0, null, null);
			}
			else{
				arMarker.move(tmp2, null, null);
				alphabet.highlightCell(tmp2, null, null);
			}
		
			sc.unhighlight(2);
			sc.unhighlight(3);
			sc.highlight(6);
		
			lang.nextStep();
			
			if(i > 0) 
			{ 
				ra.unhighlightCell(i-1, null, null);
			}
			rMarker.move(i, null, null);
			ra.put(i, ""+resultArray[i], null, null);
			ra.highlightCell(i, null, null);
			
			
			sc.unhighlight(6);
			sc.highlight(7);
			lang.nextStep();
		}
		
		String resultString = new String(resultArray);
		CheckpointUtils.checkpointEvent(this, "verschluesseltesErgebnisEvent", new Variable ("ergebnis", resultString));


		sc.unhighlight(7);
		wMarker.hide();
		arMarker.hide();
		awMarker.hide();
		rMarker.hide();
		ra.unhighlightCell(wordArray.length - 1, null, null);
		alphabet.unhighlightCell(wordArray[wordArray.length-1]-97, null, null);
		alphabet.unhighlightCell(resultArray[wordArray.length-1]-97, null, null);
		wa.unhighlightCell(wordArray.length-1, null, null);	
	
	}	

	@Override
	public String generate(AnimationPropertiesContainer arg0,Hashtable<String, Object> arg1) {
		if(arg1 != null & arg0 != null){
			
			init();
			
			//Annotationen aktivieren dafür aber Highlighting deaktivieren
			/*super.init();
			SourceCodeProperties props = new SourceCodeProperties();
			props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.blue);
			props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);

			// instantiate source code primitive to work with
			sourceCode = lang.newSourceCode(new Coordinates(20, 100), "sumupCode",
					null, props);
			
			// setup complexity
			vars.declare("int", comp); vars.setGlobal(comp);
			vars.declare("int", assi); vars.setGlobal(assi);
			
			Text text = lang.newText(new Coordinates(500, 200), "...", "Counting Sort Komplexität", null);
			TextUpdater tu = new TextUpdater(text);
			tu.addToken("Compares: ");
			tu.addToken(vars.getVariable(comp));
			tu.addToken(" - Assignments: ");
			tu.addToken(vars.getVariable(assi));
			tu.update();
			
			parse();*/
			
			//INIT Word and Key
			word = (String) arg1.get("Eingabewort");
			key = (String) arg1.get("Eingabeschluessel");
		
		
			//INIT Style and Color
			font = (Font) arg1.get("Schriftart");
			fontColor = (Color) arg1.get("Schriftfarbe");
			waCH =  (Color) arg1.get("EingabewortFarbeCH");
			waEC =  (Color) arg1.get("EingabewortFarbe");
			waEH =  (Color) arg1.get("EingabewortHC");
			waMC =	(Color) arg1.get("EingabewortMC");
			raCH =  (Color) arg1.get("AusgabewortFarbeCH");
			raEC =  (Color) arg1.get("AusgabewortFarbe");
			raEH =  (Color) arg1.get("AusgabewortHC");
			raMC =  (Color) arg1.get("AusgabewortMC");
			alphCH =  (Color) arg1.get("AlphabetFarbeCH");
			alphEC =  (Color) arg1.get("AlphabetFarbe");
			alphEH =  (Color) arg1.get("AlphabetHC");
			alphMLC =  (Color) arg1.get("AlphabetAktBuchstabe");
			alphMRC =  (Color) arg1.get("AlphabetEncrptBuchstabe");
		
			if(word.length() > 0 && key.length() > 0)
			{
			
			this.doCaesar(parseWord(word), key);
			}
			else 
			{
			word = "AVPraktikum";
			key = "m";
			this.doCaesar(word, key);
			}
			return lang.toString();
			}
		else return "";
	}

	@Override
	public String getAlgorithmName() {
    return "Caesar Cipher";
	}

	
	public String getAnimationAuthor() {
		return "Can Güler, Tayfun Atik";
	}

	
/*	public String getCodeExample() {
		return 	"for (int i = 0; i < wordArray.length; i++) {"+"\n"
				+" intArray[i] = wordArray[i];"+"\n"
				+"\n"
				+"if(intArray[i] > 64 && intArray[i] < 91)"+"\n"
			    +" intArray[i] += 32;"  +"\n"
			    +"if(intArray[i]+moveCount > 122) {"+"\n"						
				+" temp = 122 - intArray[i];"	 +"\n"							
				+" temp = moveCount - temp;"	 +"\n"							
				+" temp += 96;"		 +"\n"										
				+" intArray[i] = temp;"	 +"\n"									
				+" tmp = true;"	 +"\n"										
				+" }" +"\n"
				+"if(tmp)" +"\n"
				+" resultArray[i] = (char)intArray[i];" +"\n"
				+"if(!tmp){" +"\n"
				+" intArray[i] += moveCount;" +"\n"
				+" resultArray[i] = (char)intArray[i];" +"\n"
				+" }" +"\n"
				+"tmp = false;" +"\n"
				+"}";
	}
*/
	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Beim Cäsar-Chiffre handelt es sich um eine monoalphabetische " +
		"Substitution (Vertauschung). Dabei wird jedem Buchstaben eines Textes ein anderer eindeutiger " +
		"Buchstabe zugeordnet. Diese Zuordnung ist allerdings nicht willkürlich, sondern basiert " +
		"auf der zyklischen Rotation (Drehung) des Alphabets um k Zeichen, dabei folgt auf Z wieder A. " +
		"Das k ist dann der Schlüssel, mit dem ver- bzw. entschlüsselt wird.";
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
		return algoName;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript(algoName,"Can Güler, Tayfun Atik" , resolution[0], resolution[1]);
		lang.setStepMode(true);
	}	
	
	private String parseWord(String word)
	{
		String result = "";
		char [] wordArray = word.toCharArray ();
		for (int i = 0; i < wordArray.length; i++) {
			if((int) wordArray[i] > 64 && (int) wordArray[i] < 91)
			{
				result += wordArray[i];
			}
			if((int) wordArray[i] > 96 && (int) wordArray[i] < 123)
			{
				result += wordArray[i];
			}
		}
		
			
		
		return result;
	}

	@Override
	public String getAnnotatedSrc() {
		return 	"for (int i = 0; i < wordArray.length; i++) {  	@label(\"test0\") @inc(\""+comp+"\")\n" +
		" if(intArray[i] > 64 && intArray[i] < 91) 				@label(\"comp1\") @inc(\""+comp+"\")\n" +
	    " intArray[i] += 32; 									@label(\"assi1\") @inc(\""+assi+"\")\n" +
	    " intArray[i] = wordArray[i];							@label(\"assi2\") @inc(\""+assi+"\")\n" +
	    "if(intArray[i]+moveCount > 122) {						@label(\"comp2\") @inc(\""+comp+"\")\n" +
		" temp = 122 - intArray[i];								@label(\"assi3\") @inc(\""+assi+"\")\n" +
		" temp = moveCount - temp;								@label(\"assi4\") @inc(\""+assi+"\")\n" +
		" temp += 96;											@label(\"assi5\") @inc(\""+assi+"\")\n" +
		" intArray[i] = temp;									@label(\"assi6\") @inc(\""+assi+"\")\n" +
		" tmp = true;											@label(\"assi7\") @inc(\""+assi+"\")\n" +
		" }														@label(\"test0\") \n" +
		"if(tmp)												@label(\"comp3\") @inc(\""+comp+"\")\n" +
		" resultArray[i] = (char)intArray[i];					@label(\"assi8\") @inc(\""+assi+"\")\n" +
		"if(!tmp){												@label(\"comp4\") @inc(\""+comp+"\")\n" +
		" intArray[i] += moveCount;								@label(\"assi9\") @inc(\""+assi+"\")\n" +
		" resultArray[i] = (char)intArray[i];					@label(\"assi10\") @inc(\""+assi+"\")\n" +
		" }														@label(\"test1\") \n" +
		"tmp = false;											@label(\"assi11\") @inc(\""+assi+"\")\n" +
		"}														@label(\"end\")\n";
	}
}
