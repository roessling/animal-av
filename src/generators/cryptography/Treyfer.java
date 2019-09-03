/*
 * Treyfer.java
 * Soundes Marzougui, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel; 
/*
 * Treyfer.java
 * Soundes Marzougui, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */ 


public class Treyfer implements ValidatingGenerator {
	private static Language l;

	String Description = "Treyfer is a block MAC Algorithm. Treyfer has a  small key size and block size of 64 bits each."
			+"\n"
			+"The Treyfer Algorithm needs near to the encryption key a constant called SBox."
			+"\n"
			+"The constant SBox could be defined by the devoloper or left."
			+"\n"
			+"Treyfer is known as a simple algorithm susceptible to side attacks despite the number of rounds (32)"
			+"\n"
			+"it excecutes."
			+"Moreover, Treyfer is a deterministic algorithm, that means the encryption of the same text with the "
			+"same key will lead always to the same cipher text.";

	String Code = "void treyfer_encrypt(int text[8], int const key[8])"
			+"\n"
			+"{"
			+"\n"
			+"    int  i;"
			+"\n"
			+"    int t = text[0];"
			+"\n"
			+"    for (i = 0; i < 8*NUMROUNDS; i++) {"
			+"\n"
			+"        t += key[i%8];"
			+"\n"
			+"        t = Sbox[t] + text[(i+1)%8];"
			+"\n"
			+"        text[(i+1) % 8] = t = (t << 1) | (t >> 7);        /* Rotate left 1 bit */"
			+"\n"
			+"    }"
			+"\n"
			+"}";


	//Init method
	public void init(){
		l = new AnimalScript("Treyfer Encryption Algorithm ", "Soundes Marzougui", 800, 600);
	}

