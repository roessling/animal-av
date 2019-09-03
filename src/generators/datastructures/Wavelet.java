/*
 * AlgoWave.java
 * Ilhan Simsiki, Christof Bienkowski, 2018 for the Animal project at TU Darmstadt.
 */
package generators.datastructures;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.ArrayProperties;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;

@SuppressWarnings("unused")
public class Wavelet implements ValidatingGenerator {
    
	private static Language lang;
	private Locale language;
	private Translator translator;
	private String ALGORITHM_NAME;
    private int indexAccess;
    private String Text;
	public LinkedList<Text> textList = new LinkedList<Text>();
	public static LinkedList<String[]> inputXY = new LinkedList<String[]>();
	public static LinkedList<Integer> xcord = new LinkedList<Integer>();
	public static LinkedList<Integer> ycord = new LinkedList<Integer>();
	public static LinkedList<Integer> nxcord = new LinkedList<Integer>();
	public static LinkedList<Integer> nycord = new LinkedList<Integer>();
    private List<Primitive> notHideTitle;
    private static int high;
    private static int low;
    public Rect r0,r3,r4;
    public SourceCodeProperties sourceCode;
    public ArrayMarkerProperties arrayMarker;
    public static ArrayProperties aProps;
    private ArrayProperties StringArray;
	private String letter;
	private Integer indexRang;
	private String position;
	private int counter;
    
    public final static Timing  defaultDurationS = new MsTiming(0);
    public final static Timing  defaultDurationE = new MsTiming(1000);
    public final static Timing  defaultDurationEE = new MsTiming(1800);

    public Wavelet(Locale l) {
    	language = l;
		translator = new Translator("resources/Wavelet", language);
		ALGORITHM_NAME = translator.translateMessage("algorithm_name");	
      }
    
    public void init(){
        lang = new AnimalScript(ALGORITHM_NAME, "Ilhan Simsiki, Christof Bienkowski", 800, 600);
        lang.setStepMode(true);
    }
    
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
		boolean validation = true;
		indexAccess = (Integer)primitives.get("indexAccess");
		
        Text = (String)primitives.get("text");
        
        letter = (String)primitives.get("letter");
        
        indexRang = (Integer)primitives.get("indexRang");
        
        if(Text.length() >=21) {
        	validation = false;
			throw new IllegalArgumentException(this.translator.translateMessage("vaild1")+" "+this.translator.translateMessage("vaild4")+Text.length());
        }
        
        if(indexAccess > Text.length()) {
        	validation = false;
			throw new IllegalArgumentException(this.translator.translateMessage("vaild3")+" "+this.translator.translateMessage("vaild4")+Text.length());
        }
        
        if(indexRang > Text.length()) {
        	validation = false;
			throw new IllegalArgumentException(this.translator.translateMessage("vaild3")+" "+this.translator.translateMessage("vaild4")+Text.length());
        }
        
        if(Text.contains(letter) == false){
        	validation = false;
        	throw new IllegalArgumentException(this.translator.translateMessage("vaild5")+" "+this.translator.translateMessage("vaild4")+Text.length());
        }
        
