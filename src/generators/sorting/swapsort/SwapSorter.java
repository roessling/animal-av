package generators.sorting.swapsort;
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
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class SwapSorter implements Generator {
	private Language language = new AnimalScript("SwapSort", "Sami Graja, Hasan Tercan", 800, 600);
	protected Language lang;
	private ArrayProperties arrayProps;
	
	// Array Marker Properties
	private ArrayMarkerProperties arrayMarkerPro_i = new ArrayMarkerProperties(); // i
	private ArrayMarkerProperties arrayMarkerPro_index = new ArrayMarkerProperties(); // index

	// Array Marker
	private ArrayMarker arrayMarker_i; // i
	private ArrayMarker arrayMarker_index; // i + 1

	// Source Code
	private SourceCode sc;
	private SourceCode sc_desc; 
	private SourceCode title; 
	
	///
	private Text counter;
	private Text kleiner;
	private Text startwert;
	
	// Animation Author
	String AnimationAuthor = "Sami Graja, Hasan Tercan";
	
	// Algorithm Name
	String AlgorithmName = "SwapSorter";
	
	// Array
	//static int[] array = new int[] { 13, 17, 45, 21, 33, 15, 20, 19 };// Array
																		// Elements
	// Array
	int[] arrayData;
	Color Element_Color = Color.RED; // Element Color
	Color Fill_Color = Color.YELLOW; // Fill Color
	Color Cell_Highlight = Color.cyan; // CELLHIGHLIGHT_PROPERTY
	
	boolean array_filled = true; // Filled initialized true
	
	// marker for i: ORANGE with label 'i'
	String label_i = "i";
	Color color_i_marker = Color.ORANGE;
	
	// marker for index: MAGENTA with label 'index'
	String label_index = "index";
	Color color_index_marker = Color.MAGENTA;
	

	// Timing
	Timing defaultTiming = new TicksTiming(15);
	Timing defaultTiming_swap = new TicksTiming(300);

	// Konstruktor 
	public SwapSorter() {
		this.init();
	}
	public SwapSorter(Language language) {
		lang = language;
		lang.setStepMode(true);
	}
	public void init() {
		lang = this.language;
		lang.setStepMode(true);
}

	public void showSourceCodedescription() {
		// set the visual properties for the title code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 20));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		// now, create the source code entity
		title = lang.newSourceCode(new Coordinates(400, 10), "sourceCodeDesc",
				null, scProps);

		title.addCodeLine("SwapSorter", null, 0, null);
		
		
		// add a code line
		// parameters: code itself; name (can be null); indentation level;
		// display options
		SourceCodeProperties scProps1 = new SourceCodeProperties();
		scProps1.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.ROMAN_BASELINE, 14));
		scProps1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		sc_desc = lang.newSourceCode(new Coordinates(30, 30), "sourceCodeDesc", null,
				scProps1);
		
		sc_desc.addCodeLine("", null, 14, null);
		sc_desc.addCodeLine("", null, 14, null);
		sc_desc.addCodeLine("Der Algorithmus in Worten", null, 0, null);
		sc_desc.addCodeLine("", null, 0, null);
		sc_desc
				.addCodeLine(
						"Die Idee von Swap-Sort ist, von jedem Element eines Arrays A(1..n) die Anzahl m der kleineren",
						null, 3, null);
		sc_desc
				.addCodeLine(
						"Werte (die in A sind) zu errechnen und das Element dann mit dem Element in A(m+1) zu vertauschen.",
						null, 3, null);
		sc_desc
				.addCodeLine(
						"Somit ist sichergestellt, dass das ausgetauschte Element bereits an der richtigen, also finalen Stelle steht.",
						null, 3, null);
		sc_desc.addCodeLine("", null, 0, null);
		sc_desc
				.addCodeLine(
						"Nachteil dieses Algorithmus ist, dass jedes Element nur einmal vorkommen darf, da sonst keine Terminierung erfolgt.",
						null, 3, null);
	}

	public IntArray showSourceCode(int[] arrayContents ) {
		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.ROMAN_BASELINE, 14));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		// now, create the source code entity
		sc = lang.newSourceCode(new Coordinates(30, 30), "sourceCode", null,
				scProps);
		// add a code line
		// parameters: code itself; name (can be null); indentation level;
		// display options
		sc.addCodeLine("public class SwapSorter {", null, 0, null);
		sc.addCodeLine("public void sort(int[] sortMe) {", null, 1, null);
		sc.addCodeLine("int startwert = 0;", null, 3, null);
		sc
				.addCodeLine("while (startwert < sortMe.length - 1) {", null,
						3, null);
		sc.addCodeLine("int kleinere = countSmallerOnes(sortMe, startwert);",
				null, 5, null);
		sc.addCodeLine("if (kleinere > 0) {", null, 5, null);
		sc.addCodeLine("swap(startwert,startwert + kleinere);", null, 7, null);
		sc.addCodeLine("}", null, 5, null);
		sc.addCodeLine("else {", null, 5, null);
		sc.addCodeLine("startwert++;", null, 7, null);
		sc.addCodeLine("}", null, 5, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine(" ", null, 1, null);
		sc.addCodeLine(" ", null, 1, null);
		sc
				.addCodeLine(
						"private int countSmallerOnes(final int[] countHere, final int index) {",
						null, 1, null);
		sc.addCodeLine("int counter = 0;", null, 3, null);
		sc.addCodeLine("for (int i = index + 1; i < countHere.length; i++) {",
				null, 3, null);
		sc.addCodeLine("if (countHere[index] > countHere[i]) {", null, 5, null);
		sc.addCodeLine("counter++;", null, 7, null);
		sc.addCodeLine("}", null, 5, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("return counter;", null, 3, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		
		// Properties fuer das Text kleiner
		this.kleiner = lang.newText(new Coordinates(520,120), "kleiner = ","kleiner = ", null);
		kleiner.setFont(new Font("SansSerif",1,16), null, null);
		kleiner.hide();
		// Properties fuer das Text counter
		this.counter = lang.newText(new Coordinates(300,400), "counter = ", "counter = ", null);
		counter.setFont(new Font("SansSerif",1,16), null, null);
		counter.hide();
		// Properties fuer das Text startwert
		this.startwert = lang.newText(new Coordinates(300,250), "startwert = ", "startwert = ", null);
		startwert.setFont(new Font("SansSerif",1,16), null, null);
		startwert.hide();
		
		// create array properties with default values
		arrayProps = new ArrayProperties();
		// Redefine properties:
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Element_Color); // color Red
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Cell_Highlight); // color cyan
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Fill_Color); // fill
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
	        Color.BLUE);
		
		IntArray array = lang.newIntArray(new Coordinates(450, 200),
				arrayContents, "array", null, arrayProps);
		
		arrayMarker_i= lang.newArrayMarker(array, 0, "i", null, arrayMarkerPro_i);
		arrayMarker_i.hide();
		arrayMarker_index= lang.newArrayMarker(array,0, "index", null, arrayMarkerPro_index);
		arrayMarker_index.hide();
		
		return array;
	}

	public void sort(int[] arrayContents) {
		showSourceCodedescription();
		lang.nextStep(" hier kann man die Beschreibung sehen");
		sc_desc.hide();
		
		IntArray array = showSourceCode(arrayContents);
		
		lang.nextStep("hier wird die Beschreibung versteckt ");

		// ligne 1
		sc.highlight(0);
		lang.nextStep(" Die erste Linie wird mit rote Farbe angezeigt");

		// ligne 2
		sc.unhighlight(0);
		sc.highlight(1);
		lang.nextStep("Die erste Linie wird mit der vorherige Farbe angezeigt + Die zweite Linie wird mit rote Farbe angezeigt");

		// ligne 3
		sc.unhighlight(1);
		sc.highlight(2);
		this.startwert.setText("startwert = 0", null, null);
		this.startwert.show();
		lang.nextStep();
		int startwert = 0;
		// ligne 4
		sc.unhighlight(2);
		sc.highlight(3);
		sc.unhighlight(3);
		while (startwert < array.getLength() - 1) {
			// ligne 5
			sc.highlight(4);
			lang.nextStep();
			// int kleiner=countSmallerOnes(array, startwert);
			// function countSmallerOnes
			sc.unhighlight(4);
			sc.highlight(15);
			lang.nextStep();
			// /
			sc.unhighlight(15);
			sc.highlight(16);
			this.counter.setText("counter = 0", null, null);
			this.counter.show();
			lang.nextStep();
			// //
			sc.unhighlight(16);
			sc.highlight(17);
			lang.nextStep();
			int counter = 0;
			arrayMarker_i.move(startwert + 1, null, defaultTiming);
			arrayMarker_i.show();
			arrayMarker_index.move(startwert, null, defaultTiming);
			arrayMarker_index.show();
			lang.nextStep();
			for (; arrayMarker_i.getPosition() < array.getLength(); arrayMarker_i.increment(null,
					defaultTiming)) {
				lang.nextStep();
				sc.unhighlight(17);
				sc.highlight(18);
				lang.nextStep();
				sc.unhighlight(18);
				if (array.getData(startwert) > array.getData(arrayMarker_i.getPosition())) {
					sc.highlight(19);
					counter++;
					this.counter.setText("counter = " + counter, null, null);
					lang.nextStep();
					sc.unhighlight(19);
				}
				sc.highlight(17);
			}
			int kleinere = counter;
			this.kleiner.setText("kleiner = counter = " + counter, null, null);
			this.kleiner.show();
			// /
			arrayMarker_i.hide();
			arrayMarker_index.hide();
			// / ligne 6
			sc.unhighlight(17);
			sc.unhighlight(15);
			sc.highlight(22);
			sc.highlight(4);
			lang.nextStep();
			sc.unhighlight(22);
			sc.unhighlight(4);
			sc.highlight(5);
			lang.nextStep();
			sc.unhighlight(5);
			// ligne 7
			if (kleinere > 0) {

				sc.highlight(6);
				lang.nextStep();
				// array.swap(startwert,(startwert + kleinere), null,
				// defaultTiming_swap);
				array.highlightCell(startwert, startwert, null, defaultTiming);
				array.highlightCell((startwert + kleinere),
						(startwert + kleinere), null, defaultTiming);
				lang.nextStep();
				array.swap(startwert, (startwert + kleinere), null,
						defaultTiming_swap);
				array
						.unhighlightCell(startwert, startwert, null,
								defaultTiming);
				array.unhighlightCell((startwert + kleinere),
						(startwert + kleinere), null, defaultTiming);
//				this.counter.setText("counter = ", null, null);	
//				this.kleiner.setText("kleiner = ", null, null);
				this.counter.hide();
				this.kleiner.hide();
				lang.nextStep();
				lang.nextStep(); // change step
				sc.unhighlight(6);
			}
			// ligne 9
			else {
				sc.unhighlight(5);
				sc.highlight(8);
				this.counter.hide();
				this.kleiner.hide(); 
				lang.nextStep();
				// /
				sc.unhighlight(8);
				sc.highlight(9);
				startwert++;
				this.startwert.setText("startwert = "+startwert, null, null);
				lang.nextStep();
				
				sc.unhighlight(9);
			}

			sc.highlight(3);
			lang.nextStep();
			sc.unhighlight(3);
		}
		
		this.startwert.hide();
		sc.unhighlight(3);
		lang.nextStep();
		array.highlightCell(0, (array.getLength() - 1), null, defaultTiming);
		lang.nextStep();
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
			if(primitives.get("array") != null) {
				this.arrayData = (int[]) primitives.get("array");
				//arrayProps = (ArrayProperties)props.getPropertiesByName("array");
				this.arrayMarkerPro_i = (ArrayMarkerProperties) props.getPropertiesByName("i");
				this.arrayMarkerPro_index = (ArrayMarkerProperties) props.getPropertiesByName("index");
					
			} else {
				this.arrayData = new int[] { 101, 75, 99, 3, 1, 100 }; 
			   
				// ArrayMarkerProperties i
				this.arrayMarkerPro_i = new ArrayMarkerProperties();
				arrayMarkerPro_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_i);   
				arrayMarkerPro_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_i_marker);
				
				// ArrayMarkerProperties i+1
				this.arrayMarkerPro_index = new ArrayMarkerProperties();
				arrayMarkerPro_index.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_index);   
				arrayMarkerPro_index.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_index_marker);
				
				
				
		   
			}
				
			
			this.sort(this.arrayData);
			return this.language.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Swap Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Sami Graja, Hasan Tercan";
	}

	@Override
	public String getCodeExample() { 
		return 	"public class SwapSorter {\n"+
				"  public void sort(int[] sortMe) {\n"+
				"    int startwert = 0;\n"+
				"    while (startwert &lt; sortMe.length - 1) {\n"+
				"       int kleinere = countSmallerOnes(sortMe, startwert);\n"+
				"       if (kleinere &gt; 0) {\n"+
				"         swap(startwert,startwert + kleinere);\n"+
				"       }\n"+
				"       else {\n"+
				"         startwert++;\n"+
				"       }\n"+
				"    }\n"+
				"  }\n"+
				" \n"+
				" \n"+
				"   private int countSmallerOnes(final int[] countHere, final int index) {\n"+
				"     int counter = 0;\n"+
				"     for (int i = index + 1; i &lt; countHere.length; i++) {\n"+
				"       if (countHere[index] &gt; countHere[i]) {\n"+
				"          counter++;\n"+
				"       }\n"+
				"     }\n"+
				"     return counter;\n"+
				"   }\n"+
				"}\n"; // to give readers an impression
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Die Idee von Swap-Sort ist, von jedem Element eines Arrays A(1..n) die Anzahl m der kleineren\n"+
		"Werte (die in A sind) zu errechnen und das Element dann mit dem Element in A(m+1) zu vertauschen.\n"+
		"Somit ist sichergestellt, dass das ausgetauschte Element bereits an der richtigen, also finalen Stelle steht."; // description
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);// this is
																		// about
																		// sorting!
	}

	@Override
	public String getName() {
		return "SwapSorter"; // the title to be displayed
	}

	@Override
	public String getOutputLanguage() {
		return JAVA_OUTPUT;
	}

}
