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
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class CaesarChiffreIPJW extends AnnotatedAlgorithm implements Generator {

	private String assi = "Assignements";
	private String comp = "Compares";
	private Language lang;
	private ArrayProperties arrayProps;
	private ArrayMarkerProperties ami, amc, amtemp;
//	private ArrayMarkerUpdater amuI, amuJ, amuTemp;
	String[] alphabet = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","_"};
	String[] smallLettersAlphabet = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","_"};
	private int n;
	private Text displayedN;
	private TextProperties textProps;
	
	public CaesarChiffreIPJW(){
		
	}
	
	public void init(){
		super.init();
		InitializeSourceCode();
		SourceCodeProperties headProps = new SourceCodeProperties();
		headProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.ITALIC, 18));
		SourceCode head = lang.newSourceCode(new Coordinates(5, 0),"head", null, headProps);
		head.addCodeLine("Caesar-Chiffer Demo", null,0, null);
		arrayProps = new ArrayProperties();
		ami = new ArrayMarkerProperties();
		amc = new ArrayMarkerProperties();
		amtemp = new ArrayMarkerProperties();
		
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSarif", Font.PLAIN, 15));
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		Text inputCode = lang.newText(new Coordinates(5, 90), "","inputCode", null);
		inputCode.setText("plaintext", null, null);
		
		TextProperties nProps = new TextProperties();
		nProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
		displayedN = lang.newText(new Coordinates(450, 280),"", "diplayedN", null);
		vars.declare("int", assi);
		vars.declare("int", comp);
	}
	
	private void InitializeSourceCode(){
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 18));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		sourceCode = lang.newSourceCode(new Coordinates(5, 320), "sumupCode", null, scProps);
	}

	private int letterPositionInAlphabet(String letter){
		for (int i = 0; i < alphabet.length; i++) {
			if (letter.equals(alphabet[i])){
				return i;
			}
		}
		for (int i = 0; i < smallLettersAlphabet.length; i++) {
			if (letter.equals(smallLettersAlphabet[i])){
				return i;
			}
		}
		return -1;
	}
	
	private void caesar_cipher(String[] A, int inputN){
		n = inputN;
		StringArray alphabetArray = lang.newStringArray(new Coordinates(5, 300), alphabet, "alphabetArray", null, arrayProps);
		StringArray inputArray = lang.newStringArray(new Coordinates(80, 100), A, "inputArray", null, arrayProps);
		Timing defaultTiming = new TicksTiming(20);
		
		displayedN.setText("n : " + n, null, null);
		exec("header");
		lang.nextStep();
		
		exec("varDec");
		ArrayMarker c = lang.newArrayMarker(alphabetArray, 0, "c", null , amc);
		ArrayMarker i = lang.newArrayMarker(inputArray, 0, "i", null , ami);
		ArrayMarker temp = lang.newArrayMarker(alphabetArray, 0, "temp", null , amtemp);
		ArrayMarkerUpdater amuI = new ArrayMarkerUpdater(i, defaultTiming, null, inputArray.getLength()-1);
		lang.nextStep();
		
		exec("showField");
		String[] emptyArray = getEmptyArray(A.length);
		StringArray outputArray = lang.newStringArray(new Coordinates(80, 200), emptyArray, "outputArray", null, arrayProps);
		Text outputCode = lang.newText(new Coordinates(5, 190), "", "outputCode", null);
		outputCode.setText("cyphertext", null, null);
		ArrayMarker i2 = lang.newArrayMarker(outputArray, 0, "i", null , ami);
		ArrayMarkerUpdater amuI2 = new ArrayMarkerUpdater(i2, defaultTiming, null, outputArray.getLength()-1);
		lang.nextStep();
		
		amuI.setVariable(vars.getVariable("i"));
		amuI2.setVariable(vars.getVariable("i"));
		
		for (;Integer.parseInt(vars.get("i")) < inputArray.getLength();) {
			exec("oFor");
			lang.nextStep();
			amuI.update();
			amuI2.update();
			
			exec("calcTemp");
			inputArray.highlightCell(Integer.parseInt(vars.get("i")), null, defaultTiming);
			temp.move(letterPositionInAlphabet(A[Integer.parseInt(vars.get("i"))]), null, defaultTiming);
			lang.nextStep();
			
			exec("calcC");
			inputArray.unhighlightCell(Integer.parseInt(vars.get("i")), null, defaultTiming);
			c.move((temp.getPosition() + n) % alphabet.length, null, defaultTiming);
			lang.nextStep();
			
			exec("setOut");
			alphabetArray.highlightCell(c.getPosition(), null, defaultTiming);
			outputArray.highlightCell(Integer.parseInt(vars.get("i")), defaultTiming, defaultTiming);
			outputArray.put(Integer.parseInt(vars.get("i")), alphabet[c.getPosition()], defaultTiming, defaultTiming);
			lang.nextStep();
			
			exec("cFor");
			alphabetArray.unhighlightCell(c.getPosition(), null, defaultTiming);
			outputArray.unhighlightCell(Integer.parseInt(vars.get("i")) - 1, null, defaultTiming);
			if (Integer.parseInt(vars.get("i")) == inputArray.getLength()) {
				i2.hide();
				i.hide();
			}
			lang.nextStep();
		}
		exec("showReturn");
		outputArray.highlightCell(0, outputArray.getLength()-1, null, defaultTiming);
		lang.nextStep();
		
		exec("showEnd");
		lang.nextStep();
	}
	
	
	private String[] getEmptyArray(int length) {
		String[] anEmptyArray = new String[length];
		for (int i = 0; i < anEmptyArray.length; i++) {
			anEmptyArray[i] = "   ";
		}
		return anEmptyArray;
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		lang = new AnimalScript("Caesar-Chiffer Demo", "Iason Parasiris & Jan Wiese", 640, 480);
		lang.setStepMode(true);
		String input = (String) primitives.get("plaintext");
		n = (Integer) primitives.get("n");
		init();
		parse();
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, props.get("array", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, props.get("array", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, props.get("array", AnimationPropertiesKeys.FILLED_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, props.get("array", AnimationPropertiesKeys.FILL_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, props.get("array", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		
		ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("iMarker", AnimationPropertiesKeys.COLOR_PROPERTY));
		amc = new ArrayMarkerProperties();
		amc.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("cMarker", AnimationPropertiesKeys.COLOR_PROPERTY));
		amtemp = new ArrayMarkerProperties();
		amtemp.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("tempMarker", AnimationPropertiesKeys.COLOR_PROPERTY));
		amtemp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "temp");
		amc.set(AnimationPropertiesKeys.LABEL_PROPERTY, "c");
		ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		String[] inputToStringArray = new String[input.length()];
		for (int i = 0; i < input.length(); i++) {
			inputToStringArray[i] = input.substring(i, i+1);
		}
		caesar_cipher(inputToStringArray, n);
		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
    return "Caesar-Verschl\u00fcsselung";
	}

	@Override
	public String getAnimationAuthor() {
		return "Iason Parasiris, Jan Wiese";
	}

	@Override
	public String getCodeExample() {
		return "caesar_cipher (field A, int n){\n" +
				"\tint c, i, temp;, null, 1, null);\n"+
				"\tfield Out;\n"+
				"\t\tfor i : = 0 to A.size - 1 do {\n"+
				"\t\t\ttemp := letterPositionInAlphabet(A[i]);\n"+
				"\t\t\tc := (temp + n) mod A.size;\n"+
				"\t\t\tOut[i] := Alphabet[c];\n"+
				"\t\t}\n"+
				"\treturn Out\n"+
				"}";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Visualisierung der Verschluesselung mit der Caesar-Chiffre, bei der jeder " +
				"Buchstabe (A-Z, a-z, _ für Leerzeichen) durch den Buchstaben repraesentiert wird," +
				"der im Alphabet um n Stellen nach rechts verschoben ist (Modulo 26).";
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
		return "Caesar Verschluesselung mit variablem Verschiebewert";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public String getAnnotatedSrc() {
		return "caesar_cipher (field A, int n){ 	@label(\"header\")\n"+
		"int c, i, temp;							@label(\"varDec\") @declare(\"int\", \"i\")\n"+
		"field Out;									@label(\"showField\")\n"+
		"for i : = 0 to A.size - 1 do {				@label(\"oFor\") @inc(\""+assi+"\") @inc(\""+comp+"\")\n"+	
		"temp := letterPositionInAlphabet(A[i]);	@label(\"calcTemp\") @inc(\""+assi+"\")\n"+
		"c := (temp + n) mod A.size;				@label(\"calcC\") @inc(\""+assi+"\")\n"+		
		"Out[i] := Alphabet[c];						@label(\"setOut\") @inc(\""+assi+"\")\n"+
		"}											@label(\"cFor\") @inc(\"i\")\n"+
		"return Out									@label(\"showReturn\")\n"+
		"}											@label(\"showEnd\")\n";
	}
}
