package generators.sorting.shakersort;

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
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
/**
 * @author Fehmi Belhadj
 * @version 1.1 2012-05-28
 * ShakerSort Algorithm with optimization
 */

public class Shakersorter2 implements Generator {
	/**
	   * Declaration of parameters
	*/
	private Language language = new AnimalScript("ShakerSort Algorithm", "Fehmi Belhadj", 800, 600);
	private Language lang ;
	private ArrayProperties arrayProperty;
	
	// Array Marker Properties
	private ArrayMarkerProperties arrayMarkerProp_i = new ArrayMarkerProperties();
	private ArrayMarkerProperties arrayMarkerProp_i_plus_1 = new ArrayMarkerProperties(); 
	private ArrayMarkerProperties arrayMarkerProp_i_minus_1 = new ArrayMarkerProperties(); 
	private ArrayMarkerProperties arrayMarkerProp_first = new ArrayMarkerProperties(); 
	private ArrayMarkerProperties arrayMarkerProp_last = new ArrayMarkerProperties(); 

	// Declaration Array Marker
	private ArrayMarker arrayMarker_i; 
	private ArrayMarker arrayMarker_i_plus1; 
	private ArrayMarker arrayMarker_i_minus1;
	private ArrayMarker arrayMarker_first; 
	private ArrayMarker arrayMarker_last; 

	// Source Code
	private SourceCode source,sc_description,title,end_description,Parametres,Parametres2,Parametres3;
	
	//Text
//	private TextProperties textProps;
	Text zahl,zahl2,tex,tex1,zahl3,text3;
	int ZtransPositions =0,VtransPositions=0,SwapTransp=0;
	
	// Animation Author
	String AnimationAuthor = "Fehmi Belhadj";
	
	// Algorithm Name
	String AlgorithmName = "ShakerSorter";
	
	// Array
	int[] original;
	
	Color Elem_Color = Color.RED;
	Color Fill_Color = Color.yellow; 
	Color Cell_light = Color.orange;
	
	// marker for i: 
	String label_i = "i";
	Color color_i_marker = Color.BLACK;
	
	// marker for i+1: 
	String label_i_plus = "i+1";
	Color color_i_plus_marker = Color.DARK_GRAY;
	
	// marker for i-1: 
	String label_i_minus = "i-1";
	Color color_i_minus_marker = Color.DARK_GRAY;
	
	// marker for first:
	String label_first = "first";
	Color color_first_marker = Color.BLUE;
	
	// marker for last: 
	String label_last = "last";
	Color color_last_marker = Color.RED;
	
	// Timing
	Timing defaultTiming = new TicksTiming(20);
	Timing defaultTiming_swap = new TicksTiming(301);

