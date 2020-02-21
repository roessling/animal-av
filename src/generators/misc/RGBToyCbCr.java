/*
 * rgbToyCbCr.java
 * Tetiana Rozenvasser, Olga Bayerle, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;
import translator.Translator;

public class RGBToyCbCr implements Generator {
	
	 private Language lang;
	
	//Translator
    private Translator translator;
    private Locale locale;
    
    //Properties
    private ArrayMarkerProperties rgbMarkerProps;
    private ArrayMarkerProperties ycbcrMarkerProps;
    private TextProperties description;
    private TextProperties rgbtxt1;
    private RectProperties headerRect;
    private int[] rgb;
    private SourceCodeProperties sourceCode;
    private TextProperties ycbcrtxt1;
    private SourceCodeProperties commentCode;
    private ArrayProperties array2;
    private TextProperties header;
    private ArrayProperties array1;
    private TextProperties finaltxt;
    
    //timing
    public final static Timing defaultDuration = new TicksTiming(30);
    
    //variablen zum berechnen
    private double matrix [][] = {{0.299, 0.587, 0.114},{-0.168736, -0.331264, 0.5},{0.5, -0.418688, -0.081312}}; //Matrix die wir für die Formel brauchen
   // public int yCbCr[] = new int[3];
	
    //yCbCr[0] = (int) (0 + (matrix[0][0] * rgb[0] + matrix[0][1] * rgb[1] + matrix[0][2] * rgb[2]));
    //yCbCr[1] = (int) (128 + (matrix[1][0] * rgb[0] + matrix[1][1] * rgb[1] + matrix[1][2] * rgb[2]));
    //yCbCr[2] = (int) (128 + (matrix[2][0] * rgb[0] + matrix[2][1] * rgb[1] + matrix[2][2] * rgb[2]));

    public void init(){
        lang = new AnimalScript((translator.translateMessage("generatorName")), "Tetiana Rozenvasser, Olga Bayerle", 800, 600);
        lang.setStepMode(true);
    }

    //konstruktor
    public RGBToyCbCr(String path, Locale locale) {
     	this.locale = locale;
     	translator = new Translator(path,locale);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        rgbMarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("rgbMarkerProps");
        ycbcrMarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("ycbcrMarkerProps");
        description = (TextProperties)props.getPropertiesByName("description");
        rgbtxt1 = (TextProperties)props.getPropertiesByName("rgbtxt1");
        headerRect = (RectProperties)props.getPropertiesByName("headerRect");
        rgb = (int[])primitives.get("rgb");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        ycbcrtxt1 = (TextProperties)props.getPropertiesByName("ycbcrtxt1");
        commentCode = (SourceCodeProperties)props.getPropertiesByName("commentCode");
        array2 = (ArrayProperties)props.getPropertiesByName("array2");
        header = (TextProperties)props.getPropertiesByName("header");
        array1 = (ArrayProperties)props.getPropertiesByName("array1");
        finaltxt = (TextProperties)props.getPropertiesByName("finaltxt");
        
        convertRGBtoYCbCr(rgb);
        return lang.toString();
    }

    public void convertRGBtoYCbCr(int[] rgb) {
    	 int yCbCr[] = new int[3];
		Font fontTitle = (Font) header.get("font");
		fontTitle = fontTitle.deriveFont(1, 36);
		header.set("font", fontTitle);
        // Überschrift wird erstellt
        Text header1 = lang.newText(new Coordinates(20, 30), (translator.translateMessage("generatorName")), "header", null, header);
        // Rectangle für den Überschrift wird erstellt
        Rect headerRect1 = this.lang.newRect(new Offset(-5, 10, "header", "SW"), new Offset(5, -10, "header", "NE"), "RGB zu YCbCr Konverter", null, headerRect);
        
        lang.nextStep((translator.translateMessage("step1")));
        // Description wird erstellt
        Text description1 = lang.newText(new Coordinates(20, 100), (translator.translateMessage("descr1")), "description1", null, description);
        Text description2 = lang.newText(new Coordinates(20, 120), (translator.translateMessage("descr2")), "description2", null, description);
        Text description3 = lang.newText(new Coordinates(20, 150), (translator.translateMessage("descr3")), "description3", null, description);
        Text description4 = lang.newText(new Coordinates(20, 170), (translator.translateMessage("descr4")), "description4", null, description);
        Text description5 = lang.newText(new Coordinates(20, 190), (translator.translateMessage("descr5")), "description5", null, description);
        Text description6 = lang.newText(new Coordinates(20,210), (translator.translateMessage("descr6")), "description6", null, description);
        Text description7 = lang.newText(new Coordinates(20, 230), (translator.translateMessage("descr7")), "description7", null, description);
        Text description8 = lang.newText(new Coordinates(20, 260), (translator.translateMessage("descr8")), "description8", null, description);
        lang.nextStep((translator.translateMessage("step2")));
        
        description1.hide();
        description2.hide();
        description3.hide();
        description4.hide();
        description5.hide();
        description6.hide();
        description7.hide();
        description8.hide();
        Text description9 = lang.newText(new Coordinates(20, 100), (translator.translateMessage("descr9")), "description9", null, description);
        Text description10 = lang.newText(new Coordinates(20, 120), "R = " + rgb[0], "description10", null, description);
        Text description11 = lang.newText(new Coordinates(20, 140), "G = " + rgb[1], "description11", null, description);
        Text description12 = lang.newText(new Coordinates(20, 160), "B = " + rgb[2], "description12", null, description);
        lang.nextStep((translator.translateMessage("step3")));
        
        description9.hide();
        description10.hide();
        description11.hide();
        description12.hide();
        
        // create IntArray object für rgb, linked to the properties
        IntArray rgb1 = lang.newIntArray(new Coordinates(40, 150), rgb, "RGBArray", null, array1);
        rgb1.hide();
        // create IntArray object für rgb, linked to the properties
        IntArray yCbCr1 = lang.newIntArray(new Coordinates(170, 150), yCbCr, "YCbCrArray", null, array2);
        yCbCr1.hide();

        // now, create the source code entity
        SourceCode sc = lang.newSourceCode(new Coordinates(20, 220), "SourceCode", null, sourceCode);

        // Add the lines to the SourceCode object.
        // Line, name, indentation, display dealy
        sc.addCodeLine("public void RGBtoYCbCr(int[] rgb) {", null, 0, null); // 0
        sc.addCodeLine("double matrix [][] = {{0.299, 0.587, 0.114},{-0.168736, -0.331264, 0.5},{0.5, -0.418688, -0.081312}};", null, 0, null); // 1
        sc.addCodeLine("int YCbCr[] = new int[3];", null, 0, null); // 2
        sc.addCodeLine("YCbCr[0] = (int) (0 + (matrix[0][0] * rgb[0] + matrix[0][1] * rgb[1] + matrix[0][2] * rgb[2]));", null, 0, null); // 3
        sc.addCodeLine("YCbCr[1] = (int) (128 + (matrix[1][0] * rgb[0] + matrix[1][1] * rgb[1] + matrix[1][2] * rgb[2]));", null, 0, null); // 4
        sc.addCodeLine("YCbCr[2] = (int) (128 + (matrix[2][0] * rgb[0] + matrix[2][1] * rgb[1] + matrix[2][2] * rgb[2]));", null, 0, null); // 5
        sc.addCodeLine("}", null, 0, null); // 6
         lang.nextStep((translator.translateMessage("step4")));
        
        // now create the comment code entity
        SourceCode comment = lang.newSourceCode(new Coordinates(600, 220), "CommentCode", null, commentCode);
        // highlights the first line of the Code
        sc.highlight(0, 0, false);
        // Highlight line 1 and add matching comments
        sc.toggleHighlight(0, 0, false, 1, 0);
        comment.addCodeLine(" " , null, 0, null);
        // zeigen rgb Array
        Text rgbtxt = lang.newText(new Coordinates (40, 90), (translator.translateMessage("descr99")), "rgb-value", null, rgbtxt1);
        rgb1.show();
        lang.nextStep();
        
        sc.toggleHighlight(1, 0, false, 2, 0);
        comment.addCodeLine(" ", null, 0, null);
        lang.nextStep((translator.translateMessage("step5")));
        
        sc.toggleHighlight(2, 0, false, 3, 0);
        comment.addCodeLine("int YCbCr[] = new int[3];", null, 0, null);
        Text ycbcrtxt = lang.newText(new Coordinates (170, 90), (translator.translateMessage("descry")), "yCbCr-value", null, ycbcrtxt1);
        yCbCr1.show();
        lang.nextStep((translator.translateMessage("step6")));
        
        sc.toggleHighlight(3, 0, false, 4, 0);
        
        // ?? wird im HauptArray value gesetzt
        yCbCr[0] = (int) (0 + (matrix[0][0] * rgb[0] + matrix[0][1] * rgb[1] + matrix[0][2] * rgb[2]));    
        // create IntArray object für rgb, linked to the properties
       
        comment.addCodeLine("YCbCr[0] = (int) (0 + (" + matrix[0][0] + " * " + rgb[0] + " + " + matrix[0][1] + " * " + rgb[1] + " + " + matrix[0][2] +" * " + rgb[2] + "));", null, 0, null);
        ArrayMarker rgbMarker = lang.newArrayMarker(rgb1, 0, "rot", null, rgbMarkerProps);
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null, null);
        ArrayMarker yCbCrMarker = lang.newArrayMarker(yCbCr1, 0, "Y", null, ycbcrMarkerProps);
        lang.nextStep();
        
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN, null, null);
        rgbMarker.move(1, null, defaultDuration);
        lang.nextStep();
        
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, null, null);
        rgbMarker.move(2, null, defaultDuration);
        IntArray yCbCr2 = lang.newIntArray(new Coordinates(170, 150), yCbCr, "YCbCrArray", null, array2);
        ArrayMarker yCbCrMarker1 = lang.newArrayMarker(yCbCr2, 0, "Y", null, ycbcrMarkerProps);
        yCbCr1.hide();
        yCbCrMarker.hide();
        lang.nextStep((translator.translateMessage("step7")));
        
        sc.toggleHighlight(4, 0, false, 5, 0);
        
        yCbCr[1] = (int) (128 + (matrix[1][0] * rgb[0] + matrix[1][1] * rgb[1] + matrix[1][2] * rgb[2]));
        comment.addCodeLine("YCbCr[1] = (int) (128 + (" + matrix[1][0] + " * " + rgb[0] + " + " + matrix[1][1] + " * " + rgb[1] + " + " + matrix[1][2] +" * " + rgb[2] + "));", null, 0, null);
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null, null);
        rgbMarker.move(0, null, defaultDuration);
        yCbCrMarker1.move(1, null, defaultDuration);
        lang.nextStep();
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN, null, null);
        rgbMarker.move(1, null, defaultDuration);
        lang.nextStep();
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, null, null);
        rgbMarker.move(2, null, defaultDuration);
        IntArray yCbCr3 = lang.newIntArray(new Coordinates(170, 150), yCbCr, "YCbCrArray", null, array2);
        yCbCr2.hide();
        ArrayMarker yCbCrMarker2 = lang.newArrayMarker(yCbCr3, 1, "Cb", null, ycbcrMarkerProps);
        yCbCrMarker1.hide();
        
        lang.nextStep((translator.translateMessage("step8")));
        
        sc.toggleHighlight(5, 0, false, 6, 0);
        
        yCbCr[2] = (int) (128 + (matrix[2][0] * rgb[0] + matrix[2][1] * rgb[1] + matrix[2][2] * rgb[2]));
        comment.addCodeLine("YCbCr[2] = (int) (128 + (" + matrix[2][0] + " * " + rgb[0] + " + " + matrix[2][1] + " * " + rgb[1] + " + " + matrix[2][2] +" * " + rgb[2] + "));", null, 0, null);
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, null, null);
        rgbMarker.move(0, null, defaultDuration);
        yCbCrMarker2.move(2, null, defaultDuration);
        lang.nextStep();
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN, null, null);
        rgbMarker.move(1, null, defaultDuration);
        lang.nextStep();
        rgbMarker.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE, null, null);
        rgbMarker.move(2, null, defaultDuration);
        IntArray yCbCr4 = lang.newIntArray(new Coordinates(170, 150), yCbCr, "YCbCrArray", null, array2);
        yCbCr3.hide();
        ArrayMarker yCbCrMarker3 = lang.newArrayMarker(yCbCr4, 2, "Cr", null, ycbcrMarkerProps);
        yCbCrMarker2.hide();
        lang.nextStep((translator.translateMessage("step9")));
        
        //unhighlight den Code
        sc.unhighlight(6,0, false);
        
        comment.hide();
        sc.hide();
        rgb1.hide();
        yCbCr4.hide();
        rgbtxt.hide();
        ycbcrtxt.hide();
        rgbMarker.hide();
        yCbCrMarker3.hide();
       
        
        // final text
      //  Text finaltext = lang.newText(new Coordinates(20, 120), (translator.translateMessage("descry")), "finaltxt", null, finaltxt);
        Text finaltext1 = lang.newText(new Coordinates(20, 210), "Y = " + yCbCr[0], "finaltxt1", null, finaltxt);
        Text finaltext2 = lang.newText(new Coordinates(20, 230), "Cb = " + yCbCr[1], "finaltxt2", null, finaltxt);
        Text finaltext3 = lang.newText(new Coordinates(20, 250), "Cr = " + yCbCr[2], "finaltxt3", null, finaltxt);  
        Text finaltext4 = lang.newText(new Coordinates(20,190), (translator.translateMessage("ergebnistext")), "finaltxt4", null, finaltxt);
        
        Text finaltext5 = lang.newText(new Coordinates(20,100), (translator.translateMessage("ergebnistext1")), "finaltxt5",null, finaltxt);
        Text finaltext6 = lang.newText(new Coordinates(20,120), "R = " + rgb[0], "finaltxt6",null, finaltxt);
        Text finaltext7 = lang.newText(new Coordinates(20,140),  "G = " + rgb[1], "finaltxt7",null, finaltxt);
        Text finaltext8 = lang.newText(new Coordinates(20,160), "B = " + rgb[2], "finaltxt8",null, finaltxt);
        Text finaltext9 = lang.newText(new Coordinates(20,270), (translator.translateMessage("ergebnistext2")), "finaltxt9",null, finaltxt);
        Text finaltext10 = lang.newText(new Coordinates(20,290), (translator.translateMessage("ergebnistext3")), "finaltxt10",null, finaltxt);
    }


    public String getName() {
        return (translator.translateMessage("generatorName"));
    }

    public String getAlgorithmName() {
        return (translator.translateMessage("algorithmName"));
    }

    public String getAnimationAuthor() {
        return "Tetiana Rozenvasser, Olga Bayerle";
    }

    public String getDescription(){
        return (translator.translateMessage("description"));
    }

    public String getCodeExample(){
        return (translator.translateMessage("code"));
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
        return Generator.JAVA_OUTPUT;
    }

	public static void main(String[] args) {
		Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "RGB zu yCbCr",
				"Tetiana Rozenvasser, Olga Bayerle", 800, 600);
		Generator generator = new RGBToyCbCr("resources/rgbToyCbCr",Locale.GERMANY);
		Animal.startGeneratorWindow(generator);
		System.out.println(l);
	}
}