		return validation;
	}
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        
    	inputXY.clear();
    	xcord.clear();
    	ycord.clear();
    	nxcord.clear();
    	nycord.clear();
    	
        StringArray = (ArrayProperties)props.getPropertiesByName("StringArray");
        
        arrayMarker = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarker");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        indexAccess = (Integer)primitives.get("indexAccess");
        Text = (String)primitives.get("text");
        
        letter = (String)primitives.get("letter");
        indexRang = (Integer)primitives.get("indexRang");

        Text = Text.toUpperCase();
        
        intro();
        int size = Text.length();
        int ai = indexAccess;
        high = Integer.MIN_VALUE;
        		
        for (int i =0; i < size; i++){
    		high = Math.max(high, getNum(Text.charAt(i)));
    		low = Math.min(low, getNum(Text.charAt(i)));	
    		}
        
       TextProperties tProps = new TextProperties();
 	   tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
 	   textList.add(lang.newText(new Coordinates(20,60), this.translator.translateMessage("step1"), "step1", null, tProps));
 	   
 	   /////////////////////////    Pseudo Code   //////////////////////////////////////////////////	 
 	   SourceCode sc = displaySourceCode(800, sourceCode);
 	   sc.highlight(0, 0, false);
 	   lang.nextStep("Tree");
    	
	   /////////////////////////    Wavelet   //////////////////////////////////////////////////
	   wavlet_tree(Text,low+1,high, size*13,120);
	   lang.hideAllPrimitivesExcept(notHideTitle);
	   
	   /////////////////////////    Access   //////////////////////////////////////////////////
	   textList.add(lang.newText(new Coordinates(20,60), this.translator.translateMessage("step2"), "step2", null, tProps));
	   int size2 = 800;
  	   if(size < 12) {
  		 size2= size2-200;
  	   } 
	   
	   SourceCode sc1 = displaySourceCode(size2, sourceCode);
	   sc1.toggleHighlight(0, 0, false, 2, 0);
	   lang.nextStep("Access");
       access(Text,ai, tProps, arrayMarker, aProps);
       lang.nextStep();
       
	   /////////////////////////    Rang   //////////////////////////////////////////////////
       textList.add(lang.newText(new Coordinates(20,290), this.translator.translateMessage("step3"), "step3", null, tProps));
       sc1.toggleHighlight(2, 0, false, 4, 0);
       lang.nextStep("Rang");
       rang(Text, letter, indexRang, tProps, arrayMarker, aProps);
       lang.nextStep();
	   
	   /////////////////////////    Zusammenfassung   //////////////////////////////////////////////////
  	   tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
  	   Text con = lang.newText(new Coordinates(450,180), this.translator.translateMessage("conclusion"), "conclusion", null, tProps);
  	   con.changeColor(null, Color.MAGENTA, null, null);
  	   con.moveBy("translate", -200, 0, defaultDurationS, defaultDurationE);
  	   lang.hideAllPrimitivesExcept(con);
  	   int xx = 0;
  	   if(size < 12) {
  		  xx= 200;
  	   }
  	   for(int i = 0; i < inputXY.size();i++) {

		   	String[] dex = inputXY.get(i);
		   	int x = xcord.get(i)+xx;
		   	int y = ycord.get(i);
		   	
		   	int nx = nxcord.get(i)+xx;
		   	int ny = nycord.get(i);
	    	ArrayProperties aProps = new ArrayProperties();
	    	aProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
          aProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
	    	
        StringArray imex  = lang.newStringArray(new Coordinates(x, y), dex, "ma", null, aProps);
		   
	    	Node[] np01= {new Coordinates(nx,ny), new Coordinates(x,y)}; 
	        Polyline p01 = lang.newPolyline(np01, "p01", null);
		   
	   }
  	   
  	    conclusionTitle();
  	    lang.nextStep("Conclusion");
  	    textList.add(lang.newText(new Coordinates(850,250), this.translator.translateMessage("conclusion1"), "conclusion1", null));
  	    Text input = lang.newText(new Coordinates(960,250), Text, "input", null);
  	    input.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
  	    textList.add(lang.newText(new Coordinates(850,270), this.translator.translateMessage("conclusion2"), "conclusion2", null));
  	    lang.nextStep();
  	    
  	    int x=0;
  	    if(language.getLanguage() == "en") {
	    	 x=1030;
	    }else {
	    	x = 1100;
	    }
  	    textList.add(lang.newText(new Coordinates(850,310), this.translator.translateMessage("conclusion7"), "conclusion7", null));
  	    lang.nextStep();
  	    
  	    textList.add(lang.newText(new Coordinates(850,350), this.translator.translateMessage("conclusion3"), "conclusion3", null, tProps));
  	    Text index = lang.newText(new Coordinates(905,350), "("+ this.indexAccess +")", "input", null);
  	    index.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
  	    
  	    textList.add(lang.newText(new Coordinates(850,370), this.translator.translateMessage("conclusion4"), "conclusion4", null));
	    Text position = lang.newText(new Coordinates(x,370), "" +this.position+"", "position", null);
	    position.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
	    lang.nextStep();
	    
	    textList.add(lang.newText(new Coordinates(850,420), this.translator.translateMessage("conclusion5"), "conclusion5", null, tProps));
  	    Text trang = lang.newText(new Coordinates(890,420), "("+ this.letter +", " + this.indexRang + ")", "input", null);
  	    trang.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
  	    
  	    textList.add(lang.newText(new Coordinates(850,440), this.translator.translateMessage("conclusion6"), "conclusion6", null));
	    Text rang = lang.newText(new Coordinates(x,440), "" +this.counter+"", "input", null);
	    rang.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
  	    
        return lang.toString();
    }
    
