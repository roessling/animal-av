package generators.searching;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
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

/**
 * @author Donghyuck Son & Timur Levent Görgü
 * @version 1.0
 */
public class ExponentialSearch {

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
	public ExponentialSearch(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}
/*
	private static final String DESCRIPTION = "QuickSort wählt ein Element aus der zu sortierenden Liste aus "
			+ "(Pivotelement) und zerlegt die Liste in zwei Teillisten, eine untere, "
			+ "die alle Elemente kleiner und eine obere, die alle Elemente gleich oder "
			+ "größer dem Pivotelement enthält.\nDazu wird zunächst ein Element von unten "
			+ "gesucht, das größer als (oder gleichgroß wie) das Pivotelement und damit "
			+ "für die untere Liste zu groß ist. Entsprechend wird von oben ein kleineres "
			+ "Element als das Pivotelement gesucht. Die beiden Elemente werden dann "
			+ "vertauscht und landen damit in der jeweils richtigen Liste.\nDer Vorgang "
			+ "wird fortgesetzt, bis sich die untere und obere Suche treffen. Damit sind "
			+ "die oben erwähnten Teillisten in einem einzigen Durchlauf entstanden. "
			+ "Suche und Vertauschung können in-place durchgeführt werden."
			+ "\n\nDie noch unsortierten Teillisten werden über denselben Algorithmus "
			+ "in noch kleinere Teillisten zerlegt (z. B. mittels Rekursion) und, sobald "
			+ "nur noch Listen mit je einem Element vorhanden sind, wieder zusammengesetzt. "
			+ "Die Sortierung ist damit abgeschlossen.";

	private static final String SOURCE_CODE = "public void quickSort(int[] array, int l, int r)" // 0
			+ "\n{" // 1
			+ "\n  int i, j, pivot;" // 2
			+ "\n  if (r>l)" // 3
			+ "\n  {" // 4
			+ "\n    pivot = array[r];" // 5
			+ "\n    for (i = l; j = r - 1; i < j; )" // 6
			+ "\n    {" // 7
			+ "\n      while (array[i] <= pivot && j > i)" // 8
			+ "\n        i++;" // 9
			+ "\n      while (pivot < array[j] && j > i)" // 10
			+ "\n        j--;" // 11
			+ "\n      if (i < j)" // 12
			+ "\n        swap(array, i, j);" // 13
			+ "\n    }" // 14
			+ "\n    if (pivot < array[i])" // 15
			+ "\n      swap(array, i, r);" // 16
			+ "\n    else" // 17
			+ "\n      i=r;" // 18
			+ "\n    quickSort(array, l, i - 1);" // 19
			+ "\n    quickSort(array, i + 1, r);" // 20
			+ "\n  }" // 21
			+ "\n}"; // 22

	/**
	 * default duration for swap processes
	 */

	public final static Timing defaultDuration = new TicksTiming(30);

	/**
	 * search the element x in sorted array
	 * 
	 * @param a
	 *            the array to be sorted
	 * @param x
	 * 			  searched element
	 */
	public void search(int[] a, int x) {
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		// Create Array: coordinates, data, name, display options,
		// default properties
	    TextProperties headerProps = new TextProperties();
	    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.BOLD, 24));
		Text title = lang.newText(new Coordinates(20,30), "Exponential Search","titel", null, headerProps);
		
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
		discription.addCodeLine("1. Finde den Bereich, in dem das Element vorhanden ist, indem man mit exponentialen Schritten durch das Array iteriert.", null, 0, null);
		discription.addCodeLine("2. Führe eine binäre Suche in oben gefundenen Bereich durch.", null, 0, null);
		
