package generators.sorting;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;

/**
 * LocalOutlierFactor is a ValidatingGenerator to create an ANIMAL-animation for the LOF-Algorithm.
 * 
 * This  animation contains the Source code on the right side with highlightings on the currently active lines, a
 * notification-box at the top, showing hints about the current operations and a cartesian coordinate system on the 
 * left side showing the points and currently calculated distances and values.
 * 
 * The constant k and the list of points will be served by parameters as well as properties for
 *  - Source Highlighting Color
 *  - Current Point Color
 *  - Near Points Color
 *  - Distance Line Color
 *  - LOF-Ring Color.
 *  - inlier-color
 *  - outlier-color
 * @author Andre Challier <andre.challier@stud.tu-darmstadt.de>
 *
 */
public class LocalOutlierFactor implements ValidatingGenerator {
	
	/*
	 * ANIMAL
	 */
	private Language lang;
	private Locale loc;
	private Translator trl;
    private Variables vars;
    private SourceCode sc;
    private Text title;
    private Text hint;
    private Text legendTitle;
    private Text legend1;
    private Text legend2;
    private Text legend3;
    private Square legendColor1;
    private Square legendColor2;
    private Square legendColor3;
    private Polyline border;
	
    /*
     * PRIMITIVES
     */
	private List<Point> points;
	private int k;
	
	/*
	 * PROPERTYS
	 */
    private CircleProperties props_points;
    private SourceCodeProperties props_sourcecode;
    private CircleProperties props_nearPoints;
    private CircleProperties props_KNN;
    private CircleProperties props_KDstRing;
    private CircleProperties props_LOFRing;
    private CircleProperties props_currentPoint;
    private CircleProperties props_strict_inlier;
    private CircleProperties props_inlier;
    private CircleProperties props_outlier;
    private PolylineProperties props_distance;
    
    //hardcoded
    private TextProperties props_title;
    private TextProperties props_text;
    private TextProperties props_info;
    private SquareProperties props_sq;
    
    /*
     * STATIC STRINGS
     */
    // Animal Primitives Arguments
    private static final String ARG_K = "k";
    private static final String ARG_POINTS = "Points";
    
    // Animal Properties Arguments
    private static final String ARG_SOURCECODE = "SourceCode";
    private static final String ARG_NEARPOINTS = "NearPoints";
    private static final String ARG_KNN = "K-th Nearest Neighbor";
    private static final String ARG_KDST_RING = "KDST-Ring";
    private static final String ARG_LOF_RING = "LOF-Ring";
    private static final String ARG_CURRENTPOINT = "CurrentPoint";
    private static final String ARG_STRICT_INLIER = "Strict Inlier";
    private static final String ARG_INLIER = "Inlier";
    private static final String ARG_OUTLIER = "Outlier";
    private static final String ARG_DISTANCE = "Distance";
    
    // Animal Primitives Names
    private static final String NAME_TITLE = "title";
    private static final String NAME_DESC = "desc_";
    private static final String NAME_SOURCECODE = "sourcecode";
    private static final String NAME_HINT = "hint";
    private static final String NAME_LEGEND_SQUARE = "legend_square";
    private static final String NAME_KDST = "_kdst";
    private static final String NAME_DISTANCE = "dst_";
    private static final String NAME_LGND = "legend";
    private static final String NAME_LGND_LINE1 = "legend1";
    private static final String NAME_LGND_LINE2 = "legend2";
    private static final String NAME_LGND_LINE3 = "legend3";
    private static final String NAME_LGND_COLOR1 = "legend_color_1";
    private static final String NAME_LGND_COLOR2 = "legend_color_2";
    private static final String NAME_LGND_COLOR3 = "legend_color_3";
    private static final String NAME_BORDER = "border";
    private static final String NAME_LOF = "_lof";
    private static final String NAME_SUMM_INLIER = "summ_inlier";
    private static final String NAME_SUMM_OUTLIER = "summ_outlier";
    private static final String NAME_SUMM_OTHER = "summ_other";
    // Question(Group) Names
    private static final String NAME_QG_NEIGHBORS = "qg_neighbor";
    private static final String NAME_QG_KNN = "qg_knn";
    private static final String NAME_QG_OUTLIER = "qg_outlier";
    private static final String NAME_Q_NEIGHBOR = "q_neighbor";
    private static final String NAME_Q_KNN = "q_knn";
    private static final String NAME_Q_OUTLIER = "q_outlier";
    
    
    // Translator General Keys
    private static final String TRL_TITLE = "TITLE";
    private static final String TRL_DESC = "DESC_";
    
    // Translator Section Keys
    private static final String TRL_SECTION_DESCRIPTION = "SECTION_DESCRIPTION";
	private static final String TRL_SECTION_KDIST = "SECTION_KDIST";
	private static final String TRL_SECTION_LRD = "SECTION_LRD";
	private static final String TRL_SECTION_LOF = "SECTION_LOF";
	private static final String TRL_SECTION_SUMMARY = "SECTION_SUMMARY";
	
