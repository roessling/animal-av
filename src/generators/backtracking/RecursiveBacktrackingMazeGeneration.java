/*
 * RecursiveBacktrackingMazeGeneration.java
 * David Berman, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.backtracking;

// Generator imports
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

//Java imports
import java.awt.Color;
import java.awt.Font;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Stack;

import javax.swing.JOptionPane;

// AlgoAnim Imports
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.animator.TrueFalseQuestionAction;

/**
 * This class is the maze generation. Here we set everything up and create a path
 * with the recursive backtracking method.
 * @author David Berman
 *
 */
public class RecursiveBacktrackingMazeGeneration implements ValidatingGenerator {
	
	private Language lang; // Animal Language pack
	
	// =====================================================================
	// Variables
	// =====================================================================

	protected int BOUND_X, BOUND_Y; // Boundaries of the maze
	private int x = 50, y = 100;
	
	/* data structures */
	private Stack<Cell> cellStack;
	private Stack<Wall> wallStack; 
	private ArrayList<Cell> unvisitedCells; 
	private ArrayList<Cell> collection; 
	private Cell start, end, current; 
	private Wall currentWall; 
	private StringMatrix supportM; 
	private StringMatrix colorM; 
	private StackVisualization visualStack; 
	
	/* content */
	private DisplayContent dpc;
	private CreateContent cct;

	// =====================================================================
	// Maze Settings: Primitives
	// =====================================================================
	
	// CELL_WIDTH & CELL_HEIGHT. By DEFAULT 80 & 9
	private int cellSize;
	private int wallSize;

	// the amount of cells in a row/col. By DEFAULT 4x4
	private int numOfCellsRow;
	private int numOfCellsCol;

	// =====================================================================
	// Maze Settings: Properties
	// =====================================================================
	
	private RectProperties unvisited_Cell;
    private RectProperties chosen_Cell;
    private RectProperties current_Cell;
    private RectProperties visited_Cell;
    private RectProperties neighbor_Cells;
    
	// =====================================================================
	// Maze Settings (additional)
	// =====================================================================

    // The total length of a row/col is calculated by adding the cell & wall 
	// amount together.
	private int totalRowLength;
	private int totalColLength;
	private int pxlSizeError = 1; // animal dependent
	private int factor = 66; // for a proportional spacing in the animation panel
	
	// These are the lower corner coordinates of the maze background board
	private int baseOffsetX;
	private int baseOffsetY;
	
	// =====================================================================
	// Fancy Boxes
	// =====================================================================
	
	private int[] introBox;
	private int[] guideBox;
	private int[] codeBox1;
	private int[] codeBox2;
	private int[] outroBox;
	
	// =====================================================================
	// Coordinates
	// =====================================================================
	
	// Global Coordinates start, title & upperLeftSupportMatrix
	private Coordinates startPos = new Coordinates(x, y);
	private Coordinates titlePos = new Coordinates(50, 25);
	private Coordinates upperLeftSupportMatrix;
	
	// various title positions
	private Offset introTitlePos;
	private Offset guideTitlePos; 
	private Offset supMxTitlePos;
	private Offset chartTitlePos;
	private Offset stackTitlePos;
	private Offset lifo_TitlePos;
	private Offset pCodeTitlePos;
	private Offset sCodeTitlePos;
	private Offset outroTitlePos;
	
	// various content positions
	private Offset introduct_Pos; 
	private Offset guidanceC_Pos;
	private Offset iva_Stack_Pos;
	private Offset pseudoCodePos;
	private Offset sourceCodePos;
	private Offset conclusionPos;
	
	// =====================================================================
	// Rectangles
	// =====================================================================
	
	private Rect base = null; 
	private Rect pcodeArea = null;
	private Rect scodeArea = null;
	private Rect guideArea = null;
	private Rect pTitleFrame = null;
	private Rect sTitleFrame = null;
	private Rect gTitleFrame = null;
	private Rect introArea = null;
	private Rect introFrame = null;
	private Rect outroArea = null;
	private Rect outroFrame = null;

	// =====================================================================
	// Colors 
	// =====================================================================
	
	// Custom fancy box colors
	private final Color coral = new Color(255,127,80);
	private final Color dark_paleturquoise = new Color(131, 214, 215);
	private final Color lightGray = new Color(223, 226, 231);
	
	//Custom Colors (Default)
	private Color unvisited_Cell_Color;
	private Color chosen_Cell_Color;
	private Color current_Cell_Color;
	private Color visited_Cell_Color;
	private Color neighbor_Cells_Color;
		
	// =====================================================================
	// Pseudo & Source 
	// =====================================================================
	
	private SourceCode psdoCode;
	private SourceCode srcCode;

	/**
	 * This method initializes the animal language.
	 */
    public void init(){
        lang = new AnimalScript("Recursive Backtracking Maze Generation", "David Berman", 2560, 1440); // TODO change res
        lang.setStepMode(true);
    }