//		MultipleSelectionQuestionModel mulQuestion = new MultipleSelectionQuestionModel("1");
//		mulQuestion.setPrompt("Welcher Algorithmus wird oder welche Algorithmen werden im ExponentialSearch verwendet?");
//		mulQuestion.setNumberOfTries(1);
//		mulQuestion.addAnswer("Exponentialsearch", 1, "ist ein Bestandteil des Algorithmus");
//		mulQuestion.addAnswer("FindNearestNeighbour", 0, "ist kein Bestandteil des Algorithmus");
//		mulQuestion.addAnswer("Binarysearch", 1, "ist ein Bestandteil des Algorithmus");
//		mulQuestion.addAnswer("Linearsearch", 0, "ist kein Bestandteil des Algorithmus");
//		mulQuestion.addAnswer("Skipsearch", 0, "ist kein Bestandteil des Algorithmus");
//		mulQuestion.addAnswer("BruteForce", 0, "ist kein Bestandteil des Algorithmus");
//		
//		lang.addMSQuestion(mulQuestion);
		
		lang.nextStep("Einleitung");
		TrueFalseQuestionModel trueFalseQ1 = new TrueFalseQuestionModel("1", true, 1);
		trueFalseQ1.setPrompt("Exponentialsearch Algorithmus benutzt den Binarysearch Algorithmus.");
		lang.addTFQuestion(trueFalseQ1);
		
		TrueFalseQuestionModel trueFalseQ2 = new TrueFalseQuestionModel("1", true, 1);
		trueFalseQ2.setPrompt("Das Array muss sortiert sein.");
		lang.addTFQuestion(trueFalseQ2);

		title_discr.hide();
		discription.hide();
		
		Text t1 = lang.newText(new Coordinates(20, 80), "Die zu suchende Zahl:", "suchende Zahl", null);
		Text t2 = lang.newText(new Coordinates(20, 100), "x = " + x, "suchende Zahl", null);
		Text t3 = lang.newText(new Coordinates(20, 120), "Das sortierte Array:", "sortierte Array", null);
		Text yellow = lang.newText(new Coordinates(180, 80), "Gelb: von binären Suche geteilte Array", "yellow", null);
		
	    RectProperties rectProps2 = new RectProperties();
	    rectProps2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rectProps2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
	    rectProps2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    
	    Rect hRect2 = lang.newRect(new Offset(-1, -1, "yellow",
	        AnimalScript.DIRECTION_NW), new Offset(1, 1, "yellow", "SE"), "hRect2",
	        null, rectProps2);
		Text red = lang.newText(new Coordinates(180, 100), "Rot: Element in Array[i]", "red", null);
		
	    RectProperties rectProps3 = new RectProperties();
	    rectProps3.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rectProps3.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
	    rectProps3.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    
	    Rect hRect3 = lang.newRect(new Offset(-1, -1, "red",
	        AnimalScript.DIRECTION_NW), new Offset(1, 1, "red", "SE"), "hRect3",
	        null, rectProps3);	    
	    
	    
		// first, set the visual properties (somewhat similar to CSS)
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

		// now, create the IntArray object, linked to the properties
		IntArray ia = lang.newIntArray(new Coordinates(20, 140), a, "intArray", null, arrayProps);

		
		
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
	    TwoValueView view = lang.newCounterView(counter, new Coordinates(260, 30), cp, true, true, name);
		// start a new step after the array was created
	    
		lang.nextStep("Initialisierung");
		

		// Create SourceCode: coordinates, name, display options,
		// default properties

		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 180), "sourceCode", null, scProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("public int exponentialSearch(int[] arr, int n, int x) {", null, 0, null); // 0
		sc.addCodeLine(" ", null, 0, null); // 1

		sc.addCodeLine("if (arr[0] == x) ", null, 1, null); // 2
		sc.addCodeLine("return 0;", null, 2, null); // 3
		sc.addCodeLine(" ", null, 1, null); // 4

		sc.addCodeLine("int i = 1;", null, 1, null); // 5
		sc.addCodeLine(" ", null, 2, null); // 6

		sc.addCodeLine("while (i < n && arr[i] <= x) ", null, 1, null); // 7
		sc.addCodeLine("i = i*2;", null, 2, null); // 8
		sc.addCodeLine(" ", null, 4, null); // 9

		sc.addCodeLine("return Arrays.binarySearch(arr, i/2, Math.min(i, n), x); ", null, 1, null); // 10
		sc.addCodeLine("}", null, 0, null); // 11
		sc.addCodeLine(" ", null, 0, null); // 12

		sc.addCodeLine("public int binarySearch(int[] arr, int left, int right, int x) {", null, 0, null); // 13
		sc.addCodeLine(" ", null, 2, null); // 14
		sc.addCodeLine("if (right >= 1 && left <= right) {", null, 1, null); // 15
		sc.addCodeLine("int mid = (left + right) / 2;", null, 2, null); // 16
		sc.addCodeLine(" ", null, 2, null); // 17
		sc.addCodeLine("if (arr[mid] == x)", null, 2, null); // 18
		sc.addCodeLine("return mid;", null, 3, null); // 19
		sc.addCodeLine(" ", null, 2, null); // 20
		sc.addCodeLine("if (arr[mid] > x)", null, 2, null); // 21
		sc.addCodeLine("return binarySearch(arr, left, mid-1, x);", null, 3, null); // 22
		sc.addCodeLine(" ", null, 2, null); // 23
		sc.addCodeLine("return binarySearch(arr, mid+1, right, x);", null, 2, null); // 24
		sc.addCodeLine("}", null, 1, null); // 25
		sc.addCodeLine("return -1;", null, 1, null); // 26
		sc.addCodeLine("}", null, 0, null); // 27

		lang.nextStep();
		// Highlight all cells
		ia.highlightCell(0,ia.getLength()-1, null, null);
		int foundIndex = -1;
		try {
			// Start Exp. Search
			foundIndex = exponentialSearch(ia, sc, (ia.getLength() - 1), x);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		TextProperties textProp = new TextProperties();
		//textProp.set(AnimationPropertiesKeys.SIZE_PROPERTY, 40);
		Text t4 = lang.newText(new Coordinates(400, 80), "Die Zahl "+ x + " befindet sich in Index " + foundIndex, "Ende", null, textProp);
		lang.nextStep();
		t1.hide();
		t2.hide();
		t3.hide();
		t4.hide();
		sc.hide();
		ia.hide();
		yellow.hide();
		red.hide();
		hRect2.hide();
		hRect3.hide();
		
		Text t5 = lang.newText(new Coordinates(20, 80), "Zusammenfassung", "zusammenfassung", null);
		SourceCode zsmf = lang.newSourceCode(new Coordinates(20, 110), "zsmf", null, scProps);
		zsmf.addCodeLine("1. Exponentielle Suche ist nützlich für die unbegrenzte Suche, wo die Größe vom Array gegen unendlich geht.", null, 0, null);
		zsmf.addCodeLine("2. Die Performenz ist besser als die der en Suche für endlich große Arrays.", null, 0, null);
		
		SourceCode zsmf2 = lang.newSourceCode(new Coordinates(20, 150), "zsmf2", null, scProps);
		zsmf2.addCodeLine("Komplexität in Abhängigkeit der Zeit:", null, 0, null);
		zsmf2.addCodeLine("Worst-case - O(log i) + O(log i) = 2 O(log i)", null, 1, null);
		zsmf2.addCodeLine("Best-case - O(1)", null, 1, null);
		zsmf2.addCodeLine("Average-case - O(log i) + O(log i) = 2 O(log i)", null, 1, null);
		zsmf2.addCodeLine("Hinweis: i steht für den Index des gesuchten Elements.", null, 1, null);
		zsmf2.addCodeLine("Es werden jeweils die Komplexität des ersten und des zweiten Algorithmus zusammenaddiert", null,  2, null);
		zsmf2.addCodeLine("Die Komplexität für den exponentiellen Teil ist log(i).", null,  2, null);
		zsmf2.addCodeLine("Die Komplexität für den en Teil ist aufgrund der Begrenzung nicht mehr log(n), sondern log(i), da log(2^(log(i)-1)) = log(i)-1 = O(log(i))", null,  2, null);


		TrueFalseQuestionModel trueFalseQ3 = new TrueFalseQuestionModel("3", false, 1);
		trueFalseQ3.setPrompt("Das Array darf keine doppelten Elemente enthalten");
		lang.addTFQuestion(trueFalseQ3);

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
	 * @param arr
	 *            the sorted IntArray
	 * @param codeSupport
	 *            the underlying code instance
	 * @param n
	 *            the length of the given array
	 * @param x
	 *            the element to search for
	 */
	private int exponentialSearch(IntArray arr, SourceCode codeSupport, int n, int x) throws LineNotExistsException {

		codeSupport.highlight(0, 0, false);
		lang.nextStep();
		codeSupport.toggleHighlight(0, 0, false, 2, 0);
		lang.nextStep();
		arr.highlightElem(0, null, null);
		
		if (arr.getData(0) == x) {
			codeSupport.toggleHighlight(2, 0, false, 3, 0);
			return 0;
		}

		codeSupport.toggleHighlight(2, 0, false, 5, 0);

		arr.unhighlightElem(0, null, null);
		int i = 1;
		arr.highlightElem(i, null, null);

		lang.nextStep();
		
		codeSupport.toggleHighlight(5, 0, false, 7, 0);
		while (i < n && arr.getData(i) <= x) {
			lang.nextStep("Iteration " + iteration++ + " in Exponential Search");
			codeSupport.toggleHighlight(7, 0, false, 8, 0);
			arr.unhighlightElem(i, null, null);
			i = i * 2;
			arr.highlightElem(i, null, null);
			lang.nextStep();
			codeSupport.toggleHighlight(8, 0, false, 7, 0);
		}
		lang.nextStep();
		codeSupport.toggleHighlight(7, 0, false, 10, 0);
		lang.nextStep();
		codeSupport.toggleHighlight(10, 0, false, 13, 0);
		lang.nextStep();
		arr.unhighlightCell(0,arr.getLength()-1, null, null);
		arr.highlightCell(i/2, Math.min(i, n), null, null);
		return binarySearch(arr, codeSupport, i / 2, Math.min(i, n), x);
	}

	private int binarySearch(IntArray arr, SourceCode codeSupport, int left, int right, int x) throws LineNotExistsException {
		
		arr.unhighlightCell(0,arr.getLength()-1, null, null);
		arr.highlightCell(left, right, null, null);
		codeSupport.unhighlight(13);
		codeSupport.unhighlight(22);
		codeSupport.unhighlight(24);
		codeSupport.highlight(15);
		
		if (right >= 1 && left <= right) {
			lang.nextStep("Iteration " + iteration++ + " in Binary Search");
			arr.unhighlightElem(0,arr.getLength()-1, null, null);
			int mid = (left + right) / 2;
			arr.highlightElem(mid, null, null);
			codeSupport.toggleHighlight(15, 0, false, 16, 0);
			lang.nextStep();
			codeSupport.toggleHighlight(16, 0, false, 18, 0);

			if (arr.getData(mid) == x) {
				lang.nextStep();
				codeSupport.toggleHighlight(18,0,false,19,0);
				return mid;
			}
			lang.nextStep();
			codeSupport.toggleHighlight(18,0,false,21,0);
			if (arr.getData(mid) > x) {
				lang.nextStep();
				codeSupport.toggleHighlight(21, 0, false, 22, 0);
				lang.nextStep();
				return binarySearch(arr, codeSupport, left, mid - 1, x);
			}
			lang.nextStep();
			codeSupport.toggleHighlight(21, 0, false, 24, 0);
			lang.nextStep();
			return binarySearch(arr, codeSupport, mid+1, right, x);
		}
		lang.nextStep();
		codeSupport.toggleHighlight(15, 0, false, 26, 0);
		lang.nextStep();
		return -1;
	}
/*
	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	public String getName() {
		return "Quicksort (pivot=last)";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}
*/
	public static void main(String[] args) {
		// Create a new language object for generating animation code
		// this requires type, name, author, screen width, screen height
		Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Exponantial Search",
				"Donghyuck Son, Timur Levent Görgü", 640, 480);
		ExponentialSearch s = new ExponentialSearch(l);
//		Folgende Werte a und x können geändert werden.
//		a ein sortiertes Array
//		x das zu suchende Element
		int[] a = { 10, 20, 40, 45, 55, 60, 75, 80, 85, 100, 115, 175, 200, 250 };
		int x = 115;
		s.search(a, x);
		System.out.println(l);
		Animal.startAnimationFromAnimalScriptCode(l.toString());
	}
}