	// Translator hint Keys
	private static final String TRL_HINT = "HINT";
	private static final String TRL_HINT_KNN_KDST = "HINT_KNN_KDST";
	private static final String TRL_HINT_ALL_NEIGHBORS = "HINT_ALL_NEIGHBORS";
	private static final String TRL_HINT_NEW_KDIST = "HINT_NEW_KDIST";
	private static final String TRL_HINT_REMOVE_NOT_NEIGHBOR = "HINT_REMOVE_NOT_NEIGHBOR";
	private static final String TRL_HINT_CALC_ALL_LRD = "HINT_CALC_ALL_LRD";
	private static final String TRL_HINT_SUM_ALL_RD = "HINT_SUM_ALL_RD";
	private static final String TRL_HINT_CALC_LRD = "HINT_CALC_LRD";
	private static final String TRL_HINT_CALC_ALL_LOF = "HINT_CALC_ALL_LOF";
	private static final String TRL_HINT_SUM_ALL_LRD = "HINT_SUM_ALL_LRD";
	private static final String TRL_HINT_CALC_LOF = "HINT_CALC_LOF";
	
	// Translator Variable Keys
	private static final String TRL_VAR_K = "VAR_K";
	private static final String TRL_VAR_CURRENT_POINT = "VAR_CURRENT_POINT";
	private static final String TRL_VAR_CP_KDST = "VAR_CP_KDST";
	private static final String TRL_VAR_CP_KNN = "VAR_CP_KNN";
	private static final String TRL_VAR_CP_LRD = "VAR_CP_LRD";
	private static final String TRL_VAR_CP_LOF = "VAR_CP_LOF";
	private static final String TRL_VAR_SUM_RD = "VAR_SUM_RD";
	private static final String TRL_VAR_SUM_LRD = "VAR_SUM_LRD";
			
	// Variable Keys
	
	// Translator Legend Keys
	private static final String TRL_LGND = "LGND";
	private static final String TRL_LGND_CURRENT_POINT = "LGND_CURRENT_POINT";
	private static final String TRL_LGND_NEIGHBOR_POINT = "LGND_NEIGHBOR_POINT";
	private static final String TRL_LGND_KTH_NN = "LGND_KTH_NN";
	private static final String TRL_LGND_INLIER = "LGND_INLIER";
	private static final String TRL_LGND_DEF_INLIER = "LGND_DEF_INLIER";
	private static final String TRL_LGND_OUTLIER = "LGND_OUTLIER";
	
	// Translator Info Keys
	private static final String TRL_INFO_DISTANCE = "INFO_DISTANCE";
	private static final String TRL_INFO_RDST = "INFO_RDST";
	private static final String TRL_INFO_KDST = "INFO_KDST";
	private static final String TRL_INFO_LRD = "INFO_LRD";
	private static final String TRL_INFO_LOF = "INFO_LOF";
	
	// translator Summary Keys
	private static final String TRL_SUMM_INLIER = "SUMM_INLIER";
	private static final String TRL_SUMM_OUTLIER = "SUMM_OUTLIER";
	private static final String TRL_SUMM_OTHER = "SUMM_OTHER";
	
	// Translator Question Keys
	private static final String TRL_QST_NEIGHBORS = "QST_NEIGHBORS";
	private static final String TRL_QST_NEIGHBORS_FB_RIGHT = "QST_NEIGHBORS_FB_RIGHT";
	private static final String TRL_QST_NEIGHBORS_FB_WRONG = "QST_NEIGHBORS_FB_WRONG";
	private static final String TRL_QST_KNN = "QST_KNN";
	private static final String TRL_QST_KNN_FB_RIGHT = "QST_KNN_FB_RIGHT";
	private static final String TRL_QST_KNN_FB_WRONG = "QST_KNN_FB_WRONG";
	private static final String TRL_QST_OUTLIER = "QST_OUTLIER";
	
	// Translator Question Keys
	
	/*
	 * Constant Strings for variable Types
	 */
	private static final String VAR_TYPE_STRING = "String";
	private static final String VAR_TYPE_INT = "int";
	
	/*
	 * Other Constants
	 */
	private static final int DESC_LINES = 20; 
	private static final int NR_OF_QUESTIONS = 3;
	private static final double QUESTION_CHANCE = 0.7; 
	
	//Question Groups
	QuestionGroupModel neighborsQuestions;
	QuestionGroupModel knnQuestions;
	QuestionGroupModel outlierQuestions;
	
	
	// decimal format with 2 digits after decimal point
	DecimalFormat twoDigit = new DecimalFormat("#.##");
	DecimalFormat fourDigit = new DecimalFormat("#.####");
	
	//comparator to compare two points by distance to one Point

	/**
	 * Create a new Instance of LocalOutlierFactor with the Locale defined in loc.
	 * 
	 * @param loc
	 */
	public LocalOutlierFactor(String path, Locale loc) {
		this.loc = loc;
		trl = new Translator(path, loc);
		//hardcoded propertys
		props_title = new TextProperties();
		props_title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		props_text = new TextProperties();
		props_text.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		props_info = new TextProperties();
		props_info.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		props_sq = new SquareProperties(NAME_LEGEND_SQUARE);
		props_sq.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	}
	
