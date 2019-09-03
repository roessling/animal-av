package generators.sorting.shakersort;

/**
 * Abgabe für 6
 */
import generators.AnnotatedAlgorithm;
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
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * Die Aufgabe wurde als Gruppe bearbeitet
 * 1. Oksana Kolach 1169296
 * 2. Michael Drescher 1200744
 * 
 * @version 2
 */
public class ShakerSortGenerator extends AnnotatedAlgorithm implements Generator {

	private IntArray array;
	private Language l; 
	public static int[] lastHighlight = null; 
	
	
	/**
	 * Parameter für die Animation
	 */
	private Timing markerMoveDelay = new MsTiming(100); 
	private Timing markerMoveDuration = new MsTiming(300); 
	private Timing swapDelay = new MsTiming(100); 
	private Timing swapDuration = new MsTiming(300); 
	private Timing highlightDelay = new MsTiming(100); 
	private Timing highlightDuration = new MsTiming(300); 

	ArrayProperties aProps = new ArrayProperties(); 
	private int[] arrayData = new int[] {55, 7, 78, 12, 42, 33, 17, 12, 93}; 
	
	ArrayMarkerProperties markerLinksProps = new ArrayMarkerProperties(); 
	ArrayMarkerProperties markerRechtsProps = new ArrayMarkerProperties(); 
	ArrayMarkerProperties markerFertigProps = new ArrayMarkerProperties(); 
	ArrayMarkerProperties markerIProps = new ArrayMarkerProperties(); 

	public ShakerSortGenerator() {
	}
	
	private void highlighLineOnly(int... line) {
		
		if (lastHighlight != null)
			for (int h: lastHighlight)
				sourceCode.unhighlight(h); 
		
		lastHighlight = line; 
		
		if (line != null) {
			if (line.length == 1) {
				exec(String.valueOf(line[0])); 
			} else {
				for (int h: lastHighlight) {
					sourceCode.highlight(h); 
					annoMan.exec(annotations.get(String.valueOf(h))); 
				}
			}
		}
		
	}
	
