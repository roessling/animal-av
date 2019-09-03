/*
 * BoothProp.java
 * Pascal Fleckenstein, Alexander Ziesing, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArcProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
//import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class BoothProp implements ValidatingGenerator {
	private Language lang;
	private int zahl2;
	private int zahl1;

	private String z1;
	private String z2;


	/**
	 * The header text including the headline
	 */
	private Text                 header;

	/**
	 * The rectangle around the headline
	 */
	private Rect                 hRect;

	/**
	 * Globally defined text properties
	 */
	private TextProperties       textProps;

	/**
	 * the source code shown in the animation
	 */
	private SourceCode           src;

	/**
	 * Globally defined source code properties
	 */
	private SourceCodeProperties sourceCodeProps;

	
    private ArrayProperties arrayProps;
	
	/*
    public BoothProp(Language l) {
        // Store the language object
        lang = l;
        // This initializes the step mode. Each pair of subsequent steps has to
        // be divdided by a call of lang.nextStep();
        lang.setStepMode(true);
      } */

	public void init(){
		lang = new AnimalScript("Booth Algorithmus [DE]", "Pascal Fleckenstein, Alexander Ziesing", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		zahl2 = (Integer)primitives.get("zahl2");
		zahl1 = (Integer)primitives.get("zahl1");
		z1 = Integer.toBinaryString(zahl1);
		z2 = Integer.toBinaryString(zahl2);        
		
		sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProp");
		
        //Auff�llen mit Nullen


		int abstand = Math.abs(z1.length()-z2.length());
		if(zahl2 >= 0 && zahl1 >= 0) {
			if(abstand != 0) {
				String zeros = new String(new char[abstand]).replace('\0','0');
				if(z1.length()>z2.length())
					z2 = zeros + z2;
				else 	z1 = zeros + z1;
			}
			//to indicate that z1 and z2 are positive integer
			z1 = '0' +z1;
			z2 = '0' +z2;

		} else if(zahl1<0 && zahl2<0) {
			int firstocc = Integer.min(z1.indexOf('0'),z2.indexOf('0'));
			if((firstocc-1)>0) {
				z1 = z1.substring(firstocc-1);
				z2 = z2.substring(firstocc-1);
			}else if(firstocc==-1) {
				if(zahl1==-1 && zahl2==-1) {
					//Design Entscheidung: -1 wird durch mind. 2 Einsen dargestellt
					z1 = z1.substring(30);
					z2 = z2.substring(30);
				} else if(zahl1==-1 ) {
					int cut = z2.indexOf('0')-1;
					z1 = z1.substring(cut);
					z2 = z2.substring(cut);
				} else if (zahl2 == -1) {
					int cut = z1.indexOf('0')-1;
					z1 = z1.substring(cut);
					z2 = z2.substring(cut);
				}
			}
		} else {
			if(zahl1>=0 && zahl2<0) {
				if(zahl2 != -1) {
					z2 = z2.substring(z2.indexOf('0')-1);
					abstand = z2.length()-z1.length();
					if(abstand <= 0) {
						String ones = new String(new char[Math.abs(abstand)+1]).replace('\0','1');
						z2 = ones + z2;
					}
				} else {
					z2 = z2.substring(31-z1.length());
				}
				abstand = z2.length() - z1.length();
				String zeros = new String(new char[abstand]).replace('\0','0');
				z1 = zeros + z1;

			} else {
				//geht sch�ner
				if(zahl1 != -1) {
					z1 = z1.substring(z1.indexOf('0')-1);
					abstand = z1.length()-z2.length();
					if(abstand <= 0) {
						String ones = new String(new char[Math.abs(abstand)+1]).replace('\0','1');
						z1 = ones + z1;
					}
				}else {
					z1 = z1.substring(31-z2.length());
				}

				abstand = z1.length() - z2.length();
				String zeros = new String(new char[abstand]).replace('\0','0');
				z2 = zeros + z2;

			}
		}
		//falls Multiplikand negativste Zahl ist, muss Bitl�nge erh�ht werden
		if(zahl1<0 && !z1.substring(1).contains("1")) {
			z1 = "1" + z1;
			if(zahl2>=0) {
				z2 = "0" + z2;
			} else {
				z2 = "1" + z2;
			}
		} 


		initialize();

		return lang.toString();
	}


	@SuppressWarnings("unused")
	public void initialize() {
		// show the header with a heading surrounded by a rectangle
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 30), "Algorithmus von Booth",
				"header", null, headerProps);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
				null, rectProps);
		//setze F�llungsfarbe wieder auf wei� f�r andere Kasten
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		// setup the start page with the description
		lang.nextStep();
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		lang.newText(new Coordinates(30, 100),
				"Der Algorithmus von Booth berechnet effizient die Multiplikation",
				"description1", null, textProps);
		lang.newText(new Offset(0, 25, "description1",
				AnimalScript.DIRECTION_NW),
				"zweier 2-Komplement Zahlen aus. Er benötigt dafür so viele Iterationen",
				"description2", null, textProps);
		lang.newText(new Offset(0, 25, "description2",
				AnimalScript.DIRECTION_NW),
				"wie die Anzahl der Bits, die einer der beiden Operanden repräsentiert.",
				"description3", null, textProps);


		List<Primitive> excl = new ArrayList<Primitive>();
		excl.add(header);
		excl.add(hRect); 


		lang.nextStep("Einleitung");

		lang.hideAllPrimitivesExcept(excl);

		// Create Array: coordinates, data, name, display options,
		// default properties

		// first, set the visual properties (somewhat similar to CSS)