	private void calculateLOF() {
		vars.declare(VAR_TYPE_INT, trl(TRL_VAR_K), Integer.toString(k));
		legendColor1.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, (Color)props_currentPoint.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
		legend1.setText(trl(TRL_LGND_CURRENT_POINT), null, null);
		legendColor2.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, (Color)props_nearPoints.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
		legend2.setText(trl(TRL_LGND_NEIGHBOR_POINT), null, null);
		legendColor3.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, (Color)props_KNN.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
		legend3.setText(trl(TRL_LGND_KTH_NN), null, null);
		
		sc.highlight(0);
		hint.setText(trl(TRL_HINT_KNN_KDST), null, null);
		lang.nextStep(trl(TRL_SECTION_KDIST));
		points.forEach(p -> p.hideInfo());
		
		// find KNN and kDst
		
		declareCurrentPoint();
		
		for(Point p : points) {
			// STEP #1 All Points exept p are neighbors -> Sort by distance
			sc.highlight(1);
			sc.highlight(2);
			sc.highlight(3);
			p.setCurrent();
			p.setKnn(clone(points));
			p.getKnn().remove(p);
			p.getKnn().sort(new DistanceComparator(p));
			p.getKnn().forEach(q -> {
				q.setNear();
				q.showInfo(trl(TRL_INFO_DISTANCE, twoDigit.format(distance(p, q))));
			});
			hint.setText(trl(TRL_HINT_ALL_NEIGHBORS, p.getName()), null, null);
			updateCurrentPoint(p);
			if(Math.random() > 0.5) {
				askNeighborsQuestion(p);
			}else {
				askKNNQuestion(p);
			}
			
			lang.nextStep();
			sc.unhighlight(1);
			sc.unhighlight(2);
			sc.unhighlight(3);
			
			// STEP #2 p.kDist is the Distance of the k-th nearest neighbor
			sc.highlight(4);
			double kdst = distance(p, p.getKnn().get(k - 1));
			p.setkDst(kdst);
			Point knn = p.getKnn().get(k - 1);
			knn.setKNN();
			Circle kdst_circle = lang.newCircle(p.dot.getCenter(), (int)p.kDst, p.getName() + NAME_KDST, null, props_KDstRing);
			Node[] nodes = {p.dot.getCenter(), knn.dot.getCenter()};
			Polyline dst = lang.newPolyline(nodes, NAME_DISTANCE + p.getName() + "_" + knn.getName(), null, props_distance);
			hint.setText(trl(TRL_HINT_NEW_KDIST, twoDigit.format(p.kDst)), null, null);
			updateCurrentPoint(p);
			lang.nextStep();
			sc.unhighlight(4);
			
			// STEP #3 remove all neighbors where dist > kdist
			sc.highlight(5);
			hint.setText(trl(TRL_HINT_REMOVE_NOT_NEIGHBOR), null, null);
			p.getKnn().forEach(o -> o.setDefault());
			p.getKnn().forEach(o -> o.hideInfo());
			p.getKnn().removeIf(o -> distance(p,o) > p.getkDst());
			p.getKnn().forEach(o -> o.setNear());
			updateCurrentPoint(p);
		    lang.nextStep();
		    kdst_circle.hide();
		    dst.hide();
		    sc.unhighlight(5);
		    p.setDefault();
		    p.hideInfo();
		}
		sc.unhighlight(0);
		
		points.forEach(p -> p.setDefault());
		points.forEach(p -> p.hideInfo());
		discardCurrentPoint();
		
		// calc LRD

		sc.highlight(6);
		hint.setText(trl(TRL_HINT_CALC_ALL_LRD), null, null);
		lang.nextStep(trl(TRL_SECTION_LRD));
		declareCurrentPoint();
		vars.declare(VAR_TYPE_STRING, trl(TRL_VAR_SUM_RD), "-");
		for(Point p: points) {
			updateCurrentPoint(p);
			p.setCurrent();
			p.showKDST();
			
		    sc.highlight(7);
		    sc.highlight(8);
		    sc.highlight(9);
		    
		    double sum_rd = p.knn.stream()
		    		.mapToDouble(q -> reachabilityDistance(p,q))
		    		.sum();
		    vars.set(trl(TRL_VAR_SUM_RD), twoDigit.format(sum_rd));
		    List<Polyline> dsts = new LinkedList<Polyline>();
		    for(Point q: p.getKnn()) {
				q.setNear();
				Node[] nodes = {p.dot.getCenter(),q.dot.getCenter()};
		    	dsts.add(lang.newPolyline(nodes, NAME_DISTANCE + p.getName() + "_" + q.getName(), null, props_distance));
		        double rd = reachabilityDistance(p,q);
		        q.showInfo(trl(TRL_INFO_RDST, twoDigit.format(rd)));
			}
		    hint.setText(trl(TRL_HINT_SUM_ALL_RD, p.getName(), twoDigit.format(sum_rd)), null, null);
		    
			lang.nextStep();
			sc.unhighlight(7);
			sc.unhighlight(8);
		    sc.unhighlight(9);
		    dsts.forEach(dst -> dst.hide());
		    p.getKnn().forEach(q -> {
		    	q.hideInfo();
		    	q.setDefault();
		    });
		    
		    sc.highlight(10);
		    p.lrd = 1 / (sum_rd / (double)p.getKnn().size());
		    updateCurrentPoint(p);
		    hint.setText(trl(TRL_HINT_CALC_LRD, p.getName(), fourDigit.format(p.lrd)), null, null);
		    lang.nextStep();
		    sc.unhighlight(10);
		    p.setDefault();
		    p.hideInfo();
		}
		vars.discard(trl(TRL_VAR_SUM_RD));
		discardCurrentPoint();
		sc.unhighlight(6);
		
		// Calc LOF
		
		hint.setText(trl(TRL_HINT_CALC_ALL_LOF), null, null);
		sc.highlight(11);
		lang.nextStep(trl(TRL_SECTION_LOF));
		declareCurrentPoint();
		vars.declare(VAR_TYPE_STRING, trl(TRL_VAR_SUM_LRD), "-");
		for(Point p: points) {
			updateCurrentPoint(p);
			p.setCurrent();
			sc.highlight(12);
			sc.highlight(13);
			sc.highlight(14);
			double sum_lrd = p.knn.stream()
					.mapToDouble(q -> q.getLrd())
					.sum();
			vars.set(trl(TRL_VAR_SUM_LRD), fourDigit.format(sum_lrd));
			for(Point q : p.getKnn()) {
				q.setNear();
				q.showLRD();
			}
			hint.setText(trl(TRL_HINT_SUM_ALL_LRD, p.getName(), fourDigit.format(sum_lrd)), null, null);
			lang.nextStep();
			sc.unhighlight(12);
			sc.unhighlight(13);
			sc.unhighlight(14);
			for(Point q : p.getKnn()) {
				q.setDefault();
				q.hideInfo();
			}
		    
		    sc.highlight(15);
		    p.lof = (sum_lrd / (double)p.getKnn().size()) / p.getLrd();
		    updateCurrentPoint(p);
		    hint.setText(trl(TRL_HINT_CALC_LOF, p.getName(), twoDigit.format(p.lof)), null, null);
		    lang.nextStep();
		    sc.unhighlight(15);
		    p.setDefault();
		}
		vars.discard(trl(TRL_VAR_SUM_LRD));
		discardCurrentPoint();
		hint.setText("", null, null);
		sc.unhighlight(11);
	}

