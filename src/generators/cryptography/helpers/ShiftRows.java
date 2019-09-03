package generators.cryptography.helpers;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;


public class ShiftRows {
   
	//variables representing the respective animal objects
	private Language lang;

	//variables for the main computation
	private StringMatrix mainMatrix;

	private Text tmpText;

	private StringMatrix tmpMatrix;

	private SourceCode shiftRowSc;

	private SourceCode shiftLeftSc;

	private Text rowText;

	private Text iText;
	
	private Polyline separatorLine;

	private TwoValueCounter counter;
	
	private MatrixProperties matrixProps;
	
	private SourceCodeProperties sourceCodeProps;
	
	//variables for the first slide
	private SourceCode descriptionText;

	private StringMatrix descriptionOutputMatrix;

	private StringMatrix descriptionInputMatrix;

	private Polyline descriptionArrow;   

	/**
	 * Constructor for a new ShiftRows algorithm.
	 * 
	 * @param l the {@link Language} for this ShiftRows to use (usually a new {@link AnimalScript}).
	 */
	public ShiftRows(Language l, MatrixProperties matrixProps, SourceCodeProperties sourceCodeProps) {
		lang = l;
		lang.setStepMode(true);
		this.matrixProps = matrixProps;
		this.sourceCodeProps = sourceCodeProps;
	}
   
	/**
	 * This method executes the algorithm. Generating the Animal Code aswell as executing the
	 * Javacode for the algorithm.
	 * 
	 * @param matrix the matrix to execute the algorithm on.
	 * Will be given by the user in the future.
	 */
	public void shiftRows(int[][] matrix) {
		//initialize and show header
		initializeAnimalHeader();
		lang.nextStep();
		//initialize and show description
		initializeDescription();
		lang.nextStep();
		//hide description, initialize and show all elements relevant for the computation
		hideDescription();
		initialzieAnimalMatrix(matrix);
		initializeAnimalShiftRowSourceCode();
		initializeAnimalShiftLeftSourceCode();
		initializeAnimalRowText();
		initializeAnimalIText();
		inititalzeAnimalMatrixCounter();
		//some source code highlighting to show which java code
		//is equivalent to the current state of the animation
		shiftRowSc.highlight(0);
		shiftRowSc.highlight(4);
		lang.nextStep();
		shiftRowSc.unhighlight(0);
		shiftRowSc.unhighlight(4);
		shiftRowSc.highlight(1);
		shiftRowSc.highlight(3);
		//initialize element that shows current state of loop variable
		rowText.setText("row = 0", new TicksTiming(0), new TicksTiming(0));
		rowText.show();
		highlightText(rowText);
		separatorLine.show();
		for (int row = 0; row < matrix.length; row++) {
			lang.nextStep();
			//some source code highlighting to show which java code
			//is equivalent to the current state of the animation
			shiftRowSc.unhighlight(1);
			shiftRowSc.unhighlight(3);
			shiftRowSc.highlight(2);
			unhighlightText(rowText);
			lang.nextStep();
			//call shifts for each row in. first execute the java code then generate
			//the equivalent animal code
			shiftLeft(matrix, row);
			shiftLeftAnimal(row);
			//some source code highlighting to show which java code
			//is equivalent to the current state of the animation
			shiftRowSc.unhighlight(2);
			shiftRowSc.highlight(1);
			shiftRowSc.highlight(3);
			rowText.setText("row = " + (row + 1), new TicksTiming(0),
					new TicksTiming(0));
			highlightText(rowText);
		}
		lang.nextStep();
		shiftRowSc.unhighlight(1);
		shiftRowSc.unhighlight(3);
		rowText.hide();
		unhighlightText(rowText);
		separatorLine.hide();
		lang.nextStep();
		//show final slide hide irrelevant computation elements
		hideComputation();
		initializeFinalSlide();
	}
   
	/**
	 * Hides computation elements of animal that are no longer relevant for the
	 * final slide.
	 */
	private void hideComputation() {
		shiftLeftSc.hide();
		shiftRowSc.hide();
		tmpMatrix.hide();
		tmpText.hide();
	}

