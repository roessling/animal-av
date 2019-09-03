/*
 * FastInverseSquareRoot
 * Copyright (c) 2018 by Max Kaiser, David Langsam
 * An Algorithm Generator for the "Animation und Visualisierung von Algorithmen" course at TU Darmstadt
 * using Animal AV API.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.AlgoAnimBackend;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.helpersFastInvsqrt.FastInverseHelper;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;


public class FastInverseSquareRoot implements /*Generator*/ ValidatingGenerator {
	
	private static int width  = 800;
	private static int height = 600;
	private static int xTextOffset = 100;
	private int currentOffset = 5;
	private final Locale locale;
	private Translator tl;
	private Language language;
	private Variables animal_variables;


 	public FastInverseSquareRoot(Locale l) {
 		locale = l;
		tl = new Translator("resources/FastInverseSquareRoot", locale);
		//Locale.GERMANY -> .de_DE    Locale.GERMAN   -> .de
		//Locale.US      -> .en_US    Locale.ENGLISCH -> .en
	}

	
	public static void main(String[] args) {
		FastInverseSquareRoot f_invsqrt = new FastInverseSquareRoot(Locale.GERMANY);
		Animal.startGeneratorWindow(f_invsqrt);

	}
	
	@Override
	public void init() {
		//language = new AnimalScript("FastInverseSquareRoot", "Max Kaiser & David Langsam", width, height);
		language = Language.createInstance(AlgoAnimBackend.ANIMALSCRIPT, "FastInverseSquareRoot", "Max Kaiser & David Langsam", width, height);
		language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		//language = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "FastInverseSquareRoot", "Max Kaiser & David Langsam", width, height);
		language.setStepMode(true); //set Step mode to "Step by Step"
		animal_variables = language.newVariables();
		//javax.swing.JOptionPane.showConfirmDialog(null, "init");
	}


	@SuppressWarnings("serial")
  @Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

	    //System.out.println("Start...");
	    
	    ///############################################################################################################################ Algorithmus variables ##########
	    Double temp_x = primitives.containsKey("x")? (Double) primitives.get("x") : 32.45F;
	    String temp_magicnumber = primitives.containsKey("magicnumber")? (String) primitives.get("magicnumber") : "0x5F3759DF";
	    Boolean askquestions = primitives.containsKey("questions")? (boolean) primitives.get("questions") : true;
	    
	    Float x = temp_x.floatValue();
	    Float threehalves = 1.5F;
		Float half_x = x * 0.5F; 
		Integer magicnumber = Integer.decode(temp_magicnumber);
	    Integer i_bits = Float.floatToIntBits(x);
	    	    i_bits = magicnumber - ( i_bits >> 1);
	    Float y_usingAlgo = Float.intBitsToFloat(i_bits);
	    Float prev_y;
	    Float y_usingMath = (float) (1 / Math.sqrt(x));
	    
	    ///############################################################################################################################ Lists with the Primitives ##########
	    LinkedList<Primitive> allwaysShown   = new LinkedList<Primitive>();
	    LinkedList<Primitive> deadPrimitives = new LinkedList<Primitive>();
	    Text text_x;
	    Text textMagicnumber;
	    Text text_currentY;
	    
