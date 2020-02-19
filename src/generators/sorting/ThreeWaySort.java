package generators.sorting;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import interactionsupport.models.TrueFalseQuestionModel;
import javafx.util.Pair;

/**
 * @author Donghyuck Son & Timur Levent Görgü
 * @version 1.0
 */
public class ThreeWaySort {

	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;
	private int iteration = 1;
	/**
	 * Default constructor
	 * 
	 * @param l
	 *            the conrete language object used for creating output
	 */
	public ThreeWaySort(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}

	public final static Timing defaultDuration = new TicksTiming(30);

	/**
	 * search the element x in sorted array
	 * 
	 * @param a
	 *            the array to be sorted
	 * @param x
	 * 			  searched element
	 */
	public void sort(int[] a) {
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		// Create Array: coordinates, data, name, display options,
		// default properties
	    TextProperties headerProps = new TextProperties();
	    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.BOLD, 24));
		Text title = lang.newText(new Coordinates(20,30), "3 Way Sorting (Dutch National Flag)","titel", null, headerProps);
		
	    RectProperties rectProps = new RectProperties();
	    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
	    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    
	    Rect hRect = lang.newRect(new Offset(-5, -5, "titel",
	        AnimalScript.DIRECTION_NW), new Offset(5, 5, "titel", "SE"), "hRect",
	        null, rectProps);
//	    title.show();
//	    hRect.show();
		
		//Rectangle header = lang.newRect(new Offset(-5, -5), lowerRight, name, display);
		Text title_discr = lang.newText(new Coordinates(20, 80), "Beschreibung des Algorithmus","discr", null);
		SourceCode discription = lang.newSourceCode(new Coordinates(20, 110),"beschreibung",null);
		discription.addCodeLine("Der Algorithmus in Worten:", null, 0, null);
		discription.addCodeLine("", null, 0, null);
		discription.addCodeLine("1. Gegeben sind ein nicht sortierte Array a[]", null, 0, null);
		discription.addCodeLine("2. Selektiere ein Pivotelement v.", null, 0, null);
		discription.addCodeLine("3. Teile das Array in 3 Teilarrays auf.", null, 0, null);
		discription.addCodeLine("4. Die Teilarrays werden nach folgenden Eigenschaft sortiert:", null, 0, null);
		discription.addCodeLine("1) Die Elemente kleiner als Pivotelement: a[start...mid] ", null, 1, null);
		discription.addCodeLine("2) Die Elemente gleich groß wie Pivotelement: a[start+1...mid] ", null, 1, null);
		discription.addCodeLine("3) Die Elemente größer als Pivotelement: a[mid+1...end]", null, 1, null);
		discription.addCodeLine("Hinweis: Hier wird das Pivotelement immer das letzte Element vom Array gewählt", null, 1, null);
		discription.addCodeLine("", null, 0, null);

		lang.nextStep("Einleitung");
		title_discr.hide();
		discription.hide();
		int start= 0;
//		Text t_array = lang.newText(new Coordinates(520, 100), "das zu sotierende Array:", "array", null);
		TextProperties startProp = new TextProperties();
		startProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		Text t_start = lang.newText(new Coordinates(520, 220), "start", "start",null, startProp);
		TextProperties endProp = new TextProperties();
		endProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		Text t_end = lang.newText(new Coordinates(560, 220), "end", "end", null, endProp);
		TextProperties midProp = new TextProperties();
		midProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		Text t_mid = lang.newText(new Coordinates(600, 220), "mid", "mid", null, midProp);
		TextProperties pivotProp = new TextProperties();
		pivotProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);
		Text t_pivot = lang.newText(new Coordinates(640, 220), "pivot", "pivot", null, pivotProp);
		
		// first, set the visual properties (somewhat similar to CSS)
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

		// now, create the IntArray object, linked to the properties
		IntArray ia = lang.newIntArray(new Coordinates(520, 160), a, "intArray", null, arrayProps);
		
		
