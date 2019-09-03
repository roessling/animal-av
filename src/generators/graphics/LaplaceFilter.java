/*
 * LaplaceFilter.java
 * Kristina Raysbikh, Victoria Stanilescu, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;

public class LaplaceFilter implements ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties scrCodeProperty;
    private MatrixProperties matrixProperties;
    public void init(){
        lang = new AnimalScript("Laplace Filter", "Kristina Raysbikh, Victoria Stanilescu", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
    	int[][] filter = (int[][])primitives.get("Filter");
        int[][] inputPicture = (int[][])primitives.get("Source Picture");
        String str = (String)primitives.get("borderHandling");
        scrCodeProperty = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties");
        matrixProperties = (MatrixProperties)props.getPropertiesByName("matrixProperties");
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        this.laplaceFilter(inputPicture, filter, str);
        lang.finalizeGeneration();
        return lang.toString();
    }
    
    @Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
    	int[][] inputPicture = (int[][]) arg1.get("Source Picture");
    	int[][] filter = (int[][]) arg1.get("Filter");
    	String str = (String)arg1.get("borderHandling");
    	 
    	if(filter.length != 3 || filter[0].length != 3){
    		JOptionPane.showMessageDialog(null, "The kernel should be a 3x3 matrix", "Invalid kernel", JOptionPane.WARNING_MESSAGE);
			return false;
    	}
    	
    	if(inputPicture.length < filter.length || inputPicture[0].length < filter.length){
    		JOptionPane.showMessageDialog(null, "The source image should not be smaller than the kernel.", "Invalid source image", JOptionPane.WARNING_MESSAGE);
			return false;
    	}
    	
    	if(inputPicture.length > 15 || inputPicture[0].length > 15){
    		JOptionPane.showMessageDialog(null, "The source image is too big.", "Invalid source image", JOptionPane.WARNING_MESSAGE);
			return false;
    	} 
    	
    	if(!str.equals("rp")){
    		if(!str.equals("cp")){
    			if(!str.equals("wa")){
    				if(!str.equals("zp")){
    					JOptionPane.showMessageDialog(null, "Unknown border handling method.", "Invalid input", JOptionPane.WARNING_MESSAGE);
    					return false;	
    				}
    			}
    		}
    	}  		
		
		return true;
	}

    public String getName() {
        return "Laplace Filter";
    }

    public String getAlgorithmName() {
        return "Laplace Filter [DE]";
    }

    public String getAnimationAuthor() {
        return "Kristina Raysbikh, Victoria Stanilescu";
    }

    public String getDescription(){
        return "Der Laplace-Filter ist ein Filter zur Kantendetektion, der die Summe der beiden reinen zweiten Ableitungen approximiert: "
 +"\n"
 +"L(x,y) = d^2I/dx^2 + d^2I/dy^2, wobei I = I(x,y) - Pixelintensitaet. "
 +"\n"
 +"Er berechnet den Farbwertgradienten an jedem einzelnen Bildpunkt eines Bildes "
 +"\n"
 +"durch Untersuchung eines den Punkt umgebenden Bereiches. Dieser Vorgang erfolgt durch diskrete Faltung des Bildes "
 +"\n"
 +"mit einer Faltungsmatrix, die eine ungerade Anzahl an Spalten und Zeilen haben soll."
 +"\n"
 +"Die daraus entstehnde Matrix der Gradienten wird als Kantenbild genannt."
 +"\n"
 +"Man verwendet eine der folgenden Faltungsmatrizen:"
 +"\n"
 +"\n"
 +"	|0  1  0|"
 +"\n"
 +"         D1 = 	|1 -4  1|"
 +"\n"
 +"	|0  1  0|"
 +"\n"
 +"\n"
 +"	 |1  1  1|"
 +"\n"
 +"         D2 =	 |1 -8  1|"
 +"\n"
 +"	 |1  1  1|"
 +"\n"
 +"\n"
 +"Die Faltungsmatrix D2 spricht im Unterschied zu D1 zusaetzlich auf 45°- Kanten an.";
    }

    public String getCodeExample(){
        return "public static int[][] laplaceFilter(int[][] src, int[][] filterMaske){"
 +"\n"
 +"          int row = src.length;"
 +"\n"
 +"          int col = src[0].length;"
 +"\n"
 +"          int[][] dst = new int[row][col];"
 +"\n"
 +"          copyBorder(src, dst);"
 +"\n"
 +"		"
 +"\n"
 +"          for(int i=1; i< row-1; i++){"
 +"\n"
 +"              for(int j=1; j<col-1; j++){"
 +"\n"
 +"				"
 +"\n"
 +"                   dst[i][j] = applyFilter(src, filterMaske, i, j);"
 +"\n"
 +"               }"
 +"\n"
 +"          }"
 +"\n"
 +"		"
 +"\n"
 +"          return dst;"
 +"\n"
 +"}"
 +"\n"
 +"\n"
 +"private static int applyFilter(int[][] picture,int[][] filter, int xP, int yP){"
 +"\n"
 +"		"
 +"\n"
 +"          int value = 0;"
 +"\n"
 +"          int row = 0;"
 +"\n"
 +"          int col = 0;"
 +"\n"
 +"		"
 +"\n"
 +"          for(int i = xP-1; i <= xP+1; i++){"
 +"\n"
 +"              for(int j = yP-1; j <= yP+1; j++){"
 +"\n"
 +"              value = value + picture[i][j]*filter[row][col];"
 +"\n"
 +"              col++;"
 +"\n"
 +"              }"
 +"\n"
 +"          row++;"
 +"\n"
 +"          col=0;"
 +"\n"
 +"          }"
 +"\n"
 +"		"
 +"\n"
 +"return value;"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    
    /**
	 * examples for border handling
	 */
	private final int[][] zeropadding = new int[][]{{0,0,0,0,0},{0,1,2,3,0},{0,4,5,6,0},{0,7,8,9,0},{0,0,0,0,0}};
	private final int[][] replication = new int[][]{{1,1,2,3,3},{1,1,2,3,3},{4,4,5,6,6},{7,7,8,9,9},{7,7,8,9,9}};
	private final int[][] wraparound = new int[][]{{9,7,8,9,7},{3,1,2,3,1},{6,4,5,6,4},{9,7,8,9,7},{3,1,2,3,1}};
	
	/**
	   * default duration for swap processes
	   */
	  public final static Timing  defaultDuration = new TicksTiming(30);
	  
	  
	  public void laplaceFilter(int[][] src, int[][] kernel, String border){
		// Titel
		TextProperties headerProperties = new TextProperties();
		headerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		headerProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 500);
		headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		Text header = lang.newText(new Coordinates(500, 0), "Laplace Filter", "header", null, headerProperties);

		// ******************************************//
	    //  			SKETCH                       //
	   // ******************************************//
		  
		MatrixProperties matProp1 = new MatrixProperties();
		matProp1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		matProp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		matProp1.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.CYAN);
		matProp1.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.CYAN);
		matProp1.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, Color.BLACK);
		matProp1.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table"); 
		
		IntMatrix mat1 = lang.newIntMatrix(new Coordinates(10, 250), new int[3][3], "mat1", null, matProp1);
		mat1.highlightCellRowRange(0, 2, 0, defaultDuration, defaultDuration);
		mat1.highlightCellRowRange(0, 2, 1, defaultDuration, defaultDuration);
		mat1.highlightCellRowRange(0, 2, 2, defaultDuration, defaultDuration);
		
		MatrixProperties matProp2 = new MatrixProperties();
		matProp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		matProp2.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		matProp2.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.GRAY);
		matProp2.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GRAY);
		matProp2.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table"); 
		
		IntMatrix mat2 = lang.newIntMatrix(new Offset(20, 0, mat1, "E"), new int[5][5], "mat2", null, matProp2);
		mat2.highlightCellRowRange(0, 4, 0, defaultDuration, defaultDuration);
		mat2.highlightCellRowRange(0, 4, 1, defaultDuration, defaultDuration);
		mat2.highlightCellRowRange(0, 4, 2, defaultDuration, defaultDuration);
		mat2.highlightCellRowRange(0, 4, 3, defaultDuration, defaultDuration);
		mat2.highlightCellRowRange(0, 4, 4, defaultDuration, defaultDuration);
		
		IntMatrix mat3 = lang.newIntMatrix(new Offset(20, 0, mat2, "NE"), new int[5][5], "mat3", null, matProp2);
		mat3.highlightCellRowRange(0, 4, 0, defaultDuration, defaultDuration);
		mat3.highlightCellRowRange(0, 4, 1, defaultDuration, defaultDuration);
		mat3.highlightCellRowRange(0, 4, 2, defaultDuration, defaultDuration);
		mat3.highlightCellRowRange(0, 4, 3, defaultDuration, defaultDuration);
		mat3.highlightCellRowRange(0, 4, 4, defaultDuration, defaultDuration);
		
		IntMatrix mat4 = lang.newIntMatrix(new Offset(0,0, mat2, "NW"), new int[3][3], "mat4", null, matProp1);
		mat4.highlightCellRowRange(0, 2, 0, defaultDuration, defaultDuration);
		mat4.highlightCellRowRange(0, 2, 1, defaultDuration, defaultDuration);
		mat4.highlightCellRowRange(0, 2, 2, defaultDuration, defaultDuration);
		
		
		PolylineProperties pp1 = new PolylineProperties();
		pp1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		pp1.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
		
		Node[] node1 = {new Offset(0,0,mat1, "NW"), new Offset(0,0,mat4, "NW")};
		Polyline line1 = lang.newPolyline(node1, "line1", null, pp1);
			
		Node[] node2 = {new Offset(0,0,mat1, "NE"), new Offset(0,0,mat4, "NE")};
		Polyline line2 = lang.newPolyline(node2, "line2", null, pp1);
			
		Node[] node3 = {new Offset(0,0,mat1, "SW"), new Offset(0,0,mat4, "SW")};
		Polyline line3 = lang.newPolyline(node3, "line3", null, pp1);
			
		Node[] node4 = {new Offset(0,0,mat1, "SE"), new Offset(0,0,mat4, "SE")};
		Polyline line4 = lang.newPolyline(node4, "line4", null, pp1);
		
		PolylineProperties pp2 = new PolylineProperties();
		pp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		pp2.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
		
		Node[] node5 = {new Offset(0,0,mat1, "C"), new Offset(0,0,mat4, "C")};
		Polyline line5 = lang.newPolyline(node5, "line5", null, pp2);
		
		Node[] node6 = {new Offset(0,0,mat4, "C"), new Offset(30,40,mat3, "NW")};
		Polyline line6 = lang.newPolyline(node6, "line6", null, pp2);
		
		TextProperties sketchProperties = new TextProperties();
		sketchProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		sketchProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 500);
		sketchProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10)); 
		
		Text sketchFilter = lang.newText(new Offset(0, -15, mat1, "NW"), "Filtermatrix", "filterm", null,  sketchProperties);
		Text sketchEing = lang.newText(new Offset(0, 10, mat2, "SW"), "Eingangsbild", "Eingangsbild", null, sketchProperties);
		Text sketchAus = lang.newText(new Offset(0, 10, mat3, "SW"), "Ausgangsbild", "Ausgangsbild", null, sketchProperties);
		
		// the visual properties for the source code
	    SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 14));
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		SourceCode firstPage = lang.newSourceCode(new Offset(-250, 50, header, "S"), "description", null,scProps);
		firstPage.addCodeLine("Ein Kantendetektor berechnet in der Regel den Farbwertgradienten an jedem einzelnen Bildpunkt", null, 0, null);
		firstPage.addCodeLine("eines Bildes durch Untersuchung eines den Punkt umgebenden Bereiches. ", null, 0, null);
		firstPage.addCodeLine("Dieser Vorgang erfolgt durch diskrete Faltung", null, 0, null);
		firstPage.addCodeLine("des Bildes mit einer Faltungsmatrix, dem Kantenoperator. Letztere definiert", null, 0, null);
		firstPage.addCodeLine("dabei die Groesse des zu untersuchenden Umfelds und mit welcher Wichtung", null, 0, null);
		firstPage.addCodeLine("dessen einzelne Bildpunkte in die Berechnung eingehen.", null, 0, null);
		lang.nextStep("Einleitung");
		SourceCode secondPage = lang.newSourceCode(new Offset(0, 20, firstPage, "SW"), "description",null, scProps);
		secondPage.addCodeLine("Die Ableitung der Funktion ist also der Schluessel zur Kantendetektion.", null, 0, null);
		secondPage.addCodeLine("Prewitt-Operator, Sobel-Operator, Roberts-Operator, Kompass-Operatoren messen nur die erste Ableitung.", null, 0, null);
		secondPage.addCodeLine("Problematisch sind dabei Kanten mit einem langsamen Helligkeitswechsel,", null, 0, null);
		secondPage.addCodeLine("die sich damit nicht genau lokalisieren lassen. Die im Laplace-Filter verwendete Loesung ist", null, 0, null);
		secondPage.addCodeLine("Bestimmung des Nulldurchgangs der zweiten Ableitung.", null, 0, null);
	
		lang.nextStep();
		lang.hideAllPrimitivesExcept(header);
		
		Text intro1 = lang.newText(new Coordinates(250, 200), "Am Ende dieser Animation koennen Sie die Verstaendisfragen " +
				"beantworten,", "introduction", null, headerProperties);
		Text intro2 = lang.newText(new Coordinates(250, 220), "um zu pruefen, ob Sie den Algorithmus richtig verstanden haben.", "introduction", null, headerProperties);
		lang.nextStep();
		intro1.hide();
		intro2.hide();
		// visual properties for the source, filter and destination		  
		MatrixProperties matProp = new MatrixProperties();
		matProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		matProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		matProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		matProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.WHITE);
		matProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.CYAN);
		matProp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table"); 
			
		// create the source picture
		IntMatrix srcPic = lang.newIntMatrix(new Coordinates(40, 80), new int[src.length+1][src[0].length+1], "intMatrix", null, matrixProperties);
	    setCoordinates(srcPic, src);
		srcPic.hide();
		// create filter
		IntMatrix filter = lang.newIntMatrix(new Offset(50, 25, srcPic, "NE"), kernel, "intMatrix", null, matrixProperties);
		filter.hide();
	
		// description of primitives
		TextProperties descPrims = new TextProperties();
		descPrims.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		descPrims.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
		Text  source = lang.newText(new Offset(20, -25, srcPic, "NW"), "Source:", "header", null, descPrims);
		// create description of filter
		Text fil = lang.newText(new Offset(0, -50, filter, "NW"), "Filter:", "description",null, descPrims);
		// create the source code entity
		SourceCode sc = lang.newSourceCode(new Offset(0, 25, srcPic, "SW"), "sourceCode", null, scrCodeProperty);
		sc.addCodeLine("public static int[][] laplaceFilter(int[][] src, int[][] filterMaske){", null, 0, null); // 0
		sc.addCodeLine("int row = src.length;", null, 1, null); // 1
		sc.addCodeLine("int col = src[0].length;", null, 1, null); // 2
		sc.addCodeLine("int[][] dst = new int[row][col];", null, 1, null); // 3
		if(border.equals("cp")) sc.addCodeLine("copyBorder(src, dst);", null, 1, null); // 4
		if(border.equals("zp")) sc.addCodeLine("zeroPadding(src);", null, 1, null); // 4
		if(border.equals("wa")) sc.addCodeLine("wraparound(src);", null, 1, null); // 4
		if(border.equals("rp")) sc.addCodeLine("replication(src);", null, 1, null); // 4
		sc.addCodeLine("for(int i=1; i < row-1; i++){", null, 1, null); // 5
		sc.addCodeLine(" for(int j=1; j < col-1; j++){", null, 2, null); // 6
		sc.addCodeLine(" dst[i][j] = applyFilter(src, filterMaske, i, j);", null, 3, null); // 7
		sc.addCodeLine("  }", null, 1, null); // 8
		sc.addCodeLine(" }", null, 2, null); // 9
		sc.addCodeLine(" return dst;", null, 1, null); // 10
		sc.addCodeLine("}", null, 0, null); // 11
		sc.addCodeLine("private static int applyFilter(int[][] scr,int[][] filter, int xP, int yP){", null, 0, null); // 12
		sc.addCodeLine("int value = 0;", null, 1, null); // 13
		sc.addCodeLine("int row = 0;", null, 1, null); // 14
		sc.addCodeLine("int col = 0;", null, 1, null); // 15
		sc.addCodeLine("for(int i = xP-1; i <= xP+1; i++){", null, 1, null); // 16
		sc.addCodeLine("for(int j = yP-1; j <= yP+1; j++){", null, 2, null); // 17
		sc.addCodeLine("value = value + picture[i][j]*filter[row][col];", null, 3, null); // 18
		sc.addCodeLine("col++;", null, 3, null); // 19
		sc.addCodeLine("}", null, 2, null); // 20
		sc.addCodeLine("row++;", null, 1, null); // 21
		sc.addCodeLine("col=0;", null, 1, null); // 22
		sc.addCodeLine("}", null, 1, null); // 23
		sc.addCodeLine("return value;", null, 1, null); // 24
		sc.addCodeLine("}", null, 0, null); // 25
		
		// start a new step after the source code was created
		lang.nextStep("Initialisierung");
		sc.highlight(0);
		srcPic.show();
		filter.show();
		// start a new step after matrices were created
		lang.nextStep();
		sc.unhighlight(0);
		sc.highlight(1);
		sc.highlight(2);
		// start a new step
		lang.nextStep();
		sc.unhighlight(1);
		sc.unhighlight(2);
		sc.highlight(3);
		
		// create destination picture
		int[][] dst = new int[src.length][src[0].length];
		IntMatrix dstPic = lang.newIntMatrix(new Offset(50, -25, filter, "NE"), new int[src.length+1][src[0].length+1], "intMatrix", null, matrixProperties);
		setCoordinates(dstPic, dst);
		// create description of destination
		Text destination = lang.newText(new Offset(20, -25, dstPic, "NW"), "Destination:", "description",null,descPrims);
		// border handling step 1
		lang.nextStep("Randbehandlung");
	    InfoBox[] infobox = borderHandling(sc);
	    infobox[0].show();
	    // border handling step 2
	    lang.nextStep();
	    infobox[0].hide();
	    infobox[1].show();
	    IntMatrix zeroPadding = lang.newIntMatrix(new Offset(150, 80, sc, "NE"), zeropadding, "ZeroPadding", null, matProp);
	    zeroPadding.highlightCellColumnRange(0, 0, 4, defaultDuration, defaultDuration);
	    zeroPadding.highlightCellColumnRange(4, 0, 4,defaultDuration, defaultDuration);
	    zeroPadding.highlightCellRowRange(1, 3, 0, defaultDuration, defaultDuration);
	    zeroPadding.highlightCellRowRange(1, 3, 4, defaultDuration, defaultDuration);
	    // border handling step 3
	    lang.nextStep();
	    infobox[1].hide();
	    zeroPadding.hide();
	    infobox[2].show();
	    IntMatrix replicat = lang.newIntMatrix(new Offset(150, 80, sc, "NE"), replication, "Replication", null, matProp);
	    replicat.highlightCellColumnRange(0, 0, 4, defaultDuration, defaultDuration);
	    replicat.highlightCellColumnRange(4, 0, 4,defaultDuration, defaultDuration);
	    replicat.highlightCellRowRange(1, 3, 0, defaultDuration, defaultDuration);
	    replicat.highlightCellRowRange(1, 3, 4, defaultDuration, defaultDuration);
	    // border handling step 4
	    lang.nextStep();
	    infobox[2].hide();
	    replicat.hide();
	    infobox[3].show();
	    IntMatrix wrapAround = lang.newIntMatrix(new Offset(150, 80, sc, "NE"), wraparound, "Wraparound", null, matProp);
	    wrapAround.highlightCellColumnRange(0, 0, 4, defaultDuration, defaultDuration);
	    wrapAround.highlightCellColumnRange(4, 0, 4,defaultDuration, defaultDuration);
	    wrapAround.highlightCellRowRange(1, 3, 0, defaultDuration, defaultDuration);
	    wrapAround.highlightCellRowRange(1, 3, 4, defaultDuration, defaultDuration);
	    // border handling step 5
	    lang.nextStep();
	    infobox[3].hide();
	    wrapAround.hide();
	    infobox[4].show();
		// start a new step
		lang.nextStep();
		srcPic.hide();
		IntMatrix srcPic1 = null;
		sc.unhighlight(3);
		sc.highlight(4);
		if(border.equals("cp")){
			srcPic.show();
			srcPic1 = srcPic;
			copyBorder(srcPic1, dstPic);
			
			
		}
		if(border.equals("wa")){
			int[][] src1 =  wraparound(src);
		    srcPic1 = lang.newIntMatrix(new Coordinates(40, 80), new int[src1.length+1][src1[0].length+1], "intMatrix", null, matrixProperties);
		    setCoordinates(srcPic1, src1);
		    srcPic1.highlightCellColumnRange(1, 1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellColumnRange(srcPic1.getNrRows()-1, 1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellRowRange(2, srcPic1.getNrRows()-1, 1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellRowRange(2, srcPic1.getNrRows()-1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		}
		if(border.equals("rp")){
			int[][] src1 = replication(src);
			srcPic1 = lang.newIntMatrix(new Coordinates(40, 80),new int[src1.length+1][src1[0].length+1], "intMatrix", null, matrixProperties);
		    setCoordinates(srcPic1, src1);
			srcPic1.highlightCellColumnRange(1, 1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellColumnRange(srcPic1.getNrRows()-1, 1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellRowRange(2, srcPic1.getNrRows()-1, 1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellRowRange(2, srcPic1.getNrRows()-1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		}
		if(border.equals("zp")){
			int[][] src1 = zeroPadding(src);
			srcPic1 = lang.newIntMatrix(new Coordinates(40, 80), new int[src1.length+1][src1[0].length+1], "intMatrix", null, matrixProperties);
			setCoordinates(srcPic1, src1);
			srcPic1.highlightCellColumnRange(1, 1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellColumnRange(srcPic1.getNrRows()-1, 1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellRowRange(2, srcPic1.getNrRows()-1, 1, defaultDuration, defaultDuration);
		    srcPic1.highlightCellRowRange(2, srcPic1.getNrRows()-1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		} 
		if(!border.equals("cp")){
			filter.moveBy(null, 50, 0, defaultDuration, defaultDuration);
			fil.moveBy(null, 50, 0, defaultDuration, defaultDuration);
			dstPic.moveBy(null, 50, 0, defaultDuration, defaultDuration);
			destination.moveBy(null, 50, 0, defaultDuration, defaultDuration);
			sc.moveBy(null, 0, 25, defaultDuration, defaultDuration);
		}
		// start a new step
		lang.nextStep();
		infobox[4].hide();
	
    	// unhighleight first row
		dstPic.unhighlightCellColumnRange(1, 1, dstPic.getNrCols()-1, defaultDuration, defaultDuration);
		srcPic1.unhighlightCellColumnRange(1, 1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		// unhighleight last row
		dstPic.unhighlightCellColumnRange(dstPic.getNrRows()-1, 1, dstPic.getNrCols()-1, defaultDuration, defaultDuration);
		srcPic1.unhighlightCellColumnRange(srcPic1.getNrRows()-1, 1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		// unhighleight first column
		dstPic.unhighlightCellRowRange(2, dstPic.getNrRows()-1, 1, defaultDuration, defaultDuration);
		srcPic1.unhighlightCellRowRange(2, srcPic1.getNrRows()-1, 1, defaultDuration, defaultDuration);
		// unhighleight last column
		dstPic.unhighlightCellRowRange(2, dstPic.getNrRows()-1, dstPic.getNrCols()-1, defaultDuration, defaultDuration);
		srcPic1.unhighlightCellRowRange(2, srcPic1.getNrRows()-1, srcPic1.getNrCols()-1, defaultDuration, defaultDuration);
		
		sc.unhighlight(4);
		sc.highlight(5);
		sc.highlight(6);
		// start a new step
		lang.nextStep("Berechnung von Werten");
		sc.unhighlight(5);
		sc.unhighlight(6);
		//***********************************************
		// 	Computation of values 
		//***********************************************
		SourceCode descript = lang.newSourceCode(new Offset(50, 0, sc, "NE"), "comput", null, scProps);
		descript.addCodeLine("Fuer jedes Pixel im Eingangsbild (in unserem Fall ausser Randpixel)", null, 0, defaultDuration);
		descript.addCodeLine("wird ein neuer Wert durch Untersuchung des umgebenden Bereiches berechnet:", null, 0, defaultDuration); 
		descript.addCodeLine("[] die Pixel des Eingangsbildes werden mit den Werten der Filtermatrix ", null, 0, defaultDuration);
		descript.addCodeLine("multipliziert und zusammenaddiert. ", null, 0, defaultDuration);
		descript.addCodeLine("[] genau dasseble wird fuer die zweite Zeile gemacht..", null, 0, defaultDuration);
		descript.addCodeLine("[]..und auch fuer die dritte Zeile.", null, 0, defaultDuration);
		descript.addCodeLine("[] der neue Wert ist die Summe ueber alle Zeilen", null, 0, defaultDuration);
		for(int i = 2; i < srcPic1.getNrRows()-1; i++){
			for(int j = 2; j < srcPic1.getNrCols()-1; j++){
				srcPic1.highlightCell(i, j, defaultDuration, defaultDuration);
				sc.highlight(7);
				sc.highlight(12);
				sc.highlight(16);
				sc.highlight(17);
				// start a new step
				lang.nextStep();
				sc.unhighlight(16);
				sc.unhighlight(17);
				sc.highlight(18);
				
				if(border.equals("cp")){
					dstPic.highlightCell(i, j, defaultDuration, defaultDuration);
				}else{
					dstPic.highlightCell(i-1, j-1, defaultDuration, defaultDuration);
				}
				
				int result = applyFilter(srcPic1, filter, i, j);
				// compute first row
				lang.nextStep();
				SourceCode ar = lang.newSourceCode(new Offset(50, 10, dstPic, "NE"), "computation", null, scProps);
				StringBuilder sb1 = new StringBuilder();
				sb1.append(srcPic1.getElement(i-1, j-1));	sb1.append("*"); sb1.append(filter.getElement(0, 0));
				sb1.append(" + "); sb1.append(srcPic1.getElement(i-1, j)); sb1.append("*"); sb1.append(filter.getElement(0, 1));
				sb1.append(" + "); sb1.append(srcPic1.getElement(i-1, j+1)); sb1.append("*"); sb1.append(filter.getElement(0, 2));
				ar.addCodeLine(sb1.toString(), null, 0, defaultDuration);
				srcPic1.highlightCellColumnRange(i-1, j-1, j+1, defaultDuration, defaultDuration);
				filter.highlightCellColumnRange(0, 0, 2, defaultDuration, defaultDuration);
				descript.highlight(2);
				descript.highlight(3);
				// compute second row
				lang.nextStep();
				ar.addCodeLine("+", null, 0, defaultDuration);
				StringBuilder sb2 = new StringBuilder();
				sb2.append(srcPic1.getElement(i, j-1));	sb2.append("*"); sb2.append(filter.getElement(1, 0));
				sb2.append(" + "); sb2.append(srcPic1.getElement(i, j)); sb2.append("*"); sb2.append(filter.getElement(1, 1));
				sb2.append(" + "); sb2.append(srcPic1.getElement(i, j+1)); sb2.append("*"); sb2.append(filter.getElement(1, 2));
				ar.addCodeLine(sb2.toString(), null, 0, defaultDuration);
				srcPic1.unhighlightCellColumnRange(i-1, j-1, j+1, defaultDuration, defaultDuration);
				srcPic1.highlightCellColumnRange(i, j-1, j+1, defaultDuration, defaultDuration);
				filter.unhighlightCellColumnRange(0, 0, 2, defaultDuration, defaultDuration);
				filter.highlightCellColumnRange(1, 0, 2, defaultDuration, defaultDuration);
				descript.unhighlight(2);
				descript.unhighlight(3);
				descript.highlight(4);

				// compute third row
				lang.nextStep();
				ar.addCodeLine("+", null, 0, defaultDuration);
				StringBuilder sb3 = new StringBuilder();
				sb3.append(srcPic1.getElement(i+1, j-1));	sb3.append("*"); sb3.append(filter.getElement(2, 0));
				sb3.append(" + "); sb3.append(srcPic1.getElement(i+1, j)); sb3.append("*"); sb3.append(filter.getElement(2, 1));
				sb3.append(" + "); sb3.append(srcPic1.getElement(i+1, j+1)); sb3.append("*"); sb3.append(filter.getElement(2, 2));
				ar.addCodeLine(sb3.toString(), null, 0, defaultDuration);
				srcPic1.unhighlightCellColumnRange(i, j-1, j+1, defaultDuration, defaultDuration);
				srcPic1.highlightCellColumnRange(i+1, j-1, j+1, defaultDuration, defaultDuration);
				srcPic1.highlightCell(i, j, defaultDuration, defaultDuration);
				filter.unhighlightCellColumnRange(1, 0, 2, defaultDuration, defaultDuration);
				filter.highlightCellColumnRange(2, 0, 2, defaultDuration, defaultDuration);
				descript.unhighlight(4);
				descript.highlight(5);
			
				// compute result
				lang.nextStep();
				ar.addCodeLine("--------------------", null, 0, defaultDuration);
				String st = "                  "+result;
				ar.addCodeLine(st, null, 0, defaultDuration);
				srcPic1.unhighlightCellColumnRange(i+1, j-1, j+1, defaultDuration, defaultDuration);
				srcPic1.unhighlightCell(i+1, j+1, defaultDuration, defaultDuration);
				if(border.equals("cp")){
					dstPic.put(i, j, result, defaultDuration, defaultDuration);
					}
					else{
						dstPic.put(i-1, j-1, result, defaultDuration, defaultDuration);
					}
				sc.unhighlight(18);
				filter.unhighlightCellColumnRange(2, 0, 2, defaultDuration, defaultDuration);
				descript.unhighlight(5);
				descript.highlight(6);
			
				// start a new step
				lang.nextStep();
				ar.hide();
				descript.unhighlight(6);
				srcPic1.unhighlightCell(i, j, defaultDuration, defaultDuration);
				if(border.equals("cp")){
					dstPic.unhighlightCell(i, j, defaultDuration, defaultDuration);
				}else{
					dstPic.unhighlightCell(i-1, j-1, defaultDuration, defaultDuration);
				}
			}
		}
		sc.unhighlight(7);
		sc.unhighlight(12);
		sc.highlight(10);
		descript.hide();
		// start a new step
		lang.nextStep();
		sc.unhighlight(10);
		lang.nextStep();
		srcPic.hide();
		List<Primitive> list = new ArrayList<Primitive>();
		list.add(header);
		list.add(destination);
		list.add(dstPic);
		lang.hideAllPrimitivesExcept(list);
		SourceCode conclusion = lang.newSourceCode(new Offset(20, 0, dstPic, "SE"), "conclusion",null, scProps);
		conclusion.addCodeLine("Der Algorithmus hat terminiert. ", null, 0, null);
		conclusion.addCodeLine("Die sich so ergebende Matrix der Gradienten an jedem Bildpunkt", null, 0, null);
		conclusion.addCodeLine("wird als Bild interpretiert – Kantenbild genannt.", null, 0, null);
		conclusion.addCodeLine("Da die zweite Ableitung noch empfindlicher gegen Rauschen ist,", null, 0, null);
		conclusion.addCodeLine("muss das Bild gleichzeitig geglaettet werden.", null, 0, null);
		conclusion.addCodeLine("Dies kann durch die Anwendung eines Gaußfilters vor", null, 0, null);
		conclusion.addCodeLine("der eigentlichen Kantenfilterung geschehen.", null, 0, null);
		
		lang.nextStep("Verstaendnisfragen");
		// Question 1
		MultipleChoiceQuestionModel aim = new MultipleChoiceQuestionModel("Aim of the filter");
		aim.setPrompt("Laplace Filter dient ");
		aim.addAnswer("zur Kantendetektion", 1, "richtig");
		aim.addAnswer("zur Berechnung einen Mittelwert", 0, "falsch, er dient zur Kantendetektion");
		aim.addAnswer("zur Erkennung einer Maus", 0, "falsch, er dient zur Kantendetektion");
		lang.addMCQuestion(aim);
		lang.nextStep();
		// Question 2
		MultipleChoiceQuestionModel calculation = new MultipleChoiceQuestionModel("Calculation");
		calculation.setPrompt("Wie berechnet Laplace Filter einen Wert?");
		calculation.addAnswer("es wird ein minimaler Wert gewaehlt", 0, "falsch, das ist ein Medianfilter");
		calculation.addAnswer("es wird die erste Ableitung gemessen", 0, "falsch,das ist z.B. ein Sobel-Operator  ");
		calculation.addAnswer("es wird ein maximaler Wert gewaehlt", 0, "falsch, das ist ein Maximumfilter");
		calculation.addAnswer("es wird die zweite Ableitung gemessen", 1, "richtig!");
		lang.addMCQuestion(calculation);
		lang.nextStep();
		// Question 3
		TrueFalseQuestionModel rand = new TrueFalseQuestionModel("Randbehandlung", false, 1);
		rand.setPrompt("Es gibt nur eine Methode, die das Randproblem loest");
		rand.setFeedbackForAnswer(false, "richtig, es gibt mehr als eine Methode, " +
				"einige davon haben Sie in dieser Animation kennengelernt.");
		rand.setFeedbackForAnswer(true, "falsch, es gibt mehr als eine Methode. " +
				"In dieser Animation haben Sie die folgenden Methoden gesehen: " +
				"Zero Padding, Replication, Wraparound, No filter at the edge");
		lang.addTFQuestion(rand); 
		
		lang.nextStep();
		// Question 4
		FillInBlanksQuestionModel computeValue = new FillInBlanksQuestionModel("");
		computeValue.setPrompt("Berechnen Sie den Wert fuer das Pixel 2 im folgenden Bild"  +
				"\n           |0  1  0|"+
				"\n Bild = |1  2  1|" +
				"\n           |0  1  0|" +
				"\n " +
				"Benutzen Sie dabei die folgende Filtermatrix:" +
				"\n             |0  1  0|" +
				"\n Filter = |1 -4  1|" +
				"\n             |0  1  0|"
				);
		computeValue.addAnswer("-4", 1, "Ihre Antwort ist richtig");
		lang.addFIBQuestion(computeValue);
		lang.nextStep();
		lang.hideAllPrimitives();
		Text result1 = lang.newText(new Offset(-250, 100, header, "S"), "Wenn Sie Ihren Resultat von Questions ansehen moechten,", "result", null, headerProperties);
		Text result2 = lang.newText(new Offset(0, 10, result1, "SW"), "dann waehlen Sie in 'Animal Control Center' Help -> Quiz Results ", "result", null, headerProperties);
		lang.nextStep();
		result1.hide();
		result2.hide();
		
	  }
	  
	 /**
	  * 
	  * @param picture is source picture 
	  * @param filter is filter
	  * @param xP x-coordinate
	  * @param yP y-coordinate
	  * @return value
	  */
	  private static int applyFilter(IntMatrix picture,IntMatrix filter, int xP, int yP){

			int value = 0;
			int row = 0;
			int col = 0;
			
			for(int i = xP-1; i <= xP+1; i++){
				for(int j = yP-1; j <= yP+1; j++){
					value += picture.getElement(i, j)*filter.getElement(row, col);
					col++;
				}
				
				row++;
				col=0;
			}
			return value;
		}
	  
	  /**
	   * create an information about border handling
	   * @param node - offset node
	   * @return an array of information boxes
	   */
	  private InfoBox[] borderHandling(SourceCode node){
		  // first information box
		  InfoBox ib1 = new InfoBox(lang, new Offset(550, 0 , node, "intro"), 4, "Randproblemen bei Filtern");
		  List<String> randProblem = new ArrayList<String>();
		  randProblem.add("Je nach Groesse der Filtermaske existiert ein Bildrand (mindestens 1 Pixel breit) ");
		  randProblem.add("dessen Pixel nicht normal berechnet werden koennen, weil ein Teil");
		  randProblem.add("der Maske ausserhlab des Eingangsbildes liegt. Es gibt verschiedene Methoden");
		  randProblem.add("zur Behandlung von Randproblemen. Einige davon sind: ");
		  ib1.setText(randProblem);
		  ib1.hide();
		  // second information box
		  InfoBox ib2 = new InfoBox(lang, new Offset(550, 0 , node, "intro"), 4, "Zero Padding");
		  List<String> zeroPadding = new ArrayList<String>();
		  zeroPadding.add("Hier wird Eingangsbild um 0 erweitert.");
		  ib2.setText(zeroPadding);
		  ib2.hide();
		  // third information box
		  InfoBox ib3 = new InfoBox(lang, new Offset(550, 0 , node, "intro"), 4, "Replication");
		  List<String> replication = new ArrayList<String>();
		  replication.add("Hier wird jedes off-Image Pixel durch den  Wert des naechsten Pixels ersetzt");
		  ib3.setText(replication);
		  ib3.hide();
		  // fourth information box
		  InfoBox ib4 = new InfoBox(lang, new Offset(550, 0 , node, "intro"), 4, "Wraparound");
		  List<String> wraparound = new ArrayList<String>();
		  wraparound.add("Wenn wir  von dem rechten Rand des Bildes gehen, erreichen wir den linken Rand. ");
		  wraparound.add("aehnlich, wenn wir den unteren Rand des Bildes verlassen,");
		  wraparound.add("kommen wir wieder an den oberen");
		  ib4.setText(wraparound);
		  ib4.hide();
          // fifth information box
		  InfoBox ib5 = new InfoBox(lang, new Offset(550, 0 , node, "intro"), 4, "No filter at the edge");
		  List<String> noFilter = new ArrayList<String>();
		  noFilter.add("Es wird kein Filter am Rand des Bildes angewendet");
		  ib5.setText(noFilter);
		  ib5.hide();
		  
		  InfoBox[] infoboxes = new InfoBox[]{ib1, ib2, ib3, ib4, ib5};

		  return infoboxes;
	  }
	  /**
	   * 
	   * @param array
	   */
	  private static void setCoordinates(IntMatrix array, int[][]src){
		  int row = array.getNrRows();
		  int col = array.getNrCols();
		  for(int i = 0; i < row; i++){
			  for(int j = 0; j < col; j++){
				  if(i == 0 && j !=0){
					  array.put(i, j, j-1, defaultDuration, defaultDuration);
					  array.setGridBorderColor(i, j, Color.WHITE, defaultDuration, defaultDuration);
				  }
				  if(j== 0 && i != 0){
					  array.put(i, j, i-1, defaultDuration, defaultDuration);
					  array.setGridBorderColor(i, j, Color.WHITE, defaultDuration, defaultDuration);
				  } 
				  if(i != 0 && j !=0){
					  
					  array.put(i, j, src[i-1][j-1], defaultDuration, defaultDuration);
					  
				  }
			  }
		  }
		  array.highlightElem(0, 0, defaultDuration, defaultDuration);
		  array.setGridBorderColor(0, 0, Color.WHITE, defaultDuration, defaultDuration);
	  }
	  
	  /**
		 * copies the source picture's border into destination picture
		 * @param src - source picture, dst -destination picture
		 */
		private static void copyBorder(IntMatrix src, IntMatrix dst){

			int row = src.getNrRows();
			int col = src.getNrCols();
			
			for(int i = 1; i < row; i++){
				for(int j = 1; j< col; j++){
					if(i == 1 || i == row-1){
						dst.put(i, j, src.getElement(i, j), defaultDuration, defaultDuration);
						dst.highlightCell(i, j, defaultDuration, defaultDuration);
						src.highlightCell(i, j, defaultDuration, defaultDuration);
					} else{
						dst.put(i, 1, src.getElement(i, 1), defaultDuration, defaultDuration);
						dst.put(i, col-1, src.getElement(i, col-1), defaultDuration, defaultDuration);
						dst.highlightCell(i, 1, defaultDuration, defaultDuration);
	                    dst.highlightCell(i, col-1, defaultDuration, defaultDuration);
	                    src.highlightCell(i, 1, defaultDuration, defaultDuration);
	                    src.highlightCell(i, col-1, defaultDuration, defaultDuration);
						break;
						
					}
				}
			}
		}
		/**
		 * 
		 * @param src
		 * @return
		 */
		public static int[][] zeroPadding(int[][] src){
			int row = src.length;
			int col = src[0].length;
			int[][] newSrc = new int[row+2][col+2];
			
			for(int i = 0; i < row+2; i++){
				for(int j = 0; j < col+2; j++){
					if(i == 0 || j == 0) newSrc[i][j] = 0;
					  else if(i == row+1 || j == col +1) newSrc[i][j] = 0;
					     else newSrc[i][j] = src[i-1][j-1];
					
				}
			}
			
			return newSrc;
		}
  /**
   * 	
   * @param src
   * @return
   */
  public static int[][] replication (int[][] src){
	  int row = src.length;
	  int col = src[0].length;
	  int[][] newSrc = new int[row+2][col+2];
	
	  for(int i = 1; i < row +1; i++){
		  for(int j = 1; j < col+1; j++){
			 newSrc[i][j] = src[i-1][j-1]; 
		  }
	  }
	  
	  for(int i = 0; i < row+2; i++){
		  for(int j = 0; j < col + 2; j++ ){
			 if(i == 0 && j !=0 || i == 0 && j != col+1){
				 newSrc[i][j] = newSrc[i+1][j];
			 }
			 
			 if(i == row+1 && j !=0 || i == row+1 && j != col+1){
				 newSrc[i][j] = newSrc[i-1][j];
			 }
			 if(j == 0 && i !=0 || j == 0 && i != row+1){
				 newSrc[i][j] = newSrc[i][j+1];
			 }
			 if(j == col+1 && i !=0 || j == col+1 && i != row+1){
				 newSrc[i][j] = newSrc[i][j-1];
			 }  
		  }
		  newSrc[0][0] = newSrc[1][1];
		  newSrc[row+1][0] = newSrc[row][0];
		  newSrc[0][col+1] = newSrc[0][col];
		  newSrc[row+1][col+1] = newSrc[row][col+1];
	  }
	  return newSrc;
  }
  /**
   * 
   * @param src
   * @return
   */
  public static int[][] wraparound(int[][] src){
	  int row = src.length;
	  int col = src[0].length;
	  int[][] newSrc = new int[row+2][col+2];
	  
	  for(int i = 1; i < row +1; i++){
		  for(int j = 1; j < col+1; j++){
			 newSrc[i][j] = src[i-1][j-1]; 
		  }
	  }
	  for(int i = 0; i < row+2; i++){
		  for(int j = 0; j < col + 2; j++ ){
			 if(i == 0 && j !=0 || i == 0 && j != col+1){
				 newSrc[i][j] = newSrc[row][j];
			 }
			 
			 if(i == row+1 && j !=0 || i == row+1 && j != col+1){
				 newSrc[i][j] = newSrc[1][j];
			 }
			 if(j == 0 && i !=0 || j == 0 && i != row+1){
				 newSrc[i][j] = newSrc[i][col];
			 }
			 if(j == col+1 && i !=0 || j == col+1 && i != row+1){
				 newSrc[i][j] = newSrc[i][1];
			 }  
		  }
		  newSrc[0][0] = newSrc[row][col];
		  newSrc[row+1][0] = newSrc[1][col];
		  newSrc[0][col+1] = newSrc[row][1];
		  newSrc[row+1][col+1] = newSrc[1][1];
	  }
	  return newSrc;
  }
  	
}