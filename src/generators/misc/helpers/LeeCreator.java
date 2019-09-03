package generators.misc.helpers;
import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

///
///
/// Tested with Animal-2.3.33.jar
///
///


/**
 * Encapsulates the functionality to visualize the behavior of Lees algorithm.
 * @author chollubetz
 *
 */
public class LeeCreator {

	private Language lang;
	
    private CircleProperties gridCircle;
    private int[][] input;
    private Color followBack1;
    private SourceCodeProperties sourceCode;
    private Color followBack2;
    private Color waveFront2;
    private Color wall;
    private Color waveFront1;
    private Color gridSpot;
    private Font introFont;
    private Font outroFont;
    private Font labelFont;
    private Font consoleFont;
	

	/**
	 * Creates a class that is able to visualize the behavior of Lees algorithm
	 */
	public LeeCreator(Language lang, CircleProperties gridCircle, int[][] input, Color followBack1, SourceCodeProperties sourceCode, Color followBack2, Color waveFront2, Color wall, Color waveFront1, Color gridSpot, Font introFont, Font outroFont, Font labelFont, Font consoleFont) {
		this.lang = lang;		;
		lang.setStepMode(true);
		
	    this.gridCircle = gridCircle;
	    this.input = input;
	    this.followBack1 = followBack1;
	    this.sourceCode = sourceCode;
	    this.followBack2 = followBack2;
	    this.waveFront2 = waveFront2;
	    this.wall = wall;
	    this.waveFront1 = waveFront1;
	    this.gridSpot = gridSpot;
	    this.introFont = introFont;
	    this.outroFont = outroFont;
	    this.labelFont = labelFont;
	    this.consoleFont = consoleFont;
	}

	/**
	 * Performs lees algorithm on the given grid and returns the language containing all the commands.
	 * @return the language containing all the commands
	 * @throws Exception
	 */
	public Language perform() throws Exception {
		new Label(new Position(310, 5), 30, 180, lang, "Lees Algorithm", labelFont);
		
		Grid inputGrid = buildInputGrid();
		
		ensureCorrectInput(inputGrid);
		
		intro();
		lee(inputGrid);
		outro();

		return lang;
	}

	private Grid buildInputGrid() {
		Grid result = new Grid(input.length, input[0].length, new Position(0, 50), 20, 10);
		for (int r = 0; r < input.length; r++)
			for (int c = 0; c < input[0].length; c++)
				if (input[r][c] == -3)
					result.placeSourceItem(new GridItemPosition(r, c));
				else if (input[r][c] == -2)
					result.placeTrainItem(new GridItemPosition(r, c));
				else if (input[r][c] == -1)
					result.placeWallItem(new GridItemPosition(r, c));
				
		return result;
	}

	/**
	 * Ensures that the input grid contains all the necessary special items.
	 * @param inputGrid the grid to check
	 * @throws Exception An exception will be thrown if the grid doen't contain the necessary items.
	 */
	private void ensureCorrectInput(Grid inputGrid) throws Exception {
		if (inputGrid.getSource() == null)
			throw new Exception("No source item found in grid. Please place a source in the grid.");
		if (inputGrid.getTrain() == null)
			throw new Exception("No train item found in grid. Please place a train in the grid.");
	}

	/**
	 * Visualizes the intro.
	 */
	private void intro() {
		
		TextBlock tb = new TextBlock(new Position(230, 50), lang, 20, 70, introFont);
		tb.insertText("This animation demonstrates the process of Lees algorithm.\n"
				+ "The purpose of Lees algorithm is to find the shortest path from a source to a sink (train).\n"
				+ "The algorithm performs on a grid. It is strongly used to solve the maze problem and with it the task to connect two parts together.\n"
				+ "The main idea is a wave front, that starts at the source and flows in every possible direction.\n"
				+ "When this wave front reaches the train, the shortest path is determined by following back the wave fronts path.");
		lang.nextStep("Intro");
		tb.hide();
	}

	/**
	 * Visualizes the outro.
	 */
	private void outro() {
		TextBlock tb = new TextBlock(new Position(230, 50), lang, 20, 70, outroFont);
		tb.insertText("As we could see, the Lee algorithm performed as wanted.\n"
				+ "It should be commented, that sometimes there are different possible shortest ways from source to train.\n"
				+ "The strategy in the follow-back step to choose a neighbor determines the ways.\n"
				+ "In a real world scenario this step should be modified to fullfill the special purpose.");
		lang.nextStep("Outro");
		tb.hide();
	}

