package generators.graphics;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.AnswerModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import java.awt.Font;

public class FloodfillEight implements ValidatingGenerator
{
	// Wizard's declarations
	private Language lang;
	private Color squareHighlightColor;
	private Color oldColor;
	private double generatorWallChance;
	private Color sourceCodeHighlightColor;
	private int width;
	private Color newColor;
	private int squareSize;
	private Color wallColor;
	private int startPositionX;
	private int height;
	private int startPositionY;
	private int tocMaxDepthAfterDe;
	private int tocMaxDepthAfterIt;
	private double chanceForEasyQuestions;
	private double chanceForMediumQuestions;
	private double chanceForHardQuestions;
	private boolean disableQuestions;

	// Martin's declarations
	public static final String ALGORITHM_NAME = "Floodfill (8-Neighbor)";
	public static final String AUTHOR_NAME = "Martin Ott";
	public Square[][] sqGrid;

	// Properties of the squares used
	public SquareProperties sqStart = new SquareProperties();
	public SquareProperties sqHighStart = new SquareProperties();
	public SquareProperties sqFilled = new SquareProperties();
	public SquareProperties sqHighFilled = new SquareProperties();
	public SquareProperties sqWall = new SquareProperties();
	public static final int SPACE_BETWEEN_SQUARES = 1;

	// Source Code
	SourceCodeProperties scp = new SourceCodeProperties();
	SourceCode sc;

	// name of some squares so the source code and ending text can be set relative to them
	public String topRightSquare;
	public String bottomLeftPixel;

	// Text
	Text header;
	Text intro1;
	Text intro2;
	Text intro3;
	Text outro1;
	Text outro2;
	Text outro3;

	// Depth
	Text depthTop;
	Text depthValue;
	Text maxDepthValue;
	Text currentDepthText;
	Text maxDepthText;
	int lastDepth = 0;

	// Questions
	public int askedQInIteration;
	public int askedRecQuestion;

	public static void main(String[] args)
	{
		Generator gen = new FloodfillEight();
		//Animal.startGeneratorWindow(gen);
	}
    
    public FloodfillEight()
    {
    }

	public void init()
	{
		lang = new AnimalScript(ALGORITHM_NAME, AUTHOR_NAME, 800, 600);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException
	{
		width = (Integer) primitives.get("Width");
		height = (Integer) primitives.get("Height");

		startPositionX = (Integer) primitives.get("Start Position X");
		startPositionY = (Integer) primitives.get("Start Position Y");

		generatorWallChance = (double) primitives.get("Wall Chance");
		squareSize = (Integer) primitives.get("Square Size");

		tocMaxDepthAfterDe = (Integer) primitives.get("Record After Depth");
		tocMaxDepthAfterIt = (Integer) primitives.get("Record After Steps");

		chanceForEasyQuestions = (Double) primitives.get("Chance for easy questions");
		chanceForMediumQuestions = (Double) primitives.get("Chance for medium questions");
		chanceForHardQuestions = (Double) primitives.get("Chance for hard questions");
		disableQuestions = (Boolean) primitives.get("Disable questions");


		// catch wrong input
		if (width < 3 || width > 50) return false;
		if (height < 3 || height > 50) return false;
		if (startPositionX < 1 || startPositionX > width - 1) return false;
		if (startPositionY < 1 || startPositionY > height - 1) return false;
		if (generatorWallChance < 0.0 || generatorWallChance > 1) return false;
		if (squareSize < 10 || squareSize > 50) return false;
		if (tocMaxDepthAfterDe < 0 || tocMaxDepthAfterDe > 1000) return false;
		if (tocMaxDepthAfterIt < 0 || tocMaxDepthAfterIt > 1000) return false;
		if (chanceForEasyQuestions < 0.0D || chanceForEasyQuestions > 1.0D) return false;
		if (chanceForMediumQuestions < 0.0D || chanceForMediumQuestions > 1.0D) return false;
		if (chanceForHardQuestions < 0.0D || chanceForHardQuestions > 1.0D) return false;

		return true;
	}

	/* ----- ----- ----- Martin's code below ----- ----- ----- */

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
	{
		width = (Integer) primitives.get("Width");
		height = (Integer) primitives.get("Height");

		startPositionX = (Integer) primitives.get("Start Position X");
		startPositionY = (Integer) primitives.get("Start Position Y");

		generatorWallChance = (double) primitives.get("Wall Chance");
		squareSize = (Integer) primitives.get("Square Size");

		oldColor = (Color) primitives.get("Old Color");
		newColor = (Color) primitives.get("New Color");
		wallColor = (Color) primitives.get("Wall Color");

		squareHighlightColor = (Color) primitives.get("Square Highlight Color");
		sourceCodeHighlightColor = (Color) primitives.get("Source Code Highlight Color");

		tocMaxDepthAfterDe = (Integer) primitives.get("Record After Depth");
		tocMaxDepthAfterIt = (Integer) primitives.get("Record After Steps");

		chanceForEasyQuestions = (Double) primitives.get("Chance for easy questions");
		chanceForMediumQuestions = (Double) primitives.get("Chance for medium questions");
		chanceForHardQuestions = (Double) primitives.get("Chance for hard questions");
		disableQuestions = (Boolean) primitives.get("Disable questions");

		/* ----- Variable init done ----- */

		initProperties();

		// init and call stuff
		lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, ALGORITHM_NAME, AUTHOR_NAME, 640, 480);
		sqGrid = new Square[width][height];

		bottomLeftPixel = "sq0" + (height - 1);
		topRightSquare = "sq" + (width - 1) + "0";

		askedQInIteration = -1;
		askedRecQuestion = 0;

		// Activate step mode
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		makeHeaderAndIntroTexts();

		lang.nextStep("Intro");

		// Fade intro text and generate random pixels
		intro1.hide();
		intro2.hide();
		intro3.hide();
		initRandom(generatorWallChance, startPositionX, startPositionY);

		// Init and show source code
		lang.nextStep();
		initSourceCode();
		initDepthVisualization();

		// Execute Floodfill8
		runFloodfill8(startPositionX, startPositionY, oldColor, newColor, -1, 0);

		// Done : finish with outro
		lang.nextStep();
		makeOutro();
		lang.nextStep("Outro");

		if (!disableQuestions)
		{
			lang.nextStep("Questions");
			askFor8InName();
			lang.nextStep();
		}

		lang.finalizeGeneration();

		lang.nextStep("Done");
		return lang.toString();
	}

