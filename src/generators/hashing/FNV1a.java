package generators.hashing;

import algoanim.animalscript.AnimalScript;
import static algoanim.animalscript.AnimalScript.*;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.*;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.hashing.helpers.FNV1aUtils;

import java.awt.Color;
import java.awt.Font;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

public class FNV1a implements Generator {
	/**
	 * Properties
	 */
	private ArrayProperties ARRAY;
	private TextProperties TITLE;
    private TextProperties TITLE2;
	private TextProperties TEXT;
	private TextProperties SYMBOL;
	private SourceCodeProperties CODE;
	private PolylineProperties LINE;
	private RectProperties TITLE_RECT;
	private ArrayMarkerProperties ARRAY_MARKER;
	
	/**
	 * Size Constants
	 */
	private static final int DIST_V_OPERATOR = 55;
	private static final int OPERATOR_SIZE = 10;
	
	/**
	 * Content Constants
	 */
	private static final BigInteger[] OFFSET_BASIS = new BigInteger[]{
		new BigInteger("2166136261"),
		new BigInteger("14695981039346656037"),
		new BigInteger("144066263297769815596495629667062367629"),
		new BigInteger("100029257958052580907070968620625704837"
		+ "092796014241193945225284501741471925557"),
		new BigInteger("965930312949666949800943540071631046609"
		+ "041874567263789610837432943446265799458293219771"
		+ "643844981305189220653980578449532823934008387619"
		+ "1928701583869517785"),
		new BigInteger("141977950649476210687220706414032183208"
		+ "806227954419339608784749146175827232522967323037"
		+ "177221508640965212023555493656281746691085718147"
		+ "604710150761480297559698040773201576924585630032"
		+ "153049571501574036444603635505054127112859663616"
		+ "102678680828938239637904393364110868845841077350"
		+ "10676915"),};
	private static final BigInteger[] FNV_PRIME = new BigInteger[]{
		new BigInteger("16777619"),
		new BigInteger("1099511628211"),
		new BigInteger("309485009821345068724781371"),
		new BigInteger("374144419156711147060143317175368453031918731002211"),
		new BigInteger("358359158748448673689190764890951084499463279557543"
		+ "92558399825615420669938882575126094039892345713852759"),
		new BigInteger("501645651011311865543459881103527895503076534540479"
		+ "074430301752383111205510814745150915769222029538271616265187"
		+ "852689524938529229181652437508374669137180409427187316048473"
		+ "7966720260389217684476157468082573")};
	
	/**
	 * Timing Constants
	 */
	public static final TicksTiming DEFAULTTIMING = new TicksTiming(15);
	public static final TicksTiming LONGTIMING = new TicksTiming(40);
	public static final TicksTiming LONGDELAY = new TicksTiming(80);	
    
	/**
	 * Other Definitions
	 */
	private Language lang;
	private Locale locale;
	private LinkedList<String> translatedText;
	private Text titleText;
	private Rect titleBorder;
	private Text descriptionText;
	private BigInteger hash;
	private int hexLength;
	private int exponent;
    
    /**
	 * Input Primitives
	 */	
    private String buffer;    
    private int hash_size;
    

