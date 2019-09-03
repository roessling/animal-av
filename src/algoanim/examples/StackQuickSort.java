/**
 * 
 */
package algoanim.examples;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.StackProperties;
import algoanim.util.Coordinates;

/**
 * @author stephan, Dima Vronskyi
 *
 */
public class StackQuickSort {
	
	private Language lang;
	
	ConceptualStack<String> cs;
	
	public StackQuickSort(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}
	
	private static final String DESCRIPTION = 
		"QuickSort wählt ein Element aus der zu sortierenden Liste aus "
		+"(Pivotelement) und zerlegt die Liste in zwei Teillisten, eine untere, "
		+"die alle Elemente kleiner und eine obere, die alle Elemente gleich oder "
		+"größer dem Pivotelement enthält.\nDazu wird zunächst ein Element von unten "
		+"gesucht, das größer als (oder gleich groß wie) das Pivotelement und damit "
		+"für die untere Liste zu groß ist. Entsprechend wird von oben ein kleineres "
		+"Element als das Pivotelement gesucht. Die beiden Elemente werden dann "
		+"vertauscht und landen damit in der jeweils richtigen Liste.\nDer Vorgang "
		+"wird fortgesetzt, bis sich die untere und obere Suche treffen. Damit sind "
		+"die oben erwähnten Teillisten in einem einzigen Durchlauf entstanden. "
		+"Suche und Vertauschung können in-place durchgeführt werden."
		+"\n\nDie noch unsortierten Teillisten werden über denselben Algorithmus "
		+"in noch kleinere Teillisten zerlegt (z. B. mittels Rekursion) und, sobald "
		+"nur noch Listen mit je einem Element vorhanden sind, wieder zusammengesetzt. "
		+"Die Sortierung ist damit abgeschlossen.";
	
//	private static final String HEADER_TEXT = "\n"
//		+ "text \"f2-01\" \"Der Algorithmus in Worten\" at (120,50) color black font SansSerif size 32 bold\n"
//		+ "text \"f2-02\" \"1. Bestimme ein Pivotelement pivot\" at (20,100) color black font SansSerif size 24\n"
//		+ "{\n"
//		+ "text \"f2-03\" \"2. Partitioniere so, dass alle Werte vor Position i kleiner,\" at (20,140) color black font SansSerif size 24\n"
//		+ "text \"f2-03a\" \"alle Werte nach Position i größer als das Pivot sind,\" at (20,180) color black font SansSerif size 24\n"
//		+ "}\n"
//		+ "text \"f2-04\" \"3. Vertausche das Pivotelement mit dem Element an Position i\" at (20,220) color black font SansSerif size 24\n"
//		+ "text \"f2-05\" \"4. Starte quicksort(l, i-1)\" at (20,260) color black font SansSerif size 24\n"
//		+ "text \"f2-05\" \"5. Starte quicksort(i+1, r)\" at (20,300) color black font SansSerif size 24\n";
	
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
	
