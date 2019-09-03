package generators.misc;

/*
 * IEEE754ToDecimal.java
 * Alymbek Sadybakasov, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */


import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.variables.VariableRoles;

public class IEEE754ToDecimal implements ValidatingGenerator {
    private Language lang;
    private ArrayProperties arrayProps;
    private ArrayMarkerProperties markerProps;
    private SourceCodeProperties sourceCodeProps;
    private String Eingabe;
    
    private Coordinates firstLineCoordinates = new Coordinates(430, 60);
    private Coordinates secondLineCoordinates = new Coordinates(430, 90);
    private Coordinates thirdLineCoordinates = new Coordinates(430, 120);
    private Coordinates fourthLineCoordinates = new Coordinates(430, 150);
    private Coordinates fifthLineCoordinates = new Coordinates(430, 180);
    private Coordinates sixthLineCoordinates = new Coordinates(430, 210);
    private Coordinates resultArrayCoordinates = new Coordinates(10, 150);
    private Coordinates sourceCodeCoordinates = new Coordinates(10, 180); 
    private Coordinates descriptionTextCoordinates = new Coordinates(10, 50);
    private Coordinates algoNameCoordinates = new Coordinates(10, 30);
    private Coordinates lastTextCoordinates = new Coordinates(430, 250);
    
    private Timing defaultTiming = new TicksTiming(15);
	private Timing nullTiming = new TicksTiming(0);
	
	private Font algoNameFont = new Font("SansSerif", Font.BOLD, 26); 
	private Font mainFont = new Font("SansSerif", Font.PLAIN, 16);
	
	private Text firstLineText;
	private Text secondLineText;
	private Text thirdLineText;
	private Text fourthLineText;
	private Text fifthLineText;
	private Text sixthLineText;
	
	private SourceCode sc;
	
	private StringArray resultArray;
	private ArrayMarker marker;
	private Variables v;

