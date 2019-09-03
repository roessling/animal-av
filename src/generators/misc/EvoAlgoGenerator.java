package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Arc;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;

/**
 * 
 * @author Andreas Altenkirch, Stefan Hoerndler
 * 
 */
public class EvoAlgoGenerator implements Generator, ValidatingGenerator {
	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;
	private static final String algorithmus = "Evolutionary algorithm";

	// Generator
	private RectProperties pointArea;
	private SourceCodeProperties textProperties;
	private int numberOfPoints;
	private TextProperties titleProperties;
	private Color removePointColor;
	private Color selectPointColor;
	private Color keepPointColor;
	private int mutationStrength;
	private TextProperties subTitleProperties;
	private int xTargetPoint;
	private int yTargetPoint;
	private EllipseProperties pointProperties;
	private EllipseProperties targetPointProperties;
	private int heightPointArea;
	private int widthPointArea;
	private int maxIterationSteps;
	private Color ratingLineColor;

	// Static Coordinates
	private Coordinates headerCoordinates;
	private Coordinates iterationHeaderCoordinates;
	private Coordinates partHeaderCoordinates;
	private Coordinates graphAreaUpperLeft;
	private Coordinates graphAreaLowerRight;

	// Start text for every phase
	private static final String EVALUATE_START_TEXT = "Evalute";
	private static final String SELECT_START_TEXT = "Select";
	private static final String MUTATE_START_TEXT = "Mutate";
	private static final String RECOMBINE_START_TEXT = "Recombine";
	private Text algoHeaderText;

	// Properties
	private TextProperties startTextProp;
	private TextProperties numberOfPointsTextProp;
	private int textSize = 14;
	private int offsetCodeFromHeader = 10;
	private GraphProperties ratingLineProperties;

	// Code Iteration
	private SourceCode iterationCode;
	private SourceCodeProperties sourceCodeHeaderProps;
	private SourceCode selectCode;

	private int numberOfIterations = 1;

	private List<Point> points = new ArrayList<Point>();
	private Random rand = new Random();
	private int pointCounter;
	private Point bestPoint = null;
	private Text numberOfPointsText = null;

	private Coordinates targetCoordinates;

	// Start-text
	private static final String START_TEXT_INTRODUCTION = "Evolutionary algorithms are class of stochastical and eneric population-based metaheuristic optimization algorithms, "
			+ "\nwhich find a sufficiently good solution instead of an optimal solution. "
			+ "\nThey are inspired by biological evolution such as reproduction, mutation, recombination und selection.";
	private static final String START_TEXT_DETAILS = "In the generator we solve a problem to get the points to a target, which they don't know. "
			+ "\nThey only can evaluate their distance to the target. Therefore we have fourphases within an iteration."
			+ "\nThey are as follows:";
	private static final String INITIALIZE_TEXT = "In the initialize phase all points are generated at random positions.";
	private static final String EVALUATE_TEXT = "In the evalutation phase every point is rated with a weight function for the distance between the target"
			+ "\nand the point. In our example we take the euclidean distance as the weight function.";
	private static final String SELECT_TEXT = "In the selection phase a third of all points are deleted and two thirds are kept. Only the best ratings"
			+ "\n(i. e. the shortest distance to the target) are kept.";
	private static final String MUTATE_TEXT = "In the mutation phase the coordingates of the points are randomly mutated.";
	private static final String RECOMBINE_TEXT = "In the recombination phase an new point is created with a recombination of two points.";

	// Source-Code

	private static final String EVALUATE_CODE = "For all points do {" // 0
			+ "\n   Calculate rating // Euclidean distance between TargetPoint and CurrentPoint" // 1
			+ "\n}";
	private String SELECT_CODE;
	private static final String MUTATE_CODE = "For all points do {" // 0
			+ "\n   Mutate Points randomly " // 1
			+ "\n   //new Coordinates("
			+ "\n   //   coords.getX() + rand.nextInt(mutationStrength) * directionX,"
			+ "\n   //   coords.getY() + rand.nextInt(mutationStrength) * directionY"
			+ "\n   //   )" + "\n}"; // 2

	private static final String RECOMBINE_CODE = "For all points do {" // 0
			+ "\n Select two points randomly" // 1
			+ "\n   Generate a new point by recombining the attributes of the two points" // 2
			+ "\n   // newX = (p1.getCoordinates().getX() + p2.getCoordinates().getX()) / 2" // 3
			+ "\n   // newY = (p1.getCoordinates().getY() + p2.getCoordinates().getY()) / 2" // 4
			+ "\n}"; // 2