/**
  * This method displays the first page of our Animation and includes the Algorithmname and Description.
  * 
  */
    public void intro() {
    	RectProperties rProps = new RectProperties();
 		rProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
 		rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.cyan);
 		rProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
 		Rect r1 = lang.newRect(new Coordinates(10,0), new Coordinates(305,35), "sfd", null, rProps);
	   
 		TextProperties tProps = new TextProperties();
  		tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
  		TextProperties tProps1 = new TextProperties();
  		tProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
  		textList.add(lang.newText(new Coordinates(85,10), this.translator.translateMessage("algorithm_name"), "algorithm_name", null, tProps));
  		textList.add(lang.newText(new Coordinates(20,50), this.translator.translateMessage("approach"), "approach", null, tProps1));
  		textList.add(lang.newText(new Coordinates(20,70), this.translator.translateMessage("description"), "description", null));
  		textList.add(lang.newText(new Coordinates(20,90), this.translator.translateMessage("description1"), "description1", null));
  		textList.add(lang.newText(new Coordinates(20,110), this.translator.translateMessage("description2"), "description2", null));
  		textList.add(lang.newText(new Coordinates(60,170), this.translator.translateMessage("description4"), "description4", null));
  		textList.add(lang.newText(new Coordinates(60,200), this.translator.translateMessage("description5"), "description5", null));
  		textList.add(lang.newText(new Coordinates(60,230), this.translator.translateMessage("description6"), "description6", null));
  		textList.add(lang.newText(new Coordinates(20,280), this.translator.translateMessage("description7"), "description7", null));
  		textList.add(lang.newText(new Coordinates(20,300), this.translator.translateMessage("description8"), "description8", null));
  		
       	lang.nextStep();
       	notHideTitle = new LinkedList<Primitive>();
       	notHideTitle.add(textList.get(0));
       	notHideTitle.add(r1);
       	lang.hideAllPrimitivesExcept(notHideTitle);  
    }
              
