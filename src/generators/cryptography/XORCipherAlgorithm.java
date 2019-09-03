package generators.cryptography;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * @author Leonid Khaylov <leonid.khaylov@gmail.com>
 * @version 1.0 2010-06-29
 */
public class XORCipherAlgorithm extends AnnotatedAlgorithm implements generators.framework.Generator {
	private String comp = "Compares";
	private String assi = "Assignments";
	
	private TicksTiming timing = new TicksTiming(100);

	private static final String DESCRIPTION = "In cryptography, the simple XOR cipher is a simple encryption algorithm " +
			"that operates according to the principles: " + "\r\n" +
			"A ^ 0 = A," + "\r\n" +
			"A ^ A = 0," + "\r\n" +
			"(B ^ A) ^ A = B ^ 0 = B," + "\r\n" +
			"where \"^\" denotes the exclusive disjunction (XOR) operation. With this logic, a string of text can be encrypted " +
			"by applying the bitwise XOR operator to every character using a given key. To decrypt the output, merely reapplying " +
			"the key will remove the cipher.";

	private String SOURCE_CODE = "public String encrypt(String message, String key) {" + "\r\n" +
	    "\t" + "String code = \"\";" + "\r\n" +
	    "\t" + "for (int i = 0; i < message.length(); i++){" + "\r\n" +
	    "\t\t" + "code += (char) (message.charAt(i)^key.charAt(i % key.length()));" + "\r\n" +
	    "\t" + "}" + "\r\n" +
	    "\t" + "return code;" + "\r\n" +
	    "}" + "\r\n" + 
	    "\r\n" +
	    "public String decrypt(String code, String key) {" + "\r\n" +
	    "\t" + "String message = \"\";" + "\r\n" +
	    "\t" + "for (int i = 0; i < code.length(); i++){" + "\r\n" +
	    "\t\t" + "message += (char) (code.charAt(i)^key.charAt(i % key.length()));" + "\r\n" +
	    "\t" + "}" + "\r\n" +
	    "\t" + "return message;" + "\r\n" +
	    "}";

	private TextProperties titleProps;

	private TextProperties subTitleProps;

	private ArrayProperties arrayProps;

	private SourceCodeProperties scProps;

	private Text codeLbl;

	private StringArray codeValue;

	private StringArray arSecretKey;

	private ArrayMarker pSecretKey;

	private Text info;

	private Text subTitle;

	private StringArray arMessage;

	private Text secretKeyLbl;
	
	/**
	 * Makes a string to a string array.
	 * @param string original string
	 * @return resulting string array. Each string element becomes array element.
	 */
	public static String[] stringToArray(String string)
	{
		String[] arMessage = new String[string.length()];
		for (int i=0; i< arMessage.length; i++)
			arMessage[i] = string.substring(i, i+1);
		
		return arMessage;
	}
	
