/*
 * Created on 25.01.2005
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
 * KMPSearch in Strings
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class KMPStringSearch {

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
	 * flag to determine, wether params dataString and stringToSearch have been
	 * set
	 */
	private boolean initialized = false;

	/**
	 * name to be used in the animation's code for the arrays
	 */
	private String stringArrayName;

	private String subStringArrayName;

	private String nextArrayName;

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

	private String markerNextName;

	/**
	 * names to be used in the animation's code for the legend-item
	 */
	private String legendeIName;

	private String legendeJName;

	private String legendeStartName;

	private String legendeNextName;

	private String legendeNextMinusEinsName;

	/**
	 * ints that save the value of the variants used in the SringSearch-algorithm
	 */
	private int textPos, xPos, curStartPosition, previous_next_value;

	/**
	 * int-array that contains the index of the marked lines in the algorithm
	 */
	private int[] markedLinesInAlgorithm;

	/**
	 * constructor that saves a pointer to the animalTranslator to be used
	 * 
	 * @param newAT
	 *          AnimalTranslator to be used
	 */
	public KMPStringSearch(AnimalTranslator newAT) {
		at = newAT;
	}

	/**
	 * initializes the instance of KMPStringSearch by saving the String and the
	 * SubString to be searched in that String
	 * 
	 * @param theString
	 *          the string in which toBeSearched shall be found
	 * @param toBeSearched
	 *          int - element to be searched in theString
	 */
	public void initialize(String theString, String toBeSearched) {
		dataString = theString;
		stringToSearch = toBeSearched;

		stringArrayName = "KMPSearchArray";
		subStringArrayName = "KMPSearchArraySubString";
		nextArrayName = "KMPSearch_NextArray";
		algorithmName = "CodeGroup_Algorithm";
		markerIName = "markerInString";
		markerJName = "markerInSubString";
		markerStartName = "markerInStringStart";
		markerNextName = "markerOnNextArray";
		legendeIName = "dieLegendeStringPointer";
		legendeJName = "dieLegendeSubStringPointer";
		legendeStartName = "dieLegendeStringCompareStart";
		legendeNextName = "dieLegendeNextArray";
		legendeNextMinusEinsName = "dieMinusEinsAmNextArray";
		textPos = -100;
		xPos = -100;

		initialized = true;
	}

	/**
	 * causes generation of the animation for the KMP string-search
	 * 
	 * @throws Exception
	 *           in case initialization of the instance has been forgotten
	 */
	public int generateAnimation() throws Exception {
		if (!initialized) {
			throw new Exception(
					"Instance of KMP-StringSearch has not been initialized!");
		} 
		at.advancedAddHeaderMM("KMP String-Suche");
		displayInitialScreen();
		displaySearchScreen();
		return kMPSearchIterativ(dataString, stringToSearch);
	}

	/**
	 * displays the initial screen
	 */
	private void displayInitialScreen() {
		at.compositeStepStart();
		at.addLabel("initial screen");
		at.advancedCreateWorkSheet();
		at.addText("sollSortiert", "KMP-Suche nach einer Zeichenkette", new Pos(
				200, 200), "color black size 35 bold", null);
		at.compositeStepEnd();

		at.compositeStepStart();
		at
				.addText(
						"comment1",
						"Es handelt sich hierbei um eine verbesserte Variante der BruteForce-Suche.",
						new Off(-420, 130, "sollSortiert", "S"), "color black size 25",
						null);
		at.addText("comment2",
				"Hierbei wird vor Beginn der Suche festgehalten, ab welcher Stelle",
				new Off(0, 40, "comment1", "SW"), "color black size 25", null);
		at.addText("comment3",
				"in der zu suchenden Zeichenkette der Vergleich, bei Ungleichheit",
				new Off(0, 40, "comment2", "SW"), "color black size 25", null);
		at.addText("comment4",
				"an einer beliebigen Stelle, fortgesetzt werden muss.", new Off(0, 40,
						"comment3", "SW"), "color black size 25", null);
		at
				.addText(
						"comment5",
						"Hierdurch muss im Text grundsätzlich nicht mehr zum 2. Zeichen des aktuellen",
						new Off(0, 40, "comment4", "SW"), "color black size 25", null);
		at
				.addText(
						"comment6",
						"Vergleichs zurückgesprungen werden, da anhand der bereits geprüften Zeichen",
						new Off(0, 40, "comment5", "SW"), "color black size 25", null);
		at
				.addText(
						"comment7",
						"entschieden werden kann, ab welcher Stelle in der zu suchenden Zeichenkette",
						new Off(0, 40, "comment6", "SW"), "color black size 25", null);
		at.addText("comment8", "der Vergleich fortzusetzen ist.", new Off(0, 40,
				"comment7", "SW"), "color black size 25", null);
		at.compositeStepEnd();
	}

	/**
	 * displays the screen, that shows the String, the algorithm for the
	 * KMP-search
	 */
	private void displaySearchScreen() {
		at.compositeStepStart();
		at.addLabel("searching screen");
		at.hide(new String[] { "sollSortiert", "comment1", "comment2", "comment3",
				"comment4", "comment5", "comment6", "comment7", "comment8" }, null);
		at.advancedTextLine("headline", new Pos(300, 70),
				"color black size 25 bold", null, ("KMP-Suche")
						+ " nach der Zeichenkette: '" + stringToSearch + "'");
		at.advancedArrayReduction(stringArrayName, new Off(-200, 120, "headline",
				"NW"), dataString);
		at.advancedArrayReduction(subStringArrayName, new Off(0, 50,
				stringArrayName, "SW"), stringToSearch);
		// zuerst markerStart, damit dieser nachher von MarkerI überdeckt wird..
		at.addArrayMarker(markerStartName, stringArrayName, 0, "color red",
				new Hidden());
		at.addArrayMarker(markerIName, stringArrayName, 0, "color blue",
				new Hidden());
		at.addArrayMarker(markerJName, subStringArrayName, 0, "color green3",
				new Hidden());
		at.advancedTextLine(legendeIName, new Off(0, -25, stringArrayName, "NW"),
				"color blue size 15", null,
				"(textPos) aktuell betrachtete Position im String");
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
						new Off(15, 100, subStringArrayName, "SW"),
						new TimeOffset(0),
						new String[] {
								"1. Berechne für Jedes Zeichen in der Zeichenkette den Rücksprungindex für Ungleichheit in der jew. Position",
								"2. Betrachte jeweils das erste Zeichen in Text und zu suchender Zeichenkette",
								"3. Ist entweder das Ende des Textes oder das der Zeichenkette erreicht?",
								"   - falls ja, gehe zu Schritt 9",
								"4. Sind die beiden betrachteten Zeichen gleich oder ist die aktuelle Position in der Zeichenkette kleiner Null?",
								"   - falls ja, gehe zu Schritt 7",
								"5. Betrachte in der zu suchenden Zeichenkette das Zeichen entsprechend dem Rücksprungindex",
								"6. gehe zu Schritt 3 (Startposition des Vergleichs wird angepasst)",
								"7. gehe in Text und Zeichenkette jeweils ein Zeichen weiter",
								"8. gehe zu Schritt 3 (ggf. Anpassen der Startposition des Vergleichs)",
								"9. Ist das Ende der Zeichenkette erreicht?",
								"   - falls ja, gib die letzte 'Startposition' des Vergleichs zurück",
								"   - sonst wurde das Element nicht gefunden" });
		at.compositeStepEnd();
	}

	/**
	 * implements the KPM-Search-Algorithm and causes the animation on the
	 * SortingScreen while Search is in progress
	 * 
	 * @param text
	 *          the String, in which x is to be searched
	 * @param x
	 *          the subString, to be looked for in text
	 * @return position in text, where x has been located, (-1), if x could not be
	 *         located
	 */
	private int kMPSearchIterativ(String text, String x) {
		int[] next = printNext(initNext(x)); // Init "next"-Daten & Anzeigen des
																					// Arrays
		at.compositeStepStart();
		setTextPos(0, true);
		setXPos(0, true);
		at.compositeStepEnd();
		// Ausfuehren bis Textende (textPos=text.length()) oder Wort fertig
		// (xPos=x.length())
		while (xPos < x.length() && textPos < text.length()) {
			codeMarkingForWhile();
			while (xPos >= 0 && text.charAt(textPos) != x.charAt(xPos)) { // Fehler an
																																		// Pos.
																																		// textPos,
																																		// Wortpos.
																																		// xPos
				setXPos(next[xPos], false); // und fange neu an mit x
				// if (textPos == text.length()) break; //algorithmus terminiert sonst
				// nicht
				// bei erfolgloser suche
				codeMarkingForWhile();
			}
			if (xPos < x.length() && textPos < text.length()) {
				at.compositeStepStart();
				setTextPos(textPos + 1, true);
				setXPos(xPos + 1, true);
				at.compositeStepEnd();
			}
		}
		if (xPos == x.length()) {
			return returnResult(textPos - x.length()); // Erfolg; Wort beginnt bei
																									// textPos-x.length()
		} 
		return returnResult(-1); // nicht gefunden
	}

	/**
	 * Initialisiere next durch Vergleich des Musters mit sich
	 */
	public int[] initNext(String p) {
		int i = 0, j = -1, m = p.length(); // Zaehler
		int[] next = new int[m]; // next-Array deklarieren
		next[0] = -1; // durch "j++" ausgeglichen

		// Bestimme next[i].
		// Falls Vergleich redundant waere, gehe direkt weiter
		for (; i < m - 1; i++, j++, next[i] = (p.charAt(i) == p.charAt(j)) ? next[j]
				: j)
			while ((j >= 0) && (p.charAt(i) != p.charAt(j)))
				// falls Fehler,
				j = next[j]; // setze j zurueck
		return next; // Array zurueckgeben
	}

	/**
	 * changes the value of the variant "TextPos", used in the
	 * KMP-Search-Algorithm and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "TextPos"
	 * @param isFor
	 *          determines, wether the marking has to be done for the for-loop or
	 *          for the while-loop
	 */
	private void setTextPos(int newValue, boolean isFor) {
		at.compositeStepStart();
		if (textPos == -100) {
			at.show(legendeIName, null);
			at.moveArrayMarker(markerIName, newValue, new WithinTiming(0));
			at.show(markerIName, null);
			markLines(new int[] { 1 });
		} else {
			if (newValue < dataString.length()) {
				at.moveArrayMarker(markerIName, newValue, new WithinTiming(1000));
			} else {
				at.hide(markerIName, null);
			}
			if (isFor) {
				markLines(new int[] { 8 });
			}
		}
		textPos = newValue;
		at.compositeStepEnd();
	}

	/**
	 * changes the value of the variant "xPos", used in the KMP-Search-Algorithm
	 * and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "xPos"
	 * @param isFor
	 *          determines, wether the marking has to be done for the for-loop or
	 *          for the while-loop
	 */
	private void setXPos(int newValue, boolean isFor) {
		at.compositeStepStart();
		if (previous_next_value >= 0) {
			at.unhighlightArrayCell(nextArrayName, previous_next_value, "", null);
		}
		if (xPos != -1 && newValue < xPos) {
			at.highlightArrayCell(nextArrayName, xPos, "", null);
		}
		if (newValue != -1) {
			if (xPos == -100) {
				at.show(legendeJName, new TimeOffset(1500));
				at.moveArrayMarker(markerJName, newValue, new WithinTiming(0));
				at.moveArrayMarker(markerStartName, 0, new WithinTiming(0));
				at.show(markerJName, new TimeOffset(1500));
				at.show(legendeStartName, new TimeOffset(1500));
				at.show(markerStartName, new TimeOffset(1500));
				at.show(markerNextName, new TimeOffset(1500));
				previous_next_value = 0;
			} else {
				at.show(markerNextName, null);
				at.hide(legendeNextMinusEinsName, null);
				if (newValue < stringToSearch.length()) {
					at.moveArrayMarker(markerJName, newValue, new WithinTiming(1000));
					at.moveArrayMarker(markerNextName, newValue, new WithinTiming(1000));
				} else {
					at.hide(new String[] { markerJName, markerNextName }, new TimeOffset(
							0));
				}
				if (isFor) {
					at.compositeStepEnd();
					at.compositeStepEnd();
					at.compositeStepStart();
					at.compositeStepStart();
					markLines(new int[] { 9 });
					if (newValue == 0) {
						if (textPos < dataString.length())
							at.moveArrayMarker(markerStartName, textPos, new WithinTiming(
									1000));
						else
							at.hide(markerStartName, null);
					}

				} else {
					markLines(new int[] { 6 });
					at.compositeStepEnd();
					at.compositeStepStart();
					markLines(new int[] { 7 });
					if (curStartPosition != textPos - newValue) {
						if (textPos < dataString.length()) {
							at.moveArrayMarker(markerStartName, textPos - newValue,
									new WithinTiming(1000));
						} else {
							at.hide(markerStartName, null);
						}
					}
				}
			}
		} else {
			// if newValue == -1
			markLines(new int[] { 6 });
			at.compositeStepEnd();
			at.compositeStepStart();
			markLines(new int[] { 7 });

			at.hide(markerNextName, null);
			at.show(legendeNextMinusEinsName, null);
		}
		at.compositeStepEnd();
		previous_next_value = xPos;
		xPos = newValue;
	}

	/**
	 * marks the lines of the algorithm, that are in use at the beginning of the
	 * while-loop in the Search-algorithm
	 */
	private void codeMarkingForWhile() {
		if (textPos < dataString.length()) {
			markLines(new int[] { 2 });
			if (xPos < stringToSearch.length() && textPos < dataString.length()) {
				markLines(new int[] { 4 });
				if (xPos == -1
						|| dataString.charAt(textPos) == stringToSearch.charAt(xPos)) {
					markLines(new int[] { 4, 5 });
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
		markLines(new int[] { 2, 3 });
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

	private int[] printNext(int[] next) {
		at.compositeStepStart();
		markLines(new int[] { 0 });
		at.advancedArrayStandard(nextArrayName, new Off(0, 50, subStringArrayName,
				"SW"), next);
		at.advancedTextLine(legendeNextName, new Off(15, 0, nextArrayName, "NE"),
				"color black size 15", null,
				"Rücksprungindex bei Ungleichheit in aktueller Position");
		at.advancedTextLine(legendeNextMinusEinsName, new Off(-25, -15,
				nextArrayName, "NW"), "color red size 20", null, "-1");
		at.hide(legendeNextMinusEinsName, null);
		at.addArrayMarker(markerNextName, nextArrayName, 0, "color green3", null);
		at.hide(markerNextName, null);
		at.compositeStepEnd();
		return next;
	}

	/**
	 * marks lines in the algorithm (by highlightning them), and unhighlightens
	 * the lines, that were highlightened before
	 * 
	 * @param linesInAlgorithm
	 *          int-array, that contains the lines to be marked in the algorithm
	 */
	private void markLines(int[] linesInAlgorithm) {
		if (linesInAlgorithm == null) {
			System.out
					.println("useless call of markLines in KMPSearch - all parameters null");
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
