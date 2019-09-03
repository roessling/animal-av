/*
 * LineareRegression.java
 * Amon Ditzinger, Dirk Schumacher, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.helpersLineareRegression.Line;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;

public class LineareRegressionEN implements ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties highlightColor;
    private int upperBound;
    private int numberOfPoints;
    private PolylineProperties lineColor;
    private int lowerBound;
    private Locale locale;
    private Translator tlr;
    
    public LineareRegressionEN(Locale loc) {
    	locale = loc;
    	tlr = new Translator("resources/LineareRegression", loc);
    	init();
    }

    public void init(){
        lang = new AnimalScript(tlr.translateMessage("name"), "Amon Ditzinger, Dirk Schumacher", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        highlightColor = (SourceCodeProperties)props.getPropertiesByName("highlightColor");
        upperBound = (Integer)primitives.get("upperBound");
        numberOfPoints = (Integer)primitives.get("numberOfPoints");
        lineColor = (PolylineProperties)props.getPropertiesByName("lineColor");
        lowerBound = (Integer)primitives.get("lowerBound");
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        regression();
        lang.finalizeGeneration();
        return lang.toString();
    }
    
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
    	numberOfPoints = (Integer)primitives.get("numberOfPoints");
        lowerBound = (Integer)primitives.get("lowerBound");
        upperBound = (Integer)primitives.get("upperBound");
        boolean checkNOP = false;
        boolean checkLB = false;
        boolean checkUB = false;
        
        if(numberOfPoints >= 50 & numberOfPoints <= 750) {
        	checkNOP = true;
        }
        else throw new IllegalArgumentException(tlr.translateMessage("nopException"));
        
        if(lowerBound >= 0 & lowerBound <= 350) {
        	checkLB = true;
        }
        else throw new IllegalArgumentException(tlr.translateMessage("lowException"));
        
        if(upperBound >= 50 & upperBound <= 350) {
        	checkUB = true;
        }
        else throw new IllegalArgumentException(tlr.translateMessage("upException"));
        
        return(checkNOP & checkLB & checkUB);
    }

     @SuppressWarnings("unused")
    public void regression() {
    	int urX = 610;
    	int urY = 460;
    	
    	RectProperties titleRecPr = new RectProperties();
    	titleRecPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	titleRecPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
    	Rect titleRec = lang.newRect(new Coordinates(35,75), new Coordinates(315, 120), "titleRec", null, titleRecPr);
    	
    	TextProperties titlePr = new TextProperties();
    	titlePr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 25));
    	Text title = lang.newText(new Coordinates(40, 80), tlr.translateMessage("title"), "Titel", null, titlePr);
    	
    	
    	SourceCode intro = lang.newSourceCode(new Coordinates(40, 150), "intro", null);
    	intro.addCodeLine(tlr.translateMessage("intro0"), "line1", 0, null);
    	intro.addCodeLine(tlr.translateMessage("intro1"), "line2", 0, null);
    	intro.addCodeLine(tlr.translateMessage("intro2"), "line3", 0, null);
    	intro.addCodeLine(tlr.translateMessage("intro3"), "line4", 0, null);
    	lang.nextStep("Intro");
    			      
    	intro.hide();
    	
    	PolylineProperties plProps = new PolylineProperties();
    	plProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    	
    	Polyline yAxis = lang.newPolyline(new Node[] {new Coordinates(urX, urY), new Coordinates(urX, urY - this.upperBound - 10)}, "Y-Axis", null, plProps);
    	Polyline xAxis = lang.newPolyline(new Node[] {new Coordinates(urX, urY), new Coordinates(urX + this.numberOfPoints + 30, urY)}, "Y-Axis", null, plProps);
    	
    	Coordinates xOffset = new Coordinates(urX + this.numberOfPoints + 40, urY - 5);
    	Text xName = lang.newText(xOffset, "X", "XName", null);
    	Text yName = lang.newText(new Coordinates(urX - 5, urY - this.upperBound - 30), "Y", "YName", null);
    	
    	int step = 0;
    	while(step <= this.numberOfPoints) {
    		Text tx = lang.newText(new Coordinates(urX + step, urY + 3), Integer.toString(step), "ValueX", null);
    		step += 50;
    	}
    	
    	step = 50;
    	while(step <= this.upperBound) {
    		Text ty = lang.newText(new Coordinates(urX - 23, urY - step), Integer.toString(step), "ValueY", null);
    		step += 50;
    	}
    	
    	SourceCodeProperties scProps = this.highlightColor;
    	if(scProps == null) {								// kann raus
    		scProps = new SourceCodeProperties();
    		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    	}
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
            new Font("Monospaced", Font.PLAIN, 12));

        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	
        
    	SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "Code", null, scProps);
    	sc.addCodeLine(tlr.translateMessage("scFirst"), null, 0, null);  //0
    	sc.addCodeLine("", null, 0, null);	//1
    	sc.addCodeLine("// y = ax + b", null, 1, null);	//2
    	sc.addCodeLine("xAverage = calcAverageX(points)", null, 1, null);	//3
    	sc.addCodeLine("yAverage = calcAverageY(points)", null, 1, null);	//4
    	sc.addCodeLine("", null, 0, null);	//5
    	sc.addCodeLine("// calculate empirical covariance", null, 1, null);	//6
    	sc.addCodeLine("empiricalCovariance = 0", null, 1, null);	//7
    	sc.addCodeLine("for(point in points):", null, 1, null);	//8
    	sc.addCodeLine("empiricalCovariance += (point.x - xAverage) * (point.y - yAverage)", null, 2, null);	//9
    	sc.addCodeLine("", null, 0, null);	//10
    	sc.addCodeLine("// calculate empirical variance of x", null, 1, null);	//11
    	sc.addCodeLine("empiricalVarianceX = 0", null, 1, null);	//12
    	sc.addCodeLine("for(point in points):", null, 1, null);	//13
    	sc.addCodeLine("empiricalVarinceX += (point.x - xAverage)�", null, 2, null);	//14
    	sc.addCodeLine("", null, 0, null);	//15
    	sc.addCodeLine("// calculate a", null, 1, null);	//16
    	sc.addCodeLine("a = empiricalCovarinace / empiricalVarianceX", null, 1, null);	//17
    	sc.addCodeLine("", null, 0, null);	//18
    	sc.addCodeLine("// calculate b", null, 1, null);	//19
    	sc.addCodeLine("b = yAverage - a * xAverage", null, 1, null);	//20
    	lang.nextStep(tlr.translateMessage("label"));
    	
    	sc.highlight(0);
     	
    	generators.maths.helpersLineareRegression.Point[] cloud = new generators.maths.helpersLineareRegression.Point[this.numberOfPoints];
    	int x;
    	int y;
    	for(int i = 0; i < this.numberOfPoints; i++) {
			y = this.lowerBound + (int)(Math.random() * ((this.upperBound - this.lowerBound) + 1));
			generators.maths.helpersLineareRegression.Point p = new generators.maths.helpersLineareRegression.Point(i, y);
			Coordinates c = new Coordinates(urX + i, urY - y);
			PointProperties pp = new PointProperties();
			pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			pp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
			algoanim.primitives.Point point = lang.newPoint(c, "Punkt", null, pp);
			cloud[i] = p;
    	}
    	Line test = new Line();
    	
    	Text xAverage = lang.newText(new Offset(50, -340, xOffset, AnimalScript.DIRECTION_NE), "xAverage:", "xAverage", null);
    	Text yAverage = lang.newText(new Offset(50, -290, xOffset, AnimalScript.DIRECTION_NE), "yAverage:", "yAverage", null);
    	Text empCov = lang.newText(new Offset(50, -240, xOffset, AnimalScript.DIRECTION_NE), "empiricalCovariance:", "empCov", null);
    	Text empVarX = lang.newText(new Offset(50, -190, xOffset, AnimalScript.DIRECTION_NE), "empiricalVarianceX:", "empVarX", null);
    	Text a = lang.newText(new Offset(50, -140, xOffset, AnimalScript.DIRECTION_NE), "a:", "a", null);
    	Text b = lang.newText(new Offset(50, -90, xOffset, AnimalScript.DIRECTION_NE), "b:", "b", null);
    	Text function = lang.newText(new Offset(50, -40, xOffset, AnimalScript.DIRECTION_NE), "Function:", "function", null);
    	
    	lang.nextStep();
    	
    	DecimalFormat f = new DecimalFormat("#0.000");
     	
    	sc.unhighlight(0);
    	sc.highlight(3);
    	Text xAverageVal = lang.newText(new Offset(200, -340, xOffset, AnimalScript.DIRECTION_NE), f.format(test.calcAverageX(cloud)), "xAverageVal", null);
    	lang.nextStep();
    	
    	sc.unhighlight(3);
    	sc.highlight(4);
    	Text yAverageVal = lang.newText(new Offset(200, -290, xOffset, AnimalScript.DIRECTION_NE), f.format(test.calcAverageY(cloud)), "yAverageVal", null);
    	lang.nextStep();
    	
    	sc.unhighlight(4);
    	sc.highlight(6);
    	sc.highlight(7);
    	sc.highlight(8);
    	sc.highlight(9);
    	Text empCovVal = lang.newText(new Offset(200, -240, xOffset, AnimalScript.DIRECTION_NE), f.format(test.calcEmpiricalCovariance(cloud)), "empCovVal", null);
    	lang.nextStep();
    	
    	sc.unhighlight(6);
    	sc.unhighlight(7);
    	sc.unhighlight(8);
    	sc.unhighlight(9);
    	sc.highlight(11);
    	sc.highlight(12);
    	sc.highlight(13);
    	sc.highlight(14);
    	Text empVarXVal = lang.newText(new Offset(200, -190, xOffset, AnimalScript.DIRECTION_NE), f.format(test.calcEmpiricalVarianceOfX(cloud)), "empVarXVal", null);
    	lang.nextStep();
    	
    	sc.unhighlight(11);
    	sc.unhighlight(12);
    	sc.unhighlight(13);
    	sc.unhighlight(14);
    	sc.highlight(16);
    	sc.highlight(17);
    	String aTemp = f.format(test.calcA(cloud));
    	Text aVal = lang.newText(new Offset(200, -140, xOffset, AnimalScript.DIRECTION_NE), aTemp, "aVal", null);
    	lang.nextStep();
    	
    	sc.unhighlight(16);
    	sc.unhighlight(17);
    	sc.highlight(19);
    	sc.highlight(20);
    	String bTemp = f.format(test.calcB(cloud));
    	Text bVal = lang.newText(new Offset(200, -90, xOffset, AnimalScript.DIRECTION_NE), bTemp, "bVal", null);
    	
    	
    	lang.nextStep();
    	sc.unhighlight(19);
    	sc.unhighlight(20);
    	String funcTemp = "F(x) = " + aTemp + "x +"+ bTemp; 
    	TextProperties funcTextPr = new TextProperties();
    	funcTextPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	Text functionVal = lang.newText(new Offset(200, -40, xOffset, AnimalScript.DIRECTION_NE),funcTemp , "functionVal", null, funcTextPr);
    	Coordinates start = new Coordinates(urX, urY - (int) test.getValue(0, cloud));
    	Coordinates end = new Coordinates(urX + this.numberOfPoints, urY - (int) test.getValue(this.numberOfPoints, cloud));
    	
    	
    	PolylineProperties functPr = this.lineColor;
    	if(functPr == null) {
    		functPr = new PolylineProperties();
    		functPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	}
    	Polyline line = lang.newPolyline(new Node[] {start,  end}, "Line", null, functPr);
    	
    	lang.nextStep();
    	lang.hideAllPrimitives();
    	title.show();
    	titleRec.show();
    	
    	SourceCode outro = lang.newSourceCode(new Coordinates(40, 150), "outro", null);
    	outro.addCodeLine(tlr.translateMessage("outro0"), null, 0, null);
    	outro.addCodeLine(tlr.translateMessage("outro1"), null, 0, null);
    	outro.addCodeLine(tlr.translateMessage("outro2"), null, 0, null);
    	outro.addCodeLine(tlr.translateMessage("outro3"), null, 0, null);
    	outro.addCodeLine(tlr.translateMessage("outro4"), null, 0, null);
    	outro.addCodeLine(tlr.translateMessage("outro5"), null, 0, null);
    	outro.addCodeLine(tlr.translateMessage("outro6"), null, 0, null);
   	
    	lang.nextStep("Outro");
    	outro.hide();
    	
    	MultipleChoiceQuestionModel question1 = new MultipleChoiceQuestionModel("Frage 1");
    	question1.setNumberOfTries(1);
    	question1.setPrompt(tlr.translateMessage("question1"));
    	question1.addAnswer(tlr.translateMessage("answer11"), 1, tlr.translateMessage("right"));
    	question1.addAnswer(tlr.translateMessage("answer12"), 0, tlr.translateMessage("wrong1"));
    	question1.addAnswer(tlr.translateMessage("answer13"), 0, tlr.translateMessage("wrong1"));
    	lang.addMCQuestion(question1);
    	lang.nextStep();
    	
    	MultipleChoiceQuestionModel question2 = new MultipleChoiceQuestionModel("Frage 2");
    	question2.setNumberOfTries(1);
    	question2.setPrompt(tlr.translateMessage("question2"));
    	question2.addAnswer(tlr.translateMessage("answer21"), 0, tlr.translateMessage("wrong2"));
    	question2.addAnswer(tlr.translateMessage("answer22"), 1, tlr.translateMessage("right"));
    	question2.addAnswer(tlr.translateMessage("answer23"), 0, tlr.translateMessage("wrong2"));
    	lang.addMCQuestion(question2);
    	lang.nextStep();
    	
    	MultipleChoiceQuestionModel question3 = new MultipleChoiceQuestionModel("Frage 3");
    	question3.setNumberOfTries(1);
    	question3.setPrompt(tlr.translateMessage("question3"));
    	question3.addAnswer(tlr.translateMessage("answer31"), 0, tlr.translateMessage("wrong3"));
    	question3.addAnswer(tlr.translateMessage("answer32"), 1, tlr.translateMessage("right"));
    	question3.addAnswer(tlr.translateMessage("answer33"), 0, tlr.translateMessage("wrong3"));
    	lang.addMCQuestion(question3);
    }

    public String getName() {
        return tlr.translateMessage("name");
    }

    public String getAlgorithmName() {
        return tlr.translateMessage("title");
    }

    public String getAnimationAuthor() {
        return "Amon Ditzinger, Dirk Schumacher";
    }

    public String getDescription(){
        return tlr.translateMessage("description0")
 +"\n"
 +"\n"
 + tlr.translateMessage("description1")
 +"\n"
 + tlr.translateMessage("description2")
 +"\n"
 + tlr.translateMessage("description3")
 +"\n"
 +"\n"
 + tlr.translateMessage("description4")
 +"\n"
 +"\n"
 + tlr.translateMessage("description5")
 +"\n"
 +"\n"
 + tlr.translateMessage("description6")
 +"\n"
 + tlr.translateMessage("description7")
 + tlr.translateMessage("description8")
 +"\n"
 + tlr.translateMessage("description9")
 + tlr.translateMessage("description10")
 +"\n"
 + tlr.translateMessage("description11")
 +"\n"
 +"\n";
    } 
    
    public String getCodeExample(){
        return "Regressionsgerade(points):"
 +"\n"
 +"\n"
 +"	// y = ax + b"
 +"\n"
 +"	xAverage = calcAverageX(points)"
 +"\n"
 +"	yAverage = calcAverageY(points)"
 +"\n"
 +"\n"
 +"	// calculate empirical covariance"
 +"\n"
 +"	empiricalCovariance = 0"
 +"\n"
 +"	for(point in points):"
 +"\n"
 +"		empiricalCovariance += (point.x - xAverage) * (point.y - yAverage)"
 +"\n"
 +"\n"
 +"	// calculate empirical variance of x"
 +"\n"
 +"	empricalVarianceX = 0"
 +"\n"
 +"	for(point in points):"
 +"\n"
 +"		empricalVarianceX += (point.x - xAverage)�"
 +"\n"
 +"\n"
 +"	// calculate a"
 +"\n"
 +"	a = empiricalCovariance / empricalVarianceX"
 +"\n"
 +"\n"
 +"	// calculate b"
 +"\n"
 +"	b = yAverage - a * xAverage";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}