/*		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW); */
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.MONOSPACED,Font.PLAIN, 12));

		Font f = (Font) arrayProps.get("font");
		

		//Linux macht leider sein eigenes Ding
		
		int[] visZ1 = new int[z1.length()];
		int[] visZ2 = new int[z2.length()];
		for(int i=0;i<z1.length();i++) {
			visZ1[i] = Integer.parseInt(String.valueOf(z1.charAt(i)));
			visZ2[i] = Integer.parseInt(String.valueOf(z2.charAt(i)));
		}
		
		sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("Monospaced", Font.PLAIN, 12));

		// now, create the IntArray object, linked to the properties
		IntArray ia = lang.newIntArray(new Coordinates(50, 120), visZ1 , "intArray1",
				null, arrayProps);

		IntArray ib = lang.newIntArray(new Offset(0, 50, "intArray1",AnimalScript.DIRECTION_SW), visZ2, "intArray2", null, arrayProps);

		TextProperties arrProp = new TextProperties();
		arrProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));
		Text descrz1 = lang.newText(new Offset(-10, -25, "intArray1",AnimalScript.DIRECTION_NW),
				"1.Zahl (" +zahl1 + " in dezimal), der Multiplikand in 2er Komplement Darstellung:",
				"descrz1", null, arrProp);
		Text descrz2 = lang.newText(new Offset(-10, -25, "intArray2",
				AnimalScript.DIRECTION_NW),
				"2.Zahl (" + zahl2 +" in dezimal), der Multiplikator in 2er Komplement Darstellung:",
				"descrz2", null, arrProp);
		
		arrProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		Text bez1 = lang.newText(new Offset(-30, 0, "intArray1",AnimalScript.DIRECTION_NW),
				"M =",
				"bez1", null, arrProp);
		Text bez2 = lang.newText(new Offset(-30, 0, "intArray2",AnimalScript.DIRECTION_NW),
				"Q =",
				"bez2", null, arrProp);

		int count = z1.length();

		int[] z = new int[count*2+1];

		for(int i=0;i<count;i++) {
			z[i] = 0;
		}
		for(int i=0;i<count;i++) {
			z[count+i] = Integer.parseInt(String.valueOf(z2.charAt(i)));
		}
		z[count*2] =0;

		//intArray3
		IntArray iz = lang.newIntArray(new Offset(0, 70, "intArray2",AnimalScript.DIRECTION_SW), z , "intArray3",
				null, arrayProps);
		iz.hide();
		
		
		

		
		//Source Code
		if(iz.getLength()>24) {
			src = lang.newSourceCode(new Offset(70,-150,"intArray3",AnimalScript.DIRECTION_NE), "sourceCode",
			        null, sourceCodeProps);
		} else {
					src = lang.newSourceCode(new Offset(100,10,"descrz1",AnimalScript.DIRECTION_SE), "sourceCode",
		        null, sourceCodeProps);
		}


		    src.addCodeLine("public int[] booth(int zahl1, int zahl2) {", null, 0,
		        null); // 0
		    src.addCodeLine("// Umwandeln in binäre Strings", null, 1, null); //1
		    src.addCodeLine("String z1 = Integer.toBinaryString(zahl1);", null, 1, null); //2
		    src.addCodeLine("String z2 = Integer.toBinaryString(zahl2);", null, 1, null); // 3
		    src.addCodeLine("count = z1.length();", null, 1, null); //4
		    src.addCodeLine("A = new int[z1.length];", null, 1, null); //5
		    src.addCodeLine("r = 0", null, 1, null); // 6
		    src.addCodeLine("-M = complement of M", null, 1, null); // 7 
		    src.addCodeLine("AQr init", null, 1, null);  // 8
		    src.addCodeLine("while(count >= 0) {", null, 1, null); // 9
		    src.addCodeLine("if((q0^r) == 1){", null, 1, null); // 10
		    src.addCodeLine("if(q0==1) {", null, 2, null); // 11
		    src
		        .addCodeLine("A  = A - M // A = A + (-M)", null, 3,
		            null); // 12
		    src.addCodeLine("}", null, 2,
		        null); // 13
		    src.addCodeLine("if(r==1) {", null, 2, null); // 14
		    src.addCodeLine("A = A + M", null, 3, null); // 15
		    src.addCodeLine("}", null, 2, null); // 16
		    src.addCodeLine(
		        "}",
		        null, 1, null); // 17
		    src.addCodeLine("AQr = AQr >>; //arithmetic right shift", null, 2, null); // 18
		    src.addCodeLine("count = count -1;", null, 2, null); // 19
		    src.addCodeLine("}", null, 1, null); // 20
		    src.addCodeLine("return AQ;", null, 1, null); // 21
		    src.addCodeLine("}", null, 0, null); // 22
		
		src.highlight(1);
		src.highlight(2);
		src.highlight(3);
		
		// start a new step after the array was created
		lang.nextStep("Initialisierungen");

		src.unhighlight(1);
		src.unhighlight(2);
		src.unhighlight(3);
		src.highlight(4);
		// Create SourceCode: coordinates, name, display options,
		// default properties

		// first, set the visual properties for the source code
