package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Philipp Malkmus <p.malkmus@web.de>
 */
public class MazeKruskal implements ValidatingGenerator {
	
    static Color connectedCellColor = Color.WHITE;			
    static Color cellListHighlightColor = Color.CYAN;		
    int randomizerSeed;							
    static Color disconnectedCellColor = Color.BLACK;		
    int verticalSize;							
    static Color wallHighlightColor = Color.GREEN;			
    int cellSize; 								
    static boolean showWallIndex = false;					
    int horizontalSize;							
    static boolean showCellIndex = false;					
    static boolean useRandomizerSeed = false;				
    static Color cellHighlightColor = Color.CYAN;			
    static Color wallArrayHighlightColor = Color.GREEN;		
    static Color cellListProblemColor = Color.RED;			
    static Color wallColor = Color.BLACK;					
    static Color borderColor = Color.BLACK;					
   
    /**
	 * The concrete language object used for creating output
	 */
	public static Language lang;
	
	public void init(){
        lang = new AnimalScript("Kruskal Maze Generation", "Philipp Malkmus", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        Wall.counter = 0;
        CellSet.counter = 0;
    }
	
	static LinkedList<CellSet> cellSets;
	LinkedList<Wall> wallList;
	
	int tileSize;
	int hsize;
	int vsize;
	int numOfCells;
	
	int allWalls;
	int destroyedWalls;
	
	Font smallHeader = new Font("SansSerif", Font.BOLD, 25);
	int x = 50, y = 100;
	Coordinates start = new Coordinates(x, y);
	Rect base;

	StringArray wallArray;
	SourceCode longSC;
	SourceCode pseudoSC;

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        connectedCellColor = (Color)primitives.get("connectedCellColor");
        cellListHighlightColor = (Color)primitives.get("cellListHighlightColor");
        randomizerSeed = (Integer)primitives.get("randomizerSeed");
        disconnectedCellColor = (Color)primitives.get("disconnectedCellColor");
        verticalSize = (Integer)primitives.get("verticalSize");
        wallHighlightColor = (Color)primitives.get("wallHighlightColor");
        cellSize = (Integer)primitives.get("cellSize");
        showWallIndex = (Boolean)primitives.get("showWallIndex");
        useRandomizerSeed = (Boolean)primitives.get("useRandomizerSeed");
        horizontalSize = (Integer)primitives.get("horizontalSize");
        showCellIndex = (Boolean)primitives.get("showCellIndex");
        cellHighlightColor = (Color)primitives.get("cellHighlightColor");
        wallArrayHighlightColor = (Color)primitives.get("wallArrayHighlightColor");
        cellListProblemColor = (Color)primitives.get("cellListProblemColor");
        wallColor = (Color)primitives.get("wallColor");
        borderColor = (Color)primitives.get("borderColor");
                
        tileSize = cellSize/2;
    	hsize = horizontalSize;
    	vsize = verticalSize;
    	numOfCells = hsize * vsize;
    	q_destroy_counter = 0;
    	q_notDestroy_counter = 0;
    	allWalls = 0;
    	destroyedWalls = 0;
        
        start();
        lang.finalizeGeneration();
        
        return lang.toString();
    }
	
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		if((Integer)primitives.get("verticalSize") > 1
				&& (Integer)primitives.get("horizontalSize") > 1
				&& (Integer)primitives.get("cellSize") > 1)
			return true;