    public void init(){
        lang = new AnimalScript("IEEE 754 Binär zu Dezimal", "Alymbek Sadybakasov", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        markerProps = (ArrayMarkerProperties)props.getPropertiesByName("markerProps");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        Eingabe = (String)primitives.get("Eingabe");
        
        getFloat32(Eingabe);
        
        return lang.toString();
    }
    
    private void showInitText(){
    	Text algoName = lang.newText(algoNameCoordinates, "IEEE 754 Binär zu Dezimal", "algoName", null);
    	algoName.setFont(algoNameFont, nullTiming, nullTiming);
    	
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
	    description.hide();
    }
    
private int getSign(String input){
    	
    	int output = 0;
    	sc.highlight(2);
    	resultArray.highlightCell(0, defaultTiming, defaultTiming);
    	marker = lang.newArrayMarker(resultArray, 0, "marker", null, markerProps);
    	lang.nextStep();
    	sc.unhighlight(2);
    	if (input.equals("0")){
    		sc.highlight(5);
    		sc.highlight(6);
    		output = 1;
    	}
    	else if (input.equals("1")) {
    		sc.highlight(3);
    		sc.highlight(4);
    		output = -1;
    	}
    	
    	secondLineText = lang.newText(secondLineCoordinates, "Erstes Bit ist " + input, "secondLineText", null);
    	thirdLineText = lang.newText(thirdLineCoordinates, "Vorzeichen = " + output, "thirdLineText", null);
    	secondLineText.setFont(mainFont, nullTiming, nullTiming);
    	thirdLineText.setFont(mainFont, nullTiming, nullTiming);
    	lang.nextStep();
    	secondLineText.hide();
    	resultArray.unhighlightCell(0, defaultTiming, defaultTiming);
    	marker.hide();
    	sc.unhighlight(1);
    	sc.unhighlight(2);
    	sc.unhighlight(3);
    	sc.unhighlight(4);
    	sc.unhighlight(5);
    	sc.unhighlight(6);
    	return output;
    }
    
    
private double getExponent(String input){
	
	double result = 0;
	sc.highlight(8);
	firstLineText = lang.newText(firstLineCoordinates, "2^pos", "firstLineText", null);
	secondLineText = lang.newText(secondLineCoordinates, "Exponente + temp[pos] * 2 ^ pos", "secondLineText", null);
	fourthLineText = lang.newText(fourthLineCoordinates, "Exponente = 0", "fourthLineText", null);
	firstLineText.setFont(mainFont, nullTiming, nullTiming);
	secondLineText.setFont(mainFont, nullTiming, nullTiming);
	fourthLineText.setFont(mainFont, nullTiming, nullTiming);
	resultArray.highlightCell(1, 8, null, null);
	lang.nextStep();
	int i = 7;
	int j = 0;
	sc.unhighlight(8);
	sc.highlight(9);
	sc.highlight(10);
	sc.highlight(11);
	while (i > -1){
		marker = lang.newArrayMarker(resultArray, i+1, "marker", null, markerProps);
		firstLineText.hide();
		secondLineText.hide();
		double tempResult = result;
		result = result + Character.getNumericValue(input.charAt(i)) * Math.pow(2, j);
		firstLineText = lang.newText(firstLineCoordinates, "2 ^ pos = " + "2 ^ " + j + " = " + Math.pow(2, j), "firstLineText", null);
		secondLineText = lang.newText(secondLineCoordinates, "Exponente + temp[pos] * 2 ^ pos = " + 
				tempResult + " + " + Character.getNumericValue(input.charAt(i)) + " * " + Math.pow(2, j) + " = " + result, 
				"secondLineText", null);
		firstLineText.setFont(mainFont, nullTiming, nullTiming);
		secondLineText.setFont(mainFont, nullTiming, nullTiming);
		lang.nextStep();
		fourthLineText.hide();
		fourthLineText = lang.newText(fourthLineCoordinates, "Exponente = " + result, "fourthLineText", null);
		fourthLineText.setFont(mainFont, nullTiming, nullTiming);
		lang.nextStep();
		marker.hide();
		i = i - 1;
		j = j + 1;
		v.declare("double", "Exponente", Double.toString(result), 
    			animal.variables.Variable.getRoleString(VariableRoles.TEMPORARY));
	}
	firstLineText.hide();
	secondLineText.hide();
	sc.unhighlight(7);
	sc.unhighlight(9);
	sc.unhighlight(10);
	sc.unhighlight(11);
	resultArray.unhighlightCell(1, 8, null, null);
	return result;
}
    
private double getFraction(String input){
	
	resultArray.highlightCell(9, 31, null, null);
	sc.highlight(13);
	double result = 0;
	String tempString = "Bruchteil + temp[pos] * 2^(-pos)";
	secondLineText = lang.newText(secondLineCoordinates, "Bruchteil + temp[pos] * 2 ^ (-pos)", "secondLineText", null);
	secondLineText.setFont(mainFont, nullTiming, nullTiming);
	fifthLineText = lang.newText(fifthLineCoordinates, "Bruchteil = 0", "fifthLineText", null);
	fifthLineText.setFont(mainFont, nullTiming, nullTiming);
	lang.nextStep();
	sc.unhighlight(13);
	sc.highlight(14);
	sc.highlight(15);
	sc.highlight(16);
	for (int i = 0; i < 23; i++){
		int ii = i + 1;
		secondLineText.hide();
		marker = lang.newArrayMarker(resultArray, i+9, "marker", null, markerProps);
		
		
		tempString = result + " + " + Integer.parseInt(input.substring(i, i + 1)) + " * 2 ^ (-" +  ii + ")";
		result = result + Integer.parseInt(input.substring(i, i + 1)) * Math.pow(2, -i - 1);
		
		
		secondLineText = lang.newText(secondLineCoordinates, "Bruchteil + temp[pos] * 2 ^ (-pos) = " + tempString, "secondLineText", null);
		secondLineText.setFont(mainFont, nullTiming, nullTiming);
		lang.nextStep();
		fifthLineText.hide();
		fifthLineText = lang.newText(fifthLineCoordinates, "Bruchteil = " + result, "fifthLineText", null);
		fifthLineText.setFont(mainFont, nullTiming, nullTiming);
		if (Integer.parseInt(input.substring(i, i + 1)) != 0) 
			lang.nextStep();
		marker.hide();
		v.declare("double", "Bruchteil", Double.toString(result), 
    			animal.variables.Variable.getRoleString(VariableRoles.TEMPORARY));
		
	}
	secondLineText.hide();
	sc.unhighlight(12);
	sc.unhighlight(14);
	sc.unhighlight(15);
	sc.unhighlight(16);
	resultArray.unhighlightCell(9, 31, null, null);
	return result;
}
    
private double getResult(int sign, double exponent, double fraction){
	sc.unhighlight(12);
	sc.highlight(17);

	String resultString = "Ergebnis = ";
	sc.highlight(18);
	sixthLineText = lang.newText(sixthLineCoordinates, resultString, "sixthLineText", null);
	sixthLineText.setFont(mainFont, nullTiming, nullTiming);
	lang.nextStep();
	thirdLineText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, null, null);
	lang.nextStep();
	resultString = "Ergebnis = " + 
			sign;
	sixthLineText.hide();
	sixthLineText = lang.newText(sixthLineCoordinates, resultString, "sixthLineText", null);
	sixthLineText.setFont(mainFont, nullTiming, nullTiming);
	
	lang.nextStep();
	
	fifthLineText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, null, null);
	lang.nextStep();
	resultString = "Ergebnis = " + 
			sign + 
			" * (1 + " + fraction + ")";
	sixthLineText.hide();
	sixthLineText = lang.newText(sixthLineCoordinates, resultString, "sixthLineText", null);
	sixthLineText.setFont(mainFont, nullTiming, nullTiming);
	lang.nextStep();
	