	/**
	 * Initialize the text for the final slide.
	 */
	private void initializeFinalSlide() {
		SourceCode finalText = lang.newSourceCode(new Offset(100, 0, "mainMatrix", AnimalScript.DIRECTION_NE), "finalText",null);
		finalText.addCodeLine("ShiftRows finished succesffully!", null, 0, null);
		finalText.addCodeLine("The computation did 30 assignments and 30 accesses.", null, 0, null);
		finalText.addCodeLine("The resulting matrix may now be used as the new state matrix for the", null, 0, null);
		finalText.addCodeLine("next steps of the AES algorithm.", null, 0, null);
	}

	/**
	 * Hide the description elements in animal.
	 */
	private void hideDescription() {
		descriptionText.hide();
		descriptionInputMatrix.hide();
		descriptionOutputMatrix.hide();
		descriptionArrow.hide();
	}

	/**
	 * Initialize the description for the first slide.
	 */
	private void initializeDescription() {
		//initialize the description text
		descriptionText = lang.newSourceCode(new Coordinates(200, 200), "descriptionText", null);
		descriptionText.addCodeLine("The ShiftRows algorithm in general is part of the Rijndael Cipher. In", null, 0, null);
		descriptionText.addCodeLine("this visualisation we want to show you how ShiftRows works as part of", null, 0, null);
		descriptionText.addCodeLine("the AES (Advanced Encryption Standard) which is a special case of the", null, 0, null);
		descriptionText.addCodeLine("Rijndael Cipher.", null, 0, null);
		descriptionText.addCodeLine("The ShiftRows step operates on the rows of the  AES statematrix; it", null, 0, null);
		descriptionText.addCodeLine("cyclically shifts the bytes in each row by a certain offset. The first", null, 0, null);
		descriptionText.addCodeLine("row is left unchanged. Each byte of the second row is shifted one to", null, 0, null);
		descriptionText.addCodeLine("the left. Similarly, the third and fourth rows are shifted by offsets", null, 0, null);
		descriptionText.addCodeLine("of two and three respectively.", null, 0, null);
		//initialize the matrices
		MatrixProperties descriptionMatrixProps = new MatrixProperties();
		descriptionMatrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		String[][]descriptionInput={{"s00","s01","s02","s03"},{"s10","s11","s12","s03"},{"s20","s21","s22","s23"},{"s33","s30","s31","s32"}};
		String[][]descriptionOutput={{"s00","s01","s02","s03"},{"s11","s12","s03","s10"},{"s22","s23","s20","s21"},{"s30","s31","s32","s33"}};
		descriptionInputMatrix = lang.newStringMatrix(new Offset(0, 50, "descriptionText", AnimalScript.DIRECTION_SW), descriptionInput, "descriptionInputMatrix", null, descriptionMatrixProps);
		descriptionOutputMatrix = lang.newStringMatrix(new Offset(100, 0, "descriptionInputMatrix", AnimalScript.DIRECTION_NE), descriptionOutput, "descriptionOutputMatrix", null, descriptionMatrixProps);
		//initialize arrow between matrices
		PolylineProperties polylineProps = new PolylineProperties();
		polylineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		Node[] nodes = {new Offset(50, 75, "descriptionInputMatrix", AnimalScript.DIRECTION_NE),new Offset(-5, 75, "descriptionOutputMatrix", AnimalScript.DIRECTION_NW)};
		descriptionArrow = lang.newPolyline(nodes, "descriptionArrow", null, polylineProps);
	}

