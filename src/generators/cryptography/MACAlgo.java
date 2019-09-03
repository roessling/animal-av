/*
 * MACAlgo.java
 * Dominik Rieder, Nicolas Schickert, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;

public class MACAlgo implements Generator {
    private Language lang;
    private boolean manipulateMessage;
    private boolean manipulateMac;
    private String message;
    private String wrongMessage;
    private String wrongMac;
    private int key;
    private SourceCode scMain;
    private SourceCode scHash;
    private SourceCode scAuthenticate;
    private ArrayProperties arrayProps;
    private SourceCodeProperties scProps;
    private StringArray firstMessageArray;
    private StringArray secondMessageArray;
    private StringArray firstMacArray;
    private StringArray secondMacArray;
    private Text caption;
    private int c;
    private static final char[] valueArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
    		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 
    		'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 
    		'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 
    		'W', 'X', 'Y', 'Z', '!', '"', '$', '%', '&', '/', '(', ')', '=', '*', '-', ':',
    		'#', '+', '.', ','}; 
    private int hashMax;
    private TextProperties importantProps;
    private TextProperties textProps;
    
    private String description = "Der Message Authentication Code dient zur Authentizierung beim Austausch von Nachrichten.  "+"\n"
    		 					+"Sender und Empfaenger einigen sich auf einen geheimen Schluessel. Aufgrund dieses Schluessels "+"\n"
    		 					+"und einer Hashfunktion wird der MAC generiert, welcher mit der original Nachricht versendet wird."
    		 					+"\n"
    		 					+"\n"
    		 					+"Der Empfaenger berechnet nun selbst den MAC der empfangenen Nachricht und vergleicht ihn mit dem "+"\n"
    		 					+"empfangenen MAC. Stimmt er ueberein, kann er davon ausgehen, dass die Nachricht nicht manipuliert "+"\n"
    		 					+"wurde. Ist er unterschiedlich, entspricht die Nachricht nicht dem original."
    		 					+"\n"
    		 					+"\n"
    		 					+"Die Wahl der Hashfunktion ist ein wichtiger Faktor. Sie sollte Kollisionsresistent und schwierig"+"\n"
    		 					+"umkehrbar sein."
    		 					+"\n"
    		 					+"\n"
    		 					+"In diesem Beispiel wurde eine relativ simple Hashfunktion gewaehlt, bei der durchaus Kollisionen"+"\n"
    		 					+"auftreten koennen.(Kollision = Zwei unterschiedliche Nachrichten werden auf gleichen Wert gehasht)";
    
    private String fazit =		 "Das MAC Verfahren wird NUR zur Ueberpruefung auf Manipulation verwendet. Ein Beispiel koennte"+"\n"
    							+ "eine online Geldtransaktion sein. Dabei ist wichtig, dass ein Angreifer nicht unbemerkt Daten"+"\n"
    							+ "wie Empfaenger oder Betrag aendern kann."
    							+"\n"
    							+"\n"
    							+"Das Verfahren an sich bietet aber keine Sicherheit im Sinne der Geheimhaltung. Verschluesselung"+"\n"
    							+ "sollte trotzdem benutzt werden.";

    public void init(){
    	hashMax = valueArray.length;
        lang = new AnimalScript("MAC Verfahren", "Dominik Rieder, Nicolas Schickert", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        manipulateMessage = (Boolean)primitives.get("manipulateMessage");
        manipulateMac = (Boolean)primitives.get("manipulateMAC");
        message = (String)primitives.get("message");
        wrongMessage = (String)primitives.get("wrongMessage");
        wrongMac = (String)primitives.get("wrongMAC");
        key = (int)primitives.get("key");
        arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProperties");
        scProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties");
        
        this.crypt();
        
        return lang.toString();
    }

    public String getName() {
        return "MAC Verfahren";
    }

    public String getAlgorithmName() {
        return "HMAC";
    }

    public String getAnimationAuthor() {
        return "Dominik Rieder, Nicolas Schickert";
    }

    public String getDescription(){
    	return "Beim MAC Verfahren wird eine Nachricht durch einen geheimen Schl�ssel und einer Hashfunktion gehasht. "
    			+ "Der gehashte Wert wird zusammen mit der Nachricht versendet. Anschlie�end hasht der Empf�nger die "
    			+ "Nachricht erneut. Stimmt der Wert mit dem versendeten MAC nicht �berein, wurde die Nachricht manipuliert.";
    }

    public String getCodeExample(){
        return "	public static void createMAC(String message, int key){"
 +"\n"
 +"		String mac = calcHash(message, key);"
 +"\n"
 +"		authenticateMAC(message, key, mac);"
 +"\n"
 +"	}"
 +"\n"
 +"	"
 +"\n"
 +"	public static void authenticateMAC(String message, String realMAC){"
 +"\n"
 +"		String newMAC = calcHash(message, key);"
 +"\n"
 +"		if(!realMAC.equals(newMAC)){"
 +"\n"
 +"			System.out.println(\"Message has been manipulated\");"
 +"\n"
 +"		}else{"
 +"\n"
 +"			System.out.println(\"Message is correct\");"
 +"\n"
 +"		}"
 +"\n"
 +"	}"
 +"\n"
 +"	"
 +"\n"
 +"	public static String calcHash(String toHash, int key){"
 +"\n"
 +"		if(toHash.length() % 2 == 1){"
 +"\n"
 +"			toHash.concat(\"0\");"
 +"\n"
 +"		}"
 +"\n"
 +"		String hashValue = \"\";"
 +"\n"
 +"		int x = 0;"
 +"\n"
 +"		for(int i = 0; i < toHash.length() - 1; i += 2){"
 +"\n"
 +"			x = (int)toHash.charAt(i) + (int)toHash.charAt(i+1);"
 +"\n"
 +"			x = (x ^ key * (key % 5)) % " + hashMax + ";" 
 +"\n"
 +"			hashValue = hashValue + (char)x;"
 +"\n"
 +"		}"
 +"\n"
 +"		return hashValue;"
 +"\n"
 +"	}";
    }
    
    public void crypt(){ 
    	TextProperties captionProps = new TextProperties();
     	captionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
     	caption = lang.newText(new Coordinates(400,10), "Message Authentication Code Verfahren", "caption",null, captionProps);
     	
    	SourceCode descriptiontxt = lang.newSourceCode(new Coordinates(380, 50), "description", 
    			null, scProps);
    	String[] parts = description.split("\n");
    	for (String string : parts) {
			descriptiontxt.addCodeLine(string, null, string.split("\\t").length, null);
		}
    	lang.nextStep();
    	descriptiontxt.hide();
    	
    	importantProps = new TextProperties();
    	importantProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 12));
       
        scMain = lang.newSourceCode(new Coordinates(40, 50), "sourceCode",
            null, scProps);
        scHash = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
                null, scProps);
        scAuthenticate = lang.newSourceCode(new Coordinates(40, 370), "sourceCode",
                null, scProps);
        
        c = 60;
        Text keylbl = lang.newText(new Coordinates(700,c), "Key: "+ Integer.toString(key), "Key", null);
        c += 20;
        if(manipulateMessage){
        	Text wrgmsglbl = lang.newText(new Coordinates(700, c), "Wrong message: " + wrongMessage, "Message", null);
        	c += 20;
        }
        if(manipulateMac){
        	Text wrgMaclbl = lang.newText(new Coordinates(700, c), "Wrong MAC: " + wrongMac, "", null);
        	c += 20;
        }
        c += 10;
       
        scMain.addCodeLine("public void createMAC(String message, int key){", null, 0,
            null); // 0
        scMain.addCodeLine("String mac = calcHash(message, key);", null, 1, null); // 1
        scMain.addCodeLine("authenticateMAC(message, key, mac);", null, 1, null); // 2
        scMain.addCodeLine("}", null,0, null); // 3
        
        scAuthenticate.addCodeLine("public void authenticateMAC(String message, String realMAC){", null, 0, null); //   0
        scAuthenticate.addCodeLine("String newMAC = calcHash(message, key);", null, 1, null); // 1
        scAuthenticate.addCodeLine("if(!realMAC.equals(newMAC)){", null, 1, null); //   2
        scAuthenticate.addCodeLine("System.out.println('Message has been manipulated');", null, 2, null); //   3
        scAuthenticate.addCodeLine("}else{", null, 1, null); //   4
        scAuthenticate.addCodeLine("System.out.println('Message is correct');", null, 2, null); //   5
        scAuthenticate.addCodeLine("}", null, 1, null); //   6
      
        scHash.addCodeLine("public String calcHash(String toHash, int key){", null, 0, null); //  0
        scHash.addCodeLine("if(toHash.length() % 2 == 1){", null, 1, null); //   1
        scHash.addCodeLine("toHash.concat('0');", null, 2, null); //   2
        scHash.addCodeLine("}", null, 1, null); // 18  3
        scHash.addCodeLine("String hashValue = '';", null, 1, null); //   4
        scHash.addCodeLine("int x = 0;", null, 1, null); //  5
        scHash.addCodeLine("for(int i = 0; i < toHash.length() - 1; i += 2){", null, 1, null); // 6
        scHash.addCodeLine("x = (int)toHash.charAt(i) + (int)toHash.charAt(i+1);", null, 2, null); // 7
        scHash.addCodeLine("x = (x ^ key * (key % 5)) %" + hashMax + ";", null, 2, null); // 8
        scHash.addCodeLine("hashValue = hashValue + (char)x;", null, 2, null); // 9
        scHash.addCodeLine("}", null, 1, null); // 10
        scHash.addCodeLine("return hashValue;", null, 1, null); // 11
        scHash.addCodeLine("}", null, 0, null); // 12
        
        scAuthenticate.hide();
        scHash.hide();
        lang.nextStep();
    
        try {
        	createMAC();
          } catch (LineNotExistsException e) {
            e.printStackTrace();
          }
    }
  
	public void createMAC(){
    	scMain.highlight(0);
    	lang.nextStep();
    	scMain.unhighlight(0);
    	scMain.highlight(1);
    	lang.nextStep();
    	
    	//calculates the Hashvalue for message
    	scHash.highlight(0);
    	scHash.show();
    	lang.nextStep();
    	
    	String mac = calcHash(message, 1);
    	scMain.unhighlight(1);
    	scMain.highlight(2);
    	scAuthenticate.show();
    	lang.nextStep();
 
    	if(manipulateMac){
    		mac = wrongMac;
    	}
    	if(manipulateMessage){
    		authenticateMAC(wrongMessage, mac);
    	}else{
    		authenticateMAC(message, mac);
    	}
    	scMain.hide();
    	lang.hideAllPrimitives();
    	firstMessageArray.hide();
    	secondMessageArray.hide();
    	firstMacArray.hide();
    	secondMacArray.hide();
    	MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("q1");
    	q1.setPrompt("Kann der MAC auch als Signatur verwendet werden?");
    	q1.addAnswer("Ja, da der Schl�ssel geheim ist und nur mit ihm ein g�ltiger MAC erzeugt werden kann", 0, 
    			"Falsch, da beide Kommunikationspartner den gleichen Schl�ssel haben und somit beide g�ltige MACs erzeugen k�nnen. "
    			+ "Es ist somit nicht eindeutig von wem die Nachricht stammt.");
    	q1.addAnswer("Nein, da beide Kommunikationspartner unter dem geheimen Schl�ssel einen g�ltigen MAC erzeugen k�nnen.", 1, "Richtig!");
    	lang.addMCQuestion(q1);
    	
    	lang.nextStep();
    	
    	MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("q2");
    	q2.setPrompt("Muss die Nachricht zusammen mit dem MAC versendet werden?");
    	q2.addAnswer("Nein, da man den MAC r�ckw�rts berechnen kann und somit die original Nachricht erh�lt", 0,
    			"Falsch! Hashfunktionen sind nicht umkehrbar!");
    	q2.addAnswer("Ja, da Hashfunktionen nicht umkehrbar sind und somit aus dem MAC nicht mehr die original Nachricht "
    			+ "erzeugt werden kann.", 1, "Richtig!");
    	lang.addMCQuestion(q2);
    	
    	lang.nextStep();
    	
    	MultipleChoiceQuestionModel q3 = new MultipleChoiceQuestionModel("q3");
    	q3.setPrompt("Ist die Hashfunktion H(k) = (2*k) mod 70 kollisionsresistent?");
    	q3.addAnswer("Ja, da mehrere unterschiedliche Werte von k nicht auf den gleichen Wert gehasht werden.", 0
    			, "Falsch! Der Hashraum ist lediglich 70 Elemente gro�. Bei allen m�glichen Nachrichten treten"
    					+ " durchaus Kollisionen auf. Beispiel: H(35) = H(70) = 0");
    	q3.addAnswer("Nein, da der Hashraum nur 70 Elemente gro� ist und somit h�ufig unterschiedliche Werte von k auf den gleichen"
    			+ " Wert gehasht werden.", 1, "Richtig!");
    	lang.addMCQuestion(q3);
    	
    	lang.nextStep();
    	caption.show();
    	SourceCode fazittxt = lang.newSourceCode(new Coordinates(380, 50), "fazit", 
    			null, scProps);
    	String[] parts = fazit.split("\n");
    	for (String string : parts) {
			fazittxt.addCodeLine(string, null, string.split("\\t").length, null);
		}
    	
    	lang.finalizeGeneration();
    }
    
    public void authenticateMAC(String message, String realMAC){
    	Text authlbl = lang.newText(new Coordinates(700, c), "Authenticate(" + message + ", " 
    			+ Integer.toString(key) + ", " + realMAC + ")", "Authenticate", null, importantProps);
    	c += 40;
    	scAuthenticate.highlight(0);
    	lang.nextStep();
    	
    	scAuthenticate.unhighlight(0);
    	scAuthenticate.highlight(1);
    	lang.nextStep();
    	
    	scHash.highlight(0);
    	scHash.show();
    	String newMAC = calcHash(message, 2);
    	
    	scAuthenticate.unhighlight(1);
    	scAuthenticate.highlight(2);
    	Text question = lang.newText(new Coordinates(700, c), newMAC + " = " + realMAC + "?", "", null, importantProps);
    	c += 20;
    	lang.nextStep();
    	if(!realMAC.equals(newMAC)){
    		scAuthenticate.unhighlight(2);
    		scAuthenticate.highlight(3);
    		Text resultText = lang.newText(new Coordinates(700, c), "--> Message has been manipulated", "", null, importantProps);
    		lang.nextStep();
    		scAuthenticate.unhighlight(3);
    		lang.nextStep();
    	}else{
    		scAuthenticate.unhighlight(2);
    		scAuthenticate.highlight(4);
    		lang.nextStep();
    		scAuthenticate.unhighlight(4);
    		scAuthenticate.highlight(5);
    		Text resultText = lang.newText(new Coordinates(700, c), "--> Message is correct", "", null, importantProps);
    		resultText.changeColor(resultText.getText(), Color.GREEN, null, null);
    		lang.nextStep();
    		scAuthenticate.unhighlight(5);
    		lang.nextStep();
    	}
    	scAuthenticate.hide();
    	scMain.unhighlight(2);
    	lang.nextStep();
    }
    
    public String calcHash(String m, int number){
    	StringArray msgArray;
    	StringArray macArray;
    	scHash.unhighlight(0);
    	scHash.highlight(1);
    	lang.nextStep();
    	Text msgtxt = lang.newText(new Coordinates(700,c), "Message:", "", null);
    	if(m.length() % 2 == 1){
    		scHash.unhighlight(1);
    		scHash.highlight(2);
    		m = m + "0";
    		if(number == 1){
    			firstMessageArray = lang.newStringArray(new Coordinates(800,c), createArray(m), "messageArray1", null, arrayProps);
    			msgArray = firstMessageArray;
    		}else{
    			secondMessageArray = lang.newStringArray(new Coordinates(800,c), createArray(m), "messageArray1", null, arrayProps);
    			msgArray = secondMessageArray;
    		}
            c += 60;
            
    		lang.nextStep();
    		scHash.unhighlight(2);
    		scHash.highlight(4);
    	}else{
    		scHash.unhighlight(1);
    		scHash.highlight(4);
    		if(number == 1){
    			firstMessageArray = lang.newStringArray(new Coordinates(800,c), createArray(m), "messageArray1", null, arrayProps);
    			msgArray = firstMessageArray;
    		}else{
    			secondMessageArray = lang.newStringArray(new Coordinates(800,c), createArray(m), "messageArray1", null, arrayProps);
    			msgArray = secondMessageArray;
    		}
    		c += 60;
    	}
    	String placeholder = "";
    	for(int i = 0; i < m.length()/2; i++){
    		placeholder += " ";
    	}
    	Text mactxt = lang.newText(new Coordinates(700, c), "MAC:","", null);
    	if(number == 1){
    		firstMacArray = lang.newStringArray(new Coordinates(800,c), createArray(placeholder), "MacArray1", null, arrayProps);
    		macArray = firstMacArray;
    	}else{
    		secondMacArray = lang.newStringArray(new Coordinates(800,c), createArray(placeholder), "MacArray1", null, arrayProps);
    		macArray = secondMacArray;
    	}
    	c += 60;
    	lang.nextStep();
    	
    	String hashValue = "";
    	scHash.unhighlight(4);
    	scHash.highlight(5);
    	lang.nextStep();
    	
    	int x = 0;
    	scHash.unhighlight(5);
    	scHash.highlight(6);
    	lang.nextStep();
    	scHash.unhighlight(6);
    	
    	for(int i = 0; i < m.length() - 1; i += 2){
    		scHash.highlight(7);
    		msgArray.highlightCell(i, null, null);
    		msgArray.highlightCell(i + 1, null, null);
    		
    		lang.nextStep();
    		x = (int)m.charAt(i) + (int)m.charAt(i+1);
    		scHash.unhighlight(7);
    		scHash.highlight(8);
    		lang.nextStep();
    		x = (x ^ key * (key % 5)) % hashMax;
    		
    		scHash.unhighlight(8);
    		scHash.highlight(9);
    		
    		macArray.put(i/2, Character.toString(valueArray[x]), null, null);
    		macArray.highlightCell(i/2, null, null);
    		
    		lang.nextStep();
    		hashValue = hashValue + valueArray[x];
    		scHash.unhighlight(9);
    		macArray.unhighlightCell(i/2,  null, null);
    		msgArray.unhighlightCell(i, null, null);
    		msgArray.unhighlightCell(i + 1, null, null);
    	}
    	scHash.highlight(11);
    	scHash.hide();
    	lang.nextStep();
    	scHash.unhighlight(11);
    	return hashValue;
    }
    
    public String[] createArray(String m){
    	String[] strArray = new String[m.length()];
    	for(int i = 0; i < m.length(); i++){
    		strArray[i] = Character.toString(m.charAt(i));
    	}
    	return strArray;
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

}