	    ///############################################################################################################################ Global Properties ##########
	    //General Text
	    Font normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	    Font boldFont   = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	    TextProperties textProps = new TextProperties();
	    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, normalFont);
	    //Important Text
	    TextProperties textImportantProps = new TextProperties();
	    textImportantProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
	    textImportantProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
	    //Intro Title Text
	    TextProperties titleIntroProps = new TextProperties();
	    titleIntroProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
	    //Intro Text
	    TextProperties textIntroProps = new TextProperties();
	    textIntroProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
	    // GUI Title Text
	    TextProperties titleProps = new TextProperties();
	    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
		//Box around the GUI Title
	    RectProperties rectProps = new RectProperties();
	    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2); //Legt das Layer fest auf dem es angezeigt werden soll 1 = oberstes inf = unterstes
	    //for the Source Code
	    SourceCodeProperties codeProps = (SourceCodeProperties) props.getPropertiesByName("SourceCode Properties");
	    //SourceCodeProperties codeProps = new SourceCodeProperties(); 
	    //codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
	    //codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
	    //binary representation in the array
	    ArrayProperties binaryProp = (ArrayProperties) props.getPropertiesByName("Array Properties");
	    //binaryProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    //binaryProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
	    //binaryProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.ORANGE);
	    //binaryProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 8));
	    // Arrow
	    RectProperties rectArrow = (RectProperties) props.getPropertiesByName("Arrow Properties");
	    //rectArrow.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    //rectArrow.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(176,176,176));
	    //rectArrow.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 69);
	    
	    ///############################################################################################################################ Genrerate Primitives ##########
	    
		// generate Headline text
	    TextProperties headlineProp = new TextProperties();
	    headlineProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
	    headlineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
	    Text textHeadlineTitle = language.newText(new Coordinates(250, 10), "Fast Inverse Square Root","header", null, headlineProp);
	    allwaysShown.add(textHeadlineTitle);
	    
	    // generate Headline box
	    RectProperties boxProps = new RectProperties();
	    boxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    boxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
	    boxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    Rect rectHeadlineBox = language.newRect(new Coordinates(240, 10), new Coordinates(500, 35), "hRect", null, boxProps);
	    allwaysShown.add(rectHeadlineBox);
	    
	    ///############################################################################################################################ General Introduction ##########
	    
	    language.nextStep("Introduction");
	    deadPrimitives.add(language.newText(new Coordinates(200, 50),T("intro.titleA"), null, null, titleIntroProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(50, 80),T("intro.text1") ,null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(50, 100),T("intro.text2") ,null, null, textIntroProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(150, 130),T("intro.titleB"), null, null, titleIntroProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(50, 160),T("intro.text3") ,null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(50, 180),T("intro.text4") ,null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(50, 200),T("intro.text5") ,null, null, textIntroProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(250, 230),T("intro.titleC"), null, null, titleIntroProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(50, 260),T("intro.text6") ,null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(50, 280),T("intro.text7") ,null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(50, 300),T("intro.text8") ,null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(50, 320),T("intro.text9") ,null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(50, 340),T("intro.text10") ,null, null, textIntroProps));
	    
	    if(askquestions)
	    {
		    language.nextStep("Question 1");
		    MultipleSelectionQuestionModel questionText = new MultipleSelectionQuestionModel("Question 1");
		    questionText.setPrompt(T("question.text1"));
		    questionText.addAnswer(T("question.answ1.1"), 1, T("question.feed1.1"));
		    questionText.addAnswer(T("question.answ1.2"), 1, T("question.feed1.2"));
		    questionText.addAnswer(T("question.answ1.3"), 1, T("question.feed1.3"));
		    questionText.addAnswer(T("question.answ1.4"), 1, T("question.feed1.4"));
		    language.addMSQuestion(questionText);
		    for(int i = 0; i < deadPrimitives.size(); i++)
		    	deadPrimitives.get(i).hide();
	    }
	    else
	    	language.nextStep("GUI Introduction");
	    
	    ///############################################################################################################################ GUI Introduction ##########
	    textHeadlineTitle.moveBy(null, 20, 0, null, null);
	    rectHeadlineBox.moveBy(null, 20, 0, null, null);
	    
	    
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    allwaysShown.add(language.newText(new Coordinates(381 + xTextOffset, 290),T("gui.intro.description"),null, null, titleProps));
	    allwaysShown.add(language.newRect(new Coordinates(380 + xTextOffset, 290), new Coordinates(480 + xTextOffset, 310), "hRect", null, rectProps));
	    deadPrimitives.add(language.newText(new Coordinates(280 + xTextOffset, 310 + O(true)),T("gui.intro.descriotion.text"),null, null, textProps));
	    
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(280 + xTextOffset, 310 + O()),T("gui.intro.text1"),null, null, textProps));
	    allwaysShown.add(language.newText(new Coordinates(20, 50),T("gui.intro.title1"),null, null, titleProps));
	    allwaysShown.add(language.newRect(new Coordinates(15, 50), new Coordinates(110, 70), "hRect", null, rectProps));
	    
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(280 + xTextOffset, 310 + O()),T("gui.intro.text2"),null, null, textProps));
	    allwaysShown.add(language.newText(new Coordinates(340, 50),T("gui.intro.title2"),null, null, titleProps));
	    allwaysShown.add(language.newRect(new Coordinates(330, 50), new Coordinates(425, 70), "hRect", null, rectProps));
	    
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(280 + xTextOffset, 310 + O()),T("gui.intro.text3"),null, null, textProps));
	    allwaysShown.add(language.newText(new Coordinates(680, 50),T("gui.intro.title3"),null, null, titleProps));
	    allwaysShown.add(language.newRect(new Coordinates(670, 50), new Coordinates(790, 70), "hRect", null, rectProps));
	    
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(280 + xTextOffset, 310 + P()),T("gui.intro.tip"),null, null, textImportantProps));
	    
	    language.nextStep("Show code");
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    SourceCode codeText = language.newSourceCode(new Coordinates(20, 70), "code", null, codeProps);
	    codeText.addCodeLine("public static Float FastInvSqrt(float x = " + x.toString() + ")", null, 0, null);
	    codeText.addCodeLine("{", null, 0, null);
	    codeText.addCodeLine("float threehalves = 1.5F;", null, 1, null);
	    codeText.addCodeLine("float half_x = x * 0.5F;", null, 1, null);
	    codeText.addCodeLine("int magicnumber = 0x"+ Integer.toHexString(magicnumber).toUpperCase() + ";", null, 1, null);
	    codeText.addCodeLine("", null, 1, null);
	    codeText.addCodeLine("int i  = Float.floatToIntBits(x);", null, 1, null);
	    codeText.addCodeLine("i  = magicnumber - ( i >> 1);", null, 1, null);
	    codeText.addCodeLine("float y  = Float.intBitsToFloat(i);", null, 1, null);
	    codeText.addCodeLine("", null, 1, null);
	    codeText.addCodeLine("float prev_y;", null, 1, null);
	    codeText.addCodeLine("do {", null, 1, null);
	    codeText.addCodeLine("prev_y = y;", null, 2, null);
	    codeText.addCodeLine("y  = y * ( threehalves - ( half_x * y * y));", null, 2, null);
	    codeText.addCodeLine("} while(Math.abs(prev_y - y) > 0);", null, 1, null);
	    codeText.addCodeLine("return y;", null, 1, null);
	    codeText.addCodeLine("}", null, 0, null);
	    
	        
	    language.nextStep("Show Variables");
	    text_x = language.newText(new Coordinates(300, 90),"x = ",null, null, textProps);
	    allwaysShown.add(text_x);
	    textMagicnumber = language.newText(new Coordinates(300, 110),"magic number = 0x",null, null, textProps);
	    allwaysShown.add(textMagicnumber);
	    allwaysShown.add(language.newText(new Coordinates(300, 130),"f(x) = 1 / sqrt( x )",null, null, textProps));
	    text_currentY = language.newText(new Coordinates(300, 205),"current  y  =", null, null, textProps);
	    allwaysShown.add(text_currentY);
	    allwaysShown.add(language.newText(new Coordinates(300, 170),"f(x) -> Java.Math = " + y_usingMath.toString(), null, null, textProps));
	    animal_variables.declare("double", "x");
	    animal_variables.declare("double", "y");
	    animal_variables.declare("double", "oldy");
	    animal_variables.declare("String", "magicnumber");
	    animal_variables.declare("int", "IterationCounter");
	    
	    language.nextStep("Show binary representation");
	    int[] binaryarray = new int[32];
	    Arrays.fill(binaryarray, 0);
	    allwaysShown.add(language.newText(new Coordinates(670, 105),T("array.title"),null, null, textProps));
	    IntArray intArray1 = language.newIntArray(new Coordinates(500, 120), binaryarray, "array", null, binaryProp);
	    
	    ///############################################################################################################################ Run the Algorithmus ##########
	    language.nextStep("Run Algorithmus");
	    codeText.highlight(0);
	    text_x.setText("x = " + x.toString(), null, null); ///********* ALGORITHMUS **********
	    animal_variables.set("x", x.toString());
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("call.text1"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("call.text2"),null, null, textProps));
	    
	    
	    language.nextStep();
	    codeText.unhighlight(0);
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    codeText.highlight(2);
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("threehalves.text1"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("threehalves.text2"),null, null, textProps));
	    
	    language.nextStep();
	    codeText.unhighlight(2);
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    codeText.highlight(3);
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("half_x.text1"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("half_x.text2"),null, null, textProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("half_x.text3"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("half_x.text4"),null, null, textProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("half_x.text5"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("half_x.text6"),null, null, textProps));
		
	    language.nextStep();
	    codeText.unhighlight(3);
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    codeText.highlight(4);
	    textMagicnumber.setText("magic number = 0x" + Integer.toHexString(magicnumber).toUpperCase(), null, null); ///********* ALGORITHMUS **********  
	    animal_variables.set("magicnumber", "0x" + Integer.toHexString(magicnumber).toUpperCase());
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("magicnumber.text1"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("magicnumber.text2"),null, null, textProps));
	    
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("magicnumber.text3"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("magicnumber.text4"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("magicnumber.text5"),null, null, textProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("magicnumber.text6"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("magicnumber.text7"),null, null, textProps));
	    
	    
	    language.nextStep();
	    codeText.unhighlight(4);
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    codeText.highlight(6);
		Rect arrow1 = language.newRect(new Coordinates(185,182), new Coordinates(500, 190), null, null, rectArrow);
		arrow1.rotate(new Coordinates(185, 182), 10, null, null);
		deadPrimitives.add(arrow1);
		FastInverseHelper.highlightArray(intArray1);
		FastInverseHelper.applyFloatToAnimalBitIntArray(intArray1, x);
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("floatasbits.text1"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("floatasbits.text2"),null, null, textProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("floatasbits.text3"),null, null, textProps));
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("floatasbits.text4"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("floatasbits.text5"),null, null, textProps));
	    
	    language.nextStep("Question 2");
	    codeText.unhighlight(6);
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    codeText.highlight(7);
	    IntArray intArray2 = language.newIntArray(new Coordinates(500, 200), binaryarray, "array", null, binaryProp);
	    FastInverseHelper.highlightArray(intArray2);
	    
	    
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("magicshift.text1"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("magicshift.text2"),null, null, textProps));
	    if(askquestions)
	    {
		    MultipleChoiceQuestionModel questionShift = new MultipleChoiceQuestionModel("Question 2");
		    questionShift.setPrompt(T("question.text2"));
		    questionShift.addAnswer(T("question.answ2.1"), 2, T("question.feed2.1"));
		    questionShift.addAnswer(T("question.answ2.2"), 3, T("question.feed2.2"));
		    language.addMCQuestion(questionShift);
	    }
	    language.nextStep();
	    Text textshift = language.newText(new Coordinates(670, 180), "SHIFT", "", null, titleProps);
	    deadPrimitives.add(textshift);
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("magicshift.text3"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("magicshift.text4"),null, null, textProps));
	    FastInverseHelper.shiftAnimalBitIntArrayByOtherArray(intArray1, intArray2);
	    language.nextStep();
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("magicshift.text5"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("magicshift.text6"),null, null, textProps));
	    textshift.setText("USE magic number", null, null);
	    FastInverseHelper.applyFloatToAnimalBitIntArray(intArray2, y_usingAlgo);
	    
	    
	    language.nextStep();
	    codeText.unhighlight(7);
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    codeText.highlight(8);
	    animal_variables.set("y", y_usingAlgo.toString());
	    text_currentY.setText("current  y  =  " + y_usingAlgo.toString() + "  =  ", null, null);
	    text_currentY.changeColor(null, new Color(244, 36, 210), null, null);
	    text_currentY.setFont(boldFont, null, null);
	    Rect arrow2 = language.newRect(new Coordinates(200,215), new Coordinates(495, 225), null, null, rectArrow);
		arrow2.rotate(new Coordinates(185, 182), 2, null, null);
		deadPrimitives.add(arrow2);
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("bitsasfloat.text"),null, null, textProps));
	    
	    Boolean FirstRun = true;
	    Integer IterationCouter = 1;
	    do
	    {
	    	animal_variables.set("IterationCounter", IterationCouter.toString());
		    language.nextStep("Loop Iteration #" + IterationCouter.toString());
		    text_currentY.changeColor(null, new Color(0, 0, 0), null, null);
		    text_currentY.setFont(normalFont, null, null);
		    codeText.unhighlight(8);
		    codeText.unhighlight(14);
		    for(int i = 0; i < deadPrimitives.size(); i++)
		    	deadPrimitives.get(i).hide();
		    if(FirstRun)
		    	codeText.highlight(10);
		    codeText.highlight(11);
		    codeText.highlight(12);
	    	prev_y = y_usingAlgo; ///********* ALGORITHMUS **********
	    	animal_variables.set("oldy", y_usingAlgo.toString());
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("lasty.text1"),null, null, textProps));
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("lasty.text2"),null, null, textProps));
		    language.nextStep();
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("lasty.text3"),null, null, textProps));
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("lasty.text4"),null, null, textProps));
		    
		    language.nextStep();
		    codeText.unhighlight(10);
		    codeText.unhighlight(11);
		    codeText.unhighlight(12);
		    for(int i = 0; i < deadPrimitives.size(); i++)
		    	deadPrimitives.get(i).hide();
		    codeText.highlight(13);
		    y_usingAlgo  = y_usingAlgo * ( threehalves - ( half_x * y_usingAlgo * y_usingAlgo)); ///********* ALGORITHMUS **********
		    text_currentY.setText("current  y  =  " + y_usingAlgo.toString() + "  =  ", null, null);
		    if (!y_usingAlgo.equals(prev_y)) {
		    	text_currentY.changeColor(null, new Color(244, 36, 210), null, null);
		    	text_currentY.setFont(boldFont, null, null);
		    }
		    animal_variables.set("y", y_usingAlgo.toString());
		    FastInverseHelper.applyFloatToAnimalBitIntArray(intArray2, y_usingAlgo);
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("calculation.text1"),null, null, textProps));
		    language.nextStep();
		    text_currentY.changeColor(null, new Color(0, 0, 0), null, null);
		    text_currentY.setFont(normalFont, null, null);
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + P()),T("calculation.text2"),null, null, textProps));
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("calculation.text3"),null, null, textProps));
		    
		    if (IterationCouter == 4 && askquestions)
		    	language.nextStep("Question 3");
		    else
		    	language.nextStep();
		    codeText.unhighlight(13);
		    for(int i = 0; i < deadPrimitives.size(); i++)
		    	deadPrimitives.get(i).hide();
		    codeText.highlight(14);
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("check.text1"),null, null, textProps));
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("check.text2"),null, null, textProps));
		    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("check.text3"),null, null, textProps));
		    
		    if(IterationCouter == 4 && askquestions) 
		    {
			    TrueFalseQuestionModel questionFinish = new TrueFalseQuestionModel("Question 3");
			    questionFinish.setPointsPossible(1);
			    questionFinish.setPrompt(T("question.text3"));
			    questionFinish.setCorrectAnswer(false);
			    language.addTFQuestion(questionFinish);
		    }
		    
		    FirstRun = false;
		    IterationCouter++;
	    } while(Math.abs(prev_y - y_usingAlgo) > 0);
	    IterationCouter--;
	    
	    language.nextStep();
	    codeText.unhighlight(14);
	    for(int i = 0; i < deadPrimitives.size(); i++)
	    	deadPrimitives.get(i).hide();
	    codeText.highlight(15);
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O(true)),T("finish.text1"),null, null, textProps));
	    deadPrimitives.add(language.newText(new Coordinates(220 + xTextOffset, 310 + O()),T("finish.text2"),null, null, textProps));
	    
	    language.nextStep("Finish");
	    language.hideAllPrimitivesExcept(new LinkedList<Primitive>() {{ 
																    	add(textHeadlineTitle); 
																    	add(rectHeadlineBox); }} );
	    //30 oder 20
	    allwaysShown.add(language.newText(new Coordinates(350, 50),T("final.title.text"),null, null, titleProps));
	    allwaysShown.add(language.newRect(new Coordinates(320, 50), new Coordinates(415, 70), "hRect", null, rectProps));
	    
	    deadPrimitives.add(language.newText(new Coordinates(50, 80),T("final.text") + " " + IterationCouter.toString() + " " + T("final.text2") ,null, null, textIntroProps));
	    
	    deadPrimitives.add(language.newText(new Coordinates(50, 110),T("final.text2b"), null, null, titleProps));
	    deadPrimitives.add(language.newText(new Coordinates(70, 130),T("final.text2c") + " " + x.toString(), null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(70, 150),T("final.text2d") + " 0x"+ Integer.toHexString(magicnumber).toUpperCase(), null, null, textIntroProps));
	    
	    deadPrimitives.add(language.newText(new Coordinates(50, 180),T("final.text2e"), null, null, titleProps));
	    deadPrimitives.add(language.newText(new Coordinates(70, 200),T("final.text3") + " " + y_usingAlgo.toString(), null, null, textIntroProps));
	    deadPrimitives.add(language.newText(new Coordinates(70, 220),T("final.text4") + " " + y_usingMath.toString(), null, null, textIntroProps));
	    
	    language.finalizeGeneration();
	    return language.toString();
	} 
	
	
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException { 
		Double temp_x = primitives.containsKey("x")? (Double) primitives.get("x") : 32.45F;
	    String temp_magicnumber = primitives.containsKey("magicnumber")? (String) primitives.get("magicnumber") : "0x5F3759DF";
	    
	    if(temp_x < Float.MIN_VALUE) {
	    	JOptionPane.showMessageDialog(null, T("validate.floattosmall"), "Error Float Input", JOptionPane.OK_OPTION);
	    	return false;
	    }
	    if(temp_x > Float.MAX_VALUE) {
	    	JOptionPane.showMessageDialog(null, T("validate.floattobig"), "Error Float Input", JOptionPane.OK_OPTION);
	    	return false;
	    }
	    
	    @SuppressWarnings("unused")
      Integer magicnumber;
	    try {
	    	magicnumber = Integer.decode(temp_magicnumber);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, T("validate.magicnumber"), "Error HEX Input", JOptionPane.OK_OPTION);
			return false;
		}
		
		
		
		return true;
	}
	
	@Override
	public String getCodeExample() { //TODO Auslagern f�r nicht redundaten / konsistenten CodeExample
		return 
		"public static Float FastInvSqrt(float x)\n" +
		"{\n" +
		"    float threehalves = 1.5F;\n" +
		"    float half_x = x * 0.5F;\n"  +
		"    int magicnumber = 0x5F3759DF;\n" +
		"\n" +
		"    int i  = Float.floatToIntBits(x);\n" +
		"    i  = magicnumber - ( i >> 1);\n" +
		"    float y  = Float.intBitsToFloat(i);\n" +
		"\n"	 +
		"    float prev_y;\n" +
		"    do {\n" +
		"        prev_y = y;\n" +
		"        y  = y * ( threehalves - ( half_x * y * y));\n" +
		"    } while(Math.abs(prev_y - y) > 0);\n" +
		"    return y;\n" +
		"}\n";
	}

	@Override
	public String getDescription() { ////TODO Mehrsprachig
		return tl.translateMessage("description");
	}
	
	
	@Override
	public Locale getContentLocale() {
		return locale;
	}
	
	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	
	@Override
	public String getName() {
		return "Fast Inverse Square Root";
	}
	
	@Override
	public String getAlgorithmName() {
		return "Fast Inverse Square Root";
	}


	@Override
	public String getAnimationAuthor() {
		return "Max Kaiser & David Langsam";
	}


	@Override
	public String getFileExtension() {
		return "asu";
	}


	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}
	
	private String T(String key) {
		return tl.translateMessage(key);
	}
	
	private int O() {
		currentOffset = currentOffset + 17;
		return currentOffset;
	}
	private int O(boolean b) {
		currentOffset = 5;
		return currentOffset;
	}
	private int P() {
		currentOffset = currentOffset + 27;
		return currentOffset;
	}

}
