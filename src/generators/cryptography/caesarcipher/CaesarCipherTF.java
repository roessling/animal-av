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
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class CaesarCipherTF extends AnnotatedAlgorithm implements generators.framework.Generator{
	Language lang;
	String[] charsArray;
	String[] charsArrayShifted;
	StringArray sab;
	StringArray saa;
	StringArray abc;
	StringArray abcs;
	String[] msgEnc;
	Timing timing;
	
	String message;
	int shift;
	SourceCodeProperties scp;
	ArrayProperties ap;


	public CaesarCipherTF() {
		timing = new TicksTiming(20);
	}

	@Override
	public String getAnnotatedSrc() {
		final String annotatedCode = "procedure CaesarCipher( message, shift ) defined as:	@label(\"procedure\") \n" +
		"  alphabet = ['A','B','C',...]														@label(\"alphabet\") \n"+
		"  for each i in sequence from 0 to length(alphabet) do:							@label(\"forShift\") \n"+
		"    shiftedAlphabet[i] = alphabet[(i+shift) % length(alphabet)]					@label(\"shiftedAssign\") \n"+
		"  done																				@label(\"doneShift\") \n"+
		"  for j in sequence from 0 to length(message) do:									@label(\"forEnc\") \n"+
		"    k = findIndexForChar(message[j], alphabet) 									@label(\"kSet\") \n"+
		"    encryptedMessage[j] = shiftedAlphabet[k]										@label(\"encMsg\") \n"+
		"  done																				@label(\"doneEnc\") \n";

		return annotatedCode;
	}

	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {

		ap = (ArrayProperties)arg0.getPropertiesByName("arrayProps");
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		scp = (SourceCodeProperties) arg0.getPropertiesByName("sourceCodeProps");
		shift = (Integer) arg1.get("Shift");
		message = (String) arg1.get("Message");

		myInit();
		phase1();
		phase2();
		lang.nextStep("End of Encryption");

		return lang.toString();
	}

	@Override
       public void init() {
	    super.init();
	    lang = new AnimalScript("Caesar Cipher Animation", "Thorsten Franzel", 640, 480);
	    lang.setStepMode(true);
	}

	public void myInit() {

		Text header = lang.newText(new Coordinates(20, 50), "Caesar Chiffre", "title", null);
		header.setFont(new Font("SansSerif", Font.BOLD, 34), null, null);

		charsArray = new String[]{"A","B","C","D","E","F","G","H","I ","J ","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","  "};
		charsArrayShifted = new String[charsArray.length];

		sourceCode = lang.newSourceCode(new Coordinates(500, 200), "sourceCode", null, scp);

		parse();
	}

	void phase1() {
		timing = new TicksTiming(40);
		msgEnc = new String[message.length()];

		for(int i=0;i<charsArray.length; i++){
			charsArrayShifted[i] = "  ";
		}

		for(int i=0;i<message.length(); i++){
			msgEnc[i] = "  ";
		}

		sab = lang.newStringArray(new Coordinates(20, 200), str2strArray(message), "message", null, ap);
		exec("procedure");
		lang.nextStep("Loading of the message to be encrypted");
		exec("alphabet");
		abc = lang.newStringArray(new Coordinates(20, 250), charsArray, "abc", null, ap);
		lang.nextStep();

		lang.newText(new Coordinates(20, 300), "shift="+String.valueOf(shift), "shift", null);
		lang.nextStep("Shifting of the alphabet");

		abcs = lang.newStringArray(new Coordinates(20, 350), charsArrayShifted, "abcs", null, ap);

		exec("forShift");
		for(int i=0;i<charsArray.length; i++){

			exec("shiftedAssign");
			charsArrayShifted[i] = charsArray[(i+shift)%charsArray.length];
			abcs = lang.newStringArray(new Coordinates(20, 350), charsArrayShifted, "abcs", null, ap);
			abc.highlightCell((i+shift)%charsArray.length, null, null);
			abcs.highlightCell(i, null, null);
			lang.nextStep();

			exec("forShift");
			lang.nextStep();

			abc.unhighlightCell((i+shift)%charsArray.length, null, null);
			abcs.unhighlightCell(i, null, null);
		}
		exec("doneShift");
	}

	private void phase2() {
		lang.nextStep("Encoding of the Message");

		String[] messageArray = str2strArray(message);
		saa = lang.newStringArray(new Coordinates(20, 400), msgEnc, "encrypted", null, ap);		

		for(int i=0; i < messageArray.length; i++){
			exec("forEnc");
			lang.nextStep();
			for(int j=0; j < charsArray.length; j++){
				if(messageArray[i].equals(charsArray[j])){
					exec("kSet");
					sab.highlightCell(i, null, null);
					lang.nextStep();
					abc.highlightCell(j, null, null);
					lang.nextStep();
					exec("encMsg");
					abcs.highlightCell(j, null, null);
					lang.nextStep();
					msgEnc[i] = charsArrayShifted[j];

					saa = lang.newStringArray(new Coordinates(20, 400), msgEnc, "encrypted", null, ap);
					saa.highlightCell(i, null, null);
					lang.nextStep();

					sab.unhighlightCell(i, null, null);
					abc.unhighlightCell(j, null, null);
					abcs.unhighlightCell(j, null, null);
					saa.unhighlightCell(i, null, null);
				}
			}
		}
		exec("doneEnc");
	}

	private String[] str2strArray(String m){
		String m2 = m;
    m2 = m2.toUpperCase();
		String[] tmp = new String[m2.length()];

		for(int i=0; i<m2.length(); i++){
			if(m2.substring(i, i+1).equals("I") || m2.substring(i, i+1).equals("J") || m2.substring(i, i+1).equals(" "))
				tmp[i] = m2.substring(i, i+1)+" ";
			else 
				tmp[i] = m2.substring(i, i+1);
		}

		return tmp;
	}

	@Override
	public String getAlgorithmName() {
		return "Caesar Cipher";
	}

	@Override
	public String getAnimationAuthor() {
		return "Thorsten Franzel";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return "Caesar cipher is a monoalphabetic substitution cipher. It was used in the Roman Empire "+
		"by Caesar to communicate with his generals. Its easily breakable with a statistical approach "+
		"and demonstrates the need for sophisticated cipher algorithms,";
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
		return "Caesar Cipher";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
}