	public static String[] answerOptions = new String[]
	{
				"[Down] from the last one.", "[Up] from the last one.", "[Right] from the last one.", "[Left] from the last one.", "[Down-Right] from the last one.", "[Up-Right] from the last one.",
				"[Down-Left] from the last one.", "[Up-Left] from the last one.", "A direction from an [older one]" // if i put a . at the end of [older one] it becomes a 0 in the animal prompt ???
	};
	
	/**
	 * Asks the user to predict the next pixel that is going to be filled in.
	 * @param answer	Number between 0-8, indicating the answer in the above answerOptions array. 0-7 are the 8 directions, 8 is going back to an older pixel.
	 * @param points	The amount of points to be rewarded.
	 */
	public void askForPrediction(int answer, int points)
	{
		MultipleChoiceQuestionModel predict = new MultipleChoiceQuestionModel("predict" + (lang.getStep() + 1));
		predict.setPrompt("Which pixel is going to be colored in next?");
		for(int i=0;i<answerOptions.length;i++)
		{
			if (!(i == answer)) predict.addAnswer(answerOptions[i], 0, "Wrong. " + answerOptions[answer]);
			if (i == answer) predict.addAnswer(answerOptions[i], points, "Correct!");
		}
		lang.addMCQuestion(predict);
	}

	/**
	 * Asks the predefined question of what the 8 in Floodfill8 stands for. Called at the end of the algorithm.
	 */
	public void askFor8InName()
	{
		MultipleChoiceQuestionModel whyTheEight = new MultipleChoiceQuestionModel("whyTheEight");
		whyTheEight.setPrompt("What does the 8 in Floodfill8 stand for?");
		whyTheEight.addAnswer("8 input variables (colors as rgb)", 0, "Wrong. The 8 stands for the 8 directions the algorithm checks.");
		whyTheEight.addAnswer("8 directions to check every recursion step", 2, "Correct!");
		whyTheEight.addAnswer("8 is the maximum recursion depth", 0, "Wrong. The 8 stands for the 8 directions the algorithm checks.");
		lang.addMCQuestion(whyTheEight);
	}