/*		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK); */

		


		//Einleitung


		TextProperties algoProps = new TextProperties();
		algoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));
		
		Text overl = lang.newText(new Offset(70, -10,"header",AnimalScript.DIRECTION_NE),"Initiale Zustände:" ,"initDesc", null, algoProps);
		
		algoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		
		Text trenner = lang.newText(new Offset(-10,15,"intArray2",AnimalScript.DIRECTION_SW), "------------------------------------------------------------------------------------",
				"trenner", null, algoProps);
		Text begin1 = lang.newText(new Offset(0,40,"intArray2",AnimalScript.DIRECTION_SW), "Für den Booth Algorithmus benötigen wir die Anzahl der Bits",
				"begin1", null, algoProps);
		Text begin2 = lang.newText(new Offset(0,20,"begin1",AnimalScript.DIRECTION_NW), "einer der beiden Zahlen, repräsentiert durch die Variable count.",
				"begin2", null, algoProps);
		lang.nextStep();

		

		Text text1 = lang.newText(new Offset(70,0,"header",AnimalScript.DIRECTION_E), "count= "+ count,
				"text1", null, algoProps);

		Rect t1Rect = lang.newRect(new Offset(-5, -5, "text1",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "text1", "SE"), "t1Rect",
				null, rectProps);

		 

		lang.nextStep();
		src.unhighlight(4);
		src.highlight(5);

		Text begin3 = lang.newText(new Offset(0,20,"begin2",AnimalScript.DIRECTION_NW), "Des Weiteren benötigen wird ein Hilfsregister A der Bitlänge count="+z1.length()+",",
				"begin3", null, algoProps);
		Text begin3zusatz = lang.newText(new Offset(0,20,"begin3",AnimalScript.DIRECTION_NW), "initialisiert mit Nullen.",
				"begin3zusatz", null, algoProps);

		lang.nextStep();
		String A = new String(new char[count]).replace('\0','0');
		int[] a = new int[count];
		Text text2 = lang.newText(new Offset(20,-0,"text1",AnimalScript.DIRECTION_NE), "A= "+ A,
				"text2", null, algoProps);

		Rect t2Rect = lang.newRect(new Offset(-5, -5, "text2",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "text2", "SE"), "t2Rect",
				null, rectProps);

		lang.nextStep();
		src.unhighlight(5);
		src.highlight(6);
		
		Text begin4 = lang.newText(new Offset(0,20,"begin3zusatz",AnimalScript.DIRECTION_NW), "Außerdem wird noch ein Hilfsbit r fürs Shiften und Ablesen gebraucht.",
				"begin4", null, algoProps);
		lang.nextStep();
		Text text3 = lang.newText(new Offset(20,-0,"text2",AnimalScript.DIRECTION_NE), "r = 0" ,
				"text3", null, algoProps);

		Rect t3Rect = lang.newRect(new Offset(-5, -5, "text3",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "text3", "SE"), "t3Rect",
				null, rectProps);   
		TextProperties indicProps = new TextProperties();
		indicProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 9));      
