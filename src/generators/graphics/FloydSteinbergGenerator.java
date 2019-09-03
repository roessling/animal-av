/*
 * FloydSteinbergGenerator.java
 * Sandra Sch�fer, Jonas Schatz, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SquareProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class FloydSteinbergGenerator implements ValidatingGenerator {
	
	// Create the global parameters
    private Color currentPxHighlightColor;
    private Color neighborPxHighlightColor;
    private Color borderColor;
    
    private int[][] imageValues;
    private int pixelSize;
    private int numberOfQuestions;
    private int closeupPxSize;
    private int nRows;
    private int nCols;
    
    private Language lang;
    
    private Locale loc;
    
    private RectProperties Border;
    private RectProperties ShadowProperties;
    private RectProperties BackgroundBoxProperties;
    
    private SourceCodeProperties sourceCodeFontProperties;
    
    private SquareProperties currentPxStyle;
    private SquareProperties neighborPx;
    
    private TextProperties closeupFontProperties;
    private TextProperties smallCaptionStyle;
    private TextProperties captionStyle;
    

    // Already create some global objects (bad style -,-)
    Square LeftBorderSquareTop;
    Square LeftBorderSquareBottom;
    Square BottomBorderSquareLeft;
    Square BottomBorderSquareMiddle;
    Square BottomBorderSquareRight;
    Square RightBorderSquareTop;
    Square RightBorderSquareBottom;
    
    Rect captionShadow;
    Rect captionBox;
    
    Text captionText;


    
    public void init(){
        lang = new AnimalScript("Floyd-Steinberg Dithering", "Sandra Schaefer, Jonas Schatz", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }
    
    public FloydSteinbergGenerator(Locale locale) {
    	loc = locale;
    }
    
    public FloydSteinbergGenerator() {
    	loc = Locale.GERMAN;
    }
    
    // Create and return the properties of a grey square with a certain brightness
    public SquareProperties createGreySquare(int brightness) {
    	SquareProperties sp = new SquareProperties();
    	sp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(brightness, brightness, brightness) );
    	sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    	return sp;
    }
    
    // Create and return the properties of a grey square with a certain color
    public SquareProperties createColorSquare(Color color) {
    	SquareProperties sp = new SquareProperties();
    	sp.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
    	sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    	return sp;
    }
   
    
    // Check if an int-array contains a value
    public static boolean contains(int[] myArray, int toCheck) {
    	for(int i:myArray) {
    		if(i == toCheck) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    // For better overview: Contains code to create the description page
    public void generateDescriptionPage() {
    	
    	// Caption
    	captionShadow = lang.newRect(new Coordinates(23, 23), new Coordinates(383, 53), "descriptionCaptionShadow", null, ShadowProperties);
    	captionBox = lang.newRect(new Coordinates(17, 17), new Coordinates(377, 47), "descriptionShadowBackground", null, BackgroundBoxProperties);
    	captionText = lang.newText(new Coordinates(20, 20), (loc == Locale.GERMANY) ? "Floyd-Steinberg Dithering - Einfuehrung" : "Floyd-Steinberg Dithering - Introduction", "captionText", null, captionStyle);
    	
    	// Top Text
    	lang.newText(new Offset(0, 40, "captionText", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "Wie zuvor beschrieben wird Dithering verwendet, um das Auge beim Uebergang zu einer niedrigeren Farbtiefe moeglichst " :"As mentioned before, dithering is commonly used to deceive the eye when color depth is reduced. In this animation, a ",
				"description1", null);
    	lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "gut zu taeuschen. Im konkreten Fall erfolgt der Uebergang von 8-bit Graustufenbildern auf 1-bit schwarz-weiss Bilder. " : "conversion from 8-bit grayscale images to 1-bit black-and-white images is performed. Thus, every pixel is either", 
    			"description2", null);
    	lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "Ein Pixel wird also entweder zu schwarz (0) abgerundet oder auf weiss (255) aufgerundet: " : "rounded off to black (0) or rounded up to white (255):", 
    			"description3", null);

    	// Example for rounding up and down
    	lang.newSquare(new Offset(40, 40, "description3", AnimalScript.DIRECTION_NW), 20, "descriptionSquare1", null, createGreySquare(120));
    	lang.newSquare(new Offset(40, 0, "descriptionSquare1", AnimalScript.DIRECTION_NE), 20, "descriptionSquare2", null, createGreySquare(0));
    	lang.newSquare(new Offset(100, 0, "descriptionSquare2", AnimalScript.DIRECTION_NE), 20, "descriptionSquare3", null, createGreySquare(191));
    	lang.newSquare(new Offset(40, 0, "descriptionSquare3", AnimalScript.DIRECTION_NE), 20, "descriptionSquare4", null, createGreySquare(255));
    	
    	PolylineProperties arrowProperty = new PolylineProperties();
    	arrowProperty.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    	lang.newPolyline(new Offset[]{
    			new Offset(5, 0, "descriptionSquare1", AnimalScript.DIRECTION_E), 
    			new Offset(35, 0, "descriptionSquare1", AnimalScript.DIRECTION_E)	
    	}, "descriptionArrow1", null, arrowProperty);
    	lang.newPolyline(new Offset[]{
    			new Offset(5, 0, "descriptionSquare3", AnimalScript.DIRECTION_E), 
    			new Offset(35, 0, "descriptionSquare3", AnimalScript.DIRECTION_E)	
    	}, "descriptionArrow1", null, arrowProperty);
    	
    	// Example for error distribution
    	lang.nextStep();
    	lang.newText(new Offset(0, 100, "description3", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "Der so entstehende Fehler wird in einem bestimmten Verhaeltnis auf die umliegenden Pixel verteilt: " : "The error introduced by rounding is distributed in a certain ratio over the neighboring pixel: ", 
    			"description4", null);
    	lang.newSquare(new Offset(40, 40, "description4", AnimalScript.DIRECTION_NW), 20, "descriptionCloseup1", null, createGreySquare(255));
    	Square descriptionCloseup2  = lang.newSquare(new Offset(20, 0, "descriptionCloseup1", AnimalScript.DIRECTION_NW), 20, "descriptionCloseup2", null, createGreySquare(191));
    	descriptionCloseup2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, currentPxHighlightColor, null, null);
    	Square descriptionCloseup3 = lang.newSquare(new Offset(20, 0, "descriptionCloseup2", AnimalScript.DIRECTION_NW), 20, "descriptionCloseup3", null, createGreySquare(255));
    	Square descriptionCloseup4 = lang.newSquare(new Offset(0, 20, "descriptionCloseup1", AnimalScript.DIRECTION_NW), 20, "descriptionCloseup4", null, createGreySquare(255));
    	Square descriptionCloseup5 = lang.newSquare(new Offset(20, 0, "descriptionCloseup4", AnimalScript.DIRECTION_NW), 20, "descriptionCloseup5", null, createGreySquare(255));
    	Square descriptionCloseup6 = lang.newSquare(new Offset(20, 0, "descriptionCloseup5", AnimalScript.DIRECTION_NW), 20, "descriptionCloseup6", null, createGreySquare(255));
    	
    	lang.nextStep();
    	Text description5 = lang.newText(new Offset(3*20, 10, "descriptionCloseup3", AnimalScript.DIRECTION_NE), (loc == Locale.GERMANY) ? "Der rechte Pixel erhaelt 7/16" : "7/16 go to the pixel on the right", "description5", null);
    	descriptionCloseup3.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
    	descriptionCloseup2.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(219, 219, 219), null, null);
    	descriptionCloseup3.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(227, 227, 227), null, null);
    	
    	lang.nextStep();
    	descriptionCloseup3.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
    	description5.setText((loc == Locale.GERMANY) ? "Der untere-linke Pixel erhaelt 3/16" : "3/16 go to the pixel on the bottom left", null, null);
    	descriptionCloseup4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
    	descriptionCloseup2.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(231, 231, 231), null, null);
    	descriptionCloseup4.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(243, 243, 243), null, null);
    	
    	lang.nextStep();
    	descriptionCloseup4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
    	description5.setText((loc == Locale.GERMANY) ? "Der untere-mittlere Pixel erhaelt 5/16" : "5/16 go to the pixel below", null, null);
    	descriptionCloseup5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
    	descriptionCloseup2.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(251, 251, 251), null, null);
    	descriptionCloseup5.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(243, 243, 243), null, null);
    	
    	lang.nextStep();
    	descriptionCloseup5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
    	description5.setText((loc == Locale.GERMANY) ? "Der untere-rechte Pixel erhaelt 1/16" : "1/16 go to the pixel on the bottom right", null, null);
    	descriptionCloseup6.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
    	descriptionCloseup2.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 255, 255), null, null);
    	descriptionCloseup6.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(251, 251, 251), null, null);
    	
    	// End
    	lang.nextStep();
    	descriptionCloseup6.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
    	lang.newText(new Offset(0, 2*20+100, "description4", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "Welchen Einfluss dies hat, wenn es auf alle Pixel eines Bildes angewendet wird, ist in der folgenden Visualisierung zu sehen." : "The result of applying this process to all pixel of an image in sequence is shown in the following visualization.", 
    			"description6", null);
    }

    
    // For better overview: Contains code to create the summary page
    public void generateSummaryPage(int[][] originalImage, int[][] image) {
    	
    	
    	captionShadow.show();
    	captionBox.show();
    	captionText.show();
    	captionText.setText((loc == Locale.GERMANY) ? "Floyd-Steinberg Dithering - Abschluss" : "Floyd-Steinberg Dithering - Summary", null, null);
    	lang.newText(new Offset(0, 40, "captionText", AnimalScript.DIRECTION_SW), 
    			(loc == Locale.GERMANY) ? "Zusammenfassend sind das urspruengliche Bild und das generierte Muster hier nochmal gezeigt: " : "Finally, the original image and the created pattern are shown side by side:", "summary1", null);
    	
    	// Show the new and original image
    	float sumNewImage = 0;
    	float sumOriginalImage = 0;
		for(int r = 0; r < nRows; r++) {
			for(int c = 0; c < nCols; c++) {
				lang.newSquare(new Offset(40 + c*pixelSize, 40 + r*pixelSize, "summary1", AnimalScript.DIRECTION_SW), pixelSize, "summaryPixelOriginal_" + r + "_" + c, null, createGreySquare(originalImage[r][c]));
				sumOriginalImage += originalImage[r][c];
				lang.newSquare(new Offset(40 + (c+nCols+2)*pixelSize, 40 + r*pixelSize, "summary1", AnimalScript.DIRECTION_SW), pixelSize, "summaryPixel_" + r + "_" + c, null, createGreySquare(image[r][c]));
				sumNewImage += image[r][c];
			}
		}
		
		// Calculate the average value of each image
		sumNewImage /= nRows*nCols;
		sumOriginalImage /= nRows*nCols;
		
		// Show how big the error was
		lang.newText(new Offset(-40, 40, "summaryPixelOriginal_" + (nRows-1) + "_0", AnimalScript.DIRECTION_SW), 
				(loc == Locale.GERMANY) ? "Bei dieser Diskretisierung wird in jedem Schritt ein kleiner Fehler gemacht." : "When discretizing, a small error might be introduced in every step.", "summary2", null);
		lang.newText(new Offset(0, 20, "summary2", AnimalScript.DIRECTION_NW), (loc == Locale.GERMANY) ? "Die mittlere Helligkeit des urspuenglichen Bildes betraegt " + sumOriginalImage + "." : "The average pixel value of the oiriginal image is "  + sumOriginalImage + ".", "summary3", null);
		lang.newText(new Offset(0, 20, "summary3", AnimalScript.DIRECTION_NW), (loc == Locale.GERMANY) ? "Die mittlere Helligkeit des neuen Bildes betraegt " + sumNewImage + "." : "The average pixel value of the new image is "  + sumNewImage + ".", "summary4", null);
		
		// Show possible explanations for the error
		lang.newText(new Offset(0, 40, "summary4", AnimalScript.DIRECTION_NW), (loc == Locale.GERMANY) ? "Dies kann mehrere Gruende haben:  " : "This can happen for various reasons:", "summary5", null);
		lang.newText(new Offset(0, 20, "summary5", AnimalScript.DIRECTION_NW), (loc == Locale.GERMANY) ? "  - Bei jeder Umverteilung des Fehler kommt es zu Rundungsungenauigkeiten." : " - Rounding errors are introduced in every step.", "summary6", null);
		lang.newText(new Offset(0, 20, "summary6", AnimalScript.DIRECTION_NW), (loc == Locale.GERMANY) ? "  - Zur Vereinfachung der Darstellung wurden keine Zuordnungen am Rand des Bildes vorgenommen." : " - for reasons of simplicity, there is no distribution of error at the image borders.", "summary7", null);
		
		
		
    	
    	
    }
    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
    	// --------------------
    	//     Parameters
    	// --------------------
    	
    	// Get user-changeable parameters from the XML file
    	currentPxStyle = (SquareProperties)props.getPropertiesByName("currentPx"); // color of the current pixel (default: light blue)
        neighborPx = (SquareProperties)props.getPropertiesByName("neighborPx"); // color of the neighboring pixels (default: green)
        Border = (RectProperties)props.getPropertiesByName("Border"); // color of the border pixels (default: red)
        imageValues = (int[][])primitives.get("imageValues"); // grey values of the image we are working with
        pixelSize = (Integer)primitives.get("pixelSize"); // how large are the pixels of the image displayed?
        closeupPxSize = (Integer)primitives.get("closeupPxSize"); // how large are the pixels of the magnification / closeup displayed?
        sourceCodeFontProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeFontProperties"); // font and highlight color of source code
        closeupFontProperties = (TextProperties)props.getPropertiesByName("closeupFontProperties"); // font used in the close-up
        numberOfQuestions = (Integer)primitives.get("numberOfQuestions"); // how many questions are asked during the visualization?
                
        
        // Derived parameters and variables
        nRows = imageValues.length;
        nCols = imageValues[0].length;
        
    	int[][] image = new int[nRows][nCols]; //deep copy of image, needed for bugfix
    	int[][] originalImage = new int[nRows][nCols]; // keep a copy of the image to show at the end
        for(int r = 0; r < nRows; r++) {
			for(int c = 0; c < nCols; c++) {
				image[r][c] = imageValues[r][c];
				originalImage[r][c] = imageValues[r][c];
			}
        }
        

        
        

        // Prepare the questions
        int[] questionIndices = ThreadLocalRandom.current().ints(0, nRows*nCols).distinct().limit(numberOfQuestions).toArray(); // random indices at which the questions will be asked
        	// https://stackoverflow.com/questions/8115722/generating-unique-random-numbers-in-java
        	// http://www.java2s.com/Tutorials/Java/java.util.stream/IntStream/IntStream.toArray_.htm        
        FillInBlanksQuestionModel[] questions = new FillInBlanksQuestionModel[numberOfQuestions]; // array containing the question-objects
        int currentQuestionIndex = 0; // just a counter to keep track where in the array of question-objects we are
        
        
        // Define some styles
        currentPxHighlightColor = (Color)(currentPxStyle.getItem("color").get());
        neighborPxHighlightColor = (Color)(neighborPx.getItem("color").get());
        borderColor = (Color)(Border.getItem("color").get());
        
        captionStyle = new TextProperties();
        captionStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 0, 20));
        
        smallCaptionStyle = new TextProperties();
        smallCaptionStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 0, 15));
        
        ShadowProperties = new RectProperties();
        ShadowProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        ShadowProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
        ShadowProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        
        BackgroundBoxProperties = new RectProperties();
        BackgroundBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        BackgroundBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(253, 253, 150));
        
    	
        
        
        
    	// --------------------
    	//  Description Page
    	// --------------------
        
    	generateDescriptionPage();    	
    	lang.nextStep("Animation Start");
    	lang.hideAllPrimitives();
    	
    	
    	
    	
    	// --------------------
    	//  Set up the Animation
    	// --------------------
    	
    	// 0. Caption
    	captionShadow.show();
    	captionBox.show();
    	captionText.show();
    	captionText.setText((loc == Locale.GERMANY) ? "Floyd-Steinberg Dithering - Algorithmus" : "Floyd-Steinberg Dithering - Algorithm", null, null);
    	
 		// 1. Create image
 		//lang.newText(new Coordinates(imageOffset, imageOffset-30), "Image", "imageText", null);
 		
 		Square[][] pixels = new Square[nRows][nCols];

		for(int r = 0; r < nRows; r++) {
			for(int c = 0; c < nCols; c++) {
				int color = image[r][c];
				pixels[r][c] = lang.newSquare(
						new Offset(c*pixelSize, r*pixelSize + 40, "captionText", AnimalScript.DIRECTION_SW), 
						pixelSize, "pixel_" + r + "_" + c, null, createGreySquare(color));
			}
		}
		
		
		// 2. Create the Close-up
		
		Square[][] closeup = new Square[2][3];
		Text[][] closeupTextOriginal = new Text[2][3];
		Text[][] closeupTextDifference = new Text[2][3];

		for(int r = 0; r < 2; r++) {
			for(int c = 0; c < 3; c++) {	
				//closeup[r][c] = lang.newSquare(new Offset(
				//		(nCols*pixelSize - 3*closeupPxSize)/2 + c*closeupPxSize,  r*closeupPxSize + 40, "pixel_" + (nRows-1) + "_" + 0, AnimalScript.DIRECTION_SW),
				//		closeupPxSize, "closeup_" + r + "_" + c, null, createGreySquare(255));
				closeup[r][c] = lang.newSquare(new Offset(
						c*closeupPxSize, r*closeupPxSize + 40, "pixel_" + (nRows-1) + "_" + 0, AnimalScript.DIRECTION_SW),
						closeupPxSize, "closeup_" + r + "_" + c, null, createGreySquare(255));
				
				closeupTextOriginal[r][c]   = lang.newText(  new Offset(2, 5, "closeup_" + r + "_" + c, AnimalScript.DIRECTION_NW), "", "closeupText_" + r + "_" + c, null, closeupFontProperties);
				closeupTextDifference[r][c] = lang.newText(  new Offset(2, 17, "closeup_" + r + "_" + c, AnimalScript.DIRECTION_NW), "", "closeupText_" + r + "_" + c, null, closeupFontProperties);
			}
		}
		
		//lang.newText(new Offset(0, 20, "closeup_1_0", AnimalScript.DIRECTION_SW), "Closeup", "closeupText", null);
		
		
		
		// 3. Sourcecode Listing
		SourceCode src = lang.newSourceCode(new Offset(Math.max(250,  Math.max(nCols * pixelSize, 3 * closeupPxSize) + 40), -10, "pixel_0_0", AnimalScript.DIRECTION_NE), "sourceCode", null, sourceCodeFontProperties);
		//SourceCode src = lang.newSourceCode(new Offset(50, -10, "pixel_0_" + (nCols-1), AnimalScript.DIRECTION_NE), "sourceCode", null, sourceCodeFontProperties);
		src.addCodeLine("public static void performFloydSteinbergDithering(int[][] image, int height, int width) {", null, 0, null); // 0
		src.addCodeLine("    for(int row = 0; row < height; row++) {", null, 0, null); // 1
		src.addCodeLine("        for(int col = 0; col < width; col++) {", null, 0, null); // 2
		src.addCodeLine("        ", null, 0, null); // 3
		src.addCodeLine("            // Border Detection", null, 0, null); // 4
		src.addCodeLine("            boolean rightBorder = (col == height-1) ? true : false;", null, 0, null); // 5
		src.addCodeLine("            boolean bottomBorder = (row == width-1) ? true : false;", null, 0, null); // 6
		src.addCodeLine("            boolean leftBorder = (col == 0) ? true : false;", null, 0, null); // 7
		src.addCodeLine("            ", null, 0, null); // 8
		src.addCodeLine("            // Calculating the Error", null, 0, null); // 9
		src.addCodeLine("            int originalValue = image[row][col];", null, 0, null); // 10
		src.addCodeLine("            int newValue =  (originalValue > 127) ? 255 : 0;", null, 0, null); // 11
		src.addCodeLine("            int error = originalValue - newValue;", null, 0, null); // 12
		src.addCodeLine("            image[row][col] = newValue;", null, 0, null); // 13
		src.addCodeLine("            ", null, 0, null); // 14
		src.addCodeLine("            // Distribute the error", null, 0, null); // 15
		src.addCodeLine("            if (!rightBorder)                  image[row][col+1]   += (int) Math.round(error*0.4375);", null, 0, null); // 16
		src.addCodeLine("            if (!bottomBorder && !leftBorder)  image[row+1][col-1] += (int) Math.round(error*0.1875);", null, 0, null); // 17
		src.addCodeLine("            if (!bottomBorder)                 image[row+1][col]   += (int) Math.round(error*0.3125);", null, 0, null); // 18
		src.addCodeLine("            if (!bottomBorder && !rightBorder) image[row+1][col+1] += (int) Math.round(error*0.0625);", null, 0, null); // 19
		src.addCodeLine("        }", null, 0, null); // 20
		src.addCodeLine("    }", null, 0, null); // 21
		src.addCodeLine("}", null, 0, null); // 22
		
		// 4. Legend
		lang.newText(new Offset(0, 80 + 2*closeupPxSize, "pixel_" + (nRows-1) + "_" + 0, AnimalScript.DIRECTION_SW), (loc == Locale.GERMANY) ? "Legende: " : "Legend:", "legendText", null, smallCaptionStyle);
		lang.newSquare(new Offset(10, 10, "legendText", AnimalScript.DIRECTION_SW), pixelSize, "legendCurrentPxSquare", null, currentPxStyle);
		lang.newText(new Offset(20, -3, "legendCurrentPxSquare", AnimalScript.DIRECTION_NE), (loc == Locale.GERMANY) ? "Aktuell betrachteter Pixel" : "Current pixel", "currentPixelLegendText", null);
		
		
		lang.newSquare(new Offset(0, 15, "legendCurrentPxSquare", AnimalScript.DIRECTION_SW), pixelSize, "legendNeighborPixelSquare", null, currentPxStyle).changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);;
		lang.newText(new Offset(20, -3, "legendNeighborPixelSquare", AnimalScript.DIRECTION_NE), (loc == Locale.GERMANY) ? "Benachbarte Pixel" : "Neighboring pixels", "legendNeighborPixelText", null);
		lang.newSquare(new Offset(0, 15, "legendNeighborPixelSquare", AnimalScript.DIRECTION_SW), pixelSize, "legendBorderPixelSquare", null, createColorSquare(borderColor));
		lang.newText(new Offset(20, -3, "legendBorderPixelSquare", AnimalScript.DIRECTION_NE), (loc == Locale.GERMANY) ? "Pixel ausserhalb des Bildes" : "Pixels outside the image", "legendBorderPixelText", null);
		
    	// --------------------
    	//  Actual Animation
    	// --------------------
		
		// Go through the pixels
		for(int r = 0; r < nRows; r++) {
			for(int c = 0; c < nCols; c++) {
				
				// --------------------
				// Before anything happens: 
				// - Calculate all values
				// - Highlight the cells we are currently looking at
				// - Write the current pixel values into the close-up
				// - Move the magnification lines
				// - maybe ask the question

				// Border Detection
				boolean rightBorder = (c == nCols-1) ? true : false;
				boolean bottomBorder = (r == nRows-1) ? true : false;
				boolean leftBorder = (c == 0) ? true : false;
				
				// Calculating the error
				int originalValue = image[r][c];
				int newValue =  (originalValue > 127) ? 255 : 0;
				int clampedValue;
				int change;
				int error = originalValue - newValue;
				

				// Highlight the active cell in the image 
				pixels[r][c].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, currentPxHighlightColor, null, null);
				closeupTextOriginal[0][1].setText(Integer.toString(image[r][c]), null, null);
				
				
				// Color the cells we are working with
				if (!rightBorder) {
					pixels[r][c+1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
					closeupTextOriginal[0][2].setText(Integer.toString(image[r][c+1]), null, null);
					closeup[0][2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);

				}
				
				if (!bottomBorder && !leftBorder) {
					pixels[r+1][c-1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
					closeupTextOriginal[1][0].setText(Integer.toString(image[r+1][c-1]), null, null);
					closeup[1][0].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
				}
				
				if (!bottomBorder) {
					pixels[r+1][c].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
					closeupTextOriginal[1][1].setText(Integer.toString(image[r+1][c]), null, null);
					closeup[1][1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
				}
				
				if (!bottomBorder && !rightBorder) {
					pixels[r+1][c+1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
					closeupTextOriginal[1][2].setText(Integer.toString(image[r+1][c+1]), null, null);
					closeup[1][2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, neighborPxHighlightColor, null, null);
				}
				
				
				
				// Color the borders
				if (rightBorder) {
					closeup[0][2].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, borderColor, null, null);
					closeup[1][2].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, borderColor, null, null);
					RightBorderSquareTop = lang.newSquare(new Offset(pixelSize, 0, "pixel_" + r + "_" + (nCols-1), AnimalScript.DIRECTION_NW), pixelSize, "topRightBorderSquare", null, createColorSquare(borderColor));
					RightBorderSquareBottom = lang.newSquare(new Offset(pixelSize, pixelSize, "pixel_" + r + "_" + (nCols-1), AnimalScript.DIRECTION_NW), pixelSize, "bottomRightBorderSquare", null, createColorSquare(borderColor));
					src.highlight(5);
				} 
				
				if (leftBorder) {
					closeup[0][0].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, borderColor, null, null);
					closeup[1][0].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, borderColor, null, null);
					LeftBorderSquareTop = lang.newSquare(new Offset(-pixelSize, 0, "pixel_" + r + "_0", AnimalScript.DIRECTION_NW), pixelSize, "topLeftBorderSquare", null, createColorSquare(borderColor));
					LeftBorderSquareBottom = lang.newSquare(new Offset(-pixelSize, pixelSize, "pixel_" + r + "_0", AnimalScript.DIRECTION_NW), pixelSize, "bottomLeftBorderSquare", null, createColorSquare(borderColor));
					src.highlight(7);
				} 
				
				if (bottomBorder) {
					closeup[1][0].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, borderColor, null, null);
					closeup[1][1].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, borderColor, null, null);
					closeup[1][2].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, borderColor, null, null);
					BottomBorderSquareLeft = lang.newSquare(new Offset(-pixelSize, 0, "pixel_" + (nRows-1) + "_" + c, AnimalScript.DIRECTION_SW), pixelSize, "bottomLeftBorderSquare", null, createColorSquare(borderColor));
					BottomBorderSquareMiddle = lang.newSquare(new Offset(0, 0, "pixel_" + (nRows-1) + "_" + c, AnimalScript.DIRECTION_SW), pixelSize, "bottomLeftBorderSquare", null, createColorSquare(borderColor));
					BottomBorderSquareRight = lang.newSquare(new Offset(pixelSize, 0, "pixel_" + (nRows-1) + "_" + c, AnimalScript.DIRECTION_SW), pixelSize, "bottomLeftBorderSquare", null, createColorSquare(borderColor));
					src.highlight(6);
				}
				
				closeup[0][1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, currentPxHighlightColor, null, null);
				
				
				// Create the Polylines indicating the magnification
				
				PolylineProperties plProp = new PolylineProperties();
				plProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);
				Polyline leftLine = lang.newPolyline(new Offset[] {
						new Offset(-pixelSize, 0, "pixel_" + r + "_" + c, AnimalScript.DIRECTION_NW), 
						new Offset(0, 0, "closeup_0_0", AnimalScript.DIRECTION_NW)
				}, "leftLine", null, plProp);
				
				Polyline rightLine = lang.newPolyline(new Offset[] {
						new Offset(pixelSize, 0, "pixel_" + r + "_" + c, AnimalScript.DIRECTION_NE), 
						new Offset(0, 0, "closeup_0_2", AnimalScript.DIRECTION_NE)
				}, "leftLine", null, plProp);
				
				
				// Ask the questions
				if (contains(questionIndices, r*nCols + c)) {
					//System.out.println(r*nCols + c);
					questions[currentQuestionIndex] = new FillInBlanksQuestionModel("newValueQuestion_" + currentQuestionIndex);
					
					
					if (loc == Locale.GERMANY) {
						// By default, ask what the value in the cell at the top-right will be
						if (!rightBorder) { 
							questions[currentQuestionIndex].setPrompt("Der aktuelle Pixel hat den Wert " + image[r][c] + ", der Pixel rechts danaben den Wert " + image[r][c+1] + ". \n"
									+ "Welchen Wert wird der Pixel direkt rechts vom aktuellen Pixel erhalten? "
									+ "Erinnerung: Er erhaelt 7/16 des Fehlers!");
							int solution = image[r][c+1] + (int) Math.round(error*0.4375);
							questions[currentQuestionIndex].addAnswer(Integer.toString(solution), 1, "Der neue Pixelwert betraegt " + Integer.toString(solution));
							
							
						} else {
							questions[currentQuestionIndex].setPrompt("Der aktuelle Pixel hat den Wert " + image[r][c] + ", der Pixel links darunter den Wert " + image[r+1][c-1] + ". \n"
									+ "Welchen Wert wird der Pixel links-unterhalb vom aktuellen Pixel erhalten? "
									+ "Erinnerung: Er erhaelt 3/16 des Fehlers!");
							int solution = image[r+1][c-1] + (int) Math.round(error*0.1875);
							questions[currentQuestionIndex].addAnswer(Integer.toString(solution), 1, "Der neue Pixelwert betraegt " + Integer.toString(solution));
						}
						lang.addFIBQuestion(questions[currentQuestionIndex]);
						currentQuestionIndex++;
					} else {
						
						if (!rightBorder) { 
							questions[currentQuestionIndex].setPrompt("The current pixel has a brightness value of " + image[r][c] + ", the pixel directly to the right a value of " + image[r][c+1] + ". \n"
									+ "What will be the new value of the pixel directly to the right? "
									+ "Reminder: It will receive 7/16 of the error!");
							int solution = image[r][c+1] + (int) Math.round(error*0.4375);
							questions[currentQuestionIndex].addAnswer(Integer.toString(solution), 1, "The new value is " + Integer.toString(solution));
							
							
						} else {
							questions[currentQuestionIndex].setPrompt("The current pixel has a brightness value of " + image[r][c] + ", the pixel to the bottom left a value of " + image[r+1][c-1] + ". \n"
									+ "What will be the new value of the pixel to the bottom left? "
									+ "Reminder: It will receive 3/16 of the error!");
							int solution = image[r+1][c-1] + (int) Math.round(error*0.1875);
							questions[currentQuestionIndex].addAnswer(Integer.toString(solution), 1, "The new value is " + Integer.toString(solution));
						}
						lang.addFIBQuestion(questions[currentQuestionIndex]);
						currentQuestionIndex++;
					}
				}
				
				
				
				
				// --------------------
				// During the step:
				// - Apply the color changes (to visible image and the underlying data)
				// - Write the changes into the closeup
				if(c == 0) {
					lang.nextStep((loc == Locale.GERMANY) ? "Zeile " + r : "Row " + r);
				} else {
					lang.nextStep();
				}

				
				// Undo the coloring of the border pixels
				if(rightBorder) {
					RightBorderSquareTop.hide();
					RightBorderSquareBottom.hide();
					src.unhighlight(5);
				}
				
				
				if(bottomBorder) {
					BottomBorderSquareLeft.hide();
					BottomBorderSquareMiddle.hide();
					BottomBorderSquareRight.hide();
					src.unhighlight(6);
				}
				
				if(leftBorder) {
					LeftBorderSquareTop.hide();
					LeftBorderSquareBottom.hide();
					src.unhighlight(7);
				}
				
				// Update the image data
				image[r][c] = newValue;
				pixels[r][c].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(newValue, newValue, newValue), null, null);
				closeupTextOriginal[0][1].setText(Integer.toString(image[r][c]), null, null);
				closeupTextDifference[0][1].setText(((-error>0)? "+" : "") +  Integer.toString(-error), null, null);
				closeupTextOriginal[0][1].setText(Integer.toString(originalValue) + ((error>0)? " " : "+") + Integer.toString(-error), null, null); 
				closeupTextDifference[0][1].setText("= " + Integer.toString(newValue), null, null); 
								
				
				// Distribute the error and highlight the corresponding pixels
				if (!rightBorder) {
					change = (int) Math.round(error*0.4375);	// calculate the fraction of the error this pixel gets
					originalValue = image[r][c+1];
					newValue = image[r][c+1] + change;	// calculate the new value
					image[r][c+1] = newValue;			// write the new value to the image
					clampedValue = Math.max(0, Math.min(255, newValue)); // new value can be <0 or >255 - for display restrict the value to [0, 255]
					pixels[r][c+1].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(clampedValue, clampedValue, clampedValue), null, null); // adjust the color according to the value
					closeupTextOriginal[0][2].setText(Integer.toString(originalValue) + ((error>=0)? "+" : " ") + Integer.toString(change), null, null); // write the new value into the closeup
					closeupTextDifference[0][2].setText("= " + Integer.toString(newValue), null, null); // write the change into the closeup
					src.highlight(16); 					// highlight the line in the source code
				}
				
				if (!bottomBorder && !leftBorder) {
					change = (int) Math.round(error*0.1875);
					originalValue = image[r+1][c-1];
					newValue = image[r+1][c-1] + change;
					image[r+1][c-1] = newValue;
					clampedValue = Math.max(0, Math.min(255, newValue));
					pixels[r+1][c-1].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(clampedValue, clampedValue, clampedValue), null, null);
					closeupTextOriginal[1][0].setText(Integer.toString(originalValue) + ((error>=0)? "+" : " ") + Integer.toString(change), null, null); 
					closeupTextDifference[1][0].setText("= " + Integer.toString(newValue), null, null); 
					src.highlight(17);
				}
				
				if (!bottomBorder) {
					change = (int) Math.round(error*0.3125);
					originalValue = image[r+1][c];
					newValue = image[r+1][c] + change;
					image[r+1][c] = newValue;
					clampedValue = Math.max(0, Math.min(255, newValue));
					pixels[r+1][c].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(clampedValue, clampedValue, clampedValue), null, null);
					closeupTextOriginal[1][1].setText(Integer.toString(originalValue) + ((error>=0)? "+" : " ") + Integer.toString(change), null, null); 
					closeupTextDifference[1][1].setText("= " + Integer.toString(newValue), null, null); 
					src.highlight(18);
				}
				
				if (!bottomBorder && !rightBorder) {
					change =(int) Math.round(error*0.0625);
					originalValue = image[r+1][c+1];
					newValue = image[r+1][c+1] + change;	
					image[r+1][c+1] = newValue;	
					clampedValue = Math.max(0, Math.min(255, newValue));
					pixels[r+1][c+1].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(clampedValue, clampedValue, clampedValue), null, null);
					closeupTextOriginal[1][2].setText(Integer.toString(originalValue) + ((error>=0)? "+" : " ") + Integer.toString(change), null, null); 
					closeupTextDifference[1][2].setText("= " + Integer.toString(newValue), null, null); 
					src.highlight(19);
				}
				

				
				
				
				
				// ++++++++++++++++++++++++
				// In preparation for the next step: 
				// - Undo all the color changes
				// - Delete the magnification lines
				// +++++++++++++++++++++++++
				lang.nextStep();
				
				
				
				
				// Undo the color changes in the closeup
				for(int i = 0; i < 2; i++) {
					for(int j = 0; j < 3; j++) {
						closeup[i][j].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null, null);
						closeupTextOriginal[i][j].setText("", null, null);
						closeupTextDifference[i][j].setText("", null, null);
					}
				}

				// Undo the highlight of the current pixel
				pixels[r][c].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				
				// Undo the highlights of the neighboring pixels
				if (!rightBorder) {
					pixels[r][c+1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					closeup[0][2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					src.unhighlight(16);
				}
				
				if (!bottomBorder && !leftBorder) {
					pixels[r+1][c-1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					closeup[1][0].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					src.unhighlight(17);
				}
				
				if (!bottomBorder) {
					pixels[r+1][c].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					closeup[1][1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					src.unhighlight(18);
				}
				
				if (!bottomBorder && !rightBorder) {
					pixels[r+1][c+1].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					closeup[1][2].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					src.unhighlight(19);
				}
				
				
				
				// Delete (hide) the lines
				leftLine.hide();
				rightLine.hide();
			
			}
		}
		
		
		
    	// --------------------
    	//    Summary Page
    	// --------------------
		lang.hideAllPrimitives();
    	generateSummaryPage(originalImage, image);    	
    	lang.nextStep("Summary Page");
    	
		
		
		lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Floyd-Steinberg Dithering";
    }

    public String getAlgorithmName() {
        return "Floyd-Steinberg";
    }

    public String getAnimationAuthor() {
        return "Sandra Schaefer, Jonas Schatz";
    }
    
    
    
    
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {


        imageValues = (int[][])primitives.get("imageValues"); // grey values of the image we are working with
        pixelSize = (Integer)primitives.get("pixelSize"); // how large are the pixels of the image displayed?
        closeupPxSize = (Integer)primitives.get("closeupPxSize"); // how large are the pixels of the magnification / closeup displayed?
        numberOfQuestions = (Integer)primitives.get("numberOfQuestions"); // how many questions are asked during the visualization?
        
        
        // Check if grey values are between 0 and 255
        nRows = imageValues.length;
        nCols = imageValues[0].length;
        for(int r = 0; r < nRows; r++) {
			for(int c = 0; c < nCols; c++) {
				if (imageValues[r][c] < 0 || imageValues[r][c] > 255) {
					throw new IllegalArgumentException("imageValues: Pixel-Werte muessen zwischen 0 und 255 liegen. Fehler in Reihe " + r + ", Spalte " + c + ", Wert von " + imageValues[r][c]);
				}
			}
        }
        
        
        // Check if the number of questions is reasonable
        if(numberOfQuestions < 0) {
        	throw new IllegalArgumentException((loc == Locale.GERMANY) ? "numberOfQuestions: Es muss eine positive Anzahl an Fragen gestellt werden" : "numberOfQuestions: There has to be a positive number of questions.");
        }
        if(numberOfQuestions > 20) {
        	throw new IllegalArgumentException((loc == Locale.GERMANY) ? "numberOfQuestions: Mehr als 20 Fragen sind wenig sinnvoll" : "numberOfQuestions: More than 20 questions would be counterproductive.");
        }
        
        
        // Check closeupPxSize
    	if(closeupPxSize < 50) {
    		throw new IllegalArgumentException((loc == Locale.GERMANY) ? "closeupPxSize sollte >50 sein, um Berechnungen komplett anzuzeigen." : "closeupPxSize should be >50, to see everything of the calculations.");
    	}
    	if(closeupPxSize > 150) {
    		throw new IllegalArgumentException((loc == Locale.GERMANY) ? "closeupPxSize sollte <150 sein, da ueberproportional dimensioniert." : "closeupPxSize should be <150 to be dimensioned proportionally to the text.");
    	}
    	
    	if(pixelSize < 5) {
    		throw new IllegalArgumentException((loc == Locale.GERMANY) ? "pixelSize sollte mindestens 5 Pixel sein, um ueberhaupt etwas erkennen zu koennen." : "pixelSize should be at least 5 to see enough detail.");
    	}
    	
    	return true;
    }
    
    

    public String getDescription(){
    	if (loc == Locale.GERMANY) {
	        return "In der Computer Grafik wird Dithering verwendet, um bei Bildern mit geringer Farbtiefe die Illusion \n"
			 +"einer groe�eren Farbtiefe zu erzeugen. \n "
			 + "\n"      		
			 +"Der Floyd-Steinberg Algorithmus verringert die Farbtiefe eines Bildes, w�hrend dabei gleichzeitig \n"
			 +"versucht wird, den Farbeindruck m�glichst gut zu erhalten. Zu diesem Zweck wird nach dem Error- \n"
			 +"Diffusion-Verfahren gearbeitet. Der Algorithmus quantisiert der Reihe nach jeden Pixel (d.h. rundet \n"
			 +"ihn auf den �hnlichsten Wert der verringerten Farbpalette auf oder ab) und verteilt den dabei ent-\n"
			 +"standenen Fehler in einem festen Verh�ltnis auf die umliegenden Pixel. \n"
			 +"\n"
			 +"Quelle: https://de.wikipedia.org/wiki/Dithering_(Bildbearbeitung)";
    	} else {
	        return "In computer graphics, dithering is used to create the illusion of a higher color depth. \n "
			 + "\n"      		
			 +"The Floyd-Steinberg algorithm tries to decrease the color depth of an image while still main- \n"
			 +"taining the appearance of the original image. To do this, it deploys the principle of error \n"
			 +"diffusion. Sequentially, starting from the top left, every pixel is quantized (rounded off  \n"
			 +"or up to the most similar value of the reduced color palette) and the error is distributed \n"
			 +"to the neighboring pixels in a certain constant ratio. \n"
			 +"\n"
			 +"Source: https://en.wikipedia.org/wiki/Dither";
    	}
    }

    public String getCodeExample(){
        return "public static void performFloydSteinbergDithering(int[][] image, int height, int width) { \n "
        		+ "\n"
		 +"    for(int row = 0; row < height; row++) { \n"
		 +"        for(int col = 0; col < width; col++) { \n "
		 +"\n"
		 +"            // Border Detection \n"
		 +"            boolean rightBorder = (col == height-1) ? true : false; \n"
		 +"            boolean bottomBorder = (row == width-1) ? true : false; \n"
		 +"            boolean leftBorder = (col == 0) ? true : false; \n"
		 +"\n"
		 +"            // Calculating the Error \n"
		 +"            int originalValue = image[row][col]; \n"
		 +"            int newValue =  (originalValue > 127) ? 255 : 0; \n"
		 +"            int error = originalValue - newValue; \n"
		 +"            image[row][col] = newValue; \n"
		 +"\n"
		 +"            // Distribute the error \n"
		 +"            if (!rightBorder)                  image[row][col+1]   += (int) Math.round(error*0.4375); \n"
		 +"            if (!bottomBorder && !leftBorder)  image[row+1][col-1] += (int) Math.round(error*0.1875); \n"
		 +"            if (!bottomBorder)                 image[row+1][col]   += (int) Math.round(error*0.3125); \n"
		 +"            if (!bottomBorder && !rightBorder) image[row+1][col+1] += (int) Math.round(error*0.0625); \n"
		 +"        }\n"
		 +"    }\n"
		 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    
    //public static void main(String[] args) {
    //	Generator generator = new FloydSteinbergGenerator(Locale.GERMANY);
    //	Animal.startGeneratorWindow(generator);
    //}

}