	/**
	 * The Floodfill algorithm with added highlighting for animal.
	 * 
	 * @param x
	 *            X coordinate of the starting position
	 * @param y
	 *            Y coordinate of the starting position.
	 * @param oldColor
	 *            Color to be overridden.
	 * @param newColor
	 *            Color to fill the pixels with.
	 * @param lastPath
	 *            Kept through each recursive step to be able to highlight the
	 *            square that will be taken next.
	 */
	public void runFloodfill8(int x, int y, Color oldColor, Color newColor, int lastPath, int recursionDepth)
	{
		if (lastPath == -1) lang.nextStep("Algorithm Start");

		String current = sqGrid[x][y].getProperties().get(AnimationPropertiesKeys.FILL_PROPERTY).toString();
		if (current.equals(oldColor.toString())) // compare colors of current position and oldColor
		{
			if (lastPath < 4) // Ask easy prediction question (down, up, right, left)
			{
				if (askedRecQuestion == 0)
				{
					if (new Random().nextDouble() < chanceForEasyQuestions && lastPath != -1 && !disableQuestions)
					{
						lang.nextStep("Prediction Question");
						askForPrediction(lastPath, 1);
						askedQInIteration = lang.getStep();
					}
				}
				else askedRecQuestion--;

			}
			else // Ask medium difficulty prediction question (diagonals)
			{
				if (askedRecQuestion == 0)
				{
					if (new Random().nextDouble() < chanceForMediumQuestions && lastPath != -1 && !disableQuestions)
					{
						lang.nextStep("Prediction Question");
						askForPrediction(lastPath, 2);
						askedQInIteration = lang.getStep();
					}
				}
				else askedRecQuestion--;

			}


			lang.nextStep();
			sc.highlight(lastPath > 3 ? lastPath + 7 : lastPath + 6, 0, false); // highlight where the square will be colored in this iteration
			sc.highlight(4, 0, false); // highlight the if-statement

			replaceSquare(x, y, makeOrangeSquare(x, y)); // change the square to orange (or user-specified color)

			int currentStep = lang.getStep() + 1;
			boolean newMaxDepthFound = writeDepth(recursionDepth);
			if (newMaxDepthFound && currentStep >= tocMaxDepthAfterIt && recursionDepth >= tocMaxDepthAfterDe)
			{
				lang.nextStep("New max recursion depth: " + recursionDepth); // save new max in ToC
			}
			else lang.nextStep();

			sc.unhighlight(4, 0, false);
			sc.unhighlight(lastPath > 3 ? lastPath + 7 : lastPath + 6);

			runFloodfill8(x, y + 1, oldColor, newColor, 0, recursionDepth + 1);
			runFloodfill8(x, y - 1, oldColor, newColor, 1, recursionDepth + 1);
			runFloodfill8(x + 1, y, oldColor, newColor, 2, recursionDepth + 1);
			runFloodfill8(x - 1, y, oldColor, newColor, 3, recursionDepth + 1);

			runFloodfill8(x + 1, y + 1, oldColor, newColor, 4, recursionDepth + 1);
			runFloodfill8(x + 1, y - 1, oldColor, newColor, 5, recursionDepth + 1);
			runFloodfill8(x - 1, y + 1, oldColor, newColor, 6, recursionDepth + 1);
			runFloodfill8(x - 1, y - 1, oldColor, newColor, 7, recursionDepth + 1);

			if (new Random().nextDouble() < chanceForHardQuestions && lastPath != -1 && !disableQuestions && askedQInIteration != lang.getStep() && askedRecQuestion == 0)
			{
				lang.nextStep("Prediction Question");
				askForPrediction(8, 3);
				askedRecQuestion = 1; // don't ask another question in next step cause that would ask where to go from the old pixel
			}
		}
	}

	// These generate squares with a given property. Color and highlighting/outline varies.
	// Highlighting means a red border.
	public Square makeLightGraySquare(int x, int y)
	{
		return lang.newSquare(new Offset(x * (squareSize + SPACE_BETWEEN_SQUARES), y * (squareSize + SPACE_BETWEEN_SQUARES), "sq00", AnimalScript.DIRECTION_NW), squareSize, "sq" + x + "" + y, null,
				sqStart);
	}

	public Square makeBlackSquare(int x, int y)
	{
		return lang.newSquare(new Offset(x * (squareSize + SPACE_BETWEEN_SQUARES), y * (squareSize + SPACE_BETWEEN_SQUARES), "sq00", AnimalScript.DIRECTION_NW), squareSize, "sq" + x + "" + y, null,
				sqWall);
	}