//		Text indic = lang.newText(new Offset(10,-8,"text3",AnimalScript.DIRECTION_SW),"-1" ,"indic", null, indicProps);   

		lang.nextStep();
		src.unhighlight(6);
		src.highlight(7);
		begin1.hide();
		begin2.hide();
		begin3.hide();
		begin4.hide();
		begin3zusatz.hide();
		
		String m_ = negate(z1);

		int[] mneg = new int[m_.length()];
		for(int i=0;i<mneg.length;i++) {
			mneg[i] = Integer.parseInt(String.valueOf(m_.charAt(i)));
		}

		Text begin5 = lang.newText(new Offset(0,40,"intArray2",AnimalScript.DIRECTION_SW), "Um das Subtrahieren in einer Iteration leichter zu machen, brauchen wir -M.",
				"begin5", null, algoProps);
		Text begin6 = lang.newText(new Offset(0,20,"begin5",AnimalScript.DIRECTION_NW), "Dafür müssen die Bits in M negiert und +1 addiert werden.",
				"begin6", null, algoProps);
		lang.nextStep();
		Text text4 = lang.newText(new Offset(20,-0,"text3",AnimalScript.DIRECTION_NE), "-M="+m_ ,
				"text4", null, algoProps);

		Rect t4Rect = lang.newRect(new Offset(-5, -5, "text4",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "text4", "SE"), "t4Rect",
				null, rectProps);

		lang.nextStep();
		src.unhighlight(7);
		src.highlight(8);
		begin5.hide();
		begin6.hide();

		Text begin7 = lang.newText(new Offset(0,150,"intArray2",AnimalScript.DIRECTION_SW), "Nun wird eine "+ (2*count+1) +"(=2*count+1) große Bitzahl erzeugt.",
				"begin7", null, algoProps);
		Text begin8 = lang.newText(new Offset(0,20,"begin7",AnimalScript.DIRECTION_NW), "Diese besteht aus A,Q,r. r ist das LSB",
				"begin8", null, algoProps);
		lang.nextStep();
		iz.show();
		


		lang.nextStep();
		Text arrDesc1 = lang.newText(new Offset(14*(count/2),-30,"intArray3",AnimalScript.DIRECTION_NW), "A",
				"arrDesc1", null, algoProps); 
		iz.highlightCell(0, count-1, null, null);
		//hier m�chte ich jeweils das rectangle highlighten

		lang.nextStep();
		iz.unhighlightCell(0, count-1, null, null);

		Text arrDesc2 = lang.newText(new Offset(-30-14*(count/2),-30,"intArray3",AnimalScript.DIRECTION_NE), "Q",
				"arrDesc2", null, algoProps); 
		iz.highlightCell(count, 2*count-1, null, null);

		lang.nextStep();
		iz.unhighlightCell(count, 2*count-1, null, null);

		Text arrDesc3 = lang.newText(new Offset(-10,-30,"intArray3",AnimalScript.DIRECTION_NE), "r",
				"arrDesc3", null, algoProps); 

		//highlight q_1
		iz.highlightCell(2*count, null, null);
		lang.nextStep();
		src.unhighlight(8);
		//unhighlight q_1
		iz.unhighlightCell(2*count, null, null);

		
		begin7.hide();
		begin8.hide();
		
		//ab hier der Booth Algo
			
		lang.nextStep();

		//tempor�res Ergebnis Array f�r die Addition
		int[] tmp;

		IntArray shift = lang.newIntArray(new Coordinates(0,0), z , "shiftArr",
				null, arrayProps);
		shift.hide();
		
		//Erstelle neues Array, das M repr�sentiert
		IntArray m = lang.newIntArray(new Offset(0, 40, "intArray3", AnimalScript.DIRECTION_SW), visZ1 , "mArray",
				null, arrayProps);
		m.hide();
		
		src.highlight(9);
		lang.nextStep("Iteration 0");
		Variables n = lang.newVariables();
		n.openContext();
		n.declare("int", "durchlauf", "0");
