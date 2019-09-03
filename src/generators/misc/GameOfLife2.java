package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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

/**
 * Generator for 'Game Of Life'.
 * 
 * @author Timo Baehr
 * @author Alexander Jandousek
 */
public class GameOfLife2 implements Generator {

  /** This counts the amount of accesses during a program */
  private IntVariable                   accessCounter;

  /** This counts the amount of assignments during a program */
  private IntVariable                   assignmentCounter;

  /** This counts the amount of generations in GAME OF LIFE */
  private IntVariable                   steps;

  /** This is the number of neighbors alive of the current cell */
  private IntVariable                   neighborsAlive;

  /**
   * The abstract Language class defines the basic methods for all particular
   * languages like AnimalScript for example, which then itselves provide
   * functionality for output management, a name registry for primitives and
   * factory methods for all supported primitives.
   */
  private Language                      language;

  private final int                     yMatrix             = 100;

  /* Notational conventions */

  private final String                  MATRIX              = "board";
  private final String                  CELL_ALIVE_SYMBOL   = "X";
  private final String                  CELL_DEAD_SYMBOL    = "O";

  private final Timing                  DEFAULT_DURATION    = new TicksTiming(
                                                                15);

  private final Timing                  ZERO_DURATION       = new TicksTiming(0);

  // Keep track of all previous configurations
  private final Set<BoardConfiguration> boardConfigurations = new HashSet<BoardConfiguration>();

  /** This stores a generation of GAME OF LIFE */
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

    BoardConfiguration(boolean[][] board) {
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

    private GameOfLife2 getOuterType() {
      return GameOfLife2.this;
    }

  }

  /* constructors for this GAME OF LIFE generator */

  // public GameOfLife(Language language){
  // this.language = language;
  // language.setStepMode(true);
  // }
  //
  // public GameOfLife(){
  // this(new AnimalScript("Game of Life [DE]",
  // "Timo Baehr, Alexander Jandousek", 640, 480));
  // language.setStepMode(true);
  // }

  /* methods of this class */

