/*
 * BitkettenOTP.java
 * Daniel Friesen,Jens Abels, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.primitives.ArrayMarker;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.ArrayMarkerProperties;

public class BitkettenOTP implements ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties ETxtprops;
    private SourceCodeProperties scProps;
    private int[] message;
    private ArrayProperties aProps;
    private int[] key;

    public void init(){
        lang = new AnimalScript("Bitketten One-Time Pad", "Daniel Friesen,Jens Abels", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        ETxtprops = (SourceCodeProperties)props.getPropertiesByName("ETxtprops");
        scProps = (SourceCodeProperties)props.getPropertiesByName("scProps");
        message = (int[])primitives.get("message");
        aProps = (ArrayProperties)props.getPropertiesByName("aProps");
        key = (int[])primitives.get("key");
        
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        
        lang.setStepMode(true);
        
        start(message,key);
        
        lang.finalizeGeneration();
        
        return lang.toString();
    }
    
    public void start(int[] m, int []k){
    	
    	int delay= 3000;
    	
    	//Guppe von Fragen
 	   QuestionGroupModel Q= new QuestionGroupModel("Null",2);
 	   
 	   
 	   
 	//1. Frage
 	   MultipleSelectionQuestionModel q_xor= new MultipleSelectionQuestionModel("XOR");
 	   q_xor.setGroupID(Q.getID());
 	   q_xor.setPrompt("Wie funktioniert ein 'XOR'?");
 	   q_xor.addAnswer("1 xor 1 --> 1",0,"Falsch");
 	   q_xor.addAnswer("0 xor 1 --> 1",1,"Richtig");
 	   q_xor.addAnswer("0 xor 0 --> 0",1,"Richtig");
 	   q_xor.addAnswer("1 xor 0 --> 0",0,"Falsch");
 	   
 	   //2. Frage
 	   
 	   MultipleChoiceQuestionModel q_xor2= new MultipleChoiceQuestionModel("XOR2");

 	   q_xor2.setGroupID(Q.getID());
	   q_xor2.setPrompt("Welcher Wert kommt an die aktuelle Stelle?");
	   q_xor2.addAnswer("1",0,"Falsch");
	   q_xor2.addAnswer("0",1,"Richtig");
 	   
    	
		//Das Chiffrat
		int [] c= new int [m.length];
	
	//######################Proberties und Primitiven###############################
		//Proberties
	 	SourceCodeProperties hProps = new SourceCodeProperties();
        hProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.BOLD, 22));
        
        SourceCodeProperties tProps = new SourceCodeProperties();
        tProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.BOLD, 16));
        
        
        
        RectProperties recProps = new RectProperties();
        

        
        
        aProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        aProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        aProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        aProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        aProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        
        //Header+Rectangle
        SourceCode header = lang.newSourceCode(new Coordinates(50,15), "header",null,hProps);
        header.addCodeLine("Bitketten One-Time-Pad", null, 0, null);
        
        Rect r1 = lang.newRect(new Offset(-10,-5,"header",
                AnimalScript.DIRECTION_NW),new Offset(10,5,"header", "SE"), "r1", null, recProps);
        
        //Arraymarker fuer Message
        ArrayMarkerProperties M= new ArrayMarkerProperties();
        M.set(AnimationPropertiesKeys.LABEL_PROPERTY, "m");
        
      //Arraymarker fuer Key
        ArrayMarkerProperties K= new ArrayMarkerProperties();
        K.set(AnimationPropertiesKeys.LABEL_PROPERTY, "k");
        
        
      //Arraymarker fuer Chiffre
        ArrayMarkerProperties C= new ArrayMarkerProperties();
        C.set(AnimationPropertiesKeys.LABEL_PROPERTY, "c");
        
        //Primitiven
        
        
        
        	//Texte+Rectangle (Primitiven)
        SourceCode klar_txt = lang.newSourceCode(new Coordinates(500,100), "klar_txt",null,tProps);
        klar_txt.addCodeLine("Klartext", null, 0, null);
        
        Rect r2 = lang.newRect(new Offset(-10,-5,"klar_txt",
                AnimalScript.DIRECTION_NW),new Offset(10,5,"klar_txt", "SE"), "r2", null, recProps);
        
        SourceCode key_txt = lang.newSourceCode(new Offset(20,259,"klar_txt","SW"), "key_txt",null,tProps);
        key_txt.addCodeLine("Key", null, 0, null);
        
        Rect r3 = lang.newRect(new Offset(-10,-5,"key_txt",
                AnimalScript.DIRECTION_NW),new Offset(10,5,"key_txt", "SE"), "r3", null, recProps);
        
        SourceCode xor_txt = lang.newSourceCode(new Coordinates(800,250), "xor_txt",null,tProps);
        xor_txt.addCodeLine("XOR", null, 0, null);
        
        Rect r4 = lang.newRect(new Offset(-15,-10,"xor_txt",
                AnimalScript.DIRECTION_NW),new Offset(15,10,"xor_txt", "SE"), "r4", null, recProps);
        
        SourceCode Einleitung=lang.newSourceCode(new Coordinates(20,80), "Einleitung",null,ETxtprops);
        
        SourceCode sc=lang.newSourceCode(new Coordinates(20,80), "Quellcode", null, scProps);
        
        
        
        	//Array und Polylines
        IntArray a_klar = lang.newIntArray(new Offset(20,-26,"klar_txt", "SE"), m, "a_klar",
                null, aProps);
        
        IntArray a_key = lang.newIntArray(new Offset(0,270,"a_klar", "SW"), k, "a_key",
                null, aProps);
        
        IntArray a_chiff = lang.newIntArray(new Offset(150,5,"r4", "NE"), c, "a_chiff",
                null, aProps);
        
        SourceCode res=lang.newSourceCode(new Offset(0,-45, "a_chiff", "NW"), "", null, scProps);
        res.addCodeLine("Das erhaltene Chriffre lautet:", "", 0, null);
        
        	//ArrayMarker
        
        ArrayMarker Mmark= lang.newArrayMarker(a_klar, 0, "", null, M);
        ArrayMarker Kmark= lang.newArrayMarker(a_key, 0, "", null, K);
        ArrayMarker Cmark= lang.newArrayMarker(a_chiff, 0, "", null, C);
        
        
        
        SourceCode c_txt = lang.newSourceCode(new Offset(25,-15,"a_chiff", "NE"), "c_txt",null,tProps);
        c_txt.addCodeLine("Chiffrat", null, 0, null);
        
        Rect r5 = lang.newRect(new Offset(-10,-5,"c_txt",
                AnimalScript.DIRECTION_NW),new Offset(10,5,"c_txt", "SE"), "r5", null, recProps);
        
        
        Node[] nodes = new Node [3];
        nodes[0] = new Offset(0,0,"a_klar", "SE");
        nodes[1] = new Offset(0,120,"a_klar", "SE");
        nodes[2] = new Offset(0,0,"r4", "NW");
                
        Polyline l = lang.newPolyline(nodes, "l", null);
        
        Node[] nodes2 = new Node [3];
        nodes2[0] = new Offset(0,0,"a_key", "NE");
        nodes2[1] = new Offset(0,-110,"a_key", "NE");
        nodes2[2] = new Offset(0,0,"r4", "SW");
                
        Polyline l2 = lang.newPolyline(nodes2, "l2", null);
        
        Node[] nodes3 = new Node [2];
        nodes3[0] = new Offset(33,0,"r4", "C");
        nodes3[1] = new Offset(181,0,"r4", "C");
        
        Polyline l3 = lang.newPolyline(nodes3, "l3", null);
        
        //Hide All
        klar_txt.hide();
        r2.hide();
        key_txt.hide();
        r3.hide();
        c_txt.hide();
        r5.hide();
        xor_txt.hide();
        r4.hide();
        l.hide();
        l2.hide();
        l3.hide();
        a_klar.hide();
        a_key.hide();
        a_chiff.hide();
        Mmark.hide();
        Kmark.hide();
        Cmark.hide();
        res.hide();
        
	
	
	//#########################Einleitungstext######################################
	Einleitung.addCodeLine("Das Bitketten One-Time Pad (OTP) ist ein symmetrisches Verschlüsselungsverfahren. Das bedeutet,", "", 0, null);
	Einleitung.addCodeLine("das beide Kommunizierenden den gleichen Schlüssel zur Ver- und Entschlüsselung einer", "", 0, null);
	Einleitung.addCodeLine("Nachricht benutzen. Dabei gilt ein OTP genau dann al pefekt sicher, wenn die Länge des", "", 0, null);
	Einleitung.addCodeLine("Keys genau so oder mindestens so lang ist (sofern der Key zufällig gewählt worden ist),", "", 0, null);
	Einleitung.addCodeLine("wie die zu verschlüsselnde Nachricht.", "", 0, null);
	Einleitung.addCodeLine("Bei einem Bitketten OTP wird die Nachricht als auch der Key binär dargestellt. Dabei werden", "", 0, null);
	Einleitung.addCodeLine("alle 'gleichrangigen' Bits von Key und Nachricht mittels einer XOR-Verknüpfung chiffriert.", "", 0, null);
	Einleitung.addCodeLine("Wendet man auf dem erhaltenen Chiffrat den verwendeten Key an,so erhält man daraus ", "", 0, null);
	Einleitung.addCodeLine("wieder den Klartext.", "", 0, null);
	Einleitung.addCodeLine("Jedoch sollte man nach jeder Anwendung einen neuen Key verwenden (die beide", "", 0, null);
	Einleitung.addCodeLine("Kommunikationspartner wissen müssen), da sonst die Sicherheit des OTP nachlässt.", "", 0, null);
	
	
	lang.nextStep("Einleitungstext");
	lang.nextStep(5000);
	
	//#########################Quelltext############################################
	Einleitung.hide();
	
	sc.addCodeLine("//Bedingung: Key und Message sind 8 Bit groß", "", 0, null);
	sc.addCodeLine("", "", 0, null);
	sc.addCodeLine("public void BitkettenOTP(int[] message, int[] key){", "", 0, null);
	sc.addCodeLine("int[] Chiffrat= new int [8];", "", 1, null);
	sc.addCodeLine("for (int i=0; i<8;i++){", "", 1, null);
	sc.addCodeLine("if(message[i]==key[i]){", "", 2, null);
	sc.addCodeLine("Chiffrat[i]=0;", "", 3, null);
	sc.addCodeLine("}", "", 2, null);
	sc.addCodeLine("else{", "", 2, null);
	sc.addCodeLine("Chiffrat[i]=1;", "", 3, null);
	sc.addCodeLine("}", "", 2, null);
	sc.addCodeLine("}", "", 1, null);
	sc.addCodeLine("return chiffrat;", "", 1, null);
	sc.addCodeLine("}", "", 0, null);
	
	lang.nextStep("Quellcode");
	lang.nextStep(delay);
	
	
	//########################Anzeigen von Klartext und Key##########################
	klar_txt.show();
    r2.show();
    key_txt.show();
    r3.show();
    a_klar.show();
    a_key.show();
    
    sc.highlight(2);
    
    lang.nextStep(delay);
   
    //#########################Chiffre Array anzeigen#################################
    
    sc.unhighlight(2);
    sc.highlight(3);
    a_chiff.show();
    c_txt.show();
    r5.show();
    
    lang.nextStep(delay);
    
    sc.unhighlight(3);
    sc.highlight(4);
    
    lang.nextStep(delay);
    
    sc.unhighlight(4);
    xor_txt.show();
    r4.show();
    l.show();
    l2.show();
    l3.show();
    
    lang.addMSQuestion(q_xor);
    
    lang.nextStep(delay+1000);
    
    lang.nextStep("Start: OTP");
    Mmark.show();
    Kmark.show();
    Cmark.show();
    
    for (int i=0; i<m.length;i++){
    	sc.highlight(5);
    	a_klar.highlightCell(i, null, null);
    	a_key.highlightCell(i, null, null);
    	Mmark.move(i, null, null);
    	Kmark.move(i, null, null);
    	Cmark.move(i, null, null);
    	
    	lang.nextStep(delay);
    	
    	sc.unhighlight(5);
    	a_klar.unhighlightCell(i, null, null);
    	a_key.unhighlightCell(i, null, null);
    	
    	if(m[i]==k[i]){
    		lang.addMCQuestion(q_xor2);
    		lang.nextStep(delay+1000);
    		a_chiff.put(i, 0, null, null);
    		sc.highlight(6);
    		a_chiff.highlightCell(i, null, null);
    		lang.nextStep(delay);
    		a_chiff.unhighlightCell(i, null, null);
    		sc.unhighlight(6);
    	}else{
    		sc.highlight(8);
    		lang.nextStep(delay);
    		sc.unhighlight(8);
    		a_chiff.put(i, 1,null,null);
    		a_chiff.highlightCell(i, null, null);
    		sc.highlight(9);
    		lang.nextStep(delay);
    		sc.unhighlight(9);
    		a_chiff.unhighlightCell(i, null, null);
    	}
    	a_chiff.unhighlightCell(i, null, null);
    }
    
    sc.highlight(12);
    
    Mmark.hide();
    Kmark.hide();
    Cmark.hide();
    res.show();
    
    for(int i=0;i<m.length;i++){
    	a_chiff.highlightCell(i, null, null);
    }
    lang.nextStep("Ergebnis");
  }
    
    

    public String getName() {
        return "Bitketten One-Time Pad";
    }

    public String getAlgorithmName() {
        return "Bitketten One-Time Pad";
    }

    public String getAnimationAuthor() {
        return "Daniel Friesen,Jens Abels";
    }

    public String getDescription(){
        return "Das Bitketten One-Time Pad (OTP) ist ein symmetrisches Verschlüsselungsverfahren. Das bedeutet,"
 +"\n"
 +"dass beide Kommunizierenden den gleichen Schlüssel zur Ver- und Entschlüsselung einer"
 +"\n"
 +"Nachricht benutzen. Dabei gilt ein OTP genau dann al pefekt sicher, wenn die Länge des"
 +"\n"
 +"Keys genau so oder mindestens so lang ist (sofern der Key zufällig gewählt worden ist), "
 +"\n"
 +"wie die zu verschlüsselnde Nachricht."
 +"\n"
 +"Bei einem Bitketten OTP wird die Nachricht als auch der Key binär dargestellt. Dabei werden"
 +"\n"
 +"alle \"gleichrangigen\" Bits von Key und Nachricht mittels einer XOR-Verknüpfung chiffriert."
 +"\n"
 +"Wendet man auf dem erhaltenen Chiffrat den verwendeten Key an,so erhält man daraus "
 +"\n"
 +"wieder den Klartext."
 +"\n"
 +"Jedoch sollte man nach jeder Anwendung einen neuen Key verwenden (die beide"
 +"\n"
 +"Kommunikationspartner wissen müssen), da sonst die Sicherheit des OTP nachlässt.";
    }

    public String getCodeExample(){
        return "//Bedingung: Key und Message sind 8 Bit groß"
 +"\n"
 +"\n"
 +"public void BitkettenOTP(int[] message, int[] key){"
 +"\n"
 +"     int[] Chiffrat= new int [8];"
 +"\n"
 +"     for (int i=0; i<8;i++){"
 +"\n"
 +"          if(message[i]==key[i]){"
 +"\n"
 +"               Chiffrat[i]=0;"
 +"\n"
 +"          }else{"
 +"\n"
 +"               Chiffrat[i]=1;"
 +"\n"
 +"          }"
 +"\n"
 +"     }"
 +"\n"
 +"     return chiffrat;"
 +"\n"
 +"}"
 +"\n";
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
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives){
    	message = (int[])primitives.get("message");
    	key = (int[])primitives.get("key");
    	
    	if(message.length!=key.length){
    		return false;
    	}
    	
    	for(int i=0; i<message.length;i++){
    		if(message[i]<0||message[i]>1||key[i]<0||key[i]>1){
    			return false;
    		}
    	}
    	return true;
    }
    
   
}