/*		TwoValueCounter counter = lang.newCounter(n);
		
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		
		TwoValueView view = lang.newCounterView(counter, new Coordinates(80,60), cp, true, false); */
		//count Iterationen ben�tigt der Algo
		for(int j=0;j< count; j++) {
			String it = Integer.toString(j+1);
			n.set("durchlauf", it);
			//Indizes sind nicht n�tig?
			iz.showIndices(false, null, null);
			src.unhighlight(9);
			//highlighte letzte beiden Bits von links aus gesehen
			iz.highlightElem(2*count-1, 2*count, null, null);
			
			//haben die beiden Bits unterschiedliche Werte?
			int q = z[2*count-1] ^ z[2*count];

			
			src.highlight(10);

			lang.nextStep();
			iz.unhighlightElem(2*count-1, 2*count, null, null);


			src.unhighlight(10);

			if(q==1 && z[2*count-1]==1) {
				//A = A-M
				//highlighte A in Z
				src.unhighlight(10);
				src.highlight(11);
				src.highlight(12);
				src.highlight(13);
				iz.highlightElem(0, count-1, null, null);
				//-M highlighten
				t4Rect.changeColor("color", Color.PINK, null, null);
				text4.changeColor("color", Color.PINK, null, null);
//				ia.highlightCell(0, count-1, null, null);
				

				m = lang.newIntArray(new Offset(0, 40, "intArray3", AnimalScript.DIRECTION_SW), mneg , "mArray"+j,
						null, arrayProps);
				m.setName("mArray"+j);

				lang.newText(new Offset(-5, -40, "mArray"+j,AnimalScript.DIRECTION_C),
						"+",
						"op1", null, headerProps);     
				lang.newText(new Offset(-30, 0, "mArray"+j,AnimalScript.DIRECTION_NW),
						"-M:",
						"bez1", null, arrProp);

				Text trenner2 = lang.newText(new Offset(-10,15,"mArray",AnimalScript.DIRECTION_SW), "------------------------------------------------------------------------------------",
						"trenner2", null, algoProps);

				tmp =  add(mneg,a);
				for(int i=0;i<count;i++) {
					z[i] = tmp[i];
				}  
				lang.nextStep();
				t4Rect.changeColor("color", Color.BLACK, null, null);
				text4.changeColor("color", Color.BLACK, null, null);
			}

			if(q==1 && z[2*count]==1) {
				src.unhighlight(10);
				src.highlight(14);
				src.highlight(15);
				src.highlight(16);
				//A = A+M
				iz.highlightElem(0, count-1, null, null);

				//M highlighten
				ia.highlightCell(0, count-1, null, null);


				m =lang.newIntArray(new Offset(0, 40, "intArray3", AnimalScript.DIRECTION_SW), visZ1 , "mArray"+j,
						null, arrayProps);
				m.setName("mArray"+j);
				lang.newText(new Offset(-5, -40, "mArray"+j,AnimalScript.DIRECTION_C),
						"+",
						"op1", null, headerProps);     
				lang.newText(new Offset(-30, 0, "mArray"+j,AnimalScript.DIRECTION_NW),
						"M:",
						"bez1", null, arrProp);
				Text trenner2 = lang.newText(new Offset(-10,15,"mArray"+j,AnimalScript.DIRECTION_SW), "------------------------------------------------------------------------------------",
						"trenner2", null, algoProps);
				tmp =   add(visZ1,a);
	/*			for(int i=0;i<count;i++) {
					System.out.println(tmp[i]);
				} */


				for(int i=0;i<count;i++) {
					z[i] = tmp[i];
				}
				lang.nextStep();
			}
			
			// nach Addition 

			src.unhighlight(10);
			src.unhighlight(11);
			src.unhighlight(13);
			src.unhighlight(14);
			src.unhighlight(16);
			
			iz.unhighlightElem(0, count-1, null, null);

			ia.unhighlightCell(0, count-1, null, null);
			int[] tmp_z = Arrays.copyOf(z, z.length);
		
			//shift z : arithmetic right shift
			for(int i = z.length-2; i>=0 ;i--) {
				z[i+1] = z[i];
			}
			//Shiften visualisieren

			//Koordinaten f�r Pfeile
			Node[] graphNodes = new Node[2];
			graphNodes[0] = new Coordinates(0,0);
			graphNodes[1] = new Coordinates(0,0);
			PolylineProperties lineProps = new PolylineProperties();

			lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY,true);
//			lineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY,true);



			excl.add(iz);
			excl.add(ia);
			excl.add(ib);
			excl.add(descrz2);
			excl.add(descrz1);
			excl.add(bez1);
			excl.add(bez2);
			excl.add(trenner);
			excl.add(text1);
			excl.add(text2);
			excl.add(text3);
			excl.add(text4);
			excl.add(t1Rect);
			excl.add(t2Rect);
			excl.add(t3Rect);
			excl.add(t4Rect);
			excl.add(overl);
			excl.add(arrDesc1);
			excl.add(arrDesc2);
			excl.add(arrDesc3);