	/**
	 * Constructor, generates translatable data
	 * @param locale Language
	 */
	public FNV1a(Locale locale) {
		this.locale = locale;
		
		translatedText = new LinkedList<String>();
		
		if(locale == Locale.GERMAN) {
			//Description
			translatedText.add("In der Informatik bezeichnet FNV1a einen Algorithmus zur Generierung von Streuwerten über Datenfelder, eine sog. Hash-Funktion.");
			translatedText.add("Der Name setzt sich aus den Nachnamen von Glenn Fowler, Curt Landong Noll und Phong Vo zusammen, die den Algorithmus in Kooperation entwickelten.");
			translatedText.add("FNV erfüllt alle Kriterien einer guten Hashing-Funktion und findet überall breiten Einsatz dort, wo große Datenmengen verarbeitet werden sowie Schnelligkeit und Zuverlässigkeit gefordert sind");
			translatedText.add("(z.b. Datenbanken und E-Mail-Servern.), eignet sich jedoch nicht für den kryptographischen Einsatz.");
			translatedText.add("Hinweis: FNV1 bzw. FNV1a ist eine Weiterentwicklung des FNV-0 Algorithmus, dabei gilt heute FNV-0 als veraltet.");
			translatedText.add("         FNV1 und FNV1a unterscheiden sich nur in der Reihenfolge des XOR-Operation und der Multiplikation voneinander.");
			
			//Pseudo Code
			translatedText.add("Initalisierung:");
			translatedText.add("Setze Prime = %s");
			translatedText.add("Setze Hash = %s");
			translatedText.add("Für jeden 8b-Block in der Eingabe:");
			translatedText.add("Zwischenergebnis = Hash XOR Block");
			translatedText.add("Hash = Zwischenergebnis * Prime");
			translatedText.add("Setze Ergebnis = Hash");
			
			//Outro Description
			translatedText.add("Nach durchlaufen des Algorithmuses wurde zu dem");
			translatedText.add("Wert %s");
			translatedText.add("der Hash-Wert %s");
			translatedText.add("berechnet.");
			translatedText.add("Damit ist der Algorithmus fertig!!!");
			
			//steps
			translatedText.add("Aktueller Schritt:");
			translatedText.add("Es wird die zur Bitbreite des Hashes passende Primzahl geladen");
			translatedText.add("Es wird der zur Bitbreite des Hashes passende Initialswert geladen");
			translatedText.add("Jedes Byte in der Eingabe wird einzeln bearbeitet");
			translatedText.add("Det aktuelle Hash-Wert wird Xor dem Byte verrechnet");
			translatedText.add("Das Zwischenergebnis wird mit der Primzahl multipliziert");
			translatedText.add("Das Endergebnis steht nun in dem Hash Feld");
		} else {
			translatedText.add("In Computer science FNV1a is a Algorithm to calculate hash-values.");
			translatedText.add("The Name is set by the last names of Glenn Fowler, Curt Landong Noll and Phong Vo, they created the algorithm in cooperation.");
			translatedText.add("FNV has all properties of a good hashing-function, but cannot used in security mechanisms and cryptography.");
			translatedText.add("");
			translatedText.add("Hinweis: FNV1 bzw. FNV1a is a upgrade of the FNV-0 Algorithm, FNV-0 is now deprecated.");
			translatedText.add("         The differences between FNV1 und FNV1a is the order of multiply and XOR Operation.");
			
			//Pseudo Code
			translatedText.add("Init:");
			translatedText.add("Set Prime = %s");
			translatedText.add("Set Hash = %s");
			translatedText.add("For each 8b-Block of the Buffer:");
			translatedText.add("temp_result = Hash XOR Block");
			translatedText.add("Hash = temp_result * Prime");
			translatedText.add("set result = Hash");
			
			//Outro Description
			translatedText.add("After Iteration through the algorithm to");
			translatedText.add("the buffer value %s");
			translatedText.add("is %s");
			translatedText.add("the hash-value calculated.");
			translatedText.add("The algorithm is now finished!!!");
			
			//steps
			translatedText.add("Current Schritt:");
			translatedText.add("Choose the prime-value, which is dependent from Bit Size");
			translatedText.add("Choose offset-value, which is dependent from bit size");
			translatedText.add("Every Byte in the Buffer is used independent from others");
			translatedText.add("Calculate XOR between actual Hash-value and byte from buffer");
			translatedText.add("multiply temporary result with prime");
			translatedText.add("The result is stored in the hash-value");			
		}
			
	}
	
	@Override
    public void init(){
        lang = new AnimalScript("FNV1a", "Matthias Mettel, Fabian Möller", 800, 600);
		lang.setStepMode(true);
    }

