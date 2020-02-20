package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.PageAttributes.ColorType;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.executors.VariableRoleExecutor;
import algoanim.primitives.IntArray;
import algoanim.primitives.*;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.variables.IntegerVariable;
import animal.animator.FillInBlanksQuestionAction;
import animal.main.Animal;
import animal.variables.VariableRoles;
import translator.Translator;

/*
 * @author Philipp Grenz, Noémie Catherine Hélène Spiller
 * MapReduce
 */

public class MapReduce implements ValidatingGenerator {

	private Translator translator;
	private Locale loc;
	public Language lang;
	private String[] inputArray; // input
	private String[][] splitArray;
	private Pair[][] mapArray;
	private HashMap<String, List<Pair>> shuffleHashmap = new HashMap<String, List<Pair>>();
	private Pair[] reduceArray;
	private int splitsNum; // input

	private Text titleText;
	private SourceCode pseudoCode;
	private SourceCode inputText;
	private Group splitGroup;
	private Group splitTextGroup;
	private Group splitRectGroup;
	private Group mapGroup;
	private Group mapTextGroup;
	private Group mapRectGroup;
	private Group shuffleGroup;
	private Group shuffleTextGroup;
	private Group shuffleRectGroup;
	private Group reduceGroup;
	private Group reduceTextGroup;
	private Group reduceRectGroup;
	private Rect rectInputText;
	private Color rectHighlightColor;
	private Color rectFillColor;

	RectProperties rectProperties;
	SourceCodeProperties sourceCodeProps;
	SourceCodeProperties pseudoCodeProps;
	PolylineProperties polylineProp;

	private final String descriptionDE = "Mit MapReduce lassen sich gro\u00dfe Datenmengen schnell verarbeiten. Dabei nutzt MapReduce Parallelisierung\n"
			+ "und Verteilung der Aufgaben auf mehrere Systeme. Beim W\u00f6rter Z\u00e4hlen werden die eingegebenen W\u00f6rter zuerst\n"
			+ "auf die gegebenen Systeme verteilt (splitting). Danach wird f\u00fcr jedes einzelne Wort ein Paar gebildet,\n"
			+ "n\u00e4mlich (Wort, 1) (mapping). Anschlie\u00dfend werden neue Paare gebildet, dazu werden alle Zahlen von den\n"
			+ "Paaren mit dem selben Wort in eine Liste geschrieben, wodurch folgende Paare entstehen: (Wort, (1, 1, 1)) (shuffling).\n"
			+ "Im letzten Schritt werden die Vorkommnisse jedes Wortes zusammengez\u00e4hlt, indem alle Zahlen in der\n"
			+ "Liste der Paare addiert werden (reducing).";
	private SourceCode desc;
	private final String descriptionUS = "With MapReduce, large amounts of data can be processed quickly. MapReduce uses parallelization and distribution\n"
			+ "of tasks across multiple systems. When counting words, the entered words are first distributed\n"
			+ "to the given systems (splitting). Then a pair is formed for each individual word, namely (word, 1) (mapping).\n"
			+ "Then new pairs are formed, all numbers of the pairs with the same word are written into a list, resulting in\n"
			+ "the following pairs: (word, (1, 1, 1)) (shuffling). In the last step, the occurrences of each word are added\n"
			+ "together by adding all the numbers in the list of pairs (reducing).";

	private final String pseudoCodeString = "start(){\n" + "\tsplit();\n" + "\tmap();\n" + "\tshuffle();\n"
			+ "\treduce();\n" + "}\n" + " \n" + "split(inputArray){\n" + "\tfor(word in inputArray){\n"
			+ "\t\temit(word);\n" + "\t}\n" + "}\n" + " \n" + "map(key, values){\n" + "// key: split number\n"
			+ "// values: words in split\n" + "\tfor(word in values){\n" + "\t\temit(word, 1);\n" + "\t}\n" + "}\n"
			+ " \n" + "shuffle(key){\n" + "// key: word\n" + "\treducerIndex = key;\n" + "}\n" + " \n"
			+ "reduce(key, values){\n" + "// key: word\n" + "// values: list of int\n" + "\tsum = 0;\n"
			+ "\tfor(num in values){\n" + "\t\tsum += num;\n" + "\t}\n" + "\temit(sum);\n" + "}";

