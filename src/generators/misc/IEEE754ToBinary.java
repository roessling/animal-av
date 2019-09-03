/*
 * IEEE754ToBinary.java
 * Alymbek Sadybakasov, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.variables.VariableRoles;

public class IEEE754ToBinary implements Generator {
    private Language lang;
    private ArrayProperties arrayProps;
    private SourceCodeProperties sourceCodeProps;
    private double Eingabe;
    
    private Timing defaultTiming = new TicksTiming(15);
	private Timing nullTiming = new TicksTiming(0);
	
	Coordinates algoNameCoordinates = new Coordinates(10, 30); 
	Coordinates descriptionTextCoordinates = new Coordinates(10, 50); 
	Coordinates sourceCodeCoordinates = new Coordinates(10, 180);
	Coordinates arrayTextCoordinates = new Coordinates(10, 70); 
	Coordinates resultArrayCoordinates = new Coordinates(10, 150); 
	Coordinates inputCoordinates = new Coordinates(430, 30); 
	Coordinates lastTextCoordinates = new Coordinates(490, 250);
	Coordinates firstLineCoordinates = new Coordinates(430, 60); 
	Coordinates secondLineCoordinates = new Coordinates(430, 90);
	Coordinates thirdLineCoordinates = new Coordinates(430, 120); 
	Coordinates fourthLineCoordinates = new Coordinates(430, 150);
	
	Font algoNameFont = new Font("SansSerif", Font.BOLD, 26); 
	Font inputStringFont = new Font("SansSerif", Font.BOLD, 16);
	Font mainFont = new Font("SansSerif", Font.PLAIN, 16);
	
	Text firstLineText;
	Text secondLineText;
	Text thirdLineText;
	Text fourthLineText;
	Text fifthLineText;
	Text sixthLineText;
	
	SourceCode sc;
	Text arrayText;
	StringArray resultArray;
	Variables v;

    public void init(){
        lang = new AnimalScript("IEEE754 Dezimal zu Binär [DE]", "Alymbek Sadybakasov", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        Eingabe = (double)primitives.get("Eingabe");
        
        getDecimal32((float) Eingabe);
        
        return lang.toString();
    }

    private String getSign(double number){
    	String sign;
    	
    	firstLineText = lang.newText(firstLineCoordinates, "Das Vorzeichen wird mit 0 für positive Zahlen "
    			+ "und 1 für negative Zahlen codiert", "firstLineText", null);
    	firstLineText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	
    	if (number < 0){
    		sign = "1";
    		
        	thirdLineText = lang.newText(thirdLineCoordinates, "Die Eingabezahl ist negativ -> erster Bit ist 1", 
        				"thirdLineText", null);
        	sc.highlight(3);
           	sc.highlight(4);
           	v.declare("String", "Vorzeichen", "1", animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
        }
    		
    	else {
    		sign = "0";
    		sc.highlight(1);
        	sc.highlight(2);
    		thirdLineText = lang.newText(thirdLineCoordinates, "Die Eingabezahl ist positiv -> erster Bit ist 0", 
    				"thirdLineText", null);
    		v.declare("String", "Vorzeichen", "0", animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
    	}
    	thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	
    	// put the bit for sign
    	arrayText = lang.newText(arrayTextCoordinates, "die " + sign + " wird im ersten Bit gespeichert ", 
    			"arrayText", null);
    	arrayText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	
    	resultArray.put(0, sign, defaultTiming, defaultTiming);
    	
    	return sign;
    }
   
	
    private static String getInt(double number){
    	int temp = Math.abs((int)number);
    	return Integer.toString(temp);
    }
    
    private static String getDec(float number){
    	String string = Float.toString(number);
    	char[] charArray = string.toCharArray();
       	char[] binDecTemp = null;
    	for (int i = 0; i < charArray.length; i++){
    		if (charArray[i] == '.'){
    			binDecTemp = new char[charArray.length - i - 1];
    			for (int j = i + 1; j < charArray.length; j++){
    				binDecTemp[j - i - 1] = charArray[j];
    			}	
    		}
    		
    	}
    	
    	string = String.copyValueOf(binDecTemp);
    	
    	return string;
    }
	
    private void showLastText(){
    	sc.hide();
    	sc = lang.newSourceCode(sourceCodeCoordinates, "lastText", null, sourceCodeProps);
    	sc.addCodeLine("IEEE 754 unterscheidet zunächst zwischen binären Rundungen und binär-", null, 0, null);
		sc.addCodeLine("dezimalen Rundungen, bei denen geringere Qualitätsforderungen gelten.", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("Bei binären Rundungen muss zur nächstgelegenen darstellbaren Zahl gerundet ", null, 0, null);
		sc.addCodeLine("werden. Wenn diese nicht eindeutig definiert ist (genau in der Mitte ", null, 0, null);
		sc.addCodeLine("zwischen zwei darstellbaren Zahlen), wird so gerundet, dass das nieder- ", null, 0, null);
		sc.addCodeLine("wertigste Bit der Mantisse 0 wird. Statistisch wird dabei in 50 % der ", null, 0, null);
		sc.addCodeLine("Fälle auf-, in den anderen 50 % der Fälle abgerundet, so dass die von ", null, 0, null);
		sc.addCodeLine("Knuth beschriebene statistische Drift in längeren Rechnungen vermieden wird.", null, 0, null);
    	
    }
    
    
	// Convert the 32-bit binary into the decimal  
    private void getDecimal32(float input)  
    {  

    	v = lang.newVariables();
    	String b = getInt(input);
    	String c = getDec(input);
    	
    	// create new String with filled 0's, that will hold the end result
    	StringBuffer outputBuffer = new StringBuffer(32);
    	for (int i = 0; i < 32; i++){
    	   outputBuffer.append("0");
    	}
    	String endStringTemp = outputBuffer.toString();
    	String[] endString = endStringTemp.split("");
    	
    	
    	// Animation starts here
    	Text algoName = lang.newText(algoNameCoordinates, "IEEE 754 Dezimal zu Binär", "algoName", null);
    	algoName.setFont(algoNameFont, nullTiming, nullTiming);
    	lang.nextStep();
    	
    	// Define and initialize description field
    	SourceCode description = lang.newSourceCode(descriptionTextCoordinates, "description",
				   null, sourceCodeProps);
        description.addCodeLine(
        		"Die Norm IEEE 754 (ANSI/IEEE Std 754-1985; IEC-60559:1989 – International version)", null, 0, null);
        description
            .addCodeLine("definiert Standarddarstellungen für binäre Gleitkommazahlen in Computern und legt", 
            		null, 0, null);
        description.addCodeLine("genaue Verfahren für die Durchführung mathematischer Operationen, insbesondere "
        		+ "für", null, 0, null);
        description.addCodeLine("Rundungen, fest. Der genaue Name der Norm ist englisch IEEE Standard for Binary ", 
        		null, 0, null);
        description
            .addCodeLine("Floating-Point Arithmetic for microprocessor systems (ANSI/IEEE Std 754-1985)", null, 0, 
            		null);
        description.addCodeLine("Quelle: https://de.wikipedia.org/wiki/IEEE_754", null, 0, null);
        
    	
    	lang.nextStep();
    	
    	// pseudocode
    	sc = lang.newSourceCode(sourceCodeCoordinates, "sourceCode", null, sourceCodeProps);
    	sc.addCodeLine("Pseudocode:", null, 0, null);
    	sc.addCodeLine("1.1 Vorzeichen positiv", null, 0, null);
    	sc.addCodeLine("erster Bit = 0", null, 3, null);
    	sc.addCodeLine("1.2 Vorzeichen negativ", null, 0, null);
    	sc.addCodeLine("erster Bit = 1", null, 3, null);
    	sc.addCodeLine("2. Konvertiere den ganzzahligen Anteil in binär", null, 0, null);
    	sc.addCodeLine("3. Konvertiere den gebrochenen Anteil in binär", null, 0, null);
    	sc.addCodeLine("3.1 Multipliziere den Rest mit 2", null, 3, null);
    	sc.addCodeLine("3.2 rest >= 1", null, 3, null);
    	sc.addCodeLine("rest = rest - 1", null, 6, null);
    	sc.addCodeLine("hänge eine 1 an", null, 6, null);
    	sc.addCodeLine("3.3 rest < 1", null, 3, null);
    	sc.addCodeLine("hänge eine 0 an.", null, 6, null);
    	sc.addCodeLine("3.3 rest = 0", null, 3, null);
    	sc.addCodeLine("break", null, 6, null);
    	sc.addCodeLine("4. Notiere das Ergebniss als Festkomma-Zahl", null, 0, null);
    	sc.addCodeLine("5. Normalisiere die Festkomma-Zahl ", null, 0, null);
    	sc.addCodeLine("das Komma wird so weit nach rechts oder links verschoben,", 
    			null, 3, null);
    	sc.addCodeLine("bis vor dem Komma eine 1 steht", null, 3, null);
    	description.hide();
    	
    	// show input double 
    	Text inputDouble = lang.newText(inputCoordinates, "Die zu konvertierende Zahl ist: " + Float.toString(input), 
    			"inputDouble", null);
    	inputDouble.setFont(inputStringFont, nullTiming, nullTiming);
    	inputDouble.show();
    	
    	// show initial Array
    	resultArray = lang.newStringArray(resultArrayCoordinates, endString, "resultArray", null, arrayProps); // holds the result
    	
    	lang.nextStep();
    	
    	// sign
    	resultArray.highlightCell(0, defaultTiming, defaultTiming);
    	lang.nextStep("Vorzeichen");
    	getSign(input);
    	
    	// decimal
    	lang.nextStep("Konvertierung des ganzzahligen Anteils");
    	arrayText.hide();
    	thirdLineText.hide();
    	firstLineText.hide();
    	sc.unhighlight(1);
    	sc.unhighlight(2);
    	sc.unhighlight(3);
    	sc.unhighlight(4);
    	resultArray.unhighlightCell(0, defaultTiming, defaultTiming);
    	
    	int integerPart = Integer.parseInt(b);
    	char[] intArray = Integer.toBinaryString(integerPart).toCharArray();
    	firstLineText = lang.newText(firstLineCoordinates, "Konvertierung des ganzzahligen Anteils", 
    			"firstLineText", null);
    	firstLineText.setFont(mainFont, nullTiming, nullTiming);
    	sc.highlight(5);
    	lang.nextStep();
    	
    	thirdLineText = lang.newText(thirdLineCoordinates, "Die ganze Zahl " + b + " ist in binär " + 
    			Integer.toString(integerPart, 2), "thirdLineText", null);
    	thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep("Konvertierung des gebrochenen Anteils");
    	thirdLineText.hide();
    	firstLineText.hide();
    	sc.unhighlight(5);
    	firstLineText = lang.newText(firstLineCoordinates, "Konvertierung des gebrochenen Anteils in Binär.", 
    			"firstLineText", null);
    	firstLineText.setFont(mainFont, nullTiming, nullTiming);
    	sc.highlight(6);
    	lang.nextStep();
    	
    	// convert Decimal to Bit
    	int[] restArray;
    	restArray = new int[30];
    	c = "0." + c;
    	Float rest = Float.parseFloat(c);
    	String restArrayString = ""; 
    	String restString = rest.toString();
    	thirdLineText = lang.newText(thirdLineCoordinates, "rest = " + restString, "thirdLineText", null); 
    	fourthLineText = lang.newText(fourthLineCoordinates, restArrayString, "thirdLineText", null);
    	thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    	fourthLineText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	for (int i = 0; i < restArray.length; i++){
    		if (rest == 0){
    			sc.highlight(13);
    			sc.highlight(14);
    			lang.nextStep();
    			sc.unhighlight(13);
    			sc.unhighlight(14);
    			break;
    		}
    		thirdLineText.hide();
    		rest = rest * 2;
    		v.declare("Double", "rest", rest.toString(), animal.variables.Variable.getRoleString(VariableRoles.TEMPORARY));
    		restString = rest.toString();
    		sc.highlight(7);
    		thirdLineText = lang.newText(thirdLineCoordinates, "rest = " + restString, "thirdLineText", null);
    		thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    		lang.nextStep();
    		sc.unhighlight(7);
    		if (rest >= 1){
    			sc.highlight(8);
    			sc.highlight(9);
    			thirdLineText.hide();
    			rest = rest - 1;
    			v.declare("Double", "rest", rest.toString(), animal.variables.Variable.getRoleString(VariableRoles.TEMPORARY));
    			restString = rest.toString();
    			thirdLineText = lang.newText(thirdLineCoordinates, "rest = " + restString, "thirdLineText", null);
    			thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    			lang.nextStep();
    			
    			sc.unhighlight(9);
    			sc.highlight(10);
    			restArray[i] = 1;
    			restArrayString = restArrayString + "1";
    			fourthLineText.hide();
    			fourthLineText = lang.newText(fourthLineCoordinates, "1 anhängen => " + restArrayString, "fourthLineText", null);
    			fourthLineText.setFont(mainFont, nullTiming, nullTiming);
    		}
    		else if (rest < 1){
    			sc.highlight(11);
    			sc.highlight(12);
    			restArray[i] = 0;
    			restArrayString = restArrayString + "0";
    			fourthLineText.hide();
    			fourthLineText = lang.newText(fourthLineCoordinates, "0 anhängen => " + restArrayString, "fourthLineText", null);
    			fourthLineText.setFont(mainFont, nullTiming, nullTiming);
    		}	
    		
    		lang.nextStep();
    		sc.unhighlight(8);
    		sc.unhighlight(10);
    		sc.unhighlight(11);
			sc.unhighlight(12);
    	}  	
    	
    	// fraction
    	StringBuilder builder = new StringBuilder();
    	for (int value : restArray) {
    	    builder.append(value);
    	}
    	String formatedString = builder.toString();
    	secondLineText = lang.newText(secondLineCoordinates, "Der Nachkommanteil: " + c + " ist in Binär " 
    			+ formatedString, "secondLineText", null);
    	secondLineText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep("Normalisierung der Festkommazahl");
    	firstLineText.hide();
    	secondLineText.hide();
    	thirdLineText.hide();
    	fourthLineText.hide();
    	sc.unhighlight(6);

    	// convert to fixpoint format
    	StringBuffer temp = new StringBuffer();
    	for (int i = 0; i < intArray.length; i++) {
    	   temp.append( intArray[i]);
    	}
    	temp.append(".");
    	for (int i = 0; i < restArray.length; i++) {
     	   temp.append( restArray[i] );
     	}
    	String number = temp.toString();
    	// fixed point representation
    	firstLineText = lang.newText(firstLineCoordinates, "Das Ergebnis wird als Festkomma-Zahl notiert", 
    			"firstLineText", null);
    	firstLineText.setFont(mainFont, nullTiming, nullTiming);
    	sc.highlight(15);
    	lang.nextStep();
    	
    	secondLineText = lang.newText(secondLineCoordinates, "Festkommadarstellung: " + number, "thirdLineText", null);
    	secondLineText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	firstLineText.hide();
    	sc.unhighlight(15);  	
    	
    	// find comma position
    	int commaPos = 0;
    	char[] charArray = number.toCharArray();
    	for (int i = 0; i < number.length(); i ++){
    		if (charArray[i] == '.'){
    			commaPos = i;
    			break;
    			}
    		}
    	// normalize
    	char temp1;
    	char temp2;
    	int e = 0;
    	
    	if (charArray[0] == '1'){
	    	for (int i = commaPos; i > 1; i--){
	    		temp2 = charArray[i];
	    		temp1 = charArray[i - 1]; 
	    		charArray[i] = temp1;
	    		charArray[i - 1] = temp2;
	    		e++;
	    	}
    	}
    	else if (charArray[1] == '.'){
    		for (int i = commaPos; i < 32; i++){
    			temp1 = charArray[i];
    			temp2 = charArray[i + 1];
    			charArray[i] = temp2;
    			charArray[i + 1] = temp1;
    			e--;
    			if (charArray[i] == '1'){
    				break;
    			}
    		}
    	}
    	
    	
    	// normalization
    	firstLineText = lang.newText(firstLineCoordinates, "Das Ergebnis wird als Festkomma-Zahl notiert und danach "
    			+ "normalisiert", "firstLineText", null);
    	firstLineText.setFont(mainFont, nullTiming, nullTiming);
    	sc.highlight(16);
    	sc.highlight(17);
    	sc.highlight(18);
    	lang.nextStep();
    	
    	thirdLineText = lang.newText(thirdLineCoordinates, "Normalisierte Festkommadarstellung: " + 
    			String.copyValueOf(charArray) + " * 2^" + e, "secondLineText", null);
    	thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    	
    	// exponent
    	lang.nextStep("Exponente");
    	secondLineText.hide();
    	thirdLineText.hide();
    	secondLineText = lang.newText(secondLineCoordinates, "Normalisierte Festkommadarstellung: " + 
    			String.copyValueOf(charArray) + " * 2^" + e, "secondLineText", null);
    	secondLineText.setFont(mainFont, nullTiming, nullTiming);
    	
    	firstLineText.hide();
    	sc.unhighlight(16);
    	sc.unhighlight(17);
    	sc.unhighlight(18);
 
    	// compute e
    	int tempE = 127 + e;
    	resultArray.highlightCell(1, 8, defaultTiming, defaultTiming);
    	
    	sc.hide();
    	sc = lang.newSourceCode(sourceCodeCoordinates, "sourceCode", null, sourceCodeProps);
    	sc.addCodeLine("Pseudocode:", null, 0, null);
    	sc.addCodeLine("6. Die Anzahl der Stellen, um die das Komma verschoben wird,", null, 0, null);
    	sc.addCodeLine(" ergibt den Wert des vorläufigen Exponenten e", null, 0, null);
    	sc.addCodeLine("das Komma nach rechts verschoben -> e ist negativ", null, 3, null);
    	sc.addCodeLine("das Komma nach links verschoben -> e ist positiv", null, 3, null);
    	sc.addCodeLine("7. Kodiere den Exponenten", null, 0, null);
    	sc.addCodeLine("e' = e + 127", null, 3, null);
    	sc.addCodeLine("speichere e' in die Bits 1-8", null, 3, null);
    	sc.addCodeLine("8. Speichere die Nachkommastellen der Festkomma-Zahl in die Bits 9-31", null, 0, null);
    	sc.highlight(1);
    	sc.highlight(2);
    	sc.highlight(3);
    	
    	// show exponent in binary
    	String tempBinExp = Integer.toString(tempE, 2);
    	char[] binExp = tempBinExp.toCharArray();
    	firstLineText = lang.newText(firstLineCoordinates, "Die Exponente wird berechnet", "firstLineText", null);
    	firstLineText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	sc.unhighlight(1);
    	sc.unhighlight(2);
    	sc.unhighlight(3);
    	sc.highlight(5);
    	sc.highlight(6);
    	thirdLineText = lang.newText(thirdLineCoordinates, e + " -> " + "e = " + e + " + 127 = " + tempE + " = " + tempBinExp, "thirdLineText", null);
    	thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    	v.declare("String", "Exponente", tempBinExp, animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
    	lang.nextStep();
    	
    	// put exponent into array
    	firstLineText.hide();
    	sc.unhighlight(6);
    	sc.highlight(7);
    	arrayText = lang.newText(arrayTextCoordinates, "Die Exponente wird in 1-8 Bits gespeichert", "arrayText", null);
    	arrayText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	
    	if (binExp.length == 7){
	    	for (int i = 1; i < 8; i++){
	    		resultArray.put(i+1, Character.toString(binExp[i-1]), nullTiming, nullTiming);
	    	}
    	}
    	else if (binExp.length == 8){
    		for (int i = 1; i < 9; i++){
	    		resultArray.put(i, Character.toString(binExp[i-1]), nullTiming, nullTiming);
	    	}
    	}
    	
    	// mantissa
    	lang.nextStep("Mantisse");
    	arrayText.hide();
    	thirdLineText.hide();
    	sc.unhighlight(5);
    	sc.unhighlight(7);
    	resultArray.unhighlightCell(1, 8, defaultTiming, defaultTiming);
    	
    	// determine the position of comma in normalized number
    	for (int i = 0; i < charArray.length; i++){
    		if (charArray[i] == '.'){
    			commaPos = i;
    			break;
    			}
    		}
    	
    	// extract decimal from normalized number
    	char[] binDecTemp;
    	binDecTemp = new char[charArray.length - commaPos - 1];
    	commaPos++;
    	for (int i = 0; i < charArray.length - commaPos; i++){
    		binDecTemp[i] = charArray[commaPos + i];
    	}
    	char[] binDecTemp1 = new char[22];
    	for (int i = 0; i < binDecTemp1.length; i++){
    		binDecTemp1[i] = binDecTemp[i];
    	}
    	System.arraycopy(charArray, commaPos-2, charArray, 0, charArray.length-commaPos);
    	
    	// highlight mantisse
    	arrayText = lang.newText(arrayTextCoordinates, "Die Mantisse wird extrahiert", "arrayText", null);
    	arrayText.setFont(mainFont, nullTiming, nullTiming);
    	resultArray.highlightCell(9, 31, defaultTiming, defaultTiming);
    	sc.highlight(8);
    
    	// show mantisse in binary
    	firstLineText = lang.newText(firstLineCoordinates, "Normalisierte Festkommadastellung: " + String.copyValueOf(charArray), "firstLineText", null);
    	firstLineText.setFont(mainFont, nullTiming, nullTiming);
    	secondLineText.hide();
    	lang.nextStep();
    	thirdLineText = lang.newText(thirdLineCoordinates, "Die Zahlen hinter der Komma ist unsere Mantisse " + String.copyValueOf(binDecTemp), "thirdLineText", null);
    	thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    	
    	// put mantissa into result String
    	lang.nextStep();
    	arrayText.hide();
    	arrayText = lang.newText(arrayTextCoordinates, "Die Mantisse wird in die restlichen Bits gespeichert", "arrayText", null);
    	arrayText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	
    	for (int i = 9; i < 32; i++){
    		resultArray.put(i, Character.toString(binDecTemp[i-9]), nullTiming, nullTiming);
    	}
    	v.declare("String", "Mantisse", new String(binDecTemp), animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
    	lang.nextStep();
    	
    	// unhighlight mantisse
    	arrayText.hide();
    	thirdLineText.hide();
    	firstLineText.hide();
    	resultArray.unhighlightCell(8, 31, defaultTiming, defaultTiming);
    	
    	// end
    	arrayText = lang.newText(arrayTextCoordinates, "Das Endergebniss sieht nun so aus ", "arrayText", null);
    	arrayText.setFont(mainFont, nullTiming, nullTiming);
    	resultArray.highlightCell(0, defaultTiming, defaultTiming);
    	resultArray.highlightCell(1, 8, defaultTiming, defaultTiming);
    	resultArray.highlightCell(9, 31, defaultTiming, defaultTiming);
    	for (int i = 1; i < 7; i++){
    		sc.highlight(i);
    	}
    
    	// show closing text
    	showLastText();
    }
    
    
    public String getName() {
        return "IEEE754 Dezimal zu Binär [DE]";
    }

    public String getAlgorithmName() {
        return "IEEE754 Dezimal zu Binär [DE]";
    }

    public String getAnimationAuthor() {
        return "Alymbek Sadybakasov";
    }

    public String getDescription(){
        return "Die Norm IEEE 754 (ANSI/IEEE Std 754-1985; IEC-60559:1989 – International version) definiert "
 +"\n"
 +"Standarddarstellungen für binäre Gleitkommazahlen in Computern und legt genaue Verfahren "
 +"\n"
 +"für die Durchführung mathematischer Operationen, insbesondere für Rundungen, fest. Der "
 +"\n"
 +"genaue Name der Norm ist englisch IEEE Standard for Binary Floating-Point Arithmetic for "
 +"\n"
 +"microprocessor systems (ANSI/IEEE Std 754-1985)."
 +"\n"
 +"\n";
    }

    public String getCodeExample(){
        return "1.1 Wenn das Vorzeichen positiv ist"
 +"\n"
 +"	setze den ersten Bit auf 0"
 +"\n"
 +"1.2 wenn das Vorzeichen negativ ist"
 +"\n"
 +"	setze den ersten Bit auf 1"
 +"\n"
 +"2. Konvertiere den ganzzahligen Anteil in binär"
 +"\n"
 +"3. Konvertiere den gebrochenen Anteil in binär"
 +"\n"
 +"4. Notiere das Ergebniss als Festkomma-Zahl"
 +"\n"
 +"5. Normalisiere die Festkomma-Zahl "
 +"\n"
 +"	das Komma wird so weit nach rechts oder links verschoben, bis vor dem "
 +"\n"
 +"	Komma eine 1 steht"
 +"\n"
 +"6. Die Anzahl der Stellen, um die das Komma verschoben wird, ergibt den Wert des vorläufigen "
 +"\n"
 +"Exponenten e"
 +"\n"
 +"	das Komma nach rechts verschoben -> e ist negativ"
 +"\n"
 +"	das Komma nach links verschoben -> e Wert ist positiv"
 +"\n"
 +"7. Kodiere den Exponenten"
 +"\n"
 +"	e' = e + 127"
 +"\n"
 +"	speichere e' in die Bits 1-8	"
 +"\n"
 +"8. Speichere die Nachkommastellen der Festkomma-Zahl in die Bits 9-31";
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

}