		return false;
	}
	
	public MazeKruskal() {
	}

	private static final String DESCRIPTION = 
			"The Kruskal Maze Generation algorithm generates (as the name would imply) a maze using a randomized Kruskal's algorithm approach."
					+"\n"
	        		+"Every cell of the maze starts in its own list. As the algorithm progresses it selects a random wall each iteration and checks the lists of its adjacent cells."
	        		+"\n"
	        		+"If both cells are in different lists, the lists will combine and the wall will be destroyed. This step will be skipped if both cells are already in the same list."
	        		+"\n"
	        		+"This continues until all cells are in one list and a maze connecting every cell without any loops is created.";

	private static final String SOURCE_CODE = 
			"public void kruskal(){"
					+"\n"
	        		+"		"
	        		+"\n"
	        		+"	Random rng = new Random();"
	        		+"\n"
	        		+"	while(getSizeOfBiggestSet(cellSets) != numOfCells){"
	        		+"\n"
	        		+"		// select a random wall"
	        		+"\n"
	        		+"		int rando = rng.nextInt(wallList.size());"
	        		+"\n"
	        		+"		Wall chosenWall = wallList.get(rando);"
	        		+"\n"
	        		+"		"
	        		+"\n"
	        		+"		// check if the cells divided by this wall belong to distinct sets"
	        		+"\n"
	        		+"		if(!chosenWall.getCell1().getCellSet().equals(chosenWall.getCell2().getCellSet())){"
	        		+"\n"
	        		+"			// if yes"
	        		+"\n"
	        		+"			// remove the wall in the maze"
	        		+"\n"
	        		+"			wallList.remove(chosenWall);"
	        		+"\n"
	        		+"			chosenWall.destroy();"
	        		+"\n"
	        		+"			"
	        		+"\n"
	        		+"			// join the sets of the formerly divided cells"
	        		+"\n"
	        		+"			chosenWall.getCell1().getCellSet().combine(chosenWall.getCell2().getCellSet());"
	        		+"\n"
	        		+"				"
	        		+"\n"
	        		+"		}else{"
	        		+"\n"
	        		+"			// else exclude the wall from the list of selectable walls"
	        		+"\n"
	        		+"			wallList.remove(chosenWall);"
	        		+"\n"
	        		+"		}"
	        		+"\n"
	        		+"	}	"
	        		+"\n"
	        		+"}";

	
	private int q_destroy_counter = 0;
	private int q_notDestroy_counter = 0;
	private final String QUESTION = "The selected wall will be destroyed?";
	private final String ANSWER1 = "The cells connected to the wall are not yet connected over another path. Both cells also are not in the same cell set. Therefor the wall has to be destroyed and the cell sets have to be combined.";
	private final String ANSWER2 = "The cells connected to the wall are already connected over another path. Both cells also are already in the same cell set. Therefor the wall will not be destroyed.";

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	public String getName() {
        return "Kruskal Maze Generation";
    }
	
	public String getAlgorithmName() {
        return "Kruskal Maze Generation";
    }
	
	public String getAnimationAuthor() {
        return "Philipp Malkmus";
    }

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}
	
	public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
	
	public void createSourceCode(){

		// visual properties for the pseudo source code
		SourceCodeProperties pseudoScProps = new SourceCodeProperties();
		pseudoScProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		pseudoScProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 15));
		
		pseudoScProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		pseudoScProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		// source code entities	
		int offset = 90;
		if(vsize*tileSize*4 > offset)
			offset = 40;
		pseudoSC = lang.newSourceCode(new Offset(0, offset, base, AnimalScript.DIRECTION_SW), "pseudoSourceCode", null, pseudoScProps);
		
		// Add the lines to the SourceCode object
		// Line, name, indentation, display delay
		pseudoSC.addCodeLine("1. Create a list of all walls, and create a set for each cell, each containing just that one cell.", null, 0, null); //0
		pseudoSC.addCodeLine("2. Select a random wall until one cell set is complete: ", null, 0, null); //1
		pseudoSC.addCodeLine("1. If the cells divided by this wall belong to distinct sets: ", null, 2, null); //2
		pseudoSC.addCodeLine("1. Remove the current wall.", null, 4, null); //3
		pseudoSC.addCodeLine("2. Join the sets of the formerly divided cells.", null, 4, null); //4
		pseudoSC.addCodeLine("2. Else if the cells already belong to the same set: ", null, 2, null); //5
		pseudoSC.addCodeLine("1. Exclude the wall from the list of selectable walls.", null, 4, null); //6

		// visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		// source code entity
		longSC = lang.newSourceCode(new Offset(0, 40, pseudoSC, AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
		
		// Add the lines to the SourceCode object
		// Line, name, indentation, display delay
		longSC.addCodeLine("public void kruskal()", null, 0, null); // 0
		longSC.addCodeLine("{", null, 0, null); // 1
		longSC.addCodeLine("Random rng = new Random();", null, 1, null); // 2
		longSC.addCodeLine("while(getSizeOfBiggestSet(cellSets) != numOfCells)", null, 1, null); // 3
		longSC.addCodeLine("{", null, 1, null); // 4
		longSC.addCodeLine("int rando = rng.nextInt(wallList.size());", null, 2, null); // 5
		longSC.addCodeLine("Wall chosenWall = wallList.get(rando);", null, 2, null); // 6
		longSC.addCodeLine("if(!chosenWall.getCell1().getCellSet().equals(chosenWall.getCell2().getCellSet()))", null, 2, null); // 7
		longSC.addCodeLine("{", null, 2, null); // 8
		longSC.addCodeLine("wallList.remove(chosenWall);", null, 3, null); // 9
		longSC.addCodeLine("chosenWall.destroy();", null, 3, null); // 10
		longSC.addCodeLine("chosenWall.getCell1().getCellSet().combine(chosenWall.getCell2().getCellSet());", null, 3, null); // 11
		longSC.addCodeLine("} else {", null, 2, null); // 12
		longSC.addCodeLine("wallList.remove(chosenWall);", null, 3, null); // 13
		longSC.addCodeLine("}", null, 2, null); // 14
		longSC.addCodeLine("}", null, 1, null); // 15
		longSC.addCodeLine("}", null, 0, null); // 16
	}	
		
	public int getSizeOfBiggestSet(LinkedList<CellSet> ll){
		int result = 0;
		for(CellSet cs: ll){
			if(cs.size() > result)
				result = cs.size();
		}
		return result;
	}
	
	public void createLayout(){

		wallList = new LinkedList<>();
		cellSets = new LinkedList<>();
		
		// for every cell
		for(int i = 0; i < numOfCells; i++){
			//calculate the cell position
			Offset cellStart = new Offset(tileSize + (i % hsize) * tileSize * 4, tileSize + (i / hsize) * tileSize * 4, start, AnimalScript.DIRECTION_SE);
			
			// create new cell
			Cell newCell = new Cell(i, cellStart, tileSize * 2);
			cellSets.add(newCell.getCellSet());
			
			// create new walls
			// left wall if it exists
			if(newCell.hasLeftNeighbor(hsize)){
				Offset wallStart = new Offset(tileSize + (i % hsize) * tileSize * 4 - tileSize * 2, tileSize + (i / hsize) * tileSize * 4, start, AnimalScript.DIRECTION_SE);
				Wall newWall1 = new Wall((i-1) + " " + i, cellSets.get(i-1).element(), newCell, wallStart, tileSize * 2);
				wallList.add(newWall1);
			}
			// top wall if it exists
			if(newCell.hasTopNeighbor(hsize)){
				Offset wallStart = new Offset(tileSize + (i % hsize) * tileSize * 4, tileSize + (i / hsize) * tileSize * 4 - tileSize * 2, start, AnimalScript.DIRECTION_SE);
				Wall newWall2 = new Wall((i-hsize) + " " + i, cellSets.get(i-hsize).element(), newCell, wallStart, tileSize * 2);
				wallList.add(newWall2);
			}
		}	
		allWalls = wallList.size();
	}
	
	public void createGrid(){
				
		Offset end = new Offset(4 * tileSize * hsize, 4 * tileSize * vsize, start, AnimalScript.DIRECTION_SE);
		
		RectProperties props = new RectProperties();
		props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
		props.set(AnimationPropertiesKeys.COLOR_PROPERTY, borderColor);
		props.set(AnimationPropertiesKeys.FILL_PROPERTY, borderColor);
		props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		base = lang.newRect(start, end, "Base", null, props);
		
		createLayout();		
	}
	
	public void createWallArray(){
		
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, smallHeader);
		Text wallListText = lang.newText(new Offset(30, 0, base, AnimalScript.DIRECTION_NE), "List of all Walls", "Wall List Text", null, textProps);
		
		String[] wallStrings = new String[wallList.size()];
		int counter = 0;
		for(Wall w: wallList){
			wallStrings[counter] = w.getId();
			counter++;
		}
		
		ArrayProperties props = new ArrayProperties();
		props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		props.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, MazeKruskal.wallArrayHighlightColor);
	
		wallArray = lang.newStringArray(
				new Offset(0, 20, wallListText, AnimalScript.DIRECTION_SW), 
				wallStrings, 
				"Wall Array", 
				null,
				props);	
	}
	
	public void createCellSets(){
		
		int offset = 700;
		if(hsize*tileSize*4 > offset)
			offset = hsize*tileSize*4;
		
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, smallHeader);
		lang.newText(new Coordinates(x + offset + 30, y + 140), "List of all Cell Sets", "Cell Set Text", null, textProps);
		
		Coordinates c = new Coordinates(x + offset + 30, y + 200);
		
		for(CellSet cl: cellSets){
			cl.draw(c);
			c = new Coordinates(c.getX(), c.getY()+28);
		}
		
	}
	
	public void kruskal(){
		
		Random rng;
		if(useRandomizerSeed)
			rng = new Random(randomizerSeed);
		else
			rng = new Random();
		while(getSizeOfBiggestSet(cellSets) != numOfCells){
			// select random wall
			
			pseudoSC.highlight(1);
			longSC.highlight(2);
			longSC.highlight(3);
			longSC.highlight(5);
			longSC.highlight(6);
			lang.nextStep();
			
			int rando = rng.nextInt(wallList.size());
			Wall chosenWall = wallList.get(rando);
			
			wallArray.highlightCell(chosenWall.getNumber(), null, null);
			chosenWall.highlightCells();			
			lang.nextStep();
			
			pseudoSC.unhighlight(1);
			longSC.unhighlight(2);
			longSC.unhighlight(3);
			longSC.unhighlight(5);
			longSC.unhighlight(6);
			pseudoSC.highlight(2);
			longSC.highlight(7);
			lang.nextStep();
			
			if(chosenWall.getCell1().getCellSet().equals(chosenWall.getCell2().getCellSet()))
				chosenWall.getCell1().getCellSet().getGrafic().setHighlightFillColor(0, chosenWall.getCell1().getCellSet().size() - 1, cellListProblemColor, null, null);
			chosenWall.getCell1().getCellSet().highlight();
			chosenWall.getCell2().getCellSet().highlight();
			lang.nextStep();

			// check if the cells divided by this wall belong to distinct sets
			if(!chosenWall.getCell1().getCellSet().equals(chosenWall.getCell2().getCellSet())){
				
				pseudoSC.unhighlight(2);
				longSC.unhighlight(7);
				pseudoSC.highlight(3);
				longSC.highlight(9);
				longSC.highlight(10);
				
				if(q_destroy_counter < 3 && rng.nextInt(wallList.size()) >= 0.7f * wallList.size()){
					TrueFalseQuestionModel question = new TrueFalseQuestionModel("QuestionWall " + chosenWall.getId(), true, 1);
					question.setPrompt(QUESTION);
					question.setFeedbackForAnswer(true, ANSWER1);
					q_destroy_counter++;
					lang.addTFQuestion(question);
				}
								
				lang.nextStep();
				
				// if yes
				// remove the wall
				wallList.remove(chosenWall);
				chosenWall.destroy();
				destroyedWalls++;
				
				chosenWall.unhighlightCells();
				wallArray.unhighlightCell(chosenWall.getNumber(), null, null);
				wallArray.setFillColor(chosenWall.getNumber(), Color.GRAY, null, null);
				lang.nextStep();
				
				pseudoSC.unhighlight(3);
				longSC.unhighlight(9);
				longSC.unhighlight(10);
				pseudoSC.highlight(4);
				longSC.highlight(11);
				lang.nextStep();
				
				// join the sets of the formerly divided cells
				if(chosenWall.getCell1().getCellSet().getNumber() <= chosenWall.getCell2().getCellSet().getNumber()){
					chosenWall.getCell1().getCellSet().combine(chosenWall.getCell2().getCellSet());
				} else {
					chosenWall.getCell2().getCellSet().combine(chosenWall.getCell1().getCellSet());
				}
				chosenWall.getCell1().getCellSet().highlight();
				lang.nextStep();

				chosenWall.getCell1().getCellSet().unhighlight();
				pseudoSC.unhighlight(4);
				longSC.unhighlight(11);
								
			}else{
				
				if(q_notDestroy_counter < 2 && rng.nextInt(wallList.size()) >= 0.5f * wallList.size()){
					TrueFalseQuestionModel question = new TrueFalseQuestionModel("QuestionWall " + chosenWall.getId(), false, 1);
					question.setPrompt(QUESTION);
					question.setFeedbackForAnswer(true, ANSWER2);
					q_notDestroy_counter++;
					lang.addTFQuestion(question);
				}
				
				pseudoSC.unhighlight(2);
				longSC.unhighlight(7);
				pseudoSC.highlight(5);
				longSC.highlight(12);
				lang.nextStep();
				
				chosenWall.unhighlightCells();			
				pseudoSC.unhighlight(5);
				longSC.unhighlight(12);
				pseudoSC.highlight(6);
				longSC.highlight(13);
				lang.nextStep();
				
				wallList.remove(chosenWall);
				wallArray.unhighlightCell(chosenWall.getNumber(), null, null);
				wallArray.setFillColor(chosenWall.getNumber(), Color.GRAY, null, null);
				pseudoSC.unhighlight(6);
				longSC.unhighlight(13);
				
				chosenWall.getGrafic().changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.borderColor, null, null);
				chosenWall.getGrafic().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.borderColor, null, null);
				chosenWall.getCell1().getCellSet().unhighlight();
				chosenWall.getCell1().getCellSet().getGrafic().setFillColor(0, chosenWall.getCell1().getCellSet().size() - 1, Color.WHITE, null, null);
				chosenWall.getCell1().getCellSet().getGrafic().setHighlightFillColor(0, chosenWall.getCell1().getCellSet().size() - 1, MazeKruskal.cellListHighlightColor, null, null);	
				lang.nextStep();
			}
		}
		for(Wall w: wallList){
			w.getGrafic().changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.borderColor, null, null);
			w.getGrafic().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.borderColor, null, null);
		}
	}
	

	public void start() {
		
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 30));
		Text t = lang.newText(new Coordinates(50, 25), "Randomized Kruskal Maze Generation", "Header", null, textProps);
		
		SourceCodeProperties textProps2 = new SourceCodeProperties();
		textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 26));
		SourceCode desc = lang.newSourceCode(new Offset(0, 50, t, AnimalScript.DIRECTION_SW), "Description", null, textProps2);

		desc.addCodeLine("The Kruskal Maze Generation algorithm generates (as the name would imply) ", null, 0, null);
		desc.addCodeLine("a maze using a randomized Kruskal's algorithm approach.", null, 0, null);
		desc.addCodeLine("Every cell of the maze starts in its own list. As the algorithm progresses ", null, 0, null);
		desc.addCodeLine("it selects a random wall each iteration and checks the lists of its adjacent cells.", null, 0, null);
		desc.addCodeLine("If both cells are in different lists, the lists will combine and the wall will be destroyed. ", null, 0, null);
		desc.addCodeLine("This step will be skipped if both cells are already in the same list.", null, 0, null);
		desc.addCodeLine("This continues until all cells are in one list and a maze connecting every cell ", null, 0, null);
		desc.addCodeLine("without any loops is created.", null, 0, null);
		
		lang.nextStep();
		desc.hide();
		
		createGrid();
		createSourceCode();
		
		lang.nextStep();
		pseudoSC.highlight(0);
		
		lang.nextStep();
		createWallArray();
		
		lang.nextStep();
		createCellSets();
		
		lang.nextStep();
		pseudoSC.unhighlight(0);
			
		kruskal();
		
		lang.nextStep();
		
		Text t2 = lang.newText(new Offset(0, 90, "Cell Set Text", AnimalScript.DIRECTION_SW), "Facit:", "FacitHeader", null, textProps);
		
		SourceCodeProperties textProps3 = new SourceCodeProperties();
		textProps3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 22));
		SourceCode facit = lang.newSourceCode(new Offset(0, 5, t2, AnimalScript.DIRECTION_SW), "Facit", null, textProps3);
		
		facit.addCodeLine("As we saw, the algorithm created a random maze.", null, 0, null);
		facit.addCodeLine("This maze connects every cell without any loop.", null, 0, null);
		facit.addCodeLine("The algorithm checked a total of " + (allWalls - wallList.size()) + " out of " + allWalls + " walls,", null, 0, null);
		facit.addCodeLine(destroyedWalls + " of which were destroyed, leaving " + (allWalls - destroyedWalls) + " walls behind.", null, 0, null);
		facit.addCodeLine("Unlike the actual Kruskal's Algorithm the randomized variant does ", null, 0, null);
		facit.addCodeLine("not use a sorting algorithm to choose the next edge (wall, in our case).", null, 0, null);
		facit.addCodeLine("Therefor the complexity of the algorithm is O(n),", null, 0, null);
		facit.addCodeLine("where n is the number of checked walls (iterations).", null, 0, null);
		facit.addCodeLine("The worst case is checking every wall of the maze and", null, 0, null);
		facit.addCodeLine("the best case is checking C-1 walls, where C is the number of cells.", null, 0, null);

		//System.out.println(l);
		//lang.writeFile("B:/Benutzer/Philipp/Documents/Uni/VAD/Animal/Kruskal Maze Konzept.asu");		
	}
}