//	    COUNTER
	    String[] name = {"Zuweisungen","Zugriffe"};
		TwoValueCounter counter = lang.newCounter(ia); // Zaehler anlegen
	    CounterProperties cp = new CounterProperties(); // Zaehler-Properties anlegen
	    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
	    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau
	    // view anlegen, Parameter:
	    // 1. Counter
	    // 2. linke obere Ecke (kann auch Offset nutzen!)
	    // 3. CounterProperties
	    // 4. Anzeige Zaehlerwert als Zahl?
	    // 5. Anzeige Zaehlerwert als Balken?
	    // Alternativ: nur Angabe Counter, Koordinate und Properties
	    TwoValueView view = lang.newCounterView(counter, new Coordinates(520, 30), cp, true, true, name);
		// start a new step after the array was created


		// Create SourceCode: coordinates, name, display options,
		// default properties

		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 80), "sourceCode", null, scProps);
		
		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy

		sc.addCodeLine("public static void quicksort(int[] arr, int start, int end){", null, 0, null); // 0
		sc.addCodeLine("", null, 0, null); // 1
		sc.addCodeLine("if (start >= end) {", null, 1, null); // 2
		sc.addCodeLine("return;", null, 2, null); // 3
		sc.addCodeLine("}", null, 1, null); // 4
		sc.addCodeLine("", null, 1, null); // 5
		sc.addCodeLine("if (start - end == 1){", null, 1, null); // 6
		sc.addCodeLine("if (arr[start] < arr[end]) {", null, 2, null); // 7
		sc.addCodeLine("swap(arr, start, end);", null, 3, null); // 8
		sc.addCodeLine("}", null, 2, null); // 9
		sc.addCodeLine("return;", null, 2, null); // 10
		sc.addCodeLine("}", null, 1, null); // 11
		sc.addCodeLine("", null, 1, null); // 12
		sc.addCodeLine("Pair pivot = partition(arr, start, end);", null, 1, null); // 13
		sc.addCodeLine("", null, 1, null); // 14
		sc.addCodeLine("quicksort(arr, start, (int) pivot.getKey());", null, 1, null); // 15
		sc.addCodeLine("", null, 1, null); // 16
		sc.addCodeLine("quicksort(arr, (int) pivot.getValue(), end);", null, 1, null); // 17
		sc.addCodeLine("}", null, 0, null); // 18
		
		sc.addCodeLine("", null, 0, null); // 19
		sc.addCodeLine("", null, 0, null); // 20

		sc.addCodeLine("public static Pair<Integer, Integer> partition(int[] arr, int start, int end){", null, 0, null); // 21
		sc.addCodeLine("", null, 0, null); // 22
		sc.addCodeLine("int mid = start;", null, 1, null); // 23
		sc.addCodeLine("int pivot = arr[end];", null, 1, null); // 24
		sc.addCodeLine("", null, 1, null); // 25
		sc.addCodeLine("while (mid <= end) {", null, 1, null); // 26
		sc.addCodeLine("if (arr[mid] < pivot) {", null, 2, null); // 27
		sc.addCodeLine("swap(arr, start, mid);", null, 3, null); // 28
		sc.addCodeLine("++start;", null, 3, null); // 29
		sc.addCodeLine("++mid;", null, 3, null); // 30
		sc.addCodeLine("}", null, 2, null); // 31
		sc.addCodeLine("else if (arr[mid] > pivot) {", null, 2, null); // 32
		sc.addCodeLine("swap(arr, mid, end);", null, 3, null); // 33
		sc.addCodeLine("--end;", null, 3, null); // 34
		sc.addCodeLine("}", null, 2, null); // 35
		sc.addCodeLine("else {", null, 2, null); // 36
		sc.addCodeLine("++mid;", null, 3, null); // 37
		sc.addCodeLine("}", null, 2, null); // 38
		sc.addCodeLine("}", null, 1, null); // 39
		sc.addCodeLine("", null, 1, null); // 40
		sc.addCodeLine("return new Pair<Integer, Integer>(start - 1, mid);", null, 1, null); // 41
		sc.addCodeLine("}", null, 0, null); // 42

		TrueFalseQuestionModel trueFalseQ = new TrueFalseQuestionModel("0", true, 1);
		trueFalseQ.setPrompt("Kann man den Algorithmus auch mit mehr als 3 verschiedenen Inputparameter starten? (z.B. [1,5,3,5,4])");
		lang.addTFQuestion(trueFalseQ);

		lang.nextStep("Initialisierung");

		
		// Highlight all cells
