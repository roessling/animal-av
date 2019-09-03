package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringMatrixGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class GeohashEncryption implements Generator {
	private Language lang;
	private TextProperties textProps;
	private TextProperties highlightedTextProps;
	private SourceCodeProperties sourceCodeProps;
	private MatrixProperties matrixProps;
	private RectProperties rectProps;
	private boolean showQuestions;

	private int precision;
	private double longitude;
	private double latitude;

	private List<Primitive> all = new LinkedList<Primitive>();

	private AnimalTextGenerator textGenerator;
	private AnimalRectGenerator rectGenerator;
	private Text[] longitudeBitText;
	private Text[] latitudeBitText;
	private int[] bitLatitude;
	private int[] bitLongitude;
	private int[] Decimal;
	private String finalCode = "";
	private Rect head;

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		init();

		matrixProps = (MatrixProperties) props
				.getPropertiesByName("matrixProps");
		highlightedTextProps = (TextProperties) props
				.getPropertiesByName("highlightedTextProps");
		precision = (Integer) primitives.get("precision");
		sourceCodeProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProps");
		longitude = (Double) primitives.get("longitude");
		rectProps = (RectProperties) props.getPropertiesByName("rectProps");
		latitude = (Double) primitives.get("latitude");
		textProps = (TextProperties) props.getPropertiesByName("textProps");
		showQuestions = (Boolean) primitives.get("showQuestions");

		// check parameters for validity
		if (precision < 1)
			precision = 1;
		if (latitude < -90)
			latitude = -90;
		if (latitude > 90)
			latitude = 90;
		if (longitude < -180)
			longitude = -180;
		if (longitude > 180)
			longitude = 180;

		initializeAlgorithm();

		lang.finalizeGeneration();

		// TODO: Remove replaceAlls once grids are fixed to display correctly
		// Negative side effect of current workaround: All cellWidths of a table
		// are equal, therefore table cells may be unnecessarily large
		return lang
				.toString()
				.replaceAll("refresh", "")
				.replaceAll(
						"columns 5 style plain",
						"columns 5 style plain cellWidth "
								+ Math.max(100, precision * 50)
								+ " fixedCellSize")
				.replaceAll(
						"columns 5 style matrix",
						"columns 5 style matrix cellWidth "
								+ Math.max(100, precision * 50)
								+ " fixedCellSize")
				.replaceAll(
						"columns 5 style table",
						"columns 5 style table cellWidth "
								+ Math.max(100, precision * 50)
								+ " fixedCellSize")
				.replaceAll("columns 33 style plain",
						"columns 33 style plain cellWidth 80 fixedCellSize")
				.replaceAll("columns 33 style matrix",
						"columns 33 style matrix cellWidth 80 fixedCellSize")
				.replaceAll("columns 33 style table",
						"columns 33 style table cellWidth 80 fixedCellSize");
	}

	public void init() {
		lang = new AnimalScript("Geohash", "Thomas Klir,Philipp Mueller", 800,
				600);

		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		textGenerator = new AnimalTextGenerator(lang);
		rectGenerator = new AnimalRectGenerator(lang);
	}

	public void initializeAlgorithm() {
		setHeader();
		lang.nextStep();

		showIntroductionText();
		lang.nextStep("Introduction");
		hideAll();

		if (showQuestions) {
			MultipleChoiceQuestionModel inputData = new MultipleChoiceQuestionModel(
					"inputData");
			inputData
					.setPrompt("Which type of data can be encrypted using Geohash?");
			inputData
					.addAnswer("Data Files", 0,
							"Wrong, Geohash is only applicable to positions in form of coordinate pairs.");
			inputData
					.addAnswer("Telephone Numbers", 0,
							"Wrong, Geohash is only applicable to positions in form of coordinate pairs.");
			inputData
					.addAnswer(
							"Coordinate Pairs",
							1,
							"Correct, Geohash can be used to encrypt coordinate pairs which depict positions on earth.");
			lang.addMCQuestion(inputData);
		}

		bitLatitude = encryptCoordinate(latitude, precision, 90, "latitude",
				"Latitude Bitcode Generation", false);
		lang.nextStep();
		hideAll();

		bitLongitude = encryptCoordinate(longitude, precision, 180,
				"longitude", "Longitude Bitcode Generation", true);
		lang.nextStep();
		hideAll();

		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		all.add(new Text(textGenerator, new Coordinates(20, 80), "Merging", "",
				null, textProps));
		lang.nextStep("Merge Bitcodes");

		displayMergingHeader(bitLongitude, bitLatitude);
		lang.nextStep();

		displayUpperSolution();

		generateBase32Matrix("Base32 Conversion");

		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		all.add(new Text(textGenerator, new Coordinates(20, 380),
				"The Geohash of latitude = " + latitude + " and longitude = "
						+ longitude + " is '" + finalCode + "'.", "resultText",
				null, textProps));

		if (showQuestions) {
			MultipleChoiceQuestionModel comp = new MultipleChoiceQuestionModel(
					"comp");
			comp.setPrompt("Of which complexity is the Geohash encryption algorithm?");
			comp.addAnswer("n", 1,
					"Correct, the Geohash algorithm is of linear complexity.");
			comp.addAnswer("n log n", 0,
					"Wrong, the Geohash algorithm is of linear complexity.");
			comp.addAnswer("n * n", 0,
					"Wrong, the Geohash algorithm is of linear complexity.");
			lang.addMCQuestion(comp);
		}

		lang.nextStep();
		hideAll();

		showFinalText();
		lang.nextStep("Conclusion");
		hideAll();
	}

	private void setHeader() {
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 24));
		Text header = new Text(textGenerator, new Coordinates(20, 30),
				"Geohash - Encryption", "header", null, textProps);

		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		head = new Rect(rectGenerator, new Offset(-5, -5, header, "NW"), new Offset(5,
				5, header, "SE"), "hRect", null, rectProps);
	}

	private void showIntroductionText() {
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		all.add(new Text(textGenerator, new Coordinates(20, 80),
				"INTRODUCTION", "text0", null, textProps));

		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.PLAIN, 14));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 120),
				"Geohash is a latitude/longitude geocode system invented by Gustavo Niemeyer. It is a hierarchical spatial data structure which subdivides",
				"", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 140),
				"space into buckets of grid shape.", "", null, textProps));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 190),
				"Geohashes are short codes which uniquely identify positions on the Earth, allowing for convenient referencing in emails, websites",
				"", null, textProps));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 210),
				"or databases. They offer properties like arbitrary precision and the possibility of gradually removing characters from the end of the code to",
				"", null, textProps));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 230),
				"reduce its size (and gradually lose precision in the process).",
				"", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 280), "Steps:", "",
				null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 310),
				"Generation of Bitcode", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 330),
				"    Longitude", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 350),
				"    Latitude", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 380),
				"Bitcode Merging", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 410),
				"Conversion of Bitcode to Geohash", "", null, textProps));
	}

	private int[] encryptCoordinate(double value, int precision, int minmax,
			String name, String next, boolean vertical) {
		SourceCode v = lang.newSourceCode(new Coordinates(20, 60), "variables"
				+ name, null, sourceCodeProps);
		all.add(v);

		v.addCodeLine("precision = " + precision, null, 0, null); // 0
		v.addCodeLine(name + " = " + value, null, 0, null); // 1

		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.PLAIN, 18));
		Text sct = new Text(textGenerator, new Offset(0, 10, v, "SW"),
				"source code " + name, "sourceCodeText" + name, null, textProps);
		all.add(sct);

		SourceCode sc = lang.newSourceCode(new Offset(0, 0, sct, "SW"),
				"sourceCode" + name, null, sourceCodeProps);
		all.add(sc);

		sc.addCodeLine("int i = 0;", null, 0, null); // 0
		sc.addCodeLine("min[i] = -" + minmax + ";", null, 0, null); // 1
		sc.addCodeLine("max[i] = " + minmax + ";", null, 0, null); // 2
		sc.addCodeLine("while (i < (precision*5)) {", null, 0, null); // 3
		sc.addCodeLine("avg[i] = (min[i] + max[i]) / 2;", null, 1, null); // 4
		sc.addCodeLine("if (value<avg[i]) {", null, 1, null); // 5
		sc.addCodeLine("bit[i] = 0;", null, 2, null); // 6
		sc.addCodeLine("min[i+1] = min[i];", null, 2, null); // 7
		sc.addCodeLine("max[i+1] = avg[i];", null, 2, null); // 8
		sc.addCodeLine("} else {", null, 1, null); // 9
		sc.addCodeLine("bit[i] = 1;", null, 2, null); // 10
		sc.addCodeLine("min[i+1] = avg[i];", null, 2, null); // 11
		sc.addCodeLine("max[i+1] = max[i];", null, 2, null); // 12
		sc.addCodeLine("}", null, 1, null); // 13
		sc.addCodeLine("i++;", null, 1, null); // 14
		sc.addCodeLine("}", null, 0, null); // 15
		sc.addCodeLine("return bit;", null, 0, null); // 16

		newLineAt(-90, false, Color.BLACK);
		newLineAt(90, false, Color.BLACK);
		newLineAt(-180, true, Color.BLACK);
		newLineAt(180, true, Color.BLACK);
		Circle circle = newCircleAt(longitude, latitude);

		String[][] matrixContent = new String[precision * 5 + 2][5];
		matrixContent[0][0] = "i";
		matrixContent[0][1] = "min";
		matrixContent[0][2] = "avg";
		matrixContent[0][3] = "max";
		matrixContent[0][4] = "bit";
		for (int row = 1; row < matrixContent.length; row++) {
			matrixContent[row][0] = Integer.toString(row - 1);
			for (int column = 1; column < matrixContent[0].length; column++) {
				matrixContent[row][column] = "";
			}
		}

		AnimalStringMatrixGenerator matrixGenerator = new AnimalStringMatrixGenerator(
				(AnimalScript) lang);
		StringMatrix sm = new StringMatrix(matrixGenerator, new Coordinates(
				260, 260), matrixContent, "matrix", null, matrixProps);
		all.add(sm);

		double[] min = new double[precision * 5 + 1];
		double[] max = new double[precision * 5 + 1];
		double[] avg = new double[precision * 5 + 1];
		int[] bit = new int[precision * 5];

		Polyline[] minLine = new Polyline[precision * 5 + 1];
		Polyline[] maxLine = new Polyline[precision * 5 + 1];
		Polyline[] avgLine = new Polyline[precision * 5 + 1];

		int i = 0; // 0
		sc.highlight(0);
		sm.highlightCell(1, 0, null, null);
		lang.nextStep(next);
		sc.unhighlight(0);

		min[i] = -minmax; // 1
		sc.highlight(1);
		sm.put(1, 1, Double.toString(min[i]), null, null);
		sm.highlightCell(1, 1, null, null);
		minLine[i] = newLineAt(-minmax, vertical, Color.BLUE);
		minLine[i].changeColor("Color", Color.RED, null, null);
		lang.nextStep();
		sc.unhighlight(1);
		sm.unhighlightCell(1, 1, null, null);
		minLine[i].changeColor("Color", Color.BLUE, null, null);

		max[i] = minmax; // 2
		sc.highlight(2);
		sm.put(1, 3, Double.toString(max[i]), null, null);
		sm.highlightCell(1, 3, null, null);
		maxLine[i] = newLineAt(minmax, vertical, Color.BLUE);
		maxLine[i].changeColor("Color", Color.RED, null, null);
		lang.nextStep();
		sc.unhighlight(2);
		sm.unhighlightCell(1, 3, null, null);
		maxLine[i].changeColor("Color", Color.BLUE, null, null);

		while (i < (precision * 5)) { // 3
			sc.highlight(3);
			v.highlight(0);
			lang.nextStep();
			sc.unhighlight(3);
			v.unhighlight(0);

			avg[i] = (min[i] + max[i]) / 2; // 4
			sc.highlight(4);
			sm.put(i + 1, 2, Double.toString(avg[i]), null, null);
			sm.highlightCell(i + 1, 1, null, null);
			sm.highlightCell(i + 1, 2, null, null);
			sm.highlightCell(i + 1, 3, null, null);
			if (i > 0)
				avgLine[i - 1].hide();
			avgLine[i] = newLineAt(avg[i], vertical, Color.BLUE);
			avgLine[i].changeColor("Color", Color.RED, null, null);
			lang.nextStep();
			sc.unhighlight(4);
			sm.unhighlightCell(i + 1, 1, null, null);
			sm.unhighlightCell(i + 1, 2, null, null);
			sm.unhighlightCell(i + 1, 3, null, null);

			if (value < avg[i]) { // 5
				sc.highlight(5);
				v.highlight(1);
				sm.highlightCell(i + 1, 2, null, null);
				circle.changeColor("Color", Color.RED, null, null);

				if (showQuestions) {
					if (i == 3) {
						FillInBlanksQuestionModel bitQuestion = new FillInBlanksQuestionModel(
								"bitQuestion1" + name);
						bitQuestion.setPrompt("The bit at i=3 is?");
						bitQuestion
								.addAnswer(
										"0",
										1,
										"Correct, as the "
												+ name
												+ " is lower than the average at i=3, the bit at i=3 is set to 0.");
						bitQuestion
								.addAnswer(
										"1",
										0,
										"Wrong, as the "
												+ name
												+ " is lower than the average at i=3, the bit at i=3 is set to 0.");
						lang.addFIBQuestion(bitQuestion);
					}
				}

				lang.nextStep();
				circle.changeColor("Color", Color.BLACK, null, null);
				sc.unhighlight(5);
				v.unhighlight(1);
				sm.unhighlightCell(i + 1, 2, null, null);
				avgLine[i].changeColor("Color", Color.BLUE, null, null);

				bit[i] = 0; // 6
				sc.highlight(6);
				sm.put(i + 1, 4, "0", null, null);
				sm.highlightCell(i + 1, 4, null, null);

				if (showQuestions) {
					if (i == 2) {
						TrueFalseQuestionModel minOldAvg = new TrueFalseQuestionModel(
								"minOldAvg1" + name);
						minOldAvg
								.setPrompt("Is the new minimum value at i=3 equal to the former average value at i=2?");
						minOldAvg.setPointsPossible(1);
						minOldAvg.setCorrectAnswer(false);
						lang.addTFQuestion(minOldAvg);
					}
				}

				lang.nextStep();
				sc.unhighlight(6);
				sm.unhighlightCell(i + 1, 4, null, null);

				min[i + 1] = min[i]; // 7
				sc.highlight(7);
				sm.put(i + 2, 1, Double.toString(min[i + 1]), null, null);
				sm.highlightCell(i + 1, 1, null, null);
				sm.highlightCell(i + 2, 1, null, null);
				minLine[i].hide();
				minLine[i + 1] = newLineAt(min[i + 1], vertical, Color.BLUE);
				minLine[i + 1].changeColor("Color", Color.RED, null, null);
				lang.nextStep();
				minLine[i + 1].changeColor("Color", Color.BLUE, null, null);
				sc.unhighlight(7);
				sm.unhighlightCell(i + 1, 1, null, null);
				sm.unhighlightCell(i + 2, 1, null, null);

				max[i + 1] = avg[i]; // 8
				sc.highlight(8);
				sm.put(i + 2, 3, Double.toString(max[i + 1]), null, null);
				sm.highlightCell(i + 1, 2, null, null);
				sm.highlightCell(i + 2, 3, null, null);
				maxLine[i].hide();
				maxLine[i + 1] = newLineAt(max[i + 1], vertical, Color.BLUE);
				maxLine[i + 1].changeColor("Color", Color.RED, null, null);
				lang.nextStep();
				maxLine[i + 1].changeColor("Color", Color.BLUE, null, null);
				sc.unhighlight(8);
				sm.unhighlightCell(i + 1, 2, null, null);
				sm.unhighlightCell(i + 2, 3, null, null);

			} else { // 9
				sc.highlight(5);
				v.highlight(1);
				sm.highlightCell(i + 1, 2, null, null);
				circle.changeColor("Color", Color.RED, null, null);

				if (showQuestions) {
					if (i == 3) {
						FillInBlanksQuestionModel bitQuestion = new FillInBlanksQuestionModel(
								"bitQuestion1" + name);
						bitQuestion.setPrompt("The bit at i=3 is?");
						bitQuestion
								.addAnswer(
										"1",
										1,
										"Correct, as the "
												+ name
												+ " is higher than or equal to the average at i=3, the bit at i=3 is set to 1.");
						bitQuestion
								.addAnswer(
										"0",
										0,
										"Wrong, as the "
												+ name
												+ " is higher than or equal to the average at i=3, the bit at i=3 is set to 1.");
						lang.addFIBQuestion(bitQuestion);
					}
				}

				lang.nextStep();
				circle.changeColor("Color", Color.BLACK, null, null);
				sc.unhighlight(5);
				v.unhighlight(1);
				sm.unhighlightCell(i + 1, 2, null, null);
				avgLine[i].changeColor("Color", Color.BLUE, null, null);

				bit[i] = 1; // 10
				sc.highlight(10);
				sm.put(i + 1, 4, "1", null, null);
				sm.highlightCell(i + 1, 4, null, null);

				if (showQuestions) {
					if (i == 2) {
						TrueFalseQuestionModel minOldAvg = new TrueFalseQuestionModel(
								"minOldAvg2" + name);
						minOldAvg
								.setPrompt("Is the new minimum value at i=3 equal to the former average value at i=2?");
						minOldAvg.setPointsPossible(1);
						minOldAvg.setCorrectAnswer(true);
						lang.addTFQuestion(minOldAvg);
					}
				}

				lang.nextStep();
				sc.unhighlight(10);
				sm.unhighlightCell(i + 1, 4, null, null);

				min[i + 1] = avg[i]; // 11
				sc.highlight(11);
				sm.put(i + 2, 1, Double.toString(min[i + 1]), null, null);
				sm.highlightCell(i + 1, 2, null, null);
				sm.highlightCell(i + 2, 1, null, null);
				minLine[i].hide();
				minLine[i + 1] = newLineAt(min[i + 1], vertical, Color.BLUE);
				minLine[i + 1].changeColor("Color", Color.RED, null, null);
				lang.nextStep();
				minLine[i + 1].changeColor("Color", Color.BLUE, null, null);
				sc.unhighlight(11);
				sm.unhighlightCell(i + 1, 2, null, null);
				sm.unhighlightCell(i + 2, 1, null, null);

				max[i + 1] = max[i]; // 12
				sc.highlight(12);
				sm.put(i + 2, 3, Double.toString(max[i + 1]), null, null);
				sm.highlightCell(i + 1, 3, null, null);
				sm.highlightCell(i + 2, 3, null, null);
				maxLine[i].hide();
				maxLine[i + 1] = newLineAt(max[i + 1], vertical, Color.BLUE);
				maxLine[i + 1].changeColor("Color", Color.RED, null, null);
				lang.nextStep();
				maxLine[i + 1].changeColor("Color", Color.BLUE, null, null);
				sc.unhighlight(12);
				sm.unhighlightCell(i + 1, 3, null, null);
				sm.unhighlightCell(i + 2, 3, null, null);

			} // 13

			i++; // 14
			sc.highlight(14);
			sm.unhighlightCell(i, 0, null, null);
			sm.highlightCell(i + 1, 0, null, null);
			lang.nextStep();
			sc.unhighlight(14);

		} // 15
		sc.highlight(3);
		v.highlight(0);
		lang.nextStep();
		sc.unhighlight(3);
		sm.unhighlightCell(i + 1, 0, null, null);
		v.unhighlight(0);

		sc.highlight(16);
		sm.highlightCellRowRange(0, matrixContent.length - 1, 4, null, null);
		String bitstring = "";
		for (int b : bit) {
			bitstring += b;
		}

		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		Text st = lang.newText(new Offset(50, 10, head, "NE"), name + " solution: "
				+ bitstring, "solutionText" + name, null, textProps);
		all.add(st);
		return bit; // 16
	}

	// create new line from x1/y1 to x2/y2
	private Polyline newLineFromTo(int x1, int y1, int x2, int y2) {
		Node[] vertices = new Node[2];
		vertices[0] = new Coordinates(260 + 180 + x1, 70 + 90 - y1);
		vertices[1] = new Coordinates(260 + 180 + x2, 70 + 90 - y2);
		Polyline newLine = lang.newPolyline(vertices, "", null);
		all.add(newLine);
		return newLine;
	}

	// create horizontal/vertical line
	private Polyline newLineAt(double c, boolean vertical, Color Color) {
		if (vertical) {
			return newLineFromTo((int) (c + 0.5), -90, (int) (c + 0.5), 90);
		} else {
			return newLineFromTo(-180, (int) (c + 0.5), 180, (int) (c + 0.5));
		}
	}

	// create circle
	private Circle newCircleAt(double longitude, double latitude) {
		Circle newCircle = lang.newCircle(new Coordinates(
				(int) (260 + 180 + longitude + 0.5),
				(int) (70 + 90 - latitude + 0.5)), 2, "position" + longitude
				+ latitude, null);
		all.add(newCircle);
		return newCircle;
	}

	private void displayMergingHeader(int[] bitsLongitude, int[] bitsLatitude) {
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		all.add(new Text(textGenerator, new Coordinates(20, 120),
				"longitude solution: ", "longitudeLabel", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 140),
				"latitude solution: ", "latitudeLabel", null, textProps));

		longitudeBitText = new Text[bitsLongitude.length];

		for (int i = 0; i < bitsLongitude.length; i++) {
			longitudeBitText[i] = new Text(textGenerator, new Coordinates(
					230 + i * 15, 120), Integer.toString(bitsLongitude[i]),
					("longitudeBitText" + (i + 1)), null, textProps);
			all.add(longitudeBitText[i]);
		}

		latitudeBitText = new Text[bitsLatitude.length];

		for (int i = 0; i < bitsLatitude.length; i++) {
			latitudeBitText[i] = new Text(textGenerator, new Coordinates(
					230 + i * 15, 140), Integer.toString(bitsLatitude[i]),
					("latitudeBitText" + (i + 1)), null, textProps);
			all.add(latitudeBitText[i]);
		}

	}

	private void displayUpperSolution() {
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		all.add(new Text(textGenerator, new Coordinates(20, 180), "Solution: ",
				"", null, textProps));

		Text textSolution[] = new Text[longitudeBitText.length
				+ latitudeBitText.length];

		Boolean switchLine = true;
		highlightedTextProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) highlightedTextProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		for (int i = 0; i < textSolution.length; i++) {
			if (switchLine) {
				longitudeBitText[i / 2].changeColor(null,
						(Color) highlightedTextProps
								.get(AnimationPropertiesKeys.COLOR_PROPERTY),
						null, null);
				textSolution[i] = new Text(textGenerator, new Coordinates(
						230 + i * 15, 180), longitudeBitText[i / 2].getText()
						.toString(), ("textSolution" + i), null,
						highlightedTextProps);

				lang.nextStep();

				longitudeBitText[i / 2].changeColor(null, (Color) textProps
						.get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
						null);
				textSolution[i].changeColor(null, (Color) textProps
						.get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
						null);

				switchLine = false;
			} else {
				latitudeBitText[i / 2].changeColor(null,
						(Color) highlightedTextProps
								.get(AnimationPropertiesKeys.COLOR_PROPERTY),
						null, null);
				textSolution[i] = new Text(textGenerator, new Coordinates(
						230 + i * 15, 180), latitudeBitText[i / 2].getText()
						.toString(), ("textSolution" + i), null,
						highlightedTextProps);

				lang.nextStep();

				latitudeBitText[i / 2].changeColor(null, (Color) textProps
						.get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
						null);
				textSolution[i].changeColor(null, (Color) textProps
						.get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
						null);

				switchLine = true;
			}
		}
		lang.nextStep();

		for (Text Texts : textSolution)
			Texts.hide();

		// The variable 'solution_new' contains the solution with '|' after
		// every fifth bit
		@SuppressWarnings("unused")
    String solution_new = "";

		String[] bit_Text = new String[textSolution.length / 5];

		for (int x = 0; x < textSolution.length; x++) {
			solution_new += textSolution[x].getText().toString();
			if (x % 5 == 0)
				bit_Text[x / 5] = "";
			bit_Text[x / 5] += textSolution[x].getText().toString();
			if (x % 5 == 4 && x != textSolution.length - 1) {
				solution_new += " | ";
			}
		}

		int Coordinates = 0;
		for (int i = 0; i < bit_Text.length; i++) {
			Coordinates = 230 + i * 87;
			all.add(new Text(textGenerator, new Coordinates(Coordinates, 180),
					bit_Text[i], "", null, highlightedTextProps));
			if (i != bit_Text.length - 1) {
				all.add(new Text(textGenerator, new Coordinates(
						Coordinates + 52 + 20, 180), "|", "", null,
						highlightedTextProps));
			}
		}
		
		all.add(new Text(textGenerator, new Coordinates(
				(230 + 20 + (bit_Text.length - 1) * 87 + 59), 180), "(binary)",
				"binary", null, highlightedTextProps));

		lang.nextStep();

		Decimal = new int[bit_Text.length];

		for (int j = 0; j < bit_Text.length; j++) {
			Decimal[j] = Integer.parseInt(bit_Text[j], 2);

			Coordinates = 230 + 20 + j * 87;

			if (Decimal[j] < 10)
				all.add(new Text(textGenerator, new Coordinates(Coordinates,
						200), "0" + Decimal[j], "", null, highlightedTextProps));
			else
				all.add(new Text(textGenerator, new Coordinates(Coordinates,
						200), "" + Decimal[j], "", null, highlightedTextProps));

			if (j != bit_Text.length - 1) {
				all.add(new Text(textGenerator, new Coordinates(
						Coordinates + 52, 200), "|", "", null,
						highlightedTextProps));
			}
		}

		lang.nextStep();

		all.add(new Text(textGenerator, new Coordinates(Coordinates + 59, 200),
				"(decimal)", "", null, highlightedTextProps));
		lang.nextStep();
		all.add(new Text(textGenerator, new Coordinates(Coordinates + 59, 220),
				"(base 32)", "", null, highlightedTextProps));
		lang.nextStep();

	}

	private void generateBase32Matrix(String next) {
		AnimalStringMatrixGenerator matrixGenerator = new AnimalStringMatrixGenerator(
				(AnimalScript) lang);

		String[][] data = new String[2][33];
		StringMatrix base = new StringMatrix(matrixGenerator, new Coordinates(
				20, 270), data, "base", null, matrixProps);
		all.add(base);

		base.put(0, 0, "Decimal", null, null);
		base.put(1, 0, "Base 32", null, null);

		for (int i = 0; i < 32; i++)
			base.put(0, i + 1, "" + i, null, null);

		int u = 10;

		for (int i = 0; i < 10; i++)
			base.put(1, i + 1, "" + i, null, null);

		for (char anfang = 'a'; anfang <= 'z'; anfang++)
			if (anfang != 'a' && anfang != 'i' && anfang != 'l'
					&& anfang != 'o') {
				base.put(1, u + 1, "" + anfang, null, null);
				u++;
			}
		lang.nextStep(next);

		// Match the base32 value to a searchable Map
		Map<Integer, String> nMap = new HashMap<Integer, String>();
		for (int i = 0; i < 32; i++)
			nMap.put(i + 1, base.getElement(1, i + 1));

		String newBase;
		finalCode = "";

		int Coordinates;
		for (int i = 0; i < Decimal.length; i++) {
			base.highlightCell(0, (Decimal[i] + 1), null, null);
			base.highlightCell(1, (Decimal[i] + 1), null, null);
			lang.nextStep();

			newBase = (String) nMap.get(Decimal[i] + 1);
			finalCode += newBase;
			Coordinates = 230 + 26 + i * 87;
			all.add(new Text(textGenerator, new Coordinates(Coordinates, 220),
					newBase, "", null, highlightedTextProps));

			if (i < Decimal.length - 1)
				all.add(new Text(textGenerator, new Coordinates(
						Coordinates + 46, 220), "|", "", null,
						highlightedTextProps));

			base.unhighlightCell(0, (Decimal[i] + 1), null, null);
			base.unhighlightCell(1, (Decimal[i] + 1), null, null);

			if (showQuestions) {
				if (i == 0) {
					FillInBlanksQuestionModel hexQuestion = new FillInBlanksQuestionModel(
							"hexQuestion");
					hexQuestion.setPrompt("The hash value of " + Decimal[i + 1]
							+ " is?");
					hexQuestion.addAnswer(
							(String) nMap.get(Decimal[i + 1] + 1), 1,
							"Correct.");
					lang.addFIBQuestion(hexQuestion);
				}
			}

			lang.nextStep();
		}
	}

	private void showFinalText() {
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		all.add(new Text(textGenerator, new Coordinates(20, 80), "CONCLUSION",
				"", null, textProps));
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.PLAIN, 14));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 120),
				"The number of steps required is directly proportional to the precision desired of its result. The Geohash encryption algorithm is thus of",
				"", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 140),
				"linear complexity.", "", null, textProps));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 190),
				"Therefore it provides an efficient algorithm to encrypt the coordinates of a position into a single alphanumerical code which is furthermore",
				"", null, textProps));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 210),
				"still usable when shortened by characters at the end, albeit with a loss of precision.",
				"", null, textProps));
	}

	private void hideAll() {
		for (Primitive p : all) {
			p.hide();
		}
	}

	public String getName() {
		return "Geohash Encryption";
	}

	public String getAlgorithmName() {
		return "Geohash";
	}

	public String getAnimationAuthor() {
		return "Thomas Klir, Philipp Müller";
	}

	public String getDescription() {
		return "Geohash is a latitude/longitude geocode system invented by Gustavo Niemeyer. It is a hierarchical spatial data structure which subdivides space into buckets of grid shape. "
				+ "Geohashes are short codes which uniquely identify positions on the Earth, allowing for convenient referencing in emails, websites or databases. They offer properties like arbitrary precision and the possibility of gradually removing characters from the end of the code to reduce its size (and gradually lose precision in the process). "
				+ "\n"
				+ "\n"
				+ "Steps:"
				+ "\n"
				+ "Generation of Bitcode"
				+ "\n"
				+ "    Latitude"
				+ "\n"
				+ "    Longitude"
				+ "\n"
				+ "\n"
				+ "Bitcode Merging"
				+ "\n"
				+ "\n"
				+ "Conversion of Bitcode to Geohash";
	}

	public String getCodeExample() {
		return "- Generate bitcode for latitude and longitude each:" + "\n"
				+ "  (JAVA)" + "\n" + "  int i = 0;" + "\n"
				+ "  min[i] = -minmax;" + "\n" + "  max[i] = minmax;" + "\n"
				+ "  while (i < (precision * 5)) {" + "\n"
				+ "  	avg[i] = (min[i] + max[i]) / 2;" + "\n"
				+ "	if (value < avg[i]) {" + "\n" + "		bit[i] = 0;" + "\n"
				+ "		min[i + 1] = min[i];" + "\n" + "		max[i + 1] = avg[i];"
				+ "\n" + "	} else {" + "\n" + "		bit[i] = 1;" + "\n"
				+ "		min[i + 1] = avg[i];" + "\n" + "		max[i + 1] = max[i];"
				+ "\n" + "	}" + "\n" + "	i++;" + "\n" + "  }" + "\n"
				+ "  return bit;" + "\n" + "\n"
				+ "- Interlace generated bitcodes into single bitcode" + "\n"
				+ "\n" + "- Convert resulting bitcode into Base32";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.US;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}