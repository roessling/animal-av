package generators.misc.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class DTWCode {
	private Language lang;
	private ArrayProperties arrayProps;
	private SourceCodeProperties codeProps;
	private MatrixProperties matrixProps;
	private TextProperties trueProps, falseProps;
	private int[][] matrix;
	private boolean loopCounter;
	
	class pair {
		int x;
		int y;
		pair(int xx, int yy) {
			x = xx;
			y = yy;
		}
	} // end pair
	
	private int min2_n(double x, double y) { return x < y ? 1 : 2; }
	private int min_n(double x, double y, double z) { return Math.min(x,y) < z ? min2_n(x,y) : 3; }
	
	/*
	 Dynamic time warping (DTW) is an algorithm for measuring similarity between 
	 two sequences which may vary in time or speed. 
	 In general, DTW is a method that allows a computer to find an optimal match 
	 between two given sequences (e.g. time series) with certain restrictions. 
	 The sequences are "warped" non-linearly in the time dimension to determine 
	 a measure of their similarity independent of certain non-linear variations 
	 in the time dimension. 
	 */
	
	// description text
	private static final String desc0 = "Dynamic time warping (DTW) is an algorithm for measuring similarity between ";
	private static final String desc1 = "two sequences which may vary in time or speed. "; 
	private static final String desc2 = "In general, DTW is a method that allows a computer to find an optimal match "; 
	private static final String desc3 = "between two given sequences (e.g. time series) with certain restrictions. "; 
	private static final String desc4 = "The sequences are 'warped' non-linearly in the time dimension to determine ";
	private static final String desc5 = "a measure of their similarity independent of certain non-linear variations";
	private static final String desc6 = "in the time dimension. ";	
	
	/*
	 DTW has been applied to video, audio, and graphics — indeed, 
	 any data which can be turned into a linear representation can be analyzed 
	 with DTW. A well known application has been automatic speech recognition, 
	 to cope with different speaking speeds. 
	 DTW is an algorithm particularly suited to matching sequences with missing 
	 information, provided there are long enough segments for matching to occur.
	 The extension of the problem for two-dimensional "series" like images 
	 (planar warping) is NP-complete, while the problem for one-dimensional signals 
	 like time series can be solved in polynomial time. 
	 */
	
	// end text
	private static final String fin0 = "DTW has been applied to video, audio, and graphics — indeed,";
	private static final String fin1 = "any data which can be turned into a linear representation can be analyzed "; 
	private static final String fin2 = "with DTW. A well known application has been automatic speech recognition, "; 
	private static final String fin3 = "to cope with different speaking speeds. "; 
	private static final String fin4 = "DTW is an algorithm particularly suited to matching sequences with missing ";
	private static final String fin5 = "information, provided there are long enough segments for matching to occur.";
	private static final String fin6 = "The extension of the problem for two-dimensional 'series' like images ";
	private static final String fin7 = "(planar warping) is NP-complete, while the problem for one-dimensional signals ";
	private static final String fin8 = "like time series can be solved in polynomial time. "; 

	public DTWCode(Language l, SourceCodeProperties codeP, ArrayProperties arrayP, MatrixProperties matrixP, Boolean loopC) {

		loopCounter = (boolean)loopC;
		lang = l;
		lang.setStepMode(true);
				
		// Properties
		// ARRAY
		arrayProps = arrayP;		
		//arrayProps = new ArrayProperties();
		//arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		//arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		//arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
		//arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 18));
		
		// SOURCECODE
		codeProps = codeP;		
		//codeProps = new SourceCodeProperties();
		codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		//codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		//codeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.RED);	
		codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 13));
		
		// MATRIX
		matrixProps = matrixP;
		//matrixProps = new MatrixProperties();
		//matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		//matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.WHITE);
		//matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		matrixProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 18));
		matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		
		// true false texts
		trueProps = new TextProperties();
		falseProps = new TextProperties();
		trueProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		trueProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 14));
		falseProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		falseProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 14));
		
		// Startpage 
		
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24));
		headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		lang.newText(new Coordinates(20,30), "Dynamic Time Warp", "header", null, headerProps);
		
		RectProperties boxProps = new RectProperties();
		boxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		boxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		boxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5,-5,"header",AnimalScript.DIRECTION_NW), new Offset(5,5,"header",AnimalScript.DIRECTION_SE), "box", null, boxProps);
		
		TextProperties authorProps = new TextProperties();
		authorProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.ITALIC, 13));
		authorProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		lang.newText(new Offset(10,-10,"header",AnimalScript.DIRECTION_E),"by Nadine Trueschler & Daniel Tanneberg", "authors", null, authorProps);
		
		TextProperties descProps = new TextProperties();
		descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		Text des0 = lang.newText(new Coordinates(20,75), desc0, "desc0", null, descProps);
		Text des1 = lang.newText(new Offset(0,16,"desc0",AnimalScript.DIRECTION_W), desc1, "desc1", null, descProps);
		Text des2 = lang.newText(new Offset(0,16,"desc1",AnimalScript.DIRECTION_W), desc2, "desc2", null, descProps);
		Text des3 = lang.newText(new Offset(0,16,"desc2",AnimalScript.DIRECTION_W), desc3, "desc3", null, descProps);
		Text des4 = lang.newText(new Offset(0,16,"desc3",AnimalScript.DIRECTION_W), desc4, "desc4", null, descProps);
		Text des5 = lang.newText(new Offset(0,16,"desc4",AnimalScript.DIRECTION_W), desc5, "desc5", null, descProps);
		Text des6 = lang.newText(new Offset(0,16,"desc5",AnimalScript.DIRECTION_W), desc6, "desc6", null, descProps);
		
		LinkedList<Primitive> desc = new LinkedList<Primitive>();
		desc.add(des0);
		desc.add(des1);
		desc.add(des2);
		desc.add(des3);
		desc.add(des4);
		desc.add(des5);
		desc.add(des6);

		Group des = lang.newGroup(desc, "des");
			
		lang.nextStep("Start");
		des.hide();		
	} // end constructor
	
			
		
	public int dtw(int reference[], int signal[]) {
				
		// set up dtw-matrix 
		matrix = new int[signal.length+1][reference.length+1];

		int cost = 0;
		
		// signale reinschreiben
		for(int i = 0; i < reference.length; i++)
			matrix[0][i+1] = reference[i];
		for(int i = 0; i < signal.length; i++)
			matrix[i+1][0] = signal[i];
			
		IntMatrix IMatrix = lang.newIntMatrix(new Offset(20,50,"header",AnimalScript.DIRECTION_W), matrix, "IMatrix", null, matrixProps);
		
		// elemente WHITE highlighten zum verstekcen am anfang
		//IMatrix.highlightElem(0, 0, null, null);
		//for(int i = 1; i < signal.length; i++)
		//	IMatrix.highlightElemColumnRange(i, 1, reference.length-1, null, null);
		
		TextProperties vectors = new TextProperties();
		vectors.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.ITALIC, 16));
		vectors.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		Text Ref = lang.newText(new Offset(33,-20,"IMatrix",AnimalScript.DIRECTION_NW), "Reference", "Reference", null, vectors);

		vectors.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		Text Sig1 = lang.newText(new Offset(-20,33,"IMatrix",AnimalScript.DIRECTION_NW), "S", "S", null, vectors);
		Text Sig2 = lang.newText(new Offset(0,20,"S",AnimalScript.DIRECTION_NW), "i", "i", null, vectors);
		Text Sig3 = lang.newText(new Offset(0,20,"i",AnimalScript.DIRECTION_NW), "g", "g", null, vectors);
		Text Sig4 = lang.newText(new Offset(0,20,"g",AnimalScript.DIRECTION_NW), "n", "n", null, vectors);
		Text Sig5 = lang.newText(new Offset(0,20,"n",AnimalScript.DIRECTION_NW), "a", "a", null, vectors);
		Text Sig6 = lang.newText(new Offset(0,20,"a",AnimalScript.DIRECTION_NW), "l", "l", null, vectors);
		
		LinkedList<Primitive> Sig = new LinkedList<Primitive>();
		Sig.add(Sig1);
		Sig.add(Sig2);
		Sig.add(Sig3);
		Sig.add(Sig4);
		Sig.add(Sig5);
		Sig.add(Sig6);
		Group SigG = lang.newGroup(Sig, "Sig");
		
		
		/*
		public int dtw(int reference[], int signal[]) {
			matrix = new int[signal.length][reference.length];
			for(int i = 0; i < matrix.length; i++) 
				for(int j = 0; j < matrix[i].length; j++) 
					matrix[i][j] = Math.abs(signal[i]-reference[j]);
			for(int i = 0; i < matrix.length; i++) {
				for(int j = 0; j < matrix[i].length; j++) {
					if(i == 0 && j == 0) 
						matrix[i][j] = Math.abs(signal[i]-reference[j]);
					else if(i == 0) 
						matrix[i][j] = Math.abs(signal[i]-reference[j]) + matrix[i][j-1];
					else if(j == 0) 
						matrix[i][j] = Math.abs(signal[i]-reference[j]) + matrix[i-1][j];
					else 
						matrix[i][j] = Math.abs(signal[i]-reference[j]) + Math.min(Math.min(matrix[i-1][j],matrix[i][j-1]),matrix[i-1][j-1]);
				}
			}
			return matrix[signal.length-1][reference.length-1];
		}
		*/
		
		SourceCode sc = lang.newSourceCode(new Offset(-20,50,"IMatrix",AnimalScript.DIRECTION_SW), "sc", null, codeProps);
		sc.addCodeLine("public int dtw(int reference[], int signal[]) {", "0", 0, null);
		sc.addCodeLine("matrix = new int[signal.length][reference.length];", "1", 1, null);
		sc.addCodeLine("for(int i = 0; i < matrix.length; i++) ", "2", 1, null);
		sc.addCodeLine("for(int j = 0; j < matrix[i].length; j++) ", "3", 2, null);
		sc.addCodeLine("matrix[i][j] = Math.abs(signal[i]-reference[j]);", "4", 3, null);
		sc.addCodeLine("for(int i = 0; i < matrix.length; i++) {", "5", 1, null);
		sc.addCodeLine("for(int j = 0; j < matrix[i].length; j++) {", "6", 2, null);
		sc.addCodeLine("if(i == 0 && j == 0) ", "7", 3, null);
		sc.addCodeLine("matrix[i][j] = Math.abs(signal[i]-reference[j]);", "8", 4, null);
		sc.addCodeLine("else if(i == 0) ", "9", 3, null);
		sc.addCodeLine("matrix[i][j] = Math.abs(signal[i]-reference[j]) + matrix[i][j-1];", "10", 4, null);
		sc.addCodeLine("else if(j == 0) ", "11", 3, null);
		sc.addCodeLine("matrix[i][j] = Math.abs(signal[i]-reference[j]) + matrix[i-1][j];", "12", 4, null);
		sc.addCodeLine("else ", "13", 3, null);
		sc.addCodeLine("matrix[i][j] = Math.abs(signal[i]-reference[j]) + Math.min(Math.min(matrix[i-1][j],matrix[i][j-1]),matrix[i-1][j-1]);", "14", 4, null);
		sc.addCodeLine("}", "15", 2, null);
		sc.addCodeLine("}", "16", 1, null);
		sc.addCodeLine("return matrix[signal.length-1][reference.length-1];", "17", 1, null);
		sc.addCodeLine("}", "18", 0, null);
		
		Text true1 = lang.newText(new Offset(-4,118,"sc",AnimalScript.DIRECTION_NW), "TRUE", "true1", null, trueProps);
		Text true2 = lang.newText(new Offset(0,34,"true1",AnimalScript.DIRECTION_NW), "TRUE", "true2", null, trueProps);
		Text true3 = lang.newText(new Offset(0,34,"true2",AnimalScript.DIRECTION_NW), "TRUE", "true3", null, trueProps);
		Text true4 = lang.newText(new Offset(0,34,"true3",AnimalScript.DIRECTION_NW), "TRUE", "true4", null, trueProps);
		Text false1 = lang.newText(new Offset(-4,118,"sc",AnimalScript.DIRECTION_NW), "FALSE", "false1", null, falseProps);
		Text false2 = lang.newText(new Offset(0,34,"false1",AnimalScript.DIRECTION_NW), "FALSE", "false2", null, falseProps);
		Text false3 = lang.newText(new Offset(0,34,"false2",AnimalScript.DIRECTION_NW), "FALSE", "false3", null, falseProps);
		Text false4 = lang.newText(new Offset(0,34,"false3",AnimalScript.DIRECTION_NW), "FALSE", "false4", null, falseProps);
		true1.hide();true2.hide();true3.hide();true4.hide();
		false1.hide();false2.hide();false3.hide();false4.hide();
		
		lang.nextStep("AlgoStart");
		sc.highlight("0", true);
		sc.highlight(1);
		// versteckte elemente anzeigen, "matrix anlegen"
		//IMatrix.unhighlightElem(0, 0, null, null);
		//for(int i = 1; i < signal.length; i++)
		//	IMatrix.unhighlightElemColumnRange(i, 1, reference.length-1, null, null);
		
		int aniCounter = 4;
		int aniCounter1 = 4;
		if(loopCounter) {
			if(signal.length < aniCounter) 
				aniCounter = reference.length;
			if(reference.length < aniCounter1)
				aniCounter1 = signal.length;
		}
		else {
			aniCounter = reference.length+1;
			aniCounter1 = signal.length+1;	
		}
			
		
		lang.nextStep("calculate Distance Matrix");
		sc.unhighlight(1);
		// distance-matrix
		for(int i = 1; i < matrix.length; i++) {
			for(int j = 1; j < matrix[i].length; j++) {
				matrix[i][j] = Math.abs(signal[i-1]-reference[j-1]);
				
				if(j < aniCounter && i < aniCounter1) {
					// werte markieren 
					IMatrix.highlightCell(0, j, null, null);
					IMatrix.highlightCell(i, 0, null, null);
					sc.highlight(2);
					sc.highlight(3);
					sc.unhighlight(4);
					lang.nextStep();
				}
				IMatrix.put(i, j, matrix[i][j], null, null);
				
				if(j < aniCounter && i < aniCounter1) {
					// ergebnis markieren
					IMatrix.highlightCell(i, j, null, null);
					sc.highlight("2", true);
					sc.highlight("3", true);
					sc.highlight(4);
					lang.nextStep();
					IMatrix.unhighlightCell(0, j, null, null);
					IMatrix.unhighlightCell(i, 0, null, null);
					IMatrix.unhighlightCell(i, j, null, null);
					
				}
			}
		}
		
		
		// matrix output to console	
		/*
		int u = 0;
		for(int i = 0; i < matrix.length; i++) {
			int buffer[] = matrix[i];
			//System.out.print(signal[u] + "\t");
			for(int j = 0; j < buffer.length; j++)
				System.out.print(buffer[j] + "\t");
			System.out.println();
			u++;
		}
		System.out.println();
		*/
		
		aniCounter = 5;
		aniCounter1 = 5;
		if(loopCounter) {
			if(signal.length < aniCounter) 
				aniCounter = reference.length;
			if(reference.length < aniCounter1)
				aniCounter1 = signal.length;
		}
		else {
			aniCounter = reference.length+1;
			aniCounter1 = signal.length+1;	
		}

		lang.nextStep("calculate Accumulated Distance Matrix");
		sc.unhighlight(2);
		sc.unhighlight(3);
		sc.unhighlight(4);
		sc.highlight(5);
		sc.highlight(6);
		lang.nextStep();
		sc.highlight("5",true);
		sc.highlight("6",true);
		// accumulated distance-matrix
		for(int i = 1; i < matrix.length; i++) {
			for(int j = 1; j < matrix[i].length; j++) {
				if(i == 1 && j == 1) {
					cost = Math.abs(signal[i-1]-reference[j-1]);
					matrix[i][j] = cost;
					
					if(j < aniCounter && i < aniCounter1) {
						true1.show();
						false2.show();
						false3.show();
						false4.show();
						sc.highlight(7);
						// werte markieren 
						IMatrix.highlightCell(0, j, null, null);
						IMatrix.highlightCell(i, 0, null, null);
						lang.nextStep();
					}
					IMatrix.put(i, j, matrix[i][j], null, null);
					
					if(j < aniCounter && i < aniCounter1) {
						// ergebnis markieren
						IMatrix.highlightCell(i, j, null, null);
						sc.highlight("7",true);
						sc.highlight(8);
						lang.nextStep();
						IMatrix.unhighlightCell(0, j, null, null);
						IMatrix.unhighlightCell(i, 0, null, null);
						IMatrix.unhighlightCell(i, j, null, null);
						true1.hide();
						false2.hide();
						false3.hide();
						false4.hide();
						sc.unhighlight("7",true);
						sc.unhighlight(8);
					}
				}
				else if(i == 1) {
					cost = Math.abs(signal[i-1]-reference[j-1]);
					matrix[i][j] = cost + matrix[i][j-1];
					
					if(j < aniCounter && i < aniCounter1) {
						true2.show();
						false1.show();
						false3.show();
						false4.show();
						sc.highlight(9);
						// werte markieren 
						IMatrix.highlightCell(0, j, null, null);
						IMatrix.highlightCell(i, 0, null, null);
						IMatrix.highlightCell(i, j-1, null, null);
						lang.nextStep();
					}
					IMatrix.put(i, j, matrix[i][j], null, null);
					
					if(j < aniCounter && i < aniCounter1) {
						// ergebnis markieren
						IMatrix.highlightCell(i, j, null, null);
						sc.highlight("9",true);
						sc.highlight(10);
						lang.nextStep();
						IMatrix.unhighlightCell(0, j, null, null);
						IMatrix.unhighlightCell(i, 0, null, null);
						IMatrix.unhighlightCell(i, j, null, null);
						IMatrix.unhighlightCell(i, j-1, null, null);
						true2.hide();
						false1.hide();
						false3.hide();
						false4.hide();
						sc.unhighlight("9",true);
						sc.unhighlight(10);
					}
				}
				else if(j == 1) {
					cost = Math.abs(signal[i-1]-reference[j-1]);
					matrix[i][j] = cost + matrix[i-1][j];
					
					if(j < aniCounter && i < aniCounter1) {
						true3.show();
						false1.show();
						false2.show();
						false4.show();
						sc.highlight(11);
						// werte markieren 
						IMatrix.highlightCell(0, j, null, null);
						IMatrix.highlightCell(i, 0, null, null);
						IMatrix.highlightCell(i-1, j, null, null);
						lang.nextStep();
					}
					IMatrix.put(i, j, matrix[i][j], null, null);
					
					if(j < aniCounter && i < aniCounter1) {
						// ergebnis markieren
						IMatrix.highlightCell(i, j, null, null);
						sc.highlight("11",true);
						sc.highlight(12);
						lang.nextStep();
						IMatrix.unhighlightCell(0, j, null, null);
						IMatrix.unhighlightCell(i, 0, null, null);
						IMatrix.unhighlightCell(i, j, null, null);
						IMatrix.unhighlightCell(i-1, j, null, null);
						true3.hide();
						false1.hide();
						false2.hide();
						false4.hide();
						sc.unhighlight("11",true);
						sc.unhighlight(12);
					}
				}
				else {
					cost = Math.abs(signal[i-1]-reference[j-1]);
					matrix[i][j] = cost + Math.min(Math.min(matrix[i-1][j],
											  				matrix[i][j-1]),
											  				matrix[i-1][j-1]);
					if(j < aniCounter && i < aniCounter1) {
						true4.show();
						false1.show();
						false3.show();
						false2.show();
						sc.highlight(13);
						// werte markieren 
						IMatrix.highlightCell(0, j, null, null);
						IMatrix.highlightCell(i, 0, null, null);
						if(Math.min(Math.min(matrix[i-1][j],matrix[i][j-1]),matrix[i-1][j-1]) == matrix[i-1][j])
							IMatrix.highlightCell(i-1, j, null, null);
						else if(Math.min(Math.min(matrix[i-1][j],matrix[i][j-1]),matrix[i-1][j-1]) == matrix[i][j-1])
							IMatrix.highlightCell(i, j-1, null, null);
						else if(Math.min(Math.min(matrix[i-1][j],matrix[i][j-1]),matrix[i-1][j-1]) == matrix[i-1][j-1])
							IMatrix.highlightCell(i-1, j-1, null, null);
						lang.nextStep();
					}
					IMatrix.put(i, j, matrix[i][j], null, null);
					
					if(j < aniCounter && i < aniCounter1) {
						// ergebnis markieren
						IMatrix.highlightCell(i, j, null, null);
						sc.highlight("13",true);
						sc.highlight(14);
						lang.nextStep();
						IMatrix.unhighlightCell(0, j, null, null);
						IMatrix.unhighlightCell(i, 0, null, null);
						if(Math.min(Math.min(matrix[i-1][j],matrix[i][j-1]),matrix[i-1][j-1]) == matrix[i-1][j])
							IMatrix.highlightCell(i-1, j, null, null);
						else if(Math.min(Math.min(matrix[i-1][j],matrix[i][j-1]),matrix[i-1][j-1]) == matrix[i][j-1])
							IMatrix.highlightCell(i, j-1, null, null);
						else if(Math.min(Math.min(matrix[i-1][j],matrix[i][j-1]),matrix[i-1][j-1]) == matrix[i-1][j-1])
							IMatrix.highlightCell(i-1, j-1, null, null);
						true4.hide();
						false1.hide();
						false3.hide();
						false2.hide();
						sc.unhighlight("13",true);
						sc.unhighlight(14);
					}
				}
			}
		}
		lang.nextStep();
		sc.unhighlight(5);
		sc.unhighlight(6);
		for(int i = 0; i < signal.length; i++)
			IMatrix.unhighlightElemColumnRange(i, 0, reference.length-1, null, null);
		IMatrix.highlightCell(signal.length, reference.length, null, null);
		
		TextProperties distProps = new TextProperties();
		distProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		distProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.ITALIC, 16));
		
		Text Distance = lang.newText(new Offset(120+((reference.length-4)*16),2,"Reference",AnimalScript.DIRECTION_NE), "Distance: " + matrix[signal.length][reference.length] , "distance", null, distProps);
		
		lang.nextStep("AlgoEnd");
		
		
		
		// matrix output to console	
		/*		
		int u = 0;
		for(int i = 0; i < matrix.length; i++) {
			int buffer[] = matrix[i];
			//System.out.print(signal[u] + "\t");
			for(int j = 0; j < buffer.length; j++)
				System.out.print(buffer[j] + "\t");
			System.out.println();
			u++;
		}
		System.out.println();
		*/
		
		// mapping
		Vector<pair> path = new Vector<pair>();
		Vector<pair> path_buffer = new Vector<pair>();
		int m = signal.length;
		int n = reference.length;
		path_buffer.add(new pair(m,n));
		while(m >= 1 && n >= 1 && m+n > 2) {
			if(m == 1)
				n--;
			else if(n == 1)
				m--;
			else {
				int number = min_n(matrix[m-1][n], matrix[m][n-1], matrix[m-1][n-1]);
				if(number == 1)
					m--;
				else if(number == 2)
					n--;
				else if(number == 3) {
					m--;
					n--;
				}

			}
			path_buffer.add(new pair(m,n));
			
		}
		for(int i = path_buffer.size()-1; i >= 0 ; i--)
			path.add(path_buffer.elementAt(i));
		/*
		// path output to console
		for(int i = 0; i < path.size(); i++)
			System.out.println(path.elementAt(i).x + " - " + path.elementAt(i).y);
		*/
		
		vectors.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		vectors.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD,18));
		Text Mapping = lang.newText(new Offset(0,15,Distance,AnimalScript.DIRECTION_SW), "Mapping", "Mapping", null, vectors);
		
		IntArray IRef = lang.newIntArray(new Offset(0,20,Mapping,AnimalScript.DIRECTION_SW), reference, "IRef", null, arrayProps);	
		IntArray ISig = lang.newIntArray(new Offset(0,70,IRef,AnimalScript.DIRECTION_SW), signal, "ISig", null, arrayProps);	
				
		lang.nextStep("Mapping");
		Polyline line = null;
		LinkedList<Primitive> Lines = new LinkedList<Primitive>();
		aniCounter = 5;
		if(signal.length < 5 || reference.length < 5)
			aniCounter = Math.min(signal.length, reference.length);		
		
		for(int i = 0; i < path_buffer.size(); i++) {
			
			pair buffer = path_buffer.elementAt(i);

			Node[] vertices = {new Offset(16*buffer.y-8,0,IRef,AnimalScript.DIRECTION_SW),new Offset(16*buffer.x-8,0,ISig,AnimalScript.DIRECTION_NW)};
			line = lang.newPolyline(vertices, "Line"+i, null);
			Lines.add(line);

			ISig.highlightCell(buffer.x-1, null,null);
			IRef.highlightCell(buffer.y-1, null,null);
			IMatrix.highlightCell(0, buffer.y, null, null);
			IMatrix.highlightCell(buffer.x, 0, null, null);
			IMatrix.highlightCell(buffer.x,  buffer.y, null, null);
			lang.nextStep();
			ISig.unhighlightCell(buffer.x-1, null,null);
			IRef.unhighlightCell(buffer.y-1, null,null);
			IMatrix.unhighlightCell(0, buffer.y, null, null);
			IMatrix.unhighlightCell(buffer.x, 0, null, null);
		}
		
		
		lang.nextStep("Algorithm finished");
		sc.hide(); 
		IMatrix.hide();
		SigG.hide();
		Ref.hide();
		Mapping.hide();
		ISig.hide();
		IRef.hide();
		Distance.hide();
		Group LinesG = lang.newGroup(Lines, "Lines");
		LinesG.hide();
		
				
		TextProperties finProps = new TextProperties();
		finProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		lang.newText(new Coordinates(20,75), fin0, "fin0", null, finProps);
		 lang.newText(new Offset(0,16,"fin0",AnimalScript.DIRECTION_W), fin1, "fin1", null, finProps);
		 lang.newText(new Offset(0,16,"fin1",AnimalScript.DIRECTION_W), fin2, "fin2", null, finProps);
		 lang.newText(new Offset(0,16,"fin2",AnimalScript.DIRECTION_W), fin3, "fin3", null, finProps);
		 lang.newText(new Offset(0,16,"fin3",AnimalScript.DIRECTION_W), fin4, "fin4", null, finProps);
		 lang.newText(new Offset(0,16,"fin4",AnimalScript.DIRECTION_W), fin5, "fin5", null, finProps);
		 lang.newText(new Offset(0,16,"fin5",AnimalScript.DIRECTION_W), fin6, "fin6", null, finProps);
		 lang.newText(new Offset(0,16,"fin6",AnimalScript.DIRECTION_W), fin7, "fin7", null, finProps);
		 lang.newText(new Offset(0,16,"fin7",AnimalScript.DIRECTION_W), fin8, "fin8", null, finProps);			
		
		return matrix[signal.length][reference.length];
	}
	
	/*
	public static void main(String[] args) {
		
			Language l = new AnimalScript("Dynamic Time Warp", "Nadine Trüschler,Daniel Tanneberg", 640, 480);			
			int[] a = {3,3,1,5,7,8};
			int[] ref = {5,4,2,1,5,6,6,9};

			DTWCode dtw = new DTWCode(l);
			dtw.dtw(ref,a);
			dtw.lang.nextStep();
			//System.out.println(l);
			l.writeFile("DTW_API.asu");
		}
		*/

}
