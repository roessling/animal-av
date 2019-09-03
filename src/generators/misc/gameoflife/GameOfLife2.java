package generators.misc.gameoflife;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

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

/**
 * Generator for 'Game Of Life'.
 * 
 * @author Timo Baehr
 * @author Alexander Jandousek
 */
public class GameOfLife2 implements ValidatingGenerator {
	
	/** This counts the amount of accesses during a program */
	private IntVariable accessCounter;
	
	/** This counts the amount of assignments during a program */
	private IntVariable assignmentCounter;
	
	/** This counts the amount of generations in GAME OF LIFE */
	private IntVariable steps;
	
	/** This is the number of neighbors alive of the current cell */
	private IntVariable neighborsAlive;
	
	/**
	 * The abstract Language class defines the basic methods for all particular languages 
	 * like AnimalScript for example, which then itselves provide functionality
	 * for output management, a name registry for primitives and factory methods 
	 * for all supported primitives.
	 */
	private Language language;
	
	private final int yMatrix = 100;
	
	/* Notational conventions */
	
	private final String MATRIX = "board";
	private final String CELL_ALIVE_SYMBOL = "X";
	private final String CELL_DEAD_SYMBOL = "";
	
	private final Timing DEFAULT_DURATION = new TicksTiming(15);
	
	private final Timing ZERO_DURATION = new TicksTiming(0);
	
	// Keep track of all previous configurations
	private final Set<BoardConfiguration> boardConfigurations = new HashSet<BoardConfiguration>();

	private Color backgroundColor = Color.WHITE;
	private Color backgroundColorHighlighted = Color.RED;
	private Color backgroundColorSelected = Color.YELLOW;

    /** This stores a generation of GAME OF LIFE */
	private class BoardConfiguration{
		
		@Override
		public String toString(){
			
			StringBuilder builder = new StringBuilder();
			
			for(int row = 0; row < configuration.length ; row++){
				for(int colon = 0; colon < configuration[0].length ; colon++){
					builder.append(configuration[row][colon]);
					builder.append("\t");
				}
				builder.append("\n");
			}
			
			return builder.toString();
		}

		private final boolean[][] configuration;
		
		private BoardConfiguration(boolean[][] board){
			this.configuration = board;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + toString().hashCode();
			
			return result;
		}

		/* (non-Javadoc)
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
			
			for(int row = 0; row < configuration.length ; row++){
				for(int colon = 0; colon < configuration[0].length ; colon++){
					if(configuration[row][colon] != other.configuration[row][colon])
						return false;
				}
			}
			
			return true;
		}

		private GameOfLife2 getOuterType() {
			return GameOfLife2.this;
		}
		
	}
	
	/* constructors for this GAME OF LIFE generator */
	
//	public GameOfLife(Language language){
//		this.language = language;
//		language.setStepMode(true);
//	}
//
//	public GameOfLife(){
//		this(new AnimalScript("Game of Life [DE]", "Timo Baehr, Alexander Jandousek", 640, 480));
//		language.setStepMode(true);
//	}
	
	/* methods of this class */
	