	//Generate method
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {

		SourceCodeProperties Code = (SourceCodeProperties)props.getPropertiesByName("sourceCode"); 
		String Text = (String)primitives.get("Text");
		int[] Key = (int[])primitives.get("Key"); 

		//call init methode
		init(); 
		l.setStepMode(true);

		//Set the properties of the Title 
		SourceCodeProperties Title = new SourceCodeProperties();  
		Title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 22));  
		SourceCode AnimTitle = l.newSourceCode(new Coordinates(350, 10),
				"sourceCode", null, Title); 
		AnimTitle.addCodeLine("Treyfer Encryption Algorithm",null, 0, null);
		AnimTitle.addCodeLine(" ",null, 0, null);
		AnimTitle.addCodeLine(" ",null, 0, null);
		AnimTitle.show();

		l.nextStep("Title"); 
		//Description of the algorithm 
		SourceCodeProperties CodeDescription = new SourceCodeProperties(); 
		CodeDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20)); 
		SourceCode AnimDescription = l.newSourceCode(new Coordinates(20, 40),
				"sourceCode", null, CodeDescription);  
		AnimDescription.addCodeLine("Treyfer is a block MAC Algorithm. Treyfer has a" ,null, 0, null);
		AnimDescription.addCodeLine("small key size and block size of 64 bits each." ,null, 0, null); 
		AnimDescription.addCodeLine("The Treyfer Algorithm needs near to the ",null, 0, null);
		AnimDescription.addCodeLine("encryption key a constant called SBox.",null, 0, null); 
		AnimDescription.addCodeLine("The constant SBox could be defined by ",null, 0, null);
		AnimDescription.addCodeLine("the devoloper or left.",null, 0, null);
		AnimDescription.addCodeLine("Treyfer is known as a simple algorithm susceptible ",null, 0, null);
		AnimDescription.addCodeLine("to side attacks despite the number of rounds (32)",null, 0, null); 
		AnimDescription.addCodeLine("Moreover, Treyfer is a deterministic algorithm, that",null, 0, null);
		AnimDescription.addCodeLine(" means the encryption of the same text with the ",null, 0, null); 
		AnimDescription.addCodeLine("same key will lead always to the same cipher text.",null, 0, null); 

		l.nextStep();
		AnimDescription.hide(); 
		l.nextStep("Description of the algorithm");
		
		//Set The properties of the Source code
		Code.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		Code.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20)); 
		Code.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED); 
		SourceCode sc1 = l.newSourceCode(new Coordinates(350, 70),
				"sourceCode", null, Code); 

		/////////////////////////:
		// Write the code on the right of the animation
		////////////////////////// 

		sc1.addCodeLine("  ",null, 0, null);
		sc1.addCodeLine("void treyfer_encrypt(int text[8], int  key[8]){", null, 0, null);

		sc1.addCodeLine("  int i;", null, 0, null);
		sc1.addCodeLine("  int t = text[0];",null, 0, null);
		sc1.addCodeLine("  int Sbox = 50;",null, 0, null);
		sc1.addCodeLine("  for (i = 0; i < 8; i++) {" ,null, 0, null); 
		sc1.addCodeLine("      t += key[i%8];",null, 0, null);
		sc1.addCodeLine("      t = Sbox + text[(i+1)%8];",null, 0, null);
		sc1.addCodeLine("      text[(i+1) % 8] = t = (t << 1) | (t >> 7);  ",null, 0, null);
		sc1.addCodeLine("   }",null, 0, null);
		sc1.addCodeLine("}",null, 0, null);
		l.nextStep("Code Source");




		//First Question
		l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		FillInBlanksQuestionModel BlockSize =
				new FillInBlanksQuestionModel("Block size");
		BlockSize.setPrompt("What is the block size of Treyfer Algorithm");
		BlockSize.addAnswer("64 bits", 1, "Treyfer has a small key size and block size of 64 bits each. ");
		l.addFIBQuestion(BlockSize);  
		l.finalizeGeneration();
		l.nextStep();


		//Second Question
		FillInBlanksQuestionModel attack =
				new FillInBlanksQuestionModel("attack");
		attack.setPrompt("What is the most known attack against Treyfer Encryption?");
		attack.addAnswer("side attack", 1, "Treyfer is susceptible to side attack because of his simplicity and small size of the key.");
		l.addFIBQuestion(attack);  
		l.finalizeGeneration();
		l.nextStep();

		//Second Question
		FillInBlanksQuestionModel sbox =
				new FillInBlanksQuestionModel("sbox");
		sbox.setPrompt("Is the Sbox a constant or a variable?");
		sbox.addAnswer("constant", 1, "sbox is a constant used during the encryption. It could be initialized by the user or left");
		l.addFIBQuestion(sbox);  
		l.finalizeGeneration();
		l.nextStep();


		l.nextStep("Questions Part");
		
		// The Table T contains the ASCII of the plaintext 
		int[] T = initialize(Text);

		//The table S contains the characters of the plaintext
		String[] S = initializeS(Text);

		//Now we construct the four tables that are going to be printed on the animation board
		//Thes are the plaintext table , the 	ASCI of the plaintext , the Key and at the end the encrypted text


		////////////////
		//The Plaintext Table with its name below is printed 
		///////////// 
		StringArray TextAr = l.newStringArray(new Coordinates(20, 20), S, "text", null); 
		List<String> codeText        = new LinkedList<String>();
		codeText.add("Text to encypt");  
		InfoBox codeBoxText = new InfoBox(l, new Coordinates(10, 50), 1 , "");  
		codeBoxText.setText(codeText);
		codeBoxText.show();  
		////////////////
		//The ASCII of the Plaintext Table with its name below is printed 
		///////////// 
		IntArray ASCIIAr = l.newIntArray(new Coordinates(20, 100), T, "text", null);
		List<String> codeTextA        = new LinkedList<String>();
		codeTextA.add("ASCII of the text to Encrypt");  
		InfoBox codeBoxTextA = new InfoBox(l, new Coordinates(10, 130), 1 , ""); 
		codeBoxTextA.setText(codeTextA);
		codeBoxTextA.show();  


		////////////////
		//The Key Table with its name below is printed 
		/////////////
		IntArray KeyAr = l.newIntArray(new Coordinates(20, 180), Key, "key", null); 
		//plot the word key beside the array 
		List<String> codeKey        = new LinkedList<String>();
		codeKey.clear();
		codeKey.add("Key");  
		InfoBox codeBoxKey = new InfoBox(l, new Coordinates(10,210 ), 1 , ""); 
		codeBoxKey.setText(codeKey);
		codeBoxKey.show();   

		//Now we create the table Crypt that will contain the encrypted values, The function 
		//CalculateCrypt will calculate the encrypted value
		int[] Crypt = new int[8] ; 
		Crypt = CalculateCrypt(T, Key);


		////////////////
		//The Encrypted Table with its name below is printed 
		/////////////
		String[] SResult = GetStringFromInt(Crypt);
		StringArray CryptAr = l.newStringArray(new Coordinates(20, 260), SResult, "Encrypted values", null);
		List<String> codeCrypt        = new LinkedList<String>();
		codeCrypt.clear();
		codeCrypt.add("Encrypted Text");  
		InfoBox codeBoxCrypt = new InfoBox(l, new Coordinates(20, 290), 1 , ""); 
		codeBoxCrypt.setText(codeCrypt);
		codeBoxCrypt.show(); 








		//The following presents the animation of adding the new computed values of the three
		// tables in parallel. 
		////////////////////
		// Animation of the Four Tables 
		////////////////////////


		Timing t0 = new MsTiming(0); 

		sc1.highlight(0);
		sc1.unhighlight(0);
		l.nextStep();
		sc1.highlight(1);
		sc1.unhighlight(1);
		l.nextStep();
		sc1.highlight(2);
		sc1.unhighlight(2);
		l.nextStep();
		sc1.highlight(3);
		sc1.unhighlight(3);
		l.nextStep();
		sc1.highlight(4);
		sc1.unhighlight(4);
		l.nextStep();
		sc1.highlight(5);
		sc1.unhighlight(5);
		l.nextStep();


		TextAr.setFillColor(0, Color.red, t0, t0);
		ASCIIAr.setFillColor(0, Color.green, t0, t0);
		KeyAr.setFillColor(0, Color.BLUE, t0, t0);
		l.nextStep();

		sc1.highlight(6); 
		l.nextStep();
		sc1.unhighlight(6);
		sc1.highlight(7);
		l.nextStep();
		sc1.unhighlight(7);
		sc1.highlight(8);
		l.nextStep();

		CryptAr.setFillColor(0, Color.YELLOW, t0, t0);
		l.nextStep();
		sc1.unhighlight(8);

		l.nextStep();



		TextAr.setFillColor(1, Color.red, t0, t0);
		ASCIIAr.setFillColor(1, Color.green, t0, t0);
		KeyAr.setFillColor(1, Color.BLUE, t0, t0); 
		l.nextStep();

		sc1.highlight(6); 
		l.nextStep();
		sc1.unhighlight(6);
		sc1.highlight(7);
		l.nextStep();
		sc1.unhighlight(7);
		sc1.highlight(8);
		l.nextStep();
		CryptAr.setFillColor(1, Color.YELLOW, t0, t0);
		l.nextStep();
		sc1.unhighlight(8);

		l.nextStep();




		l.nextStep();
		TextAr.setFillColor(2, Color.red, t0, t0);
		ASCIIAr.setFillColor(2, Color.green, t0, t0);
		KeyAr.setFillColor(2, Color.BLUE, t0, t0);
		l.nextStep();

		sc1.highlight(6); 
		l.nextStep();
		sc1.unhighlight(6);
		sc1.highlight(7);
		l.nextStep();
		sc1.unhighlight(7);
		sc1.highlight(8);
		l.nextStep();
		CryptAr.setFillColor(2, Color.YELLOW, t0, t0);
		l.nextStep();
		sc1.unhighlight(8);

		l.nextStep();


		l.nextStep();
		KeyAr.setFillColor(3, Color.BLUE, t0, t0);
		ASCIIAr.setFillColor(3, Color.green, t0, t0);
		TextAr.setFillColor(3, Color.red, t0, t0); 
		l.nextStep();

		sc1.highlight(6); 
		l.nextStep();
		sc1.unhighlight(6);
		sc1.highlight(7);
		l.nextStep();
		sc1.unhighlight(7);
		sc1.highlight(8);
		l.nextStep();
		CryptAr.setFillColor(3, Color.YELLOW, t0, t0);
		l.nextStep();
		sc1.unhighlight(8);

		l.nextStep();




		l.nextStep();
		TextAr.setFillColor(4, Color.red, t0, t0);
		ASCIIAr.setFillColor(4, Color.green, t0, t0);
		KeyAr.setFillColor(4, Color.BLUE, t0, t0); 
		l.nextStep();

		sc1.highlight(6); 
		l.nextStep();
		sc1.unhighlight(6);
		sc1.highlight(7);
		l.nextStep();
		sc1.unhighlight(7);
		sc1.highlight(8);
		l.nextStep();
		CryptAr.setFillColor(4, Color.YELLOW, t0, t0);
		l.nextStep();
		sc1.unhighlight(8);

		l.nextStep();

		TextAr.setFillColor(5, Color.red, t0, t0);
		ASCIIAr.setFillColor(5, Color.green, t0, t0);
		KeyAr.setFillColor(5, Color.BLUE, t0, t0);
		l.nextStep();

		sc1.highlight(6); 
		l.nextStep();
		sc1.unhighlight(6);
		sc1.highlight(7);
		l.nextStep();
		sc1.unhighlight(7);
		sc1.highlight(8);
		l.nextStep();
		CryptAr.setFillColor(5, Color.YELLOW, t0, t0);
		l.nextStep();
		sc1.unhighlight(8);

		l.nextStep();




		l.nextStep();
		TextAr.setFillColor(6, Color.red, t0, t0);
		ASCIIAr.setFillColor(6, Color.green, t0, t0);
		KeyAr.setFillColor(6, Color.BLUE, t0, t0); 
		l.nextStep();
		sc1.highlight(6); 
		l.nextStep();
		sc1.unhighlight(6);
		sc1.highlight(7);
		l.nextStep();
		sc1.unhighlight(7);
		sc1.highlight(8);
		l.nextStep();
		CryptAr.setFillColor(6, Color.YELLOW, t0, t0);
		l.nextStep();
		sc1.unhighlight(8);

		l.nextStep();


		TextAr.setFillColor(7, Color.red, t0, t0);
		ASCIIAr.setFillColor(7, Color.green, t0, t0);
		KeyAr.setFillColor(7, Color.BLUE, t0, t0); 
		l.nextStep();

		sc1.highlight(6); 
		l.nextStep();
		sc1.unhighlight(6);
		sc1.highlight(7);
		l.nextStep();
		sc1.unhighlight(7);
		sc1.highlight(8);
		l.nextStep();
		CryptAr.setFillColor(7, Color.YELLOW, t0, t0);
		l.nextStep();
		sc1.unhighlight(8);
 
		l.nextStep("Execution of the algorithm");




		
		//print the input text and the encrypted text as results
		//Description of the algorithm 
		SourceCodeProperties CodeConc = new SourceCodeProperties(); 
		CodeConc.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20)); 
		CodeConc.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN); 
		SourceCode AnimConclusion = l.newSourceCode(new Coordinates(700, 350),
				"sourceCode", null, CodeConc); 
		AnimConclusion.addCodeLine("PlainText: "+ Text,null, 0, null);
		AnimConclusion.addCodeLine("Encrypted Text: "+ SResult[0]+ SResult[1]+ SResult[2]+ SResult[3]+ SResult[4]+ SResult[5]+ SResult[6]+ SResult[7],null, 0, null);
		AnimConclusion.show();
		l.nextStep("Results");

		return l.toString();
	}

	public String getName() {
		return "Treyfer Encryption Algorithm ";
	}

	public String getAlgorithmName() {
		return "Treyfer";
	}

	public String getAnimationAuthor() {
		return "Soundes Marzougui";
	}

	public String getDescription(){
		return Description;

	}

	public String getCodeExample(){
		return Code; 
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


	//Transform The text to an array of integer containing the ASCII of each character
	//The Text should not have more than 8 characters, otherwise only the first 8 characters will be considered.
	public static int[] initialize(String Text)
	{
		int[] T  = new int[8];
		char space = ' ';
		for(int i = 0; i<8; i++)
		{
			if(i >= Text.length()) T[i] = (int)space;
			else T[i] = (int) Text.charAt(i);
		} 


		return T;
	}

	//Transform the values given by the encryption algorithm to a string table.
	public static String[] GetStringFromInt(int[] Crypt)
	{
		String[] S = new String[8];
		for(int i = 0; i<8; i++)
		{
			S[i] = Character.toString( (char)(Crypt[i]%127)); 
		}  
		return S;
	}

	//convert the Plaintext to a chain of string.
	public static String[] initializeS(String Text)
	{
		String[] S = new String[8];

		for(int i = 0; i<8; i++)
		{
			if(i >= Text.length()) S[i] = " ";
			else S[i] = Character.toString(Text.charAt(i)); 
		} 

		return S;
	}

	//Run the encryption algorithm. The sbox is fixed to a constant
	public static int[] CalculateCrypt(int [] text, int[] key)
	{
		int i;
		int sbox = 50; 
		int t = text[0];
		for (i = 0; i < 8; i++) {
			t += key[i%8];
			t =  sbox + text[(i+1)%8];
			text[(i+1) % 8] = t = (t << 1) | (t >> 7);        /* Rotate left 1 bit */
		}

		return text;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		String Text = (String)primitives.get("Text");
		if(Text.length()>8)
		{	try {
			throw new IllegalArgumentException("Text size exceeded 8");
		} catch (IllegalArgumentException e) { 
			e.printStackTrace();
			return false;
		}
		}
		return true;
	}


}