	public EvoAlgoGenerator() {
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		// PointArea
		Integer widthPointArea = (Integer) primitives.get("widthPointArea");
		Integer heightPointArea = (Integer) primitives.get("heightPointArea");

		// TargetPoint
		Integer xTargetPoint = (Integer) primitives.get("xTargetPoint");
		Integer yTargetPoint = (Integer) primitives.get("yTargetPoint");

		// General
		Integer numberOfPoints = (Integer) primitives.get("numberOfPoints");
		Integer mutationStrength = (Integer) primitives.get("mutationStrength");
		Integer maxIterationSteps = (Integer) primitives
				.get("maxIterationSteps");

		boolean isValid = true;
		StringBuilder errorMessage = new StringBuilder("The following errors occurred:");
		
		if (numberOfPoints < 3) {
			errorMessage.append("\n - numberOfPoints must be at least 3");
			isValid = false;
		}
		if (numberOfPoints > 100) {
			errorMessage.append("\n - numberOfPoints must be at most 100");
			isValid = false;
		}
		if (maxIterationSteps < 1) {
			errorMessage.append("\n - maxIterationSteps must be at least 1");
			isValid = false;
		}
		if (maxIterationSteps > 100) {
			errorMessage.append("\n - maxIterationSteps must be at most 100");
			isValid = false;
		}
		if (widthPointArea < 100) {
			errorMessage.append("\n - widthPointArea must be at least 100");
			isValid = false;
		}
		if (widthPointArea > 2000) {
			errorMessage.append("\n - widthPointArea must be at most 2000");
			isValid = false;
		}
		if (heightPointArea < 100) {
			errorMessage.append("\n - heightPointArea must be at least 100");
			isValid = false;
		}
		if (heightPointArea > 2000) {
			errorMessage.append("\n - heightPointArea must be at most 2000");
			isValid = false;
		}
		if (mutationStrength < 1) {
			errorMessage.append("\n - mutationStrength must be at least 1");
			isValid = false;
		}
		if (mutationStrength > widthPointArea / 2) {
			errorMessage.append(String.format("\n - mutationStrength must be at most %d", widthPointArea / 2));
			isValid = false;
		}
		if (mutationStrength > heightPointArea / 2) {
			errorMessage.append(String.format("\n - mutationStrength must be at most %d", heightPointArea / 2));
			isValid = false;
		}
		if (xTargetPoint < 35) {
			errorMessage.append("\n - xTargetPoint must be at least 35");
			isValid = false;
		}
		if (xTargetPoint > widthPointArea + 5) {
			errorMessage.append(String.format("\n - xTargetPoint must be at most %d", widthPointArea + 5));
			isValid = false;
		}
		if (yTargetPoint < 35) {
			errorMessage.append("\n - yTargetPoint must be at least 35");
			isValid = false;
		}
		if (yTargetPoint > heightPointArea + 5) {
			errorMessage.append(String.format("\n - yTargetPoint must be at most %d", heightPointArea + 5));
			isValid = false;
		}

		if (!isValid) {
			showError(errorMessage.toString());
			return false;
		}
		
		if (numberOfPoints * maxIterationSteps > 100) {
			boolean result = showWarning("The chosen values of the properties \"numberOfPoints\" and \"maxIterationSteps\""
					+ "\nmay result in a large amount of animation steps. \n\nDo you want to cancel?");
			return result;
		}
		
		return isValid;
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, "Input validation error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	private boolean showWarning(String message) {
		int result = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), message, "Performance warning", JOptionPane.YES_NO_OPTION);
		return result != JOptionPane.YES_OPTION;
	}

	private void initializeVariables() {
		
		numberOfIterations = 1;

		// Point area
		graphAreaUpperLeft = new Coordinates(20, 20);
		graphAreaLowerRight = new Coordinates(20 + widthPointArea,
				20 + heightPointArea);

		// Text coordinates
		headerCoordinates = new Coordinates(widthPointArea + 40, 15);
		iterationHeaderCoordinates = new Coordinates(widthPointArea + 40, 25);
		partHeaderCoordinates = new Coordinates(widthPointArea + 40, 280);

		// Text Properties
		Font titleTemp = (Font) titleProperties.get("font");
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				titleTemp.getFamily(), Font.BOLD, textSize + 4));

		Font subTitleTemp = (Font) subTitleProperties.get("font");
		subTitleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				subTitleTemp.getFamily(), Font.BOLD, textSize + 2));

		Font textTemp = (Font) textProperties.get("font");
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				textTemp.getFamily(), Font.PLAIN, textSize));

		// SourceCode properties
		sourceCodeHeaderProps = new SourceCodeProperties();
		sourceCodeHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.BOLD, textSize));

		// Target point
		targetCoordinates = new Coordinates(xTargetPoint, yTargetPoint);

		ratingLineProperties = new GraphProperties();
		ratingLineProperties.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY,
				Color.WHITE);
		ratingLineProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE);
		ratingLineProperties.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY,
				ratingLineColor);

		ratingLineProperties.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY,
				Boolean.FALSE);
		ratingLineProperties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY,
				Boolean.FALSE);
		ratingLineProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 50);

		pointCounter = numberOfPoints;

		SELECT_CODE = "For all points do {" // 0
				+ "\n   Select point with highest rating" // 1
				+ "\n   If numberOfPoints > maxNumberOfPoints / 2 { // numberOfPoints="
				+ points + ", maxNumberOfPoints=" + numberOfPoints // 2
				+ "\n      Remove point" // 3
				+ "\n   }" // 4
				+ "\n   else {" // 5
				+ "\n      Keep point" // 6
				+ "\n}"; // 7

	}

	private String getIterationCode(int iteration) {
		String iterationCode = "Initialize()" // 0
				+ "\nWhile (iterations <= maxIterations) { // iterations="
				+ iteration + ", maxIterations=" + maxIterationSteps // 1
				+ "\n   Evaluate()" // 2
				+ "\n   Select()" // 3
				+ "\n   Mutate()" // 4
				+ "\n   Recombine()" // 5
				+ "\n}" // 6
				+ "\nEvaluate"; // 7
		return iterationCode;
	}

	private void start() {

		initializeVariables();

		showStartPage();

		/**
		 * Create static areas
		 */
		// Create graph area
		lang.newRect(graphAreaUpperLeft, graphAreaLowerRight, "graphAreas",
				null, pointArea);

		algoHeaderText = lang.newText(headerCoordinates, algorithmus,
				"headerAlgo", null, titleProperties);

		iterationCode = lang.newSourceCode(iterationHeaderCoordinates,
				"iterationSourceCode", null, textProperties);
		iterationCode.addMultilineCode(getIterationCode(numberOfIterations),
				"iterationCode", null);

		initialize();

		// Algorithm
		while (numberOfIterations <= maxIterationSteps) {
			iterationCode.hide();
			iterationCode = lang.newSourceCode(iterationHeaderCoordinates,
					"iterationSourceCode", null, textProperties);
			iterationCode
					.addMultilineCode(getIterationCode(numberOfIterations),
							"iterationCode", null);

			iterationCode.highlight(1);
			lang.nextStep("Step " + numberOfIterations + ": Start a new iteration.");
			iterationCode.unhighlight(1);

			iterationCode.highlight(2);
			evaluate();
			iterationCode.unhighlight(2);
			lang.nextStep();

			select();
			mutate();
			recombine();
			numberOfIterations++;
		}

		iterationCode.highlight(7);
		evaluate();
		iterationCode.unhighlight(7);
		lang.nextStep();

		showResult();

	}

	private void initialize() {

		lang.nextStep();

		iterationCode.highlight(0);

		Text initializeHeaderText = lang.newText(partHeaderCoordinates,
				"Initialize()", "initializeHeaderText", null,
				subTitleProperties);

		String initializeCodeText = "Create target point" // 0
				+ "\nFor numberOfPoints do { // numberOfPoints="
				+ numberOfPoints // 1
				+ "\n   Create a point with random coordinates" // 2
				+ "\n   // Coordinates(rand.nextInt("
				+ widthPointArea
				+ "-30) + 35, rand.nextInt(" + heightPointArea
				+ "-30) + 35)"
				+ "\n}";

		SourceCode initializeCode = lang.newSourceCode(new Offset(0,
				offsetCodeFromHeader, "initializeHeaderText", null),
				"initializeCodeText", null, textProperties);
		initializeCode.addMultilineCode(initializeCodeText, "initializeCode",
				null);

		lang.nextStep("Initialize Points.");

		initializeCode.highlight(0);
		Point target = new Point(targetCoordinates, "target", true,
				targetPointProperties);

		lang.nextStep();

		initializeCode.unhighlight(0);
		initializeCode.highlight(1);

		setNumberOfPointsText(0);
		for (int i = 0; i < numberOfPoints; i++) {
			lang.nextStep();
			initializeCode.unhighlight(1);

			initializeCode.highlight(2);
			initializeCode.highlight(3);

			Coordinates coords = new Coordinates(
					rand.nextInt(widthPointArea - 30) + 35,
					rand.nextInt(heightPointArea - 30) + 35);
			Point p = new Point(coords, "p" + i, false, pointProperties);
			p.setHighlight(true, selectPointColor);
			points.add(p);
			setNumberOfPointsText(points.size());

			lang.nextStep();
			p.setHighlight(false);
			initializeCode.unhighlight(2);
			initializeCode.unhighlight(3);
			initializeCode.highlight(1);
		}
		initializeCode.hide();
		iterationCode.unhighlight(0);
		initializeHeaderText.hide();
	}

	private void showStartPage() {
		Text headerText = lang.newText(new Coordinates(10, 10), algorithmus,
				"headerAlgo", null, titleProperties);

		SourceCode startIntroduction = lang.newSourceCode(
				new OffsetFromLastPosition(0, 15), "startIntroductionText",
				null, textProperties);
		startIntroduction.addMultilineCode(START_TEXT_INTRODUCTION,
				"startIntroductionText", null);
		SourceCode startText = lang.newSourceCode(new OffsetFromLastPosition(0,
				60), "startText", null, textProperties);
		startText.addMultilineCode(START_TEXT_DETAILS, "startText", null);

		lang.nextStep();

		// Evaluate
		SourceCode evaluateText = lang.newSourceCode(
				new OffsetFromLastPosition(0, 60), "evaluateStartHeaderText",
				null, sourceCodeHeaderProps);
		evaluateText.addMultilineCode(EVALUATE_START_TEXT,
				"evaluateStartHeaderText", null);
		SourceCode evaluate = lang.newSourceCode(new OffsetFromLastPosition(0,
				20), "evaluateStartText", null, textProperties);
		evaluate.addMultilineCode(EVALUATE_TEXT, "evaluateStartText", null);

		lang.nextStep();

		// Select
		SourceCode selectText = lang.newSourceCode(new OffsetFromLastPosition(
				0, 40), "selectStartHeaderText", null, sourceCodeHeaderProps);
		selectText.addMultilineCode(SELECT_START_TEXT, "selectStartHeaderText",
				null);
		SourceCode select = lang.newSourceCode(
				new OffsetFromLastPosition(0, 20), "selectStartText", null,
				textProperties);
		select.addMultilineCode(SELECT_TEXT, "selectStartText", null);

		lang.nextStep();

		// Mutate
		SourceCode mutateText = lang.newSourceCode(new OffsetFromLastPosition(
				0, 42), "mutateStartHeaderText", null, sourceCodeHeaderProps);
		mutateText.addMultilineCode(MUTATE_START_TEXT, "mutateStartHeaderText",
				null);
		SourceCode mutate = lang.newSourceCode(
				new OffsetFromLastPosition(0, 20), "mutateStartText", null,
				textProperties);
		mutate.addMultilineCode(MUTATE_TEXT, "mutateStartText", null);

		lang.nextStep();

		// Recombine
		SourceCode recombineText = lang.newSourceCode(
				new OffsetFromLastPosition(0, 22), "recombineStartHeaderText",
				null, sourceCodeHeaderProps);
		recombineText.addMultilineCode(RECOMBINE_START_TEXT,
				"recombineStartHeaderText", null);
		SourceCode recombine = lang.newSourceCode(new OffsetFromLastPosition(0,
				20), "recombineStartText", null, textProperties);
		recombine.addMultilineCode(RECOMBINE_TEXT, "recombineStartText", null);

		lang.nextStep();
		// Hide all texts
		headerText.hide();

		startIntroduction.hide();
		startText.hide();

		evaluateText.hide();
		evaluate.hide();

		selectText.hide();
		select.hide();

		mutateText.hide();
		mutate.hide();

		recombineText.hide();
		recombine.hide();
	}

	private void evaluate() {

		Text evaluateHeaderText = lang.newText(partHeaderCoordinates,
				"Evaluate()", "evaluateHeaderText", null, subTitleProperties);
		SourceCode evaluateCode = lang.newSourceCode(new Offset(0,
				offsetCodeFromHeader, "evaluateHeaderText", null),
				"evaluateCodeText", null, textProperties);
		evaluateCode.addMultilineCode(EVALUATE_CODE, "evaluateCode", null);

		if(numberOfIterations > maxIterationSteps){
			lang.nextStep("Result: Evaluate step.");
		} else {
			lang.nextStep("Step " + numberOfIterations + ": Evaluate step.");
		}

		evaluateCode.highlight(0);

		// Calculate the rating for every Point
		for (Point point : points) {
			lang.nextStep();
			evaluateCode.unhighlight(0);
			evaluateCode.highlight(1);

			point.setHighlight(true, selectPointColor);

			// Calculating of rating
			int rating = (int) Math.sqrt(Math.pow(point.getCoordinates().getX()
					- targetCoordinates.getX(), 2)
					+ Math.pow(point.getCoordinates().getY()
							- targetCoordinates.getY(), 2));
			point.setRating(rating);

			// Draw line
			Coordinates[] coordinats = {
					new Coordinates(point.getCoordinates().getX(), point
							.getCoordinates().getY() - 8),
					new Coordinates(xTargetPoint, yTargetPoint - 8) };
			int[][] matrix = new int[2][2];
			String[] names = new String[2];

			matrix[0][0] = 0;
			matrix[0][1] = 1;
			matrix[1][0] = 1;
			matrix[1][1] = 0;
			names[0] = "";
			names[1] = "";

			Graph line = lang.newGraph("line", matrix, coordinats, names, null,
					ratingLineProperties);

			// check if point is bestPoint
			if (bestPoint == null) {
				bestPoint = point;
			} else if (rating < bestPoint.getRating()) {
				bestPoint = point;
			}
			lang.nextStep();
			point.setHighlight(false);
			evaluateCode.unhighlight(1);
			evaluateCode.highlight(0);
			line.hide();
		}

		evaluateCode.hide();

		evaluateHeaderText.hide();

	}

	private void select() {

		iterationCode.highlight(3);

		Text selectHeaderText = lang.newText(partHeaderCoordinates, "Select()",
				"selectHeaderText", null, subTitleProperties);
		SourceCode selectCode = lang.newSourceCode(new Offset(0,
				offsetCodeFromHeader, "selectHeaderText", null),
				"selectCodeText", null, textProperties);
		selectCode.addMultilineCode(SELECT_CODE, "selectCode", null);

		lang.nextStep("Step " + numberOfIterations + ": " + "Select step.");

		// remove points with bad rating
		Collections.sort(points);
		int numberDeletingPoints = (int) Math.floor(numberOfPoints / 2);

		for (int i = 0; i < points.size();) {

			Point point = points.get(i);

			selectCode.highlight(0);
			lang.nextStep();
			selectCode.unhighlight(0);

			point.setHighlight(true);
			selectCode.highlight(1);
			lang.nextStep();
			selectCode.unhighlight(1);

			selectCode.highlight(2);
			lang.nextStep();
			selectCode.unhighlight(2);

			if (points.size() > numberOfPoints - numberDeletingPoints) {
				point.setHighlight(true, removePointColor);
				selectCode.highlight(3);
				lang.nextStep();
				selectCode.unhighlight(3);
				point.setVisibility(false);
				points.remove(point);
				setNumberOfPointsText(points.size());
			} else {
				selectCode.highlight(6);
				point.setHighlight(true, keepPointColor);
				lang.nextStep();
				i++;
			}
			selectCode.unhighlight(3);
			selectCode.unhighlight(6);
		}

		// remove highlights
		for (Point point : points) {
			point.setHighlight(false);
		}

		selectHeaderText.hide();
		iterationCode.unhighlight(3);
		selectCode.hide();
	}

	private void mutate() {

		iterationCode.highlight(4);

		Text mutateHeaderText = lang.newText(partHeaderCoordinates, "Mutate()",
				"mutateHeaderText", null, subTitleProperties);
		SourceCode mutateCode = lang.newSourceCode(new Offset(0,
				offsetCodeFromHeader, "mutateHeaderText", null),
				"mutateCodeText", null, textProperties);
		mutateCode.addMultilineCode(MUTATE_CODE, "mutateCode", null);

		lang.nextStep("Step " + numberOfIterations + ": " + "Mutate step.");

		for (Point point : points) {
			mutateCode.highlight(0);
			lang.nextStep();
			mutateCode.unhighlight(0);
			mutateCode.highlight(1);
			mutateCode.highlight(2);
			mutateCode.highlight(3);
			mutateCode.highlight(4);
			mutateCode.highlight(5);

			point.setHighlight(true, selectPointColor);
			lang.nextStep();

			// mutate by sliding coordinates
			Coordinates coords = point.getCoordinates();
			int directionX = (int) Math.signum(rand.nextInt()); // +1 or -1
			int directionY = (int) Math.signum(rand.nextInt());

			Coordinates newCoords = new Coordinates(coords.getX()
					+ rand.nextInt(mutationStrength) * directionX,
					coords.getY() + rand.nextInt(mutationStrength) * directionY);

			// keep Coordinates in cage
			if (newCoords.getX() < 20 + 15)
				newCoords = new Coordinates(35, newCoords.getY());
			if (newCoords.getX() > 20 + widthPointArea - 15)
				newCoords = new Coordinates(heightPointArea - 15,
						newCoords.getY());
			if (newCoords.getY() < 20 + 15)
				newCoords = new Coordinates(newCoords.getX(), 35);
			if (newCoords.getY() > 20 + heightPointArea - 15)
				newCoords = new Coordinates(newCoords.getX(),
						widthPointArea - 15);

			point.setCoordinates(newCoords);
			point.setRating(Integer.MAX_VALUE);

			lang.nextStep();
			mutateCode.unhighlight(1);
			mutateCode.unhighlight(2);
			mutateCode.unhighlight(3);
			mutateCode.unhighlight(4);
			mutateCode.unhighlight(5);
			point.setHighlight(false);
		}

		mutateHeaderText.hide();
		iterationCode.unhighlight(4);
		mutateCode.hide();
		lang.nextStep();
	}

	private void recombine() {

		iterationCode.highlight(5);
		Text recombineHeaderText = lang.newText(partHeaderCoordinates,
				"Recombine()", "recombineHeaderText", null, subTitleProperties);
		SourceCode recombineCode = lang.newSourceCode(new Offset(0,
				offsetCodeFromHeader, "recombineHeaderText", null),
				"recombineCodeText", null, textProperties);
		recombineCode.addMultilineCode(RECOMBINE_CODE, "recombineCode", null);

		lang.nextStep("Step " + numberOfIterations + ": " + "Recombine step.");

		List<Point> newPoints = new ArrayList<Point>();
		List<Integer> occuredPointCombinations = new ArrayList<Integer>();
		while (points.size() + newPoints.size() < numberOfPoints) {
			recombineCode.highlight(0);
			lang.nextStep();
			recombineCode.unhighlight(0);

			Point p1 = points.get(rand.nextInt(points.size()));
			Point p2 = points.get(rand.nextInt(points.size()));

			// try another p2 if p1 == p2 or combination of points occurred
			// already and there is no free point combination
			boolean combinationOccurred = occuredPointCombinations.contains(p1
					.hashCode() * p2.hashCode());
			boolean freeCombinations = points.size() > occuredPointCombinations
					.size() * 2;
			while (p1.equals(p2) || combinationOccurred && !freeCombinations) {
				p2 = points.get(rand.nextInt(points.size()));
			}

			occuredPointCombinations.add(p1.hashCode() + p2.hashCode());

			p1.setHighlight(true, selectPointColor);
			p2.setHighlight(true, selectPointColor);
			recombineCode.highlight(1);
			lang.nextStep();

			recombineCode.unhighlight(1);
			recombineCode.highlight(2);
			recombineCode.highlight(3);
			recombineCode.highlight(4);

			int newX = (p1.getCoordinates().getX() + p2.getCoordinates().getX()) / 2;
			int newY = (p1.getCoordinates().getY() + p2.getCoordinates().getY()) / 2;

			Coordinates newCoords = new Coordinates(newX, newY);
			Point newPoint = new Point(newCoords, "p" + pointCounter++, false,
					pointProperties);
			newPoint.setHighlight(true, keepPointColor);

			newPoints.add(newPoint);
			setNumberOfPointsText(points.size() + newPoints.size());

			lang.nextStep();

			recombineCode.unhighlight(2);
			recombineCode.unhighlight(3);
			recombineCode.unhighlight(4);

			p1.setHighlight(false);
			p2.setHighlight(false);
			newPoint.setHighlight(false);
		}
		points.addAll(newPoints);

		recombineHeaderText.hide();
		iterationCode.unhighlight(5);
		recombineCode.hide();
		lang.nextStep();
	}

	private void showResult() {

		// Hide the header and the iteration code
		algoHeaderText.hide();
		iterationCode.hide();

		for (Point point : points) {
			if (!point.equals(bestPoint)) {
				point.setVisibility(false);
			}
		}
		bestPoint.setHighlight(true, keepPointColor);
		setNumberOfPointsText(1);

		// Calculating of rating
		int rating = (int) Math.sqrt(Math.pow(bestPoint.getCoordinates().getX()
				- targetCoordinates.getX(), 2)
				+ Math.pow(bestPoint.getCoordinates().getY()
						- targetCoordinates.getY(), 2));
		bestPoint.setRating(rating);

		lang.newText(headerCoordinates, "Emphasize Result",
				"headerTextEmphasizeResult", null, titleProperties);

		String resultText = "Number of iteration steps made: "
				+ numberOfIterations
				+ "\nBest rating: "
				+ bestPoint.getRating()
				+ "\nComplexity: linear to the number of points"
				+ "\n \nThis point has (most likely) a better rating than the points at the beginning. "
				+ "\nIn our example this point is closer to the target point than the points at the beginning."
				+ "\nKeep in mind that you do not find the best (closest) point with that algorithm but a sufficiently good point."
				+ "\n \nSome other population based meta-heuristic methods are:"
				+ "\nFirefly algorithm, Harmony search, Memetic algorithm, Gaussian adaptation";

		SourceCode scResultText = lang.newSourceCode(
				new OffsetFromLastPosition(0, 20), "headerTextEmphasizeResult",
				null, textProperties);
		scResultText.addMultilineCode(resultText, "resultText", null);

		lang.nextStep("End of algorithm. Emphasize result.");
	}

	private void setNumberOfPointsText(int currentPoints) {
		/*
		 * optional display of number of points String text =
		 * String.format("Points: %d/%d", currentPoints, numberOfPoints);
		 * 
		 * if (numberOfPointsText == null) { numberOfPointsText =
		 * lang.newText(new Coordinates(20, 0), text, "numberOfPointsText",
		 * null, numberOfPointsTextProp); } else {
		 * numberOfPointsText.setText(text, null, null); }
		 */
	}

	public void init() {
		lang = new AnimalScript("Evolutionary algorithm [EN]",
				"Andreas Altenkirch, Stefan Hoerndler", 800, 700);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		titleProperties = (TextProperties) props.getPropertiesByName("title");
		subTitleProperties = (TextProperties) props
				.getPropertiesByName("subTitle");
		textProperties = (SourceCodeProperties) props
				.getPropertiesByName("text");

		// PointArea
		pointArea = (RectProperties) props.getPropertiesByName("pointArea");
		widthPointArea = (Integer) primitives.get("widthPointArea");
		heightPointArea = (Integer) primitives.get("heightPointArea");

		// TargetPoint
		targetPointProperties = (EllipseProperties) props
				.getPropertiesByName("targetPoint");
		xTargetPoint = (Integer) primitives.get("xTargetPoint");
		yTargetPoint = (Integer) primitives.get("yTargetPoint");

		// Point
		pointProperties = (EllipseProperties) props
				.getPropertiesByName("point");
		selectPointColor = (Color) primitives.get("selectPointColor");
		keepPointColor = (Color) primitives.get("keepPointColor");
		removePointColor = (Color) primitives.get("removePointColor");
		ratingLineColor = (Color) primitives.get("calculateRatingAnimation");

		// General
		numberOfPoints = (Integer) primitives.get("numberOfPoints");
		mutationStrength = (Integer) primitives.get("mutationStrength");
		maxIterationSteps = (Integer) primitives.get("maxIterationSteps");

		numberOfPointsTextProp = new TextProperties();
		numberOfPointsTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.BOLD, textSize));

		start();

		return lang.toString();
	}

	// TODO delete
	public String generateDummy(int numberOfPoints, int mutationStrength,
			int maxIterationSteps) {

		// highlightColor = Color.YELLOW;
		pointArea = new RectProperties("name");
		this.numberOfPoints = numberOfPoints;
		ratingLineColor = Color.GRAY;
		// targetPointColor = new Color(120,200,200);
		removePointColor = Color.RED;
		keepPointColor = Color.GREEN;
		selectPointColor = Color.YELLOW;
		this.mutationStrength = mutationStrength;
		this.maxIterationSteps = maxIterationSteps;

		textProperties = new SourceCodeProperties();
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, textSize));
		textProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.RED);

		numberOfPointsTextProp = new TextProperties();
		numberOfPointsTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.PLAIN, textSize));

		titleProperties = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, textSize + 4));

		subTitleProperties = new TextProperties();
		subTitleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, textSize));

		xTargetPoint = 50;
		yTargetPoint = 50;

		widthPointArea = 100;
		heightPointArea = 100;

		targetPointProperties = new EllipseProperties("targetPoint");
		targetPointProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);

		pointProperties = new EllipseProperties("point");

		start();

		return lang.toString();
	}

	public String getName() {
		return "Evolutionary algorithm [EN]";
	}

	public String getAlgorithmName() {
		return "Evolutionary algorithm";
	}

	public String getAnimationAuthor() {
		return "Andreas Altenkirch, Stefan Hoerndler";
	}

	public String getDescription() {
		return "An evolutionary algorithm is inspired by biological evolution such as reproduction, mutation, recombination und selection. There are stochastical algorithms so they don't find the best solution but a sufficiently good solution."
				+ "\n"
				+ "In the generator we solve a problem to get the points to a target, which they don't know. Therefore we have 4 phases within an iteration. They are as follows:"
				+ "\n"
				+ "\n"
				+ "Evaluate:"
				+ "\n"
				+ "In the evalutation phase every point is rated with a weight function for the distance between the target and the point. In our example we take the euclidean distance as the weight function."
				+ "\n"
				+ "\n"
				+ "Select:"
				+ "\n"
				+ "In the selection phase have of the points are deleted and half of the points are kept. Only the best ratings are kept."
				+ "\n"
				+ "\n"
				+ "Mutate:"
				+ "\n"
				+ "In the mutation phase the coordingates of the points are randomly mutated."
				+ "\n"
				+ "\n"
				+ "Recombine:"
				+ "\n"
				+ "In the recombination phase an new point is created with a recombination of two points.";
	}

	public String getCodeExample() {
		return "Initialize" + "\n" + "For each iteration do {" + "\n"
				+ "   Evaluate" + "\n" + "   Select" + "\n" + "   Mutate"
				+ "\n" + "   Recombine" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * 
	 * @author Andreas Altenkirch, Stefan Hoerndler
	 * 
	 */
	private class Point implements Comparable<Point> {
		private Ellipse ellipse;
		private String ellipseName;

		private Text text;
		private String label;
		private String textName;
		private Boolean isTarget;

		private Coordinates coordinates;
		private EllipseProperties properties;
		private int rating = Integer.MAX_VALUE;

		public Point(Coordinates coordinates, String name, Boolean isTarget,
				EllipseProperties elipseProperties) {
			this.coordinates = coordinates;
			this.ellipseName = "e_" + name;
			this.textName = "t_" + name;

			this.properties = elipseProperties;

			this.isTarget = isTarget;
			if (isTarget) {
				// properties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				// targetPointColor);
				properties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
						isTarget);
				label = "T";
			} else {
				// properties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				// highlightColor);
				properties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
						isTarget);
			}

			update();
		}

		public Coordinates getCoordinates() {
			return coordinates;
		}

		public void setCoordinates(Coordinates coordinates) {
			this.coordinates = coordinates;
			update();
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;

			if (!isTarget) {
				this.label = String.valueOf(rating);
			}

			update();
		}

		public String getEllipseName() {
			return ellipseName;
		}

		public String getTextName() {
			return textName;
		}

		public Boolean isTarget() {
			return isTarget;
		}

		public void setHighlight(Boolean isHighlighted, Color color) {
			properties.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
			setHighlight(isHighlighted);
		}

		public void setHighlight(Boolean isHighlighted) {
			properties.set(AnimationPropertiesKeys.FILLED_PROPERTY,
					isHighlighted);
			update();
		}

		/**
		 * Deletes the old Point and creates a new one based on the changed
		 * properties.
		 */
		public void update() {
			if (ellipse != null) {
				ellipse.hide();
			}

			ellipse = lang.newEllipse(coordinates, new Coordinates(15, 15),
					ellipseName, null, this.properties);

			if (text != null) {
				text.hide();
			}

			if (!isTarget) {
				if (rating == Integer.MAX_VALUE) {
					label = "?";
				} else {
					label = String.valueOf(rating);
				}
			}

			text = lang.newText(calcLabelCoords(), label, textName, null);
		}

		public void setVisibility(Boolean isVisible) {
			if (isVisible) {
				ellipse.show();
				text.show();
			} else {
				ellipse.hide();
				text.hide();
			}
		}

		private Coordinates calcLabelCoords() {
			int dx = 3 * label.length();
			return new Coordinates(coordinates.getX() - dx,
					coordinates.getY() - 8);
		}

		@Override
		public int compareTo(Point other) {
			return other.getRating() - this.rating;
		}

		public boolean equals(Object obj) {
			Point other = (Point) obj;
			if (this.getCoordinates().equals(other.getCoordinates())
					&& this.getTextName().equals(other.getTextName())) {
				return true;
			}
			return false;
		}

		public int hashCode() {
			return this.getTextName().hashCode();
		}
	}
}
