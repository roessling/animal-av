/*
 * JPEGCompression.java
 * Aleksej Strassheim, Kosntantin Strassheim, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.compression;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;
import animal.main.Animal;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.MatrixPrimitive;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;

public class JPEGCompression implements ValidatingGenerator {
	private Locale locale;
	private Translator translator;
    private Language lang;
    private Text title;
    private Polyline titleLine;
    private int[][] dctMatrix;
    private int[][] quantMatrix;
    private int[][] subsamplingMatrixR;
    private int[][] subsamplingMatrixG;
    private int[][] subsamplingMatrixB;
    private int[][] yMatrix;
    private int[][] cbMatrix;
    private int[][] crMatrix;
    private int[][] dctm;
    private int[][] quantizedMatrix;
    private int[][] zigzagcut;
    private TextProperties textTitle;
    private TextProperties textDescription;
    private TextProperties textSubtitle;
    private TextProperties textHeaderDescription;
    private Font userFontTitle;
    private Font userFontSubtitle;
    private Font userFontHeaderDescription;
    private MatrixProperties matrix;
    private IntMatrix afterDCT;
    private IntMatrix beforeDCT;
    private IntMatrix afterQuant;

    public JPEGCompression(String path, Locale locale){
    	this.locale = locale;
    	translator = new Translator(path, getContentLocale());
    }
    public void init(){
        lang = new AnimalScript("JPEG Compression", "Aleksej Strassheim, Kosntantin Strassheim", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	lang.setStepMode(true);
        dctMatrix = (int[][])primitives.get("dctMatrix");
        quantMatrix = (int[][])primitives.get("quantMatrix");
        subsamplingMatrixR = (int[][])primitives.get("subsamplingMatrixR");
        subsamplingMatrixG = (int[][])primitives.get("subsamplingMatrixG");
        subsamplingMatrixB = (int[][])primitives.get("subsamplingMatrixB");
        textTitle = (TextProperties)props.getPropertiesByName("textTitle");
        textDescription = (TextProperties)props.getPropertiesByName("textDescription");
        textSubtitle = (TextProperties)props.getPropertiesByName("textSubtitle");
        textHeaderDescription = (TextProperties)props.getPropertiesByName("textHeaderDescription");
        userFontTitle = (Font) textTitle.get("font");
        userFontSubtitle = (Font) textSubtitle.get("font");
        userFontHeaderDescription = (Font) textHeaderDescription.get("font");
        matrix = (MatrixProperties)props.getPropertiesByName("matrix");
        
        textTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userFontTitle.getFamily(), Font.BOLD, 32));
        textSubtitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userFontSubtitle.getFamily(), Font.BOLD, 24));
        textHeaderDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userFontHeaderDescription.getFamily(), Font.BOLD, 16));
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        QuestionGroupModel qs = new QuestionGroupModel("section_question", 3);
		lang.addQuestionGroup(qs);
		
        createTitle();
        createIntro();
        colorConverion();
        colorConversionQuestion();
        subsampling();
        subsamplingQuestion();
        dctransform();
        dcttransformgQuestion();
        quantization();
        quantizationQuestion();
        encode();
        summary();
        
        lang.finalizeGeneration();
        return lang.toString();
    }
	private void quantizationQuestion() {
		MultipleChoiceQuestionModel quest = new MultipleChoiceQuestionModel("Quantisation Question");
		quest.setGroupID("section_question");
		quest.setNumberOfTries(2);
		quest.setPrompt(translator.translateMessage("question_quant"));
		quest.addAnswer(translator.translateMessage("question_quant1"), 1, translator.translateMessage("question_right"));
		quest.addAnswer(translator.translateMessage("question_quant2"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_quant1"));
		quest.addAnswer(translator.translateMessage("question_quant3"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_quant1"));
		quest.addAnswer(translator.translateMessage("question_quant4"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_quant1"));
		lang.addMCQuestion(quest);
		
	}
	private void dcttransformgQuestion() {
		MultipleChoiceQuestionModel quest = new MultipleChoiceQuestionModel("DCT Question");
		quest.setGroupID("section_question");
		quest.setNumberOfTries(2);
		quest.setPrompt(translator.translateMessage("question_dct"));
		quest.addAnswer(translator.translateMessage("question_dct1"), 1, translator.translateMessage("question_right"));
		quest.addAnswer(translator.translateMessage("question_dct2"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_dct1"));
		quest.addAnswer(translator.translateMessage("question_dct3"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_dct1"));
		quest.addAnswer(translator.translateMessage("question_dct4"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_dct1"));
		lang.addMCQuestion(quest);
		
	}
	private void subsamplingQuestion() {
		MultipleChoiceQuestionModel quest = new MultipleChoiceQuestionModel("Subsampling Question");
		quest.setGroupID("section_question");
		quest.setNumberOfTries(2);
		quest.setPrompt(translator.translateMessage("question_sub"));
		quest.addAnswer(translator.translateMessage("question_sub1"), 1, translator.translateMessage("question_right"));
		quest.addAnswer(translator.translateMessage("question_sub2"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_sub1"));
		quest.addAnswer(translator.translateMessage("question_sub3"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_sub1"));
		quest.addAnswer(translator.translateMessage("question_sub4"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_sub1"));
		lang.addMCQuestion(quest);
		
	}
	private void colorConversionQuestion() {
		MultipleChoiceQuestionModel quest = new MultipleChoiceQuestionModel("Color Conversion Question");
		quest.setGroupID("section_question");
		quest.setNumberOfTries(2);
		quest.setPrompt(translator.translateMessage("question_color"));
		quest.addAnswer(translator.translateMessage("question_color1"), 1, translator.translateMessage("question_right"));
		quest.addAnswer(translator.translateMessage("question_color2"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_color1"));
		quest.addAnswer(translator.translateMessage("question_color3"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_color1"));
		quest.addAnswer(translator.translateMessage("question_color4"), 0, translator.translateMessage("question_wrong") + " " + translator.translateMessage("question_color1"));
		lang.addMCQuestion(quest);
	}
	private void createTitle() {
		//Erstellen des Titels
    	title = lang.newText(new Coordinates(300, 50), translator.translateMessage("title"), "title",null, textTitle);
    	Node[] nodes = {new Offset(-10, 0, "title", AnimalScript.DIRECTION_SW), new Offset(10, 0, "title", AnimalScript.DIRECTION_SE)};
		titleLine = lang.newPolyline(nodes, "title_line", null);
    }
    private void createIntro() {
    	//Erstellen des Intro Abschnittes
    	//Text
    	String lastOffset = "title";
    	for(int i = 1; i<= 10; i++) {
    		lang.newText(new Offset(i == 1 ? -100 : 0, i == 1 ? 100 : 10, lastOffset, AnimalScript.DIRECTION_SW), translator.translateMessage("intro"+i), "intro"+i, null, textDescription);
    		lastOffset = "intro"+i;
    	}
    	//Verstecke vor naechsten Step
    	lang.nextStep("Intro");
    	List<Primitive> l = new ArrayList<Primitive>();
    	l.add(title);
    	l.add(titleLine);
    	lang.hideAllPrimitivesExcept(l);
    }
    private void colorConverion() {
    	//Erstellen des Farbsubsampling abschnittes
    	//Text
    	lang.newText(new Offset(-50, 100, "title", AnimalScript.DIRECTION_SW), translator.translateMessage("conversion_title"), "conversion_title", null, textSubtitle);
    	String lastOffset = "conversion_title";
    	for(int i = 1; i <= 5; i++) {
    		lang.newText(new Offset(i == 1 ? -50 : 0, i == 1 ? 30 : 10, lastOffset, AnimalScript.DIRECTION_SW), translator.translateMessage("conversion"+i), "conversion"+i, null, textDescription);
    		lastOffset = "conversion"+i;
    	}
    	
    	//Rechnung
    	//YCbCr Vektor
    	String[][] ycbcr = {{"Y'"}, {"Cb"}, {"Cr"}};
    	StringMatrix ycbcrVec = lang.newStringMatrix(new Offset(0, 50, lastOffset, AnimalScript.DIRECTION_SW), ycbcr, "YCbCr", null);
		drawBracket(ycbcrVec);
		//Gleicheitszeichen
		lang.newText(new Offset(40, 0, "YCbCr", AnimalScript.DIRECTION_W), "=", "YEquals", null);
		//Offsetmatrix
		int[][] offset = {{0},{128},{128}};
		IntMatrix offsetMatrix = lang.newIntMatrix(new Offset(40, 0, "YCbCr", AnimalScript.DIRECTION_NE), offset, "offset", null, matrix);
		drawBracket(offsetMatrix);
		//Plus
		lang.newText(new Offset(40, 0, "offset", AnimalScript.DIRECTION_W), "+", "YEquals", null);
		//YCbCr formel
		double[][] schema = {{0.299, 0.587, 0.114}, {-0.168736, -0.331264, 0.5}, {0.5, -0.418688, -0.081312}};
		DoubleMatrix schem = lang.newDoubleMatrix(new Offset(40, 0, "offset", AnimalScript.DIRECTION_NE), schema, "schema", null, matrix);
		drawBracket(schem);
		//Multiplikation
		lang.newText(new Offset(20, 0, "schema", AnimalScript.DIRECTION_E), "*", "YEquals", null);
		//RGB Vektor
		String[][] rgb = {{"R"}, {"G"}, {"B"}};
		StringMatrix rgbMatrix = lang.newStringMatrix(new Offset(40, 0, "schema", AnimalScript.DIRECTION_NE), rgb, "rgb", null);
		drawBracket(rgbMatrix);
		
		lang.nextStep();
		lang.newText(new Offset(0, 100, "YCbCr", AnimalScript.DIRECTION_SW), translator.translateMessage("conversion6"), "conversion6", null, textDescription);
		
		//Rechnung
		//Erstellung RGB Matrix
		String[][] a = new String[2][4];
		for(int x = 0; x < 2; x++) {
			for(int y = 0; y < 4; y++) {
				a[x][y] =  "(" + subsamplingMatrixR[x][y] + ", " + subsamplingMatrixG[x][y] + ", " + subsamplingMatrixB[x][y] + ")";
			}
		}
		lang.newText(new Offset(0, 50, "conversion6", AnimalScript.DIRECTION_SW), translator.translateMessage("conversion_rgb_matrix_title"), "conversion_rgb_matrix", null, textDescription);
		StringMatrix rgb2Matrix = lang.newStringMatrix(new Offset(0, 100, "conversion_rgb_matrix", AnimalScript.DIRECTION_SW), a, "rbgStringMatrix", null, matrix);
		drawBracket(rgb2Matrix);
		lang.nextStep();
		
		//Aufteilung in RGB Farben
		lang.newText(new Offset(300, 0, "conversion_rgb_matrix", AnimalScript.DIRECTION_NE), translator.translateMessage("conversion_seperate"), "conversion_seperate", null, textDescription);
		createImpArrow(new Offset(20, -5, "rbgStringMatrix", AnimalScript.DIRECTION_E));
		IntMatrix rMatrix = lang.newIntMatrix(new Offset(150, -70, "rbgStringMatrix", AnimalScript.DIRECTION_NE), subsamplingMatrixR, "rMatrix", null, matrix);
		drawBracket(rMatrix);
		lang.newText(new Offset(-30, 0, "rMatrix", AnimalScript.DIRECTION_W), "R =", "rEq", null, textDescription);
		IntMatrix gMatrix = lang.newIntMatrix(new Offset(0, 30, "rMatrix", AnimalScript.DIRECTION_SW), subsamplingMatrixG, "gMatrix", null, matrix);
		drawBracket(gMatrix);
		lang.newText(new Offset(-30, 0, "gMatrix", AnimalScript.DIRECTION_W), "G =", "gEq", null, textDescription);
		IntMatrix bMatrix = lang.newIntMatrix(new Offset(0, 30, "gMatrix", AnimalScript.DIRECTION_SW), subsamplingMatrixB, "bMatrix", null, matrix);
		drawBracket(bMatrix);
		lang.newText(new Offset(-30, 0, "bMatrix", AnimalScript.DIRECTION_W), "B =", "bEq", null, textDescription);
		lang.nextStep();
		
		//Umwandlung
		lang.newText(new Offset(100, 0, "conversion_seperate", AnimalScript.DIRECTION_NE), translator.translateMessage("conversion_convert"), "conversion_convert", null, textDescription);
		createImpArrow(new Offset(20, 0, "gMatrix", AnimalScript.DIRECTION_E));
		//YCbCr Matrix
		yMatrix = new int[2][4];
		cbMatrix = new int[2][4];
		crMatrix = new int[2][4];
		for(int x = 0; x < 2; x++) {
			for(int y = 0; y < 4; y++) {
				yMatrix[x][y] = (int) Math.round(0.299 * subsamplingMatrixR[x][y] + 0.587 * subsamplingMatrixG[x][y] + 0.114 * subsamplingMatrixB[x][y]);
				cbMatrix[x][y] = 128 + (int) Math.round((-0.168736) * subsamplingMatrixR[x][y] + (-0.331264) * subsamplingMatrixG[x][y] + 0.5 * subsamplingMatrixB[x][y]);
				crMatrix[x][y] = 128 + (int) Math.round(0.5 * subsamplingMatrixR[x][y] + (-0.418688) * subsamplingMatrixG[x][y] + (-0.081312) * subsamplingMatrixB[x][y]);
			}
		}
		IntMatrix yMatrixO = lang.newIntMatrix(new Offset(120, 0, "rMatrix", AnimalScript.DIRECTION_NE), yMatrix, "yMatrix", null, matrix);
		drawBracket(yMatrixO);
		lang.newText(new Offset(-30, 0, "yMatrix", AnimalScript.DIRECTION_W), "Y' =", "rEq", null, textDescription);
		
		IntMatrix cbMatrixO = lang.newIntMatrix(new Offset(0, 30, "yMatrix", AnimalScript.DIRECTION_SW), cbMatrix, "cbMatrix", null, matrix);
		drawBracket(cbMatrixO);
		lang.newText(new Offset(-30, 0, "cbMatrix", AnimalScript.DIRECTION_W), "Cb =", "gEq", null, textDescription);
		
		IntMatrix crMatrixO = lang.newIntMatrix(new Offset(0, 30, "cbMatrix", AnimalScript.DIRECTION_SW), crMatrix, "crMatrix", null, matrix);
		drawBracket(crMatrixO);
		lang.newText(new Offset(-30, 0, "crMatrix", AnimalScript.DIRECTION_W), "Cr =", "bEq", null, textDescription);
		
		lang.nextStep("Color Conversion");
		List<Primitive> l = new ArrayList<Primitive>();
    	l.add(title);
    	l.add(titleLine);
    	lang.hideAllPrimitivesExcept(l);
    	ycbcrVec.hide();
    	offsetMatrix.hide();
    	schem.hide();
    	rgbMatrix.hide();
    	rgb2Matrix.hide();
    	rMatrix.hide();
    	gMatrix.hide();
    	bMatrix.hide();
    	yMatrixO.hide();
    	cbMatrixO.hide();
    	crMatrixO.hide();
		
    }
    private void subsampling() {
    	lang.newText(new Offset(-50, 100, "title", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling_title"), "subsampling_title", null, textSubtitle);
    	String lastOffset = "subsampling_title";
    	for(int i = 1; i <= 6; i++) {
    		lang.newText(new Offset(i == 1 ? -50 : 0, i == 1 ? 30 : 10, lastOffset, AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling"+i), "subsampling"+i, null, textDescription);
    		lastOffset = "subsampling"+i;
    	}
    	
    	lang.newText(new Offset(0, 60, "subsampling6", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling444_title"), "444_title", null, textSubtitle);
    	List<Primitive> l1 = create444(new Offset(0, 50, "444_title", AnimalScript.DIRECTION_SW));
    	lang.newText(new Offset(0, 40, "444Y[0][1]", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling444_desc1"), "subsampling444_desc1", null, textDescription);
    	lang.newText(new Offset(0, 10, "subsampling444_desc1", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling444_desc2"), "subsampling444_desc2", null, textDescription);
    	
    	lang.nextStep();
    	
    	lang.newText(new Offset(0, 60, "subsampling444_desc2", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling422_title"), "422_title", null, textSubtitle);
    	List<Primitive> l2 = create422(new Offset(0, 50, "422_title", AnimalScript.DIRECTION_SW));
    	lang.newText(new Offset(0, 40, "422Y[0][1]", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling422_desc1"), "subsampling422_desc1", null, textDescription);
    	lang.newText(new Offset(0, 10, "subsampling422_desc1", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling422_desc2"), "subsampling422_desc2", null, textDescription);
    	lang.newText(new Offset(0, 10, "subsampling422_desc2", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling422_desc3"), "subsampling422_desc3", null, textDescription);
    	
    	lang.nextStep();
    	
    	lang.newText(new Offset(0, 60, "subsampling422_desc3", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling420_title"), "420_title", null, textSubtitle);
    	List<Primitive> l3 = create420(new Offset(0, 50, "420_title", AnimalScript.DIRECTION_SW));
    	lang.newText(new Offset(0, 40, "420Y[0][1]", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling420_desc1"), "subsampling420_desc1", null, textDescription);
    	lang.newText(new Offset(0, 10, "subsampling420_desc1", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling420_desc2"), "subsampling420_desc2", null, textDescription);
    	lang.newText(new Offset(0, 10, "subsampling420_desc2", AnimalScript.DIRECTION_SW), translator.translateMessage("subsampling420_desc3"), "subsampling420_desc3", null, textDescription);
    	
    	lang.nextStep("Sub Sampling Intro");
    	//Beispiel
    	lang.newText(new Offset(600, 0, "444_title", AnimalScript.DIRECTION_NE), translator.translateMessage("subsampling_example"), "subsampling_example", null, textHeaderDescription);
    	IntMatrix yMtx = lang.newIntMatrix(new Offset(0, 20, "subsampling_example", AnimalScript.DIRECTION_SW), yMatrix, "yMatrix1", null, matrix);
    	drawBracket(yMtx);
    	IntMatrix cbMtx = lang.newIntMatrix(new Offset(20, 0, "yMatrix1", AnimalScript.DIRECTION_NE), cbMatrix, "cbMatrix1", null, matrix);
    	drawBracket(cbMtx);
    	IntMatrix crMtx = lang.newIntMatrix(new Offset(20, 0, "cbMatrix1", AnimalScript.DIRECTION_NE), crMatrix, "crMatrix1", null, matrix);
    	drawBracket(crMtx);
    	lang.nextStep();
    	createImpArrowDown(new Offset(0, 5, "yMatrix1", AnimalScript.DIRECTION_S));
    	createImpArrowDown(new Offset(0, 5, "cbMatrix1", AnimalScript.DIRECTION_S));
    	createImpArrowDown(new Offset(0, 5, "crMatrix1", AnimalScript.DIRECTION_S));
    	
    	int[][] cbsampled = new int[1][2];
    	int[][] crsampled = new int[1][2];
    	for(int i = 0; i<2; i++) {
    		cbsampled[0][i] = (cbMatrix[0][i*2] + cbMatrix[1][i*2] + cbMatrix[0][i*2 + 1] + cbMatrix[1][i*2 + 1])/4;
    		crsampled[0][i] = (crMatrix[0][i*2] + crMatrix[1][i*2] + crMatrix[0][i*2 + 1] + crMatrix[1][i*2 + 1])/4;
    	}
    	
    	
    	IntMatrix yMtxSmpld = lang.newIntMatrix(new Offset(0, 70, "yMatrix1", AnimalScript.DIRECTION_SW), yMatrix, "yMatrixSmpld", null, matrix);
    	IntMatrix cbMtxSmpld = lang.newIntMatrix(new Offset(0, 70, "cbMatrix1", AnimalScript.DIRECTION_SW), cbsampled, "cbMatrixSmpld", null, matrix);
    	IntMatrix crMtxSmpld = lang.newIntMatrix(new Offset(00, 70, "crMatrix1", AnimalScript.DIRECTION_SW), crsampled, "crMatrixSmpld", null, matrix);
    	drawBracket(yMtxSmpld);
    	drawBracket(crMtxSmpld);
    	drawBracket(cbMtxSmpld);
    
        l1.addAll(l2);
        l1.addAll(l3);
        lang.nextStep("Sub Sampling Example");
    	for(Primitive p : l1) {
    		p.hide();
    	}
		List<Primitive> l = new ArrayList<Primitive>();
    	l.add(title);
    	l.add(titleLine);
    	lang.hideAllPrimitivesExcept(l);
    	yMtxSmpld.hide();
    	crMtxSmpld.hide();
    	cbMtxSmpld.hide();
    	yMtx.hide();
    	crMtx.hide();
    	cbMtx.hide();
    	
	}
    private void dctransform() {
    	lang.newText(new Offset(-50, 100, "title", AnimalScript.DIRECTION_SW), translator.translateMessage("dct_title"), "dct_title", null, textSubtitle);
    	String lastOffset = "dct_title";
    	for(int i = 1; i <= 7; i++) {
    		lang.newText(new Offset(i == 1 ? -50 : 0, i == 1 ? 30 : 10, lastOffset, AnimalScript.DIRECTION_SW), translator.translateMessage("dct"+i), "dct"+i, null, textDescription);
    		lastOffset = "dct"+i;
    	}
    	lang.nextStep("DCT Intro");
    	
    	beforeDCT = lang.newIntMatrix(new Offset(0, 100, "dct7", AnimalScript.DIRECTION_SW),dctMatrix, "beforeDCT", null, matrix);
    	drawBracket(beforeDCT);
    	createImpArrow(new Offset(5, 0, "beforeDCT", AnimalScript.DIRECTION_E));
    	dctm =  dct(dctMatrix);
    	afterDCT = lang.newIntMatrix(new Offset(70, 0, "beforeDCT", AnimalScript.DIRECTION_NE),dctm, "afterDCT", null);
    	drawBracket(afterDCT);
    	
    	lang.nextStep("DCT");
		List<Primitive> l = new ArrayList<Primitive>();
    	l.add(title);
    	l.add(titleLine);
    	lang.hideAllPrimitivesExcept(l);
    	//Draw brackets again for quant section
    	drawBracket(beforeDCT);
    	drawBracket(afterDCT);
    	createImpArrow(new Offset(5, 0, "beforeDCT", AnimalScript.DIRECTION_E));
	}
    private void quantization() {
    	lang.newText(new Offset(-50, 100, "title", AnimalScript.DIRECTION_SW), translator.translateMessage("quant_title"), "quant_title", null, textSubtitle);
    	String lastOffset = "quant_title";
    	for(int i = 1; i <= 4; i++) {
    		lang.newText(new Offset(i == 1 ? -50 : 0, i == 1 ? 30 : 10, lastOffset, AnimalScript.DIRECTION_SW), translator.translateMessage("quant"+i), "quant"+i, null, textDescription);
    		lastOffset = "quant"+i;
    	}
    	lang.nextStep();
    	
    	//Quantization table
    	lang.newText(new Offset(200, 0, "quant_title", AnimalScript.DIRECTION_NE), translator.translateMessage("quant_matrix_title"), "quant_matrix_title", null, textHeaderDescription);
    	IntMatrix qMatrix = lang.newIntMatrix(new Offset(0, 20, "quant_matrix_title", AnimalScript.DIRECTION_SW), quantMatrix, "quantMatrix", null, matrix);
    	drawBracket(qMatrix);
    	lang.nextStep();
    	
    	createImpArrow(new Offset(5, 0, "afterDCT", AnimalScript.DIRECTION_E));
    	quantizedMatrix = new int[8][8];
    	for(int i = 0; i < 8; i++) {
    		for(int j = 0; j<8; j++) {
    			quantizedMatrix[i][j] = dctm[i][j] / quantMatrix[i][j];
    		}
    	}
    	afterQuant = lang.newIntMatrix(new Offset(70, 0, "afterDCT", AnimalScript.DIRECTION_NE), quantizedMatrix, "quantizied", null, matrix);
    	drawBracket(afterQuant);
    	
    	lang.nextStep("Quantization");
		List<Primitive> l = new ArrayList<Primitive>();
    	l.add(title);
    	l.add(titleLine);
    	lang.hideAllPrimitivesExcept(l);
    	beforeDCT.hide();
    	afterDCT.hide();
    	afterQuant.hide();
    }
    private void encode() {
    	
    	lang.newText(new Offset(-50, 100, "title", AnimalScript.DIRECTION_SW), translator.translateMessage("encode_title"), "encode_title", null, textSubtitle);
    	String lastOffset = "encode_title";
    	for(int i = 1; i <= 3; i++) {
    		lang.newText(new Offset(i == 1 ? -50 : 0, i == 1 ? 30 : 10, lastOffset, AnimalScript.DIRECTION_SW), translator.translateMessage("encode"+i), "encode"+i, null, textDescription);
    		lastOffset = "encode"+i;
    	}
    	lang.nextStep();
    	
    	IntMatrix m = lang.newIntMatrix(new Offset(0, 30, "encode3", AnimalScript.DIRECTION_SW), quantizedMatrix, "encodeMatrix", null, matrix);
    	drawBracket(m);
    	
    	int[] zigzag = new int[8*8];
    	
    	int x = 0;
    	int y = 0;
    	int currentIteration = 0;
    	//Dir 1 -> oben rechts | -1 -> unten links
    	int direction = 1;
    	boolean changeDirFlag;
    	while(x < 7 || y < 7) {
    		 changeDirFlag = false;

    		 
    		//Wenn es oben an die Decke stoesst
    		if(x < 0) {
    			x = 0;
    			changeDirFlag = true;
    		}
    		//Wenn gegen rechte wand
    		if(y > 7) {
    			y = 7;
    			x = x+2;
    			changeDirFlag = true;
    		}
    		//Wenn gegen boden
    		if(x > 7) {
    			x = 7;
    			y = y+2;
    			changeDirFlag = true;
    		}
    		//Linke Wand
    		if(y < 0) {
    			y = 0;
    			changeDirFlag = true;
    		}
    		
    		//Wenn Kollision stattgefunden hat
    		if(changeDirFlag) {
    			direction = direction*-1;
    		}
    		//Speichere ergebnis
    		zigzag[currentIteration] = quantizedMatrix[x][y];
    		if(x == 7 && y == 7) break;
    		currentIteration++;
    		//Fahre fort
    		x -= direction;
    		y += direction;
    		
    	}
    	
    	//cut zero
    	int index = zigzag.length-1;
    	while(zigzag[index] == 0) {
    		index--;
    	}
    	int[] newZigZag = new int[index];
    	for(int i = 0; i<newZigZag.length; i++) {
    		newZigZag[i] = zigzag[i];
    	}
    	
    	createImpArrowDown(new Offset(40, 15, "encodeMatrix", AnimalScript.DIRECTION_SW));
    	int[][] zigzagzero = new int[1][zigzag.length];
    	zigzagzero[0] = zigzag;
    	zigzagcut = new int[1][newZigZag.length];
    	zigzagcut[0] = newZigZag;
    	IntMatrix zigzagMatrix = lang.newIntMatrix(new Offset(0, 80, "encodeMatrix", AnimalScript.DIRECTION_SW), zigzagzero, "zigzagzero", null, matrix);
    	createImpArrowDown(new Offset(40, 5, "zigzagzero", AnimalScript.DIRECTION_SW));
    	IntMatrix zigzagCutMatrix = lang.newIntMatrix(new Offset(0, 70, "zigzagzero", AnimalScript.DIRECTION_SW), zigzagcut, "zigzagzerocut", null, matrix);
    	
    	lang.nextStep("Entropy Coding");
		List<Primitive> l = new ArrayList<Primitive>();
    	l.add(title);
    	l.add(titleLine);
    	lang.hideAllPrimitivesExcept(l);
    	zigzagMatrix.hide();
    	zigzagCutMatrix.hide();
    	m.hide();
    	
    }
    private void summary() {
    	lang.newText(new Offset(-50, 100, "title", AnimalScript.DIRECTION_SW), translator.translateMessage("summary_title"), "summary_title", null, textSubtitle);
    	lang.newText(new Offset(0, 20, "summary_title", AnimalScript.DIRECTION_SW), translator.translateMessage("summary_ycrcb"), "summary_ycrcb", null, textDescription);
    	lang.nextStep();
    	lang.newText(new Offset(0, 20, "summary_ycrcb", AnimalScript.DIRECTION_SW), translator.translateMessage("summary_dct"), "summary_dct", null, textDescription);
    	lang.nextStep();
    	IntMatrix beforeDCT = lang.newIntMatrix(new Offset(0, 100, "summary_ycrcb", AnimalScript.DIRECTION_SW),dctMatrix, "summaryBeforeDCT", null, matrix);
    	drawBracket(beforeDCT);
    	createImpArrow(new Offset(10, 0, "summaryBeforeDCT", AnimalScript.DIRECTION_E));
    	IntMatrix zigzagCutMatrix = lang.newIntMatrix(new Offset(100, 0, "summaryBeforeDCT", AnimalScript.DIRECTION_E), zigzagcut, "summaryZigzag", null, matrix);
    	lang.newText(new Offset(0, 40, "summaryBeforeDCT", AnimalScript.DIRECTION_SW), translator.translateMessage("summary_decode"), "summary_decode", null, textDescription);
    	
    	
    }
    private List<Primitive> create444(Node node){
    	List<Primitive> rtns = new ArrayList<Primitive>();
    	rtns.addAll(createNode(node, "444Y", 25, Color.GREEN, 444));
    	rtns.addAll(createNode(new Offset(25, 0, "444Y[3][0]", AnimalScript.DIRECTION_NE), "444Cb", 25, Color.BLUE, 444));
    	rtns.addAll(createNode(new Offset(25, 0, "444Cb[3][0]", AnimalScript.DIRECTION_NE), "444Cr", 25, Color.RED, 444));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "444Y[1][0]", AnimalScript.DIRECTION_NE), "4", "444y4", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(20, -30, "444Y[3][0]", AnimalScript.DIRECTION_NE), ":", "444y:", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "444Cb[1][0]", AnimalScript.DIRECTION_NE), "4", "444Cb4", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(20, -30, "444Cb[3][0]", AnimalScript.DIRECTION_NE), ":", "444Cb:", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "444Cr[1][0]", AnimalScript.DIRECTION_NE), "4", "444Cr4", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, 10, "444Y[1][1]", AnimalScript.DIRECTION_SE), "Y'", "444yY", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(0, 10, "444Cb[1][1]", AnimalScript.DIRECTION_SE), "Cb'", "444CbCb", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(0, 10, "444Cr[1][1]", AnimalScript.DIRECTION_SE), "Cr'", "444CrCr", null, textHeaderDescription));
    	return rtns;
    }
    private List<Primitive> create422(Node node){
    	List<Primitive> rtns = new ArrayList<Primitive>();
    	rtns.addAll(createNode(node, "422Y", 25, Color.GREEN, 444));
    	rtns.addAll(createNode(new Offset(25, 0, "422Y[3][0]", AnimalScript.DIRECTION_NE), "422Cb", 25, Color.BLUE, 422));
    	rtns.addAll(createNode(new Offset(25, 0, "422Cb[3][0]", AnimalScript.DIRECTION_NE), "422Cr", 25, Color.RED, 422));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "422Y[1][0]", AnimalScript.DIRECTION_NE), "4", "422y4", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(20, -30, "422Y[3][0]", AnimalScript.DIRECTION_NE), ":", "422y:", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "422Cb[1][0]", AnimalScript.DIRECTION_NE), "2", "422Cb4", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(20, -30, "422Cb[3][0]", AnimalScript.DIRECTION_NE), ":", "422Cb:", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "422Cr[1][0]", AnimalScript.DIRECTION_NE), "2", "422Cr4", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, 10, "422Y[1][1]", AnimalScript.DIRECTION_SE), "Y'", "422yY", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(0, 10, "422Cb[1][1]", AnimalScript.DIRECTION_SE), "Cb'", "422CbCb", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(0, 10, "422Cr[1][1]", AnimalScript.DIRECTION_SE), "Cr'", "422CrCr", null, textHeaderDescription));
    	
    	return rtns;
    }
    private List<Primitive> create420(Node node){
    	List<Primitive> rtns = new ArrayList<Primitive>();
    	rtns.addAll(createNode(node, "420Y", 25, Color.GREEN, 444));
    	rtns.addAll(createNode(new Offset(25, 0, "420Y[3][0]", AnimalScript.DIRECTION_NE), "420Cb", 25, Color.BLUE, 420));
    	rtns.addAll(createNode(new Offset(25, 0, "420Cb[3][0]", AnimalScript.DIRECTION_NE), "420Cr", 25, Color.RED, 420));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "420Y[1][0]", AnimalScript.DIRECTION_NE), "4", "420y4", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(20, -30, "420Y[3][0]", AnimalScript.DIRECTION_NE), ":", "420y:", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "420Cb[1][0]", AnimalScript.DIRECTION_NE), "2:0", "420Cb4", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(20, -30, "420Cb[3][0]", AnimalScript.DIRECTION_NE), ":", "420Cb:", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, -30, "420Cr[1][0]", AnimalScript.DIRECTION_NE), "2:0", "420Cr4", null, textHeaderDescription));
    	
    	rtns.add(lang.newText(new Offset(0, 10, "420Y[1][1]", AnimalScript.DIRECTION_SE), "Y'", "420yY", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(0, 10, "420Cb[1][1]", AnimalScript.DIRECTION_SE), "Cb'", "420CbCb", null, textHeaderDescription));
    	rtns.add(lang.newText(new Offset(0, 10, "420Cr[1][1]", AnimalScript.DIRECTION_SE), "Cr'", "420CrCr", null, textHeaderDescription));
    	
    	return rtns;
    }

    private List<Primitive> createNode(Node node, String name, int size, Color fillColor, int type){
    	List<Primitive> rtns = new ArrayList<Primitive>();
    	RectProperties rectPropsY = new RectProperties();
    	rectPropsY.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectPropsY.set(AnimationPropertiesKeys.FILL_PROPERTY, fillColor);
        rectPropsY.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        
        RectProperties rectPropsEmpty = new RectProperties();
    	rectPropsEmpty.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectPropsEmpty.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectPropsEmpty.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        Node lastNode = node;
    	for(int i = 0; i < 4; i++) {
    			//Top
    			Rect rec = lang.newRect(lastNode, new OffsetFromLastPosition(size, size), name+"["+i+"]["+0+"]", null, (i % 2 == 0) || (type == 444) ? rectPropsY : rectPropsEmpty);
    			//Bottom
    			Rect rec2 = lang.newRect(new Offset(0, 0, rec.getName(), AnimalScript.DIRECTION_SW), new OffsetFromLastPosition(size, size), name+"["+i+"]["+1+"]", null, ((i % 2 == 0) || (type == 444)) && (type!=420) ? rectPropsY : rectPropsEmpty);
    			lastNode = new Offset(0, 0, rec.getName(), AnimalScript.DIRECTION_NE);
    			rtns.add(rec);
    			rtns.add(rec2);
    	}
    	return rtns;
    }
    private Polyline createImpArrow(Node node) {
		Node[] nodes = {node, new OffsetFromLastPosition(50, 0)};
		PolylineProperties plp = new PolylineProperties();
		plp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		Polyline imp = lang.newPolyline(nodes, "imp", null, plp);
		return imp;
    }
    private Polyline createImpArrowDown(Node node) {
		Node[] nodes = {node, new OffsetFromLastPosition(0, 50)};
		PolylineProperties plp = new PolylineProperties();
		plp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		Polyline imp = lang.newPolyline(nodes, "imp", null, plp);
		return imp;
    }

	public String getName() {
        return "JPEG Compression";
    }

    public String getAlgorithmName() {
        return "JPEGCompression";
    }

    public String getAnimationAuthor() {
        return "Aleksej Strassheim, Kosntantin Strassheim";
    }

    public String getDescription(){
        return translator.translateMessage("description1") + "\n" + translator.translateMessage("description2");
    }

    public String getCodeExample(){
        return "Example";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return this.locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    private void drawBracket(MatrixPrimitive m) {
		Offset [] nodes;
		nodes =new Offset[] {new Offset(0,0,m,AnimalScript.DIRECTION_NW),new Offset(0,10,m,AnimalScript.DIRECTION_SW)};
		Polyline lLeft = lang.newPolyline(nodes,"line1",null);
		nodes = new Offset[] {new Offset(-5,0,m,AnimalScript.DIRECTION_NE),new Offset(-5,10,m,AnimalScript.DIRECTION_SE)};
		Polyline lRight = lang.newPolyline(nodes,"line2",null);
		nodes = new Offset[]{new Offset(0,0,lLeft,AnimalScript.DIRECTION_NW),new Offset(3,0,lLeft,AnimalScript.DIRECTION_NW)};
		lang.newPolyline(nodes,"line1",null);
		nodes = new Offset[]{new Offset(0,0,lRight,AnimalScript.DIRECTION_NW),new Offset(-3,0,lRight,AnimalScript.DIRECTION_NW)};
		lang.newPolyline(nodes,"line1",null);
		nodes = new Offset[]{new Offset(0,0,lLeft,AnimalScript.DIRECTION_SW),new Offset(3,0,lLeft,AnimalScript.DIRECTION_SW)};
		lang.newPolyline(nodes,"line1",null);
		nodes = new Offset[]{new Offset(0,0,lRight,AnimalScript.DIRECTION_SW),new Offset(-3,0,lRight,AnimalScript.DIRECTION_SW)};
		lang.newPolyline(nodes,"line1",null);
	}
    public int[][] dct(int[][] matrix) {
    	int[][] outputMatrix = new int[8][8];
    	for(int i = 0; i < 8; i++) {
    		for(int j = 0; j<8; j++) {
    			double sum = 0;
    			for(int x = 0; x<8; x++) {
    				for(int y = 0; y<8; y++) {
    					sum = sum + matrix[x][y] * Math.cos(((2*x+1)*i*Math.PI)/16.0 ) * Math.cos(((2*y+1)*j*Math.PI)/16.0 );
    				}
    			}
    			double ci = i == 0 ? 1/Math.sqrt(2) : 1;
    			double cj = j == 0 ? 1/Math.sqrt(2) : 1;
    			long finished = Math.round(0.25 * ci * cj * sum);
    			outputMatrix[i][j] = (int) finished;
    		}
    	}
    	return outputMatrix;
    }
	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		//Check Length of Matrix
		int[][] dctMatrix = (int[][])arg1.get("dctMatrix");
		if(dctMatrix.length != 8 || dctMatrix[0].length != 8) {
			throw new IllegalArgumentException("DCT Matrix needs to be 8x8");
		}
		int[][] quantMatrix = (int[][])arg1.get("quantMatrix");
		if(quantMatrix.length != 8 || quantMatrix[0].length != 8) {
			throw new IllegalArgumentException("Quantization Matrix needs to be 8x8");
		}
		int[][] subsamplingMatrixR = (int[][])arg1.get("subsamplingMatrixR");
		if(subsamplingMatrixR.length != 2 || subsamplingMatrixR[0].length != 4) {
			throw new IllegalArgumentException("subsamplingMatrix R  needs to be 4x2");
		}
		int[][]  subsamplingMatrixG = (int[][])arg1.get("subsamplingMatrixG");
		if(subsamplingMatrixG.length != 2 || subsamplingMatrixG[0].length != 4) {
			throw new IllegalArgumentException("subsamplingMatrix G  needs to be 4x2");
		}
		int[][] subsamplingMatrixB = (int[][])arg1.get("subsamplingMatrixB");
		if(subsamplingMatrixB.length != 2 || subsamplingMatrixB[0].length != 4) {
			throw new IllegalArgumentException("subsamplingMatrix B  needs to be 4x2");
		}
		//Check for values
		for(int i = 0; i<8; i++) {
			for(int j = 0; j <8; j++) {
				if(i<2 && j<4) {
					if(subsamplingMatrixR[i][j] < 0 || subsamplingMatrixR[i][j] > 255) {
						throw new IllegalArgumentException("Element of subsamplingMatrix R needs to be between 0 and 255");
					}
					if(subsamplingMatrixG[i][j] < 0 || subsamplingMatrixG[i][j] > 255) {
						throw new IllegalArgumentException("Element of subsamplingMatrix G needs to be between 0 and 255");
					}
					if(subsamplingMatrixB[i][j] < 0 || subsamplingMatrixB[i][j] > 255) {
						throw new IllegalArgumentException("Element of subsamplingMatrix B needs to be between 0 and 255");
					}
				}
				if(dctMatrix[i][j] < 0 || dctMatrix[i][j] > 255) {
					throw new IllegalArgumentException("Elemt of DCT Matrix needs to be between 0 and 255");
				}
				if(quantMatrix[i][j] < 0 || quantMatrix[i][j] > 255) {
					throw new IllegalArgumentException("Elemt of Quantization Matrix needs to be between 0 and 255");
				}
			}
		}
		return true;
	}
	/*
    public static void main(String[] args) {
		Generator generator = new JPEGCompression("resources/JPEGCompression", Locale.GERMANY); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}
	*/
}