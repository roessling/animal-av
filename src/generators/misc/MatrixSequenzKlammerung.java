/*
 * MatrixSequenzKlammerung.java
 * Igor Braun, Vladimir Bolgov, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import animal.variables.VariableRoles;

public class MatrixSequenzKlammerung implements Generator {
    private Language lang;

    private StringMatrix m;
	private StringMatrix s;
	
	private IntArray p; 
	
	private SourceCode sourceCode;
	private SourceCode sourceCodeKlammer;
	
	private Text optimalKlammerCallParam1;
	private Text optimalKlammerCallParam2;
	private Text optimalKlammerCallParam3;
	private Text optimalKlammerCallParam4;
	private Text optimalKlammerCallParam6;
	private Text optimalKlammerCallParam7;
	
	// Statistik
	private int lesendeZugriffeArrayP;
	private int lesendeZugriffeMatrixM;
	private int lesendeZugriffeMatrixS;
	private int schreibendeZugriffeMatrixM;
	private int schreibendeZugriffeMatrixS;
	
	private TextProperties h2TextProperties;
	private TextProperties pTextProperties;
	private TextProperties pGreyTextProperties;
	private TextProperties pBoldTextProperties;
	
	Variables v;
	
	private int[][] mIntMatrix;
	private int[][] sIntMatrix;
	
	private boolean showedIfQDescription;
	private boolean showedElseQDescription;
	
	private ArrayList<Integer> questionIndizes;
	
	
    public void init(){
        lang = new AnimalScript("Optimale Klammerung von Matrix-Kettenmultiplikation", 
        		"Igor Braun, Vladimir Bolgov", 800, 600);
        lang.setStepMode(true);
		lang.setInteractionType(
				 Language.INTERACTION_TYPE_AVINTERACTION);
    }
    
    public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	showedIfQDescription = false;
    	showedElseQDescription = false;
    	
    	int[] mxDimensions = (int[])primitives.get("mxDimensions");
    	int mxDimensionsCount = mxDimensions.length - 1;
    	
    	mIntMatrix = new int[mxDimensionsCount][mxDimensionsCount];
    	sIntMatrix = new int[mxDimensionsCount][mxDimensionsCount];
    	
    	int i, j;
        
        v = lang.newVariables();
        
        
        lesendeZugriffeArrayP = 0;
		v.declare("int", "lesendeZugriffeArrayP", ""+lesendeZugriffeArrayP, VariableRoles.FOLLOWER.name());
		lesendeZugriffeMatrixM = 0;
		v.declare("int", "lesendeZugriffeMatrixM", ""+lesendeZugriffeMatrixM, VariableRoles.FOLLOWER.name());
		lesendeZugriffeMatrixS = 0;
		v.declare("int", "lesendeZugriffeMatrixS", ""+lesendeZugriffeMatrixS, VariableRoles.FOLLOWER.name());
		schreibendeZugriffeMatrixM = 0;
		v.declare("int", "schreibendeZugriffeMatrixM", ""+schreibendeZugriffeMatrixM, VariableRoles.FOLLOWER.name());
		schreibendeZugriffeMatrixS = 0;
		v.declare("int", "schreibendeZugriffeMatrixS", ""+schreibendeZugriffeMatrixS, VariableRoles.FOLLOWER.name());
		
        // ## HEADER ##---
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(20, 30), getAlgorithmName(),
				"header", null, headerProps);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.getHSBColor(38.09f, 37.0f, 82.75f));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, rectProps);
		header.show();
		hRect.show();
		// ---###
		
		// ## TEXT PROPERTIES ##---
		h2TextProperties = new TextProperties();
		h2TextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 24));
		
		pTextProperties = (TextProperties) props.getPropertiesByName("descrTextProps");
		pTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		
		pGreyTextProperties = new TextProperties();
		pGreyTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		pGreyTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
				
		pBoldTextProperties = new TextProperties();
		pBoldTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
		
		
		// ---###
		
		lang.nextStep("Initialisierung");
		
		// ## Matrix m ##---
		MatrixProperties mMatrixProps = (MatrixProperties) props.getPropertiesByName("mMatrixProps");
		mMatrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		mMatrixProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		
		String stringMatrixM[][] = new String[mxDimensionsCount][mxDimensionsCount];
		
		for(i=0; i<mxDimensionsCount; i++){
			for(j=0; j<mxDimensionsCount; j++){
				stringMatrixM[i][j] = "INF";
				mIntMatrix[i][j] = Integer.MAX_VALUE;
			}
		}
		
		m = lang.newStringMatrix(new Offset(0, 80, "hRect",
				AnimalScript.DIRECTION_SW), stringMatrixM, "mMatrix", null, mMatrixProps);
		
		Text mCaption = lang.newText(new Offset(0, -70, "mMatrix",
				AnimalScript.DIRECTION_NW), "Matrix m", "mMatrixText",
				null, h2TextProperties);
		
		for(i=0; i<mxDimensionsCount; i++){
			for(j=0; j<mxDimensionsCount; j++){
				if(i>j){
					m.setGridHighlightFillColor(i, j, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					m.setGridHighlightBorderColor(i, j, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					m.highlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				}
				
			}
		}
		
		Text mMatrixDescription = lang.newText(new Offset(100, 50, "mMatrix", AnimalScript.DIRECTION_SW), 
				"In der Matrix m steht an den bereits abgearbeiteten Indizes die "
				+ "minimale Anzahl an elementaren Multiplikationen"
				,"mMatrixDescription1", null, pTextProperties);
		Text mMatrixDescription2 = lang.newText(new Offset(100, 70, "mMatrix", AnimalScript.DIRECTION_SW), 
				"für den Teil der Matrizenkette, die diesen Indizes entspricht. "
				+ "Beispielsweise ist für die Matrixsequenz A_0 * A_1 * A_2 * A_3 * A_4 * A_5 "
						,"mMatrixDescription2", null, pTextProperties);
		Text mMatrixDescription3 = lang.newText(new Offset(100, 90, "mMatrix", AnimalScript.DIRECTION_SW),
				"an der Stelle m[1][3] die minimale Anzahl der elementaren Multiplikationen angegeben,"
						,"mMatrixDescription3", null, pTextProperties);
		Text mMatrixDescription4 = lang.newText(new Offset(100, 110, "mMatrix", AnimalScript.DIRECTION_SW),
				"die erforderlich ist, um das Ergebnis des Produkts A_1 * A_2 * A_3 zu berechnen."
				,"mMatrixDescription4", null, pTextProperties);
		
		lang.nextStep();
		mMatrixDescription.hide();
		mMatrixDescription2.hide();
		mMatrixDescription3.hide();
		mMatrixDescription4.hide();
		// ---###
		
		// ## p Array ##---
		
		ArrayProperties pArrayProps = (ArrayProperties) props.getPropertiesByName("pArrayProps");
		p = lang.newIntArray(new Offset(150, 0, "mMatrix",
				AnimalScript.DIRECTION_NE), mxDimensions, "pArray", null, pArrayProps);
		
		Text pCaption = lang.newText(new Offset(0, -70, "pArray",
				AnimalScript.DIRECTION_NW), "p Array", "pArrayText",
				null, h2TextProperties);

		
		ArrayMarkerProperties pArrayMarker1Prop = new ArrayMarkerProperties();
		pArrayMarker1Prop.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);

		ArrayMarker pArrayMarker1 = lang.newArrayMarker(p, 0, "pArrayMarker1", null, pArrayMarker1Prop);
		ArrayMarker pArrayMarker2 = lang.newArrayMarker(p, 0, "pArrayMarker2", null, pArrayMarker1Prop);
		ArrayMarker pArrayMarker3 = lang.newArrayMarker(p, 0, "pArrayMarker3", null, pArrayMarker1Prop);
		
		pArrayMarker1.hide();
		pArrayMarker2.hide();
		pArrayMarker3.hide();
		

		
		Text pArrayDescription = lang.newText(new Offset(100, 50, "mMatrix", AnimalScript.DIRECTION_SW), 
				"Das Array p enthält die Dimensionen der Matrizen in der Eingabesequenz. So ist "
				+ "die Dimension der Matrix A_0 ", 
				"pArrayDescription", null, pTextProperties);
		Text pArrayDescription2 = lang.newText(new Offset(100, 70, "mMatrix", AnimalScript.DIRECTION_SW), 
				"in der Beispielsequenz A_0 * A_1 * A_2 * A_3 * A_4 * A_5 durch p[0] x p[1] und der Matrix A_1 durch p[1] x p[2] angegeben. ", 
				"pArrayDescription", null, pTextProperties);
		
		lang.nextStep();
		pArrayDescription.hide();
		pArrayDescription2.hide();
		// ---###
				
		// ## Matrix s ##---
		MatrixProperties sMatrixProps = (MatrixProperties) props.getPropertiesByName("sMatrixProps");
		sMatrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		sMatrixProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		
		String stringMatrixS[][] = new String[mxDimensionsCount][mxDimensionsCount];
		
		for(i=0; i<mxDimensionsCount; i++){
			for(j=0; j<mxDimensionsCount; j++){
				stringMatrixS[i][j] = " - ";
				sIntMatrix[i][j] = Integer.MAX_VALUE;
			}
		}
		
		s = lang.newStringMatrix(new Offset(150, 0, "pArray",
				AnimalScript.DIRECTION_NE), stringMatrixS, "sMatrix", null, sMatrixProps);
		
		lang.newText(new Offset(0, -70, "sMatrix",
				AnimalScript.DIRECTION_NW), "Matrix s", "sMatrixText",
				null, h2TextProperties);
		
		Text sMatrixDescription = lang.newText(new Offset(100, 50, "mMatrix", AnimalScript.DIRECTION_SW), 
				"In der Matrix s wird der für eine Teilsequenz optimale Index  für die Zerlegung in zwei weitere Teilsequenzen", 
				"sMatrixDescription1", null, pTextProperties);
		Text sMatrixDescription2 = lang.newText(new Offset(100, 70, "mMatrix", AnimalScript.DIRECTION_SW), 
				"gespeichert. Beispielsweise ist für die Matrixsequenz A_0 * A_1 * A_2 * A_3 * A_4 * A_5 an der Stelle s[1][3] ", 
				"sMatrixDescription2", null, pTextProperties);
		Text sMatrixDescription3 = lang.newText(new Offset(100, 90, "mMatrix", AnimalScript.DIRECTION_SW), 
				"der Index gespeichert, wie die Teilsequenz A_1 * A_2 * A_3 weiter zerlegt wird. Die zwei mögliche", 
				"sMatrixDescription3", null, pTextProperties);
		Text sMatrixDescription4 = lang.newText(new Offset(100, 110, "mMatrix", AnimalScript.DIRECTION_SW), 
				"Zerlegungen sind (A_1 * A_2) * A_3 und A_1 * (A_2 * A_3). Das ist erforderlich, um doppelte Berechnung zu vermeiden.", 
				"sMatrixDescription4", null, pTextProperties);
		
		for(i=0; i<mxDimensionsCount; i++){
			for(j=0; j<mxDimensionsCount; j++){
				if(i>=j){
					s.setGridHighlightFillColor(i, j, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					s.setGridHighlightBorderColor(i, j, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					s.highlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				}
				
			}
		}
		
		lang.nextStep();
		sMatrixDescription.hide();
		sMatrixDescription2.hide();
		sMatrixDescription3.hide();
		sMatrixDescription4.hide();
		// ---###
		
		// ## SOURCE CODE ##---
		
		SourceCodeProperties srp = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
		srp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		
		sourceCode = lang.newSourceCode(new Offset(10, 190, "mMatrix",
				AnimalScript.DIRECTION_SW), "pseudoCode", null, srp);
		sourceCode.addMultilineCode(getCodeExampleShorted(), "Code", Timing.INSTANTEOUS);
		// ---###
		
		for(i=0; i<mxDimensionsCount; i++){
			mIntMatrix[i][i] = 0;
			m.put(i, i, "0", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			m.highlightCell(i, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}
		
		Text initDescr = lang.newText(new Offset(100, 50, "mMatrix", AnimalScript.DIRECTION_SW), 
				"Auf der Hauptdiagonale befinden sich Einträge m[i][i]. Diese repräsentieren den Operationsaufwand "
				+ "für einzelne Matrizen A_i,",
				"initDescr", null, pTextProperties);
		Text initDescr2 = lang.newText(new Offset(100, 70, "mMatrix", AnimalScript.DIRECTION_SW), 
				"welche vom Anfang an bekannt sind. Also sind keine Operationen nötig, um sie zu berechnen.",
				"initDescr2", null, pTextProperties);
		
		sourceCode.highlight(0);
		
		questionIndizes = new ArrayList<Integer>();
		
		for(int a=0; a<3; a++){
			questionIndizes.add(randInt(3,mxDimensionsCount*mxDimensionsCount-mxDimensionsCount));
		}
		
		lang.nextStep();
		
		
		
		// ## Algorithm ##
		
		initDescr.hide();
		initDescr2.hide();
		
		for(i=0; i<mxDimensionsCount; i++) {
			m.unhighlightCell(i, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}
		
		
		Text kVariable = lang.newText(new Offset(100, 90, "mMatrix", AnimalScript.DIRECTION_SW), 
				"k = ", "qVariable", null, pTextProperties);
		Text m1Variable = lang.newText(new Offset(200, 90, "mMatrix", AnimalScript.DIRECTION_SW), 
				"m[i][j] = ", "m1Variable", null, pTextProperties);
		Text mx1Variable = lang.newText(new Offset(400, 90, "mMatrix", AnimalScript.DIRECTION_SW), 
				"p[i] = ", "mx1Variable", null, pTextProperties);
		
		Text iVariable = lang.newText(new Offset(100, 110, "mMatrix", AnimalScript.DIRECTION_SW), 
				"i = ", "iVariable", null, pTextProperties);
		Text m2Variable = lang.newText(new Offset(200, 110, "mMatrix", AnimalScript.DIRECTION_SW), 
				"m[k+1][j] = ", "m2Variable", null, pTextProperties);
		Text mx2Variable = lang.newText(new Offset(400, 110, "mMatrix", AnimalScript.DIRECTION_SW), 
				"p[k+1] = ", "mx2Variable", null, pTextProperties);
		
		Text jVariable = lang.newText(new Offset(100, 130, "mMatrix", AnimalScript.DIRECTION_SW), 
				"j = ", "jVariable", null, pTextProperties);
		Text mx3Variable = lang.newText(new Offset(400, 130, "mMatrix", AnimalScript.DIRECTION_SW), 
				"p[j+1] = ", "mx3Variable", null, pTextProperties);
		Text m3Variable = lang.newText(new Offset(200, 130, "mMatrix", AnimalScript.DIRECTION_SW), 
				"m[i][k] = ", "m3Variable", null, pTextProperties);
		
		Text qVariable = lang.newText(new Offset(100, 160, "mMatrix", AnimalScript.DIRECTION_SW), 
				"minOp = ", "qVariable", null, pTextProperties);
		
		
		qVariable.hide();
		kVariable.hide();
		iVariable.hide();
		jVariable.hide();
		m1Variable.hide();
		m2Variable.hide();
		m3Variable.hide();
		mx1Variable.hide();
		mx2Variable.hide();
		mx3Variable.hide();
		
		v.declare("int", "i", "0", VariableRoles.FOLLOWER.name());
		v.declare("int", "k", "0", VariableRoles.FOLLOWER.name());
		v.declare("int", "j", "1", VariableRoles.FOLLOWER.name());
		v.declare("int", "minOp", "0", VariableRoles.FOLLOWER.name());
		
		int localCounter = 0;
		
		String prevPrevQ = "";
		String prevQ = "";
		String currentQ = "";
		
		sourceCode.unhighlight(0);
		
		// l ist Offset für Zeilen UND Spalten
		for(int l = 2; l <= mxDimensionsCount; l++) {
			
			// ## markiere obere Nebendiagonale ##---
			sourceCode.highlight(1);
			int a = 0;
			for(a = 0; a < (mxDimensionsCount - l + 1); a++) {
				int s = a + l - 1;
				m.setGridHighlightFillColor(a, s, Color.GREEN, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				m.highlightCell(a, s, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			}
			
			lang.nextStep();
			
			pArrayMarker1.show();
			pArrayMarker2.show();
			pArrayMarker3.show();
			qVariable.show();
			kVariable.show();
			iVariable.show();
			jVariable.show();
			m1Variable.show();
			m2Variable.show();
			m3Variable.show();
			mx1Variable.show();
			mx2Variable.show();
			mx3Variable.show();
			
			sourceCode.highlight(1, 0, true);
			sourceCode.highlight(2, 0, true);
			sourceCode.highlight(3);
			sourceCode.highlight(4);
			sourceCode.highlight(5);
			
			for(a = 0; a < (mxDimensionsCount - l + 1); a++) {
				int s = a + l - 1;
				m.unhighlightCell(a, s, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			}
			// ---####
			
			
			// Iteration durch Zeilen in Mx. i - Zeile
			for(i = 0; i < (mxDimensionsCount - l + 1); i++) {
				// j ist aktuell gesuchte Spalte
				j = i + l - 1;
				for(int k = i; k < j; k++) {
					
					
					int q = mIntMatrix[i][k] + mIntMatrix[k+1][j] + mxDimensions[i]*mxDimensions[k+1]*mxDimensions[j+1];
					
					lesendeZugriffeArrayP += 3;
					lesendeZugriffeMatrixM += 2;
					v.set("lesendeZugriffeArrayP", ""+lesendeZugriffeArrayP);
					v.set("lesendeZugriffeMatrixM", ""+lesendeZugriffeMatrixM);
					
					v.set("i", ""+i);
					v.set("k", ""+k);
					v.set("minOp", ""+q);
					v.set("j", ""+j);
					
					// visualize q
					//sourceCode.highlight(7);
					pArrayMarker1.move(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					pArrayMarker2.move(k+1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					pArrayMarker3.move(j+1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					p.highlightCell(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					p.highlightCell(k+1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					p.highlightCell(j+1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					m.highlightCell(i, k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					m.highlightCell(k+1, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					prevPrevQ = prevQ;
					prevQ = currentQ;
					currentQ = mIntMatrix[i][k] + " + " + mIntMatrix[k+1][j] + " + " + mxDimensions[i] + " * " +  mxDimensions[k+1] + " * " + mxDimensions[j+1] + " = " + q;
					
					
					if(questionIndizes.contains(localCounter)){
						MultipleChoiceQuestionModel quest = new MultipleChoiceQuestionModel("question" + localCounter);
						quest.setPrompt("Wie sieht die nächste Berechnung für q aus?");
						quest.addAnswer(currentQ, 1, "Right"); 
						quest.addAnswer(prevQ, 0, "Wrong"); 
						quest.addAnswer(prevPrevQ, 0, "Wrong"); 
						
						lang.addMCQuestion(quest);
					}
					localCounter++;
					
					iVariable.setText("i = " + i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					jVariable.setText("j = " + j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					qVariable.setText("minOp = " + currentQ, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					kVariable.setText("k = " + k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					
					if(mIntMatrix[i][j] == Integer.MAX_VALUE) {
						m1Variable.setText("m[i][j] = INF", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					} else {
						m1Variable.setText("m[i][j] = " + mIntMatrix[i][j], Timing.INSTANTEOUS, Timing.INSTANTEOUS);	
					}
										
					m2Variable.setText("m[k+1][j] = " + mIntMatrix[k+1][j], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					m3Variable.setText("m[i][k] = " + mIntMatrix[i][k], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					mx1Variable.setText("p[i] = " + mxDimensions[i], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					mx2Variable.setText("p[k+1] = " + mxDimensions[k+1], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					mx3Variable.setText("p[j+1] = " + mxDimensions[j+1], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					
					if((l == 2 || l == 3) && i == 0 && k == 0) { // erste oder zweite obere Nebendiagonale
						qVariable.hide();
						kVariable.hide();
						iVariable.hide();
						jVariable.hide();
						m1Variable.hide();
						m2Variable.hide();
						m3Variable.hide();
						mx1Variable.hide();
						mx2Variable.hide();
						mx3Variable.hide();
						
						if(l == 2) {
							Text descriptionObereDiagonale1 = lang.newText(new Offset(100, 50, "mMatrix", AnimalScript.DIRECTION_SW), 
									"In jedem Schritt werden immer zwei Teilsequenzen der ursprünglichen Matrixsequenz betrachtet. ", 
									"descriptionObereDiagonale1", null, pTextProperties);
							Text descriptionObereDiagonale2 = lang.newText(new Offset(100, 70, "mMatrix", AnimalScript.DIRECTION_SW), 
									"Angenommen, das Ergebnis der Multiplikation der 1. Teilsequenz hat die Dimension " + mxDimensions[i] + 
									"x" + mxDimensions[k+1] + " und das der 2. Teilsequenz " + mxDimensions[k+1] + "x" + 
									mxDimensions[j+1]  + ".", 
									"descriptionObereDiagonale2", null, pTextProperties);
							Text descriptionObereDiagonale3 = lang.newText(new Offset(100, 90, "mMatrix", AnimalScript.DIRECTION_SW), 
									"Dann sind für das Ausmultiplizieren dieser beiden Sequenzen " + mxDimensions[i] + 
									"*" + mxDimensions[k+1] + "*" + mxDimensions[j+1] + " = " + 
									q + " atomare Operationen erforderlich.", 
									"descriptionObereDiagonale3", null, pTextProperties);
							
							lang.nextStep();
							
							descriptionObereDiagonale1.hide();
							descriptionObereDiagonale2.hide();
							descriptionObereDiagonale3.hide();
						} else {
							Text descriptionObereDiagonale1 = lang.newText(new Offset(100, 50, "mMatrix", AnimalScript.DIRECTION_SW), 
									"In der zweiten oberen Nebendiagonale haben die jeweils zwei betrachteten Matrixsequenzen nun eine Länge von 2.", 
									"descriptionObereDiagonale1", null, pTextProperties);
							Text descriptionObereDiagonale2 = lang.newText(new Offset(100, 70, "mMatrix", AnimalScript.DIRECTION_SW), 
									"Somit reicht es nicht mehr nur p[i] * p[k+1] * p[j+1] zu berechnen, stattdessen müssen noch zu diesem Ergebnis ", 
									"descriptionObereDiagonale2", null, pTextProperties);
							Text descriptionObereDiagonale3 = lang.newText(new Offset(100, 90, "mMatrix", AnimalScript.DIRECTION_SW), 
									"die Anzahl der Operation für die Berechnung der jeweils zwei Matrixsequenzen addiert werden.", 
									"descriptionObereDiagonale3", null, pTextProperties);
							Text descriptionObereDiagonale4 = lang.newText(new Offset(100, 110, "mMatrix", AnimalScript.DIRECTION_SW), 
									"minOp = m[i][k] + m[k+1][j] + p[i] * p[k+1] * p[j+1]", 
									"descriptionObereDiagonale4", null, pTextProperties);
							lang.nextStep();
							
							descriptionObereDiagonale1.hide();
							descriptionObereDiagonale2.hide();
							descriptionObereDiagonale3.hide();
							descriptionObereDiagonale4.hide();
						}
						qVariable.show();
						kVariable.show();
						iVariable.show();
						jVariable.show();
						m1Variable.show();
						m2Variable.show();
						m3Variable.show();
						mx1Variable.show();
						mx2Variable.show();
						mx3Variable.show();
						
						
					}
					
					lang.nextStep();
					
					
					
					
					lesendeZugriffeMatrixM++;
					v.set("lesendeZugriffeMatrixM", ""+lesendeZugriffeMatrixM);
					
					if(q < mIntMatrix[i][j]) {
						Text ifQDescription = null;
						if(!showedIfQDescription) {
							ifQDescription = lang.newText(new Offset(100, 50, "mMatrix", AnimalScript.DIRECTION_SW), 
									"Mit Grün werden Zellen markiert, bei denen die Bedingung minOp < m[i][j] erfüllt ist.", 
									"IfQDescription", null, pTextProperties);
						}
						
						m.setGridHighlightFillColor(i, j, Color.GREEN, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						m.highlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						m.put(i, j, ""+q, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						
						schreibendeZugriffeMatrixM++;
						schreibendeZugriffeMatrixS++;
						v.set("schreibendeZugriffeMatrixM", ""+schreibendeZugriffeMatrixM);
						v.set("schreibendeZugriffeMatrixS", ""+schreibendeZugriffeMatrixS);
						mIntMatrix[i][j] = q;
						
						s.setGridHighlightFillColor(i, j, Color.GREEN, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						s.highlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						s.put(i, j, ""+k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						sIntMatrix[i][j] = k;
						
						m1Variable.setText("m[i][j] = " + mIntMatrix[i][j], Timing.INSTANTEOUS, Timing.INSTANTEOUS);	
						
						lang.nextStep();
						
						if(!showedIfQDescription) {
							ifQDescription.hide();
							showedIfQDescription = true;
						}
						m.unhighlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						s.unhighlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						m.setGridHighlightFillColor(i, j, Color.YELLOW, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						s.setGridHighlightFillColor(i, j, Color.YELLOW, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
												
					} else {
						Text elseQDescription = null;
						if(!showedElseQDescription) {
							elseQDescription = lang.newText(new Offset(100, 50, "mMatrix", AnimalScript.DIRECTION_SW), 
									"Mit Rot werden Zellen markiert, bei denen die Bedingung minOp < m[i][j] nicht erfüllt ist.", 
									"elseQDescription", null, pTextProperties);
							
						}
						
						m.setGridHighlightFillColor(i, j, Color.PINK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						m.highlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						
						lang.nextStep();
						
						if(!showedElseQDescription) {
							elseQDescription.hide();
							showedElseQDescription = true;
						}
						m.unhighlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						m.setGridHighlightFillColor(i, j, Color.YELLOW, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					}
					
					p.unhighlightCell(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					p.unhighlightCell(k+1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					p.unhighlightCell(j+1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					m.unhighlightCell(i, k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					m.unhighlightCell(k+1, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					
				}
			}
			
			sourceCode.unhighlight(2);
			sourceCode.unhighlight(3);
			sourceCode.unhighlight(4);
			sourceCode.unhighlight(5);
			
			pArrayMarker1.hide();
			pArrayMarker2.hide();
			pArrayMarker3.hide();
			qVariable.hide();
			kVariable.hide();
			iVariable.hide();
			jVariable.hide();
			m1Variable.hide();
			m2Variable.hide();
			m3Variable.hide();
			mx1Variable.hide();
			mx2Variable.hide();
			mx3Variable.hide();
		}
				
		sourceCode.hide();
		p.hide();
		pCaption.hide();
		m.hide();
		mCaption.hide();
		
		
		String matrixNames[] = new String[mxDimensions.length - 1];
		for(i = 0; i < matrixNames.length; i++) {
			matrixNames[i] = "A" + i;
		}
		
		String klam = getMatrixSequenz(matrixNames, 0, mxDimensionsCount - 1);
		
		
		Text klammerDescription1 = lang.newText(new Offset(0, 80, "hRect", AnimalScript.DIRECTION_SW), 
				"Die Funktion wird nun mit folgenden Parametern aufgerufen:", 
				"klammerDescription1", null, pTextProperties);
		Text klammerDescription2 = lang.newText(new Offset(0, 100, "hRect", AnimalScript.DIRECTION_SW), 
				"Optimale_Klammer('" + klam + "', Matrix s, 0, " + (mxDimensionsCount - 1) + ")", 
				"klammerDescription2", null, pTextProperties);
		
		
		optimalKlammerCallParam7 = lang.newText(new Offset(10, 180, "hRect", AnimalScript.DIRECTION_SW), 
				"Laufvariablen von Optimale_Klammer", "optimalKlammerCallParam7", null, pBoldTextProperties);
		optimalKlammerCallParam1 = lang.newText(new Offset(10, 200, "hRect", AnimalScript.DIRECTION_SW), 
				"i = " + 0, "optimalKlammerCallParam1", null, pTextProperties);
				
		optimalKlammerCallParam2 = lang.newText(new Offset(10, 220, "hRect", AnimalScript.DIRECTION_SW), 
				"j = " + (mxDimensionsCount - 1), "optimalKlammerCallParam2", null, pTextProperties);
				
		optimalKlammerCallParam3 = lang.newText(new Offset(10, 240, "hRect", AnimalScript.DIRECTION_SW), 
				"s[i][j] = " + (sIntMatrix[0][mxDimensionsCount - 1]), "optimalKlammerCallParam3", null, pTextProperties);
				
		optimalKlammerCallParam4 = lang.newText(new Offset(10, 350, "hRect", AnimalScript.DIRECTION_SW), 
				"", "optimalKlammerCallParam4", null, pTextProperties);
		
		sourceCodeKlammer = lang.newSourceCode(new Offset(10, 390, "hRect",
				AnimalScript.DIRECTION_SW), "pseudoCodeKlammer", null, srp);
		
		sourceCodeKlammer.addMultilineCode(getKlammerungCodeShorted(), "Code", Timing.INSTANTEOUS);
		sourceCodeKlammer.highlight(0, 0, true);
				
		optimalKlammerCallParam6 = lang.newText(new Offset(10, 260, "hRect", AnimalScript.DIRECTION_SW), 
				"matrixSequenz = " + klam, "optimalKlammerCallParam6", null, pTextProperties);
		
	
		sourceCodeKlammer.highlight(1, 0, true);
		String s = Optimale_Klammer(klam, matrixNames, sIntMatrix, 0, mxDimensionsCount - 1);
		
		klammerDescription1.hide();
		klammerDescription2.hide();
		sourceCodeKlammer.hide();
		optimalKlammerCallParam1.hide();
		optimalKlammerCallParam2.hide();
		optimalKlammerCallParam3.hide();
		optimalKlammerCallParam4.hide();
		optimalKlammerCallParam6.hide();
		optimalKlammerCallParam7.hide();
		
		lang.newText(new Offset(10, 200, "hRect", AnimalScript.DIRECTION_SW), 
				"matrixSequenz = ", "matrixSequenzText", null, pGreyTextProperties);
		optimalKlammerCallParam6 = lang.newText(new Offset(150, 200, "hRect", AnimalScript.DIRECTION_SW), 
				s, "matrixSequenzText2", null, pTextProperties);
		
		lang.newText(new Offset(0, 80, "hRect", AnimalScript.DIRECTION_SW), 
				"Damit enthält die Variable matrixSequenz die optimale Klammerung.", 
				"klammerDescription3", null, pTextProperties);
		lang.newText(new Offset(0, 100, "hRect", AnimalScript.DIRECTION_SW), 
				"Diese Berechnung erfordert " + mIntMatrix[0][mxDimensionsCount - 1] + " elementare Multiplikationen.", 
				"klammerDescription4", null, pTextProperties);
		sourceCodeKlammer.unhighlight(0);
		sourceCodeKlammer.unhighlight(1);
		
		CircleProperties cp1 = (CircleProperties) props.getPropertiesByName("statistikCircles");
		
		
		cp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		lang.newCircle(new Offset(3, 73, "matrixSequenzText", AnimalScript.DIRECTION_SW), 5, "c1", null, cp1);
		lang.newText(new Offset(15, 60, "matrixSequenzText", AnimalScript.DIRECTION_SW), 
				"Lesende Zugriffe auf Matrix M: " + lesendeZugriffeMatrixM, 
				"lesendeZugriffeMatrixM", null, pTextProperties);
		
		lang.newCircle(new Offset(3, 93, "matrixSequenzText", AnimalScript.DIRECTION_SW), 5, "c2", null, cp1);
		lang.newText(new Offset(15, 80, "matrixSequenzText", AnimalScript.DIRECTION_SW), 
				"Lesende Zugriffe auf Matrix S: " + lesendeZugriffeMatrixS, 
				"lesendeZugriffeMatrixS", null, pTextProperties);
		
		lang.newCircle(new Offset(3, 113, "matrixSequenzText", AnimalScript.DIRECTION_SW), 5, "c3", null, cp1);
		lang.newText(new Offset(15, 100, "matrixSequenzText", AnimalScript.DIRECTION_SW), 
				"Lesende Zugriffe auf Array P: " + lesendeZugriffeArrayP, 
				"lesendeZugriffeArrayP", null, pTextProperties);
		
		lang.newCircle(new Offset(3, 133, "matrixSequenzText", AnimalScript.DIRECTION_SW), 5, "c4", null, cp1);
		lang.newText(new Offset(15, 120, "matrixSequenzText", AnimalScript.DIRECTION_SW), 
				"Schreibende Zugriffe auf Matrix M: " + schreibendeZugriffeMatrixM, 
				"schreibendeZugriffeMatrixM", null, pTextProperties);
		
		lang.newCircle(new Offset(3, 153, "matrixSequenzText", AnimalScript.DIRECTION_SW), 5, "c5", null, cp1);
		lang.newText(new Offset(15, 140, "matrixSequenzText", AnimalScript.DIRECTION_SW), 
				"Schreibende Zugriffe auf Matrix S: " + schreibendeZugriffeMatrixS, 
				"schreibendeZugriffeMatrixS", null, pTextProperties);
		
		lang.nextStep("Ende");
		        
        lang.finalizeGeneration();
        return lang.toString();
    }
    
    private String getMatrixSequenz(String a[], int i, int j) {
    	String seq = "";
    	int c = 0;
    	for(int w = i; w <= j; w++) {
			if(c != 0) {
				seq += " * A" + w;
			} else {
				seq += "A" + w;
			}
			c++;
		}
    	return seq;
    }
    
    
    private String Optimale_Klammer(String matrixSequenz, String a[], int s[][], int i, int j) {
    	
    	optimalKlammerCallParam1.setText("i = " + i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    	optimalKlammerCallParam2.setText("j = " + j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    	optimalKlammerCallParam3.setText("s[i][j] = " +((s[i][j] == Integer.MAX_VALUE) ? "INF" : s[i][j]), 
    			Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    	
    	sourceCodeKlammer.highlight(1);
    	lang.nextStep();
    	sourceCodeKlammer.unhighlight(1);
    	
    	
    	if(i < j - 1) { // nur, wenn mehr als zwei Matrizen
    		
    		String klam = "";
    		this.s.highlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    		
    		lesendeZugriffeMatrixS++;
			v.set("lesendeZugriffeMatrixS", ""+lesendeZugriffeMatrixS);
    		if(i < s[i][j]) {
    			    			
    			sourceCodeKlammer.highlight(2, 0, true);
    			sourceCodeKlammer.highlight(3);
    			
    			optimalKlammerCallParam4.setText("Setze Klammern vor A" + i + " und nach A" + s[i][j], 
    					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    			
    			lang.nextStep();
    			
    			lesendeZugriffeMatrixS++;
    			v.set("lesendeZugriffeMatrixS", ""+lesendeZugriffeMatrixS);
    			
    			klam = getMatrixSequenz(a, i, s[i][j]);
    			matrixSequenz = matrixSequenz.replace(klam, "("+klam+")");
    			optimalKlammerCallParam6.setText("matrixSequenz = " + matrixSequenz, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    			
    			lang.nextStep();
    			
    			
    			sourceCodeKlammer.unhighlight(2);
    			sourceCodeKlammer.unhighlight(3);
    			
    		}
    		
    		lesendeZugriffeMatrixS++;
			v.set("lesendeZugriffeMatrixS", ""+lesendeZugriffeMatrixS);
			
    		if(s[i][j] + 1 < j) {
    			
    			sourceCodeKlammer.highlight(4, 0, true);
    			sourceCodeKlammer.highlight(5);
    			//optimalKlammerVariables.setText("(s[i][j] + 1) = " + (s[i][j] + 1) + " <=> j = " + j, 
    			//		Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    			optimalKlammerCallParam4.setText("Setze Klammern vor A" + (s[i][j] + 1) + " und nach A" + j, 
    					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    			
    			lang.nextStep();
    			
    			klam = getMatrixSequenz(a, s[i][j] + 1, j);
    			matrixSequenz = matrixSequenz.replace(klam, "("+klam+")");
    			optimalKlammerCallParam6.setText("matrixSequenz = " + matrixSequenz, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    			
    			lang.nextStep();
    			
    			sourceCodeKlammer.unhighlight(4);
    			sourceCodeKlammer.unhighlight(5);
    			
    		}
    		
    		
    		this.s.unhighlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    		
    		lesendeZugriffeMatrixS++;
			v.set("lesendeZugriffeMatrixS", ""+lesendeZugriffeMatrixS);
			
			sourceCodeKlammer.highlight(6);
			lang.nextStep();
			sourceCodeKlammer.unhighlight(6);
			
			matrixSequenz = Optimale_Klammer(matrixSequenz, a, s, i, s[i][j]);
    		
    		lesendeZugriffeMatrixS++;
			v.set("lesendeZugriffeMatrixS", ""+lesendeZugriffeMatrixS);
			
			optimalKlammerCallParam1.setText("i = " + i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	    	optimalKlammerCallParam2.setText("j = " + j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	    	optimalKlammerCallParam3.setText("s[i][j] = " +((s[i][j] == Integer.MAX_VALUE) ? "INF" : s[i][j]), 
	    			Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	    	
			sourceCodeKlammer.highlight(7);
			lang.nextStep();
			sourceCodeKlammer.unhighlight(7);
			
			matrixSequenz = Optimale_Klammer(matrixSequenz, a, s, s[i][j] + 1, j);
    		
    		
    	} 
    	return matrixSequenz;
    }
    

    public String getName() {
        return "Optimale Klammerung von Matrix-Kettenmultiplikation";
    }

    public String getAlgorithmName() {
        return "Optimale Klammerung von Matrix-Kettenmultiplikation, gelöst durch dynamische Programmierung";
    }

    public String getAnimationAuthor() {
        return "Igor Braun, Vladimir Bolgov";
    }

    public String getDescription(){
        return "Eine Sequenz von Matrizen kann man auf verschiedenen Weisen multiplizieren, da die Matrixmultiplikation assoziativ ist, d.h."
 +"\n"
 +"((A*B)*C) = (A*(B*C)). Ist eine solche Sequenz lang, kann die Multiplikationsreihenfolge erhebliche Auswirkungen auf die Rechenzeit haben. Eine Multiplikation von Matrizen A,B wobei A eine Dimension K x L und B eine Dimesion L x M hat, erfordert K*L*M elementare Multiplikationen. Angenommen, es sind folgende Matrizen auszumultiplizieren:"
 +"\n"
 +"\n"
 +"A_1 mit Dimension 10x100"
 +"\n"
 +"A_2 mit Dimension 100x5 und"
 +"\n"
 +"A_3 mit Dimension 5x50"
 +"\n"
 +"\n"
 +"((A_1*A_2)*A_3) ergibt 10*100*5 + 10*5*50 = 5000 + 2500 = 7500 elementare Multiplikationen. "
 +"\n"
 +"(A_1*(A_2*A_3)) ergibt 100*5*50 + 10*100*50 = 25000 + 50000 = 75000 elementare Multiplikationen. "
 +"\n"
 +"\n"
 +"An diesem Beispiel ist zu erkennen, dass eine optimale Setzung der Klammern die Anzahl der erforderten Operationen stark reduzieren kann. "
 +"\n"
 +"\n"
 +"Der hier visualisierte Algorithmus ist ein exakter Optimierungsalgorithmus für dieses Problem, d.h. er liefert tatsächlich die optimale Lösung für die vollständige Klammerung einer Matrixsequenz.  ";
    }

    public String getCodeExample(){
        return "In der Matrix m ist an den bereits abgearbeiteten Indizes die minimale Anzahl an elementaren Multiplikationen für den Teil der Matrizenkette, die diesen Indizes entspricht. Beispielsweise ist für die Matrixsequenz A_0 * A_1 * A_2 * A_3 * A_4 * A_5 an der Stelle m[1][3] die minimale Anzahl der elementaren Multiplikationen angegeben, die erforderlich ist, um das Ergebnis des Produkts A_1 * A_2 * A_3 zu berechnen. "
 +"\n"
 +"In der Matrix s wird der für eine Teilsequenz optimale Index für die Zerlegung in zwei weitere Teilsequenzen gespeichert. Beispielsweise ist für die Matrixsequenz A_0 * A_1 * A_2 * A_3 * A_4 * A_5 an der Stelle s[1][3] der Index gespeichert, wie die Teilsequenz A_1 * A_2 * A_3 weiter zerlegt wird. Die zwei mögliche Zerlegungen sind (A_1 * A_2) * A_3 und A_1 * (A_2 * A_3). Das ist erforderlich, um doppelte Berechnung zu vermeiden.  "
 +"\n"
 +"Das Array p enthält die Dimensionen der Matrizen in der Eingabesequenz. So ist die Dimension der Matrix A_0 in der Beispielsequenz A_0 * A_1 * A_2 * A_3 * A_4 * A_5 durch p[0] x p[1] und der Matrix A_1 durch p[1] x p[2] angegeben.  "
 +"\n"
 +"\n"
 +"Initialisiere die Hauptdiagonale mit Nullen"
 +"\n"
 +"Für jede obere Nebendiagonale:"
 +"\n"
 +"    Für jeden Eintrag m[i][j]:"
 +"\n"
 +"        Wähle den Verbindungsindex k mit i <= k < j, sodass die Multiplikation"
 +"\n"
 +"        der Matrixsequenz (A_i * ... * A_k) * (A_k+1 * ... * A_j)"
 +"\n"
 +"        die minimale Anzahl der elementaren Operationen minOp erfordert."
 +"\n"
 +"\n"
 +"Optimale_Klammer(matrixSequenz, s[][], i, j): "
 +"\n"
 +"    if(i < j - 1):"
 +"\n"
 +"        if(i < s[i][j]):"
 +"\n"
 +"            matrixSequenz := Setze Klammer vor A_i und nach A_s[i][j]"
 +"\n"
 +"        if(s[i][j] + 1 < j):"
 +"\n"
 +"            matrixSequenz = Setze Klammer vor A_(s[i][j]+1) und nach A_j"
 +"\n"
 +"        Optimale_Klammer(matrixSequenz, s, i, s[i][j])"
 +"\n"
 +"        Optimale_Klammer(matrixSequenz, s, s[i][j] + 1, j)"
 +"\n"
 +"\n"
 +"Optimale_Klammer('A_i....An', Matrix s, 0, n - 1)";
    }

    public String getCodeExampleShorted(){
        return ""
+"Initialisiere die Hauptdiagonale mit Nullen"
+"\n"
+"Für jede obere Nebendiagonale:"
+"\n"
+"        Für jeden Eintrag m[i][j]:"
+"\n"
+"                Wähle den Verbindungsindex k mit i <= k < j, sodass die Multiplikation"
+"\n"
+"                der Matrixsequenz (A_i * ... * A_k) * (A_k+1 * ... * A_j)"
+"\n"
+"                die minimale Anzahl der elementaren Operationen minOp erfordert.";
    }
    
    public String getKlammerungCodeShorted(){
        return ""
 +"Optimale_Klammer(matrixSequenz, s[][], i, j): "
 +"\n"
 +"    if(i < j - 1):"
 +"\n"
 +"        if(i < s[i][j]):"
 +"\n"
 +"            matrixSequenz := Setze Klammer vor A_i und nach A_s[i][j]"
 +"\n"
 +"        if(s[i][j] + 1 < j):"
 +"\n"
 +"            matrixSequenz = Setze Klammer vor A_(s[i][j]+1) und nach A_j"
 +"\n"
 +"        Optimale_Klammer(matrixSequenz, s, i, s[i][j])"
 +"\n"
 +"        Optimale_Klammer(matrixSequenz, s, s[i][j] + 1, j)";
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