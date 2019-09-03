/*
 * TokenBucketEN.java
 * Emil Angelov , Nikolay Dimitrov, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

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
import algoanim.util.Timing;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class TokenBucketEN implements Generator {
	private Language lang;
	private int deltaTokens;
	private int beta;
	private int inputLength;
	private int chanceForZeros;
	private int maxPacketLength;
	private int buffer = 0;
	/////////////////////////////////////////// Vars
	private int utilization = 0;
	private int bucketFilled = 0;
	private int[] input;
	private List<Integer> highlight = new LinkedList<Integer>();
	
	private Polyline bucketLine;

	public void init() {
		lang = new AnimalScript(getAlgorithmName(), "Emil Angelov , Nikolay Dimitrov", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		deltaTokens = (Integer)primitives.get("deltaTokens");
        chanceForZeros = (Integer)primitives.get("chanceForZeros");
        inputLength = (Integer)primitives.get("inputLength");
        maxPacketLength = (Integer)primitives.get("maxPacketLength");
        beta = (Integer)primitives.get("beta");
		
		input = generateRandomList(inputLength, chanceForZeros, maxPacketLength);
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
		Text introduction6 = lang.newText(new Coordinates(60, 270), getDescription().split("\n")[6], "bucketheader", null, tpDesctiption);
		lang.nextStep();
		
		introduction0.hide();
		introduction1.hide();
		introduction2.hide();
		introduction3.hide();
		introduction4.hide();
		introduction5.hide();
		introduction6.hide();
		lang.nextStep();
		
		
		
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		Rect hRect = lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5, 5, "header", "SE"), "hRect", null,
				rp);


		
		// set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc = lang.newSourceCode(new Coordinates(500, 80), "sourceCode", null, scProps);
		generateSourceCode(sc);

		/////////////////// Initalization ///////////////////
		// first, set the visual properties (somewhat similar to CSS)
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

		IntArray ia = lang.newIntArray(new Coordinates(60, 140), input, "intArray", null, arrayProps);
		
		RectProperties rpBucket = new RectProperties();
	    rpBucket.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    rpBucket.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rpBucket.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    Rect bucketRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 210, "header", "SE"), "rpBucket", null, rpBucket);
		
		
	    RectProperties filledBucket = new RectProperties();
	    filledBucket.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    filledBucket.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    filledBucket.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
	    Rect filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410, "header", "SE"), "rpBucket", null, filledBucket);
		
	   	    
	    RectProperties utilBucket = new RectProperties();
	    utilBucket.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    utilBucket.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    utilBucket.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
	    Rect utilRect = lang.newRect(new Offset(240, 480, "header", "NW"), new Offset(80, 420, "header", "SE"), "rpBucket", null, utilBucket);
		
	    RectProperties rpBuffer = new RectProperties();
	    rpBuffer.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
	    rpBuffer.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rpBuffer.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
	    Rect bufferRect = lang.newRect(new Offset(200, 480, "header", "NW"), new Offset(40, 420, "header", "SE"), "rpBucket", null, rpBuffer);
		
	    
	    PolylineProperties polyp = new PolylineProperties();
        polyp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
	    
	    Offset[] verticesHeight = {new Offset(50, 50, "bucketRect", "NE"),
				new Offset(75, 50, "bucketRect", "NE")};
	    
	    bucketLine = lang.newPolyline(verticesHeight, "bucketline", null, polyp); 
	    
	    ArrayMarkerProperties currentP = new ArrayMarkerProperties();
		currentP.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		currentP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "next packets");	
		ArrayMarker current = lang.newArrayMarker(ia, 0, "current", null, currentP);
		
		///////////Vars
		TextProperties tpVars = new TextProperties();
		tpVars.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		tpVars.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
	    Text bf = lang.newText(new Coordinates(420, 320), "", "bucketheader", null,tpVars);
	    Text bfTEXT = lang.newText(new Coordinates(300, 320), "buckedFilled", "bucketheader", null, tpVars);
	    Text inputValue = lang.newText(new Coordinates(420, 340), "", "bucketheader", null, tpVars);
	    Text inputValueTEXT = lang.newText(new Coordinates(300, 340), "input", "bucketheader", null, tpVars);
	    Text utilization = lang.newText(new Coordinates(420, 360), "", "bucketheader", null, tpVars);
	    Text utilizationTEXT = lang.newText(new Coordinates(300, 360), "utilization", "bucketheader", null, tpVars);
	    Text bufferValue = lang.newText(new Coordinates(420, 380), "", "bucketheader", null, tpVars);
	    Text bufferTEXT = lang.newText(new Coordinates(300, 380), "buffer", "bucketheader", null, tpVars);
		
		lang.nextStep();
		Text helpingText0 = lang.newText(new Coordinates(60, 550), "The green bucket show the amount of tokens that we have in the bucket available.", "bucketheader", null, tpDesctiption);
		Text helpingText1 = lang.newText(new Coordinates(60, 580), "The blue bucket show the amount of data that is being processed, for every piece of data from the input a token is used.", "bucketheader", null, tpDesctiption);
		Text helpingText2 = lang.newText(new Coordinates(60, 610), "The yellow bucket show the amount of data that can not be processed, if the input is larger than the amount of tokens available.", "bucketheader", null, tpDesctiption);
		Text helpingText3 = lang.newText(new Coordinates(60, 640), "This unprocessed packets can either be dropped or queued in a separate buffer, this is not part of the token bucket algorithm", "bucketheader", null, tpDesctiption);
		
		for (int i = 0; i < input.length; i++) {
			unHighlight(highlight, sc);
			
			filledRect.hide();
			utilRect.hide();
			bufferRect.hide();
			

			fillBucket();
			
		    ////// LOGIC : set global variables
			outputFromBuckt(input[i]);
			filledRect = lang.newRect(new Offset(200, 440, "header", "NW"), new Offset(80, 410 - bucketFilled*((410-210)/beta), "header", "SE"), "rpBucket", null, filledBucket);
			utilRect = lang.newRect(new Offset( 240 + this.utilization *10, 480, "header", "NW"), new Offset(80, 420, "header", "SE"), "rpBucket", null, utilBucket);
			bufferRect = lang.newRect(new Offset(200 - buffer * 10, 480, "header", "NW"), new Offset(40, 420, "header", "SE"), "rpBucket", null, rpBuffer);
					
			lang.nextStep();
			
			//////////resetVars
			bf.setText(""+this.bucketFilled, null, null);
			inputValue.setText(""+input[i], null, null);
			utilization.setText(""+this.utilization, null, null);
			bufferValue.setText(""+this.buffer, null, null);

			// Highlight all cells
			highlight(highlight, sc);

			ia.highlightCell(i, ia.getLength() - 1, null, null);
			
			current.increment(null, null);
			
			bucketLine.moveTo("S", "translate", new Offset(50, 50 + i*20, "bucketline", "NE"), null, null);
			

			lang.nextStep();
		}
		lang.nextStep();
		
		///END SLIDE
		lang.hideAllPrimitives();
		ia.hide();
		filledRect.hide();
		
		Text endText0 = lang.newText(new Coordinates(60, 60), "The token bucket algorithm can be conceptually understood as follows:", "bucketheader", null, tpDesctiption);
		Text endText1 = lang.newText(new Coordinates(60, 90), "        A token is added to the bucket every 1/r seconds.", "bucketheader", null, tpDesctiption);
		Text endText2 = lang.newText(new Coordinates(60, 120), "        The bucket can hold at the most b tokens. If a token arrives when the bucket is full, it is discarded.", "bucketheader", null, tpDesctiption);
		Text endText3 = lang.newText(new Coordinates(60, 150), "        When a packet (network layer PDU) of n bytes arrives, n tokens are removed from the bucket, and the packet is sent to the network.", "bucketheader", null, tpDesctiption);
		Text endText4 = lang.newText(new Coordinates(60, 180), "        If fewer than n tokens are available, no tokens are removed from the bucket, and the packet is considered to be non-conformant.", "bucketheader", null, tpDesctiption);
		Text endText5 = lang.newText(new Coordinates(60, 210), "Variations ", "bucketheader", null, tpDesctiption);
		Text endText6 = lang.newText(new Coordinates(60, 240), "        Implementers of this algorithm on platforms lacking the clock resolution necessary to add a single token to the bucket every 1/r seconds may want to consider an alternative formulation.", "bucketheader", null, tpDesctiption);
		Text endText7 = lang.newText(new Coordinates(60, 270), "        Given the ability to update the token bucket every S milliseconds, the number of tokens to add every S milliseconds = (r*S)/1000.", "bucketheader", null, tpDesctiption);
		Text endText8 = lang.newText(new Coordinates(60, 300), "The token bucket algorithm is directly comparable to one of the two versions of the leaky bucket algorithm which is also described in animal.", "bucketheader", null, tpDesctiption);
		

		return lang.toString();

	}

	public String getName() {
		return "Token Bucket";
	}

	public String getAlgorithmName() {
		return "Token Bucket";
	}

	public String getAnimationAuthor() {
		return "Emil Angelov , Nikolay Dimitrov";
	}

	public String getDescription() {
		return "The token bucket algorithm is an algorithm widely used in packet switching computer networks for" 
				+ "\n"
				+ "traffic shaping. This allows applications with limited capacity to run without being overloaded due to the"
				+ "\n"
				+ "jitter that occures in the network. Teaky bucket puts a predefined size of tokens to the bucket"
				+ "\n"
				+ "every second. In order to process input of size X, X many tokens have to be present in the bucket. If"
				+ "\n"
				+ "this is the case, the input is forwarded, otherwise only X size from the input is taken and the same amount"
				+ "\n"
				+ "of tokens is removed from the bucket. We can say that for every inut, the bucket should contain at least,"
				+ "\n" + "the same amount of tokens.";
	}

	private static int[] generateRandomList(int size, int chanceForZeros, int maxPacketLength) {
		int[] alpha = new int[size];
		for (int i = 0; i < alpha.length; i++) {
			alpha[i] = (int) (Math.random() * maxPacketLength);
			if (Math.random() < (((double) chanceForZeros) /100)) { // 60% chance for zero
				alpha[i] = 0;
			}
		}
		return alpha;
	}

	private void unHighlight(List<Integer> list, SourceCode sc) {
		for (int i = 0; i < list.size(); i++) {
			sc.unhighlight(list.get(i));
		}
	}

	private void generateSourceCode(SourceCode sc) {
		String[] codeExample = getCodeExample().split("[\\r\\n]+");
		for (int i = 0; i < codeExample.length; i++) {
			sc.addCodeLine(codeExample[i], null, 0, null);
		}
	}

	private void highlight(List<Integer> list, SourceCode sc) {
		for (int i = 0; i < list.size(); i++) {
			sc.highlight(list.get(i));
		}
	}

	public String getCodeExample() { 
		return 	"public void tokenBucket(input, int tokens, int bucketSize){" + "\n" + 
	"    Buck bucket = new Bucket(bucketSize)"+ "\n" + 
	"    while(true){" + "\n" + 
	"        var bucketSize = 0"+ "\n" + 
	"        var utilization= 0"  + "\n" + 
	"        var restCapacity= 0"+ "\n" +
	"        var bufferedPackets= 0" + "\n" +
	"        if(input.arrivedTokens()){"+ "\n" +
	"            bucket.size = min(bucketSize, (bucket.size + tokens); " + "\n" +
	"        }" + "\n" +
	"        if(startLeak()){" + "\n" + 
	"            input = input + bufferedPackets" + "\n" +
	"            if(bucketFilled >= input){" + "\n" +
	"                output.forward( bucket.get(0, input-1));" + "\n" +
	"                bucket.shiftLeft(input-1)"+ "\n" +
	"                utilization = input"+ "\n" + 
	"                bufferedPackets = 0"+ "\n" + 
	"            }else{"+ "\n" + 
	"                utilization = bucket.size"+ "\n" + 
	"                output.forward( bucket.get(0, bucketFilled-1));" + "\n" +
	"                bucket.shiftLeft(bucketFilled-1)"+ "\n" +
	"                buffer = buffer + input - bucketFilled"+ "\n" + 
	"            }"+ "\n" + 
	"        }" + "\n" + 
	"    }" + "\n" + 
	"}";
}

	private void fillBucket(){
		bucketFilled += deltaTokens;
		if(bucketFilled > beta)
			bucketFilled = beta;
		highlight.add(7);
		highlight.add(8);
	}
	
	private void outputFromBuckt(int inputPacket){
		highlight = new LinkedList<Integer>();
		highlight.add(10);
		highlight.add(11);
		highlight.add(12);
		if(inputPacket + buffer < bucketFilled){
			utilization = inputPacket + buffer;
			bucketFilled -= utilization;
			buffer = 0;
			highlight.add(13);
			highlight.add(14);
			highlight.add(15);
			highlight.add(16);
		}else{
			utilization = bucketFilled;
			bucketFilled = 0;
			buffer = (buffer + inputPacket) - utilization;
			highlight.add(18);
			highlight.add(19);
			highlight.add(20);
			highlight.add(21);
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


}