	public void sort() {
		boolean austausch;
		
		highlighLineOnly(0); 
		l.nextStep(); 
		
		highlighLineOnly(1, 2, 3, 4); 
		ArrayMarker links = l.newArrayMarker(array, 1, "links", null, markerLinksProps); 
		ArrayMarker rechts = l.newArrayMarker(array, array.getLength() - 1, "rechts", null, markerRechtsProps); 
		ArrayMarker fertig = l.newArrayMarker(array, rechts.getPosition(), "fertig", null, markerFertigProps); 
		ArrayMarker i = l.newArrayMarker(array, 0, "i", null, markerIProps); 
		i.moveOutside(new MsTiming(1), new TicksTiming(1)); 
		l.nextStep(); 
		
		highlighLineOnly(5); 
		l.nextStep(); 
		
		do {
			
			highlighLineOnly(6); 
			l.nextStep(); 
			austausch = false;
			
			highlighLineOnly(7); 
			for (i.move(rechts.getPosition(), markerMoveDelay, markerMoveDuration); i.getPosition() >= links.getPosition(); i.decrement(markerMoveDelay, markerMoveDuration)) {
				l.nextStep(); 
				highlighLineOnly(8); 
				if (compare(i.getPosition(), i.getPosition() - 1) < 0) {
					highlighLineOnly(9); 
					austausch = true;
					l.nextStep(); 
					highlighLineOnly(10);
					fertig.move(i.getPosition(), markerMoveDelay, markerMoveDuration); 
					l.nextStep(); 
					highlighLineOnly(11, 12, 13); 
					array.swap(i.getPosition() - 1, i.getPosition(), swapDelay, swapDuration); 
					l.nextStep(); 
				}
				highlighLineOnly(14); 
				l.nextStep(); 
				highlighLineOnly(7); 
			}	
			highlighLineOnly(15); 
			i.moveOutside(markerMoveDelay, markerMoveDuration); 
			l.nextStep(); 
			highlighLineOnly(16); 
			links.move(fertig.getPosition() + 1, markerMoveDelay, markerMoveDuration); 
			l.nextStep(); 
			array.highlightCell(0, fertig.getPosition() - 1, highlightDelay, highlightDuration); 
			l.nextStep(); 
			highlighLineOnly(17); 
			for (i.move(links.getPosition(), markerMoveDelay, markerMoveDuration); i.getPosition() <= rechts.getPosition(); i.increment(markerMoveDelay, markerMoveDuration)) {
				l.nextStep(); 
				highlighLineOnly(18); 
				if (compare(i.getPosition(), i.getPosition() - 1) < 0) {
					highlighLineOnly(19); 
					austausch = true;
					l.nextStep(); 
					highlighLineOnly(20); 
					fertig.move(i.getPosition(), markerMoveDelay, markerMoveDuration); 
					l.nextStep(); 
					highlighLineOnly(21, 22, 23); 
					array.swap(i.getPosition() - 1, i.getPosition(), swapDelay, swapDuration); 
					l.nextStep(); 
				}
				highlighLineOnly(24); 
				l.nextStep(); 
				highlighLineOnly(17); 
			}
			highlighLineOnly(25); 
			i.moveOutside(markerMoveDelay, markerMoveDuration); 
			l.nextStep();
			highlighLineOnly(26); 
			rechts.move(fertig.getPosition() - 1, markerMoveDelay, markerMoveDuration); 
			l.nextStep(); 
			array.highlightCell(fertig.getPosition(), array.getLength() - 1, highlightDelay, highlightDuration); 
			l.nextStep(); 
			
			highlighLineOnly(27); 
			l.nextStep(); 
			if (austausch) {
				highlighLineOnly(5); 
				l.nextStep(); 
			}
			
		} while (austausch);
	
		highlighLineOnly(28); 
		l.nextStep(); 
		highlighLineOnly(null); 
	}
	
	public int compare(int p1, int p2) {
		
		array.highlightCell(p1, highlightDelay, highlightDuration); 
		array.highlightCell(p2, highlightDelay, highlightDuration); 
		l.nextStep(); 
		array.unhighlightCell(p1, highlightDelay, highlightDuration); 
		array.unhighlightCell(p2, highlightDelay, highlightDuration);
		l.nextStep(); 
		
		if (array.getData(p1) < array.getData(p2))
			return -1; 
		if (array.getData(p1) > array.getData(p2))
			return 1; 
		return 0;
	}
		
	
	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		init(); 
		
		markerLinksProps = (ArrayMarkerProperties)props.getPropertiesByName("marker links"); 
		markerRechtsProps = (ArrayMarkerProperties)props.getPropertiesByName("marker rechts"); 
		markerFertigProps = (ArrayMarkerProperties)props.getPropertiesByName("marker fertig"); 
		markerIProps = (ArrayMarkerProperties)props.getPropertiesByName("marker i"); 
		aProps = (ArrayProperties)props.getPropertiesByName("array style"); 
		
		markerMoveDelay = new MsTiming((Integer)primitives.get("marker move delay (ms)"));
		markerMoveDuration = new MsTiming((Integer)primitives.get("marker move duration (ms)"));
		swapDelay = new MsTiming((Integer)primitives.get("swap delay (ms)"));
		swapDuration = new MsTiming((Integer)primitives.get("swap duration (ms)"));
		highlightDelay = new MsTiming((Integer)primitives.get("highlight delay (ms)"));
		highlightDuration = new MsTiming((Integer)primitives.get("highlight duration (ms)"));
		
		arrayData = (int[])primitives.get("array data"); 
		
