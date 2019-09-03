package generators.cryptography;

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
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CBC extends AnnotatedAlgorithm implements Generator{

	private String assi = "Assignements";
//	private String comp = "Compares";
	StringArray messageInBlocks;
	
	Language lang;
	SourceCode functionText;
	Text inputText;
	ArrayProperties arrayProps;
	ArrayMarker i1,i2;
	ArrayMarkerProperties i1Props,i2Props;
	int blocksize = 4;
	Polyline inputArrow1,inputArrow2,fInputArrow,fOutputArrow,loopArrow;
	Circle xor;
	Text xorText,input1Label,input2Label,inputFLabel,outputFLabel,ci,ai;
	TextProperties textProps,labelProps;
	Timing defaultDelay = new TicksTiming(50);
	Timing doubleDefaultDelay = new TicksTiming(100);
	Timing trippleDefaultDelay = new TicksTiming(150);
	Timing fourDefaultDelay = new TicksTiming(200);
	
	public void init(){
		super.init();
		lang = new AnimalScript("CBC-Mode Demo", "Iason Parasiris & Jan Wiese", 640, 480);
		lang.setStepMode(true);
		arrayProps = new ArrayProperties();
		i1Props = new ArrayMarkerProperties();
		i2Props = new ArrayMarkerProperties();
		SourceCodeProperties headProps = new SourceCodeProperties();
		headProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
		headProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.ITALIC, 18));
		SourceCode head = lang.newSourceCode(new Coordinates(5, 0), "head", null, headProps);
		head.addCodeLine("CBC-Mode Demo", null, 0, null);
		initializeSourceCode();
		showFunction();
		paintGraphic();
		vars.declare("int", assi); vars.setGlobal(assi);
		vars.declare("String", "ci");
		vars.declare("String", "temp");
		vars.declare("int", "i", "0");
		vars.declare("int", "i2", "1");
		parse();
	}
	
	private void initializeSourceCode(){
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 18));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		sourceCode = lang.newSourceCode(new Coordinates(450, 15), "sumupCode", null, scProps);
	}
	
	public String functionF(String input){
		Integer inputAsInteger = Integer.parseInt(input);
		switch (inputAsInteger) {
		case 0:
			functionText.highlight(1);
			return "0010";
		case 1:
			functionText.highlight(2);
			return "1010";
		case 10:
			functionText.highlight(3);
			return "1000";
		case 11:
			functionText.highlight(4);
			return "0001";
		case 100:
			functionText.highlight(5);
			return "1110";
		case 101:
			functionText.highlight(6);
			return "1011";
		case 110:
			functionText.highlight(7);
			return "0100";
		case 111:
			functionText.highlight(8);
			return "1101";
		case 1000:
			functionText.highlight(9);
			return "0000";
		case 1001:
			functionText.highlight(10);
			return "0011";
		case 1010:
			functionText.highlight(11);
			return "1001";
		case 1011:
			functionText.highlight(12);
			return "0110";
		case 1100:
			functionText.highlight(13);
			return "0111";
		case 1101:
			functionText.highlight(14);
			return "1111";
		case 1110:
			functionText.highlight(15);
			return "1100";
		case 1111: 
			functionText.highlight(16);
			return "0101";

		default:
			return "0";
		}
	}
	
	public void showFunction(){
		SourceCodeProperties ftProps = new SourceCodeProperties();
		ftProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		ftProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		ftProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		functionText = lang.newSourceCode(new Coordinates(650, 200), "functionText", null, ftProps);
		functionText.addCodeLine("Function F:", null, 0, null);
		functionText.addCodeLine("0000 -> 0010", null, 0, null);
		functionText.addCodeLine("0001 -> 1010", null, 0, null);
		functionText.addCodeLine("0010 -> 1000", null, 0, null);
		functionText.addCodeLine("0011 -> 0001", null, 0, null);
		functionText.addCodeLine("0100 -> 1110", null, 0, null);
		functionText.addCodeLine("0101 -> 1011", null, 0, null);
		functionText.addCodeLine("0110 -> 0100", null, 0, null);
		functionText.addCodeLine("0111 -> 1101", null, 0, null);
		functionText.addCodeLine("1000 -> 0000", null, 0, null);
		functionText.addCodeLine("1001 -> 0011", null, 0, null);
		functionText.addCodeLine("1010 -> 1001", null, 0, null);
		functionText.addCodeLine("1011 -> 0110", null, 0, null);
		functionText.addCodeLine("1100 -> 0111", null, 0, null);
		functionText.addCodeLine("1101 -> 1111", null, 0, null);
		functionText.addCodeLine("1110 -> 1100", null, 0, null);
		functionText.addCodeLine("1111 -> 0101", null, 0, null);
	}
	
	public void paintGraphic(){
		
		PolylineProperties arcProps = new PolylineProperties();
		arcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arcProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		Node[] inputArrowCoordinates1 = new Node[]{new Coordinates(70, 180), new Coordinates(150, 180)};
		Node[] inputArrowCoordinates2 = new Node[]{new Coordinates(160, 80), new Coordinates(160, 170)};
		Node[] fInputArrowCoordinates = new Node[]{new Coordinates(160, 190), new Coordinates(160, 250)};
		Node[] fOutputArrowCoordinates = new Node[]{new Coordinates(200, 275), new Coordinates(260, 275)};
		Node[] loopArrowCoordinates = new Node[]{new Coordinates(290, 290), new Coordinates(290, 310), new Coordinates(40, 310), new Coordinates(40, 195)};
		
		inputArrow1 = lang.newPolyline(inputArrowCoordinates1, "inputArrow1", null, arcProps);
		inputArrow2 = lang.newPolyline(inputArrowCoordinates2, "inputArrow2", null, arcProps);
		fInputArrow = lang.newPolyline(fInputArrowCoordinates, "fInput", null, arcProps);
		fOutputArrow = lang.newPolyline(fOutputArrowCoordinates, "fOutput", null, arcProps);
		loopArrow = lang.newPolyline(loopArrowCoordinates, "loopArrow", null,arcProps);
		loopArrow.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);
		loopArrow.hide();
		
		labelProps = new TextProperties();
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.ITALIC, 15));
		
		input1Label = lang.newText(new Offset(5, -5, "inputArrow1", AnimalScript.DIRECTION_NW), "input", "inputLabel1", null, labelProps);
		input2Label = lang.newText(new Offset(5, 25, "inputArrow2", AnimalScript.DIRECTION_NW), "A[i]", "inputLabel2", null,labelProps);
		inputFLabel = lang.newText(new Offset(5, 13, "fInput", AnimalScript.DIRECTION_NW), "", "fInputLabel", null, labelProps);
		outputFLabel = lang.newText(new Offset(3, -20, "fOutput", AnimalScript.DIRECTION_NW), "ci", "fOutputLabel", null,labelProps);
		
		CircleProperties circleProps = new CircleProperties();
		circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		xor = lang.newCircle(new Offset(10, 0, "inputArrow1", AnimalScript.DIRECTION_E), 10, "xor", null, circleProps);
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
		xorText = lang.newText(new Offset(-15, -11, "xor", AnimalScript.DIRECTION_E), "+", "xorText", null,textProps);
		
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
		lang.newRect(new Coordinates(120, 250), new Coordinates(200, 300), "funktionRect", null,rectProps);
		
		lang.newText(new Offset(2, 5, "funktionRect", AnimalScript.DIRECTION_NW), "Function", "functionLabel", null,labelProps);
	}
	
	public void cbc(String IV, String message){
		lang.nextStep();
		String[] initial = new String[1];
		initial[0] = IV;
		lang.newText(new Coordinates(450, 300), "IV", "IVlabel", null, labelProps);
		messageInBlocks = calculateBlocks(message);
		
		String[] emptyCipher = calculateEmptyCipher(messageInBlocks.getLength());
		StringArray IVArray = lang.newStringArray(new Coordinates(500, 300), initial, "initial", null,arrayProps);
		StringArray chipherInBlocks = lang.newStringArray(new Coordinates(110, 500), emptyCipher, "cipherArray", null, arrayProps);
		ci = lang.newText(new Coordinates(270, 260), "", "ci", null,textProps);
		ai = lang.newText(new Coordinates(140, 50), "", "ai", null,textProps);
		
		TextUpdater inputFUpdater = new TextUpdater(inputFLabel);
		inputFUpdater.addToken("temp = ");
		inputFUpdater.addToken(vars.getVariable("temp"));
		
		TextUpdater ciUpdater = new TextUpdater(ci);
		ciUpdater.addToken(vars.getVariable("ci"));
		
		exec("header");
		lang.nextStep();
		
		exec("var1");
		inputText = lang.newText(new Coordinates(15, 160), "", "input", null, textProps);
		inputText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, defaultDelay);
		IVArray.highlightCell(0, null, defaultDelay);
		TextUpdater inputUpdater = new TextUpdater(inputText);
		vars.declare("String","input",IV);
		inputUpdater.addToken(vars.getVariable("input"));
		inputUpdater.update();
		lang.nextStep();
		
		exec("var2");
		inputText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
		chipherInBlocks.highlightCell(0, null, defaultDelay);
		chipherInBlocks.put(0, IV, defaultDelay, defaultDelay);
		i1 = lang.newArrayMarker(messageInBlocks, 0, "iMarkerPlaintext", null,i1Props);
		i2 = lang.newArrayMarker(chipherInBlocks, 1, "iMarkerCiphertext", null,i2Props);
		ArrayMarkerUpdater amu1 = new ArrayMarkerUpdater(i1, defaultDelay, defaultDelay, messageInBlocks.getLength()-1);
		ArrayMarkerUpdater amu2 = new ArrayMarkerUpdater(i2, defaultDelay, defaultDelay, messageInBlocks.getLength());
		lang.nextStep();
		
		IVArray.unhighlightCell(0, null, defaultDelay);
		chipherInBlocks.unhighlightCell(0, null, defaultDelay);
		amu1.setVariable(vars.getVariable("i"));
		amu2.setVariable(vars.getVariable("i2"));
		
		for (; Integer.parseInt(vars.get("i")) < messageInBlocks.getLength(); ) {
			
			exec("oFor");
			lang.nextStep();
			
			exec("showTemp");
			ai.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, defaultDelay);
			messageInBlocks.highlightCell(Integer.parseInt(vars.get("i")), null, defaultDelay);
			inputText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, doubleDefaultDelay, defaultDelay);
			inputArrow1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, doubleDefaultDelay, defaultDelay);
			inputArrow2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, doubleDefaultDelay, defaultDelay);
			xor.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, trippleDefaultDelay, defaultDelay);
			xorText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, trippleDefaultDelay, defaultDelay);
			ai.setText(messageInBlocks.getData(Integer.parseInt(vars.get("i"))), null, defaultDelay);
			lang.nextStep();
			
			exec("calcTemp");
			ai.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			inputText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			inputArrow1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			inputArrow2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			xor.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			xorText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			vars.set("temp", xor(vars.get("input"), messageInBlocks.getData(Integer.parseInt(vars.get("i")))));
			inputFLabel.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, defaultDelay, null);
			inputFUpdater.update();
			lang.nextStep();
			messageInBlocks.unhighlightCell(Integer.parseInt(vars.get("i")), null, null);
			
			exec("showCi");
			fInputArrow.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, defaultDelay, defaultDelay);
			lang.nextStep();
			
			exec("calcCi");
			vars.set("ci", functionF(vars.get("temp")));
			ciUpdater.update();
			ci.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);
			fOutputArrow.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);
			outputFLabel.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);
			fInputArrow.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
			inputFLabel.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
			chipherInBlocks.put(Integer.parseInt(vars.get("i"))+1, vars.get("ci"), defaultDelay, null);
			lang.nextStep();
			
			
			exec("showCiplus1");
			unhighlightAllInF();
			chipherInBlocks.highlightCell(Integer.parseInt(vars.get("i2")), null, defaultDelay);
			fOutputArrow.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			outputFLabel.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			lang.nextStep();
			
			
			exec("showInput");
			loopArrow.show();
			inputText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, defaultDelay, defaultDelay);
			vars.set("input", vars.get("ci"));
			chipherInBlocks.unhighlightCell(Integer.parseInt(vars.get("i2")) - 1, null, defaultDelay);
			inputUpdater.update();
			if (Integer.parseInt(vars.get("i2")) == messageInBlocks.getLength() + 1) {
				i1.hide();
				i2.hide();
			}
			lang.nextStep();
			
			inputText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			ci.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, defaultDelay);
			loopArrow.hide();
		}
		exec("cFor");
		chipherInBlocks.highlightCell(0, chipherInBlocks.getLength()-1, null, defaultDelay);
		lang.nextStep();
		
		exec("showReturn");
		lang.nextStep();
		
		exec("cAll");
	}
	
	private void unhighlightAllInF() {
		for (int i = 1; i < 17; i++) {
			functionText.unhighlight(i);
		}
		
	}

	private String[] calculateEmptyCipher(int length) {
		String[] output = new String[length+1];
		for (int i = 0; i < output.length; i++) {
			output[i] = "         ";
		}
		return output;
	}

	private StringArray calculateBlocks(String message) {
		String[] returnArray = new String[(message.length()-1)/blocksize + 1];
		int savelasti = 0;
		for (int i = 0; i < returnArray.length-1; i++) {
			returnArray[i] = message.substring(i*blocksize,i*blocksize+blocksize);
			savelasti++;
		}		
		int lengthOfLastBlock = message.length() - (savelasti*4);
		if (lengthOfLastBlock == blocksize){
			returnArray[savelasti] = message.substring(savelasti*blocksize,savelasti*blocksize+blocksize);
		}else {
			String lastBlock = message.substring(4*savelasti,4*savelasti + lengthOfLastBlock);
			for (int i = 0; i < blocksize - lengthOfLastBlock; i++) {
				lastBlock += "0";
			}
			returnArray[savelasti] = lastBlock;
		}
		lang.newText(new Coordinates(5, 400), "plaintext", "plaintext", null, labelProps);
		 lang.newText(new Coordinates(5, 500), "chiphertext", "chiphertext", null, labelProps);
		return lang.newStringArray(new Coordinates(110, 400), returnArray, "plaintext", null,arrayProps);
	}

	private String xor(String input1, String input2) {
		String output = "";
		for (int i = 0; i < input1.length(); i++) {
			if(input1.charAt(i) == input2.charAt(i)) 
				output += "0"; 
			else 
				output += "1";
		}
		return output;
	}
	
	public CBC(){
	
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		init();
		String input = (String) primitives.get("plaintext");
		String IV = (String) primitives.get("IV");
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, props.get("plaintextProps", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, props.get("plaintextProps", AnimationPropertiesKeys.FILL_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, props.get("plaintextProps", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		i1Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("plaintextMarker", AnimationPropertiesKeys.COLOR_PROPERTY));
		i2Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("ciphertextMarker", AnimationPropertiesKeys.COLOR_PROPERTY));
		i1Props.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		i2Props.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i+1");
		cbc(IV, input);
		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Cipher Block Chaining";
	}

	@Override
	public String getAnimationAuthor() {
		return "Iason Parasiris, Jan Wiese";
	}

	@Override
	public String getCodeExample() {
		return "cbc_encrypt (dual A, dual IV){\n" +
				"\tinput := IV;\n" +
		"\tdual cipher := IV;\n" +
		"\t\tfor i := 1 to numberOfBlocks do{\n" + 
		"\t\t\ttemp := input xor A[i];\n"+
		"\t\t\tci := F(temp);\n"+
		"\t\t\tc[i+1] := ci;\n"+
		"\t\t\tinput := ci;\n"+
		"\t\t}\n"+
		"\treturn cipher;\n"+
		"}\n";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Visualisierung des CBC-Modes. Dabei beträgt bei dieser Visualisierung die Blockgröße 4, d.h. IV muss" +
				"eine 4-stellige Dualzahl sein und der Plaintext eine Dualzahl, deren Länge ein Vielfaches von 4 ist. Sollte " +
				"der Plaintext nicht durch 4 teilbar sein werden die restlichen Bits mit '0' aufgefüllt";
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
		return "CBC-Encryption";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public String getAnnotatedSrc() {
		return "cbc_encrypt (dual A, dual IV){ 		@label(\"header\")\n"+
		"input := IV;								@label(\"var1\")\n"+
		"dual cipher := IV;							@label(\"var2\")\n"+
		"  for i := 1 to numberOfBlocks do{			@label(\"oFor\") @inc(\""+assi+"\")\n"+
		"    temp := 								@label(\"showTemp\") @inc(\""+assi+"\")\n" +
		"input xor A[i];							@label(\"calcTemp\") @continue \n"+							
		"    ci := 									@label(\"showCi\") @inc(\""+assi+"\")\n"+
		"F(temp)									@label(\"calcCi\") @continue\n" +
		"    c[i+1] := ci;							@label(\"showCiplus1\") @inc(\""+assi+"\")\n"+
		"    input := ci;							@label(\"showInput\") @inc(\""+assi+"\") @inc(\"i\") @inc(\"i2\")\n"+
		"  }										@label(\"cFor\")\n"+
		"return cipher;								@label(\"showReturn\")\n"+
		"}											@label(\"cAll\")";
	}
}