	public void sort(int[] a) {

		// Create Array: coordinates, data, name, display options, 
		// default  properties
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
        Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
        Color.RED);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
        Color.YELLOW);
		IntArray ia = lang.newIntArray(new Coordinates(20, 100), a, "intArray", 
				null, arrayProps);
		lang.nextStep();
		
		
		// Create SourceCode: coordinates, name, display options, 
		// default properties
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
        Color.RED);   
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, scProps);
		
		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("public void quickSort(int[] array, int l, int r)", null, 0, null);  // 0
		sc.addCodeLine("{", null, 0, null); 
		sc.addCodeLine("int i, j, pivot;", null, 1, null); 
		sc.addCodeLine("if (r>l)", null, 1, null);  // 3
		sc.addCodeLine("{", null, 1, null);  // 4
		sc.addCodeLine("pivot = array[r];", null, 2, null);  // 5
		sc.addCodeLine("for (i = l; j = r - 1; i < j; )", null, 2, null);  // 6
		sc.addCodeLine("{", null, 2, null); // 7
		sc.addCodeLine("while (array[i] <= pivot && j > i)", null, 3, null); // 8
		sc.addCodeLine("i++;", null, 4, null); // 9
		sc.addCodeLine("while (pivot < array[j] && j > i)", null, 3, null); // 10
		sc.addCodeLine("j--;", null, 4, null); // 11
		sc.addCodeLine("if (i < j)", null, 3, null); // 12
		sc.addCodeLine("swap(array, i, j);", null, 4, null); // 13
		sc.addCodeLine("}", null, 2, null); // 14
		sc.addCodeLine("if (pivot < array[i])", null, 2, null); // 15
		sc.addCodeLine("swap(array, i, r);", null, 3, null); // 16
		sc.addCodeLine("else", null, 2, null); // 17
		sc.addCodeLine("i=r;", null, 3, null); // 18
		sc.addCodeLine(" quickSort(array, l, i - 1);", null, 2, null); // 19
		sc.addCodeLine(" quickSort(array, i + 1, r);", null, 2, null); // 20
		sc.addCodeLine(" }", null, 1, null); // 21
		sc.addCodeLine("}", null, 0, null); // 22
		
		lang.nextStep();
		StackProperties sp = new StackProperties();
		sp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		sp.set(AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY, Color.ORANGE);
		sp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		sp.set(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY, true);
		sp.set(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY, Color.GREEN);
		sp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		
		cs = lang.newConceptualStack(new Coordinates(400, 160), null,
				"CStack", null, sp);
		lang.nextStep();
		
		// Highlight all cells
		ia.highlightCell(0, ia.getLength() - 1, null, null);
		try {
			// Start quicksort
			cs.push("quicksort(0, "+(ia.getLength() - 1)+")", null, null);
			lang.nextStep();
			quickSort(ia, sc, 0, (ia.getLength() - 1));
			cs.pop(null, null);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
	}
	
	private int pointerCounter = 0;
	
	/**
	 * Quicksort: Sortiere mittels Pivotelement in Teilmenge "kleiner/ groesser
	 * als Pivot". Komplexitaet: O(n log n) bzw. O(n<sup>2</sup>)
	 */
	private void quickSort(IntArray array, SourceCode codeSupport, int l, int r) 
	throws LineNotExistsException {
		
		// Highlight first line
		// Line, Column, use context colour?, display options, duration
		codeSupport.highlight(0, 0, false);
		lang.nextStep();
		
		// Highlight next line
		codeSupport.toggleHighlight(0, 0, false, 2, 0);
		
		// Create two markers to point on i and j
		pointerCounter++;
		// Array, current index, name, display options, properties
	    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
	    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");   
	    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ArrayMarker iMarker = lang.newArrayMarker(array, 0, "i" + pointerCounter, 
        null, arrayIMProps);
		pointerCounter++;

	    ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
	    arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");   
	    arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    ArrayMarker jMarker = lang.newArrayMarker(array, 0, "j" + pointerCounter, 
	        null, arrayJMProps);

	    int i, j;
	
		lang.nextStep();
		// Highlight next line
		codeSupport.toggleHighlight(2, 0, false, 3, 0);

		// Create a marker for the pivot element
		int pivot;
		pointerCounter++;
	    ArrayMarkerProperties arrayPMProps = new ArrayMarkerProperties();
	    arrayPMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "pivot");   
	    arrayPMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

		ArrayMarker pivotMarker = lang.newArrayMarker(array, 0, 
        "pivot" + pointerCounter, null, arrayPMProps);
	
		lang.nextStep();

		codeSupport.unhighlight(3, 0, false);
		if (r > l) {
			lang.nextStep();
			// Highlight next line
			codeSupport.highlight(5, 0, false);
			
			// Receive the value of the pivot element
			pivot = array.getData(r);
			// Move marker to that position
			pivotMarker.move(r, null, null);
			
			
			lang.nextStep();
			codeSupport.unhighlight(5, 0, false);
			for (i = l, j = r - 1; i < j;) {
				// Highlight next line
				codeSupport.highlight(6, 0, false);
				// Move the two markers i,j to their proper positions
				iMarker.move(i, null, null);
				jMarker.move(j, null, null);
					
				lang.nextStep();
				// Highlight next line
				codeSupport.toggleHighlight(6, 0, false, 8, 0);

//				codeSupport.unhighlight(6, 0, false);
//				codeSupport.highlight(8, 0, false);
				
				while (array.getData(i) <= pivot && j > i) {
					lang.nextStep();
					i++;
					
					// Highlight next line
					codeSupport.toggleHighlight(8, 0, false, 9, 0);
					// Move marker i to its next position
					iMarker.move(i, null, null);
					
					lang.nextStep();
					// Highlight next line
					codeSupport.toggleHighlight(9, 0, false, 8, 0);
//					codeSupport.unhighlight(9, 0, false);
//					codeSupport.highlight(8, 0, false);
				}

				lang.nextStep();
				// Highlight next line
				codeSupport.toggleHighlight(8, 0, false, 10, 0);
//				codeSupport.unhighlight(8, 0, false);
//				codeSupport.highlight(10, 0, false);
				while (pivot < array.getData(j) && j > i) {
					lang.nextStep();

					j--;
					// Highlight next line
					codeSupport.toggleHighlight(10, 0, false, 11, 0);

//					codeSupport.unhighlight(10, 0, false);
//					codeSupport.highlight(11, 0, false);

					// Move marker j to its next position
					jMarker.move(j, null, null);
					
					lang.nextStep();
					// Highlight next line
					codeSupport.toggleHighlight(11, 0, false, 10, 0);

//					codeSupport.unhighlight(11, 0, false);
//					codeSupport.highlight(10, 0, false);				
				}
				
				lang.nextStep();
				// Highlight next line
				codeSupport.toggleHighlight(10, 0, false, 12, 0);

//				codeSupport.unhighlight(10, 0, false);
//				codeSupport.highlight(12, 0, false);				
				
				if (i < j) {
					lang.nextStep();
					// Highlight next line
					codeSupport.toggleHighlight(12, 0, false, 13, 0);

//					codeSupport.unhighlight(12, 0, false);
//					codeSupport.highlight(13, 0, false);
					
					// Swap the array elements at position i and j
					array.swap(i, j, null, null);
				}
				lang.nextStep();
				// Highlight next line
				codeSupport.toggleHighlight(13, 0, false, 12, 0);

//				codeSupport.unhighlight(13, 0, false);
//				codeSupport.unhighlight(12, 0, false); 
				// if (!(i < j))
			} // end for...
			// Highlight next line
			codeSupport.toggleHighlight(6, 0, false, 13, 0);

//			codeSupport.unhighlight(6, 0, false);
//			codeSupport.highlight(15, 0, false);
			
			lang.nextStep();
			if (pivot < array.getData(i)) {
				// Highlight next line
				codeSupport.toggleHighlight(15, 0, false, 16, 0);

//				codeSupport.unhighlight(15, 0, false);
//				codeSupport.highlight(16, 0, false);
				
				// Swap the array elements at position i and r
				array.swap(i, r, null, null);
				// Set pivot marker to position i
				pivotMarker.move(i, null, null);
				
				lang.nextStep();
				codeSupport.unhighlight(16, 0, false);
			} else {
				i = r;
				// Highlight next line
				codeSupport.toggleHighlight(15, 0, false, 18, 0);

//				codeSupport.unhighlight(15, 0, false);
//				codeSupport.highlight(18, 0, false);
				// Move marker i to position r
				iMarker.move(r, null, null);
				
				lang.nextStep();
				codeSupport.unhighlight(18, 0, false);
			}
			// Highlight the i'th array element
			array.highlightElem(i, null, null);
			
			lang.nextStep();
			codeSupport.highlight(19, 0, false);
			
			lang.nextStep();
			codeSupport.unhighlight(19, 0, false);
			
			// Unhighlight cells from i to r
			// this part is not scheduled...
			array.unhighlightCell(i, r, null, null); 
			
			/*if (i-l > r-i) {
				lang.nextStep();
				cs.push(l, null, null);
				lang.nextStep();
				cs.push(i-1, null, null);
			}
			lang.nextStep();
			cs.push(i+l, null, null);
			lang.nextStep();
			cs.push(r, null, null);
			if (r-i >= i-l) {
				lang.nextStep();
				cs.push(l, null, null);
				lang.nextStep();
				cs.push(i-1, null, null);
			}*/
		
			cs.push("quicksort("+l+", "+(i-1)+")", null, null);
			
			// Apply quicksort to the left array part
			quickSort(array, codeSupport, l, i - 1);
			
			cs.pop(null, null);
			
			// Left recursion finished.
			lang.nextStep();
			// Highlight cells l to r
			array.highlightCell(l, r, null, null);
			codeSupport.highlight(20, 0, false);
			
			lang.nextStep();
			codeSupport.unhighlight(20, 0, false);
			// Unhighlight cells l to i
			array.unhighlightCell(l, i, null, null);
			
			cs.push("quicksort("+(i+1)+", "+r+")", null, null);
			
			// Apply quicksort to the right array part
			quickSort(array, codeSupport, i + 1, r);
			
			cs.pop(null, null);
			}
		lang.nextStep();
		// Highlight next line
		codeSupport.highlight(21, 0, false);
		lang.nextStep();
		// Highlight next line
		codeSupport.highlight(22, 0, false);
		
		lang.nextStep();
		// Unhighlight cells from l to r
		array.unhighlightCell(l, r, null, null);
	}
	
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
	
	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height
		Language l = new AnimalScript("Quicksort Animation with Stack", "Dima Vronskyi", 640, 480);
		StackQuickSort s = new StackQuickSort(l);
		int[] a = {7,3,2,4,1,13,52,13,5,1};
		s.sort(a);
		System.out.println(l);
	}
}