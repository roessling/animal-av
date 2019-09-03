package generators.cryptography.onetimepad;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;


/**
 * OneTimePad Algorithmus welcher ASCII String verschluesselt und entschluesselt.
 * 
 * @author Mateusz Umstaedter, Aidin Dinkhah, Jakub Pilarski
 * @version 1.0
 */
public class OneTimePadString implements Generator {
    private Language lang;
    private TextProperties varsProp;
    private SourceCodeProperties scProps;
    private ArrayProperties arrayProps;
    private String[] Key;
    private String[] Message;
    private boolean method;

        
    private static final String DESCRIPTION = 
    		"One-Time-Pad (oder abgek&uuml;rzt OTP) ist ein symmetrisches Verschl&uuml;sselungsverfahren" 
    		+"(beide Teilnehmer verwenden den gleichen Schl&uuml;ssel). <br />"
    	  	+"Das Verfahren ist theoretisch informationssicher und konnte bis dato nicht gebrochen werden.<br />" 
    	  	+"Es gibt eine Nachricht N und einen dazugehoerigen Schl&uuml;ssel S. Dabei muss der Schl&uuml;ssel genau so lang sein wie die Nachricht.<br />"
    	  	+"In diesem Beispiel wird dies anhand der buchstabenweise Addition von dem Klartext und dem Schl&uuml;ssel erbracht.<br />" 
    	  	+"In dem Fall werden die Buchstaben in Zahlen umgewandelt um die Addition bzw. Substitution zu erleichtern.<br />"
    	  	+"In diesem Verfahren ist method eine boolean Variable welche mit dem Wert true den String verschl&uuml;sselt<br />"
    	  	+"und mit dem Wert false den String Entschl&uuml;sselt.<br />"
    	  	+"Beispiel 1) (A(1) + T(20)) Mod 26 = U(21)<br />"
    	  	+"Beispiel 2) (X(24) + X(24)) Mod 26 = 48 Mod 26 = V(22)<br />"
    	  	+"<br />"
    	  	+"Es gibt 3 Eingabeparameter: <br />"
    	  	+"MessageArray - ASCII Zeichen A,B,...,Z die als Nachricht dienen. <br />"
    		+"SchluesselArray - ASCII Zeichen A,B,...,Z die als Schl&uuml;ssel dienn. <br />"
    		+"method - Boolescher mit dem welchem man den ASCII Code verschl&uuml;sset (true) oder entschl&uuml;sselt (false).";
    
    private static final String SOURCE_CODE =
    		"function OneTimePad (MessageArray, SchluesselArray, Methode)" // 0
    		+"\n if SchluesselArray.length == MessageArray.length then" // 1
    		+"\n 	for i:=0 to MessageArray.length do" // 2
    		+"\n 		if Methode == Verschluesseln then" // 3
    		+"\n 			output[i]=(MessageArray[i] + KeyArray[i]) % 26" // 4
    		+"\n 		else if Methode == Entschluesseln" // 5
    		+"\n 			if (MessageArray[i] - SchluesselArray[i]) < 26 then" // 6
    		+"\n 				output[i]=(26 + (MessageArray[i] - KeyArray[i]) % 26" // 7
    		+"\n 			else" // 8
    		+"\n 				output[i]=(MessageArray[i] - KeyArray[i]) % 26" // 9
    		+"\n 	endfor" // 10
    		+"\n end"; // 11
    
    /**
     * Konstruktor
     */
    public OneTimePadString() {
    	
    }
    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	varsProp = (TextProperties)props.getPropertiesByName("textProperties");
        arrayProps = (ArrayProperties)props.getPropertiesByName("ArrayProperties");
        scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        
        if (primitives.get("Key") instanceof String) {
          String[] KeyString = ((String) primitives.get("Key")).split("");
          int n = KeyString.length - 1;
          Key = new String[n];
          System.arraycopy(KeyString, 1, Key, 0, n);
        }
        else Key = (String[])primitives.get("Key");
       
        if (primitives.get("Message") instanceof String) {
          String[] MessageString = ((String) primitives.get("Message")).split("");
          int n = MessageString.length - 1;
          Message = new String[n];
          System.arraycopy(MessageString, 1, Message, 0, n);
        }
        else Message = (String[])primitives.get("Message");
        
