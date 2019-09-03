package generators.graphics.regiongrowing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.helpers.Coordinate;
import generators.graphics.helpers.CountingArray;
import generators.graphics.helpers.CountingList;
import generators.graphics.helpers.ImageMatrix;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class RegionGrowing implements ValidatingGenerator {
	private Language lang;
	private TextProperties textProperties;
	private SourceCodeProperties sourceCodeProperties;
	private boolean showLegend;
	private int comparisonTolerance;
	private int[][] image;
	private int comparisonValue;
//	private SourceCodeProperties descriptionsProperties;
	private TriangleProperties nonVisitedProperties;
	private TriangleProperties visitedProperties;
	private TextProperties titleProperties;
	private RectProperties neighborsProperties;
	private CircleProperties seedsVisualsProperties;
	private RectProperties currentProperties;
	private RectProperties currentNeighborProperties;
	private RectProperties roiProperties;
	private int[][] seeds;

	private CountingList<Coordinate> nonVisitedCoordinates;
	private CountingList<Coordinate> visitedCoordinates;
	private Coordinates matrixTopLeft = new Coordinates(500, 100);

	SourceCode sc, compareSc;
	Coordinates topLeft = new Coordinates(500, 300);
	Text title, nonVisitedCounterText, visitedCounterText, imageCounterText;
	Text nonVisited;
	TwoValueView tvvNonVisited;
	TwoValueView tvvVisited;
	TwoValueView tvvImage;

	public void init() {
		lang = new AnimalScript("Region Growing Algorithm",
				"Lucas Rothamel; Manuel Weiel", 800, 600);
		nonVisitedCoordinates = new CountingList<Coordinate>(null, null);
		visitedCoordinates = new CountingList<Coordinate>(null, null);

		initProperties();
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		textProperties = (TextProperties) props.getPropertiesByName("text");
		sourceCodeProperties = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
		showLegend = (Boolean) primitives.get("showLegend");
		comparisonTolerance = (Integer) primitives.get("comparisonTolerance");
		image = (int[][]) primitives.get("image");
		comparisonValue = (Integer) primitives.get("comparisonValue");
//		descriptionsProperties = (SourceCodeProperties) props
//				.getPropertiesByName("descriptions");
		nonVisitedProperties = (TriangleProperties) props
				.getPropertiesByName("nonVisited");
		visitedProperties = (TriangleProperties) props
				.getPropertiesByName("visited");
		titleProperties = (TextProperties) props.getPropertiesByName("title");
		neighborsProperties = (RectProperties) props
				.getPropertiesByName("neighbors");
		seedsVisualsProperties = (CircleProperties) props
				.getPropertiesByName("seedsVisuals");
		currentProperties = (RectProperties) props
				.getPropertiesByName("current");
		currentNeighborProperties = (RectProperties) props
				.getPropertiesByName("currentNeighbor");
		roiProperties = (RectProperties) props.getPropertiesByName("roi");
		seeds = (int[][]) primitives.get("seeds");

		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		Integer[][] vals = new Integer[image.length][];
		for (int i = 0; i < image.length; i++) {
			vals[i] = new Integer[image[i].length];
			for (int j = 0; j < image[i].length; j++)
				vals[i][j] = image[i][j];
		}
		setComparison(comparisonValue, comparisonTolerance);
		Coordinate[] seedsCoordinates = new Coordinate[seeds[0].length];
		for (int i = 0; i < seeds[0].length; i++) {
			seedsCoordinates[i] = new Coordinate(seeds[0][i], seeds[1][i]);
		}
		grow(vals, seedsCoordinates);
		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "Region Growing Algorithm";
	}

	public String getAlgorithmName() {
		return "Region Growing";
	}

	public String getAnimationAuthor() {
		return "Lucas Rothamel, Manuel Weiel";
	}

	public String getDescription() {
		return "Region growing is a simple image segmentation method. The idea behind the algorithm is to"
				+ "\n"
				+ "find segmentations of a grayscale image. "
				+ "\n"
				+ "In our case, a segmentation into two regions is carried out - a region of interest (ROI),"
				+ "\n"
				+ "and an (implicit) region of non-interest. At first, a number of seed points are selected."
				+ "\n"
				+ "These seed points are always added to the ROI. Then, each neighbour of a seed point is"
				+ "\n"
				+ "checked against an acceptance criterium - and if the criterium matches, is added to the"
				+ "\n"
				+ "ROI. Neighbours of points inside the ROI continue to be checked and added until no more"
				+ "\n" + "points can be checked, and the ROI has been found.";
	}

	public String getCodeExample() {
		return "for (Coordinate c: seeds) {" + "\n"
				+ "  nonVisitedCoordinates.add(c);" + "\n"
				+ "  visitedCoordinates.add(c);" + "\n" + "}" + "\n"
				+ "while (!nonVisitedCoordinates.isEmpty()) {" + "\n"
				+ "  v = nonVisitedCoordinates.get(0);" + "\n"
				+ "  roi.add(v);" + "\n" + "  nonVisitedCoordinates.remove(v);"
				+ "\n"
				+ "  neighbors = v.neighbors(image[0].length, image.length);"
				+ "\n" + "  for (Coordinate neighbor: neighbors) {" + "\n"
				+ "    if (!visitedCoordinates.contains(neighbor)) {" + "\n"
				+ "      visitedCoordinates.add(neighbor);" + "\n"
				+ "      nonVisitedCoordinates.remove(neighbor);" + "\n"
				+ "      if (compare(image[neighbor.getY()][neighbor.getX()]))"
				+ "\n" + "        nonVisitedCoordinates.add(neighbor);" + "\n"
				+ "    }" + "\n" + "  }" + "\n" + "}";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.US;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	// color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0)
	// highlightTextColor (0, 0, 0) highlightBackColor (0, 0, 0)
	private void initProperties() {
		// sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
		// Font("Monospaced", Font.PLAIN, 12));
		// scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
		// Color.BLUE);
		// scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// "Monospaced", Font.PLAIN, 12));
		// scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
		// Color.RED);
		// scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		// arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
		// Font("Monospaced", Font.PLAIN, 12));
	}

	public void grow(Integer[][] img, Coordinate[] seeds) {
		CountingArray<Integer> image = new CountingArray<Integer>(null, null,
				img);

		showIntroSlides();
		printSourceCode();
		ImageMatrix im = new ImageMatrix(img, seeds, lang, matrixTopLeft);
		im.setCurrentNeighborProperties(currentNeighborProperties);
		im.setCurrentProperties(currentProperties);
		im.setNeighborProperties(neighborsProperties);
		im.setNonVisitedProperties(nonVisitedProperties);
		im.setRoiProperties(roiProperties);
		im.setSeedProperties(seedsVisualsProperties);
		im.setTextProperties(textProperties);
		im.setVisitedProperties(visitedProperties);
		im.init();
		if (showLegend)
			im.drawLegend();

		nonVisitedCounterText = lang.newText(new Offset(0, 30, sc,
				AnimalScript.DIRECTION_SW), "nonVisitedCoordinates:",
				"nonVisitedCoordinatesText", null, textProperties);
		tvvNonVisited = initCounter(new Offset(0, 10, nonVisitedCounterText,
				AnimalScript.DIRECTION_SW), nonVisitedCoordinates);
		visitedCounterText = lang.newText(new Offset(0, 70,
				nonVisitedCounterText, AnimalScript.DIRECTION_SW),
				"visitedCoordinates:", "visitedCoordinatesText", null,
				textProperties);
		tvvVisited = initCounter(new Offset(0, 10, visitedCounterText,
				AnimalScript.DIRECTION_SW), visitedCoordinates);

		nonVisited = lang.newText(new Offset(10, 0, nonVisitedCounterText,
				AnimalScript.DIRECTION_NE), "", "nonVisitedLabel", null,
				textProperties);

		imageCounterText = lang.newText(new Offset(0, 70, nonVisited,
				AnimalScript.DIRECTION_SW), "image:", "imageCounterText", null,
				textProperties);
		tvvImage = initCounter(new Offset(0, 10, imageCounterText,
				AnimalScript.DIRECTION_SW), image);
		printCompareSourceCode();

		lang.nextStep("Beginning");

		sc.highlight("forcoordinates");
		for (Coordinate c : seeds) {
			sc.highlight("nonvisadd");
			nonVisitedCoordinates.add(c);
			updateImageText(nonVisited, nonVisitedCoordinates);
			im.highlightCoordinate(c, ImageMatrix.HighlightStyles.NONVISITED);
			updateImageText(nonVisited, nonVisitedCoordinates);

			lang.nextStep();
			sc.unhighlight("nonvisadd");
			sc.highlight("visadd");
			visitedCoordinates.add(c);
			im.highlightCoordinate(c, ImageMatrix.HighlightStyles.VISITED);
			lang.nextStep();
			sc.unhighlight("visadd");
		}
		lang.nextStep();
		sc.unhighlight("forcoordinates");

		sc.highlight("while");
		while (!nonVisitedCoordinates.isEmpty()) {
			sc.highlight("v");

			Coordinate v = nonVisitedCoordinates.get(0);

			im.deHighlightCoordinate(ImageMatrix.HighlightStyles.NEIGHBOR);
			im.highlightCoordinate(v, ImageMatrix.HighlightStyles.CURRENT);

			lang.nextStep("current: " + v.toString());
			sc.unhighlight("v");
			sc.highlight("addToROI");

			im.addToROI(v);

			lang.nextStep();
			sc.unhighlight("addToROI");
			sc.highlight("nonVisRemove");

			nonVisitedCoordinates.remove(v);
			updateImageText(nonVisited, nonVisitedCoordinates);
			im.deHighlightCoordinate(v, ImageMatrix.HighlightStyles.NONVISITED);

			lang.nextStep();
			sc.unhighlight("nonVisRemove");
			sc.highlight("neighbors");

			List<Coordinate> neighbors = v.neighbors(image.get(0).length,
					image.length());
			im.highlightNeighbors(neighbors);

			lang.nextStep();
			sc.unhighlight("neighbors");
			sc.highlight("foreachNeighbors");

			for (Coordinate neighbor : neighbors) {
				im.highlightCoordinate(neighbor,
						ImageMatrix.HighlightStyles.CURRENT_NEIGHBOR);
				lang.nextStep();
				sc.highlight("notVisitedNeighbor");
				if (!visitedCoordinates.contains(neighbor)) {
					lang.nextStep();
					sc.highlight("neighborVisAdd");

					visitedCoordinates.add(neighbor);
					im.highlightCoordinate(neighbor,
							ImageMatrix.HighlightStyles.VISITED);

					lang.nextStep();
					sc.unhighlight("neighborVisAdd");
					sc.highlight("neighborNonVisRemove");

					nonVisitedCoordinates.remove(neighbor);
					updateImageText(nonVisited, nonVisitedCoordinates);
					im.deHighlightCoordinate(neighbor,
							ImageMatrix.HighlightStyles.NONVISITED);

					lang.nextStep();
					sc.unhighlight("neighborNonVisRemove");

					sc.highlight("check");
					if (compare(image.get(neighbor.getY(), neighbor.getX()))) {
						sc.highlight("neighborNonVisAdd");
						nonVisitedCoordinates.add(neighbor);
						updateImageText(nonVisited, nonVisitedCoordinates);
						im.highlightCoordinate(neighbor,
								ImageMatrix.HighlightStyles.NONVISITED);

						lang.nextStep();
						sc.unhighlight("neighborNonVisAdd");
					} else
						lang.nextStep();
					sc.unhighlight("check");
				} else
					lang.nextStep();
				sc.unhighlight("notVisitedNeighbor");
			}
			im.deHighlightCoordinate(ImageMatrix.HighlightStyles.CURRENT_NEIGHBOR);

			sc.unhighlight("foreachNeighbors");

			/*
			 * for (Coordinate c: nonVisitedCoordinates)
			 * System.out.print(c.toString() + " "); System.out.println();
			 */
		}
		im.deHighlightCoordinate(ImageMatrix.HighlightStyles.CURRENT);
		im.deHighlightCoordinate(ImageMatrix.HighlightStyles.NEIGHBOR);
		sc.unhighlight("while");

		//
		lang.nextStep();

		clearAll();

		im.clearAll();
		showOutroSlide();
		/*
		 * for (Coordinate c: im.getROI()) System.out.print(c.toString() + " ");
		 * System.out.println();
		 */
	}

	/*
	 * private boolean compare(Integer value) { return Math.abs(comparisonValue
	 * - value) <= comparisonTolerance; }
	 */
	private void printCompareSourceCode() {
		compareSc = lang.newSourceCode(new Offset(120, -10, imageCounterText,
				AnimalScript.DIRECTION_NE), "compareSource", null,
				sourceCodeProperties);

		compareSc.addCodeLine("private boolean compare(Integer value) {", null,
				0, null);
		compareSc.addCodeLine("return Math.abs(" + comparisonValue
				+ " - value) <= " + comparisonTolerance + ";", null, 1, null);
		compareSc.addCodeLine("}", null, 0, null);
	}

	private void updateImageText(Text text, CountingList<Coordinate> cl) {
		text.setText(cl.toString(), null, null);
	}

	private void clearAll() {
		sc.hide();
		compareSc.hide();
		nonVisitedCounterText.hide();
		visitedCounterText.hide();
		imageCounterText.hide();
		tvvNonVisited.hide();
		tvvVisited.hide();
		tvvImage.hide();
		nonVisited.hide();
	}

	private void showOutroSlide() {
		TrueFalseQuestionModel question1 = new TrueFalseQuestionModel(
				"question1", false, 1);
		question1
				.setPrompt("Does the algorithm always cover all pixels in the source image?");
		question1
				.setFeedbackForAnswer(
						false,
						"Correct! The algorithm depends on the chosen seed points. It is possible that some points are never reached.");
		question1
				.setFeedbackForAnswer(
						true,
						"Wrong! The algorithm depends on the chosen seed points. It is possible that some points are never reached.");
		lang.addTFQuestion(question1);

		SourceCode outro = lang
				.newSourceCode(new Offset(0, 50, title,
						AnimalScript.DIRECTION_SW), "outro", null,
						sourceCodeProperties);
		outro.addCodeLine(
				"This animation shows a very basic region growing algorithm.",
				null, 0, null);
		outro.addCodeLine(
				"This implementation restrains itself to one ROI,",
				null, 0, null);
		outro.addCodeLine(
				"which is not always given. It is possible to extend the",
				null, 0, null);
		outro.addCodeLine(
				"algorithm to incorporate these more complex features.",
				null, 0, null);
		outro.addCodeLine(
				"This would make it more difficult",
				null, 0, null);
		outro.addCodeLine(
				"to follow along the process.",
				null, 0, null);
		lang.nextStep("Outro");
	}

	private void showIntroSlides() {
		// TextProperties props = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				((Font) titleProperties
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.deriveFont(42.0f));
		title = lang.newText(new Coordinates(50, 50),
				"Region Growing Algorithm", "title", null, titleProperties);
		SourceCode intro = lang
				.newSourceCode(new Offset(0, 50, title,
						AnimalScript.DIRECTION_SW), "intro", null,
						sourceCodeProperties);
		intro.addCodeLine(
				"Region growing is a simple image segmentation method. The idea behind the algorithm is to",
				null, 0, null);
		intro.addCodeLine("find segmentations of a grayscale image. ", null, 0,
				null);
		intro.addCodeLine(
				"In our case, a segmentation into two regions is carried out - a region of interest (ROI),",
				null, 0, null);
		intro.addCodeLine(
				"and an (implicit) region of non-interest. At first, a number of seed points are selected.",
				null, 0, null);
		intro.addCodeLine(
				"These seed points are always added to the ROI. Then, each neighbour of a seed point is",
				null, 0, null);
		intro.addCodeLine(
				"checked against an acceptance criterium - and if the criterium matches, is added to the",
				null, 0, null);
		intro.addCodeLine(
				"ROI. Neighbours of points inside the ROI continue to be checked and added until no more",
				null, 0, null);
		intro.addCodeLine("points can be checked, and the ROI has been found.",
				null, 0, null);
		lang.nextStep();
		intro.hide();
	}

	private void printSourceCode() {
		sc = lang.newSourceCode(new Offset(0, 20, title,
				AnimalScript.DIRECTION_SW), "source", null,
				sourceCodeProperties);
		sc.addCodeLine("for (Coordinate c: seeds) {", "forcoordinates", 0, null);
		sc.addCodeLine("nonVisitedCoordinates.add(c);", "nonvisadd", 1, null);
		sc.addCodeLine("visitedCoordinates.add(c);", "visadd", 1, null);
		sc.addCodeLine("}", null, 0, null);

		sc.addCodeLine("while (!nonVisitedCoordinates.isEmpty()) {", "while",
				0, null);

		sc.addCodeLine("v = nonVisitedCoordinates.get(0);", "v", 1, null);
		sc.addCodeLine("roi.add(v);", "addToROI", 1, null);
		sc.addCodeLine("nonVisitedCoordinates.remove(v);", "nonVisRemove", 1,
				null);
		sc.addCodeLine(
				"neighbors = v.neighbors(image[0].length, image.length);",
				"neighbors", 1, null);

		sc.addCodeLine("for (Coordinate neighbor: neighbors) {",
				"foreachNeighbors", 1, null);
		sc.addCodeLine("if (!visitedCoordinates.contains(neighbor)) {",
				"notVisitedNeighbor", 2, null);
		sc.addCodeLine("visitedCoordinates.add(neighbor);", "neighborVisAdd",
				3, null);
		sc.addCodeLine("nonVisitedCoordinates.remove(neighbor);",
				"neighborNonVisRemove", 3, null);

		sc.addCodeLine("if (compare(image[neighbor.getY()][neighbor.getX()]))",
				"check", 3, null);
		sc.addCodeLine("nonVisitedCoordinates.add(neighbor);",
				"neighborNonVisAdd", 4, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
	}

	private <T> TwoValueView initCounter(Node position, ArrayPrimitive list) {
		TwoValueCounter counter = lang.newCounter(list);

		CounterProperties cp = new CounterProperties(); // Zaehler-Properties
														// anlegen
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit
																	// Blau
		TwoValueView tvv = lang.newCounterView(counter, position, cp, true,
				false);
		return tvv;
	}

	public void setComparison(int value, int tolerance) {
		comparisonValue = value;
		comparisonTolerance = tolerance;
	}

	private boolean compare(Integer value) {
		return Math.abs(comparisonValue - value) <= comparisonTolerance;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		image = (int[][]) primitives.get("image");
		comparisonValue = (Integer) primitives.get("comparisonValue");

		seeds = (int[][]) primitives.get("seeds");
		comparisonTolerance = (Integer) primitives.get("comparisonTolerance");

		if (image.length > 20)
			throw new IllegalArgumentException(
					"This algorithm only allows up to 20 rows and columns.");
		if (image.length < 1)
			throw new IllegalArgumentException(
					"This algorithm needs at least one row and column.");
		if (image[0].length > 20)
			throw new IllegalArgumentException(
					"This algorithm only allows up to 20 rows and columns.");
		if (image[0].length < 1)
			throw new IllegalArgumentException(
					"This algorithm needs at least one row and column.");

		if (seeds.length != 2)
			throw new IllegalArgumentException(
					"The seeds are written in a column-based matrix. So this matrix has to have exactly 2 rows.");
		if (seeds[0].length < 1)
			throw new IllegalArgumentException(
					"You need to have at least 1 seed");
		for (int i = 0; i < seeds[0].length; i++) {
			int x = seeds[0][i];
			int y = seeds[1][i];
			if (x < 0 || x >= image[0].length)
				throw new IllegalArgumentException("The x value of the seed has to be in the image");
			if (y < 0 || y >= image.length)
				throw new IllegalArgumentException("The y value of the seed has to be in the image");
			
		}
		
		Integer[][] vals = new Integer[image.length][];
		for (int i = 0; i < image.length; i++) {
			vals[i] = new Integer[image[i].length];
			for (int j = 0; j < image[i].length; j++)
				vals[i][j] = image[i][j];
		}
		
		if (comparisonValue < 0 || comparisonValue >= ImageMatrix.getMax(vals))
			throw new IllegalArgumentException("The comparison value has to be positive and cannot be bigger are same as the maximum value");
		if (comparisonValue < 0 || comparisonValue >= ImageMatrix.getMax(vals))
			throw new IllegalArgumentException("The tolarance has to be positive and cannot be bigger are same as the maximum value");

		return true;
	}

}