package generators.backtracking.helpers;

import generators.graphics.helpers.AnimalUtilities;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

/**
 * The QueensPuzzle class covers both logic and animation for the queens puzzle.
 */
public class QueensPuzzle {

	public static final String QUEEN_SYMBOL = "♛";
	public static final String EMPTY_CELL_SYMBOL = "";

	// the number of solutions that have been found for all relevant values of
	// the queens puzzle, distinct and non distinct
	public static final int[] NUMBER_OF_DISTINCT_SOLUTIONS = { 0, 1, 0, 0, 1,
			2, 1, 6, 12, 46, 92, 341, 1787, 9233, 45752, 285053, 1846955,
			11977939, 83263591 };
	public static final int[] NUMBER_OF_OVERALL_SOLUTIONS = { 0, 1, 0, 0, 2,
			10, 4, 40, 92, 352, 724, 2680, 14200, 73712, 365596, 2279184,
			14772512, 95815104, 666090624 };

	// variables for visual representation
	private Color queenColor;
	private Color gridColor1;
	private Color gridColor2;
	private Color threatenedQueenColor;
	private Color threateningQueenColor;
	private Color pseudoCodeHighlightColor;
	private Color pseudoCodeBackgroundColor;
	private Color pseudoCodeTextColor;
	private Color headTextColor;

	// chess board size and amount of queens which are to be placed
	protected int n;

	// the queens that are positioned on the chess board
	protected Queen[] queens;

	// the AnimalScript language of this animation
	private Language language;

	// the chess board itself
	private StringMatrix qGrid;

	// the pseudocode of the queens puzzle which is shown next to the chess
	// board
	private SourceCode pseudoCode;

	// the amount of times a queen had to be placed on a new field
	private int numberOfTimesQueensHaveBeenMoved = 0;

	// the display for the number of times a queen had to be placed on a new
	// field
	private Text stepCounterDisplay;

	/**
	 * Creates a QueensPuzzle for a desired chess board size and generates the
	 * corresponding script afterwards.
	 * 
	 * @param n
	 *            the size of the chess board
	 * @param lang the language instance
	 * @param queenColor the color for the queen
	 * @param gridColor1 the first grid color
	 * @param gridColor2 the second grid color
	 * @param threatenedQueenColor the color for a threatened queen position
	 * @param threateningQueenColor the color for a threatening queen
	 * @param pseudoCodeHighlightColor the highlight color for the code
	 * @param pseudoCodeBackgroundColor the background color for the code
	 * @param pseudoCodeTextColor the color for the pseudo code text
	 * @param headTextColor the color for the header
	 */
	public QueensPuzzle(int n, Language lang, Color queenColor,
			Color gridColor1, Color gridColor2, Color threatenedQueenColor,
			Color threateningQueenColor, Color pseudoCodeHighlightColor,
			Color pseudoCodeBackgroundColor, Color pseudoCodeTextColor,
			Color headTextColor) {
		if (n <= 0) {
			throw new IllegalArgumentException(
					"The chess board size must be a positive integer.");
		} else if (n > 16) {
			throw new IllegalArgumentException(
					"Due to performance issues only n smaller than 16 are allowed.");
		}

		// assign variables passed by generator
		this.n = n;
		this.queenColor = queenColor;
		this.gridColor1 = gridColor1;
		this.gridColor2 = gridColor2;
		this.threatenedQueenColor = threatenedQueenColor;
		this.threateningQueenColor = threateningQueenColor;
		this.pseudoCodeHighlightColor = pseudoCodeHighlightColor;
		this.pseudoCodeBackgroundColor = pseudoCodeBackgroundColor;
		this.pseudoCodeTextColor = pseudoCodeTextColor;
		this.headTextColor = headTextColor;

		// create the queen array
		queens = new Queen[n];

		this.language = lang;
		language.setStepMode(true);

		// create the script itself
		createInitialDescription();
		createAlgorithmExecution();
		createFinalDescription();
	}

