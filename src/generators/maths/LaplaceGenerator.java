package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class LaplaceGenerator implements ValidatingGenerator {
	
	// Generated fields:
    private Language lang;
    private int[][] intMatrix;
    private SourceCodeProperties sourceCode;
    private MatrixProperties matrix;
    private TextProperties headerProperties, introProperties, calculationProperties, variableProperties, counterProperties, outroProperties;
    
    // Custom fields:
	private SourceCode sc;
	private Text header, Text_N, Text_K, Text_Ergebnis, Text_Ebene, Text_ArithmeticCounter, Text_RecursionCounter;
	private int Ebene, ArithmeticCounter = 0, RecursionCounter = 0;

	// Generated functions:
    public void init(){
        lang = new AnimalScript("Determinantenberechnung nach Laplace [DE]", "Karsten Will, Stephan Wezorke", 800, 600);
        lang.setStepMode(true);
    }

    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {

	int[][] intMatrix = (int[][])primitives.get("intMatrix");
        if (intMatrix.length != intMatrix[0].length){
            throw new IllegalArgumentException("ERROR: The given matrix must be square!");
        }
        return true;
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        intMatrix = (int[][])primitives.get("intMatrix");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        matrix = (MatrixProperties)props.getPropertiesByName("matrix");
		matrix.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        
		headerProperties = (TextProperties)props.getPropertiesByName("headerProperties");
		introProperties = (TextProperties)props.getPropertiesByName("introProperties");
		calculationProperties = (TextProperties)props.getPropertiesByName("calculationProperties");
		variableProperties = (TextProperties)props.getPropertiesByName("variableProperties");
		counterProperties = (TextProperties)props.getPropertiesByName("counterProperties");
		outroProperties = (TextProperties)props.getPropertiesByName("outroProperties");
		
        run();
        
        return lang.toString();
    }

    public String getName() {
        return "Determinantenberechnung nach Laplace [DE]";
    }

    public String getAlgorithmName() {
        return "Determinantenberechnung nach Laplace [DE]";
    }

    public String getAnimationAuthor() {
        return "Karsten Will, Stephan Wezorke";
    }

    public String getDescription(){
        return "<p>Gem&auml;&szlig; dem Laplace&#039;schen Entwicklungssatz kann man die Determinante beliebig gro&szlig;er, quadratischer Matrizen berechnen."
 +"\n"
 +"Dazu werden systematisch die Determinanten bestimmter Untermatrizen berechnet, die entstehen wenn man eine Zeile und"
 +"\n"
 +"Spalte der Originalmatrix entfernt. Die Determinanten der Untermatrizen werden dann zur Berechnung der eigentlich gesuchten"
 +"\n"
 +"Determinante verwendet.</p>"
 +"\n"
 +"<p/>Da zur Berechnung der Determinante (und damit auch jeder Unterdeterminante) die Determinanten kleinerer Matrizen ausgewertet"
 +"\n"
 +"werden m&uuml;ssen, handelt es sich hier um einen rekursiven Algorithmus. Als Rekursionsanker dient die (1x1)-Matrix, deren"
 +"\n"
 +"Determinante ihr einziges Matrixelement ist.</p>"
 +"\n"
 +"<p>Die elementaren Rechenoperationen beim Berechnen der Determinante sind nicht zeitkritisch. Relevant f&uuml;r die Laufzeit"
 +"\n"
 +"sind die rekursiven Funktionsaufrufe; f&uuml;r jede (NxN)-Matrix werden N Unterdeterminanten berechnet, der Zeitaufwand ist"
 +"\n"
 +"folglich O(N!).</p>";
    }

    public String getCodeExample(){
        return "Berechne Determinante von quadtratischer Matrix M:"
 +"\n"
 +"	N := Anzahl der Spalten von M"
 +"\n"
 +"	Falls N == 0:"
 +"\n"
 +"		Ergebnis = NaN"
 +"\n"
 +"		Ende"
 +"\n"
 +"\n"
 +"	Falls N == 1"
 +"\n"
 +"		Ergebnis = M[0][0]"
 +"\n"
 +"		Ende"
 +"\n"
 +"\n"
 +"	Ergebnis = 0"
 +"\n"
 +"	Für K=0; K<N; K++:"
 +"\n"
 +"		Ergebnis = Ergebnis + (-1^K)*M[0][K] * Det(Matrix(M ohne Zeile 0 und Spalte K))"
 +"\n"
 +"	Ende";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    // Custom functions:
	/**
	 * Returns the submatrix of a given matrix.
	 * 
	 * @param matrix matrix to be reduced
	 * @param row row to be omitted
	 * @param column column to be omitted
	 * @return the submatrix
	 */
	private IntMatrix subMatrix(IntMatrix M, int row, int column, Offset o) {
		int l = M.getNrRows();
		int[][] empty = new int[l-1][l-1];

		IntMatrix result = lang.newIntMatrix(o, empty, "M1", null, this.matrix);
		int k = 0, m = 0;
		
		for(int i=0; i<l; i++) {
			if(i == row) continue;
			m = 0;
			for(int j=0; j<l; j++) {
				if(j == column) continue;
				result.put(k,  m,  M.getElement(i,  j), new MsTiming(0), new MsTiming(0));
				M.highlightCell(i, j, null, null);
				m++;
			}
			k++;
		}
		return result;
	}
	
	/**
	 * Calculates the determinant of a given matrix using Laplace's famous algorithm.
	 * 
	 * @param matrix a square matrix
	 * @return the determinant of the matrix
	 */
	private int determinante(IntMatrix matrix) {
		unsetVariables();
		matrix.show();
		incEbene();
		sc.toggleHighlight(13, 0);
		incRecursionCounter();
		lang.nextStep();
		
		IntMatrix subM1, subM2;
		
		setN(matrix.getNrRows());
		sc.toggleHighlight(0, 1);
		lang.nextStep();
		
		sc.toggleHighlight(1, 2);
		lang.nextStep();

		sc.toggleHighlight(2, 6);
		lang.nextStep();
		
		if(matrix.getNrRows() == 1) {
			sc.toggleHighlight(6, 7);
			calculationProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
			Text equation = lang.newText(new Offset(30, matrix.getNrRows()*14, matrix, AnimalScript.DIRECTION_NE),
					" = " + String.valueOf(matrix.getElement(0, 0)), "result", null, calculationProperties);
			setErgebnis(matrix.getElement(0, 0));
			lang.nextStep();
			
			sc.toggleHighlight(7, 8);
			lang.nextStep();
			
			decEbene();
			equation.hide();
			sc.unhighlight(8);
			return matrix.getElement(0, 0);
		}
		


		sc.toggleHighlight(6, 10);		
		int result = 0;
		setErgebnis(0);
		lang.nextStep();

		sc.unhighlight(10);		
		Text equation = null;
		for(int k=0; k<matrix.getNrRows(); k++) {
			setK(k);
			sc.highlight(11);
			lang.nextStep();
			
			


			sc.toggleHighlight(11, 12);
			if(equation != null) equation.hide();
			equation = lang.newText(new Offset(30, matrix.getNrRows()*14, matrix, AnimalScript.DIRECTION_NE),
					" = " + String.valueOf(result) + " + (-1)^" + k + " * " + matrix.getElement(0,  k) + " * Det", "result", null, calculationProperties);
				
			subM1 = subMatrix(matrix, 0, k, new Offset(10, -matrix.getNrRows()*14, equation, AnimalScript.DIRECTION_E));
			subM2 = subMatrix(matrix, 0, k, new Offset(40, matrix.getNrRows()*30+40, matrix, AnimalScript.DIRECTION_NW));
			subM2.hide();
			lang.nextStep();
			
			if(matrix.getElement(0,  k) != 0) {
				subM2.show();
				sc.unhighlight(12);
				double det = determinante(subM2);
				setErgebnis(result);
				setN(matrix.getNrRows());
				setK(k);
				double addend = Math.pow(-1, k)*matrix.getElement(0, k)*det;
				sc.highlight(12);
				unhighlightMatrix(matrix);
				subM1.hide();
				subM2.hide();
				subM2.hide(new MsTiming(0));
				equation.setText(" = " + String.valueOf(result) + " + (-1)^" + k + " * " + matrix.getElement(0,  k) + " * "  + det, null, null);
				lang.nextStep();
				
				result += addend;
			} else {
				if(equation != null) equation.hide();
				unhighlightMatrix(matrix);
				subM1.hide();
			}
			
			equation.hide();
			equation = lang.newText(new Offset(30, matrix.getNrRows()*14, matrix, AnimalScript.DIRECTION_NE),
					" = " + String.valueOf(result), "result", null, calculationProperties);
			if(matrix.getElement(0,  k) != 0) incArithmeticCounter(4);
			setErgebnis(result);
			lang.nextStep();
			
			sc.unhighlight(12);
		}
		
		sc.highlight(13);
		lang.nextStep();
		
		sc.unhighlight(13);
		equation.hide();
		decEbene();
		return result;
	}
	
	private void run() {
		initialText();
		showSourceCode();
		showVariables();
		
		IntMatrix M = lang.newIntMatrix(new Coordinates(10, 50), intMatrix, "M1", null, matrix);
		
		lang.nextStep("Berechnung");
		M.hide();
		incEbene(); // Set Counter to 0 so it will be correctly increased to 1 when
		decEbene(); // entering determinante() for the first time.
		
		int det = determinante(M);
		
		lang.hideAllPrimitives();
		header.show();
		M.show();
		Text_ArithmeticCounter.show();
		Text_RecursionCounter.show();
		
		finalText(M, det);
	}
	
	private void initialText() {
		headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 22));
		header = lang.newText(new Coordinates(270, 10), "Determinantenberechnung nach Laplace", "header", null, headerProperties);
		lang.nextStep("Einleitung");

		lang.newText(new Coordinates(140, 50), "Gemäß dem Laplace'schen Entwicklungssatz kann man die Determinante beliebig großer, quadratischer Matrizen berechnen.", "intro1", null, introProperties);
		lang.newText(new Coordinates(140, 75), "Dazu werden systematisch die Determinanten bestimmter Untermatrizen berechnet, die entstehen wenn man eine Zeile und", "intro2", null, introProperties);
		lang.newText(new Coordinates(140, 90), "Spalte der Originalmatrix entfernt. Die Determinanten der Untermatrizen werden dann zur Berechnung der eigentlich gesuchten", "intro3", null, introProperties);
		lang.newText(new Coordinates(140, 105), "Determinante verwendet.", "intro4", null, introProperties);
		lang.nextStep();

		lang.newText(new Coordinates(140, 140), "Da zur Berechnung der Determinante (und damit auch jeder Unterdeterminante) die Determinanten kleinerer Matrizen ausgewertet", "intro5", null, introProperties);
		lang.newText(new Coordinates(140, 155), "werden müssen, handelt es sich hier um einen rekursiven Algorithmus. Als Rekursionsanker dient die (1x1)-Matrix, deren", "intro6", null, introProperties);
		lang.newText(new Coordinates(140, 170), "Determinante ihr einziges Matrixelement ist.", "intro7", null, introProperties);
		lang.nextStep();
		
		lang.newText(new Coordinates(140, 205), "Zur vereinfachten Darstellung werden in dieser Visualisierung die Matrizen mit ihren Determinanten gleichgesetzt; es gibt", "intro8", null, introProperties);
		lang.newText(new Coordinates(140, 220), "keine zusätzlichen Betragsstriche um die Matrizen oder Funktionssymbole wie det(). Da keine Matrizen für sich stehen, sondern", "intro9", null, introProperties);
		lang.newText(new Coordinates(140, 235), "lediglich Determinanten augerechnet werden, kann es nicht zu Verwechslungen kommen.", "intro10", null, introProperties);
		lang.nextStep();
		
		lang.hideAllPrimitives();
		header.show();
	}
	
	private void showSourceCode() {
		sc = lang.newSourceCode(new Coordinates(670, 300), "sourceCode", null, sourceCode);
		sc.addCodeLine("Berechne Determinante von quadtratischer Matrix M:", null, 0, null);
		sc.addCodeLine("N := Anzahl der Spalten von M", null, 1, null);
		sc.addCodeLine("Falls N == 0:", null, 1, null);
		sc.addCodeLine("Ergebnis = NaN", null, 2, null);
		sc.addCodeLine("Ende", null, 2, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("Falls N == 1", null, 1, null);
		sc.addCodeLine("Ergebnis = M[0][0]", null, 2, null);
		sc.addCodeLine("Ende", null, 2, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("Ergebnis = 0", null, 1, null);
		sc.addCodeLine("Für K=0; K<N; K++:", null, 1, null);
		sc.addCodeLine("Ergebnis = Ergebnis + (-1^K)*M[0][K] * Det(Matrix(M ohne Zeile 0 und Spalte K))", null, 2, null);
		sc.addCodeLine("Ende", null, 2, null);
	}

	private void showVariables() {
		lang.newText(new Coordinates(1000, 50), "N", "N_key", null, variableProperties);
		lang.newText(new Coordinates(1000, 80), "K", "K_key", null, variableProperties);
		lang.newText(new Coordinates(1000, 110), "Ergebnis", "Ergebnis_key", null, variableProperties);
		lang.newText(new Coordinates(1000, 140), "Ebene", "Ebene_key", null, variableProperties);
		
		Text_N = lang.newText(new Coordinates(1090, 50), "", "N", null, variableProperties);
		Text_K = lang.newText(new Coordinates(1090, 80), "", "K", null, variableProperties);
		Text_Ergebnis = lang.newText(new Coordinates(1090, 110), "", "Ergebnis", null, variableProperties);
		Text_Ebene = lang.newText(new Coordinates(1090, 140), "", "Ebene", null, variableProperties);
		
		ArithmeticCounter = 0;
		RecursionCounter = 0;
		Text_ArithmeticCounter = lang.newText(new Coordinates(670, 180), "Anzahl elementarer Rechenoperationen: " + String.valueOf(ArithmeticCounter), "Counter", null, counterProperties);
		Text_RecursionCounter = lang.newText(new Coordinates(670, 195), "Anzahl Rekursionen: " + String.valueOf(RecursionCounter), "Counter", null, counterProperties);
	}
	
	private void setN(int N) {
		this.Text_N.setText(String.valueOf(N), null, null);
	}
	private void setK(int K) {
		this.Text_K.setText(String.valueOf(K), null, null);
	}
	private void setErgebnis(double Ergebnis) {
		this.Text_Ergebnis.setText(String.valueOf(Ergebnis), null, null);
	}
	private void incEbene() {
		Ebene++;
		Text_Ebene.setText(String.valueOf(Ebene), null, null);
	}
	private void decEbene() {
		Ebene--;
		Text_Ebene.setText(String.valueOf(Ebene), null, null);
	}
	private void unsetVariables() {
		Text_N.setText("", null, null);
		Text_K.setText("", null, null);
		Text_Ergebnis.setText("", null, null);
	}
	private void incArithmeticCounter(int increaseBy) {
		ArithmeticCounter += increaseBy;
		Text_ArithmeticCounter.setText("Anzahl elementarer Rechenoperationen: " + String.valueOf(ArithmeticCounter), null, null);
	}
	private void incRecursionCounter() {
		RecursionCounter++;
		Text_RecursionCounter.setText("Anzahl Rekursionen: " + String.valueOf(RecursionCounter), null, null);
	}
	
	/**
	 * Auxiliary function to unhighlight a matrix.
	 * 
	 * This function unhighlights every element; this is usefull if
	 * it is not tracked, which elements have been highlighted.
	 */
	private void unhighlightMatrix(IntMatrix M) {
		for(int i=0; i<M.getNrRows(); i++)
			M.unhighlightCellRowRange(0, M.getNrRows()-1, i, null, null);
	}
	
	private void finalText(IntMatrix M, int det) {
		lang.newText(new Offset(30, M.getNrRows()*14, M, AnimalScript.DIRECTION_NE), " = " + det, "result", null, calculationProperties);
		
		lang.newText(new Coordinates(60, 250), "Der Aufwand für die Berechnung nach dem Laplaceschen Entwicklungssatz für eine Matrix der Dimension n x n ist von der Ordnung O(n!).", "final1", null, outroProperties);
		lang.newText(new Coordinates(60, 265), "Die üblichen Verfahren haben hingegen oft nur einen Zeitaufwand von O(n^3). Dennoch kann der Laplacesche Entwicklungssatz bei kleinen Matrizen", "final2", null, outroProperties);
		lang.newText(new Coordinates(60, 280), "und Matrizen mit vielen Nullen gut angewendet werden.", "final3", null, outroProperties);

		int maxRecs = maxRecursions(intMatrix.length);
		if(maxRecs > RecursionCounter) {
			lang.nextStep();
			lang.newText(new Coordinates(60, 320), "Dazu ein paar Zahlen: Es wurden " + String.valueOf(RecursionCounter) + " Rekursionen und " + String.valueOf(ArithmeticCounter) + " arithmetische Rechenoperationen benötigt, um die Determinante der Matrix zu ", "final4", null, outroProperties);
			lang.newText(new Coordinates(60, 335), "berechnen. Ohne das Abkürzen des Algorithmus bei auftretenden Nullen wären " + String.valueOf(maxRecs) + " Rekusionen und " + String.valueOf(4*(maxRecs-1)) + " Rechenoperationen notwendig gewesen.", "final5", null, outroProperties);
			lang.newText(new Coordinates(60, 350), "Da die Entwicklung nach einer beliebigen Zeile oder Spalte erfolgen kann, wählt ein cleverer Algorithmus jene", "final6", null, outroProperties);
			lang.newText(new Coordinates(60, 365), "mit den meisten Nullen aus, um diesen positiven Aspekt des Laplace'schen Entwicklungssatzes optimal auszunutzen.", "final7", null, outroProperties);
		}
		lang.nextStep("Fazit");
	}
	
	private int maxRecursions(int matrixSize) {
		if(matrixSize == 1) return 1;
		else return 1 + matrixSize*maxRecursions(matrixSize - 1);
	}
}
