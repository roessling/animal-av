/*
 * Marzullos.java
 * Lutz Langhorst, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.helpersMarzullo.Marzullo_Tuple;
import translator.Translator;

public class Marzullo_algorithm implements ValidatingGenerator {
	private Translator t;
	private Locale genLocale;
	
	private Language l;
	private Variables vars;
	
	private String[][] input_double_intervals;
    private int intervalLength;
    private boolean showAllBest;
    
    private SourceCodeProperties code;
    private MatrixProperties matrix;
    private RectProperties rectangle;
    private RectProperties line;
    private RectProperties mark;
    private PointProperties hPoint;
    private TextProperties text;
    private TextProperties numberText;
    private TextProperties headline;
    private TextProperties title;
    
    Text titleVis;
	Rect titleRec;
    
    private Comparator<Marzullo_Tuple> cmpM = (Marzullo_Tuple o1, Marzullo_Tuple o2)->o1.compare(o2, false);
    //private Comparator<double[]> cmpD = (double[] o1, double[] o2)->{if((o1[1] - o1[0])-(o2[1] - o2[0]) < 0) {return 1;} else {return -1;}};
    
    /*
     * main
     */
    
    @SuppressWarnings("unchecked")
	private ArrayList<double[]> marzullo(ArrayList<Marzullo_Tuple> iTable, Offset mainOffset) {
    	ArrayList<Marzullo_Tuple> table = (ArrayList<Marzullo_Tuple>) iTable.clone();
    	
		//Marzullo var
    	int cnt = 0;
    	int best = 0;
		double bestStart;
		double bestEnd;
		int i;

		//vis var
		vars.declare("int", "best", "0");
		vars.declare("int", "cnt", "0");
		vars.declare("double", "bestStart");
		vars.declare("double", "bestEnd");
		vars.declare("int", "i");
		ArrayList<double[]> bestIntervall = new ArrayList<double[]>();
		
		// JavaCode
		Text codeHeader = l.newText(mainOffset, t.translateMessage("marzCodeHeader"), "MarzCodeHeader", null,headline);
		SourceCode marzCode = l.newSourceCode(new Offset(10, 0, codeHeader, "SW"), "MarzCode", null, code);
		marzCode.addCodeLine("void marzullo(ArrayList<Marzullo_Tuple> table) {", null, 0, null);
		marzCode.addCodeLine("table.sort(cmpM);", null, 1, null);
		marzCode.addCodeLine("for(i = 0 ; i < table.size() ; i++) {", null, 1, null);
		marzCode.addCodeLine("cnt -= table.get(i).edg();", null, 2, null);
		marzCode.addCodeLine("if(cnt > best) {", null, 2, null);
		marzCode.addCodeLine("best = cnt;", null, 3, null);
		marzCode.addCodeLine("beststart = table.get(i).pos();", null, 3, null);
		marzCode.addCodeLine("bestend = table.get(i+1).pos();", null, 3, null);
		marzCode.addCodeLine("}", null, 3, null);
		marzCode.addCodeLine("}", null, 2, null);
		marzCode.addCodeLine("}", null, 1, null);
		marzCode.addCodeLine("}", null, 0, null);
		
		//show table
		marzCode.highlight(0);
		String[][] tempM = showMarzTable(table);
		StringMatrix tableVis = l.newStringMatrix(new Offset(10, 0, marzCode, "NE"), tempM, "tableVis", null, matrix);
		//show Vars
		String[][] tempVarVis = {{"cnt", "0"}, {"best", "0"}, {"bestStart", ""}, {"bestEnd", ""}};
		StringMatrix varVis = l.newStringMatrix(new Offset(10, 0, tableVis, "NE"), tempVarVis, "varVis", null, matrix);
		//show bars
		inputVis( iTable , new Offset(10, 12, varVis, "SW"));
		
		l.nextStep(t.translateMessage("marzCodeHeader"));
		tableVis.hide();
		

		marzCode.toggleHighlight(0, 1);
		table.sort(cmpM);
		tempM = showMarzTable(table);
		tableVis = l.newStringMatrix(new Offset(10, 0, marzCode, "NE"), tempM, "tableVis", null, matrix);
		l.nextStep();
		marzCode.toggleHighlight(1, 2);

		for(i = 0 ; i < table.size() ; i++) {
			tableVis.highlightCellColumnRange(i, 0, 1, null, null);
			marzCode.highlight(2);
			vars.set("i", ""+i);
			l.nextStep();
			
			marzCode.toggleHighlight(2, 3);
			cnt -= table.get(i).edge();
			vars.set("cnt", ""+cnt);
			varVis.put(0, 1, ""+cnt, null, null);
			l.nextStep();
			
			marzCode.toggleHighlight(3, 4);
			l.nextStep();
			if(cnt > best) {
				marzCode.highlight(5);
				best = cnt;
				vars.set("best", ""+best);
				varVis.put(1, 1, ""+best, null, null);
				l.nextStep();
				
				marzCode.toggleHighlight(5, 6);
				bestStart = table.get(i).pos();
				vars.set("bestStart", ""+bestStart);
				varVis.put(2, 1, ""+bestStart, null, null);
				l.nextStep();
				
				marzCode.toggleHighlight(6, 7);
				bestEnd = table.get(i+1).pos();
				vars.set("bestEnd", ""+bestEnd);
				varVis.put(3, 1, ""+bestEnd, null, null);
				tableVis.highlightCellColumnRange(i+1, 0, 1, null, null);
				
				l.hideAllPrimitivesExcept( Arrays.asList(new Primitive[] {codeHeader, marzCode, tableVis, varVis, titleVis, titleRec}));
				ArrayList<Marzullo_Tuple> vis = (ArrayList<Marzullo_Tuple>) iTable.clone();
				vis.add(new Marzullo_Tuple(bestStart, -1));
				vis.add(new Marzullo_Tuple(bestEnd, 1));
				inputVis( vis , new Offset(10, 12, varVis, "SW"));
				
				l.nextStep();
				tableVis.unhighlightCell(i+1, 0, null, null);
				tableVis.unhighlightCell(i+1, 1, null, null);
				
				bestIntervall = new ArrayList<double[]>();
				bestIntervall.add(new double[] {bestStart, bestEnd});
			}else if(cnt == best) {
				bestIntervall.add(new double[] {table.get(i).pos(), table.get(i+1).pos()});
			}
			
			marzCode.unhighlight(4);
			marzCode.unhighlight(7);
			tableVis.unhighlightCell(i, 0, null, null);
			tableVis.unhighlightCell(i, 1, null, null);
		}
		
		return bestIntervall;
	}
    
    @SuppressWarnings(value = "unchecked")
    private void setupVisFrame() {
    	ArrayList<Marzullo_Tuple> table = new ArrayList<Marzullo_Tuple>();
    	stringMatToTable(input_double_intervals , table);
    	
    	//title
    	titleVis = l.newText(new Coordinates(30, 10), t.translateMessage("title"), "title", null, title);
    	titleRec = l.newRect(new Offset(-10, 0, titleVis, "NW"), new Offset(10, 0, titleVis, "SE"), "title", null, rectangle);
		Offset mainOffset = new Offset(10, 10, titleVis, "SW");
    	
    	//intro
		Vector<String> introText = new Vector<String>();
		loadText(introText, "intro");
		Text introHeader = l.newText(mainOffset, t.translateMessage("introHeader"), "iH", null, headline);
		showText(introText , new Offset(10,2,introHeader,"SW"), null);
		l.nextStep(t.translateMessage("introHeader"));
		l.hideAllPrimitives();
		titleRec.show();
		titleVis.show();
    	
    	//input
		Text inputHeadline = l.newText(mainOffset, t.translateMessage("input"), "input", null, headline);
		StringMatrix inputM = l.newStringMatrix(new Offset(0, 10, inputHeadline, "SW"), input_double_intervals, "inputM", null, matrix);
		//danger: absolute positioning
		inputVis(table, new Offset(30, 13, inputM, "NE"));
		Text inputInfo = l.newText(new Offset(0, 10, inputM, "SW"), t.translateMessage("inputToTable1"), "input", null, text);
		l.newText(new Offset(0, 0, inputInfo, "SW"), t.translateMessage("inputToTable2"), "input", null, text);
		l.nextStep(t.translateMessage("input"));
		l.hideAllPrimitivesExcept(titleVis);
		titleRec.show();
		inputM.hide();
    	
    	//run
		ArrayList<double[]> bestIntervalls = marzullo((ArrayList<Marzullo_Tuple>) table.clone(), mainOffset);
		
    	//outro
    	l.hideAllPrimitives();
		titleRec.show();
		titleVis.show();
		
		int sumDist;
		if(showAllBest) {
			for(double[] e : bestIntervalls) {
				table.add(new Marzullo_Tuple(e[0], -1));
				table.add(new Marzullo_Tuple(e[1], 1));
			}
			sumDist = 26*bestIntervalls.size();
		}else {
			table.add(new Marzullo_Tuple(bestIntervalls.get(0)[0], -1));
			table.add(new Marzullo_Tuple(bestIntervalls.get(0)[1], 1));
			sumDist = 26;
		}
		
		inputHeadline = l.newText(mainOffset, t.translateMessage("intervals"), "input", null, headline);
		inputM = l.newStringMatrix(new Offset(0, 10, inputHeadline, "SW"), input_double_intervals, "inputM", null, matrix);
		//danger: absolute positioning
		inputVis(table, new Offset(30, 13, inputM, "NE"));
		
		Vector<String> outroText = new Vector<String>();
		loadText(outroText, "outro");
		double bestS = bestIntervalls.get(0)[0];
		double bestE = bestIntervalls.get(0)[1];
		double bestO = (bestE - bestS)/2;
		Text outroHeader = l.newText(new Offset(0, 10 + sumDist, inputM, "SW"), t.translateMessage("outroHeader"), "oH", null, headline);
		showText(outroText, new Offset(10,2,outroHeader,"SW"),
				new String[]{""+bestS, ""+bestE, ""+(bestS + bestO), ""+bestO, ""+(Integer.parseInt(vars.get("i"))+1), vars.get("best") , ""+input_double_intervals.length});
		l.nextStep(t.translateMessage("outroHeader"));
    }
    
    /*
     * Algorithm utility
     */
    
    private void stringMatToTable(String[][] mat, ArrayList<Marzullo_Tuple> table) {
    	for(int i = 0 ; i < mat.length ; i++) {
    		table.addAll(Marzullo_Tuple.intervall(Double.parseDouble(mat[i][0]), Double.parseDouble(mat[i][1])));
    	}
    }
    
    private void loadText(Vector<String> v, String id) {
		int codeSize = Integer.parseInt(t.translateMessage(id +"Size"));
		for (int i = 0; i < codeSize; i++) {
			v.add(t.translateMessage("" + id + i));
		}
	}
    
    private void showText(List<String> itext, Offset offset , String[] values){
    	if(values != null)
    	for(int i = 0 ; i < values.length ; i++) {
    		for(int j = 0 ; j < itext.size() ; j++) {
    			itext.set(j, itext.get(j).replaceAll("\\{"+ i +"\\}", values[i]));
    		}
    	}
    	
    	for(String e : itext) {
    		Text temp = l.newText(offset, e, "text", null, text);
    		offset = new Offset(0,0,temp,"SW");
    	}
    }
    
    @SuppressWarnings(value = { "unchecked", "unused" })
	private void inputVis(ArrayList<Marzullo_Tuple> table, Offset offset) {
    	ArrayList<Marzullo_Tuple> x = (ArrayList<Marzullo_Tuple>) table.clone();
		x.sort(cmpM);
		double min = x.get(0).pos();
		double max = x.get(x.size()-1).pos();
    	
    	Primitive prevPrim = l.newPoint(offset, "tempPoint", null, hPoint);
    	prevPrim.hide();
    	
    	for(int i = 0 ; i < table.size(); i += 2) {
    		Rect linePrim = l.newRect( new Offset(0, 0, prevPrim, "NW"), new Offset(intervalLength,1,prevPrim, "NW"), "inVis", null, line);
    		int start = (int) Math.round(intervalLength*(table.get(i).pos()-min)/(max - min));
    		int end= (int) Math.round(intervalLength*(table.get(i + 1).pos()-min)/(max - min));
    		Rect lineMarc = l.newRect(new Offset(start, -2, linePrim, "NW"), new Offset(end, 3, linePrim, "NW"), "inVis", null, mark);
    		Text nStart = l.newText(new Offset(0, 1, lineMarc, "SW"), (""+table.get(i).pos()).replaceAll("\\.0$", ""), "nStart", null, numberText);
    		Text nEnd = l.newText(new Offset(0, 1, lineMarc, "SE"), (""+table.get(i +1).pos()).replaceAll("\\.0$", ""), "nEnd", null, numberText);
    		
    		prevPrim = l.newPoint(new Offset(0, 25, linePrim, "SW"), "tempPoint", null, hPoint);
    	}
    }
    
    private String[][] showMarzTable(ArrayList<Marzullo_Tuple> table){
    	String[][] temp = new String[table.size()][2];
 		for(int i = 0 ; i < table.size(); i++) {
 			temp[i][0] = ""+ table.get(i).pos();
 			temp[i][1] = ""+ table.get(i).edge();
 		}
 		return temp;
    }
    
    /*
     * Generator utility
     */
    
    /*
     * Locale.GERMANY
     * Locale.US
     */
    Marzullo_algorithm(Locale loc) {
		genLocale = loc;
		t = new Translator("resources/Marzullo_algorithm", genLocale);
	}

    public void init(){
        l = new AnimalScript("Marzullo's Algorthim", "Lutz Langhorst", 800, 600);
        l.setStepMode(true);
		vars = l.newVariables();
    }
    
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
    	
    	input_double_intervals = (String[][])primitives.get("input_double_intervals");
    	intervalLength = (Integer)primitives.get("interval_display_size");
		
		String err = t.translateMessage("assert");
		boolean test = true;
		
		if(intervalLength < 0) {
			err += "interval_display_size >= 0 ; ";
			test = false;
		}
		
		if(input_double_intervals[0].length != 2) {
			err += t.translateMessage("errColumn") +" ; ";
			test = false;
		}
		
		for(String[] a : input_double_intervals) {
			boolean temp = true;
			for(String e : a) {
				if(!e.matches("[0-9]+\\.?[0-9]*")) {
					err += t.translateMessage("errDouble") +" ; ";
					test = false;
					temp = false;
					break;
				}
			}
			if(!temp)
				break;
		}
		
		
		if(!test)
			throw new IllegalArgumentException(err);
		return test;
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	//primitives
    	input_double_intervals = (String[][])primitives.get("input_double_intervals");
    	intervalLength = (Integer)primitives.get("interval_display_size");
    	showAllBest = (Boolean)primitives.get("show_all_best_intervals");
    	
    	//Properties
    	Font temp;
    	
        code = (SourceCodeProperties)props.getPropertiesByName("code");
        matrix = (MatrixProperties)props.getPropertiesByName("matrix");
        line = (RectProperties)props.getPropertiesByName("line");
        mark = (RectProperties)props.getPropertiesByName("marker");
        text = (TextProperties)props.getPropertiesByName("text");
        numberText = (TextProperties)props.getPropertiesByName("numbers");;
        headline = (TextProperties)props.getPropertiesByName("headline");
        title = (TextProperties)props.getPropertiesByName("title");
        rectangle = (RectProperties)props.getPropertiesByName("rectangle");
        
        hPoint = new PointProperties();
        hPoint.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

		temp = ((Font) headline.getItem(AnimationPropertiesKeys.FONT_PROPERTY).get());
		temp = temp.deriveFont(Font.BOLD, 16);
		headline.set(AnimationPropertiesKeys.FONT_PROPERTY, temp);

		temp = ((Font) title.getItem(AnimationPropertiesKeys.FONT_PROPERTY).get());
		temp = temp.deriveFont(Font.BOLD, 20);
		title.set(AnimationPropertiesKeys.FONT_PROPERTY, temp);
        
        
        //start
        setupVisFrame();
        return l.toString();
    }
    
    public String getName() {
        return t.translateMessage("xtitle");
    }

    public String getAlgorithmName() {
        return t.translateMessage("xtitle");
    }

    public String getAnimationAuthor() {
        return "Lutz Langhorst";
    }

    public String getDescription(){
    	String temp = "";
		Vector<String> text = new Vector<String>();
		loadText(text, "xintro");
		for(String e : text) {
			temp = temp + e + "\n";
		}
		return temp;
    }

    public String getCodeExample(){
        return "private void marzullo(ArrayList<Marzullo_Tuple> table) {\r\n" + 
        		"		int best = 0;\r\n" + 
        		"		int cnt = 0;\r\n" + 
        		"		double bestStart;\r\n" + 
        		"		double bestEnd;\r\n" + 
        		"		int i;\r\n" + 
        		"\r\n" + 
        		"		table.sort(cmp);\r\n" + 
        		"		for(i = 0 ; i < table.size() ; i++) {\r\n" + 
        		"			cnt -= table.get(i).edg();\r\n" + 
        		"			\r\n" + 
        		"			if(cnt > best) {\r\n" + 
        		"				best = cnt;\r\n" + 
        		"				bestStart = table.get(i).pos();\r\n" + 
        		"				bestEnd = table.get(i+1).pos();\r\n" + 
        		"			}\r\n" + 
        		"		}"+
        		"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return genLocale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    
  	public static void main(String[] args) {
  		// Animal.startAnimationFromAnimalScriptCode(l.toString());
  		Generator generator = new Marzullo_algorithm(Locale.GERMANY); // GERMANY , US
  		Animal.startGeneratorWindow(generator);
  	}

}