	private int getCellsAliveAround(StringMatrixExtended board, int row, int column){
		boolean[][] b = boardToBoolean(board);
		int numberOfLivingCells = 0;
		
		List<Coordinates> highlighted = new LinkedList<Coordinates>();

		if ((row - 1) >= 0 && (row - 1) < b.length && (column - 1) >= 0 && (column - 1) < b[row - 1].length) {
			
			Coordinates coordinate = new Coordinates(column-1, row-1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				numberOfLivingCells++;
				neighborsAlive.increment();
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColorHighlighted);
			accessCounter.increment();
			highlighted.add(coordinate);
			language.nextStep();
			
		}
		if ((row - 1) >= 0 && (row - 1) < b.length && (column) >= 0 && (column) < b[row - 1].length) {
			
			Coordinates coordinate = new Coordinates(column, row-1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				numberOfLivingCells++;
				neighborsAlive.increment();
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColorHighlighted);
			accessCounter.increment();
			highlighted.add(coordinate);
			language.nextStep();
			
		}
		if ((row - 1) >= 0 && (row - 1) < b.length && (column + 1) >= 0 && (column + 1) < b[row - 1].length) {
			
			Coordinates coordinate = new Coordinates(column + 1, row - 1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				numberOfLivingCells++;
				neighborsAlive.increment();
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColorHighlighted);
			accessCounter.increment();
			highlighted.add(coordinate);
			language.nextStep();
		}
		if ((row) >= 0 && (row) < b.length && (column + 1) >= 0 && (column + 1) < b[row].length) {
			
			Coordinates coordinate = new Coordinates(column + 1, row ); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				numberOfLivingCells++;
				neighborsAlive.increment();
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColorHighlighted);
			accessCounter.increment();
			highlighted.add(coordinate);
			language.nextStep();
		}
		if ((row + 1) >= 0 && (row + 1) < b.length && (column + 1) >= 0 && (column + 1) < b[row + 1].length) {
			
			Coordinates coordinate = new Coordinates(column + 1, row + 1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				numberOfLivingCells++;
				neighborsAlive.increment();
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColorHighlighted);
			accessCounter.increment();
			highlighted.add(coordinate);
			language.nextStep();
		}
		if ((row + 1) >= 0 && (row + 1) < b.length && (column) >= 0 && (column) < b[row + 1].length) {
			
			Coordinates coordinate = new Coordinates(column, row + 1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				numberOfLivingCells++;
				neighborsAlive.increment();
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColorHighlighted);
			accessCounter.increment();
			highlighted.add(coordinate);
			language.nextStep();
		}
		if ((row + 1) >= 0 && (row + 1) < b.length && (column - 1) >= 0 && (column - 1) < b[row + 1].length) {
			
			Coordinates coordinate = new Coordinates(column - 1, row + 1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				numberOfLivingCells++;
				neighborsAlive.increment();
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColorHighlighted);
			accessCounter.increment();
			highlighted.add(coordinate);
			language.nextStep();
		}
		if ((row) >= 0 && (row) < b.length && (column - 1) >= 0 && (column - 1) < b[row].length) {
			
			Coordinates coordinate = new Coordinates(column - 1, row); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				numberOfLivingCells++;
				neighborsAlive.increment();
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColorHighlighted);
			accessCounter.increment();
			highlighted.add(coordinate);
			language.nextStep();
		}
		
		for(Coordinates coordinate : highlighted)
			board.setBackgroundColor(coordinate.getY(), coordinate.getX(), backgroundColor);
			//board.unhighlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);

