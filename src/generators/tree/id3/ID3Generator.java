package generators.tree.id3;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.Graph;
import algoanim.primitives.Group;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.tree.id3.tree.Node;
import generators.tree.id3.tree.Tree;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.ResourceLocator;

public class ID3Generator {

	/* CONSTANTS */

	private final int NUM_QUESTIONS = 6;

	/* CONFIGURABLE */

	private BiFunction<DataTable, String, Double> purityFunction;

	private int exampleID;

	private double probRandom;

	private double probSpecific;


	// TODO visualize recursion depth
	/* HELPERS */

	private int nodeIdentifier = 0;

	private int dataTableCounter = 0;

	private StringMatrix currentDataTable;

	private Tree resultToVisualize;

	private DataTable table;

	// TODO correctly working?
	private int recursionCounter = 0;

	private Timing timing = new TicksTiming(200);

	private LinkedList<String> codeText = new LinkedList<String>();

	private HashMap<String, String> dictionary = new HashMap<String, String>();

	private List<String> startText = new LinkedList<String>();

	private List<String> endText = new LinkedList<String>();

	private List<String> functionText = new LinkedList<String>();

	private int specificQuestionHelperCounter = 0;

	private HashMap<String, LinkedList<String>> valueMap = new HashMap<String, LinkedList<String>>();

	private int recCounter = 0;

	/* ANIMALSCRIPT OBJECTS */
	private Language lang;

	private SourceCode code;

	private StringMatrix datatable;

	private Rect codeRect;

	private Text title;

	private Text mainTableText;

	private StringMatrix temporaryDatatable;

	private Text temporaryTableText;

	private Text temporaryTableFeatureText;

	private Graph tree;

	private LinkedList<Text> edgeLabels;

	private LinkedList<Group> markers;

	private LinkedList<Text> nodeTitles;

	private Rect infoRect;

	private InfoBox infoBox;

	private InfoBox functionExplanationBox;

	private Rect functionExplanationRect;

	private LinkedList<Integer> questionNumbers = new LinkedList<Integer>();


	/* USER VARIABLES NOT MENTIONED ABOVE */

	private String[][] exampleTable = new String[][] { { "Motivation", "Dizzy", "Money", "Billiards?" }, { "a bit", "yes", "a lot", "no" },
			{ "good", "no", "average", "yes" }, { "none", "no", "average", "no" }, { "super", "yes", "a lot", "yes" },
			{ "a bit", "no", "average", "no" }, { "good", "no", "not much", "no" } };

	private Color nodeColor = new Color(65, 223, 244);

	private Color yesColor = new Color(65, 244, 71);

	private Color noColor = new Color(244, 197, 66);

	private Color tableHighlightColor = new Color(179, 255, 224);

	private Color codeHighlightColor = new Color(230, 138, 0);

	private Color codeColor = new Color(0, 0, 0);

	private int codeStyle = Font.PLAIN;

	private int codeSize = 10;

	private String codeFont = "Monospaced";

	private Color tableElemColor = new Color(0, 0, 0);

	private String tableFont = "SansSerif";

	private Color tableBorderColor = new Color(0, 0, 0);

	private Color explanationBoxFillColor = new Color(240, 240, 240);

	private Color explanationBoxColor = new Color(0, 0, 0);



	/* MAIN */
	public static void main(String[] args) throws Exception {

		// System.err.close();

		boolean simpleSetup = false;

		ID3Generator id3;
		if (simpleSetup) {
			id3 = new ID3Generator(PurityFunctions.INFORMATION_GAIN, 0, CodeText.PSEUDOCODE, Lang.ENGLISH, 0, 0);
		} else {
			id3 = new ID3Generator(true, 1, 0, 0,
					new String[][] { { "Motivation", "Tired", "Money", "Billiards?" }, { "a bit", "yes", "a lot", "no" },
							{ "good", "no", "average", "yes" }, { "none", "no", "average", "no" }, { "super", "yes", "a lot", "yes" },
							{ "a bit", "no", "average", "no" }, { "good", "no", "not much", "no" } },
					new Color(65, 223, 244), new Color(65, 244, 71), new Color(244, 197, 66), new Color(230, 138, 0), new Color(0, 0, 0), 10, false,
					false, new Font("Monospaced", 0, 0), new Color(179, 255, 224), new Color(0, 0, 0), new Color(0, 0, 0),
					new Font("SansSerif", 0, 0), new Color(240, 240, 240), new Color(0, 0, 0), true, true);
		}

		id3.execute();
	}


	private void setup(BiFunction<DataTable, String, Double> purityFunction, int exampleID, CodeText text, Lang language, double probRandom,
			double probSpecific) throws Exception {
		this.purityFunction = purityFunction;
		this.exampleID = exampleID;
		setupCode(text, language);
		setupTexts(language, purityFunction, exampleID);
		setupExample();
		PurityFunctions.setYes(dictionary.get("yes"));
		this.probRandom = probRandom;
		this.probSpecific = probSpecific;
		constructQuestionNumbersList();
	}


	public ID3Generator(BiFunction<DataTable, String, Double> purityFunction, int exampleID, CodeText text, Lang language, double probRandom,
			double probSpecific) throws Exception {
		setup(purityFunction, exampleID, text, language, probRandom, probSpecific);
	}


	public ID3Generator(boolean informationGainOrGiniIndex, int exampleID, double probRandom, double probSpecific, String[][] exampleTable,
			Color nodeColor, Color yesColor, Color noColor, Color codeHighlightColor, Color codeColor, int codeSize, boolean codeBold,
			boolean codeItalic, Font codeFont, Color tableHighlightColor, Color tableBorderColor, Color tableElementColor, Font tableFont,
			Color explanationBoxFillColor, Color explanationBoxColor, boolean language, boolean codeLanguage) throws Exception {

		BiFunction<DataTable, String, Double> purityFunction;
		if (informationGainOrGiniIndex) {
			purityFunction = PurityFunctions.INFORMATION_GAIN;
		} else {
			purityFunction = PurityFunctions.AVERAGE_GINI_INDEX;
		}

		CodeText text;
		if (codeLanguage) {
			text = CodeText.PSEUDOCODE;
		} else {
			text = CodeText.JAVA;
		}

		Lang textLanguage;
		if (language) {
			textLanguage = Lang.ENGLISH;
		} else {
			textLanguage = Lang.GERMAN;
		}

		double probRandomDouble = probRandom / 100.0;
		double probSpecificDouble = probSpecific / 100.0;

		this.codeColor = codeColor;
		this.codeSize = codeSize;
		this.codeStyle = 0 + ((codeBold) ? Font.BOLD : 0) + ((codeItalic) ? Font.ITALIC : 0);
		this.codeFont = codeFont.getName();// .split("\\.")[0];
		this.tableBorderColor = tableBorderColor;
		this.tableElemColor = tableElementColor;
		this.tableFont = tableFont.getName();
		this.explanationBoxFillColor = explanationBoxFillColor;
		this.explanationBoxColor = explanationBoxColor;

		this.exampleTable = exampleTable;
		this.noColor = noColor;
		this.yesColor = yesColor;
		this.nodeColor = nodeColor;
		this.tableHighlightColor = tableHighlightColor;
		this.codeHighlightColor = codeHighlightColor;

		exampleID = exampleID - 1;

		setup(purityFunction, exampleID, text, textLanguage, probRandomDouble, probSpecificDouble);

	}


