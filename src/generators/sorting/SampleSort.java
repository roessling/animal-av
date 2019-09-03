/*
 * SampleSort.java
 * Laura Hofmann, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.QueueProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;

@SuppressWarnings("unused")
public class SampleSort implements Generator {
	private Language lang;
	private int intP;
	private int[] intArrayData;
	private ArrayProperties arrayProps;
  private SourceCodeProperties scProps;
	private int intK;
	// next are put in by me
	private Text header;
	private Text subheader;
	private Rect headerRect;
	private Rect shadowRect;

	public void init() {
		lang = new AnimalScript("SampleSort by Frazer and McKellar", "Laura Hofmann", 800, 600);
		this.lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// enable questions
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		intP = (Integer) primitives.get("intP");
		intArrayData = (int[]) primitives.get("intArrayData");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
		intK = (Integer) primitives.get("intK");

		try {
			this.setup(intArrayData, intK, intP, this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// finalise questions
		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return "SampleSort by Frazer and McKellar";
	}

	public String getAlgorithmName() {
		return "SampleSort";
	}

	public String getAnimationAuthor() {
		return "Laura Hofmann";
	}

	public String getDescription() {
		return "SampleSort (as by W. D. Frazer and A. C. McKellar) takes a given Array D of unsorted Integers and sorts it by stuffing the data from D into buckets (more on these below) sorting all buckets separately and concatenating them."
				+ "\n"
				+ "For the buckets p gives us the number of buckets we want, and k is the oversampling factor. We need p-1 borders for p buckets. As the first bucket collects all elements in the interval (-inf,border 1]  and last buckets holds (border p-1, inf]. "
				+ "\n"
				+ "We collect k times as many samples as we need bucket borders (that is k*(p-1) samples) and pick our borders from these to have a better representation of the distribution of data. We then sort the borders so that the first bucket holds the smallest elements from D and so on. "
				+ "\n"
				+ "After that we separate the data into our buckets defined by our borders and sort each bucket. For that we can use sampleSort again or any other sorting algorithm (Frazer and MCKellar use Quicksort from there on)."
				+ "\n" + "The sorted buckets are then concatenated and voila: D has been sorted.";
	}

	public String getCodeExample() {
		return "/**" + "\n" + " * sample sort sorts an array by picking random boundaries for buckets and" + "\n"
				+ " * sorting the array data into the buckets. The buckets are then sorted and" + "\n"
				+ " * concatenated to return a sorted array" + "\n" + " * " + "\n" + " * @param data" + "\n"
				+ " *            array to be sorted" + "\n" + " * @param k" + "\n" + " *            oversampling factor"
				+ "\n" + " * @param p" + "\n" + " *            number of buckets" + "\n" + " * @return sorted array"
				+ "\n" + " */" + "\n" + "public static int[] sampleSort(int[] data, int k, int p) {" + "\n"
				+ "    // select Samples" + "\n" + "    int[] s = selectSamples(data, k, p);" + "\n"
				+ "    // generate buckets" + "\n" + "    List<List<Integer>> buckets = generateBuckets(p);" + "\n"
				+ "    // seperate data into buckets" + "\n"
				+ "    buckets = seperateDataIntoBuckets(data, buckets, s);" + "\n"
				+ "    // sort and concatenate non-empty buckets" + "\n"
				+ "    return sortAndConcatenateBuckets(buckets, data.length);" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setup(int[] data, int k, int p, SampleSort s) throws InterruptedException {
		// show the header with a heading surrounded by a rectangle
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 30), "SampleSort Algorithmus", "header", null, headerProps);
		TextProperties subheaderProps = new TextProperties();
		subheaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
		subheader = lang.newText(new Offset(0, 5, "header", "SW"), "as by W. D. Frazer and A. C. McKellar       ",
				"subheader", null, subheaderProps);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0x00c8ff"));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "subheader", "SE"), "hRect", null, rectProps);
		// rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		shadowRect = lang.newRect(new Offset(5, 5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "hRect", "SE"), "hRect", null, rectProps);
		lang.nextStep("setup: show header -> first step!");

		// write intro text
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		lang.newText(new Coordinates(10, 100),
				"SampleSort (as by W. D. Frazer and A. C. McKellar) takes a given Array D of unsorted Integers",
				"description1", null, textProps);
		lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
				"and sorts it by stuffing the data from D into buckets (more on these below) sorting all",
				"description2", null, textProps);
		lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
				"buckets separately and concatenating them.", "description3", null, textProps);
		lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
				"For the buckets p gives us the number of buckets we want, and k is the oversampling factor.",
				"description4", null, textProps);
		lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
				"We need p-1 borders for p buckets. As the first bucket collects all elements in the interval",
				"description5", null, textProps);
		lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
				"(-inf,border 1]  and last buckets holds (border p-1, inf].", "description6", null, textProps);
		lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
				"We collect k times as many samples as we need bucket borders (that is k*(p-1) samples)",
				"description7", null, textProps);
		lang.newText(new Offset(0, 25, "description7", AnimalScript.DIRECTION_NW),
				"and pick our borders from these to have a better representation of the distribution of data. ",
				"description8", null, textProps);
		lang.newText(new Offset(0, 25, "description8", AnimalScript.DIRECTION_NW),
				"We then sort the borders so that the first bucket holds the smallest elements from D and so on. ",
				"description9", null, textProps);
		lang.newText(new Offset(0, 25, "description9", AnimalScript.DIRECTION_NW),
				"After that we separate the data into our buckets defined by our borders and sort each bucket.",
				"description10", null, textProps);
		lang.newText(new Offset(0, 25, "description10", AnimalScript.DIRECTION_NW),
				"For that we can use sampleSort again or any other sorting algorithm (Frazer and MCKellar ",
				"description11", null, textProps);
		lang.newText(new Offset(0, 25, "description11", AnimalScript.DIRECTION_NW), "use Quicksort from there on).",
				"description12", null, textProps);
		lang.newText(new Offset(0, 25, "description12", AnimalScript.DIRECTION_NW),
				"The sorted buckets are then concatenated and voila: D has been sorted.", "description13", null,
				textProps);
		lang.nextStep();

		// cleanup for next step
		lang.hideAllPrimitives();
		header.show();
		subheader.show();
		headerRect.show();
		shadowRect.show();

		// 1. Frage
		MultipleChoiceQuestionModel whyOversampling = new MultipleChoiceQuestionModel("oversampling");
		whyOversampling
				.setPrompt("Why do we sample so many datapoints before choosing the splinters as the bucket borders?");
		whyOversampling.addAnswer("just to make it more complicated", 0, " -> this is not right");
		whyOversampling.addAnswer("to get a better representation of data distribution", 1, " -> correct");
		whyOversampling.addAnswer("to determine the number of buckets", 0,
				" -> not right, you determined the numer of buckets in the beginning. Remember?");

		lang.addMCQuestion(whyOversampling);
		/////////////////

		////////////////////////////////////////////////////////////////////////////////////

		// Create SourceCode
		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode mainCode = lang.newSourceCode(new Coordinates(20, 100), "sourceCode", null, scProps);

		// Add the lines to the SourceCode object:(Line_Text, name, indentation,
		// display, delay)
		mainCode.addCodeLine("public static int[] sampleSort(int[] data, int k, int p) {", null, 0, null); // 0
		mainCode.addCodeLine("int[] s = selectSamples(data, k, p);", null, 1, null);// 1
		mainCode.addCodeLine("	List<List<Integer>> buckets = generateBuckets(p);", null, 1, null); // 2
		mainCode.addCodeLine("buckets = seperateDataIntoBuckets(data, buckets, s);", null, 1, null); // 3
		mainCode.addCodeLine("return sortAndConcatenateBuckets(buckets, data.length);", null, 1, null); // 4
		mainCode.addCodeLine("}", null, 0, null); // 6

		// make blue square behind main code
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xb2eeff"));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect codeRect = lang.newRect(new Offset(-5, -5, "sourceCode", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "sourceCode", "SE"), "hRect", null, rectProps);

		lang.nextStep();

		mainCode.highlight(0);
		// first, set the visual properties (somewhat similar to CSS)
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

		// display input data
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text dataDesc = lang.newText(new Coordinates(600, 100), "Data: ", "dataDesc", null, textProps);
		IntArray dataArray = lang.newIntArray(new Offset(100, 0, "dataDesc", AnimalScript.DIRECTION_NW), data,
				"intArray", null, arrayProps);
		Text kText = lang.newText(new Offset(0, 50, "dataDesc", AnimalScript.DIRECTION_NW),
				"Oversampling factor k = " + k, "kText", null, textProps);
		Text pText = lang.newText(new Offset(0, 25, "kText", AnimalScript.DIRECTION_NW), "Number of Buckets p = " + p,
				"pText", null, textProps);
		// start a new step after the array was created
		lang.nextStep("setup: show code and input data begin actual execution now:");

		try {
			// Start quicksort
			s.sampleSort(dataArray, mainCode, k, p);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		lang.hideAllPrimitives();
		header.show();
		subheader.show();
		headerRect.show();
		shadowRect.show();

		// finale thing to show
		lang.newText(new Coordinates(10, 100),
				"One clearly sees that sample sort is a glorified version of bucket sort. ", "description1", null,
				textProps);
		lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
				"The interesting thing about sample sort is the way the buckets borders are", "description2", null,
				textProps);
		lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW), "selected in the beginning.",
				"description3", null, textProps);
		lang.nextStep("all finished");
		
	}

	/**
	 * sample sort sorts an array by picking random boundaries for buckets and
	 * sorting the array data into the buckets. The buckets are then sorted and
	 * concatenated to return a sorted array
	 * 
	 * @param data
	 *            array to be sorted
	 * @param k
	 *            oversampling factor
	 * @param p
	 *            number of buckets
	 * @return sorted array
	 * @throws InterruptedException
	 */
	public IntArray sampleSort(IntArray data, SourceCode mainCode, int k, int p) throws InterruptedException {
		mainCode.toggleHighlight(0, 1);
		IntArray splitters = selectSplitters(data, k, p);
		mainCode.toggleHighlight(1, 2);
		List<ListBasedQueue<Integer>> buckets = generateBuckets(p);
		mainCode.toggleHighlight(2, 3);
		seperateDataIntoBuckets(data, buckets, splitters);
		mainCode.toggleHighlight(3, 4);
		return sortAndConcatenateBuckets(buckets, data.getLength(), data);

	}

	/**
	 * selecting k*(p-1) samples evenly from data. From those p-1 splitters are
	 * evenly picked
	 * 
	 * @param data
	 * @param k
	 *            oversampling factor
	 * @param p
	 *            number of buckets
	 * @return sorted array of the p-1 splitters and INF as last value for easier
	 *         use as bucket boundaries
	 */
	private IntArray selectSplitters(IntArray data, int k, int p) {
		// first, set the visual properties (somewhat similar to CSS)
		// for source Code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode samplesCode = lang.newSourceCode(new Coordinates(40, 300), "sourceCode", null, scProps);

		// for arrays
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

		// for text
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		samplesCode.addCodeLine("private int[] selectSamples(int[] data, int k, int p) {", null, 0, null); // 0
		samplesCode.addCodeLine("int nrOfSamples = k * (p - 1);", null, 1, null);// 1
		samplesCode.addCodeLine("int[] samples = new int[nrOfSamples];", null, 1, null); // 2
		samplesCode.addCodeLine("Random rn = new Random();", null, 1, null); // 3
		samplesCode.addCodeLine("for (int i = 0; i < nrOfSamples; i++) {", null, 1, null); // 4
		samplesCode.addCodeLine("samples[i] = data[rn.nextInt(data.length)];", null, 2, null); // 5
		samplesCode.addCodeLine("}", null, 1, null); // 6
		samplesCode.addCodeLine("int[] splitters = new int[p];", null, 1, null); // 7
		samplesCode.addCodeLine("for (int i = 1; i < p; i++) {", null, 1, null);// 8
		samplesCode.addCodeLine("splitters[i - 1] = samples[k * i - 1];", null, 2, null); // 9
		samplesCode.addCodeLine("}", null, 1, null); // 10
		samplesCode.addCodeLine("splitters[splitters.length - 1] = Integer.MAX_VALUE;", null, 1, null); // 11
		samplesCode.addCodeLine("Arrays.sort(splitters);", null, 1, null); // 12
		samplesCode.addCodeLine("return splitters;", null, 1, null); // 13
		samplesCode.addCodeLine("}", null, 0, null); // 14
		lang.nextStep("getSamples: show code");

		// make and hide array splitters
		int[] inputSplitters = new int[p];
		Text splittersDesc = lang.newText(new Offset(0, 60, "pText", AnimalScript.DIRECTION_NW), "Splitters: ",
				"splittersDesc", null, textProps);
		IntArray splitters = lang.newIntArray(new Offset(100, 0, "splittersDesc", AnimalScript.DIRECTION_NW),
				inputSplitters, "splittersArray", null, arrayProps);
		splittersDesc.hide();
		splitters.hide();
		lang.nextStep();

		// make and show array samples
		samplesCode.highlight(1);
		int nrOfSamples = k * (p - 1);
		Text nrOfSamplesText = lang.newText(new Offset(0, 50, "splittersDesc", AnimalScript.DIRECTION_NW),
				"Nr of samples: " + nrOfSamples, "nrOfSamples", null, textProps);
		lang.nextStep();

		int[] inputSamples = new int[nrOfSamples];

		samplesCode.toggleHighlight(1, 2);
		Text samplesDesc = lang.newText(new Offset(0, 60, "nrOfSamples", AnimalScript.DIRECTION_NW), "Samples: ",
				"samplesDesc", null, textProps);
		IntArray samples = lang.newIntArray(new Offset(100, 0, "samplesDesc", AnimalScript.DIRECTION_NW), inputSamples,
				"samplesArray", null, arrayProps);
		lang.nextStep();

		// fill samples
		Random rn = new Random();
		samplesCode.toggleHighlight(2, 3);
		Text random = lang.newText(new Offset(0, 50, "samplesDesc", AnimalScript.DIRECTION_NW),
				"pick random sample from position: ", "random", null, textProps);
		int randomPos = 0;
		lang.nextStep("getSamples: prep done");
		samplesCode.toggleHighlight(3, 5);

		// 2.Frage
		TrueFalseQuestionModel howSampling = new TrueFalseQuestionModel("howSampling");
		howSampling.setPrompt("Must we sample randomly?");
		howSampling.setPointsPossible(1);
		howSampling.setCorrectAnswer(false);
		lang.addTFQuestion(howSampling);
		/////////////////

		// make pointer at samples where you pick data from
		ArrayMarkerProperties dataPointerProp = new ArrayMarkerProperties();
		dataPointerProp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		dataPointerProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		ArrayMarker dataPointer = lang.newArrayMarker(data, 0, "i", null, dataPointerProp);

		for (int i = 0; i < nrOfSamples; i++) {
			randomPos = rn.nextInt(data.getLength());
			dataPointer.move(randomPos, null, null);
			random.setText("pick random sample from position: " + randomPos, null, null);
			samples.put(i, data.getData(dataPointer.getPosition()), null, null);
			samples.highlightElem(i, null, null);
			samples.setTextColor(i, Color.black, null, null);
			lang.nextStep();
			samples.unhighlightElem(i, null, null);

		}
		random.hide();
		dataPointer.hide();
		// show array splitters
		samplesCode.toggleHighlight(5, 7);
		splittersDesc.show();
		splitters.show();

		// make pointer at samples where you pick data from
		ArrayMarkerProperties samplesPointerProp = new ArrayMarkerProperties();
		samplesPointerProp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		samplesPointerProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		ArrayMarker samplesPointer = lang.newArrayMarker(samples, 0, "i", null, samplesPointerProp);

		samplesCode.toggleHighlight(7, 9);
		// fill splitters array
		for (int i = 0; i < p - 1; i++) {
			samplesPointer.move(k * i, null, null);
			splitters.put(i, samples.getData(samplesPointer.getPosition()), null, null);
			splitters.highlightElem(i, null, null);
			splitters.setTextColor(i, Color.black, null, null);
			lang.nextStep();
			splitters.unhighlightElem(i, null, null);
		}
		samplesPointer.hide();
		samplesCode.toggleHighlight(9, 11);
		splitters.highlightElem(splitters.getLength() - 1, null, null);
		splitters.put(splitters.getLength() - 1, Integer.MAX_VALUE, null, null);
		splitters.setTextColor(splitters.getLength() - 1, Color.black, null, null);
		lang.nextStep("getSamples: samples selected and splitters array filled");

		// 3. Frage
		MultipleChoiceQuestionModel whyMaxValue = new MultipleChoiceQuestionModel("whyMaxValue");
		whyMaxValue.setPrompt("Why do we add max_Value to the splitters array?");
		whyMaxValue.addAnswer("no reason", 0, " -> this is not right");
		whyMaxValue.addAnswer(
				"it is the upper border for the bucket of greatest numbers in data. Using max value guarantees that all numbers can be accomodated",
				1, " -> correct");
		whyMaxValue.addAnswer("to make sure we don't put to small values into the bucket", 0,
				" -> not right, The values from data will be put into the first bucket with a upper bound equal or higher to the value");
		lang.addMCQuestion(whyMaxValue);
		/////////////////

		splitters.unhighlightElem(splitters.getLength() - 1, null, null);
		// sort splitters
		samplesCode.toggleHighlight(11, 12);
		splitters = sortIntArray(splitters);
		lang.nextStep();

		// 4. Frage
		MultipleChoiceQuestionModel notSorting = new MultipleChoiceQuestionModel("notSorting");
		notSorting.setPrompt("What would happen if splitters was not sorted?");
		notSorting.addAnswer("Nothing. Why?", 0, " -> this is not right");
		notSorting.addAnswer("the algorithm would not terminate", 0,
				" -> not right. It would terminate just not correctly");
		notSorting.addAnswer("The data would not be sorted correctly", 1, " -> correct!");
		lang.addMCQuestion(notSorting);
		/////////////////

		// cleanup
		samplesCode.hide();
		samples.hide();
		samplesDesc.hide();
		nrOfSamplesText.hide();
		lang.nextStep("getSamples: done");
		return splitters;
	}

	/**
	 * generates a List of buckets
	 * 
	 * @param n
	 *            number of buckets
	 * @return
	 */
  @SuppressWarnings({ "rawtypes", "unchecked" })
	private List<ListBasedQueue<Integer>> generateBuckets(int n) {
		// draw code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode generateBucketsCode = lang.newSourceCode(new Coordinates(40, 300), "GenerateBucketsCode", null,
				scProps);

		generateBucketsCode.addCodeLine("private List<List<Integer>> generateBuckets(int n) {", null, 0, null); // 0
		generateBucketsCode.addCodeLine("List<List<Integer>> buckets = new ArrayList<>(n);", null, 1, null);// 1
		generateBucketsCode.addCodeLine("for (int i = 0; i < n; i++)", null, 1, null); // 2
		generateBucketsCode.addCodeLine("buckets.add(new ArrayList<>());", null, 2, null); // 3
		generateBucketsCode.addCodeLine("return buckets;", null, 1, null); // 4
		generateBucketsCode.addCodeLine("}", null, 1, null); // 5
		lang.nextStep("generateBuckets: show code");

		List<ListBasedQueue<Integer>> buckets = new ArrayList<>(n);
		// draw rect props
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("0xb2eeff"));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		// draw array
		generateBucketsCode.highlight(1);
		Text bucktesDesc = lang.newText(new Coordinates(600, 300), "Buckets: ", "bucketsDesc", null, textProps);
		for (int i = 0; i < n; i++) {
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			lang.newRect(new Coordinates(700, 300 + 60 * i), new Coordinates(760, 360 + 60 * i), "Bucket" + i, null,
					rectProps);
			lang.newText(new Offset(-10, 30, "Bucket" + i, "NW"), "" + i, "bucketDesc" + i, null, textProps);
		}
		lang.nextStep();

		// draw actual buckets
		generateBucketsCode.toggleHighlight(1, 3);
		QueueProperties queueProps = new QueueProperties();
		queueProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);// bugfix
		queueProps.set(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY, false);

		// lang.addError("hier behindert uns der Queue-Bug");
		for (int i = 0; i < n; i++) {
			ListBasedQueue queue = lang.newListBasedQueue(new Offset(5, 5, "Bucket" + i, "NW"), null, "listBucket" + i,
					null, queueProps);
			queue.setAutoSteps(false);
			buckets.add(queue);
			queue.enqueue(""); // workaround um den rect-Bug
			lang.nextStep();
		}
		lang.nextStep("generateBuckets: done");
		generateBucketsCode.hide();
		return buckets;
	}

	/**
	 * seperates the given data into the buckets while using the array boundaries to
	 * determine which data goes into which bucket
	 * 
	 * @param data
	 *            to be sorted into buckets
	 * @param buckets
	 *            to be filled
	 * @param samples
	 *            of the bucket (elements of bucket[i] are <= boundaries[i])
	 * @return
	 */
	private List<ListBasedQueue<Integer>> seperateDataIntoBuckets(IntArray data, List<ListBasedQueue<Integer>> buckets,
			IntArray splitters) {
		// show code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode dataIntoBucketsCode = lang.newSourceCode(new Coordinates(40, 300), "dataIntoBucketsCode", null,
				scProps);

		dataIntoBucketsCode.addCodeLine("private List<List<Integer>> seperateDataIntoBuckets(", null, 1, null);// 1
		dataIntoBucketsCode.addCodeLine("int[] data, List<List<Integer>> buckets,int[] boundaries) {", null, 2, null);// 1
		dataIntoBucketsCode.addCodeLine("for (int d : data) {", "1", 1, null);// 1
		dataIntoBucketsCode.addCodeLine("for (int j = 0; j < buckets.size(); j++) {", "2", 2, null); // 2
		dataIntoBucketsCode.addCodeLine("if (d <= boundaries[j]) {", null, 3, null); // 3
		dataIntoBucketsCode.addCodeLine("buckets.get(j).add(d);", "4", 4, null); // 4
		dataIntoBucketsCode.addCodeLine("break;", null, 4, null); // 5
		dataIntoBucketsCode.addCodeLine("};", null, 3, null); // 6
		dataIntoBucketsCode.addCodeLine("};", null, 2, null); // 7
		dataIntoBucketsCode.addCodeLine("};", null, 1, null); // 8
		dataIntoBucketsCode.addCodeLine("return buckets;", null, 1, null); // 9
		dataIntoBucketsCode.addCodeLine("};", null, 0, null); // 10
		lang.nextStep("seperateDataIntoBuckets: show code");

		// pointer auf data
		dataIntoBucketsCode.highlight("1");
		ArrayMarkerProperties dataPointerProp = new ArrayMarkerProperties();
		dataPointerProp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		dataPointerProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		// pointer für buckets
		dataIntoBucketsCode.toggleHighlight("2");
		ArrayMarkerProperties splittersPointerProp = new ArrayMarkerProperties();
		splittersPointerProp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		splittersPointerProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		ArrayMarker splittersPointer = lang.newArrayMarker(splitters, 0, "j", null, splittersPointerProp);
		for (ArrayMarker dataPointer = lang.newArrayMarker(data, 0, "i", null, dataPointerProp); dataPointer
				.getPosition() < data.getLength(); dataPointer.increment(null, null)) {
			dataIntoBucketsCode.toggleHighlight("1"); // wieder in den äußeren Loop
			splittersPointer.move(0, null, null); // splitters Pointer wieder auf 0
			lang.nextStep();
			for (splittersPointer.getPosition(); splittersPointer.getPosition() < buckets.size(); splittersPointer
					.increment(null, null)) {
				dataIntoBucketsCode.toggleHighlight("2");
				if (data.getData(dataPointer.getPosition()) <= splitters.getData(splittersPointer.getPosition())) {
					dataIntoBucketsCode.toggleHighlight("4");
					buckets.get(splittersPointer.getPosition()).enqueue(data.getData(dataPointer.getPosition())); // data
																													// into
																													// bucket
					data.setTextColor(dataPointer.getPosition(), Color.white, null, null); // hide data in data :)
					if (dataPointer.getPosition() == data.getLength() - 1) {// hide pointer when done :)
						dataPointer.hide();
						splittersPointer.hide();
					}
					lang.nextStep();
					break;
				}
				lang.nextStep();
			}
		}
		// cleanup
		dataIntoBucketsCode.hide();
		return buckets;
	}

	/**
	 * sorts the buckets that are not empty and fills their data into the data array
	 * after
	 * 
	 * @param buckets
	 *            list of buckets
	 * @param length
	 *            length of array to be returned
	 * @return sorted array of data in buckets
	 */
	private IntArray sortAndConcatenateBuckets(List<ListBasedQueue<Integer>> buckets, int length, IntArray data) {
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sortAndConcatenateBucketsCode = lang.newSourceCode(new Coordinates(40, 300),
				"SortAndCocatenateBucketsCode", null, scProps);

		ArrayMarkerProperties dataPointerProp = new ArrayMarkerProperties();
		dataPointerProp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		dataPointerProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

		sortAndConcatenateBucketsCode.addCodeLine("private static int[] sortAndConcatenateBuckets", null, 1, null);// 1
		sortAndConcatenateBucketsCode.addCodeLine("(List<List<Integer>> buckets, int length) {", null, 2, null);// 1
		sortAndConcatenateBucketsCode.addCodeLine("int[] result = new int[length];", "1", 1, null);// 1
		sortAndConcatenateBucketsCode.addCodeLine("int j = 0;", "2", 1, null); // 2
		sortAndConcatenateBucketsCode.addCodeLine("for (List<Integer> b : buckets) {", "3", 1, null); // 3
		sortAndConcatenateBucketsCode.addCodeLine("if (!b.isEmpty()) {", "4", 2, null); // 4
		sortAndConcatenateBucketsCode.addCodeLine("Collections.sort(b);", "5", 3, null); // 5
		sortAndConcatenateBucketsCode.addCodeLine("for (int i = 0; i < b.size(); i++) {", "6", 3, null); // 6
		sortAndConcatenateBucketsCode.addCodeLine("result[j++] = b.get(i);", "7", 4, null); // 7
		sortAndConcatenateBucketsCode.addCodeLine("}", null, 3, null); // 8
		sortAndConcatenateBucketsCode.addCodeLine("}", null, 2, null); // 9
		sortAndConcatenateBucketsCode.addCodeLine("}", null, 1, null); // 10
		sortAndConcatenateBucketsCode.addCodeLine("return result", null, 1, null); // 11
		sortAndConcatenateBucketsCode.addCodeLine("}", null, 0, null); // 12

		lang.nextStep("sortAndConcatenateBuckets: show code");
		int i = 0;
		ArrayMarker dataPointer = lang.newArrayMarker(data, 0, "j", null, dataPointerProp);
		for (ListBasedQueue<Integer> b : buckets) {
			sortAndConcatenateBucketsCode.toggleHighlight("3");
			// b.changeColor("cellhighlight", Color.decode("0x00c8ff"), null, null);
			// was muss denn da statt cellhighlight hin?
			lang.nextStep();
			b = sortQueue(b, buckets, i, sortAndConcatenateBucketsCode);
			b.hide();
			while (!b.isEmpty()) {
				sortAndConcatenateBucketsCode.toggleHighlight("7");
				data.put(dataPointer.getPosition(), b.dequeue(), null, null);
				data.setTextColor(dataPointer.getPosition(), Color.black, null, null);
				dataPointer.increment(null, null);
			}
			i++;
		}
		lang.nextStep("sortAndConcatenateBuckets: all buckets sorted and data filled again");
		return data;
	}

	private IntArray sortIntArray(IntArray splitters) {
		int[] a = new int[splitters.getLength()];
		for (int i = 0; i < splitters.getLength(); i++) {
			a[i] = splitters.getData(i);
		}
		Arrays.sort(a);
		for (int i = 0; i < splitters.getLength(); i++) {
			splitters.put(i, a[i], null, null);
		}
		return splitters;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
  private ListBasedQueue<Integer> sortQueue(ListBasedQueue<Integer> b, List<ListBasedQueue<Integer>> buckets, int i,
			SourceCode sc) {
		sc.toggleHighlight("4");
		lang.nextStep();
		b.hide();
		b.dequeue();
		if (b.isEmpty())
			return b;// no work on empty queues
		QueueProperties props = b.getProperties();
		List<Integer> tmp = new ArrayList();
		// queue in sortable
		while (!b.isEmpty()) {
			tmp.add(b.dequeue());
			// lang.nextStep("sortQueue: dequeuing to sort");
		}
		// sort
		sc.toggleHighlight("5");
		Collections.sort(tmp);
		// make new queue (workaround jumps in queue nodes)
		ListBasedQueue queue = lang.newListBasedQueue(new Offset(5, 5, "Bucket" + i, "NW"), null, "listBucket" + i,
				null, props);
		queue.setAutoSteps(false);
		queue.hide();
		// sorted stuff back into new queue
		for (int d : tmp) {
			queue.enqueue(d);
		}
		queue.show();
		buckets.set(i, queue);// replace unsorted queue with new queue
		lang.nextStep();
		sc.toggleHighlight("7");
		queue.hide();
		lang.nextStep();
		return queue;
	}

}