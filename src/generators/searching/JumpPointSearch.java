/*
 * JumpPointSearch.java
 * Christian Kunz, Jonas Winter, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;

import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.animalscript.addons.*;

//import translator.TranslatableGUIElement;
import translator.Translator;

public class JumpPointSearch implements ValidatingGenerator {
	private Locale locale;
	private Translator translator;
	
	private Language lang;

	private SquareProperties Maze_tile_start_properties;
	private SquareProperties Maze_tile_jumpPoint_properties;
	private SquareProperties Maze_tile_free_properties;
	private SquareProperties Maze_tile_end_properties;
	private PolylineProperties Maze_trail_line_properties;
	private SquareProperties Maze_tile_trail_properties;
    private SourceCodeProperties sourceCode_properties;
    private SourceCodeProperties infoTextProperties;
	private CounterProperties counter_Properties;
	private int[][] Maze;
	private int[][] newMaze;
	private SquareProperties Maze_tile_foundEnd_properties;
	private SquareProperties Maze_tile_blocked_properties;
	private int Maze_tile_size;
	private boolean Question_useage;
	private boolean Highlight_sourcecode;

	private JPSMaze maze;
	private JPSNode cur;
	ArrayList<JPSNode> trail;
	private JPSNode[] successors, possibleSuccess;
	private int[][] neighbors;
	int[] tmpXY;
	float ng;
	private SourceCode sourceCode;
	private InfoBox heapBox;
	private List<String> heapBoxText;
	private InfoBox curBox;
	private List<String> curBoxText;
	private InfoBox pathBox;
	private List<String> pathBoxText;
	private int questionCounter = 0;

	private Variables var;

	private SourceCode introText;
	private SourceCode outroText;
	
	private int sourceCodeSize;
	
	String varCurrent;
	String varJumpPointCounter;
	String varNeccesaryJumpPoints;
	
	public JumpPointSearch(Locale l) {
		locale = l;
		translator = new Translator("resources/JPSTranslatorFile", l);
	}

	public void init() {
		lang = new AnimalScript("Jump Point Search", "Christian Kunz, Jonas Winter", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		var = lang.newVariables();
		
		this.varCurrent = this.translator.translateMessage("varCurrent");
		var.declare("string", this.varCurrent);
		
		this.varJumpPointCounter = this.translator.translateMessage("varJumpPointCounter");
		var.declare("int", this.varJumpPointCounter, "0");
		
		this.varNeccesaryJumpPoints = this.translator.translateMessage("varNeccesaryJumpPoints");
		var.declare("int", this.varNeccesaryJumpPoints);

	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		//TranslatableGUIElement guiBuilder = translator.getGenerator();
		
		infoTextProperties = getInfoTextProperties();
		Maze_tile_start_properties = (SquareProperties) props.getPropertiesByName("Maze_tile_start_properties");
		Maze_tile_jumpPoint_properties = (SquareProperties) props.getPropertiesByName("Maze_tile_jumpPoint_properties");
		Maze_tile_free_properties = (SquareProperties) props.getPropertiesByName("Maze_tile_free_properties");
		Maze_tile_end_properties = (SquareProperties) props.getPropertiesByName("Maze_tile_end_properties");
		Maze_trail_line_properties = (PolylineProperties) props.getPropertiesByName("Maze_trail_line_properties");
		Maze_tile_trail_properties = (SquareProperties) props.getPropertiesByName("Maze_tile_trail_properties");
		sourceCode_properties = (SourceCodeProperties) props.getPropertiesByName("sourceCode_properties");
		Maze = (int[][]) primitives.get("Maze");
		Maze_tile_foundEnd_properties = (SquareProperties) props.getPropertiesByName("Maze_tile_foundEnd_properties");

		Maze_tile_blocked_properties = (SquareProperties) props.getPropertiesByName("Maze_tile_blocked_properties");
		Maze_tile_size = (Integer) primitives.get("Maze_tile_size");
		Question_useage = (boolean) primitives.get("Question_useage");
		Highlight_sourcecode = (boolean) primitives.get("Highlight_sourcecode");
		counter_Properties = new CounterProperties();
		counter_Properties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		counter_Properties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		
        introText = getInfo("intro");
        outroText = getInfo("outro");
        sourceCodeSize = 0;

		visualizeJPSAlgo();

		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return this.translator.translateMessage("title");
	}

	public String getAlgorithmName() {
		return this.translator.translateMessage("algorithmName");
	}

	public String getAnimationAuthor() {
		return "Christian Kunz, Jonas Winter";
	}

    public String getDescription() {
    	String descriptionString = "";
    	
    	for (String descLine : getInfoLines("intro")) {
    		descriptionString = descriptionString + descLine;
    		descriptionString = descriptionString + "\n";
    	}
        return descriptionString;
    }

    public String getCodeExample(){
    	String sourceCodeString = "";
    	
    	for (JPSCodeLine codeLine : getSourceCodeLines()) {
    		for (int i = 0; i < codeLine.getIndentation(); i++) {
    			sourceCodeString = sourceCodeString + "    ";
    		}
    		sourceCodeString = sourceCodeString + codeLine.getCode();
    		sourceCodeString = sourceCodeString +"\n";
    	}
        return sourceCodeString;
    }

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		// check for illegal numbers in the matrix
		Maze = (int[][]) primitives.get("Maze");
		int startCounter = 0;
		int endCounter = 0;
		for (int i = 0; i < Maze.length; i++) {
			for (int j = 0; j < Maze[i].length; j++) {
				if (Maze[i][j] == 2) {
					startCounter += 1;
				}
				if (Maze[i][j] == 3) {
					endCounter += 1;
				}
				if (Maze[i][j] < 0 || Maze[i][j] > 3) {
					return false;
				}
			}
		}
		if (startCounter == 1 && endCounter == 1) {
			return true;
		}

		return false;
	}

	private SourceCodeProperties getInfoTextProperties() {
		SourceCodeProperties infoTextProperties = new SourceCodeProperties();
		infoTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		infoTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		return infoTextProperties;
	}

	//// JPS CODE ////

	private void visualizeJPSAlgo() {
		showTitle();
		showStartPage();

		initializeMaze();

		showTitle();
		showSource();
		showHeapBox();
		showCurrentBox();
		showPathBox();

		heapBox.show();
		curBox.show();

		lang.nextStep();

		jumpPointSearch(Maze_tile_size);



		heapBox.hide();
		curBox.hide();
		pathBox.show();

		lang.nextStep();
		showEndPage();

	}

	private void showStartPage() {
		introText.show();
		lang.nextStep();

		introText.hide();
	}

    private SourceCode getInfo(String infoLineType) {
    	SourceCode info = lang.newSourceCode(new Coordinates(50, 80), infoLineType, null, infoTextProperties);
    	
    	for (String line : getInfoLines(infoLineType)) {
    		info.addCodeLine(line, null, 0, null);
    	}
    	info.hide();
    	return info;
    }
    
    private ArrayList<String> getInfoLines(String infoLineType) {
    	ArrayList<String> lines = new ArrayList<String>() {
    		private static final long serialVersionUID = 1L;
    	};
    	
    	int currentLine = 0;
		while (true) {
			currentLine++;
			String l = this.translator.translateMessage(infoLineType + "Line" + currentLine, null, false);
			if (!l.contains("Invalid Message Key")) {
				lines.add(l);
			}
			else {
				break;
			}
		}
    	return lines;
    }

	private ArrayList<JPSCodeLine> getSourceCodeLines(){
		ArrayList<JPSCodeLine> sourceCodeLines = new ArrayList<JPSCodeLine>() {
			private static final long serialVersionUID = 1L;
		};
		
		ArrayList<String> codeLines = getInfoLines("code");
		
		for (int i = 0; i < codeLines.size(); i ++) {
			sourceCodeLines.add(new JPSCodeLine(codeLines.get(i), Integer.parseInt(this.translator.translateMessage("clIndentation" + (i + 1), null, false))));
		}
		
		return sourceCodeLines;
	}
    
	private void showEndPage() {

		lang.hideAllPrimitives();
		sourceCode.hide();
		showTitle();
		outroText.show();
		lang.nextStep();

		outroText.hide();
	}

	private JPSMaze initializeMaze() {
		newMaze = flipMatrix(turnMatrixRight(Maze));
		maze = new JPSMaze(Maze_tile_size, newMaze);

		SquareProperties sProps = Maze_tile_free_properties;
		SquareProperties sStartProps = Maze_tile_start_properties;
		SquareProperties sEndProps = Maze_tile_end_properties;
		SquareProperties sBlockedProps = Maze_tile_blocked_properties;

		for (int i = 0; i < maze.maxSizeX; i++) {
			for (int j = 0; j < maze.maxSizeY; j++) {
				Node node = maze.maze[i][j].getNode();
				if (maze.maze[i][j].isStart()) {
					lang.newSquare(node, Maze_tile_size, "x" + i + "y" + j, null, sStartProps);

				} else if (maze.maze[i][j].isEnd()) {
					lang.newSquare(node, Maze_tile_size, "x" + i + "y" + j, null, sEndProps);

				} else if (!maze.maze[i][j].isWalkable()) {
					lang.newSquare(node, Maze_tile_size, "x" + i + "y" + j, null, sBlockedProps);

				} else {
					lang.newSquare(node, Maze_tile_size, "x" + i + "y" + j, null, sProps);

				}

			}
		}

		return maze;

	}

	private void showTitle() {
		TextProperties titleProperties = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 30));
		titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		lang.newText(new Coordinates(10, 10), getAlgorithmName(), "Title", null, titleProperties);

	}

	private void showSource() {
		sourceCode = getSourceCode();
		sourceCode.show();
	}

	private SourceCode getSourceCode() {
		SourceCode sc = lang.newSourceCode(new Coordinates((maze.maxSizeX * Maze_tile_size) + 210, 50), "SourceCode", 
				null, sourceCode_properties);
		
		int lineCounter = 0;
		for (JPSCodeLine codeLine : getSourceCodeLines()) {
			sc.addCodeLine(codeLine.getCode(), null, codeLine.getIndentation(), null);
			lineCounter++;
		}
		sc.hide();
		this.sourceCodeSize = lineCounter;
		return sc;
	}

	private void showHeapBox() {
		heapBox = new InfoBox(lang, new Coordinates((maze.maxSizeX * Maze_tile_size) + 10, 90), 40, "Heap");
		heapBox.hide();
		heapBoxText = new ArrayList<String>();
	}

	private void showCurrentBox() {
		curBox = new InfoBox(lang, new Coordinates((maze.maxSizeX * Maze_tile_size) + 10, 60), 40, "Current");
		curBox.hide();
		curBoxText = new ArrayList<String>();
	}

	private void showPathBox() {
		pathBox = new InfoBox(lang, new Coordinates((maze.maxSizeX * Maze_tile_size) + 10, 60), 40, "Path");
		pathBox.hide();
		pathBoxText = new ArrayList<String>();
	}

	private void sourceCodeHighlighter(int sequence) {

		if (Highlight_sourcecode) {
			unhighlightAllSourceCode();
			switch (sequence) {
			case 0:
				
				sourceCode.highlight(0);
				sourceCode.highlight(1);
				sourceCode.highlight(2);
				lang.nextStep();
				break;
			case 1:
				sourceCode.highlight(4);
				lang.nextStep();
				break;
			case 2:
				sourceCode.highlight(5);
				lang.nextStep();
				break;
			case 3:
				sourceCode.highlight(6);
				sourceCode.highlight(7);
				lang.nextStep();
				break;
			case 4:
				sourceCode.highlight(9);
				sourceCode.highlight(10);
				sourceCode.highlight(11);
				lang.nextStep();
				break;
			case 5:
				sourceCode.highlight(12);
				sourceCode.highlight(21);
				sourceCode.highlight(22);
				sourceCode.highlight(23);
				sourceCode.highlight(24);
				sourceCode.highlight(25);
				sourceCode.highlight(26);
				sourceCode.highlight(27);
				sourceCode.highlight(28);
				sourceCode.highlight(29);
				sourceCode.highlight(30);
				sourceCode.highlight(31);
				sourceCode.highlight(32);
				lang.nextStep();
				break;
			case 6:
				sourceCode.highlight(13);
				sourceCode.highlight(14);
				sourceCode.highlight(15);
				lang.nextStep();
				break;
			case 7:
				sourceCode.highlight(16);
				sourceCode.highlight(17);
				sourceCode.highlight(18);

				break;

			default:
			}
		}
	}

	private void unhighlightAllSourceCode() {
		for (int i = 0; i < sourceCodeSize; i++) {
			sourceCode.unhighlight(i);
		}
	}

	private void jumpPointSearch(int rectSize) {

		sourceCodeHighlighter(0);

		SquareProperties sVisitProps = Maze_tile_jumpPoint_properties;
		SquareProperties sTraceProps = Maze_tile_trail_properties;
		SquareProperties sFoundEndProps = Maze_tile_foundEnd_properties;
		PolylineProperties pProps = Maze_trail_line_properties;
		maze.getNode(this.getStartNode(maze).x, this.getStartNode(maze).y).updateGHFP(0, 0, null);
		maze.heapAdd(maze.getNode(this.getStartNode(maze).x, this.getStartNode(maze).y));

		heapBoxText.add("x:" + this.getStartNode(maze).x + " y:" + this.getStartNode(maze).y + " Dist:"
				+ this.getStartNode(maze).f);
		heapBox.setText(heapBoxText);

		int endX = this.getEndNode(maze).x;
		int endY = this.getEndNode(maze).y;

		QuestionGroupModel group = new QuestionGroupModel("possibleSuccessorsGroup", 3);
		lang.addQuestionGroup(group);

		var.set(varJumpPointCounter, Integer.toString(0));

		int oldJPC = 0;

		while (true) {
			sourceCodeHighlighter(1);

			cur = maze.heapPopNode(); // the current node is removed from the heap.

			LinkedList<float[]> heap = maze.getHeap();
			heapBoxText.clear();
			for (int i = 0; i < heap.size(); i++) {
				float[] node = heap.get(i);
				heapBoxText.add("x:" + (int) node[0] + " y:" + (int) node[1] + " Dist:" + node[2]);

			}
			heapBox.setText(heapBoxText);
			curBoxText.clear();
			curBoxText.add("x:" + cur.x + " y:" + cur.y + " Dist:" + cur.f);
			curBox.setText(curBoxText);
			var.set(varCurrent, "x:" + cur.x + " y:" + cur.y + " Dist:" + cur.f);

			oldJPC = Integer.parseInt(var.get(varJumpPointCounter));
			var.set(varJumpPointCounter, Integer.toString(1 + oldJPC));

			sourceCodeHighlighter(2);
			if (!cur.start) {
				if (cur.end) {
					sourceCodeHighlighter(3);
					lang.newSquare(cur.getNode(), rectSize, "x" + cur.x + "y" + cur.y, null, sFoundEndProps);

				} else {
					lang.newSquare(cur.getNode(), rectSize, "x" + cur.x + "y" + cur.y, null, sVisitProps);

					sourceCodeHighlighter(4);
				}

				if (!Highlight_sourcecode)
					lang.nextStep();
			}

			//draw current point
			if (cur.x == endX && cur.y == endY) { 

				unhighlightAllSourceCode();

				// path

				trail = maze.pathCreate(cur); // the path is then created

				trail.add(0, getStartNode(maze));


				int shortestPathInt = trail.size();
				
				if (Question_useage) {
					MultipleChoiceQuestionModel shortestPath = new MultipleChoiceQuestionModel("shortestPath");
					shortestPath.setPrompt(this.translator.translateMessage("shortestPathQuestion"));
					shortestPath.addAnswer(Integer.toString(shortestPathInt - 3), 0, "");
					shortestPath.addAnswer(Integer.toString(shortestPathInt), 1, "");
					shortestPath.addAnswer(Integer.toString(shortestPathInt + 3), 0, "");
					
					lang.addMCQuestion(shortestPath);
					lang.nextStep();
				}
				int neccessaryJP = 0;
				for (JPSNode jpsnode : trail) {
					pathBoxText.add("x:" + jpsnode.x + " y:" + jpsnode.y);
					neccessaryJP += 1;
					if (!jpsnode.end && !jpsnode.start) {
						lang.newSquare(jpsnode.getNode(), rectSize, "x" + jpsnode.x + "y" + jpsnode.y, null,
								sTraceProps);

					}

				}
				String NJP = neccessaryJP + "";
				var.set(varNeccesaryJumpPoints, NJP);



				pathBox.setText(pathBoxText);

				for (int i = 0; i < trail.size() - 1; i++) {
					Node[] vertices = new Node[2];
					JPSNode JPSNode1 = trail.get(i);
					JPSNode JPSNode2 = trail.get(i + 1);

					Node node1 = new Coordinates(JPSNode1.drawX + rectSize / 2, JPSNode1.drawY + rectSize / 2);
					Node node2 = new Coordinates(JPSNode2.drawX + rectSize / 2, JPSNode2.drawY + rectSize / 2);

					vertices[0] = node1;
					vertices[1] = node2;

					lang.newPolyline(vertices, "Polyline", null, pProps);

				}


				if (!Highlight_sourcecode)
					lang.nextStep();
				break;
			}

			sourceCodeHighlighter(5);
			possibleSuccess = identifySuccessors(cur); // get all possible successors of the current node

			if (Question_useage) {
				MultipleSelectionQuestionModel possibleSuccessors = new MultipleSelectionQuestionModel(
						Integer.toString(questionCounter));
				possibleSuccessors.setGroupID("possibleSuccessorsGroup");
				questionCounter += 1;

				possibleSuccessors.setPrompt(this.translator.translateMessage("possibleSuccessorsQuestion"));
				
				Random random = new Random();
				int randomNumber = 2 + random.nextInt(4);
				for (int i = 1; i < randomNumber; i++) {
					int x = cur.x + i;
					int y = 0;
					if (randomNumber < 3) {
						y = cur.y - i;
					} else {
						y = cur.y + i;
					}

					possibleSuccessors.addAnswer("x:" + x + " y:" + y, 0, this.translator.translateMessage("interWrong"));
				}

				for (JPSNode node : possibleSuccess) {
					if (node != null) {
						possibleSuccessors.addAnswer("x:" + node.x + " y:" + node.y, 1, this.translator.translateMessage("interCorrect"));

						randomNumber = 2 + random.nextInt(4);
						for (int i = 1; i < randomNumber; i++) {
							int x = node.x + i;
							int y = 0;
							if (randomNumber < 3) {
								y = node.y - i;
							} else {
								y = node.y + i;
							}

							possibleSuccessors.addAnswer("x:" + x + " y:" + y, 0, this.translator.translateMessage("interWrong"));
						}
					}
				}

				lang.addMSQuestion(possibleSuccessors);
			}

			for (int i = 0; i < possibleSuccess.length; i++) { // for each one of them
				if (possibleSuccess[i] != null) { // if it is not null
					sourceCodeHighlighter(6);
					
					maze.heapAdd(possibleSuccess[i]); // add it to the heap for later use (a possible future cur)

					LinkedList<float[]> heap1 = maze.getHeap();
					heapBoxText.clear();
					for (float[] node : heap1) {
						heapBoxText.add("x:" + (int) node[0] + " y:" + (int) node[1] + " Dist:" + node[2]);

					}

					heapBox.setText(heapBoxText);

				}
			}

			if (maze.heapSize() == 0) { // if the grid size is 0, and we have not found our end, the end is unreachable
				sourceCodeHighlighter(7);
				if (!Highlight_sourcecode)
					lang.nextStep();
				

				break; 
			}

		}
		if (!Highlight_sourcecode)
			lang.nextStep();

	}

	private JPSNode getEndNode(JPSMaze input) {
		for (JPSNode[] nodeArray : input.maze) {
			for (JPSNode node : nodeArray) {
				if (node.isEnd()) {
					return node;
				}
			}
		}
		return null;
	}

	private JPSNode getStartNode(JPSMaze input) {
		for (JPSNode[] nodeArray : input.maze) {
			for (JPSNode node : nodeArray) {
				if (node.isStart()) {
					return node;
				}
			}
		}
		return null;
	}

	public JPSNode[] identifySuccessors(JPSNode node) {
		successors = new JPSNode[8];
		neighbors = getNeighborsPrune(node);
		for (int i = 0; i < neighbors.length; i++) { // for each of these neighbors
			tmpXY = jump(neighbors[i][0], neighbors[i][1], node.x, node.y); // get next jump point
			if (tmpXY[0] != -1) { // if that point is not null( {-1,-1} )
				int x = tmpXY[0];
				int y = tmpXY[1];
				ng = (maze.toPointApprox(x, y, node.x, node.y) + node.g); // get the distance from start
				if (maze.getNode(x, y).f <= 0 || maze.getNode(x, y).g > ng) { // if this node is not already found, or
																				// we have a shorter distance from the
																				// current node
					maze.getNode(x, y).updateGHFP(maze.toPointApprox(x, y, node.x, node.y) + node.g,
							maze.toPointApprox(x, y, this.getEndNode(maze).x, this.getEndNode(maze).y), node); 
					successors[i] = maze.getNode(x, y); // add this node to the successors list to be returned
				}
			}
		}
		return successors; // finally, successors is returned

	}

	public int[] jump(int x, int y, int px, int py) {
		int[] jx = { -1, -1 }; // used to later check if full or null
		int[] jy = { -1, -1 }; // used to later check if full or null
		int dx = (x - px) / Math.max(Math.abs(x - px), 1); // because parents aren't always adjacent, this is used to
															// find parent -> child direction (for x)
		int dy = (y - py) / Math.max(Math.abs(y - py), 1); // because parents aren't always adjacent, this is used to
															// find parent -> child direction (for y)

		if (!maze.walkable(x, y)) { // if this space is not grid.walkable, return a null.
			return tmpInt(-1, -1); // in this system, returning a {-1,-1} equates to a null and is ignored.
		}
		if (x == this.getEndNode(maze).x && y == this.getEndNode(maze).y) { // if end point, return that point. The
																			// search is over! Have a beer.
			return tmpInt(x, y);
		}
		if (dx != 0 && dy != 0) { // if x and y both changed, we are on a diagonally adjacent square: here we
									// check for forced neighbors on diagonals
			if ((maze.walkable(x - dx, y + dy) && !maze.walkable(x - dx, y)) || // we are moving diagonally, we don't
																				// check the parent, or our next
																				// diagonal step, but the other
																				// diagonals
					(maze.walkable(x + dx, y - dy) && !maze.walkable(x, y - dy))) { // if we find a forced neighbor
																					// here, we are on a jump point, and
																					// we return the current position
				return tmpInt(x, y);
			}
		} else { // check for horizontal/vertical
			if (dx != 0) { // moving along x
				if ((maze.walkable(x + dx, y + 1) && !maze.walkable(x, y + 1)) || // we are moving along the x axis
						(maze.walkable(x + dx, y - 1) && !maze.walkable(x, y - 1))) { // we check our side nodes to see
																						// if they are forced neighbors
					return tmpInt(x, y);
				}
			} else {
				if ((maze.walkable(x + 1, y + dy) && !maze.walkable(x + 1, y)) || // we are moving along the y axis
						(maze.walkable(x - 1, y + dy) && !maze.walkable(x - 1, y))) { // we check our side nodes to see
																						// if they are forced neighbors
					return tmpInt(x, y);
				}
			}
		}

		if (dx != 0 && dy != 0) { // when moving diagonally, must check for vertical/horizontal jump points
			jx = jump(x + dx, y, x, y);
			jy = jump(x, y + dy, x, y);
			if (jx[0] != -1 || jy[0] != -1) {
				return tmpInt(x, y);
			}
		}
		if (maze.walkable(x + dx, y) || maze.walkable(x, y + dy)) { // moving diagonally, must make sure one of the
																	// vertical/horizontal neighbors is open to allow
																	// the path
			return jump(x + dx, y + dy, x, y);
		} else { // if we are trying to move diagonally but we are blocked by two touching
					// corners of adjacent nodes, we return a null
			return tmpInt(-1, -1);
		}
	}

	public int[][] getNeighborsPrune(JPSNode node) {
		JPSNode parent = node.parent; // the parent node is retrieved for x,y values
		int x = node.x;
		int y = node.y;
		int px, py, dx, dy;
		int[][] neighbors = new int[5][2];
		// directed pruning: can ignore most neighbors, unless forced
		if (parent != null) {
			px = parent.x;
			py = parent.y;
			// get the normalized direction of travel
			dx = (x - px) / Math.max(Math.abs(x - px), 1);
			dy = (y - py) / Math.max(Math.abs(y - py), 1);
			// search diagonally
			if (dx != 0 && dy != 0) {
				if (maze.walkable(x, y + dy)) {
					neighbors[0] = (tmpInt(x, y + dy));
				}
				if (maze.walkable(x + dx, y)) {
					neighbors[1] = (tmpInt(x + dx, y));
				}
				if (maze.walkable(x, y + dy) || maze.walkable(x + dx, y)) {
					neighbors[2] = (tmpInt(x + dx, y + dy));
				}
				if (!maze.walkable(x - dx, y) && maze.walkable(x, y + dy)) {
					neighbors[3] = (tmpInt(x - dx, y + dy));
				}
				if (!maze.walkable(x, y - dy) && maze.walkable(x + dx, y)) {
					neighbors[4] = (tmpInt(x + dx, y - dy));
				}
			} else {
				if (dx == 0) {
					if (maze.walkable(x, y + dy)) {
						if (maze.walkable(x, y + dy)) {
							neighbors[0] = (tmpInt(x, y + dy));
						}
						if (!maze.walkable(x + 1, y)) {
							neighbors[1] = (tmpInt(x + 1, y + dy));
						}
						if (!maze.walkable(x - 1, y)) {
							neighbors[2] = (tmpInt(x - 1, y + dy));
						}
					}
				} else {
					if (maze.walkable(x + dx, y)) {
						if (maze.walkable(x + dx, y)) {
							neighbors[0] = (tmpInt(x + dx, y));
						}
						if (!maze.walkable(x, y + 1)) {
							neighbors[1] = (tmpInt(x + dx, y + 1));
						}
						if (!maze.walkable(x, y - 1)) {
							neighbors[2] = (tmpInt(x + dx, y - 1));
						}
					}
				}
			}
		} else {// return all neighbors
			return maze.getNeighbors(node); // adds initial nodes to be jumped from!
		}

		return neighbors; // this returns the neighbors, you know
	}

	public int[] tmpInt(int x, int y) {
		int[] tmpIntsTmpInt = { x, y }; // create the tmpInt's tmpInt[]
		return tmpIntsTmpInt; // return it
	}

	private int[][] turnMatrixRight(int input[][]) {
		int totalRowsOfRotatedMatrix = input[0].length;
		int totalColsOfRotatedMatrix = input.length;

		int[][] rotatedMatrix = new int[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];

		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				rotatedMatrix[j][(totalColsOfRotatedMatrix - 1) - i] = input[i][j];
			}
		}

		return rotatedMatrix;
	}

	private int[][] flipMatrix(int input[][]) {

		int flippedMatrix[][] = new int[input.length][input[0].length];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				flippedMatrix[i][(input[0].length) - j - 1] = input[i][j];
			}
		}

		return flippedMatrix;
	}
}