/**
  * This method gets an arbitrarily long string and initiates the initial step 
  * to divide the tree.
  * 
  * @param text, input String
  * @param l, value smallest letter
  * @param h, value biggest letter
  * @param x, start X coordinate
  * @param y, start Y coordinate
  * 
  */ 
    public static void wavlet_tree(String text, int l, int h, int x, int y){
    	ArrayList<String> input = new ArrayList<String>();
	
    	for(int i = 0; i<text.length();i++){
    		input.add(Character.toString(text.charAt(i)));
    	}

    	int mid = (l+h)/2;
    	Math.round(mid);
    		
    	String[] dex = input.toArray(new String[input.size()]);
    	inputXY.add(dex);
    	aProps = new ArrayProperties();
    	aProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	StringArray imex  = lang.newStringArray(new Coordinates(x, y), dex, "ma", null, aProps);
    	xcord.add(x);
    	ycord.add(y+120);
    	
    	nxcord.add(x);
        nycord.add(y+120);
    	imex.setHighlightBorderColor(0, 0, Color.black, null, null);

    	lang.nextStep();
    	    
    	ArrayList<String> left = new ArrayList<String>();
    	ArrayList<String> right = new ArrayList<String>();
    		
    	int midl = 0;
    	for(int i = 0; i<text.length();i++){
    		if(getNum(text.charAt(i)) < mid){
    			midl = Math.max(midl, getNum(text.charAt(i)));
    			left.add(Character.toString(text.charAt(i)));
    		}else{
    			right.add(Character.toString(text.charAt(i)));
    			}
    	}
    		
    	String[] dexl = left.toArray(new String[left.size()]);
    	inputXY.add(dexl);
    	StringArray imexl  = lang.newStringArray(new Coordinates(x-(5*input.size()),y+60), dexl, "ma", null, aProps);
    	xcord.add(x-(5*input.size()));
    	ycord.add(y+180);
    	imexl.setHighlightBorderColor(0, 0, Color.black, null, null);
   
    	Node[] np01= {new Coordinates(x+((18*input.size())/2),y+30), new Coordinates(x-(5*input.size()),y+60)}; 
        Polyline p01 = lang.newPolyline(np01, "p01", null);
        
        nxcord.add(x+((18*input.size())/2));
        nycord.add(y+150);
        
        lang.nextStep();
    	    
    	String[] dexr = right.toArray(new String[right.size()]);
    	inputXY.add(dexr);
    	StringArray imexr  = lang.newStringArray(new Coordinates(x+(21*input.size()-right.size()), y+60), dexr, "ma", null, aProps);
    	xcord.add(x+(21*input.size()));
    	ycord.add(y+180);
    	imexr.setHighlightBorderColor(0, 0, Color.black, null, null);

    	Node[] np02= {new Coordinates(x+((18*input.size())/2),y+30), new Coordinates(x+((21*input.size())-right.size()),y+60)}; 
        Polyline p02 = lang.newPolyline(np02, "p02", null);
        
        nxcord.add(x+((18*input.size())/2));
        nycord.add(y+150);
        
    	lang.nextStep();
    		
    	int lc = 0;
    	int rc = 0;
    		
    	for(int i = 0; i < left.size()-1; i++){
    		if(left.get(i).equals(left.get(i+1))){
    			lc++;
    		}
    	}
    		
    	for(int i = 0; i < right.size()-1; i++){
    		if(right.get(i).equals(right.get(i+1))){
    			rc++;
    		}
    	}

    	if(lc == left.size()-1){
    		removedup(left);
    	}

    	if(rc == right.size()-1){
    		removedup(right);	
    	}
    		
    	wavlet_tree_l(stringmaker(left), l, midl,x-(5*left.size()),y+60, aProps);
    	wavlet_tree_r(stringmaker(right),mid,h,x+(22*input.size()),y+60, aProps);
    	lang.nextStep();
    }
    
