/*
 * CohenSutherland.java
 * David Steiner,Jens Krüger, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import translator.Translator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;

public class CohenSutherland implements Generator {
    private Language lang;
    private RectProperties clippingPlaneProperties;
    private RectProperties highlightCurrentAreaProperties;
    private SourceCodeProperties sourceCodeProperties;
    private RectProperties strongHighlightAreaProperties;
    private PolylineProperties lineProperties;
    private MatrixProperties lineArrayProperties;
    private int[][] clippingPlane;
    private PolylineProperties clippedLineProperties;
    private PolylineProperties gmCodeGraphicProperties;
    private CircleProperties pointProperties;
    private SourceCodeProperties introProperties;
    private int[][] lines;
    private TextProperties calcHighlightProperties;
    private RectProperties unhighlightAreaProperties;
    
    
    // unchangeable properties
    	private TextProperties calcProperties;
	    private PolylineProperties gmBorderProperties;
	    private TextProperties gmTextProperties;
	    private SourceCodeProperties conclusionProperties;
    
    // finales
    	private final static int GL_X_LEFT  =  20;
    	private final static int GL_X_RIGHT = 470;
    	private final static int GL_Y_UP    =  50;
    	private final static int LINE_CODE_Y_UP = 405;
    	private final static int SRC_CODE_Y_UP = 150;
    	
    	private final static int UP    = 0b1000;
    	private final static int DOWN  = 0b0100;
    	private final static int RIGHT = 0b0010;
    	private final static int LEFT  = 0b0001;
    	
    	private final static int CG_UNHIGHLIGHT = 0;
    	
	//offsets for the gm window position 
		public static final int GM_X0 = 60;
		public static final int GM_Y0 = 65;	
	//the size of the gm window
		public static final int GM_XL = 300;
		public static final int GM_YL = 300;
	//the interval of points, 0 ... N
		public static final int X_COORD_N = 20; 
		public static final int Y_COORD_N = 20;
	//distance from coordinate axis to the numbers
		public static final int GM_LINE_LETTER_DIST_X = 5;
		public static final int GM_LINE_LETTER_DIST_Y = 18;
    
		//INFO TEXT ATTRIBUTES
		//offsets for the info window position
			public static final int INFO_X0 = 40;
			public static final int INFO_Y0 = 385;
		
    // global variables
    	private Locale location;
	    private Translator translator;
    	private StringMatrixWA linesArray;
    	private SourceCode sourceCode;
    	private LineCodes lineCodes;
        private double[][][] intersection_points; //clipped lines
        private double[][] lines_updated;
        private Text terminated;
		ArrayList<Integer> clipped_indices;

    	
    // counter
    	int mCounter = 0;
    	int iCounter = 0;
    	int line_count; //for LABEL_INIT_ALGO
    	int clippedLines;
    	int ipTextCounter = 0;
    	
    // primitive vectors
    	private Vector<Primitive> gmLinePrimitives;
    	private Vector<Primitive> gmCLinePrimitives;
    	private Vector<Primitive> gmIPPrimitives;
    	private Vector<Primitive> gmCodeGraphicLPrimitives;
    	private Vector<Primitive> gmCodeGraphicAPrimitives;
    	private Vector<Primitive> infoTextPrimitives;
    	/**
    	 * 
    	 * @param	location language/location of generator
    	 * 			current support
    	 * 			Locale.GERMANY
    	 * 			Locale.US
    	 */
    	public CohenSutherland(Locale location) {
    		
    		this.location = location;
    		
    		// initialize translator
    			String textResource = "generators/graphics/CohenSutherland";
    			translator = new Translator(textResource, this.location);
    	}

    /**
     * initialize the generator in this way, it is possible to call {@link generate} more than one time
     */
    @Override
	public void init(){
    		
    	// initialize language object
        	lang = new AnimalScript("Cohen-Sutherland-Algorithmus", "David Steiner,Jens Kr�ger", 800, 600);
        	lang.setStepMode(true);
    }
    
    /**
     * initalize unchangeable properties and primitives collectors
     */
    private void initUnchangeableProperties() {
    	
        // gmTextProperties = axis number symbols 
        	gmTextProperties = new TextProperties(); 
        	gmTextProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
            gmTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
            gmTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,10));
    	    
	    // gmBorderProperties = border of the GM window
            gmBorderProperties = new PolylineProperties(); 
            gmBorderProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
            gmBorderProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
            
        // calcProperties
            calcProperties = new TextProperties();
            Font calcFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
            calcProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, calcFont);
            
        // conclusionProperties
    	    Font conclusionFont = new Font (Font.SANS_SERIF, Font.BOLD, 16);
    	    conclusionProperties = new SourceCodeProperties();
    	    conclusionProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, conclusionFont);
            
    // init primitives 
            gmLinePrimitives = new Vector<Primitive>();
            gmCLinePrimitives = new Vector<Primitive>();
            gmIPPrimitives = new Vector<Primitive>();
            gmCodeGraphicLPrimitives = new Vector<Primitive>();
            gmCodeGraphicAPrimitives = new Vector<Primitive>();
            infoTextPrimitives = new Vector<Primitive>();
    }
    
    /**
     * checks if the user input valid
     */
    private void invalidInputCheck() {
    	
		Random randomgen = new Random(System.currentTimeMillis());
		
		boolean cp_edited = false;
        // check clip plane no double coordinates (so that it is an rectangle)
		//System.out.println("cp: [0][0]=" + clippingPlane[0][0] + ", [0][1]=" + clippingPlane[0][1] + ",[1][0]=" + clippingPlane[1][0] + ",[1[1]=" + clippingPlane[1][1]);
        if (clippingPlane[0][0] == clippingPlane [0][1]){
        	//clippingPlane[0][1] = randomgen.nextInt(X_COORD_N);
    		if(clippingPlane[0][0] == clippingPlane [0][1] && clippingPlane[0][1] < (X_COORD_N/2)){
    			clippingPlane[0][1] += X_COORD_N/2 - 1;
    		} else if(clippingPlane[0][0] == clippingPlane [0][1] && clippingPlane[0][1] >= (X_COORD_N/2)){
    			clippingPlane[0][1] -= X_COORD_N/2 - 1;
    		}
        	//System.out.println("changed [0][1] to " + clippingPlane[0][1]);
        	cp_edited = true;
        } 
        if(clippingPlane[1][0] == clippingPlane [1][1]){
        	//clippingPlane[1][1] = randomgen.nextInt(Y_COORD_N);
    		if(clippingPlane[1][0] == clippingPlane [1][1] && clippingPlane[1][1] < (Y_COORD_N/2)){
    			clippingPlane[1][1] += Y_COORD_N/2 - 1;
    		} else if(clippingPlane[1][0] == clippingPlane [1][1] && clippingPlane[1][1] >= (Y_COORD_N/2)){
    			clippingPlane[1][1] -= Y_COORD_N/2 - 1;
    		}
        	//System.out.println("changed [1][1] to " + clippingPlane[1][1]);
        	cp_edited = true;
        }
    	
	    // check order of clipping plane values
	        if (clippingPlane[0][0] > clippingPlane[0][1]) {
	        	int x = clippingPlane[0][0];
	        	clippingPlane[0][0] = clippingPlane[0][1];
	        	clippingPlane[0][1] = x;
            	//System.out.println("switch cp[0][0] with cp[0][1]");
	        	cp_edited = true;
	        }
	        if (clippingPlane[1][0] < clippingPlane[1][1]) {
	        	int y = clippingPlane[1][0];
	        	clippingPlane[1][0] = clippingPlane[1][1];
	        	clippingPlane[1][1] = y;
            	//System.out.println("switch cp[1][0] with cp[1][1]");
	        	cp_edited = true;
	        }
	        
		    // check if clipping plane is inside the drawing window
	        if (clippingPlane[0][0] < 0){
	        	clippingPlane[0][0] = 0;
	        	cp_edited = true;
	        }
	        if (clippingPlane[0][0] > Y_COORD_N-1){
	        	clippingPlane[0][0] = Y_COORD_N-1;
	        	cp_edited = true;
	        }
	        if (clippingPlane[1][0] < 1){
	        	clippingPlane[1][0] = 1;
	        	cp_edited = true;
	        }
	        if (clippingPlane[1][0] > Y_COORD_N){
	        	clippingPlane[1][0] = Y_COORD_N;
	        	cp_edited = true;
	        }
	        if (clippingPlane[0][1] < 1){
	        	clippingPlane[0][1] = 1;
	        	cp_edited = true;
	        }
	        
	        if (clippingPlane[0][1] > X_COORD_N){
	        	clippingPlane[0][1] = X_COORD_N;
	        	cp_edited = true;
	        }
	        
	        if (clippingPlane[1][1] < 0){
	        	clippingPlane[1][1] = 0;
	        	cp_edited = true;
	        }
	        if (clippingPlane[1][1] > X_COORD_N-1){
	        	clippingPlane[1][1] = X_COORD_N-1;
	        	cp_edited = true;
	        }
	        if(cp_edited){
	    		infoTextPrimitives.add(lang.newText(new Coordinates(INFO_X0, INFO_Y0+10), translator.translateMessage("INFO_CLIP_PLANE"), "info_clipplane", null, gmTextProperties));
	        }
			//System.out.println("cp (final): [0][0]=" + clippingPlane[0][0] + ", [0][1]=" + clippingPlane[0][1] + ",[1][0]=" + clippingPlane[1][0] + ",[1[1]=" + clippingPlane[1][1]);


	    // check if lines outside the drawing window
			//check lines
			ArrayList<Integer> invalid_indices = new ArrayList<Integer>();
			invalid_indices.clear();
			int reduced = 0;

			for (int i = 0; i < lines[0].length; i++) {
				if(lines[0][i] > X_COORD_N || lines[1][i] > Y_COORD_N || lines[2][i] > X_COORD_N || lines[3][i] > Y_COORD_N || (lines[0][i] == lines[2][i] && lines[1][i] == lines[3][i])){
					invalid_indices.add(i);
					reduced++;

				}
			}

			if(reduced != 0){
				int[][] lines_new;
				
				if(reduced == lines[0].length){ //none of the lines were valid: add a random line
					
					lines_new = new int[4][1];
					lines_new[0][0] = randomgen.nextInt(X_COORD_N/2);
					lines_new[1][0] = randomgen.nextInt(Y_COORD_N/2);
					lines_new[2][0] = randomgen.nextInt(X_COORD_N/2) + X_COORD_N/2;
					lines_new[3][0] = randomgen.nextInt(Y_COORD_N/2) + Y_COORD_N/2;
					infoTextPrimitives.add(lang.newText(new Coordinates(INFO_X0, INFO_Y0), translator.translateMessage("INFO_NO_VALID_LINE1") + "(0.." + X_COORD_N +" ; 0.." + Y_COORD_N + ")" + translator.translateMessage("INFO_NO_VALID_LINE2"), "outofrange2", null, gmTextProperties));

				} else {	//else: remove lines that are not valid
					int ii_iterator = 0;
					int ar_iterator = 0;
					lines_new = new int[4][lines[0].length-(reduced)];
					
					for (int i = 0; i < lines[0].length; i++) {						
						if(ii_iterator <= invalid_indices.size()-1){
							if(i == invalid_indices.get(ii_iterator)){
								ii_iterator++;

								continue;
							}
						}
						
						lines_new[0][ar_iterator] = lines[0][i];
						lines_new[1][ar_iterator] = lines[1][i];
						lines_new[2][ar_iterator] = lines[2][i];
						lines_new[3][ar_iterator] = lines[3][i];
						ar_iterator++;

					}
					infoTextPrimitives.add(lang.newText(new Coordinates(INFO_X0, INFO_Y0), reduced + translator.translateMessage("INFO_OUTOFRANGE"), "outofrange1", null, gmTextProperties));

				}
				lines = lines_new;
			}
			lines_updated = new double[4][lines[0].length];
			for(int i = 0; i < lines[0].length; i++){
				lines_updated[0][i] = (double) lines[0][i];
				lines_updated[1][i] = (double) lines[1][i];
				lines_updated[2][i] = (double) lines[2][i];
				lines_updated[3][i] = (double) lines[3][i];

			}


    }

    
    /**
     * displays the introduction
     */
	private void introduction() {
		
	// display headline
		TextProperties headlineProperties = new TextProperties();
		headlineProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		headlineProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
		lang.newText(new Coordinates(GL_X_LEFT, 20), translator.translateMessage("TITLE"), "headline", null, headlineProperties);
		
	// display introduction first part
		SourceCode introFirstPart = lang.newSourceCode(new Coordinates(GL_X_LEFT, 40), "introFirstPart", null, introProperties);
		for(int i = 0; i < Integer.valueOf(translator.translateMessage("INTRODUCTION_1_LENGTH")); i++){
			introFirstPart.addCodeLine(translator.translateMessage("INTRODUCTION_1_"+i), "introFirstPart", 0, null);
		}
		
	// display code graphic
		Vector<Primitive> codeGraphic = displayCodeGraphic(new Coordinates(GL_X_LEFT, 380));
		
	// hide introduction first part
		lang.nextStep(translator.translateMessage("LABEL_INTRO"));
		introFirstPart.hide();
		hide(codeGraphic);
		
	// display introduction second part
		SourceCode introSecondPart = lang.newSourceCode(new Coordinates(GL_X_LEFT, 40), "introSecondPart", null, introProperties);
		for(int i = 0; i < Integer.valueOf(translator.translateMessage("INTRODUCTION_2_LENGTH")); i++){
			introSecondPart.addCodeLine(translator.translateMessage("INTRODUCTION_2_"+i), "introSecondPart", 0, null);
		}		
		
	// hide introduction second part
		lang.nextStep();
		introSecondPart.hide();
		
	// display introduction third part
		SourceCode introThirdPart = lang.newSourceCode(new Coordinates(GL_X_LEFT, 40), "introThirdPart", null, introProperties);
		for(int i = 0; i < Integer.valueOf(translator.translateMessage("INTRODUCTION_3_LENGTH")); i++){
			introThirdPart.addCodeLine(translator.translateMessage("INTRODUCTION_3_"+i), "introThirdPart", 0, null);
		}
		
	// hide introduction third part
		lang.nextStep();
		introThirdPart.hide();
	
	}
	
	/**
	 * displays the conclusion
	 */
	private void conclusion() {
		//TODO
		
			int unclippedLines = lines[0].length - clippedLines;
		
		// hide line codes
			lineCodes.hide();			
		
		// hide source code
			sourceCode.hide();
		
		// seperate lines with and without intersection
			Vector<Primitive> intersectionLines = new Vector<Primitive>();
			Vector<Primitive> inOutLines = new Vector<Primitive>();
			
			for (int i = 0; i < intersection_points.length; i++){
				if (intersection_points[i][0] == null &&
					intersection_points[i][1] == null &&
					intersection_points[i][2] == null &&
					intersection_points[i][3] == null) {
					inOutLines.add(gmLinePrimitives.elementAt(i));
				}
				else
					intersectionLines.add(gmLinePrimitives.elementAt(i));
			}
			
		
		// show lines with intersection and clipped lines
			show(intersectionLines);
			show(gmIPPrimitives);
			show(gmCLinePrimitives);
						
		// show conclusion first part
			for(int i = 0; i < clipped_indices.size(); i++){
				linesArray.highlightCellSpace(clipped_indices.get(i)+1, clipped_indices.get(i)+1, 1, 2);
			}
			SourceCode conclusionFirstPart = lang.newSourceCode(sourceCode.getUpperLeft(), "conclusionFirstPart", null, conclusionProperties);
			for(int i = 0; i < Integer.valueOf(translator.translateMessage("CONCLUSION_1_LENGTH")); i++){
				conclusionFirstPart.addCodeLine(translator.translateMessage("CONCLUSION_1_"+i), "conclusionFirstPart", 0, null);
			}
			conclusionFirstPart.addCodeLine(translator.translateMessage("CONCLUSION_1_X") + clippedLines +")", "conclusionFirstPart", 0, null);

		
		lang.nextStep(translator.translateMessage("LABEL_CONCLUSION"));
		
		// hide conclusion first part
			conclusionFirstPart.hide();
			hide(intersectionLines);
			hide(gmIPPrimitives);
			hide(gmCLinePrimitives);
			for(int i = 0; i < clipped_indices.size(); i++){
				linesArray.unhighlightCellSpace(clipped_indices.get(i)+1, clipped_indices.get(i)+1, 1, 2);
			}

		// show lines which are completely inside or outside (no intersection points)
			show(inOutLines);
			int c_ii = 0;
			for(int i = 0; i < lines[0].length; i++){
				if(c_ii < clipped_indices.size()){
					if(i == clipped_indices.get(c_ii)){
						c_ii++;
						continue;
					}
				}
				linesArray.highlightCellSpace(i+1, i+1, 1, 2);
			}
			
		// show conclusion second part
			SourceCode conclusionSecondPart = lang.newSourceCode(sourceCode.getUpperLeft(), "conclusionSecondPart", null, conclusionProperties);
			for(int i = 0; i < Integer.valueOf(translator.translateMessage("CONCLUSION_2_LENGTH")); i++){
				conclusionSecondPart.addCodeLine(translator.translateMessage("CONCLUSION_2_"+i), "conclusionSecondPart", 0, null);
			}
			conclusionSecondPart.addCodeLine(translator.translateMessage("CONCLUSION_2_X") + unclippedLines +")", "conclusionSecondPart", 0, null);
			
		lang.nextStep();
		
		// hide conclusion second part
			conclusionSecondPart.hide();
			hide(inOutLines);
			sourceCode.show();
			c_ii = 0;
			for(int i = 0; i < lines[0].length; i++){
				if(c_ii < clipped_indices.size()){
					if(i == clipped_indices.get(c_ii)){
						c_ii++;
						continue;
					}
				}
				linesArray.unhighlightCellSpace(i+1, i+1, 1, 2);
			}
			show(gmLinePrimitives);
			show(gmIPPrimitives);
			show(gmCLinePrimitives);


	}
	
	/**** ALGORITHM ****/
	
	/**
	 * Cohen-Sutherland-Algorithm
	 * @param line position of the line in lines
	 */
	private void cohenSutherland (int linePos) {
		
		int[] line = {lines[0][linePos],lines[1][linePos],lines[2][linePos],lines[3][linePos]};
		int[] codes = {0b0000, 0b0000};
		
		boolean first_x_lower;
		boolean first_y_lower;
		if(line[0] < line[2]){
			first_x_lower = true;
		} else first_x_lower = false;
		if(line[1] < line[3]){
			first_y_lower = true;
		} else first_y_lower = false;
		
		// highlight line in linesArray			
			linesArray.highlightCellSpace(linePos+1, linePos+1, 1, 2);

			// draw line, show the areas
			draw_GM_line(linePos, false);
			show(gmCodeGraphicLPrimitives);
			show(gmCodeGraphicAPrimitives);
			line_count++;
			lang.nextStep(translator.translateMessage("LABEL_INIT_ALGO") + line_count);
			
		// FOR  final points A and B
			for (int i = 0; i < line.length; i = i+2) {
				
				sourceCode.highlight(0, 0, true, null, null);
				lineCodes.initLineCode(i/2);				
				
				// IF final point is in upper half space -> set code at position 0 to 1
					sourceCode.highlight(1);
					highlight_GM_codeGraphic(UP, false);
					if (line[i+1] > clippingPlane[1][0]) {
						lang.nextStep();
						sourceCode.highlight(1, 0, true, null, null);
						sourceCode.highlight(2);
						codes[i/2] += UP;
						lineCodes.updateAB(codes);
						lineCodes.highlight(i/2, 0);
						highlight_GM_codeGraphic(UP, true);
					}
					lang.nextStep();
					lineCodes.unhighlight(i/2, 0);
					sourceCode.unhighlight(1);
					sourceCode.unhighlight(2);
					highlight_GM_codeGraphic(CG_UNHIGHLIGHT, false);

				// IF final point is in lower half space -> set code at position 1 to 1
					sourceCode.highlight(3);
					highlight_GM_codeGraphic(DOWN, false);
					if (line[i+1] < clippingPlane[1][1]) {
						lang.nextStep();
						sourceCode.highlight(3, 0, true, null, null);
						sourceCode.highlight(4);
						codes[i/2] += DOWN;
						lineCodes.updateAB(codes);
						lineCodes.highlight(i/2, 1);
						highlight_GM_codeGraphic(DOWN, true);
					}
					lang.nextStep();
					lineCodes.unhighlight(i/2, 1);
					sourceCode.unhighlight(3);
					sourceCode.unhighlight(4);
					highlight_GM_codeGraphic(CG_UNHIGHLIGHT, false);

				// IF final points is in right half space -> set code at position 2 to 1
					sourceCode.highlight(5);
					highlight_GM_codeGraphic(RIGHT, false);
					if (line[i] > clippingPlane[0][1]) {
						lang.nextStep();
						sourceCode.highlight(5, 0, true, null, null);
						sourceCode.highlight(6);
						codes[i/2] += RIGHT;
						lineCodes.updateAB(codes);
						lineCodes.highlight(i/2, 2);
						highlight_GM_codeGraphic(RIGHT, true);
					}
					lang.nextStep();
					lineCodes.unhighlight(i/2, 2);
					sourceCode.unhighlight(5);
					sourceCode.unhighlight(6);
					highlight_GM_codeGraphic(CG_UNHIGHLIGHT, false);

				// IF final points is in left half space -> set cod at position 3 to 1
					sourceCode.highlight(7);
					highlight_GM_codeGraphic(LEFT, false);
					if (line[i] < clippingPlane[0][0]) {
						lang.nextStep();
						sourceCode.highlight(7, 0, true, null, null);
						sourceCode.highlight(8);
						codes[i/2] += LEFT;		
						lineCodes.updateAB(codes);
						lineCodes.highlight(i/2, 3);
						highlight_GM_codeGraphic(LEFT, true);
					}
					lang.nextStep();
					lineCodes.unhighlight(i/2, 3);
					sourceCode.unhighlight(7);
					sourceCode.unhighlight(8);
					highlight_GM_codeGraphic(CG_UNHIGHLIGHT, false);
			}
			sourceCode.unhighlight(0);
			//hide gmCodeGraphic
			hide(gmCodeGraphicAPrimitives);

			
		// IF A | B == 0000	-> terminate (line segment inside clipping plane)
			// dummy
				Text dummy = lang.newText(new Coordinates(GL_X_LEFT + 100, LINE_CODE_Y_UP), "A", "dummy"+iCounter, null, calcProperties);
				dummy.hide();
			sourceCode.highlight(10);
			String aOrBStr = String.format("%"+Integer.toString(4)+"s",Integer.toBinaryString(codes[0] | codes[1])).replace(" ","0");
			Text aOrB = lang.newText(new Coordinates(GL_X_LEFT + 100, LINE_CODE_Y_UP), "A | B = " + aOrBStr, "aOrB"+iCounter, null, calcProperties);
			if ((codes[0] | codes[1]) == 0) {
				lang.nextStep();				
				sourceCode.highlight(10, 0, true, null, null);
				sourceCode.highlight(11);							
				lang.nextStep();
				
				// set intersection_points to null
					for (int i = 0; i < 4; i++)
						intersection_points[linePos][i] = null;
				
				// terminated message
					sourceCode.hide();
					terminated.show();
					lang.nextStep();
					terminated.hide();
					sourceCode.show();
				
				// hide a or b
					aOrB.hide();
					sourceCode.unhighlight(10);
					sourceCode.unhighlight(11);
					
				// reset line codes
					lineCodes.reset();
					
				//hide the old line
					gmLinePrimitives.get(linePos).hide();
					
				// unhighlight line in linesArray
					linesArray.unhighlightCellSpace(linePos+1, linePos+1, 1, 2);
				return;
			}
			lang.nextStep();
			aOrB.hide();
			sourceCode.unhighlight(10);
			sourceCode.unhighlight(11);
			
		// IF A & B != 0000 -> terminate (line segment outside clipping plane)
			sourceCode.highlight(12);
			String aAndBStr = String.format("%"+Integer.toString(4)+"s",Integer.toBinaryString(codes[0] & codes[1])).replace(" ","0");
			Text aAndB = lang.newText(new Coordinates(GL_X_LEFT + 100, LINE_CODE_Y_UP), "A & B = " + aAndBStr, "aAndB"+iCounter, null, calcProperties);
			if ((codes[0] & codes[1]) != 0) {
				lang.nextStep();
				sourceCode.highlight(12, 0, true, null, null);
				sourceCode.highlight(13);
				lang.nextStep();
				
				// set intersection_points to null
					for (int i = 0; i < 4; i++)
						intersection_points[linePos][i] = null;
				
				// terminated message
					sourceCode.hide();
					terminated.show();
					lang.nextStep();
					terminated.hide();
					sourceCode.show();
				
				// hide a and b
					aAndB.hide();
					sourceCode.unhighlight(12);
					sourceCode.unhighlight(13);
					
				// reset line codes
					lineCodes.reset();
					
				//hide the old line
					gmLinePrimitives.get(linePos).hide();
					
				// unhighlight line in linesArray
					linesArray.unhighlightCellSpace(linePos+1, linePos+1, 1, 2);
				return;
			}
			lang.nextStep();
			aAndB.hide();
			sourceCode.unhighlight(12);
			sourceCode.unhighlight(13);

		// ELSE
			sourceCode.highlight(14, 0, true, null, null);
			sourceCode.highlight(15);
			int s = codes[0] | codes[1];
			lineCodes.updateS(s);
			lang.nextStep();
			sourceCode.unhighlight(15);
			
			double[] ipUP, ipDOWN, ipRIGHT, ipLEFT;
			int ip_counter = 0;
			
			boolean clipped = false;
			//System.out.println("i = " + linePos + ", A= " +line[0]+ "|" +line[1]+ " ; " +line[2]+ "|" +line[3]);
			
			// for intersection point display
			int xIpText = GL_X_LEFT+100;
			int yIpText = LINE_CODE_Y_UP;
			Vector<Primitive> ipDisplay = new Vector<Primitive>();

			// IF s[0] == 1 -> calculate intersection point with upper line of intersection
				sourceCode.highlight(16);
				if ((s & UP) == UP) {
					lang.nextStep();
					sourceCode.highlight(16, 0, true, null, null);
					sourceCode.highlight(17);
					lineCodes.highlight(2,0);
					ipUP = liangBarsky(line, UP);
					ipDisplay.add(lang.newText( new Coordinates(xIpText, yIpText), 
												translator.translateMessage("INTERSECTION_POINT") + "0 = {" + 
												Double.valueOf(Math.round(100.0 * ipUP[0])/100.0) + "," + 
												Double.valueOf(Math.round(100.0 * ipUP[1])/100.0) + "}", 
												"ip0" + ipTextCounter, null, calcProperties));
					yIpText += 30;
				} else ipUP = null;
				intersection_points[linePos][0] = ipUP; // update ip-point entry
				if(draw_GM_ip(linePos, 0)){
					ip_counter++;
					//System.out.println("candidate:UP: " + ipUP[0] + "|" + ipUP[1]);
					if(ipUP[0] >= clippingPlane[0][0] && ipUP[0] <= clippingPlane[0][1]){
						clipped = true;
						if(first_y_lower){
							//System.out.println("set B to " + ipUP[0]+ "|" + ipUP[1]);
							lines_updated[2][linePos] = ipUP[0];
							lines_updated[3][linePos] = ipUP[1];
						} else {
							//System.out.println("set A to " + ipUP[0]+ "|" + ipUP[1]);
							lines_updated[0][linePos] = ipUP[0];
							lines_updated[1][linePos] = ipUP[1];
						}
					}
				}
				lang.nextStep();
				sourceCode.unhighlight(16);
				sourceCode.unhighlight(17);
				lineCodes.unhighlight(2,0);
	

			// IF s[1] == 1 -> calculate intersection point with lower line of intersection
				sourceCode.highlight(18);
				if ((s & DOWN) == DOWN) {
					lang.nextStep();
					sourceCode.highlight(18, 0, true, null, null);
					sourceCode.highlight(19);
					lineCodes.highlight(2,1);
					ipDOWN = liangBarsky(line, DOWN);
					ipDisplay.add(lang.newText( new Coordinates(xIpText, yIpText), 
												translator.translateMessage("INTERSECTION_POINT") + "1 = {" + 
												Double.valueOf(Math.round(100.0 * ipDOWN[0])/100.0) + "," + 
												Double.valueOf(Math.round(100.0 * ipDOWN[1])/100.0) + "}", 
												"ip1" + ipTextCounter, null, calcProperties));
					yIpText += 30;
				} else ipDOWN = null;
				intersection_points[linePos][1] = ipDOWN;
				if(draw_GM_ip(linePos, 1)){
					ip_counter++;
					//System.out.println("candidate:DOWN: " + ipDOWN[0] + "|" + ipDOWN[1]);
					if(ipDOWN[0] >= clippingPlane[0][0] && ipDOWN[0] <= clippingPlane[0][1]){
						clipped = true;
						if(first_y_lower){
							//System.out.println("set A to " + ipDOWN[0]+ "|" + ipDOWN[1]);
							lines_updated[0][linePos] = ipDOWN[0];
							lines_updated[1][linePos] = ipDOWN[1];
						} else {
							//System.out.println("set B to " + ipDOWN[0]+ "|" + ipDOWN[1]);
							lines_updated[2][linePos] = ipDOWN[0];
							lines_updated[3][linePos] = ipDOWN[1];
						}
					}
				}
				lang.nextStep();
				sourceCode.unhighlight(18);
				sourceCode.unhighlight(19);
				lineCodes.unhighlight(2,1);



				
			// IF s[2] == 1 -> calculate intersection point with right line of intersection
				sourceCode.highlight(20);
				if ((s & RIGHT) == RIGHT) {
					lang.nextStep();
					sourceCode.highlight(20, 0, true, null, null);
					sourceCode.highlight(21);
					lineCodes.highlight(2,2);
					ipRIGHT = liangBarsky(line, RIGHT);
					ipDisplay.add(lang.newText( new Coordinates(xIpText, yIpText), 
												translator.translateMessage("INTERSECTION_POINT") + "2 = {" + 
												Double.valueOf(Math.round(100.0 * ipRIGHT[0])/100.0) + "," + 
												Double.valueOf(Math.round(100.0 * ipRIGHT[1])/100.0) + "}", 
												"ip2" + ipTextCounter, null, calcProperties));
					yIpText += 30;
				}  else ipRIGHT = null;
				intersection_points[linePos][2] = ipRIGHT;  // update ip-point entry
				if(draw_GM_ip(linePos, 2)){
					ip_counter++;
					//System.out.println("candidate:RIGHT: " + ipRIGHT[0] + "|" + ipRIGHT[1]);
					if(ipRIGHT[1] <= clippingPlane[1][0] && ipRIGHT[1] >= clippingPlane[1][1]){
						clipped = true;
						if(first_x_lower){
							//System.out.println("set B to " + ipRIGHT[0]+ "|" + ipRIGHT[1]);
							lines_updated[2][linePos] = ipRIGHT[0];
							lines_updated[3][linePos] = ipRIGHT[1];
						} else {
							//System.out.println("set A to " + ipRIGHT[0]+ "|" + ipRIGHT[1]);
							lines_updated[1][linePos] = ipRIGHT[0];
							lines_updated[0][linePos] = ipRIGHT[1];
						}
					}
				}
				lang.nextStep();
				sourceCode.unhighlight(20);
				sourceCode.unhighlight(21);
				lineCodes.unhighlight(2,2);


				
			// IF s[3] == 1 -> calculate intersection point with left line of intersection
				sourceCode.highlight(22);
				if ((s & LEFT) == LEFT) {
					lang.nextStep();
					sourceCode.highlight(22, 0, true, null, null);
					sourceCode.highlight(23);
					lineCodes.highlight(2,3);
					ipLEFT = liangBarsky(line, LEFT);
					ipDisplay.add(lang.newText( new Coordinates(xIpText, yIpText), 
												translator.translateMessage("INTERSECTION_POINT") + "3 = {" + 
												Double.valueOf(Math.round(100.0 * ipLEFT[0])/100.0) + "," + 
												Double.valueOf(Math.round(100.0 * ipLEFT[1])/100.0) + "}", 
												"ip3" + ipTextCounter, null, calcProperties));
					yIpText += 30;
				}  else ipLEFT = null;
				intersection_points[linePos][3] = ipLEFT; // update ip-point entry
				if(draw_GM_ip(linePos, 3)){
					ip_counter++;
					//System.out.println("candidate:LEFT: " + ipLEFT[0] + "|" + ipLEFT[1]);
					if(ipLEFT[1] <= clippingPlane[1][0] && ipLEFT[1] >= clippingPlane[1][1]){
						clipped = true;
						if(first_x_lower){
							//System.out.println("set A to " + ipLEFT[0]+ "|" + ipLEFT[1]);
							lines_updated[0][linePos] = ipLEFT[0];
							lines_updated[1][linePos] = ipLEFT[1];
						} else {
							//System.out.println("set B to " + ipLEFT[0]+ "|" + ipLEFT[1]);
							lines_updated[2][linePos] = ipLEFT[0];
							lines_updated[3][linePos] = ipLEFT[1];
						}
					}
				}
				lang.nextStep();
				sourceCode.unhighlight(22);
				sourceCode.unhighlight(23);
				lineCodes.unhighlight(2,3);
				sourceCode.unhighlight(14); // unhighlight ELSE

				
				
			// terminated message
				sourceCode.hide();
				terminated.show();

				lang.nextStep();
				
			// delete displayed intersection points
					hide(ipDisplay);

			//hide the old line
				gmLinePrimitives.get(linePos).hide();

				
			//hide the intersection points (just the current calls line will be shown)
				for(int i = 0; i < ip_counter; i++){
					gmIPPrimitives.get(gmIPPrimitives.size()-1-i).hide();
				}
			
			//hide the codeGraphic Lines
			hide(gmCodeGraphicLPrimitives);

				
			//if clipped, show the clipped line
				if(clipped){

					//draw the clipped line
					draw_GM_line(linePos, true);
					
					lang.nextStep();
					
					//hide the clipped line
					gmCLinePrimitives.lastElement().hide();
					clipped_indices.add(linePos);
					clippedLines++;
				}
				
				terminated.hide();
				sourceCode.show();
				
			// reset line codes
				lineCodes.reset();
				
			// unhighlight line in linesArray
				linesArray.unhighlightCellSpace(linePos+1, linePos+1, 1, 2);
				
				

	}
	
	/**** HELPER METHODS ****/
	
	/**
	 * Liang-Barsky-Algorithm
	 * 
	 * @param line line which intersects the line of intersection
	 * @param crossing line of intersection (valid inputs: UP, DOWN, RIGHT, LEFT)
	 * @return intersection point {x,y}
	 */
	private double[] liangBarsky (int[] line, int crossing) {
		
		// line points
			double[] p1 = {line[0], line[1]};
			double[] p2 = {line[2], line[3]};
		
		// calculate line of intersection from clipping plane
			double[] q1 = new double[2];
			double[] q2 = new double[2];
			switch (crossing) {
				case UP: {
					q1[0] = clippingPlane[0][0];
					q1[1] = clippingPlane[1][0];
					q2[0] = clippingPlane[0][1];
					q2[1] = clippingPlane[1][0];
					break;
				}
				case DOWN: {
					q1[0] = clippingPlane[0][0];
					q1[1] = clippingPlane[1][1];
					q2[0] = clippingPlane[0][1];
					q2[1] = clippingPlane[1][1];
					break;
				}
				case RIGHT: {
					q1[0] = clippingPlane[0][1];
					q1[1] = clippingPlane[1][0];
					q2[0] = clippingPlane[0][1];
					q2[1] = clippingPlane[1][1];
					break;
				}
				case LEFT: {
					q1[0] = clippingPlane[0][0];
					q1[1] = clippingPlane[1][0];
					q2[0] = clippingPlane[0][0];
					q2[1] = clippingPlane[1][1];
					break;
				}
				default: {
					System.err.println("invalid input to liang barsky");
					return null;
				}
			}
			
		// calculate n 
			double[] n = {  q2[1]-q1[1],
					   -(q2[0]-q1[0])};
		
		// calculate t
			double t;
			if (n[0] != 0)	t = (q1[0]-p1[0])/(p2[0]-p1[0]);
			else			t = (q1[1]-p1[1])/(p2[1]-p1[1]);
	
		// calculate ip
			double[] ip = {p1[0] + t*(p2[0]-p1[0]),
						   p1[1] + t*(p2[1]-p1[1])};
		
		return ip;
		
	}
	
	/**
	 * displays the code graphic as an information how the point's positions are coded
	 * @param upperLeft upper left corner of the graphic
	 * @return all primitives of the graphic saved in a vector of primitives
	 */
	private Vector<Primitive> displayCodeGraphic(Coordinates upperLeft) {
		
		// row height & col width
			final int x = upperLeft.getX();
			final int y = upperLeft.getY();
			final int ROW_HEIGHT = 30;
			final int COL_WIDTH = 60;
			final int TEXT_OFFSET = 7;
			
		// vector for primitives
			Vector<Primitive> primitives = new Vector<Primitive>();
			
		// properties
			// line
				PolylineProperties lineProperties = new PolylineProperties();
				lineProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
				lineProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
				
			// text
				Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
				TextProperties textProperties = new TextProperties();
				textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
				textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
				textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
				textProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
				
			// rect in the middle
				RectProperties rectProperty = new RectProperties();
				rectProperty.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
				rectProperty.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				
		
		// primitives
			// lines
				Coordinates line1Left  = new Coordinates(x,               y + ROW_HEIGHT);
				Coordinates line1Right = new Coordinates(x + 3*COL_WIDTH, y + ROW_HEIGHT);
				Node [] line1 = {line1Left, line1Right};
				primitives.add(lang.newPolyline(line1, "gline1", null, lineProperties));
				
				Coordinates line2Left  = new Coordinates(x,               y + 2*ROW_HEIGHT);
				Coordinates line2Right = new Coordinates(x + 3*COL_WIDTH, y + 2*ROW_HEIGHT);
				Node [] line2 = {line2Left, line2Right};
				primitives.add(lang.newPolyline(line2, "gline2", null, lineProperties));
				
				Coordinates line3Up   = new Coordinates(x + COL_WIDTH, y);
				Coordinates line3Down = new Coordinates(x + COL_WIDTH, y + 3*ROW_HEIGHT);
				Node [] line3 = {line3Up, line3Down};
				primitives.add(lang.newPolyline(line3, "gline3", null, lineProperties));
				
				Coordinates line4Up   = new Coordinates(x + 2*COL_WIDTH, y);
				Coordinates line4Down = new Coordinates(x + 2*COL_WIDTH, y + 3*ROW_HEIGHT);
				Node [] line4 = {line4Up, line4Down};
				primitives.add(lang.newPolyline(line4, "gline4", null, lineProperties));
				
			// text
				Node text1001 = new Coordinates (x + COL_WIDTH/2, y + TEXT_OFFSET);
				primitives.add(lang.newText(text1001, "1001", "gtext1001", null, textProperties));
				
				Node text1000 = new Coordinates (x + COL_WIDTH + COL_WIDTH/2, y + TEXT_OFFSET);
				primitives.add(lang.newText(text1000, "1000", "gtext1000", null, textProperties));
				
				Node text1010 = new Coordinates (x + 2*COL_WIDTH + COL_WIDTH/2, y + TEXT_OFFSET);
				primitives.add(lang.newText(text1010, "1010", "gtext1010", null, textProperties));
				
				Node text0001 = new Coordinates (x + COL_WIDTH/2, y + ROW_HEIGHT + TEXT_OFFSET);
				primitives.add(lang.newText(text0001, "0001", "gtext0001", null, textProperties));
				
				Node text0000 = new Coordinates (x + COL_WIDTH + COL_WIDTH/2, y + ROW_HEIGHT + TEXT_OFFSET);
				primitives.add(lang.newText(text0000, "0000", "gtext0000", null, textProperties));
				
				Node text0010 = new Coordinates (x + 2*COL_WIDTH + COL_WIDTH/2, y + ROW_HEIGHT + TEXT_OFFSET);
				primitives.add(lang.newText(text0010, "0010", "gtext0010", null, textProperties));
				
				Node text0101 = new Coordinates (x + COL_WIDTH/2, y + 2*ROW_HEIGHT + TEXT_OFFSET);
				primitives.add(lang.newText(text0101, "0101", "gtext0101", null, textProperties));
				
				Node text0100 = new Coordinates (x + COL_WIDTH + COL_WIDTH/2, y + 2*ROW_HEIGHT + TEXT_OFFSET);
				primitives.add(lang.newText(text0100, "0100", "gtext0100", null, textProperties));
				
				Node text0110 = new Coordinates (x + 2*COL_WIDTH + COL_WIDTH/2, y + 2*ROW_HEIGHT + TEXT_OFFSET);
				primitives.add(lang.newText(text0110, "0110", "gtext0110", null, textProperties));
				
			// rect in the middle
				Node rectUpperLeft  = new Coordinates(x + COL_WIDTH, y + ROW_HEIGHT);
				Node rextLowerRight = new Coordinates(x + 2*COL_WIDTH, y + 2*ROW_HEIGHT);
				primitives.add(lang.newRect(rectUpperLeft, rextLowerRight, "gRect", null, rectProperty));
		
		return primitives;
	}
	
	/**
	 * initialize the visualization of the algorithm
	 * execute Cohen-Sutherland-Algorithm with user input saved in lines
	 */
	private void executeAlgoWithUserInput() {
		
			// initialize algo visualization

	    // check order of clipping plane values
        if (clippingPlane[0][0] > clippingPlane[0][1]) {
        	int x = clippingPlane[0][0];
        	clippingPlane[0][0] = clippingPlane[0][1];
        	clippingPlane[0][1] = x;
        }
        if (clippingPlane[1][0] < clippingPlane[1][1]) {
        	int y = clippingPlane[1][0];
        	clippingPlane[1][0] = clippingPlane[1][1];
        	clippingPlane[1][1] = y;
        }
        
        		// clipped line counter + index list
					clippedLines = 0;
					clipped_indices = new ArrayList<Integer>();
		
				// array for the intersection points
					intersection_points = new double[lines[0].length][4][2];
				
				// line array
					initLineArray();
					linesArray.drawMatrix(lang, new Coordinates(GL_X_RIGHT, GL_Y_UP), lineArrayProperties, mCounter);
					mCounter++;
					
				// source code
					displaySourceCode();
					
				// GM window
					draw_GM_window();
					
				// line codes
					lineCodes = new LineCodes(lang, new Coordinates(GL_X_LEFT, LINE_CODE_Y_UP), calcProperties, calcHighlightProperties);
				
				// terminated message
					terminated = lang.newText(sourceCode.getUpperLeft(), translator.translateMessage("LABEL_TERMINATED"), "terminated", null);
					terminated.hide();


				
			// execute algorithm with the lines entered by the user
				for (int i = 0; i < lines[0].length; i++) {
					cohenSutherland(i);
				}

	}
	
	/**
	 * initializes the line array
	 */
	private void initLineArray() {

		String[][] linesAsStrings = new String[3][lines[0].length+1];
		linesAsStrings[0][0] = "";
		linesAsStrings[1][0] = "A";
		linesAsStrings[2][0] = "B";
		for (int i = 0; i < lines[0].length; i++) {
			linesAsStrings[0][i+1] = String.valueOf(i);
			linesAsStrings[1][i+1] = "{"+lines[0][i]+","+lines[1][i]+"}";
			linesAsStrings[2][i+1] = "{"+lines[2][i]+","+lines[3][i]+"}";
		}
		linesArray = new StringMatrixWA(linesAsStrings);
		
	}
	
	/**
	 * displays the source code
	 */
	private void displaySourceCode () {
		
    	sourceCode = lang.newSourceCode(new Coordinates(GL_X_RIGHT, SRC_CODE_Y_UP), "sourceCode", null, sourceCodeProperties);
    	
    	for(int i = 0; i < Integer.valueOf(translator.translateMessage("SOURCE_CODE_LENGTH")); i++){
    		sourceCode.addCodeLine(translator.translateMessage("SOURCE_CODE_"+i), "code", 0, null);
    	}
    }
	
	/**
	 * hide all primitives given by a vector of primitives
	 * @param primitives
	 */
	private void hide (Vector<Primitive> primitives) {
		for (Primitive p : primitives) {
			p.hide();
		}
	}
	
	/**
	 * show all primitives given by a vector of primitives
	 * @param primitives
	 */
	private void show (Vector<Primitive> primitives) {
		for (Primitive p : primitives) {
			p.show();
		}
	}
	
	/**
	 * draw the GM Window
	 * @return all primitives of the window (except for the lines)
	 */
	private Vector<Primitive> draw_GM_window(){
		//all colors here should be black
		
		Vector<Primitive> gmPrimitives = new Vector<Primitive>();

		
		//Verhältnis zwischen Koordinaten und Pixeln bzw. Länge einer Koordinate in Pixeln
		int gm_xn = GM_X0 + GM_XL;
		int gm_yn = GM_Y0 + GM_YL;

		//Coordinates gr_up_left = new Coordinates(GM_X0,gm_yn);
		//Coordinates gr_low_right = new Coordinates(gm_xn,GM_Y0);		
		//lang.newGraph(name, graphAdjacencyMatrix, graphNodes, null, null, props);
		
		//Border
		Coordinates l_1= new Coordinates(GM_X0,gm_yn);		
		Coordinates l_2= new Coordinates(gm_xn,gm_yn);
		Coordinates l_3 = new Coordinates(gm_xn,GM_Y0);
		Coordinates l_4 = new Coordinates(GM_X0,GM_Y0);

		Coordinates[] lcoords = {l_1, l_2, l_3, l_4, l_1};
		gmPrimitives.add(lang.newPolyline(lcoords, "border", null, gmBorderProperties));

		//number symbols for X_AXIS
		for(int i = 0; i <= X_COORD_N; i++){
			int pos_x = get_GM_x_pix(i);
			int pos_y = gm_yn + GM_LINE_LETTER_DIST_X;
			Coordinates t_up_left = new Coordinates(pos_x - 7, pos_y);
			
			if (i == 0) {
				Text t = lang.newText(t_up_left, String.valueOf(i), "xsymb_"+"BUGFIX"+i, null, gmTextProperties);
				t.hide();
			}

			Text t = lang.newText(t_up_left, String.valueOf(i), "xsymb_"+i, null, gmTextProperties);
			Coordinates[] lps = {new Coordinates(pos_x, gm_yn-1), new Coordinates(pos_x, gm_yn+2)};
			//create line from (pos_x | gm_yn-1) to (pos_x | gm_yn+2)
			Polyline lt = lang.newPolyline(lps, "xmarker_"+i, null, gmBorderProperties);
			
			//add primitives to vector
				gmPrimitives.add(t);
				gmPrimitives.add(lt);
		}
		
		//number symbols for Y_AXIS
		for(int i = 0; i <= Y_COORD_N; i++){
			int pos_x = GM_X0 - GM_LINE_LETTER_DIST_Y;
			int pos_y = get_GM_y_pix(i) ;
			Coordinates t_up_left = new Coordinates(pos_x, pos_y - 7);
			
			Text t = lang.newText(t_up_left, String.valueOf(i), "ysymb_"+i, null, gmTextProperties);
			
			Coordinates[] lps = {new Coordinates(GM_X0+1, pos_y), new Coordinates(GM_X0-2, pos_y)};
			Polyline lt = lang.newPolyline(lps, "ymarker_"+i, null, gmBorderProperties);
			
			//add primitives to vector
				gmPrimitives.add(t);
				gmPrimitives.add(lt);
			}
		
		//draw clipping plane
		int cp_up_px = get_GM_y_pix(clippingPlane[1][1]);
		int cp_down_px = get_GM_y_pix(clippingPlane[1][0]);
		int cp_right_px = get_GM_x_pix(clippingPlane[0][1]);
		int cp_left_px = get_GM_x_pix(clippingPlane[0][0]);
		Coordinates cp_1 = new Coordinates(cp_right_px,cp_up_px); //up/r
		Coordinates cp_2 = new Coordinates(cp_left_px,cp_down_px); //down/l

		gmPrimitives.add(lang.newRect(cp_1, cp_2, "clipPlane", null, clippingPlaneProperties));
		
        unhighlightAreaProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 6);
		set_GM_codeGraphic();
		
		return gmPrimitives;
	
		/*
		//draw points
		Circle[] circles = get_GM_points(points, gmAllPointsProperties, "normalpoint");
		for(int c = 0; c < circles.length; c++){
			Circle ci = circles[c];
			ci.show();
			gm_Primitives.add(ci); //add primitives to vector
		}
		
		left_points = get_GM_points(points, gmLeftPointsProperties, "leftpoints");
		right_points = get_GM_points(points, gmRightPointsProperties, "rightpoints");
		*/
	}
	
	private void highlight_GM_codeGraphic(int area_code, boolean pointInside){
		
		Color highlight;
		if(pointInside){
			highlight = (Color) strongHighlightAreaProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		} else {
			highlight = (Color) highlightCurrentAreaProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		}
		Color unhighlight = (Color) unhighlightAreaProperties.get(AnimationPropertiesKeys.FILL_PROPERTY);
		

		switch(area_code){
		case UP:
			gmCodeGraphicAPrimitives.get(0).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(1).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(2).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(3).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(4).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(5).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(6).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(7).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(8).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			break;
		case DOWN:
			gmCodeGraphicAPrimitives.get(0).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(1).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(2).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(3).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(4).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(5).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(6).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(7).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(8).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			break;
		case LEFT:
			gmCodeGraphicAPrimitives.get(0).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(1).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(2).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(3).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(4).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(5).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(6).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(7).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(8).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			break;
		case RIGHT:
			gmCodeGraphicAPrimitives.get(0).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(1).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(2).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(3).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(4).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(5).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(6).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(7).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(8).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, highlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			break;
		case CG_UNHIGHLIGHT:
		default:
			gmCodeGraphicAPrimitives.get(0).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(1).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(2).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(3).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(4).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(5).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(6).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(7).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			gmCodeGraphicAPrimitives.get(8).changeColor(AnimationPropertiesKeys.FILL_PROPERTY, unhighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		}

	}
	
	private void set_GM_codeGraphic(){
		
		//middle coordinates (of the clipping plane)
		int ml_x = clippingPlane[0][0];
		int mr_x = clippingPlane[0][1];
		int mu_y = clippingPlane[1][0];
		int md_y = clippingPlane[1][1];
		/*
		int dist_lr = mr_x - ml_x;
		int dist_ud = mu_y - md_y;
		
		//outside coordinates for showing the code graphic
		
		int u_y = mu_y + dist_ud;
		//1) up coordinate
		if(u_y > Y_COORD_N){
			u_y = Y_COORD_N;
		}
		//2) down coordinate
		int d_y = md_y - dist_ud;
		if(d_y < 0){
			d_y = 0;
		}
		
		//3) right coordinate
		int r_x = mr_x + dist_lr;
		if(r_x > X_COORD_N){
			r_x = X_COORD_N;
		}
		//4) left coordinate
		int l_x = ml_x - dist_lr;
		if(l_x < 0){
			l_x = 0;
		}
		*/
		int u_y = Y_COORD_N;
		int d_y = 0;
		int r_x = X_COORD_N;
		int l_x = 0;

		//line coordinates
		Coordinates lc_mul = new Coordinates(get_GM_x_pix(ml_x), get_GM_y_pix(mu_y));
		Coordinates lc_mdl = new Coordinates(get_GM_x_pix(ml_x), get_GM_y_pix(md_y));
		Coordinates lc_mur = new Coordinates(get_GM_x_pix(mr_x), get_GM_y_pix(mu_y));
		Coordinates lc_mdr = new Coordinates(get_GM_x_pix(mr_x), get_GM_y_pix(md_y));
		
		Coordinates lc_ul = new Coordinates(get_GM_x_pix(ml_x), get_GM_y_pix(u_y));
		Coordinates lc_ur = new Coordinates(get_GM_x_pix(mr_x), get_GM_y_pix(u_y));
		Coordinates lc_dl = new Coordinates(get_GM_x_pix(ml_x), get_GM_y_pix(d_y));
		Coordinates lc_dr = new Coordinates(get_GM_x_pix(mr_x), get_GM_y_pix(d_y));
		
		Coordinates lc_ru = new Coordinates(get_GM_x_pix(r_x), get_GM_y_pix(mu_y));
		Coordinates lc_rd = new Coordinates(get_GM_x_pix(r_x), get_GM_y_pix(md_y));
		Coordinates lc_lu = new Coordinates(get_GM_x_pix(l_x), get_GM_y_pix(mu_y));
		Coordinates lc_ld = new Coordinates(get_GM_x_pix(l_x), get_GM_y_pix(md_y));
		
		
		Coordinates[] coords_ul = {lc_ul, lc_mul};
		Coordinates[] coords_ur = {lc_ur, lc_mur};
		Coordinates[] coords_dl = {lc_dl, lc_mdl};
		Coordinates[] coords_dr = {lc_dr, lc_mdr};
		
		Coordinates[] coords_ru = {lc_ru, lc_mur};
		Coordinates[] coords_rd = {lc_rd, lc_mdr};
		Coordinates[] coords_lu = {lc_lu, lc_mul};
		Coordinates[] coords_ld = {lc_ld, lc_mdl};

		gmCodeGraphicLPrimitives.add(lang.newPolyline(coords_ul, "gmCodeGraphicLine_u_l", null, gmCodeGraphicProperties));
		gmCodeGraphicLPrimitives.add(lang.newPolyline(coords_ur, "gmCodeGraphicLine_u_r", null, gmCodeGraphicProperties));
		gmCodeGraphicLPrimitives.add(lang.newPolyline(coords_dl, "gmCodeGraphicLine_d_l", null, gmCodeGraphicProperties));
		gmCodeGraphicLPrimitives.add(lang.newPolyline(coords_dr, "gmCodeGraphicLine_d_r", null, gmCodeGraphicProperties));

		gmCodeGraphicLPrimitives.add(lang.newPolyline(coords_ru, "gmCodeGraphicLine_r_u", null, gmCodeGraphicProperties));
		gmCodeGraphicLPrimitives.add(lang.newPolyline(coords_rd, "gmCodeGraphicLine_r_d", null, gmCodeGraphicProperties));
		gmCodeGraphicLPrimitives.add(lang.newPolyline(coords_lu, "gmCodeGraphicLine_l_u", null, gmCodeGraphicProperties));
		gmCodeGraphicLPrimitives.add(lang.newPolyline(coords_ld, "gmCodeGraphicLine_l_d", null, gmCodeGraphicProperties));
		
		Coordinates ac_ul = new Coordinates(get_GM_x_pix(l_x), get_GM_y_pix(u_y));
		Coordinates ac_dr = new Coordinates(get_GM_x_pix(r_x), get_GM_y_pix(d_y));

		//upper areas
		gmCodeGraphicAPrimitives.add(lang.newRect(ac_ul, lc_mul, "gmCodeGraphicArea_u_l", null, unhighlightAreaProperties));
		gmCodeGraphicAPrimitives.add(lang.newRect(lc_ul, lc_mur, "gmCodeGraphicArea_u_m", null, unhighlightAreaProperties));
		gmCodeGraphicAPrimitives.add(lang.newRect(lc_ur, lc_ru, "gmCodeGraphicArea_u_r", null, unhighlightAreaProperties));
		
		//left middle area
		gmCodeGraphicAPrimitives.add(lang.newRect(lc_lu, lc_mdl, "gmCodeGraphicArea_l_m", null, unhighlightAreaProperties));
		//middle area
		gmCodeGraphicAPrimitives.add(lang.newRect(lc_mul, lc_mdr, "gmCodeGraphicArea_m", null, unhighlightAreaProperties));
		//right middle area
		gmCodeGraphicAPrimitives.add(lang.newRect(lc_mur, lc_rd, "gmCodeGraphicArea_r_m", null, unhighlightAreaProperties));
	
		//lower areas
		gmCodeGraphicAPrimitives.add(lang.newRect(lc_ld, lc_dl, "gmCodeGraphicArea_d_l", null, unhighlightAreaProperties));
		gmCodeGraphicAPrimitives.add(lang.newRect(lc_mdl, lc_dr, "gmCodeGraphicArea_d_m", null, unhighlightAreaProperties));
		gmCodeGraphicAPrimitives.add(lang.newRect(lc_mdr, ac_dr, "gmCodeGraphicArea_d_r", null, unhighlightAreaProperties));

		hide(gmCodeGraphicLPrimitives);
		hide(gmCodeGraphicAPrimitives);
		
	}
	
	private void draw_GM_line(int line_id, boolean clipped){
		
		PolylineProperties l_properties;
		String l_name;
		Vector<Primitive> l_vector;
		int l1_x, l1_y, l2_x, l2_y;
		
		if(!clipped){
			l_properties = lineProperties;
			l_name = "line";
			l_vector = gmLinePrimitives;
			if(line_id < gmLinePrimitives.size()){
				//System.out.println("Error! There is already a line at this index in the respective Primitive Vector!");
				return;
			}
			l1_x = get_GM_x_pix(lines[0][line_id]);
			l1_y = get_GM_y_pix(lines[1][line_id]);
			l2_x = get_GM_x_pix(lines[2][line_id]);
			l2_y = get_GM_y_pix(lines[3][line_id]);
		}else{
			l_properties = clippedLineProperties;
			l_name = "clipped line";
			l_vector = gmCLinePrimitives;
			l1_x = get_GM_x_pix(lines_updated[0][line_id]);
			l1_y = get_GM_y_pix(lines_updated[1][line_id]);
			l2_x = get_GM_x_pix(lines_updated[2][line_id]);
			l2_y = get_GM_y_pix(lines_updated[3][line_id]);
		}
		


		
		Coordinates l_1= new Coordinates(l1_x,l1_y);		
		Coordinates l_2= new Coordinates(l2_x,l2_y);

		Coordinates[] line_coords = {l_1, l_2};
		l_vector.add(lang.newPolyline(line_coords, l_name+line_id, null, l_properties));
		//gmCLinePrimitives.add();
	}
	
	/**
	 * shows the intersection points computed in csa
	 * @return true, if there was an intersection point entry at the specified index
	 **/
	private boolean draw_GM_ip(int line_id, int point_id){
		if(intersection_points[line_id][point_id] == null){
			//System.out.println("draw_GM_ip: the intersection point at Line " + line_id + ", Point " + point_id +" is null.");
			return false;
		}
		
		int ip_x = get_GM_x_pix(intersection_points[line_id][point_id][0]);
		int ip_y = get_GM_y_pix(intersection_points[line_id][point_id][1]);
		Coordinates ip_c = new Coordinates(ip_x, ip_y);

		gmIPPrimitives.addElement(lang.newCircle(ip_c, 3, line_id+"_"+point_id, null, pointProperties));
		return true;
	}
	

	private int get_GM_x_pix(int x_coord){
		int x_coord_pix_ratio = GM_XL / X_COORD_N; 		
		return (GM_X0 + (x_coord * x_coord_pix_ratio));
    }
    private int get_GM_y_pix(int y_coord){
		int y_coord_pix_ratio = GM_YL / Y_COORD_N;
		int gm_yn = GM_Y0 + GM_YL;
		return (gm_yn - (y_coord* y_coord_pix_ratio));
    }
    private int get_GM_x_pix(double x_coord){
		int x_coord_pix_ratio = GM_XL / X_COORD_N; 	
		return (GM_X0 + (int)(x_coord * x_coord_pix_ratio));
    }
    private int get_GM_y_pix(double y_coord){
		int y_coord_pix_ratio = GM_YL / Y_COORD_N;
		int gm_yn = GM_Y0 + GM_YL;
		return (gm_yn - (int)(y_coord * y_coord_pix_ratio));
    }
	

	/**
	 * generate method for generating in Animal
	 */
    @Override
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        clippingPlaneProperties = (RectProperties)props.getPropertiesByName("clippingPlaneProperties");
        highlightCurrentAreaProperties = (RectProperties)props.getPropertiesByName("highlightCurrentAreaProperties");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        strongHighlightAreaProperties = (RectProperties)props.getPropertiesByName("strongHighlightAreaProperties");
        lineProperties = (PolylineProperties)props.getPropertiesByName("lineProperties");
        lineArrayProperties = (MatrixProperties)props.getPropertiesByName("lineArrayProperties");
        clippingPlane = (int[][])primitives.get("clippingPlane");
        clippedLineProperties = (PolylineProperties)props.getPropertiesByName("clippedLineProperties");
        gmCodeGraphicProperties = (PolylineProperties)props.getPropertiesByName("gmCodeGraphicProperties");
        pointProperties = (CircleProperties)props.getPropertiesByName("pointProperties");
        introProperties = (SourceCodeProperties)props.getPropertiesByName("introProperties");
        lines = (int[][])primitives.get("lines");
        calcHighlightProperties = (TextProperties)props.getPropertiesByName("calcHighlightProperties");
        unhighlightAreaProperties = (RectProperties)props.getPropertiesByName("unhighlightAreaProperties");
 
		init();
		introduction();
		initUnchangeableProperties();
		invalidInputCheck();
		executeAlgoWithUserInput();
		conclusion();
        
        return lang.toString();
    }

    @Override
	public String getName() {
        return translator.translateMessage("TITLE");
    }

    @Override
	public String getAlgorithmName() {
        return "Cohen-Sutherland";
    }

    @Override
	public String getAnimationAuthor() {
        return "David Steiner,Jens Krüger";
    }

    @Override
	public String getDescription(){
    	return translator.translateMessage("INTRODUCTION_HTML");
    }

    @Override
	public String getCodeExample(){
        return translator.translateMessage("SOURCE_CODE_INTRO");
    }

    @Override
	public String getFileExtension(){
        return "asu";
    }

    @Override
	public Locale getContentLocale() {
        return location;
    }

    @Override
	public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }
    

    @Override
	public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public static void writeOutputToFile(String animal_str){
    	File file = new File("test.asu");
    	try {
			FileWriter fileWriter = new FileWriter(file, false);
			fileWriter.write(animal_str);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("generate done");
    }
    /*
    public static void main(String[] args){ //for error tracing
        CohenSutherland csa = new CohenSutherland(Locale.GERMANY);
        String out = csa.strartFromMain();
        //writeOutputToFile(out);
    }
 
	*/
}