	double result = 1 + fraction;
	result = result * Math.pow(2, exponent - 127);
	result = result * sign;
	
	fourthLineText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, null, null);
	lang.nextStep();
	resultString = "Ergebnis = " + 
			sign + 
			" * (1 + " + fraction + ") * 2 ^ (" + 
			exponent
			+ " - " + 
			127 + ")";
	sixthLineText.hide();
	sixthLineText = lang.newText(sixthLineCoordinates, resultString, "sixthLineText", null);
	sixthLineText.setFont(mainFont, nullTiming, nullTiming);
	lang.nextStep();
	
	sixthLineText = lang.newText(sixthLineCoordinates, resultString + " = " + result, "sixthLineText", null);
	sixthLineText.setFont(mainFont, nullTiming, nullTiming);
	return result;
}
    
private void showLastText(){
	for (int i = 0; i < 19; i ++){
		sc.highlight(i);;
	}
	sc = lang.newSourceCode(lastTextCoordinates, "lastText", null, sourceCodeProps);
	sc.addCodeLine("IEEE 754 unterscheidet zunächst zwischen binären Rundungen und binär-", null, 0, null);
	sc.addCodeLine("dezimalen Rundungen, bei denen geringere Qualitätsforderungen gelten.", null, 0, null);
	sc.addCodeLine("", null, 0, null);
	sc.addCodeLine("Bei binären Rundungen muss zur nächstgelegenen darstellbaren Zahl gerundet ", null, 0, null);
	sc.addCodeLine("werden. Wenn diese nicht eindeutig definiert ist (genau in der Mitte ", null, 0, null);
	sc.addCodeLine("zwischen zwei darstellbaren Zahlen), wird so gerundet, dass das nieder- ", null, 0, null);
	sc.addCodeLine("wertigste Bit der Mantisse 0 wird. Statistisch wird dabei in 50 % der ", null, 0, null);
	sc.addCodeLine("Fälle auf-, in den anderen 50 % der Fälle abgerundet, so dass die von ", null, 0, null);
	sc.addCodeLine("Knuth beschriebene statistische Drift in längeren Rechnungen vermieden wird.", null, 0, null);
	sixthLineText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null, null);
}
    
