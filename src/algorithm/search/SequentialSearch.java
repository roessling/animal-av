/*
 * Created on 03.01.2005
 *
 */
package algorithm.search;

import algorithm.animalTranslator.AnimalTranslator;
import algorithm.animalTranslator.codeItems.Hidden;
import algorithm.animalTranslator.codeItems.Off;
import algorithm.animalTranslator.codeItems.Pos;
import algorithm.animalTranslator.codeItems.TimeOffset;
import algorithm.animalTranslator.codeItems.WithinTiming;

/**
 * this class implements the algorithm to create AnimalScript-code for a
 * Sequential Search
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class SequentialSearch {

	/**
	 * the animalTranslator to be used to translate the Search to AnimalScript
	 */
	private AnimalTranslator at;

	/**
	 * the data-array (int), in which toSearch is to be searched
	 */
	private int[] intA;

	/**
	 * the element to be searched in intA
	 */
	private int toSearch;

	// private boolean intSearch = true; //ggf. spaeter StringSearch??

	/**
	 * flag to determine, whether params intA and toSearch have been set
	 */
	private boolean initialized = false;

	/**
	 * name to be used in the animation's code for the int-array
	 */
	private String arrayName;

	/**
	 * name to be used in the animation's code for the code-group, containing the
	 * java-code
	 */
	private String javaCodeName;

	/**
	 * name to be used in the animation's code for the code-group, containing the
	 * algorithm
	 */
	private String algorithmName;

	/**
	 * names to be used in the animation's code for the marker
	 */
	private String markerName;

	/**
	 * names to be used in the animation's code for the legend-item
	 */
	private String legendeName;

	/**
	 * ints that save the value of the variants used in the
	 * SequentialSearch-algorithm
	 */
	private int iii;

	/**
	 * int-arrays that contain the index of the marked lines in both, algorithm
	 * and java-code
	 */
	private int[] markedLinesInAlgorithm, markedLinesInJavaCode;

	/**
	 * flag, that determines wether javacode will be displayed instead pseudo-code
	 */
	private boolean javaCode = false;

	public SequentialSearch() {
		// do nothing	
	}

	/**
	 * constructor that saves a pointer to the animalTranslator to be used and
	 * determines wether java-code or pseudo-code will be displayed
	 * 
	 * @param newAT
	 *          AnimalTranslator to be used
	 * @param displayJavaCode
	 *          flag that determines wether java-Code will be displayed instead of
	 *          pseudo-code
	 */
	public SequentialSearch(AnimalTranslator newAT, boolean displayJavaCode) {
		at = newAT;
		javaCode = displayJavaCode;
	}

	/**
	 * initializes the instance of SequentialSearch by saving the int-array
	 * (should be sorted) and the element to be searched in that array
	 * 
	 * @param intArrayToSort
	 *          array of int's, in which a certain element will be searched
	 *          (sorted, if search shall succeed)
	 * @param toBeSearched
	 *          int - element to be searched in the int-array
	 */
	public void initialize(int[] intArrayToSort, int toBeSearched) {
		intA = intArrayToSort;
		toSearch = toBeSearched;

		arrayName = "SequentialSearchArray";
		javaCodeName = "CodeGroup_SeqSearchIterativ";
		algorithmName = "CodeGroup_Algorithm";
		markerName = "theMarker";
		legendeName = "dieLegende";
		iii = -1;

		initialized = true;
	}

	/**
	 * causes generation of the animation for the sequential search
	 * 
	 * @throws Exception
	 *           in case initialization of the instance has been forgotten
	 */
	public int generateAnimation() throws Exception {
		if (!initialized) {
			throw new Exception(
					"Instance of SequentialSearch has not been initialized!");
		} 
		at.advancedAddHeaderMM("Sequentielle Suche");
		displayInitialScreen();
		displaySortingScreen();
		return seqSearchIterativ(intA, toSearch);
	}

	/**
	 * displays the initial screen, telling that the data has to be sorted
	 */
	private void displayInitialScreen() {
		at.compositeStepStart();
		at.addLabel("initial screen");
		at.advancedCreateWorkSheet();
		at.addText("sollSortiert", "Sequentielle Suche", new Pos(400, 150),
				"color black size 40 bold", null);
		at.compositeStepEnd();

		at.compositeStepStart();
		at.addText("comment1",
				"Die einfachste und allgemeinste Methode der Suche nach Elementen.",
				new Off(-420, 130, "sollSortiert", "S"), "color black size 25", null);
		at.addText("comment2",
				"Die Elemente werden dabei der Reihe nach überprüft.", new Off(0, 40,
						"comment1", "SW"), "color black size 25", null);
		at
				.addText(
						"comment3",
						"auch als Brute-Force-Search ('Suche mit brutaler Gewalt') bezeichnet.",
						new Off(0, 40, "comment2", "SW"), "color black size 25", null);
		at.addText("comment4", "Die Komplexität ist mit O(n) sehr ungünstig.",
				new Off(0, 40, "comment3", "SW"), "color black size 25", null);
		at.compositeStepEnd();
	}

	/**
	 * displays the screen, that shows the int-array, the algorithm and the
	 * java-code for the sequential search
	 */
	private void displaySortingScreen() {
		at.compositeStepStart();
		at.addLabel("searching screen");
		at.hide(new String[] { "sollSortiert", "comment1", "comment2", "comment3",
				"comment4" }, null);
		at.advancedTextLine("headline", new Pos(300, 70),
				"color black size 30 bold", null, ("Sequentielle Suche")
						+ " nach dem Element: " + toSearch);
		at.advancedArrayReduction(arrayName, new Off(-200, 120, "headline", "NW"),
				intA);
		at.addArrayMarker(markerName, arrayName, 0, "color blue", new Hidden());
		at.addText(legendeName, "(i) aktuell betrachtete Position im Feld",
				new Off(0, -25, arrayName, "NW"), "color blue size 15", null);

		at.hide(new String[] { legendeName }, null);
		if (!javaCode) {
			at
					.advancedCodeGroupStandard(
							algorithmName,
							new Off(15, 75, arrayName, "SW"),
							new TimeOffset(0),
							new String[] {
									"1. Beginne die Suche beim ersten Element des Arrays",
									"2. Ist das aktuelle Element gleich dem gesuchten?",
									"   - Falls ja, weiter bei Schritt 5 (gefunden)",
									"3. Hat das Array noch weitere Elemente?",
									"   - falls nein, weiter bei Schritt 5 (nicht gefunden)",
									"4. Gehe zu Schritt 2 mit dem nächsten Element im Array",
									"5. Gib den entsprechenden Wert zurueck",
									"   - (-1) falls nicht gefunden",
									"   - Ansonsten wurde das Element an der aktuellen Position gefunden." });
		} else {
			// at.advancedCodeGroupStandard(javaCodeName,new Off(120, -50,
			// algorithmName, "NE"),
			at.advancedCodeGroupStandard(javaCodeName, new Off(15, 75, arrayName,
					"SW"), new TimeOffset(0), new String[] {
					"private int sequentialSearch(int[] a, int x) {",
					"if (a == null || a.length == 0) {",
					"return -1; //feld null oder leer", "}", "int i = 0;",
					"while (i < a.length && a[i] != x) {", "i++;", "}",
					"if (i < a.length) {", "return i;", "} else {", "return -1;", "}",
					"}" }, new int[] { 0, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1, 2, 1, 0 });
		}
		at.compositeStepEnd();
	}

	/**
	 * implements the Sequential Search - Algorithm and causes the animation on
	 * the SortingScreen while Search is in progress
	 * 
	 * @param a
	 *          the data-array (int), in which x is to be searched
	 * @param x
	 *          the int, that is to be searched in the array a
	 * @return index in array a, where x has been located, (-1), if x could not be
	 *         located
	 */
	private int seqSearchIterativ(int[] a, int x) {
		if (a == null || a.length == 0) {
			return returnResult(-1); // feld null oder leer
		}
		setI(0, a);
		codeMarkingForWhile(a, x);
		while (iii < a.length && a[iii] != x) {
			setI(iii + 1, a);
			codeMarkingForWhile(a, x);
		}
		if (iii < a.length) {
			return returnResult(iii);
		} 
		return returnResult(-1);
	}

	/**
	 * changes the value of the variant "mark", used in the BinarySearch-Algorithm
	 * and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "mark"
	 */
	private void setI(int newValue, int[] a) {
		at.compositeStepStart();
		if (iii == -1) {
			at.show(legendeName, null);
			at.moveArrayMarker(markerName, newValue, new WithinTiming(0));
			at.show(markerName, null);
			markLines(new int[] { 0 }, new int[] { 4 });
			// at.advancedHighlightArrayCells(arrayName, mark, newValue - 1);
		} else {
			at.advancedHighlightArrayCells(arrayName, iii, newValue - 1);
			if (newValue < a.length) {
				at.moveArrayMarker(markerName, newValue, new WithinTiming(1000));
				markLines(new int[] { 5 }, new int[] { 6 });
			}
			// markLines(new int[] {10}, null);###############
		}
		iii = newValue;
		at.compositeStepEnd();
	}

	/**
	 * marks the code-lines and the lines of the algorithm, that are in use at the
	 * beginning of the while-loop in the Binary-Search-algorithm
	 */
	private void codeMarkingForWhile(int[] a, int x) {
		if (iii < a.length) {
			markLines(new int[] { 1 }, new int[] { 5 });
			if (a[iii] != x) {
				markLines(new int[] { 3 }, null);
			}
		}
	}

	/**
	 * creates the animation that is to take place, when the final result of the
	 * binary Search is returned
	 * 
	 * @param result
	 *          the result of the binary search (index of searched element/-1, if
	 *          not found)
	 * @return same as parameter result - unchanged
	 */
	private int returnResult(int result) {
		if (result == -1) {
			at.compositeStepStart();
			at.advancedHighlightArrayCell(arrayName, iii);// ???????????????????
			markLines(new int[] { 3 }, null);
			markLines(new int[] { 3, 4 }, new int[] { 8 });
			at.compositeStepEnd();
			at.compositeStepStart();
			markLines(new int[] { 6, 7 }, new int[] { 8, 10, 11 });
			if (!javaCode) {
				at.advancedTextLine("Ergebnis", new Off(130, 100, algorithmName, "SW"),
						"color blue size 30", new TimeOffset(0),
						"Das gesuchte Element wurde nicht gefunden!");
			} else {
				at.advancedTextLine("Ergebnis", new Off(130, 100, javaCodeName, "SW"),
						"color blue size 30", new TimeOffset(0),
						"Das gesuchte Element wurde nicht gefunden!");
			}
			at.compositeStepEnd();
		} else {
			markLines(new int[] { 1, 2 }, new int[] { 8 });
			at.compositeStepStart();
			markLines(new int[] { 6, 8 }, new int[] { 8, 9 });
			if (!javaCode) {
				at.advancedTextLine("Ergebnis", new Off(130, 50, algorithmName, "SW"),
						"color blue size 30", new TimeOffset(0),
						"Das gesuchte Element wurde bei index " + iii + " gefunden!");
			} else {
				at.advancedTextLine("Ergebnis", new Off(130, 50, javaCodeName, "SW"),
						"color blue size 30", new TimeOffset(0),
						"Das gesuchte Element wurde bei index " + iii + " gefunden!");
			}
			at.advancedTextLine("Ergebnis2", new Off(200, 15, "Ergebnis", "SW"),
					"color blue size 15", new TimeOffset(0),
					"(der index beginnt bei 0 und geht bis a.length - 1)");
			at.compositeStepEnd();
		}
		at.addLabel("SortingResult");
		return result;
	}

	/**
	 * marks lines in both texts, the java-code and the algorithm, and
	 * unhighlightens the lines, that were highlightened before
	 * 
	 * @param linesInAlgorithm
	 *          int-array, that contains the lines to be marked in the algorithm
	 * @param linesInJavaCode
	 *          int-array, that contains the lines to be marked in the java-code
	 */
	private void markLines(int[] linesInAlgorithm, int[] linesInJavaCode) {
		if (linesInAlgorithm == null && linesInJavaCode == null) {
			System.out
					.println("useless call of markLines in Sequential Search - all parameters null");
		} else if ((linesInAlgorithm == null && !javaCode)
				|| (linesInJavaCode == null && javaCode)) {
			// nothing!
		} else {
			at.compositeStepStart();
			if (markedLinesInAlgorithm == null) {
				markedLinesInAlgorithm = new int[0];
			}
			if (markedLinesInJavaCode == null) {
				markedLinesInJavaCode = new int[0];
			}
			if (linesInAlgorithm != null && !javaCode) {
				for (int i = 0; i < markedLinesInAlgorithm.length; i++) {
					at
							.unhighlightCode(algorithmName, markedLinesInAlgorithm[i], "",
									null);
				}
				markedLinesInAlgorithm = linesInAlgorithm;
				for (int i = 0; i < markedLinesInAlgorithm.length; i++) {
					at.highlightCode(algorithmName, markedLinesInAlgorithm[i], "", null);
				}
			}
			if (linesInJavaCode != null && javaCode) {
				for (int i = 0; i < markedLinesInJavaCode.length; i++) {
					at.unhighlightCode(javaCodeName, markedLinesInJavaCode[i], "", null);
				}
				markedLinesInJavaCode = linesInJavaCode;
				for (int i = 0; i < markedLinesInJavaCode.length; i++) {
					at.highlightCode(javaCodeName, markedLinesInJavaCode[i], "", null);
				}
			}
			at.compositeStepEnd();
		}
	}

}