/**
  * This recursive method gets the right partString and performs the splitting 
  * recursively until a leaf account is reached.
  * 
  * @param text, input String
  * @param l, value smallest letter
  * @param h, value biggest letter
  * @param x, start X coordinate
  * @param y, start Y coordinate
  * 
  */
  private static void wavlet_tree_r(String text, int l, int h, int x, int y, ArrayProperties aProps) {
    	int mid = (l+h)/2;
    	Math.round(mid);
    		
    	ArrayList<String> left = new ArrayList<String>();
    	ArrayList<String> right = new ArrayList<String>();
    	
    	for(int i = 0; i<text.length();i++){
    		if(getNum(text.charAt(i)) < mid){
    			left.add(Character.toString(text.charAt(i)));
    		} else{
    			right.add(Character.toString(text.charAt(i)));
    		}
    	}
    		
    	int lc = 0;
    	int rc = 0;
    		
    	for(int i = 0; i < left.size()-1; i++){
    		if(left.get(i).equals(left.get(i+1))){
    			lc++;
    		}
    	}
    		
    	for(int i = 0; i < right.size()-1; i++){
    		if(right.get(i).equals(right.get(i+1))){
    			rc++;
    		}
    	}

    	if(lc == left.size()-1){
    		removedup(left);	
    	}

    	if(rc == right.size()-1){
    		removedup(right);	
    	}
    		

    	if(!left.isEmpty()){
    		String[] dex = left.toArray(new String[left.size()]);
    		inputXY.add(dex);
    		StringArray imex  = lang.newStringArray(new Coordinates(x-(text.length()/2),y+60), dex, "ma", null, aProps);
    		xcord.add(x-(text.length()/2));
    		ycord.add(y+180);
    		imex.setHighlightBorderColor(0, 0, Color.black, null, null);
    	   
    		Node[] np01= {new Coordinates(x+((20*text.length())/2),y+30), new Coordinates(x-(text.length()/2),y+60)}; 
    	    Polyline p01 = lang.newPolyline(np01, "p01", null);
    	    
    	    nxcord.add(x+((20*text.length())/2));
            nycord.add(y+150);
    	    
    		lang.nextStep();
    	}

    	if(!right.isEmpty()){
    		String[] dex = right.toArray(new String[right.size()]);
    		inputXY.add(dex);
    		StringArray imex  = lang.newStringArray(new Coordinates(x+((21*text.length())-right.size()),y+60), dex, "ma", null, aProps);
    		xcord.add(x+((21*text.length())-right.size()));
    		ycord.add(y+180);
    		imex.setHighlightBorderColor(0, 0, Color.black, null, null);
    	
    		Node[] np02= {new Coordinates(x+((20*text.length())/2),y+30), new Coordinates(x+((21*text.length())-right.size()),y+60)}; 
    	    Polyline p02 = lang.newPolyline(np02, "p02", null);
    	    
    	    nxcord.add(x+((20*text.length())/2));
            nycord.add(y+150);
    		lang.nextStep();
    	}
    		
    	y = y +60;
    		
    	if(l != mid && !left.isEmpty() && left.size() > 1){
    		wavlet_tree_r(stringmaker(left), l, mid,x-(5*left.size()),y, aProps);
    	}
    		
    	if(h != mid && !right.isEmpty() && right.size() > 1){
    		wavlet_tree_r(stringmaker(right), mid+1, h,x+(22*text.length()),y, aProps);
    	}
    }
    
    /**
     * This recursive method gets the left partString and performs the splitting 
     * recursively until a leaf account is reached.
     * @param text, input String
     * @param l, value smallest letter
     * @param h, value biggest letter
     * @param x, start X coordinate
     * @param y, start Y coordinate
     * 
     */
    
    private static void wavlet_tree_l(String text, int l, int h, int x, int y, ArrayProperties aProps) {

    	int mid = (l+h)/2;
    	Math.round(mid);
    		
    	ArrayList<String> left = new ArrayList<String>();
    	ArrayList<String> right = new ArrayList<String>();
    		
    	for(int i = 0; i<text.length();i++){
    		if(getNum(text.charAt(i)) < mid){
    			left.add(Character.toString(text.charAt(i)));
    		}else{
    			right.add(Character.toString(text.charAt(i)));
    		}
    	}
    		
    	int lc = 0;
    	int rc = 0;
    		
    	for(int i = 0; i < left.size()-1; i++){
    		if(left.get(i).equals(left.get(i+1))){
    			lc++;
    		}
    	}
    		
    	for(int i = 0; i < right.size()-1; i++){
    		if(right.get(i).equals(right.get(i+1))){
    			rc++;
    		}
    	}

    	if(lc == left.size()-1){
    		removedup(left);	
    	}

    	if(rc == right.size()-1){
    		removedup(right);	
    	}

    	if(!left.isEmpty()){
    		String[] dex = left.toArray(new String[left.size()]);
    		inputXY.add(dex);
    		StringArray imex  = lang.newStringArray(new Coordinates(x-(text.length()/2),y+60), dex, "ma", null, aProps);
    		xcord.add(x-(text.length()/2));
    		ycord.add(y+180);
    		imex.setHighlightBorderColor(0, 0, Color.black, null, null);
  
    		Node[] np01= {new Coordinates(x+((20*text.length())/2),y+30), new Coordinates(x-(text.length()/2),y+60)}; 
    	    Polyline p01 = lang.newPolyline(np01, "p01", null);
    	    
    	    nxcord.add(x+((20*text.length())/2));
            nycord.add(y+150);
    		lang.nextStep();    
    	}

    	if(!right.isEmpty()){
    		String[] dex = right.toArray(new String[right.size()]);
    		inputXY.add(dex);
    		StringArray imex  = lang.newStringArray(new Coordinates(x+((21*text.length())-right.size()),y+60), dex, "ma", null, aProps);
    		xcord.add(x+((21*text.length())-right.size()));
    		ycord.add(y+180);
    		imex.setHighlightBorderColor(0, 0, Color.black, null, null);

    		Node[] np02= {new Coordinates(x+((20*text.length())/2),y+30), new Coordinates(x+((21*text.length())-right.size()),y+60)}; 
    	    Polyline p02 = lang.newPolyline(np02, "p02", null);
    	    
    	    nxcord.add(x+((20*text.length())/2));
            nycord.add(y+150);
    		lang.nextStep();
    	}
    	y = y +60;
    	
    	if(l != mid && !left.isEmpty() && left.size() > 1){
    		wavlet_tree_l(stringmaker(left), l, mid,x-(5*left.size()),y, aProps);
    	}
    		
    	if(h != mid && !right.isEmpty() && right.size() > 1){
    		wavlet_tree_l(stringmaker(right), mid+1, h,x+(22*text.length()),y, aProps);
    	}
    }
    	
    	