  private int getCellsAliveAround(StringMatrixExtended board, int row,
      int column) {
    boolean[][] b = boardToBoolean(board);
    int numberOfLivingCells = 0;

    List<Coordinates> highlighted = new LinkedList<Coordinates>();

    if ((row - 1) >= 0 && (row - 1) < b.length && (column - 1) >= 0
        && (column - 1) < b[row - 1].length) {

      Coordinates coordinate = new Coordinates(column - 1, row - 1);

      if (b[coordinate.getY()][coordinate.getX()]) {
        numberOfLivingCells++;
        neighborsAlive.increment();
      }

      // board.highlightElem(coordinate.getY(), coordinate.getX(), null,
      // DEFAULT_DURATION);
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(), Color.RED);
      accessCounter.increment();
      highlighted.add(coordinate);
      language.nextStep();

    }
    if ((row - 1) >= 0 && (row - 1) < b.length && (column) >= 0
        && (column) < b[row - 1].length) {

      Coordinates coordinate = new Coordinates(column, row - 1);

      if (b[coordinate.getY()][coordinate.getX()]) {
        numberOfLivingCells++;
        neighborsAlive.increment();
      }

      // board.highlightElem(coordinate.getY(), coordinate.getX(), null,
      // DEFAULT_DURATION);
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(), Color.RED);
      accessCounter.increment();
      highlighted.add(coordinate);
      language.nextStep();

    }
    if ((row - 1) >= 0 && (row - 1) < b.length && (column + 1) >= 0
        && (column + 1) < b[row - 1].length) {

      Coordinates coordinate = new Coordinates(column + 1, row - 1);

      if (b[coordinate.getY()][coordinate.getX()]) {
        numberOfLivingCells++;
        neighborsAlive.increment();
      }

      // board.highlightElem(coordinate.getY(), coordinate.getX(), null,
      // DEFAULT_DURATION);
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(), Color.RED);
      accessCounter.increment();
      highlighted.add(coordinate);
      language.nextStep();
    }
    if ((row) >= 0 && (row) < b.length && (column + 1) >= 0
        && (column + 1) < b[row].length) {

      Coordinates coordinate = new Coordinates(column + 1, row);

      if (b[coordinate.getY()][coordinate.getX()]) {
        numberOfLivingCells++;
        neighborsAlive.increment();
      }

      // board.highlightElem(coordinate.getY(), coordinate.getX(), null,
      // DEFAULT_DURATION);
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(), Color.RED);
      accessCounter.increment();
      highlighted.add(coordinate);
      language.nextStep();
    }
    if ((row + 1) >= 0 && (row + 1) < b.length && (column + 1) >= 0
        && (column + 1) < b[row + 1].length) {

      Coordinates coordinate = new Coordinates(column + 1, row + 1);

      if (b[coordinate.getY()][coordinate.getX()]) {
        numberOfLivingCells++;
        neighborsAlive.increment();
      }

      // board.highlightElem(coordinate.getY(), coordinate.getX(), null,
      // DEFAULT_DURATION);
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(), Color.RED);
      accessCounter.increment();
      highlighted.add(coordinate);
      language.nextStep();
    }
    if ((row + 1) >= 0 && (row + 1) < b.length && (column) >= 0
        && (column) < b[row + 1].length) {

      Coordinates coordinate = new Coordinates(column, row + 1);

      if (b[coordinate.getY()][coordinate.getX()]) {
        numberOfLivingCells++;
        neighborsAlive.increment();
      }

      // board.highlightElem(coordinate.getY(), coordinate.getX(), null,
      // DEFAULT_DURATION);
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(), Color.RED);
      accessCounter.increment();
      highlighted.add(coordinate);
      language.nextStep();
    }
    if ((row + 1) >= 0 && (row + 1) < b.length && (column - 1) >= 0
        && (column - 1) < b[row + 1].length) {

      Coordinates coordinate = new Coordinates(column - 1, row + 1);

      if (b[coordinate.getY()][coordinate.getX()]) {
        numberOfLivingCells++;
        neighborsAlive.increment();
      }

      // board.highlightElem(coordinate.getY(), coordinate.getX(), null,
      // DEFAULT_DURATION);
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(), Color.RED);
      accessCounter.increment();
      highlighted.add(coordinate);
      language.nextStep();
    }
    if ((row) >= 0 && (row) < b.length && (column - 1) >= 0
        && (column - 1) < b[row].length) {

      Coordinates coordinate = new Coordinates(column - 1, row);

      if (b[coordinate.getY()][coordinate.getX()]) {
        numberOfLivingCells++;
        neighborsAlive.increment();
      }

      // board.highlightElem(coordinate.getY(), coordinate.getX(), null,
      // DEFAULT_DURATION);
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(), Color.RED);
      accessCounter.increment();
      highlighted.add(coordinate);
      language.nextStep();
    }

    for (Coordinates coordinate : highlighted)
      board.setBackgroundColor(coordinate.getY(), coordinate.getX(),
          Color.WHITE);
    // board.unhighlightElem(coordinate.getY(), coordinate.getX(), null,
    // DEFAULT_DURATION);

    return numberOfLivingCells;
  }

  private void calculateNewBoard(StringMatrixExtended board,
      StringMatrixExtended newBoard, Set<Integer> g, Set<Integer> t,
      SourceCode source) {

    Coordinates lastHighlight = null;

    for (int row = 0; row < board.getNrRows(); row++) {

      source.toggleHighlight(2, 3);
      language.nextStep();

      for (int column = 0; column < board.getNrCols(); column++) {

        source.toggleHighlight(3, 4);
        language.nextStep();

        // highlight actual cell
        accessCounter.increment();

        // board.highlightCell(row, column, null, DEFAULT_DURATION);
        board.setBackgroundColor(row, column, Color.YELLOW);

        // unhighlight last cell
        // if(lastHighlight != null){
        // board.unhighlightCell(lastHighlight.getY(), lastHighlight.getX(),
        // null, DEFAULT_DURATION);
        // //newBoard.unhighlightCell(lastHighlight.getY(),
        // lastHighlight.getX(), null, DEFAULT_DURATION);
        // }

        source.toggleHighlight(4, 5);
        neighborsAlive.show();
        language.nextStep(); // highlight Matrix

        Integer count = getCellsAliveAround(board, row, column);

        if (board.getElement(row, column).equals(CELL_ALIVE_SYMBOL)) {

          if (t.contains(count))
            newBoard.put(row, column, CELL_DEAD_SYMBOL, null, DEFAULT_DURATION);
          else
            newBoard
                .put(row, column, CELL_ALIVE_SYMBOL, null, DEFAULT_DURATION);

        } else {

          if (g.contains(count))
            newBoard
                .put(row, column, CELL_ALIVE_SYMBOL, null, DEFAULT_DURATION);
          else
            newBoard.put(row, column, CELL_DEAD_SYMBOL, null, DEFAULT_DURATION);
        }

        assignmentCounter.increment();

        // newBoard.highlightCell(row, column, null, DEFAULT_DURATION);
        newBoard.setBackgroundColor(row, column, Color.YELLOW);
        source.toggleHighlight(5, 6);
        language.nextStep();

        lastHighlight = new Coordinates(column, row);

        source.unhighlight(6);
        // newBoard.unhighlightCell(row, column, null, DEFAULT_DURATION);
        newBoard.setBackgroundColor(row, column, Color.WHITE);
        board.setBackgroundColor(row, column, Color.WHITE);

        neighborsAlive.set(0);
        neighborsAlive.hide();
      }
    }

    // unhighlight last Cell
    if (lastHighlight != null) {
      // board.unhighlightCell(lastHighlight.getY(), lastHighlight.getX(), null,
      // DEFAULT_DURATION);
      // newBoard.unhighlightCell(lastHighlight.getY(), lastHighlight.getX(),
      // null, DEFAULT_DURATION);
      board.setBackgroundColor(lastHighlight.getY(), lastHighlight.getX(),
          Color.WHITE);
      newBoard.setBackgroundColor(lastHighlight.getY(), lastHighlight.getX(),
          Color.WHITE);
    }

    // move new configuration to old target
    newBoard.moveTo("W", null, new Coordinates(50, yMatrix), null,
        DEFAULT_DURATION);

    // Delete content of old board
    setBoard(createStubData(board.getNrRows(), board.getNrCols()), board);
    board.hide();
    board.moveTo("E", null, new Coordinates(50 * board.getNrCols(), yMatrix),
        null, DEFAULT_DURATION);

    source.toggleHighlight(6, 10);

    language.nextStep();
  }

  private boolean[][] boardToBoolean(StringMatrixExtended board) {
    boolean[][] conf = new boolean[board.getNrRows()][board.getNrCols()];
    for (int row = 0; row < board.getNrRows(); row++) {
      for (int colon = 0; colon < board.getNrCols(); colon++) {
        conf[row][colon] = board.getElement(row, colon).equals(
            CELL_ALIVE_SYMBOL);
      }
    }
    return conf;
  }

  private boolean isAlive(StringMatrixExtended board) {

    BoardConfiguration conf = new BoardConfiguration(boardToBoolean(board));

    return boardConfigurations.add(conf);
  }

  @Override
  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {

    MatrixProperties matrixProperties = (MatrixProperties) properties
        .getPropertiesByName("matrix");
    TextProperties textProperties = (TextProperties) properties
        .getPropertiesByName("text");

    int[] BirthRules = (int[]) primitives.get("BirthRules");

    Set<Integer> g = new HashSet<Integer>();
    for (int i : BirthRules)
      g.add(i);

    int[] DieRules = (int[]) primitives.get("DieRules");

    Set<Integer> t = new HashSet<Integer>();
    for (int i : DieRules)
      t.add(i);

    // StringMatrix currentBoard = (StringMatrix) primitives.get(MATRIX);

    String[][] gameBoard = (String[][]) primitives.get("GameBoard");
    StringMatrixExtended currentBoard = new StringMatrixExtended(language,
        new Coordinates(50, yMatrix), gameBoard, MATRIX, null, matrixProperties);

    // for(int row = 0 ; row < currentBoard.getNrRows() ; row++)
    // for(int col = 0 ; col < currentBoard.getNrCols() ; col++)
    // if(currentBoard.getElement(row, col).equals(CELL_ALIVE_SYMBOL)){
    // currentBoard.setTextColor(row, col, Color.BLACK);
    // currentBoard.setBackgroundColor(row, col, Color.BLACK);
    // } else {
    // currentBoard.setTextColor(row, col, Color.WHITE);
    // currentBoard.setBackgroundColor(row, col, Color.WHITE);
    // }

    int maxSteps = (Integer) primitives.get("maxIterations");

    String[][] rule = new String[3][10];

    rule[0] = new String[] { " ", "0", "1", "2", "3", "4", "5", "6", "7", "8" };
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

    currentBoard.hide();

    /* create the first slide */

    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    Text header = language.newText(new Coordinates(40, 20), getName(),
        "header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect headerRect = language.newRect(new Offset(-5, -5, "header",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
        null, rectProps);

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

    String description2[] = { "", "Conway verwendete folgende Regeln:",
        "Die folgende Belegung bedeutet, dass bei drei 'lebendigen' ",
        "Nachbarzellen eine tote Zelle lebendig wird und eine lebende Zelle",
        "bei keinem oder einem sowie bei vier bis acht lebendigen",
        "Nachbarzellen stirbt und ansonsten der Zustand einer Zelle",
        "unangetastet bleibt." };

    // setup the start page with the description
    TextProperties descriptionProps = new TextProperties();
    descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));

    language.newText(new Coordinates(40, 70), description1[0], "description1",
        null, descriptionProps);

    int lineNumber;
    for (lineNumber = 1; lineNumber < description1.length; lineNumber++) {
      language.newText(new Offset(0, 25, "description" + lineNumber,
          AnimalScript.DIRECTION_NW), description1[lineNumber], "description"
          + (lineNumber + 1), null, descriptionProps);
    }

    /* create the next slide */
    language.nextStep();

    language.newText(new Offset(0, 25, "description" + lineNumber,
        AnimalScript.DIRECTION_NW), description2[0], "description"
        + (lineNumber + 1), null, descriptionProps);
    lineNumber++;
    language.newText(new Offset(0, 25, "description" + lineNumber,
        AnimalScript.DIRECTION_NW), description2[1], "description"
        + (lineNumber + 1), null, descriptionProps);
    lineNumber++;

    StringMatrixExtended exampleRuleMatrix = new StringMatrixExtended(
        language,
        new Offset(0, 40, "description" + lineNumber, AnimalScript.DIRECTION_NW),
        exampleRule, "exampleRule", null, matrixProperties);

    lineNumber++;

    language.newText(
        new Offset(0, 25, "exampleRule", AnimalScript.DIRECTION_SW),
        description2[2], "description" + (lineNumber + 1), null,
        descriptionProps);
    lineNumber++;

    for (int j = 3; j < description2.length; j++) {
      language.newText(new Offset(0, 25, "description" + lineNumber,
          AnimalScript.DIRECTION_NW), description2[j], "description"
          + (lineNumber + 1), null, descriptionProps);
      lineNumber++;
    }

    /* create the next slide */
    language.nextStep();
    language.hideAllPrimitives();
    exampleRuleMatrix.hide();
    header.show();
    headerRect.show();
    currentBoard.show();
    StringMatrixExtended nextBoard = new StringMatrixExtended(language,
        new Coordinates(50 * currentBoard.getNrCols(), 100), createStubData(
            currentBoard.getNrRows(), currentBoard.getNrCols()), "temp", null,
        matrixProperties);

    nextBoard.hide();
    // show pseudo code under the first matrix
    SourceCode source = showSourceCode(currentBoard);

    int x = 50 * currentBoard.getNrCols() * 2;
    if (x < 750)
      x = 750;
    accessCounter = new IntVariable(language, new Coordinates(x, yMatrix),
        "Access :", "accessCounter", 0, ZERO_DURATION, textProperties);
    assignmentCounter = new IntVariable(language, new Coordinates(x,
        yMatrix + 30), "Assignments :", "assignmentCounter", 0, ZERO_DURATION,
        textProperties);

    /* create the next slide */
    language.nextStep();

    source.highlight(0);

    @SuppressWarnings("unused")
    IntVariable maximalSteps = new IntVariable(language, new Coordinates(750,
        50 + 50 * currentBoard.getNrRows()), "maxSteps", "maxSteps", maxSteps,
        DEFAULT_DURATION, textProperties);

    // show rules
    StringMatrixExtended ruleMatrix = new StringMatrixExtended(language,
        new Coordinates(750, 190 + 50 * currentBoard.getNrRows()), rule,
        "rule", null, matrixProperties);

    /* create the next slide */
    language.nextStep();

    steps = new IntVariable(language, new Coordinates(750,
        80 + 50 * currentBoard.getNrRows()), "steps", "steps", 0,
        ZERO_DURATION, textProperties);

    neighborsAlive = new IntVariable(language, new Coordinates(750,
        130 + 50 * currentBoard.getNrRows()), "neighboursAlive",
        "neighboursAlive", 0, ZERO_DURATION, textProperties);
    neighborsAlive.hide();

    source.toggleHighlight(0, 1);

    /* create the next slides */
    language.nextStep();

    for (int currentStep = 0; currentStep < maxSteps && isAlive(currentBoard); currentStep++) {

      source.toggleHighlight(1, 2);
      nextBoard.show();
      language.nextStep();

      calculateNewBoard(currentBoard, nextBoard, g, t, source);

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

    List<String> conclusionList = getConclusion(steps.getIntegerValue(),
        maxSteps);

    // setup the last slide with the conclusion
    TextProperties conclusionProps = new TextProperties();
    conclusionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));

    language.newText(new Coordinates(40, 70), conclusionList.get(0),
        "conclusion1", null, conclusionProps);
    for (int k = 1; k < conclusionList.size(); k++) {
      language.newText(new Offset(0, 25, "conclusion" + k,
          AnimalScript.DIRECTION_NW), conclusionList.get(k), "conclusion"
          + (k + 1), null, conclusionProps);
    }

    return language.toString().replaceAll("refresh", "");
  }

  private void setBoard(String[][] data, StringMatrixExtended matrix) {
    for (int row = 0; row < matrix.getNrRows(); row++)
      for (int colon = 0; colon < matrix.getNrCols(); colon++) {
        matrix.put(row, colon, data[row][colon], null, DEFAULT_DURATION);
      }
  }

  private String[][] createStubData(int rows, int colunns) {
    String[][] t = new String[rows][colunns];
    for (int i = 0; i < rows; i++)
      for (int j = 0; j < colunns; j++)
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
    return "Timo Bähr, Alexander Jandousek";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
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
    String code = "public void gameOfLife(Board, maxsteps, rules) {\n"
        + "	for (int steps = 0; steps < maxsteps && Board is a known Generation; steps++) {\n"
        + "Create NewBoard;\n"
        + "		for (int row : Board.rows) {\n"
        + "			for (int column : Board.columns) {\n"
        + "				int neighborsAlive = getNeighborsAlive(Board, row, column);\n"
        + "				NewBoard[row][column] = isAliveInNextGeneration(neighborsAlive, rules);\n"
        + "			}\n" + "		}\n" + "	Board = NewBoard;\n" + "	}\n" + "}\n";
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
        + "Im folgenden wird eine lebendige Zelle durch ein \"X\" dargestellt und eine tote Zelle durch ein \"O\".";
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
      conclusion.add("Die " + steps
          + ". Folgegeneration gleicht einer bereits dagewesenen Generation.");
    } else {
      conclusion
          .add("Die Hoechstanzahl an Schritten wurde erreicht, bevor die Folgegeneration");
      conclusion.add("einer bereits dagewesenen Generation gleicht.");
    }
    conclusion.add("");
    conclusion
        .add("Hintergrund zum Algorithmus: Wie anhand des Quellcodes erkennbar, wird stets in");
    conclusion
        .add("einem Schritt nur eine Zelle der Tabelle veraendert. Der Zustand dieser Zelle");
    conclusion
        .add("laesst sich unabhaengig von dem Zustand der anderen Zellen berechnen.");
    conclusion
        .add("Der Algorithmus eignet sich daher besonders gut dazu, parallelisiert zu werden.");

    return conclusion;
  }

  @Override
  public void init() {
    language = new AnimalScript("Game of Life",
        "Timo Bähr, Alexander Jandousek", 800, 600);
    language.setStepMode(true);
  }

  /**
   * Show the sourcecode below the matrix. This also returns the source code.
   */
  private SourceCode showSourceCode(StringMatrixExtended matrix) {
    // first, set the visual properties for the source code
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // now, create the source code entity
    SourceCode sc = language.newSourceCode(new Coordinates(50,
        50 + (50 * matrix.getNrRows())), "sourceCode", null, scProps);

    // add a code line
    // parameters: code itself; name (can be null); indentation level; display
    // options
    sc.addCodeLine("public void gameOfLife(Board, maxsteps, rules) {", null, 0,
        null);

    sc.addCodeLine(
        "for (int steps = 0; steps < maxsteps && Board is a known Generation; steps++) {",
        null, 1, null);
    sc.addCodeLine("Create NewBoard;", null, 2, null);
    sc.addCodeLine("for (int row : Board.rows) {", null, 2, null);

    sc.addCodeLine("for (int column : Board.columns) {", null, 3, null);

    sc.addCodeLine(
        "int neighborsAlive = getNeighborsAlive(Board, row, column);", null, 4,
        null);
    sc.addCodeLine(
        "NewBoard[row][column] = isAliveInNextGeneration(neighborsAlive, rules);",
        null, 4, null);

    sc.addCodeLine("}", null, 3, null);

    sc.addCodeLine("}", null, 2, null);

    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("Board = NewBoard;", null, 1, null);

    sc.addCodeLine("}", null, 0, null);

    return sc;
  }
}
