/*
 * KDTree.java
 * David Steiner, Jens Kr�ger, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;
import java.util.Vector;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import translator.Translator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.MatrixProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.tree.KDTreeNode;
import generators.tree.IntMatrixWA;
import generators.tree.SeparatorWindow;

public class KDTree implements Generator {
	
	private Language lang;
	
	// user input
    	private int[][] pointsArray;
    	
    // global primitives
        private Polyline xAxisMarker;
        private Polyline yAxisMarker;
        private Circle[] left_points;
        private Circle[] right_points;
        private SourceCode sourceCode;

    // global properties user is allowed to change 
	    private RectProperties gmLeftAreaProperties;
	    private CircleProperties gmLeftPointsProperties;
	    private SourceCodeProperties sourceCodeProperties;
	    private PolylineProperties currentAxisProperties;
	    private CircleProperties nodeHighlightProperties;
	    private CircleProperties gmRightPointsProperties;
	    private MatrixProperties pointsArrayProperties;
	    private CircleProperties nodeProperties;
	    private SourceCodeProperties introProperties;
	    private CircleProperties gmAllPointsProperties;
	    private PolylineProperties gmYLineProperties;
	    private PolylineProperties gmXLineProperties;
	    private TextProperties nodeTextProperties;
	    private RectProperties gmRightAreaProperties;
	    
	// global properties user is not allowed to change
	    private TextProperties gmTextProperties;
	    private PolylineProperties gmBorderProperties;
	    private TextProperties conclusionProperties;
	   	    
	// global variables
	    private int[][] all_points;
	    private Translator translator;
	    private Locale location;
	    
    // STATICS  
	    // AXIS
		    public static final int AXIS_X = 0;
			public static final int AXIS_Y = 1; 
	    
	    // pointsArray
		    public static final int COL_LENGTH = 60;
			public static final int SEP_LENGTH = 20;
			public static final int P_X_AXIS = 50;
			public static final int P_Y_AXIS = 50;
			public static final int ARRAY_HEIGHT = 25;
		
		//FIXME needed for?
		    public static final boolean LOWER = false;
			public static final boolean HIGHER = true; 
	    
		//GM WINDOW ATTRIBUTES
			//offsets for the gm window position 
				public static final int GM_X0 = 100;
				public static final int GM_Y0 = 150;
				
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
					public static final int INFO_X0 = 100;
					public static final int INFO_Y0 = 470;
		
		// tree
			public static final int TREE_AREA_WIDTH = 1000;
			public static int TREE_AREA_X = GM_X0 + GM_XL + 50;
			public static int TREE_AREA_Y = GM_Y0;
			public static int radius = 100;
			
	// vectors for primitives collection (is needed for conclusion)
		private Vector<Primitive> arrayPrimitives;
		private Vector<Primitive> diagramPrimitives;
		private Vector<Primitive> treePrimitives;
		private Vector<KDTreeNode<int[], Integer>> treeNodes;
		private Vector<KDTreeNode<int[], Integer>> treeLeafs;
		
	// primitive counter
		int mCounter = 0; //matrix counter
		int rCounter = 0; //rect counter
		int lCounter = 0; //line counter
		int nCounter = 0; //node counter
	 
	/**
	 * 
	 * @param	location language/location of generator
	 * 			current support
	 * 			Locale.GERMANY
	 * 			Locale.US
	 */
	public KDTree(Locale location) {
		
		this.location = location;
		
		// initialize translator
			String textResource = "generators/tree/KDTree";
			translator = new Translator(textResource, this.location);
	}

    /**
     * initialize the generator in this way, it is possible to call {@link generate} more than one time
     */
    @Override
	public void init(){
    	
    	// initialize language object
        	lang = new AnimalScript("KD-Tree [DE]", "David Steiner, Jens Krüger", 800, 600);
        	lang.setStepMode(true);
        
        // initialize properties user is not allowed to change
    	    // currentAxisProperties
	        	currentAxisProperties = new PolylineProperties();
	            currentAxisProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        	
            // gmTextProperties = axis number symbols 
	        	gmTextProperties = new TextProperties(); 
	            gmTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
	            gmTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,10));
	    	    
    	    // gmBorderProperties = border of the GM window
	            gmBorderProperties = new PolylineProperties(); 
	            gmBorderProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
	            gmBorderProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        	
        	// conclusionProperties
	    	    Font conclusionFont = new Font (Font.SANS_SERIF, Font.BOLD, 16);
	    	    conclusionProperties = new TextProperties();
	    	    conclusionProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, conclusionFont);
            
        // initialize vectors for primitives collection
            arrayPrimitives = new Vector<Primitive>();
            diagramPrimitives = new Vector<Primitive>();
            treePrimitives = new Vector<Primitive>();
            treeNodes = new Vector<KDTreeNode<int[], Integer>>();
            treeLeafs = new Vector<KDTreeNode<int[], Integer>>();       
    }
    
    
    /**
     * displays the introduction
     */
	private void introduction() {
		
		// display headline
			TextProperties headlineProperties = new TextProperties();
			headlineProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
			headlineProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
			lang.newText(new Coordinates(P_X_AXIS, 20), translator.translateMessage("TITLE"), "headline", null, headlineProperties);
			
		// display introduction			
			SourceCode intro = lang.newSourceCode(new Coordinates(P_X_AXIS, 40), "intro", null, introProperties);
			for(int i = 0; i < Integer.valueOf(translator.translateMessage("INTRODUCTION_LENGTH")); i++){
				intro.addCodeLine(translator.translateMessage("INTRODUCTION_" + i), "intro", 0, null);
			}
			
		// hide introduction
			lang.nextStep(translator.translateMessage("LABEL_INTRO"));
			intro.hide();
	}
	
	/**
	 * displays the conclusion
	 */
	private void conclusion() {
		
		lang.nextStep();
		
		// hide source code
			sourceCode.hide();
		
		//algorithm has terminated
			Text conclusionAlgoText = lang.newText(new Coordinates(P_X_AXIS, 580), translator.translateMessage("CONCLUSION_0"), "algoTermin", null, conclusionProperties);
			lang.nextStep(translator.translateMessage("LABEL_CONCLUSION"));
			conclusionAlgoText.hide();
		
		// display array information
			hide(diagramPrimitives);
			hide(treePrimitives);
			Text conclusionArrayText = lang.newText(new Coordinates(P_X_AXIS, 200), translator.translateMessage("CONCLUSION_1"), "algoTermin", null, conclusionProperties);
			lang.nextStep();
			conclusionArrayText.hide();
			
		// display diagram information
			hide(arrayPrimitives);
			show(diagramPrimitives);
			Text conclusionDiagramText = lang.newText(new Coordinates(P_X_AXIS + GM_XL + 200, GM_Y0), translator.translateMessage("CONCLUSION_2"), "algoTermin", null, conclusionProperties);
			lang.nextStep();
			conclusionDiagramText.hide();

		// display tree nodes information
			hide(diagramPrimitives);
			show(treePrimitives);
			Text conclusionTreeNodesText = lang.newText(new Coordinates(P_X_AXIS + 100, P_Y_AXIS * 2),translator.translateMessage("CONCLUSION_3"), "algoTermin", null, conclusionProperties);
			for (KDTreeNode<int[], Integer> node : treeNodes) node.highlightNode(nodeHighlightProperties);
			lang.nextStep();
			for (KDTreeNode<int[], Integer> node : treeNodes) node.unhighlightNode();
			conclusionTreeNodesText.hide();
			
		// display tree leafs information
			Text conclusionTreeLeafsText = lang.newText(new Coordinates(P_X_AXIS + 100, P_Y_AXIS * 2), translator.translateMessage("CONCLUSION_4"), "algoTermin", null, conclusionProperties);
			for (KDTreeNode<int[], Integer> node : treeLeafs) node.highlightNode(nodeHighlightProperties);
			lang.nextStep();
			for (KDTreeNode<int[], Integer> node : treeLeafs) node.unhighlightNode();
			conclusionTreeLeafsText.hide();
						
		// show all
			show(arrayPrimitives);
			show(diagramPrimitives);		
			
		// THE END :)	
	}
	
	/**
	 * algorithm of KDTree 
	 * variant for 2D-tree 
	 * @param points array of points which will be processed by the algorithm
	 * @return root of the KDTree
	 */
	public KDTreeNode<int[], Integer> kd_tree(int[][] points){
		
		// traverse points
			int[][] traversedPoints = traverse(points);
			
		// check for points out of range
			int[][] inRangePoints = checkForOutOfRange(traversedPoints);
					
		// check for doubles
			int[][] s_points = checkForDoubles(inRangePoints);
			all_points = s_points;
	
	    // initialize the intMatrix for the points
			IntMatrixWA pointsVisual = new IntMatrixWA(traverse(s_points));
			pointsVisual = pointsVisual.drawMatrix(lang, new Coordinates(P_X_AXIS, P_Y_AXIS), pointsArrayProperties, mCounter);
			mCounter++;
			
	    // initialize the current axis markers
	        Node[] xAxisMarkerNodes = {new Coordinates(P_X_AXIS - 60, P_Y_AXIS + ARRAY_HEIGHT / 2), new Coordinates(P_X_AXIS - 10, P_Y_AXIS + ARRAY_HEIGHT / 2)};
	        xAxisMarker = lang.newPolyline(xAxisMarkerNodes, "xAxisMarker", null, currentAxisProperties);
	        xAxisMarker.hide();
	        Node[] yAxisMarkerNodes = {new Coordinates(P_X_AXIS - 60, P_Y_AXIS + ARRAY_HEIGHT + ARRAY_HEIGHT / 2), new Coordinates(P_X_AXIS - 10, P_Y_AXIS + ARRAY_HEIGHT + ARRAY_HEIGHT / 2)};
	        yAxisMarker = lang.newPolyline(yAxisMarkerNodes, "yAxisMarker", null, currentAxisProperties);
	        yAxisMarker.hide();
		
		// display source code
			displaySourceCode();
		
        // draw GM window
	        draw_GM_window(s_points);
	      
	   // initialize root of the tree, position of the tree and radius of one node
	        double depth =	Math.ceil(Math.log(s_points.length)/Math.log(2));
	        double maxLeaf = Math.pow(2, depth);
	        double d = TREE_AREA_WIDTH / (1.5 * maxLeaf - 0.5);
	        radius = (int) Math.floor(d/2);
	        if (radius < 20) radius = 20;
	        if (radius > 30) radius = 30;
	        KDTreeNode<int[], Integer> root = new KDTreeNode<int[], Integer>(new Coordinates(TREE_AREA_X, TREE_AREA_Y), TREE_AREA_WIDTH, 0);
		
		// initialize separator window
	        SeparatorWindow swindow = new SeparatorWindow(0, 0, X_COORD_N, Y_COORD_N); //save borders for separator lines
	        
       lang.nextStep(translator.translateMessage("LABEL_INIT"));
	
       return rek_kd_tree(AXIS_X, root, pointsVisual, swindow);
	}
	
	/**
	 * recursive method for kd-tree
	 * @param current_axis specified axis: 	AXIS_X will sort according to the first axis
	 * 										AXIS_Y will sort according to the second axis 
	 * @param node current tree node for processing
	 * @param pointsArray array with the points
	 * @return the current processed node
	 */
	private KDTreeNode<int[], Integer> rek_kd_tree (int current_axis, KDTreeNode<int[], Integer> node, IntMatrixWA pointsArray, SeparatorWindow swindow){
		
		int[][] pointsValues = traverse(pointsArray.getMatrix());
				
		//display if-statement
			sourceCode.highlight(0);
			pointsArray.highlightCellSpace(0, pointsValues.length-1, 0, pointsValues[0].length-1);
			lang.nextStep();
			sourceCode.unhighlight(0);
		
		
			
		// sort according to the current Bounding Box
			if(pointsValues.length <= 1){
				
				//sourceCode
					sourceCode.highlight(0, 0, true, null, null);
					sourceCode.highlight(1);
				
				//set value in leaf
					int[] leaf = pointsValues[0];	
					node.setAsLeaf(leaf);
				
				//draw the leaf
					node.drawNode(lang, radius, nodeProperties, nodeTextProperties, nCounter);
					node.highlightNode(nodeHighlightProperties);
					nCounter++;
							
				lang.nextStep();
				
				//unhighlight
					pointsArray.unhighlightCellSpace(0, pointsValues.length-1, 0, pointsValues[0].length-1);
					node.unhighlightNode();
					sourceCode.unhighlight(1);
					sourceCode.unhighlight(0);
					
				//add primitives to vectors
					arrayPrimitives.addAll(pointsArray.getPrimitives());
					treePrimitives.addAll(node.getPrimitives());
					treeLeafs.add(node);
				
				return new KDTreeNode<int[], Integer>(leaf);
			}
			
		
		// ELSE (more than one point in array)
			sourceCode.highlight(2, 0, true, null, null);
		
			
		// sort matrix according to axis
			int[][] p_sorted = mergeSortAccordingToAxis(current_axis, pointsValues);

			
		// draw the sorted array
			sourceCode.highlight(3);
			if (current_axis == AXIS_X)
				xAxisMarker.show();
			else 
				yAxisMarker.show();
			pointsArray.hide();
			IntMatrixWA pointsArraySorted = new IntMatrixWA(traverse(p_sorted));
			pointsArraySorted.drawMatrix(lang, pointsArray.getUpperLeft(), pointsArrayProperties, mCounter);
			pointsArraySorted.highlightCellSpace(0, p_sorted.length-1, 0, p_sorted[0].length-1);
			mCounter++;
			lang.nextStep();
			if (current_axis == AXIS_X)
				xAxisMarker.hide();
			else
				yAxisMarker.hide();
			sourceCode.unhighlight(3);

			
		//split the array at the median
			int p_size = p_sorted.length;
			int median = p_size/2;
		
			int[][] p_left = new int[median][2];
			int[][] p_right = new int[p_size-median][2];
			
			System.arraycopy(p_sorted, 0, p_left, 0, median);
			System.arraycopy(p_sorted, median, p_right, 0, (p_size - median));
		
			
		//draw the two new separated arrays
			sourceCode.highlight(4);
			pointsArraySorted.hide();
			IntMatrixWA p_leftArray = new IntMatrixWA(traverse(p_left));
			p_leftArray = p_leftArray.drawMatrix(lang, new Coordinates(pointsArray.getUpperLeft().getX(), P_Y_AXIS), pointsArrayProperties, mCounter);
			p_leftArray.highlightCellSpace(0, p_leftArray.getMatrix()[0].length-1, 0, p_leftArray.getMatrix().length-1);
			mCounter++;
			int rightMatrixLeftCorner = pointsArray.getUpperLeft().getX() + median*COL_LENGTH + median*SEP_LENGTH;
			IntMatrixWA p_rightArray = new IntMatrixWA(traverse(p_right));
			p_rightArray = p_rightArray.drawMatrix(lang, new Coordinates(rightMatrixLeftCorner, P_Y_AXIS), pointsArrayProperties, mCounter);
			p_rightArray.highlightCellSpace(0, p_rightArray.getMatrix()[0].length-1, 0, p_rightArray.getMatrix().length-1);
			mCounter++;
			lang.nextStep();
			sourceCode.unhighlight(4);
	
			
		//set node
			int sep_left = (p_left[p_left.length - 1][current_axis]);
			int sep_right = (p_right[0][current_axis]);
			float separator = ((float)sep_left + (float)sep_right ) / 2;
			node.setAsSeparator(separator);
			node.setSeparatorAxis(current_axis);
			
			
		//draw node
			sourceCode.highlight(5);
			
			node.drawNode(lang, radius, nodeProperties, nodeTextProperties, nCounter);
			nCounter++;
			node.highlightNode(nodeHighlightProperties);
			
			lang.nextStep();
			sourceCode.unhighlight(5);
		
			
		//set new separator windows
			SeparatorWindow leftwindow;
			SeparatorWindow rightwindow;
			int new_axis = (current_axis + 1) % 2;
			if(current_axis == AXIS_X){
				leftwindow = new SeparatorWindow(swindow.get_lx(), swindow.get_ly(), separator, swindow.get_hy()); //save borders for separator lines
				rightwindow = new SeparatorWindow(separator, swindow.get_ly(), swindow.get_hx(), swindow.get_hy()); //save borders for separator lines
			} else {
				leftwindow = new SeparatorWindow(swindow.get_lx(), swindow.get_ly(), swindow.get_hx(), separator); //save borders for separator lines
				rightwindow = new SeparatorWindow(swindow.get_lx(), separator, swindow.get_hx(), swindow.get_hy()); //save borders for separator lines
			}
			
			
		//init point subsets
			Rect left_gm_window = get_GM_current_window(leftwindow, gmLeftAreaProperties); 
			Rect right_gm_window = get_GM_current_window(rightwindow, gmRightAreaProperties); 
			
			//loop: collect the needed circle primitives by finding their index in the points-array
			Vector<Circle> current_left_points = new Vector<Circle>(0);
			Vector<Circle> current_right_points = new Vector<Circle>(0);
			for(int c = 0; c < all_points.length; c++){
				boolean skip_loop = false;
				for(int l_i = 0; l_i < p_left.length; l_i++)
				{
					if(all_points[c][0] == p_left[l_i][0] && all_points[c][1] == p_left[l_i][1]){
						current_left_points.addElement(left_points[c]);
						skip_loop = true;
					}
					
				}
				if(skip_loop){
					continue;
				}
				for(int r_i = 0; r_i < p_right.length; r_i++)
				{
					if(all_points[c][0] == p_right[r_i][0] && all_points[c][1] == p_right[r_i][1]){
						current_right_points.addElement(right_points[c]);
					}
					
				}
	
			}
			
			
		//display separator line
			sourceCode.highlight(6);
			if(current_axis == AXIS_X){
				draw_GM_axis_line(current_axis, separator, swindow.low_y, swindow.high_y);		
			} else 
			{
				draw_GM_axis_line(current_axis, separator, swindow.low_x, swindow.high_x);		
			}
			
			
		//display both areas and points
			left_gm_window.show();
			right_gm_window.show();
	
	
			for(int i_l = 0; i_l < current_left_points.size(); i_l++){
				current_left_points.get(i_l).show();
			}
			for(int i_r = 0; i_r < current_right_points.size(); i_r++){
				current_right_points.get(i_r).show();
			}
			
			lang.nextStep(translator.translateMessage("LABEL_CUT"));
			sourceCode.unhighlight(6);
			
		
			//hide right points, right area
				for(int i_r = 0; i_r < current_right_points.size(); i_r++){
					current_right_points.get(i_r).hide();
				}
				right_gm_window.hide();
	
			sourceCode.highlight(7);
			lang.nextStep();
			sourceCode.unhighlight(7);
			
			
		//unhighlight 
			sourceCode.unhighlight(2);
			p_leftArray.unhighlightCellSpace(0, p_leftArray.getMatrix()[0].length-1, 0, p_leftArray.getMatrix().length-1);
			p_rightArray.unhighlightCellSpace(0, p_rightArray.getMatrix()[0].length-1, 0, p_rightArray.getMatrix().length-1);
			node.unhighlightNode();
			
			
		//add primitives to vectors
			treePrimitives.addAll(node.getPrimitives());
			treeNodes.add(node);
		
			
		//hide left points, left area
			for(int i_l = 0; i_l < current_left_points.size(); i_l++){
				current_left_points.get(i_l).hide();
			}
			left_gm_window.hide();
			Coordinates leftUpperLeft = new Coordinates(node.getUpperLeft().getX(), node.getUpperLeft().getY() + radius*4);
			Coordinates rightUpperLeft = new Coordinates(node.getUpperLeft().getX()+node.getWidth()/2, node.getUpperLeft().getY() + radius*4);
			
			
		// call 2D-tree recursive with the left subarea
			node.setLeftNode(rek_kd_tree(new_axis, new KDTreeNode<int[], Integer>(leftUpperLeft, node.getWidth()/2, 1), p_leftArray, leftwindow));
			
			
		//show right points, right area
			for(int i_r = 0; i_r < current_right_points.size(); i_r++){
				current_right_points.get(i_r).show();
			}
			right_gm_window.show();
			sourceCode.highlight(7);
			node.highlightNode(nodeHighlightProperties);
			lang.nextStep();
			node.unhighlightNode();
			sourceCode.unhighlight(7);
			//hide right points, right area
			for(int i_r = 0; i_r < current_right_points.size(); i_r++){
				current_right_points.get(i_r).hide();
			}
			right_gm_window.hide();
			
			
		// call 2D-tree recursive with the right subarea
			node.setRightNode(rek_kd_tree(new_axis, new KDTreeNode<int[], Integer>(rightUpperLeft, node.getWidth()/2, 2), p_rightArray, rightwindow));
	
		return node;
		
	}
	
	/**** HELPER METHODS ****/
	
	/**
	 * displays the source code
	 */
	private void displaySourceCode () {
		
    	sourceCode = lang.newSourceCode(new Coordinates(P_X_AXIS, 500), "sourceCode", null, sourceCodeProperties);
    	    	
    	for(int i = 0; i < Integer.valueOf(translator.translateMessage("SOURCE_CODE_LENGTH")); i++){
    		sourceCode.addCodeLine(translator.translateMessage("SOURCE_CODE_" + i), "code", 0, null);
    	}
    }
	
	/**
	 * check if user input include the same point more than one time
	 * @param points the point's array
	 * @return array without duplicates
	 */
    private int[][] checkForDoubles(int[][] points){
		Vector<Boolean> delete_indices = new Vector<Boolean>();
		delete_indices.setSize(points.length);
		for(int b = 0; b < delete_indices.size(); b++){
			delete_indices.setElementAt(false, b);
		}
		int reduced = 0;
		for(int i = 0; i < points.length-1; i++){
			if(delete_indices.get(i)){
				continue;
			}
			for(int j = i+1; j < points.length; j++){
				if(points[i][0] == points[j][0] && points[i][1] == points[j][1]){
					delete_indices.setElementAt(true, j);
					reduced++;
				}
			}
		}
		int[][] s_points = new int[points.length - reduced][2];
		int s_count = 0;
		for(int s = 0; s < points.length; s++){
			if(delete_indices.get(s)){
				continue;
			}
			else {
				s_points[s_count][0] = points[s][0];
				s_points[s_count][1] = points[s][1];
				s_count++;
			}
		}
		
		if(reduced > 0){
			Coordinates c_info_c = new Coordinates(INFO_X0, INFO_Y0);
			Text info_c = lang.newText(c_info_c, reduced + translator.translateMessage("INFO_DOUBLES"), "info_1", null, gmTextProperties);
			diagramPrimitives.add(info_c); // add to primitive vector
		}
		
		return s_points;
	}
    
    /**
     * check if user input is out of range of draw window. If points out of range the will be set to the maximum of the window size.
     * @param points the point's array
     * @return array without point out of range
     */
    private int[][] checkForOutOfRange(int[][] points) {
    	boolean outOfRange = false;
    	for (int i = 0; i < points.length; i++) {
    			if (points[i][0] > X_COORD_N) points[i][0] = X_COORD_N; outOfRange = true;
    			if (points[i][1] > Y_COORD_N) points[i][1] = Y_COORD_N; outOfRange = true;
    	}
    	
		if(outOfRange){
			Coordinates c_info_o = new Coordinates(INFO_X0, INFO_Y0+15);
			Text info_o = lang.newText(c_info_o, translator.translateMessage("INFO_OUTOFRANGE"), "info_2", null, gmTextProperties);
			diagramPrimitives.add(info_o); // add to primitive vector
		}
    	
    	return points;
    }
	
	
	/**
	 * Mergesort According to Axis
	 * sort an array of int[][] according to the specified axis
	 * @param current_axis specified axis: 	AXIS_X will sort according to the first axis
	 * 										AXIS_Y will sort according to the second axis 
	 * @param s the int[][] array
	 * @return
	 */
	private int[][] mergeSortAccordingToAxis(int current_axis, int[][] s) {
		
		// unknown axis
		if (current_axis != AXIS_X && current_axis != AXIS_Y) {
			System.err.println("Mergesort: unknown axis");
		}
				
		if (s.length == 1) 
			return s;
		
		int[][] s1;
		int[][] s2;
		
		if (s.length%2 == 0) {
			s1 = new int[s.length/2][2]; 
			s2 = new int[s.length/2][2];
			System.arraycopy(s, 0, s1, 0, s.length/2);
			System.arraycopy(s, s.length/2, s2, 0, s.length/2);
		}
		
		else {
			s1 = new int[s.length/2][2]; 
			s2 = new int[s.length/2 +1][2];			
			System.arraycopy(s, 0, s1, 0, s.length/2);
			System.arraycopy(s, s.length/2, s2, 0, s.length/2+1);
		}
		
		s1 = mergeSortAccordingToAxis(current_axis, s1);
		s2 = mergeSortAccordingToAxis(current_axis, s2);
		
		return mergeAccordingToAxis(current_axis, s1, s2);
		
		
	}


	/**
	 * merge two sorted arrays according to current_axis
	 * @param current_axis specified axis: 	AXIS_X will sort according to the first axis
	 * 										AXIS_Y will sort according to the second axis 
	 * @param s1 first array
	 * @param s2 second array
	 * @return merged (sorted) array from s1 and s2
	 */
	private int[][] mergeAccordingToAxis(int current_axis, int[][] s1, int[][] s2) {
		
		int[][] s = new int[s1.length + s2.length][2];
		int i = 0;
		int i1 = 0;
		int i2 = 0;
				
			while (i1 < s1.length || i2 < s2.length) {
				
				if (i1 == s1.length) {
					s[i] = s2[i2];
					i++;
					i2++;
				}
				else if (i2 == s2.length) {
					s[i] = s1[i1];
					i++;
					i1++;
				}
				else if (current_axis == AXIS_X && s1[i1][0]<s2[i2][0]) {
					s[i] = s1[i1];
					i++;
					i1++;
				}
				else if (current_axis == AXIS_Y && s1[i1][1]<s2[i2][1]) {
					s[i] = s1[i1];
					i++;
					i1++;
				}
				else {
					s[i] = s2[i2];
					i++;
					i2++;
				}
			}
		
		return s;
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
    private int get_GM_x_pix(float x_coord){
		int x_coord_pix_ratio = GM_XL / X_COORD_N; 	
		return (GM_X0 + (int)(x_coord * x_coord_pix_ratio));
    }
    private int get_GM_y_pix(float y_coord){
		int y_coord_pix_ratio = GM_YL / Y_COORD_N;
		int gm_yn = GM_Y0 + GM_YL;
		return (gm_yn - (int)(y_coord * y_coord_pix_ratio));
    }
	
    /**
     * draw GM window
     * WARNING: the y-coordinates [0 ... N] are at [gm_yn ... GM_N0] (inverted) !!
     * @param points
     */
	private void draw_GM_window(int[][] points){
		//all colors here should be black
		
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
		Polyline border = lang.newPolyline(lcoords, "border", null, gmBorderProperties);
		diagramPrimitives.add(border);

		
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
				diagramPrimitives.add(t);
				diagramPrimitives.add(lt);
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
				diagramPrimitives.add(t);
				diagramPrimitives.add(lt);

		}		
		
		//draw points
		Circle[] circles = get_GM_points(points, gmAllPointsProperties, "normalpoint");
		for(int c = 0; c < circles.length; c++){
			Circle ci = circles[c];
			ci.show();
			diagramPrimitives.add(ci); //add primitives to vector
		}
		
		left_points = get_GM_points(points, gmLeftPointsProperties, "leftpoints");
		right_points = get_GM_points(points, gmRightPointsProperties, "rightpoints");
		
	}
	
	/**
	 * draws GM axis line
	 * @param axis
	 * @param separator
	 * @param low_c
	 * @param high_c
	 */
	private void draw_GM_axis_line(int axis, float separator, float low_c, float high_c){

		if(axis == AXIS_X){
			int c_pos_x = get_GM_x_pix(separator);
			int b_low = get_GM_y_pix(low_c);	
			int b_high = get_GM_y_pix(high_c);
			
			Coordinates[] lps = {new Coordinates(c_pos_x, b_low), new Coordinates(c_pos_x, b_high)};
			Polyline lt = lang.newPolyline(lps, "xline_"+lCounter, null, gmXLineProperties);
			//create line from (c_pos_x | GM_Y0) to (c_pos_x | gm_yn), color: red
			diagramPrimitives.add(lt); //add primitives to vector
			
		}
		
		else { // AXIS_Y
			int c_pos_y = get_GM_y_pix(separator);
			int b_low = get_GM_x_pix(low_c);
			int b_high = get_GM_x_pix(high_c);
			
			Coordinates[] lps = {new Coordinates(b_low, c_pos_y), new Coordinates(b_high, c_pos_y)};
			Polyline lt = lang.newPolyline(lps, "yline_"+lCounter, null, gmYLineProperties);
			diagramPrimitives.add(lt); //add primitives to vector

		}
		lCounter++;
	}
	
	/**
	 * 
	 * @param points
	 * @param cprops
	 * @param name
	 * @return
	 */
	private Circle[] get_GM_points(int[][] points, CircleProperties cprops, String name){
	
		Circle[] circles = new Circle[points.length];
		for(int i = 0; i < circles.length; i++){

			int i_x = get_GM_x_pix(points[i][0]);
			int i_y = get_GM_y_pix(points[i][1]);
			Coordinates ic = new Coordinates(i_x, i_y);
			Circle c = lang.newCircle(ic, 3, (name+"_"+i), null, cprops);
			c.hide();
			circles[i] = c;
		}
		return circles;
		
	}
	
	/**
	 * 
	 * @param sw
	 * @param rprops
	 * @return
	 */
	private Rect get_GM_current_window(SeparatorWindow sw, RectProperties rprops){
		int c_left = get_GM_x_pix(sw.get_lx());
		int c_up = get_GM_y_pix(sw.get_hy());
		int c_right = get_GM_x_pix(sw.get_hx());
		int c_down = get_GM_y_pix(sw.get_ly());
		Coordinates b_up_left = new Coordinates(c_left, c_up);
		Coordinates b_low_right = new Coordinates(c_right, c_down);

		Rect window = lang.newRect(b_up_left, b_low_right, "window"+rCounter, null, rprops);
		window.hide();
		
		rCounter++;
		return window;
	}
	
	/**
	 * traverses the matrix
	 * @param matrix
	 * @return the traversed matrix
	 */
	private int[][] traverse (int [][] matrix) {
		int[][] result = new int[matrix[0].length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j <matrix[0].length; j++) {
				result[j][i] = matrix[i][j];
			}
		}
		return result;
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
	 * generate method for generating in Animal
	 */
    @Override
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        gmLeftAreaProperties = (RectProperties)props.getPropertiesByName("gmLeftAreaProperties");
        gmLeftPointsProperties = (CircleProperties)props.getPropertiesByName("gmLeftPointsProperties");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        pointsArray = (int[][])primitives.get("pointsArray");
        currentAxisProperties = (PolylineProperties)props.getPropertiesByName("currentAxisProperties");
        nodeHighlightProperties = (CircleProperties)props.getPropertiesByName("nodeHighlightProperties");
        gmRightPointsProperties = (CircleProperties)props.getPropertiesByName("gmRightPointsProperties");
        pointsArrayProperties = (MatrixProperties)props.getPropertiesByName("pointsArrayProperties");
        nodeProperties = (CircleProperties)props.getPropertiesByName("nodeProperties");
        introProperties = (SourceCodeProperties)props.getPropertiesByName("introProperties");
        gmAllPointsProperties = (CircleProperties)props.getPropertiesByName("gmAllPointsProperties");
        gmYLineProperties = (PolylineProperties)props.getPropertiesByName("gmYLineProperties");
        gmXLineProperties = (PolylineProperties)props.getPropertiesByName("gmXLineProperties");
        nodeTextProperties = (TextProperties)props.getPropertiesByName("nodeTextProperties");
        gmRightAreaProperties = (RectProperties)props.getPropertiesByName("gmRightAreaProperties");
        
		init();
		introduction();
		kd_tree(pointsArray);
		conclusion();
        
        return lang.toString();
    }

    @Override
	public String getName() {
        return "KD-Tree" + "[" + location + "]";
    }

    @Override
	public String getAlgorithmName() {
        return "KD-Tree";
    }

    @Override
	public String getAnimationAuthor() {
        return "David Steiner, Jens Krüger";
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
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    @Override
	public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}