/**
  * This method gets a letter and returns the Value from the letter. Example: A -> 1, C -> 3
  * 
  * @param letter, input letter
  * @return i, int value of the letter
  * 
  */
    private static int getNum(char letter) {		
    	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";		
    	for(int i = 0; i <= 26; i++){
    		if(letter == alphabet.charAt(i)){
    			return i+1;
    		}
    	}
    	return 0;
    }
    	
/**
  * This method will removes the dublicated entry in the ArrayList of strings
  * 
  * @param arr - ArrayList of strings
  * @return ArrayList of strings
  *         
  */
    private static ArrayList<String> removedup(ArrayList<String> arr){
    	Set<String> hs = new HashSet<String>();	
    	hs.addAll(arr);
    	arr.clear();
    	arr.addAll(hs);
    	hs.clear();
    		
    	return arr;	
    }
    	
/**
  * This method will transform a ArrayList<String> to an String value
  * 
  * @param al - ArrayList of Strings
  * @return String value 
  *         
  */
    private static String stringmaker(ArrayList<String> al) {
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < al.size(); i++){
    		sb.append(al.get(i));
    	}
    	return sb.toString();
    }
    
/**
  * This method create the second SourceCodeProperties and displays the SoruceCode and return the SoruceCode
  * 
  * @param size - integer for position of SoruceCode 
  * @return sc - SoruceCode
  * 
  */	
    public SourceCode displaySourceCode(int x, SourceCodeProperties scProps) { 
    	RectProperties rProps = new RectProperties();
        rProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray); 
        rProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        r4 = lang.newRect(new Coordinates(x,60), new Coordinates(x+400,195), "r4", null, rProps);
     		
     	SourceCodeProperties scProps1 = new SourceCodeProperties();
        scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
        SourceCode sc1 = lang.newSourceCode(new Coordinates(x+30, 65), "sourceCode", null, scProps1);
        sc1.addCodeLine(this.translator.translateMessage("line1"), null, 0, null);
         
        SourceCode sc = lang.newSourceCode(new Coordinates(x+50, 90), "sourceCode", null, scProps);  													
     	sc.addCodeLine(this.translator.translateMessage("line2"), null, 2, null);
     	sc.addCodeLine("", null, 1, null);
        sc.addCodeLine(this.translator.translateMessage("line3"), null, 2, null);
     	sc.addCodeLine("", null, 1, null);	
     	sc.addCodeLine(this.translator.translateMessage("line4"), null, 2, null); 										 								
     	lang.nextStep();

     	return sc;   
     }
     	
