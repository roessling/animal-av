/*
 * Simplex.java
 * Antonio Kupresak, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import translator.Translator;
import translator.TranslatableGUIElement;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class SimplexKupresak implements Generator {
    private Language lang;
    private Translator translator;
    private Locale language;
    
    //external variables
    private String[][] constraints;
    private int numberOfNotBaseVars = 3;
    private int numberOfBasevars = 2;
    private MatrixProperties tableauProps;
    private String[] targetFunction;
    private SourceCodeProperties sourceCodeProps;
    private TextProperties explanationProps;
    private CircleProperties pointProps;
    


    public void init(){
        lang = new AnimalScript("SimplexKupresak", "Antonio Kupresak", 1050, 750);

    }
    
    public SimplexKupresak(Locale l){
    	language = l;
    	translator = new Translator("generators/misc/SimplexKupresakLanguageFile", language);
    	
    }
        

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        constraints = (String[][])primitives.get("constraints");	
        explanationProps = (TextProperties) props.getPropertiesByName("ExplationTextProps");
        pointProps = (CircleProperties) props.getPropertiesByName("PointProps");
        tableauProps = (MatrixProperties)props.getPropertiesByName("TableauProps");
        targetFunction = (String[])primitives.get("targetFunction");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("SourceCodeProps");
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        run();
        lang.finalizeGeneration();
		return lang.toString();
		
        
    }

    public String getName() {
        return "SimplexKupresak";
    }

    public String getAlgorithmName() {
        return "SimplexKupresak";
    }

    public String getAnimationAuthor() {
        return "Antonio Kupresak";
    }

    public String getDescription(){
        return translator.translateMessage("explLine0")+"\n"+
    			translator.translateMessage("explLine1")+"\n"+
    			translator.translateMessage("explLine2")+"\n"+
    			translator.translateMessage("explLine3")+"\n"+
    			translator.translateMessage("explLine4")+"\n"+
    			translator.translateMessage("explLine5")+"\n"+
        		translator.translateMessage("explLine6")+"\n"+
        		translator.translateMessage("explLine7")+"\n"+
        		translator.translateMessage("explLine8")+"\n"+
        		translator.translateMessage("explLine9")+"\n"+   		
        		translator.translateMessage("explLine10")+"\n"+
        		translator.translateMessage("explLine11")+"\n"+
        		translator.translateMessage("explLine12");
        
    }

    public String getCodeExample(){
        return translator.translateMessage("codeLine0")+"\n"+
        		translator.translateMessage("codeLine1")+"\n"+
        		translator.translateMessage("codeLine2")+"\n"+
        		translator.translateMessage("codeLine3")+"\n"+
        		translator.translateMessage("codeLine4")+"\n"+
        		translator.translateMessage("codeLine5")+"\n"+
        		translator.translateMessage("codeLine6");
        		
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return language;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    
    private static final String DESCRIPTION="";
	
	private static final String SOURCE_CODE = "";

	
	// internal variables
	private StringMatrix example;
	private DoubleMatrix tableauAnim;
	private SourceCode code;
	private Text explanation;
	private ArrayList<double[]> resultsList;
	private StringMatrix resultsAnim;
	private String[][] resultsTable;
	private Text headline;
	private double[][] tableau;
	
	private int[] coordinatesUpperLeft = new int[2];
	private int[] coordinatesLowerLeft = new int[2];
	private int[] coordinatesUpperRight = new int[2];
	private int[] coordinatesLowerRight = new int[2];

	
	
	private void calcCoordinates(){
		int offset = 20;
		
		coordinatesUpperLeft[0] = 20;
		coordinatesUpperLeft[1] = 100;
		
		int rows = tableau.length;
		int columns = tableau[0].length;
		

		coordinatesLowerLeft[0] = coordinatesUpperLeft[0];
		coordinatesLowerLeft[1] = coordinatesUpperLeft[1]+300;

		
		coordinatesUpperRight[0] = coordinatesUpperLeft[0] + 700;
		coordinatesUpperRight[1] = coordinatesUpperLeft[1];
		
		coordinatesLowerRight[0] = coordinatesUpperRight[0];
		coordinatesLowerRight[1] = coordinatesLowerLeft[1];
		
	}

	
	
	private void createGraph(double[][] tableau, int nullX, int nullY, int axisLength){
		
		lang.nextStep();
		explanation.setText(translator.translateMessage("createGraph.drawAxis"),null,null);
		int columns = tableau[0].length;
		int rows = tableau.length;
		
		int endY = nullY-axisLength;
		int endX = nullX+axisLength;
		
		Node nullPoint = new Coordinates(nullX,nullY);
		Node endXAxis = new Coordinates(endX,nullY);
		Node endYAxis = new Coordinates(nullX,endY);
		
		Node[] xAxisNodes = new Node[2];
		xAxisNodes[1] = nullPoint;
		xAxisNodes[0] = endXAxis;
		
		Node[] yAxisNodes = new Node[2];
		yAxisNodes[1] = nullPoint;
		yAxisNodes[0] = endYAxis;
		
		PolylineProperties axisProps = new PolylineProperties();
		axisProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
		
		Polyline xAxis = lang.newPolyline(xAxisNodes, "xAxis", null, axisProps);
		lang.newText(new Offset(10,0,"xAxis","E"),"x1","xAxisLabel",null);
		Polyline YAxis = lang.newPolyline(yAxisNodes, "yAxis", null, axisProps);
		lang.newText(new Offset(-10,-20,"yAxis","N"),"x2","yAxisLabel",null);

		lang.nextStep();
		explanation.setText(translator.translateMessage("createGraph.drawConstraints1"),null,null);
		lang.nextStep();
		explanation.setText(translator.translateMessage("createGraph.drawConstraints2"), null, null);
		lang.nextStep();
		drawLines(tableau, nullX, nullY, axisLength);
		
		
	}
	double scaleFactor=1;
	
	private void drawLines(double[][] t, int nullX, int nullY, int axisLength){
		double max = 0;
		
			for (double[] line : t){
				double x1 = line[0];
				double x2 = line[1];
				double b = line[line.length-2];
				
				double xAbschn=Double.MIN_VALUE;
				double yAbschn=Double.MIN_VALUE;
				
				if (x1 != 0.0) xAbschn = b/x1;
				if (x2 != 0.0) yAbschn = b / x2;
				
				max = Math.max(max, Math.max(xAbschn, yAbschn));
				
		}
		lang.newText(new Offset(-20,10,"xAxis","SE"),Double.toString(max),"xAxisMax",null);
		lang.newText(new Offset(-50,10,"yAxis","NW"),Double.toString(max),"yAxisMax",null);

		

		scaleFactor = axisLength / max;	
		
		  for (int i = 0; i<t.length-1; i++){
			double[] line = t[i];
			double x1 = line[0];
			double x2 = line[1];
			double b = line[line.length-2];
			
			double xAbschn=Double.MIN_VALUE;
			double yAbschn=Double.MIN_VALUE;
			
			if (x1 != 0) xAbschn = b/x1;
			if (x2 != 0) yAbschn = b / x2;
			
			Node xAxisAbschnPoint=null;
			Node yAxisAbschnPoint=null;
			
			if (xAbschn!=Double.MIN_VALUE && yAbschn!=Double.MIN_VALUE){
				xAxisAbschnPoint = new Coordinates(nullX + (int) (xAbschn*scaleFactor) , nullY);
				yAxisAbschnPoint = new Coordinates(nullX, nullY-(int)(yAbschn*scaleFactor));
				
			}
			
			if (xAbschn==Double.MIN_VALUE && yAbschn!=Double.MIN_VALUE){
				xAxisAbschnPoint = new Coordinates(nullX +  axisLength ,nullY-(int)(yAbschn*scaleFactor));
				yAxisAbschnPoint = new Coordinates(nullX, nullY-(int)(yAbschn*scaleFactor));
			}
			
			if (xAbschn!=Double.MIN_VALUE && yAbschn==Double.MIN_VALUE){
				xAxisAbschnPoint = new Coordinates(nullX + (int) (xAbschn*scaleFactor) , nullY);
				yAxisAbschnPoint = new Coordinates(nullX + (int) (xAbschn*scaleFactor), nullY-axisLength);
			}

			Node[] nodes = new Node[2];
			nodes[0] = xAxisAbschnPoint;
			nodes[1] = yAxisAbschnPoint;
			
			explanation.setText(translator.translateMessage("drawLines.label")+(i+1), null, null);
			Polyline polyline = lang.newPolyline(nodes, "line"+i, null);
			lang.nextStep();
		}
		
	}
	
	
	private void createPoint(double[] lsg, int nullX, int nullY, int axisLength, String name){


		lang.newCircle(new Offset((int)(lsg[0]*scaleFactor),(int) (-lsg[1]*scaleFactor),"xAxis", "SW"), 3 ,name, null, pointProps);
		lang.newText(new Offset(-15,-15,name,"NW"),name, name+"Label", null);
	}
	
	
	private void showCode(){
		code = lang.newSourceCode(new Offset(0,10, headline, AnimalScript.DIRECTION_NW), "code", null, sourceCodeProps);
		code.addCodeLine(translator.translateMessage("codeLine0"), "line1", 1, null);
		code.addCodeLine(translator.translateMessage("codeLine1"), "line2", 1, null);
		code.addCodeLine(translator.translateMessage("codeLine2"), "line3", 2, null);
		code.addCodeLine(translator.translateMessage("codeLine3"), "line4", 2, null); 
		code.addCodeLine(translator.translateMessage("codeLine4"), "line5", 2, null);
		code.addCodeLine(translator.translateMessage("codeLine5"), "line6", 2, null);
		code.addCodeLine(translator.translateMessage("codeLine6"), "line7", 2, null);
	}
	
	private String printDoubleArray(double[] array){
		
		String result = "(";
		
		for (int i = 0; i<array.length;i++){
			result+=SimplexMathFunctions.round(array[i]);
			if (i!=array.length-1) result+=",";
		}
		
		result+=")";
		return result;
			
	}
	
	
	
	/**
	 * Takes the initial parameters and converts the values into a double[][] in order to use them afterwards
	 * @return A double[][] that contains all the needed values
	 */
	private void prepareForSimplex(){
		double[][] result = new double[numberOfNotBaseVars+1][numberOfBasevars+numberOfNotBaseVars+2];
		lang.setStepMode(true);
		resultsList = new ArrayList<double[]>();
		
		for (int i = 0; i<result.length; i++){
			for ( int j = 0; j<result[0].length; j++){
				if (i!=numberOfNotBaseVars){
					if (j<numberOfBasevars) result[i][j] = Double.parseDouble(constraints[i][j]);
					else if (j==i+numberOfBasevars) result[i][j] = 1;
					else if (j==result[0].length-2) result[i][j] = Double.parseDouble(constraints[i][numberOfBasevars]);
					else result[i][j] = 0;
				}
				else
					if (j<numberOfBasevars) result[i][j] = -Double.parseDouble(targetFunction[j]);
					else if (j==result[0].length-2) result[i][j]	= Double.parseDouble(targetFunction[targetFunction.length-1]);
					else result[i][j] = 0;
					
			}
		}
		tableau = result;

	}
	
	private void clearLastColumn(DoubleMatrix d){
		for (int i = 0; i<d.getNrRows(); i++){
			d.put(i, d.getNrCols()-1, 0.0, null, null);
		}
	}
	
	public void simplex() {
		
		int nBV = numberOfBasevars;
		int nSV = numberOfNotBaseVars;
		int columns = tableau[0].length;
		int rows = tableau.length;
		
		double[] aktLoesung = new double[nBV+nSV];
		int[] bvRowIndizes = new int[nBV+nSV];
		
		for (int i = 0; i<aktLoesung.length; i++){
			if (i<nBV){
				aktLoesung[i]=0;
				bvRowIndizes[i]=-1;
			}
			else {
				aktLoesung[i] = tableau[i-nBV][columns-2];
				bvRowIndizes[i] = i-nBV;
			}
			
			
		}
		
		resultsList.add(aktLoesung);
		

		lang.setStepMode(true);
		
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 24));
		lang.newText(new Coordinates(20,30), translator.translateMessage("title"), "title", null,titleProps);

		lang.nextStep("Einführung");
		
		String[][] beschr ={
				{translator.translateMessage("explLine0"),""},
				{translator.translateMessage("explLine1"),""},
				{translator.translateMessage("explLine2"),""},
				{translator.translateMessage("explLine3"),""},
				{translator.translateMessage("explLine4"),""},
				{translator.translateMessage("explLine5"),""}				
		};

		
		StringMatrix beschreib = lang.newStringMatrix(new Coordinates(20,110), beschr, "beschreibung", null);
		
		lang.nextStep();
		
		beschreib.hide();
		
		TextProperties title2Props = new TextProperties();
		title2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.ITALIC, 20));
		headline = lang.newText(new Coordinates(20,100), translator.translateMessage("subtitle"), "title2", null,title2Props);
		
		lang.nextStep();
		
		showCode();
		
		lang.nextStep();
		headline.moveBy("translate", 20, 240, null, new TicksTiming(20));
		code.moveBy("translate", 20, 245, null, new TicksTiming(20));
		
		
		lang.nextStep("Beispiel");
		
		// Beispiel als StringMatrix
		///////////////////////////////////////////////////////////////////////////
		String[][] stringMat = new String[rows+3][columns+1];
		for (int i = 0 ; i<rows+3;i++){
			for (int j = 0; j<columns+1;j++){
				stringMat[i][j]="";
			}
			
		}
		
		stringMat[0][0] = translator.translateMessage("exampleLine0");
		
		stringMat[1][1] = targetFunction[0];
		stringMat[1][2] = "* x1";
		stringMat[1][3] = "+";
		stringMat[1][4] = targetFunction[1];
		stringMat[1][5] = "* x2";
		
		stringMat[2][0] = translator.translateMessage("exampleLine2");
		
		for (int i = 3; i<6;i++){
			for (int j=1;j<8;j++){
				
				if (j==1) stringMat[i][j] = constraints[i-3][0];
				if (j==2) stringMat[i][j] = "* x1";
				if (j==3) stringMat[i][j] = "+";
				if (j==4) stringMat[i][j] = constraints[i-3][1];
				if (j==5) stringMat[i][j] = "* x2";
				if (j==6) stringMat[i][j] = "<=";
				if (j==7) stringMat[i][j] = constraints[i-3][2];
			}
			
		}

		example = lang.newStringMatrix(new Coordinates (20,125), stringMat, "BSP",null);
		Text bspLabel = lang.newText(new Offset(5, -10, "BSP", "NW"), translator.translateMessage("exampleLabel"), "bsp-label", null);
		///////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////

		lang.nextStep();
		
		bspLabel.moveBy("translate", 600, 0, null, new TicksTiming(20));
		example.moveBy("translate", 600, 0, null, new TicksTiming(20));
		
		lang.nextStep("Tableau");


		
		//Tableau erstellen
		///////////////////////////////////////////////////////////////////////////		
		
		// leeres erstellen

		double[][] empty =  new double[rows][columns];
	
		for (int i = 0; i<rows; i++ ){
			for (int j = 0; j<columns; j++){
				empty[i][j]=0;
			}
		}
			
			
		tableauAnim = lang.newDoubleMatrix(
				new Coordinates(60, 125), empty, "tableau", null, tableauProps);
		tableauAnim.hide();
		
		String text = translator.translateMessage("createTableau");

		explanation =  lang.newText(new Offset(10, 20, "tableau", "SW"), text , "Erkl",null, explanationProps);
		code.highlight(0);
		lang.nextStep();

		tableauAnim.show();		
		lang.nextStep();
		
		//// labels dranschreiben 
		///////////////////////////////////////////////////////////////////////////
		explanation.setText(translator.translateMessage("writeLabels"), null, null);
		lang.nextStep();
		int dx = 10;
		int dy = -15;
		int c = 1;
		for (int i =0; i<columns-2;i++){
			lang.newText(new Offset (dx,dy,"tableau","NW"), "x"+c, "x", null);
			dx+= (70+8);
			c++;
		}
		lang.newText(new Offset (dx,dy,"tableau","NW"), "b", "x", null);
		dx+= (70+8);
		lang.newText(new Offset (dx,dy,"tableau","NW"), "b/a", "x", null);

		dx = -15;
		dy = 5;
		c = nBV+1;
		int rowLabel = 1;
		List<Text> rowLabelList = new ArrayList<Text>();
		for (int j = 0; j<rows-1;j++){
			rowLabelList.add(lang.newText(new Offset (dx,dy,"tableau","NW"), "x"+c, Integer.toString(rowLabel), null));
			dy+= (20+8);
			c++;
			rowLabel++;
		}
		lang.newText(new Offset (dx,dy,"tableau","NW"), "-F", "-F", null);
		///////////////////////////////////////////////////////////////////////////


		lang.nextStep();
		//// tableau initial befüllen
		
		
		explanation.setText(translator.translateMessage("fillCoefficients"), null, null);

		tableauAnim.highlightCell(0, 0, null, null);
		example.highlightCell(3,1,null,null);
		lang.nextStep();
		tableauAnim.put(0, 0, SimplexMathFunctions.round(tableau[0][0]), null, null);
		tableauAnim.unhighlightCell(0, 0, null, null);
		example.unhighlightCell(0,1,null,null);
		lang.nextStep();

		
		for (int i = 0; i<3;i++){
			for (int j = 0; j<2;j++){
				tableauAnim.highlightCell(i, j, null, null);
				if(j==0) example.highlightCell(i+3,j+1,null,null);
				else example.highlightCell(i+3,j+3,null,null);
				lang.nextStep();
				tableauAnim.put(i, j, SimplexMathFunctions.round(tableau[i][j]), null, null);
				lang.nextStep();
				tableauAnim.unhighlightCell(i, j, null, null);
				if(j==0) example.unhighlightCell(i+3,j+1,null,null);
				else example.unhighlightCell(i+3,j+3,null,null);
				lang.nextStep();
				}
				tableauAnim.highlightCell(i, 5, null, null);
				example.highlightCell(i+3,7,null,null);
				lang.nextStep();
				tableauAnim.put(i, 5, SimplexMathFunctions.round(tableau[i][5]), null, null);
				tableauAnim.unhighlightCell(i, 5, null, null);
				example.unhighlightCell(i+3,7,null,null);
				lang.nextStep();
		}
		

		explanation.setText(translator.translateMessage("fillFRow"), null, null);
		lang.nextStep();
		
			for (int j = 0; j<2;j++){
				tableauAnim.highlightCell(3, j, null, null);
				if(j==0) example.highlightCell(1,j+1,null,null);
				else example.highlightCell(1,j+3,null,null);
				lang.nextStep();
				tableauAnim.put(3, j, SimplexMathFunctions.round(tableau[3][j]), null, null);
				lang.nextStep();
				tableauAnim.unhighlightCell(3, j, null, null);
				if(j==0) example.unhighlightCell(1,j+1,null,null);
				else example.unhighlightCell(1,j+3,null,null);	
				lang.nextStep();
			}

		
		explanation.setText(translator.translateMessage("identMatrix"), null, null);
		lang.nextStep();
		
		
		for (int i = 0; i<rows-1;i++){
			tableauAnim.highlightCellColumnRange(i, nBV, columns-3, null, null);
			for (int j = nBV; j<columns-2;j++){
				if (i==j-nBV) {
					tableauAnim.put(i,j,1.0, null, null);
				}
			}
		}
		lang.nextStep();
		for (int i = 0; i<rows-1;i++){
			tableauAnim.unhighlightCellColumnRange(i, nBV, columns-3, null, null);
		}
		
		lang.nextStep();
		///////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////
		explanation.setText(translator.translateMessage("tableFinished"), null, null);
		
		lang.nextStep("Graph");
		
		MultipleChoiceQuestionModel graphPossible = new MultipleChoiceQuestionModel("Graph");
		graphPossible.setPrompt(translator.translateMessage("GraphQuestion"));
		graphPossible.addAnswer(translator.translateMessage("GraphQuestion.rightAnswer"), 1, translator.translateMessage("GraphQuestion.rightAnswerExplanation"));
		graphPossible.addAnswer(translator.translateMessage("GraphQuestion.wrongAnswer1"), 0, translator.translateMessage("GraphQuestion.wrongAnswer1Explanation"));
		graphPossible.addAnswer(translator.translateMessage("GraphQuestion.wrongAnswer2"), 0, translator.translateMessage("GraphQuestion.wrongAnswer2Explanation"));
		lang.addMCQuestion(graphPossible);

		// Graphen erstellen
	
		if (nBV == 2){
		explanation.setText(translator.translateMessage("graphPossible"), null, null);
		lang.nextStep();
		example.hide();
		createGraph(tableau, 700, 325, 200);
		lang.nextStep();
		}

		
		// Startlösung als 1. Basislösung
		///////////////////////////////////////////////////////////////////////////
		
		explanation.setText(translator.translateMessage("firstSolution")+printDoubleArray(aktLoesung), null, null);
		
		lang.nextStep();
		
		resultsTable = new String[20][3];
		
		for (int i = 0; i<20;i++){
			for (int j = 0; j<3;j++){
				resultsTable[i][j]="";
			}
		}
		
		resultsTable[0][0] = "i";
		resultsTable[0][1] = translator.translateMessage("resultTable");
		resultsTable[0][2] = "F";
		
		resultsTable[1][0]="0";
		resultsTable[1][1]=printDoubleArray(aktLoesung);
		resultsTable[1][2]="0.0";
		
		MatrixProperties loesungsMatrixProps = new MatrixProperties();
		resultsAnim= lang.newStringMatrix(new Offset(20, 0, "code","NE" ), resultsTable, "lsg", null, loesungsMatrixProps);
		
		if (nBV == 2) createPoint(aktLoesung, 600, 325, 200, "L0");

		///////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////

		// eigentlichen Algorithmus durchführen
		///////////////////////////////////////////////////////////////////////////

		double[] fRow = tableau[rows - 1];
		int pivotRowIndex;
		int pivotColumnIndex;
		boolean negativeValues = false;
	

		for (double d : fRow) {
			if (d < 0)
				{
				negativeValues = true;
				}
		}
		lang.nextStep("Algorithmus Start");
		code.toggleHighlight(0, 1);
		
		lang.nextStep();
		
		int iteration = 1;
		while (negativeValues) {
			
			code.highlight(2);
			explanation.setText(translator.translateMessage("smallestFValue"), null, null);
			lang.nextStep();
			tableauAnim.highlightCellColumnRange(rows-1, 0, rows-1, null, null);

			// Pivotspalte finden
			pivotColumnIndex = SimplexMathFunctions.findMinIndexInRow(fRow);
			
			lang.nextStep();
			explanation.setText(translator.translateMessage("pivotColumn"), null, null);
			tableauAnim.unhighlightCellColumnRange(rows-1, 0, rows-1, null, null);
			tableauAnim.highlightCell(rows-1, pivotColumnIndex, null, null);

			lang.nextStep();
			for (int i = 0; i < rows; i++) {
				tableauAnim.highlightCell(i, pivotColumnIndex, null, null);
			}
			lang.nextStep();
			code.toggleHighlight(2,3);
			explanation.setText(translator.translateMessage("calcBA"), null, null);
			
			lang.nextStep();
			if (iteration==1){
			MultipleChoiceQuestionModel unlimited = new MultipleChoiceQuestionModel("Unlimited");
			unlimited.setPrompt(translator.translateMessage("UnlimitedQuestion"));
			unlimited.addAnswer(translator.translateMessage("UnlimitedQuestion.rightAnswer"), 1, translator.translateMessage("UnlimitedQuestion.rightAnswerExplanation"));
			unlimited.addAnswer(translator.translateMessage("UnlimitedQuestion.wrongAnswer1"), 0, translator.translateMessage("UnlimitedQuestion.wrongAnswer1Explanation"));
			unlimited.addAnswer(translator.translateMessage("UnlimitedQuestion.wrongAnswer2"), 0, translator.translateMessage("UnlimitedQuestion.wrongAnswer2Explanation"));
			lang.addMCQuestion(unlimited);
			}
			if (SimplexMathFunctions.checkIfColumnAllNegative(tableau, pivotColumnIndex)){
				explanation.setText(translator.translateMessage("unlimited"), null, null);
				lang.nextStep();
				explanation.setText(translator.translateMessage("noSolution"), null, null);
				return;
				
			}

			// b/a berechnen
			for (int i = 0; i < rows - 1; i++) {
				if (tableau[i][pivotColumnIndex] <= 0) {
					tableau[i][columns - 1] = Double.MAX_VALUE;
					tableauAnim.put(i, columns - 1, Double.NaN, null, null);
					lang.nextStep();
				}

				else {
					double value = tableau[i][columns - 2]
							/ tableau[i][pivotColumnIndex];
					tableau[i][columns - 1] = value;
					explanation.setText(SimplexMathFunctions.round(tableau[i][columns - 2])+" / "+SimplexMathFunctions.round(tableau[i][pivotColumnIndex])+" = "+value, null, null);
					tableauAnim.put(i, columns - 1, SimplexMathFunctions.round(value), null, null);
					lang.nextStep();

				}


			}
			
			explanation.setText(translator.translateMessage("smallestBA"), null, null);
			
			lang.nextStep();
			// PivotZeile finden
			pivotRowIndex = SimplexMathFunctions.findMinIndexInColumnWithoutLast(tableau,
					columns - 1);
			
			
			tableauAnim.highlightCell(pivotRowIndex, columns - 1, null, null);
			lang.nextStep();
			
			explanation.setText(translator.translateMessage("pivotRow"), null, null);

			for (int i = 0; i < rows; i++) {
				if (i != pivotRowIndex)
					tableauAnim
							.unhighlightCell(i, pivotColumnIndex, null, null);
			}
			tableauAnim.unhighlightCell(pivotRowIndex, columns - 1, null, null);
			lang.nextStep();
			explanation.setText(translator.translateMessage("pivotElem"), null, null);
			
			SimplexMathFunctions.clearLastColumn(tableau);
			clearLastColumn(tableauAnim);
			
			lang.nextStep();
			code.toggleHighlight(3,4);
			explanation.setText(translator.translateMessage("bvSwap"), null,null);
			lang.nextStep();
			
			// BV Tausch
			
			int alteBasis = Integer.parseInt(rowLabelList.get(pivotRowIndex).getText().substring(1, 2));
			bvRowIndizes[alteBasis-1] = -1;
			
			int neueBasis = pivotColumnIndex+1;
			bvRowIndizes[neueBasis-1] = pivotRowIndex;
			
			rowLabelList.get(pivotRowIndex).setText("x"+Integer.toString(pivotColumnIndex+1), null, null);
			rowLabelList.get(pivotRowIndex).changeColor(AnimalScript.COLORCHANGE_COLOR,Color.RED, null, null);
			
			
			
			lang.nextStep();
			rowLabelList.get(pivotRowIndex).changeColor(AnimalScript.COLORCHANGE_COLOR,Color.BLACK, null, null);
			lang.nextStep();
			code.toggleHighlight(4,5);
			code.highlight(6);
			explanation.setText(translator.translateMessage("transformPivotRow") + "(/ "+SimplexMathFunctions.round(tableau[pivotRowIndex][pivotColumnIndex])+")", null, null);
			
			lang.nextStep();
			
			
			
			// Pivotzeile normieren
			tableauAnim.unhighlightCell(pivotRowIndex, pivotColumnIndex, null,
					null);
			
			SimplexMathFunctions.rowMultiply(tableau[pivotRowIndex],
					1 / (tableau[pivotRowIndex][pivotColumnIndex]));
			for (int i = 0; i < columns; i++) {
				tableauAnim.put(pivotRowIndex, i,
						SimplexMathFunctions.round(tableau[pivotRowIndex][i]), null, null);
			}
			lang.nextStep();
			
			explanation.setText(translator.translateMessage("transformOthers"), null, null);
			lang.nextStep();
			// Alle anderen Zeilen addieren
			for (int i = 0; i < rows; i++) {
				if (i != pivotRowIndex) {
					explanation.setText(translator.translateMessage("row")+(i+1)+" += "+"-"+SimplexMathFunctions.round(tableau[i][pivotColumnIndex])+" * "+translator.translateMessage("row")+(pivotRowIndex+1), null, null);
					lang.nextStep();
					SimplexMathFunctions.rowPlusWithFactor(tableau[i], tableau[pivotRowIndex], -1
							* tableau[i][pivotColumnIndex]);
					for (int k = 0; k < columns; k++) {
						tableauAnim.put(i, k, SimplexMathFunctions.round(tableau[i][k]), null,
								null);
					}

					lang.nextStep();

				}
			}
			code.unhighlight(5);
			code.unhighlight(6);
			
			
			
			aktLoesung = new double[nBV+nSV];
		
			for (int i = 0; i<aktLoesung.length; i++){
				
				if (bvRowIndizes[i] == -1) aktLoesung[i]=0;
						
				else
					aktLoesung[i]=SimplexMathFunctions.round(tableau[bvRowIndizes[i]][columns-2]);
				
			}
			
		
			resultsList.add(aktLoesung);
	
			
			explanation.setText(translator.translateMessage("iterFinish")+printDoubleArray(resultsList.get(resultsList.size()-1)), null, null);
			lang.nextStep();
			//highlight
			for (int i : bvRowIndizes){
				if (i!=-1){
					tableauAnim.highlightCell(i,5,null,null);
				}
			}
			for (Text t : rowLabelList){
				t.changeColor(AnimalScript.COLORCHANGE_COLOR,Color.RED, null, null);
			}
			lang.nextStep();
			resultsAnim.put(iteration+1, 0, Integer.toString(iteration), null, null);
			resultsAnim.put(iteration+1,1,printDoubleArray(aktLoesung), null, null);
			resultsAnim.put(iteration+1,2,Double.toString(tableau[rows-1][columns-2]), null, null);
			
			createPoint(aktLoesung, 600, 325, 200, "L"+Integer.toString(iteration));
			lang.nextStep();
			//unhighlight
			for (int i : bvRowIndizes){
				if (i!=-1){
					tableauAnim.unhighlightCell(i,5,null,null);
				}
			}
			for (Text t : rowLabelList){
				t.changeColor(AnimalScript.COLORCHANGE_COLOR,Color.BLACK, null, null);
			}
			lang.nextStep();
			negativeValues = false;
			for (double d : fRow) {
				if (d < 0)
					negativeValues = true;
			}
			iteration++;
		
	 }	

		
		code.unhighlight(1);
		explanation.setText(translator.translateMessage("noNegF"), null, null);
		lang.nextStep("Algorithmus beendet");
		explanation.setText(translator.translateMessage("done"), null, null);
		lang.nextStep();
		explanation.setText(translator.translateMessage("opt")+ printDoubleArray(resultsList.get(resultsList.size()-1))+" mit  F = "+SimplexMathFunctions.round(tableau[rows-1][columns-2]), null, null);
				SimplexMathFunctions.roundMatrix(tableau);
		lang.nextStep();
		lang.hideAllPrimitives();
		resultsAnim.hide();
		String[][] finalText ={
				{translator.translateMessage("finishLine0"),""},
				{translator.translateMessage("finishLine1"),""},
				{translator.translateMessage("finishLine2"),""},
				{translator.translateMessage("finishLine3"),""},
				{translator.translateMessage("finishLine4"),""},
				{translator.translateMessage("finishLine5.1")+(iteration-1)+translator.translateMessage("finishLine5.2"),""}					
		};
		
		StringMatrix finalTextAnim = lang.newStringMatrix(new Coordinates(20,110), finalText, "finalText", null);
		
		
	
	}
	
	public void run(){
	    prepareForSimplex();
		calcCoordinates();
		simplex();
	}

	static class SimplexMathFunctions{

			/**
			 * Gibt den Index des kleinsten Werts eines eindimensionalen double Arrays zurück
			 * @param array das zu durchsuchende Array
			 * @return den Index des kleinsten Werts (falls alle gleich groß, dann 0)
			 */
			public static int findMinIndexInRow(double[] array) {
				int index = 0;

				for (int a = 0; a < array.length; a++) {
					if (array[a] < array[index])
						index = a;
				}
				return index;
			}
			
			/**
			 * Gibt den Zeilenindex des kleinsten Wertes einer Spalte in einem zweidimensionalen Double Array zurück
			 * dabei wird die letzte Zeile ignoriert 
			 * @param array das zu durchsuchende Array
			 * @param columnIndex Index der zu durchsuchenden Spalte
			 * @return Zeilenindex des kleinsten Wertes in der Spalte
			 */
			public static int findMinIndexInColumnWithoutLast(double[][] array,
					int columnIndex) {

				int index = 0;

				for (int a = 0; a < array.length - 1; a++) {
					if (array[a][columnIndex] < array[index][columnIndex])
						index = a;

				}
				return index;
			}

			public static void clearLastColumn(double[][] array){
				for (double[]  r : array){
					r[r.length-1]=0;
				}
			}
			
			public static boolean checkIfColumnAllNegative(double[][] array, int columnIndex){
				for (double [] r : array){
					if (r[columnIndex]>0) return false;
				}
				return true;
			}
			
			/**
			 * Multipliziert alle Zahlen eines Double Arrays mit einer anderen Double Zahl
			 * (zum Beispiel um Matrixtransformationen im Gauß Verfahren durchzuführen)
			 * @param row Das zu mulitiplizierende Array
			 * @param factor Der Faktor mit dem die Zahlen multipliziert werden sollen
			 */
			public static void rowMultiply(double[] row, double factor) {
				for (int i = 0; i < row.length; i++) {
					if (row[i]!=Double.MAX_VALUE)
							row[i] = row[i] * factor;
				}
			}
			

			/**
			 * Addiert den Inhalt eines Double Arrays B (mulitipliziert mit einem Faktor), zu den Inhalten eines 
			 * anderen Double Arrays
			 * ACHTUNG: Arrays müssen die selbe Größe haben
			 * (Zum Beispiel um Matrixtransformationen im Gauß Verfahren durchzuführen)
			 * @param rowA Array dessen Werte verändert werden soll
			 * @param rowB Array das zu rowA hinzugefügt werden soll
			 * @param factor Faktor mit dem rowB multipliziert werden soll
			 * @return void (rowA = rowA + rowB * factor)
			 */
			public static void rowPlusWithFactor(double[] rowA, double[] rowB, double factor) {
				if (rowA.length!=rowB.length) System.err.println("ERROR: Arrays must have the same length");
				else
				for (int i = 0; i < rowA.length; i++) {
					if (rowA[i]!=Double.MAX_VALUE && rowB[i]!=Double.MAX_VALUE)
					rowA[i] = rowA[i] + rowB[i] * factor;
				}
			}
			
			


			/**
			 * rundet alle Werte einer zweidimensionalen Double Matrix mit der Funktion @see round
			 * @param matrix Matrix deren Werte gerundet werden sollen
			 */
			public static void roundMatrix(double[][] matrix) {
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[i].length; j++) {
						matrix[i][j] = round(matrix[i][j]);
					}
				}
			}
			


			/**
			 * rundet eine Double Zahl auf Zwei Nachkommastellen mithilfe von @see Math.round(double a)
			 * @param die zu rundende Double Zahl
			 * @return die gerundete Zahl
			 */
			public static double round(double d) {
					return Math.round(d * 100.0) / 100.0;
			}
			
			/**
			 * gibt den größten Wert einer zweidimensionalen Double Matrix zurück
			 * @param mat die zu durchsuchende Matrix
			 * @return den Wert des größten Matrixeintrags (Wenn alle Werte gleichgroß sind wird der Wert mit dem Index [0][0] zurück gegeben
			 */
			public static double getMaxInMatrix(double[][] mat){
				double max = mat[0][0];
				
				for (int i = 0; i<mat.length; i++){
					for (int j = 0 ; j<mat[0].length;j++){
						if (mat[i][j]>max) max = mat[i][j];
					}
				}
				return max;
				
			}
			
	}

}