	@Override
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		//Read Properties
		ARRAY = (ArrayProperties)props.getPropertiesByName("Array");
		ARRAY.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 18));
		ARRAY_MARKER = (ArrayMarkerProperties)props.getPropertiesByName("ArrayMarker");
		TITLE = (TextProperties)props.getPropertiesByName("Titel");
		TITLE.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		TITLE2 = (TextProperties)props.getPropertiesByName("Titel2");
		TITLE2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		TEXT = (TextProperties)props.getPropertiesByName("Text");
		TEXT.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		CODE = (SourceCodeProperties)props.getPropertiesByName("Code");
		CODE.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		LINE = (PolylineProperties)props.getPropertiesByName("Linie");
		TITLE_RECT = (RectProperties)props.getPropertiesByName("Titel Umrandung");
		
		//Create Constant Properties
		SYMBOL = new TextProperties();
		SYMBOL.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 26));
		SYMBOL.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		//Read Primitives
		buffer = ((String)primitives.get("Buffer (Hex)")).toUpperCase();
		hash_size = (Integer)primitives.get("hash_size (32, 64, 128)");
		
		//Animate Intro
		Intro();
		
		//Animate Hash-Value Calculation
		Hash();
		
		//Animate Outro
		Outro();
		
		//convert to AnimalScript
        return lang.toString();
    }

	@Override
    public String getName() {
        return "1a";
    }

	@Override
    public String getAlgorithmName() {
        return "FNV";
    }

	@Override
    public String getAnimationAuthor() {
        return "Matthias Mettel, Fabian Möller";
    }

	@Override
    public String getDescription(){
		return String.format("%s\n%s\n\n%s\n%s\n\n%s\n%s",
				translatedText.get(0), translatedText.get(1), translatedText.get(2),
				translatedText.get(3), translatedText.get(4), translatedText.get(5));
    }

	@Override
    public String getCodeExample(){
		return String.format("%s\n    %s\n    %s\n\n%s\n    %s\n    %s\n\n%s",
				translatedText.get(6), String.format(translatedText.get(7), "0x01000193") ,
				String.format(translatedText.get(8), "0x877C9DC5"), translatedText.get(9), translatedText.get(10), translatedText.get(11),
				translatedText.get(12));
    }

	@Override
    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

	@Override
    public Locale getContentLocale() {
        return (locale == Locale.GERMANY) ? locale : Locale.US;
    }

	@Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
    }

	@Override
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
	
	private void Intro() {
		//Create & Display Title
		titleText = lang.newText(new Coordinates(35, 25), "Fowler/Noll/Vo (FNV1a) Hashing", "AnimationTitle", null, TITLE);
		titleBorder = lang.newRect(
				new Offset(-5, -5, titleText, DIRECTION_NW),
				new Offset(5, 5, titleText, DIRECTION_SE),
				"titleBorder", null, TITLE_RECT);

		//Create & Display Description
		SourceCode description = lang.newSourceCode(new Coordinates(30, 80), "desc", null);
		String[] str = getDescription().split("\n");
		for(String s : str)
			description.addCodeLine(s, "", 0, null);

		//Hide Algorithm Description after next step
		lang.nextStep("Start");
		description.hide();		
	}
	
	private void Hash() {
		//Check Data
		FNV1aUtils.Assert(FNV1aUtils.IsHexString(buffer));
		FNV1aUtils.Assert(hash_size >= 32 && hash_size <= 128);
		
		//calculate data sizes
		hexLength = (int) hash_size / 4;
		exponent = (int)(Math.log(hash_size) / Math.log(2));
		int arrayLength = hexLength * 16;
		int arrayLength2 = arrayLength / 2;

		//Choose Prime, OFFSET & convert to Array for Display
		BigInteger prime = FNV_PRIME[exponent - 5];
		String[] primeHexString = FNV1aUtils.IntToStrArray(prime, hexLength);
		BigInteger offset = OFFSET_BASIS[exponent - 5];
		String[] offsetHexString = FNV1aUtils.IntToStrArray(offset, hexLength);

		//Convert buffer to display
		String[] dataArray = FNV1aUtils.split1Char(buffer);
		if (dataArray.length % 2 != 0) {
	    String[] tempdataArray = new String[dataArray.length + 1];
	    for(int i=0 ; i<dataArray.length ; i++) {
	      tempdataArray[i] = dataArray[i];
	    }
	    tempdataArray[tempdataArray.length - 1] = "0";
	    dataArray = tempdataArray;
//      dataArray = Arrays.copyOf(dataArray, dataArray.length + 1);
//			dataArray[dataArray.length - 1] = "0";
		}

		//Create Display
		StringArray ergebnisArray = lang.newStringArray(
				new Offset(100, 250, titleBorder, DIRECTION_NW),
				//new Coordinates(120, 250),
				FNV1aUtils.createFilledStringArray(hexLength, " "),
				"ergebnis_array", null, ARRAY);

		lang.newText(new Offset(-100, -25, ergebnisArray, DIRECTION_NW),
				"Zwischenergebnis:", "ergebnis_text", null);

		Rect xorRect = lang.newRect(
				new Offset(-OPERATOR_SIZE, -DIST_V_OPERATOR - OPERATOR_SIZE, ergebnisArray, DIRECTION_N),
				new Offset(OPERATOR_SIZE, -DIST_V_OPERATOR + OPERATOR_SIZE, ergebnisArray, DIRECTION_N),
				"xor_rect", null);

		lang.newText(new Offset(0, 5, xorRect, DIRECTION_NW), "\u2295", "xorSymbol", null, SYMBOL);

		lang.newPolyline(new Node[]{
					new Offset(0, 0, xorRect, DIRECTION_S),
					new Offset(arrayLength2, 0, ergebnisArray, DIRECTION_NW),},
				"line_xor_ergebnis", null, LINE);

		Node inputArrayPos = dataArray.length < hexLength
				? new Offset(-arrayLength2 + OPERATOR_SIZE, -DIST_V_OPERATOR - 30, xorRect, DIRECTION_NW)
				: new Offset(0, -2 * DIST_V_OPERATOR - 30, ergebnisArray, DIRECTION_NW);

		StringArray inputArray = lang.newStringArray(
				inputArrayPos,
				dataArray,
				"input_array", null, ARRAY);

		lang.newText(new Offset(-50, -25, inputArray, DIRECTION_NW),
				"Eingabe:", "input_text", null);

		Node inputLinePos = dataArray.length < hexLength
				? new Offset(0, 0, inputArray, DIRECTION_S)
				: new Offset(arrayLength2, 0, inputArray, DIRECTION_SW);

		lang.newPolyline(new Node[]{
					inputLinePos,
					new Offset(0, 0, xorRect, DIRECTION_N),},
				"line_input_xor", null, LINE);

		StringArray hashArray = lang.newStringArray(
				new Offset(60, -DIST_V_OPERATOR - 15, ergebnisArray, DIRECTION_NE),
				FNV1aUtils.createFilledStringArray(hexLength, " "),
				"hash_array", null, ARRAY);

		lang.newText(new Offset(-35, -25, hashArray, DIRECTION_NW),
				"Hash:", "hash_text", null);

		lang.newPolyline(new Node[]{
					new Offset(0, 0, hashArray, DIRECTION_W),
					new Offset(0, 0, xorRect, DIRECTION_E),},
				"line_hash_xor", null, LINE);

		StringArray primeArray = lang.newStringArray(
				new Offset(0, 2 * DIST_V_OPERATOR, ergebnisArray, DIRECTION_SW),
				FNV1aUtils.createFilledStringArray(hexLength, " "),
				"prime_array", null, ARRAY);

		lang.newText(new Offset(-50, -25, primeArray, DIRECTION_NW),
				"Primzahl:", "prime_text", null);

		Rect mulRect = lang.newRect(
				new Offset(arrayLength2 - OPERATOR_SIZE, DIST_V_OPERATOR - OPERATOR_SIZE, ergebnisArray, DIRECTION_SW),
				new Offset(arrayLength2 + OPERATOR_SIZE, DIST_V_OPERATOR + OPERATOR_SIZE, ergebnisArray, DIRECTION_SW),
				"mul_rect", null);

		lang.newText(new Offset(3, 6, mulRect, DIRECTION_NW), "\u00D7", "xorSymbol", null, SYMBOL);

		lang.newPolyline(new Node[]{
					new Offset(0, 0, ergebnisArray, DIRECTION_S),
					new Offset(0, 0, mulRect, DIRECTION_N),},
				"line_ergebnis_mul", null, LINE);

		lang.newPolyline(new Node[]{
					new Offset(0, 0, primeArray, DIRECTION_N),
					new Offset(0, 0, mulRect, DIRECTION_S),},
				"line_prime_mul", null, LINE);

		lang.newPolyline(new Node[]{
					new Offset(0, 0, mulRect, DIRECTION_E),
					new Offset(arrayLength + 60 - OPERATOR_SIZE, 0, mulRect, DIRECTION_E),
					new Offset(0, 0, hashArray, DIRECTION_S),},
				"line_prime_hash", null, LINE);

		SourceCode code = lang.newSourceCode(
				new Offset(-60, 15, primeArray, DIRECTION_SW),
				"Pseudo Code", null, CODE);
		code.addCodeLine(translatedText.get(6), "Init", 0, null);
		code.addCodeLine(String.format(translatedText.get(7), FNV1aUtils.IntToStr(prime, hexLength)), "Init1", 1, null);
		code.addCodeLine(String.format(translatedText.get(8),  FNV1aUtils.IntToStr(offset, hexLength)), "Init2", 1, null);
		code.addCodeLine("", "", 0, null);
		code.addCodeLine(translatedText.get(9), "Loop", 0, null);
		code.addCodeLine(translatedText.get(10), "Calc1", 1, null);
		code.addCodeLine(translatedText.get(11), "Calc2", 1, null);
		code.addCodeLine(" ", "", 0, null);
		code.addCodeLine(translatedText.get(12), "End", 0, null);

		Text currentStepTitle = lang.newText(new Offset(100, 0, code, DIRECTION_NE), translatedText.get(18), "currentStepTitle", null, TITLE2);
		descriptionText = lang.newText(new Offset(60, 0, currentStepTitle, DIRECTION_SW), "", "currentStep", null, TEXT);

		ArrayMarker marker = lang.newArrayMarker(inputArray, 0, "inputMarker", null, ARRAY_MARKER);

		lang.nextStep("Init");

		descriptionText.setText(translatedText.get(19), LONGDELAY, LONGDELAY);
		code.highlight("Init");
		code.highlight("Init1");
		FNV1aUtils.highlightAll(lang, primeArray);
		FNV1aUtils.ArraySet(primeArray, primeHexString, DEFAULTTIMING, LONGDELAY, true);
		lang.nextStep();

		descriptionText.setText(translatedText.get(20), LONGDELAY, LONGDELAY);
		code.unhighlight("Init1");
		code.highlight("Init2");
		FNV1aUtils.unhighlightAll(lang, primeArray);
		FNV1aUtils.highlightAll(lang, hashArray);

		FNV1aUtils.ArraySet(hashArray, offsetHexString, DEFAULTTIMING, LONGDELAY, true);
		lang.nextStep();
		FNV1aUtils.unhighlightAll(lang, hashArray);

		code.unhighlight("Init");
		code.unhighlight("Init2");
		BigInteger val, ergebnis;
		hash = offset;
		descriptionText.setText(translatedText.get(21), LONGDELAY, LONGDELAY);

		for (int inputOffset = 0; inputOffset < inputArray.getLength(); inputOffset += 2) {
			if (inputOffset > 0) {
				FNV1aUtils.unhighlight(lang, inputArray, inputOffset - 2, inputOffset - 1);
			}
			code.highlight("Loop");
			marker.move(inputOffset, null, LONGTIMING);
			FNV1aUtils.highlight(lang, inputArray, inputOffset, inputOffset + 2 - 1);
			lang.nextStep("Block " + inputOffset / 2);

			descriptionText.setText(translatedText.get(22), LONGDELAY, LONGDELAY);
			FNV1aUtils.highlightAll(lang, hashArray);
			lang.nextStep();
			FNV1aUtils.unhighlightAll(lang, inputArray);
			FNV1aUtils.unhighlightAll(lang, hashArray);
			FNV1aUtils.highlightAll(lang, ergebnisArray);
			code.unhighlight("Loop");
			code.highlight("Calc1");

//			NEW
			int nrElems = inputArray.getLength();
			String[] data = new String[nrElems];
			for (int pos = 0; pos < nrElems; pos++) {
			  data[pos] = inputArray.getData(pos);
			}
			val = new BigInteger(FNV1aUtils.join(data, inputOffset, 2), 16);//TODO
//      val = new BigInteger(FNV1aUtils.join(inputArray.getData(), inputOffset, 2), 16);

			ergebnis = hash.xor(val);
			FNV1aUtils.ArrayClear(ergebnisArray);
			FNV1aUtils.ArraySet(ergebnisArray, FNV1aUtils.IntToStrArray(ergebnis, hexLength), DEFAULTTIMING, LONGDELAY, true);

			lang.nextStep();
//			unhighlightAll(ergebnisArray);
			FNV1aUtils.highlightAll(lang, primeArray);
			descriptionText.setText(translatedText.get(23), LONGDELAY, LONGDELAY);
			lang.nextStep();
			FNV1aUtils.unhighlightAll(lang, ergebnisArray);
			FNV1aUtils.unhighlightAll(lang, primeArray);
			FNV1aUtils.highlightAll(lang, hashArray);
			code.unhighlight("Calc1");
			code.highlight("Calc2");

			hash = ergebnis.multiply(prime);
			FNV1aUtils.ArrayClear(hashArray);
			FNV1aUtils.ArraySet(hashArray, FNV1aUtils.IntToStrArray(hash, hexLength), DEFAULTTIMING, LONGDELAY, true);
			lang.nextStep();
			FNV1aUtils.unhighlightAll(lang, hashArray);
			code.unhighlight("Calc2");
		}
		descriptionText.setText(translatedText.get(24), LONGDELAY, LONGDELAY);
		marker.moveOutside(null, LONGTIMING);
		code.highlight("End");

		//set last step marker
		lang.nextStep("Ende");		
	}

	private void Outro() {
		//Hide all Primitives
		lang.addLine("hideAll");
		titleBorder.show();
		titleText.show();

		SourceCode outroDescription = lang.newSourceCode(new Offset(10, 45, titleBorder, DIRECTION_NW), "", null, CODE);
		outroDescription.addCodeLine(translatedText.get(13), "", 0, null);
		outroDescription.addCodeLine("", "", 0, null);
		outroDescription.addCodeLine(String.format(translatedText.get(14), buffer), "", 1, null);
		outroDescription.addCodeLine("", "", 0, null);
		outroDescription.addCodeLine(String.format(translatedText.get(15), FNV1aUtils.IntToStr(hash, hexLength)), "", 1, null);
		outroDescription.addCodeLine("", "", 0, null);
		outroDescription.addCodeLine(translatedText.get(16), "", 0, null);
		outroDescription.addCodeLine("", "", 0, null);
		outroDescription.addCodeLine("", "", 0, null);
		outroDescription.addCodeLine(translatedText.get(17), "", 0, null);
	}
}