	private void setupExample() {

		String[] featureNames;

		switch (exampleID) {

		case 0:
			featureNames = new String[] { dictionary.get("motivation"), dictionary.get("tired"), dictionary.get("money") };
			table = new DataTable(featureNames, dictionary.get("billards"));
			table.addInstance("1", dictionary.get("abit"), dictionary.get("yes"), dictionary.get("alot"), dictionary.get("no"));
			table.addInstance("2", dictionary.get("good"), dictionary.get("no"), dictionary.get("average"), dictionary.get("yes"));
			table.addInstance("3", dictionary.get("none"), dictionary.get("no"), dictionary.get("average"), dictionary.get("no"));
			table.addInstance("4", dictionary.get("super"), dictionary.get("yes"), dictionary.get("alot"), dictionary.get("yes"));
			table.addInstance("5", dictionary.get("abit"), dictionary.get("no"), dictionary.get("average"), dictionary.get("no"));
			table.addInstance("6", dictionary.get("good"), dictionary.get("no"), dictionary.get("notmuch"), dictionary.get("no"));
			break;

		case 1:
			featureNames = new String[] { dictionary.get("size"), dictionary.get("handcrafted"), dictionary.get("colors") };
			table = new DataTable(featureNames, dictionary.get("likeit"));
			table.addInstance("1", dictionary.get("average"), dictionary.get("no"), dictionary.get("many"), dictionary.get("yes"));
			table.addInstance("2", dictionary.get("small"), dictionary.get("no"), dictionary.get("some"), dictionary.get("no"));
			table.addInstance("3", dictionary.get("huge"), dictionary.get("yes"), dictionary.get("some"), dictionary.get("yes"));
			table.addInstance("4", dictionary.get("small"), dictionary.get("yes"), dictionary.get("many"), dictionary.get("yes"));
			table.addInstance("5", dictionary.get("small"), dictionary.get("no"), dictionary.get("blackwhite"), dictionary.get("no"));
			break;

		case 2:
			featureNames = new String[] { dictionary.get("style"), dictionary.get("creativity"), dictionary.get("violin") };
			table = new DataTable(featureNames, dictionary.get("hit"));
			table.addInstance("1", dictionary.get("country"), dictionary.get("normal"), dictionary.get("yes"), dictionary.get("no"));
			table.addInstance("2", dictionary.get("country"), dictionary.get("high"), dictionary.get("yes"), dictionary.get("yes"));
			table.addInstance("3", dictionary.get("rock"), dictionary.get("normal"), dictionary.get("no"), dictionary.get("yes"));
			table.addInstance("4", dictionary.get("pop"), dictionary.get("low"), dictionary.get("yes"), dictionary.get("yes"));
			table.addInstance("5", dictionary.get("pop"), dictionary.get("low"), dictionary.get("no"), dictionary.get("no"));
			table.addInstance("6", dictionary.get("country"), dictionary.get("high"), dictionary.get("yes"), dictionary.get("yes"));
			break;
		/*
		 * case 2: featureNames = new String[] { dictionary.get("style"),
		 * dictionary.get("creativity"), dictionary.get("violin") }; table = new
		 * DataTable(featureNames, dictionary.get("hit"));
		 * table.addInstance("1", dictionary.get("country"),
		 * dictionary.get("normal"), dictionary.get("yes"),
		 * dictionary.get("no")); table.addInstance("4", dictionary.get("pop"),
		 * dictionary.get("low"), dictionary.get("yes"), dictionary.get("yes"));
		 * table.addInstance("2", dictionary.get("country"),
		 * dictionary.get("high"), dictionary.get("yes"),
		 * dictionary.get("yes")); table.addInstance("3",
		 * dictionary.get("rock"), dictionary.get("normal"),
		 * dictionary.get("no"), dictionary.get("yes")); table.addInstance("5",
		 * dictionary.get("pop"), dictionary.get("low"), dictionary.get("no"),
		 * dictionary.get("no")); table.addInstance("6",
		 * dictionary.get("country"), dictionary.get("high"),
		 * dictionary.get("yes"), dictionary.get("yes")); break;
		 */

		case 3:
			featureNames = new String[] { dictionary.get("height"), dictionary.get("slope"), dictionary.get("snow") };
			table = new DataTable(featureNames, dictionary.get("coped"));
			table.addInstance("1", dictionary.get("normal"), dictionary.get("steep"), dictionary.get("yes"), dictionary.get("yes"));
			table.addInstance("2", dictionary.get("high"), dictionary.get("verysteep"), dictionary.get("no"), dictionary.get("yes"));
			table.addInstance("3", dictionary.get("normal"), dictionary.get("steep"), dictionary.get("no"), dictionary.get("yes"));
			break;

		case 4:
			featureNames = new String[] { dictionary.get("author"), dictionary.get("genre"), dictionary.get("perspective") };
			table = new DataTable(featureNames, dictionary.get("recommend"));
			table.addInstance("1", dictionary.get("sevens"), dictionary.get("fantasy"), dictionary.get("omniscient"), dictionary.get("yes"));
			table.addInstance("2", dictionary.get("sevens"), dictionary.get("sci-fi"), dictionary.get("firstperson"), dictionary.get("no"));
			table.addInstance("3", dictionary.get("kbloom"), dictionary.get("fantasy"), dictionary.get("thirdperson"), dictionary.get("no"));
			table.addInstance("4", dictionary.get("sevens"), dictionary.get("horror"), dictionary.get("firstperson"), dictionary.get("no"));
			table.addInstance("5", dictionary.get("lhill"), dictionary.get("horror"), dictionary.get("thirdperson"), dictionary.get("yes"));
			table.addInstance("6", dictionary.get("kbloom"), dictionary.get("sci-fi"), dictionary.get("thirdperson"), dictionary.get("yes"));
			break;

		case 5:
			featureNames = new String[] { dictionary.get("skills"), dictionary.get("experience"), dictionary.get("grade") };
			table = new DataTable(featureNames, dictionary.get("hired"));
			table.addInstance("1", dictionary.get("high"), dictionary.get("some"), dictionary.get("b"), dictionary.get("yes"));
			table.addInstance("2", dictionary.get("average"), dictionary.get("some"), dictionary.get("c"), dictionary.get("no"));
			table.addInstance("3", dictionary.get("average"), dictionary.get("alot"), dictionary.get("c"), dictionary.get("yes"));
			table.addInstance("4", dictionary.get("high"), dictionary.get("some"), dictionary.get("b"), dictionary.get("yes"));
			break;

		case 6:
			featureNames = new String[] { dictionary.get("type"), dictionary.get("shape"), dictionary.get("taste") };
			table = new DataTable(featureNames, dictionary.get("tryout"));
			table.addInstance("1", dictionary.get("sweet"), dictionary.get("lollipop"), dictionary.get("apple"), dictionary.get("yes"));
			table.addInstance("2", dictionary.get("sweet"), dictionary.get("candystick"), dictionary.get("orange"), dictionary.get("no"));
			table.addInstance("3", dictionary.get("sour"), dictionary.get("lollipop"), dictionary.get("cherry"), dictionary.get("yes"));
			table.addInstance("4", dictionary.get("sweet"), dictionary.get("lollipop"), dictionary.get("apple"), dictionary.get("yes"));
			break;

		case 7:
			featureNames = new String[] { dictionary.get("cream"), dictionary.get("banana"), dictionary.get("chocolate") };
			table = new DataTable(featureNames, dictionary.get("tasty"));
			table.addInstance("1", dictionary.get("yes"), dictionary.get("no"), dictionary.get("yes"), dictionary.get("yes"));
			table.addInstance("2", dictionary.get("yes"), dictionary.get("no"), dictionary.get("no"), dictionary.get("no"));
			table.addInstance("3", dictionary.get("yes"), dictionary.get("yes"), dictionary.get("yes"), dictionary.get("yes"));
			table.addInstance("4", dictionary.get("yes"), dictionary.get("yes"), dictionary.get("yes"), dictionary.get("yes"));
			table.addInstance("5", dictionary.get("no"), dictionary.get("no"), dictionary.get("yes"), dictionary.get("no"));
			table.addInstance("6", dictionary.get("yes"), dictionary.get("yes"), dictionary.get("no"), dictionary.get("no"));
			break;

		case 8:
			featureNames = new String[] { exampleTable[0][0], exampleTable[0][1], exampleTable[0][2] };
			table = new DataTable(featureNames, exampleTable[0][3]);
			for (int i = 1; i < exampleTable.length; i++) {
				table.addInstance(i + "", exampleTable[i][0], exampleTable[i][1], exampleTable[i][2], exampleTable[i][3]);
			}
			break;
		}

		valueMap = table.createValueMap();
	}