	/**
	 * generates the animal code for shifting a row
	 * @param row the rownumber to shift
	 */
	private void shiftLeftAnimal(int row) {
		//some source code and matrix highlighting to show which java code
		//is equivalent to the current state of the animation
		mainMatrix.highlightCellColumnRange(row, 0, 3, new TicksTiming(0), new TicksTiming(0));
		shiftLeftSc.highlight(0);
		shiftLeftSc.highlight(9);
		lang.nextStep();
		shiftLeftSc.unhighlight(0);
		shiftLeftSc.unhighlight(9);
		shiftLeftSc.highlight(1);
		shiftLeftSc.highlight(7);
		//initialize element that shows current state of loop variable
		iText.setText("i = 0", new TicksTiming(0), new TicksTiming(0));
		iText.show();
		highlightText(iText);
		//loop for shifts
		for (int i = 0; i < row; i++) {
			lang.nextStep();
			//some source code and matrix highlighting to show which java code
			//is equivalent to the current state of the animation
			shiftLeftSc.unhighlight(1);
			shiftLeftSc.unhighlight(7);
			shiftLeftSc.highlight(2);
			unhighlightText(iText);
			tmpText.show();
			tmpMatrix.show();
			
			//add lines to swap values through the matrix. This is where the action happens!
			//increment counter
			counter.accessInc(1);
			counter.assignmentsInc(1);
			lang.addLine("swapGridValues \"mainMatrix[" + row + "][0]\" and \"tmpMatrix[0][0]\" refresh after 0 ticks within 100 ticks");
			lang.nextStep();
			//increment counter
			counter.accessInc(1);
			counter.assignmentsInc(1);
			shiftLeftSc.unhighlight(2);
			shiftLeftSc.highlight(3);
			lang.addLine("swapGridValues \"mainMatrix[" + row + "][0]\" and \"mainMatrix[" + row + "][1]\" refresh after 0 ticks within 100 ticks");
			lang.nextStep();
			//increment counter
			counter.accessInc(1);
			counter.assignmentsInc(1);
			shiftLeftSc.unhighlight(3);
			shiftLeftSc.highlight(4);
			lang.addLine("swapGridValues \"mainMatrix[" + row + "][1]\" and \"mainMatrix[" + row + "][2]\" refresh after 0 ticks within 100 ticks");
			lang.nextStep();
			//increment counter
			counter.accessInc(1);
			counter.assignmentsInc(1);
			shiftLeftSc.unhighlight(4);
			shiftLeftSc.highlight(5);
			lang.addLine("swapGridValues \"mainMatrix[" + row + "][2]\" and \"mainMatrix[" + row + "][3]\" refresh after 0 ticks within 100 ticks");
			lang.nextStep();
			//increment counter
			counter.accessInc(1);
			counter.assignmentsInc(1);
			shiftLeftSc.unhighlight(5);
			shiftLeftSc.highlight(6);
			lang.addLine("swapGridValues \"mainMatrix[" + row + "][3]\" and \"tmpMatrix[0][0]\" refresh after 0 ticks within 100 ticks");

			lang.nextStep();
			tmpText.hide();
			tmpMatrix.hide();
			//some source code and matrix highlighting to show which java code
			//is equivalent to the current state of the animation
			shiftLeftSc.highlight(1);
			shiftLeftSc.highlight(7);
			shiftLeftSc.unhighlight(6);
			iText.setText("i = " + (i + 1), new TicksTiming(0), new TicksTiming(0));
			highlightText(iText);
		}
		lang.nextStep();
		//some source code and matrix highlighting to show which java code
		//is equivalent to the current state of the animation
		shiftLeftSc.unhighlight(0);
		shiftLeftSc.unhighlight(9);
		shiftLeftSc.unhighlight(1);
		shiftLeftSc.unhighlight(7);
		shiftLeftSc.highlight(8);
		unhighlightText(iText);
		iText.hide();
		lang.nextStep();
		shiftLeftSc.unhighlight(8);
		mainMatrix.unhighlightCellColumnRange(row, 0, 3, new TicksTiming(0), new TicksTiming(0));
	}
   
	/**
	 * JavaCode for shifting a row in a matrix left row-1 times
	 * 
	 * @param matrix the matrix to shift
	 * @param row the row to shift
	 * @return the shifted matrix
	 */
	private int[][] shiftLeft(int[][] matrix, int row) {
		for (int i = 0; i < row; i++) {
			int tmp = matrix[row][0];
			matrix[row][0] = matrix[row][1];
			matrix[row][1] = matrix[row][2];
			matrix[row][2] = matrix[row][3];
			matrix[row][3] = tmp;
		}
		return matrix;
	}
   
	/**
	 * initialize the source animal code element for the shift rows method
	 */
	private void initializeAnimalShiftRowSourceCode() {
		shiftRowSc = lang.newSourceCode(new Offset(100, 0, mainMatrix, AnimalScript.DIRECTION_NE), "shiftRowSourceCode", null, sourceCodeProps);
		shiftRowSc.addCodeLine("private void shiftRow(int[][] matrix){", null, 0, null);
		shiftRowSc.addCodeLine( "   for(int row = 0; row < matrix.length; row++){", null, 1, null);
		shiftRowSc.addCodeLine("      matrix = shiftLeft(matrix, row);", null, 2, null);
		shiftRowSc.addCodeLine("   }", null, 1, null);
		shiftRowSc.addCodeLine("}", null, 0, null);
	}
   