	public String encrypt(String message, String secretKey) {		
		exec("encHeader");
		
		lang.nextStep();
		
		exec("encVarsDecl");

		lang.nextStep();
		
		// create array pointer for first array element
		ArrayMarkerProperties ampI = new ArrayMarkerProperties();
		ampI.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ampI.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		ArrayMarker pMessage = lang.newArrayMarker(arMessage, 0, "pMessage", null, ampI);
		ArrayMarkerUpdater amuI = new ArrayMarkerUpdater(pMessage, null, this.timing, arMessage.getLength() - 1);
		
	    String code = "";

	    TextProperties infoProps = new TextProperties();
		infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		infoProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
	    info = lang.newText(new Offset(0, 50, secretKeyLbl, AnimalScript.DIRECTION_SW), "", "info", null, infoProps);
	    
	    codeLbl = lang.newText(new Offset(0, 50, info,
				AnimalScript.DIRECTION_SW), "Code:", "codeLbl",
				null, subTitleProps);
	    String[] arCode = new String[ arMessage.getLength() ];
	    for (int i=0; i<arMessage.getLength(); i++)
	    	arCode[i] = " ";
	    codeValue = lang.newStringArray(new Offset(70, 0, codeLbl, AnimalScript.DIRECTION_NW), arCode, "codeValue", null, arrayProps);
	
	    // while there are unvisited elements in the array
	    char newChar;
	    NumberFormat numberFormat = NumberFormat.getInstance();
		// you can also define the length of integer
		// that is the count of digits before the decimal point
		numberFormat.setMinimumIntegerDigits(8);
		numberFormat.setMaximumIntegerDigits(8);
		numberFormat.setGroupingUsed(false);
		
		exec("encForInit");
		amuI.setVariable(vars.getVariable("i"));
		
		// create array pointer for first array element
		ArrayMarkerProperties codI = new ArrayMarkerProperties();
		codI.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		codI.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i % " + secretKey.length());
		pSecretKey = lang.newArrayMarker(arSecretKey, 0, "pSecretKey", null, codI);
		
		lang.nextStep();
		 
		while (Integer.parseInt(vars.get("i")) < arMessage.getLength()) {
			
			newChar = (char) (message.charAt( Integer.parseInt(vars.get("i")) )^secretKey.charAt( pSecretKey.getPosition() )); 
			code += newChar;
			codeValue.put( Integer.parseInt(vars.get("i")), Integer.toBinaryString( newChar ), null, null );
			
			arMessage.highlightCell(Integer.parseInt(vars.get("i")), null, null);
			arSecretKey.highlightCell(pSecretKey.getPosition(), null, null);
			codeValue.highlightCell( Integer.parseInt(vars.get("i")), null, null);
			info.setText(message.charAt( Integer.parseInt(vars.get("i")) ) + " xor " + secretKey.charAt( pSecretKey.getPosition() ) + " = " + Integer.toBinaryString((int) message.charAt( Integer.parseInt(vars.get("i")) )) + " xor " + Integer.toBinaryString((int) secretKey.charAt( pSecretKey.getPosition() )) + " = " + Integer.toBinaryString( newChar ), null, null);
			info.show();
			exec("encrypt");
			
			lang.nextStep();
			
			arMessage.unhighlightCell(Integer.parseInt(vars.get("i")), null, null);
			arSecretKey.unhighlightCell(pSecretKey.getPosition(), null, null);
			codeValue.unhighlightCell( Integer.parseInt(vars.get("i")), null, null);
			codeValue.highlightElem( Integer.parseInt(vars.get("i")), null, null);
			info.hide();
			
			codeValue.unhighlightElem( Integer.parseInt(vars.get("i")), null, null);
			
			exec("encForInc");
			
			if (Integer.parseInt(vars.get("i")) == arMessage.getLength()-1)
			{//pMessage.moveOutside(null, null);
			}
			else
				{}
			
			if (pSecretKey.getPosition() == arSecretKey.getLength()-1)
				pSecretKey.move(0, null, timing);
			else
				pSecretKey.increment(null, this.timing);
			
			lang.nextStep();
			
			exec("encForComp");
			
			lang.nextStep();
	    }
		
		exec("encForEnd");
		lang.nextStep();
		
		pMessage.hide();		
		pSecretKey.hide();
		exec("encReturn");
		
		lang.nextStep();
		
		exec("encEnd");
		
	    return code;
	}
	