	/**
	 * Backtracking solve method.
	 * 
	 * @param row
	 *            the row which is currently worked on
	 * @return whether a solution was found
	 */
	private boolean solveBacktrack(int row) {
		AnimalUtilities.easyHighlight(pseudoCode, 7);
		language.nextStep();
		AnimalUtilities.easyHighlight(pseudoCode, 9);
		language.nextStep();

		placeQueen(row);
		Queen queen = queens[row];

		while (isQueenInBounds(queen)) {
			AnimalUtilities.easyHighlight(pseudoCode, 14);
			language.nextStep();
			if (getThreateningQueensOf(queen).isEmpty()) {
				AnimalUtilities.easyHighlight(pseudoCode, 17);
				language.nextStep();
				// solution found if this was the last row, else continue with
				// the row below
				if (row == n - 1) {
					AnimalUtilities.easyHighlight(pseudoCode, 18);
					language.nextStep();
					return true;
				} else {
					AnimalUtilities.easyHighlight(pseudoCode, 19);

					language.nextStep();

					if (solveBacktrack(row + 1)) {
						AnimalUtilities.easyHighlight(pseudoCode, 20);
						language.nextStep();
						return true;
					}
				}
			}
			// move the current queen a column and try again
			moveQueen(row);
		}

		// remove this queen and try again a row above
		removeQueen(row);
		AnimalUtilities.easyHighlight(pseudoCode, 25);
		language.nextStep();
		return false;
	}

	/**
	 * Moves the queen in the given row. The queen is moved to the left or to
	 * the right depending on whether the row is odd or even.
	 * 
	 * @param row
	 *            the row in which the queen is supposed to be moved
	 */
	private void moveQueen(int row) {
		if (row < 0 || row >= n) {
			throw new IllegalArgumentException(
					"Rows can't be lower than zero or greater than n.");
		}

		// move the queen logically and visually
		int newColumn = queens[row].getColumn() + (row % 2 == 0 ? 1 : -1);

		// reason for this check: logical queens are deliberately moved out of
		// bounds in the solving process which would
		// naturally cause exceptions in the AlgoAnim API
		if (newColumn >= 0 && newColumn < n) {
			updateStepDisplay();
			qGrid.swap(row, queens[row].getColumn(), row, newColumn, null,
					new MsTiming(200));
			AnimalUtilities.easyHighlight(pseudoCode, 22);
			language.nextStep();
		}

		queens[row].setColumn(newColumn);
	}

	/**
	 * Places a queen in a previously empty row. The queen is placed on the left
	 * or on the right depending on whether the row is odd or even.
	 * 
	 * @param row
	 *            the row in which a queen is supposed to be placed
	 */
	private void placeQueen(int row) {
		if (row < 0 || row >= n) {
			throw new IllegalArgumentException(
					"Rows can't be lower than zero or greater than n.");
		}
		if (queens[row] != null) {
			throw new IllegalStateException(
					"There already exists a queen in this row.");
		}

		// the placement of a new Queen is also considered moving it
		updateStepDisplay();

		// place the queen logically and visually
		AnimalUtilities.easyHighlight(pseudoCode, 9);
		language.nextStep();
		int col;
		if (row % 2 == 0) {
			col = 0;
			AnimalUtilities.easyHighlight(pseudoCode, 10);
		} else {
			col = n - 1;
			AnimalUtilities.easyHighlight(pseudoCode, 12);
		}
		qGrid.put(row, col, QUEEN_SYMBOL, null, null);

		// generate a Label for the table of contents
		// more spaces for lower lines as indent
		String indent = "";
		for (int i = 0; i <= row; i++) {
			indent = indent.concat("     ");
		}

		language.nextStep(indent.concat("Dame platziert auf Zeile ").concat(
				Integer.toString(row + 1)));

		queens[row] = new Queen(row, col);
	}

	/**
	 * Removes the queen from the given row.
	 * 
	 * @param row
	 *            the row in which the queen is to be removed
	 */
	private void removeQueen(int row) {
		if (row < 0 || row >= n) {
			throw new IllegalArgumentException(
					"Rows can't be lower than zero or greater than n.");
		}

		// remove the queen logically and visually
		qGrid.put(row, row % 2 == 0 ? n - 1 : 0, EMPTY_CELL_SYMBOL, null, null);
		AnimalUtilities.easyHighlight(pseudoCode, 24);
		language.nextStep();

		queens[row] = null;
	}

	/**
	 * Returns whether a queen is out of bounds.
	 * 
	 * @param queen
	 *            the queen to test
	 * @return whether the queen is out of bounds
	 */
	private boolean isQueenInBounds(Queen queen) {
		return queen.getColumn() >= 0 && queen.getColumn() < n;
	}

