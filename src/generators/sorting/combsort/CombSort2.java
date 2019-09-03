package generators.sorting.combsort;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CombSort2 implements generators.framework.Generator {

	private ArrayMarker marker;

	private SourceCode intro;

	private Text gapSizeTxt;
	
	private Text swappedTxt;
	
	private SourceCode source;

	private static final String DESCRIPTION=
	"Comb Sort is a relatively simplistic sorting algorithm originally designed by "+
	"Wlodek Dobosiewicz in 1980 and later rediscovered and popularised by Stephen "+
	"Lacey and Richard Box, who described it in Byte Magazine in April 1991. Comb "+
	"sort improves on bubble sort, and rivals in speed more complex algorithms like "+
	"Quicksort. The basic idea is to eliminate turtles, or small values near the end "+
	"of the list, since in a bubble sort these slow the sorting down tremendously. "+
	"Large values around the beginning of the list, do not pose a problem "+
	"in bubble sort.).\n"+
	"The basic idea of comb sort is that the gap between two elements, that are "+
	"compared and swapped, when not in the right order, can be much more than "+
	"one - like in bubble sort. Tests showed that this gap should be decreased by a "+
	"factor of ~1.3 each round to make the algortihm as fast as possible. When the gapsize "+
	"reaches 9 or 10, it should be set to 11, because then the ordering is done faster. \n"+
	"Comb Sort has the same worst case complexity as Bubble sort - O(n*n) in Landau notation, " +
	"but is slightly faster in most cases. (extended/taken from wikipedia)";
	
	private static final String CODE =
	"static void combSort(int[] array) {\n"+
	"  int gap = array.length;\n"+
	"  boolean swapped;\n"+
	"  do {\n"+
	"    swapped = false;\n"+
	"    gap = gap * 10 / 13;\n"+
	"    if (gap == 9 || gap == 10)\n" +
	"      gap = 11;\n"+
	"    if (gap < 1)\n"+						
	"      gap = 1;\n"+
	"    for (int i = 0; i < array.length - gap; i++) \n"+
	"      if (array[i] > array[i + gap]) \n"+
	"        swapped = true;\n"+
	"        swap(array[i],array[i+gap]);\n"+
	"  } while (gap > 1 || swapped);\n" +
	"}";

	
	public CombSort2() {
		//nothing to be done here...
	}

	private void sort(Language lang, IntArray array) {
		
		// Intro Page
		array.hide();
		gapSizeTxt.hide();
		swappedTxt.hide();
		
		intro.addCodeLine("Comb Sort is a relatively simplistic sorting algorithm originally designed by ",null,0,null);
		intro.addCodeLine("Wlodek Dobosiewicz in 1980 and later rediscovered and popularised by Stephen ",null,0,null);
		intro.addCodeLine("Lacey and Richard Box, who described it in Byte Magazine in April 1991. Comb ",null,0,null);
		intro.addCodeLine("sort improves on bubble sort, and rivals in speed more complex algorithms like ",null,0,null);
		intro.addCodeLine("Quicksort. The basic idea is to eliminate turtles, or small values near the end ",null,0,null);
		intro.addCodeLine("of the list, since in a bubble sort these slow the sorting down tremendously. ",null,0,null);
		intro.addCodeLine("Large values around the beginning of the list, do not pose a problem ",null,0,null);
		intro.addCodeLine("in bubble sort.).",null,0,null);
		intro.addCodeLine("",null,0,null);
		intro.addCodeLine("The basic idea of comb sort is that the gap between two elements, that are ",null,0,null);
		intro.addCodeLine("compared and swapped, when not in the right order, can be much more than ",null,0,null);
		intro.addCodeLine("one - like in bubble sort. Tests showed, that this gap should be decreased by ",null,0,null);
		intro.addCodeLine("factor ~1.3 each round to make the algortihm as fast as possible. When the gapsize ",null,0,null);
		intro.addCodeLine("reaches 9 or 10, it should be set to 11, beuause then, the ordering is done ",null,0,null);
		intro.addCodeLine("faster. Comb Sort has the same worst case complexity as Bubble sort - O(n*n) in ",null,0,null);
		intro.addCodeLine("Landau notation, but is slightly faster in most cases.",null,0,null);
		intro.addCodeLine("(extended/taken from wikipedia)",null,0,null);
		lang.nextStep();

		// Show Code and Array
		array.show();
		marker.hide();
		intro.hide();
		
		source.addCodeLine("static void combSort(int[] array) {",null,0,null);
		source.addCodeLine("int gap = array.length;",null,1,null);
		source.addCodeLine("boolean swapped;",null,1,null);
		source.addCodeLine("do {",null,1,null);
		source.addCodeLine("swapped = false;",null,2,null);
		source.addCodeLine("gap = gap * 10 / 13;",null,2,null);
		source.addCodeLine("if (gap == 9 || gap == 10)",null,2,null);
		source.addCodeLine("gap = 11;",null,3,null);
		source.addCodeLine("if (gap < 1)",null,2,null);						
		source.addCodeLine("gap = 1;",null,3,null);
		source.addCodeLine("for (int i = 0; i < array.length - gap; i++) ",null,2,null);
		source.addCodeLine("if (array[i] > array[i + gap]) ",null,3,null);
		source.addCodeLine("swapped = true;",null,4,null);
		source.addCodeLine("swap(array[i],array[i+gap]);",null,4,null);
		source.addCodeLine("} while (gap > 1 || swapped);",null,1,null);
		source.addCodeLine("}",null,0,null);
		
		combSort(lang, array);
		
	}
	
	private void combSort(Language lang, IntArray array){
		Timing defaultTiming = new TicksTiming(15);

		
		source.highlight(0);
		lang.nextStep();
		source.unhighlight(0);
		//-------------------------------------
		
		
		//-------------------------------------
		source.highlight(1);
		int gap = array.getLength(); //initialize gap size
		gapSizeTxt.show();
		gapSizeTxt.setText("Current gap size: " + gap, defaultTiming, defaultTiming);
		gapSizeTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
		
		lang.nextStep();
		gapSizeTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
		source.unhighlight(1);
		//-------------------------------------

		
		
		//-------------------------------------
		source.highlight(2);
		boolean swapped;
		swappedTxt.show();
		swappedTxt.setText("swapped: ", defaultTiming, defaultTiming);
		swappedTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
		
		lang.nextStep();
		swappedTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
		source.unhighlight(2);
		//-------------------------------------
		
		
		
		//-------------------------------------
		do {
			source.highlight(3);			
			
			lang.nextStep();
			source.unhighlight(3);
			//-------------------------------------
			
			
			//-------------------------------------			
			source.highlight(4);			
			swapped = false;
			swappedTxt.setText("swapped: " + String.valueOf(swapped), defaultTiming, defaultTiming);
			swappedTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
			
			lang.nextStep();
			swappedTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);;
			source.unhighlight(4);
			//-------------------------------------
			
			
			//-------------------------------------
			source.highlight(5);
			gap = gap *10 / 13;
			gapSizeTxt.setText("Current gap size: " + gap, defaultTiming, defaultTiming);
			gapSizeTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
			
			lang.nextStep();
			gapSizeTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
			source.unhighlight(5);
			//-------------------------------------
			
			
			//-------------------------------------
			source.highlight(6);
			gapSizeTxt.setText("Current gap size: " + gap, defaultTiming, defaultTiming);
			gapSizeTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
			
			lang.nextStep();
			gapSizeTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
			source.unhighlight(6);
			
			if (gap == 9 || gap == 10){
				//-------------------------------------
				
				
				//-------------------------------------
				source.highlight(7);
				gap = 11;
				gapSizeTxt.setText("Current gap size: " + gap, defaultTiming, defaultTiming);
				gapSizeTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
				
				lang.nextStep();
				gapSizeTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
				source.unhighlight(7);
			}
			//-------------------------------------
			
			
			//-------------------------------------
			source.highlight(8);	
			gapSizeTxt.setText("Current gap size: " + gap, defaultTiming, defaultTiming);
			gapSizeTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
			
			lang.nextStep();
			gapSizeTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
			source.unhighlight(8);
			
			if (gap < 1){
				//-------------------------------------
				
				
				//-------------------------------------
				source.highlight(9);
				gap= 1;
				gapSizeTxt.setText("Current gap size: " + gap, defaultTiming, defaultTiming);
				gapSizeTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
				
				lang.nextStep();
				gapSizeTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
					source.unhighlight(9);
			}
			//-------------------------------------
			
			
			
			//-------------------------------------
			source.highlight(10);
			
			lang.nextStep();
			source.unhighlight(10);
			
			marker.show();
						
			for (marker.move(0, null, null); marker.getPosition() < (array.getLength() - gap); marker.increment(null, null)) {
				source.highlight(10);
				
				lang.nextStep();
				source.unhighlight(10);
				//-------------------------------------
				
				
				//-------------------------------------
				source.highlight(11);
				array.highlightElem(marker.getPosition(), null, defaultTiming);
				array.highlightElem(marker.getPosition()+gap, null, defaultTiming);
				array.highlightCell(marker.getPosition(), marker.getPosition()+gap, null, null);
				
				lang.nextStep();
				source.unhighlight(11);
				
				if (array.getData(marker.getPosition()) > array.getData(marker.getPosition() + gap)) {
					//-------------------------------------

					
					//-------------------------------------
					source.highlight(12);
					swapped = true;
					swappedTxt.setText("swapped: " + String.valueOf(swapped), defaultTiming, defaultTiming);
					swappedTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
					
					lang.nextStep();
					swappedTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
					source.unhighlight(12);
					//-------------------------------------
					
					
					
					//-------------------------------------
					source.highlight(13);
					
					CheckpointUtils.checkpointEvent(this, "swapEvent", 
							new Variable("pos1", marker.getPosition()), 
							new Variable("pos2", marker.getPosition()+ gap),
							new Variable("val1", array.getData(marker.getPosition())),
							new Variable("val2", array.getData(marker.getPosition()+gap)),
							new Variable("gapSize", gap),
							new Variable("animstep",lang.getStep()));
					
					array.swap(marker.getPosition(), marker.getPosition() + gap, null, defaultTiming);
					
					lang.nextStep();
					source.unhighlight(13);
					//-------------------------------------
					

				}
				lang.nextStep();
				array.unhighlightCell(marker.getPosition(), marker.getPosition()+gap, null, null);
				array.unhighlightElem(marker.getPosition(), null, defaultTiming);
				array.unhighlightElem(marker.getPosition()+gap, null, defaultTiming);
			}
			
			//-----------------------------------
			source.highlight(14);
			
			marker.hide();
			marker.hide();

			gapSizeTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);
			swappedTxt.changeColor(null, Color.RED, defaultTiming, defaultTiming);

			
			lang.nextStep();
			gapSizeTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
			swappedTxt.changeColor(null, Color.BLACK, defaultTiming, defaultTiming);
			source.unhighlight(14);
		
		} while (gap > 1 || swapped);
		//-----------------------------------

		lang.nextStep();
		
		
		source.highlight(15);
		
		lang.nextStep();
		source.unhighlight(15);
	}
	
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		Language lang = new AnimalScript("CombSort Animation", "trickSoft", 640, 480);

		lang.setStepMode(true);

		RectProperties rectProps= new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		@SuppressWarnings(value="unused")
		Rect textBox = lang.newRect(new Coordinates(60,10),new Coordinates(230, 55),"Box",null, rectProps);		

		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		@SuppressWarnings(value="unused")
		Text nameText = lang.newText(new Coordinates(86,28), "CombSort", "nameLabelOnTop", null, textProps);
		
		int[] arrayData = (int[]) primitives.get("array");
		if (arrayData == null)
			arrayData = (int[]) primitives.get("Input Data");
		//int[] arrayData = (int[]) primitives.get("Input Data");
		IntArray array = lang.newIntArray(new Coordinates(60, 130), arrayData, "array",	null, (ArrayProperties) props.getPropertiesByName("Array"));
		
		gapSizeTxt = lang.newText(new Coordinates(100,170), "Current gap size: ", "actual gapsize: ", null);
		
		swappedTxt = lang.newText(new Coordinates(100,190), "swappped: ", "swapped: ", null);
		
		source = lang.newSourceCode(new Coordinates(60, 210), "sourceCode", null, (SourceCodeProperties) props.getPropertiesByName("Source Code"));
		
		intro = lang.newSourceCode(new Coordinates(60, 90), "Intro Text", null, (SourceCodeProperties) props.getPropertiesByName("Intro Text"));
		
		marker = lang.newArrayMarker(array, 0, "i", null,(ArrayMarkerProperties) props.getPropertiesByName("Marker"));
		
		sort(lang, array);
		return lang.toString();
	}

	public String getAlgorithmName() {
		return "Comb Sort";
	}

	public String getCodeExample() {
		return CODE;
	}

	public Locale getContentLocale() {
		return Locale.US; // US-English
	}

	public String getDescription() {
		return DESCRIPTION; // description
	}

	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getName() {
		return "Comb Sort";
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public String getAnimationAuthor() {
		return "Daniel Trick, Jonathan Römer, Florian Jung";
	}

	@Override
	public void init() {
    // Nothing to be done here
	}


}