/*
 * parity.java
 * Tim Förster, Johannes Merz, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.OffsetFromLastPosition;

public class HammingCode implements Generator {
    private Language lang;    
    private Boolean evenBit;
    private StringArray is;
    private int step;
    private SourceCode sc;
    private int bitValue;
            
    private static String DESCRIPTION = "Hamming Code is the use of additional parity bits in a code word to identify errors. "
    		+ "\nThe algorithm adds parity bits in every position of the data hat is a power of two. "
    		+ "\nEach of the parity bits represents the parity for parts of the codeword. "
    		+ "\nThe part of the codeword is determined by the following scheme:"
    		+ "\n "
    		+ "\nParity bit in position 2^0=1: check 1, skip 1 --> (1, 3, 5, 7, ...)"
    		+ "\nParity bit in position 2^1=2: check 2, skip 2 --> (2, 3, 6, 7, ...)"
    		+ "\nAnd so on..."
    		+ "\n "
    		+ "\nThe parity bit is then set to 1 if the part of the data has an odd number of ones, otherwise it is set to 0";
   
    
    public void init(){
        lang = new AnimalScript("HammingCode", "Tim Förster, Johannes Merz", 600, 600);
        lang.setStepMode(true);        
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    
    	step = -1;
    	
    	//Local params
    	int arrayHeight = 120;
    	
    	/*		+-------------+
    	 * 		|Input mapping|	
    	 *		+-------------+
    	 */    	
    	// Primitives
        evenBit = (Boolean)primitives.get("evenBit");        
        int[] inputArray = (int[]) primitives.get("data");
        
        //Props
        SourceCodeProperties sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
    	TextProperties textProps = (TextProperties) props.getPropertiesByName("textProps");
    	ArrayProperties arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    	ArrayMarkerProperties arrayMarkerProps = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarkerProps");
    	RectProperties rectProps = (RectProperties) props.getPropertiesByName("rectProps");
    	SourceCodeProperties descriptionProps = (SourceCodeProperties) props.getPropertiesByName("descriptionProps");
    	
    	bitValue = evenBit ? 0 : 1;
    	
    	
    	/*		+-----------+
    	 * 		|Calculation|	
    	 *		+-----------+
    	 */	
    	// ------
    	// Array
    	// ------
    	//Input
    	ArrayList<ArrayList<int[]>> arrays = new ArrayList<ArrayList<int[]>>();
    	ArrayList<int[]> inputArrayList = new ArrayList<int[]>();
    	inputArrayList.add(inputArray);
    	arrays.add(inputArrayList);


    	
    	/*		+---+
    	 * 		|GUI|	
    	 *		+---+
    	 */
    	
    	// --------------
    	// Creating title
    	// --------------
        Text subject = lang.newText(new Coordinates(20, 20), this.getName(), "textSubject", null, textProps);               
        //change font
        Font subjectFont = (Font) subject.getProperties().get(AnimationPropertiesKeys.FONT_PROPERTY);
        subject.setFont(new Font(subjectFont.getName(), Font.BOLD, subjectFont.getSize()+1), null, null);        
        int width = subject.getText().length() * 8;
        //place a rectangle around it
        lang.newRect(new OffsetFromLastPosition(-5, -5), new OffsetFromLastPosition(width, 30), "rectTextSubject", null, rectProps);
        
    	// -----------
    	// Description
    	// -----------      
    	lang.nextStep("Description");  
    	SourceCode algoDescription = lang.newSourceCode(new Coordinates(20, 50), "description", null, descriptionProps);
    	algoDescription.addMultilineCode(DESCRIPTION, "description", null);
    	    	
    	// -----------
    	// Input
    	// -----------
    	lang.nextStep("Input");
    	algoDescription.hide();    
    	
    	// -----------
    	// Description
    	// -----------    	
    	Text description = lang.newText(new Coordinates(20, 150), "", "textDescription", null, textProps);
    	
    	// -----------
    	// Source Code
    	// -----------
    	sc = lang.newSourceCode(new OffsetFromLastPosition(0, 40), "sourceCode", null, sourceCodeProps);    	

    	sc.addCodeLine("Program HammingCode(Data)", null, 0, null);	// 0
    	sc.addCodeLine("Declare constant Bit = (evenBit ? 0 : 1 )", null, 1, null); // 1 
    	sc.addCodeLine("For I = 0 to Math.pow(2, i) <= Data.size()", "firstFor", 1, null); // 2
    	sc.addCodeLine("Add Bit to Data[Math.pow(2, i) - 1)]", "addBit", 2, null); // 3
    	sc.addCodeLine("End For", "endFirstFor", 1, null); // 4
    	sc.addCodeLine("For I = 1 to Math.pow(2, i) <= Data.size()", "outerFor", 1, null); // 5
		sc.addCodeLine("Declare parityBitPos = Math.pow(2, i - 1) - 1", "declareP", 2, null); // 6
    	sc.addCodeLine("For blockIndex = 0 to Data.size() - 1", "blockFor", 2, null); // 7
    	sc.addCodeLine("For innerblock = 0 to Math.pow(2, i-1) - 1", "innerFor", 3, null); // 8
		sc.addCodeLine("Declare index = parityBitPos + Math.pow(2, i) * blockIndex + innerblock", "declareI", 4, null); // 9
		sc.addCodeLine("If index >= Data.size() Do break Else if parityBitPos == index Do Continue", "if", 4, null); // 10
		sc.addCodeLine("Add Data[parityBitPos] XOR Data[index] to Data[parityBitPos]", "add", 4, null); // 11
    	sc.addCodeLine("End For", "endInnerFor", 3, null); // 12
    	sc.addCodeLine("End For", "endBlockFor", 2, null); // 13
    	sc.addCodeLine("End For", "endOuterFor", 1, null); // 14
    	sc.addCodeLine("End Program", "endProgram" , 0, null); // 15	
        
    	/*		+-------+
    	 * 		|Program|	
    	 *		+-------+
    	 */        
        lang.nextStep("Program Start");
    	this.nextSourceCodeLineHighlight(); // 0   
    	description.setText("Starting program.", null, null);
       	
    	//Input Array
    	Text data = lang.newText(new Coordinates(20, arrayHeight), "Data", "txtDataArray", null, textProps);    	
    	Coordinates arrayStartPosition = new Coordinates(60, arrayHeight);    	 
    	IntArray inputIntArray = lang.newIntArray(arrayStartPosition, arrays.get(0).get(0), "inputIntArray", null, arrayProps);   
    	
    	//Calculations
        Text calculations = lang.newText(new Coordinates(400, arrayHeight), "", "textCalculations", null, textProps);
        Font calcFont = (Font) subject.getProperties().get(AnimationPropertiesKeys.FONT_PROPERTY);
        calculations.setFont(new Font(calcFont.getName(), Font.BOLD, calcFont.getSize()+1), null, null);   
        
        Text txtParityBitPos = lang.newText(new Coordinates(400, arrayHeight + 20), "", "textParityBitPosition", null, textProps);
        Text txtIndex = lang.newText(new Coordinates(400, arrayHeight + 40), "", "textIndex", null, textProps);
        Text txtNewValue = lang.newText(new Coordinates(400, arrayHeight + 60), "", "textNewValue", null, textProps);

    	
    	lang.nextStep();    	
    	this.nextSourceCodeLineHighlight(); // 1   
    	lang.nextStep("Add Parity Positions");
    	sc.unhighlight(1);
    	sc.highlight("firstFor");
    	
    	//
    	//Stage one, insert Bit in (2^i)-1 postions
    	//
    	
    	description.setText("Adding parity bit positions.", null, null);
    	
    	//length := ceil(log2(input.length + log_2(input.length)))
    	    	
    	int numberOfAddedBits = this.ceilLog2(inputArray.length);
    	int paddedArrayLength = this.ceilLog2(inputArray.length + numberOfAddedBits);
    	ArrayList<Integer> array = new ArrayList<>();
    	ArrayList<Integer> inArray = new ArrayList<>();
    	ArrayList<String> stringArray = new ArrayList<>();
    	
    	for (int i = 0; i < inputArray.length; i++) {
			array.add(inputArray[i]);
			stringArray.add(Integer.toString(inputArray[i]));
		}
    	
    	inArray = array;
    	
    	inputIntArray.hide();    	
    	is = lang.newStringArray(arrayStartPosition, this.toStringArray(stringArray), "", null, arrayProps);

    	for (int i = 0; Math.pow(2, i) <= array.size(); i++) {
    		lang.nextStep();
    		sc.unhighlight("firstFor");
    		sc.highlight("addBit");
			array.add((int) (Math.pow(2, i) - 1), bitValue);
			stringArray.add((int) (Math.pow(2, i) - 1), "_");		
			is.hide();
	    	is = lang.newStringArray(arrayStartPosition, this.toStringArray(stringArray), "", null, arrayProps);
	    	lang.nextStep();
	    	sc.unhighlight("addBit");
	    	sc.highlight("endFirstFor");
	    	lang.nextStep();
	    	sc.unhighlight("endFirstFor");
	    	sc.highlight("firstFor");
		}
    	
    	sc.unhighlight("firstFor");
    	sc.highlight("outerFor");
    	
    	lang.nextStep("Calculate Parity Bits");
    	
        description.setText("Calculating parity bits", null, null);
    	//
    	//Stage two, calc all parityBits
    	//
    	ArrayMarker marker = lang.newArrayMarker(is, 0, "parityBitMarker", null, arrayMarkerProps);
    	calculations.setText("Calculations", null, null);
    	
    	for (int i = 1; Math.pow(2, i - 1) <= array.size(); i++) {
    		sc.unhighlight("endOuterFor");
    		sc.highlight("outerFor");
    		lang.nextStep();    
    		sc.unhighlight("outerFor");
    		sc.highlight("declareP");
    		//Parity bit at pos 2^i - 1
    		int parityBitPos = (int) (Math.pow(2, i - 1) - 1);
    		txtParityBitPos.setText("Parity Bit Position: " + Integer.toString(parityBitPos), null, null);
    		txtIndex.setText("", null, null);
    		txtNewValue.setText("", null, null);


    		marker.hide();
    		marker = lang.newArrayMarker(is, parityBitPos, "i", null, arrayMarkerProps);
    		marker.rotate(is, 180, null, null);
    		
    		is.highlightCell(parityBitPos, null, null); 
    		
    		for (int blockIndex = 0; blockIndex < array.size(); blockIndex++) {
				for (int innerblock = 0; innerblock < Math.pow(2, i-1); innerblock++) {
					int index = (int) (parityBitPos + Math.pow(2, i) * blockIndex + innerblock);
						is.highlightCell(index, null, null); 
				}
    		}
    		
    		lang.nextStep("Loop with I = " + String.valueOf(i) + ", Position: " + Integer.toString(parityBitPos));
    		
    		for (int blockIndex = 0; blockIndex < (array.size()/(Math.pow(2, i))); blockIndex++) {
    			sc.unhighlight("endBlockFor");
    			sc.unhighlight("declareP");
    			sc.highlight("blockFor");
    			lang.nextStep();
				
				for (int innerblock = 0; innerblock < Math.pow(2, i-1); innerblock++) {
					sc.unhighlight("endInnerFor");
					sc.unhighlight("blockFor");
					sc.highlight("innerFor");
					txtNewValue.setText("", null, null);

					lang.nextStep();
					int index = (int) (parityBitPos + Math.pow(2, i) * blockIndex + innerblock);
		    		txtIndex.setText("Index: " + Integer.toString(index), null, null);
		    		
					is.highlightElem(index, null, null);

					sc.unhighlight("innerFor");
					sc.highlight("declareI");
					lang.nextStep();

					sc.unhighlight("declareI");
					sc.highlight("if");
					
					lang.nextStep();
					if(index >= array.size()) {
						is.unhighlightElem(index, null, null);
						sc.unhighlight("if");
						sc.highlight("endInnerFor");
						lang.nextStep();
						break;
					}
					else if(parityBitPos == index) {
						is.unhighlightElem(index, null, null);
						sc.unhighlight("if");
						sc.highlight("endInnerFor");
						lang.nextStep();
						continue;
					}

					sc.unhighlight("if");
					sc.highlight("add");
					lang.nextStep();
					int newValue = array.get(parityBitPos) ^ array.get(index);
					
					txtNewValue.setText("New Parity Value: " + Integer.toString(array.get(parityBitPos)) + " XOR " + Integer.toString(array.get(index)) + " = " + Integer.toString(newValue), null, null);
					
					array.set(parityBitPos, newValue);
					is.put(parityBitPos, Integer.toString(newValue), null, null);
					is.unhighlightElem(index, null, null);
					
					sc.unhighlight("add");
					sc.highlight("endInnerFor");
					lang.nextStep();
				}
				
				sc.unhighlight("endInnerFor");
				sc.highlight("endBlockFor");
				lang.nextStep();
			}
    		sc.unhighlight("endBlockFor");
    		sc.highlight("endOuterFor");
    		lang.nextStep();
    		
    		for(int j = 0; j < is.getLength(); j++)
    			is.unhighlightCell(j, null, null);
		}
    	
    	marker.hide(); 	
    	
    	sc.unhighlight("endOuterFor");
    	sc.highlight("endProgram");    	
    	lang.nextStep("End Program");
    	
    	sc.unhighlight("endProgram");
    	
    	calculations.hide();
    	txtParityBitPos.hide();
    	txtIndex.hide();
    	txtNewValue.hide();
    	
    	lang.nextStep("Conclusion");
    	data.hide();
    	
    	
    	String END_SCREEN = "Algorithm terminated successfully:"
    			+ "\n "
    			+ "\nInput Data: " + inArray.toString()
    	    		+ "\nNumber of added parity bits: " + Integer.toString(paddedArrayLength)
    	    		+ "\nResulting code word: " + array.toString()
    	    		+ "\n "
    	    		+ "\nComplexity class: O(n^2)"    		
    	    		+ "\n "
    	    		+ "\nOther algorithms:"
    	    		+ "\n	- Cyclic redundancy check (CRC)"
    	    		+ "\n	- Repetition codes"
    	    		+ "\n	- Checksums"
    	    		+ "\n	- Cryptographic hash functions (e.g. md5)";
    	
    	SourceCode algoConclusion = lang.newSourceCode(new Coordinates(20, 50), "conclusion", null, descriptionProps);
    	algoConclusion.addMultilineCode(END_SCREEN, "conclusion", null);

    	
    	//hide all elements
    	is.hide();    
    	sc.hide();
    	description.hide();
    	
    	algoConclusion.show();
    	
    	
    	//Dirty bugfix for matrix display bug
        return lang.toString().replaceAll("refresh", "");
    }
    
    private double log2(int x)
    {
        return  (Math.log(x) / Math.log(2));
    }
    
    private int ceilLog2(int x){
    	return (int) Math.ceil(this.log2(x));
    }
    
    protected int[][] arrayToSquareMatrix(int[] inputArray){
    	
    	int arrayLength= inputArray.length;
    	int matrixDim = (int) Math.ceil(Math.sqrt(arrayLength));
    	int[][] inputData = new int[matrixDim][matrixDim];
    	int arrayCounter = 0;
    	for(int i=0; i<matrixDim; i++) {
    		for(int j=0; j<matrixDim; j++) {
    			if(i*matrixDim+j < arrayLength) {
    				inputData[i][j] = inputArray[arrayCounter];
    				arrayCounter++;
    			}
    			else inputData[i][j] = 0;
    		}
    	}
    	return inputData;
    }
    
    public String getName() {        
        return "Hamming Code Encoder";
    }

    public String getAlgorithmName() {
    	return "Hamming Code";
    }

    public String getAnimationAuthor() {
        return "Tim Förster, Johannes Merz";
    }

    public String getDescription(){
        return "Hammin Code is a code for error correction. It uses multiple overlapping parity bits to detect single-errors."
        		+ "It is named after R.W. Hamming and can be used for checking data transmissions."
        		+ "The code is created by adding parity bits in positions that are a power of two into the data array. Each parity bit then checks a part of the data.";
    }

    public String getCodeExample(){
    	
    	String code = "Program HammingCode(Data)"
    	+ "\n    Declare constant Bit = (evenBit ? 0 : 1 )"
    	+ "\n    For I = 0 to Math.pow(2, i) <= Data.size()"
    	+ "\n        Add Bit to Data[Math.pow(2, i) - 1)]"
    	+ "\n    End For"
    	+ "\n    For I = 1 to Math.pow(2, i) <= Data.size()"
		+ "\n        Declare parityBitPos = Math.pow(2, i - 1) - 1"
    	+ "\n        For blockIndex = 0 to Data.size() - 1"
    	+ "\n            For innerblock = 0 to Math.pow(2, i-1) - 1"
		+ "\n                Declare index = parityBitPos + Math.pow(2, i) * blockIndex + innerblock"
		+ "\n                If index >= Data.size() Do break Else if parityBitPos == index Do Continue"
		+ "\n                Add Data[parityBitPos] XOR Data[index] to Data[parityBitPos]"
    	+ "\n            End For"
    	+ "\n        End For"
    	+ "\n    End For"
    	+ "\nEnd Program";
    	
        return code;
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    private void nextSourceCodeLineHighlight(){
    	
    	if(step == -1) {
    		sc.highlight(++step);
    	}
    	else {
    		sc.toggleHighlight(step, ++step);
    	}
    	
    }
    
    //convert ArrayList<String> to String[]
    private String[] toStringArray(ArrayList<String> stringArray) {
    	String[] paddedArray = new String[stringArray.size()];
    	int index = 0;
    	
    	for (String I: stringArray)
    	{
    		paddedArray[index++] = I.toString();
    	}
    	
    	return paddedArray;
    }

}