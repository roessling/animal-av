package generators.hashing;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class HashingAnnotated extends AnnotatedAlgorithm implements Generator {

	private int[] hashtable;
	private int[] temp;
	private int[] legend;
	private int size;
	private ArrayList<Integer> collisionIndex;
	private boolean verbose = false;

	private Language lang;

	private ArrayProperties arrayProps;
	private IntArray tempArray, hashtableArray, legendArray;
	private ArrayMarkerProperties ami;
	private ArrayMarkerProperties amj;
	private TextProperties warningProps, textProps, headerProps; // introProps;
	private Text iterationText;
//	private Text infoText1a, infoText1b, infoText1c, infoText1d, infoText1e,
//			infoText1f, infoText1g;
	private Text hashfunktion; //, hashfunktion1, hashfunktion2, hashfunktion3;
	private Text hashIndex;
	private Text infoTextFreeSlot;
	private Text collision;

	private ArrayMarker tempMarker, hashMarker;

	private String comp = "Compares";
	private String assi = "Assignments"; 
	private String collisions = "Collisions";
	
	/* (non-Javadoc)
	 * @see generators.AnnotatedAlgorithm#init()
	 */
	public void init() {

		lang = new AnimalScript("Lineares Hashing Animation",
				"David Sharma und Björn Pantel", 640, 480);

		lang.setStepMode(true);

		super.init();
		
		SourceCodeProperties props = new SourceCodeProperties();
		props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
		// instantiate source code primitive to work with
		sourceCode = lang.newSourceCode(new Coordinates(800, 300), "sumupCode", null, props);
		
		
		vars.declare("int", collisions, "0");   vars.setGlobal(collisions);
		vars.declare("int", comp, "0"); vars.setGlobal(comp);
		vars.declare("int", assi, "0"); vars.setGlobal(assi);
		// parsing anwerfen
		parse();
		initProps();

	}
	
	
	@Override
	public String getAnnotatedSrc() {
		return "public void runHashing(int[] data) {							@label(\"header\")\n" +
				"  int k = 0; 													@label(\"initK\") @declare(\"int\", \"k\", \"0\") @inc(\""+assi+"\")\n" +
				"  for (int j = 0; 												@label(\"jForInit\")  @declare(\"int\", \"j\", \"0\") @inc(\""+assi+"\")\n" +
				"		   j < input.length; 										@label(\"jForComp\") @continue @inc(\""+comp+"\")\n" +
				"			   j++) { 											@label(\"jForInc\")	@continue @inc(\"j\") @inc(\""+assi+"\")\n" +			
				"	  for (int i = 0;												@label(\"iFor\") @declare(\"int\", \"i\", \"0\")  @inc(\""+assi+"\")\n" +
				"		  i < hashtable.length; 									@label(\"iForComp\") @continue @inc(\""+comp+"\")\n" +
				"			   i++) { 											@label(\"iForInc\") @continue @inc(\"i\")@inc(\""+collisions+"\") @inc(\""+assi+"\")\n" +
				"		  k = ((key % hashtable.length) + i) % hashtable.length;	@label(\"calcHash\") @inc(\""+assi+"\")\n" +
				"		  if (hashtableArray.getData(k) == 0) { 					@label(\"checkFreeSlot\")@inc(\""+comp+"\")\n"+
				"			  break;	 											@label(\"break\")\n"+
				"		  } 														@label(\"endIf\")\n"+				
				"	  }															@label(\"endiFor\")\n" +
				"	  hashtable[k] = input[j];									@label(\"setHash\")@inc(\""+assi+"\")\n" +
				"  }																@label(\"endjFor\")\n" +
				"}																@label(\"endMethod\")\n";
		
	}
	public void initProps() {
		headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 24));
			
		 /*textProps = new TextProperties();
		 textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		 "SansSerif", Font.PLAIN, 14));
		 

		
		 introProps = new TextProperties();
		 introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		 "SansSerif", Font.PLAIN, 14));
		  
		 warningProps = new TextProperties();
		 warningProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		 "SansSerif", Font.BOLD, 18));
		 warningProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		 
		 arrayProps = new ArrayProperties();
		 arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		 Color.BLUE); // color red
		 arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		 arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		 arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		 Color.RED);
		 arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
		 Color.ORANGE);
		 
		 ami = new ArrayMarkerProperties();
		 ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		 ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		  
		 amj = new ArrayMarkerProperties();
		 amj.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		 amj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "k");*/
		 
	}

	public void showIntro() {
		lang.newText(new Coordinates(20, 30),
				"Hashing mit linearer Sondierung", "header", null, headerProps);

		//lang.nextStep();

		/*infoText1a = lang.newText(new Offset(20, 50, "header", AnimalScript.DIRECTION_SW),
						"Eine Hashtabelle ist eine Datenstruktur, die fuer schnelle Zugriffe auf Daten konzipiert wurde.",
						"infoText1a", null, introProps);
		lang.nextStep();

		infoText1b = lang.newText(new Offset(0, 20, "infoText1a",AnimalScript.DIRECTION_SW),
						"Mit der Hashfunktion werden Schluessel (KEY) auf eine Adresse der Hashtabelle abgebildet.",
						"infoText1b", null, introProps);

		lang.nextStep();

		hashfunktion1 = lang.newText(new Offset(50, 30, "infoText1b",
				AnimalScript.DIRECTION_SW),
				"Hashfunktion: SCHLUESSEL mod Hashtabellenlaenge = Adresse ",
				"hashfunktion1", null, warningProps);

		lang.nextStep();

		infoText1c = lang.newText(new Offset(0, 50, "infoText1b",AnimalScript.DIRECTION_SW),
						"Die Schluessel werden hier als natuerliche Zahlen repraesentiert.",
						"infoText1c", null, introProps);

		lang.nextStep();

		infoText1d = lang.newText(new Offset(0, 20, "infoText1c",AnimalScript.DIRECTION_SW),
						"Bei der Abbildung von Schluesseln auf die Adressen der Tabelle kann es zur Kollisionen kommen",
						"infoText1d", null, introProps);

		lang.nextStep();

		infoText1e = lang.newText(new Offset(0, 20, "infoText1d",
				AnimalScript.DIRECTION_SW),
				"wenn beide Schluessel die selbe Adresse zugewiesen bekommen.",
				"infoText1e", null, introProps);

		lang.nextStep();

		infoText1f = lang.newText(new Offset(0, 20, "infoText1e",AnimalScript.DIRECTION_SW),
						"Um dieses Problem zu loesen gibt es mehrere Kollisionsstrategien. Die Strategie in dieser Animation nennt man 'lineare Sondierung'.",
						"infoText1f", null, introProps);
		lang.nextStep();

		hashfunktion2 = lang.newText(new Offset(50, 30, "infoText1f",
				AnimalScript.DIRECTION_SW), "Hashfunktion/Kollisionfunktion: ",
				"hashfunktion2", null, warningProps);

		lang.nextStep();

		hashfunktion3 = lang
				.newText(new Offset(0, 20, "hashfunktion2",	AnimalScript.DIRECTION_SW),
						"((SCHLUESSEL mod Hashtabellenlaenge) + i) mod Hashtabellenlaenge = Adresse ",
						"hashfunktion3", null, warningProps);

		lang.nextStep();

		infoText1g = lang.newText(new Offset(0, 100, "infoText1f",AnimalScript.DIRECTION_SW),
						"Im Falle einer Kollision wird iterativ die Variable i erhoeht, bis ein freies Feld zur Verfuegung steht.",
						"infoText1f", null, introProps);

		lang.nextStep();
		infoText1a.hide();
		infoText1b.hide();
		infoText1c.hide();
		infoText1d.hide();
		infoText1e.hide();
		infoText1f.hide();
		infoText1g.hide();
		hashfunktion1.hide();
		hashfunktion2.hide();
		hashfunktion3.hide();*/

	}

	public void initAnimation() {

		hashfunktion = lang.newText(new Offset(50, 30, "header", AnimalScript.DIRECTION_SW),
						"Hashfunktion/Kollisionfunktion: ((SCHLUESSEL mod Hashtabellenlaenge) + i) mod Hashtabellenlaenge = Adresse ",
						"hashfunktion", null, textProps);

		tempArray = lang.newIntArray(new Offset(200, 100, "hashfunktion",
						AnimalScript.DIRECTION_SW), temp, "tempArray", null,
						arrayProps);
		lang.newText(new Offset(-150, 0, "tempArray", AnimalScript.DIRECTION_NW),
				"Eingabedaten: ", "InputLine", null, textProps);

		hashtableArray = lang.newIntArray(new Offset(0, 150, "tempArray",
				AnimalScript.DIRECTION_SW), hashtable, "hashtable", null,
				arrayProps);
		lang.newText(
				new Offset(-150, 0, "hashtable", AnimalScript.DIRECTION_NW),
				"Hashtabelle: ", "hashTableLine", null, textProps);

		legendArray = lang.newIntArray(new Offset(0, 160, "hashtable",
				AnimalScript.DIRECTION_SW), legend, "legendArray", null,
				arrayProps);
		lang.newText(new Offset(-150, 0, "legendArray",
				AnimalScript.DIRECTION_NW), "Legende: ", "legendLine", null,
				textProps);

		initLegend();

		lang.newText(new Offset(0, 20, "legendArray",AnimalScript.DIRECTION_SW),
						"1. Slot: Leerer Slot (in Hashtabelle). Ansonsten: unbearbeitetes Element (Eingabedaten). ",
						"legendLine", null, textProps);

		lang.newText(new Offset(0, 40, "legendArray",AnimalScript.DIRECTION_SW),
						"2. Slot: Element eingefuegt (in Hashtabelle). Ansonsten: Element wird bearbeitet (Eingabedaten). ",
						"legendLine", null, textProps);

		lang.newText(new Offset(0, 60, "legendArray", AnimalScript.DIRECTION_SW),
				"3. Slot: Kollision mit Element (in Hashtabelle). ",
				"legendLine", null, textProps);

		infoTextFreeSlot = lang.newText(new Offset(0, 30, "hashtable",
				AnimalScript.DIRECTION_SW), "", "infoTextFreeSlot", null,
				textProps);
		infoTextFreeSlot.hide();

		lang.nextStep();

	/*	tempMarker = lang.newArrayMarker(tempArray, 0, "tempMarker", null, ami);

		hashMarker = lang.newArrayMarker(hashtableArray, 0, "hashMarker", null,
				amj);

		lang.nextStep();*/

		hashIndex = lang.newText(new Offset(0, 10, "hashfunktion",
				AnimalScript.DIRECTION_SW), "Ergebnis: ", "hashIndex", null,
				textProps);
		collision = lang
				.newText(new Offset(0, 75, "hashtable",
						AnimalScript.DIRECTION_SW), "", "collision", null,
						warningProps);
		collision.hide();
		iterationText = lang.newText(new Offset(10, 0, "hashfunktion",
				AnimalScript.DIRECTION_NE), "", "iterationText", null,
				warningProps);
		
		Text collsionText = lang.newText(new Offset(0, 50, "iterationText", AnimalScript.DIRECTION_SW), "", "collisionText", null);
		TextUpdater tu = new TextUpdater(collsionText);
		tu.addToken("Kollisionen: ");
		tu.addToken(vars.getVariable(collisions));
		tu.update();
		
		Text comparisonText = lang.newText(new Offset(0, 25, "collisionText", AnimalScript.DIRECTION_SW), "", "comparisonText", null);
		TextUpdater comptu = new TextUpdater(comparisonText);
		comptu.addToken("Vergleiche: ");
		comptu.addToken(vars.getVariable(comp));
		comptu.update();
		
		Text assiText = lang.newText(new Offset(0, 25, "comparisonText", AnimalScript.DIRECTION_SW), "", "assiText", null);
		TextUpdater assitu = new TextUpdater(assiText);
		assitu.addToken("Zuweisungen: ");
		assitu.addToken(vars.getVariable(assi));
		assitu.update(); // zum Initialisieren
		
		
		

	}

	public void initLegend() {
		legendArray.highlightCell(1, null, null);
		legendArray.highlightCell(2, null, null);
		legendArray.highlightElem(2, null, null);
	}

	public void runHashing(int[] input) {
		temp = input;
		size = getPrime(input.length);
		hashtable = new int[size];

		legend = new int[3];
		collisionIndex = new ArrayList<Integer>();

		showIntro();
		//lang.nextStep();

		initAnimation();

		exec("header");
		lang.nextStep();
		exec("initK");
		hashMarker = lang.newArrayMarker(hashtableArray, 0, "hashMarker", null,
				amj);
		lang.nextStep();
		
		exec("jForInit");
		tempMarker = lang.newArrayMarker(tempArray, 0, "tempMarker", null, ami);
		tempArray.highlightCell(0, null, null);
		lang.nextStep();
		exec("jForComp");
		//lang.nextStep();
		
		for (int j = 0; j < input.length; j++) {
			tempMarker.move(j, null, null);
			tempArray.highlightCell(j, null, null);
			lang.nextStep();
			addElement(temp[j]);
			//lang.nextStep();
			exec("jForInc");

		}
		
		exec("endjFor");
		lang.nextStep();
		exec("endMethod");

	}

	/**
	 * find the smallest prime >= n copied from
	 * http://java.sun.com/developer/JDCTechTips/2002/tt0806.html
	 * 
	 * @param n
	 * @return smalles prime >= n
	 */
	static int getPrime(int n) {
		int n2 = n;
    while (!isPrime(n2))
			n2++;
		return n2;
	}

	/**
	 * Tests whether a given number n is a prime Copied from
	 * http://java.sun.com/developer/JDCTechTips/2002/tt0806.html
	 * 
	 * @param n
	 * @return
	 */
	static boolean isPrime(int n) {
		// 2 is the smallest prime
		if (n <= 1)
			return (n == 1);
		if (n == 2)
			return true;

		// even numbers other than 2 are not prime
		if (n % 2 == 0)
			return false;

		// check odd divisors from 3
		// to the square root of n
		for (int i = 3, end = (int) Math.sqrt(n); i <= end; i += 2)
			if (n % i == 0)
				return false;
		return true;
	}

	public void addElement(int i) {
		// find the location for the element in the hashtable and add it to the
		// table
		//exec("initK");
		int k = findLocation(i);

		//hashtableArray.highlightCell(k, null, null);
		
		//lang.nextStep();
		exec("setHash");
		hashtableArray.put(k, i, null, null);
		
		infoTextFreeSlot.hide();
		
		lang.nextStep();
	}

	public int findLocation(int key) {
		//exec("initK");
		int loc = 0;

		//lang.nextStep();
		
		exec("iFor");
		iterationText.setText("Iteration: i= " + vars.getVariable("i"), null, null);
		lang.nextStep();
		exec("iForComp");
		//lang.nextStep();
		for (int i = 0; i < hashtable.length; i++) {
			// compute the location for the element in the hashtable
			iterationText.setText("Iteration: i= " + vars.getVariable("i"), null, null);
			//iterationText.show();
			lang.nextStep();
			
			hashfunktion.setText("Hashfunktion: (("
					+ tempArray.getData(tempMarker.getPosition()) + " mod "
					+ size + ") + " + i + ") mod " + size, null, null);

			exec("calcHash");
			loc = computeLocation(key, i);
			//lang.nextStep();
			hashIndex.setText("Ergebnis: " + loc, null, null);

			//lang.nextStep();

			hashMarker.move(loc, null, null);

			lang.nextStep();

			// if the slot is empty, return the location
			// else print out, that a collision is detected
			// and search for a free slot until you found one
			exec("checkFreeSlot");
			if (hashtableArray.getData(loc) == 0) {
				lang.nextStep();
				exec("break");
				hashtableArray.highlightCell(loc, null, null);
				//iterationText.hide();
				break;
				
			}
			lang.nextStep();
			//exec("countCollision");
			print("collison detected!");
			collisionIndex.add(loc);
			collision.setText("Kollision bei Index " + hashMarker.getPosition()
					+ " mit Eintrag "
					+ hashtableArray.getData(hashMarker.getPosition())
					+ "! Benutze Kollisionsfunktion mit i = " + (i + 1), null,
					null);

			hashtableArray.highlightElem(loc, null, null);

			collision.show();
			lang.nextStep();

			collision.hide();
			//exec("endiFor");
			//iterationText.hide();
			//lang.nextStep();
			exec("iForInc");
			

		}
		
		
		
		for (Iterator<Integer> iterator = collisionIndex.iterator(); iterator.hasNext();) {
			Integer temp = iterator.next();
			hashtableArray.unhighlightElem(temp, null, null);
		}
		collisionIndex.clear();

		print("Found slot " + loc);

		infoTextFreeSlot.setText("Freier Platz im Feld: " + loc
				+ " => Fuege Daten ein!", null, null);
		infoTextFreeSlot.show();
		
		lang.nextStep();
		
		exec("endIf");
		
		//lang.nextStep();

		return loc;
	}

	public int computeLocation(int key, int i) {
		// the formula for computing a location
		return ((key % hashtable.length) + i) % hashtable.length;
	}

	public void print(String s) {
		if (verbose)
			System.out.println(s);
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		init();

		
				
		textProps = (TextProperties) props.getPropertiesByName("textProps");
		
//		TextProperties introProps = (TextProperties) props.getPropertiesByName("introProps");
		
		warningProps = (TextProperties) props
				.getPropertiesByName("warningProps");
		

		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");

		ami = (ArrayMarkerProperties) props.getPropertiesByName("ami");
		
		amj = (ArrayMarkerProperties) props.getPropertiesByName("amj");
	
		int[] input = (int[]) primitives.get("intArray");

		runHashing(input);

		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
    return "Hashing mit linearer Sondierung";
	}

	@Override
	public String getAnimationAuthor() {
		return "David Sharma, Björn Pantel";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Bemerkung: Als Eingabedaten sind alle Integerzahlen zulässig mit Ausnahme der 0\n" +
				"Die 0 ist in der Hashtabelle eine Markierung dafür, das der Platz noch frei ist.\n" +
				"\n" +
				"Animiert einen Hashing-Algorithmus mit linearer Sondierung\nEine Hashtabelle ist eine Datenstruktur, die fuer schnelle Zugriffe auf Daten konzipiert wurde.\n"
				+ "Mit der Hashfunktion werden Schluessel (KEY) auf eine Adresse der Hashtabelle abgebildet.\n"
				+ "Die Schluessel werden hier als natuerliche Zahlen repraesentiert.\n"
				+ "Bei der Abbildung von Schluesseln auf die Adressen der Tabelle kann es zur Kollisionen kommen, wenn beide Schluessel die selbe Adresse zugewiesen bekommen.\n"
				+ "Um dieses Problem zu loesen gibt es mehrere Kollisionsstrategien. Die Strategie in dieser Animation nennt man 'lineare Sondierung'.\n"
				+ "Hashfunktion/Kollisionfunktion: ((SCHLUESSEL mod Hashtabellenlaenge) + i) mod Hashtabellenlaenge = Adresse \n"
				+ "Im Falle einer Kollision wird iterativ die Variable i erhoeht, bis ein freies Feld zur Verfuegung steht.\n";
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	@Override
	public String getName() {
		return "Hashing mit linearer Sondierung (annotiert)";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}
