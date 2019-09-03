package generators.graphics;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * 
 * @author Annika Beissler, Martin Hess, Nando Fuchs
 *
 */
public class AnnotatedDilation extends AnnotatedAlgorithm implements Generator {
	
	/**
	 * binary image for dilation
	 */
	private int[][] sourcePic;
	/**
	 * binary structureelement
	 */
	private int[][] struct;
	/**
	 * Comparsioncell in structureelement - x and y coordinate
	 */
	private int x;
	private int y;
	
	/**
	 * resulting / dilated image
	 */
	private int[][] resultPic;
	

	/**
	 * Primitives
	 */
	private IntMatrix source;
	private IntMatrix structure;
	private IntMatrix result;
	private Text header;
	
	/**
	 * Timing constraints
	 */
	private final int delay = 100;
	
	
	/**
	 * Algorithm description
	 */
	private final String DESCRIPTION = "Morphological Operations: dilation. The dilation is a morphologic operation often used in " +
	"Digital Image Processing. An example for this operation is to close holes in digital images, which could cause artifacts in " +
	"different other applications of digital image processing. To perform a dilation on an image a structureelement is needed. " +
	"This structureelement works like a stamp. The algorithm looks at each image pixel, decides wether the current pixel matches a specified " +
	"pixel in the structureelement and if this match is true, stamps the structureelement at the associating pixelposition in the result image. " +
	"This algorithm described here works only on binary images, like this operation orginally based on. For learning purposes this should be " +
	"sufficient, so please enter only the values 1 and 0 in the image and the structureelement!";

	
	/**
	 * This methods generates an animalscript which visualizes the dilation of a given image
	 * with a specified structure element. 
	 * 
	 * @param sourcePic the binary input image
	 * @param struct the binary structure element
	 * @param x the x position of the comparisonelement within the structurelement
	 * @param y the y position of the comparisonelement within the structurelement
	 */	
	public String generateScript(int[][] sourcePic, int[][] struct, int x, int y){
		// Initialize Data
		this.sourcePic = sourcePic;
		this.struct = struct;
		this.x = x;
		this.y = y;
		resultPic = new int[sourcePic.length][sourcePic[0].length];
		// Create elements for visualization
		
		initializeMatrices();
		// Perform algorithm
		dilation();
		// Returns the generated script
		return lang.toString();
	}
	
