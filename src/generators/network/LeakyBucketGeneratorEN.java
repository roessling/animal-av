/*
 * LeakyBucketGeneratorEN.java
 * Emil Angelov, Nikolay Dimitrov, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
//package generators.network;
package generators.network;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

public class LeakyBucketGeneratorEN implements Generator {
	private Language lang;
	private int ro;
	private int beta;

/////////////////////////////////////////// Vars
	private int higlightInCode = 0;
	private int higlightInputArray = 0;
	private int droppedPackets = 0;
	private int utilization = 0;
	private int bucketFilled = 0;
	private int inputLength = 20;
	private int chanceForZeros = 30;
	private int maxPacketLength = 15;
	private int[] input =  generateRandomList(inputLength, chanceForZeros, maxPacketLength);
	private List<Integer> highlight = new LinkedList<Integer>();
	
	private Polyline heightMarker;
	private Polyline bucketLine;

	public void init() {
		lang = new AnimalScript("Leaky Bucket", "Emil Angelov, Nikolay Dimitrov", 800, 600);
		lang.setStepMode(true);
	}

	/////////TODO: revert the generator()
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
	
		ro = (Integer) primitives.get("ro");
		beta = (Integer) primitives.get("beta");
		inputLength = (Integer) primitives.get("inputLength");
		chanceForZeros = (Integer) primitives.get("ChanceForZeros");
		maxPacketLength = (Integer) primitives.get("MaxPacketLength");
		//return "deleteThis";
	//}
	//public String generate() {
		
	//	ro = 5;
	//	beta = 20;
		
		TextProperties tpHeader = new TextProperties();
	    tpHeader.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
	    tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
	    Text header = lang.newText(new Coordinates(20, 30), getAlgorithmName(), "header", null, tpHeader);
	     
	  //Introduction Slide
	  		TextProperties tpDesctiption = new TextProperties();
	  		tpDesctiption.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
	  		tpDesctiption.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
	  		Text introduction0 = lang.newText(new Coordinates(60, 90), getDescription().split("\n")[0], "bucketheader", null, tpDesctiption);
	  		Text introduction1 = lang.newText(new Coordinates(60, 120), getDescription().split("\n")[1], "bucketheader", null, tpDesctiption);
	  		Text introduction2 = lang.newText(new Coordinates(60, 150), getDescription().split("\n")[2], "bucketheader", null, tpDesctiption);
	  		Text introduction3 = lang.newText(new Coordinates(60, 180), getDescription().split("\n")[3], "bucketheader", null, tpDesctiption);
	  		Text introduction4 = lang.newText(new Coordinates(60, 210), getDescription().split("\n")[4], "bucketheader", null, tpDesctiption);
	  		Text introduction5 = lang.newText(new Coordinates(60, 240), getDescription().split("\n")[5], "bucketheader", null, tpDesctiption);
	  		lang.nextStep();
	  		
	  		introduction0.hide();
	  		introduction1.hide();
	  		introduction2.hide();
	  		introduction3.hide();
	  		introduction4.hide();
	  		introduction5.hide();
	  		//lang.nextStep();
	    
	    RectProperties rp = new RectProperties();
	    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    Rect hRect = lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header", "SE"), "hRect", null, rp);
		
	    
		/////////////////// Initalization ///////////////////
		// first, set the visual properties (somewhat similar to CSS)
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		// now, create the IntArray object, linked to the properties
		//lang.nextStep();
		IntArray ia = lang.newIntArray(new Coordinates(60, 140), input, "intArray", null, arrayProps);
		// start a new step after the array was created
		//lang.nextStep();
		
		RectProperties rpBucket = new RectProperties();
	    rpBucket.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    rpBucket.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rpBucket.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    Rect bucketRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 210, "header", "SE"), "rpBucket", null, rpBucket);
		
	    RectProperties utilBucket = new RectProperties();
	    utilBucket.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    utilBucket.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    utilBucket.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
	    Rect utilRect = lang.newRect(new Offset(200, 480, "header", "NW"), new Offset(80, 420, "header", "SE"), "rpBucket", null, utilBucket);
		
	    Text utilizationValue = lang.newText(new Coordinates(200, 480), "", "header", null);
	    
	    RectProperties filledBucket = new RectProperties();
	    filledBucket.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    filledBucket.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    filledBucket.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
	    Rect filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410, "header", "SE"), "rpBucket", null, filledBucket);
		

	    RectProperties droppedPackets = new RectProperties();
	    droppedPackets.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    droppedPackets.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    droppedPackets.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
	    //Rect droppedpackets = lang.newRect(new Offset(200, 200, "header", "NW"), new Offset(80, 200, "header", "SE"), "rpBucket", null, droppedPackets);
		
	    RectProperties noDroppedPackets = new RectProperties();
	    noDroppedPackets.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    noDroppedPackets.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    noDroppedPackets.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
	    //Rect droppedpackets = lang.newRect(new Offset(200, 200, "header", "NW"), new Offset(80, 200, "header", "SE"), "rpBucket", null, droppedPackets);

	    
	    PolylineProperties polyp = new PolylineProperties();
        polyp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        //Offset[] verticesHeight = {new Offset(50, 50, "algo", "NE"), 
        //                          new Offset(75, 50, "algo", "NE")};
        
        Offset[] verticesHeight = {new Offset(50, 50, "bucketRect", "NE"),
        							new Offset(75, 50, "bucketRect", "NE")};
        
	    
	    bucketLine = lang.newPolyline(verticesHeight, "bucketline", null, polyp); 
		    
	    TextProperties bucketheader = new TextProperties();
	    bucketheader.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
	    bucketheader.set(AnimationPropertiesKeys.COLOR_PROPERTY,  Color.CYAN);
	    bucketheader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
	    //Text bucketheader = lang.newText(new Coordinates(160, 320), "Bucket", "bucketheader", null);
	    
		ArrayMarkerProperties currentP = new ArrayMarkerProperties();
		currentP.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		currentP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "next packets");	
				//currentP.set(AnimationPropertiesKeys.LABEL_PROPERTY, �current");
		ArrayMarker current = lang.newArrayMarker(ia, 0, "current", null, currentP);


		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc = lang.newSourceCode(new Coordinates(500, 30), "sourceCode", null, scProps);
		generateSourceCode(sc);
		
		

		//TextProperties bucketHeader = new TextProperties();
		TextProperties tpVars = new TextProperties();
		tpVars.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		tpVars.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
		tpHeader.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 60));
		Text bf = lang.newText(new Coordinates(420, 320), "", "bucketheader", null, tpVars);
		Text bfTEXT = lang.newText(new Coordinates(300, 320), "buckedFilled", "bucketheader", null, tpVars);
		Text inputValue = lang.newText(new Coordinates(420, 340), "", "bucketheader", null, tpVars);
		Text inputValueTEXT = lang.newText(new Coordinates(300, 340), "input", "bucketheader", null, tpVars);
		Text utilization = lang.newText(new Coordinates(420, 360), "", "bucketheader", null, tpVars);
		Text utilizationTEXT = lang.newText(new Coordinates(300, 360), "utiliz", "bucketheader", null, tpVars);
		Text dropped = lang.newText(new Coordinates(420, 380), "", "bucketheader", null, tpVars);
		Text droppedTEXT = lang.newText(new Coordinates(300, 380), "dropped", "bucketheader", null, tpVars);
		Text bucketName = lang.newText(new Coordinates(160, 320), "Bucket", "bucketheader", null, tpVars);
		

		lang.nextStep();
		Text helpingText0 = lang.newText(new Coordinates(60, 550), "The green bucket show the amount of data packets that are waiting in the bucket to be processed.", "bucketheader", null, tpDesctiption);
		Text helpingText1 = lang.newText(new Coordinates(60, 580), "The blue bucket show that a data is being processed, its max value is the constant rate of the leaky bucket.", "bucketheader", null, tpDesctiption);
		Text helpingText2 = lang.newText(new Coordinates(60, 610), "The red bucket show that some packets are dropped and can not be processed, if the input is larger than the free amount in the bucket.", "bucketheader", null, tpDesctiption);
		Text helpingText3 = lang.newText(new Coordinates(60, 640), "The gray bucket show that no new packets are dropped, all of the incoming are processed in the leaky bucket.", "bucketheader", null, tpDesctiption);
		
		for (int i = 0; i < input.length; i++) {
			unHighlight(highlight ,sc);
			filledRect.hide();	
			filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410 - bucketFilled*((410-210)/beta), "header", "SE"), "rpBucket", null, filledBucket);
						
			outputFromBucket();
			filledRect.hide();	
			filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410 - bucketFilled*((410-210)/beta), "header", "SE"), "rpBucket", null, filledBucket);
			
			bf.setText(""+this.bucketFilled, null, null);
			inputValue.setText(""+input[i], null, null);
			utilization.setText(""+this.utilization, null, null);
			dropped.setText(""+this.droppedPackets, null, null);
			
			//filledRect.hide();	
			//filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410 - bucketFilled*((410-210)/beta), "header", "SE"), "rpBucket", null, filledBucket);
			
			lang.nextStep();
			
			unHighlight(highlight ,sc);
			
			fillBucket(input[i]);
			filledRect.hide();	
			filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410 - bucketFilled*((410-210)/beta), "header", "SE"), "rpBucket", null, filledBucket);
			
			//filledRect.hide();	
			//filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410 - bucketFilled*((410-210)/beta), "header", "SE"), "rpBucket", null, filledBucket);
			
			//lang.nextStep();
			
			/////////// Variables
			bf.setText(""+this.bucketFilled, null, null);
			inputValue.setText(""+input[i], null, null);
			utilization.setText(""+this.utilization, null, null);
			dropped.setText(""+this.droppedPackets, null, null);
			
			//filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410 - bucketFilled*((410-210)/beta), "header", "SE"), "rpBucket", null, filledBucket);
			
			if (this.droppedPackets > 0) {
				Rect droppedpackets = lang.newRect(new Offset(200, 200, "header", "NW"), new Offset(80, 200, "header", "SE"), "rpBucket", null, droppedPackets);
				//droppedpackets.hide();
			} else {
				Rect nodroppedpackets = lang.newRect(new Offset(200, 200, "header", "NW"), new Offset(80, 200, "header", "SE"), "rpBucket", null, noDroppedPackets);
			}
			
			
			utilizationValue.setText(""+this.utilization, null, null);
		    
			
			// Highlight all cells
			highlight(highlight ,sc);
			
			ia.highlightCell(i, ia.getLength() - 1, null, null);
			
			current.increment(null, null);
			bucketLine.moveTo("S", "translate", new Offset(50, 50 + i*20, "bucketline", "NE"), null, new TicksTiming(50));
			lang.nextStep();
		}
		//ia.hide();
		lang.nextStep();
		
		//END SLIDE
				lang.hideAllPrimitives();
				ia.hide();
				filledRect.hide();
				
				Text endText0 = lang.newText(new Coordinates(60, 60), "The leaky bucket algorithm can be conceptually understood as follows:", "bucketheader", null, tpDesctiption);
				Text endText1 = lang.newText(new Coordinates(60, 90), "        A packet is added to the leaky bucket on arriving if there is enough space in the bucket.", "bucketheader", null, tpDesctiption);
				Text endText2 = lang.newText(new Coordinates(60, 120), "       If there is not enough space in the bucket, the whole packet is discarded.", "bucketheader", null, tpDesctiption);
				Text endText3 = lang.newText(new Coordinates(60, 150), "       Every 1/r seconds a max amount of the constant rate is processed and removed from the bucket.", "bucketheader", null, tpDesctiption);
				Text endText4 = lang.newText(new Coordinates(60, 210), " ", "bucketheader", null, tpDesctiption);
				Text endText5 = lang.newText(new Coordinates(60, 180), "At least some implementations of the leaky bucket are a mirror image of the Token Bucket algorithm and will,", "bucketheader", null, tpDesctiption);
				Text endText6 = lang.newText(new Coordinates(60, 210), "       given equivalent parameters, determine exactly the same sequence of events to conform or not conform to the same limits.", "bucketheader", null, tpDesctiption);
				
		return lang.toString();
	}
	
	/**
	 * 
	 * @param arrivingPacket
	 * @return + if bucket full and x data lost - if bucket got empty and x data
	 *         removed from bucket 0 else
	 */
	

	
	private static int[] generateRandomList(int size, int chanceForZeros, int maxPacketLength) {
		int[] alpha = new int[size];
		for (int i = 0; i < alpha.length; i++) {
			alpha[i] = (int) (Math.random() * maxPacketLength);
			if (Math.random() < (((double) chanceForZeros) /100)) { 
				alpha[i] = 0;
			}
		}
		return alpha;
	}

	public String getName() {
		return "Leaky Bucket";
	}

	public String getAlgorithmName() {
		return "Leaky Bucket";
	}

	public String getAnimationAuthor() {
		return "Emil Angelov, Nikolay Dimitrov";
	}

	public String getDescription() {
		return "The Leaky Bucket algorithm is an algorithm widely used in networking for traffic shaping. This allows"
				+ "\n"
				+ "applications with limited capacity to run without being overloaded due to the jitter that occures in the"
				+ "\n" + "network." + "\n"
				+ "Leaky Bucket allow flow to burst for a short period of time while the drain rate will not exceed the"
				+ "\n"
				+ "predefined drainrate of the bucket. Burst time and size depend on the size of the bucket, while drain"
				+ "\n" + "speed is chosen as the maximum the appliation can process. ";
	}

	private void generateSourceCode(SourceCode sc) {
		String[] codeExample = getCodeExample().split("[\\r\\n]+");
		for (int i = 0; i < codeExample.length; i++) {
			sc.addCodeLine(codeExample[i], null, 0, null);
		}

	}

	
	
	public String getCodeExample() {
		return 	"public void leakyBucket(input, int ro, int beta){" + "\n" + 
				"    Buck bucket = new Bucket(bucketSize)"+ "\n" + 
				"    while(true){" + "\n" + 
				"        var bucketSize = "+ beta + "\n" + 
				"        var utilization=0;" + "\n" + 
				"        var restCapacity=0;"+ "\n" +
				"        var droppedPackets= 0"+ "\n" +
				"        if(input.onArrive()){"+ "\n" +
				"            restCapacity = bucketSize - bucket.size;"  + "\n" +
				"            if(restCapacity >= input.size){" + "\n" +
				"                bucket.add(input);" + "\n" +
				"            }else{" + "\n" +
				"                droppedPacks = input.size"+ "\n" +
				"            }" + "\n" +
				"        }" + "\n" +
				"        if(startLeak()){" + "\n" +
				"            if(bucketSize >= ro){" + "\n" +
				"                output.forward( bucket.get(0, ro-1));" + "\n" +
				"                bucket.shiftLeft(ro-1)"+ "\n" +
				"                utilization = ro"+ "\n" + 
				"            }else{"+ "\n" + 
				"                output.forward( bucket.get(0, bucketSize-1));" + "\n" +
				"                bucket.shiftLeft(bucketSize-1)"+ "\n" +
				"                utilization = bucketSize;"+ "\n" + 
				"            }"+ "\n" + 
				"        }" + "\n" + 
				"    }" + "\n" + 
				"}";
	}
	
	private void fillBucket(int inputPacket){
		int restCapacity = beta - bucketFilled;
		if(restCapacity >= inputPacket){
			droppedPackets = 0;
			bucketFilled += inputPacket;
			highlight.add(7);
			highlight.add(8);
			highlight.add(9);
			highlight.add(10);
		}else{
			droppedPackets = inputPacket - restCapacity;
			//bucketFilled = beta; // the whole input packet is dropped
			highlight.add(7);
			highlight.add(8);
			highlight.add(9);
			highlight.add(12);
		}		
	}
	
    private void outputFromBucket(){
		if(bucketFilled >= ro){
			utilization = ro;
			bucketFilled -= ro;
			highlight = new LinkedList<Integer>();
			highlight.add(15);
			highlight.add(16);
			highlight.add(17);
			highlight.add(18);
			highlight.add(19);
		}else{
			utilization = bucketFilled;
			bucketFilled =0;
			highlight = new LinkedList<Integer>();
			highlight.add(15);
			highlight.add(16);
			highlight.add(21);
			highlight.add(22);
			highlight.add(23);
		}
	}
    
    private void unHighlight(List<Integer> list, SourceCode sc){
    	for (int i = 0; i < list.size(); i++) {
			sc.unhighlight(list.get(i));
		}
    }
    
    private void highlight(List<Integer> list, SourceCode sc){
    	for (int i = 0; i < list.size(); i++) {
			sc.highlight(list.get(i));
		}
    }

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
	/*
	public static void main(String[] args) {
		LeakyBucketGeneratorEN lb = new LeakyBucketGeneratorEN();
		lb.init();
	    System.out.println(lb.generate());
}
*/

}