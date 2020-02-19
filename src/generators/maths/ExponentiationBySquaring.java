/*
 * exponentiationBySquaring.java
 * Aleksej Strassheim, Konstantin Strassheim, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.maths;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import java.util.Locale;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.Hashtable;
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
import algoanim.properties.SourceCodeProperties;

public class ExponentiationBySquaring implements ValidatingGenerator {
    private Language lang;
    private TextProperties textRecursiveJumps;
    private SourceCodeProperties sourceCode;
    private TextProperties textTitle;
    private double x;
    private TextProperties textDescription;
    private TextProperties textStats;
    private int n;
    private TextProperties textHeaderStats;
    private Translator trans;
    private Text title;
    private Polyline title_line;
    private SourceCode src;
    private Text mult;
    private Text div;
    private Text recursiveTitle;
    private int divValue;
    private int multValue;
    private double returnValue;
    private Font userTitleFont;
    private Font userDescFont;
    private Text topRec;
    private Locale locale;
    private String path;
    private int questionCounter;
    private int userQuestion;
    
    
    public ExponentiationBySquaring(String path, Locale local) {
    	this.locale = local;
    	this.path = path;
    	trans = new Translator(path, getContentLocale());
    }
    public void init(){
        lang = new AnimalScript("Exponentiation by squaring", "Aleksej Strassheim, Konstantin Strassheim", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        textRecursiveJumps = (TextProperties)props.getPropertiesByName("textRecursiveJumps");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        textTitle = (TextProperties)props.getPropertiesByName("textTitle");
        x = (double)primitives.get("x");
        textDescription = (TextProperties)props.getPropertiesByName("textDescription");
        textStats = (TextProperties)props.getPropertiesByName("textStats");
        n = (Integer)primitives.get("n");
        userQuestion = (int)primitives.get("question_seed");
        textHeaderStats = (TextProperties)props.getPropertiesByName("textHeaderStats");
        
        //Get User Fonts
        userTitleFont = (Font) textTitle.get("font");
        userDescFont = (Font) textDescription.get("font");
        questionCounter = 0;
        
        
        userQuestion = 100;
        createGenerator();
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Exponentiation by squaring";
    }

    public String getAlgorithmName() {
        return "Exponentiation by squaring";
    }

    public String getAnimationAuthor() {
        return "Aleksej Strassheim, Konstantin Strassheim";
    }

    public String getDescription(){
    	String str = "";
    	str += trans.translateMessage("description1") + "\n";
    	str += trans.translateMessage("description2") + "\n";
    	str += trans.translateMessage("description3") + "\n";
    	str += trans.translateMessage("description4") + "\n";
    	str += trans.translateMessage("description5") + "\n";
        return str;
    }

    public String getCodeExample(){
        return "Instead of 10^10 = 10 * 10 * 10 * 10 * 10 * 10 * 10 * 10 * 10 * 10"+"\n"
        		+"We can calculate 10^10 = (10*10)^5 = 10(100*100)^2 = (100.000*100.000)^1 = 10.000.000.000";
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
	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		int user = (int)arg1.get("question_seed");
		if(user < 0 || user > 100) {
			return false;
		}
		return true;
	}
	private void createGenerator() {
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		QuestionGroupModel qs = new QuestionGroupModel("reccallquestion", 3);
		lang.addQuestionGroup(qs);
		createTitle(200, 30);
		createIntro();
		createCode();
		createStats();
		createAlgo();
		createEndcard();
		
	}

	private void createTitle(int x, int y) {
		textTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userTitleFont.getFamily(), Font.BOLD, 32));
		title = lang.newText(new Coordinates(x,y), trans.translateMessage("title"), "title", null, textTitle);
		Node[] nodes = {new Offset(-10, 0, "title", AnimalScript.DIRECTION_SW), new Offset(10, 0, "title", AnimalScript.DIRECTION_SE)};
		title_line = lang.newPolyline(nodes, "title_line", null);
	}
	private void createQuestion(int correctLine, double x, int n) {
		if((int)(Math.random()*100) >= userQuestion) return;
		questionCounter++;
		MultipleChoiceQuestionModel quest = new MultipleChoiceQuestionModel("question"+questionCounter);
		String wrongAnswer = trans.translateMessage("wrongAnswer");
		String correctAnswer = trans.translateMessage("correctAnswer");
		switch(correctLine) {
		case 1:
			wrongAnswer+= " " + "exp_by_squaring(1/" + getTextForX(x) + ", -" + n + ")";
			break;
		case 2:
			wrongAnswer+= " " + "return 1";
			break;
		case 3:
			wrongAnswer+= " " + "return "+getTextForX(x);
			break;
		case 4:
			wrongAnswer+= " " + "exp_by_squaring("+getTextForX(x)+"*"+getTextForX(x)+", "+ n + "/2)";
			break;
		case 5:
			wrongAnswer+= " " + getTextForX(x) + " * exp_by_squaring("+getTextForX(x)+"*"+getTextForX(x)+", ("+ n + "-1)/2)";
			break;
		}
		quest.setGroupID("reccallquestion");
		quest.setNumberOfTries(2);
		quest.setPrompt(trans.translateMessage("question1"));
		
		quest.addAnswer("exp_by_squaring(1/" + x + ", -" + n + ")", correctLine == 1 ? 1 : 0, correctLine == 1 ? correctAnswer : wrongAnswer);
		quest.addAnswer("return 1", correctLine == 2 ? 1 : 0 ,correctLine == 2 ? correctAnswer : wrongAnswer);
		quest.addAnswer("return " + x, correctLine == 3 ? 1 : 0, correctLine == 3 ? correctAnswer : wrongAnswer);
		quest.addAnswer("exp_by_squaring("+x+"*"+x+", "+ n + "/2)", correctLine == 4 ? 1 : 0, correctLine == 4 ? correctAnswer : wrongAnswer);
		quest.addAnswer(x + " * exp_by_squaring("+x+"*"+x+", ("+ n + "-1)/2)", correctLine == 5 ? 1 : 0, correctLine == 5 ? correctAnswer : wrongAnswer);
		
		lang.addMCQuestion(quest);
	}
	private void createIntro() {
		textDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 16));
		lang.newText(new Offset(-100, 50, "title", AnimalScript.DIRECTION_SW), trans.translateMessage("intro1"), "intro1", null, textDescription);
		lang.newText(new Offset(0, 25, "intro1", AnimalScript.DIRECTION_NW), trans.translateMessage("intro2"), "intro2", null, textDescription);
		lang.newText(new Offset(0, 45, "intro2", AnimalScript.DIRECTION_NW), "x  =", "intro3", null, textDescription);
		textDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 12));
		lang.newText(new Offset(10, 1, "intro3", AnimalScript.DIRECTION_NW), "n", "intro4", null, textDescription);
		textDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 64));
		lang.newText(new Offset(40, -30, "intro3", AnimalScript.DIRECTION_NW), "{", "intro5", null, textDescription);
		textDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 24));
		lang.newText(new Offset(30, 15, "intro5", AnimalScript.DIRECTION_NW), "x(x  )   ,   "+trans.translateMessage("intro6"), "intro6", null, textDescription);
		textDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 12));
		lang.newText(new Offset(35, 0, "intro6", AnimalScript.DIRECTION_NW), "2", "intro7", null, textDescription);
		lang.newText(new Offset(55, -8, "intro6", AnimalScript.DIRECTION_NW), "n-1", "intro8", null, textDescription);
		lang.newText(new Offset(2, 0, "intro8", AnimalScript.DIRECTION_NW), "___", "intro9", null, textDescription);
		lang.newText(new Offset(5, 13, "intro8", AnimalScript.DIRECTION_NW), "2", "intro10", null, textDescription);
		textDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 24));
		lang.newText(new Offset(0, 35, "intro6", AnimalScript.DIRECTION_NW), "(x  )  ,      "+trans.translateMessage("intro11"), "intro11", null, textDescription);
		textDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 12));
		lang.newText(new Offset(20, 0, "intro11", AnimalScript.DIRECTION_NW), "2", "intro12", null, textDescription);
		lang.newText(new Offset(45, -5, "intro11", AnimalScript.DIRECTION_NW), "n", "intro13", null, textDescription);
		lang.newText(new Offset(-3, 0, "intro13", AnimalScript.DIRECTION_NW), "__", "intro14", null, textDescription);
		lang.newText(new Offset(0, 10, "intro13", AnimalScript.DIRECTION_NW), "n", "intro15", null, textDescription);
		textDescription.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 16));
		lang.newText(new Offset(0, 95, "intro3", AnimalScript.DIRECTION_NW), trans.translateMessage("intro16"), "intro16", null, textDescription);
		lang.newText(new Offset(0, 20, "intro16", AnimalScript.DIRECTION_NW), trans.translateMessage("intro17"), "intro17", null, textDescription);
		lang.newText(new Offset(0, 20, "intro17", AnimalScript.DIRECTION_NW), trans.translateMessage("intro18"), "intro18", null, textDescription);
		lang.newText(new Offset(0, 20, "intro18", AnimalScript.DIRECTION_NW), trans.translateMessage("intro19"), "intro19", null, textDescription);
		lang.newText(new Offset(0, 20, "intro19", AnimalScript.DIRECTION_NW), trans.translateMessage("intro20"), "intro20", null, textDescription);
		lang.nextStep(trans.translateMessage("section1"));
		lang.hideAllPrimitives();
		title.show();
		title_line.show();
	}
	private void createCode() {
		src = lang.newSourceCode(new Offset(-100, 50, "title", AnimalScript.DIRECTION_SW), "code", null, sourceCode);
		src.addCodeLine("Function exp_by_squaring(x, n)", null, 1, null);
		src.addCodeLine("if n < 0  then return exp_by_squaring(1 / x, -n);", null, 2, null);
		src.addCodeLine("else if n = 0  then return  1;", null, 2, null);
		src.addCodeLine("else if n = 1  then return  x ;", null, 2, null);
		src.addCodeLine("else if n is even  then return exp_by_squaring(x * x,  n / 2);", null, 2, null);
		src.addCodeLine("else if n is odd  then return x * exp_by_squaring(x * x, (n - 1) / 2);", null, 2, null);
		
		
	}
	private void createStats() {
		textHeaderStats.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.BOLD, 16));
		lang.newText(new Offset(400, 0, src, AnimalScript.DIRECTION_NE), trans.translateMessage("multi"), "head_mult", null, textHeaderStats);
		lang.newText(new Offset(0, 5, "head_mult", AnimalScript.DIRECTION_SW), trans.translateMessage("div"), "head_div", null, textHeaderStats);
		textStats.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 16));
		mult = lang.newText(new Offset(100, 0, "head_mult", AnimalScript.DIRECTION_NE), "0", "mult", null, textStats);
		div = lang.newText(new Offset(0, 5, "mult", AnimalScript.DIRECTION_SW), "0", "div", null, textStats);
		
		recursiveTitle = lang.newText(new Offset(-120, 50, "head_div", AnimalScript.DIRECTION_SW), trans.translateMessage("recursive_calls"), "head_rec", null, textHeaderStats);
		lang.nextStep();
	}
	private void createAlgo() {
		textRecursiveJumps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.PLAIN, 16));
		topRec = lang.newText(new Offset(50, 5, recursiveTitle.getName(), AnimalScript.DIRECTION_SW),"exp_by_squaring(" + getTextForX(x) + ", " + n + ")", "rec0", null, textRecursiveJumps);
		lang.nextStep(trans.translateMessage("funccall"));
		returnValue = exponentiation(0, topRec.getName(), x, n);
		src.highlight(1);
		
	}
	private void highlightCodeUntil(int i) {
		for(int j = 0; j<=i; j++) {
			if(j>0) {
				src.unhighlight(j-1);
			}
			src.highlight(j);
			if(j!=i)lang.nextStep();
		}
	}
	private String getTextForX(double x) {
		NumberFormat f = NumberFormat.getInstance(getContentLocale());
		f.setMinimumIntegerDigits(1);
		f.setMaximumFractionDigits(30);
		if((x - (int)x) == 0) {
			f.setMaximumFractionDigits(0);
		}
		return f.format(x);
		
	}
	private Polyline createImpArrowFw(String targetId) {
		//Node[] nodes = {new Coordinates(x, y), new Coordinates(x, y+10), new Coordinates(x+20, y+10), new Coordinates(x+15, y+15), new Coordinates(x+20, y+10), new Coordinates(x+15, y+5)};
		Node[] nodes = {new Offset(-30, -10, targetId, AnimalScript.DIRECTION_W), new OffsetFromLastPosition(0, 10), new OffsetFromLastPosition(20, 0), new OffsetFromLastPosition(-5, 5), new OffsetFromLastPosition(5, -5), new OffsetFromLastPosition(-5, -5)};
		return lang.newPolyline(nodes, "recArrowFw", null);
	
	}
	private Polyline createImpArrowBw(String targetId) {
		Node[] nodes = {new Offset(-10, 0, targetId, AnimalScript.DIRECTION_W), new OffsetFromLastPosition(-20, 0), new OffsetFromLastPosition(0, -10), new OffsetFromLastPosition(-5, 5), new OffsetFromLastPosition(5, -5), new OffsetFromLastPosition(5, 5)};
		return lang.newPolyline(nodes, "recArrowBw", null);
	}
	private double exponentiation(int recCounter, String lastId, double x, int n) {
		recCounter++;
		
		if(n < 0) {
			//Question
			createQuestion(1, x, n);
			//-------
			//Highlight code till this point
			highlightCodeUntil(1);
			//Creating new Text box
			Text text = lang.newText(new Offset(50, 5, lastId, AnimalScript.DIRECTION_SW),"exp_by_squaring(1/" + getTextForX(x) + ", -" + n + ")", "exp"+recCounter, null, textRecursiveJumps);
			Polyline imp = createImpArrowFw(text.getName());
			lastId = text.getName();
			lang.nextStep();
			//Summarize
			text.setText("exp_by_squaring("+getTextForX(1/x) + ", " + (-n) + ")", null, null);
			divValue++;
			div.setText(""+divValue, null, null);
			lang.nextStep(trans.translateMessage("reccall") + " " + recCounter);
			src.unhighlight(1);
			double val = exponentiation( recCounter, text.getName(), 1/x, -n);
			src.highlight(1);
			text.setText(getTextForX(val), null, null);
			imp.hide();
			Polyline backImp = createImpArrowBw(text.getName());
			lang.nextStep();
			text.hide();
			backImp.hide();
			src.unhighlight(1);
			return val;
		}
		else if(n==0) {
			//Question
			createQuestion(2, x, n);
			//-------
			highlightCodeUntil(2);
			Text text = lang.newText(new Offset(50, 5, lastId, AnimalScript.DIRECTION_SW), "1", "exp"+recCounter, null, textRecursiveJumps);
			Polyline imp = createImpArrowFw(text.getName());
			lastId = text.getName();
			lang.nextStep();
			imp.hide();
			Polyline backImp = createImpArrowBw(text.getName());
			lang.nextStep(trans.translateMessage("return"));
			src.unhighlight(2);
			backImp.hide();
			text.hide();
			return 1;
		}
		else if(n==1) {
			//Question
			createQuestion(3, x, n);
			//-------
			highlightCodeUntil(3);
			Text text = lang.newText(new Offset(50, 5, lastId, AnimalScript.DIRECTION_SW),""+getTextForX(x), "exp"+recCounter, null, textRecursiveJumps);
			Polyline imp = createImpArrowFw(text.getName());
			lastId = text.getName();
			lang.nextStep();
			imp.hide();
			Polyline backImp = createImpArrowBw(text.getName());
			lang.nextStep(trans.translateMessage("return"));
			src.unhighlight(3);
			backImp.hide();
			text.hide();
			return x;
		}
		else if(n%2==0) {
			//Question
			createQuestion(4, x, n);
			//-------
			//Highlight code
			highlightCodeUntil(4);
			Text text = lang.newText(new Offset(50, 5, lastId, AnimalScript.DIRECTION_SW),"exp_by_squaring("+getTextForX(x)+"*"+getTextForX(x)+", "+ n + "/2)", "exp"+recCounter, null, textRecursiveJumps);
			lastId = text.getName();
			Polyline imp = createImpArrowFw(text.getName());
			lang.nextStep();
			//Summarize
			text.setText("exp_by_squaring("+(getTextForX(x*x)) + ", " + (n/2) +")", null, null);
			multValue++;
			mult.setText(""+multValue, null, null);
			//Don't count easy divisions? Division by to just right shift
			//divValue++;
			//div.setText(""+divValue, null, null);
			lang.nextStep(trans.translateMessage("reccall") + " " + recCounter);
			//Get into recursive
			src.unhighlight(4);
			double val = exponentiation(recCounter, text.getName(), x*x, n/2);
			//Summarize text
			src.highlight(4);
			text.setText(getTextForX(val), null, null);
			imp.hide();
			Polyline backImp = createImpArrowBw(text.getName());
			lang.nextStep();
			text.hide();
			src.unhighlight(4);
			backImp.hide();
			return val;
		}
		else {
			//Question
			createQuestion(5, x, n);
			//-------
			//Highlight Code
			highlightCodeUntil(5);
			//Creating new Text box and recursive arrow
			Text text = lang.newText(new Offset(50, 5, lastId, AnimalScript.DIRECTION_SW), getTextForX(x) + " * exp_by_squaring("+getTextForX(x)+"*"+getTextForX(x)+", ("+ n + "-1)/2)", "exp"+recCounter, null, textRecursiveJumps);
			Polyline imp = createImpArrowFw(text.getName());
			lang.nextStep();
			//Summarize 
			text.setText(getTextForX(x) + " * exp_by_squaring("+(getTextForX(x*x)) + ", " + ((n-1)/2) +")", null, null);
			//Increase Mult Counter
			multValue++;
			mult.setText(""+multValue, null, null);
			//Don't count easy divisions? Division by to just right shift
			//divValue++;
			//div.setText(""+divValue, null, null);
			lang.nextStep(trans.translateMessage("reccall") + " " + recCounter);
			//Get into recusrive
			src.unhighlight(5);
			double val = exponentiation(recCounter, text.getName(), x*x, (n-1)/2);
			src.highlight(5);
			//Get Value and resum text
			text.setText(getTextForX(x) + " * " + getTextForX(val), null, null);
			lang.nextStep();
			val = x * val;
			//Resum
			text.setText(getTextForX(val), null, null);
			multValue++;
			mult.setText(""+multValue, null, null);
			//Change Arrow
			imp.hide();
			Polyline backImp = createImpArrowBw(text.getName());
			lang.nextStep();
			src.unhighlight(5);
			text.hide();
			backImp.hide();
			return val;
		}
	}
	private void createEndcard() {
		int normMultValue = n == 0 ? 0 : Math.abs(n)-1;
		recursiveTitle.hide();
		topRec.hide();
		
		lang.newText(new Offset(0, 5, "head_div", AnimalScript.DIRECTION_SW), trans.translateMessage("result"), "result_title", null, textHeaderStats);
		lang.newText(new Offset(0, 5, "div", AnimalScript.DIRECTION_SW), ""+getTextForX(returnValue), "result_value", null, textDescription);
		lang.nextStep();
		
		lang.newText(new Offset(150, 50, "code", AnimalScript.DIRECTION_SW), multValue +  " " +trans.translateMessage("end_multi_rec"), "endcard1", null, textHeaderStats);
		lang.newText(new Offset(-15, 5, "endcard1", AnimalScript.DIRECTION_SW), trans.translateMessage("end_multi_rec_desc"), "endcard2", null, textStats);
		textHeaderStats.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.BOLD, 24));
		lang.newText(new Offset(150, 0, "endcard1", AnimalScript.DIRECTION_NE), "VS", "endcard3", null, textHeaderStats);
		textHeaderStats.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userDescFont.getFamily(), Font.BOLD, 16));
		lang.newText(new Offset(150, 0, "endcard3", AnimalScript.DIRECTION_NE), normMultValue + " " + trans.translateMessage("end_mutli_norm"), "endcard4", null, textHeaderStats);
		lang.newText(new Offset(-15, 5, "endcard4", AnimalScript.DIRECTION_SW), trans.translateMessage("end_multi_norm_desc"), "endcard5", null, textStats);
		lang.nextStep(trans.translateMessage("summary"));
	}
	public static void main(String[] args) {
		Generator generator = new ExponentiationBySquaring("resources/ExponentiationBySquaring", Locale.US); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

}