	/**
	 * Returns a list of queens which threaten the given queen. Information on
	 * the current row is used to reduce the search space and thereby to keep
	 * execution times low.
	 * 
	 * @param queen
	 *            the queen to test
	 * @return the list of queens that threaten the given queen
	 */
	private ArrayList<Queen> getThreateningQueensOf(Queen queen) {
		ArrayList<Queen> threatening = new ArrayList<Queen>();

		// vertical, check only rows above the current row (there can't be any
		// queens below)
		for (int row = 0; row < queen.getRow(); row++) {
			if (row != queen.getRow()) {
				if (queens[row].getColumn() == queen.getColumn()) {
					threatening.add(queens[row]);
					// there can only be one queen in the same row (if at all)
					// by definition, therefore break if a threatening queen was
					// found
					break;
				}
			}
		}

		// check upwards diagonal threatening at the corner points of a growing
		// box (again, the search range is kept as small as possible - because
		// queens are placed from top to bottom, there is no need to check for
		// queens below)
		for (int boxSize = 1; boxSize < queen.getRow() + 1; boxSize++) {
			int row = queen.getRow();
			int column = queen.getColumn();

			if (row - boxSize >= 0 && row - boxSize < n) {
				if (column - boxSize >= 0 && column - boxSize < n) {
					// top left
					if (queens[row - boxSize].getColumn() == column - boxSize) {
						threatening.add(queens[row - boxSize]);
					}
				}
				if (column + boxSize >= 0 && column + boxSize < n) {
					// top right
					if (queens[row - boxSize].getColumn() == column + boxSize) {
						threatening.add(queens[row - boxSize]);
					}
				}
			}
		}

		// visualise threatening queens if existent
		if (!threatening.isEmpty()) {
			// mark threatening queens first
			for (Queen tQueen : threatening) {
				language.addLine(AnimalUtilities.setGridColor(qGrid,
						tQueen.getRow(), tQueen.getColumn(), null,
						AnimalUtilities.colorToString(threateningQueenColor),
						null, null, null));
			}
			// mark the threatened queen
			language.addLine(AnimalUtilities.setGridColor(qGrid,
					queen.getRow(), queen.getColumn(), null,
					AnimalUtilities.colorToString(threatenedQueenColor), null,
					null, null));
			AnimalUtilities.easyHighlight(pseudoCode, 16);
			language.nextStep();

			// unmark them and omit the language.nextStep() to avoid unnecessary
			// animation steps
			for (Queen tQueen : threatening) {
				language.addLine(AnimalUtilities.setGridColor(qGrid,
						tQueen.getRow(), tQueen.getColumn(), null,
						AnimalUtilities.colorToString(queenColor), null, null,
						null));
			}
			// unmark the threatened queen
			language.addLine(AnimalUtilities.setGridColor(qGrid,
					queen.getRow(), queen.getColumn(), null,
					AnimalUtilities.colorToString(queenColor), null, null, null));
		} else {
			AnimalUtilities.easyHighlight(pseudoCode, 16);
			language.nextStep();
		}

		return threatening;
	}

	/**
	 * Refresh the number for the stepCounter.
	 */
	private void updateStepDisplay() {
		numberOfTimesQueensHaveBeenMoved++;
		stepCounterDisplay.setText("Anzahl Damenbewegungen: ".concat(Integer
				.toString(numberOfTimesQueensHaveBeenMoved)), null, null);
	}

	/**
	 * Create the initial description part of the animation (animation header,
	 * description text).
	 */
	private void createInitialDescription() {
		// create the algorithm banner
		language.addLine("text \"headerIntro\" \"Das\" at (30, 50) color "
				+ AnimalUtilities.colorToString(headTextColor)
				+ " font SansSerif size 30 bold depth 2");
		language.addLine("text \"headerAlgo\" \""
				+ n
				+ " Damenproblem\" offset (8, 0) from \"headerIntro\" NE color "
				+ AnimalUtilities.colorToString(headTextColor)
				+ " font SansSerif size 50 bold depth 2");

		// the description code which is displayed in the beginning
		String[] initialDescriptionText = {
				"Das Damenproblem ist eine schachmathematische Aufgabe.",
				"Es sollen jeweils n Damen auf einem Schachbrett so",
				"aufgestellt werden, dass keine zwei Damen einander nach",
				"den Schachregeln schlagen können. Die Figurenfarbe wird",
				"dabei ignoriert, und es wird angenommen, dass jede",
				"Figur jede andere angreifen könnte. Oder anders",
				"ausgedrückt: Es sollen sich keine zwei Damen die",
				"gleiche Reihe, Linie oder Diagonale teilen. Im",
				"Mittelpunkt steht die Frage nach der Anzahl der",
				"möglichen Lösungen.",
				"",
				"Im Folgenden wird diese Aufgabe für ".concat(
						Integer.toString(n)).concat(" Damen mittels"),
				"Backtracking gelöst. Dabei werden stets so viele Damen",
				"wie möglich Zeile für Zeile gesetzt. Lässt sich für eine",
				"Dame keine mögliche Position mehr finden, so springt der",
				"Algorithmus Zeile um Zeile zurück, um die bisher",
				"platzierten Damen auf eine womöglich bessere",
				"Ausgangsposition zu verschieben." };

		// set up the description codeGroup
		SourceCodeProperties initialDescriptionProp = new SourceCodeProperties();
		initialDescriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("SansSerif", Font.PLAIN, 14));
		SourceCode initialDescription = language.newSourceCode(new Coordinates(
				80, 125), "initialDescription", null, initialDescriptionProp);
		for (int i = 0; i < initialDescriptionText.length; i++) {
			initialDescription.addCodeLine(initialDescriptionText[i], null, 0,
					null);
		}
		language.nextStep("Beschreibung des Algorithmus");

