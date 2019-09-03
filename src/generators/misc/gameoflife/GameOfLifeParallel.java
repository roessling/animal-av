package generators.misc.gameoflife;


import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import util.IntVariable;
import util.StringMatrixExtended;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class GameOfLifeParallel implements ValidatingGenerator {

	private final int yMatrix = 100;

	private final String MATRIX = "board";

	public static final String CELL_ALIVE_SYMBOL = "X";
	public static final String CELL_DEAD_SYMBOL = "";

	public static final Timing DEFAULT_DURATION = new TicksTiming(15);
	public static final Timing ZERO_DURATION = new TicksTiming(0);

	private Language language;

	private final Set<BoardConfiguration> boardConfigurations = new HashSet<BoardConfiguration>();

	public static boolean[][] boardToBoolean(StringMatrixExtended board) {
		boolean[][] conf = new boolean[board.getNrRows()][board.getNrCols()];
		for (int row = 0; row < board.getNrRows(); row++) {
			for (int colon = 0; colon < board.getNrCols(); colon++) {
				String element = board.getElement(row, colon);
				conf[row][colon] = element.equals(CELL_ALIVE_SYMBOL);
			}
		}
		return conf;
	}

	private class BoardConfiguration {

		@Override
		public String toString() {

			StringBuilder builder = new StringBuilder();

			for (int row = 0; row < configuration.length; row++) {
				for (int colon = 0; colon < configuration[0].length; colon++) {
					builder.append(configuration[row][colon]);
					builder.append("\t");
				}
				builder.append("\n");
			}

			return builder.toString();
		}

		private final boolean[][] configuration;

		private BoardConfiguration(boolean[][] board) {
			this.configuration = board;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + toString().hashCode();

			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {

			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof BoardConfiguration)) {
				return false;
			}

			BoardConfiguration other = (BoardConfiguration) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}

			for (int row = 0; row < configuration.length; row++) {
				for (int colon = 0; colon < configuration[0].length; colon++) {
					if (configuration[row][colon] != other.configuration[row][colon])
						return false;
				}
			}

			return true;
		}

		private GameOfLifeParallel getOuterType() {
			return GameOfLifeParallel.this;
		}

	}

	@Override
	public String getAlgorithmName() {
		return "Game of Life";
	}

	@Override
	public String getAnimationAuthor() {
		return "Timo Baehr,Alexander Jandousek";
	}

	@Override
	public String getCodeExample() {
		String code = "public void gameOfLife(Board board, LineSolverPool solvers, maxsteps, rules) {\n"
				+ "	for (int steps = 0; steps < maxsteps && Board is a known Generation; steps++) {\n"
				+ "		Board newBoard =  new Board();\n"
				+ "		for(int i = 0; i < solvers.length ; i++){\n"
				+ "			List<Board> worklist = board.split(solvers.length, i);\n"
				+ "			solvers.getThread(i).setLinesToSolve(worklist, newBoard);\n"
				+ "		}\n"
				+ "		solvers.start();\n\n"
				+ "		//wait until every solver is finished\n"
				+ "		solvers.join();\n"
				+ "		Board = NewBoard;\n"
				+ "	}\n"
				+ "}\n";
		return code;
	}

	@Override
	public String getDescription() {
		return "\"Game of Life\" ist ein von dem Mathematiker John Horton Conway entworfenes Spiel, "
				+ "welches sich spielerisch mit der Automatentheorie auseinander setzt. Der zellulaere Automat "
				+ "wird als beliebig große Tabelle dargestellt. Diese Tabelle bildet das Spielfeld von \"Game of Life\". "
				+ "Jede Zelle bekommt entweder den Zustand \"lebendig\" "
				+ "oder \"tot\" zugewiesen. Die Folgegeneration ergibt sich durch die Befolgung "
				+ "einfacher Regeln, dem Zustand der Zelle selbst und dem Zustand der bis zu acht Nachbarzellen.\n\n"
				+ "Conway verwendete folgende Regeln:\t\n "
				// + "\n"
				// + "| |0|1|2|3|4|5|6|7|8|\n"
				// + "|G| | | |x| | | | | |\n"
				// + "|T|x|x| | |x|x|x|x| |\n"
				// + "\n"
				+ "3/0145678\t "
				+ "Die folgende Belegung bedeutet, dass bei drei \"lebendigen\" Nachbarzellen eine tote Zelle "
				+ "lebendig wird und eine lebende Zelle bei keinem oder einem sowie bei vier bis acht lebendigen"
				+ "Nachbarzellen stirbt und ansonsten der Zustand einer Zelle unangetastet bleibt."
				+ "Im folgenden wird eine lebendige Zelle durch ein \"X\" dargestellt und eine tote Zelle durch ein \"O\"."
				+ "Bei dem Algorithmus \"Game of Life\" wird in einem Berechnungsschritt nur eine Zelle der Tabelle veraendert. "
				+ "Der Zustand dieser Zelle laesst sich unabhaengig von dem Zustand der anderen Zellen berechnen. Der "
				+ "Algorithmus eignet sich daher gut dazu, parallelisiert zu werden. In dieser Version von \"Game of Life\" wird "
				+ "die Berechnung der nächsten Generation parallelisiert. In diesem Beispiel übernimmt jeder Prozessor "
				+ "(im Pseudocode LineSolver genannt) die Berechnung einer Menge von Reihen des Spielfelds. "
				+ "Wird die Berechnung auf zwei Prozessoren aufgeteilt, so übernimmt der erste Prozessor "
				+ "die Reihen mit einem ungeraden Index (1, 3, ...) und der zweite Prozessor die Reihen mit einem geraden Index.";

	}

	public List<String> getConclusion(int steps, int maxsteps) {

		List<String> conclusion = new ArrayList<String>();
		if (steps == 1) {
			conclusion
			.add("Ueberpruefen Sie Ihre Regeln oder die Ursprungsgeneration.");
			conclusion
			.add("Die erste Folgegeneration unterscheidet sich nicht von der Ursprungsgeneration.");
		}
		if (steps < maxsteps) {
			conclusion
			.add("Die "
					+ steps
					+ ". Folgegeneration gleicht einer bereits dagewesenen Generation.");
		} else {
			conclusion
			.add("Die Hoechstanzahl an Schritten wurde erreicht, bevor die Folgegeneration");
			conclusion.add("einer bereits dagewesenen Generation gleicht.");
		}
		conclusion.add("");
		conclusion
		.add("Vergleicht man die Anzahl von Animationsfolien für den nicht-parallelisierten");
		conclusion
		.add("'Game of Life'-Algorithmus mit dieser parallelisierten Variante, kann gut");
		conclusion
		.add("nachvollzogen werden, dass sich die Ausfuehrungszeit für die Berechnung der");
		conclusion
		.add("neuen Generation verbessert (Genaue Verbesserung Faktor 1/Prozessorzahl) und");
		conclusion
		.add("warum sich die Parallelisierung des Algorithmus lohnt.");
		conclusion.add("");
		conclusion
		.add("Anmerkung: Die zusätzliche Dauer für die Aufteilung der Ursprungsmatrix in");
		conclusion
		.add("Teilmatrizen kann außer Acht gelassen werden. Sie dient nur zur Verdeutlichung des");
		conclusion
		.add("Algorithmus. In einer parallelisierten Implementierung von 'Game of Life' können");
		conclusion
		.add("alle Prozessoren direkt auf der Ursprungsmatrix arbeiten. Eine Aufteilung der");
		conclusion.add("Matrix ist nicht nötig.");

		return conclusion;
	}

	/**
	 * Show the sourcecode below the matrix. This also returns the source code.
	 */
	private SourceCode showSourceCode(StringMatrixExtended matrix , SourceCodeProperties sourceCodeProperties) {
		// now, create the source code entity
		SourceCode sc = language.newSourceCode(new Coordinates(50,
				50 + (50 * matrix.getNrRows())), "sourceCode", null, sourceCodeProperties);

		// add a code line
		// parameters: code itself; name (can be null); indentation level;
		// display options
		sc.addCodeLine(
				"public void gameOfLife(Board board, Threadpool solvers, maxsteps, rules) {",
				null, 0, null);

		sc.addCodeLine(
				"for (int steps = 0; steps < maxsteps && Board is a known Generation; steps++) {",
				null, 1, null);

		sc.addCodeLine("Board newBoard = new Board();", null, 2, null);
		sc.addCodeLine("for(int i = 0; i < solvers.count() ; i++){", null, 2,
				null);

		sc.addCodeLine(
				"List<Board> worklist = board.split(solvers.length, i);", null,
				3, null);
		sc.addCodeLine(
				"solvers.getThread(i).setLinesToSolve(worklist, newBoard);",
				null, 3, null);

		sc.addCodeLine("}", null, 2, null);

		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("solvers.start();", null, 1, null);
		sc.addCodeLine("", null, 1, null);
		sc.addCodeLine("//wait until every solver is finished", null, 1, null);
		sc.addCodeLine("solvers.join();", null, 1, null);
		sc.addCodeLine("Board = NewBoard;", null, 1, null);

		sc.addCodeLine("}", null, 0, null);

		return sc;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return "Game of Life Parallel";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
		language = new AnimalScript("Game of Life Parallel",
				"Timo Bähr, Alexander Jandousek", 1024, 768);
		language.setStepMode(true);
	}

	private String[][] createStubData(int rows, int colunns) {
		String[][] t = new String[rows][colunns];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < colunns; j++)
				t[i][j] = "";
		return t;
	}

	private boolean isAlive(StringMatrixExtended board) {

		BoardConfiguration conf = new BoardConfiguration(boardToBoolean(board));

		return boardConfigurations.add(conf);
	}

	public List<GameOfLifeSubProcess> initProcesses(int processors, StringMatrixExtended currentBoard) {
		/*
		 * For each processor create a worklist (or stack) containing all rows which should
		 * be updated from this processor.
		 */
		List<List<WorkItem>> listOfWorkLists = new LinkedList<List<WorkItem>>();

		for (int i = 0; i < processors; i++) {
			listOfWorkLists.add(new LinkedList<WorkItem>());
		}

		Iterator<List<WorkItem>> iterator = listOfWorkLists.iterator();
		for (int row = 0; row < currentBoard.getNrRows(); row++) {
			int startRow = row;

			if (row > 0)
				startRow--;

			int endRow = row;

			if (row < currentBoard.getNrRows() - 1)
				endRow++;

			StringMatrixExtended submatrix = 
					currentBoard.getSubMatrix(startRow, endRow, new Coordinates(0, 0));
			submatrix.hide();

			// Start from the beginning if end of list is reached
			if (!iterator.hasNext()) {
				iterator = listOfWorkLists.iterator();
			}
			List<WorkItem> worklist = iterator.next();
			worklist.add(new WorkItem(submatrix, row, currentBoard.getNrRows()));
		}

		List<GameOfLifeSubProcess> processes = new LinkedList<GameOfLifeSubProcess>();

		// for each processor create a GameOfLifeSubProcess
		for (List<WorkItem> worklist : listOfWorkLists) {
			processes.add(new GameOfLifeSubProcess(worklist));
		}

		return processes;
	}

	private void setBoard(String[][] data, StringMatrixExtended matrix) {
		for (int row = 0; row < matrix.getNrRows(); row++)
			for (int colon = 0; colon < matrix.getNrCols(); colon++) {
				matrix.put(row, colon, data[row][colon], null, DEFAULT_DURATION);
			}
	}

	@Override
	public String generate(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) {
		// reset board configurations (only relevant in case of multiple executions)
		boardConfigurations.clear();

		MatrixProperties matrixProperties = (MatrixProperties) properties.getPropertiesByName("GameBoard");
		MatrixProperties rulesProperties  = (MatrixProperties) properties.getPropertiesByName("Rules");
		TextProperties statisticsProperties = (TextProperties) properties.getPropertiesByName("Statistics");
		TextProperties titleProperties = (TextProperties) properties.getPropertiesByName("Title");
		SourceCodeProperties sourceCodeProperties = (SourceCodeProperties) properties.getPropertiesByName("SourceCode");
		TextProperties textualDescriptionsProperties = (TextProperties) properties.getPropertiesByName("TextualDescriptions");
		RectProperties titleBoxProperties = (RectProperties) properties.getPropertiesByName("TitleBox");

		IntVariable steps = new IntVariable(language, new Coordinates(0, 0),
				"steps", "steps", 0, ZERO_DURATION, statisticsProperties);
		steps.hide();
		// --------------------------------------------------------------------------------------------------------------
		int[] BirthRules = (int[]) primitives.get("BirthRules");

		Set<Integer> g = new HashSet<Integer>();
		for (int i : BirthRules)
			g.add(i);

		int[] DieRules = (int[]) primitives.get("DieRules");

		Set<Integer> t = new HashSet<Integer>();
		for (int i : DieRules)
			t.add(i);

		String[][] rule = new String[3][10];

		String[][] gameBoard = (String[][]) primitives.get("GameBoard");
		StringMatrixExtended currentBoard = new StringMatrixExtended(language,
				new Coordinates(50, yMatrix), gameBoard, MATRIX, null,
				matrixProperties);

		StringMatrixExtended nextBoard = new StringMatrixExtended(language,
				new Coordinates(50 * currentBoard.getNrCols(), yMatrix),
				createStubData(currentBoard.getNrRows(),
						currentBoard.getNrCols()), "temp", null,
						matrixProperties);

		nextBoard.hide();
		currentBoard.hide();

		/* create the first slide */

		Font oldFont = (Font) titleProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(oldFont.getName(), Font.BOLD, 24));

		Text header = language.newText(new Coordinates(40, 20), getName(),
				"header", null, titleProperties);
		Rect headerRect = language.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, titleBoxProperties);

		String description1[] = {
				"'Game of Life' ist ein von dem Mathematiker John Horton Conway",
				"entworfenes Spiel, welches sich spielerisch mit der Automatentheorie",
				"auseinander setzt. Der zellulaere Automat wird als beliebig große",
				"Tabelle dargestellt. Diese Tabelle bildet das Spielfeld von 'Game of ",
				"Life'. Jede Zelle bekommt entweder den Zustand 'lebendig' oder 'tot'",
				"zugewiesen. Die Folgegeneration ergibt sich durch die Befolgung",
				"einfacher Regeln, dem Zustand der Zelle selbst und dem Zustand",
		"der bis zu acht Nachbarzellen." };

		String[][] exampleRule = new String[][] {
				{ " ", "0", "1", "2", "3", "4", "5", "6", "7", "8" },
				{ "G", " ", " ", " ", "x", " ", " ", " ", " ", " " },
				{ "T", "x", "x", " ", " ", "x", "x", "x", "x", "x" } };

		String description2[] = {
				"",
				"Conway verwendete folgende Regeln:",
				"Die folgende Belegung bedeutet, dass bei drei 'lebendigen' ",
				"Nachbarzellen eine tote Zelle lebendig wird und eine lebende Zelle",
				"bei keinem oder einem sowie bei vier bis acht lebendigen",
				"Nachbarzellen stirbt und ansonsten der Zustand einer Zelle",
		"unangetastet bleibt." };

		String description3[] = {
				"",
				"Bei dem Algorithmus 'Game of Life' wird in einem Berechnungsschritt nur ",
				"eine Zelle der Tabelle veraendert. Der Zustand dieser Zelle laesst sich ",
				"unabhaengig von dem Zustand der anderen Zellen berechnen. Der Algorithmus ",
				"eignet sich daher gut dazu, parallelisiert zu werden. In dieser Version",
				"von 'Game of Life' wird die Berechnung der nächsten Generation parallel-",
				"isiert. In diesem Beispiel übernimmt jeder Prozessor (im Pseudocode ",
				"LineSolver genannt) die Berechnung einer Menge von Reihen des Spielfelds.",
				"Wird die Berechnung auf zwei Prozessoren aufgeteilt, so übernimmt der ",
				"erste Prozessor die Reihen mit einem ungeraden Index (1, 3, ...) und der",
		"zweite Prozessor die Reihen mit einem geraden Index." };

		Font oldFontDescription = (Font) textualDescriptionsProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
		textualDescriptionsProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(oldFontDescription.getName(), Font.PLAIN, 16));

		language.newText(new Coordinates(40, 70), description1[0],
				"description1", null, textualDescriptionsProperties);

		int lineNumber;
		for (lineNumber = 1; lineNumber < description1.length; lineNumber++) {
			language.newText(new Offset(0, 25, "description" + lineNumber,
					AnimalScript.DIRECTION_NW), description1[lineNumber],
					"description" + (lineNumber + 1), null, textualDescriptionsProperties);
		}

		/* create the next slide */
		language.nextStep();

		language.newText(new Offset(0, 25, "description" + lineNumber,
				AnimalScript.DIRECTION_NW), description2[0], "description"
						+ (lineNumber + 1), null, textualDescriptionsProperties);
		lineNumber++;
		language.newText(new Offset(0, 25, "description" + lineNumber,
				AnimalScript.DIRECTION_NW), description2[1], "description"
						+ (lineNumber + 1), null, textualDescriptionsProperties);
		lineNumber++;

		StringMatrixExtended exampleRuleMatrix = new StringMatrixExtended(
				language, new Offset(0, 40, "description" + lineNumber,
						AnimalScript.DIRECTION_NW), exampleRule, "exampleRule",
						null, rulesProperties);

		lineNumber++;

		language.newText(new Offset(0, 25, "exampleRule",
				AnimalScript.DIRECTION_SW), description2[2], "description"
						+ (lineNumber + 1), null, textualDescriptionsProperties);
		lineNumber++;

		for (int j = 3; j < description2.length; j++) {
			language.newText(new Offset(0, 25, "description" + lineNumber,
					AnimalScript.DIRECTION_NW), description2[j], "description"
							+ (lineNumber + 1), null, textualDescriptionsProperties);
			lineNumber++;
		}

		/* create a second description slide */
		language.nextStep();
		language.hideAllPrimitives();
		exampleRuleMatrix.hide();
		header.show();
		headerRect.show();



		language.newText(new Coordinates(40, 70), description3[0],
				"seconddescription1", null, textualDescriptionsProperties);

		for (lineNumber = 1; lineNumber < description3.length; lineNumber++) {
			language.newText(
					new Offset(0, 25, "seconddescription" + lineNumber,
							AnimalScript.DIRECTION_NW),
							description3[lineNumber], "seconddescription"
									+ (lineNumber + 1), null, textualDescriptionsProperties);
		}

		/* create the next slide (board) */
		language.nextStep();
		language.hideAllPrimitives();
		currentBoard.show();

		int maxSteps = (Integer) primitives.get("MaxIterations");

		SourceCode sc = showSourceCode(currentBoard, sourceCodeProperties);

		rule[0] = new String[] { " ", "0", "1", "2", "3", "4", "5", "6", "7",
		"8" };
		rule[1][0] = "G";
		rule[2][0] = "T";

		for (int i = 1; i < 10; i++) {
			if (g.contains(i - 1))
				rule[1][i] = "x";
			else
				rule[1][i] = " ";

			if (t.contains(i - 1))
				rule[2][i] = "x";
			else
				rule[2][i] = " ";

		}
		// show rules
		StringMatrixExtended ruleMatrix = new StringMatrixExtended(language,
				new Coordinates(50, 300 + 50 * currentBoard.getNrRows()), rule,
				"rule", null, rulesProperties);

		sc.highlight(0);
		language.nextStep();

		for (int currentStep = 0; currentStep < maxSteps && isAlive(currentBoard); currentStep++) {
			sc.toggleHighlight(12, 1);
			sc.toggleHighlight(0, 1);
			language.nextStep("Step " + currentStep);

			sc.toggleHighlight(1, 2);
			nextBoard.show();
			language.nextStep();

			// set rows for each process
			int processors = (Integer) primitives.get("Processors");
			List<GameOfLifeSubProcess> processes = this.initProcesses(processors, currentBoard);

			List<IntVariable> counters = new LinkedList<IntVariable>();
			List<Text> names = new LinkedList<Text>();

			int previousPosition = yMatrix;
			int element = 0;

			sc.toggleHighlight(2, 3);
			language.nextStep();

			for (GameOfLifeSubProcess process : processes) {
				sc.toggleHighlight(3, 4);
				language.nextStep();

				sc.toggleHighlight(4, 5);
				language.nextStep();

				process.moveAllTo("SE", null, new Coordinates(
						150 * currentBoard.getNrCols() + 50, previousPosition));

				Coordinates coords = new Coordinates(150
						* currentBoard.getNrCols() + 100 + 50
						* process.getMaxNumberOfColons(), previousPosition);

				names.add(language.newText(coords, "Process" + element, "name", null));
				counters.add(new IntVariable(language, new Coordinates(coords
						.getX(), coords.getY() + 15), "count", "count", 0,
						ZERO_DURATION, statisticsProperties));

				language.nextStep("Set rows for processor "+element);
				previousPosition += process.getMaxNumberOfRows() * 50 + 50;
				element++;

				sc.toggleHighlight(5, 3);
				language.nextStep();
			}

			List<Boolean> todo = new LinkedList<Boolean>();

			boolean begining = true;

			while (true) {

				todo.clear();

				for (GameOfLifeSubProcess process : processes) {
					todo.add(process.nextMatrix());
				}

				if (begining) {
					sc.toggleHighlight(3, 8);
					language.nextStep();
					begining = false;
				}

				int todoCount = 0;

				for (Boolean bool : todo) {
					if (bool)
						todoCount++;
				}

				if (todoCount == 0)
					break;

				for (int i = 0; i < currentBoard.getNrCols(); i++) {
					StringBuilder rows = new StringBuilder();
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						rows.append(process.getCurrentRow()).append(",");
						
						if (todo.get(j) && process.nextCell()) {
							process.initLookup();
							Coordinates coord = process.getCoordinatesOfCurrentCell();
							currentBoard.highlightCell(coord.getY(), coord.getX(), null, DEFAULT_DURATION);
							nextBoard.highlightCell(coord.getY(), coord.getX(), null, DEFAULT_DURATION);
						}
					}
					rows.setLength(rows.length()-1);
					language.nextStep("Processes updates cells in column "+i+" (in rows "+rows+")");

					sc.toggleHighlight(8, 11);

					// for each process lookup the eight cells around the current one
					boolean changed = false;
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j))
							if (process.lookupLeftTop()) {
								counters.get(j).set(process.getCurrentCount());
								changed = true;
							}
					}
					if (changed)
						language.nextStep();

					changed = false;
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j))
							if (process.lookupTop()) {
								counters.get(j).set(process.getCurrentCount());
								changed = true;
							}
					}
					if (changed)
						language.nextStep();

					changed = false;
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j))
							if (process.lookupRightTop()) {
								counters.get(j).set(process.getCurrentCount());
								changed = true;
							}
					}
					if (changed)
						language.nextStep();

					changed = false;
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j))
							if (process.lookupRight()) {
								counters.get(j).set(process.getCurrentCount());
								changed = true;
							}
					}
					if (changed)
						language.nextStep();

					changed = false;
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j))
							if (process.lookupRightBottom()) {
								counters.get(j).set(process.getCurrentCount());
								changed = true;
							}
					}
					if (changed)
						language.nextStep();

					changed = false;
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j))
							if (process.lookupBottom()) {
								counters.get(j).set(process.getCurrentCount());
								changed = true;
							}
					}
					if (changed)
						language.nextStep();

					changed = false;
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j))
							if (process.lookupLeftBottom()) {
								counters.get(j).set(process.getCurrentCount());
								changed = true;
							}
					}
					if (changed)
						language.nextStep();

					changed = false;
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j))
							if (process.lookupLeft()) {
								counters.get(j).set(process.getCurrentCount());
								changed = true;
							}
					}
					if (changed)
						language.nextStep();
					
					// highlight rule matrix
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						int count = process.getCurrentCount();
						ruleMatrix.highlightColumn(count+1, null, null);
					}
					language.nextStep();

					//sc.toggleHighlight(8, 11);
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j)) {
							nextBoard.put(process.getCoordinatesOfCurrentCell()
									.getY(), process
									.getCoordinatesOfCurrentCell().getX(),
									process.getNextCellStatus(g, t), null,
									DEFAULT_DURATION);
							process.clearLookup();
							counters.get(j).set(0);
						}
					}
					language.nextStep();

					// unhighlight rule matrix
					for (int row = 0; row < ruleMatrix.getNrRows(); row++) {
						for (int col = 0; col < ruleMatrix.getNrCols(); col++) {
							ruleMatrix.unhighlightCell(row, col, null, null);
						}
					}
					
					// unhighlight boards
					for (int j = 0; j < processes.size(); j++) {
						GameOfLifeSubProcess process = processes.get(j);
						if (todo.get(j)) {
							Coordinates current = process
									.getCoordinatesOfCurrentCell();
							nextBoard.unhighlightCell(current.getY(),
									current.getX(), null, DEFAULT_DURATION);
							currentBoard.unhighlightCell(current.getY(),
									current.getX(), null, DEFAULT_DURATION);
						}
					}
					language.nextStep();

				}

			}

			for (IntVariable counter : counters)
				counter.hide();

			for (Text name : names)
				name.hide();

			// Move new configuration to old target
			nextBoard.moveTo("W", null, new Coordinates(50, yMatrix), null,
					DEFAULT_DURATION);

			// Delete content of old board
			setBoard(
					createStubData(currentBoard.getNrRows(),
							currentBoard.getNrCols()), currentBoard);
			currentBoard.hide();
			currentBoard.moveTo("E", null,
					new Coordinates(50 * currentBoard.getNrCols(), yMatrix),
					null, DEFAULT_DURATION);

			sc.toggleHighlight(11, 12);
			language.nextStep();

			StringMatrixExtended temp = currentBoard;
			currentBoard = nextBoard;
			nextBoard = temp;

			// Increment step counter in UI
			steps.increment();
		}

		nextBoard.hide();
		sc.toggleHighlight(12, 1);
		language.nextStep();

		sc.unhighlight(1);
		//language.nextStep();
		// ----------------------------------------------------------------------------------------------------------------------------------

		/* create the last slide */
		language.nextStep();
		language.hideAllPrimitives();
		exampleRuleMatrix.hide();
		currentBoard.hide();
		nextBoard.hide();
		ruleMatrix.hide();

		header.show();
		headerRect.show();

		List<String> conclusionList = getConclusion(steps.getIntegerValue(),
				maxSteps);

		// setup the last slide with the conclusion
		//		TextProperties conclusionProps = new TextProperties();
		//		conclusionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		//				Font.SANS_SERIF, Font.PLAIN, 16));

		Font f = (Font) titleProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
		textualDescriptionsProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getName(), Font.PLAIN, 16));

		language.newText(new Coordinates(40, 70), conclusionList.get(0),
				"conclusion1", null, textualDescriptionsProperties);
		for (int k = 1; k < conclusionList.size(); k++) {
			language.newText(new Offset(0, 25, "conclusion" + k,
					AnimalScript.DIRECTION_NW), conclusionList.get(k),
					"conclusion" + (k + 1), null, textualDescriptionsProperties);
		}

		return language.toString().replaceAll("refresh", "");
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		boolean error = true;

		StringBuffer errorMessage = new StringBuffer();


		int processors = (Integer) primitives.get("Processors");

		if(processors < 1){
			errorMessage.append("The number of processors has to be greater or equal 1 but was ");
			errorMessage.append(processors);
			errorMessage.append(".\n");
			error = false;
		}

		String[][] gameBoard = (String[][])primitives.get("GameBoard");
		if(gameBoard.length < processors){
			errorMessage.append("The number of processors (");
			errorMessage.append(processors);
			errorMessage.append(") has to be less or equal the number of \"GameBoard\" rows (");
			errorMessage.append(gameBoard.length);
			errorMessage.append(").\n");
			error = false;
		}

		int maxSteps = (Integer) primitives.get("MaxIterations");

		if(maxSteps < 1){
			errorMessage.append("The value of \"MaxIterations\" should be greater then 0 but was ");
			errorMessage.append(maxSteps);
			errorMessage.append(".\n");
			error = false;
		}



		for(int x = 0; x < gameBoard.length; x++)
			for(int y = 0; y < gameBoard[0].length; y++){
				if(!gameBoard[x][y].equals(CELL_ALIVE_SYMBOL) && !gameBoard[x][y].equals(CELL_DEAD_SYMBOL)){
					errorMessage.append("The \"GameBoard\" has the unknown entry \"");
					errorMessage.append(gameBoard[x][y]);
					errorMessage.append("\" at column ");
					errorMessage.append(y);
					errorMessage.append(" in row ");
					errorMessage.append(x);
					errorMessage.append(".\n");
					error = false;
				}
			}

		int[] BirthRules = (int[])primitives.get("BirthRules");

		if(BirthRules.length > 8){
			errorMessage.append("The \"BirthRules\" can only have a maximum of 8 entries but has ");
			errorMessage.append(BirthRules.length);
			errorMessage.append(".\n");
			error = false;
		}

		Map<Integer, Set<Integer>> birthRules = new HashMap<Integer, Set<Integer>>();
		for(int column = 0; column < BirthRules.length ; column++){
			Integer rule = BirthRules[column];
			if(rule < 0 || rule > 8){
				errorMessage.append("A \"BirthRule\" can only have a value between 0 and 8 but has ");
				errorMessage.append(rule);
				errorMessage.append(" in column ");
				errorMessage.append(column);
				errorMessage.append(".\n");
				error = false;
			}
			if(!birthRules.containsKey(rule))
				birthRules.put(rule, new HashSet<Integer>());

			birthRules.get(rule).add(column);
		}

		for(Integer key : birthRules.keySet()){
			if(birthRules.get(key).size() > 1){
				errorMessage.append("A \"BirthRule\" has to be unique. \n");
				errorMessage.append("The \"BirthRule\" ");
				errorMessage.append(key);
				errorMessage.append(" has duplicate entries in the columns");
				for(Integer column : birthRules.get(key)){
					errorMessage.append(" ");
					errorMessage.append(column);
					errorMessage.append(",");
				}

				errorMessage.setLength(errorMessage.length()-1);
				errorMessage.append(".\n");
				error=false;
			}
		}


		int[] DieRules = (int[])primitives.get("DieRules");

		if(DieRules.length > 8){
			errorMessage.append("The \"DieRules\" can only have a maximum of 8 entries but has ");
			errorMessage.append(DieRules.length);
			errorMessage.append(".\n");
			error = false;
		}

		Map<Integer, Set<Integer>> dieRules = new HashMap<Integer, Set<Integer>>();
		for(int column = 0; column < DieRules.length ; column++){
			Integer rule = DieRules[column];
			if(rule < 0 || rule > 8){
				errorMessage.append("A \"DieRules\" can only have a value between 0 and 8 but has ");
				errorMessage.append(rule);
				errorMessage.append(" in column ");
				errorMessage.append(column);
				errorMessage.append(".\n");
				error = false;
			}

			if(!dieRules.containsKey(rule))
				dieRules.put(rule, new HashSet<Integer>());

			dieRules.get(rule).add(column);
		}

		for(Integer key : dieRules.keySet()){
			if(dieRules.get(key).size() > 1){
				errorMessage.append("A \"DieRule\" has to be unique. \n");
				errorMessage.append("The \"DieRule\" ");
				errorMessage.append(key);
				errorMessage.append(" has duplicate entries in the columns");
				for(Integer column : dieRules.get(key)){
					errorMessage.append(" ");
					errorMessage.append(column);
					errorMessage.append(",");
				}

				errorMessage.setLength(errorMessage.length()-1);
				errorMessage.append(".\n");
				error=false;
			}
		}

		for(Integer key : birthRules.keySet())
			if(dieRules.keySet().contains(key)){
				errorMessage.append("Contradictory rules. The rule ");
				errorMessage.append(key);
				errorMessage.append(" is present in both \"BirthRules\" and \"DieRules\".\n");
				error=false;
			}

		if(!error)
			throw new IllegalArgumentException(errorMessage.toString());

		return error;
	}
}