    /**
     * This method catches user inputs? & replaces the java main class.
     */
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
    	lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION); // quiz start
    	
    	// catch Primitives
    	cellSize = 		(Integer)primitives.get("cellSize");
    	wallSize = 		(Integer)primitives.get("wallSize");
    	numOfCellsRow = (Integer)primitives.get("numOfCellsRow");
    	numOfCellsCol = (Integer)primitives.get("numOfCellsCol");

    	// catch Properties
    	unvisited_Cell = 	(RectProperties)props.getPropertiesByName("unvisited_Cell");
        chosen_Cell = 		(RectProperties)props.getPropertiesByName("chosen_Cell");
        current_Cell = 		(RectProperties)props.getPropertiesByName("current_Cell");
        visited_Cell = 		(RectProperties)props.getPropertiesByName("visited_Cell");
        neighbor_Cells = 	(RectProperties)props.getPropertiesByName("neighbor_Cells");
        
        this.calculations(); 	// Ready? - calculate all positions
        this.setColors(); 		// Set.   - pass colors through
        
        this.setup(); 			// Here we go.
       
        lang.finalizeGeneration(); // quiz end
        return lang.toString();
    }
    
    /**
     * This method handles all necessary positioning calculations.
     */
    public void calculations() {
    	
    	// total row/col length calculation
    	totalRowLength = numOfCellsRow + (numOfCellsRow - 1);
    	totalColLength = numOfCellsCol + (numOfCellsCol - 1);
    	
    	// background coordinates calculation
    	baseOffsetX = (cellSize * numOfCellsRow) + (wallSize * (numOfCellsRow + 1));
    	baseOffsetY = (cellSize * numOfCellsCol) + (wallSize * (numOfCellsCol + 1))
    										+ (((numOfCellsCol * 2) * pxlSizeError) - 2);
    	    	
    	// fancy boxes calculation
    	introBox = 	new int[]{0, 25, 580, 420}; 
    	guideBox =  new int[]{baseOffsetX + (factor * 2) + (numOfCellsRow * 38) - pxlSizeError , 25, 580, 248};
    	codeBox1 = 	new int[]{0, 	baseOffsetY + (factor * 3) + 15, 580, 420};
    	codeBox2 = 	new int[]{648, 	baseOffsetY + (factor * 3) + 15, 580, 420};
    	outroBox = 	new int[]{baseOffsetX + factor, 25, 580, 300};
    	
    	
    	// upper left coordinate of the support matrix
    	upperLeftSupportMatrix = new Coordinates(x + baseOffsetX + factor, y + 26);
    	
    	/* Offset calculation based on the global coordinates */
    	
    	// various title position calculation
    	introTitlePos = new Offset(10, 75, titlePos, AnimalScript.DIRECTION_S);
    	guideTitlePos = new Offset(guideBox[0] + 10, 0, startPos, AnimalScript.DIRECTION_S); 
    	supMxTitlePos = new Offset(0, -26, upperLeftSupportMatrix, AnimalScript.DIRECTION_S);
    	chartTitlePos = new Offset(0, 	(numOfCellsCol * 33), upperLeftSupportMatrix, AnimalScript.DIRECTION_S);
    	stackTitlePos = new Offset(0,	baseOffsetY + (factor/2), startPos, AnimalScript.DIRECTION_S);
    	lifo_TitlePos = new Offset(75, 	baseOffsetY + (factor/2) + 20, startPos, AnimalScript.DIRECTION_S);
    	pCodeTitlePos = new Offset(10, 	baseOffsetY + (factor * 3) - 10, startPos, AnimalScript.DIRECTION_S);
    	sCodeTitlePos = new Offset(658, 	baseOffsetY + (factor * 3) - 10, startPos, AnimalScript.DIRECTION_S);
    	outroTitlePos = new Offset(outroBox[0] + 10, 75, titlePos, AnimalScript.DIRECTION_S);
    	
    	// various content position calculation
    	introduct_Pos = new Offset(20, 100, titlePos, AnimalScript.DIRECTION_S); 
    	guidanceC_Pos = new Offset((numOfCellsRow * 38) + factor + 25, 0, upperLeftSupportMatrix, AnimalScript.DIRECTION_S);
    	iva_Stack_Pos = new Offset(0, 	baseOffsetY + (factor * 2) - 15, startPos, AnimalScript.DIRECTION_SE);
    	pseudoCodePos = new Offset(30, 	baseOffsetY + (factor * 3) + 15, startPos, AnimalScript.DIRECTION_SW);
    	sourceCodePos = new Offset(688, 	baseOffsetY + (factor * 3) + 15, startPos, AnimalScript.DIRECTION_S);
    	conclusionPos = new Offset(baseOffsetX + factor + 25, 100, titlePos, AnimalScript.DIRECTION_S);
    }
    
    /**
     * This method assigns the user picked colors.
     */
    public void setColors () {
    	this.unvisited_Cell_Color = 	(Color) unvisited_Cell.getItem("fillColor").get();
    	this.chosen_Cell_Color = 		(Color) chosen_Cell.getItem("fillColor").get();
    	this.current_Cell_Color = 		(Color) current_Cell.getItem("fillColor").get();
    	this.visited_Cell_Color = 		(Color) visited_Cell.getItem("fillColor").get();
    	this.neighbor_Cells_Color = 	(Color) neighbor_Cells.getItem("fillColor").get();
    }
    
    /**
     * This method prevents some illegal primitive values. 
     * Affected primitives: (cellSize, wallSize, numOfCellsRow, numOfCellsCol)
     */
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, 
			Hashtable<String, Object> primitives) throws IllegalArgumentException {
		
		int[] primitiveCollection = (int[]) new int[] {
				(int)primitives.get("cellSize"), (int)primitives.get("wallSize"),
				(int)primitives.get("numOfCellsRow"), (int)primitives.get("numOfCellsCol")
				};
			
		if((primitiveCollection[0] < 5) || (primitiveCollection[0] > 100)) { // cellSize
			JOptionPane.showMessageDialog(null, "Size of a cell out of allowed range (5-100)!",
					"Illegal cell size", JOptionPane.ERROR_MESSAGE);
			return false; // not good
		}
		if((primitiveCollection[1] < 5) || (primitiveCollection[1] > 50)) { // wallSize
			JOptionPane.showMessageDialog(null, "Size of a wall out of allowed range (5-50)!",
					"Illegal wall size", JOptionPane.ERROR_MESSAGE);
			return false; // not good
		}
		if((primitiveCollection[2] < 2) || (primitiveCollection[2] > 6)) { // numOfCellsRow
			JOptionPane.showMessageDialog(null, "Amount of cells in a row out of allowed range (2-6)!",
					"Illegal row length", JOptionPane.ERROR_MESSAGE);
			return false; // not good
		}
		if((primitiveCollection[3] < 2) || (primitiveCollection[3] > 6)) { // numOfCellsCol
			JOptionPane.showMessageDialog(null, "Amount of cells in a column out of allowed range (2-6)!",
					"Illegal column length", JOptionPane.ERROR_MESSAGE);
			return false; // not good
		}

		// default case; all good.
		return true;
	}
	
	// =====================================================================
	// Quiz - (3 Questions)
	// =====================================================================
	
	/**
	 * This method is a question about backtracking in a maze.
	 */
	public void quizQuestionOne() {
		
		// True/False Choice - 1 answer correct
		TrueFalseQuestionModel backtrackQ = new TrueFalseQuestionModel("backtrackingQuestion", true, 1);
		
		// Question prompt
		backtrackQ.setPrompt("While exploring, Is it possible to visited all cells without "
				+ "ever backtracking once? (Apart from backtracking always to the starting point)");
		
		// First choice
		backtrackQ.setFeedbackForAnswer(true, "This is correct, well done. "
				+ "This means that the maze generated only one dead end.");
		
		// Second choice
		backtrackQ.setFeedbackForAnswer(false, "This is not right. For example: "
				+ "Start at a corner cell and follow the edge to the inner part of the maze. "
				+ "This will create one dead end, which means the algorithm needs to backtrack only once. "
				+ "And that is the excluded returning path to the starting point.");

		
		backtrackQ.setCorrectAnswer(true);
		
		lang.addTFQuestion(backtrackQ);
	}
	
	/**
	 * This method is a question about the stack principle
	 */
	public void quizQuestionTwo() {
		
		// Multiple Choice - 1 answer correct
		MultipleChoiceQuestionModel stackQ = new MultipleChoiceQuestionModel("stackPrincipleQuestion");
		// Question prompt
		stackQ.setPrompt("After which principle organizes a stack its elements?");
		
		// Possible answers
		// FIFO
		stackQ.addAnswer("FIFO - (first in, first out)", 0, "This is not right. "
				+ "FIFO models the exact opposite behavior of a stack. "
				+ "Here the oldest element in the data structure will be processed first.");
		
		// LIFO
		stackQ.addAnswer("LIFO - (last in, first out)", 1, "This is correct, well done.");
		
		// FCFS
		stackQ.addAnswer("FCFS - (first-come, first-served)", 0, "This is not right. "
				+ "Similar behavior like FIFO with the only difference that the data structure is a queue. "
				+ "For example: people exit the queue in the order in which they arrive.");
		
		// HIFO
		stackQ.addAnswer("HIFO - (highest in, first out)", 0, "This is not right. "
				+ "Here the first element in the data structure will be processed first, "
				+ "if it has the highest value of all other.");
		
		// LOFO
		stackQ.addAnswer("LOFO - (lowest in, first out)", 0 , "This is not right. "
				+ "Here the first element in the data structure will be processed first, "
				+ "if it has the lowest value of all other.");
		
		lang.addMCQuestion(stackQ);
	}
	
	/**
	 * This method is a question about backtracking and stack organization in general.
	 */
	public void quizQuestionThree() {
		
		// MultipleSelectionModel - 2 answers correct
		MultipleSelectionQuestionModel path_stackQ = new MultipleSelectionQuestionModel("path&stackQuestion");
		
		// Question prompt
		path_stackQ.setPrompt("Can the stack potentially overfill? And will recursive backtracking always find a solution?");
		
		// Choice - stack cannot overfill
		path_stackQ.addAnswer("No, the stack can never overfill.", 1, "This is correct, well done.");
		
		// Choice - backtracking generates a path loop
		path_stackQ.addAnswer("No, recursive backtracking could generate a path loop and cannot return "
				+ "anymore to the starting point.", 0, "This is not right.");
		
		// Choice - stack can overfill
		path_stackQ.addAnswer("Yes, the stack could overfill if the maze needs to visit an already visited cell, "
				+ "because behind there is an unexplored path.", 0, "This is not right.");
		
		// Choice - backtracking finds a solution
		path_stackQ.addAnswer("Yes, recursive backtracking will always find a solution, "
				+ "because a maze has no unreachable areas and no path loops.", 1,
				"This is correct, well done.");
		
		
		lang.addMSQuestion(path_stackQ);
	
	}
    
	/**
	 * The constructor of the maze. Calls the init method of the generator.
	 */
	public RecursiveBacktrackingMazeGeneration() {
		this.init();
	}
 	
	/**
	 * The constructor of the maze. Assigns the animal language and
	 * activates Stepper Mode for controlling each animation step.
	 * 
	 * (Used for creating an animal script code in the java console.)
	 * @param lang the animal language
	 */
	public RecursiveBacktrackingMazeGeneration(Language lang) {
		this.lang = lang; 
		// Stepper Mode
		lang.setStepMode(true);
	}
	

    	
	/**
	 * This method initializes all components like the stacks
	 * for cells, walls and also sets the correct maze boundaries.
	 */
	public void initializeComponents() {
		setBOUND_X(totalRowLength); 
		setBOUND_Y(totalColLength); 
		
		this.cellStack = new Stack<Cell>();
		this.wallStack = new Stack<Wall>();
		this.collection = new ArrayList<Cell>();
		this.unvisitedCells = new ArrayList<Cell>();
	
	}
	
	/**
	 * This method shows the stack during the animation.
	 */
	public void showStack() {
		this.visualStack = new StackVisualization(iva_Stack_Pos, numOfCellsRow, 
				numOfCellsCol, current_Cell_Color, visited_Cell_Color);
	}
	
	// =====================================================================
	// Maze Core & Heart
	// ===================================================================== 
	
	/**
	 * The recursive Backtracking method explore.
	 * Responsible for traversing the uncharted maze,
	 * finding a path to carve & highlighting the corresponding
	 * Pseudo-/Source Code lines in the animation panel.
	 */
	public void explore() {
		
		current = unvisitedCells.remove(getRandomInt(unvisitedCells.size()));
		current.setState(State.VISITED);
		end = null;
		start = current;
		
		// Highlights
		psdoCode.highlight(0);
		srcCode.highlight(1);
		srcCode.highlight(2);
		
		start.currentCell(current_Cell);
		cct.highlightM(start.getYCoord(), start.getXCoord(), current_Cell_Color); 
		lang.nextStep(); // next Step
		
		this.quizQuestionOne(); // first quiz question
		
		while(!this.unvisitedCells.isEmpty()) {
			
			if(unvisitedCells.size() == 1) {
				end = unvisitedCells.get(0);
			}
			
			// Highlights
			psdoCode.toggleHighlight(0, 1);
			srcCode.unhighlight(1);
			srcCode.unhighlight(2);
			srcCode.highlight(3);
			
			Cell unvisited = this.chooseRandomUnvisitedNeighbor(current);
			
			if(unvisited != null) {
				
				this.showAllRandomUnvisitedNeighbors(current, neighbor_Cells); 
				
				// Highlights
				psdoCode.highlight(2);
				srcCode.highlight(4);
				srcCode.highlight(5);
				lang.nextStep();
				
				this.showAllRandomUnvisitedNeighbors(current, unvisited_Cell); 
				unvisited.chosenCell(chosen_Cell);
				
				// Highlights
				psdoCode.highlight(3);
				
				lang.nextStep(); // next Step
				
				// Highlights
				psdoCode.toggleHighlight(3,4);
				srcCode.toggleHighlight(4,6);
				
				this.cellStack.push(current);
				this.visualStack.pushIn(current.getID());
				
				cct.highlightM(current.getYCoord(), current.getXCoord(), current_Cell_Color); 
				
				lang.nextStep(); // next Step
				
				// Highlights
				psdoCode.toggleHighlight(4,5);
				srcCode.toggleHighlight(6,7);
				srcCode.highlight(8);
	
				currentWall = this.betweenWall(current, unvisited);
				currentWall.removeWall(current, unvisited);
				
				lang.nextStep(); // next Step
				
				cct.highlightM(current.getYCoord(), current.getXCoord(), visited_Cell_Color);
				this.visualStack.highlight(visited_Cell_Color);
				
				// Un-Highlights
				psdoCode.toggleHighlight(5,6);
				srcCode.unhighlight(7);
				srcCode.unhighlight(8);
				
				// Highlights
				srcCode.highlight(9);
				srcCode.highlight(10);
				srcCode.highlight(11);
				
				currentWall.traceCell(visited_Cell);
				this.wallStack.push(currentWall);
				current.traceCell(visited_Cell);

				current = unvisited; // now neighbor
				current.currentCell(current_Cell);
				cct.highlightM(current.getYCoord(), current.getXCoord(), current_Cell_Color);
				current.setState(State.VISITED); // also visited
				this.unvisitedCells.remove(current);
				
				lang.nextStep(); // next Step
				
				// Un-Highlights
				psdoCode.unhighlight(2);
				psdoCode.unhighlight(6);
				srcCode.unhighlight(5);
				srcCode.unhighlight(9);
				srcCode.unhighlight(10);
				srcCode.unhighlight(11);
				
			} else if(!this.cellStack.isEmpty()) {
					
					// Highlights
					psdoCode.highlight(7);
					psdoCode.highlight(8);
					psdoCode.highlight(9);
					srcCode.highlight(4);
					srcCode.highlight(12);
					srcCode.highlight(13);
					
					current.purgeCell();
					currentWall = this.wallStack.peek(); // top of the stack
					currentWall.removeWall(current, currentWall); // the Way Back
					
					cct.unhighlightM(current.getYCoord(), current.getXCoord());
					current = this.cellStack.pop();
					current.currentCell(current_Cell);
					this.visualStack.popOut();
					currentWall = this.wallStack.pop();
					cct.highlightM(current.getYCoord(), current.getXCoord(), current_Cell_Color);
					
					lang.nextStep(); // next Step
					
					// Un-Highlights
					psdoCode.unhighlight(7);
					psdoCode.unhighlight(8);
					psdoCode.unhighlight(9);
					srcCode.unhighlight(12);
					srcCode.unhighlight(13);
						
			} else {
			
				current = this.unvisitedCells.remove(getRandomInt(unvisitedCells.size()));
				current.setState(State.VISITED);
			}			
		}
		this.quizQuestionTwo(); // second quiz question
		// Un-Highlights
		psdoCode.unhighlight(1);
		srcCode.unhighlight(3);
		
		// clean stack at the end
		while (!this.cellStack.isEmpty()) {
			
			// Highlights
			psdoCode.highlight(10);
			srcCode.highlight(15);
			srcCode.highlight(16);
			
			current.purgeCell(); // make cells white
			currentWall = this.wallStack.peek(); // top of the stack
			currentWall.removeWall(current, currentWall); // the Way Back
			
			cct.unhighlightM(current.getYCoord(), current.getXCoord());
			current = this.cellStack.peek();
			current.currentCell(current_Cell);
			cct.highlightM(current.getYCoord(), current.getXCoord(), current_Cell_Color);
			this.visualStack.highlight(current_Cell_Color);
			
			lang.nextStep(); // next Step
			
			current = this.cellStack.pop();
			this.visualStack.popOut();
			currentWall = this.wallStack.pop();
			cct.unhighlightM(current.getYCoord(), current.getXCoord());	
		}
		
		// Un-Highlights
		psdoCode.unhighlight(10);
		srcCode.unhighlight(15);
		srcCode.unhighlight(16);
		start.purgeCell();

		currentWall.removeWall(start, currentWall); // the Way back
		
		lang.nextStep(); // next Step		
	}
	
	/**
	 * This method chooses a random unvisited neighbor of a cell.
	 * @param c the cell
	 * @return the cell neighbor
	 */
	public Cell chooseRandomUnvisitedNeighbor(Cell c) {
		ArrayList<Cell> neighs = this.findUnvisitedNeighbors(c);
		
		if(neighs.isEmpty()) {
			return null;
			
		}
		return neighs.get(getRandomInt(neighs.size()));
	}
	
	/**
	 * This method shows a random unvisited neighbor of a cell.
	 * @param c the cell
	 * @param clr the color
	 */
	public void showAllRandomUnvisitedNeighbors(Cell c, RectProperties recProps) {
		ArrayList<Cell> neighs = this.findUnvisitedNeighbors(c);
	
		for(int i = 0; i < neighs.size(); i++) {
			neighs.get(i).markNeighborhood(recProps);	
		}
	}
	
	/**
	 * This method finds unvisited neighbors around a cell.
	 * @param c the cell
	 * @return the neighborhood of the cell
	 */
	public ArrayList<Cell> findUnvisitedNeighbors(Cell c) {
		ArrayList<Cell> neighborhood = new ArrayList<Cell>();
		ArrayList<Orientation> pos = c.getNeighborsPos();
		
		for(Orientation o : pos) {
			Cell neighbor = this.collection.get(this.getIndexOfCell(c.getXCoord() + o.dx, c.getYCoord() + o.dy));
			
			if(!neighbor.isVisited())
				neighborhood.add(neighbor);
		}
		return neighborhood;
	}
	
	/**
	 * This method calculates the position of a wall between two cells
	 * in the collection.
	 * @param c1 the first cell
	 * @param c2 the second cell
	 * @return the wall in-between two cells
	 */
	public Wall betweenWall(Cell c1, Cell c2) {
		int x = c1.getXCoord() - c2.getXCoord(); // indices
		int y = c1.getYCoord() - c2.getYCoord(); // indices
		if(x != 0 && y != 0) 
			return null;
		x = c2.getXCoord() + (x / 2);
		y = c2.getYCoord() + (y / 2);
		
		return (Wall) this.collection.get(getIndexOfCell(x,y));
	}
	
	// =====================================================================
	// Setter method
	// =====================================================================
	
	/**
	 * Setter method for defining the boundary of the maze in x-direction.
	 * @param bound_x the x-coordinate
	 */
	public void setBOUND_X(int bound_x) {
		BOUND_X = bound_x;
	}
	
	/**
	 * Setter method for defining the boundary of the maze in y-direction. 
	 * @param bound_y the y-coordinate
	 */
	public void setBOUND_Y(int bound_y) {
		BOUND_Y = bound_y;
	}
	
	// =====================================================================
	// Getter method
	// =====================================================================
	
	/**
	 * Getter method for generating a random number (rng).
	 * Rng is determined by the system time.
	 * @param bound boundary
	 * @return a random number
	 */
	public int getRandomInt(int bound) {
		return (int) (System.currentTimeMillis() % bound);
	}
	
	/**
	 * Getter method for returning the indices of a cell.
	 * @param x the index x-direction
	 * @param y the index y-direction
	 * @return the indices
	 */
	private int getIndexOfCell(int x, int y) {
		return (BOUND_X * y) + x;
	}
	
	/**
	 * Getter method for returning the collection of cells & walls.
	 * @return the collection
	 */
	public ArrayList<Cell> getCollection() {
		return collection;
	}
	
	/**
	 * This method prevents overlapping of various content in the animation panel
	 * if the maze is configured smaller than its default settings.
	 * @param cSize the cellSize
	 * @param wSize the wallSize
	 */
	public void preventOverlapping(int cSize, int wSize) {
		
		// Rewrite affected areas located under the maze.
		if(numOfCellsRow == 2) {
			guideBox[0] += 40;
			guideTitlePos = new Offset(guideBox[0] + 10, 0, startPos, AnimalScript.DIRECTION_S); 
			guidanceC_Pos = new Offset((numOfCellsRow * 38) + factor + 65, 0, upperLeftSupportMatrix, AnimalScript.DIRECTION_S);
			
		}
		if(baseOffsetY < ((numOfCellsCol + 8) * 32)) {
			stackTitlePos = new Offset(0, 	((numOfCellsCol + 8) * 32), startPos, AnimalScript.DIRECTION_SE); 
			lifo_TitlePos = new Offset(75, ((numOfCellsCol + 8) * 32) + 20, startPos, AnimalScript.DIRECTION_SE);
			iva_Stack_Pos = new Offset(0, 	((numOfCellsCol + 8) * 32) + factor + 15, startPos, AnimalScript.DIRECTION_SE); 
			
			pCodeTitlePos = new Offset(10, 	((numOfCellsCol + 12) * 32) + 12, startPos, AnimalScript.DIRECTION_S); 
			sCodeTitlePos = new Offset(658, ((numOfCellsCol + 12) * 32) + 12, startPos, AnimalScript.DIRECTION_S); 
			codeBox1[1] = ((numOfCellsCol + 13) * 32) + 5;
			codeBox2[1] = ((numOfCellsCol + 13) * 32) + 5; 
			
			pseudoCodePos = new Offset(30, 	codeBox1[1], startPos, AnimalScript.DIRECTION_SW); 
			sourceCodePos = new Offset(688, codeBox2[1], startPos, AnimalScript.DIRECTION_S); 
		} 
		
	}
	
	/**
	 * This method just hides all areas for the conclusion slide.
	 */
	public void hideAreas () {
		supportM.hide();
		colorM.hide();
		visualStack.hideIvaStack();
		pTitleFrame.hide();
		sTitleFrame.hide();
		pcodeArea.hide();
		scodeArea.hide();
		psdoCode.hide();
		srcCode.hide();
		
	}
	
	/**
	 * This methods coordinates the whole animation flow from start to finish.
	 */
	public void setup() {
		
		this.dpc = new DisplayContent(coral, dark_paleturquoise);
		this.cct = new CreateContent();
		
		this.preventOverlapping(cellSize, wallSize); // for a non-default maze.
		
		dpc.showTitleMaze(titlePos); // Global Title, always on.
		
		// 1. Introduction =====================================================	
		introArea 	= cct.createArea(introBox, lightGray, "INTRO", x, y);
		introFrame 	= cct.createFrame(introBox, lightGray, "INTRO", x, y);
		
		dpc.showTitleIntroduction(introTitlePos);
		dpc.showIntroductionMaze(introduct_Pos);
		
		introArea.hide();
		introFrame.hide();
		// =====================================================================
		
		// 2. Initialize maze ==================================================
		this.initializeComponents();
		cct.createBoard(startPos);
		base = cct.createBackGround(startPos);
		// =====================================================================
		
		// 3. Instruction ======================================================
		guideArea 	= cct.createArea(guideBox,  lightGray, "GUIDE", x, y);
		gTitleFrame = cct.createFrame(guideBox, lightGray, "GUIDE", x, y);
		
		dpc.showTitleGuide(guideTitlePos); 		// guide Section
		dpc.showInstructionMaze(guidanceC_Pos); // Start with the Maze Guide
		// =====================================================================
		
		// 4. Show mirror maze =================================================
		supportM = cct.createSupportMatrix(upperLeftSupportMatrix);
		
		dpc.showTitleSupportGrid(supMxTitlePos);		// Title
		dpc.showInstructionSuppMatrix(guidanceC_Pos); 	// Support Matrix Guide
		// =====================================================================
		
		// 5. Show color legend ================================================
		colorM = cct.createColorLegend(upperLeftSupportMatrix);
		
		dpc.showTitleColor(chartTitlePos);				// Title
		dpc.showInstructionColorChart(guidanceC_Pos); 	// Color Legend Guide
		// =====================================================================
		
		// 6. Show stack =======================================================
		this.showStack();
		
		dpc.showTitleStack(stackTitlePos);			// Title
		dpc.showTitleLifo(lifo_TitlePos);			// Title (lifo)
		dpc.showInstructionStack(guidanceC_Pos); 	// Stack Guide
		// =====================================================================
		
		// 7. Show pseudo code =================================================
		this.psdoCode = dpc.showPseudoCode(pseudoCodePos);

		pcodeArea 	= cct.createArea(codeBox1, coral, "CAP", x, y);
		pTitleFrame = cct.createFrame(codeBox1, coral, "CAP", x, y);
		
		dpc.showTitlePseudoCode(pCodeTitlePos);			// Title
		dpc.showInstructionPseudoCode(guidanceC_Pos);	// Pseudo Code Guide
		// =====================================================================
		
		// 8. Show source code =================================================
		this.srcCode = dpc.showSourceCode(sourceCodePos);

		scodeArea 	= cct.createArea(codeBox2, dark_paleturquoise, "CAS", x, y);
		sTitleFrame = cct.createFrame(codeBox2, dark_paleturquoise, "CAS", x, y);
		
		dpc.showTitleSourceCode(sCodeTitlePos);			// Title
		dpc.showInstructionSourceCode(guidanceC_Pos); 	// Source Code Guide
		// =====================================================================
		
		// 9. Intermezzo =======================================================
		// prepare everything for the generation
		guideArea.hide();
		gTitleFrame.hide();
		lang.nextStep();
		// =====================================================================
		
		// 10. Maze generation =================================================
		this.explore();
		// =====================================================================
	
		// 11. Conclusion ======================================================
		this.quizQuestionThree(); // third quiz question
		
		dpc.hideTitles();
		this.hideAreas();
		
		outroArea 	= cct.createArea(outroBox, lightGray, "OUTRO", x, y);
		outroFrame 	= cct.createFrame(outroBox, lightGray, "OUTRO", x, y);
		
		dpc.showTitleConclusion(outroTitlePos); // Title
		dpc.showConclusionMaze(conclusionPos);	// Conclusion
		// =====================================================================
	}

    public String getName() {
        return "Recursive Backtracking Maze Generation";
    }

    public String getAlgorithmName() {
        return "Recursive Backtracking Maze Generation";
    }

    public String getAnimationAuthor() {
        return "David Berman";
    }

    public String getDescription(){ // TODO
       String description = 
    		   "A maze is a complex collection of branching paths or passages with various routes."
    		   +"\n"
    		   +"Categorized as a puzzle, a maze is defined by these two important characteristics:"
    		   +"\n"
    		   +"\n"
    		   +"There are no unreachable areas"
    		   +"\n"
    		   +"There are no path loops"
    		   +"\n"
    		   +"\n"
    		   +"Many different ways of creating a perfect maze exist, but the most elementary"
    		   +"\n"
    		   +"procedure is either to carve a passage into an uncharted field or by adding walls"
    		   +"\n"
    		   +"along the path."
    		   +"\n"
    		   +"The recursive backtracking maze generation follows the first concept of carving a"
    		   +"\n"
    		   +"random passage into an unknown section. A section or an area is represented by"
    		   +"\n"
    		   +"a cell surrounded by four walls."
    		   +"\n"
    		   +"Each time the algorithm moves to a new unexplored cell it will push the previous"
    		   +"\n"
    		   +"cell into its memory, modeled by a stack, and remove the separating wall among"
    		   +"\n"
    		   +"both cells."
    		   +"\n"
    		   +"What makes the recursive backtracking method different to other algorithm is its"
    		   +"\n"
    		   +"approach to the problem while exploring. Whenever the algorithm reaches a death"
    		   +"\n"
    		   +"end, it will trace its path back to find more areas to carve passageways."
    		   +"\n"
    		   +"The maze is generated when all areas are explored and the algorithm backed all"
    		   +"\n"
    		   +"the way up to the starting point, while clearing the stack.";
    	
    	return description;
        		
        		
        		
    }

    public String getCodeExample(){
        return "1. Make the initial cell the current cell and mark it as visited"
 +"\n"
 +"2. While there are unvisited cells"
 +"\n"
 +"    1. If the current cell has any neighbours which have not been visited"
 +"\n"
 +"        1. Choose randomly one of the unvisited neighbours"
 +"\n"
 +"        2. Push the current cell to the stack"
 +"\n"
 +"        3. Remove the wall between the current cell and the chosen cell"
 +"\n"
 +"        4. Make the chosen cell the current cell and mark it as visited"
 +"\n"
 +"    2. Else if stack is not empty"
 +"\n"
 +"        1. Pop a cell from the stack"
 +"\n"
 +"        2. Make it the current cell"
 +"\n"
 +"3. Pop the complete stack"
 +"\n"
 +"\n"
 +"	          Source: Wikipedia - Maze generation algorithm";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_BACKTRACKING);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT; // TODO
    }
 // =====================================================================
 	// Inner CLass Cell
 	// =====================================================================

 	/**
 	 * This class defines a single cell component.
 	 * Every cell has a position, state & also an identification.
 	 * In addition each cell keeps track of its neighboring cells. 
 	 * @author David Berman
 	 *
 	 */
 	protected class Cell {
 		
 		// Language instance
 		private Language lang = RecursiveBacktrackingMazeGeneration.this.lang;
 		
 		// =====================================================================
 		// Variables
 		// =====================================================================
 		
 		protected int x, y; 							// indices
 		private int id; 								// identifier
 		private State state; 							// state 
 		private ArrayList<Orientation> neighborsPos; 	// neighborhood 
 		
 		/* AlgoAnim Variables */
 		protected Rect crec; 	// the cell as an AlgoAnim Rectangle
 		private Node upLeft; 	// AlgoAnim Node Coordinates
 		private Node lowRight; 	// AlgoAnim Node Coordinates
 		
 		// =====================================================================
 		// Properties
 		// =====================================================================
 		
 		// purge cell properties
 		private RectProperties purgeProps = new RectProperties() {{ 
 			set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2); // above background
 			set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
 			set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
 			set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
 		}};

 		// =====================================================================
 		// Constructors
 		// =====================================================================
 		
 		/**
 		 * First constructor of a cell. A cell in its core only needs its id,
 		 * its 2 AlgoAnim coordinate-pairs (for the positioning in the Animation 
 		 * panel) and their indices.
 		 * @param id the identifier
 		 * @param upLeft the AlgoAnim coordinates
 		 * @param lowRight the AlgoAnim coordinates
 		 * @param x the index x-direction
 		 * @param y the index y-direction
 		 */
 		public Cell(int id, Node upLeft, Node lowRight, int x, int y) {
 			this.id = id;
 			this.upLeft = upLeft;
 			this.lowRight = lowRight;
 			this.setState(State.DEFAULT); // a default state
 			this.setX(x); 					
 			this.setY(y); 					
 		}
 		
 		/**
 		 * Second constructor of a cell. Also takes the state.
 		 * The wall component extends from the cell component and does not need 
 		 * a state & neighbor positions, thats why we split the constructor in 
 		 * 2 separate ones.
 		 * @param id the identifier
 		 * @param upLeft the AlgoAnim coordinates
 		 * @param lowRight the AlgoAnim coordinates
 		 * @param st the state
 		 * @param x the index x-direction
 		 * @param y the index y-direction
 		 */
 		public Cell(int id, Node upLeft, Node lowRight, State st, int x, int y) {
 			this(id, upLeft,lowRight, x ,y); // super

 			this.setState(st);
 			this.setNeighborsPos();
 			
 		}

 		// =====================================================================
 		// Setter Methods
 		// =====================================================================

 		/**
 		 * This method sets the position of the neighbors surrounding the cell.
 		 * In addition it also finds out how many neighbors are around the cell.
 		 * (Because the corners of the maze have cells with maximum 2 neighbors.)
 		 */
 		private void setNeighborsPos(){
 			// create a list with 4 orientations (top, down, left, right) & remove
 			// the orientation for the special corner cells.
 			this.neighborsPos = new ArrayList<Orientation>(Arrays.asList(Orientation.values()));
 			ArrayList<Orientation> removeList = new ArrayList<Orientation>();
 			
 			for(Orientation o : neighborsPos) {
 				
 				switch (o) {
 				case LEFT : // check left
 					// check corner/border indices of cell
 					if (this.x == 0 || this.x == 1)  
 						removeList.add(o);
 					break;
 				
 				case DOWN : // check down
 					// check corner/border indices of cell
 					if (this.y == RecursiveBacktrackingMazeGeneration.this.BOUND_Y - 1 || 
 						this.y == RecursiveBacktrackingMazeGeneration.this.BOUND_Y - 2)
 						removeList.add(o);
 					break;
 				
 				case RIGHT: // check right
 					// check corner/border indices of cell
 					if (this.x == RecursiveBacktrackingMazeGeneration.this.BOUND_X - 1 ||
 						this.x == RecursiveBacktrackingMazeGeneration.this.BOUND_X - 2)
 						removeList.add(o);
 					break;
 				
 				case TOP: // check top
 					// check corner/border indices of cell
 					if (this.y == 0 || this.y == 1)
 						removeList.add(o);
 					break;
 				
 				default:
 					break;
 				}

 			}
 			this.neighborsPos.removeAll(removeList);
 		}
 			
 		/**
 		 * Setter method the final x index of the cell,
 		 * while checking the boundaries of the maze.
 		 * @param x the index x-direction
 		 */
 		private void setX(int x) {
 			if(x < 0 || x > RecursiveBacktrackingMazeGeneration.this.BOUND_X) {
 				throw new IndexOutOfBoundsException();
 			}
 			this.x = x;
 		}
 		
 		/**
 		 * Setter method for the final y index of the cell,
 		 * while checking the boundaries of the maze.
 		 * @param y the index y-direction
 		 */
 		private void setY(int y) {
 			if(y < 0 || y > RecursiveBacktrackingMazeGeneration.this.BOUND_Y) {
 				throw new IndexOutOfBoundsException();
 			}
 			this.y = y;
 		}
 		
 		/**
 		 * Setter method for the cell state.
 		 * @param state the state
 		 */
 		public void setState(State state) {
 			this.state = state;
 		}
 		
 		/**
 		 * Setter method for setting a cell as an AlgoAnim primitive Rectangle.
 		 */
 		public void setCell(RectProperties recProps) { // recProps passing
 			this.crec = lang.newRect(upLeft, lowRight, "ID"+id, null, recProps);
 			
 		}
 		
 		// =====================================================================
 		// Getter methods
 		// =====================================================================
 		
 		/**
 		 * Getter method for the cell state.
 		 * @return the state
 		 */
 		public State getState() {
 			return state;
 		}
 		
 		/**
 		 * Getter method for the neighbor positions.
 		 * @return the neighbor positions
 		 */
 		public ArrayList<Orientation> getNeighborsPos(){
 			return neighborsPos;
 		}
 		
 		/**
 		 * Getter method for the x index/coordinate of the cell.
 		 * @return the x index/coordinate
 		 */
 		public int getXCoord(){
 			return x;
 		
 		}
 		
 		/**
 		 * Getter method for the y index/coordinate of the cell.
 		 * @return the y index/coordinate
 		 */
 		public int getYCoord(){
 			return y;
 		}
 		
 		/**
 		 * Getter method for a cell.
 		 * @return the cell
 		 */
 		public Rect getCell() {
 			return crec;
 		}
 		
 		/**
 		 * Getter method for the cell identifier.
 		 * @return the identifier
 		 */
 		public int getID() {
 			return id;
 		}
 		
 		/**
 		 * This method checks if the cell is visited or not.
 		 * @return boolean
 		 */
 		public boolean isVisited(){
 			return this.getState() == State.VISITED;
 		}

 		// =====================================================================
 		// Visualisation
 		// =====================================================================
 		
 		/**
 		 * This method displays a cell with the initial cell color.
 		 * Initial cell are by default "unvisited".
 		 */
 		public void initCell(RectProperties recProps) {
 			this.setCell(recProps);
 		}
 		
 		/**
 		 * This method traces cell by the trace color.
 		 * Cells that are traced are "visited".
 		 */
 		public void traceCell(RectProperties recProps) {
 			this.setCell(recProps);
 		}
 		
 		/**
 		 * This method displays a cell with the current cell color.
 		 */
 		public void currentCell(RectProperties recProps) {
 			this.setCell(recProps);
 		}
 		
 		/**
 		 * This method displays a cell with the chosen cell color.
 		 * Cells that are selected are "chosen".
 		 */
 		public void chosenCell(RectProperties recProps) {
 			this.setCell(recProps);
 			
 		}
 		
 		/**
 		 * This method displays a cell with the purge cell color, which is always white.
 		 */
 		public void purgeCell() {
 			this.setCell(purgeProps);
 		}
 		
 		/**
 		 * This method displays cells with the neighbor color.
 		 * Cells that are surrounding another cell are the "neighborhood" of the other cell.
 		 * @param clr the color
 		 */
 		public void markNeighborhood(RectProperties recProps) {
 			this.setCell(recProps);
 		}
		
 	}
 	
 	// =====================================================================
 	// Inner Class Wall
 	// =====================================================================

 	/**
 	 * This class defines the wall component. A wall component
 	 * is actually a cell but with lesser features, like no state
 	 * and no knowledge of neighbor positions.
 	 * @author David Berman
 	 *
 	 */
 	protected class Wall extends Cell {
 		
 		// Language instance
 		private Language lang = RecursiveBacktrackingMazeGeneration.this.lang; 
 		
 		// =====================================================================
 		// Variables
 		// =====================================================================
 		
 		private int x, y; 	// indices
 		private int id; 	// the identifier
 		
 		/* AlgoAnim Variables */
 		protected Rect wrec; 	// the wall as an AlgoAnim rectangle
 		private Node upLeft; 	// AlgoAnim Node Coordinates
 		private Node lowRight; 	// AlgoAnim Node Coordinates
 			
 		/**
 		 * The constructor of a wall. A wall has an identifier, its
 		 * 2 AlgoAnim coordinate-pairs (for the positioning in the 
 		 * Animation panel) and its indices.
 		 * @param id the identifier
 		 * @param upLeft the ALgoAnim coordinates
 		 * @param lowRight the AlgoAnim coordinates
 		 * @param x the index x-direction
 		 * @param y the index y-direction
 		 */
 		public Wall(int id, Node upLeft, Node lowRight, int x, int y) {
 			super(id, upLeft, lowRight, x, y); // super
 			this.id = id;
 			this.upLeft = upLeft;
 			this.lowRight = lowRight;
 			this.x = x;
 			this.y = y;

 		}
 			
 		/**
 		 * This method removes the selected wall that separates 2 cells.
 		 * @param c1 the first cell
 		 * @param c2 the second cell
 		 */
 		public void removeWall(Cell c1, Cell c2) { 
 			if(this.isRemoved() == true){
 				purgeCell(); // end-removing
 				return;
 			}
 			this.setState(State.VISITED);
 			// adjust color to surrounding cells
 			// during animation.
 			chosenCell(RecursiveBacktrackingMazeGeneration.this.chosen_Cell); 
 			//purgeCell();
 			
 		}
 		
 		/**
 		 * This method checks if a wall is removed.
 		 * @return boolean
 		 */
 		public boolean isRemoved() {
 			return (this.getState() == State.VISITED) ? true : false;
 		}
 			
 		/**
 		 * Getter method for a wall component.
 		 * @return the wall
 		 */
 		public Rect getWall() {
 			return wrec;
 		}
 			
 	}

 	// =====================================================================
 	// Inner Class StackVisualisation
 	// =====================================================================

 	/**
 	 * This class handles the custom stack visualisation based on a simple array.
 	 * @author David Berman
 	 *
 	 */
 	protected class StackVisualization { 

 		// Language instance
 		private Language lang = RecursiveBacktrackingMazeGeneration.this.lang;
 		
 		// =====================================================================
 		// Variables
 		// =====================================================================
 		
 		private int top = 0; 			// safety assignment
 		private int capacity; 			// stack size
 		private Node pos = null; 		// position
 		private StringArray ivaStack; 	// improvised & visualized array Stack
 		private ArrayMarker topM; 		// Array Marker

 		// =====================================================================
 		// Properties
 		// =====================================================================
 		
 		// array properties
 		private ArrayProperties aProps = new ArrayProperties() {{ 
 			set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
 			set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
 			set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
 		}};
 		
 		// marker properties
 		private ArrayMarkerProperties markerProps = new ArrayMarkerProperties() {{ 
 			set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
 			set(AnimationPropertiesKeys.LABEL_PROPERTY, "Top");
 		}};
 		
 		// =====================================================================
 		// Colors
 		// =====================================================================
 		
 		private Color highlightColor1;
 		private Color highlightColor2;
 		
 		/**
 		 * The constructor assigns the stack capacity, the stack position,
 		 * the stack marker and the highlight colors.
 		 * @param offset the position in the animation panel
 		 * @param rowSize the rowSize of the maze
 		 * @param colSize  the colSize of the maze
 		 * @param clr1 the first color
 		 * @param clr2 the second color
 		 */
 		public StackVisualization(Offset pos, int rowSize, int colSize, Color clr1, Color clr2) {
 			capacity = rowSize * colSize;
 			this.pos = pos;
 			ivaStack = lang.newStringArray(pos, new String[capacity], "Stack", null, aProps);
 			topM = lang.newArrayMarker(ivaStack, top, "StackTop", null, markerProps);
 			ivaStack.showIndices(false, null, null);
 			this.highlightColor1 = clr1;
 			this.highlightColor2 = clr2;
 		}
 		
 		/**
 		 * This method highlights the corresponding stack entries.
 		 * @param clr the color
 		 */
 		public void highlight(Color clr) {
 			if(top != 0) {
 				top--; // peak top
 				ivaStack.setHighlightFillColor(top, clr, null, null);
 				ivaStack.highlightCell(top, null,null);
 				top++; // move top marker back to top
 			} else {
 				return;
 			}
 		}
 		
 		/**
 		 * This method unhighlights the corresponding stack entries.
 		 */
 		public void unhighlight() {
 			ivaStack.unhighlightCell(top,  null, null);
 		}
 		
 		/**
 		 * This method is a push operation for an array stack.
 		 * @param id the identifier
 		 */
 		public void pushIn(int id) {
 			String identifier = String.format("%02d", id);
 			if (top < capacity) {
 				ivaStack.put(top, "C"+identifier, null, null);
 				ivaStack.setTextColor(top, Color.BLACK, null, null);
 				topM.move(top, null, null);
 					
 			}
 			top++;
 			highlight(highlightColor1);
 		}
 		
 		/**
 		 * This method is a pop operation for an array stack.
 		 */
 		public void popOut() {
 			top--;
 			unhighlight();
 			ivaStack.setTextColor(top, Color.WHITE, null, null);
 			highlight(highlightColor2);
 			if(top > 0) { // prevent out of bound
 				topM.move(top - 1, null, null);	
 			}else {
 				topM.move(top, null, null);
 			}
 		}

 		/**
 		 * This method just hides the ivaStack & marker for the conclusion slide.
 		 */
 		public void hideIvaStack() {
 			ivaStack.hide();
 			topM.hide();
 		}


 	}
 	
 	// =====================================================================
 	// Inner Class DisplayContent
 	// =====================================================================

 	/**
 	 * This class handles all displayable Text content for the Generator.
 	 * @author David Berman
 	 *
 	 */
 	protected class DisplayContent {

 		// Language instance
 		private Language lang = RecursiveBacktrackingMazeGeneration.this.lang;
 		
 		// Main Title & Author
 		private static final String generator = "Recursive Backtracking Maze Generation";
 		private static final String author = "David Berman";
 		
 		// =====================================================================
 		// Properties
 		// =====================================================================
 		
 		// code properties
 		SourceCodeProperties codeProps = new SourceCodeProperties() {{ 
 			set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
 			set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 15)); 
 			// Highlight settings
 			set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
 			set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

 		}};
 		
 		// guide properties
 		SourceCodeProperties guideProps = new SourceCodeProperties() {{ 
 			set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15)); 
 			set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

 		}};
 		
 		// title properties
 		SourceCodeProperties titleProps = new SourceCodeProperties() {{ 
 			set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18)); 
 			set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
 		}};
 		
 		// =====================================================================
 		// Colors
 		// =====================================================================
 		
 		private final Color coral;
 		private final Color dark_paleturquoise;
 		private final Color hyperlinkColor = new Color(56, 99, 172);
 		private final Color darkGreen = new Color(63, 158, 0);
 		private final Color darkBlue = new Color(80, 92, 161);
 		private final Color lightRedBrown = new Color(166, 49, 92);
 		
 		// =====================================================================
 		// Titles
 		// =====================================================================
 		
 		private Text introMzeTitle;
 		private Text guidanceTitle;
 		private Text gridMtx_Title;
 		private Text clrChartTitle;
 		private Text ivaStackTitle;
 		private Text stkLifo_Title;
 		private Text pseudoC_Title;
 		private Text sourceC_Title;
 		private Text outroMzeTitle;
 		
 		// =====================================================================
 		// Text Content (local)
 		// =====================================================================
 		
 		private final String[] MazeIntroduction =
 			{
 				"A maze is a complex collection of branching paths or passages with various routes.",
 				"Categorized as a puzzle, a maze is defined by these two important characteristics:",
 				"\u2022 There are no unreachable areas",	// BulletPoint
 				"\u2022 There are no path loops",			// BulletPoint
 				"Many different ways of creating a perfect maze exist, but the most elementary",
 				"procedure is either to carve a passage into an uncharted field or by adding walls",
 				"along the path.",
 				"The recursive backtracking maze generation follows the first concept of carving a",
 				"random passage into an unknown section. A section or an area is represented by",
 				"a cell surrounded by four walls.",
 				"Each time the algorithm moves to a new unexplored cell it will push the previous",
 				"cell into its memory, modeled by a stack, and remove the separating wall among",
 				"both cells.",
 				"What makes the recursive backtracking method different to other algorithm is its",
 				"approach to the problem while exploring. Whenever the algorithm reaches a death",
 				"end, it will trace its path back to find more areas to carve passageways.",
 				"The maze is generated when all areas are explored and the algorithm backed all",
 				"the way up to the starting point, while clearing the stack."
 			};
 		
 		private final String[] MazeConclusion = 
 			{
 				"Recursive Backtracking stops once it encounters any solution. In case of creating",
 				"a maze, once we find a path from a starting cell to the last unvisited cell - we",
 				"found a valid solution.",
 				"It may not be the best result (from a strict performance perspective), but the",
 				"additional randomness gurantess a variety of different mazes while still",
 				"maintaining the same size. This means that recursive backtracking, unlike other",
 				"algorithm, is plain fast and efficient by a reasonable amount of cells in the",
 				"maze.",
 				"Therefore if the size grows, backtracking will lead to fewer but longer dead ends",
 				"and usually to a more complex solution. (Which is great for an interesting maze!)",
 				"What generally holds backtracking back is the memory consumption. You need to",
 				"store the entire maze, which requires a stack capacity proportional to the amount",
 				"of cells in rows (n) & columns (m) rendering a complexity of O(n*m)."
 			};
 		
 		private final String[] MazeInstruction = 
 			{
 				"Maze", // Title
 				"On the lefthand side is the maze located. To generate a proper maze with a ",
 				"recursive backtracking method, we need to start with a predetermined",
 				"arrangement of cells that are separated by walls. During the animation the",
 				"algorithm will traverse the uncharted fields and create a perfect passageway."
 			};
 			
 		private final String[] CellMatrixInstruction = 
 			{
 				"Cell Identifier", // Title
 				"Every cell has an internal identification. The ID consists of a leading 'C', which",
 				"stands for cell, and a grid number. This matrix renders the background process of",
 				"the recursive generation and mirros the maze. It also acts as a guidance for the",
 				"stack visualisation."
 			};
 		
 		private final String[] ColorLegendInstruction =
 			{
 				"Color Code Chart", // Title
 				"For a better visualisation each state is colored differently. The small color chart",
 				"next to the maze serves as a look-up table."
 			};

 		private final String[] StackInstruction = 
 			{
 				"Stack Visualisation", // Title
 				"A stack is an abstract data type, which stores a collection of elements temporary",
 				"by using two operations: Push (insert items) & Pop (discard latest items).",
 				"The element order in a stack is following the LIFO princip, which stands for",
 				"'Last in First out'. Here the marker 'Top' symbolises the peak of the stack and",
 				"always points to the most recent element, that either can be popped or stacked",
 				"with another one (push).",
 				"In our case we will use the stack as a dynamic data structure that keeps track of",
 				"all visited cells. To display the internal behavior, we will Push&Pop the associated",
 				"cell ID's from the identifier matrix based on the rules from the pseudo code."
 			};
 		
 		private final String[] PseudoCodeInstruction = 
 			{
 				"Pseudo Code", // Title
 				"Under the stack visualisation is the pseudo code, which defines what the algorithm",
 				"should do next in a specific situation."
 			};

 		private final String[] SourceCodeInstruction = 
 			{
 				"Source Code", //Title
 				"On the opposite side is a Java code snippet of the method explore(). This method",
 				"is responsible for generating a maze by exploring the cells randomly and tracing",
 				"a path. Each important code line will be highlighted in sync with the pseudo code",
 				"rule set.",
 				"Lets start traversing the uncharted Maze to carve a path."
 			};
 		
 		private final String[] PseudoCode = 
 			{
 				"1. Make the initial cell the current cell and mark it as visited",
 				"2. While there are unvisited cells",
 				"1. If the current cell has any neighbours which have not been visited",
 				"1. Choose randomly one of the unvisited neighbours",
 				"2. Push the current cell to the stack",
 				"3. Remove the wall between the current cell and the chosen cell",
 				"4. Make the chosen cell the current cell and mark it as visited",
 				"2. Else if stack is not empty",
 				"1. Pop a cell from the stack",
 				"2. Make it the current cell",
 				"3. Pop the complete stack",
 				"Source: Wikipedia - Maze generation algorithm"
 			};
 		
 		private final String[] SourceCode = 
 			{
 				"public void explore() {",
 				"current = unvisitedCells.remove(getRandomInt(unvisitedCells.size()));",
 				"current.setState(State.VISITED);",
 				"while(!this.unvisitedCells.isEmpty()) {",
 				"Cell unvisited = this.chooseRandomUnvisitedNeighbor(current);",
 				"if(unvisited != null) {",
 				"this.cellStack.push(current);",
 				"currentWall = this.betweenWall(current, unvisited);",
 				"currentWall.removeWall(current, unvisited);",
 				"current = unvisited;",
 				"current.setState(State.VISITED);",
 				"this.unvisitedCells.remove(current);",
 				"} else if(!this.cellStack.isEmpty()) {",
 				"current = this.cellStack.pop();",
 				"}",
 				"} while(!this.cellStack.isEmpty()) {",
 				"current = this.cellStack.pop();",
 				"}",
 				"}"
 			
 			};

 		/**
 		 * The constructor for DisplayContent.
 		 * Passing 2 color values for 2 specific titles.
 		 * @param clr1 first color
 		 * @param clr2 second color
 		 */
 		public DisplayContent(Color clr1, Color clr2) { 
 			this.coral = clr1;
 			this.dark_paleturquoise = clr2;
 		}
 		
 		/**
 		 * This method shows the global title of the Generator.
 		 * Title: Recursive Backtracking Maze Generation.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleMaze(Node pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 30));
 			lang.newText(pos, generator, "GeneratorTitle", null, txtProps);
 			
 			this.showAuthor(pos); // call author
 		}
 		
 		/**
 		 * This method show the author of the Generator.
 		 * @author David Berman
 		 * @param pos the author position in the animation panel
 		 */
 		public void showAuthor(Node pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.ITALIC, 15));
 			Offset relaPos = new Offset(430, 35, pos, AnimalScript.DIRECTION_BASELINE_START); 
 			lang.newText(relaPos, "by " + author, "Author", null, txtProps);
 		}
 		
 		/**
 		 * This method shows the introduction title on the first slide.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleIntroduction (Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 17)); 
 			
 			introMzeTitle = lang.newText(pos, "Introduction", "TitleIntro", null, txtProps);
 			introMzeTitle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null); //?
 		}
 		
 		/**
 		 * This method shows the title of the guide/instruction section in the first slides.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleGuide(Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 17));
 			
 			guidanceTitle = lang.newText(pos, "Instruction", "TitleGuide", null, txtProps);
 			guidanceTitle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
 		}
 		
 		/**
 		 * This method shows the title of the support cell grid during the animation.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleSupportGrid(Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18)); 
 			txtProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, darkGreen);
 			
 			gridMtx_Title = lang.newText(pos, "Cell Identifier", "TitleCI", null, txtProps);
 		}
 		
 		/**
 		 * This method shows the title of the color legend during the animation.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleColor(Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
 			txtProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, darkBlue);
 			
 			clrChartTitle = lang.newText(pos, "Color Code Chart", "TitleC", null, txtProps);
 		}
 		
 		/**
 		 * This method shows the stack title during the animation.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleStack(Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18)); 
 			txtProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, lightRedBrown);
 			
 			ivaStackTitle = lang.newText(pos, "Stack Visualisation", "TitleStk", null, txtProps);
 			
 		}
 		
 		/**
 		 * This method shows the title of the stack principle, LiFo, during the animation.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleLifo(Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14)); 
 			txtProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
 			
 			stkLifo_Title = lang.newText(pos, "LIFO-Principle", "TitleLifo", null, txtProps);
 		}
 		
 		/**
 		 * This method shows the title of the pseudo code during the animation.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitlePseudoCode (Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 17)); 
 			
 			pseudoC_Title = lang.newText(pos, "Pseudo Code", "TitleP", null, txtProps);
 			pseudoC_Title.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE, null, null);
 		}
 		
 		/**
 		 * This method shows the title of the source code during the animation.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleSourceCode(Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 17)); 
 			
 			sourceC_Title = lang.newText(pos, "Source Code", "TitleS", null, txtProps);
 			sourceC_Title.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE, null, null);
 		}
 		
 		/**
 		 * This method shows the conclusion title on the last slide.
 		 * @param pos the title position in the animation panel
 		 */
 		public void showTitleConclusion (Offset pos) {
 			TextProperties txtProps = new TextProperties();
 			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 17)); 
 			
 			outroMzeTitle = lang.newText(pos, "Conclusion", "TitleOutro", null, txtProps);
 			outroMzeTitle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
 		}
 			
 		/**
 		 * This method shows the text content of the maze introduction for the first slide.
 		 * @param pos the content position in the animation panel
 		 */
 		public void showIntroductionMaze(Offset pos) {
 			
 			SourceCode introMaze = lang.newSourceCode(pos, "mazeIntro", null, guideProps);
 			byte[] bytes = MazeIntroduction[2].getBytes(Charset.forName("UTF-8"));
 			String test = new String(bytes, StandardCharsets.UTF_8);
 			
 			introMaze.addCodeLine(MazeIntroduction[0], "Sentence1", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[1], "Sentence2", 0, null);
 			introMaze.addCodeLine("", "Paragraph", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[2], "BulletPoint1", 1, null);
 			introMaze.addCodeLine(MazeIntroduction[3], "BulletPoint2", 1, null);
 			introMaze.addCodeLine("", "Paragraph", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[4], 	"Sentence3", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[5],	"Sentence4", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[6], 	"Sentence5", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[7], 	"Sentence6", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[8], 	"Sentence7", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[9], 	"Sentence8", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[10], "Sentence9", 0, null);
 			introMaze.addCodeLine(MazeIntroduction[11], "Sentence10",0, null);
 			introMaze.addCodeLine(MazeIntroduction[12], "Sentence11",0, null);
 			introMaze.addCodeLine(MazeIntroduction[13], "Sentence12",0, null);
 			introMaze.addCodeLine(MazeIntroduction[14], "Sentence13",0, null);
 			introMaze.addCodeLine(MazeIntroduction[15], "Sentence15",0, null);
 			introMaze.addCodeLine(MazeIntroduction[16], "Sentence16",0, null);
 			introMaze.addCodeLine(MazeIntroduction[17], "Sentence16",0, null);
 			lang.nextStep("Maze Introduction"); // label setting
 			introMzeTitle.hide();
 			introMaze.hide();
 		}
 		
 		/**
 		 * This method shows the text content of the guide/instruction during the animation.
 		 * @param pos the content position in the animation panel
 		 */
 		public void showInstructionMaze(Offset pos) {
 			
 			SourceCode mazeGuide = lang.newSourceCode(pos, "mazeGuide", null, guideProps);
 			SourceCode mazeGuideTitle = lang.newSourceCode(pos, "mazeT", null, titleProps);
 			
 			mazeGuideTitle.addCodeLine(MazeInstruction[0],"Title", 0, null);
 			
 			mazeGuide.addCodeLine("", "Paragraph", 0, null);
 			mazeGuide.addCodeLine("", "Paragraph", 0, null);
 			mazeGuide.addCodeLine(MazeInstruction[1], "Sentence1", 0, null);
 			mazeGuide.addCodeLine(MazeInstruction[2], "Sentence2", 0, null);
 			mazeGuide.addCodeLine(MazeInstruction[3], "Sentence3", 0, null);
 			mazeGuide.addCodeLine(MazeInstruction[4], "Sentance4", 0, null);
 			lang.nextStep("Maze");
 			mazeGuideTitle.hide();
 			mazeGuide.hide();

 		}
 		
 		/**
 		 * This method shows the text content of the support cell matrix guide during the animation.
 		 * @param pos the content position in the animation panel
 		 */
 		public void showInstructionSuppMatrix(Offset pos) {
 			
 			titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, darkGreen);
 			SourceCode supMx = lang.newSourceCode(pos, "cellIDGuide", null, guideProps);
 			SourceCode supMxTitle = lang.newSourceCode(pos, "cellMatrixT", null, titleProps);
 			
 			supMxTitle.addCodeLine(CellMatrixInstruction[0], "Title", 0, null);
 			
 			supMx.addCodeLine("", "Paragraph", 0, null);
 			supMx.addCodeLine("", "Paragraph", 0, null);
 			supMx.addCodeLine(CellMatrixInstruction[1], "Sentence1", 0, null);
 			supMx.addCodeLine(CellMatrixInstruction[2], "Sentence2", 0, null);
 			supMx.addCodeLine(CellMatrixInstruction[3], "Sentence3", 0, null);
 			supMx.addCodeLine(CellMatrixInstruction[4], "Sentence4", 0, null);
 			lang.nextStep("Maze Mirror");
 			supMxTitle.hide();
 			supMx.hide();
 		}
 		
 		/**
 		 * This method shows the text content of the color legend guide during the animation.
 		 * @param pos the content position in the animation panel
 		 */
 		public void showInstructionColorChart(Offset pos) {
 			
 			titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, darkBlue);
 			SourceCode colorLg = lang.newSourceCode(pos, "colorLegendGuide", null, guideProps);
 			SourceCode colorLgTitle = lang.newSourceCode(pos, "colorTitle", null, titleProps);
 			
 			colorLgTitle.addCodeLine(ColorLegendInstruction[0], "Title", 0, null);
 			
 			colorLg.addCodeLine("", "Paragraph", 0, null);
 			colorLg.addCodeLine("", "Paragraph", 0, null);
 			colorLg.addCodeLine(ColorLegendInstruction[1], "Sentence1", 0, null);
 			colorLg.addCodeLine(ColorLegendInstruction[2], "Sentence2", 0, null);
 			lang.nextStep("Color Legend");
 			colorLgTitle.hide();
 			colorLg.hide();
 		}
 		
 		/**
 		 * This method shows the text content of the stack guide during the animation.
 		 * @param pos the content position in the animation panel
 		 */
 		public void showInstructionStack(Offset pos) {
 			
 			titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, lightRedBrown);
 			SourceCode stackGuide = lang.newSourceCode(pos, "stackGuide", null, guideProps);
 			SourceCode stackGuideTitle = lang.newSourceCode(pos, "stackTitle", null, titleProps);
 			
 			stackGuideTitle.addCodeLine(StackInstruction[0], "Title", 0, null);
 			
 			stackGuide.addCodeLine("", "Paragraph", 0, null);
 			stackGuide.addCodeLine("", "Paragraph", 0, null);
 			stackGuide.addCodeLine(StackInstruction[1], "Sentence1", 0, null);
 			stackGuide.addCodeLine(StackInstruction[2], "Sentence2", 0, null);
 			stackGuide.addCodeLine(StackInstruction[3], "Sentence3", 0, null);
 			stackGuide.addCodeLine(StackInstruction[4], "Sentence4", 0, null);
 			stackGuide.addCodeLine(StackInstruction[5], "Sentence5", 0, null);
 			stackGuide.addCodeLine(StackInstruction[6], "Sentence6", 0, null);
 			stackGuide.addCodeLine(StackInstruction[7], "Sentence7", 0, null);
 			stackGuide.addCodeLine(StackInstruction[8], "Sentence8", 0, null);
 			stackGuide.addCodeLine(StackInstruction[9], "Sentence9", 0, null);
 			lang.nextStep("Stack");
 			stackGuideTitle.hide();
 			stackGuide.hide();
 		}
 		
 		/**
 		 * This method shows the text content of the pseudo code guide during the animation.
 		 * @param pos the content position in the animation panel
 		 */
 		public void showInstructionPseudoCode(Offset pos) {
 			
 			titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, coral);
 			SourceCode psdoGuide = lang.newSourceCode(pos,  "code1Guide", null, guideProps);
 			SourceCode psdoGuideTitle = lang.newSourceCode(pos, "code1Title", null, titleProps);
 			
 			psdoGuideTitle.addCodeLine(PseudoCodeInstruction[0], "Title", 0, null);
 			
 			psdoGuide.addCodeLine("", "Paragraph", 0, null);
 			psdoGuide.addCodeLine("", "Paragraph", 0, null);
 			psdoGuide.addCodeLine(PseudoCodeInstruction[1], "Sentence1", 0, null);
 			psdoGuide.addCodeLine(PseudoCodeInstruction[2], "Sentence2", 0, null);
 			lang.nextStep("Pseudo Code");
 			psdoGuideTitle.hide();
 			psdoGuide.hide();
 		}
 		
 		/**
 		 * This method shows the text content of the source code guide during the animation.
 		 * @param pos the content position in the animation panel
 		 */
 		public void showInstructionSourceCode(Offset pos) {
 			
 			titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, dark_paleturquoise);
 			SourceCode srcGuide = lang.newSourceCode(pos, "code2Guide", null, guideProps);
 			SourceCode srcGuideTitle = lang.newSourceCode(pos, "code2Title", null, titleProps);
 			
 			srcGuideTitle.addCodeLine(SourceCodeInstruction[0], "Title", 0, null);
 			
 			srcGuide.addCodeLine("", "Paragraph", 0, null);
 			srcGuide.addCodeLine("", "Paragraph", 0, null);
 			srcGuide.addCodeLine(SourceCodeInstruction[1], "Sentence1", 0, null);
 			srcGuide.addCodeLine(SourceCodeInstruction[2], "Sentence2", 0, null);
 			srcGuide.addCodeLine(SourceCodeInstruction[3], "Sentence3", 0, null);
 			srcGuide.addCodeLine(SourceCodeInstruction[4], "Sentence3", 0, null);
 			srcGuide.addCodeLine("", "Paragraph", 0, null);
 			srcGuide.addCodeLine(SourceCodeInstruction[5], "Sentence4", 0, null);
 			lang.nextStep("Source Code");
 			srcGuideTitle.hide();
 			srcGuide.hide();
 			guidanceTitle.hide();
 		}
 		
 		/**
 		 * This method shows the actual pseudo code during the animation.
 		 * @param pos the pseudo code position in the animation panel
 		 * @return the pseudo code
 		 */
 		public SourceCode showPseudoCode(Offset pos) {
 			
 			// create Pseudo Mask
 			SourceCode psdoCode = lang.newSourceCode(pos, "pseudoCode", null, codeProps); // dynamic!
 			
 			// indentation lvl third argument
 			psdoCode.addCodeLine(PseudoCode[0], "Step1", 	 0, null);
 			psdoCode.addCodeLine(PseudoCode[1], "Step2", 	 0, null);
 			psdoCode.addCodeLine(PseudoCode[2], "Step2.1", 	 1, null);
 			psdoCode.addCodeLine(PseudoCode[3], "Step2.1.1", 2, null);
 			psdoCode.addCodeLine(PseudoCode[4], "Step2.1.2", 2, null);
 			psdoCode.addCodeLine(PseudoCode[5], "Step2.1.3", 2, null);
 			psdoCode.addCodeLine(PseudoCode[6], "Step2.1.4", 2, null);
 			psdoCode.addCodeLine(PseudoCode[7], "Step2.2",	 1, null);
 			psdoCode.addCodeLine(PseudoCode[8], "Step2.2.1", 2, null);
 			psdoCode.addCodeLine(PseudoCode[9], "Step2.2.2", 2, null);
 			psdoCode.addCodeLine(PseudoCode[10],"extra Step",0, null);
 			psdoCode.addCodeLine("", "Paragraph",0,null);
 			psdoCode.addCodeLine("", "Paragraph",0,null);
 			
 			//codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.ITALIC, 15));
 			//codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, hyperlinkColor);
 			psdoCode.addCodeLine(PseudoCode[11], "Source", 11, null);
 			
 			return psdoCode;
 		}
 		
 		/**
 		 * This method shows the actual source code during the animation.
 		 * @param pos the source code position in the animation panel
 		 * @return the source code
 		 */
 		public SourceCode showSourceCode(Offset pos) {

 			// create Source Mask
 			codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, hyperlinkColor);
 			codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
 			SourceCode srcCode = lang.newSourceCode(pos, "sourceCode", null, codeProps); // dynamic!
 			
 			
 			srcCode.addCodeLine(SourceCode[0], 	"method",	 	0, null);
 			srcCode.addCodeLine(SourceCode[1], 	"initCell", 	1, null);
 			srcCode.addCodeLine(SourceCode[2], 	"initCell_v", 	1, null);
 			srcCode.addCodeLine(SourceCode[3], 	"While1", 		1, null);
 			srcCode.addCodeLine(SourceCode[4], 	"randCell", 	2, null);
 			srcCode.addCodeLine(SourceCode[5], 	"If1", 			2, null);
 			srcCode.addCodeLine(SourceCode[6], 	"pushCell", 	3, null);
 			srcCode.addCodeLine(SourceCode[7], 	"betweenWall", 	3, null);
 			srcCode.addCodeLine(SourceCode[8], 	"removeWall", 	3, null);
 			srcCode.addCodeLine(SourceCode[9], 	"setCurrent", 	3, null);
 			srcCode.addCodeLine(SourceCode[10], "current_v",	3, null);
 			srcCode.addCodeLine(SourceCode[11], "updateRlist", 	3, null);
 			srcCode.addCodeLine(SourceCode[12], "ElseIf1", 		2, null);
 			srcCode.addCodeLine(SourceCode[13], "popCell", 		3, null);
 			srcCode.addCodeLine(SourceCode[14], "c_Brace", 		2, null);
 			srcCode.addCodeLine(SourceCode[15], "While2", 		1, null);
 			srcCode.addCodeLine(SourceCode[16], "popCell", 		2, null);
 			srcCode.addCodeLine(SourceCode[17], "c_Brace", 		1, null);
 			srcCode.addCodeLine(SourceCode[18], "c_Brace", 		0, null);
 			
 			return srcCode;
 			
 		}
 		
 		/**
 		 * This method shows the text content of the maze conclusion for the last slide.
 		 * @param pos the content position in the animation panel
 		 */
 		public void showConclusionMaze(Offset pos) {
 			
 			SourceCode outroMaze = lang.newSourceCode(pos, "mazeOutro", null, guideProps);
 			
 			outroMaze.addCodeLine(MazeConclusion[0], "Sentence1", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[1], "Sentence2", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[2], "Sentence3", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[3], "Sentence4", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[4], "Sentence5", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[5], "Sentence6", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[6], "Sentence7", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[7], "Sentence8", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[8], "Sentence9", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[9], "Sentence9", 0, null);
 			outroMaze.addCodeLine("", "Paragraph", 0, null);
 			outroMaze.addCodeLine(MazeConclusion[10],"Sentence10",0, null);
 			outroMaze.addCodeLine(MazeConclusion[11],"Sentence11",0, null);
 			outroMaze.addCodeLine(MazeConclusion[12],"Sentence12",0, null);
 			
 		}
 		
 		/**
 		 * This method just hides all titles for the conclusion slide.
 		 */
 		public void hideTitles() {
 			ivaStackTitle.hide();
 			stkLifo_Title.hide();
 			pseudoC_Title.hide();
 			sourceC_Title.hide();
 			clrChartTitle.hide();
 			gridMtx_Title.hide();
 		}
 	}

 	// =====================================================================
 	// Inner Class CreateContent
 	// =====================================================================

 	/**
 	 * This class creates all content in the animation panel.
 	 * From the actual visual maze, the color chart & support 
 	 * matrix till the fancy text content boxes.
 	 * @author David Berman
 	 *
 	 */
 	protected class CreateContent {
 		
 		// Language instance
 		private Language lang = RecursiveBacktrackingMazeGeneration.this.lang;	
 		
 		// =====================================================================
 		// Variables (@see Maze Settings)
 		// =====================================================================
 		
 		private int numCol;
 		private int numRow;
 		private int cellSize;
 		private int wallSize;
 		private int baseOffsetX, baseOffsetY;
 		private int pxlSizeError;
 		
 		private StringMatrix supportM;
 		
 		
 		// =====================================================================
 		// Properties
 		// =====================================================================
 		
 		// background properties
 		private RectProperties bgProps = new RectProperties() {{
 			set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3); // rear most position
 			set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
 			set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
 			set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
 		}};
 		
 		// guideBox properties
 		private RectProperties boxProps = new RectProperties() {{
 			set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
 			set(AnimationPropertiesKeys.FILL_PROPERTY,  Color.WHITE);
 			set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
 		}};
 		
 		// frame properties
 		private RectProperties frameProps = new RectProperties() {{
 			set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
 			set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
 		}};
 		
 		// matrix properties
 		private MatrixProperties mxProps = new MatrixProperties() {{
 			set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
 			set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table"); // style
 			
 			// Additional Properties
 			set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
 			set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
 		}};
 		
 		/**
 		 * The constructor of create content.
 		 */
 		public CreateContent() {
 			this.cellSize = RecursiveBacktrackingMazeGeneration.this.cellSize;
 			this.wallSize = RecursiveBacktrackingMazeGeneration.this.wallSize;
 			this.numCol = RecursiveBacktrackingMazeGeneration.this.numOfCellsCol;
 			this.numRow = RecursiveBacktrackingMazeGeneration.this.numOfCellsRow;
 			this.baseOffsetX = RecursiveBacktrackingMazeGeneration.this.baseOffsetX;
 			this.baseOffsetY = RecursiveBacktrackingMazeGeneration.this.baseOffsetY;
 			this.pxlSizeError = RecursiveBacktrackingMazeGeneration.this.pxlSizeError;
 		}
 		
 		/**
 		 * This method creates the actual maze board with initialized & unvisited cells.
 		 * @param pos the position in the animation panel
 		 */
 		public void createBoard(Node pos) {
 			// Offset sums
 			int x = wallSize;
 			int y = wallSize;
 			int id = 1;
 			int wallId = 1;
 			for(int i = 0; i < RecursiveBacktrackingMazeGeneration.this.totalColLength; ++i) {
 				if(i % 2 == 0) {
 					for(int j = 0; j < RecursiveBacktrackingMazeGeneration.this.totalRowLength; ++j) {
 						if(j % 2 == 0) {
 							//draw cell
 							Offset startCell = new Offset(x,y, pos, AnimalScript.DIRECTION_SE);
 							Offset endCell = new Offset(x + cellSize, y + cellSize, pos, AnimalScript.DIRECTION_SE);
 							Cell c = new Cell(id, startCell, endCell, State.UNVISITED, j, i);
 							RecursiveBacktrackingMazeGeneration.this.unvisitedCells.add(c);
 							RecursiveBacktrackingMazeGeneration.this.collection.add(c);
 							//drawing
 							c.initCell(RecursiveBacktrackingMazeGeneration.this.unvisited_Cell);
 							x += cellSize; //add
 							id++;
 						} else {
 							// draw vertical rectangle wall
 							Offset startWall = new Offset(x + pxlSizeError,y, pos, AnimalScript.DIRECTION_SE);
 							Offset endWall = new Offset(x + wallSize - pxlSizeError, y + cellSize, pos, AnimalScript.DIRECTION_SE);
 							//drawing
 							Wall w = new Wall(wallId, startWall, endWall, j, i);
 							RecursiveBacktrackingMazeGeneration.this.collection.add(w);
 							x += wallSize;
 							wallId++;
 						}
 					}
 					// y update
 				} else {
 					x = wallSize; // reset
 					y += cellSize + pxlSizeError; // pixelSizeError?
 					for(int j = 0; j < RecursiveBacktrackingMazeGeneration.this.totalRowLength; ++j) {
 						wallId++;
 						if(j % 2 == 0) {
 							// draw horizontal rectangle wall
 							Offset startWallSet = new Offset(x , y, pos, AnimalScript.DIRECTION_SE);
 							Offset endWallSet = new Offset(x + cellSize , y + wallSize, pos, AnimalScript.DIRECTION_SE);
 							//drawing
 							Wall w = new Wall(wallId, startWallSet, endWallSet, j, i);
 							RecursiveBacktrackingMazeGeneration.this.collection.add(w);
 							x += cellSize;
 						} else {
 							// drawing mini quad (fill hole) with pixelSizeError +1 ?
 							Offset startWallSet2 = new Offset(x + pxlSizeError, y, pos, AnimalScript.DIRECTION_SE);
 							Offset endWallSet2 = new Offset(x + wallSize - pxlSizeError, y + wallSize, pos, AnimalScript.DIRECTION_SE);
 							//drawing
 							Wall w = new Wall(wallId, startWallSet2, endWallSet2, j, i);
 							RecursiveBacktrackingMazeGeneration.this.collection.add(w);
 							x += wallSize;
 						}
 					}
 					y += wallSize + pxlSizeError;
 					x = wallSize; // reset
 					wallId++;
 				}
 			}
 		}
 		
 		/**
 		 * This method creates the background of the maze.
 		 * @param pos the position in the animation panel
 		 * @return the background represented by an AlgoAnim rectangle
 		 */
 		public Rect createBackGround(Node pos) {
 			Offset endCorner = new Offset(baseOffsetX, baseOffsetY, pos, AnimalScript.DIRECTION_SE);
 			
 			Rect base = lang.newRect(pos, endCorner, "BG", null, bgProps);
 			
 			return base;
 		}
 		
 		/**
 		 * This method creates an area for the text content in the box.
 		 * @param corners the corner coordinates
 		 * @param clr the color
 		 * @param name the name of the area
 		 * @param x the x coordinate
 		 * @param y the y coordinate
 		 * @return an area represented by an AlgoAnim rectangle
 		 */
 		public Rect createArea(int[] corners, Color clr, String name, int x, int y) {
 			Coordinates upperLeft = new Coordinates(x + corners[0], y + corners[1]);
 			Offset lowerRight = new Offset(corners[2], corners[3], upperLeft, AnimalScript.DIRECTION_SE);
 		
 			boxProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, clr);
 			
 			Rect area = lang.newRect(upperLeft, lowerRight, name , null, boxProps);
 			
 			return area;
 		}
 		
 		/**
 		 * This method creates a small frame for the box titles.
 		 * @param corners the corner coordinates
 		 * @param clr the color
 		 * @return a frame represented by an AlgoAnim rectangle
 		 */
 		public Rect createFrame(int[] corners, Color clr, String name, int x, int y) {
 			Coordinates upperLeft = new Coordinates(x + corners[0], y + corners[1]);
 		
 			frameProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, clr);
 			frameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, clr);
 			
 			Rect frame = lang.newRect(new Offset(0, -25, upperLeft, AnimalScript.DIRECTION_S),
 					new Offset(corners[2], 0, upperLeft, AnimalScript.DIRECTION_SE), name + "-TF", null, frameProps);
 			
 			return frame;
 		}
 		
 		/**
 		 * This method creates a support matrix, which is responsible for mirroring the maze.
 		 * @param pos the position in the animation panel
 		 * @return the support matrix represented by an AlgoAnim rectangle
 		 */
 		public StringMatrix createSupportMatrix(Node pos) {
 			int id = 1;
 			String[][] mirrorMaze = new String[numCol][numRow];
 			for (int i = 0; i < mirrorMaze.length; i++) {
 				for (int j = 0; j < mirrorMaze[i].length; j++) {
 					mirrorMaze[i][j] = "C"+String.format("%02d", id);
 					id++;
 				}
 			}
 			
 			supportM = lang.newStringMatrix(pos, mirrorMaze, "SupportMatrix", null, mxProps);
 			
 			return supportM;
 		}

 		
 		/**
 		 * This method creates & shows the color legend during the animation.
 		 * @param pos the position in the animation panel
 		 * @return the color legend matrix represented by an AlgoAnim rectangle
 		 */
 		public StringMatrix createColorLegend(Node pos) {
 			String[][] chart = new String[6][2];
 			String[] txt = {"Color", "Description", "", "unvisited Cell", "",
 					"chosen Cell", "", "visited Cell" , "", "current Cell", "", "Neighborhood "};
 			int counter = 0;
 			for (int i = 0; i < chart.length; i++) {
 				for (int j = 0; j < chart[i].length; j++) {
 					chart[i][j] = txt[counter];
 					counter++;
 				}
 			}

 			mxProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "left");
 			
 			StringMatrix colorM = lang.newStringMatrix(new Offset(0, (((numCol + 1) * 33) - numCol * pxlSizeError - (2 * pxlSizeError)), 
 					pos, AnimalScript.DIRECTION_SE), chart, "ColorChart", null, mxProps);
 			
 			// Set corresponding colors. TODO
 			colorM.setGridFillColor(1, 0, RecursiveBacktrackingMazeGeneration.this.unvisited_Cell_Color, null, null);
 			colorM.setGridFillColor(2, 0, RecursiveBacktrackingMazeGeneration.this.chosen_Cell_Color, null, null); 
 			colorM.setGridFillColor(3, 0, RecursiveBacktrackingMazeGeneration.this.visited_Cell_Color, null, null);
 			colorM.setGridFillColor(4, 0, RecursiveBacktrackingMazeGeneration.this.current_Cell_Color, null, null);
 			colorM.setGridFillColor(5, 0, RecursiveBacktrackingMazeGeneration.this.neighbor_Cells_Color, null, null); 
 			
 			return colorM;
 			
 		}
 			
 		/**
 		 * This method highlights the corresponding grid-cell of the support Matrix.
 		 * @param x the x-coordinate
 		 * @param y the y-coordinate
 		 * @param clr the color
 		 */
 		public void highlightM(int x, int y, Color clr) {
 			supportM.setGridFillColor(x-(x/2), y-(y/2), clr, null, null);
 		}
 		
 		/**
 		 * This method unhighlights the corresponding grid-cell of the support Matrix.
 		 * @param x the x-coordinate
 		 * @param y the y-coordinate
 		 */
 		public void unhighlightM(int x, int y) {
 			supportM.setGridFillColor(x-(x/2), y-(y/2), Color.WHITE, null, null);
 		}
 		 		
 	}

 	// =====================================================================
 	// Inner Enum State
 	// =====================================================================
 	
 	/**
 	 * This enumerator class handles all the states
 	 * a cell can achieve.
 	 * Available States: VISITED, UNVISITED, DEFAULT
 	 * @author David Berman
 	 *
 	 */
 	protected enum State {
 		// State Enum's.
 		VISITED(1), 
 		UNVISITED(2), 
 		DEFAULT(0); 

 		private int st; // state
 		
 		/**
 		 * This method just assigns the corresponding state.
 		 * @param st the state
 		 */
 		private State(int st) {
 			this.st = st;
 		}
 		
 		/**
 		 * Getter Method for State.
 		 * @return the state
 		 */
 		public int getState() {
 			return this.st;
 		}
 	}

 	// =====================================================================
 	// Inner Enum Orientation
 	// =====================================================================
 	
 	/**
 	 * This enumerator class handles all orientations in the maze.
 	 * Available orientations: TOP, DOWN, LEFT, RIGHT
 	 * @author David Berman
 	 * 
 	 */
 	protected enum Orientation {
 		// regardless of the orientation, cells are always a value of 2 apart,
 		// since walls are separating them.
 		TOP (0, -2), DOWN (0, 2),
 		LEFT (-2, 0), RIGHT (2, 0);
 		
 		public int dx,dy; // coordinates
 		
 		/**
 		 * This method just assigns the corresponding coordinates. 
 		 * @param dx the directional coordinate x
 		 * @param dy the directional coordinate y
 		 */
 		private Orientation(int dx, int dy) {
 			this.dx = dx;
 			this.dy = dy;
 		}
 		
 	}

}