	private List<Point> clone(List<Point> list) {
		List<Point> rtn = new LinkedList<Point>();
		for(Point p: list) {
			rtn.add(p);
		}
		return rtn;
	}

	@Override
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		//get properties
		props_points = (CircleProperties)props.getPropertiesByName(ARG_POINTS);
        props_sourcecode = (SourceCodeProperties)props.getPropertiesByName(ARG_SOURCECODE);
        props_nearPoints = (CircleProperties)props.getPropertiesByName(ARG_NEARPOINTS);
        props_KNN = (CircleProperties)props.getPropertiesByName(ARG_KNN);
        props_KDstRing = (CircleProperties)props.getPropertiesByName(ARG_KDST_RING);
        props_LOFRing = (CircleProperties)props.getPropertiesByName(ARG_LOF_RING);
        props_currentPoint = (CircleProperties)props.getPropertiesByName(ARG_CURRENTPOINT);
        props_strict_inlier = (CircleProperties)props.getPropertiesByName(ARG_STRICT_INLIER);
        props_inlier = (CircleProperties)props.getPropertiesByName(ARG_INLIER);
        props_outlier = (CircleProperties)props.getPropertiesByName(ARG_OUTLIER);
        props_distance = (PolylineProperties)props.getPropertiesByName(ARG_DISTANCE);
        
        //Draw Title
  		title = lang.newText(new Coordinates(30,30), trl.translateMessage(TRL_TITLE), NAME_TITLE, null, props_title);
        
  		// Draw Description
  		
  		LinkedList<Text> desc = new LinkedList<Text>();
  		for(int i = 0; i < DESC_LINES; i++) {
  			Offset o;
  			if(i == 0) {
  				o = new Offset(0, 30, title, AnimalScript.DIRECTION_NW);
  			}else {
  				o = new Offset(0, 15, desc.get(i - 1), AnimalScript.DIRECTION_NW);
  			}
  			desc.add(lang.newText(o, trl(TRL_DESC + i), NAME_DESC + i, null, props_text));
  		}
  		lang.nextStep(trl(TRL_SECTION_DESCRIPTION));
  		desc.forEach(t -> t.hide());
  		
  		// Draw SourceCode
  		
   		sc = lang.newSourceCode(new Offset(0, 20, title, AnimalScript.DIRECTION_SW), NAME_SOURCECODE, null, props_sourcecode);
   		
   		sc.addCodeLine(trl.translateMessage("SRC_0"), null, 0, null);	// line 0
   		sc.addCodeLine(trl.translateMessage("SRC_1"), null, 1, null);	// line 1
   		sc.addCodeLine(trl.translateMessage("SRC_2"), null, 1, null);	// line 2
   		sc.addCodeLine(trl.translateMessage("SRC_3"), null, 1, null);	// line 3
   		sc.addCodeLine(trl.translateMessage("SRC_4"), null, 1, null);	// line 4
   		sc.addCodeLine(trl.translateMessage("SRC_5"), null, 1, null);	// line 5
   		sc.addCodeLine(trl.translateMessage("SRC_6"), null, 0, null);	// line 6
   		sc.addCodeLine(trl.translateMessage("SRC_7"), null, 1, null);	// line 7
   		sc.addCodeLine(trl.translateMessage("SRC_8"), null, 3, null);	// line 8
   		sc.addCodeLine(trl.translateMessage("SRC_9"), null, 3, null); 	// line 9
   		sc.addCodeLine(trl.translateMessage("SRC_10"), null, 1, null);	// line 10
   		sc.addCodeLine(trl.translateMessage("SRC_11"), null, 0, null);	// line 11
   		sc.addCodeLine(trl.translateMessage("SRC_12"), null, 1, null);	// line 12
   		sc.addCodeLine(trl.translateMessage("SRC_13"), null, 1, null);	// line 13
   		sc.addCodeLine(trl.translateMessage("SRC_14"), null, 2, null);	// line 14
   		sc.addCodeLine(trl.translateMessage("SRC_15"), null, 1, null);	// line 15
   		