	//Initialisation
	public Shakersorter2() {
		this.init();
	}
	public Shakersorter2(Language l) {
		lang = l;
		lang.setStepMode(true);
	}
	/**
	   * The method init()
	   * initialize the main elements
	   * Generate a new Language instance for content creation
	   * Parameter: Animation title, author, width, height
	*/
	public void init() {
		lang = this.language;
		lang.setStepMode(true);
}
	/**
	   * Globally defined source code properties
	   * create the source code entity
	   * set the visual properties for the sourcedescription code
	   */
	public void showSourceCodedescription() {
		// set the visual properties for the title code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		// now, create the source code entity
		title = lang.newSourceCode(new Coordinates(400, 10), "sourceCodeDesc",	null, scProps);
		title.addCodeLine(" ShakerSort Algorithm", null, 0, null);
		
		//Text property
		
		// set the visual properties for the sourcedescription code
		SourceCodeProperties scProps1 = new SourceCodeProperties();
		scProps1.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
		scProps1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		
		// set the visual properties for the source code
		SourceCodeProperties scProps2 = new SourceCodeProperties();
		scProps2.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		scProps2.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		// now, create the source code entity
		sc_description = lang.newSourceCode(new Coordinates(30, 120),"sourceCodeDesc", null, scProps2);
		// add a code line
		// parameters: code itself; name (can be null); indentation level;
		// display options
		sc_description.addCodeLine("Prinzip des Algorithms", null, 0, null);
		sc_description.addCodeLine("", null, 0, null);
		sc_description.addCodeLine("Das zu sortierende Feld wird abwechselnd nach oben und nach unten durchlaufen.",null, 2, null);
		sc_description.addCodeLine("Dabei werden jeweils zwei benachbarte Elemente verglichen und gegebenenfalls vertauscht.",null, 2, null);
		sc_description.addCodeLine("Durch diese Bidirektionalität kommt es zu einem schnellerem Absetzen von grossen bzw. kleinen Elementen.",null, 2, null);
		sc_description.addCodeLine("Anhand des Sortierverfahrens lässt sich auch der Name erklaeren",null, 2, null);
		sc_description.addCodeLine("denn der Sortiervorgang erinnert an das Schütteln des Arrays oder eines Barmixers.",null,2,null);
	}
	//Abschluss
	public void showEnddescription() {
		// set the visual properties for the title code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		// now, create the source code entity
		title = lang.newSourceCode(new Coordinates(400, 10), "sourceCodeDesc",	null, scProps);
		title.addCodeLine(" ShakerSort Algorithm", null, 0, null);

		// set the visual properties for the sourcedescription code
		SourceCodeProperties scProps1 = new SourceCodeProperties();
		scProps1.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
		scProps1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		
		// set the visual properties for the source code
		SourceCodeProperties scProps2 = new SourceCodeProperties();
		scProps2.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		scProps2.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		// now, create the source code entity
		end_description = lang.newSourceCode(new Coordinates(30, 120),"sourceCodeDesc", null, scProps2);
		// add a code line
		// parameters: code itself; name (can be null); indentation level;
		// display options
		end_description.addCodeLine("Komplexität des Algorithms ShakerSort:", null, 0, null);
		end_description.addCodeLine("", null, 0, null);
		end_description.addCodeLine("Der Algorithmus besitzt eine quadratische und daher im Vergleich ",null, 0, null);
		end_description.addCodeLine("zu vielen anderen Sortieralgorithmen schlechte Worst-Case-Laufzeit," ,null,0,null);
		end_description.addCodeLine("die jedoch in der einfachen ",null, 0, null);
		end_description.addCodeLine("Version gleichzeitig auch der normalen Laufzeit entspricht.",null, 0, null);
		end_description.addCodeLine("O(n log(n))= 6 log(6)=4.668 ~ 5 ",null, 0, null);
		}
	
