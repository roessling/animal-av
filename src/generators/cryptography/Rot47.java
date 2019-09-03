/*
 * rot47.java
 * Denis Türkpencesi, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

import java.awt.Color;
import java.awt.Font;
import java.math.*;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import interactionsupport.models.*;

public class Rot47 implements Generator {
	private SourceCodeProperties scProps;
	private ArrayProperties arrayProps;
	private RectProperties rec1Props;
	private RectProperties rec2Props;
	private double warsch;
	private RectProperties uberRec;
	RectProperties asciiRec;
	RectProperties codeRec;
	TextProperties textAddProps;
	MatrixProperties matrixProps;
	
	private static final String DESCRIPTION =
			"ROT47 ist ein einfacher Codierungsalgorithmus"
			+ "Zeichen werden entsprechend ihres ASCII- Wertes um 47 Stellen rotiert."
			+ "Dabei werden die ASCII-Werte 33('!') bis 79('O') um 47 erhöht "
			+ "und alle Werte von 80 ('P') bis 126 ('~') um 47 dekrementiert."
			+ "ROT47 ist kein sicherer Verschlüsselungsalgorithmus. "
			+ "Er wird vielmehr oft als Beispiel für eine schlechte Verschlüsselungstechnik genannt."
			+ "ROT47 dient hauptsächlich dazu Text unleserlich zu machen, so dass eine Handlung des Lesers erforderlich ist,"
			+ "um den ursprünglich Text lesen zu können.";
	
	
	private static final String SOURCE_CODE =
			"public String rot(String s){"
			+ "\n\n CharArrayWriter ret = new CharArrayWriter() ;"
			+ "\n\n for(char c: s.toCharArray()){"
			+ "\n\n  if(c >= '!' && c <= 'O')"
			+ "\n   c += 47;"
			+ "\n  else if (c >= 'P' && c <= '~')"
			+ "\n   c -= 47;"
			+ "\n\n  ret.append(c);"
			+ "\n }"
			+ "\n return ret.toString();"
			+ "\n}";
			
	private int quest;
	
    private Language lang;
    private String Verschluesselungsstring;

    public void init(){
        lang = new AnimalScript("Rot47[DE]", "Denis Türkpencesi", 800, 600);
		
		quest = 0;
		
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Verschluesselungsstring = (String)primitives.get("Verschluesselungsstring");
        scProps = (SourceCodeProperties) props.getPropertiesByName("Sourcecode");
		arrayProps = (ArrayProperties) props.getPropertiesByName("Array");
		rec1Props = (RectProperties) props.getPropertiesByName("Rechteck1");
		rec2Props = (RectProperties) props.getPropertiesByName("Rechteck2");
		warsch = (double)primitives.get("Fragewahrscheinlichkeit");
		uberRec = (RectProperties) props.getPropertiesByName("Ueberschriftrechteck");
		asciiRec = (RectProperties) props.getPropertiesByName("Ascii Rechteck");
		codeRec = (RectProperties) props.getPropertiesByName("Code Rechteck");
		textAddProps = (TextProperties)props.getPropertiesByName("Wertanzeige");
		matrixProps = (MatrixProperties)props.getPropertiesByName("Asciitabelle");
		
		sort(Verschluesselungsstring);
		
		lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Rot47[DE]";
    }

    public String getAlgorithmName() {
        return "Rot47";
    }

    public String getAnimationAuthor() {
        return "Denis Türkpencesi";
    }

    public String getDescription(){
        return "ROT47 ist ein einfacher Kodierungsalgorithmus"
 +"\n"
 +"\n"
 +"Zeichen werden entsprechend ihres ASCII-Wertes um 47 Stellen rotiert."
 +"\n"
 +"Dabei werden die ASCII-Werte 33('!') bis 79('O') um 47 erhöht und alle Werte von 80('P') bis 126('~') um "
 +"\n"
 +"47 dekrementiert."
 +"\n"
 +"ROT47 ist kein sicherer Verschlüsselungsalgorithmus."
 +"\n"
 +"Er wird vielmehr oft als Beispiel für eine schlechte Verschlüsselungstechnik genannt."
 +"\n"
 +"ROT47 dient hauptsächlich dazu Text unleserlich zu machen, sodass eine Handlung des Lesers "
 +"\n"
 +"erforderlich ist, um den ursprünglichen Text lesen zu können.";
    }

    public String getCodeExample(){
        return "public String rot(String s){"
 +"\n"
 +"	CharArrayWriter ret = new CharArrayWriter();"
 +"\n"
 +"\n"
 +"	for(char c : s.toCharArray()){"
 +"\n"
 +"		if(c >= '!' && c <= 'O'){"
 +"\n"
 +"			c += 47;"
 +"\n"
 +"		}"
 +"\n"
 +"		else if(c >= 'P' && c <= '~'){"
 +"\n"
 +"			c -= 47;"
 +"\n"
 +"		}"
 +"\n"
 +"\n"
 +"		ret.append(c);"
 +"\n"
 +"	}"
 +"\n"
 +"	"
 +"\n"
 +"	return ret.toString();"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
	
	public void sort(String in){		
		//uberschrift rec

	    //uberRec.set(AnimationPropertiesKeys.BORDER_PROPERTY, );
	    
	    Rect uberRectangle = lang.newRect(new Coordinates(0,40), new Coordinates(830,80), "horst",null, uberRec);
		
	    //Einstellungen für den Text
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
				new Font("SansSerif", Font.PLAIN, 36));
		
		//Überschrift
		Text aText = lang.newText(new Coordinates(10, 55), "Rot47", 
				"text", null, textProps);
		
		TextProperties firstTextProps = new TextProperties();
		firstTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		firstTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
		firstTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
				new Font("SansSerif", Font.PLAIN, 16));
		
		/*TextProperties textAddProps = new TextProperties();
		textAddProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textAddProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);*/
		textAddProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
				new Font("SansSerif", Font.PLAIN, 30));
		
		//Beschriftung rechts oben
		Text charText = lang.newText(new Coordinates(400, 160), "Char: ", 
				"text2", null, textAddProps);
		
		Text charActText = lang.newText(new Coordinates(480, 166), "C", 
				"text3", null, textAddProps);
		
		//Beschriftung rechts oben
		Text charAsciiText = lang.newText(new Coordinates(580, 166), "Ascii: ", 
				"text2", null, textAddProps);
		
		Text charAsciiActText = lang.newText(new Coordinates(660, 166), "C", 
				"text3", null, textAddProps);
		
		Text charAddSubText = lang.newText(new Coordinates(710, 166), "C", 
				"text3", null, textAddProps);
		
		
		charText.hide();
		charActText.hide();
		charAsciiText.hide();
		charAsciiActText.hide();
		charAddSubText.hide();
		
		Text introText1 = lang.newText(new Coordinates(10, 110), "ROT47 ist ein einfacher Kodierungsalgorithmus", 
				"text", null, firstTextProps);
		Text introText2 = lang.newText(new Coordinates(10, 150), "Zeichen werden entsprechend ihres ASCII- Wertes um 47 Stellen rotiert.", 
				"text", null, firstTextProps);
		Text introText3 = lang.newText(new Coordinates(10, 170), "Dabei werden die ASCII-Werte 33('!') bis 79('O') um 47 erhöht ", 
				"text", null, firstTextProps);
		Text introText4 = lang.newText(new Coordinates(10, 190), "und alle Werte von 80 ('P') bis 126 ('~') um 47 dekrementiert.", 
				"text", null, firstTextProps);
		Text introText5 = lang.newText(new Coordinates(10, 210), "ROT47 ist kein sicherer Verschlüsselungsalgorithmus. ", 
				"text", null, firstTextProps);
		Text introText6 = lang.newText(new Coordinates(10, 230), "Er wird vielmehr oft als Beispiel für eine schlechte Verschlüsselungstechnik genannt.", 
				"text", null, firstTextProps);
		Text introText7 = lang.newText(new Coordinates(10, 250), "ROT47 dient hauptsächlich dazu Text unleserlich zu machen, so dass eine Handlung des Lesers erforderlich ist,", 
				"text", null, firstTextProps);
		Text introText8 = lang.newText(new Coordinates(10, 270), "um den ursprünglichen Text lesen zu können.", 
				"text", null, firstTextProps);
		lang.nextStep("1. Einführung");
		
		introText1.hide();
		introText2.hide();
		introText3.hide();
		introText4.hide();
		introText5.hide();
		introText6.hide();
		introText7.hide();
		introText8.hide();
		
		charText.show();
		charAsciiText.show();
		
		//ascii rectangle

	   // asciiRec.set(AnimationPropertiesKeys.FILL_PROPERTY, colorLightGreen);
	    //asciiRec.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	    //uberRec.set(AnimationPropertiesKeys.BORDER_PROPERTY, );
	    
	    Rect asciiRectangle = lang.newRect(new Coordinates(400,200), new Coordinates(830,550), "horst",null, asciiRec);
	    
	    
		//code rectangle

	    //uberRec.set(AnimationPropertiesKeys.BORDER_PROPERTY, );
	    
	    Rect codeRectangle = lang.newRect(new Coordinates(0,200), new Coordinates(400,550), "horst",null, codeRec);
	    
	    
		char[] c = in.toCharArray();
		//Einstellungen für den Array der bearbeitet wird
		
	   /* arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
	        Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
	        Color.RED);
	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
	        Color.YELLOW);*/
	    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 22));
	    
	    //Einstellungen für die ASCII-Tabelle
	    /*MatrixProperties matrixProps = new MatrixProperties();
	    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
	        Color.BLACK);
	    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
	        Color.RED);
	    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
	        Color.YELLOW);
	    matrixProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);*/
	    
	    
	    

		
		//Einstellungen für den Sourcecode
	    
	   /* scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
	        Font.PLAIN, 12));

	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
	        Color.RED);   
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);*/



	    
		//zubearbeitender Array
	    String[] inputArray = charToStringArray(c);
	    String[] outputArray = new String[inputArray.length];
	    for(int e=0;e<outputArray.length;e++)outputArray[e]="";
	    StringArray sAr = lang.newStringArray(new Coordinates(0, 170), inputArray, "stringArray", null, arrayProps);
	    //StringArray sArOut = lang.newStringArray(new Coordinates(20, 120), outputArray, "stringArrayOut", null, arrayProps);
	     
	    //Ascii-Tabelle
	    String[][] stringData = {
				{"33  ! ","45  - ", "57  9 ","69  E ",  "81  Q ","93  ] ",  "105  i ","117  u "},
				{"34  \" ","46  . ","58  : ","70  F ",  "82  R ","94  ^ ",  "106  j ","118  v "},
				{"35  #","47  / ", "59  ; ","71  G ", "83  S ","95  _ ",  "107  k ","119  w "},
				{"36  $ ","48  0 ", "60  < ","72  H ", "84  T ","96  ´ ", "108  l ", "120  x "},
				{"37  % ","49  1 ", "61  = ","73  I ", "85  U ","97  a ", "109  m ","121  y "},
				{"38  & ","50  2 ","62  > ","74  J ", "86  V ","98  b ", "110  n ","122  z "},
				{"39  ' ","51  3 ","63  ? ","75  K ", "87  W ","99  c ", "111  o ","123  { "},
				{"40  ( ","52  4 ","64  @ ","76  L ", "88  X ","100  d ", "112  p ","124  | "},
				{"41  ) ","53  5 ", "65  A ","77  M ", "89  Y ","101  e ", "113  q ","125  } "},
				{"42  * ","54  6 ", "66  B ","78  N ", "90  Z ","102  f ",  "114  r ","126  } "},
				{"43  + ","55  7 ", "67  C ","79  O ", "91  [ ","103  g ", "115  s ","   "},
				{"44  , ","56  8 ", "68  D ","80  P ", "92  \\ ","104  h ", "116  t ","   "}

	    };
	    
	    Node[][] stringCompare = new Node[12][8];
	    String[][] vergleich = new String[12][8];
	    int plusX = 50;
	    int plusY = 29;
	    int xCoordinate = 400;
	    int yCoordinate = 200;
	    
	    for(int i = 0; i < stringCompare.length; i++){
	    	for(int j = 0; j < stringCompare[0].length; j++){
	    		stringCompare[i][j] = new Coordinates(xCoordinate,yCoordinate);
	    		vergleich[i][j] =  " 1: "+xCoordinate+" 2: "+yCoordinate;
	    		xCoordinate += plusX;
	    		if(xCoordinate > 620) xCoordinate += 9;
	    		
	    		
	    	}
	    	xCoordinate = 400;
	    	yCoordinate += plusY;
	    }	    
	  
	   Rect rec = lang.newRect(new Coordinates(400,200), new Coordinates(445,220), "horst",null, rec1Props);
	   Rect rec2 = lang.newRect(new Coordinates(400,200), new Coordinates(445,220), "horst", null, rec2Props);
       rec.hide();
       rec2.hide();
		StringMatrix stringMatrix = lang.newStringMatrix(new Coordinates(400, 200),
				stringData, "stringMatrix", null, matrixProps);
		
		//Sourcecode
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 200), "sourceCode",
		        null, scProps);
		
		sc.addCodeLine("public String rot(String s){", null, 0, null);//0
		sc.addCodeLine("", null, 0, null);//1
		sc.addCodeLine("CharArrayWriter ret = new CharArrayWriter() ;", null, 1, null);//2
		sc.addCodeLine("", null, 0, null);//3
		sc.addCodeLine("for(char c: s.toCharArray()){", null, 1, null);//4
		sc.addCodeLine("", null, 0, null);//5
		sc.addCodeLine("if(c >= '!' && c <= 'O')", null, 2, null);//6
		sc.addCodeLine("c += 47;", null, 3, null);//7
		sc.addCodeLine("else if (c >= 'P' && c <= '~')", null, 2, null);//8
		sc.addCodeLine("c -= 47;", null, 3, null);//9
		sc.addCodeLine("", null, 0, null);//10
		sc.addCodeLine("ret.append(c);", null, 2, null);//11
		sc.addCodeLine("}", null, 2, null);//12
		sc.addCodeLine("return ret.toString();", null, 1, null);//13
		sc.addCodeLine("}", null, 0, null);//14
	    
		lang.nextStep();
		rot47(c, sAr, stringMatrix, sc, stringCompare, rec, rec2, charActText, charAsciiActText, charAddSubText);
		
		asciiRectangle.hide();
		codeRectangle.hide();
		charText.hide();
		charAsciiText.hide();
		
		Text outroText1 = lang.newText(new Coordinates(10, 110), "ROT47 besitzt eine Komplexität von n.", 
				"text", null, firstTextProps);
		Text outroText2 = lang.newText(new Coordinates(10, 150), "Das bedeutet, dass die Verschlüsselungsdauer linear ", 
				"text", null, firstTextProps);
		Text outroText3 = lang.newText(new Coordinates(10, 170), "zur Länge des Eingabestrings steigt.", 
				"text", null, firstTextProps);
		Text outroText4 = lang.newText(new Coordinates(10, 190), "Mit Rot47 verschlüsselte Texte sind sehr leicht zu entschlüsseln,", 
				"text", null, firstTextProps);
		Text outroText5 = lang.newText(new Coordinates(10, 210), "da jedem Wert genau ein anderer Wert zugewiesen wird. Dadurch kann der Ursprungstext durch", 
				"text", null, firstTextProps);
		Text outroText6 = lang.newText(new Coordinates(10, 230), "kryptoanalytische Verfahren wie das Untersuchen von Buchstabenhäufigkeiten oder ", 
				"text", null, firstTextProps);
	    Text outroText7 = lang.newText(new Coordinates(10, 250), "häufigen Buchstabenkombinationen relativ leicht entschlüsselt werden.", 
				"text", null, firstTextProps);
	    Text outroText8 = lang.newText(new Coordinates(10, 270), "Rot47 ist eine sogenannte Caesar-Verschlüsselung.", 
				"text", null, firstTextProps);

	}
	
	public void moveRec(char compare, StringMatrix ascii, Rect rec, Node[][] stringCompare){
		
		int compareParameter1 = 0;
		int compareParameter2 = 0;
		int asciiValue = (int) compare; //asciiwert bestimmen
		int zif = 1;
		
		for(compareParameter1 = 0; compareParameter1 < ascii.getNrRows(); compareParameter1++){
			for(compareParameter2 = 0; compareParameter2 < ascii.getNrCols(); compareParameter2++){
				if(compare>99) zif=3; else zif=2;
				//System.out.println("compare: "+compare+" asciiValue: "+asciiValue+" zif: "+zif+" ascii: "+ascii.getElement(compareParameter1, compareParameter2).substring(0, zif));
				if(ascii.getElement(compareParameter1, compareParameter2).substring(0, zif).equals(String.valueOf(asciiValue))){
					//ascii.highlightElem(compareParameter1, compareParameter2, null, null);
					
					rec.moveTo(null, "translate", stringCompare[compareParameter1][compareParameter2], null, null);
					break;
				}
			}
		}	
	}
	
	public void rot47(char[] toSort,StringArray toSortOutPut, StringMatrix ascii,SourceCode sourceCode, Node[][] stringCompare, Rect rec, Rect rec2, Text charActText, Text charAsciiActText,Text charAddSubText){

		Variables vars = lang.newVariables();
		vars.declare("String", "altesC");
		vars.declare("String", "neuesC");
		vars.declare("String", "alterAscii");
		vars.declare("String", "neuerAscii");
		vars.declare("String", "BisherVerschlüsselt");
		String bisherV = "";
		vars.declare("String", "BisherBetrachtet");
		String bisherB = "";

		
		sourceCode.highlight(0);
		lang.nextStep();
		sourceCode.highlight(2);
		sourceCode.unhighlight(0);
		lang.nextStep();
		sourceCode.unhighlight(2);
		for(int i = 0; i< toSort.length; i++){
			sourceCode.highlight(4);
			toSortOutPut.highlightCell(i, null, null);	
			

			
			char compare = toSort[i];//Wert holen
			bisherB += compare;
			
					
			vars.set("altesC", String.valueOf(compare));
			vars.set("alterAscii", String.valueOf(Integer.valueOf(compare)));
			vars.set("neuesC", " ");
			vars.set("neuerAscii", " ");
			
			//AsciiTabelle markieren	
			rec2.hide();
			//lang.nextStep();
			rec.show();
			moveRec(compare, ascii, rec, stringCompare);
			lang.nextStep(i+". Element");
			charActText.show();
			charActText.setText(String.valueOf(compare), null, null);
			charAsciiActText.show();
			charAsciiActText.setText(String.valueOf(Integer.valueOf(compare)), null, null);
			
			lang.nextStep();
			sourceCode.unhighlight(4);
			sourceCode.highlight(6);
			lang.nextStep();
			
			double rand = Math.random()*100;
            if(warsch>100)warsch=100;
			if(warsch<0)warsch=0;

			if(rand < warsch && quest<2){
				
                 char questCompare = compare;
                 questCompare += ( (compare >= '!' && compare <= 'O') ? 47 : ( (compare >= 'P' && compare <= '~') ? -47 : 0 ));				 
								
				switch(quest){
				case 0:
				FillInBlanksQuestionModel question1 = new FillInBlanksQuestionModel("qest1");				
				question1.setPrompt("Welchen Wert wird c im nächsten Schritt annehmen?");
				question1.addAnswer(String.valueOf(questCompare), 1, "Richtig! Sehr gut!");				
				lang.addFIBQuestion(question1);
				break;
				case 1:
				FillInBlanksQuestionModel question2 = new FillInBlanksQuestionModel("qest2");
				question2.setPrompt("Welchen Wert wird c im nächsten Schritt annehmen?");
				question2.addAnswer(String.valueOf(questCompare), 1, "Richtig! Sehr gut!");				
				lang.addFIBQuestion(question2);
				}

				quest++;
			}
			
			if(compare >= '!' && compare <= 'O'){
				sourceCode.unhighlight(6);
				sourceCode.highlight(7);
			   	compare += 47;
			   	
			   	vars.set("neuesC", String.valueOf(compare));
				vars.set("neuerAscii", String.valueOf(Integer.valueOf(compare)));
			   	
				charAddSubText.show();
				charAddSubText.setText("+47", null, null);
				lang.nextStep();
				charAddSubText.hide();
				charAsciiActText.setText(String.valueOf(Integer.valueOf(compare)), null, null);
				lang.nextStep();
				
				rec2.show();
			   	moveRec(compare, ascii, rec2, stringCompare);
			   	lang.nextStep();
			   	
			   	charActText.setText(String.valueOf(compare), null, null);			   	
			   	lang.nextStep();
			   	
				rec.hide();
				rec2.hide();
				sourceCode.unhighlight(7);
				sourceCode.highlight(11);
			   	toSortOutPut.put(i, String.valueOf(compare), null, null);
	
			   	

			}else{
				sourceCode.unhighlight(6);
				sourceCode.highlight(8);
				if (compare >= 'P' && compare <= '~'){
					lang.nextStep();
					sourceCode.unhighlight(8);
					sourceCode.highlight(9);
					compare -= 47;
					
				   	vars.set("neuesC", String.valueOf(compare));
					vars.set("neuerAscii", String.valueOf(Integer.valueOf(compare)));
					
					charAddSubText.show();
					charAddSubText.setText("-47", null, null);
					lang.nextStep();
					
					charAddSubText.hide();
					charAsciiActText.setText(String.valueOf(Integer.valueOf(compare)), null, null);
					lang.nextStep();
					
					rec2.show();		
					moveRec(compare, ascii, rec2, stringCompare);
					lang.nextStep();
					charActText.setText(String.valueOf(compare), null, null);	
					lang.nextStep();
					
					rec.hide();
					rec2.hide();
					sourceCode.unhighlight(9);
					sourceCode.highlight(11);
					toSortOutPut.put(i, String.valueOf(compare), null, null);

				}

			}
			bisherV += compare;
			vars.set("BisherVerschlüsselt", bisherV);
			vars.set("BisherBetrachtet", bisherB);

			lang.nextStep();
			sourceCode.unhighlight(7);
			sourceCode.unhighlight(8);
			sourceCode.unhighlight(9);
			
			toSortOutPut.unhighlightCell(i, null, null);
			rec.hide();
			rec2.hide();
		   	charActText.hide();
		   	charAsciiActText.hide();
		   	//sourceCode.highlight(11);
		   	
		   	//lang.nextStep();
		   	sourceCode.unhighlight(11);
		   	sourceCode.highlight(12);
		   	lang.nextStep();
		   	sourceCode.unhighlight(12);
		   	
		}
		rec2.hide();
		sourceCode.highlight(4);
		lang.nextStep();
		sourceCode.unhighlight(4);
		sourceCode.highlight(13);
		lang.nextStep();
		sourceCode.unhighlight(13);
		sourceCode.highlight(14);
		lang.nextStep();	
		sourceCode.unhighlight(14);
		sourceCode.hide();
		rec.hide();
		ascii.hide();
		toSortOutPut.hide();
	}
	
	public String[]  charToStringArray(char[] c){
		String [] ret = new String[c.length];
		
		for(int i = 0; i < c.length; i++){
			ret[i] = String.valueOf(c[i]);
		}
		
		return ret;
	}

}