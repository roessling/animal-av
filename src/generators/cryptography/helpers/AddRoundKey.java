package generators.cryptography.helpers;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class AddRoundKey {
	//variables representing the respective animal objects
	private Language lang;
	
	private StringMatrix animalMatrix;
	
	private StringMatrix animalRoundKey;
	
	private StringMatrix animalCurrentColumn;
	
	private StringMatrix animalCurrentKey;
	
	private StringMatrix animalResultMatrix;
	
	private StringMatrix animalXORMatrix;
	
	private StringMatrix animalEqualsMatrix;
	
	private SourceCode animalSourceCode;
	
	private SourceCode descriptionText;
	
	private SourceCodeProperties sourceCodeProps;
	
	private MatrixProperties roundKeyProps;
	
	private MatrixProperties matrixProps;

	private TwoValueCounter counter;
	
	private Text animalRoundkeyLabel;
	
	public AddRoundKey(Language lang, MatrixProperties matrixProps, MatrixProperties roundKeyProps, SourceCodeProperties sourceCodeProps){
		this.lang = lang;
		lang.setStepMode(true);
		this.matrixProps=matrixProps;
		this.roundKeyProps=roundKeyProps;
		this.sourceCodeProps=sourceCodeProps;
	}

	/**
	 * The method that computes the addRoundKey in java and adds the correct animalcode to the language
	 * 
	 * @param matrix the statematrix of the AES
	 * @param roundKey the key for the current AES round
	 */
	public void addRoundKey(int[][] matrix, int[][] roundKey) {
		//show header
		initializeAnimalHeader();
		lang.nextStep();
		//show description
		initializeDescription();
		lang.nextStep();
		//hide description show initial computation values
		hideDescription();
		initializeMatrix(matrix);
		initializeAnimalMatrixCounter();
		initializeKey(roundKey);
		initializeSideMatrices();
		initializeSourceCode();
		//highlight sourcecode
		animalSourceCode.highlight(0);
		animalSourceCode.highlight(9);
		lang.nextStep();
		//highlight sourcecode
		animalSourceCode.unhighlight(0);
		animalSourceCode.unhighlight(9);
		animalSourceCode.highlight(1);
		animalSourceCode.highlight(8);
		for(int column = 0; column < 4; column++){
			//highlight matrices
			animalMatrix.highlightCellRowRange(0, 3, column, new TicksTiming(0), new TicksTiming(0));
			animalRoundKey.highlightCellColumnRange(column, 0, 3, new TicksTiming(0), new TicksTiming(0));
			lang.nextStep("compute column "+(column+1));
			//highlight sourcecode
			animalSourceCode.unhighlight(1);
			animalSourceCode.unhighlight(8);
			animalSourceCode.highlight(2);
			//moves matrixvalues into arrays for XOR computation
			animalCurrentColumn.put(0, 0, "(", new TicksTiming(100), new TicksTiming(0));
			lang.addLine("swapGridValues \"matrix[0][" + column + "]\" and \"currentColumn[0][1]\" refresh after 0 ticks within 100 ticks");
			lang.addLine("swapGridValues \"matrix[1][" + column + "]\" and \"currentColumn[0][2]\" refresh after 0 ticks within 100 ticks");
			lang.addLine("swapGridValues \"matrix[2][" + column + "]\" and \"currentColumn[0][3]\" refresh after 0 ticks within 100 ticks");
			lang.addLine("swapGridValues \"matrix[3][" + column + "]\" and \"currentColumn[0][4]\" refresh after 0 ticks within 100 ticks");
			counter.accessInc(4);
			animalCurrentColumn.put(0, 5, ")", new TicksTiming(100), new TicksTiming(0));

			int[] currentColumn = new int[4];
			currentColumn[0] = matrix[0][column];
			currentColumn[1] = matrix[1][column];
			currentColumn[2] = matrix[2][column];
			currentColumn[3] = matrix[3][column];
			lang.nextStep();
			//moves keyvalues into arrays for XOR computation
			animalXORMatrix.put(0, 0, "XOR", new TicksTiming(100), new TicksTiming(0));
			animalCurrentKey.put(0, 0, "(", new TicksTiming(100), new TicksTiming(0));
			lang.addLine("swapGridValues \"roundKey["+column+"][0]\" and \"currentKey[0][1]\" refresh after 100 ticks within 100 ticks");
			lang.addLine("swapGridValues \"roundKey["+column+"][1]\" and \"currentKey[0][2]\" refresh after 100 ticks within 100 ticks");
			lang.addLine("swapGridValues \"roundKey["+column+"][2]\" and \"currentKey[0][3]\" refresh after 100 ticks within 100 ticks");
			lang.addLine("swapGridValues \"roundKey["+column+"][3]\" and \"currentKey[0][4]\" refresh after 100 ticks within 100 ticks");
			animalCurrentKey.put(0, 5, ")", new TicksTiming(100), new TicksTiming(0));
			animalEqualsMatrix.put(0, 0, "=", new TicksTiming(100), new TicksTiming(0));
			counter.accessInc(4);
			//highlight sourcecode
			animalSourceCode.unhighlight(2);
			animalSourceCode.highlight(3);
			int []currentKey = new int[4];
			currentKey = roundKey[column];
			int[] resultVector = new int[4];
			lang.nextStep();
			//highlight sourcecode
			animalSourceCode.highlight(4);
			animalSourceCode.highlight(6);
			animalSourceCode.unhighlight(3);
			for(int row = 0; row < 4; row++){
				lang.nextStep();
				//highlight sourcecode
				animalSourceCode.unhighlight(4);
				animalSourceCode.unhighlight(6);
				animalSourceCode.highlight(5);
				//compute XOR result
				String result = String.valueOf(currentColumn[row] ^ currentKey[row]);
				animalResultMatrix.put(0, 0, "(", new TicksTiming(0), new TicksTiming(0));
				animalResultMatrix.put(0, row+1, result, new TicksTiming(0), new TicksTiming(0));
				animalResultMatrix.put(0, 5, ")", new TicksTiming(0), new TicksTiming(0));
				resultVector[row]=currentColumn[row] ^ currentKey[row];
				lang.nextStep();
				//highlight sourcecode
				animalSourceCode.highlight(4);
				animalSourceCode.highlight(6);
				animalSourceCode.unhighlight(5);
			}
			matrix[0][column]=resultVector[0];
			matrix[1][column]=resultVector[1];
			matrix[2][column]=resultVector[2];
			matrix[3][column]=resultVector[3];
			lang.nextStep();
			//highlight sourcecode
			animalSourceCode.unhighlight(4);
			animalSourceCode.unhighlight(6);
			animalSourceCode.highlight(7);
			//move XOR result into matrix, move key values back
			lang.addLine("swapGridValues \"matrix[0][" + column + "]\" and \"resultMatrix[0][1]\" refresh after 0 ticks within 100 ticks");
			lang.addLine("swapGridValues \"matrix[1][" + column + "]\" and \"resultMatrix[0][2]\" refresh after 0 ticks within 100 ticks");
			lang.addLine("swapGridValues \"matrix[2][" + column + "]\" and \"resultMatrix[0][3]\" refresh after 0 ticks within 100 ticks");
			lang.addLine("swapGridValues \"matrix[3][" + column + "]\" and \"resultMatrix[0][4]\" refresh after 0 ticks within 100 ticks");
			lang.addLine("swapGridValues \"roundKey["+column+"][0]\" and \"currentKey[0][1]\" refresh after 100 ticks within 100 ticks");
			lang.addLine("swapGridValues \"roundKey["+column+"][1]\" and \"currentKey[0][2]\" refresh after 100 ticks within 100 ticks");
			lang.addLine("swapGridValues \"roundKey["+column+"][2]\" and \"currentKey[0][3]\" refresh after 100 ticks within 100 ticks");
			lang.addLine("swapGridValues \"roundKey["+column+"][3]\" and \"currentKey[0][4]\" refresh after 100 ticks within 100 ticks");
			counter.assignmentsInc(8);
			clearSideMatrices();
			lang.nextStep();
			//highlight sourcecode
			animalSourceCode.unhighlight(7);
			animalSourceCode.highlight(1);
			animalSourceCode.highlight(8);
			//highlight matrices
			animalMatrix.unhighlightCellRowRange(0, 3, column, new TicksTiming(0), new TicksTiming(0));
			animalRoundKey.unhighlightCellColumnRange(column, 0, 3, new TicksTiming(0), new TicksTiming(0));
		}
		lang.nextStep();
		//hide computation, show final slide
		animalSourceCode.unhighlight(1);
		animalSourceCode.unhighlight(8);
		hideComputation();
		initializeFinalSlide();
	}

	/**
	 * initializes the source code that the user sees as visualization for the current algorithm state
	 */
	private void initializeSourceCode() {
		animalSourceCode = lang.newSourceCode(new Offset(50, 0, animalRoundKey, AnimalScript.DIRECTION_NE), "shiftRowSourceCode", null, sourceCodeProps);
		animalSourceCode.addCodeLine("private void addRoundKey(int[][] matrix, int[] roundKey){", null, 0, null);
		animalSourceCode.addCodeLine("	for(int column = 0; column < 4; column++){", null, 1, null);
		animalSourceCode.addCodeLine("		int[] currentColumn = matrix.getColumn(column);", null, 2, null);
		animalSourceCode.addCodeLine("		int []currentKey = roundKey.getRow(column);", null, 2, null);
		animalSourceCode.addCodeLine("		for(int row = 0; row < 4; row++){", null, 2, null);
		animalSourceCode.addCodeLine("			resultVector[row]=currentColumn[row] ^ currentKey[row];", null, 3, null);
		animalSourceCode.addCodeLine("		}", null, 2, null);
		animalSourceCode.addCodeLine("		matrix.setColumn(column, resultVector);", null, 2, null);
		animalSourceCode.addCodeLine("	}", null, 1, null);
		animalSourceCode.addCodeLine("}", null, 0, null);
		
	}


	/**
	 * shows the final slide with a description of what happened
	 */
	private void initializeFinalSlide() {
		SourceCode finalText = lang.newSourceCode(new Offset(100, 0, "matrix", AnimalScript.DIRECTION_NE), "finalText",null);
		finalText.addCodeLine("AddRoundKey finished succesffully!", null, 0, null);
		finalText.addCodeLine("The computation did 32 assignments and 32 accesses.", null, 0, null);
		finalText.addCodeLine("The resulting matrix may now be used as the new state matrix for the", null, 0, null);
		finalText.addCodeLine("next steps of the AES algorithm.", null, 0, null);
		
	}

	/**
	 * hides the irrelevant computation elements
	 */
	private void hideComputation() {
		animalSourceCode.hide();
		animalRoundKey.hide();
		animalRoundkeyLabel.hide();
	}

	/**
	 * clears the values of the matrices used for the computation
	 */
	private void clearSideMatrices() {
		for(int i = 0; i < 6; i++){
			animalCurrentColumn.put(0, i, "", new TicksTiming(201), new TicksTiming(0));
			animalCurrentKey.put(0, i, "", new TicksTiming(201), new TicksTiming(0));
			animalResultMatrix.put(0, i, "", new TicksTiming(201), new TicksTiming(0));
		}
		animalXORMatrix.put(0, 0, "", new TicksTiming(201), new TicksTiming(0));
		animalEqualsMatrix.put(0, 0, "", new TicksTiming(201), new TicksTiming(0));
	}

	/**
	 * initializes the matrices used for the computation of the XOR
	 */
	private void initializeSideMatrices() {
		animalCurrentColumn = lang.newStringMatrix(new Offset(0, 50, "matrix", AnimalScript.DIRECTION_SW), createEmptyMatrix(1, 6), "currentColumn", null);
		animalXORMatrix = lang.newStringMatrix(new Offset(155, 50, "matrix", AnimalScript.DIRECTION_SW), createEmptyMatrix(1, 1), "XORMatrix", null);
		animalCurrentKey = lang.newStringMatrix(new Offset(200, 50, "matrix", AnimalScript.DIRECTION_SW), createEmptyMatrix(1, 6), "currentKey", null);
		animalEqualsMatrix = lang.newStringMatrix(new Offset(355, 50, "matrix", AnimalScript.DIRECTION_SW), createEmptyMatrix(1, 1), "equalsMatrix", null);
		animalResultMatrix = lang.newStringMatrix(new Offset(380, 50, "matrix", AnimalScript.DIRECTION_SW), createEmptyMatrix(1, 6), "resultMatrix", null);
	}

	/**
	 * initialize the matrix for the roundkey
	 * 
	 * @param roundKey the key to be displayed
	 */
	private void initializeKey(int[][] roundKey) {
		roundKeyProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		animalRoundKey = lang.newStringMatrix(new Offset(100, 0, "matrix", AnimalScript.DIRECTION_NE), convertMatrix(roundKey), "roundKey", null, roundKeyProps);
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		animalRoundkeyLabel = lang.newText(new Offset(0, -30, "roundKey", AnimalScript.DIRECTION_NW), "Round Key", "roundKeyLabel", null, textProps);
	}

	/**
	 * initialize the counter for the algorithm.
	 */
	private void initializeAnimalMatrixCounter() {
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		counter = lang.newCounter(animalMatrix);
		lang.newCounterView(counter, new Offset(200, 0, "header", AnimalScript.DIRECTION_NW), cp, true, true);
	}

	/**
	 * initialize the matrix for the state
	 * 
	 * @param matrix the matrix to be displayed
	 */
	private void initializeMatrix(int[][]matrix) {
		matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		animalMatrix = lang.newStringMatrix(new Coordinates(50, 200), convertMatrix(matrix), "matrix", null, matrixProps);
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		lang.newText(new Offset(0, -30, "matrix", AnimalScript.DIRECTION_NW), "State Matrix", "matrixLabel", null, textProps);
	}

	/**
	 * hide description slide
	 */
	private void hideDescription() {
		descriptionText.hide();
		
	}

	/**
	 * show description slide
	 */
	private void initializeDescription() {
		//initialize the description text
		descriptionText = lang.newSourceCode(new Coordinates(200, 200), "descriptionText", null);
		descriptionText.addCodeLine("AddRoundKey is a step of the Rijndael-Chiffre. For AES this step gets", null, 0, null);
		descriptionText.addCodeLine("an AES state matrix (consisting of 16 byte values) and a 16 byte round", null, 0, null);
		descriptionText.addCodeLine("key as input. The first column of the matrix is XORedwith the first 4 bytes", null, 0, null);
		descriptionText.addCodeLine("of the key. The resulting bytes are the new values for the first column.", null, 0, null);
		descriptionText.addCodeLine("The next column gets XORed with the following 4 bytes of the key and so on.", null, 0, null);
		
	}

	/**
	 * creates an empty string matrix with the given rows and colums
	 * @param rows
	 * @param columns
	 * @return the empty matrix
	 */
	private String[][] createEmptyMatrix(int rows, int columns) {
		String[][] outputMatrix = new String[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				outputMatrix[i][j] = "";
			}
		}
		return outputMatrix;
	}
	
	/**
	 * converts an int matrix to to a string matrix
	 * @param inputMatrix the matrix to be converted
	 * @return the converted matrix
	 */
	private String[][] convertMatrix(int[][] inputMatrix) {
		String[][] outputMatrix = new String[inputMatrix.length][inputMatrix[0].length];
		for (int i = 0; i < inputMatrix.length; i++) {
			for (int j = 0; j < inputMatrix[0].length; j++) {
				outputMatrix[i][j] = String.valueOf(inputMatrix[i][j]);
			}
		}
		return outputMatrix;
	}

	/**
	 * initialize the animal header
	 */
	private void initializeAnimalHeader() {
		//initialize text
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		lang.newText(new Coordinates(20, 30), "AddRoundKey", "header", null, textProps);
		//initialize rectangle
		RectProperties hRectProps = new RectProperties();
		hRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null, hRectProps);
	}
}