   		hint = lang.newText(new Offset(100, 0, sc, AnimalScript.DIRECTION_NE), trl(TRL_HINT), NAME_HINT, null, props_text);
   		
   		// create legend
   		
        legendTitle = lang.newText(new Offset(0, 20, sc, AnimalScript.DIRECTION_SW), trl(TRL_LGND), NAME_LGND, null, props_text);
        
        legendColor1 = lang.newSquare(new Offset(0, 10, legendTitle, AnimalScript.DIRECTION_SW), 10, NAME_LGND_COLOR1, null, props_sq);
   		legend1 = lang.newText(new Offset(5, -4, legendColor1, AnimalScript.DIRECTION_NE), "", NAME_LGND_LINE1, null, props_text);
   		
   		legendColor2 = lang.newSquare(new Offset(0, 10, legendColor1, AnimalScript.DIRECTION_SW), 10, NAME_LGND_COLOR2, null, props_sq);
   		legend2 = lang.newText(new Offset(5, -4, legendColor2, AnimalScript.DIRECTION_NE), "", NAME_LGND_LINE2, null, props_text);
   		
   		legendColor3 = lang.newSquare(new Offset(0, 10, legendColor2, AnimalScript.DIRECTION_SW), 10, NAME_LGND_COLOR3, null, props_sq);
   		legend3 = lang.newText(new Offset(5, -4, legendColor3, AnimalScript.DIRECTION_NE), "", NAME_LGND_LINE3, null, props_text);
   		
   		//get primitives
   		points = new LinkedList<Point>();
        k = (Integer)primitives.get(ARG_K);
        String[][] stringMatrix = (String[][])primitives.get(ARG_POINTS);
        for(int i = 0; i < stringMatrix[0].length; i++) {
        	float x = Float.parseFloat(stringMatrix[0][i]);
        	float y = Float.parseFloat(stringMatrix[1][i]);
        	points.add(new Point(x, y, toAlphabetic(points.size())));
        }
        // draw lines sourrounding coordinate system
        Node[] square = new Node[5];
        square[0] = new Offset(0, 50, hint, AnimalScript.DIRECTION_SW);
        square[1] = new Offset(0, 570, hint, AnimalScript.DIRECTION_SW);
        square[2] = new Offset(520, 570, hint, AnimalScript.DIRECTION_SW);
        square[3] = new Offset(520, 50, hint, AnimalScript.DIRECTION_SW);
        square[4] = new Offset(0, 50, hint, AnimalScript.DIRECTION_SW);
        border = lang.newPolyline(square, NAME_BORDER, null);
        points.forEach(p -> p.showKoordinates());
        
        // init Questionmodels
        neighborsQuestions = new QuestionGroupModel(NAME_QG_NEIGHBORS, NR_OF_QUESTIONS);
        lang.addQuestionGroup(neighborsQuestions);
        
        //kDstQuestions = new QuestionGroupModel(NAME_QG_KDST, NR_OF_QUESTIONS);
        //lang.addQuestionGroup(kDstQuestions);
        
        //knnQuestions = new QuestionGroupModel(NAME_QG_KNN, NR_OF_QUESTIONS);
        //lang.addQuestionGroup(neighborsQuestions);
        
        //outlierQuestions = new QuestionGroupModel(NAME_QG_OUTLIER, NR_OF_QUESTIONS);
        //lang.addQuestionGroup(neighborsQuestions);
        
 		
 		calculateLOF();
 		summarize();
 		lang.nextStep(trl(TRL_SECTION_SUMMARY));
 		lang.finalizeGeneration();
        
