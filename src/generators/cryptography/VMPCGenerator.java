/*
 * vmpcgenerator.java
 * Philipp Geiger, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.variables.Variable;
import animal.main.Animal;
import animal.variables.VariableRoles;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;

public class VMPCGenerator implements ValidatingGenerator {
	private Language lang;
	private int outputLength;
	private int[] initVector;
	private int[] key;
	private String[] test;
	
	public static void main(String[] args) {
		Generator generator = new VMPCGenerator(); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

	public void init(){
		lang = new AnimalScript("VMPCcipher", "Philipp Geiger", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		outputLength = (Integer)primitives.get("outputLength");
		initVector = (int[])primitives.get("initVector");
		key = (int[])primitives.get("key");

		ksa(key, initVector, outputLength); 
		return lang.toString();
	}

	public String getName() {
		return "VMPCcipher";
	}

	public String getAlgorithmName() {
		return "Variably Modified Permutation Composition Stream Cipher";
	}

	public String getAnimationAuthor() {
		return "Philipp Geiger";
	}

	public String getDescription(){
		return "Variably Modified Permutation Composition (short: VMPC) is a stream cipher"
				+"\n"
				+"designed by Bartosz Zoltak, based on the VMPC one-way function. "
				+"\n"
				+" "
				+"\n"
				+" The VMPC stream cipher is a combination of the VMPC Key Scheduling Algorithm (short: KSA)"
				+"\n"
				+" and the VMPC Internal State. The algorithm generates a stream of 8-bit values, from the"
				+"\n"
				+" internal state using a 256-byte permutation and two 8-bit integer variables. The initial values"
				+"\n"
				+"are determined by the KSA."
				+"\n"
				+" "
				+"\n"
				+" To encrypt, the generated values and the plaintext have to be xored.";
	}

	public String getCodeExample(){
		return "VMPC-KSA:"
				+"\n"
				+"for (int i = 0; i != 256; i++) {"
				+"\n"
				+"    P[i] = i;"
				+"\n"
				+"}"
				+"\n"
				+"\n"
				+"for (int m = 0; m != 768; m++) {"
				+"\n"
				+"    n = m % 256;"
				+"\n"
				+"    s = P[(s + P[n] + K[m % keyLength]) % 256];"
				+"\n"
				+"    Swap: P[n] mit P[s%256];"
				+"\n"
				+"}"
				+"\n"
				+"\n"
				+"for (int m = 0; m != 768; m++) {"
				+"\n"
				+"    n = m % 256;"
				+"\n"
				+"    s = P[(s + P[n] + K[m % initVectorLength]) % 256];"
				+"\n"
				+"    Swap: P[n] mit P[s%256];"
				+"\n"
				+"}"
				+"\n"
				+"\n"
				+"VMPC-Internal State:"
				+"\n"
				+"for (int i = 0; i != outLength; i++) {"
				+"\n"
				+"    s = P[(s + P[n]) % 256];"
				+"\n"
				+"    out[i] = P[(P[P[s]] + 1) % 256];"
				+"\n"
				+"    Swap: P[n] mit P[s%256];"
				+"\n"
				+"    n = (n + 1) % 256;"
				+"\n"
				+"}";
	}

	public String getFileExtension(){
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	
	public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives)throws IllegalArgumentException {
		if((Integer)primitives.get("outputLength")<0){
			return false;
		}
	return true;
	}

	static String[] INTRO_TEXT = {"Variably Modified Permutation Composition Stream Cipher(short: VMPC),",
			"is a stream cipher designed by Bartosz Zoltak, ",
			"based on the VMPC one-way function. ",
			"The VMPC stream cipher is a combination of the VMPC Key Scheduling Algorithm",
			"and the VMPC Internal State. The algorithm generates a stream",
			"of 8-bit values, from the internal state using a 256-byte permuatation",
			"and two 8-bit integer variables. The initial values are determined by the KSA.",
			"To encrypt, the generated values and the plaintext should be xored.",
			"",
			"Animation by Philipp Geiger, SoSe2019."
	};
	static String[] OUTRO_TEXT = {"The VMPC stream cipher successfully generated a stream of 8-bit values.", 
			"",
			"For this, the key and the initialization vector have been set.",
			"Then the VMPC Key Scheduling Algorithm generated the internal values",
			"via permuting the permutation array using the initialization vector",
			"and the encryption key.",
			"",
			"To compute the interntal state, the algorithm used the computed values",
			"and successfully set the 8-bit stream.",
			"The output stream is now ready to encrypt, by xoring the plaintext",
			"with the 8-bit stream."

	};

	static String[] DESCRIPTION_TEXT = {"First, the permutation array is initialized with",
			"integers from 0 to 255 in ascending order.",
			"",
			"Then the integer variables n and s are initialized",
			"using the encryption key. After that elements are swapped based those values.",
			"",
			"The third for-loop is analogue to the previous. Instead of the key",
			"the initialization vector is used.",
			"",
			"The computation of the internal state is step-by-step animated.",
			"For most parts it is analogue to the previous loops.",

	};


	public void ksa(int[] K, int[] V, int outputLength){

		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		String keyLabel = "Key:";
		String initVecLabel = "Init. Vector:";
		String permuLabel = "Permutation Array:";
		String outputLabel = "Internal State (output):";

		/**
		 * counter
		 */
		TwoValueCounter counter;
		TwoValueView cv;
		
		/**
		 * Question-time
		 */
		
		MultipleChoiceQuestionModel question0 = new MultipleChoiceQuestionModel("assignments");
		question0.setPrompt("Which of the four  is not variable in its length?");
		question0.addAnswer("Permutation-Array", 1, "Correct! The permutation array is not variable in its length. It always has to have 256 1-byte elements. ");
		question0.addAnswer("Key-Array", 0, "Wrong. As for the init. vector, you can choose initialization vectors of different lengths.");
		question0.addAnswer("Init. Vector-Array", 0, "Wrong. As for the key, you can choose initialization vectors of different lengths.");
		question0.addAnswer("Output-Array", 0, "Wrong. The output is a stream, with variable length.");
		
		MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("assignments1");
		question.setPrompt("What does output[] contain?");
		question.addAnswer("Stream of 8-bit integer values", 1, "Correct!");
		question.addAnswer("Stream of 16-bit integer values", 0, "Wrong.");
		question.addAnswer("Stream of 1-byte integer values", 1, "Correct!");
		question.addAnswer("String[] filled with integer values", 0, "Wrong.");
		
		
		
		
		
		int s = 0;
		int n = 0;
		int c = key.length;
		int z = initVector.length;
		int[] out = new int[outputLength]; 
		int[] P = new int[256];
		
		/**
		Variables var;
		var = lang.newVariables();
		var.declare("int", "s", "0");
		var.setRole("s",);
		**/
		
		ArrayProperties ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);


		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font("Monospaced", Font.PLAIN, 16));
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		lang.newText(new Coordinates(20, 10), "Variably Modified Permutation Composition (Stream Cipher)", "header", null, tp);
		

		/**
		 * intro/outro properties
		 */
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode intro = lang.newSourceCode(new Coordinates(20, 40), "intro", null, scProps);
		for(int i = 0; i < INTRO_TEXT.length; i++){
			intro.addCodeLine(INTRO_TEXT[i], null, 0, null);
		}
		lang.nextStep();
		intro.hide();
		lang.nextStep();
		
		/**
		 * Source-code init
		 */
		
		SourceCode sc0;
		SourceCode sc;
		SourceCode sc2;
		SourceCode sc3;
		SourceCode sc4;

		//erstellen und befuellen des source codes
		sc0 = lang.newSourceCode(new Coordinates(40, 40), "sourceCode", null, scProps);
		sc = lang.newSourceCode(new Coordinates(500, 50), "sourceCode", null, scProps);
		sc2 = lang.newSourceCode(new Coordinates(500, 50), "sourceCode", null, scProps);
		sc3 = lang.newSourceCode(new Coordinates(500, 50), "sourceCode", null, scProps);
		sc4 = lang.newSourceCode(new Coordinates(500, 50), "sourceCode", null, scProps);

		sc0.addCodeLine("VMPC-KSA:", null, 0, null);
		sc0.addCodeLine("for (int i = 0; i != 256; i++) ", null, 0, null);
		sc0.addCodeLine("{ ", null, 0, null);
		sc0.addCodeLine("P[i] = i;", null, 1, null);
		sc0.addCodeLine("} ", null, 0, null);
		sc0.addCodeLine("for (int m = 0; m != 768; m++) ", null, 0, null);
		sc0.addCodeLine("{ ", null, 0, null);
		sc0.addCodeLine("n = m % 256;", null, 1, null);
		sc0.addCodeLine("s = P[(s + P[n] + K[m % keyLength]) % 256];", null, 1, null);
		sc0.addCodeLine("Swap: P[n] mit P[s%256]", null, 1, null);
		sc0.addCodeLine("} ", null, 0, null);
		sc0.addCodeLine("for (int m = 0; m != 768; m++)", null, 0, null);
		sc0.addCodeLine("{ ", null, 0, null);
		sc0.addCodeLine("n = m % 256; ", null, 1, null);
		sc0.addCodeLine("s = P[(s + P[n] + V[m % initVectorLength]) % 256]; ", null, 1, null);
		sc0.addCodeLine("Swap: P[n] mit P[s%256]", null, 1, null);
		sc0.addCodeLine("} ", null, 0, null);
		sc0.addCodeLine("VMPC-Internal State:", null, 0, null);
		sc0.addCodeLine("for (int i = 0; i != outLength; i++)", null, 0, null);
		sc0.addCodeLine("{ ", null, 0, null);
		sc0.addCodeLine("s = P[(s + P[n]) % 256] ", null, 1, null);
		sc0.addCodeLine("out[i] = P[(P[P[s]] + 1) % 256]; ", null, 1, null);
		sc0.addCodeLine("Swap: P[n] mit P[s%256]", null, 1, null);
		sc0.addCodeLine("n = (n + 1) % 256;", null, 1, null);
		sc0.addCodeLine("}", null, 0, null);
		lang.nextStep("Complete sourcecode");

		SourceCode description = lang.newSourceCode(new Coordinates(420, 40), "description", null, scProps);


		description.addCodeLine(DESCRIPTION_TEXT[0], null, 0, null);
		description.addCodeLine(DESCRIPTION_TEXT[1], null, 0, null);
		sc0.highlight(1);
		sc0.highlight(2);
		sc0.highlight(3);
		sc0.highlight(4);
		lang.nextStep();
		sc0.unhighlight(1);
		sc0.unhighlight(2);
		sc0.unhighlight(3);
		sc0.unhighlight(4);
		description.addCodeLine(DESCRIPTION_TEXT[2], null, 0, null);
		description.addCodeLine(DESCRIPTION_TEXT[3], null, 0, null);
		description.addCodeLine(DESCRIPTION_TEXT[4], null, 0, null);
		sc0.highlight(5);
		sc0.highlight(6);
		sc0.highlight(7);
		sc0.highlight(8);
		sc0.highlight(9);
		sc0.highlight(10);
		lang.nextStep();
		sc0.unhighlight(5);
		sc0.unhighlight(6);
		sc0.unhighlight(7);
		sc0.unhighlight(8);
		sc0.unhighlight(9);
		sc0.unhighlight(10);
		description.addCodeLine(DESCRIPTION_TEXT[5], null, 0, null);
		description.addCodeLine(DESCRIPTION_TEXT[6], null, 0, null);
		description.addCodeLine(DESCRIPTION_TEXT[7], null, 0, null);
		sc0.highlight(11);
		sc0.highlight(12);
		sc0.highlight(13);
		sc0.highlight(14);
		sc0.highlight(15);
		sc0.highlight(16);
		lang.nextStep();
		sc0.unhighlight(11);
		sc0.unhighlight(12);
		sc0.unhighlight(13);
		sc0.unhighlight(14);
		sc0.unhighlight(15);
		sc0.unhighlight(16);
		description.addCodeLine(DESCRIPTION_TEXT[8], null, 0, null);
		description.addCodeLine(DESCRIPTION_TEXT[9], null, 0, null);
		description.addCodeLine(DESCRIPTION_TEXT[10], null, 0, null);
		sc0.highlight(17);
		sc0.highlight(18);
		sc0.highlight(19);
		sc0.highlight(20);
		sc0.highlight(21);
		sc0.highlight(22);
		sc0.highlight(23);
		sc0.highlight(24);
		lang.nextStep();


		sc0.hide();
		description.hide();

		/**
		 * initialization of the arrays and the array labels
		 */
		lang.newText(new Coordinates(20, 45), "Key:", "keyLabel", null, tp);
		IntArray keyArray = lang.newIntArray(new Coordinates(20,66), K, "KeyArray", null, ap);	
		lang.nextStep();
		lang.newText(new Coordinates(20, 125), "Initialization Vector:", "InitVecLabel", null, tp);
		IntArray initVector = lang.newIntArray(new Coordinates(20,146), V, "InitVectorArray", null, ap);
		lang.nextStep();
		lang.newText(new Coordinates(20, 205), "Permutation Array:", "permuLabel", null, tp);
		IntArray permutation = lang.newIntArray(new Coordinates(20,226), P, "PermutationArray", null, ap);
		lang.nextStep();
		lang.newText(new Coordinates(20, 285), "Internal State (output):", "outputLabel", null, tp);
		IntArray output = lang.newIntArray(new Coordinates(20,306), out, "OutputStream", null, ap);
		lang.nextStep("KSA: Initialization");
		
		lang.addMCQuestion(question0);

		sc.addCodeLine("VMPC-KSA:", null, 0, null);
		sc.addCodeLine("for (int i = 0; i != 256; i++) ", null, 0, null);
		sc.addCodeLine("{ ", null, 0, null);
		sc.addCodeLine("P[i] = i;", null, 0, null);
		sc.addCodeLine("} ", null, 0, null);
		lang.nextStep();

		/**
		 * beginning of ksa
		 */
		int temp;
		for (int i = 0; i != 256; i++) {
			P[i] = i;
			permutation.put(i, i, null, null);
			sc.highlight(3);
			//permutation.highlightElem(i, null, null);
			//permutation.unhighlightElem(i, null, null);
		}

		
		lang.nextStep();
		sc.hide();
		sc2.addCodeLine("for (int m = 0; m != 768; m++) ", null, 0, null);
		sc2.addCodeLine("{ ", null, 0, null);
		sc2.addCodeLine("n = m % 256;", null, 1, null);
		sc2.addCodeLine("s = P[(s + P[n] + K[m % keyLength]) % 256];", null, 1, null);
		sc2.addCodeLine("Swap: P[n] mit P[s%256]", null, 1, null);
		sc2.addCodeLine("} ", null, 0, null);
		lang.nextStep("KSA: Step 2");

		for (int m = 0; m != 768; m++) {
			int s2 = s;
			int n2 = n;
			n = m % 256;
			n2 = n;
			s = P[(s + P[n] + K[m % c]) % 256];
			s2 = permutation.getData((s2+permutation.getData(n2)+keyArray.getData(m%c))%256);
			temp = P[n];
			P[n] = P[s % 256];
			P[s % 256] = temp;
			permutation.swap(permutation.getData(n2), permutation.getData(s2%256), null, null);
		}

		lang.nextStep();
		sc2.hide();
		sc3.addCodeLine("for (int m = 0; m != 768; m++)", null, 0, null);
		sc3.addCodeLine("{ ", null, 0, null);
		sc3.addCodeLine("n = m % 256; ", null, 1, null);
		sc3.addCodeLine("s = P[(s + P[n] + V[m % initVecLength]) % 256]; ", null, 1, null);
		sc3.addCodeLine("Swap: P[n] mit P[s%256]", null, 1, null);
		sc3.addCodeLine("} ", null, 0, null);
		lang.nextStep("KSA: Step 3");

		for (int m = 0; m != 768; m++) {
			int s2 = s;
			int n2 = n;

			n = m % 256;
			n2 = n;
			s = P[(s + P[n] + V[m % z]) % 256];
			s2 = permutation.getData((s2+permutation.getData(n2)+initVector.getData(m%z))%256);
			temp = P[n];
			P[n] = P[s % 256];
			P[s % 256] = temp;
			permutation.swap(permutation.getData(n2), permutation.getData(s2%256), null, null);

		}

		lang.nextStep();
		n = 0;
		sc3.hide();
		//end of the ksa
		sc4 = lang.newSourceCode(new Coordinates(500, 40), "sourceCode", null, scProps);
		sc4.addCodeLine("VMPC-Internal State:", null, 0, null);
		sc4.addCodeLine("for (int i = 0; i != outLength; i++)", null, 0, null);
		sc4.addCodeLine("{ ", null, 0, null);
		sc4.addCodeLine("s = P[(s + P[n]) % 256] ", null, 1, null);
		sc4.addCodeLine("out[i] = P[(P[P[s]] + 1) % 256]; ", null, 1, null);
		sc4.addCodeLine("Swap: P[n] mit P[s%256]", null, 1, null);
		sc4.addCodeLine("n = (n + 1) % 256;", null, 1, null);
		sc4.addCodeLine("}", null, 0, null);
		lang.nextStep("Internal State");
		
		lang.addMCQuestion(question);
		
		/**
		 * counter for the permutation array
		 */
		
		lang.newText(new Coordinates(800, 45), "Permutation array:", "outputLabel", null, tp);
		counter = lang.newCounter(permutation);
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		cp.setName("Permutation");
		
		cv = lang.newCounterView(counter, new Coordinates(802, 67), cp, true, false);
		
		//building of the output array used to encrypt
		sc4.highlight(1);
		for (int i = 0; i != outputLength; i++) {
			int s2 = s;
			int n2 = n;
			s = P[(s + P[n]) % 256];
			sc4.highlight(3);
			s2 = permutation.getData((s2+permutation.getData(n))%256);
			lang.nextStep();
			sc4.unhighlight(3);
			sc4.highlight(4);
			out[i] = P[(P[P[s]] + 1) % 256];
			output.put(i, P[(P[P[s]] + 1) % 256], null, null);
			output.highlightElem(i, null, null);
			lang.nextStep();
			output.unhighlightElem(i, null, null);
			sc4.unhighlight(4);
			sc4.highlight(5);
			temp = P[n % 256];
			P[n % 256] = P[s % 256];
			P[s % 256] = temp;
			permutation.highlightElem(n2%256, null, null);
			permutation.highlightElem(s2%256, null, null);
			lang.nextStep();
			permutation.swap(n2%256, s2%256, null, null);
			lang.nextStep();
			permutation.unhighlightElem(n2%256, null, null);
			permutation.unhighlightElem(s2%256, null, null);
			sc4.unhighlight(5);
			sc4.highlight(6);
			n = (n + 1) % 256;
			n2 = n;
			sc4.unhighlight(6);
			lang.nextStep();
			}
		lang.nextStep("Outro");
		sc4.hide();
		lang.hideAllPrimitives();

		lang.newText(new Coordinates(20, 10), "Variably Modified Permutation Composition (Stream Cipher)", "header", null, tp);
		SourceCode outro = lang.newSourceCode(new Coordinates(20, 40), "outro", null, scProps);
		for(int i = 0; i < OUTRO_TEXT.length; i++){
			outro.addCodeLine(OUTRO_TEXT[i], null, 0, null);
		}
		lang.finalizeGeneration();

	}

}