/**
  * this method gets a string and an index, counts up to the position index in the string 
  * and returns the letter in the position. 
  * 
  * @param text - string value
  * @param index - integer
  *         
  */
    public void access(String text, int index, TextProperties tProps, ArrayMarkerProperties currentP, ArrayProperties aProps){
    	position = null;
        String bool = "FALSE";
    
        ArrayList<String> input = new ArrayList<String>();         
                        
        for(int i = 0; i<text.length();i++){
        	input.add(Character.toString(text.charAt(i)));
        }
                        
        String[] dex = input.toArray(new String[input.size()]);
        StringArray imex  = lang.newStringArray(new Coordinates(100, 120), dex, "ma", null, aProps);
        aProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
        lang.nextStep();
                            
        String a2 = "access(" + index + ")";
        Text at2 = lang.newText(new Coordinates (20,180), a2, "tex8", null);
        at2.setFont( new Font("SansSerif", Font.BOLD, 12), null, null);          
        ArrayMarker pointer = lang.newArrayMarker(imex, 0, "pointer", null, currentP);
                     
        for(int i = 0; i <= index; i++){ 
        	imex.setBorderColor(i, Color.RED, null, null);
            String a3 = "i = " + i + "";
            Text at3 = lang.newText(new Coordinates (20,200), a3, "tex8", null);
            pointer.move(i, null, null);
                      
            if(i == index){
            	bool = "TRUE";
            	imex.setHighlightFillColor(i, Color.YELLOW, null, null);
            	imex.highlightCell(i, null, null);
                position = imex.getData(i);
            }
            String a4 = "i = index? -> " + bool + "";
            Text at4 = lang.newText(new Coordinates (20,220), a4, "tex8", null);
            lang.nextStep();
            imex.setBorderColor(i, Color.BLACK, null, null);
            at3.hide();
            at4.hide();         
         }
         
        textList.add(lang.newText(new Coordinates(20,220), this.translator.translateMessage("line6"), "line6", null));
        lang.newText(new Coordinates (270,220), position, "tex8", null, tProps);
                       
    }
    
/**
  * this method is given a string, a letter and an index, counts up to the position index in the string 
  * and then returns the value of all occurrences of the input letter
  * 
  * @param text - string value
  * @param index - integer
  *         
  */
    public void rang(String text, String letter, int index, TextProperties tProps, ArrayMarkerProperties currentP, ArrayProperties aProps){
        counter = 0;
        String bool = "FALSE";

        ArrayList<String> input = new ArrayList<String>();         
             
        for(int i = 0; i<text.length();i++){
                       input.add(Character.toString(text.charAt(i)));
        }
        
        String[] dex = input.toArray(new String[input.size()]);
        StringArray imex  = lang.newStringArray(new Coordinates(100, 350), dex, "ma", null, aProps);
        aProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
        lang.nextStep();
                 
        String a2 = "rang(" + letter + ", " + index + ")";
        Text at2 = lang.newText(new Coordinates (20,410), a2, "tex8", null);
        at2.setFont( new Font("SansSerif", Font.BOLD, 12), null, null);
        ArrayMarker pointer = lang.newArrayMarker(imex, 0, "pointer", null, currentP);
        
        for(int i = 0; i <= index; i++){ 
            imex.setBorderColor(i, Color.RED, null, null);
            pointer.move(i, null, null);
            String a3 = "i = " + i + "";
            Text at3 = lang.newText(new Coordinates (20,430), a3, "tex8", null);
            if(imex.getData(i).equals(letter)){
                        counter++;
                        bool = "TRUE";
                        imex.setHighlightFillColor(i, Color.YELLOW, null, null);
                        imex.highlightCell(i, null, null);
            }
            String a4 = "i = index? -> " + bool + "";
            Text at4 = lang.newText(new Coordinates (20,450), a4, "tex8", null);
            lang.nextStep();
            imex.setBorderColor(i, Color.BLACK, null, null);
            at3.hide();
            at4.hide();
            bool = "FALSE";
          }
     
          textList.add(lang.newText(new Coordinates(20,450), this.translator.translateMessage("line5"), "line5", null));
          int x=0;
    	  if(language.getLanguage() == "en") {
    		  lang.newText(new Coordinates (250,450), Integer.toString(counter), "tex8", null, tProps);    
  	      }else {
  	    	  lang.newText(new Coordinates (230,450), Integer.toString(counter), "tex8", null, tProps);  
  	      }
    }
   
