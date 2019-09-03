package generators.cryptography;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import animal.main.Animal;

/*
 * TEA.java
 * Soundes Marzougui, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */


import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;


//////////////////////////////////////////////////////////////////////
// Tea Algorithm is an encryption algorithm that takes as input V. V is 
// splitted into v0 and v1.
// a key K that corresponds to k0 k1 k2 k3 is used for the encryption
// Other variables like Delta and sum are used. These are constants.
//The user can input only the v and the k.That means the text to encrypt and the 
//key for the encryption.
//In this visualization we adress only the two cycles of Feistel that 
//constitute one iteration of the algorithm.
//the visualization is based on two schemes: a circuit and a table or list for every cycle.
//The list shows the value of the sum and the v0 or v1(That depends on the cycle). 
//The circuit is constructed via text animation, rectangle and circle animation.
//Moreover the algorithm is printed each time for a better understanding.
///////////////////////////////////////////////////////////////////////


public class TEA implements ValidatingGenerator {
	private static Language l;

	String Description = "\n"
			+"The Tiny Encryption Algorithm (TEA) is a block cipher notable for its simplicity "
			+"\n"
			+"of description and implementation, typically a few lines of code. "
			+"\n"
			+"It was designed by David Wheeler and Roger Needham of the Cambridge "
			+"\n"
			+"Computer Laboratory;"
			+"\n"
			+"TEA operates on two 32-bit unsigned integers and uses a 128-bit key. It has "
			+"\n"
			+"a Feistel structure with a suggested 32 rounds, typically implemented in pairs"
			+"\n"
			+" termed cycles.";

	String SourceCode = "\n"
			+"void encrypt (uint32_t* v, uint32_t* k) {"
			+"\n"
			+"    uint32_t v0=v[0], v1=v[1], sum=0, i;           /* set up */"
			+"\n"
			+"    uint32_t delta=0x9e3779b9;                     /* a key schedule constant */"
			+"\n"
			+"    uint32_t k0=k[0], k1=k[1], k2=k[2], k3=k[3];   /* cache key */"
			+"\n"
			+"    for (i=0; i < 32; i++) {                       /* basic cycle start */"
			+"\n"
			+"        sum += delta;"
			+"\n"
			+"        v1 += ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);"
			+"\n"
			+"        v0 += ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);"
			+"\n"
			+"    }                                              /* end cycle */"
			+"\n"
			+"    v[0]=v0; v[1]=v1;"
			+"\n"
			+"}" ;

	public void init(){

		l = new AnimalScript("Tiny Encryption Algorithm ", "Soundes Marzougui", 800, 600);  
	}


	public String getName() {
		return "Tiny Encryption Algorithm ";
	}

	public String getAlgorithmName() {
		return "TEA";
	}

	public String getAnimationAuthor() {
		return "Soundes Marzougui";
	}

	public String getDescription(){
		return Description;
	}

