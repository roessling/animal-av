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
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.Group;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.OffsetFromLastPosition;

public class Parity implements Generator {
    private Language lang;    
    private Boolean evenBit;
    private int[][] dataMatrix;
    private IntMatrix im;
    private IntArray ia;
    private int step;
    private SourceCode sc;
    private int bitValue;
            
    private static String DESCRIPTION = "Parity bits are used for error detection. The parity bit checks the number of ones in the data word."
    		+ "\nIn the following animation the two dimensional parity bit generation is used:"
    		+ "\n "
    		+ "\n     1. The input data is represented in a square matrix. To do so the input data is filled up with zeros if necessary."
    		+ "\n "
    		+ "\n     2. Afterwards the dimension of the matrix is increased by one to gain another row and column for the parity bits."
    		+ "\n "
    		+ "\n     3. Now the parity bit for each row value OR each column value is calculated via cross sum (modular 2) "
    		+ "\n			and stored in the last bit of the array."
    		+ "\n "
    		+ "\nIf an error is detected, the parity bit of the column and row identify the wrong bit and it can be corrected.";
    
    
    public void init(){
        lang = new AnimalScript("ParityBit", "Tim Förster, Johannes Merz", 600, 600);
        lang.setStepMode(true);
        
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	//Reset step counter
    	step = -1;   
    	
    	/*		+-------------+
    	 * 		|Input mapping|	
    	 *		+-------------+
    	 */    	
    	// Primitives
        evenBit = (Boolean)primitives.get("evenBit");        
        int[] inputArray = (int[]) primitives.get("data");
        
        //Props
        MatrixProperties matrixOptions = (MatrixProperties)props.getPropertiesByName("matrixOptions");
        SourceCodeProperties sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
    	TextProperties textProps = (TextProperties) props.getPropertiesByName("textProps");
    	ArrayProperties arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    	RectProperties rectProps = (RectProperties) props.getPropertiesByName("rectProps");
    	SourceCodeProperties descriptionProps = (SourceCodeProperties) props.getPropertiesByName("descriptionProps");
    	
    	bitValue = evenBit ? 0 : 1;
    	
    	
    	/*		+-----------+
    	 * 		|Calculation|	
    	 *		+-----------+
    	 */
    	
    	// ------
    	// Matrix
    	// ------
    	int sqrt = (int) Math.ceil(Math.sqrt(inputArray.length));    	
    	int[][] matrix = this.arrayToSquareMatrix(inputArray);    	
    	int[][] dimension1Matrix = new int[sqrt][sqrt + 1];
		dataMatrix = new int[sqrt + 1][sqrt + 1];
		
		//Set the old values
    	for(int row = 0; row < sqrt; row++) {
    		for(int column = 0; column < sqrt; column++) {
    			dataMatrix[row][column] = matrix[row][column];
   				dimension1Matrix[row][column] = matrix[row][column];
        	}
    	}
    	
    	//init the new values with 1, if we use oddbit
    	if(!evenBit) {
        	for(int i = 0; i < sqrt + 1; i++) {
        			//right
        			dataMatrix[i][sqrt] = bitValue; 
        			
        			if (i < dimension1Matrix.length)
        				dimension1Matrix[i][sqrt] = bitValue;            	
        			
        			//buttom
        			dataMatrix[sqrt][i] = bitValue;
       				
        	}    		
    	}    	    	
    	
    	// ------
    	// Array
    	// ------
    	//Input
    	ArrayList<ArrayList<int[]>> arrays = new ArrayList<ArrayList<int[]>>();
    	ArrayList<int[]> inputArrayList = new ArrayList<int[]>();
    	inputArrayList.add(inputArray);
    	arrays.add(inputArrayList);

    	//Pad array
    	int[] paddingArray = new int[sqrt * sqrt];
    	for(int i = 0; i < inputArray.length; i++) {
    		paddingArray[i] = inputArray[i]; 
    	}
    	
    	//First Dim
    	/*
    	 * 					+---------------+---------------+------------------+
    	 * i  				|	    0       |     1         |      2           |
    	 * j				| 0 | 1 | 2 |	| 0 | 1 | 2 |   | 0 | 1  | 2  |    | 
    	 * addingArray[i]	|		  	| 4 |			| 8 |             | 12 |
    	 * currentArray[j]	| 0 | 1 | 2     | 5 | 6 | 7     | 9 | 10 | 11      |
    	 * concantArray 	| 0 | 1 | 2 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 |
    	 * 					+---+---+---+---+---+---+---+---+---+----+----+----+
    	 */
    	ArrayList<int[]> dim1ArrayListPre = new ArrayList<int[]>();
    	int[] currentArray;    	
    	int[] addingArray = {bitValue};
    	int[] concantArray = new int[sqrt * sqrt + sqrt];
    	for(int i = 0; i < sqrt; i++) {
    		
    		//new sub array
    		currentArray = new int[sqrt];    		
    		//set old values or fill with 0
    		for(int j = 0; j < sqrt; j++) {
    			int inputArrayValue = 0;
    			int indexOfInputArray = i*sqrt+j;
    			if(indexOfInputArray < paddingArray.length){
    				inputArrayValue = paddingArray[indexOfInputArray];
    			}
    			currentArray[j] = inputArrayValue;
    			concantArray[i*sqrt+i+j] = inputArrayValue;
    		}
    		

			concantArray[(i+1)*sqrt+i] = bitValue;

    		dim1ArrayListPre.add(currentArray);
    		dim1ArrayListPre.add(addingArray);
    	}
    	
    	arrays.add(dim1ArrayListPre);
    	
    	ArrayList<int[]> dim1ArrayList = new ArrayList<int[]>();
    	dim1ArrayList.add(concantArray);    	
    	arrays.add(dim1ArrayList);
    	
    	//Second dim
    	ArrayList<int[]> dim2ArrayListPre = new ArrayList<int[]>();
    	
    	int[] dim2ConcantArray = new int[concantArray.length + sqrt];
		addingArray = new int[sqrt];
		
		for(int i = 0; i < concantArray.length; i++) {
			dim2ConcantArray[i] = concantArray[i];			
		}		
		for(int i = 0; i < sqrt; i++) {
			addingArray[i] = bitValue;
			dim2ConcantArray[concantArray.length - 1 + i] = bitValue; 		
		}		
					
		dim2ArrayListPre.add(concantArray);
		dim2ArrayListPre.add(addingArray);
		arrays.add(dim2ArrayListPre);
		
    	ArrayList<int[]> dim2ArrayList = new ArrayList<int[]>();
    	dim2ArrayList.add(dim2ConcantArray);	
		arrays.add(dim2ArrayList);

		//ParityBit
    	ArrayList<int[]> parityArrayListPre = new ArrayList<int[]>();
    	
    	int[] parityConcantArray = new int[dim2ConcantArray.length + 1];
    	addingArray = new int[1];
    	addingArray[0] = bitValue;
    	
		for(int i = 0; i < dim2ConcantArray.length; i++) {
			parityConcantArray[i] = dim2ConcantArray[i];			
		}	
		parityConcantArray[dim2ConcantArray.length] = bitValue;
		
		parityArrayListPre.add(dim2ConcantArray);
		parityArrayListPre.add(addingArray);
		arrays.add(parityArrayListPre);

		
    	ArrayList<int[]> parityArrayList = new ArrayList<int[]>();
    	parityArrayList.add(parityConcantArray);
		arrays.add(parityArrayList);

    	
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
    	SourceCode algoDescription = lang.newSourceCode(new Coordinates(20, 50), "descirption", null, descriptionProps);
    	algoDescription.addMultilineCode(DESCRIPTION, "description", null);
    	

    	
    	// -----------
    	// Input
    	// -----------
    	lang.nextStep("Input");
    	algoDescription.hide();
    	
    	int arrayHeight = 75;
    	//Input Array
    	Text dataText = lang.newText(new Coordinates(20, arrayHeight), "Data", "txtDataArray", null, textProps);
    	
    	Coordinates arrayStartPosition = new Coordinates(60, arrayHeight);
    	
    	IntArray inputIntArray = lang.newIntArray(arrayStartPosition, arrays.get(0).get(0), "inputIntArray", null, arrayProps);    	    	    
    	inputIntArray.hide();
    	
    	
    	IntArray paddedIntArray = lang.newIntArray(arrayStartPosition,paddingArray, "paddedIntArray", null, arrayProps);    	    	    
    	paddedIntArray.hide();
    	
    	//Insert one
    	LinkedList<Primitive> dim1insert = new LinkedList<Primitive>();

    	int[] insertingArray;
    	for(int i = 0; i < arrays.get(1).size() ; i++){
    		
    		insertingArray = arrays.get(1).get(i);
    		//main array
    		if(i % 2 == 0) {
	    		if (i == 0) {
	    			ia = lang.newIntArray(arrayStartPosition, insertingArray, "", null, arrayProps);
	    		}
	    		else {
	    			ia = lang.newIntArray(new OffsetFromLastPosition(13*arrays.get(1).get(i-1).length, 8), insertingArray, "", null, arrayProps);
	    		}
    		}
    		// inserting elements
    		else {
    			ia = lang.newIntArray(new OffsetFromLastPosition(13*arrays.get(1).get(i-1).length, -8), insertingArray, "", null, arrayProps);
    		}
    		
    		dim1insert.add(ia);	
    		

    	}
    	
    	Group dim1InsertGroup = lang.newGroup(dim1insert, null);
    	dim1InsertGroup.hide();

    	
    	IntArray dim1Array = lang.newIntArray(arrayStartPosition, arrays.get(2).get(0), "inputIntArray", null, arrayProps);
    	dim1Array.hide();
    	
    	LinkedList<Primitive> dim2insert = new LinkedList<Primitive>();

    	for(int i = 0; i < arrays.get(3).size() ; i++){
    		
    		insertingArray = arrays.get(3).get(i);
    		//main array
    		if(i % 2 == 0) {
	    		if (i == 0) {
	    			ia = lang.newIntArray(arrayStartPosition, insertingArray, "", null, arrayProps);
	    		}
	    		else {
	    			ia = lang.newIntArray(new OffsetFromLastPosition(13*arrays.get(3).get(i-1).length, 8), insertingArray, "", null, arrayProps);
	    		}
    		}
    		// inserting elements
    		else {
    			ia = lang.newIntArray(new OffsetFromLastPosition(13*arrays.get(3).get(i-1).length, -8), insertingArray, "", null, arrayProps);
    		}
    		
    		dim2insert.add(ia);	
    		
    	}
    	
    	Group dim2InsertGroup = lang.newGroup(dim2insert, null);
       	dim2InsertGroup.hide();
    	

    	IntArray dim2Array = lang.newIntArray(arrayStartPosition, arrays.get(4).get(0), "inputIntArray", null, arrayProps);
    	dim2Array.hide();
    	
    	//Parity bit
       	LinkedList<Primitive> parityInsert = new LinkedList<Primitive>();
    	IntArray inputIntArrayWithoutParity = lang.newIntArray(arrayStartPosition, arrays.get(5).get(0), "inputIntArray", null, arrayProps);
    	ia = lang.newIntArray(new OffsetFromLastPosition(13*arrays.get(5).get(0).length, -8), arrays.get(5).get(1), "", null, arrayProps);
    	parityInsert.add(inputIntArrayWithoutParity);
    	parityInsert.add(ia);
    	
    	Group parityInsertGroup = lang.newGroup(parityInsert, null);
    	parityInsertGroup.hide();
    			
    	ia = lang.newIntArray(arrayStartPosition, arrays.get(6).get(0), "inputIntArray", null, arrayProps);
    	ia.hide();
    	    	
    	
    	// -----------
    	// Matrix
    	// -----------  	
    	IntMatrix inputMatrix = lang.newIntMatrix(new OffsetFromLastPosition(13*(paddingArray.length+sqrt) + 150, 0), matrix, "intMatrix", 
    			null, matrixOptions);    
    	inputMatrix.hide();
    	
    	IntMatrix dim1Matrix = lang.newIntMatrix(new OffsetFromLastPosition(0, 0), dimension1Matrix, "intMatrix", 
    			null, matrixOptions);
    	dim1Matrix.hide();
    	
    	im = lang.newIntMatrix(new OffsetFromLastPosition(0, 0), dataMatrix, "intMatrix", 
    			null, matrixOptions);
    	im.hide();
    	
    	// -----------
    	// Description
    	// -----------    	
    	Text description = lang.newText(new Coordinates(20, 150), "", "textDescription", null, textProps);
    	
    	// -----------
    	// Source Code
    	// -----------
    	sc = lang.newSourceCode(new OffsetFromLastPosition(0, 40), "sourceCode", null, sourceCodeProps);    	

    	sc.addCodeLine("Program ParityBitCalculation(Data)", null, 0, null);  // 0
    	sc.addCodeLine("Declare constant UpperSqrt = ceil(√(Data))", null, 1, null); // 1
    	sc.addCodeLine("Declare constant Bit = (evenBit ? 0 : 1 )", null, 1, null); // 2
    	sc.addCodeLine("Append 0's till length of Data modular UpperSqrt is 0", null, 1, null); // 3
    	sc.addCodeLine("Insert Bit into every UpperSqrt position in Data", null, 1, null); // 4
    	sc.addCodeLine("Append UpperSqrt times Bit to Data", null, 1, null); // 5
    	sc.addCodeLine("Append Bit to data", null, 1, null); // 6
    	sc.addCodeLine("For I = 0 to UpperSqrt-1", "outerFor", 1, null); // 7
    	sc.addCodeLine("For J = 0 to UpperSqrt-1", "innerFor", 2, null);  // 8    	
    	sc.addCodeLine("Set Value = Data[I * (UpperSqrt+1) + J]", "setValue", 3, null);  // 9    	
    	sc.addCodeLine("Add Value to Data[(I+1) * (UpperSqrt+1) - 1]", "addValueRow", 3, null);  // 10
    	sc.addCodeLine("Set Data[(I+1) * (UpperSqrt+1) - 1] = Data[(I+1) * (UpperSqrt+1) - 1] modular 2", "modValueRow", 3, null);  // 11    	
    	sc.addCodeLine("Add Value to Data[(UpperSqrt + 1) * UpperSqrt + J]", "addValueColumn", 3, null);  // 12
    	sc.addCodeLine("Set Data[(I+1) * UpperSqrt] = Data[(I+1) * UpperSqrt] modular 2", "modValueColumn", 3, null);  // 13    	
    	sc.addCodeLine("End For", "innerForEnd", 2, null);  // 14
    	sc.addCodeLine("End For", "outerForEnd", 1, null);  // 15
    	sc.addCodeLine("For I = 0 to UpperSqrt-1", "parityFor", 1, null); // 16
    	sc.addCodeLine("Set ParityBit = ParityBit + Data[(I+1) * UpperSqrt] mod 2", "calcParity", 2, null);  // 17
    	sc.addCodeLine("End For", "parityForEnd", 1, null);  // 18
    	sc.addCodeLine("End Program", "endProgram" , 0, null);  // 19    	    
        
    	/*		+-------+
    	 * 		|Program|	
    	 *		+-------+
    	 */        
        lang.nextStep("ProgramStart");
    	this.nextSourceCodeLineHighlight(); //0    	
    	
    	inputIntArray.show();
    	
    	lang.nextStep();
    	description.setText("Starting program.", null, null);
    	
    	lang.nextStep();    	
    	this.nextSourceCodeLineHighlight(); // 1       	
    	description.setText("UpperSqrt = " + String.valueOf(sqrt), null, null);
    	     	
    	lang.nextStep();    	
    	this.nextSourceCodeLineHighlight(); // 2
    	description.setText("Bit = " + String.valueOf(bitValue), null, null);
    	
    	lang.nextStep();
    	inputIntArray.hide();
    	this.nextSourceCodeLineHighlight(); // 3
    	description.setText("Padding " + String.valueOf(paddingArray.length - inputArray.length) + " bits to the data array", null, null);
    	paddedIntArray.show();
    	
    	lang.nextStep();    	
    	inputMatrix.show();
    	description.setText("The matrix is a square matrix of the input data.", null, null);

    	lang.nextStep();    	
    	this.nextSourceCodeLineHighlight(); // 4
    	paddedIntArray.hide();
    	
    	description.setText("Inserting " + String.valueOf(bitValue)+ " in every " + String.valueOf(sqrt) + " positions", null, null);
    	
    	inputMatrix.hide();
    	dim1Matrix.show();
    	
    	dim1Matrix.highlightCellRowRange(0, sqrt-1, sqrt, null, null);
    	
    	inputIntArray.hide();
    	dim1InsertGroup.show();
    	
    	lang.nextStep();
    	
    	dim1InsertGroup.hide();
    	dim1Array.show();
    	
    	dim1Matrix.unhighlightCellRowRange(0, sqrt-1, sqrt, null, null);
    	
    	lang.nextStep();       	
    	this.nextSourceCodeLineHighlight(); // 5
    	
    	description.setText("Appending " + String.valueOf(bitValue)+ " to the data", null, null);
    	dim1Array.hide();
    	dim2InsertGroup.show();
        	    	 
    	dim1Matrix.hide();
    	im.show();
    	im.highlightCellColumnRange(sqrt, 0, sqrt-1, null, null);
    	
    	lang.nextStep();
    	dim2InsertGroup.hide();
    	dim2Array.show();
  
    	im.unhighlightCellColumnRange(sqrt, 0, sqrt-1, null, null);
    	
    	lang.nextStep();
    	
    	this.nextSourceCodeLineHighlight(); // 6
    	description.setText("Appending last parity bit of value " + String.valueOf(bitValue)+ " to the data", null, null);
    	
    	dim2Array.hide();
    	parityInsertGroup.show();
    	im.highlightCell(sqrt, sqrt, null, null);
    	
    	lang.nextStep();
    	
    	parityInsertGroup.hide();
    	ia.show();    	
    	im.unhighlightCell(sqrt, sqrt, null, null);
    	
    	/*		+----+
    	 * 		|Algo|	
    	 *		+----+
    	 */
    	lang.nextStep("Begin of loops");
    	this.nextSourceCodeLineHighlight(); // 7
    	
    	description.setText("For each rows (I)", null, null);
    	    	
    	int newVal;
    	
    	lang.nextStep();
    	
    	
    	for(int i = 0; i < sqrt; i++) {
    		
    		sc.highlight("outerFor");
    		description.setText("I = " + String.valueOf(i) , null, null);
    		lang.nextStep("Loop with I (row) = " + String.valueOf(i));    		
    		sc.unhighlight("outerFor");

    		for(int j = 0; j < sqrt; j++) {
    			    			
    			sc.highlight("innerFor");
    			
        		description.setText("I = " + String.valueOf(i) + ", J = " + String.valueOf(j), null, null);
        		
        		lang.nextStep();
        		sc.toggleHighlight("innerFor", "setValue");
        		
        		int readPosition = i * (sqrt+1) + j;
        		int value = ia.getData(readPosition);
        		int positionFirstDim = (i+1) * (sqrt+1) - 1;
        		int positionSecondDim = ((sqrt + 1) * sqrt) + j;
        		
        		        		
        		ia.highlightCell(readPosition, null, null);
        		im.highlightCell(i, j, null, null);
        		
        		description.setText("Value = Data[" + String.valueOf(readPosition) + "] = " + String.valueOf(value), null, null);
    		
	        	lang.nextStep();
	        	sc.toggleHighlight("setValue", "addValueRow");
	        	
        		ia.unhighlightCell(readPosition, null, null);
        		im.unhighlightCell(i, j, null, null);
        		
	        	newVal = value + ia.getData(positionFirstDim);
	        	description.setText("Data[" + String.valueOf(positionFirstDim) + "] += Value = " + String.valueOf(newVal), null, null);
	        	
	        	ia.put(positionFirstDim, newVal, null, null);
	        	im.put(i, sqrt, newVal , null, null) ;
	        	
        		ia.highlightCell(positionFirstDim, null, null);
        		im.highlightCell(i, sqrt, null, null);

	        	lang.nextStep();
	        	sc.toggleHighlight("addValueRow", "modValueRow");
	        		    		
	        	int newModVal = newVal % 2;
	        	description.setText("Data[" + String.valueOf(positionFirstDim) + "] modular 2 = " + String.valueOf(newVal) + " modular 2 = "+ String.valueOf(newModVal), null, null);
	        	
	        	ia.put(positionFirstDim, newModVal, null, null);
	        	im.put(i, sqrt, newModVal , null, null) ;
	        	
	        	
	        	lang.nextStep();
	        	sc.toggleHighlight("modValueRow", "addValueColumn");
	        	
        		ia.unhighlightCell(positionFirstDim, null, null);
        		im.unhighlightCell(i, sqrt, null, null);
        		
	        	newVal = value + ia.getData(positionSecondDim);
	        	description.setText("Data[" + String.valueOf(positionSecondDim) + "] += Value = " + String.valueOf(newVal), null, null);
	        	
	        	ia.put(positionSecondDim, newVal, null, null);
	        	im.put(sqrt, j, newVal , null, null) ;	        	
	        	
        		ia.highlightCell(positionSecondDim, null, null);
        		im.highlightCell(sqrt, j, null, null);	        	
	        	
	        	lang.nextStep();
	        	sc.toggleHighlight("addValueColumn", "modValueColumn");
	        	
	        	newModVal = newVal % 2;
	        	description.setText("Data[" + String.valueOf(positionSecondDim) + "] modular 2 = " + String.valueOf(newVal) + " modular 2 = "+ String.valueOf(newModVal), null, null);
	        	
	        	ia.put(positionSecondDim, newModVal, null, null);
	        	im.put(sqrt, j, newModVal , null, null) ;	        	
	        	
	        	lang.nextStep();
        		ia.unhighlightCell(positionSecondDim, null, null);
        		im.unhighlightCell(sqrt, j, null, null);		        	
    			            		
        		sc.toggleHighlight("modValueColumn", "innerForEnd");
        		
        		lang.nextStep();
        		
        		sc.unhighlight("innerForEnd");
        		description.setText("" , null, null);
        		
        	}
    		
    		sc.highlight("outerForEnd");
    				 
    		lang.nextStep();
    		sc.unhighlight("outerForEnd");    		
    	}
    	
    	sc.highlight("outerFor");    	
    	lang.nextStep("Calculate Paritybit");
    	sc.unhighlight("outerFor");
    	
    	for(int i = 0; i < sqrt; i++) {
        
    		sc.highlight("parityFor");    		
        	lang.nextStep();
        	sc.toggleHighlight("parityFor", "calcParity");
        	
        	int parityPos = (sqrt+1) * (sqrt+1) - 1;
        	int parityBitValue = ia.getData(parityPos);
        	int pos = (sqrt+1) * sqrt + i; 
        	int value = ia.getData(pos);
        	int newValue = parityBitValue + value;
        	int modValue = newValue % 2;
        	
        	description.setText("ParityBit = ParityBit + Data[(UpperSqrt+1) * UpperSqrt + I] modular 2", null, null);
        	ia.highlightCell(parityPos, null, null);
        	im.highlightCell(sqrt, sqrt, null, null);
        	
        	lang.nextStep();
        	description.setText("ParityBit = " + String.valueOf(parityBitValue) +" + Data[(UpperSqrt+1) * UpperSqrt + I] modular 2", null, null);
        	
        	lang.nextStep();
        	description.setText("ParityBit = " + String.valueOf(parityBitValue) +" + Data[("+String.valueOf(sqrt)+ "+1) * UpperSqrt + I] modular 2", null, null);
        	
        	lang.nextStep();
        	description.setText("ParityBit = " + String.valueOf(parityBitValue) +" + Data[("+String.valueOf(sqrt+1)+ ") * UpperSqrt + I] modular 2", null, null);
        
        	lang.nextStep();
        	description.setText("ParityBit = " + String.valueOf(parityBitValue) +" + Data["+String.valueOf(sqrt+1)+ " * " + String.valueOf(sqrt)+" + I] modular 2", null, null);
        	
        	lang.nextStep();
        	description.setText("ParityBit = " + String.valueOf(parityBitValue) +" + Data["+String.valueOf((sqrt+1) * sqrt)+ " + " + String.valueOf(i)+"] modular 2", null, null);
        	
        	lang.nextStep();
        	description.setText("ParityBit = " + String.valueOf(parityBitValue) +" + Data[" + String.valueOf(pos) + "] modular 2", null, null);
        	ia.highlightCell(pos, null, null);
        	im.highlightCell(sqrt, i, null, null);       
        	
        	lang.nextStep();        	        	       
        	description.setText("ParityBit = " + String.valueOf(parityBitValue) +" + " + String.valueOf(value)+ " modular 2", null, null);
 	        	
        	lang.nextStep();
        	description.setText("ParityBit = " + String.valueOf(newValue) + " modular 2", null, null);        	
        	ia.unhighlightCell(pos, null, null);
        	im.unhighlightCell(sqrt, i, null, null);       
        	
        	lang.nextStep();
        	description.setText("ParityBit = " + String.valueOf(modValue), null, null);

        	ia.put(parityPos, modValue, null, null);
        	im.put(sqrt, sqrt, modValue, null, null);
        	
        	lang.nextStep();        	        	
        	sc.toggleHighlight("calcParity","parityForEnd");
        	description.setText("", null, null);
        	
        	ia.unhighlightCell(parityPos, null, null);
        	im.unhighlightCell(sqrt, sqrt, null, null);

        	lang.nextStep();
        	sc.unhighlight("parityForEnd");
    	}
    	
    	sc.highlight("endProgram");    	
    	lang.nextStep("End Program");    	
    	
    	lang.nextStep("Conclusion");
    	
    	//hide all elements
    	ia.hide();
    	im.hide();
    	sc.hide();
    	dataText.hide();
    	
    	description.hide();
    	
    	String inputString = "";
    	for (int i = 0; i < inputArray.length; i++) {
			inputString+= String.valueOf(inputArray[i]);
		}
    	
    	String outputString = "";
    	for (int i = 0; i < ia.getLength(); i++) {
			outputString += String.valueOf(ia.getData(i));
		}
    	
    	int addedBits = ia.getLength() - inputArray.length;
    	
    	float relativeSize = ((float) ia.getLength()) / inputArray.length;
    	relativeSize *= 10000;
    	relativeSize = ((float) Math.round(relativeSize)) / 100;
    	
    	String END_SCREEN = "Conclusion:"
    	+ "\n "
		+ "\nInput:		" + inputString
		+ "\nOutput: 	" + outputString
		+ "\nNumber of added Bit's: " + addedBits
		+ "\nRelative output size: " + String.valueOf(relativeSize) + "%"
		+ "\n "
		+ "\nComplexity class: O(n)"
		+ "\n "
		+ "\nOther algorithms:"
		+ "\n	- Cyclic redundancy check (CRC)"
		+ "\n	- Repetition codes"
		+ "\n	- Checksums"
		+ "\n	- Cryptographic hash functions (e.g. md5)"
		+ "\n	- Error-correcting codes (e.g. Hamming)";
		
    	SourceCode algoConclusion = lang.newSourceCode(new Coordinates(20, 50), "conclusion", null, descriptionProps);
    	algoConclusion.addMultilineCode(END_SCREEN, "conclusion", null);
    	
    	algoConclusion.show();    	
    	
    	//Dirty bugfix of matrix display bug
        return lang.toString().replaceAll("refresh", "");
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
        return "Two-Dimension Parity Bit Generation";
    }

    public String getAlgorithmName() {
    	return "Parity Bit";
    }

    public String getAnimationAuthor() {
        return "Tim Förster, Johannes Merz";
    }

    public String getDescription(){
        return "Parity bit is the simplest and a basic form of error detection that is used in data communication."
        		+ "\nMultiple partiy bit's can also be used to do error-correction (for example in Hamming-Codes)."
        		+ "\nParity bit's are also used in some RAID levels to archive data redundancy."
        		+ "\nParity bit can represented in an even parity bit or an odd parity bit.";
    }

    public String getCodeExample(){
    	String code = "Program ParityBitCalculation "
    			+ "\n	For Row = 0 to ROWSIZE"
    			+ "\n 		SET ParityDim1 ^= Data[Row] "
    			+ "\n	End For"
    			+ "\n"
    			+ "\n 	For Column = 0 to COLUMNSIZE"    	
    			+ "\n 		SET ParityDim2 ^= Data[Column] "
    			+ "\n	End For"
    			+ "\n	SET ParityBit = sum(ParityDim1) mod 2"
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

}