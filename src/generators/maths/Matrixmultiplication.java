/*
 * Matrixmultiplication.java
 * Annemarie Mattmann, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/*
 * Generator for standard matrix multiplication
 */
public class Matrixmultiplication implements ValidatingGenerator {
	private Language lang;
    private PolylineProperties arrowProperties;
    private SourceCodeProperties sourceCodeProperties;
    private int[][] A;
    private int[][] B;
    private TextProperties textProperties;
    private MatrixProperties resultMatrixProperties;
    private RectProperties codeFrameProperties;
    private MatrixProperties matrixProperties;

    public void init(){
        lang = new AnimalScript("Matrixmultiplication", "Annemarie Mattmann", 640, 480);
		// initialize StepMode
		lang.setStepMode(true);
    }
    
    /*
     * Initialize animation, show explanations and call algorithm
     */
    public void multiply() {
		
    	// Animation title + frame (rectangle around title)
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
			    Font.BOLD, 24));
		
		Text title = lang.newText(new Coordinates(20, 30), "Matrixmultiplication", "title", null, titleProps);
		Rect titleFrame = lang.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW), new Offset(5, 5, "title", AnimalScript.DIRECTION_SE), "titleFrame", null);
		
		// Animation background information
		InfoBox description = new InfoBox(lang, new Offset(0, 20, "title", AnimalScript.DIRECTION_SW), 20, "Background Information"); 
		List<String> preText = Arrays.asList(
				"Matrix multiplication refers to the multiplication of two matrices A and B and results in a new matrix C with",
				"as many rows as A and as many columns as B.",
				"The latter can be remembered more easily if one writes the matrix B in the middle and the matrix A diagonally",
				"below it to the left side, then the result matrix can be fitted next to A and below B.",
				"To perform the computation of A*B the number of columns of A must equal the number of rows of the matrix B",
				"(which clarifies why matrix multiplication is not commutative).",
				"The product (i.e. each entry of the result matrix C) is then calulated by summing up the products of each element",
				"in one row of A and one column of B for all rows in A and all columns in B.",
				"",
				"The algorithm describes the formula for the multiplication of two matrices A and B:",
				"(AB)_ij = Sum_k=1^m A_ik * B_kj.");
		description.setText(preText);
		
		lang.nextStep("Introduction");
		
		description.hide();
		
		// Minimum offset between matrices A respectively B and result
		int offsetBetweenMatrices = 20;
		// Invisible shadow matrix to find out how to place A and B to clear enough space for both
		IntMatrix shadowA = lang.newIntMatrix(new Offset(80, 100, "title", AnimalScript.DIRECTION_SW), A, "shadowA", null, matrixProperties);
		shadowA.hide();
		IntMatrix shadowB = lang.newIntMatrix(new Offset(80, 100, "title", AnimalScript.DIRECTION_SW), B, "shadowB", null, matrixProperties);
		shadowB.hide();
		
		// Matrices A and B placed diagonally to each other
		IntMatrix matrixB = lang.newIntMatrix(new Offset(offsetBetweenMatrices, 0, "shadowA", AnimalScript.DIRECTION_NE), B, "matrixB", null, matrixProperties);
		IntMatrix matrixA = lang.newIntMatrix(new Offset(0, offsetBetweenMatrices, "shadowB", AnimalScript.DIRECTION_SW), A, "matrixA", null, matrixProperties);
		
		// Source code + frame placed to the right of the matrices
		SourceCode code = lang.newSourceCode(new Offset(90, 0, "matrixB", AnimalScript.DIRECTION_NE), "code", null, sourceCodeProperties);
		code.addCodeLine("def matrixmultiplication(matrixA, matrixB):", "definition", 0, null);
		code.addCodeLine("result = numpy.zeros([matrixA.shape[0], matrixB.shape[1]])", "defResult", 2, null);
		code.addCodeLine("for i in range(0, matrixA.shape[0]):", "forRowA", 2, null);
		code.addCodeLine("for j in range(0, matrixB.shape[1]):", "forColB", 4, null);
		code.addCodeLine("for k in range(0, matrixA.shape[1]):", "forElements", 6, null);
		code.addCodeLine("result[i,j] += matrixA[i,k] * matrixB[k,j]", "intermediateResult", 8, null);
		code.addCodeLine("return result", "return", 2, null);
		
		Rect codeFrame = lang.newRect(new Offset(-5, -5, "code", AnimalScript.DIRECTION_NW), new Offset(5, 5, "code", AnimalScript.DIRECTION_SE), "codeFrame", null, codeFrameProperties);
		
		lang.nextStep("Initialization");
		
		code.highlight(1);
		
		// Result matrix placed below B (and next to A)
		int[][] dataResult = new int[A.length][B[0].length];
		IntMatrix result = lang.newIntMatrix(new Offset(0, offsetBetweenMatrices, "matrixB", AnimalScript.DIRECTION_SW), dataResult, "result", null, resultMatrixProperties);
		
		lang.nextStep("Enter Algorithm");
		
		code.unhighlight(1);
		
		// Call algorithm for matrix multiplication
		algorithm(matrixA, matrixB, result, code);
		
		code.highlight(6);
		
		lang.nextStep("Leave Algorithm");
		
		matrixA.hide();
		matrixB.hide();
		result.hide();
		code.hide();
		codeFrame.hide();
		
		// Final remark
		InfoBox outlook = new InfoBox(lang, new Offset(0, 20, "title", AnimalScript.DIRECTION_SW), 20, "Final Remark"); 
		List<String> postText = Arrays.asList(
				"This animation displayed the standard method for multiplying two matrices. As mentioned in the introduction",
				"matrix multiplication is not commutative which means that while you may be able to calculate A*B the reverse,",
				"i.e. multiplying B*A is only possible if A and B are quadratic matrices because the number of columns of A",
				"must equal the number of rows of B.",
				"",
				"The shown algorithm for matrix multiplication is in O(n^3) owing to the three for-loops. However, faster",
				"algorithms exist with a cap of O(n^2) since all entries of the input matrices must be used.",
				"",
				"The algorithm code displayed was written in Python using NumPy.");
		outlook.setText(postText);
		
		lang.nextStep("Final Remark");
	}
	
    /*
     * Standard matrix multiplication algorithm
     */
	private void algorithm(IntMatrix matrixA, IntMatrix matrixB, IntMatrix result, SourceCode code) {
		// Rows and columns of matrices
		int colsA = matrixA.getNrCols();
		int rowsA = matrixA.getNrRows();
		int colsB = matrixB.getNrCols();
		int rowsB = matrixB.getNrRows();
		
		// Create arrows with text to show the current status of i, j and k
		// Hide them to show them when needed later on
		Polyline[] iArrows = new Polyline[rowsA];
		Text[] iTexts = new Text[rowsA];
		for (int i = 0; i < rowsA; i++) {
			int heightOffset = (int)matrixA.getProperties().get(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY)/2 + i*(int)matrixA.getProperties().get(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY) + 5 + (i+1)*12;
			iArrows[i] = lang.newPolyline(new Offset[]{(new Offset(-40, heightOffset, "matrixA", AnimalScript.DIRECTION_NW)), (new Offset(5, heightOffset, "matrixA", AnimalScript.DIRECTION_NW))}, "iPoly" + i, null, arrowProperties);
			iArrows[i].hide();
			iTexts[i] = lang.newText(new Offset(-40, -8, "iPoly" + i, AnimalScript.DIRECTION_W), "i = " + i, "itext" + i, null, textProperties);
			iTexts[i].hide();
		}
		Polyline[] jArrows = new Polyline[colsB];
		Text[] jTexts = new Text[colsB];
		for (int j = 0; j < colsB; j++) {
			int widthOffset = (int)matrixB.getProperties().get(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY)/2 + j*(int)matrixB.getProperties().get(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY) + 5 + (j+1)*12;
			jArrows[j] = lang.newPolyline(new Offset[]{(new Offset(widthOffset, -40, "matrixB", AnimalScript.DIRECTION_NW)), (new Offset(widthOffset, 5, "matrixB", AnimalScript.DIRECTION_NW))}, "jPoly" + j, null, arrowProperties);
			jArrows[j].hide();
			jTexts[j] = lang.newText(new Offset(-10, -30, "jPoly" + j, AnimalScript.DIRECTION_N), "j = " + j, "jtext" + j, null, textProperties);
			jTexts[j].hide();
		}
		Polyline[][] kArrows = new Polyline[colsA][2];
		Text[] kTexts = new Text[colsA];
		for (int k = 0; k < colsA; k++) {
			int heightOffset = (int)matrixB.getProperties().get(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY)/2 + k*(int)matrixB.getProperties().get(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY) + 5 + (k+1)*12;
			int widthOffset = (int)matrixB.getProperties().get(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY)/2 + k*(int)matrixB.getProperties().get(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY) + 5 + (k+1)*12;
			kArrows[k][0] = lang.newPolyline(new Offset[]{(new Offset(widthOffset, -40, "matrixA", AnimalScript.DIRECTION_NW)), (new Offset(widthOffset, 5, "matrixA", AnimalScript.DIRECTION_NW))}, "kPoly0" + k, null, arrowProperties);
			kArrows[k][1] = lang.newPolyline(new Offset[]{(new Offset(-40, heightOffset, "matrixB", AnimalScript.DIRECTION_NW)), (new Offset(5, heightOffset, "matrixB", AnimalScript.DIRECTION_NW))}, "kPoly1" + k, null, arrowProperties);
			kArrows[k][0].hide();
			kArrows[k][1].hide();
			kTexts[k] = lang.newText(new Offset(-10, -(heightOffset/(k+1))*(colsA-k), "kPoly0" + k, AnimalScript.DIRECTION_N), "k = " + k, "ktext" + k, null, textProperties);
			kTexts[k].hide();
		}
		
		// Initialize variables for the variable counter
		Variables vars = lang.newVariables();
		vars.declare("int", "i");
		vars.declare("int", "j");
		vars.declare("int", "k");
		
		// Run the algorithm
		for (int i = 0; i < rowsA; i++) {
			code.highlight(2);
			// Highlight row
			matrixA.highlightCellColumnRange(i, 0, colsA-1, null, null);
			
			iArrows[i].show();
			iTexts[i].show();
			
			vars.set("i", Integer.toString(i));
			
			lang.nextStep(i + "th Iteration");
			code.unhighlight(2);
			matrixA.unhighlightCellColumnRange(i, 0, colsA-1, null, null);
			for (int j = 0; j < colsB; j++) {
				code.highlight(3);
				// Highlight column
				matrixB.highlightCellRowRange(0, rowsB-1, j, null, null);
				
				jArrows[j].show();
				jTexts[j].show();
				
				vars.set("j", Integer.toString(j));
				
				lang.nextStep();
				code.unhighlight(3);
				matrixB.unhighlightCellRowRange(0, rowsB-1, j, null, null);
				for (int k = 0; k < colsA; k++) {
					code.highlight(4);
					
					// Highlight elements of A and B
					matrixA.highlightCell(i, k, null, null);
					matrixB.highlightCell(k, j, null, null);
					
					kArrows[k][0].show();
					kArrows[k][1].show();
					kTexts[k].show();
					
					vars.set("k", Integer.toString(k));
					
					lang.nextStep();
					
					code.unhighlight(4);

					matrixA.unhighlightCell(i, k, null, null);
					matrixB.unhighlightCell(k, j, null, null);
					
					code.highlight(5);
					// Put current result into respective result matrix cell
					result.put(i, j, result.getElement(i, j) + matrixA.getElement(i, k) + matrixB.getElement(k, j), null, null);
					
					// Highlight result element
					result.highlightCell(i, j, null, null);
					
					lang.nextStep();
					
					code.unhighlight(5);
					
					result.unhighlightCell(i, j, null, null);
					
					kArrows[k][0].hide();
					kArrows[k][1].hide();
					kTexts[k].hide();
				}
				jArrows[j].hide();
				jTexts[j].hide();
			}
			iArrows[i].hide();
			iTexts[i].hide();
		}
	}
    
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arrowProperties = (PolylineProperties)props.getPropertiesByName("arrowProperties");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        A = (int[][])primitives.get("A");
        B = (int[][])primitives.get("B");
        textProperties = (TextProperties)props.getPropertiesByName("textProperties");
        resultMatrixProperties = (MatrixProperties)props.getPropertiesByName("resultMatrixProperties");
        codeFrameProperties = (RectProperties)props.getPropertiesByName("codeFrameProperties");
        matrixProperties = (MatrixProperties)props.getPropertiesByName("matrixProperties");
        
        multiply();
        
        return lang.toString();
    }

    public String getName() {
        return "Matrixmultiplication";
    }

    public String getAlgorithmName() {
        return "Matrixmultiplication";
    }

    public String getAnimationAuthor() {
        return "Annemarie Mattmann";
    }

    public String getDescription(){
        return "Matrix multiplication refers to the multiplication of two matrices <b>A</b> and <b>B</b> and results in a new matrix <b>C</b> with as many rows as <b>A</b> and as many columns as <b>B</b>."
 +"\n"
 +"The latter can be remembered more easily if one writes the matrix <b>B</b> in the middle and the matrix <b>A</b> diagonally below it to the left side, then the result matrix can be fitted next to"
 +"\n"
 +"<b>A</b> and below <b>B</b>."
 +"\n"
 +"</br>"
 +"\n"
 +"To perform the computation of <b>A</b>*<b>B</b> the number of columns of <b>A</b> must equal the number of rows of the matrix <b>B</b> (which clarifies why matrix multiplication is not commutative)."
 +"\n"
 +"The product (i.e. each entry of the result matrix <b>C</b>) is then calulated by summing up the products of each element in one row of <b>A</b> and one column of <b>B</b> for all rows in <b>A</b> and all columns in <b>B</b>.";
    }

    public String getCodeExample(){
        return "def matrixmultiplication(matrixA, matrixB):"
 +"\n"
 +"  result = numpy.zeros([matrixA.shape[0], matrixB.shape[1]])"
 +"\n"
 +"  for i in range(0, matrixA.shape[0]):"
 +"\n"
 +"    for j in range(0, matrixB.shape[1]):"
 +"\n"
 +"      for k in range(0, matrixA.shape[1]):"
 +"\n"
 +"        result[i,j] += matrixA[i,k] * matrixB[k,j]"
 +"\n"
 +"  return result";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		A = (int[][])arg1.get("A");
        B = (int[][])arg1.get("B");
        // Throw an error when the number of columns in A does not equal the number of rows in B
		if (A[0].length != B.length) {
			JOptionPane.showMessageDialog(null, "The number of columns in A must equal the number of rows in B!");
			return false;
		}
		return true;
	}
    
}