	/**
	 * Performs and visualized lees algorithm.
	 * @param inputGrid the grid to perform lee on
	 * @throws Exception
	 */
	private void lee(Grid inputGrid) throws Exception {
		inputGrid.draw(lang, this.gridCircle, this.wall);
		Console leeConsole = new Console(new Position(inputGrid
				.getNorthEastPosition().getX() + 20, inputGrid
				.getNorthEastPosition().getY()), lang, 9, consoleFont);
		Set<GridItem> wave, new_wave;
		GridItem path_elem;
		int label;

		inputGrid.getSource().setText("S", lang);
		inputGrid.getTrain().setText("T", lang);
		lang.nextStep("Lees Algorithm");
		leeConsole
				.writeLine("find the shortest path in the given grid from S to T");

		SourceCodeWrapper scw = prepareSourceCodes(new Position(inputGrid
				.getSouthWestPosition().getX(), inputGrid
				.getSouthWestPosition().getY() + 20), lang);
		scw.markSourceCode1Lines(0);

		leeConsole
				.writeLineAndFinishCurrentStep("for that purpose we call lees algorithm with lee(S, T)");
		// 1. step - wave front
		wave = new HashSet<GridItem>();
		new_wave = new HashSet<GridItem>();
		new_wave.add(inputGrid.getSource());
		inputGrid.getSource().getCircle()
				.changeColor("fillColor", this.waveFront2, null, null);
		scw.markSourceCode1Lines(5);
		leeConsole.writeLine("the new_wave starts in S");
		lang.nextStep("Initialization");
		label = 0;
		scw.markSourceCode1Lines(6);
		leeConsole.writeLineAndFinishCurrentStep("label is initialized to 0");
		scw.markSourceCode1Lines(7);
		leeConsole
				.writeLine("while the new wave does not reach T");
		lang.nextStep("Wave-Front");
		while (!new_wave.contains(inputGrid.getTrain())) {
			label++;
			scw.markSourceCode1Lines(8);
			leeConsole.writeLineAndFinishCurrentStep("increment label to "
					+ label);

			for (GridItem tmp : wave)
				tmp.getCircle().changeColor("fillColor",
						new Color(255, 255, 255), null, null);

			wave = new_wave;

			for (GridItem tmp : wave)
				tmp.getCircle().changeColor("fillColor",
						this.waveFront1, null, null);
			scw.markSourceCode1Lines(9, 10);
			leeConsole
					.writeLineAndFinishCurrentStep("the new_wave becomes the current wave and the new wave is cleared");

			new_wave = new HashSet<GridItem>();
			scw.markSourceCode1Lines(11, 12, 13);
			leeConsole
					.writeLineAndFinishCurrentStep("for each direct neighbor of the blue ones, that are not labeled yet...");

			for (GridItem element : wave)
				for (GridItem currentNeighbor : element.getVisitableNeighbors())
					if (currentNeighbor.getValue() == 0) {
						currentNeighbor.setValue(label);
						currentNeighbor.setText("" + label, lang);
						new_wave.add(currentNeighbor);
						currentNeighbor.getCircle().changeColor("fillColor",
								this.waveFront2, null, null);
					} else if (currentNeighbor.getValue() == GridItem.TRAIN) {
						currentNeighbor.getText().hide();
						currentNeighbor.setText("T/" + label, lang);
						new_wave.add(currentNeighbor);
						currentNeighbor.getCircle().changeColor("fillColor",
								this.waveFront2, null, null);
					}
			scw.markSourceCode1Lines(11, 12, 13, 14, 15, 16);
			leeConsole
					.writeLineAndFinishCurrentStep("label it with the current value of label and add it to new_wave");
		}
		for (GridItem tmp : wave)
			tmp.getCircle().changeColor("fillColor", new Color(255, 255, 255),
					null, null);
		inputGrid.getTrain().getCircle()
				.changeColor("fillColor", this.gridSpot, null, null);
		scw.markSourceCode1Lines(7);
		leeConsole
				.writeLineAndFinishCurrentStep("because new_wave contains T the loop will not be executed again");
		for (GridItem tmp : new_wave)
			tmp.getCircle().changeColor("fillColor", new Color(255, 255, 255),
					null, null);
		leeConsole.clear();
		scw.markSourceCode1Lines(18);
		leeConsole
				.writeLine("now we need to follow back from T to S");
		lang.nextStep("Follow-Back");

		// 2. step - follow-back
		path_elem = inputGrid.getTrain();
		path_elem.getCircle()
				.changeColor("fillColor", this.followBack2, null, null);
		scw.markSourceCode1Lines(19);
		leeConsole.writeLineAndFinishCurrentStep("we start on element T");
		boolean firstIterationOfStep2 = true;
		for (int i = label - 1; i >= 1; i--) {
			path_elem.getCircle().changeColor("fillColor", this.followBack1, null,
					null);
			if (firstIterationOfStep2) {
				scw.markSourceCode1Lines(20, 21);
				leeConsole
						.writeLine("iteratively we set the current path element as a barricade");
				inputGrid.getTrain().getText().hide();
				inputGrid.getTrain().setText("T", lang);
			}
			lang.nextStep();
			path_elem = getFirst(path_elem.getNeighborsWithValue(i));
			path_elem.setValue(GridItem.WALL);
			path_elem.getCircle().changeColor("fillColor", this.followBack2, null,
					null);
			if (firstIterationOfStep2) {
				scw.markSourceCode1Lines(20, 21, 22, 23);
				leeConsole
						.writeLine("and choose a neighbor with decreased by one value as the new current");
			}
			lang.nextStep();
			if (firstIterationOfStep2)
				firstIterationOfStep2 = false;
		}
		path_elem.getCircle().changeColor("fillColor", this.followBack1, null, null);
		lang.nextStep();
		inputGrid.getSource().getCircle()
				.changeColor("fillColor", this.followBack2, null, null);
		lang.nextStep();
		inputGrid.getSource().getCircle()
				.changeColor("fillColor", this.followBack1, null, null);
		lang.nextStep();
		
		// 3. step - clean-up
		scw.markSourceCode1Lines(24, 25, 26, 27);
		leeConsole.clear();
		leeConsole
				.writeLine("in the last step we cleanup all the values");
		lang.nextStep("Cleanup");
		for (GridItem currentItem : inputGrid.getItems()) {
			if (currentItem.getValue() > 0)
				currentItem.setValue(0);
			if (currentItem.getText() != null)
				currentItem.getText().hide();
		}
		inputGrid.getSource().setText("S", lang);
		inputGrid.getTrain().setText("T", lang);
		lang.nextStep();
		scw.markSourceCode1Lines();
		leeConsole
				.writeLineAndFinishCurrentStep("as a result we found one possible shortest way from S to T");
		inputGrid.hide();
		leeConsole.hide();
		scw.hide();
	}

