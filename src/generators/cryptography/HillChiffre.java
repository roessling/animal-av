/*
 * hill_chiffre_wiz.asu.java
 * Phillip Schneider, Zi Wang, 2019 for the Animal project at TU Darmstadt.
 */
package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;

public class HillChiffre implements ValidatingGenerator {
    private Language language;
    private ArrayProperties arrayProps;
    private String plaintext;
    private TextProperties textProps;
    private MatrixProperties matrixProps;
    private String key;
	private Text header;
	private Text Schlüssel;
	private Text Klartext;
	private Text Chiffre;
	private Text valueOfln;
	private Text valueOfpm;
	private Text kM_text;
	private Text pM_text;
	private Text cM_text;
	private Text cipher;
	private Text newPlaintext;
	private Rect hRect;
	private SourceCode src;
	private SourceCodeProperties sourceCodeProps;
	private StringArray klarAlphabet;
	private IntMatrix kMatrix;
	private IntMatrix pMatrix;
	private IntMatrix cMatrix;
    public Translator translator;
    private Locale loc;
    
	public HillChiffre(String languageFilePath, Locale l){  	
    	translator = new Translator("resources/HillChiffre", l);
    	loc = l;
    }
	
	public void init(){
        language = new AnimalScript("Hill-Chiffre", "Phillip Schneider, Zi Wang", 1024, 860);
        language.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        textProps = (TextProperties)props.getPropertiesByName("textProps");
        matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        plaintext = (String)primitives.get("plaintext");
        key = (String)primitives.get("key");
        language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        QuestionGroupModel groupQ1 = new QuestionGroupModel("matrix size", 1);
        language.addQuestionGroup(groupQ1);
        start(plaintext, key);
        language.finalizeGeneration();
        return language.toString();
    }