/**
  * This method will draw a chessboard and the title of this Animation. 
  *         
  */
    public void conclusionTitle() {
    	RectProperties rProps = new RectProperties();
   	   	rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
   	   	rProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
         
   	   	int x= 0;
        int y= 0;
        boolean a = true;
             
        for(int k = 0; k< 93; k++) {
        	y = 0;
            for(int i=0; i<5; i++) {
            	if(a) {
            		r0 = lang.newRect(new Coordinates(170+x,70+y), new Coordinates(180+x,80+y), "chessboard", null, rProps);
                    y = y + 20;
                    if(i == 4)
                    	a = false;    
                    } else {
                    	r0 = lang.newRect(new Coordinates(170+x,80+y), new Coordinates(180+x,90+y), "chessboard", null, rProps);
                        y = y + 20;
                        if(i == 4)
                        	a = true; 
                    }	 
             } 
             x = x + 10;
         }
         lang.nextStep();
         
         rProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
         rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
         r3 = lang.newRect(new Coordinates(390,90), new Coordinates(900,150), "chesstitle", null, rProps);
             
         String con = "Wavelet Tree";
         Text con1 = lang.newText(new Coordinates(520,120), con, "con1", null);
         con1.setFont(new Font("SansSerif", Font.BOLD, 32), defaultDurationS, defaultDurationE);
         con1.changeColor(null, Color.MAGENTA, defaultDurationS, defaultDurationEE);
    }
    
    public String getName() {
        return "Wavelet Tree";
    }

    public String getAlgorithmName() {
        return "Wavlet Tree";
    }

    public String getAnimationAuthor() {
        return "Ilhan Simsiki, Christof Bienkowski";
    }

    public String getDescription(){
    	
    	return this.translator.translateMessage("description")
    			+"\n"
    			+this.translator.translateMessage("description1")
    			+"\n"
    			+this.translator.translateMessage("description2")
    			+"\n"
    			+"\n"
    			+this.translator.translateMessage("description10")
    			+this.translator.translateMessage("description11")
    			+"\n"
    			+"\n"
    			+this.translator.translateMessage("description12")
    			+"\n"
    			+"       "+this.translator.translateMessage("description13")
    			+"\n"
    			+"       "+this.translator.translateMessage("description14")
    			+"\n"
    			+"       "+this.translator.translateMessage("description15");
    }

    public String getCodeExample(){
        return "public void wavelet(String text, int index) {"
        		+"\n"
        		+"\n"
        		+ "        "+"wavelettree(text);"
        		+"\n"
        		+"\n"
        		+ "        "+"while (i < text.length())"
        		+"\n"
        		+ "              "+"access(text, index);"
        		+"\n"
        		+"\n"
        		+ "        "+"while (i < text.length())"
        		+"\n"
        		+ "              "+"rang(text, index);"
        		+"\n"
        		+"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return language;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}