//			excl.add(indic);
			excl.add(src);

			excl.add(n);
			
			Polyline zeiger = lang.newPolyline(graphNodes, "zeiger", null, lineProps);


			if(q==1) {
				shift= lang.newIntArray(new Offset(0, 60, "mArray"+j, AnimalScript.DIRECTION_SW), tmp_z , "shiftArr"+j,
						null, arrayProps);
				//hier shift Pfeile in einem step aber mit timing?
				lang.nextStep();
				lang.newText(new Offset(5, 0, "shiftArr"+j,AnimalScript.DIRECTION_NE),
						">>",
						"shiftZeichen", null, headerProps);
				
				src.unhighlight(12);
				src.unhighlight(15);
				src.highlight(18);
				IntArray interim = lang.newIntArray(new Offset(0, 60, "shiftArr"+j, AnimalScript.DIRECTION_SW), z , "interim"+j,
						null, arrayProps);
				
				for(int v = 0; v<shift.getLength();v++) {

					if(v==0) { //Verzweigung zusammenfassen
						graphNodes[0] = new Offset(8, 0,"shiftArr"+j, AnimalScript.DIRECTION_SW);
						graphNodes[1] = new Offset(8, 0,"interim"+j, AnimalScript.DIRECTION_NW);
						shift.highlightCell(0, null, null);
						interim.highlightCell(0, null, null);
					} else {
						if(f.getFamily().equals("Serif")) {
							//Nur wenn Schriftart Serif
							graphNodes[0] = new Offset(15*(v-1)+(8+v-1), 0,"shiftArr"+j, AnimalScript.DIRECTION_SW);
							graphNodes[1] = new Offset(15*v+v+8, 0,"interim"+j, AnimalScript.DIRECTION_NW);
							
						} else {
							graphNodes[0] = new Offset(8*(2*(v-1))+(8+v-1), 0,"shiftArr"+j, AnimalScript.DIRECTION_SW);
							graphNodes[1] = new Offset(8*(2*v)+v+8, 0,"interim"+j, AnimalScript.DIRECTION_NW);
						}
						shift.highlightCell(v-1, null, null);
						interim.highlightCell(v, null, null);
					}
					//zeiger zeigen zum alten shiftArr?
					zeiger= lang.newPolyline(graphNodes, "zeiger"+v+j, null, lineProps);
					
					lang.nextStep();
					if(v==0) {
						shift.unhighlightCell(0, null, null);
					} else {
						shift.unhighlightCell(v-1, null, null);
					}

					
					interim.unhighlightCell(v, null, null);
				}
				

				
				
				ArcProperties arcProps = new ArcProperties();
				arcProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY,false);
				arcProps.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 180);
				arcProps.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 270);
				arcProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY,true);
				arcProps.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY,false);
				arcProps.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY,true);
				lang.newArc(new Offset(0,-105,"interim"+j,AnimalScript.DIRECTION_NE),new Coordinates(50,120) , "arc", null, arcProps);
				lang.newText(new Offset(18,-120, "interim"+j,AnimalScript.DIRECTION_NE),
						"write",
						"write", null, arrProp);
				lang.newText(new Offset(18,-110, "interim"+j,AnimalScript.DIRECTION_NE),
						"back",
						"back", null, arrProp);
				interim.highlightElem(0, 2*count, null, null);
				lang.nextStep();
				src.unhighlight(18);
				
				src.highlight(19);
				lang.hideAllPrimitivesExcept(excl); 


			} else {
				
				shift= lang.newIntArray(new Offset(0, 60, "intArray3", AnimalScript.DIRECTION_SW), z , "shiftArr"+j,
						null, arrayProps);
				src.highlight(18);
				src.unhighlight(12);
				src.unhighlight(15);
				Text shiftZe = lang.newText(new Offset(5, 0, "intArray3",AnimalScript.DIRECTION_NE),
						">>",
						"shiftZeichen", null, headerProps);
				lang.nextStep();
				

				for(int v = 0; v<shift.getLength();v++) {
					if(v==0) {
						graphNodes[0] = new Offset(8, 0,"intArray3", AnimalScript.DIRECTION_SW);
						graphNodes[1] = new Offset(8, 0,"shiftArr"+j, AnimalScript.DIRECTION_NW);
						iz.highlightCell(0, null, null);
						shift.highlightCell(0,null,null);
					}else {
						if(f.getFamily().equals("Serif")) {
							//Nur wenn Schriftart Serif
							graphNodes[0] = new Offset(15*(v-1)+(7+v), 0,"intArray3", AnimalScript.DIRECTION_SW);
							graphNodes[1] = new Offset(15*v+v+8, 0,"shiftArr"+j, AnimalScript.DIRECTION_NW);
						} else {
							graphNodes[0] = new Offset(8*(2*(v-1))+(8+v-1), 0,"intArray3", AnimalScript.DIRECTION_SW);
							graphNodes[1] = new Offset(8*(2*v)+v+8, 0,"shiftArr"+j, AnimalScript.DIRECTION_NW);
						}
						shift.highlightCell(v,null,null);
						iz.highlightCell(v-1,null,null);
					}
					zeiger = lang.newPolyline(graphNodes, "zeiger"+v+j, null, lineProps);
	//				zeiger.show(new MsTiming(500*v));
					lang.nextStep();
					if(v==0) {
						iz.unhighlightCell(0, null, null);
					} else {
						iz.unhighlightCell(v-1, null, null);
					}

					
					shift.unhighlightCell(v, null, null); 

			
					
				}
//				lang.nextStep();
				shiftZe.hide();
				//Zwischenergebnis zur�ckschreiben
				ArcProperties arcProps = new ArcProperties();
				arcProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY,false);
				arcProps.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 180);
				arcProps.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 270);
				arcProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY,true);
				arcProps.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY,false);
				arcProps.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY,true);
				lang.newArc(new Offset(0,-30,"shiftArr"+j,AnimalScript.DIRECTION_NE),new Coordinates(50,40) , "arc", null, arcProps);
				lang.newText(new Offset(10,-40, "shiftArr"+j,AnimalScript.DIRECTION_NE),
						"write",
						"write", null, arrProp);
				lang.newText(new Offset(10,-30, "shiftArr"+j,AnimalScript.DIRECTION_NE),
						"back",
						"back", null, arrProp);

				shift.highlightElem(0, 2*count, null, null);
				lang.nextStep();
				src.unhighlight(18);
				src.highlight(19);

				lang.hideAllPrimitivesExcept(excl);

			}

			//kopiere das neue A aus z in A
			a = Arrays.copyOfRange(z, 0, count);
			iz = lang.newIntArray(new Offset(0, 70, "intArray2",AnimalScript.DIRECTION_SW), z , "intArray3",
					null, arrayProps); 
			iz.showIndices(false, null, null);
			lang.nextStep();
			src.unhighlight(18);
			
			
			src.unhighlight(19);
			src.highlight(9);
			
			if(j+1==count) {
				lang.nextStep("Schlussteil");
			}else {
				lang.nextStep("Iteration "+(j+1));
			}
			
			
			
		}//end for
		// � = ü
		// � = ä
		// � = ö+
		
		/*
		 *  � -> ä
 			� -> Ä
 			� -> ö
 			� -> Ö
 			� -> ü
 			� -> Ü
 			� -> ß
		 */
		n.closeContext();
		
		src.unhighlight(9);
		
		String tmwp = "";
		for(int i = 0; i<z.length-1; i++) {
			tmwp = tmwp + String.valueOf(z[i]);
		}