//		ia.highlightCell(0,ia.getLength()-1, null, null);
		//int foundIndex = -1;
		//int j = r;
		//int i = l;
		

		try {
			// Start 3 way quicksort
			quicksort(ia, 0, ia.getLength()-1, sc);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		TextProperties textProp = new TextProperties();
		//textProp.set(AnimationPropertiesKeys.SIZE_PROPERTY, 40);
		lang.nextStep();
		sc.hide();
		ia.hide();
//		t_array.hide();
		t_start.hide();
		t_end.hide();
		t_mid.hide();
		t_pivot.hide();
		
		
		Text t5 = lang.newText(new Coordinates(20, 80), "Zusammenfassung", "zusammenfassung", null);
		SourceCode zsmf = lang.newSourceCode(new Coordinates(20, 110), "zsmf", null, scProps);
		zsmf.addCodeLine("1. 3-Way Quicksort ist eine Optimierung fÜr den Fall, wo das Input-Array mehreren redundanten Elemente entält", null, 0, null);
		zsmf.addCodeLine("2. 3-Way Quicksort ist allgemein nicht stabil wie andere Sortieralgorithmen", null, 0, null);
		
		SourceCode zsmf2 = lang.newSourceCode(new Coordinates(20, 150), "zsmf2", null, scProps);
		zsmf2.addCodeLine("Komplexität in Abhängigkeit der Zeit:", null, 0, null);
		zsmf2.addCodeLine("Worst-case - O(n^2)", null, 1, null);
		zsmf2.addCodeLine("Best-case - O(1)", null, 1, null);
		zsmf2.addCodeLine("Average-case - O(n * log(n))", null, 1, null);
		zsmf2.addCodeLine("Hinweis: n steht für die Anazahl der Elemente im Input-Array", null, 1, null);

		TrueFalseQuestionModel trueFalseQ1 = new TrueFalseQuestionModel("1", false, 1);
		trueFalseQ1.setPrompt("Der Algorithmus zerlegt das Input-Array in 2 Teilarrays");
		lang.addTFQuestion(trueFalseQ1);
	
		
		TrueFalseQuestionModel trueFalseQ2 = new TrueFalseQuestionModel("1", false, 1);
		trueFalseQ2.setPrompt("Für Input-Array a = [1,1,1,1,2,2,2,3,3] und das Pivotelement p = 3 handelt es sich um den Average-Case");
		lang.addTFQuestion(trueFalseQ2);
		
		lang.nextStep("Fazit");
		lang.finalizeGeneration();

	}

	/**
	 * counter for the number of pointers
	 * 
	 */
	private int pointerCounter = 0;

	/**
	 * exponentialSearch: search elements in sorted array
	 * 
	 * @param a
	 *            unsorted array
	 * @param codeSupport
	 *            the underlying code instance
	 * @param l
	 *           index of first element in first partition
	 * @param r
	 *           index of last element in third partition
	 * @param i
	 *           index of last element in first partition
	 * @param j
	 *           index of first element in third partition
	 */
	private Pair<Integer, Integer> partition(IntArray arr, int start, int end, SourceCode sc) throws LineNotExistsException {
		
		sc.toggleHighlight(13, 21);
		
		pointerCounter++;
	    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
	    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "s");
	    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
	    ArrayMarker startMarker = lang.newArrayMarker(arr, start, "start" + pointerCounter,
	        null, arrayIMProps);
	    
	    pointerCounter++;
	    ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
	    arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "e");
	    arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
	    ArrayMarker endMarker = lang.newArrayMarker(arr, end, "end" + pointerCounter,
	        null, arrayJMProps);
	    
	    lang.nextStep();
		
		int mid = start;
		
	    pointerCounter++;
	    ArrayMarkerProperties arrayMProps = new ArrayMarkerProperties();
	    arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "m");
	    arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
	    arrayMProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
	    ArrayMarker midMarker = lang.newArrayMarker(arr, mid,
	        "mid" + pointerCounter, null, arrayMProps);
	    sc.toggleHighlight(21, 23);
		lang.nextStep();
		

		
		int pivot = arr.getData(end);
	    
	    pointerCounter++;
	    ArrayMarkerProperties arrayPMProps = new ArrayMarkerProperties();
	    arrayPMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "p");
	    arrayPMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);
	    arrayPMProps.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true);
	    ArrayMarker pivotMarker = lang.newArrayMarker(arr, pivot,
	        "pivot" + pointerCounter, null, arrayPMProps);
	    sc.toggleHighlight(23,24);
	    lang.nextStep();
	    
	    sc.toggleHighlight(24,26);
	    lang.nextStep();

		while (mid <= end) {
			
		    sc.toggleHighlight(26,27);
		    lang.nextStep();
			
			if (arr.getData(mid) < pivot) {
				
			    sc.toggleHighlight(27,28);
			    arr.highlightCell(start, null, null);
			    arr.highlightCell(mid, null, null);
			    lang.nextStep();
				
				arr.swap(start, mid, null, null);
				
				arr.unhighlightCell(start, null, null);
				arr.unhighlightCell(mid, null, null);
				sc.toggleHighlight(28,29);
				lang.nextStep();			
				
				++start;
				
				startMarker.move(start, null, null);
				sc.toggleHighlight(29,30);
				lang.nextStep();
				
				
				++mid;
				
				midMarker.move(mid, null, null);
				sc.toggleHighlight(30,26);
				lang.nextStep();
				
			}
			else if (arr.getData(mid) > pivot) {
				sc.toggleHighlight(27,32);
				lang.nextStep();
				sc.toggleHighlight(32,33);
				
				arr.highlightCell(mid, null, null);
				arr.highlightCell(end, null, null);
				lang.nextStep();
				
				
				arr.swap( mid, end, null, null);
				
				arr.unhighlightCell(mid, null, null);
				arr.unhighlightCell(end, null, null);
				sc.toggleHighlight(33, 34);
				lang.nextStep();
				
				
				--end;
				
				endMarker.move(end, null, null);
				sc.toggleHighlight(34,26);
				lang.nextStep();
			}
			else {
				sc.toggleHighlight(27,32);
				lang.nextStep();
				sc.toggleHighlight(32,36);
				lang.nextStep();
				sc.toggleHighlight(36,37);
				lang.nextStep();
				
				++mid;
				
				midMarker.move(mid, null, null);
				sc.toggleHighlight(37,26);
				lang.nextStep();
			}
		}
		
	    sc.toggleHighlight(26,41);
	    lang.nextStep();
	    
	    startMarker.hide();
	    endMarker.hide();
	    midMarker.hide();
	    pivotMarker.hide();

		// arr[start .. mid - 1] contains all occurrences of pivot
		return new Pair<Integer, Integer>(start - 1, mid);
	}
	
	private void quicksort(IntArray arr, int start, int end, SourceCode sc) throws LineNotExistsException {
		
		pointerCounter++;
	    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
	    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "s");
	    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
	    ArrayMarker startMarker = lang.newArrayMarker(arr, start, "start" + pointerCounter,
	        null, arrayIMProps);
	    
	    pointerCounter++;
	    ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
	    arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "e");
	    arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
	    ArrayMarker endMarker = lang.newArrayMarker(arr, end, "end" + pointerCounter,
	        null, arrayJMProps);
	    
	    sc.highlight(0);
	    sc.unhighlight(15);
	    sc.unhighlight(17);
		lang.nextStep();
	    
		sc.toggleHighlight(0, 2);
	    lang.nextStep();
		// base condition for 0 or 1 elements
		if (start >= end) {
			sc.toggleHighlight(2, 3);
			lang.nextStep();
			startMarker.hide();
			endMarker.hide();
			return;
		}
		
		sc.toggleHighlight(2, 6);
		lang.nextStep();
		
		// handle 2 elements separately as Dutch national flag
		// algorithm will work for 3 or more elements
		if (start - end == 1){
			sc.toggleHighlight(6, 7);
			lang.nextStep();
			if (arr.getData(start) < arr.getData(end)) {
				sc.toggleHighlight(7, 8);
				arr.highlightCell(start, null, null);
				arr.highlightCell(end, null, null);
				lang.nextStep();
				arr.swap(start, end, null, null);
				arr.unhighlightCell(start, null, null);
				arr.unhighlightCell(end, null, null);
			}
			sc.toggleHighlight(7, 10);
			lang.nextStep();
			startMarker.hide();
			endMarker.hide();
			return;
		}
		sc.toggleHighlight(6, 13);
		lang.nextStep();


		// rearrange the elements across pivot using Dutch
		// national flag problem algorithm
		startMarker.hide();
		endMarker.hide();
		
		Pair pivot = partition(arr, start, end, sc);
		
		startMarker.show();
		endMarker.show();
		sc.toggleHighlight(41, 15);
		lang.nextStep();
		
		startMarker.hide();
		endMarker.hide();
		// recur on sub-array containing elements that are less than pivot
		quicksort(arr, start, (int) pivot.getKey(), sc);
		
		startMarker.show();
		endMarker.show();
		sc.highlight(17);
		sc.unhighlight(3);
		sc.unhighlight(10);
		sc.unhighlight(18);
		lang.nextStep();
		
		startMarker.hide();
		endMarker.hide();

		// recur on sub-array containing elements that are more than pivot
		quicksort(arr, (int) pivot.getValue(), end, sc);
		
		startMarker.show();
		endMarker.show();
		sc.highlight(18);
		sc.unhighlight(3);
		sc.unhighlight(10);
		lang.nextStep();
		startMarker.hide();
		endMarker.hide();
		
	}

	public static void main(String[] args) {
		// Create a new language object for generating animation code
		// this requires type, name, author, screen width, screen height
		Language lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "3 way sort",
				"Donghyuck Son, Timur Levent Görgü", 640, 480);
		ThreeWaySort s = new ThreeWaySort(lang);
		int a[] = {3,3,3,2,1,2,1,1,1,1};
//		int a[] = {4, 9, 4, 4, 1, 9, 4, 4, 9, 4, 4, 1, 4};

		s.sort(a);
//		System.out.println(lang);
		Animal.startAnimationFromAnimalScriptCode(lang.toString());
	}
}