	public String getCodeExample(){
		return SourceCode;

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



	public static void main(String[] args) {

		Generator generator = new TEA();
		Animal.startGeneratorWindow(generator);
	}



	public void createCircle(int coord1, int coord2, PolylineProperties v11)
	{
		CircleProperties rp = new CircleProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red); 
		Circle c = l.newCircle(new Coordinates(coord1+10, coord2+10), 15, "Circ", new MsTiming(0), rp);

		l.nextStep();
		c.show(); 
		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1-5 , coord2-60 ), 8 , "");
		List<String> code        = new LinkedList<String>();
		code.clear();

		Node[] nd1 = new Node[2];
		Node[] nd2 = new Node[2];

		nd1[0] = new Coordinates(coord1+10, coord2-37);
		nd1[1] = new Coordinates(coord1+10, coord2-5); 

		nd2[0] = new Coordinates(coord1+10, coord2-35+60);
		nd2[1] = new Coordinates(coord1+10, coord2-5+68); 

		l.newPolyline(nd1, "line", null, v11);
		l.newPolyline(nd2, "line", null, v11);


		code.add(""); 
		code.add(""); 
		code.add(""); 
		code.add("XOR"); 
		codeBox.setText(code);
		codeBox.show();
		l.nextStep();
	}


	public void createV0(String v, int coord1, int coord2,PolylineProperties v11)
	{

		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1, coord2-10 ), 1, "");
		List<String> code        = new LinkedList<String>();  
		code.add(v);      
		codeBox.setText(code);
		codeBox.show(); 

		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1+20, coord2+32);
		nd1[1] = new Coordinates(coord1+20, coord2+162); 
		l.newPolyline(nd1, "line", null, v11);

	}

	public void Link(int coord1, int coord2, PolylineProperties v11)
	{ 
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1-135, coord2+80);
		nd1[1] = new Coordinates(coord1-135, coord2+100); 
		l.newPolyline(nd1, "line", null, v11);

		l.nextStep();


	}

	public void LinkHoriz(int coord1, int coord2, PolylineProperties v11)
	{ 
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1 +11 , coord2+23 );
		nd1[1] = new Coordinates(coord1 +50, coord2+23 ); 
		l.newPolyline(nd1, "line", null, v11);

		l.nextStep(); 
	}

	public void LinkHorizLast(int coord1, int coord2, PolylineProperties v11)
	{ 
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1 -20, coord2+23 );
		nd1[1] = new Coordinates(coord1 +50, coord2+23 ); 
		l.newPolyline(nd1, "line", null, v11);

		l.nextStep(); 
	}

	public void LinkHorizXor(int coord1, int coord2, PolylineProperties v11)
	{ 
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1 +18 , coord2+23 );
		nd1[1] = new Coordinates(coord1 +45, coord2+23 ); 
		l.newPolyline(nd1, "line", null, v11);

		l.nextStep(); 
	} 

	public void createV1(String v, int coord1, int coord2,PolylineProperties v11)
	{ 
		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1, coord2-10 ), 1, "");
		List<String> code        = new LinkedList<String>();  

		code.add(v);      
		codeBox.setText(code);
		codeBox.show(); 

		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1+20, coord2+32);
		nd1[1] = new Coordinates(coord1+20, coord2+170); 
		l.newPolyline(nd1, "line", null, v11);


	}


	public void createInc1(int coord1, int coord2, PolylineProperties v11)
	{ 
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.yellow);

		Rect r = l.newRect(new Coordinates(coord1, coord2),new Coordinates(coord1+50, coord2+30), "Rect", new MsTiming(0), rp); 
		r.show(); 
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1+120 , coord2+13);
		nd1[1] = new Coordinates(coord1+50 , coord2+13); 
		l.newPolyline(nd1, "line", null, v11);

		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1+10 , coord2-10 ), 2 , "");
		List<String> code        = new LinkedList<String>();
		code.clear();
		code.add("<<4"); // 0  
		codeBox.setText(code);
		codeBox.show(); 
		l.nextStep();
	}

	public void createInc2(int coord1, int coord2, PolylineProperties v11)
	{
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);
		Rect r = l.newRect(new Coordinates(coord1, coord2),new Coordinates(coord1+50, coord2+30), "Rect", new MsTiming(0), rp);
		r.show(); 
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1+120 , coord2+13);
		nd1[1] = new Coordinates(coord1+50 , coord2+13); 
		l.newPolyline(nd1, "line", null, v11);

		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1+10 , coord2-10 ), 1 , "");
		List<String> code        = new LinkedList<String>();
		code.clear();
		code.add(">>5");
		codeBox.setText(code);
		codeBox.show(); 
		l.nextStep();


	}
	public void createAnd1(String k, int coord1, int coord2, PolylineProperties v11)
	{


		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1+10 , coord2-26 ), 2 , "");
		List<String> code        = new LinkedList<String>();
		code.clear();

		code.add(k);  
		code.add("+");  
		codeBox.setText(code);
		codeBox.show(); 
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1 +30 , coord2+13);
		nd1[1] = new Coordinates(coord1 +50, coord2+13); 
		l.newPolyline(nd1, "line", null, v11);

		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray);  
		Rect r = l.newRect(new Coordinates(coord1, coord2),new Coordinates(coord1+30, coord2+30), "Carre", new MsTiming(0), rp);
		l.nextStep();
		r.show();
		l.nextStep();

	}


	public void createAnd2(String k, int coord1, int coord2,PolylineProperties v11)
	{ 


		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1 +30 , coord2+13);
		nd1[1] = new Coordinates(coord1 +50, coord2+13); 
		l.newPolyline(nd1, "line", null, v11);

		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray); 
		Rect r = l.newRect(new Coordinates(coord1, coord2),new Coordinates(coord1+30, coord2+30), "Carre", new MsTiming(0), rp);
		l.nextStep();
		r.show();

		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1+10 , coord2-26 ), 4 , "");
		List<String> code        = new LinkedList<String>();
		code.clear();
		code.add(k);  
		code.add("+");  
		codeBox.setText(code); 
		codeBox.show(); 
	}



	public void createAnd3(int coord1, int coord2)
	{ 
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray); 
		Rect r = l.newRect(new Coordinates(coord1, coord2),new Coordinates(coord1+30, coord2+30), "Carre", new MsTiming(0),rp);
		l.nextStep();
		r.show();

		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1+10 , coord2-10  ), 4 , "");
		List<String> code        = new LinkedList<String>();
		code.clear(); 
		code.add("+ "); 
		codeBox.setText(code);
		codeBox.show(); 
		l.nextStep();

	}
	public void  LinkV1(PolylineProperties v11)
	{
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(790 , 263);
		nd1[1] = new Coordinates(1070 , 330); 
		l.newPolyline(nd1, "line", null, v11);  
		l.nextStep();

	}
	public void  LinkV2(PolylineProperties v11)
	{
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(1070 , 263);
		nd1[1] = new Coordinates(790 , 330); 
		l.newPolyline(nd1, "line", null, v11);  
		l.nextStep();

	}


	public void createAndMittel(int coord1, int coord2, PolylineProperties v11)
	{
		Node[] nd1 = new Node[2];
		nd1[0] = new Coordinates(coord1+170 , coord2+13);
		nd1[1] = new Coordinates(coord1+30 , coord2+13); 
		l.newPolyline(nd1, "line", null, v11); RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray); 
		Rect r = l.newRect(new Coordinates(coord1, coord2),new Coordinates(coord1+30, coord2+30), "Carre", new MsTiming(0),rp);
		l.nextStep();
		r.show();

		InfoBox codeBox = new InfoBox(l, new Coordinates(coord1+10 , coord2-30 ), 2 , "");
		List<String> code        = new LinkedList<String>();
		code.clear(); 
		code.add("Delta");  
		code.add("+");  
		codeBox.setText(code);
		codeBox.show();  

	}

	int[] encrypt (int[]  v, int[]  k) {
		int v0=v[0], v1=v[1], sum=0, i;           
		int delta=0x9e3779b9;                     
		int k0=k[0], k1=k[1], k2=k[2], k3=k[3];   
		for (i=0; i < 32; i++) {                       
			sum += delta;
			v0 += ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);
			v1 += ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);
		}                                             
		v[0]=v0; v[1]=v1;
		return v;
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		SourceCodeProperties  Code = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		int[] v = (int[])primitives.get("v");
		PolylineProperties v00 = (PolylineProperties)props.getPropertiesByName("v0");
		int[] k = (int[])primitives.get("k");
		PolylineProperties v11 = (PolylineProperties)props.getPropertiesByName("v1");



		//call init methode
		init();  
		l.setStepMode(true); 

		//Set the properties of the Title 
		SourceCodeProperties Title = new SourceCodeProperties();  
		Title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 22));  
		SourceCode AnimTitle = l.newSourceCode(new Coordinates(350, 10),
				"sourceCode", null, Title); 
		AnimTitle.addCodeLine("Tiny Encryption Algorithm (TEA)",null, 0, null);
		AnimTitle.addCodeLine(" ",null, 0, null);
		AnimTitle.addCodeLine(" ",null, 0, null);
		AnimTitle.show();

		l.nextStep("Title"); 
		//Description of the algorithm 
		SourceCodeProperties CodeDescription = new SourceCodeProperties(); 
		CodeDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20)); 
		SourceCode AnimDescription = l.newSourceCode(new Coordinates(20, 40),
				"sourceCode", null, CodeDescription);  
		AnimDescription.addCodeLine("The Tiny Encryption Algorithm (TEA) is a block cipher notable for its simplicity " ,null, 0, null);
		AnimDescription.addCodeLine("of description and implementation, typically a few lines of code. " ,null, 0, null);
		AnimDescription.addCodeLine("It was designed by David Wheeler and Roger Needham of the Cambridge " ,null, 0, null); 
		AnimDescription.addCodeLine("Computer Laboratory" ,null, 0, null);
		AnimDescription.addCodeLine("TEA operates on two 32-bit unsigned integers and uses a 128-bit key. It has ",null, 0, null);
		AnimDescription.addCodeLine("a Feistel structure with a suggested 32 rounds, typically implemented in pairs",null, 0, null);
		AnimDescription.addCodeLine("termed cycles.",null, 0, null);

		l.nextStep();
		AnimDescription.hide(); 
		l.nextStep("Description of the algorithm");


		//Set The properties of the Source code
		Code.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		Code.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20)); 
		Code.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.ORANGE); 
		SourceCode sc1 = l.newSourceCode(new Coordinates(20, 70),
				"sourceCode", null, Code); 

		sc1.addCodeLine("void encrypt (uint32_t* v, uint32_t* k) {",null, 0, null);
		sc1.addCodeLine("    uint32_t v0=v[0], v1=v[1], sum=0, i;  ", null, 0, null);
		sc1.addCodeLine("    uint32_t delta=0x9e3779b9;        ", null, 0, null);
		sc1.addCodeLine("    uint32_t k0=k[0], k1=k[1], k2=k[2], k3=k[3]; ", null, 0, null);
		sc1.addCodeLine("    for (i=0; i < 32; i++) {    ", null, 0, null);
		sc1.addCodeLine("        sum += delta;", null, 0, null);
		sc1.addCodeLine("        v1 += ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);", null, 0, null); 
		sc1.addCodeLine("        v0 += ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);", null, 0, null); 
		sc1.addCodeLine("    }                                            ", null, 0, null); 
		sc1.addCodeLine("    v[0]=v0; v[1]=v1;", null, 0, null);
		sc1.addCodeLine("}", null, 0, null);


		l.nextStep("Code Source");
		//First Question
		l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		FillInBlanksQuestionModel name =
				new FillInBlanksQuestionModel("Name of the algorithm");
		name.setPrompt("What does the abbreviation of TEA stay for?");
		name.addAnswer("Tiny Encryption Algorithm", 1, " ");
		l.addFIBQuestion(name);  
		l.finalizeGeneration();
		l.nextStep();

		//Second Question
		FillInBlanksQuestionModel size =
				new FillInBlanksQuestionModel("Size");
		size.setPrompt("what is the size of a TEA Key?");
		size.addAnswer("128 bits", 1, " Tea uses 128 B key.");
		l.addFIBQuestion(size);  
		l.finalizeGeneration();
		l.nextStep();

		//Third Question
		FillInBlanksQuestionModel inv =
				new FillInBlanksQuestionModel("inventor");
		inv.setPrompt("Who has invented the TEA ?");
		inv.addAnswer("David wheeler and Roger Needham", 1, " David wheeler and Roger Needham have invented TEA at cambridge university.");
		l.addFIBQuestion(inv);  
		l.finalizeGeneration();
		l.nextStep();



		l.nextStep("Questions"); 

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



		//This is for the two Feistel loops
		for(int i=0; i<2;i++)
		{
			int o = i % 2;

			// Here starts the Graph construction for the encryption
			// CreateV0 visualizes the v0 input, by creating a square and 
			// an arrow 
			String v0,v1,k0,k1, k2, k3;
			v0 ="V0 <-"+ v[0]; 
			v1 ="V1<-"+v[1]; 
			k0 ="K0<-"+k[0];
			k1 ="K1<-"+k[1];
			k2 ="K2<-"+k[2];
			k3 ="K3<-"+k[3]; 

			if(o == 1) LinkV1(v11);

			if(o == 0)sc1.highlight(6);
			else sc1.highlight(7);
			if(o == 0) createV0(v0, 850+200,150+(i*200)-50,v11);
			else createV0(" ", 850+200,150+(i*200)-50,v00);
			l.nextStep();
			//createInc1 is the increment rectangle of the circuit
			// it is created in a way that it should be linked to the 
			// input value of v

			createInc1(750+200,200+ (i*200)-50,v11);

			//createAnd1, createAndMittel correspond to the and gate 
			//of the circuit 

			if(o == 0) createAnd1(k0,700+200,200+(i*200)-50, v11);
			else createAnd1(k2,700+200,200+(i*200)-50, v11);
			//Link(850+200,150+(i*200) -50, v11);
			createAndMittel(700+200,250+(i*200)-50, v11 );
			//Link(850+200 , 200+(i*200) -50 ,v11); 
			createInc2(750+200  ,300+(i*200)-50, v11 );


			if(o == 0) createAnd2(k1,700+200,300+(i*200) -50,v11);
			else createAnd2(k3,700+200,300+(i*200) -50,v11);
			LinkHoriz(650 +198, 190+(i*200) -50, v11); 

			LinkHorizXor(650 +205, 190+(i*200), v11);
			LinkHoriz(650 +200, 290+(i*200) -50, v11);

			l.nextStep();
			//createCircle is the xor gate that links three inputs
			createCircle(650+200,250+(i*200) -50, v11);
			LinkHorizLast(595+200 , 236 +(i*200) -50, v11);

			if(o == 1) LinkV2(v11);
			createAnd3(570+200+4 ,249+(i*200) -55);


			if(o == 0) createV0(v1,570+200,150+(i*200)-50 ,v00);  
			else createV1(" ",570+200,150+(i*200)-50, v11 );


			if(o==0) sc1.unhighlight(6);
			else sc1.unhighlight(7);
 


			l.nextStep(); 

		}

		sc1.highlight(8);
		sc1.unhighlight(8);
		sc1.highlight(9);
		sc1.unhighlight(9);



		sc1.highlight(10);
		sc1.unhighlight(10);
		l.nextStep("Excecution of the algorithm"); 
		
		//print the input text and the encrypted text as results
		//Description of the algorithm 
		SourceCodeProperties CodeConc = new SourceCodeProperties(); 
		CodeConc.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20)); 
		CodeConc.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red); 
		SourceCode AnimConclusion = l.newSourceCode(new Coordinates(100, 400),
				"sourceCode", null, CodeConc); 
		AnimConclusion.addCodeLine("For one iteration and two Feistle cycle we obtain:" ,null, 0, null); 
		AnimConclusion.addCodeLine("Original values: "+ v[0]+" "+ v[1],null, 0, null); 
		int[] c = encrypt(v, k); 
		AnimConclusion.addCodeLine("Encrypted values: "+ c[0]+ c[1],null, 0, null);
		AnimConclusion.show(); 
		l.nextStep("Results"); 
		
		
		return l.toString();
	}


	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		int[] v = (int[])primitives.get("v");
		int[] k = (int[])primitives.get("k");

		if(v.length!=2 )
		{	try {
			throw new IllegalArgumentException("The size should be 2");
		} catch (IllegalArgumentException e) { 
			e.printStackTrace();
			return false;
		}
		}

		if(k.length!=4 )
		{	try {
			throw new IllegalArgumentException("The size of should be 4");
		} catch (IllegalArgumentException e) { 
			e.printStackTrace();
			return false;
		}
		}
		return true; 
	}



}