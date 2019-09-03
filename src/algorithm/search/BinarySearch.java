/*
 * Created on 10.11.2004
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
 * TODO pasting single text as text - not as group...
 */

/**
 * this class implements the algorithm to create AnimalScript-code for a Binary
 * Search
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class BinarySearch {

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
	 * names to be used in the animation's code for the markers
	 */
	private String markerLName, markerRName, markerMidElemName;

	/**
	 * names to be used in the animation's code for the legend-items
	 */
	private String legendeLName, legendeMidElemName;

	private String legendeRName;

	/**
	 * ints that save the value of the variants used in the BinarySearch-algorithm
	 */
	private int l, r, midElem;

	/**
	 * int-arrays that contain the index of the marked lines in both, algorithm
	 * and java-code
	 */
	private int[] markedLinesInAlgorithm, markedLinesInJavaCode;

	/**
	 * boolean, that determines, wether generated sorting-algorithm uses
	 * interpolation
	 */
	private boolean interpolation = false;

	/**
	 * flag, that determines wether javacode will be displayed instead pseudo-code
	 */
	private boolean javaCode = false;

	/**
	 * int used in the name for the text-field, containing the nebenrechnung
	 */
	private int counterNebenRechnung = 0;

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
	public BinarySearch(AnimalTranslator newAT, boolean displayJavaCode) {
		this(newAT, false, displayJavaCode);
	}

	public BinarySearch() {
		// do nothing
	}
	
	/**
	 * constructor that saves a pointer to the animalTranslator to be used and
	 * determines wether interpolation is used and wether java-code or pseudo-code
	 * will be displayed
	 * 
	 * @param newAT
	 *          AnimalTranslator to be used
	 * @param interpolationSort
	 *          flag, that determines, wether the generated sorting-algorithm uses
	 *          interpolation
	 * @param displayJavaCode
	 *          flag that determines wether java-Code will be displayed instead of
	 *          pseudo-code
	 */
	BinarySearch(AnimalTranslator newAT, boolean interpolationSearch,
			boolean displayJavaCode) {
		at = newAT;
		interpolation = interpolationSearch;
		javaCode = displayJavaCode;
	}

	/**
	 * initializes the instance of BinarySearch by saving the int-array (should be
	 * sorted) and the element to be searched in that array
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

		arrayName = interpolation ? "InterpolationSearchArray"
				: "BinarySearchArray";
		javaCodeName = interpolation ? "CodeGroup_IntSearchIterativ"
				: "CodeGroup_BinSearchIterativ";
		algorithmName = "CodeGroup_Algorithm";
		markerLName = "l";
		markerRName = "r";
		markerMidElemName = "midElem";
		legendeLName = "legendeL";
		legendeMidElemName = "legendeMidElem";
		legendeRName = "legendeR";
		l = -1;
		r = -1;
		midElem = -1;

		initialized = true;
	}

	/**
	 * causes generation of the animation for the binary search
	 * 
	 * @throws Exception
	 *           in case initialization of the instance has been forgotten
	 */
	public int generateAnimation() throws Exception {
		if (!initialized) {
			throw new Exception("Instance of BinarySearch has not been initialized!");
		} 
		at.advancedAddHeaderMM(interpolation ? "Interpolationssuche"
				: "Binaere Suche");
		displayInitialScreen();
		displaySortingScreen();
		return binSearchIterativ(intA, toSearch);
	}

	/**
	 * displays the initial screen, telling that the data has to be sorted
	 */
	private void displayInitialScreen() {
		at.compositeStepStart();
		at.addLabel("initial screen");
		at.advancedCreateWorkSheet();
		at.addText("sollSortiert", interpolation ? "Interpolationssuche"
				: "Binaere Suche", new Pos(400, 150), "color black size 40 bold", null);
		at.compositeStepEnd();

		at.compositeStepStart();
		at.addText("sollSortiert2", "funktioniert nur auf", new Off(-200, 150,
				"sollSortiert", "S"), "color red size 40", null);
		at.addText("sollSortiert3", "vorsortierten Daten!!", new Off(0, 60,
				"sollSortiert2", "SW"), "color red size 40", null);
		at.addText("sollSortiert4",
				"(ansonsten wird gesuchtes Element in der Regel nicht gefunden)",
				new Off(-100, 50, "sollSortiert3", "SW"), "color red size 20", null);
		if (interpolation) {
			at
					.addText(
							"comment1",
							"Die Interpolationssuche entspricht konzeptuell der Suche im Telefonbuch",
							new Off(-200, 100, "sollSortiert4", "SW"), "color black size 25",
							null);
			at
					.addText(
							"comment2",
							"- Aufschlagen einer möglichen Seite und schauen, wo man sich befindet",
							new Off(0, 40, "comment1", "SW"), "color black size 25", null);
			at
					.addText(
							"comment3",
							"- Je nach Entfernung des gewünschten Eintrags vom aktuellen blättert",
							new Off(0, 40, "comment2", "SW"), "color black size 25", null);
			at.addText("comment4", "  man entsprechend weit vor bzw. zurück.",
					new Off(0, 25, "comment3", "SW"), "color black size 25", null);
		}
		at.compositeStepEnd();
	}

	/**
	 * displays the screen, that shows the int-array, the algorithm and the
	 * java-code for the binary search
	 */
	private void displaySortingScreen() {
		at.compositeStepStart();
		at.addLabel("searching screen");
		at.hide(new String[] { "sollSortiert", "sollSortiert2", "sollSortiert3",
				"sollSortiert3a", "sollSortiert3b", "sollSortiert4", "comment1",
				"comment2", "comment3", "comment4" }, null);
		at.advancedTextLine("headline", new Pos(300, 70),
				"color black size 30 bold", null,
				(interpolation ? "Interpolationssuche" : "Binaere Suche")
						+ " nach dem Element: " + toSearch);
		at.advancedArrayReduction(arrayName, new Off(-200, 120, "headline", "NW"),
				intA);
		at.addArrayMarker(markerLName, arrayName, 0, "color blue", new Hidden());
		at.addArrayMarker(markerRName, arrayName, intA.length - 1, "color green3",
				new Hidden());
		at.addArrayMarker(markerMidElemName, arrayName, intA.length - 1,
				"color red", new Hidden());
		// at.addText(legendeLName, "(l) linke Grenze betrachtetes Feld",
		// new Off(0, -25,arrayName, "NW"),
		// "color blue size 15", null);
		at.advancedTextLine(legendeLName, new Off(0, -25, arrayName, "NW"),
				"color blue size 15", null, "(l) linke Grenze betrachtetes Feld");
		// at.addText(legendeMidElemName,
		// interpolation ?
		// "(interpPos) interpolierte Position des gesuchen Elements" :
		// "(midElem) mittleres Element des betrachteten Feldes"
		// ,new Off(30, 0,legendeLName, "E"),
		// "color red size 15", null);
		at
				.advancedTextLine(
						legendeMidElemName,
						new Off(30, 0, legendeLName, "NE"),
						"color red size 15",
						null,
						interpolation ? "(midElem) interpolierte Position des gesuchen Elements"
								: "(midElem) mittleres Element des betrachteten Feldes");
		// at.addText(legendeRName, "(r) rechte Grenze betrachtetes Feld",
		// new Off(30, 0,legendeMidElemName, "NE"),
		// "color green3 size 15", null);
		at.advancedTextLine(legendeRName, new Off(30, 0, legendeMidElemName, "NE"),
				"color green3 size 15", null, "(r) rechte Grenze betrachtetes Feld");
		at.hide(new String[] { legendeLName, legendeMidElemName, legendeRName },
				null);
		if (!javaCode) {
			at
					.advancedCodeGroupStandard(
							algorithmName,
							new Off(15, 75, arrayName, "SW"),
							new TimeOffset(0),
							new String[] {
									"1. Betrachte zunächst das gesamte Feld",
									interpolation ? "2. interpoliere Position des gesuchten Elements im Feld"
											: "2. Bestimme die mittlere Posistion des (Teil-)Feldes",
									interpolation ? "   midElem = l + ((x - a[l]) * (r - l)) / (a[r] - a[l]);"
											: "   midElem = (l + r) / 2;",
									interpolation ? "3. Ist das interpolierte Element gleich dem gesuchten?"
											: "3. Ist das Element in der Mitte gleich dem gesuchten?",
									"   - Falls ja, weiter bei Schritt 7 (gefunden)",
									"4. Ist das betrachtete Feld weiter teilbar? (min. 2 Elemente)",
									"   - falls nein, weiter bei Schritt 7 (nicht gefunden)",
									interpolation ? "5. Ist das gesuchte Element kleiner als das interpolierte?"
											: "5. Ist das gesuchte Element kleiner als das mittlere?",
									"   - Falls ja, betrachte nun das linke Teilfeld",
									"   - Andernfalls betrachte das rechte Teilfeld",
									"6. Gehe zu Schritt 2 mit dem neuen Teilfeld",
									"7. Gib den entsprechenden Wert zurück",
									"   - (-1) falls nicht gefunden",
									interpolation ? "   - Ansonsten wurde das Element an der interpolierten Position im"
											: "   - Ansonsten wurde das Element an der mittleren Position des",
									interpolation ? "     zuletzt betrachteten Teilfeld gefunden."
											: "     zuletzt betrachteten Teilfeldes gefunden." });
			at.addText("nebenrechnung", "Nebenrechnung zu Punkt 2:", new Off(50, 33,
					algorithmName, "NE"), "color black size 20", null);
		} else {
			// at.advancedCodeGroupStandard(javaCodeName,new Off(120, -50,
			// algorithmName, "NE"),
			at
					.advancedCodeGroupStandard(
							javaCodeName,
							new Off(15, 75, arrayName, "SW"),
							new TimeOffset(0),
							new String[] {
									"private int binSearchIterativ(int[] a, int x) {",
									"if (a == null || a.length == 0) {",
									"return -1;",
									"}",
									interpolation ? "int l = 0, r = a.length - 1, midElem = "
											+ "l + ((x - a[l]) * (r - l)) / (a[r] - a[l]);"
											: "int l = 0, r = a.length - 1, midElem = (l + r) / 2;",
									"while (a[midElem] != x && r > l) {",
									"if (x < a[midElem]) {",
									"r = midElem - 1;",
									"} else {",
									"l = midElem + 1;",
									"}",
									interpolation ? "midElem = l + ((x - a[l]) * (r - l)) / (a[r] - a[l]);"
											: "midElem = (l + r) / 2;", "}",
									"if (a[midElem] == x) {", "return midElem;", "} else {",
									"return -1;", "}", "}" }, new int[] { 0, 1, 2, 1, 1, 1, 2, 3,
									2, 3, 2, 2, 1, 1, 2, 1, 2, 1, 0 });
			at.addText("nebenrechnung", "Nebenrechnung zur Berechnung von midElem:",
					new Off(-20, 150, javaCodeName, "NE"), "color black size 20", null);
		}
		at.compositeStepEnd();
	}

	/**
	 * implements the Binary Search - Algorithm and causes the animation on the
	 * SortingScreen while Search is in progress
	 * 
	 * @param a
	 *          the data-array (int), in which x is to be searched
	 * @param x
	 *          the int, that is to be searched in the array a
	 * @return index in array a, where x has been located, (-1), if x could not be
	 *         located
	 */
	private int binSearchIterativ(int[] a, int x) {
		if (a == null || a.length == 0) {
			return returnResult(-1);
		}
		setL(0);
		setR(a.length - 1);
		if (!interpolation) {
			setMidElem((l + r) / 2, a, x);
		} else {
			setMidElem(l + ((x - a[l]) * (r - l)) / (a[r] - a[l]), a, x);
		}
		while (a[midElem] != x && r > l) {
			codeMarkingForWhile();
			if (x < a[midElem]) {
				setR(midElem - 1);
			} else {
				setL(midElem + 1);
			}
			if (!interpolation) {
				setMidElem((l + r) / 2, a, x);
			} else {
				setMidElem(l + ((x - a[l]) * (r - l)) / (a[r] - a[l]), a, x);
			}
		}
		if (a[midElem] == x) {
			return returnResult(midElem);
		} 
		return returnResult(-1);
	}

	/**
	 * changes the value of the variant "l", used in the BinarySearch-Algorithm
	 * and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "l"
	 */
	private void setL(int newValue) {
		at.compositeStepStart();
		if (l == -1) {
			at.show(legendeLName, null);
			at.moveArrayMarker(markerLName, newValue, new WithinTiming(0));
			at.show(markerLName, null);
			markLines(new int[] { 0 }, new int[] { 4 });
			// at.advancedHighlightArrayCells(arrayName, l, newValue - 1);
			l = newValue;
			at.compositeStepEnd();
		} else {
			at.moveArrayMarker(markerLName, newValue, new WithinTiming(1000));
			markLines(new int[] { 7, 9 }, new int[] { 6, 8, 9 });
			at.advancedHighlightArrayCells(arrayName, l, newValue - 1);
			l = newValue;
			at.compositeStepEnd();
			markLines(new int[] { 10 }, null);
		}
	}

	/**
	 * changes the value of the variant "r", used in the BinarySearch-Algorithm
	 * and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "r"
	 */
	private void setR(int newValue) {
		at.compositeStepStart();
		if (r == -1) {
			at.show(legendeRName, null);
			at.moveArrayMarker(markerRName, newValue, new WithinTiming(0));
			at.show(markerRName, null);
			// at.advancedHighlightArrayCells(arrayName, newValue + 1, r);
			r = newValue;
			at.compositeStepEnd();
		} else {
			at.moveArrayMarker(markerRName, newValue, new WithinTiming(1000));
			markLines(new int[] { 7, 8 }, new int[] { 6, 7 });
			at.advancedHighlightArrayCells(arrayName, newValue + 1, r);
			r = newValue;
			at.compositeStepEnd();
			markLines(new int[] { 10 }, null);
		}
	}

	/**
	 * changes the value of the variant "midElem", used in the
	 * BinarySearch-Algorithm and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "midElem"
	 */
	private void setMidElem(int newValue, int[] a, int x) {
		at.compositeStepStart();
		if (midElem == -1) {
			at.show(legendeMidElemName, null);
			at.highlightArrayElem(arrayName, newValue, null, null);
			at.moveArrayMarker(markerMidElemName, newValue, new WithinTiming(0));
			at.show(markerMidElemName, null);
			markLines(new int[] { 1, 2 }, null);
		} else {
			at.advancedHighlightArrayElemSwitch(arrayName, midElem, newValue);
			at.moveArrayMarker(markerMidElemName, newValue, new WithinTiming(1000));
			markLines(new int[] { 1, 2 }, new int[] { 11 });
		}
		if (interpolation) {
			// setMidElem(l + ((x - a[l]) * (r - l)) / (a[r] - a[l]));
			displayNebenrechnung(l + " + ((" + x + " - " + a[l] + ") * (" + r + " - "
					+ l + ")) / (" + a[r] + " - " + a[l] + "))", "= " + l + " + "
					+ ((x - a[l]) * (r - l)) + " / " + (a[r] - a[l]) + " = " + newValue);
		} else {
			// setMidElem((l + r) / 2, a, x);
			displayNebenrechnung("(" + l + " + " + r + ") / 2", "= " + newValue);
		}
		midElem = newValue;
		at.compositeStepEnd();
	}

	/**
	 * marks the code-lines and the lines of the algorithm, that are in use at the
	 * beginning of the while-loop in the Binary-Search-algorithm
	 */
	private void codeMarkingForWhile() {
		markLines(new int[] { 3 }, new int[] { 5 });
		markLines(new int[] { 5 }, null);
		markLines(new int[] { 7 }, new int[] { 6 });
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
			markLines(new int[] { 3 }, new int[] { 5 });
			markLines(new int[] { 5 }, null);
			at.compositeStepStart();
			at.advancedHighlightArrayCell(arrayName, midElem);
			markLines(new int[] { 5, 6 }, new int[] { 13 });
			at.compositeStepEnd();
			at.compositeStepStart();
			markLines(new int[] { 11, 12 }, new int[] { 13, 15, 16 });
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
			markLines(new int[] { 3 }, new int[] { 5 });
			markLines(new int[] { 3, 4 }, new int[] { 13 });
			at.compositeStepStart();
			markLines(new int[] { 11, 13, 14 }, new int[] { 13, 14 });
			if (!javaCode) {
				at.advancedTextLine("Ergebnis", new Off(130, 50, algorithmName, "SW"),
						"color blue size 30", new TimeOffset(0),
						"Das gesuchte Element wurde bei index " + midElem + " gefunden!");
			} else {
				at.advancedTextLine("Ergebnis", new Off(130, 50, javaCodeName, "SW"),
						"color blue size 30", new TimeOffset(0),
						"Das gesuchte Element wurde bei index " + midElem + " gefunden!");
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
					.println("useless call of markLines in Binary-Search - all parameters null");
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

	/**
	 * displays the 'nebenrechnung'
	 * 
	 * @param txt1
	 *          first line of the nebenrechnung
	 * @param txt2
	 *          second line of the nebenrechnung
	 */
	private void displayNebenrechnung(String txt1, String txt2) {
		if (counterNebenRechnung > 0) {
			at.hide("Nebenrechnung" + counterNebenRechnung, null);
			at.hide("NebenrechnungZwei" + counterNebenRechnung, null);
		}
		counterNebenRechnung++;
		at.addText("Nebenrechnung" + counterNebenRechnung, txt1, new Off(0, 20,
				"nebenrechnung", "SW"), "color black size 20", null);
		at.addText("NebenrechnungZwei" + counterNebenRechnung, txt2, new Off(0, 20,
				"Nebenrechnung" + counterNebenRechnung, "SW"), "color black size 20",
				null);
	}

}