//		System.out.println(zahl1);

//		System.out.println(zahl2);


		long mult = getTwosComplement(tmwp);
//		System.out.println(mult);
//		System.out.println(tmwp);

		iz.highlightCell(0, null, null);
		
		src.highlight(21);
		//intArray3 ist eigentlich nicht richtig, das aktuelle ist intArray..
		lang.newText(new Offset(5,5,"intArray3",AnimalScript.DIRECTION_SW), "||" ,
				"ispos1", null, algoProps);
		lang.newText(new Offset(2,10,"ispos1",AnimalScript.DIRECTION_SW), "Das höchstwertige Bit gibt an, ob das Ergebnis positiv(0) oder negativ(1) ist." ,
				"ispos2", null, algoProps);
		if(mult>=0) {
			lang.newText(new Offset(0,10,"ispos2",AnimalScript.DIRECTION_SW), "Hier also positiv." ,
					"ispos3", null, algoProps);
		} else {
			lang.newText(new Offset(0,10,"ispos2",AnimalScript.DIRECTION_SW), "Hier also negativ." ,
				"ispos3", null, algoProps);
		}
		
		lang.nextStep();
		iz.unhighlightCell(0, null, null);
		iz.highlightCell(1, 2*count-1, null, null);
		
		if(mult>=0) {
		lang.newText(new Offset(0,10,"ispos3",AnimalScript.DIRECTION_SW), "Das Ergebnis steht somit in Q als gewöhnliche Bitzahl." ,
				"ispos4", null, algoProps); 
		}else {
		lang.newText(new Offset(0,10,"ispos3",AnimalScript.DIRECTION_SW), "Das Ergebnis ist also AQ als 2er-Komplement." ,
				"ispos4", null, algoProps); 
		iz.highlightCell(0, null, null);
		}
		

		TextProperties ergebnisProp = new TextProperties();
		ergebnisProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 22));
		
		Text ergebnis = lang.newText(new Offset(0,90,"ispos4",AnimalScript.DIRECTION_SW), "Das Ergebnis der Multiplikation von "+ zahl1 + " * " + zahl2 + " ist: " + mult ,
				"ergebnis", null, ergebnisProp);

		lang.nextStep("Ergebnis");
        
	}


	public long getTwosComplement(String binary) {
		if(binary.charAt(0)=='1') {
			String invertedInt = invertDigits(binary);
			long decimalVal = Long.parseLong(invertedInt,2);
			decimalVal = (decimalVal +1) * -1;
			return decimalVal;  		
		} 
		return Long.parseLong(binary,2);

	}

	public String invertDigits(String binary) {
		binary = binary.replace('0', ' ');
		binary = binary.replace('1', '0');
		binary = binary.replace(' ', '1');
		return binary;
	}

	public String negate(String compl) {
		String neg= "";
		for(int i = 0; i<compl.length();i++)
			if(compl.charAt(i)=='0') {
				neg = neg + '1';
			} else {
				neg = neg + '0';
			}
		int x = Integer.parseUnsignedInt(neg,2);
		x++;
		neg = Integer.toBinaryString(x);
		if(neg.length()>compl.length()) {
			neg = neg.substring(1);
		}
		if(neg.length()<compl.length()) {
			neg = new String(new char[compl.length()-neg.length()]).replace('\0','0') + neg;
		}
		return neg;
	}


	public int[] add(int[] op1, int[] op2) {
		//Annahme: beide Arrays gleich gro�
		//geht leichter mit Umrechnung in dez
		int[] erg = new int[op1.length];
		int carry = 0;
		for(int i= op1.length-1;i>=0;i--) {
			switch(op1[i]+op2[i]+carry) {
			case 0: erg[i]=0; carry=0; break;
			case 1: erg[i]=1; carry=0; break;
			case 2: erg[i]=0; carry=1; break;
			case 3: erg[i]=1; carry=1; break;
			}
		}

		return erg;
	}

	public String getName() {
		return "Booth Algorithmus [DE]";
	}

	public String getAlgorithmName() {
		return "Booth";
	}

	public String getAnimationAuthor() {
		return "Pascal Fleckenstein, Alexander Ziesing";
	}

	public String getDescription(){
		return "Der Booth Algorithmus dient zur Multiplikation zweier Zahlen in Zweierkomplement-Darstellung."
				 +"\n"
				 +"Der Vorteil von dem Verfahren ist, dass die Vorzeichen der beiden Zahlen keine Rolle spielen."
				 +"\n"
				 +"Der Algorithmus ist im Durchschnitt schneller als die gew�hnliche Multiplikation.";
	}

	public String getCodeExample(){
		 return "function  booth(){"
				 +"\n"
				 +"//Zahl 1, der Multiplikand"
				 +"\n"
				 +"M"
				 +"\n"
				 +"//Zahl 2, der Multiplikator"
				 +"\n"
				 +"Q"
				 +"\n"
				 +"//Hilfsbit"
				 +"\n"
				 +"r = 0"
				 +"\n"
				 +"//Bitl�nge der l�ngsten Zahl"
				 +"\n"
				 +"n"
				 +"\n"
				 +"//Komplement von M"
				 +"\n"
				 +"-M"
				 +"\n"
				 +"//n Bits, initialisiert mit Nullen "
				 +"\n"
				 +"A = new int[n]"
				 +"\n"
				 +"//Register zum Berechnen des Ergebnises"
				 +"\n"
				 +"AQr"
				 +"\n"
				 +"while(n>0){"
				 +"\n"
				 +"	if(q0 ^ r == 1){"
				 +"\n"
				 +"		if(q0 == 1){"
				 +"\n"
				 +"		//�berlauf wird ignoriert"
				 +"\n"
				 +"		A  = A + (-M)"
				 +"\n"
				 +"		}"
				 +"\n"
				 +"		if(r == 1){"
				 +"\n"
				 +"		A = A + M "
				 +"\n"
				 +"		}"
				 +"\n"
				 +"	}"
				 +"\n"
				 +"	//Arithmeitscher Shift nach rechts"
				 +"\n"
				 +"	AQr = AQr >>"
				 +"\n"
				 +"	n = n -1"
				 +"\n"
				 +"}"
				 +"\n"
				 +"return AQ"
				 +"\n"
				 +"}"
				 +"\n"
				 +"\n";
	}

	public String getFileExtension(){
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		int z2 = (Integer)arg1.get("zahl2");
		int z1 = (Integer)arg1.get("zahl1");
		
		if(z2>Integer.MAX_VALUE|| z1>Integer.MAX_VALUE ||z2<Integer.MIN_VALUE+1 || z1<Integer.MIN_VALUE+1) {
			JFrame frameErr = new JFrame("Error Message");
			JOptionPane.showMessageDialog(frameErr, "Zahl 1 oder Zahl 2 verletzt die Beschränkung einer Integer Zahl.","Input Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	//Auskommentieren bei Abgabe
	/*
	public static void main(String[] args) {
		ValidatingGenerator generator = new BoothProp();
		generator.init();
		Animal.startGeneratorWindow(generator);
	} */

}