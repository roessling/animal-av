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
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class ShakerSorter implements Generator {
	private Language language = new AnimalScript("ShakerSort", "Sami Graja, Hasan Tercan", 800, 600);
	private Language lang ;
	private ArrayProperties arrayProps;
	// Array Marker Properties
	private ArrayMarkerProperties arrayMarkerPro_i = new ArrayMarkerProperties(); // i
	private ArrayMarkerProperties arrayMarkerPro_i_plus1 = new ArrayMarkerProperties(); // i + 1
	private ArrayMarkerProperties arrayMarkerPro_i_minus1 = new ArrayMarkerProperties(); // i - 1
	private ArrayMarkerProperties arrayMarkerPro_Start = new ArrayMarkerProperties(); // start
	private ArrayMarkerProperties arrayMarkerPro_Ende = new ArrayMarkerProperties(); // ende

	// Array Marker
	private ArrayMarker arrayMarker_i; // i
	private ArrayMarker arrayMarker_i_plus1; // i + 1
	private ArrayMarker arrayMarker_i_minus1; // i - 1
	private ArrayMarker arrayMarker_Start; // start
	private ArrayMarker arrayMarker_Ende; // ende

	// Source Code
	private SourceCode sc;
	private SourceCode sc_desc; 
	private SourceCode title; 
	
	
	// Animation Author
	String AnimationAuthor = "Sami Graja, Hasan Tercan";
	
	// Algorithm Name
	String AlgorithmName = "ShakerSorter";
	
	// Array
	int[] arrayData;
	Color Element_Color = Color.RED; // Element Color
	Color Fill_Color = Color.YELLOW; // Fill Color
	Color Cell_Highlight = Color.cyan; // CELLHIGHLIGHT_PROPERTY
	
	// marker for i: ORANGE with label 'i'
	String label_i = "i";
	Color color_i_marker = Color.ORANGE;
	
	// marker for i+1: MAGENTA with label 'i+1'
	String label_i_plus = "i+1";
	Color color_i_plus_marker = Color.MAGENTA;
	
	// marker for i-1: MAGENTA with label 'i-1'
	String label_i_minus = "i-1";
	Color color_i_minus_marker = Color.MAGENTA;
	
	// marker for Start: green with label 'start'
	String label_start = "Start";
	Color color_start_marker = Color.GREEN;
	
	// marker for Ende: red with label 'ende'
	String label_ende = "Ende";
	Color color_ende_marker = Color.RED;
	
	// Timing
	Timing defaultTiming = new TicksTiming(15);
	Timing defaultTiming_swap = new TicksTiming(300);

	// method
	public ShakerSorter() {
		this.init();
	}
	public ShakerSorter(Language language) {
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

		title.addCodeLine(" Shaker Sort ", null, 0, null);

		// set the visual properties for the sourcedescription code
		SourceCodeProperties scProps1 = new SourceCodeProperties();
		scProps1.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 20));
		scProps1
				.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		
		// set the visual properties for the source code
		SourceCodeProperties scProps2 = new SourceCodeProperties();
		scProps2.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 16));
		scProps2
				.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		// now, create the source code entity
		sc_desc = lang.newSourceCode(new Coordinates(30, 120),
				"sourceCodeDesc", null, scProps2);
		// add a code line
		// parameters: code itself; name (can be null); indentation level;
		// display options
		sc_desc.addCodeLine("Der Algorithmus in Worten", null, 0, null);
		sc_desc.addCodeLine("", null, 0, null);
		sc_desc
				.addCodeLine(
						"Das zu sortierende Feld wird abwechselnd nach oben und nach unten durchlaufen.",
						null, 3, null);
		sc_desc
				.addCodeLine(
						"Dabei werden jeweils zwei benachbarte Elemente verglichen und gegebenenfalls vertauscht.",
						null, 3, null);
		sc_desc
				.addCodeLine(
						"Durch diese Bidirektionalitaet kommt es zu einem schnellerem Absetzen von grossen bzw. kleinen Elementen.",
						null, 3, null);

	}

	public IntArray showSourceCode( int[] arrayContents) {
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
		
		sc.addCodeLine("public int [] sortieren(int [] liste) {", null,
						0, null);
		sc.addCodeLine("boolean sortiert = false;", null, 1, null);
		sc.addCodeLine("boolean vorwaerts = true;", null, 1, null);
		sc.addCodeLine("int start = 0;", null, 1, null);
		sc.addCodeLine("int ende = liste.length-1;", null, 1, null);
		sc.addCodeLine("while (start<ende && !sortiert) {", null, 1, null);
		sc.addCodeLine("sortiert = true;", null, 3, null);
		sc.addCodeLine("if (vorwaerts) {", null, 3, null);
		sc.addCodeLine("for (int i=start; i<ende; i++) {", null, 5, null);
		sc.addCodeLine("if (liste[i] > liste[i+1]){", null, 7, null);
		sc.addCodeLine("swap(liste[i],liste[i+1]);", null, 9, null);
		sc.addCodeLine("sortiert = false;", null, 9, null);
		sc.addCodeLine("}", null, 7, null);
		sc.addCodeLine("}", null, 5, null);
		sc.addCodeLine("ende--;", null, 5, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("else {", null, 3, null);
		sc.addCodeLine("for (int i=ende; i>start; i--) {", null, 5, null);
		sc.addCodeLine("if (liste[i] < liste[i-1]) {", null, 7, null);
		sc.addCodeLine("swap(liste[i],liste[i-1]);", null, 9, null);
		sc.addCodeLine(" sortiert = false;", null, 9, null);
		sc.addCodeLine("}", null, 7, null);
		sc.addCodeLine("}", null, 5, null);
		sc.addCodeLine("start++;", null, 5, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("vorwaerts = !vorwaerts;", null, 3, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("return liste;", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		
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
		arrayMarker_i_plus1= lang.newArrayMarker(array,0, "i+1", null, arrayMarkerPro_i_plus1);
		arrayMarker_i_plus1.hide();
		arrayMarker_i_minus1= lang.newArrayMarker(array, 0, "i-1", null, arrayMarkerPro_i_minus1);
		arrayMarker_i_minus1.hide();
		arrayMarker_Start= lang.newArrayMarker(array, 0, "start", null, arrayMarkerPro_Start);
		
		arrayMarker_Ende= lang.newArrayMarker(array, arrayContents.length-1, "ende", null, arrayMarkerPro_Ende);
		
		
		return array;
	}

	public void sort(int[] arrayContents) {
		showSourceCodedescription();
		lang.nextStep(" hier kann man die Beschreibung sehen");
		sc_desc.hide();
	
		IntArray array = showSourceCode(arrayContents);
		//showSourceCode();
		// 1 ligne
		sc.highlight(0);
		lang.nextStep("hier wird die Beschreibung versteckt + Die erste Linie wird mit rote Farbe angezeigt");
		// 2 ligne
		sc.unhighlight(0);
		sc.highlight(1);
		boolean sortiert = false;
		lang.nextStep("Die erste Linie wird mit der vorherige Farbe angezeigt + Die zweite Linie wird mit rote Farbe angezeigt");
		// 3 ligne
		sc.unhighlight(1);
		sc.highlight(2);
		boolean vorwaerts = true;
		lang.nextStep("Die 2. Linie wird mit der vorherige Farbe angezeigt +  Die 3. Linie wird mit rote Farbe angezeigt");
		// 4 ligne
		sc.unhighlight(2);
		sc.highlight(3);
		
		lang.nextStep("Die 3. Linie wird mit der vorherige Farbe angezeigt +  Die 4. Linie wird mit rote Farbe angezeigt");

		// 5 ligne
		sc.unhighlight(3);
		sc.highlight(4);
		
		lang.nextStep("Die 4. Linie wird mit der vorherige Farbe angezeigt +  Die 5. Linie wird mit rote Farbe angezeigt");

		// 6 ligne
		sc.unhighlight(4);
		sc.highlight(5);
		lang.nextStep("Die 5. Linie wird mit der vorherige Farbe angezeigt +  Die 6. Linie wird mit rote Farbe angezeigt");

		// show
		// i
		// marker
	
		while (arrayMarker_Start.getPosition() < arrayMarker_Ende.getPosition() && !sortiert) {
			// 7 ligne
			sortiert = true;
			sc.unhighlight(5);
			sc.highlight(6);
			lang.nextStep("Die 6. Linie wird mit der vorherige Farbe angezeigt +  Die 7. Linie wird mit rote Farbe angezeigt");

			// 8 ligne
			sc.unhighlight(6);
			sc.highlight(7);
			lang.nextStep("Die 7. Linie wird mit der vorherige Farbe angezeigt + Die 8. Linie wird mit rote Farbe angezeigt");


			// to show i
			// marker
			if (vorwaerts) {

				// 9 ligne
				sc.unhighlight(7);
				sc.highlight(8);
				lang.nextStep("Die 8. Linie wird mit der vorherige Farbe angezeigt + Die 9. Linie wird mit rote Farbe angezeigt");

				arrayMarker_i.move(arrayMarker_Start.getPosition(), null, defaultTiming);
				arrayMarker_i.show();
				for (; arrayMarker_i.getPosition() < arrayMarker_Ende.getPosition(); arrayMarker_i.increment(null,
						defaultTiming)) {

					lang.nextStep("Sort from "+arrayMarker_Start.getPosition() +" to "+arrayMarker_Ende.getPosition());
					// 10 ligne
					sc.unhighlight(8);
					sc.highlight(9);
					arrayMarker_i_plus1.move((arrayMarker_i.getPosition() + 1), null, defaultTiming);
					arrayMarker_i_plus1.show();
					lang.nextStep();
					sc.unhighlight(9);
					if (array.getData(arrayMarker_i.getPosition()) > array.getData(arrayMarker_i
							.getPosition() + 1)) {
						// 11 ligne
						sc.highlight(10);
						array.highlightElem((arrayMarker_i.getPosition()),
								arrayMarker_i_plus1.getPosition(), null, defaultTiming);
						lang.nextStep();
						array.swap((arrayMarker_i.getPosition()), arrayMarker_i_plus1.getPosition(), null,
								defaultTiming_swap);
						
						lang.nextStep();
						array.unhighlightElem((arrayMarker_i.getPosition()), arrayMarker_i_plus1
								.getPosition(), null, defaultTiming);
						// 12 ligne
						arrayMarker_i_plus1.hide();
						sc.unhighlight(10);
						sc.highlight(11);
						sortiert = false;
						lang.nextStep();
						sc.unhighlight(11);
						sc.highlight(8);
						lang.nextStep();
					}
				}
				array.highlightCell(arrayMarker_Ende.getPosition(),arrayContents.length-1, null, defaultTiming);
				arrayMarker_Ende.move((arrayMarker_Ende.getPosition() - 1), null, defaultTiming);
				sc.unhighlight(8);
				sc.highlight(14);
				arrayMarker_i_plus1.hide();
				lang.nextStep();
				sc.unhighlight(14);

			} else {
				sc.unhighlight(7);
				sc.highlight(16);
				lang.nextStep();
				// 18 ligne
				sc.unhighlight(16);
				sc.highlight(17);
				lang.nextStep();
				//
				arrayMarker_i.move(arrayMarker_Ende.getPosition(), null, defaultTiming);
				arrayMarker_i.show();
				lang.nextStep();
				for (; arrayMarker_i.getPosition() > arrayMarker_Start.getPosition(); arrayMarker_i.decrement(null,
						defaultTiming)) {
					lang.nextStep();
					// 19 ligne
					sc.unhighlight(17);
					sc.highlight(18);
					lang.nextStep();
					sc.unhighlight(18);
					arrayMarker_i_minus1.move((arrayMarker_i.getPosition() - 1), null, defaultTiming);
					arrayMarker_i_minus1.show();
					lang.nextStep();
					if (array.getData(arrayMarker_i.getPosition()) < array.getData(arrayMarker_i
							.getPosition() - 1)) {
						sc.highlight(19);
						lang.nextStep();
						array.highlightElem(arrayMarker_i_minus1.getPosition(),
								(arrayMarker_i.getPosition()), null, defaultTiming);
						lang.nextStep();
						array.swap((arrayMarker_i.getPosition()), arrayMarker_i_minus1.getPosition(), null,
								defaultTiming_swap);
						
						lang.nextStep();
						array.unhighlightElem(arrayMarker_i_minus1.getPosition(), (arrayMarker_i
								.getPosition()), null, defaultTiming);
						arrayMarker_i_minus1.hide();
						sortiert = false;
						sc.unhighlight(19);
						sc.highlight(20);
						lang.nextStep();
					}

				}
				sc.unhighlight(20);
				sc.highlight(23);
				array.highlightCell(0, arrayMarker_Start.getPosition(), null, defaultTiming);
				arrayMarker_Start.move((arrayMarker_Start.getPosition() + 1), null, defaultTiming);
				arrayMarker_i_minus1.hide();
				lang.nextStep();
				sc.unhighlight(23);

			}
			arrayMarker_i_minus1.hide();
			arrayMarker_i_plus1.hide();
			sc.highlight(25);
			vorwaerts = !vorwaerts;
			lang.nextStep();
			sc.unhighlight(25);
			sc.highlight(5);
			arrayMarker_i.hide();
			lang.nextStep();
		}
		sc.unhighlight(5);
		arrayMarker_Start.hide();
		arrayMarker_Ende.hide();
		array.highlightCell(0, (array.getLength() - 1), null, defaultTiming);
		sc.highlight(27);
		lang.nextStep();

	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		if(primitives.get("array") != null) {
			this.arrayData = (int[]) primitives.get("array");
			//arrayProps = (ArrayProperties)props.getPropertiesByName("array");
			this.arrayMarkerPro_i = (ArrayMarkerProperties) props.getPropertiesByName("i");
			this.arrayMarkerPro_i_plus1 = (ArrayMarkerProperties) props.getPropertiesByName("ip1");
			this.arrayMarkerPro_i_minus1 = (ArrayMarkerProperties) props.getPropertiesByName("im1");
			this.arrayMarkerPro_Start = (ArrayMarkerProperties) props.getPropertiesByName("start");	
			this.arrayMarkerPro_Ende = (ArrayMarkerProperties) props.getPropertiesByName("ende");	
		} else {
			this.arrayData = new int[] { 101, 75, 99, 3, 1, 100 }; 

			// create array properties with default values
			
		   
			// ArrayMarkerProperties i
			this.arrayMarkerPro_i = new ArrayMarkerProperties();
			arrayMarkerPro_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_i);   
			arrayMarkerPro_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_i_marker);
			
			// ArrayMarkerProperties i+1
			this.arrayMarkerPro_i_plus1 = new ArrayMarkerProperties();
			arrayMarkerPro_i_plus1.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_i_plus);   
			arrayMarkerPro_i_plus1.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_i_plus_marker);
			
			// ArrayMarkerProperties i-1
			this.arrayMarkerPro_i_minus1 = new ArrayMarkerProperties();
			arrayMarkerPro_i_minus1.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_i_minus);   
			arrayMarkerPro_i_minus1.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_i_minus_marker);
			
			// ArrayMarkerProperties start
			this.arrayMarkerPro_Start = new ArrayMarkerProperties();
			arrayMarkerPro_Start.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_start);   
			arrayMarkerPro_Start.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_start_marker);
			
			// ArrayMarkerProperties ende
			this.arrayMarkerPro_Ende = new ArrayMarkerProperties();
			arrayMarkerPro_Ende.set(AnimationPropertiesKeys.LABEL_PROPERTY, label_ende);   
			arrayMarkerPro_Ende.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_ende_marker);
		
	   
		}
			
		
		this.sort(this.arrayData);
		return this.language.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Shaker Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Sami Graja, Hasan Tercan";
	}

	

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Das zu sortierende Feld wird abwechselnd nach oben und nach unten durchlaufen.\n"+
		"Dabei werden jeweils zwei benachbarte Elemente verglichen und gegebenenfalls vertauscht.\n"+
		"Durch diese Bidirektionalit&auml;t kommt es zu einem schnellerem Absetzen von gro&szlig;en bzw. kleinen Elementen."; // description
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT); // this is
																		// about
																		// sorting!
	}

	@Override
	public String getName() {
		return "ShakerSorter"; // the title to be displayed
	}

	@Override
	public String getOutputLanguage() {
		return JAVA_OUTPUT;
	}
	@Override
	public String getCodeExample() { 
		return "public int [] sortieren(int [] liste) {\n"+
			   " boolean sortiert = false;\n"+
			   " boolean vorwaerts = true;\n"+
			   " int start = 0;\n"+
			   " int ende = liste.length-1;\n"+
			   " while (start&lt;ende && !sortiert) {\n"+
			   "   sortiert = true;\n"+
			   "   if (vorwaerts) {\n"+
			   "     for (int i=start; i&lt;ende; i++) {\n"+
			   "       if (liste[i] &gt; liste[i+1]){\n"+
			   "         swap(liste[i],liste[i+1]);\n"+
			   "         sortiert = false;\n"+
			   "       }\n"+
			   "      }\n"+
			   "      ende--;\n"+
			   "    }\n"+
			   "    else {\n"+
			   "      for (int i=ende; i&gt;start; i--) {\n"+
			   "        if (liste[i] &lt; liste[i-1]) {\n"+
			   "          swap(liste[i],liste[i-1]);\n"+
			   "          sortiert = false;\n"+
			   "         }\n"+
			   "      }\n"+
			   "      start++;\n"+
			   "    }\n"+
			   "    vorwaerts = !vorwaerts;\n"+
			   "  }\n"+
			   " return liste;\n"+
			   "}";
	}

}