private void getFloat32(String input){
	
	showInitText();
	
	sc = lang.newSourceCode(sourceCodeCoordinates, "sourceCode", null, sourceCodeProps);
	sc.addCodeLine("Pseudocode:", null, 0, null);
	sc.addCodeLine("1. Vorzeichen", null, 0, null);
	sc.addCodeLine("temp = array[0]", null, 2, null);
	sc.addCodeLine("if temp = 1", null, 2, null);
	sc.addCodeLine("Vorzeichen = -1", null, 4, null);
	sc.addCodeLine("else", null, 2, null);
	sc.addCodeLine("Vorzeichen = 1", null, 4, null);
	sc.addCodeLine("2. Exponent", null, 0, null);
	sc.addCodeLine("temp = array[1-8]", null, 2, null);
	sc.addCodeLine("Iterate temp:", null, 2, null);
	sc.addCodeLine("Exponent = Exponent + temp[pos] * 2 ^ pos", null, 4, null);
	sc.addCodeLine("End", null, 2, null);
	sc.addCodeLine("3. Bruchteil", null, 0, null);
	sc.addCodeLine("temp = array[9-31]", null, 2, null);
	sc.addCodeLine("Iterate temp:", null, 2, null);
	sc.addCodeLine("Bruchteil = Bruchteil + temp[pos] * 2 ^ (-pos)", null, 4, null);
	sc.addCodeLine("End", null, 2, null);
	sc.addCodeLine("4. Ergebnis", null, 0, null);
	sc.addCodeLine("Ergebnis = (-1) ^ Vorzeichen * (1 + Bruchteil) * 2 ^ Exponente - Bias", null, 2, null);
	
	v = lang.newVariables();
	
	String[] endString = input.split("");
	resultArray = lang.newStringArray(resultArrayCoordinates, endString, "resultArray", null, arrayProps);
	lang.nextStep();
	sc.highlight(1);
	
	lang.nextStep("Vorzeichen");
	int sign = getSign(input.substring(0, 1));
	v.declare("int", "sign", Integer.toString(sign), 
			animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
	
	sc.unhighlight(1);
	sc.highlight(7);
	
	lang.nextStep("Exponente");
	double exponent = getExponent(input.substring(1, 9));
	v.declare("double", "Exponente", Double.toString(exponent), 
			animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
	
	lang.nextStep("Bruchteil");
	sc.unhighlight(7);
	sc.highlight(12);
	double fraction = getFraction(input.substring(9, 32));
	v.declare("double", "Bruchteil", Double.toString(fraction), 
			animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
	
	lang.nextStep("Ergebnis");
	double result = getResult(sign, exponent, fraction);
	v.declare("double", "Ergebnis", Double.toString(result), 
			animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
	
	lang.nextStep("Abschluss");
	showLastText();
	
}

public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
	    throws IllegalArgumentException
	  {
	    String EingabeStream = (String)arg1.get("Eingabe");
	    
	    
	    String ausgabe = "";
	    
	    int bitsMoreThenFitInByte = EingabeStream.length() % 32;
	    if (bitsMoreThenFitInByte != 0)
	    {
	      ausgabe = "Die Eingabezahl soll 32 Bits haben!%n";
	      
	      ausgabe = String.format(ausgabe, new Object[] { Integer.valueOf(8 - bitsMoreThenFitInByte) });
	    }
	    String worker = EingabeStream.replace("0", "");
	    worker = worker.replace("1", "");
	    
	    int numberOfInvalidSymbols = worker.length();
	    if (numberOfInvalidSymbols != 0)
	    {
	      ausgabe = ausgabe + "Bitte gib als Eingabe entweder nur 0 oder 1 ein!";
	      
	      ausgabe = String.format(ausgabe, new Object[] { Integer.valueOf(numberOfInvalidSymbols) });
	    }
		if (!ausgabe.equals("")) {
      throw new IllegalArgumentException(ausgabe);
		}
	    
	    return true;
	  }

    public String getName() {
        return "IEEE 754 Binär zu Dezimal";
    }

    public String getAlgorithmName() {
        return "IEEE 754 Binär zu Dezimal";
    }

    public String getAnimationAuthor() {
        return "Alymbek Sadybakasov";
    }

    public String getDescription(){
        return "Die Norm IEEE 754 (ANSI/IEEE Std 754-1985; IEC-60559:1989 – International version) definiert "
 +"\n"
 +"Standarddarstellungen für binäre Gleitkommazahlen in Computern und legt genaue Verfahren für"
 +"\n"
 +" die Durchführung mathematischer Operationen, insbesondere für Rundungen, fest. Der genaue "
 +"\n"
 +"Name der Norm ist englisch IEEE Standard for Binary Floating-Point Arithmetic for microprocessor "
 +"\n"
 +"systems (ANSI/IEEE Std 754-1985).";
    }

    public String getCodeExample(){
        return "1. Vorzeichen"
 +"\n"
 +"	temp = array[0]"
 +"\n"
 +"	if temp= 1"
 +"\n"
 +"		Vorzeichen = -1 "
 +"\n"
 +"	else "
 +"\n"
 +"		Vorzeichen = 1"
 +"\n"
 +"2. Exponent"
 +"\n"
 +"	temp = array[1-8]"
 +"\n"
 +"	Loop temp:"
 +"\n"
 +"		Exponent = Exponent + temp[pos]*2^pos"
 +"\n"
 +"	Ende"
 +"\n"
 +"3. Bruchteil"
 +"\n"
 +"	temp = array[9-31]"
 +"\n"
 +"	Loop temp:"
 +"\n"
 +"		Bruchteil = Bruchteil + temp[pos] * 2^-pos"
 +"\n"
 +"	Ende"
 +"\n"
 +"4. Ergebniss"
 +"\n"
 +"	Ergebniss = (-1) ^ Vorzeichen * (1 + fraction) * 2 ^ Exponente - bias"
 +"\n"
 +"		";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}