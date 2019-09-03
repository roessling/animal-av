/*
 * Created on 18.01.2005	
 *
 */
package algorithm.stringSearch;

import algorithm.animalTranslator.AnimalTranslator;
import algorithm.animalTranslator.codeItems.Hidden;
import algorithm.animalTranslator.codeItems.Off;
import algorithm.animalTranslator.codeItems.Pos;
import algorithm.animalTranslator.codeItems.TimeOffset;
import algorithm.animalTranslator.codeItems.WithinTiming;

/**
 * this class implements the algorithm to create AnimalScript-code for a
 * BruteForceSearch in Strings
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class BruteForceStringSearch {

	/**
	 * the animalTranslator to be used to translate the Search to AnimalScript
	 */
	private AnimalTranslator at;

	/**
	 * the String, in which stringToSearch is to be searched
	 */
	private String dataString;

	/**
	 * the String to be searched in dataString
	 */
	private String stringToSearch;

	/**
	 * flag to determine, whether params dataString and stringToSearch have been
	 * set
	 */
	private boolean initialized = false;

	/**
	 * name to be used in the animation's code for the array
	 */
	private String stringArrayName;

	private String subStringArrayName;

	/**
	 * name to be used in the animation's code for the code-group, containing the
	 * algorithm
	 */
	private String algorithmName;

	/**
	 * names to be used in the animation's code for the marker
	 */
	private String markerIName;

	private String markerJName;

	private String markerStartName;

	/**
	 * names to be used in the animation's code for the legend-item
	 */
	private String legendeIName;

	private String legendeJName;

	private String legendeStartName;

	/**
	 * ints that save the value of the variants used in the SringSearch-algorithm
	 */
	private int textPos, xPos;

	/**
	 * int-arrays that contain the index of the marked lines in the algorithm
	 */
	private int[] markedLinesInAlgorithm;

	/**
	 * constructor that saves a pointer to the animalTranslator to be used
	 * 
	 * @param newAT
	 *          AnimalTranslator to be used
	 */
	public BruteForceStringSearch(AnimalTranslator newAT) {
		at = newAT;
	}

	/**
	 * initializes the instance of BruteForceStringSearch by saving the String and
	 * the SubString to be searched in that String
	 * 
	 * @param theString
	 *          String, in which toBeSearched shall be found
	 * @param toBeSearched
	 *          SubString to be searched in theString
	 */
	public void initialize(String theString, String toBeSearched) {
		dataString = theString;
		stringToSearch = toBeSearched;

		stringArrayName = "BruteForceSearchArray";
		subStringArrayName = "BruteForceSearchArraySubString";
		algorithmName = "CodeGroup_Algorithm";
		markerIName = "markerInString";
		markerJName = "markerInSubString";
		markerStartName = "markerInStringStart";
		legendeIName = "dieLegendeStringPointer";
		legendeJName = "dieLegendeSubStringPointer";
		legendeStartName = "dieLegendeStringCompareStart";
		textPos = -1;
		xPos = -1;

		initialized = true;
	}

	/**
	 * causes generation of the animation for the brute force string-search
	 * 
	 * @throws Exception
	 *           in case initialization of the instance has been forgotten
	 * @return the position of the element searched for, if existing.
	 */
	public int generateAnimation() throws Exception {
		if (!initialized) {
			throw new Exception(
					"Instance of BruteForce-StringSearch has not been initialized!");
		} 
		at.advancedAddHeaderMM("BruteForce String-Suche");
		displayInitialScreen();
		displaySearchScreen();
		return bFSSearchIterativ(dataString, stringToSearch);
	}

	/**
	 * displays the initial screen
	 */
	private void displayInitialScreen() {
		at.compositeStepStart();
		at.addLabel("initial screen");
		at.advancedCreateWorkSheet();
		at.addText("sollSortiert", "BruteForce-Suche nach einer Zeichenkette",
				new Pos(200, 200), "color black size 35 bold", null);
		at.compositeStepEnd();

		at.compositeStepStart();
		at
				.addText(
						"comment1",
						"Die einfachste und allgemeinste Methode der Suche nach Zeichenketten.",
						new Off(-420, 130, "sollSortiert", "S"), "color black size 25",
						null);
		at.addText("comment2",
				"Hierbei wird sequentiel für jedes Zeichen im Text überprüft,",
				new Off(0, 40, "comment1", "SW"), "color black size 25", null);
		at.addText("comment3", "ob hier die gesuchte Zeichenkette beginnt.",
				new Off(0, 40, "comment2", "SW"), "color black size 25", null);
		at
				.addText(
						"comment4",
						"Daher auch der Name 'Brute-Force-Suche' ('Suche mit brutaler Gewalt').",
						new Off(0, 40, "comment3", "SW"), "color black size 25", null);
		at.compositeStepEnd();
	}

	/**
	 * displays the screen, that shows the strings and the algorithm for the
	 * brute-force-search
	 */
	private void displaySearchScreen() {
		at.compositeStepStart();
		at.addLabel("searching screen");
		at.hide(new String[] { "sollSortiert", "comment1", "comment2", "comment3",
				"comment4" }, null);
		at.advancedTextLine("headline", new Pos(300, 70),
				"color black size 25 bold", null, ("Brute-Force-Suche")
						+ " nach der Zeichenkette: '" + stringToSearch + "'");
		at.advancedArrayReduction(stringArrayName, new Off(-200, 120, "headline",
				"NW"), dataString);
		// zuerst markerStart, damit dieser nachher von MarkerI überdeckt wird..
		at.addArrayMarker(markerStartName, stringArrayName, 0, "color red",
				new Hidden());
		at.addArrayMarker(markerIName, stringArrayName, 0, "color blue",
				new Hidden());
		at.advancedTextLine(legendeIName, new Off(0, -25, stringArrayName, "NW"),
				"color blue size 15", null,
				"(textPos) aktuell betrachtete Position im String");
		at.advancedArrayReduction(subStringArrayName, new Off(0, 50,
				stringArrayName, "SW"), stringToSearch);
		at.addArrayMarker(markerJName, subStringArrayName, 0, "color green3",
				new Hidden());
		at.advancedTextLine(legendeJName, new Off(30, 0, legendeIName, "NE"),
				"color green3 size 15", null,
				"(xPos) aktuell betrachtete Position in der Zeichenkette");
		at.advancedTextLine(legendeStartName, new Off(30, 0, legendeJName, "NE"),
				"color red size 15", null, "Startposition des aktuellen Vergleichs");

		at
				.hide(new String[] { legendeIName, legendeJName, legendeStartName },
						null);

		at
				.advancedCodeGroupStandard(
						algorithmName,
						new Off(15, 75, subStringArrayName, "SW"),
						new TimeOffset(0),
						new String[] {
								"1. Betrachte jeweils das erste Zeichen in Text und zu suchender Zeichenkette",
								"2. Ist entweder das Ende des Textes oder das der Zeichenkette erreicht?",
								"   - falls ja, gehe zu Schritt 9",
								"3. Sind die beiden betrachteten Zeichen gleich?",
								"   - falls ja, gehe zu Schritt 7",
								"4. Betrachte im Text das Zeichen direkt hinter der letzten 'Startposition' des Vergleichs",
								"5. Betrachte in der zu suchenden Zeichenkette wieder das erste Zeichen",
								"6. gehe zu Schritt 2 (Startposition des Vergleichs wird angepasst)",
								"7. gehe in Text und Zeichenkette jeweils ein Zeichen weiter",
								"8. gehe zu Schritt 2",
								"9. Ist das Ende der Zeichenkette erreicht?",
								"   - falls ja, gib die letzte 'Startposition' des Vergleichs zurück",
								"   - sonst wurde das Element nicht gefunden" });
		at.compositeStepEnd();
	}

	/**
	 * implements the BruteForce-Search - Algorithm and causes the animation on
	 * the SearchScreen while Search is in progress
	 * 
	 * @param text
	 *          the String, in which x is to be found
	 * @param x
	 *          the subString, that is to be searched in text
	 * @return index in array a, where x has been located, (-1), if x could not be
	 *         located
	 */
	private int bFSSearchIterativ(String text, String x) {
		setTextPos(0, true);
		setXPos(0, true);
		// Ausfuehren bis Textende (textPos=text.length()) oder Wort fertig
		// (xPos=x.length())
		while (xPos < x.length() && textPos < text.length()) {
			codeMarkingForWhile();
			while (text.charAt(textPos) != x.charAt(xPos)) { // Fehler an Pos.
																												// textPos, Wortpos.
																												// xPos
				setTextPos(textPos - xPos + 1, false); // zum zweiten Zeichen zurueck
				setXPos(0, false); // und fange neu an mit x
				if (textPos == text.length())
					break; // algorithmus terminiert sonst nicht
				// bei erfolgloser suche
				codeMarkingForWhile();
			}
			if (xPos < x.length() && textPos < text.length()) {
				setTextPos(textPos + 1, true);
				setXPos(xPos + 1, true);
			}
		}
		if (xPos == x.length()) {
			return returnResult(textPos - x.length()); // Erfolg; Wort beginnt bei
																									// textPos-x.length()
		}
		return returnResult(-1); // nicht gefunden
	}

	/**
	 * changes the value of the variant "TextPos", used in the
	 * BruteForceSearch-Algorithm and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "TextPos"
	 * @param isFor
	 *          determines, wether the marking has to be done for the for-loop or
	 *          for the while-loop
	 */
	private void setTextPos(int newValue, boolean isFor) {
		at.compositeStepStart();
		if (textPos == -1) {
			at.show(legendeIName, null);
			at.moveArrayMarker(markerIName, newValue, new WithinTiming(0));
			at.show(markerIName, null);
			markLines(new int[] { 0 });
		} else {
			if (newValue < dataString.length()) {
				at.moveArrayMarker(markerIName, newValue, new WithinTiming(1000));
			} else {
				at.hide(markerIName, null);
			}
			if (isFor) {
				markLines(new int[] { 8 });
			} else {
				markLines(new int[] { 5 });
			}
		}
		textPos = newValue;
		at.compositeStepEnd();
	}

	/**
	 * changes the value of the variant "xPos", used in the
	 * BruteForceSearch-Algorithm and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "xPos"
	 * @param isFor
	 *          determines, wether the marking has to be done for the for-loop or
	 *          for the while-loop
	 */
	private void setXPos(int newValue, boolean isFor) {
		at.compositeStepStart();
		if (xPos == -1) {
			at.show(legendeJName, null);
			at.moveArrayMarker(markerJName, newValue, new WithinTiming(0));
			at.show(markerJName, null);
			at.show(legendeStartName, null);
			at.show(markerStartName, null);
			if (newValue == 0) {
				at.moveArrayMarker(markerStartName, textPos, new WithinTiming(0));
			}
		} else {
			if (newValue < stringToSearch.length()) {
				at.moveArrayMarker(markerJName, newValue, new WithinTiming(1000));
			} else {
				at.hide(markerJName, null);
			}
			if (isFor) {
				at.compositeStepEnd();
				at.compositeStepStart();
				markLines(new int[] { 9 });
			} else {
				markLines(new int[] { 6 });
				at.compositeStepEnd();
				at.compositeStepStart();
				markLines(new int[] { 7 });
				if (newValue == 0) {
					if (textPos < dataString.length()) {
						at
								.moveArrayMarker(markerStartName, textPos, new WithinTiming(
										1000));
					} else {
						at.hide(markerStartName, null);
					}
				}
			}
		}
		at.compositeStepEnd();
		xPos = newValue;
	}

	/**
	 * marks the code-lines and the lines of the algorithm, that are in use at the
	 * beginning of the while-loop in the Search-algorithm
	 */
	private void codeMarkingForWhile() {
		if (textPos < dataString.length()) {
			markLines(new int[] { 1 });
			if (xPos < stringToSearch.length() && textPos < dataString.length()) {
				markLines(new int[] { 3 });
				if (dataString.charAt(textPos) == stringToSearch.charAt(xPos)) {
					markLines(new int[] { 3, 4 });
				}
			}
		}
	}

	/**
	 * creates the animation that is to take place, when the final result of the
	 * Search is returned
	 * 
	 * @param result
	 *          the result of the search (index of searched element/-1, if not
	 *          found)
	 * @return same as parameter result - unchanged
	 */
	private int returnResult(int result) {
		at.compositeStepStart();
		markLines(new int[] { 1, 2 });
		at.compositeStepEnd();
		at.compositeStepStart();
		markLines(new int[] { 10 });
		if (result == -1) {
			markLines(new int[] { 10, 12 });
			at.advancedTextLine("Ergebnis", new Off(130, 100, algorithmName, "SW"),
					"color blue size 30", new TimeOffset(0),
					"Die gesuchte Zeichenfolge wurde nicht gefunden!");
		} else {
			markLines(new int[] { 10, 11 });
			at.advancedTextLine("Ergebnis", new Off(130, 50, algorithmName, "SW"),
					"color blue size 30", new TimeOffset(0),
					"Die gesuchte Zeichenfolge wurde bei index " + result + " gefunden!");
			at.advancedTextLine("Ergebnis2", new Off(200, 15, "Ergebnis", "SW"),
					"color blue size 15", new TimeOffset(0),
					"(der index beginnt bei 0 und geht bis text.length() - 1)");
		}
		at.compositeStepEnd();
		at.addLabel("SearchResult");
		return result;
	}

	/**
	 * marks lines the algorithm (by highlightning them), and unhighlightens the
	 * lines, that were highlightened before
	 * 
	 * @param linesInAlgorithm
	 *          int-array, that contains the lines to be marked in the algorithm
	 */
	private void markLines(int[] linesInAlgorithm) {
		if (linesInAlgorithm == null) {
			System.out
					.println("useless call of markLines in BruteForceSearch - all parameters null");
//		} else if (linesInAlgorithm == null) {
//			// nothing!
		} else {
			at.compositeStepStart();
			if (markedLinesInAlgorithm == null) {
				markedLinesInAlgorithm = new int[0];
			}
			if (linesInAlgorithm != null) {
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
			at.compositeStepEnd();
		}
	}

}
