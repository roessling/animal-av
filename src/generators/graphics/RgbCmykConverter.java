/*
 * CmykRgbConverter.java

 * Florian Sunnus, Elvir Sinancevic, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;

import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.CodeView;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class RgbCmykConverter implements ValidatingGenerator {
    private Language lang;
   
	//Translator
	Translator translator;
    private Locale locale;
	
	//RGB & CMYK Array
	private int[] rgb, inputRGB, rgbDisplay = {0, 0, 0};
	private int[] cmyk, inputCMYK, cmykDisplay = {0, 0, 0, 0};
	
	//Primitives & InfoBoxes
	private Text rgbArrayHeader, cmykArrayHeader;
	private Text explanation1, explanation2, explanation3;
	private Rect redRect, greenRect, blueRect, cyanRect, magentaRect, yellowRect, blackRect, arrayBackgroundRect, rgbArrayHeaderBackground, cmykArrayHeaderBackground;
	private IntArray rgbArray, cmykArray;
	private SourceCode sourceCode;
	
	//Properties
	private TextProperties headerProperties, arrayHeaderProperties, explanationProperties, textProperties; 
	private ArrayMarkerProperties arrayMarkerProperties;
	private SourceCodeProperties sourceCodeProperties, descriptionProperties;
	private ArrayProperties rgbArrayProperties, cmykArrayProperties;
	private RectProperties headerBackgroundProperties, arrayHeaderBackgroundProperties, arrayBackgroundProperties, rectProperties;
	
	//direction
	private boolean cmykToRgbDir;
    
	//cmykToRgb
	private static final String SOURCE_CODE1     = "public float[] cmykToRgb(double[] cmyk){"
			+ "\n   "
			+ "\n	int[] rgb = new int[3];"
			+ "\n	rgb[0] = (int) (255 * (1 - cmyk[0]) * (1 - cmyk[3]));"
			+ "\n	rgb[1] = (int) (255 * (1 - cmyk[1]) * (1 - cmyk[3]));"
			+ "\n	rgb[2] = (int) (255 * (1 - cmyk[2]) * (1 - cmyk[3]));"
			+ "\n	"
			+ "\n	return rgb;"
			+ "\n}";
    
	//rgbToCmyk
	private static final String SOURCE_CODE2     = "public float[] rgbToCmyk(int[] rgb){"
			+ "\n		float r = rgb[0] / 255;"
			+ "\n		float g = rgb[1] / 255;"
			+ "\n		float b = rgb[2] / 255;"
			+ "\n		"
			+ "\n		float[] cmyk = new float[4];"
			+ "\n		cmyk[3] = 1 - max(r, g, b);"
			+ "\n		cmyk[0] = (1 - r - cmyk[3]) / (1 - cmyk[3]);"
			+ "\n		cmyk[1] = (1 - g - cmyk[3]) / (1 - cmyk[3]);"
			+ "\n		cmyk[2] = (1 - b - cmyk[3]) / (1 - cmyk[3]);"
			+ "\n		return cmyk;"
			+ "\n}";
	
	//Style of the code
	Style codeStyle = new Style() {	
		@Override
		public AnimationProperties getProperties(String arg0) {
			sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
			return sourceCodeProperties;
		}
	};
	//Used for the introduction
	Style textStyle = new Style() {	
		@Override
		public AnimationProperties getProperties(String arg0) {
			descriptionProperties = new SourceCodeProperties();
			descriptionProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
			descriptionProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLACK);
			descriptionProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(40, 40, 40));
			return descriptionProperties;
		}
	};
    
	  // new RgbCmykConverter("resources/RgbCmykConverter", Locale.GERMANY)
	  // new RgbCmykConverter("resources/RgbCmykConverter", Locale.US)
    /**
     * Constructor, handles the selected language
     * @param path path of the language files
     * @param locale locale that is selected
     */
    public RgbCmykConverter(String path, Locale locale){
    	
    	this.locale = locale;
        translator = new Translator(path, locale);    	
    }
	
	/**
	 * initialization lang
	 */
    public void init(){
        lang = new AnimalScript("CMYK / RGB Konverter", "Florian Sunnus, Elvir Sinancevic", 800, 600);
        lang.setStepMode(true);
    }
 
    /**
     * Handles the user input and calls convert() to start the animation
     * returns the generated animalscript code
     */
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
    	inputRGB = (int[])primitives.get("intRGB");
    	inputCMYK = (int[])primitives.get("intCMYK");
    	cmykToRgbDir = (boolean)primitives.get("direction"); 

    	rgb = inputRGB.clone();
    	cmyk = inputCMYK.clone();
    	
    	sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("code");
        arrayBackgroundProperties = (RectProperties)props.getPropertiesByName("arrayBackground");
        arrayHeaderProperties = (TextProperties)props.getPropertiesByName("arrayHeader");
        headerProperties = (TextProperties)props.getPropertiesByName("header");
        arrayHeaderBackgroundProperties = (RectProperties)props.getPropertiesByName("arrayHeaderBackground");
        rgbArrayProperties = (ArrayProperties)props.getPropertiesByName("arrayRGB");
        cmykArrayProperties = (ArrayProperties)props.getPropertiesByName("arrayCMYK");
        explanationProperties = (TextProperties)props.getPropertiesByName("explanation");
        headerBackgroundProperties = (RectProperties)props.getPropertiesByName("headerBackground");       

        convert();
        
        return lang.toString();
    }
    

    /**
     * Shows the basic elements of the animation and then calls the specific animation, rgbToCmyk() or cmykToRgb()
     */
    public void convert() {

		//Calculate the values of cmyk or rgb, depends on direction
		if(cmykToRgbDir){
			cmykDisplay = cmyk;
			rgb[0] = (int) (255 * (1 - (double)cmyk[0] / 100) * (1 - (double)cmyk[3] / 100));
			rgb[1] = (int) (255 * (1 - (double)cmyk[1] / 100) * (1 - (double)cmyk[3] / 100));
			rgb[2] = (int) (255 * (1 - (double)cmyk[2] / 100) * (1 - (double)cmyk[3] / 100));		
		}else{
			rgbDisplay = rgb;
			double r = (double)rgb[0] / 255;
			double g = (double)rgb[1] / 255;
			double b = (double)rgb[2] / 255;		
			double tmp = (1 - Math.max(r, Math.max(g, b)));
			cmyk[0] = (int) (100 * (1 - r - tmp) / (1 - tmp));
			cmyk[1] = (int) (100 * (1 - g - tmp) / (1 - tmp));
			cmyk[2] = (int) (100 * (1 - b - tmp) / (1 - tmp));
			cmyk[3] = (int) (100 * (tmp));
		}
		
		//Initialization properties
		textProperties = new TextProperties();
		arrayMarkerProperties = new ArrayMarkerProperties();
		rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);		
		
		//Header
		headerBackgroundProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		Font headerFont = (Font) headerProperties.get("font");
		headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(headerFont.getFamily(), Font.BOLD, 20));
		if(cmykToRgbDir){
			lang.newText(new Coordinates(250, 15), translator.translateMessage("headerRgbToCmyk"), "header", null, headerProperties);
			lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(10, 5, "header", "SE"), "rectHeader", null, headerBackgroundProperties);
		}else{
			lang.newText(new Coordinates(250, 15), translator.translateMessage("headerCmykToRgb"), "header", null, headerProperties);
			lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(10, 5, "header", "SE"), "rectHeader", null, headerBackgroundProperties);
		}
		
		//Description
		SourceCode descriptionText = CodeView.primitiveFromString(lang, translator.translateMessage("description"), "description", new Coordinates(30, 60), null, textStyle);
		descriptionText.highlight(0);
		descriptionText.highlight(5);
		descriptionText.highlight(10);
		
		lang.nextStep(translator.translateMessage("introduction"));	  
		descriptionText.hide();
		

		//Arrays
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		if(cmykToRgbDir)
			rgbArrayHeader = lang.newText(new Offset(-200, 50, "rectHeader", "SW"), translator.translateMessage("RGB-Values"), "rgbArrayHeader", null, arrayHeaderProperties);
		else
			rgbArrayHeader = lang.newText(new Offset(-200, 30, "rectHeader", "SW"), translator.translateMessage("RGB-Values"), "rgbArrayHeader", null, arrayHeaderProperties);
		cmykArrayHeader = lang.newText(new Offset(60, 0, "rgbArrayHeader", "NE"), translator.translateMessage("CMYK-Values"), "cmykArrayHeader", null, arrayHeaderProperties);
		arrayHeaderBackgroundProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		rgbArrayHeaderBackground = lang.newRect(new Offset(-5, -5, "rgbArrayHeader", "NW"),  new Offset(5, 5, "rgbArrayHeader", "SE"), "rgbArrayHeaderBackground", null, arrayHeaderBackgroundProperties);
		cmykArrayHeaderBackground = lang.newRect(new Offset(-5, -5, "cmykArrayHeader", "NW"),  new Offset(5, 5, "cmykArrayHeader", "SE"), "cmykArrayHeaderBackground", null, arrayHeaderBackgroundProperties);
		rgbArray = lang.newIntArray(new Offset(0, 100, "rgbArrayHeader", "SW"), rgbDisplay, "rgbArray", null, rgbArrayProperties);
		cmykArray = lang.newIntArray(new Offset(60, 100, "rgbArrayHeader", "SE"),  cmykDisplay, "cmykArray", null, cmykArrayProperties);
		arrayBackgroundProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
		arrayBackgroundRect = lang.newRect(new Offset(-10, -15, "rgbArrayHeader", "NW"),  new Offset(35, 160, "cmykArrayHeader", "SE"), "arrayBackground", null, arrayBackgroundProperties);
		
		//Sourcecode
		if(cmykToRgbDir)
			sourceCode = CodeView.primitiveFromString(lang, SOURCE_CODE1, "code", new Offset(-20, 30, "rgbArray", "SW"), null, codeStyle);
		else
			sourceCode = CodeView.primitiveFromString(lang, SOURCE_CODE2, "code", new Offset(-5, 30, "rgbArray", "SW"), null, codeStyle);
		
		//Line
		Node[] lineNodes = {new Coordinates(425, 60), new Coordinates(425, 525)};
		lang.newPolyline(lineNodes, "line", null);

		//Display of the converted Color 
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
		lang.newText(new Offset(10, 15, "line", "N"), translator.translateMessage("givenColor"), "descriptionColor", null, textProperties);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(rgb[0], rgb[1], rgb[2]));
		lang.newRect(new Offset(30, 15, "descriptionColor",  "SW"), new Offset(-30, 30, "descriptionColor", "SE"), "colorRect", null, rectProperties);

		//Diagrams
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
		Node[] diagramRgbNodes = {new Offset(60, 50, "descriptionColor", "SW"), new Offset(60, 150, "descriptionColor", "SW"), new Offset(145, 150, "descriptionColor", "SW")};
		lang.newPolyline(diagramRgbNodes, "rgbDiagram", null);
		lang.newText(new Offset(-25, -5, "rgbDiagram", "NW"), "255", "rgbYAxis", null);
		lang.newText(new Offset(-10, -10, "rgbDiagram", "SW"), "0", "rgbXAxis", null);
		Node[] diagramCmykNodes = {new Offset(200, 0, "rgbDiagram", "NW"), new Offset(200, 0, "rgbDiagram", "SW"), new Offset(310, 0, "rgbDiagram", "SW")};
		lang.newPolyline(diagramCmykNodes, "cmykDiagram", null);
		lang.newText(new Offset(-35, -5, "cmykDiagram", "NW"), "100%", "cmykYAxis", null);
		lang.newText(new Offset(-20, -10, "cmykDiagram", "SW"), "0%", "cmykXAxis", null);
		//RGB Rects Diagram
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
		redRect = lang.newRect(new Offset(5, 0, "rgbDiagram", "SW"), new Offset(25, -(int)(rgb[0]/2.55), "rgbDiagram", "SW"), "redRect", null, rectProperties);
		lang.newText(new Offset(0, 0, "redRect", "S"), "r", "r", null, textProperties);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
		greenRect = lang.newRect(new Offset(5, 0, "redRect", "SE"), new Offset(25, -(int)(rgb[1]/2.55), "redRect", "SE"), "greenRect", null, rectProperties);
		lang.newText(new Offset(0, 0, "greenRect", "S"), "g", "g", null, textProperties);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		blueRect = lang.newRect(new Offset(5, 0, "greenRect", "SE"), new Offset(25, -(int)(rgb[2]/2.55), "greenRect", "SE"), "blueRect", null, rectProperties);
		lang.newText(new Offset(0, 0, "blueRect", "S"), "b", "b", null, textProperties);
		//CMYK Rects Diagram
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
		cyanRect = lang.newRect(new Offset(5, 0, "cmykDiagram", "SW"), new Offset(25, -cmyk[0], "cmykDiagram", "SW"), "cyanRect", null, rectProperties);
		lang.newText(new Offset(0, 0, "cyanRect", "S"), "c", "c", null, textProperties);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.MAGENTA);
		magentaRect = lang.newRect(new Offset(5, 0, "cyanRect", "SE"), new Offset(25, -cmyk[1], "cyanRect", "SE"), "magentaRect", null, rectProperties);
		lang.newText(new Offset(0, 0, "magentaRect", "S"), "m", "m", null, textProperties);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		yellowRect = lang.newRect(new Offset(5, 0, "magentaRect", "SE"), new Offset(25, -cmyk[2], "magentaRect", "SE"), "yellowRect", null, rectProperties);
		lang.newText(new Offset(0, 0, "yellowRect", "S"), "y", "y", null, textProperties);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		blackRect = lang.newRect(new Offset(5, 0, "yellowRect", "SE"), new Offset(25, -cmyk[3], "yellowRect", "SE"), "blackRect", null, rectProperties);
		lang.newText(new Offset(0, 0, "blackRect", "S"), "k", "k", null, textProperties);

		//Arrow
		PolylineProperties polylineProperties = new PolylineProperties();
		polylineProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		Node[] arrowNodes = new Node[2];
		if(cmykToRgbDir){
			arrowNodes[0] = new Offset(70, 0, "rgbDiagram", "E");
			arrowNodes[1] = new Offset(15, 0, "rgbDiagram", "E");
		}else{
			arrowNodes[0] = new Offset(15, 0, "rgbDiagram", "E");
			arrowNodes[1] = new Offset(70, 0, "rgbDiagram", "E");
		}
		lang.newPolyline(arrowNodes, "arrow", null, polylineProperties);

		//Explanation
		Font explanationFont = (Font) explanationProperties.get("font");
		explanationProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(explanationFont.getFamily(), Font.BOLD, 16));
		lang.newText(new Offset(30, 225, "descriptionColor", "NW"), translator.translateMessage("descriptionHeader"), "explanationHeader", null, explanationProperties);
		explanationProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(explanationFont.getFamily(), Font.PLAIN, 14));
		explanation1 = lang.newText(new Offset(0, 15, "explanationHeader", "SW"), "", "explanation1", null, explanationProperties);
		explanation2 = lang.newText(new Offset(0, 8, "explanation1", "SW"), "", "explanation2", null, explanationProperties);
		explanation3 = lang.newText(new Offset(0, 8, "explanation2", "SW"), "", "explanation3", null, explanationProperties);
		
		
		if(cmykToRgbDir)
			cmykToRgbAnimation();
		else
			rgbToCmykAnimation();

	}
    
    /**
	 * Shows the animation: rgb -> cymk
	 */
	private void rgbToCmykAnimation(){
		
		blackRect.hide();
		cyanRect.hide();
		magentaRect.hide();
		yellowRect.hide();
		cmykArray.hide();
		cmykArrayHeader.hide();
		cmykArrayHeaderBackground.hide();
	
		sourceCode.highlight(0);
		explanation1.setText(translator.translateMessage("explanation1"), null, null);
		explanation2.setText(translator.translateMessage("explanation2"), null, null);
		lang.nextStep(translator.translateMessage("startAlgorithm"));
		
		//Step
		highlightNextLine(sourceCode, 0, 1);
		rgbArray.highlightCell(0, null, null);
		ArrayMarker rgbMarker1 = newArrayMarker("rgbMarker1", "red", Color.RED, rgbArray, 0);
		
		explanation1.setText(translator.translateMessage("explanation3"), null, null);
		explanation2.setText(translator.translateMessage("explanation4"), null, null);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 1, 2);
		highlightOtherCell(rgbArray, 0, 1);
		rgbMarker1.hide();    

		ArrayMarker rgbMarker2 = newArrayMarker("rgbMarker2", "green", Color.GREEN, rgbArray, 1);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 2, 3);
		highlightOtherCell(rgbArray, 1, 2);
		rgbMarker2.hide();

		ArrayMarker rgbMarker3 = newArrayMarker("rgbMarker3", "blue", Color.BLUE, rgbArray, 2);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 3, 5);
		rgbArray.unhighlightCell(2, null, null);
		rgbMarker3.hide();

		cmykArray.show();
		cmykArrayHeader.show();
		cmykArrayHeaderBackground.show();
		explanation1.setText(translator.translateMessage("explanation5"), null, null);
		explanation2.setText(translator.translateMessage("explanation6"), null, null);
		explanation3.setText(translator.translateMessage("explanation7"), null, null);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 5, 6);

		ArrayMarker cmykMarker1 = newArrayMarker("cymkMarker1", "black", Color.BLACK, cmykArray, 3);
		cmykArray.highlightCell(3, null, null);  
		cmykArray.put(3, cmyk[3], null, null);		
		blackRect.show();
		explanation1.setText(translator.translateMessage("explanation8"), null, null);
		explanation2.setText(translator.translateMessage("explanation9"), null, null);
		explanation3.setText("", null, null);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 6, 7);
		highlightOtherCell(cmykArray, 3, 0);
		rgbMarker3.hide();
		cmykMarker1.hide();

		ArrayMarker cmykMarker2 = newArrayMarker("cymkMarker2", "cyan", Color.CYAN, cmykArray, 0);
		cmykArray.put(0, cmyk[0], null, null);	
		cyanRect.show();
		explanation1.setText(translator.translateMessage("explanation10"), null, null);
		explanation2.setText(translator.translateMessage("explanation11"), null, null);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 7, 8);
		highlightOtherCell(cmykArray, 0, 1);
		cmykMarker2.hide();

		ArrayMarker cmykMarker3 = newArrayMarker("cmykMarker3", "magenta", Color.MAGENTA, cmykArray, 1);
		cmykArray.put(1, cmyk[1], null, null);	  
		magentaRect.show();
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 8, 9);
		highlightOtherCell(cmykArray, 1, 2);
		cmykMarker3.hide();	    

		ArrayMarker cmykMarker4 = newArrayMarker("cymkMarker4", "yellow", Color.YELLOW, cmykArray, 2);
		cmykArray.put(2, cmyk[2], null, null);	
		yellowRect.show();
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 9, 10);
		cmykMarker4.hide();
		cmykArray.unhighlightCell(2, null, null);
		explanation1.setText(translator.translateMessage("explanation12"), null, null);
		explanation2.setText(translator.translateMessage("explanation13"), null, null);
		lang.nextStep();
				
		//Step
		endScreen();

	}


	/**
	 * Shows the animation: cmyk -> rgb
	 */
	private void cmykToRgbAnimation(){

		redRect.hide();
		greenRect.hide();
		blueRect.hide();
		rgbArray.hide();
		rgbArrayHeader.hide();
		rgbArrayHeaderBackground.hide();

		explanation1.setText(translator.translateMessage("explanation14"), null, null);		
		explanation2.setText(translator.translateMessage("explanation15"), null, null);
		sourceCode.highlight(0);
		lang.nextStep(translator.translateMessage("startAlgorithm"));

		//Step
		highlightNextLine(sourceCode, 0, 2);
		rgbArray.show();
		rgbArrayHeader.show();
		rgbArrayHeaderBackground.show();
		explanation1.setText(translator.translateMessage("explanation16"), null, null);		
		explanation2.setText(translator.translateMessage("explanation17"), null, null);
		explanation3.setText(translator.translateMessage("explanation18"), null, null);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 2, 3);

		ArrayMarker redMarker = newArrayMarker("redMarker", "red", Color.RED, rgbArray, 0);
		ArrayMarker cyanMarker = newArrayMarker("cyanMarker", "cyan", Color.CYAN, cmykArray, 0);
		arrayMarkerProperties.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true);
		ArrayMarker blackMarker = newArrayMarker("blackMarker", "black", Color.BLACK, cmykArray, 3);	
		arrayMarkerProperties.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, false);
		rgbArray.highlightCell(0, null, null);
		cmykArray.highlightCell(0, null, null);
		cmykArray.highlightCell(3, null, null);
		redRect.show();
		rgbArray.put(0, rgb[0], null, null);
		explanation1.setText(translator.translateMessage("explanation19"), null, null);		
		explanation2.setText(translator.translateMessage("explanation20"), null, null);
		explanation3.setText("", null, null);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 3, 4);
		redMarker.hide();
		cyanMarker.hide();

		ArrayMarker greenMarker = newArrayMarker("greenMarker", "green", Color.GREEN, rgbArray, 1);
		ArrayMarker magentaMarker = newArrayMarker("magentaMarker", "magenta", Color.MAGENTA, cmykArray, 1);
		highlightOtherCell(rgbArray, 0, 1);
		highlightOtherCell(cmykArray, 0, 1);
		greenRect.show();
		rgbArray.put(1, rgb[1], null, null);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 4, 5);
		greenMarker.hide();
		magentaMarker.hide();

		ArrayMarker blueMarker = newArrayMarker("blueMarker", "blue", Color.BLUE, rgbArray, 2);
		ArrayMarker yellowMarker = newArrayMarker("yellowMarker", "yellow", Color.YELLOW, cmykArray, 2);
		highlightOtherCell(rgbArray, 1, 2);
		highlightOtherCell(cmykArray, 1, 2);
		blueRect.show();
		rgbArray.put(2, rgb[2], null, null);
		lang.nextStep();

		//Step
		highlightNextLine(sourceCode, 5, 7);
		blueMarker.hide();
		yellowMarker.hide();
		blackMarker.hide();
		rgbArray.unhighlightCell(2, null, null);
		cmykArray.unhighlightCell(2, null, null);
		cmykArray.unhighlightCell(3, null, null);

		explanation1.setText(translator.translateMessage("explanation21"), null, null);		
		explanation2.setText(translator.translateMessage("explanation22"), null, null);
		lang.nextStep();

		//Step
		endScreen();
		
		
	}
		
	/**
	 * Shows the summary of the conversion
	 */
	private void endScreen() {
		
		sourceCode.hide();
		cmykArray.hide();
		cmykArrayHeader.hide();
		rgbArray.hide();
		rgbArrayHeader.hide();
		rgbArrayHeaderBackground.hide();
		cmykArrayHeaderBackground.hide();
		arrayBackgroundRect.hide();
		
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));

		if(!cmykToRgbDir){
			lang.newText(new Coordinates(20, 110), String.format(translator.translateMessage("summary1"), rgb[0], rgb[1], rgb[2]), "s1", null, textProperties);
			lang.newText(new Offset(0, 20, "s1", "SW"), translator.translateMessage("summary2"), "s2", null, textProperties);
			lang.newText(new Offset(0, 8, "s2", "SW"), translator.translateMessage("summary3"), "s3", null, textProperties);
			lang.newText(new Offset(0, 8, "s3", "SW"), translator.translateMessage("summary4"), "s4", null, textProperties);
			
			lang.newText(new Offset(0, 25, "s4", "SW"), translator.translateMessage("summary5"), "s5", null, textProperties);
			lang.newText(new Offset(0, 10, "s5", "SW"), translator.translateMessage("summary6"), "s6", null, textProperties);
			lang.newText(new Offset(0, 10, "s6", "SW"), translator.translateMessage("summary7"), "s7", null, textProperties);
			lang.newText(new Offset(0, 10, "s7", "SW"), translator.translateMessage("summary8"), "s8", null, textProperties);
	
			lang.newText(new Offset(150, 0, "s5", "NW"), String.format("%d %c", cmyk[0], '%'), "s51", null, textProperties);
			lang.newText(new Offset(150, 0, "s6", "NW"), String.format("%d %c", cmyk[1], '%'), "s52", null, textProperties);
			lang.newText(new Offset(150, 0, "s7", "NW"), String.format("%d %c", cmyk[2], '%'), "s52", null, textProperties);
			lang.newText(new Offset(150, 0, "s8", "NW"), String.format("%d %c", cmyk[3], '%'), "s53", null, textProperties);
		}else{
			lang.newText(new Coordinates(20, 110), String.format(translator.translateMessage("summary9"), cmyk[0], '%', cmyk[1], '%', cmyk[2], '%', cmyk[3], '%'), "s1", null, textProperties);
			lang.newText(new Offset(0, 20, "s1", "SW"), translator.translateMessage("summary2"), "s2", null, textProperties);
			lang.newText(new Offset(0, 8, "s2", "SW"), translator.translateMessage("summary10"), "s3", null, textProperties);
			lang.newText(new Offset(0, 8, "s3", "SW"), translator.translateMessage("summary4"), "s4", null, textProperties);
			
			lang.newText(new Offset(0, 25, "s4", "SW"), translator.translateMessage("summary11"), "s5", null, textProperties);
			lang.newText(new Offset(0, 10, "s5", "SW"), translator.translateMessage("summary12"), "s6", null, textProperties);
			lang.newText(new Offset(0, 10, "s6", "SW"), translator.translateMessage("summary13"), "s7", null, textProperties);
	
			lang.newText(new Offset(150, 0, "s5", "NW"), Integer.toString(rgb[0]), "s51", null, textProperties);
			lang.newText(new Offset(150, 0, "s6", "NW"), Integer.toString(rgb[1]), "s52", null, textProperties);
			lang.newText(new Offset(150, 0, "s7", "NW"), Integer.toString(rgb[2]), "s52", null, textProperties);

		}
		
		lang.nextStep(translator.translateMessage("end"));		
	}
	
	
	/**
	 * Unhighlights prev line, highlights next line 
	 * @param source code
	 * @param prev line that will be unhighlited
	 * @param next line that will be highlighted
	 */
	private void highlightNextLine(SourceCode source, int prev, int next){
		source.unhighlight(prev);
		source.highlight(next);
	}

	/**
	 * Unhighlights prev arraycell, highlights next arraycell
	 * @param array
	 * @param prev cell that will be unhighlited
	 * @param next cell that will be highlighted
	 */
	private void highlightOtherCell(IntArray array, int prev, int next){
		array.unhighlightCell(prev, null, null);	    
		array.highlightCell(next, null, null);	    
	}

	/**
	 * Creates a new arraymarker
	 * @param name name of the new marker
	 * @param label label of the new marker
	 * @param color color of the new marker
	 * @param array the array for the marker
	 * @param pos postition of the new marker
	 * @return the created Arraymarker
	 */
	private ArrayMarker newArrayMarker(String name, String label, Color color, IntArray array, int pos){
		arrayMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, label);
		arrayMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,  color);
		return lang.newArrayMarker(array, pos, name, null, arrayMarkerProperties);
	}


    /**
     * Returns the name of the generator
     */
    public String getName() {
        return translator.translateMessage("name");
    }

    /**
     * Returns the name of the algorithm
     */
    public String getAlgorithmName() {
        return translator.translateMessage("nameAlgorithm");
    }

    /**
     * Returns the authors of the generator
     */
    public String getAnimationAuthor() {
        return "Florian Sunnus, Elvir Sinancevic";
    }

    /**
     * Returns a description of the generator
     */
    public String getDescription(){
        return translator.translateMessage("description");
    }

    /**
     * Returns a code example of the algorithm
     */
    public String getCodeExample(){
        return SOURCE_CODE1 + "\n" + SOURCE_CODE2;
    }

    /**
     * Returns the file extension for the animalscript code
     */
    public String getFileExtension(){
        return "asu";
    }

    /**
     * Returns current locale
     */
    public Locale getContentLocale() {
        return locale;
    }

    /**
     * Returns the type of the generator
     */
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    /**
     * Returns the program language of the shown algorithm
     */
    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }


	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		
        int[] rgbI = (int[])primitives.get("intRGB");
        int[] cmykI = (int[])primitives.get("intCMYK");
        boolean directionI = (boolean)primitives.get("direction");
        
        if(directionI){
        	if((cmykI[0] >= 0 && cmykI[0] <= 100) && (cmykI[1] >= 0 && cmykI[1] <= 100) && (cmykI[2] >= 0 && cmykI[2] <= 100) && (cmykI[3] >= 0 && cmykI[3] <= 100))
        		return true;
        	else 
        		throw new IllegalArgumentException(translator.translateMessage("error1"));
        }else{
        	if((rgbI[0] >= 0 && rgbI[0] <= 255) && (rgbI[1] >= 0 && rgbI[1] <= 255) && (rgbI[2] >= 0 && rgbI[2] <= 255))
        		return true;
        	else
        		throw new IllegalArgumentException(translator.translateMessage("error2"));        	
        }      
	}
	

}