	public IntArray showSourceCode( int[] arrayContents) {
		
		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.ROMAN_BASELINE, 14));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
	
		// now, create the source code entity
		source = lang.newSourceCode(new Coordinates(10, 150), "sourceCode", null,scProps);
		
		source.addCodeLine("public int [] sort(int [] array) {", null,	0, null);
		source.addCodeLine("boolean sorted = false;", null, 1, null);
		source.addCodeLine("boolean forward = true;", null, 1, null);
		source.addCodeLine("int first = 0;", null, 1, null);
		source.addCodeLine("int last = array.length-1;", null, 1, null);
		source.addCodeLine("while (first<last && !sorted) {", null, 1, null);
		source.addCodeLine("sorted = true;", null, 2, null);
		source.addCodeLine("if (forward) {", null, 2, null);
		source.addCodeLine("for (int i=first; i<last; i++) {", null, 4, null);
		source.addCodeLine("if (array[i] > array[i+1]){", null, 6, null);
		source.addCodeLine("swap(array[i],array[i+1]);", null, 8, null);
		source.addCodeLine("sorted = false;", null, 8, null);
		source.addCodeLine("}", null, 6, null);
		source.addCodeLine("}", null, 4, null);
		source.addCodeLine("last--;", null, 4, null);
		source.addCodeLine("}", null, 2, null);
		source.addCodeLine("else {", null, 2, null);
		source.addCodeLine("for (int i=last; i>first; i--) {", null, 4, null);
		source.addCodeLine("if (array[i] < array[i-1]) {", null, 6, null);
		source.addCodeLine("swap(array[i],array[i-1]);", null, 8, null);
		source.addCodeLine(" sorted = false;", null, 8, null);
		source.addCodeLine("}", null, 6, null);
		source.addCodeLine("}", null, 4, null);
		source.addCodeLine("first++;", null, 4, null);
		source.addCodeLine("}", null, 2, null);
		source.addCodeLine("forward = !forward;", null, 2, null);
		source.addCodeLine("}", null, 1, null);
		source.addCodeLine("return array;", null, 1, null);
		source.addCodeLine("}", null, 0, null);
		
		arrayProperty = new ArrayProperties();
		
		arrayProperty.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,	Elem_Color); 
		arrayProperty.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,	Cell_light); 
		arrayProperty.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); 
		arrayProperty.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProperty.set(AnimationPropertiesKeys.FILL_PROPERTY, Fill_Color); 
	    arrayProperty.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProperty.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,Color.BLUE);
		
		
		IntArray array = lang.newIntArray(new Coordinates(600, 200),arrayContents, "array", null, arrayProperty);

		arrayMarker_i= lang.newArrayMarker(array, 0, "i", null, arrayMarkerProp_i);
		arrayMarker_i.hide();
		arrayMarker_i_plus1= lang.newArrayMarker(array,0, "i+1", null, arrayMarkerProp_i_plus_1);
		arrayMarker_i_plus1.hide();
		arrayMarker_i_minus1= lang.newArrayMarker(array, 0, "i-1", null, arrayMarkerProp_i_minus_1);
		arrayMarker_i_minus1.hide();
		
			
		return array;
	}

	public static void main(String[] args) {
		Shakersorter2 sort = new Shakersorter2();
	    System.out.println(sort.generate(new AnimationPropertiesContainer(), new Hashtable<String, Object>()));
	}

	public void sort(int[] arrayContents) {
		//Text Property
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		
		//show code
		showSourceCodedescription();
		lang.nextStep("Introduction to Algorithm: ");
		sc_description.hide();
		
		Parametres = lang.newSourceCode(new Coordinates(200, 50), "sourceCodeDesc",null);
		Parametres.addCodeLine("Vergleiche=", null, 0, null);
		Parametres2= lang.newSourceCode(new Coordinates(20, 50), "sourceCodeDesc",null);
		Parametres2.addCodeLine("Zuweisung= ", null, 0, null);
		Parametres3= lang.newSourceCode(new Coordinates(380, 50), "sourceCodeDesc",null);
		Parametres3.addCodeLine("Swap= ", null, 0, null);
		
		textProps = new TextProperties();
		//Zuweisung
		tex = lang.newText(new Offset(220, 110, "", AnimalScript.DIRECTION_NE),"" + " ", "transpoText", null, textProps);
		zahl = lang.newText(new Offset(200, 110, "transpoText",AnimalScript.DIRECTION_NE), "0", "Transpositionen", null,textProps);
		
		//Vergleich
		tex1 = lang.newText(new Offset(320, 10, "", AnimalScript.DIRECTION_NW),"" + " ", "transpoText1", null, textProps);
		zahl2=lang.newText(new Offset(20,110,"transpoText1",AnimalScript.DIRECTION_NW), "0", "Transpositionen", null,textProps);
		
		//Swap
		text3 = lang.newText(new Offset(380, 110, "", AnimalScript.DIRECTION_NW),"" + " ", "transpoText1", null, textProps);
		zahl3=lang.newText(new Offset(380,110,"transpoText1",AnimalScript.DIRECTION_NW), "0", "Transpositionen", null,textProps);
		
		
		IntArray array = showSourceCode(arrayContents);
		
		
		source.highlight(0);
		lang.nextStep();
		
		source.unhighlight(0);
		source.highlight(1);
		boolean sorted = false;
		ZtransPositions++;
		zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
		lang.nextStep("The first linie of code is colored");
		
		source.unhighlight(1);
		source.highlight(2);
		boolean forward = true;
		ZtransPositions++;
		zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
		lang.nextStep("The second linie of code is colored");
		
		source.unhighlight(2);
		source.highlight(3);
		arrayMarker_first= lang.newArrayMarker(array, 0, "first", null, arrayMarkerProp_first);
		ZtransPositions++;
		zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
		lang.nextStep("The third linie of code is colored and Marker First is appeared");

		
		source.unhighlight(3);
		source.highlight(4);
		arrayMarker_last= lang.newArrayMarker(array, arrayContents.length-1, "last", null, arrayMarkerProp_last);
		ZtransPositions++;
		zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
		lang.nextStep("The fourth linie of code is colored and the last Marker is appeared");

		
		source.unhighlight(4);
		source.highlight(5);
		VtransPositions++;
		zahl.setText(String.valueOf(VtransPositions), null,defaultTiming);
		lang.nextStep("The fifth linie of code is colored");

		// show i marker
			
		while (arrayMarker_first.getPosition() < arrayMarker_last.getPosition() && !sorted) {
			
			sorted = true;
			source.unhighlight(5);
			source.highlight(6);
			ZtransPositions++;
			zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
			lang.nextStep("The sixth linie of code is colored");

			
			source.unhighlight(6);
			source.highlight(7);
			VtransPositions++;
			zahl.setText(String.valueOf(VtransPositions), null,defaultTiming);
			lang.nextStep("The seventh linie of code is colored");


			// to show i marker
			
			if (forward) {

		
				source.unhighlight(7);
				source.highlight(8);
				lang.nextStep("The eighth linie of code is colored");

				arrayMarker_i.move(arrayMarker_first.getPosition(), null, defaultTiming);
				arrayMarker_i.show();
				for (; arrayMarker_i.getPosition() < arrayMarker_last.getPosition(); arrayMarker_i.increment(null,defaultTiming)) {

					lang.nextStep("Sorting elements from "+arrayMarker_first.getPosition() +" to "+arrayMarker_last.getPosition());
					
					source.unhighlight(8);
					source.highlight(9);
					VtransPositions++;
					zahl.setText(String.valueOf(VtransPositions), null,defaultTiming);
					arrayMarker_i_plus1.move((arrayMarker_i.getPosition() + 1), null, defaultTiming);
					arrayMarker_i_plus1.show();
					lang.nextStep("The ninth linie of code is colored and moving Marker i+1");
					source.unhighlight(9);
					if (array.getData(arrayMarker_i.getPosition()) > array.getData(arrayMarker_i.getPosition() + 1)) {
						
						source.highlight(10);
						array.highlightElem((arrayMarker_i.getPosition()),arrayMarker_i_plus1.getPosition(), null, defaultTiming);
						lang.nextStep("The tenth linie of code is colored");
						array.swap((arrayMarker_i.getPosition()), arrayMarker_i_plus1.getPosition(), null,defaultTiming_swap);
						SwapTransp++;
						zahl3.setText(String.valueOf(SwapTransp), null,defaultTiming);
						lang.nextStep("A swap is done");
						array.unhighlightElem((arrayMarker_i.getPosition()), arrayMarker_i_plus1.getPosition(), null, defaultTiming);
					
						arrayMarker_i_plus1.hide();
						source.unhighlight(10);
						source.highlight(11);
						sorted = false;
						ZtransPositions++;
						zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
						lang.nextStep("The 11th linie of code is colored + Marker i+1 is hidden");
						source.unhighlight(11);
						source.highlight(8);
						lang.nextStep("The eighth linie of code is colored");
					}
				}
				array.highlightCell(arrayMarker_last.getPosition(),arrayContents.length-1, null, defaultTiming);
				arrayMarker_last.move((arrayMarker_last.getPosition() - 1), null, defaultTiming);
				source.unhighlight(8);
				source.highlight(14);
				ZtransPositions++;
				zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
				arrayMarker_i_plus1.hide();
				lang.nextStep("The 14th linie of code is colored");
				source.unhighlight(14);

			} else {
				source.unhighlight(7);
				source.highlight(16);
				lang.nextStep("The 16th linie of code is colored");
				
				source.unhighlight(16);
				source.highlight(17);
				lang.nextStep("The 17th linie of code is colored");
				//
				arrayMarker_i.move(arrayMarker_last.getPosition(), null, defaultTiming);
				arrayMarker_i.show();
				lang.nextStep("Marker i is moved");
				for (; arrayMarker_i.getPosition() > arrayMarker_first.getPosition(); arrayMarker_i.decrement(null,defaultTiming)) {
					lang.nextStep();
					
					source.unhighlight(17);
					source.highlight(18);
					VtransPositions++;
					zahl.setText(String.valueOf(VtransPositions), null,defaultTiming);
					lang.nextStep("The 18th linie of code is colored");
					source.unhighlight(18);
					arrayMarker_i_minus1.move((arrayMarker_i.getPosition() - 1), null, defaultTiming);
					arrayMarker_i_minus1.show();
					lang.nextStep("Marker i-1 is moved");
					if (array.getData(arrayMarker_i.getPosition()) < array.getData(arrayMarker_i.getPosition() - 1)) {
						source.highlight(19);
						lang.nextStep("The 19th linie of code is colored");
						array.highlightElem(arrayMarker_i_minus1.getPosition(),	(arrayMarker_i.getPosition()), null, defaultTiming);
						lang.nextStep();
						array.swap((arrayMarker_i.getPosition()), arrayMarker_i_minus1.getPosition(), null,	defaultTiming_swap);
						SwapTransp++;
						zahl3.setText(String.valueOf(SwapTransp), null,defaultTiming);
						lang.nextStep("A swap is done");
						array.unhighlightElem(arrayMarker_i_minus1.getPosition(), (arrayMarker_i.getPosition()), null, defaultTiming);
						arrayMarker_i_minus1.hide();
						sorted= false;
						source.unhighlight(19);
						source.highlight(20);
						ZtransPositions++;
						zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
						lang.nextStep("The 20th linie of code is colored");
					}

				}
				source.unhighlight(20);
				source.highlight(23);
				ZtransPositions++;
				zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
				array.highlightCell(0, arrayMarker_first.getPosition(), null, defaultTiming);
				arrayMarker_first.move((arrayMarker_first.getPosition() + 1), null, defaultTiming);
				arrayMarker_i_minus1.hide();
				lang.nextStep("The seventh linie of code is colored + Marker first is moved + Marker i-1 is hidden");
				source.unhighlight(23);

			}
			arrayMarker_i_minus1.hide();
			arrayMarker_i_plus1.hide();
			ZtransPositions++;
			zahl2.setText(String.valueOf(ZtransPositions), null,defaultTiming);
			source.highlight(25);
			forward = !forward;
			lang.nextStep("The 25th linie of code is colored");
			source.unhighlight(25);
			source.highlight(5);
			arrayMarker_i.hide();
			lang.nextStep("The seventh linie of code is colored + Marker i is hidden");
		}
		source.unhighlight(5);
		
		array.highlightCell(0, (array.getLength() - 1), null, defaultTiming);
		source.highlight(27);
		lang.nextStep("The 27th linie of code is colored");
		arrayMarker_first.hide();
		arrayMarker_last.hide();
		lang.nextStep("Markers first and last are hidden");
		//showSourceCode(arrayContents).hide();
		source.hide();
		Parametres.hide();zahl2.hide();Parametres2.hide();zahl.hide();zahl3.hide();Parametres3.hide();
		showEnddescription();
		
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		
		if(primitives.get("array") != null) {
			this.original = (int[]) primitives.get("array");
			this.arrayMarkerProp_i = (ArrayMarkerProperties) props.getPropertiesByName("i");
			this.arrayMarkerProp_i_plus_1 = (ArrayMarkerProperties) props.getPropertiesByName("iplus1");
			this.arrayMarkerProp_i_minus_1 = (ArrayMarkerProperties) props.getPropertiesByName("iminus1");
			this.arrayMarkerProp_first = (ArrayMarkerProperties) props.getPropertiesByName("first");	
			this.arrayMarkerProp_last = (ArrayMarkerProperties) props.getPropertiesByName("last");	
		} else {
			this.original = new int[] { 56, 72, 3, 30, 65, 24 }; 

		
			
		   
			// ArrayMarkerProperties i
			this.arrayMarkerProp_i = new ArrayMarkerProperties();
			arrayMarkerProp_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_i);   
			arrayMarkerProp_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_i_marker);
			
			// ArrayMarkerProperties i+1
			this.arrayMarkerProp_i_plus_1 = new ArrayMarkerProperties();
			arrayMarkerProp_i_plus_1.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_i_plus);   
			arrayMarkerProp_i_plus_1.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_i_plus_marker);
			
			// ArrayMarkerProperties i-1
			this.arrayMarkerProp_i_minus_1 = new ArrayMarkerProperties();
			arrayMarkerProp_i_minus_1.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_i_minus);   
			arrayMarkerProp_i_minus_1.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_i_minus_marker);
			
			// ArrayMarkerProperties first
			this.arrayMarkerProp_first = new ArrayMarkerProperties();
			arrayMarkerProp_first.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_first);   
			arrayMarkerProp_first.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_first_marker);
			
			// ArrayMarkerProperties last
			this.arrayMarkerProp_last = new ArrayMarkerProperties();
			arrayMarkerProp_last.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_last);   
			arrayMarkerProp_last.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_last_marker);
		
	   
		}
			
		
		this.sort(this.original);
		return this.language.toString();
	}

	public String getAlgorithmName() {
		return "Shaker Sort";
	}

	public String getAnimationAuthor() {
		return "Fehmi Belhadj";
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public String getDescription() {
		return "Das zu sortierende Feld wird abwechselnd nach oben und nach unten durchlaufen.\n"+
		"Dabei werden jeweils zwei benachbarte Elemente verglichen und gegebenenfalls vertauscht.\n"+
		"Durch diese Bidirektionalit&auml;t kommt es zu einem schnellerem Absetzen von gro&szlig;en bzw. kleinen Elementen.\n" +
		" Anhand des Sortierverfahrens lässt sich auch der Name erklären,\n" +
		" denn der Sortiervorgang erinnert an das Schütteln des Arrays oder eines Barmixers."; 
		// Quelle: http://de.wikipedia.org/wiki/Shakersort
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION; 
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT); 
	}

	public String getName() {
		return "ShakerSorter"; 
	}

	public String getOutputLanguage() {
		return JAVA_OUTPUT;
	}
	
	public String getCodeExample() { 
		return "public int [] sorting(int [] array) {\n"+
			   " boolean sort = false;\n"+
			   " boolean forward = true;\n"+
			   " int first = 0;\n"+
			   " int last = array.length-1;\n"+
			   " while (first&lt;last && !sort) {\n"+
			   "   sort = true;\n"+
			   "   if (forward) {\n"+
			   "     for (int i=first; i&lt;last; i++) {\n"+
			   "       if (array[i] &gt; array[i+1]){\n"+
			   "         swap(array[i],array[i+1]);\n"+
			   "         sort = false;\n"+
			   "       }\n"+
			   "      }\n"+
			   "      last--;\n"+
			   "    }\n"+
			   "    else {\n"+
			   "      for (int i=last; i&gt;first; i--) {\n"+
			   "        if (array[i] &lt; array[i-1]) {\n"+
			   "          swap(liste[i],liste[i-1]);\n"+
			   "          sort = false;\n"+
			   "         }\n"+
			   "      }\n"+
			   "      first++;\n"+
			   "    }\n"+
			   "    forward =! forward;\n"+
			   "  }\n"+
			   " return array;\n"+
			   "}";
	}

}
