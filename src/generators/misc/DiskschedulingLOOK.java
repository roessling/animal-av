/*
 * look.java
 * Jana Vatter, Rene Hammann, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

import java.util.Hashtable;

import translator.Translator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import animal.main.Animal;

public class DiskschedulingLOOK implements ValidatingGenerator {
    private Language lang;
    private int start;
    private int[] queue;
    private Translator translator;

    public void init(){
    	lang = new AnimalScript("Disk Scheduling: LOOK", "Jana Vatter, Rene Hammann", 800, 600);
    	lang.setStepMode(true);
    }
 
    //   new look("resources/DiskschedulingLOOK", Locale.GERMANY);
    //or new look("resources/DiskschedulingLOOK", Locale.US);
    public DiskschedulingLOOK(String spe, Locale loc){
        translator = new Translator(spe, loc);
    	
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        start = (Integer)primitives.get("start");
        queue = (int[])primitives.get("queue");
        this.schedule(queue, start);
        
        return lang.toString();
    }
    
    @Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives) throws IllegalArgumentException
	{
    	queue = (int[])primitives.get("queue");
        start = (Integer)primitives.get("start");
        if(!(0<=start && start<=199))
        {
        	throw new IllegalArgumentException("Please use only values betweeen 0 and 199 for the start.");
        }
        for(int i=0;i < queue.length;i++)
        {
        	if(queue[i]<0 || queue[i]>199)
        		throw new IllegalArgumentException("Please use only values betweeen 0 and 199 for the "+ String.valueOf(i) +"th Element in the queue.");
        }
        return true;
		
	}

    public String getName() {
        return "Disk Scheduling: LOOK";
    }

    public String getAlgorithmName() {
        return "LOOK";
    }

    public String getAnimationAuthor() {
        return "Jana Vatter, Rene Hammann";
    }

    public String getDescription(){
        return translator.translateMessage("overDesc");
    }

    public String getCodeExample(){
        return translator.translateMessage("overCode");
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        if(translator.getCurrentLocale()==Locale.GERMANY)
        	return Locale.GERMANY;
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public static void main(String[] args){
    	Generator generator = new DiskschedulingLOOK("/resources/DiskschedulingLOOK", Locale.GERMANY);
    	Animal.startGeneratorWindow(generator);
    }
    
    /**
	 * schedule the int array passed in
	 * 
	 * @param a the array to be scheduled
	 * @param s the position where the head starts
	 * 
	 */
	public void schedule(int[] a, int s){
		
		
		// Create SourceCode: coordinates, name, display options,
	    // default properties

	    // first, set the visual properties for the source code
	    SourceCodeProperties scProps = new SourceCodeProperties();
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
	        new Font("SansSerif", Font.BOLD, 24));

	    // now, create the source code entity
	    SourceCode sc = lang.newSourceCode(new Coordinates(20, 30), "header",
	        null, scProps);

	    // Add the lines to the SourceCode object.
	    // Line, name, indentation, display dealy
	    sc.addCodeLine(translator.translateMessage("title"), null, 0, null);
	    
	    RectProperties rectProps = new RectProperties();
	    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
	    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    
	    
	    Rect rect = lang.newRect(new Offset(5, 5, "header", AnimalScript.DIRECTION_SE),new Offset(-5, -5, "header", 
	    		AnimalScript.DIRECTION_NW), "rect", null, rectProps);
	    
	    lang.nextStep();
	    
	    RectProperties rectProps2 = new RectProperties();
	    rectProps2.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	    rectProps2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    rectProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    rectProps2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    
		// Description
	    SourceCodeProperties descrProps = new SourceCodeProperties();
	    descrProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
	    descrProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	    
	    SourceCode desc = lang.newSourceCode(new Offset(0, 30, "header", AnimalScript.DIRECTION_SW), "description", 
	    		null, descrProps);
	    desc.addCodeLine(translator.translateMessage("descheader"), null, 0, null);
	    desc.addCodeLine(translator.translateMessage("desc1"), null, 0, null);
	    desc.addCodeLine(translator.translateMessage("desc2"), null, 0, null);
	    desc.addCodeLine(translator.translateMessage("desc3"), null, 0, null);
	    
	    Rect descrect = lang.newRect(new Offset(5, 5, "description", AnimalScript.DIRECTION_SE),new Offset(-5, -5, "description", 
	    		AnimalScript.DIRECTION_NW), "rectdesc", null, rectProps2);
	    
	    lang.nextStep("Description");
	    
	    descrect.hide();
	    
	    // Pseudocode
	    SourceCode code = lang.newSourceCode(new Offset(0, 30, "description", AnimalScript.DIRECTION_SW),"list", 
	    		null, descrProps);
	    code.addCodeLine(translator.translateMessage("codeheader"), null, 0, null);
	    code.addCodeLine(translator.translateMessage("code1"), null, 0, null);
	    code.addCodeLine(translator.translateMessage("code2"), null, 0, null);
	    code.addCodeLine(translator.translateMessage("code3"), null, 0, null);
	    code.addCodeLine(translator.translateMessage("code4"), null, 0, null);
	    code.addCodeLine(translator.translateMessage("code5"), null, 0, null);
	    
	    Rect coderect = lang.newRect(new Offset(5, 5, "list", AnimalScript.DIRECTION_SE),new Offset(-5, -5, "list", 
	    		AnimalScript.DIRECTION_NW), "rectcode", null, rectProps2);
	    
	    lang.nextStep("Code Example");
	    
	    int dist = 0;
	    
	    TextProperties textProps = new TextProperties();
	    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
	    
	    TextProperties t2Props = new TextProperties();
	    t2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
	    t2Props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
	    //distance
	    Text distance = lang.newText(new Offset(0, 30, "list", AnimalScript.DIRECTION_SW), 
	    		translator.translateMessage("arr1") + String.valueOf(dist), "dist", null, t2Props);
	    
	    //Head starts at ...
	    lang.newText(new Offset(180, 0, "description", AnimalScript.DIRECTION_NE), 
	    		translator.translateMessage("arr2") + String.valueOf(s), "head", null, textProps);
	    
	    //Requests
	    lang.newText(new Offset(0, 60, "head", AnimalScript.DIRECTION_NW), translator.translateMessage("arr3"), "requests", null, textProps);
	    
	    //queue
	    lang.newText(new Offset(0, 80, "requests", AnimalScript.DIRECTION_NW),translator.translateMessage("arr4"), "queue", null, textProps);
	    
		// Create Array: coordinates, data, name, display options,
	    // default properties

	    // first, set the visual properties (somewhat similar to CSS)
	    ArrayProperties arrayProps = new ArrayProperties();
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
	        Color.YELLOW);

	    // now, create the IntArray object, linked to the properties
	    IntArray ia = lang.newIntArray(new Offset(20, 0, "requests", AnimalScript.DIRECTION_NE), a, "intArray",
	        null, arrayProps);

	    // create the box where to draw the arrows
	    RectProperties boxProps = new RectProperties();
	    boxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	    boxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
	    boxProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
	    boxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    
	    lang.newRect(new Offset(0, 100, "queue", AnimalScript.DIRECTION_SW),new Offset(600, a.length*40+180, "queue", 
	    		AnimalScript.DIRECTION_SW), "box", null, boxProps);
	    
	    Node[] na = {new Offset(0, 0, "box", AnimalScript.DIRECTION_NW), new Offset(0, 0, "box", AnimalScript.DIRECTION_NE)};
	    lang.newPolyline(na, "line", null);
	    
	    Node[] n0 = {new Offset(0, -10, "line", AnimalScript.DIRECTION_SW), 
    			new Offset(0, a.length*40+80, "line", AnimalScript.DIRECTION_SW)};
    	lang.newPolyline(n0, "line0", null);
    	lang.newText(new Offset(-3, -25, "line", AnimalScript.DIRECTION_NW), "0", "number0", null);
	    
    	Node[] n1 = {new Offset(600, -10, "line", AnimalScript.DIRECTION_SW), 
    			new Offset(600, a.length*40+80, "line", AnimalScript.DIRECTION_SW)};
    	lang.newPolyline(n1, "line200", null);
    	lang.newText(new Offset(590, -25, "line", AnimalScript.DIRECTION_NW), "200", "number200", null);
	    
    	RectProperties lineProps = new RectProperties();
    	lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    	
    	// dynamically create the labels for the box and vertical lines
    	for(int i = 10 ; i<199; i+=10){
	    	
    		int pos = i*3;
	    	
    		//short line
    		Node[] node = {new Offset(pos, -10, "line", AnimalScript.DIRECTION_SW), 
    				new Offset(pos, 10, "line", AnimalScript.DIRECTION_SW)};
    		lang.newPolyline(node, "linee" + String.valueOf(i), null);
    		
    		//diagram line
    		lang.newRect(new Offset(pos, 10, "line", AnimalScript.DIRECTION_SW), 
    				new Offset(pos, a.length*40+80, "line", AnimalScript.DIRECTION_SW), "linee" + String.valueOf(i), null, lineProps);
    		if(i%20 == 0){
    			int k;
    			if(i<100)
    			{
    				k = -5;
    			}else
    			{
    				k = -10;
    			}
    				
        		lang.newText(new Offset(k, -15, "linee" + String.valueOf(i), AnimalScript.DIRECTION_N), String.valueOf(i), "number" + String.valueOf(i), null);
    		}
    	}
    	
    	//create horizontal lines
    	for(int i = 40; i<=a.length*40+80; i+=40) {
    		if(i==a.length*40+80){
    			lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    		}
    		lang.newRect(new Offset(0, i, "line", AnimalScript.DIRECTION_NW), 
    				new Offset(600, i, "line", AnimalScript.DIRECTION_NW), "hline" + String.valueOf(i), null, lineProps);	
    	}
	    
    	CircleProperties cProps = new CircleProperties();
    	cProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    	cProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    	
    	//head start circle
	    lang.newCircle(new Offset(s*3, 40, "line", AnimalScript.DIRECTION_NW), 4, "scircle", null, cProps);
	    
	    lang.nextStep("Structure");
	    
	    code.highlight(1, 0, false);
	    
	    lang.nextStep("Sorting");
	    
	    // sort requests increasing from start of head

	    int[] qu = new int[0];
	    int counter = 0;
	    for(int i =0; i<a.length; i++){
	    	if(a[i]>=s){
	    		int[] tmp = new int [qu.length+1];
	    		tmp[counter] = a[i];
	    		System.arraycopy(qu, 0, tmp, 0, qu.length);
	    		qu = tmp;
	    		
	    		ia.highlightElem(i, null, null);
	    		counter ++;
	    	}
	    }
	    
	    Arrays.sort(qu);
	    
	    IntArray queue = lang.newIntArray(new Offset(20, 0, "queue", AnimalScript.DIRECTION_NE), qu, "qArray",
	    		null, arrayProps);
	    
	    lang.nextStep();
	    
	    code.unhighlight(1, 0, false);
	    code.highlight(2, 0, false);
	    
	    lang.nextStep();
	    
	    // sort remaining requests decreasing

	    int counter2 = counter;
	    for(int i =0; i<a.length; i++){
	    	if(a[i]<s){
	    		int[] tmp = new int [qu.length+1];
	    		tmp[counter2] = a[i];
	    		System.arraycopy(qu, 0, tmp, 0, qu.length);
	    		qu = tmp;
	    		

	    		ia.highlightElem(i, null, null);	    		
	    		counter2 ++;
	    	}
	    }
	    
	    Arrays.sort(qu, counter, counter2);
	    
	    // reverse second part of array
	    int size = counter2 - counter;
	    int temp;
	    int j = 0;
	    
	    for(int i = counter; i < counter + size/2; i++){
	    	temp = qu[i];
	    	qu[i] = qu[qu.length-1-j];
	    	qu[qu.length-1-j] = temp;
	    	j++;
	    }
	    
	    queue.hide();
	    
	    IntArray queue2 = lang.newIntArray(new Offset(20, 0, "queue", AnimalScript.DIRECTION_NE), qu, "qArray",
	    		null, arrayProps);
	    
	    lang.nextStep();
	    
    	ArrayMarkerProperties arrayMProps = new ArrayMarkerProperties();
    	arrayMProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, Boolean.TRUE);
    	arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	
    	PolylineProperties plProps = new PolylineProperties();
    	plProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
    	
    	int cnt = 40;
    	Node[] node = null;
    	
    	
        ArrayMarker marker = lang.newArrayMarker(queue2, 0, "", null, arrayMProps);
        queue2.highlightCell(0, null, null);
        code.unhighlight(2, 0, false);
    	code.highlight(3, 0, false);
   
    	
	    
	    for(int i = 0; i<qu.length; i++){
	    	lang.nextStep("Request: "+String.valueOf(qu[i]));	    	
	    	
	    	code.highlight(4, 0, false);
	    	code.unhighlight(3, 0, false);
	    	queue2.highlightElem(i, null, null);
	    	
	    	lang.newCircle(new Offset(qu[i]*3, 40+cnt, "line", AnimalScript.DIRECTION_NW), 4, 
	    			"circle" + String.valueOf(qu[i]), null, cProps);
	    	
	    	if(i>0 & i<qu.length){
	    		node = new Node[]{new Offset(0, 0, "circle" + String.valueOf(qu[i-1]), AnimalScript.DIRECTION_C),
		    			new Offset(0, 0, "circle" + String.valueOf(qu[i]), AnimalScript.DIRECTION_C)};
		    	lang.newPolyline(node,"pl", null, plProps);
		    	dist += Math.abs(qu[i-1]-qu[i]);
	    	}
	    	else if(i == 0){
	    		node = new Node[]{new Offset(0, 0, "scircle", AnimalScript.DIRECTION_C),
		    			new Offset(0, 0, "circle" + String.valueOf(qu[i]), AnimalScript.DIRECTION_C)};
		    	lang.newPolyline(node,"pl", null, plProps);
		    	dist += Math.abs(s-qu[i]);
	    	}
	    	else if(i == qu.length-1){
	    		 
	    	}
	    	
	    	distance.setText(translator.translateMessage("arr1") + String.valueOf(dist), null, null);
	    	
	    	cnt += 40;
	    	
	    	lang.nextStep();
	    	
	    	code.unhighlight(4, 0, false);
	    	code.highlight(5, 0, false);
	    	
	    	lang.nextStep();
	    		
		    if(i<qu.length-1){
		    	code.unhighlight(5, 0 , false);
	    		code.highlight(3, 0, false);
	    		queue2.highlightCell(i+1, null, null);
	    	
	    		queue2.unhighlightCell(i, null, null);
		    	marker.move(i+1, null, null);
	    	}
	    	else {
	    		break;
	    	}	    	
	    	  	
	    	
	    }
	    
	    code.unhighlight(5, 0, false);
	    queue2.unhighlightCell(qu.length-1, null, null);
	    marker.hide();
	    
	    coderect.hide();
	    
	    // short summary of travelled distance
	    SourceCode fin = lang.newSourceCode(new Offset(0, 30, "dist", AnimalScript.DIRECTION_SW), "fin", null, descrProps);
	    fin.addCodeLine(translator.translateMessage("finheader"), null, 0, null);
	    fin.addCodeLine(translator.translateMessage("fin1"), null, 0, null);
	    fin.addCodeLine(translator.translateMessage("fin2") + String.valueOf(dist) + " " + translator.translateMessage("fin3"), null, 0, null);
	    
	    lang.newRect(new Offset(-5, -5, "fin", AnimalScript.DIRECTION_NW), 
				new Offset(5, 5, "fin", AnimalScript.DIRECTION_SE), "rectfin", null, rectProps2);
		
		lang.nextStep("End");

		// ending
		List<Primitive> list = new ArrayList<Primitive>();
		
		list.add(sc);
		list.add(rect);
		
		lang.hideAllPrimitivesExcept(list);
		
		SourceCode sum = lang.newSourceCode(new Offset(0, 30, "header", AnimalScript.DIRECTION_SW), "summary", 
	    		null, descrProps);
		sum.addCodeLine(translator.translateMessage("sumheader"), null, 0, null);
	    sum.addCodeLine(translator.translateMessage("sum1"), null, 0, null);
	    sum.addCodeLine(translator.translateMessage("sum2"), null, 0, null);
	    sum.addCodeLine(translator.translateMessage("sum3"), null, 0, null);
	    sum.addCodeLine(translator.translateMessage("sum4"), null, 0, null);
	    
	    lang.newRect(new Offset(-5, -5, "summary", AnimalScript.DIRECTION_NW), 
				new Offset(5, 5, "summary", AnimalScript.DIRECTION_SE), "rectsummary", null, rectProps2);
	    
	    lang.nextStep("Summary");
	    
	}

}