	/**
	 * Creates the topic for the animal visualization
	 */
	private void initializeHeader(){
		TextProperties textprop = new TextProperties();
		textprop.set(AnimationPropertiesKeys.CENTERED_PROPERTY, Boolean.FALSE);
		textprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		textprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.BOLD, 18));
		textprop.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, Boolean.FALSE);
		
		header = lang.newText(new Coordinates(10, 10), "Morphological Operations in Digital Image Processing: The dilation", "header", null, textprop);
	}
	
	/**
	 * Generates the Source code for the Visualization
	 */
	private void generateSourceCode(){
		// Create SourceCode Properties
		SourceCodeProperties scProps = new SourceCodeProperties();
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.BOLD, 14));
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

	    // create SourceCode
	    Offset scOffset = new Offset(0, 30, header, AnimalScript.DIRECTION_SW);
		sourceCode = lang.newSourceCode(scOffset, "sourceCode",null, scProps);	
	}
	
	/**
	 * This methods creates the Matrix Primitives for the visualization
	 */
	private void initializeMatrices(){
		
		MatrixProperties prop = new MatrixProperties();
		
		prop.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.ORANGE);		
		prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		prop.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,Color.BLACK);
		prop.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
		prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
		prop.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, Boolean.FALSE);
		prop.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		prop.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		
		// Source Matrix with header	   
		final Offset scOff = new Offset(0, 50, sourceCode, AnimalScript.DIRECTION_SW);
		source = lang.newIntMatrix(scOff, sourcePic, "source",null, prop);		
		final Offset scTextOff = new Offset(0, -20, source,  AnimalScript.DIRECTION_NW);
		lang.newText(scTextOff, "Source Image", "sourceMatText", null);	 
		
		// Structure element with header
		final Offset structOff = new Offset(50, 0, source, AnimalScript.DIRECTION_NE);
		structure = lang.newIntMatrix(structOff, struct, "structure", null,prop);
		final Offset structTextOff = new Offset(0, -20, structure, AnimalScript.DIRECTION_NW);
		lang.newText(structTextOff, "Structure Element", "structText", null);
		
		// Resulting image with header
		final Offset resultOff = new Offset(50, 0, structure, AnimalScript.DIRECTION_NE);
		result = lang.newIntMatrix(resultOff, resultPic, "result", null,prop);
		final Offset resultTextOff = new Offset(0, -20, result, AnimalScript.DIRECTION_NW);
		lang.newText(resultTextOff, "Dilated Image", "structText", null);
	}
	
	@Override
	public String getAnnotatedSrc() {
		String annotatedSource = 
			"private int[][] dilation(int[][] source, int[][] struct, int x, int y){			@label(\"header\")\n"+																				
			" int [][] result = new int[source.length][source[0].length];						@label(\"initresult\") \n"+
			" int center = struct[x][y];														@label(\"getcenter\") @declare(\"int\", \"center\")\n"+
			"																					@label(\"empty\")\n"+
			" for (int i = 0; 																	@label(\"for1\") @declare(\"int\", \"i\")\n"+
			"	i < source.length;																@label(\"for1a\") @continue\n"+
			"	i++){																			@label(\"for1b\") @continue @inc(\"i\")\n"+
			"  for(int j = 0; 																	@label(\"for2\") @declare(\"int\", \"j\")\n"+
			"	j < source[0].length; 															@label(\"for2a\") @continue\n"+
			"	j++){																			@label(\"for2b\") @continue @inc(\"j\")\n"+	
			"   if (source[i][j] == center){													@label(\"iffound\")\n"+
			"    for (int a = 0; 																@label(\"forStruct1\") @declare(\"int\", \"a\")\n"+
			"		a < struct.length; 															@label(\"forStruct1a\") @continue\n"+
			"		a++){																		@label(\"forStruct1b\") @continue @inc(\"a\")\n"+
			"     for (int b = 0; 																@label(\"forStruct2\") @declare(\"int\", \"b\")\n"+
			"		b < struct[0].length; 														@label(\"forStruct2a\") @continue\n"+
			"		b++){																		@label(\"forStruct2b\") @continue @inc(\"b\")\n"+						
			"      if (struct[a][b] == 1){														@label(\"ifMatchOne\")\n"+
			"       int offx = i - x + a;														@label(\"offsetx\") @declare(\"int\", \"offx\") \n"+
			"       int offy = j - y + b;														@label(\"offsety\") @declare(\"int\", \"offy\")\n"+
			"       if (offx >= 0 && offx < result.length){										@label(\"ifInSpaceX\")\n"+
			"        if (offy >= 0 && offy < result[0].length){									@label(\"ifInSpaceY\")\n"+
			"         result[offx][offy] = 1;													@label(\"assign\") \n"+
			"        }																			@label(\"br_ifInSpaceY\")\n"+
			"       }																			@label(\"br_ifInSpaceX\")\n"+							
			"      }																			@label(\"br_ifMatchOne\")\n"+
			"     }																				@label(\"br_forStruct2\")\n"+
			"    }																				@label(\"br_forStruct1\")\n"+
			"   }																				@label(\"br_iffound\")\n"+
			"  }																				@label(\"br_for2\")\n"+
			" }																					@label(\"br_for1\")\n"+
			" return result;																	@label(\"return\")\n"+
			"}																					@label(\"br_header\")\n";
		return annotatedSource;
	}
	
	/**
	 * The real algorithm for the dilation with associated animal highlighting functions
	 */
	private void dilation(){
		
		// Additional Text for displaying informations
		TextProperties textprop = new TextProperties();
		textprop.set(AnimationPropertiesKeys.CENTERED_PROPERTY, Boolean.FALSE);
		textprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		textprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		textprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.BOLD, 18));
		textprop.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, Boolean.FALSE);
		Offset matchoff = new Offset(0, 30, source,AnimalScript.DIRECTION_SW);
		Text match = lang.newText(matchoff, "Found matching pixel!", "match", null, textprop);
		Text nomatch = lang.newText(matchoff, "No Match for current pixel", "match", null, textprop);
		match.hide();
		nomatch.hide();
		// Text for the structureelement events
		TextProperties structEventProp = new TextProperties();
		structEventProp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, Boolean.FALSE);
		structEventProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		structEventProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		structEventProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.BOLD, 18));
		structEventProp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, Boolean.FALSE);
		Offset structEventOff = new Offset(0, 20, match,AnimalScript.DIRECTION_SW);
		Text structEvent = lang.newText(structEventOff, "Found 1 in structure element -> stamp", "structev", null, structEventProp);
		Text structEvent2 = lang.newText(structEventOff, "Found Don't care in structure element -> ignore", "structev2", null, structEventProp);
		structEvent.hide();
		structEvent2.hide();
		
		// Text for out of imageborder
		TextProperties outofborderProp = new TextProperties();
		outofborderProp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, Boolean.FALSE);
		outofborderProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);
		outofborderProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		outofborderProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.BOLD, 18));
		outofborderProp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, Boolean.FALSE);
		Offset outofborderOff = new Offset(0, 20, structEvent,AnimalScript.DIRECTION_SW);
		Text outofborder = lang.newText(outofborderOff, "Pixel is out of imageborder -> ignore", "outofborder", null, outofborderProp);
		outofborder.hide();
		
		exec("header");
		lang.nextStep();
		
		exec("initresult");
		lang.nextStep();
		
		int center = struct[x][y];
		exec("getcenter");
		structure.highlightCell(x, y, null, null);	
		lang.nextStep();
		
		exec("for1");
		for (int i = 0; i < sourcePic.length; i++){
			
			exec("for1a");
			if (i != 0)
				exec("for1b");
			lang.nextStep();
			
			exec("for2");
			for(int j = 0; j < sourcePic[0].length; j++){
				
				exec("for2a");
				if (j != 0)
					exec("for2b");
				source.highlightCell(i, j, null, null);	
				lang.nextStep();
				
				
				exec("iffound");
				lang.nextStep();
				if (sourcePic[i][j] == center){
					match.show();
					lang.nextStep();
					
					// Stempel
					// Durchlaufe Strukturelement
					exec("forStruct1");
					for (int a = 0; a < struct.length; a++){
						
						exec("forStruct1a");
						if (a != 0)
							exec("forStruct1b");
						lang.nextStep();
						
						exec("forStruct2");
						for (int b = 0; b < struct[0].length; b++){
							
							exec("forStruct2a");
							if (b != 0)
								exec("forStruct2b");
							structure.highlightCell(a, b, null, null);
							lang.nextStep();
							
							exec("ifMatchOne");
							lang.nextStep();
							if (struct[a][b] == 1){
								structEvent.show();
								lang.nextStep();
																		
								int offx = i - x + a ;
								exec("offsetx");
								lang.nextStep();
								
								int offy = j - y + b;
								exec("offsety");
								lang.nextStep();
								
								exec("ifInSpaceX");
								lang.nextStep();
								if (offx >= 0 && offx < resultPic.length){
									
									exec("ifInSpaceY");
									lang.nextStep();
									if (offy >= 0 && offy < resultPic[0].length){
										exec("assign");																		
										resultPic[offx][offy] = 1;										
										lang.nextStep();
										
										result.highlightCell(offx, offy, null, null);
										lang.nextStep();
										
										result.put(offx, offy, 1, null, null);
										lang.nextStep();
									} else {
										outofborder.show();
										lang.nextStep();
										outofborder.hide();
									}
									exec("br_ifInSpaceY");
									lang.nextStep(delay);
									
								} else {
									outofborder.show();
									lang.nextStep();
									outofborder.hide();
								}								
								exec("br_ifInSpaceX");
								lang.nextStep(delay);
								
							} else {
								structEvent2.show();
								lang.nextStep();								
							}
							exec("br_ifMatchOne");						
							structure.unhighlightCell(a, b, null, null);
							structure.highlightCell(x, y, null, null);
							structEvent.hide();
							structEvent2.hide();
							lang.nextStep(delay);
						}
						exec("br_forStruct2");
						lang.nextStep(delay);
					}
					exec("br_forStruct1");
					lang.nextStep(delay);
				} else {
					nomatch.show();
					lang.nextStep();
				}
				exec("br_iffound");
				
				lang.nextStep(delay);
				source.unhighlightCell(i, j, null, null);
				nomatch.hide();
				match.hide();
			}
			exec("br_for2");
			lang.nextStep(delay);
		}
		exec("br_for1");
		lang.nextStep(delay);
		
		exec("return");
		lang.nextStep(delay);
		
		exec("br_header");
		lang.nextStep(delay);
	}


	// Methods for generator interface
	
	/**
	 * Generates the animal script for the given properties and primitives
	 */
	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		
		int[][] sourcePic = (int[][]) arg1.get("input_image"); 
		int[][] struct = (int[][]) arg1.get("structureelement");
		int x = (Integer) arg1.get("comparisonelement_x_pos");
		int y = (Integer) arg1.get("comparisonelement_y_pos");
		init();
		return generateScript(sourcePic, struct, x, y);
	}
	
	/**
	 * Returns the name of the algorithm
	 */
	@Override
	public String getAlgorithmName() {
		return "Dilation"; //Morphological operations in digital image processing: The dilation Algorithm";
	}
	
	/**
	 * Returns the authors of this animation
	 */
	@Override
	public String getAnimationAuthor() {
		return "Annika Beißler, Martin Hess, Nando Fuchs";
	}
	
	/**
	 * Returns the locale for this animation
	 */
	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}
	
	/**
	 * Returns the algorithm description
	 */
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * Returns the generator type
	 */
	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}
	
	/**
	 * Returns the name of this animation
	 */
	@Override
	public String getName() {
		return "Morphological Operations - dilation [annotation based]";
	}
	
	/**
	 * Returns the output language of this animation, java for example
	 */
	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	
	/**
	 * Initializes the animation
	 */
	@Override
	public void init() {
		super.init();		
		initializeHeader();
		generateSourceCode();
		parse();
	}
}