	public Square makeOrangeSquare(int x, int y)
	{
		return lang.newSquare(new Offset(x * (squareSize + SPACE_BETWEEN_SQUARES), y * (squareSize + SPACE_BETWEEN_SQUARES), "sq00", AnimalScript.DIRECTION_NW), squareSize, "sq" + x + "" + y, null,
				sqFilled);
	}

	public Square makeOrangeSquareHighlighted(int x, int y)
	{
		return lang.newSquare(new Offset(x * (squareSize + SPACE_BETWEEN_SQUARES), y * (squareSize + SPACE_BETWEEN_SQUARES), "sq00", AnimalScript.DIRECTION_NW), squareSize, "sq" + x + "" + y, null,
				sqHighFilled);
	}

	public Square makeLightGraySquareHighlighted(int x, int y)
	{
		return lang.newSquare(new Offset(x * (squareSize + SPACE_BETWEEN_SQUARES), y * (squareSize + SPACE_BETWEEN_SQUARES), "sq00", AnimalScript.DIRECTION_NW), squareSize, "sq" + x + "" + y, null,
				sqHighStart);
	}

	/**
	 * Initializes the grid with walls around it. Then fills it with empty squares
	 * or walls, depending on the wallChance
	 * 
	 * @param wallChance
	 *            The chance to have a wall in any square
	 * @param startX
	 *            The starting X coordinate so it can be set to an empty square
	 * @param startY
	 *            See startX
	 */
	public void initRandom(double wallChance, int startX, int startY)
	{
		sqGrid[0][0] = lang.newSquare(new Offset(20, 50, "header", AnimalScript.DIRECTION_SW), squareSize, "sq00", null, sqWall); // reference for all other squares
		sqGrid[startX][startY] = makeLightGraySquareHighlighted(startX, startY);
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				if (!(i == 0 && j == 0) && !(i == startX && j == startY))
				{
					// Walls
					if (i == 0 || i == (width - 1) || j == 0 || j == (height - 1)) sqGrid[i][j] = makeBlackSquare(i, j);
					else
					{ // Randomly choose between wall and light gray
						float rng = new Random().nextFloat();
						sqGrid[i][j] = rng < wallChance ? makeBlackSquare(i, j) : makeLightGraySquare(i, j);
					}
				}
			}
		}
	}

	/**
	 * This is the source code that's displayed in the animation.
	 */
	public void initSourceCode()
	{
		sc = lang.newSourceCode(new Offset(40, -30, topRightSquare, AnimalScript.DIRECTION_NE), "sourceCode", null, scp);
		sc.addCodeLine("public void floodfill8(int x, int y, Color oldColor, Color newColor)", null, 0, null);
		sc.addCodeLine("{", null, 0, null);
		sc.addCodeLine("    if(getPixel(x, y) == newColor)", null, 0, null);
		sc.addCodeLine("    {", null, 0, null);
		sc.addCodeLine("        setPixel(x, y, newColor);", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("        floodfill8(  x  , y + 1, oldColor, newColor); // down", null, 0, null);
		sc.addCodeLine("        floodfill8(  x  , y - 1, oldColor, newColor); // up", null, 0, null);
		sc.addCodeLine("        floodfill8(x + 1,   y  , oldColor, newColor); // right", null, 0, null);
		sc.addCodeLine("        floodfill8(x - 1,   y  , oldColor, newColor); // left", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("        floodfill8(x + 1, y + 1, oldColor, newColor); // down-right", null, 0, null);
		sc.addCodeLine("        floodfill8(x + 1, y - 1, oldColor, newColor); // up-right", null, 0, null);
		sc.addCodeLine("        floodfill8(x - 1, y + 1, oldColor, newColor); // down-left", null, 0, null);
		sc.addCodeLine("        floodfill8(x - 1, y - 1, oldColor, newColor); // up-left", null, 0, null);
		sc.addCodeLine("    }", null, 0, null);
		sc.addCodeLine("}", null, 0, null);
	}

	public void initDepthVisualization()
	{
		int size = squareSize;

		// header for depth values
		TextProperties depthHeaderProps = new TextProperties();
		depthHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, size));
		depthTop = lang.newText(new Offset(50, -20, "sourceCode", AnimalScript.DIRECTION_NE), "Recursion Depth:", "depthText", null, depthHeaderProps);

		int offsetX = (int) (squareSize * 1.5);
		int offsetY1 = (int) (squareSize / 2);
		int offsetY2 = (int) (squareSize * 2);

		// current depth value
		TextProperties depthTextProps = new TextProperties();
		depthTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, size));
		depthValue = lang.newText(new Offset(offsetX, offsetY2, "depthText", AnimalScript.DIRECTION_S), "0", "depthValue", null, depthTextProps);

		// "current"
		currentDepthText = lang.newText(new Offset(0, offsetY2, "depthText", AnimalScript.DIRECTION_SW), "current:", "currentText", null, depthTextProps);

		// max reached depth value
		maxDepthValue = lang.newText(new Offset(offsetX, offsetY1, "depthText", AnimalScript.DIRECTION_S), "0", "maxDepthValue", null, depthTextProps);

		// "max"
		maxDepthText = lang.newText(new Offset(0, offsetY1, "depthText", AnimalScript.DIRECTION_SW), "max:", "maxDepthText", null, depthTextProps);

		int offset3 = (int) (size / 4);

		Rect depthBorder;
		RectProperties depthBorderProps = new RectProperties();
		depthBorderProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		depthBorderProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.cyan);
		depthBorderProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		depthBorder = lang.newRect(new Offset(-offset3, -offset3, "depthText", AnimalScript.DIRECTION_NW),
				new Offset(offset3, offsetY1 * 2 + (int) (offsetY2 * 1.5), "depthText", AnimalScript.DIRECTION_SE), "depthBorder", null, depthBorderProps);

	}

	// writes the current recursion depth to the text field
	// if there was a new max it also sets the appropriate maxDepth text and returns true
	// if no new max was found returns false
	public boolean writeDepth(int newValue)
	{
		depthValue.setText(newValue + "", null, null);
		int currentMax = 0;
		try
		{
			currentMax = Integer.parseInt(maxDepthValue.getText());
		} catch (NumberFormatException e)
		{
			return false;
		}
		if (newValue > currentMax)
		{
			maxDepthValue.setText(newValue + "", null, null);
			return true;
		}
		return false;
	}

	/**
	 * Disables the square at posX/posY and makes a new one at that position.
	 */
	public void replaceSquare(int posX, int posY, Square s)
	{
		sqGrid[posX][posY].hide();
		sqGrid[posX][posY] = s;
	}

	// Initializes the 5 different square types used (oldColor, newColor and wall as
	// well as highlighted versions of the first two)
	// Also sets the settings for the source code
	public void initProperties()
	{
		// Define a light gray square
		sqStart.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		sqStart.set(AnimationPropertiesKeys.FILL_PROPERTY, oldColor);

		// Define a highlighted light gray, orange square
		sqHighStart.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		sqHighStart.set(AnimationPropertiesKeys.FILL_PROPERTY, oldColor);
		sqHighStart.set(AnimationPropertiesKeys.COLOR_PROPERTY, squareHighlightColor);

		// Define a filled, orange square
		sqFilled.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		sqFilled.set(AnimationPropertiesKeys.FILL_PROPERTY, newColor);

		// Define a highlighted filled, orange square
		sqHighFilled.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		sqHighFilled.set(AnimationPropertiesKeys.FILL_PROPERTY, newColor);
		sqHighFilled.set(AnimationPropertiesKeys.COLOR_PROPERTY, squareHighlightColor);

		// Define a wall square
		sqWall.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		sqWall.set(AnimationPropertiesKeys.FILL_PROPERTY, wallColor);

		// Source Code Properties
		scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.blue);
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, squareSize)); // works nicely to scale the source code to how much space the squares take up
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sourceCodeHighlightColor);
		scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}

	// Sets the header and three small intro texts when first loading the algorithm
	public void makeHeaderAndIntroTexts()
	{
		// Header
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, (squareSize + 5) > 40 ? 40 : squareSize + 5));
		header = lang.newText(new Coordinates(20, 30), ALGORITHM_NAME, "header", null, headerProps);

		// Intro texts
		TextProperties introProperties = new TextProperties();
		introProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, (squareSize + 5) > 30 ? 30 : squareSize + 5));

		Rect hRect;
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(12, 5, "header", "SE"), "hRect", null, rectProps);

		intro1 = lang.newText(new Offset(0, 10, "header", AnimalScript.DIRECTION_SW), "- Fills a grid of connected pixels of the same color", "intro1", null, introProperties);
		intro2 = lang.newText(new Offset(0, 0, "intro1", AnimalScript.DIRECTION_SW), "- Connected in this case means directly attached pixels as well as diagonals", "intro2", null, introProperties);
		intro3 = lang.newText(new Offset(0, 0, "intro2", AnimalScript.DIRECTION_SW), "- We choose the middle pixel as the starting point, marked with a red outline", "intro3", null, introProperties);
	}

	// Sets the two outro texts after the animation is done
	public void makeOutro()
	{
		TextProperties outroProperties = new TextProperties();
		outroProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, (squareSize + 5) > 30 ? 30 : squareSize + 5));
		outroProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
		outro1 = lang.newText(new Offset(0, 20, bottomLeftPixel, AnimalScript.DIRECTION_SW), "- As the result we have the whole grid colored in", "outro1", null, outroProperties);
		outro2 = lang.newText(new Offset(0, 0, "outro1", AnimalScript.DIRECTION_SW), "- Except for pixels not connected to the main area", "outro2", null, outroProperties);
		outro3 = lang.newText(new Offset(0, 0, "outro2", AnimalScript.DIRECTION_SW), "- As you can see on the recursion depth in the top right, stack overflows can be a problem for big pictures",
				"outro3", null, outroProperties);
	}

	/* ----- ----- ----- Wizard code below ----- ----- ----- */

	public String getName()
	{

		return ALGORITHM_NAME;
	}

	public String getAlgorithmName()
	{
		return ALGORITHM_NAME;
	}

	public String getAnimationAuthor()
	{
		return AUTHOR_NAME;
	}

	public String getDescription()
	{
		return "Floodfill gets a start point in a 2D pixel array as well " + "\n" + "as an old color to override and the new color with which to override the old one." + "\n"
				+ "From there it recursively goes over the array and fills it with the new color." + "\n" + "\n"
				+ "This algorithm is pretty well known from paint programs where it's used with the bucket icon" + "\n" + "that fills a selected area with color." + "\n"
				+ "It can also be used in games such as Go or Minesweeper to declare areas \"cleared\" of pieces." + "\n" + "\n"
				+ "This version will go through diagonal \"walls\" because it checks not only" + "\n" + "down, up, right and left, but also all four diagonals for pixels to spread to." + "\n"
				+ "It will stop at straight or two-thick walls though." + "\n" + "\n" + "Walls are defined by not being of the oldColor, the color to override." + "\n" + "\n"
				+ "There is a version called Floodfill 4 that only checks down, up, right and left." + "\n" + "Which in turn means that it doesn't go through diagonal walls." + "\n"
				+ "There is an animation in animal for Floodfill 4 as well." + "\n" + "\n" + "Floodfill also goes under the name \"Seed Fill\".";
	}

	public String getCodeExample()
	{
		return "void floodfill8(int x, int y, Color oldColor, Color newColor)" + "\n" + "{" + "\n" + "    if(getPixel(x, y,) == oldColor)" + "\n" + "    {" + "\n" + "        setPixel(x, y, newColor)"
				+ "\n" + "\n" + "        floodfill8(  x  , y + 1, oldColor, newColor);    // down" + "\n" + "        floodfill8(  x  , y - 1, oldColor, newColor);     // up" + "\n"
				+ "        floodfill8(x + 1,   y  , oldColor, newColor);    // right" + "\n" + "        floodfill8(x - 1,   y  , oldColor, newColor);     // left" + "\n" + "\n"
				+ "        floodfill8(x + 1, y + 1, oldColor, newColor);    // down-right" + "\n" + "        floodfill8(x + 1, y - 1, oldColor, newColor);    // up-right" + "\n"
				+ "        floodfill8(x - 1, y + 1, oldColor, newColor);    // down-left" + "\n" + "        floodfill8(x - 1, y - 1, oldColor, newColor);    // up-left" + "\n" + "    }" + "\n" + "}";
	}

	public String getFileExtension()
	{
		return "asu";
	}

	public Locale getContentLocale()
	{
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType()
	{
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage()
	{
		return Generator.JAVA_OUTPUT;
	}
}