	/**
	 * initialize the source animal code element for the shift left method
	 */
	private void initializeAnimalShiftLeftSourceCode() {
		shiftLeftSc = lang.newSourceCode(new Offset(0, 100, shiftRowSc, AnimalScript.DIRECTION_SW), "shiftLeftSourceCode", null, sourceCodeProps);
		shiftLeftSc.addCodeLine( "private int[][] shiftLeft(int[][] matrix, int row){", null, 0, null);
		shiftLeftSc.addCodeLine("   for(int i = row; i < row; i++){", null, 1, null);
		shiftLeftSc.addCodeLine("      int tmp = matrix[row][0];", null, 2, null);
		shiftLeftSc.addCodeLine("      matrix[row][0] = matrix[row][1];", null, 2, null);
		shiftLeftSc.addCodeLine("      matrix[row][1] = matrix[row][2];", null, 2, null);
		shiftLeftSc.addCodeLine("      matrix[row][2] = matrix[row][3];", null, 2, null);
		shiftLeftSc.addCodeLine("      matrix[row][3] = tmp;", null, 2, null);
		shiftLeftSc.addCodeLine("   }", null, 1, null);
		shiftLeftSc.addCodeLine("   return matrix;", null, 3, null);
		shiftLeftSc.addCodeLine("}", null, 0, null);
	}
   
	/**
	 * initialize the counter for the algorithm.
	 */
	private void inititalzeAnimalMatrixCounter() {
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		counter = lang.newCounter(mainMatrix);
		lang.newCounterView(counter, new Offset(200, 0, "header", AnimalScript.DIRECTION_NW), cp, true, true);
	}
   
	/**
	 * initialize the animal header
	 */
	private void initializeAnimalHeader() {
		//initialize text
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		lang.newText(new Coordinates(20, 30), "ShiftRows", "header", null, textProps);
		//initialize rectangle
		RectProperties hRectProps = new RectProperties();
		hRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null, hRectProps);
	}
   
	/**
	 * initialize loop variable element
	 */
	private void initializeAnimalRowText() {
		rowText = lang.newText(new Offset(30, 16, shiftRowSc, AnimalScript.DIRECTION_NE), "", "rowText", null);
		rowText.hide();
		Node[] vertices = new Node[2];
		vertices[0] = new Offset(15, 0, shiftRowSc, AnimalScript.DIRECTION_NE);
		vertices[1] = new Offset(15, 290, shiftRowSc, AnimalScript.DIRECTION_SE);
		separatorLine = lang.newPolyline(vertices, "separatorLine", null);
		separatorLine.hide();
	}
   
	/**
	 * initialize loop variable element
	 */
	private void initializeAnimalIText() {
		iText = lang.newText(new Offset(0, 178, rowText, AnimalScript.DIRECTION_SW), "", "iText", null);
		iText.hide();
	}
	
	 /**
	  * highlights the given text
	 * @param text to highlight
	 */
	private void highlightText(Text text){
	      text.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, new TicksTiming(0), new TicksTiming(0));
	   }
	   
	 /**
	  * unhighlights the given text
	 * @param text to unhighlight
	 */
	private void unhighlightText(Text text){
	      text.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, new TicksTiming(0), new TicksTiming(0));

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
	 * initialize animal matrices.
	 * @param matrix the input matrix, given by user normally
	 */
	private void initialzieAnimalMatrix(int[][] matrix) {
		//initialize main matrix
		String[][] stringMatrix = convertMatrix(matrix);
		matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		mainMatrix = lang.newStringMatrix(new Coordinates(100, 200), stringMatrix, "mainMatrix", null, matrixProps);
		
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		lang.newText(new Offset(0, -30, "mainMatrix", AnimalScript.DIRECTION_NW), "State Matrix", "matrixLabel", null, textProps);

		tmpText = lang.newText(new Offset(0, -50, mainMatrix, AnimalScript.DIRECTION_NW), "tmp = ", "tmpText", null);
		tmpText.hide();
		//initialize side matrix for swapvalue
		String[][] emptyTmpMatrix = createEmptyMatrix(1, 1);
		matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");
		tmpMatrix = lang.newStringMatrix(new Offset(0, -3, "tmpText", AnimalScript.DIRECTION_NE), emptyTmpMatrix, "tmpMatrix", null, matrixProps);
		tmpMatrix.highlightCell(0, 0, new TicksTiming(0), new TicksTiming(0));
		tmpMatrix.hide();

	} 
}
