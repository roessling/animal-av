package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringMatrixGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.MatrixProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;

public class GeohashDecryption implements Generator {
	private Language lang;
	private TextProperties textProps;
	private SourceCodeProperties sourceCodeProps;
	private MatrixProperties matrixProps;
	private TextProperties highlightedTextProps;
	private RectProperties rectProps;
	private ArrayProperties arrayProps;
	private boolean showQuestions;

	private String geohash;
	private int precision;

	private List<Primitive> all = new LinkedList<Primitive>();

	private AnimalTextGenerator textGenerator;
	private AnimalRectGenerator rectGenerator;
	private StringMatrix base;

	private Text header;
	private Rect head;

	private int[] longBit;
	private int[] latBit;

	private double[] longitude;
	private double[] latitude;

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		init();

		matrixProps = (MatrixProperties) props
				.getPropertiesByName("matrixProps");
		highlightedTextProps = (TextProperties) props
				.getPropertiesByName("highlightedTextProps");
		geohash = (String) primitives.get("geohash");
		sourceCodeProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProps");
		rectProps = (RectProperties) props.getPropertiesByName("rectProps");
		textProps = (TextProperties) props.getPropertiesByName("textProps");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		showQuestions = (Boolean) primitives.get("showQuestions");

		// set precision
		precision = (geohash.length() + 1) / 2;

		initializeAlgorithm();

		lang.finalizeGeneration();
		// TODO: Remove replaceAlls once grids are fixed to display correctly
		// Negative side effect of current workaround: All cellWidths of a table
		// are equal, therefore table cells may be unnecessarily large
		return lang
				.toString()
				.replaceAll("refresh", "")
				.replaceAll(
						"columns 4 style plain",
						"columns 4 style plain cellWidth "
								+ Math.max(100, precision * 50)
								+ " fixedCellSize")
				.replaceAll(
						"columns 4 style matrix",
						"columns 4 style matrix cellWidth "
								+ Math.max(100, precision * 50)
								+ " fixedCellSize")
				.replaceAll(
						"columns 4 style table",
						"columns 4 style table cellWidth "
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
			inputData.setPrompt("Which type of data does a Geohash store?");
			inputData.addAnswer("File Data", 0,
					"Wrong, a Geohash stores a compressed coordinate pair.");
			inputData.addAnswer("Telephone Numbers", 0,
					"Wrong, a Geohash stores a compressed coordinate pair.");
			inputData.addAnswer("Coordinates", 1,
					"Correct, a Geohash stores a compressed coordinate pair.");
			lang.addMCQuestion(inputData);
		}

		splitGeohash();
		hideAll();

		longitude = decryptCoordinate(longBit, 180, "longitude",
				"Longitude Approximation", true);
		lang.nextStep();
		hideAll();

		latitude = decryptCoordinate(latBit, 90, "latitude",
				"Latitude Approximation", false);
		lang.nextStep();
		hideAll();
		