	private String summaryUS;
	
	private String summaryDE;
	
	public MapReduce(String path, Locale loc) {
		translator = new Translator(path, loc);
		this.loc = loc;

		rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		sourceCodeProps = new SourceCodeProperties();
		sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.ORANGE);
		sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		pseudoCodeProps = new SourceCodeProperties();
		pseudoCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		pseudoCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
		pseudoCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		pseudoCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		polylineProp = new PolylineProperties();
		polylineProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

	}

	private void start() {

		titleText = lang.newText(new Coordinates(20, 20), translator.translateMessage("titleText"), "titleText",
				null);
		titleText.setFont(new Font("SansSerif", Font.BOLD, 24), new TicksTiming(0), new TicksTiming(0));

		SourceCodeProperties tempProps = new SourceCodeProperties();
		tempProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
		tempProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		desc = lang.newSourceCode(new Offset(20, 20, titleText, AnimalScript.DIRECTION_SW), "description", null,
				tempProps);

		if (loc.equals(Locale.GERMANY))
			desc.addMultilineCode(descriptionDE, null, null);
		else if (loc.equals(Locale.US))
			desc.addMultilineCode(descriptionUS, null, null);

		lang.nextStep(translator.translateMessage("introduction"));

		desc.hide();

		pseudoCode = lang.newSourceCode(new Offset(0, 40, titleText, AnimalScript.DIRECTION_SW), "pseudoCode", null,
				pseudoCodeProps);
		pseudoCode.addMultilineCode(getCodeExample(), "pc", null);

		inputText = lang.newSourceCode(new Offset(60, 60, pseudoCode, AnimalScript.DIRECTION_NE), "inputText", null,
				sourceCodeProps);
		for (int i = 0; i < inputArray.length; i++) {
			inputText.addCodeLine(inputArray[i], "" + i, 0, null);
		}

		rectInputText = lang.newRect(new Offset(-5, -5, inputText, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, inputText, AnimalScript.DIRECTION_SE), null, null, rectProperties);

		lang.nextStep();

		pseudoCode.highlight(0);

		lang.nextStep();

		pseudoCode.toggleHighlight(0, 1);

		lang.nextStep();

		split();
		map();
		shuffle();
		reduce();

		lang.nextStep();

		LinkedList<Primitive> textList = new LinkedList<Primitive>();
		LinkedList<Primitive> rectList = new LinkedList<Primitive>();
		LinkedList<Primitive> resultList = new LinkedList<Primitive>();

		// new SourceCode

		SourceCode curCode;
		curCode = lang.newSourceCode(new Offset(80, 0, reduceGroup, AnimalScript.DIRECTION_NE), "finalResult", null,
				sourceCodeProps);

		for (int i = 0; i < reduceArray.length; i++) {
			curCode.addCodeLine(reduceArray[i].toString(), null, 0, null);
		}

		textList.add(curCode);
		resultList.add(curCode);

		// new Rectangle
		Rect curRect = lang.newRect(new Offset(-5, -5, curCode, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, curCode, AnimalScript.DIRECTION_SE), null, null, rectProperties);
		rectList.add(curRect);
		resultList.add(curRect);
		// new Arrows
		Node[] nodes = new Node[2];
		nodes[1] = new Offset(0, 0, curRect, AnimalScript.DIRECTION_W);
		for (Primitive p : reduceRectGroup.getPrimitives()) {
			nodes[0] = new Offset(0, 0, p, AnimalScript.DIRECTION_E);
			resultList.add(lang.newPolyline(nodes, null, null, polylineProp));

		}
		lang.nextStep(translator.translateMessage("finalResult"));
		
		summaryUS = "The " + inputArray.length + " words from the input have become " + reduceArray.length + " tuples\n" + " which consist of a word and a number" 
				+ "through the operations split, map, shuffle and reduce.";
		summaryDE = "Die " + inputArray.length + " W\u00f6rter aus dem Input sind durch die Operationen split, map,\n"
				+ "shuffle und reduce zu " + reduceArray.length + " Tupel, welche aus einem Wort und einer Zahl bestehen, geworden. ";
		
		
		lang.hideAllPrimitivesExcept(titleText);
		SourceCode summary = lang.newSourceCode(new Offset(0, 20, titleText, AnimalScript.DIRECTION_SW), "summary", null, sourceCodeProps);
		if(loc.equals(Locale.GERMANY))
			summary.addMultilineCode(summaryDE, "summaryDE", null);
		else if(loc.equals(Locale.US))
			summary.addMultilineCode(summaryUS, "summaryUS", null);
		
		lang.nextStep(translator.translateMessage("summary"));
	}

	private void split() {

		pseudoCode.toggleHighlight(1, 7);
		rectInputText.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, rectHighlightColor, null, null);
		lang.nextStep("split");

		pseudoCode.toggleHighlight(7, 8);
		splitArray = new String[splitsNum][(int) Math.ceil(inputArray.length / (double) splitsNum)];
		int inputIndex = 0;
		Rect curRect = null;
		SourceCode curText = null;
		Polyline curLine = null;
		Node[] nodes = new Node[2];
		nodes[0] = new Offset(0, 0, rectInputText, AnimalScript.DIRECTION_E);
		LinkedList<Primitive> splitGroupList = new LinkedList<Primitive>();
		LinkedList<Primitive> splitRectGroupList = new LinkedList<Primitive>();
		LinkedList<Primitive> splitTextGroupList = new LinkedList<Primitive>();
		
		for (int i = 0; i < splitArray.length; i++) {
			for (int j = 0; j < splitArray[0].length && inputIndex < inputArray.length; j++, inputIndex++) {
				splitArray[i][j] = inputArray[inputIndex];

				inputText.highlight(inputIndex);

				lang.nextStep();

				pseudoCode.toggleHighlight(8, 9);
				if (i == 0 && j == 0) {
					curText = lang.newSourceCode(new Offset(120, -10, rectInputText, AnimalScript.DIRECTION_NE),
							"splitMat" + i + "" + j, null, sourceCodeProps);
					curText.addCodeLine(inputArray[inputIndex], null, 0, null);
					splitGroupList.add(curText);
					splitTextGroupList.add(curText);

					curRect = lang.newRect(new Offset(-5, -5, curText, AnimalScript.DIRECTION_NW),
							new Offset(5, 5, curText, AnimalScript.DIRECTION_SE), "splitRect" + i, null,
							rectProperties);
					nodes[1] = new Offset(0, 0, curRect, AnimalScript.DIRECTION_W);
					curLine = lang.newPolyline(nodes, "line", null, polylineProp);
					splitGroupList.add(curLine);

				} else if (j == 0) {
					curText = lang.newSourceCode(new Offset(0, 20, curText, AnimalScript.DIRECTION_SW),
							"splitMat" + i + "" + j, null, sourceCodeProps);
					curText.addCodeLine(inputArray[inputIndex], null, 0, null);
					splitGroupList.add(curText);
					splitTextGroupList.add(curText);

					curRect = lang.newRect(new Offset(-5, -5, curText, AnimalScript.DIRECTION_NW),
							new Offset(5, 5, curText, AnimalScript.DIRECTION_SE), "splitRect" + i, null,
							rectProperties);
					nodes[1] = new Offset(0, 0, curRect, AnimalScript.DIRECTION_W);
					curLine = lang.newPolyline(nodes, "line", null, polylineProp);
					splitGroupList.add(curLine);

				} else {
					curText.addCodeLine(inputArray[inputIndex], "splitMat" + i + "" + j, 0, null);

					curRect.hide();
					splitGroupList.remove(curRect);
					splitRectGroupList.remove(curRect);
					curRect = lang.newRect(new Offset(-5, -5, curText, AnimalScript.DIRECTION_NW),
							new Offset(5, 5, curText, AnimalScript.DIRECTION_SE), "splitRect" + i, null,
							rectProperties);
				}

				splitGroupList.add(curRect);
				splitRectGroupList.add(curRect);

				lang.nextStep();

				pseudoCode.toggleHighlight(9, 8);
				inputText.unhighlight(inputIndex);
			}

		}
		splitGroup = lang.newGroup(splitGroupList, "splitGroup");
		splitRectGroup = lang.newGroup(splitRectGroupList, "splitRectGroup");
		splitTextGroup = lang.newGroup(splitTextGroupList, "splitTextGroup");
		pseudoCode.toggleHighlight(8, 2);
		rectInputText.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, rectFillColor, null, null);
	}

	private void map() {

		lang.nextStep();
		pseudoCode.toggleHighlight(2, 13);
		splitRectGroup.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, rectHighlightColor, null, null);

		mapArray = new Pair[splitsNum][(int) Math.ceil(inputArray.length / (double) splitsNum)];
		for (int i = 0; i < splitArray.length; i++) {
			for (int j = 0; j < splitArray[0].length; j++) {
				if (splitArray[i][j] != null) {
					mapArray[i][j] = new Pair(splitArray[i][j], "1");
				}
			}
		}

		// question
		Random rand = new Random();
		int systemNumber = rand.nextInt(mapArray.length);
		String word = mapArray[systemNumber][0].first;
		String mapQuestion = translator.translateMessage("questionMap", word, Integer.toString(systemNumber + 1));
		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("mapQuestion");
		String feedback = translator.translateMessage("questionFeedback");
		question.addAnswer(Integer.toString(1), 1, feedback);
		question.setPrompt(mapQuestion);
		lang.addFIBQuestion(question);

		lang.nextStep("map");

		pseudoCode.toggleHighlight(13, 16);
		SourceCode curText = null;
		Rect curRect = null;
		LinkedList<Primitive> textList = new LinkedList<Primitive>();
		LinkedList<Primitive> rectList = new LinkedList<Primitive>();
		Node[] nodes = new Node[2];
		int splitCounter = 0;
		SourceCode[] mapTexts = new SourceCode[splitTextGroup.getPrimitives().size()];
		Rect[] mapRects = new Rect[splitTextGroup.getPrimitives().size()];
		Polyline[] mapArrows = new Polyline[splitTextGroup.getPrimitives().size()];
		for (int i = 0; i < ((SourceCode) splitTextGroup.getPrimitives().getFirst()).length(); i++) {
			// highlight words
			for (Primitive p : splitTextGroup.getPrimitives()) {
				if (((SourceCode) p).length() > i) {
					((SourceCode) p).highlight(i);
				}
			}
			// unhighlight words
			for (Primitive p : splitTextGroup.getPrimitives()) {
				if (i > 0 && ((SourceCode) p).length() > i - 1) {
					((SourceCode) p).unhighlight(i - 1);
				}
			}

			lang.nextStep();

			pseudoCode.toggleHighlight(16, 17);

			if (i == 0) {
				// new Rectangle, new SourceCode for every split
				splitCounter = 0;
				for (Primitive p : splitTextGroup.getPrimitives()) {
					if (splitCounter == 0) {
						// new SourceCode
						curText = lang.newSourceCode(new Offset(60, -15, splitRectGroup, AnimalScript.DIRECTION_NE),
								"map" + i, null, sourceCodeProps);
						curText.addCodeLine(mapArray[splitCounter][i].toString(), null, 0, null);
						mapTexts[splitCounter] = curText;
					} else {
						// new SourceCode
						curText = lang.newSourceCode(new Offset(0, 20, curText, AnimalScript.DIRECTION_SW), "map" + i,
								null, sourceCodeProps);
						curText.addCodeLine(mapArray[splitCounter][i].toString(), null, 0, null);
						mapTexts[splitCounter] = curText;
					}
					// new Rectangle
					curRect = lang.newRect(new Offset(-5, -5, curText, AnimalScript.DIRECTION_NW),
							new Offset(5, 5, curText, AnimalScript.DIRECTION_SE), null, null, rectProperties);
					mapRects[splitCounter] = curRect;

					// new Arrow
					nodes[0] = new Offset(0, 0, splitRectGroup.getPrimitives().get(splitCounter),
							AnimalScript.DIRECTION_E);
					nodes[1] = new Offset(0, 0, curRect, AnimalScript.DIRECTION_W);
					mapArrows[splitCounter] = lang.newPolyline(nodes, "line", null, polylineProp);

					splitCounter++;

				}
			} else {

				// new Rectangle, add to SourceCode to every split
				splitCounter = 0;
				curText = null;

				SourceCode tempText = null;
				SourceCode topText = null;
				for (Primitive p : splitTextGroup.getPrimitives()) {

					if (splitCounter == 0) {
						mapTexts[splitCounter].addCodeLine(mapArray[splitCounter][i].toString(), null, 0, null);

					} else {
						mapTexts[splitCounter].hide();
						topText = mapTexts[splitCounter - 1];
						tempText = lang.newSourceCode(new Offset(0, 20, topText, AnimalScript.DIRECTION_SW),
								"mapTexts" + splitCounter, null, sourceCodeProps);
						for (int ind = 0; ind <= i; ind++) {
							if (mapArray[splitCounter][ind] != null)
								tempText.addCodeLine(mapArray[splitCounter][ind].toString(), null, 0, null);
							else
								break;
						}
						mapTexts[splitCounter] = tempText;

					}
					mapArrows[splitCounter].hide();
					mapRects[splitCounter].hide();
					mapRects[splitCounter] = lang.newRect(
							new Offset(-5, -5, mapTexts[splitCounter], AnimalScript.DIRECTION_NW),
							new Offset(5, 5, mapTexts[splitCounter], AnimalScript.DIRECTION_SE), null, null,
							rectProperties);
					nodes[0] = new Offset(0, 0, splitRectGroup.getPrimitives().get(splitCounter),
							AnimalScript.DIRECTION_E);
					nodes[1] = new Offset(0, 0, mapRects[splitCounter], AnimalScript.DIRECTION_W);
					mapArrows[splitCounter] = lang.newPolyline(nodes, "line", null, polylineProp);

					splitCounter++;
				}

			}
			rectList = new LinkedList<Primitive>();
			textList = new LinkedList<Primitive>();
			textList = new LinkedList<Primitive>();
			for (int j = 0; j < mapTexts.length; j++) {
				textList.add(mapTexts[j]);
			}
			mapTextGroup = lang.newGroup(textList, null);
			rectList = new LinkedList<Primitive>();
			for (int j = 0; j < mapRects.length; j++) {
				rectList.add(mapRects[j]);
			}
			mapRectGroup = lang.newGroup(rectList, null);

			lang.nextStep();
			pseudoCode.toggleHighlight(17, 16);

		}
		// unhighlight words
		for (Primitive p : splitTextGroup.getPrimitives()) {
			if (((SourceCode) p).length() > 0)
				((SourceCode) p).unhighlight(((SourceCode) p).length() - 1);
		}
		splitRectGroup.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, rectFillColor, null, null);

		pseudoCode.toggleHighlight(16, 3);

	}

	private void shuffle() {
		lang.nextStep();
		pseudoCode.toggleHighlight(3, 21);
		mapRectGroup.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, rectHighlightColor, null, null);
		// highlight words
		for (Primitive p : mapTextGroup.getPrimitives()) {
			for (int i = 0; i < ((SourceCode) p).length(); i++) {
				((SourceCode) p).highlight(i);
			}

		}

		List<Pair> list;
		for (int i = 0; i < mapArray.length; i++) {
			for (int j = 0; j < mapArray[0].length; j++) {
				if (mapArray[i][j] != null) {
					String key = mapArray[i][j].first;
					if (shuffleHashmap.get(key) != null) {
						list = shuffleHashmap.get(key);
						list.add(mapArray[i][j]);
						shuffleHashmap.put(key, list);
					} else {
						list = new LinkedList<Pair>();
						list.add(mapArray[i][j]);
						shuffleHashmap.put(key, list);

					}
				}

			}
		}

		// question
		Random rand = new Random();
		int wordNumber = rand.nextInt(shuffleHashmap.size());
		String word = (String) shuffleHashmap.keySet().toArray()[wordNumber];
		int listSize = shuffleHashmap.get(word).size();
		String shuffleQuestion = translator.translateMessage("questionShuffle", word);
		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("shuffleQuestion");
		String feedback = translator.translateMessage("questionFeedback");
		question.addAnswer(Integer.toString(listSize), 1, feedback);
		question.setPrompt(shuffleQuestion);
		lang.addFIBQuestion(question);

		lang.nextStep("shuffle");
		pseudoCode.toggleHighlight(21, 23);

		LinkedList<Primitive> textList = new LinkedList<Primitive>();
		LinkedList<Primitive> rectList = new LinkedList<Primitive>();
		LinkedList<Primitive> shuffleList = new LinkedList<Primitive>();
		SourceCode curText = null;
		Rect curRect = null;

		int counter = 0;
		Node[] nodes = new Node[2];
		for (String key : shuffleHashmap.keySet()) {
			if (counter == 0) {
				// new SourceCode
				curText = lang.newSourceCode(new Offset(60, -15, mapRectGroup, AnimalScript.DIRECTION_NE),
						"shuffle" + counter, null, sourceCodeProps);
				curText.addCodeLine(key + ", " + listToString(shuffleHashmap.get(key)), null, 0, null);
				textList.add(curText);
				shuffleList.add(curText);

				// new Rectangle
				curRect = lang.newRect(new Offset(-5, -5, curText, AnimalScript.DIRECTION_NW),
						new Offset(5, 5, curText, AnimalScript.DIRECTION_SE), null, null, rectProperties);
				rectList.add(curRect);
				shuffleList.add(curRect);

			} else {
				// new SourceCode
				curText = lang.newSourceCode(new Offset(0, 20, curText, AnimalScript.DIRECTION_SW), "shuffle" + counter,
						null, sourceCodeProps);
				curText.addCodeLine(key + ", " + listToString(shuffleHashmap.get(key)), null, 0, null);
				textList.add(curText);
				shuffleList.add(curText);

				// new Rectangle
				curRect = lang.newRect(new Offset(-5, -5, curText, AnimalScript.DIRECTION_NW),
						new Offset(5, 5, curText, AnimalScript.DIRECTION_SE), null, null, rectProperties);
				rectList.add(curRect);
				shuffleList.add(curRect);

			}
			// new Arrow
			nodes[1] = new Offset(0, 0, curRect, AnimalScript.DIRECTION_W);

			for (int split = 0; split < splitsNum; split++) {
				for (int index = 0; index < mapArray[split].length; index++) {
					if (mapArray[split][index] != null && mapArray[split][index].first.equals(key)) {
						nodes[0] = new Offset(0, 0, mapRectGroup.getPrimitives().get(split), AnimalScript.DIRECTION_E);
						shuffleList.add(lang.newPolyline(nodes, "line", null, polylineProp));
						break;
					}
				}
			}
			counter++;
		}
		shuffleTextGroup = lang.newGroup(textList, null);
		shuffleRectGroup = lang.newGroup(rectList, null);
		shuffleGroup = lang.newGroup(shuffleList, null);
	}

	private void reduce() {
		Group sumGroup;
		Integer[] sumArray = new Integer[shuffleHashmap.keySet().size()];
		for (int i = 0; i < shuffleHashmap.keySet().size(); i++) {
			sumArray[i] = 0;
		}
		LinkedList<Primitive> sumList = new LinkedList<Primitive>();
		LinkedList<Primitive> reduceList = new LinkedList<Primitive>();
		LinkedList<Primitive> rectList = new LinkedList<Primitive>();
		LinkedList<Primitive> textList = new LinkedList<Primitive>();

		lang.nextStep();
		pseudoCode.toggleHighlight(23, 4);
		mapRectGroup.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, rectFillColor, null, null);
		// unhighlight words
		for (Primitive p : mapTextGroup.getPrimitives()) {
			for (int i = 0; i < ((SourceCode) p).length(); i++) {
				((SourceCode) p).unhighlight(i);
			}

		}
		lang.nextStep("reduce");

		pseudoCode.toggleHighlight(4, 26);
		shuffleRectGroup.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, rectHighlightColor, null, null);
		// highlight words
		for (Primitive p : shuffleTextGroup.getPrimitives()) {
			((SourceCode) p).highlight(0);

		}

		lang.nextStep();

		pseudoCode.toggleHighlight(26, 29);
		Text curText = null;
		for (Primitive p : shuffleRectGroup.getPrimitives()) {

			// new SourceCode
			curText = lang.newText(new Offset(20, 0, (Rect) p, AnimalScript.DIRECTION_E), "sum: 0", "sum", null);
			curText.setFont(new Font("SansSerif", Font.BOLD, 14), null, null);

			sumList.add(curText);
			reduceList.add(curText);
		}

		sumGroup = lang.newGroup(sumList, null);

		lang.nextStep();

		pseudoCode.toggleHighlight(29, 30);

		int biggestI = 0;
		reduceArray = new Pair[shuffleHashmap.keySet().size()];
		List<Pair> list;
		int index = 0;
		for (String key : shuffleHashmap.keySet()) {
			if (shuffleHashmap.get(key).size() > biggestI)
				biggestI = shuffleHashmap.get(key).size();
			list = shuffleHashmap.get(key);
			reduceArray[index] = new Pair(key, String.valueOf(list.size()));
			index++;
		}

		// question
		Random rand = new Random();
		int wordNumber = rand.nextInt(reduceArray.length);
		String word = reduceArray[wordNumber].first;
		String wordCount = reduceArray[wordNumber].second;
		String reduceQuestion = translator.translateMessage("questionReduce", word);
		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("reduceQuestion");
		String feedback = translator.translateMessage("questionFeedback");
		question.addAnswer(wordCount, 1, feedback);
		question.setPrompt(reduceQuestion);
		lang.addFIBQuestion(question);

		for (int i = 0; i < biggestI; i++) {
			lang.nextStep();

			pseudoCode.toggleHighlight(30, 31);
			index = 0;
			for (String key : shuffleHashmap.keySet()) {
				if (shuffleHashmap.get(key).size() > i) {
					((Text) sumGroup.getPrimitives().get(index)).setText("sum: " + ++sumArray[index], null, null);
				}
				index++;
			}

			lang.nextStep();

			pseudoCode.toggleHighlight(31, 30);
		}
		pseudoCode.toggleHighlight(30, 33);
		sumGroup.hide();

		Rect curRect = null;
		SourceCode curCode = null;
		index = 0;
		Node[] nodes = new Node[2];
		for (String key : shuffleHashmap.keySet()) {

			if (index == 0) {
				// new SourceCode
				curCode = lang.newSourceCode(new Offset(60, -15, shuffleRectGroup, AnimalScript.DIRECTION_NE),
						"reduce" + index, null, sourceCodeProps);
				curCode.addCodeLine(reduceArray[index].toString(), null, 0, null);
				textList.add(curCode);
				reduceList.add(curCode);

			} else {
				// new SourceCode
				curCode = lang.newSourceCode(new Offset(0, 20, curCode, AnimalScript.DIRECTION_SW), "reduce" + index,
						null, sourceCodeProps);
				curCode.addCodeLine(reduceArray[index].toString(), null, 0, null);
				textList.add(curCode);
				reduceList.add(curCode);

			}
			// new Rectangle
			curRect = lang.newRect(new Offset(-5, -5, curCode, AnimalScript.DIRECTION_NW),
					new Offset(5, 5, curCode, AnimalScript.DIRECTION_SE), null, null, rectProperties);
			rectList.add(curRect);
			reduceList.add(curRect);

			// new Arrow
			nodes[0] = new Offset(0, 0, shuffleRectGroup.getPrimitives().get(index), AnimalScript.DIRECTION_E);
			nodes[1] = new Offset(0, 0, curRect, AnimalScript.DIRECTION_W);
			reduceList.add(lang.newPolyline(nodes, "line", null, polylineProp));

			index++;
		}

		reduceGroup = lang.newGroup(reduceList, null);
		reduceRectGroup = lang.newGroup(rectList, null);
		reduceTextGroup = lang.newGroup(textList, null);

		lang.nextStep();

		pseudoCode.unhighlight(33);
		shuffleRectGroup.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, rectFillColor, null, null);
		// highlight words
		for (Primitive p : shuffleTextGroup.getPrimitives()) {
			((SourceCode) p).unhighlight(0);

		}

	}

	public void init() {
		polylineProp = new PolylineProperties();
		polylineProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		lang = new AnimalScript("MapReduce", "Philipp Grenz, No\u00e9mie Catherine H\u00e9l\u00e8ne Spiller", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public static void main(String[] args) {
		MapReduce mapReduceGeneratorDE = new MapReduce("resources/MapReduce", Locale.GERMANY);
		MapReduce mapReduceGeneratorUS = new MapReduce("resources/MapReduce", Locale.US);
		//Animal.startGeneratorWindow(mapReduceGeneratorDE);
		Animal.startGeneratorWindow(mapReduceGeneratorUS);
	}

	class Pair {
		String first = "";
		String second = "";

		Pair(String f, String s) {
			first = f;
			second = s;
		}

		@Override
		public String toString() {
			return first + ", " + second;
		}
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		inputArray = null;
		splitArray = null;
		mapArray = null;
		shuffleHashmap = new HashMap<String, List<Pair>>();
		reduceArray = null;
		inputArray = (String[]) primitives.get("stringArray");
		splitsNum = (int) primitives.get("int");
		ArrayProperties arrProps = (ArrayProperties) props.getPropertiesByName("rectangles");
		rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		rectFillColor = (Color) arrProps.get(AnimationPropertiesKeys.FILL_PROPERTY);
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, rectFillColor);
		rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) arrProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		rectHighlightColor = (Color) arrProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY);
		pseudoCodeProps = (SourceCodeProperties) props.getPropertiesByName("pseudoCode");
		sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("words");
		sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) sourceCodeProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName(), Font.BOLD, 16));

		start();
		lang.finalizeGeneration();

		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "MapReduce";
	}

	@Override
	public String getAnimationAuthor() {
		return "Philipp Grenz, No\u00e9mie Catherine H\u00e9l\u00e8ne Spiller";
	}

	@Override
	public String getCodeExample() {
		return pseudoCodeString;
	}

	@Override
	public Locale getContentLocale() {
		return loc;
	}

	@Override
	public String getDescription() {
		return translator.translateMessage("descriptionGenerator");
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return translator.translateMessage("nameGenerator");
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	private String listToString(List<Pair> list) {
		String ret = "(";
		for (Pair elem : list) {
			ret = ret + elem.second + ", ";
		}
		ret = ret.substring(0, ret.length() - 2);
		ret = ret + ")";
		return ret;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		int splits = (int) primitives.get("int");
		String[] array = (String[]) primitives.get("stringArray");
		return splits > 0 && array.length >= splits;
	}

}