		return numberOfLivingCells;
	}
	
	private void calculateNewBoard(StringMatrixExtended board, StringMatrixExtended newBoard, 
			Set<Integer> g, Set<Integer> t, SourceCode source, int currentStep, StringMatrixExtended ruleMatrix) {
		
		Coordinates lastHighlight = null;
		
		language.nextStep("Step "+currentStep);
		for(int row = 0; row < board.getNrRows() ; row++){
			
			source.toggleHighlight(2, 3);
			//language.nextStep();
			
			language.nextStep("  Updating row "+row);
			for(int column = 0 ; column < board.getNrCols() ; column++){
				source.toggleHighlight(3, 4);
				language.nextStep("    Updating cell ("+row+"|"+column+")");
				
				//highlight actual cell
				accessCounter.increment();
				
//				board.highlightCell(row, column, null, DEFAULT_DURATION);
				board.setBackgroundColor(row, column, backgroundColorSelected);
				
				//unhighlight last cell
//				if(lastHighlight != null){
//					board.unhighlightCell(lastHighlight.getY(), lastHighlight.getX(), null, DEFAULT_DURATION);
//					//newBoard.unhighlightCell(lastHighlight.getY(), lastHighlight.getX(), null, DEFAULT_DURATION);
//				}
				
				source.toggleHighlight(4, 5);
				neighborsAlive.show();
				language.nextStep(); // highlight Matrix
				
				Integer count = getCellsAliveAround(board, row, column);

				ruleMatrix.highlightColumn(count+1, null, null);
				language.nextStep();
				
				if(board.getElement(row, column).equals(CELL_ALIVE_SYMBOL)){
					
					if(t.contains(count))
						newBoard.put(row, column, CELL_DEAD_SYMBOL, null, DEFAULT_DURATION);
					else 
						newBoard.put(row, column, CELL_ALIVE_SYMBOL, null, DEFAULT_DURATION);
					
				} else {
					
					if(g.contains(count))
						newBoard.put(row, column, CELL_ALIVE_SYMBOL, null, DEFAULT_DURATION);
					else
						newBoard.put(row, column, CELL_DEAD_SYMBOL, null, DEFAULT_DURATION);
				}
				
				assignmentCounter.increment();
				
//				newBoard.highlightCell(row, column, null, DEFAULT_DURATION);
				newBoard.setBackgroundColor(row, column, backgroundColorSelected);
				source.toggleHighlight(5, 6);
				language.nextStep();
				ruleMatrix.unhighlightColumn(count+1, null, null);
				
				lastHighlight = new Coordinates(column, row);
				
				source.unhighlight(6);
//				newBoard.unhighlightCell(row, column, null, DEFAULT_DURATION);
				newBoard.setBackgroundColor(row, column, backgroundColor);
				board.setBackgroundColor(row, column, backgroundColor);
				
				neighborsAlive.set(0);
				neighborsAlive.hide();
			}
		}
		
		//unhighlight last Cell
		if(lastHighlight != null){
//			board.unhighlightCell(lastHighlight.getY(), lastHighlight.getX(), null, DEFAULT_DURATION);
//			newBoard.unhighlightCell(lastHighlight.getY(), lastHighlight.getX(), null, DEFAULT_DURATION);
			board.setBackgroundColor(lastHighlight.getY(), lastHighlight.getX(), backgroundColor);
			newBoard.setBackgroundColor(lastHighlight.getY(), lastHighlight.getX(), backgroundColor);
		}
		
		//move new configuration to old target
		newBoard.moveTo("W", null, new Coordinates(50, yMatrix), null, DEFAULT_DURATION);
		
		//Delete content of old board
		setBoard(createStubData(board.getNrRows(), board.getNrCols()), board);
		board.hide();
		board.moveTo("E", null, new Coordinates(50 * board.getNrCols(), yMatrix), null, DEFAULT_DURATION);
		
		source.toggleHighlight(6,10);
		
		language.nextStep();
	}
	
	private boolean[][] boardToBoolean(StringMatrixExtended board){
		boolean[][] conf = new boolean[board.getNrRows()][board.getNrCols()];
		for(int row = 0; row < board.getNrRows() ; row++){
			for(int colon = 0 ; colon < board.getNrCols() ; colon++){
				conf[row][colon] = board.getElement(row, colon).equals(CELL_ALIVE_SYMBOL);
			}
		}
		return conf;
	}
	
	private boolean isAlive(StringMatrixExtended board){
		
		BoardConfiguration conf = new BoardConfiguration(boardToBoolean(board));
		
		return boardConfigurations.add(conf);
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
		
		backgroundColor = (Color) matrixProperties.get(AnimationPropertiesKeys.FILL_PROPERTY);
		backgroundColorHighlighted = (Color) matrixProperties.getItem("cellHighlight").get();
		// propertyNames = [fillColor, cellHighlight, depth, color, hidden, name, filled, style, align, elementColor, font, elemHighlight]
		
		int[] BirthRules = (int[])primitives.get("BirthRules");	
		
		Set<Integer> g = new HashSet<Integer>();
		for(int i : BirthRules)
			g.add(i);
		
		
        int[] DieRules = (int[])primitives.get("DieRules");
        
        Set<Integer> t = new HashSet<Integer>();
        for(int i : DieRules)
			t.add(i);

		String[][] gameBoard = (String[][])primitives.get("GameBoard");
	    StringMatrixExtended currentBoard = new StringMatrixExtended(language, new Coordinates(50, yMatrix), gameBoard, MATRIX, null, matrixProperties);
	    
		int maxSteps = (Integer) primitives.get("MaxIterations");
		
		
		String[][] rule = new String[3][10];
		
		rule[0] = new String[]{" ","0","1","2","3","4","5","6","7","8"};
		rule[1][0] = "G";
		rule[2][0] = "T";
		
		for(int i = 1; i < 10 ; i++){
			if(g.contains(i-1))
				rule[1][i] = "x";
			else
				rule[1][i] = " ";
			
			if(t.contains(i-1))
				rule[2][i] = "x";
			else
				rule[2][i] = " ";
			
		}
	
		
		currentBoard.hide();
		
		/* create the first slide */
		
		// show the header with a heading surrounded by a rectangle
		Font oldFont = (Font) titleProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(oldFont.getName(), Font.BOLD, 24));
	    Text header = language.newText(new Coordinates(40, 20), getName(), "header", null, titleProperties);
	    Rect headerRect = language.newRect(new Offset(-5, -5, "header",
	        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
	        null, titleBoxProperties);
	    
	    String description1[] = {
	    		"'Game of Life' ist ein von dem Mathematiker John Horton Conway",
				"entworfenes Spiel, welches sich spielerisch mit der Automatentheorie",
				"auseinander setzt. Der zellulaere Automat wird als beliebig große",
				"Tabelle dargestellt. Diese Tabelle bildet das Spielfeld von 'Game of ",
				"Life'. Jede Zelle bekommt entweder den Zustand 'lebendig' oder 'tot'",
				"zugewiesen. Die Folgegeneration ergibt sich durch die Befolgung",
				"einfacher Regeln, dem Zustand der Zelle selbst und dem Zustand",
				"der bis zu acht Nachbarzellen."};
	    
	    String[][] exampleRule = new String[][] {
	    		{" ","0","1","2","3","4","5","6","7","8"},
	    		{"G"," "," "," ","x"," "," "," "," "," "},
	    		{"T","x","x"," "," ","x","x","x","x","x"}};
	    
	    String description2[] = {
	    		"",
				"Conway verwendete folgende Regeln:",
				"Die folgende Belegung bedeutet, dass bei drei 'lebendigen' ",
				"Nachbarzellen eine tote Zelle lebendig wird und eine lebende Zelle",
				"bei keinem oder einem sowie bei vier bis acht lebendigen",
				"Nachbarzellen stirbt und ansonsten der Zustand einer Zelle",
				"unangetastet bleibt."};
		
	    // setup the start page with the description
//	    TextProperties descriptionProps = new TextProperties();
//	    descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//	        Font.SANS_SERIF, Font.PLAIN, 16));
	    Font oldFontDescription = (Font) textualDescriptionsProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
	    textualDescriptionsProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(oldFontDescription.getName(), Font.PLAIN, 16));
	    
	    language.newText(
	    	new Coordinates(40, 70),
	        description1[0],
	        "description1", null, textualDescriptionsProperties);
	    
	    int lineNumber;
	    for (lineNumber = 1; lineNumber < description1.length; lineNumber++) {
	    	language.newText(new Offset(0, 25, "description"+lineNumber, AnimalScript.DIRECTION_NW),
	    	        description1[lineNumber],
	    	        "description"+(lineNumber+1), null, textualDescriptionsProperties);
	    }	  
	    
	    /* create the next slide */
	    language.nextStep();
	    
	    language.newText(new Offset(0, 25, "description"+lineNumber, AnimalScript.DIRECTION_NW),
    	        description2[0],
    	        "description"+(lineNumber+1), null, textualDescriptionsProperties);
	    lineNumber++;
	    language.newText(new Offset(0, 25, "description"+lineNumber, AnimalScript.DIRECTION_NW),
    	        description2[1],
    	        "description"+(lineNumber+1), null, textualDescriptionsProperties);
	    lineNumber++;
	    
	    StringMatrixExtended exampleRuleMatrix = new StringMatrixExtended(language, new Offset(0, 40, "description"+lineNumber, AnimalScript.DIRECTION_NW), 
	    		exampleRule, "exampleRule", null, rulesProperties);
	    
	    lineNumber++;
	    
	    language.newText(new Offset(0, 25, "exampleRule", AnimalScript.DIRECTION_SW),
    	        description2[2],
    	        "description"+(lineNumber+1), null, textualDescriptionsProperties);
	    lineNumber++;
	    
	    for (int j = 3; j < description2.length; j++) {
	    	language.newText(new Offset(0, 25, "description"+lineNumber,
	    	        AnimalScript.DIRECTION_NW),
	    	        description2[j],
	    	        "description"+(lineNumber+1), null, textualDescriptionsProperties);
	    	lineNumber++;
	    }	    
	    
		/* create the next slide */
		language.nextStep();
		language.hideAllPrimitives();
		exampleRuleMatrix.hide();
	    header.show();
	    headerRect.show();
		currentBoard.show();
		StringMatrixExtended nextBoard = new StringMatrixExtended(
				language,
				new Coordinates(50 * currentBoard.getNrCols(), 100), createStubData(currentBoard.getNrRows(), 
				currentBoard.getNrCols()), "temp", null, matrixProperties);
		
		nextBoard.hide();
		// show pseudo code under the first matrix
		SourceCode source = showSourceCode(currentBoard, sourceCodeProperties);
		
		int x = 50 * currentBoard.getNrCols() * 2;
		if (x < 750)
			x = 750;
		accessCounter     = new IntVariable(language, new Coordinates(x, yMatrix),      "Access :",      "accessCounter" ,     0, ZERO_DURATION, statisticsProperties);
		assignmentCounter = new IntVariable(language, new Coordinates(x, yMatrix + 30), "Assignments :", "assignmentCounter" , 0, ZERO_DURATION, statisticsProperties);
		
		
		/* create the next slide */
		language.nextStep();
		
		source.highlight(0);
		
		@SuppressWarnings("unused")
		IntVariable maximalSteps = new IntVariable(language, new Coordinates(750, 50+50*currentBoard.getNrRows()), "maxSteps", "maxSteps", maxSteps, DEFAULT_DURATION, statisticsProperties);
		
		// show rules
		StringMatrixExtended ruleMatrix = new StringMatrixExtended(language, new Coordinates(750, 190+50*currentBoard.getNrRows()), 
	    		rule, "rule", null, rulesProperties);
		
		/* create the next slide */
		language.nextStep();
		
		steps = new IntVariable(language, new Coordinates(750, 80+50*currentBoard.getNrRows()), "steps", "steps", 0, ZERO_DURATION, statisticsProperties);
		
		neighborsAlive = new IntVariable(language, new Coordinates(750, 130+50*currentBoard.getNrRows()), "neighboursAlive", "neighboursAlive", 0, ZERO_DURATION, statisticsProperties);
		neighborsAlive.hide();
		
		source.toggleHighlight(0, 1);
		
		/* create the next slides */
		language.nextStep();
		
		for(int currentStep = 0; currentStep < maxSteps && isAlive(currentBoard); currentStep++){
			
			source.toggleHighlight(1, 2);
			nextBoard.show();
			//language.nextStep();
			
			calculateNewBoard(currentBoard, nextBoard, g ,t, source, currentStep, ruleMatrix);
			
			// exchange the board
			StringMatrixExtended temp = currentBoard;
			currentBoard = nextBoard;
			nextBoard = temp;
			steps.increment();
			source.unhighlight(10);
		}
		
		/* create the last slide */
		language.nextStep();
		language.hideAllPrimitives();
		exampleRuleMatrix.hide();
		currentBoard.hide();
		nextBoard.hide();
		ruleMatrix.hide();
		
	    header.show();
	    headerRect.show();
	    
	    List<String> conclusionList = getConclusion(steps.getIntegerValue(), maxSteps);
	    
	    language.newText(new Coordinates(40, 70),
	    		conclusionList.get(0),
    	        "conclusion1", null, textualDescriptionsProperties);
	    for (int k = 1; k < conclusionList.size(); k++) {
	    	language.newText(new Offset(0, 25, "conclusion"+k, AnimalScript.DIRECTION_NW),
	    			conclusionList.get(k),
	    	        "conclusion"+(k+1), null, textualDescriptionsProperties);
	    }

		return language.toString().replaceAll("refresh", "");
	}
	
	private void setBoard(String[][] data, StringMatrixExtended matrix){
		for(int row = 0; row < matrix.getNrRows() ; row++)
			for(int colon = 0; colon < matrix.getNrCols() ; colon++){
				matrix.put(row, colon, data[row][colon], null, DEFAULT_DURATION);
			}
	}
	
	private String[][] createStubData(int rows, int colunns){
		String[][] t = new String[rows][colunns];
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < colunns; j++)
				t[i][j] = "";
		return t;
	}

	@Override
	public String getAlgorithmName() {
		return "Game of Life";
	}
	
	@Override
	public String getName() {
		return "Game of Life";
	}

	@Override
	public String getAnimationAuthor() {
		return "Timo Baehr,Alexander Jandousek";
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
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public String getCodeExample() {
		String code = 
				  "public void gameOfLife(Board, maxsteps, rules) {\n"
				+ "	for (int steps = 0; steps < maxsteps && Board is a known Generation; steps++) {\n"+"Create NewBoard;\n"
				+ "		for (int row : Board.rows) {\n"
				+ "			for (int column : Board.columns) {\n"
				+ "				int neighborsAlive = getNeighborsAlive(Board, row, column);\n"
				+ "				NewBoard[row][column] = isAliveInNextGeneration(neighborsAlive, rules);\n"
				+ "			}\n"
				+ "		}\n"
				+ "	Board = NewBoard;\n"
				+ "	}\n"
				+ "}\n";
		return code;
	}
	

	@Override
	public String getDescription() {
		return    "\"Game of Life\" ist ein von dem Mathematiker John Horton Conway entworfenes Spiel, "
				+ "welches sich spielerisch mit der Automatentheorie auseinander setzt. Der zellulaere Automat "
				+ "wird als beliebig große Tabelle dargestellt. Diese Tabelle bildet das Spielfeld von \"Game of Life\". "
				+ "Jede Zelle bekommt entweder den Zustand \"lebendig\" "
				+ "oder \"tot\" zugewiesen. Die Folgegeneration ergibt sich durch die Befolgung "
				+ "einfacher Regeln, dem Zustand der Zelle selbst und dem Zustand der bis zu acht Nachbarzellen.\n\n"
				+ "Conway verwendete folgende Regeln:\t\n "
//				+ "\n"
//				+ "| |0|1|2|3|4|5|6|7|8|\n"
//				+ "|G| | | |x| | | | | |\n"
//				+ "|T|x|x| | |x|x|x|x| |\n"
//				+ "\n"
				+ "3/0145678\t "
				+ "Die folgende Belegung bedeutet, dass bei drei \"lebendigen\" Nachbarzellen eine tote Zelle "
				+ "lebendig wird und eine lebende Zelle bei keinem oder einem sowie bei vier bis acht lebendigen"
				+ "Nachbarzellen stirbt und ansonsten der Zustand einer Zelle unangetastet bleibt."
				+ "Im folgenden wird eine lebendige Zelle durch ein \"X\" dargestellt und eine tote Zelle durch ein \"O\".";
	}
	
	public List<String> getConclusion(int steps, int maxsteps) {
		List<String> conclusion = new ArrayList<String>();
		if (steps == 1) {
			conclusion.add("Ueberpruefen Sie Ihre Regeln oder die Ursprungsgeneration.");
			conclusion.add("Die erste Folgegeneration unterscheidet sich nicht von der Ursprungsgeneration.");
		} if (steps < maxsteps) {
			conclusion.add("Die "+steps+". Folgegeneration gleicht einer bereits dagewesenen Generation.");
		} else {
			conclusion.add("Die Hoechstanzahl an Schritten wurde erreicht, bevor die Folgegeneration");
			conclusion.add("einer bereits dagewesenen Generation gleicht.");
		}
		conclusion.add("");
		conclusion.add("Hintergrund zum Algorithmus: Wie anhand des Quellcodes erkennbar, wird stets in");
		conclusion.add("einem Schritt nur eine Zelle der Tabelle veraendert. Der Zustand dieser Zelle");
		conclusion.add("laesst sich unabhaengig von dem Zustand der anderen Zellen berechnen.");
		conclusion.add("Der Algorithmus eignet sich daher besonders gut dazu, parallelisiert zu werden.");
		
		return conclusion;
	}
	
	@Override
	public void init() {
		language = new AnimalScript("Game of Life", "Timo Bähr, Alexander Jandousek", 800, 600);
		language.setStepMode(true);
	}
	
	/**
	 * Show the sourcecode below the matrix. This also returns the source code.
	 */
	private SourceCode showSourceCode(StringMatrixExtended matrix, SourceCodeProperties sourceCodeProperties) {
		// now, create the source code entity
		SourceCode sc = language.newSourceCode(new Coordinates(50, 50 + (50 * matrix.getNrRows())), "sourceCode", null, sourceCodeProperties);
		
		// add a code line
		// parameters: code itself; name (can be null); indentation level; display options
		sc.addCodeLine("public void gameOfLife(Board, maxsteps, rules) {", null, 0, null);
		
		sc.addCodeLine("for (int steps = 0; steps < maxsteps && Board is a known Generation; steps++) {", null, 1, null);
		sc.addCodeLine("Create NewBoard;", null, 2, null);
		sc.addCodeLine("for (int row : Board.rows) {", null, 2, null);
		
		sc.addCodeLine("for (int column : Board.columns) {", null, 3, null);
		
		sc.addCodeLine("int neighborsAlive = getNeighborsAlive(Board, row, column);", null, 4, null);
		sc.addCodeLine("NewBoard[row][column] = isAliveInNextGeneration(neighborsAlive, rules);", null, 4, null);
		
		sc.addCodeLine("}", null, 3, null);
		
		sc.addCodeLine("}", null, 2, null);
		
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("Board = NewBoard;", null, 1, null);
		
		sc.addCodeLine("}", null, 0, null);
		
		return sc;
	}

    @Override
    public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        boolean error = true;

        StringBuffer errorMessage = new StringBuffer();

        int maxSteps = (Integer) primitives.get("MaxIterations");

        if(maxSteps < 1){
            errorMessage.append("The value of \"MaxIterations\" should be greater then 0 but was ");
            errorMessage.append(maxSteps);
            errorMessage.append(".\n");
            error = false;
        }

        String[][] gameBoard = (String[][])primitives.get("GameBoard");

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