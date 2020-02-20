/*
 * Stern-Brocot-Folge.java
 * Philipp Schneider, Zi Wang, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import translator.Translator;

public class SternBrocot implements ValidatingGenerator {
    private Language lang;
    private ArrayMarkerProperties arrayMarkeri;
    private ArrayMarkerProperties arrayMarkerj;
    private SourceCodeProperties sourceCode;
    private RectProperties rectjMarkerBox;
    private TextProperties resulttext;
    private ArrayProperties array;
    private SourceCodeProperties descriptionText;
    private RectProperties rectiMarkerBox;
    private TextProperties markerivalue;
    private TextProperties markerjvalue;
    private int input_n;
    private int questionProp;
    private TwoValueCounter counter;
    private TextProperties endText;
    public Translator translator;
    //default Sprache auf Deutsch
    private Locale locale = Locale.GERMANY;
    public final static Timing defaultDuration = new TicksTiming(30);

    
    SternBrocot(String languageFilePath, Locale locale){  	
    	translator = new Translator("resources/SternBrocot",locale);
    	this.setLanguage(locale);
    }
    
    
	public void init(){    
		lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
    	        "Stern-Brocot-Folge", "Philipp Schneider, Zi Wang", 640, 480);
    }   
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	input_n = (Integer)primitives.get("input_n");
    	questionProp = (Integer)primitives.get("questionProp");
    	if(input_n > 0 && questionProp >=0 && questionProp<= 100) {
    		return true;
    	}
    	return false;
    }
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
        arrayMarkeri = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkeri");
        arrayMarkerj = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkerj");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        rectjMarkerBox = (RectProperties)props.getPropertiesByName("rectjMarkerBox");
        resulttext = (TextProperties)props.getPropertiesByName("resulttext");
        array = (ArrayProperties)props.getPropertiesByName("array");
        descriptionText = (SourceCodeProperties)props.getPropertiesByName("descriptionText");
        rectiMarkerBox = (RectProperties)props.getPropertiesByName("rectiMarkerBox");
        markerivalue = (TextProperties)props.getPropertiesByName("markerivalue");
        markerjvalue = (TextProperties)props.getPropertiesByName("markerjvalue");
        input_n = (Integer)primitives.get("input_n");
        questionProp = (Integer)primitives.get("questionProp");
        endText = (TextProperties)props.getPropertiesByName("endtext");        
       lang.setStepMode(true);
        int[] a = new int[input_n*2];
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        
    	TextProperties headerProps = new TextProperties();
    	Font fontHeader = new Font("SansSerif", Font.BOLD, 24);
    	headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, fontHeader);
    		
    	lang.newText(new Coordinates(20, 10), translator.translateMessage("header"), "header", null, headerProps);
    	
    	lang.nextStep(translator.translateMessage("bookmark1"));
    	
    	SourceCode descr = createDescription();

    	lang.nextStep(translator.translateMessage("bookmark2"));
    	
    	descr.hide();
    	
    	SourceCode sc = createSourceCode(descr);
            
        lang.nextStep();
     

        IntArray ia = createArray(a, descr); 
        CounterProperties cp = createCounter();
        
        createCounterProps(descr, sc, ia, cp);
       
        addAlgoQuestion();	
        	
        	
        lang.nextStep(translator.translateMessage("bookmark3"));
        	sourceCodeStep1(sc, ia);
        lang.nextStep();
        	sourceCodeStep2(sc, ia);
        lang.nextStep();
        	ia.unhighlightCell(1,1 , null, null);
        //Aufruf SternBrocot Berechnung auf array
        try {
        	sternBrocot(ia, sc, 0, (ia.getLength()-1));
        } catch (LineNotExistsException e) {
        	e.printStackTrace();
        }
        
        
        sc.hide();
        ia.hide();
        
        lang.nextStep();
        
        lang.finalizeGeneration();
            
        
        return lang.toString();
    }


	private void sourceCodeStep2(SourceCode sc, IntArray ia) {
		sc.toggleHighlight(3,0,false,4,0);
		ia.put(1, 1, null, null);
		ia.unhighlightCell(0, 0, null,null);
		ia.highlightCell(1, 1, null,null);
	}


	private void sourceCodeStep1(SourceCode sc, IntArray ia) {
		ia.unhighlightCell(0, ia.getLength()-1, null,null);
		sc.toggleHighlight(2,0, false, 3,0);
		ia.put(0, 0, null, null);
		ia.highlightCell(0, 0, null,null);
	}


	private void createCounterProps(SourceCode descr, SourceCode sc, IntArray ia, CounterProperties cp) {
		TwoValueView view = lang.newCounterView(counter,
                new Offset(500, 20, descr.getUpperLeft(), null), cp, true, true);
        	ia.highlightCell(0, ia.getLength()-1, null,null);
        	sc.highlight(2,0,false);
	}


	private CounterProperties createCounter() {
		CounterProperties cp = new CounterProperties(); 
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); 
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		return cp;
	}


	private IntArray createArray(int[] a, SourceCode descr) {
		IntArray ia = lang.newIntArray(new Offset(400, 250, descr.getUpperLeft(), null), a, "A", null, array);
        counter = lang.newCounter(ia);
		return ia;
	}


	private SourceCode createSourceCode(SourceCode descr) {
		SourceCode sc = lang.newSourceCode(new Offset(-10, 90, descr.getUpperLeft(), null), "Pseudocode", null, sourceCode);
    	
        sc.addCodeLine("public array[] Stern-Brocot-Folge(int n)", null, 0,
                null); // 0
            sc.addCodeLine("{", null, 0, null);
            sc.addCodeLine("array sbArray[n*2];", null, 1, null);
            sc.addCodeLine("int sbArray[0] = 1;", null, 1, null); 
            sc.addCodeLine("int sbArray[1] = 1;", null, 1, null); 
            sc.addCodeLine("for ( int i=0; i<=n; i++){", null, 2, null);
            sc.addCodeLine("sbArray[2*i]=sbArray[i];", null, 3, null); 
            sc.addCodeLine("sbArray[2*i+1]=sbArray[i] + sbArray[i+1];", null, 3, null);
            sc.addCodeLine("}", null, 2, null);
            sc.addCodeLine("return sbArray;", null, 1, null); 
            sc.addCodeLine("}", null, 0, null);
		return sc;
	}


	private SourceCode createDescription() {
		SourceCode descr = lang.newSourceCode(new Coordinates(20,30), "Description", null, descriptionText);

    	descr.addCodeLine(translator.translateMessage("description1"), null, 0,
            null); // 0
    	descr.addCodeLine(translator.translateMessage("description2"), null, 0, null);
    	descr.addCodeLine(translator.translateMessage("description3"), null, 0, null);
    	descr.addCodeLine(translator.translateMessage("description4"), null, 0, null); 
    	descr.addCodeLine(translator.translateMessage("description5"), null, 0, null); 
    	descr.addCodeLine(translator.translateMessage("description6"), null, 0, null);
		return descr;
	}
    
    private int pointerCounter;
    

    private void addPivotQuestion(int i) {
    	QuestionGroupModel pivotQuestions = new QuestionGroupModel("pivotQuestions", 3);
    	
    	if(Math.random()*100<questionProp) {
    	MultipleChoiceQuestionModel pivotQ1 = new MultipleChoiceQuestionModel("pivotQ1");
    	pivotQ1.setPrompt(translator.translateMessage("pivotQ1_1"));
    	pivotQ1.addAnswer("2", 1, translator.translateMessage("pivotQ1_2"));
    	pivotQ1.addAnswer("1", 0, translator.translateMessage("pivotQ1_3"));
    	pivotQ1.addAnswer("3", 0, translator.translateMessage("pivotQ1_3"));
    	pivotQ1.addAnswer("4", 0, translator.translateMessage("pivotQ1_3"));
    	pivotQ1.setGroupID("pivotQuestions");
    	lang.addMCQuestion(pivotQ1);
    	lang.nextStep();
    	}
    	
    	if(Math.random()*100<questionProp) {
    	MultipleChoiceQuestionModel pivotQ2 = new MultipleChoiceQuestionModel("pivotQ2");
    	pivotQ2.setPrompt(translator.translateMessage("pivotQ2_1"));
    	pivotQ2.addAnswer("8", 1, translator.translateMessage("pivotQ2_2"));
    	pivotQ2.addAnswer("6", 0, translator.translateMessage("pivotQ2_3"));
    	pivotQ2.addAnswer("2", 0, translator.translateMessage("pivotQ2_3"));
    	pivotQ2.addAnswer("4", 0, translator.translateMessage("pivotQ2_3"));
    	pivotQ2.setGroupID("pivotQuestions");
    	lang.addMCQuestion(pivotQ2);
    	lang.nextStep();
    	}
    	
    	if(Math.random()*100<questionProp) {
        	MultipleChoiceQuestionModel pivotQ3 = new MultipleChoiceQuestionModel("pivotQ3");
        	pivotQ3.setPrompt(translator.translateMessage("pivotQ3_1"));
        	pivotQ3.addAnswer(Integer.toString(i), 1, translator.translateMessage("pivotQ3_2")+Integer.toString(i));
        	pivotQ3.addAnswer(Integer.toString(i-1), 0, translator.translateMessage("pivotQ3_3"));
        	pivotQ3.addAnswer(Integer.toString(i+1), 0, translator.translateMessage("pivotQ3_3"));
        	pivotQ3.addAnswer(Integer.toString(i+2), 0, translator.translateMessage("pivotQ3_3"));
        	pivotQ3.setGroupID("pivotQuestions");
        	lang.addMCQuestion(pivotQ3);
        	lang.nextStep();
        	}
        	
    	
    	lang.addQuestionGroup(pivotQuestions);
    	
    }
    private void addResultQuestion() {
    	QuestionGroupModel resultQuestions = new QuestionGroupModel("resultQuestions", 2);
    	
    	if(Math.random()*100<questionProp) {
        	MultipleChoiceQuestionModel resultQ1 = new MultipleChoiceQuestionModel("resultQ1");
        	resultQ1.setPrompt(translator.translateMessage("resultQ1_1"));
        	resultQ1.addAnswer(Integer.toString(input_n*2), 1, translator.translateMessage("resultQ1_2"));
        	resultQ1.addAnswer(Integer.toString(input_n), 0, translator.translateMessage("resultQ1_3"));
        	resultQ1.addAnswer(Integer.toString(input_n/2), 0, translator.translateMessage("resultQ1_3"));
        	resultQ1.setGroupID("resultQuestions");
        	lang.addMCQuestion(resultQ1);
        	}
    	
    	
    lang.addQuestionGroup(resultQuestions);
    }
    
    private void addAlgoQuestion() {
    	QuestionGroupModel algoQuestions = new QuestionGroupModel("algoQuestions", 3);
    	
    	if(Math.random()*100<questionProp) {
    	FillInBlanksQuestionModel init2 = new FillInBlanksQuestionModel("init2");
    		init2.setPrompt(translator.translateMessage("initQ2_1"));
    		init2.addAnswer("1", 1, translator.translateMessage("initQ2_2"));
    		init2.setGroupID("algoQuestions");	
    		lang.addFIBQuestion(init2);
    	lang.nextStep();	
    	}
    		
    	if(Math.random()*100<questionProp) {
    	FillInBlanksQuestionModel init1 =
        		new FillInBlanksQuestionModel("init1");
    		init1.setPrompt(translator.translateMessage("initQ1_1"));
    		init1.addAnswer("0", 1, translator.translateMessage("initQ1_2"));
    		init1.setGroupID("algoQuestions");
    		lang.addFIBQuestion(init1);
    	lang.nextStep();
    	}
    	
    	if(Math.random()*100<questionProp) {		
    	FillInBlanksQuestionModel algoYear =
    		new FillInBlanksQuestionModel("year");
    		algoYear.setPrompt(translator.translateMessage("algoYear1_1"));
    		algoYear.addAnswer("1952", 1, translator.translateMessage("algoYear1_2"));
    		algoYear.setGroupID("algoQuestions");
    		lang.addFIBQuestion(algoYear);
    	lang.nextStep();
    	}
    	
    	if(Math.random()*100<questionProp) {
    	FillInBlanksQuestionModel author =	
    		new FillInBlanksQuestionModel("author");
    		author.setPrompt(translator.translateMessage("authorQ1_1"));
    		author.addAnswer("Stern", 1, translator.translateMessage("authorQ1_1"));
    		author.addAnswer("Brocot", 1, translator.translateMessage("authorQ1_1"));
    		author.setGroupID("algoQuestions");
    		lang.addFIBQuestion(author);
    	lang.nextStep();
    	}
    		
    	lang.addQuestionGroup(algoQuestions);
    }
    	
    
    private void sternBrocot(IntArray array, SourceCode codeSupport, int l, int r)
    		throws LineNotExistsException{
    	
    			codeSupport.toggleHighlight(4,0,false,5,0);
    			
    			pointerCounter++;
    			ArrayMarker iMarker = lang.newArrayMarker(array, 0, "i"+ pointerCounter, null, arrayMarkeri);
    			
    			pointerCounter++;
    			
    		    ArrayMarker jMarker = lang.newArrayMarker(array, 1, "j" + pointerCounter,
    		        null, arrayMarkerj);

    		    int i, j;
    		    
    		    Rect iMarkerBox = createIMarker();
    		  
    		    Text iMarkerValue = createIValue(iMarkerBox);
    		    
    		    Rect jMarkerBox = createJMarker();
    		    
    		    Text jMarkerValue = createJValue(jMarkerBox);
    		     
    		    lang.nextStep(translator.translateMessage("bookmark4"));
    		    
    		    if(r >1 ) {
    		    	int i1=0;
    		    	for( i1 = l, j = r; i1 < (j/2)+1;) {
        		    	addPivotQuestion(i1);
        		    	
    		    		loopStep1(array, codeSupport, iMarker, iMarkerValue, i1);
    		    		lang.nextStep();
    		    		
    		    		loopStep2(array, codeSupport, jMarker, jMarkerValue, i1);
    		    		
    		    		lang.nextStep();
    		    		
    		    		loopStep3(array, codeSupport, jMarker, jMarkerValue, i1); 		    		
    		    		
    		    		i1++;
    		    		lang.nextStep();
    		    	}   	   		    	
    		    }
    		    lang.nextStep(translator.translateMessage("bookmark5"));
    		    
    		    counter.deactivateCounting();
    		    
    		    StringBuilder output = createStringBuilder(array);
    		    
    		    hideEverything(array, iMarker, jMarker, iMarkerBox, iMarkerValue, jMarkerBox, jMarkerValue);
    		    
    		    createFinishText(array, codeSupport, output);
    		    
    		    addResultQuestion();
    		    
    		    lang.nextStep(translator.translateMessage("bookmark6"));
    		    
    		    lang.hideAllPrimitives();
    		    createEndText();
    		    
    	}


	private void createEndText() {
		Text end1 = lang.newText(new Coordinates(100,40),translator.translateMessage("end1"), "end1", null, endText);
		Text end2 = lang.newText(new Offset(0, 20, end1.getUpperLeft(), null),translator.translateMessage("end2"), "end2", null, endText);
		Text end3 = lang.newText(new Offset(0, 40, end1.getUpperLeft(), null),translator.translateMessage("end3"), "end3", null, endText);
		Text end4 = lang.newText(new Offset(0, 60, end1.getUpperLeft(), null),translator.translateMessage("end4"), "end4", null, endText);
		Text end5 = lang.newText(new Offset(0, 80, end1.getUpperLeft(), null),translator.translateMessage("end5"), "end5", null, endText);
		Text end6 = lang.newText(new Offset(0, 100, end1.getUpperLeft(), null),translator.translateMessage("end6"), "end6", null, endText);
	}


	private void createFinishText(IntArray array, SourceCode codeSupport, StringBuilder output) {
		Text resultRow1 = lang.newText(new Coordinates(80,40), translator.translateMessage("result1") +" " +array.getLength() + " "+translator.translateMessage("result2"), "resultrow1", null, resulttext);
		Text resultRow2 = lang.newText(new Offset(0, 20, resultRow1.getUpperLeft(), null), output.toString(),"resultrow2", null, resulttext);
		Text resultRow3 = lang.newText(new Offset(0, 40, resultRow1.getUpperLeft(), null), translator.translateMessage("result3"),"resultrow2", null, resulttext); 		    
		codeSupport.toggleHighlight(7,0,false, 9,0);
	}


	private void hideEverything(IntArray array, ArrayMarker iMarker, ArrayMarker jMarker, Rect iMarkerBox,
			Text iMarkerValue, Rect jMarkerBox, Text jMarkerValue) {
		iMarker.hide();
		iMarkerBox.hide();
		iMarkerValue.hide();
		jMarker.hide();
		jMarkerBox.hide();
		jMarkerValue.hide();
		array.hide();
	}


	private StringBuilder createStringBuilder(IntArray array) {
		StringBuilder output = new StringBuilder();
		for(int e=0; e<array.getLength();e++) {
			output.append(Integer.toString(array.getData(e)));
			output.append(", ");
		}
		return output;
	}


	private void loopStep3(IntArray array, SourceCode codeSupport, ArrayMarker jMarker, Text jMarkerValue, int i1) {
		codeSupport.toggleHighlight(6,0,false,7,0);
		array.unhighlightCell(2*i1, 2*i1, null, defaultDuration);
		jMarker.move(2*i1+1, null, defaultDuration);
		jMarkerValue.setText("Array["+Integer.toString(i1) +"]" + "+ " +"Array["+ i1 +  "+1" +"]" +"  (" + array.getData(i1) +" + "+array.getData(i1+1) + ")", null, defaultDuration);
		array.highlightCell(2*i1+1, 2*i1+1, null, defaultDuration);
		int arrayComputed = array.getData(i1)+ array.getData(i1+1);
		array.put((2*i1+1), arrayComputed, null, defaultDuration);
	}


	private void loopStep2(IntArray array, SourceCode codeSupport, ArrayMarker jMarker, Text jMarkerValue, int i1) {
		codeSupport.toggleHighlight(5,0,false,6,0);  		    		  		    		
		jMarker.move((2*i1), null, defaultDuration);
		jMarkerValue.setText("Array["+Integer.toString(i1) +"]" + "  (" + array.getData(i1) + ")", null, defaultDuration);
		array.highlightCell(2*i1, 2*i1, null, defaultDuration);
		array.unhighlightCell(2*i1-1, 2*i1-1, null, defaultDuration);
		array.put((2*i1), array.getData(i1), null, defaultDuration);
	}


	private void loopStep1(IntArray array, SourceCode codeSupport, ArrayMarker iMarker, Text iMarkerValue, int i1) {
		codeSupport.toggleHighlight(7,0,false,5,0);
		array.unhighlightElem(i1-1, null, defaultDuration);
		iMarker.move((i1), null, defaultDuration);
		iMarkerValue.setText(Integer.toString(i1), null, defaultDuration);
		array.highlightElem(i1, null, defaultDuration);
	}


	private Text createJValue(Rect jMarkerBox) {
		markerjvalue.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		Text jMarkerValue = lang.newText(jMarkerBox.getUpperLeft(), "0", "iMarkerValue", null, markerjvalue);
		return jMarkerValue;
	}


	private Rect createJMarker() {
		Rect jMarkerBox = lang.newRect(new Coordinates(450, 180), new Coordinates(740, 220), "jMarkerBox", null, rectjMarkerBox);
		return jMarkerBox;
	}


	private Text createIValue(Rect iMarkerBox) {
		markerivalue.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		Text iMarkerValue = lang.newText(iMarkerBox.getUpperLeft(), "0", "iMarkerValue", null, markerivalue);
		return iMarkerValue;
	}


	private Rect createIMarker() {
		RectProperties iMarkerProps = new RectProperties();
		iMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		Rect iMarkerBox = lang.newRect(new Coordinates(400, 180), new Coordinates(430, 220), "iMarkerBox", null, rectiMarkerBox);
		return iMarkerBox;
	}

    public String getName() {
        return "Stern-Brocot-Folge";
    }

    public String getAlgorithmName() {
        return "Stern-Brocot-Folge";
    }

    public String getAnimationAuthor() {
        return "Philipp Schneider, Zi Wang";
    }

    public String getDescription(){
        return 
        		
      translator.translateMessage("generatorNewLine")    		
    + translator.translateMessage("generatorDescription1")
    + translator.translateMessage("generatorNewLine") 
    + translator.translateMessage("generatorDescription2")
    + translator.translateMessage("generatorNewLine") 
    + translator.translateMessage("generatorDescription3")
    + translator.translateMessage("generatorNewLine") 
    + translator.translateMessage("generatorDescription4")
    + translator.translateMessage("generatorNewLine") 
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription5")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription6")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription7")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription8")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription9")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription10")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription11")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription12")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription13")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorDescription14")
    + translator.translateMessage("generatorNewLine")
    + translator.translateMessage("generatorNewLine");
 
    }

    public String getCodeExample(){
        return "public Stern-Brocot-" + translator.translateMessage("header") + "( int n ){"
 +"\n"
 +"     Int[ ] sbArray = new Int[ n*2 ];"
 +"\n"
 +"     sbArray[ 0 ] = 0;"
 +"\n"
 +"     sbArray[ 1 ] = 1;"
 +"\n"
 +"     for( int i = 0; i <= n ; i++) {"
 +"\n"
 +"          sbArray[ 2*i ] = sbArray[ i ];"
 +"\n"
 +"          sbArray[ 2*i+1 ] = sbArray[ i ] + sbArray[ i +1 ];"
 +"\n"
 +"     }"
 +"\n"
 +"}";
    }

    public void setLanguage(Locale lang) {
    	this.locale = lang;
    	this.translator.setTranslatorLocale(this.locale);
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
        return Generator.JAVA_OUTPUT;
    }
    
    public static void main(String[] args) {
    	SternBrocot generator = new SternBrocot("resources/Stern_Brocot", Locale.US); // Generator erzeugen
    	Animal.startGeneratorWindow(generator); // Animal mit Generator starten
    	}

}