	/**
	 * Places the source code of lees algorithm on a given position.
	 * @param position the position to place the source code
	 * @param lang the language to draw on
	 * @return the wrapper for the source code
	 */
	private SourceCodeWrapper prepareSourceCodes(Position position,
			Language lang) {
		Coordinates position1 = new Coordinates(position.getX(),
				position.getY());
		Coordinates position2 = new Coordinates(position.getX() + 350,
				position.getY());

		List<String> sources1 = new LinkedList<String>();
		List<String> sources2 = new LinkedList<String>();

		sources1.add("lee(grid_point S, grid_point T) {"); // 0
		sources1.add("  set<grid_point> wave, new_wave;"); // 1
		sources1.add("  grid_point neighbor, elem, path_elem;"); // 2
		sources1.add("  int label;"); // 3
		sources1.add("  /* 1. Step: Wave front */"); // 4
		sources1.add("  new_wave := {S};"); // 5
		sources1.add("  label := 0;"); // 6
		sources1.add("  while (!new_wave.contains(T)) {"); // 7
		sources1.add("    ++label;"); // 8
		sources1.add("    wave := new_wave;"); // 9
		sources1.add("    new_wave := empty;"); // 10
		sources1.add("    foreach (element : wave)"); // 11
		sources1.add("      foreach (neighbor : neighbors(element))"); // 12
		sources1.add("        if (neighbor.value == 0) {"); // 13
		sources1.add("          neighbor.value := label;"); // 14
		sources1.add("          new_wave := new_wave + {neighbor};"); // 15
		sources1.add("        }"); // 16
		sources1.add("  }"); // 17
		sources1.add("  /* 2. Step: Follow back */"); // 18
		sources1.add("  path_elem := T;"); // 19
		sources1.add("  for (i:=label-1; i >= 1; --i) {"); // 20
		sources1.add("    path_elem.value := -1;"); // 21
		sources1.add("    path_elem := chooseNeighborOf_WithValue_(path_elem, i);"); // 22
		sources1.add("  }"); // 23
		sources1.add("  /* 3. Step: Cleanup */"); // 24
		sources1.add("  foreach 'point on grid'"); // 25
		sources1.add("    if (point.value > 0)"); // 26
		sources1.add("      point.value := 0;"); // 27
		sources1.add("}"); // 28

		sources2.add("class grid_point : point {"); // 0
		sources2.add(" int value;"); // 1
		sources2.add("};"); // 2

		return new SourceCodeWrapper(position1, position2, lang,
				sources1, sources2, this.sourceCode);
	}

	/**
	 * Returns the first element of a given set of grid items.
	 * @param gridItems the set of grid items
	 * @return the first element of the set, if set is empty null
	 */
	private GridItem getFirst(Set<GridItem> gridItems) {
		for (GridItem currentItem : gridItems)
			return currentItem;
		return null;
	}
}