        if (primitives.get("method") instanceof String) {
          method = Boolean.parseBoolean((String) primitives.get("method"));
        }
        else method = (Boolean)primitives.get("method");
        
        OneTimePadInit(Message, Key, method, props);
        return lang.toString();
    }
    
   	public void init(){
    	lang = new AnimalScript("One-Time-Pad", "Mateusz Umstaedter, Aidin Dinkhah, Jakub Pilarski", 800, 600);
    	lang.setStepMode(true);
        
    	TextProperties titleProp = new TextProperties();
		titleProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
					Font.BOLD, 20));
		
		// Erstellen des Titels
		Text title = lang.newText(new Coordinates(10, 35), "One-Time-Pad", "title", null, titleProp);
		
		RectProperties rectProp = new RectProperties();
		rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.FALSE);
		
		// Erstellen des umrandenden Rechtecks.
		Rect rect = lang.newRect(new Offset(-5, -5, title, AnimalScript.DIRECTION_NW), 
				new Offset(5, 5, title, AnimalScript.DIRECTION_SE), "rectangle", null, rectProp);
		
		rect.show();
		
		TextProperties descrhdProp = new TextProperties();
		descrhdProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 18));
		
		Text descrhd = lang.newText(new Coordinates(20, 80), "One-Time-Pad: Beschreibung", 
				"descrhd", null, descrhdProp);
		
		SourceCodeProperties descrProp = new SourceCodeProperties();
		descrProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
								    Font.PLAIN, 12));
		descrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		// Erstellen der Beschreibung.
		SourceCode descr = lang.newSourceCode(new Offset(0, 30, descrhd, AnimalScript.DIRECTION_S), "descr", null, descrProp);
		descr.addCodeLine("One-Time-Pad (oder abgekuerzt OTP) ist ein symmetrisches Verschluesselungsverfahren", null, 0, null);
		descr.addCodeLine("(beide Teilnehmer verwenden den gleichen Schluessel).", null, 0, null);
		descr.addCodeLine("Das Verfahren ist theoretisch informationssicher und konnte bis dato nicht gebrochen werden.", null, 0, null);
		descr.addCodeLine("Es gibt eine Nachricht N und einen dazugehoerigen Schlüssel S. ", null, 0, null);
		descr.addCodeLine("Dabei muss der Schluessel genau so lang sein wie die Nachricht.", null, 0, null);
		descr.addCodeLine("In diesem Beispiel wird dies anhand der buchstabenweise Addition von ", null, 0, null);
		descr.addCodeLine("dem Klartext und dem Schluessel erbracht.", null, 0, null);
		descr.addCodeLine("In dem Fall werden die Buchstaben in Zahlen umgewandelt ", null, 0, null);
		descr.addCodeLine("um die Addition bzw. Substitution zu erleichtern. Es werden die ASCII Zeichen A,B,...,Z kodiert.", null, 0, null);
		descr.addCodeLine("Beispiel 1) (A(1) + T(20)) Mod 26 = U(21)", null, 0, null);
		descr.addCodeLine("Beispiel 2) (X(24) + X(24)) Mod 26 = 48 Mod 26 = V(22)", null, 0, null);

		// Erstellen der zusätzlichen Beschreibung.
		SourceCode descr2 = lang.newSourceCode(new Offset(0, 220, descrhd, AnimalScript.DIRECTION_S), "descr2", null, descrProp);
		descr2.addCodeLine("Es gibt 3 Eingabeparameter:", null, 0, null);
		descr2.addCodeLine("MessageArray - ASCII Zeichen A,B,...,Z die als Nachricht dienen. <br />", null, 0, null);
		descr2.addCodeLine("SchluesselArray - ASCII Zeichen A,B,...,Z die als Schluessel dienn. <br />", null, 0, null);
		descr2.addCodeLine("method - Boolescher mit dem welchem man den ASCII Code verschluesselt (true) oder entschluesselt (false).", null, 0, null);
		
		lang.nextStep();
		
		descrhd.hide();
		descr.hide();
		descr2.hide();
    }

   	
    /**
     * Initialisiert den Algorithmus. Es werden die String Array Objekte erstellt,
     * Falls die Arrays unterschiedliche Laenge haben wird eine 1-Schritt Animation
     * erstellt. 
     * Amsonten wird die Verschluesselungs- / Entschluesselungs- Methode aufgerufen
     *  
     * @param message - Die zu verschluesselnde / entschluesselnde String Array. 
     * @param skey -  Der Schluessel String Array.
     * @param method - boolean Variable welche mit true verschluesselt und 
     * 					mit false entschluesselt.
     * @param props - ubergiebt die jeweiligen Text-, Array-, SourceCode- Einstellungen. 
     */
    private void OneTimePadInit(String[] message, String[] skey, boolean method,
			AnimationPropertiesContainer props) {
    	arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
        		props.get("ArrayProperties", AnimationPropertiesKeys.FONT_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
        		props.get("ArrayProperties", AnimationPropertiesKeys.COLOR_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
				props.get("ArrayProperties", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		    
		// Erstellen des leeren output Arrays
		String[] out = new String[message.length];
		for (int i=0; i<message.length; i++) {
			out[i] = " ";
		}
		// Erstellen der 3 Arrays fuer Message, Key und output. 
		StringArray key = lang.newStringArray(new Coordinates(600, 200), skey, "KeyArray", 
						null, arrayProps);
		StringArray msg = lang.newStringArray(new Coordinates(600, 300), message, "MessageArray", 
						null, arrayProps);
		StringArray output = lang.newStringArray(new Coordinates(700, 250), out, "EncArray", 
			       		null, arrayProps);
		
		// Kreis fuer Plus / Minus und Modulo
		CircleProperties circleProp = new CircleProperties();
		Circle circle1 = lang.newCircle(new Coordinates(626, 260), 12, "circle1", null, circleProp);
		Circle circle2 = lang.newCircle(new Offset(30, 0, circle1,  AnimalScript.DIRECTION_E), 
						12, "circle2", null, circleProp);
		
		/* Erstellung der 4 Linien:
		 * 1. Vom Message Array zum Kreis
		 * 2. Vom Key Array zum Kreis (mit Plus oder Minus)
		 * 3. Vom Kreis zum zweiten Kreis (mit Modulo)
		 * 4. Vom zweiten Kreis zum Output Array
		 */
		Node[] l1 = {new Offset(0, 0, key, AnimalScript.DIRECTION_S), 
					new Offset(0, 0, circle1, AnimalScript.DIRECTION_N)};
		Polyline line1 = lang.newPolyline(l1, "line1", null);
		
		
		Node[] l2 = {new Offset(0, 0, msg, AnimalScript.DIRECTION_N),
					new Offset(0, 0, circle1, AnimalScript.DIRECTION_S)};
		Polyline line2 = lang.newPolyline(l2, "line2", null);
				
		Node[] l3 = {new Offset(0, 0, circle1, AnimalScript.DIRECTION_E), 
					new Offset(0, 0, circle2, AnimalScript.DIRECTION_W)};
		Polyline line3 = lang.newPolyline(l3, "line3", null);
		
		Node[] l4 = {new Offset(0, 0, output, AnimalScript.DIRECTION_W), 
				new Offset(0, 0, circle2, AnimalScript.DIRECTION_E)};
		Polyline line4 = lang.newPolyline(l4, "line4", null);
		
		msg.show();
		key.show();
		line1.show();
		line2.show();
		line3.show();
		line4.show();
		
		// Erstellen des Source Codes
		scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
        		props.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
				props.get("sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));    
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
        		props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
		
		// Erstellen des "Grundcodes"
		SourceCode code = lang.newSourceCode(new Coordinates(10, 70), "code",
					   null, scProps);
	    code.addCodeLine("function OneTimePad (MessageArray, SchluesselArray, Methode)", null, 0, null);  // 0
		code.addCodeLine("if SchluesselArray.length == MessageArray.length then", null, 1, null); // 1
		code.addCodeLine("for i:=0 to MessageArray.length do", null, 2, null);  // 2
		code.addCodeLine("if Methode == Verschluesseln then", null, 3, null); // 3
		code.addCodeLine("output[i]=(MessageArray[i] + KeyArray[i]) % 26", null, 4, null); // 4
		code.addCodeLine("else if Methode == Entschluesseln", null, 3, null); // 5
		code.addCodeLine("if (MessageArray[i] - SchluesselArray[i]) < 26 then", null, 4, null); // 6
		code.addCodeLine("output[i]=(26 + (MessageArray[i] - KeyArray[i]) % 26", null, 5, null); // 7
		code.addCodeLine("else", null, 4, null); // 8
		code.addCodeLine("output[i]=(MessageArray[i] - KeyArray[i]) % 26", null, 5, null); // 9
		code.addCodeLine("endfor", null, 3, null); // 10
		code.addCodeLine("end", null, 1, null); // 11

		// Erstellen des Codes zur Verschluesselung
		SourceCode code2 = lang.newSourceCode(new Coordinates(10, 70), "code2",
				   null, scProps);
		code2.addCodeLine("function Encryption (MessageArray, SchluesselArray, Methode)", null, 0, null);  // 0
		code2.addCodeLine("if SchluesselArray.length == MessageArray.length then", null, 1, null); // 1
		code2.addCodeLine("for i:=0 to MessageArray.length do", null, 2, null);  // 2
		code2.addCodeLine("output[i]=(MessageArray[i] + KeyArray[i]) % 26", null, 3, null); // 3
		code2.addCodeLine("endfor", null, 2, null); // 4
		code2.addCodeLine("end", null, 1, null); // 5

		// Erstellen des Codes zur Entschluesselung
		SourceCode code3 = lang.newSourceCode(new Coordinates(10, 70), "code3",
						   null, scProps);
		code3.addCodeLine("function Decryption (MessageArray, SchluesselArray, Methode)", null, 0, null);  // 0
		code3.addCodeLine("if SchluesselArray.length == MessageArray.length then", null, 1, null); // 1
		code3.addCodeLine("for i:=0 to MessageArray.length do", null, 2, null);  // 2
		code3.addCodeLine("if (MessageArray[i] - SchluesselArray[i]) < 26 then", null, 3, null); // 3
		code3.addCodeLine("output[i]=(26 + (MessageArray[i] - KeyArray[i]) % 26", null, 4, null); // 4
		code3.addCodeLine("else", null, 3, null); // 5
		code3.addCodeLine("output[i]=(MessageArray[i] - KeyArray[i]) % 26", null, 4, null); // 6
		code3.addCodeLine("endfor", null, 2, null); // 7
		code3.addCodeLine("end", null, 1, null); // 8
		
		TextProperties plusminusProp = new TextProperties();
		plusminusProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 18));

		// Plus / Minus und Modulo das fuer die Animation benoetigt wird.
		Text plus = lang.newText(new Offset(8, 4, circle1, null), "+", "plus", null, plusminusProp);
		Text minus = lang.newText(new Offset(7, 0, circle1, null), "-", "minus", null, plusminusProp);
		Text modulo = lang.newText(new Offset(7, 0, circle2, null), "%", "modulo", null, plusminusProp);
		
		code.hide();
		code2.hide();
		code3.hide();
		plus.hide();
		minus.hide();
		
		/* Pruefe ob die Lange des Message und Key Arrays gleich lang sind.
		 * Falls ungleich wird eine 1-Schritt Animation erstellt,
		 * sonst falls "method" = true wird verschluesselung aufgerufen,
		 * bei "method" = false die entschluesselung aufgerufen. 
		 */
		if (message.length != skey.length) {
			code.show();
			code.highlight(0, 0, false);
			lang.nextStep();
			code.toggleHighlight(0, 0, false, 1, 0);
			
			Text error = lang.newText(new Offset(0, 300, code, null), 
					("Die Nachricht und der Schluessel sind nicht gleich lang!"), "error", null, varsProp);
			error.show();
			lang.nextStep();
		} else {
			if (method == true) {
				modulo.show();
				plus.show();
				code2.show();
				minus.hide();
				code2.highlight(0, 0, false);
				lang.nextStep("Verschluesselung");
				Encryption(msg, key, output, code2, props);
				
				String encryptString = "";
				for (int i = 0; i < output.getLength(); i++) {
				  encryptString += output.getData(i);
				}
				CheckpointUtils.checkpointEvent(this, "encyrptEvent", new Variable ("verschluesselt", encryptString));
				
			} else if (method == false) {
				modulo.show();
				code3.show();
				minus.show();
				plus.hide();
				code3.highlight(0, 0, false);
				lang.nextStep("Entschluesselung");
				Decryption(msg, key, output, code3, props);
				
				String decryptString = "";
        for (int i = 0; i < output.getLength(); i++) {
          decryptString += output.getData(i);
        }
        CheckpointUtils.checkpointEvent(this, "decryptEvent", new Variable ("entschluesselt", decryptString));
        
			}
		}
		
	}
   	
    
   	/**
   	 * Die Entschluesselungs-Methode wandelt die ASCII Zeichen in Integer um.
   	 * Danach wird der Wert berechnet mit: (Message - Key) % 26
   	 * Falls Message - Key < 0 muss nochmals 26 dazuaddiert werden,
   	 * also: (26 + (Message - Key)) % 26.
   	 * 
   	 * @param msg - Die zu verschluesselnde / entschluesselnde String Array.
   	 * @param key -  Der Schluessel String Array.
   	 * @param output - Der leere Ausgabe Array.
   	 * @param code3 - Der Source Code fuer die Animation.
   	 * @param props - ubergiebt die jeweiligen Text-, Array-, SourceCode- Einstellungen.
   	 */
   	private void Decryption(StringArray msg, StringArray key,
			StringArray output, SourceCode code3,
			AnimationPropertiesContainer props) {
   		varsProp = new TextProperties();
		varsProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
        		props.get("textProperties", AnimationPropertiesKeys.COLOR_PROPERTY));
        varsProp.set(AnimationPropertiesKeys.FONT_PROPERTY, 
        		props.get("textProperties", AnimationPropertiesKeys.FONT_PROPERTY));
        
        // Die jeweiligen Hilfsvariablen zur Darstellung der ASCII Zeichen.
		Text var1 = lang.newText(new Offset(0, 300, code3, null), "intMessage: ", "intMessagevar", null, varsProp);
		Text var2 = lang.newText(new Offset(0, 20, var1, null), "intKey: ", "intMessagevar", null, varsProp);
		Text var3 = lang.newText(new Offset(0, 20, var2, null), "intOutput: ", "intMessagevar", null, varsProp);
		String summaryText = "";

		// Variablen fuer das "Variablen Fenster"
		Variables vars = lang.newVariables();
		vars.declare("int", "intMessage");
		vars.declare("int", "intKey");
		vars.declare("int", "intOutput");
		vars.declare("String", "Message");
		vars.declare("String", "Output");
		vars.declare("String", "SKey");
		
		
		code3.toggleHighlight(0, 0, false, 1, 0);
		msg.highlightElem(0, msg.getLength() - 1, null, null);
		key.highlightElem(0, key.getLength() - 1, null, null);
		lang.nextStep();
		code3.toggleHighlight(1, 0, false, 2, 0);
		msg.unhighlightElem(0, msg.getLength() - 1, null, null);
		key.unhighlightElem(0, key.getLength() - 1, null, null);
		lang.nextStep();
		for(int i=0; i < msg.getLength(); i++){
			int tempN = 0;
			int tempS = 0;
			
			/* Konvertiere ASCII Zeichen zu Integer mit: "ASCII Zeichen" - 64,
			 * um für A = 1, B = 2, ... zu erhalten
			 * Gebe die konvertierten Integer mit var1.setText aus.
			 */
			if (msg.getData(i).toCharArray()[0] >= 'A' && msg.getData(i).toCharArray()[0] <= 'Z') {
					/* Wandeln und Speichern des ASCII Zeichens (Grossbuchstaben) 
				 	 * im Message Array als Integer. Gebe den resultierenden Integer
				 	 * mit var1 aus.
				 	 */
					tempN = (msg.getData(i).toCharArray()[0] - 64);
					var1.setText(("intMessage: " + tempN), null, null);
					vars.set("intMessage", String.valueOf(tempN));
					vars.set("Message", msg.getData(i));
			   } else if (msg.getData(i).toCharArray()[0] >= 'a' && msg.getData(i).toCharArray()[0] <= 'z') {
				   /* Wandeln und Speichern des ASCII Zeichens (Kleinbuchstaben) 
					* im Message Array als Integer.  Gebe den resultierenden Integer
				 	 * mit var1 aus.
					*/
				   tempN = (msg.getData(i).toCharArray()[0] - 70);
				   var1.setText(("intMessage: " + tempN), null, null);
				   vars.set("intMessage", String.valueOf(tempN));
				   vars.set("Message", msg.getData(i));
			   }
			if (key.getData(i).toCharArray()[0] >= 'A' && key.getData(i).toCharArray()[0] <= 'Z') {
					/* Wandeln und Speichern des ASCII Zeichens (Grossbuchstaben) 
				 	 * im Key Array als Integer.  Gebe den resultierenden Integer
				 	 * mit var2 aus.
				 	 */
					tempS = (key.getData(i).toCharArray()[0] - 64);
					var2.setText(("intKey: " + tempS), null, null);
					vars.set("intKey", String.valueOf(tempS));
				   	vars.set("SKey", key.getData(i));
			   } else if (key.getData(i).toCharArray()[0] >= 'a' && key.getData(i).toCharArray()[0] <= 'z') {
				   /* Wandeln und Speichern des ASCII Zeichens (Kleinbuchstaben) 
					* im Key Array als Integer.  Gebe den resultierenden Integer
				 	 * mit var2 aus.
					*/
				   tempS = (key.getData(i).toCharArray()[0] - 70);
				   var2.setText(("intKey: " + tempS), null, null);
				   vars.set("intKey", String.valueOf(tempS));
				   vars.set("SKey", key.getData(i));
			   }
			code3.toggleHighlight(2, 0, false, 3, 0);
			lang.nextStep();
			
			/* Falls Message - Key < 0 tritt ein Spezialfall ein,
			 * dann muss nochmals 26 dazuaddiert werden 
			 */
			if (tempN - tempS < 0) {
				code3.toggleHighlight(3, 0, false, 4, 0);
				
				// Berechne und wandle danach das Resultat wieder in String um.
				String soutput = String.valueOf((char)(((26 + (tempN - tempS)) % 26) + 64));
				
				// Speichern von dem Ergebnis im Output Array
				output.put(i, soutput, null, null);
				output.highlightElem(i, null, null);
				var3.setText(("intOutput: " + (26 + (tempN - tempS)) % 26), null, null);
				vars.set("intOutput", String.valueOf((26 + (tempN - tempS)) % 26));
			   	vars.set("Output", soutput);
				int tempi = i;
				lang.nextStep("Zelle " + (tempi + 1));
				
				// Hinzufuegen der Berechnung zur Zusammenfassung.
				summaryText += ("(" + msg.getData(i) + " - " + key.getData(i) + " = " + soutput + "), ");
				code3.toggleHighlight(4, 0, false, 2, 0);
				output.unhighlightElem(i, null, null);
				var1.setText("intMessage: ", null, null);
				var2.setText("intKey: ", null, null);
				var3.setText("intOutput: ", null, null);
				lang.nextStep();
			} else {
				code3.toggleHighlight(3, 0, false, 5, 0);
				lang.nextStep();
				code3.toggleHighlight(5, 0, false, 6, 0);
				
				// Berechne und wandle danach das Resultat wieder in String um.
				String soutput = String.valueOf((char)(((tempN - tempS) % 26) + 64));
				
				// Speichern von dem Ergebnis im Output Array
				output.put(i, soutput, null, null);
				output.highlightElem(i, null, null);
				var3.setText(("intOutput: " + (tempN - tempS) % 26), null, null);
				vars.set("intOutput", String.valueOf((tempN - tempS) % 26));
			   	vars.set("Output", soutput);
				
				int tempi = i;
				lang.nextStep("Zelle " + (tempi + 1));

				// Hinzufuegen der Berechnung zur Zusammenfassung.
				summaryText += ("(" + msg.getData(i) + " - " + key.getData(i) + " = " + soutput + "), ");
				code3.toggleHighlight(6, 0, false, 2, 0);
				output.unhighlightElem(i, null, null);
				var1.setText("intMessage: ", null, null);
				var2.setText("intKey: ", null, null);
				var3.setText("intOutput: ", null, null);
				lang.nextStep();
			}
			
		}
		// Zusammenfassung
		code3.toggleHighlight(2, 0, false, 7, 0);
		lang.nextStep();
		code3.toggleHighlight(7, 0, false, 8, 0);
		lang.nextStep();
		code3.unhighlight(8);
		var1.hide();
		var2.hide();
		var3.hide();
		Text summary = lang.newText(new Offset(0, 350, code3, null), 
						("Zusammenfassend: " + summaryText), "summary", null, varsProp);
		summary.show();
		lang.nextStep("Zusammenfassung");
	}

   	/**
   	 * Die Verschluesselungs-Methode wandelt die ASCII Zeichen in Integer um.
   	 * Danach wird der Wert berechnet mit: (Message + Key) % 26
   	 * 
   	 * @param msg - Die zu verschluesselnde / entschluesselnde String Array.
   	 * @param key -  Der Schluessel String Array.
   	 * @param output - Der leere Ausgabe Array.
   	 * @param code2 - Der Source Code fuer die Animation.
   	 * @param props - ubergiebt die jeweiligen Text-, Array-, SourceCode- Einstellungen.
   	 */
	private void Encryption(StringArray msg, StringArray key,
			StringArray output, SourceCode code2,
			AnimationPropertiesContainer props) {
		varsProp = new TextProperties();
		varsProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
        		props.get("textProperties", AnimationPropertiesKeys.COLOR_PROPERTY));
        varsProp.set(AnimationPropertiesKeys.FONT_PROPERTY, 
        		props.get("textProperties", AnimationPropertiesKeys.FONT_PROPERTY));
        
        // Die jeweiligen Hilfsvariablen zur Darstellung der ASCII Zeichen.
		Text var1 = lang.newText(new Offset(0, 300, code2, null), "intMessage: ", "intMessagevar", null, varsProp);
		Text var2 = lang.newText(new Offset(0, 20, var1, null), "intKey: ", "intMessagevar", null, varsProp);
		Text var3 = lang.newText(new Offset(0, 20, var2, null), "intOutput: ", "intMessagevar", null, varsProp);
		String summaryText = "";
		
		// Variablen fuer das "Variablen Fenster"
		Variables vars = lang.newVariables();
		vars.declare("int", "intMessage");
		vars.declare("int", "intKey");
		vars.declare("int", "intOutput");
		vars.declare("String", "SKey");
		vars.declare("String", "Message");
		vars.declare("String", "Output");
		
		code2.toggleHighlight(0, 0, false, 1, 0);
		msg.highlightElem(0, msg.getLength() - 1, null, null);
		key.highlightElem(0, key.getLength() - 1, null, null);
		lang.nextStep();
		code2.toggleHighlight(1, 0, false, 2, 0);
		msg.unhighlightElem(0, msg.getLength() - 1, null, null);
		key.unhighlightElem(0, key.getLength() - 1, null, null);
		lang.nextStep();
		for(int i=0; i < msg.getLength(); i++){
			int tempN = 0;
			int tempS = 0;
			
			/* Konvertiere ASCII Zeichen zu Integer mit: "ASCII Zeichen" - 64,
			 * um für A = 1, B = 2, ... zu erhalten
			 * Gebe die konvertierten Integer mit var1.setText aus.
			 */
			if (msg.getData(i).toCharArray()[0] >= 'A' && msg.getData(i).toCharArray()[0] <= 'Z') {
					/* Wandeln und Speichern des ASCII Zeichens (Grossbuchstaben) 
				 	 * im Message Array als Integer. Gebe den resultierenden Integer
				 	 * mit var1 aus.
				 	 */
					tempN = (msg.getData(i).toCharArray()[0] - 64);
					var1.setText(("intMessage: " + tempN), null, null);
					vars.set("intMessage", String.valueOf(tempN));
					vars.set("Message", msg.getData(i));
			   } else if (msg.getData(i).toCharArray()[0] >= 'a' && msg.getData(i).toCharArray()[0] <= 'z') {
				   /* Wandeln und Speichern des ASCII Zeichens (Kleinbuchstaben) 
					* im Message Array als Integer.  Gebe den resultierenden Integer
				 	* mit var1 aus.
					*/
				   tempN = (msg.getData(i).toCharArray()[0] - 70);
				   var1.setText(("intMessage: " + tempN), null, null);
				   vars.set("intMessage", String.valueOf(tempN));
				   vars.set("Message", msg.getData(i));
			   }
			if (key.getData(i).toCharArray()[0] >= 'A' && key.getData(i).toCharArray()[0] <= 'Z') {
					/* Wandeln und Speichern des ASCII Zeichens (Grossbuchstaben) 
				 	 * im Key Array als Integer.  Gebe den resultierenden Integer
				 	 * mit var2 aus.
				 	 */
				   	tempS = (key.getData(i).toCharArray()[0] - 64);
				   	var2.setText(("intKey: " + tempS), null, null);
				   	vars.set("intKey", String.valueOf(tempS));
				   	vars.set("SKey", key.getData(i));
			   } else if (key.getData(i).toCharArray()[0] >= 'a' && key.getData(i).toCharArray()[0] <= 'z') {
				   /* Wandeln und Speichern des ASCII Zeichens (Kleinbuchstaben) 
					* im Key Array als Integer.  Gebe den resultierenden Integer
				 	* mit var2 aus.
					*/
				   tempS = (key.getData(i).toCharArray()[0] - 70);
				   var2.setText(("intKey: " + tempS), null, null);
				   vars.set("intKey", String.valueOf(tempS));
				   vars.set("SKey", key.getData(i));
			   }
			code2.toggleHighlight(2, 0, false, 3, 0);
			
			// Berechne und wandle danach das Resultat wieder in String um.
			String soutput = String.valueOf((char)(((tempN + tempS) % 26) + 64));
			
			// Speichern von dem Ergebnis im Output Array
			output.put(i, soutput, null, null);
			output.highlightElem(i, null, null);
			var3.setText(("intOutput: " + ((tempN + tempS) % 26)), null, null);
			vars.set("intOutput", String.valueOf(((tempN + tempS) % 26)));
		   	vars.set("Output", soutput);
			int tempi = i;
			lang.nextStep("Zelle " + (tempi + 1));
			
			// Hinzufuegen der Berechnung zur Zusammenfassung.
			summaryText += ("(" + msg.getData(i) + " + " + key.getData(i) + " = " + soutput + "), ");
			code2.toggleHighlight(3, 0, false, 2, 0);
			output.unhighlightElem(i, null, null);
			var1.setText("intMessage: ", null, null);
			var2.setText("intKey: ", null, null);
			var3.setText("intOutput: ", null, null);
			lang.nextStep();  
		}
		// Zusammenfassung
		code2.toggleHighlight(2, 0, false, 4, 0);
		lang.nextStep();
		code2.toggleHighlight(4, 0, false, 5, 0);
		lang.nextStep();
		code2.unhighlight(5);
		var1.hide();
		var2.hide();
		var3.hide();
		Text summary = lang.newText(new Offset(0, 350, code2, null), 
						("Zusammenfassend: " + summaryText), "summary", null, varsProp);
		summary.show();
		lang.nextStep("Zusammenfassung");
	
	}

	public String getName() {
        return "One-Time Pad";
    }

    public String getAlgorithmName() {
        return "One-Time Pad";
    }

    public String getAnimationAuthor() {
        return "Mateusz Umstädter, Aidin Dinkhah, Jakub Pilarski";
    }

    public String getDescription(){
        return DESCRIPTION;
    }

    public String getCodeExample(){
        return SOURCE_CODE;
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}