		Font headerFont = new Font("SansSerif", Font.BOLD, 20); 
		TextProperties headerProps = new TextProperties(); 
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont); 
		l.newText(new Coordinates(30, 30), "Shakersort", "header", null, headerProps);
		
		SourceCodeProperties sourceProps = new SourceCodeProperties(); 
		sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED); 
		sourceCode = l.newSourceCode(new Offset(0, 200, "header", AnimalScript.DIRECTION_SW), "source", null, sourceProps); 
		/*
		sourceCode.addCodeLine("static <E extends Comparable<? super E>> void shakerSort(E[] sammlung) {", "1", 0, null); 
		sourceCode.addCodeLine("boolean austausch;", "2", 1, null); 
		sourceCode.addCodeLine("int links = 1;", "3", 1, null); 
		sourceCode.addCodeLine("int rechts = sammlung.length-1;", "4", 1, null); 
		sourceCode.addCodeLine("int fertig = rechts;", "5", 1, null); 
		sourceCode.addCodeLine("do {", "6", 1, null); 
		sourceCode.addCodeLine("austausch = false;", "1", 2, null); 
		sourceCode.addCodeLine("for (int i = rechts; i >= links; i--) {", "7", 2, null); 
		sourceCode.addCodeLine("if (sammlung[i].compareTo(sammlung[i-1]) < 0) {", "8", 3, null); 
		sourceCode.addCodeLine("austausch = true;", "9", 4, null); 
		sourceCode.addCodeLine("fertig = i;", "10", 4, null); 
		sourceCode.addCodeLine("final E temp = sammlung[i-1];", "11", 4, null); 
		sourceCode.addCodeLine("sammlung[i-1] = sammlung[i];", "12", 4, null); 
		sourceCode.addCodeLine("sammlung[i] = temp;", "13", 4, null); 
		sourceCode.addCodeLine("}", "14", 3, null); 
		sourceCode.addCodeLine("}", "15", 2, null); 
		sourceCode.addCodeLine("links = fertig + 1;", "16", 2, null); 
		sourceCode.addCodeLine("for (int i = links; i <= rechts; i++) {", "17", 2, null); 
		sourceCode.addCodeLine("if (sammlung[i].compareTo(sammlung[i-1]) < 0) {", "18", 3, null); 
		sourceCode.addCodeLine("austausch = true;", "19", 4, null); 
		sourceCode.addCodeLine("fertig = i;", "20", 4, null); 
		sourceCode.addCodeLine("final E temp = sammlung[i-1];", "21", 4, null); 
		sourceCode.addCodeLine("sammlung[i-1] = sammlung[i];", "22", 4, null); 
		sourceCode.addCodeLine("sammlung[i] = temp;", "23", 4, null); 
		sourceCode.addCodeLine("}", "24", 3, null); 
		sourceCode.addCodeLine("}", "25", 2, null); 
		sourceCode.addCodeLine("rechts = fertig - 1;", "26", 2, null); 
		sourceCode.addCodeLine("} while (austausch);", "27", 1, null); 
		sourceCode.addCodeLine("}", "28", 0, null);
		*/ 
		parse();
		
		this.array = l.newIntArray(new Offset(0, 100, "header", AnimalScript.DIRECTION_SW), arrayData, "array", null, aProps);
		l.nextStep(); 
		
		sort(); 
		
		return l.toString(); 
	}

	@Override
	public String getAlgorithmName() {
		return "Shaker Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Oksana Kolach, Michael Drescher";
	}

	/*
	@Override
	public String getCodeExample() {
		return 
				"static <E extends Comparable<? super E>> void shakerSort(E[] sammlung) {\n"
			    + "    boolean austausch;\n"
			    + "    int links = 1; // Start beim zweiten Element\n"
			    + "    int rechts = sammlung.length-1;\n"
			    + "    int fertig = rechts;\n"
			    + "    do {\n"
			    + "        austausch = false;\n"
			    + "        for (int i = rechts; i >= links; i--) {\n"
			    + "            if (sammlung[i].compareTo(sammlung[i-1]) < 0) {\n"
			    + "                austausch = true; \n"
			    + "                fertig = i;\n"
			    + "                final E temp = sammlung[i-1];\n"
			    + "                sammlung[i-1] = sammlung[i];\n"
			    + "                sammlung[i] = temp;\n"
			    + "            }\n"
			    + "        }\n"
			    + "        links = fertig + 1;\n"
			    + "        for (int i = links; i <= rechts; i++) {\n"
			    + "            if (sammlung[i].compareTo(sammlung[i-1]) < 0) {\n"
			    + "                austausch = true;\n"
			    + "                fertig = i;\n"
			    + "                final E temp = sammlung[i-1];\n"
			    + "                sammlung[i-1] = sammlung[i];\n"
			    + "                sammlung[i] = temp;\n"
			    + "            }\n"
			    + "        }\n"
			    + "        rechts = fertig - 1;\n"
			    + "    } while (austausch);\n"
			    + "}"; 
	}
	*/ 

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Die Animation zeigt den Ablauf des Shakersort Algorithmus. Die Elemente werden hierbei aufsteigend sortiert.";
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "Shakersort";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		super.init();
		l = new AnimalScript("ShakerSort", "Oksana Kolach & Michael Drescher", 640, 480); 
		l.setStepMode(true); 	 
	}

	@Override
	public String getAnnotatedSrc() {
		return "static <E extends Comparable<? super E>> void shakerSort(E[] sammlung) { @label(\"0\")\n"
	    + "    boolean austausch; @label(\"1\") @declare(\"int\", \"austausch\", \"0\")\n"
	    + "    int links = 1; // Start beim zweiten Element @label(\"2\") @declare(\"int\", \"links\", \"1\")\n"
	    + "    int rechts = sammlung.length-1; @label(\"3\") @declare(\"int\", \"rechts\")\n" // wie löst man das? 
	    + "    int fertig = rechts; @label(\"4\") @declare(\"int\", \"fertig\") @set(\"fertig\", \"rechts\")\n"
	    + "    do { @label(\"5\")\n"
	    + "        austausch = false; @label(\"6\") @eval(\"austausch\", \"0\")\n"
	    + "        for (int i = rechts; i >= links; i--) { @label(\"7\")\n" // wie soll man das mit annotations umsetzen / das würde sich mit unserem highlighting nicht vertragen
	    + "            if (sammlung[i].compareTo(sammlung[i-1]) < 0) { @label(\"8\")\n"
	    + "                austausch = true; @label(\"9\") @eval(\"austausch\", \"1\")\n"
	    + "                fertig = i; @label(\"10\") @set(\"fertig\", \"links\")\n"
	    + "                final E temp = sammlung[i-1]; @label(\"11\")\n"
	    + "                sammlung[i-1] = sammlung[i]; @label(\"12\")\n"
	    + "                sammlung[i] = temp; @label(\"13\")\n"
	    + "            } @label(\"14\")\n"
	    + "        } @label(\"15\")\n"
	    + "        links = fertig + 1; @label(\"16\") @eval(\"links\", \"fertig + 1\")\n"
	    + "        for (int i = links; i <= rechts; i++) { @label(\"17\")\n"
	    + "            if (sammlung[i].compareTo(sammlung[i-1]) < 0) { @label(\"18\")\n"
	    + "                austausch = true; @label(\"19\") @eval(\"austausch\", \"1\")\n"
	    + "                fertig = i; @label(\"20\") @set(\"fertig\", \"i\")\n"
	    + "                final E temp = sammlung[i-1]; @label(\"21\")\n"
	    + "                sammlung[i-1] = sammlung[i]; @label(\"22\")\n"
	    + "                sammlung[i] = temp; @label(\"23\")\n"
	    + "            } @label(\"24\")\n"
	    + "        } @label(\"25\")\n"
	    + "        rechts = fertig - 1; @label(\"26\") @eval(\"rechts\", \"fertig - 1\")\n"
	    + "    } while (austausch); @label(\"27\")\n"
	    + "} @label(\"28\")"; 
	}
	
}