		// hide the description in the next step
		initialDescription.hide();
	}

	/**
	 * Create the animation for the actual algorithm execution.
	 */
	private void createAlgorithmExecution() {
		// set up the pseudo code codeGroup
		SourceCodeProperties pseudoCodeProp = new SourceCodeProperties();
		pseudoCodeProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				pseudoCodeTextColor);
		pseudoCodeProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				pseudoCodeHighlightColor);
		pseudoCodeProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		pseudoCode = language.newSourceCode(new Coordinates(600, 90),
				"pseudoCode", null, pseudoCodeProp);

		// create and add Code lines for pseudo code
		String[] pseudoCodeLines = { "",
				"Prozedur LöseDamenproblem() gibt Wahrheitswert zurück",
				"    Falls Damenproblem(1) ist Wahr",
				"            gib Wahr zurück", "        Sonst",
				"            gib Falsch zurück", "",
				"Prozedur Damenproblem (Zeile) gibt Wahrheitswert zurück", "",
				"    Falls Zeile ungerade",
				"            setze neue Dame auf linkestes Feld von Zeile",
				"        Sonst",
				"            setze neue Dame auf rechtestes Feld von Zeile",
				"", "    Solange Dame innerhalb des Spielfeldes", "",
				"        Falls Dame ist nicht bedroht",
				"            Falls Zeile ist letzte Zeile",
				"                    gib Wahr zurück",
				"                Sonst Falls Damenproblem(Zeile+1) ist Wahr",
				"                    gib Wahr zurück", "",
				"        setze Dame eine Spalte weiter", "",
				"    entferne Dame von Spielfeld", "    gib Falsch zurück" };
		// line 0 is not supposed to be visible
		pseudoCode.addCodeLine("", null, 0, null);
		// every other line shall be numbered starting with 1
		for (int i = 1; i < pseudoCodeLines.length; i++) {
			pseudoCode.addCodeLine(
					String.format("%02d", i).concat(" ")
							.concat(pseudoCodeLines[i]), null, 0, null);
		}
		language.addLine("rectangle \"pseudoCodeBox\" offset (-16, 0) from \"pseudoCode\" NW offset (32, 12) from \"pseudoCode\" SE depth 2 filled fillColor "
				.concat(AnimalUtilities
						.colorToString(pseudoCodeBackgroundColor)));

		// create the display for the amount of times a queen has been moved
		stepCounterDisplay = language.newText(new Offset(0, 20, pseudoCode,
				AnimalScript.DIRECTION_SW), "Anzahl Damenbewegungen: "
				.concat(Integer.toString(numberOfTimesQueensHaveBeenMoved)),
				"stepCounterDisplay", null);

		// create the grid for the chess board and the queens
		CustomStringMatrixGenerator generator = new CustomStringMatrixGenerator(
				(AnimalScript) language, queenColor, threatenedQueenColor,
				gridColor1, gridColor2);
		qGrid = new StringMatrix(generator, new Coordinates(80, 125),
				new String[n][n], "qGrid", null, new MatrixProperties());

		// create the chessboard look by filling cells with even coordinates
		// gray
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				if ((row + col) % 2 == 0) {
					language.addLine(AnimalUtilities.setGridColor(qGrid, row,
							col, null, null,
							AnimalUtilities.colorToString(gridColor2), null,
							AnimalUtilities.colorToString(gridColor2)));
				}
			}
		}

		// start animating
		AnimalUtilities.easyHighlight(pseudoCode, 1);
		language.nextStep("Start");
		AnimalUtilities.easyHighlight(pseudoCode, 2);
		language.nextStep();

		// create the script for the actual algorithm execution and wait 1000ms
		// after its last step
		boolean solved = solveBacktrack(0);
		AnimalUtilities.easyHighlight(pseudoCode, solved ? 3 : 5);
		language.nextStep(2000, solved ? "Lösung" : "Fehlschlag");

		// hide the grid, the step counter, the pseudo code and the pseudo code
		// box in the next language step
		qGrid.hide();
		stepCounterDisplay.hide();
		pseudoCode.hide();
		language.addLine("hide \"pseudoCodeBox\"");
	}

	private void createFinalDescription() {
		// the description code which is displayed at the end of the animation
		// there are 3 different descriptions for one solution, no solution
		// and multiple possible solutions
		ArrayList<String> finalDescriptionText = new ArrayList<String>();
		if (n >= 4) {
			finalDescriptionText
					.addAll(Arrays.asList(
							"Für das Damenproblem mit "
									.concat(Integer.toString(n))
									.concat(" Damen existieren insgesamt ")
									.concat(Integer
											.toString(NUMBER_OF_OVERALL_SOLUTIONS[n]))
									.concat(" Lösungen. Einige"),
							"von Ihnen können jedoch durch Spiegeln und Drehen ineinander überführt werden.",
							"Daher gibt es nur "
									.concat(Integer
											.toString(NUMBER_OF_DISTINCT_SOLUTIONS[n]))
									.concat(NUMBER_OF_DISTINCT_SOLUTIONS[n] == 1 ? " Lösung"
											: " Lösungen")
									.concat(", die als eindeutig bezeichnet werden ")
									.concat(NUMBER_OF_DISTINCT_SOLUTIONS[n] == 1 ? "kann."
											: "können."),
							"",
							"Eine solche Lösung wurde mithilfe des Backtracking Algorithmus",
							"innerhalb von "
									.concat(Integer
											.toString(numberOfTimesQueensHaveBeenMoved))
									.concat(" Schritten gefunden."),
							"",
							"Der hier vorgestellte Algorithmus lässt sich ohne Veränderungen auf beliebig",
							"großen Schachfeldern bzw. mit beliebig vielen Damen anwenden. Da es sich jedoch",
							"um einen Algorithmus mit exponentieller Laufzeit handelt, führt dieses Vorgehen",
							"nicht für beliebig große Felder auch in annehmbarer Zeit zum Erfolg.",
							"",
							"Für normal große Schachfelder und somit das klassische 8 Damen Problem kann der",
							"Backtracking-Algorithmus jedoch in kurzer Zeit Ergebnisse liefern und ist somit",
							"trotz seiner Einfachheit praktisch einsetzbar."));
		} else if (n == 2 || n == 3) {
			finalDescriptionText
					.addAll(Arrays.asList(
							"Für das Damenproblem mit "
									.concat(Integer.toString(n))
									.concat(" Damen existiert keine gültige Lösung. Zu diesem"),
							"Schluss konnte der Backtracking Algorithmus innerhalb von "
									.concat(Integer
											.toString(numberOfTimesQueensHaveBeenMoved))
									.concat(" Schritten gelangen."),
							"",
							"Der hier vorgestellte Algorithmus lässt sich ohne Veränderungen auf beliebig",
							"großen Schachfeldern bzw. mit beliebig vielen Damen anwenden. Da es sich jedoch",
							"um einen Algorithmus mit exponentieller Laufzeit handelt, führt dieses Vorgehen",
							"nicht für beliebig große Felder auch in annehmbarer Zeit zum Erfolg.",
							"",
							"Für normal große Schachfelder und somit das klassische 8 Damen Problem kann der",
							"Backtracking-Algorithmus jedoch in kurzer Zeit Ergebnisse liefern und ist somit",
							"trotz seiner Einfachheit praktisch einsetzbar."));
		} else {
			finalDescriptionText.addAll(Arrays.asList(
					"Sie haben das ".concat(Integer.toString(n)).concat(
							" Damen Problem gelöst."), "", "",
					"Herzlichen Glückwunsch!"));
		}
		// set up the description codeGroup
		SourceCodeProperties finalDescriptionProp = new SourceCodeProperties();
		finalDescriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("SansSerif", Font.PLAIN, 14));
		SourceCode finalDescription = language.newSourceCode(new Coordinates(
				30, 125), "finalDescription", null, finalDescriptionProp);
		for (int i = 0; i < finalDescriptionText.size(); i++) {
			finalDescription.addCodeLine(finalDescriptionText.get(i), null, 0,
					null);
		}
		language.nextStep("Ende");
	}
}