		showResult();
		lang.nextStep("Result");
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
		header = new Text(textGenerator, new Coordinates(20, 30),
				"Geohash - Decryption", "header", null, textProps);

		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		head = new Rect(rectGenerator, new Offset(-5, -5, header, "NW"),
				new Offset(5, 5, header, "SE"), "hRect", null, rectProps);
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
				"Conversion from Geohash to Bitcode", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 340),
				"Bitcode Splitting", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 370),
				"    Longitude bitcode", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 400),
				"    Latitude bitcode", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 430),
				"Generation of coordinates", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 460),
				"    Longitude", "", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 490),
				"    Latitude", "", null, textProps));
	}

	private void splitGeohash() {
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		all.add(new Text(textGenerator, new Offset(0, 30, header, "SW"),
				"Splitting", "", null, textProps));

		Text[] binaryCode = transformDecToBin(transformBase32ToDec(splittingSolution()));

		longBit = generateLongitudeCode(binaryCode);
		latBit = generateLatitudeCode(binaryCode);
	}

	private char[] splittingSolution() {
		Text geohashvalue = lang.newText(new Offset(70, 120, header, "SW"),
				"Geohash: ", "Geohashvalue", null, textProps);
		all.add(geohashvalue);
		Text solutionNormal = lang.newText(new Offset(20, -24, geohashvalue,
				"SE"), "" + geohash, "solutionNormal", null, textProps);
		// TODO: replace -24 with the textProps size
		lang.nextStep("Geohash Conversion to Bitcodes");
		solutionNormal.hide();

		// In the variable geohashChars is the geohash value splitted as a char
		// Array
		char[] geohashChars = geohash.toCharArray();

		Text[] solutionSplitted = new Text[geohashChars.length * 2 - 1];

		for (int i = 0; i < geohashChars.length; i++) {
			if (i == 0) {
				// TODO: fix y position relative to following
				solutionSplitted[i * 2] = lang.newText(new Offset(60, -24,
						geohashvalue, "SE"), "" + geohashChars[i], "", null,
						textProps);
			} else {
				solutionSplitted[i * 2] = lang.newText(
						new OffsetFromLastPosition(40, 0),
						"" + geohashChars[i], "", null, textProps);
			}
			all.add(solutionSplitted[i * 2]);

			if (i != geohashChars.length - 1) {
				solutionSplitted[i * 2 + 1] = lang.newText(new Coordinates(
						285 + (i * 85), 166), "|", "", null, textProps);
				all.add(solutionSplitted[i * 2 + 1]);
			}

		}

		all.add(lang.newText(new OffsetFromLastPosition(45, 0), "(base32)", "",
				null, textProps));
		lang.nextStep();

		generateBase32Matrix("Base32 Conversion");
		lang.nextStep();

		return geohashChars;
	}

	private void generateBase32Matrix(String next) {
		AnimalStringMatrixGenerator matrixGenerator = new AnimalStringMatrixGenerator(
				(AnimalScript) lang);

		String[][] data = new String[2][33];
		base = new StringMatrix(matrixGenerator,
				new Offset(0, 60, header, "SW"), data, "base", null,
				matrixProps);
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
	}

	private Text[] transformBase32ToDec(char[] geohashChars) {

		// Match the base32 value to a searchable Map
		Map<String, Integer> nMap = new HashMap<String, Integer>();
		for (int i = 0; i < 32; i++)
			nMap.put(base.getElement(1, i + 1), i + 1);

		Text[] decimal = new Text[geohashChars.length * 2 - 1];

		for (int i = 0; i < geohashChars.length; i++) {

			int value = nMap.get("" + geohashChars[i]) - 1;

			if (i == 0) {
				// TODO: fix y position relative to following
				if (value < 10)
					decimal[i * 2] = lang.newText(new Offset(220, 150, header,
							"SW"), "0" + value, "", null, textProps);
				else
					decimal[i * 2] = lang.newText(new Offset(220, 150, header,
							"SW"), "" + value, "", null, textProps);
			} else {
				if (value < 10)
					decimal[i * 2] = lang.newText(new OffsetFromLastPosition(
							35, 0), "0" + value, "", null, textProps);
				else
					decimal[i * 2] = lang.newText(new OffsetFromLastPosition(
							35, 0), "" + value, "", null, textProps);
			}
			all.add(decimal[i * 2]);

			if (i != geohashChars.length - 1) {
				decimal[i * 2 + 1] = lang.newText(new Coordinates(
						285 + (i * 85), 196), "|", "", null, textProps);
				all.add(decimal[i * 2 + 1]);
			}

		}
		all.add(lang.newText(new OffsetFromLastPosition(50, 0), "(decimal)",
				"", null, textProps));
		lang.nextStep();

		return decimal;
	}

	private Text[] transformDecToBin(Text[] decimal) {

		Text[] bin = new Text[geohash.length() * 5 + geohash.length() - 1];

		char[] oneBinaryValue;
		for (int i = 0; i < decimal.length; i++) {
			if (i % 2 == 0) {
				oneBinaryValue = (Integer.toBinaryString(new Integer(decimal[i]
						.getText()) + 32).substring(1)).toCharArray();
				if (i == 0) {
					bin[0] = lang.newText(new Offset(200, 175, header, "SW"),
							"" + oneBinaryValue[0], "", null, textProps);
					bin[1] = lang.newText(new OffsetFromLastPosition(10, 0), ""
							+ oneBinaryValue[1], "", null, textProps);
					bin[2] = lang.newText(new OffsetFromLastPosition(10, 0), ""
							+ oneBinaryValue[2], "", null, textProps);
					bin[3] = lang.newText(new OffsetFromLastPosition(10, 0), ""
							+ oneBinaryValue[3], "", null, textProps);
					bin[4] = lang.newText(new OffsetFromLastPosition(10, 0), ""
							+ oneBinaryValue[4], "", null, textProps);
				} else {
					bin[(i / 2) * 6] = lang
							.newText(new OffsetFromLastPosition(20, 0), ""
									+ oneBinaryValue[0], "", null, textProps);
					bin[(i / 2) * 6 + 1] = lang.newText(
							new OffsetFromLastPosition(10, 0), ""
									+ oneBinaryValue[1], "", null, textProps);
					bin[(i / 2) * 6 + 2] = lang.newText(
							new OffsetFromLastPosition(10, 0), ""
									+ oneBinaryValue[2], "", null, textProps);
					bin[(i / 2) * 6 + 3] = lang.newText(
							new OffsetFromLastPosition(10, 0), ""
									+ oneBinaryValue[3], "", null, textProps);
					bin[(i / 2) * 6 + 4] = lang.newText(
							new OffsetFromLastPosition(10, 0), ""
									+ oneBinaryValue[4], "", null, textProps);
				}
			} else
				bin[((i + 1) / 2) * 5 + (((i + 1) / 2) - 1)] = lang.newText(
						new OffsetFromLastPosition(25, 0), "|", "", null,
						textProps);
		}
		for (Text text : bin) {
			all.add(text);
		}
		all.add(lang.newText(new OffsetFromLastPosition(25, 0), "(binary)", "",
				null, textProps));
		lang.nextStep();

		return bin;
	}

	private int[] generateLongitudeCode(Text[] binaryCode) {
		all.add(new Text(textGenerator, new Offset(0, 220, header, "SW"),
				"Longitude code: ", "", null, textProps));
		return generateCode("Longitude", binaryCode);
	}

	private int[] generateLatitudeCode(Text[] binaryCode) {
		all.add(new Text(textGenerator, new Offset(20, 250, header, "SW"),
				"Latitude code: ", "", null, textProps));
		return generateCode("Latitude", binaryCode);
	}

	private int[] generateCode(String name, Text[] binaryCode) {
		int Coordinate = 220;
		int value = 3;
		int oddEven = 0;

		if (name.equals("Latitude")) {
			Coordinate = 250;
			value = 2;
			oddEven = 1;
		}
		Text[] Code = new Text[(geohash.length() % 2 == 0) ? geohash.length() / 2 * 5
				: (((5 * (geohash.length() - 1) / 2)) + value)];
		int[] bit = new int[(geohash.length() % 2 == 0) ? geohash.length() / 2 * 5
				: (((5 * (geohash.length() - 1) / 2)) + value)];

		highlightedTextProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) highlightedTextProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));

		lang.nextStep();

		int bitCounter = 0;
		boolean initial = true;
		int slashCounter = 0;
		for (int i = 0; i < binaryCode.length; i++) {
			if (!(slashCounter % 5 == 0) || slashCounter == 0) {
				if (!initial)
					bitCounter++;
				slashCounter++;
				if (bitCounter % 2 == oddEven) {
					binaryCode[i]
							.changeColor(
									null,
									(Color) highlightedTextProps
											.get(AnimationPropertiesKeys.COLOR_PROPERTY),
									null, null);
					if (i == oddEven)
						Code[bitCounter / 2] = lang.newText(new Offset(180,
								Coordinate, header, "SW"),
								"" + binaryCode[i].getText(), "", null,
								highlightedTextProps);
					else
						Code[bitCounter / 2] = lang.newText(
								new OffsetFromLastPosition(15, 0), ""
										+ binaryCode[i].getText(), "", null,
								highlightedTextProps);
					all.add(Code[bitCounter / 2]);
					bit[bitCounter / 2] = Integer.parseInt(binaryCode[i]
							.getText());
					lang.nextStep();
					binaryCode[i].changeColor(null, (Color) textProps
							.get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
							null);
					Code[bitCounter / 2].changeColor(null, (Color) textProps
							.get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
							null);
				}
				if (initial)
					initial = false;
			} else
				slashCounter = 0;
		}
		return bit;
	}

	private double[] decryptCoordinate(int[] bit, int minmax, String name,
			String next, boolean vertical) {
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.PLAIN, 18));
		Text sct = new Text(textGenerator, new Coordinates(20, 60),
				"source code " + name, "sourceCodeText" + name, null, textProps);
		all.add(sct);

		SourceCode sc = lang.newSourceCode(new Offset(0, 0, sct, "SW"),
				"sourceCode" + name, null, sourceCodeProps);
		all.add(sc);

		sc.addCodeLine("int i = 0;", null, 0, null); // 0
		sc.addCodeLine("min[i] = -" + minmax + ";", null, 0, null); // 1
		sc.addCodeLine("max[i] = " + minmax + ";", null, 0, null); // 2
		sc.addCodeLine("while(i < bit.length) {", null, 0, null); // 3
		sc.addCodeLine("avg[i] = (min[i] + max[i]) / 2;", null, 1, null); // 4
		sc.addCodeLine("if (bit[i] == 0) {", null, 1, null); // 5
		sc.addCodeLine("min[i+1] = min[i];", null, 2, null); // 6
		sc.addCodeLine("max[i+1] = avg[i];", null, 2, null); // 7
		sc.addCodeLine("} else {", null, 1, null); // 8
		sc.addCodeLine("min[i+1] = avg[i];", null, 2, null); // 9
		sc.addCodeLine("max[i+1] = max[i];", null, 2, null); // 10
		sc.addCodeLine("}", null, 1, null); // 11
		sc.addCodeLine("i++;", null, 1, null); // 12
		sc.addCodeLine("}", null, 0, null); // 13
		sc.addCodeLine("return (min[bit.length] + max[bit.length]) / 2;", null,
				0, null); // 14

		newLineAt(-90, false, Color.BLACK);
		newLineAt(90, false, Color.BLACK);
		newLineAt(-180, true, Color.BLACK);
		newLineAt(180, true, Color.BLACK);
		
		IntArray v = lang.newIntArray(new Coordinates(260, 260), bit, "variables"
				+ name, null, arrayProps);
		all.add(v);

		String[][] matrixContent = new String[bit.length + 2][4];
		matrixContent[0][0] = "i";
		matrixContent[0][1] = "min";
		matrixContent[0][2] = "avg";
		matrixContent[0][3] = "max";
		for (int row = 1; row < matrixContent.length; row++) {
			matrixContent[row][0] = Integer.toString(row - 1);
			for (int column = 1; column < matrixContent[0].length; column++) {
				matrixContent[row][column] = "";
			}
		}

		AnimalStringMatrixGenerator matrixGenerator = new AnimalStringMatrixGenerator(
				(AnimalScript) lang);
		StringMatrix sm = new StringMatrix(matrixGenerator, new Offset(0, 10, v, "SW"), matrixContent, "matrix", null, matrixProps);
		all.add(sm);

		double[] min = new double[bit.length + 1];
		double[] max = new double[bit.length + 1];
		double[] avg = new double[bit.length * 5 + 1];

		Polyline[] minLine = new Polyline[bit.length + 1];
		Polyline[] maxLine = new Polyline[bit.length + 1];
		Polyline[] avgLine = new Polyline[bit.length + 1];

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

		while (i < bit.length) { // 3
			sc.highlight(3);
			lang.nextStep();
			sc.unhighlight(3);

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
			avgLine[i].changeColor("Color", Color.BLUE, null, null);

			if (bit[i] == 0) { // 5
				v.highlightCell(i, null, null);
				sc.highlight(5);

				lang.nextStep();
				v.unhighlightCell(i, null, null);
				sc.unhighlight(5);

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

				min[i + 1] = min[i]; // 6
				sc.highlight(6);
				sm.put(i + 2, 1, Double.toString(min[i + 1]), null, null);
				sm.highlightCell(i + 1, 1, null, null);
				sm.highlightCell(i + 2, 1, null, null);
				minLine[i].hide();
				minLine[i + 1] = newLineAt(min[i + 1], vertical, Color.BLUE);
				minLine[i + 1].changeColor("Color", Color.RED, null, null);
				lang.nextStep();
				minLine[i + 1].changeColor("Color", Color.BLUE, null, null);
				sc.unhighlight(6);
				sm.unhighlightCell(i + 1, 1, null, null);
				sm.unhighlightCell(i + 2, 1, null, null);

				max[i + 1] = avg[i]; // 7
				sc.highlight(7);
				sm.put(i + 2, 3, Double.toString(max[i + 1]), null, null);
				sm.highlightCell(i + 1, 2, null, null);
				sm.highlightCell(i + 2, 3, null, null);
				maxLine[i].hide();
				maxLine[i + 1] = newLineAt(max[i + 1], vertical, Color.BLUE);
				maxLine[i + 1].changeColor("Color", Color.RED, null, null);
				lang.nextStep();
				maxLine[i + 1].changeColor("Color", Color.BLUE, null, null);
				sc.unhighlight(7);
				sm.unhighlightCell(i + 1, 2, null, null);
				sm.unhighlightCell(i + 2, 3, null, null);

			} else { // 8
				v.highlightCell(i, null, null);
				sc.highlight(5);

				lang.nextStep();
				v.unhighlightCell(i, null, null);
				sc.unhighlight(5);

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

				min[i + 1] = avg[i]; // 9
				sc.highlight(9);
				sm.put(i + 2, 1, Double.toString(min[i + 1]), null, null);
				sm.highlightCell(i + 1, 2, null, null);
				sm.highlightCell(i + 2, 1, null, null);
				minLine[i].hide();
				minLine[i + 1] = newLineAt(min[i + 1], vertical, Color.BLUE);
				minLine[i + 1].changeColor("Color", Color.RED, null, null);
				lang.nextStep();
				minLine[i + 1].changeColor("Color", Color.BLUE, null, null);
				sc.unhighlight(9);
				sm.unhighlightCell(i + 1, 2, null, null);
				sm.unhighlightCell(i + 2, 1, null, null);

				max[i + 1] = max[i]; // 10
				sc.highlight(10);
				sm.put(i + 2, 3, Double.toString(max[i + 1]), null, null);
				sm.highlightCell(i + 1, 3, null, null);
				sm.highlightCell(i + 2, 3, null, null);
				maxLine[i].hide();
				maxLine[i + 1] = newLineAt(max[i + 1], vertical, Color.BLUE);
				maxLine[i + 1].changeColor("Color", Color.RED, null, null);
				lang.nextStep();
				maxLine[i + 1].changeColor("Color", Color.BLUE, null, null);
				sc.unhighlight(10);
				sm.unhighlightCell(i + 1, 3, null, null);
				sm.unhighlightCell(i + 2, 3, null, null);

			} // 11

			i++; // 12
			sc.highlight(12);
			sm.unhighlightCell(i, 0, null, null);
			sm.highlightCell(i + 1, 0, null, null);
			lang.nextStep();
			sc.unhighlight(12);

		} // 13
		sc.highlight(3);
		lang.nextStep();
		sc.unhighlight(3);
		sm.unhighlightCell(i + 1, 0, null, null);

		sc.highlight(14);
		minLine[i].changeColor("Color", Color.RED, null, null);
		maxLine[i].changeColor("Color", Color.RED, null, null);
		avg[bit.length] = (min[bit.length] + max[bit.length]) / 2;
		
		double results[] = new double[2];
		results[0] = avg[bit.length];
		results[1] = (max[bit.length] - avg[bit.length]);

		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		Text st = lang.newText(new Offset(50, 10, head, "NE"),
				"approximated " + name + ": " + results[0] + " (+- " + results[1] + ")", "solutionText"
						+ name, null, textProps);
		all.add(st);
		return results; // 14
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
	
	private void showResult() {
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.BOLD, 20));
		all.add(new Text(textGenerator, new Coordinates(20, 80), "RESULT",
				"", null, textProps));	
		textProps.set(
				AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textProps
						.get(AnimationPropertiesKeys.FONT_PROPERTY))
						.getFamily(), Font.PLAIN, 14));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 120),
				"The Geohash '" + geohash + "' has been decrypted to the following coordinates:",
				"", null, textProps));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 160),
				"Longitude: "+longitude[0]+" (+- "+longitude[1]+")",
				"", null, textProps));	
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 190),
				"Latitude: "+latitude[0]+" (+- "+latitude[1]+")",
				"", null, textProps));
		
		if (showQuestions) {
			MultipleChoiceQuestionModel compressionQuestion = new MultipleChoiceQuestionModel(
					"compressionQuestion");
			compressionQuestion.setPrompt("How can you further compress a Geohash?");
			compressionQuestion.addAnswer("Cut characters at the beginning", 1,
					"Wrong, you need to cut characters at the end of the Geohash to further compress its stored coordinate pair.");
			compressionQuestion.addAnswer("Cut characters at the end", 1,
					"Correct, you can further compress a Geohash's stored coordinate pair by cutting characters at the end of the Geohash.");
			compressionQuestion.addAnswer("You have to manually recalculate it", 0,
					"Wrong, you can further compress a Geohash's stored coordinate pair by cutting characters at the end of the Geohash.");
			lang.addMCQuestion(compressionQuestion);
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
				"The number of steps required is directly proportional to the length of the given Geohash. The Geohash decryption algorithm is thus",
				"", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 140),
				"of linear complexity.", "", null, textProps));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 190),
				"Therefore it provides an efficient algorithm to decrypt a Geohash's alphanumerical code into approximated coordinates of a position.",
				"", null, textProps));
		all.add(new Text(
				textGenerator,
				new Coordinates(20, 240),
				"Shortening a Geohash prior to decryption, however, results in a loss of precision as coordinate precision is proportional to the",
				"", null, textProps));
		all.add(new Text(textGenerator, new Coordinates(20, 260),
				"decrypted Geohash's length.", "", null, textProps));
	}

	private void hideAll() {
		for (Primitive p : all) {
			p.hide();
		}
	}

	public String getName() {
		return "Geohash Decryption";
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
				+ "Conversion from Geohash to Bitcode"
				+ "\n"
				+ "Bitcode Spliting"
				+ "\n"
				+ "    Longitude bitcode"
				+ "\n"
				+ "    Latitude bitcode"
				+ "\n"
				+ "Generation of coordinates"
				+ "\n" + "    Longitude" + "\n" + "    Latitude";
	}

	public String getCodeExample() {
		return "- Convert Base32 Geohash to bitcode"
				+ "\n"
				+ "- Divide obtained bitcode into two bitcodes representing longitude and latitude"
				+ "\n"
				+ "- Convert bitcodes to longitude and latitude approximations:"
				+ "\n" + "  (JAVA)" + "\n" + "  int i = 0;" + "\n"
				+ "  min[i] = -minmax;" + "\n" + "  max[i] = minmax;" + "\n"
				+ "  while(i < bit.length) {" + "\n"
				+ "  	avg[i] = (min[i] + max[i]) / 2;" + "\n"
				+ "	if (bit[i] == 0) {" + "\n" + "		min[i + 1] = min[i];"
				+ "\n" + "		max[i + 1] = avg[i];" + "\n" + "	} else {" + "\n"
				+ "		min[i + 1] = avg[i];" + "\n" + "		max[i + 1] = max[i];"
				+ "\n" + "	}" + "\n" + "	i++;" + "\n" + "  }" + "\n"
				+ "  return (min[bit.length] + max[bit.length]) / 2;";
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