		return lang.toString();
	}
	
	public void summarize() {
		// show LOF, find biggest/smallest
		double min_lof = Double.MAX_VALUE;
		double max_lof = Double.MIN_VALUE;
		for(Point p : points)  {
			if(p.lof < min_lof) min_lof = p.lof;
			if(p.lof > max_lof) max_lof = p.lof;
			p.showLOF();
		}
		Color strictinlier = (Color)props_strict_inlier.get(AnimationPropertiesKeys.FILL_PROPERTY);
		Color outlier = (Color)props_outlier.get(AnimationPropertiesKeys.FILL_PROPERTY);
		Color inlier = (Color)props_inlier.get(AnimationPropertiesKeys.FILL_PROPERTY);
		
		legendColor1.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, inlier, null, null);
		legend1.setText(trl(TRL_LGND_INLIER), null, null);
		legendColor2.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, strictinlier, null, null);
		legend2.setText(trl(TRL_LGND_DEF_INLIER), null, null);
		legendColor3.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, outlier, null, null);
		legend3.setText(trl(TRL_LGND_OUTLIER), null, null);
		// colorize points by LOF and draw LOF-Ring
		for(Point p: points) {
			Color lofcolor;
			if(p.lof <= 1) {
				double ratio = (p.lof - min_lof) / (1 - min_lof);
				lofcolor = blend(inlier, strictinlier, ratio);
			}else {
				double ratio = (p.lof - 1) / (max_lof - 1);
				lofcolor = blend(outlier, inlier, ratio);
			}
			p.dot.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, lofcolor, null, null);
			int r = (int)((p.lof) * 5);
			if(r > 4) {
				lang.newCircle(p.dot.getCenter(), r, p.getName() + NAME_LOF, null, props_LOFRing);
			}
			
		}
		
		/*
		 * Ask Questions
		 */		
		List<Point> ins = points.stream().sorted(new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					return Double.compare(o1.getLof(), o2.getLof());
				}
		}).collect(Collectors.toList());
		Point def = ins.get(ins.size()/2);
		Point in = ins.get(0);
		Point out = ins.get(ins.size()-1);
		
		askOutlierQuestion(in);
		lang.nextStep();
		askOutlierQuestion(def);
		lang.nextStep();
		askOutlierQuestion(out);
		lang.nextStep();
		
		/*
		 * Find anomalous data 
		 */
		
		//calculate Standard deviation
		double stdDev = points.stream()
				.filter(p -> p.lof > 1)
				.mapToDouble(p -> Math.pow(p.getLof() - 1, 2))
				.sum() / points.size();
		List<Point> myPoints = clone(points);
		List<Point> outlierPoints = myPoints.stream().filter(p -> p.getLof() > (1 + stdDev)).collect(Collectors.toList());
		myPoints.removeAll(outlierPoints);
		List<Point> inlierPoints = myPoints.stream().filter(p -> p.lof <= 1).collect(Collectors.toList());
		myPoints.removeAll(inlierPoints);
		String inliers = pointList(inlierPoints);
		String outliers = pointList(outlierPoints);
		String others = pointList(myPoints);

		Text inlierText = lang.newText(new Offset(40, 0, border, AnimalScript.DIRECTION_NE), 
				trl(TRL_SUMM_INLIER), 
				NAME_SUMM_INLIER + "_text", 
				null,
				props_text);
		Text inlierValues = lang.newText(new Offset(10, 10, inlierText, AnimalScript.DIRECTION_SW), 
				inliers, 
				NAME_SUMM_INLIER + "_values", 
				null,
				props_text);
		Text outlierText = lang.newText(new Offset(-10, 10, inlierValues, AnimalScript.DIRECTION_SW), 
				trl(TRL_SUMM_OUTLIER), 
				NAME_SUMM_OUTLIER + "_text", 
				null,
				props_text);
		Text outlierValues = lang.newText(new Offset(10, 10, outlierText, AnimalScript.DIRECTION_SW), 
				outliers, 
				NAME_SUMM_OUTLIER + "_values", 
				null,
				props_text);
		Text otherText = lang.newText(new Offset(-10, 10, outlierValues, AnimalScript.DIRECTION_SW), 
				trl(TRL_SUMM_OTHER), 
				NAME_SUMM_OTHER + "_text", 
				null,
				props_text);
		lang.newText(new Offset(10, 10, otherText, AnimalScript.DIRECTION_SW), 
				others, 
				NAME_SUMM_OTHER + "_values", 
				null,
				props_text);
	}
	/**
	 * blend blends two colors by a weight. if weight is 1.0 only c0 will be returned, if weight is 0.0 only c1 will be
	 * returned and if weight is 0.5 both colors will be equally blended together.
	 * 
	 * @param c0	first color
	 * @param c1	second color
	 * @param weight	blending weight.
	 * @return
	 */
	public static Color blend(Color c0, Color c1, double weight) {
		double weight0 = weight;
		double weight1 = 1-weight0;

	    double r = weight0 * c0.getRed() + weight1 * c1.getRed();
	    double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
	    double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();

	    return new Color((int) r, (int) g, (int) b);
	}

	public String getName() {
        return "Local Outlier Factor";
    }

    public String getAlgorithmName() {
        return "Local Outlier Factor";
    }

    public String getAnimationAuthor() {
        return "Andre Challier, Christian Richter";
    }

    public String getDescription(){
        return "The local outlier factor is an algorithm proposed by Markus M. Breunig, Hans-"
 +"\n"
 +"Peter Kriegel, Raymond T. Ng and Jörg Sander in 2000 for finding anomalous data"
 +"\n"
 +"points by measuring the local deviation of a given data point with respect to"
 +"\n"
 +"is neighbors."
 +"\n"
 +"\n"
 +"The algorithm starts with a constant k and a set of objects with a distance"
 +"\n"
 +"function. At first the distance to the k-th nearest neighbor will be determined"
 +"\n"
 +"for every object A ( k-distance(A) ), as well as a set of objects containing all"
 +"\n"
 +"objects within the k-distance of an object ( Nk(A) )."
 +"\n"
 +"\n"
 +"The next step is to calculate the local reachability density (lrd) for every"
 +"\n"
 +"object, wich is the inverse of the average reachability distance of an object"
 +"\n"
 +"from its neighbors, wich can be infinite at duplicate points."
 +"\n"
 +"\n"
 +"Afterwards the local reachability densities will be compared with those of the"
 +"\n"
 +"neighbors, which is the average local reachability density of the neighbors"
 +"\n"
 +"divided by the objects own local reachability density. A value near 1 indicates"
 +"\n"
 +"that the object comparable to its neighbors, a value below 1 indicates a denser"
 +"\n"
 +"region, while values significantly larger than 1 indicate outliers.";
    }

    public String getCodeExample(){
        return "CODE";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
    	return loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

	@Override
	public void init() {
		lang = new AnimalScript("Local Outlier Factor", "Andre Challier, Christian Richter", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		vars = lang.newVariables();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		int k = (Integer)primitives.get("k");
        String[][] stringMatrix = (String[][])primitives.get("Points");
        if(stringMatrix.length != 2) throw new IllegalArgumentException("Points Matrix must have 2 columns!");
        if(stringMatrix[0].length != stringMatrix[1].length) throw new IllegalArgumentException("Every Point must habe x and y value");
        if(stringMatrix[0].length <= k) throw new IllegalArgumentException("Must have at least k+1 Points");
        for(int i = 0; i < stringMatrix[0].length; i++) {
        	float x;
        	float y;
        	try {
        		x = Float.parseFloat(stringMatrix[0][i]);
        	} catch (NumberFormatException nfe) {
        		throw new IllegalArgumentException("\"" + stringMatrix[0][i] + "\" is not a floating-point arithmetic.");
        	}
        	try {
        		y = Float.parseFloat(stringMatrix[1][i]);
        	} catch (NumberFormatException nfe) {
        		throw new IllegalArgumentException("\"" + stringMatrix[1][i] + "\" is not a floating-point arithmetic.");
        	}
        	if(0 > x || 500 < x) {
        		throw new IllegalArgumentException("\"" + x + "\" is out of range (0-500).");
        	}
        	if(0 > y || 500 < y) {
        		throw new IllegalArgumentException("\"" + y + "\" is out of range (0-500).");
        	}
        }
		return true;
	}
	
	public double distance(Point a, Point b) {
		return Math.sqrt(Math.pow(a.getX()- b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}
	
	public double reachabilityDistance(Point a, Point b) {
		return Math.max(b.getkDst(), distance(a,b));
	}
	
	/**
	 * A shortcut for trl.translateMessage(key)
	 * 
	 * @param key message key
	 * @return
	 */
	private String trl(String key) {
		return trl.translateMessage(key);
	}
	
	/**
	 * A shortcut for trl.translateMessage(key, params)
	 * 
	 * @param key message key
	 * @param params String... of params
	 * @return
	 */
	private String trl(String key, String... params ) {
		return trl.translateMessage(key, params);
	}
	
	private void declareCurrentPoint() {
		vars.declare(VAR_TYPE_STRING, trl(TRL_VAR_CURRENT_POINT));
		vars.declare(VAR_TYPE_STRING, trl(TRL_VAR_CP_KDST));
		vars.declare(VAR_TYPE_STRING, trl(TRL_VAR_CP_KNN));
		vars.declare(VAR_TYPE_STRING, trl(TRL_VAR_CP_LRD));
		vars.declare(VAR_TYPE_STRING, trl(TRL_VAR_CP_LOF));
	}
	
	private void updateCurrentPoint(Point p) {
		vars.set(trl(TRL_VAR_CURRENT_POINT), p.getName());
		if(Double.isNaN(p.getkDst())) {
			vars.set(trl(TRL_VAR_CP_KDST), " - ");
		}else {
			vars.set(trl(TRL_VAR_CP_KDST), twoDigit.format(p.getkDst()));
		}
		
		if(Double.isNaN(p.getLrd())) {
			vars.set(trl(TRL_VAR_CP_LRD), " - ");
		}else {
			vars.set(trl(TRL_VAR_CP_LRD), fourDigit.format(p.getLrd()));
		}
		
		if(Double.isNaN(p.getLof())) {
			vars.set(trl(TRL_VAR_CP_LOF), " - ");
		}else {
			vars.set(trl(TRL_VAR_CP_LOF), fourDigit.format(p.getLof()));
		}
		
		vars.set(trl(TRL_VAR_CP_KNN), pointList(p.getKnn()));
	}
	
	private void discardCurrentPoint() {
		vars.discard(trl(TRL_VAR_CURRENT_POINT));
		vars.discard(trl(TRL_VAR_CP_KDST));
		vars.discard(trl(TRL_VAR_CP_KNN));
		vars.discard(trl(TRL_VAR_CP_LRD));
		vars.discard(trl(TRL_VAR_CP_LOF));
	}
	
	public static String toAlphabetic(int i) {
	    if( i<0 ) {
	        return "-"+toAlphabetic(-i-1);
	    }

	    int quot = i/26;
	    int rem = i%26;
	    char letter = (char)((int)'A' + rem);
	    if( quot == 0 ) {
	        return ""+letter;
	    } else {
	        return toAlphabetic(quot-1) + letter;
	    }
	}
	
	private String pointList(List<Point> l) {
		String ret = new String("");
		if(l == null) return ret;
		for(int i = 0; i < l.size(); i++) {
			ret += l.get(i).getName();
			if(i != l.size() - 1) {
				ret += ", ";
			}
		}
		return ret;
	}
	
	/*
	 * 
	 * Questions
	 *
	 */
	private void askNeighborsQuestion(Point p) {
		if(askQuestion()) {
			MultipleSelectionQuestionModel neighborsQuestion = new MultipleSelectionQuestionModel(NAME_Q_NEIGHBOR + p.getName());
			neighborsQuestion.setPrompt(trl(TRL_QST_NEIGHBORS, p.getName()));
			neighborsQuestion.setGroupID(NAME_QG_NEIGHBORS);
			List<Point> allneighbors = points.stream()
					.filter(q -> !q.equals(p))
					.sorted(new DistanceComparator(p))
					.collect(Collectors.toList());
			double kDst = distance(p, allneighbors.get(k - 1));
			allneighbors.sort(new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			for(int i = 0; i < allneighbors.size(); i++) {
				Point q = allneighbors.get(i);
				if(distance(q, p) <= kDst) {
					neighborsQuestion.addAnswer(q.getName(), 1, trl(TRL_QST_NEIGHBORS_FB_RIGHT, q.getName()));
				}else {
					neighborsQuestion.addAnswer(q.getName(), 0, trl(TRL_QST_NEIGHBORS_FB_WRONG, q.getName()));
				}
			}
			lang.addMSQuestion(neighborsQuestion);
		}
	}
	
	private void askKNNQuestion(Point p) {
		if(askQuestion()) {
			MultipleChoiceQuestionModel KNNQuestion = new MultipleChoiceQuestionModel(NAME_Q_KNN + p.getName());
			KNNQuestion.setPrompt(trl(TRL_QST_KNN, p.getName()));
			KNNQuestion.setGroupID(NAME_QG_KNN);
			List<Point> allneighbors = points.stream()
					.filter(q -> !q.equals(p))
					.sorted(new DistanceComparator(p))
					.collect(Collectors.toList());
			Point knn = allneighbors.get(k-1);
			double kDist = distance(p, knn);
			if(allneighbors.stream().filter(q -> distance(p, q) == kDist).collect(Collectors.toList()).size() != 1) return;
			for(int i = 0; i < allneighbors.size(); i++) {
				Point q = allneighbors.get(i);
				if(q.equals(knn)) {
					KNNQuestion.addAnswer(q.getName(), 1, trl(TRL_QST_KNN_FB_RIGHT, q.getName(), p.getName()));
				}else {
					KNNQuestion.addAnswer(q.getName(), 1, trl(TRL_QST_KNN_FB_WRONG, p.getName(), knn.getName()));
				}
			}
			lang.addMCQuestion(KNNQuestion);
		}
	}
	
	private void askOutlierQuestion(Point p) {
		TrueFalseQuestionModel outlierQuestion = new TrueFalseQuestionModel(NAME_Q_OUTLIER + p.getName());
		outlierQuestion.setPrompt(trl(TRL_QST_OUTLIER, p.getName()));
		outlierQuestion.setGroupID(NAME_QG_OUTLIER);
		double stdDev = points.stream()
				.filter(q -> q.lof > 1)
				.mapToDouble(q -> Math.pow(q.getLof() - 1, 2))
				.sum() / points.size();
		outlierQuestion.setCorrectAnswer(p.lof > stdDev + 1.0);
		outlierQuestion.setPointsPossible(1);
		lang.addTFQuestion(outlierQuestion);
	}
	
	private boolean askQuestion() {
    	return (Math.random() <= QUESTION_CHANCE);
    }
	
	public class Point {
		
		private double x;
		private double y;
		private String name;
		
		private double kDst;
		private double lrd;
		private double lof;
		private Circle dot;
		private Text nameText;
		private Text infoText;
		
		private List<Point> knn;
		
		public Point(double x, double y, String name) {
			this.x = x;
			this.y = y;
			this.name = name;
			kDst = Double.NaN;
			lrd = Double.NaN;
			lof = Double.NaN;
			dot = lang.newCircle(new Offset((int)(x) + 10, (int)(y + 60), hint, AnimalScript.DIRECTION_SW), 5, "point_" + name, null, props_points);
			nameText = lang.newText(new Offset(0, -12, dot, AnimalScript.DIRECTION_NW), getName(), "name_" + getName(), null, props_info);
			infoText = lang.newText(new Offset(0, 0, dot, AnimalScript.DIRECTION_SW), getName(), "info_" + getName(), null, props_info);
			infoText.hide();
			nameText.show();
		}
		
		
		/*
		 * Display Options
		 */
		
		public void setCurrent() {
			dot.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, (Color)props_currentPoint.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
		}
		
		public void setDefault() {
			dot.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, (Color)props_points.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
		}
		
		public void setNear() {
			dot.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, (Color)props_nearPoints.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
		}
		
		public void setKNN() {
			dot.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, (Color)props_KNN.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
		}
		
		public void showKoordinates() {
			infoText.setText("[" + (int)x + ", " + (int)y + "]", null, null);
			infoText.show();
		}
		
		public void showKDST() {
			infoText.setText(trl(TRL_INFO_KDST, twoDigit.format(kDst)), null, null);
			infoText.show();
		}
		
		public void showLRD() {
			infoText.setText(trl(TRL_INFO_LRD, fourDigit.format(lrd)), null, null);
			infoText.show();
		}
		
		public void showLOF() {
			infoText.setText(trl(TRL_INFO_LOF, fourDigit.format(lof)), null, null);
			infoText.show();
		}
		
		public void showInfo(String s) {
			infoText.setText(s, null, null);
			infoText.show();
		}
		
		public void hideInfo() {
			infoText.hide();
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public double getkDst() {
			return kDst;
		}

		public void setkDst(double kDst) {
			this.kDst = kDst;
		}

		public double getLrd() {
			return lrd;
		}

		public void setLrd(double lrd) {
			this.lrd = lrd;
		}

		public double getLof() {
			return lof;
		}

		public void setLof(double lof) {
			this.lof = lof;
		}

		public List<Point> getKnn() {
			return knn;
		}

		public void setKnn(List<Point> knn) {
			this.knn = knn;
		}
		
		public String getName() {
			return name;
		}
	}
	
	private class DistanceComparator implements Comparator<Point> {
		
		Point p;
		
		public DistanceComparator(Point p) {
			this.p = p;
		}

		@Override
		public int compare(Point q1, Point q2) {
			return Double.compare(distance(q1, p), distance(q2, p));
		}
		
	}

}
