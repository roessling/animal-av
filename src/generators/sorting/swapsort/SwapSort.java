package generators.sorting.swapsort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
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
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class SwapSort implements Generator {
	
	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;
	
	private boolean hidden_am_i;
	private TextProperties textProps;
	private ArrayProperties arrayProps;
	private ArrayMarkerProperties startProps, iProps;
	private SourceCodeProperties scProps;
	private boolean counter_hidden;

	/**
	 * (Default) constructor.
	 * @param l the concrete language object used for creating output.
	 */
	public SwapSort(Language l) {
		lang = l;
		lang.setStepMode(true);
	}
	
	/**
	 * The standard constructor.
	 */
	public SwapSort() {
		this(new AnimalScript("SwapSort", "Dmytro Vronskyi", 640, 480));
	}

	private static final String DESCRIPTION = "Dieser Generator sortiert das vom Nutzer " +
		"eingegebene Array mittels Swap-Sort. Swap-Sort ist ein Sortieralgorithmus, " +
		"der ein Array aus paarweise verschiedenen Zahlen sortiert. Daher ist wichtig, " +
		"dass der Nutzer im zu sortierenden Array nur PAARWEISE VERSCHIEDENE Zahlen eingibt, " +
		"da der Algorithmus sonst NICHT TERMINIERT (und kein Code generiert werden kann)!" +
		"\n\nBeim Swap-Sort werden fuer jedes Element elem des zu sortierenden Arrays " +
		"A[0..n-1] die Anzahl \"smallers\" der Elemente von A mit kleineren Werten gezaehlt. " +
		"Anschliessend wird elem mit dem Element in A[smallers] vertauscht. Somit wird " +
		"der Platz fuer die \"smallers\" Elemente geschaffen, so dass elem bereits an " +
		"der richtigen Position steht.";
	
	private static final String CODE_EXAMPLE = "public void sort(int[] array) { \n" +
        "  int start = 0; \n" +
        "  while (start < array.length - 1) { \n" +
        "    int smallers = 0; \n" +
        "    for (int i = start + 1; i < array.length; i++) \n" +
        "      if (array[i] < array[start]) \n" +
        "        smallers++; \n" +
        "    if (smallers > 0) \n" +
        "      swap(array, start, start + smallers); \n" +              
        "    else \n" +
        "      start++; \n" +
        "  } \n" +
        "}";

	/**
	 * Erhaelt die vom Nutzer getaetigten Einstellungen und liefert den Animationscode
	 * als <code>java.lang.String</code> zurueck.
	 * @return Der Animationscode als <code>java.lang.String</code>.
	 * 
	 * @see generators.framework.Generator#generate(generators.framework.properties.AnimationPropertiesContainer, java.util.Hashtable)
	 */
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		textProps = (TextProperties) props.getPropertiesByName("text");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		startProps = (ArrayMarkerProperties) props.getPropertiesByName("startMarker");
		iProps = (ArrayMarkerProperties) props.getPropertiesByName("iMarker");
		scProps = (SourceCodeProperties) props.getPropertiesByName("code");
		
		int [] array = (int[]) primitives.get("intArray");
		try {
			sort(array);
		}
		catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		
		lang.finalizeGeneration();
		return lang.getAnimationCode();
	}

	/**
	 * Animiert die Sortierung von <code>array</code> mittels Swap-Sort.
	 * 
	 * @param array Das zu sortierende Array von Ganzzahlen.
	 * @throws LineNotExistsException Wenn im Sourcecode auf eine nicht existierende
	 * 								  Zeile zugegriffen wird.
	 */
	private void sort(int[] array) throws LineNotExistsException {
		MsTiming duration = new MsTiming(1000);
		
		TextProperties titleTextProp = new TextProperties();
		titleTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("Monospaced", Font.BOLD, 24));
		
		lang.newText(new Coordinates(480, 40), "Swap-Sort Sortieralgorithmus", "title", 
				null, titleTextProp);
		
		lang.nextStep();
		
		SourceCode sc = lang.newSourceCode(new Coordinates(20, 200), "code", null, scProps);
		sc.addCodeLine("public void sort(int[] array) {", null, 0, null);
		sc.addCodeLine("int start = 0;", null, 1, null);
		sc.addCodeLine("while (start < array.length - 1) {", null, 1, null);
		sc.addCodeLine("int smallers = 0;", null, 2, null);
		sc.addCodeLine("for (int i = start + 1; i < array.length; i++)", null, 2, null);
		sc.addCodeLine("if (array[i] < array[start])", null, 3, null);
		sc.addCodeLine("smallers++;", null, 4, null);
		sc.addCodeLine("if (smallers > 0)", null, 2, null);
		sc.addCodeLine("swap(array, start, start + smallers);", null, 3, null);
		sc.addCodeLine("else", null, 2, null);
		sc.addCodeLine("start++;", null, 3, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		
		lang.nextStep();
		
		lang.newText(new Offset(70, 0, sc, AnimalScript.DIRECTION_NE), 
				"Erläuterung zur Animation: ", "", null, textProps);
		lang.newText(new Offset(70, 25, sc, AnimalScript.DIRECTION_NE), 
				"Die aktuell betrachteten Elemente des Arrays werden hervorgehoben. ",
				"", null, textProps);
		lang.newText(new Offset(70, 40, sc, AnimalScript.DIRECTION_NE), 
				"Die hervorgehobenen Zellen des Arrays enthalten die bereits sortierten Elemente.",
				"", null, textProps);
		
		IntArray ia = lang.newIntArray(new Coordinates(20, 150), array, "array", 
				null, arrayProps);
		sc.highlight(0);
		
		lang.nextStep();
		
		sc.toggleHighlight(0, 1);
		int start = 0;
		ArrayMarker am_start = lang.newArrayMarker(ia, start, "start", null, startProps);
		ia.highlightElem(start, null, null);
		ArrayMarker am_i = lang.newArrayMarker(ia, start + 1, "i", null, iProps);
		am_i.hide();
		hidden_am_i = true;
		
		lang.nextStep();
		
		sc.toggleHighlight(1, 2);
		Text counter = lang.newText(new Offset(25, 0, ia, AnimalScript.DIRECTION_E),
				"smallers = ", "smallers", null, textProps);
		counter.hide();
		counter_hidden = true;
		Text sm_value = lang.newText(new Offset(0, 0, counter, AnimalScript.DIRECTION_NE),
				"", "sm_value", null, textProps);
		while (start < array.length - 1) {
			lang.nextStep();
			int smallers = 0;
			sc.highlight(2, 0, true);
			sc.highlight(11, 0, true);
			sc.highlight(3);
			if (counter_hidden) {
				counter.show();
				counter_hidden = false;
			}
			sm_value.setText(String.valueOf(smallers), null, null);
			
			lang.nextStep();
			
			sc.toggleHighlight(3, 4);
			
			for (int i = start + 1; i < array.length; i++) {
				am_i.move(i, null, null);
				if (hidden_am_i)
					am_i.show();
				
				lang.nextStep();
				
				ia.highlightElem(i, null, null);
				sc.highlight(4, 0, true);
				sc.highlight(5);
				if (array[i] < array[start]) {
					
					lang.nextStep();
					
					sc.toggleHighlight(5, 6);
                	smallers++;
        			sm_value.setText(String.valueOf(smallers), null, null);
        			
        			lang.nextStep();
        			
        			sc.unhighlight(6);
				}
				else {
					
					lang.nextStep();
					
					sc.unhighlight(5);
				}
				ia.unhighlightElem(i, null, null);
			}
			
			lang.nextStep();
			
			sc.toggleHighlight(4, 7);
			if (smallers > 0) {
				
				lang.nextStep();
				
				sc.toggleHighlight(7, 8);
				ia.swap(start, start + smallers, null, duration);
				
				lang.nextStep();
				
				sc.unhighlight(8);
				ia.unhighlightElem(start + smallers, null, null);
				ia.highlightCell(start + smallers, null, null);
				ia.highlightElem(start, null, null);
			}
			else {
				
				lang.nextStep();
				
				sc.toggleHighlight(7, 9);
				
				lang.nextStep();
				
				sc.toggleHighlight(9, 10);
				ia.highlightCell(start, null, null);
				ia.unhighlightElem(start, null, null);
				start++;
				am_start.increment(null, null);
				ia.highlightElem(start, null, null);

				lang.nextStep();
				
				sc.unhighlight(10);
			}
		}
		
		lang.nextStep();
		
		sc.unhighlight(2);
		sc.unhighlight(11);
		ia.unhighlightElem(start, null, null);
	}
	
	/**
	 * Gibt den Basisnamen des Algorithmus zurueck.
	 * @return Der Basisname des Algorithmus.
	 *  
	 * @see generators.framework.Generator#getAlgorithmName()
	 */
	public String getAlgorithmName() {
		return "Swap Sort";
	}

	/** 
	 * Liefert den verwendeten Sourcecode.
	 * @return Der Sourcecode.
	 * 
	 * @see generators.framework.Generator#getCodeExample()
	 */
	public String getCodeExample() {
		return CODE_EXAMPLE;
	}

	/** 
	 * Liefert die <code>java.util.Locale</code> der Inhalte.
	 * @return die <code>java.util.Locale</code> der Inhalte.
	 * 
	 * @see generators.framework.Generator#getContentLocale()
	 */
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	/**
	 * Liefert eine kurze textuelle Beschreibung des Algorithmus.
	 * @return Beschreibung des Algorithmus.
	 * 
	 * @see generators.framework.Generator#getDescription()
	 */
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * Liefert die Dateiendung.
	 * @return die Dateiendung.
	 * 
	 * @see generators.framework.Generator#getFileExtension()
	 */
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	/**
	 * Liefert den Typ des Algorithmus zurueck.
	 * @return Der Typ des Algorithmus.
	 * 
	 * @see generators.framework.Generator#getGeneratorType()
	 */
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	/**
	 * Gibt den Namen des Generators zurueck.
	 * @return der Name des Generators.
	 * 
	 * @see generators.framework.Generator#getName()
	 */
	public String getName() {
		return "SwapSort";
	}

	/**
	 * Definiert die Ausgabesprache.
	 * @return die Ausgabesprache.
	 * 
	 * @see generators.framework.Generator#getOutputLanguage()
	 */
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	
	public String getAnimationAuthor() {
		return "Dmytro Vronskyi";
	}

	
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
