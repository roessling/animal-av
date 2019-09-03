/*
 * ImageGradient.java
 * Marko Rücker, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import translator.Translator;

@SuppressWarnings("unused")
public class ScharrOperator implements ValidatingGenerator {
    
	Color specialHighlightColor = new Color(3, 252, 245);
	
	private Language lang;
	private Locale location;
	Translator translator;
       
    IntMatrix srcMatrixAnim, kernelXmatrixAnim, kernelYmatrixAnim, dstXmatrixAnim, dstYmatrixAnim, finalMatrixAnim;
    SourceCode scBorderHandling, scConvolute, scApplyFilter, scFirstPage, scThirdPage, scFourthPage;
    MatrixProperties matrixProp, scPropSpecHighlight;
    SourceCodeProperties scProp, textPropAsSc;
	TextProperties textPropTitle, headerProp, calcProp;
	RectProperties rectProp;
    Text srcTitle, dstTitleX, dstTitleY, kernelTitle, header, finalMatrixTitle, op1, op2, calcLine0,calcLine1,calcLine2,calcLine3,calcLine4;
    Rect headerRect, calcRect;
    MultipleSelectionQuestionModel whatBorderHandling;
    FillInBlanksQuestionModel whatBorderValue, whatConvoluteValue,whatEuklidValue;
    MultipleChoiceQuestionModel whatIsEuklid;
    
    private ScharrOperator scharrOperatorGen;

    public ScharrOperator(Locale location) {
    	this.location = location;
    	this.translator = new Translator("resources/ScharrOperator", location);
    }
    
    public void init(){
        lang = new AnimalScript("Image Gradient - Scharr Operator", "Marko Rücker", 1280, 1024);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {  	
    	lang.setInteractionType(
    			Language.INTERACTION_TYPE_AVINTERACTION);
    	
    	int[][] srcMatrix = (int[][])primitives.get("orginalImageMatrix");
    	int[][] kernelX = (int[][])primitives.get("kernelX");
    	int[][] kernelY = (int[][])primitives.get("kernelY");
    	int[][] dstMatrix = (int[][])primitives.get("gradientImageMatrix");
        
    	// get properties from xml-file
        matrixProp = (MatrixProperties) props.getPropertiesByName("matrixProp");
        scProp = (SourceCodeProperties) props.getPropertiesByName("scProp");
        textPropTitle = (TextProperties) props.getPropertiesByName("textProp");
        
        /* Error in animal? no possibility to set size boldness etc. 
		 *
		 * --> workaround: set textProp programatically
         */
        textPropAsSc = new SourceCodeProperties(); 
        calcProp = new TextProperties();
        calcProp.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textPropTitle.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF, Font.BOLD, 15));
        textPropAsSc.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        
        headerProp = new TextProperties();
        headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font( Font.SANS_SERIF, Font.BOLD, 26));
        
        rectProp = new RectProperties();
        rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(200, 200, 200));
        rectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        
        // create questions
        whatBorderHandling = new MultipleSelectionQuestionModel("whatBorderHandling");
        whatBorderHandling.setPrompt(translator.translateMessage("Q1_WHAT_BORDER_HANDLING"));
        whatBorderHandling.addAnswer("Zero Padding", 0, translator.translateMessage("Q1_A1_ZERO_PADDING"));
        whatBorderHandling.addAnswer("Extend", 1, translator.translateMessage("Q1_A2_EXTEND"));
        whatBorderHandling.addAnswer("Wrap", 0, translator.translateMessage("Q1_A3_WRAP"));
        whatBorderHandling.addAnswer("Mirror", 1, translator.translateMessage("Q1_A4_MIRROR"));
        
        whatBorderValue = new FillInBlanksQuestionModel("whatBorderValue");
        whatBorderValue.setPrompt(translator.translateMessage("Q0_WHAT_BORDER_VALUE"));
        
        whatConvoluteValue = new FillInBlanksQuestionModel("whatConvoluteValue");
        whatConvoluteValue.setPrompt(translator.translateMessage("Q2_WHAT_CONVOLUTE_VALUE"));
        
        whatIsEuklid = new MultipleChoiceQuestionModel("whatIsEuklid");
        whatIsEuklid.setPrompt(translator.translateMessage("Q3_WHAT_IS_EUKLID"));
        whatIsEuklid.addAnswer("√[(G_x)²+(G_y)²] = G_xy", 1, translator.translateMessage("Q3_A1_CORRECT"));
        whatIsEuklid.addAnswer("|(G_x)+(G_y)|+|(G_x)-(G_y)| = G_xy", 0, translator.translateMessage("Q3_A2_FALSE"));
        
        whatEuklidValue = new FillInBlanksQuestionModel("whatEuklidValue");
        whatEuklidValue.setPrompt(translator.translateMessage("Q4_WHAT_EUKLID_VALUE"));
        
        // create header to show on all pages
        header = lang.newText(new Coordinates(20,20), "Image Gradient (Scharr Operator) - Step 1", "header",	null, headerProp);
        headerRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "headerRect", null, rectProp);
        header.setText("      Image Gradient (Scharr Operator)", null, null);
        
        // Zero Page
        createScFirstPage();
        lang.nextStep(translator.translateMessage("LABEL_BORDER_HANDLING"));
        scFirstPage.hide();
        
        // First Page
        header.setText("Image Gradient (Scharr Operator) - Step 1", null, null);
		srcMatrixAnim = lang.newIntMatrix(new Coordinates(680, 40), srcMatrix, "srcMatrix", null, matrixProp);
		srcTitle = lang.newText(new Coordinates(680, 20), "Source Matrix", "srcTitle", null, textPropTitle);

		this.borderHandling();
		
		lang.addMSQuestion(whatBorderHandling);
		
        lang.nextStep(translator.translateMessage("LABEL_SCHARR_VERTICAL"));
        
        // Second Page (THE ALGORITHM)
        header.setText("Image Gradient (Scharr Operator) - Step 2", null, null);
		kernelXmatrixAnim = lang.newIntMatrix(new Coordinates(940, 40), kernelX, "kernel_X_Matrix", null, matrixProp);
		kernelYmatrixAnim = lang.newIntMatrix(new Coordinates(940, 40), kernelY, "kernel_Y_Matrix", null, matrixProp);
		kernelYmatrixAnim.hide();
		dstXmatrixAnim = lang.newIntMatrix(new Coordinates(680, 280), dstMatrix, "dst_X_Matrix", null, matrixProp);
		dstYmatrixAnim = lang.newIntMatrix(new Coordinates(680, 280), dstMatrix, "dst_Y_Matrix", null, matrixProp);
		dstYmatrixAnim.hide();
		dstTitleX = lang.newText(new Coordinates(680, 260), "Destination Matrix: G_x", "dstXTitle", null, textPropTitle);
		dstTitleY = lang.newText(new Coordinates(680, 260), "Destination Matrix: G_Y", "dstYTitle", null, textPropTitle);
		dstTitleY.hide();
		kernelTitle = lang.newText(new Coordinates(940, 20), "Kernel Matrix: K_x", "kernelTitle", null, textPropTitle);
        highlightAll(dstXmatrixAnim);
        highlightAll(kernelXmatrixAnim);
        
        createCalcLines();
        calcRect = lang.newRect(new Offset(-10, -10, "calcLine0", AnimalScript.DIRECTION_NW), new Offset(10, 10, "calcLine4", AnimalScript.DIRECTION_SE), "calcRect", null, rectProp);
        
		createScConvolute();
		createScApplyFilter();
		scConvolute.highlight(1);
		lang.nextStep();
		scConvolute.unhighlight(1);
		resetHighlighting(dstXmatrixAnim);
		resetHighlighting(kernelXmatrixAnim);
		
		this.convolute(srcMatrixAnim, kernelXmatrixAnim, dstXmatrixAnim, false);
        
        lang.nextStep(translator.translateMessage("LABEL_SCHARR_HORIZONTAL"));
        scConvolute.hide();
        scApplyFilter.hide();
        
        // Third Page
        header.setText("Image Gradient (Scharr Operator) - Step 3", null, null);
        createScThirdPage();

        kernelTitle.setText("Kernel Matrix: K_y", null, null);
        kernelXmatrixAnim.hide();
        kernelYmatrixAnim.show();
        highlightAll(kernelYmatrixAnim);
                
        dstTitleX.moveTo(AnimalScript.DIRECTION_SW, null, new Coordinates(20, 380), null, new MsTiming(600));
        dstXmatrixAnim.moveTo(AnimalScript.DIRECTION_SW, null, new Coordinates(20, 400), null, new MsTiming(600));
                
        dstTitleY.show(new MsTiming(300));
        dstYmatrixAnim.show(new MsTiming(300));
        
        
        lang.nextStep();
        resetHighlighting(kernelYmatrixAnim);
        convolute(srcMatrixAnim, kernelYmatrixAnim, dstYmatrixAnim, true);
        highlightAll(dstYmatrixAnim);
        
        lang.nextStep(translator.translateMessage("LABEL_CONCLUSION"));
        resetHighlighting(dstYmatrixAnim);
        scThirdPage.hide();
        kernelYmatrixAnim.hide();
        kernelTitle.hide();
        srcMatrixAnim.hide();
        srcTitle.hide();
        
        // Fourth Page
        header.setText("Image Gradient (Scharr Operator) - Step 4", null, null);
        
        calcLine0.hide();
        calcLine1.hide();
        calcLine2.hide();
        calcLine3.hide();
        calcLine4.hide();
        calcRect.hide();
        
        dstYmatrixAnim.moveTo(AnimalScript.DIRECTION_SW, null, new Coordinates(300, 400), null, new MsTiming(600));
        dstTitleY.moveTo(AnimalScript.DIRECTION_SW, null, new Coordinates(300, 380), null, new MsTiming(600));
        
        
        lang.nextStep(300);
        
        finalMatrixAnim = lang.newIntMatrix(new Coordinates(580, 400), dstMatrix, "finalMatrix", null, matrixProp);
        finalMatrixTitle = lang.newText(new Coordinates(580, 380), "Gradient: G_xy", "finalMatrixTitle", null, textPropTitle);
        
        op2 = lang.newText(new Coordinates(510, 450), "⇨", "operators", null, headerProp);
        op1 = lang.newText(new Coordinates(230, 450), "*", "operators", null, headerProp);
        
        createScFourthPage();
        
        lang.nextStep();
        
        whatEuklidValue.addAnswer(Integer.toString(euklidHelper(dstXmatrixAnim.getElement(0, 0), dstYmatrixAnim.getElement(0, 0))), 1, translator.translateMessage("Q4_A1_CORRECT"));
        lang.addFIBQuestion(whatEuklidValue);
        
        lang.nextStep();
        
        euklid(dstXmatrixAnim, dstYmatrixAnim, finalMatrixAnim);
        highlightAll(finalMatrixAnim);
        
        scFourthPage.addCodeLine("", null, 0, null);
        scFourthPage.addCodeLine("", null, 0, null);
        scFourthPage.addCodeLine(translator.translateMessage("CONCLUSION_0"),null, 0, null);
        scFourthPage.addCodeLine(translator.translateMessage("CONCLUSION_1"),null, 0, null);
        scFourthPage.addCodeLine(translator.translateMessage("CONCLUSION_2"), null, 0, null);
        scFourthPage.addCodeLine(translator.translateMessage("CONCLUSION_3"), null, 0, null);
        scFourthPage.addCodeLine("(https://en.wikipedia.org/wiki/Canny_edge_detector)", null, 0, null);
        
        lang.finalizeGeneration();
        
        return lang.toString();
    }

    public String getName() {
        return "Image Gradient (Scharr-Operator) ["+location.toString()+"]";
    }

    public String getAlgorithmName() {
        return "Image Gradient (Scharr-Operator)";
    }

    public String getAnimationAuthor() {
        return "Marko Rücker";
    }

    public String getDescription(){
    	return translator.translateMessage("DESCRIPTION");
    }

    public String getCodeExample(){
		return "  public static int[][] convolute(int[][] src, int[][] kernel) {\n"
			 +"    int[][] dstMatrix = new int[src.length][src[0].length];\n"
			 +"    src = borderHandling(src);\n"
			 +"    \n"
			 +"    for(int i=1; i<src.length-1; i++ )\n"
			 +"      for(int j=1; j<src[0].length-1; j++)\n"
			 +"        dstMatrix[i-1][j-1] = applyFilter(src, kernel, i-1, j-1);\n"
			 +"    \n"
			 +"    return dstMatrix;\n"
			 +"  }\n"
			 +"  \n"
			 +"  public int applyFilter(int[][] src, int[][] k, int x, int y) {\n"
			 +"    int value = 0;\n"
			 +"    \n"
			 +"    for(int i=0; i<k.length; i++ )\n"
			 +"      for(int j=0; j<k[0].length; j++)\n"
			 +"        value += src[x+i][y+j]*k[i][j];\n"
			 +"    \n"
			 +"    return value;\n"
			 +"  }\n"
			 +"  \n"
			 +"  // extend srcMatrix (copy last element for borders)\n"
			 +"  public int[][] borderHandling(int[][] src) {\n"
			 +"    int[][] tSrcMatr = src;\n"
			 +"    int[][] res = new int[src.length+2][src[0].length+2];\n"
			 +"    \n"
			 +"    //inner matrix\n"
			 +"    for(int i=1; i<src.length-1; i++ ) {\n"
			 +"      for(int j=1; j<src[0].length-1; j++) {\n"
			 +"        res[i][j] = tSrcMatr[i-1][j-1];\n"
			 +"      }\n"
			 +"    }\n"
			 +"    \n"
			 +"    // upper border\n"
			 +"      srcMatrix[0][0] = tSrcMatr[0][0];\n"
			 +"      src[0][src.length-1] = tSrcMatr[0][tSrcMatr[0].length-1];\n"
			 +"      for(int i=1; i<src[0].length-1;i++)\n"
			 +"        res[0][i] = tSrcMatr[0][i-1];\n"
			 +"        \n"
			 +"    // right border\n"
			 +"      for(int i=1; i<src.length-1;i++)\n"
			 +"        res[i][src.length-1] = tSrcMatr[i-1][tSrcMatr.length-1];\n"
			 +"    \n"
			 +"    // bottom border\n"
			 +"      res[src.length-1][0] = tSrcMatr[tSrcMatr.length-1][0];\n"
			 +"      res[src.length-1][src.length-1] = tSrcMatr[tSrcMatr.length-1][tSrcMatr[0].length-1];\n"
			 +"      for(int i=1; i<src[0].length-1;i++)\n"
			 +"        res[src.length-1][i] = tSrcMatr[tSrcMatr.length-1][i-1];\n"
			 +"    \n"
			 +"    // left border\n"
			 +"      for(int i=1; i<src.length-1;i++)\n"
			 +"        res[i][0] = tSrcMatr[i-1][0];\n"
			 +"    \n"
			 +"    return res;\n"
			 +"  }\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return location;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
	public void convolute(IntMatrix src, IntMatrix kernel, IntMatrix dst, boolean autoRun) {				
		for(int i=1; i<src.getNrCols()-1; i++ ) {
			for(int j=1; j<src.getNrRows()-1; j++) {
				
				//highlight srcMatrix cells around focus
				highlightAround(src, i, j);

				dst.highlightCell(i-1, j-1, null, null);
				scConvolute.highlight(5);
				
				if(autoRun) lang.nextStep(50); else lang.nextStep();
				
				dst.put(i-1, j-1, applyFilter(src, kernel, j-1, i-1), null, null);
				
				if(i-1 == 0 && j-1 == 3) {
					whatConvoluteValue.addAnswer(Integer.toString(dst.getElement(i-1,j-1)), 1, translator.translateMessage("Q2_A1_CORRECT"));
					lang.addFIBQuestion(whatConvoluteValue);
				}
				
				if(autoRun) lang.nextStep(50); else lang.nextStep();
				resetHighlighting(scApplyFilter);
				resetHighlighting(src); // optim: only unhighlight 3x3
				dst.unhighlightCell(i-1, j-1, null, null);
			}
		}
	}
		
	
	public int applyFilter(IntMatrix src, IntMatrix k, int y, int x) {
		int value = 0;
		
		scApplyFilter.highlight(3);
		scApplyFilter.highlight(4);
		scApplyFilter.highlight(5);
		scApplyFilter.highlight(6);
		scApplyFilter.highlight(7);
		scApplyFilter.highlight(8);
		for(int i=0; i<k.getNrCols(); i++ )
			for(int j=0; j<k.getNrRows(); j++)
				value += src.getElement(x+i, y+j)*k.getElement(i, j);
		
		if(value > 255) value = 255;
		if(value < 0) value = 0;
		
		setCalc(src,k, x, y, value);
		return value;
	}
	
	public void euklid(IntMatrix src1, IntMatrix src2, IntMatrix dst) {
		int res;
		int v1, v2;
		for(int i=0; i<src1.getNrCols(); i++ ) {
			for(int j=0; j<src1.getNrRows(); j++) {
				v1 = src1.getElement(i, j);
				v2 = src2.getElement(i, j);
				res = euklidHelper(v1, v2);
				dst.put(i, j, res, null, null);
			}
		}
	}
	
	private int euklidHelper(int v1, int v2) {
		// round, because legal range is 0..255 in rgb
		int value = (int)Math.round(Math.sqrt((double)(v1*v1)+(v2*v2)));
		
		if(value > 255) value = 255;
		if(value < 0) value = 0;
		
		return value;
	}
	
	/*
	// BorderHandling: extend with zeroes
	public void borderHandling() { 
		int[][] tSrcMatr = srcMatrix;
		
		srcMatrix = new int[srcMatrix.length+2][srcMatrix[0].length+2];
		srcMatrixAnim.hide();
		srcMatrixAnim = lang.newIntMatrix(new Coordinates(600, 40), srcMatrix, "srcMatrix", null);
		
		for(int i=0; i<srcMatrix.length; i++ )
			for(int j=0; j<srcMatrix[0].length; j++)
				srcMatrixAnim.highlightCell(i, j, null, null);
		
		
		//set inner matrix
		for(int i=1; i<srcMatrix.length-1; i++ ) {
			for(int j=1; j<srcMatrix[0].length-1; j++) {
				srcMatrixAnim.put(i, j, tSrcMatr[i-1][j-1], null, null);
				srcMatrixAnim.unhighlightCell(i, j, null, null);
			}
		}
		
		lang.nextStep();
		
	}
	*/
	
	
	public void borderHandling() {
		// extend srcMatrix (copy last element for borders)
		
		this.createScBorderHandling();
		lang.nextStep();
		
		scBorderHandling.highlight(1);
		scBorderHandling.highlight(2);
		int[][] tSrcMatr = getValuesFromAnimMatrix(srcMatrixAnim);
		int[][] srcMatrix = new int[tSrcMatr.length+2][tSrcMatr[0].length+2];
		srcMatrixAnim.hide();
		srcMatrixAnim = lang.newIntMatrix(new Coordinates(680, 40), srcMatrix, "srcMatrix", null, matrixProp);
		lang.nextStep();
		scBorderHandling.unhighlight(1);
		scBorderHandling.unhighlight(2);
		
		//inner matrix
		scBorderHandling.highlight(4);
		scBorderHandling.highlight(5);
		scBorderHandling.highlight(6);
		scBorderHandling.highlight(7);
		for(int i=1; i<srcMatrixAnim.getNrRows()-1; i++ ) {
			for(int j=1; j<srcMatrixAnim.getNrCols()-1; j++) {
				srcMatrixAnim.put(i, j, tSrcMatr[i-1][j-1], null, null);
				srcMatrixAnim.highlightCell(i, j, null, null);
			}
		}
		lang.nextStep();
		resetHighlighting(scBorderHandling);
		resetHighlighting(srcMatrixAnim);
		
		// upper border
		scBorderHandling.highlight(9);
		scBorderHandling.highlight(10);
		scBorderHandling.highlight(11);
		scBorderHandling.highlight(12);
		scBorderHandling.highlight(13);
			srcMatrixAnim.put(0, 0, tSrcMatr[0][0], null, null);
			srcMatrixAnim.highlightCell(0, 0, null, null);
			srcMatrixAnim.put(0, srcMatrixAnim.getNrCols()-1, tSrcMatr[0][tSrcMatr[0].length-1], null, null);
			srcMatrixAnim.highlightCell(0, srcMatrixAnim.getNrCols()-1, null, null);
			for(int i=1; i<srcMatrixAnim.getNrCols()-1;i++) {
				srcMatrixAnim.put(0, i, tSrcMatr[0][i-1], null, null);
				srcMatrixAnim.highlightCell(0, i, null, null);
			}
			lang.nextStep();
			resetHighlighting(scBorderHandling);
				
		// right border
			scBorderHandling.highlight(15);
			scBorderHandling.highlight(16);
			scBorderHandling.highlight(17);
			for(int i=1; i<srcMatrixAnim.getNrRows()-1;i++) {
				srcMatrixAnim.put(i, srcMatrixAnim.getNrCols()-1, tSrcMatr[i-1][tSrcMatr.length-1], null, null);
				srcMatrixAnim.highlightCell(i, srcMatrixAnim.getNrCols()-1, null, null);
			}
			lang.nextStep();
			resetHighlighting(scBorderHandling);

		
		// bottom border
			scBorderHandling.highlight(19);
			scBorderHandling.highlight(20);
			scBorderHandling.highlight(21);
			scBorderHandling.highlight(22);
			scBorderHandling.highlight(23);
			srcMatrix[srcMatrix.length-1][0] = tSrcMatr[tSrcMatr.length-1][0];
			srcMatrix[srcMatrix.length-1][srcMatrix.length-1] = tSrcMatr[tSrcMatr.length-1][tSrcMatr[0].length-1];
			
			whatBorderValue.addAnswer(Integer.toString(tSrcMatr[tSrcMatr.length-1][tSrcMatr[0].length-1]), 1, translator.translateMessage("Q0_A1_CORRECT"));
			lang.addFIBQuestion(whatBorderValue);
			
			srcMatrixAnim.setGridHighlightFillColor(srcMatrixAnim.getNrRows()-1, srcMatrixAnim.getNrCols()-1, specialHighlightColor, null, null);
			srcMatrixAnim.highlightCell(srcMatrixAnim.getNrRows()-1, srcMatrixAnim.getNrCols()-1, null, null);
			
			lang.nextStep();
			
			srcMatrixAnim.put(srcMatrixAnim.getNrRows()-1, srcMatrixAnim.getNrCols()-1,tSrcMatr[tSrcMatr.length-1][tSrcMatr[0].length-1], null, null);
			
			srcMatrixAnim.put(srcMatrixAnim.getNrRows()-1, 0, tSrcMatr[tSrcMatr.length-1][0], null, null);
			srcMatrixAnim.highlightCell(srcMatrixAnim.getNrRows()-1, 0, null, null);
			
			for(int i=1; i<srcMatrixAnim.getNrCols()-1;i++) {
				srcMatrixAnim.put(srcMatrixAnim.getNrRows()-1, i, tSrcMatr[tSrcMatr.length-1][i-1], null, null);
				srcMatrixAnim.highlightCell(srcMatrixAnim.getNrRows()-1, i, null, null);
			}
			lang.nextStep();
			resetHighlighting(scBorderHandling);
			
			
		// left border
			scBorderHandling.highlight(25);
			scBorderHandling.highlight(26);
			scBorderHandling.highlight(27);			
			for(int i=1; i<srcMatrixAnim.getNrRows()-1;i++) {
				srcMatrixAnim.put(i, 0, tSrcMatr[i-1][0], null, null);
				srcMatrixAnim.highlightCell(i, 0, null, null);
			}
			lang.nextStep();
			resetHighlighting(scBorderHandling);
			resetHighlighting(srcMatrixAnim);
			scBorderHandling.hide();
	}
	
	
	// helper methods
	private void createCalcLines() {
		calcLine0 = lang.newText(new Coordinates(350, 320), "                                      ", "calcLine0", null, calcProp);
		calcLine1 = lang.newText(new Coordinates(350, 335), "                                      ", "calcLine1", null, calcProp);
		calcLine2 = lang.newText(new Coordinates(350, 350), "                                      ", "calcLine2", null, calcProp);
		calcLine3 = lang.newText(new Coordinates(350, 365), "--------------------------------------", "calcLine3", null, calcProp);
		calcLine4 = lang.newText(new Coordinates(350, 380), "     ==>                              ", "calcLine4", null, calcProp);
	}

	private void createScFirstPage() {
		scFirstPage = lang.newSourceCode(new Coordinates(20, 70), "firstPageText", null, textPropAsSc);
		addTranslatetCodeLines(scFirstPage);
	}
	
	private void createScThirdPage() {
		scThirdPage = lang.newSourceCode(new Coordinates(20, 70), "thirdPageText", null, textPropAsSc);
		addTranslatetCodeLines(scThirdPage);
		
	}
	
	private void createScFourthPage() {
		scFourthPage = lang.newSourceCode(new Coordinates(20, 70), "fourthPageText", null, textPropAsSc);
		addTranslatetCodeLines(scFourthPage);
		
		lang.addMCQuestion(whatIsEuklid);
		lang.nextStep();
		scFourthPage.addCodeLine("√[(G_x)²+(G_y)²] = G_xy", null, 0, null);
		
	}
	
	private void createScApplyFilter() {
		scApplyFilter = lang.newSourceCode(new Coordinates(20, 250), "applyFilter", null, scProp);
		scApplyFilter.addCodeLine("  public int applyFilter(int[][] src, int[][] k, int x, int y) {", null, 0, null);
		scApplyFilter.addCodeLine("    int value = 0;", null, 0, null);
		scApplyFilter.addCodeLine("    ", null, 0, null);
		scApplyFilter.addCodeLine("    for(int i=0; i<k.length; i++ )", null, 0, null);
		scApplyFilter.addCodeLine("      for(int j=0; j<k[0].length; j++)", null, 0, null);
		scApplyFilter.addCodeLine("        value += src[x+i][y+j]*k[i][j];", null, 0, null);
		scApplyFilter.addCodeLine("    ", null, 0, null);
		scApplyFilter.addCodeLine("    if(value > 255) value = 255;", null, 0, null);
		scApplyFilter.addCodeLine("    if(value < 0) value = 0;", null, 0, null);
		scApplyFilter.addCodeLine("    return value;", null, 0, null);
		scApplyFilter.addCodeLine("  }", null, 0, null);

	}
	private void createScConvolute() {
		scConvolute = lang.newSourceCode(new Coordinates(20, 70), "convolute", null, scProp);
		scConvolute.addCodeLine("  public static int[][] convolute(int[][] src, int[][] kernel) {", null, 0, null);
		scConvolute.addCodeLine("    int[][] dstMatrix = new int[src.length][src[0].length];", null, 0, null);
		scConvolute.addCodeLine("    ", null, 0, null);
		scConvolute.addCodeLine("    for(int i=1; i<src.length-1; i++ )", null, 0, null);
		scConvolute.addCodeLine("      for(int j=1; j<src[0].length-1; j++)", null, 0, null);
		scConvolute.addCodeLine("        dstMatrix[i-1][j-1] = applyFilter(src, kernel, i-1, j-1);", null, 0, null);
		scConvolute.addCodeLine("    ", null, 0, null);
		scConvolute.addCodeLine("    return dstMatrix;", null, 0, null);
		scConvolute.addCodeLine("  }", null, 0, null);
	}
	
	private void createScBorderHandling() {
		scBorderHandling = lang.newSourceCode(new Coordinates(20, 70), "borderHandling", null, scProp);
		scBorderHandling.addCodeLine("  public int[][] borderHandling(int[][] src) {", null, 0, null);
		scBorderHandling.addCodeLine("    int[][] tSrcMatr = src;", null, 0, null);
		scBorderHandling.addCodeLine("    int[][] res = new int[src.length+2][src[0].length+2];", null, 0, null);
		scBorderHandling.addCodeLine("    ", null, 0, null);
		scBorderHandling.addCodeLine("    //inner matrix ", null, 0, null);
		scBorderHandling.addCodeLine("    for(int i=1; i<src.length-1; i++ )", null, 0, null);
		scBorderHandling.addCodeLine("      for(int j=1; j<src[0].length-1; j++)", null, 0, null);
		scBorderHandling.addCodeLine("        res[i][j] = tSrcMatr[i-1][j-1]; ", null, 0, null);
		scBorderHandling.addCodeLine("     ", null, 0, null);
		scBorderHandling.addCodeLine("    // upper border ", null, 0, null);
		scBorderHandling.addCodeLine("      srcMatrix[0][0] = tSrcMatr[0][0]; ", null, 0, null);
		scBorderHandling.addCodeLine("      src[0][src.length-1] = tSrcMatr[0][tSrcMatr[0].length-1]; ", null, 0, null);
		scBorderHandling.addCodeLine("      for(int i=1; i<src[0].length-1;i++) ", null, 0, null);
		scBorderHandling.addCodeLine("        res[0][i] = tSrcMatr[0][i-1]; ", null, 0, null);
		scBorderHandling.addCodeLine("         ", null, 0, null);
		scBorderHandling.addCodeLine("    // right border ", null, 0, null);
		scBorderHandling.addCodeLine("      for(int i=1; i<src.length-1;i++) ", null, 0, null);
		scBorderHandling.addCodeLine("        res[i][src.length-1] = tSrcMatr[i-1][tSrcMatr.length-1]; ", null, 0, null);
		scBorderHandling.addCodeLine("     ", null, 0, null);
		scBorderHandling.addCodeLine("    // bottom border ", null, 0, null);
		scBorderHandling.addCodeLine("      res[src.length-1][0] = tSrcMatr[tSrcMatr.length-1][0]; ", null, 0, null);
		scBorderHandling.addCodeLine("      res[src.length-1][src.length-1] = tSrcMatr[tSrcMatr.length-1][tSrcMatr[0].length-1]; ", null, 0, null);
		scBorderHandling.addCodeLine("      for(int i=1; i<src[0].length-1;i++) ", null, 0, null);
		scBorderHandling.addCodeLine("        res[src.length-1][i] = tSrcMatr[tSrcMatr.length-1][i-1]; ", null, 0, null);
		scBorderHandling.addCodeLine("         ", null, 0, null);
		scBorderHandling.addCodeLine("    // left border ", null, 0, null);
		scBorderHandling.addCodeLine("      for(int i=1; i<src.length-1;i++) ", null, 0, null);
		scBorderHandling.addCodeLine("        res[i][0] = tSrcMatr[i-1][0]; ", null, 0, null);
		scBorderHandling.addCodeLine("     ", null, 0, null);
		scBorderHandling.addCodeLine("    return res; ", null, 0, null);
		scBorderHandling.addCodeLine("  } ", null, 0, null);
	}
	
	private void setCalc(IntMatrix src, IntMatrix kernel, int x, int y, int value) {
		/*
		 * 1 2 3
		 * 4 5 6
		 * 7 8 9
		 */
		
		int s1, s2, s3, s4, s5, s6, s7, s8, s9, k1, k2, k3, k4, k5, k6, k7, k8, k9;
		s1 = src.getElement(y,x);
		s2 = src.getElement(y,x+1);
		s3 = src.getElement(y,x+2);
		s4 = src.getElement(y+1,x);
		s5 = src.getElement(y+1,x+1);
		s6 = src.getElement(y+1,x+2);
		s7 = src.getElement(y+2,x);
		s8 = src.getElement(y+2,x+1);
		s9 = src.getElement(y+2,x+2);
		
		k1 = kernel.getElement(0, 0);
		k2 = kernel.getElement(0, 1);
		k3 = kernel.getElement(0, 2);
		k4 = kernel.getElement(1, 0);
		k5 = kernel.getElement(1, 1);
		k6 = kernel.getElement(1, 2);
		k7 = kernel.getElement(2, 0);
		k8 = kernel.getElement(2, 1);
		k9 = kernel.getElement(2, 2);
		
		calcLine0.setText(k1+"*"+gfS(s1)+" + "+k2+"*"+gfS(s2)+" + "+k3+"*"+gfS(s3)+" = "+gfS((k1*s1)+(k2*s2)+(k3*s3)), null, null);
		calcLine1.setText(k4+"*"+gfS(s4)+" + "+k5+"*"+gfS(s5)+" + "+k6+"*"+gfS(s6)+" = "+gfS((k4*s4)+(k5*s5)+(k6*s6)), null, null);
		calcLine2.setText(k7+"*"+gfS(s7)+" + "+k8+"*"+gfS(s8)+" + "+k9+"*"+gfS(s9)+" = "+gfS((k7*s7)+(k8*s8)+(k9*s9)), null, null);
		//scCalculation.addCodeLine("------------------------------", null, 0, null);
		calcLine4.setText("     ==>  "+gfS(value)+"                 ", null, null);
	}
	
	// get formatted String
	private String gfS(int value) {
		if(value < 10) return " "+value+" ";
		if(value < 100) return " "+value;
		else return ""+value;
	}
	
	private void resetHighlighting(SourceCode sc) {
		for(int i=0; i<sc.length(); i++) {
			sc.unhighlight(i);
		}
	}
	
	private void highlightAll(IntMatrix matrix) {
		for(int i=0; i<matrix.getNrRows(); i++ )
			for(int j=0; j<matrix.getNrCols(); j++)
				matrix.highlightCell(i, j, null, null);
	}
	
	private void resetHighlighting(IntMatrix matrix) {
		for(int i=0; i<matrix.getNrRows(); i++ ) {
			for(int j=0; j<matrix.getNrCols(); j++) {
				matrix.setGridHighlightFillColor(i, j, (Color) matrix.getProperties().get("cellHighlight"), null, null);
				matrix.unhighlightCell(i, j, null, null);
			}
		}
	}
	
	private void resetMatrix(IntMatrix matrix) {
		for(int i=0; i<matrix.getNrRows(); i++ )
			for(int j=0; j<matrix.getNrCols(); j++)
				matrix.put(i, j, 0, null, null);
	}
	
	
	
	private void highlightAround(IntMatrix matrix, int x, int y) {
		for(int i=-1; i<2; i++ )
			for(int j=-1; j<2; j++)
				if(i==0 && j==0) {
					// special highlight for middle cell
					matrix.setGridHighlightFillColor(x+i, y+j, specialHighlightColor, null, null);
					matrix.highlightCell(x+i, y+j, null, null);
				} else {
					// normal highlight for others
					matrix.highlightCell(x+i, y+j, null, null);
				}
	}
	
	private int[][] getValuesFromAnimMatrix(IntMatrix matrix) {
		int[][] valueMatrix = new int[matrix.getNrRows()][matrix.getNrCols()];
		for(int i=0; i<matrix.getNrRows(); i++ )
			for(int j=0; j<matrix.getNrCols(); j++)
				valueMatrix[i][j] = matrix.getElement(i, j);
		
		return valueMatrix;
	}
	
	private void addTranslatetCodeLines(SourceCode sourceCodePrim) {
		String scUpperCase = sourceCodePrim.getName().toUpperCase();
		for(int i=0; i<Integer.valueOf(translator.translateMessage(scUpperCase+"_LENGTH")); i++) {
			sourceCodePrim.addCodeLine(translator.translateMessage(scUpperCase+"_"+i), null, 0, null);
		}
		
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		
		int[][] orginalImageMatrix = (int[][]) arg1.get("orginalImageMatrix");
		
		for (int[] is : orginalImageMatrix) {
			for (int i : is) {
				if(i<0 || i > 255) throw new IllegalArgumentException("Image Values have to be Byte! [0..255]");
			}
		}
		
		return true;
	}

}