	public String decrypt(String code, String secretKey) {
		Text messageLbl = lang.newText(new Offset(0, 80, codeLbl, AnimalScript.DIRECTION_SW), "Decoded message:", "messageLbl", null, subTitleProps);
		String[] arMessage = new String[ code.length() ];
	    for (int i=0; i<arMessage.length; i++)
	    	arMessage[i] = " ";
		StringArray message = lang.newStringArray(new Offset(30, 0, messageLbl, AnimalScript.DIRECTION_NE), arMessage, "message", null, arrayProps);
		
		exec("decHeader");
		
		lang.nextStep();
		
		exec("decVarsDecl");
		
		lang.nextStep();
		
		// create array pointer for first array element
		ArrayMarkerProperties ampI = new ArrayMarkerProperties();
		ampI.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ampI.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		ArrayMarker pCode = lang.newArrayMarker(codeValue, 0, "pCode", null, ampI);
		
		char newChar;
		String msg = "";
		
		exec("decForInit");
		
		ArrayMarkerUpdater amuI = new ArrayMarkerUpdater(pCode, null, timing, codeValue.getLength()-1);
		amuI.setVariable(vars.getVariable("i"));
		
		lang.nextStep();
		
		pSecretKey.show();
		
	    for (int i = 0; i < code.length(); i++){
	    	arSecretKey.highlightCell( pSecretKey.getPosition(), null, null);
	    	codeValue.highlightCell( Integer.parseInt(vars.get("i")), null, null);
	    	newChar = (char) (code.charAt(i)^secretKey.charAt(i % secretKey.length()));
	    	msg += newChar;
	    	info.setText(secretKey.charAt( pSecretKey.getPosition() ) + " xor " + Integer.toBinaryString((int) code.charAt( Integer.parseInt(vars.get("i")) )) + " = " + Integer.toBinaryString((int) secretKey.charAt( pSecretKey.getPosition() )) + " xor " + Integer.toBinaryString((int) code.charAt( Integer.parseInt(vars.get("i")) )) + " = " + Integer.toBinaryString( newChar ) + " = " + String.valueOf(newChar), null, null);
			info.show();
			message.put( Integer.parseInt(vars.get("i")), String.valueOf(newChar), null, null );
			message.highlightCell( Integer.parseInt(vars.get("i")), null, null);
			
			exec("decrypt");
			
			lang.nextStep();
			
			codeValue.unhighlightCell(Integer.parseInt(vars.get("i")), null, null);
			arSecretKey.unhighlightCell(pSecretKey.getPosition(), null, null);
			message.unhighlightCell( Integer.parseInt(vars.get("i")), null, null);
			message.highlightElem( Integer.parseInt(vars.get("i")), null, null);
			info.hide();
			
			exec("decForInc");
			
			if (pSecretKey.getPosition() == arSecretKey.getLength()-1)
				pSecretKey.move(0, null, timing);
			else
				pSecretKey.increment(null, timing);
			
			lang.nextStep();
			
			exec("decForComp");
			
			message.unhighlightElem( Integer.parseInt(vars.get("i")), null, null);
			
			lang.nextStep();
	    }
	    
	    exec("decForEnd");
		lang.nextStep();
	    
	    pCode.hide();		
		pSecretKey.hide();
		exec("decReturn");
		
		lang.nextStep();
		
		exec("decEnd");
	    
	    return msg;
	}
	
/*	public static void main (String[] args){
		XORCipherAlgorithm prog = new XORCipherAlgorithm();

		// Create primitives hashtable
		Hashtable<String, Object> primitives = new Hashtable<String, Object>();
		primitives.put("Message", "TU Darmstadt");
		primitives.put("Secret Key", "secret");
		
		// Create properties container
		AnimationPropertiesContainer propertiesContainer = new AnimationPropertiesContainer();
		ArrayProperties arrayProps = new ArrayProperties("Array Properties");
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.BLUE);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);
		propertiesContainer.add(arrayProps);
		
		SourceCodeProperties scProps = new SourceCodeProperties("Source Code Properties");
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		propertiesContainer.add(scProps);

		System.out.println(prog.generate(propertiesContainer, primitives));
	 }
*/
	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		this.init();

		// Retrieve optional values
		String message = (String) primitives.get("Message");
		String secretKey = (String) primitives.get("Secret Key");

		// Retrieve properties
		this.arrayProps = (ArrayProperties) props.getPropertiesByName("Array Properties");
		this.scProps = (SourceCodeProperties) props.getPropertiesByName("Source Code Properties");
    sourceCode = lang.newSourceCode(new Coordinates(500, 20), "listSource", null, scProps);

		// Create title and subtitle
		Text title = lang.newText(new Coordinates(20, 30), this.getAlgorithmName(), "header", null, titleProps);
		subTitle = lang.newText(new Offset(0, 30, title, AnimalScript.DIRECTION_NW), "Encryption and decryption example", "subtitle", null, subTitleProps);
		