	private void setupCode(CodeText text, Lang language) throws Exception {

		BufferedReader reader = null;

		if (text == CodeText.PSEUDOCODE && language == Lang.ENGLISH) {
			reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/pseudocode_en");
//      reader = new BufferedReader(new FileReader("resources/id3/pseudocode_en"));
		} else if (text == CodeText.PSEUDOCODE && language == Lang.GERMAN) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/pseudocode_de");
//			reader = new BufferedReader(new FileReader("resources/id3/pseudocode_de"));
		} else if (text == CodeText.JAVA && language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/javacode_en");
//			reader = new BufferedReader(new FileReader("resources/id3/javacode_en"));
		} else {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/javacode_de");
//			reader = new BufferedReader(new FileReader("resources/id3/javacode_de"));
		}

		reader.lines().forEach((String line) -> {
			codeText.add(line);
		});

	}


	@SuppressWarnings("resource")
  private void setupTexts(Lang language, BiFunction<DataTable, String, Double> purityFunction, int exampleID) throws Exception {

		BufferedReader reader = null;

		if (language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/en_US");
//			reader = new BufferedReader(new FileReader("resources/id3/en_US"));
		} else {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/de_DE");
//			reader = new BufferedReader(new FileReader("resources/id3/de_DE"));
		}

		reader.lines().forEach((String line) -> {
			dictionary.put(line.split("#")[0], line.split("#")[1]);
		});


		if (language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/start_US");
//			reader = new BufferedReader(new FileReader("resources/id3/start_US"));
		} else {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/start_DE");
//			reader = new BufferedReader(new FileReader("resources/id3/start_DE"));
		}

		reader.lines().forEach((String line) -> {
			startText.add(line);
		});


		if (language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/end_US");
//			reader = new BufferedReader(new FileReader("resources/id3/end_US"));
		} else {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/end_DE");
//			reader = new BufferedReader(new FileReader("resources/id3/end_DE"));
		}

		reader.lines().forEach((String line) -> {
			endText.add(line);
		});


		if (language == Lang.ENGLISH) {
			if (exampleID == 8) {

				String firstFoundClass = exampleTable[1][3];
				boolean onlyALeaf = true;

				for (int i = 2; i < exampleTable.length; i++) {
					if (!exampleTable[i][3].equals(firstFoundClass)) {
						onlyALeaf = false;
					}
				}

				if (onlyALeaf) {
		      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/examples/example8L_EN");
//					reader = new BufferedReader(new FileReader("resources/id3/examples/example8L_EN"));
				} else {
		      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/examples/example8_EN");
//					reader = new BufferedReader(new FileReader("resources/id3/examples/example8_EN"));
				}


			} else {
				reader = new BufferedReader(new FileReader("resources/id3/examples/example" + exampleID + "_EN"));
			}
		} else {
			if (exampleID == 8) {

				String firstFoundClass = exampleTable[1][3];
				boolean onlyALeaf = true;

				for (int i = 2; i < exampleTable.length; i++) {
					if (!exampleTable[i][3].equals(firstFoundClass)) {
						onlyALeaf = false;
					}
				}

				if (onlyALeaf) {
		      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/examples/example8L_DE");
//					reader = new BufferedReader(new FileReader("resources/id3/examples/example8L_DE"));
				} else {
		      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/examples/example8_DE");
//					reader = new BufferedReader(new FileReader("resources/id3/examples/example8_DE"));
				}

			} else {
	      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/examples/example" + exampleID + "_DE");
//				reader = new BufferedReader(new FileReader("resources/id3/examples/example" + exampleID + "_DE"));
			}
		}

		LinkedList<String> exampleText = new LinkedList<String>();

		reader.lines().forEach((String line) -> {
			exampleText.add(line);
		});

		startText.addAll(6, exampleText.stream().limit(6).collect(Collectors.toList()));
		endText.addAll(1, exampleText.stream().skip(7).collect(Collectors.toList()));


		if (exampleID == 8) {

			List<String> insert = new LinkedList<String>();
			insert.addAll(Arrays.asList(exampleTable[0][3], exampleTable[0][0], exampleTable[0][1], exampleTable[0][2]));

			for (int i = 0; i < startText.size(); i++) {
				while (startText.get(i).contains("§")) {
					startText.set(i, startText.get(i).replaceFirst("§", insert.get(0)));
					insert.remove(0);
				}
			}
		}


		if (language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/steps_EN");
//			reader = new BufferedReader(new FileReader("resources/id3/steps_EN"));
		} else {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/steps_DE");
//			reader = new BufferedReader(new FileReader("resources/id3/steps_DE"));
		}

		reader.lines().forEach((String line) -> {
			dictionary.put(line.split("#")[0], line.split("#")[1]);
		});

		if (language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/questions_EN");
//			reader = new BufferedReader(new FileReader("resources/id3/questions_EN"));
		} else {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/questions_DE");
//			reader = new BufferedReader(new FileReader("resources/id3/questions_DE"));
		}

		reader.lines().forEach((String line) -> {
			dictionary.put(line.split("#")[0], line.split("#")[1]);
		});


		if (language == Lang.ENGLISH) {
			if (purityFunction == PurityFunctions.INFORMATION_GAIN) {
	      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/informationgain_EN");
//				reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/id3/informationgain_EN"), "UTF-8"));
			} else {
	      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/averageginiindex_EN");
//				reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/id3/averageginiindex_EN"), "UTF-8"));
			}
		} else {
			if (purityFunction == PurityFunctions.INFORMATION_GAIN) {
	      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/informationgain_DE");
//				reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/id3/informationgain_DE"), "UTF-8"));
			} else {
	      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/id3/averageginiindex_DE");
//				reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/id3/averageginiindex_DE"), "UTF-8"));
			}

		}

		reader.lines().forEach((String line) -> {
			functionText.add(line);
		});


	}


	public void executeGenerator(Language lang) throws Exception {

		// Language setup

		// CHANGED:
		this.lang = lang;
		// lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
		// "ID3", "Anja Kirchhöfer, Ben Kohr", 640, 480);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);

		lang.addQuestionGroup(new QuestionGroupModel("anotherDescentG", 5));
		lang.addQuestionGroup(new QuestionGroupModel("whichFeatureG", 5));
		lang.addQuestionGroup(new QuestionGroupModel("labelG", 5));
		lang.addQuestionGroup(new QuestionGroupModel("valuesG", 5));

		// Animalscript setup
		animalScriptSetup();

		title.hide();
		datatable.hide();
		code.hide();
		codeRect.hide();
		temporaryTableText.hide();
		temporaryTableFeatureText.hide();
		mainTableText.hide();
		infoRect.hide();
		functionExplanationBox.hide();
		functionExplanationRect.hide();

		for (algoanim.util.Node n : tree.getNodes()) {
			tree.hideNode(n, null, null);
		}


		for (Text t : edgeLabels) {
			t.hide();
		}

		for (Group g : markers) {
			g.hide();
		}

		for (Text t : nodeTitles) {
			t.hide();
		}

		lang.nextStep();

		title.setText(dictionary.get("title"), null, timing);
		title.show();
		InfoBox startBox = new InfoBox(lang, new Offset(-4, 3, datatable, AnimalScript.DIRECTION_SW), 20, "");
		startBox.setText(startText);

		mainTableText.setText(dictionary.get("maintabletext"), null, timing);
		mainTableText.show();
		datatable.show();

		lang.nextStep();
		startBox.hide();

		infoRect.show();
		code.show();
		codeRect.show();

		lang.nextStep(dictionary.get("startofid3"));

		code.highlight(0);

		infoBox.setText(Arrays.asList(dictionary.get("start1"), dictionary.get("start2")));

		lang.nextStep();

		code.unhighlight(0);

		// run algorithm
		generateWithAnimalScript(table);

		infoBox.setText(Arrays.asList(dictionary.get("endtree1"), dictionary.get("endtree2")));

		lang.nextStep(dictionary.get("endofid3"));

		getSpecificQuestion("explainsData");

		infoBox.setText(Arrays.asList(dictionary.get("end1"), dictionary.get("end2")));
		lang.nextStep();

		code.hide();
		codeRect.hide();

		infoBox.setText(Arrays.asList(""));
		infoRect.hide();

		InfoBox endBox = new InfoBox(lang, new Offset(-4, 3, datatable, AnimalScript.DIRECTION_SW), 20, "");
		endBox.setText(endText);

		lang.finalizeGeneration();


		// print Animalscript code

		// CHANGED:
		// System.out.println(lang);
		// BufferedWriter writer = new BufferedWriter(new
		// FileWriter("C:/Users/Business/Desktop/animalscriptfile.txt"));
		// writer.write(lang + "");
		// writer.close();
		// TODO remove later
		// moveInteractionFile();

	}


	public void execute() throws Exception {

		// Language setup
		lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "ID3", "Anja Kirchhöfer, Ben Kohr", 640, 480);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);

		lang.addQuestionGroup(new QuestionGroupModel("anotherDescentG", 5));
		lang.addQuestionGroup(new QuestionGroupModel("whichFeatureG", 5));
		lang.addQuestionGroup(new QuestionGroupModel("labelG", 5));
		lang.addQuestionGroup(new QuestionGroupModel("valuesG", 5));

		// Animalscript setup
		animalScriptSetup();

		title.hide();
		datatable.hide();
		code.hide();
		codeRect.hide();
		temporaryTableText.hide();
		temporaryTableFeatureText.hide();
		mainTableText.hide();
		infoRect.hide();
		functionExplanationBox.hide();
		functionExplanationRect.hide();

		for (algoanim.util.Node n : tree.getNodes()) {
			tree.hideNode(n, null, null);
		}


		for (Text t : edgeLabels) {
			t.hide();
		}

		for (Group g : markers) {
			g.hide();
		}

		for (Text t : nodeTitles) {
			t.hide();
		}

		lang.nextStep();

		title.setText(dictionary.get("title"), null, timing);
		title.show();
		InfoBox startBox = new InfoBox(lang, new Offset(-4, 3, datatable, AnimalScript.DIRECTION_SW), 20, "");
		startBox.setText(startText);

		mainTableText.setText(dictionary.get("maintabletext"), null, timing);
		mainTableText.show();
		datatable.show();

		lang.nextStep();
		startBox.hide();

		infoRect.show();
		code.show();
		codeRect.show();

		lang.nextStep(dictionary.get("startofid3"));

		code.highlight(0);

		infoBox.setText(Arrays.asList(dictionary.get("start1"), dictionary.get("start2")));

		lang.nextStep();

		code.unhighlight(0);

		// run algorithm
		generateWithAnimalScript(table);

		infoBox.setText(Arrays.asList(dictionary.get("endtree1"), dictionary.get("endtree2")));

		lang.nextStep(dictionary.get("endofid3"));

		getSpecificQuestion("explainsData");

		infoBox.setText(Arrays.asList(dictionary.get("end1"), dictionary.get("end2")));
		lang.nextStep();

		code.hide();
		codeRect.hide();

		infoBox.setText(Arrays.asList(""));
		infoRect.hide();

		InfoBox endBox = new InfoBox(lang, new Offset(-4, 3, datatable, AnimalScript.DIRECTION_SW), 20, "");
		endBox.setText(endText);

		lang.finalizeGeneration();

		// print Animalscript code
		System.out.println(lang);
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/Business/Desktop/animalscriptfile.txt"));
		writer.write(lang + "");
		writer.close();
		// TODO remove later
		moveInteractionFile();

	}


	private void moveInteractionFile() throws Exception {
		File folder = new File("C:/Users/Business/AlgoAnim/AlgoAnim/AlgoAnim");
		List<File> intDefFiles = Arrays.asList(folder.listFiles()).stream().filter((File f) -> {
			return f.getName().startsWith("intDef");
		}).sorted().collect(Collectors.toList());
		File newest = intDefFiles.get(intDefFiles.size() - 1);
		Files.move(Paths.get(newest.getAbsolutePath()), Paths.get("C:/Users/Business/Desktop/Animal/" + newest.getName()),
				StandardCopyOption.REPLACE_EXISTING);
		newest.delete();
	}


	private void animalScriptSetup() {

		// title
		TextProperties titleProperties = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		title = lang.newText(new Coordinates(20, 5), "", "title", null, titleProperties);

		// main table text
		TextProperties mainTableProperties = new TextProperties();
		mainTableProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 14));
		mainTableText = lang.newText(new Offset(0, 10, title, AnimalScript.DIRECTION_SW), dictionary.get("maintable"), "maintable", null,
				mainTableProperties);

		// data table
		datatable = constructDataTable("datatable", new Offset(0, 4, mainTableText, AnimalScript.DIRECTION_SW), table);
		currentDataTable = datatable;

		// temporary table text
		TextProperties temporaryTableProperties = new TextProperties();
		temporaryTableProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 14));
		temporaryTableText = lang.newText(new Offset(300, 0, mainTableText, AnimalScript.DIRECTION_BASELINE_START), dictionary.get("temptabletext"),
				"temporarytable", null, temporaryTableProperties);

		// temporary table feature text
		// TODO italic? Framework bug?
		TextProperties temporaryTablefeatureProperties = new TextProperties();
		temporaryTablefeatureProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.ITALIC, 14));
		temporaryTableFeatureText = lang.newText(new Offset(10, 0, temporaryTableText, AnimalScript.DIRECTION_BASELINE_END), "test",
				"temporarytablefeature", null, temporaryTablefeatureProperties);

		// source code
		SourceCodeProperties codeProperties = new SourceCodeProperties();
		codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, codeHighlightColor);
		codeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeColor);
		codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(codeFont, codeStyle, codeSize));

		code = lang.newSourceCode(new Offset(8, 6, datatable, AnimalScript.DIRECTION_SW), "code", null, codeProperties);

		code.addCodeLine(codeText.get(0), null, 0, null);
		code.addCodeLine(codeText.get(1), null, 0, null);
		code.addCodeLine(codeText.get(2), null, 1, null);
		code.addCodeLine(codeText.get(3), null, 2, null);
		code.addCodeLine(codeText.get(4), null, 2, null);
		code.addCodeLine(codeText.get(5), null, 2, null);
		code.addCodeLine(codeText.get(6), null, 0, null);
		code.addCodeLine(codeText.get(7), null, 1, null);
		code.addCodeLine(codeText.get(8), null, 1, null);
		code.addCodeLine(codeText.get(9), null, 1, null);
		code.addCodeLine(codeText.get(10), null, 1, null);
		code.addCodeLine(codeText.get(11), null, 1, null);
		code.addCodeLine(codeText.get(12), null, 1, null);
		code.addCodeLine(codeText.get(13), null, 2, null);
		code.addCodeLine(codeText.get(14), null, 1, null);
		code.addCodeLine(codeText.get(15), null, 1, null);
		code.addCodeLine(codeText.get(16), null, 1, null);
		code.addCodeLine(codeText.get(17), null, 0, null);

		// rectangle around code
		RectProperties coderectProperties = new RectProperties();
		coderectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		codeRect = lang.newRect(new Offset(-8, -2, code, AnimalScript.DIRECTION_NW), new Offset(8, 2, code, AnimalScript.DIRECTION_SE), "coderect",
				null, coderectProperties);


		// tree
		GraphProperties treeProperties = new GraphProperties();
		treeProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		treeProperties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		treeProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
		treeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.ORANGE);

		resultToVisualize = generate(table);
		edgeLabels = new LinkedList<Text>();
		markers = new LinkedList<Group>();
		nodeTitles = new LinkedList<Text>();
		tree = resultToVisualize.constructAnimalScriptTree(lang, treeProperties, edgeLabels, markers, nodeTitles, dictionary.get("newNode"),
				dictionary.get("newLeaf"), nodeColor, yesColor, noColor);

		// rectangle around info box
		RectProperties infoBoxProperties = new RectProperties();
		infoBoxProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, explanationBoxColor);
		infoBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		infoBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, explanationBoxFillColor);
		infoRect = lang.newRect(new Coordinates(650, 465), new Coordinates(1245, 500), "infoBoxRect", null, infoBoxProperties);

		// info box
		infoBox = new InfoBox(lang, new Coordinates(655, 450), 2, "");

		// rectangle around function explanation box
		RectProperties functionExplanationRectProperties = new RectProperties();
		functionExplanationRectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, explanationBoxColor);
		functionExplanationRectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		functionExplanationRectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, explanationBoxFillColor);
		functionExplanationRect = lang.newRect(new Coordinates(605, 70), new Coordinates(1290, 460), "functionExplanationRect", null,
				functionExplanationRectProperties);


		// function explanation box
		functionExplanationBox = new InfoBox(lang, new Coordinates(610, 60), 50, "");


	}


	private StringMatrix constructDataTable(String name, Offset offset, DataTable table) {
		MatrixProperties tableProperties = new MatrixProperties();
		tableProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		tableProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, tableElemColor);
		tableProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.ORANGE);
		tableProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		tableProperties.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		tableProperties.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, tableBorderColor);
		tableProperties.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, Color.BLACK);
		tableProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(tableFont, Font.PLAIN, 10));
		StringMatrix datatable = lang.newStringMatrix(offset, table.dataAsStringArray(), name, null, tableProperties);

		for (int i = 0; i < table.getNumberOfFeatures() + 2; i++) {
			datatable.setGridHighlightFillColor(0, i, new Color(200, 200, 200), null, null);
			datatable.highlightCell(0, i, null, null);
		}

		for (int i = 1; i < table.getNumberOfInstances() + 1; i++) {
			datatable.setGridHighlightFillColor(i, 0, new Color(230, 230, 230), null, null);
			datatable.highlightCell(i, 0, null, null);
		}

		return datatable;
	}


	/* ID3 ALGORITHM WITHOUT ANIMALSCRIPT */
	public Tree generate(DataTable table) {

		boolean onlyOneClass = table.singleClassLeft();

		if (onlyOneClass) {
			Tree treeWithOneNode = new Tree();
			String className = table.getFirstFoundClass();
			treeWithOneNode.addRootNode(new Node(className, true));

			return treeWithOneNode;
		}

		String feature = PurityFunctions.getMostInformativeFeature(table, purityFunction);
		LinkedList<String> values = table.getValues(feature);

		LinkedList<Tree> subTrees = new LinkedList<Tree>();

		Tree combination = new Tree();
		Node featureNode = new Node(feature, false);

		for (String value : values) {

			DataTable reducedTable = table.reduce(feature, value);

			Tree subTree = generate(reducedTable);

			subTrees.add(subTree);
		}

		combination.addSubTrees(featureNode, subTrees, values);

		return combination;
	}


	public boolean validate() {

		try {
			validateHelper(table);
		} catch (RecursionTooDeepException rtde) {
			return false;
		}

		return true;

	}


	private Tree validateHelper(DataTable table) throws RecursionTooDeepException {

		if (recCounter > 2) {
			throw new RecursionTooDeepException();
		}

		boolean onlyOneClass = table.singleClassLeft();

		if (onlyOneClass) {
			Tree treeWithOneNode = new Tree();
			String className = table.getFirstFoundClass();
			treeWithOneNode.addRootNode(new Node(className, true));

			recCounter--;
			return treeWithOneNode;
		}

		String feature = PurityFunctions.getMostInformativeFeature(table, purityFunction);
		LinkedList<String> values = table.getValues(feature);

		LinkedList<Tree> subTrees = new LinkedList<Tree>();

		Tree combination = new Tree();
		Node featureNode = new Node(feature, false);

		for (String value : values) {

			DataTable reducedTable = table.reduce(feature, value);

			recCounter++;
			Tree subTree = validateHelper(reducedTable);

			subTrees.add(subTree);
		}

		combination.addSubTrees(featureNode, subTrees, values);

		recCounter--;
		return combination;
	}


	/* ID3 ALGORITHM WITH ANIMALSCRIPT */
	private Tree generateWithAnimalScript(DataTable table) {

		// ------------------------------------------------
		boolean onlyOneClass = table.singleClassLeft();
		// ------------------------------------------------

		code.highlight(2);
		infoBox.setText(Arrays.asList(dictionary.get("checkedoneclass1"), dictionary.get("checkedoneclass2")));

		lang.nextStep();

		getSpecificQuestion("anotherDescent", !onlyOneClass);

		LinkedList<String> classOccurences = table.getOrderedClassOccurences();
		int numFeatures = table.getNumberOfFeatures();

		for (int i = 0; i < table.getNumberOfInstances(); i++) {
			if (classOccurences.get(i).equals(dictionary.get("yes"))) {
				currentDataTable.setGridHighlightFillColor(i + 1, numFeatures + 1, yesColor, null, null);
				currentDataTable.highlightCell(i + 1, numFeatures + 1, null, null);
			} else {
				currentDataTable.setGridHighlightFillColor(i + 1, numFeatures + 1, noColor, null, null);
				currentDataTable.highlightCell(i + 1, numFeatures + 1, null, null);
			}
		}

		if (onlyOneClass) {
			infoBox.setText(
					Arrays.asList(dictionary.get("onlyoneclass1").replaceFirst("§", table.getFirstFoundClass()), dictionary.get("onlyoneclass2")));
		} else {
			infoBox.setText(Arrays.asList(dictionary.get("notoneclass1"), dictionary.get("notoneclass2")));
		}


		lang.nextStep();

		code.unhighlight(2);

		// ---------------------------------------------------
		if (onlyOneClass) {
			Tree treeWithOneNode = new Tree();
			// ------------------------------------------------

			code.highlight(3);
			infoBox.setText(Arrays.asList(dictionary.get("onlyoneclassrecend1").replaceFirst("§", table.getFirstFoundClass()),
					dictionary.get("onlyoneclassrecend2")));

			lang.nextStep();

			String label = tree.getNodeLabel(nodeIdentifier);
			String emptyLabel = label.replaceAll(".", " ");
			tree.setNodeLabel(nodeIdentifier, emptyLabel, null, null);
			tree.showNode(nodeIdentifier, null, null);
			markers.get(nodeIdentifier).show();

			for (int i = 0; i < tree.getNodes().length; i++) {
				tree.hideEdge(i, nodeIdentifier, null, null);
			}


			lang.nextStep();

			code.unhighlight(3);

			// ---------------------------------------------------
			String className = table.getFirstFoundClass();
			treeWithOneNode.addRootNode(new Node(className, true));
			// ---------------------------------------------------

			code.highlight(4);

			lang.nextStep();

			getSpecificQuestion("label", true, className);

			tree.setNodeLabel(nodeIdentifier, label, null, null);
			tree.highlightNode(nodeIdentifier, null, null);

			int node = nodeIdentifier;

			nodeIdentifier++;

			lang.nextStep();

			code.unhighlight(4);

			code.highlight(5);
			infoBox.setText(Arrays.asList(dictionary.get("retleaf1"), dictionary.get("retleaf2")));


			lang.nextStep(dictionary.get("returnleaf") + " " + label);

			pickRandomQuestion();

			code.unhighlight(5);

			if (temporaryDatatable != null && temporaryDatatable != datatable) {
				temporaryDatatable.hide();
				temporaryTableText.hide();
				temporaryTableFeatureText.hide();
			}

			recursionCounter--;

			if (recursionCounter == -1) {
				for (int i = 1; i < table.getNumberOfInstances() + 1; i++) {
					for (int j = 1; j < table.getNumberOfFeatures() + 2; j++) {
						datatable.unhighlightCell(i, j, null, null);
					}
				}
			}

			markers.get(node).hide();

			// ---------------------------------------------------
			return treeWithOneNode;
			// ---------------------------------------------------
		}


		// TODO Visualize feature selection criterion
		// ---------------------------------------------------
		String feature = PurityFunctions.getMostInformativeFeature(table, purityFunction);
		// ---------------------------------------------------

		code.highlight(7);

		lang.nextStep();

		infoBox.setText(Arrays.asList(dictionary.get("measure1"), dictionary.get("measure2").replaceFirst("§",
				((purityFunction == PurityFunctions.INFORMATION_GAIN) ? "Information Gain" : "Average Gini Index"))));

		lang.nextStep();

		getSpecificQuestion("whichFeature", feature, table.getFeatureNames());

		// ----

		// if (!linksUsed) {

		// lang.nextStep();

		// lang.addDocumentationLink(new
		// HtmlDocumentationModel("informationgain",
		// "http://www.learnbymarketing.com/481/decision-tree-flavors-gini-info-gain/"));//"https://abhyast.files.wordpress.com/2015/01/image6.png"));

		// linksUsed = true;
		// }

		// ----

		List<String> currentFunctionText = new LinkedList<String>();

		if (purityFunction == PurityFunctions.INFORMATION_GAIN) {

			LinkedList<String> result = PurityFunctions.getList();
			LinkedList<String> toDisplay = new LinkedList<String>();
			for (int a = 0; a < result.size(); a++) {
				String newString = null;

				if (result.get(a).startsWith("_")) {
					newString = result.get(a).substring(1);
					toDisplay.add(newString);
				} else if (!result.get(a).contains(".")) {
					String s = "";
					do {

						s = s + "    - (" + result.get(a + 1) + "/" + result.get(a + 2) + ") * " + result.get(a + 3);
						a = a + 4;

					} while (!result.get(a).contains("."));
					toDisplay.add(s);
					toDisplay.add(result.get(a));
				} else {
					toDisplay.add(result.get(a));
				}

			}

			currentFunctionText.addAll(functionText);
			for (String s : toDisplay) {
				for (int i = 0; i < currentFunctionText.size(); i++) {
					String line = currentFunctionText.get(i);
					if (line.contains("§")) {
						currentFunctionText.set(i, line.replaceFirst("§", s));
						break;
					}
				}
			}


		} else {

			LinkedList<String> result = PurityFunctions.getList();
			LinkedList<String> toDisplay = new LinkedList<String>();
			for (int a = 0; a < result.size(); a++) {
				String newString = null;

				if (result.get(a).startsWith("_")) {
					newString = result.get(a).substring(1);
					toDisplay.add(newString);
				} else if (!result.get(a).matches(".*[0-9]+.*")) {
					String s = "";
					boolean firstTime = true;
					do {

						s = s + ((firstTime) ? "(" : "    + (") + result.get(a + 1) + "/" + result.get(a + 2) + ") * " + result.get(a + 3);
						a = a + 4;
						firstTime = false;

					} while (!result.get(a).matches(".*[0-9]+.*"));
					toDisplay.add(s);
					toDisplay.add(result.get(a));
				} else {
					toDisplay.add(result.get(a));
				}

			}

			currentFunctionText.addAll(functionText);
			for (String s : toDisplay) {
				for (int i = 0; i < currentFunctionText.size(); i++) {
					String line = currentFunctionText.get(i);
					if (line.contains("§")) {
						currentFunctionText.set(i, line.replaceFirst("§", s));
						break;
					}
				}
			}


		}


		functionExplanationRect.show();
		functionExplanationBox.setText(currentFunctionText);
		functionExplanationBox.show();
		tree.hide();

		lang.nextStep();

		String s1;
		String s2;

		if (purityFunction == PurityFunctions.INFORMATION_GAIN) {
			s1 = dictionary.get("highest");
			s2 = "Information Gain";
		} else {
			s1 = dictionary.get("lowest");
			s2 = "Average Gini Index";
		}


		infoBox.setText(Arrays.asList(dictionary.get("featurechosen1").replaceFirst("§", feature).replaceFirst("§", s1).replaceFirst("§", s2),
				dictionary.get("featurechosen2")));

		lang.nextStep();

		tree.show();
		functionExplanationBox.hide();
		functionExplanationRect.hide();

		lang.nextStep();

		int featureNum = table.getFeatureNumber(feature);

		for (int i = 1; i < table.getNumberOfInstances() + 1; i++) {
			currentDataTable.setGridHighlightFillColor(i, featureNum + 1, tableHighlightColor, null, null);
			currentDataTable.highlightCell(i, featureNum + 1, null, null);
		}


		lang.nextStep();

		pickRandomQuestion();

		code.unhighlight(7);

		// ---------------------------------------------------
		LinkedList<String> values = table.getValues(feature);
		LinkedList<Tree> subTrees = new LinkedList<Tree>();

		Tree combination = new Tree();
		Node featureNode = new Node(feature, false);
		// ---------------------------------------------------

		code.highlight(9);

		getSpecificQuestion("label", false, feature);

		infoBox.setText(Arrays.asList(dictionary.get("createnode1").replaceFirst("§", feature), dictionary.get("createnode2")));

		lang.nextStep();


		String label = tree.getNodeLabel(nodeIdentifier);
		String emptyLabel = label.replaceAll(".", " ");
		tree.setNodeLabel(nodeIdentifier, emptyLabel, null, null);
		tree.showNode(nodeIdentifier, null, null);
		markers.get(nodeIdentifier).show();

		int parentNode = nodeIdentifier;

		for (int i = 0; i < tree.getNodes().length; i++) {
			tree.hideEdge(i, nodeIdentifier, null, null);
		}

		lang.nextStep();

		code.unhighlight(9);
		code.highlight(10);

		lang.nextStep();

		tree.setNodeLabel(nodeIdentifier, label, null, null);
		nodeIdentifier++;

		lang.nextStep();
		code.unhighlight(10);
		code.highlight(12);
		infoBox.setText(
				Arrays.asList(dictionary.get("allvalues1").replaceFirst("§", feature), dictionary.get("allvalues2").replaceFirst("§", values + "")));

		lang.nextStep();

		getSpecificQuestion("values", values.toArray(new String[values.size()]), feature);

		if (temporaryDatatable != null && temporaryDatatable != datatable) {
			temporaryDatatable.hide();
			temporaryTableText.hide();
			temporaryTableFeatureText.hide();
		}

		// ---------------------------------------------------
		for (String value : values) {
			// ---------------------------------------------------

			infoBox.setText(Arrays.asList(dictionary.get("allvaluescurrent1").replaceFirst("§", feature),
					dictionary.get("allvaluescurrent2").replaceFirst("§", values + "").replaceFirst("§", value)));

			lang.nextStep();

			pickRandomQuestion();

			code.unhighlight(12);
			code.highlight(13);

			infoBox.setText(
					Arrays.asList(dictionary.get("reccall1").replaceFirst("§", feature).replaceFirst("§", value), dictionary.get("reccall2")));
			lang.nextStep();

			recursionCounter++;
			// ---------------------------------------------------
			DataTable reducedTable = table.reduce(feature, value);
			// ---------------------------------------------------

			dataTableCounter++;

			if (dataTableCounter == 1) {
				for (int i = 1; i < table.getNumberOfInstances() + 1; i++) {
					for (int j = 1; j < table.getNumberOfFeatures() + 2; j++) {
						datatable.unhighlightCell(i, j, null, null);
					}
				}
			}


			temporaryDatatable = constructDataTable("reducedTable" + dataTableCounter,
					new Offset(0, 7, temporaryTableText, AnimalScript.DIRECTION_SW), reducedTable);
			temporaryTableText.show();
			temporaryTableFeatureText.setText(feature + ": " + value, null, timing);
			temporaryTableFeatureText.show();

			nodeTitles.get(nodeIdentifier).setText(feature + ": " + value, null, null);
			nodeTitles.get(nodeIdentifier).show();

			currentDataTable = temporaryDatatable;

			infoBox.setText(Arrays.asList(dictionary.get("reccreatetemptable1").replaceFirst("§", feature),
					dictionary.get("reccreatetemptable2").replaceFirst("§", value)));

			lang.nextStep(dictionary.get("createsubtree") + " " + feature + ": " + value);
			code.unhighlight(13);

			markers.get(parentNode).hide();


			// ---------------------------------------------------
			Tree subTree = generateWithAnimalScript(reducedTable);
			subTrees.add(subTree);
			// ---------------------------------------------------

			markers.get(parentNode).show();
		}

		// ---------------------------------------------------
		combination.addSubTrees(featureNode, subTrees, values);
		// ---------------------------------------------------

		code.highlight(15);

		infoBox.setText(
				Arrays.asList(dictionary.get("attach1").replaceFirst("§", values + ""), dictionary.get("attach2").replaceFirst("§", feature)));

		lang.nextStep();

		for (int i = 0; i < tree.getNodes().length; i++) {
			tree.showEdge(parentNode, i, null, null);
		}

		LinkedList<Integer> indices = resultToVisualize.getEdgeIndicesForParentNodeID(parentNode);

		for (int i : indices) {
			edgeLabels.get(i).show();
			nodeTitles.get(resultToVisualize.getNodes().indexOf(resultToVisualize.getEdges().get(i).getTo())).hide();
		}


		lang.nextStep(dictionary.get("createdsubtree") + " " + label);

		pickRandomQuestion();

		code.unhighlight(15);

		code.highlight(16);

		lang.nextStep();
		code.unhighlight(16);

		recursionCounter--;

		markers.get(parentNode).hide();

		// ---------------------------------------------------
		return combination;
		// ---------------------------------------------------
	}


	private void constructQuestionNumbersList() {

		for (int i = 0; i < NUM_QUESTIONS; i++) {
			questionNumbers.add(i);
		}

	}


	private void pickRandomQuestion() {

		boolean newQuestion = (Math.random() < probRandom);

		if (!newQuestion || questionNumbers.isEmpty()) {
			return;
		}

		int index = new Random().nextInt(questionNumbers.size());
		int questionNumber = questionNumbers.get(index);
		questionNumbers.remove(index);

		switch (questionNumber) {
		case 0:
			MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("differenceNodeLeaf");
			q1.setPrompt(dictionary.get("differenceNodeLeaf1"));
			String correct1 = dictionary.get("differenceNodeLeaf2");
			q1.addAnswer(dictionary.get("differenceNodeLeaf3"), 0, dictionary.get("differenceNodeLeaf4") + " " + correct1);
			q1.addAnswer(correct1, 1, dictionary.get("differenceNodeLeaf5"));
			q1.addAnswer(dictionary.get("differenceNodeLeaf6"), 0, dictionary.get("differenceNodeLeaf7") + " " + correct1);
			lang.addMCQuestion(q1);
			break;
		case 1:
			TrueFalseQuestionModel q2 = new TrueFalseQuestionModel("recursiveDepth", true, 1);
			q2.setPrompt(dictionary.get("recursiveDepth1"));
			// TODO set feedback not working
			q2.setFeedbackForAnswer(true, dictionary.get("recursiveDepth2"));
			q2.setFeedbackForAnswer(false, dictionary.get("recursiveDepth3"));
			lang.addTFQuestion(q2);
			break;
		case 2:
			MultipleSelectionQuestionModel q3 = new MultipleSelectionQuestionModel("partsOfDecisionTree");
			q3.setPrompt(dictionary.get("partsOfDecisionTree1"));
			q3.addAnswer(dictionary.get("partsOfDecisionTree2"), 0, dictionary.get("partsOfDecisionTree3"));
			q3.addAnswer(dictionary.get("partsOfDecisionTree4"), 1, dictionary.get("partsOfDecisionTree5"));
			q3.addAnswer(dictionary.get("partsOfDecisionTree6"), 0, dictionary.get("partsOfDecisionTree7"));
			q3.addAnswer(dictionary.get("partsOfDecisionTree8"), 1, dictionary.get("partsOfDecisionTree9"));
			q3.addAnswer(dictionary.get("partsOfDecisionTree10"), 1, dictionary.get("partsOfDecisionTree11"));

			/*
			 * q3.addAnswer("Node", 0,
			 * "Node: Wrong, a tree does not have to have a node; it may consist of a labeled leaf only."
			 * ); q3.addAnswer("Root", 1, "Root: Right, each tree has a root.");
			 * q3.addAnswer("Edge", 0,
			 * "Edge: Wrong, a tree does not have to have an edge; with only one leaf, there is no edge."
			 * ); q3.addAnswer("A label", 1,
			 * "A label: Right, a tree has at least one label.");
			 * q3.addAnswer("Leaf", 1,
			 * "Leaf: Right, a tree has at least one leaf.");
			 */
			lang.addMSQuestion(q3);
			break;
		case 3:
			FillInBlanksQuestionModel q4 = new FillInBlanksQuestionModel("howManyColumns");
			q4.setPrompt(dictionary.get("howManyColumns1"));
			q4.addAnswer("1", 1, dictionary.get("howManyColumns2"));
			lang.addFIBQuestion(q4);
			break;
		case 4:
			TrueFalseQuestionModel q5 = new TrueFalseQuestionModel("notTerminate", true, 1);
			q5.setPrompt(dictionary.get("notTerminate1"));
			// TODO set feedback not working
			q5.setFeedbackForAnswer(true, dictionary.get("notTerminate2"));
			q5.setFeedbackForAnswer(false, dictionary.get("notTerminate3"));
			lang.addTFQuestion(q5);
			break;
		case 5:
			TrueFalseQuestionModel q6 = new TrueFalseQuestionModel("subTrees", false, 1);
			q6.setPrompt(dictionary.get("subTrees1"));
			// TODO set feedback not working
			String right6 = dictionary.get("subTrees2");
			q6.setFeedbackForAnswer(true, dictionary.get("subTrees3") + " " + right6);
			q6.setFeedbackForAnswer(false, dictionary.get("subTrees4") + " " + right6);
			lang.addTFQuestion(q6);
			break;
		}
		lang.nextStep();
	}

	/*
	 * 
	 * MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("");
	 * q2.setGroupID(""); String correct2 = ""; q2.setPrompt("");
	 * q2.addAnswer("", 0, ""); q2.addAnswer("", 1, ""); q2.addAnswer("", 0,
	 * ""); lang.addMCQuestion(q2);
	 * 
	 */


	private void getSpecificQuestion(String name, Object... params) {

		boolean newQuestion = (Math.random() < probSpecific);

		if (!newQuestion) {
			return;
		}

		// TODO add specific questions and link them to question groups

		switch (name) {

		case "anotherDescent":
			boolean descent = (Boolean) params[0];
			TrueFalseQuestionModel q1 = new TrueFalseQuestionModel("anotherDescent" + specificQuestionHelperCounter, descent, 1);
			q1.setGroupID("anotherDescentG");
			q1.setPrompt(dictionary.get("anotherDescent1"));
			String right1;
			if (descent) {
				right1 = dictionary.get("anotherDescent2");
				q1.setFeedbackForAnswer(true, dictionary.get("anotherDescent3") + " " + right1);
				q1.setFeedbackForAnswer(false, dictionary.get("anotherDescent4") + " " + right1);
			} else {
				right1 = dictionary.get("anotherDescent5");
				q1.setFeedbackForAnswer(true, dictionary.get("anotherDescent6") + " " + right1);
				q1.setFeedbackForAnswer(false, dictionary.get("anotherDescent7") + " " + right1);
			}
			lang.addTFQuestion(q1);
			break;

		case "whichFeature":
			String feature = (String) params[0];
			String[] features = (String[]) params[1];
			MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("whichFeature" + specificQuestionHelperCounter);
			q2.setGroupID("whichFeatureG");
			q2.setPrompt(dictionary.get("whichFeature1"));
			q2.addAnswer(features[0], features[0].equals(feature) ? 1 : 0,
					features[0].equals(feature) ? dictionary.get("whichFeature2") : dictionary.get("whichFeature3").replaceFirst("§", feature));
			q2.addAnswer(features[1], features[1].equals(feature) ? 1 : 0,
					features[1].equals(feature) ? dictionary.get("whichFeature2") : dictionary.get("whichFeature3").replaceFirst("§", feature));
			q2.addAnswer(features[2], features[2].equals(feature) ? 1 : 0,
					features[2].equals(feature) ? dictionary.get("whichFeature2") : dictionary.get("whichFeature3").replaceFirst("§", feature));
			lang.addMCQuestion(q2);
			break;

		case "explainsData":
			TrueFalseQuestionModel q3 = new TrueFalseQuestionModel("explainsData", true, 1);
			q3.setPrompt(dictionary.get("explainsData1"));
			String right3 = dictionary.get("explainsData2");
			q3.setFeedbackForAnswer(true, dictionary.get("explainsData3") + " " + right3);
			q3.setFeedbackForAnswer(false, dictionary.get("explainsData4") + " " + right3);
			lang.addTFQuestion(q3);
			break;

		case "label":
			boolean leaf = (Boolean) params[0];
			String label = (String) params[1];
			if (!leaf) {
				lang.nextStep();
			}
			FillInBlanksQuestionModel q4 = new FillInBlanksQuestionModel("label" + specificQuestionHelperCounter);
			q4.setGroupID("labelG");
			q4.setPrompt(dictionary.get("label1").replaceFirst("§", ((leaf) ? dictionary.get("label00") : dictionary.get("label0"))));
			q4.addAnswer(label.toLowerCase(), 1, dictionary.get("label2"));
			lang.addFIBQuestion(q4);
			break;

		case "values":
			String[] values = (String[]) params[0];
			String theFeature = (String) params[1];

			String[] allValues;
			allValues = valueMap.get(theFeature).toArray(new String[valueMap.get(theFeature).size()]);

			if (values.length == allValues.length) {
				return;
			}

			MultipleSelectionQuestionModel q5 = new MultipleSelectionQuestionModel("values" + specificQuestionHelperCounter);
			q5.setGroupID("valuesG");
			q5.setPrompt(dictionary.get("values1").replaceFirst("§", theFeature).replaceFirst("§", "" + values.length));
			for (String s : allValues) {
				q5.addAnswer(s, ((Arrays.asList(values).contains(s) ? 1 : 0)),
						s + ": " + ((Arrays.asList(values).contains(s) ? dictionary.get("values2") : dictionary.get("values3"))));
			}
			lang.addMSQuestion(q5);
			break;

		}

		specificQuestionHelperCounter++;

		lang.nextStep();

	}

}