	 public void start(String plain, String key) {
		  
		    // show the header with a heading surrounded by a rectangle
		    TextProperties headerProps = new TextProperties();
		    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		        Font.SANS_SERIF, Font.BOLD, 20));
		    header = language.newText(new Coordinates(400, 30), translator.translateMessage("name"),
		        "header", null, headerProps);
		    RectProperties rectProps = new RectProperties();
		    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
		    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		    hRect = language.newRect(new Offset(-7, -7, "header",
		        AnimalScript.DIRECTION_NW), new Offset(7, 7, "header", "SE"), "hRect",
		        null, rectProps);
	    
		    // setup the start page with the description
		    language.nextStep();
		    language.newText(new Offset(-390, 40,"header", 
		    	AnimalScript.DIRECTION_NW),
		        translator.translateMessage("description1"),
		        "description1", null, textProps);
		    language.newText(new Offset(0, 20, "description1",
		        AnimalScript.DIRECTION_NW),
		    	translator.translateMessage("description2"),
		        "description2", null, textProps);
		    language.newText(new Offset(0, 20, "description2",
		        AnimalScript.DIRECTION_NW),
		    	translator.translateMessage("description3"),
		        "description3", null, textProps);
		    language.newText(new Offset(0, 20, "description3",
		        AnimalScript.DIRECTION_NW),
		    	translator.translateMessage("description4"),
		        "description4", null, textProps);
		    language.newText(new Offset(0, 20, "description4",
			    AnimalScript.DIRECTION_NW),
		    	translator.translateMessage("description5"),
			    "description5", null, textProps);
		    language.nextStep("intro");
		    // hide the description 
		    language.hideAllPrimitives();
		    header.show();
		    hRect.show();
		    language.nextStep();
		    //to upper case
		    key = key.toUpperCase();
		    plain = plain.toUpperCase();
		    //show initial setting
		    TextProperties initialProps = new TextProperties();
		    initialProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		        Font.SANS_SERIF, Font.PLAIN, 16));
		    Schlüssel = language.newText(new Offset(-390, 50, "header", AnimalScript.DIRECTION_NW), translator.translateMessage("Schluessel")+ key,
			        "Schlüssel", null, initialProps);
		    Klartext = language.newText(new Offset(0, 20, "Schlüssel", AnimalScript.DIRECTION_NW), translator.translateMessage("Klartext")+ plain,
		    		"Klartext", null, initialProps);
		    Chiffre = language.newText(new Offset(0, 20, "Klartext", AnimalScript.DIRECTION_NW), translator.translateMessage("Chiffre"),
		    		"Chiffre", null, initialProps);
		    //show source code
		    
		    src = language.newSourceCode(new Offset(160, 50, "header", AnimalScript.DIRECTION_E), "sourcecode", null, sourceCodeProps);
		    src.addCodeLine(translator.translateMessage("code0"), null, 0,null); // 0
		    src.addCodeLine(translator.translateMessage("code1"), null, 0, null);
		    src.addCodeLine("FOR i=0 TO i<n {", null, 0, null);
		    src.addCodeLine("   FOR j=0 TO j<n {", null, 0, null); // 3
		    src.addCodeLine("       keyMatrix[i][j]= getIndex(key.charAt(k))}}", null, 0, null); // 4
		    src.addCodeLine(translator.translateMessage("code5"), null, 0, null); // 5
		    src.addCodeLine(translator.translateMessage("code6"), null, 0, null); // 6
		    src.addCodeLine("IF p mod n !=0 THEN", null, 0, null); // 7
		    src.addCodeLine("   FOR l=0 TO l<m*n-p", null, 0, null); // 8
		    src.addCodeLine("       plaintext +='X' END", null, 0, null); // 9
		    src.addCodeLine("FOR i=0 TO i<m {", null, 0, null); // 10
		    src.addCodeLine("   FOR j=0 TO j<n {", null, 0, null); // 11
		    src.addCodeLine("       plainMatrix[j][i]= getIndex(key.charAt(p))}}", null, 0, null); // 12
		    src.addCodeLine(translator.translateMessage("code13"),
		        null, 0, null); // 13
		    src.addCodeLine("cipherMatrix = keyMatrix * plainMatrix % 26", null, 0, null); // 14
		    src.addCodeLine("FOR i=0 TO i<m {", null, 0, null); // 15
		    src.addCodeLine("   FOR j=0 TO j<n {", null, 0, null); // 16
		    src.addCodeLine("       Chiffre = getAlphabet(cipherMatrix[j][i])}}", null, 0, null); // 17
		    
		    //show the table with a to z
		    String[] plainAlphabet = { "A", "B", "C", "D", "E", "F", "G", "H", " I ", 
		    	      "J ", "K", "L ", "M", "N", "O", "P", "Q", "R", "S", "T ", "U", "V", 
		    	      "W", "X", "Y ", "Z" };
		    klarAlphabet = language.newStringArray(new Offset(0, 20, "Chiffre", AnimalScript.DIRECTION_NW),  
		    	      plainAlphabet, "KlarAlphabet", null, arrayProps);
		    
		    language.nextStep("Initialization");
		    // call the enc algorithm
		    enc(plain, key);
		    // the last page
		    language.nextStep("End");
		    src.hide();
		    Schlüssel.hide();
		    Klartext.hide();
		    Chiffre.hide();
		    klarAlphabet.hide();
		    cMatrix.hide();
		    newPlaintext.hide();
		    cM_text.hide();
		    cipher.hide();
		    TextProperties endProps = new TextProperties();
		    endProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		        Font.SANS_SERIF, Font.PLAIN, 22));
		    language.newText(new Offset(-390, 40,"header", AnimalScript.DIRECTION_NW),
		    	translator.translateMessage("end1"),
			    "end1", null, endProps);
			language.newText(new Offset(0, 20, "end1", AnimalScript.DIRECTION_NW),
				translator.translateMessage("end2"),
			    "end2", null, endProps);
		    language.newText(new Offset(0, 20, "end2", AnimalScript.DIRECTION_NW),
		    	translator.translateMessage("end3"),
			    "end3", null, endProps);
			language.newText(new Offset(0, 20, "end3", AnimalScript.DIRECTION_NW),
				translator.translateMessage("end4"),
			    "end4", null, endProps);
			language.newText(new Offset(0, 20, "end4", AnimalScript.DIRECTION_NW),
				translator.translateMessage("end5"),
				"end5", null, endProps);
			language.newText(new Offset(0, 20, "end5", AnimalScript.DIRECTION_NW),
				translator.translateMessage("end6"),
				"end6", null, endProps);
		}

	 /**
	   * Executes the encrypt algorithm on the given key and plain text.
	   * @param plain plain text
	   * @param key key      
	   */  

	private void enc(String plain, String key) {
	    // highlight the code lines 
	    src.highlight(0);
	    src.highlight(1);
	    int k = key.length();
	    int p = plain.length();
	    int n = (int) Math.floor(Math.sqrt(k));
	    int m = (int) Math.ceil(plain.length()/Math.floor(Math.sqrt(k)));
	    TextProperties initialProps = new TextProperties();
		    initialProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		        Font.SANS_SERIF, Font.PLAIN, 16));
		valueOfln = language.newText(new Offset(0, 70, "KlarAlphabet", AnimalScript.DIRECTION_NW),
		        "k="+ k+ ", n=" +n, "valueOfln", null, initialProps);
		language.nextStep();
		// keyMatrix 
		src.unhighlight(0);
		src.unhighlight(1);
		src.highlight(2);
	    src.highlight(3);
	    src.highlight(4);
	    kM_text = language.newText(new Offset(0, 30, "valueOfln", AnimalScript.DIRECTION_NW), 
	    		"keyMatrix=", "kM_text", null, initialProps);
	    //MultiCQuestion1
	    MultipleChoiceQuestionModel question1 = new MultipleChoiceQuestionModel("q1");
	    question1.setPrompt(translator.translateMessage("q1p"));
	    question1.addAnswer(n+"*"+n, 1, translator.translateMessage("a1"));
	    question1.addAnswer((n-1)+"*"+(n-1), 0, translator.translateMessage("a2") + n+"*"+n);
	    question1.addAnswer((n+1)+"*"+(n+1), 0, translator.translateMessage("a3") + n+"*"+n);
	    question1.setGroupID("matrix size");
	    language.addMCQuestion(question1);
	    language.nextStep("keyMatrix");
	    int[][] keyMatrix;
	    keyMatrix = new int[n][n];
	    kMatrix = language.newIntMatrix(new Offset(20, -20, "kM_text", AnimalScript.DIRECTION_E), keyMatrix, "kMatrix", null, matrixProps);
	    language.nextStep();
		// fill the value of the key matrix
	    key = key.toUpperCase(); //to upper case
	    int count = 0;
	    for(int i=0; i<n; i++) {
	    	for (int j=0; j<n; j++){
	    		int x = getIndex(key.charAt(count));
	    		klarAlphabet.highlightCell(x, null, null);
	    		kMatrix.highlightCell(i, j, null, null);
	    		kMatrix.put(i, j, x, null, null);
	    		keyMatrix[i][j] = x;
	    		count = count + 1;
	    		language.nextStep();
	    		klarAlphabet.unhighlightCell(x, null, null);
	    		kMatrix.unhighlightCell(i, j, null, null);
	    	}
	    }
	    //PlainMatrix
	    src.unhighlight(2);
		src.unhighlight(3);
		src.unhighlight(4);
		src.highlight(5);
	    src.highlight(6);
	    //MultiCQuestion1
	    MultipleChoiceQuestionModel question2 = new MultipleChoiceQuestionModel("q2");
	    question2.setPrompt(translator.translateMessage("q2p"));
	    question2.addAnswer(n+"*"+m, 1, translator.translateMessage("a1"));
	    question2.addAnswer(n+"*"+(m+1), 0, translator.translateMessage("a2") + n+"*"+m);
	    question2.addAnswer((n+1)+"*"+m, 0, translator.translateMessage("a3") + n+"*"+m);
	    question2.setGroupID("matrix size");
	    language.addMCQuestion(question2);
	    language.nextStep();
	    valueOfpm = language.newText(new Offset(0, 80+10*n, "kM_text", AnimalScript.DIRECTION_NW), 
	    		"p="+ p+ ", m=" +m, "valueOfpm", null, initialProps);
	    language.nextStep();

	    // fill 'X' to the end of plain text
	    src.unhighlight(5);
		src.unhighlight(6);
		src.highlight(7);
		src.highlight(8);
	    src.highlight(9);
	    plain = plain.toUpperCase(); //to upper case
	    for (int i=0; i<m*n-p; i++) {
	    	plain += "X";
	    }
	    newPlaintext = language.newText(new Offset(5, 0, "Klartext", AnimalScript.DIRECTION_NE), 
	    		"-->"+plain, "newPlaintext", null, initialProps);
	    language.nextStep();
	    // plain matrix
	    src.unhighlight(7);
		src.unhighlight(8);
		src.unhighlight(9);
		src.highlight(10);
	    src.highlight(11);
	    src.highlight(12);
	    pM_text = language.newText(new Offset(0, 30, "valueOfpm", AnimalScript.DIRECTION_NW), 
	    		"plainMatrix=", "pM_text", null, initialProps);
	    language.nextStep("plainMatrix");
	    int[][] plainMatrix;
	    plainMatrix = new int[n][m];
	    pMatrix = language.newIntMatrix(new Offset(20, -20, "pM_text", AnimalScript.DIRECTION_E), plainMatrix, "pMatrix", null, matrixProps);
	    language.nextStep();
	    // fill the value of the plain matrix
	    int count_2 = 0;
	    for(int i=0; i<m; i++) {
	    	for (int j=0; j<n; j++){
	    		int x = getIndex(plain.charAt(count_2));
	    		klarAlphabet.highlightCell(x, null, null);
	    		pMatrix.highlightCell(j, i, null, null);
	    		pMatrix.put(j, i, x, null, null);
	    		plainMatrix[j][i] = x;
	    		count_2 = count_2 + 1;
	    		language.nextStep();
	    		klarAlphabet.unhighlightCell(x, null, null);
	    		pMatrix.unhighlightCell(j, i, null, null);
	    	}
	    }
	    
	    //new page
	    valueOfln.hide();
	    valueOfpm.hide();
	    pMatrix.hide();
	    kMatrix.hide();
	    pM_text.hide();
	    kM_text.hide();
	    src.unhighlight(10);
		src.unhighlight(11);
		src.unhighlight(12);
		src.highlight(13);
	    src.highlight(14);
	    language.nextStep();
	    cM_text = language.newText(new Offset(0, 70, "KlarAlphabet", AnimalScript.DIRECTION_NW), 
	    		"cipherMatrix=", "cM_text", null, initialProps);
	    language.nextStep("cipherMatrix");
	    int[][] cipherMatrix;
	    cipherMatrix = new int[n][m];
	    //calculate the multi-matrix cipherMatrix=keyMatrix*plainMatrix%26
	    for(int i=0; i<n; i++) {  
	    	for(int j=0; j<m; j++) { 
	    		int temp = 0;
	    		for(int t=0; t<n; t++) { 
	    			temp+=keyMatrix[i][t]*plainMatrix[t][j];
	    			cipherMatrix[i][j] = temp % 26;
	    		}
	    	}		
	    }
	    cMatrix = language.newIntMatrix(new Offset(20, -20, "cM_text", AnimalScript.DIRECTION_E), cipherMatrix, "cMatrix", null, matrixProps);
	    language.nextStep();
	    //get the cipher text (convert the index to the alphabet)
	    src.unhighlight(13);
		src.unhighlight(14);
		src.highlight(15);
		src.highlight(16);
	    src.highlight(17);
	    int count_3 = 0;
	    String s = "";
	    for(int i=0; i<m; i++) {
	    	for (int j=0; j<n; j++){
	    		s += getAlphabet(cipherMatrix[j][i]);
	    		klarAlphabet.highlightCell(cipherMatrix[j][i], null, null);
	    		cMatrix.highlightCell(j, i, null, null);
	    		cipher = language.newText(new Offset(5, 0, "Chiffre", AnimalScript.DIRECTION_NE), 
	    	    		s, "cipher"+count_3, null, initialProps);
	    		count_3 = count_3 + 1;
	    		language.nextStep();
	    		klarAlphabet.unhighlightCell(cipherMatrix[j][i], null, null);
	    		cMatrix.unhighlightCell(j, i, null, null);
	    		
	    		if (j<n & i!=m-1){
	    			cipher.hide();
	    		} else if (j!=n-1){
	    			cipher.hide();
	    		}
	    	}
	    }
	}

	private int getIndex(char c){
		return Math.abs(c - 'A') % 26;
	}
	
	private String getAlphabet(int i){
		return String.valueOf((char)(i+65));
	}
	///
    public String getName() {
        return translator.translateMessage("name");
    }

    public String getAlgorithmName() {
        return translator.translateMessage("name");
    }

    public String getAnimationAuthor() {
        return "Phillip Schneider, Zi Wang";
    }

    public String getDescription(){
        return translator.translateMessage("D1")
 +"\n"
 +translator.translateMessage("D2")
 +"\n"
 +translator.translateMessage("D3")
 +"\n"
 +translator.translateMessage("D4")
 +"\n"
 +translator.translateMessage("D5")
 +"\n"
 +translator.translateMessage("D6")
 +"\n"
 +translator.translateMessage("D7");
    }

    public String getCodeExample(){
        return translator.translateMessage("code0")
 +"\n"
 +translator.translateMessage("code1")
 +"\n"
 +"FOR i=0 TO i<n {"
 +"\n"
 +"     FOR j=0 TO j<n {"
 +"\n"
 +"        keyMatrix[i][j]= getIndex(key.charAt(k))}}"
 +"\n"
 +translator.translateMessage("code5")
 +"\n"
 +translator.translateMessage("code6")
 +"\n"
 +" IF p mod n !=0 THEN"
 +"\n"
 +"     FOR l=0 TO l<m*n-p"
 +"\n"
 +"         plaintext +='X' END"
 +"\n"
 +" FOR i=0 TO i<m {"
 +"\n"
 +"     FOR j=0 TO j<n {"
 +"\n"
 +"        plainMatrix[j][i]= getIndex(key.charAt(p))}}"
 +"\n"
 +translator.translateMessage("code13")
 +"\n"
 +"cipherMatrix = keyMatrix * plainMatrix % 26"
 +"\n"
 +" FOR i=0 TO i<m"
 +"\n"
 +"      FOR j=0 TO j<n"
 +"\n"
 +"         Chiffre = getAlphabet(cipherMatrix[j][i])}}";
    }

    
    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		String plaintext = (String)arg1.get("plaintext");
	    String key = (String)arg1.get("key");
	    if (plaintext.isEmpty()) {
	      throw new IllegalArgumentException("Der Klartext darf nicht leer sein");
	    }
	    if (key.isEmpty()) {
	      throw new IllegalArgumentException("Der Schlüssel darf nicht leer sein");
	    }
	    if (key.length() > 25) {
	      throw new IllegalArgumentException(
	        "Die Länge des Schlüssels sollte kleiner als 26.");
	    }
	    return true;
	}
	//main function
    public static void main (String[] args){
    	Generator generator = new HillChiffre("resources/HillChiffre", Locale.GERMANY); //Locale.US available
    	Animal.startGeneratorWindow(generator);
    }

}