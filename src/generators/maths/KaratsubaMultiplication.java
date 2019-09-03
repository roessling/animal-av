package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class KaratsubaMultiplication implements Generator {
    private Language lang;
    private int num2;
    private int num1;
    private Color titleBackgroundColor;
    private Color titleBorderColor;
    private Color titleFontColor;
    private Color srcHighlightColor;
    private Color srcFontColor;
    private Color arrayBorderColor;
    private Color arrayCellHighlightColor;
    private Color arrayFontColor;
    private Color falseColor;
    private Color trueColor;
    private Color labelFontColor;
    
    private Text num1_label, num2_label, m_label, high1_label, high2_label, low1_label, low2_label, bool1_true_label, bool1_false_label, bool2_true_label, bool2_false_label;
    private Text simple_mult, rec_title, shiftValue, multValue, recValue, shiftLabel, multLabel, recLabel;
   	private IntArray sgl_num1_arr, sgl_num2_arr, m_arr, dbl_num1_arr, dbl_num2_arr, high1_arr, high2_arr, low1_arr, low2_arr;
   	private SourceCode source;
   	private int currentSrcRow = 0;
   	private int rec_depth = -1;
   	private ArrayList<int[]> recDepthData = new ArrayList<int[]>();
   	private ArrayList<Text> recRows = new ArrayList<Text>();
   	private int shiftCounter, multCounter, recCounter = 0;

    public void init(){
        lang = new AnimalScript("Karatsuba Multiplication [EN]", "Sebastian Sztwiertnia", 800, 600);
        lang.setStepMode(true);
    }

    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        num2 = (Integer)primitives.get("num2");
        num1 = (Integer)primitives.get("num1");
        titleBackgroundColor = (Color)primitives.get("titleBackgroundColor");
        titleBorderColor = (Color)primitives.get("titleBorderColor");
        titleFontColor = (Color)primitives.get("titleFontColor");
        srcHighlightColor = (Color)primitives.get("srcHighlightColor");
        srcFontColor = (Color)primitives.get("srcFontColor");
        arrayBorderColor = (Color)primitives.get("arrayBorderColor"); 
        arrayCellHighlightColor = (Color)primitives.get("arrayCellHighlightColor");
        arrayFontColor = (Color)primitives.get("arrayFontColor");
        falseColor = (Color)primitives.get("falseColor");
        trueColor = (Color)primitives.get("trueColor");
        labelFontColor = (Color)primitives.get("labelFontColor");
        karatsubaMain(num1, num2);
        
        return lang.toString();
    }

    public String getName() {
        return "Karatsuba Multiplication [EN]";
    }

    public String getAlgorithmName() {
        return "Karatsuba";
    }

    public String getAnimationAuthor() {
        return "Sebastian Sztwiertnia";
    }

    public String getDescription(){
        return "<html>The Karatsuba algorithm is a fast multiplication algorithm. It was invented by Anatolii Alexeevitch Karatsuba in 1960 and published in 1962."
 +"\n"
 +"It reduces the multiplication of two n-digit numbers to at most ~ 3 n<sup>1.585</sup> single-digit multiplications in general."
 +"\n"
 +"It is therefore faster than the classical algorithm, which requires n<sup>2</sup> single-digit products."
 +"\n"
 +"For small values of n, however, the extra shift and add operations may make it run slower than the longhand method. "
 +"\n"
 +"The point of positive return depends on the computer platform and context. As a rule of thumb, Karatsuba is usually faster when the multiplicands are longer than 320-640 bits."
 +"\n"
 +"The Toom-Cook algorithm is a faster generalization of this algorithm.</html>";
    }

    public String getCodeExample(){
        return "procedure karatsuba( num1, num2 )"
 +"\n"
 +"     if ( ( num1 < 10 ) or ( num2 < 10 ) )"
 +"\n"
 +"          return num1 * num2"
 +"\n"
 +"     m = even max( length( num1 ), length( num2 ) )"
 +"\n"
 +"     high1, high2 = higher half of num1, num2"
 +"\n"
 +"     low1, low2 = lower half of num1, num2"
 +"\n"
 +"     z0 = karatsuba( low1, low2 )"
 +"\n"
 +"     z2 = karatsuba( high1, high2 )"
 +"\n"
 +"     z1 = karatsuba( ( high1 + low1 ), ( high2 + low2 ) ) - z2 - z0"
 +"\n"
 +"     return ( z2 * 10 ^ ( m ) ) + ( z1 * 10 ^ ( m / 2 ) ) + z0";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.US;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public void clearBoard() {
    	//labels
    	num1_label.hide();
    	num2_label.hide();
    	simple_mult.hide();
    	m_label.hide();
    	high1_label.hide();
    	high2_label.hide();
    	low1_label.hide();
    	low2_label.hide();
    	
    	//arrays
    	dbl_num1_arr.hide();
    	dbl_num2_arr.hide();
    	sgl_num1_arr.hide();
    	sgl_num2_arr.hide();
    	m_arr.hide();
    	high1_arr.hide();
    	high2_arr.hide();
    	low1_arr.hide();
    	low2_arr.hide();
    }
    
    public void hideBools() {
    	bool1_true_label.hide(); 
    	bool1_false_label.hide(); 
    	bool2_true_label.hide();
    	bool2_false_label.hide();
    }
    
    public void drawHeader() {
    	//headline
    	Coordinates headerTextPos =  new Coordinates(20, 30);
    	TextProperties textProps = new TextProperties();
    	Font sans =  new Font("SansSerif", Font.BOLD, 24);
    	textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, titleFontColor);
    	Text header = lang.newText(headerTextPos, "Karatsuba Multiplication", "header", null, textProps);
    	
    	//rectangle
    	Offset upLeftOffset = new Offset(-5, -5, header, "NW");
    	Offset lowRightOffset = new Offset(5, 5, header, "SE");
    	RectProperties rectProps = new RectProperties();
    	rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    	rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, titleBackgroundColor);
    	rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, titleBorderColor);
    	lang.newRect(upLeftOffset, lowRightOffset, "header_rect", null, rectProps);
    }
        
    public Text[] printIntro() {
    	//define properties and data structures
    	TextProperties textProps = new TextProperties();
    	Font sans =  new Font("SansSerif", Font.PLAIN, 15);
    	textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);
    	String[] text = new String[5];
    	Text[] textObjects = new Text[5];
    	
    	//fill array with intro text
    	text[0] = "The Karatsuba algorithm is a fast multiplication algorithm. It was invented by Anatolii Alexeevitch Karatsuba";
    	text[1] = "in 1960 and published in 1962. It reduces the multiplication of two n-digit numbers to at most";
    	text[2] = "~ 3 n^1.585 single-digit multiplications in general. It is therefore faster than the classical algorithm,";
    	text[3] = "which requires n^2 single-digit products. The Karatsuba algorithm was the first multiplication algorithm";
    	text[4] = "asymptotically faster than the quadratic grade school algorithm.";
        	
    	Coordinates introPos =  new Coordinates(20, 72);
    	textObjects[0] = lang.newText(introPos, text[0], "intro1", null, textProps);
    	
    	//construct text objects
    	for (int i = 1; i < text.length; i++ ) {
			Offset introPosOffset = new Offset(0, 0, textObjects[ i - 1 ] , "SW");
			textObjects[ i ] = lang.newText(introPosOffset, text[ i ], "intro" + ( i + 1 ), null, textProps);
    	}
    	return textObjects;
    }
    
    public Text[] printNote() {
    	//define properties and data structures
    	TextProperties textProps = new TextProperties();
    	Font sans =  new Font("SansSerif", Font.PLAIN, 12);
    	textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);
    	String[] text = new String[8];
    	Text[] textObjects = new Text[8];
    	
    	//fill array with intro text
    	text[0] = "Note:";
    	text[1] = "This particular implemenation of karatsubas algorithm operates with a Base of B = 10, ";
    	text[2] = "which makes a lot of steps, especially the shifting operations, more understandable.";
    	text[3] = "The method 'length' used in the 4th row calculates the string length of a certain number.";
    	text[4] = "In the same row 'm' is the max string length of num1 and num2, and must always be even,";
    	text[5] = "which is achieved by adding leading zeros. Further, row 5 and 6 stand for Base-10-Shifts with m/2.";
    	text[6] = "Additionally we calculate z2 before z1, simply because in this code version the z1 calculation contains z2 as well.";
    	text[7] = "The purpose of this small adjustment is to keep the return statement calculation as simple and understandable as possible.";
        	
    	Offset notePos = new Offset(5, 50, source, "SW");
    	textObjects[0] = lang.newText(notePos, text[0], "intro1", null, textProps);
    	
    	//construct text objects
    	for (int i = 1; i < text.length; i++ ) {
			Offset notePosOffset = new Offset(0, 0, textObjects[ i - 1 ] , "SW");
			textObjects[ i ] = lang.newText(notePosOffset, text[ i ], "intro" + ( i + 1 ), null, textProps);
    	}
    	return textObjects;
    }
    
    public void printOutro() {
    	//define properties and data structures
    	TextProperties textProps = new TextProperties();
    	Font sans =  new Font("SansSerif", Font.PLAIN, 15);
    	textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);
    	String[] text = new String[8];
    	Text[] textObjects = new Text[8];
    	
    	TextProperties statProps = new TextProperties();
    	Font sansBold =  new Font("SansSerif", Font.BOLD, 14);
    	statProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sansBold);
    	statProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);
    	String[] stats = new String[4];
    	Text[] statObjects = new Text[4];
    	
    	//fill array with intro text
    	text[0] = "It follows that, for sufficiently large numbers, Karatsuba's algorithm will";
    	text[1] = "perform fewer shifts and single-digit additions than longhand multiplication, even though its basic step";
    	text[2] = "uses more additions and shifts than the straightforward formula. For small values of n, however,";
    	text[3] = "the extra shift and add operations may make it run slower than the longhand method.";
    	text[4] = "The point of positive return depends on the computer platform and context.";
    	text[5] = "As a rule of thumb, Karatsuba is usually faster when the multiplicands are longer than 320-640 bits.";
    	text[6] = "Additionally a potential advantage of Karatsuba's algorithm is it permits a simple means of parallelisation:";
    	text[7] = "the three multiplications of each 'round' can be run in parallel on separate cores.";
    	
    	stats[0] = "Algorithm performance stats for your multiplicands:";
    	stats[1] = "shifting operations done: " + shiftCounter;
    	stats[2] = "multiplications calculated: " + multCounter;
    	stats[3] = "recursion calls: " + recCounter;
        	
    	Coordinates outroPos =  new Coordinates(20, 72);
    	textObjects[0] = lang.newText(outroPos, text[0], "intro1", null, textProps);
    	
    	//construct text objects
    	for (int i = 1; i < text.length; i++ ) {
			Offset outroPosOffset = new Offset(0, 0, textObjects[ i - 1 ] , "SW");
			textObjects[ i ] = lang.newText(outroPosOffset, text[ i ], "outro" + ( i + 1 ), null, textProps);
    	}
    	
    	Offset statPos =  new Offset(0, 40, "outro" + text.length, "SW");
    	statObjects[0] = lang.newText(statPos, stats[0], "stat1", null, statProps);
    	
    	//construct stat objects
    	for (int i = 1; i < stats.length; i++ ) {
			Offset statPosOffset = new Offset(0, 0, statObjects[ i - 1 ] , "SW");
			statObjects[ i ] = lang.newText(statPosOffset, stats[ i ], "stat" + ( i + 1 ), null, textProps);
    	}
    }
    
    public void generateCounter(){
		TextProperties textProps = new TextProperties();
		Font sans = new Font("SansSerif", Font.PLAIN, 15);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);

		Coordinates firstRow = new Coordinates(550, 15);
		shiftLabel = lang.newText(firstRow, "shifts: ", "shiftsLabel", null, textProps);

		Offset secondRowOffset = new Offset(0, 0, shiftLabel, "SW");
		multLabel = lang.newText(secondRowOffset, "multiplications: ", "multLabel", null, textProps);

		Offset thirdRowOffset = new Offset(0, 0, multLabel, "SW");
		recLabel = lang.newText(thirdRowOffset, "recursion calls : ", "recLabel", null, textProps);

		TextProperties ValueTextProps = new TextProperties();
		Font boldSans = new Font("SansSerif", Font.BOLD, 15);
		ValueTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, boldSans);
		ValueTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);

		Offset toFirstLabel = new Offset(120, 0, shiftLabel, "NW");
		shiftValue = lang.newText(toFirstLabel, "0", "shiftValue", null, ValueTextProps);

		Offset toSecondLabel = new Offset(120, 0, multLabel, "NW");
		multValue = lang.newText(toSecondLabel, "0", "multValue", null, ValueTextProps);

		Offset toThirdLabel = new Offset(120, 0, recLabel, "NW");
		recValue = lang.newText(toThirdLabel, "0", "recValue", null, ValueTextProps);
	}
    
    public void hideCounter(){
    	shiftLabel.hide(); multLabel.hide(); recLabel.hide();
    	shiftValue.hide(); multValue.hide(); recValue.hide();
    }
    
    public void updateCounter(){
    	shiftValue.setText(Integer.toString(shiftCounter), null, null);
    	multValue.setText(Integer.toString(multCounter), null, null);
    	recValue.setText(Integer.toString(recCounter), null, null);
    }
    
    public void hideTextObjects( Text[] textObjects) {
    	int length = textObjects.length;
    	for (int i = 0; i < length; i++ ){
    		textObjects[i].hide();
    	}
    }
    
    public Text generateRecRow(boolean incOffset, String text){
       	TextProperties labelProps = new TextProperties();
    	Font sans =  new Font("SansSerif", Font.PLAIN, 15);
    	labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);
    	Text lastRow = getLastRow();
    	String name = "rec" + rec_depth;
    	
    	int xOffset;
    	if (incOffset) {
    		xOffset = 15;
    	} else {
    		xOffset = 0;
    	}
    	Text row = lang.newText(new Offset(xOffset, 5, lastRow, "SW"), text, name, null, labelProps);
    	recRows.add(row);
    	return row;
    }
    
    public Text getLastRow() {
    	int size = recRows.size();
    	Text lastRow = recRows.get(size - 1);
    	return lastRow;
    }
    
    public Text getUberRow() {
    	int size = recRows.size();
    	Text uberRow = recRows.get(size - 4);
    	return uberRow;
    }
    
    public void hideRecRemains() {
    	int size = recRows.size();
    	Text three = recRows.get(size - 1);
    	Text two = recRows.get(size - 2);
    	Text one = recRows.get(size - 3);
    	three.hide();
    	two.hide();
    	one.hide();
    	recRows.remove(size - 1);
    	recRows.remove(size - 2);
    	recRows.remove(size - 3);
    }
    
    public Text generateFirstRow(String text){

    	TextProperties labelProps = new TextProperties();
    	Font sans =  new Font("SansSerif", Font.PLAIN, 15);
    	labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);
    	
    	Text row = lang.newText(new Offset(0, 70, source, "SW"), text, "rec0", null, labelProps);
    	
    	recRows.add(row);
    	return row;
    }
    
    public void printSrc() {
    	//define properties
    	SourceCodeProperties sourceProps = new SourceCodeProperties();    
    	Font sans =  new Font("SansSerif", Font.PLAIN, 15);
    	sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, srcHighlightColor);   
    	sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, srcFontColor);
    	sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	
    	//create source code block
    	source = lang.newSourceCode(new Coordinates(20, 100), "src", null, sourceProps);
    	    
    	source.addCodeLine("procedure karatsuba( num1, num2 )", null, 0, null);
    	source.addCodeLine("if ( ( num1 < 10 ) or ( num2 < 10 ) )", null, 1, null); 
    	source.addCodeLine("return num1 * num2", null, 2, null); 
    	source.addCodeLine("m = even max( length( num1 ), length( num2 ) )", null, 1, null);
    	source.addCodeLine("high1, high2 = higher half of num1, num2", null, 1, null);
    	source.addCodeLine("low1, low2 = lower half of num1, num2", null, 1, null);
    	source.addCodeLine("z0 = karatsuba( low1, low2 )", null, 1, null);
    	source.addCodeLine("z2 = karatsuba( high1, high2 )", null, 1, null);
    	source.addCodeLine("z1 = karatsuba( ( high1 + low1 ), ( high2 + low2 ) ) - z2 - z0", null, 1, null);
    	source.addCodeLine("return ( z2 * 10 ^ ( m ) ) + ( z1 * 10 ^ ( m / 2 ) ) + z0", null, 1, null);
    }
    
    public void createDynamicObjects(){
    	//define properties, coordinates
    	Coordinates num1LabelPosition = new Coordinates(550, 120);
    	TextProperties labelProps = new TextProperties();
    	Font sans =  new Font("SansSerif", Font.PLAIN, 15);
    	Font sans20 =  new Font("SansSerif", Font.PLAIN, 20);
    	labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);    	
    	labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);
    	
    	TextProperties multProps = new TextProperties();
    	multProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans20);
    	multProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, labelFontColor);
 
    	TextProperties PosProps = new TextProperties();
    	PosProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	PosProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, trueColor);
    	
    	TextProperties NegProps = new TextProperties();
    	NegProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	NegProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, falseColor);
    	
    	int[] sglArray1Content = new int[1];
    	int[] sglArray2Content = new int[1];
    	int[] dblArray1Content = new int[2];
    	int[] dblArray2Content = new int[2];
    	int[] high1Content = new int[1];
    	int[] high2Content = new int[1];
    	int[] low1Content = new int[1];
    	int[] low2Content = new int[1];
    	int[] mContent = new int[1];

    	ArrayProperties arrayProps = new ArrayProperties();
    	arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrayBorderColor);
    	arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, arrayFontColor);
    	arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, arrayCellHighlightColor);
    	arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sans);
    	
    	//create objects
    	num1_label = lang.newText(num1LabelPosition, "num1: ", "num1_label", null, labelProps);
    	num2_label = lang.newText(new Offset(0, 30, num1_label, "SW"), "num2: ", "num2_label", null, labelProps);
    	rec_title = lang.newText(new Offset(0, 30, num1_label, "SW"), "Recursion depth 1", "num2_label", null, multProps);
    	simple_mult = lang.newText(new Offset(0, 80, num2_label, "SW"), "null", "simple_mult", null, multProps);
    	m_label = lang.newText(new Offset(0, 30, num2_label, "SW"), "m: ", "m_label", null, labelProps);
    	sgl_num1_arr = lang.newIntArray(new Offset(10, 0, num1_label, "NE"), sglArray1Content, "sgl_num1_arr", null, arrayProps);
    	sgl_num2_arr = lang.newIntArray(new Offset(10, 0, num2_label, "NE"), sglArray2Content, "sgl_num2_arr", null, arrayProps);
    	m_arr = lang.newIntArray(new Offset(10, 0, m_label, "NE"), mContent, "m_arr", null, arrayProps);
    	dbl_num1_arr = lang.newIntArray(new Offset(10, 0, num1_label, "NE"), dblArray1Content, "dbl_num1_arr", null, arrayProps);
    	dbl_num2_arr = lang.newIntArray(new Offset(10, 0, num2_label, "NE"), dblArray2Content, "dbl_num2_arr", null, arrayProps);
    	bool1_true_label = lang.newText(new Offset(70, 0, sgl_num1_arr, "NE"), "< 10", "bool1true_label", null, PosProps);
    	bool1_false_label = lang.newText(new Offset(70, 0, sgl_num1_arr, "NE"), "> 10", "bool1false_label", null, NegProps);
    	bool2_true_label = lang.newText(new Offset(70, 0, sgl_num2_arr, "NE"), "< 10", "bool2true_label", null, PosProps);
    	bool2_false_label = lang.newText(new Offset(70, 0, sgl_num2_arr, "NE"), "> 10", "bool2false_label", null, NegProps);
    	high1_label = lang.newText(new Offset(30, 0, dbl_num1_arr, "NE"), "high1: ", "high1_label", null, labelProps);
    	high2_label = lang.newText(new Offset(30, 0, dbl_num2_arr, "NE"), "high2: ", "high2_label", null, labelProps);
    	high1_arr = lang.newIntArray(new Offset(10, 0, high1_label, "NE"), high1Content, "high1_arr", null, arrayProps);
    	high2_arr = lang.newIntArray(new Offset(10, 0, high2_label, "NE"), high2Content, "high2_arr", null, arrayProps);
    	low1_label = lang.newText(new Offset(30, 0, high1_arr, "NE"), "low1: ", "low1_label", null, labelProps);
    	low2_label = lang.newText(new Offset(30, 0, high2_arr, "NE"), "low2: ", "low2_label", null, labelProps);
    	low1_arr = lang.newIntArray(new Offset(10, 0, low1_label, "NE"), low1Content, "low1_arr", null, arrayProps);
    	low2_arr = lang.newIntArray(new Offset(10, 0, low2_label, "NE"), low2Content, "low2_arr", null, arrayProps);
    	
    	//hiding
    	num1_label.hide(); sgl_num1_arr.hide(); dbl_num1_arr.hide();
    	num2_label.hide(); sgl_num2_arr.hide(); dbl_num2_arr.hide();
    	simple_mult.hide(); rec_title.hide();
    	m_label.hide(); m_arr.hide();
    	high1_label.hide(); high1_arr.hide();
    	high2_label.hide(); high2_arr.hide();
    	low1_label.hide(); low1_arr.hide();
    	low2_label.hide(); low2_arr.hide();
    	bool1_true_label.hide(); bool1_false_label.hide(); bool2_true_label.hide(); bool2_false_label.hide();	
    }
    
    public void karatsubaMain (int num1, int num2) {
    	Text[] textObjects;
    	drawHeader();
    	lang.nextStep("print intro");
    	textObjects = printIntro();
    	lang.nextStep("hide intro and print src code");
    	hideTextObjects( textObjects );
    	printSrc();
    	createDynamicObjects();
    	lang.nextStep("print note");
    	Text[] noteBlock = printNote();
    	lang.nextStep("hide note and start calculation");
    	hideTextObjects(noteBlock);
    	generateCounter();
    	shiftCounter = 0;
    	multCounter = 0;
    	currentSrcRow = 0;
    	rec_depth = -1;
    	karatsubaAlgo(num1, num2);
    	getLastRow().hide();
    	source.hide();
    	hideCounter();
    	printOutro();
    }
	
    public int karatsubaAlgo (int num1, int num2){
    	rec_depth++;
    	int[] depthArr = new int[3];
    	recDepthData.add(rec_depth, depthArr);
    	
    	String titleString = "Recursion depth: " + rec_depth;
    	rec_title.setText(titleString, null, null);
    	
    	//CLEAR BOARD & SHOW REC DEPTH
    	source.toggleHighlight(currentSrcRow, 0);
    	currentSrcRow = 0;
    	rec_title.show();
    	clearBoard();
    	lang.nextStep("show num1 and num2");
    	
    	//STEP TO 0
    	rec_title.hide();
    	num1_label.show(); num2_label.show(); sgl_num1_arr.show(); sgl_num2_arr.show();
    	sgl_num1_arr.put(0, num1, null, null);
    	sgl_num2_arr.put(0, num2, null, null);
    	if (rec_depth == 0){
        	generateFirstRow("karatsuba( " + num1 + ", " + num2 + " )");
    	}

    	lang.nextStep("if statement");
    	
    	//STEP TO 1
    	source.toggleHighlight(currentSrcRow, 1);
    	currentSrcRow = 1;
    	boolean bool1 = num1 < 10;
    	boolean bool2 = num2 < 10;
    	if (bool1){
    		bool1_true_label.show();
    	} else {
    		bool1_false_label.show();
    	}
    	if (bool2){
    		bool2_true_label.show();
    	} else {
    		bool2_false_label.show();
    	}
    	if (bool1 || bool2) {
    		lang.nextStep("bool true");
    		
    		source.toggleHighlight(currentSrcRow, 2);
        	currentSrcRow = 2;
        	simple_mult.show();
        	int retResult = num1 * num2;
        	String calc = num1 + " x " + num2 + " = " + retResult;
        	simple_mult.setText(calc, null, null);
        	multCounter++;
        	updateCounter();
        	rec_depth--;
        	lang.nextStep("resolving");
        	
        	Text lastRow = getLastRow();
        	String oldText = lastRow.getText();
        	String decider = oldText.substring(0, 2);
        	String prefix = oldText.substring(0, 5);
        	
        	
        	if (! decider.equals("z1") ) {
        		lastRow.setText(oldText + " = " + retResult, null, null); 	
        		lang.nextStep();
        		if (! decider.equals("ka")) {
        			lastRow.setText(prefix + String.valueOf(retResult), null, null);
        			lang.nextStep();
        		}
        	} else {
        		int[] depthInfo = recDepthData.get(rec_depth);
        		lastRow.setText(oldText + " = " + retResult + " - " + depthInfo[2] + " - " + depthInfo[0], null, null); 
        		lang.nextStep();
        		int tempZ3 = retResult - depthInfo[2] - depthInfo[0];
        		lastRow.setText(prefix + String.valueOf(tempZ3), null, null);
        	}
    		return num1 * num2;
    		
    	}
    	lang.nextStep("calculate m");
    	
    	//STEP TO 3
    	source.toggleHighlight(currentSrcRow, 3);
    	currentSrcRow = 3;
    	hideBools();
    	int num1Length = String.valueOf(num1).length();
    	int num2Length = String.valueOf(num2).length();
    	int m = Math.max(num1Length, num2Length);
    	if (m % 2 == 1){
    		m++;
    	}
    	m_label.show(); m_arr.show(); m_arr.put(0, m, null, null);
    	lang.nextStep("calculate high1 and high2");
    	
    	//STEP TO 4
    	source.toggleHighlight(currentSrcRow, 4);
    	currentSrcRow = 4;
    	DecimalFormat df = new DecimalFormat("#");
     	df.setRoundingMode(RoundingMode.DOWN);
     	float high1Float = (float) num1 / (float) Math.pow(10, (m / 2));
     	float high2Float = (float) num2 / (float) Math.pow(10, (m / 2)); 	
    	int high1 = Integer.valueOf(df.format(high1Float));
    	int high2 = Integer.valueOf(df.format(high2Float)); 	
    	int low1 = num1 % (int) Math.pow( 10 , ( m / 2));
    	int low2 = num2 % (int) Math.pow( 10 , ( m / 2));
    	shiftCounter =  shiftCounter + 2;
    	updateCounter();
    	sgl_num1_arr.hide();
    	sgl_num2_arr.hide();
    	dbl_num1_arr.show();
    	dbl_num2_arr.show();
    	
    	dbl_num1_arr.put(0, high1, null, null);
    	dbl_num1_arr.put(1, low1, null, null);
    	dbl_num1_arr.highlightCell(0, null, null);
    	dbl_num2_arr.put(0, high2, null, null);
    	dbl_num2_arr.put(1, low2, null, null);
    	dbl_num2_arr.highlightCell(0, null, null);
    	
    	high1_label.show();
    	high1_arr.show();
    	high1_arr.put(0, high1, null, null);
    	high1_arr.highlightCell(0, null, null);
    	
    	high2_label.show();
    	high2_arr.show();
    	high2_arr.put(0, high2, null, null);
    	high2_arr.highlightCell(0, null, null);
    	lang.nextStep("calculate low1 and low2");
    	
    	//STEP TO 5
    	source.toggleHighlight(currentSrcRow, 5);
    	currentSrcRow = 5;
    	shiftCounter =  shiftCounter + 2;
    	updateCounter();
    	dbl_num1_arr.unhighlightCell(0, null, null);
    	dbl_num1_arr.highlightCell(1, null, null);
    	dbl_num2_arr.unhighlightCell(0, null, null);
    	dbl_num2_arr.highlightCell(1, null, null);
    	
    	high1_arr.unhighlightCell(0, null, null);
    	high2_arr.unhighlightCell(0, null, null);
    	
    	low1_label.show();
    	low1_arr.show();
    	low1_arr.put(0, low1, null, null);
    	low1_arr.highlightCell(0, null, null);
    	
    	low2_label.show();
    	low2_arr.show();
    	low2_arr.put(0, low2, null, null);
    	low2_arr.highlightCell(0, null, null);
    	lang.nextStep();
    	
    	//STEP TO 6
    	source.toggleHighlight(currentSrcRow, 6);
    	currentSrcRow = 6;
    	dbl_num1_arr.unhighlightCell(1, null, null);
    	dbl_num2_arr.unhighlightCell(1, null, null);
    	dbl_num1_arr.hide();
    	dbl_num2_arr.hide();
    	sgl_num1_arr.show();
    	sgl_num2_arr.show();
    	lang.nextStep("calculate z0"); 
    	generateRecRow(true, "z0 = karatsuba( " + low1 + ", " + low2 + " )");
    	recCounter++;
    	updateCounter();
    	int z0 = karatsubaAlgo( low1, low2 );
    	
    	depthArr[0] = z0;
    	recDepthData.remove(rec_depth);
    	recDepthData.add(rec_depth, depthArr);
    	
    	//STEP TO 7
    	source.toggleHighlight(currentSrcRow, 7);
    	currentSrcRow = 7;
    	hideBools();
    	simple_mult.hide();
    	
    	high1_label.show();
    	high1_arr.show();
    	high1_arr.put(0, high1, null, null);
    	high1_arr.highlightCell(0, null, null);
    	
    	high2_label.show();
    	high2_arr.show();
    	high2_arr.put(0, high2, null, null);
    	high2_arr.highlightCell(0, null, null);
    	
    	low1_label.show();
    	low1_arr.show();
    	low1_arr.put(0, low1, null, null);
    	low1_arr.unhighlightCell(0, null, null);
    	
    	low2_label.show();
    	low2_arr.show();
    	low2_arr.put(0, low2, null, null);
    	low2_arr.unhighlightCell(0, null, null);
    	lang.nextStep("z2 calculation"); 	
    	generateRecRow(false, "z2 = karatsuba( " + high1 + ", " + high2 + " )");
    	recCounter++;
    	updateCounter();
    	int z2 = karatsubaAlgo( high1, high2 );
    	depthArr[2] = z2;
    	recDepthData.remove(rec_depth);
    	recDepthData.add(rec_depth, depthArr);
    	
    	//STEP TO 8
    	source.toggleHighlight(currentSrcRow, 8);
    	currentSrcRow = 8;
    	hideBools();
    	simple_mult.hide();
    	
    	high1_label.show();
    	high1_arr.show();
    	high1_arr.put(0, high1, null, null);
    	high1_arr.highlightCell(0, null, null);
    	
    	high2_label.show();
    	high2_arr.show();
    	high2_arr.put(0, high2, null, null);
    	high2_arr.highlightCell(0, null, null);
    	
    	low1_label.show();
    	low1_arr.show();
    	low1_arr.put(0, low1, null, null);
    	low1_arr.highlightCell(0, null, null);
    	
    	low2_label.show();
    	low2_arr.show();
    	low2_arr.put(0, low2, null, null);
    	low2_arr.highlightCell(0, null, null);
    	lang.nextStep("add high1 and low1, high2 and low2 for z1 calculation"); 	
    	
    	simple_mult.show();
    	int res1 = high1 + low1;
    	int res2 = high2 + low2;
    	String calc = high1 + " + " + low1 + " = " + res1 + " and " + high2 + " + " + low2 + " = " + res2;
    	simple_mult.setText(calc, null, null);
    	lang.nextStep("calculate z1");
    	
    	generateRecRow(false, "z1 = karatsuba( " + res1 + ", " + res2 + " ) - z2 - z0");
    	recCounter++;
    	updateCounter();
    	int z1 = karatsubaAlgo( (low1 + high1), ( low2 + high2 ) ) - z2 - z0;
    	depthArr[1] = z1;
    	recDepthData.remove(rec_depth);
    	recDepthData.add(rec_depth, depthArr);
    	
    	clearBoard();
    	hideBools();
    	source.toggleHighlight(currentSrcRow, 9);
    	currentSrcRow = 9;
    	lang.nextStep("return statement");
    	
    	Text uberRow =  getUberRow();
    	String oldUberText = uberRow.getText();
    	String uberDecider = oldUberText.substring(0, 2);
    	String uberPrefix = oldUberText.substring(0, 5);
    	
     	boolean atZ1 = uberDecider.equals("z1");
 
    	int depth_result = z2 * (int) Math.pow( 10, m ) + z1 * (int) Math.pow( 10, ( m / 2 ) ) + z0;
    	multCounter = multCounter + 2;
    	updateCounter();
    	if (rec_depth != 0 && atZ1) {
    		int[] previousData = recDepthData.get(rec_depth - 1);
    		String resolve = " = " + z2 + " * 10^" + m + " + " + z1 + " * 10^" + m/2 + " + " + z0 + " - " + previousData[2] + " - " + previousData[0];
    		uberRow.setText(oldUberText + resolve, null, null);
        	lang.nextStep();
    		uberRow.setText(oldUberText + " = " + depth_result + " - " + previousData[2] + " - " + previousData[0], null, null);
    		hideRecRemains();
        	lang.nextStep();
        	int tempRes = depth_result - previousData[2] - previousData[0];
        	uberRow.setText(uberPrefix + tempRes, null, null);
        		
    	} else {
    		String resolve = " = " + z2 + " * 10^" + m + " + " + z1 + " * 10^" + m/2 + " + " + z0;
    		uberRow.setText(oldUberText + resolve, null, null);
        	lang.nextStep();
        	if (rec_depth == 0) {
        		uberRow.setText(oldUberText + " = " + depth_result, null, null);
        	} else {
        		uberRow.setText(uberPrefix  + depth_result, null, null);
        	}
    		hideRecRemains();
        	lang.nextStep();
    	}
    	
    	rec_depth--;
    	return depth_result;
    }
   
}