package algorithm.stringSearch;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algorithm.animalTranslator.AnimalTranslator;
import algorithm.animalTranslator.codeItems.Hidden;
import algorithm.animalTranslator.codeItems.Off;
import algorithm.animalTranslator.codeItems.Pos;
import algorithm.animalTranslator.codeItems.TimeOffset;
import algorithm.animalTranslator.codeItems.WithinTiming;

/**
 * this class implements the algorithm to create AnimalScript-code for a
 * BMSearch in Strings
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class BMStringSearch {

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

	// private String nextArrayName;

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

	// private String markerNextName;

	/**
	 * names to be used in the animation's code for the legend-item
	 */
	private String legendeIName;

	private String legendeJName;

	private String legendeStartName;

	// private String legendeNextName;
	// private String legendeNextMinusEinsName;

	/**
	 * ints that save the value of the variants used in the SringSearch-algorithm
	 */
	private int textPos, xPos, crossCounter;

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
	public BMStringSearch(AnimalTranslator newAT) {
		at = newAT;
	}

	/**
	 * array of flags, needed to darken the cells in the string-array in order to
	 * not run out of performance
	 */
	private boolean[] isMarked;

	/**
	 * saves the delta-tab (the values for the right-shift, used in the search) as
	 * string-output
	 */
	private int[] ourDeltaTab;

	/**
	 * initializes the instance of BMStringSearch by saving the String and the
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

		stringArrayName = "BMSearchArray";
		subStringArrayName = "BMSearchArraySubString";
		// nextArrayName = "BMSearch_NextArray";
		algorithmName = "CodeGroup_Algorithm";
		markerIName = "markerInString";
		markerJName = "markerInSubString";
		markerStartName = "markerInStringStart";
		// markerNextName = "ERRORFIXmarkerOnNextArray";
		legendeIName = "dieLegendeStringPointer";
		legendeJName = "dieLegendeSubStringPointer";
		legendeStartName = "dieLegendeStringCompareStart";
		// legendeNextName = "dieLegendeNextArray";
		// legendeNextMinusEinsName ="dieMinusEinsAmNextArray";
		textPos = -1;
		xPos = -1;
		isMarked = new boolean[dataString.length()];
		for (int i = 0; i < dataString.length(); i++)
			isMarked[i] = false;
		crossCounter = 0;

		initialized = true;
	}
	
	// GR
	Language lang;
	public void init() {
	  lang = new AnimalScript("Boyer-Moore String Search", "Michael Maur", 800, 640);
	  lang.setStepMode(true);
	}

	/**
	 * causes generation of the animation for the BM string-search
	 * 
	 * @throws Exception
	 *           in case initialization of the instance has been forgotten
	 */
	public int generateAnimation() throws Exception {
		if (!initialized) {
			throw new Exception(
					"Instance of BM-StringSearch has not been initialized!");
		} 
		at.advancedAddHeaderMM("BM String-Suche");
		displayInitialScreen();
		displaySearchScreen();
		return bMSearchIterativ(dataString, stringToSearch);
	}

	/**
	 * displays the initial screen
	 */
	private void displayInitialScreen() {
		at.compositeStepStart();
		lang.nextStep("Initial Screen");
		at.addLabel("initial screen");
		at.advancedCreateWorkSheet();
//		Text title = lang.newText(new Coordinates(200, 200), 
//		    "Boyer-Moore - Suche nach einer Zeichenkette", 800, 640);
		at.addText("sollSortiert", "Boyer-Moore - Suche nach einer Zeichenkette",
				new Pos(200, 200), "color black size 35 bold", null);
		at.compositeStepEnd();

		at.compositeStepStart();
		at
				.addText(
						"comment1",
						"Idee beim Boyer-Moore-Algorithmus zur Suche von Zeichenketten in Texten:",
						new Off(-100, 130, "sollSortiert", "SW"), "color black size 25",
						null);
		at
				.addText(
						"comment2",
						"- Vergleiche die Buchstaben innerhalb eines betrachteten Bereichs von der",
						new Off(0, 40, "comment1", "SW"), "color black size 25", null);
		at.addText("comment3", "   Länge der Zeichenkette von rechts nach links.",
				new Off(0, 40, "comment2", "SW"), "color black size 25", null);
		at
				.addText(
						"comment4",
						"- bei Ungleichheit schiebe den betrachteten Bereich möglichst weit nach rechts",
						new Off(0, 40, "comment3", "SW"), "color black size 25", null);
		at
				.addText(
						"comment5",
						"Als Anhaltspunkt dient hier das letzte Zeichen im betrachteten Bereich im Text.",
						new Off(0, 40, "comment4", "SW"), "color black size 25", null);
		at
				.addText(
						"comment6",
						"Kommt es in der Zeichenkette vor, so gibt der Abstand vom letzten Vorkommen",
						new Off(0, 40, "comment5", "SW"), "color black size 25", null);
		at
				.addText(
						"comment7",
						"des Zeichens in der Zeichenkette zu deren letztem Zeichen an, wie weit der ",
						new Off(0, 40, "comment6", "SW"), "color black size 25", null);
		at
				.addText(
						"comment8",
						"betrachtete Bereich nach rechts zu schieben ist. Dieser Wert wird zu Beginn ",
						new Off(0, 40, "comment7", "SW"), "color black size 25", null);
		at.addText("comment9",
				"der Suche für jedes Zeichen in der Zeichenkette berechnet.", new Off(
						0, 40, "comment8", "SW"), "color black size 25", null);
		at.compositeStepEnd();
	}

	/**
	 * displays the screen, that shows the String, the algorithm for the BM-search
	 */
	private void displaySearchScreen() {
		at.compositeStepStart();
		at.addLabel("searching screen");
		at.hide(
				new String[] { "sollSortiert", "comment1", "comment2", "comment3",
						"comment4", "comment5", "comment6", "comment7", "comment8",
						"comment9" }, null);
		at.advancedTextLine("headline", new Pos(300, 70),
				"color black size 25 bold", null, ("Boyer-Moore - Suche")
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
		at.addArrayMarker(markerJName, subStringArrayName, 0, "color blue",
				new Hidden());
		at.advancedTextLine(legendeIName, new Off(0, -25, stringArrayName, "NW"),
				"color blue size 15", null,
				"(textPos) aktuell betrachtete Position in Text und Zeichenkette");
		at.advancedTextLine(legendeStartName, new Off(30, 0, legendeIName, "NE"),
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
								"1. Berechne für jedes Zeichen, wieviel Schritte im Text nach rechts gegangen werden kann,",
								"   wenn das Zeichen am rechten Rand des betrachteten Feldes gefunden wird.",
								"2. Betrachte das letzte Zeichen in der Zeichenkette und den gleichen Index im Text.",
								"   Insgesamt wird im Text ein Bereich verglichen, der der Länge der Zeichenkette entspricht.",
								"3. Ist entweder das Ende des Textes oder der Anfang der Zeichenkette erreicht?",
								"   - falls ja, gehe zu Schritt 7",
								"4. Sind die beiden betrachteten Zeichen gleich?",
								"   - falls ja, betrachte in Text und Zeichenkette jeweils das Zeichen davor und gehe zu Schritt 3",
								"5. Verschiebe den betrachteten Bereich entsprechend seinem letzten Zeichen nach rechts (Tabelle aus Schritt 1).",
								"6. Betrachte wieder das letzte Zeichen in der Zeichenkette und gehe zu Schritt 3.",
								"7. Wurde die Zeichenkette vollständig überprüft?",
								"   - falls ja, wurde die Zeichenkette bei Index 'Startposition - Länge der Zeichenkette + 1' gefunden",
								"   - sonst wurde das Element nicht gefunden" });
		at.compositeStepEnd();
	}

	/**
	 * implements the BM-Search-Algorithm and causes the animation on the
	 * SortingScreen while Search is in progress
	 * 
	 * @param text
	 *          the String, in which x is to be searched
	 * @param x
	 *          the subString, to be looked for in text
	 * @return position in text, where x has been located, (-1), if x could not be
	 *         located
	 */
	private int bMSearchIterativ(String text, String x) {
		int[] step = baueDeltaTab(x);// berechne verschiebungstabelle step
		displayIncludedDeltaTab(x);
		at.compositeStepStart();
		setXPos(x.length() - 1); // betrachte das letzte Zeichen in der
															// Zeichenkette
		setTextPos(0); // betrachtete Position im Text entspricht der in der
										// Zeichenkette (da nachher +xPos)
		at.compositeStepEnd();
		while (textPos <= text.length() - x.length()) {
			// ist das Ende der Zeichenkette erreicht - falls zu Ergebnis not Found
			// springen
			codeMarkingForWhile1();
			while (x.charAt(xPos) == text.charAt(textPos + xPos)) {
				// sind betrachtete Zeichen gleich?
				setXPos(xPos - 1); // falls ja, gehe je ein Zeichen nach vorne
				if (xPos < 0)
					// Zeichenkette vollstaendig ueberprueft?
					return returnResult(textPos);// falls ja, Ergebnis zurueckgeben

			}
			// gehe die der Tabelle zu entnehmende Anzahl Stellen weiter
			setTextPos(textPos + step[dataString.charAt(textPos + x.length() - 1)]);
			setXPos(x.length() - 1); // betrachte das letzte Zeichen in der
																// Zeichenkette
		}
		return returnResult(-1);// Ergebnis zurueckgeben - not found
	}

	/**
	 * changes the value of the variant "TextPos", used in the BM-Search-Algorithm
	 * and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "TextPos"
	 */
	private void setTextPos(int newValue) {
		at.compositeStepStart();
		if (textPos == -1) {
			markLines(new int[] { 2, 3 });
			at.show(new String[] { markerIName, markerJName, markerStartName,
					legendeIName, legendeStartName }, null);
		} else {
			if ((stringToSearch.charAt(xPos) != dataString.charAt(textPos + xPos))
					&& (xPos != stringToSearch.length() - 1)) {
				markLines(new int[] { 6 });
				at.compositeStepEnd();
				at.compositeStepStart();
			}
			markDelta(dataString.charAt(textPos + stringToSearch.length() - 1));
			markLines(new int[] { 8 });
			at.compositeStepEnd();
			at.compositeStepStart();
		}
		if (newValue < dataString.length()) {
			at.moveArrayMarker(markerIName, newValue + stringToSearch.length() - 1,
					new WithinTiming(1000));
			at.moveArrayMarker(markerStartName, newValue + stringToSearch.length()
					- 1, new WithinTiming(1000));
		}
		markArrayArea(newValue);
		textPos = newValue;
		at.compositeStepEnd();
	}

	/**
	 * changes the value of the variant "xPos", used in the BM-Search-Algorithm
	 * and causes the connected animation
	 * 
	 * @param newValue
	 *          the new Value of "xPos"
	 * @param isFor
	 *          determines, wether the marking has to be done for the for-loop or
	 *          for the while-loop
	 */
	private void setXPos(int newValue) {
		at.compositeStepStart();
		if (xPos != -1) {
			if (newValue == stringToSearch.length() - 1) {
				markLines(new int[] { 9 });
				at.moveArrayMarker(markerIName, textPos + newValue, new WithinTiming(
						1000));
				at.moveArrayMarker(markerJName, newValue, new WithinTiming(1000));
			} else {
				markLines(new int[] { 6 });
				at.compositeStepEnd();
				at.compositeStepStart();
				markLines(new int[] { 6, 7 });
				if (newValue >= 0) {
					at.moveArrayMarker(markerIName, textPos + newValue, new WithinTiming(
							1000));
					at.moveArrayMarker(markerJName, newValue, new WithinTiming(1000));
				} else {
					at.hide(new String[] { markerIName, markerJName }, null);
				}
				if (newValue >= 0) {
					at.compositeStepEnd();
					at.compositeStepStart();
					markLines(new int[] { 4 });
				}
			}
		} else {
			if (newValue >= 0) {
				at.moveArrayMarker(markerIName, textPos + newValue, new WithinTiming(
						1000));
				at.moveArrayMarker(markerJName, newValue, new WithinTiming(1000));
			} else {
				at.hide(new String[] { markerIName, markerJName }, null);
			}
		}
		at.compositeStepEnd();
		xPos = newValue;
	}

	/**
	 * marks the lines of the algorithm, that are in use at the beginning of the
	 * while-loop in the Search-algorithm
	 */
	private void codeMarkingForWhile1() {
		markLines(new int[] { 4 });
		if (stringToSearch.charAt(xPos) != dataString.charAt(textPos + xPos))
			markLines(new int[] { 6 });
	}

	/**
	 * returns an array with the distances, the string Muster has to be moved to
	 * the right depending on the very right letter in its former position
	 * 
	 * @param Muster
	 *          the string to look for
	 * @return an int-array with the distances for the rightshift of Muster during
	 *         search
	 */
	private int[] baueDeltaTab(String Muster) {
		int ls = Muster.length();
		int[] deltaTab = new int[256];
		for (int i = 0; i < 256; i++)
			deltaTab[i] = ls;
		for (int i = 1; i < ls; i++)
			deltaTab[Muster.charAt(i - 1)] = ls - i;
		return deltaTab;
	}

	/**
	 * returns an array with the distances, the string Muster has to be moved to
	 * the right depending on the very right letter in its former position - here,
	 * only the letters used in Muster are included -
	 * 
	 * @param Muster
	 *          the string to look for
	 * @return an string-array with the letters and the distances for the
	 *         rightshift of Muster during search!?
	 */
	private String[] includedDeltaTab(String Muster) {
		int ls = Muster.length();
		ourDeltaTab = baueDeltaTab(Muster);
		String[] newDeltaTab = new String[] { "Buchstabe:    ", "Rechtsshift:  " };
		for (int i = 0; i < 256; i++) {
			int dti = ourDeltaTab[i];
			if (dti != ls) {
				newDeltaTab[0] = newDeltaTab[0] + ((dti >= 10) ? " " : "") + (char) i
						+ " ";
				newDeltaTab[1] = newDeltaTab[1] + ourDeltaTab[i] + " ";
			}
		}
		newDeltaTab[0] = newDeltaTab[0] + "  sonstige";
		newDeltaTab[1] = newDeltaTab[1] + "  " + ls + "";
		return newDeltaTab;
	}

	/**
	 * marks an entry of the delta-table in the animation by a red 'x'
	 * 
	 * @param toMark
	 *          the letter to be marked
	 */
	private void markDelta(char toMark) {
		at.compositeStepStart();
		// altes x ausblenden
		if (crossCounter > 0)
			at.hide("Delta_Marker_X_" + crossCounter + "", null);
		// position fuer x bestimmen
		int position = 14;
		boolean cancelled = false;
		for (int i = 0; i < 256; i++) {
			int odti = ourDeltaTab[i];
			if (odti < stringToSearch.length()) {
				if (toMark == i) {
					cancelled = true;
					break;
				}
				position += 2;
				if (odti >= 10)
					position++;
			}
		}
		if (!cancelled)
			position += 2; // zusaetzliches leerzeichen vor sonstige
		// neues x erzeugen
		String txt = "";
		for (int i = 0; i < position; i++)
			txt = txt + " ";
		txt = txt + "x";
		crossCounter++;
		at.advancedTextLine("Delta_Marker_X_" + crossCounter + "", new Off(0, -6,
				"DeltaTabLineOne", "SW"), "color red font Monospaced size 16 bold",
				null, txt);
		at.compositeStepEnd();
	}

	/**
	 * darkens the area in the text, that is currently not compared
	 * 
	 * @param start
	 *          start-index of the are, that is currently compared
	 */
	private void markArrayArea(int start) {
		at.compositeStepStart();
		int ende = start + stringToSearch.length() - 1;
		for (int i = 0; i < dataString.length(); i++) {
			if (i < start || i > ende) {
				// have to be darkened
				if (!isMarked[i]) {
					at.advancedHighlightArrayCells(stringArrayName, i, i);
					isMarked[i] = true;
				}
			} else {
				// have to be lightened
				if (isMarked[i])
					at.advancedUnhighlightArrayCells(stringArrayName, i, i);
				isMarked[i] = false;
			}
		}
		at.compositeStepEnd();
	}

	/**
	 * displays the deltatab in the animation
	 * 
	 * @param Muster
	 *          the string to look for in the text
	 */
	private void displayIncludedDeltaTab(String Muster) {
		at.compositeStepStart();
		markLines(new int[] { 0, 1 });
		String[] deltaTab = includedDeltaTab(Muster);
		at.advancedTextLine("DeltaTabLineOne", new Off(0, 25, subStringArrayName,
				"SW"), "font Monospaced", null, deltaTab[0]);
		at.advancedTextLine("DeltaTabLineTwo", new Off(0, 8, "DeltaTabLineOne",
				"SW"), "font Monospaced", null, deltaTab[1]);
		at.compositeStepEnd();
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
		markLines(new int[] { 4 });
		markLines(new int[] { 4, 5 });
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
	 * marks lines in the algorithm (by highlightning them), and unhighlightens
	 * the lines, that were highlightened before
	 * 
	 * @param linesInAlgorithm
	 *          int-array, that contains the lines to be marked in the algorithm
	 */
	private void markLines(int[] linesInAlgorithm) {
		if (linesInAlgorithm == null) {
			System.out
					.println("useless call of markLines in BMSearch - all parameters null");
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