		// create the original message array
		Text originalMessage = lang.newText(new Offset(0, 80, subTitle,
				AnimalScript.DIRECTION_NW), "Message:", "originalMessage",
				null, subTitleProps);
		arMessage = lang.newStringArray(new Offset(24, 0, originalMessage,
				AnimalScript.DIRECTION_NE), stringToArray(message), "message", null,
				arrayProps);
		
		// create the secret key array
		secretKeyLbl = lang.newText(new Offset(0, 120, originalMessage,
				AnimalScript.DIRECTION_NW), "Secret Key:", "secretKeyLbl",
				null, subTitleProps);
		arSecretKey = lang.newStringArray(new Offset(10, 0, secretKeyLbl,
				AnimalScript.DIRECTION_NE), stringToArray(secretKey), "secretKey", null,
				arrayProps);
		
		lang.nextStep();
		
		// setup complexity
		vars.declare("int", comp, "0"); vars.setGlobal(comp);
		vars.declare("int", assi, "0"); vars.setGlobal(assi);
		
		Text text = lang.newText(new Coordinates(300, 5), "...", "complexity", null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Compares: ");
		tu.addToken(vars.getVariable(comp));
		tu.addToken(" - Assignments: ");
		tu.addToken(vars.getVariable(assi));
		tu.update();
		
		String code = this.encrypt( message, secretKey );
		
		this.decrypt( code, secretKey );

		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "XOR-Cipher Algorithm";
	}

	@Override
	public String getAnimationAuthor() {
		return "Leonid Khaylov";
	}

	@Override
	public String getCodeExample() {
		return this.SOURCE_CODE;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return XORCipherAlgorithm.DESCRIPTION;
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
		return "XOR-Cipher Algorithm (encryption and decryption)";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		super.init();
		
		// Create text properties
		titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 24));

		subTitleProps = new TextProperties();
		subTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 16));
		
		// now, create the source code entity
//		SourceCodeProperties scProps = new SourceCodeProperties();
//		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		
		parse();
	}

	@Override
	public String getAnnotatedSrc() {
		return "public String encrypt(String message, String key) {" + "@label(\"encHeader\")\r\n" +
	    "\t" + "String code = \\\"\\\";" + "@label(\"encVarsDecl\") @inc(\""+assi+"\")\r\n" +
	    "\t" + "for (int i = 0;" + "@label(\"encForInit\") @declare(\"int\", \"i\", \"0\") @inc(\""+assi+"\")\r\n" +
	    " i < message.length();" + "@label(\"encForComp\") @continue @inc(\""+comp+"\")\r\n" +
	    "i++){" + "@label(\"encForInc\") @continue @inc(\"i\") @inc(\""+assi+"\")\r\n" +
	    "\t\t" + "code += (char) (message.charAt(i)^key.charAt(i % key.length()));" + "@label(\"encrypt\") @inc(\""+assi+"\")\r\n" +
	    "\t" + "}" + "@label(\"encForEnd\")\r\n" +
	    "\t" + "return code;" + "@label(\"encReturn\")\r\n" +
	    "}" + "@label(\"encEnd\")\r\n" + 
	    "@continue\r\n" +
	    "public String decrypt(String code, String key) {" + "@label(\"decHeader\")\r\n" +
	    "\t" + "String message = \\\"\\\";" + "@label(\"decVarsDecl\") @inc(\""+assi+"\")\r\n" +
	    "\t" + "for (int i = 0;" + "@label(\"decForInit\") @set(\"i\", \"0\") @inc(\""+assi+"\")\r\n" +
	    " i < code.length();" + "@label(\"decForComp\") @continue @inc(\""+comp+"\")\r\n" +
	    " i++){" + "@label(\"decForInc\") @continue @inc(\"i\") @inc(\""+assi+"\")\r\n" +
	    "\t\t" + "message += (char) (code.charAt(i)^key.charAt(i % key.length()));" + "@label(\"decrypt\") @inc(\""+assi+"\")\r\n" +
	    "\t" + "}" + "@label(\"decForEnd\")\r\n" +
	    "\t" + "return message;" + "@label(\"decReturn\")\r\n" +
	    "}	@label(\"decEnd\")\r\n";
	}
}
